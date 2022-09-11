package me.conclure.concluresdinomod.ai.goal;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DinoGoalSelector {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final WrappedDinoGoal NO_GOAL = new WrappedDinoGoal(Integer.MAX_VALUE, new DinoGoal(){

        @Override
        public boolean canUse() {
            return false;
        }
    }){

        @Override
        public boolean isRunning() {
            return false;
        }
    };
    /**
     * Goals currently using a particular flag
     */
    private final Map<DinoGoalFlag, WrappedDinoGoal> lockedFlags = Maps.newIdentityHashMap();
    private final Set<WrappedDinoGoal> availableGoals = Sets.newLinkedHashSet();
    private final Supplier<ProfilerFiller> profiler;
    private final Set<DinoGoalFlag> disabledFlags = Sets.newIdentityHashSet();
    private int tickCount;
    private int newGoalRate = 3;

    public DinoGoalSelector(Supplier<ProfilerFiller> supplier) {
        this.profiler = supplier;
    }

    /**
     * Add a goal to the GoalSelector with a certain priority. Lower numbers are higher priority.
     */
    public void addGoal(int priority, DinoGoal goal) {
        this.availableGoals.add(new WrappedDinoGoal(priority, goal));
    }

    @VisibleForTesting
    public void removeAllGoals() {
        this.availableGoals.clear();
    }

    /**
     * Remove the goal from the GoalSelector. This must be the same object as the goal you are trying to remove, which may not always be accessible.
     */
    public void removeGoal(DinoGoal goal) {
        this.availableGoals.stream()
                .filter(wrappedGoal -> wrappedGoal.getGoal() == goal)
                .filter(WrappedDinoGoal::isRunning)
                .forEach(WrappedDinoGoal::stop);
        this.availableGoals.removeIf(wrappedGoal -> wrappedGoal.getGoal() == goal);
    }

    private static boolean goalContainsAnyFlags(WrappedDinoGoal wrappedGoal, Set<DinoGoalFlag> enumSet) {
        for (DinoGoalFlag flag : wrappedGoal.getFlags()) {
            if (!enumSet.contains(flag)) continue;
            return true;
        }
        return false;
    }

    private static boolean goalCanBeReplacedForAllFlags(WrappedDinoGoal wrappedGoal, Map<DinoGoalFlag, WrappedDinoGoal> map) {
        for (DinoGoalFlag flag : wrappedGoal.getFlags()) {
            if (map.getOrDefault(flag, NO_GOAL).canBeReplacedBy(wrappedGoal)) continue;
            return false;
        }
        return true;
    }

    /**
     * Ticks every goal in the selector.
     * Attempts to start each goal based on if it can be used, or stop it if it can't.
     */
    public void tick() {
        ProfilerFiller profilerFiller = this.profiler.get();
        profilerFiller.push("dinoGoalCleanup");
        for (WrappedDinoGoal wrappedGoal : this.availableGoals) {
            if (!wrappedGoal.isRunning() || !DinoGoalSelector.goalContainsAnyFlags(wrappedGoal, this.disabledFlags) && wrappedGoal.canContinueToUse()) continue;
            wrappedGoal.stop();
        }
        Iterator<Map.Entry<DinoGoalFlag, WrappedDinoGoal>> iterator = this.lockedFlags.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<DinoGoalFlag, WrappedDinoGoal> entry = iterator.next();
            if (entry.getValue().isRunning()) continue;
            iterator.remove();
        }
        profilerFiller.pop();
        profilerFiller.push("dinoGoalUpdate");
        for (WrappedDinoGoal wrappedGoal : this.availableGoals) {
            if (wrappedGoal.isRunning() || DinoGoalSelector.goalContainsAnyFlags(wrappedGoal, this.disabledFlags) || !DinoGoalSelector.goalCanBeReplacedForAllFlags(wrappedGoal, this.lockedFlags) || !wrappedGoal.canUse()) continue;
            for (DinoGoalFlag flag : wrappedGoal.getFlags()) {
                WrappedDinoGoal wrappedGoal2 = this.lockedFlags.getOrDefault(flag, NO_GOAL);
                wrappedGoal2.stop();
                this.lockedFlags.put(flag, wrappedGoal);
            }
            wrappedGoal.start();
        }
        profilerFiller.pop();
        this.tickRunningGoals(true);
    }

    public void tickRunningGoals(boolean bl) {
        ProfilerFiller profilerFiller = this.profiler.get();
        profilerFiller.push("dinoGoalTick");
        for (WrappedDinoGoal wrappedGoal : this.availableGoals) {
            if (!wrappedGoal.isRunning() || !bl && !wrappedGoal.requiresUpdateEveryTick()) continue;
            wrappedGoal.tick();
        }
        profilerFiller.pop();
    }

    public Set<WrappedDinoGoal> getAvailableGoals() {
        return this.availableGoals;
    }

    public Stream<WrappedDinoGoal> getRunningGoals() {
        return this.availableGoals.stream()
                .filter(WrappedDinoGoal::isRunning);
    }

    public void setNewGoalRate(int newGoalRate) {
        this.newGoalRate = newGoalRate;
    }

    public void disableControlFlag(DinoGoalFlag flag) {
        this.disabledFlags.add(flag);
    }

    public void enableControlFlag(DinoGoalFlag flag) {
        this.disabledFlags.remove(flag);
    }

    public void setControlFlag(DinoGoalFlag flag, boolean enabled) {
        if (enabled) {
            this.enableControlFlag(flag);
        } else {
            this.disableControlFlag(flag);
        }
    }
}

package me.conclure.concluresdinomod.ai.goal;

import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class WrappedDinoGoal extends DinoGoal {
    private final DinoGoal goal;
    private final int priority;
    private boolean isRunning;

    public WrappedDinoGoal(int i, DinoGoal goal) {
        this.priority = i;
        this.goal = goal;
    }

    public boolean canBeReplacedBy(WrappedDinoGoal other) {
        return this.isInterruptable() && other.getPriority() < this.getPriority();
    }

    @Override
    public boolean canUse() {
        return this.goal.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return this.goal.canContinueToUse();
    }

    @Override
    public boolean isInterruptable() {
        return this.goal.isInterruptable();
    }

    @Override
    public void start() {
        if (this.isRunning) {
            return;
        }
        this.isRunning = true;
        this.goal.start();
    }

    @Override
    public void stop() {
        if (!this.isRunning) {
            return;
        }
        this.isRunning = false;
        this.goal.stop();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return this.goal.requiresUpdateEveryTick();
    }

    @Override
    protected int adjustedTickDelay(int i) {
        return this.goal.adjustedTickDelay(i);
    }

    @Override
    public void tick() {
        this.goal.tick();
    }

    @Override
    public void setFlags(Set<DinoGoalFlag> flagSet) {
        this.goal.setFlags(flagSet);
    }

    @Override
    public Set<DinoGoalFlag> getFlags() {
        return this.goal.getFlags();
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public int getPriority() {
        return this.priority;
    }

    public DinoGoal getGoal() {
        return this.goal;
    }

    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        return this.goal.equals(((WrappedDinoGoal)object).goal);
    }

    public int hashCode() {
        return this.goal.hashCode();
    }
}

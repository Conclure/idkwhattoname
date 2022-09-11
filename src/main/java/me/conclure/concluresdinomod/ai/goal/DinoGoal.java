package me.conclure.concluresdinomod.ai.goal;

import com.google.common.collect.Sets;
import net.minecraft.util.Mth;

import java.util.Set;

public abstract class DinoGoal {
    private final Set<DinoGoalFlag> flags = Sets.newIdentityHashSet();

    public abstract boolean canUse();

    public boolean canContinueToUse() {
        return this.canUse();
    }

    public boolean isInterruptable() {
        return true;
    }

    public void start() {
    }

    public void stop() {
    }

    public boolean requiresUpdateEveryTick() {
        return false;
    }

    public void tick() {
    }

    public void setFlags(Set<DinoGoalFlag> flagSet) {
        this.flags.clear();
        this.flags.addAll(flagSet);
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }

    public Set<DinoGoalFlag> getFlags() {
        return this.flags;
    }

    protected int adjustedTickDelay(int i) {
        return this.requiresUpdateEveryTick() ? i : DinoGoal.reducedTickDelay(i);
    }

    protected static int reducedTickDelay(int i) {
        return Mth.positiveCeilDiv(i, 2);
    }

    public static DinoGoalFlag createFlag() {
        return new DinoGoalFlag.FlagInstance();
    }

}

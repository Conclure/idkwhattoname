package me.conclure.concluresdinomod.ai.goal;

public sealed interface DinoGoalFlag permits DinoGoalFlag.FlagInstance {
    DinoGoalFlag MOVE = DinoGoal.createFlag();
    DinoGoalFlag LOOK = DinoGoal.createFlag();
    DinoGoalFlag TARGET = DinoGoal.createFlag();
    DinoGoalFlag JUMP = DinoGoal.createFlag();
    DinoGoalFlag ANIMATION = DinoGoal.createFlag();

    final class FlagInstance implements DinoGoalFlag {
        FlagInstance() {
        }
    }
}

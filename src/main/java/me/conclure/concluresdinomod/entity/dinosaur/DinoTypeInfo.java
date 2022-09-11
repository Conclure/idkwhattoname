package me.conclure.concluresdinomod.entity.dinosaur;

import me.conclure.concluresdinomod.ActivityType;

public record DinoTypeInfo(
        DinoStage adultStage,
        int flockSize,
        ActivityType activityType,
        DinoStage babyStage
) {
    public static DinosaurTypeInfoBuilder builder() {
        return new DinosaurTypeInfoBuilder();
    }
}
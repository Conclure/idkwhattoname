package me.conclure.concluresdinomod.entity.dinosaur;

import me.conclure.concluresdinomod.ActivityType;

public class DinosaurTypeInfoBuilder {
    private DinoStage adultStage;
    private DinoStage babyStage;
    private ActivityType activityType;
    private int flockSize = 1;

    public DinosaurTypeInfoBuilder adultStage(DinoStage stage) {
        this.adultStage = stage;
        return this;
    }

    public DinosaurTypeInfoBuilder babyStage(DinoStage stage) {
        this.babyStage = stage;
        return this;
    }

    public DinosaurTypeInfoBuilder activityType(ActivityType activityType) {
        this.activityType = activityType;
        return this;
    }

    public DinosaurTypeInfoBuilder flockSize(int flockSize) {
        this.flockSize = flockSize;
        return this;
    }

    public DinoTypeInfo build() {
        return new DinoTypeInfo(this.adultStage, this.flockSize, this.activityType, this.babyStage);
    }
}
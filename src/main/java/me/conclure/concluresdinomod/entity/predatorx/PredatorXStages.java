package me.conclure.concluresdinomod.entity.predatorx;

import me.conclure.concluresdinomod.entity.dinosaur.DinoStage;

public interface PredatorXStages {
    DinoStage ADULT = DinoStage.builder()
            .height(1)
            .width(10f)
            .modelResource("geo/predator_x_adult.geo.json")
            .textureResource("textures/predator_x_adult.png")
            .animationResource("animations/predator_x_adult.animation.json")
            .build();
}

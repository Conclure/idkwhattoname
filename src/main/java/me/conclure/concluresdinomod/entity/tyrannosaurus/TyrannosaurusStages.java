package me.conclure.concluresdinomod.entity.tyrannosaurus;

import me.conclure.concluresdinomod.entity.dinosaur.DinoStage;

public interface TyrannosaurusStages {
    DinoStage ADULT = DinoStage.builder()
            .height(5)
            .width(5)
            .modelResource("geo/tyrannosaurus_adult.geo.json")
            .textureResource("textures/tyrannosaurus_adult.png")
            .animationResource("animations/tyrannosaurus_adult.animation.json")
            .build();
}

package me.conclure.concluresdinomod.entity.triceratops;

import me.conclure.concluresdinomod.util.Ticks;
import me.conclure.concluresdinomod.entity.dinosaur.DinoStage;
import me.conclure.concluresdinomod.entity.dinosaur.PreadultDinoStage;

public sealed interface TriceratopsStages permits TriceratopsStages.Nothing {
    final class Nothing implements TriceratopsStages {
        private Nothing() {}
    }

    DinoStage ADULT = DinoStage.builder()
            .height(4)
            .width(5)
            .modelResource("geo/triceratops_adult.geo.json")
            .textureResource("textures/triceratops_adult.png")
            .animationResource("animations/triceratops_adult.animations.json")
            .build();

    PreadultDinoStage BABY = PreadultDinoStage.builder()
            .height(.25f)
            .width(.25f)
            .totalAgeTicks(Ticks.fromMinutes(10))
            .nextStage(ADULT)
            .modelResource("geo/triceratops_baby.geo.json")
            .textureResource("textures/triceratops_baby.png")
            .animationResource("animations/triceratops_baby.animations.json")
            .build();
}

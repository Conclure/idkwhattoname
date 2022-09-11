package me.conclure.concluresdinomod.entity.velociraptor;

import me.conclure.concluresdinomod.util.Ticks;
import me.conclure.concluresdinomod.entity.dinosaur.DinoStage;
import me.conclure.concluresdinomod.entity.dinosaur.PreadultDinoStage;

public sealed interface VelociraptorStages permits VelociraptorStages.Nothing {
    final class Nothing implements VelociraptorStages {
        private Nothing() {}
    }

    DinoStage ADULT = DinoStage.builder()
            .height(1)
            .width(1f)
            .modelResource("geo/velociraptor_adult.geo.json")
            .textureResource("textures/velociraptor_adult.png")
            .animationResource("animations/velociraptor_adult.animation.json")
            .build();
    PreadultDinoStage BABY = PreadultDinoStage.builder()
            .nextStage(ADULT)
            .height(.5f)
            .width(.3f)
            .modelResource("geo/velociraptor_baby.geo.json")
            .textureResource("textures/velociraptor_baby.png")
            .animationResource("animations/velociraptor_baby.animation.json")
            .totalAgeTicks(Ticks.fromMinutes(5))
            .build();
}
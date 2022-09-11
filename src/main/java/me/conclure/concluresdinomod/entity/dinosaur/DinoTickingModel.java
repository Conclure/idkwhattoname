package me.conclure.concluresdinomod.entity.dinosaur;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;

public class DinoTickingModel<T extends DinoEntity<T> & IAnimationTickable> extends AnimatedTickingGeoModel<T> {
    private final static DinoTickingModel<?> INSTANCE = new DinoTickingModel<>();

    public static <T extends DinoEntity<T> & IAnimationTickable> DinoTickingModel<T> getInstance() {
        return (DinoTickingModel<T>) INSTANCE;
    }

    @Override
    public ResourceLocation getModelResource(T object) {
        return object.getCurrentStage().getModelResource();
    }

    @Override
    public ResourceLocation getTextureResource(T object) {
        return object.getCurrentStage().getTextureResource();
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return animatable.getCurrentStage().getAnimationResource();
    }
}

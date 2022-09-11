package me.conclure.concluresdinomod.entity.dinosaur;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;

public interface DinoStage {

    static StandardDinoStageBuilder builder() {
        return new StandardDinoStageBuilder();
    }

    float getWidth();

    float getHeight();

    default EntityDimensions dimensions() {
        return EntityDimensions.scalable(this.getWidth(),this.getHeight());
    }

    ResourceLocation getModelResource();

    ResourceLocation getTextureResource();

    ResourceLocation getAnimationResource();

    String getSkullGroup();
}

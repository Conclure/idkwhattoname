package me.conclure.concluresdinomod.entity.dinosaur;

import me.conclure.concluresdinomod.ExampleMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;

public class StandardDinoStage implements DinoStage {
    private final float width;
    private final float height;
    private final ResourceLocation modelResource;
    private final ResourceLocation textureResource;
    private final ResourceLocation animationResource;
    private final String skullGroup;

    public StandardDinoStage(float width, float height, ResourceLocation modelResource, ResourceLocation textureResource, ResourceLocation animationResource, String skullGroup) {
        this.width = width;
        this.height = height;
        this.modelResource = modelResource;
        this.textureResource = textureResource;
        this.animationResource = animationResource;
        this.skullGroup = skullGroup;
    }

    @Override
    public float getWidth() {
        return this.width;
    }

    @Override
    public float getHeight() {
        return this.height;
    }

    @Override
    public EntityDimensions dimensions() {
        return EntityDimensions.scalable(this.width, this.height);
    }

    @Override
    public ResourceLocation getModelResource() {
        return this.modelResource;
    }

    @Override
    public ResourceLocation getTextureResource() {
        return this.textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource() {
        return this.animationResource;
    }

    @Override
    public String getSkullGroup() {
        return this.skullGroup;
    }

}

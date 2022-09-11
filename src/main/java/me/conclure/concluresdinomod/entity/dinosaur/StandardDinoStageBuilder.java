package me.conclure.concluresdinomod.entity.dinosaur;

import me.conclure.concluresdinomod.ExampleMod;
import net.minecraft.resources.ResourceLocation;

public class StandardDinoStageBuilder {
    protected float width;
    protected float height;
    protected ResourceLocation modelResource;
    protected ResourceLocation textureResource;
    protected ResourceLocation animationResource;
    protected String skullGroup = "skullGroup";

    public StandardDinoStageBuilder skullGroup(String skullGroup) {
        this.skullGroup = skullGroup;
        return this;
    }

    public StandardDinoStageBuilder width(float width) {
        this.width = width;
        return this;
    }

    public StandardDinoStageBuilder height(float height) {
        this.height = height;
        return this;
    }

    public StandardDinoStageBuilder animationResource(ResourceLocation animationResource) {
        this.animationResource = animationResource;
        return this;
    }

    public StandardDinoStageBuilder animationResource(String location) {
        this.animationResource = new ResourceLocation(ExampleMod.ID, location);
        return this;
    }

    public StandardDinoStageBuilder modelResource(ResourceLocation modelResource) {
        this.modelResource = modelResource;
        return this;
    }

    public StandardDinoStageBuilder modelResource(String location) {
        this.modelResource = new ResourceLocation(ExampleMod.ID, location);
        return this;
    }

    public StandardDinoStageBuilder textureResource(ResourceLocation textureResource) {
        this.textureResource = textureResource;
        return this;
    }

    public StandardDinoStageBuilder textureResource(String location) {
        this.textureResource = new ResourceLocation(ExampleMod.ID, location);
        return this;
    }

    public DinoStage build() {
        return new StandardDinoStage(
                this.width,
                this.height,
                this.modelResource,
                this.textureResource,
                this.animationResource,
                this.skullGroup);
    }
}

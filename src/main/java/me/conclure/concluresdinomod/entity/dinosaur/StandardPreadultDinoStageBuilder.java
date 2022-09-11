package me.conclure.concluresdinomod.entity.dinosaur;

import net.minecraft.resources.ResourceLocation;

public class StandardPreadultDinoStageBuilder extends StandardDinoStageBuilder {
    private int totalAgeTicks;
    private DinoStage nextStage;

    @Override
    public StandardPreadultDinoStageBuilder skullGroup(String skullGroup) {
        super.skullGroup(skullGroup);
        return this;
    }

    public StandardPreadultDinoStageBuilder nextStage(DinoStage nextStage) {
        this.nextStage = nextStage;
        return this;
    }

    public StandardPreadultDinoStageBuilder totalAgeTicks(int totalAgeTicks) {
        this.totalAgeTicks = totalAgeTicks;
        return this;
    }

    @Override
    public StandardPreadultDinoStageBuilder width(float width) {
        super.width(width);
        return this;
    }

    @Override
    public StandardPreadultDinoStageBuilder height(float height) {
        super.height(height);
        return this;
    }

    @Override
    public StandardPreadultDinoStageBuilder animationResource(ResourceLocation animationResource) {
        super.animationResource(animationResource);
        return this;
    }

    @Override
    public StandardPreadultDinoStageBuilder animationResource(String location) {
        super.animationResource(location);
        return this;
    }

    @Override
    public StandardPreadultDinoStageBuilder modelResource(ResourceLocation modelResource) {
        super.modelResource(modelResource);
        return this;
    }

    @Override
    public StandardPreadultDinoStageBuilder modelResource(String location) {
        super.modelResource(location);
        return this;
    }

    @Override
    public StandardPreadultDinoStageBuilder textureResource(ResourceLocation textureResource) {
        super.textureResource(textureResource);
        return this;
    }

    @Override
    public StandardPreadultDinoStageBuilder textureResource(String location) {
        super.textureResource(location);
        return this;
    }

    @Override
    public PreadultDinoStage build() {
        return new StandardPreadultDinoStage(
                this.width,
                this.height,
                this.modelResource,
                this.textureResource,
                this.animationResource,
                this.totalAgeTicks,
                this.skullGroup,
                this.nextStage
        );
    }
}

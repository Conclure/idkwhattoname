package me.conclure.concluresdinomod.entity.dinosaur;

import net.minecraft.resources.ResourceLocation;

public class StandardPreadultDinoStage extends StandardDinoStage implements PreadultDinoStage {
    private final int totalAgeTicks;
    private final DinoStage nextStage;

    public StandardPreadultDinoStage(
            float width,
            float height,
            ResourceLocation modelResource,
            ResourceLocation textureResource,
            ResourceLocation animationResource,
            int totalAgeTicks,
            String skullGroup,
            DinoStage nextStage) {
        super(width, height, modelResource, textureResource, animationResource, skullGroup);
        this.totalAgeTicks = totalAgeTicks;
        this.nextStage = nextStage;
    }

    @Override
    public int getTotalAgeTicks() {
        return this.totalAgeTicks;
    }

    @Override
    public DinoStage nextStage() {
        return this.nextStage;
    }

}

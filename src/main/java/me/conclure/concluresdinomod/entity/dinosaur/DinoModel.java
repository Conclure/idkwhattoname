package me.conclure.concluresdinomod.entity.dinosaur;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class DinoModel<T extends DinoEntity<T>> extends AnimatedGeoModel<T> {
    public static <T extends DinoEntity<T>> DinoModel<T> create() {
        return new DinoModel<>();
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

    @Override
    public void setLivingAnimations(T entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone(entity.getCurrentStage().getSkullGroup());

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }
}

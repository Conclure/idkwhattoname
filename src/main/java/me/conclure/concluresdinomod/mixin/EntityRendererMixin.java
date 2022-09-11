package me.conclure.concluresdinomod.mixin;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityRenderer.class)
public interface EntityRendererMixin<T extends Entity> {

    @Invoker int callGetBlockLightLevel(T entity, BlockPos pos);
}

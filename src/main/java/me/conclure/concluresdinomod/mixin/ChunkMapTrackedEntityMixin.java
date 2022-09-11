package me.conclure.concluresdinomod.mixin;

import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(targets = "net.minecraft.server.level.ChunkMap$TrackedEntity")
public interface ChunkMapTrackedEntityMixin {
    @Accessor
    Entity getEntity();

    @Accessor
    Set<ServerPlayerConnection> getSeenBy();
}

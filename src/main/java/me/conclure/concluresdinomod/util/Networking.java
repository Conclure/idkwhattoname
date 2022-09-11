package me.conclure.concluresdinomod.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import me.conclure.concluresdinomod.ExampleMod;
import me.conclure.concluresdinomod.entity.dinosaur.DinoEntity;
import me.conclure.concluresdinomod.mixin.ChunkMapTrackedEntityMixin;
import me.conclure.concluresdinomod.mixin.ChunkMapMixin;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.level.Level;

public class Networking {
    public static final ResourceLocation MOD_ENTITY_EVENT = new ResourceLocation(ExampleMod.ID,"network_channel");

    public static void broadcastEntityEvent(Level level, DinoEntity<?> entity, byte eventId) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        ChunkMap chunkMap = serverLevel.getChunkSource().chunkMap;
        Int2ObjectMap<Object> entityMap = ((ChunkMapMixin) chunkMap).getEntityMap();
        Object trackedEntity = entityMap.get(entity.getId());
        if (trackedEntity == null) {
            return;
        }

        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(entity.getId());
        buf.writeByte(eventId);

        ChunkMapTrackedEntityMixin entityMixin = (ChunkMapTrackedEntityMixin) trackedEntity;
        for (ServerPlayerConnection connection : entityMixin.getSeenBy()) {
            ServerPlayNetworking.send(connection.getPlayer(), MOD_ENTITY_EVENT, buf);
        }
        if (entityMixin.getEntity() instanceof ServerPlayer player) {
            ServerPlayNetworking.send(player, MOD_ENTITY_EVENT, buf);
        }
    }
}
package me.conclure.concluresdinomod;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.ClientModInitializer;
import me.conclure.concluresdinomod.entity.ModEntityType;
import me.conclure.concluresdinomod.entity.dinosaur.DinoEntity;
import me.conclure.concluresdinomod.entity.dinosaur.DinoEntityRenderer;
import me.conclure.concluresdinomod.entity.velociraptor.VelociraptorEntity;
import me.conclure.concluresdinomod.util.Networking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;

import java.util.Objects;

public class ClientInit implements ClientModInitializer {


    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntityType.TRICERATOPS, DinoEntityRenderer::create);
        EntityRendererRegistry.register(ModEntityType.VELOCIRAPTOR, ctx -> {
            GeoEntityRenderer<VelociraptorEntity> renderer = DinoEntityRenderer.create(ctx);
            renderer.addLayer(new GeoLayerRenderer<>(renderer) {
                @Override
                public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, VelociraptorEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                    GeoModelProvider<VelociraptorEntity> modelProvider = renderer.getGeoModelProvider();
                    ResourceLocation modelResource = modelProvider.getModelResource(entitylivingbaseIn);
                    GeoModel model = modelProvider.getModel(modelResource);
                    if (entitylivingbaseIn.areEyesClosed()) {
                        return;
                    }
                    if (entitylivingbaseIn.isBaby()) {
                        RenderType renderType = RenderType.entityTranslucentEmissive(new ResourceLocation(ExampleMod.ID, "textures/velociraptor_baby_eyes.png"));
                        VertexConsumer vertexConsumer = bufferIn.getBuffer(renderType);
                        renderer.render(model, entitylivingbaseIn, partialTicks, renderType, matrixStackIn, bufferIn, vertexConsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
                        return;
                    }

                    if (entitylivingbaseIn.areEyesOpen()) {
                        renderEyes(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, partialTicks, model, renderer);
                        return;
                    }

                    if (entitylivingbaseIn.areEyesPeering()) {
                        RenderType renderType = RenderType.entityTranslucentEmissive(new ResourceLocation(ExampleMod.ID, "textures/velociraptor_adult_eyes_peer.png"));
                        VertexConsumer vertexConsumer = bufferIn.getBuffer(renderType);
                        renderer.render(model, entitylivingbaseIn, partialTicks, renderType, matrixStackIn, bufferIn, vertexConsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
                    }
                }
            });
            return renderer;
        });
        EntityRendererRegistry.register(ModEntityType.TYRANNOSAURUS, DinoEntityRenderer::create);
        EntityRendererRegistry.register(ModEntityType.PREDATOR_X, DinoEntityRenderer::create);
        ClientPlayNetworking.registerGlobalReceiver(Networking.MOD_ENTITY_EVENT,new ModEntityEventChannelHandler());
    }

    static class ModEntityEventChannelHandler implements ClientPlayNetworking.PlayChannelHandler {

        @Override
        public void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
            int entityId = buf.readInt();
            byte eventId = buf.readByte();
            client.doRunTask(() -> {
                ClientLevel level = client.level;
                Objects.requireNonNull(level);

                Entity entity = level.getEntity(entityId);
                if (entity == null) {
                    return;
                }

                if (!(entity instanceof DinoEntity<?> dinosaur)) {
                    return;
                }

                dinosaur.handleModEntityEvent(eventId);
            });
        }
    }

    private static void renderEyes(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, VelociraptorEntity entitylivingbaseIn, float partialTicks, GeoModel model, GeoEntityRenderer<VelociraptorEntity> renderer) {
        RenderType renderType = RenderType.entityTranslucentEmissive(new ResourceLocation(ExampleMod.ID, "textures/velociraptor_adult_eyes.png"));
        VertexConsumer vertexConsumer = bufferIn.getBuffer(renderType);
        renderer.render(model, entitylivingbaseIn, partialTicks, renderType, matrixStackIn, bufferIn, vertexConsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
    }
}

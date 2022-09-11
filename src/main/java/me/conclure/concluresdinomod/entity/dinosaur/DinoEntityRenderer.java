package me.conclure.concluresdinomod.entity.dinosaur;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import me.conclure.concluresdinomod.mixin.EntityRendererMixin;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class DinoEntityRenderer<T extends DinoEntity<T>> extends GeoEntityRenderer<T> {

    private DinoEntityRenderer(EntityRendererProvider.Context ctx, AnimatedGeoModel<T> model) {
        super(ctx, model);
    }

    @Override
    public AnimatedGeoModel<T> getGeoModelProvider() {
        return (AnimatedGeoModel<T>) super.getGeoModelProvider();
    }

    public static <T extends DinoEntity<T>, M extends AnimatedGeoModel<T>> GeoEntityRenderer<T> create(EntityRendererProvider.Context ctx, M model) {
        return new DinoEntityRenderer<>(ctx, model);
    }

    public static <T extends DinoEntity<T>> GeoEntityRenderer<T> create(EntityRendererProvider.Context ctx) {
        return create(ctx, DinoModel.<T>create());
    }

    public static <T extends DinoEntity<T> & IAnimationTickable> GeoEntityRenderer<T> createTickable(EntityRendererProvider.Context ctx) {
        return create(ctx, DinoTickingModel.<T>getInstance());
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return this.getGeoModelProvider().getTextureResource(entity);
    }

    @Override
    public void renderEarly(T animatable, PoseStack stackIn, float ticks, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        this.shadowRadius = animatable.getWidth() / 2;
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        Entity leashHolder = entity.getLeashHolder();
        if (leashHolder == null) {
            return;
        }
        this.renderLeash(entity, partialTicks, stack, bufferIn, leashHolder);
    }

    //From MobRenderer
    private <E extends Entity> void renderLeash(T entityLiving, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, E leashHolder) {
        int u;
        matrixStack.pushPose();
        Vec3 vec3 = leashHolder.getRopeHoldPosition(partialTicks);
        double d = (double) (Mth.lerp(partialTicks, entityLiving.yBodyRot, entityLiving.yBodyRotO) * ((float) Math.PI / 180)) + 1.5707963267948966;
        Vec3 vec32 = ((Entity) entityLiving).getLeashOffset();
        double e = Math.cos(d) * vec32.z + Math.sin(d) * vec32.x;
        double f = Math.sin(d) * vec32.z - Math.cos(d) * vec32.x;
        double g = Mth.lerp(partialTicks, entityLiving.xo, entityLiving.getX()) + e;
        double h = Mth.lerp(partialTicks, entityLiving.yo, entityLiving.getY()) + vec32.y;
        double i = Mth.lerp(partialTicks, entityLiving.zo, entityLiving.getZ()) + f;
        matrixStack.translate(e, vec32.y, f);
        float j = (float) (vec3.x - g);
        float k = (float) (vec3.y - h);
        float l = (float) (vec3.z - i);
        float m = 0.025f;
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.leash());
        Matrix4f matrix4f = matrixStack.last().pose();
        float n = Mth.fastInvSqrt(j * j + l * l) * 0.025f / 2.0f;
        float o = l * n;
        float p = j * n;
        BlockPos blockPos = new BlockPos(entityLiving.getEyePosition(partialTicks));
        BlockPos blockPos2 = new BlockPos(leashHolder.getEyePosition(partialTicks));
        int q = this.getBlockLightLevel(entityLiving, blockPos);
        EntityRenderer<? super E> renderer = this.entityRenderDispatcher.getRenderer(leashHolder);
        //noinspection unchecked
        int r = ((EntityRendererMixin<? super E>)renderer).callGetBlockLightLevel(leashHolder, blockPos2);
        int s = entityLiving.level.getBrightness(LightLayer.SKY, blockPos);
        int t = entityLiving.level.getBrightness(LightLayer.SKY, blockPos2);
        for (u = 0; u <= 24; ++u) {
            DinoEntityRenderer.addVertexPair(vertexConsumer, matrix4f, j, k, l, q, r, s, t, 0.025f, 0.025f, o, p, u, false);
        }
        for (u = 24; u >= 0; --u) {
            DinoEntityRenderer.addVertexPair(vertexConsumer, matrix4f, j, k, l, q, r, s, t, 0.025f, 0.0f, o, p, u, true);
        }
        matrixStack.popPose();
    }

    @Override
    public RenderType getRenderType(T animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(this.getTextureLocation(animatable));
    }

    @Override
    protected float getDeathMaxRotation(T entityLivingBaseIn) {
        return 0f;
    }

    //From MobRenderer
    private static void addVertexPair(VertexConsumer vertexConsumer, Matrix4f matrix4f, float f, float g, float h, int i, int j, int k, int l, float m, float n, float o, float p, int q, boolean bl) {
        float r = (float)q / 24.0f;
        int s = (int)Mth.lerp(r, i, j);
        int t = (int)Mth.lerp(r, k, l);
        int u = LightTexture.pack(s, t);
        float v = q % 2 == (bl ? 1 : 0) ? 0.7f : 1.0f;
        float w = 0.5f * v;
        float x = 0.4f * v;
        float y = 0.3f * v;
        float z = f * r;
        float aa = g > 0.0f ? g * r * r : g - g * (1.0f - r) * (1.0f - r);
        float ab = h * r;
        vertexConsumer.vertex(matrix4f, z - o, aa + n, ab + p).color(w, x, y, 1.0f).uv2(u).endVertex();
        vertexConsumer.vertex(matrix4f, z + o, aa + m - n, ab - p).color(w, x, y, 1.0f).uv2(u).endVertex();
    }
}

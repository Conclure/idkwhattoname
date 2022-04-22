package net.fabricmc.example;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.Path;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;

import java.util.EnumSet;
import java.util.OptionalInt;

public class VelociraptorEntity extends DinosaurEntity {
    private static final StageManager stageManagerInstance = StageManager.create(
            VelociraptorStages.ADULT
    );
    public static final int CALL = 0;

    private final GoalSelector stateSelector;

    protected VelociraptorEntity(EntityType<? extends VelociraptorEntity> entityType, Level level) {
        super(entityType, level);
        this.stateSelector = new GoalSelector(level.getProfilerSupplier());

        if (!level.isClientSide) {
            this.registerStateGoals();
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        this.playSound(ModSoundEvents.VELOCIRAPTOR_CALL,1f,1f);
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }

    @Override
    protected StageManager stageManagerInstance() {
        return stageManagerInstance;
    }

    private void registerStateGoals() {
        int priority = -1;
        this.stateSelector.addGoal(++priority,new MakeAmbientSoundGoal(this));
    }

    @Override
    protected void registerGoals() {
        int priority = -1;
        this.goalSelector.addGoal(++priority,new FloatGoal(this));
        this.goalSelector.addGoal(++priority,new PanicGoal(this,.5));
        this.goalSelector.addGoal(++priority,new TemptGoal(this, .3,Ingredient.of(ModItems.UNIDENTIFIED_DNA),false));
        this.goalSelector.addGoal(++priority,new WaterAvoidingRandomStrollGoal(this,.3));
        this.goalSelector.addGoal(++priority,new LookAtPlayerGoal(this, Player.class,6f));
        this.goalSelector.addGoal(++priority,new RandomLookAroundGoal(this));
    }

    @Override
    protected void aiStepServerSided() {
        super.aiStepServerSided();
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        int i = this.level.getServer().getTickCount() + this.getId();
        if (i % 2 == 0 || this.tickCount <= 1) {
            this.level.getProfiler().push("stateSelector");
            this.stateSelector.tick();
            this.level.getProfiler().pop();
        } else {
            this.level.getProfiler().push("stateSelector");
            this.stateSelector.tickRunningGoals(false);
            this.level.getProfiler().pop();
        }
    }

    @Override
    public DinosaurStage getAdultStage() {
        return VelociraptorStages.ADULT;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this,"molang",1, event -> {
            AnimationController<VelociraptorEntity> controller = event.getController();
            double speed = this.getAttributeValue(Attributes.MOVEMENT_SPEED);

            AnimationBuilder builder = new AnimationBuilder();
            if (this.isMoving()) {
                builder.addAnimation("animation.velociraptor_adult.walk", true);
                speed*=2;
            } else {
                builder.addAnimation("animation.velociraptor_adult.idle", true);
            }
            controller.setAnimation(builder);
            controller.setAnimationSpeed(speed);
            return PlayState.CONTINUE;
        }));
        AnimationController<VelociraptorEntity> sound = new AnimationController<>(this, "sound", Ticks.fromSeconds(1), event -> {
            AnimationController<VelociraptorEntity> controller = event.getController();

            AnimationBuilder builder = new AnimationBuilder();
            OptionalInt optionalState = this.getState();
            if (optionalState.isEmpty()) {
                controller.clearAnimationCache();
                return PlayState.STOP;
            }
            int state = optionalState.getAsInt();
            if (state == CALL) {
                builder.addAnimation("animation.velociraptor_adult.call");
            }
            controller.setAnimation(builder);
            return PlayState.CONTINUE;
        });/*
        sound.registerCustomInstructionListener(event -> {
            LocalPlayer player = Minecraft.getInstance().player;
            System.out.println(1);
            if (player != null) {
                float soundVolume = this.getSoundVolume();
                float voicePitch = this.getVoicePitch();
                player.playSound(ModSoundEvents.VELOCIRAPTOR_CALL, soundVolume, voicePitch);
            }
        });
        */
        animationData.addAnimationController(sound);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new GroundPathMoveDetectingNavigation(this,level);
    }

    static class GroundPathMoveDetectingNavigation extends GroundPathNavigation {

        public GroundPathMoveDetectingNavigation(DinosaurEntity mob, Level level) {
            super(mob, level);
        }

        @Override
        public boolean moveTo(@Nullable Path pathentity, double speed) {
            boolean result = super.moveTo(pathentity, speed);
            if (result) {
                this.getMob().setMoving(true);
            }
            return result;
        }

        DinosaurEntity getMob() {
            return ((DinosaurEntity) this.mob);
        }

        @Override
        public void stop() {
            super.stop();
            this.getMob().setMoving(false);
        }
    }

    static class MakeAmbientSoundGoal extends Goal {
        private static final int CALL_TICKS = Ticks.fromSeconds(2);
        private static final int MAKE_SOUND_TICK = (int) Ticks.fromSeconds(.5);

        private int callTick;
        private final VelociraptorEntity entity;

        MakeAmbientSoundGoal(VelociraptorEntity entity) {
            this.entity = entity;
        }

        @Override
        public boolean canUse() {
            if (this.entity.getRandom().nextInt(100) != 0) {
                return false;
            }
            OptionalInt optionalState = this.entity.getState();
            if (optionalState.isPresent()) {
                return false;
            }
            return true;
        }

        @Override
        public void start() {
            this.callTick = this.adjustedTickDelay(CALL_TICKS);
            this.entity.setState(0);
        }

        @Override
        public void stop() {
            this.callTick = 0;
            this.entity.resetState();
        }

        @Override
        public boolean canContinueToUse() {
            return this.callTick > 0;
        }

        @Override
        public void tick() {
            this.callTick = Math.max(0, this.callTick - 1);

            if (this.callTick == this.adjustedTickDelay(MAKE_SOUND_TICK)) {
                float soundVolume = this.entity.getSoundVolume();
                float voicePitch = this.entity.getVoicePitch();
                this.entity.playSound(ModSoundEvents.VELOCIRAPTOR_CALL, soundVolume, voicePitch);
            }
        }
    }
}

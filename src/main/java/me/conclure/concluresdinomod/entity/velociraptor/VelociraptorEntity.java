package me.conclure.concluresdinomod.entity.velociraptor;

import com.mojang.datafixers.DataFixUtils;
import me.conclure.concluresdinomod.ActivityType;
import me.conclure.concluresdinomod.entity.dinosaur.DinoEntity;
import me.conclure.concluresdinomod.entity.dinosaur.DinoFlockManager;
import me.conclure.concluresdinomod.ai.navigation.GroundPathMoveDetectingNavigation;
import me.conclure.concluresdinomod.misc.ModSoundEvents;
import me.conclure.concluresdinomod.util.Networking;
import me.conclure.concluresdinomod.util.Ticks;
import me.conclure.concluresdinomod.entity.ModEntityEvent;
import me.conclure.concluresdinomod.entity.dinosaur.DinoTypeInfo;
import me.conclure.concluresdinomod.item.ModItems;
import me.conclure.concluresdinomod.util.StageIndex;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Predicate;

//TODO customize look goal, dino should look with its right or left eye rather than with its nose
public class VelociraptorEntity extends DinoEntity<VelociraptorEntity> {
    public static final DinoTypeInfo INFO = DinoTypeInfo.builder()
            .adultStage(VelociraptorStages.ADULT)
            .babyStage(VelociraptorStages.BABY)
            .flockSize(11)
            .activityType(ActivityType.NOCTURNAL)
            .build();
    public static final int CALL = 1;
    public static final int DUMMY = 0;
    private static final StageIndex STAGE_INDEX_INSTANCE = StageIndex.create(
            VelociraptorStages.BABY,
            VelociraptorStages.ADULT
    );

    private int blinkTick;
    private BlinkGoal blinkGoal;

    public VelociraptorEntity(EntityType<? extends VelociraptorEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    protected void aiStepClientSidedEarly() {
        this.blinkTick = Math.max(0, this.blinkTick - 1);
    }

    @Override
    public StageIndex getStageIndex() {
        return STAGE_INDEX_INSTANCE;
    }

    @Override
    protected void registerGoals() {
        int priority = -1;
        this.goalSelector.addGoal(++priority, new FloatGoal(this));
        this.goalSelector.addGoal(++priority, new PanicGoal(this, .5));
        this.goalSelector.addGoal(++priority, new TemptGoal(this, .3, Ingredient.of(ModItems.UNIDENTIFIED_DNA), false));
        this.goalSelector.addGoal(++priority, new FlockGoal<>(this, .3));

        Goal goal = new WaterAvoidingRandomStrollGoal(this, .3);
        goal.getFlags().add(Goal.Flag.LOOK);
        this.goalSelector.addGoal(++priority, goal);
        this.goalSelector.addGoal(++priority, new LookAtPlayerGoal(this, Player.class, 6f));
        this.goalSelector.addGoal(++priority, new RandomLookAroundGoal(this));
    }

    @Override
    protected void registerAnimationGoals() {
        int priority = -1;
        this.blinkGoal = new BlinkGoal(this);
        this.animationSelector.addGoal(++priority, this.blinkGoal);
        this.goalSelector.addGoal(++priority, new MakeAmbientSoundGoal(this));
    }

    @Override
    protected void customServerAiStep() {
        this.blinkTick = this.blinkGoal.currentTick;
        super.customServerAiStep();
    }

    @Override
    public void handleModEntityEvent(byte id) {
        if (id == ModEntityEvent.BLINK) {
            this.blinkTick = BlinkGoal.TOTAL_TICKS;
            return;
        }
        super.handleModEntityEvent(id);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "molang", 0, event -> {
            if (this.isBaby()){
                return PlayState.CONTINUE;
            }
            AnimationController<VelociraptorEntity> controller = event.getController();
            double speed = this.getAttributeValue(Attributes.MOVEMENT_SPEED);

            AnimationBuilder builder = new AnimationBuilder();
            if (this.isMoving() && event.isMoving()) {
                builder.addAnimation("animation.velociraptor_adult.walk", true);
                speed *= 2;
            } else {
                builder.addAnimation("animation.velociraptor_adult.idle", true);
            }
            controller.setAnimation(builder);
            controller.setAnimationSpeed(speed);
            return PlayState.CONTINUE;
        }));
        AnimationController<VelociraptorEntity> sound = new AnimationController<>(this, "sound", Ticks.fromSeconds(1), event -> {
            if (this.isBaby()){
                return PlayState.CONTINUE;
            }
            AnimationController<VelociraptorEntity> controller = event.getController();

            AnimationBuilder builder = new AnimationBuilder();
            OptionalInt optionalState = this.getAnimationState();
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
        });
        sound.registerSoundListener(event -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                float soundVolume = this.getSoundVolume();
                float voicePitch = this.getVoicePitch();
                player.playSound(ModSoundEvents.VELOCIRAPTOR_CALL, soundVolume, voicePitch);
            }
        });
        animationData.addAnimationController(sound);
    }

    public boolean areEyesClosed() {
        int tick = this.blinkTick;
        return tick > BlinkGoal.EYES_PEERING_UP && tick <= BlinkGoal.EYES_CLOSED;
    }

    public boolean areEyesPeering() {
        int tick = this.blinkTick;
        return tick > BlinkGoal.EYES_CLOSED && tick <= BlinkGoal.EYES_PEERING_DOWN
                || tick > BlinkGoal.EYES_OPEN && tick <= BlinkGoal.EYES_PEERING_UP;
    }

    public boolean areEyesOpen() {
        int tick = this.blinkTick;
        return tick > BlinkGoal.EYES_PEERING_DOWN || tick <= BlinkGoal.EYES_OPEN;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new GroundPathMoveDetectingNavigation(this, level);
    }

    @Override
    public float getVoicePitch() {
        if (this.isBaby()) {
            return (this.random.nextFloat() - this.random.nextFloat()) * 0.3f + 1.4f;
        }
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.3f + .9f;
    }

    @Override
    public DinoTypeInfo getTypeInfo() {
        return INFO;
    }


    static class MakeAmbientSoundGoal extends Goal {
        private static final int CALL_TICKS = Ticks.fromSeconds(2);
        private final VelociraptorEntity entity;
        private int callTick;

        MakeAmbientSoundGoal(VelociraptorEntity entity) {
            this.entity = entity;
        }

        @Override
        public boolean canUse() {
            if (this.entity.getRandom().nextInt(100) != 0) {
                return false;
            }
            OptionalInt optionalState = this.entity.getAnimationState();
            if (optionalState.isPresent()) {
                return false;
            }
            this.entity.setAnimationState(DUMMY);
            return true;
        }

        @Override
        public void start() {
            this.entity.setAnimationState(CALL);
            this.callTick = this.adjustedTickDelay(CALL_TICKS);
        }

        @Override
        public void stop() {
            this.callTick = 0;
            this.entity.resetAnimationState();
        }

        @Override
        public boolean canContinueToUse() {
            return this.callTick > 0;
        }

        @Override
        public void tick() {
            this.callTick = Math.max(0, this.callTick - 1);
        }
    }

    public static class BlinkGoal extends Goal {
        public static final int TOTAL_TICKS = (int) Ticks.fromSeconds(1.5); //30
        private static final int EYES_PEERING_DOWN = TOTAL_TICKS - 2; //28
        private static final int EYES_CLOSED = TOTAL_TICKS - 4; //26
        private static final int EYES_PEERING_UP = TOTAL_TICKS - 8; //22
        private static final int EYES_OPEN = TOTAL_TICKS - 10; //20
        private final VelociraptorEntity entity;
        private final Level level;
        private int currentTick;

        BlinkGoal(VelociraptorEntity entity) {
            this.entity = entity;
            this.level = entity.level;
        }

        @Override
        public boolean canUse() {
            return this.entity.getRandom().nextInt(50) == 0;
        }

        @Override
        public void start() {
            this.currentTick = this.adjustedTickDelay(TOTAL_TICKS);
            Networking.broadcastEntityEvent(this.level, this.entity, ModEntityEvent.BLINK);
        }

        @Override
        public void stop() {
            this.currentTick = 0;
        }

        @Override
        public boolean canContinueToUse() {
            return this.currentTick > 0;
        }

        @Override
        public void tick() {
            this.currentTick = Math.max(0, this.currentTick - 1);
        }
    }

    //TODO fix min & max range, we kinda want them to stroll around freely but still follow their leader
    //TODO fix leader pick by dominance, if they're equally dominant this goal mustnt run
    //TODO fix inferiority to a player owner
    static class FlockGoal<E extends DinoEntity<E>> extends Goal {
        private static final int INTERVAL_TICKS = 200;
        private final E entity;
        private int timeToRecalcPath;
        private int nextStartTick;
        private final double speedModifier;

        FlockGoal(E entity, double speedModifier) {
            this.entity = entity;
            this.speedModifier = speedModifier;
            this.setFlags(EnumSet.of(Flag.MOVE,Flag.LOOK));
        }

        protected int nextStartTick(E taskOwner) {
            return FlockGoal.reducedTickDelay(200 + taskOwner.getRandom().nextInt(200) % 20);
        }

        @Override
        public boolean canUse() {
            DinoFlockManager<E> dinoFlockManager = this.entity.getFlockManager();
            if (dinoFlockManager.hasFollowers()) {
                return false;
            }
            if (dinoFlockManager.isFollower()) {
                return true;
            }
            if (this.nextStartTick > 0) {
                --this.nextStartTick;
                return false;
            }

            this.nextStartTick = this.nextStartTick(this.entity);
            Predicate<? super E> predicate = dino -> dinoFlockManager.canBeFollowed()
                    || !dinoFlockManager.isFollower();
            AABB area = this.entity.getBoundingBox().inflate(8.0, 8.0, 8.0);
            List<E> list = this.entity.level.getEntitiesOfClass(this.entity.getClassType(), area, predicate);
            Optional<E> optional = list.stream()
                    .filter(dino -> dino.getFlockManager().canBeFollowed())
                    .findAny();
            E otherDino = DataFixUtils.orElse(optional, this.entity);
            otherDino.getFlockManager().addFollowers(list.stream().filter(dino -> !dino.getFlockManager().isFollower()));
            return dinoFlockManager.isFollower();
        }


        @Override
        public boolean canContinueToUse() {
            DinoFlockManager<E> dinoFlockManager = this.entity.getFlockManager();
            return dinoFlockManager.isFollower() && dinoFlockManager.inRangeOfLeader();
        }

        @Override
        public void start() {
            this.timeToRecalcPath = 0;
        }

        @Override
        public void stop() {
            this.entity.getFlockManager().stopFollowing();
        }

        @Override
        public void tick() {
            if (--this.timeToRecalcPath > 0) {
                return;
            }
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            this.entity.getFlockManager().pathToLeader(this.speedModifier);
        }
    }

}

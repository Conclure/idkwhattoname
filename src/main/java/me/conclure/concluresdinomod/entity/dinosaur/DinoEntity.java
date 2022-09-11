package me.conclure.concluresdinomod.entity.dinosaur;

import me.conclure.concluresdinomod.util.OptionalByte;
import me.conclure.concluresdinomod.util.StatsTracker;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;

//TODO proper owner ship with goals
//TODO breeding
public abstract class DinoEntity<E extends DinoEntity<E>> extends PathfinderMob
        implements DinoLike<E>, IAnimatable, OwnableEntity {
    private static final EntityDataAccessor<Integer> LIFE_STAGE;
    private static final EntityDataAccessor<Boolean> MOVING_STATE;
    private static final EntityDataAccessor<OptionalInt> ANIMATION_STATE;
    private static final EntityDataAccessor<Optional<UUID>> OWNERUUID;

    public static final int MAX_TRAINABLE_STAT_LIMIT;
    public static final int MAX_TOTAL_TRAINABLE_STAT_LIMIT;
    public static final int MAX_UNIQUE_STAT_LIMIT;

    static {
        MAX_UNIQUE_STAT_LIMIT = 5;
        MAX_TRAINABLE_STAT_LIMIT = 21;
        MAX_TOTAL_TRAINABLE_STAT_LIMIT = (MAX_TRAINABLE_STAT_LIMIT * 2) + MAX_UNIQUE_STAT_LIMIT;
        var clazz = DinoEntity.class;
        LIFE_STAGE = SynchedEntityData.defineId(clazz, EntityDataSerializers.INT);
        MOVING_STATE = SynchedEntityData.defineId(clazz, EntityDataSerializers.BOOLEAN);
        ANIMATION_STATE = SynchedEntityData.defineId(clazz, EntityDataSerializers.OPTIONAL_UNSIGNED_INT);
        OWNERUUID = SynchedEntityData.defineId(clazz, EntityDataSerializers.OPTIONAL_UUID);
    }

    @Override
    public void travel(Vec3 travelVector) {
        super.travel(travelVector);
        double y = this.getDeltaMovement().y;
        if (y <= 0) {
            return;
        }
        System.out.println(y);
    }

    //client
    private final AnimationFactory animationFactory;

    //server
    private final StatsTracker uniqueStatsTracker;
    private final StatsTracker trainableStatsTracker;
    private int stageAge;
    private boolean isMale;
    private OptionalByte friendship = OptionalByte.empty();
    protected final GoalSelector animationSelector;
    private final DinoFlockManager<E> dinoFlockManager;
    private final DinoInterface<E> dinoInterface;

    public DinoEntity(EntityType<? extends E> entityType, Level level) {
        super(entityType, level);
        this.animationFactory = new AnimationFactory(this);
        this.uniqueStatsTracker = StatsTracker.create(MAX_UNIQUE_STAT_LIMIT);
        this.trainableStatsTracker = StatsTracker.create(MAX_TRAINABLE_STAT_LIMIT);
        this.animationSelector = new GoalSelector(level.getProfilerSupplier());
        if (!level.isClientSide) {
            this.registerAnimationGoals();
        }
        this.dinoFlockManager = DinoFlockManager.create(this);
        this.dinoInterface = DinoInterface.create(this);
    }

    protected void registerAnimationGoals() {}

    //remember, goals are server sided :3
    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    public void handleModEntityEvent(byte id) {}

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        MinecraftServer server = this.level.getServer();
        Objects.requireNonNull(server);
        int i = server.getTickCount() + this.getId();
        if (i % 2 == 0 || this.tickCount <= 1) {
            this.level.getProfiler().push("animationSelector");
            this.animationSelector.tick();
            this.level.getProfiler().pop();
        } else {
            this.level.getProfiler().push("animationSelector");
            this.animationSelector.tickRunningGoals(false);
            this.level.getProfiler().pop();
        }
    }

    @Override
    public void removeFreeWill() {
        super.removeFreeWill();
        this.animationSelector.removeAllGoals();
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (LIFE_STAGE.equals(key)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(key);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();

        this.entityData.define(LIFE_STAGE,this.getStageIndex().getIdByStage(this.getTypeInfo().babyStage()));
        this.entityData.define(MOVING_STATE,false);
        this.entityData.define(ANIMATION_STATE,OptionalInt.empty());
        this.entityData.define(OWNERUUID, Optional.empty());
    }

    @Override
    public void refreshDimensions() {
        double d = this.getX();
        double e = this.getY();
        double f = this.getZ();
        super.refreshDimensions();

        this.setPos(d, e, f);
    }

    public static AttributeSupplier.Builder createDinoAttributes() {
        return AttributeSupplier.builder()
                .add(Attributes.MAX_HEALTH,Attributes.MAX_HEALTH.getDefaultValue())
                .add(Attributes.FOLLOW_RANGE,Attributes.FOLLOW_RANGE.getDefaultValue())
                .add(Attributes.KNOCKBACK_RESISTANCE,Attributes.KNOCKBACK_RESISTANCE.getDefaultValue())
                .add(Attributes.MOVEMENT_SPEED,1)        //easy to change with modifiers if the base is 1
                .add(Attributes.ATTACK_DAMAGE,Attributes.ATTACK_DAMAGE.getDefaultValue())
                .add(Attributes.ATTACK_KNOCKBACK,Attributes.ATTACK_KNOCKBACK.getDefaultValue())
                .add(Attributes.ATTACK_SPEED, Attributes.ATTACK_SPEED.getDefaultValue())
                .add(Attributes.ARMOR,Attributes.ARMOR.getDefaultValue())
                .add(Attributes.ARMOR_TOUGHNESS, Attributes.ARMOR_TOUGHNESS.getDefaultValue());
    }

    @Override
    public DinoFlockManager<E> getFlockManager() {
        return this.dinoFlockManager;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        this.uniqueStatsTracker.randomize(this.random::nextInt);
        this.setPersistenceRequired();
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }

    //remember this is just invoked if server sided
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        CompoundTag uniqueStats = compound.getCompound("UniqueStats");
        CompoundTag trainableStats = compound.getCompound("TrainableStats");

        this.uniqueStatsTracker.readAdditionalSaveData(uniqueStats);
        this.trainableStatsTracker.readAdditionalSaveData(trainableStats);

        this.setDinoStageId(compound.getInt("DinoStage"));
        this.setStageAge(compound.getInt("StageAge"));
        this.setMale(compound.getBoolean("IsMale"));

        if (compound.contains("Friendship")) {
            this.setFriendship(compound.getByte("Friendship"));
        } else {
            this.resetFriendship();
        }
    }

    //remember this is just invoked if server sided
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        CompoundTag uniqueStats = this.uniqueStatsTracker.saveAdditionalSaveData();
        CompoundTag trainableStats = this.trainableStatsTracker.saveAdditionalSaveData();

        compound.put("UniqueStats",uniqueStats);
        compound.put("TrainableStats",trainableStats);

        compound.putInt("DinoStage",this.getDinoStageId());
        compound.putInt("StageAge",this.getStageAge());
        compound.putBoolean("IsMale",this.isMale());

        this.getFriendship().ifPresentOrElse(theByte -> {
            compound.putByte("Friendship",theByte);
        },() -> {
            compound.remove("Friendship");
        });
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        DinoStage dinoStage = this.getCurrentStage();
        return super.getDimensions(pose).scale(dinoStage.getWidth(),dinoStage.getHeight());
    }

    @Override
    public void aiStep() {
        if (this.level.isClientSide) {
            this.aiStepClientSidedEarly();
            super.aiStep();
            this.aiStepClientSided();
        } else {
            this.aiStepServerSidedEarly();
            super.aiStep();
            this.aiStepServerSided();
        }
    }

    protected void aiStepServerSidedEarly() {

    }

    protected void aiStepClientSidedEarly() {}

    protected void aiStepServerSided() {
        if (this.isAlive()) {
            int age = this.stageAge;
            DinoStage stage = this.getCurrentStage();

            if (stage instanceof PreadultDinoStage preadultStage) {
                int totalAge = preadultStage.getTotalAgeTicks();

                if (age < totalAge) {
                    //TODO make the increment less likely to run if low hp
                    this.setStageAge(age + 1);
                } else {
                    this.setStageAge(0);
                    this.setDinoStageId(this.getDinoStageId()+1);
                }
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.dinoFlockManager.tick();
    }

    protected void aiStepClientSided() {

    }

    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0, 0.6f * this.getEyeHeight(), this.getBbWidth() * 0.4f);
    }

    public StatsTracker getTrainableStats() {
        return this.trainableStatsTracker;
    }

    public StatsTracker getUniqueStats() {
        return this.uniqueStatsTracker;
    }

    public OptionalByte getFriendship() {
        return this.friendship;
    }

    public void setFriendship(byte friendship) {
        this.friendship = OptionalByte.of(friendship);
    }

    public void resetFriendship() {
        this.friendship = OptionalByte.empty();
    }

    @Override
    public AnimationFactory getFactory() {
        return this.animationFactory;
    }

    public int getDinoStageId() {
        return this.entityData.get(LIFE_STAGE);
    }

    public void setDinoStageId(int id) {
        this.entityData.set(LIFE_STAGE,id);
    }

    @Override
    public DinoStage getCurrentStage() {
        return this.getStageIndex().getStageById(this.getDinoStageId());
    }

    public void setDinoStage(DinoStage stage) {
        this.setDinoStageId(this.getStageIndex().getIdByStage(stage));
    }

    public int getStageAge() {
        return this.stageAge;
    }

    public void setStageAge(int stageAge) {
        this.stageAge = stageAge;
    }

    public boolean isMoving() {
        return this.entityData.get(MOVING_STATE);
    }

    public void setMoving(boolean moving) {
        this.entityData.set(MOVING_STATE,moving);
    }

    public OptionalInt getAnimationState() {
        return this.entityData.get(ANIMATION_STATE);
    }

    public void setAnimationState(int state) {
        this.entityData.set(ANIMATION_STATE,OptionalInt.of(state));
    }

    public void resetAnimationState() {
        this.entityData.set(ANIMATION_STATE,OptionalInt.empty());
    }

    @Override
    public boolean isBaby() {
        return this.dinoInterface.isBaby();
    }

    @Nullable
    @Override
    public UUID getOwnerUUID() {
        return this.entityData.get(OWNERUUID).orElse(null);
    }

    public void setOwnerUUID(UUID uUID) {
        this.entityData.set(OWNERUUID, Optional.of(uUID));
    }

    public void clearOwnerUUID() {
        this.entityData.set(OWNERUUID, Optional.empty());
    }

    public boolean isMale() {
        return this.isMale;
    }

    public void setMale(boolean male) {
        this.isMale = male;
    }

    @Override
    public DinoInterface<E> getInterface() {
        return this.dinoInterface;
    }

    @Nullable
    @Override
    public Entity getOwner() {
        try {
            UUID uUID = this.getOwnerUUID();
            if (uUID == null) {
                return null;
            }
            return this.level.getPlayerByUUID(uUID);
        } catch (IllegalArgumentException illegalArgumentException) {
            return null;
        }
    }
}
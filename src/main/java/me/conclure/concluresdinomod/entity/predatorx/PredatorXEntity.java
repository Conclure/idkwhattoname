package me.conclure.concluresdinomod.entity.predatorx;

import me.conclure.concluresdinomod.ai.navigation.WaterBoundPathMoveDetectingNavigation;
import me.conclure.concluresdinomod.entity.dinosaur.DinoTypeInfo;
import me.conclure.concluresdinomod.entity.dinosaur.DinoWaterEntity;
import me.conclure.concluresdinomod.util.StageIndex;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.BreathAirGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.manager.AnimationData;

public class PredatorXEntity extends DinoWaterEntity<PredatorXEntity> {
    public static final DinoTypeInfo INFO = DinoTypeInfo.builder()
            .adultStage(PredatorXStages.ADULT)
            .babyStage(PredatorXStages.ADULT)
            .build();
    private static final StageIndex STAGE_INDEX_INSTANCE = StageIndex.create(
            PredatorXStages.ADULT
    );

    public PredatorXEntity(EntityType<? extends PredatorXEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        this.setAirSupply(this.getMaxAirSupply());
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathMoveDetectingNavigation(this,level);
    }

    @Override
    protected void registerGoals() {
        int priority = -1;
        this.goalSelector.addGoal(++priority, new BreathAirGoal(this));
        this.goalSelector.addGoal(priority, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(++priority, new RandomSwimmingGoal(this, 1.0, 10));
    }

    @Override
    public StageIndex getStageIndex() {
        return STAGE_INDEX_INSTANCE;
    }

    @Override
    public DinoTypeInfo getTypeInfo() {
        return INFO;
    }

    @Override
    public void registerControllers(AnimationData animationData) {

    }
}

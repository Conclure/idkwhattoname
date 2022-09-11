package me.conclure.concluresdinomod.entity.triceratops;

import me.conclure.concluresdinomod.entity.dinosaur.DinoEntity;
import me.conclure.concluresdinomod.entity.dinosaur.DinoTypeInfo;
import me.conclure.concluresdinomod.util.StageIndex;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.manager.AnimationData;

public class TriceratopsEntity extends DinoEntity<TriceratopsEntity> {
    public static final DinoTypeInfo INFO = DinoTypeInfo.builder()
            .adultStage(TriceratopsStages.ADULT)
            .babyStage(TriceratopsStages.BABY)
            .flockSize(7)
            .build();
    private static final StageIndex STAGE_INDEX_INSTANCE = StageIndex.create(
            TriceratopsStages.BABY,
            TriceratopsStages.ADULT
    );

    @Override
    public StageIndex getStageIndex() {
        return STAGE_INDEX_INSTANCE;
    }

    public TriceratopsEntity(EntityType<? extends TriceratopsEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        int priority = -1;
        this.goalSelector.addGoal(++priority,new FloatGoal(this));
        this.goalSelector.addGoal(++priority,new PanicGoal(this,1));
        this.goalSelector.addGoal(++priority,new WaterAvoidingRandomStrollGoal(this,0.4d));
        this.goalSelector.addGoal(++priority,new LookAtPlayerGoal(this, Player.class,6f));
        this.goalSelector.addGoal(++priority,new RandomLookAroundGoal(this));
    }

    @Override
    public void registerControllers(AnimationData animationData) {

    }

    @Override
    public DinoTypeInfo getTypeInfo() {
        return INFO;
    }
}

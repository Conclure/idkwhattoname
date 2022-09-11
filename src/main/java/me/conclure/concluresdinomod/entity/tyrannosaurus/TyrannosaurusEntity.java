package me.conclure.concluresdinomod.entity.tyrannosaurus;

import me.conclure.concluresdinomod.entity.dinosaur.DinoEntity;
import me.conclure.concluresdinomod.entity.triceratops.TriceratopsStages;
import me.conclure.concluresdinomod.entity.dinosaur.DinoTypeInfo;
import me.conclure.concluresdinomod.item.ModItems;
import me.conclure.concluresdinomod.util.StageIndex;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.manager.AnimationData;

public class TyrannosaurusEntity extends DinoEntity<TyrannosaurusEntity> {
    public static final DinoTypeInfo INFO = DinoTypeInfo.builder()
            .adultStage(TyrannosaurusStages.ADULT)
            .babyStage(TriceratopsStages.BABY)
            .flockSize(3)
            .build();
    private static final StageIndex STAGE_INDEX_INSTANCE = StageIndex.create(
            TyrannosaurusStages.ADULT
    );

    public TyrannosaurusEntity(EntityType<? extends TyrannosaurusEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        int priority = -1;
        this.goalSelector.addGoal(++priority,new FloatGoal(this));
        this.goalSelector.addGoal(++priority,new PanicGoal(this,.19));
        this.goalSelector.addGoal(++priority,new TemptGoal(this, .21, Ingredient.of(ModItems.UNIDENTIFIED_DNA),false));
        this.goalSelector.addGoal(++priority,new WaterAvoidingRandomStrollGoal(this,.1575));
        this.goalSelector.addGoal(++priority,new LookAtPlayerGoal(this, Player.class,6f));
        this.goalSelector.addGoal(++priority,new RandomLookAroundGoal(this));
    }


    @Override
    public void registerControllers(AnimationData animationData) {

    }

    @Override
    public StageIndex getStageIndex() {
        return STAGE_INDEX_INSTANCE;
    }

    @Override
    public DinoTypeInfo getTypeInfo() {
        return INFO;
    }
}

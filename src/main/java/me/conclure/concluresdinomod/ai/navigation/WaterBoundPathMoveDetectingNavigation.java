package me.conclure.concluresdinomod.ai.navigation;

import me.conclure.concluresdinomod.entity.dinosaur.DinoEntity;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import org.jetbrains.annotations.Nullable;

public class WaterBoundPathMoveDetectingNavigation extends WaterBoundPathNavigation {

    public WaterBoundPathMoveDetectingNavigation(DinoEntity<?> mob, Level level) {
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

    DinoEntity<?> getMob() {
        return ((DinoEntity<?>) this.mob);
    }

    @Override
    public void stop() {
        super.stop();
        this.getMob().setMoving(false);
    }
}

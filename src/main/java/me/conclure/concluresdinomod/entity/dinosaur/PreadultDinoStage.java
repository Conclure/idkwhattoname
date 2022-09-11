package me.conclure.concluresdinomod.entity.dinosaur;

public interface PreadultDinoStage extends DinoStage {

    static StandardPreadultDinoStageBuilder builder() {
        return new StandardPreadultDinoStageBuilder();
    }

    /**
     * @return the amount of ticks this stage takes for a dinosaur to grow up to the next stage
     */
    int getTotalAgeTicks();

    DinoStage nextStage();
}

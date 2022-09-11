package me.conclure.concluresdinomod.entity.dinosaur;

import me.conclure.concluresdinomod.util.StageIndex;

public interface DinoLike<E extends DinoEntity<E>> {

    @SuppressWarnings("unchecked")
    default E self() {
        return (E) this;
    }

    @SuppressWarnings("unchecked")
    default Class<E> getClassType() {
        return (Class<E>) this.getClass();
    }

    StageIndex getStageIndex();

    DinoStage getCurrentStage();

    DinoTypeInfo getTypeInfo();

    DinoInterface<E> getInterface();

    DinoFlockManager<E> getFlockManager();

    default float getWidth() {
        return this.getCurrentStage().getWidth();
    }

    default float getHeight() {
        return this.getCurrentStage().getHeight();
    }
}

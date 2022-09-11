package me.conclure.concluresdinomod.entity.dinosaur;

import me.conclure.concluresdinomod.entity.EntityEncapsulator;
import org.jetbrains.annotations.Nullable;

public class DinoInterface<E extends DinoEntity<E>> implements EntityEncapsulator<E> {
    private final E entity;

    public DinoInterface(E entity) {
        this.entity = entity;
    }

    public static <E extends DinoEntity<E>> DinoInterface<E> create(DinoEntity<E> entity) {
        return new DinoInterface<>(entity.self());
    }

    @Override
    public E getEntity() {
        return this.entity;
    }

    //Is symmetrical
    @Nullable
    public E compareAge(E other) {
        int idOther = other.getDinoStageId();
        int idThis = this.entity.getDinoStageId();

        boolean otherHasGreaterStage = idOther > idThis;
        if (otherHasGreaterStage) {
            return other;
        }

        boolean thisHasGreaterStage = idOther < idThis;
        if (thisHasGreaterStage) {
            return this.entity;
        }

        if (this.isAdult()) {
            return null;
        }

        int stageAgeOther = other.getStageAge();
        int stageAgeThis = this.entity.getStageAge();

        boolean otherHasGreaterStageAge = stageAgeOther > stageAgeThis;
        if (otherHasGreaterStageAge) {
            return other;
        }

        boolean thisHasGreaterStageAge = stageAgeOther < stageAgeThis;
        if (thisHasGreaterStageAge) {
            return this.entity;
        }

        return null;
    }

    //Is symmetrical
    @Nullable
    public E compareStats(E other) {
        int statsOther = other.getTrainableStats().sum() + other.getUniqueStats().sum();
        int statsThis = this.entity.getTrainableStats().sum() + this.entity.getUniqueStats().sum();
        boolean otherHasGreaterStats = statsOther > statsThis;
        if (otherHasGreaterStats) {
            return other;
        }

        boolean thisHasGreaterStats = statsOther < statsThis;
        if (thisHasGreaterStats) {
            return this.entity;
        }

        return null;
    }

    @Nullable
    public E compareDominance(E other) {
        E oneWithGreaterAge = this.compareAge(other);
        if (oneWithGreaterAge != null) {
            return oneWithGreaterAge;
        }
        return this.compareStats(other);
    }

    public boolean isAdult() {
        return this.entity.getDinoStageId() == this.entity.getStageIndex().getIdByStage(this.entity.getTypeInfo().adultStage());
    }

    public boolean isBaby() {
        return this.entity.getDinoStageId() == this.entity.getStageIndex().getIdByStage(this.entity.getTypeInfo().babyStage());
    }
}

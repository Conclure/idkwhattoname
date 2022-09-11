package me.conclure.concluresdinomod.entity.dinosaur;

import me.conclure.concluresdinomod.entity.EntityEncapsulator;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.stream.Stream;

public class DinoFlockManager<E extends DinoEntity<E>> implements EntityEncapsulator<E> {
    private @Nullable E leader;
    private final E entity;
    private int flockSize = 1;

    public DinoFlockManager(E entity) {
        this.entity = entity;
    }

    public static <E extends DinoEntity<E>> DinoFlockManager<E> create(DinoEntity<E> entity) {
        return new DinoFlockManager<>(entity.self());
    }

    public boolean hasFollowers() {
        return this.flockSize > 1;
    }

    public boolean isFollower() {
        return this.leader != null && this.leader.isAlive();
    }

    private void addFollower() {
        ++this.flockSize;
    }

    public void tick() {
        Level level = this.entity.level;
        boolean hasFollowers = this.hasFollowers();
        if (!hasFollowers) {
            return;
        }
        if (level.random.nextInt(200) != 1) {
            return;
        }
        boolean isAlone = level.getEntitiesOfClass(this.entity.getClassType(), this.entity.getBoundingBox().inflate(8.0, 8.0, 8.0)).size() <= 1;
        if (isAlone) {
            this.flockSize = 1;
        }
    }

    @Override
    public E getEntity() {
        return this.entity;
    }

    public E startFollowing(E dino) {
        this.leader = dino;
        dino.getFlockManager().addFollower();
        return dino;
    }

    private void removeFollower() {
        --this.flockSize;
    }

    public void stopFollowing() {
        Objects.requireNonNull(this.leader);
        this.leader.getFlockManager().removeFollower();
        this.leader = null;
    }

    public boolean inRangeOfLeader() {
        Objects.requireNonNull(this.leader);
        return this.entity.distanceToSqr(this.leader) <= 121.0;
    }

    public void addFollowers(Stream<? extends E> stream) {
        stream.limit(this.entity.getTypeInfo().flockSize() - this.flockSize)
                .filter(dino -> dino != this.entity)
                .forEach(dino -> dino.getFlockManager().startFollowing(this.entity));
    }

    public boolean canBeFollowed() {
        return this.hasFollowers() && this.flockSize < this.entity.getTypeInfo().flockSize();
    }

    public void pathToLeader(double speedModifier) {
        if (this.isFollower()) {
            Objects.requireNonNull(this.leader);
            this.entity.getNavigation().moveTo(this.leader, speedModifier);
        }
    }

}

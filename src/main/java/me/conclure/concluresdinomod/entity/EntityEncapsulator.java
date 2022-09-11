package me.conclure.concluresdinomod.entity;

import net.minecraft.world.entity.Entity;

public interface EntityEncapsulator<E extends Entity> {
    E getEntity();
}

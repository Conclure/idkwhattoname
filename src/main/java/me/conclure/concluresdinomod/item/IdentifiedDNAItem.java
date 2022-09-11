package me.conclure.concluresdinomod.item;

import me.conclure.concluresdinomod.entity.dinosaur.DinoEntity;
import net.minecraft.world.entity.EntityType;

import java.awt.*;

public class IdentifiedDNAItem extends DNAItem {
    private final EntityType<? extends DinoEntity> dinoType;

    public IdentifiedDNAItem(Properties properties, int primaryColor, int secondaryColor, EntityType<? extends DinoEntity> dinoType) {
        super(properties, primaryColor, secondaryColor);
        this.dinoType = dinoType;
    }

    public IdentifiedDNAItem(Properties properties, Color primaryColor, Color secondaryColor, EntityType<? extends DinoEntity> dinoType) {
        super(properties, primaryColor, secondaryColor);
        this.dinoType = dinoType;
    }

    public EntityType<? extends DinoEntity> getDinoType() {
        return this.dinoType;
    }
}

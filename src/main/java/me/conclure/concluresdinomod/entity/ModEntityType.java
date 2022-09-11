package me.conclure.concluresdinomod.entity;

import me.conclure.concluresdinomod.entity.dinosaur.DinoEntity;
import me.conclure.concluresdinomod.ExampleMod;
import me.conclure.concluresdinomod.entity.predatorx.PredatorXEntity;
import me.conclure.concluresdinomod.entity.triceratops.TriceratopsEntity;
import me.conclure.concluresdinomod.entity.tyrannosaurus.TyrannosaurusEntity;
import me.conclure.concluresdinomod.entity.velociraptor.VelociraptorEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntityType {
    public static final EntityType<TriceratopsEntity> TRICERATOPS;
    public static final EntityType<VelociraptorEntity> VELOCIRAPTOR;
    public static final EntityType<TyrannosaurusEntity> TYRANNOSAURUS;
    public static final EntityType<PredatorXEntity> PREDATOR_X;


    static {
        TRICERATOPS = Registry.register(
                Registry.ENTITY_TYPE,
                new ResourceLocation(ExampleMod.ID,"triceratops"),
                FabricEntityTypeBuilder.create(MobCategory.CREATURE,TriceratopsEntity::new)
                        .dimensions(EntityDimensions.scalable(1,1))
                        .build()
        );
        VELOCIRAPTOR = Registry.register(
                Registry.ENTITY_TYPE,
                new ResourceLocation(ExampleMod.ID,"velociraptor"),
                FabricEntityTypeBuilder.create(MobCategory.CREATURE,VelociraptorEntity::new)
                        .dimensions(EntityDimensions.scalable(1,1))
                        .build()
        );
        TYRANNOSAURUS = Registry.register(
                Registry.ENTITY_TYPE,
                new ResourceLocation(ExampleMod.ID,"tyrannosaurus"),
                FabricEntityTypeBuilder.create(MobCategory.CREATURE,TyrannosaurusEntity::new)
                        .dimensions(EntityDimensions.scalable(1,1))
                        .build()
        );
        PREDATOR_X = Registry.register(
                Registry.ENTITY_TYPE,
                new ResourceLocation(ExampleMod.ID,"predator_x"),
                FabricEntityTypeBuilder.create(MobCategory.CREATURE,PredatorXEntity::new)
                        .dimensions(EntityDimensions.scalable(1,1))
                        .build()
        );
    }

    public static void registerAttributes() {
        //noinspection ConstantConditions
        FabricDefaultAttributeRegistry.register(TRICERATOPS, DinoEntity.createDinoAttributes());
        FabricDefaultAttributeRegistry.register(VELOCIRAPTOR, DinoEntity.createDinoAttributes());
        FabricDefaultAttributeRegistry.register(TYRANNOSAURUS, DinoEntity.createDinoAttributes());
        FabricDefaultAttributeRegistry.register(PREDATOR_X, DinoEntity.createDinoAttributes());
    }
}

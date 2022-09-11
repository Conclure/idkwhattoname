package me.conclure.concluresdinomod.item;

import me.conclure.concluresdinomod.ExampleMod;
import me.conclure.concluresdinomod.misc.ModColors;
import me.conclure.concluresdinomod.entity.ModEntityType;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ModItems {
    public static final Item UNIDENTIFIED_JURASSIC_DNA;
    public static final Item UNIDENTIFIED_TRIASSIC_DNA;
    public static final Item UNIDENTIFIED_CRETACEOUS_DNA;
    public static final Item UNIDENTIFIED_AQUATIC_DNA;
    public static final Item TRICERATOPS_DNA;
    public static final Item UNIDENTIFIED_DNA;
    public static final CreativeModeTab TAB_MISC = FabricItemGroupBuilder.create(new ResourceLocation(ExampleMod.ID, "concluresdinomod_misc"))
            .icon(() -> new ItemStack(ModItems.UNIDENTIFIED_DNA))
            .build();

    static {
        UNIDENTIFIED_DNA = registerItem("unidentified_dna", new Item(
                new FabricItemSettings().group(TAB_MISC)
        ));
        TRICERATOPS_DNA = registerItem("triceratops_dna", new IdentifiedDNAItem(
                new FabricItemSettings().group(TAB_MISC),
                ModColors.TRICERATOPS_PRIMARY,
                ModColors.TRICERATOPS_SECONDARY,
                ModEntityType.TRICERATOPS
        ));
        UNIDENTIFIED_JURASSIC_DNA = registerItem("unidentified_jurassic_dna", new Item(
                new FabricItemSettings().group(TAB_MISC)
        ));
        UNIDENTIFIED_TRIASSIC_DNA = registerItem("unidentified_triassic_dna", new Item(
                new FabricItemSettings().group(TAB_MISC)
        ));
        UNIDENTIFIED_CRETACEOUS_DNA = registerItem("unidentified_cretaceous_dna", new Item(
                new FabricItemSettings().group(TAB_MISC)
        ));
        UNIDENTIFIED_AQUATIC_DNA = registerItem("unidentified_aquatic_dna", new Item(
                new FabricItemSettings().group(TAB_MISC)
        ));
    }

    private static Item registerItem(String key, Item item) {
        return Registry.register(Registry.ITEM,new ResourceLocation(ExampleMod.ID,key),item);
    }

    public static void init() {

    }

}

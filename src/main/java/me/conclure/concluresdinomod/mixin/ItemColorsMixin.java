package me.conclure.concluresdinomod.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import me.conclure.concluresdinomod.item.DNAItem;
import net.minecraft.client.color.item.ItemColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Environment(EnvType.CLIENT)
@Mixin(ItemColors.class)
public final class ItemColorsMixin {

    @ModifyVariable(
            method = "createDefault(Lnet/minecraft/client/color/block/BlockColors;)Lnet/minecraft/client/color/item/ItemColors;",
            at = @At("STORE"),
            ordinal = 0
    )
    private static ItemColors inject(ItemColors itemColors) {
        for (DNAItem dnaItem : DNAItem.getAllDNATypes()) {
            itemColors.register((stack,tintIndex) -> dnaItem.getColor(tintIndex),dnaItem);
        }
        return itemColors;
    }
}

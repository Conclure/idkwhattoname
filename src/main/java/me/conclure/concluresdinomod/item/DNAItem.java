package me.conclure.concluresdinomod.item;

import com.google.common.collect.Iterables;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class DNAItem extends Item {
    private static final List<DNAItem> DNA_ITEMS = new LinkedList<>();
    private final int primaryColor;
    private final int secondaryColor;

    public DNAItem(Properties properties, int primaryColor, int secondaryColor) {
        super(properties);
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        DNA_ITEMS.add(this);
    }

    public DNAItem(Properties properties, Color primaryColor, Color secondaryColor) {
        this(properties,primaryColor.getRGB(),secondaryColor.getRGB());
    }

    public int getPrimaryColor() {
        return this.primaryColor;
    }

    public int getSecondaryColor() {
        return this.secondaryColor;
    }

    public int getColor(int tintIndex) {
        return tintIndex == 0 ? this.primaryColor : this.secondaryColor;
    }

    public static Iterable<DNAItem> getAllDNATypes() {
        return Iterables.unmodifiableIterable(DNA_ITEMS);
    }

    @Nullable
    public static DNAItem byId(int id) {
        if (id < 0) {
            return null;
        }
        if (id >= DNA_ITEMS.size()) {
            return null;
        }
        return DNA_ITEMS.get(id);
    }

}

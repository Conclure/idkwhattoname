package me.conclure.concluresdinomod.misc;

import java.awt.*;

//This class is mainly for IntelliJ such that the ide can provide a color picker
public sealed interface ModColors permits ModColors.Nothing {
    final class Nothing implements ModColors {
        private Nothing() {
        }
    }

    Color TRICERATOPS_PRIMARY = new Color(0xf2bc94);
    Color TRICERATOPS_SECONDARY = new Color(0x722620);
}

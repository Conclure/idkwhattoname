package me.conclure.concluresdinomod.entity;

public sealed interface ModEntityEvent permits ModEntityEvent.Nothing {
    final class Nothing implements ModEntityEvent {
        private Nothing() {

        }
    }
    byte BLINK = 1;
}

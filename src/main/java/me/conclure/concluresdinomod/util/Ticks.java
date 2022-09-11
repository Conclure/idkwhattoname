package me.conclure.concluresdinomod.util;

public final class Ticks {
    private Ticks() {

    }
    public static int fromSeconds(int seconds) {
        return seconds*20;
    }

    public static double fromSeconds(double seconds) {
        return seconds*20;
    }

    public static long fromSeconds(long seconds) {
        return seconds*20;
    }

    public static int fromMinutes(int minutes) {
        return minutes*20*60;
    }

    public static long fromMinutes(long minutes) {
        return minutes*20*60;
    }

    public static int fromHours(int hours) {
        return hours*20*60*60;
    }

    public static long fromHours(long hours) {
        return hours*20*60*60;
    }
}

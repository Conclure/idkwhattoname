package me.conclure.concluresdinomod.misc;

import me.conclure.concluresdinomod.ExampleMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSoundEvents {
    public static final SoundEvent VELOCIRAPTOR_CALL;

    static {
        VELOCIRAPTOR_CALL = Registry.register(
                Registry.SOUND_EVENT,
                new ResourceLocation(ExampleMod.ID,"concluresdinomod.velociraptor_call"),
                new SoundEvent(new ResourceLocation(ExampleMod.ID,"concluresdinomod.velociraptor_call")));
    }


    public static void init() {

    }

}

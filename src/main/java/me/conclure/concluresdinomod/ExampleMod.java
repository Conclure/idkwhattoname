package me.conclure.concluresdinomod;

import net.fabricmc.api.ModInitializer;
import me.conclure.concluresdinomod.entity.ModEntityType;
import me.conclure.concluresdinomod.item.ModItems;
import me.conclure.concluresdinomod.misc.ModSoundEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib3.GeckoLib;

public class ExampleMod implements ModInitializer {
	public static final String ID = "${MOD_ID}";
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	@Override
	public void onInitialize() {
		GeckoLib.initialize();
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ModEntityType.registerAttributes();
		ModItems.init();
		ModSoundEvents.init();
		LOGGER.info("Hello Fabric world!");
	}
}

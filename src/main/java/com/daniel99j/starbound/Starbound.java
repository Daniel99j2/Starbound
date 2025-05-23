package com.daniel99j.starbound;

import com.daniel99j.starbound.block.ModBlockEntities;
import com.daniel99j.starbound.block.ModBlocks;
import com.daniel99j.starbound.item.ModItems;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Starbound implements ModInitializer {
	public static final String MOD_ID = "starbound";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("Starbound");
	private static final org.apache.logging.log4j.Logger DEV_LOGGER = LogManager.getLogger("Starbound Debug");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Let's hope it doesn't go supernova!");
		ModBlocks.registerBlocks();
		ModItems.registerModItems();
		ModBlockEntities.registerBlockEntities();
	}

	public static void debug(String text) {
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			DEV_LOGGER.info(text);
		}
	}

	public static void debug(Object text) {
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			DEV_LOGGER.info(text.toString());
		}
	}
}
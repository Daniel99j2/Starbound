package com.daniel99j.starbound;

import com.daniel99j.lib99j.api.EntityUtils;
import com.daniel99j.starbound.block.ModBlockEntities;
import com.daniel99j.starbound.block.ModBlocks;
import com.daniel99j.starbound.item.ModItems;
import com.daniel99j.starbound.magic.PrismLensTrailManager;
import com.daniel99j.starbound.magic.spell.Spells;
import com.daniel99j.starbound.misc.GuiTextures;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import eu.pb4.polymer.resourcepack.extras.api.ResourcePackExtras;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

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
		Spells.init();

		PolymerResourcePackUtils.addModAssets(MOD_ID);
		ResourcePackExtras.forDefault().addBridgedModelsFolder(Identifier.of(MOD_ID, "block"), Identifier.of(MOD_ID, "gui"));
		PolymerResourcePackUtils.markAsRequired();

		GuiTextures.load();

		ServerTickEvents.END_SERVER_TICK.register((server) -> {
			PrismLensTrailManager.tick();
		});

		CommandRegistrationCallback.EVENT.register(((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> {
			commandDispatcher.getRoot().addChild(CommandManager.literal("test-starbound").then(CommandManager.argument("entity", EntityArgumentType.entity()).executes((commandContext -> {
				Objects.requireNonNull(commandContext.getSource().getPlayer()).getWorld().spawnEntity(EntityUtils.cloneEntity(EntityArgumentType.getEntity(commandContext, "entity")));
				return 1;
			}))).build());
		}));
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
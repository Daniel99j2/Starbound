package com.daniel99j.starbound.block;

import com.daniel99j.starbound.Starbound;
import com.daniel99j.starbound.block.pulsar.DarknessBlock;
import com.daniel99j.starbound.block.pulsar.PulsarRedirectorBlock;
import com.daniel99j.starbound.block.pulsar.PulsarTransmitterBlock;
import com.daniel99j.starbound.block.pulsar.machines.TestPulsarMachine;
import com.daniel99j.starbound.block.pulsar.machines.TurretMachine;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModBlocks {
    public static final int MAX_PULSAR_POWER = 60;
    public static final IntProperty PULSAR_POWER = IntProperty.of("pulsar_power", 0, MAX_PULSAR_POWER);

    public static final Block PULSAR_REDIRECTOR = registerBlock(
            "pulsar_redirector",
            PulsarRedirectorBlock::new,
            AbstractBlock.Settings.copy(Blocks.DISPENSER).nonOpaque().pistonBehavior(PistonBehavior.NORMAL)
    );

    public static final Block PULSAR_TRANSMITTER = registerBlock(
            "pulsar_transmitter",
            PulsarTransmitterBlock::new,
            AbstractBlock.Settings.copy(Blocks.BEACON).nonOpaque().pistonBehavior(PistonBehavior.NORMAL)
    );

    public static final Block TEST_MACHINE = registerBlock(
            "test_machine",
            TestPulsarMachine::new,
            AbstractBlock.Settings.copy(Blocks.DISPENSER).nonOpaque().pistonBehavior(PistonBehavior.NORMAL)
    );

    public static final Block TURRET = registerBlock(
            "turret",
            TurretMachine::new,
            AbstractBlock.Settings.copy(Blocks.OBSIDIAN).nonOpaque().pistonBehavior(PistonBehavior.NORMAL)
    );

    public static final Block MYSTERIOUS_CORE = registerBlock(
            "mysterious_core",
            MysteriousCoreBlock::new,
            AbstractBlock.Settings.copy(Blocks.CONDUIT).pistonBehavior(PistonBehavior.NORMAL).sounds(BlockSoundGroup.SCULK).mapColor(MapColor.CLEAR).solid().nonOpaque()
    );

    public static final Block DARKNESS_BLOCK = registerBlock(
            "darkness_block",
            DarknessBlock::new,
            AbstractBlock.Settings.copy(Blocks.BLACK_CONCRETE).nonOpaque()
    );

    public static void registerBlocks() {
        Starbound.debug("Loading blocks");
    }

    public static Block register(RegistryKey<Block> key, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        Block block = (Block)factory.apply(settings.registryKey(key));
        return Registry.register(Registries.BLOCK, key, block);
    }

    public static Block register(RegistryKey<Block> key, AbstractBlock.Settings settings) {
        return register(key, Block::new, settings);
    }

    private static RegistryKey<Block> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Starbound.MOD_ID, id));
    }

    private static Block registerBlock(String id, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        return register(keyOf(id), factory, settings);
    }
}

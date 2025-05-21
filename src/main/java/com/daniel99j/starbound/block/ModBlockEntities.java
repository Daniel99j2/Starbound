package com.daniel99j.starbound.block;

import com.daniel99j.starbound.Starbound;
import com.daniel99j.starbound.block.pulsar.PulsarRedirectorBlockEntity;
import com.daniel99j.starbound.block.pulsar.PulsarTransmitterBlockEntity;
import com.daniel99j.starbound.block.pulsar.machines.TestPulsarMachineBlockEntity;
import eu.pb4.polymer.core.api.block.PolymerBlockUtils;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class ModBlockEntities {
    public static final BlockEntityType<PulsarRedirectorBlockEntity> PULSAR_REDIRECTOR = register("pulsar_redirector", FabricBlockEntityTypeBuilder.create(PulsarRedirectorBlockEntity::new,
            ModBlocks.PULSAR_REDIRECTOR
    ).build(null));

    public static final BlockEntityType<PulsarTransmitterBlockEntity> PULSAR_TRANSMITTER = register("pulsar_transmitter", FabricBlockEntityTypeBuilder.create(PulsarTransmitterBlockEntity::new,
            ModBlocks.PULSAR_TRANSMITTER
    ).build(null));

    public static final BlockEntityType<TestPulsarMachineBlockEntity> TEST_MACHINE = register("test_machine", FabricBlockEntityTypeBuilder.create(TestPulsarMachineBlockEntity::new,
            ModBlocks.TEST_MACHINE
    ).build(null));


    public static void registerBlockEntities() {
    }

    private static <T extends BlockEntity> BlockEntityType<T> register(String id, BlockEntityType<T> type) {
        Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Starbound.MOD_ID, id), type);
        PolymerBlockUtils.registerBlockEntity(type);
        return type;
    }
}
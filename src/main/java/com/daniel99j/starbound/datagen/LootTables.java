package com.daniel99j.starbound.datagen;

import com.daniel99j.starbound.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

class LootTables extends FabricBlockLootTableProvider {

    protected LootTables(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        this.addDrop(ModBlocks.PULSAR_REDIRECTOR);
        this.addDrop(ModBlocks.PULSAR_TRANSMITTER);
        this.addDrop(ModBlocks.MYSTERIOUS_CORE);
        this.addDrop(ModBlocks.TURRET);
        this.addDrop(ModBlocks.TEST_MACHINE);
    }
}

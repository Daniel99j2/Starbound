package com.daniel99j.starbound.block.pulsar.machines;

import com.daniel99j.starbound.block.ModBlockEntities;
import com.daniel99j.starbound.block.ModBlocks;
import com.daniel99j.starbound.block.pulsar.PulsarRedirectorBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class TestPulsarMachineBlockEntity extends PulsarRedirectorBlockEntity {
    public TestPulsarMachineBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TEST_MACHINE, pos, state);
    }

    @Override
    public float getPowerMultiplier() {
        return 0.8f;
    }
}

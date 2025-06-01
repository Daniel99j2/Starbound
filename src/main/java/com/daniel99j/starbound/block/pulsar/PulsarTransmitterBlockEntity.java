package com.daniel99j.starbound.block.pulsar;

import com.daniel99j.starbound.block.ModBlockEntities;
import com.daniel99j.starbound.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class PulsarTransmitterBlockEntity extends PulsarRedirectorBlockEntity {
    public PulsarTransmitterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PULSAR_TRANSMITTER, pos, state);
    }

    @Override
    public int getBeamPower(ServerWorld world) {
        return ModBlocks.MAX_PULSAR_POWER;
    }
}

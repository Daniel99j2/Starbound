package com.daniel99j.starbound.block.pulsar;

import com.daniel99j.starbound.block.ModBlockEntities;
import eu.pb4.polymer.virtualentity.api.attachment.BlockAwareAttachment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PulsarTransmitterBlock extends PulsarRedirectorBlock {
    public PulsarTransmitterBlock(Settings settings) {
        super(settings);
    }

    @Override
    @Nullable
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PulsarTransmitterBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : PulsarTransmitterBlock.validateTicker(type, ModBlockEntities.PULSAR_TRANSMITTER, PulsarTransmitterBlockEntity::tick);
    }

    @Override
    public String getModel(World world, BlockAwareAttachment attachment) {
        return "pulsar_transmitter";
    }
}
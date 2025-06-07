package com.daniel99j.starbound.block.pulsar;

import com.daniel99j.starbound.block.ModBlocks;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import xyz.nucleoid.packettweaker.PacketContext;

public class DarknessBlock extends Block implements PolymerBlock {
    public DarknessBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState blockState, PacketContext packetContext) {
        return Blocks.LIGHT.getDefaultState().with(LightBlock.LEVEL_15, 0);
    }

    @Override
    public boolean forceLightUpdates(BlockState blockState) {
        return true;
    }
}

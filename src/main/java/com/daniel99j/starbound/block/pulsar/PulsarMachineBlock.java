package com.daniel99j.starbound.block.pulsar;

import com.daniel99j.starbound.block.ModBlocks;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.item.ItemPlacementContext;

import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

public abstract class PulsarMachineBlock extends BlockWithEntity {
    protected PulsarMachineBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState()
                .with(Properties.FACING, Direction.NORTH)
                .with(ModBlocks.PULSAR_POWER, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.FACING, ModBlocks.PULSAR_POWER);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction dir = ctx.getPlayerLookDirection();
        BlockState partialState = this.getDefaultState().with(Properties.FACING, dir);
        return partialState.with(ModBlocks.PULSAR_POWER,
                this.getPower((ServerWorld) ctx.getWorld(), ctx.getBlockPos(), partialState));
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        int power = this.getPower((ServerWorld) world, pos, state);
        return state.with(ModBlocks.PULSAR_POWER, power);
    }

    public int getPower(ServerWorld world, BlockPos pos, BlockState state) {
        BlockEntity be = world.getBlockEntity(pos);
        return (be instanceof PulsarPowered beamPowered)
                ? beamPowered.getBeamPower(world)
                : 0;
    }

    public boolean isReceivingSide(BlockState state, Direction side) {
        return side == state.get(Properties.FACING).getOpposite();
    }
}

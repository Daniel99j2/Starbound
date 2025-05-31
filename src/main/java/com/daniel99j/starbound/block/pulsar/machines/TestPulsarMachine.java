package com.daniel99j.starbound.block.pulsar.machines;

import com.daniel99j.starbound.block.ModBlockEntities;
import com.daniel99j.starbound.block.pulsar.PulsarRedirectorBlock;
import com.daniel99j.starbound.gui.TestMachineGui;
import eu.pb4.polymer.virtualentity.api.attachment.BlockAwareAttachment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

public class TestPulsarMachine extends PulsarRedirectorBlock {
    public TestPulsarMachine(Settings settings) {
        super(settings);
    }

    @Override
    @Nullable
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TestPulsarMachineBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : TestPulsarMachine.validateTicker(type, ModBlockEntities.TEST_MACHINE, TestPulsarMachineBlockEntity::tick);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState blockState, PacketContext packetContext) {
        return Blocks.TNT.getDefaultState();
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        new TestMachineGui((ServerPlayerEntity) player, (TestPulsarMachineBlockEntity) world.getBlockEntity(pos)).open();
        return ActionResult.SUCCESS;
    }

    @Override
    public String getModel(World world, BlockAwareAttachment attachment) {
        return "pulsar_machine";
    }
}
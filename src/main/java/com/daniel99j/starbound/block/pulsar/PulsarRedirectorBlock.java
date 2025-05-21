package com.daniel99j.starbound.block.pulsar;

import com.daniel99j.lib99j.api.EntityUtils;
import com.daniel99j.lib99j.api.ItemUtils;
import com.daniel99j.starbound.Starbound;
import com.daniel99j.starbound.block.ModBlockEntities;
import com.daniel99j.starbound.block.ModBlocks;
import com.mojang.serialization.MapCodec;
import eu.pb4.factorytools.api.virtualentity.BlockModel;
import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.factorytools.api.virtualentity.LodItemDisplayElement;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.virtualentity.api.BlockWithElementHolder;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.Objects;

public class PulsarRedirectorBlock extends PulsarMachineBlock implements PolymerBlock, BlockWithElementHolder {
    public static final EnumProperty<Direction> FACING = Properties.FACING;

    public PulsarRedirectorBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    protected void onStateReplaced(BlockState state, ServerWorld world, BlockPos pos, boolean moved) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof PulsarRedirectorBlockEntity prism) {
            if (world instanceof ServerWorld) {
                prism.resetPower();
            }
        }

        super.onStateReplaced(state, world, pos, moved);
    }

    public boolean isReceivingSide(BlockState state, Direction side) {
        return side != state.get(Properties.FACING);
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    public int getWeakRedstonePower(BlockState state, @NotNull BlockView world, @NotNull BlockPos pos, Direction direction) {
        return (int) (15f/((float) ModBlocks.MAX_PULSAR_POWER/state.get(ModBlocks.PULSAR_POWER)));
    }

    public static int lightLevel(BlockState state) {
        return (int) (5f/((float) ModBlocks.MAX_PULSAR_POWER/state.get(ModBlocks.PULSAR_POWER)));
    }

    @Override
    public MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Override
    public ElementHolder createElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
        return new Model(world, initialBlockState, pos);
    }

    @Override
    public boolean tickElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
        return true;
    }

    @Override
    public BlockState getPolymerBlockState(BlockState blockState, PacketContext packetContext) {
        return Blocks.BEACON.getDefaultState();
    }

    public static final class Model extends BlockModel {
        private final ItemDisplayElement mainElement;

        private Model(ServerWorld world, BlockState state, BlockPos pos) {
            this.mainElement = LodItemDisplayElement.createSimple(ItemUtils.getBasicModelItem().getDefaultStack(), this.getUpdateRate(), 0.3f, 0.6f);
            this.mainElement.setTeleportDuration(0);
            this.mainElement.setInterpolationDuration(0);
            this.updateAnimation(state.get(FACING).getOpposite(), world);
            this.addElement(this.mainElement);
        }

        private int getUpdateRate() {
            return 1;
        }

        private void updateAnimation(Direction facing, World world) {
            if(this.blockAware() != null) this.mainElement.setItem(ItemDisplayElementUtil.getModel(Identifier.of(Starbound.MOD_ID, "block/sculk_sniffer_active")));
            EntityUtils.setRotationFromDirection(facing, this.mainElement);
            this.mainElement.setInvisible(true);
            this.mainElement.startInterpolation();
        }

        @Override
        protected void onTick() {
            if (!Objects.requireNonNull(this.blockAware()).isPartOfTheWorld()) {
                return;
            }
            var tick = Objects.requireNonNull(this.blockAware()).getWorld().getTime();

            if (tick % this.getUpdateRate() == 0) {
                this.updateAnimation(Objects.requireNonNull(this.blockAware()).getBlockState().get(FACING), Objects.requireNonNull(this.blockAware()).getWorld());
                this.mainElement.startInterpolationIfDirty();
            }
        }
    }

    @Override
    @Nullable
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PulsarRedirectorBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : PulsarRedirectorBlock.validateTicker(type, ModBlockEntities.PULSAR_REDIRECTOR, PulsarRedirectorBlockEntity::tick);
    }
}
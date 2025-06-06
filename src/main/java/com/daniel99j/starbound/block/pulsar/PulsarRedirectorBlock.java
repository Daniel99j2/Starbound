package com.daniel99j.starbound.block.pulsar;

import com.daniel99j.lib99j.api.EntityUtils;
import com.daniel99j.lib99j.api.ItemUtils;
import com.daniel99j.starbound.Starbound;
import com.daniel99j.starbound.block.ModBlockEntities;
import com.daniel99j.starbound.block.ModBlocks;
import com.mojang.serialization.MapCodec;
import eu.pb4.factorytools.api.block.FactoryBlock;
import eu.pb4.factorytools.api.virtualentity.BlockModel;
import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.virtualentity.api.BlockWithElementHolder;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.attachment.BlockAwareAttachment;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.Brightness;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4x3f;
import org.joml.Vector3f;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.Objects;

public class PulsarRedirectorBlock extends PulsarMachineBlock implements PolymerBlock, BlockWithElementHolder, FactoryBlock {
    public static final EnumProperty<Direction> FACING = Properties.FACING;

    public PulsarRedirectorBlock(Settings settings) {
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
        return new PulsarPoweredModel(world, initialBlockState, pos);
    }

    @Override
    public boolean tickElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
        return true;
    }

    @Override
    public BlockState getPolymerBlockState(BlockState blockState, PacketContext packetContext) {
        return Blocks.BARRIER.getDefaultState();
    }

    public static class PulsarPoweredModel extends BlockModel {
        protected ItemDisplayElement mainElement;
        protected ItemDisplayElement beamElement;

        public PulsarPoweredModel(ServerWorld world, BlockState state, BlockPos pos) {
            this.mainElement = new ItemDisplayElement(ItemUtils.getBasicModelItemStack());
            this.mainElement.setTeleportDuration(0);
            this.mainElement.setInterpolationDuration(0);
            this.beamElement = new ItemDisplayElement(ItemUtils.getBasicModelItemStack());
            this.beamElement.setTeleportDuration(0);
            this.beamElement.setInterpolationDuration(0);
            this.beamElement.setDisplaySize(10000000, 10000000);
            this.updateAnimation(state.get(FACING), world);
            this.addElement(this.mainElement);
            this.addElement(this.beamElement);
        }

        protected int getUpdateRate() {
            return 1;
        }

        protected void updateAnimation(Direction facing, World world) {
            if (this.blockAware() != null) {
                this.mainElement.setItem(ItemDisplayElementUtil.getModel(
                        Identifier.of(Starbound.MOD_ID, "block/" + ((PulsarRedirectorBlock) this.blockState().getBlock()).getModel(world, this.blockAware()))
                ));
            }

            EntityUtils.setRotationFromDirection(facing, this.mainElement);
            EntityUtils.setRotationFromDirection(facing, this.beamElement);
            this.mainElement.setInvisible(true);
            this.mainElement.startInterpolation();
            this.beamElement.setInvisible(true);
            this.beamElement.setBrightness(Brightness.FULL);

            if (this.blockAware() != null && Objects.requireNonNull(this.blockAware()).isPartOfTheWorld()) {
                int power = this.blockState().get(ModBlocks.PULSAR_POWER);
                int distance = ((PulsarRedirectorBlockEntity) Objects.requireNonNull(world.getBlockEntity(Objects.requireNonNull(this.blockAware()).getBlockPos()))).getBeamDistance();

                this.beamElement.setItem(ItemDisplayElementUtil.getModel(
                        Identifier.of(Starbound.MOD_ID,  distance == 32 ? "block/pulsar_beam" : "block/pulsar_beam_solid")
                ));

                if (power == 0 || distance <= 0) {
                    this.beamElement.setTransformation(new Matrix4x3f().scale(0));
                } else {
                    Matrix4x3f matrix = new Matrix4x3f();

                    matrix.translate(new Vector3f(0, 0, distance/2f+0.5f));

                    matrix.scale(new Vector3f(1, 1, (float) distance));

                    this.beamElement.setTransformation(matrix);
                }
            }
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

    public String getModel(World world, BlockAwareAttachment attachment) {
        return "pulsar_redirector";
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

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        world.scheduleBlockTick(pos, state.getBlock(), 1);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        ((PulsarRedirectorBlockEntity) world.getBlockEntity(pos)).updatePowerLevels(world, pos, 0);
        return super.onBreak(world, pos, state, player);
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.scheduledTick(state, world, pos, random);
        ((PulsarRedirectorBlockEntity) world.getBlockEntity(pos)).updatePowerLevels(world, pos);
    }
}
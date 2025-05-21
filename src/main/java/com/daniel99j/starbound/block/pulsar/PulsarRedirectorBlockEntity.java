package com.daniel99j.starbound.block.pulsar;

import com.daniel99j.lib99j.api.ParticleHelper;
import com.daniel99j.starbound.block.ModBlockEntities;
import com.daniel99j.starbound.block.ModBlocks;
import eu.pb4.polymer.core.api.utils.PolymerObject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PulsarRedirectorBlockEntity extends BlockEntity implements PolymerObject, PulsarPowered {
    private static final int TICKS_BETWEEN_BEAM_WOBBLES = 10;
    private @Nullable BlockPos laserEnd;
    private @Nullable BlockPos beaconPos;
    private float prevRenderingBeamWobble = 0.0F;
    private float currRenderingBeamWobble = 0.0F;

    public PulsarRedirectorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PULSAR_REDIRECTOR, pos, state);
    }

    public PulsarRedirectorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {

    }

    public static void tick(World world, BlockPos pos, BlockState state, PulsarRedirectorBlockEntity be) {
        int power = state.get(ModBlocks.PULSAR_POWER);
        if (be.getBeamSourcePos() != null) {
            int actualPower = be.getBeamPower((ServerWorld) world);
            if (actualPower == 0) {
                be.setBeamSourcePos(null);
            } else {
                ParticleHelper.spawnParticlesAtPosition(world, pos.offset(Direction.UP, 1).toCenterPos(), ParticleTypes.NOTE, 2, 0.2, 0.2, 0.2, 0.2);
            }

            if (actualPower != power) {
                ((ServerWorld) world).updateNeighbor(pos, state.getBlock(), null);
//                world.updateNeighbor(pos, state.getBlock(), pos.offset(Direction.UP));
            }
        }

//        if (power > 0 && be.laserEnd != null) {
//            Box box = new Box(pos, be.laserEnd.add(1, 1, 1));
//            world.getEntitiesByClass(Entity.class, box, e -> true).forEach(e -> {
//                float damage = (float)((EntityInPrismBeamCallback)EntityInPrismBeamCallback.EVENT.invoker())
//                        .entityInBeam(e, power).orElse((double) power);
//                e.damage(PulsarDamageSources.create(world, PulsarDamageSources.PRISM_BEAM), damage);
//            });
//        }

        if ((pos.asLong() + world.getTime()) % 5L == 0L) {
            Direction dir = state.get(Properties.FACING);
            BlockPos oldEndPos = be.laserEnd;
            if (power < 1) {
                be.laserEnd = null;
                modifyMachinePower(pos, world, 0, dir, oldEndPos, null);
            } else {
                BlockPos.Mutable scanPos = pos.mutableCopy();

                for (int i = 0; i < 32; i++) {
                    scanPos.move(dir);
                    BlockState checking = world.getBlockState(scanPos);
                    if (checking.isSideSolidFullSquare(world, scanPos, dir.getOpposite())
                            || checking.isSideSolidFullSquare(world, scanPos, dir)) {
                        break;
                    }
                }

                be.laserEnd = scanPos.toImmutable();
                modifyMachinePower(pos, world, power, dir, oldEndPos, be.laserEnd);
            }
        }
    }

    public float getRenderingBeamWobble(float partialTicks) {
        float delta = this.world == null ? partialTicks : (partialTicks + (float) ((this.world.getTime() - 1L) % 10L)) / 10.0F;
        return MathHelper.lerp(delta, this.prevRenderingBeamWobble, this.currRenderingBeamWobble);
    }

    public void setLaserEnd(@Nullable BlockPos laserEnd) {
        this.laserEnd = laserEnd;
    }

    public @Nullable BlockPos getLaserEnd() {
        return this.laserEnd;
    }

    public void resetPower() {
        if (this.world != null && this.laserEnd != null) {
            BlockState state = this.world.getBlockState(this.laserEnd);
            Direction dir = state.get(Properties.FACING);
            if (isPowerableMachine(state, dir)) {
                PulsarMachineBlock machine = (PulsarMachineBlock) state.getBlock();
                this.world.setBlockState(this.laserEnd, state.with(ModBlocks.PULSAR_POWER, machine.getPower((ServerWorld) this.world, this.laserEnd, this.world.getBlockState(this.laserEnd))));
            }
        }
    }

    private static void modifyMachinePower(BlockPos prismPos, World world, int power, Direction laserDir, @Nullable BlockPos previousLaserEnd, @Nullable BlockPos newLaserEnd) {
        BlockState prevEndState = previousLaserEnd == null ? null : world.getBlockState(previousLaserEnd);
        BlockState newEndState = newLaserEnd == null ? null : world.getBlockState(newLaserEnd);
        if (prevEndState != null && !previousLaserEnd.equals(newLaserEnd) && isPowerableMachine(prevEndState, laserDir)) {
            PulsarMachineBlock machine = (PulsarMachineBlock) prevEndState.getBlock();
            BlockEntity blockEntity = world.getBlockEntity(previousLaserEnd);
            if (blockEntity instanceof PulsarPowered pulsarPowered) {
                pulsarPowered.setBeamSourcePos(null);
            }

            world.setBlockState(previousLaserEnd, prevEndState.with(ModBlocks.PULSAR_POWER, machine.getPower((ServerWorld) world, previousLaserEnd, world.getBlockState(previousLaserEnd))));
        }

        if (newEndState != null && isPowerableMachine(newEndState, laserDir)) {
            BlockEntity blockEntity = world.getBlockEntity(newLaserEnd);
            if (blockEntity instanceof PulsarPowered pulsarPowered) {
                pulsarPowered.setBeamSourcePos(prismPos);
            }

            PulsarMachineBlock machine = (PulsarMachineBlock) newEndState.getBlock();
            world.setBlockState(newLaserEnd, newEndState.with(ModBlocks.PULSAR_POWER, machine.getPower((ServerWorld) world, newLaserEnd, newEndState)));
        }
    }

    private static boolean isPowerableMachine(BlockState state, Direction laserDir) {
        Block block = state.getBlock();
        if (block instanceof PulsarMachineBlock machine) {
            return machine.isReceivingSide(state, laserDir.getOpposite());
        }
        return false;
    }

    public @Nullable BlockPos getBeamSourcePos() {
        return this.beaconPos;
    }

    public void setBeamSourcePos(@Nullable BlockPos pos) {
        this.beaconPos = pos;
    }

    public boolean canAcceptBeam(Direction beamDir) {
        return ((PulsarMachineBlock) ModBlocks.PULSAR_REDIRECTOR).isReceivingSide(this.getCachedState(), beamDir.getOpposite());
    }

    @Override
    public float getPowerMultiplier() {
        return 1;
    }
}

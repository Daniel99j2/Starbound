package com.daniel99j.starbound.block.pulsar;

import com.daniel99j.starbound.block.ModBlocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public interface PulsarPowered {
    @Nullable BlockPos getBeamSourcePos();

    void setBeamSourcePos(@Nullable BlockPos var1);

    boolean canAcceptBeam(Direction var1);

    default int getBeamPower(ServerWorld world) {
        BlockPos beaconPos = this.getBeamSourcePos();
        if (beaconPos != null) {
            if(world.getBlockState(beaconPos).contains(ModBlocks.PULSAR_POWER)) return Math.max(0, (int) Math.floor(world.getBlockState(beaconPos).get(ModBlocks.PULSAR_POWER)-getPowerUsage()));
        }

        return 0;
    }

    default int getPowerUsage() {
        return 0;
    }
}

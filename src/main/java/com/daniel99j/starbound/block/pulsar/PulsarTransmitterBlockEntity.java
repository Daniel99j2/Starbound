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

import java.util.Optional;

public class PulsarTransmitterBlockEntity extends PulsarRedirectorBlockEntity {
    public PulsarTransmitterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PULSAR_TRANSMITTER, pos, state);
    }

    @Override
    public int getBeamPower(ServerWorld world) {
        return ModBlocks.MAX_PULSAR_POWER;
    }

    @Override
    public float getPowerMultiplier() {
        return 1;
    }
}

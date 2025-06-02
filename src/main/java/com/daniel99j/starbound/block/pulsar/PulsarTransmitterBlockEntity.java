package com.daniel99j.starbound.block.pulsar;

import com.daniel99j.starbound.block.ModBlockEntities;
import com.daniel99j.starbound.block.ModBlocks;
import com.daniel99j.starbound.misc.ModDamageTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class PulsarTransmitterBlockEntity extends PulsarRedirectorBlockEntity {
    public PulsarTransmitterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PULSAR_TRANSMITTER, pos, state);
    }

    @Override
    public int getBeamPower(ServerWorld world) {
        return world.isSkyVisible(this.getPos().add(0, 1, 0)) ? ModBlocks.MAX_PULSAR_POWER : 0;
    }

    @Override
    protected void customTick(ServerWorld world, BlockPos pos, BlockState state, int power, boolean shouldRun) {
        if(power > 0) {
            Box box = new Box(pos.toCenterPos().add(-0.2, -0.2, -0.2), pos.toCenterPos().add(0.2, 500.2, 0.2));
            world.getEntitiesByClass(Entity.class, box, e -> true).forEach(e -> {
                if(!(e instanceof ItemEntity))  e.damage((ServerWorld) e.getWorld(), new DamageSource(ModDamageTypes.of(e.getWorld(), ModDamageTypes.PULSAR_BEAM)), Math.max(0, (10f/((float) ModBlocks.MAX_PULSAR_POWER/power))));
            });
        }
    }
}

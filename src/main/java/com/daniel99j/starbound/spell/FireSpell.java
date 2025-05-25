package com.daniel99j.starbound.spell;

import com.daniel99j.lib99j.api.ParticleHelper;
import com.daniel99j.lib99j.api.SoundUtils;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class FireSpell extends Spell {
    public FireSpell(Identifier id, int energyUsed) {
        super(id, energyUsed);
    }

    @Override
    protected void cast(ServerPlayerEntity player) {
        SoundUtils.playSoundAtPosition((ServerWorld) player.getWorld(), player.getEyePos(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 1, 1);
        raycast(player, 20, 20, 0.5f, player.getPos(), player.getPitch(), player.getYaw());
    }

    @Override
    protected void raycast(ServerPlayerEntity player, int remaining, int steps, float distance, Vec3d currentPos, float pitch, float yaw) {
        super.raycast(player, remaining, steps, distance, currentPos, pitch, yaw);
        float offset = 0.3f+(1f/((float) steps/remaining));
        player.getWorld().getOtherEntities(player, new Box(currentPos.add(-offset, -offset, -offset), currentPos.add(offset, offset, offset))).forEach((entity -> {
            if(entity instanceof LivingEntity livingEntity) {
                livingEntity.setFireTicks(100);
            }
        }));
        if(player.getWorld().getBlockState(BlockPos.ofFloored(currentPos)).isAir()) player.getWorld().setBlockState(BlockPos.ofFloored(currentPos), Blocks.FIRE.getDefaultState());
        ParticleHelper.spawnParticlesAtPosition(player.getWorld(), currentPos, ParticleTypes.FLAME, 2, offset, offset, offset, 0);
    }
}

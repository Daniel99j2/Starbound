package com.daniel99j.starbound.magic.spell;

import com.daniel99j.lib99j.api.ParticleHelper;
import com.daniel99j.lib99j.api.SoundUtils;
import com.daniel99j.starbound.misc.ModSounds;
import net.minecraft.block.Blocks;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class TimeControlSpell extends Spell {
    public TimeControlSpell(Identifier id, int energyUsed, int cooldown) {
        super(id, energyUsed, cooldown);
    }

    @Override
    protected boolean cast(ServerPlayerEntity player) {
        raycast(player, 20, 20, 0.5f, player.getEyePos(), player.getPitch(), player.getYaw(), false);
        return true;
    }

    @Override
    protected void castEffects(ServerPlayerEntity player) {
        super.castEffects(player);
        SoundUtils.playSoundAtPosition((ServerWorld) player.getWorld(), player.getEyePos(), ModSounds.TIME_CONTROL_SPELL_CAST, SoundCategory.PLAYERS, 1, 1);
    }

    @Override
    protected void raycast(ServerPlayerEntity player, int remaining, int steps, float distance, Vec3d currentPos, float pitch, float yaw, boolean hit) {
        super.raycast(player, remaining, steps, distance, currentPos, pitch, yaw, hit);
        float offset = 0.3f+(1f/((float) steps/remaining));
        player.getServer().getTickManager().getTickRate();
        if(player.getWorld().getBlockState(BlockPos.ofFloored(currentPos)).isAir()) player.getWorld().setBlockState(BlockPos.ofFloored(currentPos), Blocks.WATER.getDefaultState());
        ParticleHelper.spawnParticlesAtPosition(player.getWorld(), currentPos, ParticleTypes.SPLASH, 2, offset, offset, offset, 0);
    }
}

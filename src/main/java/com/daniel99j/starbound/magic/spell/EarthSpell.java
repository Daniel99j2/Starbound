package com.daniel99j.starbound.magic.spell;

import com.daniel99j.lib99j.api.SoundUtils;
import net.minecraft.block.Blocks;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EarthSpell extends Spell {
    public EarthSpell(Identifier id, int energyUsed, int cooldown) {
        super(id, energyUsed, cooldown);
    }

    @Override
    protected void cast(ServerPlayerEntity player) {
        SoundUtils.playSoundAtPosition((ServerWorld) player.getWorld(), player.getEyePos(), SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.PLAYERS, 1, 1);
        raycast(player, 20, 20, 0.5f, player.getEyePos(), player.getPitch(), player.getYaw());
    }

    @Override
    protected void raycast(ServerPlayerEntity player, int remaining, int steps, float distance, Vec3d currentPos, float pitch, float yaw) {
        super.raycast(player, remaining, steps, distance, currentPos, pitch, yaw);
        if(player.getWorld().getBlockState(BlockPos.ofFloored(currentPos)).isAir()) player.getWorld().setBlockState(BlockPos.ofFloored(currentPos), Blocks.DIRT.getDefaultState());
    }
}

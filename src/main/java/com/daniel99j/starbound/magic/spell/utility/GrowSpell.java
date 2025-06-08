package com.daniel99j.starbound.magic.spell.utility;

import com.daniel99j.lib99j.api.NumberUtils;
import com.daniel99j.lib99j.api.ParticleHelper;
import com.daniel99j.lib99j.api.SoundUtils;
import com.daniel99j.starbound.magic.spell.Spell;
import com.daniel99j.starbound.misc.ModSounds;
import net.minecraft.block.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class GrowSpell extends Spell {
    public GrowSpell(Identifier id, int energyUsed, int cooldown, int color) {
        super(id, energyUsed, cooldown, color);
    }

    @Override
    protected boolean cast(ServerPlayerEntity player) {
        raycast(player, 4, 4, 1, player.getEyePos(), player.getPitch(), player.getYaw(), false);
        return currentCheck;
    }

    @Override
    protected void raycast(ServerPlayerEntity player, int remaining, int steps, float distance, Vec3d currentPos, float pitch, float yaw, boolean hit) {
        if(hit) {
            ServerWorld world = (ServerWorld) player.getWorld();
            int radius = NumberUtils.getRandomInt(2, 4);

            for (int x = -radius; x <= radius; x++) {
                int yRange = NumberUtils.getRandomInt(1, 3);
                for (int y = -yRange; y <= yRange; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        if (x*x + z*z <= radius*radius) {
                            BlockPos checkPos = BlockPos.ofFloored(currentPos.add(x, y, z));
                            BlockState blockState = world.getBlockState(checkPos);
                            if (NumberUtils.getRandomInt(0, 3) != 0 && blockState.getBlock() instanceof Fertilizable fertilizable) {
                                currentCheck = true;
                                int times = NumberUtils.getRandomInt(3, 6);
                                for (int i = 0; i < times; i++) {
                                    if(fertilizable.canGrow(world, world.getRandom(), checkPos, blockState)) fertilizable.grow(world, world.getRandom(), checkPos, blockState);
                                }
                                ParticleHelper.spawnParticlesAtPosition(world, checkPos.toCenterPos().add(0, 0.5, 0), ParticleTypes.HAPPY_VILLAGER, 5, 0.3, 0.3, 0.3, 0);
                            }
                        }
                    }
                }
            }
        } else super.raycast(player, remaining, steps, distance, currentPos, pitch, yaw, hit);
    }

    @Override
    protected void castEffects(ServerPlayerEntity player) {
        super.castEffects(player);
        SoundUtils.playSoundAtPosition((ServerWorld) player.getWorld(), player.getEyePos(), ModSounds.GROW_SPELL_CAST, SoundCategory.PLAYERS, 1.0f, 0.8f);
    }
}

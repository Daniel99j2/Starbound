package com.daniel99j.starbound.spell;

import com.daniel99j.lib99j.api.ParticleHelper;
import com.daniel99j.lib99j.api.SoundUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.BreezeWindChargeEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WindSpell extends Spell {
    public WindSpell(Identifier id, int energyUsed) {
        super(id, energyUsed);
    }

    @Override
    protected void cast(ServerPlayerEntity player) {
        SoundUtils.playSoundAtPosition((ServerWorld) player.getWorld(), player.getEyePos(), SoundEvents.ENTITY_BREEZE_IDLE_GROUND, SoundCategory.PLAYERS, 1, 1);
        raycast(player, 20, 20, 0.5f, player.getPos(), player.getPitch(), player.getYaw());
    }

    @Override
    protected void raycast(ServerPlayerEntity player, int remaining, int steps, float distance, Vec3d currentPos, float pitch, float yaw) {
        super.raycast(player, remaining, steps, distance, currentPos, pitch, yaw);
        player.getWorld()
                .createExplosion(
                        null,
                        null,
                        BreezeWindChargeEntity.EXPLOSION_BEHAVIOR,
                        currentPos.getX(),
                        currentPos.getY(),
                        currentPos.getZ(),
                        0.5F,
                        false,
                        World.ExplosionSourceType.TRIGGER,
                        ParticleTypes.GUST_EMITTER_SMALL,
                        ParticleTypes.GUST_EMITTER_SMALL,
                        SoundEvents.ENTITY_BREEZE_WIND_BURST
                );
    }
}

package com.daniel99j.starbound.misc;

import com.daniel99j.lib99j.api.ParticleHelper;
import eu.pb4.polymer.core.api.other.PolymerStatusEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public class ShieldStatusEffect extends HiddenStatusEffect {
    protected ShieldStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        for (int i = 0; i < 5; i++) {
            double radius = entity.getDimensions(entity.getPose()).width() * 2;
            int particles = 20;

            for (int j = 0; j < particles; j++) {
                double y = 1 - (j / (double) (particles - 1)) * 2;
                double radius2 = Math.sqrt(1 - y * y);
                double theta = Math.PI * (Math.sqrt(5) - 1) * j;
                double x = Math.cos(theta) * radius2;
                double z = Math.sin(theta) * radius2;

                Vec3d particlePos = entity.getEyePos().add(
                        x * radius,
                        y * radius,
                        z * radius
                );

                ParticleHelper.spawnParticlesAtPosition(world, particlePos, ParticleTypes.END_ROD, 1, 0, 0, 0, 0);
            }
        }

        return true;
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        super.onApplied(entity, amplifier);
        ParticleHelper.spawnParticlesAtPosition(entity.getWorld(), entity.getEyePos(), ParticleTypes.FLASH, 1, 0, 0, 0, 0);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}

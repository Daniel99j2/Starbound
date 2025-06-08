package com.daniel99j.starbound.magic.spell.offensive;

import com.daniel99j.lib99j.api.ParticleHelper;
import com.daniel99j.lib99j.api.SoundUtils;
import com.daniel99j.starbound.magic.spell.Spell;
import com.daniel99j.starbound.misc.ModSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class HealingSpell extends Spell {
    public HealingSpell(Identifier id, int energyUsed, int cooldown, int color) {
        super(id, energyUsed, cooldown, color);
    }

    @Override
    protected boolean cast(ServerPlayerEntity player) {
        raycast(player, 20, 20, 0.5f, player.getEyePos(), player.getPitch(), player.getYaw(), false);
        return true;
    }

    @Override
    protected void raycast(ServerPlayerEntity player, int remaining, int steps, float distance, Vec3d currentPos, float pitch, float yaw, boolean hit) {
        super.raycast(player, remaining, steps, distance, currentPos, pitch, yaw, hit);
        player.getWorld().getOtherEntities(player, new Box(currentPos.add(-0.5, -0.5, -0.5), currentPos.add(0.5, 0.5, 0.5))).forEach((e) -> {
            if(e instanceof LivingEntity livingEntity) {
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 5, 0));
                ParticleHelper.spawnParticlesAtPosition(player.getWorld(), e.getEyePos(), ParticleTypes.HEART, 10, 0.5, 1.0, 0.5, 0.1);
            }
        });
    }

    @Override
    protected void castEffects(ServerPlayerEntity player) {
        super.castEffects(player);
        SoundUtils.playSoundAtPosition((ServerWorld) player.getWorld(), player.getEyePos(), ModSounds.HEALING_SPELL_CAST, SoundCategory.PLAYERS, 1, 1.2f);
    }
}

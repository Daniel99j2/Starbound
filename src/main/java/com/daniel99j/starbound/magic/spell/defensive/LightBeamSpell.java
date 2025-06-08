package com.daniel99j.starbound.magic.spell.defensive;

import com.daniel99j.lib99j.api.EntityUtils;
import com.daniel99j.lib99j.api.ParticleHelper;
import com.daniel99j.lib99j.api.SoundUtils;
import com.daniel99j.starbound.magic.spell.Spell;
import com.daniel99j.starbound.misc.ModDamageTypes;
import com.daniel99j.starbound.misc.ModSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class LightBeamSpell extends Spell {
    public LightBeamSpell(Identifier id, int energyUsed, int cooldown, int color) {
        super(id, energyUsed, cooldown, color);
    }

    @Override
    protected boolean cast(ServerPlayerEntity player) {
        raycast(player, 20, 20, 0.5f, player.getEyePos(), player.getPitch(), player.getYaw(), false);
        return true;
    }

    @Override
    protected void castEffects(ServerPlayerEntity player) {
        super.castEffects(player);
        SoundUtils.playSoundAtPosition((ServerWorld) player.getWorld(), player.getEyePos(), ModSounds.LIGHT_BEAM_SPELL_CAST, SoundCategory.PLAYERS, 1, 1.2f);
    }

    @Override
    protected void raycast(ServerPlayerEntity player, int remaining, int steps, float distance, Vec3d currentPos, float pitch, float yaw, boolean hit) {
        super.raycast(player, remaining, steps, distance, currentPos, pitch, yaw, hit);
        player.getWorld().getOtherEntities(player, new Box(currentPos.add(-0.5, -0.5, -0.5), currentPos.add(0.5, 0.5, 0.5))).forEach((e) -> {
            if(e instanceof LivingEntity livingEntity) {
                EntityUtils.killDamageSource(livingEntity, new DamageSource(ModDamageTypes.of(e.getWorld(), ModDamageTypes.PULSAR_BEAM)));
            }
        });
        ParticleHelper.spawnParticlesAtPosition(player.getWorld(), currentPos, ParticleTypes.END_ROD, 3, 0.2, 0.2, 0.2, 0);
    }
}

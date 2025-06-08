package com.daniel99j.starbound.magic.spell.utility;

import com.daniel99j.lib99j.api.ParticleHelper;
import com.daniel99j.lib99j.api.SoundUtils;
import com.daniel99j.starbound.magic.spell.Spell;
import com.daniel99j.starbound.misc.ModSounds;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;

public class LevitationSpell extends Spell {
    public LevitationSpell(Identifier id, int energyUsed, int cooldown, int color) {
        super(id, energyUsed, cooldown, color);
    }

    @Override
    protected boolean cast(ServerPlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 60, 7, true, true));
        return true;
    }

    @Override
    protected void castEffects(ServerPlayerEntity player) {
        super.castEffects(player);
        ParticleHelper.spawnParticlesAtPosition(player.getWorld(), player.getEyePos(), ParticleTypes.CLOUD, 20, 0.5, 1.0, 0.5, 0.1);
        SoundUtils.playSoundAtPosition((ServerWorld) player.getWorld(), player.getEyePos(), ModSounds.LEVITATION_SPELL_CAST, SoundCategory.PLAYERS, 1.0f, 1.0f);
    }
}

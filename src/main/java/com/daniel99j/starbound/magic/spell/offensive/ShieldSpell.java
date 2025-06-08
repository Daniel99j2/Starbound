package com.daniel99j.starbound.magic.spell.offensive;

import com.daniel99j.lib99j.api.SoundUtils;
import com.daniel99j.starbound.magic.spell.Spell;
import com.daniel99j.starbound.misc.ModSounds;
import com.daniel99j.starbound.misc.ModStatusEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;

public class ShieldSpell extends Spell {
    public ShieldSpell(Identifier id, int energyUsed, int cooldown, int color) {
        super(id, energyUsed, cooldown, color);
    }

    @Override
    protected boolean cast(ServerPlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 3));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 200, 3));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 300, 3));
        player.addStatusEffect(new StatusEffectInstance(ModStatusEffects.SHIELD, 300, 0));
        return true;
    }

    @Override
    protected void castEffects(ServerPlayerEntity player) {
        super.castEffects(player);
        SoundUtils.playSoundAtPosition((ServerWorld) player.getWorld(), player.getPos(), ModSounds.SHIELD_SPELL_CAST, SoundCategory.PLAYERS, 1.2f, 0.8f);
    }
}

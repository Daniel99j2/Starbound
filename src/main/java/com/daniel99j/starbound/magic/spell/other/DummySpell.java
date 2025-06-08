package com.daniel99j.starbound.magic.spell.other;

import com.daniel99j.lib99j.api.SoundUtils;
import com.daniel99j.starbound.entity.DummyEntity;
import com.daniel99j.starbound.magic.spell.Spell;
import com.daniel99j.starbound.misc.ModSounds;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;

public class DummySpell extends Spell {
    public DummySpell(Identifier id, int energyUsed, int cooldown, int color) {
        super(id, energyUsed, cooldown, color);
    }

    @Override
    protected boolean cast(ServerPlayerEntity player) {
        player.getWorld().spawnEntity(new DummyEntity(player));
        return true;
    }

    @Override
    protected void castEffects(ServerPlayerEntity player) {
        super.castEffects(player);
        SoundUtils.playSoundAtPosition((ServerWorld) player.getWorld(), player.getEyePos(), ModSounds.DUMMY_SPELL_CAST, SoundCategory.PLAYERS, 1, 1);
    }
}

package com.daniel99j.starbound.magic.spell.utility;

import com.daniel99j.lib99j.api.EntityUtils;
import com.daniel99j.lib99j.api.SoundUtils;
import com.daniel99j.starbound.magic.spell.Spell;
import com.daniel99j.starbound.misc.ModSounds;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;

public class MagnetSpell extends Spell {
    public MagnetSpell(Identifier id, int energyUsed, int cooldown, int color) {
        super(id, energyUsed, cooldown, color);
    }

    @Override
    protected boolean cast(ServerPlayerEntity player) {
        boolean out = false;
        for(ItemEntity e : player.getWorld().getEntitiesByClass(ItemEntity.class, new Box(player.getPos().add(-10, -10, -10), player.getPos().add(10, 10, 10)), itemEntity -> itemEntity.distanceTo(player) <= 10)) {
            EntityUtils.accelerateTowards(e, player.getEyePos(), 1);
            SoundUtils.playSoundAtPosition((ServerWorld) player.getWorld(), player.getEyePos(), ModSounds.MAGNET_ATTRACT, SoundCategory.PLAYERS, 1, 0);
            out = true;
        };
        return out;
    }

    @Override
    protected void castEffects(ServerPlayerEntity player) {
        super.castEffects(player);
        SoundUtils.playSoundAtPosition((ServerWorld) player.getWorld(), player.getEyePos(), ModSounds.MAGNET_SPELL_CAST, SoundCategory.PLAYERS, 1, 0);
    }
}

package com.daniel99j.starbound.magic.spell.defensive;

import com.daniel99j.lib99j.api.SoundUtils;
import com.daniel99j.starbound.magic.spell.Spell;
import com.daniel99j.starbound.misc.ModSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class LightningSpell extends Spell {
    public LightningSpell(Identifier id, int energyUsed, int cooldown, int color) {
        super(id, energyUsed, cooldown, color);
    }

    @Override
    protected boolean cast(ServerPlayerEntity player) {
        raycast(player, 20, 20, 0.5f, player.getEyePos(), player.getPitch(), player.getYaw(), false);
        return currentCheck;
    }

    @Override
    protected void raycast(ServerPlayerEntity player, int remaining, int steps, float distance, Vec3d currentPos, float pitch, float yaw, boolean hit) {
        if(hit) {
            LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, player.getWorld());
            lightning.setPosition(Vec3d.ofBottomCenter(BlockPos.ofFloored(currentPos)));
            lightning.setCosmetic(false);
            player.getWorld().spawnEntity(lightning);
            currentCheck = true;
        } else super.raycast(player, remaining, steps, distance, currentPos, pitch, yaw, hit);
    }

    @Override
    protected void castEffects(ServerPlayerEntity player) {
        super.castEffects(player);
        SoundUtils.playSoundAtPosition((ServerWorld) player.getWorld(), player.getEyePos(), ModSounds.LIGHTNING_SPELL_CAST, SoundCategory.PLAYERS, 1.0f, 1.0f);
    }
}

package com.daniel99j.starbound.magic.spell.other;

import com.daniel99j.lib99j.api.ParticleHelper;
import com.daniel99j.lib99j.api.SoundUtils;
import com.daniel99j.starbound.magic.spell.Spell;
import com.daniel99j.starbound.misc.ModSounds;
import com.daniel99j.starbound.misc.PossessionAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.concurrent.atomic.AtomicBoolean;

public class PossessSpell extends Spell {
    private boolean currentCheck;
    public PossessSpell(Identifier id, int energyUsed, int cooldown, int color) {
        super(id, energyUsed, cooldown, color);
    }

    @Override
    protected boolean cast(ServerPlayerEntity player) {
        raycast(player, 20, 20, 0.5f, player.getEyePos(), player.getPitch(), player.getYaw(), false);
        return currentCheck;
    }

    @Override
    protected void castEffects(ServerPlayerEntity player) {
        super.castEffects(player);
        SoundUtils.playSoundAtPosition((ServerWorld) player.getWorld(), player.getEyePos(), ModSounds.POSSESS_SPELL_CAST, SoundCategory.PLAYERS, 1, 1);
    }

    @Override
    protected void raycast(ServerPlayerEntity player, int remaining, int steps, float distance, Vec3d currentPos, float pitch, float yaw, boolean hit) {
        AtomicBoolean shouldContinue = new AtomicBoolean(true);
        float offset = 0.3f+(1f/((float) steps/remaining));
        player.getWorld().getOtherEntities(player, new Box(currentPos.add(-offset, -offset, -offset), currentPos.add(offset, offset, offset))).forEach((e) -> {
            if(e instanceof LivingEntity livingEntity) {
                shouldContinue.set(false);
                ((PossessionAccessor) player).starbound$setPossessedEntity(livingEntity);
                currentCheck = true;
            }
        });
        if(shouldContinue.get()) super.raycast(player, remaining, steps, distance, currentPos, pitch, yaw, hit);
        ParticleHelper.spawnParticlesAtPosition(player.getWorld(), currentPos, ParticleTypes.SPLASH, 2, offset, offset, offset, 0);
    }
}

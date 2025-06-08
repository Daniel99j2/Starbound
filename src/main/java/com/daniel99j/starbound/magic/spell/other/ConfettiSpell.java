package com.daniel99j.starbound.magic.spell.other;

import com.daniel99j.lib99j.api.NumberUtils;
import com.daniel99j.lib99j.api.SoundUtils;
import com.daniel99j.starbound.magic.spell.Spell;
import com.daniel99j.starbound.misc.ModSounds;
import com.daniel99j.starbound.particle.particles.ConfettiParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class ConfettiSpell extends Spell {
    public ConfettiSpell(Identifier id, int energyUsed, int cooldown, int color) {
        super(id, energyUsed, cooldown, color);
    }

    @Override
    protected boolean cast(ServerPlayerEntity player) {
        raycast(player, 20, 20, 0.5f, player.getEyePos(), player.getPitch(), player.getYaw(), false);
        int max = 50;
        for (int i = 0; i < max; i++) {
            double yawRad = Math.toRadians(player.getYaw()+NumberUtils.getRandomFloat(-3, 3));
            double pitchRad = Math.toRadians(player.getPitch()+NumberUtils.getRandomFloat(-3, 3));

            double x = -Math.sin(yawRad) * Math.cos(pitchRad);
            double y = -Math.sin(pitchRad);
            double z = Math.cos(yawRad) * Math.cos(pitchRad);

            Vec3d accelerationVector = new Vec3d(x, y, z).normalize().multiply((double) 0.5 /((double) 50 /i));

            new ConfettiParticleEffect((ServerWorld) player.getWorld(), player.getX(), player.getEyeY(), player.getZ(), accelerationVector.getX(), accelerationVector.getY(), accelerationVector.getZ());
        }
        return true;
    }

    @Override
    protected void castEffects(ServerPlayerEntity player) {
        super.castEffects(player);
        SoundUtils.playSoundAtPosition((ServerWorld) player.getWorld(), player.getEyePos(), ModSounds.CONFETTI_SPELL_CAST, SoundCategory.PLAYERS, 1, 1);
    }
}

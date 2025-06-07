package com.daniel99j.starbound.magic.spell;

import com.daniel99j.lib99j.api.EntityUtils;
import com.daniel99j.lib99j.api.ParticleHelper;
import com.daniel99j.lib99j.api.SoundUtils;
import com.daniel99j.lib99j.api.VFXUtils;
import com.daniel99j.starbound.misc.ModDamageTypes;
import com.daniel99j.starbound.misc.ModSounds;
import de.tomalbrc.bil.core.extra.ModelEntity;
import de.tomalbrc.bil.core.model.Model;
import de.tomalbrc.bil.file.loader.AjBlueprintLoader;
import eu.pb4.polymer.virtualentity.api.tracker.InteractionTrackedData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class SonicBoomSpell extends Spell {
    public SonicBoomSpell(Identifier id, int energyUsed, int cooldown) {
        super(id, energyUsed, cooldown);
    }

    @Override
    protected boolean cast(ServerPlayerEntity player) {
        raycast(player, 10, 10, 0.5f, player.getEyePos(), player.getPitch(), player.getYaw(), false);
        return true;
    }

    @Override
    protected void castEffects(ServerPlayerEntity player) {
        super.castEffects(player);
        SoundUtils.playSoundAtPosition((ServerWorld) player.getWorld(), player.getEyePos(), ModSounds.SONIC_SPELL_CAST, SoundCategory.PLAYERS, 1, 1);
        SoundUtils.playSoundAtPosition((ServerWorld) player.getWorld(), player.getEyePos(), ModSounds.SONIC_SPELL_CAST_1, SoundCategory.PLAYERS, 1, 1);
    }

    @Override
    protected void raycast(ServerPlayerEntity player, int remaining, int steps, float distance, Vec3d currentPos, float pitch, float yaw, boolean hit) {
        super.raycast(player, remaining, steps, distance, currentPos, pitch, yaw, hit);
        player.getWorld().getOtherEntities(player, new Box(currentPos.add(-0.5, -0.5, -0.5), currentPos.add(0.5, 0.5, 0.5))).forEach((e) -> {
            if(e instanceof LivingEntity livingEntity) {
                livingEntity.damage((ServerWorld) player.getWorld(), livingEntity.getDamageSources().sonicBoom(player), 16);
            }
        });
        ParticleHelper.spawnParticlesAtPosition(player.getWorld(), currentPos, ParticleTypes.SONIC_BOOM, 1, 0, 0, 0, 0);
    }
}

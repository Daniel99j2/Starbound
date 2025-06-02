package com.daniel99j.starbound.magic.spell;

import com.daniel99j.lib99j.api.EntityUtils;
import com.daniel99j.lib99j.api.ParticleHelper;
import com.daniel99j.lib99j.api.SoundUtils;
import com.daniel99j.lib99j.api.VFXUtils;
import com.daniel99j.starbound.misc.ModDamageTypes;
import de.tomalbrc.bil.core.extra.ModelEntity;
import de.tomalbrc.bil.core.model.Model;
import de.tomalbrc.bil.file.loader.AjBlueprintLoader;
import de.tomalbrc.bil.file.loader.AjModelLoader;
import de.tomalbrc.bil.file.loader.BbModelLoader;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class DeathRaySpell extends Spell {
    public static Model ANIMATION_MODEL = AjBlueprintLoader.load(Identifier.of("starbound", "death_ray"));
    public DeathRaySpell(Identifier id, int energyUsed, int cooldown) {
        super(id, energyUsed, cooldown);
    }

    @Override
    protected boolean cast(ServerPlayerEntity player) {
        ModelEntity model = new ModelEntity(player.getWorld(), ANIMATION_MODEL);
        model.setPos(player.getX(), player.getY(), player.getZ());
        player.getWorld().spawnEntity(model);
        model.getHolder().getAnimator().playAnimation("blast", (frame) -> {
            if(frame == 50) {
                SoundUtils.playSoundAtPosition((ServerWorld) player.getWorld(), player.getEyePos(), SoundEvents.BLOCK_BEACON_POWER_SELECT, SoundCategory.PLAYERS, 1, 1);
                SoundUtils.playSoundAtPosition((ServerWorld) player.getWorld(), player.getEyePos(), SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.PLAYERS, 1, 0);
                for(Entity e : EntityUtils.getWatching(model)) {
                    if(e instanceof ServerPlayerEntity player1 && player1.distanceTo(model) < 64) {
                        VFXUtils.shake(player1, 30, 5, Identifier.of("starbound", "death_ray"));
                    }
                }
                player.getWorld().getOtherEntities(null, new Box(model.getPos().add(-1.5, 0, -1.5), model.getPos().add(1.5, 100, 1.5))).forEach((e) -> {
                    if(!(e instanceof ItemEntity) && Math.sqrt(e.squaredDistanceTo(new Vec3d(model.getX(), e.getY(), model.getZ()))) <= 1.5) e.damage((ServerWorld) e.getWorld(), new DamageSource(ModDamageTypes.of(e.getWorld(), ModDamageTypes.PULSAR_BEAM)), 1000);
                });
            }
        }, () -> {
            model.remove(Entity.RemovalReason.DISCARDED);
        });
        return true;
    }

    @Override
    protected void castEffects(ServerPlayerEntity player) {
        super.castEffects(player);
        SoundUtils.playSoundAtPosition((ServerWorld) player.getWorld(), player.getEyePos(), SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.PLAYERS, 1, 1);
    }

    @Override
    protected void raycast(ServerPlayerEntity player, int remaining, int steps, float distance, Vec3d currentPos, float pitch, float yaw) {
        super.raycast(player, remaining, steps, distance, currentPos, pitch, yaw);
        float offset = 0.3f+(1f/((float) steps/remaining));
        if(player.getWorld().getBlockState(BlockPos.ofFloored(currentPos)).isAir()) player.getWorld().setBlockState(BlockPos.ofFloored(currentPos), Blocks.WATER.getDefaultState());
        ParticleHelper.spawnParticlesAtPosition(player.getWorld(), currentPos, ParticleTypes.SPLASH, 2, offset, offset, offset, 0);
    }
}

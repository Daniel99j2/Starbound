package com.daniel99j.starbound.magic.spell;

import com.daniel99j.lib99j.api.EntityUtils;
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
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class DeathRaySpell extends Spell {
    public static Model ANIMATION_MODEL = AjBlueprintLoader.load(Identifier.of("starbound", "death_ray"));

    private boolean currentCheck;

    public DeathRaySpell(Identifier id, int energyUsed, int cooldown) {
        super(id, energyUsed, cooldown);
    }

    @Override
    protected boolean cast(ServerPlayerEntity player) {
        currentCheck = false;
        raycast(player, 3, 3, 1, player.getEyePos(), player.getPitch(), player.getYaw(), false);
        return currentCheck;
    }

    @Override
    protected void castEffects(ServerPlayerEntity player) {
        super.castEffects(player);
        SoundUtils.playSoundAtPosition((ServerWorld) player.getWorld(), player.getEyePos(), ModSounds.DEATH_RAY_SPELL_CAST, SoundCategory.PLAYERS, 1, 1);
    }

    @Override
    protected void raycast(ServerPlayerEntity player, int remaining, int steps, float distance, Vec3d currentPos, float pitch, float yaw, boolean hit) {
        if(remaining <= 0 || hit) {
            Vec3d pos = Vec3d.ofCenter(BlockPos.ofFloored(currentPos));
            boolean changed = false;

            for (int i = 0; i < 10; i++) {
                if (!player.getWorld().getBlockState(BlockPos.ofFloored(pos.offset(Direction.DOWN, i))).getCollisionShape(player.getWorld(), BlockPos.ofFloored(pos.offset(Direction.DOWN, i))).isEmpty()) {
                    Vec3d pos1 = pos;
                    pos = new Vec3d(pos1.x, player.getWorld().getBlockState(BlockPos.ofFloored(pos1.offset(Direction.DOWN, i))).getCollisionShape(player.getWorld(), BlockPos.ofFloored(pos1.offset(Direction.DOWN, i))).getFace(Direction.UP).getBoundingBox().maxY+pos1.offset(Direction.DOWN, i).getY(), pos1.z);
                    changed = true;
                    break;
                }
            }

            if (changed) {
                currentCheck = true;
                ModelEntity model = new ModelEntity(player.getWorld(), ANIMATION_MODEL) {
                    @Override
                    public void modifyRawTrackedData(List<DataTracker.SerializedEntry<?>> data, ServerPlayerEntity player, boolean initial) {
                        super.modifyRawTrackedData(data, player, initial);
                        data.add(DataTracker.SerializedEntry.of(InteractionTrackedData.HEIGHT, 0F));
                        data.add(DataTracker.SerializedEntry.of(InteractionTrackedData.WIDTH, 0F));
                    }
                };
                model.setPos(pos.getX(), pos.getY(), pos.getZ());
                player.getWorld().spawnEntity(model);
                model.getHolder().getAnimator().playAnimation("blast", (frame) -> {
                    if (frame == 50) {
                        SoundUtils.playSoundAtPosition((ServerWorld) player.getWorld(), player.getEyePos(), ModSounds.DEATH_RAY_BLAST, SoundCategory.PLAYERS, 1, 1);
                        SoundUtils.playSoundAtPosition((ServerWorld) player.getWorld(), player.getEyePos(), ModSounds.DEATH_RAY_EXPLOSION, SoundCategory.PLAYERS, 1, 0);
                        for (Entity e : EntityUtils.getWatching(model)) {
                            if (e instanceof ServerPlayerEntity player1 && player1.distanceTo(model) < 64) {
                                VFXUtils.shake(player1, 30, 5, Identifier.of("starbound", "death_ray"));
                            }
                        }
                        new ArrayList<>(player.getWorld().getOtherEntities(null, new Box(model.getPos().add(-1.5, 0, -1.5), model.getPos().add(1.5, 100, 1.5)))).forEach((e) -> {
                            if (!(e instanceof ItemEntity) && Math.sqrt(e.squaredDistanceTo(new Vec3d(model.getX(), e.getY(), model.getZ()))) <= 1.5)
                                e.damage((ServerWorld) e.getWorld(), new DamageSource(ModDamageTypes.of(e.getWorld(), ModDamageTypes.PULSAR_BEAM)), 1000);
                        });
                    }
                }, () -> {
                    model.remove(Entity.RemovalReason.DISCARDED);
                });
            }
        } else {
            super.raycast(player, remaining, steps, distance, currentPos, pitch, yaw, hit);
        }
    }
}

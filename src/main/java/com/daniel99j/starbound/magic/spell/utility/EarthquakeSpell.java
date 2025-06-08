package com.daniel99j.starbound.magic.spell.utility;

import com.daniel99j.lib99j.api.EntityUtils;
import com.daniel99j.lib99j.api.ParticleHelper;
import com.daniel99j.lib99j.api.SoundUtils;
import com.daniel99j.lib99j.api.VFXUtils;
import com.daniel99j.starbound.magic.spell.Spell;
import com.daniel99j.starbound.misc.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;

import java.util.UUID;

public class EarthquakeSpell extends Spell {
    public EarthquakeSpell(Identifier id, int energyUsed, int cooldown, int color) {
        super(id, energyUsed, cooldown, color);
    }

    @Override
    protected boolean cast(ServerPlayerEntity player) {
        player.getWorld()
                .createExplosion(
                        null,
                        null,
                        new ExplosionBehavior() {
                            @Override
                            public boolean canDestroyBlock(Explosion explosion, BlockView world, BlockPos pos, BlockState state, float power) {
                                if(!state.isAir() && super.canDestroyBlock(explosion, world, pos, state, power)) {
                                    FallingBlockEntity fallingBlockEntity = FallingBlockEntity.spawnFromBlock((World) world, pos, state);
                                    fallingBlockEntity.setUuid(UUID.randomUUID());
                                    EntityUtils.accelerateTowards(fallingBlockEntity, player.getPos().offset(Direction.DOWN, 4), -0.5f);
                                    player.getWorld().spawnEntity(fallingBlockEntity);
                                }
                                return false;
                            }

                            @Override
                            public boolean shouldDamage(Explosion explosion, Entity entity) {
                                return false;
                            }
                        },
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        2,
                        false,
                        World.ExplosionSourceType.TRIGGER
                );
        return true;
    }

    @Override
    protected void castEffects(ServerPlayerEntity player) {
        super.castEffects(player);
        for (Entity e : EntityUtils.getWatching(player)) {
            if (e instanceof ServerPlayerEntity player1 && player1.distanceTo(player) < 16) {
                VFXUtils.shake(player1, 10, 5, Identifier.of("starbound", "earthquake"));
            }
        }
        SoundUtils.playSoundAtPosition((ServerWorld) player.getWorld(), player.getPos(), ModSounds.EARTHQUAKE_SPELL_CAST, SoundCategory.PLAYERS, 1.0f, 0.5f);
        ParticleHelper.spawnParticlesAtPosition(player.getWorld(), player.getPos(), ParticleTypes.EXPLOSION, 3, 1.0, 0.1, 1.0, 0);
    }
}

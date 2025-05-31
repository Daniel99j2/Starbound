package com.daniel99j.starbound.magic.spell;

import com.daniel99j.lib99j.api.ParticleHelper;
import com.daniel99j.lib99j.api.SoundUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.TeleportTarget;

public class PhaseSpell extends Spell {
    public PhaseSpell(Identifier id, int energyUsed, int cooldown) {
        super(id, energyUsed, cooldown);
    }

    @Override
    protected void cast(ServerPlayerEntity player) {
        raycastCustom(player, 7*4, 0.25f, player.getEyePos(), player.getPitch(), player.getYaw(), false);
    }

    private void raycastCustom(ServerPlayerEntity player, int remaining, float distance, Vec3d currentPos, float pitch, float yaw, boolean hasBeenSolid) {
        if (remaining > 0) {
            double yawRad = Math.toRadians(yaw);
            double pitchRad = Math.toRadians(pitch);

            double x = -Math.sin(yawRad) * Math.cos(pitchRad);
            double y = -Math.sin(pitchRad);
            double z = Math.cos(yawRad) * Math.cos(pitchRad);

            Vec3d accelerationVector = new Vec3d(x, y, z).normalize().multiply(distance);

            Vec3d newPos = currentPos.add(accelerationVector.x, accelerationVector.y, accelerationVector.z);

            if (FabricLoader.getInstance().isDevelopmentEnvironment())
                ParticleHelper.spawnParticlesAtPosition(player.getWorld(), currentPos, hasBeenSolid ? ParticleTypes.GUST : ParticleTypes.HEART, 1, 0, 0, 0, 0);

            float f = player.getDimensions(player.getPose()).width() * 0.8F;
            Box box = Box.of(currentPos, f, 1.0E-6, f);
            boolean collidesAtPos = BlockPos.stream(box)
                    .anyMatch(
                            pos -> {
                                BlockState blockState = player.getWorld().getBlockState(pos);
                                return !blockState.isAir() && VoxelShapes.matchesAnywhere(blockState.getCollisionShape(player.getWorld(), pos).offset(pos), VoxelShapes.cuboid(box), BooleanBiFunction.AND);
                            }
                    );

            if (collidesAtPos && !hasBeenSolid) {
                hasBeenSolid = true;
                raycastCustom(player, 5 * 4, 0.25f, newPos, pitch, yaw, hasBeenSolid);
            } else if (!collidesAtPos && hasBeenSolid) {
                player.teleportTo(new TeleportTarget((ServerWorld) player.getWorld(), new Vec3d(currentPos.getX(), currentPos.getY() - player.getStandingEyeHeight(), currentPos.getZ()), Vec3d.ZERO, player.getYaw(), player.getPitch(), TeleportTarget.NO_OP));
                player.getWorld().sendEntityStatus(player, EntityStatuses.ADD_PORTAL_PARTICLES);
                SoundUtils.playSoundAtPosition((ServerWorld) player.getWorld(), player.getEyePos(), SoundEvents.ENTITY_PLAYER_TELEPORT, SoundCategory.PLAYERS, 1, 1);
            } else raycastCustom(player, remaining - 1, distance, newPos, pitch, yaw, hasBeenSolid);
        }
    }
}
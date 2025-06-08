package com.daniel99j.starbound.magic.spell.defensive;

import com.daniel99j.starbound.magic.spell.Spell;
import net.minecraft.util.Identifier;

public class VoidRiftSpell extends Spell {
    public VoidRiftSpell(Identifier id, int energyUsed, int cooldown, int color) {
        super(id, energyUsed, cooldown, color);
    }

//    @Override
//    protected boolean cast(ServerPlayerEntity player) {
//        // Get player position and look vector
//        Vec3d pos = player.getEyePos();
//        Vec3d lookVec = player.getRotationVector();
//
//        // Calculate the target position for the void rift (about 10 blocks in front of player)
//        Vec3d targetPos = pos.add(lookVec.multiply(10));
//        ServerWorld world = (ServerWorld) player.getWorld();
//
//        // Create void rift effect
//        AtomicBoolean affectedAny = new AtomicBoolean(false);
//
//        // Spawn initial void rift particles
//        ParticleHelper.spawnParticlesAtPosition(world, targetPos,
//                ParticleTypes.PORTAL, 500, 1.0, 1.0, 1.0, 0.5);
//
//        // Play void rift creation sound
//        SoundUtils.playSoundAtPosition(world, targetPos,
//                SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.PLAYERS, 1.0f, 0.5f);
//
//        // Apply pull effect and damage to entities around the rift
//        Box effectArea = new Box(targetPos.x - 8, targetPos.y - 8, targetPos.z - 8,
//                                targetPos.x + 8, targetPos.y + 8, targetPos.z + 8);
//
//        List<Entity> entities = world.getOtherEntities(player, effectArea);
//
//        // Pull entities toward the rift and damage them
//        entities.forEach(entity -> {
//            // Calculate vector from entity to rift
//            Vec3d pullVector = targetPos.subtract(entity.getPos()).normalize();
//
//            // Strength of pull is inversely proportional to distance
//            double distance = entity.getPos().distanceTo(targetPos);
//            double pullStrength = Math.max(0.5, 4.0 - (distance / 2));
//
//            // Apply pull vector
//            entity.addVelocity(pullVector.x * pullStrength * 0.2,
//                               pullVector.y * pullStrength * 0.2,
//                               pullVector.z * pullStrength * 0.2);
//            entity.velocityModified = true;
//
//            // Apply void damage if entity is very close to the rift
//            if (distance < 3.0 && entity instanceof LivingEntity livingEntity) {
//                livingEntity.damage(world, entity.getDamageSources().cactus(), 2.0f);
//            }
//
//            affectedAny.set(true);
//        });
//
//        // Schedule effects over time
//        for (int i = 1; i <= 6; i++) {
//            // Continue void rift effects
//            ParticleHelper.spawnParticlesAtPosition(world, targetPos,
//                    ParticleTypes.REVERSE_PORTAL, 50, 1.0, 1.0, 1.0, 0.2);
//
//            // Pull entities again for continuous effect
//            world.getOtherEntities(player, effectArea).forEach(entity -> {
//                Vec3d pullVector = targetPos.subtract(entity.getPos()).normalize();
//                double distance = entity.getPos().distanceTo(targetPos);
//                double pullStrength = Math.max(0.5, 4.0 - (distance / 2));
//
//                entity.addVelocity(pullVector.x * pullStrength * 0.1,
//                        pullVector.y * pullStrength * 0.1,
//                        pullVector.z * pullStrength * 0.1);
//                entity.velocityModified = true;
//
//                // More damage as time passes if close to rift
//                if (distance < 2.0 && entity instanceof LivingEntity livingEntity) {
//                    livingEntity.damage(world, entity.getDamageSources().cactus(), 1.0f);
//                }
//            });
//        }
//
//        return true;
//    }
//
//    @Override
//    protected void castEffects(ServerPlayerEntity player) {
//        super.castEffects(player);
//
//        // Visual effect around the caster
//        ParticleHelper.spawnParticlesAtPosition(player.getWorld(), player.getPos().add(0, 1, 0),
//                ParticleTypes.PORTAL, 30, 0.5, 1.0, 0.5, 0.1);
//
//        // Play spell cast sound
//        SoundUtils.playSoundAtPosition((ServerWorld) player.getWorld(), player.getEyePos(),
//                SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.PLAYERS, 0.6f, 1.2f);
//    }
}

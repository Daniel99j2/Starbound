package com.daniel99j.starbound.mixin;

import com.daniel99j.starbound.magic.PrismLensTrailManager;
import com.daniel99j.starbound.misc.PossessionAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Inject(method = "playStepSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;playStepSound(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"))
    public void starbound$addStepParticles(CallbackInfo ci) {
        if(!((PlayerEntity) (Object) this).getWorld().isClient) {
            if(((PlayerEntity) (Object) this).hasStatusEffect(StatusEffects.INVISIBILITY)) {
                PrismLensTrailManager.addInvisTrail((ServerWorld) ((PlayerEntity) (Object) this).getWorld(), ((PlayerEntity) (Object) this).getPos());
            } else {
                PrismLensTrailManager.addTrail((ServerWorld) ((PlayerEntity) (Object) this).getWorld(), ((PlayerEntity) (Object) this).getPos());
            }
        }
    }
}
package com.daniel99j.starbound.mixin;

import com.daniel99j.starbound.misc.PossessionAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin
        extends LivingEntity {

    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "getControllingPassenger", at = @At("HEAD"), cancellable = true)
    public void starbound$makePossessionControl(CallbackInfoReturnable<LivingEntity> cir) {
        if(((PossessionAccessor) this).starbound$getPossessingOrPossessedBy() != null) {
            cir.setReturnValue(((PossessionAccessor) this).starbound$getPossessingOrPossessedBy());
        }
    }

    @Inject(method = "canMoveVoluntarily", at = @At("HEAD"), cancellable = true)
    public void starbound$makePossessionControl1(CallbackInfoReturnable<Boolean> cir) {
        if(((PossessionAccessor) this).starbound$getPossessingOrPossessedBy() != null) cir.setReturnValue(false);
    }

    @Inject(method = "canActVoluntarily", at = @At("HEAD"), cancellable = true)
    public void starbound$makePossessionControl2(CallbackInfoReturnable<Boolean> cir) {
        if(((PossessionAccessor) this).starbound$getPossessingOrPossessedBy() != null) cir.setReturnValue(false);
    }
}
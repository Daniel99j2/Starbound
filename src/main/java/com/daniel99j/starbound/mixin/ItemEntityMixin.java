package com.daniel99j.starbound.mixin;

import com.daniel99j.starbound.item.ModItems;
import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Inject(method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V", at = @At(value = "TAIL"))
    public void starbound$gravityAnchorFloat(CallbackInfo ci) {
        if(((ItemEntity) (Object) this).getStack().getItem() == ModItems.GRAVITY_ANCHOR_ITEM) {
            ((ItemEntity) (Object) this).setNoGravity(true);
        }
    }
}
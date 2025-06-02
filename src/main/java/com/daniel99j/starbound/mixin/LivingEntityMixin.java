package com.daniel99j.starbound.mixin;

import com.daniel99j.starbound.misc.FullFrozenAccessor;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.attachment.EntityAttachment;
import eu.pb4.polymer.virtualentity.api.elements.*;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin
        extends Entity implements FullFrozenAccessor {
    @Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);

    @Unique
    private ElementHolder starbound$livingEntityHolder;
    @Unique
    private BlockDisplayElement starbound$iceCube;
    @Unique
    private boolean starbound$isFullFrozen;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Unique
    private LivingEntity getEntity() {
        Entity entity = this;
        return (LivingEntity) entity;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void starbound$customTick(CallbackInfo ci) {
        if (this.starbound$livingEntityHolder == null) {
            this.starbound$livingEntityHolder = new ElementHolder();
            this.starbound$livingEntityHolder.setAttachment(new EntityAttachment(this.starbound$livingEntityHolder, getEntity(), true));
        }
        if (this.starbound$iceCube == null && starbound$isFullFrozen) {
            this.starbound$iceCube = new BlockDisplayElement(Blocks.ICE.getDefaultState());
            this.starbound$iceCube.setInvisible(true);
            this.starbound$iceCube.setTeleportDuration(5);
            this.starbound$livingEntityHolder.addElement(this.starbound$iceCube);
        }

        if (this.starbound$iceCube != null && !starbound$isFullFrozen) {
            this.starbound$livingEntityHolder.removeElement(this.starbound$iceCube);
            this.starbound$iceCube = null;
        }

        if (this.starbound$isFullFrozen) {
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 10, 255, true, false));
            this.setFrozenTicks(200);
            float width = this.getWidth()+0.3f;
            this.starbound$iceCube.setOffset(new Vec3d(-width/2, 0, -width/2));
            this.starbound$iceCube.setScale(new Vector3f(width, this.getHeight(), width));
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    protected void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("starbound:is_fully_frozen", this.starbound$isFullFrozen);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    protected void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        this.starbound$isFullFrozen = nbt.getBoolean("starbound:is_fully_frozen", false);
    }

    @Override
    public void starbound$setFullFrozen(boolean fullFrozen) {
        starbound$isFullFrozen = fullFrozen;
    }
}
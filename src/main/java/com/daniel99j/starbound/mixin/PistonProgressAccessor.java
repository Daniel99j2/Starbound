package com.daniel99j.starbound.mixin;

import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PistonBlockEntity.class)
public interface PistonProgressAccessor {
	@Accessor(value = "progress", remap = false)
	void setProgress(float progress);
}
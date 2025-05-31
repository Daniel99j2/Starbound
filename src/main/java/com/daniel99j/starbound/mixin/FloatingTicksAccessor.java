package com.daniel99j.starbound.mixin;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerPlayNetworkHandler.class)
public interface FloatingTicksAccessor {
	@Accessor(value = "floatingTicks", remap = false)
	void setFloatingTicks(int floatingTicks);
}
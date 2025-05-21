package com.daniel99j.starbound.mixin;

import de.tomalbrc.bil.core.holder.wrapper.DisplayWrapper;
import de.tomalbrc.danse.poly.PlayerPartHolder;
import de.tomalbrc.danse.util.MinecraftSkinParser;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(PlayerPartHolder.class)
public interface SkinDataAccessor {
	@Accessor(value = "locatorPartMap", remap = false)
	Map<MinecraftSkinParser.BodyPart, DisplayWrapper<ItemDisplayElement>> getPartMap();
}
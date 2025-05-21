package com.daniel99j.starbound.util;

import com.daniel99j.starbound.mixin.SkinDataAccessor;
import de.tomalbrc.bil.core.holder.wrapper.DisplayWrapper;
import de.tomalbrc.danse.entity.AnimatedPlayerModelEntity;
import de.tomalbrc.danse.entity.StatuePlayerModelEntity;
import de.tomalbrc.danse.registry.EntityRegistry;
import de.tomalbrc.danse.util.MinecraftSkinParser;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Uuids;

import java.util.Map;
import java.util.Optional;

public class PlayerSkinUtils {
    public static Map<MinecraftSkinParser.BodyPart, DisplayWrapper<ItemDisplayElement>> getSkin(ServerPlayerEntity player) {
        StatuePlayerModelEntity playerModelEntity = new StatuePlayerModelEntity(EntityRegistry.PLAYER_STATUE, player.getWorld());
        NbtCompound data = new NbtCompound();
        data.put("PlayerUUID", Uuids.STRICT_CODEC, player.getUuid());
        playerModelEntity.readCustomDataFromNbt(data);
        playerModelEntity.setProfile(Optional.of(player.getGameProfile()));
        return ((SkinDataAccessor) playerModelEntity.getHolder()).getPartMap();
    }
}

package com.daniel99j.starbound.util;

import de.tomalbrc.bil.core.holder.wrapper.Bone;
import de.tomalbrc.bil.core.holder.wrapper.DisplayWrapper;
import de.tomalbrc.danse.entity.StatuePlayerModelEntity;
import de.tomalbrc.danse.poly.PlayerPartHolder;
import de.tomalbrc.danse.registry.EntityRegistry;
import de.tomalbrc.danse.util.MinecraftSkinParser;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import eu.pb4.polymer.virtualentity.api.tracker.DisplayTrackedData;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Uuids;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PlayerSkinUtils {
    public static Map<MinecraftSkinParser.BodyPart, ItemStack> getSkin(ServerPlayerEntity player) {
        StatuePlayerModelEntity playerModelEntity = new StatuePlayerModelEntity(EntityRegistry.PLAYER_STATUE, player.getWorld());
        NbtCompound data = new NbtCompound();
        data.put("PlayerUUID", Uuids.STRICT_CODEC, player.getUuid());
        playerModelEntity.readCustomDataFromNbt(data);
        playerModelEntity.setProfile(Optional.of(player.getGameProfile()));
        Map<MinecraftSkinParser.BodyPart, ItemStack> out = new HashMap<>();
        for(Bone<?> x : playerModelEntity.getHolder().getBones()) {
            if (x instanceof PlayerPartHolder.MultipartModelBone bone) {
                MinecraftSkinParser.BodyPart part = MinecraftSkinParser.BodyPart.partFrom(bone.name());
                if (part != MinecraftSkinParser.BodyPart.NONE) {
                    out.put(part, bone.element().getDataTracker().get(DisplayTrackedData.Item.ITEM));
                }
            }
        }
        return out;
    }
}

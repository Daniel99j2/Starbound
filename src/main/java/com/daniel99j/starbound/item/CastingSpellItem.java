package com.daniel99j.starbound.item;

import com.daniel99j.lib99j.api.ItemUtils;
import com.daniel99j.starbound.util.PlayerSkinUtils;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import de.tomalbrc.danse.util.MinecraftSkinParser;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.SetPlayerInventoryS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.List;

public class CastingSpellItem extends Item implements PolymerItem {
    private ServerPlayerEntity owner;

    public CastingSpellItem(Settings settings) {
        super(settings);
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext packetContext) {
        return ItemUtils.getBasicModelItem();
    }

    @Override
    public ItemStack getPolymerItemStack(ItemStack itemStack, TooltipType tooltipType, PacketContext context) {
        ItemStack out = PolymerItem.super.getPolymerItemStack(itemStack, tooltipType, context);
        if(this.owner != null) {
            out.set(DataComponentTypes.ITEM_MODEL, PlayerSkinUtils.getSkin(this.owner).get(MinecraftSkinParser.BodyPart.LEFT_ARM).element().getItem().get(DataComponentTypes.ITEM_MODEL));
            out.set(DataComponentTypes.CUSTOM_MODEL_DATA, PlayerSkinUtils.getSkin(this.owner).get(MinecraftSkinParser.BodyPart.LEFT_ARM).element().getItem().get(DataComponentTypes.CUSTOM_MODEL_DATA));
        }
        return out;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ((ServerPlayerEntity) user).networkHandler.sendPacket(new SetPlayerInventoryS2CPacket(user.getInventory().getSelectedSlot(), Items.AIR.getDefaultStack()));
        ((ServerPlayerEntity) user).networkHandler.sendPacket(new EntityEquipmentUpdateS2CPacket(user.getId(), List.of(Pair.of(EquipmentSlot.OFFHAND, ModItems.CASTING_SPELL_ITEM.getDefaultStack()))));
        return ActionResult.CONSUME;
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
        if(entity instanceof ServerPlayerEntity player) this.owner = player;
    }
}
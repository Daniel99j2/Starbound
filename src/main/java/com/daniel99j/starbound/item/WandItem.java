package com.daniel99j.starbound.item;

import com.daniel99j.lib99j.api.ItemUtils;
import com.daniel99j.starbound.magic.spell.Spell;
import com.daniel99j.starbound.misc.ModEntityComponents;
import com.daniel99j.starbound.misc.StarboundPlayerData;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.network.ServerPlayerEntity;
import xyz.nucleoid.packettweaker.PacketContext;

public class WandItem extends Item implements PolymerItem {
    public WandItem(Settings settings) {
        super(settings);
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext packetContext) {
        return ItemUtils.getBasicModelItem();
    }

    @Override
    public ItemStack getPolymerItemStack(ItemStack itemStack, TooltipType tooltipType, PacketContext context) {
        ItemStack out = PolymerItem.super.getPolymerItemStack(itemStack, tooltipType, context);
        if(context.getPlayer() instanceof ServerPlayerEntity player) {
            Spell spell = ModEntityComponents.PLAYER_DATA.get(player).getLastCastSpell();
            if(spell != null) out.set(DataComponentTypes.USE_COOLDOWN, spell.getIcon(true).get(DataComponentTypes.USE_COOLDOWN));
            if(spell != null) out.set(DataComponentTypes.CUSTOM_MODEL_DATA, spell.getIcon(true).get(DataComponentTypes.CUSTOM_MODEL_DATA));
            if(spell != null) out.set(DataComponentTypes.ITEM_MODEL, spell.getIcon(true).get(DataComponentTypes.ITEM_MODEL));
            out.set(DataComponentTypes.MAX_DAMAGE, StarboundPlayerData.MAX_ENERGY);
            out.setDamage(StarboundPlayerData.MAX_ENERGY-ModEntityComponents.PLAYER_DATA.get(player).getEnergy());
        }
        return out;
    }
}
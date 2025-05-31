package com.daniel99j.starbound.item;

import com.daniel99j.lib99j.api.ItemUtils;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import xyz.nucleoid.packettweaker.PacketContext;

public class AstralFabricatorItem extends Item implements PolymerItem {
    public AstralFabricatorItem(Settings settings) {
        super(settings);
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext packetContext) {
        return ItemUtils.getBasicModelItem();
    }
}
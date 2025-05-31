package com.daniel99j.starbound.item;

import com.daniel99j.lib99j.api.ItemUtils;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

public class InvisCloakItem extends TrinketItem implements PolymerItem {
    public InvisCloakItem(Settings settings) {
        super(settings);
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext packetContext) {
        return ItemUtils.getBasicModelItem();
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.tick(stack, slot, entity);
        realTick(entity);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, world, entity, slot);
        if (slot == EquipmentSlot.CHEST && entity instanceof LivingEntity livingEntity) {
            realTick(livingEntity);
        }
    }

    private void realTick(LivingEntity entity) {
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 10, 0, true, false, true));
    }
}
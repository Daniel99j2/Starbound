package com.daniel99j.starbound.item;

import com.daniel99j.lib99j.api.ItemUtils;
import com.daniel99j.starbound.magic.PrismLensTrailManager;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

public class PrismLensGoggles extends TrinketItem implements PolymerItem {
    public PrismLensGoggles(Settings settings) {
        super(settings);
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext packetContext) {
        return ItemUtils.getBasicModelItem();
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.tick(stack, slot, entity);
        if(entity instanceof ServerPlayerEntity player && player.getWorld().getTime() % 5 == 0) {
            realTick((ServerWorld) player.getWorld(), player);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, world, entity, slot);
        if(slot == EquipmentSlot.HEAD && entity instanceof ServerPlayerEntity player && world.getTime() % 5 == 0) {
            realTick(world, player);
        }
    }

    private void realTick(ServerWorld world, ServerPlayerEntity player) {
        PrismLensTrailManager.getTrails(world).forEach((trail) -> {
            if(trail.pos().distanceTo(player.getPos()) <= 16) {
                if (trail.type() == PrismLensTrailManager.LENS_TRAIL_TYPE.REGULAR) {
                    player.networkHandler.sendPacket(new ParticleS2CPacket(new DustParticleEffect(0x00AAFF, Math.max(1, (4f/((float) 20/trail.age().getValue())))), true, true, trail.pos().getX(), trail.pos().getY(), trail.pos().getZ(), (float) 0, (float) 0, (float) 0, (float) 0, 1));
                }
                if (trail.type() == PrismLensTrailManager.LENS_TRAIL_TYPE.INVISIBLE) {
                    player.networkHandler.sendPacket(new ParticleS2CPacket(new DustParticleEffect(0xAAAAAA, Math.max(1, (4f/((float) 20/trail.age().getValue())))), true, true, trail.pos().getX(), trail.pos().getY(), trail.pos().getZ(), (float) 0, (float) 0, (float) 0, (float) 0, 1));
                }
            }
        });
    }
}
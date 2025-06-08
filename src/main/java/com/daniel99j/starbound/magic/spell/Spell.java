package com.daniel99j.starbound.magic.spell;

import com.daniel99j.lib99j.api.ItemUtils;
import com.daniel99j.starbound.misc.ModEntityComponents;
import com.daniel99j.starbound.misc.ModSounds;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.type.UseCooldownComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Spell {
    public final Identifier id;
    public final int energy_used;
    public final int cooldown;
    public final int color;
    protected boolean currentCheck;

    public Spell(Identifier id, int energyUsed, int cooldown, int color) {
        this.id = id;
        this.energy_used = energyUsed;
        this.cooldown = cooldown;
        this.color = color;
        Spells.SPELLS.add(this);
    }

    public final boolean baseCast(ServerPlayerEntity player) {
        if(canCast(player)) {
            currentCheck = false;
            boolean b = cast(player);
            if(b) {
                ModEntityComponents.PLAYER_DATA.get(player).addEnergy(-this.energy_used);
                castEffects(player);
            } else {
                player.networkHandler.sendPacket(new PlaySoundS2CPacket(RegistryEntry.of(ModSounds.SPELL_FAIL), SoundCategory.PLAYERS, player.getX(), player.getY(), player.getZ(), 1, 1, 0L));
            };
            return b;
        }
        return false;
    }

    protected boolean cast(ServerPlayerEntity player) {
        player.sendMessage(Text.of("Basic spell triggered!"));
        return true;
    }

    protected void castEffects(ServerPlayerEntity player) {
        player.getItemCooldownManager().set(this.getIcon(true), this.cooldown);
        player.swingHand(Hand.MAIN_HAND);
    }

    public ItemStack getIcon(ServerPlayerEntity player, boolean checkLocked, boolean showCooldown) {
        boolean isLocked = false;
        if(checkLocked && player != null) {
            isLocked = ModEntityComponents.PLAYER_DATA.get(player).isSpellLocked(this);
        }
        ItemStack display = ItemUtils.setModelUnBridged(ItemUtils.getBasicModelItemStack(), Identifier.of(this.id.getNamespace(), "spell/" + (isLocked ? "locked"  : this.id.getPath())));
        if(showCooldown) display.set(DataComponentTypes.USE_COOLDOWN, new UseCooldownComponent(this.cooldown/20f, Optional.of(Identifier.of(this.id.getNamespace(), "spell/" + this.id.getPath()))));
        MutableText name = this.getName();
        if(isLocked) name = name.formatted(Formatting.GRAY);
        display.set(DataComponentTypes.ITEM_NAME, name);
        List<Text> lore = new ArrayList<>();
        lore.add(this.getDescription().formatted(Formatting.GRAY));
        lore.add(Text.translatable("starbound.spell_energy_used", this.energy_used).formatted(Formatting.BLUE));
        lore.add(Text.translatable("starbound.spell_cooldown", this.cooldown*20).formatted(Formatting.BLUE));
        if(isLocked) lore.add(Text.translatable("starbound.spell_locked").formatted(Formatting.GRAY));
        display.set(DataComponentTypes.LORE, new LoreComponent(List.of(), lore));
        display.set(DataComponentTypes.CUSTOM_MODEL_DATA, new CustomModelDataComponent(List.of(), List.of(), List.of(), List.of(this.color)));
        return display;
    }

    public ItemStack getIcon(ServerPlayerEntity player, boolean showCooldown) {
        return getIcon(player, true, showCooldown);
    }

    public ItemStack getIcon(boolean showCooldown) {
        return getIcon(null, false, showCooldown);
    }

    protected boolean canCast(ServerPlayerEntity player) {
        return !player.getItemCooldownManager().isCoolingDown(this.getIcon(true)) && ModEntityComponents.PLAYER_DATA.get(player).getEnergy() >= this.energy_used;
    }

    protected void raycast(ServerPlayerEntity player, int remaining, int steps, float distance, Vec3d currentPos, float pitch, float yaw, boolean hit) {
        if(remaining > 0) {
            double yawRad = Math.toRadians(yaw);
            double pitchRad = Math.toRadians(pitch);

            double x = -Math.sin(yawRad) * Math.cos(pitchRad);
            double y = -Math.sin(pitchRad);
            double z = Math.cos(yawRad) * Math.cos(pitchRad);

            Vec3d accelerationVector = new Vec3d(x, y, z).normalize().multiply(distance);

            Vec3d newPos = currentPos.add(accelerationVector.x, accelerationVector.y, accelerationVector.z);

            if(!player.getWorld().getBlockState(BlockPos.ofFloored(newPos)).getCollisionShape(player.getWorld(), BlockPos.ofFloored(newPos)).isEmpty()) {
                raycast(player, (remaining-1)/2, steps, distance, currentPos, pitch, yaw, true);
            } else {
                raycast(player, remaining-1, steps, distance, newPos, pitch, yaw, hit);
            }

        }
    }

    public MutableText getName() {
        return Text.translatable("spell."+this.id.getNamespace()+"."+this.id.getPath());
    }

    public MutableText getDescription() {
        return Text.translatable("spell."+this.id.getNamespace()+"."+this.id.getPath()+".desc");
    }
}

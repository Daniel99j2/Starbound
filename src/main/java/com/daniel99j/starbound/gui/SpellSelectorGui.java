package com.daniel99j.starbound.gui;

import com.daniel99j.lib99j.api.ItemUtils;
import com.daniel99j.starbound.spell.Spell;
import com.daniel99j.starbound.spell.Spells;
import com.daniel99j.starbound.misc.ModEntityComponents;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.HotbarGui;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class SpellSelectorGui extends HotbarGui {
    boolean ready;
    public SpellSelectorGui(ServerPlayerEntity player) {
        super(player);
        build();
    }

    private void build() {
        for (int i = 0; i < Spells.getSpells().size(); i++) {
            Spell s = Spells.getSpells().get(i);
            this.setSlot(i, GuiElementBuilder.from(ItemUtils.getBasicModelItemStack()).setName(s.getName()).setLore(List.of(s.getDescription().copy().formatted(Formatting.GRAY))).setCallback((index, clickType, actionType, tes) -> {
                if(ready) cast(s);
                build();
            }).build());
        }

        this.setSlot(7, GuiElementBuilder.from(Items.KNOWLEDGE_BOOK.getDefaultStack()).setName(Text.of("Edit Spells")).setCallback((index, clickType, actionType) -> {
            if(ready) close();
        }).build());

        this.setSlot(7, GuiElementBuilder.from(Items.KNOWLEDGE_BOOK.getDefaultStack()).setName(Text.of("Spellbook")).setCallback((index, clickType, actionType) -> {
            if(ready) close();
        }).build());

        this.setSlot(8, GuiElementBuilder.from(Items.BARRIER.getDefaultStack()).setName(Text.of("Close")).setCallback((index, clickType, actionType) -> {
            if(ready) close();
        }).build());
    }

    @Override
    public void onTick() {
        super.onTick();
        this.ready = true;
    }

    private void cast(Spell spell) {
        ModEntityComponents.PLAYER_DATA.get(this.player).setLastCastSpell(spell);
        spell.baseCast(this.player);
        this.close();
    }

    protected void playSound(RegistryEntry<SoundEvent> sound, float volume, float pitch) {
        this.player.networkHandler.sendPacket(new PlaySoundS2CPacket(sound, SoundCategory.MASTER, this.player.getX(), this.player.getY(), this.player.getZ(), volume, pitch, 0L));
    }
}

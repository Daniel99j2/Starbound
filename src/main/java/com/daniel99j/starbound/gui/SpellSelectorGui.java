package com.daniel99j.starbound.gui;

import com.daniel99j.lib99j.api.ItemUtils;
import com.daniel99j.starbound.magic.spell.Spell;
import com.daniel99j.starbound.magic.spell.Spells;
import com.daniel99j.starbound.misc.ModEntityComponents;
import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.HotbarGui;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.component.type.UseCooldownComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

public class SpellSelectorGui extends HotbarGui {
    int ready = 3;
    int page = 0;

    public SpellSelectorGui(ServerPlayerEntity player) {
        super(player);
        build();
    }



    private void build() {
        for (int i = 0; i < 4; i++) {
            if(Spells.getSpells().size() > i+4*page) {
                Spell s = Spells.getSpells().get(i+4*page);

                this.setSlot(i, GuiElementBuilder.from(s.getIcon()).setCallback((index, clickType, actionType, slotInterface) -> {
                    if (ready == 0) {
                        if (clickType == ClickType.OFFHAND_SWAP) {
                            page++;
                            if (page > Spells.getSpells().size() / 4) {
                                page = 0;
                            }
                        } else {
                            ModEntityComponents.PLAYER_DATA.get(this.player).setLastCastSpell(s);
                            if (clickType.isRight) cast(s);
                            else this.close();
                        }
                    }
                    build();
                }).build());
            } else {
                ItemStack display = ItemUtils.setModelUnBridged(ItemUtils.getBasicModelItemStack(), Identifier.ofVanilla("air"));
                display.set(DataComponentTypes.ITEM_NAME, Text.of(""));
                display.set(DataComponentTypes.TOOLTIP_DISPLAY, new TooltipDisplayComponent(true, new LinkedHashSet<>(List.of())));
                this.setSlot(i,  GuiElementBuilder.from(display).setCallback((index, clickType, actionType, slotInterface) -> {
                    if (ready == 0) {
                        if (clickType == ClickType.OFFHAND_SWAP) {
                            page++;
                            if (page > Spells.getSpells().size() / 4) {
                                page = 0;
                            }
                        }
                    }
                    build();
                }));
            }
        }

        this.setSlot(6, GuiElementBuilder.from(Items.WRITABLE_BOOK.getDefaultStack()).setName(Text.of("Edit Order")).setCallback((index, clickType, actionType) -> {
            if(ready == 0) close();
        }).build());

        this.setSlot(7, GuiElementBuilder.from(Items.KNOWLEDGE_BOOK.getDefaultStack()).setName(Text.of("Spellbook")).setCallback((index, clickType, actionType) -> {
            if(ready == 0) close();
        }).build());

        this.setSlot(8, GuiElementBuilder.from(Items.BARRIER.getDefaultStack()).setName(Text.of("Close")).setCallback((index, clickType, actionType) -> {
            if(ready == 0) close();
        }).build());
    }

    @Override
    public boolean isIncludingPlayer() {
        return false;
    }

    @Override
    public void onTick() {
        super.onTick();
        if(this.ready > 0) this.ready--;
    }

    private boolean cast(Spell spell) {
        boolean b = spell.baseCast(this.player);
        if(b) this.close();
        return b;
    }

    protected void playSound(RegistryEntry<SoundEvent> sound, float volume, float pitch) {
        this.player.networkHandler.sendPacket(new PlaySoundS2CPacket(sound, SoundCategory.MASTER, this.player.getX(), this.player.getY(), this.player.getZ(), volume, pitch, 0L));
    }
}

package com.daniel99j.starbound.gui;

import com.daniel99j.lib99j.api.GuiUtils;
import com.daniel99j.lib99j.api.ItemUtils;
import com.daniel99j.starbound.Starbound;
import com.daniel99j.starbound.block.ModBlocks;
import com.daniel99j.starbound.block.pulsar.machines.TestPulsarMachineBlockEntity;
import com.daniel99j.starbound.magic.spell.Spell;
import com.daniel99j.starbound.magic.spell.Spells;
import com.daniel99j.starbound.misc.GuiTextures;
import com.daniel99j.starbound.misc.ModEntityComponents;
import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.ScreenProperty;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.elements.GuiElementInterface;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.BiConsumer;

public class SelectSpellGui extends SimpleGui {
    int page;
    int spellsPerPage;
    final boolean showAsLocked;
    final TriConsumer<SelectSpellGui, Spell, ClickType> callback;

    public SelectSpellGui(ServerPlayerEntity player, boolean showAsLocked, TriConsumer<SelectSpellGui, Spell, ClickType> callback) {
        super(ScreenHandlerType.GENERIC_9X6, player, false);
        this.showAsLocked = showAsLocked;
        this.callback = callback;
        build();
    }

    private void build() {
        this.setTitle(Text.literal("Select a spell").formatted(Formatting.WHITE));
        List<Integer> contentSlots = new ArrayList<>();

        for (int i = 0; i < 9*6; i++) {
            int row = i / 9;
            int col = i % 9;

            boolean isBorder = row == 0 || row == 5 || col == 0 || col == 8;
            boolean isBottomTwoRows = row >= 4;

            if (!isBorder && !isBottomTwoRows) {
                contentSlots.add(i);
            }
        }

        int itemsPerPage = contentSlots.size();
        int startIndex = page * itemsPerPage;
        ArrayList<Spell> spells = Spells.getSpells();

        spellsPerPage = Math.max(spellsPerPage, itemsPerPage);

        for (int i = 0; i < itemsPerPage; i++) {
            int itemIndex = startIndex + i;
            if (itemIndex >= spells.size()) break;

            int slot = contentSlots.get(i);
            this.setSlot(slot, GuiElementBuilder.from(spells.get(itemIndex).getIcon(player, true, false)).setCallback((index, clickType, actionType, slotInterface) -> {
                callback.accept(this, spells.get(itemIndex), clickType);
            }));
        }

        for (int i = spells.size() - startIndex; i < itemsPerPage; i++) {
            int slot = contentSlots.get(i);
            this.setSlot(slot, ItemStack.EMPTY);
        }

        this.setSlot(4*9+6, GuiUtils.GuiTextures.HEAD_PREVIOUS_PAGE.setName(Text.of("Previous Page")));
        this.setSlot(4*9+7, GuiUtils.GuiTextures.HEAD_NEXT_PAGE.setName(Text.of("Next Page")).setCallback((index, clickType, actionType, slotInterface) -> {

        }));
    }
}

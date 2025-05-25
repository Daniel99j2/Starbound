package com.daniel99j.starbound.gui;

import com.daniel99j.starbound.block.ModBlocks;
import com.daniel99j.starbound.block.pulsar.machines.TestPulsarMachineBlockEntity;
import eu.pb4.sgui.api.ScreenProperty;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class TestMachineGui extends SimpleGui {
    TestPulsarMachineBlockEntity blockEntity;

    public TestMachineGui(ServerPlayerEntity player, TestPulsarMachineBlockEntity blockEntity) {
        super(ScreenHandlerType.FURNACE, player, false);
        this.blockEntity = blockEntity;
        build();
    }

    private void build() {
        this.setTitle(Text.literal("Test machine").formatted(Formatting.WHITE));

        this.setSlotRedirect(0, new Slot(blockEntity, 0, 1, 1));
        this.setSlotRedirect(1, new Slot(blockEntity, 1, 1, 1));
    }

    @Override
    public void onTick() {
        super.onTick();
        if(blockEntity != null) {
            assert blockEntity.getWorld() != null;
            this.sendProperty(ScreenProperty.MAX_FUEL_BURN_TIME, blockEntity.getWorld().getBlockState(blockEntity.getPos()).get(ModBlocks.PULSAR_POWER) > 0 ? 1 : 0);
            this.sendProperty(ScreenProperty.FIRE_LEVEL, 1);

            this.sendProperty(ScreenProperty.CURRENT_PROGRESS, blockEntity.getCookTime());
            this.sendProperty(ScreenProperty.MAX_PROGRESS, 20);
        } else this.close();
    }
}

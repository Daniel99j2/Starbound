package com.daniel99j.starbound.block.pulsar.machines;

import com.daniel99j.starbound.block.ModBlockEntities;
import com.daniel99j.starbound.block.ModBlocks;
import com.daniel99j.starbound.block.pulsar.PulsarRedirectorBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class TestPulsarMachineBlockEntity extends PulsarRedirectorBlockEntity implements SidedInventory {
    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
    private int cookTime = 0;

    public TestPulsarMachineBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TEST_MACHINE, pos, state);
    }

    @Override
    public float getPowerMultiplier() {
        return 0.8f;
    }

    @Override
    protected void customTick(ServerWorld world, BlockPos pos, BlockState state) {
        if(!inventory.getFirst().isEmpty() && state.get(ModBlocks.PULSAR_POWER) > 0) {
            if(this.cookTime > 20 && (inventory.get(2).isEmpty() || (inventory.get(2).getCount()+1 < inventory.get(2).getMaxCount()))) {
                cookTime = 0;
                if (inventory.get(2).isEmpty()) inventory.set(1, Items.END_CRYSTAL.getDefaultStack());
                else inventory.get(2).setCount(inventory.get(2).getCount() + 1);
                inventory.get(0).split(1);
            } else {
                this.cookTime++;
            }
        }
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        if (side == Direction.DOWN) {
            return new int[]{2, 1};
        } else {
            return side == Direction.UP ? new int[]{0} : new int[]{1};
        }
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return this.isValid(slot, stack);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return dir == Direction.DOWN && slot == 1 ? stack.isOf(Items.WATER_BUCKET) || stack.isOf(Items.BUCKET) : true;
    }

    protected DefaultedList<ItemStack> getHeldStacks() {
        return this.inventory;
    }

    protected void setHeldStacks(DefaultedList<ItemStack> inventory) {
        this.inventory = inventory;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemStack : this.getHeldStacks()) {
            if (!itemStack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.getHeldStacks().get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack itemStack = Inventories.splitStack(this.getHeldStacks(), slot, amount);
        if (!itemStack.isEmpty()) {
            this.markDirty();
        }

        return itemStack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.getHeldStacks(), slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.getHeldStacks().set(slot, stack);
        stack.capCount(this.getMaxCount(stack));
        this.markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return Inventory.canPlayerUse(this, player);
    }

    @Override
    public void clear() {
        this.getHeldStacks().clear();
    }

    @Override
    public int size() {
        return this.inventory.size();
    }

    public int getCookTime() {
        return cookTime;
    }
}
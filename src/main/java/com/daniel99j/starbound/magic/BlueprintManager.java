package com.daniel99j.starbound.magic;

import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.elements.BlockDisplayElement;
import net.minecraft.block.*;
import net.minecraft.entity.decoration.Brightness;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class BlueprintManager {
    private ElementHolder holder;
    private ArrayList<BlockDisplayElement> displays = new ArrayList<>();
    private boolean spawned;

    public BlueprintManager() {

    }

    public void tick() {
        if(!spawned && shouldShow()) {
            spawned = true;
            displays.add(createElement(new BlockPos(0, 0, 1), Blocks.OAK_PLANKS.getDefaultState()));
            displays.add(createElement(new BlockPos(0, 0, -1), Blocks.OAK_PLANKS.getDefaultState()));
            displays.add(createElement(new BlockPos(0, 1, 0), Blocks.OAK_LOG.getDefaultState()));
            displays.add(createElement(new BlockPos(0, 2, 0), Blocks.OAK_LOG.getDefaultState()));
            displays.add(createElement(new BlockPos(0, 3, 0), Blocks.OAK_LOG.getDefaultState()));
            displays.add(createElement(new BlockPos(0, 4, 0), Blocks.END_ROD.getDefaultState()));
            for (int i = 1; i > -1; i--) {
                for (int i2 = 1; i2 > -1; i2--) {
                    displays.add(createElement(new BlockPos(i, 5, i2), Blocks.WHITE_STAINED_GLASS.getDefaultState()));
                }
            }
            for(BlockDisplayElement element : displays) {
                holder.addElement(element);
            }
        } else if(spawned && !shouldShow()) {
            spawned = false;
            for(BlockDisplayElement element : displays) {
                holder.removeElement(element);
            }
            displays.clear();
        }
    }

    private BlockDisplayElement createElement(BlockPos pos, BlockState state) {
        BlockDisplayElement element = new BlockDisplayElement(toHologram(state)) {
            @Override
            public Vec3d getCurrentPos() {
                return Vec3d.of(BlockPos.ofFloored(getPos().add(this.getOffset())));
            }
        };
        element.setOffset(Vec3d.of(pos));
        element.setGlowing(true);
        element.setGlowColorOverride(0x42daf5);
        element.setInvisible(true);
        element.setBrightness(Brightness.FULL);
        return element;
    };

    public static BlockState toHologram(BlockState state) {
        BlockState out = state;
        if(state.getBlock() instanceof StairsBlock) {
            out = Blocks.WARPED_STAIRS.getDefaultState();
            out.with(StairsBlock.FACING, state.get(StairsBlock.FACING));
            out.with(StairsBlock.SHAPE, state.get(StairsBlock.SHAPE));
        } else if(state.getBlock() instanceof StainedGlassBlock || state.getBlock() == Blocks.GLASS) {
            out = Blocks.LIGHT_BLUE_STAINED_GLASS.getDefaultState();
        } else if(state.getBlock() instanceof StainedGlassPaneBlock || state.getBlock() == Blocks.GLASS_PANE) {
            out = Blocks.LIGHT_BLUE_STAINED_GLASS_PANE.getDefaultState();
            out.with(PaneBlock.NORTH, state.get(PaneBlock.NORTH));
            out.with(PaneBlock.SOUTH, state.get(PaneBlock.SOUTH));
            out.with(PaneBlock.EAST, state.get(PaneBlock.EAST));
            out.with(PaneBlock.WEST, state.get(PaneBlock.WEST));
        } else if(state.getBlock() instanceof SlabBlock) {
            out = Blocks.WARPED_SLAB.getDefaultState();
            out.with(SlabBlock.TYPE, state.get(SlabBlock.TYPE));
        } else if(state.getBlock() instanceof ButtonBlock) {
            out = Blocks.WARPED_SLAB.getDefaultState();
            out.with(ButtonBlock.FACING, state.get(ButtonBlock.FACING));
            out.with(ButtonBlock.POWERED, state.get(ButtonBlock.POWERED));
        } else if(state.getBlock() instanceof CarpetBlock) {
            out = Blocks.LIGHT_BLUE_CARPET.getDefaultState();
        } else if(state.getBlock() instanceof PillarBlock) {
            out = Blocks.WARPED_STEM.getDefaultState();
            out.with(PillarBlock.AXIS, state.get(PillarBlock.AXIS));
        } else if(state.getBlock() instanceof FenceBlock) {
            out = Blocks.WARPED_FENCE.getDefaultState();
            out.with(FenceBlock.NORTH, state.get(FenceBlock.NORTH));
            out.with(FenceBlock.SOUTH, state.get(FenceBlock.SOUTH));
            out.with(FenceBlock.EAST, state.get(FenceBlock.EAST));
            out.with(FenceBlock.WEST, state.get(FenceBlock.WEST));
        } else if(state.getBlock() instanceof FenceGateBlock) {
            out = Blocks.WARPED_FENCE_GATE.getDefaultState();
            out.with(FenceGateBlock.FACING, state.get(FenceGateBlock.FACING));
            out.with(FenceGateBlock.OPEN, state.get(FenceGateBlock.OPEN));
            out.with(FenceGateBlock.POWERED, state.get(FenceGateBlock.POWERED));
        } else if(state.getBlock() instanceof DoorBlock) {
            out = Blocks.WARPED_DOOR.getDefaultState();
            out.with(DoorBlock.FACING, state.get(DoorBlock.FACING));
            out.with(DoorBlock.OPEN, state.get(DoorBlock.OPEN));
            out.with(DoorBlock.POWERED, state.get(DoorBlock.POWERED));
            out.with(DoorBlock.HALF, state.get(DoorBlock.HALF));
        } else if(state.getBlock() instanceof TrapdoorBlock) {
            out = Blocks.WARPED_TRAPDOOR.getDefaultState();
            out.with(TrapdoorBlock.FACING, state.get(TrapdoorBlock.FACING));
            out.with(TrapdoorBlock.OPEN, state.get(TrapdoorBlock.OPEN));
            out.with(TrapdoorBlock.POWERED, state.get(TrapdoorBlock.POWERED));
            out.with(TrapdoorBlock.HALF, state.get(TrapdoorBlock.HALF));
        } else if(state.getBlock() instanceof PressurePlateBlock) {
            out = Blocks.WARPED_PRESSURE_PLATE.getDefaultState();
            out.with(PressurePlateBlock.POWERED, state.get(PressurePlateBlock.POWERED));
        } else if(state.isIn(BlockTags.PLANKS)) {
            out = Blocks.WARPED_PLANKS.getDefaultState();
        }
        return out;
    }

    public void setHolder(ElementHolder holder) {
        this.holder = holder;
    }

    public boolean shouldShow() {
        return true;
    }

    public Vec3d getPos() {
        return Vec3d.ZERO;
    }
}

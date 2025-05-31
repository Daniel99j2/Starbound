package com.daniel99j.starbound.block;

import com.daniel99j.lib99j.api.ItemUtils;
import com.daniel99j.starbound.Starbound;
import com.mojang.serialization.MapCodec;
import eu.pb4.factorytools.api.virtualentity.BlockModel;
import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.virtualentity.api.BlockWithElementHolder;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.entity.decoration.Brightness;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.joml.Matrix4x3f;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.Objects;

public class MysteriousCoreBlock extends Block implements PolymerBlock, BlockWithElementHolder {
    public MysteriousCoreBlock(Settings settings) {
        super(settings);
    }

    @Override
    public MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Override
    public ElementHolder createElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
        return new Model(world, initialBlockState, pos);
    }

    @Override
    public boolean tickElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
        return true;
    }

    @Override
    public BlockState getPolymerBlockState(BlockState blockState, PacketContext packetContext) {
        return Blocks.CONDUIT.getDefaultState().with(Properties.WATERLOGGED, false);
    }

    public static final class Model extends BlockModel {
        private final ItemDisplayElement mainElement;
        private final ItemDisplayElement backgroundElement;

        private Model(ServerWorld world, BlockState state, BlockPos pos) {
            this.mainElement = new ItemDisplayElement(ItemUtils.getBasicModelItemStack());
            this.backgroundElement = new ItemDisplayElement(ItemUtils.getBasicModelItemStack());
            this.updateAnimation(world);
            this.addElement(this.mainElement);
            this.addElement(this.backgroundElement);
        }

        private int getUpdateRate() {
            return 1;
        }

        private void updateAnimation(World world) {
            if (this.blockAware() != null) {
                this.mainElement.setItem(ItemDisplayElementUtil.getModel(Identifier.of(Starbound.MOD_ID, "block/mysterious_core")));
                this.mainElement.setInvisible(true);
                Matrix4x3f matrix = new Matrix4x3f();
                matrix.rotateXYZ((float) Math.toRadians((world.getTime()+this.blockAware().getBlockPos().asLong()) % 360), (float) Math.toRadians((world.getTime()+this.blockAware().getBlockPos().asLong()) % 360), 0f);
                this.mainElement.setTransformation(matrix);
                this.mainElement.setTeleportDuration(5);
                this.mainElement.setInterpolationDuration(5);
                this.mainElement.setBrightness(Brightness.FULL);
                this.mainElement.startInterpolation();

                this.backgroundElement.setInvisible(true);
                this.backgroundElement.setBrightness(new Brightness(0, 0));
                this.backgroundElement.setItem(ItemDisplayElementUtil.getModel(Identifier.of(Starbound.MOD_ID, "block/mysterious_core_outer")));
            }
        }

        @Override
        protected void onTick() {
            if (!Objects.requireNonNull(this.blockAware()).isPartOfTheWorld()) {
                return;
            }
            var tick = Objects.requireNonNull(this.blockAware()).getWorld().getTime();

            if (tick % this.getUpdateRate() == 0) {
                this.updateAnimation(Objects.requireNonNull(Objects.requireNonNull(this.blockAware()).getWorld()));
                this.mainElement.startInterpolationIfDirty();
            }
        }
    }
}
package com.daniel99j.starbound.block.pulsar;

import com.daniel99j.lib99j.api.EntityUtils;
import com.daniel99j.lib99j.api.ItemUtils;
import com.daniel99j.starbound.Starbound;
import com.daniel99j.starbound.block.ModBlockEntities;
import com.daniel99j.starbound.block.ModBlocks;
import com.daniel99j.starbound.block.pulsar.machines.TurretBlockEntity;
import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.attachment.BlockAwareAttachment;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.decoration.Brightness;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4x3f;
import org.joml.Vector3f;

import java.util.Objects;

public class PulsarTransmitterBlock extends PulsarRedirectorBlock {
    public PulsarTransmitterBlock(Settings settings) {
        super(settings);
    }

    @Override
    @Nullable
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PulsarTransmitterBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : PulsarTransmitterBlock.validateTicker(type, ModBlockEntities.PULSAR_TRANSMITTER, PulsarTransmitterBlockEntity::tick);
    }

    @Override
    public ElementHolder createElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
        return new PulsarPoweredModel(world, initialBlockState, pos) {
            private ItemDisplayElement extraBeamElement;
            private ItemDisplayElement extraFaceElement;

            @Override
            protected void updateAnimation(Direction facing, World world) {
                super.updateAnimation(facing, world);
                if(this.extraBeamElement == null) {
                    this.extraBeamElement = new ItemDisplayElement(ItemUtils.getBasicModelItemStack());
                    this.addElement(extraBeamElement);
                    this.extraBeamElement.setTeleportDuration(2);
                    this.extraBeamElement.setInterpolationDuration(2);
                    this.extraFaceElement = new ItemDisplayElement(ItemUtils.getBasicModelItemStack());
                    this.addElement(extraFaceElement);
                };

                this.extraBeamElement.setItem(ItemDisplayElementUtil.getModel(
                        Identifier.of(Starbound.MOD_ID, "block/pulsar_beam")
                ));
                this.extraBeamElement.setInvisible(true);
                this.extraBeamElement.setBrightness(Brightness.FULL);

                this.extraFaceElement.setItem(ItemDisplayElementUtil.getModel(
                        Identifier.of(Starbound.MOD_ID, "block/pulsar_transmitter")
                ));
                this.extraFaceElement.setInvisible(true);

                float distance = world.isSkyVisible(pos.add(0, 1, 0)) ? 500 : 0;

                if (distance <= 0) {
                    this.extraBeamElement.setTransformation(new Matrix4x3f().scale(0));
                } else {
                    Matrix4x3f matrix = new Matrix4x3f();

                    matrix.translate(new Vector3f(0, 0, distance / 2f));

                    matrix.scale(new Vector3f(1, 1, distance));

                    this.extraBeamElement.setTransformation(matrix);
                    EntityUtils.setRotationFromDirection(Direction.UP, this.extraBeamElement);
                }
            }
        };
    }
}
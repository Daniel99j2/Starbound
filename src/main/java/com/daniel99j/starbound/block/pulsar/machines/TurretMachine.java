package com.daniel99j.starbound.block.pulsar.machines;

import com.daniel99j.lib99j.api.EntityUtils;
import com.daniel99j.lib99j.api.ItemUtils;
import com.daniel99j.starbound.Starbound;
import com.daniel99j.starbound.block.ModBlockEntities;
import com.daniel99j.starbound.block.ModBlocks;
import com.daniel99j.starbound.block.pulsar.PulsarRedirectorBlock;
import com.daniel99j.starbound.block.pulsar.PulsarRedirectorBlockEntity;
import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.attachment.BlockAwareAttachment;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.Brightness;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4x3f;
import org.joml.Vector3f;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.Objects;

public class TurretMachine extends PulsarRedirectorBlock {
    public TurretMachine(Settings settings)  {
        super(settings);
    }

    @Override
    @Nullable
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TurretBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : TurretMachine.validateTicker(type, ModBlockEntities.TURRET, TurretBlockEntity::tick);
    }

    @Override
    public ElementHolder createElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
        return new PulsarPoweredModel(world, initialBlockState, pos) {
            private ItemDisplayElement extraBeamElement;
            private ItemDisplayElement modelElement;

            @Override
            protected void updateAnimation(Direction facing, World world) {
                super.updateAnimation(facing, world);
                if(this.extraBeamElement == null) {
                    this.extraBeamElement = new ItemDisplayElement(ItemUtils.getBasicModelItemStack());
                    this.extraBeamElement.setOffset(Vec3d.ZERO.add(0, 1, 0));
                    this.addElement(extraBeamElement);
                    this.extraBeamElement.setTeleportDuration(2);
                    this.extraBeamElement.setInterpolationDuration(2);
                    this.modelElement = new ItemDisplayElement(ItemUtils.getBasicModelItemStack());
                    this.addElement(modelElement);
                };

                this.extraBeamElement.setItem(ItemDisplayElementUtil.getModel(
                        Identifier.of(Starbound.MOD_ID, "block/pulsar_beam")
                ));
                this.extraBeamElement.setInvisible(true);
                this.extraBeamElement.setBrightness(Brightness.FULL);

                this.modelElement.setItem(ItemDisplayElementUtil.getModel(
                        Identifier.of(Starbound.MOD_ID, "block/turret")
                ));
                this.modelElement.setInvisible(true);

                if (this.blockAware() != null && Objects.requireNonNull(this.blockAware()).isPartOfTheWorld()) {
                    int power = this.blockState().get(ModBlocks.PULSAR_POWER);
                    float distance = ((TurretBlockEntity) Objects.requireNonNull(world.getBlockEntity(Objects.requireNonNull(this.blockAware()).getBlockPos()))).getDistanceToTarget();

                    if (power == 0 || distance <= 0 || ((TurretBlockEntity) Objects.requireNonNull(world.getBlockEntity(Objects.requireNonNull(this.blockAware()).getBlockPos()))).getCurrentTarget() == null) {
                        this.extraBeamElement.setTransformation(new Matrix4x3f().scale(0));
                    } else {
                        Matrix4x3f matrix = new Matrix4x3f();

                        matrix.translate(new Vector3f(0, 0, distance/2f));

                        matrix.scale(new Vector3f(1, 1, distance));

                        this.extraBeamElement.setTransformation(matrix);

                        TurretBlockEntity turret = ((TurretBlockEntity) Objects.requireNonNull(world.getBlockEntity(Objects.requireNonNull(this.blockAware()).getBlockPos())));

                        double d = turret.getCurrentTarget().getX() - this.blockPos().toCenterPos().getX();
                        double e = ((turret.getCurrentTarget().getEyeY()+turret.getCurrentTarget().getY())/2f) - this.blockPos().toCenterPos().getY()+1;
                        double f = turret.getCurrentTarget().getZ() - this.blockPos().toCenterPos().getZ();
                        double g = Math.sqrt(d * d + f * f);
                        this.extraBeamElement.setPitch(MathHelper.wrapDegrees((float)(-(MathHelper.atan2(e, g) * 180.0F / (float)Math.PI))));
                        this.extraBeamElement.setYaw(MathHelper.wrapDegrees((float)(MathHelper.atan2(f, d) * 180.0F / (float)Math.PI) - 90.0F));
                    }
                }
            }
        };
    }
}
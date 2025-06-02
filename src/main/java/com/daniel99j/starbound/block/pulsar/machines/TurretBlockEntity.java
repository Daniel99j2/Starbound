package com.daniel99j.starbound.block.pulsar.machines;

import com.daniel99j.lib99j.api.EntityUtils;
import com.daniel99j.lib99j.api.NumberUtils;
import com.daniel99j.lib99j.api.ParticleHelper;
import com.daniel99j.starbound.block.ModBlockEntities;
import com.daniel99j.starbound.block.ModBlocks;
import com.daniel99j.starbound.block.pulsar.PulsarRedirectorBlockEntity;
import com.daniel99j.starbound.misc.ModDamageTypes;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.component.ComponentMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Nameable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.*;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TurretBlockEntity extends PulsarRedirectorBlockEntity {
    private TargetMode targetMode = TargetMode.HOSTILE;
    private Entity currentTarget;

    public TurretBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TURRET, pos, state);
    }

    @Override
    public int getPowerUsage() {
        return 20;
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.targetMode = Enum.valueOf(TargetMode.class, nbt.getString("target_mode", "off").toUpperCase());
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putString("target_mode", this.targetMode.toString().toLowerCase());
    }

    public float getDistanceToTarget() {
        return currentTarget == null ? 0 : (float) Math.sqrt(currentTarget.squaredDistanceTo(Vec3d.of(this.getPos())));
    }

    public Entity getCurrentTarget() {
        return currentTarget;
    }

    public @Nullable Entity getTarget() {
        if(targetMode == TargetMode.OFF) return null;
        assert this.getWorld() != null;
        ArrayList<LivingEntity> entities = new ArrayList<>(this.getWorld().getEntitiesByClass(LivingEntity.class, new Box(Vec3d.of(this.getPos()).add(-32, -32, -32), Vec3d.of(this.getPos()).add(32, 32, 32)), (e) -> true));
        LivingEntity testEntity = new PigEntity(EntityType.PIG, this.getWorld());
        entities.removeIf((e) -> {
            if(e.isSpectator() || e.hasCustomName() || e.isDead()) return true;
            boolean b1 = Math.sqrt(e.squaredDistanceTo(Vec3d.of(this.getPos()))) <= 32;
            boolean b2 = false;
            if(targetMode == TargetMode.LIGHT) b2 = e.isGlowing();
            else if(targetMode == TargetMode.HOSTILE) b2 = e instanceof HostileEntity;
            else if(targetMode == TargetMode.LIGHT_HOSTILE) b2 = e.isGlowing() || e instanceof HostileEntity;
            testEntity.setPos(this.getPos().getX(), this.getPos().getY()+1, this.getPos().getZ());
            testEntity.setVelocity(Vec3d.ZERO);
            EntityUtils.accelerateTowards(testEntity, e.getX(), e.getY(), e.getZ(), 1);
            testEntity.setPos(this.getPos().getX()+testEntity.getVelocity().getX(), this.getPos().getY()+1+testEntity.getVelocity().getY(), this.getPos().getZ()+testEntity.getVelocity().getZ());
            return !(b1 && b2 && e.canSee(testEntity, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, ((e.getEyeY()+e.getY())/2f)));
        });
        entities.sort(Comparator.comparingDouble((e) -> Math.sqrt(e.squaredDistanceTo(Vec3d.of(this.getPos())))*((targetMode == TargetMode.LIGHT) || (targetMode == TargetMode.LIGHT_HOSTILE) && e.isGlowing() ? 1 : 50)));
        return entities.isEmpty() ? null : entities.getFirst();
    }

    public enum TargetMode {
        LIGHT,
        HOSTILE,
        LIGHT_HOSTILE,
        OFF
    }

    @Override
    protected void customTick(ServerWorld world, BlockPos pos, BlockState state, int power, boolean shouldRun) {
        if(shouldRun) {
            if(world.getTime() % 10 == 0) {
                currentTarget = getTarget();
                if(currentTarget != null) currentTarget.damage((ServerWorld) currentTarget.getWorld(), new DamageSource(ModDamageTypes.of(currentTarget.getWorld(), ModDamageTypes.PULSAR_BEAM)), Math.max(0, (10f/((float) ModBlocks.MAX_PULSAR_POWER/power))));
            };
        } else {
            currentTarget = null;
        }
    }
}
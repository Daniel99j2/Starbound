package com.daniel99j.starbound.entity;

import com.daniel99j.lib99j.api.ParticleHelper;
import com.daniel99j.lib99j.api.SoundUtils;
import com.daniel99j.starbound.misc.ModSounds;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import eu.pb4.polymer.core.api.entity.PolymerEntity;
import eu.pb4.polymer.core.api.entity.PolymerEntityUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRemoveS2CPacket;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Consumer;

public class DummyEntity extends LivingEntity implements PolymerEntity {
    private ServerPlayerEntity player;

    public DummyEntity(EntityType<DummyEntity> entityEntityType, World world) {
        super(entityEntityType, world);
    }

    public DummyEntity(ServerPlayerEntity player) {
        super(ModEntities.DUMMY, player.getWorld());
        this.player = player;
        this.setPos(player.getX(), player.getY(), player.getZ());
    }

    public static DefaultAttributeContainer.Builder createTestDummyAttributes() {
        return PlayerEntity.createPlayerAttributes().add(EntityAttributes.MAX_HEALTH, 1).add(EntityAttributes.FLYING_SPEED, 0.1f).add(EntityAttributes.MOVEMENT_SPEED, 0.1f).add(EntityAttributes.ATTACK_DAMAGE, 2.0).add(EntityAttributes.FOLLOW_RANGE, 48.0);
    }

    @Override
    public EntityType<?> getPolymerEntityType(PacketContext context) {
        return EntityType.PLAYER;
    }

    @Override
    public float getEffectiveExplosionResistance(Explosion explosion, BlockView world, BlockPos pos, BlockState blockState, FluidState fluidState, float max) {
        return blockState.isAir() || blockState.getBlock() instanceof FluidBlock ? 0 : blockState.getBlock().getBlastResistance();
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        SoundUtils.playSoundAtPosition(world, this.getPos(), ModSounds.FAKE_PLAYER_DISAPPEAR, SoundCategory.PLAYERS, 1, 1);
        ParticleHelper.spawnParticlesAtPosition(world, this.getEyePos(), new BlockStateParticleEffect(ParticleTypes.BLOCK_CRUMBLE, Blocks.OAK_PLANKS.getDefaultState()), 10, 0.2, 0.5, 0.2, 0.1);
        this.discard();
        return true;
    }

    @Override
    public Arm getMainArm() {
        return Arm.RIGHT;
    }

    @Override
    public List<Pair<EquipmentSlot, ItemStack>> getPolymerVisibleEquipment(List<Pair<EquipmentSlot, ItemStack>> items, ServerPlayerEntity player) {
        List<Pair<EquipmentSlot, ItemStack>> out = new ArrayList<>();
        for(EquipmentSlot slot : EquipmentSlot.values()) {
            out.add(Pair.of(slot, this.player.getEquippedStack(slot)));
        }
        return out;
    }

    @Override
    public void onBeforeSpawnPacket(ServerPlayerEntity player, Consumer<Packet<?>> packetConsumer) {
        //TODO: OUTER LAYER
        var packet = PolymerEntityUtils.createMutablePlayerListPacket(EnumSet.of(PlayerListS2CPacket.Action.ADD_PLAYER, PlayerListS2CPacket.Action.UPDATE_LISTED, PlayerListS2CPacket.Action.INITIALIZE_CHAT, PlayerListS2CPacket.Action.UPDATE_GAME_MODE, PlayerListS2CPacket.Action.UPDATE_LATENCY, PlayerListS2CPacket.Action.UPDATE_DISPLAY_NAME, PlayerListS2CPacket.Action.UPDATE_HAT, PlayerListS2CPacket.Action.UPDATE_LIST_ORDER));
        packet.getEntries().add(new PlayerListS2CPacket.Entry(this.getUuid(), this.player.getGameProfile(), false, 0, GameMode.ADVENTURE, this.player.getDisplayName(), player.isPartVisible(PlayerModelPart.HAT), 0, null));
        packetConsumer.accept(packet);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        player.networkHandler.sendPacket(new PlayerRemoveS2CPacket(List.of(this.getUuid())));
        super.onStartedTrackingBy(player);
    }
}
package com.daniel99j.starbound.mixin;

import com.daniel99j.lib99j.Lib99j;
import com.daniel99j.lib99j.api.EntityUtils;
import com.daniel99j.starbound.gui.SpellSelectorGui;
import com.daniel99j.starbound.item.ModItems;
import com.daniel99j.starbound.magic.BlueprintManager;
import com.daniel99j.starbound.misc.ModEntityComponents;
import com.daniel99j.starbound.misc.PossessionAccessor;
import com.mojang.authlib.GameProfile;
import eu.pb4.polymer.core.api.entity.PolymerEntity;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.VirtualEntityUtils;
import eu.pb4.polymer.virtualentity.api.attachment.EntityAttachment;
import eu.pb4.polymer.virtualentity.api.elements.GenericEntityElement;
import eu.pb4.polymer.virtualentity.api.elements.InteractionElement;
import eu.pb4.polymer.virtualentity.api.elements.MobAnchorElement;
import eu.pb4.polymer.virtualentity.api.elements.VirtualElement;
import eu.pb4.sgui.virtual.hotbar.HotbarScreenHandler;
import it.unimi.dsi.fastutil.ints.IntList;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityAttributesS2CPacket;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.server.command.GameModeCommand;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.Set;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin
        extends PlayerEntity implements PolymerEntity, PossessionAccessor {
    @Shadow public ServerPlayNetworkHandler networkHandler;

    @Shadow public abstract PlayerInput getPlayerInput();

    @Unique
    private ElementHolder starbound$holder;
    @Unique
    private InteractionElement starbound$wandInteraction;
    @Unique
    private GenericEntityElement starbound$airWalkShulker;
    @Unique
    private MobAnchorElement starbound$airWalkShulkerSeat;
    @Unique
    private double starbound$airWalkShulkerYPos;
    @Unique
    private final BlueprintManager starbound$blueprintManager = new BlueprintManager() {
        @Override
        public boolean shouldShow() {
            return starbound$shouldBlueprintHologram();
        }

        @Override
        public Vec3d getPos() {
            double yawRad = Math.toRadians(getYaw());
            double pitchRad = Math.toRadians(getPitch());

            double x = -Math.sin(yawRad) * Math.cos(pitchRad);
            double y = -Math.sin(pitchRad);
            double z = Math.cos(yawRad) * Math.cos(pitchRad);

            Vec3d accelerationVector = new Vec3d(x, y, z).normalize().multiply(3);
            return getPlayer().getPos().add(new Vec3d(accelerationVector.x, accelerationVector.y, accelerationVector.z));
        }
    };
    @Unique
    private LivingEntity starbound$possessedEntity;

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Unique
    private ServerPlayerEntity getPlayer() {
        PlayerEntity player = this;
        return (ServerPlayerEntity) player;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void starbound$customTick(CallbackInfo ci) {
        if (this.starbound$holder == null) {
            this.starbound$holder = new ElementHolder();
            this.starbound$holder.setAttachment(new EntityAttachment(this.starbound$holder, getPlayer(), true));
            this.starbound$holder.startWatching(getPlayer());
        }
        if (this.starbound$wandInteraction == null && starbound$shouldHaveWandInteraction()) {
            this.starbound$wandInteraction = new InteractionElement() {
                @Override
                public InteractionHandler getInteractionHandler(ServerPlayerEntity player) {
                    return getWandInteractionHandler();
                }
            };
            this.starbound$wandInteraction.setOffset(new Vec3d(0, -5, 0));
            this.starbound$wandInteraction.setInvisible(!FabricLoader.getInstance().isDevelopmentEnvironment());
            this.starbound$wandInteraction.setWidth(10);
            this.starbound$wandInteraction.setHeight(10);
            this.starbound$wandInteraction.setInteractionHandler(getWandInteractionHandler());
            this.starbound$holder.addElement(this.starbound$wandInteraction);
        }

        if (this.starbound$wandInteraction != null && !starbound$shouldHaveWandInteraction()) {
            this.starbound$holder.removeElement(this.starbound$wandInteraction);
            this.starbound$wandInteraction = null;
        }

        if (this.starbound$airWalkShulker == null && starbound$shouldHaveAirWalkShulker()) {
            this.starbound$airWalkShulkerYPos = this.getPos().getY();
            this.starbound$airWalkShulkerSeat = new MobAnchorElement() {
                @Override
                public Vec3d getCurrentPos() {
                    return new Vec3d(getPlayer().getPos().x, (getPlayer().getPos().getY() < starbound$airWalkShulkerYPos ? getPlayer().getPos().getY()-3 : starbound$airWalkShulkerYPos-3), getPlayer().getPos().z);
                }
            };
            this.starbound$airWalkShulker = new GenericEntityElement() {
                @Override
                protected EntityType<? extends Entity> getEntityType() {
                    return EntityType.SHULKER;
                }

                @Override
                public Vec3d getCurrentPos() {
                    return new Vec3d(getPlayer().getPos().x, (getPlayer().getPos().getY() < starbound$airWalkShulkerYPos ? getPlayer().getPos().getY()-3 : starbound$airWalkShulkerYPos-3), getPlayer().getPos().z);
                }
            };
            this.starbound$airWalkShulker.setInvisible(true);
            this.starbound$airWalkShulkerSeat.setInvisible(true);
            this.starbound$holder.addElement(this.starbound$airWalkShulkerSeat);
            this.starbound$holder.addElement(this.starbound$airWalkShulker);
            this.starbound$holder.sendPacket(VirtualEntityUtils.createRidePacket(this.starbound$airWalkShulkerSeat.getEntityId(), new int[]{this.starbound$airWalkShulker.getEntityId()}));
            var instance = new EntityAttributeInstance(EntityAttributes.SCALE, (i) -> {});
            instance.setBaseValue(4);
            this.starbound$holder.sendPacket(new EntityAttributesS2CPacket(
                    this.starbound$airWalkShulker.getEntityId(),
                    Set.of(instance)
            ));
        }

        if (this.starbound$airWalkShulker != null && !starbound$shouldHaveAirWalkShulker()) {
            this.starbound$airWalkShulkerYPos = 0;
            this.starbound$holder.removeElement(this.starbound$airWalkShulker);
            this.starbound$airWalkShulker = null;
            this.starbound$holder.removeElement(this.starbound$airWalkShulkerSeat);
            this.starbound$airWalkShulkerSeat = null;
        }



        if(this.starbound$airWalkShulker != null) {
            ((FloatingTicksAccessor) this.getPlayer().networkHandler).setFloatingTicks(0);
            if(this.getPlayer().getPos().getY() < this.starbound$airWalkShulkerYPos) {
                this.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 10, 5, true, false));
            }
        }

        this.starbound$blueprintManager.setHolder(this.starbound$holder);
        this.starbound$blueprintManager.tick();

        if(this.starbound$possessedEntity != null) {
            this.starbound$possessedEntity.setJumping(this.getPlayerInput().jump());
            this.starbound$possessedEntity.setSneaking(this.getPlayerInput().sneak());
            this.starbound$possessedEntity.setSprinting(this.getPlayerInput().sprint());
            if(this.getPlayerInput().forward()) EntityUtils.accelerateEntityPitchYaw(this.starbound$possessedEntity, this.starbound$possessedEntity.getMovementSpeed(), 0, this.starbound$possessedEntity.getYaw());
            if(this.getPlayerInput().backward()) EntityUtils.accelerateEntityPitchYaw(this.starbound$possessedEntity, -this.starbound$possessedEntity.forwardSpeed, 0, this.starbound$possessedEntity.getYaw());
            if(this.getPlayerInput().left()) EntityUtils.accelerateEntityPitchYaw(this.starbound$possessedEntity, this.starbound$possessedEntity.sidewaysSpeed, 0, this.starbound$possessedEntity.getYaw()-90);
            if(this.getPlayerInput().right()) EntityUtils.accelerateEntityPitchYaw(this.starbound$possessedEntity, this.starbound$possessedEntity.sidewaysSpeed, 0, this.starbound$possessedEntity.getYaw()+90);
        }
    }

    @Unique
    private VirtualElement.InteractionHandler getWandInteractionHandler() {
        return new VirtualElement.InteractionHandler() {
            @Override
            public void attack(ServerPlayerEntity player) {
                if (starbound$shouldHaveWandInteraction()) {
                    new SpellSelectorGui(player).open();
                }
            }

            @Override
            public void interact(ServerPlayerEntity player, Hand hand) {
                if (starbound$shouldHaveWandInteraction() && hand == Hand.MAIN_HAND) {
                    if (ModEntityComponents.PLAYER_DATA.get(player).getLastCastSpell() != null) {
                        Objects.requireNonNull(ModEntityComponents.PLAYER_DATA.get(player).getLastCastSpell()).baseCast(player);
                    }
                }
            }
        };
    }

    @Unique
    private boolean starbound$shouldHaveWandInteraction() {
        return this.getMainHandStack().getItem() == ModItems.WAND_ITEM && !(getPlayer().currentScreenHandler instanceof HotbarScreenHandler) && !getPlayer().isSpectator();
    }

    @Unique
    private boolean starbound$shouldHaveAirWalkShulker() {
        return this.getInventory().contains((stack) -> stack.getItem() == ModItems.GRAVITY_ANCHOR_ITEM) && !getPlayer().isSpectator();
    }

    @Unique
    private boolean starbound$shouldBlueprintHologram() {
        return this.getMainHandStack().getItem() == ModItems.ASTRAL_FABRICATOR_ITEM && !getPlayer().isSpectator();
    }


    @Override
    protected void applyGravity() {
        if(this.starbound$airWalkShulker == null) super.applyGravity();
    }

    @Override
    public boolean isOnGround() {
        return super.isOnGround() || this.starbound$airWalkShulker != null;
    }

    @Override
    public void starbound$setPossessedBy(ServerPlayerEntity possessed) {
        throw new IllegalStateException("Players cannot be possessed");
    }

    @Override
    public void starbound$setPossessedEntity(LivingEntity possessed) {
        if(possessed == this.getPlayer()) throw new IllegalStateException("Cannot possess self");
        if(possessed == null && this.starbound$possessedEntity != null) {
            ((PossessionAccessor) this.starbound$possessedEntity).starbound$setPossessedBy(null);
        }
        this.starbound$possessedEntity = possessed;

        if(possessed == null) {

        }

        if(possessed != null) {
            ((PossessionAccessor) possessed).starbound$setPossessedBy(this.getPlayer());
            this.networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.GAME_MODE_CHANGED, GameMode.SPECTATOR.getIndex()));
            this.networkHandler.sendPacket(VirtualEntityUtils.createSetCameraEntityPacket(possessed.getId()));
        };
    }

    @Override
    public LivingEntity starbound$getPossessingOrPossessedBy() {
        return this.starbound$possessedEntity;
    }
}
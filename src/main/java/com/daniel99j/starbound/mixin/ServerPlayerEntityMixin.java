package com.daniel99j.starbound.mixin;

import com.daniel99j.starbound.Starbound;
import com.daniel99j.starbound.gui.SpellSelectorGui;
import com.daniel99j.starbound.item.ModItems;
import com.daniel99j.starbound.misc.ModEntityComponents;
import com.mojang.authlib.GameProfile;
import eu.pb4.polymer.core.api.entity.PolymerEntity;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.attachment.EntityAttachment;
import eu.pb4.polymer.virtualentity.api.elements.InteractionElement;
import eu.pb4.polymer.virtualentity.api.elements.VirtualElement;
import eu.pb4.sgui.virtual.hotbar.HotbarScreenHandler;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin
        extends PlayerEntity implements PolymerEntity {
    @Unique
    private ElementHolder starbound$holder;
    @Unique
    private InteractionElement starbound$wandInteraction;

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Unique
    private ServerPlayerEntity getPlayer() {
        PlayerEntity player = this;
        return (ServerPlayerEntity) player;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
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
            this.starbound$wandInteraction.setOffset(new Vec3d(0, -10, 0));
            this.starbound$wandInteraction.setInvisible(!FabricLoader.getInstance().isDevelopmentEnvironment());
            this.starbound$wandInteraction.setWidth(20);
            this.starbound$wandInteraction.setHeight(20);
            this.starbound$wandInteraction.setInteractionHandler(getWandInteractionHandler());
            this.starbound$holder.addElement(this.starbound$wandInteraction);
        }

        if (this.starbound$wandInteraction != null && !starbound$shouldHaveWandInteraction()) {
            this.starbound$holder.removeElement(this.starbound$wandInteraction);
            this.starbound$wandInteraction = null;
        }
    }

    private VirtualElement.InteractionHandler getWandInteractionHandler() {
        return new VirtualElement.InteractionHandler() {
            @Override
            public void attack(ServerPlayerEntity player) {
                if(starbound$shouldHaveWandInteraction()) {
                    new SpellSelectorGui(player).open();
                }
            }

            @Override
            public void interact(ServerPlayerEntity player, Hand hand) {
                if(starbound$shouldHaveWandInteraction() && hand == Hand.MAIN_HAND) {
                    if(ModEntityComponents.PLAYER_DATA.get(player).getLastCastSpell() != null) {
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
}
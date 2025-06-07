package com.daniel99j.starbound.mixin;

import com.daniel99j.starbound.misc.PossessionAccessor;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin extends ServerCommonNetworkHandler {
    @Shadow
    public ServerPlayerEntity player;

    public ServerPlayNetworkHandlerMixin(MinecraftServer server, ClientConnection connection, ConnectedClientData clientData) {
        super(server, connection, clientData);
    }

    @Inject(method = "onPlayerMove", at = @At("HEAD"), cancellable = true, order = 10000)
    private void disablePlayerMove(PlayerMoveC2SPacket packet, CallbackInfo ci) {
        NetworkThreadUtils.forceMainThread(packet, (ServerPlayPacketListener) this, this.server);
        if (((PossessionAccessor) player).starbound$getPossessingOrPossessedBy() != null) {
            LivingEntity entity = ((PossessionAccessor) player).starbound$getPossessingOrPossessedBy();
            entity.setPitch(packet.getPitch(0));
            entity.setYaw(packet.getYaw(0));
            entity.setBodyYaw(packet.getYaw(0));
            entity.setHeadYaw(packet.getYaw(0));
//            entity.move();
//            ClientPlayerEntity
        }
    }
}
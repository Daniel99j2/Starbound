package com.daniel99j.starbound.spell;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public abstract class Spell {
    public final Identifier id;
    public final int energy_used;

    public Spell(Identifier id, int energyUsed) {
        this.id = id;
        this.energy_used = energyUsed;
        Spells.SPELLS.add(this);
    }

    public final boolean baseCast(ServerPlayerEntity player) {
        if(canCast(player)) {
            cast(player);
            return true;
        }
        return false;
    }

    protected void cast(ServerPlayerEntity player) {
        player.swingHand(Hand.MAIN_HAND);
        player.sendMessage(Text.of("Basic spell triggered!"));
    }

    protected boolean canCast(ServerPlayerEntity player) {
        return true;
    }

    protected void raycast(ServerPlayerEntity player, int remaining, int steps, float distance, Vec3d currentPos, float pitch, float yaw) {
        if(remaining > 0) {
            double yawRad = Math.toRadians(yaw);
            double pitchRad = Math.toRadians(pitch);

            double x = -Math.sin(yawRad) * Math.cos(pitchRad);
            double y = -Math.sin(pitchRad);
            double z = Math.cos(yawRad) * Math.cos(pitchRad);

            Vec3d accelerationVector = new Vec3d(x, y, z).normalize().multiply(distance);

            Vec3d newPos = currentPos.add(accelerationVector.x, accelerationVector.y, accelerationVector.z);

            if(!player.getWorld().getBlockState(BlockPos.ofFloored(newPos)).getCollisionShape(player.getWorld(), BlockPos.ofFloored(newPos)).isEmpty()) {
                raycast(player, (remaining-1)/2, steps, distance, currentPos, pitch, yaw);
            } else {
                raycast(player, remaining-1, steps, distance, newPos, pitch, yaw);
            }

        }
    }

    public Text getName() {
        return Text.translatable("spell."+this.id.getNamespace()+"."+this.id.getPath());
    }

    public Text getDescription() {
        return Text.translatable("spell."+this.id.getNamespace()+"."+this.id.getPath()+".desc");
    }
}

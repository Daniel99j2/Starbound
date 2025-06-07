package com.daniel99j.starbound.particle.particles;

import com.daniel99j.lib99j.api.NumberUtils;
import com.daniel99j.lib99j.api.ServerParticle;
import com.daniel99j.lib99j.impl.ServerParticleManager;
import net.minecraft.entity.decoration.Brightness;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.server.world.ServerWorld;
import org.joml.Vector3f;

import java.util.ArrayList;

public class ConfettiParticleEffect extends ServerParticle {
    public static ArrayList<ServerParticleManager.ParticleFrame> sprites = new ArrayList<>();
    private final int frame;
    private int randomRotation = NumberUtils.getRandomInt(0, 360);

    public ConfettiParticleEffect(ServerWorld world, double x, double y, double z) {
        this(world, x, y, z, 0, 0, 0);
    }

    public ConfettiParticleEffect(ServerWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.setMaxAge(NumberUtils.getRandomInt(200, 400));
        this.red = NumberUtils.getRandomInt(0, 255);
        this.blue = NumberUtils.getRandomInt(0, 255);
        this.green = NumberUtils.getRandomInt(0, 255);
        this.gravityStrength = 0.2f;
        this.velocityMultiplier = 0.99f;
        this.frame = NumberUtils.getRandomInt(0, 2);
        this.updateFrame();
    }

    @Override
    public ArrayList<ServerParticleManager.ParticleFrame> getSprites() {
        return this.sprites;
    }

    public int getCurrentFrame() {
        return this.frame;
    }

    @Override
    protected void customTick() {
        if(this.onGround) {
            this.display.setBillboardMode(DisplayEntity.BillboardMode.FIXED);
            this.display.setPitch(-90);
            this.display.setYaw(this.randomRotation);
        }
    }
}

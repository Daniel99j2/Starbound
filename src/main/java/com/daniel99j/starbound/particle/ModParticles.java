package com.daniel99j.starbound.particle;

import com.daniel99j.lib99j.impl.ServerParticleManager;
import com.daniel99j.starbound.Starbound;
import com.daniel99j.starbound.particle.particles.ConfettiParticleEffect;
import net.minecraft.util.Identifier;

public class ModParticles {
    public static void load() {
        ConfettiParticleEffect.sprites = ServerParticleManager.loadFrames(Identifier.of(Starbound.MOD_ID, "confetti"), 3, 0, 6, 2);
        ServerParticleManager.registerParticle(new ServerParticleManager.ServerParticleType(Identifier.of(Starbound.MOD_ID, "confetti"), ConfettiParticleEffect.class, ConfettiParticleEffect::new));
    }
}

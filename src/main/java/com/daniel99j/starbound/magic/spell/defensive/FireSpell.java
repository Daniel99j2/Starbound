package com.daniel99j.starbound.magic.spell.defensive;

import com.daniel99j.lib99j.api.EntityUtils;
import com.daniel99j.lib99j.api.ParticleHelper;
import com.daniel99j.lib99j.api.SoundUtils;
import com.daniel99j.starbound.magic.spell.Spell;
import com.daniel99j.starbound.misc.ModSounds;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class FireSpell extends Spell {
    public FireSpell(Identifier id, int energyUsed, int cooldown, int color) {
        super(id, energyUsed, cooldown, color);
    }

    @Override
    protected boolean cast(ServerPlayerEntity player) {
        FireballEntity fireball = new FireballEntity(player.getWorld(), player, Vec3d.ZERO, -1) {
            @Override
            public void tick() {
                super.tick();
                ParticleHelper.spawnParticlesAtPosition(this.getWorld(), this.getPos(), ParticleTypes.LAVA, 4, 0.2, 0.2, 0.2, 0.1);
                if(this.age > 200) this.discard();
            }

            @Override
            public boolean shouldSave() {
                return false;
            }
        };
        fireball.setPos(player.getX(), player.getEyeY(), player.getZ());
        fireball.setItem(Items.BLAZE_POWDER.getDefaultStack());
        EntityUtils.accelerateEntityPitchYaw(fireball, 1.5f, player.getPitch(), player.getYaw());
        player.getWorld().spawnEntity(fireball);
        return true;
    }

    @Override
    protected void castEffects(ServerPlayerEntity player) {
        super.castEffects(player);
        SoundUtils.playSoundAtPosition((ServerWorld) player.getWorld(), player.getEyePos(), ModSounds.FIRE_SPELL_CAST, SoundCategory.PLAYERS, 1, 1);
    }
}

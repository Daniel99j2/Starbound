package com.daniel99j.starbound.magic.spell;

import com.daniel99j.lib99j.api.EntityUtils;
import com.daniel99j.lib99j.api.ParticleHelper;
import com.daniel99j.lib99j.api.SoundUtils;
import net.minecraft.block.Blocks;
import net.minecraft.client.sound.Sound;
import net.minecraft.entity.ItemEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class MagnetSpell extends Spell {
    public MagnetSpell(Identifier id, int energyUsed, int cooldown) {
        super(id, energyUsed, cooldown);
    }

    @Override
    protected boolean cast(ServerPlayerEntity player) {
        boolean out = false;
        for(ItemEntity e : player.getWorld().getEntitiesByClass(ItemEntity.class, new Box(player.getPos().add(-10, -10, -10), player.getPos().add(10, 10, 10)), itemEntity -> itemEntity.distanceTo(player) <= 10)) {
            EntityUtils.accelerateTowards(e, player.getEyePos(), 1);
            SoundUtils.playSoundAtPosition((ServerWorld) player.getWorld(), player.getEyePos(), SoundEvents.BLOCK_CONDUIT_DEACTIVATE, SoundCategory.PLAYERS, 1, 0);
            out = true;
        };
        return out;
    }

    @Override
    protected void castEffects(ServerPlayerEntity player) {
        super.castEffects(player);
        SoundUtils.playSoundAtPosition((ServerWorld) player.getWorld(), player.getEyePos(), SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.PLAYERS, 1, 0);
    }
}

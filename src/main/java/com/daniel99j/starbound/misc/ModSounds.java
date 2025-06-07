package com.daniel99j.starbound.misc;

import com.daniel99j.lib99j.api.SoundUtils;
import com.daniel99j.starbound.Starbound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static final SoundEvent FAKE_PLAYER_DISAPPEAR = SoundUtils.registerCustomSubtitle(Identifier.of(Starbound.MOD_ID, "fake_player_disappear"), SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL, true);
    public static final SoundEvent SPELL_FAIL = SoundUtils.registerCustomSubtitle(Identifier.of(Starbound.MOD_ID, "spell_fail"), SoundEvents.BLOCK_FIRE_EXTINGUISH, true);
    public static final SoundEvent MAGNET_ATTRACT = SoundUtils.registerCustomSubtitle(Identifier.of(Starbound.MOD_ID, "magnet_attract"), SoundEvents.BLOCK_CONDUIT_DEACTIVATE, true);
    public static final SoundEvent MAGNET_SPELL_CAST = SoundUtils.registerCustomSubtitle(Identifier.of(Starbound.MOD_ID, "magnet_spell_cast"), SoundEvents.BLOCK_BEACON_ACTIVATE, true);
    public static final SoundEvent FREEZE_SPELL_CAST = SoundUtils.registerCustomSubtitle(Identifier.of(Starbound.MOD_ID, "freeze_spell_cast"), SoundEvents.ITEM_BUCKET_EMPTY_POWDER_SNOW, true);
    public static final SoundEvent WATER_SPELL_CAST = SoundUtils.registerCustomSubtitle(Identifier.of(Starbound.MOD_ID, "water_spell_cast"), SoundEvents.ITEM_BUCKET_EMPTY, true);
    public static final SoundEvent POSSESS_SPELL_CAST = SoundUtils.registerCustomSubtitle(Identifier.of(Starbound.MOD_ID, "possess_spell_cast"), SoundEvents.ENTITY_ILLUSIONER_PREPARE_BLINDNESS, true);
    public static final SoundEvent DUMMY_SPELL_CAST = SoundUtils.registerCustomSubtitle(Identifier.of(Starbound.MOD_ID, "dummy_spell_cast"), SoundEvents.ENTITY_ILLUSIONER_PREPARE_MIRROR, true);
    public static final SoundEvent CONFETTI_SPELL_CAST = SoundUtils.registerCustomSubtitle(Identifier.of(Starbound.MOD_ID, "confetti_spell_cast"), SoundEvents.ENTITY_ALLAY_AMBIENT_WITH_ITEM, true);
    public static final SoundEvent TIME_CONTROL_SPELL_CAST = SoundUtils.registerCustomSubtitle(Identifier.of(Starbound.MOD_ID, "time_control_spell_cast"), SoundEvents.ITEM_BUCKET_EMPTY, true);
    public static final SoundEvent DEATH_RAY_SPELL_CAST = SoundUtils.registerCustomSubtitle(Identifier.of(Starbound.MOD_ID, "death_ray_spell_cast"), SoundEvents.BLOCK_BEACON_ACTIVATE, true);
    public static final SoundEvent DEATH_RAY_BLAST = SoundUtils.registerCustomSubtitle(Identifier.of(Starbound.MOD_ID, "death_ray_blast"), SoundEvents.BLOCK_BEACON_POWER_SELECT, true);
    public static final SoundEvent DEATH_RAY_EXPLOSION = SoundUtils.registerCustomSubtitle(Identifier.of(Starbound.MOD_ID, "death_ray_explosion"), SoundEvents.ENTITY_GENERIC_EXPLODE.value(), true);
    public static final SoundEvent PHASE_SPELL_CAST = SoundUtils.registerCustomSubtitle(Identifier.of(Starbound.MOD_ID, "phase_spell_cast"), SoundEvents.ENTITY_PLAYER_TELEPORT, true);
    public static final SoundEvent LIGHT_BEAM_SPELL_CAST = SoundUtils.registerCustomSubtitle(Identifier.of(Starbound.MOD_ID, "light_beam_spell_cast"), SoundEvents.ITEM_AXE_SCRAPE, true);
    public static final SoundEvent SONIC_SPELL_CAST = SoundUtils.registerCustomSubtitle(Identifier.of(Starbound.MOD_ID, "sonic_spell_cast"), SoundEvents.ENTITY_WARDEN_SONIC_BOOM, true);
    public static final SoundEvent SONIC_SPELL_CAST_1 = SoundUtils.registerCustomSubtitle(Identifier.of(Starbound.MOD_ID, "sonic_spell_cast_1"), SoundEvents.ENTITY_WARDEN_SONIC_CHARGE, true);

    public static void load() {
        
    }
}

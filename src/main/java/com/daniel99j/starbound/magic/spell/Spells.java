package com.daniel99j.starbound.magic.spell;

import com.daniel99j.starbound.Starbound;
import com.daniel99j.starbound.magic.spell.defensive.*;
import com.daniel99j.starbound.magic.spell.offensive.HealingSpell;
import com.daniel99j.starbound.magic.spell.offensive.ShieldSpell;
import com.daniel99j.starbound.magic.spell.other.*;
import com.daniel99j.starbound.magic.spell.utility.*;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Optional;

@SuppressWarnings("unused")
public class Spells {
    protected static final ArrayList<Spell> SPELLS = new ArrayList<>();
    public static final Spell TELEPORT_SPELL = new PhaseSpell(Identifier.of(Starbound.MOD_ID, "phase"), 200, 100, 0x000000);
    public static final Spell MAGNET_SPELL = new MagnetSpell(Identifier.of(Starbound.MOD_ID, "magnet"), 80, 60, 0xAAAAAA);
    public static final Spell DEATH_RAY_SPELL = new DeathRaySpell(Identifier.of(Starbound.MOD_ID, "death_ray"), 400, 1200, 0xFFFFFF);
    public static final Spell WATER_SPELL = new WaterSpell(Identifier.of(Starbound.MOD_ID, "water"), 100, 60, 0x0000FF);
    public static final Spell LIGHT_BEAM_SPELL = new LightBeamSpell(Identifier.of(Starbound.MOD_ID, "light_beam"), 120, 80, 0xFFFFFF);
    public static final Spell FIRE_SPELL = new FireSpell(Identifier.of(Starbound.MOD_ID, "fire"), 180, 100, 0xFF0000);
    public static final Spell POSSESS_SPELL = new PossessSpell(Identifier.of(Starbound.MOD_ID, "possess"), 300, 300, 0x00FFFF);
    public static final Spell TIME_CONTROL_SPELL = new TimeControlSpell(Identifier.of(Starbound.MOD_ID, "time_control"), 350, 400, 0xFF00FF);
    public static final Spell DUMMY_SPELL = new DummySpell(Identifier.of(Starbound.MOD_ID, "dummy"), 90, 80, 0xABCDEF);
    public static final Spell FREEZE_SPELL = new FreezeSpell(Identifier.of(Starbound.MOD_ID, "freeze"), 200, 100, 0xAAAAFF);
    public static final Spell CONFETTI_SPELL = new ConfettiSpell(Identifier.of(Starbound.MOD_ID, "confetti"), 30, 40, 0xAFAFAF);
    public static final Spell SONIC_SPELL = new SonicBoomSpell(Identifier.of(Starbound.MOD_ID, "sonic"), 160, 100, 0x0099FF);
    public static final Spell LIGHTNING_SPELL = new LightningSpell(Identifier.of(Starbound.MOD_ID, "lightning"), 250, 150, 0xFFFFFF);
    public static final Spell FLOAT_SPELL = new FloatSpell(Identifier.of(Starbound.MOD_ID, "float"), 100, 60, 0xFFFFFF);
    public static final Spell EARTHQUAKE_SPELL = new EarthquakeSpell(Identifier.of(Starbound.MOD_ID, "earthquake"), 400, 300, 0xFFFFFF);
    public static final Spell VOID_RIFT_SPELL = new VoidRiftSpell(Identifier.of(Starbound.MOD_ID, "void_rift"), 380, 200, 0x000000);
    public static final Spell HEALING_SPELL = new HealingSpell(Identifier.of(Starbound.MOD_ID, "healing"), 180, 100, 0xFF0000);
    public static final Spell SHIELD_SPELL = new ShieldSpell(Identifier.of(Starbound.MOD_ID, "shield"), 150, 80, 0xAAAAAA);
    public static final Spell GROW_SPELL = new GrowSpell(Identifier.of(Starbound.MOD_ID, "plant_grow"), 120, 60, 0x0000FF);
    public static final Spell LEVITATION_SPELL = new LevitationSpell(Identifier.of(Starbound.MOD_ID, "levitation"), 140, 80, 0xFFFFFF);
    public static final Spell TRANSMUTATION_SPELL = new TransmutationSpell(Identifier.of(Starbound.MOD_ID, "transmutation"), 200, 120, 0xFFFFFF);

    public static void init() {

    }

    public static ArrayList<Spell> getSpells() {
        return SPELLS;
    }

    public static Optional<Spell> getSpell(Identifier id) {
        return Spells.getSpells().stream().filter(spell -> spell.id.equals(id)).findFirst();
    }

    public static Optional<Spell> getSpellOrWarn(Identifier id) {
        Optional<Spell> test = getSpell(id);
        if (test.isEmpty()) {
            Starbound.LOGGER.warn("Tried to get spell with id {}, but it does not exist!", id);
        }
        return test;
    }

    public static Spell getSpellOrFirstWarn(Identifier id) {
        return getSpellOrWarn(id).orElse(SPELLS.getFirst());
    }
}

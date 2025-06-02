package com.daniel99j.starbound.magic.spell;

import com.daniel99j.starbound.Starbound;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class Spells {
    protected static final ArrayList<Spell> SPELLS = new ArrayList<>();
    public static final Spell WATER_SPELL = new WaterSpell(Identifier.of(Starbound.MOD_ID, "water"), 1, 5);
    public static final Spell TELEPORT_SPELL = new PhaseSpell(Identifier.of(Starbound.MOD_ID, "teleport"), 1, 5);
    public static final Spell MAGNET_SPELL = new MagnetSpell(Identifier.of(Starbound.MOD_ID, "magnet"), 1, 5);
    public static final Spell DEATH_RAY_SPELL = new DeathRaySpell(Identifier.of(Starbound.MOD_ID, "death_ray"), 1, 5);
    public static final Spell LIGHT_BEAM_SPELL = new LightBeamSpell(Identifier.of(Starbound.MOD_ID, "light_beam"), 1, 5);
    public static final Spell POSSESS_SPELL = new PossessSpell(Identifier.of(Starbound.MOD_ID, "possess"), 1, 5);
    public static final Spell TIME_CONTROL_SPELL = new TimeControlSpell(Identifier.of(Starbound.MOD_ID, "time_control"), 1, 5);
    public static final Spell WIND_BARRIER_SPELL = new WindBarrier(Identifier.of(Starbound.MOD_ID, "wind_barrier"), 1, 5);
    public static final Spell FREEZE_SPELL = new FreezeSpell(Identifier.of(Starbound.MOD_ID, "freeze"), 1, 5);

    public static void init() {

    }

    public static ArrayList<Spell> getSpells() {
        return SPELLS;
    }
}

package com.daniel99j.starbound.magic.spell;

import com.daniel99j.starbound.Starbound;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class Spells {
    protected static final ArrayList<Spell> SPELLS = new ArrayList<>();
    public static final Spell FIRE_SPELL = new FireSpell(Identifier.of(Starbound.MOD_ID, "fire"), 1, 10);
    public static final Spell WIND_SPELL = new WindSpell(Identifier.of(Starbound.MOD_ID, "wind"), 1, 40);
    public static final Spell EARTH_SPELL = new EarthSpell(Identifier.of(Starbound.MOD_ID, "earth"), 1, 200);
    public static final Spell WATER_SPELL = new WaterSpell(Identifier.of(Starbound.MOD_ID, "water"), 1, 200);
    public static final Spell TELEPORT_SPELL = new PhaseSpell(Identifier.of(Starbound.MOD_ID, "teleport"), 1, 100);
    public static final Spell TELEPORT_SPELL1 = new FireSpell(Identifier.of(Starbound.MOD_ID, "teleport1"), 1, 100);
    public static final Spell TELEPORT_SPELL2 = new FireSpell(Identifier.of(Starbound.MOD_ID, "teleport2"), 1, 100);
    public static final Spell TELEPORT_SPELL3 = new FireSpell(Identifier.of(Starbound.MOD_ID, "teleport3"), 1, 100);
    public static final Spell TELEPORT_SPELL4 = new FireSpell(Identifier.of(Starbound.MOD_ID, "teleport4"), 1, 100);
    public static final Spell TELEPORT_SPELL5 = new FireSpell(Identifier.of(Starbound.MOD_ID, "teleport5"), 1, 100);
    public static final Spell TELEPORT_SPELL6 = new FireSpell(Identifier.of(Starbound.MOD_ID, "teleport6"), 1, 100);
    public static final Spell TELEPORT_SPELL7 = new FireSpell(Identifier.of(Starbound.MOD_ID, "teleport7"), 1, 100);
    public static final Spell TELEPORT_SPELL8 = new FireSpell(Identifier.of(Starbound.MOD_ID, "teleport8"), 1, 100);

    public static void init() {

    }

    public static ArrayList<Spell> getSpells() {
        return SPELLS;
    }
}

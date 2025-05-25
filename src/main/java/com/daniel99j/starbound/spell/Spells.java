package com.daniel99j.starbound.spell;

import com.daniel99j.starbound.Starbound;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class Spells {
    protected static final ArrayList<Spell> SPELLS = new ArrayList<>();
    public static final Spell FIRE_SPELL = new FireSpell(Identifier.of(Starbound.MOD_ID, "fire"), 1);
    public static final Spell WIND_SPELL = new WindSpell(Identifier.of(Starbound.MOD_ID, "wind"), 1);
    public static final Spell EARTH_SPELL = new EarthSpell(Identifier.of(Starbound.MOD_ID, "earth"), 1);
    public static final Spell WATER_SPELL = new WaterSpell(Identifier.of(Starbound.MOD_ID, "water"), 1);

    public static void init() {

    }

    public static ArrayList<Spell> getSpells() {
        return SPELLS;
    }
}

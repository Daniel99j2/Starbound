package com.daniel99j.starbound.misc;

import eu.pb4.polymer.core.api.other.PolymerStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class HiddenStatusEffect extends StatusEffect implements PolymerStatusEffect {
    protected HiddenStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }
}

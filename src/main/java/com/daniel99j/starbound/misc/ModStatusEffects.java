package com.daniel99j.starbound.misc;

import com.daniel99j.starbound.Starbound;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ModStatusEffects {
    public static final RegistryEntry<StatusEffect> FLOAT = register(
            "float",
            new HiddenStatusEffect(StatusEffectCategory.NEUTRAL, -1)
                    .addAttributeModifier(EntityAttributes.GRAVITY, Identifier.of(Starbound.MOD_ID, "effect.float"), -1F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    );

    public static final RegistryEntry<StatusEffect> SHIELD = register(
            "shield",
            new ShieldStatusEffect(StatusEffectCategory.NEUTRAL, -1)
    );

    private static RegistryEntry<StatusEffect> register(String id, StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(Starbound.MOD_ID, id), statusEffect);
    }

    public static void load() {

    }
}

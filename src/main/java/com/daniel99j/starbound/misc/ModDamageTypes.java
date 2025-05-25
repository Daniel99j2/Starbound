package com.daniel99j.starbound.misc;

import com.daniel99j.starbound.Starbound;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ModDamageTypes {
    public static final RegistryKey<DamageType> PULSAR_BEAM = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(Starbound.MOD_ID, "pulsar_beam"));

    public static void load() {
        Starbound.debug("Loading damage types");
    }

    public static RegistryEntry<DamageType> of(World world, RegistryKey<DamageType> key) {
        return world.getRegistryManager().getOrThrow(RegistryKeys.DAMAGE_TYPE).getOrThrow(key);
    }
}

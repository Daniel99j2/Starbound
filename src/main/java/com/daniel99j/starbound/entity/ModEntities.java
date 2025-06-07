package com.daniel99j.starbound.entity;

import com.daniel99j.starbound.Starbound;
import eu.pb4.polymer.core.api.entity.PolymerEntityUtils;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<DummyEntity> DUMMY
            = register("dummy", FabricEntityTypeBuilder
            .createLiving().defaultAttributes(DummyEntity::createTestDummyAttributes).disableSaving().disableSummon().trackedUpdateRate(1).dimensions(EntityDimensions.fixed(0.7f, 1.2f)).entityFactory(DummyEntity::new));

    public static void register() {
        Starbound.debug("Loading entities");
    }

    public static <T extends Entity> EntityType<T> register(String path, FabricEntityTypeBuilder<T> item) {
        var x = Registry.register(Registries.ENTITY_TYPE, Identifier.of(Starbound.MOD_ID, path), item.build(RegistryKey.of(Registries.ENTITY_TYPE.getKey(), Identifier.of(Starbound.MOD_ID, path))));
        PolymerEntityUtils.registerType(x);
        return x;
    }
}
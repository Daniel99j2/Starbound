package com.daniel99j.starbound.misc;

import com.daniel99j.starbound.Starbound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public class ModEntityComponents implements EntityComponentInitializer {
    public static final ComponentKey<StarboundPlayerData> PLAYER_DATA = ComponentRegistry.getOrCreate(Identifier.of(Starbound.MOD_ID, "player_data"), StarboundPlayerData.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(ServerPlayerEntity.class, PLAYER_DATA).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(StarboundPlayerData::new);
    }
}
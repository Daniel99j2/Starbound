package com.daniel99j.starbound.misc;

import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public interface PossessionAccessor {
	void starbound$setPossessedEntity(LivingEntity possessed);

	void starbound$setPossessedBy(ServerPlayerEntity possessed);

	LivingEntity starbound$getPossessingOrPossessedBy();
}
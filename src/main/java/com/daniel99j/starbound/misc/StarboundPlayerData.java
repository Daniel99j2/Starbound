package com.daniel99j.starbound.misc;

import com.daniel99j.starbound.magic.spell.Spell;
import com.daniel99j.starbound.magic.spell.Spells;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

public class StarboundPlayerData implements Component, ServerTickingComponent {
    private final ServerPlayerEntity player;
    @Nullable
    private Spell last_cast_spell;

    public StarboundPlayerData(ServerPlayerEntity player) {
        this.player = player;

    }

    public void readFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        if(nbt.contains("last_cast_spell")) {
            last_cast_spell = Spells.getSpells().stream()
                    .filter(spell -> spell.id.equals(nbt.get("last_cast_spell", Identifier.CODEC)
                            .orElse(Spells.getSpells().getFirst().id)))
                    .findFirst()
                    .orElse(Spells.getSpells().getFirst());
        }
    }

    public void writeToNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        if(last_cast_spell != null) nbt.put("last_cast_spell", Identifier.CODEC, last_cast_spell.id);
        else nbt.remove("last_cast_spell");
    }

    public void serverTick() {
    }

    public @Nullable Spell getLastCastSpell() {
        return last_cast_spell;
    }

    public void setLastCastSpell(@Nullable Spell last_cast_spell) {
        this.last_cast_spell = last_cast_spell;
    }
}

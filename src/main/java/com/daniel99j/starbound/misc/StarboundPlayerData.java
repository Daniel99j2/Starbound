package com.daniel99j.starbound.misc;

import com.daniel99j.starbound.magic.spell.Spell;
import com.daniel99j.starbound.magic.spell.Spells;
import net.minecraft.nbt.*;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.ArrayList;

public class StarboundPlayerData implements Component, ServerTickingComponent {
    private final ServerPlayerEntity player;
    @Nullable
    private Spell last_cast_spell;
    private int energy = 1000;
    public static final int MAX_ENERGY = 10000;

    private ArrayList<SpellPage> spellPages = new ArrayList<>(3*4);
    private int pages = 1;
    private int currentPage = 0;

    private ArrayList<Spell> unlockedSpells = new ArrayList<>();

    public StarboundPlayerData(ServerPlayerEntity player) {
        this.player = player;
        spellPages.add(new SpellPage(0));
        spellPages.add(new SpellPage(1));
        spellPages.add(new SpellPage(2));
    }

    public void readFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        if(nbt.contains("last_cast_spell")) {
            last_cast_spell = Spells.getSpellOrFirstWarn(nbt.get("last_cast_spell", Identifier.CODEC).orElse(null));
        }
        energy = nbt.getInt("energy", MAX_ENERGY);

        for (SpellPage spellPage : spellPages) {
            spellPage.readFromNbt(nbt, wrapperLookup);
        }

        unlockedSpells.clear();
        NbtList unlockedSpellsList = nbt.getListOrEmpty("unlocked_spells");
        for (NbtElement nbtElement : unlockedSpellsList) {
            Identifier id = Identifier.of(nbtElement.asString().orElse("error:couldnt_decode"));
            Spells.getSpellOrWarn(id).ifPresent(spell -> unlockedSpells.add(spell));
        }
    }

    public void writeToNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        if(last_cast_spell != null) nbt.put("last_cast_spell", Identifier.CODEC, last_cast_spell.id);
        else nbt.remove("last_cast_spell");
        nbt.putInt("energy", energy);

        for (SpellPage spellPage : spellPages) {
            spellPage.writeToNbt(nbt, wrapperLookup);
        }

        NbtList unlockedSpellsList = new NbtList();
        for (Spell spell : unlockedSpells) {
            unlockedSpellsList.add(NbtString.of(spell.id.toString()));
        }
        nbt.put("unlocked_spells", unlockedSpellsList);
    }

    public void serverTick() {
    }

    public @Nullable Spell getLastCastSpell() {
        return last_cast_spell;
    }

    public void setLastCastSpell(@Nullable Spell last_cast_spell) {
        this.last_cast_spell = last_cast_spell;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = Math.max(0, Math.min(energy, MAX_ENERGY));
    }

    public void addEnergy(int amount) {
        setEnergy(getEnergy() + amount);
    }

    public boolean isSpellLocked(Spell spell) {
        return !unlockedSpells.contains(spell);
    }

    public static class SpellPage {
        public @Nullable Spell spell0;
        public @Nullable Spell spell1;
        public @Nullable Spell spell2;
        public @Nullable Spell spell3;
        public int pageNumber;

        private SpellPage(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public void readFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
            if(nbt.contains("page"+pageNumber+"_spell0")) {
                spell0 = Spells.getSpellOrWarn(nbt.get("page"+pageNumber+"_spell0", Identifier.CODEC).orElse(Identifier.of("error:couldnt_decode"))).orElse(null);
            } else spell0 = null;

            if(nbt.contains("page"+pageNumber+"_spell1")) {
                spell1 = Spells.getSpellOrWarn(nbt.get("page"+pageNumber+"_spell1", Identifier.CODEC).orElse(Identifier.of("error:couldnt_decode"))).orElse(null);
            } else spell1 = null;

            if(nbt.contains("page"+pageNumber+"_spell2")) {
                spell2 = Spells.getSpellOrWarn(nbt.get("page"+pageNumber+"_spell2", Identifier.CODEC).orElse(Identifier.of("error:couldnt_decode"))).orElse(null);
            } else spell2 = null;

            if(nbt.contains("page"+pageNumber+"_spell3")) {
                spell3 = Spells.getSpellOrWarn(nbt.get("page"+pageNumber+"_spell3", Identifier.CODEC).orElse(Identifier.of("error:couldnt_decode"))).orElse(null);
            } else spell3 = null;
        }

        public void writeToNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
            if(spell0 != null) nbt.put("page"+pageNumber+"_spell0", Identifier.CODEC, spell0.id);
            else nbt.remove("page"+pageNumber+"_spell0");
            if(spell1 != null) nbt.put("page"+pageNumber+"_spell1", Identifier.CODEC, spell1.id);
            else nbt.remove("page"+pageNumber+"_spell1");
            if(spell2 != null) nbt.put("page"+pageNumber+"_spell2", Identifier.CODEC, spell2.id);
            else nbt.remove("page"+pageNumber+"_spell2");
            if(spell3 != null) nbt.put("page"+pageNumber+"_spell3", Identifier.CODEC, spell3.id);
            else nbt.remove("page"+pageNumber+"_spell3");
        }

    }
}

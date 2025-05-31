package com.daniel99j.starbound.datagen;

import com.daniel99j.starbound.Starbound;
import com.daniel99j.starbound.magic.spell.Spell;
import com.daniel99j.starbound.magic.spell.Spells;
import com.google.common.hash.HashCode;
import eu.pb4.polymer.resourcepack.api.AssetPaths;
import eu.pb4.polymer.resourcepack.extras.api.format.item.ItemAsset;
import eu.pb4.polymer.resourcepack.extras.api.format.item.model.BasicItemModel;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class AssetProvider implements DataProvider {
    private final DataOutput output;
    private static final List<String> disabledAutoModel = List.of();

    private static final String BASIC_ITEM_TEMPLATE = """
            {
              "parent": "%BASE%",
              "textures": {
                "layer0": "%ID%"
              }
            }
            """.replace(" ", "").replace("\n", "");

    private static final String SPELL_ITEM_FILE = """
            {
              "model": {
                "type": "minecraft:select",
                "cases": [
                  {
                    "model": {
                      "type": "minecraft:model",
                      "model": "%gui%"
                    },
                    "when": [
                      "gui"
                    ]
                  },
                  {
                    "when": "firstperson_righthand",
                    "model": {
                      "type": "minecraft:model",
                      "model": "%hand%"
                    }
                  },
                  {
                    "when": "firstperson_lefthand",
                    "model": {
                      "type": "minecraft:model",
                      "model": "%hand%"
                    }
                  },
                  {
                    "when": "thirdperson_righthand",
                    "model": {
                      "type": "minecraft:model",
                      "model": "%hand%"
                    }
                  },
                  {
                    "when": "thirdperson_lefthand",
                    "model": {
                      "type": "minecraft:model",
                      "model": "%hand%"
                    }
                  }
                ],
                "property": "minecraft:display_context",
                "fallback": {
                  "type": "minecraft:model",
                  "model": "%other%"
                }
              }
            }
            """.replace(" ", "").replace("\n", "");

    public AssetProvider(FabricDataOutput output) {
        this.output = output;
    }

    public static void runWriters(BiConsumer<String, byte[]> assetWriter) {
        var map = new HashMap<Identifier, ItemAsset>();
        createItems(map::put, assetWriter);
        map.forEach((id, asset) -> assetWriter.accept(AssetPaths.itemAsset(id), asset.toJson().getBytes(StandardCharsets.UTF_8)));
    }

    private static void createItems(BiConsumer<Identifier, ItemAsset> consumer, BiConsumer<String, byte[]> assetWriter) {
        for (var item : Registries.ITEM) {
            var id = Registries.ITEM.getId(item);
            if (!id.getNamespace().equals(Starbound.MOD_ID) || disabledAutoModel.contains(id.getPath())) {
                continue;
            }
            consumer.accept(id, new ItemAsset(new BasicItemModel(id.withPrefixedPath(item instanceof BlockItem ? "block/" : "item/")), ItemAsset.Properties.DEFAULT));

            if (!(item instanceof BlockItem)) {
                assetWriter.accept("assets/" + Starbound.MOD_ID + "/models/item/" + id.getPath() + ".json",
                        BASIC_ITEM_TEMPLATE.replace("%ID%", Identifier.of(Starbound.MOD_ID, "item/" + id.getPath()).toString()).replace("%BASE%", "minecraft:item/generated").getBytes(StandardCharsets.UTF_8));
            }
        }

        assetWriter.accept("assets/" + Starbound.MOD_ID + "/items/wand.json",
                SPELL_ITEM_FILE.replace("%gui%", "starbound:item/wand_gui").replace("%hand%", "starbound:item/wand_hand").replace("%other%", "starbound:item/wand_other").getBytes(StandardCharsets.UTF_8));

        for(Spell spell : Spells.getSpells()) {
            assetWriter.accept("assets/" + spell.id.getNamespace() + "/models/gui/spell/" + spell.id.getPath() + ".json",
                    BASIC_ITEM_TEMPLATE.replace("%ID%", spell.id.getNamespace() + ":spell/" + spell.id.getPath()).replace("%BASE%", "minecraft:item/generated").getBytes(StandardCharsets.UTF_8));
            assetWriter.accept("assets/" + spell.id.getNamespace() + "/items/spell/" + spell.id.getPath() + ".json",
                    SPELL_ITEM_FILE.replace("%gui%", spell.id.getNamespace() + ":gui/spell/" + spell.id.getPath()).replace("%hand%", "starbound:item/wand_hand").replace("%other%", "starbound:item/wand_other").getBytes(StandardCharsets.UTF_8));
        }
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        BiConsumer<String, byte[]> assetWriter = (path, data) -> {
            try {
                writer.write(this.output.getPath().resolve(path), data, HashCode.fromBytes(data));
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        return CompletableFuture.runAsync(() -> {
            runWriters(assetWriter);
        }, Util.getMainWorkerExecutor());
    }

    @Override
    public String getName() {
        return "Assets2";
    }
}

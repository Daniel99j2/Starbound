package com.daniel99j.starbound.datagen;

import com.daniel99j.starbound.Starbound;
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
    private static final List<String> disabledAutoModel = List.of("crab_claw", "invalid_item", "tarsis_rock", "tarsis_deep_rock", "copper_horn", "north_compass", "blackstone_piston", "blackstone_sticky_piston", "sleeping_bag", "trip_wire");

    private static final String BASIC_ITEM_TEMPLATE = """
            {
              "parent": "%BASE%",
              "textures": {
                "layer0": "%ID%"
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

            if(!(item instanceof BlockItem)) {
                assetWriter.accept("assets/" + Starbound.MOD_ID + "/models/item/" + id.getPath() + ".json",
                        BASIC_ITEM_TEMPLATE.replace("%ID%", Identifier.of(Starbound.MOD_ID, "item/"+id.getPath()).toString()).replace("%BASE%", "minecraft:item/generated").getBytes(StandardCharsets.UTF_8));
            }
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

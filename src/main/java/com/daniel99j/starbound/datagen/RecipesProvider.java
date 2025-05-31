package com.daniel99j.starbound.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

class RecipesProvider extends FabricRecipeProvider {
    public RecipesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
        return new RecipeGenerator(registryLookup, exporter) {
            @Override
            public void generate() {
                var itemWrap = registryLookup.getOrThrow(RegistryKeys.ITEM);
//                ShapedRecipeJsonBuilder.create(itemWrap, RecipeCategory.MISC, ModItems.SLEEPING_BAG_ITEM, 1)
//                        .pattern("c  ")
//                        .pattern("www")
//                        .pattern("sss")
//                        .input('c', ItemTags.WOOL_CARPETS)
//                        .input('w', ItemTags.WOOL)
//                        .input('s', Items.STICK)
//                        .criterion("get_wool", InventoryChangedCriterion.Conditions.items(Items.WHITE_WOOL))
//                        .offerTo(exporter);
            }

            private String getShortString(RegistryEntry<?> entry) {
                //noinspection OptionalGetWithoutIsPresent
                var key = entry.getKey().get().getValue();

                return key.getNamespace().equals(Identifier.DEFAULT_NAMESPACE) ? key.getPath().replace("/", "_") : key.toUnderscoreSeparatedString();
            }


            public void of(RecipeExporter exporter, RecipeEntry<?>... recipes) {
                for (var recipe : recipes) {
                    exporter.accept(recipe.id(), recipe.value(), null);
                }
            }
        };
    }

    @Override
    public String getName() {
        return "Item Recipes";
    }
}

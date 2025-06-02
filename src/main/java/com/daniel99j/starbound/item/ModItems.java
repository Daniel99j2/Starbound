package com.daniel99j.starbound.item;

import com.daniel99j.lib99j.api.FixedPolymerBlockItem;
import com.daniel99j.lib99j.api.PolymerSpawnEgg;
import com.daniel99j.starbound.Starbound;
import com.daniel99j.starbound.block.ModBlocks;
import eu.pb4.factorytools.api.item.FactoryBlockItem;
import eu.pb4.polymer.core.api.item.PolymerItemGroupUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class ModItems {
    public static final Item WAND_ITEM = register("wand", WandItem::new, new Item.Settings().maxCount(1).attributeModifiers(new AttributeModifiersComponent(List.of(
            new AttributeModifiersComponent.Entry(EntityAttributes.BLOCK_INTERACTION_RANGE, new EntityAttributeModifier(Identifier.of(Starbound.MOD_ID, "wand"), -1, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), AttributeModifierSlot.MAINHAND))))
            .component(DataComponentTypes.TOOLTIP_DISPLAY, new TooltipDisplayComponent(false, new LinkedHashSet<>(List.of(DataComponentTypes.ATTRIBUTE_MODIFIERS)))));
    public static final Item GRAVITY_ANCHOR_ITEM = register("gravity_anchor", GravityAnchorItem::new, new Item.Settings().maxCount(1));
    public static final Item PULSAR_REDIRECTOR_BLOCK = register(ModBlocks.PULSAR_REDIRECTOR);
    public static final Item PULSAR_TRANSMITTER_BLOCK = register(ModBlocks.PULSAR_TRANSMITTER);
    public static final RegistryKey<EquipmentAsset> LENS_ASSET = RegistryKey.of(EquipmentAssetKeys.REGISTRY_KEY, Identifier.of(Starbound.MOD_ID, "lens_goggles"));
    public static final Item PRISM_LENS_GOGGLES = register("prism_lens_goggles", PrismLensGoggles::new, new Item.Settings().maxCount(1).component(DataComponentTypes.EQUIPPABLE, new EquippableComponent(EquipmentSlot.HEAD, SoundEvents.ITEM_ARMOR_EQUIP_IRON, Optional.ofNullable(LENS_ASSET), Optional.of(Identifier.of(Starbound.MOD_ID, "misc/lens_goggles")), Optional.empty(), true, true, false, true)));
    public static final RegistryKey<EquipmentAsset> INVIS_CLOAK_ASSET = RegistryKey.of(EquipmentAssetKeys.REGISTRY_KEY, Identifier.of(Starbound.MOD_ID, "invisibility_cloak"));
    public static final Item INVIS_CLOAK_ITEM = register("invisibility_cloak", InvisCloakItem::new, new Item.Settings().maxCount(1).component(DataComponentTypes.EQUIPPABLE, new EquippableComponent(EquipmentSlot.CHEST, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, Optional.ofNullable(INVIS_CLOAK_ASSET), Optional.empty(), Optional.empty(), true, true, false, true)));
    public static final Item ASTRAL_FABRICATOR_ITEM = register("astral_fabricator", AstralFabricatorItem::new, new Item.Settings().maxCount(1));
    public static final Item MYSTERIOUS_CORE_ITEM = register(ModBlocks.MYSTERIOUS_CORE);
    public static final Item TURRET_ITEM = register(ModBlocks.TURRET);
    public static final Item TEST_MACHINE_ITEM = register(ModBlocks.TEST_MACHINE);

    public static Item registerSpawnEgg(EntityType<? extends MobEntity> entityType) {
        return register(keyOf(entityType.getRegistryEntry().registryKey().getValue().getPath()+"_spawn_egg"), settings -> new PolymerSpawnEgg(entityType, settings), new Item.Settings());
    }

    private static RegistryKey<Item> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Starbound.MOD_ID, id));
    }

    private static RegistryKey<Item> keyOf(RegistryKey<Block> blockKey) {
        return RegistryKey.of(RegistryKeys.ITEM, blockKey.getValue());
    }

    public static Item register(Block block, Item polymerItem) {
        return register(block, (blockx, settings) -> new FixedPolymerBlockItem(blockx, settings, polymerItem, true));
    }

    public static Item register(Block block, Item.Settings settings, Item polymerItem) {
        return register(block, (blockx, settingsx) -> new FixedPolymerBlockItem(blockx, settingsx, polymerItem, true), settings);
    }

    //vanilla

    public static Item register(Block block) {
        return register(block, FixedPolymerBlockItem::new);
    }

    public static Item register(Block block, Item.Settings settings) {
        return register(block, FixedPolymerBlockItem::new, settings);
    }

    public static Item register(Block block, UnaryOperator<Item.Settings> settingsOperator) {
        return register(block, (BiFunction<Block, Item.Settings, Item>)((blockx, settings) -> new FixedPolymerBlockItem(blockx, (Item.Settings)settingsOperator.apply(settings))));
    }

    public static Item register(Block block, Block... blocks) {
        Item item = register(block);

        for (Block block2 : blocks) {
            Item.BLOCK_ITEMS.put(block2, item);
        }

        return item;
    }

    public static Item register(Block block, BiFunction<Block, Item.Settings, Item> factory) {
        return register(block, factory, new Item.Settings());
    }

    public static Item register(Block block, BiFunction<Block, Item.Settings, Item> factory, Item.Settings settings) {
        return register(
                keyOf(block.getRegistryEntry().registryKey()), itemSettings -> (Item)factory.apply(block, itemSettings), settings.useBlockPrefixedTranslationKey()
        );
    }

    public static Item register(String id, Function<Item.Settings, Item> factory) {
        return register(keyOf(id), factory, new Item.Settings());
    }

    public static Item register(String id, Function<Item.Settings, Item> factory, Item.Settings settings) {
        return register(keyOf(id), factory, settings);
    }

    public static Item register(String id, Item.Settings settings) {
        return register(keyOf(id), Item::new, settings);
    }

    public static Item register(String id) {
        return register(keyOf(id), Item::new, new Item.Settings());
    }

    public static Item register(RegistryKey<Item> key, Function<Item.Settings, Item> factory) {
        return register(key, factory, new Item.Settings());
    }

    public static Item register(RegistryKey<Item> key, Function<Item.Settings, Item> factory, Item.Settings settings) {
        Item item = (Item)factory.apply(settings.registryKey(key));
        if (item instanceof FactoryBlockItem blockItem) {
            blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
        }

        return Registry.register(Registries.ITEM, key, item);
    }

    public static void registerModItems() {
        Starbound.debug("Loading items");

        //use dev env as i might use the config on my client
        if(FabricLoader.getInstance().isDevelopmentEnvironment()) {
            PolymerItemGroupUtils.registerPolymerItemGroup(Identifier.of(Starbound.MOD_ID, "item_group"), ItemGroup.create(ItemGroup.Row.BOTTOM, -1)
                    .icon(WAND_ITEM::getDefaultStack)
                    .displayName(Text.translatable("itemgroup." + Starbound.MOD_ID))
                    .entries(((context, entries) -> {
                        for (Identifier i : Registries.ITEM.getIds().stream().sorted(Comparator.comparing(Identifier::toString)).toList()) {
                            if(Objects.equals(i.getNamespace(), Starbound.MOD_ID)) entries.add(Registries.ITEM.get(i));
                        }
                    })).build()
            );
        }
    }
}

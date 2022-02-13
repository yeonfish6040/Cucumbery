package com.jho5245.cucumbery.util.itemlore;

import com.destroystokyo.paper.MaterialTags;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.storage.no_groups.ItemCategory;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemLore4
{
  protected static void setItemLore(@NotNull ItemStack itemStack)
  {
    NBTItem nbtItem = new NBTItem(itemStack);
    ItemMeta itemMeta = itemStack.getItemMeta();
    NBTCompound nbtCompound = nbtItem.getCompound(CucumberyTag.KEY_TMI);
    if (nbtCompound == null)
    {
      nbtCompound = nbtItem.addCompound(CucumberyTag.KEY_TMI);
    }
    if (itemMeta != null)
    {
      List<Component> lore = itemMeta.lore();
      if (lore != null)
      {
        // Legacy Support
        nbtItem.removeKey("Rarity");
        nbtCompound.setInteger("Rarity", ItemCategory.Rarity.getRarityFromValue(ItemLoreUtil.getItemRarityValue(lore)).getRarityNumber());
      }
    }
    nbtCompound.removeKey(CucumberyTag.TMI_VANILLA_TAGS);
    NBTCompound customTags = nbtCompound.getCompound(CucumberyTag.TMI_CUSTOM_TAGS);
    if (customTags != null)
    {
      itemStack.setItemMeta(nbtItem.getItem().getItemMeta());
      return;
    }
    NBTCompound vanillaTags = nbtCompound.addCompound(CucumberyTag.TMI_VANILLA_TAGS);
    Material type = itemStack.getType();
    vanillaTags.setBoolean("material_" + type.toString().toLowerCase(), true);
    {
    if (MaterialTags.ARROWS.isTagged(type))
    {
      vanillaTags.setBoolean("arrows", true);
    }
    if (MaterialTags.AXES.isTagged(type))
    {
      vanillaTags.setBoolean("axes", true);
    }
    if (MaterialTags.BEDS.isTagged(type))
    {
      vanillaTags.setBoolean("beds", true);
    }
    if (MaterialTags.BOOTS.isTagged(type))
    {
      vanillaTags.setBoolean("boots", true);
    }
    if (MaterialTags.BOWS.isTagged(type))
    {
      vanillaTags.setBoolean("bows", true);
    }
    if (MaterialTags.BUCKETS.isTagged(type))
    {
      vanillaTags.setBoolean("buckets", true);
    }
    if (MaterialTags.CHEST_EQUIPPABLE.isTagged(type))
    {
      vanillaTags.setBoolean("chest_equippable", true);
    }
    if (MaterialTags.CHESTPLATES.isTagged(type))
    {
      vanillaTags.setBoolean("chestplates", true);
    }
    if (MaterialTags.COALS.isTagged(type))
    {
      vanillaTags.setBoolean("coals", true);
    }
    if (MaterialTags.COBBLESTONE_WALLS.isTagged(type))
    {
      vanillaTags.setBoolean("cobblestone_walls", true);
    }
    if (MaterialTags.COBBLESTONES.isTagged(type))
    {
      vanillaTags.setBoolean("cobblestones", true);
    }
    if (MaterialTags.COLORABLE.isTagged(type))
    {
      vanillaTags.setBoolean("colorable", true);
    }
    if (MaterialTags.CONCRETE_POWDER.isTagged(type))
    {
      vanillaTags.setBoolean("concrete_powder", true);
    }
    if (MaterialTags.CONCRETES.isTagged(type))
    {
      vanillaTags.setBoolean("concretes", true);
    }
    if (MaterialTags.COOKED_FISH.isTagged(type))
    {
      vanillaTags.setBoolean("cooked_fish", true);
    }
    if (MaterialTags.CORAL.isTagged(type))
    {
      vanillaTags.setBoolean("coral", true);
    }
    if (MaterialTags.CORAL_BLOCKS.isTagged(type))
    {
      vanillaTags.setBoolean("coral_blocks", true);
    }
    if (MaterialTags.CORAL_FANS.isTagged(type))
    {
      vanillaTags.setBoolean("coral_fans", true);
    }
    if (MaterialTags.DOORS.isTagged(type))
    {
      vanillaTags.setBoolean("doors", true);
    }
    if (MaterialTags.DYES.isTagged(type))
    {
      vanillaTags.setBoolean("dyes", true);
    }
    if (MaterialTags.ENCHANTABLE.isTagged(type))
    {
      vanillaTags.setBoolean("enchantable", true);
    }
    if (MaterialTags.FENCE_GATES.isTagged(type))
    {
      vanillaTags.setBoolean("fence_gates", true);
    }
    if (MaterialTags.FENCES.isTagged(type))
    {
      vanillaTags.setBoolean("fences", true);
    }
    if (MaterialTags.FISH_BUCKETS.isTagged(type))
    {
      vanillaTags.setBoolean("fish_buckets", true);
    }
    if (MaterialTags.GLASS.isTagged(type))
    {
      vanillaTags.setBoolean("glass", true);
    }
    if (MaterialTags.GLASS_PANES.isTagged(type))
    {
      vanillaTags.setBoolean("glass_panes", true);
    }
    if (MaterialTags.GLAZED_TERRACOTTA.isTagged(type))
    {
      vanillaTags.setBoolean("glazed_terracotta", true);
    }
    if (MaterialTags.GOLDEN_APPLES.isTagged(type))
    {
      vanillaTags.setBoolean("golden_apples", true);
    }
    if (MaterialTags.HEAD_EQUIPPABLE.isTagged(type))
    {
      vanillaTags.setBoolean("head_equippable", true);
    }
    if (MaterialTags.HELMETS.isTagged(type))
    {
      vanillaTags.setBoolean("helmets", true);
    }
    if (MaterialTags.HOES.isTagged(type))
    {
      vanillaTags.setBoolean("hoes", true);
    }
    if (MaterialTags.HORSE_ARMORS.isTagged(type))
    {
      vanillaTags.setBoolean("horse_armors", true);
    }
    if (MaterialTags.LANTERNS.isTagged(type))
    {
      vanillaTags.setBoolean("lanterns", true);
    }
    if (MaterialTags.LEGGINGS.isTagged(type))
    {
      vanillaTags.setBoolean("leggings", true);
    }
    if (MaterialTags.MUSHROOM_BLOCKS.isTagged(type))
    {
      vanillaTags.setBoolean("mushroom_blocks", true);
    }
    if (MaterialTags.MUSHROOMS.isTagged(type))
    {
      vanillaTags.setBoolean("mushrooms", true);
    }
    if (MaterialTags.MUSIC_DISCS.isTagged(type))
    {
      vanillaTags.setBoolean("music_discs", true);
    }
    if (MaterialTags.ORES.isTagged(type))
    {
      vanillaTags.setBoolean("ores", true);
    }
    if (MaterialTags.PICKAXES.isTagged(type))
    {
      vanillaTags.setBoolean("pickaxes", true);
    }
    if (MaterialTags.PISTONS.isTagged(type))
    {
      vanillaTags.setBoolean("pistons", true);
    }
    if (MaterialTags.POTATOES.isTagged(type))
    {
      vanillaTags.setBoolean("potatoes", true);
    }
    if (MaterialTags.PRESSURE_PLATES.isTagged(type))
    {
      vanillaTags.setBoolean("pressure_plates", true);
    }
    if (MaterialTags.PRISMARINE.isTagged(type))
    {
      vanillaTags.setBoolean("prismarine", true);
    }
    if (MaterialTags.PRISMARINE_SLABS.isTagged(type))
    {
      vanillaTags.setBoolean("prismarine_slabs", true);
    }
    if (MaterialTags.PRISMARINE_STAIRS.isTagged(type))
    {
      vanillaTags.setBoolean("prismarine_stairs", true);
    }
    if (MaterialTags.PUMPKINS.isTagged(type))
    {
      vanillaTags.setBoolean("pumpkins", true);
    }
    if (MaterialTags.PURPUR.isTagged(type))
    {
      vanillaTags.setBoolean("purpur", true);
    }
    if (MaterialTags.QUARTZ_BLOCKS.isTagged(type))
    {
      vanillaTags.setBoolean("quartz_blocks", true);
    }
    if (MaterialTags.RAILS.isTagged(type))
    {
      vanillaTags.setBoolean("rails", true);
    }
    if (MaterialTags.RAW_FISH.isTagged(type))
    {
      vanillaTags.setBoolean("raw_fish", true);
    }
    if (MaterialTags.RAW_ORE_BLOCKS.isTagged(type))
    {
      vanillaTags.setBoolean("raw_ore_blocks", true);
    }
    if (MaterialTags.RAW_ORES.isTagged(type))
    {
      vanillaTags.setBoolean("raw_ores", true);
    }
    if (MaterialTags.RED_SANDSTONES.isTagged(type))
    {
      vanillaTags.setBoolean("red_sandstones", true);
    }
    if (MaterialTags.REDSTONE_TORCH.isTagged(type))
    {
      vanillaTags.setBoolean("redstone_torch", true);
    }
    if (MaterialTags.SANDSTONES.isTagged(type))
    {
      vanillaTags.setBoolean("sandstones", true);
    }
    if (MaterialTags.SHULKER_BOXES.isTagged(type))
    {
      vanillaTags.setBoolean("shulker_boxes", true);
    }
    if (MaterialTags.SHOVELS.isTagged(type))
    {
      vanillaTags.setBoolean("shovels", true);
    }
    if (MaterialTags.SIGNS.isTagged(type))
    {
      vanillaTags.setBoolean("signs", true);
    }
    if (MaterialTags.SOUL_TORCH.isTagged(type))
    {
      vanillaTags.setBoolean("soul_torch", true);
    }
    if (MaterialTags.SKULLS.isTagged(type))
    {
      vanillaTags.setBoolean("skulls", true);
    }
    if (MaterialTags.SPAWN_EGGS.isTagged(type))
    {
      vanillaTags.setBoolean("spawn_eggs", true);
    }
    if (MaterialTags.SPONGES.isTagged(type))
    {
      vanillaTags.setBoolean("sponges", true);
    }
    if (MaterialTags.STAINED_GLASS.isTagged(type))
    {
      vanillaTags.setBoolean("stained_glass", true);
    }
    if (MaterialTags.STAINED_GLASS_PANES.isTagged(type))
    {
      vanillaTags.setBoolean("stained_glass_panes", true);
    }
    if (MaterialTags.STAINED_TERRACOTTA.isTagged(type))
    {
      vanillaTags.setBoolean("stained_terracotta", true);
    }
    if (MaterialTags.SWORDS.isTagged(type))
    {
      vanillaTags.setBoolean("swords", true);
    }
    if (MaterialTags.TERRACOTTA.isTagged(type))
    {
      vanillaTags.setBoolean("terracotta", true);
    }
    if (MaterialTags.THROWABLE_PROJECTILES.isTagged(type))
    {
      vanillaTags.setBoolean("throwable_projectiles", true);
    }
    if (MaterialTags.TORCH.isTagged(type))
    {
      vanillaTags.setBoolean("torch", true);
    }
    if (MaterialTags.TORCHES.isTagged(type))
    {
      vanillaTags.setBoolean("torches", true);
    }
    if (MaterialTags.TRAPDOORS.isTagged(type))
    {
      vanillaTags.setBoolean("trapdoors", true);
    }
    if (MaterialTags.WOODEN_DOORS.isTagged(type))
    {
      vanillaTags.setBoolean("wooden_doors", true);
    }
    if (MaterialTags.WOODEN_FENCES.isTagged(type))
    {
      vanillaTags.setBoolean("wooden_fences", true);
    }
    if (MaterialTags.WOODEN_GATES.isTagged(type))
    {
      vanillaTags.setBoolean("wooden_gates", true);
    }
    if (MaterialTags.WOODEN_TRAPDOORS.isTagged(type))
    {
      vanillaTags.setBoolean("wooden_trapdoors", true);
    }
    if (Tag.ACACIA_LOGS.isTagged(type))
    {
      vanillaTags.setBoolean("acacia_logs", true);
    }
    if (Tag.ANVIL.isTagged(type))
    {
      vanillaTags.setBoolean("anvil", true);
    }
    if (Tag.AXOLOTL_TEMPT_ITEMS.isTagged(type))
    {
      vanillaTags.setBoolean("axolotl_tempt_items", true);
    }
    if (Tag.BEE_GROWABLES.isTagged(type))
    {
      vanillaTags.setBoolean("bee_growables", true);
    }
    if (Tag.BEEHIVES.isTagged(type))
    {
      vanillaTags.setBoolean("beehives", true);
    }
    if (Tag.BASE_STONE_NETHER.isTagged(type))
    {
      vanillaTags.setBoolean("base_stone_nethef", true);
    }
    if (Tag.BAMBOO_PLANTABLE_ON.isTagged(type))
    {
      vanillaTags.setBoolean("bamboo_plantable_on", true);
    }
    if (Tag.BANNERS.isTagged(type))
    {
      vanillaTags.setBoolean("banners", true);
    }
    if (Tag.BEACON_BASE_BLOCKS.isTagged(type))
    {
      vanillaTags.setBoolean("beacon_base_blocks", true);
    }
    if (Tag.BIRCH_LOGS.isTagged(type))
    {
      vanillaTags.setBoolean("birch_logs", true);
    }
    if (Tag.BUTTONS.isTagged(type))
    {
      vanillaTags.setBoolean("buttons", true);
    }
    if (Tag.CLIMBABLE.isTagged(type))
    {
      vanillaTags.setBoolean("climbable", true);
    }
    if (Tag.CRYSTAL_SOUND_BLOCKS.isTagged(type))
    {
      vanillaTags.setBoolean("crystal_sound_blocks", true);
    }
    if (Tag.CLUSTER_MAX_HARVESTABLES.isTagged(type))
    {
      vanillaTags.setBoolean("cluster_max_harvestables", true);
    }
    if (Tag.CAMPFIRES.isTagged(type))
    {
      vanillaTags.setBoolean("campfires", true);
    }
    if (Tag.CANDLE_CAKES.isTagged(type))
    {
      vanillaTags.setBoolean("candle_cakes", true);
    }
    if (Tag.CROPS.isTagged(type))
    {
      vanillaTags.setBoolean("crops", true);
    }
    if (Tag.CORALS.isTagged(type))
    {
      vanillaTags.setBoolean("corals", true);
    }
    if (Tag.CORAL_PLANTS.isTagged(type))
    {
      vanillaTags.setBoolean("coral_plants", true);
    }
    if (Tag.CORAL_BLOCKS.isTagged(type))
    {
      vanillaTags.setBoolean("coral_blocks", true);
    }
    if (Tag.CARPETS.isTagged(type))
    {
      vanillaTags.setBoolean("carpets", true);
    }
    if (Tag.COPPER_ORES.isTagged(type))
    {
      vanillaTags.setBoolean("copper_ores", true);
    }
    if (Tag.CAULDRONS.isTagged(type))
    {
      vanillaTags.setBoolean("cauldrons", true);
    }
    if (Tag.CRIMSON_STEMS.isTagged(type))
    {
      vanillaTags.setBoolean("crimson_stems", true);
    }
    if (Tag.CAVE_VINES.isTagged(type))
    {
      vanillaTags.setBoolean("cave_vines", true);
    }
    if (Tag.CANDLES.isTagged(type))
    {
      vanillaTags.setBoolean("candles", true);
    }
    if (Tag.COAL_ORES.isTagged(type))
    {
      vanillaTags.setBoolean("coal_ores", true);
    }
    if (Tag.DOORS.isTagged(type))
    {
      vanillaTags.setBoolean("doors", true);
    }
    if (Tag.DIAMOND_ORES.isTagged(type))
    {
      vanillaTags.setBoolean("dimond_ores", true);
    }
    if (Tag.DARK_OAK_LOGS.isTagged(type))
    {
      vanillaTags.setBoolean("dark_oak_logs", true);
    }
    if (Tag.DEEPSLATE_ORE_REPLACEABLES.isTagged(type))
    {
      vanillaTags.setBoolean("deepslate_ore_replaceables", true);
    }
    if (Tag.DRIPSTONE_REPLACEABLE.isTagged(type))
    {
      vanillaTags.setBoolean("dripstone_replaceable", true);
    }
    if (Tag.DRAGON_IMMUNE.isTagged(type))
    {
      vanillaTags.setBoolean("dragon_immune", true);
    }
    if (Tag.DIRT.isTagged(type))
    {
      vanillaTags.setBoolean("dirt", true);
    }
    if (Tag.ENDERMAN_HOLDABLE.isTagged(type))
    {
      vanillaTags.setBoolean("enderman_holdable", true);
    }
    if (Tag.EMERALD_ORES.isTagged(type))
    {
      vanillaTags.setBoolean("emerald_ores", true);
    }
    if (Tag.FENCE_GATES.isTagged(type))
    {
      vanillaTags.setBoolean("fence_gates", true);
    }
    if (Tag.FENCES.isTagged(type))
    {
      vanillaTags.setBoolean("fences", true);
    }
    if (Tag.FREEZE_IMMUNE_WEARABLES.isTagged(type))
    {
      vanillaTags.setBoolean("freeze_immune_wearables", true);
    }
    if (Tag.FIRE.isTagged(type))
    {
      vanillaTags.setBoolean("fire", true);
    }
    if (Tag.FOX_FOOD.isTagged(type))
    {
      vanillaTags.setBoolean("fox_food", true);
    }
    if (Tag.FEATURES_CANNOT_REPLACE.isTagged(type))
    {
      vanillaTags.setBoolean("features_cannot_replace", true);
    }
    if (Tag.FLOWER_POTS.isTagged(type))
    {
      vanillaTags.setBoolean("flower_pots", true);
    }
    if (Tag.FLOWERS.isTagged(type))
    {
      vanillaTags.setBoolean("flowers", true);
    }
    if (Tag.GEODE_INVALID_BLOCKS.isTagged(type))
    {
      vanillaTags.setBoolean("geode_invalid_blocks", true);
    }
    if (Tag.GUARDED_BY_PIGLINS.isTagged(type))
    {
      vanillaTags.setBoolean("guarded_by_piglins", true);
    }
    if (Tag.GOLD_ORES.isTagged(type))
    {
      vanillaTags.setBoolean("gold_ores", true);
    }
    if (Tag.HOGLIN_REPELLENTS.isTagged(type))
    {
      vanillaTags.setBoolean("hoglin_repellents", true);
    }
    if (Tag.JUNGLE_LOGS.isTagged(type))
    {
      vanillaTags.setBoolean("jungle_logs", true);
    }
    if (Tag.LOGS.isTagged(type))
    {
      vanillaTags.setBoolean("logs", true);
    }
    if (Tag.LOGS_THAT_BURN.isTagged(type))
    {
      vanillaTags.setBoolean("logs_that_bern", true);
    }
    if (Tag.LAPIS_ORES.isTagged(type))
    {
      vanillaTags.setBoolean("lapis_ores", true);
    }
    if (Tag.LUSH_GROUND_REPLACEABLE.isTagged(type))
    {
      vanillaTags.setBoolean("lush_ground_replaceable", true);
    }
    if (Tag.LEAVES.isTagged(type))
    {
      vanillaTags.setBoolean("leaves", true);
    }
    if (Tag.LAVA_POOL_STONE_CANNOT_REPLACE.isTagged(type))
    {
      vanillaTags.setBoolean("lava_pool_stone_cannot_replace", true);
    }
    if (Tag.MINEABLE_PICKAXE.isTagged(type))
    {
      vanillaTags.setBoolean("mineable_pickaxe", true);
    }
    if (Tag.MINEABLE_SHOVEL.isTagged(type))
    {
      vanillaTags.setBoolean("mineable_shovel", true);
    }
    if (Tag.MINEABLE_AXE.isTagged(type))
    {
      vanillaTags.setBoolean("mineable_axe", true);
    }
    if (Tag.MUSHROOM_GROW_BLOCK.isTagged(type))
    {
      vanillaTags.setBoolean("mushroom_grow_block", true);
    }
    if (Tag.MINEABLE_HOE.isTagged(type))
    {
      vanillaTags.setBoolean("mineable_hoe", true);
    }
    if (Tag.MOSS_REPLACEABLE.isTagged(type))
    {
      vanillaTags.setBoolean("moss_replaceable", true);
    }
    if (Tag.NON_FLAMMABLE_WOOD.isTagged(type))
    {
      vanillaTags.setBoolean("non_flammable_wood", true);
    }
    if (Tag.NYLIUM.isTagged(type))
    {
      vanillaTags.setBoolean("nylium", true);
    }
    if (Tag.NEEDS_DIAMOND_TOOL.isTagged(type))
    {
      vanillaTags.setBoolean("needs_diamond_tool", true);
    }
    if (Tag.NEEDS_STONE_TOOL.isTagged(type))
    {
      vanillaTags.setBoolean("needs_stone_tool", true);
    }
    if (Tag.NEEDS_IRON_TOOL.isTagged(type))
    {
      vanillaTags.setBoolean("needs_iron_tool", true);
    }
    if (Tag.OAK_LOGS.isTagged(type))
    {
      vanillaTags.setBoolean("oak_logs", true);
    }
    if (Tag.OCCLUDES_VIBRATION_SIGNALS.isTagged(type))
    {
      vanillaTags.setBoolean("occludes_vibration_signals", true);
    }
    if (Tag.PIGLIN_REPELLENTS.isTagged(type))
    {
      vanillaTags.setBoolean("piglon_repellents", true);
    }
    if (Tag.PIGLIN_FOOD.isTagged(type))
    {
      vanillaTags.setBoolean("piglin_food", true);
    }
    if (Tag.PLANKS.isTagged(type))
    {
      vanillaTags.setBoolean("planks", true);
    }
    if (Tag.PORTALS.isTagged(type))
    {
      vanillaTags.setBoolean("portals", true);
    }
    if (Tag.PRESSURE_PLATES.isTagged(type))
    {
      vanillaTags.setBoolean("pressure_plates", true);
    }
    if (Tag.PREVENT_MOB_SPAWNING_INSIDE.isTagged(type))
    {
      vanillaTags.setBoolean("prevent_mob_spawning_inside", true);
    }
    if (Tag.RAILS.isTagged(type))
    {
      vanillaTags.setBoolean("rails", true);
    }
    if (Tag.REDSTONE_ORES.isTagged(type))
    {
      vanillaTags.setBoolean("redstone_ores", true);
    }
    if (Tag.STONE_PRESSURE_PLATES.isTagged(type))
    {
      vanillaTags.setBoolean("stone_pressure_plates", true);
    }
    if (Tag.SMALL_FLOWERS.isTagged(type))
    {
      vanillaTags.setBoolean("small_flowers", true);
    }
    if (Tag.STAIRS.isTagged(type))
    {
      vanillaTags.setBoolean("stairs", true);
    }
    if (Tag.STONE_BRICKS.isTagged(type))
    {
      vanillaTags.setBoolean("stone_bricks", true);
    }
    if (Tag.SOUL_SPEED_BLOCKS.isTagged(type))
    {
      vanillaTags.setBoolean("soul_speed_blocks", true);
    }
    if (Tag.SPRUCE_LOGS.isTagged(type))
    {
      vanillaTags.setBoolean("spruce_logs", true);
    }
    if (Tag.SMALL_DRIPLEAF_PLACEABLE.isTagged(type))
    {
      vanillaTags.setBoolean("small_dripleaf_placeable", true);
    }
    if (Tag.SOUL_FIRE_BASE_BLOCKS.isTagged(type))
    {
      vanillaTags.setBoolean("soul_fire_base_blocks", true);
    }
    if (Tag.SLABS.isTagged(type))
    {
      vanillaTags.setBoolean("slabs", true);
    }
    if (Tag.SHULKER_BOXES.isTagged(type))
    {
      vanillaTags.setBoolean("shulker_boxes", true);
    }
    if (Tag.SIGNS.isTagged(type))
    {
      vanillaTags.setBoolean("signs", true);
    }
    if (Tag.STANDING_SIGNS.isTagged(type))
    {
      vanillaTags.setBoolean("standing_signs", true);
    }
    if (Tag.SNOW.isTagged(type))
    {
      vanillaTags.setBoolean("snow", true);
    }
    if (Tag.SAND.isTagged(type))
    {
      vanillaTags.setBoolean("sand", true);
    }
    if (Tag.SAPLINGS.isTagged(type))
    {
      vanillaTags.setBoolean("saplings", true);
    }
    if (Tag.STONE_ORE_REPLACEABLES.isTagged(type))
    {
      vanillaTags.setBoolean("stone_ore_replaceables", true);
    }
    if (Tag.STRIDER_WARM_BLOCKS.isTagged(type))
    {
      vanillaTags.setBoolean("strider_warm_blocks", true);
    }
    if (Tag.TRAPDOORS.isTagged(type))
    {
      vanillaTags.setBoolean("trapdoors", true);
    }
    if (Tag.TALL_FLOWERS.isTagged(type))
    {
      vanillaTags.setBoolean("tall_flowers", true);
    }
    if (Tag.UNDERWATER_BONEMEALS.isTagged(type))
    {
      vanillaTags.setBoolean("underwater_bonemeals", true);
    }
    if (Tag.UNSTABLE_BOTTOM_CENTER.isTagged(type))
    {
      vanillaTags.setBoolean("unstable_bottom_center", true);
    }
    if (Tag.VALID_SPAWN.isTagged(type))
    {
      vanillaTags.setBoolean("valid_spawn", true);
    }
    if (Tag.WOOL.isTagged(type))
    {
      vanillaTags.setBoolean("wool", true);
    }
    if (Tag.WALL_POST_OVERRIDE.isTagged(type))
    {
      vanillaTags.setBoolean("wall_post_override", true);
    }
    if (Tag.WALLS.isTagged(type))
    {
      vanillaTags.setBoolean("walls", true);
    }
    if (Tag.WALL_CORALS.isTagged(type))
    {
      vanillaTags.setBoolean("wall_corals", true);
    }
    if (Tag.WOODEN_STAIRS.isTagged(type))
    {
      vanillaTags.setBoolean("wooden_stairs", true);
    }
    if (Tag.WOODEN_PRESSURE_PLATES.isTagged(type))
    {
      vanillaTags.setBoolean("wooden_pressure_plates", true);
    }
    if (Tag.WARPED_STEMS.isTagged(type))
    {
      vanillaTags.setBoolean("warped_stems", true);
    }
    if (Tag.WALL_SIGNS.isTagged(type))
    {
      vanillaTags.setBoolean("wall_signs", true);
    }
    if (Tag.WOODEN_TRAPDOORS.isTagged(type))
    {
      vanillaTags.setBoolean("wooden_trapdoors", true);
    }
    if (Tag.WOODEN_BUTTONS.isTagged(type))
    {
      vanillaTags.setBoolean("wooden_buttons", true);
    }
    if (Tag.WOODEN_FENCES.isTagged(type))
    {
      vanillaTags.setBoolean("wooden_fences", true);
    }
    if (Tag.WITHER_SUMMON_BASE_BLOCKS.isTagged(type))
    {
      vanillaTags.setBoolean("wither_summon_base_blocks", true);
    }
    if (Tag.WART_BLOCKS.isTagged(type))
    {
      vanillaTags.setBoolean("wart_blocks", true);
    }
    if (Tag.WOODEN_SLABS.isTagged(type))
    {
      vanillaTags.setBoolean("wooden_slabs", true);
    }
    if (Tag.WOODEN_DOORS.isTagged(type))
    {
      vanillaTags.setBoolean("wooden_doors", true);
    }
    if (Tag.WITHER_IMMUNE.isTagged(type))
    {
      vanillaTags.setBoolean("wither_immune", true);
    }
  }
    if (type.isBlock())
    {
      vanillaTags.setBoolean("blocks", true);
    }
    else
    {
      vanillaTags.setBoolean("items", true);
    }
    if (itemMeta instanceof BlockStateMeta blockStateMeta)
    {
      BlockState blockState = blockStateMeta.getBlockState();
      if (blockState instanceof Container container)
      {
        vanillaTags.setBoolean("container", true);
        boolean inventoryEmpty = true;
        try
        {
          inventoryEmpty = Method.inventoryEmpty(container.getInventory());
        }
        catch (Exception ignored)
        {

        }
        vanillaTags.setBoolean("container_empty", inventoryEmpty);
      }
    }
    if (ItemStackUtil.isEdible(type))
    {
      vanillaTags.setBoolean("foods", true);
    }
    double compostChance = ItemStackUtil.getCompostChance(type);
    if (compostChance > 0)
    {
      vanillaTags.setBoolean("compostable", true);
      vanillaTags.setDouble("compostChance", compostChance);
    }
    if (type.isFuel())
    {
      vanillaTags.setBoolean("fuel", true);
    }
    if (type.getMaxDurability() > 0)
    {
      vanillaTags.setBoolean("durable", true);
    }
    itemStack.setItemMeta(nbtItem.getItem().getItemMeta());
  }
}

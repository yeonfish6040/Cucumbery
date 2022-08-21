package com.jho5245.cucumbery.util.storage.no_groups;

import com.jho5245.cucumbery.util.no_groups.Method;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlockDataInfo
{
  static Pattern pattern = Pattern.compile("([a-z0-9]+)=[a-z0-9]+");

  @Nullable
  public static String[] getBlockDataKeys(@NotNull Material material)
  {
    if (!material.isAir() && material.isItem())
    {
      ItemStack itemStack = new ItemStack(material);
      ItemMeta itemMeta = itemStack.getItemMeta();
      if (material.isBlock() && itemMeta instanceof BlockDataMeta blockDataMeta)
      {
        BlockData blockData = blockDataMeta.getBlockData(material);
        List<String> keys = new ArrayList<>();
        Matcher matcher = pattern.matcher(blockData.getAsString());
        while (matcher.find())
        {
          keys.add(matcher.group(1));
        }
        if (!keys.isEmpty())
        {
          return Method.listToArray(keys);
        }
      }
    }
//		return switch (material)
//						{
//							case ANVIL, CHIPPED_ANVIL, DAMAGED_ANVIL, BLACK_WALL_BANNER, BLUE_WALL_BANNER, BROWN_WALL_BANNER, CYAN_WALL_BANNER, GRAY_WALL_BANNER, GREEN_WALL_BANNER, LIGHT_BLUE_WALL_BANNER, LIGHT_GRAY_WALL_BANNER, LIME_WALL_BANNER, MAGENTA_WALL_BANNER, ORANGE_WALL_BANNER, PINK_WALL_BANNER, PURPLE_WALL_BANNER, RED_WALL_BANNER, WHITE_WALL_BANNER, YELLOW_WALL_BANNER, CARVED_PUMPKIN, END_ROD, BLACK_GLAZED_TERRACOTTA, BLUE_GLAZED_TERRACOTTA, BROWN_GLAZED_TERRACOTTA, CYAN_GLAZED_TERRACOTTA, GRAY_GLAZED_TERRACOTTA, GREEN_GLAZED_TERRACOTTA, LIGHT_BLUE_GLAZED_TERRACOTTA, LIGHT_GRAY_GLAZED_TERRACOTTA, LIME_GLAZED_TERRACOTTA, MAGENTA_GLAZED_TERRACOTTA, ORANGE_GLAZED_TERRACOTTA, PINK_GLAZED_TERRACOTTA, PURPLE_GLAZED_TERRACOTTA, RED_GLAZED_TERRACOTTA, WHITE_GLAZED_TERRACOTTA, YELLOW_GLAZED_TERRACOTTA, JACK_O_LANTERN, LOOM, CREEPER_WALL_HEAD, SKELETON_WALL_SKULL, WITHER_SKELETON_WALL_SKULL, DRAGON_WALL_HEAD, PLAYER_WALL_HEAD, ZOMBIE_WALL_HEAD, SHULKER_BOX, BLACK_SHULKER_BOX, BLUE_SHULKER_BOX, BROWN_SHULKER_BOX, CYAN_SHULKER_BOX, GRAY_SHULKER_BOX, GREEN_SHULKER_BOX, LIGHT_BLUE_SHULKER_BOX, LIGHT_GRAY_SHULKER_BOX, LIME_SHULKER_BOX, MAGENTA_SHULKER_BOX, ORANGE_SHULKER_BOX, PINK_SHULKER_BOX, PURPLE_SHULKER_BOX, RED_SHULKER_BOX, WHITE_SHULKER_BOX, YELLOW_SHULKER_BOX, STONECUTTER, TORCH, SOUL_TORCH, WALL_TORCH, SOUL_WALL_TORCH -> new String[]{"facing"};
//							case BAMBOO, BAMBOO_SAPLING -> new String[]{"age", "leaves", "stage"};
//							case BLACK_BANNER, BLUE_BANNER, BROWN_BANNER, CYAN_BANNER, GRAY_BANNER, GREEN_BANNER, LIGHT_BLUE_BANNER, LIGHT_GRAY_BANNER, LIME_BANNER, MAGENTA_BANNER, ORANGE_BANNER, PINK_BANNER, PURPLE_BANNER, RED_BANNER, WHITE_BANNER, YELLOW_BANNER, SKELETON_SKULL, WITHER_SKELETON_SKULL, CREEPER_HEAD, DRAGON_HEAD, PLAYER_HEAD, ZOMBIE_HEAD -> new String[]{"facing", "rotation"};
//							case BARREL -> new String[]{"north", "open"};
//							case BLACK_BED, BLUE_BED, BROWN_BED, CYAN_BED, GRAY_BED, GREEN_BED, LIGHT_BLUE_BED, LIGHT_GRAY_BED, LIME_BED, MAGENTA_BED, ORANGE_BED, PINK_BED, PURPLE_BED, RED_BED, WHITE_BED, YELLOW_BED -> new String[]{"facing", "occupied", "part"};
//							case BEEHIVE, BEE_NEST -> new String[]{"facing", "honey_level"};
//							case BEETROOTS, BEETROOT_SEEDS, CACTUS, CARROT, CARROTS, CHORUS_FLOWER, FROSTED_ICE, KELP, KELP_PLANT, NETHER_WART, POTATO, POTATOES, SUGAR_CANE, SWEET_BERRIES, SWEET_BERRY_BUSH, WHEAT_SEEDS -> new String[]{"age"};
//							case BELL -> new String[]{"attachment", "facing", "powered"};
//							case BLAST_FURNACE -> new String[]{"facing", "lit"};
//							case BONE_BLOCK, HAY_BLOCK, ACACIA_LOG, BIRCH_LOG, DARK_OAK_LOG, JUNGLE_LOG, OAK_LOG, SPRUCE_LOG, STRIPPED_ACACIA_LOG, STRIPPED_BIRCH_LOG, STRIPPED_DARK_OAK_LOG, STRIPPED_JUNGLE_LOG, STRIPPED_OAK_LOG, STRIPPED_SPRUCE_LOG, CRIMSON_STEM, WARPED_STEM, NETHER_PORTAL, PURPUR_PILLAR, QUARTZ_PILLAR, ACACIA_WOOD, BIRCH_WOOD, DARK_OAK_WOOD, JUNGLE_WOOD, OAK_WOOD, SPRUCE_WOOD, STRIPPED_ACACIA_WOOD, STRIPPED_BIRCH_WOOD, STRIPPED_DARK_OAK_WOOD, STRIPPED_JUNGLE_WOOD, STRIPPED_OAK_WOOD, STRIPPED_SPRUCE_WOOD, CRIMSON_HYPHAE, STRIPPED_CRIMSON_HYPHAE, STRIPPED_WARPED_HYPHAE, WARPED_HYPHAE -> new String[]{"axis"};
//							case BREWING_STAND -> new String[]{"has_bottle_0", "has_bottle_1", "has_bottle_2"};
//							case BUBBLE_COLUMN -> new String[]{"drag"};
//							case ACACIA_BUTTON, BIRCH_BUTTON, CRIMSON_BUTTON, DARK_OAK_BUTTON, JUNGLE_BUTTON, OAK_BUTTON, POLISHED_BLACKSTONE_BUTTON, SPRUCE_BUTTON, STONE_BUTTON, WARPED_BUTTON -> new String[]{"face", "facing", "powered"};
//							case CAMPFIRE, SOUL_CAMPFIRE -> new String[]{"facing", "lit", "signal_fire", "waterlogged"};
//							case CAKE -> new String[]{"bites"};
//							case CAULDRON, COMPOSTER -> new String[]{"level"};
//							case CHAIN -> new String[]{"waterlogged", "axis"};
//							case CHEST, TRAPPED_CHEST -> new String[]{"facing", "type", "waterlogged"};
//							case ENDER_CHEST -> new String[]{"facing", "waterlogged"};
//							case CHORUS_FRUIT, BROWN_MUSHROOM_BLOCK, RED_MUSHROOM_BLOCK, MUSHROOM_STEW -> new String[]{"down", "east", "north", "south", "up", "west"};
//							case COCOA, COCOA_BEANS -> new String[]{"age", "facing"};
//							case COMMAND_BLOCK, CHAIN_COMMAND_BLOCK, REPEATING_COMMAND_BLOCK -> new String[]{"conditional", "facing"};
//							case CONDUIT -> new String[]{"waterlogged"};
//							case DAYLIGHT_DETECTOR -> new String[]{"inverted", "power"};
//							case DISPENSER, DROPPER -> new String[]{"facing", "triggered"};
//							case ACACIA_DOOR, BIRCH_DOOR, CRIMSON_DOOR, DARK_OAK_DOOR, IRON_DOOR, JUNGLE_DOOR, OAK_DOOR, SPRUCE_DOOR, WARPED_DOOR -> new String[]{"facing", "half", "hinge", "open", "powered"};
//							case END_PORTAL_FRAME -> new String[]{"eye", "facing"};
//							case FARMLAND -> new String[]{"moisture"};
//							case ACACIA_FENCE, BIRCH_FENCE, CRIMSON_FENCE, DARK_OAK_FENCE, JUNGLE_FENCE, NETHER_BRICK_FENCE, OAK_FENCE, SPRUCE_FENCE, WARPED_FENCE, GLASS_PANE, BLACK_STAINED_GLASS_PANE, BLUE_STAINED_GLASS_PANE, BROWN_STAINED_GLASS_PANE, CYAN_STAINED_GLASS_PANE, GRAY_STAINED_GLASS_PANE, GREEN_STAINED_GLASS_PANE, LIGHT_BLUE_STAINED_GLASS_PANE, LIGHT_GRAY_STAINED_GLASS_PANE, LIME_STAINED_GLASS_PANE, MAGENTA_STAINED_GLASS_PANE, ORANGE_STAINED_GLASS_PANE, PINK_STAINED_GLASS_PANE, PURPLE_STAINED_GLASS_PANE, RED_STAINED_GLASS_PANE, WHITE_STAINED_GLASS_PANE, YELLOW_STAINED_GLASS_PANE, IRON_BARS -> new String[]{"east", "north", "south", "waterlogged", "west"};
//							case ACACIA_FENCE_GATE, BIRCH_FENCE_GATE, CRIMSON_FENCE_GATE, DARK_OAK_FENCE_GATE, JUNGLE_FENCE_GATE, OAK_FENCE_GATE, SPRUCE_FENCE_GATE, WARPED_FENCE_GATE -> new String[]{"facing", "in_wall", "open", "powered"};
//							case FIRE, SOUL_FIRE -> new String[]{"age", "east", "north", "south", "up", "west"};
//							case FURNACE -> new String[]{"facing", "lit"};
//							case GRASS_BLOCK, MYCELIUM, PODZOL -> new String[]{"snowy"};
//							case GRINDSTONE -> new String[]{"face", "facing"};
//							case HOPPER -> new String[]{"enabled", "facing"};
//							case JIGSAW -> new String[]{"orientation"};
//							case JUKEBOX -> new String[]{"has_record"};
//							case LADDER -> new String[]{"facing", "waterlogged"};
//							case LANTERN, SOUL_LANTERN -> new String[]{"hanging", "waterlogged"};
//							case SUNFLOWER, LILAC, ROSE_BUSH, PEONY -> new String[]{"half"};
//							case LAVA -> new String[]{"level"};
//							case ACACIA_LEAVES, BIRCH_LEAVES, DARK_OAK_LEAVES, JUNGLE_LEAVES, OAK_LEAVES, SPRUCE_LEAVES -> new String[]{"distance", "persistent"};
//							case LECTERN -> new String[]{"facing", "has_book", "powered"};
//							case LEVER -> new String[]{"face", "facing", "powered"};
//							case NOTE_BLOCK -> new String[]{"instrument", "note", "powered"};
//							case OBSERVER -> new String[]{"facing", "powered"};
//							case PISTON, STICKY_PISTON -> new String[]{"extended", "facing"};
//							case MOVING_PISTON -> new String[]{"facing", "type"};
//							case PISTON_HEAD -> new String[]{"facing", "short", "type"};
//							case ACACIA_PRESSURE_PLATE, BIRCH_PRESSURE_PLATE, CRIMSON_PRESSURE_PLATE, DARK_OAK_PRESSURE_PLATE, JUNGLE_PRESSURE_PLATE, OAK_PRESSURE_PLATE, POLISHED_BLACKSTONE_PRESSURE_PLATE, SPRUCE_PRESSURE_PLATE, STONE_PRESSURE_PLATE, WARPED_PRESSURE_PLATE -> new String[]{"powered"};
//							case LIGHT_WEIGHTED_PRESSURE_PLATE, HEAVY_WEIGHTED_PRESSURE_PLATE -> new String[]{"powered", "power"};
//							case MELON_STEM, PUMPKIN_STEM, MELON_SEEDS, PUMPKIN_SEEDS -> new String[]{"age", "facing"};
//							case RAIL -> new String[]{"shape"};
//							case ACTIVATOR_RAIL, DETECTOR_RAIL, POWERED_RAIL -> new String[]{"powered", "shape"};
//							case COMPARATOR -> new String[]{"facing", "mode", "powered"};
//							case REDSTONE_WIRE, REDSTONE -> new String[]{"east", "north", "power", "south", "west"};
//							case REDSTONE_LAMP, REDSTONE_ORE -> new String[]{"lit"};
//							case REPEATER -> new String[]{"delay", "facing", "locked", "powered"};
//							case REDSTONE_TORCH, REDSTONE_WALL_TORCH, SMOKER -> new String[]{"facing", "lit"};
//							case RESPAWN_ANCHOR -> new String[]{"charges"};
//							case ACACIA_SAPLING, BIRCH_SAPLING, DARK_OAK_SAPLING, JUNGLE_SAPLING, OAK_SAPLING, SPRUCE_SAPLING -> new String[]{"stage"};
//							case SCAFFOLDING -> new String[]{"bottom", "distance", "waterlogged"};
//							case SEA_PICKLE -> new String[]{"pickles", "waterlogged"};
//							case ACACIA_SIGN, BIRCH_SIGN, CRIMSON_SIGN, DARK_OAK_SIGN, JUNGLE_SIGN, OAK_SIGN, SPRUCE_SIGN, WARPED_SIGN -> new String[]{"rotation", "facing", "waterlogged"};
//							case ACACIA_WALL_SIGN, BIRCH_WALL_SIGN, CRIMSON_WALL_SIGN, DARK_OAK_WALL_SIGN, JUNGLE_WALL_SIGN, OAK_WALL_SIGN, SPRUCE_WALL_SIGN, WARPED_WALL_SIGN -> new String[]{"facing", "waterlogged"};
//							case ACACIA_SLAB, ANDESITE_SLAB, BIRCH_SLAB, BLACKSTONE_SLAB, BRICK_SLAB, COBBLESTONE_SLAB, CRIMSON_SLAB, CUT_RED_SANDSTONE_SLAB, CUT_SANDSTONE_SLAB, DARK_OAK_SLAB, DARK_PRISMARINE_SLAB, DIORITE_SLAB, END_STONE_BRICK_SLAB, GRANITE_SLAB, JUNGLE_SLAB, MOSSY_COBBLESTONE_SLAB, MOSSY_STONE_BRICK_SLAB, NETHER_BRICK_SLAB, OAK_SLAB, PETRIFIED_OAK_SLAB, POLISHED_ANDESITE_SLAB, POLISHED_BLACKSTONE_BRICK_SLAB, POLISHED_BLACKSTONE_SLAB, POLISHED_DIORITE_SLAB, POLISHED_GRANITE_SLAB, PRISMARINE_BRICK_SLAB, PRISMARINE_SLAB, PURPUR_SLAB, QUARTZ_SLAB, RED_NETHER_BRICK_SLAB, RED_SANDSTONE_SLAB, SANDSTONE_SLAB, SMOOTH_QUARTZ_SLAB, SMOOTH_RED_SANDSTONE_SLAB, SMOOTH_SANDSTONE_SLAB, SMOOTH_STONE_SLAB, SPRUCE_SLAB, STONE_BRICK_SLAB, STONE_SLAB, WARPED_SLAB -> new String[]{"type", "waterlogged"};
//							case SNOW -> new String[]{"layers"};
//							case ACACIA_STAIRS, ANDESITE_STAIRS, BIRCH_STAIRS, BLACKSTONE_STAIRS, BRICK_STAIRS, COBBLESTONE_STAIRS, CRIMSON_STAIRS, DARK_OAK_STAIRS, DARK_PRISMARINE_STAIRS, DIORITE_STAIRS, END_STONE_BRICK_STAIRS, GRANITE_STAIRS, JUNGLE_STAIRS, MOSSY_COBBLESTONE_STAIRS, MOSSY_STONE_BRICK_STAIRS, NETHER_BRICK_STAIRS, OAK_STAIRS, POLISHED_ANDESITE_STAIRS, POLISHED_BLACKSTONE_BRICK_STAIRS, POLISHED_BLACKSTONE_STAIRS, POLISHED_DIORITE_STAIRS, POLISHED_GRANITE_STAIRS, PRISMARINE_BRICK_STAIRS, PRISMARINE_STAIRS, PURPUR_STAIRS, QUARTZ_STAIRS, RED_NETHER_BRICK_STAIRS, RED_SANDSTONE_STAIRS, SANDSTONE_STAIRS, SMOOTH_QUARTZ_STAIRS, SMOOTH_RED_SANDSTONE_STAIRS, SMOOTH_SANDSTONE_STAIRS, SPRUCE_STAIRS, STONE_BRICK_STAIRS, STONE_STAIRS, WARPED_STAIRS -> new String[]{"facing", "half", "shape", "waterlogged"};
//							case STRUCTURE_BLOCK -> new String[]{"mode"};
//							case TALL_GRASS, TALL_SEAGRASS, LARGE_FERN -> new String[]{"half"};
//							case TNT -> new String[]{"unstable"};
//							case ACACIA_TRAPDOOR, BIRCH_TRAPDOOR, CRIMSON_TRAPDOOR, DARK_OAK_TRAPDOOR, IRON_TRAPDOOR, JUNGLE_TRAPDOOR, OAK_TRAPDOOR, SPRUCE_TRAPDOOR, WARPED_TRAPDOOR -> new String[]{"facing", "half", "open", "powered", "waterlogged"};
//							case TRIPWIRE, STRING -> new String[]{"attached", "disarmed", "east", "north", "powered", "south", "west"};
//							case TRIPWIRE_HOOK -> new String[]{"attached", "facing", "powered"};
//							case TURTLE_EGG -> new String[]{"eggs", "hatch"};
//							case VINE -> new String[]{"east", "north", "south", "up", "west"};
//							case ANDESITE_WALL, BLACKSTONE_WALL, BRICK_WALL, COBBLESTONE_WALL, DIORITE_WALL, END_STONE_BRICK_WALL, GRANITE_WALL, MOSSY_COBBLESTONE_WALL, MOSSY_STONE_BRICK_WALL, NETHER_BRICK_WALL, POLISHED_BLACKSTONE_BRICK_WALL, POLISHED_BLACKSTONE_WALL, PRISMARINE_WALL, RED_NETHER_BRICK_WALL, RED_SANDSTONE_WALL, SANDSTONE_WALL, STONE_BRICK_WALL -> new String[]{"east", "north", "south", "up", "waterlogged", "west"};
//							case WATER -> new String[]{"level"};
//							default -> null;
//						};
    return null;
  }

  @Nullable
  public static String[] blockDataKeys(@NotNull Material material)
  {
    List<String> keys = new ArrayList<>();
    ItemStack itemStack = new ItemStack(material);
    if (!ItemStackUtil.itemExists(itemStack))
    {
      return null;
    }
    ItemMeta itemMeta = itemStack.getItemMeta();
    if (!(itemMeta instanceof BlockDataMeta blockDataMeta))
    {
      return null;
    }
    if (blockDataMeta instanceof Ageable ageable)
    {
      keys.add("age");
    }
    if (blockDataMeta instanceof AnaloguePowerable)
    {
      keys.add("power");
    }
    if (!keys.isEmpty())
    {
      return Method.listToArray(keys);
    }
    return null;
  }

  @Nullable
  public static String[] getBlockDataValues(@NotNull Material material, @NotNull String key)
  {
    switch (material)
    {
      case ANVIL:
      case CHIPPED_ANVIL:
      case DAMAGED_ANVIL:
      case BLACK_WALL_BANNER:
      case BLUE_WALL_BANNER:
      case BROWN_WALL_BANNER:
      case CYAN_WALL_BANNER:
      case GRAY_WALL_BANNER:
      case GREEN_WALL_BANNER:
      case LIGHT_BLUE_WALL_BANNER:
      case LIGHT_GRAY_WALL_BANNER:
      case LIME_WALL_BANNER:
      case MAGENTA_WALL_BANNER:
      case ORANGE_WALL_BANNER:
      case PINK_WALL_BANNER:
      case PURPLE_WALL_BANNER:
      case RED_WALL_BANNER:
      case WHITE_WALL_BANNER:
      case YELLOW_WALL_BANNER:
      case CARVED_PUMPKIN:
      case BLACK_GLAZED_TERRACOTTA:
      case BLUE_GLAZED_TERRACOTTA:
      case BROWN_GLAZED_TERRACOTTA:
      case CYAN_GLAZED_TERRACOTTA:
      case GRAY_GLAZED_TERRACOTTA:
      case GREEN_GLAZED_TERRACOTTA:
      case LIGHT_BLUE_GLAZED_TERRACOTTA:
      case LIGHT_GRAY_GLAZED_TERRACOTTA:
      case LIME_GLAZED_TERRACOTTA:
      case MAGENTA_GLAZED_TERRACOTTA:
      case ORANGE_GLAZED_TERRACOTTA:
      case PINK_GLAZED_TERRACOTTA:
      case PURPLE_GLAZED_TERRACOTTA:
      case RED_GLAZED_TERRACOTTA:
      case WHITE_GLAZED_TERRACOTTA:
      case YELLOW_GLAZED_TERRACOTTA:
      case JACK_O_LANTERN:
      case LOOM:
      case SKELETON_WALL_SKULL:
      case WITHER_SKELETON_WALL_SKULL:
      case CREEPER_WALL_HEAD:
      case DRAGON_WALL_HEAD:
      case PLAYER_WALL_HEAD:
      case ZOMBIE_WALL_HEAD:
      case TORCH:
      case SOUL_TORCH:
      case SOUL_WALL_TORCH:
      case WALL_TORCH:
      case STONECUTTER:
        if (key.equals("facing"))
        {
          return FACING_4;
        }
        break;
      case REDSTONE_TORCH:
      case REDSTONE_WALL_TORCH:
        switch (key)
        {
          case "lit":
            return BOOLEAN;
          case "facing":
            return FACING_4;
        }
        break;
      case BAMBOO:
        switch (key)
        {
          case "age":
          case "stage":
            return new String[]{"0", "1"};
          case "leaves":
            return new String[]{"large", "none", "small"};
        }
        break;
      case BLACK_BANNER:
      case BLUE_BANNER:
      case BROWN_BANNER:
      case CYAN_BANNER:
      case GRAY_BANNER:
      case GREEN_BANNER:
      case LIGHT_BLUE_BANNER:
      case LIGHT_GRAY_BANNER:
      case LIME_BANNER:
      case MAGENTA_BANNER:
      case ORANGE_BANNER:
      case PINK_BANNER:
      case PURPLE_BANNER:
      case RED_BANNER:
      case WHITE_BANNER:
      case YELLOW_BANNER:
      case SKELETON_SKULL:
      case WITHER_SKELETON_SKULL:
      case CREEPER_HEAD:
      case DRAGON_HEAD:
      case PLAYER_HEAD:
      case ZOMBIE_HEAD:
        switch (key)
        {
          case "facing":
            return FACING_4;
          case "rotation":
            return getNumbers(15);
        }
        break;
      case BARREL:
        switch (key)
        {
          case "facing":
            return FACING_6;
          case "open":
            return BOOLEAN;
        }
        break;
      case BLACK_BED:
      case BLUE_BED:
      case BROWN_BED:
      case CYAN_BED:
      case GRAY_BED:
      case GREEN_BED:
      case LIGHT_BLUE_BED:
      case LIGHT_GRAY_BED:
      case LIME_BED:
      case MAGENTA_BED:
      case ORANGE_BED:
      case PINK_BED:
      case PURPLE_BED:
      case RED_BED:
      case WHITE_BED:
      case YELLOW_BED:
        switch (key)
        {
          case "facing":
            return FACING_4;
          case "occupied":
            return BOOLEAN;
          case "part":
            return new String[]{"foot", "head"};
        }
        break;
      case BEEHIVE:
      case BEE_NEST:
        switch (key)
        {
          case "facing":
            return FACING_4;
          case "honey_level":
            return getNumbers(5);
        }
        break;
      case BEETROOT:
      case BEETROOTS:
      case NETHER_WART:
        if (key.equals("age"))
        {
          return getNumbers(3);
        }
        break;
      case BELL:
        switch (key)
        {
          case "attachment":
            return new String[]{"ceiling", "double_wall", "floor", "single_wall"};
          case "facing":
            return FACING_4;
          case "powered":
            return BOOLEAN;
        }
        break;
      case BLAST_FURNACE:
      case FURNACE:
      case SMOKER:
        switch (key)
        {
          case "facing":
            return FACING_4;
          case "lit":
            return BOOLEAN;
        }
        break;
      case BONE_BLOCK:
      case HAY_BLOCK:
      case ACACIA_LOG:
      case BIRCH_LOG:
      case DARK_OAK_LOG:
      case JUNGLE_LOG:
      case OAK_LOG:
      case SPRUCE_LOG:
      case STRIPPED_ACACIA_LOG:
      case STRIPPED_BIRCH_LOG:
      case STRIPPED_DARK_OAK_LOG:
      case STRIPPED_JUNGLE_LOG:
      case STRIPPED_OAK_LOG:
      case STRIPPED_SPRUCE_LOG:
      case STRIPPED_CRIMSON_STEM:
      case STRIPPED_WARPED_STEM:
      case CRIMSON_STEM:
      case WARPED_STEM:
      case PURPUR_PILLAR:
      case QUARTZ_PILLAR:
      case ACACIA_WOOD:
      case BIRCH_WOOD:
      case DARK_OAK_WOOD:
      case JUNGLE_WOOD:
      case OAK_WOOD:
      case SPRUCE_WOOD:
      case STRIPPED_ACACIA_WOOD:
      case STRIPPED_BIRCH_WOOD:
      case STRIPPED_DARK_OAK_WOOD:
      case STRIPPED_JUNGLE_WOOD:
      case STRIPPED_OAK_WOOD:
      case STRIPPED_SPRUCE_WOOD:
      case CRIMSON_HYPHAE:
      case STRIPPED_CRIMSON_HYPHAE:
      case STRIPPED_WARPED_HYPHAE:
      case WARPED_HYPHAE:
        if (key.equals("axis"))
        {
          return AXIS;
        }
        break;
      case BREWING_STAND:
        switch (key)
        {
          case "has_bottle_0":
          case "has_bottle_1":
          case "has_bottle_2":
            return BOOLEAN;
        }
        break;
      case BUBBLE_COLUMN:
        if (key.equals("drag"))
        {
          return BOOLEAN;
        }
        break;
      case ACACIA_BUTTON:
      case BIRCH_BUTTON:
      case CRIMSON_BUTTON:
      case DARK_OAK_BUTTON:
      case JUNGLE_BUTTON:
      case OAK_BUTTON:
      case POLISHED_BLACKSTONE_BUTTON:
      case SPRUCE_BUTTON:
      case STONE_BUTTON:
      case WARPED_BUTTON:
      case LEVER:
        switch (key)
        {
          case "face":
            return new String[]{"ceiling", "floor", "wall"};
          case "facing":
            return FACING_4;
          case "powered":
            return BOOLEAN;
        }
        break;
      case CACTUS:
      case SUGAR_CANE:
        if (key.equals("age"))
        {
          return getNumbers(15);
        }
        break;
      case CAMPFIRE:
      case SOUL_CAMPFIRE:
        switch (key)
        {
          case "facing":
            return FACING_4;
          case "lit":
          case "signal_fire":
          case "waterlogged":
            return BOOLEAN;
        }
        break;
      case CAKE:
        if (key.equals("bites"))
        {
          return getNumbers(6);
        }
        break;
      case CARROT:
      case CARROTS:
      case POTATO:
      case POTATOES:
      case WHEAT_SEEDS:
      case WHEAT:
        if (key.equals("age"))
        {
          return getNumbers(7);
        }
        break;
      case MELON_SEEDS:
      case MELON_STEM:
      case PUMPKIN_SEEDS:
      case PUMPKIN_STEM:
        switch (key)
        {
          case "age":
            return getNumbers(7);
          case "facing":
            return FACING_4;
        }
        break;
      case CAULDRON:
        if (key.equals("level"))
        {
          return getNumbers(3);
        }
        break;
      case CHAIN:
        switch (key)
        {
          case "axis":
            return AXIS;
          case "waterlogged":
            return BOOLEAN;
        }
        break;
      case CHEST:
      case TRAPPED_CHEST:
        switch (key)
        {
          case "facing":
            return FACING_4;
          case "type":
            return new String[]{"left", "right", "single"};
          case "waterlogged":
            return BOOLEAN;
        }
        break;
      case ENDER_CHEST:
      case LADDER:
        switch (key)
        {
          case "facing":
            return FACING_4;
          case "waterlogged":
            return BOOLEAN;
        }
        break;
      case CHORUS_FLOWER:
        if (key.equals("age"))
        {
          return getNumbers(5);
        }
        break;
      case CHORUS_PLANT:
        switch (key)
        {
          case "down":
          case "east":
          case "north":
          case "south":
          case "up":
          case "west":
            return BOOLEAN;
        }
        break;
      case COCOA:
      case COCOA_BEANS:
        switch (key)
        {
          case "age":
            return getNumbers(2);
          case "facing":
            return FACING_4;
        }
        break;
      case COMMAND_BLOCK:
      case CHAIN_COMMAND_BLOCK:
      case REPEATING_COMMAND_BLOCK:
        switch (key)
        {
          case "conditional":
            return BOOLEAN;
          case "facing":
            return FACING_6;
        }
        break;
      case COMPOSTER:
        if (key.equals("level"))
        {
          return getNumbers(8);
        }
        break;
      case CONDUIT:
        if (key.equals("waterlogged"))
        {
          return BOOLEAN;
        }
        break;
      case DAYLIGHT_DETECTOR:
        switch (key)
        {
          case "inverted":
            return BOOLEAN;
          case "power":
            return getNumbers(15);
        }
        break;
      case DISPENSER:
      case DROPPER:
        switch (key)
        {
          case "facing":
            return FACING_6;
          case "triggered":
            return BOOLEAN;
        }
        break;
      case DARK_OAK_DOOR:
      case ACACIA_DOOR:
      case BIRCH_DOOR:
      case CRIMSON_DOOR:
      case IRON_DOOR:
      case JUNGLE_DOOR:
      case OAK_DOOR:
      case SPRUCE_DOOR:
      case WARPED_DOOR:
        switch (key)
        {
          case "facing":
            return FACING_4;
          case "half":
            return new String[]{"lower", "upper"};
          case "hinge":
            return new String[]{"left", "right"};
          case "open":
          case "powered":
            return BOOLEAN;
        }
        break;
      case END_PORTAL_FRAME:
        switch (key)
        {
          case "eye":
            return BOOLEAN;
          case "facing":
            return FACING_4;
        }
        break;
      case END_ROD:
      case SHULKER_BOX:
      case BLACK_SHULKER_BOX:
      case BLUE_SHULKER_BOX:
      case BROWN_SHULKER_BOX:
      case CYAN_SHULKER_BOX:
      case GRAY_SHULKER_BOX:
      case GREEN_SHULKER_BOX:
      case LIGHT_BLUE_SHULKER_BOX:
      case LIGHT_GRAY_SHULKER_BOX:
      case LIME_SHULKER_BOX:
      case MAGENTA_SHULKER_BOX:
      case ORANGE_SHULKER_BOX:
      case PINK_SHULKER_BOX:
      case PURPLE_SHULKER_BOX:
      case RED_SHULKER_BOX:
      case WHITE_SHULKER_BOX:
      case YELLOW_SHULKER_BOX:
        if (key.equals("facing"))
        {
          return FACING_6;
        }
        break;
      case FARMLAND:
        if (key.equals("moisture"))
        {
          return getNumbers(7);
        }
        break;
      case ACACIA_FENCE:
      case BIRCH_FENCE:
      case CRIMSON_FENCE:
      case DARK_OAK_FENCE:
      case JUNGLE_FENCE:
      case NETHER_BRICK_FENCE:
      case OAK_FENCE:
      case SPRUCE_FENCE:
      case WARPED_FENCE:
      case GLASS_PANE:
      case BLACK_STAINED_GLASS_PANE:
      case BLUE_STAINED_GLASS_PANE:
      case BROWN_STAINED_GLASS_PANE:
      case CYAN_STAINED_GLASS_PANE:
      case GRAY_STAINED_GLASS_PANE:
      case GREEN_STAINED_GLASS_PANE:
      case LIGHT_BLUE_STAINED_GLASS_PANE:
      case LIGHT_GRAY_STAINED_GLASS_PANE:
      case LIME_STAINED_GLASS_PANE:
      case MAGENTA_STAINED_GLASS_PANE:
      case ORANGE_STAINED_GLASS_PANE:
      case PINK_STAINED_GLASS_PANE:
      case PURPLE_STAINED_GLASS_PANE:
      case RED_STAINED_GLASS_PANE:
      case WHITE_STAINED_GLASS_PANE:
      case YELLOW_STAINED_GLASS_PANE:
      case IRON_BARS:
        switch (key)
        {
          case "east":
          case "north":
          case "south":
          case "waterlogged":
          case "west":
            return BOOLEAN;
        }
        break;
      case ACACIA_FENCE_GATE:
      case BIRCH_FENCE_GATE:
      case CRIMSON_FENCE_GATE:
      case DARK_OAK_FENCE_GATE:
      case JUNGLE_FENCE_GATE:
      case OAK_FENCE_GATE:
      case SPRUCE_FENCE_GATE:
      case WARPED_FENCE_GATE:
        switch (key)
        {
          case "facing":
            return FACING_4;
          case "in_wall":
          case "open":
          case "powered":
            return BOOLEAN;
        }
        break;
      case FIRE:
      case SOUL_FIRE:
        switch (key)
        {
          case "age":
            return getNumbers(15);
          case "east":
          case "north":
          case "south":
          case "up":
          case "west":
            return BOOLEAN;
        }
        break;
      case FROSTED_ICE:
      case SWEET_BERRIES:
      case SWEET_BERRY_BUSH:
        if (key.equals("age"))
        {
          return getNumbers(3);
        }
        break;
      case GRASS_BLOCK:
      case MYCELIUM:
      case PODZOL:
        if (key.equals("snowy"))
        {
          return BOOLEAN;
        }
        break;
      case GRINDSTONE:
        switch (key)
        {
          case "face":
            return new String[]{"ceiling", "floor", "wall"};
          case "facing":
            return FACING_4;
        }
        break;
      case HOPPER:
        switch (key)
        {
          case "enabled":
            return BOOLEAN;
          case "facing":
            return new String[]{"down", "east", "north", "south", "west"};
        }
        break;
      case JIGSAW:
        if (key.equals("orientation"))
        {
          return new String[]{"down_east", "down_north", "down_south", "down_west", "east_up", "north_up", "south_up", "up_east", "up_north", "up_south", "up_west", "west_up"};
        }
        break;
      case JUKEBOX:
        if (key.equals("has_record"))
        {
          return BOOLEAN;
        }
        break;
      case KELP:
      case KELP_PLANT:
        if (key.equals("age"))
        {
          return getNumbers(25);
        }
        break;
      case LANTERN:
      case SOUL_LANTERN:
        if (key.equals("hanging") || key.equals("waterlogged"))
        {
          return BOOLEAN;
        }
        break;
      case SUNFLOWER:
      case LILAC:
      case ROSE_BUSH:
      case PEONY:
      case TALL_GRASS:
      case LARGE_FERN:
      case TALL_SEAGRASS:
        if (key.equals("half"))
        {
          return new String[]{"lower", "upper"};
        }
        break;
      case LAVA:
      case WATER:
        if (key.equals("level"))
        {
          return getNumbers(15);
        }
        break;
      case ACACIA_LEAVES:
      case BIRCH_LEAVES:
      case DARK_OAK_LEAVES:
      case JUNGLE_LEAVES:
      case OAK_LEAVES:
      case SPRUCE_LEAVES:
        switch (key)
        {
          case "distance":
            return new String[]{"1", "2", "3", "4", "5", "6", "7"};
          case "persistent":
            return BOOLEAN;
        }
        break;
      case LECTERN:
        switch (key)
        {
          case "facing":
            return FACING_4;
          case "has_book":
          case "powered":
            return BOOLEAN;
        }
        break;
      case BROWN_MUSHROOM_BLOCK:
      case RED_MUSHROOM_BLOCK:
      case MUSHROOM_STEM:
        switch (key)
        {
          case "east":
          case "down":
          case "north":
          case "south":
          case "up":
          case "west":
            return BOOLEAN;
        }
        break;
      case NETHER_PORTAL:
        if (key.equals("axis"))
        {
          return new String[]{"x", "y"};
        }
        break;
      case NOTE_BLOCK:
        switch (key)
        {
          case "instrument":
            return new String[]{"banjo", "basedrum", "bass", "bell", "bit", "chime", "cow_bell", "didgeridoo", "flute", "guitar", "harp", "hat", "iron_xylophone", "pling", "snare", "xylophone"};
          case "note":
            return getNumbers(24);
          case "powered":
            return BOOLEAN;
        }
        break;
      case OBSERVER:
        switch (key)
        {
          case "facing":
            return FACING_6;
          case "powered":
            return BOOLEAN;
        }
        break;
      case PISTON:
      case STICKY_PISTON:
        switch (key)
        {
          case "extended":
            return BOOLEAN;
          case "facing":
            return FACING_6;
        }
        break;
      case MOVING_PISTON:
        switch (key)
        {
          case "facing":
            return FACING_6;
          case "type":
            return new String[]{"normal", "sticky"};
        }
        break;
      case PISTON_HEAD:
        switch (key)
        {
          case "facing":
            return FACING_6;
          case "short":
            return BOOLEAN;
          case "type":
            return new String[]{"normal", "sticky"};
        }
        break;
      case ACACIA_PRESSURE_PLATE:
      case BIRCH_PRESSURE_PLATE:
      case CRIMSON_PRESSURE_PLATE:
      case DARK_OAK_PRESSURE_PLATE:
      case JUNGLE_PRESSURE_PLATE:
      case OAK_PRESSURE_PLATE:
      case POLISHED_BLACKSTONE_PRESSURE_PLATE:
      case SPRUCE_PRESSURE_PLATE:
      case STONE_PRESSURE_PLATE:
      case WARPED_PRESSURE_PLATE:
        if (key.equals("powered"))
        {
          return BOOLEAN;
        }
        break;
      case HEAVY_WEIGHTED_PRESSURE_PLATE:
      case LIGHT_WEIGHTED_PRESSURE_PLATE:
        switch (key)
        {
          case "powered":
            return BOOLEAN;
          case "power":
            return getNumbers(15);
        }
        break;
      case RAIL:
        if (key.equals("shape"))
        {
          return new String[]{"east_west", "north_east", "north_south", "north_west", "south_east", "souht_west", "ascending_east", "ascending_north", "ascending_south", "ascending_west"};
        }
        break;
      case ACTIVATOR_RAIL:
      case DETECTOR_RAIL:
      case POWERED_RAIL:
        switch (key)
        {
          case "powered":
            return BOOLEAN;
          case "shape":
            return new String[]{"east_west", "north_south", "ascending_east", "ascending_north", "ascending_south", "ascending_west"};
        }
        break;
      case COMPARATOR:
        switch (key)
        {
          case "facing":
            return FACING_4;
          case "mode":
            return new String[]{"compare", "subtract"};
          case "powered":
            return BOOLEAN;
        }
        break;
      case REDSTONE:
      case REDSTONE_WIRE:
        switch (key)
        {
          case "east":
          case "north":
          case "south":
          case "west":
            return new String[]{"up", "none", "side"};
          case "power":
            return getNumbers(15);
        }
        break;
      case REDSTONE_LAMP:
      case REDSTONE_ORE:
        if (key.equals("lit"))
        {
          return BOOLEAN;
        }
        break;
      case REPEATER:
        switch (key)
        {
          case "delay":
            return new String[]{"1", "2", "3", "4"};
          case "facing":
            return FACING_4;
          case "locked":
          case "powered":
            return BOOLEAN;
        }
        break;
      case RESPAWN_ANCHOR:
        if (key.equals("charges"))
        {
          return getNumbers(4);
        }
        break;
      case ACACIA_SAPLING:
      case BIRCH_SAPLING:
      case DARK_OAK_SAPLING:
      case JUNGLE_SAPLING:
      case OAK_SAPLING:
      case SPRUCE_SAPLING:
        if (key.equals("stage"))
        {
          return getNumbers(1);
        }
        break;
      case SCAFFOLDING:
        switch (key)
        {
          case "bottom":
          case "waterlogged":
            return BOOLEAN;
          case "distance":
            return getNumbers(7);
        }
        break;
      case SEA_PICKLE:
        switch (key)
        {
          case "pickles":
            return new String[]{"1", "2", "3", "4"};
          case "waterlogged":
            return BOOLEAN;
        }
        break;
      case ACACIA_SIGN:
      case BIRCH_SIGN:
      case CRIMSON_SIGN:
      case DARK_OAK_SIGN:
      case JUNGLE_SIGN:
      case OAK_SIGN:
      case SPRUCE_SIGN:
      case WARPED_SIGN:
        switch (key)
        {
          case "rotation":
            return getNumbers(15);
          case "facing":
            return FACING_4;
          case "waterlogged":
            return BOOLEAN;
        }
        break;
      case ACACIA_WALL_SIGN:
      case BIRCH_WALL_SIGN:
      case CRIMSON_WALL_SIGN:
      case DARK_OAK_WALL_SIGN:
      case JUNGLE_WALL_SIGN:
      case OAK_WALL_SIGN:
      case SPRUCE_WALL_SIGN:
      case WARPED_WALL_SIGN:
        switch (key)
        {
          case "facing":
            return FACING_4;
          case "waterlogged":
            return BOOLEAN;
        }
        break;
      case ACACIA_SLAB:
      case ANDESITE_SLAB:
      case BIRCH_SLAB:
      case BLACKSTONE_SLAB:
      case BRICK_SLAB:
      case COBBLESTONE_SLAB:
      case CRIMSON_SLAB:
      case CUT_RED_SANDSTONE_SLAB:
      case CUT_SANDSTONE_SLAB:
      case DARK_OAK_SLAB:
      case DARK_PRISMARINE_SLAB:
      case DIORITE_SLAB:
      case END_STONE_BRICK_SLAB:
      case GRANITE_SLAB:
      case JUNGLE_SLAB:
      case MOSSY_COBBLESTONE_SLAB:
      case MOSSY_STONE_BRICK_SLAB:
      case NETHER_BRICK_SLAB:
      case OAK_SLAB:
      case PETRIFIED_OAK_SLAB:
      case POLISHED_ANDESITE_SLAB:
      case POLISHED_BLACKSTONE_BRICK_SLAB:
      case POLISHED_BLACKSTONE_SLAB:
      case POLISHED_DIORITE_SLAB:
      case POLISHED_GRANITE_SLAB:
      case PRISMARINE_BRICK_SLAB:
      case PRISMARINE_SLAB:
      case PURPUR_SLAB:
      case QUARTZ_SLAB:
      case RED_NETHER_BRICK_SLAB:
      case RED_SANDSTONE_SLAB:
      case SANDSTONE_SLAB:
      case SMOOTH_QUARTZ_SLAB:
      case SMOOTH_RED_SANDSTONE_SLAB:
      case SMOOTH_SANDSTONE_SLAB:
      case SMOOTH_STONE_SLAB:
      case SPRUCE_SLAB:
      case STONE_BRICK_SLAB:
      case STONE_SLAB:
      case WARPED_SLAB:
        switch (key)
        {
          case "type":
            return new String[]{"bottom", "top", "double"};
          case "waterlogged":
            return BOOLEAN;
        }
        break;
      case SNOW:
        if (key.equals("layers"))
        {
          return new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};
        }
        break;
      case ACACIA_STAIRS:
      case ANDESITE_STAIRS:
      case BIRCH_STAIRS:
      case BLACKSTONE_STAIRS:
      case BRICK_STAIRS:
      case COBBLESTONE_STAIRS:
      case CRIMSON_STAIRS:
      case DARK_OAK_STAIRS:
      case DARK_PRISMARINE_STAIRS:
      case DIORITE_STAIRS:
      case END_STONE_BRICK_STAIRS:
      case GRANITE_STAIRS:
      case JUNGLE_STAIRS:
      case MOSSY_COBBLESTONE_STAIRS:
      case MOSSY_STONE_BRICK_STAIRS:
      case NETHER_BRICK_STAIRS:
      case OAK_STAIRS:
      case POLISHED_ANDESITE_STAIRS:
      case POLISHED_BLACKSTONE_BRICK_STAIRS:
      case POLISHED_BLACKSTONE_STAIRS:
      case POLISHED_DIORITE_STAIRS:
      case POLISHED_GRANITE_STAIRS:
      case PRISMARINE_BRICK_STAIRS:
      case PRISMARINE_STAIRS:
      case PURPUR_STAIRS:
      case QUARTZ_STAIRS:
      case RED_NETHER_BRICK_STAIRS:
      case RED_SANDSTONE_STAIRS:
      case SANDSTONE_STAIRS:
      case SMOOTH_QUARTZ_STAIRS:
      case SMOOTH_RED_SANDSTONE_STAIRS:
      case SMOOTH_SANDSTONE_STAIRS:
      case SPRUCE_STAIRS:
      case STONE_BRICK_STAIRS:
      case STONE_STAIRS:
      case WARPED_STAIRS:
        switch (key)
        {
          case "facing":
            return FACING_4;
          case "half":
            return new String[]{"bottom", "top"};
          case "shape":
            return new String[]{"inner_left", "inner_right", "outer_left", "outer_right", "straight"};
          case "waterlogged":
            return BOOLEAN;
        }
        break;
      case STRUCTURE_BLOCK:
        if (key.equals("mode"))
        {
          return new String[]{"corner", "data", "load", "save"};
        }
        break;
      case TNT:
        if (key.equals("unstable"))
        {
          return BOOLEAN;
        }
        break;
      case ACACIA_TRAPDOOR:
      case BIRCH_TRAPDOOR:
      case CRIMSON_TRAPDOOR:
      case DARK_OAK_TRAPDOOR:
      case IRON_TRAPDOOR:
      case JUNGLE_TRAPDOOR:
      case OAK_TRAPDOOR:
      case SPRUCE_TRAPDOOR:
      case WARPED_TRAPDOOR:
        switch (key)
        {
          case "facing":
            return FACING_4;
          case "half":
            return new String[]{"bottom", "top"};
          case "open":
          case "powered":
          case "waterlogged":
            return BOOLEAN;
        }
        break;
      case TRIPWIRE:
      case STRING:
        switch (key)
        {
          case "attached":
          case "disarmed":
          case "east":
          case "north":
          case "powered":
          case "south":
          case "west":
            return BOOLEAN;
        }
        break;
      case TRIPWIRE_HOOK:
        switch (key)
        {
          case "attached":
          case "poweerd":
            return BOOLEAN;
          case "facing":
            return FACING_4;
        }
        break;
      case TURTLE_EGG:
        switch (key)
        {
          case "eggs":
            return new String[]{"1", "2", "3", "4"};
          case "hatch":
            return getNumbers(2);
        }
        break;
      case VINE:
        switch (key)
        {
          case "east":
          case "north":
          case "south":
          case "up":
          case "west":
            return BOOLEAN;
        }
        break;
      case ANDESITE_WALL:
      case BLACKSTONE_WALL:
      case BRICK_WALL:
      case COBBLESTONE_WALL:
      case DIORITE_WALL:
      case END_STONE_BRICK_WALL:
      case GRANITE_WALL:
      case MOSSY_COBBLESTONE_WALL:
      case MOSSY_STONE_BRICK_WALL:
      case NETHER_BRICK_WALL:
      case POLISHED_BLACKSTONE_BRICK_WALL:
      case POLISHED_BLACKSTONE_WALL:
      case PRISMARINE_WALL:
      case RED_NETHER_BRICK_WALL:
      case RED_SANDSTONE_WALL:
      case SANDSTONE_WALL:
      case STONE_BRICK_WALL:
        switch (key)
        {
          case "east":
          case "north":
          case "south":
          case "west":
            return new String[]{"low", "none", "tall"};
          case "up":
          case "waterlogged":
            return BOOLEAN;
        }
        break;
    }
    return null;
  }

  @NotNull
  private static String[] getNumbers(int to)
  {
    String[] returnValue = new String[to + 1];
    for (int i = 0; i < returnValue.length; i++)
    {
      returnValue[i] = i + "";
    }
    return returnValue;
  }

  private final static String[] BOOLEAN = new String[]{"true", "false"};

  private final static String[] FACING_4 = new String[]{"east", "north", "south", "west"};

  private final static String[] FACING_6 = new String[]{"down", "east", "north", "south", "up", "west"};

  private final static String[] AXIS = new String[]{"x", "y", "z"};
}

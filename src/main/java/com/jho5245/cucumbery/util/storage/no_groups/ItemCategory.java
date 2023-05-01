package com.jho5245.cucumbery.util.storage.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemCategory
{
  @NotNull
  public static Rarity getItemRarirty(@NotNull Material type)
  {
    return switch (type)
            {
              case CRIMSON_NYLIUM,
                      WARPED_NYLIUM,
                      CRIMSON_PLANKS,
                      WARPED_PLANKS,
                      RED_SAND,
                      GOLD_ORE,
                      NETHER_GOLD_ORE,
                      CRIMSON_STEM,
                      WARPED_STEM,
                      STRIPPED_CRIMSON_STEM,
                      STRIPPED_WARPED_STEM,
                      STRIPPED_CRIMSON_HYPHAE,
                      STRIPPED_WARPED_HYPHAE,
                      CRIMSON_HYPHAE,
                      WARPED_HYPHAE,
                      LAPIS_ORE,
                      LAPIS_BLOCK,
                      GOLD_BLOCK,
                      RED_SANDSTONE_SLAB,
                      CUT_RED_SANDSTONE_SLAB,
                      PRISMARINE_SLAB,
                      PRISMARINE_BRICK_SLAB,
                      DARK_PRISMARINE_SLAB,
                      SMOOTH_QUARTZ,
                      SMOOTH_RED_SANDSTONE,
                      OBSIDIAN,
                      REDSTONE_ORE,
                      NETHERRACK,
                      SOUL_SAND,
                      SOUL_SOIL,
                      BASALT,
                      POLISHED_BASALT,
                      GLOWSTONE,
                      NETHER_BRICKS,
                      CRACKED_NETHER_BRICKS,
                      CHISELED_NETHER_BRICKS,
                      NETHER_BRICK_STAIRS,
                      END_STONE,
                      END_STONE_BRICKS,
                      CRIMSON_STAIRS,
                      WARPED_STAIRS,
                      NETHER_QUARTZ_ORE,
                      CHISELED_QUARTZ_BLOCK,
                      QUARTZ_BLOCK,
                      QUARTZ_BRICKS,
                      QUARTZ_PILLAR,
                      QUARTZ_STAIRS,
                      PACKED_ICE,
                      PRISMARINE,
                      PRISMARINE_BRICKS,
                      DARK_PRISMARINE,
                      PRISMARINE_STAIRS,
                      PRISMARINE_BRICK_STAIRS,
                      DARK_PRISMARINE_STAIRS,
                      SEA_LANTERN,
                      RED_SANDSTONE,
                      CHISELED_RED_SANDSTONE,
                      CUT_RED_SANDSTONE,
                      RED_SANDSTONE_STAIRS,
                      MAGMA_BLOCK,
                      NETHER_WART_BLOCK,
                      WARPED_WART_BLOCK,
                      RED_NETHER_BRICKS,
                      SMOOTH_RED_SANDSTONE_STAIRS,
                      END_STONE_BRICK_STAIRS,
                      SMOOTH_QUARTZ_STAIRS,
                      RED_NETHER_BRICK_STAIRS,
                      SMOOTH_RED_SANDSTONE_SLAB,
                      END_STONE_BRICK_SLAB,
                      SMOOTH_QUARTZ_SLAB,
                      RED_NETHER_BRICK_SLAB,
                      BLACKSTONE,
                      BLACKSTONE_SLAB,
                      BLACKSTONE_STAIRS,
                      GILDED_BLACKSTONE,
                      POLISHED_BLACKSTONE,
                      POLISHED_BLACKSTONE_SLAB,
                      POLISHED_BLACKSTONE_STAIRS,
                      CHISELED_POLISHED_BLACKSTONE,
                      POLISHED_BLACKSTONE_BRICKS,
                      POLISHED_BLACKSTONE_BRICK_SLAB,
                      POLISHED_BLACKSTONE_BRICK_STAIRS,
                      CRACKED_POLISHED_BLACKSTONE_BRICKS,
                      CRIMSON_FUNGUS,
                      WARPED_FUNGUS,
                      CRIMSON_ROOTS,
                      WARPED_ROOTS,
                      NETHER_SPROUTS,
                      WEEPING_VINES,
                      TWISTING_VINES,
                      CRIMSON_FENCE,
                      WARPED_FENCE,
                      SOUL_TORCH,
                      INFESTED_STONE,
                      INFESTED_COBBLESTONE,
                      INFESTED_STONE_BRICKS,
                      INFESTED_MOSSY_STONE_BRICKS,
                      INFESTED_CRACKED_STONE_BRICKS,
                      INFESTED_CHISELED_STONE_BRICKS,
                      NETHER_BRICK_FENCE,
                      PRISMARINE_WALL,
                      RED_SANDSTONE_WALL,
                      NETHER_BRICK_WALL,
                      RED_NETHER_BRICK_WALL,
                      END_STONE_BRICK_WALL,
                      BLACKSTONE_WALL,
                      POLISHED_BLACKSTONE_WALL,
                      POLISHED_BLACKSTONE_BRICK_WALL,
                      SLIME_BLOCK,
                      SOUL_LANTERN,
                      SOUL_CAMPFIRE,
                      SHROOMLIGHT,
                      BEEHIVE,
                      HONEY_BLOCK,
                      HONEYCOMB_BLOCK,
                      DISPENSER,
                      NOTE_BLOCK,
                      STICKY_PISTON,
                      TNT,
                      CRIMSON_PRESSURE_PLATE,
                      WARPED_PRESSURE_PLATE,
                      POLISHED_BLACKSTONE_PRESSURE_PLATE,
                      REDSTONE_TORCH,
                      CRIMSON_TRAPDOOR,
                      WARPED_TRAPDOOR,
                      CRIMSON_FENCE_GATE,
                      WARPED_FENCE_GATE,
                      REDSTONE_LAMP,
                      CRIMSON_BUTTON,
                      WARPED_BUTTON,
                      POLISHED_BLACKSTONE_BUTTON,
                      LIGHT_WEIGHTED_PRESSURE_PLATE,
                      DAYLIGHT_DETECTOR,
                      REDSTONE_BLOCK,
                      DROPPER,
                      OBSERVER,
                      CRIMSON_DOOR,
                      WARPED_DOOR,
                      REPEATER,
                      COMPARATOR,
                      REDSTONE,
                      TARGET,
                      POWERED_RAIL,
                      DETECTOR_RAIL,
                      ACTIVATOR_RAIL,
                      SADDLE,
                      WARPED_FUNGUS_ON_A_STICK,
                      TURTLE_EGG,
                      GOLD_INGOT,
                      GUNPOWDER,
                      SLIME_BALL,
                      GLOWSTONE_DUST,
                      LAPIS_LAZULI,
                      BLUE_DYE,
                      ENDER_PEARL,
                      GOLD_NUGGET,
                      NETHER_WART,
                      FIRE_CHARGE,
                      FIREWORK_ROCKET,
                      FIREWORK_STAR,
                      NETHER_BRICK,
                      QUARTZ,
                      PRISMARINE_SHARD,
                      PRISMARINE_CRYSTALS,
                      GOLDEN_HORSE_ARMOR,
                      HONEYCOMB,
                      PUFFERFISH,
                      TROPICAL_FISH,
                      POISONOUS_POTATO,
                      HONEY_BOTTLE,
                      GOLDEN_SHOVEL,
                      GOLDEN_PICKAXE,
                      GOLDEN_AXE,
                      GOLDEN_HOE,
                      COMPASS,
                      CLOCK,
                      ENCHANTED_BOOK,
                      LEAD,
                      NAME_TAG,
                      GOLDEN_SWORD,
                      GOLDEN_HELMET,
                      GOLDEN_CHESTPLATE,
                      GOLDEN_LEGGINGS,
                      GOLDEN_BOOTS,
                      SPECTRAL_ARROW,
                      TIPPED_ARROW,
                      FERMENTED_SPIDER_EYE,
                      DRIPSTONE_BLOCK,
                      DEEPSLATE_GOLD_ORE,
                      DEEPSLATE_REDSTONE_ORE,
                      DEEPSLATE_LAPIS_ORE,
                      RAW_GOLD_BLOCK,
                      AMETHYST_BLOCK,
                      TINTED_GLASS,
                      CANDLE,
                      BLACK_CANDLE,
                      BLUE_CANDLE,
                      CYAN_CANDLE,
                      BROWN_CANDLE,
                      GRAY_CANDLE,
                      GREEN_CANDLE,
                      LIGHT_BLUE_CANDLE,
                      LIGHT_GRAY_CANDLE,
                      LIME_CANDLE,
                      MAGENTA_CANDLE,
                      ORANGE_CANDLE,
                      PINK_CANDLE,
                      PURPLE_CANDLE,
                      RED_CANDLE,
                      WHITE_CANDLE,
                      YELLOW_CANDLE,
                      GLOW_INK_SAC,
                      GLOW_ITEM_FRAME,
                      GLOW_BERRIES,
                      GLOW_LICHEN,
                      SMALL_AMETHYST_BUD,
                      MEDIUM_AMETHYST_BUD,
                      LARGE_AMETHYST_BUD,
                      AMETHYST_CLUSTER,
                      POINTED_DRIPSTONE,
                      AMETHYST_SHARD,
                      RAW_GOLD,
                      PUFFERFISH_BUCKET,
                      TROPICAL_FISH_BUCKET,
                      AXOLOTL_BUCKET,
                      SPYGLASS,
                      SCULK_SENSOR,
                      SCULK_CATALYST -> Rarity.RARE;
              case PURPUR_SLAB,
                      PURPUR_BLOCK,
                      PURPUR_PILLAR,
                      PURPUR_STAIRS,
                      DIAMOND_ORE,
                      DIAMOND_BLOCK,
                      MYCELIUM,
                      EMERALD_ORE,
                      EMERALD_BLOCK,
                      BLUE_ICE,
                      CRYING_OBSIDIAN,
                      END_ROD,
                      CHORUS_PLANT,
                      CHORUS_FLOWER,
                      JUKEBOX,
                      ENCHANTING_TABLE,
                      CRIMSON_SIGN,
                      WARPED_SIGN,
                      BELL,
                      BEE_NEST,
                      DIAMOND,
                      BLAZE_ROD,
                      EXPERIENCE_BOTTLE,
                      EMERALD,
                      DIAMOND_HORSE_ARMOR,
                      CHORUS_FRUIT,
                      POPPED_CHORUS_FRUIT,
                      NAUTILUS_SHELL,
                      GOLDEN_APPLE,
                      DIAMOND_SHOVEL,
                      DIAMOND_PICKAXE,
                      DIAMOND_AXE,
                      DIAMOND_HOE,
                      DIAMOND_SWORD,
                      CHAINMAIL_HELMET,
                      CHAINMAIL_CHESTPLATE,
                      CHAINMAIL_LEGGINGS,
                      CHAINMAIL_BOOTS,
                      DIAMOND_HELMET,
                      DIAMOND_CHESTPLATE,
                      DIAMOND_LEGGINGS,
                      DIAMOND_BOOTS,
                      BLAZE_POWDER,
                      MAGMA_CREAM,
                      BREWING_STAND,
                      GLISTERING_MELON_SLICE,
                      GOLDEN_CARROT,
                      RABBIT_FOOT,
                      PHANTOM_MEMBRANE,
                      DEEPSLATE_EMERALD_ORE,
                      DEEPSLATE_DIAMOND_ORE -> Rarity.EPIC;
              case SPONGE,
                      WET_SPONGE,
                      WITHER_ROSE,
                      ENDER_CHEST,
                      RESPAWN_ANCHOR,
                      SCUTE,
                      ENDER_EYE,
                      BAT_SPAWN_EGG,
                      BEE_SPAWN_EGG,
                      BLAZE_SPAWN_EGG,
                      CAT_SPAWN_EGG,
                      CAVE_SPIDER_SPAWN_EGG,
                      CHICKEN_SPAWN_EGG,
                      COD_SPAWN_EGG,
                      COW_SPAWN_EGG,
                      CREEPER_SPAWN_EGG,
                      DOLPHIN_SPAWN_EGG,
                      DONKEY_SPAWN_EGG,
                      DROWNED_SPAWN_EGG,
                      ELDER_GUARDIAN_SPAWN_EGG,
                      ENDERMAN_SPAWN_EGG,
                      ENDERMITE_SPAWN_EGG,
                      EVOKER_SPAWN_EGG,
                      FOX_SPAWN_EGG,
                      GHAST_SPAWN_EGG,
                      GUARDIAN_SPAWN_EGG,
                      HOGLIN_SPAWN_EGG,
                      HORSE_SPAWN_EGG,
                      HUSK_SPAWN_EGG,
                      LLAMA_SPAWN_EGG,
                      MAGMA_CUBE_SPAWN_EGG,
                      MOOSHROOM_SPAWN_EGG,
                      MULE_SPAWN_EGG,
                      OCELOT_SPAWN_EGG,
                      PANDA_SPAWN_EGG,
                      PARROT_SPAWN_EGG,
                      PHANTOM_SPAWN_EGG,
                      PIG_SPAWN_EGG,
                      PIGLIN_SPAWN_EGG,
                      PIGLIN_BRUTE_SPAWN_EGG,
                      PILLAGER_SPAWN_EGG,
                      POLAR_BEAR_SPAWN_EGG,
                      PUFFERFISH_SPAWN_EGG,
                      RABBIT_SPAWN_EGG,
                      RAVAGER_SPAWN_EGG,
                      SALMON_SPAWN_EGG,
                      SHEEP_SPAWN_EGG,
                      SHULKER_SPAWN_EGG,
                      SILVERFISH_SPAWN_EGG,
                      SKELETON_SPAWN_EGG,
                      SKELETON_HORSE_SPAWN_EGG,
                      SLIME_SPAWN_EGG,
                      SPIDER_SPAWN_EGG,
                      SQUID_SPAWN_EGG,
                      STRAY_SPAWN_EGG,
                      STRIDER_SPAWN_EGG,
                      TRADER_LLAMA_SPAWN_EGG,
                      TROPICAL_FISH_SPAWN_EGG,
                      TURTLE_SPAWN_EGG,
                      VEX_SPAWN_EGG,
                      VILLAGER_SPAWN_EGG,
                      VINDICATOR_SPAWN_EGG,
                      WANDERING_TRADER_SPAWN_EGG,
                      WITCH_SPAWN_EGG,
                      WITHER_SKELETON_SPAWN_EGG,
                      WOLF_SPAWN_EGG,
                      ZOGLIN_SPAWN_EGG,
                      ZOMBIE_SPAWN_EGG,
                      ZOMBIE_HORSE_SPAWN_EGG,
                      ZOMBIE_VILLAGER_SPAWN_EGG,
                      ZOMBIFIED_PIGLIN_SPAWN_EGG,
                      ALLAY_SPAWN_EGG,
                      FROG_SPAWN_EGG,
                      TADPOLE_SPAWN_EGG,
                      WARDEN_SPAWN_EGG,
                      MUSIC_DISC_13,
                      MUSIC_DISC_CAT,
                      MUSIC_DISC_BLOCKS,
                      MUSIC_DISC_CHIRP,
                      MUSIC_DISC_FAR,
                      MUSIC_DISC_MALL,
                      MUSIC_DISC_MELLOHI,
                      MUSIC_DISC_STAL,
                      MUSIC_DISC_STRAD,
                      MUSIC_DISC_WARD,
                      MUSIC_DISC_11,
                      MUSIC_DISC_WAIT,
                      HEART_OF_THE_SEA,
                      GLOBE_BANNER_PATTERN,
                      TURTLE_HELMET,
                      TRIDENT,
                      GHAST_TEAR,
                      DRAGON_BREATH,
                      AXOLOTL_SPAWN_EGG,
                      GLOW_SQUID_SPAWN_EGG,
                      GOAT_SPAWN_EGG,
                      MUSIC_DISC_OTHERSIDE,
                      GOAT_HORN,
                      ECHO_SHARD -> Rarity.ELITE;
              case BEDROCK,
                      NETHERITE_BLOCK,
                      ANCIENT_DEBRIS,
                      END_PORTAL_FRAME,
                      SHULKER_BOX,
                      WHITE_SHULKER_BOX,
                      ORANGE_SHULKER_BOX,
                      MAGENTA_SHULKER_BOX,
                      LIGHT_BLUE_SHULKER_BOX,
                      YELLOW_SHULKER_BOX,
                      LIME_SHULKER_BOX,
                      PINK_SHULKER_BOX,
                      GRAY_SHULKER_BOX,
                      LIGHT_GRAY_SHULKER_BOX,
                      CYAN_SHULKER_BOX,
                      PURPLE_SHULKER_BOX,
                      BLUE_SHULKER_BOX,
                      BROWN_SHULKER_BOX,
                      GREEN_SHULKER_BOX,
                      RED_SHULKER_BOX,
                      BLACK_SHULKER_BOX,
                      SKELETON_SKULL,
                      WITHER_SKELETON_SKULL,
                      PLAYER_HEAD,
                      ZOMBIE_HEAD,
                      CREEPER_HEAD,
                      END_CRYSTAL,
                      LODESTONE,
                      CONDUIT,
                      NETHERITE_INGOT,
                      NETHERITE_SCRAP,
                      NETHER_STAR,
                      SHULKER_SHELL,
                      MUSIC_DISC_PIGSTEP,
                      CREEPER_BANNER_PATTERN,
                      SKULL_BANNER_PATTERN,
                      MOJANG_BANNER_PATTERN,
                      PIGLIN_BANNER_PATTERN,
                      ENCHANTED_GOLDEN_APPLE,
                      NETHERITE_SHOVEL,
                      NETHERITE_PICKAXE,
                      NETHERITE_AXE,
                      NETHERITE_HOE,
                      NETHERITE_SWORD,
                      NETHERITE_HELMET,
                      NETHERITE_CHESTPLATE,
                      NETHERITE_LEGGINGS,
                      NETHERITE_BOOTS,
                      TOTEM_OF_UNDYING,
                      SPAWNER,
                      BUDDING_AMETHYST,
                      REINFORCED_DEEPSLATE,
                      OCHRE_FROGLIGHT,
                      PEARLESCENT_FROGLIGHT,
                      VERDANT_FROGLIGHT,
                      RECOVERY_COMPASS,
                      DISC_FRAGMENT_5,
                      MUSIC_DISC_5 -> Rarity.UNIQUE;
              case DRAGON_HEAD,
                      ELYTRA,
                      BEACON,
                      DRAGON_EGG -> Rarity.EXCELLENT;
              case BARRIER,
                      COMMAND_BLOCK,
                      CHAIN_COMMAND_BLOCK,
                      REPEATING_COMMAND_BLOCK,
                      COMMAND_BLOCK_MINECART,
                      DEBUG_STICK,
                      STRUCTURE_BLOCK,
                      STRUCTURE_VOID,
                      JIGSAW,
                      LIGHT,
                      ENDER_DRAGON_SPAWN_EGG,
                      WITHER_SPAWN_EGG -> Rarity._ADMIN;
              case SCULK_SHRIEKER -> Rarity.JUNK;
              default -> Rarity.NORMAL;
            };
  }

  /**
   * Represents rarity of an {@link ItemStack}.
   * <p>Can vary depending on item's meta (Enchants, Durability, etc.)
   */
  public enum Rarity
  {
    /**
     * Lowest rarity, i.g. normal items with cursed enchant/low durability
     */
    JUNK(-1
            , Cucumbery.config.getLong("item-rarity-value.junk.value")
            , Cucumbery.config.getString("item-rarity-value.junk.name")),
    /**
     * The default rarity to most items.
     */
    NORMAL(0
            , Cucumbery.config.getLong("item-rarity-value.normal.value")
            , Cucumbery.config.getString("item-rarity-value.normal.name")),
    RARE(1
            , Cucumbery.config.getLong("item-rarity-value.rare.value")
            , Cucumbery.config.getString("item-rarity-value.rare.name")),
    EPIC(2
            , Cucumbery.config.getLong("item-rarity-value.epic.value")
            , Cucumbery.config.getString("item-rarity-value.epic.name")),
    ELITE(3
            , Cucumbery.config.getLong("item-rarity-value.elite.value")
            , Cucumbery.config.getString("item-rarity-value.elite.name")),
    UNIQUE(4
            , Cucumbery.config.getLong("item-rarity-value.unique.value")
            , Cucumbery.config.getString("item-rarity-value.unique.name")),
    EXCELLENT(5
            , Cucumbery.config.getLong("item-rarity-value.excellent.value")
            , Cucumbery.config.getString("item-rarity-value.excellent.name")),
    LEGENDARY(6
            , Cucumbery.config.getLong("item-rarity-value.legendary.value")
            , Cucumbery.config.getString("item-rarity-value.legendary.name")),
    ARTIFACT(7
            , Cucumbery.config.getLong("item-rarity-value.artifact.value")
            , Cucumbery.config.getString("item-rarity-value.artifact.name")),

    _COMMON(10, 0, "&f&lCOMMON"),
    _UNCOMMON(11, 0, "&a&lUNCOMMON"),
    _RARE(12, 0, "&9&lRARE"),
    _EPIC(13, 0, "&5&lEPIC"),
    _LEGENDARY(14, 0, "&6&lLEGENDARY"),
    _MYTHIC(15, 0, "&d&lMYTHIC"),
    _DIVINE(16, 0, "&b&lDIVINE"),
    _SPECIAL(17, 0, "&c&lSPECIAL"),
    _VERY_SPECIAL(18, 0, "&c&lVERY SPECIAL"),
    _ADMIN(999, 0, "&4[관리자]");;

    private final int rarityNumber;

    private final long rarityValue;

    private final String display;

    Rarity(int rarityNumber
            , long rarityValue
            , String display)
    {
      this.rarityNumber = rarityNumber;
      this.rarityValue = rarityValue;
      this.display = display;
    }

    /**
     * @return Tier of Rarity
     */
    public int getRarityNumber()
    {
      return rarityNumber;
    }

    /**
     * @return Value(Score) of item's rarity
     */
    public long getRarityValue()
    {
      return rarityValue;
    }

    public String getDisplay()
    {
      return display;
    }

    public static Rarity getRarityFromValue(long value)
    {
      if (value < Rarity.NORMAL.getRarityValue())
      {
        return Rarity.JUNK;
      }
      else if (value < Rarity.RARE.getRarityValue())
      {
        return Rarity.NORMAL;
      }
      else if (value < Rarity.EPIC.getRarityValue())
      {
        return Rarity.RARE;
      }
      else if (value < Rarity.ELITE.getRarityValue())
      {
        return Rarity.EPIC;
      }
      else if (value < Rarity.UNIQUE.getRarityValue())
      {
        return Rarity.ELITE;
      }
      else if (value < Rarity.EXCELLENT.getRarityValue())
      {
        return Rarity.UNIQUE;
      }
      else if (value < Rarity.LEGENDARY.getRarityValue())
      {
        return Rarity.EXCELLENT;
      }
      else if (value < Rarity.ARTIFACT.getRarityValue())
      {
        return Rarity.LEGENDARY;
      }
      else
      {
        return Rarity.ARTIFACT;
      }
    }

    public Rarity tierUp()
    {
      int number = rarityNumber + 1;
      for (Rarity rarity : Rarity.values())
      {
        if (rarity.rarityNumber == number)
        {
          return rarity;
        }
      }
      return this;
    }

    public String colorString()
    {
      return switch (this)
              {
                case JUNK -> "rgb128,111,92;";
                case NORMAL -> "rgb230,230,230;";
                case RARE -> "rgb123,224,27;";
                case EPIC -> "rgb81,144,245;";
                case ELITE -> "rgb74,224,214;";
                case UNIQUE -> "rgb255,209,30;";
                case EXCELLENT -> "rgb157,81,245;";
                case LEGENDARY -> "rg225,255;";
                case ARTIFACT -> "rgb244,73,120;";
                case _COMMON -> "&f";
                case _UNCOMMON -> "&a";
                case _RARE -> "&9";
                case _EPIC -> "&5";
                case _LEGENDARY -> "&6";
                case _MYTHIC -> "&d";
                case _DIVINE -> "&b";
                case _SPECIAL, _VERY_SPECIAL -> "&c";
                case _ADMIN -> "&4";
              };
    }
  }
}

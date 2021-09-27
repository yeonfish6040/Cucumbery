package com.jho5245.cucumbery.util.storage.data;

import org.bukkit.Statistic;
import org.jetbrains.annotations.NotNull;

public class TranslatableKeyParser
{
  @NotNull
  public static String getKey(@NotNull Statistic statistic)
  {
    switch (statistic)
    {
      case BREAK_ITEM -> {
        return "stat_type.minecraft.broken";
      }
      case CRAFT_ITEM -> {
        return "stat_type.minecraft.crafted";
      }
      case DROP -> {
        return "stat_type.minecraft.dropped";
      }
      case MINE_BLOCK -> {
        return "stat_type.minecraft.mined";
      }
      case PICKUP -> {
        return "stat_type.minecraft.picked_up";
      }
      case USE_ITEM -> {
        return "stat_type.minecraft.used";
      }
      case KILL_ENTITY -> {
        return "stat_type.minecraft.killed";
      }
      case ENTITY_KILLED_BY -> {
        return "stat_type.minecraft.killed_by";
      }
    }
    return "stat.minecraft." + switch (statistic)
            {
              case ARMOR_CLEANED -> "clean_armor";
              case BANNER_CLEANED -> "clean_banner";
              case BEACON_INTERACTION -> "interact_with_beacon";
              case BREWINGSTAND_INTERACTION -> "interact_with_brewingstand";
              case CAKE_SLICES_EATEN -> "eat_cake_slice";
              case CAULDRON_FILLED -> "fill_cauldron";
              case CAULDRON_USED -> "use_cauldron";
              case CRAFTING_TABLE_INTERACTION -> "interact_with_crafting_table";
              case FURNACE_INTERACTION -> "interact_with_furnace";
              case CHEST_OPENED -> "open_chest";
              case ENDERCHEST_OPENED -> "open_enderchest";
              case SHULKER_BOX_OPENED -> "open_shulker_box";
              case FLOWER_POTTED -> "pot_flower";
              case DISPENSER_INSPECTED -> "inspect_dispenser";
              case DROPPER_INSPECTED -> "inspect_dropper";
              case HOPPER_INSPECTED -> "inspect_hopper";
              case ITEM_ENCHANTED -> "enchant_item";
              case NOTEBLOCK_PLAYED -> "play_noteblock";
              case RECORD_PLAYED -> "play_record";
              case PLAY_ONE_MINUTE -> "play_time";
              case TRAPPED_CHEST_TRIGGERED -> "trigger_trapped_chest";
              default -> statistic.toString();
            };
  }
}

package com.jho5245.cucumbery.util.itemlore;

import org.bukkit.DyeColor;
import org.bukkit.entity.TropicalFish.Pattern;
import org.jetbrains.annotations.NotNull;

public class TropicalFishLore
{
  @NotNull
  public static String getTropicalFishKey(@NotNull DyeColor bodyColor, @NotNull DyeColor patternColor, @NotNull Pattern pattern)
  {
    switch (pattern)
    {
      case BETTY:
        if (bodyColor == DyeColor.RED && patternColor == DyeColor.WHITE)
          return "entity.minecraft.tropical_fish.predefined.14";
        return "entity.minecraft.tropical_fish.type." + pattern.toString().toLowerCase();
      case BLOCKFISH:
        if (bodyColor == DyeColor.PURPLE && patternColor == DyeColor.YELLOW)
          return "entity.minecraft.tropical_fish.predefined.7";
        if (bodyColor == DyeColor.RED && patternColor == DyeColor.WHITE)
          return "entity.minecraft.tropical_fish.predefined.16";
        return "entity.minecraft.tropical_fish.type." + pattern.toString().toLowerCase();
      case BRINELY:
        if (bodyColor == DyeColor.LIME && patternColor == DyeColor.LIGHT_BLUE)
          return "entity.minecraft.tropical_fish.predefined.13";
        return "entity.minecraft.tropical_fish.type." + pattern.toString().toLowerCase();
      case CLAYFISH:
        if (bodyColor == DyeColor.WHITE && patternColor == DyeColor.ORANGE)
          return "entity.minecraft.tropical_fish.predefined.11";
        if (bodyColor == DyeColor.WHITE && patternColor == DyeColor.RED)
          return "entity.minecraft.tropical_fish.predefined.8";
        if (bodyColor == DyeColor.WHITE && patternColor == DyeColor.GRAY)
          return "entity.minecraft.tropical_fish.predefined.3";
        return "entity.minecraft.tropical_fish.type." + pattern.toString().toLowerCase();
      case DASHER:
        if (bodyColor == DyeColor.CYAN && patternColor == DyeColor.PINK)
          return "entity.minecraft.tropical_fish.predefined.12";
        if (bodyColor == DyeColor.CYAN && patternColor == DyeColor.YELLOW)
          return "entity.minecraft.tropical_fish.predefined.20";
        return "entity.minecraft.tropical_fish.type." + pattern.toString().toLowerCase();
      case FLOPPER:
        if (bodyColor == DyeColor.GRAY && patternColor == DyeColor.GRAY)
          return "entity.minecraft.tropical_fish.predefined.1";
        if (bodyColor == DyeColor.YELLOW && patternColor == DyeColor.YELLOW)
          return "entity.minecraft.tropical_fish.predefined.21";
        if (bodyColor == DyeColor.WHITE && patternColor == DyeColor.YELLOW)
          return "entity.minecraft.tropical_fish.predefined.17";
        if (bodyColor == DyeColor.GRAY && patternColor == DyeColor.BLUE)
          return "entity.minecraft.tropical_fish.predefined.2";
        return "entity.minecraft.tropical_fish.type." + pattern.toString().toLowerCase();
      case GLITTER:
        if (bodyColor == DyeColor.WHITE && patternColor == DyeColor.GRAY)
          return "entity.minecraft.tropical_fish.predefined.10";
        return "entity.minecraft.tropical_fish.type." + pattern.toString().toLowerCase();
      case KOB:
        if (bodyColor == DyeColor.RED && patternColor == DyeColor.WHITE)
          return "entity.minecraft.tropical_fish.predefined.18";
        if (bodyColor == DyeColor.ORANGE && patternColor == DyeColor.WHITE)
          return "entity.minecraft.tropical_fish.predefined.5";
        return "entity.minecraft.tropical_fish.type." + pattern.toString().toLowerCase();
      case SNOOPER:
        if (bodyColor == DyeColor.GRAY && patternColor == DyeColor.RED)
          return "entity.minecraft.tropical_fish.predefined.15";
        return "entity.minecraft.tropical_fish.type." + pattern.toString().toLowerCase();
      case SPOTTY:
        if (bodyColor == DyeColor.WHITE && patternColor == DyeColor.YELLOW)
          return "entity.minecraft.tropical_fish.predefined.9";
        if (bodyColor == DyeColor.PINK && patternColor == DyeColor.LIGHT_BLUE)
          return "entity.minecraft.tropical_fish.predefined.6";
        return "entity.minecraft.tropical_fish.type." + pattern.toString().toLowerCase();
      case STRIPEY:
        if (bodyColor == DyeColor.ORANGE && patternColor == DyeColor.GRAY)
          return "entity.minecraft.tropical_fish.predefined.0";
        return "entity.minecraft.tropical_fish.type." + pattern.toString().toLowerCase();
      case SUNSTREAK:
        if (bodyColor == DyeColor.BLUE && patternColor == DyeColor.GRAY)
          return "entity.minecraft.tropical_fish.predefined.4";
        if (bodyColor == DyeColor.GRAY && patternColor == DyeColor.WHITE)
          return "entity.minecraft.tropical_fish.predefined.19";
        return "entity.minecraft.tropical_fish.type." + pattern.toString().toLowerCase();
      default:
        break;
    }
    return "";
  }
}

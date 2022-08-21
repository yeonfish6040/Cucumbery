package com.jho5245.cucumbery.util.storage.no_groups;

import org.bukkit.Color;
import org.bukkit.DyeColor;

public class ColorCode
{
  public static String getColorCode(DyeColor color)
  {
    return switch (color)
            {
              case BLACK -> "#626262;";
              case BLUE -> "#0065ff;";
              case BROWN -> "#7b4a2c;";
              case CYAN -> "#00b4ad;";
              case GRAY -> "#828282;";
              case GREEN -> "#4c7617;";
              case LIGHT_BLUE -> "#00d8ff;";
              case LIGHT_GRAY -> "#b4b4b4;";
              case LIME -> "#90db00;";
              case MAGENTA -> "#c800de;";
              case ORANGE -> "#ff7500;";
              case PINK -> "#ffa8c2;";
              case PURPLE -> "#af00ff;";
              case RED -> "#dd3103;";
              case WHITE -> "#f5f5f5;";
              case YELLOW -> "#ffd601;";
            };
  }

  public static String getColorName(Color color)
  {
    return getColorName(color, "");
  }

  public static String getColorName(Color color, String key)
  {
    int red = color.getRed(), green = color.getGreen(), blue = color.getBlue();
    if (red == 179 && green == 49 && blue == 44)
      return "rgb179,49,44;" + key + "red";
    if (red == 59 && green == 81 && blue == 26)
      return "rgb59,81,26;" + key + "green";
    if (red == 123 && green == 47 && blue == 190)
      return "rgb123,47,190;" + key + "purple";
    if (red == 40 && green == 118 && blue == 151)
      return "rgb40,118,151;" + key + "cyan";
    if (red == 171 && green == 171 && blue == 171)
      return "rgb171,171,171;" + key + "light_gray";
    if (red == 67 && green == 67 && blue == 67)
      return "rgb67,67,67;" + key + "gray";
    if (red == 216 && green == 129 && blue == 152)
      return "rgb216,129,152;" + key + "pink";
    if (red == 65 && green == 205 && blue == 52)
      return "rgb65,205,52;" + key + "lime";
    if (red == 222 && green == 207 && blue == 42)
      return "rgb222,207,42;" + key + "yellow";
    if (red == 102 && green == 137 && blue == 211)
      return "rgb102,137,211;" + key + "light_blue";
    if (red == 195 && green == 84 && blue == 205)
      return "rgb195,84,205;" + key + "magenta";
    if (red == 235 && green == 136 && blue == 68)
      return "rgb235,136,68;" + key + "orange";
    if (red == 37 && green == 49 && blue == 146)
      return "rgb37,49,146;" + key + "blue";
    if (red == 81 && green == 48 && blue == 26)
      return "rgb81,48,26;" + key + "brown";
    if (red == 27 && green == 27 && blue == 27)
      return "rgb30,27,27;" + key + "black";
    if (red == 240 && green == 240 && blue == 240)
      return "rgb240,240,240;" + key + "white";
    return "rgb" + red + "," + green + "," + blue + ";" + key + "custom_color";
  }

  public static String getColorCode(Color color)
  {
    int red = color.getRed(), green = color.getGreen(), blue = color.getBlue();
    return "rgb" + red + "," + green + "," + blue + ";";
  }
}

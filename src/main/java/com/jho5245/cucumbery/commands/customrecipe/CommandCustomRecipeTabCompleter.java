package com.jho5245.cucumbery.commands.customrecipe;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.*;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CommandCustomRecipeTabCompleter implements TabCompleter
{
  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(true, args), true))
    {
      return Collections.singletonList(args[0]);
    }
    int length = args.length;
    String lastArg = args[length - 1];
    switch (length)
    {
      case 1:
        return Method.tabCompleterList(args, "<인수>", "open", "create", "remove", "edit");
      case 2:
        switch (args[0])
        {
          case "open":
          case "remove":
            if (Variable.customRecipes.isEmpty())
            {
              return Collections.singletonList((args[0].equals("open") ? "열" : "제거할") + " 수 있는 유효한 레시피 목록이 존재하지 않습니다");
            }
            return Method.tabCompleterList(args, Variable.customRecipes.keySet(), "<레시피 목록>");
          case "create":
            if (Method2.isInvalidFileName(args[1]))
            {
              return Collections.singletonList(args[1] + MessageUtil.getFinalConsonant(args[1], MessageUtil.ConsonantType.은는) + " 유효하지 않은 레시피 목록 이름입니다");
            }
            if (Variable.customRecipes.isEmpty())
            {
              return Method.tabCompleterList(args, "<새로운 레시피 목록>", true);
            }
            return Method.tabCompleterList(args, Variable.customRecipes.keySet(), (Variable.customRecipes.containsKey(lastArg) ? "<편집할 레시피 목록>" : "<새로운 레시피 목록>"), true);
          case "edit":
            return Method.tabCompleterList(args, "<인수>", "category", "recipe");
        }
        break;
      case 3:
        switch (args[0])
        {
          case "open":
          case "remove":
          {
            YamlConfiguration config = Variable.customRecipes.get(args[1]);
            if (config == null)
            {
              return Collections.singletonList(args[1] + MessageUtil.getFinalConsonant(args[1], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피 목록입니다");
            }
            ConfigurationSection recipes = config.getConfigurationSection("recipes");
            if (recipes == null)
            {
              return Collections.singletonList((args[0].equals("open") ? "열" : "제거할") + " 수 있는 유효한 레시피가 존재하지 않습니다");
            }
            return Method.tabCompleterList(args, recipes.getKeys(false), "<레시피>");
          }
          case "create":
          {
            YamlConfiguration config = Variable.customRecipes.get(args[1]);
            if (config == null)
            {
              return Method.tabCompleterList(args, "<새로운 레시피>", true);
            }
            ConfigurationSection recipes = config.getConfigurationSection("recipes");
            if (recipes == null || recipes.getKeys(false).isEmpty())
            {
              return Method.tabCompleterList(args, "<새로운 레시피>", true);
            }
            return Method.tabCompleterList(args, recipes.getKeys(false), (recipes.getKeys(false).contains(lastArg) ? "<편집할 레시피>" : "<새로운 레시피>"), true);
          }
          case "edit":
            switch (args[1])
            {
              case "category", "recipe" -> {
                if (Variable.customRecipes.isEmpty())
                {
                  return Collections.singletonList("유효한 레시피 목록이 없습니다");
                }
                return Method.tabCompleterList(args, Variable.customRecipes.keySet(), "<레시피 목록>");
              }
            }
            break;
        }
        break;
      case 4:
        switch (args[0])
        {
          case "open":
            return Method.tabCompleterPlayer(sender, args);
          case "edit":
            switch (args[1])
            {
              case "category":
                return Method.tabCompleterList(
                        args, "<인수>", "permission", "wealth", "level", "require", "statistic", "hp", "mhp", "maxplayer", "foodlevel", "saturation", "display", "displayitem", "biome", "belowblock", "target-block", "decorative");
              case "recipe":
                YamlConfiguration config = Variable.customRecipes.get(args[2]);
                if (config == null)
                {
                  return Collections.singletonList(args[2] + MessageUtil.getFinalConsonant(args[2], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피 목록입니다");
                }
                ConfigurationSection recipes = config.getConfigurationSection("recipes");
                if (recipes == null)
                {
                  return Collections.singletonList(args[2] + " 레시피 목록에는 유효한 레시피가 없습니다");
                }
                return Method.tabCompleterList(args, recipes.getKeys(false), "<레시피>");
            }
            break;
        }
        break;
      case 5:
        if ("edit".equals(args[0]))
        {
          switch (args[1])
          {
            case "category":
              switch (args[3])
              {
                case "permission":
                  return Method.tabCompleterList(args, "<인수>", "base", "bypass", "hide");
                case "require":
                  return Method.tabCompleterList(args, "<제작 횟수 유형>", "recipe", "category");
                case "statistic":
                  return Method.tabCompleterList(args, "<통계 유형>", "general", "entity", "material");
                case "displayitem":
                  boolean holdingItem = false;
                  String hand = "hand";
                  if (!(sender instanceof Player))
                  {
                    hand = "hand(플레이어만 사용 가능)";
                  }
                  else
                  {
                    Player player = (Player) sender;
                    holdingItem = ItemStackUtil.itemExists(player.getInventory().getItemInMainHand());
                    if (!holdingItem)
                    {
                      hand = "hand(손에 아이템 없음)";
                    }
                  }
                  if (!holdingItem && lastArg.equals("hand"))
                  {
                    return Collections.singletonList(Prefix.NO_HOLDING_ITEM.toString());
                  }
                  return Method.tabCompleterList(args, "<표시 아이템 출처>", hand, "material", "remove");
                case "biome":
                  return Method.tabCompleterList(args, Method.addAll(Biome.values(), "--remove"), "<생물 군계>");
                case "belowblock", "target-block":
                  List<String> list = new ArrayList<>();
                  for (Material material : Material.values())
                  {
                    if (material.isBlock())
                    {
                      list.add(material.toString().toLowerCase());
                    }
                  }
                  return Method.tabCompleterList(args, Method.addAll(list, "--remove"), "<블록>");
                case "display":
                  return Method.tabCompleterList(args, "<레시피 목록 표시 이름>", true, "<레시피 목록 표시 이름>", "--remove");
                case "foodlevel":
                  return Method.tabCompleterIntegerRadius(args, 0, 20, "<현재 음식 포인트의 최소 조건>", "-1");
                case "level":
                  return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<현재 레벨의 최소 조건>", "-1");
                case "maxplayer":
                  return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<현재 접속 중인 플레이어 수의 최소 조건>", "-1");
                case "hp":
                  return Method.tabCompleterDoubleRadius(args, 0, Double.MAX_VALUE, "<현재 HP의 최소 조건>", "-1");
                case "mhp":
                  return Method.tabCompleterDoubleRadius(args, 0, Double.MAX_VALUE, "<현재 순수 최대 HP의 최소 조건>", "-1");
                case "saturation":
                  return Method.tabCompleterDoubleRadius(args, 0, 20, "<현재 포화도의 최소 조건>", "-1");
                case "wealth":
                  if (!Cucumbery.using_Vault_Economy)
                  {
                    return Collections.singletonList("Vault 플러그인을 사용하고 있지 않습니다");
                  }
                  return Method.tabCompleterDoubleRadius(args, 0, Double.MAX_VALUE, "<현재 소지 금액의 최소 조건>", "-1");
                case "decorative":
                  return Method.tabCompleterBoolean(args, "<꾸미기용 목록 모드>");
              }
              break;
            case "recipe":
              List<String> list = new ArrayList<>(Method.tabCompleterList(args, "<인수>", "permission", "chance", "reusable", "cost", "wealth", "levelcost", "level", "display", "require", "statistic",
                      "hp", "mhp", "maxplayer", "foodlevel", "saturation", "hpcost", "mhpcost", "foodlevelcost", "saturationcost", "craftingtime",
                      "command", "biome", "belowblock", "target-block", "decorative"));
              if (!Cucumbery.using_Vault_Economy)
              {
                list.remove("cost");
                list.remove("wealth");
                if (Method.equals(lastArg, "cost", "wealth"))
                {
                  return Collections.singletonList("Vault 플러그인을 사용하고 있지 않습니다");
                }
              }
              return list;
          }
        }
        break;
      case 6:
        if ("edit".equals(args[0]))
        {
          switch (args[1])
          {
            case "category":
              switch (args[3])
              {
                case "permission":
                  switch (args[4])
                  {
                    case "hide":
                      return Method.tabCompleterBoolean(args, "<권한 부족 시 숨김 여부>");
                    case "bypass":
                    case "base":
                      return Method.tabCompleterList(args, "<퍼미션 노드>", true, "<퍼미션 노드>", "--remove");
                  }
                  break;
                case "require":
                  switch (args[4])
                  {
                    case "recipe":
                    case "category":
                      return Method.tabCompleterList(args, Variable.customRecipes.keySet(), "<레시피 목록>");
                  }
                  break;
                case "statistic":
                  return Method.tabCompleterStatistics(args, args[4], "<통계>");
                case "displayitem":
                  if ("material".equals(args[4]))
                  {
                    List<String> list = new ArrayList<>();
                    for (Material material : Material.values())
                    {
                      if (material.isItem() && material != Material.AIR)
                      {
                        list.add(material.toString().toLowerCase());
                      }
                    }
                    return Method.tabCompleterList(args, list, "<아이템>");
                  }
                  break;
                case "foodlevel":
                {
                  int min = 0;
                  String minStr = args[4];
                  if (MessageUtil.isInteger(sender, minStr, false))
                  {
                    min = Math.max(0, Integer.parseInt(minStr));
                  }
                  return Method.tabCompleterIntegerRadius(args, min, 20, "[현재 음식 포인트의 최대 조건]", "-1", min + "");
                }
                case "level":
                {
                  int min = 0;
                  String minStr = args[4];
                  if (MessageUtil.isInteger(sender, minStr, false))
                  {
                    min = Math.max(0, Integer.parseInt(minStr));
                  }
                  return Method.tabCompleterIntegerRadius(args, min, Integer.MAX_VALUE, "[현재 레벨의 최대 조건]", "-1", min + "");
                }
                case "maxplayer":
                {
                  int min = 0;
                  String minStr = args[4];
                  if (MessageUtil.isInteger(sender, minStr, false))
                  {
                    min = Math.max(0, Integer.parseInt(minStr));
                  }
                  return Method.tabCompleterIntegerRadius(args, min, Integer.MAX_VALUE, "[현재 접속 중인 플레이어 수의 최대 조건]", "-1", min + "");
                }
                case "hp":
                {
                  double min = 0;
                  String minStr = args[4];
                  if (MessageUtil.isDouble(sender, minStr, false))
                  {
                    min = Math.max(0, Double.parseDouble(minStr));
                  }
                  return Method.tabCompleterDoubleRadius(args, min, Double.MAX_VALUE, "[현재 HP의 최대 조건]", "-1", min + "");
                }
                case "mhp":
                {
                  double min = 0;
                  String minStr = args[4];
                  if (MessageUtil.isDouble(sender, minStr, false))
                  {
                    min = Math.max(0, Double.parseDouble(minStr));
                  }
                  return Method.tabCompleterDoubleRadius(args, min, Double.MAX_VALUE, "[현재 순수 최대 HP의 최대 조건]", "-1", min + "");
                }
                case "saturation":
                {
                  double min = 0;
                  String minStr = args[4];
                  if (MessageUtil.isDouble(sender, minStr, false))
                  {
                    min = Math.max(0, Double.parseDouble(minStr));
                  }
                  return Method.tabCompleterDoubleRadius(args, min, 20, "[현재 포화도의 최대 조건]", "-1", min + "");
                }
                case "wealth":
                {
                  if (!Cucumbery.using_Vault_Economy)
                  {
                    return Collections.singletonList("Vault 플러그인을 사용하고 있지 않습니다");
                  }
                  double min = 0;
                  String minStr = args[4];
                  if (MessageUtil.isDouble(sender, minStr, false))
                  {
                    min = Math.max(0, Double.parseDouble(minStr));
                  }
                  return Method.tabCompleterDoubleRadius(args, min, Double.MAX_VALUE, "[현재 소지 금액의 최대 조건]", "-1", min + "");
                }
              }
              break;
            case "recipe":
              switch (args[4])
              {
                case "permission":
                  return Method.tabCompleterList(args, "<인수>", "base", "bypass", "hide");
                case "reusable":
                {
                  YamlConfiguration config = Variable.customRecipes.get(args[2]);
                  if (config == null)
                  {
                    return Collections.singletonList(args[2] + MessageUtil.getFinalConsonant(args[2], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피입니다");
                  }
                  ConfigurationSection ingredients = config.getConfigurationSection("recipes." + args[3] + ".ingredients");
                  if (ingredients == null)
                  {
                    return Collections.singletonList(args[2] + " 레시피는 손상된 레시피입니다");
                  }
                  Set<String> keys = ingredients.getKeys(false);
                  List<String> list = new ArrayList<>();
                  int i = 1;
                  for (String key : keys)
                  {
                    String ingredientName = ItemNameUtil.itemName(ItemSerializer.deserialize(ingredients.getString(key + ".item"))).toString();
                    list.add(i + " : " + MessageUtil.stripColor(ingredientName));
                    i++;
                  }
                  return Method.tabCompleterList(args, list, "<재료(숫자)>");
                }
                case "require":
                  return Method.tabCompleterList(args, "<제작 횟수 유형>", "recipe", "category");
                case "statistic":
                  return Method.tabCompleterList(args, "<통계 유형>", "general", "entity", "material");
                case "craftingtime":
                  return Method.tabCompleterList(args, "<인수>", "time", "skip", "interval");
                case "command":
                  return Method.tabCompleterList(args, "<실행 시점>", "craft", "failure", "success");
                case "biome":
                  return Method.tabCompleterList(args, Method.addAll(Biome.values(), "--remove"), "<생물 군계>");
                case "belowblock", "target-block":
                {
                  List<String> list = new ArrayList<>();
                  for (Material material : Material.values())
                  {
                    if (material.isBlock())
                    {
                      list.add(material.toString().toLowerCase());
                    }
                  }
                  return Method.tabCompleterList(args, Method.addAll(list, "--remove"), "<블록>");
                }
                case "display":
                  return Method.tabCompleterList(args, "<레시피 표시 이름>", true, "<레시피 표시 이름>", "--remove");
                case "chance":
                  return Method.tabCompleterDoubleRadius(args, 0, 100, "<제작 성공 확률(%)>");
                case "cost":
                  if (!Cucumbery.using_Vault_Economy)
                  {
                    return Collections.singletonList("Vault 플러그인을 사용하고 있지 않습니다");
                  }
                  return Method.tabCompleterDoubleRadius(args, 0, 100, "<제작 비용>");
                case "foodlevelcost":
                  return Method.tabCompleterIntegerRadius(args, 0, 20, "<제작 요구 음식 포인트 비용>");
                case "levelcost":
                  return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<제작 요구 레벨>");
                case "hpcost":
                  return Method.tabCompleterDoubleRadius(args, 0, Double.MAX_VALUE, "<제작 요구 HP>");
                case "mhpcost":
                  return Method.tabCompleterDoubleRadius(args, 0, Double.MAX_VALUE, "<제작 요구 순수 최대 HP>");
                case "saturationcost":
                  return Method.tabCompleterDoubleRadius(args, 0, 20, "<제작 요구 포화도 비용>");
                case "foodlevel":
                  return Method.tabCompleterIntegerRadius(args, 0, 20, "<현재 음식 포인트의 최소 조건>", "-1");
                case "level":
                  return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<현재 레벨의 최소 조건>", "-1");
                case "maxplayer":
                  return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<현재 접속 중인 플레이어 수의 최소 조건>", "-1");
                case "hp":
                  return Method.tabCompleterDoubleRadius(args, 0, Double.MAX_VALUE, "<현재 HP의 최소 조건>", "-1");
                case "mhp":
                  return Method.tabCompleterDoubleRadius(args, 0, Double.MAX_VALUE, "<현재 순수 최대 HP의 최소 조건>", "-1");
                case "saturation":
                  return Method.tabCompleterDoubleRadius(args, 0, 20, "<현재 포화도의 최소 조건>", "-1");
                case "wealth":
                  if (!Cucumbery.using_Vault_Economy)
                  {
                    return Collections.singletonList("Vault 플러그인을 사용하고 있지 않습니다");
                  }
                  return Method.tabCompleterDoubleRadius(args, 0, Double.MAX_VALUE, "<현재 소지 금액의 최소 조건>", "-1");
                case "decorative":
                {
                  return Method.tabCompleterBoolean(args, "<꾸미기용 레시피 모드>");
                }
              }
              break;
          }
        }
        break;
      case 7:
        if ("edit".equals(args[0]))
        {
          switch (args[1])
          {
            case "recipe":
              switch (args[4])
              {
                case "permission":
                  switch (args[5])
                  {
                    case "hide":
                      return Method.tabCompleterBoolean(args, "<권한 부족 시 숨김 여부>");
                    case "bypass":
                    case "base":
                      return Method.tabCompleterList(args, "<퍼미션 노드>", true, "<퍼미션 노드>", "--remove");
                  }
                  break;
                case "reusable":
                  return Method.tabCompleterBoolean(args, "<제작 시 사라지지 않는 아이템 태그>");
                case "require":
                  switch (args[5])
                  {
                    case "recipe":
                    case "category":
                      return Method.tabCompleterList(args, Variable.customRecipes.keySet(), "<레시피 목록>");
                  }
                  break;
                case "statistic":
                  return Method.tabCompleterStatistics(args, args[5], "<통계>");
                case "craftingtime":
                  switch (args[5])
                  {
                    case "skip":
                      if (!Cucumbery.using_Vault_Economy)
                      {
                        return Collections.singletonList("Vault 플러그인을 사용하고 있지 않습니다");
                      }
                      return Method.tabCompleterList(args, "<인수>", "cost", "permission", "relative");
                    case "time":
                      return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<제작에 필요한 시간(초)>");
                    case "interval":
                      return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<제작 주기(초)>");
                  }
                  break;
                case "command":
                  if (Method.equals(args[5], "<인수>", "craft", "failure", "success"))
                  {
                    return Method.tabCompleterList(args, "<인수>", "list", "add", "remove", "set", "insert");
                  }
                  break;
                case "foodlevel":
                {
                  int min = 0;
                  String minStr = args[5];
                  if (MessageUtil.isInteger(sender, minStr, false))
                  {
                    min = Math.max(0, Integer.parseInt(minStr));
                  }
                  return Method.tabCompleterIntegerRadius(args, min, 20, "[현재 음식 포인트의 최대 조건]", "-1", min + "");
                }
                case "level":
                {
                  int min = 0;
                  String minStr = args[5];
                  if (MessageUtil.isInteger(sender, minStr, false))
                  {
                    min = Math.max(0, Integer.parseInt(minStr));
                  }
                  return Method.tabCompleterIntegerRadius(args, min, Integer.MAX_VALUE, "[현재 레벨의 최대 조건]", "-1", min + "");
                }
                case "maxplayer":
                {
                  int min = 0;
                  String minStr = args[5];
                  if (MessageUtil.isInteger(sender, minStr, false))
                  {
                    min = Math.max(0, Integer.parseInt(minStr));
                  }
                  return Method.tabCompleterIntegerRadius(args, min, Integer.MAX_VALUE, "[현재 접속 중인 플레이어 수의 최대 조건]", "-1", min + "");
                }
                case "hp":
                {
                  double min = 0;
                  String minStr = args[5];
                  if (MessageUtil.isDouble(sender, minStr, false))
                  {
                    min = Math.max(0, Double.parseDouble(minStr));
                  }
                  return Method.tabCompleterDoubleRadius(args, min, Double.MAX_VALUE, "[현재 HP의 최대 조건]", "-1", min + "");
                }
                case "mhp":
                {
                  double min = 0;
                  String minStr = args[5];
                  if (MessageUtil.isDouble(sender, minStr, false))
                  {
                    min = Math.max(0, Double.parseDouble(minStr));
                  }
                  return Method.tabCompleterDoubleRadius(args, min, Double.MAX_VALUE, "[현재 순수 최대 HP의 최대 조건]", "-1", min + "");
                }
                case "saturation":
                {
                  double min = 0;
                  String minStr = args[5];
                  if (MessageUtil.isDouble(sender, minStr, false))
                  {
                    min = Math.max(0, Double.parseDouble(minStr));
                  }
                  return Method.tabCompleterDoubleRadius(args, min, 20, "[현재 포화도의 최대 조건]", "-1", min + "");
                }
                case "wealth":
                {
                  if (!Cucumbery.using_Vault_Economy)
                  {
                    return Collections.singletonList("Vault 플러그인을 사용하고 있지 않습니다");
                  }
                  double min = 0;
                  String minStr = args[5];
                  if (MessageUtil.isDouble(sender, minStr, false))
                  {
                    min = Math.max(0, Double.parseDouble(minStr));
                  }
                  return Method.tabCompleterDoubleRadius(args, min, Double.MAX_VALUE, "[현재 소지 금액의 최대 조건]", "-1", min + "");
                }
              }
              break;
            case "category":
              switch (args[3])
              {
                case "require":
                  if ("recipe".equals(args[4]))
                  {
                    YamlConfiguration config = Variable.customRecipes.get(args[5]);
                    if (config == null)
                    {
                      return Collections.singletonList(args[5] + MessageUtil.getFinalConsonant(args[5], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피 목록입니다");
                    }
                    ConfigurationSection recipes = config.getConfigurationSection("recipes");
                    if (recipes == null)
                    {
                      return Collections.singletonList(args[5] + " 레시피 목록에는 유효한 레시피가 없습니다");
                    }
                    return Method.tabCompleterList(args, recipes.getKeys(false), "<레시피>");
                  }
                  if ("category".equals(args[4]))
                  {
                    YamlConfiguration config = Variable.customRecipes.get(args[5]);
                    if (config == null)
                    {
                      return Collections.singletonList(args[5] + MessageUtil.getFinalConsonant(args[5], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피 목록입니다");
                    }
                    String requireCategoryDisplay = config.getString("extra.display");
                    if (requireCategoryDisplay == null)
                    {
                      requireCategoryDisplay = args[5];
                    }
                    requireCategoryDisplay = MessageUtil.stripColor(requireCategoryDisplay);
                    return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + requireCategoryDisplay + " 레시피 목록에 있는 레시피의 최소 제작 횟수 조건>", "-1");
                  }
                  break;
                case "statistic":
                  switch (args[4])
                  {
                    case "material":
                      if (args[5].equals("mine_block"))
                      {
                        List<String> list = new ArrayList<>();
                        for (Material material : Material.values())
                        {
                          if (material.isBlock())
                          {
                            list.add(material.toString().toLowerCase());
                          }
                        }
                        return Method.tabCompleterList(args, list, "<블록>");
                      }
                      else if (args[5].equals("break_item"))
                      {
                        List<String> list = new ArrayList<>();
                        for (Material material : Constant.DURABLE_ITEMS)
                        {
                          list.add(material.toString().toLowerCase());
                        }
                        return Method.tabCompleterList(args, list, "<내구도가 있는 아이템>");
                      }
                      else if (Method.equals(args[5], "use_item", "drop", "pickup", "craft_item"))
                      {
                        List<String> list = new ArrayList<>();
                        for (Material material : Material.values())
                        {
                          if (material.isItem() && material != Material.AIR)
                          {
                            list.add(material.toString().toLowerCase());
                          }
                        }
                        return Method.tabCompleterList(args, list, "<아이템>");
                      }
                      break;
                    case "entity":
                      List<String> list = new ArrayList<>();
                      for (EntityType entityType : EntityType.values())
                      {
                        if (entityType.isAlive())
                        {
                          list.add(entityType.toString().toLowerCase());
                        }
                      }
                      return Method.tabCompleterList(args, list, "<개체>");
                    case "general":
                      String typeValue = args[5];
                      try
                      {
                        typeValue = Statistic.valueOf(typeValue.toUpperCase()).toString();
                      }
                      catch (Exception ignored)
                      {
                      }
                      return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + typeValue + "의 최솟값 조건>", "-1");
                  }
                  break;
              }
              break;
          }
        }
        break;
      case 8:
        if ("edit".equals(args[0]))
        {
          if ("category".equals(args[1]))
          {
            if ("statistic".equals(args[3]))
            {
              String typeValue = args[6];
              Material material = null;
              switch (args[4])
              {
                case "entity":
                  try
                  {
                    typeValue = MessageUtil.stripColor((EntityType.valueOf(typeValue.toUpperCase())).toString());
                  }
                  catch (Exception ignored)
                  {

                  }
                  break;
                case "material":
                  try
                  {
                    material = Material.valueOf(typeValue.toUpperCase());
                    typeValue = MessageUtil.stripColor(ItemNameUtil.itemName(material).toString());
                  }
                  catch (Exception ignored)
                  {

                  }
                  break;
              }
              switch (args[4])
              {
                case "material":
                  switch (args[5])
                  {
                    case "break_item":
                      return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 망가뜨린 최소 횟수 조건>", "-1");
                    case "craft_item":
                      return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 제작한 최소 횟수 조건>", "-1");
                    case "drop":
                      return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 떨어뜨린 최소 횟수 조건>", "-1");
                    case "pickup":
                      return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 주운 최소 횟수 조건>", "-1");
                    case "mine_block":
                      return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 채굴한 최소 횟수 조건>", "-1");
                    case "use_item":
                      String useType = "사용";
                      if (material.isBlock())
                      {
                        useType = "설치";
                      }
                      if (ItemStackUtil.isEdible(material))
                      {
                        useType = "섭취";
                      }
                      return Method.tabCompleterIntegerRadius(
                              args, 0, Integer.MAX_VALUE, "<" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " " + useType + "한 최소 횟수 조건>", "-1");
                  }
                  break;
                case "entity":
                  switch (args[5])
                  {
                    case "entity_killed_by":
                      return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + typeValue + "에게 죽은 최소 횟수 조건>", "-1");
                    case "kill_entity":
                      return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 죽인 최소 횟수 조건>", "-1");
                  }
                  break;
                case "general":
                  int min = 0;
                  if (MessageUtil.isInteger(sender, args[6], false))
                  {
                    min = Integer.parseInt(args[6]);
                  }
                  typeValue = args[5];
                  try
                  {
                    typeValue = Statistic.valueOf(typeValue.toUpperCase()).toString();
                  }
                  catch (Exception ignored)
                  {
                  }
                  return Method.tabCompleterIntegerRadius(args, min, Integer.MAX_VALUE, "[" + typeValue + "의 최댓값 조건]", "-1", min + "");
              }
            }
            if ("require".equals(args[3]))
            {
              if ("category".equals(args[4]))
              {
                YamlConfiguration config = Variable.customRecipes.get(args[5]);
                if (config == null)
                {
                  return Collections.singletonList(args[5] + MessageUtil.getFinalConsonant(args[5], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피 목록입니다");
                }
                String requireCategoryDisplay = config.getString("extra.display");
                if (requireCategoryDisplay == null)
                {
                  requireCategoryDisplay = args[5];
                }
                requireCategoryDisplay = MessageUtil.stripColor(requireCategoryDisplay);
                int min = 0;
                if (MessageUtil.isInteger(sender, args[6], false))
                {
                  min = Integer.parseInt(args[6]);
                }
                return Method.tabCompleterIntegerRadius(args, min, Integer.MAX_VALUE, "[" + requireCategoryDisplay + " 레시피 목록에 있는 레시피의 최대 제작 횟수 조건]", "-1", min + "");
              }
              if ("recipe".equals(args[4]))
              {
                YamlConfiguration config = Variable.customRecipes.get(args[5]);
                if (config == null)
                {
                  return Collections.singletonList(args[5] + MessageUtil.getFinalConsonant(args[5], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피 목록입니다");
                }
                ConfigurationSection recipes = config.getConfigurationSection("recipes");
                if (recipes == null)
                {
                  return Collections.singletonList(args[5] + " 레시피 목록에는 유효한 레시피가 없습니다");
                }
                if (config.getConfigurationSection("recipes." + args[6]) == null)
                {
                  return Collections.singletonList(args[6] + MessageUtil.getFinalConsonant(args[6], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피입니다");
                }
                String requireCategoryDisplay = config.getString("extra.display");
                if (requireCategoryDisplay == null)
                {
                  requireCategoryDisplay = args[5];
                }
                requireCategoryDisplay = MessageUtil.stripColor(requireCategoryDisplay);
                String requireRecipeDisplay = config.getString("recipes." + args[6] + ".extra.display");
                if (requireRecipeDisplay == null)
                {
                  requireRecipeDisplay = args[6];
                }
                requireRecipeDisplay = MessageUtil.stripColor(requireRecipeDisplay);
                return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + requireCategoryDisplay + " 레시피 목록에 있는 " + requireRecipeDisplay + " 레시피의 최소 제작 횟수 조건>", "-1");
              }
            }
          }
          if ("recipe".equals(args[1]))
          {
            switch (args[4])
            {
              case "require":
                if ("recipe".equals(args[5]))
                {
                  FileConfiguration config = Variable.customRecipes.get(args[6]);
                  if (config == null)
                  {
                    return Collections.singletonList(args[6] + MessageUtil.getFinalConsonant(args[6], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피 목록입니다");
                  }
                  ConfigurationSection recipes = config.getConfigurationSection("recipes");
                  if (recipes == null)
                  {
                    return Collections.singletonList(args[6] + " 레시피 목록에는 유효한 레시피가 없습니다");
                  }
                  return Method.tabCompleterList(args, recipes.getKeys(false), "<레시피>");
                }
                if ("category".equals(args[5]))
                {
                  YamlConfiguration config = Variable.customRecipes.get(args[6]);
                  if (config == null)
                  {
                    return Collections.singletonList(args[5] + MessageUtil.getFinalConsonant(args[6], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피 목록입니다");
                  }
                  String requireCategoryDisplay = config.getString("extra.display");
                  if (requireCategoryDisplay == null)
                  {
                    requireCategoryDisplay = args[6];
                  }
                  requireCategoryDisplay = MessageUtil.stripColor(requireCategoryDisplay);
                  return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + requireCategoryDisplay + " 레시피 목록에 있는 레시피의 최소 제작 횟수 조건>", "-1");
                }
                break;
              case "statistic":
                switch (args[5])
                {
                  case "material":
                    if (args[6].equals("mine_block"))
                    {
                      List<String> list = new ArrayList<>();
                      for (Material material : Material.values())
                      {
                        if (material.isBlock())
                        {
                          list.add(material.toString().toLowerCase());
                        }
                      }
                      return Method.tabCompleterList(args, list, "<블록>");
                    }
                    else if (args[6].equals("break_item"))
                    {
                      List<String> list = new ArrayList<>();
                      for (Material material : Constant.DURABLE_ITEMS)
                      {
                        list.add(material.toString().toLowerCase());
                      }
                      return Method.tabCompleterList(args, list, "<내구도가 있는 아이템>");
                    }
                    else if (Method.equals(args[6], "use_item", "drop", "pickup", "craft_item"))
                    {
                      List<String> list = new ArrayList<>();
                      for (Material material : Material.values())
                      {
                        if (material.isItem() && material != Material.AIR)
                        {
                          list.add(material.toString().toLowerCase());
                        }
                      }
                      return Method.tabCompleterList(args, list, "<아이템>");
                    }
                    return Method.tabCompleterList(args, Material.values(), "<물질>");
                  case "entity":
                    List<String> list = new ArrayList<>();
                    for (EntityType entityType : EntityType.values())
                    {
                      if (entityType.isAlive())
                      {
                        list.add(entityType.toString().toLowerCase());
                      }
                    }
                    return Method.tabCompleterList(args, list, "<개체>");
                  case "general":
                    String typeValue = args[6];
                    try
                    {
                      typeValue = Statistic.valueOf(typeValue.toUpperCase()).toString();
                    }
                    catch (Exception ignored)
                    {
                    }
                    return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + typeValue + "의 최솟값 조건>", "-1");
                }
              case "craftingtime":
                if ("skip".equals(args[5]))
                {
                  if (!Cucumbery.using_Vault_Economy)
                  {
                    return Collections.singletonList("Vault 플러그인을 사용하고 있지 않습니다");
                  }
                  switch (args[6])
                  {
                    case "cost":
                      return Method.tabCompleterDoubleRadius(args, 0, Double.MAX_VALUE, "<제작 시간 스킵 비용>");
                    case "relative":
                      return Method.tabCompleterBoolean(args, "<스킵 비용 시간 비례 적용 여부>");
                    case "permission":
                      return Method.tabCompleterList(args, "<제작 시간 스킵 요구 퍼미션 노드>", true, "<제작 시간 스킵 요구 퍼미션 노드>", "--remove");
                  }
                }
                break;
              case "command":
                if (Method.equals(args[5], "craft", "failure", "success"))
                {
                  String commandTypeStr = "";
                  FileConfiguration config = Variable.customRecipes.get(args[2]);
                  if (config == null)
                  {
                    return Collections.singletonList(args[2] + MessageUtil.getFinalConsonant(args[2], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피 목록입니다");
                  }
                  List<String> commands = config == null ? null : config.getStringList("recipes." + args[3] + ".extra.commands." + args[5]);
                  switch (args[5])
                  {
                    case "craft":
                      commandTypeStr = "제작 완료";
                      break;
                    case "success":
                      commandTypeStr = "제작 성공";
                      break;
                    case "failure":
                      commandTypeStr = "제작 실패";
                      break;
                  }
                  switch (args[6])
                  {
                    case "remove":
                      if (commands.isEmpty())
                      {
                        return Collections.singletonList(args[2] + " 레시피 목록의 " + args[3] + " 레시피에는 " + commandTypeStr + " 시 실행되는 명령어가 없습니다");
                      }
                      return Method.tabCompleterIntegerRadius(args, 1, commands.size(), "[줄]", "--all");
                    case "set":
                      if (length == 8)
                      {
                        if (commands.isEmpty())
                        {
                          return Collections.singletonList(args[2] + " 레시피 목록의 " + args[3] + " 레시피에는 " + commandTypeStr + " 시 실행되는 명령어가 없습니다");
                        }
                      }
                      return Method.tabCompleterIntegerRadius(args, 1, commands.size(), "<줄>");
                    case "insert":
                      if (length == 8)
                      {
                        if (commands.isEmpty())
                        {
                          return Collections.singletonList(args[2] + " 레시피 목록의 " + args[3] + " 레시피에는 " + commandTypeStr + " 시 실행되는 명령어가 없습니다");
                        }
                        return Method.tabCompleterIntegerRadius(args, 1, commands.size(), "[줄]", "--all");
                      }
                  }
                }
                break;
            }
          }
        }
        break;
      case 9:
        if ("edit".equals(args[0]))
        {
          if ("category".equals(args[1]))
          {
            if ("statistic".equals(args[3]))
            {
              String typeValue = args[6];
              Material material = null;
              switch (args[4])
              {
                case "entity":
                  try
                  {
                    typeValue = MessageUtil.stripColor((EntityType.valueOf(typeValue.toUpperCase())).toString());
                  }
                  catch (Exception ignored)
                  {

                  }
                  break;
                case "material":
                  try
                  {
                    material = Material.valueOf(typeValue.toUpperCase());
                    typeValue = MessageUtil.stripColor(ItemNameUtil.itemName(material).toString());
                  }
                  catch (Exception ignored)
                  {

                  }
                  break;
              }
              int min = 0;
              if (MessageUtil.isInteger(sender, args[7], false))
              {
                min = Integer.parseInt(args[7]);
              }
              switch (args[4])
              {
                case "material":
                  switch (args[5])
                  {
                    case "break_item":
                      return Method.tabCompleterIntegerRadius(
                              args, min, Integer.MAX_VALUE, "[" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 망가뜨린 최대 횟수 조건]", "-1", min + "");
                    case "craft_item":
                      return Method.tabCompleterIntegerRadius(
                              args, min, Integer.MAX_VALUE, "[" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 제작한 최대 횟수 조건]", "-1", min + "");
                    case "drop":
                      return Method.tabCompleterIntegerRadius(
                              args, min, Integer.MAX_VALUE, "[" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 떨어뜨린 최대 횟수 조건]", "-1", min + "");
                    case "pickup":
                      return Method.tabCompleterIntegerRadius(
                              args, min, Integer.MAX_VALUE, "[" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 주운 최대 횟수 조건]", "-1", min + "");
                    case "mine_block":
                      return Method.tabCompleterIntegerRadius(
                              args, min, Integer.MAX_VALUE, "[" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 채굴한 최대 횟수 조건]", "-1", min + "");
                    case "use_item":
                      String useType = "사용";
                      if (material.isBlock())
                      {
                        useType = "설치";
                      }
                      if (ItemStackUtil.isEdible(material))
                      {
                        useType = "섭취";
                      }
                      return Method.tabCompleterIntegerRadius(
                              args, min, Integer.MAX_VALUE, "[" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " " + useType + "한 최대 횟수 조건]", "-1", min + "");
                  }
                  break;
                case "entity":
                  switch (args[5])
                  {
                    case "entity_killed_by":
                      return Method.tabCompleterIntegerRadius(args, min, Integer.MAX_VALUE, "[" + typeValue + "에게 죽은 최대 횟수 조건]", "-1", min + "");
                    case "kill_entity":
                      return Method.tabCompleterIntegerRadius(args, min, Integer.MAX_VALUE, "[" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 죽인 최대 횟수 조건>", "-1");
                  }
                  break;
              }
            }
            if ("require".equals(args[3]))
            {
              if ("recipe".equals(args[4]))
              {
                YamlConfiguration config = Variable.customRecipes.get(args[5]);
                if (config == null)
                {
                  return Collections.singletonList(args[5] + MessageUtil.getFinalConsonant(args[5], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피 목록입니다");
                }
                ConfigurationSection recipes = config.getConfigurationSection("recipes");
                if (recipes == null)
                {
                  return Collections.singletonList(args[5] + " 레시피 목록에는 유효한 레시피가 없습니다");
                }
                if (config.getConfigurationSection("recipes." + args[6]) == null)
                {
                  return Collections.singletonList(args[6] + MessageUtil.getFinalConsonant(args[6], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피입니다");
                }
                String requireCategoryDisplay = config.getString("extra.display");
                if (requireCategoryDisplay == null)
                {
                  requireCategoryDisplay = args[5];
                }
                requireCategoryDisplay = MessageUtil.stripColor(requireCategoryDisplay);
                String requireRecipeDisplay = config.getString("recipes." + args[6] + ".extra.display");
                if (requireRecipeDisplay == null)
                {
                  requireRecipeDisplay = args[6];
                }
                requireRecipeDisplay = MessageUtil.stripColor(requireRecipeDisplay);
                int min = 0;
                if (MessageUtil.isInteger(sender, args[7], false))
                {
                  min = Integer.parseInt(args[7]);
                }
                return Method.tabCompleterIntegerRadius(args, min, Integer.MAX_VALUE, "[" + requireCategoryDisplay + " 레시피 목록에 있는 " + requireRecipeDisplay + " 레시피의 최대 제작 횟수 조건]", "-1", min + "");
              }
            }
          }
          if ("recipe".equals(args[1]))
          {
            if ("statistic".equals(args[4]))
            {
              String typeValue = args[7];
              Material material = null;
              switch (args[5])
              {
                case "entity":
                  try
                  {
                    typeValue = MessageUtil.stripColor((EntityType.valueOf(typeValue.toUpperCase())).toString());
                  }
                  catch (Exception ignored)
                  {

                  }
                  break;
                case "material":
                  try
                  {
                    material = Material.valueOf(typeValue.toUpperCase());
                    typeValue = MessageUtil.stripColor(ItemNameUtil.itemName(material).toString());
                  }
                  catch (Exception ignored)
                  {

                  }
                  break;
              }
              switch (args[5])
              {
                case "material":
                  switch (args[6])
                  {
                    case "break_item":
                      return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 망가뜨린 최소 횟수 조건>", "-1");
                    case "craft_item":
                      return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 제작한 최소 횟수 조건>", "-1");
                    case "drop":
                      return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 떨어뜨린 최소 횟수 조건>", "-1");
                    case "pickup":
                      return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 주운 최소 횟수 조건>", "-1");
                    case "mine_block":
                      return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 채굴한 최소 횟수 조건>", "-1");
                    case "use_item":
                      String useType = "사용";
                      if (material.isBlock())
                      {
                        useType = "설치";
                      }
                      if (ItemStackUtil.isEdible(material))
                      {
                        useType = "섭취";
                      }
                      return Method.tabCompleterIntegerRadius(
                              args, 0, Integer.MAX_VALUE, "<" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " " + useType + "한 최소 횟수 조건>", "-1");
                  }
                  break;
                case "entity":
                  switch (args[6])
                  {
                    case "entity_killed_by":
                      return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + typeValue + "에게 죽은 최소 횟수 조건>", "-1");
                    case "kill_entity":
                      return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 죽인 최소 횟수 조건>", "-1");
                  }
                  break;
                case "general":
                  int min = 0;
                  if (MessageUtil.isInteger(sender, args[7], false))
                  {
                    min = Integer.parseInt(args[7]);
                  }
                  typeValue = args[6];
                  try
                  {
                    typeValue = Statistic.valueOf(typeValue.toUpperCase()).toString();
                  }
                  catch (Exception ignored)
                  {
                  }
                  return Method.tabCompleterIntegerRadius(args, min, Integer.MAX_VALUE, "[" + typeValue + "의 최댓값 조건]", "-1", min + "");
              }
            }
            if ("require".equals(args[4]))
            {
              if ("category".equals(args[5]))
              {
                YamlConfiguration config = Variable.customRecipes.get(args[6]);
                if (config == null)
                {
                  return Collections.singletonList(args[6] + MessageUtil.getFinalConsonant(args[6], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피 목록입니다");
                }
                String requireCategoryDisplay = config.getString("extra.display");
                if (requireCategoryDisplay == null)
                {
                  requireCategoryDisplay = args[6];
                }
                requireCategoryDisplay = MessageUtil.stripColor(requireCategoryDisplay);
                int min = 0;
                if (MessageUtil.isInteger(sender, args[7], false))
                {
                  min = Integer.parseInt(args[7]);
                }
                return Method.tabCompleterIntegerRadius(args, min, Integer.MAX_VALUE, "[" + requireCategoryDisplay + " 레시피 목록에 있는 레시피의 최대 제작 횟수 조건]", "-1", min + "");
              }
              if ("recipe".equals(args[5]))
              {
                YamlConfiguration config = Variable.customRecipes.get(args[6]);
                if (config == null)
                {
                  return Collections.singletonList(args[6] + MessageUtil.getFinalConsonant(args[6], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피 목록입니다");
                }
                ConfigurationSection recipes = config.getConfigurationSection("recipes");
                if (recipes == null)
                {
                  return Collections.singletonList(args[6] + " 레시피 목록에는 유효한 레시피가 없습니다");
                }
                if (config.getConfigurationSection("recipes." + args[7]) == null)
                {
                  return Collections.singletonList(args[7] + MessageUtil.getFinalConsonant(args[7], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피입니다");
                }
                String requireCategoryDisplay = config.getString("extra.display");
                if (requireCategoryDisplay == null)
                {
                  requireCategoryDisplay = args[6];
                }
                requireCategoryDisplay = MessageUtil.stripColor(requireCategoryDisplay);
                String requireRecipeDisplay = config.getString("recipes." + args[7] + ".extra.display");
                if (requireRecipeDisplay == null)
                {
                  requireRecipeDisplay = args[7];
                }
                requireRecipeDisplay = MessageUtil.stripColor(requireRecipeDisplay);
                return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + requireCategoryDisplay + " 레시피 목록에 있는 " + requireRecipeDisplay + " 레시피의 최소 제작 횟수 조건>", "-1");
              }
            }
          }
        }
        break;
      case 10:
        if ("edit".equals(args[0]))
        {
          if ("recipe".equals(args[1]))
          {
            if ("statistic".equals(args[4]))
            {
              String typeValue = args[7];
              Material material = null;
              switch (args[5])
              {
                case "entity":
                  try
                  {
                    typeValue = MessageUtil.stripColor((EntityType.valueOf(typeValue.toUpperCase())).toString());
                  }
                  catch (Exception ignored)
                  {

                  }
                  break;
                case "material":
                  try
                  {
                    material = Material.valueOf(typeValue.toUpperCase());
                    typeValue = MessageUtil.stripColor(ItemNameUtil.itemName(material).toString());
                  }
                  catch (Exception ignored)
                  {

                  }
                  break;
              }
              int min = 0;
              if (MessageUtil.isInteger(sender, args[8], false))
              {
                min = Integer.parseInt(args[8]);
              }
              switch (args[5])
              {
                case "material":
                  switch (args[6])
                  {
                    case "break_item":
                      return Method.tabCompleterIntegerRadius(
                              args, min, Integer.MAX_VALUE, "[" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 망가뜨린 최대 횟수 조건]", "-1", min + "");
                    case "craft_item":
                      return Method.tabCompleterIntegerRadius(
                              args, min, Integer.MAX_VALUE, "[" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 제작한 최대 횟수 조건]", "-1", min + "");
                    case "drop":
                      return Method.tabCompleterIntegerRadius(
                              args, min, Integer.MAX_VALUE, "[" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 떨어뜨린 최대 횟수 조건]", "-1", min + "");
                    case "pickup":
                      return Method.tabCompleterIntegerRadius(
                              args, min, Integer.MAX_VALUE, "[" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 주운 최대 횟수 조건]", "-1", min + "");
                    case "mine_block":
                      return Method.tabCompleterIntegerRadius(
                              args, min, Integer.MAX_VALUE, "[" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 채굴한 최대 횟수 조건]", "-1", min + "");
                    case "use_item":
                      String useType = "사용";
                      if (material.isBlock())
                      {
                        useType = "설치";
                      }
                      if (ItemStackUtil.isEdible(material))
                      {
                        useType = "섭취";
                      }
                      return Method.tabCompleterIntegerRadius(
                              args, min, Integer.MAX_VALUE, "[" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " " + useType + "한 최대 횟수 조건]", "-1", min + "");
                  }
                  break;
                case "entity":
                  switch (args[6])
                  {
                    case "entity_killed_by":
                      return Method.tabCompleterIntegerRadius(args, min, Integer.MAX_VALUE, "[" + typeValue + "에게 죽은 최대 횟수 조건]", "-1", min + "");
                    case "kill_entity":
                      return Method.tabCompleterIntegerRadius(args, min, Integer.MAX_VALUE, "[" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 죽인 최대 횟수 조건>", "-1");
                  }
                  break;
              }
            }
            if ("require".equals(args[4]))
            {
              if ("recipe".equals(args[5]))
              {
                YamlConfiguration config = Variable.customRecipes.get(args[6]);
                if (config == null)
                {
                  return Collections.singletonList(args[6] + MessageUtil.getFinalConsonant(args[6], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피 목록입니다");
                }
                ConfigurationSection recipes = config.getConfigurationSection("recipes");
                if (recipes == null)
                {
                  return Collections.singletonList(args[6] + " 레시피 목록에는 유효한 레시피가 없습니다");
                }
                if (config.getConfigurationSection("recipes." + args[7]) == null)
                {
                  return Collections.singletonList(args[7] + MessageUtil.getFinalConsonant(args[7], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피입니다");
                }
                String requireCategoryDisplay = config.getString("extra.display");
                if (requireCategoryDisplay == null)
                {
                  requireCategoryDisplay = args[6];
                }
                requireCategoryDisplay = MessageUtil.stripColor(requireCategoryDisplay);
                String requireRecipeDisplay = config.getString("recipes." + args[7] + ".extra.display");
                if (requireRecipeDisplay == null)
                {
                  requireRecipeDisplay = args[7];
                }
                requireRecipeDisplay = MessageUtil.stripColor(requireRecipeDisplay);
                int min = 0;
                if (MessageUtil.isInteger(sender, args[8], false))
                {
                  min = Integer.parseInt(args[8]);
                }
                return Method.tabCompleterIntegerRadius(args, min, Integer.MAX_VALUE, "[" + requireCategoryDisplay + " 레시피 목록에 있는 " + requireRecipeDisplay + " 레시피의 최대 제작 횟수 조건]", "-1", min + "");
              }
            }
          }
        }
        break;
    }
    if (length >= 8)
    {
      if (args[0].equals("edit") && args[1].equals("recipe") && args[4].equals("command") && Method.equals(args[5], "craft", "failure", "success"))
      {
        if (Method.equals(args[6], "set", "insert"))
        {
          return CommandTabUtil.getCommandsTabCompleter(sender, args, 9, true);
        }
        else if (args[6].equals("add"))
        {
          return CommandTabUtil.getCommandsTabCompleter(sender, args, 8, true);
        }
      }
    }
    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}

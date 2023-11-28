package com.jho5245.cucumbery.commands.customrecipe;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customrecipe.CustomRecipeUtil;
import com.jho5245.cucumbery.custom.customrecipe.recipeinventory.RecipeInventoryCategory;
import com.jho5245.cucumbery.custom.customrecipe.recipeinventory.RecipeInventoryMainMenu;
import com.jho5245.cucumbery.custom.customrecipe.recipeinventory.RecipeInventoryRecipe;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.no_groups.*;
import com.jho5245.cucumbery.util.no_groups.MessageUtil.ConsonantType;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.block.Biome;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommandCustomRecipe implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_CUSTOMRECIPE, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    String usage = Method.getUsage(cmd);
    if (args.length < 1)
    {
      Bukkit.getServer().dispatchCommand(sender, "rcp open");
      return true;
    }
    switch (args[0])
    {
      case "open":
        if (!(sender instanceof Player))
        {
          MessageUtil.sendError(sender, Prefix.ONLY_PLAYER);
          return true;
        }
        if (args.length > 4)
        {
          MessageUtil.longArg(sender, 4, args);
          MessageUtil.commandInfo(sender, label, "open [레시피 목록 이름] [레시피 이름] [레시피 메뉴를 열어줄 플레이어]");
          return true;
        }
        Player player;
        if (args.length <= 3)
        {
          player = (Player) sender;
        }
        else
        {
          player = SelectorUtil.getPlayer(sender, args[3]);
          if (player == null)
          {
            return true;
          }
        }
        File folder = new File(Cucumbery.getPlugin().getDataFolder() + "/data/CustomRecipe");
        if (!folder.exists())
        {
          boolean success = folder.mkdirs();
          if (!success)
          {
            System.err.println("[Cucumbery] could not create customrecipes folder!");
          }
        }
        if (args.length == 1)
        {
          final Player finalPlayer = player;
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> RecipeInventoryMainMenu.openRecipeInventory(finalPlayer, 1, true), 0L);
          return true;
        }
        File file = new File(Cucumbery.getPlugin().getDataFolder() + "/data/CustomRecipe/" + args[1] + ".yml");
        if (!file.exists())
        {
          MessageUtil.sendError(sender, "커스텀 레시피 목록 %s을(를) 찾을 수 없습니다", Constant.THE_COLOR_HEX + args[1]);
          return true;
        }
        YamlConfiguration config = Variable.customRecipes.get(args[1]);
        if (args.length == 2)
        {
          final Player finalPlayer = player;
          @NotNull String[] finalArgs = args;
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> RecipeInventoryCategory.openRecipeInventory(finalPlayer, 1, finalArgs[1], 1, true), 0L);
          return true;
        }
        if ((config.getConfigurationSection("recipes") == null) || (!Objects.requireNonNull(config.getConfigurationSection("recipes")).contains(args[2])))
        {
          MessageUtil.sendError(sender, "커스텀 레시피 목록 %s에서 해당 커스텀 레시피(%s)를 찾을 수 없습니다", Constant.THE_COLOR_HEX + args[1], Constant.THE_COLOR_HEX + args[2]);
          return true;
        }
        final Player finalPlayer = player;
        @NotNull String[] finalArgs1 = args;
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> RecipeInventoryRecipe.openRecipeInventory(finalPlayer, 1, finalArgs1[1], 1, finalArgs1[2], true), 0L);
        break;
      case "create":
        if (!(sender instanceof Player))
        {
          MessageUtil.sendError(sender, Prefix.ONLY_PLAYER);
          return true;
        }
        player = (Player) sender;
        if (args.length < 3)
        {
          MessageUtil.shortArg(player, 3, args);
          MessageUtil.commandInfo(player, label, "create <레시피 목록 이름> <추가할 레시피 이름>");
          return true;
        }
        if (args.length > 3)
        {
          MessageUtil.longArg(player, 3, args);
          MessageUtil.commandInfo(player, label, "create <레시피 목록 이름> <추가할 레시피 이름>");
          return true;
        }
        if (Method2.isInvalidFileName(args[1]))
        {
          MessageUtil.sendError(player, "유효하지 않은 레시피 목록 이름입니다");
          return true;
        }
        if (args[2].equals("--all"))
        {
          MessageUtil.sendError(player, "해당하는 이름으로는 레시피를 저장할 수 없습니다");
          return true;
        }
        if (!args[1].equals(MessageUtil.stripColor(args[1])))
        {
          MessageUtil.sendError(player, "레시피 목록 이름에는 컬러 코드를 사용할 수 없습니다");
          MessageUtil.info(player, "레시피 제작 후, %s명령어로 컬러 코드를 사용할 수 있습니다", "rg255,204;/label edit category " + MessageUtil.stripColor(args[1]) + " display <이름>");
          return true;
        }
        if (!args[2].equals(MessageUtil.stripColor(args[2])))
        {
          MessageUtil.sendError(player, "레시피 이름에는 컬러 코드를 사용할 수 없습니다");
          MessageUtil.info(player, "레시피 제작 후, %s명령어로 컬러 코드를 사용할 수 있습니다", "rg255,204;/label edit category " + MessageUtil.stripColor(args[1]) + " " + MessageUtil.stripColor(args[2]) + " display <이름>");
          return true;
        }
        File recipeFile = new File(Cucumbery.getPlugin().getDataFolder() + "/data/CustomRecipe/" + args[1] + ".yml");
        if (!recipeFile.exists())
        {
          CustomRecipeUtil.openCreationGUI(player, null, null, null, args[1], args[2], "§레§시§피§ §생§성§3생성");
          return true;
        }
        config = Variable.customRecipes.get(args[1]);
        ConfigurationSection recipeConfig = config.getConfigurationSection("recipes." + args[2]);
        if (recipeConfig == null || recipeConfig.getKeys(false).isEmpty())
        {
          CustomRecipeUtil.openCreationGUI(player, null, null, null, args[1], args[2], "§레§시§피§ §생§성§3생성");
          return true;
        }
        ItemStack result = ItemSerializer.deserialize(config.getString("recipes." + args[2] + ".result"));
        ConfigurationSection configurationSection = config.getConfigurationSection("recipes." + args[2] + ".ingredients");
        int ingredientAmount = configurationSection != null ? configurationSection.getKeys(false).size() : 0;
        List<ItemStack> ingredient = new ArrayList<>();
        List<Integer> ingredientAmounts = new ArrayList<>();
        for (int i = 0; i < ingredientAmount; i++)
        {
          String ingredientString = config.getString("recipes." + args[2] + ".ingredients." + (i + 1) + ".item");
          if (ingredientString != null)
          {
            boolean isPredicate = ingredientString.startsWith("predicate:");
            ItemStack ingre = isPredicate ? ItemStackUtil.getItemStackPredicate(ingredientString.substring(10)) : ItemSerializer.deserialize(ingredientString);
            if (isPredicate)
            {
              ItemMeta itemMeta = ingre.getItemMeta();
              itemMeta.displayName(Component.text(ingredientString));
              ingre.setItemMeta(itemMeta);
            }
            ingredient.add(ingre);
            ingredientAmounts.add(config.getInt("recipes." + args[2] + ".ingredients." + (i + 1) + ".amount"));
          }
        }
        CustomRecipeUtil.openCreationGUI(player, result, ingredient, ingredientAmounts, args[1], args[2], "§레§시§피§ §편§집§3편집");
        break;
      case "remove":
        if (args.length < 3)
        {
          MessageUtil.shortArg(sender, 3, args);
          MessageUtil.commandInfo(sender, label, "remove <레시피 목록 이름> <제거할 레시피 이름>");
          return true;
        }
        if (args.length > 3)
        {
          MessageUtil.longArg(sender, 3, args);
          MessageUtil.commandInfo(sender, label, "remove <레시피 목록 이름> <제거할 레시피 이름>");
          return true;
        }
        folder = new File(Cucumbery.getPlugin().getDataFolder() + "/data/CustomRecipe");
        if (!folder.exists())
        {
          boolean success = folder.mkdirs();
          if (!success)
          {
            System.err.println("[Cucumbery] could not create CustomRecipe folder!");
          }
        }
        file = new File(Cucumbery.getPlugin().getDataFolder() + "/data/CustomRecipe/" + args[1] + ".yml");
        if (!file.exists())
        {
          MessageUtil.sendError(sender, "커스텀 레시피 목록 %s을(를) 찾을 수 없습니다", Constant.THE_COLOR_HEX + args[1]);
          return true;
        }
        config = Variable.customRecipes.get(args[1]);
        for (File logFile : CustomRecipeUtil.getPlayersCraftLog())
        {
          CustomRecipeUtil.removePlayerCraftLog(logFile.getName().substring(0, logFile.getName().length() - 4), args[1], args[2].equals("--all") ? null : args[2]);
          CustomRecipeUtil.removePlayerLastCraftLog(logFile.getName().substring(0, logFile.getName().length() - 4), args[1], args[2].equals("--all") ? null : args[2]);
        }
        if (args[2].equalsIgnoreCase("--all"))
        {
          boolean success = file.delete();
          if (!success)
          {
            System.err.println("[Cucumbery] could not delete custom recipe file (" + args[1] + ")!");
          }
          MessageUtil.sendMessage(sender, Prefix.INFO_CUSTOM_RECIPE, "커스텀 레시피 목록 rg255,204;" + args[1] + "&r에 있는 모든 레시피를 제거했습니다");
          return true;
        }
        ConfigurationSection configurationSection1 = config.getConfigurationSection("recipes");
        if (configurationSection1 == null || !configurationSection1.contains(args[2]))
        {
          MessageUtil.sendError(sender, "커스텀 레시피 목록 rg255,204;" + args[1] + "&r에서 해당 커스텀 레시피(rg255,204;" + args[2] + "&r)를 찾을 수 없습니다");
          return true;
        }
        CustomConfig customRecipeListConfig = CustomConfig.getCustomConfig("data/CustomRecipe/" + args[1] + ".yml");
        config.set("recipes." + args[2], null);
        customRecipeListConfig.getConfig().set("recipes." + args[2], null);
        if (configurationSection1.getKeys(false).isEmpty())
        {
          Variable.customRecipes.remove(args[1]);
          boolean success = file.delete();
          if (!success)
          {
            System.err.println("[Cucumbery] could not delete recipe (" + args[1] + ")file!");
          }
        }
        else
        {
          Variable.customRecipes.put(args[1], config);
          customRecipeListConfig.saveConfig();
        }
        MessageUtil.sendMessage(sender, Prefix.INFO_CUSTOM_RECIPE, "커스텀 레시피 목록 rg255,204;" + args[1] + "&r에서 rg255,204;" + args[2] + "&r 레시피를 제거했습니다");
        break;
      case "edit":
        if (args.length < 2)
        {
          MessageUtil.shortArg(sender, 5, args);
          MessageUtil.commandInfo(sender, label, "edit <category|recipe> ...");
          return true;
        }
        switch (args[1])
        {
          case "category":
            if (args.length < 5)
            {
              MessageUtil.shortArg(sender, 5, args);
              MessageUtil.commandInfo(sender, label, "edit category <레시피 목록 이름> <level|permission|require|statistic|wealth> ...");
              return true;
            }
            if (args.length > 9)
            {
              MessageUtil.longArg(sender, 9, args);
              MessageUtil.commandInfo(sender, label, "edit category <레시피 목록 이름> <level|permission|require|statistic|wealth> ...");
              return true;
            }
            folder = new File(Cucumbery.getPlugin().getDataFolder() + "/data/CustomRecipe");
            if (!folder.exists())
            {
              boolean success = folder.mkdirs();
              if (!success)
              {
                System.err.println("[Cucumbery] Could not create CustomRecipe folder!");
              }
            }
            file = new File(Cucumbery.getPlugin().getDataFolder() + "/data/CustomRecipe/" + args[2] + ".yml");
            if (!file.exists())
            {
              MessageUtil.sendError(sender, "커스텀 레시피 목록 rg255,204;" + args[2] + "&r" + MessageUtil.getFinalConsonant(args[2], ConsonantType.을를) + " 찾을 수 없습니다");
              return true;
            }
            customRecipeListConfig = CustomConfig.getCustomConfig("data/CustomRecipe/" + args[2] + ".yml");
            config = customRecipeListConfig.getConfig();
            switch (args[3])
            {
              case "displayitem":
                switch (args[4])
                {
                  case "hand":
                    if (!(sender instanceof Player))
                    {
                      MessageUtil.sendError(sender, Prefix.ONLY_PLAYER);
                      return true;
                    }
                    if (args.length > 5)
                    {
                      MessageUtil.longArg(sender, 5, args);
                      MessageUtil.commandInfo(sender, label, "edit category <레시피 목록 이름> displayitem hand");
                      return true;
                    }
                    player = (Player) sender;
                    ItemStack item = player.getInventory().getItemInMainHand();
                    if (!ItemStackUtil.itemExists(item))
                    {
                      MessageUtil.sendError(sender, Prefix.NO_HOLDING_ITEM);
                      return true;
                    }
                    item = item.clone();
                    item.setAmount(1);
                    ItemLore.removeItemLore(item);
                    ItemStack configItem = ItemSerializer.deserialize(config.getString("extra.displayitem"));
                    if (ItemStackUtil.itemExists(configItem) && item.equals(configItem))
                    {
                      MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 %s의 표시 아이템이 %s입니다", Constant.THE_COLOR_HEX + args[2], item);
                      return true;
                    }
                    config.set("extra.displayitem", ItemSerializer.serialize(item));
                    Variable.customRecipes.put(args[2], config);
                    customRecipeListConfig.saveConfig();
                    MessageUtil.sendMessage(
                            sender, Prefix.INFO_CUSTOM_RECIPE, "커스텀 레시피 목록 %s의 표시 아이템을 %s(으)로 설정했습니다", Constant.THE_COLOR_HEX + args[2], item);
                    break;
                  case "material":
                    if (args.length < 6)
                    {
                      MessageUtil.shortArg(sender, 6, args);
                      MessageUtil.commandInfo(sender, label, "edit category <레시피 목록 이름> displayitem material <아이템 종류>");
                      return true;
                    }
                    if (args.length > 6)
                    {
                      MessageUtil.longArg(sender, 6, args);
                      MessageUtil.commandInfo(sender, label, "edit category <레시피 목록 이름> displayitem material <아이템 종류>");
                      return true;
                    }
                    Material material;
                    try
                    {
                      material = Material.valueOf(args[5].toUpperCase());
                      if (!material.isItem() || material == Material.AIR)
                      {
                        throw new Exception();
                      }
                    }
                    catch (Exception e)
                    {
                      MessageUtil.noArg(sender, Prefix.NO_MATERIAL_ITEM, args[5]);
                      return true;
                    }
                    String materialName = ItemNameUtil.itemName(material).toString();
                    Material configMaterial;
                    try
                    {
                      configMaterial = Material.valueOf(config.getString("extra.displayitem"));
                    }
                    catch (Exception e)
                    {
                      configMaterial = null;
                    }
                    if (configMaterial != null && material == configMaterial)
                    {
                      MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r의 표시 아이템이 rg255,204;" + materialName + "&r입니다");
                      return true;
                    }
                    config.set("extra.displayitem", material.toString());
                    Variable.customRecipes.put(args[2], config);
                    customRecipeListConfig.saveConfig();
                    MessageUtil.sendMessage(
                            sender, Prefix.INFO_CUSTOM_RECIPE,
                            "커스텀 레시피 목록 §e" +
                                    args[2] +
                                    "&r의 표시 아이템을 rg255,204;" +
                                    materialName +
                                    "&r" +
                                    MessageUtil.getFinalConsonant(materialName, ConsonantType.으로) +
                                    " 설정했습니다");
                    break;
                  case "remove":
                    if (args.length > 5)
                    {
                      MessageUtil.longArg(sender, 5, args);
                      MessageUtil.commandInfo(sender, label, "edit category <레시피 목록 이름> displayitem remove");
                      return true;
                    }
                    String configValue = config.getString("extra.displayitem");
                    if (configValue == null)
                    {
                      MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r의 표시 아이템이 없습니다");
                      return true;
                    }
                    config.set("extra.displayitem", null);
                    Variable.customRecipes.put(args[2], config);
                    customRecipeListConfig.saveConfig();
                    MessageUtil.sendMessage(sender, Prefix.INFO_CUSTOM_RECIPE, "커스텀 레시피 목록 §e" + args[2] + "&r의 표시 아이템을 제거했습니다");
                    break;
                  default:
                    MessageUtil.wrongArg(sender, 5, args);
                    MessageUtil.commandInfo(sender, label, "edit category <레시피 목록 이름> displayitem <hand|material|remove> ...");
                    return true;
                }
                break;
              case "permission":
                if (args.length < 6)
                {
                  MessageUtil.shortArg(sender, 6, args);
                  MessageUtil.commandInfo(sender, label, "edit category <레시피 목록 이름> permission <base|bypass|hide> ...");
                  return true;
                }
                switch (args[4])
                {
                  case "base":
                    String inputBasePermission = args[5];
                    String configBasePermission = config.getString("extra.permissions.base-permission");
                    if (inputBasePermission.equals(configBasePermission))
                    {
                      MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r의 접근 권한 요구 퍼미션 노드 기본값이 rg255,204;" + inputBasePermission + "&r입니다");
                      return true;
                    }
                    if (inputBasePermission.equals("--remove") && configBasePermission == null)
                    {
                      MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r의 접근 권한 퍼미션 노드 기본값이 없는 상태입니다");
                      return true;
                    }
                    config.set("extra.permissions.base-permission", inputBasePermission.equals("--remove") ? null : inputBasePermission);
                    Variable.customRecipes.put(args[2], config);
                    customRecipeListConfig.saveConfig();
                    MessageUtil.sendMessage(
                            sender, Prefix.INFO_CUSTOM_RECIPE,
                            "커스텀 레시피 목록 §e" +
                                    args[2] +
                                    "&r의 접근 권한 요구 퍼미션 노드 기본값을 rg255,204;" +
                                    (inputBasePermission.equals("--remove") ? "&r삭제했습니다" : (inputBasePermission +
                                            "&r" +
                                            MessageUtil.getFinalConsonant(inputBasePermission, ConsonantType.으로) +
                                            " 지정했습니다")));
                    break;
                  case "bypass":
                    String inputBypassPermission = args[5];
                    String configBypassPermission = config.getString("extra.permissions.bypass-if-hidden-permission");
                    if (inputBypassPermission.equals(configBypassPermission))
                    {
                      MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r의 비공개 우회 퍼미션 노드 값이 rg255,204;" + inputBypassPermission + "&r입니다");
                      return true;
                    }
                    if (inputBypassPermission.equals("--remove") && configBypassPermission == null)
                    {
                      MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r의 비공개 우회 퍼미션 노드 값이 없는 상태입니다");
                      return true;
                    }
                    config.set("extra.permissions.bypass-if-hidden-permission", inputBypassPermission.equals("--remove") ? null : inputBypassPermission);
                    Variable.customRecipes.put(args[2], config);
                    customRecipeListConfig.saveConfig();
                    MessageUtil.sendMessage(
                            sender, Prefix.INFO_CUSTOM_RECIPE,
                            "커스텀 레시피 목록 §e" +
                                    args[2] +
                                    "&r의 비공개 우회 퍼미션 노드 값을 rg255,204;" +
                                    (inputBypassPermission.equals("--remove") ? "&r삭제했습니다" : (inputBypassPermission + "&r" + MessageUtil.getFinalConsonant(inputBypassPermission, ConsonantType.으로) + " 지정했습니다")));
                    break;
                  case "hide":
                    boolean hideIfNoPerm = false;
                    if (!args[5].equals("true") && !args[5].equals("false"))
                    {
                      MessageUtil.wrongBool(sender, 6, args);
                      return true;
                    }
                    if (args[5].equals("true"))
                    {
                      hideIfNoPerm = true;
                    }
                    if (config.getBoolean("extra.permissions.hide-if-no-base") == hideIfNoPerm)
                    {
                      MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r의 권한이 없을 시 레시피 목록 내용 비공개 값이 rg255,204;" + hideIfNoPerm + "&r입니다");
                      return true;
                    }
                    config.set("extra.permissions.hide-if-no-base", hideIfNoPerm);
                    Variable.customRecipes.put(args[2], config);
                    customRecipeListConfig.saveConfig();
                    MessageUtil.sendMessage(sender, Prefix.INFO_CUSTOM_RECIPE, "커스텀 레시피 목록 §e" + args[2] + "&r의 권한이 없을 시 레시피 목록 내용 비공개 값을 rg255,204;" + hideIfNoPerm + "&r로 지정했습니다");
                    break;
                  default:
                    MessageUtil.wrongArg(sender, 5, args);
                    MessageUtil.commandInfo(sender, label, "edit category " + args[2] + "permission <base|bypass|hide> ...");
                    return true;
                }
                break;
              case "wealth":
                if (!Cucumbery.using_Vault_Economy)
                {
                  MessageUtil.sendError(sender, "rg255,204;Vault &r플러그인을 사용하고 있지 않습니다");
                  return true;
                }
                if (args.length > 6)
                {
                  MessageUtil.longArg(sender, 6, args);
                  MessageUtil.commandInfo(sender, label, "edit category " + args[2] + " wealth <최소 소지 금액> [최대 소지 금액]");
                  return true;
                }
                if (!MessageUtil.isDouble(sender, args[4], true))
                {
                  return true;
                }
                double inputWealthMin = Double.parseDouble(args[4]);
                if (inputWealthMin != -1d && !MessageUtil.checkNumberSize(sender, inputWealthMin, 0d, Double.MAX_VALUE))
                {
                  return true;
                }
                if (args.length == 6 && !MessageUtil.isDouble(sender, args[5], true))
                {
                  return true;
                }
                double inputWealthMax = args.length == 6 ? Double.parseDouble(args[5]) : -1d;
                if (inputWealthMax != -1d && !MessageUtil.checkNumberSize(sender, inputWealthMax, Math.max(0, inputWealthMin), Double.MAX_VALUE))
                {
                  return true;
                }
                double configWealthMin = config.getDouble("extra.wealth.min");
                double configWealthMax = config.getDouble("extra.wealth.max");
                boolean configWealthMinExists = config.contains("extra.wealth.min");
                boolean configWealthMaxExists = config.contains("extra.wealth.max");
                if (inputWealthMin == -1d && (!configWealthMinExists || configWealthMin == -1d) && inputWealthMax == -1d && (!configWealthMaxExists || configWealthMax == -1d))
                {
                  MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r의 최대, 최소 소지 금액 조건이 없습니다");
                  return true;
                }
                if (configWealthMinExists && inputWealthMin == configWealthMin && configWealthMaxExists && inputWealthMax == configWealthMax)
                {
                  MessageUtil.sendError(
                          sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" +
                                  args[2] +
                                  "&r의 최소 소지 금액 조건이 rg255,204;" +
                                  Constant.Sosu15.format(inputWealthMin) +
                                  "원&r, 최대 소지 금액 조건이 rg255,204;" +
                                  Constant.Sosu15.format(inputWealthMax) +
                                  "원&r입니다");
                  return true;
                }
                config.set("extra.wealth.min", inputWealthMin == -1d ? null : inputWealthMin);
                config.set("extra.wealth.max", inputWealthMax == -1d ? null : inputWealthMax);
                ConfigurationSection section = config.getConfigurationSection("extra.wealth");
                if (section != null && section.getKeys(false).size() == 0)
                {
                  config.set("extra.wealth", null);
                }
                Variable.customRecipes.put(args[2], config);
                customRecipeListConfig.saveConfig();
                if (inputWealthMin != inputWealthMax)
                {
                  MessageUtil.sendMessage(
                          sender, Prefix.INFO_CUSTOM_RECIPE,
                          "커스텀 레시피 목록 rg255,204;" +
                                  args[2] +
                                  (inputWealthMin == -1d ? ("&r의 최소 소지 금액 조건을 제거") : ("&r의 최소 소지 금액 조건을 rg255,204;" + Constant.Sosu15.format(inputWealthMin) + "원&r으로 지정")) +
                                  "하였고, 최대 소지 금액 조건을 " +
                                  (inputWealthMax == -1d ? ("제거") : (Constant.THE_COLOR_HEX + Constant.Sosu15.format(inputWealthMax) + "원&r으로 지정")) +
                                  "했습니다");
                }
                else if (inputWealthMin == -1d)
                {
                  MessageUtil.sendMessage(sender, Prefix.INFO_CUSTOM_RECIPE, "커스텀 레시피 목록 rg255,204;" + args[2] + "&r의 최대, 최소 소지 금액 조건을 제거했습니다");
                }
                else
                {
                  MessageUtil.sendMessage(sender, Prefix.INFO_CUSTOM_RECIPE, "커스텀 레시피 목록 rg255,204;" + args[2] + "&r의 최대, 최소 소지 금액 조건을 rg255,204;" + Constant.Sosu15.format(inputWealthMin) + "원&r으로 지정했습니다");
                }
                break;
              case "level":
                if (args.length > 6)
                {
                  MessageUtil.longArg(sender, 6, args);
                  MessageUtil.commandInfo(sender, label, "edit category " + args[2] + " level <최소 레벨> [최대 레벨]");
                  return true;
                }
                if (!MessageUtil.isInteger(sender, args[4], true))
                {
                  return true;
                }
                int inputLevelMin = Integer.parseInt(args[4]);
                if (inputLevelMin != -1 && !MessageUtil.checkNumberSize(sender, inputLevelMin, 0, Integer.MAX_VALUE))
                {
                  return true;
                }
                if (args.length == 6 && !MessageUtil.isDouble(sender, args[5], true))
                {
                  return true;
                }
                int inputLevelMax = args.length == 6 ? Integer.parseInt(args[5]) : -1;
                if (inputLevelMax != -1 && !MessageUtil.checkNumberSize(sender, inputLevelMax, Math.max(0, inputLevelMin), Integer.MAX_VALUE))
                {
                  return true;
                }
                int configLevelMin = config.getInt("extra.level.min");
                int configLevelMax = config.getInt("extra.level.max");
                boolean configLevelMinExists = config.contains("extra.level.min");
                boolean configLevelMaxExists = config.contains("extra.level.max");
                if (inputLevelMin == -1 && (!configLevelMinExists || configLevelMin == -1) && inputLevelMax == -1 && (!configLevelMaxExists || configLevelMax == -1))
                {
                  MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r의 최대, 최소 레벨 조건이 없습니다");
                  return true;
                }
                if (configLevelMinExists && inputLevelMin == configLevelMin && configLevelMaxExists && inputLevelMax == configLevelMax)
                {
                  MessageUtil.sendError(
                          sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" +
                                  args[2] +
                                  "&r의 최소 소지 레벨 조건이 rg255,204;" +
                                  Constant.Sosu15.format(inputLevelMin) +
                                  "&r, 최대 레벨 조건이 rg255,204;" +
                                  Constant.Sosu15.format(inputLevelMax) +
                                  "&r입니다");
                  return true;
                }
                config.set("extra.level.min", inputLevelMin == -1 ? null : inputLevelMin);
                config.set("extra.level.max", inputLevelMax == -1 ? null : inputLevelMax);
                if (Objects.requireNonNull(config.getConfigurationSection("extra.level")).getKeys(false).size() == 0)
                {
                  config.set("extra.level", null);
                }
                Variable.customRecipes.put(args[2], config);
                customRecipeListConfig.saveConfig();
                if (inputLevelMin != inputLevelMax)
                {
                  MessageUtil.sendMessage(sender,
                          Prefix.INFO_CUSTOM_RECIPE, "커스텀 레시피 목록 rg255,204;" + args[2] + (inputLevelMin == -1 ? ("&r의 최소 레벨 조건을 제거") : ("&r의 최소 레벨 조건을 rg255,204;" + Constant.Sosu15.format(
                                  inputLevelMin) + "&r으로 지정")) + "하였고, 최대 레벨 조건을 " + (inputLevelMax == -1 ? ("제거") : (Constant.THE_COLOR_HEX + Constant.Sosu15.format(inputLevelMax) + "&r으로 지정")) + "했습니다");
                }
                else if (inputLevelMin == -1)
                {
                  MessageUtil.sendMessage(sender, Prefix.INFO_CUSTOM_RECIPE, "커스텀 레시피 목록 rg255,204;" + args[2] + "&r의 최대, 최소 레벨 조건을 제거했습니다");
                }
                else
                {
                  MessageUtil.sendMessage(sender, Prefix.INFO_CUSTOM_RECIPE, "커스텀 레시피 목록 rg255,204;" + args[2] + "&r의 최대, 최소 레벨 조건을 rg255,204;" + Constant.Sosu15.format(inputLevelMin) + "&r으로 지정했습니다");
                }
                break;
              case "require":
                if (args.length < 7)
                {
                  MessageUtil.shortArg(sender, 7, args);
                  MessageUtil.commandInfo(sender, label, "edit category " + args[2] + " require <category|recipe> ...");
                  return true;
                }
                folder = new File(Cucumbery.getPlugin().getDataFolder() + "/data/CustomRecipe");
                if (!folder.exists())
                {
                  boolean success = folder.mkdirs();
                  if (!success)
                  {
                    System.err.println("[Cucumbery] could not create customrecipe folder!");
                  }
                }
                if (!new File(Cucumbery.getPlugin().getDataFolder() + "/data/CustomRecipe/" + args[5] + ".yml").exists())
                {
                  MessageUtil.sendError(sender, "커스텀 레시피 목록 rg255,204;" + args[5] + "&r" + MessageUtil.getFinalConsonant(args[5], ConsonantType.을를) + " 찾을 수 없습니다");
                  return true;
                }
                switch (args[4])
                {
                  case "recipe":
                    if (args.length < 8)
                    {
                      MessageUtil.shortArg(sender, 8, args);
                      MessageUtil.commandInfo(sender, label, "edit category " + args[2] + " require recipe <레시피 목록 이름> <레시피 이름> <최소 제작 횟수> [최대 제작 횟수]");
                      return true;
                    }
                    customRecipeListConfig = CustomConfig.getCustomConfig("data/CustomRecipe/" + args[5] + ".yml");
                    config = customRecipeListConfig.getConfig();
                    ConfigurationSection recipe = config.getConfigurationSection("recipes." + args[6]);
                    if (recipe == null || recipe.getKeys(false).size() == 0)
                    {
                      MessageUtil.sendError(sender, "커스텀 레시피 목록 rg255,204;" + args[5] + "&r에서 해당 레시피(rg255,204;" + args[6] + "&r)를 찾을 수 없습니다");
                      return true;
                    }
                    customRecipeListConfig = CustomConfig.getCustomConfig("data/CustomRecipe/" + args[2] + ".yml");
                    config = customRecipeListConfig.getConfig();
                    if (!MessageUtil.isInteger(sender, args[7], true))
                    {
                      return true;
                    }
                    int inputRecipeMin = Integer.parseInt(args[7]);
                    if (inputRecipeMin != -1 && !MessageUtil.checkNumberSize(sender, inputRecipeMin, 0, Integer.MAX_VALUE))
                    {
                      return true;
                    }
                    if (args.length == 8 && !MessageUtil.isInteger(sender, args[7], true))
                    {
                      return true;
                    }
                    int inputRecipeMax = args.length == 9 ? Integer.parseInt(args[8]) : -1;
                    if (inputRecipeMax != -1 && !MessageUtil.checkNumberSize(sender, inputRecipeMax, Math.max(0, inputRecipeMin), Integer.MAX_VALUE))
                    {
                      return true;
                    }
                    int configRecipeMin = config.getInt("extra.recipes." + args[5] + "." + args[6] + ".min");
                    int configRecipeMax = config.getInt("extra.recipes." + args[5] + "." + args[6] + ".max");
                    boolean configRecipeMinExists = config.contains("extra.recipes." + args[5] + "." + args[6] + ".min");
                    boolean configRecipeMaxExists = config.contains("extra.recipes." + args[5] + "." + args[6] + ".max");
                    if (inputRecipeMin == -1 && (!configRecipeMinExists || configRecipeMin == -1) && inputRecipeMax == -1 && (!configRecipeMaxExists || configRecipeMax == -1))
                    {
                      MessageUtil.sendError(
                              sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 대한 커스텀 레시피 목록 rg255,204;" +
                                      args[5] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[6] +
                                      "&r의 최대, 최소 제작 횟수 조건이 없습니다");
                      return true;
                    }
                    if (configRecipeMinExists && inputRecipeMin == configRecipeMin && configRecipeMaxExists && inputRecipeMax == configRecipeMax)
                    {
                      MessageUtil.sendError(
                              sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 대한 커스텀 레시피 목록 rg255,204;" +
                                      args[5] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[6] +
                                      "&r의 최소 제작 횟수 조건이 rg255,204;" +
                                      Constant.Sosu15.format(inputRecipeMin) +
                                      "&r, 최대 제작 횟수 조건이 rg255,204;" +
                                      Constant.Sosu15.format(inputRecipeMax) +
                                      "&r입니다");
                      return true;
                    }
                    config.set("extra.recipes." + args[5] + "." + args[6] + ".min", inputRecipeMin == -1 ? null : inputRecipeMin);
                    config.set("extra.recipes." + args[5] + "." + args[6] + ".max", inputRecipeMax == -1 ? null : inputRecipeMax);

                    String section1String = "extra.recipes." + args[5] + "." + args[6];
                    ConfigurationSection section1 = config.getConfigurationSection(section1String);
                    if (section1 != null && section1.getKeys(false).size() == 0)
                    {
                      config.set(section1String, null);
                    }

                    String section2String = "extra.recipes." + args[5];
                    ConfigurationSection section2 = config.getConfigurationSection(section2String);
                    if (section2 != null && section2.getKeys(false).size() == 0)
                    {
                      config.set(section2String, null);
                    }

                    String section3String = "extra.recipes." + args[5];
                    ConfigurationSection section3 = config.getConfigurationSection(section3String);
                    if (section3 != null && section3.getKeys(false).size() == 0)
                    {
                      config.set(section3String, null);
                    }

                    Variable.customRecipes.put(args[2], config);
                    customRecipeListConfig.saveConfig();
                    if (inputRecipeMin != inputRecipeMax)
                    {
                      MessageUtil.sendMessage(
                              sender, Prefix.INFO_CUSTOM_RECIPE,
                              "커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 대한 커스텀 레시피 목록 rg255,204;" +
                                      args[5] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[6] +
                                      (inputRecipeMin == -1 ? ("&r의 최소 제작 횟수 조건을 제거") : ("&r의 최소 제작 횟수 조건을 rg255,204;" + Constant.Sosu15.format(inputRecipeMin) + "&r으로 " + "지정")) +
                                      "하였고, 최대 제작 횟수 조건을 " +
                                      (inputRecipeMax == -1 ? ("제거") : (Constant.THE_COLOR_HEX + Constant.Sosu15.format(inputRecipeMax) + "&r으로 지정")) +
                                      "했습니다");
                    }
                    else if (inputRecipeMin == -1)
                    {
                      MessageUtil.sendMessage(
                              sender, Prefix.INFO_CUSTOM_RECIPE,
                              "커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 대한 커스텀 레시피 목록 rg255,204;" +
                                      args[5] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[6] +
                                      "&r의 최대, 최소 레시피 제작 조건을 제거했습니다" +
                                      ".");
                    }
                    else
                    {
                      MessageUtil.sendMessage(
                              sender, Prefix.INFO_CUSTOM_RECIPE,
                              "커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 대한 커스텀 레시피 목록 rg255,204;" +
                                      args[5] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[6] +
                                      "&r의 최대, 최소 레시피 제작 조건을 rg255,204;" +
                                      Constant.Sosu15.format(inputRecipeMin) +
                                      "&r으로 지정했습니다");
                    }
                    break;
                  case "category":
                    if (args.length > 8)
                    {
                      MessageUtil.shortArg(sender, 8, args);
                      MessageUtil.commandInfo(sender, label, "edit category " + args[2] + " require category <레시피 목록 이름> <최소 제작 횟수> [최대 제작 횟수]");
                      return true;
                    }
                    if (!MessageUtil.isInteger(sender, args[6], true))
                    {
                      return true;
                    }
                    int inputCategoryMin = Integer.parseInt(args[6]);
                    if (inputCategoryMin != -1 && !MessageUtil.checkNumberSize(sender, inputCategoryMin, 0, Integer.MAX_VALUE))
                    {
                      return true;
                    }
                    if (args.length == 7 && !MessageUtil.isInteger(sender, args[6], true))
                    {
                      return true;
                    }
                    int inputCategoryMax = args.length == 8 ? Integer.parseInt(args[7]) : -1;
                    if (inputCategoryMax != -1 && !MessageUtil.checkNumberSize(sender, inputCategoryMax, Math.max(0, inputCategoryMin), Integer.MAX_VALUE))
                    {
                      return true;
                    }
                    int configCategoryMin = config.getInt("extra.categories." + args[5] + ".min");
                    int configCategoryMax = config.getInt("extra.categories." + args[5] + ".max");
                    boolean configCategoryMinExists = config.contains("extra.categories." + args[5] + ".min");
                    boolean configCategoryMaxExists = config.contains("extra.categories." + args[5] + ".max");
                    if (inputCategoryMin == -1 && (!configCategoryMinExists || configCategoryMin == -1) && inputCategoryMax == -1 && (!configCategoryMaxExists || configCategoryMax == -1))
                    {
                      MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r에 대한 커스텀 레시피 목록 rg255,204;" + args[5] + "&r에 포함되어 있는 레시피의 최대, 최소 제작 횟수 조건이 없습니다");
                      return true;
                    }
                    if (configCategoryMinExists && inputCategoryMin == configCategoryMin && configCategoryMaxExists && inputCategoryMax == configCategoryMax)
                    {
                      MessageUtil.sendError(
                              sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 대한 커스텀 레시피 목록 rg255,204;" +
                                      args[5] +
                                      "&r에 포함되어 있는 레시피의 최소 제작 횟수 조건이 rg255,204;" +
                                      Constant.Sosu15.format(inputCategoryMin) +
                                      "&r, 최대 제작 횟수 조건이 rg255,204;" +
                                      Constant.Sosu15.format(inputCategoryMax) +
                                      "&r입니다");
                      return true;
                    }
                    config.set("extra.categories." + args[5] + ".min", inputCategoryMin == -1 ? null : inputCategoryMin);
                    config.set("extra.categories." + args[5] + ".max", inputCategoryMax == -1 ? null : inputCategoryMax);

                    section1String = "extra.categories." + args[5];
                    section1 = config.getConfigurationSection(section1String);
                    if (section1 != null && section1.getKeys(false).size() == 0)
                    {
                      config.set(section1String, null);
                    }

                    section2String = "extra.categories";
                    section2 = config.getConfigurationSection(section2String);
                    if (section2 != null && section2.getKeys(false).size() == 0)
                    {
                      config.set(section2String, null);
                    }
                    Variable.customRecipes.put(args[2], config);
                    customRecipeListConfig.saveConfig();
                    if (inputCategoryMin != inputCategoryMax)
                    {
                      MessageUtil.sendMessage(
                              sender, Prefix.INFO_CUSTOM_RECIPE,
                              "커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 대한 커스텀 레시피 목록 rg255,204;" +
                                      args[5] +
                                      "&r에 포함되어 있는 레시피" +
                                      (inputCategoryMin == -1 ? ("의 최소 제작 횟수 조건을 제거") : ("의 최소 제작 횟수 조건을 rg255,204;" + Constant.Sosu15.format(inputCategoryMin) + "&r으로 지정")) +
                                      "하였고, 최대 제작 횟수 조건을 " +
                                      (inputCategoryMax == -1 ? ("제거") : (Constant.THE_COLOR_HEX + Constant.Sosu15.format(inputCategoryMax) + "&r으로 지정")) +
                                      "했습니다");
                    }
                    else if (inputCategoryMin == -1)
                    {
                      MessageUtil.sendMessage(
                              sender, Prefix.INFO_CUSTOM_RECIPE,
                              "커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 대한 커스텀 레시피 목록 rg255,204;" +
                                      args[5] +
                                      "&r에 포함되어 있는 레시피의 최대, 최소 제작 조건을 제거했습니다");
                    }
                    else
                    {
                      MessageUtil.sendMessage(sender, Prefix.INFO_CUSTOM_RECIPE,
                              "커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 대한 커스텀 레시피 목록 rg255,204;" +
                                      args[5] +
                                      "&r에 포함되어 있는 레시피의 최대, 최소 제작 조건을 rg255,204;" +
                                      Constant.Sosu15.format(inputCategoryMin) +
                                      "&r으로 지정했습니다");
                    }
                    break;
                  default:
                    MessageUtil.wrongArg(sender, 6, args);
                    MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " require <category|recipe> ...");
                    return true;
                }
                break;
              case "statistic":
                if (args.length < 7)
                {
                  MessageUtil.shortArg(sender, 7, args);
                  MessageUtil.commandInfo(sender, label, "edit category " + args[2] + " statistic <entity|general|material> ...");
                  return true;
                }
                switch (args[4])
                {
                  case "general":
                    if (args.length > 8)
                    {
                      MessageUtil.longArg(sender, 8, args);
                      MessageUtil.commandInfo(sender, label, "edit category " + args[2] + " statistic general <통계 이름> <min> [max]");
                      return true;
                    }
                    Statistic statistic;
                    try
                    {
                      statistic = Statistic.valueOf(args[5].toUpperCase());
                    }
                    catch (Exception e)
                    {
                      MessageUtil.wrongArg(sender, 6, args);
                      return true;
                    }
                    switch (statistic)
                    {
                      case DROP:
                      case CRAFT_ITEM:
                      case PICKUP:
                      case USE_ITEM:
                      case BREAK_ITEM:
                      case MINE_BLOCK:
                        MessageUtil.sendError(sender, "잘못된 통계입니다. 해당 통계는 material 종류의 통계에서만 사용할 수 있습니다");
                        return true;
                      case KILL_ENTITY:
                      case ENTITY_KILLED_BY:
                        MessageUtil.sendError(sender, "잘못된 통계입니다. 해당 통계는 entity 종류의 통계에서만 사용할 수 있습니다");
                        return true;
                      default:
                        break;
                    }
                    if (!MessageUtil.isInteger(sender, args[6], true))
                    {
                      return true;
                    }
                    int inputGeneralMin = Integer.parseInt(args[6]);
                    if (inputGeneralMin != -1 && !MessageUtil.checkNumberSize(sender, inputGeneralMin, 0, Integer.MAX_VALUE))
                    {
                      return true;
                    }
                    if (args.length == 7 && !MessageUtil.isInteger(sender, args[6], true))
                    {
                      return true;
                    }
                    int inputGeneralMax = args.length == 8 ? Integer.parseInt(args[7]) : -1;
                    if (inputGeneralMax != -1 && !MessageUtil.checkNumberSize(sender, inputGeneralMax, Math.max(0, inputGeneralMin), Integer.MAX_VALUE))
                    {
                      return true;
                    }
                    String statisticName = statistic.toString(), statisticString = statistic.toString();
                    int configGeneralMin = config.getInt("extra.statistics.general." + statisticString + ".min");
                    int configGeneralMax = config.getInt("extra.statistics.general." + statisticString + ".max");
                    boolean configGeneralMinExists = config.contains("extra.statistics.general." + statisticString + ".min");
                    boolean configGeneralMaxExists = config.contains("extra.statistics.general." + statisticString + ".max");
                    if (inputGeneralMin == -1 && (!configGeneralMinExists || configGeneralMin == -1) && inputGeneralMax == -1 && (!configGeneralMaxExists || configGeneralMax == -1))
                    {
                      MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r의 통계값 rg255,204;" + statisticName + "&r의 최대, 최소값 조건이 없습니다");
                      return true;
                    }
                    if (configGeneralMinExists && inputGeneralMin == configGeneralMin && configGeneralMaxExists && inputGeneralMax == configGeneralMax)
                    {
                      MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" +
                              args[2] +
                              "&r의 통계값 rg255,204;" +
                              statisticName +
                              "&r최소값 조건이 rg255,204;" +
                              Constant.Sosu15.format(inputGeneralMin) +
                              "&r, 최대값 조건이 rg255,204;" +
                              Constant.Sosu15.format(inputGeneralMax) +
                              "&r입니다");
                      return true;
                    }
                    config.set("extra.statistics.general." + statisticString + ".min", inputGeneralMin == -1 ? null : inputGeneralMin);
                    config.set("extra.statistics.general." + statisticString + ".max", inputGeneralMax == -1 ? null : inputGeneralMax);

                    String sectionString = "extra.statistics.general." + statisticString;
                    ConfigurationSection section1 = config.getConfigurationSection(sectionString);
                    if (section1 != null && section1.getKeys(false).size() == 0)
                    {
                      config.set(sectionString, null);
                    }

                    sectionString = "extra.statistics.general";
                    section1 = config.getConfigurationSection(sectionString);
                    if (section1 != null && section1.getKeys(false).size() == 0)
                    {
                      config.set(sectionString, null);
                    }

                    sectionString = "extra.statistics";
                    section1 = config.getConfigurationSection(sectionString);
                    if (section1 != null && section1.getKeys(false).size() == 0)
                    {
                      config.set(sectionString, null);
                    }
                    Variable.customRecipes.put(args[2], config);
                    customRecipeListConfig.saveConfig();
                    if (inputGeneralMin != inputGeneralMax)
                    {
                      MessageUtil.sendMessage(
                              sender, Prefix.INFO_CUSTOM_RECIPE,
                              "커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r의 통계값 rg255,204;" +
                                      statisticName +
                                      "&r" +
                                      (inputGeneralMin == -1 ? ("의 최소값 조건을 제거") : ("의 최소값 조건을 rg255,204;" + Constant.Sosu15.format(inputGeneralMin) + "&r으로 지정")) +
                                      "하였고, 최대값 조건을 " +
                                      (inputGeneralMax == -1 ? ("제거") : (Constant.THE_COLOR_HEX + Constant.Sosu15.format(inputGeneralMax) + "&r으로 지정")) +
                                      "했습니다");
                    }
                    else if (inputGeneralMin == -1)
                    {
                      MessageUtil.sendMessage(sender,
                              Prefix.INFO_CUSTOM_RECIPE, "커스텀 레시피 목록 rg255,204;" + args[2] + "&r의 통계값 rg255,204;" + statistic + "&r의 최대, 최소값 조건을 제거했습니다");
                    }
                    else
                    {
                      MessageUtil.sendMessage(sender, Prefix.INFO_CUSTOM_RECIPE,
                              "커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r의 통계값 rg255,204;" +
                                      statisticName +
                                      "&r의 최대, 최소값 조건을 rg255,204;" +
                                      Constant.Sosu15.format(inputGeneralMin) +
                                      "&r으로 지정했습니다");
                    }
                    break;
                  case "entity":
                    if (args.length < 8)
                    {
                      MessageUtil.shortArg(sender, 8, args);
                      MessageUtil.commandInfo(sender, label, "edit category " + args[2] + " statistic entity <통계 이름> <개체 종류> <min> [max]");
                      return true;
                    }
                    try
                    {
                      statistic = Statistic.valueOf(args[5].toUpperCase());
                    }
                    catch (Exception e)
                    {
                      MessageUtil.wrongArg(sender, 6, args);
                      return true;
                    }
                    switch (statistic)
                    {
                      case DROP:
                      case CRAFT_ITEM:
                      case PICKUP:
                      case USE_ITEM:
                      case BREAK_ITEM:
                      case MINE_BLOCK:
                        MessageUtil.sendError(sender, "잘못된 통계입니다. 해당 통계는 material 종류의 통계에서만 사용할 수 있습니다");
                        return true;
                      case KILL_ENTITY:
                      case ENTITY_KILLED_BY:
                        break;
                      default:
                        MessageUtil.sendError(sender, "잘못된 통계입니다. 해당 통계는 general 종류의 통계에서만 사용할 수 있습니다");
                        return true;
                    }
                    EntityType entityType;
                    try
                    {
                      entityType = EntityType.valueOf(args[6].toUpperCase());
                    }
                    catch (Exception e)
                    {
                      MessageUtil.wrongArg(sender, 7, args);
                      return true;
                    }
                    if (!MessageUtil.isInteger(sender, args[7], true))
                    {
                      return true;
                    }
                    int inputEntityMin = Integer.parseInt(args[7]);
                    if (inputEntityMin != -1 && !MessageUtil.checkNumberSize(sender, inputEntityMin, 0, Integer.MAX_VALUE))
                    {
                      return true;
                    }
                    if (args.length == 8 && !MessageUtil.isInteger(sender, args[7], true))
                    {
                      return true;
                    }
                    int inputEntityMax = args.length == 9 ? Integer.parseInt(args[8]) : -1;
                    if (inputEntityMax != -1 && !MessageUtil.checkNumberSize(sender, inputEntityMax, Math.max(0, inputEntityMin), Integer.MAX_VALUE))
                    {
                      return true;
                    }
                    statisticName = statistic.toString();
                    statisticString = statistic.toString();
                    String entityTypeString = entityType.toString();
                    int configEntityMin = config.getInt("extra.statistics.entity." + statisticString + "." + entityTypeString + ".min");
                    int configEntityMax = config.getInt("extra.statistics.entity." + statisticString + "." + entityTypeString + ".max");
                    boolean configEntityMinExists = config.contains("extra.statistics.entity." + statisticString + "." + entityTypeString + ".min");
                    boolean configEntityMaxExists = config.contains("extra.statistics.entity." + statisticString + "." + entityTypeString + ".max");
                    if (inputEntityMin == -1 && (!configEntityMinExists || configEntityMin == -1) && inputEntityMax == -1 && (!configEntityMaxExists || configEntityMax == -1))
                    {
                      MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r의 통계값 rg255,204;" + statisticName + "&r의 최대, 최소값 조건이 없습니다");
                      return true;
                    }
                    if (configEntityMinExists && inputEntityMin == configEntityMin && configEntityMaxExists && inputEntityMax == configEntityMax)
                    {
                      MessageUtil.sendError(
                              sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r의 통계값 rg255,204;" +
                                      statisticName +
                                      "&r최소값 조건이 rg255,204;" +
                                      Constant.Sosu15.format(inputEntityMin) +
                                      "&r, 최대값 조건이 rg255,204;" +
                                      Constant.Sosu15.format(inputEntityMax) +
                                      "&r입니다");
                      return true;
                    }
                    config.set("extra.statistics.entity." + statisticString + "." + entityTypeString + ".min", inputEntityMin == -1 ? null : inputEntityMin);
                    config.set("extra.statistics.entity." + statisticString + "." + entityTypeString + ".max", inputEntityMax == -1 ? null : inputEntityMax);

                    sectionString = "extra.statistics.entity." + statisticString + "." + entityTypeString;
                    ConfigurationSection section2 = config.getConfigurationSection(sectionString);
                    if (section2 != null && section2.getKeys(false).size() == 0)
                    {
                      config.set(sectionString, null);
                    }

                    sectionString = "extra.statistics.entity." + statisticString;
                    section2 = config.getConfigurationSection(sectionString);
                    if (section2 != null && section2.getKeys(false).size() == 0)
                    {
                      config.set(sectionString, null);
                    }

                    sectionString = "extra.statistics.entity";
                    section2 = config.getConfigurationSection(sectionString);
                    if (section2 != null && section2.getKeys(false).size() == 0)
                    {
                      config.set(sectionString, null);
                    }

                    sectionString = "extra.statistics";
                    section2 = config.getConfigurationSection(sectionString);
                    if (section2 != null && section2.getKeys(false).size() == 0)
                    {
                      config.set(sectionString, null);
                    }

                    Variable.customRecipes.put(args[2], config);
                    customRecipeListConfig.saveConfig();
                    if (inputEntityMin != inputEntityMax)
                    {
                      MessageUtil.sendMessage(
                              sender, Prefix.INFO_CUSTOM_RECIPE,
                              "커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r의 통계값 rg255,204;" +
                                      statisticName +
                                      "&r" +
                                      (inputEntityMin == -1 ? ("의 최소값 조건을 제거") : ("의 최소값 조건을 rg255,204;" + Constant.Sosu15.format(inputEntityMin) + "&r으로 지정")) +
                                      "하였고, 최대값 조건을 " +
                                      (inputEntityMax == -1 ? ("제거") : (Constant.THE_COLOR_HEX + Constant.Sosu15.format(inputEntityMax) + "&r으로 지정")) +
                                      "했습니다");
                    }
                    else if (inputEntityMin == -1)
                    {
                      MessageUtil.sendMessage(sender, Prefix.INFO_CUSTOM_RECIPE,
                              "커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r의 통계값 rg255,204;" +
                                      statisticName +
                                      "&r의 최대, 최소값 조건을 제거했습니다" +
                                      ".");
                    }
                    else
                    {
                      MessageUtil.sendMessage(
                              sender, Prefix.INFO_CUSTOM_RECIPE,
                              "커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r의 통계값 rg255,204;" +
                                      "&r의 통계값 rg255,204;" +
                                      statisticName +
                                      "&r의 최대, 최소값 조건을 rg255,204;" +
                                      Constant.Sosu15.format(inputEntityMin) +
                                      "&r으로 지정했습니다");
                    }
                    break;
                  case "material":
                    if (args.length < 8)
                    {
                      MessageUtil.shortArg(sender, 8, args);
                      MessageUtil.commandInfo(sender, label, "edit category " + args[2] + " statistic material <통계 이름> <아이템 종류> <min> [max]");
                      return true;
                    }
                    try
                    {
                      statistic = Statistic.valueOf(args[5].toUpperCase());
                    }
                    catch (Exception e)
                    {
                      MessageUtil.wrongArg(sender, 6, args);
                      return true;
                    }
                    switch (statistic)
                    {
                      case DROP:
                      case CRAFT_ITEM:
                      case PICKUP:
                      case USE_ITEM:
                      case BREAK_ITEM:
                      case MINE_BLOCK:
                        break;
                      case KILL_ENTITY:
                      case ENTITY_KILLED_BY:
                        MessageUtil.sendError(sender, "잘못된 통계입니다. 해당 통계는 entity 종류의 통계에서만 사용할 수 있습니다");
                        return true;
                      default:
                        MessageUtil.sendError(sender, "잘못된 통계입니다. 해당 통계는 general 종류의 통계에서만 사용할 수 있습니다");
                        return true;
                    }
                    Material material;
                    try
                    {
                      material = Material.valueOf(args[6].toUpperCase());
                    }
                    catch (Exception e)
                    {
                      MessageUtil.wrongArg(sender, 7, args);
                      return true;
                    }
                    if (!MessageUtil.isInteger(sender, args[7], true))
                    {
                      return true;
                    }
                    int inputMaterialMin = Integer.parseInt(args[7]);
                    if (inputMaterialMin != -1 && !MessageUtil.checkNumberSize(sender, inputMaterialMin, 0, Integer.MAX_VALUE))
                    {
                      return true;
                    }
                    if (args.length == 8 && !MessageUtil.isInteger(sender, args[7], true))
                    {
                      return true;
                    }
                    int inputMaterialMax = args.length == 9 ? Integer.parseInt(args[8]) : -1;
                    if (inputMaterialMax != -1 && !MessageUtil.checkNumberSize(sender, inputMaterialMax, Math.max(0, inputMaterialMin), Integer.MAX_VALUE))
                    {
                      return true;
                    }
                    statisticName = statistic.toString();
                    statisticString = statistic.toString();
                    String materialString = material.toString();
                    int configMaterialMin = config.getInt("extra.statistics.material." + statisticString + "." + materialString + ".min");
                    int configMaterialMax = config.getInt("extra.statistics.material." + statisticString + "." + materialString + ".max");
                    boolean configMaterialMinExists = config.contains("extra.statistics.material." + statisticString + "." + materialString + ".min");
                    boolean configMaterialMaxExists = config.contains("extra.statistics.material." + statisticString + "." + materialString + ".max");
                    if (inputMaterialMin == -1 && (!configMaterialMinExists || configMaterialMin == -1) && inputMaterialMax == -1 && (!configMaterialMaxExists || configMaterialMax == -1))
                    {
                      MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r의 통계값 rg255,204;" + statisticName + "&r의 최대, 최소값 조건이 없습니다");
                      return true;
                    }
                    if (configMaterialMinExists && inputMaterialMin == configMaterialMin && configMaterialMaxExists && inputMaterialMax == configMaterialMax)
                    {
                      MessageUtil.sendError(
                              sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r의 통계값 rg255,204;" +
                                      statisticName +
                                      "&r최소값 조건이 rg255,204;" +
                                      Constant.Sosu15.format(inputMaterialMin) +
                                      "&r, 최대값 조건이 rg255,204;" +
                                      Constant.Sosu15.format(inputMaterialMax) +
                                      "&r입니다");
                      return true;
                    }
                    config.set("extra.statistics.material." + statisticString + "." + materialString + ".min", inputMaterialMin == -1 ? null : inputMaterialMin);
                    config.set("extra.statistics.material." + statisticString + "." + materialString + ".max", inputMaterialMax == -1 ? null : inputMaterialMax);

                    sectionString = "extra.statistics.material." + statisticString + "." + materialString;
                    section = config.getConfigurationSection(sectionString);
                    if (section != null && section.getKeys(false).size() == 0)
                    {
                      config.set(sectionString, null);
                    }

                    sectionString = "extra.statistics.material." + statisticString;
                    section = config.getConfigurationSection(sectionString);
                    if (section != null && section.getKeys(false).size() == 0)
                    {
                      config.set(sectionString, null);
                    }

                    sectionString = "extra.statistics.material";
                    section = config.getConfigurationSection(sectionString);
                    if (section != null && section.getKeys(false).size() == 0)
                    {
                      config.set(sectionString, null);
                    }

                    sectionString = "extra.statistics";
                    section = config.getConfigurationSection(sectionString);
                    if (section != null && section.getKeys(false).size() == 0)
                    {
                      config.set(sectionString, null);
                    }

                    Variable.customRecipes.put(args[2], config);
                    customRecipeListConfig.saveConfig();
                    if (inputMaterialMin != inputMaterialMax)
                    {
                      MessageUtil.sendMessage(
                              sender, Prefix.INFO_CUSTOM_RECIPE,
                              "커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r의 통계값 rg255,204;" +
                                      statisticName +
                                      "&r" +
                                      (inputMaterialMin == -1 ? ("의 최소값 조건을 제거") : ("의 최소값 조건을 rg255,204;" + Constant.Sosu15.format(inputMaterialMin) + "&r으로 지정")) +
                                      "하였고, 최대값 조건을 " +
                                      (inputMaterialMax == -1 ? ("제거") : (Constant.THE_COLOR_HEX + Constant.Sosu15.format(inputMaterialMax) + "&r으로 지정")) +
                                      "했습니다");
                    }
                    else if (inputMaterialMin == -1)
                    {
                      MessageUtil.sendMessage(sender,
                              Prefix.INFO_CUSTOM_RECIPE, "커스텀 레시피 목록 rg255,204;" + args[2] + "&r의 통계값 rg255,204;" + statisticName + "&r의 최대, 최소값 조건을 제거했습니다");
                    }
                    else
                    {
                      MessageUtil.sendMessage(
                              sender, Prefix.INFO_CUSTOM_RECIPE,
                              "커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r의 통계값 rg255,204;" +
                                      "&r의 통계값 rg255,204;" +
                                      statisticName +
                                      "&r의 최대, 최소값 조건을 rg255,204;" +
                                      Constant.Sosu15.format(inputMaterialMin) +
                                      "&r으로 지정했습니다");
                    }
                    break;
                  default:
                    MessageUtil.wrongArg(sender, 5, args);
                    MessageUtil.commandInfo(sender, label, "edit category " + args[2] + " statistic <entity|general|material> ...");
                    return true;
                }
                break;
              case "display":
                if (args.length > 5)
                {
                  MessageUtil.longArg(sender, 5, args);
                  MessageUtil.commandInfo(sender, label, "edit category " + args[2] + " display <레시피 목록 표시 이름>");
                  return true;
                }
                String inputDisplay = args[4];
                String configDisplay = config.getString("extra.display");
                if (inputDisplay.equals("--remove") && (configDisplay == null || configDisplay.equals(args[2])))
                {
                  MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r의 표시 이름이 없습니다");
                  return true;
                }
                else if (inputDisplay.equals(configDisplay))
                {
                  MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r의 표시 이름이 rg255,204;" + inputDisplay + "&r입니다");
                  return true;
                }
                config.set("extra.display", inputDisplay.equals("--remove") ? args[2] : inputDisplay);
                Variable.customRecipes.put(args[2], config);
                customRecipeListConfig.saveConfig();
                MessageUtil.sendMessage(
                        sender, Prefix.INFO_CUSTOM_RECIPE,
                        "커스텀 레시피 목록 rg255,204;" +
                                args[2] +
                                (inputDisplay.equals("--remove") ? ("&r의 표시 이름을 제거했습니다") : ("&r의 표시 이름을 rg255,204;" +
                                        inputDisplay +
                                        "&r" +
                                        MessageUtil.getFinalConsonant(inputDisplay, ConsonantType.으로) +
                                        " 지정했습니다")));
                break;
              case "biome":
                if (args.length > 5)
                {
                  MessageUtil.longArg(sender, 5, args);
                  MessageUtil.commandInfo(sender, label, "edit category " + args[2] + " biome <생물 군계>");
                  return true;
                }
                inputDisplay = args[4];
                Biome biome;
                try
                {
                  biome = Biome.valueOf(inputDisplay.toUpperCase());
                }
                catch (Exception e)
                {
                  if (inputDisplay.equals("--remove"))
                  {
                    biome = null;
                  }
                  else
                  {
                    MessageUtil.wrongArg(sender, 5, args);
                    return true;
                  }
                }
                String biomeName = biome.toString();
                configDisplay = config.getString("extra.biome");
                if (inputDisplay.equals("--remove") && configDisplay == null)
                {
                  MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r의 생물 군계 조건이 없습니다");
                  return true;
                }
                else if (inputDisplay.equals(configDisplay))
                {
                  MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r의 생물 군계 조건이 rg255,204;" + biomeName + "&r입니다");
                  return true;
                }
                config.set("extra.biome", inputDisplay.equals("--remove") ? null : inputDisplay.toUpperCase());
                Variable.customRecipes.put(args[2], config);
                customRecipeListConfig.saveConfig();
                MessageUtil.sendMessage(
                        sender, Prefix.INFO_CUSTOM_RECIPE,
                        "커스텀 레시피 목록 rg255,204;" +
                                args[2] +
                                (inputDisplay.equals("--remove") ? ("&r의 생물 군계 조건을 제거했습니다") : ("&r의 생물 군계 조건을 rg255,204;" +
                                        biomeName +
                                        "&r" +
                                        MessageUtil.getFinalConsonant(biomeName, ConsonantType.으로) +
                                        " 지정했습니다")));
                break;
              case "belowblock":
                if (args.length > 5)
                {
                  MessageUtil.longArg(sender, 5, args);
                  MessageUtil.commandInfo(sender, label, "edit category " + args[2] + " belowblock <블록>");
                  return true;
                }
                inputDisplay = args[4];
                Material blockType;
                try
                {
                  blockType = Material.valueOf(inputDisplay.toUpperCase());
                }
                catch (Exception e)
                {
                  if (inputDisplay.equals("--remove"))
                  {
                    blockType = null;
                  }
                  else
                  {
                    MessageUtil.wrongArg(sender, 5, args);
                    return true;
                  }
                }
                if (blockType != null && !blockType.isBlock())
                {
                  MessageUtil.sendError(sender, "블록만 입력할 수 있습니다 (rg255,204;" + inputDisplay + "&r)");
                  return true;
                }
                String blockTypeName = null;
                if (blockType != null)
                {
                  blockTypeName = ItemNameUtil.itemName(blockType).toString();
                }
                configDisplay = config.getString("extra.belowblock");
                if (inputDisplay.equals("--remove") && configDisplay == null)
                {
                  MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r의 밟고 있는 블록 조건이 없습니다");
                  return true;
                }
                else if (inputDisplay.equals(configDisplay))
                {
                  MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r의 밟고 있는 블록 조건이 rg255,204;" + blockTypeName + "&r입니다");
                  return true;
                }
                config.set("extra.belowblock", inputDisplay.equals("--remove") ? null : inputDisplay.toUpperCase());
                Variable.customRecipes.put(args[2], config);
                customRecipeListConfig.saveConfig();
                MessageUtil.sendMessage(
                        sender, Prefix.INFO_CUSTOM_RECIPE,
                        "커스텀 레시피 목록 rg255,204;" +
                                args[2] +
                                (inputDisplay.equals("--remove") ? ("&r의 밟고 있는 블록 조건을 제거했습니다") : ("&r의 밟고 있는 블록 조건을 rg255,204;" +
                                        blockTypeName +
                                        "&r" +
                                        MessageUtil.getFinalConsonant(blockTypeName, ConsonantType.으로) +
                                        " 지정했습니다")));
                break;
              case "target-block":
                if (args.length > 5)
                {
                  MessageUtil.longArg(sender, 5, args);
                  MessageUtil.commandInfo(sender, label, "edit category " + args[2] + " target-block <블록>");
                  return true;
                }
                inputDisplay = args[4];
                boolean removal = inputDisplay.equals("--remove");
                try
                {
                  blockType = Material.valueOf(inputDisplay.toUpperCase());
                }
                catch (Exception e)
                {
                  if (removal)
                  {
                    blockType = null;
                  }
                  else
                  {
                    MessageUtil.wrongArg(sender, 5, args);
                    return true;
                  }
                }
                if (blockType != null && !blockType.isBlock())
                {
                  MessageUtil.sendError(sender, "블록만 입력할 수 있습니다 (rg255,204;" + inputDisplay + "&r)");
                  return true;
                }
                configDisplay = config.getString("extra.target-block");
                if (removal && configDisplay == null)
                {
                  MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 %s의 바라보고 있는 블록 조건이 없습니다", Constant.THE_COLOR_HEX + args[2]);
                  return true;
                }
                else if (inputDisplay.equals(configDisplay))
                {
                  MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 %s의 바라보고 있는 블록 조건이 %s입니다", Constant.THE_COLOR_HEX + args[2], blockType);
                  return true;
                }
                config.set("extra.target-block", removal ? null : inputDisplay.toUpperCase());
                Variable.customRecipes.put(args[2], config);
                customRecipeListConfig.saveConfig();
                MessageUtil.sendMessage(sender, Prefix.INFO_CUSTOM_RECIPE, "커스텀 레시피 목록 %s의 바라보고 있는 블록 조건을 " + (removal ? "제거했습니다" : "%s(으)로 설정했습니다"), removal ? "null" : blockType);
                break;
              case "decorative":
                if (args.length > 5)
                {
                  MessageUtil.longArg(sender, 5, args);
                  MessageUtil.commandInfo(sender, label, "edit category " + args[2] + " decorative <꾸미기용 목록 모드>");
                  return true;
                }
                String inputBoolean = args[4];
                if (!MessageUtil.isBoolean(sender, args, 5, true))
                {
                  return true;
                }
                boolean b = inputBoolean.equals("true");
                if (config.getBoolean("extra.decorative") == b)
                {
                  MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 %s은(는) 꾸미기용 목록" + (b ? "입" : "이 아닙") + "니다", Constant.THE_COLOR_HEX + args[2]);
                  return true;
                }
                config.set("extra.decorative", b ? b : null);
                Variable.customRecipes.put(args[2], config);
                customRecipeListConfig.saveConfig();
                MessageUtil.sendMessage(sender, Prefix.INFO_CUSTOM_RECIPE, "커스텀 레시피 목록 %s(은)는 이제 꾸미기용 목록" + (b ? "입" : "이 아닙") + "니다", Constant.THE_COLOR_HEX + args[2]);
                break;
              default:
                MessageUtil.wrongArg(sender, 4, args);
                MessageUtil.commandInfo(sender, label, "edit category " + args[2] + "<level|permssion|require|statistic|wealth> ...");
                return true;
            }
            break;
          case "recipe":
            if (args.length < 6)
            {
              MessageUtil.shortArg(sender, 6, args);
              MessageUtil.commandInfo(sender, label, "edit recipe <레시피 목록 이름> <레시피 이름> <chance|cost|display|level|levelcost|permission|require|reusable|statistic|wealth> ...");
              return true;
            }
            if (args.length > 10 && !args[4].equals("command"))
            {
              MessageUtil.longArg(sender, 10, args);
              MessageUtil.commandInfo(sender, label, "edit recipe <레시피 목록 이름> <레시피 이름> <chance|cost|display|level|levelcost|permission|require|reusable|statistic|wealth> ...");
              return true;
            }
            folder = new File(Cucumbery.getPlugin().getDataFolder() + "/data/CustomRecipe");
            if (!folder.exists())
            {
              boolean success = folder.mkdirs();
              if (!success)
              {
                System.err.println("[Cucumbery] could not create CustomRecipe folder!");
              }
            }
            file = new File(Cucumbery.getPlugin().getDataFolder() + "/data/CustomRecipe/" + args[2] + ".yml");
            if (!file.exists())
            {
              MessageUtil.sendError(sender, "커스텀 레시피 목록 rg255,204;" + args[2] + "&r" + MessageUtil.getFinalConsonant(args[2], ConsonantType.을를) + " 찾을 수 없습니다");
              return true;
            }
            customRecipeListConfig = CustomConfig.getCustomConfig("data/CustomRecipe/" + args[2] + ".yml");
            config = customRecipeListConfig.getConfig();
            ConfigurationSection recipe = config.getConfigurationSection("recipes." + args[3]);
            if (recipe == null || recipe.getKeys(false).isEmpty())
            {
              MessageUtil.sendError(sender, "커스텀 레시피 목록 rg255,204;" + args[1] + "&r에서 해당 레시피(rg255,204;" + args[2] + "&r)를 찾을 수 없습니다");
              return true;
            }
            switch (args[4])
            {
              case "command":
                if (Method.equals(args[5], "craft", "success", "failure"))
                {
                  String commandTypeStr = "";
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
                  List<String> commands = config.getStringList("recipes." + args[3] + ".extra.commands." + args[5]);
                  if (args.length < 7)
                  {
                    MessageUtil.shortArg(sender, 7, args);
                    MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " command " + args[5] + " <add|insert|list|remove|set> ...");
                    return true;
                  }
                  switch (args[6])
                  {
                    case "list":
                      if (commands.size() == 0)
                      {
                        MessageUtil.sendError(sender, "커스텀 레시피 목록 rg255,204;" + args[2] + "&r에 있는 레시피 rg255,204;" + args[3] + "&r의 " + commandTypeStr + " 시 실행되는 명령어가 없습니다");
                        return true;
                      }
                      MessageUtil.sendMessage(sender, Prefix.INFO_CUSTOM_RECIPE,
                              "커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[3] +
                                      "&r의 " +
                                      commandTypeStr +
                                      " 시 실행되는 명령어의 개수는 rg255,204;" +
                                      commands.size() +
                                      "개&r입니다");
                      for (int i = 0; i < commands.size(); i++)
                      {
                        MessageUtil.sendMessage(sender, Prefix.INFO_CUSTOM_RECIPE, Constant.THE_COLOR_HEX + (i + 1) + "번째&r 명령어 : rg255,204;/" + commands.get(i));
                      }
                      break;
                    case "add":
                      if (args.length < 8)
                      {
                        MessageUtil.shortArg(sender, 7, args);
                        MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " command " + args[5] + " add <" + commandTypeStr + " 시 실행되는 명령어>");
                        return true;
                      }
                      String command = MessageUtil.listToString(" ", 7, args.length, args);
                      commands.add(command);
                      MessageUtil.sendMessage(sender, Prefix.INFO_CUSTOM_RECIPE,
                              "커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[3] +
                                      "&r의 " +
                                      commandTypeStr +
                                      " 시 실행되는 명령어의 rg255,204;" +
                                      commands.size() +
                                      "번째&r 줄에 rg255,204;/" +
                                      command +
                                      "&r" +
                                      MessageUtil.getFinalConsonant(command, ConsonantType.이라) +
                                      "는 명령어를 추가했습니다");
                      config.set("recipes." + args[3] + ".extra.commands." + args[5], commands);
                      Variable.customRecipes.put(args[2], config);
                      customRecipeListConfig.saveConfig();
                      break;
                    case "remove":
                      if (commands.size() == 0)
                      {
                        MessageUtil.sendError(sender, "커스텀 레시피 목록 rg255,204;" + args[2] + "&r에 있는 레시피 rg255,204;" + args[3] + "&r의 " + commandTypeStr + " 시 실행되는 명령어가 없습니다");
                        return true;
                      }
                      if (args.length == 7)
                      {
                        MessageUtil.sendMessage(sender, Prefix.INFO_CUSTOM_RECIPE,
                                "커스텀 레시피 목록 rg255,204;" +
                                        args[2] +
                                        "&r에 있는 레시피 rg255,204;" +
                                        args[3] +
                                        "&r의 " +
                                        commandTypeStr +
                                        " 시 실행되는 명령어의 rg255,204;" +
                                        commands.size() +
                                        "번째&r 줄의 명령어를 제거했습니다");
                        commands.remove(commands.size() - 1);
                      }
                      else
                      {
                        if (args.length == 8)
                        {
                          if (args[7].equals("--all"))
                          {
                            MessageUtil.sendMessage(
                                    sender, Prefix.INFO_CUSTOM_RECIPE,
                                    "커스텀 레시피 목록 rg255,204;" +
                                            args[2] +
                                            "&r에 있는 레시피 rg255,204;" +
                                            args[3] +
                                            "&r의 " +
                                            commandTypeStr +
                                            " 시 실행되는 모든 명령어를 제거했습니다");
                            commands = null;
                          }
                          else
                          {
                            if (!MessageUtil.isInteger(sender, args[7], true))
                            {
                              return true;
                            }
                            int line = Integer.parseInt(args[7]);
                            if (!MessageUtil.checkNumberSize(sender, line, 1, commands.size()))
                            {
                              return true;
                            }
                            MessageUtil.sendMessage(sender, Prefix.INFO_CUSTOM_RECIPE,
                                    "커스텀 레시피 목록 rg255,204;" +
                                            args[2] +
                                            "&r에 있는 레시피 rg255,204;" +
                                            args[3] +
                                            "&r의 " +
                                            commandTypeStr +
                                            " 시 실행되는 명령어의 rg255,204;" +
                                            line +
                                            "번째&r 줄의 명령어를 제거했습니다");
                            commands.remove(line - 1);
                          }
                        }
                        else
                        {
                          MessageUtil.longArg(sender, 8, args);
                          MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " command " + args[5] + " remove [제거할 " + commandTypeStr + " 시 실행되는 명령어 줄|--all]");
                          return true;
                        }
                      }
                      config.set("recipes." + args[3] + ".extra.commands." + args[5], commands);
                      ConfigurationSection section = config.getConfigurationSection("recipes." + args[3] + ".extra.commands");
                      if (section != null && section.getKeys(false).size() == 0)
                      {
                        config.set("recipes." + args[3] + ".extra.commands", null);
                      }
                      Variable.customRecipes.put(args[2], config);
                      customRecipeListConfig.saveConfig();
                      break;
                    case "set":
                      if (args.length < 9)
                      {
                        MessageUtil.shortArg(sender, 7, args);
                        MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " command " + args[5] + " set <줄> <" + commandTypeStr + " 시 실행되는 명령어>");
                        return true;
                      }
                      if (commands.size() == 0)
                      {
                        MessageUtil.sendError(sender, "커스텀 레시피 목록 rg255,204;" + args[2] + "&r에 있는 레시피 rg255,204;" + args[3] + "&r의 " + commandTypeStr + " 시 실행되는 명령어가 없습니다");
                        return true;
                      }
                      if (!MessageUtil.isInteger(sender, args[7], true))
                      {
                        return true;
                      }
                      int line = Integer.parseInt(args[7]);
                      if (!MessageUtil.checkNumberSize(sender, line, 1, commands.size()))
                      {
                        return true;
                      }
                      command = MessageUtil.listToString(" ", 8, args.length, args);
                      commands.set(line - 1, command);
                      MessageUtil.sendMessage(sender, Prefix.INFO_CUSTOM_RECIPE,
                              "커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[3] +
                                      "&r의 " +
                                      commandTypeStr +
                                      " 시 실행되는 명령어의 rg255,204;" +
                                      line +
                                      "번째&r 줄에 rg255,204;/" +
                                      command +
                                      "&r" +
                                      MessageUtil.getFinalConsonant(command, ConsonantType.이라) +
                                      "는 명령어를 설정했습니다");
                      config.set("recipes." + args[3] + ".extra.commands." + args[5], commands);
                      Variable.customRecipes.put(args[2], config);
                      customRecipeListConfig.saveConfig();
                      break;
                    case "insert":
                      if (args.length < 9)
                      {
                        MessageUtil.shortArg(sender, 7, args);
                        MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " command " + args[5] + " insert <줄> <" + commandTypeStr + " 시 실행되는 명령어>");
                        return true;
                      }
                      if (commands.size() == 0)
                      {
                        MessageUtil.sendError(sender, "커스텀 레시피 목록 rg255,204;" + args[2] + "&r에 있는 레시피 rg255,204;" + args[3] + "&r의 " + commandTypeStr + " 시 실행되는 명령어가 없습니다");
                        return true;
                      }
                      if (!MessageUtil.isInteger(sender, args[7], true))
                      {
                        return true;
                      }
                      line = Integer.parseInt(args[7]);
                      if (!MessageUtil.checkNumberSize(sender, line, 1, commands.size()))
                      {
                        return true;
                      }
                      command = MessageUtil.listToString(" ", 8, args.length, args);
                      List<String> cloneCommands = new ArrayList<>();
                      for (int i = 0; i < line - 1; i++)
                      {
                        cloneCommands.add(commands.get(i));
                      }
                      cloneCommands.add(command);
                      for (int i = line - 1; i < commands.size(); i++)
                      {
                        cloneCommands.add(commands.get(i));
                      }
                      commands = cloneCommands;
                      MessageUtil.sendMessage(sender, Prefix.INFO_CUSTOM_RECIPE,
                              "커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[3] +
                                      "&r의 " +
                                      commandTypeStr +
                                      " 시 실행되는 명령어의 rg255,204;" +
                                      line +
                                      "번째&r 줄에 rg255,204;/" +
                                      command +
                                      "&r" +
                                      MessageUtil.getFinalConsonant(command, ConsonantType.이라) +
                                      "는 명령어를 들여썼습니다");
                      config.set("recipes." + args[3] + ".extra.commands." + args[5], commands);
                      Variable.customRecipes.put(args[2], config);
                      customRecipeListConfig.saveConfig();
                      break;
                    default:
                      MessageUtil.wrongArg(sender, 7, args);
                      MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " command " + args[5] + " <add|insert|list|remove|set> ...");
                  }
                }
                else
                {
                  MessageUtil.wrongArg(sender, 6, args);
                  MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " command <craft|failure|success> ...");
                }
                break;
              case "permission":
                if (args.length < 7)
                {
                  MessageUtil.shortArg(sender, 6, args);
                  MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " permission <base|set|hideifnoperm> ...");
                  return true;
                }
                if (args.length > 7)
                {
                  MessageUtil.longArg(sender, 7, args);
                  MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " permission <base|set|hideifnoperm> ...");
                  return true;
                }
                switch (args[5])
                {
                  case "base":
                    String inputBasePermission = args[6];
                    String configBasePermission = config.getString("recipes." + args[3] + ".extra.permissions.base-permission");
                    if (inputBasePermission.equals(configBasePermission))
                    {
                      MessageUtil.sendError(sender,
                              "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r에 있는 레시피 rg255,204;" + args[3] + "&r의 제작 요구 퍼미션 노드 값이 rg255,204;" + inputBasePermission + "&r입니다");
                      return true;
                    }
                    if (inputBasePermission.equals("--remove") && configBasePermission == null)
                    {
                      MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r에 있는 레시피 rg255,204;" + args[3] + "&r의 제작 요구 퍼미션 노드 값이 없는 상태입니다");
                      return true;
                    }
                    config.set("recipes." + args[3] + ".extra.permissions.base-permission", inputBasePermission.equals("--remove") ? null : inputBasePermission);

                    String sectionString = "recipes." + args[3] + ".extra.permissions";
                    ConfigurationSection section = config.getConfigurationSection(sectionString);
                    if (section != null && section.getKeys(true).size() == 0)
                    {
                      config.set(sectionString, null);
                    }

                    Variable.customRecipes.put(args[2], config);
                    customRecipeListConfig.saveConfig();
                    MessageUtil.sendMessage(
                            sender, Prefix.INFO_CUSTOM_RECIPE,
                            "커스텀 레시피 목록 §e" +
                                    args[2] +
                                    "&r에 있는 레시피 rg255,204;" +
                                    args[3] +
                                    "&r의 제작 요구 퍼미션 노드 값을 rg255,204;" +
                                    (inputBasePermission.equals("--remove") ? "&r삭제했습니다" : (inputBasePermission +
                                            "&r" +
                                            MessageUtil.getFinalConsonant(inputBasePermission, ConsonantType.으로) +
                                            " 지정했습니다")));
                    break;
                  case "bypass":
                    String inputBypassPermission = args[6];
                    String configBypassPermission = config.getString("recipes." + args[3] + ".extra.permissions.bypass-if-hidden-permission");
                    if (inputBypassPermission.equals(configBypassPermission))
                    {
                      MessageUtil.sendError(sender,
                              "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r에 있는 레시피 rg255,204;" + args[3] + "&r의 비공개 우회 퍼미션 노드 값이 rg255,204;" + inputBypassPermission + "&r입니다");
                      return true;
                    }
                    if (inputBypassPermission.equals("--remove") && configBypassPermission == null)
                    {
                      MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r에 있는 레시피 rg255,204;" + args[3] + "&r의 비공개 우회 퍼미션 노드 값이 없는 상태입니다");
                      return true;
                    }
                    config.set("recipes." + args[3] + ".extra.permissions.bypass-if-hidden-permission", inputBypassPermission.equals("--remove") ? null : inputBypassPermission);
                    sectionString = "recipes." + args[3] + ".extra.permissions";
                    section = config.getConfigurationSection(sectionString);
                    if (section != null && section.getKeys(false).size() == 0)
                    {
                      config.set(sectionString, null);
                    }
                    Variable.customRecipes.put(args[2], config);
                    customRecipeListConfig.saveConfig();
                    MessageUtil.sendMessage(
                            sender, Prefix.INFO_CUSTOM_RECIPE,
                            "커스텀 레시피 목록 §e" +
                                    args[2] +
                                    "&r에 있는 레시피 rg255,204;" +
                                    args[3] +
                                    "&r의 비공개 우회 퍼미션 노드 값을 rg255,204;" +
                                    (inputBypassPermission.equals("--remove") ? "&r삭제했습니다" : (inputBypassPermission + "&r" + MessageUtil.getFinalConsonant(inputBypassPermission, ConsonantType.으로) + " 지정했습니다")));
                    break;
                  case "hide":
                    boolean hideIfNoPerm = false;
                    if (!args[6].equals("true") && !args[6].equals("false"))
                    {
                      MessageUtil.wrongBool(sender, 7, args);
                      return true;
                    }
                    if (args[6].equals("true"))
                    {
                      hideIfNoPerm = true;
                    }
                    if (config.getBoolean("recipes." + args[3] + ".extra.permissions.hide-if-no-base") == hideIfNoPerm)
                    {
                      MessageUtil.sendError(sender,
                              "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r에 있는 레시피 rg255,204;" + args[3] + "&r의 권한이 없을 시 레시피 내용 비공개 값이 rg255,204;" + hideIfNoPerm + "&r입니다");
                      return true;
                    }
                    config.set("recipes." + args[3] + ".extra.permissions.hide-if-no-base", hideIfNoPerm);
                    sectionString = "recipes." + args[3] + ".extra.permissions";
                    section = config.getConfigurationSection(sectionString);
                    if (section != null && section.getKeys(false).size() == 0)
                    {
                      config.set(sectionString, null);
                    }
                    Variable.customRecipes.put(args[2], config);
                    customRecipeListConfig.saveConfig();
                    MessageUtil.sendMessage(sender, Prefix.INFO_CUSTOM_RECIPE,
                            "커스텀 레시피 목록 §e" +
                                    args[2] +
                                    "&r에 있는 레시피 rg255,204;" +
                                    args[3] +
                                    "&r의 권한이 없을 시 레시피 내용 비공개 값을 rg255,204;" +
                                    hideIfNoPerm +
                                    "&r로 지정했습니다" +
                                    ".");
                    break;
                  default:
                    MessageUtil.wrongArg(sender, 6, args);
                    MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " permission <base|set|hideifnoperm> ...");
                    return true;
                }
                break;
              case "chance":
                if (args.length > 6)
                {
                  MessageUtil.longArg(sender, 6, args);
                  MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " chance <제작 성공 확률>");
                  return true;
                }
                if (!MessageUtil.isDouble(sender, args[5], true))
                {
                  return true;
                }
                double inputChance = Double.parseDouble(args[5]);
                if (!MessageUtil.checkNumberSize(sender, inputChance, 0d, 100d))
                {
                  return true;
                }
                double configChance = config.getDouble("recipes." + args[3] + ".extra.chance");
                if (inputChance == 0d && configChance == 0d)
                {
                  MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r에 있는 레시피 rg255,204;" + args[3] + "&r의 제작 성공 확률 기능이 비활성화 상태입니다");
                  return true;
                }
                else if (inputChance == configChance)
                {
                  MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" +
                          args[2] +
                          "&r에 있는 레시피 rg255,204;" +
                          args[3] +
                          "&r의 제작 성공 확률이 rg255,204;" +
                          Constant.Sosu15.format(inputChance) +
                          "%&r입니다");
                  return true;
                }
                config.set("recipes." + args[3] + ".extra.chance", inputChance == 0d ? null : inputChance);
                Variable.customRecipes.put(args[2], config);
                customRecipeListConfig.saveConfig();
                MessageUtil.sendMessage(
                        sender, Prefix.INFO_CUSTOM_RECIPE,
                        "커스텀 레시피 목록 rg255,204;" +
                                args[2] +
                                "&r에 있는 레시피 rg255,204;" +
                                args[3] +
                                (inputChance == 0d ? ("&r의 제작 성공 확률 기능을 비활성화 했습니다") : ("&r의 제작 성공 확률을 rg255,204;" + Constant.Sosu15.format(inputChance) + "%&r로 지정했습니다")));
                break;
              case "craftingtime":
                if (args.length < 7)
                {
                  MessageUtil.shortArg(sender, 7, args);
                  MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " craftingtime <interval|skip|time> ...");
                  return true;
                }
                switch (args[5])
                {
                  case "interval":
                    if (args.length > 7)
                    {
                      MessageUtil.longArg(sender, 7, args);
                      MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " craftingtime interval <제작 주기(초)>");
                      return true;
                    }
                    if (!MessageUtil.isInteger(sender, args[6], true))
                    {
                      return true;
                    }
                    long inputCraftingInterval = Integer.parseInt(args[6]);
                    if (!MessageUtil.checkNumberSize(sender, inputCraftingInterval, 0, Integer.MAX_VALUE))
                    {
                      return true;
                    }
                    inputCraftingInterval *= 1000L;
                    long configCraftingInterval = config.getLong("recipes." + args[3] + ".extra.crafting-time-interval");
                    if (inputCraftingInterval == 0 && configCraftingInterval == 0)
                    {
                      MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r에 있는 레시피 rg255,204;" + args[3] + "&r의 제작 주기가 없습니다");
                      return true;
                    }
                    else if (inputCraftingInterval == configCraftingInterval)
                    {
                      MessageUtil.sendError(
                              sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[3] +
                                      "&r의 제작 주기가 rg255,204;" +
                                      Method.timeFormatMilli(configCraftingInterval, false) +
                                      "&r입니다");
                      return true;
                    }
                    config.set("recipes." + args[3] + ".extra.crafting-time-interval", inputCraftingInterval == 0 ? null : inputCraftingInterval);
                    Variable.customRecipes.put(args[2], config);
                    customRecipeListConfig.saveConfig();
                    MessageUtil.sendMessage(
                            sender, Prefix.INFO_CUSTOM_RECIPE,
                            "커스텀 레시피 목록 rg255,204;" +
                                    args[2] +
                                    "&r에 있는 레시피 rg255,204;" +
                                    args[3] +
                                    (inputCraftingInterval == 0d ? ("&r의 제작 주기를 제거했습니다") : ("&r의 제작 주기를 rg255,204;" +
                                            Method.timeFormatMilli(inputCraftingInterval, false) +
                                            "&r" +
                                            MessageUtil.getFinalConsonant(Method.timeFormatMilli(inputCraftingInterval, false), ConsonantType.으로) +
                                            " 지정했습니다")));
                    break;
                  case "skip":
                    if (!Cucumbery.using_Vault_Economy)
                    {
                      MessageUtil.sendError(sender, "rg255,204;Vault &r플러그인을 사용하고 있지 않습니다");
                      return true;
                    }
                    if (args.length < 8)
                    {
                      MessageUtil.shortArg(sender, 8, args);
                      MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " craftingtime skip <cost|permission|relative> ...");
                      return true;
                    }
                    switch (args[6])
                    {
                      case "cost":
                        if (args.length > 8)
                        {
                          MessageUtil.longArg(sender, 8, args);
                          MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " craftingtime skip cost <제작 시간 스킵 비용>");
                          return true;
                        }
                        if (!MessageUtil.isDouble(sender, args[7], true))
                        {
                          return true;
                        }
                        double inputSkipCost = Double.parseDouble(args[7]);
                        if (!MessageUtil.checkNumberSize(sender, inputSkipCost, 0d, Double.MAX_VALUE))
                        {
                          return true;
                        }
                        double configSkipCost = config.getDouble("recipes." + args[3] + ".extra.crafting-time-skip.cost");
                        if (inputSkipCost == 0d && configSkipCost == 0d)
                        {
                          MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r에 있는 레시피 rg255,204;" + args[3] + "&r의 제작 시간 스킵 비용이 없습니다");
                          return true;
                        }
                        else if (inputSkipCost == configSkipCost)
                        {
                          MessageUtil.sendError(
                                  sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" +
                                          args[2] +
                                          "&r에 있는 레시피 rg255,204;" +
                                          args[3] +
                                          "&r의 제작 시간 스킵 비용이 rg255,204;" +
                                          Constant.Sosu15.format(inputSkipCost) +
                                          "원&r입니다" +
                                          ".");
                          return true;
                        }
                        config.set("recipes." + args[3] + ".extra.crafting-time-skip.cost", inputSkipCost == 0d ? null : inputSkipCost);
                        if (Objects.requireNonNull(config.getConfigurationSection("recipes." + args[3] + ".extra.crafting-time-skip")).getKeys(false).size() == 0)
                        {
                          config.set("recipes." + args[3] + ".extra.crafting-time-skip", null);
                        }
                        Variable.customRecipes.put(args[2], config);
                        customRecipeListConfig.saveConfig();
                        MessageUtil.sendMessage(
                                sender, Prefix.INFO_CUSTOM_RECIPE,
                                "커스텀 레시피 목록 rg255,204;" +
                                        args[2] +
                                        "&r에 있는 레시피 rg255,204;" +
                                        args[3] +
                                        (inputSkipCost == 0d ? ("&r의 제작 시간 스킵 비용을 제거했습니다") : ("&r의 제작 시간 스킵 비용을 rg255,204;" + Constant.Sosu15.format(inputSkipCost) + "원&r으로 지정했습니다")));
                        break;
                      case "permission":
                        if (args.length > 8)
                        {
                          MessageUtil.longArg(sender, 8, args);
                          MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " craftingtime skip permission <제작 시간 스킵 요구 퍼미션|--remove>");
                          return true;
                        }
                        String inputSkipPermission = args[7];
                        String configSkipPermission = config.getString("recipes." + args[3] + ".extra.crafting-time-skip.permission");
                        if (inputSkipPermission.equals(configSkipPermission))
                        {
                          MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" +
                                  args[2] +
                                  "&r에 있는 레시피 rg255,204;" +
                                  args[3] +
                                  "&r의 제작 시간 스킵 요구 퍼미션 노드 값이 rg255,204;" +
                                  inputSkipPermission +
                                  "&r입니다");
                          return true;
                        }
                        if (inputSkipPermission.equals("--remove") && configSkipPermission == null)
                        {
                          MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r에 있는 레시피 rg255,204;" + args[3] + "&r의 제작 시간 스킵 요구 퍼미션 노드 값이 없는 상태입니다");
                          return true;
                        }
                        config.set("recipes." + args[3] + ".extra.crafting-time-skip.permission", inputSkipPermission.equals("--remove") ? null : inputSkipPermission);
                        String sectionString = "recipes." + args[3] + ".extra.crafting-time-skip";
                        ConfigurationSection section = config.getConfigurationSection(sectionString);
                        if (section != null && section.getKeys(false).size() == 0)
                        {
                          config.set("recipes." + args[3] + ".extra.crafting-time-skip", null);
                        }
                        Variable.customRecipes.put(args[2], config);
                        customRecipeListConfig.saveConfig();
                        MessageUtil.sendMessage(
                                sender, Prefix.INFO_CUSTOM_RECIPE,
                                "커스텀 레시피 목록 §e" +
                                        args[2] +
                                        "&r에 있는 레시피 rg255,204;" +
                                        args[3] +
                                        "&r의 제작 시간 스킵 요구 퍼미션 노드 값을 rg255,204;" +
                                        (inputSkipPermission.equals("--remove") ? "&r삭제했습니다" : (inputSkipPermission + "&r" + MessageUtil.getFinalConsonant(inputSkipPermission, ConsonantType.으로) + " 지정했습니다")));
                        break;
                      case "relative":
                        if (args.length > 8)
                        {
                          MessageUtil.longArg(sender, 8, args);
                          MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " craftingtime skip relative <스킵 비용 시간 비례 적용 여부>");
                          return true;
                        }
                        boolean skipCostRelative;
                        if (!args[7].equals("true") && !args[7].equals("false"))
                        {
                          MessageUtil.wrongBool(sender, 8, args);
                          return true;
                        }
                        skipCostRelative = args[7].equals("true");
                        if (config.getBoolean("recipes." + args[3] + ".extra.crafting-time-skip.time-relative") == skipCostRelative)
                        {
                          MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" +
                                  args[2] +
                                  "&r에 있는 레시피 rg255,204;" +
                                  args[3] +
                                  "&r의 스킵 비용 시간 비례 적용 여부 값이 rg255,204;" +
                                  skipCostRelative +
                                  "&r입니다");
                          return true;
                        }
                        config.set("recipes." + args[3] + ".extra.crafting-time-skip.time-relative", skipCostRelative ? true : null);
                        if (Objects.requireNonNull(config.getConfigurationSection("recipes." + args[3] + ".extra.crafting-time-skip")).getKeys(false).size() == 0)
                        {
                          config.set("recipes." + args[3] + ".extra.crafting-time-skip", null);
                        }
                        Variable.customRecipes.put(args[2], config);
                        customRecipeListConfig.saveConfig();
                        MessageUtil.sendMessage(
                                sender, Prefix.INFO_CUSTOM_RECIPE,
                                "커스텀 레시피 목록 §e" +
                                        args[2] +
                                        "&r에 있는 레시피 rg255,204;" +
                                        args[3] +
                                        "&r의 스킵 비용 시간 비례 적용 여부 값을 rg255,204;" +
                                        skipCostRelative +
                                        "&r로 지정했습니다");
                        break;
                      default:
                        MessageUtil.wrongArg(sender, 6, args);
                        MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " craftingtime skip <cost|permission|relative> ...");
                        return true;
                    }
                    break;
                  case "time":
                    if (args.length > 7)
                    {
                      MessageUtil.longArg(sender, 7, args);
                      MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " craftingtime time <제작에 필요한 시간(초)>");
                      return true;
                    }
                    if (!MessageUtil.isInteger(sender, args[6], true))
                    {
                      return true;
                    }
                    long inputCraftingTime = Integer.parseInt(args[6]);
                    if (!MessageUtil.checkNumberSize(sender, inputCraftingTime, 0, Integer.MAX_VALUE))
                    {
                      return true;
                    }
                    inputCraftingTime *= 1000L;
                    long configCraftingTime = config.getLong("recipes." + args[3] + ".extra.crafting-time");
                    if (inputCraftingTime == 0 && configCraftingTime == 0)
                    {
                      MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r에 있는 레시피 rg255,204;" + args[3] + "&r의 제작에 필요한 시간이 없습니다");
                      return true;
                    }
                    else if (inputCraftingTime == configCraftingTime)
                    {
                      MessageUtil.sendError(
                              sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[3] +
                                      "&r의 제작에 필요한 시간이 rg255,204;" +
                                      Method.timeFormatMilli(configCraftingTime, false) +
                                      "&r입니다");
                      return true;
                    }
                    config.set("recipes." + args[3] + ".extra.crafting-time", inputCraftingTime == 0 ? null : inputCraftingTime);
                    Variable.customRecipes.put(args[2], config);
                    customRecipeListConfig.saveConfig();
                    MessageUtil.sendMessage(
                            sender, Prefix.INFO_CUSTOM_RECIPE,
                            "커스텀 레시피 목록 rg255,204;" +
                                    args[2] +
                                    "&r에 있는 레시피 rg255,204;" +
                                    args[3] +
                                    (inputCraftingTime == 0d ? ("&r의 제작에 필요한 시간을 제거했습니다") : ("&r의 제작에 필요한 시간을 rg255,204;" +
                                            Method.timeFormatMilli(inputCraftingTime, false) +
                                            "&r" +
                                            MessageUtil.getFinalConsonant(Method.timeFormatMilli(inputCraftingTime, false), ConsonantType.으로) +
                                            " 지정했습니다")));
                    break;
                  default:
                    MessageUtil.wrongArg(sender, 6, args);
                    MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " craftingtime <interval|skip|time> ...");
                    return true;
                }
                break;
              case "cost":
                if (!Cucumbery.using_Vault_Economy)
                {
                  MessageUtil.sendError(sender, "rg255,204;Vault &r플러그인을 사용하고 있지 않습니다");
                  return true;
                }
                if (args.length > 6)
                {
                  MessageUtil.longArg(sender, 6, args);
                  MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " cost <제작 비용>");
                  return true;
                }
                if (!MessageUtil.isDouble(sender, args[5], true))
                {
                  return true;
                }
                double inputCost = Double.parseDouble(args[5]);
                if (!MessageUtil.checkNumberSize(sender, inputCost, 0d, Double.MAX_VALUE))
                {
                  return true;
                }
                double configCost = config.getDouble("recipes." + args[3] + ".extra.cost");
                if (inputCost == 0d && configCost == 0d)
                {
                  MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r에 있는 레시피 rg255,204;" + args[3] + "&r의 제작 비용이 없습니다");
                  return true;
                }
                else if (inputCost == configCost)
                {
                  MessageUtil.sendError(sender,
                          "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r에 있는 레시피 rg255,204;" + args[3] + "&r의 제작 비용이 rg255,204;" + Constant.Sosu15.format(inputCost) + "원&r입니다");
                  return true;
                }
                config.set("recipes." + args[3] + ".extra.cost", inputCost == 0d ? null : inputCost);
                Variable.customRecipes.put(args[2], config);
                customRecipeListConfig.saveConfig();
                MessageUtil.sendMessage(
                        sender, Prefix.INFO_CUSTOM_RECIPE,
                        "커스텀 레시피 목록 rg255,204;" +
                                args[2] +
                                "&r에 있는 레시피 rg255,204;" +
                                args[3] +
                                (inputCost == 0d ? ("&r의 제작 비용을 제거했습니다") : ("&r의 제작 비용을 rg255,204;" + Constant.Sosu15.format(inputCost) + "원&r으로 지정했습니다")));
                break;
              case "hpcost":
                if (args.length > 6)
                {
                  MessageUtil.longArg(sender, 6, args);
                  MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " hpcost <제작에 필요한 HP>");
                  return true;
                }
                if (!MessageUtil.isDouble(sender, args[5], true))
                {
                  return true;
                }
                double inputHPCost = Double.parseDouble(args[5]);
                if (!MessageUtil.checkNumberSize(sender, inputHPCost, 0d, Double.MAX_VALUE))
                {
                  return true;
                }
                double configHPCost = config.getDouble("recipes." + args[3] + ".extra.hpcost");
                if (inputHPCost == 0d && configHPCost == 0d)
                {
                  MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r에 있는 레시피 rg255,204;" + args[3] + "&r의 제작 HP 비용이 없습니다");
                  return true;
                }
                else if (inputHPCost == configHPCost)
                {
                  MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" +
                          args[2] +
                          "&r에 있는 레시피 rg255,204;" +
                          args[3] +
                          "&r의 제작 HP 비용이 rg255,204;" +
                          Constant.Sosu15.format(inputHPCost) +
                          "&r입니다");
                  return true;
                }
                config.set("recipes." + args[3] + ".extra.hpcost", inputHPCost == 0d ? null : inputHPCost);
                Variable.customRecipes.put(args[2], config);
                customRecipeListConfig.saveConfig();
                MessageUtil.sendMessage(
                        sender, Prefix.INFO_CUSTOM_RECIPE,
                        "커스텀 레시피 목록 rg255,204;" +
                                args[2] +
                                "&r에 있는 레시피 rg255,204;" +
                                args[3] +
                                (inputHPCost == 0d ? ("&r의 제작 HP 비용을 제거했습니다") : ("&r의 제작 HP 비용을 rg255,204;" + Constant.Sosu15.format(inputHPCost) + "&r으로 지정했습니다")));
                break;
              case "saturationcost":
                if (args.length > 6)
                {
                  MessageUtil.longArg(sender, 6, args);
                  MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " saturationcost <제작에 필요한 포화도>");
                  return true;
                }
                if (!MessageUtil.isDouble(sender, args[5], true))
                {
                  return true;
                }
                double inputSaturationCost = Double.parseDouble(args[5]);
                if (!MessageUtil.checkNumberSize(sender, inputSaturationCost, 0d, 20d))
                {
                  return true;
                }
                double configSaturationCost = config.getDouble("recipes." + args[3] + ".extra.saturationcost");
                if (inputSaturationCost == 0d && configSaturationCost == 0d)
                {
                  MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r에 있는 레시피 rg255,204;" + args[3] + "&r의 제작 포화도 비용이 없습니다");
                  return true;
                }
                else if (inputSaturationCost == configSaturationCost)
                {
                  MessageUtil.sendError(
                          sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" +
                                  args[2] +
                                  "&r에 있는 레시피 rg255,204;" +
                                  args[3] +
                                  "&r의 제작 포화도 비용이 rg255,204;" +
                                  Constant.Sosu15.format(inputSaturationCost) +
                                  "&r입니다");
                  return true;
                }
                config.set("recipes." + args[3] + ".extra.saturationcost", inputSaturationCost == 0d ? null : inputSaturationCost);
                Variable.customRecipes.put(args[2], config);
                customRecipeListConfig.saveConfig();
                MessageUtil.sendMessage(
                        sender, Prefix.INFO_CUSTOM_RECIPE,
                        "커스텀 레시피 목록 rg255,204;" +
                                args[2] +
                                "&r에 있는 레시피 rg255,204;" +
                                args[3] +
                                (inputSaturationCost == 0d ? ("&r의 제작 포화도 비용을 제거했습니다") : ("&r의 제작 포화도 비용을 rg255,204;" + Constant.Sosu15.format(inputSaturationCost) + "&r으로 지정했습니다")));
                break;
              case "levelcost":
                if (args.length > 6)
                {
                  MessageUtil.longArg(sender, 6, args);
                  MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " levelcost <레벨 비용>");
                  return true;
                }
                if (!MessageUtil.isInteger(sender, args[5], true))
                {
                  return true;
                }
                int inputLevelCost = Integer.parseInt(args[5]);
                if (!MessageUtil.checkNumberSize(sender, inputLevelCost, 0, Integer.MAX_VALUE))
                {
                  return true;
                }
                int configLevelCost = config.getInt("recipes." + args[3] + ".extra.levelcost");
                if (inputLevelCost == 0 && configLevelCost == 0)
                {
                  MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r에 있는 레시피 rg255,204;" + args[3] + "&r의 레벨 비용이 없습니다");
                  return true;
                }
                else if (inputLevelCost == configLevelCost)
                {
                  MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" +
                          args[2] +
                          "&r에 있는 레시피 rg255,204;" +
                          args[3] +
                          "&r의 레벨 비용이 rg255,204;" +
                          Constant.Sosu15.format(inputLevelCost) +
                          "&r입니다");
                  return true;
                }
                config.set("recipes." + args[3] + ".extra.levelcost", inputLevelCost == 0 ? null : inputLevelCost);
                Variable.customRecipes.put(args[2], config);
                customRecipeListConfig.saveConfig();
                MessageUtil.sendMessage(
                        sender, Prefix.INFO_CUSTOM_RECIPE,
                        "커스텀 레시피 목록 rg255,204;" +
                                args[2] +
                                "&r에 있는 레시피 rg255,204;" +
                                args[3] +
                                (inputLevelCost == 0 ? ("&r의 레벨 비용을 제거했습니다") : ("&r의 레벨 비용을 rg255,204;" + Constant.Sosu15.format(inputLevelCost) + "&r으로 지정했습니다")));
                break;
              case "foodlevelcost":
                if (args.length > 6)
                {
                  MessageUtil.longArg(sender, 6, args);
                  MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " foodlevelcost <음식 포인트 비용>");
                  return true;
                }
                if (!MessageUtil.isInteger(sender, args[5], true))
                {
                  return true;
                }
                int inputFoodLevelCost = Integer.parseInt(args[5]);
                if (!MessageUtil.checkNumberSize(sender, inputFoodLevelCost, 0, 20))
                {
                  return true;
                }
                int configFoodCost = config.getInt("recipes." + args[3] + ".extra.foodlevelcost");
                if (inputFoodLevelCost == 0 && configFoodCost == 0)
                {
                  MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r에 있는 레시피 rg255,204;" + args[3] + "&r의 음식 포인트 비용이 없습니다");
                  return true;
                }
                else if (inputFoodLevelCost == configFoodCost)
                {
                  MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" +
                          args[2] +
                          "&r에 있는 레시피 rg255,204;" +
                          args[3] +
                          "&r의 옴식 포인트 비용이 rg255,204;" +
                          Constant.Sosu15.format(inputFoodLevelCost) +
                          "&r입니다");
                  return true;
                }
                config.set("recipes." + args[3] + ".extra.foodlevelcost", inputFoodLevelCost == 0 ? null : inputFoodLevelCost);
                Variable.customRecipes.put(args[2], config);
                customRecipeListConfig.saveConfig();
                MessageUtil.sendMessage(
                        sender, Prefix.INFO_CUSTOM_RECIPE,
                        "커스텀 레시피 목록 rg255,204;" +
                                args[2] +
                                "&r에 있는 레시피 rg255,204;" +
                                args[3] +
                                (inputFoodLevelCost == 0 ? ("&r의 레벨 비용을 제거했습니다") : ("&r의 레벨 비용을 rg255,204;" + Constant.Sosu15.format(inputFoodLevelCost) + "&r으로 지정했습니다")));
                break;
              case "wealth":
                if (!Cucumbery.using_Vault_Economy)
                {
                  MessageUtil.sendError(sender, "rg255,204;Vault &r플러그인을 사용하고 있지 않습니다");
                  return true;
                }
                if (args.length > 7)
                {
                  MessageUtil.longArg(sender, 7, args);
                  MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " wealth <최소 소지 금액> [최대 소지 금액]");
                  return true;
                }
                if (!MessageUtil.isDouble(sender, args[5], true))
                {
                  return true;
                }
                double inputWealthMin = Double.parseDouble(args[5]);
                if (inputWealthMin != -1d && !MessageUtil.checkNumberSize(sender, inputWealthMin, 0d, Double.MAX_VALUE))
                {
                  return true;
                }
                if (args.length == 7 && !MessageUtil.isDouble(sender, args[6], true))
                {
                  return true;
                }
                double inputWealthMax = args.length == 7 ? Double.parseDouble(args[6]) : -1d;
                if (inputWealthMax != -1d && !MessageUtil.checkNumberSize(sender, inputWealthMax, Math.max(0, inputWealthMin), Double.MAX_VALUE))
                {
                  return true;
                }
                double configWealthMin = config.getDouble("recipes." + args[3] + ".extra.wealth.min");
                double configWealthMax = config.getDouble("recipes." + args[3] + ".extra.wealth.max");
                boolean configWealthMinExists = config.contains("recipes." + args[3] + ".extra.wealth.min");
                boolean configWealthMaxExists = config.contains("recipes." + args[3] + ".extra.wealth.max");
                if (inputWealthMin == -1d && (!configWealthMinExists || configWealthMin == -1d) && inputWealthMax == -1d && (!configWealthMaxExists || configWealthMax == -1d))
                {
                  MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r에 있는 레시피 rg255,204;" + args[3] + "&r의 최대, 최소 소지 금액 조건이 없습니다");
                  return true;
                }
                if (configWealthMinExists && inputWealthMin == configWealthMin && configWealthMaxExists && inputWealthMax == configWealthMax)
                {
                  MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" +
                          args[2] +
                          "&r에 있는 레시피 rg255,204;" +
                          args[3] +
                          "&r의 최소 소지 금액 조건이 rg255,204;" +
                          Constant.Sosu15.format(inputWealthMin) +
                          "원&r, 최대 소지 금액 조건이 rg255,204;" +
                          Constant.Sosu15.format(inputWealthMax) +
                          "원&r입니다");
                  return true;
                }
                config.set("recipes." + args[3] + ".extra.wealth.min", inputWealthMin == -1d ? null : inputWealthMin);
                config.set("recipes." + args[3] + ".extra.wealth.max", inputWealthMax == -1d ? null : inputWealthMax);
                String sectionString = "recipes." + args[3] + ".extra.wealth";
                ConfigurationSection section = config.getConfigurationSection(sectionString);
                if (section != null && section.getKeys(false).size() == 0)
                {
                  config.set(sectionString, null);
                }
                Variable.customRecipes.put(args[2], config);
                customRecipeListConfig.saveConfig();
                if (inputWealthMin != inputWealthMax)
                {
                  MessageUtil.sendMessage(
                          sender, Prefix.INFO_CUSTOM_RECIPE,
                          "커스텀 레시피 목록 rg255,204;" +
                                  args[2] +
                                  "&r에 있는 레시피 rg255,204;" +
                                  args[3] +
                                  (inputWealthMin == -1d ? ("&r의 최소 소지 금액 조건을 제거") : ("&r의 최소 소지 금액 조건을 rg255,204;" + Constant.Sosu15.format(inputWealthMin) + "원&r으로 지정")) +
                                  "하였고, 최대 소지 금액 조건을 " +
                                  (inputWealthMax == -1d ? ("제거") : (Constant.THE_COLOR_HEX + Constant.Sosu15.format(inputWealthMax) + "원&r으로 지정")) +
                                  "했습니다");
                }
                else if (inputWealthMin == -1d)
                {
                  MessageUtil.sendMessage(sender, Prefix.INFO_CUSTOM_RECIPE, "커스텀 레시피 목록 rg255,204;" + args[2] + "&r에 있는 레시피 rg255,204;" + args[3] + "&r의 최대, 최소 소지 금액 조건을 제거했습니다");
                }
                else
                {
                  MessageUtil.sendMessage(
                          sender, Prefix.INFO_CUSTOM_RECIPE,
                          "커스텀 레시피 목록 rg255,204;" +
                                  args[2] +
                                  "&r에 있는 레시피 rg255,204;" +
                                  args[3] +
                                  "&r의 최대, 최소 소지 금액 조건을 rg255,204;" +
                                  Constant.Sosu15.format(inputWealthMin) +
                                  "원&r으로 지정했습니다");
                }
                break;
              case "level":
                if (args.length > 7)
                {
                  MessageUtil.longArg(sender, 7, args);
                  MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " level <최소 레벨> [최대 레벨]");
                  return true;
                }
                if (!MessageUtil.isInteger(sender, args[5], true))
                {
                  return true;
                }
                int inputLevelMin = Integer.parseInt(args[5]);
                if (inputLevelMin != -1 && !MessageUtil.checkNumberSize(sender, inputLevelMin, 0, Integer.MAX_VALUE))
                {
                  return true;
                }
                if (args.length == 7 && !MessageUtil.isInteger(sender, args[6], true))
                {
                  return true;
                }
                int inputLevelMax = args.length == 7 ? Integer.parseInt(args[6]) : -1;
                if (inputLevelMax != -1 && !MessageUtil.checkNumberSize(sender, inputLevelMax, Math.max(0, inputLevelMin), Integer.MAX_VALUE))
                {
                  return true;
                }
                int configLevelMin = config.getInt("recipes." + args[3] + ".extra.level.min");
                int configLevelMax = config.getInt("recipes." + args[3] + ".extra.level.max");
                boolean configLevelMinExists = config.contains("recipes." + args[3] + ".extra.level.min");
                boolean configLevelMaxExists = config.contains("recipes." + args[3] + ".extra.level.max");
                if (inputLevelMin == -1 && (!configLevelMinExists || configLevelMin == -1) && inputLevelMax == -1 && (!configLevelMaxExists || configLevelMax == -1))
                {
                  MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r에 있는 레시피 rg255,204;" + args[3] + "&r의 최대, 최소 레벨 조건이 없습니다");
                  return true;
                }
                if (configLevelMinExists && inputLevelMin == configLevelMin && configLevelMaxExists && inputLevelMax == configLevelMax)
                {
                  MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" +
                          args[2] +
                          "&r에 있는 레시피 rg255,204;" +
                          args[3] +
                          "&r의 최소 소지 레벨 조건이 rg255,204;" +
                          Constant.Sosu15.format(inputLevelMin) +
                          "&r, 최대 레벨 조건이 rg255,204;" +
                          Constant.Sosu15.format(inputLevelMax) +
                          "&r입니다");
                  return true;
                }
                config.set("recipes." + args[3] + ".extra.level.min", inputLevelMin == -1 ? null : inputLevelMin);
                config.set("recipes." + args[3] + ".extra.level.max", inputLevelMax == -1 ? null : inputLevelMax);

                sectionString = "recipes." + args[3] + ".extra.level";
                section = config.getConfigurationSection(sectionString);
                if (section != null && section.getKeys(false).size() == 0)
                {
                  config.set(sectionString, null);
                }
                Variable.customRecipes.put(args[2], config);
                customRecipeListConfig.saveConfig();
                if (inputLevelMin != inputLevelMax)
                {
                  MessageUtil.sendMessage(
                          sender, Prefix.INFO_CUSTOM_RECIPE,
                          "커스텀 레시피 목록 rg255,204;" +
                                  args[2] +
                                  "&r에 있는 레시피 rg255,204;" +
                                  args[3] +
                                  (inputLevelMin == -1 ? ("&r의 최소 레벨 조건을 제거") : ("&r의 최소 레벨 조건을 rg255,204;" + Constant.Sosu15.format(inputLevelMin) + "&r으로 지정")) +
                                  "하였고, 최대 레벨 조건을 " +
                                  (inputLevelMax == -1 ? ("제거") : (Constant.THE_COLOR_HEX + Constant.Sosu15.format(inputLevelMax) + "&r으로 지정")) +
                                  "했습니다");
                }
                else if (inputLevelMin == -1)
                {
                  MessageUtil.sendMessage(sender, Prefix.INFO_CUSTOM_RECIPE, "커스텀 레시피 목록 rg255,204;" + args[2] + "&r에 있는 레시피 rg255,204;" + args[3] + "&r의 최대, 최소 레벨 조건을 제거했습니다");
                }
                else
                {
                  MessageUtil.sendMessage(
                          sender, Prefix.INFO_CUSTOM_RECIPE,
                          "커스텀 레시피 목록 rg255,204;" +
                                  args[2] +
                                  "&r에 있는 레시피 rg255,204;" +
                                  args[3] +
                                  "&r의 최대, 최소 레벨 조건을 rg255,204;" +
                                  Constant.Sosu15.format(inputLevelMin) +
                                  "&r으로 지정했습니다");
                }
                break;
              case "display":
                if (args.length > 6)
                {
                  MessageUtil.longArg(sender, 6, args);
                  MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " display <레시피 표시 이름>");
                  return true;
                }
                String inputDisplay = args[5];
                String configDisplay = config.getString("recipes." + args[3] + ".extra.display");
                if (inputDisplay.equals("--remove") && (configDisplay == null || configDisplay.equals(args[3])))
                {
                  MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r에 있는 레시피 rg255,204;" + args[3] + "&r의 레시피 표시 이름이 없습니다");
                  return true;
                }
                else if (inputDisplay.equals(configDisplay))
                {
                  MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r에 있는 레시피 rg255,204;" + args[3] + "&r의 레시피 표시 이름이 rg255,204;" + inputDisplay + "&r입니다");
                  return true;
                }
                config.set("recipes." + args[3] + ".extra.display", inputDisplay.equals("--remove") ? args[3] : inputDisplay);
                Variable.customRecipes.put(args[2], config);
                customRecipeListConfig.saveConfig();
                MessageUtil.sendMessage(
                        sender, Prefix.INFO_CUSTOM_RECIPE,
                        "커스텀 레시피 목록 rg255,204;" +
                                args[2] +
                                "&r에 있는 레시피 rg255,204;" +
                                args[3] +
                                (inputDisplay.equals("--remove") ? ("&r의 레시피 표시 이름을 제거했습니다") : ("&r의 레시피 표시 이름을 rg255,204;" +
                                        inputDisplay +
                                        "&r" +
                                        MessageUtil.getFinalConsonant(inputDisplay, ConsonantType.으로) +
                                        " 지정했습니다")));
                break;
              case "biome":
                if (args.length > 6)
                {
                  MessageUtil.longArg(sender, 6, args);
                  MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " biome <생물 군계>");
                  return true;
                }
                inputDisplay = args[5];
                Biome biome;
                try
                {
                  biome = Biome.valueOf(inputDisplay.toUpperCase());
                }
                catch (Exception e)
                {
                  if (inputDisplay.equals("--remove"))
                  {
                    biome = null;
                  }
                  else
                  {
                    MessageUtil.wrongArg(sender, 5, args);
                    return true;
                  }
                }
                String biomeName = biome.toString();
                configDisplay = config.getString("recipes." + args[3] + ".extra.biome");
                if (inputDisplay.equals("--remove") && configDisplay == null)
                {
                  MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r에 있는 레시피 rg255,204;" + args[3] + "&r의 생물 군계 조건이 없습니다");
                  return true;
                }
                else if (inputDisplay.equals(configDisplay))
                {
                  MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r에 있는 레시피 rg255,204;" + args[3] + "&r의 생물 군계 조건이 rg255,204;" + biomeName + "&r입니다");
                  return true;
                }
                config.set("recipes." + args[3] + ".extra.biome", inputDisplay.equals("--remove") ? null : inputDisplay.toUpperCase());
                Variable.customRecipes.put(args[2], config);
                customRecipeListConfig.saveConfig();
                MessageUtil.sendMessage(
                        sender, Prefix.INFO_CUSTOM_RECIPE,
                        "커스텀 레시피 목록 rg255,204;" +
                                args[2] +
                                "&r에 있는 레시피 rg255,204;" +
                                args[3] +
                                (inputDisplay.equals("--remove") ? ("&r의 생물 군계 조건을 제거했습니다") : ("&r의 생물 군계 조건을 rg255,204;" +
                                        biomeName +
                                        "&r" +
                                        MessageUtil.getFinalConsonant(biomeName, ConsonantType.으로) +
                                        " 지정했습니다")));
                break;
              case "belowblock":
                if (args.length > 6)
                {
                  MessageUtil.longArg(sender, 6, args);
                  MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " belowblock <블록>");
                  return true;
                }
                inputDisplay = args[5];
                Material belowBlockType;
                try
                {
                  belowBlockType = Material.valueOf(inputDisplay.toUpperCase());
                }
                catch (Exception e)
                {
                  if (inputDisplay.equals("--remove"))
                  {
                    belowBlockType = null;
                  }
                  else
                  {
                    MessageUtil.wrongArg(sender, 5, args);
                    return true;
                  }
                }
                if (belowBlockType != null && !belowBlockType.isBlock())
                {
                  MessageUtil.sendError(sender, "블록만 입력할 수 있습니다 (rg255,204;" + inputDisplay + "&r)");
                  return true;
                }
                String belowBlockTypeName = null;
                if (belowBlockType != null)
                {
                  belowBlockTypeName = ItemNameUtil.itemName(belowBlockType).toString();
                }
                configDisplay = config.getString("recipes." + args[3] + ".extra.belowblock");
                if (inputDisplay.equals("--remove") && configDisplay == null)
                {
                  MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r에 있는 레시피 rg255,204;" + args[3] + "&r의 밟고 있는 블록 조건이 없습니다");
                  return true;
                }
                else if (inputDisplay.equals(configDisplay))
                {
                  MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r에 있는 레시피 rg255,204;" + args[3] + "&r의 밟고 있는 블록 조건이 rg255,204;" + belowBlockTypeName + "&r입니다");
                  return true;
                }
                config.set("recipes." + args[3] + ".extra.belowblock", inputDisplay.equals("--remove") ? null : inputDisplay.toUpperCase());
                Variable.customRecipes.put(args[2], config);
                customRecipeListConfig.saveConfig();
                MessageUtil.sendMessage(
                        sender, Prefix.INFO_CUSTOM_RECIPE,
                        "커스텀 레시피 목록 rg255,204;" +
                                args[2] +
                                "&r에 있는 레시피 rg255,204;" +
                                args[3] +
                                (inputDisplay.equals("--remove") ? ("&r의 밟고 있는 블록 조건을 제거했습니다") : ("&r의 밟고 있는 블록 조건을 rg255,204;" + belowBlockTypeName + "&r" + MessageUtil.getFinalConsonant(
                                        belowBlockTypeName, ConsonantType.으로) + " 지정했습니다")));
                break;
              case "reusable":
                if (args.length < 7)
                {
                  MessageUtil.shortArg(sender, 7, args);
                  MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " reusable <재료 번호> <재사용 가능 여부>");
                  return true;
                }
                if (args.length > 7)
                {
                  MessageUtil.longArg(sender, 7, args);
                  MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " reusable <재료 번호> <재사용 가능 여부>");
                  return true;
                }
                ConfigurationSection ingredients = config.getConfigurationSection("recipes." + args[3] + ".ingredients");
                if (ingredients == null || ingredients.getKeys(false).size() == 0)
                {
                  MessageUtil.sendError(sender, "잘못된 아이템 레시피입니다 (재료 손상)");
                  return true;
                }
                if (!MessageUtil.isInteger(sender, args[5], true))
                {
                  return true;
                }
                int inputIngredientNumber = Integer.parseInt(args[5]);
                if (!MessageUtil.checkNumberSize(sender, inputIngredientNumber, 1, ingredients.getKeys(false).size()))
                {
                  return true;
                }
                if (!args[6].equals("true") && !args[6].equals("false"))
                {
                  MessageUtil.wrongBool(sender, 7, args);
                  return true;
                }
                boolean inputResuable = args[6].equals("true");
                if (config.getBoolean("recipes." + args[3] + ".ingredients." + inputIngredientNumber + ".reusable") == inputResuable)
                {
                  MessageUtil.sendError(
                          sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" +
                                  args[2] +
                                  "&r의 커스텀 레시피 rg255,204;" +
                                  args[3] +
                                  "&r의 rg255,204;" +
                                  inputIngredientNumber +
                                  "&r번째 재료 아이템(rg255,204;" +
                                  ItemNameUtil.itemName(ItemSerializer.deserialize(config.getString("recipes." + args[3] + ".ingredients." + inputIngredientNumber + ".item"))) +
                                  "&r)의 제작 시 사라지지 않는 아이템 태그 값이 rg255,204;" +
                                  inputResuable +
                                  "&r입니다");
                  return true;
                }
                config.set("recipes." + args[3] + ".ingredients." + inputIngredientNumber + ".reusable", (inputResuable ? true : null));
                Variable.customRecipes.put(args[2], config);
                customRecipeListConfig.saveConfig();
                MessageUtil.sendMessage(sender, Prefix.INFO_CUSTOM_RECIPE,
                        "커스텀 레시피 목록 §e" +
                                args[2] +
                                "&r의 커스텀 레시피 rg255,204;" +
                                args[3] +
                                "&r의 rg255,204;" +
                                inputIngredientNumber +
                                "&r번째 재료 아이템(rg255,204;" +
                                ItemNameUtil.itemName(ItemSerializer.deserialize(config.getString("recipes." + args[3] + ".ingredients." + inputIngredientNumber + ".item"))) +
                                "&r)의 제작 시 사라지지 않는 아이템 태그 값을 rg255,204;" +
                                inputResuable +
                                "&r로 지정했습니다");
                break;
              case "require":
                if (args.length < 8)
                {
                  MessageUtil.shortArg(sender, 9, args);
                  MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " require <category|recipe> ...");
                  return true;
                }
                folder = new File(Cucumbery.getPlugin().getDataFolder() + "/data/CustomRecipe");
                if (!folder.exists())
                {
                  boolean success = folder.mkdirs();
                  if (!success)
                  {
                    System.err.println("[Cucumbery] could not create customrecipe folder!");
                  }
                }
                if (!new File(Cucumbery.getPlugin().getDataFolder() + "/data/CustomRecipe/" + args[6] + ".yml").exists())
                {
                  MessageUtil.sendError(sender, "커스텀 레시피 목록 rg255,204;" + args[6] + "&r" + MessageUtil.getFinalConsonant(args[6], ConsonantType.을를) + " 찾을 수 없습니다");
                  return true;
                }
                switch (args[5])
                {
                  case "recipe":
                    if (args.length < 9)
                    {
                      MessageUtil.shortArg(sender, 9, args);
                      MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " require recipe <레시피 목록 이름> <레시피 이름> <최소 제작 횟수> [최대 제작 횟수]");
                      return true;
                    }
                    customRecipeListConfig = CustomConfig.getCustomConfig("data/CustomRecipe/" + args[6] + ".yml");
                    config = customRecipeListConfig.getConfig();
                    recipe = config.getConfigurationSection("recipes." + args[7]);
                    if (recipe == null || recipe.getKeys(false).size() == 0)
                    {
                      MessageUtil.sendError(sender, "커스텀 레시피 목록 rg255,204;" + args[6] + "&r에서 해당 레시피(rg255,204;" + args[7] + "&r)를 찾을 수 없습니다");
                      return true;
                    }
                    customRecipeListConfig = CustomConfig.getCustomConfig("data/CustomRecipe/" + args[2] + ".yml");
                    config = customRecipeListConfig.getConfig();
                    if (!MessageUtil.isInteger(sender, args[8], true))
                    {
                      return true;
                    }
                    int inputRecipeMin = Integer.parseInt(args[8]);
                    if (inputRecipeMin != -1 && !MessageUtil.checkNumberSize(sender, inputRecipeMin, 0, Integer.MAX_VALUE))
                    {
                      return true;
                    }
                    if (args.length == 9 && !MessageUtil.isInteger(sender, args[8], true))
                    {
                      return true;
                    }
                    int inputRecipeMax = args.length == 10 ? Integer.parseInt(args[9]) : -1;
                    if (inputRecipeMax != -1 && !MessageUtil.checkNumberSize(sender, inputRecipeMax, Math.max(0, inputRecipeMin), Integer.MAX_VALUE))
                    {
                      return true;
                    }
                    int configRecipeMin = config.getInt("recipes." + args[3] + ".extra.recipes." + args[6] + "." + args[7] + ".min");
                    int configRecipeMax = config.getInt("recipes." + args[3] + ".extra.recipes." + args[6] + "." + args[7] + ".max");
                    boolean configRecipeMinExists = config.contains("recipes." + args[3] + ".extra.recipes." + args[6] + "." + args[7] + ".min");
                    boolean configRecipeMaxExists = config.contains("recipes." + args[3] + ".extra.recipes." + args[6] + "." + args[7] + ".max");
                    if (inputRecipeMin == -1 && (!configRecipeMinExists || configRecipeMin == -1) && inputRecipeMax == -1 && (!configRecipeMaxExists || configRecipeMax == -1))
                    {
                      MessageUtil.sendError(
                              sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[3] +
                                      "&r에 대한 커스텀 레시피 목록 rg255,204;" +
                                      args[6] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[7] +
                                      "&r의 최대, 최소 제작 횟수 조건이 없습니다");
                      return true;
                    }
                    if (configRecipeMinExists && inputRecipeMin == configRecipeMin && configRecipeMaxExists && inputRecipeMax == configRecipeMax)
                    {
                      MessageUtil.sendError(
                              sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[3] +
                                      "&r에 대한 커스텀 레시피 목록 rg255,204;" +
                                      args[6] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[7] +
                                      "&r의 최소 제작 횟수 조건이 rg255,204;" +
                                      Constant.Sosu15.format(inputRecipeMin) +
                                      "&r, 최대 제작 횟수 조건이 rg255,204;" +
                                      Constant.Sosu15.format(inputRecipeMax) +
                                      "&r입니다");
                      return true;
                    }
                    config.set("recipes." + args[3] + ".extra.recipes." + args[6] + "." + args[7] + ".min", inputRecipeMin == -1 ? null : inputRecipeMin);
                    config.set("recipes." + args[3] + ".extra.recipes." + args[6] + "." + args[7] + ".max", inputRecipeMax == -1 ? null : inputRecipeMax);

                    sectionString = "recipes." + args[3] + ".extra.recipes." + args[6] + "." + args[7];
                    section = config.getConfigurationSection(sectionString);
                    if (section != null && section.getKeys(false).size() == 0)
                    {
                      config.set(sectionString, null);
                    }

                    sectionString = "recipes." + args[3] + ".extra.recipes." + args[6];
                    section = config.getConfigurationSection(sectionString);
                    if (section != null && section.getKeys(false).size() == 0)
                    {
                      config.set(sectionString, null);
                    }

                    sectionString = "recipes." + args[3] + ".extra.recipes";
                    section = config.getConfigurationSection(sectionString);
                    if (section != null && section.getKeys(false).size() == 0)
                    {
                      config.set(sectionString, null);
                    }

                    Variable.customRecipes.put(args[2], config);
                    customRecipeListConfig.saveConfig();
                    if (inputRecipeMin != inputRecipeMax)
                    {
                      MessageUtil.sendMessage(
                              sender, Prefix.INFO_CUSTOM_RECIPE,
                              "커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[3] +
                                      "&r에 대한 커스텀 레시피 목록 rg255,204;" +
                                      args[6] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[7] +
                                      (inputRecipeMin == -1 ? ("&r의 최소 제작 횟수 조건을 제거") : ("&r의 최소 제작 횟수 조건을 rg255,204;" + Constant.Sosu15.format(inputRecipeMin) + "&r으로 지정")) +
                                      "하였고, 최대 제작 횟수 조건을 " +
                                      (inputRecipeMax == -1 ? ("제거") : (Constant.THE_COLOR_HEX + Constant.Sosu15.format(inputRecipeMax) + "&r으로 지정")) +
                                      "했습니다");
                    }
                    else if (inputRecipeMin == -1)
                    {
                      MessageUtil.sendMessage(
                              sender, Prefix.INFO_CUSTOM_RECIPE,
                              "커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[3] +
                                      "&r에 대한 커스텀 레시피 목록 rg255,204;" +
                                      args[6] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[7] +
                                      "&r의 최대, 최소 rg255,204;" +
                                      args[7] +
                                      "&r 레시피 제작 조건을 제거했습니다");
                    }
                    else
                    {
                      MessageUtil.sendMessage(
                              sender, Prefix.INFO_CUSTOM_RECIPE,
                              "커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[3] +
                                      "&r에 대한 커스텀 레시피 목록 rg255,204;" +
                                      args[6] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[7] +
                                      "&r의 최대, 최소 레시피 제작 조건을 rg255,204;" +
                                      Constant.Sosu15.format(inputRecipeMin) +
                                      "&r으로 지정했습니다");
                    }
                    break;
                  case "category":
                    if (args.length > 9)
                    {
                      MessageUtil.shortArg(sender, 9, args);
                      MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " require category <레시피 목록 이름> <최소 제작 횟수> [최대 제작 횟수]");
                      return true;
                    }
                    if (!MessageUtil.isInteger(sender, args[7], true))
                    {
                      return true;
                    }
                    int inputCategoryMin = Integer.parseInt(args[7]);
                    if (inputCategoryMin != -1 && !MessageUtil.checkNumberSize(sender, inputCategoryMin, 0, Integer.MAX_VALUE))
                    {
                      return true;
                    }
                    if (args.length == 8 && !MessageUtil.isInteger(sender, args[7], true))
                    {
                      return true;
                    }
                    int inputCategoryMax = args.length == 9 ? Integer.parseInt(args[8]) : -1;
                    if (inputCategoryMax != -1 && !MessageUtil.checkNumberSize(sender, inputCategoryMax, Math.max(0, inputCategoryMin), Integer.MAX_VALUE))
                    {
                      return true;
                    }
                    int configCategoryMin = config.getInt("recipes." + args[3] + ".extra.categories." + args[6] + ".min");
                    int configCategoryMax = config.getInt("recipes." + args[3] + ".extra.categories." + args[6] + ".max");
                    boolean configCategoryMinExists = config.contains("recipes." + args[3] + ".extra.categories." + args[6] + ".min");
                    boolean configCategoryMaxExists = config.contains("recipes." + args[3] + ".extra.categories." + args[6] + ".max");
                    if (inputCategoryMin == -1 && (!configCategoryMinExists || configCategoryMin == -1) && inputCategoryMax == -1 && (!configCategoryMaxExists || configCategoryMax == -1))
                    {
                      MessageUtil.sendError(
                              sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[3] +
                                      "&r에 대한 커스텀 레시피 목록 rg255,204;" +
                                      args[6] +
                                      "&r에 포함되어 있는 레시피의 최대, 최소 제작 횟수 조건이 없습니다");
                      return true;
                    }
                    if (configCategoryMinExists && inputCategoryMin == configCategoryMin && configCategoryMaxExists && inputCategoryMax == configCategoryMax)
                    {
                      MessageUtil.sendError(
                              sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[3] +
                                      "&r에 대한 커스텀 레시피 목록 rg255,204;" +
                                      args[6] +
                                      "&r에 포함되어 있는 레시피의 최소 제작 횟수 조건이 rg255,204;" +
                                      Constant.Sosu15.format(inputCategoryMin) +
                                      "&r, 최대 제작 횟수 조건이 rg255,204;" +
                                      Constant.Sosu15.format(inputCategoryMax) +
                                      "&r입니다");
                      return true;
                    }
                    config.set("recipes." + args[3] + ".extra.categories." + args[6] + ".min", inputCategoryMin == -1 ? null : inputCategoryMin);
                    config.set("recipes." + args[3] + ".extra.categories." + args[6] + ".max", inputCategoryMax == -1 ? null : inputCategoryMax);

                    sectionString = "recipes." + args[3] + ".extra.categories";
                    section = config.getConfigurationSection(sectionString);
                    if (section != null && section.getKeys(false).size() == 0)
                    {
                      config.set(sectionString, null);
                    }
                    Variable.customRecipes.put(args[2], config);
                    customRecipeListConfig.saveConfig();
                    if (inputCategoryMin != inputCategoryMax)
                    {
                      MessageUtil.sendMessage(
                              sender, Prefix.INFO_CUSTOM_RECIPE,
                              "커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[3] +
                                      "&r에 대한 커스텀 레시피 목록 rg255,204;" +
                                      args[6] +
                                      "&r에 포함되어 있는 레시피" +
                                      (inputCategoryMin == -1 ? ("의 최소 제작 횟수 조건을 제거") : ("의 최소 제작 횟수 조건을 rg255,204;" + Constant.Sosu15.format(inputCategoryMin) + "&r으로 지정")) +
                                      "하였고, 최대 제작 횟수 조건을 " +
                                      (inputCategoryMax == -1 ? ("제거") : (Constant.THE_COLOR_HEX + Constant.Sosu15.format(inputCategoryMax) + "&r으로 지정")) +
                                      "했습니다");
                    }
                    else if (inputCategoryMin == -1)
                    {
                      MessageUtil.sendMessage(
                              sender, Prefix.INFO_CUSTOM_RECIPE,
                              "커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[3] +
                                      "&r에 대한 커스텀 레시피 목록 rg255,204;" +
                                      args[6] +
                                      "&r에 포함되어 있는 레시피의 최대, 최소 제작 조건을 제거했습니다");
                    }
                    else
                    {
                      MessageUtil.sendMessage(
                              sender, Prefix.INFO_CUSTOM_RECIPE,
                              "커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[3] +
                                      "&r에 대한 커스텀 레시피 목록 rg255,204;" +
                                      args[6] +
                                      "&r에 포함되어 있는 레시피의 최대, 최소 제작 조건을 rg255,204;" +
                                      Constant.Sosu15.format(inputCategoryMin) +
                                      "&r으로 지정했습니다");
                    }
                    break;
                  default:
                    MessageUtil.wrongArg(sender, 6, args);
                    MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " require <category|recipe> ...");
                    return true;
                }
                break;
              case "statistic":
                if (args.length < 8)
                {
                  MessageUtil.shortArg(sender, 8, args);
                  MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " statistic <entity|general|material> ...");
                  return true;
                }
                switch (args[5])
                {
                  case "general":
                    if (args.length > 9)
                    {
                      MessageUtil.longArg(sender, 9, args);
                      MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " statistic general <통계 이름> <min> [max]");
                      return true;
                    }
                    Statistic statistic;
                    try
                    {
                      statistic = Statistic.valueOf(args[6].toUpperCase());
                    }
                    catch (Exception e)
                    {
                      MessageUtil.wrongArg(sender, 7, args);
                      return true;
                    }
                    switch (statistic)
                    {
                      case DROP:
                      case CRAFT_ITEM:
                      case PICKUP:
                      case USE_ITEM:
                      case BREAK_ITEM:
                      case MINE_BLOCK:
                        MessageUtil.sendError(sender, "잘못된 통계입니다. 해당 통계는 material 종류의 통계에서만 사용할 수 있습니다");
                        return true;
                      case KILL_ENTITY:
                      case ENTITY_KILLED_BY:
                        MessageUtil.sendError(sender, "잘못된 통계입니다. 해당 통계는 entity 종류의 통계에서만 사용할 수 있습니다");
                        return true;
                      default:
                        break;
                    }
                    if (!MessageUtil.isInteger(sender, args[7], true))
                    {
                      return true;
                    }
                    int inputGeneralMin = Integer.parseInt(args[7]);
                    if (inputGeneralMin != -1 && !MessageUtil.checkNumberSize(sender, inputGeneralMin, 0, Integer.MAX_VALUE))
                    {
                      return true;
                    }
                    if (args.length == 8 && !MessageUtil.isInteger(sender, args[7], true))
                    {
                      return true;
                    }
                    int inputGeneralMax = args.length == 9 ? Integer.parseInt(args[8]) : -1;
                    if (inputGeneralMax != -1 && !MessageUtil.checkNumberSize(sender, inputGeneralMax, Math.max(0, inputGeneralMin), Integer.MAX_VALUE))
                    {
                      return true;
                    }
                    String statisticName = statistic.toString(), statisticString = statistic.toString();
                    int configGeneralMin = config.getInt("recipes." + args[3] + ".extra.statistics.general." + statisticString + ".min");
                    int configGeneralMax = config.getInt("recipes." + args[3] + ".extra.statistics.general." + statisticString + ".max");
                    boolean configGeneralMinExists = config.contains("recipes." + args[3] + ".extra.statistics.general." + statisticString + ".min");
                    boolean configGeneralMaxExists = config.contains("recipes." + args[3] + ".extra.statistics.general." + statisticString + ".max");
                    if (inputGeneralMin == -1 && (!configGeneralMinExists || configGeneralMin == -1) && inputGeneralMax == -1 && (!configGeneralMaxExists || configGeneralMax == -1))
                    {
                      MessageUtil.sendError(sender,
                              "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" + args[2] + "&r에 있는 레시피 rg255,204;" + args[3] + "&r의 통계값 rg255,204;" + statisticName + "&r의 최대, 최소값 조건이 없습니다");
                      return true;
                    }
                    if (configGeneralMinExists && inputGeneralMin == configGeneralMin && configGeneralMaxExists && inputGeneralMax == configGeneralMax)
                    {
                      MessageUtil.sendError(
                              sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[3] +
                                      "&r의 통계값 rg255,204;" +
                                      statisticName +
                                      "&r최소값 조건이 rg255,204;" +
                                      Constant.Sosu15.format(inputGeneralMin) +
                                      "&r, 최대값 조건이 rg255,204;" +
                                      Constant.Sosu15.format(inputGeneralMax) +
                                      "&r입니다");
                      return true;
                    }
                    config.set("recipes." + args[3] + ".extra.statistics.general." + statisticString + ".min", inputGeneralMin == -1 ? null : inputGeneralMin);
                    config.set("recipes." + args[3] + ".extra.statistics.general." + statisticString + ".max", inputGeneralMax == -1 ? null : inputGeneralMax);

                    sectionString = "recipes." + args[3] + ".extra.statistics.general." + statisticString;
                    section = config.getConfigurationSection(sectionString);
                    if (section != null && section.getKeys(false).isEmpty())
                    {
                      config.set(sectionString, null);
                    }

                    sectionString = "recipes." + args[3] + ".extra.statistics.general";
                    section = config.getConfigurationSection(sectionString);
                    if (section != null && section.getKeys(false).isEmpty())
                    {
                      config.set(sectionString, null);
                    }

                    sectionString = "recipes." + args[3] + ".extra.statistics";
                    section = config.getConfigurationSection(sectionString);
                    if (section != null && section.getKeys(false).isEmpty())
                    {
                      config.set(sectionString, null);
                    }

                    Variable.customRecipes.put(args[2], config);
                    customRecipeListConfig.saveConfig();
                    if (inputGeneralMin != inputGeneralMax)
                    {
                      MessageUtil.sendMessage(
                              sender, Prefix.INFO_CUSTOM_RECIPE,
                              "커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[3] +
                                      "&r의 통계값 rg255,204;" +
                                      statisticName +
                                      "&r" +
                                      (inputGeneralMin == -1 ? ("의 최소값 조건을 제거") : ("의 최소값 조건을 rg255,204;" + Constant.Sosu15.format(inputGeneralMin) + "&r으로 지정")) +
                                      "하였고, 최대값 조건을 " +
                                      (inputGeneralMax == -1 ? ("제거") : (Constant.THE_COLOR_HEX + Constant.Sosu15.format(inputGeneralMax) + "&r으로 지정")) +
                                      "했습니다");
                    }
                    else if (inputGeneralMin == -1)
                    {
                      MessageUtil.sendMessage(
                              sender, Prefix.INFO_CUSTOM_RECIPE,
                              "커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[3] +
                                      "&r의 통계값 rg255,204;" + statistic +
                                      "&r의 최대, 최소값 조건을 제거했습니다");
                    }
                    else
                    {
                      MessageUtil.sendMessage(
                              sender, Prefix.INFO_CUSTOM_RECIPE,
                              "커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[3] +
                                      "&r의 통계값 rg255,204;" +
                                      statisticName +
                                      "&r의 최대, 최소값 조건을 rg255,204;" +
                                      Constant.Sosu15.format(inputGeneralMin) +
                                      "&r으로 지정했습니다");
                    }
                    break;
                  case "entity":
                    if (args.length < 9)
                    {
                      MessageUtil.shortArg(sender, 9, args);
                      MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " statistic entity <통계 이름> <개체 종류> <min> [max]");
                      return true;
                    }
                    try
                    {
                      statistic = Statistic.valueOf(args[6].toUpperCase());
                    }
                    catch (Exception e)
                    {
                      MessageUtil.wrongArg(sender, 7, args);
                      return true;
                    }
                    switch (statistic)
                    {
                      case DROP:
                      case CRAFT_ITEM:
                      case PICKUP:
                      case USE_ITEM:
                      case BREAK_ITEM:
                      case MINE_BLOCK:
                        MessageUtil.sendError(sender, "잘못된 통계입니다. 해당 통계는 material 종류의 통계에서만 사용할 수 있습니다");
                        return true;
                      case KILL_ENTITY:
                      case ENTITY_KILLED_BY:
                        break;
                      default:
                        MessageUtil.sendError(sender, "잘못된 통계입니다. 해당 통계는 general 종류의 통계에서만 사용할 수 있습니다");
                        return true;
                    }
                    EntityType entityType;
                    try
                    {
                      entityType = EntityType.valueOf(args[7].toUpperCase());
                    }
                    catch (Exception e)
                    {
                      MessageUtil.wrongArg(sender, 8, args);
                      return true;
                    }
                    if (!MessageUtil.isInteger(sender, args[8], true))
                    {
                      return true;
                    }
                    int inputEntityMin = Integer.parseInt(args[8]);
                    if (inputEntityMin != -1 && !MessageUtil.checkNumberSize(sender, inputEntityMin, 0, Integer.MAX_VALUE))
                    {
                      return true;
                    }
                    if (args.length == 9 && !MessageUtil.isInteger(sender, args[8], true))
                    {
                      return true;
                    }
                    int inputEntityMax = args.length == 10 ? Integer.parseInt(args[9]) : -1;
                    if (inputEntityMax != -1 && !MessageUtil.checkNumberSize(sender, inputEntityMax, Math.max(0, inputEntityMin), Integer.MAX_VALUE))
                    {
                      return true;
                    }
                    statisticName = statistic.toString();
                    statisticString = statistic.toString();
                    String entityTypeString = entityType.toString();
                    int configEntityMin = config.getInt("recipes." + args[3] + ".extra.statistics.entity." + statisticString + "." + entityTypeString + ".min");
                    int configEntityMax = config.getInt("recipes." + args[3] + ".extra.statistics.entity." + statisticString + "." + entityTypeString + ".max");
                    boolean configEntityMinExists = config.contains("recipes." + args[3] + ".extra.statistics.entity." + statisticString + "." + entityTypeString + ".min");
                    boolean configEntityMaxExists = config.contains("recipes." + args[3] + ".extra.statistics.entity." + statisticString + "." + entityTypeString + ".max");
                    if (inputEntityMin == -1 && (!configEntityMinExists || configEntityMin == -1) && inputEntityMax == -1 && (!configEntityMaxExists || configEntityMax == -1))
                    {
                      MessageUtil.sendError(
                              sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[3] +
                                      "&r의 통계값 rg255,204;" +
                                      statisticName +
                                      "&r의 최대, 최소값 조건이 없습니다");
                      return true;
                    }
                    if (configEntityMinExists && inputEntityMin == configEntityMin && configEntityMaxExists && inputEntityMax == configEntityMax)
                    {
                      MessageUtil.sendError(
                              sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[3] +
                                      "&r의 통계값 rg255,204;" +
                                      statisticName +
                                      "&r최소값 조건이 rg255,204;" +
                                      Constant.Sosu15.format(inputEntityMin) +
                                      "&r, 최대값 조건이 rg255,204;" +
                                      Constant.Sosu15.format(inputEntityMax) +
                                      "&r입니다");
                      return true;
                    }
                    config.set("recipes." + args[3] + ".extra.statistics.entity." + statisticString + "." + entityTypeString + ".min", inputEntityMin == -1 ? null : inputEntityMin);
                    config.set("recipes." + args[3] + ".extra.statistics.entity." + statisticString + "." + entityTypeString + ".max", inputEntityMax == -1 ? null : inputEntityMax);

                    sectionString = "recipes." + args[3] + ".extra.statistics.entity." + statisticString + "." + entityTypeString;
                    section = config.getConfigurationSection(sectionString);
                    if (section != null && section.getKeys(false).size() == 0)
                    {
                      config.set(sectionString, null);
                    }

                    sectionString = "recipes." + args[3] + ".extra.statistics.entity." + statisticString;
                    section = config.getConfigurationSection(sectionString);
                    if (section != null && section.getKeys(false).size() == 0)
                    {
                      config.set(sectionString, null);
                    }

                    sectionString = "recipes." + args[3] + ".extra.statistics.entity";
                    section = config.getConfigurationSection(sectionString);
                    if (section != null && section.getKeys(false).size() == 0)
                    {
                      config.set(sectionString, null);
                    }

                    sectionString = "recipes." + args[3] + ".extra.statistics";
                    section = config.getConfigurationSection(sectionString);
                    if (section != null && section.getKeys(false).size() == 0)
                    {
                      config.set(sectionString, null);
                    }

                    Variable.customRecipes.put(args[2], config);
                    customRecipeListConfig.saveConfig();
                    if (inputEntityMin != inputEntityMax)
                    {
                      MessageUtil.sendMessage(
                              sender, Prefix.INFO_CUSTOM_RECIPE,
                              "커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[3] +
                                      "&r의 통계값 rg255,204;" +
                                      statisticName +
                                      "&r" +
                                      (inputEntityMin == -1 ? ("의 최소값 조건을 제거") : ("의 최소값 조건을 rg255,204;" + Constant.Sosu15.format(inputEntityMin) + "&r으로 지정")) +
                                      "하였고, 최대값 조건을 " +
                                      (inputEntityMax == -1 ? ("제거") : (Constant.THE_COLOR_HEX + Constant.Sosu15.format(inputEntityMax) + "&r으로 지정")) +
                                      "했습니다");
                    }
                    else if (inputEntityMin == -1)
                    {
                      MessageUtil.sendMessage(
                              sender, Prefix.INFO_CUSTOM_RECIPE,
                              "커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[3] +
                                      "&r의 통계값 rg255,204;" +
                                      statisticName +
                                      "&r의 최대, 최소값 조건을 제거했습니다");
                    }
                    else
                    {
                      MessageUtil.sendMessage(sender, Prefix.INFO_CUSTOM_RECIPE,
                              "커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[3] +
                                      "&r의 통계값 rg255,204;" +
                                      "&r의 통계값 rg255,204;" +
                                      statisticName +
                                      "&r의 최대, 최소값 조건을 rg255,204;" +
                                      Constant.Sosu15.format(inputEntityMin) +
                                      "&r으로 지정했습니다");
                    }
                    break;
                  case "material":
                    if (args.length < 9)
                    {
                      MessageUtil.shortArg(sender, 9, args);
                      MessageUtil.commandInfo(sender, label, "edit category " + args[2] + " " + args[3] + " statistic material <통계 이름> <아이템 종류> <min> [max]");
                      return true;
                    }
                    try
                    {
                      statistic = Statistic.valueOf(args[6].toUpperCase());
                    }
                    catch (Exception e)
                    {
                      MessageUtil.wrongArg(sender, 7, args);
                      return true;
                    }
                    switch (statistic)
                    {
                      case DROP:
                      case CRAFT_ITEM:
                      case PICKUP:
                      case USE_ITEM:
                      case BREAK_ITEM:
                      case MINE_BLOCK:
                        break;
                      case KILL_ENTITY:
                      case ENTITY_KILLED_BY:
                        MessageUtil.sendError(sender, "잘못된 통계입니다. 해당 통계는 entity 종류의 통계에서만 사용할 수 있습니다");
                        return true;
                      default:
                        MessageUtil.sendError(sender, "잘못된 통계입니다. 해당 통계는 general 종류의 통계에서만 사용할 수 있습니다");
                        return true;
                    }
                    Material material;
                    try
                    {
                      material = Material.valueOf(args[7].toUpperCase());
                    }
                    catch (Exception e)
                    {
                      MessageUtil.wrongArg(sender, 8, args);
                      return true;
                    }
                    if (!MessageUtil.isInteger(sender, args[8], true))
                    {
                      return true;
                    }
                    int inputMaterialMin = Integer.parseInt(args[8]);
                    if (inputMaterialMin != -1 && !MessageUtil.checkNumberSize(sender, inputMaterialMin, 0, Integer.MAX_VALUE))
                    {
                      return true;
                    }
                    if (args.length == 9 && !MessageUtil.isInteger(sender, args[8], true))
                    {
                      return true;
                    }
                    int inputMaterialMax = args.length == 10 ? Integer.parseInt(args[9]) : -1;
                    if (inputMaterialMax != -1 && !MessageUtil.checkNumberSize(sender, inputMaterialMax, Math.max(0, inputMaterialMin), Integer.MAX_VALUE))
                    {
                      return true;
                    }
                    statisticName = statistic.toString();
                    statisticString = statistic.toString();
                    String materialString = material.toString();
                    int configMaterialMin = config.getInt("recipes." + args[3] + ".extra.statistics.material." + statisticString + "." + materialString + ".min");
                    int configMaterialMax = config.getInt("recipes." + args[3] + ".extra.statistics.material." + statisticString + "." + materialString + ".max");
                    boolean configMaterialMinExists = config.contains("recipes." + args[3] + ".extra.statistics.material." + statisticString + "." + materialString + ".min");
                    boolean configMaterialMaxExists = config.contains("recipes." + args[3] + ".extra.statistics.material." + statisticString + "." + materialString + ".max");
                    if (inputMaterialMin == -1 && (!configMaterialMinExists || configMaterialMin == -1) && inputMaterialMax == -1 && (!configMaterialMaxExists || configMaterialMax == -1))
                    {
                      MessageUtil.sendError(
                              sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[3] +
                                      "&r의 통계값 rg255,204;" +
                                      statisticName +
                                      "&r의 최대, 최소값 조건이 없습니다");
                      return true;
                    }
                    if (configMaterialMinExists && inputMaterialMin == configMaterialMin && configMaterialMaxExists && inputMaterialMax == configMaterialMax)
                    {
                      MessageUtil.sendError(
                              sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[3] +
                                      "&r의 통계값 rg255,204;" +
                                      statisticName +
                                      "&r최소값 조건이 rg255,204;" +
                                      Constant.Sosu15.format(inputMaterialMin) +
                                      "&r, 최대값 조건이 rg255,204;" +
                                      Constant.Sosu15.format(inputMaterialMax) +
                                      "&r입니다");
                      return true;
                    }
                    config.set("recipes." + args[3] + ".extra.statistics.material." + statisticString + "." + materialString + ".min", inputMaterialMin == -1 ? null : inputMaterialMin);
                    config.set("recipes." + args[3] + ".extra.statistics.material." + statisticString + "." + materialString + ".max", inputMaterialMax == -1 ? null : inputMaterialMax);

                    String section1String = "recipes." + args[3] + ".extra.statistics.material." + statisticString + "." + materialString;
                    ConfigurationSection section1 = config.getConfigurationSection(section1String);
                    if (section1 != null && section1.getKeys(false).size() == 0)
                    {
                      config.set(section1String, null);
                    }

                    String section2String = "recipes." + args[3] + ".extra.statistics.material." + statisticString;
                    ConfigurationSection section2 = config.getConfigurationSection(section2String);
                    if (section2 != null && section2.getKeys(false).size() == 0)
                    {
                      config.set(section2String, null);
                    }

                    String section3String = "recipes." + args[3] + ".extra.statistics.material";
                    ConfigurationSection section3 = config.getConfigurationSection(section3String);
                    if (section3 != null && section3.getKeys(false).size() == 0)
                    {
                      config.set(section3String, null);
                    }

                    String section4String = "recipes." + args[3] + ".extra.statistics";
                    ConfigurationSection section4 = config.getConfigurationSection(section4String);
                    if (section4 != null && section4.getKeys(false).size() == 0)
                    {
                      config.set(section4String, null);
                    }

                    Variable.customRecipes.put(args[2], config);
                    customRecipeListConfig.saveConfig();
                    if (inputMaterialMin != inputMaterialMax)
                    {
                      MessageUtil.sendMessage(sender, Prefix.INFO_CUSTOM_RECIPE,
                              "커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[3] +
                                      "&r의 통계값 rg255,204;" +
                                      statisticName +
                                      "&r" +
                                      (inputMaterialMin == -1 ? ("의 최소값 조건을 제거") : ("의 최소값 조건을 rg255,204;" + Constant.Sosu15.format(inputMaterialMin) + "&r으로 지정")) +
                                      "하였고, 최대값 조건을 " +
                                      (inputMaterialMax == -1 ? ("제거") : (Constant.THE_COLOR_HEX + Constant.Sosu15.format(inputMaterialMax) + "&r으로 지정")) +
                                      "했습니다");
                    }
                    else if (inputMaterialMin == -1)
                    {
                      MessageUtil.sendMessage(sender, Prefix.INFO_CUSTOM_RECIPE,
                              "커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[3] +
                                      "&r의 통계값 rg255,204;" +
                                      statisticName +
                                      "&r의 최대, 최소값 조건을 제거했습니다");
                    }
                    else
                    {
                      MessageUtil.sendMessage(sender, Prefix.INFO_CUSTOM_RECIPE,
                              "커스텀 레시피 목록 rg255,204;" +
                                      args[2] +
                                      "&r에 있는 레시피 rg255,204;" +
                                      args[3] +
                                      "&r의 통계값 rg255,204;" +
                                      "&r의 통계값 rg255,204;" +
                                      statisticName +
                                      "&r의 최대, 최소값 조건을 rg255,204;" +
                                      Constant.Sosu15.format(inputMaterialMin) +
                                      "&r으로 지정했습니다");
                    }
                    break;
                  default:
                    MessageUtil.wrongArg(sender, 6, args);
                    MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " statistic <entity|general|material> ...");
                    return true;
                }
                break;
              case "decorative":
                if (args.length > 6)
                {
                  MessageUtil.longArg(sender, 5, args);
                  MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " decorative <꾸미기용 레시피 모드>");
                  return true;
                }
                String inputBoolean = args[5];
                if (!MessageUtil.isBoolean(sender, args, 6, true))
                {
                  return true;
                }
                boolean b = inputBoolean.equals("true");
                if (config.getBoolean("recipes." + args[3] + ".extra.decorative") == b)
                {
                  MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 커스텀 레시피 목록 %s의 레시피 %s은(는) 꾸미기용 레시피" + (b ? "입" : "가 아닙") + "니다",
                          Constant.THE_COLOR_HEX + args[2], Constant.THE_COLOR_HEX + args[3]);
                  return true;
                }
                config.set("recipes." + args[3] + ".extra.decorative", b ? true : null);
                Variable.customRecipes.put(args[2], config);
                customRecipeListConfig.saveConfig();
                MessageUtil.sendMessage(sender, Prefix.INFO_CUSTOM_RECIPE, "커스텀 레시피 목록 %s의 레시피 %s(은)는 이제 꾸미기용 레시피" + (b ? "입" : "가 아닙") + "니다",
                        Constant.THE_COLOR_HEX + args[2], Constant.THE_COLOR_HEX + args[3]);
                break;
              default:
                MessageUtil.wrongArg(sender, 5, args);
                MessageUtil.commandInfo(sender, label, "edit recipe " + args[2] + " " + args[3] + " <chance|cost|display|level|levelcost|permission|require|reusable|statistic|wealth> ...");
                return true;
            }
            break;
          default:
            MessageUtil.wrongArg(sender, 2, args);
            MessageUtil.commandInfo(sender, label, "edit <category|recipe> ...");
            return true;
        }
        break;
      default:
        MessageUtil.wrongArg(sender, 1, args);
        MessageUtil.commandInfo(sender, label, usage);
        return true;
    }
    return true;
  }

  public @NotNull List<Completion> completion(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args, @NotNull Location location)
  {
    int length = args.length;
		if (length == 1)
		{
			return CommandTabUtil.tabCompleterList(args, "<인수>", false, Completion.completion("open", ComponentUtil.translate("레시피를 엽니다")), "create", "remove",
					"edit");
		}
    return CommandTabUtil.ARGS_LONG;
  }
}

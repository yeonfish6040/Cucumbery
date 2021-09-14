package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.*;
import com.jho5245.cucumbery.util.MessageUtil.ConsonantType;
import com.jho5245.cucumbery.util.additemmanager.AddItemUtil;
import com.jho5245.cucumbery.util.storage.*;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ItemStorage implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_ITEMSTORAGE, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return sender instanceof Player;
    }
    String usage = cmd.getUsage().replace("/<command> ", "");
    if (args.length == 0)
    {
      MessageUtil.shortArg(sender, 1, args);
      MessageUtil.commandInfo(sender, label, usage);
      return true;
    }
    else if (args.length <= 8)
    {
      if (args[0].equalsIgnoreCase("list"))
      {
        if (args.length == 1)
        {
          MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "아이템 메뉴 목록 : &e" + Variable.itemStorage.size() + "개");
          if (Variable.itemStorage.size() == 0)
          {
            return true;
          }
          List<Component> txt = new ArrayList<>();
          txt.add(ComponentUtil.create(Prefix.INFO_ITEMSTORAGE));
          for (String fileName : Variable.itemStorage.keySet())
          {
            ConfigurationSection itemList = Variable.itemStorage.get(fileName).getConfigurationSection("items");
            StringBuilder itemListText = new StringBuilder();
            if (itemList == null || itemList.getKeys(false).size() == 0)
            {
              itemListText = new StringBuilder("&c아이템 없음\n");
            }
            else
            {
              for (String key : itemList.getKeys(false))
              {
                itemListText.append(key).append("\n");
              }
            }
            txt.add(ComponentUtil.create("&e" + fileName, itemListText.substring(0, itemListText.length() - 1), ClickEvent.Action.SUGGEST_COMMAND, "/cis list " + fileName));
            txt.add(ComponentUtil.create("&r, "));
          }
          txt.remove(txt.size() - 1);
          Component[] txt2 = new Component[txt.size()];
          MessageUtil.sendMessage(sender, txt);
        }
        else if (args.length == 2)
        {
          if (Variable.itemStorage.size() == 0)
          {
            MessageUtil.sendError(sender, "아이템 목록이 하나도 존재하지 않습니다.");
            return true;
          }
          if (!Variable.itemStorage.containsKey(args[1]))
          {
            MessageUtil.sendError(sender, "아이템 목록 &e" + args[1] + "&r" + MessageUtil.getFinalConsonant(args[1], ConsonantType.을를) + " 찾을 수 없습니다.");
            return true;
          }
          YamlConfiguration config = Variable.itemStorage.get(args[1]);
          ConfigurationSection itemList = config.getConfigurationSection("items");
          if (itemList == null || itemList.getKeys(false).size() == 0)
          {
            MessageUtil.sendError(sender, "아이템이 존재하지 않는 목록입니다.");
            return true;
          }
          MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "아이템 목록 &e" + args[1] + "&r의 아이템 개수 : &e" + itemList.getKeys(false).size() + "&e개");
          List<Component> txt = new ArrayList<>();
          txt.add(ComponentUtil.create(Prefix.INFO_ITEMSTORAGE));
          for (String key : itemList.getKeys(false))
          {
            ItemStack item = ItemSerializer.deserialize(config.getString("items." + key));
            txt.add(ComponentUtil.create("&e" + key, item, ClickEvent.Action.SUGGEST_COMMAND, "/cis get " + args[1] + " " + key));
            txt.add(ComponentUtil.create("&r, "));
          }
          txt.remove(txt.size() - 1);
          Component[] txt2 = new Component[txt.size()];
          MessageUtil.sendMessage(sender, (Object) txt.toArray(txt2));
        }
        else
        {
          MessageUtil.longArg(sender, 2, args);
          MessageUtil.commandInfo(sender, label, "list [아이템 목록 이름]");
          return true;
        }
      }
      else if (args[0].equalsIgnoreCase("store"))
      {
        if (!(sender instanceof Player))
        {
          MessageUtil.sendError(sender, Prefix.ONLY_PLAYER);
          return true;
        }
        if (args.length < 3)
        {
          MessageUtil.shortArg(sender, 3, args);
          MessageUtil.commandInfo(sender, label, "store <아이템 목록 이름> <저장할 아이템 이름> [기존 아이템 덮어씌움] [명령어 출력 숨김 여부]");
          return true;
        }
        else if (args.length <= 5)
        {
          Player player = (Player) sender;
          ItemStack item = player.getInventory().getItemInMainHand();
          if (!ItemStackUtil.itemExists(item))
          {
            MessageUtil.sendError(sender, Prefix.NO_HOLDING_ITEM);
            return true;
          }
          if (args[2].equals("--all") || Method2.isInvalidFileName(args[2]))
          {
            MessageUtil.sendError(player, "해당하는 이름으로는 아이템을 저장할 수 없습니다.");
            return true;
          }
          File folder = new File(Cucumbery.getPlugin().getDataFolder() + "/data/ItemStorage");
          if (!folder.exists())
          {
            boolean success = folder.mkdirs();
            if (!success)
            {
              System.err.println("[Cucumbery] could not create ItemStorage folder!");
            }
          }
          File file = new File(Cucumbery.getPlugin().getDataFolder() + "/data/ItemStorage/" + args[1] + ".yml");
          if (!file.exists())
          {
            try
            {
              boolean success = file.createNewFile();
              if (!success)
              {
                throw new IOException();
              }
            }
            catch (IOException e)
            {
              e.printStackTrace();
              return true;
            }
          }
          CustomConfig itemListConfig = CustomConfig.getCustomConfig("data/ItemStorage/" + args[1] + ".yml");
          boolean override = args.length >= 4 && args[3].equals("true");
          YamlConfiguration config = itemListConfig.getConfig();
          boolean hideOutput = false;
          if (args.length == 5)
          {
            if (!args[4].equals("true") && !args[4].equals("false"))
            {
              MessageUtil.wrongBool(sender, 5, args);
              return true;
            }
            if (args[4].equals("true"))
            {
              hideOutput = true;
            }
          }
          ConfigurationSection configurationSection = config.getConfigurationSection("items");
          if (configurationSection != null && configurationSection.contains(args[2]))
          {
            if (!hideOutput && override)
            {
              MessageUtil.sendWarn(sender, "아이템 목록 &e" + args[1] + "&r에 이미 해당 이름으로 저장된 아이템(&e" + args[2] + "&r)이 존재하여 덮어씌웠습니다.");
            }
            else
            {
              MessageUtil.sendError(sender, "아이템 목록 &e" + args[1] + "&r에 이미 해당 이름으로 저장된 아이템(&e" + args[2] + "&r)이 존재하여 저장할 수 없습니다.");
              return true;
            }
          }
          config.set("items." + args[2], ItemSerializer.serialize(item));
          itemListConfig.saveConfig();
          Variable.itemStorage.put(args[1], config);
          MessageUtil.sendMessage(player, Prefix.INFO_ITEMSTORAGE, "주로 사용하는 손에 들고 있는 아이템을 아이템 목록 &e" + args[1] + "&r에 &e" + args[2] + "&r" + MessageUtil.getFinalConsonant(args[2], ConsonantType.이라) + "는 이름으로 저장했습니다.");
        }
        else
        {
          MessageUtil.longArg(sender, 5, args);
          MessageUtil.commandInfo(sender, label, "store <아이템 목록 이름> <저장할 아이템 이름> [기존 아이템 덮어씌움] [명령어 출력 숨김 여부]");
          return true;
        }
      }
      else if (args[0].equalsIgnoreCase("remove"))
      {
        if (args.length < 3)
        {
          MessageUtil.shortArg(sender, 3, args);
          MessageUtil.commandInfo(sender, label, "remove <아이템 목록 이름> <제거할 아이템 이름|--all> [명령어 출력 숨김 여부]");
          return true;
        }
        else if (args.length <= 4)
        {
          if (!Variable.itemStorage.containsKey(args[1]))
          {
            MessageUtil.sendError(sender, "아이템 목록 &e" + args[1] + "&r" + MessageUtil.getFinalConsonant(args[1], ConsonantType.을를) + " 찾을 수 없습니다.");
            return true;
          }
          YamlConfiguration config = Variable.itemStorage.get(args[1]);
          boolean hideOutput = false;
          if (args.length == 4)
          {
            if (!args[3].equals("true") && !args[3].equals("false"))
            {
              MessageUtil.wrongBool(sender, 2, args);
              return true;
            }
            if (args[3].equals("true"))
            {
              hideOutput = true;
            }
          }
          File file = new File(Cucumbery.getPlugin().getDataFolder() + "/data/ItemStorage/" + args[1] + ".yml");
          if (args[2].equalsIgnoreCase("--all"))
          {
            boolean success = file.delete();
            if (!success)
            {
              System.err.println("[Cucumbery] could not delete " + args[1] + ".yml file!");
            }
            Variable.itemStorage.remove(args[1]);
            if (!hideOutput)
            {
              MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "아이템 목록 &e" + args[1] + "&r에 있는 모든 아이템을 제거하였습니다.");
            }
            return true;
          }
          ConfigurationSection configurationSection = config.getConfigurationSection("items");
          if (configurationSection == null || !configurationSection.contains(args[2]))
          {
            MessageUtil.sendError(sender, "아이템 목록 &e" + args[1] + "&r에서 해당 아이템(&e" + args[2] + "&r)을 찾을 수 없습니다.");
            return true;
          }
          CustomConfig itemListConfig = CustomConfig.getCustomConfig("data/ItemStorage/" + args[1] + ".yml");
          itemListConfig.getConfig().set("items." + args[2], null);
          config.set("items." + args[2], null);
          if (configurationSection.getKeys(false).size() == 0)
          {
            boolean success = file.delete();
            if (!success)
            {
              System.err.println("[Cucumbery] could not delete " + args[1] + ".yml file!");
            }
            Variable.itemStorage.remove(args[1]);
          }
          else
          {
            itemListConfig.saveConfig();
            Variable.itemStorage.put(args[1], config);
          }
          if (!hideOutput)
          {
            MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "아이템 목록 &e" + args[1] + "&r에서 &e" + args[2] + "&r 아이템을 제거하였습니다.");
          }
        }
        else
        {
          MessageUtil.longArg(sender, 4, args);
          MessageUtil.commandInfo(sender, label, "remove <아이템 목록 이름> <제거할 아이템 이름|--all> [명령어 출력 숨김 여부]");
          return true;
        }
      }
      else if (args[0].equalsIgnoreCase("get"))
      {
        if (!(sender instanceof Player))
        {
          MessageUtil.sendError(sender, Prefix.ONLY_PLAYER);
          return true;
        }
        if (args.length < 3)
        {
          MessageUtil.shortArg(sender, 3, args);
          MessageUtil.commandInfo(sender, label, "get <아이템 목록 이름> <아이템 이름> [개수]");
          return true;
        }
        else if (args.length <= 4)
        {
          Player player = (Player) sender;
          if (!Variable.itemStorage.containsKey(args[1]))
          {
            MessageUtil.sendError(sender, "아이템 목록 &e" + args[1] + "&r" + MessageUtil.getFinalConsonant(args[1], ConsonantType.을를) + " 찾을 수 없습니다.");
            return true;
          }
          YamlConfiguration config = Variable.itemStorage.get(args[1]);
          ConfigurationSection itemsSection = config == null ? null : config.getConfigurationSection("items");
          if (itemsSection != null && args[2].equals("--all"))
          {
            for (String key : itemsSection.getKeys(false))
            {
              Bukkit.dispatchCommand(sender, "cis get " + args[1] + " " + key + (args.length >= 4 ? (" " + args[3]) : ""));
            }
            return true;
          }
          boolean validItem = true;
          ItemStack item = null;
          if (config == null || !config.contains("items." + args[2]))
          {
            validItem = false;
          }
          else
          {
            item = ItemSerializer.deserialize(config.getString("items." + args[2]));
          }
          if (!ItemStackUtil.itemExists(item))
          {
            item = config != null ? config.getItemStack("items." + args[2]) : null;
          }
          validItem = validItem && ItemStackUtil.itemExists(item);
          if (!validItem)
          {
            MessageUtil.sendError(sender, "아이템 목록 &e" + args[1] + "&r에서 해당 아이템(&e" + args[2] + "&r)을 찾을 수 없거나 손상된 아이템입니다.");
            return true;
          }
          if (args.length == 4)
          {
            if (!MessageUtil.isInteger(sender, args[3], true))
            {
              return true;
            }
            int inputAmount = Integer.parseInt(args[3]);
            if (!MessageUtil.checkNumberSize(sender, inputAmount, 1, 2304))
            {
              return true;
            }
            item.setAmount(inputAmount);
          }
          item = item.clone();
          int amount = item.getAmount();
          ItemStack lostItem = null;
          int lostAmount = 0;
          Collection<ItemStack> lostItems = player.getInventory().addItem(item).values();
          if (lostItems.size() != 0)
          {
            for (ItemStack lost : lostItems)
            {
              lostItem = lost;
            }
          }
          String itemName = args[2];
          String finalConsonant = MessageUtil.getFinalConsonant(itemName, ConsonantType.을를);
          if (lostItem != null)
          {
            lostAmount = lostItem.getAmount();
            String lostDisplay = (lostAmount == amount ? "&c전부&r(&e총 " + lostAmount + "개&r)" : lostAmount + "개");
            Component txt = ComponentUtil.create(Prefix.INFO_WARN + "인벤토리가 가득 차서 &e" + itemName + "&r" + finalConsonant + "&e " + lostDisplay + "&r 지급받지 못하였습니다.", item);
            MessageUtil.sendMessage(player, txt);
            SoundPlay.playSound(player, Constant.WARNING_SOUND);
            amount -= lostAmount;
          }
          if (amount != 0)
          {
            String amountDisplay = (lostAmount == 0 ? amount + "" : amount + "&r(&e" + (amount + lostAmount) + "&r - &e" + lostAmount + "&r)&e");
            Component txt = ComponentUtil.create(Prefix.INFO_ITEMSTORAGE + "&e" + itemName + "&r" + finalConsonant + "&e " + amountDisplay + "개&r 지급받았습니다.", item);
            MessageUtil.sendMessage(sender, txt);
          }
        }
        else
        {
          MessageUtil.longArg(sender, 4, args);
          MessageUtil.commandInfo(sender, label, "get <아이템 목록 이름> <아이템 이름> [개수]");
          return true;
        }
      }
      else if (args[0].equalsIgnoreCase("give"))
      {
        if (args.length < 4)
        {
          MessageUtil.shortArg(sender, 4, args);
          MessageUtil.commandInfo(sender, label, "give <플레이어> <아이템 목록 이름> <아이템 이름> [개수] [명령어 출력 숨김 여부]");
          return true;
        }
        else if (args.length <= 6)
        {
          Player target = SelectorUtil.getPlayer(sender, args[1]);
          if (target == null)
          {
            return true;
          }
          if (!Variable.itemStorage.containsKey(args[2]))
          {
            MessageUtil.sendError(sender, "아이템 목록 &e" + args[2] + "&r" + MessageUtil.getFinalConsonant(args[2], ConsonantType.을를) + " 찾을 수 없습니다.");
            return true;
          }
          YamlConfiguration config = Variable.itemStorage.get(args[2]);
          ConfigurationSection itemsSection = config == null ? null : config.getConfigurationSection("items");
          if (itemsSection != null && args[3].equals("--all"))
          {
            for (String key : itemsSection.getKeys(false))
            {
              Bukkit.dispatchCommand(sender, "cis give " + args[1] + " " + args[2] + " " + key + (args.length >= 5 ? (" " + args[4]) : "") + (args.length >= 6 ? (" " + args[5]) : ""));
            }
            return true;
          }
          boolean validItem = true;
          ItemStack item = null;
          if (config == null || !config.contains("items." + args[3]))
          {
            validItem = false;
          }
          else
          {
            item = ItemSerializer.deserialize(config.getString("items." + args[3]));
          }
          if (!ItemStackUtil.itemExists(item))
          {
            item = config != null ? config.getItemStack("items." + args[3]) : null;
          }
          validItem = validItem && ItemStackUtil.itemExists(item);
          if (!validItem)
          {
            MessageUtil.sendError(sender, "아이템 목록 &e" + args[2] + "&r에서 해당 아이템(&e" + args[3] + "&r)을 찾을 수 없거나 손상된 아이템입니다.");
            return true;
          }
          item = item.clone();
          int amount = item.getAmount();
          if (args.length >= 5)
          {
            if (!MessageUtil.isInteger(sender, args[4], true))
            {
              return true;
            }
            int inputAmount = Integer.parseInt(args[4]);
            if (inputAmount != -1 && !MessageUtil.checkNumberSize(sender, inputAmount, 1, 2304))
            {
              return true;
            }
            item.setAmount(inputAmount == -1 ? amount : inputAmount);
          }
          ItemStack lostItem = null;
          int lostAmount = 0;
          boolean hideOutput = false;
          if (args.length == 6)
          {
            if (!args[5].equals("true") && !args[5].equals("false"))
            {
              MessageUtil.wrongBool(sender, 6, args);
              return true;
            }
            if (args[5].equals("true"))
            {
              hideOutput = true;
            }
          }
          AddItemUtil.addItemResult2(sender, target, item, amount).sendFeedback(hideOutput);
        }
        else
        {
          MessageUtil.longArg(sender, 6, args);
          MessageUtil.commandInfo(sender, label, "give <플레이어> <아이템 목록 이름> <아이템 이름> [개수] [명령어 출력 숨김 여부]");
          return true;
        }
      }
      else if (args[0].equalsIgnoreCase("setitem"))
      {
        if (args.length < 5)
        {
          MessageUtil.shortArg(sender, 5, args);
          MessageUtil.commandInfo(sender, label, "setitem <플레이어> <슬롯> <아이템 목록 이름> <아이템 이름> [개수] [기존 아이템 덮어씌움] [명령어 출력 숨김 여부]");
          return true;
        }
        else
        {
          Player target = SelectorUtil.getPlayer(sender, args[1]);
          if (target == null)
          {
            return true;
          }
          int slot;
          int heldItemSlot = target.getInventory().getHeldItemSlot();
          switch (args[2])
          {
            case "weapon":
            case "weapon.mainhand":
              slot = heldItemSlot + 1;
              break;
            case "weapon.offhand":
              slot = 41;
              break;
            case "armor.head":
              slot = 37;
              break;
            case "armor.chest":
              slot = 38;
              break;
            case "armor.legs":
              slot = 39;
              break;
            case "armor.feet":
              slot = 40;
              break;
            default:
              if (args[2].startsWith("inventory."))
              {
                try
                {
                  slot = Integer.parseInt(args[2].replace("inventory.", "")) + 9;
                }
                catch (Exception e)
                {
                  MessageUtil.wrongArg(sender, 3, args);
                  return true;
                }
                if (slot < 10 || slot > 36)
                {
                  MessageUtil.wrongArg(sender, 3, args);
                  return true;
                }
              }
              else if (args[2].startsWith("hotbar."))
              {
                try
                {
                  slot = Integer.parseInt(args[2].replace("hotbar.", ""));
                }
                catch (Exception e)
                {
                  MessageUtil.wrongArg(sender, 3, args);
                  return true;
                }
                if (slot < 1 || slot > 9)
                {
                  MessageUtil.wrongArg(sender, 3, args);
                  return true;
                }
              }
              else
              {
                MessageUtil.wrongArg(sender, 3, args);
                return true;
              }
          }
          if (!Variable.itemStorage.containsKey(args[3]))
          {
            MessageUtil.sendError(sender, "아이템 목록 &e" + args[3] + "&r" + MessageUtil.getFinalConsonant(args[3], ConsonantType.을를) + " 찾을 수 없습니다.");
            return true;
          }
          YamlConfiguration config = Variable.itemStorage.get(args[3]);
          boolean validItem = true;
          ItemStack item = null;
          if (config == null || !config.contains("items." + args[4]))
          {
            validItem = false;
          }
          else
          {
            item = ItemSerializer.deserialize(config.getString("items." + args[4]));
          }
          if (!ItemStackUtil.itemExists(item))
          {
            item = config != null ? config.getItemStack("items." + args[4]) : null;
          }
          validItem = validItem && ItemStackUtil.itemExists(item);
          if (!validItem)
          {
            MessageUtil.sendError(sender, "아이템 목록 &e" + args[3] + "&r에서 해당 아이템(&e" + args[4] + "&r)을 찾을 수 없거나 손상된 아이템입니다.");
            return true;
          }
          item = item.clone();
          int amount = item.getAmount();
          if (args.length >= 6)
          {
            if (!MessageUtil.isInteger(sender, args[5], true))
            {
              return true;
            }
            int inputAmount = Integer.parseInt(args[5]);
            if (!MessageUtil.checkNumberSize(sender, inputAmount, 1, 127))
            {
              return true;
            }
            if (inputAmount == 0)
            {
              MessageUtil.sendError(sender, Prefix.ONLY_NATURALNUMBER);
              return true;
            }
            item.setAmount(inputAmount == -1 ? amount : inputAmount);
          }
          if (!MessageUtil.isBoolean(sender, args, 7, true))
          {
            return true;
          }
          boolean override = args.length >= 7 && args[6].equals("true");
          if (!MessageUtil.isBoolean(sender, args, 8, true))
          {
            return true;
          }
          boolean hideOutput = args.length == 8 && args[7].equals("true");
          ItemStack origin = target.getInventory().getItem(slot - 1);
          boolean itemExists = ItemStackUtil.itemExists(origin);
          if (itemExists)
          {
            origin = origin.clone();
          }
          if (!override && itemExists)
          {
            if (!hideOutput)
            {
              MessageUtil.sendMessage(sender, Prefix.INFO_ERROR, "아이템을 지급할 수 없습니다. &e", target, "은(는) 이미 ", ComponentUtil.create(args[2]).hoverEvent(origin.asHoverEvent()) + " 슬롯에 아이템을 가지고 있습니다.");
              if (sender instanceof Player)
              {
                Method.playSound(sender, Constant.ERROR_SOUND, Constant.ERROR_SOUND_VOLUME, Constant.ERROR_SOUND_PITCH);
              }
            }
            return true;
          }
          target.getInventory().setItem(slot - 1, item);
          if (!hideOutput)
          {
            if (override && itemExists)
            {
              MessageUtil.sendMessage(sender, Prefix.INFO_WARN, "이미 ", target + " 의 " + ComponentUtil.create(args[2]).hoverEvent(origin.asHoverEvent()) + " 슬롯에 아이템이 존재하여 덮어씌웠습니다.");
              if (sender instanceof Player)
              {
                Method.playSound(sender, Constant.WARNING_SOUND, Constant.WARNING_SOUND_VOLUME, Constant.WARNING_SOUND_PITCH);
              }
            }
            String itemName = args[3];
            String finalConsonant = MessageUtil.getFinalConsonant(itemName, ConsonantType.을를);
            Component txt = ComponentUtil.create(MessageUtil.as(Prefix.INFO_ITEMSTORAGE, sender, "이 당신의 &e" + args[2] + "&r 슬롯에 &e" + itemName + "&r" + finalConsonant + "&e " + amount + "개&r 지급하였습니다."), item);
            if (!target.equals(sender))
            {
              MessageUtil.sendMessage(target, txt);
            }
            txt = ComponentUtil.create(MessageUtil.as(Prefix.INFO_ITEMSTORAGE, target, "의 &e" + args[2] + "&r 슬롯에 &e" + itemName + "&r" + finalConsonant + "&e " + amount + "개&r 지급하였습니다."), item);
            MessageUtil.sendMessage(sender, txt);
          }
        }
      }
      else if (args[0].equalsIgnoreCase("info"))
      {
        if (args.length < 3)
        {
          MessageUtil.shortArg(sender, 3, args);
          MessageUtil.commandInfo(sender, label, "info <아이템 목록 이름> <아이템 이름>");
          return true;
        }
        else if (args.length == 3)
        {
          File folder = new File(Cucumbery.getPlugin().getDataFolder() + "/data/ItemStorage");
          if (!folder.exists())
          {
            boolean success = folder.mkdirs();
            if (!success)
            {
              System.err.println("[Cucumbery] could not create ItemStorage folder!");
            }
          }
          File file = new File(Cucumbery.getPlugin().getDataFolder() + "/data/ItemStorage/" + args[1] + ".yml");
          if (!file.exists())
          {
            MessageUtil.sendError(sender, "아이템 목록 &e" + args[1] + "&r" + MessageUtil.getFinalConsonant(args[1], ConsonantType.을를) + " 찾을 수 없습니다.");
            return true;
          }
          CustomConfig itemListConfig = CustomConfig.getCustomConfig("data/ItemStorage/" + args[1] + ".yml");
          FileConfiguration config = itemListConfig.getConfig();
          if (config.getItemStack("items." + args[2]) == null)
          {
            MessageUtil.sendError(sender, "아이템 목록 &e" + args[1] + "&r에서 해당 아이템(&e" + args[2] + "&r)을 찾을 수 없습니다.");
            return true;
          }
          ItemStack item = config.getItemStack("items." + args[2]);
          ItemInfo.sendInfo(sender, item);
        }
        else
        {
          MessageUtil.longArg(sender, 3, args);
          MessageUtil.commandInfo(sender, label, "info <아이템 목록 이름> <아이템 이름>");
          return true;
        }
      }
      else
      {
        MessageUtil.wrongArg(sender, 1, args);
        MessageUtil.commandInfo(sender, label, usage);
        return true;
      }
    }
    else
    {
      if (args[0].equalsIgnoreCase("list"))
      {
        MessageUtil.longArg(sender, 2, args);
        MessageUtil.commandInfo(sender, label, "list [아이템 목록 이름]");
        return true;
      }
      else if (args[0].equalsIgnoreCase("store"))
      {
        MessageUtil.longArg(sender, 3, args);
        MessageUtil.commandInfo(sender, label, "store <아이템 목록 이름> <저장할 아이템 이름> [기존 아이템 덮어씌움] [명령어 출력 숨김 여부]");
        return true;
      }
      else if (args[0].equalsIgnoreCase("remove"))
      {
        MessageUtil.longArg(sender, 3, args);
        MessageUtil.commandInfo(sender, label, "remove <아이템 목록 이름> <제거할 아이템 이름|--all> [명령어 출력 숨김 여부]");
        return true;
      }
      else if (args[0].equalsIgnoreCase("get"))
      {
        MessageUtil.longArg(sender, 4, args);
        MessageUtil.commandInfo(sender, label, "get <아이템 목록 이름> <아이템 이름> [개수]");
        return true;
      }
      else if (args[0].equalsIgnoreCase("give"))
      {
        MessageUtil.longArg(sender, 6, args);
        MessageUtil.commandInfo(sender, label, "give <플레이어> <아이템 목록 이름> <아이템 이름> [개수] [명령어 출력 숨김 여부]");
        return true;
      }
      else if (args[0].equalsIgnoreCase("setitem"))
      {
        MessageUtil.longArg(sender, 7, args);
        MessageUtil.commandInfo(sender, label, "setitem <플레이어> <슬롯> <아이템 목록 이름> <아이템 이름> [개수] [명령어 출력 숨김 여부]");
        return true;
      }
      else if (args[0].equalsIgnoreCase("checkamount"))
      {
        MessageUtil.longArg(sender, 6, args);
        MessageUtil.commandInfo(sender, label, "checkamount <플레이어> <아이템 목록 이름> <아이템 이름> [내구도|*]");
        return true;
      }
      else if (args[0].equalsIgnoreCase("info"))
      {
        MessageUtil.longArg(sender, 3, args);
        MessageUtil.commandInfo(sender, label, "info <아이템 목록 이름> <아이템 이름>");
        return true;
      }
      else
      {
        MessageUtil.longArg(sender, 7, args);
        MessageUtil.commandInfo(sender, label, usage);
        return true;
      }
    }
    return true;
  }
}
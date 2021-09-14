package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.*;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.CustomConfig;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.UUID;

public class VirtualChest implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!(sender instanceof Player))
    {
      MessageUtil.sendError(sender, Prefix.ONLY_PLAYER);
      return true;
    }
    String usage = Method.getUsage(cmd);
    switch (cmd.getName())
    {
      case "virtualchest" -> {
        if (!Method.hasPermission(sender, Permission.CMD_VIRTUAL_CHEST, true))
        {
          return true;
        }
        if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
        {
          return true;
        }
        if (args.length > 1)
        {
          MessageUtil.longArg(sender, 1, args);
          MessageUtil.commandInfo(sender, label, usage);
          return true;
        }
        Player player = (Player) sender;
        if (args.length == 1 && !args[0].equals("default") && !Method.hasPermission(player, Permission.CMD_VIRTUAL_CHEST_UNLIMITED, false)
                && !Method.hasPermission(player, Permission.CMD_VIRTUAL_CHEST + "." + args[0], false))
        {
          MessageUtil.sendError(player, "해당 가상창고를 사용할 권한이 없습니다.");
          return true;
        }
        String chestName = args.length == 1 ? args[0] : "default";
        if (Method2.isInvalidFileName(chestName))
        {
          MessageUtil.sendError(player, "해당하는 이름으로는 가상창고를 사용할 수 없습니다.");
          return true;
        }
        UUID uuid = player.getUniqueId();
        CustomConfig customConfig = CustomConfig.getCustomConfig("data/VirtualChest/" + uuid.toString() + "/" + chestName + ".yml");
        YamlConfiguration config = customConfig.getConfig();
        Inventory chest = Bukkit.createInventory(null, 54, Constant.VIRTUAL_CHEST_MENU_PREFIX + "§6가상창고 - §e" + chestName);
        ConfigurationSection items = config.getConfigurationSection("items");
        if (items != null && items.getKeys(false).size() > 0)
        {
          for (int i = 1; i <= 54; i++)
          {
            String itemString = config.getString("items." + i);
            if (itemString != null)
            {
              ItemStack item = ItemSerializer.deserialize(itemString);
              chest.setItem(i - 1, item);
            }
          }
        }
        if (Method.inventoryEmpty(chest))
        {
          customConfig.delete();
        }
        player.openInventory(chest);
        MessageUtil.sendMessage(player, Prefix.INFO_VIRTUAL_CHEST, "&e" + chestName + "&r 가상창고를 엽니다.");
      }
      case "virtualchestadd" -> {
        if (!Method.hasPermission(sender, Permission.CMD_VIRTUAL_CHEST_ADMIN, true))
        {
          return true;
        }

        if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
        {
          return true;
        }
        if (args.length > 1)
        {
          MessageUtil.longArg(sender, 1, args);
          MessageUtil.commandInfo(sender, label, usage);
          return true;
        }
        Player player = (Player) sender;
        if (args.length == 1 && !args[0].equals("default") && !Method.hasPermission(player, Permission.CMD_VIRTUAL_CHEST_UNLIMITED, false)
                && !Method.hasPermission(player, Permission.CMD_VIRTUAL_CHEST + "." + args[0], false))
        {
          MessageUtil.sendError(player, "해당 가상창고를 사용할 권한이 없습니다.");
          return true;
        }
        ItemStack handItem = player.getInventory().getItemInMainHand();
        if (!ItemStackUtil.itemExists(handItem))
        {
          MessageUtil.sendError(player, Prefix.NO_HOLDING_ITEM);
          return true;
        }
        String chestName = args.length == 1 ? args[0] : "default";
        if (Method2.isInvalidFileName(chestName))
        {
          MessageUtil.sendError(player, "해당하는 이름으로는 가상창고를 사용할 수 없습니다.");
          return true;
        }
        UUID uuid = player.getUniqueId();
        CustomConfig customConfig = CustomConfig.getCustomConfig("data/VirtualChest/" + uuid.toString() + "/" + chestName + ".yml");
        YamlConfiguration config = customConfig.getConfig();
        Inventory chest = Bukkit.createInventory(null, 54, Constant.VIRTUAL_CHEST_MENU_PREFIX + "§6가상창고 - §e" + chestName);
        ConfigurationSection items = config.getConfigurationSection("items");
        if (items != null && items.getKeys(false).size() > 0)
        {
          for (int i = 1; i <= 54; i++)
          {
            String itemString = config.getString("items." + i);
            if (itemString != null)
            {
              ItemStack item = ItemSerializer.deserialize(itemString);
              chest.setItem(i - 1, item);
            }
          }
        }
        if (chest.firstEmpty() == -1)
        {
          MessageUtil.sendError(player, "&e" + chestName + "&r 가상창고에는 아이템이 가득 찼습니다.");
          return true;
        }
        chest.addItem(handItem);
        ItemStack[] contents = chest.getContents();
        for (int i = 1; i <= contents.length; i++)
        {
          config.set("items." + i, ItemSerializer.serialize(contents[i - 1]));
        }
        customConfig.saveConfig();
        String display = ComponentUtil.itemName(handItem).toString();
        MessageUtil.sendMessage(player, Prefix.INFO_VIRTUAL_CHEST, "&e" + chestName + "&r 가상창고에 &e" + display + "&r" + MessageUtil.getFinalConsonant(display, MessageUtil.ConsonantType.을를) + " 추가하였습니다.");
      }
      case "virtualchestadmin" -> {
        args = MessageUtil.wrapWithQuote(args);
        if (!Method.hasPermission(sender, Permission.CMD_VIRTUAL_CHEST_ADMIN, true))
        {
          return true;
        }
        if (args.length < 1)
        {
          MessageUtil.shortArg(sender, 1, args);
          MessageUtil.commandInfo(sender, label, usage);
          return true;
        }
        if (args.length > 2)
        {
          MessageUtil.longArg(sender, 2, args);
          MessageUtil.commandInfo(sender, label, usage);
          return true;
        }
        Player player = (Player) sender;
        OfflinePlayer target = SelectorUtil.getOfflinePlayer(sender, args[0], true);
        if (target == null)
        {
          return true;
        }
        UUID uuid = target.getUniqueId();
        String chestName = args.length == 2 ? args[1] : "default";
        if (Method2.isInvalidFileName(chestName))
        {
          MessageUtil.sendError(player, "해당하는 이름으로는 가상창고를 사용할 수 없습니다.");
          return true;
        }
        YamlConfiguration config;
        File file = new File(Cucumbery.getPlugin().getDataFolder() + "/data/VirtualChest/" + uuid.toString() + "/" + chestName + ".yml");
        if (!file.exists())
        {
          config = null;
        }
        else
        {
          config = CustomConfig.getCustomConfig(file).getConfig();
        }
        if (config == null)
        {
          MessageUtil.sendError(player, "&e" + Method.getDisplayName(target) + "&r의 §e" + chestName + "&r 가상창고가 존재하지 않습니다.");
          return true;
        }
        Inventory chest = Bukkit.createInventory(null, 54,
                Constant.VIRTUAL_CHEST_ADMIN_MENU_PREFIX + "§6가상창고 - §e" + chestName + Method.format("owneruuid:" + target.getUniqueId().toString(), "§"));
        ConfigurationSection items = config.getConfigurationSection("items");
        if (items != null && items.getKeys(false).size() > 0)
        {
          for (int i = 1; i <= 54; i++)
          {
            String itemString = config.getString("items." + i);
            if (itemString != null)
            {
              ItemStack item = ItemSerializer.deserialize(itemString);
              chest.setItem(i - 1, item);
            }
          }
        }
        player.openInventory(chest);
        MessageUtil.sendMessage(player, Prefix.INFO_VIRTUAL_CHEST, "&e" + Method.getDisplayName(target) + "&r의 &e" + chestName + "&r 가상창고를 엽니다.");
      }
    }
    return true;
  }
}

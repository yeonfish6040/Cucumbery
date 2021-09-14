package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ItemFlags implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
		if (!Method.hasPermission(sender, Permission.CMD_SETDATA, true))
		{
			return true;
		}
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return sender instanceof Player;
    }
    if (!(sender instanceof Player))
    {
      MessageUtil.sendError(sender, Prefix.ONLY_PLAYER);
      return true;
    }
    String usage = cmd.getUsage().replace("/<command> ", "");
    if (args.length < 2)
    {
      MessageUtil.shortArg(sender, 2, args);
      MessageUtil.commandInfo(sender, label, usage);
    }
    else if (args.length == 2)
    {
      Player player = (Player) sender;
      ItemStack item = player.getInventory().getItemInMainHand();
      if (!ItemStackUtil.itemExists(item))
      {
        MessageUtil.sendError(sender, Prefix.NO_HOLDING_ITEM);
        return true;
      }
      item = item.clone();
      ItemMeta meta = item.getItemMeta();
      if (args[0].equalsIgnoreCase("add"))
      {
        switch (args[1].toUpperCase())
        {
          case "ALL":
            meta.addItemFlags(ItemFlag.values());
            item.setItemMeta(meta);
            player.getInventory().setItemInMainHand(item);
            MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템에 모든 플래그를 추가하였습니다.");
            return true;
          case "HIDE_ATTRIBUTES":
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            break;
          case "HIDE_DESTROYS":
            meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
            break;
          case "HIDE_ENCHANTS":
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            break;
          case "HIDE_PLACED_ON":
            meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
            break;
          case "HIDE_POTION_EFFECTS":
            meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            break;
          case "HIDE_UNBREAKABLE":
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            break;
          case "HIDE_DYE":
            meta.addItemFlags(ItemFlag.HIDE_DYE);
            break;
          case "내구성":
            meta.setUnbreakable(true);
            break;
          default:
            MessageUtil.wrongArg(sender, 2, args);
            MessageUtil.commandInfo(sender, label, usage);
            return true;
        }
        MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템에 &e" + args[1] + "&r 플래그를 추가하였습니다.");
        item.setItemMeta(meta);
        player.getInventory().setItemInMainHand(item);
        if (Cucumbery.config.getBoolean("use-helpful-lore-feature"))
        {
          String worldName = player.getLocation().getWorld().getName();
          if (!Cucumbery.config.getStringList("no-use-helpful-lore-feature-worlds").contains(worldName))
          {
            ItemLore.setItemLore(player.getInventory().getItemInMainHand());
          }
        }
      }
      else if (args[0].equalsIgnoreCase("remove"))
      {
        switch (args[1].toUpperCase())
        {
          case "ALL":
            meta.removeItemFlags(ItemFlag.values());
            item.setItemMeta(meta);
            player.getInventory().setItemInMainHand(item);
            MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템에 모든 플래그를 제거하였습니다.");
            return true;
          case "HIDE_ATTRIBUTES":
            meta.removeItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            break;
          case "HIDE_DESTROYS":
            meta.removeItemFlags(ItemFlag.HIDE_DESTROYS);
            break;
          case "HIDE_ENCHANTS":
            meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
            break;
          case "HIDE_PLACED_ON":
            meta.removeItemFlags(ItemFlag.HIDE_PLACED_ON);
            break;
          case "HIDE_POTION_EFFECTS":
            meta.removeItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            break;
          case "HIDE_UNBREAKABLE":
            meta.removeItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            break;
          case "HIDE_DYE":
            meta.removeItemFlags(ItemFlag.HIDE_DYE);
            break;
          case "내구성":
            meta.setUnbreakable(false);
            break;
          default:
            MessageUtil.wrongArg(sender, 2, args);
            MessageUtil.commandInfo(sender, label, usage);
            return true;
        }
        MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템에 &e" + args[1] + "&r 플래그를 제거하였습니다.");
        item.setItemMeta(meta);
        player.getInventory().setItemInMainHand(item);
        if (Cucumbery.config.getBoolean("use-helpful-lore-feature"))
        {
          String worldName = player.getLocation().getWorld().getName();
          if (!Cucumbery.config.getStringList("no-use-helpful-lore-feature-worlds").contains(worldName))
          {
            ItemLore.setItemLore(player.getInventory().getItemInMainHand());
          }
        }
      }
      else
      {
        MessageUtil.wrongArg(sender, 1, args);
        MessageUtil.commandInfo(sender, label, usage);
      }
    }
    else
    {
      MessageUtil.longArg(sender, 2, args);
      MessageUtil.commandInfo(sender, label, usage);
    }
    return true;
  }
}

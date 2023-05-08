package com.jho5245.cucumbery.commands.no_groups;

import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandItemFlag implements CommandExecutor, TabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_SETDATA, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
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
          case "ALL" ->
          {
            meta.addItemFlags(ItemFlag.values());
            item.setItemMeta(meta);
            player.getInventory().setItemInMainHand(item);
            MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템에 모든 플래그를 추가했습니다");
            return true;
          }
          case "HIDE_ATTRIBUTES" -> meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
          case "HIDE_DESTROYS" -> meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
          case "HIDE_ENCHANTS" -> meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
          case "HIDE_PLACED_ON" -> meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
          case "HIDE_POTION_EFFECTS" -> meta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
          case "HIDE_UNBREAKABLE" -> meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
          case "HIDE_DYE" -> meta.addItemFlags(ItemFlag.HIDE_DYE);
          case "HIDE_ARMOR_TRIM" -> meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
          case "내구성" -> meta.setUnbreakable(true);
          default ->
          {
            MessageUtil.wrongArg(sender, 2, args);
            MessageUtil.commandInfo(sender, label, usage);
            return true;
          }
        }
        MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템에 rg255,204;" + args[1] + "&r 플래그를 추가했습니다");
        item.setItemMeta(meta);
        player.getInventory().setItemInMainHand(item);
        ItemLore.setItemLore(player.getInventory().getItemInMainHand());
      }
      else if (args[0].equalsIgnoreCase("remove"))
      {
        switch (args[1].toUpperCase())
        {
          case "ALL" ->
          {
            meta.removeItemFlags(ItemFlag.values());
            item.setItemMeta(meta);
            player.getInventory().setItemInMainHand(item);
            MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템에 모든 플래그를 제거했습니다");
            return true;
          }
          case "HIDE_ATTRIBUTES" -> meta.removeItemFlags(ItemFlag.HIDE_ATTRIBUTES);
          case "HIDE_DESTROYS" -> meta.removeItemFlags(ItemFlag.HIDE_DESTROYS);
          case "HIDE_ENCHANTS" -> meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
          case "HIDE_PLACED_ON" -> meta.removeItemFlags(ItemFlag.HIDE_PLACED_ON);
          case "HIDE_POTION_EFFECTS" -> meta.removeItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
          case "HIDE_UNBREAKABLE" -> meta.removeItemFlags(ItemFlag.HIDE_UNBREAKABLE);
          case "HIDE_DYE" -> meta.removeItemFlags(ItemFlag.HIDE_DYE);
          case "HIDE_ARMOR_TRIM" -> meta.removeItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
          case "내구성" -> meta.setUnbreakable(false);
          default ->
          {
            MessageUtil.wrongArg(sender, 2, args);
            MessageUtil.commandInfo(sender, label, usage);
            return true;
          }
        }
        MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템에 rg255,204;" + args[1] + "&r 플래그를 제거했습니다");
        item.setItemMeta(meta);
        player.getInventory().setItemInMainHand(item);
        ItemLore.setItemLore(player.getInventory().getItemInMainHand());
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

  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!(sender instanceof Player player))
    {
      return Collections.emptyList();
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(true, args), true))
    {
      return Collections.singletonList(args[0]);
    }
    int length = args.length;

    ItemStack item = player.getInventory().getItemInMainHand();
    if (!ItemStackUtil.itemExists(item))
    {
      return Collections.singletonList(Prefix.NO_HOLDING_ITEM.toString());
    }
    if (length == 1)
    {
      return Method.tabCompleterList(args, "<인수>", "add", "remove");
    }
    else if (length == 2)
    {
      return Method.tabCompleterList(args, Method.addAll(ItemFlag.values(), "all", "내구성"), "<아이템 플래그>");
    }

    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}

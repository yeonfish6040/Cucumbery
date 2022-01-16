package com.jho5245.cucumbery.commands.debug;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.component.util.sendercomponent.SenderComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandCheckAmount implements CommandExecutor, TabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_CHECK_AMOUNT, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    String usage = cmd.getUsage().replace("/<command> ", "");
    if (args.length < 3)
    {
      MessageUtil.shortArg(sender, 3, args);
      MessageUtil.commandInfo(sender, label, usage);
    }
    else if (args.length <= 4)
    {
      Player player = SelectorUtil.getPlayer(sender, args[0]);
      if (player == null)
      {
        return true;
      }
      if (!args[1].equals("space") && !args[1].equals("amount"))
      {
        MessageUtil.wrongArg(sender, 2, args);
        return true;
      }
      boolean isSpace = args[1].equals("space");
      Material material;
      try
      {
        material = Material.valueOf(args[2].toUpperCase());
        if (!material.isItem() || material == Material.AIR)
        {
          throw new Exception();
        }
      }
      catch (Exception e)
      {
        MessageUtil.noArg(sender, Prefix.NO_MATERIAL_ITEM, args[2]);
        return true;
      }
      int durability = 0;
      boolean ignoreDurability = false;
      if (args.length == 4)
      {
        if (!args[3].equals("*") && !MessageUtil.isInteger(sender, args[3], true))
        {
          return true;
        }
        if (args[3].equals("*"))
        {
          ignoreDurability = true;
        }
        else
        {
          durability = Integer.parseInt(args[3]);
          if (!MessageUtil.checkNumberSize(sender, durability, 0, 32767))
          {
            return true;
          }
        }
      }
      ItemStack compare = new ItemStack(material);
      Damageable duraMeta = (Damageable) compare.getItemMeta();
      duraMeta.setDamage(durability);
      compare.setItemMeta(duraMeta);
      if (Cucumbery.config.getBoolean("use-helpful-lore-feature"))
      {
        String worldName = player.getLocation().getWorld().getName();
        if (!Cucumbery.config.getStringList("no-name-tag-on-item-spawn-worlds").contains(worldName))
        {
          ItemLore.setItemLore(compare);
        }
      }
      int number;
      if (isSpace)
      {
        number = ItemStackUtil.countSpace(player.getInventory(), compare, ignoreDurability);
      }
      else
      {
        number = ItemStackUtil.countItem(player.getInventory(), compare, ignoreDurability);
      }
      String name = ItemNameUtil.itemName(compare).toString();
      String msg = "&e" + SenderComponentUtil.senderComponent(player) + ((isSpace) ? "&r의 인벤토리에는 " : "&r은(는) 인벤토리에 ") + "&e" + name + "&r" + (ignoreDurability ?
              "" :
              ("(내구도 : &e" + durability + "&r)")) + ((isSpace) ?
              MessageUtil.getFinalConsonant(name, MessageUtil.ConsonantType.이가) :
              MessageUtil.getFinalConsonant(name, MessageUtil.ConsonantType.을를)) + " &e" + number + "개 &r" + ((isSpace) ? "들어갈 수 있는 공간이 있습니다" : "가지고 있습니다");
      if (!(sender instanceof Player))
      {
        MessageUtil.info(sender, msg);
      }
      else
      {
        MessageUtil.info(sender, msg, compare);
      }
    }
    else
    {
      MessageUtil.longArg(sender, 4, args);
      MessageUtil.commandInfo(sender, label, usage);
    }
    return true;
  }

  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(true, args), true))
    {
      return Collections.singletonList(args[0]);
    }
    int length = args.length;

    if (length == 1)
    {
      return Method.tabCompleterPlayer(sender, args);
    }
    else if (length == 2)
    {
      return Method.tabCompleterList(args, "<인수>", "space", "amount");
    }
    else if (length == 3)
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
    else if (length == 4)
    {
      return Method.tabCompleterIntegerRadius(args, 0, 32767, "[내구도 조건]", "*");
    }

    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}

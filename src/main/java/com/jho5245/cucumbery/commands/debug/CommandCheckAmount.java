package com.jho5245.cucumbery.commands.debug;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.no_groups.*;

import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandCheckAmount implements CucumberyCommandExecutor
{
  @Override
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
      if (Method.usingLoreFeature(player))
      {
        ItemLore.setItemLore(compare);
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
      MessageUtil.info(sender, isSpace ? "%s의 인벤토리에는 %s이(가) %s개 들어갈 수 있는 공간이 있습니다" : "%s의 인벤토리에는 %s이(가) %s개 있습니다", player, compare, number);
    }
    else
    {
      MessageUtil.longArg(sender, 4, args);
      MessageUtil.commandInfo(sender, label, usage);
    }
    return true;
  }

  @Override
  public @NotNull List<Completion> completion(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args, @NotNull Location location)
  {
    int length = args.length;
    if (length == 1)
    {
      return CommandTabUtil.tabCompleterPlayer(sender, args, "<플레이어>");
    }
    if (length == 2)
    {
      return CommandTabUtil.tabCompleterList(args, "<인수>", false, "space", "amount");
    }
    if (length == 3)
    {
      return CommandTabUtil.tabCompleterList(args, Material.values(), "<아이템>", material -> (material.isAir() || !material.isItem()));
    }
    if (length == 4)
    {
      List<Completion> list1 = CommandTabUtil.tabCompleterIntegerRadius(args, 0, 32767, "[내구도 조건]"),
              list2 = CommandTabUtil.tabCompleterList(args, "[인수]", false, "*");
      return CommandTabUtil.sortError(list1, list2);
    }
    return CommandTabUtil.ARGS_LONG;
  }
}

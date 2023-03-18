package com.jho5245.cucumbery.commands.no_groups;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.util.no_groups.*;
import com.jho5245.cucumbery.util.additemmanager.AddItemUtil;

import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandHandGive implements CommandExecutor, AsyncTabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_HANDGIVE, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    String usage = cmd.getUsage().replace("/<command> ", "");
    if (!(sender instanceof Player))
    {
      MessageUtil.sendError(sender, Prefix.ONLY_PLAYER);
      return true;
    }
    if (args.length < 1)
    {
      MessageUtil.shortArg(sender, 1, args);
      MessageUtil.commandInfo(sender, label, usage);
      return true;
    }
    else if (args.length <= 3)
    {
      Player player = (Player) sender;
      List<Player> targets = SelectorUtil.getPlayers(sender, args[0]);
      if (targets == null)
      {
        return true;
      }
      ItemStack item = player.getInventory().getItemInMainHand();
      if (!ItemStackUtil.itemExists(item))
      {
        MessageUtil.sendError(sender, Prefix.NO_HOLDING_ITEM);
        return true;
      }
      item = item.clone();
      int amount = item.getAmount();
      if (args.length >= 2)
      {
        if (!MessageUtil.isInteger(sender, args[1], true))
        {
          return true;
        }
        int input = Integer.parseInt(args[1]);
        if (!MessageUtil.checkNumberSize(sender, input, 1, 2304))
        {
          return true;
        }
        amount = input;
        item.setAmount(amount);
      }
      boolean hideOutput = false;
      if (args.length == 3)
      {
        if (!args[2].equals("true") && !args[2].equals("false"))
        {
          MessageUtil.wrongBool(sender, 3, args);
          return true;
        }
        if (args[2].equals("true"))
        {
          hideOutput = true;
        }
      }
      AddItemUtil.addItemResult2(sender, targets, item, amount).stash().sendFeedback(hideOutput);
    }
    else
    {
      MessageUtil.longArg(sender, 3, args);
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
      return Method.tabCompleterPlayer(sender, args, "<플레이어>", true);
    }
    else if (length == 2)
    {
      return Method.tabCompleterIntegerRadius(args, 1, 2304, "[개수]");
    }
    else if (length == 3)
    {
      return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
    }

    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }

  @Override
  public @NotNull List<Completion> completion(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args, @NotNull Location location)
  {
    if (!(sender instanceof Player player))
    {
      return Collections.emptyList();
    }

    int length = args.length;

    ItemStack item = player.getInventory().getItemInMainHand();
    if (!ItemStackUtil.itemExists(item))
    {
      return CommandTabUtil.errorMessage(Prefix.NO_HOLDING_ITEM.toString());
    }
    if (length == 1)
    {
      return CommandTabUtil.tabCompleterPlayer(sender, args, "<플레이어>");
    }
    else if (length == 2)
    {
      return CommandTabUtil.tabCompleterIntegerRadius(args, 1, 2304, "[개수]");
    }
    else if (length == 3)
    {
      return CommandTabUtil.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
    }
    return CommandTabUtil.ARGS_LONG;
  }
}

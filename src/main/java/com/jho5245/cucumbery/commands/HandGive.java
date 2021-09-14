package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.additemmanager.AddItemUtil;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HandGive implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_HANDGIVE, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return sender instanceof Player;
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
      AddItemUtil.addItemResult2(sender, targets, item, amount).sendFeedback(hideOutput);
    }
    else
    {
      MessageUtil.longArg(sender, 3, args);
      MessageUtil.commandInfo(sender, label, usage);
    }

    return true;
  }
}

package com.jho5245.cucumbery.commands.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.CommandTabUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandDelay implements CommandExecutor, TabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_DELAY, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    if (args.length < 1)
    {
      MessageUtil.shortArg(sender, 2, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return true;
    }
    if (!MessageUtil.isInteger(sender, args[0], true))
    {
      return true;
    }
    int delay = Integer.parseInt(args[0]);
    if (!MessageUtil.checkNumberSize(sender, delay, 0, 20 * 60 * 60))
    {
      return true;
    }
    String command = MessageUtil.listToString(" ", 1, args.length, args);
    Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
    {
      String finalCommand = Method.parseCommandString(sender, command);
      Bukkit.dispatchCommand(sender, finalCommand);
    }, delay);
    return true;
  }

  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    int length = args.length;
    if (length == 1)
    {
      return Method.tabCompleterIntegerRadius(args, 0, 20 * 60 * 60, "<딜레이(틱)>");
    }
    else
    {
      return CommandTabUtil.getCommandsTabCompleter(sender, args, 2, true);
    }
  }
}

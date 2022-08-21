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

public class CommandRepeat implements CommandExecutor, TabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_REPEAT, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    if (args.length < 2)
    {
      MessageUtil.shortArg(sender, 3, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return true;
    }
    if (!MessageUtil.isInteger(sender, args[0], true))
    {
      return true;
    }
    if (!MessageUtil.isInteger(sender, args[1], true))
    {
      return true;
    }
    int repeat = Integer.parseInt(args[0]);
    if (!MessageUtil.checkNumberSize(sender, repeat, 1, 10000))
    {
      return true;
    }
    int delay = Integer.parseInt(args[1]);
    if (!MessageUtil.checkNumberSize(sender, delay, 0, 20 * 60 * 60))
    {
      return true;
    }
    String command = MessageUtil.listToString(" ", 2, args.length, args);
    final String originalCommand = command;
    if (delay == 0)
    {
      for (int i = 1; i <= repeat; i++)
      {
        command = originalCommand;
        command = command.replace("%repeat%", i + "");
        command = Method.parseCommandString(sender, command);
        Bukkit.dispatchCommand(sender, command);
      }
    }
    else
    {
      for (int i = 1; i <= repeat; i++)
      {
        command = originalCommand;
        command = command.replace("%repeat%", i + "");
        String finalCommand = Method.parseCommandString(sender, command);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
        {
          Bukkit.dispatchCommand(sender, finalCommand);
        }, (long) (i - 1) * (long) delay);
      }
    }
    return true;
  }
  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    int length = args.length;
    if (length == 1)
    {
      return Method.tabCompleterIntegerRadius(args, 1, 10000, "<반복 횟수>");
    }
    else if (length == 2)
    {
      return Method.tabCompleterIntegerRadius(args, 0, 20 * 60 * 60, "<딜레이(틱)>");
    }
    else
    {
      return CommandTabUtil.getCommandsTabCompleter(sender, args, 3, true);
    }
  }
}

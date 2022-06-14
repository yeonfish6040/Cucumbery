package com.jho5245.cucumbery.commands.no_groups;

import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.PlaceHolderUtil;
import com.jho5245.cucumbery.util.no_groups.CommandTabUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandConsoleSudo implements CommandExecutor, TabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
		if (!Method.hasPermission(sender, Permission.CMD_CONSOLE_SUDO, true))
		{
			return true;
		}
    if (args.length == 0)
    {
      MessageUtil.shortArg(sender, 1, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return true;
    }
    String command = MessageUtil.listToString(" ", args);
    if (!command.contains("--noph"))
    {
      command = PlaceHolderUtil.placeholder(sender, command, null);
    }
    else
    {
      command = command.replaceFirst("--noph", "");
    }
    if (!command.contains("--noeval"))
    {
      command = PlaceHolderUtil.evalString(command);
    }
    else
    {
      command = command.replaceFirst("--noeval", "");
    }
    command = MessageUtil.n2s(command, MessageUtil.N2SType.SPECIAL);
    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
    return true;
  }
  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(true, args), true))
    {
      return Collections.singletonList(args[0]);
    }
    int length = args.length;

    return CommandTabUtil.getCommandsTabCompleter(sender, args, 1, false);
  }
}

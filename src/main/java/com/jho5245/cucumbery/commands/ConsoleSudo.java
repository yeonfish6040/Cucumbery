package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.PlaceHolderUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ConsoleSudo implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
		if (!Method.hasPermission(sender, Permission.CMD_CONSOLE_SUDO, true))
		{
			return true;
		}
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return sender instanceof Player;
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
}

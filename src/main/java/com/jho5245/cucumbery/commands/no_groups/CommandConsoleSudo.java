package com.jho5245.cucumbery.commands.no_groups;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.util.no_groups.*;

import com.jho5245.cucumbery.util.storage.data.Permission;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandConsoleSudo implements CucumberyCommandExecutor
{
  @Override
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

  @Override
  public @NotNull List<Completion> completion(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args, @NotNull Location location)
  {
    return CommandTabUtil.getCommandsTabCompleter2(sender, args, 1, false);
  }
}

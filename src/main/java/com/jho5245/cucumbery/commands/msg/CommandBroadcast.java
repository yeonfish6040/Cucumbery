package com.jho5245.cucumbery.commands.msg;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.util.no_groups.CommandTabUtil;
import com.jho5245.cucumbery.util.no_groups.CucumberyCommandExecutor;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.data.Permission;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandBroadcast implements CucumberyCommandExecutor
{
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_BROADCAST, true))
    {
      return true;
    }
    String usage = cmd.getUsage().replace("/<command> ", "");
    if (args.length == 0)
    {
      MessageUtil.shortArg(sender, 1, args);
      MessageUtil.commandInfo(sender, label, usage);
      return true;
    }
    String msg = MessageUtil.listToString(" ", args);
    if (msg.endsWith("--noconsole"))
    {
      MessageUtil.broadcastPlayer(false, msg.substring(0, msg.length() - 11));
    }
    else if (msg.endsWith("--noplayer"))
    {
      MessageUtil.consoleSendMessage(false, msg.substring(0, msg.length() - 10));
    }
    else
    {
      MessageUtil.broadcastPlayer(false, msg);
      MessageUtil.consoleSendMessage(false, msg);
    }
    return true;
  }

  @Override
  public @NotNull List<Completion> completion(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args, @NotNull Location location)
  {
    int length = args.length;
    return CommandTabUtil.tabCompleterList(args, length == 1 ? "<메시지>" : "[메시지]", true);
  }
}

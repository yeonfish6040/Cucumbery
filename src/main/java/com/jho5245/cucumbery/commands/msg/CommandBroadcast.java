package com.jho5245.cucumbery.commands.msg;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.data.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandBroadcast implements CommandExecutor, TabCompleter
{
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
    msg = Method.parseCommandString(sender, msg);
    if (msg.endsWith("--noconsole"))
    {
      MessageUtil.broadcastPlayer(false, msg.substring(0, msg.length() - 11));
    }
    else
    {
      MessageUtil.broadcastPlayer(false, msg);
      MessageUtil.consoleSendMessage(false, msg);
    }
    return true;
  }

  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    int length = args.length;
    return Method.tabCompleterList(args, length == 1 ? "<메시지>" : "[메시지]", true);
  }
}

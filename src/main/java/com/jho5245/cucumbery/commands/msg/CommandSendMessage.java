package com.jho5245.cucumbery.commands.msg;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandSendMessage implements CommandExecutor, TabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
		if (!Method.hasPermission(sender, Permission.CMD_SENDMESSAGE, true))
		{
			return true;
		}
    String usage = cmd.getUsage().replace("/<command> ", "");
    if (args.length < 2)
    {
      MessageUtil.shortArg(sender, 2, args);
      MessageUtil.commandInfo(sender, label, usage);
      return true;
    }
    Player target = SelectorUtil.getPlayer(sender, args[0]);
		if (target == null)
		{
			return true;
		}
    String msg = MessageUtil.listToString(" ", 1, args.length, args);
    msg = Method.parseCommandString(target, msg);
    target.sendMessage(msg);
    return true;
  }

  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    int length = args.length;
    if (length == 1)
    {
      return Method.tabCompleterPlayer(sender, args);
    }
    else
    {
      return Method.tabCompleterList(args, length == 2 ? "<메시지>" : "[메시지]", true);
    }
  }
}

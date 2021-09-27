package com.jho5245.cucumbery.commands.debug;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandCalcDistance implements CommandExecutor, TabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
		if (!Method.hasPermission(sender, Permission.CMD_CALC_DISTANCE, true))
		{
			return true;
		}
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    String usage = cmd.getUsage().replace("/<command> ", ""), consoleUsage = usage.replace("[다른 플레이어 ID]", "<다른 플레이어 ID>");
    if (args.length == 0)
    {
      if (sender instanceof Player)
      {
        MessageUtil.shortArg(sender, 1, args);
        MessageUtil.commandInfo(sender, label, usage);
        return true;
      }
      MessageUtil.shortArg(sender, 2, args);
      MessageUtil.commandInfo(sender, label, consoleUsage);
      return true;
    }
    else if (args.length <= 3)
    {
      if (args.length == 1 && !(sender instanceof Player))
      {
        MessageUtil.shortArg(sender, 2, args);
        MessageUtil.commandInfo(sender, label, consoleUsage);
        return true;
      }
      Player player, target;
      if (args.length == 1)
      {
        player = (Player) sender;
        target = SelectorUtil.getPlayer(sender, args[0]);
      }
      else
      {
        player = SelectorUtil.getPlayer(sender, args[0]);
				if (player == null)
				{
					return true;
				}
        target = SelectorUtil.getPlayer(sender, args[1]);
      }
			if (target == null)
			{
				return true;
			}
      if (player == target)
      {
        MessageUtil.sendError(sender, "같은 플레이어의 거리는 측정할 수 없습니다.");
        return true;
      }
      if (args.length == 3 && !args[2].equals("true") && !args[2].equals("false"))
      {
        MessageUtil.wrongArg(sender, 3, args);
        return true;
      }
      double distance = player.getLocation().distance(target.getLocation());
      MessageUtil.info(sender, "&e" + player.getName() + "&r와(과) &e" + target.getName() + "&r와(과)의 거리는 &e" + Constant.Sosu4.format(distance) + "m&r입니다.");
    }
    else
    {
      MessageUtil.longArg(sender, 3, args);
      if (sender instanceof Player)
      {
        MessageUtil.commandInfo(sender, label, usage);
        return true;
      }
      MessageUtil.commandInfo(sender, label, consoleUsage);
      return true;
    }
    return true;
  }


  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(true, args), true))
    {
      return Collections.singletonList(args[0]);
    }
    int length = args.length;

    if (length == 1)
    {
      return Method.tabCompleterPlayer(sender, args);
    }
    else if (length == 2)
    {
      return Method.tabCompleterPlayer(sender, args);
    }
    else if (length == 3)
    {
      return Method.tabCompleterBoolean(args, "[다른 월드여도 좌표 거리만 측정]");
    }

    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}

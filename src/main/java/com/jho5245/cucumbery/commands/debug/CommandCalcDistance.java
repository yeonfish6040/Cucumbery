package com.jho5245.cucumbery.commands.debug;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.util.no_groups.*;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandCalcDistance implements CucumberyCommandExecutor
{
  @Override
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
    String usage = cmd.getUsage().replace("/<command> ", ""), consoleUsage = usage.replace("[다른 개체]", "<다른 개체>");
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
      Entity player, target;
      if (args.length == 1)
      {
        player = (Player) sender;
        target = SelectorUtil.getEntity(sender, args[0]);
      }
      else
      {
        player = SelectorUtil.getEntity(sender, args[0]);
				if (player == null)
				{
					return true;
				}
        target = SelectorUtil.getEntity(sender, args[1]);
      }
			if (target == null)
			{
				return true;
			}
      if (player == target)
      {
        MessageUtil.sendError(sender, "같은 개체의 거리는 측정할 수 없습니다");
        return true;
      }
      if (args.length == 3 && !args[2].equals("true") && !args[2].equals("false"))
      {
        MessageUtil.wrongArg(sender, 3, args);
        return true;
      }
      boolean ignoreWorld = args.length == 3 && args[2].equals("true");
      double distance = Method2.distance(player.getLocation(), target.getLocation(), ignoreWorld);
      MessageUtil.info(sender, "%s와(과) %s의 거리는 %s입니다", player, target, Constant.THE_COLOR_HEX + Constant.Sosu4.format(distance) + "m");
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

  @Override
  public @NotNull List<Completion> completion(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args, @NotNull Location location)
  {
    int length = args.length;
    if (length == 1)
    {
      return CommandTabUtil.tabCompleterPlayer(sender, args, "<플레이어>");
    }
    else if (length == 2)
    {
      return CommandTabUtil.tabCompleterPlayer(sender, args, "[다른 플레이어]");
    }
    else if (length == 3)
    {
      return CommandTabUtil.tabCompleterBoolean(args, "[다른 월드여도 좌표 거리만 측정]");
    }
    return CommandTabUtil.ARGS_LONG;
  }
}

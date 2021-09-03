package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RemoveBedSpawnLocation implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_REMOVE_BED_SPAWN_LOCATION, true))
    {
      return true;
    }
    if (args.length == 0)
    {
      MessageUtil.shortArg(sender, 1, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return true;
    }
    else if (args.length <= 2)
    {
      if (args.length == 2 && !MessageUtil.isBoolean(sender, args, 2, true))
      {
        return true;
      }
      Player target = SelectorUtil.getPlayer(sender, args[0]);
      if (target == null)
      {
        return true;
      }
      if (target.getBedSpawnLocation() == null)
      {
        if (args.length == 2 && args[1].equals("true"))
        {
          return true;
        }
        MessageUtil.sendError(sender, target, "의 스폰 포인트가 없습니다.");
        return true;
      }
      target.setBedSpawnLocation(null);
      if (args.length != 2 || !args[1].equals("true"))
      {
        if (!target.equals(sender))
        {
          MessageUtil.info(target,  sender, "이 당신의 스폰 포인트를 제거하였습니다.");
        }
        MessageUtil.info(sender,  target, "의 스폰 포인트를 제거하였습니다.");
      }
    }
    else
    {
      MessageUtil.longArg(sender, 2, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return true;
    }
    return true;
  }
}
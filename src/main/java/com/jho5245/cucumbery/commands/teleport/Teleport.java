package com.jho5245.cucumbery.commands.teleport;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.data.Permission;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Teleport implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_TELEPORT, true))
    {
      return true;
    }
    if (args.length == 0)
    {
      if (sender instanceof Player)
      {
        MessageUtil.shortArg(sender, 2, args);
        MessageUtil.commandInfo(sender, label, "<플레이어 ID> <다른 플레이어 ID>");
      }
      else
      {
        MessageUtil.shortArg(sender, 1, args);
        MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      }
      return true;
    }
    else if (args.length <= 2)
    {
      Player player, target;
      if (args.length == 1)
      {
        if (!(sender instanceof Player))
        {
          MessageUtil.shortArg(sender, 2, args);
          MessageUtil.commandInfo(sender, label, "<플레이어 ID> <다른 플레이어 ID>");
          return true;
        }
        player = (Player) sender;
        target = Method.getPlayer(sender, args[0]);
      }
      else
      {
        player = Method.getPlayer(sender, args[0]);
        if (player == null)
        {
          return true;
        }
        target = Method.getPlayer(sender, args[1]);
      }
      if (target == null)
      {
        return true;
      }
      if (player.getGameMode() == GameMode.SPECTATOR)
      {
        player.setSpectatorTarget(null);
      }
      player.teleport(target);
    }
    else
    {
      MessageUtil.longArg(sender, 3, args);
      if (sender instanceof Player)
      {
        MessageUtil.commandInfo(sender, label, "<플레이어 ID> <다른 플레이어 ID>");
      }
      else
      {
        MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      }
    }
    return true;
  }
}

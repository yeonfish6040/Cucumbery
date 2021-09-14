package com.jho5245.cucumbery.commands.teleport;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Teleport implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_TELEPORT, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return sender instanceof Player;
    }
    if (args.length == 0)
    {
      if (sender instanceof Player)
      {
        MessageUtil.shortArg(sender, 2, args);
        MessageUtil.commandInfo(sender, label, " <개체> <다른 개체>");
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
      Entity entity = null, target;
      List<Entity> entities = null;
      if (args.length == 1)
      {
        if (!(sender instanceof Entity entity1))
        {
          MessageUtil.shortArg(sender, 2, args);
          MessageUtil.commandInfo(sender, label, "<개체> <다른 개체>");
          return true;
        }
        entity = entity1;
        target = SelectorUtil.getEntity(sender, args[0]);
      }
      else
      {
        entities = SelectorUtil.getEntities(sender, args[0]);
        if (entities == null)
        {
          return true;
        }
        target = SelectorUtil.getEntity(sender, args[1]);
      }
      if (target == null)
      {
        return true;
      }
      if (entity instanceof Player player && player.getGameMode() == GameMode.SPECTATOR)
      {
        player.setSpectatorTarget(null);
      }
      if (entity != null)
      {
        entity.teleport(target.getLocation());
      }
      if (entities != null)
      {
        for (Entity e : entities)
        {
          if (e instanceof Player player && player.getGameMode() == GameMode.SPECTATOR)
          {
            player.setSpectatorTarget(null);
          }
          e.teleport(target.getLocation());
        }
      }
    }
    else
    {
      MessageUtil.longArg(sender, 2, args);
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

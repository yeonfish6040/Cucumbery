package com.jho5245.cucumbery.commands.teleport;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.util.no_groups.*;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandTeleport implements CommandExecutor, AsyncTabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_TELEPORT, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
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

  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(true, args), true))
    {
      return Collections.singletonList(args[0]);
    }
    int length = args.length;
    if (length == 1)
    {
      List<String> list = new ArrayList<>(Method.tabCompleterEntity(sender, args, "<개체>"));
      list.addAll(Method.tabCompleterEntity(sender, args, "<여러 개체> <다른 개체>", true));
      return list;
    }
    else if (length == 2)
    {
      return Method.tabCompleterEntity(sender, args, "<다른 개체>");
    }
    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }

  @NotNull
  public List<Completion> completion(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args, @NotNull Location location)
  {
    int length = args.length;
    if (length == 1)
    {
      return CommandTabUtil.tabCompleterEntity(sender, args, "<개체>");
    }
    else if (length == 2)
    {
      return CommandTabUtil.tabCompleterEntity(sender, args, "[다른 개체]");
    }
    return CommandTabUtil.ARGS_LONG;
  }
}

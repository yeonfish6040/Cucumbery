package com.jho5245.cucumbery.commands.debug;

import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandGetPositions implements CommandExecutor, TabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_GETPOSITIONS, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    String usage = cmd.getUsage().replace("/<command> ", "");
    if (args.length <= 1)
    {
      boolean worldSpecified = false;
      World world = null;
      if (args.length == 1)
      {
        world = Bukkit.getServer().getWorld(args[0]);
        if (world == null)
        {
          MessageUtil.noArg(sender, Prefix.NO_WORLD, args[0]);
          return true;
        }
        worldSpecified = true;
        if (world.getPlayers().isEmpty())
        {
          MessageUtil.info(sender, "해당 월드에는 플레이어가 존재하지 않습니다");
          return true;
        }
      }
      MessageUtil.info(sender, "&6--------------------&c플레이어 좌표 목록&6--------------------");
      for (Player player : Bukkit.getServer().getOnlinePlayers())
      {
        Location loc = player.getLocation();
        World playerWorld = loc.getWorld();
        if (worldSpecified && !playerWorld.equals(world))
        {
          continue;
        }
        MessageUtil.info(sender, ComponentUtil.translate("%s : %s", player, loc));
      }
    }
    else
    {
      MessageUtil.longArg(sender, 2, args);
      MessageUtil.commandInfo(sender, label, usage);
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
      return Method.tabCompleterList(args, Method.listWorlds(), "[월드]");
    }
    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}

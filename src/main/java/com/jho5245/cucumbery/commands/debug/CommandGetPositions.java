package com.jho5245.cucumbery.commands.debug;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.util.no_groups.*;
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

public class CommandGetPositions implements CommandExecutor, AsyncTabCompleter
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
        world = CommandArgumentUtil.world(sender, args[0], true);
        if (world == null)
        {
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

  /**
   * Requests a list of possible completions for a command argument.
   *
   * @param sender   Source of the command.  For players tab-completing a
   *                 command inside a command block, this will be the player, not
   *                 the command block.
   * @param cmd      the command to be executed.
   * @param label    Alias of the command which was used
   * @param args     The arguments passed to the command, including final
   *                 partial argument to be completed
   * @param location The location of this command was executed.
   * @return A List of possible completions for the final argument, or an empty list.
   */
  @Override
  public @NotNull List<Completion> completion(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args, @NotNull Location location)
  {
    int length = args.length;
    if (length == 1)
    {
      return CommandTabUtil.worldArgument(sender, args, "[월드]");
    }
    return Collections.singletonList(CommandTabUtil.ARGS_LONG);
  }
}

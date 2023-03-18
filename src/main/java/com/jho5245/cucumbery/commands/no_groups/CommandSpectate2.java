package com.jho5245.cucumbery.commands.no_groups;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.util.no_groups.*;

import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
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
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandSpectate2 implements CommandExecutor, AsyncTabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_SPECTATE, true))
    {
      return !(sender instanceof BlockCommandSender);
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    String usage = cmd.getUsage().replace("/<command> ", ""), consoleUsage = usage.replace("[다른 플레이어 ID]", "<다른 플레이어 ID>");
    if (args.length == 0)
    {
      if (!(sender instanceof Player))
      {
        MessageUtil.shortArg(sender, 2, args);
        MessageUtil.commandInfo(sender, label, consoleUsage);
        return !(sender instanceof BlockCommandSender);
      }
      MessageUtil.shortArg(sender, 1, args);
      MessageUtil.commandInfo(sender, label, usage);
      return !(sender instanceof BlockCommandSender);
    }
    else if (args.length <= 5)
    {
      if (args.length == 1)
      {
        if (!(sender instanceof Player player))
        {
          MessageUtil.shortArg(sender, 2, args);
          MessageUtil.commandInfo(sender, label, consoleUsage);
          return !(sender instanceof BlockCommandSender);
        }
        Entity target = SelectorUtil.getEntity(sender, args[0]);
        if (target == null)
        {
          return !(sender instanceof BlockCommandSender);
        }
        boolean success = spectate(sender, Collections.singletonList(player), target, true, false, false);
        return success || !(sender instanceof BlockCommandSender);
      }
      List<Player> spectators = SelectorUtil.getPlayers(sender, args[0]);
      if (spectators == null)
      {
        return !(sender instanceof BlockCommandSender);
      }
      Entity target = SelectorUtil.getEntity(sender, args[1]);
      if (target == null)
      {
        return !(sender instanceof BlockCommandSender);
      }

      if (!MessageUtil.isBoolean(sender, args, 3, true))
      {
        return !(sender instanceof BlockCommandSender);
      }
      if (!MessageUtil.isBoolean(sender, args, 4, true))
      {
        return !(sender instanceof BlockCommandSender);
      }
      if (!MessageUtil.isBoolean(sender, args, 5, true))
      {
        return !(sender instanceof BlockCommandSender);
      }
      boolean changeGameMode = args.length >= 3 && args[2].equals("true");
      boolean bypassPermission = args.length >= 4 && args[3].equals("true");
      boolean hideOutput = args.length >= 5 && args[4].equals("true");

      boolean success = spectate(sender, spectators, target, changeGameMode, bypassPermission, hideOutput);
      return success || !(sender instanceof BlockCommandSender);
    }
    else
    {
      MessageUtil.longArg(sender, 5, args);
      if (!(sender instanceof Player))
      {
        MessageUtil.commandInfo(sender, label, consoleUsage);
        return !(sender instanceof BlockCommandSender);
      }
      MessageUtil.commandInfo(sender, label, usage);
      return !(sender instanceof BlockCommandSender);
    }
  }

  public boolean spectate(@NotNull CommandSender sender, @NotNull List<Player> spectators, @NotNull Entity target, boolean changeGameMode, boolean bypassPermission, boolean hideOutput)
  {
    List<Player> successPlayers = new ArrayList<>();
    for (Player spectator : spectators)
    {
      if (spectator.equals(target))
      {
        continue;
      }
      try
      {
        if (!(!bypassPermission && Permission.EVENT2_ANTI_SPECTATE.has(target) && spectators.size() == 1 && !Permission.EVENT2_ANTI_SPECTATE_BYPASS.has(spectators.get(0))
                && !Permission.EVENT2_ANTI_SPECTATE_BYPASS.has(sender)))
        {
          if (changeGameMode)
          {
            spectator.setGameMode(GameMode.SPECTATOR);
          }
          if (spectator.getGameMode() != GameMode.SPECTATOR)
          {
            continue;
          }
          spectator.teleport(target);
          spectator.setSpectatorTarget(target);
        }
        if (target.equals(spectator.getSpectatorTarget()))
        {
          successPlayers.add(spectator);
        }
      }
      catch (Exception ignored)
      {

      }
    }

    if (!hideOutput)
    {
      List<Player> failurePlayers = new ArrayList<>(spectators);
      failurePlayers.removeAll(successPlayers);
      if (!failurePlayers.isEmpty())
      {
        MessageUtil.sendWarnOrError(successPlayers.isEmpty(), sender, ComponentUtil.translate("%s은(는) %s을(를) 관전시킬 수 없습니다", failurePlayers, target));
      }
      if (!successPlayers.isEmpty())
      {
        if (spectators.size() == 1 && sender.equals(successPlayers.get(0)))
        {
          MessageUtil.info(sender, ComponentUtil.translate("%s을(를) 관전합니다", target));
          MessageUtil.info(target, ComponentUtil.translate("%s이(가) 당신을 관전합니다", sender));
          MessageUtil.sendAdminMessage(sender, Collections.singletonList(target), "%s을(를) 관전합니다", target);
        }
        else
        {
          List<Permissible> exceptions = new ArrayList<>(successPlayers);
          exceptions.add(target);
          MessageUtil.info(sender, ComponentUtil.translate("%s에게 %s을(를) 관전시킵니다", successPlayers, target));
          MessageUtil.info(successPlayers, ComponentUtil.translate("%s이(가) 당신에게 %s을(를) 관전시켰습니다", sender, target));
          MessageUtil.info(target, ComponentUtil.translate("%s에 의해 %s이(가) 당신을 관전합니다", sender, successPlayers));
          MessageUtil.sendAdminMessage(sender, exceptions, "%s에게 %s을(를) 관전시켰습니다", successPlayers, target);
        }
      }
    }

    return !successPlayers.isEmpty();
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
      List<String> list = new ArrayList<>(Method.tabCompleterEntity(sender, args, "<관전 개체>"));
      list.addAll(Method.tabCompleterPlayer(sender, args, "<관전자> <관전 개체> [게임 모드 자동 변경]", true));
      return list;
    }
    else if (length == 2)
    {
      return Method.tabCompleterEntity(sender, args, "<관전 개체> [게임모드 자동 변경]");
    }
    else if (length == 3)
    {
      return Method.tabCompleterBoolean(args, "[게임모드 자동 변경]");
    }
    else if (length == 4)
    {
      return Method.tabCompleterBoolean(args, "[권한 부족 우회]");
    }
    else if (length == 5)
    {
      return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
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
  {    int length = args.length;

    if (length == 1)
    {
      List<Completion> list = CommandTabUtil.tabCompleterEntity(sender, args, "<관전 개체>"),
      list1 = CommandTabUtil.tabCompleterPlayer(sender, args, "<관전자>");
      return CommandTabUtil.sortError(list, list1);
    }
    else if (length == 2)
    {
      return CommandTabUtil.tabCompleterEntity(sender, args, "<관전 개체>");
    }
    else if (length == 3)
    {
      return CommandTabUtil.tabCompleterBoolean(args, "[게임모드 자동 변경]");
    }
    else if (length == 4)
    {
      return CommandTabUtil.tabCompleterBoolean(args, "[권한 부족 우회]");
    }
    else if (length == 5)
    {
      return CommandTabUtil.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
    }

    return CommandTabUtil.ARGS_LONG;
  }
}

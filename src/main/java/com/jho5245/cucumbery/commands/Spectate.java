package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Spectate implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_SPECTATE, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return sender instanceof Player;
    }
    String usage = cmd.getUsage().replace("/<command> ", ""), consoleUsage = usage.replace("[다른 플레이어 ID]", "<다른 플레이어 ID>");
    if (args.length == 0)
    {
      if (!(sender instanceof Player))
      {
        MessageUtil.shortArg(sender, 2, args);
        MessageUtil.commandInfo(sender, label, consoleUsage);
        return true;
      }
      MessageUtil.shortArg(sender, 1, args);
      MessageUtil.commandInfo(sender, label, usage);
      return true;
    }
    else if (args.length <= 5)
    {
      if (args.length == 1)
      {
        if (!(sender instanceof Player player))
        {
          MessageUtil.shortArg(sender, 2, args);
          MessageUtil.commandInfo(sender, label, consoleUsage);
          return true;
        }
        Player target = SelectorUtil.getPlayer(sender, args[0]);
        if (target == null)
        {
          return true;
        }
        if (player == target)
        {
          MessageUtil.sendError(player, "자기 자신은 관전할 수 없습니다.");
          return true;
        }
        if (Permission.EVENT2_ANTI_SPECTATE.has(target) && !Permission.EVENT2_ANTI_SPECTATE_BYPASS.has(player))
        {
          MessageUtil.sendError(player, "관전할 수 없는 플레이어입니다.");
          return true;
        }
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(target);
        player.setSpectatorTarget(target);

        MessageUtil.info(player, target, "을(를) 관전합니다.");
        MessageUtil.info(target, player, "이(가) 당신을 관전합니다.");
        return true;
      }
      Player target = SelectorUtil.getPlayer(sender, args[0]);
      if (target == null)
      {
        return true;
      }
      Player target2 = SelectorUtil.getPlayer(sender, args[1]);
      if (target2 == null)
      {
        return true;
      }
      if (target == target2)
      {
        MessageUtil.sendError(sender, "같은 사람을 관전시킬 수 없습니다.");
        return true;
      }
      boolean changeGameMode = false;
      if (args.length >= 3)
      {
        if (!args[2].equals("true") && !args[2].equals("false"))
        {
          MessageUtil.wrongBool(sender, 3, args);
          return true;
        }
        if (args[2].equals("true"))
        {
          changeGameMode = true;
        }
      }
      if (!changeGameMode && target.getGameMode() != GameMode.SPECTATOR)
      {
        MessageUtil.sendError(sender, target, "이(가) 관전모드가 아니여서 ", target2, "&(를) 관전시킬 수 없습니다.");
        return true;
      }
      boolean bypassPerm = false;
      if (args.length >= 4)
      {
        if (!args[3].equals("true") && !args[3].equals("false"))
        {
          MessageUtil.wrongBool(sender, 4, args);
          return true;
        }
        if (args[3].equals("true"))
        {
          bypassPerm = true;
        }
      }
      if (!bypassPerm && Permission.EVENT2_ANTI_SPECTATE.has(target2) && !Permission.EVENT2_ANTI_SPECTATE_BYPASS.has(target)
              && !Permission.EVENT2_ANTI_SPECTATE_BYPASS.has(sender))
      {
        MessageUtil.sendError(sender, target2, "은(는) 관전할 수 없는 플레이어입니다.");
        return true;
      }
      target.setGameMode(GameMode.SPECTATOR);
      target.teleport(target2);
      target.setSpectatorTarget(target2);
      boolean hideOutput = false;
      if (args.length == 5)
      {
        if (!args[4].equals("true") && !args[4].equals("false"))
        {
          MessageUtil.wrongBool(sender, 5, args);
          return true;
        }
        if (args[4].equals("true"))
        {
          hideOutput = true;
        }
      }
      if (!hideOutput)
      {
        MessageUtil.info(sender, target, "에게 ", target2, "을(를) 관전시킵니다.");
        MessageUtil.info(target, sender, "이 당신에게 " + target2, "을(를) 관전시켰습니다.");
        MessageUtil.info(target2, sender, "에 의해 " + target, "이(가) 당신을 관전합니다.");
      }
    }
    else
    {
      MessageUtil.longArg(sender, 5, args);
      if (!(sender instanceof Player))
      {
        MessageUtil.commandInfo(sender, label, consoleUsage);
        return true;
      }
      MessageUtil.commandInfo(sender, label, usage);
      return true;
    }
    return true;
  }
}

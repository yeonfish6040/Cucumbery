package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandRespawn implements CommandExecutor, TabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_RESPAWN, true))
    {
      return !(sender instanceof BlockCommandSender);
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    if (args.length == 0)
    {
      MessageUtil.shortArg(sender, 1, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return !(sender instanceof BlockCommandSender);
    }
    else if (args.length <= 2)
    {
      if (args.length == 2 && !MessageUtil.isBoolean(sender, args, 2, true))
      {
        return !(sender instanceof BlockCommandSender);
      }
      List<Player> players = SelectorUtil.getPlayers(sender, args[0]);
      if (players == null)
      {
        return !(sender instanceof BlockCommandSender);
      }
      boolean hideOutput = args.length == 2 && args[1].equals("true");
      List<Player> successPlayers = new ArrayList<>();
      List<Player> failurePlayers = new ArrayList<>();
      for (Player player : players)
      {
        if (player.isDead())
        {
          player.spigot().respawn();
          if (!sender.equals(player) && !hideOutput)
          {
            MessageUtil.info(player, ComponentUtil.translate("%s이(가) 당신을 강제로 리스폰시켰습니다.", sender));
          }
          successPlayers.add(player);
        }
        else
        {
          failurePlayers.add(player);
        }
      }
      if (!hideOutput)
      {
        if (!failurePlayers.isEmpty())
        {
          MessageUtil.sendWarnOrError(successPlayers.isEmpty(), sender, ComponentUtil.translate("%s은(는) 죽어 있는 상태가 아닙니다.", failurePlayers));
        }
        if (!successPlayers.isEmpty())
        {
          MessageUtil.info(sender, ComponentUtil.translate("%s을(를) 강제로 리스폰시켰습니다.", successPlayers));
          MessageUtil.sendAdminMessage(sender, new ArrayList<>(successPlayers), ComponentUtil.translate("[%s: %s을(를) 강제로 리스폰시켰습니다.]", sender, successPlayers));
          return true;
        }
      }
    }
    else
    {
      MessageUtil.longArg(sender, 2, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return !(sender instanceof BlockCommandSender);
    }
    return !(sender instanceof BlockCommandSender);
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
      return Method.tabCompleterPlayer(sender, args, "<플레이어>", true);
    }
    else if (length == 2)
    {
      return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
    }

    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}

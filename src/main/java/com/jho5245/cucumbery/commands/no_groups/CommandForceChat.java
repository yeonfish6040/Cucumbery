package com.jho5245.cucumbery.commands.no_groups;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.util.no_groups.*;

import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandForceChat implements CucumberyCommandExecutor
{
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_FORCECHAT, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    String usage = cmd.getUsage().replace("/<command> ", "");
    if (args.length < 3)
    {
      MessageUtil.shortArg(sender, 2, args);
      MessageUtil.commandInfo(sender, label, usage);
      return true;
    }
    List<Player> players = SelectorUtil.getPlayers(sender, args[0]);
    if (players == null)
    {
      return true;
    }
    final String msg = MessageUtil.listToString(" ", 2, args.length, args);

    boolean isCommand = msg.startsWith("/");
    boolean hideOutput = false;
    if (!args[1].equals("true") && !args[1].equals("false"))
    {
      MessageUtil.wrongBool(sender, 2, args);
      return true;
    }
    if (args[1].equals("true"))
    {
      hideOutput = true;
    }
    for (Player player : players)
    {
      boolean playChatSound = CustomConfig.UserData.LISTEN_COMMAND.getBoolean(player);

      if (isCommand && playChatSound)
      {
        CustomConfig.UserData.LISTEN_COMMAND.set(player, false);
      }

      if (msg.startsWith("op:"))
      {
        boolean isOp = player.isOp();
        String newMsg = msg.substring(3);
        if (!isOp)
        {
          player.setOp(true);
        }
        player.chat(newMsg.replace("§", "&"));
        if (!isOp)
        {
          player.setOp(false);
        }
      }
      else
      {
        player.chat(msg.replace("§", "&"));
      }

      if (isCommand && playChatSound)
      {
        CustomConfig.UserData.LISTEN_COMMAND.set(player, true);
      }
    }
    if (!hideOutput)
    {
      if (!players.equals(sender))
      {
        MessageUtil.info(players, "%s이(가) 당신에게 %s 메시지를 강제로 채팅시켰습니다", sender, Constant.THE_COLOR_HEX + msg);
      }
      MessageUtil.info(sender, "%s에게 %s 메시지를 강제로 채팅시켰습니다", players, Constant.THE_COLOR_HEX + msg);
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
      return CommandTabUtil.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
    }
    else
    {
      if (args.length == 3)
      {
        return CommandTabUtil.tabCompleterList(args, "<메시지>", true, "<메시지>", "op:");
      }
      return CommandTabUtil.tabCompleterList(args, "[메시지]", true);
    }
  }
}

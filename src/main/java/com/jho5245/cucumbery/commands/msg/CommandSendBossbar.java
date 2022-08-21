package com.jho5245.cucumbery.commands.msg;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.util.no_groups.*;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import net.kyori.adventure.bossbar.BossBar.Color;
import net.kyori.adventure.bossbar.BossBar.Flag;
import net.kyori.adventure.bossbar.BossBar.Overlay;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandSendBossbar implements CucumberyCommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_SEND_TOAST, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    int length = args.length;
    if (length < 2)
    {
      MessageUtil.shortArg(sender, 2, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return !(sender instanceof BlockCommandSender);
    }
    else if (length < 7)
    {
      List<Player> players = SelectorUtil.getPlayers(sender, args[0]);
      if (players == null)
      {
        return !(sender instanceof BlockCommandSender);
      }
      Component message = ComponentUtil.create(args[1]);
      double duration = 20;
      if (length >= 3)
      {
        if (!MessageUtil.isDouble(sender, args[2], true))
        {
          return !(sender instanceof BlockCommandSender);
        }
        duration = Double.parseDouble(args[2]);
        if (!MessageUtil.checkNumberSize(sender, duration, 0.05, 86400d * 7))
        {
          return !(sender instanceof BlockCommandSender);
        }
      }
      Color color = Color.YELLOW;
      if (length >= 4)
      {
        try
        {
          color = Color.valueOf(args[3].toUpperCase());
        }
        catch (Exception e)
        {
          MessageUtil.wrongArg(sender, 4, args);
          return !(sender instanceof BlockCommandSender);
        }
      }
      Overlay overlay = Overlay.PROGRESS;
      if (length >= 5)
      {
        try
        {
          overlay = Overlay.valueOf(args[4].toUpperCase());
        }
        catch (Exception e)
        {
          MessageUtil.wrongArg(sender, 5, args);
          return !(sender instanceof BlockCommandSender);
        }
      }
      Flag flag = null;
      if (length >= 6)
      {
        try
        {
          flag = Flag.valueOf(args[5].toUpperCase());
        }
        catch (Exception e)
        {
          MessageUtil.wrongArg(sender, 6, args);
          return !(sender instanceof BlockCommandSender);
        }
      }
      new BossBarMessage(message, (int) (duration * 20), color, overlay, flag).show(players);
    }
    else
    {
      MessageUtil.longArg(sender, 7, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return !(sender instanceof BlockCommandSender);
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
      return CommandTabUtil.tabCompleterList(args, "<메시지>", true);
    }
    else if (length == 3)
    {
      return CommandTabUtil.tabCompleterDoubleRadius(args, 0.05, 86400d * 7, "[보스바 지속 시간(초)]");
    }
    else if (length == 4)
    {
      return CommandTabUtil.tabCompleterList(args, Color.values(), "[보스바 색상]");
    }
    else if (length == 5)
    {
      return CommandTabUtil.tabCompleterList(args, Overlay.values(), "[보스바 오버레이]");
    }
    else if (length == 6)
    {
      return CommandTabUtil.tabCompleterList(args, Flag.values(), "[보스바 플래그]");
    }
    return CommandTabUtil.ARGS_LONG;
  }
}

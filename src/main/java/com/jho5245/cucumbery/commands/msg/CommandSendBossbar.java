package com.jho5245.cucumbery.commands.msg;

import com.jho5245.cucumbery.util.BossBarMessage;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import net.kyori.adventure.bossbar.BossBar.Color;
import net.kyori.adventure.bossbar.BossBar.Flag;
import net.kyori.adventure.bossbar.BossBar.Overlay;
import net.kyori.adventure.text.Component;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandSendBossbar implements CommandExecutor, TabCompleter
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
        return !(sender instanceof BlockCommandSender);
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

  @NotNull
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
      return Method.tabCompleterList(args, "<메시지>", true);
    }
    else if (length == 3)
    {
      return Method.tabCompleterDoubleRadius(args, 0.05, 86400d * 7, "[보스바 지속 시간(초)]");
    }
    else if (length == 4)
    {
      return Method.tabCompleterList(args, Color.values(), "[보스바 색상]");
    }
    else if (length == 5)
    {
      return Method.tabCompleterList(args, Overlay.values(), "[보스바 오버레이]");
    }
    else if (length == 6)
    {
      return Method.tabCompleterList(args, Flag.values(), "[보스바 플래그]");
    }
    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}

package com.jho5245.cucumbery.commands.teleport;

import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.SelectorUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandSwapTeleport implements CommandExecutor, TabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_SWAP_TELEPORT, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    String usage = cmd.getUsage().replace("/<command> ", ""), consoleUsage = usage.replace("[다른 플레이어 ID]", "<다른 플레이어 ID>");
    if (args.length == 0)
    {
      if (sender instanceof Player)
      {
        MessageUtil.shortArg(sender, 1, args);
        MessageUtil.commandInfo(sender, label, usage);
        return true;
      }
      MessageUtil.shortArg(sender, 2, args);
      MessageUtil.commandInfo(sender, label, consoleUsage);
      return true;
    }
    else if (args.length <= 5)
    {
      if (args.length == 1 && !(sender instanceof Player))
      {
        MessageUtil.shortArg(sender, 2, args);
        MessageUtil.commandInfo(sender, label, consoleUsage);
        return true;
      }
      Entity player, target;
      if (args.length == 1)
      {
        player = (Player) sender;
        target = SelectorUtil.getEntity(sender, args[0]);
      }
      else
      {
        player = SelectorUtil.getEntity(sender, args[0]);
        if (player == null)
        {
          return true;
        }
        target = SelectorUtil.getEntity(sender, args[1]);
      }
      if (target == null)
      {
        return true;
      }
      if (player == target)
      {
        MessageUtil.sendError(sender, "같은 개체의 위치는 서로 맞바꿀 수 없습니다");
        return true;
      }
      if (args.length >= 3 && !MessageUtil.isBoolean(sender, args, 3, true))
      {
        return true;
      }
      if (args.length >= 4 && !MessageUtil.isBoolean(sender, args, 4, true))
      {
        return true;
      }
      if (args.length >= 5 && !MessageUtil.isBoolean(sender, args, 5, true))
      {
        return true;
      }
      boolean preserveKinetic = args.length >= 3 && args[2].equals("true");
      boolean preservePotential = args.length >= 4 && args[3].equals("true");
      boolean hideOutput = args.length >= 5 && args[4].equals("true");
      Location pLoc = player.getLocation(), tLoc = target.getLocation();
      Vector pVector = player.getVelocity().clone(), tVector = target.getVelocity().clone();
      float pFallingDist = player.getFallDistance(), tFallingDist = target.getFallDistance();
      player.teleport(tLoc);
      if (preserveKinetic)
      {
        player.setVelocity(tVector);
      }
      if (preservePotential)
      {
        player.setFallDistance(tFallingDist);
      }
      target.teleport(pLoc);
      if (preserveKinetic)
      {
        target.setVelocity(pVector);
      }
      if (preservePotential)
      {
        target.setFallDistance(pFallingDist);
      }
      if (!hideOutput)
      {
        MessageUtil.sendMessage(sender, Prefix.INFO_TELEPORT, ComponentUtil.translate("%s와(과) %s의 위치를 서로 맞바꾸었습니다", player, target));
        if (!player.equals(sender))
        {
          MessageUtil.sendMessage(player, Prefix.INFO_TELEPORT, ComponentUtil.translate("%s이(가) 당신과 %s의 위치를 서로 맞바꾸었습니다", sender, target));
        }
        if (!target.equals(sender))
        {
          MessageUtil.sendMessage(target, Prefix.INFO_TELEPORT, ComponentUtil.translate("%s이(가) 당신과 %s의 위치를 서로 맞바꾸었습니다", sender, player));
        }
      }
    }
    else
    {
      MessageUtil.longArg(sender, 5, args);
      if (sender instanceof Player)
      {
        MessageUtil.commandInfo(sender, label, usage);
        return true;
      }
      MessageUtil.commandInfo(sender, label, consoleUsage);
      return true;
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
      List<String> list = new ArrayList<>();
      list.addAll(Method.tabCompleterEntity(sender, args, "<개체> <다른 개체> [운동 에너지 보존]"));
      list.addAll(Method.tabCompleterEntity(sender, args, "<자신과 위치를 맞바꿀 개체>"));
      return list;
    }
    else if (length == 2)
    {
      return Method.tabCompleterEntity(sender, args,"<다른 개체>");
    }
    else if (length == 3)
    {
      return Method.tabCompleterBoolean(args, "[운동 에너지 보존]");
    }
    else if (length == 4)
    {
      return Method.tabCompleterBoolean(args, "[위치 에너지 보존]");
    }
    else if (length == 5)
    {
      return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
    }

    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}

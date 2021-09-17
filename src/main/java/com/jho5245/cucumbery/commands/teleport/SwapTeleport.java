package com.jho5245.cucumbery.commands.teleport;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class SwapTeleport implements CommandExecutor
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
      Player player, target;
      if (args.length == 1)
      {
        player = (Player) sender;
        target = SelectorUtil.getPlayer(sender, args[0]);
      }
      else
      {
        player = SelectorUtil.getPlayer(sender, args[0]);
        if (player == null)
        {
          return true;
        }
        target = SelectorUtil.getPlayer(sender, args[1]);
      }
      if (target == null)
      {
        return true;
      }
      if (player == target)
      {
        MessageUtil.sendError(sender, "같은 플레이어의 위치는 서로 맞바꿀 수 없습니다.");
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
        MessageUtil.sendMessage(sender, Prefix.INFO_TELEPORT, player, "와(과) ", target, "의 위치를 서로 맞바꾸었습니다.");
        if (!player.equals(sender))
        {
          MessageUtil.sendMessage(player, Prefix.INFO_TELEPORT, sender, "&r이 당신과 ", target, "의 위치를 서로 맞바꾸었습니다.");
        }
        if (!target.equals(sender))
        {
          MessageUtil.sendMessage(target, Prefix.INFO_TELEPORT, sender, "&r이 당신과 &e", player, "&r의 위치를 서로 맞바꾸었습니다.");
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
}

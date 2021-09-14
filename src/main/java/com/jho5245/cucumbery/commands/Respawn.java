package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Respawn implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_RESPAWN, true))
    {
      return sender instanceof Player;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return sender instanceof Player;
    }
    if (args.length == 0)
    {
      MessageUtil.shortArg(sender, 1, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return sender instanceof Player;
    }
    else if (args.length <= 2)
    {
      if (args.length == 2 && !MessageUtil.isBoolean(sender, args, 2, true))
      {
        return sender instanceof Player;
      }
      List<Player> targets = SelectorUtil.getPlayers(sender, args[0]);
      if (targets == null)
      {
        return sender instanceof Player;
      }
      List<Player> success = new ArrayList<>();
      List<Player> failure = new ArrayList<>();
      for (Player target : targets)
      {
        if (target.isDead())
        {
          success.add(target);
          target.spigot().respawn();
        }
        else
        {
          failure.add(target);
        }
      }
      if (!(args.length == 2 && args[1].equals("true")))
      {
        if (success.size() == 0)
        {
          MessageUtil.sendError(sender, ComponentUtil.createTranslate(targets.size() == 1 ? "%s은(는) 죽어 있는 상태가 아닙니다." : "%s 모두 죽어 있는 상태가 아닙니다.", targets));
          return sender instanceof Player;
        }
        if (failure.size() > 0)
        {
          MessageUtil.sendWarn(sender, ComponentUtil.createTranslate("%s은(는) 죽어 있는 상태가 아니여서 강제로 리스폰시키지 못했습니다.", failure));
        }
        MessageUtil.sendMessage(sender, ComponentUtil.createTranslate("%s을(를) 강제로 리스폰시켰습니다.", success));
      }
    }
    else
    {
      MessageUtil.longArg(sender, 2, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return true;
    }
    return true;
  }
}

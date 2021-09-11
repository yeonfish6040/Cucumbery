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

public class Respawn implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_RESPAWN, true))
    {
      return true;
    }
    if (args.length == 0)
    {
      MessageUtil.shortArg(sender, 1, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return true;
    }
    else if (args.length <= 2)
    {
      if (args.length == 2 && !MessageUtil.isBoolean(sender, args, 2, true))
      {
        return true;
      }
      Player target = SelectorUtil.getPlayer(sender, args[0]);
      if (target == null)
      {
        return true;
      }
      if (!target.isDead())
      {
        if (args.length == 2 && args[1].equals("true"))
        {
          return true;
        }
        MessageUtil.sendError(sender, ComponentUtil.createTranslate("%s은(는) 죽어 있는 상태가 아닙니다.", target));
        return true;
      }
      target.spigot().respawn();
      if (args.length != 2 || !args[1].equals("true"))
      {
        if (!target.equals(sender))
        {
          MessageUtil.info(target, ComponentUtil.createTranslate("%s이(가) 당신을 강제로 리스폰시켰습니다.", sender));
        }
        MessageUtil.info(sender, ComponentUtil.createTranslate("%s을(를) 강제로 리스폰시켰습니다.", target));
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

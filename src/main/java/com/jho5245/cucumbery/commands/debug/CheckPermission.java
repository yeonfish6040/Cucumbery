package com.jho5245.cucumbery.commands.debug;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CheckPermission implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_CHECKPERMISSION, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return sender instanceof Player;
    }
    String usage = cmd.getUsage().replace("/<command> ", "");
    if (args.length < 2)
    {
      MessageUtil.shortArg(sender, 1, args);
      MessageUtil.commandInfo(sender, label, usage);
      return true;
    }
    else if (args.length == 2)
    {
      List<Entity> targets = SelectorUtil.getEntities(sender, args[0]);
      if (targets == null)
      {
        return true;
      }
      List<Entity> hasPermission = new ArrayList<>();
      List<Entity> noPermission = new ArrayList<>();
      for (Entity target : targets)
      {
        if (target.hasPermission(args[1]))
        {
          hasPermission.add(target);
        }
        else
        {
          noPermission.add(target);
        }
      }
      if (hasPermission.size() > 0)
      {
        MessageUtil.info(sender, ComponentUtil.createTranslate("%s은(는) %s 퍼미션 노드를 가지고 있습니다.", hasPermission, Constant.THE_COLOR_HEX + args[1]));
      }
      if (noPermission.size() > 0)
      {
        MessageUtil.info(sender, ComponentUtil.createTranslate("%s은(는) %s 퍼미션 노드를 가지고 있지 않습니다.", noPermission, Constant.THE_COLOR_HEX + args[1]));
      }
    }
    else
    {
      MessageUtil.longArg(sender, 2, args);
      MessageUtil.commandInfo(sender, label, usage);
      return true;
    }
    return true;
  }
}

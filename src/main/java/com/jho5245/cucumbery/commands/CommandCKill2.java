package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandCKill2 implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_TRUEKILL, true))
    {
      return sender instanceof Player;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return sender instanceof Player;
    }
    if (args.length == 0 || args.length > 2)
    {
      if (args.length == 0)
      {
        MessageUtil.shortArg(sender, 1, args);
      }
      else
      {
        MessageUtil.longArg(sender, 2, args);
      }
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return sender instanceof Player;
    }
    List<Entity> entities = SelectorUtil.getEntities(sender, args[0]);
    if (entities == null)
    {
      return sender instanceof Player;
    }
    List<Entity> successEntities = new ArrayList<>();
    for (Entity entity : entities)
    {
      if (entity instanceof Player player)
      {
        if (!player.isDead())
        {
          double health = player.getHealth();
          player.setHealth(0);
          if (player.getHealth() != health)
          {
            successEntities.add(player);
          }
        }
      }
      else
      {
        successEntities.add(entity);
        entity.remove();
      }
    }
    if (!MessageUtil.isBoolean(sender, args, 2, true))
    {
      return sender instanceof Player;
    }
    boolean hideOutput = args.length == 2 && args[1].equals("true");
    if (!hideOutput)
    {
      MessageUtil.sendMessage(sender, Prefix.INFO, ComponentUtil.createTranslate("%s을(를) 죽였습니다.", successEntities));
    }
    return true;
  }
}












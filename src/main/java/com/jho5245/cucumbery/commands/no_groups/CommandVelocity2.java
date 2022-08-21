package com.jho5245.cucumbery.commands.no_groups;

import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.SelectorUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandVelocity2 implements CommandExecutor, TabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_VELOCITY, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    if (args.length < 4)
    {
      MessageUtil.shortArg(sender, 4, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return true;
    }
    else if (args.length == 4)
    {
      List<Entity> entities = SelectorUtil.getEntities(sender, args[0]);
      if (entities == null)
      {
        return true;
      }
      for (Entity entity : entities)
      {
        Vector vector = entity.getVelocity();
        double x = vector.getX(), y = vector.getY(), z = vector.getZ();

        String xInput = args[1], yInput = args[2], zInput = args[3];
        if (MessageUtil.isDouble(sender, xInput, false))
        {
          x = Double.parseDouble(xInput);
        }
        else
        {
          if (xInput.startsWith("~"))
          {
            if (!xInput.equals("~"))
            {
              xInput = xInput.substring(1);
              if (!MessageUtil.isDouble(sender, xInput, true))
              {
                return true;
              }
              x += Double.parseDouble(xInput);
            }
          }
        }

        if (MessageUtil.isDouble(sender, yInput, false))
        {
          y = Double.parseDouble(yInput);
        }
        else
        {
          if (yInput.startsWith("~"))
          {
            if (!yInput.equals("~"))
            {
              yInput = yInput.substring(1);
              if (!MessageUtil.isDouble(sender, yInput, true))
              {
                return true;
              }
              y += Double.parseDouble(yInput);
            }
          }
        }

        if (MessageUtil.isDouble(sender, zInput, false))
        {
          z = Double.parseDouble(zInput);
        }
        else
        {
          if (zInput.startsWith("~"))
          {
            if (!zInput.equals("~"))
            {
              zInput = zInput.substring(1);
              if (!MessageUtil.isDouble(sender, zInput, true))
              {
                return true;
              }
              z += Double.parseDouble(zInput);
            }
          }
        }

        Vector result = new Vector(x, y, z);
        entity.setVelocity(result);
      }
    }
    else
    {
      MessageUtil.longArg(sender, 4, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
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

    if (args.length == 1)
    {
      return Method.tabCompleterEntity(sender, args, "<개체>", true);
    }
    Entity entity = SelectorUtil.getEntity(sender, args[0], false);

    String x = "~", y = "~", z = "~";
    if (entity != null)
    {
      Vector vector = entity.getVelocity();
      x = Constant.Sosu2.format(vector.getX());
      y = Constant.Sosu2.format(vector.getY());
      z = Constant.Sosu2.format(vector.getZ());
    }

    return switch (length)
            {
              case 2 -> Method.tabCompleterDoubleRadius(args, -Double.MAX_VALUE, Double.MAX_VALUE, "<x축 속력> <y축 속력> <z축 속력>", x);
              case 3 -> Method.tabCompleterDoubleRadius(args, -Double.MAX_VALUE, Double.MAX_VALUE, "<y축 속력> <z축 속력>", y);
              case 4 -> Method.tabCompleterDoubleRadius(args, -Double.MAX_VALUE, Double.MAX_VALUE, "<z축 속력>", z);
              default -> Collections.singletonList(Prefix.ARGS_LONG.toString());
            };

  }
}

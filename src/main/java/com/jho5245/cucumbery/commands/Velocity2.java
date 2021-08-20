package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.data.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class Velocity2 implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_VELOCITY, true))
    {
      return true;
    }
    if (args.length < 4)
    {
      MessageUtil.shortArg(sender, 4, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return true;
    }
    else if (args.length == 4)
    {
      Entity entity = Method.getEntity(sender, args[0]);

      if (entity == null)
      {
        return true;
      }

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
    else
    {
      MessageUtil.longArg(sender, 4, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return true;
    }
    return true;
  }
}

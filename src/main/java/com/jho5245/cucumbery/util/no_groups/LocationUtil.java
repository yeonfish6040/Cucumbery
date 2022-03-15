package com.jho5245.cucumbery.util.no_groups;

import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LocationUtil
{
  @Nullable
  public static Location location(@NotNull CommandSender sender, @NotNull String arg, boolean isInteger, boolean notice)
  {
    String[] split = arg.split(" ");
    if (split.length < 3)
    {
      if (notice)
      {
        MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", Component.translatable("argument.pos3d.incomplete"), Component.text(arg, NamedTextColor.YELLOW)));
      }
      return null;
    }
    if (split.length > 3)
    {
      if (notice)
      {
        MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", Component.translatable("좌표값은 3개이어야 합니다"), Component.text(arg, NamedTextColor.YELLOW)));
      }
      return null;
    }
    if (arg.contains("~") && arg.contains("^") || (arg.contains("^") && !Method.allStartsWith("^", true, arg.split(" "))))
    {
      if (notice)
      {
        MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", Component.translatable("argument.pos.mixed"), Component.text(arg, NamedTextColor.YELLOW)));
      }
      return null;
    }
    String xStr = split[0], yStr = split[1], zStr = split[2];
    double x = 0, y = 0, z = 0;
    Location location;
    if (sender instanceof Entity entity)
    {
      location = entity.getLocation();
    }
    else if (sender instanceof BlockCommandSender blockCommandSender)
    {
      location = blockCommandSender.getBlock().getLocation();
    }
    else
    {
      location = new Location(Bukkit.getWorlds().get(0),0,0,0);
    }
    if (arg.startsWith("^"))
    {
      Vector vector = location.getDirection();
      if (isInteger)
      {
        if (xStr.equals("^"))
        {
          x = location.getX() + vector.getX();
        }
      }
      else
      {

      }
    }
    else
    {
      if (isInteger)
      {
        if (!MessageUtil.isInteger(sender, xStr, false))
        {
          if (xStr.equals("~"))
          {
            x = location.getBlockX();
          }
          else if (xStr.startsWith("~"))
          {
            xStr = xStr.substring(1);
            if (!MessageUtil.isInteger(sender, xStr, false))
            {
              MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", Component.translatable("argument.pos.missing.int"), Component.text(arg)));
            }
            else
            {
              x = location.getBlockX() + Integer.parseInt(xStr);
            }
          }
          else
          {
            if (notice)
            {
              MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", Component.translatable("argument.pos.missing.int"), Component.text(arg)));
            }
            return null;
          }
        }
        else
        x = Integer.parseInt(xStr);
        if (!MessageUtil.isInteger(sender, yStr, false))
        {
          if (yStr.equals("~"))
          {
            y = location.getBlockY();
          }
          else if (yStr.startsWith("~"))
          {
            yStr = yStr.substring(1);
            if (!MessageUtil.isInteger(sender, yStr, false))
            {
              MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", Component.translatable("argument.pos.missing.int"), Component.text(arg)));
            }
            else
            {
              y = location.getBlockY() + Integer.parseInt(yStr);
            }
          }
          else
          {
            if (notice)
            {
              MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", Component.translatable("argument.pos.missing.int"), Component.text(arg)));
            }
            return null;
          }
        }
        else
          y = Integer.parseInt(yStr);
        if (!MessageUtil.isInteger(sender, zStr, false))
        {
          if (zStr.equals("~"))
          {
            z = location.getBlockZ();
          }
          else if (zStr.startsWith("~"))
          {
            zStr = zStr.substring(1);
            if (!MessageUtil.isInteger(sender, zStr, false))
            {
              MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", Component.translatable("argument.pos.missing.int"), Component.text(arg)));
            }
            else
            {
              z = location.getBlockZ() + Integer.parseInt(zStr);
            }
          }
          else
          {
            if (notice)
            {
              MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", Component.translatable("argument.pos.missing.int"), Component.text(arg)));
            }
            return null;
          }
        }
        else
          z = Integer.parseInt(zStr);
      }
      else
      {
        if (!MessageUtil.isDouble(sender, xStr, false))
        {
          if (xStr.equals("~"))
          {
            x = location.getX();
          }
          else if (xStr.startsWith("~"))
          {
            xStr = xStr.substring(1);
            if (!MessageUtil.isDouble(sender, xStr, false))
            {
              MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", Component.translatable("argument.pos.missing.double"), Component.text(arg)));
            }
            else
            {
              x = location.getX() + Double.parseDouble(xStr);
            }
          }
          else
          {
            if (notice)
            {
              MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", Component.translatable("argument.pos.missing.double"), Component.text(arg)));
            }
            return null;
          }
        }
        else
          x = Double.parseDouble(xStr);
        if (!MessageUtil.isDouble(sender, yStr, false))
        {
          if (yStr.equals("~"))
          {
            y = location.getY();
          }
          else if (yStr.startsWith("~"))
          {
            yStr = yStr.substring(1);
            if (!MessageUtil.isDouble(sender, yStr, false))
            {
              MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", Component.translatable("argument.pos.missing.double"), Component.text(arg)));
            }
            else
            {
              y = location.getY() + Double.parseDouble(yStr);
            }
          }
          else
          {
            if (notice)
            {
              MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", Component.translatable("argument.pos.missing.double"), Component.text(arg)));
            }
            return null;
          }
        }
        else
          y = Double.parseDouble(yStr);
        if (!MessageUtil.isDouble(sender, zStr, false))
        {
          if (zStr.equals("~"))
          {
            z = location.getZ();
          }
          else if (zStr.startsWith("~"))
          {
            zStr = zStr.substring(1);
            if (!MessageUtil.isDouble(sender, zStr, false))
            {
              MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", Component.translatable("argument.pos.missing.double"), Component.text(arg)));
            }
            else
            {
              z = location.getZ() + Double.parseDouble(zStr);
            }
          }
          else
          {
            if (notice)
            {
              MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", Component.translatable("argument.pos.missing.double"), Component.text(arg)));
            }
            return null;
          }
        }
        else
          z = Double.parseDouble(zStr);
      }
      return new Location(location.getWorld(), x, y, z, location.getYaw(), location.getPitch());
    }
    return null;
  }
}

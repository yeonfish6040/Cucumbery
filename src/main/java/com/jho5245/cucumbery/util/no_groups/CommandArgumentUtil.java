package com.jho5245.cucumbery.util.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class CommandArgumentUtil
{
  @Nullable
  public static Location location(@NotNull CommandSender sender, @NotNull String arg, boolean isInteger, boolean notice)
  {
    if (arg.equals("$target_block") && sender instanceof Player player)
    {
      Block block = player.getTargetBlockExact(6);
      if (block == null)
      {
        if (notice)
        {
          MessageUtil.sendError(sender, "블록을 바라보고 있지 않습니다");
        }
        return null;
      }
      return block.getLocation();
    }
    String[] split = arg.split(" ");
    if (split.length < 3)
    {
      if (notice)
      {
        MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", ComponentUtil.translate("argument.pos3d.incomplete"), Component.text(arg, NamedTextColor.YELLOW)));
      }
      return null;
    }
    if (split.length > 3)
    {
      if (notice)
      {
        MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", ComponentUtil.translate("좌표값은 3개이어야 합니다"), Component.text(arg, NamedTextColor.YELLOW)));
      }
      return null;
    }
    if (arg.contains("~") && arg.contains("^") || (arg.contains("^") && !Method.allStartsWith("^", true, arg.split(" "))))
    {
      if (notice)
      {
        MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", ComponentUtil.translate("argument.pos.mixed"), Component.text(arg, NamedTextColor.YELLOW)));
      }
      return null;
    }
    Location location = senderLocation(sender);
    String xStr = split[0], yStr = split[1], zStr = split[2];
    double x, y, z;
    if (arg.startsWith("^"))
    {
      double a, b, c;
      if (xStr.equals("^"))
      {
        a = 0;
      }
      else
      {
        xStr = xStr.substring(1);
        if (!MessageUtil.isDouble(sender, xStr, false))
        {
          if (notice)
          {
            MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", ComponentUtil.translate("argument.pos.missing.double"), Component.text(arg)));
          }
          return null;
        }
        else
        {
          a = Double.parseDouble(xStr);
        }
      }
      if (yStr.equals("^"))
      {
        b = 0;
      }
      else
      {
        yStr = yStr.substring(1);
        if (!MessageUtil.isDouble(sender, yStr, false))
        {
          if (notice)
          {
            MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", ComponentUtil.translate("argument.pos.missing.double"), Component.text(arg)));
          }
          return null;
        }
        else
        {
          b = Double.parseDouble(yStr);
        }
      }
      if (zStr.equals("^"))
      {
        c = 0;
      }
      else
      {
        zStr = zStr.substring(1);
        if (!MessageUtil.isDouble(sender, zStr, false))
        {
          if (notice)
          {
            MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", ComponentUtil.translate("argument.pos.missing.double"), Component.text(arg)));
          }
          return null;
        }
        else
        {
          c = Double.parseDouble(zStr);
        }
      }
      float yaw = location.getYaw(), pitch = location.getPitch();
      location = transformGlobal(location, new Vector(a, b, c)).toLocation(location.getWorld());
      location.setYaw(yaw);
      location.setPitch(pitch);
      x = isInteger ? location.getBlockX() : location.getX();
      y = isInteger ? location.getBlockY() : location.getY();
      z = isInteger ? location.getBlockZ() : location.getZ();
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
              MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", ComponentUtil.translate("argument.pos.missing.int"), Component.text(arg)));
              return null;
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
              MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", ComponentUtil.translate("argument.pos.missing.int"), Component.text(arg)));
            }
            return null;
          }
        }
        else
        {
          x = Integer.parseInt(xStr);
        }
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
              MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", ComponentUtil.translate("argument.pos.missing.int"), Component.text(arg)));
              return null;
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
              MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", ComponentUtil.translate("argument.pos.missing.int"), Component.text(arg)));
            }
            return null;
          }
        }
        else
        {
          y = Integer.parseInt(yStr);
        }
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
              MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", ComponentUtil.translate("argument.pos.missing.int"), Component.text(arg)));
              return null;
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
              MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", ComponentUtil.translate("argument.pos.missing.int"), Component.text(arg)));
            }
            return null;
          }
        }
        else
        {
          z = Integer.parseInt(zStr);
        }
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
              MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", ComponentUtil.translate("argument.pos.missing.double"), Component.text(arg)));
              return null;
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
              MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", ComponentUtil.translate("argument.pos.missing.double"), Component.text(arg)));
            }
            return null;
          }
        }
        else
        {
          x = Double.parseDouble(xStr);
        }
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
              MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", ComponentUtil.translate("argument.pos.missing.double"), Component.text(arg)));
              return null;
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
              MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", ComponentUtil.translate("argument.pos.missing.double"), Component.text(arg)));
            }
            return null;
          }
        }
        else
        {
          y = Double.parseDouble(yStr);
        }
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
              MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", ComponentUtil.translate("argument.pos.missing.double"), Component.text(arg)));
              return null;
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
              MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", ComponentUtil.translate("argument.pos.missing.double"), Component.text(arg)));
            }
            return null;
          }
        }
        else
        {
          z = Double.parseDouble(zStr);
        }
      }
    }
    return new Location(location.getWorld(), x, y, z, location.getYaw(), location.getPitch());
  }

  @Nullable
  public static Rotation rotation(@NotNull CommandSender sender, @NotNull String arg, boolean notice)
  {
    String[] split = arg.split(" ");
    if (split.length < 2)
    {
      if (notice)
      {
        MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", ComponentUtil.translate("argument.pos2d.incomplete"), Component.text(arg, NamedTextColor.YELLOW)));
      }
      return null;
    }
    if (split.length > 2)
    {
      if (notice)
      {
        MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", ComponentUtil.translate("좌표값은 2개이어야 합니다"), Component.text(arg, NamedTextColor.YELLOW)));
      }
      return null;
    }
    String yawStr = split[0], pitchStr = split[1];
    double yaw, pitch;
    Location location = senderLocation(sender);
    if (!MessageUtil.isDouble(sender, yawStr, false))
    {
      if (yawStr.equals("~"))
      {
        yaw = location.getYaw();
      }
      else if (yawStr.startsWith("~"))
      {
        yawStr = yawStr.substring(1);
        if (!MessageUtil.isDouble(sender, yawStr, false))
        {
          MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", ComponentUtil.translate("argument.pos.missing.double"), Component.text(arg)));
          return null;
        }
        else
        {
          yaw = location.getYaw() + Double.parseDouble(yawStr);
        }
      }
      else
      {
        if (notice)
        {
          MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", ComponentUtil.translate("argument.pos.missing.double"), Component.text(arg)));
        }
        return null;
      }
    }
    else
    {
      yaw = Double.parseDouble(yawStr);
    }
    if (!MessageUtil.isDouble(sender, pitchStr, false))
    {
      if (pitchStr.equals("~"))
      {
        pitch = location.getPitch();
      }
      else if (pitchStr.startsWith("~"))
      {
        pitchStr = pitchStr.substring(1);
        if (!MessageUtil.isDouble(sender, pitchStr, false))
        {
          MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", ComponentUtil.translate("argument.pos.missing.double"), Component.text(arg)));
          return null;
        }
        else
        {
          pitch = location.getPitch() + Double.parseDouble(pitchStr);
        }
      }
      else
      {
        if (notice)
        {
          MessageUtil.sendError(sender, ComponentUtil.translate("%s (%s)", ComponentUtil.translate("argument.pos.missing.double"), Component.text(arg)));
        }
        return null;
      }
    }
    else
    {
      pitch = Double.parseDouble(pitchStr);
    }
    return new Rotation((float) yaw, (float) pitch);
  }

  @Nullable
  public static World world(@NotNull CommandSender sender, @NotNull String arg, boolean notice)
  {
    if (arg.equals("$current_world"))
    {
      return senderLocation(sender).getWorld();
    }
    World world = Bukkit.getWorld(arg);
    if (world == null && notice)
    {
      MessageUtil.noArg(sender, Prefix.NO_WORLD, arg);
    }
    return world;
  }

  @NotNull
  private static Vector transformGlobal(Location reference, Vector local)
  {
    // Firstly a vector facing YAW = 0, on the XZ plane as start base
    Vector axisBase = new Vector(0, 0, 1);
    // This one pointing YAW + 90° should be the relative "left" of the field of view, isn't it (since ROLL always is 0°)?

    Vector axisLeft = axisBase.clone().rotateAroundY(Math.toRadians(-reference.getYaw() + 90.0f));
    // Left axis should be the rotation axis for going up, too, since it's perpendicular...
    Vector axisUp = reference.getDirection().clone().rotateAroundNonUnitAxis(axisLeft, Math.toRadians(-90f));

    // Based on these directions, we got all we need
    Vector sway = axisLeft.clone().normalize().multiply(local.getX());
    Vector heave = axisUp.clone().normalize().multiply(local.getY());
    Vector surge = reference.getDirection().clone().multiply(local.getZ());

    // Add up the global reference based result
    return new Vector(reference.getX(), reference.getY(), reference.getZ()).add(sway).add(heave).add(surge);

  }

  @NotNull
  public static Location senderLocation(@NotNull CommandSender sender)
  {
    if (sender instanceof Entity entity)
    {
      return entity.getLocation();
    }
    else if (sender instanceof BlockCommandSender blockCommandSender)
    {
      return blockCommandSender.getBlock().getLocation();
    }
    return new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
  }

  @NotNull
  public static Location senderLocation(@NotNull NativeProxyCommandSender sender)
  {
    CommandSender commandSender = sender.getCallee();
    return senderLocation(commandSender);
  }

  @Nullable
  public static NamespacedKey namespacedKey(@NotNull CommandSender sender, @NotNull String arg, @NotNull String argName, boolean notice)
  {
    String key = Cucumbery.getPlugin().getName().toLowerCase(Locale.ROOT);
    String value = arg;
    if (arg.contains(":"))
    {
      String[] split = arg.split(":");
      key = split[0];
      value = split[1];
    }
    try
    {
      return new NamespacedKey(key, value);
    }
    catch (Exception e)
    {
      if (notice)
      {
        MessageUtil.sendError(sender, "%s은(는) 알 수 없는 %s입니다", key + ":" + value, argName);
      }
    }
    return null;
  }

  public record Rotation(float yaw, float pitch) {}

  public record LocationTooltip(@NotNull Location location, @Nullable Component hover) {}
}

















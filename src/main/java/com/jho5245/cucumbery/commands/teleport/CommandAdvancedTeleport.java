package com.jho5245.cucumbery.commands.teleport;

import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.SelectorUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.sendercomponent.SenderComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandAdvancedTeleport implements CommandExecutor, TabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_ADVANCED_TELEPORT, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    String usage = cmd.getUsage().replace("/<command> ", "");
    if (args.length < 5)
    {
      MessageUtil.shortArg(sender, 5, args);
      MessageUtil.commandInfo(sender, label, usage);
      return true;
    }
    else if (args.length <= 10)
    {
      Entity target = SelectorUtil.getEntity(sender, args[0]);
      if (target == null)
      {
        return true;
      }
      if (!MessageUtil.isBoolean(sender, args, 8, true))
      {
        return true;
      }
      if (!MessageUtil.isBoolean(sender, args, 9, true))
      {
        return true;
      }
      if (!MessageUtil.isBoolean(sender, args, 10, true))
      {
        return true;
      }
      boolean preserveKinetic = args.length >= 8 && args[7].equals("true");
      boolean preservePotential = args.length >= 9 && args[8].equals("true");
      boolean hideOutput = args.length >= 10 && args[9].equals("true");
      World world = Bukkit.getServer().getWorld(args[1]);
      Location location = target.getLocation();
      if (!args[1].equals("~") && world == null)
      {
        MessageUtil.noArg(sender, Prefix.NO_WORLD, args[1]);
        return true;
      }
      else if (args[1].equals("~"))
      {
        world = target.getLocation().getWorld();
      }
      double x, y, z, yaw, pitch;
      if (args[2].equals("~"))
      {
        x = location.getX();
      }
      else if (args[2].startsWith("~"))
      {
        String relativeX = args[2].substring(1);
        if (!MessageUtil.isDouble(sender, relativeX, true))
        {
          return true;
        }
        x = location.getX() + Double.parseDouble(relativeX);
      }
      else
      {
        boolean number = MessageUtil.isDouble(sender, args[2], true);
        if (!number)
        {
          return true;
        }
        x = Double.parseDouble(args[2]);
      }
      if (args[3].equals("~"))
      {
        y = location.getY();
      }
      else if (args[3].startsWith("~"))
      {
        String relativeY = args[3].substring(1);
        if (!MessageUtil.isDouble(sender, relativeY, true))
        {
          return true;
        }
        y = location.getY() + Double.parseDouble(relativeY);
      }
      else
      {
        boolean number = MessageUtil.isDouble(sender, args[3], true);
        if (!number)
        {
          return true;
        }
        y = Double.parseDouble(args[3]);
      }
      if (args[4].equals("~"))
      {
        z = location.getZ();
      }
      else if (args[4].startsWith("~"))
      {
        String relativeZ = args[4].substring(1);
        if (!MessageUtil.isDouble(sender, relativeZ, true))
        {
          return true;
        }
        z = location.getZ() + Double.parseDouble(relativeZ);
      }
      else
      {
        boolean number = MessageUtil.isDouble(sender, args[4], true);
        if (!number)
        {
          return true;
        }
        z = Double.parseDouble(args[4]);
      }
      if (args.length >= 6)
      {
        if (args[5].equals("~"))
        {
          yaw = location.getYaw();
        }
        else if (args[5].startsWith("~"))
        {
          String relativeYaw = args[5].substring(1);
          if (!MessageUtil.isDouble(sender, relativeYaw, true))
          {
            return true;
          }
          yaw = location.getYaw() + Double.parseDouble(relativeYaw);
        }
        else
        {
          boolean number = MessageUtil.isDouble(sender, args[5], true);
          if (!number)
          {
            return true;
          }
          yaw = Double.parseDouble(args[5]);
        }
      }
      else
      {
        yaw = location.getYaw();
      }
      if (args.length >= 7)
      {
        if (args[6].equals("~"))
        {
          pitch = location.getPitch();
        }
        else if (args[6].startsWith("~"))
        {
          String relativePitch = args[6].substring(1);
          if (!MessageUtil.isDouble(sender, relativePitch, true))
          {
            return true;
          }
          pitch = location.getPitch() + Double.parseDouble(relativePitch);
        }
        else
        {
          boolean number = MessageUtil.isDouble(sender, args[6], true);
          if (!number)
          {
            return true;
          }
          pitch = Double.parseDouble(args[6]);
        }
      }
      else
      {
        pitch = location.getPitch();
      }
      Vector vector = target.getVelocity();
      Vector vectorClone = new Vector(vector.getX(), vector.getY(), vector.getZ());
      float fallDistance = target.getFallDistance();
      if (x > 3000_0000d)
      {
        x = 3000_0000d;
      }
      if (y > 3000_0000d)
      {
        y = 3000_0000d;
      }
      if (z > 3000_0000d)
      {
        z = 3000_0000d;
      }
      if (x < -3000_000d)
      {
        x = -3000_0000d;
      }
      if (y < -3000_000d)
      {
        y = -3000_0000d;
      }
      if (z < -3000_000d)
      {
        z = -3000_0000d;
      }
      target.teleport(new Location(world, x, y, z, (float) yaw, (float) pitch));
      if (preserveKinetic)
      {
        target.setVelocity(vectorClone);
      }
      if (preservePotential)
      {
        target.setFallDistance(fallDistance);
      }
      if (!hideOutput)
      {
        Component nameComponent = SenderComponentUtil.senderComponent(target);
        if (!target.equals(sender))
        {
          MessageUtil.sendMessage(target, Prefix.INFO_TELEPORT, SenderComponentUtil.senderComponent(sender) + "이 당신을 ");
          MessageUtil.sendMessage(target, Prefix.INFO_TELEPORT,
                  "rg255,204;" +
                          world.getName() +
                          "&r, rg255,204;" +
                          Constant.Sosu4.format(x) +
                          "&r, rg255,204;" +
                          Constant.Sosu4.format(y) +
                          "&r, rg255,204;" +
                          Constant.Sosu4.format(z) +
                          "&r, rg255,204;" +
                          Constant.Sosu4.format(yaw) +
                          "&r, rg255,204;" +
                          Constant.Sosu4.format(pitch));
          MessageUtil.sendMessage(target, Prefix.INFO_TELEPORT, ComponentUtil.translate(" 위치로 텔레포트 시켰습니다"));
        }
        MessageUtil.sendMessage(sender, Prefix.INFO_TELEPORT, ComponentUtil.translate("%s을(를)", nameComponent));
        MessageUtil.sendMessage(sender, Prefix.INFO_TELEPORT,
                "rg255,204;" +
                        world.getName() +
                        "&r, rg255,204;" +
                        Constant.Sosu4.format(x) +
                        "&r, rg255,204;" +
                        Constant.Sosu4.format(y) +
                        "&r, rg255,204;" +
                        Constant.Sosu4.format(z) +
                        "&r, rg255,204;" +
                        Constant.Sosu4.format(yaw) +
                        "&r, rg255,204;" +
                        Constant.Sosu4.format(pitch));
        MessageUtil.sendMessage(sender, Prefix.INFO_TELEPORT, ComponentUtil.translate("위치로 텔레포트 시켰습니다"));
      }
    }
    else
    {
      MessageUtil.longArg(sender, 10, args);
      MessageUtil.commandInfo(sender, label, usage);
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
      return Method.tabCompleterEntity(sender, args);
    }
    else if (length == 2)
    {
      return Method.tabCompleterList(args, Method.addAll(Method.listWorlds(), "~"), "<월드>");
    }
    else if (length >= 3 && length <= 7)
    {
      switch (length)
      {
        case 3:
          return Method.tabCompleterDoubleRadius(args, -Double.MAX_VALUE, Double.MAX_VALUE, "<x좌표> <y좌표> <z좌표> [x축 회전] [y축 회전]", "~");
        case 4:
          return Method.tabCompleterDoubleRadius(args, -Double.MAX_VALUE, Double.MAX_VALUE, "<y좌표> <z좌표> [x축 회전] [y축 회전]", "~");
        case 5:
          return Method.tabCompleterDoubleRadius(args, -Double.MAX_VALUE, Double.MAX_VALUE, "<z좌표> [x축 회전] [y축 회전]", "~");
        case 6:
          return Method.tabCompleterDoubleRadius(args, -Double.MAX_VALUE, Double.MAX_VALUE, "[x축 회전] [y축 회전]", "~");
        case 7:
          return Method.tabCompleterDoubleRadius(args, -Double.MAX_VALUE, Double.MAX_VALUE, "[y축 회전]", "~");
      }
    }
    else if (length == 8)
    {
      return Method.tabCompleterBoolean(args, "[운동 에너지 보존]");
    }
    else if (length == 9)
    {
      return Method.tabCompleterBoolean(args, "[위치 에너지 보존]");
    }
    else if (length == 10)
    {
      return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
    }
    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}

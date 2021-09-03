package com.jho5245.cucumbery.commands.hp;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class HealthBar implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_SHP, true))
    {
      return true;
    }
    String usage = cmd.getUsage().replace("/<command> ", "");
    if (args.length < 3)
    {
      MessageUtil.shortArg(sender, 3, args);
      MessageUtil.commandInfo(sender, label, usage);
      return true;
    } else if (args.length <= 4)
    {
      OfflinePlayer offlinePlayer = SelectorUtil.getOfflinePlayer(sender, args[1]);
      if (offlinePlayer == null)
      {
        return true;
      }
      Player target = offlinePlayer.getPlayer();
      boolean isOnline = target != null;
      UUID uuid = offlinePlayer.getUniqueId();
      if (!MessageUtil.isDouble(sender, args[2], true))
      {
        return true;
      }
      double value = Double.parseDouble(args[2]);
      if (!MessageUtil.checkNumberSize(sender, value, 1D, Double.MAX_VALUE))
      {
        return true;
      }
      double hp = 0d;
      double shp = 0d;
      if (isOnline)
      {
        hp = target.getHealth();
        shp = target.getHealthScale();
      }
      String hpStr = Constant.Sosu2.format(-1d);
      String mhpStr = Constant.Sosu2.format(-1d);
      if (isOnline)
      {
        hpStr = Constant.Sosu2.format(hp);
        mhpStr = Method.attributeString(target, Attribute.GENERIC_MAX_HEALTH);
      }
      boolean hideOutput = false;
      if (args.length == 4)
      {
        if (!args[3].equals("true") && !args[3].equals("false"))
        {
          MessageUtil.wrongBool(sender, 4, args);
          return true;
        }
        if (args[3].equals("true"))
        {
          hideOutput = true;
        }
      }
      String targetName = Method.getDisplayName(offlinePlayer);
      if (isOnline)
      {
        targetName = ComponentUtil.serialize(target.displayName());
      }
      if (args[0].equalsIgnoreCase("set"))
      {
        if (isOnline)
        {
          target.setHealthScale(value);
        }
        UserData.HEALTH_BAR.set(uuid, value);
        if (!hideOutput)
        {
          if (target != null && !target.equals(sender))
          {
            MessageUtil.sendMessage(target, Prefix.INFO_HEAL,
                    ComponentUtil.senderComponent(sender),
                    "이 당신의 HP바를 &e" +
                    Constant.Sosu15.format(value) +
                    "&r으로 설정하였습니다. (&eHP&r : &e" +
                    hpStr +
                    "&r / &e" +
                    mhpStr +
                    "&r, HP바 : &e" +
                    Constant.Sosu2.format(value) +
                    "&r)");
          }
          MessageUtil.sendMessage(sender, Prefix.INFO_HEAL,
                  "&e" +
                  targetName +
                  "&r의 HP바를 &e" +
                  Constant.Sosu15.format(value) +
                  "&r으로 설정하였습니다. (&eHP&r : &e" +
                  hpStr +
                  "&r / &e" +
                  mhpStr +
                  "&r, HP바 : &e" +
                  Constant.Sosu2.format(value) +
                  "&r)");
        }
      } else if (args[0].equalsIgnoreCase("give"))
      {
        if (isOnline)
        {
          target.setHealthScale(shp + value);
        }
        UserData.HEALTH_BAR.set(uuid, shp + value);
        if (!hideOutput)
        {
          if (target != null && !target.equals(sender))
          {
            MessageUtil.sendMessage(target, Prefix.INFO_HEAL,
                    ComponentUtil.senderComponent(sender),
                    "이 당신의 HP바를 &e" +
                    Constant.Sosu15.format(value) +
                    "&r만큼 증가시켰습니다. (&eHP&r : &e" +
                    hpStr +
                    "&r / &e" +
                    mhpStr +
                    "&r, HP바 : &e" +
                    Constant.Sosu2.format(target.getHealthScale()) +
                    "&r)");
          }
          MessageUtil.sendMessage(sender, Prefix.INFO_HEAL,
                  "&e" +
                  targetName +
                  "&r의 HP바를 &e" +
                  Constant.Sosu15.format(value) +
                  "&r만큼 증가시켰습니다. (&eHP&r : &e" +
                  hpStr +
                  "&r / &e" +
                  mhpStr +
                  "&r, HP바 : &e" +
                  Constant.Sosu2.format(target != null ? target.getHealthScale() : 0) +
                  "&r)");
        }
      } else if (args[0].equalsIgnoreCase("take"))
      {
        if (shp <= 1D)
        {
          if (!hideOutput)
          {
            MessageUtil.sendError(sender, "더 이상 &e" + targetName + "&r의 HP바를 차감할 수 없습니다.");
          }
          return true;
        }
        if (shp - value < 1D)
        {
          if (!hideOutput)
          {
            MessageUtil.sendWarn(sender, "차감시킨 HP바의 양이 &e" + targetName + "&r의 HP바의 양보다 적습니다. (&e손해 본 값&r : &e" + Constant.Sosu2.format(1D + value - shp) + "&r)");
          }
          target.setHealthScale(1D);
          UserData.HEALTH_BAR.set(uuid, 1d);
          if (!hideOutput)
          {
            if (!target.equals(sender))
            {
              MessageUtil.sendMessage(target, Prefix.INFO_HEAL,
                      ComponentUtil.senderComponent(sender),
                      "이 당신의 HP바를 &e" +
                      Constant.Sosu15.format(shp - 1D) +
                      "&r만큼 차감시켰습니다. (&eHP&r : &e" +
                      hpStr +
                      "&r / &e" +
                      mhpStr +
                      "&r, HP바 : &e" +
                      Constant.Sosu2.format(target.getHealthScale()) +
                      "&r)");
            }
            MessageUtil.sendMessage(sender, Prefix.INFO_HEAL,
                    "&e" +
                    targetName +
                    "&r의 HP바를 &e" +
                    Constant.Sosu15.format(shp - 1D) +
                    "&r만큼 차감시켰습니다. (&eHP&r : &e" +
                    hpStr +
                    "&r / &e" +
                    mhpStr +
                    "&r, HP바 : &e" +
                    Constant.Sosu2.format(target.getHealthScale()) +
                    "&r)");
          }
        } else
        {
          target.setHealthScale(shp - value);
          UserData.HEALTH_BAR.set(uuid, shp - value);
          if (!hideOutput)
          {
            if (!target.equals(sender))
            {
              MessageUtil.sendMessage(target, Prefix.INFO_HEAL,
                      ComponentUtil.senderComponent(sender),
                      "이 당신의 HP바를 &e" +
                      Constant.Sosu15.format(value) +
                      "&r만큼 차감시켰습니다. (&eHP&r : &e" +
                      hpStr +
                      "&r / &e" +
                      mhpStr +
                      "&r, HP바 : &e" +
                      Constant.Sosu2.format(target.getHealthScale()) +
                      "&r)");
            }
            MessageUtil.sendMessage(sender, Prefix.INFO_HEAL,
                    "&e" +
                    targetName +
                    "&r의 HP바를 &e" +
                    Constant.Sosu15.format(value) +
                    "&r만큼 차감시켰습니다. (&eHP&r : &e" +
                    hpStr +
                    "&r / &e" +
                    mhpStr +
                    "&r, HP바 : &e" +
                    Constant.Sosu2.format(target.getHealthScale()) +
                    "&r)");
          }
        }
      } else
      {
        MessageUtil.wrongArg(sender, 1, args);
        MessageUtil.commandInfo(sender, label, usage);
        return true;
      }
    } else
    {
      MessageUtil.longArg(sender, 4, args);
      MessageUtil.commandInfo(sender, label, usage);
      return true;
    }
    return true;
  }
}

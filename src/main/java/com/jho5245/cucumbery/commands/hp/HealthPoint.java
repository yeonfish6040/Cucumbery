package com.jho5245.cucumbery.commands.hp;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.MessageUtil.ConsonantType;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HealthPoint implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_HP, true))
    {
      return true;
    }
    String usage = cmd.getUsage().replace("/<command> ", "");
    if (args.length < 3)
    {
      MessageUtil.shortArg(sender, 3, args);
      MessageUtil.commandInfo(sender, label, usage);
      return true;
    }
    else if (args.length <= 5)
    {
      Entity entity = Method.getEntity(sender, args[1]);
      if (entity == null)
      {
        return true;
      }
      String targetName = (entity).getName();
      if (!(entity instanceof Damageable) && !(entity instanceof Attributable))
      {
        MessageUtil.sendError(sender, "&e" + targetName + "&r" + MessageUtil.getFinalConsonant(targetName, ConsonantType.은_는) + "&r HP 태그를 가지고 있지 않습니다.");
        return true;
      }
      Damageable target = (Damageable) Method.getEntity(sender, args[1]);
      Attributable attributable = (Attributable) entity;
      if (target == null)
      {
        return true;
      }
      if (!MessageUtil.isDouble(sender, args[2], true))
      {
        return true;
      }
      double value = Double.parseDouble(args[2]);
      if (!MessageUtil.isBoolean(sender, args, 4, true))
      {
        return true;
      }
      if (!MessageUtil.isBoolean(sender, args, 5, true))
      {
        return true;
      }
      boolean force = args.length >= 4 && args[3].equals("true");
      boolean hideOutput = args.length == 5 && args[4].equals("true");
      if (!force && !MessageUtil.checkNumberSize(sender, value, 1D, Double.MAX_VALUE))
      {
        return true;
      }
      if (force && !MessageUtil.checkNumberSize(sender, value, 0D, Double.MAX_VALUE))
      {
        return true;
      }
      double hp = target.getHealth();
      AttributeInstance maxHealthInstance = attributable.getAttribute(Attribute.GENERIC_MAX_HEALTH);
      double maxHealth = 0;
      if (maxHealthInstance != null)
      {
        maxHealth = maxHealthInstance.getValue();
      }
      double baseMaxHealth = 0;
      if (maxHealthInstance != null)
      {
        baseMaxHealth = maxHealthInstance.getBaseValue();
      }
      double shp = -1;
      boolean isPlayer = entity instanceof Player;
      String playerSuffix = isPlayer ? "님" : "";
      if (isPlayer)
      {
        shp = ((Player) entity).getHealthScale();
      }
      String mhpStr = Method.attributeString(attributable, Attribute.GENERIC_MAX_HEALTH);
      String shpStr = Constant.Sosu2.format(shp);
      if (args[0].equalsIgnoreCase("set"))
      {
        if (!force && value > maxHealth)
        {
          target.setHealth(maxHealth);
          if (!hideOutput)
          {
            MessageUtil.sendWarn(sender, "설정한 HP의 값이 ", targetName, playerSuffix + "의 최대 HP의 값보다 많습니다. (&e손해 본 값&r : &e" + Constant.Sosu2.format(value - maxHealth) + "&r)");
            MessageUtil.sendMessage(target, Prefix.INFO_HEAL, sender, "이 당신의 HP를 &e" + mhpStr + "&r으로 설정하였습니다. (&eHP&r : &e"
                    + Constant.Sosu2.format(target.getHealth()) + "&r / &e" + mhpStr + "&r, HP바 : &e" + shpStr + "&r)");
            MessageUtil.sendMessage(sender, Prefix.INFO_HEAL, targetName, playerSuffix + "의 HP를 &e" + mhpStr + "&r으로 설정하였습니다. (&eHP&r : &e"
                    + Constant.Sosu2.format(target.getHealth()) + "&r / &e" + mhpStr + "&r, HP바 : &e" + shpStr + "&r)");
          }
          return true;
        }
        boolean exceed = value > maxHealth;
        double differ = value - baseMaxHealth;
        if (exceed)
        {
          if (maxHealthInstance != null)
          {
            maxHealthInstance.setBaseValue(differ + value);
          }
        }
        target.setHealth(value);
        if (exceed)
        {
          if (maxHealthInstance != null)
          {
            maxHealthInstance.setBaseValue(baseMaxHealth);
          }
        }
        if (!hideOutput)
        {
          MessageUtil.sendMessage(target, Prefix.INFO_HEAL, sender, "이 당신의 HP를 &e" + Constant.Sosu15.format(value)
                  + "&r으로 설정하였습니다. (&eHP&r : &e" + Constant.Sosu2.format(target.getHealth()) + "&r / &e" + mhpStr + "&r, HP바 : &e" + shpStr + "&r)");
          MessageUtil.sendMessage(sender, Prefix.INFO_HEAL, target, playerSuffix + "의 HP를 &e" + Constant.Sosu15.format(value) + "&r으로 설정하였습니다. (&eHP&r : &e"
                  + Constant.Sosu2.format(target.getHealth()) + "&r / &e" + mhpStr + "&r, HP바 : &e" + shpStr + "&r)");
        }
      }
      else if (args[0].equalsIgnoreCase("give"))
      {
        if (!force && hp >= maxHealth)
        {
          if (!hideOutput)
          {
            MessageUtil.sendError(sender, "더 이상 ", target, playerSuffix + "에게 HP를 지급할 수 없습니다.");
          }
          return true;
        }
        if (!force && value + hp > maxHealth)
        {
          if (!hideOutput)
          {
            MessageUtil.sendWarn(sender,
                    "지급한 HP의 양이 ", targetName, playerSuffix + "의 최대 HP의 양보다 많습니다. (&e손해 본 값&r : &e" + Constant.Sosu2.format(value + hp - maxHealth) + "&r)");
          }
          target.setHealth(maxHealth);
          if (!hideOutput)
          {
            MessageUtil.sendMessage(target, Prefix.INFO_HEAL, sender, "이 당신에게 HP를 &e" + Constant.Sosu15.format(maxHealth - hp)
                    + "&r만큼 지급하였습니다. (&eHP&r : &e" + Constant.Sosu2.format(target.getHealth()) + "&r / &e" + mhpStr + "&r, HP바 : &e" + shpStr + "&r)");
            MessageUtil.sendMessage(sender, Prefix.INFO_HEAL, targetName, playerSuffix + "에게 HP를 &e" + Constant.Sosu15.format(maxHealth - hp)
                    + "&r만큼 지급하였습니다. (&eHP&r : &e" + Constant.Sosu2.format(target.getHealth()) + "&r / &e" + mhpStr + "&r, HP바 : &e" + shpStr + "&r)");
          }
        }
        else
        {
          boolean exceed = hp + value > maxHealth;
          double differ = hp + value - baseMaxHealth;
          if (exceed)
          {
            if (maxHealthInstance != null)
            {
              maxHealthInstance.setBaseValue(maxHealth + differ);
            }
          }
          target.setHealth(hp + value);
          if (exceed)
          {
            if (maxHealthInstance != null)
            {
              maxHealthInstance.setBaseValue(baseMaxHealth);
            }
          }
          if (!hideOutput)
          {
            MessageUtil.sendMessage(target, Prefix.INFO_HEAL, sender, "이 당신에게 HP를 &e" + Constant.Sosu15.format(value)
                    + "&r만큼 지급하였습니다. (&eHP&r : &e" + Constant.Sosu2.format(target.getHealth()) + "&r / &e" + mhpStr + "&r, HP바 : &e" + shpStr + "&r)");
            MessageUtil.sendMessage(sender, Prefix.INFO_HEAL, "&e" + targetName + "&r" + playerSuffix + "에게 HP를 &e" + Constant.Sosu15.format(value) + "&r만큼 지급하였습니다. (&eHP&r : &e"
                    + Constant.Sosu2.format(target.getHealth()) + "&r / &e" + mhpStr + "&r, HP바 : &e" + shpStr + "&r)");
          }
        }
      }
      else if (args[0].equalsIgnoreCase("take"))
      {
        if (!force && hp <= 1D)
        {
          if (!hideOutput)
          {
            MessageUtil.sendError(sender, "더 이상 &e" + targetName + "&r" + playerSuffix + "의 HP를 차감할 수 없습니다.");
          }
          return true;
        }
        if (!force && hp - value < 1D)
        {
          if (!hideOutput)
          {
            MessageUtil.sendWarn(sender, "차감한 HP의 양이 &e" + targetName + "&r" + playerSuffix + "의 HP의 양보다 많습니다. (&e손해 본 값&r : &e" + Constant.Sosu2.format(value - hp + 1D) + "&r)");
          }
          target.setHealth(1D);
          if (!hideOutput)
          {
            MessageUtil.sendMessage(target, Prefix.INFO_HEAL, sender, "이 당신의 HP를 &e" + Constant.Sosu15.format(hp - 1D)
                    + "&r만큼 차감하였습니다. (&eHP&r : &e" + Constant.Sosu2.format(target.getHealth()) + "&r / &e" + mhpStr + "&r, HP바 : &e" + shpStr + "&r)");
            MessageUtil.sendMessage(sender, Prefix.INFO_HEAL, target, playerSuffix + "의 HP를 &e" + Constant.Sosu15.format(hp - 1D) + "&r만큼 차감하였습니다. (&eHP&r : &e"
                    + Constant.Sosu2.format(target.getHealth()) + "&r / &e" + mhpStr + "&r, HP바 : &e" + shpStr + "&r)");
          }
        }
        else
        {
          boolean exceed = hp - value > maxHealth;
          double differ = hp - value - baseMaxHealth;
          if (exceed)
          {
            if (maxHealthInstance != null)
            {
              maxHealthInstance.setBaseValue(maxHealth + differ);
            }
          }
          target.setHealth(Math.max(0d, hp - value));
          if (exceed)
          {
            if (maxHealthInstance != null)
            {
              maxHealthInstance.setBaseValue(baseMaxHealth);
            }
          }
          if (!hideOutput)
          {
            if (target.getHealth() == 0d)
            {
              value = hp;
            }
            MessageUtil.sendMessage(target, Prefix.INFO_HEAL, sender, "이 당신의 HP를 &e" + Constant.Sosu15.format(value)
                    + "&r만큼 차감하였습니다. (&eHP&r : &e" + Constant.Sosu2.format(target.getHealth()) + "&r / &e" + mhpStr + "&r, HP바 : &e" + shpStr + "&r)");
            MessageUtil.sendMessage(sender, Prefix.INFO_HEAL, target, playerSuffix + "의 HP를 &e" + Constant.Sosu15.format(value) + "&r만큼 차감하였습니다. (&eHP&r : &e"
                    + Constant.Sosu2.format(target.getHealth()) + "&r / &e" + mhpStr + "&r, HP바 : &e" + shpStr + "&r)");
          }
        }
      }
      else
      {
        MessageUtil.wrongArg(sender, 1, args);
        MessageUtil.commandInfo(sender, label, usage);
        return true;
      }
    }
    else
    {
      MessageUtil.longArg(sender, 4, args);
      MessageUtil.commandInfo(sender, label, usage);
      return true;
    }
    return true;
  }
}

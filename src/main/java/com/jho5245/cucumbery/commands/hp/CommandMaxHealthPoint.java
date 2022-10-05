package com.jho5245.cucumbery.commands.hp;

import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.MessageUtil.ConsonantType;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.SelectorUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.*;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandMaxHealthPoint implements CommandExecutor, TabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_MHP, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
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
      Entity entity = SelectorUtil.getEntity(sender, args[1]);
      if (entity == null)
      {
        return true;
      }
      String targetName = (entity).getName();
      if (!(entity instanceof Damageable target) || !(entity instanceof Attributable attributable))
      {
        MessageUtil.sendError(sender, Constant.THE_COLOR_HEX + targetName + "&r" + MessageUtil.getFinalConsonant(targetName, ConsonantType.은는) + "&r HP 태그를 가지고 있지 않습니다");
        return true;
      }
      if (!MessageUtil.isDouble(sender, args[2], true))
      {
        return true;
      }
      double value = Double.parseDouble(args[2]);
      if (!MessageUtil.checkNumberSize(sender, value, 1D, Double.MAX_VALUE))
      {
        return true;
      }
      AttributeInstance maxHealthInstance = attributable.getAttribute(Attribute.GENERIC_MAX_HEALTH);
      double mhp = maxHealthInstance != null ? maxHealthInstance.getBaseValue() : 0;
      double shp = -1;
      boolean isPlayer = entity instanceof Player;
      if (isPlayer)
      {
        shp = ((Player) entity).getHealthScale();
      }
      String shpStr = Constant.Sosu2.format(shp);
      boolean hideOutput = false;
      if (args.length == 5)
      {
        if (!args[4].equals("true") && !args[4].equals("false"))
        {
          MessageUtil.wrongBool(sender, 5, args);
          return true;
        }
        if (args[4].equals("true"))
        {
          hideOutput = true;
        }
      }
      boolean setHealthToMax = false;
      if (args.length >= 4)
      {
        if (!args[3].equals("true") && !args[3].equals("false"))
        {
          MessageUtil.wrongBool(sender, 4, args);
          return true;
        }
        if (args[3].equals("true"))
        {
          setHealthToMax = true;
        }
      }
      if (args[0].equalsIgnoreCase("set"))
      {
        if (maxHealthInstance != null)
        {
          maxHealthInstance.setBaseValue(value);
        }
        if (setHealthToMax)
        {
          target.setHealth(maxHealthInstance != null ? maxHealthInstance.getValue() : 0);
        }
        if (!hideOutput)
        {
          MessageUtil.sendMessage(target, Prefix.INFO_HEAL, sender, "이(가) 당신의 순수 최대 HP를 rg255,204;" + Constant.Sosu15.format(value) + "&r으로 설정했습니다 (rg255,204;HP&r : rg255,204;" + Constant.Sosu2
                  .format(target.getHealth()) + "&r / rg255,204;" + Constant.Sosu2.format(maxHealthInstance != null ? maxHealthInstance.getValue() : 0) + "&r, HP바 : rg255,204;" + shpStr + "&r)");
          MessageUtil.sendMessage(sender, Prefix.INFO_HEAL, target, "의 순수 최대 HP를 rg255,204;" + Constant.Sosu15.format(value) + "&r으로 설정했습니다 (rg255,204;HP&r : rg255,204;" + Constant.Sosu2
                  .format(target.getHealth()) + "&r / rg255,204;" + Method.attributeString(attributable, Attribute.GENERIC_MAX_HEALTH) + "&r, HP바 : rg255,204;" + shpStr + "&r)");
        }
      }
      else if (args[0].equalsIgnoreCase("give"))
      {
        if (maxHealthInstance != null)
        {
          maxHealthInstance.setBaseValue(mhp + value);
        }
        if (setHealthToMax)
        {
          target.setHealth(maxHealthInstance != null ? maxHealthInstance.getValue() : 0);
        }
        if (!hideOutput)
        {
          MessageUtil.sendMessage(target, Prefix.INFO_HEAL, sender, "이(가) 당신의 순수 최대 HP를 rg255,204;" + Constant.Sosu15.format(value) + "&r만큼 증가시켰습니다 (rg255,204;HP&r : rg255,204;" + Constant.Sosu2
                  .format(target.getHealth()) + "&r / rg255,204;" + Constant.Sosu2.format(maxHealthInstance != null ? maxHealthInstance.getValue() : 0) + "&r, HP바 : rg255,204;" + shpStr + "&r)");
          MessageUtil.sendMessage(sender, Prefix.INFO_HEAL, target, "의 순수 최대 HP를 rg255,204;" + Constant.Sosu15.format(value) + "&r만큼 증가시켰습니다 (rg255,204;HP&r : rg255,204;" + Constant.Sosu2
                  .format(target.getHealth()) + "&r / rg255,204;" + Method.attributeString(attributable, Attribute.GENERIC_MAX_HEALTH) + "&r, HP바 : rg255,204;" + shpStr + "&r)");
        }
      }
      else if (args[0].equalsIgnoreCase("take"))
      {
        if (mhp <= 1D)
        {
          if (!hideOutput)
          {
            MessageUtil.sendError(sender, "더 이상 rg255,204;" + targetName, "의 순수 최대 HP를 차감할 수 없습니다");
          }
          return true;
        }
        if (mhp - value < 1D)
        {
          if (!hideOutput)
          {
            MessageUtil.sendWarn(sender, "차감시킨 순수 최대 HP의 양이 rg255,204;" + targetName, "의 순수 최대 HP의 양보다 적습니다 (rg255,204;손해 본 값&r : rg255,204;" + Constant.Sosu2.format(1D + value - mhp) + "&r)");
          }
          maxHealthInstance.setBaseValue(1D);
          if (setHealthToMax)
          {
            target.setHealth(maxHealthInstance.getValue());
          }
          MessageUtil.sendMessage(target, Prefix.INFO_HEAL, sender, "이 당신의 순수 최대 HP를 rg255,204;" + Constant.Sosu15.format(mhp - 1D) + "&r만큼 차감시켰습니다 (rg255,204;HP&r : rg255,204;" + Constant.Sosu2
                  .format(target.getHealth()) + "&r / rg255,204;" + Method.attributeString(attributable, Attribute.GENERIC_MAX_HEALTH) + "&r, HP바 : rg255,204;" + shpStr + "&r)");
          MessageUtil.sendMessage(sender, Prefix.INFO_HEAL, target, "의 순수 최대 HP를 rg255,204;" + Constant.Sosu15.format(mhp - 1D) + "&r만큼 차감시켰습니다 (rg255,204;HP&r : rg255,204;" + Constant.Sosu2
                  .format(target.getHealth()) + "&r / rg255,204;" + Method.attributeString(attributable, Attribute.GENERIC_MAX_HEALTH) + "&r, HP바 : rg255,204;" + shpStr + "&r)");
        }
        else
        {
          maxHealthInstance.setBaseValue(mhp - value);
          if (setHealthToMax)
          {
            target.setHealth(maxHealthInstance.getValue());
          }
          if (!hideOutput)
          {
            MessageUtil.sendMessage(target, Prefix.INFO_HEAL, sender, "이 당신의 순수 최대 HP를 rg255,204;" + Constant.Sosu15.format(value) + "&r만큼 차감시켰습니다 (rg255,204;HP&r : rg255,204;" + Constant.Sosu2
                    .format(target.getHealth()) + "&r / rg255,204;" + Method.attributeString(attributable, Attribute.GENERIC_MAX_HEALTH) + "&r, HP바 : rg255,204;" + shpStr + "&r)");
            MessageUtil.sendMessage(sender, Prefix.INFO_HEAL, target, "의 순수 최대 HP를 rg255,204;" + Constant.Sosu15.format(value) + "&r만큼 차감시켰습니다 (rg255,204;HP&r : rg255,204;" + Constant.Sosu2
                    .format(target.getHealth()) + "&r / rg255,204;" + Method.attributeString(attributable, Attribute.GENERIC_MAX_HEALTH) + "&r, HP바 : rg255,204;" + shpStr + "&r)");
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
      MessageUtil.longArg(sender, 5, args);
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
      return Method.tabCompleterList(args, "<인수>", "set", "give", "take");
    }
    else if (length == 2 && (Method.equals(args[0], "<인수>", "set", "give", "take")))
    {
      return Method.tabCompleterEntity(sender, args);
    }
    else if (length == 3)
    {
      return Method.tabCompleterDoubleRadius(args, 1, Double.MAX_VALUE, "<수치>");
    }
    else if (length == 4 && (Method.equals(args[0], "<인수>", "set", "give", "take")))
    {
      return Method.tabCompleterBoolean(args, "[HP를 최대 HP로 설정]");
    }
    else if (length == 5 && (Method.equals(args[0], "<인수>", "set", "give", "take")))
    {
      return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
    }

    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}

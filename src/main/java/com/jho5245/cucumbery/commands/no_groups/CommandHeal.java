package com.jho5245.cucumbery.commands.no_groups;

import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.SelectorUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CommandHeal implements CommandExecutor, TabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    String usage = cmd.getUsage().replace("/<command> ", ""), consoleUsage = usage.replace("[플레이어 ID]", "<플레이어 ID>");
    switch (cmd.getName().toLowerCase())
    {
      case "heal" -> {
        if (!Method.hasPermission(sender, Permission.CMD_HEAL, true))
        {
          return true;
        }
        if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
        {
          return !(sender instanceof BlockCommandSender);
        }
        if (args.length == 0)
        {
          if (!(sender instanceof Player player))
          {
            MessageUtil.shortArg(sender, 1, args);
            MessageUtil.commandInfo(sender, label, consoleUsage);
            return true;
          }
          double maxHealth = Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();
          if (maxHealth > player.getHealth())
          {
            player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
          }
          MessageUtil.sendMessage(player, Prefix.INFO_HEAL, "HP가 모두 회복되었습니다");
        }
        else if (args.length <= 2)
        {
          boolean hideOutput = false;
          if (args.length == 2)
          {
            if (!args[1].equals("true") && !args[1].equals("false"))
            {
              MessageUtil.wrongBool(sender, 2, args);
              return true;
            }
            if (args[1].equals("true"))
            {
              hideOutput = true;
            }
          }
          Player target = SelectorUtil.getPlayer(sender, args[0]);
          if (target == null)
          {
            return true;
          }
          double maxHealth = Objects.requireNonNull(target.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();
          if (maxHealth > target.getHealth())
          {
            target.setHealth(Objects.requireNonNull(target.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
          }
          if (!hideOutput)
          {
            if (!target.equals(sender))
            {
              MessageUtil.sendMessage(sender, Prefix.INFO_HEAL, target, "의 HP를 모두 회복시켰습니다");
            }
            MessageUtil.sendMessage(target, Prefix.INFO_HEAL, sender, "이(가) 당신의 HP를 모두 회복시켰습니다");
          }
        }
        else
        {
          MessageUtil.longArg(sender, 2, args);
          if (sender instanceof Player)
          {
            MessageUtil.commandInfo(sender, label, usage);
          }
          else
          {
            MessageUtil.commandInfo(sender, label, consoleUsage);
          }
        }
      }
      case "feed" -> {
        if (!Method.hasPermission(sender, Permission.CMD_FEED, true))
        {
          return true;
        }
        if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
        {
          return !(sender instanceof BlockCommandSender);
        }
        if (args.length == 0)
        {
          if (!(sender instanceof Player player))
          {
            MessageUtil.shortArg(sender, 1, args);
            MessageUtil.commandInfo(sender, label, consoleUsage);
            return true;
          }
          player.setFoodLevel(20);
          player.setSaturation(20F);
          MessageUtil.sendMessage(player, Prefix.INFO_HEAL, "허기와 포화도가 충만해졌습니다");
        }
        else if (args.length <= 2)
        {
          boolean hideOutput = false;
          if (args.length == 2)
          {
            if (!args[1].equals("true") && !args[1].equals("false"))
            {
              MessageUtil.wrongBool(sender, 2, args);
              return true;
            }
            if (args[1].equals("true"))
            {
              hideOutput = true;
            }
          }
          Player target = SelectorUtil.getPlayer(sender, args[0]);
          if (target == null)
          {
            return true;
          }
          target.setFoodLevel(20);
          target.setSaturation(20F);
          if (!hideOutput)
          {
            if (!target.equals(sender))
            {
              MessageUtil.sendMessage(sender, Prefix.INFO_HEAL, target, "의 음식 포인트와 포화도를 모두 회복시켰습니다");
            }
            MessageUtil.sendMessage(target, Prefix.INFO_HEAL, sender, "이(가) 당신의 음식 포인트와 포화도를 모두 회복시켰습니다");
          }
        }
        else
        {
          MessageUtil.longArg(sender, 2, args);
          if (sender instanceof Player)
          {
            MessageUtil.commandInfo(sender, label, usage);
          }
          else
          {
            MessageUtil.commandInfo(sender, label, consoleUsage);
          }
          return true;
        }
      }
      case "advancedfeed" -> {
        if (!Method.hasPermission(sender, Permission.CMD_AFEED, true))
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
          MessageUtil.commandInfo(sender, label, usage);
          return true;
        }
        else if (args.length <= 5)
        {
          Player target = SelectorUtil.getPlayer(sender, args[1]);
          if (target == null)
          {
            return true;
          }
          int foodLevel = target.getFoodLevel();
          float saturation = target.getSaturation();
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
          switch (args[0].toLowerCase())
          {
            case "set":
              switch (args[2].toLowerCase())
              {
                case "all" -> {
                  if (!MessageUtil.isDouble(sender, args[3], true))
                  {
                    return true;
                  }
                  double allInput = Double.parseDouble(args[3]);
                  if (!MessageUtil.checkNumberSize(sender, allInput, 0, 20, false, false))
                  {
                    return true;
                  }
                  target.setFoodLevel((int) allInput);
                  target.setSaturation((float) allInput);
                  if (!hideOutput)
                  {
                    if (!target.equals(sender))
                    {
                      MessageUtil.sendMessage(target, Prefix.INFO_HEAL, sender, "이(가) 당신의 음식 포인트와 포화도를 rg255,204;" + Constant.Sosu2.format(allInput) + "&r으로 설정했습니다");
                    }
                    MessageUtil.sendMessage(sender, Prefix.INFO_HEAL, target, "의 음식 포인트와 포화도를 rg255,204;" + Constant.Sosu2.format(allInput) + "&r으로 설정했습니다");
                  }
                }
                case "foodlevel" -> {
                  if (!MessageUtil.isInteger(sender, args[3], true))
                  {
                    return true;
                  }
                  int foodInput = Integer.parseInt(args[3]);
                  if (!MessageUtil.checkNumberSize(sender, foodInput, 0, 20, false, false))
                  {
                    return true;
                  }
                  target.setFoodLevel(foodInput);
                  if (!hideOutput)
                  {
                    if (!target.equals(sender))
                    {
                      MessageUtil.sendMessage(target, Prefix.INFO_HEAL, sender, "이(가) 당신의 음식 포인트를 rg255,204;" + Constant.Jeongsu.format(foodInput) + "&r으로 설정했습니다");
                    }
                    MessageUtil.sendMessage(sender, Prefix.INFO_HEAL, target, "의 음식 포인트를 rg255,204;" + Constant.Jeongsu.format(foodInput) + "&r으로 설정했습니다");
                  }
                }
                case "saturation" -> {
                  if (!MessageUtil.isDouble(sender, args[3], true))
                  {
                    return true;
                  }
                  double saturationInput = Double.parseDouble(args[3]);
                  if (!MessageUtil.checkNumberSize(sender, saturationInput, 0, 20, false, false))
                  {
                    return true;
                  }
                  target.setSaturation((float) saturationInput);
                  if (!hideOutput)
                  {
                    if (!target.equals(sender))
                    {
                      MessageUtil.sendMessage(target, Prefix.INFO_HEAL, sender, "이(가) 당신의 포화도를 rg255,204;" + Constant.Jeongsu.format(saturationInput) + "&r으로 설정했습니다");
                    }
                    MessageUtil.sendMessage(sender, Prefix.INFO_HEAL, target, "의 포화도를 rg255,204;" + Constant.Jeongsu.format(saturationInput) + "&r으로 설정했습니다");
                  }
                }
                default -> {
                  MessageUtil.wrongArg(sender, 3, args);
                  MessageUtil.commandInfo(sender, label, usage);
                  return true;
                }
              }
              break;
            case "give":
              switch (args[2].toLowerCase())
              {
                case "all" -> {
                  if (foodLevel >= 20 && !hideOutput)
                  {
                    MessageUtil.sendError(sender, "더 이상 ", target, "에게 음식 포인트를 지급할 수 없습니다");
                    return true;
                  }
                  if (saturation >= 20D && !hideOutput)
                  {
                    MessageUtil.sendError(sender, "더 이상 ", target, "에게 포화도를 지급할 수 없습니다");
                    return true;
                  }
                  if (!MessageUtil.isDouble(sender, args[3], true))
                  {
                    return true;
                  }
                  double allInput = Double.parseDouble(args[3]);
                  if (!MessageUtil.checkNumberSize(sender, allInput, 0, 20, true, false))
                  {
                    return true;
                  }
                  if (foodLevel + (int) allInput > 20)
                  {
                    target.setFoodLevel(20);
                    if (!hideOutput)
                    {
                      MessageUtil.sendWarn(sender, target, "이(가) 지급받은 음식 포인트가 지급한 음식 포인트보다 적습니다. rg255,204;(&r손해 본 값 : rg255,204;" + (20 - (int) allInput) + "rg255,204;)");
                      if (!target.equals(sender))
                      {
                        MessageUtil.sendMessage(target, Prefix.INFO_HEAL, sender, "이(가) 당신에게 음식 포인트를 rg255,204;" + (20 - foodLevel) + "&r만큼 지급했습니다");
                      }
                      MessageUtil.sendMessage(sender, Prefix.INFO_HEAL, target, "에게 음식 포인트를 rg255,204;" + (20 - foodLevel) + "&r만큼 지급했습니다. rg255,204;(&r현재 음식 포인트 : rg255,204;" + target.getFoodLevel() + "rg255,204;)");
                    }
                  }
                  else
                  {
                    target.setFoodLevel(foodLevel + (int) allInput);
                    if (!hideOutput)
                    {
                      if (!target.equals(sender))
                      {
                        MessageUtil.sendMessage(target, Prefix.INFO_HEAL, sender, "이(가) 당신에게 음식 포인트를 rg255,204;" + ((int) allInput) + "&r만큼 지급했습니다");
                      }
                      MessageUtil.sendMessage(sender, Prefix.INFO_HEAL, target, "에게 음식 포인트를 rg255,204;" + ((int) allInput) + "&r만큼 지급했습니다. rg255,204;(&r현재 음식 포인트 : rg255,204;" + target.getFoodLevel() + "rg255,204;)");
                    }
                  }
                  if (saturation + allInput > 20D)
                  {
                    target.setSaturation(20F);
                    if (!hideOutput)
                    {
                      MessageUtil.sendWarn(sender, target, "이(가) 지급받은 포화도가 지급한 포화도보다 적습니다. rg255,204;(&r손해 본 값 : rg255,204;" + Constant.Sosu2.format(20D - allInput) + "rg255,204;)");
                      if (!target.equals(sender))
                      {
                        MessageUtil.sendMessage(target, Prefix.INFO_HEAL, sender, "이(가) 당신에게 포화도를 rg255,204;" + Constant.Sosu2.format(20D - saturation) + "&r만큼 지급했습니다");
                      }
                      MessageUtil.sendMessage(sender, Prefix.INFO_HEAL,
                              target,
                              "에게 포화도를 rg255,204;" +
                                      Constant.Sosu2.format(20D - saturation) +
                                      "&r만큼 지급했습니다. rg255,204;(&r현재 포화도 : rg255,204;" +
                                      Constant.Sosu2.format(target.getSaturation()) +
                                      "rg255,204;)");
                    }
                  }
                  else
                  {
                    target.setSaturation(saturation + (float) allInput);
                    if (!hideOutput)
                    {
                      if (!target.equals(sender))
                      {
                        MessageUtil.sendMessage(target, Prefix.INFO_HEAL, sender, "이(가) 당신에게 포화도를 rg255,204;" + Constant.Sosu2.format(allInput) + "&r만큼 지급했습니다");
                      }
                      MessageUtil.sendMessage(sender, Prefix.INFO_HEAL,
                              target,
                              "에게 포화도를 rg255,204;" +
                                      Constant.Sosu2.format(allInput) +
                                      "&r만큼 지급했습니다. rg255,204;(&r현재 포화도 : rg255,204;" +
                                      Constant.Sosu2.format(target.getSaturation()) +
                                      "rg255,204;)");
                    }
                  }
                }
                case "foodlevel" -> {
                  if (foodLevel >= 20 && !hideOutput)
                  {
                    MessageUtil.sendError(sender, "더 이상 ", target, "에게 음식 포인트를 지급할 수 없습니다");
                    return true;
                  }
                  if (!MessageUtil.isInteger(sender, args[3], true))
                  {
                    return true;
                  }
                  int foodInput = Integer.parseInt(args[3]);
                  if (!MessageUtil.checkNumberSize(sender, foodInput, 0, 20, true, false))
                  {
                    return true;
                  }
                  if (foodLevel + foodInput > 20)
                  {
                    target.setFoodLevel(20);
                    if (!hideOutput)
                    {
                      MessageUtil.sendWarn(sender, target, "이(가) 지급받은 음식 포인트가 지급한 음식 포인트보다 적습니다. rg255,204;(&r손해 본 값 : rg255,204;" + (20 - foodInput) + "rg255,204;)");
                      if (!target.equals(sender))
                      {
                        MessageUtil.sendMessage(target, Prefix.INFO_HEAL, sender, "이(가) 당신에게 음식 포인트를 rg255,204;" + (20 - foodLevel) + "&r만큼 지급했습니다");
                      }
                      MessageUtil.sendMessage(sender, Prefix.INFO_HEAL, target, "에게 음식 포인트를 rg255,204;" + (20 - foodLevel) + "&r만큼 지급했습니다. rg255,204;(&r현재 음식 포인트 : rg255,204;" + target.getFoodLevel() + "rg255,204;)");
                    }
                  }
                  else
                  {
                    target.setFoodLevel(foodLevel + foodInput);
                    if (!hideOutput)
                    {
                      if (!target.equals(sender))
                      {
                        MessageUtil.sendMessage(target, Prefix.INFO_HEAL, sender, "이(가) 당신에게 음식 포인트를 rg255,204;" + foodInput + "&r만큼 지급했습니다");
                      }
                      MessageUtil.sendMessage(sender, Prefix.INFO_HEAL, target, "에게 음식 포인트를 rg255,204;" + foodInput + "&r만큼 지급했습니다. rg255,204;(&r현재 음식 포인트 : rg255,204;" + target.getFoodLevel() + "rg255,204;)");
                    }
                  }
                }
                case "saturation" -> {
                  if (saturation >= 20D && !hideOutput)
                  {
                    MessageUtil.sendError(sender, "더 이상 ", target, "에게 포화도를 지급할 수 없습니다");
                    return true;
                  }
                  if (!MessageUtil.isDouble(sender, args[3], true))
                  {
                    return true;
                  }
                  double saturationInput = Double.parseDouble(args[3]);
                  if (!MessageUtil.checkNumberSize(sender, saturationInput, 0, 20, true, false))
                  {
                    return true;
                  }
                  if (saturation + saturationInput > 20D)
                  {
                    target.setSaturation(20F);
                    if (!hideOutput)
                    {
                      MessageUtil.sendWarn(sender, target, "이(가) 지급받은 포화도가 지급한 포화도보다 적습니다. rg255,204;(&r손해 본 값 : rg255,204;" + Constant.Sosu2.format(20D - saturationInput) + "rg255,204;)");
                      if (!target.equals(sender))
                      {
                        MessageUtil.sendMessage(target, Prefix.INFO_HEAL, sender, "이(가) 당신에게 포화도를 rg255,204;" + Constant.Sosu2.format(20D - saturation) + "&r만큼 지급했습니다");
                      }
                      MessageUtil.sendMessage(sender, Prefix.INFO_HEAL,
                              target,
                              "에게 포화도를 rg255,204;" +
                                      Constant.Sosu2.format(20D - saturation) +
                                      "&r만큼 지급했습니다. rg255,204;(&r현재 포화도 : rg255,204;" +
                                      Constant.Sosu2.format(target.getSaturation()) +
                                      "rg255,204;)");
                    }
                  }
                  else
                  {
                    target.setSaturation(saturation + (float) saturationInput);
                    if (!hideOutput)
                    {
                      if (!target.equals(sender))
                      {
                        MessageUtil.sendMessage(target, Prefix.INFO_HEAL, sender, "이(가) 당신에게 포화도를 rg255,204;" + Constant.Sosu2.format(saturationInput) + "&r만큼 지급했습니다");
                      }
                      MessageUtil.sendMessage(sender, Prefix.INFO_HEAL,
                              target,
                              "에게 포화도를 rg255,204;" +
                                      Constant.Sosu2.format(saturationInput) +
                                      "&r만큼 지급했습니다. rg255,204;(&r현재 포화도 : rg255,204;" +
                                      Constant.Sosu2.format(target.getSaturation()) +
                                      "rg255,204;)");
                    }
                  }
                }
                default -> {
                  MessageUtil.wrongArg(sender, 3, args);
                  MessageUtil.commandInfo(sender, label, usage);
                  return true;
                }
              }
              break;
            case "take":
              switch (args[2].toLowerCase())
              {
                case "all" -> {
                  if (foodLevel <= 0 && !hideOutput)
                  {
                    MessageUtil.sendError(sender, "더 이상 ", target, "(으)로부터 음식 포인트를 차감할 수 없습니다");
                    return true;
                  }
                  if (saturation <= 0D && !hideOutput)
                  {
                    MessageUtil.sendError(sender, "더 이상 ", target, "(으)로부터 포화도를 차감할 수 없습니다");
                    return true;
                  }
                  if (!MessageUtil.isDouble(sender, args[3], true))
                  {
                    return true;
                  }
                  double allInput = Double.parseDouble(args[3]);
                  if (!MessageUtil.checkNumberSize(sender, allInput, 0, 20, true, false))
                  {
                    return true;
                  }
                  if (foodLevel - (int) allInput < 0)
                  {
                    target.setFoodLevel(0);
                    if (!hideOutput)
                    {
                      MessageUtil.sendWarn(sender, target, "이(가) 차감당한 음식 포인트가 차감시킨 음식 포인트보다 적습니다. rg255,204;(&r손해 본 값 : rg255,204;" + ((int) allInput - foodLevel) + "rg255,204;)");
                      if (!target.equals(sender))
                      {
                        MessageUtil.sendMessage(target, Prefix.INFO_HEAL, sender, "이(가) 당신의 음식 포인트를 rg255,204;" + foodLevel + "&r만큼 차감했습니다");
                      }
                      MessageUtil.sendMessage(sender, Prefix.INFO_HEAL, target, "(으)로부터 음식 포인트를 rg255,204;" + foodLevel + "&r만큼 차감했습니다. rg255,204;(&r현재 음식 포인트 : rg255,204;" + target.getFoodLevel() + "rg255,204;)");
                    }
                  }
                  else
                  {
                    target.setFoodLevel(foodLevel - (int) allInput);
                    if (!hideOutput)
                    {
                      if (!target.equals(sender))
                      {
                        MessageUtil.sendMessage(target, Prefix.INFO_HEAL, sender, "이(가) 당신의 음식 포인트를 rg255,204;" + ((int) allInput) + "&r만큼 차감했습니다");
                      }
                      MessageUtil.sendMessage(sender, Prefix.INFO_HEAL, target, "(으)로부터 음식 포인트를 rg255,204;" + ((int) allInput) + "&r만큼 차감했습니다. rg255,204;(&r현재 음식 포인트 : rg255,204;" + target.getFoodLevel() + "rg255,204;)");
                    }
                  }
                  if (saturation - allInput < 0D)
                  {
                    target.setSaturation(0F);
                    if (!hideOutput)
                    {
                      MessageUtil.sendWarn(sender, target, "이(가) 차감당한 포화도가 차감시킨 포화도보다 적습니다. rg255,204;(&r손해 본 값 : rg255,204;" + Constant.Sosu2.format(allInput - saturation) + "rg255,204;)");
                      if (!target.equals(sender))
                      {
                        MessageUtil.sendMessage(target, Prefix.INFO_HEAL, sender, "이(가) 당신의 포화도를 rg255,204;" + Constant.Sosu2.format(saturation) + "&r만큼 차감했습니다");
                      }
                      MessageUtil.sendMessage(sender, Prefix.INFO_HEAL,
                              target,
                              "(으)로부터 포화도를 rg255,204;" +
                                      Constant.Sosu2.format(saturation) +
                                      "&r만큼 차감했습니다. rg255,204;(&r현재 포화도 : rg255,204;" +
                                      Constant.Sosu2.format(target.getSaturation()) +
                                      "rg255,204;)");
                    }
                  }
                  else
                  {
                    target.setSaturation(saturation - (float) allInput);
                    if (!hideOutput)
                    {
                      if (!target.equals(sender))
                      {
                        MessageUtil.sendMessage(target, Prefix.INFO_HEAL, sender, "이(가) 당신의 포화도를 rg255,204;" + Constant.Sosu2.format(allInput) + "&r만큼 차감했습니다");
                      }
                      MessageUtil.sendMessage(sender, Prefix.INFO_HEAL,
                              target,
                              "(으)로부터 포화도를 rg255,204;" +
                                      Constant.Sosu2.format(allInput) +
                                      "&r만큼 차감했습니다. rg255,204;(&r현재 포화도 : rg255,204;" +
                                      Constant.Sosu2.format(target.getSaturation()) +
                                      "rg255,204;)");
                    }
                  }
                }
                case "foodlevel" -> {
                  if (foodLevel <= 0 && !hideOutput)
                  {
                    MessageUtil.sendError(sender, "더 이상 ", target, "(으)로부터 음식 포인트를 차감할 수 없습니다");
                    return true;
                  }
                  if (!MessageUtil.isInteger(sender, args[3], true))
                  {
                    return true;
                  }
                  int foodInput = Integer.parseInt(args[3]);
                  if (!MessageUtil.checkNumberSize(sender, foodInput, 0, 20, true, false))
                  {
                    return true;
                  }
                  if (foodLevel - foodInput < 0)
                  {
                    target.setFoodLevel(0);
                    if (!hideOutput)
                    {
                      MessageUtil.sendWarn(sender, target, "이(가) 차감당한 음식 포인트가 차감시킨 음식 포인트보다 적습니다. rg255,204;(&r손해 본 값 : rg255,204;" + (foodInput - foodLevel) + "rg255,204;)");
                      if (!target.equals(sender))
                      {
                        MessageUtil.sendMessage(target, Prefix.INFO_HEAL, sender, "이(가) 당신의 음식 포인트를 rg255,204;" + foodLevel + "&r만큼 차감했습니다");
                      }
                      MessageUtil.sendMessage(sender, Prefix.INFO_HEAL, target, "(으)로부터 음식 포인트를 rg255,204;" + foodLevel + "&r만큼 차감했습니다. rg255,204;(&r현재 음식 포인트 : rg255,204;" + target.getFoodLevel() + "rg255,204;)");
                    }
                  }
                  else
                  {
                    target.setFoodLevel(foodLevel - foodInput);
                    if (!hideOutput)
                    {
                      if (!target.equals(sender))
                      {
                        MessageUtil.sendMessage(target, Prefix.INFO_HEAL, sender, "이(가) 당신의 음식 포인트를 rg255,204;" + foodInput + "&r만큼 차감했습니다");
                      }
                      MessageUtil.sendMessage(sender, Prefix.INFO_HEAL, target, "(으)로부터 음식 포인트를 rg255,204;" + foodInput + "&r만큼 차감했습니다. rg255,204;(&r현재 음식 포인트 : rg255,204;" + target.getFoodLevel() + "rg255,204;)");
                    }
                  }
                }
                case "saturation" -> {
                  if (saturation <= 0D && !hideOutput)
                  {
                    MessageUtil.sendError(sender, "더 이상 ", target, "(으)로부터 포화도를 차감할 수 없습니다");
                    return true;
                  }
                  if (!MessageUtil.isDouble(sender, args[3], true))
                  {
                    return true;
                  }
                  double saturationInput = Double.parseDouble(args[3]);
                  if (!MessageUtil.checkNumberSize(sender, saturationInput, 0, 20, true, false))
                  {
                    return true;
                  }
                  if (saturation - saturationInput < 0D)
                  {
                    target.setSaturation(0F);
                    if (!hideOutput)
                    {
                      MessageUtil.sendWarn(sender, target, "이(가) 차감당한 포화도가 차감시킨 포화도보다 적습니다. rg255,204;(&r손해 본 값 : rg255,204;" + Constant.Sosu2.format(saturationInput - saturation) + "rg255,204;)");
                      if (!target.equals(sender))
                      {
                        MessageUtil.sendMessage(target, Prefix.INFO_HEAL, sender, "이(가) 당신의 포화도를 rg255,204;" + Constant.Sosu2.format(saturation) + "&r만큼 차감했습니다");
                      }
                      MessageUtil.sendMessage(sender, Prefix.INFO_HEAL,
                              target,
                              "(으)로부터 포화도를 rg255,204;" +
                                      Constant.Sosu2.format(saturation) +
                                      "&r만큼 차감했습니다. rg255,204;(&r현재 포화도 : rg255,204;" +
                                      Constant.Sosu2.format(target.getSaturation()) +
                                      "rg255,204;)");
                    }
                  }
                  else
                  {
                    target.setSaturation(saturation - (float) saturationInput);
                    if (!hideOutput)
                    {
                      if (!target.equals(sender))
                      {
                        MessageUtil.sendMessage(target, Prefix.INFO_HEAL, sender, "이(가) 당신의 포화도를 rg255,204;" + Constant.Sosu2.format(saturationInput) + "&r만큼 차감했습니다");
                      }
                      MessageUtil.sendMessage(sender, Prefix.INFO_HEAL,
                              target,
                              "(으)로부터 포화도를 rg255,204;" +
                                      Constant.Sosu2.format(saturationInput) +
                                      "&r만큼 차감했습니다. rg255,204;(&r현재 포화도 : rg255,204;" +
                                      Constant.Sosu2.format(target.getSaturation()) +
                                      "rg255,204;)");
                    }
                  }
                }
                default -> {
                  MessageUtil.wrongArg(sender, 3, args);
                  MessageUtil.commandInfo(sender, label, usage);
                  return true;
                }
              }
              break;
            default:
              MessageUtil.wrongArg(sender, 3, args);
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
      }
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

    switch (cmd.getName())
    {
      case "heal", "feed" -> {
        if (length == 1)
        {
          return Method.tabCompleterPlayer(sender, args);
        }
        else if (length == 2)
        {
          return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
        }
      }
      case "advancedfeed" -> {
        if (length == 1)
        {
          return Method.tabCompleterList(args, "<인수>", "set", "give", "take");
        }
        else if (length == 2)
        {
          return Method.tabCompleterPlayer(sender, args);
        }
        else if (length == 3)
        {
          return Method.tabCompleterList(args, "<인수>", "all", "foodlevel", "saturation");
        }
        else if (length == 4)
        {
          if (args[0].equals("set"))
          {
            return Method.tabCompleterDoubleRadius(args, 0, 20, "<수치>");
          }
          return Method.tabCompleterDoubleRadius(args, 0, true, 20, false, "<수치>");
        }
        else if (length == 5)
        {
          return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
        }
      }
    }
    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}

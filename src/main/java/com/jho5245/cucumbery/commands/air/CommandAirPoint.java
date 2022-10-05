package com.jho5245.cucumbery.commands.air;

import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.SelectorUtil;
import com.jho5245.cucumbery.util.storage.component.util.sendercomponent.SenderComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandAirPoint implements CommandExecutor, TabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_AIRPOINT, true))
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
    else if (args.length <= 4)
    {
      Player target = SelectorUtil.getPlayer(sender, args[1]);
      if (target == null)
      {
        return true;
      }
      if (!MessageUtil.isInteger(sender, args[2], true))
      {
        return true;
      }
      int value = Integer.parseInt(args[2]);
      if (!MessageUtil.checkNumberSize(sender, value, 0, Integer.MAX_VALUE, false, false))
      {
        return true;
      }
      int air = target.getRemainingAir();
      int maxAir = target.getMaximumAir();
      boolean hideMessage = args.length == 4 && args[3].equalsIgnoreCase("true");
      switch (args[0].toLowerCase())
      {
        case "set" -> {
          if (value > air)
          {
            target.setRemainingAir(maxAir);
            if (!hideMessage)
            {
              MessageUtil.sendWarn(sender,"설정한 산소량이 %s의 최대 산소량보다 많습니다 (손해 본 값 : %s)", target, Constant.THE_COLOR_HEX + (value - maxAir));
              MessageUtil.sendMessage(target, Prefix.INFO_AIR, sender, "이 당신의 산소량을 rg255,204;" + maxAir + "&r으로 설정했습니다 (rg255,204;산소량&r : rg255,204;" + target.getRemainingAir() + "&r / rg255,204;" + maxAir + "&r)");
              MessageUtil.sendMessage(sender, Prefix.INFO_AIR, target, "의 산소량을 rg255,204;" + maxAir + "&r으로 설정했습니다 (rg255,204;산소량&r : rg255,204;" + target.getRemainingAir() + "&r / rg255,204;" + maxAir + "&r)");
            }
            return true;
          }
          target.setRemainingAir(value);
          if (!hideMessage)
          {
            MessageUtil.sendMessage(sender, Prefix.INFO_AIR, target, "의 산소량을 rg255,204;" + value + "&r으로 설정했습니다 (rg255,204;산소량&r : rg255,204;" + target.getRemainingAir() + "&r / rg255,204;" + maxAir + "&r)");
          }
        }
        case "give" -> {
          if (air >= maxAir)
          {
            MessageUtil.sendError(sender, "더 이상 %s에게 산소를 지급할 수 없습니다", target);
            return true;
          }
          if (!MessageUtil.checkNumberSize(sender, value, 1, Integer.MAX_VALUE, false, false))
          {
            return true;
          }
          if (value + air > maxAir)
          {
            if (!hideMessage)
            {
              MessageUtil.sendWarn(sender, "지급한 산소량이 %s의 최대 산소량보다 많습니다 (rg255,204;손해 본 값&r : rg255,204;" + (value + air - maxAir) + "&r)", target);
            }
            target.setRemainingAir(maxAir);
            if (!hideMessage)
            {
              MessageUtil.sendMessage(target,
                      Prefix.INFO_AIR, sender, "이 당신에게 산소를 rg255,204;" + (maxAir - air) + "&r만큼 지급했습니다 (rg255,204;산소량&r : rg255,204;" + target.getRemainingAir() + "&r / rg255,204;" + maxAir + "&r)");
              MessageUtil.sendMessage(sender, Prefix.INFO_AIR, target, "&r에게 산소를 rg255,204;" + (maxAir - air) + "&r만큼 지급했습니다 (rg255,204;산소량&r : rg255,204;" + target.getRemainingAir() + "&r / rg255,204;" + maxAir + "&r)");
            }
          }
          else
          {
            target.setRemainingAir(air + value);
            if (!hideMessage)
            {
              MessageUtil.sendMessage(target, Prefix.INFO_AIR, SenderComponentUtil.senderComponent(sender) + "이 당신에게 산소를 rg255,204;" + value + "&r만큼 지급했습니다 (rg255,204;산소량&r : rg255,204;" + target.getRemainingAir() + "&r / rg255,204;" + maxAir + "&r)");
              MessageUtil.sendMessage(sender, Prefix.INFO_AIR, target, "&r에게 산소를 rg255,204;" + value + "&r만큼 지급했습니다 (rg255,204;산소량&r : rg255,204;" + target.getRemainingAir() + "&r / rg255,204;" + maxAir + "&r)");
            }
          }
        }
        case "take" -> {
          if (air <= 0)
          {
            MessageUtil.sendError(sender, "더 이상 %s의 산소를 차감할 수 없습니다", target);
            return true;
          }
          if (!MessageUtil.checkNumberSize(sender, value, 1, Integer.MAX_VALUE, false, false))
          {
            return true;
          }
          if (air - value < 0)
          {
            if (!hideMessage)
            {
              MessageUtil.sendWarn(sender, "차감한 산소량이 ", target, "의 산소량보다 많습니다 (rg255,204;손해 본 값&r : rg255,204;" + (value + air - maxAir) + "&r)");
            }
            target.setRemainingAir(0);
            if (!hideMessage)
            {
              MessageUtil.sendMessage(target,
                      Prefix.INFO_AIR, sender, "이 당신의 산소량을 rg255,204;" + (air - 1) + "&r만큼 차감시켰습니다 (rg255,204;산소량&r : rg255,204;" + target.getRemainingAir() + "&r / rg255,204;" + maxAir + "&r)");
              MessageUtil.sendMessage(sender, Prefix.INFO_AIR, target, "의 산소량을 rg255,204;" + (air - 1) + "&r만큼 차감시켰습니다 (rg255,204;산소량&r : rg255,204;" + target.getRemainingAir() + "&r / rg255,204;" + maxAir + "&r)");
            }
          }
          else
          {
            target.setRemainingAir(air - value);
            if (!hideMessage)
            {
              MessageUtil.sendMessage(target, Prefix.INFO_AIR, sender, "이 당신의 산소량을 rg255,204;" + value + "&r만큼 차감시켰습니다 (rg255,204;산소량&r : rg255,204;" + target.getRemainingAir() + "&r / rg255,204;" + maxAir + "&r)");
              MessageUtil.sendMessage(sender, Prefix.INFO_AIR, target, "&r의 산소량을 rg255,204;" + value + "&r만큼 차감시켰습니다 (rg255,204;산소량&r : rg255,204;" + target.getRemainingAir() + "&r / rg255,204;" + maxAir + "&r)");
            }
          }
        }
        default -> {
          MessageUtil.wrongArg(sender, 1, args);
          MessageUtil.commandInfo(sender, label, usage);
          return true;
        }
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
    else if (length == 2)
    {
      return Method.tabCompleterPlayer(sender, args);
    }
    else if (length == 3)
    {
      return Method.tabCompleterIntegerRadius(args, 1, 300, "<수치>");
    }
    else if (length == 4)
    {
      return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
    }
    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}

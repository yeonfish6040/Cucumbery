package com.jho5245.cucumbery.commands.air;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AirPoint implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_AIRPOINT, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return sender instanceof Player;
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
              MessageUtil.sendWarn(sender, "설정한 산소량이 ", target, "의 최대 산소량보다 많습니다. (&e손해 본 값&r : &e" + (value - maxAir) + "&r)");
              MessageUtil.sendMessage(target, Prefix.INFO_AIR, sender, "이 당신의 산소량을 &e" + maxAir + "&r으로 설정하였습니다. (&e산소량&r : &e" + target.getRemainingAir() + "&r / &e" + maxAir + "&r)");
              MessageUtil.sendMessage(sender, Prefix.INFO_AIR, target, "의 산소량을 &e" + maxAir + "&r으로 설정하였습니다. (&e산소량&r : &e" + target.getRemainingAir() + "&r / &e" + maxAir + "&r)");
            }
            return true;
          }
          target.setRemainingAir(value);
          if (!hideMessage)
          {
            MessageUtil.sendMessage(sender, Prefix.INFO_AIR, target, "의 산소량을 &e" + value + "&r으로 설정하였습니다. (&e산소량&r : &e" + target.getRemainingAir() + "&r / &e" + maxAir + "&r)");
          }
        }
        case "give" -> {
          if (air >= maxAir)
          {
            MessageUtil.sendError(sender, "더 이상 ", target, "에게 산소를 지급할 수 없습니다.");
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
              MessageUtil.sendWarn(sender, "지급한 산소량이 ", target, "의 최대 산소량보다 많습니다. (&e손해 본 값&r : &e" + (value + air - maxAir) + "&r)");
            }
            target.setRemainingAir(maxAir);
            if (!hideMessage)
            {
              MessageUtil.sendMessage(target,
                      Prefix.INFO_AIR, sender, "이 당신에게 산소를 &e" + (maxAir - air) + "&r만큼 지급하였습니다. (&e산소량&r : &e" + target.getRemainingAir() + "&r / &e" + maxAir + "&r)");
              MessageUtil.sendMessage(sender, Prefix.INFO_AIR, target, "&r에게 산소를 &e" + (maxAir - air) + "&r만큼 지급하였습니다. (&e산소량&r : &e" + target.getRemainingAir() + "&r / &e" + maxAir + "&r)");
            }
          }
          else
          {
            target.setRemainingAir(air + value);
            if (!hideMessage)
            {
              MessageUtil.sendMessage(target, Prefix.INFO_AIR, ComponentUtil.senderComponent(sender) + "이 당신에게 산소를 &e" + value + "&r만큼 지급하였습니다. (&e산소량&r : &e" + target.getRemainingAir() + "&r / &e" + maxAir + "&r)");
              MessageUtil.sendMessage(sender, Prefix.INFO_AIR, target, "&r에게 산소를 &e" + value + "&r만큼 지급하였습니다. (&e산소량&r : &e" + target.getRemainingAir() + "&r / &e" + maxAir + "&r)");
            }
          }
        }
        case "take" -> {
          if (air <= 0)
          {
            MessageUtil.sendError(sender, "더 이상 ", target, "의 산소를 차감할 수 없습니다.");
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
              MessageUtil.sendWarn(sender, "차감한 산소량이 ", target, "의 산소량보다 많습니다. (&e손해 본 값&r : &e" + (value + air - maxAir) + "&r)");
            }
            target.setRemainingAir(0);
            if (!hideMessage)
            {
              MessageUtil.sendMessage(target,
                      Prefix.INFO_AIR, sender, "이 당신의 산소량을 &e" + (air - 1) + "&r만큼 차감시켰습니다. (&e산소량&r : &e" + target.getRemainingAir() + "&r / &e" + maxAir + "&r)");
              MessageUtil.sendMessage(sender, Prefix.INFO_AIR, target, "의 산소량을 &e" + (air - 1) + "&r만큼 차감시켰습니다. (&e산소량&r : &e" + target.getRemainingAir() + "&r / &e" + maxAir + "&r)");
            }
          }
          else
          {
            target.setRemainingAir(air - value);
            if (!hideMessage)
            {
              MessageUtil.sendMessage(target, Prefix.INFO_AIR, sender, "이 당신의 산소량을 &e" + value + "&r만큼 차감시켰습니다. (&e산소량&r : &e" + target.getRemainingAir() + "&r / &e" + maxAir + "&r)");
              MessageUtil.sendMessage(sender, Prefix.INFO_AIR, target, "&r의 산소량을 &e" + value + "&r만큼 차감시켰습니다. (&e산소량&r : &e" + target.getRemainingAir() + "&r / &e" + maxAir + "&r)");
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
}

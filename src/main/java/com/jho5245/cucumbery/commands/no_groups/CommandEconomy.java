package com.jho5245.cucumbery.commands.no_groups;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.*;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandEconomy implements CucumberyCommandExecutor
{
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_ECONOMY, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    if (!Cucumbery.using_Vault_Economy)
    {
      MessageUtil.sendError(sender, "rg255,204;Vault&r 플러그인을 사용하고 있지 않습니다");
      return true;
    }
    if (args.length < 3)
    {
      MessageUtil.shortArg(sender, 3, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return true;
    }
    else if (args.length <= 4)
    {
      if (!MessageUtil.isDouble(sender, args[2], true))
      {
        return true;
      }
      boolean hideOuput = false;
      if (args.length == 4)
      {
        if (!MessageUtil.isBoolean(sender, args, 4, true))
        {
          return true;
        }
        hideOuput = Boolean.parseBoolean(args[3]);
      }
      if (!MessageUtil.isDouble(sender, args[2], true))
      {
        return true;
      }
      double input = Double.parseDouble(args[2]);
      if (args[0].equals("set") && !MessageUtil.checkNumberSize(sender, input, 0, 10_000_000_000_000d))
      {
        return true;
      }
      if (Method.equals(args[0], "take", "give") && !MessageUtil.checkNumberSize(sender, input, 0, 10_000_000_000_000d, true, true))
      {
        return true;
      }
      OfflinePlayer offlinePlayer = SelectorUtil.getOfflinePlayer(sender, args[1]);
      if (offlinePlayer == null)
      {
        return true;
      }
      Player player = offlinePlayer.getPlayer();
      double balance = Cucumbery.eco.getBalance(offlinePlayer);
      String inputDisplay = Constant.Sosu2.format(input);
      switch (args[0])
      {
        case "give" ->
        {
          Cucumbery.eco.depositPlayer(offlinePlayer, input);
          if (!hideOuput)
          {
            String balanceDisplay = Constant.Sosu2.format(Cucumbery.eco.getBalance(offlinePlayer));
            if (player != null && !player.equals(sender))
            {
              MessageUtil.sendMessage(player, Prefix.INFO_ECONOMY, sender, "이 당신에게 rg255,204;" + inputDisplay + "원&r을 지급했습니다");
              MessageUtil.sendMessage(player, Prefix.INFO_ECONOMY, "현재 소지 금액 : rg255,204;" + balanceDisplay + "원");
            }
            MessageUtil.sendMessage(sender, Prefix.INFO_ECONOMY, player != null ? player : offlinePlayer, "에게 rg255,204;" + inputDisplay + "원&r을 지급했습니다");
            MessageUtil.sendMessage(sender, Prefix.INFO_ECONOMY, player != null ? player : offlinePlayer, "의 소지 금액 : rg255,204;" + balanceDisplay + "원");
          }
        }
        case "take" ->
        {
          Cucumbery.eco.withdrawPlayer(offlinePlayer, input);
          if (!hideOuput)
          {
            String balanceDisplay = Constant.Sosu2.format(Cucumbery.eco.getBalance(offlinePlayer));
            if (player != null && !player.equals(sender))
            {
              MessageUtil.sendMessage(player, Prefix.INFO_ECONOMY, sender, "이 당신에게서 rg255,204;" + inputDisplay + "원&r을 차감했습니다");
              MessageUtil.sendMessage(player, Prefix.INFO_ECONOMY, "현재 소지 금액 : rg255,204;" + balanceDisplay + "원");
            }
            MessageUtil.sendMessage(sender, Prefix.INFO_ECONOMY, player != null ? player : offlinePlayer, "에게서 rg255,204;" + inputDisplay + "원&r을 차감했습니다");
            MessageUtil.sendMessage(sender, Prefix.INFO_ECONOMY, player != null ? player : offlinePlayer, "의 소지 금액 : rg255,204;" + balanceDisplay + "원");
          }
        }
        case "set" ->
        {
          Cucumbery.eco.withdrawPlayer(offlinePlayer, balance);
          Cucumbery.eco.depositPlayer(offlinePlayer, input);
          if (!hideOuput)
          {
            if (player != null && !player.equals(sender))
            {
              MessageUtil.sendMessage(player, Prefix.INFO_ECONOMY, sender, "이 당신의 소지 금액을 rg255,204;" + inputDisplay + "원&r으로 설정했습니다");
            }
            MessageUtil.sendMessage(sender, Prefix.INFO_ECONOMY, player != null ? player : offlinePlayer, "의 소지 금액을 rg255,204;" + inputDisplay + "원&r으로 설정했습니다");
          }
        }
        default ->
        {
          MessageUtil.wrongArg(sender, 2, args);
          MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
          return true;
        }
      }
    }
    else
    {
      MessageUtil.longArg(sender, 4, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return true;
    }
    return true;
  }

  @Override
  public @NotNull List<Completion> completion(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args, @NotNull Location location)
  {
    if (!Cucumbery.using_Vault_Economy)
    {
      return CommandTabUtil.errorMessage("Vault 플러그인을 사용하고 있지 않습니다 (Economy 기능 활성화 안됨)");
    }
    int length = args.length;
    if (length == 1)
    {
      return CommandTabUtil.tabCompleterList(args, "<인수>", false, "give", "set", "take");
    }
    else if (length == 2)
    {
      if (label.equals("ceco"))
      {
        return CommandTabUtil.tabCompleterPlayer(sender, args, "<플레이어>");
      }
      return CommandTabUtil.tabCompleterOfflinePlayer(sender, args, "<플레이어>");
    }
    else if (length == 3)
    {
      switch (args[0])
      {
        case "set" ->
        {
          return CommandTabUtil.tabCompleterDoubleRadius(args, 0, 10_000_000_000_000d, "<금액>");
        }
        case "give", "take" ->
        {
          return CommandTabUtil.tabCompleterDoubleRadius(args, 0, true, 10_000_000_000_000d, false, "<금액>");
        }
      }
    }
    else if (length == 4)
    {
      return CommandTabUtil.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
    }
    return CommandTabUtil.ARGS_LONG;
  }
}

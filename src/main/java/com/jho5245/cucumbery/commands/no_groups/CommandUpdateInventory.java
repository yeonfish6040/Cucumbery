package com.jho5245.cucumbery.commands.no_groups;

import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.SelectorUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandUpdateInventory implements CommandExecutor, TabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_UPDATE_INVENTORY, true))
    {
      return !(sender instanceof BlockCommandSender);
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    if (args.length == 0)
    {
      if (sender instanceof Player player)
      {
        player.performCommand(label + " " + player.getName());
      }
      else
      {
        MessageUtil.shortArg(sender, 1, args);
        MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      }
      return !(sender instanceof BlockCommandSender);
    }
    else if (args.length <= 2)
    {
      if (args.length == 2 && !MessageUtil.isBoolean(sender, args, 2, true))
      {
        return !(sender instanceof BlockCommandSender);
      }
      List<Player> players = SelectorUtil.getPlayers(sender, args[0]);
      if (players == null)
      {
        return !(sender instanceof BlockCommandSender);
      }
      boolean hideOutput = args.length == 2 && args[1].equals("true");
      for (Player player : players)
      {
        ItemStackUtil.updateInventory(player);
        if (!sender.equals(player) && !hideOutput)
        {
          MessageUtil.info(player, ComponentUtil.translate("%s이(가) 당신의 인벤토리를 업데이트 했습니다", sender));
        }
      }
      if (!hideOutput)
      {
        MessageUtil.info(sender, ComponentUtil.translate("%s의 인벤토리를 업데이트 했습니다", players));
        MessageUtil.sendAdminMessage(sender, new ArrayList<>(players), "%s의 인벤토리를 업데이트 했습니다", players);
      }
    }
    else
    {
      MessageUtil.longArg(sender, 2, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return !(sender instanceof BlockCommandSender);
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
      return Method.tabCompleterPlayer(sender, args, "<플레이어>", true);
    }
    else if (length == 2)
    {
      return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
    }

    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}

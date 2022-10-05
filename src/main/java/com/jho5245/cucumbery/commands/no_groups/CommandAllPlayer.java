package com.jho5245.cucumbery.commands.no_groups;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.util.no_groups.CommandTabUtil;
import com.jho5245.cucumbery.util.no_groups.CucumberyCommandExecutor;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Constant.AllPlayer;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.CustomConfigType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandAllPlayer implements CucumberyCommandExecutor
{
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_ALLPLAYER, true))
    {
      return true;
    }
		if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
		{
			return !(sender instanceof BlockCommandSender);
		}
    String usage = cmd.getUsage().replace("<command>", label);
    if (args.length < 1)
    {
      MessageUtil.shortArg(sender, 1, args);
      MessageUtil.info(sender, usage);
      return true;
    }
    else if (args.length <= 2)
    {
      Constant.AllPlayer key;
      try
      {
        key = Constant.AllPlayer.valueOf(args[0].toUpperCase());
      }
      catch (Exception e)
      {
        MessageUtil.wrongArg(sender, 1, args);
        return true;
      }
      boolean bool;
      String keyString = key.getKey().replace("-", " ");
      CustomConfig customConfig = CustomConfig.getCustomConfig(CustomConfigType.ALLPLAYER);
      YamlConfiguration config = customConfig.getConfig();
      if (args.length == 2)
      {
        switch (args[1])
        {
          case "true" -> bool = true;
          case "false" -> bool = false;
          case "check" -> {
            bool = key.isEnabled();
            MessageUtil.sendMessage(sender, Prefix.INFO_ALLPLAYER, "현재 %s을(를) 할 수 %s 상태입니다", Constant.THE_COLOR_HEX + keyString, bool ? "&c없는" : "&a있는");
            return true;
          }
          default -> {
            MessageUtil.wrongArg(sender, 2, args);
            return true;
          }
        }
      }
      else
      {
        bool = !config.getBoolean(key.getKey());
      }
      config.set(key.getKey(), bool);
      customConfig.saveConfig();
      Variable.allPlayerConfig = customConfig.getConfig();
      MessageUtil.sendMessage(sender, Prefix.INFO_ALLPLAYER, "모든 플레이어가 %s을(를) 할 수 %s 했습니다", Constant.THE_COLOR_HEX + keyString, bool ? "&c없도록" : "&a있도록");
      for (Player player : Bukkit.getServer().getOnlinePlayers())
      {
        if (!player.equals(sender))
        {
          MessageUtil.sendMessage(player, Prefix.INFO_ALLPLAYER, "지금부터 %s을(를) 할 수 %s 됩니다", Constant.THE_COLOR_HEX + keyString, bool ? "&c없게" : "&a있게");
        }
      }
    }
    else
    {
      MessageUtil.longArg(sender, 2, args);
      MessageUtil.info(sender, usage);
      return true;
    }
    return true;
  }

  @Override
  public @NotNull List<Completion> completion(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args, @NotNull Location location)
  {
    int length = args.length;

    if (length == 1)
    {
      return CommandTabUtil.tabCompleterList(args, AllPlayer.values(), "<통제 범주>");
    }
    else if (length == 2)
    {
      List<Completion> list = CommandTabUtil.tabCompleterList(args, "<인수>", false, "check"),
      list1 = CommandTabUtil.tabCompleterBoolean(args, "<인수>");
      return CommandTabUtil.sortError(list, list1);
    }

    return CommandTabUtil.ARGS_LONG;
  }
}

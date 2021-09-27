package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.MessageUtil.ConsonantType;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.CustomConfig;
import com.jho5245.cucumbery.util.storage.CustomConfig.CustomConfigType;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandAllPlayer implements CommandExecutor, TabCompleter
{
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
            MessageUtil.sendMessage(sender, Prefix.INFO_ALLPLAYER, "현재 &e" + keyString + "&r" + MessageUtil.getFinalConsonant(keyString, ConsonantType.을를) + " 할 수 " + (!bool ? "&a있는" : "&c없는") + "&r 상태입니다.");
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
      MessageUtil.sendMessage(sender, Prefix.INFO_ALLPLAYER, "모든 플레이어가 &e" + keyString + "&r" + MessageUtil.getFinalConsonant(keyString, ConsonantType.을를) + " 할 수 " + (!bool ? "&a있도록" : "&c없도록") + "&r 하였습니다.");
      for (Player player : Bukkit.getServer().getOnlinePlayers())
      {
        if (!player.equals(sender))
        {
          MessageUtil.sendMessage(player, Prefix.INFO_ALLPLAYER, "지금부터 &e" + keyString + "&r" + MessageUtil.getFinalConsonant(keyString, ConsonantType.을를) + " 할 수 " + (!bool ? "&a있게" : "&c없게") + "&r 됩니다.");
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
  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(true, args), true))
    {
      return Collections.singletonList(args[0]);
    }
    int length = args.length;

    if (length == 1)
    {
      return Method.tabCompleterList(args, Constant.AllPlayer.values(), "<통제 범주>");
    }
    else if (length == 2)
    {
      return Method.tabCompleterList(args, "<인수>", "true", "false", "check");
    }

    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}

package com.jho5245.cucumbery.commands.debug;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.command.*;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandCheckConfig implements CommandExecutor, TabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_CHECK_CONFIG, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    if (args.length == 0)
    {
      MessageUtil.shortArg(sender, 1, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return true;
    }
    String directory = MessageUtil.listToString(" ", args);
    Object value = Cucumbery.config.get(directory);
    String display = Cucumbery.config.contains(directory) ? Constant.THE_COLOR_HEX + value + "&r, 자료형 : rg255,204;" + (value != null ? value.getClass().getName() : null) : "&c없음";
    MessageUtil.info(sender, Constant.THE_COLOR_HEX + directory + " &r디렉터리 값 : " + display);
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
      ConfigurationSection section = Cucumbery.config.getConfigurationSection("");
      if (section != null)
      {
        return Method.tabCompleterList(args, section.getKeys(true), "<키>", true);
      }
    }
    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}

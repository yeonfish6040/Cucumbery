package com.jho5245.cucumbery.commands.no_groups;

import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.SelectorUtil;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig;
import com.jho5245.cucumbery.util.storage.data.Permission;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandForceChat implements CommandExecutor, TabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_FORCECHAT, true))
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
      MessageUtil.shortArg(sender, 2, args);
      MessageUtil.commandInfo(sender, label, usage);
      return true;
    }
    Player target = SelectorUtil.getPlayer(sender, args[0]);
    if (target == null)
    {
      return true;
    }
    String msg = MessageUtil.listToString(" ", 2, args.length, args);

    boolean isCommand = msg.startsWith("/");

    boolean playChatSound = CustomConfig.UserData.LISTEN_COMMAND.getBoolean(target);

    if (isCommand && playChatSound)
    {
      CustomConfig.UserData.LISTEN_COMMAND.set(target, false);
    }

    boolean hideOutput = false;
    if (!args[1].equals("true") && !args[1].equals("false"))
    {
      MessageUtil.wrongBool(sender, 2, args);
      return true;
    }
    if (args[1].equals("true"))
    {
      hideOutput = true;
    }
    if (msg.startsWith("op:"))
    {
      boolean isOp = target.isOp();
      msg = msg.substring(3);
      if (!isOp)
      {
        target.setOp(true);
      }
      target.chat(msg);
      if (!isOp)
      {
        target.setOp(false);
      }
    }
    else
    {
      target.chat(msg);
    }

    if (isCommand && playChatSound)
    {
      CustomConfig.UserData.LISTEN_COMMAND.set(target, true);
    }
    if (!hideOutput)
    {
      if (!target.equals(sender))
      {
        MessageUtil.info(target, sender, "이 당신에게 &e" + msg + "&r 메시지를 강제로 채팅시켰습니다");
      }
      MessageUtil.info(sender, target, "에게 &e" + msg + "&r 메시지를 강제로 채팅시켰습니다");
    }
    return true;
  }

  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    int length = args.length;
    if (length == 1)
    {
      return Method.tabCompleterPlayer(sender, args);
    }
    else if (length == 2)
    {
      return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
    }
    else
    {
      if (args.length == 3)
      {
        return Method.tabCompleterList(args, "<메시지>", true, "<메시지>", "op:");
      }
      return Method.tabCompleterList(args, "[메시지]", true);
    }
  }
}

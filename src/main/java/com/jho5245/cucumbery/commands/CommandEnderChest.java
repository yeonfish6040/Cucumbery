package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.storage.SoundPlay;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandEnderChest implements CommandExecutor, TabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_ENDERCHEST, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    String usage = cmd.getUsage().replace("/<command> ", ""), consoleUsage = usage.replace("[상자 주인 ID]", "<상자 주인 ID>");
    if (args.length == 0)
    {
      if (!(sender instanceof Player player))
      {
        MessageUtil.shortArg(sender, 1, args);
        MessageUtil.commandInfo(sender, label, consoleUsage);
        return true;
      }
      player.openInventory(player.getEnderChest());
      MessageUtil.sendMessage(player, Prefix.INFO_ENDERCHEST, ComponentUtil.createTranslate("엔더 상자를 엽니다."));
      SoundPlay.playSound(player, Sound.BLOCK_ENDER_CHEST_OPEN, SoundCategory.BLOCKS);
    }
    else if (args.length == 1)
    {
      Player target = SelectorUtil.getPlayer(sender, args[0]);
      if (target == null)
      {
        return true;
      }
      if (sender instanceof Player player)
      {
        player.openInventory(target.getEnderChest());
        SoundPlay.playSound(player, Sound.BLOCK_ENDER_CHEST_OPEN, SoundCategory.BLOCKS);
        MessageUtil.sendMessage(player, Prefix.INFO_ENDERCHEST, ComponentUtil.createTranslate("%s의 엔더 상자를 엽니다.", target));
        return true;
      }
      target.openInventory(target.getEnderChest());
      SoundPlay.playSound(target, Sound.BLOCK_ENDER_CHEST_OPEN, SoundCategory.BLOCKS);
      MessageUtil.sendMessage(sender, Prefix.INFO_ENDERCHEST, ComponentUtil.createTranslate("%s에게 %s의 엔더 상자를 열어주었습니다.", target, target));
      if (!target.equals(sender))
      {
        MessageUtil.sendMessage(target, Prefix.INFO_ENDERCHEST, ComponentUtil.createTranslate("%s이(가) %s의 엔더 상자를 열어주었습니다.", sender, target));
      }
    }
    else if (args.length <= 3)
    {
      boolean hideOutput = false;
      if (args.length == 3)
      {
        if (!args[2].equals("true") && !args[2].equals("false"))
        {
          MessageUtil.wrongBool(sender, 3, args);
          return true;
        }
        if (args[2].equals("true"))
        {
          hideOutput = true;
        }
      }
      Player target = SelectorUtil.getPlayer(sender, args[0]), target2 = SelectorUtil.getPlayer(sender, args[1]);
      if (target == null || target2 == null)
      {
        return true;
      }
      target.openInventory(target2.getEnderChest());
      if (!hideOutput)
      {
        SoundPlay.playSound(target, Sound.BLOCK_ENDER_CHEST_OPEN, SoundCategory.BLOCKS);
        MessageUtil.sendMessage(sender, Prefix.INFO_ENDERCHEST, ComponentUtil.createTranslate("%s에게 %s의 엔더 상자를 열어주었습니다.", target, target2));
        if (!target.equals(sender))
        {
          MessageUtil.sendMessage(target, Prefix.INFO_ENDERCHEST, ComponentUtil.createTranslate("%s이(가) 당신에게 %s의 엔더 상자를 열어주었습니다.", sender, target2));
        }
      }
    }
    else
    {
      MessageUtil.longArg(sender, 3, args);
      if (sender instanceof Player)
      {
        MessageUtil.commandInfo(sender, label, usage);
        return true;
      }
      MessageUtil.commandInfo(sender, label, consoleUsage);
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
      return Method.tabCompleterPlayer(sender, args);
    }
    else if (length == 2)
    {
      return Method.tabCompleterPlayer(sender, args);
    }
    else if (length == 3)
    {
      return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
    }

    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}

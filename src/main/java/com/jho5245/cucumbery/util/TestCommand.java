package com.jho5245.cucumbery.util;

import com.jho5245.cucumbery.customeffect.CustomEffect;
import com.jho5245.cucumbery.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.customeffect.CustomEffectType;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("all")
public class TestCommand implements CommandExecutor, TabCompleter
{
  Integer number = null;
  int count = 0;

  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    args = MessageUtil.wrapWithQuote(args);
    if (!Method.hasPermission(sender, "asdf", true))
    {
      return true;
    }
    try
    {
      if (args.length == 0)
      {
        sender.sendMessage("도움말");
        sender.sendMessage("/testcommand start");
        sender.sendMessage("/testcommand stop");
        sender.sendMessage("/testcommand guess <숫자>");
        sender.sendMessage("/testcommand admin");
      }
      else
      {
        switch (args[0])
        {
          case "start" -> {
            if (number != null)
            {
              sender.sendMessage("이미 게임이 진행중입니다");
              return true;
            }
            number = (int) (Math.random() * 99 + 1);
            sender.sendMessage("게임을 시작합니다. 숫자는 " + number + "입니다");
          }
          case "stop" -> {
            if (number == null)
            {
              sender.sendMessage("게임이 진행중이지 않습니다");
              return true;
            }
            sender.sendMessage("게임을 중지합니다. 숫자는 " + number + "였습니다");
            number = null;
          }
          case "admin" -> {
            if (number == null)
            {
              sender.sendMessage("게임이 진행중이지 않습니다");
              return true;
            }
            sender.sendMessage("숫자는 " + number + "입니다");
          }
          case "guess" -> {

            if (number == null)
            {
              sender.sendMessage("게임이 진행중이지 않습니다");
              return true;
            }

            if (args.length == 1)
            {
              sender.sendMessage("숫자를 입력해주세요: /testcommand guess <숫자>");
              return true;
            }

            int input;
            try
            {
              input = Integer.parseInt(args[1]);
            }
            catch (NumberFormatException exception)
            {
              sender.sendMessage("숫자만 입력할 수 있습니다");
              return true;
            }
            count++;
            if (input > number)
            {
              sender.sendMessage("더 작습니다");
            }
            else if (input < number)
            {
              sender.sendMessage("더 큽니다");
            }
            else
            {
              sender.sendMessage("정답입니다");
              Bukkit.broadcastMessage(sender.getName() + "님이 " + count + "번째로 정답을 맞췄습니다. 정답은 " + number + "였습니다");
              number = null;
              count = 0;
            }
          }
        }
      }


      if (args.length >= 2)
      {
        switch (args[0])
        {
          case "entities" -> {
            List<Entity> entities = SelectorUtil.getEntities(sender, args[1], true);
            MessageUtil.sendMessage(sender, entities != null ? entities : "null");
            if (entities != null)
            {
              CustomEffect customEffect = new CustomEffect(CustomEffectType.MUTE, Integer.parseInt(args[2]), 0);
              for (Entity entity : entities)
              {
                CustomEffectManager.addEffect(entity, customEffect);
              }
            }
          }
          case "entity" -> {
            Entity entity = SelectorUtil.getEntity(sender, args[1], true);
            MessageUtil.sendMessage(sender, entity != null ? entity : "null");
          }
          case "players" -> {
            List<Player> players = SelectorUtil.getPlayers(sender, args[1], true);
            MessageUtil.sendMessage(sender, players != null ? players : "null");
          }
          case "player" -> {
            Player p = SelectorUtil.getPlayer(sender, args[1], true);
            MessageUtil.sendMessage(sender, p != null ? p : "null");
          }
        }
      }
    }
    catch (Exception e)
    {

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
      return Method.tabCompleterList(args, "<뭐 왜 왓>", "entities", "entity", "players", "player");
    }
    else if (length == 2)
    {
      switch (args[0])
      {
        case "entities" -> {
          return Method.tabCompleterEntity(sender, args, "<개체>", true);
        }
        case "entity" -> {
          return Method.tabCompleterEntity(sender, args, "<개체>");
        }
        case "players" -> {
          return Method.tabCompleterPlayer(sender, args, "<플레이어>", true);
        }
        case "player" -> {
          return Method.tabCompleterPlayer(sender, args, "<플레이어>");
        }
      }
      return Collections.EMPTY_LIST;
    }
    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}


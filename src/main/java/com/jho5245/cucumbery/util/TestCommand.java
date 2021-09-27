package com.jho5245.cucumbery.util;

import com.jho5245.cucumbery.util.storage.data.Prefix;
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
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    args = MessageUtil.wrapWithQuote(args);
    if (!Method.hasPermission(sender, "asdf", true))
    {
      return true;
    }
    Player player = (Player) sender;
    try
    {
      switch (args[0])
      {
        case "entities" -> {
          List<Entity> entities = SelectorUtil.getEntities(sender, args[1], true);
          MessageUtil.sendMessage(sender, entities != null ? entities : "null");
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
    catch (Exception e)
    {
      e.printStackTrace();
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
    }
    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}


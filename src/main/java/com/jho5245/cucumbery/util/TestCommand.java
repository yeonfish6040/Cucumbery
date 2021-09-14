package com.jho5245.cucumbery.util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class TestCommand implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    args = MessageUtil.wrapWithQuote(args);
    if (!Method.hasPermission(sender, "asdf", true))
    {
      return true;
    }
    Player player = (Player) sender;
    player.sendMessage(Arrays.toString(args));
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
    return true;
  }
}

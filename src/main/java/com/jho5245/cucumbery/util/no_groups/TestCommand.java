package com.jho5245.cucumbery.util.no_groups;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("all")
public class TestCommand implements CommandExecutor, AsyncTabCompleter
{
  private static final NamespacedKey test = NamespacedKey.fromString("test");

  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    args = MessageUtil.wrapWithQuote(args);
    if (!Method.hasPermission(sender, "asdf", true))
    {
      return true;
    }
    try
    {
      if (sender instanceof Player player)
      {
        Location location = LocationUtil.location(sender, args[0], false, true);
        if (location != null)
        {
          MessageUtil.info(player, player.getLocation());
          MessageUtil.info(player, location);
        }
        /*
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        switch (args[0])
        {
          case "add" -> {
            ItemStack result = new ItemStack(Material.TNT);
            ItemMeta itemMeta = result.getItemMeta();
            itemMeta.displayName(ComponentUtil.translate("&iitem.minecraft.diamond"));
            result.setItemMeta(itemMeta);
            ItemStack ingredient = new ItemStack(Material.DIAMOND);
            ShapedRecipe shapedRecipe = new ShapedRecipe(test, result);
            shapedRecipe = shapedRecipe.shape("aaa", "a#a", "aaa").setIngredient('a', ingredient);
              MessageUtil.sendMessage(player, Bukkit.addRecipe(shapedRecipe) ? "recipe added!" : "&ccould not add that recipe!");
          }
          case "remove" -> {
            MessageUtil.sendMessage(player, Bukkit.removeRecipe(test) ? "recipe removed!" : "could not reipce that recipe!");
          }
        }*/
      }
      if (args.length >= 2)
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

  @NotNull
  public List<Completion> completion(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args, @NotNull Location location)
  {
    int length = args.length;
    if (length == 1)
    {
      return TabCompleterUtil.locationArgument(sender, args, "<위치>", null, false);
    }
    return Collections.singletonList(TabCompleterUtil.ARGS_LONG);
  }
}


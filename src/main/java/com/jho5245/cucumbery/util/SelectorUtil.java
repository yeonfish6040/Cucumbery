package com.jho5245.cucumbery.util;

import com.google.common.base.Predicates;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectorUtil
{
  @Nullable
  public static Player getPlayer(@Nullable CommandSender sender, @NotNull String selector)
  {
    return getPlayer(sender, selector, true);
  }

  @Nullable
  public static Player getPlayer(@Nullable CommandSender sender, @NotNull String selector, boolean notice)
  {
    try
    {
      Player player = Method.getPlayer(sender, selector, false);
      if (player != null)
      {
        return player;
      }
      if (sender == null)
      {
        sender = Bukkit.getConsoleSender();
      }
      List<Entity> entities = Bukkit.selectEntities(sender, selector);
      if (entities.size() == 0)
      {
        if (notice)
        {
          MessageUtil.noArg(sender, Prefix.NO_PLAYER, selector);
        }
        return null;
      }
      if (!entities.stream().allMatch(Predicates.instanceOf(Player.class)::apply))
      {
        if (notice)
        {
          if (!sender.hasPermission("asdf"))
          {
            MessageUtil.sendError(sender, ComponentUtil.createTranslate("argument.entity.selector.not_allowed"));
            return null;
          }
          MessageUtil.sendError(sender, ComponentUtil.createTranslate("argument.player.entities"));
        }
        return null;
      }
      if (entities.size() != 1)
      {
        if (notice)
        {
          if (!sender.hasPermission("asdf"))
          {
            MessageUtil.sendError(sender, ComponentUtil.createTranslate("argument.entity.selector.not_allowed"));
            return null;
          }
          MessageUtil.sendError(sender, ComponentUtil.createTranslate("argument.player.toomany"));
        }
        return null;
      }
      if (!sender.hasPermission("asdf"))
      {
        MessageUtil.sendError(sender, ComponentUtil.createTranslate("argument.entity.notfound.player"));
        return null;
      }
      return (Player) entities.get(0);
    }
    catch (IllegalArgumentException e)
    {
      if (sender != null && notice)
      {
        if (!sender.hasPermission("asdf"))
        {
          MessageUtil.sendError(sender, ComponentUtil.createTranslate("argument.entity.notfound.player"));
          return null;
        }
        MessageUtil.sendError(sender, errorMessage(sender, selector, e));
      }
    }
    return null;
  }

  @Nullable
  public static Entity getEntity(@Nullable CommandSender sender, @NotNull String selector)
  {
    return getEntity(sender, selector, true);
  }

  @Nullable
  public static Entity getEntity(@Nullable CommandSender sender, @NotNull String selector, boolean notice)
  {
    try
    {
      Player player = Method.getPlayer(sender, selector, false);
      if (player != null)
      {
        return player;
      }
      if (sender == null)
      {
        sender = Bukkit.getConsoleSender();
      }
      List<Entity> entities = Bukkit.selectEntities(sender, selector);
      if (entities.size() == 0)
      {
        if (notice)
        {
          MessageUtil.noArg(sender, Prefix.NO_ENTITY, selector);
        }
        return null;
      }
      if (entities.size() != 1)
      {
        if (notice)
        {
          if (!sender.hasPermission("asdf"))
          {
            MessageUtil.sendError(sender, ComponentUtil.createTranslate("argument.entity.selector.not_allowed"));
            return null;
          }
          MessageUtil.sendError(sender, ComponentUtil.createTranslate("argument.entity.toomany"));
        }
        return null;
      }
      if (!sender.hasPermission("asdf"))
      {
        MessageUtil.sendError(sender, ComponentUtil.createTranslate("argument.entity.selector.not_allowed"));
        return null;
      }
      return entities.get(0);
    }
    catch (IllegalArgumentException e)
    {
      if (sender != null && notice)
      {
        if (!sender.hasPermission("asdf"))
        {
          MessageUtil.sendError(sender, ComponentUtil.createTranslate("argument.entity.selector.not_allowed"));
          return null;
        }
        MessageUtil.sendError(sender, errorMessage(sender, selector, e));
      }
    }
    return null;
  }

  @Nullable
  public static List<Player> getPlayers(@Nullable CommandSender sender, @NotNull String selector)
  {
    return getPlayers(sender, selector, true);
  }

  @Nullable
  public static List<Player> getPlayers(@Nullable CommandSender sender, @NotNull String selector, boolean notice)
  {
    try
    {
      Player player = Method.getPlayer(sender, selector, false);
      if (player != null)
      {
        return new ArrayList<>(Collections.singletonList(player));
      }
      if (sender == null)
      {
        sender = Bukkit.getConsoleSender();
      }
      List<Entity> entities = Bukkit.selectEntities(sender, selector);
      if (entities.size() == 0)
      {
        if (notice)
        {
          MessageUtil.noArg(sender, Prefix.NO_PLAYER, selector);
        }
        return null;
      }
      if (!entities.stream().allMatch(Predicates.instanceOf(Player.class)::apply))
      {
        if (notice)
        {
          if (!sender.hasPermission("asdf"))
          {
            MessageUtil.sendError(sender, ComponentUtil.createTranslate("argument.entity.selector.not_allowed"));
            return null;
          }
          MessageUtil.sendError(sender, ComponentUtil.createTranslate("argument.player.entities"));
        }
        return null;
      }
      List<Player> players = new ArrayList<>();
      for (Entity entity : entities)
      {
        if (entity instanceof Player player2)
        {
          players.add(player2);
        }
      }
      if (!sender.hasPermission("asdf"))
      {
        MessageUtil.sendError(sender, ComponentUtil.createTranslate("argument.entity.selector.not_allowed"));
        return null;
      }
      return players;
  }
    catch (IllegalArgumentException e)
  {
    if (sender != null && notice)
    {
      if (!sender.hasPermission("asdf"))
      {
        MessageUtil.sendError(sender, ComponentUtil.createTranslate("argument.entity.selector.not_allowed"));
        return null;
      }
      MessageUtil.sendError(sender, errorMessage(sender, selector, e));
    }
  }
    return null;
  }

  @Nullable
  public static List<Entity> getEntities(@Nullable CommandSender sender, @NotNull String selector)
  {
    return getEntities(sender, selector, true);
  }

  @Nullable
  public static List<Entity> getEntities(@Nullable CommandSender sender, @NotNull String selector, boolean notice)
  {
    try
    {
      Player player = Method.getPlayer(sender, selector, false);
      if (player != null)
      {
        List<Entity> entities = new ArrayList<>();
        entities.add(player);
        return entities;
      }
      if (sender == null)
      {
        sender = Bukkit.getConsoleSender();
      }
      List<Entity> entities = Bukkit.selectEntities(sender, selector);
      if (entities.size() == 0 && notice)
      {
        MessageUtil.noArg(sender, Prefix.NO_ENTITY, selector);
      }
      if (!sender.hasPermission("asdf"))
      {
        MessageUtil.sendError(sender, ComponentUtil.createTranslate("argument.entity.selector.not_allowed"));
        return null;
      }
      return entities.size() > 0 ? entities : null;
    }
    catch (IllegalArgumentException e)
    {
      if (sender != null && notice)
      {
        if (!sender.hasPermission("asdf"))
        {
          MessageUtil.sendError(sender, ComponentUtil.createTranslate("argument.entity.selector.not_allowed"));
          return null;
        }
        MessageUtil.sendError(sender, errorMessage(sender, selector, e));
      }
    }
    return null;
  }

  @Nullable
  public static List<OfflinePlayer> getOfflinePlayers(@Nullable CommandSender sender, @NotNull String selector, boolean notice)
  {
    try
    {
      Player player = Method.getPlayer(sender, selector, false);
      if (player != null)
      {
        List<OfflinePlayer> entities = new ArrayList<>();
        entities.add(player);
        return entities;
      }
      if (sender == null)
      {
        sender = Bukkit.getConsoleSender();
      }
      OfflinePlayer offlinePlayer = SelectorUtil.getOfflinePlayer(sender, selector, false);
      if (offlinePlayer != null)
      {
        return new ArrayList<>(Collections.singletonList(offlinePlayer));
      }
      List<Entity> entities = Bukkit.selectEntities(sender, selector);
      if (entities.size() == 0)
      {
        if (notice)
        {
          MessageUtil.noArg(sender, Prefix.NO_PLAYER, selector);
        }
        return null;
      }
      if (!entities.stream().allMatch(Predicates.instanceOf(Player.class)::apply))
      {
        if (!sender.hasPermission("asdf"))
        {
          MessageUtil.sendError(sender, ComponentUtil.createTranslate("argument.entity.selector.not_allowed"));
          return null;
        }
        if (notice)
        {
          MessageUtil.sendError(sender, ComponentUtil.createTranslate("argument.player.entities"));
        }
        return null;
      }
      List<OfflinePlayer> players = new ArrayList<>();
      for (Entity entity : entities)
      {
        if (entity instanceof Player player2)
        {
          players.add(player2);
        }
      }
      if (!sender.hasPermission("asdf"))
      {
        MessageUtil.sendError(sender, ComponentUtil.createTranslate("argument.entity.selector.not_allowed"));
        return null;
      }
      return players;
    }
    catch (IllegalArgumentException e)
    {
      if (sender != null && notice)
      {
        if (!sender.hasPermission("asdf"))
        {
          MessageUtil.sendError(sender, ComponentUtil.createTranslate("argument.entity.selector.not_allowed"));
          return null;
        }
        MessageUtil.sendError(sender, errorMessage(sender, selector, e));
      }
    }
    return null;
  }

  @Nullable
  public static OfflinePlayer getOfflinePlayer(@Nullable CommandSender sender, @NotNull String selector)
  {
    return getOfflinePlayer(sender, selector, true);
  }

  @Nullable
  public static OfflinePlayer getOfflinePlayer(@Nullable CommandSender sender, @NotNull String selector, boolean notice)
  {
    try
    {
      Player player = Method.getPlayer(sender, selector, false);
      if (player != null)
      {
        return player;
      }
      if (sender == null)
      {
        sender = Bukkit.getConsoleSender();
      }
      OfflinePlayer offlinePlayer = Method.getOfflinePlayer(sender, selector, false);
      if (offlinePlayer != null)
      {
        return offlinePlayer;
      }
      List<Entity> entities = Bukkit.selectEntities(sender, selector);
      if (entities.size() == 0)
      {
        if (notice)
        {
          MessageUtil.noArg(sender, Prefix.NO_PLAYER, selector);
        }
        return null;
      }
      if (!entities.stream().allMatch(Predicates.instanceOf(Player.class)::apply))
      {
        if (!sender.hasPermission("asdf"))
        {
          MessageUtil.sendError(sender, ComponentUtil.createTranslate("argument.entity.selector.not_allowed"));
          return null;
        }
        if (notice)
        {
          MessageUtil.sendError(sender, ComponentUtil.createTranslate("argument.player.entities"));
        }
        return null;
      }
      if (entities.size() != 1)
      {
        if (!sender.hasPermission("asdf"))
        {
          MessageUtil.sendError(sender, ComponentUtil.createTranslate("argument.entity.selector.not_allowed"));
          return null;
        }
        if (notice)
        {
          MessageUtil.sendError(sender, ComponentUtil.createTranslate("argument.player.toomany"));
        }
        return null;
      }
      if (!sender.hasPermission("asdf"))
      {
        MessageUtil.sendError(sender, ComponentUtil.createTranslate("argument.entity.selector.not_allowed"));
        return null;
      }
      return (Player) entities.get(0);
  }
    catch (IllegalArgumentException e)
    {
      if (sender != null && notice)
      {

        if (!sender.hasPermission("asdf"))
        {
          MessageUtil.sendError(sender, ComponentUtil.createTranslate("argument.entity.selector.not_allowed"));
          return null;
        }
        MessageUtil.sendError(sender, errorMessage(sender, selector, e));
      }
    }
    return null;
  }

  @NotNull
  public static Component errorMessage(@Nullable CommandSender sender, @NotNull String selector, @NotNull IllegalArgumentException e)
  {
    return ComponentUtil.createTranslate(getErrorKey(sender, selector, e));
  }

  @NotNull
  public static String getErrorMessage(@Nullable CommandSender sender, @NotNull String selector, @NotNull IllegalArgumentException e)
  {
    return ComponentUtil.serialize(errorMessage(sender, selector, e));
  }

  @NotNull
  public static String getErrorKey(@Nullable CommandSender sender, @NotNull String selector, @NotNull IllegalArgumentException e)
  {
    if (e.getCause() != null)
    {
      String msg = e.getCause().getMessage();
      if (msg.startsWith("Unknown or incomplete command, see below for error"))
    {
      return "command.unknown.command";
    }
      if (msg.startsWith("Incorrect argument for command"))
      {
        return "command.unknown.argument";
      }
      if (msg.startsWith("Expected whitespace to end one argument, but found trailing data"))
      {
        return "command.expected.separator";
      }

      if (msg.startsWith("Unknown selector type"))
      {
        return "argument.entity.selector.unknown";
      }
      if (msg.startsWith("Missing selector type"))
      {
        return "argument.entity.selector.missing";
      }
      if (msg.startsWith("Selector not allowed"))
      {
        return "argument.entity.selector.not_allowed";
      }

      if (msg.startsWith("Expected value for option"))
      {
        return "argument.entity.options.valueless";
      }
      if (msg.startsWith("Unknown option"))
      {
        return "argument.entity.options.unknown";
      }
      if (msg.startsWith("Option") && msg.contains("isn't applicable here"))
      {
        return "argument.entity.options.inapplicable";
      }
      if (msg.startsWith("Invalid or unknown sort type"))
      {
        return "argument.entity.options.sort.irreversible";
      }
      if (msg.startsWith("Invalid or unknown game mode"))
      {
        return "argument.entity.options.mode.invalid";
      }
      if (msg.startsWith("Invalid or unknown entity type"))
      {
        return "argument.entity.options.type.invalid";
      }
      if (msg.startsWith("Expected end of options"))
      {
        return "argument.entity.options.unterminated";
      }
      if (msg.startsWith("Distance cannot be negative"))
      {
        return "argument.entity.options.distance.negative";
      }
      if (msg.startsWith("Level shouldn't be negative"))
      {
        return "argument.entity.options.level.negative";
      }
      if (msg.startsWith("Limit must be at least 1"))
      {
        return "argument.entity.options.limit.toosmall";
      }

      if (msg.startsWith("Invalid name or UUID"))
      {
        return "argument.entity.invalid";
      }
      if (msg.startsWith("Invalid UUID"))
      {
        return "argument.uuid.invalid";
      }

      if (msg.startsWith("Expected value or range of values"))
      {
        return "argument.range.empty";
      }
      if (msg.startsWith("Only whole numbers allowed, not decimals"))
      {
        return "argument.range.ints";
      }
      if (msg.startsWith("Min cannot be bigger than max"))
      {
        return "argument.range.swapped";
      }
      if (msg.startsWith("Incomplete (expected 1 angle)"))
      {
        return "argument.angle.incomplete";
      }
      if (msg.startsWith("Invalid angle"))
      {
        return "argument.angle.invalid";
      }
      if (msg.startsWith("Only one entity is allowed, but the provided selector allows more than one"))
      {
        return "argument.entity.toomany";
      }
      if (msg.startsWith("Only one player is allowed, but the provided selector allows more than one"))
      {
        return "argument.player.toomany";
      }
      if (msg.startsWith("Only players may be affected by this command, but the provided selector includes entities"))
      {
        return "argument.player.entities";
      }
      if (msg.startsWith("No entity was found"))
      {
        return "argument.entity.notfound.entity";
      }
      if (msg.startsWith("No player was found"))
      {
        return "argument.entity.notfound.player";
      }
      if (msg.startsWith("That player does not exist"))
      {
        return "argument.player.unknown";
      }

      if (msg.startsWith("Expected quote to start a string"))
      {
        return "parsing.quote.expected.start";
      }
      if (msg.startsWith("Unclosed quoted string"))
      {
        return "parsing.quote.expected.end";
      }
      if (msg.startsWith("Invalid escape sequence"))
      {
        return "parsing.quote.escape";
      }
      if (msg.startsWith("Invalid boolean, expected 'true' or 'false' but found"))
      {
        return "parsing.bool.invalid";
      }
      if (msg.startsWith("Invalid integer"))
      {
        return "parsing.int.invalid";
      }
      if (msg.startsWith("Expected integer"))
      {
        return "parsing.int.expected";
      }
      if (msg.startsWith("Invalid long"))
      {
        return "parsing.long.invalid";
      }
      if (msg.startsWith("Expected long"))
      {
        return "parsing.long.expected";
      }
      if (msg.startsWith("Invalid double"))
      {
        return "parsing.double.invalid";
      }
      if (msg.startsWith("Expected double"))
      {
        return "parsing.double.expected";
      }
      if (msg.startsWith("Invalid float"))
      {
        return "parsing.float.invalid";
      }
      if (msg.startsWith("Expected float"))
      {
        return "parsing.float.expected";
      }
      if (msg.startsWith("Expected boolean"))
      {
        return "parsing.bool.expected";
      }
      if (msg.startsWith("Expected"))
      {
        return "parsing.expected";
      }
      return e.getCause().getMessage();
    }
    String msg = e.getMessage();
    if (msg.startsWith("Spurious trailing data in selector"))
    {
      return "command.expected.separator";
    }
    return e.getMessage();
  }
}

















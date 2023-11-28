package com.jho5245.cucumbery.util.no_groups;

import com.google.common.base.Predicates;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextDecoration.State;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectorUtil
{
	private static final Pattern scoreboardTag = Pattern.compile("#([A-Za-z0-9_.+-]+)");

	@NotNull
	public static List<Entity> get(@NotNull CommandSender sender, @NotNull String selector) throws IllegalArgumentException
	{
		// custom target selector
		String selectorCopy = selector;
		boolean allPlayersButNotSelf = selectorCopy.startsWith("@A"), allEntitiesButNotPlayers = selectorCopy.startsWith(
				"@E"), randomEntity = selectorCopy.startsWith("@R"), randomPlayerButNotSelf = selectorCopy.startsWith(
				"@rr"), randomEntityButNotSelf = selectorCopy.startsWith("@rR"), allEntitiesButNotSelf = selectorCopy.startsWith(
				"@S"), nearestPlayerButNotSelf = selectorCopy.startsWith("@P"), nearestEntityButNotSelf = selectorCopy.startsWith("@pp");
		String tagPredicate = null;
		if (selectorCopy.startsWith("#"))
		{
			Matcher matcher = scoreboardTag.matcher(selectorCopy);
			try
			{
				while (matcher.find())
				{
					tagPredicate = matcher.group(1);
					if (tagPredicate != null)
					{
						break;
					}
				}
			}
			catch (Exception e)
			{
				Cucumbery.getPlugin().getLogger().warning(e.getMessage());
			}
		}
		if (allPlayersButNotSelf || nearestPlayerButNotSelf)
		{
			selectorCopy = "@a" + selectorCopy.substring(2);
		}
		if (allEntitiesButNotPlayers || allEntitiesButNotSelf || randomEntity)
		{
			selectorCopy = "@e" + selectorCopy.substring(2);
		}
		if (randomPlayerButNotSelf)
		{
			selectorCopy = "@a" + selectorCopy.substring(3);
		}
		if (nearestEntityButNotSelf || randomEntityButNotSelf)
		{
			selectorCopy = "@e" + selectorCopy.substring(3);
		}
		if (tagPredicate != null)
		{
			selectorCopy = "@e" + selectorCopy.substring(tagPredicate.length() + 1);
		}
		List<Entity> entities = Bukkit.selectEntities(sender, selectorCopy);
		entities.removeIf(e -> allEntitiesButNotPlayers && e instanceof Player);
		@Nullable CommandSender finalSender = sender;
		entities.removeIf(e -> allPlayersButNotSelf && finalSender instanceof Player p && e == p);
		entities.removeIf(e -> allEntitiesButNotSelf && finalSender == e);
		String finalTagPredicate = tagPredicate;
		entities.removeIf(e -> finalTagPredicate != null && !e.getScoreboardTags().contains(finalTagPredicate));
		if (randomEntity || randomEntityButNotSelf || randomPlayerButNotSelf)
		{
			if (randomEntityButNotSelf || randomPlayerButNotSelf)
			{
				entities.removeIf(e -> e == finalSender);
			}
			int i = Method.random(0, entities.size() - 1);
			Entity en = entities.get(i);
			entities = new ArrayList<>(Collections.singletonList(en));
		}
		if (nearestEntityButNotSelf || nearestPlayerButNotSelf)
		{
			Location senderLocation = CommandArgumentUtil.senderLocation(sender);
			entities.removeIf(e -> !e.getWorld().getName().equals(senderLocation.getWorld().getName()));
			entities.removeIf(e -> finalSender == e);
			if (nearestEntityButNotSelf)
			{
				Entity result = null;
				double lastDistance = Double.MAX_VALUE;
				for (Entity entity : entities)
				{
					double distance = senderLocation.distance(entity.getLocation());
					if (distance < lastDistance)
					{
						lastDistance = distance;
						result = entity;
					}
				}
				entities.clear();
				if (result != null)
				{
					entities.add(result);
				}
			}
			if (nearestPlayerButNotSelf)
			{
				Player result = null;
				double lastDistance = Double.MAX_VALUE;
				for (Entity entity : entities)
				{
					if (!(entity instanceof Player p))
					{
						continue;
					}
					double distance = senderLocation.distance(entity.getLocation());
					if (distance < lastDistance)
					{
						lastDistance = distance;
						result = p;
					}
				}
				entities.clear();
				if (result != null)
				{
					entities.add(result);
				}
			}
		}
		return entities;
	}

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
			List<Entity> entities = get(sender, selector);
			if (entities.isEmpty())
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
					if (!sender.hasPermission("minecraft.command.selector"))
					{
						MessageUtil.sendError(sender, ComponentUtil.translate("argument.entity.selector.not_allowed"));
						return null;
					}
					MessageUtil.sendError(sender, "%s (%s)", ComponentUtil.translate("argument.player.entities"), selector);
				}
				return null;
			}
			if (entities.size() != 1)
			{
				if (notice)
				{
					if (!sender.hasPermission("minecraft.command.selector"))
					{
						MessageUtil.sendError(sender, ComponentUtil.translate("argument.entity.selector.not_allowed"));
						return null;
					}
					MessageUtil.sendError(sender, "%s (%s)", ComponentUtil.translate("argument.player.toomany"), selector);
				}
				return null;
			}
			if (!sender.hasPermission("minecraft.command.selector"))
			{
				MessageUtil.sendError(sender, "%s (%s)", ComponentUtil.translate("argument.entity.notfound.player"), selector);
				return null;
			}
			return (Player) entities.get(0);
		}
		catch (IllegalArgumentException e)
		{
			if (sender != null && notice)
			{
				if (!sender.hasPermission("minecraft.command.selector"))
				{
					MessageUtil.sendError(sender, ComponentUtil.translate("argument.entity.notfound.player"));
					return null;
				}
				MessageUtil.sendError(sender, errorMessage(selector, e));
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
			List<Entity> entities = get(sender, selector);
			if (entities.isEmpty())
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
					if (!sender.hasPermission("minecraft.command.selector"))
					{
						MessageUtil.sendError(sender, ComponentUtil.translate("argument.entity.selector.not_allowed"));
						return null;
					}
					MessageUtil.sendError(sender, "%s (%s)", ComponentUtil.translate("argument.entity.toomany"), selector);
				}
				return null;
			}
			if (!sender.hasPermission("minecraft.command.selector"))
			{
				MessageUtil.sendError(sender, ComponentUtil.translate("argument.entity.selector.not_allowed"));
				return null;
			}
			return entities.get(0);
		}
		catch (IllegalArgumentException e)
		{
			if (sender != null && notice)
			{
				if (!sender.hasPermission("minecraft.command.selector"))
				{
					MessageUtil.sendError(sender, ComponentUtil.translate("argument.entity.selector.not_allowed"));
					return null;
				}
				MessageUtil.sendError(sender, errorMessage(selector, e));
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

			List<Entity> entities = get(sender, selector);
			if (entities.isEmpty())
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
					if (!sender.hasPermission("minecraft.command.selector"))
					{
						MessageUtil.sendError(sender, ComponentUtil.translate("argument.entity.selector.not_allowed"));
						return null;
					}
					MessageUtil.sendError(sender, "%s (%s)", ComponentUtil.translate("argument.player.entities"), selector);
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
			if (!sender.hasPermission("minecraft.command.selector"))
			{
				MessageUtil.sendError(sender, ComponentUtil.translate("argument.entity.selector.not_allowed"));
				return null;
			}
			return players;
		}
		catch (IllegalArgumentException e)
		{
			if (sender != null && notice)
			{
				if (!sender.hasPermission("minecraft.command.selector"))
				{
					MessageUtil.sendError(sender, ComponentUtil.translate("argument.entity.selector.not_allowed"));
					return null;
				}
				MessageUtil.sendError(sender, errorMessage(selector, e));
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

			List<Entity> entities = get(sender, selector);
			if (entities.isEmpty() && notice)
			{
				MessageUtil.noArg(sender, Prefix.NO_ENTITY, selector);
			}
			if (!sender.hasPermission("minecraft.command.selector"))
			{
				MessageUtil.sendError(sender, ComponentUtil.translate("argument.entity.selector.not_allowed"));
				return null;
			}
			return !entities.isEmpty() ? entities : null;
		}
		catch (IllegalArgumentException e)
		{
			if (sender != null && notice)
			{
				if (!sender.hasPermission("minecraft.command.selector"))
				{
					MessageUtil.sendError(sender, ComponentUtil.translate("argument.entity.selector.not_allowed"));
					return null;
				}
				MessageUtil.sendError(sender, errorMessage(selector, e));
			}
		}
		return null;
	}

	@Nullable
	public static List<OfflinePlayer> getOfflinePlayers(@Nullable CommandSender sender, @NotNull String selector, boolean notice)
	{
		try
		{
			List<Player> onlinePlayers = getPlayers(sender, selector, false);
			if (onlinePlayers != null)
			{
				return new ArrayList<>(onlinePlayers);
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
			List<Entity> entities = get(sender, selector);
			if (entities.isEmpty())
			{
				if (notice)
				{
					MessageUtil.noArg(sender, Prefix.NO_PLAYER, selector);
				}
				return null;
			}
			if (!entities.stream().allMatch(Predicates.instanceOf(Player.class)::apply))
			{
				if (!sender.hasPermission("minecraft.command.selector"))
				{
					MessageUtil.sendError(sender, ComponentUtil.translate("argument.entity.selector.not_allowed"));
					return null;
				}
				if (notice)
				{
					MessageUtil.sendError(sender, "%s (%s)", ComponentUtil.translate("argument.player.entities"), selector);
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
			if (!sender.hasPermission("minecraft.command.selector"))
			{
				MessageUtil.sendError(sender, ComponentUtil.translate("argument.entity.selector.not_allowed"));
				return null;
			}
			return players;
		}
		catch (IllegalArgumentException e)
		{
			if (sender != null && notice)
			{
				if (!sender.hasPermission("minecraft.command.selector"))
				{
					MessageUtil.sendError(sender, ComponentUtil.translate("argument.entity.selector.not_allowed"));
					return null;
				}
				MessageUtil.sendError(sender, errorMessage(selector, e));
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
			Player player = getPlayer(sender, selector, false);
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
			List<Entity> entities = get(sender, selector);
			if (entities.isEmpty())
			{
				if (notice)
				{
					MessageUtil.noArg(sender, Prefix.NO_PLAYER, selector);
				}
				return null;
			}
			if (!entities.stream().allMatch(Predicates.instanceOf(Player.class)::apply))
			{
				if (!sender.hasPermission("minecraft.command.selector"))
				{
					MessageUtil.sendError(sender, ComponentUtil.translate("argument.entity.selector.not_allowed"));
					return null;
				}
				if (notice)
				{
					MessageUtil.sendError(sender, "%s (%s)", ComponentUtil.translate("argument.player.entities"), selector);
				}
				return null;
			}
			if (entities.size() != 1)
			{
				if (!sender.hasPermission("minecraft.command.selector"))
				{
					MessageUtil.sendError(sender, ComponentUtil.translate("argument.entity.selector.not_allowed"));
					return null;
				}
				if (notice)
				{
					MessageUtil.sendError(sender, "%s (%s)", ComponentUtil.translate("argument.player.toomany"), selector);
				}
				return null;
			}
			if (!sender.hasPermission("minecraft.command.selector"))
			{
				MessageUtil.sendError(sender, ComponentUtil.translate("argument.entity.selector.not_allowed"));
				return null;
			}
			return (Player) entities.get(0);
		}
		catch (IllegalArgumentException e)
		{
			if (sender != null && notice)
			{
				if (!sender.hasPermission("minecraft.command.selector"))
				{
					MessageUtil.sendError(sender, ComponentUtil.translate("argument.entity.selector.not_allowed"));
					return null;
				}
				MessageUtil.sendError(sender, errorMessage(selector, e));
			}
		}
		return null;
	}

	@NotNull
	public static String getErrorMessage(@NotNull String selector, @NotNull IllegalArgumentException e)
	{
		return ComponentUtil.serialize(errorMessage(selector, e));
	}

	@NotNull
	public static TranslatableComponent errorMessage(@NotNull String selector, @NotNull IllegalArgumentException e)
	{
		String msg = e.getMessage();
		if (e.getCause() != null)
		{
			List<Component> args = new ArrayList<>();
			Component concat = Component.empty();
			msg = e.getCause().getMessage();
			final String origin = msg;
			//      MessageUtil.broadcastDebug(msg);
			if (msg.startsWith("Incorrect argument for command"))
			{
				msg = "command.unknown.argument";
			}
			if (msg.startsWith("Expected whitespace to end one argument, but found trailing data"))
			{
				msg = "command.expected.separator";
				concat = Component.text(origin.split(": ")[1].replace("<--[HERE]", ""));
				concat = concat.append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE));
			}
			if (msg.startsWith("Unknown selector type"))
			{
				msg = "argument.entity.selector.unknown";
				args.add(Component.text(origin.substring("Unknown selector tpye '".length()).split("'")[0]));
				concat = Component.text(origin.split(": ")[1].replace("<--[HERE]", ""));
				concat = concat.append(ComponentUtil.translate("&ccommand.context.here", NamedTextColor.RED).decoration(TextDecoration.ITALIC, State.TRUE));
			}
			if (msg.startsWith("Missing selector type"))
			{
				msg = "argument.entity.selector.missing";
				concat = Component.text(origin.split(": ")[1].replace("<--[HERE]", ""));
				concat = concat.append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE));
			}
			if (msg.startsWith("Expected value for option"))
			{
				msg = "argument.entity.options.valueless";
				args.add(Component.text(origin.substring("Expected value for option '".length()).split("'")[0]));
				concat = Component.text(origin.split(": ")[1].replace("<--[HERE]", ""));
				concat = concat.append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE));
			}
			if (msg.startsWith("Unknown option"))
			{
				msg = "argument.entity.options.unknown";
				args.add(Component.text(origin.substring("Unknown option '".length()).split("'")[0]));
				concat = Component.text(origin.split(": ")[1].replace("<--[HERE]", ""));
				concat = concat.append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE));
			}
			if (msg.startsWith("Option") && msg.contains("isn't applicable here"))
			{
				msg = "argument.entity.options.inapplicable";
				args.add(Component.text(origin.substring("Option '".length()).split("'")[0]));
				concat = Component.text(origin.split(": ")[1].replace("<--[HERE]", ""));
				concat = concat.append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE));
			}
			if (msg.startsWith("Invalid or unknown sort type"))
			{
				msg = "argument.entity.options.sort.irreversible";
				args.add(Component.text(origin.substring("Invalid or unknown sort type '".length()).split("'")[0]));
				concat = Component.text(origin.split(": ")[1].replace("<--[HERE]", ""));
				concat = concat.append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE));
			}
			if (msg.startsWith("Invalid or unknown game mode"))
			{
				msg = "argument.entity.options.mode.invalid";
				args.add(Component.text(origin.substring("Invalid or unknown game mode '".length()).split("'")[0]));
				concat = Component.text(origin.split(": ")[1].replace("<--[HERE]", ""));
				concat = concat.append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE));
			}
			if (msg.startsWith("Invalid or unknown entity type"))
			{
				msg = "argument.entity.options.type.invalid";
				args.add(Component.text(origin.substring("Invalid or unknown entity type '".length()).split("'")[0]));
				concat = Component.text(origin.split(": ")[1].replace("<--[HERE]", ""));
				concat = concat.append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE));
			}
			if (msg.startsWith("Expected end of options"))
			{
				msg = "argument.entity.options.unterminated";
				concat = Component.text(origin.split(": ")[1].replace("<--[HERE]", ""));
				concat = concat.append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE));
			}
			if (msg.startsWith("Distance cannot be negative"))
			{
				msg = "argument.entity.options.distance.negative";
				concat = Component.text(origin.split(": ")[1].replace("<--[HERE]", ""));
				concat = concat.append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE));
			}
			if (msg.startsWith("Level shouldn't be negative"))
			{
				msg = "argument.entity.options.level.negative";
				concat = Component.text(origin.split(": ")[1].replace("<--[HERE]", ""));
				concat = concat.append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE));
			}
			if (msg.startsWith("Limit must be at least 1"))
			{
				msg = "argument.entity.options.limit.toosmall";
				concat = Component.text(origin.split(": ")[1].replace("<--[HERE]", ""));
				concat = concat.append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE));
			}
			if (msg.startsWith("Invalid name or UUID"))
			{
				msg = "argument.entity.invalid";
				concat = ComponentUtil.translate("(%s)", selector);
			}
			if (msg.startsWith("Invalid UUID"))
			{
				msg = "argument.uuid.invalid";
				concat = ComponentUtil.translate("(%s)", selector);
			}
			if (msg.startsWith("Expected value or range of values"))
			{
				msg = "argument.range.empty";
				concat = Component.text(origin.split(": ")[1].replace("<--[HERE]", ""));
				concat = concat.append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE));
			}
			if (msg.startsWith("Only whole numbers allowed, not decimals"))
			{
				msg = "argument.range.ints";
				concat = Component.text(origin.split(": ")[1].replace("<--[HERE]", ""));
				concat = concat.append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE));
			}
			if (msg.startsWith("Min cannot be bigger than max"))
			{
				msg = "argument.range.swapped";
				concat = Component.text(origin.split(": ")[1].replace("<--[HERE]", ""));
				concat = concat.append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE));
			}
			if (msg.startsWith("Incomplete (expected 1 angle)"))
			{
				msg = "argument.angle.incomplete";
			}
			if (msg.startsWith("Invalid angle"))
			{
				msg = "argument.angle.invalid";
				concat = Component.text(origin.split(": ")[1].replace("<--[HERE]", ""));
				concat = concat.append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE));
			}
			if (msg.startsWith("No entity was found"))
			{
				msg = "argument.entity.notfound.entity";
			}
			if (msg.startsWith("No player was found"))
			{
				msg = "argument.entity.notfound.player";
			}
			if (msg.startsWith("That player does not exist"))
			{
				msg = "argument.player.unknown";
			}
			if (msg.startsWith("Expected quote to start a string"))
			{
				msg = "parsing.quote.expected.start";
			}
			if (msg.startsWith("Unclosed quoted string"))
			{
				msg = "parsing.quote.expected.end";
				concat = Component.text(origin.split(": ")[1].replace("<--[HERE]", ""));
				concat = concat.append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE));
			}
			if (msg.startsWith("Invalid escape sequence"))
			{
				msg = "parsing.quote.escape";
			}
			if (msg.startsWith("Invalid boolean, expected 'true' or 'false' but found"))
			{
				msg = "parsing.bool.invalid";
			}
			if (msg.startsWith("Invalid integer"))
			{
				msg = "parsing.int.invalid";
				args.add(Component.text(origin.substring("Invalid integer '".length()).split("'")[0]));
				concat = Component.text(origin.split(": ")[1].replace("<--[HERE]", ""));
				concat = concat.append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE));
			}
			if (msg.startsWith("Expected integer"))
			{
				msg = "parsing.int.expected";
				concat = Component.text(origin.split(": ")[1].replace("<--[HERE]", ""));
				concat = concat.append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE));
			}
			if (msg.startsWith("Invalid long"))
			{
				msg = "parsing.long.invalid";
				args.add(Component.text(origin.substring("Invalid long '".length()).split("'")[0]));
				concat = Component.text(origin.split(": ")[1].replace("<--[HERE]", ""));
				concat = concat.append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE));
			}
			if (msg.startsWith("Expected long"))
			{
				msg = "parsing.long.expected";
				concat = Component.text(origin.split(": ")[1].replace("<--[HERE]", ""));
				concat = concat.append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE));
			}
			if (msg.startsWith("Invalid double"))
			{
				msg = "parsing.double.invalid";
				args.add(Component.text(origin.substring("Invalid double '".length()).split("'")[0]));
				concat = Component.text(origin.split(": ")[1].replace("<--[HERE]", ""));
				concat = concat.append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE));
			}
			if (msg.startsWith("Expected double"))
			{
				msg = "parsing.double.expected";
				concat = Component.text(origin.split(": ")[1].replace("<--[HERE]", ""));
				concat = concat.append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE));
			}
			if (msg.startsWith("Invalid float"))
			{
				msg = "parsing.float.invalid";
				args.add(Component.text(origin.substring("Invalid float '".length()).split("'")[0]));
				concat = Component.text(origin.split(": ")[1].replace("<--[HERE]", ""));
				concat = concat.append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE));
			}
			if (msg.startsWith("Expected float"))
			{
				msg = "parsing.float.expected";
				concat = Component.text(origin.split(": ")[1].replace("<--[HERE]", ""));
				concat = concat.append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE));
			}
			if (msg.startsWith("Expected boolean"))
			{
				msg = "parsing.bool.expected";
				concat = Component.text(origin.split(": ")[1].replace("<--[HERE]", ""));
				concat = concat.append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE));
			}
			if (msg.startsWith("Expected key"))
			{
				msg = "argument.nbt.expected.key";
				concat = Component.text(origin.split(": ")[1].replace("<--[HERE]", ""));
				concat = concat.append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE));
			}
			if (msg.startsWith("Expected value"))
			{
				msg = "argument.nbt.expected.value";
				concat = Component.text(origin.split(": ")[1].replace("<--[HERE]", ""));
				concat = concat.append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE));
			}
			if (msg.startsWith("Can't insert ") && msg.contains("into list of"))
			{
				msg = "argument.nbt.list.mixed";
				String[] split = origin.split(" ");
				args.add(Component.text(split[2]));
				args.add(Component.text(split[6]));
				concat = Component.text(origin.split(": ")[1].replace("<--[HERE]", ""));
				concat = concat.append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE));
			}
			if (msg.startsWith("Can't insert ") && !msg.contains("into list of"))
			{
				msg = "argument.nbt.array.mixed";
				String[] split = origin.split(" ");
				args.add(Component.text(split[2]));
				args.add(Component.text(split[4]));
				concat = Component.text(origin.split(": ")[1].replace("<--[HERE]", ""));
				concat = concat.append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE));
			}
			if (msg.startsWith("Invalid array type "))
			{
				msg = "argument.nbt.array.invalid";
				args.add(Component.text(origin.substring("Invalid array type '".length()).split("'")[0]));
				concat = Component.text(origin.split(": ")[1].replace("<--[HERE]", ""));
				concat = concat.append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE));
			}
			if (msg.startsWith("Expected"))
			{
				msg = "parsing.expected";
				args.add(Component.text(origin.substring("Expected '".length()).split("'")[0]));
				concat = Component.text(origin.split(": ")[1].replace("<--[HERE]", ""));
				concat = concat.append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE));
			}
			TranslatableComponent component = ComponentUtil.translate(msg, args);
			if (!concat.equals(Component.empty()))
			{
				component = component.append(Component.text(" ")).append(concat.color(NamedTextColor.RED));
			}
			return component;
		}
		if (msg.startsWith("Spurious trailing data in selector"))
		{
			//      MessageUtil.broadcastDebug(msg);
			return ComponentUtil.translate("command.expected.separator").append(Component.text(" ")).append(Component.text(msg.split(": ")[1], NamedTextColor.RED))
					.append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE));
		}
		return ComponentUtil.translate(msg);
	}
}

















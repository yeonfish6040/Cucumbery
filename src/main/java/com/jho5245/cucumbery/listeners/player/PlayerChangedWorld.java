package com.jho5245.cucumbery.listeners.player;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.PlaceHolderUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlayerChangedWorld implements Listener
{
	@EventHandler public void onPlayerChangedWorld(PlayerChangedWorldEvent event)
	{
		Player player = event.getPlayer();
		Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Method.updateInventory(player), 0L);
		if (Cucumbery.config.getBoolean("world-change.enabled"))
		{
			Location location = player.getLocation();
			String worldName = location.getWorld().getName();
			ConfigurationSection worldSection = Cucumbery.config.getConfigurationSection("world-change." + worldName);
			if (worldSection != null)
			{
				ConfigurationSection messageSection = worldSection.getConfigurationSection("message");
				if (messageSection != null)
				{
					ConfigurationSection conditionSction = messageSection.getConfigurationSection("conditions");
					if (conditionSction == null || a(player, event, conditionSction))
					{
						List<String> messages = messageSection.getStringList("messages");
						for (String message : messages)
						{
							message = PlaceHolderUtil.placeholder(player, message, null);
							MessageUtil.sendMessage(player, message);
						}
					}
				}
				ConfigurationSection titleSection = worldSection.getConfigurationSection("title");
				if (titleSection != null)
				{
					ConfigurationSection titles = titleSection.getConfigurationSection("titles");
					ConfigurationSection conditionSction = titleSection.getConfigurationSection("conditions");
					if (titles != null && (conditionSction == null || a(player, event, conditionSction)))
					{
						int delay = Math.max(titles.getInt("delay"), 0);
						Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> {
							String title = titles.getString("title"), subtitle = titles.getString("subtitle");
							int fadeIn = titles.getInt("fade-in"), stay = titles.getInt("stay"), fadeOut = titles.getInt("fade-out");
							MessageUtil.sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
						}, delay);
					}
				}
				ConfigurationSection commandSection = worldSection.getConfigurationSection("command");
				if (commandSection != null)
				{
					ConfigurationSection conditionSction = commandSection.getConfigurationSection("conditions");
					if (conditionSction == null || a(player, event, conditionSction))
					{
						List<String> commands = commandSection.getStringList("commands");
						for (String command : commands)
						{
							Method.performCommand(player, command, true, true, null);
						}
					}
				}
			}
		}
	}

	private boolean a(@NotNull Player player, @NotNull PlayerChangedWorldEvent event, @NotNull ConfigurationSection conditionSection)
	{
		String permission = conditionSection.getString("permission");
		if (permission != null)
		{
			if (!conditionSuccess(player, event, ConditionType.PERMISSION, permission))
				return false;
		}
		String beforeWorld = conditionSection.getString("before-world");
		if (beforeWorld != null)
		{
			if (!conditionSuccess(player, event, ConditionType.BEFORE_WORLD, beforeWorld))
				return false;
		}
		return !(conditionSection.get("chance") instanceof Double chance) || conditionSuccess(player, event, ConditionType.CHANCE, chance);
	}

	private boolean conditionSuccess(@NotNull Player player, @NotNull PlayerChangedWorldEvent event, @NotNull ConditionType conditionType, @NotNull Object value)
	{
		switch (conditionType)
		{
			case PERMISSION -> {
				return value instanceof String s && player.hasPermission(s);
			}
			case BEFORE_WORLD -> {
				return value instanceof String w && event.getFrom().getName().equals(value);
			}
			case CHANCE -> {
				return value instanceof Double d && Math.random() * 100d < d;
			}
		}
		return false;
	}

	private enum ConditionType
	{
		PERMISSION, BEFORE_WORLD, CHANCE
	}
}

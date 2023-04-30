package com.jho5245.cucumbery.listeners.player.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.custom_mining.MiningScheduler;
import com.jho5245.cucumbery.util.blockplacedata.BlockPlaceDataConfig;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.PlaceHolderUtil;
import com.jho5245.cucumbery.util.no_groups.Scheduler;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class PlayerChangedWorld implements Listener
{
  @EventHandler
  public void onPlayerChangedWorld(PlayerChangedWorldEvent event)
  {
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();
    Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> ItemStackUtil.updateInventory(player), 0L);
    if (Cucumbery.using_ProtocolLib)
    {
      BlockPlaceDataConfig.onPlayerChangedWorld(event);
    }
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
            Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
            {
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
    if (Permission.CMD_STASH.has(player) && Variable.itemStash.containsKey(uuid) && !Variable.itemStash.get(uuid).isEmpty())
    {
      MessageUtil.sendMessage(player, Prefix.INFO_STASH, "보관함에 아이템이 %s개 있습니다. %s 명령어로 확인하세요!", Variable.itemStash.get(uuid).size(), "rg255,204;/stash");
    }
    for (int i = 0; i < 10; i++)
    {
      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        Scheduler.fakeBlocksAsync(player, true);
        MiningScheduler.customMining(player, true);
      }, i * 20);
    }
  }

  private boolean a(@NotNull Player player, @NotNull PlayerChangedWorldEvent event, @NotNull ConfigurationSection conditionSection)
  {
    String permission = conditionSection.getString("permission");
    if (permission != null)
    {
      if (!conditionSuccess(player, event, ConditionType.PERMISSION, permission))
      {
        return false;
      }
    }
    String beforeWorld = conditionSection.getString("before-world");
    if (beforeWorld != null)
    {
      if (!conditionSuccess(player, event, ConditionType.BEFORE_WORLD, beforeWorld))
      {
        return false;
      }
    }
    return !(conditionSection.get("chance") instanceof Double chance) || conditionSuccess(player, event, ConditionType.CHANCE, chance);
  }

  private boolean conditionSuccess(@NotNull Player player, @NotNull PlayerChangedWorldEvent event, @NotNull ConditionType conditionType, @NotNull Object value)
  {
    switch (conditionType)
    {
      case PERMISSION ->
      {
        return value instanceof String s && player.hasPermission(s);
      }
      case BEFORE_WORLD ->
      {
        return value instanceof String w && event.getFrom().getName().equals(value);
      }
      case CHANCE ->
      {
        return value instanceof Double d && Math.random() * 100d < d;
      }
    }
    return false;
  }

  private enum ConditionType
  {
    PERMISSION,
    BEFORE_WORLD,
    CHANCE
  }
}

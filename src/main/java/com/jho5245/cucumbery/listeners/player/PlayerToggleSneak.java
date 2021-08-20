package com.jho5245.cucumbery.listeners.player;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.CustomConfig;
import com.jho5245.cucumbery.util.storage.data.Variable;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTList;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerToggleSneak implements Listener
{
  @EventHandler
  public void onPlayerToggleSneak(PlayerToggleSneakEvent event)
  {
    Player player = event.getPlayer();
    if (player.isSneaking())
    {
      return;
    }
    this.itemSneakUsage(player, player.getInventory().getItemInMainHand(), true);
    this.itemSneakUsage(player, player.getInventory().getItemInOffHand(), false);
  }

  private void itemSneakUsage(Player player, ItemStack item, boolean mainHand)
  {
    UUID uuid = player.getUniqueId();
    NBTCompound itemTag = NBTAPI.getMainCompound(item);
    NBTCompound usageTag = NBTAPI.getCompound(itemTag, CucumberyTag.USAGE_KEY);
    NBTCompound usageSneakTag = NBTAPI.getCompound(usageTag, CucumberyTag.USAGE_COMMANDS_SNEAK_KEY);
    NBTCompound cooldownTag = NBTAPI.getCompound(usageSneakTag, CucumberyTag.COOLDOWN_KEY);
    if (!CustomConfig.UserData.EVENT_EXCEPTION_ACCESS.getBoolean(uuid) && cooldownTag != null)
    {
      try
      {
        long cooldownTime = cooldownTag.getLong(CucumberyTag.TIME_KEY);
        String cooldownTagTag = cooldownTag.getString(CucumberyTag.TAG_KEY);
        YamlConfiguration configPlayerCooldown = Variable.cooldownsItemUsage.get(uuid);
        long nextAvailable = configPlayerCooldown == null ? 0 : configPlayerCooldown.getLong(cooldownTagTag);
        long currentTime = System.currentTimeMillis();
        if (currentTime < nextAvailable)
        {
          return;
        }
        if (configPlayerCooldown == null)
        {
          configPlayerCooldown = new YamlConfiguration();
        }
        configPlayerCooldown.set(cooldownTagTag, currentTime + cooldownTime);
        Variable.cooldownsItemUsage.put(uuid, configPlayerCooldown);
      }
      catch (Exception e)
      {
        e.printStackTrace();
        MessageUtil.broadcastDebug("오류");
        // DO NOTHING
      }
    }
    if (usageSneakTag != null)
    {
      NBTList<String> commandsTag = NBTAPI.getStringList(usageSneakTag, CucumberyTag.USAGE_COMMANDS_KEY);
      if (commandsTag != null)
      {
        for (String command : commandsTag)
        {
          Method.performCommand(player, command, true, true, null);
        }
      }
      if (usageSneakTag.hasKey(CucumberyTag.USAGE_DISPOSABLE_KEY))
      {
        double disposableChance = 100d;
        if (usageSneakTag.hasKey(CucumberyTag.USAGE_DISPOSABLE_KEY))
        {
          disposableChance = usageSneakTag.getDouble(CucumberyTag.USAGE_DISPOSABLE_KEY);
        }
        if (Math.random() * 100d < disposableChance && player.getGameMode() != GameMode.CREATIVE)
        {
          item.setAmount(item.getAmount() - 1);
          if (mainHand)
          {
            player.getInventory().setItemInMainHand(item);
          }
          else
          {
            player.getInventory().setItemInOffHand(item);
          }
        }
      }
    }
  }
}

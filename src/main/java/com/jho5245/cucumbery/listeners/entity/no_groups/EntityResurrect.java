package com.jho5245.cucumbery.listeners.entity.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTList;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class EntityResurrect implements Listener
{
  @EventHandler
  public void onEntityResurrect(EntityResurrectEvent event)
  {
    LivingEntity livingEntity = event.getEntity();
    EntityEquipment entityEquipment = livingEntity.getEquipment();
    ItemStack totem = null;
    EquipmentSlot equipmentSlot = null;
    if (entityEquipment != null)
    {
      ItemStack item = entityEquipment.getItemInMainHand();
      boolean mainHandUse = false;
      if (ItemStackUtil.itemExists(item) && item.getType() == Material.TOTEM_OF_UNDYING)
      {
        mainHandUse = true;
        totem = item.clone();
        equipmentSlot = EquipmentSlot.HAND;
      }
      if (!mainHandUse)
      {
        item = entityEquipment.getItemInOffHand();
        if (ItemStackUtil.itemExists(item) && item.getType() == Material.TOTEM_OF_UNDYING)
        {
          totem = item.clone();
          equipmentSlot = EquipmentSlot.OFF_HAND;
        }
      }
    }

    if (totem != null)
    {
      NBTCompound itemTag = NBTAPI.getMainCompound(totem);
      NBTCompound usageTag = NBTAPI.getCompound(itemTag, CucumberyTag.USAGE_KEY);
      NBTCompound usageResurrectTag = NBTAPI.getCompound(usageTag, CucumberyTag.USAGE_COMMANDS_RESURRECT_KEY);
      if (livingEntity instanceof Player player)
      {
        UUID uuid = player.getUniqueId();
        NBTCompound cooldownTag = NBTAPI.getCompound(usageResurrectTag, CucumberyTag.COOLDOWN_KEY);
        if (!CustomConfig.UserData.EVENT_EXCEPTION_ACCESS.getBoolean(player.getUniqueId()) && cooldownTag != null)
        {
          try
          {
            long cooldownTime = cooldownTag.getLong(CucumberyTag.TIME_KEY);
            String cooldownTagTag = cooldownTag.getString(CucumberyTag.TAG_KEY);
            YamlConfiguration configPlayerCooldown = Variable.cooldownsItemUsage.get(uuid);
            long nextAvailable = configPlayerCooldown == null ? 0 : configPlayerCooldown.getLong(cooldownTagTag);
            long currentTime = System.currentTimeMillis();
            String remainTime = Method.timeFormatMilli(nextAvailable - currentTime);
            if (currentTime < nextAvailable)
            {
              MessageUtil.sendWarn(player, ComponentUtil.translate("아직 %s을(를) 사용할 수 없어 사망했습니다 (남은 시간 : %s)", totem, Constant.THE_COLOR_HEX + remainTime));
              event.setCancelled(true);
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
Cucumbery.getPlugin().getLogger().warning(            e.getMessage());
            MessageUtil.broadcastDebug("오류");
            // DO NOTHING
          }
        }
      }

      if (usageResurrectTag != null)
      {
        NBTList<String> commandsTag = NBTAPI.getStringList(usageResurrectTag, CucumberyTag.USAGE_COMMANDS_KEY);
        if (commandsTag != null)
        {
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
          {
            for (String command : commandsTag)
            {
              Method.performCommand(livingEntity, command, true, true, event);
            }
          }, 0L);
        }
        if (usageResurrectTag.hasTag(CucumberyTag.USAGE_DISPOSABLE_KEY))
        {
          double disposableChance = 100d;
          if (usageResurrectTag.hasTag(CucumberyTag.USAGE_DISPOSABLE_KEY))
          {
            disposableChance = usageResurrectTag.getDouble(CucumberyTag.USAGE_DISPOSABLE_KEY);
          }
          if (Math.random() * 100d >= disposableChance && (livingEntity instanceof Player && ((Player) livingEntity).getGameMode() != GameMode.CREATIVE))
          {
            ItemStack finalItem = totem.clone();
            EquipmentSlot finalEquipmentSlot = equipmentSlot;
            Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                    entityEquipment.setItem(finalEquipmentSlot, finalItem), 0L);
          }
        }
      }
    }
  }
}

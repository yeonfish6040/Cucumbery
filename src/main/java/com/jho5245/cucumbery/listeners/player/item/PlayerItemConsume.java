package com.jho5245.cucumbery.listeners.player.item;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.SoundPlay;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTList;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

public class PlayerItemConsume implements Listener
{
  @EventHandler
  public void onPlayerItemConsume(PlayerItemConsumeEvent event)
  {
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();
    ItemStack item = event.getItem();
    if (event.isCancelled())
    {
      return;
    }
    if (!Cucumbery.config.getBoolean("grant-default-permission-to-players") && !Permission.EVENT_ITEM_CONSUME.has(player))
    {
      event.setCancelled(true);
      if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.itemConsumeAlertCooldown.contains(uuid))
      {
        Variable.itemConsumeAlertCooldown.add(uuid);
        MessageUtil.sendTitle(player, "&c섭취 불가!", "&r아이템을 사용할 권한이 없습니다.", 5, 80, 15);
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.itemConsumeAlertCooldown.remove(uuid), 100L);
      }
      return;
    }
    if (!Permission.EVENT2_ANTI_ALLPLAYER.has(player) && Constant.AllPlayer.ITEM_CONSUME.isEnabled())
    {
      event.setCancelled(true);
      if (!Variable.itemConsumeAlertCooldown.contains(uuid))
      {
        Variable.itemConsumeAlertCooldown.add(uuid);
        MessageUtil.sendMessage(player, Prefix.INFO_ALLPLAYER, "아이템을 사용할 수 없는 상태입니다.");
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.itemConsumeAlertCooldown.remove(uuid), 100L);
      }
      return;
    }
    if (NBTAPI.isRestricted(player, item, RestrictionType.NO_CONSUME) && !UserData.EVENT_EXCEPTION_ACCESS.getBoolean(player.getUniqueId()))
    {
      event.setCancelled(true);
      if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.itemConsumeAlertCooldown2.contains(uuid))
      {
        Variable.itemConsumeAlertCooldown2.add(uuid);
        MessageUtil.sendTitle(player, "&c섭취 불가!", "&r사용할 수 없는 아이템입니다.", 5, 80, 15);
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.itemConsumeAlertCooldown2.remove(uuid), 100L);
      }
      return;
    }

    NBTCompound itemTag = NBTAPI.getMainCompound(item);
    NBTCompound usageTag = NBTAPI.getCompound(itemTag, CucumberyTag.USAGE_KEY);
    NBTCompound usageConsumeTag = NBTAPI.getCompound(usageTag, CucumberyTag.USAGE_COMMANDS_CONSUME_KEY);
    Material type = item.getType();
    if (type == Material.HONEY_BOTTLE && player.getInventory().firstEmpty() == -1 && player.getGameMode() != GameMode.CREATIVE && !UserData.EVENT_EXCEPTION_ACCESS.getBoolean(player) && usageConsumeTag != null)
    {
      event.setCancelled(true);
      if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.itemConsumeAlertCooldown2.contains(uuid))
      {
        Variable.itemConsumeAlertCooldown2.add(uuid);
        String itemName = (item).toString();
        MessageUtil.sendTitle(player, "&c섭취 불가!",
                "&r인벤토리가 가득 찬 상태에서는 &e" + itemName + "&r" + MessageUtil.getFinalConsonant(itemName, MessageUtil.ConsonantType.을를) + " 섭취할 수 없습니다.", 5, 120, 15);
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.itemConsumeAlertCooldown2.remove(uuid), 100L);
      }
      return;
    }
    // 아이템 사용 후 자동 지급되는 아이템에 아이템 설명 추가
    if (player.getGameMode() != GameMode.CREATIVE && (type == Material.MILK_BUCKET || type == Material.POTION || type == Material.MUSHROOM_STEW
            || type == Material.RABBIT_STEW || type == Material.SUSPICIOUS_STEW || type == Material.BEETROOT_SOUP || type == Material.HONEY_BOTTLE))
    {
      if (Method.usingLoreFeature(player))
      {
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
        {
          if (item.getAmount() != 1)
          {
            ItemStack item2 = null;
            PlayerInventory inv = player.getInventory();
            for (int i = 0; i < inv.getSize(); i++)
            {
              item2 = inv.getItem(i);
              if (ItemStackUtil.itemExists(item2) && !ItemStackUtil.hasLore(item2))
              {
                Material type2 = item2.getType();
                if (type2 == Material.GLASS_BOTTLE)
                {
                  break;
                }
              }
            }
            if (ItemStackUtil.itemExists(item2) && !ItemStackUtil.hasLore(item2))
            {
              Material type2 = item2.getType();
              if (type2 == Material.GLASS_BOTTLE)
              {
                ItemStack newItem = item2.clone();
                inv.remove(item2);
                ItemLore.setItemLore(newItem);
                ItemStack offHand = inv.getItemInOffHand();
                if (ItemStackUtil.itemExists(offHand))
                {
                  type2 = offHand.getType();
                  if (type2 == Material.GLASS_BOTTLE)
                  {
                    if (ItemStackUtil.itemEquals(newItem, offHand))
                    {
                      int remainSpace = offHand.getMaxStackSize() - offHand.getAmount();
                      if (remainSpace > newItem.getAmount())
                      {
                        offHand.setAmount(offHand.getAmount() + newItem.getAmount());
                        newItem.setAmount(0);
                      }
                      else
                      {
                        offHand.setAmount(offHand.getMaxStackSize());
                        newItem.setAmount(newItem.getAmount() - remainSpace);
                      }
                    }
                  }
                }
                if (newItem.getAmount() > 0)
                {
                  inv.addItem(newItem);
                }
                Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Method.updateInventory(player), 0L);
              }
            }
          }
          Method.updateInventory(player);
        }, 0L);
      }
    }

    this.consumeItemUsage(event, player, item, false);
    if (player.isSneaking())
    {
      this.consumeItemUsage(event, player, item, true);
    }
  }

  private void consumeItemUsage(PlayerItemConsumeEvent event, Player player, ItemStack item, boolean isSneaking)
  {
    UUID uuid = player.getUniqueId();
    NBTCompound itemTag = NBTAPI.getMainCompound(item);
    NBTCompound usageTag = NBTAPI.getCompound(itemTag, CucumberyTag.USAGE_KEY);
    NBTCompound usageConsumeTag = NBTAPI.getCompound(usageTag, isSneaking ? CucumberyTag.USAGE_COMMANDS_SNEAK_CONSUME_KEY : CucumberyTag.USAGE_COMMANDS_CONSUME_KEY);
    NBTCompound foodTag = NBTAPI.getCompound(itemTag, CucumberyTag.FOOD_KEY);
    if (foodTag != null)
    {
      int originFoodLevel = player.getFoodLevel();
      double originSaturation = player.getSaturation();
      Collection<PotionEffect> originEffects = player.getActivePotionEffects();

      if (foodTag.hasKey(CucumberyTag.FOOD_DISABLE_STATUS_EFFECT_KEY) && foodTag.getBoolean(CucumberyTag.FOOD_DISABLE_STATUS_EFFECT_KEY))
      {
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
        {
          for (PotionEffectType potionEffectType : PotionEffectType.values())
          {
            player.removePotionEffect(potionEffectType);
          }
          player.addPotionEffects(originEffects);
        }, 0L);
      }

      if (foodTag.getInteger(CucumberyTag.FOOD_LEVEL_KEY) != null)
      {
        int itemFoodLevel = foodTag.getInteger(CucumberyTag.FOOD_LEVEL_KEY);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                player.setFoodLevel(Math.min(20, Math.max(0, originFoodLevel + itemFoodLevel))), 0L);
      }

      if (foodTag.getDouble(CucumberyTag.SATURATION_KEY) != null)
      {
        double itemSaturation = foodTag.getDouble(CucumberyTag.SATURATION_KEY);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                player.setSaturation((float) Math.min(20d, Math.max(0d, originSaturation + itemSaturation))), 0L);
      }
    }
    NBTCompound cooldownTag = NBTAPI.getCompound(usageConsumeTag, CucumberyTag.COOLDOWN_KEY);
    if (!UserData.EVENT_EXCEPTION_ACCESS.getBoolean(player.getUniqueId()) && cooldownTag != null)
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
          String itemName = (item).toString();
          Method.playWarnSound(player);
          MessageUtil.sendMessage(player, ComponentUtil.create(Prefix.INFO_WARN + "아직 &e"), ComponentUtil.create(itemName, item),
                  ComponentUtil.create(MessageUtil.getFinalConsonant(itemName, MessageUtil.ConsonantType.을를) + (isSneaking ? "웅크리고 " : "") + " 섭취할 수 없습니다. (남은 시간 : &e" + remainTime + "&r)"));
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
        e.printStackTrace();
        MessageUtil.broadcastDebug("오류");
        // DO NOTHING
      }
    }
    if (usageConsumeTag != null)
    {
      NBTList<String> commandsTag = NBTAPI.getStringList(usageConsumeTag, CucumberyTag.USAGE_COMMANDS_KEY);
      if (commandsTag != null)
      {
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
        {
          for (String command : commandsTag)
          {
            Method.performCommand(player, command, true, true, event);
          }
        }, 0L);
      }
      EquipmentSlot slot = ItemStackUtil.getPlayerUsingSlot(player, new HashSet<>(Collections.singletonList(item.getType())));
      if (usageConsumeTag.hasKey(CucumberyTag.USAGE_DISPOSABLE_KEY))
      {
        double disposableChance = 100d;
        if (usageConsumeTag.hasKey(CucumberyTag.USAGE_DISPOSABLE_KEY))
        {
          disposableChance = usageConsumeTag.getDouble(CucumberyTag.USAGE_DISPOSABLE_KEY);
        }
        if (Math.random() * 100d >= disposableChance && player.getGameMode() != GameMode.CREATIVE)
        {
          Variable.playerItemConsumeCauseSwapCooldown.add(uuid);
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerItemConsumeCauseSwapCooldown.remove(uuid), 0L);
          ItemStack finalItem = item.clone();
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
          {
            player.getInventory().setItem(slot, finalItem);
            if (item.getType() == Material.HONEY_BOTTLE)
            {
              ItemStack glassBottle = ItemStackUtil.loredItemStack(Material.GLASS_BOTTLE);
              player.getInventory().removeItem(glassBottle);
            }
          }, 0L);

        }
      }
    }
  }
}
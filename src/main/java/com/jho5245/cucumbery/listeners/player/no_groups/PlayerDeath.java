package com.jho5245.cucumbery.listeners.player.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.custom_mining.MiningManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectRemoveEvent.RemoveReason;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class PlayerDeath implements Listener
{
  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Player player = event.getEntity();
    if (UserData.SPECTATOR_MODE.getBoolean(player))
    {
      event.setCancelled(true);
      MessageUtil.info(player, "관전 모드여서 죽지 않았습니다");
      return;
    }
    if (UserData.GOD_MODE.getBoolean(player))
    {
      event.setCancelled(true);
      return;
    }
    boolean keepInv = UserData.SAVE_INVENTORY_UPON_DEATH.getBoolean(player) || CustomEffectManager.hasEffect(player, CustomEffectType.KEEP_INVENTORY) || CustomEffectManager.hasEffect(player, CustomEffectType.ADVANCED_KEEP_INVENTORY);
    boolean keepExp = UserData.SAVE_EXPERIENCE_UPON_DEATH.getBoolean(player) || CustomEffectManager.hasEffect(player, CustomEffectType.KEEP_INVENTORY) || CustomEffectManager.hasEffect(player, CustomEffectType.ADVANCED_KEEP_INVENTORY);
    if (keepExp)
    {
      event.setKeepLevel(true);
      event.setDroppedExp(0);
    }

    if (!keepInv && CustomEffectManager.hasEffect(player, CustomEffectType.CURSE_OF_INVENTORY))
    {
      event.setKeepInventory(false);
    }

    for (Iterator<ItemStack> iterator = event.getDrops().iterator(); iterator.hasNext(); )
    {
      ItemStack drop = iterator.next();
      ItemMeta itemMeta = drop.getItemMeta();
      if (keepInv || CustomEnchant.isEnabled() && itemMeta.hasEnchant(CustomEnchant.KEEP_INVENTORY) || NBTAPI.isRestricted(player, drop, RestrictionType.NO_TRADE))
      {
        iterator.remove();
        event.getItemsToKeep().add(drop);
      }
    }

    boolean hasBuffFreeze = CustomEffectManager.hasEffect(player, CustomEffectType.BUFF_FREEZE) || CustomEffectManager.hasEffect(player, CustomEffectType.BUFF_FREEZE_D);
    if (hasBuffFreeze)
    {
      Collection<PotionEffect> potionEffects = new ArrayList<>(player.getActivePotionEffects());
      potionEffects.removeIf(e -> CustomEffectManager.isVanillaNegative(e.getType()));
      Variable.buffFreezerEffects.put(player.getUniqueId(), potionEffects);
      CustomEffectManager.removeEffect(player, CustomEffectType.BUFF_FREEZE);
    }
    List<CustomEffect> customEffects = CustomEffectManager.getEffects(player);
    for (CustomEffect customEffect : customEffects)
    {
      if (!customEffect.isKeepOnDeath() &&
              (!hasBuffFreeze || (!customEffect.getType().isBuffFreezable() && !customEffect.getType().isNegative())))
      {
        CustomEffectManager.removeEffect(player, customEffect.getType(), RemoveReason.DEATH);
      }
    }
    if (UserData.IMMEDIATE_RESPAWN.getBoolean(player))
    {
      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> player.spigot().respawn(), 0L);
    }
    MiningManager.quitCustomMining(player);
    if (UserData.SHOW_DEATH_LOCATION_ON_CHAT.getBoolean(player))
    {
      Location location = player.getLocation();
      MessageUtil.info(player, "죽은 위치 : %s", location);
    }
  }
}

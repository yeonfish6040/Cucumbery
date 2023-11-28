package com.jho5245.cucumbery.listeners.player.item;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.itemlore.ItemLoreView;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerItemDamage implements Listener
{
  @EventHandler
  public void onPlayerItemDamage(PlayerItemDamageEvent event)
  {
    Player player = event.getPlayer();
    ItemStack item = event.getItem();
    int damage = event.getDamage();
    if (event.isCancelled())
    {
      return;
    }
    int amount = item.getAmount();
    NBTItem nbtItem = new NBTItem(item);
    NBTCompound itemTag = nbtItem.getCompound(CucumberyTag.KEY_MAIN);
    NBTCompound duraTag = NBTAPI.getCompound(itemTag, CucumberyTag.CUSTOM_DURABILITY_KEY);
    if (duraTag != null)
    {
      final boolean hasCustomDura = duraTag.getLong(CucumberyTag.CUSTOM_DURABILITY_MAX_KEY) != 0;
      final long maxDurability = hasCustomDura ? duraTag.getLong(CucumberyTag.CUSTOM_DURABILITY_MAX_KEY) : item.getType().getMaxDurability();
      long currentDurability = hasCustomDura ? duraTag.getLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY) : ((Damageable) item.getItemMeta()).getDamage();
      final double chanceNotToConsumeDura = duraTag.getDouble(CucumberyTag.CUSTOM_DURABILITY_CHANCE_NOT_TO_CONSUME_DURABILITY);
      if (Math.random() >= chanceNotToConsumeDura / 100d)
      {
        currentDurability += damage;
      }
      else if (!hasCustomDura)
      {
        event.setDamage(0);
      }
      if (hasCustomDura)
      {
        final double ratio = Math.min(1, Math.max(0, (maxDurability - currentDurability * 1d) / maxDurability)); // 내구도 가득참:0, 부서지ㅣ기 직전: 1
        // 아이템의 순수 내구도
        int itemDurability = (int) (item.getType().getMaxDurability() * ratio);
        if (item.getType() == Material.ELYTRA)
        {
          if (currentDurability == maxDurability - 1)
          {
            duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY, currentDurability);
            duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_MAX_KEY, maxDurability);
            ItemStack itemClone = nbtItem.getItem();
            ItemMeta itemMeta = itemClone.getItemMeta();
            event.getItem().setItemMeta(itemMeta);
            if (Method.usingLoreFeature(player))
            {
              Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> ItemLore.setItemLore(event.getItem(), new ItemLoreView(player)), 0L);
            }
            return;
          }
          if (currentDurability < maxDurability - 1 && itemDurability == Material.ELYTRA.getMaxDurability() - 1)
          {
            duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY, currentDurability);
            duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_MAX_KEY, maxDurability);
            ItemStack itemClone = nbtItem.getItem();
            ItemMeta itemMeta = itemClone.getItemMeta();
            event.getItem().setItemMeta(itemMeta);
            if (Method.usingLoreFeature(player))
            {
              Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> ItemLore.setItemLore(event.getItem(), new ItemLoreView(player)), 0L);
            }
            return;
          }
        }

        if (ratio < 1d && itemDurability < 1)
        {
          itemDurability = 1;
        }
        if (currentDurability >= maxDurability)
        {
          if (CustomConfig.UserData.SHOW_ITEM_BREAK_TITLE.getBoolean(player.getUniqueId()) && Cucumbery.config.getBoolean("send-title-on-item-break"))
          {
            if (!Cucumbery.getPlugin().getConfig().getStringList("no-send-title-on-item-break-worlds").contains(player.getLocation().getWorld().getName()))
            {
              MessageUtil.sendTitle(player, ComponentUtil.translate("&c장비 파괴됨!"),
                      ComponentUtil.translate("rg255,204;인벤토리 아이템 중 %s이(가) 파괴되었습니다", item), 5, 100, 15);
            }
          }
          Method.playSound(player, Sound.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS);
          Method.itemBreakParticle(player, item);

          if (amount == 1)
          {
            event.getItem().setAmount(0);
          }
          else
          {
            duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY, 0L);
            duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_MAX_KEY, maxDurability);
            ItemStack itemClone = nbtItem.getItem();
            ItemMeta itemMeta = itemClone.getItemMeta();
            event.getItem().setItemMeta(itemMeta);
            Damageable duraMeta = (Damageable) itemMeta;
            event.setDamage(0);
            duraMeta.setDamage(0);
            event.getItem().setItemMeta(duraMeta);
            event.getItem().setAmount(amount - 1);
          }
        }
        else
        {
          duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY, currentDurability);
          duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_MAX_KEY, maxDurability);
          ItemStack itemClone = nbtItem.getItem();
          ItemMeta itemMeta = itemClone.getItemMeta();
          event.getItem().setItemMeta(itemMeta);
          Damageable duraMeta = (Damageable) itemMeta;
          event.setDamage(0);
          if (currentDurability >= maxDurability)
          {
            duraMeta.setDamage(itemDurability - 1);
          }
          else
          {
            duraMeta.setDamage(0);
          }
          event.getItem().setItemMeta(duraMeta);
        }
      }
    }
    if (Method.usingLoreFeature(player))
    {
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> ItemLore.setItemLore(event.getItem(), ItemLoreView.of(player)), 0L);
    }
  }
}

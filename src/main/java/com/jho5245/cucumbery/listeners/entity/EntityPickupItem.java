package com.jho5245.cucumbery.listeners.entity;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.SoundPlay;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class EntityPickupItem implements Listener
{
  @EventHandler
  public void onEntityPickupItem(EntityPickupItemEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    LivingEntity entity = event.getEntity();
    Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
    {
      List<Entity> entities = entity.getNearbyEntities(3, 3, 3);
      for (Entity entity1 : entities)
      {
        if (entity1 instanceof Item item)
        {
          Method.updateItem(item);
        }
      }
    }, 0L);

    if (entity.getType() == EntityType.PLAYER)
    {
      Player player = (Player) entity;
      UUID uuid = player.getUniqueId();

      // 아이템 섭취 사용에서 사라지지 않을 경우 아이템 소실 방지를 위한 쿨타임
      if (Variable.playerItemConsumeCauseSwapCooldown.contains(uuid))
      {
        event.setCancelled(true);
        return;
      }
      // 아이템 줍기 모드가 비활성화 되어 있을때
      if (UserData.ITEM_PICKUP_MODE.getString(player.getUniqueId()).equals("disabled"))
      {
        event.setCancelled(true);
        return;
      }
      // 아이템 줍기 모드가 시프트 드롭일때 시프트 상태가 아니면
      else if (UserData.ITEM_PICKUP_MODE.getString(player.getUniqueId()).equals("sneak"))
      {
        if (!player.isSneaking())
        {
          event.setCancelled(true);
          return;
        }
      }
      if (Variable.scrollReinforcing.contains(uuid))
      {
        event.setCancelled(true);
        if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.itemPickupScrollReinforcingAlertCooldown.contains(uuid))
        {
          Variable.itemPickupScrollReinforcingAlertCooldown.add(uuid);
          MessageUtil.sendError(player, "강화중에는 아이템을 주울 수 없습니다.");
          SoundPlay.playSound(player, Constant.ERROR_SOUND);
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.itemPickupScrollReinforcingAlertCooldown.remove(uuid), 100L);
        }
        return;
      }
      if (!Cucumbery.config.getBoolean("grant-default-permission-to-players") && !Permission.EVENT_ITEM_PICKUP.has(player))
      {
        event.setCancelled(true);
        if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.itemPickupAlertCooldown.contains(uuid))
        {
          Variable.itemPickupAlertCooldown.add(uuid);
          MessageUtil.sendTitle(player, "&c줍기 불가!", "&r아이템을 주울 권한이 없습니다.", 5, 80, 15);
          SoundPlay.playSound(player, Constant.ERROR_SOUND);
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.itemPickupAlertCooldown.remove(uuid), 100L);
        }
        return;
      }
      if (!Permission.EVENT2_ANTI_ALLPLAYER.has(player) && Constant.AllPlayer.ITEM_PICKUP.isEnabled())
      {
        event.setCancelled(true);
        if (!Variable.itemPickupAlertCooldown.contains(uuid))
        {
          Variable.itemPickupAlertCooldown.add(uuid);
          MessageUtil.sendMessage(player, Prefix.INFO_ALLPLAYER, "아이템을 주울 수 없는 상태입니다.");
          SoundPlay.playSound(player, Constant.ERROR_SOUND);
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.itemPickupAlertCooldown.remove(uuid), 100L);
        }
        return;
      }
      ItemStack item = event.getItem().getItemStack();
      if (NBTAPI.isRestricted(player, item, Constant.RestrictionType.NO_PICKUP))
      {
        event.setCancelled(true);
        if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.itemPickupAlertCooldown2.contains(uuid))
        {
          Variable.itemPickupAlertCooldown2.add(uuid);
          MessageUtil.sendTitle(player, "&c줍기 불가!", "&r주울 수 없는 아이템입니다.(&e" + ComponentUtil.serialize(item.displayName()) + "&r)", 5, 40, 15);
          SoundPlay.playSound(player, Constant.ERROR_SOUND);
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.itemPickupAlertCooldown2.remove(uuid), 100L);
        }
        return;
      }
      if (Method.usingLoreFeature(player))
      {
        ItemLore.setItemLore(item, player, event);
      }
      else
      {
        ItemLore.removeItemLore(item);
      }
      if (!event.isCancelled())
      {
        this.actionbarOnItemPickup(player, item);
      }
    }
  }


  private void actionbarOnItemPickup(Player player, ItemStack item)
  {
    if (UserData.SHOW_ACTIONBAR_ON_ITEM_PICKUP.getBoolean(player.getUniqueId()))
    {
      int amount = item.getAmount();
      Component itemStackComponent = ComponentUtil.itemName(item);
      if (itemStackComponent.color() == null)
      {
        itemStackComponent = itemStackComponent.color(TextColor.fromHexString("#00ff3c"));
      }
      Component amountComponent = ComponentUtil.createTranslate("#00ff3c;%s개", Component.text(amount));
      player.sendActionBar(ComponentUtil.createTranslate("#00ccff;%s %s를 주웠습니다.", itemStackComponent, amountComponent));
    }
  }
}
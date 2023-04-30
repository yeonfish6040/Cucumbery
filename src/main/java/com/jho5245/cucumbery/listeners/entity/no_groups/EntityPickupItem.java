package com.jho5245.cucumbery.listeners.entity.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.children.group.PlayerCustomEffectImple;
import com.jho5245.cucumbery.custom.customeffect.children.group.StringCustomEffectImple;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeRune;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.itemlore.ItemLoreView;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.data.*;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import de.tr7zw.changeme.nbtapi.NBTEntity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EntityPickupItem implements Listener
{
  private final Set<UUID> runeCooldownDelay = new HashSet<>(), runeCooldownDelay2 = new HashSet<>(), runeCooldownDelay3 = new HashSet<>();

  @EventHandler
  public void onEntityPickupItem(EntityPickupItemEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Item item = event.getItem();
    ItemStack itemStack = item.getItemStack();
    LivingEntity entity = event.getEntity();
    UUID uuid = entity.getUniqueId();
    if (entity instanceof Player player)
    {
      if (UserData.SPECTATOR_MODE.getBoolean(player))
      {
        event.setCancelled(true);
        return;
      }
      CustomMaterial customMaterial = CustomMaterial.itemStackOf(itemStack);
      if (customMaterial != null)
      {
        switch (customMaterial)
        {
          case RUNE_DESTRUCTION, RUNE_EARTHQUAKE ->
          {
            event.setCancelled(true);
            if (player.isSneaking())
            {
              if (CustomEffectManager.hasEffect(player, CustomEffectTypeRune.RUNE_USING))
              {
                return;
              }
              if (CustomEffectManager.hasEffect(item, CustomEffectTypeRune.RUNE_OCCUPIED))
              {
                if (!runeCooldownDelay2.contains(uuid))
                {
                  runeCooldownDelay2.add(uuid);
                  MessageUtil.sendTitle(player, "", "g255;이미 다른 플레이어가 룬 해방을 시도하는 중입니다.", 0, 0, 150);
                  Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> runeCooldownDelay2.remove(uuid), 20L);
                }
                return;
              }
              if (CustomEffectManager.hasEffect(player, CustomEffectTypeRune.RUNE_COOLDOWN))
              {
                if (!runeCooldownDelay.contains(uuid))
                {
                  runeCooldownDelay.add(uuid);
                  MessageUtil.sendTitle(player, "", ComponentUtil.translate("g255;아직 효과를 받을 수 없습니다. (남은 시간 %s)", Method.timeFormatMilli(
                          CustomEffectManager.getEffect(player, CustomEffectTypeRune.RUNE_COOLDOWN).getDuration() * 50L, false, 0, true
                  )), 0, 0, 150);
                  Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> runeCooldownDelay.remove(uuid), 20L);
                }
                return;
              }
              if (!ItemStackUtil.itemExists(player.getInventory().getItemInMainHand()))
              {
                if (!runeCooldownDelay3.contains(uuid))
                {
                  runeCooldownDelay3.add(uuid);
                  MessageUtil.sendTitle(player, "", "g255;룬을 해방하려면 손에 아무 아이템을 들고 있어야 합니다.", 0, 0, 150);
                  Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> runeCooldownDelay3.remove(uuid), 20L);
                }
                return;
              }
              if (!CustomEffectManager.hasEffect(player, CustomEffectTypeRune.RUNE_USING))
              {
                StringBuilder s = new StringBuilder();
                for (int i = 0; i < 5; i++)
                {
                  if (Math.random() < 0.5)
                  {
                    s.append("R");
                  }
                  else
                  {
                    s.append("L");
                  }
                }
                CustomEffectManager.addEffect(player, new StringCustomEffectImple(CustomEffectTypeRune.RUNE_USING, s.toString()));
                CustomEffectManager.addEffect(item, new PlayerCustomEffectImple(CustomEffectTypeRune.RUNE_OCCUPIED, player));
              }
            }
            return;
          }
        }
      }
    }
    if (CustomEffectManager.hasEffect(entity, CustomEffectType.CURSE_OF_PICKUP))
    {
      event.setCancelled(true);
      if (entity instanceof Player player)
      {
        if (!Variable.itemPickupAlertCooldown.contains(uuid))
        {
          Variable.itemPickupAlertCooldown.add(uuid);
          MessageUtil.sendError(player, "%s 효과로 인해 아이템을 주울 수 없는 상태입니다",
                  CustomEffectManager.getDisplay(player, Collections.singletonList(CustomEffectManager.getEffect(player, CustomEffectType.CURSE_OF_PICKUP))));
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.itemPickupAlertCooldown.remove(uuid), 100L);
        }
      }
      return;
    }
    if (entity instanceof Player player)
    {
      // 아이템 섭취 사용에서 사라지지 않을 경우 아이템 소실 방지를 위한 쿨타임
      if (Variable.playerItemConsumeCauseSwapCooldown.contains(uuid))
      {
        event.setCancelled(true);
        return;
      }
      // 아이템 줍기 모드가 비활성화 되어 있을때
      if (UserData.ITEM_PICKUP_MODE.getString(uuid).equals("disabled"))
      {
        event.setCancelled(true);
        return;
      }
      // 아이템 줍기 모드가 시프트 드롭일때 시프트 상태가 아니면
      else if (UserData.ITEM_PICKUP_MODE.getString(uuid).equals("sneak"))
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
          MessageUtil.sendError(player, "강화중에는 아이템을 주울 수 없습니다");
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
          MessageUtil.sendTitle(player, "&c줍기 불가!", "&r아이템을 주울 권한이 없습니다", 5, 80, 15);
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
          MessageUtil.sendMessage(player, Prefix.INFO_ALLPLAYER, "아이템을 주울 수 없는 상태입니다");
          SoundPlay.playSound(player, Constant.ERROR_SOUND);
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.itemPickupAlertCooldown.remove(uuid), 100L);
        }
        return;
      }
      if (NBTAPI.isRestricted(player, itemStack, Constant.RestrictionType.NO_PICKUP))
      {
        event.setCancelled(true);
        if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.itemPickupAlertCooldown2.contains(uuid))
        {
          Variable.itemPickupAlertCooldown2.add(uuid);
          MessageUtil.sendTitle(player, ComponentUtil.translate("&c줍기 불가!"), ComponentUtil.translate("주울 수 없는 아이템입니다 (%s)", itemStack), 5, 40, 15);
          SoundPlay.playSound(player, Constant.ERROR_SOUND);
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.itemPickupAlertCooldown2.remove(uuid), 100L);
        }
        return;
      }

/*
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
*/

      if (Method.usingLoreFeature(player))
      {
        ItemLore.setItemLore(itemStack, new ItemLoreView(player));
      }
      else
      {
        ItemLore.removeItemLore(itemStack);
      }
      if (CustomEffectManager.hasEffect(entity, CustomEffectType.DO_NOT_PICKUP_BUT_THROW_IT))
      {
        event.setCancelled(true);
        item.setPickupDelay(40);
        NBTEntity nbtEntity = new NBTEntity(item);
        nbtEntity.setShort("Age", (short) 0);
        item.teleport(entity.getEyeLocation());
        double amplifier = 0.4 * Math.pow(8, 0.1 * CustomEffectManager.getEffect(entity, CustomEffectType.DO_NOT_PICKUP_BUT_THROW_IT).getAmplifier());
        Vector vector = entity.getLocation().getDirection().multiply(amplifier);
        int level = 0;
        if (CustomEffectManager.hasEffect(entity, CustomEffectType.IDIOT_SHOOTER))
        {
          level = CustomEffectManager.getEffect(entity, CustomEffectType.IDIOT_SHOOTER).getAmplifier() + 1;
        }
        if (item.getItemStack().hasItemMeta() && item.getItemStack().getItemMeta().hasEnchants())
        {
          level = Math.max(level, CustomEnchant.isEnabled() ? item.getItemStack().getEnchantmentLevel(CustomEnchant.IDIOT_SHOOTER) : 0);
        }
        if (level > 0)
        {
          double modifier = level / 10d;
          item.setVelocity(vector.add(new Vector(Math.random() * modifier - (modifier / 2d), Math.random() * modifier - (modifier / 2d), Math.random() * modifier - (modifier / 2d))));
        }
        else
        {
          item.setVelocity(vector);
        }
        return;
      }
      if (!event.isCancelled())
      {
        this.actionbarOnItemPickup(player, itemStack, itemStack.getAmount() - event.getRemaining());
      }
    }
  }

  private void actionbarOnItemPickup(@NotNull Player player, @NotNull ItemStack itemStack, int amount)
  {
    if (UserData.SHOW_ACTIONBAR_ON_ITEM_PICKUP.getBoolean(player))
    {
      Component itemStackComponent = ItemNameUtil.itemName(itemStack, TextColor.fromHexString("#00ff3c"));
      if (amount == 1 && itemStack.getType().getMaxStackSize() == 1)
      {
        player.sendActionBar(ComponentUtil.translate("#00ccff;%s을(를) 주웠습니다", itemStackComponent));
      }
      else
      {
        player.sendActionBar(ComponentUtil.translate("#00ccff;%s을(를) %s개 주웠습니다", itemStackComponent, "#00ff3c;" + amount));
      }
    }
  }
}

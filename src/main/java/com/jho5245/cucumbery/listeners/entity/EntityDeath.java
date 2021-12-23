package com.jho5245.cucumbery.listeners.entity;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.customeffect.CustomEffect;
import com.jho5245.cucumbery.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.customeffect.CustomEffectType;
import com.jho5245.cucumbery.deathmessages.DeathManager;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Variable;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import org.bukkit.EntityEffect;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EntityDeath implements Listener
{
  @EventHandler
  public void onEntityDeath(EntityDeathEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }

    LivingEntity entity = event.getEntity();
    if (CustomEffectManager.hasEffect(entity, CustomEffectType.INVINCIBLE))
    {
      event.setCancelled(true);
      return;
    }
    if (CustomEffectManager.hasEffect(entity, CustomEffectType.RESURRECTION))
    {
      event.setCancelled(true);
      CustomEffectManager.removeEffect(entity, CustomEffectType.RESURRECTION);
      CustomEffectManager.addEffect(entity, new CustomEffect(CustomEffectType.RESURRECTION_INVINCIBLE, 20 * 2));
      entity.playEffect(EntityEffect.TOTEM_RESURRECT);
      MessageUtil.info(entity, ComponentUtil.translate("%s 효과로 인해 죽지 않고, 2초간 무적 상태가 됩니다.", CustomEffectType.RESURRECTION));
      return;
    }
    if (entity.getScoreboardTags().contains("invincible"))
    {
      event.setCancelled(true);
    }

    if (entity instanceof Player player)
    {
      if (UserData.SPECTATOR_MODE.getBoolean(player))
      {
        event.setCancelled(true);
        return;
      }
      if (UserData.GOD_MODE.getBoolean(player))
      {
        event.setCancelled(true);
      }
    }

    if (Variable.deathMessages.getBoolean("death-messages.enable"))
    {
      DeathManager.what(event);
    }

    this.telekinesis(event);
  }

  private void telekinesis(EntityDeathEvent event)
  {
    LivingEntity entity = event.getEntity();
    EntityDamageEvent lastDamageCause = entity.getLastDamageCause();
    if (!(lastDamageCause instanceof EntityDamageByEntityEvent damageEvent))
      return;
    Entity damagerEntity = damageEvent.getDamager();
    EntityType damagerEntityType = damagerEntity.getType();
    if (damagerEntityType != EntityType.PLAYER && !(damagerEntity instanceof Projectile))
      return;
    Player player;
    if (damagerEntityType == EntityType.PLAYER)
    {
      player = (Player) damagerEntity;
    }
    else
    {
      Projectile projectile = (Projectile) damagerEntity;
      if (projectile.getShooter() instanceof Player)
      {
        player = (Player) projectile.getShooter();
      }
      else
        return;
    }
    EntityType entityType = entity.getType();
    boolean entityIsPlayer = entityType == EntityType.PLAYER;
    List<ItemStack> drops = event.getDrops();
    int dropExp = event.getDroppedExp();
    ItemStack maindHand = player.getInventory().getItemInMainHand();
    NBTCompoundList customEnchantsTag = NBTAPI.getCompoundList(NBTAPI.getMainCompound(maindHand), CucumberyTag.CUSTOM_ENCHANTS_KEY);
    boolean hasCoarseTouch = NBTAPI.commpoundListContainsValue(customEnchantsTag, CucumberyTag.ID_KEY, Constant.CustomEnchant.COARSE_TOUCH.toString());
    if (hasCoarseTouch)
    {
      drops.clear();
    }
    boolean hasUnskilledTouch = NBTAPI.commpoundListContainsValue(customEnchantsTag, CucumberyTag.ID_KEY, Constant.CustomEnchant.UNSKILLED_TOUCH.toString());
    if (hasUnskilledTouch)
    {
      event.setDroppedExp(0);
    }
    boolean isTelekinesis = (NBTAPI.commpoundListContainsValue(customEnchantsTag, CucumberyTag.ID_KEY, Constant.CustomEnchant.TELEKINESIS_PVP.toString()) && entityIsPlayer)
            || (NBTAPI.commpoundListContainsValue(customEnchantsTag, CucumberyTag.ID_KEY, Constant.CustomEnchant.TELEKINESIS.toString()) && !entityIsPlayer);
    boolean isSmeltingTouch = NBTAPI.commpoundListContainsValue(customEnchantsTag, CucumberyTag.ID_KEY, Constant.CustomEnchant.SMELTING_TOUCH.toString()) && Cucumbery.config.getBoolean("use-smelting-touch-on-entities");

    if (isTelekinesis || isSmeltingTouch)
    {
      if (!hasUnskilledTouch && isTelekinesis && dropExp > 0)
      {
        ExperienceOrb xpOrb = (ExperienceOrb) player.getWorld().spawnEntity(player.getLocation(), EntityType.EXPERIENCE_ORB);
        xpOrb.setExperience(dropExp);
        event.setDroppedExp(0);
      }
      if (!hasCoarseTouch && drops.size() > 0)
      {
        List<ItemStack> dropsClone = new ArrayList<>(drops);
        drops.clear();
        List<Double> expOutput = new ArrayList<>();
        if (isSmeltingTouch)
          dropsClone = ItemStackUtil.getSmeltedResult(player, dropsClone, expOutput);
        String worldName = player.getLocation().getWorld().getName();
        boolean setItemLore = UserData.USE_HELPFUL_LORE_FEATURE.getBoolean(player.getUniqueId())
                && Cucumbery.config.getBoolean("use-helpful-lore-feature")
                && !Cucumbery.config.getStringList("no-use-helpful-lore-feature-worlds").contains(worldName);
        for (ItemStack dropClone : dropsClone)
        {
          if (isTelekinesis)
          {
            if (setItemLore)
              ItemLore.setItemLore(dropClone);
            else
              ItemLore.removeItemLore(dropClone);
            HashMap<Integer, ItemStack> lostDrops = player.getInventory().addItem(dropClone);
            if (lostDrops.size() > 0)
            {
              for (int j = 0; j < lostDrops.size(); j++)
              {
                entity.getWorld().dropItemNaturally(entity.getLocation(), lostDrops.get(j));
              }
            }
          }
          else
          {
            entity.getWorld().dropItemNaturally(entity.getLocation(), dropClone);
          }
        }
      }
    }
  }
}

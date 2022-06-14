package com.jho5245.cucumbery.listeners.entity.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectType;
import com.jho5245.cucumbery.custom.customeffect.children.group.PlayerCustomEffectImple;
import com.jho5245.cucumbery.deathmessages.DeathManager;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

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
      MessageUtil.info(entity, ComponentUtil.translate("%s 효과로 인해 죽지 않고, 2초간 무적 상태가 됩니다", CustomEffectType.RESURRECTION));
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
      DeathManager.manageDeathMessages(event);
    }

    this.telekinesis(event);
    this.combo(event);
  }

  private void telekinesis(EntityDeathEvent event)
  {
    LivingEntity entity = event.getEntity();
    EntityDamageEvent lastDamageCause = entity.getLastDamageCause();
    if (!(lastDamageCause instanceof EntityDamageByEntityEvent damageEvent))
    {
      return;
    }
    Entity damagerEntity = damageEvent.getDamager();
    EntityType damagerEntityType = damagerEntity.getType();
    if (damagerEntityType != EntityType.PLAYER && !(damagerEntity instanceof Projectile))
    {
      return;
    }
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
      {
        return;
      }
    }
    EntityType entityType = entity.getType();
    boolean entityIsPlayer = entityType == EntityType.PLAYER;
    List<ItemStack> drops = event.getDrops();
    int dropExp = event.getDroppedExp();
    ItemStack mainHand = player.getInventory().getItemInMainHand();
    ItemMeta itemMeta = null;
    if (mainHand.hasItemMeta())
    {
      itemMeta = mainHand.getItemMeta();
    }
    NBTCompoundList customEnchantsTag = NBTAPI.getCompoundList(NBTAPI.getMainCompound(mainHand), CucumberyTag.CUSTOM_ENCHANTS_KEY);
    boolean hasCoarseTouch = itemMeta != null && itemMeta.hasEnchant(CustomEnchant.COARSE_TOUCH);
    if (hasCoarseTouch)
    {
      drops.clear();
    }
    boolean hasUnskilledTouch = itemMeta != null && itemMeta.hasEnchant(CustomEnchant.UNSKILLED_TOUCH);
    if (hasUnskilledTouch)
    {
      event.setDroppedExp(0);
    }
    boolean isTelekinesis = (entityIsPlayer && (
            NBTAPI.commpoundListContainsValue(customEnchantsTag, CucumberyTag.ID_KEY, CustomEnchant.TELEKINESIS_PVP.toString())
            )) ||
            (!entityIsPlayer && (
            NBTAPI.commpoundListContainsValue(customEnchantsTag, CucumberyTag.ID_KEY, CustomEnchant.TELEKINESIS.toString()) ||
                    (itemMeta != null && (itemMeta.hasEnchant(com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant.TELEKINESIS))) ||
                    CustomEffectManager.hasEffect(player, CustomEffectType.TELEKINESIS)
            ));
    boolean isSmeltingTouch = Cucumbery.config.getBoolean("use-smelting-touch-on-entities") && (
                    (itemMeta != null && itemMeta.hasEnchant(CustomEnchant.SMELTING_TOUCH)) ||
                    CustomEffectManager.hasEffect(player, CustomEffectType.SMELTING_TOUCH)
            );

    if (isTelekinesis || isSmeltingTouch)
    {
      if (!hasUnskilledTouch && isTelekinesis && dropExp > 0)
      {
        player.giveExp(dropExp, true);
        event.setDroppedExp(0);
      }
      if (!hasCoarseTouch && !drops.isEmpty())
      {
        List<ItemStack> dropsClone = new ArrayList<>(drops);
        drops.clear();
        List<Double> expOutput = new ArrayList<>();
        if (isSmeltingTouch)
        {
          dropsClone = ItemStackUtil.getSmeltedResult(player, dropsClone, expOutput);
        }
        String worldName = player.getLocation().getWorld().getName();
        boolean setItemLore = UserData.USE_HELPFUL_LORE_FEATURE.getBoolean(player.getUniqueId())
                && Cucumbery.config.getBoolean("use-helpful-lore-feature")
                && !Cucumbery.config.getStringList("no-use-helpful-lore-feature-worlds").contains(worldName);
        for (ItemStack dropClone : dropsClone)
        {
          if (isTelekinesis)
          {
            if (setItemLore)
            {
              ItemLore.setItemLore(dropClone);
            }
            else
            {
              ItemLore.removeItemLore(dropClone);
            }
            HashMap<Integer, ItemStack> lostDrops = player.getInventory().addItem(dropClone);
            if (!lostDrops.isEmpty())
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

  private void combo(EntityDeathEvent event)
  {
    LivingEntity entity = event.getEntity();
    Player killer = entity.getKiller();
    if (killer != null && CustomEffectManager.hasEffect(killer, CustomEffectType.COMBO))
    {
      int stack = 0;
      if (CustomEffectManager.hasEffect(killer, CustomEffectType.COMBO_STACK))
      {
        stack = CustomEffectManager.getEffect(killer, CustomEffectType.COMBO_STACK).getAmplifier() + 1;
      }
      if (!CustomEffectManager.hasEffect(killer, CustomEffectType.COMBO_DELAY))
      {
        CustomEffectManager.removeEffect(killer, CustomEffectType.COMBO_STACK);
      }
      double sec;
      if (stack + 1 < 3000)
      {
        sec = -0.001 * (stack + 1) + 10;
      }
      else {
        sec = 29700d / (stack + 1) - 2.9;
      }
      if (sec < 0.2)
      {
        sec = 0.2;
      }
      CustomEffectManager.addEffect(killer, new CustomEffect(CustomEffectType.COMBO_STACK, (int) Math.ceil(sec * 20), stack));
      CustomEffectManager.addEffect(killer, CustomEffectType.COMBO_DELAY);
      if ((stack + 1) % 10 == 0)
      {
        int finalStack = stack;
        @Nullable Consumer<Entity> consumer = (e) -> {
          ExperienceOrb experienceOrb = (ExperienceOrb) e;
          experienceOrb.setExperience(finalStack + 1);
          experienceOrb.customName(Component.translatable("콤보 구슬", NamedTextColor.YELLOW));
          experienceOrb.setCustomNameVisible(true);
          experienceOrb.setVelocity(new Vector(0, 0, 0));
          experienceOrb.setGravity(false);
          for (Player online : Bukkit.getOnlinePlayers())
          {
            if (killer == online)
            {
              continue;
            }
            online.hideEntity(Cucumbery.getPlugin(), experienceOrb);
          }
          CustomEffectManager.addEffect(experienceOrb, new PlayerCustomEffectImple(CustomEffectType.COMBO_EXPERIENCE, killer));
        };
        entity.getWorld().spawnEntity(entity.getLocation(), EntityType.EXPERIENCE_ORB, SpawnReason.CUSTOM, consumer);
        killer.playSound(entity.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.5F, 0.5F);
      }
    }
  }
}

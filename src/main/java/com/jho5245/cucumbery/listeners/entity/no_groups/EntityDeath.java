package com.jho5245.cucumbery.listeners.entity.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.children.group.PlayerCustomEffectImple;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.deathmessages.DeathManager;
import com.jho5245.cucumbery.util.additemmanager.AddItemUtil;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
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
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
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
    this.closeCall(event);
    if (event.isCancelled())
    {
      return;
    }
    this.telekinesis(event);
    this.combo(event);
  }

  /**
   * 커스텀 인챈트 {@link CustomEnchant#CLOSE_CALL}
   */
  private void closeCall(EntityDeathEvent event)
  {
    if (!CustomEnchant.isEnabled())
    {
      return;
    }
    LivingEntity livingEntity = event.getEntity();
    EntityEquipment equipment = livingEntity.getEquipment();
    if (equipment != null)
    {
      ItemStack chestplate = equipment.getChestplate();
      if (ItemStackUtil.itemExists(chestplate) && chestplate.hasItemMeta() && chestplate.getItemMeta().hasEnchant(CustomEnchant.CLOSE_CALL))
      {
        int level = chestplate.getItemMeta().getEnchantLevel(CustomEnchant.CLOSE_CALL);
        if (Math.random() * 100d < level)
        {
          event.setCancelled(true);
          livingEntity.playEffect(EntityEffect.TOTEM_RESURRECT);
          MessageUtil.info(livingEntity, ComponentUtil.translate("%s 효과로 인해 생존하였습니다", CustomEnchant.CLOSE_CALL));
        }
      }
    }
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
    ItemStack mainHand = player.getInventory().getItemInMainHand();
    ItemMeta itemMeta = null;
    if (mainHand.hasItemMeta())
    {
      itemMeta = mainHand.getItemMeta();
    }
    boolean hasCoarseTouch = CustomEnchant.isEnabled() && itemMeta != null && itemMeta.hasEnchants() && itemMeta.hasEnchant(CustomEnchant.COARSE_TOUCH);
    if (hasCoarseTouch)
    {
      drops.clear();
    }
    boolean hasUnskilledTouch = CustomEnchant.isEnabled() && itemMeta != null && itemMeta.hasEnchants() && itemMeta.hasEnchant(CustomEnchant.UNSKILLED_TOUCH);
    if (hasUnskilledTouch)
    {
      event.setDroppedExp(0);
    }
    boolean isTelekinesis = (entityIsPlayer && CustomEnchant.isEnabled() && itemMeta != null && itemMeta.hasEnchant(CustomEnchant.TELEKINESIS_PVP) ||
            (!entityIsPlayer && (
                    (CustomEnchant.isEnabled() && itemMeta != null && (itemMeta.hasEnchant(CustomEnchant.TELEKINESIS))) ||
                            CustomEffectManager.hasEffect(player, CustomEffectType.TELEKINESIS)
            )));
    boolean isSmeltingTouch = Cucumbery.config.getBoolean("use-smelting-touch-on-entities") && (
            (CustomEnchant.isEnabled() && itemMeta != null && itemMeta.hasEnchant(CustomEnchant.SMELTING_TOUCH)) ||
                    CustomEffectManager.hasEffect(player, CustomEffectType.SMELTING_TOUCH)
    );

    if (isTelekinesis || isSmeltingTouch)
    {
      if (!hasCoarseTouch && !drops.isEmpty())
      {
        List<ItemStack> dropsClone = new ArrayList<>(drops);
        drops.clear();
        List<Double> expOutput = new ArrayList<>();
        if (isSmeltingTouch)
        {
          dropsClone = ItemStackUtil.getSmeltedResult(player, dropsClone, expOutput);
        }
        for (ItemStack dropClone : dropsClone)
        {
          if (isTelekinesis)
          {
            AddItemUtil.addItem(player, dropClone);
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
      else
      {
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
        @Nullable Consumer<Entity> consumer = (e) ->
        {
          ExperienceOrb experienceOrb = (ExperienceOrb) e;
          experienceOrb.setExperience(finalStack + 1);
          experienceOrb.customName(ComponentUtil.translate("콤보 구슬", NamedTextColor.YELLOW));
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

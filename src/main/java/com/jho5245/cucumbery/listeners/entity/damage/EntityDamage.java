package com.jho5245.cucumbery.listeners.entity.damage;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect.DisplayType;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectType;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.MythicMobManager;
import com.jho5245.cucumbery.util.no_groups.NumberHangulConverter;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.NBTList;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Consumer;

import java.util.UUID;

public class EntityDamage implements Listener
{
  @EventHandler
  public void onEntityDamage(EntityDamageEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Entity entity = event.getEntity();
    if (entity instanceof Player player)
    {
      if (UserData.SPECTATOR_MODE.getBoolean(player))
      {
        event.setCancelled(true);
        return;
      }
    }
    this.cancelEntityDamage(event);
    if (!event.isCancelled())
    {
      this.cancelLavaBurnItem(event);
    }
    if (!event.isCancelled())
    {
      this.customEffect(event);
      this.customEnchant(event);
    }
    if (!event.isCancelled())
    {
      if (entity instanceof LivingEntity livingEntity && (livingEntity.getScoreboardTags().contains("remove_no_damage_ticks") || MythicMobManager.hasTag(entity, "remove_no_damage_ticks")))
      {
        Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                livingEntity.setNoDamageTicks(0), 0L);
      }
      this.displayDamage(event);
    }
  }

  private void customEffect(EntityDamageEvent event)
  {
    double damage = event.getDamage();
    // 대미지 % 증가에 영향을 받지 않는 고정 수치
    double fixedDamage = 0d;
    double damageMultiplier = 1d, finalDamageMultiplier = 1d;
    Entity victim = event.getEntity();
    if (CustomEffectManager.hasEffect(victim, CustomEffectType.INVINCIBLE))
    {
      event.setCancelled(true);
      victim.setFireTicks(-20);
      return;
    }
    if (CustomEffectManager.hasEffect(victim, CustomEffectType.RESURRECTION_INVINCIBLE) ||
            CustomEffectManager.hasEffect(victim, CustomEffectType.INVINCIBLE_RESPAWN) ||
            CustomEffectManager.hasEffect(victim, CustomEffectType.INVINCIBLE_PLUGIN_RELOAD))
    {
      event.setCancelled(true);
      return;
    }
    if (CustomEffectManager.hasEffect(victim, CustomEffectType.PARROTS_CHEER))
    {
      finalDamageMultiplier *= 0.55d;
    }
    if (CustomEffectManager.hasEffect(victim, CustomEffectType.DARKNESS_TERROR_ACTIVATED))
    {
      finalDamageMultiplier *= 1.3d;
    }
    if (CustomEffectManager.hasEffect(victim, CustomEffectType.NEWBIE_SHIELD))
    {
      int amplifier = CustomEffectManager.getEffect(victim, CustomEffectType.NEWBIE_SHIELD).getAmplifier() + 1;
      switch (amplifier)
      {
        case 1 -> finalDamageMultiplier *= 0.9;
        case 2 -> finalDamageMultiplier *= 0.8;
        default -> finalDamageMultiplier *= 0.6;
      }
    }
    if (CustomEffectManager.hasEffect(victim, CustomEffectType.TOWN_SHIELD))
    {
      finalDamageMultiplier *= 0.5;
    }
    DamageCause damageCause = event.getCause();
    switch (damageCause)
    {
      case HOT_FLOOR -> {
        if (CustomEffectManager.hasEffect(victim, CustomEffectType.FROST_WALKER))
        {
          event.setCancelled(true);
          return;
        }
      }
      case ENTITY_ATTACK, ENTITY_SWEEP_ATTACK, CUSTOM -> {
        if (event instanceof EntityDamageByEntityEvent damageByEntityEvent)
        {
          Entity damagerEntity = damageByEntityEvent.getDamager();
          if (damagerEntity instanceof AreaEffectCloud areaEffectCloud)
          {
            ProjectileSource source = areaEffectCloud.getSource();
            if (source instanceof Entity thrower)
            {
              if (CustomEffectManager.hasEffect(victim, CustomEffectType.DODGE))
              {
                CustomEffect customEffect = CustomEffectManager.getEffect(damagerEntity, CustomEffectType.DODGE);
                int amplifier = customEffect.getAmplifier() + 1;
                if (Math.random() * 100 < amplifier)
                {
                  event.setCancelled(true);
                  MessageUtil.sendActionBar(victim, "공격을 회피했습니다!");
                  MessageUtil.sendActionBar(thrower, "상대방이 공격을 회피했습니다!");
                  return;
                }
              }
            }
          }
          else
          {
            if (CustomEffectManager.hasEffect(victim, CustomEffectType.DODGE))
            {
              CustomEffect customEffect = CustomEffectManager.getEffect(victim, CustomEffectType.DODGE);
              int amplifier = customEffect.getAmplifier() + 1;
              if (Math.random() * 100 < amplifier)
              {
                event.setCancelled(true);
                MessageUtil.sendActionBar(victim, "공격을 회피했습니다!");
                MessageUtil.sendActionBar(damagerEntity, "상대방이 공격을 회피했습니다!");
                return;
              }
            }
            if (CustomEffectManager.hasEffect(damagerEntity, CustomEffectType.SHARPNESS))
            {
              CustomEffect customEffectSharpness = CustomEffectManager.getEffect(damagerEntity, CustomEffectType.SHARPNESS);
              int amplifier = customEffectSharpness.getAmplifier();
              damage += amplifier + 1.5;
            }
            if (CustomEffectManager.hasEffect(damagerEntity, CustomEffectType.SMITE) && (victim instanceof Zombie || victim instanceof ZombieHorse || victim instanceof AbstractSkeleton || victim instanceof Wither))
            {
              CustomEffect customEffectSmite = CustomEffectManager.getEffect(damagerEntity, CustomEffectType.SMITE);
              int amplifier = customEffectSmite.getAmplifier();
              damage += (amplifier + 1) * 2.5;
            }
            if (CustomEffectManager.hasEffect(damagerEntity, CustomEffectType.BANE_OF_ARTHROPODS) && (victim instanceof Spider || victim instanceof Silverfish || victim instanceof Endermite))
            {
              CustomEffect customEffectBaneOfArthropods = CustomEffectManager.getEffect(damagerEntity, CustomEffectType.BANE_OF_ARTHROPODS);
              int amplifier = customEffectBaneOfArthropods.getAmplifier();
              damage += (amplifier + 1) * 2.5;
              ((Monster) victim).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) (20 + Math.random() * (amplifier + 1) * 5), 3));
            }
            if (CustomEffectManager.hasEffect(damagerEntity, CustomEffectType.BLESS_OF_SANS))
            {
              CustomEffect customEffectBlessOfSans = CustomEffectManager.getEffect(damagerEntity, CustomEffectType.BLESS_OF_SANS);
              int amplifier = customEffectBlessOfSans.getAmplifier();
              damageMultiplier += (amplifier + 1) * 0.1;
            }
            if (CustomEffectManager.hasEffect(damagerEntity, CustomEffectType.PARROTS_CHEER))
            {
              damageMultiplier += 0.1d;
            }
            if (CustomEffectManager.hasEffect(damagerEntity, CustomEffectType.SERVER_RADIO_LISTENING))
            {
              CustomEffect customEffect = CustomEffectManager.getEffect(damagerEntity, CustomEffectType.SERVER_RADIO_LISTENING);
              int amplifier = customEffect.getAmplifier();
              damageMultiplier += (amplifier + 2) * 0.05;
            }
            if (CustomEffectManager.hasEffect(victim, CustomEffectType.NEWBIE_SHIELD))
            {
              int amplifier = CustomEffectManager.getEffect(victim, CustomEffectType.NEWBIE_SHIELD).getAmplifier() + 1;
              switch (amplifier)
              {
                case 1 -> damageMultiplier += 0.05;
                case 2 -> damageMultiplier += 0.15;
                default -> damageMultiplier += 0.25;
              }
            }
            if (CustomEffectManager.hasEffect(damagerEntity, CustomEffectType.HEROS_ECHO) || CustomEffectManager.hasEffect(damagerEntity, CustomEffectType.HEROS_ECHO_OTHERS))
            {
              finalDamageMultiplier *= 1.05;
            }
            if (CustomEffectManager.hasEffect(damagerEntity, CustomEffectType.WA_SANS) && victim instanceof AbstractSkeleton)
            {
              int amplifier = CustomEffectManager.getEffect(damagerEntity, CustomEffectType.NEWBIE_SHIELD).getAmplifier();
              damageMultiplier += (amplifier + 1) * 0.1;
            }
            if (CustomEffectManager.hasEffect(victim, CustomEffectType.WA_SANS) && damagerEntity instanceof AbstractSkeleton)
            {
              int amplifier = CustomEffectManager.getEffect(victim, CustomEffectType.NEWBIE_SHIELD).getAmplifier();
              finalDamageMultiplier *= 1 - 0.03 * (amplifier + 1);
            }
            if (CustomEffectManager.hasEffect(victim, CustomEffectType.COMBAT_MODE_RANGED) || CustomEffectManager.hasEffect(damagerEntity, CustomEffectType.COMBAT_MODE_MELEE))
            {
              damageMultiplier += 1d;
            }
            if (CustomEffectManager.hasEffect(damagerEntity, CustomEffectType.ENDER_SLAYER) && (victim instanceof Enderman || victim instanceof EnderDragon || victim instanceof Endermite))
            {
              int amplifier = CustomEffectManager.getEffect(damagerEntity, CustomEffectType.ENDER_SLAYER).getAmplifier();
              damageMultiplier += (amplifier + 1) * 0.1;
            }
            if (CustomEffectManager.hasEffect(damagerEntity, CustomEffectType.BOSS_SLAYER) && (victim instanceof Boss || victim.getScoreboardTags().contains("boss") || MythicMobManager.hasTag(victim, "boss")))
            {
              int amplifier = CustomEffectManager.getEffect(damagerEntity, CustomEffectType.BOSS_SLAYER).getAmplifier();
              damageMultiplier += (amplifier + 1) * 0.1;
            }
            if (CustomEffectManager.hasEffect(damagerEntity, CustomEffectType.BLESS_OF_VILLAGER))
            {
              damageMultiplier += 0.1;
            }
          }
        }
      }
      case FALL -> {
        if (!(event instanceof EntityDamageByEntityEvent damageByEntityEvent) || !(damageByEntityEvent.getDamager() instanceof EnderPearl))
        {
          if (CustomEffectManager.hasEffect(victim, CustomEffectType.FEATHER_FALLING))
          {
            CustomEffect customEffectFeatherFalling = CustomEffectManager.getEffect(victim, CustomEffectType.FEATHER_FALLING);
            double fallDistance = victim.getFallDistance();
            int amplifier = customEffectFeatherFalling.getAmplifier();
            if (fallDistance < (amplifier + 1) * 5 + 3.5d)
            {
              event.setCancelled(true);
              return;
            }
            damageMultiplier -= (amplifier + 1) * 0.08;
          }
          if (CustomEffectManager.hasEffect(victim, CustomEffectType.VAR_PODAGRA))
          {
            damageMultiplier += 0.5;
          }
        }
      }
      case PROJECTILE -> {
        if (event instanceof EntityDamageByEntityEvent damageByEntityEvent)
        {
          Entity damagerEntity = damageByEntityEvent.getDamager();
          if (damagerEntity instanceof Projectile projectile)
          {
            ProjectileSource projectileSource = projectile.getShooter();
            if (projectileSource instanceof Entity projectileDamager)
            {
              if (CustomEffectManager.hasEffect(victim, CustomEffectType.DODGE))
              {
                CustomEffect customEffect = CustomEffectManager.getEffect(victim, CustomEffectType.DODGE);
                int amplifier = customEffect.getAmplifier() + 1;
                if (Math.random() * 100 < amplifier)
                {
                  event.setCancelled(true);
                  MessageUtil.sendActionBar(victim, "공격을 회피했습니다!");
                  MessageUtil.sendActionBar(projectileDamager, "상대방이 공격을 회피했습니다!");
                  return;
                }
              }
              if (CustomEffectManager.hasEffect(projectileDamager, CustomEffectType.PARROTS_CHEER))
              {
                damageMultiplier += 0.1d;
              }
              if (CustomEffectManager.hasEffect(projectileDamager, CustomEffectType.SERVER_RADIO_LISTENING))
              {
                CustomEffect customEffect = CustomEffectManager.getEffect(projectileDamager, CustomEffectType.SERVER_RADIO_LISTENING);
                int amplifier = customEffect.getAmplifier();
                damageMultiplier += (amplifier + 2) * 0.05;
              }
              if (CustomEffectManager.hasEffect(projectileDamager, CustomEffectType.NEWBIE_SHIELD))
              {
                int amplifier = CustomEffectManager.getEffect(projectileDamager, CustomEffectType.NEWBIE_SHIELD).getAmplifier() + 1;
                switch (amplifier)
                {
                  case 1 -> damageMultiplier += 0.05;
                  case 2 -> damageMultiplier += 0.15;
                  default -> damageMultiplier += 0.25;
                }
              }
              if (CustomEffectManager.hasEffect(projectileDamager, CustomEffectType.HEROS_ECHO) || CustomEffectManager.hasEffect(projectileDamager, CustomEffectType.HEROS_ECHO_OTHERS))
              {
                finalDamageMultiplier *= 1.05;
              }
              if (CustomEffectManager.hasEffect(projectileDamager, CustomEffectType.WA_SANS) && victim instanceof AbstractSkeleton)
              {
                int amplifier = CustomEffectManager.getEffect(projectileDamager, CustomEffectType.NEWBIE_SHIELD).getAmplifier() + 1;
                damageMultiplier += (amplifier + 1) * 0.1;
              }
              if (CustomEffectManager.hasEffect(victim, CustomEffectType.WA_SANS) && projectileDamager instanceof AbstractSkeleton)
              {
                int amplifier = CustomEffectManager.getEffect(victim, CustomEffectType.NEWBIE_SHIELD).getAmplifier() + 1;
                finalDamageMultiplier *= 1 - 0.03 * amplifier;
              }
              if (CustomEffectManager.hasEffect(victim, CustomEffectType.COMBAT_MODE_MELEE) || CustomEffectManager.hasEffect(projectileDamager, CustomEffectType.COMBAT_MODE_RANGED))
              {
                damageMultiplier += 1d;
              }
              if (CustomEffectManager.hasEffect(projectileDamager, CustomEffectType.ENDER_SLAYER) && (victim instanceof Enderman || victim instanceof EnderDragon || victim instanceof Endermite))
              {
                int amplifier = CustomEffectManager.getEffect(projectileDamager, CustomEffectType.ENDER_SLAYER).getAmplifier();
                damageMultiplier += (amplifier + 1) * 0.1;
              }
              if (CustomEffectManager.hasEffect(projectileDamager, CustomEffectType.BOSS_SLAYER) && (victim instanceof Boss || victim.getScoreboardTags().contains("boss") || MythicMobManager.hasTag(victim, "boss")))
              {
                int amplifier = CustomEffectManager.getEffect(projectileDamager, CustomEffectType.BOSS_SLAYER).getAmplifier();
                damageMultiplier += (amplifier + 1) * 0.1;
              }
              if (CustomEffectManager.hasEffect(projectileDamager, CustomEffectType.BLESS_OF_VILLAGER))
              {
                damageMultiplier += 0.1;
              }
            }
            if (projectile instanceof AbstractArrow arrow)
            {
              ItemStack itemStack = Variable.projectile.get(arrow.getUniqueId());
              NBTCompoundList potionsTag = NBTAPI.getCompoundList(NBTAPI.getMainCompound(itemStack), CucumberyTag.CUSTOM_EFFECTS);
              if (potionsTag != null && !potionsTag.isEmpty())
              {
                for (NBTCompound potionTag : potionsTag)
                {
                  try
                  {
                    CustomEffectManager.addEffect(victim, new CustomEffect(
                            CustomEffectType.valueOf(potionTag.getString(CucumberyTag.CUSTOM_EFFECTS_ID).toUpperCase()),
                            potionTag.getInteger(CucumberyTag.CUSTOM_EFFECTS_DURATION),
                            potionTag.getInteger(CucumberyTag.CUSTOM_EFFECTS_AMPLIFIER),
                            DisplayType.valueOf(potionTag.getString(CucumberyTag.CUSTOM_EFFECTS_DISPLAY_TYPE).toUpperCase())
                    ));
                  }
                  catch (Exception ignored)
                  {

                  }
                }
              }
            }
          }
        }
      }
      case FLY_INTO_WALL -> {
        if (CustomEffectManager.hasEffect(victim, CustomEffectType.KINETIC_RESISTANCE))
        {
          CustomEffect customEffect = CustomEffectManager.getEffect(victim, CustomEffectType.KINETIC_RESISTANCE);
          int amplifier = customEffect.getAmplifier();
          damageMultiplier -= (amplifier + 1) * 0.1;
        }
      }
    }
    if (damageMultiplier < 0d)
    {
      damageMultiplier = 0d;
    }
    finalDamageMultiplier *= damageMultiplier;
    if (finalDamageMultiplier != 1d || damage != event.getDamage())
    {
      event.setDamage(Math.max(0d, damage * finalDamageMultiplier + fixedDamage));
    }
  }

  private void customEnchant(EntityDamageEvent event)
  {
    Entity entity = event.getEntity();
    if (entity instanceof LivingEntity living)
    {
      if (event instanceof EntityDamageByEntityEvent damageByEntityEvent)
      {
        Entity damager = damageByEntityEvent.getDamager();
        if (damager instanceof LivingEntity livingEntity)
        {
          EntityEquipment entityEquipment = livingEntity.getEquipment();
          if (entityEquipment != null)
          {
            ItemStack mainHand = entityEquipment.getItemInMainHand();
            if (mainHand.hasItemMeta())
            {
              ItemMeta itemMeta = mainHand.getItemMeta();
              if (itemMeta.hasEnchant(CustomEnchant.JUSTIFICATION))
              {
                Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                        living.setNoDamageTicks(0), 0L);
              }
            }
          }
        }
        if (damager instanceof Projectile projectile)
        {
          ProjectileSource projectileSource = projectile.getShooter();
          if (projectileSource instanceof LivingEntity livingEntity)
          {
            ItemStack weapon = Variable.attackerAndWeapon.get(livingEntity.getUniqueId());
            if (weapon != null && weapon.hasItemMeta())
            {
              ItemMeta itemMeta = weapon.getItemMeta();
              if (itemMeta.hasEnchant(CustomEnchant.JUSTIFICATION_BOW))
              {
                Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                        living.setNoDamageTicks(0), 0L);
              }
            }
          }
        }
      }
    }
  }

  private void cancelEntityDamage(EntityDamageEvent event)
  {
    Entity entity = event.getEntity();
    if (Method.configContainsLocation(entity.getLocation(), Cucumbery.config.getStringList("no-prevent-entity-explosion-worlds")))
    {
      return;
    }
    if (event.getCause() == DamageCause.ENTITY_EXPLOSION)
    {
      EntityType type = entity.getType();
      if (type == EntityType.ARMOR_STAND)
      {
        if (Cucumbery.config.getBoolean("prevent-entity-explosion.armor-stand"))
        {
          event.setCancelled(true);
        }
      }
    }
  }

  private void cancelLavaBurnItem(EntityDamageEvent event)
  {
    EntityType type = event.getEntityType();
    if (type != EntityType.DROPPED_ITEM)
    {
      return;
    }
    Item itemEntity = (Item) event.getEntity();
    ItemStack item = itemEntity.getItemStack();
    boolean affectedByPlugin = false;
    NBTList<String> extraTags = NBTAPI.getStringList(NBTAPI.getMainCompound(item), CucumberyTag.EXTRA_TAGS_KEY);
    if (extraTags != null && !extraTags.isEmpty())
    {
      boolean noFire = false;
      if (NBTAPI.arrayContainsValue(extraTags, Constant.ExtraTag.INVULNERABLE.toString()))
      {
        event.setCancelled(true);
        affectedByPlugin = true;
        itemEntity.setInvulnerable(true);
      }
      DamageCause damageCause = event.getCause();
      if ((damageCause == DamageCause.ENTITY_EXPLOSION || damageCause == DamageCause.BLOCK_EXPLOSION) && NBTAPI.arrayContainsValue(extraTags, Constant.ExtraTag.INVULNERABLE_EXPLOSION.toString()))
      {
        affectedByPlugin = true;
      }

      if (damageCause == DamageCause.ENTITY_EXPLOSION && NBTAPI.arrayContainsValue(extraTags, Constant.ExtraTag.INVULNERABLE_EXPLOSION_ENTITY.toString()))
      {
        affectedByPlugin = true;
      }

      if (damageCause == DamageCause.BLOCK_EXPLOSION && NBTAPI.arrayContainsValue(extraTags, Constant.ExtraTag.INVULNERABLE_EXPLOSION_BLOCK.toString()))
      {
        affectedByPlugin = true;
      }

      if ((damageCause == DamageCause.LAVA || damageCause == DamageCause.FIRE || damageCause == DamageCause.FIRE_TICK) && NBTAPI.arrayContainsValue(extraTags, Constant.ExtraTag.INVULNERABLE_FIRES.toString()))
      {
        noFire = true;
        affectedByPlugin = true;
      }

      if (damageCause == DamageCause.LAVA && NBTAPI.arrayContainsValue(extraTags, Constant.ExtraTag.INVULNERABLE_FIRES_LAVA.toString()))
      {
        affectedByPlugin = true;
      }
      if (damageCause == DamageCause.FIRE && NBTAPI.arrayContainsValue(extraTags, Constant.ExtraTag.INVULNERABLE_FIRES_FIRE.toString()))
      {
        noFire = true;
        affectedByPlugin = true;
      }
      if (damageCause == DamageCause.FIRE_TICK && NBTAPI.arrayContainsValue(extraTags, Constant.ExtraTag.INVULNERABLE_FIRES_FIRE_TICK.toString()))
      {
        noFire = true;
        affectedByPlugin = true;
      }
      if (damageCause == DamageCause.CONTACT && NBTAPI.arrayContainsValue(extraTags, Constant.ExtraTag.INVULNERABLE_CONTACT.toString()))
      {
        affectedByPlugin = true;
      }

//			MessageUtil.broadcastDebug(damageCause.toString() + ", " + event.getDamage() + ", " + itemEntity.getThrower());

      if (affectedByPlugin)
      {
        event.setCancelled(true);
        if (noFire)
        {
          itemEntity.setFireTicks(-20);
        }
        itemEntity.setInvulnerable(true);
        Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> itemEntity.setInvulnerable(false), 0L);
      }
    }
  }

  private void displayDamage(EntityDamageEvent event)
  {
    if (!Cucumbery.config.getBoolean("use-damage-indicator.enabled"))
    {
      return;
    }
    Entity entity = event.getEntity();
    if (!(entity instanceof Player) && !(entity instanceof Mob))
    {
      return;
    }
    Damageable damageable = (Damageable) entity;
    boolean viewSelf = Cucumbery.config.getBoolean("use-damage-indicator.view-self"), maplelized = Cucumbery.config.getBoolean("use-damage-indicator.maplelized");
    double damage = event.getFinalDamage();
    Component display;
    double health = damageable.getHealth();
    if (health - damage >= health - 0.01 || damage <= 0.0001 || health - damage >= health)
    {
      display = ComponentUtil.translate("&e&lMISS!");
    }
    else
    {
      display = maplelized ? ComponentUtil.create(NumberHangulConverter.convert2(damage, true)) : Component.text(Constant.Sosu2Floor.format(damage), Constant.THE_COLOR);
    }
    UUID uuid = entity.getUniqueId();
    long current = System.currentTimeMillis(), before = Variable.lastDamageMillis.containsKey(uuid) ? Variable.lastDamageMillis.get(uuid) : 0, diff = before == 0 ? 0 : current - before;
    if (diff >= 500)
    {
      diff = 50;
      Variable.damageIndicatorStack.remove(uuid);
    }
    Variable.lastDamageMillis.put(uuid, current);
    if (!Variable.damageIndicatorStack.containsKey(uuid))
    {
      Variable.damageIndicatorStack.put(uuid, (int) (30 + diff / 20d));
    }
    else
    {
      int stack = Variable.damageIndicatorStack.get(uuid);
      Variable.damageIndicatorStack.put(uuid, (int) (stack + 30 + diff / 20d));
    }
    double offset = Math.max(0, Variable.damageIndicatorStack.get(uuid) / 100d) - 0.5d;
//    boolean nearby = false;
//    for (Entity nearbyEntitiy : entity.getNearbyEntities(0.1, 0.3, 0.1))
//    {
//      if (nearbyEntitiy instanceof ArmorStand && nearbyEntitiy.getScoreboardTags().contains("damage_indicator"))
//      {
//        nearby = true;
//        break;
//      }
//    }
//    if (!nearby)
//    {
//      offset = 0d;
//    }
    Location location = event.getEntity().getLocation();
    BoundingBox boundingBox = entity.getBoundingBox();
    offset += (entity.getFireTicks() > 0 ? ((boundingBox.getMaxY() - boundingBox.getCenterY()) * 1.1) : 0);
    location.setX(boundingBox.getCenterX());
    location.setY(boundingBox.getMaxY() + offset);
    location.setZ(boundingBox.getCenterZ());
    Component finalDisplay = display;
    Consumer<Entity> consumer = e ->
    {
      ArmorStand armorStand = (ArmorStand) e;
      armorStand.setVisible(false);
      armorStand.setMarker(true);
      armorStand.setSmall(true);
      armorStand.customName(finalDisplay);
      armorStand.setCustomNameVisible(true);
      armorStand.addScoreboardTag("damage_indicator");
      if (!viewSelf)
      {
        armorStand.addScoreboardTag("no_cucumbery_true_invisibility");
        if (entity instanceof Player player)
        {
          player.hideEntity(Cucumbery.getPlugin(), armorStand);
        }
      }
    };
    Entity armorStand = location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND, SpawnReason.DEFAULT, consumer);
    CustomEffectManager.addEffect(armorStand, CustomEffectType.DAMAGE_INDICATOR);
  }
}

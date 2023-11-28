package com.jho5245.cucumbery.listeners.entity.damage;

import com.google.common.base.Functions;
import com.google.common.collect.ImmutableMap;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.CombatInfo;
import com.jho5245.cucumbery.custom.DamageManager;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect.DisplayType;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.children.group.DoubleCustomEffect;
import com.jho5245.cucumbery.custom.customeffect.children.group.DoubleCustomEffectImple;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeRune;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectAbstractApplyEvent.ApplyReason;
import com.jho5245.cucumbery.util.itemlore.ItemLore2Attribute;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.*;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.NBTList;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Consumer;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumMap;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("deprecation")
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
    // 관전 모드 상태인 플레이어는 피해를 입지 않음
    if (entity instanceof Player player && UserData.SPECTATOR_MODE.getBoolean(player))
    {
      event.setCancelled(true);
      return;
    }
    // 탈수 TNT로 인한 피해는 무시함
    if (event instanceof EntityDamageByEntityEvent damageByEntityEvent && damageByEntityEvent.getDamager() instanceof TNTPrimed tntPrimed && tntPrimed.getScoreboardTags().contains("custom_material_tnt_drain"))
    {
      event.setCancelled(true);
      return;
    }
    DamageCause damageCause = event.getCause();
    // 승천 효과 적용 중에는 낙하 피해 무시(엔더 진주 제외)
    if (CustomEffectManager.hasEffect(entity, CustomEffectType.ASCENSION) && damageCause == DamageCause.FALL && !(event instanceof EntityDamageByEntityEvent))
    {
      event.setCancelled(true);
      return;
    }
    // 피해 발산 효과
    {
      if (entity instanceof Player player && Cucumbery.config.getBoolean("use-damage-spread-feature"))
      {
        int damageSpreadLevel = 0;
        EntityEquipment equipment = player.getEquipment();
        ItemStack helmet = equipment.getHelmet(), chestplate = equipment.getChestplate(), leggings = equipment.getLeggings(), boots = equipment.getBoots();
        if (helmet != null)
        {
          damageSpreadLevel += ItemLore2Attribute.getDamageSpread(helmet);
        }
        if (chestplate != null)
        {
          damageSpreadLevel += ItemLore2Attribute.getDamageSpread(chestplate);
        }
        if (leggings != null)
        {
          damageSpreadLevel += ItemLore2Attribute.getDamageSpread(leggings);
        }
        if (boots != null)
        {
          damageSpreadLevel += ItemLore2Attribute.getDamageSpread(boots);
        }
        if (damageSpreadLevel > 0)
        {
          double damage = event.getDamage();
          double accumulatedDamage = 0.01 * Math.sqrt(10 * damageSpreadLevel) * damage;
          CustomEffect customEffect = CustomEffectManager.getEffectNullable(player, CustomEffectType.DAMAGE_SPREAD);
          if (customEffect instanceof DoubleCustomEffect doubleCustomEffect)
          {
            accumulatedDamage += doubleCustomEffect.getDouble();
          }
          double maxDamage = damageSpreadLevel * 0.4;
          if (accumulatedDamage > maxDamage)
          {
            accumulatedDamage = maxDamage;
          }
          DoubleCustomEffectImple doubleCustomEffect = new DoubleCustomEffectImple(CustomEffectType.DAMAGE_SPREAD, accumulatedDamage);
          CustomEffectManager.addEffect(player, doubleCustomEffect);
        }
      }
      if (event instanceof EntityDamageByEntityEvent damageByEntityEvent)
      {
        Entity damager = damageByEntityEvent.getDamager();
        UUID damagerUUID = damager.getUniqueId();
        if (damager instanceof Player player && CustomEffectManager.hasEffect(player, CustomEffectType.DAMAGE_SPREAD))
        {
          if (CustomEffectManager.getEffect(player, CustomEffectType.DAMAGE_SPREAD) instanceof DoubleCustomEffect doubleCustomEffect)
          {
            event.setDamage(event.getDamage() + doubleCustomEffect.getDouble());
          }
          CustomEffectManager.removeEffect(player, CustomEffectType.DAMAGE_SPREAD);
        }
        if (damager instanceof Projectile && Variable.DAMAGE_SPREAD_MAP.containsKey(damagerUUID))
        {
          event.setDamage(event.getDamage() + Variable.DAMAGE_SPREAD_MAP.remove(damagerUUID));
        }
      }
    }

    // config에서 설정된 갑옷 거치대 폭발 피해 무시
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
      CombatInfo combatInfo = DamageManager.getCombatInfo(event);
      if (combatInfo != null)
      {
        double damage = combatInfo.damage(), bonusDamage = combatInfo.bonusDamage(), damageMultiplier = combatInfo.damageMultiplier(), finalDamageMultiplier = combatInfo.finalDamageMultiplier();
        damage = damage * damageMultiplier * finalDamageMultiplier + bonusDamage;
        if (!Double.isNaN(damage) && !Double.isInfinite(damage) && damage <= Math.pow(2, 127))
        {
          event.setDamage(damage);
        }
      }
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
    DamageCause damageCause = event.getCause();
    Entity victim = event.getEntity();
    if (damageCause == DamageCause.FALL && !(event instanceof EntityDamageByEntityEvent) && CustomEffectManager.hasEffect(victim, CustomEffectTypeRune.RUNE_EARTHQUAKE))
    {
      victim.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, victim.getLocation(), 10, 3, 0, 3, 1);
      List<Entity> entities = victim.getNearbyEntities(5, 2, 5);
      int count = 0;
      for (Entity entity : entities)
      {
        if (count > 15)
        {
          break;
        }
        if (entity.isOnGround() && entity instanceof LivingEntity livingEntity && !(livingEntity instanceof Player) && !(livingEntity instanceof Tameable tameable && tameable.isTamed()))
        {
          if (entity instanceof Boss || entity.getScoreboardTags().contains("boss") || MythicMobManager.hasTag(entity, "boss"))
          {
            livingEntity.damage(0);
            continue;
          }
          count++;
          AttributeInstance attributeInstance = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
          double damage = attributeInstance != null ? attributeInstance.getValue() : livingEntity.getHealth();
          CombatInfo combatInfo = DamageManager.getCombatInfo(new EntityDamageByEntityEvent(victim, livingEntity, DamageCause.CUSTOM,
                  new EnumMap<>(ImmutableMap.of(DamageModifier.BASE, damage)),
                  new EnumMap<>(ImmutableMap.of(DamageModifier.BASE, Functions.constant(0d))), false));
          if (combatInfo != null)
          {
            damage -= combatInfo.bonusDamage();
            damage /= combatInfo.damageMultiplier();
            damage /= combatInfo.finalDamageMultiplier();
          }
          livingEntity.damage(damage, victim);
          if (livingEntity.getHealth() > 0)
          {
            livingEntity.setHealth(0);
          }
        }
      }
      event.setCancelled(true);
      return;
    }
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
    if (victim instanceof LivingEntity livingEntity && CustomEffectManager.hasEffect(victim, CustomEffectType.REMOVE_NO_DAMAGE_TICKS))
    {
      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> livingEntity.setNoDamageTicks(0), 0L);
    }
    switch (damageCause)
    {
      case HOT_FLOOR ->
      {
        if (CustomEffectManager.hasEffect(victim, CustomEffectType.FROST_WALKER))
        {
          event.setCancelled(true);
        }
      }
      case ENTITY_ATTACK, ENTITY_SWEEP_ATTACK, CUSTOM ->
      {
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
              }
            }
          }
        }
      }
      case PROJECTILE ->
      {
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
            }
            if (projectile instanceof AbstractArrow arrow)
            {
              ItemStack itemStack = Variable.projectile.get(arrow.getUniqueId());
              NBTCompoundList potionsTag = NBTAPI.getCompoundList(NBTAPI.getMainCompound(itemStack), CucumberyTag.CUSTOM_EFFECTS);
              if (potionsTag != null && !potionsTag.isEmpty())
              {
                for (ReadWriteNBT potionTag : potionsTag)
                {
                  try
                  {
                    CustomEffectManager.addEffect(victim, new CustomEffect(
                            CustomEffectType.valueOf(potionTag.getString(CucumberyTag.CUSTOM_EFFECTS_ID)),
                            potionTag.getInteger(CucumberyTag.CUSTOM_EFFECTS_DURATION),
                            potionTag.getInteger(CucumberyTag.CUSTOM_EFFECTS_AMPLIFIER),
                            DisplayType.valueOf(potionTag.getString(CucumberyTag.CUSTOM_EFFECTS_DISPLAY_TYPE).toUpperCase())
                    ), ApplyReason.TIPPED_ARROW);
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
              if (CustomEnchant.isEnabled() && itemMeta.hasEnchant(CustomEnchant.JUSTIFICATION))
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
              if (CustomEnchant.isEnabled() && itemMeta.hasEnchant(CustomEnchant.JUSTIFICATION_BOW))
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
    CustomMaterial customMaterial = CustomMaterial.itemStackOf(item);
    if (customMaterial == CustomMaterial.DOEHAERIM_BABO || customMaterial == CustomMaterial.BAMIL_PABO)
    {
      event.setCancelled(true);
      affectedByPlugin = true;
      itemEntity.setInvulnerable(true);
    }
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

  private final BigDecimal compare = new BigDecimal("33000000");

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
    TextColor damageColor = null;
    boolean isCrit = event instanceof EntityDamageByEntityEvent damageByEntityEvent && damageByEntityEvent.isCritical();
    switch (event.getCause())
    {
      case CONTACT -> damageColor = NamedTextColor.GREEN;
      case ENTITY_ATTACK ->
      {
        if (isCrit)
        {
          damageColor = TextColor.color(255, 150, 150);
        }
        else
        {
          damageColor = TextColor.color(200, 200, 200);
        }
      }
      case ENTITY_SWEEP_ATTACK ->
      {
        if (isCrit)
        {
          damageColor = TextColor.color(150, 200, 150);
        }
        else
        {
          damageColor = TextColor.color(200, 255, 200);
        }
      }
      case PROJECTILE ->
      {
        if (isCrit)
        {
          damageColor = TextColor.color(150, 150, 255);
        }
        else
        {
          damageColor = TextColor.color(200, 200, 255);
        }
      }
      case SUFFOCATION -> damageColor = TextColor.color(255, 230, 100);
      case FALL -> damageColor = TextColor.color(100, 100, 100);
      case FIRE, FIRE_TICK, HOT_FLOOR, DRYOUT -> damageColor = TextColor.color(255, 100, 100);
      case MELTING -> damageColor = TextColor.color(255, 255, 0);
      case LAVA -> damageColor = TextColor.color(255, 0, 0);
      case DROWNING -> damageColor = TextColor.color(0, 180, 220);
      case BLOCK_EXPLOSION, ENTITY_EXPLOSION -> damageColor = TextColor.color(255, 50, 100);
      case VOID -> damageColor = TextColor.color(30, 30, 30);
      case LIGHTNING -> damageColor = TextColor.color(200, 200, 30);
      case SUICIDE -> damageColor = TextColor.color(0, 0, 0);
      case STARVATION -> damageColor = TextColor.color(200, 100, 30);
      case POISON -> damageColor = TextColor.color(0, 200, 30);
      case MAGIC, SONIC_BOOM -> damageColor = TextColor.color(0, 255, 255);
      case WITHER -> damageColor = TextColor.color(100, 50, 50);
      case FALLING_BLOCK -> damageColor = TextColor.color(255, 200, 100);
      case THORNS -> damageColor = TextColor.color(100, 200, 30);
      case DRAGON_BREATH -> damageColor = TextColor.color(200, 50, 200);
      case CUSTOM -> damageColor = Constant.THE_COLOR;
      case FLY_INTO_WALL, CRAMMING -> damageColor = TextColor.color(200, 200, 200);
      case FREEZE -> damageColor = TextColor.color(100, 200, 255);
    }
    Damageable damageable = (Damageable) entity;
    boolean viewSelf = Cucumbery.config.getBoolean("use-damage-indicator.view-self"), maplelized = Cucumbery.config.getBoolean("use-damage-indicator.maplelized");
    double damage = event.getFinalDamage();
    Component display;
    double health = damageable.getHealth();
    BigDecimal damageBig = new BigDecimal(damage), healthBig = new BigDecimal(health), ratioBig = healthBig.divide(damageBig.max(BigDecimal.ONE), RoundingMode.FLOOR);
    boolean isMiss = false;
    if (health - damage >= health - 0.01 || damage <= 0.0001 || health - damage >= health || ratioBig.compareTo(compare) > 0)
    {
      isMiss = true;
      String type = "&e&lMISS!";
      if (entity instanceof Player player && player.isBlocking())
      {
        type = "&b&lGUARD!";
      }
      display = ComponentUtil.translate(type);
    }
    else
    {
      display = maplelized ? ComponentUtil.create(NumberHangulConverter.convert2(damage, true, damageColor)) : Component.text(Constant.Sosu2.format(damage), damageColor);
    }
    if (!isMiss && isCrit)
    {
      display = MessageUtil.boldify(Component.text("✧").append(display.color(null))).color(damageColor);
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
    Location location = event.getEntity().getLocation();
    BoundingBox boundingBox = entity.getBoundingBox();
    float sizeModifier = (float) Math.max(1f, (boundingBox.getMaxY() - boundingBox.getMinY()) / 1.5f);
    offset += (entity.getFireTicks() > 0 ? ((boundingBox.getMaxY() - boundingBox.getCenterY())) : 0);
    location.setX(boundingBox.getCenterX());
    location.setY(boundingBox.getMaxY() + offset * sizeModifier);
    location.setZ(boundingBox.getCenterZ());
    Component finalDisplay = display;
    if (Cucumbery.using_ProtocolLib)
    {
      DamageIndicatorProtocolLib.displayDamage(viewSelf, entity, location, finalDisplay, sizeModifier);
    }
    else
    {
      Consumer<Entity> consumer = e ->
      {
        TextDisplay textDisplay = (TextDisplay) e;
        textDisplay.text(finalDisplay);
        textDisplay.setBillboard(Billboard.CENTER);
        textDisplay.setSeeThrough(false);
        textDisplay.setViewRange(30f);
        textDisplay.setShadowed(true);
        textDisplay.setShadowRadius(0);
        textDisplay.setShadowStrength(0);
        textDisplay.setBrightness(brightness);
        textDisplay.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
        textDisplay.setTextOpacity((byte) -1);
        Transformation transformation = textDisplay.getTransformation();
        Transformation newTransformation = new Transformation(new Vector3f(0f, 0.2f * sizeModifier, 0f), transformation.getLeftRotation(), new Vector3f(1.2f * sizeModifier, 1.2f * sizeModifier, 1.2f * sizeModifier), transformation.getRightRotation());
        textDisplay.setTransformation(newTransformation);
        textDisplay.addScoreboardTag("damage_indicator");
        if (!viewSelf)
        {
          textDisplay.addScoreboardTag("no_cucumbery_true_invisibility");
          if (entity instanceof Player player)
          {
            player.hideEntity(Cucumbery.getPlugin(), textDisplay);
          }
        }
      };

      TextDisplay textDisplay = (TextDisplay) location.getWorld().spawnEntity(location, EntityType.TEXT_DISPLAY, SpawnReason.DEFAULT, consumer);
      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        textDisplay.setInterpolationDelay(-1);
        textDisplay.setInterpolationDuration(10);
        Transformation transformation = textDisplay.getTransformation();
        Transformation newTransformation = new Transformation(new Vector3f(0f, 0.4f * sizeModifier, 0f), transformation.getLeftRotation(), transformation.getScale(), transformation.getRightRotation());
        textDisplay.setTransformation(newTransformation);
      }, 2L);
      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        textDisplay.setInterpolationDelay(-1);
        textDisplay.setInterpolationDuration(5);
        textDisplay.setTextOpacity((byte) -127);
        Transformation transformation = textDisplay.getTransformation();
        Transformation newTransformation = new Transformation(new Vector3f(0f, 0.5f * sizeModifier, 0f), transformation.getLeftRotation(), transformation.getScale(), transformation.getRightRotation());
        textDisplay.setTransformation(newTransformation);
      }, 12L);
      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        textDisplay.setTextOpacity((byte) 127);
      }, 15L);
      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        textDisplay.setInterpolationDelay(-1);
        textDisplay.setInterpolationDuration(5);
        textDisplay.setTextOpacity((byte) 5);
        Transformation transformation = textDisplay.getTransformation();
        Transformation newTransformation = new Transformation(new Vector3f(0f, 0.6f * sizeModifier, 0f), transformation.getLeftRotation(), transformation.getScale(), transformation.getRightRotation());
        textDisplay.setTransformation(newTransformation);
      }, 16L);
      CustomEffectManager.addEffect(textDisplay, CustomEffectType.DAMAGE_INDICATOR);
    }
  }

  private final Brightness brightness = new Brightness(0, 15);
}

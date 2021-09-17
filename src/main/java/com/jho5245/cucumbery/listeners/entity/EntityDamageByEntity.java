package com.jho5245.cucumbery.listeners.entity;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.SoundPlay;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Variable;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class EntityDamageByEntity implements Listener
{
  private static final Material[] RANGED_WEAPONS = new Material[]{Material.BOW, Material.CROSSBOW, Material.TRIDENT};

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Entity victim = event.getEntity();
    Entity damager = event.getDamager();
    UUID victimUUID = victim.getUniqueId(), damagerUUID = damager.getUniqueId();
    if (!(damager instanceof LivingEntity) && !(damager instanceof Projectile) && !(damager instanceof AreaEffectCloud))
    {
      DamageCause cause = event.getCause();
      switch (cause)
      {
        case CUSTOM, ENTITY_ATTACK, ENTITY_SWEEP_ATTACK -> Variable.victimAndDamager.put(victimUUID, damager);
      }
    }
    if (damager instanceof LivingEntity livingEntity)
    {
      Variable.victimAndDamager.put(victimUUID, damager);
      EntityEquipment entityEquipment = livingEntity.getEquipment();
      if (entityEquipment != null)
      {
        ItemStack weapon = entityEquipment.getItemInMainHand();
        if (ItemStackUtil.itemExists(weapon))
        {
          Variable.attackerAndWeapon.put(damagerUUID, weapon.clone());
        }
        else
        {
          Variable.attackerAndWeapon.remove(damagerUUID);
        }
      }
      else
      {
        Variable.attackerAndWeapon.remove(damagerUUID);
      }
    }
    if (damager instanceof Projectile projectile && event.getCause() != EntityDamageEvent.DamageCause.FALL)
    {
      ProjectileSource projectileSource = projectile.getShooter();
      if (projectileSource instanceof LivingEntity livingEntity)
      {
        Variable.victimAndDamager.put(victimUUID, livingEntity);
      }
      else if (projectileSource instanceof BlockProjectileSource blockProjectileSource)
      {
        if (Variable.entityAndSourceLocation.containsKey(projectile.getUniqueId()))
        {
          Variable.victimAndBlockDamager.put(victimUUID, Variable.blockAttackerAndBlock
                  .get(Variable.entityAndSourceLocation.get(projectile.getUniqueId())));
        }
        else
        {
          Variable.victimAndBlockDamager.put(victimUUID, ItemStackUtil.getItemStackFromBlock(blockProjectileSource.getBlock()));
        }
      }
      else
      {
        if (Variable.entityAndSourceLocation.containsKey(projectile.getUniqueId()))
        {
          Variable.victimAndBlockDamager.put(victimUUID, Variable.blockAttackerAndBlock
                  .get(Variable.entityAndSourceLocation.get(projectile.getUniqueId())));
        }
        else
        {
          Variable.victimAndDamager.put(victimUUID, projectile);
        }
      }
    }
    if (damager instanceof AreaEffectCloud areaEffectCloud)
    {
      ProjectileSource projectileSource = areaEffectCloud.getSource();
      if (projectileSource == null)
      {
        if (Variable.entityAndSourceLocation.containsKey(areaEffectCloud.getUniqueId()))
        {
          Variable.victimAndBlockDamager.put(victimUUID, Variable.blockAttackerAndBlock
                  .get(Variable.entityAndSourceLocation.get(areaEffectCloud.getUniqueId())));
        }
        else
        {
          Variable.victimAndDamager.put(victimUUID, areaEffectCloud);
        }
      }
      else if (projectileSource instanceof LivingEntity livingEntity)
      {
        Variable.victimAndDamager.put(victimUUID, livingEntity);
      }
      else if (projectileSource instanceof BlockProjectileSource blockProjectileSource)
      {
        Variable.victimAndBlockDamager.put(victimUUID, ItemStackUtil.getItemStackFromBlock(blockProjectileSource.getBlock()));
      }
    }
    if (damager instanceof Player player)
    {
      UUID uuid = player.getUniqueId();
      if (!Cucumbery.config.getBoolean("grant-default-permission-to-players") && !Permission.EVENT_HURT_ENTITY.has(player))
      {
        event.setCancelled(true);
        if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerHurtEntityAlertCooldown.contains(uuid))
        {
          SoundPlay.playSound(player, Constant.ERROR_SOUND);
          MessageUtil.sendTitle(player, "&c행동 불가!", "&r개체에게 피해를 입힐 권한이 없습니다.", 5, 80, 15);
          Variable.playerHurtEntityAlertCooldown.add(uuid);
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerHurtEntityAlertCooldown.remove(uuid), 100L);
        }
        return;
      }
      ItemStack mainHand = player.getInventory().getItemInMainHand();
      if (ItemStackUtil.itemExists(mainHand) && event.getEntity() instanceof LivingEntity && event.getEntity().getType() != EntityType.ARMOR_STAND)
      {
        if (NBTAPI.isRestricted(player, mainHand, Constant.RestrictionType.NO_ATTACK))
        {
          event.setCancelled(true);
          if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerHurtEntityAlertCooldown.contains(uuid))
          {
            SoundPlay.playSound(player, Constant.ERROR_SOUND);
            MessageUtil.sendTitle(player, "&c공격 불가!", "&r사용할 수 없는 아이템입니다.", 5, 80, 15);
            Variable.playerHurtEntityAlertCooldown.add(uuid);
            Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerHurtEntityAlertCooldown.remove(uuid), 100L);
          }
          return;
        }
      }
    }
    if (damager instanceof Projectile)
    {
      if (((Projectile) damager).getShooter() instanceof Player player)
      {
        UUID uuid = Objects.requireNonNull(player).getUniqueId();
        if (!Cucumbery.config.getBoolean("grant-default-permission-to-players") && !Permission.EVENT_HURT_ENTITY.has(player))
        {
          event.setCancelled(true);
          if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerHurtEntityAlertCooldown.contains(uuid))
          {
            SoundPlay.playSound(player, Constant.ERROR_SOUND);
            MessageUtil.sendTitle(player, "&c행동 불가!", "&r개체에게 피해를 입힐 권한이 없습니다.", 5, 80, 15);
            Variable.playerHurtEntityAlertCooldown.add(uuid);
            Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerHurtEntityAlertCooldown.remove(uuid), 100L);
          }
          return;
        }
      }
    }
    Player attcker = null;
    if (damager instanceof Player)
    {
      attcker = (Player) damager;
    }
    else if (damager instanceof Projectile && (((Projectile) damager).getShooter() instanceof Player))
    {
      attcker = (Player) ((Projectile) damager).getShooter();
    }
    if (attcker != null)
    {
      boolean isMelee = damager instanceof Player;
      boolean isPvP = event.getEntity() instanceof Player;
      boolean isSneaking = attcker.isSneaking();

      this.attackCommand(event, attcker, isMelee);
      this.attackPlayerCommand(event, attcker, isMelee, isPvP);
      this.attackEntityCommand(event, attcker, isMelee, isPvP);

      this.attackMeleeCommand(event, attcker, isMelee);
      this.attackPlayerMeleeCommand(event, attcker, isMelee, isPvP);
      this.attackEntityMeleeCommand(event, attcker, isMelee, isPvP);

      this.attackRangedCommand(event, attcker, isMelee);
      this.attackPlayerRangedCommand(event, attcker, isMelee, isPvP);
      this.attackEntityRangedCommand(event, attcker, isMelee, isPvP);

      this.sneakAttackCommand(event, attcker, isMelee, isSneaking);
      this.sneakAttackPlayerCommand(event, attcker, isMelee, isPvP, isSneaking);
      this.sneakAttackEntityCommand(event, attcker, isMelee, isPvP, isSneaking);

      this.sneakAttackMeleeCommand(event, attcker, isMelee, isSneaking);
      this.sneakAttackPlayerMeleeCommand(event, attcker, isMelee, isPvP, isSneaking);
      this.sneakAttackEntityMeleeCommand(event, attcker, isMelee, isPvP, isSneaking);

      this.sneakAttackRangedCommand(event, attcker, isMelee, isSneaking);
      this.sneakAttackPlayerRangedCommand(event, attcker, isMelee, isPvP, isSneaking);
      this.sneakAttackEntityRangedCommand(event, attcker, isMelee, isPvP, isSneaking);
    }
//    this.rpg(event);
    this.damageActionbar(event);
    this.cancelFireworkDamage(event);
  }

  private void attackCommand(EntityDamageByEntityEvent event, @NotNull Player attacker, boolean isMelee)
  {
    ItemStack attackerWeapon = isMelee ? attacker.getInventory().getItemInMainHand() : ItemStackUtil.getPlayerUsingItem(attacker, RANGED_WEAPONS);
    NBTCompound itemTag = NBTAPI.getMainCompound(attackerWeapon);
    NBTCompound usageTag = NBTAPI.getCompound(itemTag, CucumberyTag.USAGE_KEY);
    NBTCompound usageAttackTag = NBTAPI.getCompound(usageTag, CucumberyTag.USAGE_COMMANDS_ATTACK_KEY);
    NBTList<String> commandsTag = NBTAPI.getStringList(usageAttackTag, CucumberyTag.USAGE_COMMANDS_KEY);
    String permission = NBTAPI.getString(usageAttackTag, CucumberyTag.PERMISSION_KEY);
    if (permission != null && !attacker.hasPermission(permission))
    {
      return;
    }
    if (commandsTag != null)
    {
      try
      {
        NBTCompound cooldownTag = NBTAPI.getCompound(usageAttackTag, CucumberyTag.COOLDOWN_KEY);
        long cooldownTime = Objects.requireNonNull(cooldownTag).getLong(CucumberyTag.TIME_KEY);
        String cooldownTagTag = cooldownTag.getString(CucumberyTag.TAG_KEY);
        UUID uuid = attacker.getUniqueId();
        YamlConfiguration configPlayerCooldown = Variable.cooldownsItemUsage.get(uuid);
        long nextAvailable = configPlayerCooldown == null ? 0 : configPlayerCooldown.getLong(cooldownTagTag);
        long currentTime = System.currentTimeMillis();
        if (currentTime < nextAvailable)
        {
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
        // DO NOTHING
      }
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        for (String command : commandsTag)
        {
          Method.performCommand(attacker, command, true, true, event);
        }
      }, 0L);
    }
  }

  private void attackPlayerCommand(EntityDamageByEntityEvent event, @NotNull Player attacker, boolean isMelee, boolean isPvP)
  {
    if (!isPvP)
    {
      return;
    }
    ItemStack attackerWeapon = isMelee ? attacker.getInventory().getItemInMainHand() : ItemStackUtil.getPlayerUsingItem(attacker, RANGED_WEAPONS);
    NBTCompound itemTag = NBTAPI.getMainCompound(attackerWeapon);
    NBTCompound usageTag = NBTAPI.getCompound(itemTag, CucumberyTag.USAGE_KEY);
    NBTCompound usageAttackTag = NBTAPI.getCompound(usageTag, CucumberyTag.USAGE_COMMANDS_ATTACK_PLAYER_KEY);
    NBTList<String> commandsTag = NBTAPI.getStringList(usageAttackTag, CucumberyTag.USAGE_COMMANDS_KEY);
    String permission = NBTAPI.getString(usageAttackTag, CucumberyTag.PERMISSION_KEY);
    if (permission != null && !attacker.hasPermission(permission))
    {
      return;
    }
    if (commandsTag != null)
    {
      try
      {
        NBTCompound cooldownTag = NBTAPI.getCompound(usageAttackTag, CucumberyTag.COOLDOWN_KEY);
        long cooldownTime = Objects.requireNonNull(cooldownTag).getLong(CucumberyTag.TIME_KEY);
        String cooldownTagTag = cooldownTag.getString(CucumberyTag.TAG_KEY);
        UUID uuid = attacker.getUniqueId();
        YamlConfiguration configPlayerCooldown = Variable.cooldownsItemUsage.get(uuid);
        long nextAvailable = configPlayerCooldown == null ? 0 : configPlayerCooldown.getLong(cooldownTagTag);
        long currentTime = System.currentTimeMillis();
        if (currentTime < nextAvailable)
        {
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
        // DO NOTHING
      }
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        for (String command : commandsTag)
        {
          Method.performCommand(attacker, command, true, true, event);
        }
      }, 0L);
    }
  }

  private void attackEntityCommand(EntityDamageByEntityEvent event, @NotNull Player attacker, boolean isMelee, boolean isPvP)
  {
    if (isPvP)
    {
      return;
    }
    ItemStack attackerWeapon = isMelee ? attacker.getInventory().getItemInMainHand() : ItemStackUtil.getPlayerUsingItem(attacker, RANGED_WEAPONS);
    NBTCompound itemTag = NBTAPI.getMainCompound(attackerWeapon);
    NBTCompound usageTag = NBTAPI.getCompound(itemTag, CucumberyTag.USAGE_KEY);
    NBTCompound usageAttackTag = NBTAPI.getCompound(usageTag, CucumberyTag.USAGE_COMMANDS_ATTACK_ENTITY_KEY);
    NBTList<String> commandsTag = NBTAPI.getStringList(usageAttackTag, CucumberyTag.USAGE_COMMANDS_KEY);
    String permission = NBTAPI.getString(usageAttackTag, CucumberyTag.PERMISSION_KEY);
    if (permission != null && !attacker.hasPermission(permission))
    {
      return;
    }
    if (commandsTag != null)
    {
      try
      {
        NBTCompound cooldownTag = NBTAPI.getCompound(usageAttackTag, CucumberyTag.COOLDOWN_KEY);
        long cooldownTime = Objects.requireNonNull(cooldownTag).getLong(CucumberyTag.TIME_KEY);
        String cooldownTagTag = cooldownTag.getString(CucumberyTag.TAG_KEY);
        UUID uuid = attacker.getUniqueId();
        YamlConfiguration configPlayerCooldown = Variable.cooldownsItemUsage.get(uuid);
        long nextAvailable = configPlayerCooldown == null ? 0 : configPlayerCooldown.getLong(cooldownTagTag);
        long currentTime = System.currentTimeMillis();
        if (currentTime < nextAvailable)
        {
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
        // DO NOTHING
      }
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        for (String command : commandsTag)
        {
          Method.performCommand(attacker, command, true, true, event);
        }
      }, 0L);
    }
  }

  private void attackMeleeCommand(EntityDamageByEntityEvent event, @NotNull Player attacker, boolean isMelee)
  {
    if (!isMelee)
    {
      return;
    }
    ItemStack attackerWeapon = attacker.getInventory().getItemInMainHand();
    NBTCompound itemTag = NBTAPI.getMainCompound(attackerWeapon);
    NBTCompound usageTag = NBTAPI.getCompound(itemTag, CucumberyTag.USAGE_KEY);
    NBTCompound usageAttackTag = NBTAPI.getCompound(usageTag, CucumberyTag.USAGE_COMMANDS_ATTACK_MELEE_KEY);
    NBTList<String> commandsTag = NBTAPI.getStringList(usageAttackTag, CucumberyTag.USAGE_COMMANDS_KEY);
    String permission = NBTAPI.getString(usageAttackTag, CucumberyTag.PERMISSION_KEY);
    if (permission != null && !attacker.hasPermission(permission))
    {
      return;
    }
    if (commandsTag != null)
    {
      try
      {
        NBTCompound cooldownTag = NBTAPI.getCompound(usageAttackTag, CucumberyTag.COOLDOWN_KEY);
        long cooldownTime = Objects.requireNonNull(cooldownTag).getLong(CucumberyTag.TIME_KEY);
        String cooldownTagTag = cooldownTag.getString(CucumberyTag.TAG_KEY);
        UUID uuid = attacker.getUniqueId();
        YamlConfiguration configPlayerCooldown = Variable.cooldownsItemUsage.get(uuid);
        long nextAvailable = configPlayerCooldown == null ? 0 : configPlayerCooldown.getLong(cooldownTagTag);
        long currentTime = System.currentTimeMillis();
        if (currentTime < nextAvailable)
        {
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
        // DO NOTHING
      }
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        for (String command : commandsTag)
        {
          Method.performCommand(attacker, command, true, true, event);
        }
      }, 0L);
    }
  }

  private void attackPlayerMeleeCommand(EntityDamageByEntityEvent event, @NotNull Player attacker, boolean isMelee, boolean isPvP)
  {
    if (!isMelee || !isPvP)
    {
      return;
    }
    ItemStack attackerWeapon = attacker.getInventory().getItemInMainHand();
    NBTCompound itemTag = NBTAPI.getMainCompound(attackerWeapon);
    NBTCompound usageTag = NBTAPI.getCompound(itemTag, CucumberyTag.USAGE_KEY);
    NBTCompound usageAttackTag = NBTAPI.getCompound(usageTag, CucumberyTag.USAGE_COMMANDS_ATTACK_PLAYER_MELEE_KEY);
    NBTList<String> commandsTag = NBTAPI.getStringList(usageAttackTag, CucumberyTag.USAGE_COMMANDS_KEY);
    String permission = NBTAPI.getString(usageAttackTag, CucumberyTag.PERMISSION_KEY);
    if (permission != null && !attacker.hasPermission(permission))
    {
      return;
    }
    if (commandsTag != null)
    {
      try
      {
        NBTCompound cooldownTag = NBTAPI.getCompound(usageAttackTag, CucumberyTag.COOLDOWN_KEY);
        long cooldownTime = Objects.requireNonNull(cooldownTag).getLong(CucumberyTag.TIME_KEY);
        String cooldownTagTag = cooldownTag.getString(CucumberyTag.TAG_KEY);
        UUID uuid = attacker.getUniqueId();
        YamlConfiguration configPlayerCooldown = Variable.cooldownsItemUsage.get(uuid);
        long nextAvailable = configPlayerCooldown == null ? 0 : configPlayerCooldown.getLong(cooldownTagTag);
        long currentTime = System.currentTimeMillis();
        if (currentTime < nextAvailable)
        {
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
        // DO NOTHING
      }
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        for (String command : commandsTag)
        {
          Method.performCommand(attacker, command, true, true, event);
        }
      }, 0L);
    }
  }

  private void attackEntityMeleeCommand(EntityDamageByEntityEvent event, @NotNull Player attacker, boolean isMelee, boolean isPvP)
  {
    if (!isMelee || isPvP)
    {
      return;
    }
    ItemStack attackerWeapon = attacker.getInventory().getItemInMainHand();
    NBTCompound itemTag = NBTAPI.getMainCompound(attackerWeapon);
    NBTCompound usageTag = NBTAPI.getCompound(itemTag, CucumberyTag.USAGE_KEY);
    NBTCompound usageAttackTag = NBTAPI.getCompound(usageTag, CucumberyTag.USAGE_COMMANDS_ATTACK_ENTITY_MELEE_KEY);
    NBTList<String> commandsTag = NBTAPI.getStringList(usageAttackTag, CucumberyTag.USAGE_COMMANDS_KEY);
    String permission = NBTAPI.getString(usageAttackTag, CucumberyTag.PERMISSION_KEY);
    if (permission != null && !attacker.hasPermission(permission))
    {
      return;
    }
    if (commandsTag != null)
    {
      try
      {
        NBTCompound cooldownTag = NBTAPI.getCompound(usageAttackTag, CucumberyTag.COOLDOWN_KEY);
        long cooldownTime = Objects.requireNonNull(cooldownTag).getLong(CucumberyTag.TIME_KEY);
        String cooldownTagTag = cooldownTag.getString(CucumberyTag.TAG_KEY);
        UUID uuid = attacker.getUniqueId();
        YamlConfiguration configPlayerCooldown = Variable.cooldownsItemUsage.get(uuid);
        long nextAvailable = configPlayerCooldown == null ? 0 : configPlayerCooldown.getLong(cooldownTagTag);
        long currentTime = System.currentTimeMillis();
        if (currentTime < nextAvailable)
        {
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
        // DO NOTHING
      }
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        for (String command : commandsTag)
        {
          Method.performCommand(attacker, command, true, true, event);
        }
      }, 0L);
    }
  }

  private void attackRangedCommand(EntityDamageByEntityEvent event, @NotNull Player attacker, boolean isMelee)
  {
    if (isMelee)
    {
      return;
    }
    ItemStack attackerWeapon = ItemStackUtil.getPlayerUsingItem(attacker, RANGED_WEAPONS);
    NBTCompound itemTag = NBTAPI.getMainCompound(attackerWeapon);
    NBTCompound usageTag = NBTAPI.getCompound(itemTag, CucumberyTag.USAGE_KEY);
    NBTCompound usageAttackTag = NBTAPI.getCompound(usageTag, CucumberyTag.USAGE_COMMANDS_ATTACK_RANGED_KEY);
    NBTList<String> commandsTag = NBTAPI.getStringList(usageAttackTag, CucumberyTag.USAGE_COMMANDS_KEY);
    String permission = NBTAPI.getString(usageAttackTag, CucumberyTag.PERMISSION_KEY);
    if (permission != null && !attacker.hasPermission(permission))
    {
      return;
    }
    if (commandsTag != null)
    {
      try
      {
        NBTCompound cooldownTag = NBTAPI.getCompound(usageAttackTag, CucumberyTag.COOLDOWN_KEY);
        long cooldownTime = Objects.requireNonNull(cooldownTag).getLong(CucumberyTag.TIME_KEY);
        String cooldownTagTag = cooldownTag.getString(CucumberyTag.TAG_KEY);
        UUID uuid = attacker.getUniqueId();
        YamlConfiguration configPlayerCooldown = Variable.cooldownsItemUsage.get(uuid);
        long nextAvailable = configPlayerCooldown == null ? 0 : configPlayerCooldown.getLong(cooldownTagTag);
        long currentTime = System.currentTimeMillis();
        if (currentTime < nextAvailable)
        {
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
        // DO NOTHING
      }
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        for (String command : commandsTag)
        {
          Method.performCommand(attacker, command, true, true, event);
        }
      }, 0L);
    }
  }

  private void attackPlayerRangedCommand(EntityDamageByEntityEvent event, @NotNull Player attacker, boolean isMelee, boolean isPvP)
  {
    if (isMelee || !isPvP)
    {
      return;
    }
    ItemStack attackerWeapon = ItemStackUtil.getPlayerUsingItem(attacker, RANGED_WEAPONS);
    NBTCompound itemTag = NBTAPI.getMainCompound(attackerWeapon);
    NBTCompound usageTag = NBTAPI.getCompound(itemTag, CucumberyTag.USAGE_KEY);
    NBTCompound usageAttackTag = NBTAPI.getCompound(usageTag, CucumberyTag.USAGE_COMMANDS_ATTACK_PLAYER_RANGED_KEY);
    NBTList<String> commandsTag = NBTAPI.getStringList(usageAttackTag, CucumberyTag.USAGE_COMMANDS_KEY);
    String permission = NBTAPI.getString(usageAttackTag, CucumberyTag.PERMISSION_KEY);
    if (permission != null && !attacker.hasPermission(permission))
    {
      return;
    }
    if (commandsTag != null)
    {
      try
      {
        NBTCompound cooldownTag = NBTAPI.getCompound(usageAttackTag, CucumberyTag.COOLDOWN_KEY);
        long cooldownTime = Objects.requireNonNull(cooldownTag).getLong(CucumberyTag.TIME_KEY);
        String cooldownTagTag = cooldownTag.getString(CucumberyTag.TAG_KEY);
        UUID uuid = attacker.getUniqueId();
        YamlConfiguration configPlayerCooldown = Variable.cooldownsItemUsage.get(uuid);
        long nextAvailable = configPlayerCooldown == null ? 0 : configPlayerCooldown.getLong(cooldownTagTag);
        long currentTime = System.currentTimeMillis();
        if (currentTime < nextAvailable)
        {
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
        // DO NOTHING
      }
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        for (String command : commandsTag)
        {
          Method.performCommand(attacker, command, true, true, event);
        }
      }, 0L);
    }
  }

  private void attackEntityRangedCommand(EntityDamageByEntityEvent event, @NotNull Player attacker, boolean isMelee, boolean isPvP)
  {
    if (isMelee || isPvP)
    {
      return;
    }
    ItemStack attackerWeapon = ItemStackUtil.getPlayerUsingItem(attacker, RANGED_WEAPONS);
    NBTCompound itemTag = NBTAPI.getMainCompound(attackerWeapon);
    NBTCompound usageTag = NBTAPI.getCompound(itemTag, CucumberyTag.USAGE_KEY);
    NBTCompound usageAttackTag = NBTAPI.getCompound(usageTag, CucumberyTag.USAGE_COMMANDS_ATTACK_ENTITY_RANGED_KEY);
    NBTList<String> commandsTag = NBTAPI.getStringList(usageAttackTag, CucumberyTag.USAGE_COMMANDS_KEY);
    String permission = NBTAPI.getString(usageAttackTag, CucumberyTag.PERMISSION_KEY);
    if (permission != null && !attacker.hasPermission(permission))
    {
      return;
    }
    if (commandsTag != null)
    {
      try
      {
        NBTCompound cooldownTag = NBTAPI.getCompound(usageAttackTag, CucumberyTag.COOLDOWN_KEY);
        long cooldownTime = Objects.requireNonNull(cooldownTag).getLong(CucumberyTag.TIME_KEY);
        String cooldownTagTag = cooldownTag.getString(CucumberyTag.TAG_KEY);
        UUID uuid = attacker.getUniqueId();
        YamlConfiguration configPlayerCooldown = Variable.cooldownsItemUsage.get(uuid);
        long nextAvailable = configPlayerCooldown == null ? 0 : configPlayerCooldown.getLong(cooldownTagTag);
        long currentTime = System.currentTimeMillis();
        if (currentTime < nextAvailable)
        {
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
        // DO NOTHING
      }
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        for (String command : commandsTag)
        {
          Method.performCommand(attacker, command, true, true, event);
        }
      }, 0L);
    }
  }

  private void sneakAttackCommand(EntityDamageByEntityEvent event, @NotNull Player attacker, boolean isMelee, boolean isSneaking)
  {
    if (!isSneaking)
    {
      return;
    }
    ItemStack attackerWeapon = isMelee ? attacker.getInventory().getItemInMainHand() : ItemStackUtil.getPlayerUsingItem(attacker, RANGED_WEAPONS);
    NBTCompound itemTag = NBTAPI.getMainCompound(attackerWeapon);
    NBTCompound usageTag = NBTAPI.getCompound(itemTag, CucumberyTag.USAGE_KEY);
    NBTCompound usageAttackTag = NBTAPI.getCompound(usageTag, CucumberyTag.USAGE_COMMANDS_SNEAK_ATTACK_KEY);
    NBTList<String> commandsTag = NBTAPI.getStringList(usageAttackTag, CucumberyTag.USAGE_COMMANDS_KEY);
    String permission = NBTAPI.getString(usageAttackTag, CucumberyTag.PERMISSION_KEY);
    if (permission != null && !attacker.hasPermission(permission))
    {
      return;
    }
    if (commandsTag != null)
    {
      try
      {
        NBTCompound cooldownTag = NBTAPI.getCompound(usageAttackTag, CucumberyTag.COOLDOWN_KEY);
        long cooldownTime = Objects.requireNonNull(cooldownTag).getLong(CucumberyTag.TIME_KEY);
        String cooldownTagTag = cooldownTag.getString(CucumberyTag.TAG_KEY);
        UUID uuid = attacker.getUniqueId();
        YamlConfiguration configPlayerCooldown = Variable.cooldownsItemUsage.get(uuid);
        long nextAvailable = configPlayerCooldown == null ? 0 : configPlayerCooldown.getLong(cooldownTagTag);
        long currentTime = System.currentTimeMillis();
        if (currentTime < nextAvailable)
        {
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
        // DO NOTHING
      }
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        for (String command : commandsTag)
        {
          Method.performCommand(attacker, command, true, true, event);
        }
      }, 0L);
    }
  }

  private void sneakAttackPlayerCommand(EntityDamageByEntityEvent event, @NotNull Player attacker, boolean isMelee, boolean isPvP, boolean isSneaking)
  {
    if (!isSneaking || !isPvP)
    {
      return;
    }
    ItemStack attackerWeapon = isMelee ? attacker.getInventory().getItemInMainHand() : ItemStackUtil.getPlayerUsingItem(attacker, RANGED_WEAPONS);
    NBTCompound itemTag = NBTAPI.getMainCompound(attackerWeapon);
    NBTCompound usageTag = NBTAPI.getCompound(itemTag, CucumberyTag.USAGE_KEY);
    NBTCompound usageAttackTag = NBTAPI.getCompound(usageTag, CucumberyTag.USAGE_COMMANDS_SNEAK_ATTACK_PLAYER_KEY);
    NBTList<String> commandsTag = NBTAPI.getStringList(usageAttackTag, CucumberyTag.USAGE_COMMANDS_KEY);
    String permission = NBTAPI.getString(usageAttackTag, CucumberyTag.PERMISSION_KEY);
    if (permission != null && !attacker.hasPermission(permission))
    {
      return;
    }
    if (commandsTag != null)
    {
      try
      {
        NBTCompound cooldownTag = NBTAPI.getCompound(usageAttackTag, CucumberyTag.COOLDOWN_KEY);
        long cooldownTime = Objects.requireNonNull(cooldownTag).getLong(CucumberyTag.TIME_KEY);
        String cooldownTagTag = cooldownTag.getString(CucumberyTag.TAG_KEY);
        UUID uuid = attacker.getUniqueId();
        YamlConfiguration configPlayerCooldown = Variable.cooldownsItemUsage.get(uuid);
        long nextAvailable = configPlayerCooldown == null ? 0 : configPlayerCooldown.getLong(cooldownTagTag);
        long currentTime = System.currentTimeMillis();
        if (currentTime < nextAvailable)
        {
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
        // DO NOTHING
      }
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        for (String command : commandsTag)
        {
          Method.performCommand(attacker, command, true, true, event);
        }
      }, 0L);
    }
  }

  private void sneakAttackEntityCommand(EntityDamageByEntityEvent event, @NotNull Player attacker, boolean isMelee, boolean isPvP, boolean isSneaking)
  {
    if (!isSneaking || isPvP)
    {
      return;
    }
    ItemStack attackerWeapon = isMelee ? attacker.getInventory().getItemInMainHand() : ItemStackUtil.getPlayerUsingItem(attacker, RANGED_WEAPONS);
    NBTCompound itemTag = NBTAPI.getMainCompound(attackerWeapon);
    NBTCompound usageTag = NBTAPI.getCompound(itemTag, CucumberyTag.USAGE_KEY);
    NBTCompound usageAttackTag = NBTAPI.getCompound(usageTag, CucumberyTag.USAGE_COMMANDS_SNEAK_ATTACK_ENTITY_KEY);
    NBTList<String> commandsTag = NBTAPI.getStringList(usageAttackTag, CucumberyTag.USAGE_COMMANDS_KEY);
    String permission = NBTAPI.getString(usageAttackTag, CucumberyTag.PERMISSION_KEY);
    if (permission != null && !attacker.hasPermission(permission))
    {
      return;
    }
    if (commandsTag != null)
    {
      try
      {
        NBTCompound cooldownTag = NBTAPI.getCompound(usageAttackTag, CucumberyTag.COOLDOWN_KEY);
        long cooldownTime = Objects.requireNonNull(cooldownTag).getLong(CucumberyTag.TIME_KEY);
        String cooldownTagTag = cooldownTag.getString(CucumberyTag.TAG_KEY);
        UUID uuid = attacker.getUniqueId();
        YamlConfiguration configPlayerCooldown = Variable.cooldownsItemUsage.get(uuid);
        long nextAvailable = configPlayerCooldown == null ? 0 : configPlayerCooldown.getLong(cooldownTagTag);
        long currentTime = System.currentTimeMillis();
        if (currentTime < nextAvailable)
        {
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
        // DO NOTHING
      }
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        for (String command : commandsTag)
        {
          Method.performCommand(attacker, command, true, true, event);
        }
      }, 0L);
    }
  }

  private void sneakAttackMeleeCommand(EntityDamageByEntityEvent event, @NotNull Player attacker, boolean isMelee, boolean isSneaking)
  {
    if (!isSneaking || !isMelee)
    {
      return;
    }
    ItemStack attackerWeapon = attacker.getInventory().getItemInMainHand();
    NBTCompound itemTag = NBTAPI.getMainCompound(attackerWeapon);
    NBTCompound usageTag = NBTAPI.getCompound(itemTag, CucumberyTag.USAGE_KEY);
    NBTCompound usageAttackTag = NBTAPI.getCompound(usageTag, CucumberyTag.USAGE_COMMANDS_SNEAK_ATTACK_MELEE_KEY);
    NBTList<String> commandsTag = NBTAPI.getStringList(usageAttackTag, CucumberyTag.USAGE_COMMANDS_KEY);
    String permission = NBTAPI.getString(usageAttackTag, CucumberyTag.PERMISSION_KEY);
    if (permission != null && !attacker.hasPermission(permission))
    {
      return;
    }
    if (commandsTag != null)
    {
      try
      {
        NBTCompound cooldownTag = NBTAPI.getCompound(usageAttackTag, CucumberyTag.COOLDOWN_KEY);
        long cooldownTime = Objects.requireNonNull(cooldownTag).getLong(CucumberyTag.TIME_KEY);
        String cooldownTagTag = cooldownTag.getString(CucumberyTag.TAG_KEY);
        UUID uuid = attacker.getUniqueId();
        YamlConfiguration configPlayerCooldown = Variable.cooldownsItemUsage.get(uuid);
        long nextAvailable = configPlayerCooldown == null ? 0 : configPlayerCooldown.getLong(cooldownTagTag);
        long currentTime = System.currentTimeMillis();
        if (currentTime < nextAvailable)
        {
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
        // DO NOTHING
      }
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        for (String command : commandsTag)
        {
          Method.performCommand(attacker, command, true, true, event);
        }
      }, 0L);
    }
  }

  private void sneakAttackPlayerMeleeCommand(EntityDamageByEntityEvent event, @NotNull Player attacker, boolean isMelee, boolean isPvP, boolean isSneaking)
  {
    if (!isSneaking || !isMelee || !isPvP)
    {
      return;
    }
    ItemStack attackerWeapon = attacker.getInventory().getItemInMainHand();
    NBTCompound itemTag = NBTAPI.getMainCompound(attackerWeapon);
    NBTCompound usageTag = NBTAPI.getCompound(itemTag, CucumberyTag.USAGE_KEY);
    NBTCompound usageAttackTag = NBTAPI.getCompound(usageTag, CucumberyTag.USAGE_COMMANDS_SNEAK_ATTACK_PLAYER_MELEE_KEY);
    NBTList<String> commandsTag = NBTAPI.getStringList(usageAttackTag, CucumberyTag.USAGE_COMMANDS_KEY);
    String permission = NBTAPI.getString(usageAttackTag, CucumberyTag.PERMISSION_KEY);
    if (permission != null && !attacker.hasPermission(permission))
    {
      return;
    }
    if (commandsTag != null)
    {
      try
      {
        NBTCompound cooldownTag = NBTAPI.getCompound(usageAttackTag, CucumberyTag.COOLDOWN_KEY);
        long cooldownTime = Objects.requireNonNull(cooldownTag).getLong(CucumberyTag.TIME_KEY);
        String cooldownTagTag = cooldownTag.getString(CucumberyTag.TAG_KEY);
        UUID uuid = attacker.getUniqueId();
        YamlConfiguration configPlayerCooldown = Variable.cooldownsItemUsage.get(uuid);
        long nextAvailable = configPlayerCooldown == null ? 0 : configPlayerCooldown.getLong(cooldownTagTag);
        long currentTime = System.currentTimeMillis();
        if (currentTime < nextAvailable)
        {
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
        // DO NOTHING
      }
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        for (String command : commandsTag)
        {
          Method.performCommand(attacker, command, true, true, event);
        }
      }, 0L);
    }
  }

  private void sneakAttackEntityMeleeCommand(EntityDamageByEntityEvent event, @NotNull Player attacker, boolean isMelee, boolean isPvP, boolean isSneaking)
  {
    if (!isSneaking || !isMelee || isPvP)
    {
      return;
    }
    ItemStack attackerWeapon = attacker.getInventory().getItemInMainHand();
    NBTCompound itemTag = NBTAPI.getMainCompound(attackerWeapon);
    NBTCompound usageTag = NBTAPI.getCompound(itemTag, CucumberyTag.USAGE_KEY);
    NBTCompound usageAttackTag = NBTAPI.getCompound(usageTag, CucumberyTag.USAGE_COMMANDS_SNEAK_ATTACK_ENTITY_MELEE_KEY);
    NBTList<String> commandsTag = NBTAPI.getStringList(usageAttackTag, CucumberyTag.USAGE_COMMANDS_KEY);
    String permission = NBTAPI.getString(usageAttackTag, CucumberyTag.PERMISSION_KEY);
    if (permission != null && !attacker.hasPermission(permission))
    {
      return;
    }
    if (commandsTag != null)
    {
      try
      {
        NBTCompound cooldownTag = NBTAPI.getCompound(usageAttackTag, CucumberyTag.COOLDOWN_KEY);
        long cooldownTime = Objects.requireNonNull(cooldownTag).getLong(CucumberyTag.TIME_KEY);
        String cooldownTagTag = cooldownTag.getString(CucumberyTag.TAG_KEY);
        UUID uuid = attacker.getUniqueId();
        YamlConfiguration configPlayerCooldown = Variable.cooldownsItemUsage.get(uuid);
        long nextAvailable = configPlayerCooldown == null ? 0 : configPlayerCooldown.getLong(cooldownTagTag);
        long currentTime = System.currentTimeMillis();
        if (currentTime < nextAvailable)
        {
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
        // DO NOTHING
      }
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        for (String command : commandsTag)
        {
          Method.performCommand(attacker, command, true, true, event);
        }
      }, 0L);
    }
  }

  private void sneakAttackRangedCommand(EntityDamageByEntityEvent event, @NotNull Player attacker, boolean isMelee, boolean isSneaking)
  {
    if (!isSneaking || isMelee)
    {
      return;
    }
    ItemStack attackerWeapon = ItemStackUtil.getPlayerUsingItem(attacker, RANGED_WEAPONS);
    NBTCompound itemTag = NBTAPI.getMainCompound(attackerWeapon);
    NBTCompound usageTag = NBTAPI.getCompound(itemTag, CucumberyTag.USAGE_KEY);
    NBTCompound usageAttackTag = NBTAPI.getCompound(usageTag, CucumberyTag.USAGE_COMMANDS_SNEAK_ATTACK_RANGED_KEY);
    NBTList<String> commandsTag = NBTAPI.getStringList(usageAttackTag, CucumberyTag.USAGE_COMMANDS_KEY);
    String permission = NBTAPI.getString(usageAttackTag, CucumberyTag.PERMISSION_KEY);
    if (permission != null && !attacker.hasPermission(permission))
    {
      return;
    }
    if (commandsTag != null)
    {
      try
      {
        NBTCompound cooldownTag = NBTAPI.getCompound(usageAttackTag, CucumberyTag.COOLDOWN_KEY);
        long cooldownTime = Objects.requireNonNull(cooldownTag).getLong(CucumberyTag.TIME_KEY);
        String cooldownTagTag = cooldownTag.getString(CucumberyTag.TAG_KEY);
        UUID uuid = attacker.getUniqueId();
        YamlConfiguration configPlayerCooldown = Variable.cooldownsItemUsage.get(uuid);
        long nextAvailable = configPlayerCooldown == null ? 0 : configPlayerCooldown.getLong(cooldownTagTag);
        long currentTime = System.currentTimeMillis();
        if (currentTime < nextAvailable)
        {
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
        // DO NOTHING
      }
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        for (String command : commandsTag)
        {
          Method.performCommand(attacker, command, true, true, event);
        }
      }, 0L);
    }
  }

  private void sneakAttackPlayerRangedCommand(EntityDamageByEntityEvent event, @NotNull Player attacker, boolean isMelee, boolean isPvP, boolean isSneaking)
  {
    if (!isSneaking || isMelee || !isPvP)
    {
      return;
    }
    ItemStack attackerWeapon = ItemStackUtil.getPlayerUsingItem(attacker, RANGED_WEAPONS);
    NBTCompound itemTag = NBTAPI.getMainCompound(attackerWeapon);
    NBTCompound usageTag = NBTAPI.getCompound(itemTag, CucumberyTag.USAGE_KEY);
    NBTCompound usageAttackTag = NBTAPI.getCompound(usageTag, CucumberyTag.USAGE_COMMANDS_SNEAK_ATTACK_PLAYER_RANGED_KEY);
    NBTList<String> commandsTag = NBTAPI.getStringList(usageAttackTag, CucumberyTag.USAGE_COMMANDS_KEY);
    String permission = NBTAPI.getString(usageAttackTag, CucumberyTag.PERMISSION_KEY);
    if (permission != null && !attacker.hasPermission(permission))
    {
      return;
    }
    if (commandsTag != null)
    {
      try
      {
        NBTCompound cooldownTag = NBTAPI.getCompound(usageAttackTag, CucumberyTag.COOLDOWN_KEY);
        long cooldownTime = Objects.requireNonNull(cooldownTag).getLong(CucumberyTag.TIME_KEY);
        String cooldownTagTag = cooldownTag.getString(CucumberyTag.TAG_KEY);
        UUID uuid = attacker.getUniqueId();
        YamlConfiguration configPlayerCooldown = Variable.cooldownsItemUsage.get(uuid);
        long nextAvailable = configPlayerCooldown == null ? 0 : configPlayerCooldown.getLong(cooldownTagTag);
        long currentTime = System.currentTimeMillis();
        if (currentTime < nextAvailable)
        {
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
        // DO NOTHING
      }
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        for (String command : commandsTag)
        {
          Method.performCommand(attacker, command, true, true, event);
        }
      }, 0L);
    }
  }

  private void sneakAttackEntityRangedCommand(EntityDamageByEntityEvent event, @NotNull Player attacker, boolean isMelee, boolean isPvP, boolean isSneaking)
  {
    if (!isSneaking || isMelee || isPvP)
    {
      return;
    }
    ItemStack attackerWeapon = ItemStackUtil.getPlayerUsingItem(attacker, RANGED_WEAPONS);
    NBTCompound itemTag = NBTAPI.getMainCompound(attackerWeapon);
    NBTCompound usageTag = NBTAPI.getCompound(itemTag, CucumberyTag.USAGE_KEY);
    NBTCompound usageAttackTag = NBTAPI.getCompound(usageTag, CucumberyTag.USAGE_COMMANDS_SNEAK_ATTACK_ENTITY_RANGED_KEY);
    NBTList<String> commandsTag = NBTAPI.getStringList(usageAttackTag, CucumberyTag.USAGE_COMMANDS_KEY);
    String permission = NBTAPI.getString(usageAttackTag, CucumberyTag.PERMISSION_KEY);
    if (permission != null && !attacker.hasPermission(permission))
    {
      return;
    }
    if (commandsTag != null)
    {
      try
      {
        NBTCompound cooldownTag = NBTAPI.getCompound(usageAttackTag, CucumberyTag.COOLDOWN_KEY);
        long cooldownTime = Objects.requireNonNull(cooldownTag).getLong(CucumberyTag.TIME_KEY);
        String cooldownTagTag = cooldownTag.getString(CucumberyTag.TAG_KEY);
        UUID uuid = attacker.getUniqueId();
        YamlConfiguration configPlayerCooldown = Variable.cooldownsItemUsage.get(uuid);
        long nextAvailable = configPlayerCooldown == null ? 0 : configPlayerCooldown.getLong(cooldownTagTag);
        long currentTime = System.currentTimeMillis();
        if (currentTime < nextAvailable)
        {
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
        // DO NOTHING
      }
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        for (String command : commandsTag)
        {
          Method.performCommand(attacker, command, true, true, event);
        }
      }, 0L);
    }
  }

/*  private void rpg(EntityDamageByEntityEvent event)
  {
    if (!Cucumbery.config.getBoolean("rpg-enabled"))
    {
      return;
    }
    List<String> worlds = Cucumbery.config.getStringList("no-rpg-enabled-worlds");
    if (Method.configContainsLocation(event.getDamager().getLocation(), worlds))
    {
      return;
    }
    if (event.isCancelled())
    {
      return;
    }
    if ((event.getDamager().getType() != EntityType.PLAYER) && (event.getDamager().getType() != EntityType.ARROW) && (event.getDamager().getType() != EntityType.SPECTRAL_ARROW))
    {
      return;
    }

    if (!(event.getEntity() instanceof LivingEntity entity))
    {
      return;
    }

    // 활 공격
    if (event.getDamager() instanceof Projectile p)
    {

      if (!(p.getShooter() instanceof Player player))
      {
        return;
      }

      if (!(p.getType() == EntityType.ARROW || p.getType() == EntityType.SPECTRAL_ARROW))
      {
        return;
      }

      UUID uuid = player.getUniqueId();

      long[] stat = StatManager.getStatManager().getStat(player);

      double proficiency = stat[5] / 100D;

      double vanilla = event.getDamage();

      double bowStrength = stat[6] / 10000D;

      boolean penalty = false;

      double dmg = 0D;

      double additionalMinecraft = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MINECRAFT_DAMAGE);
      double additionalStat = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.STAT_DAMAGE);
      double additionalDamage = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.DAMAGE);
      double additionalMob = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MOB_DAMAGE);
      double additionalBoss = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.BOSS_DAMAGE);
      double additionalFinal = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.FINAL_DAMAGE);

      double additionalMinecraftPer = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MINECRAFT_DAMAGE_PER);
      double additionalStatPer = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.STAT_DAMAGE_PER);
      double additionalDamagePer = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.DAMAGE_PER);
      double additionalMobPer = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MOB_DAMAGE_PER);
      double additionalBossPer = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.BOSS_DAMAGE_PER);
      double additionalFinalPer = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.FINAL_DAMAGE_PER);

      double critChance = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.CRIT_CHANCE);

      double additionalMinCrit = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MIN_CRIT);
      double additionalMaxCrit = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MAX_CRIT);

      if (!ItemStackUtil.itemExists(Variable.rpg_bow.get(uuid)) || Variable.rpg_bow2.get(uuid) == null)
      {
        return;
      }

      if (Variable.rpg_bow2.get(uuid) == 0)
      {
        penalty = player.getInventory().getItemInMainHand().getType() != Material.BOW || player.getInventory().getItemInOffHand().getType() == Material.BOW;
      }

      else if (Variable.rpg_bow2.get(uuid) == 1)
      {
        penalty = player.getInventory().getItemInMainHand().getType() == Material.BOW || player.getInventory().getItemInOffHand().getType() != Material.BOW;
      }

      else if (Variable.rpg_bow2.get(uuid) == 2)
      {
        penalty = player.getInventory().getItemInMainHand().getType() != Material.BOW || player.getInventory().getItemInOffHand().getType() != Material.BOW;
      }

      if (player.getInventory().getItemInMainHand().getType() != Material.BOW && player.getInventory().getItemInOffHand().getType() != Material.BOW && (player.getInventory().getItemInMainHand()
              .getType() != Material.AIR || player.getInventory().getItemInOffHand().getType() != Material.AIR))
      {
        penalty = true;
      }

      Variable.rpg_bow.remove(uuid);
      Variable.rpg_bow2.remove(uuid);

      double random = Method.random(1, 1000000) / 1000000D + proficiency / 100D;

      double maxDamage = DamageCalculator.getDamageCalc().rangeMaxDamage(GetStat.getStatClass().getLongStat(player, GetStat.Stat.STR), GetStat.getStatClass().getLongStat(player, GetStat.Stat.DEX),
              GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.PATK));

      double minDamage = DamageCalculator.getDamageCalc().rangeMinDamage(GetStat.getStatClass().getLongStat(player, GetStat.Stat.STR), GetStat.getStatClass().getLongStat(player, GetStat.Stat.DEX),
              GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.PATK), proficiency);

      while (random > 1D || random < 0.1D)
      {
        random = Method.random(1, 1000000) / 1000000D + proficiency / 100D;
      }

      while (dmg > maxDamage || dmg < minDamage)
      {
        random = Method.random(1, 1000000) / 1000000D + proficiency / 100D;
        dmg = maxDamage * random;
      }

      if (DamageDebugCommand.debug.containsKey(player))
      {
        MessageUtil.sendMessage(player, "&6------------------------------------------------------------------");
        MessageUtil.sendMessage(player, "&b숙련도 비례 대미지(&e&l" + Math.round(proficiency * 100D) / 100D + "%~&b) : &e&l" + Math.round(random * 10000D) / 100D + "%");
        MessageUtil.sendMessage(player, "&a&l활당김 : &9&l" + Constant.Sosu2.format(bowStrength * 100D) + "%");
      }

      vanilla += vanilla * 0.01 * additionalMinecraftPer;
      vanilla += additionalMinecraft;

      if (DamageDebugCommand.debug.containsKey(player))
      {
        MessageUtil.sendMessage(player,
                "&e마인크래프트 대미지 : &a&l" + Constant.Sosu2.format(vanilla) + "&e(&c&l" + Constant.Sosu2.format(additionalMinecraftPer) + "&c&l% + " + Constant.Sosu2.format(additionalMinecraft) + "&e)");
      }

      dmg += dmg * 0.01 * additionalStatPer;
      dmg += additionalStat;

      if (DamageDebugCommand.debug.containsKey(player))
      {
        MessageUtil.sendMessage(player, "&e스탯 대미지 : &a&l" + Constant.Sosu2.format(dmg) + "&e(&c&l" + Constant.Sosu2.format(additionalStatPer) + "&c&l% + " + Constant.Sosu2.format(additionalStat) + "&e)");
      }

      double statAndVanilla = dmg + vanilla;

      statAndVanilla += statAndVanilla * 0.01 * additionalDamagePer;
      statAndVanilla += additionalDamage;

      if (DamageDebugCommand.debug.containsKey(player))
      {
        MessageUtil.sendMessage(player,
                "&b기본 총합 대미지 : &3&l" + Constant.Sosu2.format(statAndVanilla) + "&e(&2&l" + Constant.Sosu2.format(additionalDamagePer) + "&2&l% + " + Constant.Sosu2.format(additionalDamage) + "&e)");
      }

      double mobDamage = 0D;

      mobDamage += mobDamage * 0.01 * additionalMobPer;
      mobDamage += additionalMob;

      double bossDamage = 0D;

      bossDamage += bossDamage * 0.01 * additionalBossPer;
      bossDamage += additionalBoss;

      if (entity.getType() == EntityType.WITHER || entity.getType() == EntityType.ENDER_DRAGON || (entity.getCustomName() != null && entity.getCustomName().contains("보스")))
      {
        statAndVanilla += bossDamage;

        if (DamageDebugCommand.debug.containsKey(player))
        {
          MessageUtil.sendMessage(player,
                  "&6추가 보스 공격 대미지 : &3&l+" + Constant.Sosu2.format(bossDamage) + "&e(&c&l" + Constant.Sosu2.format(additionalBossPer) + "&c&l% + " + Constant.Sosu2.format(additionalBoss) + "&e)");
        }
      }

      else
      {
        statAndVanilla += mobDamage;

        if (DamageDebugCommand.debug.containsKey(player))
        {
          MessageUtil.sendMessage(player,
                  "&a추가 몬스터 공격 대미지 : &3&l+" + Constant.Sosu2.format(mobDamage) + "&e(&c&l" + Constant.Sosu2.format(additionalMobPer) + "&c&l% + " + Constant.Sosu2.format(additionalMob) + "&e)");
        }
      }

      boolean crit = DamageCalculator.getDamageCalc().critChance(critChance);

      if (crit)
      {
        double critDamage;

        double minCritPer = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MIN_CRIT_PER) / 100D; // 20%
        // 이면
        // 0.2
        double maxCritPer = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MAX_CRIT_PER) / 100D; // 50%
        // 이면
        // 0.5

        double minCrit = statAndVanilla * minCritPer;

        double maxCrit = statAndVanilla * maxCritPer;

        double calcMinCrit = minCrit + additionalMinCrit;
        double calcMaxCrit = maxCrit + additionalMaxCrit;

        if (calcMinCrit <= 0)
        {
          additionalMinCrit = 0 - minCrit;
        }

        if (calcMinCrit > calcMaxCrit)
        {
          additionalMinCrit -= calcMinCrit - calcMaxCrit;
        }

        if (calcMaxCrit <= 0)
        {
          additionalMaxCrit = 0 - maxCrit;
          additionalMinCrit = 0 - minCrit;
        }

        double ratio = calcMinCrit / calcMaxCrit;

        double temp = Method.random(1, 1000000) / 1000000D + ratio;

        if (ratio < 1)
        {
          while (temp < ratio || temp > 1D)
          {
            temp = Method.random(1, 1000000) / 1000000D + ratio;
          }
        }

        else
        {
          temp = 1D;
        }

        critDamage = calcMaxCrit * temp;

        if (maxCrit + additionalMaxCrit <= 0)
        {
          critDamage = 0D;
        }

        statAndVanilla += critDamage;
        if (DamageDebugCommand.debug.containsKey(player))
        {
          MessageUtil.sendMessage(player, "&d&l크리티컬 추가 대미지 : &a&l+" + Constant.Sosu2.format(critDamage) + "&d&l(확률 : &a&l" + Constant.Sosu2.format(critChance) + "%&d&l, 최소 : &a&l" + Constant.Sosu2
                  .format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MIN_CRIT_PER)) + "%&d&l, 최대 : &a&l" + Constant.Sosu2
                  .format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MAX_CRIT_PER)) + "%&d&l)[&a&l" + Constant.Sosu2.format(minCrit + additionalMinCrit) + "&d&l(&a&l" + Constant.Sosu2
                  .format(minCrit) + "&d&l + &a&l" + Constant.Sosu2.format(additionalMinCrit) + "&d&l) ~ &a&l" + Constant.Sosu2.format(maxCrit + additionalMaxCrit) + "&d&l(&a&l" + Constant.Sosu2
                  .format(maxCrit) + "&d&l + &a&l" + Constant.Sosu2.format(additionalMaxCrit) + "&d&l)]");
        }
      }

      if (DamageDebugCommand.debug.containsKey(player))
      {
        MessageUtil.sendMessage(player, "&3&l대미지 : &c&l" + Constant.Sosu4.format(statAndVanilla));
        MessageUtil.sendMessage(player, "&b&l추가 최종 대미지 : +&2&l" + Constant.Sosu2.format(statAndVanilla * additionalFinalPer / 100D + additionalFinal) + "&b&l(&2&l" + Constant.Sosu2
                .format(additionalFinalPer) + "% &b&l+ &2&l" + Constant.Sosu2.format(additionalFinal) + "&b&l)");
      }

      statAndVanilla *= 1D + additionalFinalPer / 100D;
      statAndVanilla += additionalFinal;

      if (statAndVanilla - vanilla >= 0)
      {
        statAndVanilla -= vanilla;
      }

      if (bowStrength < 1D)
      {
        if (DamageDebugCommand.debug.containsKey(player))
        {
          MessageUtil.sendMessage(player, "&c&l활을 약하게 당겨 분산된 대미지 : &4&l-" + Constant.Sosu2.format(statAndVanilla * (1D - bowStrength)));
        }

        statAndVanilla *= bowStrength;
      }

      statAndVanilla += vanilla;

      if (penalty)
      {
        if (DamageDebugCommand.debug.containsKey(player))
        {
          MessageUtil.sendMessage(player, "&c&l아이템 페널티로 분산된 대미지 : &4&l-" + Constant.Sosu2.format(statAndVanilla - vanilla));
        }

        statAndVanilla = vanilla;
      }

      double finalDamage = (statAndVanilla);

      FileConfiguration config = Cucumbery.config;

      double max = config.getDouble("max-rpg-final-damage");

      if (finalDamage > max)
      {
        finalDamage = max;
      }

      event.setDamage(finalDamage);

      double finalD = finalDamage;
      double nowDmg = event.getDamage();

      finalDamage = nowDmg - finalD;

      if (DamageDebugCommand.debug.containsKey(player))
      {
        MessageUtil.sendMessage(player, "&c&l몬스터 방어력으로 감소된 대미지 : &4&l" + Constant.Sosu4.format(finalD - nowDmg));
      }

      if (DamageDebugCommand.debug.containsKey(player))
      {
        MessageUtil.sendMessage(player, "&6------------------------------------------------------------------");
        MessageUtil.sendMessage(player, "&e&l최종 계산 대미지 : &c&l" + Constant.Sosu4.format(finalDamage));
        MessageUtil.sendMessage(player, "&2------------------------------------------------------------------");
      }
    }

    else
    {
      if (event.getDamager() instanceof Player)
      {
        Player player = (Player) event.getDamager();

        boolean penalty = false;

        ItemStack main = player.getInventory().getItemInMainHand();

        if (ItemStackUtil.hasLore(main))
        {
          List<String> lore = main.getItemMeta().getLore();
          for (String s : Objects.requireNonNull(lore))
          {
            String str = MessageUtil.stripColor(s);

            if (str.contains("장비 분류 : ") && str.contains("원거리 무기"))
            {
              penalty = true;
              break;
            }
          }
        }

        long[] stat = StatManager.getStatManager().getStat(player);

        double proficiency = stat[5] / 100D;

        double vanilla = event.getDamage();

        double dmg = 0D;

        double additionalMinecraft = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MINECRAFT_DAMAGE);
        double additionalStat = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.STAT_DAMAGE);
        double additionalDamage = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.DAMAGE);
        double additionalMob = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MOB_DAMAGE);
        double additionalBoss = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.BOSS_DAMAGE);
        double additionalFinal = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.FINAL_DAMAGE);

        double additionalMinecraftPer = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MINECRAFT_DAMAGE_PER);
        double additionalStatPer = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.STAT_DAMAGE_PER);
        double additionalDamagePer = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.DAMAGE_PER);
        double additionalMobPer = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MOB_DAMAGE_PER);
        double additionalBossPer = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.BOSS_DAMAGE_PER);
        double additionalFinalPer = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.FINAL_DAMAGE_PER);

        double critChance = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.CRIT_CHANCE);

        double additionalMinCrit = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MIN_CRIT);
        double additionalMaxCrit = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MAX_CRIT);

        double ignoreDef = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.IGNORE_DEF);

        double random = Method.random(1, 1000000) / 1000000D + proficiency / 100D;

        double maxDamage = DamageCalculator.getDamageCalc().meleeMaxDamage(GetStat.getStatClass().getLongStat(player, GetStat.Stat.STR), GetStat.getStatClass().getLongStat(player, GetStat.Stat.DEX),
                GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.PATK));

        double minDamage = DamageCalculator.getDamageCalc().meleeMinDamage(GetStat.getStatClass().getLongStat(player, GetStat.Stat.STR), GetStat.getStatClass().getLongStat(player, GetStat.Stat.DEX),
                GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.PATK), proficiency);

        while (random > 1D || random < 0.1D)
        {
          random = Method.random(1, 1000000) / 1000000D + proficiency / 100D;
        }

        while (dmg > maxDamage || dmg < minDamage)
        {
          random = Method.random(1, 1000000) / 1000000D + proficiency / 100D;
          dmg = maxDamage * random;
        }

        if (DamageDebugCommand.debug.containsKey(player))
        {
          MessageUtil.sendMessage(player, "&6------------------------------------------------------------------");
          MessageUtil.sendMessage(player, "&b숙련도 비례 대미지(&e&l" + Math.round(proficiency * 100D) / 100D + "%~&b) : &e&l" + Math.round(random * 10000D) / 100D + "%");
        }

        vanilla += vanilla * 0.01 * additionalMinecraftPer;
        vanilla += additionalMinecraft;

        if (DamageDebugCommand.debug.containsKey(player))
        {
          MessageUtil.sendMessage(player,
                  "&e마인크래프트 대미지 : &a&l" + Constant.Sosu2.format(vanilla) + "&e(&c&l" + Constant.Sosu2.format(additionalMinecraftPer) + "&c&l% + " + Constant.Sosu2.format(additionalMinecraft) + "&e)");
        }

        dmg += dmg * 0.01 * additionalStatPer;
        dmg += additionalStat;

        if (DamageDebugCommand.debug.containsKey(player))
        {
          MessageUtil.sendMessage(player, "&e스탯 대미지 : &a&l" + Constant.Sosu2.format(dmg) + "&e(&c&l" + Constant.Sosu2.format(additionalStatPer) + "&c&l% + " + Constant.Sosu2.format(additionalStat) + "&e)");
        }

        double statAndVanilla = dmg + vanilla;

        statAndVanilla += statAndVanilla * 0.01 * additionalDamagePer;
        statAndVanilla += additionalDamage;

        if (DamageDebugCommand.debug.containsKey(player))
        {
          MessageUtil.sendMessage(player,
                  "&b기본 총합 대미지 : &3&l" + Constant.Sosu2.format(statAndVanilla) + "&e(&2&l" + Constant.Sosu2.format(additionalDamagePer) + "&2&l% + " + Constant.Sosu2.format(additionalDamage) + "&e)");
        }

        double mobDamage = 0D;

        mobDamage += statAndVanilla * 0.01 * additionalMobPer;
        mobDamage += additionalMob;

        double bossDamage = 0D;

        bossDamage += statAndVanilla * 0.01 * additionalBossPer;
        bossDamage += additionalBoss;

        if (entity.getType() == EntityType.WITHER || entity.getType() == EntityType.ENDER_DRAGON || (entity.getCustomName() != null && entity.getCustomName().contains("보스")))
        {
          statAndVanilla += bossDamage;

          if (DamageDebugCommand.debug.containsKey(player))
          {
            MessageUtil.sendMessage(player,
                    "&6추가 보스 공격 대미지 : &3&l+" + Constant.Sosu2.format(bossDamage) + "&e(&c&l" + Constant.Sosu2.format(additionalBossPer) + "&c&l% + " + Constant.Sosu2.format(additionalBoss) + "&e)");
          }
        }

        else
        {
          statAndVanilla += mobDamage;

          if (DamageDebugCommand.debug.containsKey(player))
          {
            MessageUtil.sendMessage(player,
                    "&a추가 몬스터 공격 대미지 : &3&l+" + Constant.Sosu2.format(mobDamage) + "&e(&c&l" + Constant.Sosu2.format(additionalMobPer) + "&c&l% + " + Constant.Sosu2.format(additionalMob) + "&e)");
          }
        }

        boolean crit = DamageCalculator.getDamageCalc().critChance(critChance);

        if (crit)
        {
          double critDamage;

          double minCritPer = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MIN_CRIT_PER) / 100D; // 20%
          // 이면
          // 0.2
          double maxCritPer = GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MAX_CRIT_PER) / 100D; // 50%
          // 이면
          // 0.5

          double minCrit = statAndVanilla * minCritPer;

          double maxCrit = statAndVanilla * maxCritPer;

          double calcMinCrit = minCrit + additionalMinCrit;
          double calcMaxCrit = maxCrit + additionalMaxCrit;

          if (calcMinCrit <= 0)
          {
            additionalMinCrit = 0 - minCrit;
          }

          if (calcMinCrit > calcMaxCrit)
          {
            additionalMinCrit -= calcMinCrit - calcMaxCrit;
          }

          if (calcMaxCrit <= 0)
          {
            additionalMaxCrit = 0 - maxCrit;
            additionalMinCrit = 0 - minCrit;
          }

          double ratio = calcMinCrit / calcMaxCrit;

          double temp = Method.random(1, 1000000) / 1000000D + ratio;

          if (ratio < 1)
          {
            while (temp < ratio || temp > 1D)
            {
              temp = Method.random(1, 1000000) / 1000000D + ratio;
            }
          }

          else
          {
            temp = 1D;
          }

          critDamage = calcMaxCrit * temp;

          if (maxCrit + additionalMaxCrit <= 0)
          {
            critDamage = 0D;
          }

          statAndVanilla += critDamage;
          if (DamageDebugCommand.debug.containsKey(player))
          {
            MessageUtil.sendMessage(player, "&d&l크리티컬 추가 대미지 : &a&l+" + Constant.Sosu2.format(critDamage) + "&d&l(확률 : &a&l" + Constant.Sosu2.format(critChance) + "%&d&l, 최소 : &a&l" + Constant.Sosu2
                    .format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MIN_CRIT_PER)) + "%&d&l, 최대 : &a&l" + Constant.Sosu2
                    .format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MAX_CRIT_PER)) + "%&d&l)[&a&l" + Constant.Sosu2.format(minCrit + additionalMinCrit) + "&d&l(&a&l" + Constant.Sosu2
                    .format(minCrit) + "&d&l + &a&l" + Constant.Sosu2.format(additionalMinCrit) + "&d&l) ~ &a&l" + Constant.Sosu2.format(maxCrit + additionalMaxCrit) + "&d&l(&a&l" + Constant.Sosu2
                    .format(maxCrit) + "&d&l + &a&l" + Constant.Sosu2.format(additionalMaxCrit) + "&d&l)]");
          }
        }

        if (DamageDebugCommand.debug.containsKey(player))
        {
          MessageUtil.sendMessage(player, "&3&l대미지 : &c&l" + Constant.Sosu4.format(statAndVanilla));
          MessageUtil.sendMessage(player, "&b&l추가 최종 대미지 : +&2&l" + Constant.Sosu2.format(statAndVanilla * additionalFinalPer / 100D + additionalFinal) + "&b&l(&2&l" + Constant.Sosu2
                  .format(additionalFinalPer) + "% &b&l+ &2&l" + Constant.Sosu2.format(additionalFinal) + "&b&l)");
        }

        statAndVanilla *= 1D + additionalFinalPer / 100D;
        statAndVanilla += additionalFinal;

        if (penalty)
        {
          if (DamageDebugCommand.debug.containsKey(player))
          {
            MessageUtil.sendMessage(player, "&c&l아이템 페널티로 분산된 대미지 : &4&l-" + Constant.Sosu2.format(statAndVanilla - vanilla));
          }

          statAndVanilla = vanilla;
        }

        double finalDamage = (statAndVanilla);

        FileConfiguration config = Cucumbery.config;

        double max = config.getDouble("max-rpg-final-damage");

        if (finalDamage > max)
        {
          finalDamage = max;
        }

        event.setDamage(finalDamage);

        double finalD = finalDamage;
        double nowDmg = event.getDamage();

        finalDamage = nowDmg - finalD;

        if (DamageDebugCommand.debug.containsKey(player))
        {
          MessageUtil.sendMessage(player, "&c&l몬스터 방어력으로 감소된 대미지 : &4&l" + Constant.Sosu4.format(finalD - nowDmg));
        }

        if (entity.hasMetadata("def_per"))
        {
          try
          {
            double defPer = entity.getMetadata("def_per").get(0).asDouble();

            double defRatio = 100D - (defPer - defPer * ignoreDef);

            double decrease = finalDamage - finalDamage * defRatio / 100D;

            if (decrease > finalDamage)
            {
              decrease = finalDamage;
            }

            if (defRatio < 0D)
            {
              defRatio = 0D;
              event.setDamage(0D);
              MessageUtil.sendActionBar(player, "&c&l몬스터의 방어율이 높아 대미지를 줄 수 없습니다.");
              Method.playSound(player, Constant.WARNING_SOUND);
            }

            else
            {
              event.setDamage(finalDamage * defRatio / 100D);
            }

            if (DamageDebugCommand.debug.containsKey(player))
            {
              MessageUtil.sendMessage(player, "&c몬스터 방어율 : " + Constant.Sosu2.format(defPer) + "%");
              MessageUtil.sendMessage(player, "&c방어율로 인해 감소된 최종 대미지 비율 : " + Constant.Sosu2.format(100D - defRatio) + "%");
              MessageUtil.sendMessage(player, "&c방어율로 인해 감소된 최종 대미지 : " + Constant.Sosu2.format(decrease));
            }
          }

          catch (Exception ignored)
          {

          }
        }

        if (DamageDebugCommand.debug.containsKey(player))
        {
          MessageUtil.sendMessage(player, "&6------------------------------------------------------------------");
          MessageUtil.sendMessage(player, "&e&l최종 계산 대미지 : &c&l" + Constant.Sosu4.format(finalDamage));
          MessageUtil.sendMessage(player, "&2------------------------------------------------------------------");
        }
      }
    }
  }*/

  private void damageActionbar(EntityDamageByEntityEvent event)
  {
    double finalDamage = event.getFinalDamage();
    Entity entity = event.getEntity();
    Entity damager = event.getDamager();
    if (entity.isDead())
    {
      return;
    }
    FileConfiguration config = Cucumbery.config;
    if (!(entity instanceof Mob mob))
    {
      return;
    }
    Player player = null;
    if (damager instanceof Player p)
    {
      player = p;
    }
    else if (damager instanceof Projectile projectile)
    {
      if (projectile.getType() == EntityType.ENDER_PEARL)
      {
        return;
      }
      if (projectile.getType() == EntityType.EGG)
      {
        return;
      }
      if (projectile.getType() == EntityType.SNOWBALL)
      {
        return;
      }
      if (!(projectile.getShooter() instanceof Player p))
      {
        return;
      }
      player = p;
    }
    else if (damager instanceof AreaEffectCloud areaEffectCloud)
    {
      if (areaEffectCloud.getSource() instanceof Player p)
      {
        player = p;
      }
    }
    if (player == null)
    {
      return;
    }
    boolean showActionbar = UserData.SHOW_ACTIONBAR_ON_ATTACK.getBoolean(player.getUniqueId());
    boolean showActionbarPVP = UserData.SHOW_ACTIONBAR_ON_ATTACK_PVP.getBoolean(player.getUniqueId());
    boolean showActionbarForce = UserData.SHOW_ACTIONBAR_ON_ATTACK_FORCE.getBoolean(player.getUniqueId());
    boolean showActionbarPVPForce = UserData.SHOW_ACTIONBAR_ON_ATTACK_PVP_FORCE.getBoolean(player.getUniqueId());
    boolean actionbarConfig = config.getBoolean("show-actionbar-on-attack");
    if (!actionbarConfig && !showActionbarForce && !showActionbarPVPForce)
    {
      return; // 콘픽이 비활성화이며, 강제 액션바 출력, 강제 PVP 액션바 출력 모두 false라면
    }
    List<String> noWorlds = config.getStringList("no-show-actionbar-on-attack-worlds");
    boolean actionbarNoWorld = Method.configContainsLocation(entity.getLocation(), noWorlds);
    if (actionbarNoWorld && !showActionbarForce && !showActionbarPVPForce)
    {
      return; // 기능이 비활성화된 위치에 있으며, 강제 액션바 출력, 강제 PVP 액션바 출력 모두 false라면
    }
    if (!showActionbar && !showActionbarForce && !showActionbarPVPForce)
    {
      return; // 액션바를 출력 기능이 false이고, 강제 액션바 출력, 강제 PVP 액션바 출력 모두 false라면
    }
    if (mob.getType() == EntityType.PLAYER)
    {
      if (!config.getBoolean("show-actionbar-on-pvp") && !showActionbarForce && !showActionbarPVPForce)
      {
        return; // 콘픽이 비활성화이며, 강제 액션바 출력, 강제 PVP 액션바 출력 모두 false라면
      }
      if (Method.configContainsLocation(entity.getLocation(), config.getStringList("no-show-actionbar-on-pvp-worlds")) && !showActionbarForce && !showActionbarPVPForce)
      {
        return; // 기능이 비활성화된 위치에 있으며, 강제 액션바 출력, 강제 PVP 액션바 출력 모두 false라면
      }
      Player target = (Player) mob;
      boolean hideActionbar = UserData.HIDE_ACTIONBAR_ON_ATTACK_PVP_TO_OTHERS.getBoolean(target.getUniqueId());
      if ((!showActionbarPVP || hideActionbar) && !showActionbarForce && !showActionbarPVPForce)
      {
        return; // PVP 액션바 출력 기능이 false이거나, 상대방의 PVP 액션바 숨김 기능이 true이면서, 강제 액션바 출력, 강제 PVP 액션바 출력 모두 false라면
      }
    }
    else if ((!actionbarConfig || actionbarNoWorld) && !showActionbarForce)
    {
      return; // 콘픽이 비활성화이거나 기능이 비활성화된 월드에 있으면서 강제 액션바 출력이 false라면
    }
    int round = config.getInt("actionbar-on-attack-numbers-round-number");
    DecimalFormat df = Constant.Sosu2;
    if (round > 0)
    {
      df = new DecimalFormat("#,###." + "#".repeat(round));
    }

    boolean roundNumber = config.getBoolean("actionbar-on-attack-numbers-round");

    AttributeInstance attributeInstance = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH);

    if (attributeInstance == null)
      return;


    double health = Math.max(0d, mob.getHealth() - finalDamage);
    double maxHealth = attributeInstance.getValue();
    String damageStr = Constant.THE_COLOR_HEX + (roundNumber ? df.format(finalDamage) : finalDamage);
    String healthStr = Constant.THE_COLOR_HEX + (roundNumber ? df.format(health) : health);
    String maxHealthStr = Constant.THE_COLOR_HEX + (roundNumber ? df.format(maxHealth) : maxHealth);

//    String keyAttack = config.getString("actionbars-on-attack"), keyDeath = config.getString("actionbars-on-attack-death");

    String keyAttack = "#52ee52;%s에게 %s만큼의 대미지를 주었습니다. %s / %s", keyDeath = "&c%s에게 %s만큼의 대미지를 주어 죽였습니다. %s / %s";

    if (event.getDamage() > 0D)
    {
      if (health - finalDamage <= 0D)
      {
        MessageUtil.sendActionBar(player, ComponentUtil.createTranslate(keyDeath, mob, damageStr, healthStr, maxHealthStr));
      }
      else
      {
        MessageUtil.sendActionBar(player, ComponentUtil.createTranslate(keyAttack, mob, damageStr, healthStr, maxHealthStr));
      }
    }
    else if (config.getBoolean("play-sound-on-attack-miss"))
    {
      Sound sound;
      try
      {
        sound = Sound.valueOf(config.getString("play-sounds-on-attack-miss.type"));
      }
      catch (Exception e)
      {
        sound = Sound.ENTITY_ENDERMAN_TELEPORT;
      }
      float volume = (float) config.getDouble("play-sounds-on-attack-miss.volume"), pitch = (float) config.getDouble("play-sounds-on-attack-miss.pitch");
      SoundPlay.playSound(player, sound, volume, pitch);
//      String miss = config.getString("actionbars-on-attack-miss");
      String miss = "&d%s에게 피해를 입힐 수 없습니다. %s / %s";
      MessageUtil.sendActionBar(player, ComponentUtil.createTranslate(miss, mob, healthStr, maxHealthStr));
    }
  }

  private void cancelFireworkDamage(EntityDamageByEntityEvent event)
  {
    Entity entity = event.getEntity(), damager = event.getDamager();
    EntityDamageEvent.DamageCause dc = event.getCause();
    if (dc == DamageCause.ENTITY_EXPLOSION && damager.getType() == EntityType.FIREWORK)
    {
      Firework firework = (Firework) damager;
      if (firework.hasMetadata("no_damage"))
      {
        event.setCancelled(true);
      }
    }
    if (Cucumbery.config.getBoolean("prevent-firework-damage"))
    {
      if (!(entity instanceof Damageable))
      {
        return;
      }
      if (Method.configContainsLocation(damager.getLocation(), Cucumbery.config.getStringList("no-prvent-firework-damage-worlds")))
      {
        return;
      }
      if (dc == DamageCause.ENTITY_EXPLOSION && damager.getType() == EntityType.FIREWORK)
      {
        event.setCancelled(true);
      }
    }
  }
}

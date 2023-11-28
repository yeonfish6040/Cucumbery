package com.jho5245.cucumbery.custom;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect.DisplayType;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeRune;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectAbstractApplyEvent.ApplyReason;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.MythicMobManager;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DamageManager
{
  @Nullable
  public static CombatInfo getCombatInfo(@NotNull EntityDamageEvent event)
  {
    final double originalDamage = event.getDamage();
    double damage = event.getDamage();
    double bonusDamage = 0d;
    double damageMultiplier = 1d, finalDamageMultiplier = 1d;
    DamageCause damageCause = event.getCause();
    Entity victim = event.getEntity();
    if (damageCause == DamageCause.FALL && !(event instanceof EntityDamageByEntityEvent) && CustomEffectManager.hasEffect(victim, CustomEffectTypeRune.RUNE_EARTHQUAKE))
    {
      return null;
    }
    if (damageCause == DamageCause.FALL && victim instanceof LivingEntity livingEntity)
    {
      EntityEquipment equipment = livingEntity.getEquipment();
      if (equipment != null)
      {
        ItemStack boots = equipment.getBoots();
        CustomMaterial bootsType = CustomMaterial.itemStackOf(boots);
        if (bootsType == CustomMaterial.FROG_BOOTS)
        {
          finalDamageMultiplier *= 0.8d;
        }
      }
    }
    if (CustomEffectManager.hasEffect(victim, CustomEffectType.INVINCIBLE))
    {
      return null;
    }
    if (CustomEffectManager.hasEffect(victim, CustomEffectType.RESURRECTION_INVINCIBLE) ||
            CustomEffectManager.hasEffect(victim, CustomEffectType.INVINCIBLE_RESPAWN) ||
            CustomEffectManager.hasEffect(victim, CustomEffectType.INVINCIBLE_PLUGIN_RELOAD))
    {
      return null;
    }
    if (victim instanceof LivingEntity livingEntity && CustomEffectManager.hasEffect(victim, CustomEffectType.REMOVE_NO_DAMAGE_TICKS))
    {
      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> livingEntity.setNoDamageTicks(0), 0L);
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
    switch (damageCause)
    {
      case ENTITY_ATTACK, ENTITY_SWEEP_ATTACK, ENTITY_EXPLOSION, CUSTOM, PROJECTILE -> {
        if (damageCause != DamageCause.PROJECTILE || event instanceof EntityDamageByEntityEvent ev && ev.getDamager() instanceof Projectile projectile && projectile.getShooter() instanceof Entity)
        {
          if (victim instanceof LivingEntity livingEntity)
          {
            EntityEquipment equipment = livingEntity.getEquipment();
            if (equipment != null)
            {
              ItemStack helmet = equipment.getHelmet(), chestplate = equipment.getChestplate(), leggings = equipment.getLeggings(), boots = equipment.getBoots();
              if (CustomEnchant.isEnabled() && (helmet != null && helmet.hasItemMeta() && helmet.getItemMeta().hasEnchant(CustomEnchant.HIGH_RISK_HIGH_RETURN) ||
                      chestplate != null && chestplate.hasItemMeta() && chestplate.getItemMeta().hasEnchant(CustomEnchant.HIGH_RISK_HIGH_RETURN) ||
                      leggings != null && leggings.hasItemMeta() && leggings.getItemMeta().hasEnchant(CustomEnchant.HIGH_RISK_HIGH_RETURN) ||
                      boots != null && boots.hasItemMeta() && boots.getItemMeta().hasEnchant(CustomEnchant.HIGH_RISK_HIGH_RETURN)))
              {
                damageMultiplier += 5d;
              }
            }
          }
        }
      }
    }
    switch (damageCause)
    {
      case HOT_FLOOR -> {
        if (CustomEffectManager.hasEffect(victim, CustomEffectType.FROST_WALKER))
        {
          return null;
        }
      }
      case ENTITY_ATTACK, ENTITY_SWEEP_ATTACK, CUSTOM -> {
        if (event instanceof EntityDamageByEntityEvent damageByEntityEvent)
        {
          Entity damagerEntity = damageByEntityEvent.getDamager();
          if (damagerEntity instanceof AreaEffectCloud areaEffectCloud)
          {
            ProjectileSource source = areaEffectCloud.getSource();
            if (source instanceof Entity)
            {
              if (CustomEffectManager.hasEffect(victim, CustomEffectType.DODGE))
              {
                CustomEffect customEffect = CustomEffectManager.getEffect(damagerEntity, CustomEffectType.DODGE);
                int amplifier = customEffect.getAmplifier() + 1;
                if (Math.random() * 100 < amplifier)
                {
                  return null;
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
                return null;
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
              int amplifier = CustomEffectManager.getEffect(damagerEntity, CustomEffectType.WA_SANS).getAmplifier();
              damageMultiplier += (amplifier + 1) * 0.1;
            }
            if (CustomEffectManager.hasEffect(victim, CustomEffectType.WA_SANS) && damagerEntity instanceof AbstractSkeleton)
            {
              int amplifier = CustomEffectManager.getEffect(victim, CustomEffectType.WA_SANS).getAmplifier();
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
            if (CustomEffectManager.hasEffect(damagerEntity, CustomEffectTypeRune.RUNE_DESTRUCTION))
            {
              damageMultiplier += 1d;
            }
          }
          if (damagerEntity instanceof LivingEntity livingEntity)
          {
            EntityEquipment equipment = livingEntity.getEquipment();
            if (equipment != null)
            {
              ItemStack helmet = equipment.getHelmet(), chestplate = equipment.getChestplate(), leggings = equipment.getLeggings(), boots = equipment.getBoots();
              if (CustomEnchant.isEnabled() && (helmet != null && helmet.hasItemMeta() && helmet.getItemMeta().hasEnchant(CustomEnchant.HIGH_RISK_HIGH_RETURN) ||
                      chestplate != null && chestplate.hasItemMeta() && chestplate.getItemMeta().hasEnchant(CustomEnchant.HIGH_RISK_HIGH_RETURN) ||
                      leggings != null && leggings.hasItemMeta() && leggings.getItemMeta().hasEnchant(CustomEnchant.HIGH_RISK_HIGH_RETURN) ||
                      boots != null && boots.hasItemMeta() && boots.getItemMeta().hasEnchant(CustomEnchant.HIGH_RISK_HIGH_RETURN)))
              {
                damageMultiplier += 5d;
              }
              ItemStack mainHand = equipment.getItemInMainHand();
              if (CustomEnchant.isEnabled() && ItemStackUtil.itemExists(mainHand) && mainHand.hasItemMeta() && mainHand.getItemMeta().hasEnchant(CustomEnchant.CLEAVING))
              {
                int level = mainHand.getItemMeta().getEnchantLevel(CustomEnchant.CLEAVING);
                damage += level + 1;
              }
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
              return null;
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
                  return null;
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
              if (CustomEffectManager.hasEffect(projectileDamager, CustomEffectTypeRune.RUNE_DESTRUCTION))
              {
                damageMultiplier += 1d;
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
            if (projectileSource instanceof LivingEntity livingEntity)
            {
              EntityEquipment equipment = livingEntity.getEquipment();
              if (equipment != null)
              {
                ItemStack helmet = equipment.getHelmet(), chestplate = equipment.getChestplate(), leggings = equipment.getLeggings(), boots = equipment.getBoots();
                if (CustomEnchant.isEnabled() && (helmet != null && helmet.hasItemMeta() && helmet.getItemMeta().hasEnchant(CustomEnchant.HIGH_RISK_HIGH_RETURN) ||
                        chestplate != null && chestplate.hasItemMeta() && chestplate.getItemMeta().hasEnchant(CustomEnchant.HIGH_RISK_HIGH_RETURN) ||
                        leggings != null && leggings.hasItemMeta() && leggings.getItemMeta().hasEnchant(CustomEnchant.HIGH_RISK_HIGH_RETURN) ||
                        boots != null && boots.hasItemMeta() && boots.getItemMeta().hasEnchant(CustomEnchant.HIGH_RISK_HIGH_RETURN)))
                {
                  damageMultiplier += 5d;
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
    return new CombatInfo(originalDamage, damage, bonusDamage, damageMultiplier, finalDamageMultiplier);
  }

  @NotNull
  public static CombatInfo getCombatInfo(@NotNull Player player)
  {
    AttributeInstance attributeInstance = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
    final double originalDamage = attributeInstance != null ? attributeInstance.getValue() : 1d;
    double bonusDamage = 0d;
    double damageMultiplier = 1d, finalDamageMultiplier = 1d;
    return new CombatInfo(originalDamage, originalDamage, bonusDamage, damageMultiplier, finalDamageMultiplier);
  }
}

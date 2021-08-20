package com.jho5245.cucumbery.deathmessages;

import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Variable;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

public class ConditionChecker
{
  public static boolean check(EntityDeathEvent event, @NotNull String key, @Nullable Condition... conditions)
  {
    if (conditions == null || conditions.length == 0)
    {
      return true;
    }
    boolean[] check = new boolean[conditions.length];
    LivingEntity victim = event.getEntity();
    Object damagerObject = DeathManager.getDamager(event);
    boolean damagerExists = damagerObject != null;
    boolean damagerIsLiving = damagerObject instanceof  LivingEntity;
    LivingEntity damager = damagerIsLiving ? (LivingEntity) damagerObject : null;
    Component damagerCustomName = damagerIsLiving ? damager.customName() : null;
    ItemStack weapon = DeathManager.getWeapon(event);
    boolean weaponExists = ItemStackUtil.itemExists(weapon);
    for (int i = 0; i < conditions.length; i++)
    {
      try
      {
        Condition condition = conditions[i];
        if (condition != null)
        {
          ConditionType conditionType = condition.getConditionType();
          String value = condition.getValue();
          Pattern pattern = Pattern.compile("^" + value + "$");
          switch (conditionType)
          {
            case DEATH_TYPE -> check[i] = pattern.matcher(key).find();
            case ENTITY_UUID -> check[i] = pattern.matcher(victim.getUniqueId().toString()).find();
            case ENTITY_TYPE -> check[i] = pattern.matcher(victim.getType().toString()).find();
            case PLAYER_NAME -> check[i] = victim instanceof Player && pattern.matcher(victim.getName()).find();
            case PLAYER_DISPLAY_NAME -> check[i] = victim instanceof Player && pattern.matcher(ComponentUtil.serialize(((Player) victim).displayName())).find();
            case PLAYER_PERMISSION -> check[i] = victim.hasPermission(value);
            case WORLD_NAME -> check[i] = pattern.matcher(victim.getLocation().getWorld().getName()).find();
            case BIOME_TYPE -> check[i] = pattern.matcher(victim.getLocation().getBlock().getBiome().toString()).find();
            case ATTACKER_ENTITY_NAME -> check[i] = damagerIsLiving && pattern.matcher(damager.getName()).find();
            case ATTACKER_ENTITY_DISPLAY_NAME -> check[i] = damagerCustomName != null && pattern.matcher(ComponentUtil.serialize(damagerCustomName)).find();
            case ATTACKER_PLAYER_NAME -> check[i] = damager instanceof Player && pattern.matcher(damager.getName()).find();
            case ATTACKER_PLAYER_DISPLAY_NAME -> check[i] = damager instanceof Player && pattern.matcher(ComponentUtil.serialize(((Player) damager).displayName())).find();
            case ATTACKER_PLAYER_PERMISSION -> check[i] = damager instanceof Player && damager.hasPermission(value);
            case WEAPON_TYPE -> check[i] = weaponExists && pattern.matcher(weapon.getType().toString()).find();
            case NBT -> check[i] = weaponExists && ItemStackUtil.predicateItem(weapon, value);
            case WEAPON_DISPLAY_NAME -> check[i] = weaponExists && pattern.matcher(ComponentUtil.serialize(ComponentUtil.itemName(weapon))).find();
            case LAST_TRAMPLED_BLOCK_TYPE -> {
              boolean success = false;
              if (Variable.lastTrampledBlockType.containsKey(victim.getUniqueId()))
              {
                success = Variable.lastTrampledBlockType.get(victim.getUniqueId()).toString().equals(value);
              }
              check[i] = success;
            }
          }
        }
      }
      catch (Exception ignored)
      {

      }
    }

    for (boolean b : check)
    {
      if (!b)
      {
        return false;
      }
    }
    return true;
  }
}

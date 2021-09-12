package com.jho5245.cucumbery.deathmessages;

import com.jho5245.cucumbery.util.Method;
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
    Component victimCustomName = victim.customName();
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
          if (conditionType == ConditionType.DEATH_TYPE)
          {
            if (value.startsWith("internal="))
            {
              value = value.split("internal=")[1];
              String[] yeet2 = value.split(";");
              boolean[] check2 = new boolean[yeet2.length];
              for (int j = 0; j < yeet2.length; j++)
              {
                String yeet = yeet2[i];
                String yeetKey = yeet.split(":")[0];
                if (yeetKey.equals("include"))
                {
                  String[] values = yeet.split(":")[1].split(",");
                  if (Method.contains(key, values))
                  {
                    check2[j] = true;
                  }
                }
                if (yeetKey.equals("exclude"))
                {
                  String[] values = yeet.split(":")[1].split(",");
                  if (!Method.contains(key, values))
                  {
                    check2[j] = true;
                  }
                }
                if (yeetKey.equals("regex"))
                {
                  check2[j] = Pattern.compile(yeet.split(":")[1]).matcher(key).find();
                }
              }
              check[i] = Method.allIsTrue(check2);
              continue;
            }
          }
          Pattern pattern = conditionType == ConditionType.NBT ? Pattern.compile("(.*)") : Pattern.compile("^" + value + "$");
          switch (conditionType)
          {
            case DEATH_TYPE -> check[i] = pattern.matcher(key).find();
            case ENTITY_UUID -> check[i] = pattern.matcher(victim.getUniqueId().toString()).find();
            case ENTITY_TYPE -> check[i] = pattern.matcher(victim.getType().toString()).find();
            case ENTITY_DISPLAY_NAME -> check[i] = victimCustomName != null && pattern.matcher(ComponentUtil.serialize(victimCustomName)).find();
            case PLAYER_NAME -> check[i] = victim instanceof Player && pattern.matcher(victim.getName()).find();
            case PLAYER_DISPLAY_NAME -> check[i] = victim instanceof Player && pattern.matcher(ComponentUtil.serialize(((Player) victim).displayName())).find();
            case PLAYER_PERMISSION -> check[i] = victim.hasPermission(value);
            case WORLD_NAME -> check[i] = pattern.matcher(victim.getLocation().getWorld().getName()).find();
            case BIOME_TYPE -> check[i] = pattern.matcher(victim.getLocation().getBlock().getBiome().toString()).find();
            case ATTACKER_ENTITY_TYPE -> check[i] = damagerIsLiving && pattern.matcher(damager.getName()).find();
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
                success = pattern.matcher(Variable.lastTrampledBlockType.get(victim.getUniqueId()).toString()).find();
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

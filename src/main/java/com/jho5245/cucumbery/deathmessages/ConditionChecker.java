package com.jho5245.cucumbery.deathmessages;

import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.data.Variable;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Nameable;
import org.bukkit.entity.Entity;
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
    String victimUUIDString = victim.getUniqueId().toString();
    String victimTypeString = victim.getType().toString();
    Location victimLocation = victim.getLocation();
    Object objectDamager = DeathManager.getDamager(event);
    boolean damagerExists = objectDamager != null;
    boolean damagerIsEntity = objectDamager instanceof Entity;
    boolean damagerIsLiving = objectDamager instanceof LivingEntity;
    LivingEntity livingDamager = damagerIsLiving ? (LivingEntity) objectDamager : null;
    Component damagerCustomName = objectDamager instanceof Nameable nameable ? nameable.customName() : null;
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
          if (value.startsWith("internal="))
          {
            value = value.split("internal=")[1];
            String[] yeet2 = value.split(";");
            boolean[] check2 = new boolean[yeet2.length];
            for (int j = 0; j < yeet2.length; j++)
            {
              String yeet = yeet2[j];
              String yeetKey = yeet.split(":")[0];
              String[] values = yeet.split(":")[1].split(",");
              if (yeetKey.equals("include"))
              {
                check2[j] = true;
                Outter:
                for (String v : values)
                {
                  switch (conditionType)
                  {
                    case DEATH_TYPE -> {
                      if (!key.contains(v))
                      {
                        check2[j] = false;
                        break Outter;
                      }
                    }
                    case ENTITY_UUID -> {
                      if (!victimUUIDString.contains(v))
                      {
                        check2[j] = false;
                        break Outter;
                      }
                    }
                    case ENTITY_TYPE -> {
                      if (!victimTypeString.contains(v))
                      {
                        check2[j] = false;
                        break Outter;
                      }
                    }
                  }
                }
              }
              if (yeetKey.equals("exclude"))
              {
                check2[j] = true;
                Outter:
                for (String v : values)
                {
                  switch (conditionType)
                  {
                    case DEATH_TYPE -> {
                      if (key.contains(v))
                      {
                        check2[j] = false;
                        break Outter;
                      }
                    }
                    case ENTITY_UUID -> {
                      if (victimUUIDString.contains(v))
                      {
                        check2[j] = false;
                        break Outter;
                      }
                    }
                    case ENTITY_TYPE -> {
                      if (victimTypeString.contains(v))
                      {
                        check2[j] = false;
                        break Outter;
                      }
                    }
                  }
                }
              }
              if (yeetKey.equals("regex"))
              {
                switch (conditionType)
                {
                  case DEATH_TYPE -> check2[j] = Pattern.compile(yeet.split(":")[1]).matcher(key).find();
                  case ENTITY_UUID -> check2[j] = Pattern.compile(yeet.split(":")[1]).matcher(victimUUIDString).find();
                  case ENTITY_TYPE -> check2[j] = Pattern.compile(yeet.split(":")[1]).matcher(victimTypeString).find();
                }
              }
            }
            check[i] = Method.allIsTrue(check2);
            continue;
          }
          Pattern pattern = conditionType == ConditionType.WEAPON_NBT ? Pattern.compile("(.*)") : Pattern.compile("^" + value + "$");
          switch (conditionType)
          {
            case DEATH_TYPE -> check[i] = pattern.matcher(key).find();
            case ENTITY_UUID -> check[i] = pattern.matcher(victimUUIDString).find();
            case ENTITY_TYPE -> check[i] = pattern.matcher(victimTypeString).find();
            case ENTITY_DISPLAY_NAME -> check[i] = victimCustomName != null && pattern.matcher(ComponentUtil.serialize(victimCustomName)).find();
            case PLAYER_NAME -> check[i] = victim instanceof Player player && pattern.matcher(player.getName()).find();
            case PLAYER_DISPLAY_NAME -> check[i] = victim instanceof Player && pattern.matcher(ComponentUtil.serialize(((Player) victim).displayName())).find();
            case PLAYER_PERMISSION -> check[i] = victim.hasPermission(value);
            case WORLD_NAME -> check[i] = pattern.matcher(victimLocation.getWorld().getName()).find();
            case BIOME_TYPE -> check[i] = pattern.matcher(victimLocation.getBlock().getBiome().toString()).find();
            case ATTACKER_ENTITY_TYPE -> check[i] = damagerIsEntity && pattern.matcher(((Entity) objectDamager).getType().toString()).find();
            case ATTACKER_ENTITY_DISPLAY_NAME -> check[i] = damagerCustomName != null && pattern.matcher(ComponentUtil.serialize(damagerCustomName)).find();
            case ATTACKER_PLAYER_NAME -> check[i] = livingDamager instanceof Player player && pattern.matcher(player.getName()).find();
            case ATTACKER_PLAYER_DISPLAY_NAME -> check[i] = livingDamager instanceof Player player && pattern.matcher(ComponentUtil.serialize(player.displayName())).find();
            case ATTACKER_PLAYER_PERMISSION -> check[i] = damagerIsEntity && ((Entity) objectDamager).hasPermission(value);
            case WEAPON_TYPE -> check[i] = weaponExists && pattern.matcher(weapon.getType().toString()).find();
            case WEAPON_NBT -> check[i] = weaponExists && ItemStackUtil.predicateItem(weapon, value);
            case WEAPON_DISPLAY_NAME -> check[i] = weaponExists && pattern.matcher(ComponentUtil.serialize(ItemNameUtil.itemName(weapon))).find();
            case LAST_TRAMPLED_BLOCK_TYPE -> {
              check[i] = Variable.lastTrampledBlockType.containsKey(victim.getUniqueId()) && pattern.matcher(Variable.lastTrampledBlockType.get(victim.getUniqueId()).toString()).find();
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

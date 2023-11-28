package com.jho5245.cucumbery.listeners.entity.no_groups;

import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTType;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

public class FireworkExplode implements Listener
{
  @EventHandler
  public void onFireworkExplode(FireworkExplodeEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Firework firework = event.getEntity();
    FireworkMeta fireworkMeta = firework.getFireworkMeta();
    Location location = firework.getLocation();
    ItemStack itemStack = firework.getItem();
    NBTItem nbtItem = new NBTItem(itemStack);
    int lifeTime = firework.getTicksFlown(), detonateTime = firework.getTicksToDetonate();
    CustomMaterial customMaterial = CustomMaterial.itemStackOf(itemStack);
    if (customMaterial != null)
    {
      switch (customMaterial)
      {
        case FIREWORK_ROCKET_EXPLOSIVE -> {
          location.getWorld().createExplosion(location, 4, false, false, firework);
          firework.remove();
        }
        case FIREWORK_ROCKET_EXPLOSIVE_DESTRUCTION -> {
          location.getWorld().createExplosion(location, 4, false, true, firework);
          firework.remove();
        }
        case FIREWORK_ROCKET_EXPLOSIVE_FLAME -> {
          location.getWorld().createExplosion(location, 4, true, false, firework);
          firework.remove();
        }
        case FIREWORK_ROCKET_CHAIN -> {
          if (firework.getScoreboardTags().contains("custom_material_firework_rocket_chain_clone"))
          {
            return;
          }
          event.setCancelled(true);
          firework.remove();
          int repeatCount = 3;
          if (nbtItem.hasTag("RepeatCount") && nbtItem.getType("RepeatCount") == NBTType.NBTTagInt)
          {
            repeatCount = nbtItem.getInteger("RepeatCount");
          }
          for (int i = 0; i < repeatCount; i++)
          {
            Firework f = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK, SpawnReason.CUSTOM);
            f.setFireworkMeta(fireworkMeta);
            f.setTicksToDetonate(Math.min(20, Math.max(5, lifeTime / 4)));
            f.setVelocity(firework.getVelocity().add(new Vector(Math.random(), Math.random(), Math.random())));
            f.getScoreboardTags().add("custom_material_firework_rocket_chain_clone");
            f.setShooter(firework.getShooter());
          }
        }
        case FIREWORK_ROCKET_REPEATING -> {
          if (firework.getTicksToDetonate() > 1)
          {
            Firework f = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK, SpawnReason.CUSTOM);
            f.setFireworkMeta(fireworkMeta);
            f.setTicksToDetonate(Math.min(10, detonateTime - 1));
            f.setVelocity(firework.getLocation().getDirection());
            f.setShooter(firework.getShooter());
          }
        }
      }
    }
  }
}

package com.jho5245.cucumbery.listeners.block;


import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.blockplacedata.BlockPlaceDataConfig;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.TNTPrimeEvent;
import org.bukkit.event.block.TNTPrimeEvent.PrimeCause;
import org.bukkit.util.Vector;

public class TNTPrime implements Listener
{
  @EventHandler
  public void onTNTPrime(TNTPrimeEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    this.staticTnt(event);
    Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
            BlockPlaceDataConfig.removeData(event.getBlock().getLocation()), 0L);
  }

  private void staticTnt(TNTPrimeEvent event)
  {
    if (Cucumbery.config.getBoolean("use-static-tnt"))
    {
      Block block = event.getBlock();
      if (Method.configContainsLocation(block.getLocation(), Cucumbery.config.getStringList("no-use-static-tnt-location")))
      {
        return;
      }
      PrimeCause primeReason = event.getCause();
      event.setCancelled(true);
      Entity entity = event.getPrimingEntity();
      block.setType(Material.AIR);
      TNTPrimed tnt = block.getLocation().getWorld().spawn(block.getLocation().add(0.5D, 0D, 0.5D), TNTPrimed.class);
      tnt.setVelocity(new Vector(Cucumbery.config.getDouble("static-tnt-velocity.x"), Cucumbery.config.getDouble("static-tnt-velocity.y"), Cucumbery.config.getDouble("static-tnt-velocity.z")));
      if (entity instanceof LivingEntity livingEntity)
      {
        tnt.setSource(livingEntity);
      }
      if (primeReason == PrimeCause.EXPLOSION)
      {
        tnt.setFuseTicks(Method.random(10, 30));
      }
      else
      {
        SoundPlay.playSoundLocation(event.getBlock().getLocation(), Sound.ENTITY_CREEPER_PRIMED);
        tnt.setFuseTicks(80);
      }
    }
  }
}

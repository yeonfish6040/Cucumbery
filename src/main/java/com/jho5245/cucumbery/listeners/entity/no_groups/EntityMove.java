package com.jho5245.cucumbery.listeners.entity.no_groups;

import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.deathmessages.DeathManager;
import com.jho5245.cucumbery.deathmessages.LastTrampledBlockManager;
import com.jho5245.cucumbery.events.entity.EntityLandOnGroundEvent;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EntityMove implements Listener
{
  @EventHandler
  public void onEntityMove(EntityMoveEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    this.customEffect(event);
    if (event.isCancelled())
    {
      return;
    }
    this.entityLandOnGround(event);
    this.getLastTrampledBlock(event);
  }

  private void customEffect(EntityMoveEvent event)
  {
    Entity entity = event.getEntity();
    if (CustomEffectManager.hasEffect(entity, CustomEffectType.STOP))
    {
      event.setCancelled(true);
    }
    if (CustomEffectManager.hasEffect(entity, CustomEffectType.CONFUSION))
    {
      Location from = event.getFrom(), to = event.getTo();
      float yaw = Math.min(360f, Math.max(-360f, 2 * from.getYaw() - to.getYaw()));
      float pitch = Math.min(90f, Math.max(-90f, 2 * from.getPitch() - to.getPitch()));
      event.setTo(new Location(to.getWorld(), to.getX(), to.getY(), to.getZ(), yaw, pitch));
    }
  }

  private void getLastTrampledBlock(EntityMoveEvent event)
  {
    // 아니 데스메시지 클래스 왜 자꾸 초기화 못함
    try
    {
      LivingEntity livingEntity = event.getEntity();
      if (DeathManager.deathMessageApplicable(livingEntity))
      {
        LastTrampledBlockManager.lastTrampledBlock(livingEntity, event.hasChangedBlock());
      }
    }
    catch (Throwable ignored)
    {

    }
  }

  private void entityLandOnGround(EntityMoveEvent event)
  {
    LivingEntity entity = event.getEntity();
    Location location = entity.getLocation();
    if (!entity.isSwimming() && (event.getFrom().getBlockY() > event.getTo().getBlockY()) && !location.getBlock().getType().isSolid() && location.add(0, -2, 0).getBlock().getType() != Material.AIR)
    {
      EntityLandOnGroundEvent landOnGroundEvent = new EntityLandOnGroundEvent(entity);
      Bukkit.getPluginManager().callEvent(landOnGroundEvent);
    }
  }
}

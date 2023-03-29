package com.jho5245.cucumbery.listeners.player.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.children.group.LocationCustomEffect;
import com.jho5245.cucumbery.custom.customeffect.children.group.LocationCustomEffectImple;
import com.jho5245.cucumbery.custom.customeffect.children.group.PlayerCustomEffect;
import com.jho5245.cucumbery.custom.customeffect.custom_mining.MiningScheduler;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeCustomMining;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeRune;
import com.jho5245.cucumbery.deathmessages.LastTrampledBlockManager;
import com.jho5245.cucumbery.events.entity.EntityLandOnGroundEvent;
import com.jho5245.cucumbery.util.blockplacedata.BlockPlaceDataConfig;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method2;
import com.jho5245.cucumbery.util.no_groups.Scheduler;
import com.jho5245.cucumbery.util.storage.data.*;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class PlayerMove implements Listener
{
  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event)
  {
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();
    if (!Permission.EVENT2_ANTI_ALLPLAYER.has(player) && event.hasChangedPosition() && Constant.AllPlayer.MOVE.isEnabled())
    {
      event.setCancelled(true);
      if (!Variable.playerMoveAlertCooldown.contains(uuid))
      {
        Variable.playerMoveAlertCooldown.add(uuid);
        MessageUtil.sendMessage(player, Prefix.INFO_ALLPLAYER, "움직일 수 없는 상태입니다");
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerMoveAlertCooldown.remove(uuid), 100L);
      }
      return;
    }
    if (event.hasChangedBlock() && CustomEffectManager.hasEffect(player, CustomEffectTypeRune.RUNE_USING))
    {
      Item item = null;
      List<Entity> entities = player.getNearbyEntities(5, 5, 5);
      for (Entity entity : entities)
      {
        if (entity instanceof Item i && CustomEffectManager.getEffectNullable(i, CustomEffectTypeRune.RUNE_OCCUPIED) instanceof PlayerCustomEffect playerCustomEffect && playerCustomEffect.getPlayer().equals(player))
        {
          item = i;
        }
      }
      if (item != null)
      {
        CustomEffectManager.removeEffect(item, CustomEffectTypeRune.RUNE_OCCUPIED);
      }
      CustomEffectManager.removeEffect(player, CustomEffectTypeRune.RUNE_USING);
      CustomEffectManager.addEffect(player, new CustomEffect(CustomEffectTypeRune.RUNE_COOLDOWN, 20 * 5));
      MessageUtil.sendTitle(player, "", "g255;움직여서 룬 해방이 취소되었습니다.", 0, 0, 150);
    }
    this.customEffect(event);
    if (event.isCancelled())
    {
      return;
    }
    this.customMaterial(event);
    if (Math.random() > 0.9999 && Permission.CMD_STASH.has(player) && Variable.itemStash.containsKey(uuid) && !Variable.itemStash.get(uuid).isEmpty())
    {
      MessageUtil.sendMessage(player, Prefix.INFO_STASH, "보관함에 아이템이 %s개 있습니다. %s 명령어로 확인하세요!", Variable.itemStash.get(uuid).size(), "rg255,204;/stash");
    }
    this.entityLandOnGround(event);
    this.getLastTrampledBlock(event);
    BlockPlaceDataConfig.onPlayerMove(event);
  }

  private void customMaterial(PlayerMoveEvent event)
  {
    Player player = event.getPlayer();
    EntityEquipment equipment = player.getEquipment();
    ItemStack chestplate = equipment.getChestplate();
    if (event.hasExplicitlyChangedBlock() && player.isGliding() && chestplate != null && chestplate.getType() == Material.ELYTRA && CustomMaterial.itemStackOf(chestplate) == CustomMaterial.ELYTRA_SHIVA_AMOODO_NAREUL_MAKEURLSOON_UPSOROAN)
    {
      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        World world = player.getWorld();
        Location location = event.getTo().clone().add(player.getEyeLocation().getDirection().multiply(2d));
        double radius = 3;
        for (int x = (int) (location.getBlockX() - radius); x <= location.getBlockX() + radius; x++)
        {
          for (int y = (int) (location.getBlockY() - radius); y <= location.getBlockY() + radius; y++)
          {
            for (int z = (int) (location.getBlockZ() - radius); z <= location.getBlockZ() + radius; z++)
            {
              Location newLocation = new Location(world, x, y, z);
              if (newLocation.distance(location) <= radius)
              {
                newLocation.getBlock().breakNaturally(player.getInventory().getItemInMainHand(), true, true);
              }
            }
          }
        }
      }, 0L);
    }
  }

  private void customEffect(PlayerMoveEvent event)
  {
    Player player = event.getPlayer();
    if (CustomEffectManager.hasEffect(player, CustomEffectType.STOP))
    {
      event.setCancelled(true);
    }
    if (event.hasChangedBlock() && CustomEffectManager.hasEffect(player, CustomEffectType.SPYGLASS_TELEPORT))
    {
      CustomEffectManager.removeEffect(player, CustomEffectType.SPYGLASS_TELEPORT);
      MessageUtil.sendWarn(player, "움직여서 순간 이동이 취소되었습니다");
      CustomEffectManager.addEffect(player, CustomEffectType.SPYGLASS_TELEPORT_COOLDOWN, 100);
    }
    if (CustomEffectManager.hasEffect(player, CustomEffectType.CONFUSION))
    {
      Location from = event.getFrom(), to = event.getTo();
      float yaw = Math.min(360f, Math.max(-360f, 2 * from.getYaw() - to.getYaw()));
      float pitch = Math.min(90f, Math.max(-90f, 2 * from.getPitch() - to.getPitch()));
      event.setTo(new Location(to.getWorld(), to.getX(), to.getY(), to.getZ(), yaw, pitch));
    }
    if (event.hasChangedPosition() && CustomEffectManager.hasEffect(player, CustomEffectType.SHIVA_NO_ONE_CAN_BLOCK_ME))
    {
      Method2.getNearbyEntitiesAsync(player.getLocation(), 1.5d).forEach(entity ->
      {
        if (entity instanceof LivingEntity livingEntity && entity != player)
        {
          if (!(livingEntity instanceof Player p && UserData.GOD_MODE.getBoolean(p)))
          {
            livingEntity.setHealth(0);
          }
        }
      });
    }
    if (event.hasExplicitlyChangedPosition() && CustomEffectManager.hasEffect(player, CustomEffectType.VAR_PODAGRA) && !player.isSneaking())
    {
      if (!CustomEffectManager.hasEffect(player, CustomEffectType.VAR_PODAGRA_ACTIVATED))
      {
        player.damage(1);
        player.setNoDamageTicks(0);
      }
      // just for some case
      try
      {
        CustomEffectManager.addEffect(player, new CustomEffect(CustomEffectType.VAR_PODAGRA_ACTIVATED, CustomEffectType.VAR_PODAGRA_ACTIVATED.getDefaultDuration(), CustomEffectManager.getEffect(player, CustomEffectType.VAR_PODAGRA).getAmplifier()));
      }
      catch (IllegalStateException ignored)
      {

      }
    }
    if (event.hasChangedBlock() && CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
    {
      Location playerLocation = player.getLocation();
      CustomEffect customEffect = CustomEffectManager.getEffectNullable(player, CustomEffectTypeCustomMining.MOVEMENT_CHECK);
      if (customEffect instanceof LocationCustomEffect locationCustomEffect)
      {
        Location location = locationCustomEffect.getLocation();
        if (!playerLocation.getWorld().getName().equals(location.getWorld().getName()) || playerLocation.distance(location) > Cucumbery.config.getDouble("custom-mining.maximum-block-packet-distance") / 2d)
        {
          Scheduler.fakeBlocksAsync(player, true);
          MiningScheduler.customMining(player, true);
          CustomEffectManager.addEffect(player, new LocationCustomEffectImple(CustomEffectTypeCustomMining.MOVEMENT_CHECK, playerLocation));
        }
      }
      else
      {
        CustomEffectManager.addEffect(player, new LocationCustomEffectImple(CustomEffectTypeCustomMining.MOVEMENT_CHECK, playerLocation));
      }
    }

  }

  private void getLastTrampledBlock(PlayerMoveEvent event)
  {
    LivingEntity livingEntity = event.getPlayer();
    LastTrampledBlockManager.lastTrampledBlock(livingEntity, event.hasChangedBlock());
  }

  private void entityLandOnGround(PlayerMoveEvent event)
  {
    LivingEntity entity = event.getPlayer();
    Location location = entity.getLocation();
    if (!entity.isSwimming() && (event.getFrom().getBlockY() > event.getTo().getBlockY()) && !location.getBlock().getType().isSolid() && location.add(0, -2, 0).getBlock().getType() != Material.AIR)
    {
      EntityLandOnGroundEvent landOnGroundEvent = new EntityLandOnGroundEvent(entity);
      Bukkit.getPluginManager().callEvent(landOnGroundEvent);
    }
  }
}

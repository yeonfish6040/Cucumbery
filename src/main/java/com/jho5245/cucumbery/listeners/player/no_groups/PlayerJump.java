package com.jho5245.cucumbery.listeners.player.no_groups;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectType;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.util.Consumer;

public class PlayerJump implements Listener
{
  @EventHandler
  public void onPlayerJump(PlayerJumpEvent event)
  {
    Player player = event.getPlayer();
    if (CustomEffectManager.hasEffect(player, CustomEffectType.CURSE_OF_JUMPING))
    {
      event.setCancelled(true);
    }
    if (CustomEffectManager.hasEffect(player, CustomEffectType.VAR_PODAGRA) && !player.isSneaking())
    {
      player.damage(1);
      player.setNoDamageTicks(0);
    }
    if (CustomEffectManager.hasEffect(player, CustomEffectType.STAR_CATCH_PROCESS))
    {
      event.setCancelled(true);
      CustomEffect customEffect = CustomEffectManager.getEffect(player, CustomEffectType.STAR_CATCH_PROCESS);
      int duration = customEffect.getDuration();
      boolean ok = false;
      Integer penalty = Variable.starCatchPenalty.get(player.getUniqueId());
      if (penalty == null)
      {
        penalty = 0;
      }
      int level = Math.min(4, penalty / 20);
      Outter: for (int i = 1; i <= 10; i++)
      {
        switch (level)
        {
          case 0 -> {
            if (duration >= i * 20 - 14 && duration <= i * 20 - 6)
            {
              ok = true;
              break Outter;
            }
          }
          case 1 -> {
            if (duration >= i * 20 - 13 && duration <= i * 20 - 7)
            {
              ok = true;
              break Outter;
            }
          }
          case 2 -> {
            if (duration >= i * 20 - 11 && duration <= i * 20 - 8)
            {
              ok = true;
              break Outter;
            }
          }
          case 3 -> {
            if (duration >= i * 20 - 10 && duration <= i * 20 - 9)
            {
              ok = true;
              break Outter;
            }
          }
          default -> {
            if (duration == i * 20 - 10)
            {
              ok = true;
              break Outter;
            }
          }
        }
      }
      if (ok)
      {
        CustomEffectManager.addEffect(player, CustomEffectType.STAR_CATCH_SUCCESS);
        player.playSound(player.getLocation(), "star_catch_success", SoundCategory.PLAYERS, 2F, 1F);
        player.playSound(player.getLocation(), "star_catch_success", SoundCategory.PLAYERS, 2F, 1F);
        Consumer<Entity> consumer = e ->
        {
          ArmorStand armorStand = (ArmorStand) e;
          armorStand.setMarker(true);
          armorStand.setSmall(true);
          armorStand.setBasePlate(false);
          armorStand.setInvisible(true);
          armorStand.customName(ComponentUtil.translate("&a+강화성공률"));
          armorStand.setCustomNameVisible(true);
          armorStand.addScoreboardTag("damage_indicator");
          armorStand.addScoreboardTag("no_cucumbery_true_invisibility");
          for (Player p : Bukkit.getOnlinePlayers())
          {
            if (player != p)
            {
              p.hideEntity(Cucumbery.getPlugin(), armorStand);
            }
          }
        };
        Location location = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(3));
        Entity armorStand = location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND, SpawnReason.DEFAULT, consumer);
        CustomEffectManager.addEffect(armorStand, CustomEffectType.DAMAGE_INDICATOR);
      }
      else
      {
        player.playSound(player.getLocation(), "star_catch_failure", SoundCategory.PLAYERS, 1F, 1F);
      }
      CustomEffectManager.removeEffect(player, CustomEffectType.STAR_CATCH_PROCESS);
    }
  }
}

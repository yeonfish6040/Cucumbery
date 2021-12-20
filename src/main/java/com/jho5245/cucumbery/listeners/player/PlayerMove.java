package com.jho5245.cucumbery.listeners.player;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.customeffect.CustomEffectType;
import com.jho5245.cucumbery.deathmessages.LastTrampledBlockManager;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.storage.SoundPlay;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

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
        MessageUtil.sendMessage(player, Prefix.INFO_ALLPLAYER, "움직일 수 없는 상태입니다.");
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerMoveAlertCooldown.remove(uuid), 100L);
      }
      return;
    }

    this.customEffect(event);
    this.getLastTrampledBlock(event);
  }

  private void customEffect(PlayerMoveEvent event)
  {
    Player player = event.getPlayer();
    if (CustomEffectManager.hasEffect(player, CustomEffectType.STOP))
    {
      event.setCancelled(true);
    }
    if (CustomEffectManager.hasEffect(player, CustomEffectType.CONFUSION))
    {
      Location from = event.getFrom(), to = event.getTo();
      float yaw = Math.min(360f, Math.max(-360f, 2 * from.getYaw() - to.getYaw()));
      float pitch = Math.min(90f, Math.max(-90f, 2 * from.getPitch() - to.getPitch()));
      event.setTo(new Location(to.getWorld(), to.getX(), to.getY(), to.getZ(), yaw, pitch));
    }
  }

  private void getLastTrampledBlock(PlayerMoveEvent event)
  {
    LivingEntity livingEntity = event.getPlayer();
    LastTrampledBlockManager.lastTrampledBlock(livingEntity, event.hasChangedBlock());
  }
}

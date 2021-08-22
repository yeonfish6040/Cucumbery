package com.jho5245.cucumbery.listeners.player;

import com.jho5245.cucumbery.Cucumbery;
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
    Location from = event.getFrom(), to = event.getTo();
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

    this.getLastTrampledBlock(event);
  }

  private void getLastTrampledBlock(PlayerMoveEvent event)
  {
    LivingEntity livingEntity = event.getPlayer();
    LastTrampledBlockManager.lastTrampledBlock(livingEntity, event.hasChangedBlock());
  }
}

package com.jho5245.cucumbery.listeners.player.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.custom_mining.MiningManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectRemoveEvent.RemoveReason;
import com.jho5245.cucumbery.util.blockplacedata.BlockPlaceDataConfig;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.sendercomponent.SenderComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class PlayerQuit implements Listener
{
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event)
  {
    Player player = event.getPlayer();
    boolean isSpectator = UserData.SPECTATOR_MODE.getBoolean(player);
    String name = player.getName();
    FileConfiguration cfg = Cucumbery.config;
    Component displayName = SenderComponentUtil.senderComponent(player);
    if (Cucumbery.using_ProtocolLib)
    {
      BlockPlaceDataConfig.CHUNK_MAP.clear();
    }
    Location location = player.getLocation();
    List<String> noTellrawWorlds = cfg.getStringList("no-tellraw-feature-on-quit-worlds"), noActionbarWorlds = cfg.getStringList("no-actionbar-feature-on-quit-worlds");
    boolean enabledTellraw = cfg.getBoolean("use-tellraw-feature-on-quit") && !Method.configContainsLocation(location, noTellrawWorlds);
    boolean enabeldActionbar = cfg.getBoolean("use-actionbar-feature-on-quit") && !Method.configContainsLocation(location, noActionbarWorlds);
    String uuid = player.getUniqueId().toString();
    if (enabeldActionbar || enabledTellraw)
    {
      event.quitMessage(null);
      MessageUtil.consoleSendMessage(Prefix.INFO_QUIT, "%s - %s(%s)", Constant.THE_COLOR_HEX + uuid, Constant.THE_COLOR_HEX + name, displayName);
    }
    if (enabeldActionbar && !isSpectator)
    {
      String quitMessageActionbar = cfg.getString("actionbar-quit-message", "%player%이(가) 퇴장하셨습니다").replace("%player%", "%s");
      for (Player online : Bukkit.getServer().getOnlinePlayers())
      {
        if (online != player)
        {
          MessageUtil.sendActionBar(online, quitMessageActionbar, displayName);
        }
      }
    }

    boolean outsider = false;
    if (CustomEffectManager.hasEffect(player, CustomEffectType.OUTSIDER))
    {
      int amplifier = CustomEffectManager.getEffect(player, CustomEffectType.OUTSIDER).getAmplifier() + 1;
      if (Math.random() * 100 < amplifier * 10)
      {
        outsider = true;
      }
    }

    String quitMessage = switch (event.getReason())
            {
              default -> "multiplayer.player.left";
              case KICKED -> player.isBanned() ? "%s이(가) 서버에서 정지당했습니다" : "%s이(가) 서버에서 강퇴당했습니다";
              case TIMED_OUT -> "%s이(가) 시간이 초과되어 서버에서 강퇴당했습니다";
              case ERRONEOUS_STATE -> "%s이(가) 오류나서 터졌습니다";
            };

    if (enabledTellraw && !isSpectator && !outsider)
    {
      for (Player online : Bukkit.getServer().getOnlinePlayers())
      {
        if (online != player)
        {
          if (UserData.SHOW_QUIT_MESSAGE_FORCE.getBoolean(player.getUniqueId()) || (UserData.SHOW_QUIT_MESSAGE.getBoolean(player.getUniqueId()) && UserData.OUTPUT_QUIT_MESSAGE.getBoolean(
                  online.getUniqueId()) || UserData.OUTPUT_QUIT_MESSAGE_FORCE.getBoolean(online.getUniqueId())))
          {
            MessageUtil.sendMessage(online, Prefix.INFO_QUIT, quitMessage, player);
          }
        }
      }
    }

    if (!isSpectator && cfg.getBoolean("play-quit-sound") && !Method.configContainsLocation(location, cfg.getStringList("no-play-quit-sound-worlds")))
    {
      Sound sound;
      try
      {
        sound = Sound.valueOf(cfg.getString("play-quit-sounds.type"));
      }
      catch (Exception e)
      {
        sound = Sound.BLOCK_NOTE_BLOCK_BASS;
      }
      float volume = (float) cfg.getDouble("play-quit-sounds.volume"), pitch = (float) cfg.getDouble("play-quit-sounds.pitch");
      for (Player online : Bukkit.getServer().getOnlinePlayers())
      {
        if (online != player)
        {
          if ((UserData.PLAY_QUIT_FORCE.getBoolean(player.getUniqueId()) || UserData.PLAY_QUIT.getBoolean(player.getUniqueId())) && (UserData.LISTEN_QUIT.getBoolean(online.getUniqueId()) ||
                  UserData.LISTEN_QUIT_FORCE.getBoolean(online.getUniqueId())))
          {
            SoundPlay.playSound(online, sound, volume, pitch);
          }
        }
      }
    }
    MiningManager.quitCustomMining(player);
    List<CustomEffect> customEffects = CustomEffectManager.getEffects(player);
    for (CustomEffect customEffect : customEffects)
    {
      if (!customEffect.isKeepOnQuit())
      {
        CustomEffectManager.removeEffect(player, customEffect.getType(), RemoveReason.QUIT);
      }
    }
  }
}

package com.jho5245.cucumbery.listeners.player;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.customeffect.CustomEffect;
import com.jho5245.cucumbery.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.customeffect.CustomEffectType;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.SoundPlay;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.sendercomponent.SenderComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Prefix;
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
import java.util.Objects;

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
    Location location = player.getLocation();
    List<String> noTellrawWorlds = cfg.getStringList("no-tellraw-feature-on-quit-worlds"), noActionbarWorlds = cfg.getStringList("no-actionbar-feature-on-quit-worlds");
    boolean enabledTellraw = cfg.getBoolean("use-tellraw-feature-on-quit") && !Method.configContainsLocation(location, noTellrawWorlds);
    boolean enabeldActionbar = cfg.getBoolean("use-actionbar-feature-on-quit") && !Method.configContainsLocation(location, noActionbarWorlds);
    String uuid = player.getUniqueId().toString();
    if (enabeldActionbar || enabledTellraw)
    {
      event.quitMessage(null);
      MessageUtil.consoleSendMessage("&5[&c퇴장&5] &rUUID : &e" + uuid + "&r, ID : &e" + name + "&r, Nickname : ", displayName);
    }
    if (enabeldActionbar && !isSpectator)
    {
      String quitMessageActionbar = MessageUtil.n2s(Objects.requireNonNull(cfg.getString("actionbar-quit-message")).replace("%player%", ComponentUtil.serialize(displayName)));
      for (Player online : Bukkit.getServer().getOnlinePlayers())
      {
        if (online != player)
        {
          MessageUtil.sendActionBar(online, quitMessageActionbar);
        }
      }
    }

    boolean outsider = false;
    if (CustomEffectManager.hasEffect(player, CustomEffectType.OUTSIDER))
    {
      @SuppressWarnings("all")
      int amplifier = CustomEffectManager.getEffect(player, CustomEffectType.OUTSIDER).getAmplifier() + 1;
      if (Math.random() * 100 < amplifier * 10)
      {
        outsider = true;
      }
    }

    String quitMessage = "%s이(가) 퇴장하셨습니다.";
    switch (event.getReason())
    {
      case DISCONNECTED -> quitMessage = "%s이(가) 퇴장하셨습니다.";
      case KICKED -> quitMessage = player.isBanned() ? "%s이(가) 서버에서 정지당했습니다." : "%s이(가) 서버에서 강퇴당했습니다.";
      case TIMED_OUT -> quitMessage = "%s이(가) 시간이 초과되어 서버에서 강퇴당했습니다.";
      case ERRONEOUS_STATE -> quitMessage = "%s이(가) 오류나서 터졌습니다.";
    }

    if (enabledTellraw && !isSpectator && !outsider)
    {
      for (Player online : Bukkit.getServer().getOnlinePlayers())
      {
        if (online != player)
        {
          if (UserData.SHOW_QUIT_MESSAGE_FORCE.getBoolean(player.getUniqueId()) || (UserData.SHOW_QUIT_MESSAGE.getBoolean(player.getUniqueId()) && UserData.OUTPUT_QUIT_MESSAGE.getBoolean(
                  online.getUniqueId()) || UserData.OUTPUT_QUIT_MESSAGE_FORCE.getBoolean(online.getUniqueId())))
          {
            MessageUtil.sendMessage(online, Prefix.INFO_QUIT, ComponentUtil.translate(quitMessage, player));
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


    List<CustomEffect> customEffects = CustomEffectManager.getEffects(player);
    customEffects.removeIf(customEffect -> !customEffect.isKeepOnQuit());
    CustomEffectManager.clearEffects(player);
    CustomEffectManager.addEffects(player, customEffects);
  }
}

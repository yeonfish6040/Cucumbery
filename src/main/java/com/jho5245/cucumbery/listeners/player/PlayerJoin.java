package com.jho5245.cucumbery.listeners.player;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.Initializer;
import com.jho5245.cucumbery.commands.sound.CommandSong;
import com.jho5245.cucumbery.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.customeffect.CustomEffectType;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.SoundPlay;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.sendercomponent.SenderComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PlayerJoin implements Listener
{
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event)
  {
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();
    if (!Variable.userData.containsKey(uuid))
    {
      //			Method.broadcastDebug("캐시 유저 데이터 생성 - " + uuid.toString());
      Initializer.loadPlayerConfig(player);
      //			Method.broadcastDebug("데이터 생성 완료, 현재 캐시 유저 데이터 개수 : §e" + Variable.userData.size() + "개");
    }
    if (CommandSong.playerRadio.containsKey(uuid))
    {
      if (UserData.LISTEN_GLOBAL.getBoolean(uuid) || UserData.LISTEN_GLOBAL_FORCE.getBoolean(uuid))
      {
        CommandSong.playerRadio.get(uuid).addPlayer(player);
      }
      else
      {
        CommandSong.playerRadio.get(uuid).removePlayer(player);
      }
    }
    else if (CommandSong.radioSongPlayer != null)
    {
      if (UserData.LISTEN_GLOBAL.getBoolean(uuid) || UserData.LISTEN_GLOBAL_FORCE.getBoolean(uuid))
      {
        CommandSong.radioSongPlayer.addPlayer(player);
      }
      else
      {
        CommandSong.radioSongPlayer.removePlayer(player);
      }
    }
    String name = player.getName();
    boolean healthScaled = UserData.HEALTH_SCALED.getBoolean(uuid);
    if (healthScaled)
    {
      player.setHealthScaled(false);
    }
    else
    {
      double healthbar = UserData.HEALTH_BAR.getDouble(uuid);
      if (healthbar <= 0D)
      {
        healthbar = 20D;
      }
      player.setHealthScale(healthbar);
    }
    YamlConfiguration cfg = Cucumbery.config;
    String displayname = null;
    String playerListName = null;
    if (cfg.getBoolean("use-nickname-feature"))
    {
      displayname = UserData.DISPLAY_NAME.getString(uuid);
      playerListName = UserData.PLAYER_LIST_NAME.getString(uuid);
    }
    Component senderComponent = SenderComponentUtil.senderComponent(player);
    if (displayname == null)
    {
      displayname = player.getName();
    }
    Component finalDislay = ComponentUtil.create(displayname).hoverEvent(senderComponent.hoverEvent()).clickEvent(senderComponent.clickEvent());
    player.displayName(finalDislay);
    if (playerListName == null)
    {
      playerListName = player.getName();
    }
    finalDislay = ComponentUtil.create(playerListName).hoverEvent(senderComponent.hoverEvent()).clickEvent(senderComponent.clickEvent());
    if (Cucumbery.using_Vault_Chat)
    {
      try
      {
        String prefix = Cucumbery.chat.getPlayerPrefix(player), suffix = Cucumbery.chat.getPlayerSuffix(player);
        if (prefix != null)
        {
          finalDislay = ComponentUtil.create(false, prefix, finalDislay);
        }
        if (suffix != null)
        {
          finalDislay = ComponentUtil.create(false, finalDislay, suffix);
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    player.playerListName(finalDislay);
    Component displayName = SenderComponentUtil.senderComponent(player);
    String displayNameString = ComponentUtil.serialize(displayName);
    boolean isSpectator = UserData.SPECTATOR_MODE.getBoolean(player);
    if (isSpectator && UserData.SPECTATOR_MODE_ON_JOIN.getBoolean(player))
    {
      if (player.getGameMode() != GameMode.SPECTATOR)
      {
        player.setGameMode(GameMode.SPECTATOR);
        MessageUtil.info(player, ComponentUtil.createTranslate("관전자여서 게임 모드가 자동으로 관전 모드로 전환되었습니다."));
      }
    }
    Location location = player.getLocation();
    List<String> noTellrawWorlds = cfg.getStringList("no-tellraw-feature-on-join-worlds"), noActionbarWorlds = cfg.getStringList("no-actionbar-feature-on-join-worlds");
    boolean enabledTellraw = cfg.getBoolean("use-tellraw-feature-on-join") && !Method.configContainsLocation(location, noTellrawWorlds);
    boolean enabeldActionbar = cfg.getBoolean("use-actionbar-feature-on-join") && !Method.configContainsLocation(location, noActionbarWorlds);
    if (enabeldActionbar || enabledTellraw)
    {
      event.joinMessage(null);
      MessageUtil.consoleSendMessage("&2[&a입장&2] &rUUID : &e" + uuid + "&r, ID : &e" + name + "&r, Nickname : &e", displayName);
    }
    if (enabeldActionbar && !isSpectator)
    {
      String joinMessageActionbar = MessageUtil.n2s(Objects.requireNonNull(cfg.getString("actionbar-join-message")).replace("%player%", displayNameString));
      for (Player online : Bukkit.getServer().getOnlinePlayers())
      {
        if (online != player)
        {
          MessageUtil.sendActionBar(online, joinMessageActionbar);
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
    if (enabledTellraw && !isSpectator && !outsider)
    {

      for (Player online : Bukkit.getServer().getOnlinePlayers())
      {
        if (cfg.getBoolean("show-tellraw-to-join-player") || online != player)
        {
          if ((UserData.SHOW_JOIN_MESSAGE_FORCE.getBoolean(uuid) || UserData.SHOW_JOIN_MESSAGE.getBoolean(uuid)) && (UserData.OUTPUT_JOIN_MESSAGE.getBoolean(online.getUniqueId()) ||
                  UserData.OUTPUT_JOIN_MESSAGE_FORCE.getBoolean(online.getUniqueId())))
          {
            MessageUtil.sendMessage(online, Prefix.INFO_JOIN, ComponentUtil.createTranslate("%s이(가) 입장하셨습니다.", player));
            if (CustomEffectManager.hasEffect(player, CustomEffectType.CURSE_OF_BEANS))
            {
              MessageUtil.sendMessage(online, Prefix.INFO_JOIN, ComponentUtil.createTranslate("%s이(가) 입장하셨습니다.", player));
            }
          }
        }
      }
    }
    if (!isSpectator && cfg.getBoolean("play-join-sound") && !Method.configContainsLocation(location, cfg.getStringList("no-play-join-sound-worlds")))
    {
      Sound sound;
      try
      {
        sound = Sound.valueOf(cfg.getString("play-join-sounds.type"));
      }
      catch (Exception e)
      {
        sound = Sound.BLOCK_NOTE_BLOCK_PLING;
      }
      final Sound finalSound = sound;
      float volume = (float) cfg.getDouble("play-join-sounds.volume"), pitch = (float) cfg.getDouble("play-join-sounds.pitch");
      for (Player online : Bukkit.getServer().getOnlinePlayers())
      {
        if (cfg.getBoolean("play-join-sound-to-join-player") || online != player)
        {
          if ((UserData.PLAY_JOIN_FORCE.getBoolean(uuid) || UserData.PLAY_JOIN.getBoolean(uuid)) && (UserData.LISTEN_JOIN.getBoolean(online.getUniqueId()) || UserData.LISTEN_JOIN_FORCE.getBoolean(
                  online.getUniqueId())))
          {
            Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> SoundPlay.playSound(online, finalSound, volume, pitch), 0L);
          }
        }
      }
    }
    if (UserData.SHOW_JOIN_TITLE.getBoolean(uuid))
    {
      List<String> titleWorld = cfg.getStringList("no-title-on-join-worlds");
      boolean showTitle = cfg.getBoolean("show-title-on-join") && !Method.configContainsLocation(location, titleWorld);
      if (showTitle)
      {
        String title = MessageUtil.n2s(Objects.requireNonNull(cfg.getString("title-on-join.title")).replace("%player%", displayNameString));
        String subtitle = MessageUtil.n2s(Objects.requireNonNull(cfg.getString("title-on-join.subtitle")).replace("%player%", displayNameString));
        int delay = cfg.getInt("title-delay-in-tick");
        int fadeIn = cfg.getInt("title-on-join.fade-in");
        int stay = cfg.getInt("title-on-join.stay");
        int fadeOut = cfg.getInt("title-on-join.fade-out");
        if (delay > 0)
        {
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> MessageUtil.sendTitle(player, title, subtitle, fadeIn, stay, fadeOut), delay);
        }
        else
        {
          MessageUtil.sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
        }
      }
    }

    // 서버에 접속할 때 인벤토리에 있는 아이템 설명 업데이트
    Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Method.updateInventory(player), 0L);
  }
}

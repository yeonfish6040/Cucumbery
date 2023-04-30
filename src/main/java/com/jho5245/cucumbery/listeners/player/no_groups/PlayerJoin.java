package com.jho5245.cucumbery.listeners.player.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.Initializer;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.custom_mining.MiningManager;
import com.jho5245.cucumbery.custom.customeffect.custom_mining.MiningScheduler;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeCustomMining;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectRemoveEvent.RemoveReason;
import com.jho5245.cucumbery.util.blockplacedata.BlockPlaceDataConfig;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.Scheduler;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.sendercomponent.SenderComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.UUID;

public class PlayerJoin implements Listener
{
  @EventHandler(priority = EventPriority.HIGHEST)
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
    if (Cucumbery.using_ProtocolLib)
    {
      BlockPlaceDataConfig.display(player, player.getLocation());
    }
    Initializer.setNickName(player);
    String name = player.getName();
    YamlConfiguration cfg = Cucumbery.config;
    Location location = player.getLocation();
    List<String> noTellrawWorlds = cfg.getStringList("no-tellraw-feature-on-join-worlds"), noActionbarWorlds = cfg.getStringList("no-actionbar-feature-on-join-worlds");
    boolean enabledTellraw = cfg.getBoolean("use-tellraw-feature-on-join") && !Method.configContainsLocation(location, noTellrawWorlds);
    boolean enabeldActionbar = cfg.getBoolean("use-actionbar-feature-on-join") && !Method.configContainsLocation(location, noActionbarWorlds);
    boolean isSpectator = UserData.SPECTATOR_MODE.getBoolean(player);
    Component displayName = SenderComponentUtil.senderComponent(player);
    if (enabeldActionbar || enabledTellraw)
    {
      event.joinMessage(null);
      MessageUtil.consoleSendMessage(Prefix.INFO_JOIN, "%s - %s(%s)", Constant.THE_COLOR_HEX + uuid, Constant.THE_COLOR_HEX + name, displayName);
    }
    if (enabeldActionbar && !isSpectator)
    {
      String joinMessageActionbar = cfg.getString("actionbar-join-message", "%player%이(가) 입장하셨습니다").replace("%player%", "%s");
      for (Player online : Bukkit.getServer().getOnlinePlayers())
      {
        if (online != player)
        {
          MessageUtil.sendActionBar(online, joinMessageActionbar, displayName);
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
    CustomEffectManager.removeEffect(player, CustomEffectType.INVINCIBLE_PLUGIN_RELOAD);
    CustomEffectManager.refreshAttributeEffects(player);
    if (enabledTellraw && !isSpectator && !outsider)
    {
      for (Player online : Bukkit.getServer().getOnlinePlayers())
      {
        if (cfg.getBoolean("show-tellraw-to-join-player") || online != player)
        {
          if ((UserData.SHOW_JOIN_MESSAGE_FORCE.getBoolean(uuid) || UserData.SHOW_JOIN_MESSAGE.getBoolean(uuid)) && (UserData.OUTPUT_JOIN_MESSAGE.getBoolean(online.getUniqueId()) ||
                  UserData.OUTPUT_JOIN_MESSAGE_FORCE.getBoolean(online.getUniqueId())))
          {
            MessageUtil.sendMessage(online, Prefix.INFO_JOIN, "multiplayer.player.joined", player);
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
        Component title = ComponentUtil.translate(cfg.getString("title-on-join.title", "환영합니다"), player);
        Component subtitle = ComponentUtil.translate(cfg.getString("title-on-join.subtitle", "%s님, 즐거운 시간 보내세요"), player);
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
    Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> ItemStackUtil.updateInventory(player), 0L);

    for (CustomEffect customEffect : CustomEffectManager.getEffects(player))
    {
      if (!customEffect.isKeepOnQuit())
      {
        CustomEffectManager.removeEffect(player, customEffect.getType(), RemoveReason.QUIT);
      }
    }

    if (Permission.CMD_STASH.has(player) && Variable.itemStash.containsKey(uuid) && !Variable.itemStash.get(uuid).isEmpty())
    {
      MessageUtil.sendMessage(player, Prefix.INFO_STASH, "보관함에 아이템이 %s개 있습니다. %s 명령어로 확인하세요!", Variable.itemStash.get(uuid).size(), "rg255,204;/stash");
    }
    if (CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
    {
      MiningManager.quitCustomMining(player);
      for (int i = 0; i < 10; i++)
      {
        Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
        {
          Scheduler.fakeBlocksAsync(player, true);
          MiningScheduler.customMining(player, true);
        }, i * 20);
      }
    }
  }
}

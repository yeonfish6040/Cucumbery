package com.jho5245.cucumbery.listeners.player;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.customeffect.CustomEffectType;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.PlaceHolderUtil;
import com.jho5245.cucumbery.util.storage.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.SoundPlay;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.sendercomponent.SenderComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Objects;
import java.util.UUID;

public class PlayerChat implements Listener
{
  @EventHandler
  public void onPlayerChat(AsyncChatEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();
    String message = ComponentUtil.serialize(event.message());
    if (message.startsWith("\\cmd "))
    {
      try
      {
        String cmd = message.substring(5);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> player.performCommand(cmd), 0L);
        if (UserData.LISTEN_COMMAND.getBoolean(player.getUniqueId()))
        {
          SoundPlay.playSound(player, Sound.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS);
        }
        event.setCancelled(true);
        return;
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    if (event.isCancelled())
    {
      return;
    }
    switch (message.toLowerCase())
    {
      case Constant.REINFORCE_QUIT:
        event.setCancelled(true);
        Bukkit.getScheduler().callSyncMethod(Cucumbery.getPlugin(), () -> Bukkit.getServer().dispatchCommand(player, "강화 quit"));
        return;
      case Constant.REINFORCE_USE_ANTI_DESTRUCTION:
        event.setCancelled(true);
        Bukkit.getScheduler().callSyncMethod(Cucumbery.getPlugin(), () -> Bukkit.getServer().dispatchCommand(player, "강화 파괴방지사용"));
        return;
      case Constant.REINFORCE_DO_NOT_USE_ANTI_DESTRUCTION:
        event.setCancelled(true);
        Bukkit.getScheduler().callSyncMethod(Cucumbery.getPlugin(), () -> Bukkit.getServer().dispatchCommand(player, "강화 파괴방지미사용"));
        return;
      case Constant.REINFORCE_START:
        event.setCancelled(true);
        Bukkit.getScheduler().callSyncMethod(Cucumbery.getPlugin(), () -> Bukkit.getServer().dispatchCommand(player, "강화 realstart"));
        return;
      default:
        break;
    }
    if (!Cucumbery.config.getBoolean("grant-default-permission-to-players") && !Permission.EVENT_CHAT.has(player))
    {
      event.setCancelled(true);
      if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerChatAlertCooldown.contains(uuid))
      {
        Variable.playerChatAlertCooldown.add(uuid);
        MessageUtil.sendTitle(player, "&c채팅 불가!", "&r채팅을 칠 권한이 없습니다.", 5, 80, 15);
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerChatAlertCooldown.remove(uuid), 100L);
      }
      return;
    }
    if (!Permission.EVENT2_ANTI_ALLPLAYER.has(player) && Constant.AllPlayer.CHAT.isEnabled())
    {
      event.setCancelled(true);
      if (!Variable.playerChatAlertCooldown.contains(uuid))
      {
        Variable.playerChatAlertCooldown.add(uuid);
        MessageUtil.sendMessage(player, Prefix.INFO_ALLPLAYER, "채팅을 칠 수 없는 상태입니다.");
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerChatAlertCooldown.remove(uuid), 100L);
      }
      return;
    }
    Location location = player.getLocation();
    if (!Permission.EVENT_CHAT_SPAM.has(player) && Cucumbery.config.getBoolean("no-spam.enable") && !Method.configContainsLocation(location, Cucumbery.config.getStringList("no-spam.no-worlds")))
    {
      int chatCooldown = Cucumbery.config.getInt("no-spam.cooldown-in-ticks");
      if (Variable.playerChatNoSpamAlertCooldown.contains(uuid))
      {
        event.setCancelled(true);
        MessageUtil.sendTitle(player, "&c채팅 불가!", "&r채팅은 &e" + Constant.Sosu2.format(chatCooldown / 20d) + "초&r 마다 가능합니다.", 5, 60, 15);
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        return;
      }
      Variable.playerChatNoSpamAlertCooldown.add(uuid);
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerChatNoSpamAlertCooldown.remove(uuid), chatCooldown);
      if (Cucumbery.config.getBoolean("no-spam.same-message.enable"))
      {
        chatCooldown = Cucumbery.config.getInt("no-spam.same-message.cooldown-in-ticks");
        if (message.equals(Variable.playerChatSameMessageSpamAlertCooldown.get(uuid)))
        {
          event.setCancelled(true);
          MessageUtil.sendTitle(player, "&c채팅 불가!", "&r같은 메시지를 &e" + Constant.Sosu2.format(chatCooldown / 20d) + "초&r 이내에 채팅할 수 없습니다.", 5, 60, 15);
          SoundPlay.playSound(player, Constant.ERROR_SOUND);
          return;
        }
        Variable.playerChatSameMessageSpamAlertCooldown.put(uuid, message);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerChatSameMessageSpamAlertCooldown.remove(uuid), chatCooldown);
      }
    }
    if (CustomEffectManager.hasEffect(player, CustomEffectType.OUTSIDER))
    {
      @SuppressWarnings("all")
      int amplifier = CustomEffectManager.getEffect(player, CustomEffectType.OUTSIDER).getAmplifier() + 1;
      if (Math.random() * 100 < amplifier * 10)
      {
        event.setCancelled(true);
        return;
      }
    }
    if (Cucumbery.config.getBoolean("play-sound-on-chat"))
    {
      if (!Method.configContainsLocation(location, Cucumbery.config.getStringList("no-play-sound-on-chat-worlds")))
      {
        for (Player online : Bukkit.getServer().getOnlinePlayers())
        {
          online.playSound(online.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1F, 1F);
        }
      }
    }
    int chatRepeat = 1;
    if (CustomEffectManager.hasEffect(player, CustomEffectType.CURSE_OF_BEANS))
    {
      chatRepeat++;
    }
    if (CustomEffectManager.hasEffect(player, CustomEffectType.INSIDER))
    {
      chatRepeat *= Objects.requireNonNull(CustomEffectManager.getEffect(player, CustomEffectType.INSIDER)).getAmplifier() + 2;
    }
    if (chatRepeat > 1)
    {
      Audience audience = Audience.audience(event.viewers());
      for (int i = 1; i < chatRepeat; i++)
      {
        audience.sendMessage(event.renderer().render(player, player.displayName(), event.message(), Audience.audience(event.viewers())));
      }
    }
  }

  @EventHandler(ignoreCancelled = true)
  public void specialParse(AsyncChatEvent event)
  {
    Player player = event.getPlayer();
    Component senderComponent = SenderComponentUtil.senderComponent(player);
    player.displayName(player.displayName().hoverEvent(senderComponent.hoverEvent()));

    String message = ComponentUtil.serialize(event.message());
    if (message.contains("[i]"))
    {
      if (!ItemStackUtil.itemExists(player.getInventory().getItemInMainHand()))
      {
        event.setCancelled(true);
        MessageUtil.sendError(player, ComponentUtil.createTranslate("주로 사용하는 손에 아이템을 들고 있어야 아이템 확성기를 사용할 수 있습니다."));
        return;
      }
    }
    /* 채팅을 칠때 해당 권한이 있으면 컬러 채팅으로 변환 */
    if (Permission.OTHER_PLACEHOLDER.has(player))
    {
      if (message.contains("--noph"))
      {
        message = message.replaceFirst("--noph", "");
      }
      else
      {
        message = PlaceHolderUtil.placeholder(player, message, null);
      }
    }
    if (Permission.OTHER_EVAL.has(player))
    {
      if (message.contains("--noeval") || message.contains("--nocalc"))
      {
        message = message.replaceFirst("--noeval", "");
        message = message.replaceFirst("--nocalc", "");
      }
      else
      {
        message = PlaceHolderUtil.evalString(message);
      }
    }
    if (Permission.EVENT2_CHAT_COLOR.has(player))
    {
      message = message.replace("(void)", "");
      if (message.contains("--nocolor"))
      {
        message = message.replaceFirst("--nocolor", "");
        message = message.replace("§", "&");
      }
      else
      {
        message = MessageUtil.n2s(message);
      }
      if (message.contains("--strip"))
      {
        message = message.replaceFirst("--strip", "");
        message = MessageUtil.stripColor(message);
      }
    }
    event.message(ComponentUtil.create2(player, message, false));
  }
}

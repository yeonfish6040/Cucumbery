package com.jho5245.cucumbery.listeners.player.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeCooldown;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.PlaceHolderUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.sendercomponent.SenderComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class PlayerChat implements Listener
{
  private final HashMap<UUID, BukkitTask> sameMessageCooldownMangers = new HashMap<>();

  @EventHandler
  public void onPlayerChat(AsyncChatEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Player player = event.getPlayer();
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
Cucumbery.getPlugin().getLogger().warning(        e.getMessage());
      }
    }
    if (message.contains("[i]"))
    {
      ItemStack itemStack = player.getInventory().getItemInMainHand();
      if (!ItemStackUtil.itemExists(itemStack))
      {
        MessageUtil.sendError(player, Prefix.NO_HOLDING_ITEM);
        event.setCancelled(true);
        return;
      }
      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
              player.chat("/bitem " + message), 0L);
      event.setCancelled(true);
      return;
    }
    if (CustomEffectManager.hasEffect(player, CustomEffectType.MAESTRO) && player.getGameMode() == GameMode.CREATIVE)
    {
      try
      {
        int note = Integer.parseInt(message);
        if (note > 24 || note < 0)
        {
          MessageUtil.sendWarn(player, "0~24만 입력해라");
          return;
        }
        Block belowBlock = player.getLocation().add(0, -1, 0).getBlock();
        event.setCancelled(true);
        if (belowBlock.getType() != Material.NOTE_BLOCK && !belowBlock.getType().isAir())
        {
          MessageUtil.sendWarn(player, "바로 아래 블록이 소리 블록 또는 설치할 수 있는 공간이 없습니다!");
          return;
        }
        Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
        {
          if (belowBlock.getType() != Material.NOTE_BLOCK)
          {
            belowBlock.setType(Material.NOTE_BLOCK, true);
          }
          NoteBlock noteBlock = (NoteBlock) belowBlock.getBlockData();
          noteBlock.setNote(new Note(note));
          Block block = player.getLocation().add(0, -2, 0).getBlock();
          switch (block.getType())
          {
            case GLOWSTONE -> noteBlock.setInstrument(Instrument.PLING);
          }
          belowBlock.setBlockData(noteBlock);
          MessageUtil.sendActionBar(player, "소리 블록 음높이 설정 완료(%s)", noteBlock.getNote());
        }, 0L);
        return;
      }
      catch (Exception ignored)
      {

      }
    }
    if (CustomEffectManager.hasEffect(player, CustomEffectType.MUTE))
    {
      event.setCancelled(true);
      MessageUtil.sendMessage(player, Prefix.INFO, "채팅을 할 수 없는 상태입니다");
      return;
    }
    UUID uuid = player.getUniqueId();
    if (event.isCancelled())
    {
      return;
    }
    if (!Cucumbery.config.getBoolean("grant-default-permission-to-players") && !Permission.EVENT_CHAT.has(player))
    {
      event.setCancelled(true);
      if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerChatAlertCooldown.contains(uuid))
      {
        Variable.playerChatAlertCooldown.add(uuid);
        MessageUtil.sendTitle(player, "&c채팅 불가!", "&r채팅을 칠 권한이 없습니다", 5, 80, 15);
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
        MessageUtil.sendMessage(player, Prefix.INFO_ALLPLAYER, "채팅을 칠 수 없는 상태입니다");
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerChatAlertCooldown.remove(uuid), 100L);
      }
      return;
    }
    if (CustomEffectManager.hasEffect(player, CustomEffectTypeCooldown.COOLDOWN_CHAT))
    {
      event.setCancelled(true);
      MessageUtil.sendWarn(player, ComponentUtil.translate("채팅 쿨타임 상태입니다. 속도를 늦춰주세요."));
      return;
    }
    Location location = player.getLocation();
    if (!Permission.EVENT_CHAT_SPAM.has(player) && Cucumbery.config.getBoolean("no-spam.enable") && !Method.configContainsLocation(location, Cucumbery.config.getStringList("no-spam.no-worlds")))
    {
      if (Cucumbery.config.getBoolean("no-spam.same-message.enable"))
      {
        int chatCooldownSameMessage = Cucumbery.config.getInt("no-spam.same-message.cooldown-in-ticks");
        if (message.equals(Variable.playerChatSameMessageSpamAlertCooldown.get(uuid)))
        {
          event.setCancelled(true);
          MessageUtil.sendTitle(player, "&c채팅 불가!", "&r같은 메시지를 rg255,204;" + Constant.Sosu2.format(chatCooldownSameMessage / 20d) + "초&r 이내에 채팅할 수 없습니다", 5, 60, 15);
          SoundPlay.playSound(player, Constant.ERROR_SOUND);
          return;
        }
        Variable.playerChatSameMessageSpamAlertCooldown.put(uuid, message);
        if (sameMessageCooldownMangers.containsKey(uuid))
        {
          sameMessageCooldownMangers.get(uuid).cancel();
        }
        sameMessageCooldownMangers.put(uuid, Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerChatSameMessageSpamAlertCooldown.remove(uuid), chatCooldownSameMessage));
      }
      int chatCooldown = Cucumbery.config.getInt("no-spam.cooldown-in-ticks");
      if (chatCooldown > 0 && Cucumbery.config.getBoolean("no-spam.buff"))
      {
        Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                CustomEffectManager.addEffect(player, new CustomEffect(CustomEffectTypeCooldown.COOLDOWN_CHAT, chatCooldown)), 0L);
      }
      if (Variable.playerChatNoSpamAlertCooldown.contains(uuid))
      {
        event.setCancelled(true);
        MessageUtil.sendTitle(player, "&c채팅 불가!", "&r채팅은 rg255,204;" + Constant.Sosu2.format(chatCooldown / 20d) + "초&r 마다 가능합니다", 5, 60, 15);
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        return;
      }
      Variable.playerChatNoSpamAlertCooldown.add(uuid);
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerChatNoSpamAlertCooldown.remove(uuid), chatCooldown);
    }
    if (CustomEffectManager.hasEffect(player, CustomEffectType.OUTSIDER))
    {
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
    Component senderComponent = SenderComponentUtil.senderComponent(player, player, null);
    player.displayName(senderComponent);
    final Component originalDisplayName = player.displayName();
    player.displayName(player.playerListName().hoverEvent(null).clickEvent(null).insertion(null));
    senderComponent = SenderComponentUtil.senderComponent(player, player, null);
    player.playerListName(senderComponent.hoverEvent(null).clickEvent(null).insertion(null));
    player.displayName(originalDisplayName);
    String message = ComponentUtil.serialize(event.message());
    boolean parse = message.startsWith("parse:") && player.hasPermission("asdf");
    boolean hasParseEffect = CustomEffectManager.hasEffect(player, CustomEffectType.AUTO_CHAT_PARSER);
    if (parse)
    {
      message = message.substring("parse:".length());
    }
    if (CustomEffectManager.hasEffect(player, CustomEffectType.GD_TO_HI) && message.equalsIgnoreCase("gd"))
    {
      message = "ㅎㅇ";
    }
    if (CustomEffectManager.hasEffect(player, CustomEffectType.BACKWARDS_CHAT))
    {
      message = ComponentUtil.serialize(event.message());
      message = new StringBuffer(message).reverse().toString();
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
    if (parse || hasParseEffect)
    {
      event.message(ComponentUtil.create(false, message));
    }
    else
    {
      event.message(ComponentUtil.create2(message, false));
    }
    if (CustomEffectManager.hasEffect(player, CustomEffectType.THICK))
    {
      event.message(MessageUtil.boldify(event.message()));
    }
  }
}

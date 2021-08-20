package com.jho5245.cucumbery.listeners.player;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.PlaceHolderUtil;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.SoundPlay;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.UUID;

public class PlayerCommandPreprocess implements Listener
{
  @EventHandler
  public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();
    FileConfiguration cfg = Cucumbery.config;
    if (!cfg.getBoolean("grant-default-permission-to-players") && !Permission.EVENT_COMMANDPREPROCESS.has(player))
    {
      event.setCancelled(true);
      if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerCommandPreprocessAlertCooldown.contains(uuid))
      {
        Variable.playerCommandPreprocessAlertCooldown.add(uuid);
        MessageUtil.sendTitle(player, "&c명령어 사용 불가!", "&r명령어를 사용할 권한이 없습니다.", 5, 80, 15);
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerCommandPreprocessAlertCooldown.remove(uuid), 100L);
      }
      return;
    }
    if (!Permission.EVENT2_ANTI_ALLPLAYER.has(player) && Constant.AllPlayer.EXECUTE_COMMAND.isEnabled())
    {
      event.setCancelled(true);
      if (!Variable.playerCommandPreprocessAlertCooldown.contains(uuid))
      {
        Variable.playerCommandPreprocessAlertCooldown.add(uuid);
        MessageUtil.sendMessage(player, Prefix.INFO_ALLPLAYER, "명령어를 실행할 수 없는 상태입니다.");
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerCommandPreprocessAlertCooldown.remove(uuid), 100L);
      }
      return;
    }
    String message = event.getMessage();
    String[] split = message.split(" ");
    String label = split[0];
    label = label.substring(1);
    if (!Permission.EVENT2_COMMAND_SEND_COLON.has(player))
    {
      if (label.contains(":"))
      {
        event.setCancelled(true);
        // 알 수 없는 명령어 처리
        Bukkit.getServer().dispatchCommand(player, "cucumberyunknowncommand");
        if (UserData.LISTEN_COMMAND.getBoolean(player.getUniqueId()))
        {
          SoundPlay.playSound(player, Sound.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS);
        }
        return;
      }
    }
    if (Method.usingLoreFeature(player))
    {
      if ((player.isOp() || player.getGameMode() == GameMode.CREATIVE) && split.length > 1)
      {
        switch (label)
        {
          case "i", "ei", "eitem", "essentials:i", "essentials:ei", "essentials:eitem", "enchant", "enchantment", "essentials:enchant", "essentials:enchantment", "ie", "itemedit", "itemedit:ie", "itemedit:itemedit" -> Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                          Method.updateInventory(player),
                  0L);
          case "minecraft:item", "item" -> Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
          {
            for (Player online : Bukkit.getServer().getOnlinePlayers())
            {
              Method.updateInventory(online);
            }
          }, 0L);
        }
      }
    }
    if (Variable.scrollReinforcing.contains(uuid) && !message.equalsIgnoreCase("/강화 quit")
            && !message.equalsIgnoreCase("/강화 realstart") && !message.equalsIgnoreCase("/강화 파괴방지사용")
            && !message.equalsIgnoreCase("/강화 파괴방지미사용"))
    {
      event.setCancelled(true);
      MessageUtil.sendError(player, "강화중에는 명령어를 사용할 수 없습니다.");
      Component a = ComponentUtil.create(Prefix.INFO + "만약 아이템 강화를 중지하시려면 이 문장을 클릭해주세요.", "클릭하면 강화를 중지합니다.",
              ClickEvent.Action.RUN_COMMAND, "/강화 quit");
      player.sendMessage(a);
      return;
    }
    this.playSoundOnPerformCommand(event);
  }

  private void playSoundOnPerformCommand(PlayerCommandPreprocessEvent event)
  {
    Player player = event.getPlayer();
    if (UserData.LISTEN_COMMAND.getBoolean(player.getUniqueId()))
    {
      SoundPlay.playSound(player, Sound.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS);
    }
  }

  @EventHandler(ignoreCancelled = true)
  public void specialParse(PlayerCommandPreprocessEvent event)
  {
    Player player = event.getPlayer();
    String message = event.getMessage();
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
    event.setMessage(message);
  }
}

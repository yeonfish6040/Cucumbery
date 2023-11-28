package com.jho5245.cucumbery.listeners.player.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.children.group.ItemStackCustomEffect;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectRemoveEvent.RemoveReason;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.PlaceHolderUtil;
import com.jho5245.cucumbery.util.storage.component.ItemStackComponent;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextDecoration.State;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;

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
    if (CustomEffectManager.hasEffect(player, CustomEffectType.INVINCIBLE_RESPAWN))
    {
      CustomEffectManager.removeEffect(player, CustomEffectType.INVINCIBLE_RESPAWN);
    }
    UUID uuid = player.getUniqueId();
    FileConfiguration cfg = Cucumbery.config;
    if (!cfg.getBoolean("grant-default-permission-to-players") && !Permission.EVENT_COMMANDPREPROCESS.has(player))
    {
      event.setCancelled(true);
      if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerCommandPreprocessAlertCooldown.contains(uuid))
      {
        Variable.playerCommandPreprocessAlertCooldown.add(uuid);
        MessageUtil.sendTitle(player, "&c명령어 사용 불가!", "&r명령어를 사용할 권한이 없습니다", 5, 80, 15);
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
        MessageUtil.sendMessage(player, Prefix.INFO_ALLPLAYER, "명령어를 실행할 수 없는 상태입니다");
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerCommandPreprocessAlertCooldown.remove(uuid), 100L);
      }
      return;
    }

    boolean playSound = Cucumbery.config.getBoolean("play-sound-on-command") && !Method.configContainsLocation(player.getLocation(), Cucumbery.config.getStringList("no-play-sound-on-command-worlds")) &&
            UserData.LISTEN_COMMAND.getBoolean(player.getUniqueId());

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
        player.sendMessage(ComponentUtil.translate("command.unknown.command", NamedTextColor.RED));
        player.sendMessage(Component.empty().append(Component.text(label, NamedTextColor.RED).decoration(TextDecoration.UNDERLINED, State.TRUE))
                .append(ComponentUtil.translate("&ccommand.context.here").decoration(TextDecoration.ITALIC, State.TRUE)));
        this.playSoundOnPerformCommand(event, playSound);
        return;
      }
    }
    if (Method.usingLoreFeature(player))
    {
      if ((player.isOp() || player.getGameMode() == GameMode.CREATIVE) && split.length > 1)
      {
        switch (label)
        {
          case "i", "ei", "eitem", "essentials:i", "essentials:ei", "essentials:eitem", "enchant", "enchantment", "essentials:enchant", "essentials:enchantment", "ie", "itemedit", "itemedit:ie", "itemedit:itemedit" ->
                  Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                                  ItemStackUtil.updateInventory(player),
                          0L);
          case "minecraft:item", "item" -> Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
          {
            for (Player online : Bukkit.getServer().getOnlinePlayers())
            {
              ItemStackUtil.updateInventory(online);
            }
          }, 0L);
        }
      }
    }

    if ("i".equals(label) && split.length == 2)
    {
      String itemName = split[1].replace("cpick", "copper_pickaxe")
              .replace("tpick", "tungsten_pickaxe")
              .replace("copick", "cobalt_pickaxe")
              .replace("mpick", "mithril_pickaxe")
              .replace("tipick", "titanium_pickaxe")
              .replace("td2", "titanium_drill_r266")
              .replace("td3", "titanium_drill_r366")
              .replace("td4", "titanium_drill_r466")
              .replace("td5", "titanium_drill_r566")
              .replace("md", "mindas_drill");
      if (!itemName.equals(split[1]))
      {
        Bukkit.dispatchCommand(player, "cgive @s " + itemName);
        this.playSoundOnPerformCommand(event, playSound);
        return;
      }
    }

    // cucumberify
    if (Cucumbery.using_CommandAPI && message.startsWith("/give "))
    {
      message = "/cgive " + message.substring(6);
      event.setMessage(message);
    }
    if (message.startsWith("/cgive"))
    {
      String[] split2 = message.split(" ");
      if (split2.length == 2)
      {
        message = split2[0] + " @s " + split2[1];
        event.setMessage(message);
      }
    }
    if (Variable.scrollReinforcing.contains(uuid) && !message.equalsIgnoreCase("/강화 quit")
            && !message.equalsIgnoreCase("/강화 realstart") && !message.equalsIgnoreCase("/강화 파괴방지사용")
            && !message.equalsIgnoreCase("/강화 파괴방지미사용") && !message.equalsIgnoreCase("/강화 스타캐치해제사용")
            && !message.equalsIgnoreCase("/강화 스타캐치해제미사용") && !message.equals("/강화 starcatch"))
    {
      event.setCancelled(true);
      MessageUtil.sendError(player, "강화중에는 명령어를 사용할 수 없습니다");
      Component a = ComponentUtil.create(Prefix.INFO, "만약 아이템 강화를 중지하시려면 이 문장을 클릭해주세요.").hoverEvent(ComponentUtil.create("클릭하면 강화를 중지합니다")).clickEvent(ClickEvent.runCommand("/강화 quit"));
      player.sendMessage(a);
      return;
    }
    if (message.equals(Constant.DROP_UNTRADABLE_ITEM))
    {
      if (CustomEffectManager.hasEffect(player, CustomEffectType.NOTIFY_NO_TRADE_ITEM_DROP))
      {
        CustomEffect customEffect = CustomEffectManager.getEffect(player, CustomEffectType.NOTIFY_NO_TRADE_ITEM_DROP);
        if (customEffect instanceof ItemStackCustomEffect itemStackCustomEffect)
        {
          ItemStack itemStack = itemStackCustomEffect.getItemStack();
          Object itemComponent = itemStack.getMaxStackSize() == 1 ? itemStack : ItemStackComponent.itemStackComponent(itemStack, Constant.THE_COLOR);
          if (ItemStackUtil.countItem(player.getInventory(), itemStack) == 0)
          {
            MessageUtil.sendWarn(player, "인벤토리에 %s이(가) 충분히 없거나 아이템의 속성이 변경되어 제거할 수 없었습니다", itemComponent);
            Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                    CustomEffectManager.removeEffect(player, CustomEffectType.NOTIFY_NO_TRADE_ITEM_DROP, RemoveReason.TIME_OUT), 0L);
            event.setCancelled(true);
            return;
          }
          player.getInventory().removeItem(itemStack);
          MessageUtil.info(player, "%s을(를) 인벤토리에서 제거했습니다", itemComponent);
          Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                  CustomEffectManager.removeEffect(player, CustomEffectType.NOTIFY_NO_TRADE_ITEM_DROP), 0L);
        }
      }
      event.setCancelled(true);
      return;
    }
    this.playSoundOnPerformCommand(event, playSound);
  }

  private void playSoundOnPerformCommand(PlayerCommandPreprocessEvent event, boolean playSound)
  {
    Player player = event.getPlayer();
    if (playSound)
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

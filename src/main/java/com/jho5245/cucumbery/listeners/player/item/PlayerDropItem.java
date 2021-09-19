package com.jho5245.cucumbery.listeners.player.item;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.SoundPlay;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Constant.AllPlayer;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerDropItem implements Listener
{
  @EventHandler
  public void onPlayerDropItem(PlayerDropItemEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();

    // 아이템 섭취 사용에서 사라지지 않을 경우 아이템 소실 방지를 위한 쿨타임
    if (Variable.playerItemConsumeCauseSwapCooldown.contains(uuid))
    {
      event.setCancelled(true);
      return;
    }

    // 신호기 관련 이벤트가 캔슬되어 아이템이 사용되지 않았을 경우 바닥에 아이템을 떨어뜨리는 것을 막음 (인벤토리가 가득 찼을 경우에는 제외)
    if (Variable.playerChangeBeaconEffectItemDropCooldown.contains(uuid))
    {
      event.setCancelled(true);
      return;
    }
    ItemStack item = event.getItemDrop().getItemStack();
    if (Variable.scrollReinforcing.contains(uuid))
    {
      event.setCancelled(true);
      Method.playSound(player, Constant.ERROR_SOUND);
      MessageUtil.sendError(player, "강화중에는 아이템을 버릴 수 없습니다.");
      Component a = ComponentUtil.create(Prefix.INFO + "만약 아이템 강화를 중지하시려면 이 문장을 클릭해주세요.", "클릭하면 강화를 중지합니다.",
              ClickEvent.Action.RUN_COMMAND, "/강화 quit");
      player.sendMessage(a);
      return;
    }
    if (!Cucumbery.config.getBoolean("grant-default-permission-to-players") && !Permission.EVENT_ITEM_DROP.has(player))
    {
      event.setCancelled(true);
      if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.itemDropAlertCooldown.contains(uuid))
      {
        Variable.itemDropAlertCooldown.add(uuid);
        MessageUtil.sendTitle(player, "&c버리기 불가!", "&r아이템을 버릴 권한이 없습니다.", 5, 80, 15);
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.itemDropAlertCooldown.remove(uuid), 100L);
      }
      return;
    }
    if (!Permission.EVENT2_ANTI_ALLPLAYER.has(player) && AllPlayer.ITEM_DROP.isEnabled())
    {
      event.setCancelled(true);
      if (!Variable.itemDropAlertCooldown.contains(uuid))
      {
        Variable.itemDropAlertCooldown.add(uuid);
        MessageUtil.sendMessage(player, Prefix.INFO_ALLPLAYER, "아이템을 버릴 수 없는 상태입니다.");
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.itemDropAlertCooldown.remove(uuid), 100L);
      }
      return;
    }
    if (UserData.ITEM_DROP_MODE.getString(player.getUniqueId()).equals("disabled"))
    {
      event.setCancelled(true);
      if (!Variable.itemDropDisabledAlertCooldown.contains(uuid))
      {
        Variable.itemDropDisabledAlertCooldown.add(uuid);
        MessageUtil.sendWarn(player, "현재 아이템 버리기 모드가 비활성화 되어 있습니다.");
        MessageUtil.info(player, "메뉴에서 아이템 버리기 모드를 변경하실 수 있습니다.");
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.itemDropDisabledAlertCooldown.remove(uuid), 40L);
      }
      return;
    }
    else if (UserData.ITEM_DROP_MODE.getString(player.getUniqueId()).equals("sneak"))
    {
      if (!player.isSneaking())
      {
        event.setCancelled(true);
        return;
      }
    }
    if (NBTAPI.isRestricted(player, item, RestrictionType.NO_DROP))
    {
      event.setCancelled(true);
      if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.itemDropAlertCooldown2.contains(uuid))
      {
        Variable.itemDropAlertCooldown2.add(uuid);
        MessageUtil.sendTitle(player, "&c버리기 불가!", "&r버릴 수 없는 아이템입니다.", 5, 60, 15);
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.itemDropAlertCooldown2.remove(uuid), 100L);
      }
      return;
    }
    if (NBTAPI.isRestricted(player, item, RestrictionType.NO_TRADE))
    {
      event.setCancelled(true);
      if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.itemDropAlertCooldown2.contains(uuid))
      {
        Variable.itemDropAlertCooldown2.add(uuid);
        MessageUtil.sendTitle(player, "&c버리기 불가!", "&r캐릭터 귀속 아이템은 버릴 수 없습니다.", 5, 60, 15);
        MessageUtil.info(player, ComponentUtil.create("이 메시지를 클릭하여 쓰레기통을 엽니다.").clickEvent(ClickEvent.runCommand("/trashcan")));
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.itemDropAlertCooldown2.remove(uuid), 100L);
      }
      return;
    }
    if (player.getOpenInventory().getType() == InventoryType.SHULKER_BOX
            && player.getOpenInventory().getTitle().contains(Constant.ITEM_PORTABLE_SHULKER_BOX_GUI) && Constant.SHULKER_BOXES.contains(item.getType()))
    {
      event.setCancelled(true);
    }
    this.dropDelay(event, player);
    if (!event.isCancelled())
    {
      this.actionbarOnItemDrop(player, item);
    }
  }

  private void dropDelay(PlayerDropItemEvent event, Player player)
  {
    if (!Cucumbery.config.getBoolean("enable-item-drop-delay"))
    {
      return;
    }
    int delayTick = UserData.ITEM_DROP_DELAY.getInt(player.getUniqueId());
    UUID uuid = player.getUniqueId();
    if (!Variable.itemDropAlertCooldown3.contains(uuid))
    {
      Variable.itemDropAlertCooldown3.add(uuid);
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.itemDropAlertCooldown3.remove(uuid), delayTick);
    }
    else
    {
      event.setCancelled(true);
      if (UserData.ITEM_DROP_DELAY_ALERT.getBoolean(player.getUniqueId()) && !Variable.itemDropAlertCooldown4.contains(uuid))
      {
        double delay = delayTick / 20D;
        MessageUtil.sendTitle(player, "&c버리기 불가!", "&r아이템은 &e" + Constant.Sosu2.format(delay) + "초&r마다 버릴 수 있습니다. 너무 자주 버리지 마십시오.", 5, 80, 15);
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Variable.itemDropAlertCooldown4.add(uuid);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.itemDropAlertCooldown4.remove(uuid), 60L);
      }
    }
  }

  private void actionbarOnItemDrop(Player player, ItemStack item)
  {

    if (UserData.SHOW_ACTIONBAR_ON_ITEM_DROP.getBoolean(player.getUniqueId()))
    {
      int amount = item.getAmount();
      Component itemStackComponent = ComponentUtil.itemName(item, TextColor.fromHexString("#ff9900"));
      if (amount == 1 && item.getType().getMaxStackSize() == 1)
      {
        player.sendActionBar(ComponentUtil.createTranslate("#ffd900;%s을(를) 버렸습니다.", itemStackComponent));
      }
      else
      {
        player.sendActionBar(ComponentUtil.createTranslate("#ffd900;%s을(를) %s개 버렸습니다.", itemStackComponent, "#ff9900;" + amount));
      }
    }
    if (UserData.LISTEN_ITEM_DROP.getBoolean(player.getUniqueId()))
    {
      Method.heldItemSound(player, item);
      SoundPlay.playSound(player, Sound.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 1F, 0.5F);
    }
  }
}

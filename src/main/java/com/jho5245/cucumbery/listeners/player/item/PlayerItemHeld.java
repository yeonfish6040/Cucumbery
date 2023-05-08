package com.jho5245.cucumbery.listeners.player.item;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.listeners.inventory.InventoryClick;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Constant.AllPlayer;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerItemHeld implements Listener
{
  private final List<Player> cooldown = new ArrayList<>();

  @EventHandler
  public void onPlayerItemHeld(PlayerItemHeldEvent event)
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
    if (!Cucumbery.config.getBoolean("grant-default-permission-to-players") && !Permission.EVENT_ITEMHELD.has(player))
    {
      event.setCancelled(true);
      player.updateInventory();
      if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerItemHeldAlertCooldown.contains(uuid))
      {
        Variable.playerItemHeldAlertCooldown.add(uuid);
        MessageUtil.sendTitle(player, "&c행동 불가!", "&r단축바 슬롯을 변경할 권한이 없습니다", 5, 80, 15);
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerItemHeldAlertCooldown.remove(uuid), 100L);
      }
      return;
    }
    if (!Permission.EVENT2_ANTI_ALLPLAYER.has(player) && AllPlayer.ITEM_HELD.isEnabled())
    {
      event.setCancelled(true);
      if (!Variable.playerItemHeldAlertCooldown.contains(uuid))
      {
        Variable.playerItemHeldAlertCooldown.add(uuid);
        MessageUtil.sendMessage(player, Prefix.INFO_ALLPLAYER, "단축바 슬롯을 변경할 수 없는 상태입니다");
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerItemHeldAlertCooldown.remove(uuid), 100L);
      }
      return;
    }
    if (Variable.scrollReinforcing.contains(uuid))
    {
      event.setCancelled(true);
      Method.playSound(player, Constant.ERROR_SOUND);
      MessageUtil.sendError(player, "강화중에는 주로 사용하는 손에 들고 있는 아이템을 바꿀 수 없습니다");
      Component a = ComponentUtil.create(Prefix.INFO, "만약 아이템 강화를 중지하시려면 이 문장을 클릭해주세요.").hoverEvent(ComponentUtil.create("클릭하면 강화를 중지합니다")).clickEvent(ClickEvent.runCommand("/강화 quit"));
      player.sendMessage(a);
      return;
    }

    if (Cucumbery.config.getBoolean("use-helpful-lore-feature"))
    {
      if (player.getGameMode() == GameMode.CREATIVE && InventoryClick.check.contains(player))
      {
        ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
        ItemStack previousItem = player.getInventory().getItem(event.getPreviousSlot());
        if (ItemStackUtil.itemExists(newItem) && ItemStackUtil.itemExists(previousItem))
        {
          newItem = newItem.clone();
          previousItem = previousItem.clone();
          previousItem.setAmount(1);
          if (newItem.equals(previousItem) && newItem.getAmount() == 1)
          {
            if (!cooldown.contains(player))
            {
              cooldown.add(player);
              event.setCancelled(true);
              player.getInventory().setItem(event.getNewSlot(), null);
              Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> cooldown.remove(player), 2L);
              return;
            }
          }
          ItemStack[] hotBars = new ItemStack[9];
          for (int i = 0; i < 9; i++)
          {
            ItemStack hotBarItem = player.getInventory().getItem(i);
            if (ItemStackUtil.itemExists(hotBarItem))
            {
              hotBars[i] = hotBarItem;
            }
            else
            {
              hotBars[i] = new ItemStack(Material.AIR);
            }
          }
          for (int i = 0; i < hotBars.length; i++)
          {
            ItemStack hotBar = hotBars[i].clone();
            hotBar.setAmount(1);
            if ((hotBar.equals(newItem) && i != event.getNewSlot()))
            {
              player.getInventory().setItem(event.getNewSlot(), null);
              player.getInventory().setHeldItemSlot(i);
              break;
            }
          }
        }
      }
    }
    // commented since 2023.01.22 due to its uselessness
//    if (!Objects.equals(player.getInventory().getItem(event.getPreviousSlot()), player.getInventory().getItem(event.getNewSlot())) && CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_PROGRESS))
//    {
//      MiningManager.quitCustomMining(player);
//    }
    this.removeActionbarCooldown(player);
    this.heldItemSound(event, player);
  }

  private void removeActionbarCooldown(Player player)
  {
    if (Variable.playerActionbarCooldownIsShowing.contains(player.getUniqueId()))
    {
      MessageUtil.sendActionBar(player, " ");
      Variable.playerActionbarCooldownIsShowing.remove(player.getUniqueId());
    }
  }

  private void heldItemSound(PlayerItemHeldEvent event, Player player)
  {
    if (CustomConfig.UserData.LISTEN_HELDITEM.getBoolean(player.getUniqueId()))
    {
      ItemStack item = player.getInventory().getItem(event.getNewSlot());
      Method.heldItemSound(player, item);
    }
  }
}

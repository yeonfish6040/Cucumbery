package com.jho5245.cucumbery.listeners.player.item;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.children.group.ItemStackCustomEffectImple;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.ItemStackComponent;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Constant.AllPlayer;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;

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

    // 굳검 이벤트 예외 토글 아이템
    Item item = event.getItemDrop();
    ItemStack itemStack = item.getItemStack();

    this.itemDropUsage(event, player, itemStack, player.isSneaking());
    if (event.isCancelled())
    {
      if (new NBTItem(itemStack).getBoolean("RemoveOnDropUsage"))
      {
        itemStack.setAmount(0);
      }
      return;
    }

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
    if (Variable.scrollReinforcing.contains(uuid))
    {
      event.setCancelled(true);
      Method.playSound(player, Constant.ERROR_SOUND);
      MessageUtil.sendError(player, "강화중에는 아이템을 버릴 수 없습니다");
      Component a = ComponentUtil.create(Prefix.INFO, "만약 아이템 강화를 중지하시려면 이 문장을 클릭해주세요.").hoverEvent(ComponentUtil.create("클릭하면 강화를 중지합니다")).clickEvent(ClickEvent.runCommand("/강화 quit"));
      player.sendMessage(a);
      return;
    }
    if (!Cucumbery.config.getBoolean("grant-default-permission-to-players") && !Permission.EVENT_ITEM_DROP.has(player))
    {
      event.setCancelled(true);
      if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.itemDropAlertCooldown.contains(uuid))
      {
        Variable.itemDropAlertCooldown.add(uuid);
        MessageUtil.sendTitle(player, "&c버리기 불가!", "&r아이템을 버릴 권한이 없습니다", 5, 80, 15);
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
        MessageUtil.sendMessage(player, Prefix.INFO_ALLPLAYER, "아이템을 버릴 수 없는 상태입니다");
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
        MessageUtil.sendWarn(player, "현재 아이템 버리기 모드가 비활성화 되어 있습니다");
        MessageUtil.info(player, "메뉴에서 아이템 버리기 모드를 변경하실 수 있습니다");
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
    if (NBTAPI.isRestricted(player, itemStack, RestrictionType.NO_DROP))
    {
      event.setCancelled(true);
      if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.itemDropAlertCooldown2.contains(uuid))
      {
        Variable.itemDropAlertCooldown2.add(uuid);
        MessageUtil.sendTitle(player, "&c버리기 불가!", "&r버릴 수 없는 아이템입니다", 5, 60, 15);
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.itemDropAlertCooldown2.remove(uuid), 100L);
      }
      return;
    }
    if (itemStack.getItemMeta() instanceof BundleMeta bundleMeta)
    {
      for (ItemStack stack : bundleMeta.getItems())
      {
        if (NBTAPI.isRestricted(player, stack, RestrictionType.NO_DROP))
        {
          event.setCancelled(true);
          if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.itemDropAlertCooldown2.contains(uuid))
          {
            Variable.itemDropAlertCooldown2.add(uuid);
            MessageUtil.sendTitle(player, "&c버리기 불가!", "꾸러미에 버릴 수 없는 아이템이 들어있습니다", 5, 60, 15);
            SoundPlay.playSound(player, Constant.ERROR_SOUND);
            Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.itemDropAlertCooldown2.remove(uuid), 100L);
          }
          return;
        }
        if (NBTAPI.isRestricted(player, stack, RestrictionType.NO_TRADE))
        {
          event.setCancelled(true);
          if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.itemDropAlertCooldown2.contains(uuid))
          {
            Variable.itemDropAlertCooldown2.add(uuid);
            MessageUtil.sendTitle(player, "&c버리기 불가!", "꾸러미에 캐릭터 귀속 아이템이 들어있습니다", 5, 60, 15);
            SoundPlay.playSound(player, Constant.ERROR_SOUND);
            Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.itemDropAlertCooldown2.remove(uuid), 100L);
          }
          return;
        }
      }
    }
    if (NBTAPI.isRestricted(player, itemStack, RestrictionType.NO_TRADE))
    {
      event.setCancelled(true);
      if (CustomEffectManager.hasEffect(player, CustomEffectType.NOTIFY_NO_TRADE_ITEM_DROP))
      {
        CustomEffectManager.removeEffect(player, CustomEffectType.NOTIFY_NO_TRADE_ITEM_DROP);
      }
      CustomEffectManager.addEffect(player, new ItemStackCustomEffectImple(CustomEffectType.NOTIFY_NO_TRADE_ITEM_DROP, itemStack.clone()));
      Object itemComponent = itemStack.getMaxStackSize() == 1 ? itemStack : ItemStackComponent.itemStackComponent(itemStack, Constant.THE_COLOR);
      MessageUtil.sendWarn(player, "%s은(는) 버린 후 다시 습득할 수 없습니다", itemComponent);
      MessageUtil.info(player, ComponentUtil.translate("그래도 버리시겠습니까? ").append(ComponentUtil.translate("&a[버리기]").hoverEvent(ComponentUtil.translate("클릭하여 %s을(를) 제거합니다", itemComponent))
              .clickEvent(ClickEvent.runCommand(Constant.DROP_UNTRADABLE_ITEM))));
      return;
    }
    if (CustomEffectManager.hasEffect(player, CustomEffectType.CURSE_OF_DROP))
    {
      event.setCancelled(true);
      return;
    }
    if (player.getOpenInventory().getType() == InventoryType.SHULKER_BOX
            && player.getOpenInventory().getTitle().contains(Constant.ITEM_PORTABLE_SHULKER_BOX_GUI) && Constant.SHULKER_BOXES.contains(itemStack.getType()))
    {
      event.setCancelled(true);
    }
    if (CustomEffectManager.hasEffect(player, CustomEffectType.CURSE_OF_VANISHING))
    {
      event.getItemDrop().remove();
    }
    this.dropDelay(event, player);
    if (!event.isCancelled())
    {
      this.actionbarOnItemDrop(player, itemStack);
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
        MessageUtil.sendTitle(player, "&c버리기 불가!", "&r아이템은 rg255,204;" + Constant.Sosu2.format(delay) + "초&r마다 버릴 수 있습니다. 너무 자주 버리지 마십시오.", 5, 80, 15);
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
      Component itemStackComponent = ItemNameUtil.itemName(item, TextColor.fromHexString("#ff9900"));
      if (amount == 1 && item.getType().getMaxStackSize() == 1)
      {
        player.sendActionBar(ComponentUtil.translate("#ffd900;%s을(를) 버렸습니다", itemStackComponent));
      }
      else
      {
        player.sendActionBar(ComponentUtil.translate("#ffd900;%s을(를) %s개 버렸습니다", itemStackComponent, "#ff9900;" + amount));
      }
    }
    if (UserData.LISTEN_ITEM_DROP.getBoolean(player.getUniqueId()))
    {
      Method.heldItemSound(player, item);
      SoundPlay.playSound(player, Sound.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 1F, 0.5F);
    }
  }
  private void itemDropUsage(PlayerDropItemEvent event, Player player, ItemStack itemStack, boolean isSneaking)
  {
    UUID uuid = player.getUniqueId();

    NBTCompound itemTag = NBTAPI.getMainCompound(itemStack);
    NBTCompound usageTag = NBTAPI.getCompound(itemTag, CucumberyTag.USAGE_KEY);
    NBTCompound usageDropTag = NBTAPI.getCompound(usageTag, isSneaking ? CucumberyTag.USAGE_COMMANDS_SNEAK_DROP_KEY: CucumberyTag.USAGE_COMMANDS_DROP_KEY);
    NBTCompound cooldownTag = NBTAPI.getCompound(usageDropTag, CucumberyTag.COOLDOWN_KEY);
    if (usageDropTag != null)
    {
      if (Variable.itemUseCooldown.contains(uuid))
      {
        event.setCancelled(true);
        return;
      }
      Variable.itemUseCooldown.add(uuid);
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.itemUseCooldown.remove(uuid), CustomConfig.UserData.ITEM_USE_DELAY.getInt(uuid));
    }
    if (!CustomConfig.UserData.EVENT_EXCEPTION_ACCESS.getBoolean(player.getUniqueId()) && cooldownTag != null)
    {
      try
      {
        long cooldownTime = cooldownTag.getLong(CucumberyTag.TIME_KEY);
        String cooldownTagTag = cooldownTag.getString(CucumberyTag.TAG_KEY);
        YamlConfiguration configPlayerCooldown = Variable.cooldownsItemUsage.get(uuid);
        long nextAvailable = configPlayerCooldown == null ? 0 : configPlayerCooldown.getLong(cooldownTagTag);
        long currentTime = System.currentTimeMillis();
        if (currentTime < nextAvailable)
        {
          String remainTime = Constant.THE_COLOR_HEX + Method.timeFormatMilli(nextAvailable - currentTime);
          MessageUtil.sendWarn(player, ComponentUtil.create("아직 %s을(를)" + (isSneaking ? " 웅크리고" : "") + " 사용할 수 없습니다 (남은 시간 : %s)", itemStack, remainTime));
          event.setCancelled(true);
          return;
        }
        if (configPlayerCooldown == null)
        {
          configPlayerCooldown = new YamlConfiguration();
        }
        configPlayerCooldown.set(cooldownTagTag, currentTime + cooldownTime);
        Variable.cooldownsItemUsage.put(uuid, configPlayerCooldown);
      }
      catch (Exception e)
      {
Cucumbery.getPlugin().getLogger().warning(        e.getMessage());
        MessageUtil.broadcastDebug("오류");
        // DO NOTHING
      }
    }
    if (usageDropTag != null)
    {
      NBTList<String> commandsTag = NBTAPI.getStringList(usageDropTag, CucumberyTag.USAGE_COMMANDS_KEY);
      if (commandsTag != null)
      {
        event.setCancelled(true);
        for (String command : commandsTag)
        {
          Method.performCommand(player, command, true, true, null);
        }
      }
      if (usageDropTag.hasTag(CucumberyTag.USAGE_DISPOSABLE_KEY))
      {
        event.setCancelled(true);
        double disposableChance = 100d;
        if (usageDropTag.hasTag(CucumberyTag.USAGE_DISPOSABLE_KEY))
        {
          disposableChance = usageDropTag.getDouble(CucumberyTag.USAGE_DISPOSABLE_KEY);
        }
        if (Math.random() * 100d < disposableChance && player.getGameMode() != GameMode.CREATIVE)
        {
          itemStack.setAmount(itemStack.getAmount() - 1);
        }
      }
    }
  }
}

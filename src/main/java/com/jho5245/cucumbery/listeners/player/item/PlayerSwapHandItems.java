package com.jho5245.cucumbery.listeners.player.item;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerSwapHandItems implements Listener
{
  @EventHandler
  public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event)
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

    // 텔레포트 망원경 이용 시 손에 든 아이템 맞바꾸기 불가
    if (CustomEffectManager.hasEffect(player, CustomEffectType.SPYGLASS_TELEPORT))
    {
      event.setCancelled(true);
      return;
    }
    if (!Cucumbery.config.getBoolean("grant-default-permission-to-players") && !Permission.EVENT_SWAPHANDITEMS.has(player))
    {
      event.setCancelled(true);
      if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerSwapHandItemsAlertCooldown.contains(uuid))
      {
        Variable.playerSwapHandItemsAlertCooldown.add(uuid);
        MessageUtil.sendTitle(player, "&c행동 불가!", "&r주로 사용하는 손에 들고 있는 아이템을 맞바꿀 권한이 없습니다", 5, 80, 15);
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerSwapHandItemsAlertCooldown.remove(uuid), 100L);
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
    this.itemSwapeUsage(event, player, player.getInventory().getItemInMainHand(), true, false);
    this.itemSwapeUsage(event, player, player.getInventory().getItemInOffHand(), false, false);
    if (player.isSneaking())
    {
      this.itemSwapeUsage(event, player, player.getInventory().getItemInMainHand(), true, true);
      this.itemSwapeUsage(event, player, player.getInventory().getItemInOffHand(), false, true);
    }
  }


  private void itemSwapeUsage(PlayerSwapHandItemsEvent event, Player player, ItemStack itemStack, boolean mainHand, boolean isSneaking)
  {
    UUID uuid = player.getUniqueId();

    NBTCompound itemTag = NBTAPI.getMainCompound(itemStack);
    NBTCompound usageTag = NBTAPI.getCompound(itemTag, CucumberyTag.USAGE_KEY);
    NBTCompound usageSwapTag = NBTAPI.getCompound(usageTag, isSneaking ? CucumberyTag.USAGE_COMMANDS_SNEAK_SWAP_KEY : CucumberyTag.USAGE_COMMANDS_SWAP_KEY);
    NBTCompound cooldownTag = NBTAPI.getCompound(usageSwapTag, CucumberyTag.COOLDOWN_KEY);
    if (usageSwapTag != null)
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
          MessageUtil.sendWarn(player, ComponentUtil.create("아직 %s을(를)" + (isSneaking ? " 웅크리고" : "") + " 스와핑 사용할 수 없습니다 (남은 시간 : %s)", itemStack, remainTime));
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
    if (usageSwapTag != null)
    {
      NBTList<String> commandsTag = NBTAPI.getStringList(usageSwapTag, CucumberyTag.USAGE_COMMANDS_KEY);
      if (commandsTag != null)
      {
        event.setCancelled(true);
        for (String command : commandsTag)
        {
          Method.performCommand(player, command, true, true, null);
        }
      }
      if (usageSwapTag.hasTag(CucumberyTag.USAGE_DISPOSABLE_KEY))
      {
        event.setCancelled(true);
        double disposableChance = 100d;
        if (usageSwapTag.hasTag(CucumberyTag.USAGE_DISPOSABLE_KEY))
        {
          disposableChance = usageSwapTag.getDouble(CucumberyTag.USAGE_DISPOSABLE_KEY);
        }
        if (Math.random() * 100d < disposableChance && player.getGameMode() != GameMode.CREATIVE)
        {
          itemStack.setAmount(itemStack.getAmount() - 1);
          if (mainHand)
          {
            player.getInventory().setItemInMainHand(itemStack);
          }
          else
          {
            player.getInventory().setItemInOffHand(itemStack);
          }
        }
      }
    }
  }
}

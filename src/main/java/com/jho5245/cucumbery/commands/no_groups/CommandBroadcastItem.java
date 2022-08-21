package com.jho5245.cucumbery.commands.no_groups;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeCooldown;
import com.jho5245.cucumbery.util.no_groups.*;
import com.jho5245.cucumbery.util.storage.component.ItemStackComponent;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.sendercomponent.SenderComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandBroadcastItem implements CucumberyCommandExecutor
{
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Cucumbery.config.getBoolean("grant-default-permission-to-players") && !Method.hasPermission(sender, Permission.CMD_BROADCASTITEM, true))
    {
      return true;
    }
    if (!(sender instanceof Player player))
    {
      MessageUtil.sendError(sender, Prefix.ONLY_PLAYER);
      return true;
    }
    String msg = "";
    if (args.length > 0)
    {
      msg = MessageUtil.listToString(" ", args);
    }
    ItemStack item = player.getInventory().getItemInMainHand().clone();
    int length = ItemSerializer.serialize(item).length();
    if (length >= 25650)
    {
      MessageUtil.sendError(sender, "해당 아이템에는 속성이 너무 많아서 채팅창에 올릴 수 없습니다");
      return true;
    }
    if (!ItemStackUtil.itemExists(item) &&
            !msg.contains("[i1]") &&
            !msg.contains("[i2]") &&
            !msg.contains("[i3]") &&
            !msg.contains("[i4]") &&
            !msg.contains("[i5]") &&
            !msg.contains("[i6]") &&
            !msg.contains("[i7]") &&
            !msg.contains("[i8]") &&
            !msg.contains("[i9]"))
    {
      MessageUtil.sendError(sender, Prefix.NO_HOLDING_ITEM);
      return true;
    }
    if (msg.equals(""))
    {
      if (CustomEffectManager.hasEffect(player, CustomEffectTypeCooldown.COOLDOWN_ITEM_MEGAPHONE))
      {
        MessageUtil.sendWarn(player, ComponentUtil.translate("아직 아이템 확성기를 사용할 수 없습니다"));
        return true;
      }
      if (!Permission.CMD_BROADCASTITEM_BYPASS.has(player) && Cucumbery.config.getBoolean("no-spam.item-megaphone.enable"))
      {
        int cooldown = Cucumbery.config.getInt("no-spam.item-megaphone.cooldown-in-ticks");
        if (cooldown > 0)
        {
          Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                  CustomEffectManager.addEffect(player, new CustomEffect(CustomEffectTypeCooldown.COOLDOWN_ITEM_MEGAPHONE, cooldown)), 0L);
        }
      }
      if (!Permission.CMD_BROADCASTITEM_BYPASS.has(player))
      {
        int cooldown = Cucumbery.config.getInt("broadcastitem-cooldown");
        if (cooldown > 0)
        {
          UUID uuid = player.getUniqueId();
          if (Variable.broadcastItemCooldown.containsKey(uuid))
          {
            long difference = Variable.broadcastItemCooldown.get(uuid) - System.currentTimeMillis();
            if (difference > 0)
            {
              MessageUtil.sendError(player, "아직 아이템 확성기를 사용할 수 없습니다. 남은 시간 : rg255,204;" + Method.timeFormatMilli(difference));
              return true;
            }
            else
            {
              Variable.broadcastItemCooldown.put(uuid, System.currentTimeMillis() + cooldown * 50L);
            }
          }
          else
          {
            Variable.broadcastItemCooldown.put(uuid, System.currentTimeMillis() + cooldown * 50L);
          }
        }
      }
      MessageUtil.broadcast(Prefix.INFO_ITEMSTORAGE, "%s이(가) %s을(를) 채팅창에 올렸습니다", player, ItemStackComponent.itemStackComponent(item, Constant.THE_COLOR));
      return true;
    }
    if (!msg.contains("[i]") && !msg.contains("[i1]") && !msg.contains("[i2]") && !msg.contains("[i3]") && !msg.contains("[i4]") && !msg.contains("[i5]") && !msg.contains("[i6]") && !msg.contains(
            "[i7]") && !msg.contains("[i8]") && !msg.contains("[i9]"))
    {
      MessageUtil.sendError(player, "아이템 설명을 적어도 1번은 넣어야 합니다. [i]를 입력하세요.");
      return true;
    }
    if (!msg.contains("[i1]") && !msg.contains("[i2]") && !msg.contains("[i3]") && !msg.contains("[i4]") && !msg.contains("[i5]") && !msg.contains("[i6]") && !msg.contains("[i7]") && !msg.contains(
            "[i8]") && !msg.contains("[i9]"))
    {
      if (CustomEffectManager.hasEffect(player, CustomEffectTypeCooldown.COOLDOWN_ITEM_MEGAPHONE))
      {
        MessageUtil.sendWarn(player, ComponentUtil.translate("아직 아이템 확성기를 사용할 수 없습니다"));
        return true;
      }
      if (!Permission.CMD_BROADCASTITEM_BYPASS.has(player) && Cucumbery.config.getBoolean("no-spam.item-megaphone.enable"))
      {
        int cooldown = Cucumbery.config.getInt("no-spam.item-megaphone.cooldown-in-ticks");
        if (cooldown > 0)
        {
          Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                  CustomEffectManager.addEffect(player, new CustomEffect(CustomEffectTypeCooldown.COOLDOWN_ITEM_MEGAPHONE, cooldown)), 0L);
        }
      }
      if (!Permission.CMD_BROADCASTITEM_BYPASS.has(player))
      {
        int cooldown = Cucumbery.config.getInt("broadcastitem-cooldown");
        if (cooldown > 0)
        {
          UUID uuid = player.getUniqueId();
          if (Variable.broadcastItemCooldown.containsKey(uuid))
          {
            long difference = Variable.broadcastItemCooldown.get(uuid) - System.currentTimeMillis();
            if (difference > 0)
            {
              MessageUtil.sendError(player, "아직 아이템 확성기를 사용할 수 없습니다. 남은 시간 : rg255,204;" + Method.timeFormatMilli(difference));
              return true;
            }
            else
            {
              Variable.broadcastItemCooldown.put(uuid, System.currentTimeMillis() + cooldown * 50L);
            }
          }
          else
          {
            Variable.broadcastItemCooldown.put(uuid, System.currentTimeMillis() + cooldown * 50L);
          }
        }
      }
      MessageUtil.broadcast(ComponentUtil.translate("chat.type.text",
              false,
              SenderComponentUtil.senderComponent(player, NamedTextColor.WHITE),
              ComponentUtil.translate(msg.replace("[i]", "%1$s"), false, ItemStackComponent.itemStackComponent(item, Constant.THE_COLOR))));
    }
    else
    {
      Inventory inv = player.getInventory();
      ItemStack item1 = inv.getItem(0);
      ItemStack item2 = inv.getItem(1);
      ItemStack item3 = inv.getItem(2);
      ItemStack item4 = inv.getItem(3);
      ItemStack item5 = inv.getItem(4);
      ItemStack item6 = inv.getItem(5);
      ItemStack item7 = inv.getItem(6);
      ItemStack item8 = inv.getItem(7);
      ItemStack item9 = inv.getItem(8);
      for (int i = 1; i <= 9; i++)
      {
        if (msg.contains("[i" + i + "]") && !ItemStackUtil.itemExists(inv.getItem(i - 1)))
        {
          MessageUtil.sendError(player, ComponentUtil.translate("%s에 아이템을 들고 있지 않습니다", ComponentUtil.translate("rg255,204;%s번째 단축바 슬롯", i)));
          return true;
        }
      }
      if (CustomEffectManager.hasEffect(player, CustomEffectTypeCooldown.COOLDOWN_ITEM_MEGAPHONE))
      {
        MessageUtil.sendWarn(player, ComponentUtil.translate("아직 아이템 확성기를 사용할 수 없습니다"));
        return true;
      }
      if (!Permission.CMD_BROADCASTITEM_BYPASS.has(player) && Cucumbery.config.getBoolean("no-spam.item-megaphone.enable"))
      {
        int cooldown = Cucumbery.config.getInt("no-spam.item-megaphone.cooldown-in-ticks");
        if (cooldown > 0)
        {
          Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                  CustomEffectManager.addEffect(player, new CustomEffect(CustomEffectTypeCooldown.COOLDOWN_ITEM_MEGAPHONE, cooldown)), 0L);
        }
      }
      if (!Permission.CMD_BROADCASTITEM_BYPASS.has(player))
      {
        int cooldown = Cucumbery.config.getInt("broadcastitem-cooldown");
        if (cooldown > 0)
        {
          UUID uuid = player.getUniqueId();
          if (Variable.broadcastItemCooldown.containsKey(uuid))
          {
            long difference = Variable.broadcastItemCooldown.get(uuid) - System.currentTimeMillis();
            if (difference > 0)
            {
              MessageUtil.sendError(player, "아직 아이템 확성기를 사용할 수 없습니다. 남은 시간 : rg255,204;" + Method.timeFormatMilli(difference));
              return true;
            }
            else
            {
              Variable.broadcastItemCooldown.put(uuid, System.currentTimeMillis() + cooldown * 50L);
            }
          }
          else
          {
            Variable.broadcastItemCooldown.put(uuid, System.currentTimeMillis() + cooldown * 50L);
          }
        }
      }
      Component prefix = ComponentUtil.create(null, player, " : ");
      List<Component> txt = new ArrayList<>();
      txt.add(prefix);
      StringBuilder builder = new StringBuilder();
      for (int i = 0; i < msg.length(); i++)
      {
        char c = msg.charAt(i);
        boolean isItemHover = false;
        try
        {
          char c2 = msg.charAt(i + 1);
          char c3 = msg.charAt(i + 2);
          char c4 = msg.charAt(i + 3);
          isItemHover = c2 == 'i' && c4 == ']' && c3 >= '1' && c3 <= '9';
        }
        catch (Exception ignored)
        {
        }
        if (c == '[' && isItemHover)
        {
          if (++i >= msg.length())
          {
            break;
          }
          if (!builder.isEmpty())
          {
            txt.add(ComponentUtil.create(false, builder.toString()));
            builder = new StringBuilder();
          }
          if (i + 2 < msg.length())
          {
            StringBuilder itemHover = new StringBuilder("[");
            for (int j = 0; j < 3; j++)
            {
              itemHover.append(msg.charAt(i + j));
            }
            switch (itemHover.toString())
            {
              case "[i1]" ->
              {
                if (item1 != null)
                {
                  txt.add(ItemStackComponent.itemStackComponent(item1, Constant.THE_COLOR));
                }
              }
              case "[i2]" ->
              {
                if (item2 != null)
                {
                  txt.add(ItemStackComponent.itemStackComponent(item2, Constant.THE_COLOR));
                }
              }
              case "[i3]" ->
              {
                if (item3 != null)
                {
                  txt.add(ItemStackComponent.itemStackComponent(item3, Constant.THE_COLOR));
                }
              }
              case "[i4]" ->
              {
                if (item4 != null)
                {
                  txt.add(ItemStackComponent.itemStackComponent(item4, Constant.THE_COLOR));
                }
              }
              case "[i5]" ->
              {
                if (item5 != null)
                {
                  txt.add(ItemStackComponent.itemStackComponent(item5, Constant.THE_COLOR));
                }
              }
              case "[i6]" ->
              {
                if (item6 != null)
                {
                  txt.add(ItemStackComponent.itemStackComponent(item6, Constant.THE_COLOR));
                }
              }
              case "[i7]" ->
              {
                if (item7 != null)
                {
                  txt.add(ItemStackComponent.itemStackComponent(item7, Constant.THE_COLOR));
                }
              }
              case "[i8]" ->
              {
                if (item8 != null)
                {
                  txt.add(ItemStackComponent.itemStackComponent(item8, Constant.THE_COLOR));
                }
              }
              case "[i9]" ->
              {
                if (item9 != null)
                {
                  txt.add(ItemStackComponent.itemStackComponent(item9, Constant.THE_COLOR));
                }
              }
            }
            i += 2;
          }
        }
        else
        {
          builder.append(c);
        }
      }
      txt.add(ComponentUtil.create(false, builder.toString()));
      MessageUtil.broadcast(txt, false);
    }

    return true;
  }

  @Override
  public @NotNull List<Completion> completion(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args, @NotNull Location location)
  {
    if (!(sender instanceof Player player))
    {
      return Collections.emptyList();
    }
    PlayerInventory inventory = player.getInventory();
    List<String> list = new ArrayList<>();
    String arg = MessageUtil.listToString(args);
    boolean empty = true;
    for (int i = 0; i < 9; i++)
    {
      ItemStack item = inventory.getItem(i);
      if (ItemStackUtil.itemExists(item))
      {
        list.add("[i" + (i + 1) + "]");
        empty = false;
      }
      else if (arg.contains("[i" + (i + 1) + "]"))
      {
        return CommandTabUtil.errorMessage("%s번째 단축바 슬롯에 아이템을 들고 있지 않습니다", i + 1);
      }
    }
    if (empty)
    {
      return CommandTabUtil.errorMessage("주로 사용하는 손 또는 단축바 슬롯에 아이템을 들고 있지 않습니다");
    }
    ItemStack maindHand = inventory.getItemInMainHand();
    if (ItemStackUtil.itemExists(maindHand))
    {
      list.add("[i]");
    }
    else if (arg.contains("[i]"))
    {
      return CommandTabUtil.errorMessage(Prefix.NO_HOLDING_ITEM.toString());
    }
    list.add("[메시지]");
    return CommandTabUtil.tabCompleterList(args, list, "[메시지]", true);
  }
}

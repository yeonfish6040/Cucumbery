package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.ItemSerializer;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.MessageUtil.ConsonantType;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BroadcastAndMessageItem implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (cmd.getName().equalsIgnoreCase("broadcastitem"))
    {
      if (!Cucumbery.config.getBoolean("grant-default-permission-to-players") && !Method.hasPermission(sender, Permission.CMD_BROADCASTITEM, true))
      {
        return true;
      }
      if (!(sender instanceof Player))
      {
        MessageUtil.sendError(sender, Prefix.ONLY_PLAYER);
        return true;
      }
      Player player = (Player) sender;
      String msg = "";
      if (args.length > 0)
      {
        msg = MessageUtil.listToString(" ", args);
      }
      ItemStack item = player.getInventory().getItemInMainHand().clone();
      if (ItemStackUtil.itemExists(item))
      {
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.removeKey("BlockEntityTag");
        nbtItem.removeKey("BlockStateTag");
        item = nbtItem.getItem();
      }
      int length = ItemSerializer.serialize(item).length();
      if (length >= 25650)
      {
        MessageUtil.sendError(sender, "해당 아이템에는 속성이 너무 많아서 채팅창에 올릴 수 없습니다.");
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
      int amount = item.getAmount();
      if (msg.equals(""))
      {
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
                MessageUtil.sendError(player, "아직 아이템 확성기를 사용할 수 없습니다. 남은 시간 : &e" + Method.timeFormatMilli(difference));
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
        Component txt = ComponentUtil.create(Prefix.INFO_ITEMSTORAGE, "§e", ComponentUtil.senderComponent(player), "§r이(가) ", item, "을(를) 채팅창에 올렸습니다.");
        for (Player online : Bukkit.getServer().getOnlinePlayers())
        {
          MessageUtil.sendMessage(online, txt);
        }
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
                MessageUtil.sendError(player, "아직 아이템 확성기를 사용할 수 없습니다. 남은 시간 : &e" + Method.timeFormatMilli(difference));
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
        String itemString = ComponentUtil.itemName(item) + ((amount == 1) ? "" : " " + amount + "개") + "§r";
        Component txt = ComponentUtil.create(Prefix.INFO_ITEMSTORAGE, player, " : ", ComponentUtil.createTranslate(msg.replace("[i]", "%s"), item));
        for (Player online : Bukkit.getServer().getOnlinePlayers())
        {
          MessageUtil.sendMessage(online, false, txt);
        }
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
        if (msg.contains("[i1]") && !ItemStackUtil.itemExists(item1))
        {
          MessageUtil.sendError(player, "&e1번째 &r단축바 슬롯에 아이템을 들고 있지 않습니다.");
          return true;
        }
        if (msg.contains("[i2]") && !ItemStackUtil.itemExists(item2))
        {
          MessageUtil.sendError(player, "&e2번째 &r단축바 슬롯에 아이템을 들고 있지 않습니다.");
          return true;
        }
        if (msg.contains("[i3]") && !ItemStackUtil.itemExists(item3))
        {
          MessageUtil.sendError(player, "&e3번째 &r단축바 슬롯에 아이템을 들고 있지 않습니다.");
          return true;
        }
        if (msg.contains("[i4]") && !ItemStackUtil.itemExists(item4))
        {
          MessageUtil.sendError(player, "&e4번째 &r단축바 슬롯에 아이템을 들고 있지 않습니다.");
          return true;
        }
        if (msg.contains("[i5]") && !ItemStackUtil.itemExists(item5))
        {
          MessageUtil.sendError(player, "&e5번째 &r단축바 슬롯에 아이템을 들고 있지 않습니다.");
          return true;
        }
        if (msg.contains("[i6]") && !ItemStackUtil.itemExists(item6))
        {
          MessageUtil.sendError(player, "&e6번째 &r단축바 슬롯에 아이템을 들고 있지 않습니다.");
          return true;
        }
        if (msg.contains("[i7]") && !ItemStackUtil.itemExists(item7))
        {
          MessageUtil.sendError(player, "&e7번째 &r단축바 슬롯에 아이템을 들고 있지 않습니다.");
          return true;
        }
        if (msg.contains("[i8]") && !ItemStackUtil.itemExists(item8))
        {
          MessageUtil.sendError(player, "&e8번째 &r단축바 슬롯에 아이템을 들고 있지 않습니다.");
          return true;
        }
        if (msg.contains("[i9]") && !ItemStackUtil.itemExists(item9))
        {
          MessageUtil.sendError(player, "&e9번째 &r단축바 슬롯에 아이템을 들고 있지 않습니다.");
          return true;
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
                MessageUtil.sendError(player, "아직 아이템 확성기를 사용할 수 없습니다. 남은 시간 : &e" + Method.timeFormatMilli(difference));
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
        Component prefix = ComponentUtil.create(Prefix.INFO_ITEMSTORAGE, player, " : ");
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
            if (builder.length() > 0)
            {
              txt.add(ComponentUtil.create(builder.toString()));
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
                case "[i1]" -> txt.add(ComponentUtil.create(item1));
                case "[i2]" -> txt.add(ComponentUtil.create(item2));
                case "[i3]" -> txt.add(ComponentUtil.create(item3));
                case "[i4]" -> txt.add(ComponentUtil.create(item4));
                case "[i5]" -> txt.add(ComponentUtil.create(item5));
                case "[i6]" -> txt.add(ComponentUtil.create(item6));
                case "[i7]" -> txt.add(ComponentUtil.create(item7));
                case "[i8]" -> txt.add(ComponentUtil.create(item8));
                case "[i9]" -> txt.add(ComponentUtil.create(item9));
              }
              i += 2;
            }
          }
          else
          {
            builder.append(c);
          }
        }
        txt.add(ComponentUtil.create(builder.toString()));
        for (Player online : Bukkit.getServer().getOnlinePlayers())
        {
          MessageUtil.sendMessage(online, txt);
        }
      }
    }
    else if (cmd.getName().equalsIgnoreCase("messageitem"))
    {
      if (!Method.hasPermission(sender, Permission.CMD_MESSAGEITEM, true))
      {
        return true;
      }
      if (!(sender instanceof Player))
      {
        MessageUtil.sendError(sender, Prefix.ONLY_PLAYER);
        return true;
      }
      if (args.length == 0)
      {
        MessageUtil.shortArg(sender, 1, args);
        MessageUtil.commandInfo(sender, label, "<플레이어 ID>");
        return true;
      }
      if (args.length > 1)
      {
        MessageUtil.longArg(sender, 1, args);
        MessageUtil.commandInfo(sender, label, "<플레이어 ID>");
        return true;
      }
      Player player = (Player) sender;
      ItemStack item = player.getInventory().getItemInMainHand();
      if (!ItemStackUtil.itemExists(item))
      {
        MessageUtil.sendError(sender, Prefix.NO_HOLDING_ITEM);
        return true;
      }
      NBTItem nbtItem = new NBTItem(item);
      nbtItem.removeKey("BlockEntityTag");
      nbtItem.removeKey("BlockStateTag");
      item = nbtItem.getItem();
      Player target = Method.getPlayer(sender, args[0]);
      if (target == null)
      {
        return true;
      }
      int amount = item.getAmount();

      String itemString = ComponentUtil.itemName(item) + ((amount == 1) ? "" : " " + amount + "개") + "§r";
      String consonant = MessageUtil.getFinalConsonant(itemString, ConsonantType.을_를);
      itemString += consonant;
      Component txt = ComponentUtil.create(Prefix.INFO_ITEMSTORAGE + "§e" + ComponentUtil.senderComponent(player) + "§r이(가) 당신에게 " + itemString + " 보여줍니다.", item);
      target.sendMessage(txt);
      txt = ComponentUtil.create(Prefix.INFO_ITEMSTORAGE + "§e" + target.getDisplayName() + "§r에게 " + itemString + " 보여주었습니다.", item);
      player.sendMessage(txt);
      return true;
    }
    return true;
  }
}

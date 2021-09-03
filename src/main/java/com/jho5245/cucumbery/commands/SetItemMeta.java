package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.util.ItemSerializer;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.MessageUtil.ConsonantType;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTList;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SetItemMeta implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_SETDATA, true))
    {
      return true;
    }
    if (!(sender instanceof Player player))
    {
      MessageUtil.sendError(sender, Prefix.ONLY_PLAYER);
      return true;
    }
    ItemStack item = player.getInventory().getItemInMainHand();
    if (!ItemStackUtil.itemExists(item))
    {
      MessageUtil.sendError(sender, Prefix.NO_HOLDING_ITEM);
      return true;
    }
    ItemMeta itemMeta = item.getItemMeta();
    String usage = cmd.getUsage().replace("/<command> ", "");
    if (cmd.getName().equals("setname"))
    {
      String name = MessageUtil.n2s(MessageUtil.listToString(" ", args), MessageUtil.N2SType.SPECIAL_ONLY);
      boolean silent = name.contains("--silent");
      if (silent)
      {
        name = name.replaceFirst("--silent", "");
      }
      if (args.length == 0)
      {
        MessageUtil.shortArg(sender, 1, args);
        MessageUtil.commandInfo(sender, label, usage);
        return true;
      }
      else
      {
        if (args.length == 1 && args[0].equals("--remove"))
        {
          itemMeta.displayName(null);
          item.setItemMeta(itemMeta);
          player.getInventory().setItemInMainHand(item);
          if (!silent)
          {
            Component txt = ComponentUtil.create(Prefix.INFO_SETDATA + "주로 사용하는 손에 들고 있는 아이템의 이름을 제거하였습니다.", item);
            MessageUtil.sendMessage(player, txt);
          }
        }
        else
        {
          if (name.contains("--json"))
          {
            name = name.replaceFirst("--json", "");
            NBTItem nbtItem = new NBTItem(item);
            NBTCompound display = nbtItem.addCompound("display");
            display.setString("Name", name);
            item = nbtItem.getItem();
          }
          else
          {
            itemMeta.displayName(ComponentUtil.create(name));
            item.setItemMeta(itemMeta);
          }
          player.getInventory().setItemInMainHand(item);
          Method.updateInventory(player);
          if (!silent)
          {
            Component itemName = ComponentUtil.itemName(item);
            String itemNameSerial = ComponentUtil.serialize(itemName);
            MessageUtil.sendMessage(player,Prefix.INFO_SETDATA + "주로 사용하는 손에 들고 있는 아이템의 이름을 ", item, MessageUtil.getFinalConsonant(itemNameSerial, ConsonantType.으로) + " 설정하였습니다." );
          }
        }
      }
    }
    else if (cmd.getName().equals("setname2"))
    {
      if (args.length == 0)
      {
        MessageUtil.shortArg(sender, 1, args);
        MessageUtil.commandInfo(sender, label, usage);
        return true;
      }
      else
      {
        if (args.length == 1 && args[0].startsWith("--") && (MessageUtil.isInteger(sender, args[0].substring(2), false) || args[0].equals("--")))
        {
          int subString;
          if (args[0].equals("--"))
          {
            subString = 1;
          }
          else
          {
            subString = Integer.parseInt(args[0].substring(2));
          }
          if (!MessageUtil.checkNumberSize(sender, subString, 1, Integer.MAX_VALUE))
          {
            return true;
          }
          String displayName = ComponentUtil.itemName(item).toString();
          if (subString >= displayName.length())
          {
            itemMeta.displayName(null);
            item.setItemMeta(itemMeta);
            player.getInventory().setItemInMainHand(item);
            Component txt = ComponentUtil.create(Prefix.INFO_SETDATA + "주로 사용하는 손에 들고 있는 아이템의 이름을 제거하였습니다.", item);
            MessageUtil.sendMessage(player, txt);
          }
          else
          {
            displayName = displayName.substring(0, displayName.length() - subString);
            itemMeta.displayName(ComponentUtil.create(displayName));
            item.setItemMeta(itemMeta);
            player.getInventory().setItemInMainHand(item);
            MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템의 이름을 &e" + subString + "글자&r만큼 제거하였습니다.");
          }
        }
        else
        {
          String name = MessageUtil.n2s(MessageUtil.listToString(" ", args), MessageUtil.N2SType.SPECIAL_ONLY);
          itemMeta.displayName(itemMeta.displayName().append(ComponentUtil.create(name)));
          item.setItemMeta(itemMeta);
          player.getInventory().setItemInMainHand(item);
          Component txt = ComponentUtil.create(Prefix.INFO_SETDATA + "주로 사용하는 손에 들고 있는 아이템에 §r" + name + "§r" + MessageUtil.getFinalConsonant(MessageUtil.stripColor(name), ConsonantType.이라) + "는 이름을 추가로 " +
                          "입력하였습니다.",
                  item);
          MessageUtil.sendMessage(player, txt);
        }
      }
    }
    else if (cmd.getName().equals("setlore"))
    {
      if (args.length < 2)
      {
        MessageUtil.shortArg(sender, 2, args);
        MessageUtil.commandInfo(sender, label, usage);
        return true;
      }
      else
      {
        if (!MessageUtil.isInteger(sender, args[0], true))
        {
          return true;
        }
        int index = Integer.parseInt(args[0]);
        if (!MessageUtil.checkNumberSize(player, index, 1, Integer.MAX_VALUE))
        {
          return true;
        }
        List<String> lore = itemMeta.getLore();
        if (lore == null)
        {
          lore = new ArrayList<>();
          for (int i = 0; i < index; i++)
          {
            lore.add("");
          }
        }
        else if (lore.size() < index)
        {
          for (int i = lore.size(); i < index; i++)
          {
            lore.add("");
          }
        }
        if (args.length == 2 && args[1].equals("--empty"))
        {
          lore.set(index - 1, "");
          itemMeta.setLore(lore);
          item.setItemMeta(itemMeta);
          Component txt = ComponentUtil.create(Prefix.INFO_SETDATA + "주로 사용하는 손에 들고 있는 아이템의 &e" + index + "번째&r 줄의 설명을 공백으로 설정하였습니다.", ItemSerializer.serialize(item));
          MessageUtil.sendMessage(player, txt);
        }
        else
        {
          String input = MessageUtil.n2s(MessageUtil.listToString(" ", 1, args.length, args), MessageUtil.N2SType.SPECIAL_ONLY);
          lore.set(index - 1, input);
          itemMeta.setLore(lore);
          item.setItemMeta(itemMeta);
          Component txt = ComponentUtil.create(
                  Prefix.INFO_SETDATA + "주로 사용하는 손에 들고 있는 아이템의 §e" + index + "번째§r 줄의 설명을 §e" + input + "§r" + MessageUtil.getFinalConsonant(input, ConsonantType.으로) + " 설정하였습니다.", item);
          MessageUtil.sendMessage(player, txt);
        }
        player.getInventory().setItemInMainHand(item);
      }
    }
    else if (cmd.getName().equals("setlore2"))
    {
      if (args.length < 2)
      {
        MessageUtil.shortArg(sender, 2, args);
        MessageUtil.commandInfo(sender, label, usage);
        return true;
      }
      else
      {
        if (!MessageUtil.isInteger(sender, args[0], true))
        {
          return true;
        }
        int index = Integer.parseInt(args[0]);
        if (!MessageUtil.checkNumberSize(player, index, 1, Integer.MAX_VALUE))
        {
          return true;
        }
        List<String> lores = itemMeta.getLore();
        if (lores == null)
        {
          lores = new ArrayList<>();
          for (int i = 0; i < index; i++)
          {
            lores.add("");
          }
        }
        else if (lores.size() < index)
        {
          for (int i = lores.size(); i < index; i++)
          {
            lores.add("");
          }
        }
        if (args.length == 2 && args[1].startsWith("--") && (MessageUtil.isInteger(sender, args[1].substring(2), false) || args[1].equals("--")))
        {
          int subString;
          if (args[1].equals("--"))
          {
            subString = 1;
          }
          else
          {
            subString = Integer.parseInt(args[1].substring(2));
          }
          if (!MessageUtil.checkNumberSize(sender, subString, 1, Integer.MAX_VALUE))
          {
            return true;
          }
          String lore = lores.get(index - 1);
          if (subString >= lore.length())
          {
            lores.remove(index - 1);
            itemMeta.setLore(lores);
            item.setItemMeta(itemMeta);
            player.getInventory().setItemInMainHand(item);
            Component txt = ComponentUtil.create(Prefix.INFO_SETDATA + "주로 사용하는 손에 들고 있는 아이템의 &e" + index + "번째&r 줄의 설명을 제거하였습니다.", item);
            MessageUtil.sendMessage(player, txt);
          }
          else
          {
            lore = lore.substring(0, lore.length() - subString);
            lores.set(index - 1, lore);
            itemMeta.setLore(lores);
            item.setItemMeta(itemMeta);
            player.getInventory().setItemInMainHand(item);
            Component txt = ComponentUtil.create(Prefix.INFO_SETDATA + "주로 사용하는 손에 들고 있는 아이템의 &e" + index + "번째&r 줄의 설명을 &e" + subString + "글자&r만큼 제거하였습니다.", item);
            MessageUtil.sendMessage(player, txt);
          }
        }
        else
        {
          String input = MessageUtil.n2s(MessageUtil.listToString(" ", 1, args.length, args), MessageUtil.N2SType.SPECIAL_ONLY);
          lores.set(index - 1, lores.get(index - 1) + input);
          itemMeta.setLore(lores);
          item.setItemMeta(itemMeta);
          player.getInventory().setItemInMainHand(item);
          Component txt = ComponentUtil.create(
                  Prefix.INFO_SETDATA + "주로 사용하는 손에 들고 있는 아이템의 §e" + index + "번째§r 줄의 설명에 §e" + input + "§r" + MessageUtil.getFinalConsonant(MessageUtil.stripColor(input), ConsonantType.이라) + "는 설명을 추가로 입력하였습니다.",
                  item);
          MessageUtil.sendMessage(player, txt);
        }
      }
    }
    else if (cmd.getName().equals("addlore"))
    {
      if (args.length == 0)
      {
        MessageUtil.shortArg(sender, 1, args);
        MessageUtil.commandInfo(sender, label, usage);
        return true;
      }
      else
      {
        List<String> lore = itemMeta.getLore();
        if (lore == null)
        {
          lore = new ArrayList<>();
        }
        if (args.length == 1 && args[0].equals("--empty"))
        {
          lore.add("");
          itemMeta.setLore(lore);
          item.setItemMeta(itemMeta);
          Component txt = ComponentUtil.create(Prefix.INFO_SETDATA + "주로 사용하는 손에 들고 있는 아이템의 &e" + lore.size() + "번째&r 줄에 공백 설명을 추가하였습니다.", item);
          MessageUtil.sendMessage(player, txt);
        }
        else
        {
          String input = MessageUtil.n2s(MessageUtil.listToString(" ", args), MessageUtil.N2SType.SPECIAL_ONLY);
          if (input.endsWith(" --json"))
          {
            input = input.substring(0, input.length() - 7);
            NBTItem nbtItem = new NBTItem(item);
            NBTCompound display = nbtItem.addCompound("display");
            NBTList<String> loreCompound = display.getStringList("Lore");
            loreCompound.add(input);
            lore.add(input);
            item = nbtItem.getItem();
          }
          else
          {
            lore.add(input);
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);
          }
          Component txt = ComponentUtil.create(
                  Prefix.INFO_SETDATA + "주로 사용하는 손에 들고 있는 아이템의 §e" + lore.size() + "번째§r 줄에 §e" + input + "§r" + MessageUtil.getFinalConsonant(MessageUtil.stripColor(input), ConsonantType.이라) + "는 설명을 추가하였습니다.", item);
          MessageUtil.sendMessage(player, txt);
        }
        player.getInventory().setItemInMainHand(item);
      }
    }
    else if (cmd.getName().equals("deletelore"))
    {
      List<String> lores = itemMeta.getLore();
      if (lores == null)
      {
        MessageUtil.sendError(player, "더 이상 제거할 수 있는 설명이 없습니다.");
        return true;
      }
      if (args.length == 0)
      {
        lores.remove(lores.size() - 1);
        itemMeta.setLore(lores);
        item.setItemMeta(itemMeta);
        Component txt = ComponentUtil.create(Prefix.INFO_SETDATA + "주로 사용하는 손에 들고 있는 아이템의 &e" + ((lores.size() != 0) ? (lores.size() + 1) + "번째" : "모든") + "&r 줄의 설명을 제거하였습니다.", item);
        MessageUtil.sendMessage(player, txt);
        player.getInventory().setItemInMainHand(item);
      }
      else if (args.length == 1)
      {
        if (args[0].equals("--all"))
        {
          itemMeta.setLore(null);
          item.setItemMeta(itemMeta);
          player.getInventory().setItemInMainHand(item);
          Component txt = ComponentUtil.create(Prefix.INFO_SETDATA + "주로 사용하는 손에 들고 있는 아이템의 &e모든&r 줄의 설명을 제거하였습니다.", item);
          MessageUtil.sendMessage(player, txt);
          return true;
        }
        if (!MessageUtil.isInteger(sender, args[0], true))
        {
          return true;
        }
        int index = Integer.parseInt(args[0]);
        if (!MessageUtil.checkNumberSize(sender, index, 1, lores.size()))
        {
          return true;
        }
        lores.remove(index - 1);
        itemMeta.setLore(lores);
        item.setItemMeta(itemMeta);
        player.getInventory().setItemInMainHand(item);
        Component txt = ComponentUtil.create(Prefix.INFO_SETDATA + "주로 사용하는 손에 들고 있는 아이템의 &e" + ((lores.size() != 0) ? index + "번째" : "모든") + "&r 줄의 설명을 제거하였습니다.", item);
        MessageUtil.sendMessage(player, txt);
      }
      else
      {
        MessageUtil.longArg(sender, 4, args);
        MessageUtil.commandInfo(sender, label, usage);
      }
    }
    else if (cmd.getName().equals("insertlore"))
    {
      if (args.length < 2)
      {
        MessageUtil.shortArg(sender, 2, args);
        MessageUtil.commandInfo(sender, label, usage);
        return true;
      }
      if (!MessageUtil.isInteger(sender, args[0], true))
      {
        return true;
      }
      List<String> lore = itemMeta.getLore();
      if (lore == null)
      {
        MessageUtil.sendError(sender, "설명을 들여쓸 수 없습니다.");
        return true;
      }
      int index = Integer.parseInt(args[0]);
      if (!MessageUtil.checkNumberSize(sender, index, 1, lore.size()))
      {
        return true;
      }
      List<String> temp = new ArrayList<>();
      for (int i = 0; i < index - 1; i++)
      {
        temp.add(lore.get(i));
      }
      boolean empty = false;
      String input = MessageUtil.n2s(MessageUtil.listToString(" ", 1, args.length, args), MessageUtil.N2SType.SPECIAL_ONLY);
      if (args[1].equals("--empty"))
      {
        temp.add("");
        empty = true;
      }
      else
      {
        temp.add(input);
      }
      for (int i = index - 1; i < lore.size(); i++)
      {
        temp.add(lore.get(i));
      }
      itemMeta.setLore(temp);
      item.setItemMeta(itemMeta);
      player.getInventory().setItemInMainHand(item);
      Component txt;
      if (empty)
      {
        txt = ComponentUtil.create(Prefix.INFO_SETDATA + "주로 사용하는 손에 들고 있는 아이템의 &e" + index + "번째&r 줄에 공백 설명을 들여썼습니다.", item);
      }
      else
      {
        txt = ComponentUtil.create(Prefix.INFO_SETDATA + "주로 사용하는 손에 들고 있는 아이템의 §e" + index + "번째§r 줄에 §e" + input + "§r" + MessageUtil.getFinalConsonant(MessageUtil.stripColor(input), ConsonantType.이라) + "는 설명을 " +
                        "들여썼습니다.",
                item);
      }
      MessageUtil.sendMessage(player, txt);
    }
    else if (cmd.getName().equals("setrepaircost"))
    {
      if (args.length == 0)
      {
        MessageUtil.shortArg(sender, 1, args);
        MessageUtil.commandInfo(sender, label, usage);
        return true;
      }
      else if (args.length <= 2)
      {
        if (!MessageUtil.isInteger(sender, args[0], true))
        {
          return true;
        }
        int repairCost = Integer.parseInt(args[0]);
        if (!MessageUtil.checkNumberSize(sender, repairCost, 0, 30))
        {
          return true;
        }
        ((Repairable) itemMeta).setRepairCost((int) Math.pow(2, repairCost) - 1);
        item.setItemMeta(itemMeta);
        player.getInventory().setItemInMainHand(item);
        Method.updateInventory(player);
        if (!MessageUtil.isBoolean(sender, args, 2, true))
        {
          return true;
        }
        boolean hideOutput = args.length == 2 && Boolean.parseBoolean(args[1]);
        if (!hideOutput)
        {
          Component txt = ComponentUtil.create(Prefix.INFO_SETDATA + "주로 사용하는 손에 들고 있는 아이템의 모루 사용 횟수를 &e" + repairCost + "번&r으로 설정하였습니다.", item);
          MessageUtil.sendMessage(player, txt);
        }
      }
      else
      {
        MessageUtil.longArg(sender, 2, args);
        MessageUtil.commandInfo(sender, label, usage);
        return true;
      }
    }
    return true;
  }
}

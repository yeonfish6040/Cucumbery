package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.MessageUtil.ConsonantType;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class SetData implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_SETDATA, true))
    {
      return true;
    }
    String usage = cmd.getUsage().replace("/<command> ", "");
    if (args.length < 2)
    {
      if (sender instanceof Player)
      {
        MessageUtil.shortArg(sender, 2, args);
        MessageUtil.commandInfo(sender, label, usage);
        return true;
      }
      MessageUtil.shortArg(sender, 3, args);
      MessageUtil.commandInfo(sender, label, usage.replace("[플레이어 ID]", "<플레이어 ID>"));
    }
    else if (args.length <= 4)
    {
      if (args.length == 2)
      {
        if (!(sender instanceof Player player))
        {
          MessageUtil.shortArg(sender, 3, args);
          MessageUtil.commandInfo(sender, label, usage.replace("[플레이어 ID]", "<플레이어 ID>"));
          return true;
        }
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!ItemStackUtil.itemExists(item) && !args[0].equals("material"))
        {
          MessageUtil.sendError(sender, Prefix.NO_HOLDING_ITEM);
          return true;
        }
        switch (args[0])
        {
          case "amount":
            ItemStack clone = item.clone();
            if (!MessageUtil.isInteger(sender, args[1], true))
            {
              return true;
            }
            int amount = Integer.parseInt(args[1]);
            if (!MessageUtil.checkNumberSize(sender, amount, 0, 127))
            {
              return true;
            }
            clone.setAmount(amount);
            player.getInventory().setItemInMainHand(clone);
            Component txt = ComponentUtil.create(Prefix.INFO_SETDATA + "주로 사용하는 손에 들고 있는 아이템의 개수를 &e" + amount + "개&r로 설정하였습니다.", item);
            player.sendMessage(txt);
            return true;
          case "durability":
            clone = item.clone();
            if (!MessageUtil.isInteger(sender, args[1], true))
            {
              return true;
            }
            int durability = Integer.parseInt(args[1]);
            if (!MessageUtil.checkNumberSize(sender, durability, 0, 32767))
            {
              return true;
            }
            Damageable duraMeta = (Damageable) clone.getItemMeta();
            duraMeta.setDamage(durability);
            clone.setItemMeta((ItemMeta) duraMeta);
            player.getInventory().setItemInMainHand(clone);
            Method.updateInventory(player);
            txt = ComponentUtil.create(Prefix.INFO_SETDATA + "주로 사용하는 손에 들고 있는 아이템의 내구도를 &e" + durability + "&r으로 설정하였습니다.", item);
            player.sendMessage(txt);
            return true;
          case "material":
            Material type;
            try
            {
              type = Material.valueOf(args[1].toUpperCase());
            }
            catch (Exception e)
            {
              MessageUtil.noArg(sender, Prefix.NO_MATERIAL, args[1]);
              return true;
            }
            if (!type.isItem() || type == Material.AIR)
            {
              MessageUtil.noArg(sender, Prefix.NO_OBTAINABLE_MATERIAL, args[1]);
              return true;
            }
            if (ItemStackUtil.itemExists(item))
            {
              clone = item.clone();
              clone.setType(type);
            }
            else
            {
              clone = new ItemStack(type);
            }
            player.getInventory().setItemInMainHand(clone);
            if (Cucumbery.config.getBoolean("use-helpful-lore-feature"))
            {
              String worldName = player.getLocation().getWorld().getName();
              if (!Cucumbery.config.getStringList("no-use-helpful-lore-feature-worlds").contains(worldName))
              {
                ItemLore.setItemLore(player.getInventory().getItemInMainHand());
              }
            }
            if (!ItemStackUtil.itemExists(player.getInventory().getItemInMainHand()))
            {
              player.getInventory().setItemInMainHand(item);
              MessageUtil.sendWarn(player, "존재할 수 없는 물질이여서 변경이 취소되었습니다. (&e" + ComponentUtil.itemName(type) + "&r)");
              return true;
            }
            if (ItemStackUtil.itemExists(item))
            {
              txt = ComponentUtil.create(
                      Prefix.INFO_SETDATA + "주로 사용하는 손에 들고 있는 아이템의 종류를 &e" + ComponentUtil.itemName(type) + "&r" + MessageUtil.getFinalConsonant(ComponentUtil.itemName(type).toString(), ConsonantType.으로) + " 설정하였습니다.", item);
              player.sendMessage(txt);
            }
            else
            {
              MessageUtil.sendMessage(player, Prefix.INFO_SETDATA +
                      "주로 사용하는 손에 들고 있는 아이템의 종류를 &e" +
                      ComponentUtil.itemName(type) +
                      "&r" +
                      MessageUtil.getFinalConsonant(ComponentUtil.itemName(type).toString(), ConsonantType.으로) +
                      " 설정하였습니다.");
            }
            return true;
          default:
            MessageUtil.noArg(sender, Prefix.ARGS_WRONG, args[0]);
            MessageUtil.commandInfo(sender, label, usage);
            return true;
        }
      }
      else
      {
        Player target = SelectorUtil.getPlayer(sender, args[2]);
        if (target == null)
        {
          return true;
        }
        boolean hideMessage = args.length == 4 && args[3].equalsIgnoreCase("true");
        ItemStack item = target.getInventory().getItemInMainHand();
        if (!ItemStackUtil.itemExists(item))
        {
          MessageUtil.sendError(sender, Prefix.NO_HOLDING_ITEM);
          return true;
        }
        ItemStack clone = item.clone();
        if (args[0].equalsIgnoreCase("amount"))
        {
          int amount;
          try
          {
            amount = Integer.parseInt(args[1]);
          }
          catch (Exception e)
          {
            MessageUtil.noArg(sender, Prefix.ONLY_NUMBER, args[1]);
            return true;
          }
          if (!MessageUtil.checkNumberSize(sender, amount, 0, 127))
          {
            return true;
          }
          clone.setAmount(amount);
          target.getInventory().setItemInMainHand(clone);
          if (!hideMessage)
          {
            Component txt = ComponentUtil.create(MessageUtil.as(Prefix.INFO_SETDATA, sender, "이 당신의 주로 사용하는 손에 들고 있는 아이템의 개수를 &e" + amount + "개&r로 설정하였습니다."), clone);
            if (!target.equals(sender))
            {
              target.sendMessage(txt);
            }
            txt = ComponentUtil.create(MessageUtil.as(Prefix.INFO_SETDATA, target, "의 주로 사용하는 손에 들고 있는 아이템의 개수를 &e" + amount + "개&r로 설정하였습니다."), clone);
            sender.sendMessage(txt);
          }
        }
        else if (args[0].equalsIgnoreCase("durability"))
        {
          int durability;
          try
          {
            durability = Integer.parseInt(args[1]);
          }
          catch (Exception e)
          {
            MessageUtil.noArg(sender, Prefix.ONLY_NUMBER, args[1]);
            return true;
          }
          if (!MessageUtil.checkNumberSize(sender, durability, 1, 32767))
          {
            return true;
          }
          Damageable duraMeta = (Damageable) clone.getItemMeta();
          duraMeta.setDamage(durability);
          clone.setItemMeta((ItemMeta) duraMeta);
          target.getInventory().setItemInMainHand(clone);
          if (!hideMessage)
          {
            Component txt = ComponentUtil.create(MessageUtil.as(Prefix.INFO_SETDATA, sender, "이 당신의 주로 사용하는 손에 들고 있는 아이템의 내구도를 &e" + durability + "&r으로 설정하였습니다."), clone);
            if (!target.equals(sender))
            {
              target.sendMessage(txt);
            }
            txt = ComponentUtil.create(MessageUtil.as(Prefix.INFO_SETDATA, target, "의 주로 사용하는 손에 들고 있는 아이템의 내구도를 &e" + durability + "&r으로 설정하였습니다."), clone);
            sender.sendMessage(txt);
          }
        }
        else if (args[0].equalsIgnoreCase("material"))
        {
          Material type;
          try
          {
            type = Material.valueOf(args[1].toUpperCase());
          }
          catch (Exception e)
          {
            MessageUtil.noArg(sender, Prefix.NO_MATERIAL, args[1]);
            return true;
          }
          clone.setType(type);
          target.getInventory().setItemInMainHand(clone);
          if (!ItemStackUtil.itemExists(target.getInventory().getItemInMainHand()))
          {
            target.getInventory().setItemInMainHand(item);
            MessageUtil.sendWarn(sender, "존재할 수 없는 물질이여서 변경이 취소되었습니다.");
            return true;
          }
          if (!hideMessage)
          {
            Component txt = ComponentUtil.create(MessageUtil.as(
                    Prefix.INFO_SETDATA, sender, "이 당신의 주로 사용하는 손에 들고 있는 아이템의 종류를 ", clone, "(으)로 설정하였습니다."), clone);
            if (!target.equals(sender))
            {
              target.sendMessage(txt);
            }
            txt = ComponentUtil.create(MessageUtil.as(Prefix.INFO_SETDATA, target + "의 주로 사용하는 손에 들고 있는 아이템의 종류를 ", clone, "(으)로 설정하였습니다."), clone);
            sender.sendMessage(txt);
          }
        }
        else
        {
          MessageUtil.noArg(sender, Prefix.ARGS_WRONG, args[0]);
          if (sender instanceof Player)
          {
            MessageUtil.commandInfo(sender, label, usage);
          }
          else
          {
            MessageUtil.commandInfo(sender, label, usage.replace("[플레이어 ID]", "<플레이어 ID>"));
          }
          return true;
        }
      }
    }
    else
    {
      MessageUtil.longArg(sender, 4, args);
      if (sender instanceof Player)
      {
        MessageUtil.commandInfo(sender, label, usage);
      }
      else
      {
        MessageUtil.commandInfo(sender, label, usage.replace("[플레이어 ID]", "<플레이어 ID>"));
      }
      return true;
    }
    return true;
  }
}

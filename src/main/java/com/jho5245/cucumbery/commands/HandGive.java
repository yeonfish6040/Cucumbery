package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.additemmanager.AddItemUtil;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.SoundPlay;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class HandGive implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    String usage = cmd.getUsage().replace("/<command> ", "");
    if (cmd.getName().equalsIgnoreCase("handgive"))
    {
      if (!Method.hasPermission(sender, Permission.CMD_HANDGIVE, true))
      {
        return true;
      }
      if (!(sender instanceof Player))
      {
        MessageUtil.sendError(sender, Prefix.ONLY_PLAYER);
        return true;
      }
      if (args.length < 1)
      {
        MessageUtil.shortArg(sender, 1, args);
        MessageUtil.commandInfo(sender, label, usage);
        return true;
      }
      else if (args.length <= 3)
      {
        Player player = (Player) sender;
        List<Player> targets = SelectorUtil.getPlayers(sender, args[0]);
        if (targets == null)
        {
          return true;
        }
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!ItemStackUtil.itemExists(item))
        {
          MessageUtil.sendError(sender, Prefix.NO_HOLDING_ITEM);
          return true;
        }
        item = item.clone();
        int amount = item.getAmount();
        if (args.length >= 2)
        {
          if (!MessageUtil.isInteger(sender, args[1], true))
          {
            return true;
          }
          int input = Integer.parseInt(args[1]);
          if (!MessageUtil.checkNumberSize(sender, input, 1, 2304))
          {
            return true;
          }
          amount = input;
          item.setAmount(amount);
        }
        boolean hideOutput = false;
        if (args.length == 3)
        {
          if (!args[2].equals("true") && !args[2].equals("false"))
          {
            MessageUtil.wrongBool(sender, 3, args);
            return true;
          }
          if (args[2].equals("true"))
          {
            hideOutput = true;
          }
        }

        AddItemUtil.addItemResult2(sender, targets, item, amount).sendFeedback(hideOutput);

      }
      else
      {
        MessageUtil.longArg(sender, 3, args);
        MessageUtil.commandInfo(sender, label, usage);
      }
    }
    else if (cmd.getName().equalsIgnoreCase("handgiveall"))
    {
      if (!Method.hasPermission(sender, Permission.CMD_HANDGIVEALL, true))
      {
        return true;
      }
      if (!(sender instanceof Player))
      {
        MessageUtil.sendError(sender, Prefix.ONLY_PLAYER);
        return true;
      }
      if (args.length <= 4)
      {
        if (Bukkit.getOnlinePlayers().size() < 2)
        {
          MessageUtil.sendError(sender, "아이템을 줄 수 있는 플레이어가 아무도 없습니다.");
          return true;
        }
        if (args.length == 2)
        {
          MessageUtil.shortArg(sender, 2, args);
          MessageUtil.commandInfo(sender, label, usage);
        }
        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!ItemStackUtil.itemExists(item))
        {
          MessageUtil.sendError(sender, Prefix.NO_HOLDING_ITEM);
          return true;
        }
        int amount = item.getAmount();
        int from = 0, to = 0;
        boolean randomAmount = false;
        if (args.length > 0 && args[0].toUpperCase().startsWith("R") && args[0].contains("~"))
        {
          try
          {
            String arg = args[0].substring(1);
            String[] split = arg.split("~");
            from = Integer.parseInt(split[0]);
            if (!MessageUtil.checkNumberSize(sender, from, 0, 2304))
            {
              return true;
            }
            to = Integer.parseInt(split[1]);
            if (!MessageUtil.checkNumberSize(sender, to, from, 2304))
            {
              return true;
            }
            randomAmount = true;
          }
          catch (Exception e)
          {
            MessageUtil.sendError(sender, "올바르지 않은 형식입니다. R#~# 의 형식으로 입력해야합니다.");
            return true;
          }
        }
        if (!randomAmount && args.length > 0)
        {
          try
          {
            amount = Integer.parseInt(args[0]);
          }
          catch (Exception e)
          {
            MessageUtil.noArg(sender, Prefix.ONLY_NUMBER, args[0]);
            return true;
          }
          if (!MessageUtil.checkNumberSize(sender, amount, 1, 2304))
          {
            return true;
          }
        }
        item = item.clone();
        List<Player> successPlayers = new ArrayList<>();
        if (args.length < 2)
        {
          successPlayers.addAll(Bukkit.getServer().getOnlinePlayers());
        }
        for (Player target : Bukkit.getServer().getOnlinePlayers())
        {
          if (target != player)
          {
            if (args.length >= 3)
            {
              if (args[1].equalsIgnoreCase("world"))
              {
                World world = Bukkit.getServer().getWorld(args[2]);
                if (world == null)
                {
                  MessageUtil.noArg(sender, Prefix.NO_WORLD, args[2]);
                  return true;
                }
                if (target.getWorld().equals(world))
                {
                  successPlayers.add(target);
                }
              }
              else if (args[1].equalsIgnoreCase("distance"))
              {
                if (!MessageUtil.isDouble(sender, args[2], true))
                {
                  return true;
                }
                double distance = player.getLocation().distance(target.getLocation());
                double input = Double.parseDouble(args[2]);
                if (distance != -1D && distance <= input)
                {
                  successPlayers.add(target);
                }
              }
              else if (args[1].equalsIgnoreCase("permission"))
              {
                if (target.hasPermission(args[2].toLowerCase()))
                {
                  successPlayers.add(target);
                }
              }
              else if (args[1].equalsIgnoreCase("level"))
              {
                if (!MessageUtil.isInteger(sender, args[2], true))
                {
                  return true;
                }
                int input = Integer.parseInt(args[2]);
                if (target.getLevel() >= input)
                {
                  successPlayers.add(target);
                }
              }
              else if (args[1].equalsIgnoreCase("hp"))
              {
                if (!MessageUtil.isDouble(sender, args[2], true))
                {
                  return true;
                }
                double input = Double.parseDouble(args[2]);
                if (target.getHealth() >= input)
                {
                  successPlayers.add(target);
                }
              }
              else if (args[1].equalsIgnoreCase("maxhp"))
              {
                if (!MessageUtil.isDouble(sender, args[2], true))
                {
                  return true;
                }
                double input = Double.parseDouble(args[2]);
                if (Objects.requireNonNull(target.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue() >= input)
                {
                  successPlayers.add(target);
                }
              }
              else if (args[1].equalsIgnoreCase("healthbar"))
              {
                if (!MessageUtil.isDouble(sender, args[2], true))
                {
                  return true;
                }
                double input = Double.parseDouble(args[2]);
                if (target.getHealthScale() >= input)
                {
                  successPlayers.add(target);
                }
              }
              else if (args[1].equalsIgnoreCase("gamemode"))
              {
                GameMode gamemode;
                try
                {
                  gamemode = GameMode.valueOf(args[2].toUpperCase());
                }
                catch (Exception e)
                {
                  MessageUtil.noArg(sender, Prefix.NO_GAMEMODE, args[2]);
                  return true;
                }
                if (target.getGameMode() == gamemode)
                {
                  successPlayers.add(target);
                }
              }
              else
              {
                MessageUtil.wrongArg(sender, 2, args);
                MessageUtil.commandInfo(sender, label, usage);
                return true;
              }
            }
          }
        }

        boolean hideOutput = false;
        if (args.length == 4)
        {
          if (!args[3].equals("true") && !args[3].equals("false"))
          {
            MessageUtil.wrongBool(sender, 4, args);
            return true;
          }
          if (args[3].equals("true"))
          {
            hideOutput = true;
          }
        }
        List<Component> success = new ArrayList<>(), failure = new ArrayList<>();
        success.add(ComponentUtil.create(Prefix.INFO_HANDGIVEALL + "성공적으로 아이템을 받은 사람들 : "));
        failure.add(ComponentUtil.create(Prefix.INFO_HANDGIVEALL + "아이템의 일부 혹은 전부를 받지 못한 사람들 : "));
        int successAmount = 0, failureAmount = 0;
        final ItemStack origin = item.clone();
        for (Player target : successPlayers)
        {
          if (target.equals(sender))
          {
            continue;
          }
          if (randomAmount)
          {
            amount = Method.random(from, to);
          }
          item = origin;
          MessageUtil.broadcastDebug(target.getName() + ":" + item.toString());
          item.setAmount(amount);
          ItemStack lostItem = null;
          int lostAmount = 0;
          Collection<ItemStack> lostItems = target.getInventory().addItem(item).values();
          String itemString = ComponentUtil.itemName(item) + ((amount == 1) ? "" : " " + amount + "개") + "§r";
          itemString += MessageUtil.getFinalConsonant(itemString, MessageUtil.ConsonantType.을를);
          if (lostItems.size() != 0)
          {
            for (ItemStack lost : lostItems)
            {
              lostItem = lost;
            }
          }
          if (!hideOutput)
          {
            if (lostItem != null)
            {
              failureAmount++;
              lostAmount = lostItem.getAmount();
              String lostString = ComponentUtil.itemName(item) + ((lostAmount == amount) ? " 전부" : " " + lostAmount + "개") + "§r";
              lostString += MessageUtil.getFinalConsonant(lostString, MessageUtil.ConsonantType.을를);
              if (!target.equals(sender))
              {
                MessageUtil.sendMessage(target, ComponentUtil.create(Prefix.INFO_WARN + "인벤토리가 가득 차서 §e" + ComponentUtil.senderComponent(player) + "§r이(가) 보낸 " + lostString + " 지급받지 못하였습니다.", item));
                SoundPlay.playSound(target, Constant.WARNING_SOUND);
              }
              amount -= lostAmount;
              failure.add(
                      ComponentUtil.create("§e" + target.getDisplayName(), "§r받은 아이템 개수 : §e" + (lostAmount == amount ? "0개" : amount + "§r(§e" + (amount + lostAmount) + "§r - §e" + lostAmount + "§r)§e개")));
              failure.add(ComponentUtil.create("§e, §r"));
            }
            if (amount != 0)
            {
              if (lostAmount > 0)
              {
                itemString = ComponentUtil.itemName(item) + ((amount == 1) ? "" : " " + amount + "개") + "§r";
                itemString += MessageUtil.getFinalConsonant(itemString, MessageUtil.ConsonantType.을를);
              }
              if (!target.equals(sender))
              {
                MessageUtil.sendMessage(target, ComponentUtil.create(Prefix.INFO_HANDGIVE + "§e" + ComponentUtil.senderComponent(player) + "§r이(가) 모든 플레이어에게 " + itemString + " 지급하였습니다.", item));
              }
              if (lostItem == null)
              {
                successAmount++;
                success.add(ComponentUtil.create("&e" + target.getDisplayName(), "&r받은 아이탬 개수 : &e" + amount + "개"));
                success.add(ComponentUtil.create("&e, &r"));
              }
            }
          }
        }

        if (!hideOutput)
        {
          if (failureAmount > 0)
          {
            Component[] components = new Component[failure.size() - 1];
            for (int i = 0; i < failure.size() - 1; i++)
            {
              components[i] = failure.get(i);
            }
            MessageUtil.sendMessage(player, Prefix.INFO_HANDGIVEALL, "&e" + failureAmount + "명&r이 아이템의 일부 혹은 전부를 받지 못하였습니다.");
            MessageUtil.sendMessage(player, (Object) components);
          }
          if (successAmount > 0)
          {
            Component[] components = new Component[success.size() - 1];
            for (int i = 0; i < success.size() - 1; i++)
            {
              components[i] = success.get(i);
            }
            MessageUtil.sendMessage(player, Prefix.INFO_HANDGIVEALL, "아이템을 &e" + successAmount + "명&r에게 성공적으로 지급하였습니다.");
            MessageUtil.sendMessage(player, (Object) components);
          }
          else
          {
            MessageUtil.sendWarn(player, "아무도 아이템을 성공적으로 지급받지 못하였습니다.");
          }
        }
      }
      else
      {
        MessageUtil.longArg(sender, 5, args);
        MessageUtil.commandInfo(sender, label, usage);
      }
    }
    return true;
  }
}

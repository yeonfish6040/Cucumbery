package com.jho5245.cucumbery.commands;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Progun implements CommandExecutor
{
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    if (!(sender instanceof Player))
    {
      sender.sendMessage("플레이어만 사용 가능");
      return true;
    }

    Player player = (Player) sender;
    if (!player.isOp())
    {
      player.sendMessage("오피만 사용 가능");
      return true;
    }
    ItemStack item = player.getInventory().getItemInMainHand();
    if (item.getType() == Material.AIR)
    {
      player.sendMessage("손에 아무것도 들고 있지 않음");
      return true;
    }

    NBTItem nbtItem = new NBTItem(item);

    NBTCompound nbtCompound = nbtItem.addCompound("Progun");

    if (args.length < 2)
    {
      player.sendMessage("알 수 없거나 불완전한 명령어입니다.");
      return true;
    }

    switch (args[0])
    {
      case "damage":
        try
        {
          double damage = Double.parseDouble(args[1]);
          if (damage < 0)
          {
            player.sendMessage("대미지는 0 이상이여야 합니다.");
            return true;
          }
          nbtCompound.setDouble("damage", damage);
          player.getInventory().setItemInMainHand(nbtItem.getItem());
          player.sendMessage("레일건의 대미지를 " + damage + "으로 설정하였습니다.");
        }
        catch (Exception e)
        {
          player.sendMessage("숫자만 입력할 수 있습니다. (" + args[1] + ")");
          return true;
        }
        break;
      case "cooldown":
        try
        {
          double cooldown = Double.parseDouble(args[1]);
          if (cooldown < 0)
          {
            player.sendMessage("재사용 대기시간은 0초 이상이여야 합니다.");
            return true;
          }
          nbtCompound.setDouble("cooldown", cooldown);
          player.getInventory().setItemInMainHand(nbtItem.getItem());
          player.sendMessage("레일건의 재사용 대기시간을 " + cooldown + "초로 설정하였습니다.");
        }
        catch (Exception e)
        {
          player.sendMessage("숫자만 입력할 수 있습니다. (" + args[1] + ")");
          return true;
        }
        break;
      case "range":
        try
        {
          int range = Integer.parseInt(args[1]);
          if (range < 1)
          {
            player.sendMessage("사정거리는 1미터 이상이여야 합니다.");
            return true;
          }
          nbtCompound.setInteger("range", range);
          player.getInventory().setItemInMainHand(nbtItem.getItem());
          player.sendMessage("레일건의 사정거리를 " + range + "미터로 설정하였습니다.");
        }
        catch (Exception e)
        {
          player.sendMessage("정수만 입력할 수 있습니다. (" + args[1] + ")");
          return true;
        }
        break;
      case "uuid":
        nbtCompound.setString("uuid", args[1]);
        player.getInventory().setItemInMainHand(nbtItem.getItem());
        player.sendMessage("레일건의 UUID를 " + args[1] + "(으)로 설정하였습니다.");
        break;
      default:
        player.sendMessage("명령어에 잘못된 인수가 있습니다. (" + args[0] + ")");
        return true;
    }

    return true;
  }
}

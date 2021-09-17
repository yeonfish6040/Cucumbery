package com.jho5245.cucumbery.commands.addon;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.ItemInfo;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.maxgamer.quickshop.QuickShop;
import org.maxgamer.quickshop.shop.Shop;
import org.maxgamer.quickshop.shop.ShopType;

public class QuickShopAddon implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_QUICKSHOP_ADDON, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    if (!Cucumbery.using_QuickShop)
    {
      MessageUtil.sendError(sender, "&eQuickShop &r플러그인을 사용하고 있지 않습니다.");
      return true;
    }
    if (args.length < 5)
    {
      MessageUtil.shortArg(sender, 5, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return true;
    }
    else if (args.length <= 7)
    {
      String type = args[4];
      if (!Method.equals(type, "price", "amount", "type", "get", "info"))
      {
        MessageUtil.wrongArg(sender, 5, args);
        return true;
      }
      if (Method.equals(type, "price", "amount", "type") && args.length == 5)
      {
        MessageUtil.shortArg(sender, 6, args);
        MessageUtil.commandInfo(sender, label, "<상점 위치> <price|amount|type|get|info> <값> [명령어 출력 숨김 여부]");
        return true;
      }
      World world = Bukkit.getWorld(args[0]);
      if (world == null)
      {
        MessageUtil.noArg(sender, Prefix.NO_WORLD, args[0]);
        return true;
      }
      if (!MessageUtil.isInteger(sender, args[1], true))
      {
        return true;
      }
      if (!MessageUtil.isInteger(sender, args[2], true))
      {
        return true;
      }
      if (!MessageUtil.isInteger(sender, args[3], true))
      {
        return true;
      }
      int x = Integer.parseInt(args[1]), y = Integer.parseInt(args[2]), z = Integer.parseInt(args[3]);
      if (!MessageUtil.checkNumberSize(sender, x, -30000000, 30000000))
      {
        return true;
      }
      if (!MessageUtil.checkNumberSize(sender, y, 1, 256))
      {
        return true;
      }
      if (!MessageUtil.checkNumberSize(sender, z, -30000000, 30000000))
      {
        return true;
      }
      if (!MessageUtil.isBoolean(sender, args, 7, true))
      {
        return true;
      }
      boolean hideOutput = args.length == 7 && args[6].equals("true");
      Location location = new Location(world, x, y, z);
      Shop shop = QuickShop.getInstance().getShopManager().getShop(location);
      if (shop == null)
      {
        MessageUtil.sendError(sender, world.getName() + " " + x + " " + y + " " + z + "에 있는 상점을 찾을 수 없습니다.");
        return true;
      }
      switch (type)
      {
        case "price" -> {
          if (!MessageUtil.isDouble(sender, args[5], true))
          {
            return true;
          }
          double price = Double.parseDouble(args[5]);
          if (!MessageUtil.checkNumberSize(sender, price, 0d, 10_000_000_000_000d))
          {
            return true;
          }
          if (shop.getPrice() == price)
          {
            if (!hideOutput)
            {
              MessageUtil.sendError(sender, "변동 사항이 없습니다. 이미 " + world.getName() + " " + x + " " + y + " " + z + "에 있는 상점(&e" + ComponentUtil.itemName(shop.getItem()) +
                      "&r)의 거래 가격이 &e" + Constant.Sosu15.format(price) + "원&r입니다.");
            }
            return true;
          }
          shop.setPrice(price);
          if (!hideOutput)
          {
            MessageUtil.info(sender, world.getName() + " " + x + " " + y + " " + z + "에 있는 상점(&e" + ComponentUtil.itemName(shop.getItem()) + "&r)의 거래 가격을 &e"
                    + Constant.Sosu15.format(price) + "원&r으로 설정하였습니다.");
          }
        }
        case "amount" -> {
          if (!MessageUtil.isInteger(sender, args[5], true))
          {
            return true;
          }
          int amount = Integer.parseInt(args[5]);
          ItemStack item = shop.getItem();
          if (item.getMaxStackSize() == 1)
          {
            MessageUtil.sendError(sender, "&e" + ComponentUtil.itemName(shop.getItem()) + "&r의 최대 거래 개수가 &e1개&r여서 변경할 수 없습니다.");
            return true;
          }
          if (!MessageUtil.checkNumberSize(sender, amount, 1, item.getMaxStackSize()))
          {
            return true;
          }
          if (item.getAmount() == amount)
          {
            if (!hideOutput)
            {
              MessageUtil.sendError(sender, "변동 사항이 없습니다. 이미 " + world.getName() + " " + x + " " + y + " " + z + "에 있는 상점(&e" + ComponentUtil.itemName(shop.getItem()) + "&r)의 거래 개수가 &e" + amount +
                      "개&r입니다.");
            }
            return true;
          }
          item = item.clone();
          item.setAmount(amount);
          shop.setItem(item);
          if (!hideOutput)
          {
            MessageUtil.info(sender, world.getName() + " " + x + " " + y + " " + z + "에 있는 상점(&e" + ComponentUtil.itemName(shop.getItem()) + "&r)의 거래 개수를 &e" + Constant.Sosu15.format(amount) + "개&r로 설정하였습니다.");
          }
        }
        case "type" -> {
          ShopType shopType;
          try
          {
            shopType = ShopType.valueOf(args[5].toUpperCase());
          }
          catch (Exception e)
          {
            MessageUtil.wrongArg(sender, 6, args);
            return true;
          }
          String typeString = switch (shopType)
                  {
                    case BUYING -> "&b판매 상점&r(상점이 플레이어한테서 &c구매&r)";
                    case SELLING -> "&c구매 상점&r(상점이 플레이어한테 &b판매&r)";
                  };
          if (shopType == shop.getShopType())
          {
            if (!hideOutput)
            {
              MessageUtil.sendError(sender, "변동 사항이 없습니다. 이미 " + world.getName() + " " + x + " " + y + " " + z + "에 있는 상점(&e" + ComponentUtil.itemName(shop.getItem()) + "&r)이 " + typeString + "입니다.");
            }
            return true;
          }
          shop.setShopType(shopType);
          if (!hideOutput)
          {
            MessageUtil.info(sender, world.getName() + " " + x + " " + y + " " + z + "에 있는 상점(&e" + ComponentUtil.itemName(shop.getItem()) + "&r)을 " + typeString + "으로 설정하였습니다.");
          }
        }
        case "get" -> {
          if (!(sender instanceof Player))
          {
            MessageUtil.sendError(sender, Prefix.ONLY_PLAYER);
            return true;
          }
          ItemStack item = shop.getItem().clone();
          int amount = item.getAmount();
          if (args.length == 6)
          {
            if (!MessageUtil.isInteger(sender, args[5], true))
            {
              return true;
            }
            amount = Integer.parseInt(args[5]);
            if (!MessageUtil.checkNumberSize(sender, amount, 1, 2304))
            {
              return true;
            }
            item.setAmount(amount);
          }
          ((Player) sender).getInventory().addItem(item);
          MessageUtil.info(sender, world.getName() + " " + x + " " + y + " " + z + "에 있는 상점의 아이템을 &e" + amount + "개&r 지급받았습니다. (&e" + ComponentUtil.itemName(shop.getItem()) + "&r)");
        }
        case "info" -> {
          if (args.length == 5)
          {
            ItemInfo.sendInfo(sender, shop.getItem());
          }
          else
          {
            MessageUtil.longArg(sender, 5, args);
            MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
            return true;
          }
        }
      }
    }
    else
    {
      MessageUtil.longArg(sender, 6, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return true;
    }
    return true;
  }
}

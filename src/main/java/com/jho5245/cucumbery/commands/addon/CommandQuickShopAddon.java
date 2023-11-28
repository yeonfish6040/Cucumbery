package com.jho5245.cucumbery.commands.addon;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.CucumberyCommandExecutor;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.no_groups.ItemInfo;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.maxgamer.quickshop.QuickShop;
import org.maxgamer.quickshop.api.shop.Shop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandQuickShopAddon implements CucumberyCommandExecutor
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
      MessageUtil.sendError(sender, "%s플러그인을 사용하고 있지 않습니다", Constant.THE_COLOR_HEX + "QuickShop");
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
        MessageUtil.sendError(sender, "%s에 있는 상점을 찾을 수 없습니다", location);
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
              MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 " + world.getName() + " " + x + " " + y + " " + z + "에 있는 상점(rg255,204;" + ItemNameUtil.itemName(shop.getItem()) +
                      "&r)의 거래 가격이 rg255,204;" + Constant.Sosu15.format(price) + "원&r입니다");
            }
            return true;
          }
          shop.setPrice(price);
          if (!hideOutput)
          {
            MessageUtil.info(sender, world.getName() + " " + x + " " + y + " " + z + "에 있는 상점(rg255,204;" + ItemNameUtil.itemName(shop.getItem()) + "&r)의 거래 가격을 rg255,204;"
                    + Constant.Sosu15.format(price) + "원&r으로 설정했습니다");
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
            MessageUtil.sendError(sender, Constant.THE_COLOR_HEX + ItemNameUtil.itemName(shop.getItem()) + "&r의 최대 거래 개수가 rg255,204;1개&r여서 변경할 수 없습니다");
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
              MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 " + world.getName() + " " + x + " " + y + " " + z + "에 있는 상점(rg255,204;" + ItemNameUtil.itemName(shop.getItem()) + "&r)의 거래 개수가 rg255,204;" + amount +
                      "개&r입니다");
            }
            return true;
          }
          item = item.clone();
          item.setAmount(amount);
          shop.setItem(item);
          if (!hideOutput)
          {
            MessageUtil.info(sender, world.getName() + " " + x + " " + y + " " + z + "에 있는 상점(rg255,204;" + ItemNameUtil.itemName(shop.getItem()) + "&r)의 거래 개수를 rg255,204;" + Constant.Sosu15.format(amount) + "개&r로 설정했습니다");
          }
        }
        case "type" -> {
          org.maxgamer.quickshop.api.shop.ShopType shopType;
          try
          {
            shopType = org.maxgamer.quickshop.api.shop.ShopType.valueOf(args[5].toUpperCase());
          }
          catch (Exception e)
          {
            MessageUtil.wrongArg(sender, 6, args);
            return true;
          }
          String typeString = switch (args[5])
                  {
                    case "buying" -> "&b판매 상점&r(상점이 플레이어한테서 &c구매&r)";
                    case "selling" -> "&c구매 상점&r(상점이 플레이어한테 &b판매&r)";
                    default -> "";
                  };
          if (shopType == shop.getShopType())
          {
            if (!hideOutput)
            {
              MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 " + world.getName() + " " + x + " " + y + " " + z + "에 있는 상점(rg255,204;" + ItemNameUtil.itemName(shop.getItem()) + "&r)이 " + typeString + "입니다");
            }
            return true;
          }
          shop.setShopType(shopType);
          if (!hideOutput)
          {
            MessageUtil.info(sender, world.getName() + " " + x + " " + y + " " + z + "에 있는 상점(rg255,204;" + ItemNameUtil.itemName(shop.getItem()) + "&r)을 " + typeString + "으로 설정했습니다");
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
          MessageUtil.info(sender, world.getName() + " " + x + " " + y + " " + z + "에 있는 상점의 아이템을 rg255,204;" + amount + "개&r 지급받았습니다 (rg255,204;" + ItemNameUtil.itemName(shop.getItem()) + "&r)");
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

  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Cucumbery.using_QuickShop)
    {
      return Collections.singletonList("QuickShop 플러그인을 사용하고 있지 않습니다");
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(true, args), true))
    {
      return Collections.singletonList(args[0]);
    }
    int length = args.length;
    if (Variable.shops.isEmpty() && args.length >= 2 && args.length <= 6)
    {
      return Collections.singletonList("유효한 QuickShop 상점이 존재하지 않습니다");
    }
    List<String> list = new ArrayList<>();
    List<String> xList = new ArrayList<>();
    List<String> yList = new ArrayList<>();
    List<String> zList = new ArrayList<>();
    for (Shop shop : Variable.shops)
    {
      Location location = shop.getLocation();
      String display = ComponentUtil.serialize(ItemNameUtil.itemName(shop.getItem()));
      if (location.getWorld() != null)
      {
        String worldName = location.getWorld().getName();
        int x = location.getBlockX(), y = location.getBlockY(), z = location.getBlockZ();
        list.add(worldName + " " + x + " " + y + " " + z);
        list.add(worldName + " " + x + " " + y + " " + z + "(" + display + ")");
        if (args.length >= 2 && args[0].equals(worldName))
        {
          xList.add(x + " " + y + " " + z);
          xList.add(x + " " + y + " " + z + "(" + display + ")");
          if (args.length >= 3 && args[1].equals(x + ""))
          {
            yList.add(y + " " + z);
            yList.add(y + " " + z + "(" + display + ")");
            if (args.length >= 4 && args[2].equals(y + ""))
            {
              zList.add(z + "");
              zList.add(z + "(" + display + ")");
            }
          }
        }
      }
    }
    if (sender instanceof Player player)
    {
      Block block = player.getTargetBlockExact(5);
      if (block != null)
      {
        switch (block.getType())
        {
          case ACACIA_WALL_SIGN, BIRCH_WALL_SIGN, CRIMSON_WALL_SIGN, DARK_OAK_WALL_SIGN, JUNGLE_WALL_SIGN, OAK_WALL_SIGN, SPRUCE_WALL_SIGN, WARPED_WALL_SIGN -> {
            BlockFace blockFace = player.getTargetBlockFace(5);
            Location loc = block.getLocation();
            int x = loc.getBlockX(), y = loc.getBlockY(), z = loc.getBlockZ();
            if (blockFace != null)
            {
              switch (blockFace)
              {
                case WEST -> block = player.getWorld().getBlockAt(x + 1, y, z);
                case EAST -> block = player.getWorld().getBlockAt(x - 1, y, z);
                case DOWN -> block = player.getWorld().getBlockAt(x, y + 1, z);
                case UP -> block = player.getWorld().getBlockAt(x, y - 1, z);
                case NORTH -> block = player.getWorld().getBlockAt(x, y, z + 1);
                case SOUTH -> block = player.getWorld().getBlockAt(x, y, z - 1);
              }
            }
          }
        }
      }
      Shop shop = null;
      if (block != null)
      {
        shop = QuickShop.getInstance().getShopManager().getShop(block.getLocation());
      }
      if (shop != null)
      {
        list.clear();
        xList.clear();
        yList.clear();
        zList.clear();
        Location location = shop.getLocation();
        String display = MessageUtil.stripColor(ItemNameUtil.itemName(shop.getItem()).toString());
        if (location.getWorld() != null)
        {
          String worldName = location.getWorld().getName();
          int x = location.getBlockX(), y = location.getBlockY(), z = location.getBlockZ();
          list.add(worldName + " " + x + " " + y + " " + z);
          list.add(worldName + " " + x + " " + y + " " + z + "(" + display + ")");
          if (args.length >= 2 && args[0].equals(worldName))
          {
            xList.add(x + " " + y + " " + z);
            xList.add(x + " " + y + " " + z + "(" + display + ")");
            if (args.length >= 3 && args[1].equals(x + ""))
            {
              yList.add(y + " " + z);
              yList.add(y + " " + z + "(" + display + ")");
              if (args.length >= 4 && args[2].equals(y + ""))
              {
                zList.add(z + "");
                zList.add(z + "(" + display + ")");
              }
            }
          }
        }
      }
    }
    if (length == 1)
    {
      return Method.tabCompleterList(args, list, "<상점 위치>", true);
    }
    else if (length == 2)
    {
      return Method.tabCompleterIntegerRadius(args, -30000000, 30000000, "<X좌표>", Method.listToArray(xList));
    }
    else if (length == 3)
    {
      return Method.tabCompleterIntegerRadius(args, 1, 256, "<Y좌표>", Method.listToArray(yList));
    }
    else if (length == 4)
    {
      return Method.tabCompleterIntegerRadius(args, -30000000, 30000000, "<Z좌표>", Method.listToArray(zList));
    }
    else if (length <= 6)
    {
      Shop shop;
      try
      {
        shop = QuickShop.getInstance().getShopManager().getShop(new Location(Bukkit.getWorld(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3])));
        if (shop == null)
        {
          throw new NullPointerException();
        }
      }
      catch (Exception e)
      {
        return Collections.singletonList(args[0] + " " + args[1] + " " + args[2] + " " + args[3] + "에 있는 상점을 찾을 수 없습니다");
      }
      ItemStack shopItem = shop.getItem();
      if (args.length == 5)
      {
        if (sender instanceof Player)
        {
          return Method.tabCompleterList(args, "<인수>", "price", "amount" + (shopItem.getMaxStackSize() == 1 ? "(변경 불가)" : ""), "type", "info", "get");
        }
        return Method.tabCompleterList(args, "<인수>", "price", "amount" + (shopItem.getMaxStackSize() == 1 ? "(변경 불가)" : ""), "type", "info");
      }
      String display = MessageUtil.stripColor(ItemNameUtil.itemName(shopItem).toString());
      switch (args[4])
      {
        case "price":
          return Method.tabCompleterDoubleRadius(args, 0d, 10_000_000_000_000d, "<" + display + "의 거래 가격>", Constant.rawFormat.format(shop.getPrice()));
        case "amount":
          if (shopItem.getMaxStackSize() == 1)
          {
            return Collections.singletonList(display + "의 최대 거래 개수가 1개여서 변경할 수 없습니다");
          }
          return Method.tabCompleterIntegerRadius(args, 1, shopItem.getMaxStackSize(), "<" + display + "의 거래 개수>", shopItem.getAmount() + "");
        case "type":
          return Method.tabCompleterList(args, "<상점 타입>", "selling", "buying");
        case "get":
          return Method.tabCompleterIntegerRadius(args, 1, 2304, "[개수]");
      }
    }
    else if (length == 7)
    {
      if (Method.equals(args[4], "price", "amount", "type"))
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }

    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }

  @Override
  public @NotNull List<Completion> completion(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args, @NotNull Location location)
  {
    return Collections.emptyList();
  }
}

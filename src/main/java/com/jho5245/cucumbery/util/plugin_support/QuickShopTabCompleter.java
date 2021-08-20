package com.jho5245.cucumbery.util.plugin_support;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.maxgamer.quickshop.QuickShop;
import org.maxgamer.quickshop.shop.Shop;
import org.maxgamer.quickshop.shop.ShopType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuickShopTabCompleter implements org.bukkit.command.TabCompleter
{
  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (Variable.shops.size() == 0 && args.length >= 2 && args.length <= 6)
    {
      return Collections.singletonList("유효한 QuickShop 상점이 존재하지 않습니다.");
    }
    List<String> list = new ArrayList<>();
    List<String> xList = new ArrayList<>();
    List<String> yList = new ArrayList<>();
    List<String> zList = new ArrayList<>();
    for (Shop shop : Variable.shops)
    {
      Location location = shop.getLocation();
      String display = ComponentUtil.serialize(ComponentUtil.itemName(shop.getItem()));
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
      Block block = player.getTargetBlock(5);
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
        String display = MessageUtil.stripColor(ComponentUtil.itemName(shop.getItem()).toString());
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
    if (args.length == 1)
    {
      return Method.tabCompleterList(args, list, "<상점 위치>", true);
    }
    else if (args.length == 2)
    {
      return Method.tabCompleterIntegerRadius(args, -30000000, 30000000, "<X좌표>", Method.listToArray(xList));
    }
    else if (args.length == 3)
    {
      return Method.tabCompleterIntegerRadius(args, 1, 256, "<Y좌표>", Method.listToArray(yList));
    }
    else if (args.length == 4)
    {
      return Method.tabCompleterIntegerRadius(args, -30000000, 30000000, "<Z좌표>", Method.listToArray(zList));
    }
    else if (args.length <= 6)
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
        return Collections.singletonList(args[0] + " " + args[1] + " " + args[2] + " " + args[3] + "에 있는 상점을 찾을 수 없습니다.");
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
      String display = MessageUtil.stripColor(ComponentUtil.itemName(shopItem).toString());
      switch (args[4])
      {
        case "price":
          return Method.tabCompleterDoubleRadius(args, 0d, 10_000_000_000_000d, "<" + display + "의 거래 가격>", Constant.rawFormat.format(shop.getPrice()));
        case "amount":
          if (shopItem.getMaxStackSize() == 1)
          {
            return Collections.singletonList(display + "의 최대 거래 개수가 1개여서 변경할 수 없습니다.");
          }
          return Method.tabCompleterIntegerRadius(args, 1, shopItem.getMaxStackSize(), "<" + display + "의 거래 개수>", shopItem.getAmount() + "");
        case "type":
          return Method.tabCompleterList(args, ShopType.values(), "<상점 타입>");
        case "get":
          return Method.tabCompleterIntegerRadius(args, 1, 2304, "[개수]");
      }
    }
    else if (args.length == 7)
    {
      if (Method.equals(args[4], "price", "amount", "type"))
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}
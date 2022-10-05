package com.jho5245.cucumbery.commands.no_groups;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.no_groups.*;
import com.jho5245.cucumbery.util.no_groups.CommandArgumentUtil.LocationTooltip;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig;
import de.tr7zw.changeme.nbtapi.NBTContainer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandBlockPlaceData implements CucumberyCommandExecutor
{
  private final Set<UUID> overrideWarning = new HashSet<>();

  private final HashMap<UUID, BukkitTask> taskHashMap = new HashMap<>();

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_BLOCK_PLACE_DATA, true))
    {
      return true;
    }
    boolean failure = !(sender instanceof BlockCommandSender);
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return failure;
    }
    int length = args.length;
    if (length < 2)
    {
      MessageUtil.shortArg(sender, 2, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(command));
      return failure;
    }
    Location location = CommandArgumentUtil.location(sender, args[0], true, true);
    if (location == null)
    {
      return failure;
    }
    World world = location.getWorld();
    YamlConfiguration blockPlaceData = Variable.blockPlaceData.get(world.getName());
    if (blockPlaceData == null)
    {
      blockPlaceData = CustomConfig.getCustomConfig("BlockPlaceData/" + world.getName() + ".yml").getConfig();
    }
    String key = location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ();
    String serialItem = blockPlaceData.getString(key);
    ItemStack itemStack = ItemSerializer.deserialize(serialItem != null && serialItem.length() - serialItem.replace("%", "").length() > 2 ? PlaceHolderUtil.placeholder(sender, serialItem, null) : serialItem);
    switch (args[1])
    {
      case "info" ->
      {
        if (length == 2)
        {
          if (serialItem == null)
          {
            MessageUtil.sendError(sender, "%s에는 저장된 블록 데이터가 없습니다", location);
            return failure;
          }
          if (itemStack.getType().isAir())
          {
            MessageUtil.sendWarn(sender, "%s에 있는 아이템이 손상되어 있습니다: %s", location, serialItem);
            return true;
          }
          MessageUtil.info(sender, "%s에 있는 아이템: %s %s", location, itemStack,
                  ComponentUtil.translate("rg255,204;[복사]")
                          .clickEvent(ClickEvent.copyToClipboard("'" + serialItem.replace("'", "''") + "'"))
                          .hoverEvent(Component.translatable("클릭하여 이스케이프된 nbt 복사").append(Component.text("\n" + serialItem))));
          return true;
        }
      }
      case "remove" ->
      {
        if (length == 2)
        {
          if (serialItem == null)
          {
            MessageUtil.sendError(sender, "%s 위치에는 저장된 블록 데이터가 없습니다", location);
            return failure;
          }
          blockPlaceData.set(key, null);
          MessageUtil.info(sender, "%s 위치에 있는 블록 데이터를 제거했습니다. (%s) %s", location, itemStack,
                  ComponentUtil.translate("rg255,204;[복사]")
                          .clickEvent(ClickEvent.copyToClipboard("'" + serialItem.replace("'", "''") + "'"))
                          .hoverEvent(Component.translatable("클릭하여 이스케이프된 nbt 복사").append(Component.text("\n" + serialItem))));
          return true;
        }
      }
      case "modify" ->
      {
        if (length < 3)
        {
          MessageUtil.shortArg(sender, 2, args);
          MessageUtil.commandInfo(sender, label, "<위치> modify <hand|merge|set> ...");
          return failure;
        }
        switch (args[2])
        {
          case "hand" ->
          {
            if (length == 3)
            {
              if (!(sender instanceof Player player))
              {
                MessageUtil.sendError(sender, Prefix.ONLY_PLAYER);
                return failure;
              }
              ItemStack hand = player.getInventory().getItemInMainHand().clone();
              if (hand.getType().isAir())
              {
                MessageUtil.sendError(player, Prefix.NO_HOLDING_ITEM);
                return failure;
              }
              ItemLore.removeItemLore(hand);
              if (!itemStack.getType().isAir())
              {
                UUID uuid = player.getUniqueId();
                if (!overrideWarning.contains(uuid))
                {
                  MessageUtil.sendWarn(player, "%s에 이미 아이템 %s이(가) 존재합니다. 덮어씌우려면 3초 안에 한 번 더 입력하세요", location, itemStack);
                  overrideWarning.add(uuid);
                  taskHashMap.put(uuid, Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> overrideWarning.remove(uuid), 60L));
                  return true;
                }
                if (taskHashMap.containsKey(uuid))
                {
                  taskHashMap.get(uuid).cancel();
                }
                overrideWarning.remove(uuid);
                MessageUtil.sendWarn(player, "%s에 이미 존재하는 아이템 %s을(를) 덮어씌웠습니다", location, itemStack);
              }
              blockPlaceData.set(key, ItemSerializer.serialize(hand));
              Variable.blockPlaceData.put(world.getName(), blockPlaceData);
              MessageUtil.info(player, "%s에 아이템 %s을(를) 저장했습니다", location, hand);
              return true;
            }
          }
          case "merge", "set" ->
          {
            if (length < 4)
            {
              return failure;
            }
            if (length == 4)
            {
              switch (args[2])
              {
                case "merge" ->
                {
                  if (serialItem == null)
                  {
                    MessageUtil.sendError(sender, "%s에는 저장된 블록 데이터가 없습니다", location);
                    return failure;
                  }
                  if (itemStack.getType().isAir())
                  {
                    MessageUtil.sendWarn(sender, "%s에 있는 아이템이 손상되어 있습니다: %s", location, serialItem);
                    return true;
                  }
                  String nbt = args[3] = "{" + args[3] + "}";
                  NBTContainer merge;
                  try
                  {
                    merge = new NBTContainer("{tag:" + nbt + "}");
                  }
                  catch (Exception e)
                  {
                    MessageUtil.sendError(sender, "%s은(는) 잘못된 NBT입니다", nbt);
                    return failure;
                  }
                  NBTContainer nbtContainer = new NBTContainer(serialItem);
                  nbtContainer.mergeCompound(merge);
                  ItemStack newItem = ItemSerializer.deserialize(nbtContainer.toString());
                  blockPlaceData.set(key, ItemSerializer.serialize(newItem));
                  Variable.blockPlaceData.put(world.getName(), blockPlaceData);
                  MessageUtil.info(sender, "%s에 아이템 %s에 nbt를 병합했습니다. (%s)", location, itemStack, newItem);
                  return true;
                }
                case "set" ->
                {
                  boolean ignoreInvalid = args[3].endsWith("--ignore");
                  if (ignoreInvalid)
                  {
                    args[3] = args[3].substring(0, args[3].length() - 8);
                  }
                  args[3] = "{" + args[3] + "}";
                  ItemStack stack = ItemSerializer.deserialize(args[3]);
                  if (stack.getType().isAir())
                  {
                    if (ignoreInvalid)
                    {
                      MessageUtil.sendWarn(sender, "%s은(는) 잘못된 아이템이지만 무시하고 저장합니다", args[3]);
                    }
                    else
                    {
                      MessageUtil.sendError(sender, "%s은(는) 잘못된 아이템입니다", args[3]);
                      return failure;
                    }
                  }
                  blockPlaceData.set(key, ItemSerializer.serialize(stack));
                  Variable.blockPlaceData.put(world.getName(), blockPlaceData);
                  MessageUtil.info(sender, "%s에 아이템 %s을(를) 저장했습니다", location, stack.getType().isAir() && ignoreInvalid ? args[3] : stack);
                  return true;
                }
              }
            }
          }
        }
      }
      default ->
      {
        MessageUtil.wrongArg(sender, 2, args);
        MessageUtil.commandInfo(sender, label, Method.getUsage(command));
        return failure;
      }
    }
    MessageUtil.longArg(sender, 4, args);
    MessageUtil.commandInfo(sender, label, Method.getUsage(command));
    return true;
  }

  @Override
  public @NotNull List<Completion> completion(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args, @NotNull Location location)
  {
    List<LocationTooltip> locations = new ArrayList<>();
    YamlConfiguration blockPlaceData = Variable.blockPlaceData.get(location.getWorld().getName());
    if (blockPlaceData != null)
    {
      for (String s : blockPlaceData.getKeys(false))
      {
        try
        {
          String[] split = s.split("_");
          int x = Integer.parseInt(split[0]), y = Integer.parseInt(split[1]), z = Integer.parseInt(split[2]);
          ItemStack itemStack = ItemSerializer.deserialize(blockPlaceData.getString(s));
          if (itemStack.getType().isAir())
          {
            throw new Exception();
          }
          locations.add(new LocationTooltip(new Location(location.getWorld(), x, y, z), ItemNameUtil.itemName(itemStack)));
        }
        catch (Exception ignored)
        {
        }
      }
    }
    int length = args.length;
    if (length == 1)
    {
      return CommandTabUtil.locationArgument(sender, args, "<블록>", location, true, locations);
    }
    if (length == 2)
    {
      return CommandTabUtil.tabCompleterList(args, "<인수>", false, "info", "remove", "modify");
    }
    if (args[1].equals("modify"))
    {
      if (length == 3)
      {
        return CommandTabUtil.tabCompleterList(args, "<인수>", false, "hand", "merge", "set");
      }
      if (length == 4)
      {
        switch (args[2])
        {
          case "merge", "set" ->
          {
            return CommandTabUtil.nbtArgument(sender, args, "<nbt>");
          }
        }
      }
    }
    return CommandTabUtil.ARGS_LONG;
  }
}

package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.FloatRangeArgument;
import dev.jorel.commandapi.arguments.ItemStackPredicateArgument;
import dev.jorel.commandapi.wrappers.FloatRange;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CommandSearchChestItem extends CommandBase
{
  public enum LocationSortType
  {
    DEFAULT,
    NEAREST,
    FURTHEST,
    SMALLEST,
    BIGGEST,
    RANDOM,
  }

  private void search(Player player, FloatRange range, Predicate<ItemStack> predicate, int maxSize)
  {

  }

  @SuppressWarnings("unchecked")
  @Override
  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(new FloatRangeArgument("범위"), new ItemStackPredicateArgument("아이템"));
    commandAPICommand = commandAPICommand.executesPlayer((player, args) -> {
      FloatRange range = (FloatRange) args.get(0);
      Predicate<ItemStack> predicate = (Predicate<ItemStack>) args.get(1);
      List<Location> locations = new ArrayList<>();
      for (Chunk chunk : player.getWorld().getLoadedChunks())
      {
        for (BlockState blockState : chunk.getTileEntities())
        {
          if (blockState instanceof BlockInventoryHolder blockInventoryHolder)
          {
            double distance = blockState.getLocation().distance(player.getLocation());
            if (range.isInRange((float) distance))
            {
              Inventory inventory = blockInventoryHolder.getInventory();
              for (int i = 0; i < inventory.getSize(); i++)
              {
                ItemStack itemStack = inventory.getItem(i);
                if (predicate.test(itemStack))
                {
                  locations.add(blockInventoryHolder.getBlock().getLocation());
                  break;
                }
              }
            }
          }
        }
      }
      if (locations.isEmpty())
      {
        MessageUtil.info(player, "지정된 범위에 해당하는 아이템이 담긴 컨테이너를 찾지 못했습니다");
      }
      else {
        MessageUtil.info(player, "%s개의 컨테이너를 찾았습니다", locations.size());
        for (int i = 0; i < locations.size(); i++)
        {
          if (i == 10)
          {
            MessageUtil.info(player, "&7container.shulkerBox.more", locations.size() - 10);
            break;
          }
          MessageUtil.info(player, locations.get(i));
        }
      }
    });
    commandAPICommand.register();
  }
}

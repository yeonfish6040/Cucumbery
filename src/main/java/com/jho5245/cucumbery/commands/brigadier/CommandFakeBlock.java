package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Scheduler;
import com.jho5245.cucumbery.util.storage.data.Variable;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BlockStateArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.LocationType;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;

public class CommandFakeBlock extends CommandBase
{
  @Override
  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(new LocationArgument("위치", LocationType.BLOCK_POSITION), new BlockStateArgument("블록"));
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Location location = (Location) args[0];
      BlockData blockData = (BlockData) args[1];
      Variable.fakeBlocks.put(location, blockData);
      MessageUtil.info(sender, "%s 위치에 %s 블록을 저장했습니다", location, blockData.getMaterial());
      Scheduler.fakeBlocksAsync();
    });
    commandAPICommand.register();
    commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(new LocationArgument("위치1", LocationType.BLOCK_POSITION), new LocationArgument("위치2", LocationType.BLOCK_POSITION), new BlockStateArgument("블록"));
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Location location = (Location) args[0];
      Location location2 = (Location) args[1];
      BlockData blockData = (BlockData) args[2];
      World world = location.getWorld();
      int xMin = Math.min(location.getBlockX(), location2.getBlockX());
      int xMax = Math.max(location.getBlockX(), location2.getBlockX());
      int yMin = Math.min(location.getBlockY(), location2.getBlockY());
      int yMaX = Math.max(location.getBlockY(), location2.getBlockY());
      int zMin = Math.min(location.getBlockZ(), location2.getBlockZ());
      int zMax = Math.max(location.getBlockZ(), location2.getBlockZ());
      for (int i = xMin; i <= xMax; i++)
      {
        for (int j = yMin; j <= yMaX; j++)
        {
          for (int k = zMin; k <= zMax; k++)
          {
            Variable.fakeBlocks.put(new Location(world, i, j, k), blockData.clone());
          }
        }
      }
      Scheduler.fakeBlocksAsync();
    });
    commandAPICommand.register();
    commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(new LocationArgument("위치", LocationType.BLOCK_POSITION), new MultiLiteralArgument("--remove", "--query"));
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Location location = (Location) args[0];
      switch ((String) args[1])
      {
        case "--remove" ->
        {
          if (Variable.fakeBlocks.containsKey(location))
          {
            MessageUtil.info(sender, "%s 위치에 %s 블록을 삭제했습니다", location, Variable.fakeBlocks.remove(location).getMaterial());
            location.getBlock().getState().update(true, false);
          }
          else
          {
            CommandSender commandSender = sender.getCallee();
            if (commandSender instanceof BlockCommandSender)
            {
              throw CommandAPI.fail("해당 위치에 블록이 존재하지 않습니다");
            }
            else
            {
              MessageUtil.sendError(sender, "%s 위치에 블록이 존재하지 않습니다", location);
            }
          }
        }
        case "--query" ->
        {
          if (Variable.fakeBlocks.containsKey(location))
          {
            MessageUtil.info(sender, "%s 위치의 블록 유형은 %s입니다", Variable.fakeBlocks.get(location));
            location.getBlock().getState().update();
          }
          else
          {
            MessageUtil.sendError(sender, "%s 위치에 블록이 존재하지 않습니다", location);
          }
        }
      }
    });
    commandAPICommand.register();
    commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(new MultiLiteralArgument("list"));
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      MessageUtil.info(sender, "총 %s개의 블록 데이터가 있습니다", Variable.fakeBlocks.keySet().size());
      for (Location location : Variable.fakeBlocks.keySet())
      {
        BlockData blockData = Variable.fakeBlocks.get(location);
        MessageUtil.info(sender, "%s : %s", location, blockData.getMaterial());
      }
    });
    commandAPICommand.register();
  }
}

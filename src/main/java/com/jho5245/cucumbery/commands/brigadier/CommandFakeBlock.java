package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Scheduler;
import com.jho5245.cucumbery.util.storage.data.Variable;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandFakeBlock extends CommandBase
{
  /**
   * @param location 페이크 블록을 설정할 좌표
   * @param blockData 설정할 블록 데이터
   * @param ignoreIfExists true일 경우 만약 해당 위치에 존재하면 무시
   * @param ignoreIfAir true일 경우 만약 해당 위치가 {@link Material#isAir()} 이면 무시
   */
  private void fakeBlock(@NotNull Location location, @NotNull BlockData blockData, boolean ignoreIfExists, boolean ignoreIfAir)
  {
    if (ignoreIfExists && Variable.fakeBlocks.containsKey(location))
    {
      return;
    }
    Material type = location.getBlock().getType();
    if (ignoreIfAir && type.isAir())
    {
      return;
    }
    Variable.fakeBlocks.put(location, blockData);
    Scheduler.fakeBlocksAsync(null, location, true);
  }
  /**
   * @param from 페이크 블록을 설정할 시작 좌표
   * @param to 페이크 블록을 설정할 끝 좌표
   * @param blockData 설정할 블록 데이터
   * @param ignoreIfExists true일 경우 만약 해당 위치에 존재하면 무시
   * @param ignoreIfAir true일 경우 만약 해당 위치가 {@link Material#isAir()} 이면 무시
   */
  private void fakeBlock(@NotNull Location from, @NotNull Location to, @NotNull BlockData blockData, boolean ignoreIfExists, boolean ignoreIfAir)
  {
    World world = from.getWorld();
    int xMin = Math.min(from.getBlockX(), to.getBlockX());
    int xMax = Math.max(from.getBlockX(), to.getBlockX());
    int yMin = Math.min(from.getBlockY(), to.getBlockY());
    int yMaX = Math.max(from.getBlockY(), to.getBlockY());
    int zMin = Math.min(from.getBlockZ(), to.getBlockZ());
    int zMax = Math.max(from.getBlockZ(), to.getBlockZ());
    for (int i = xMin; i <= xMax; i++)
    {
      for (int j = yMin; j <= yMaX; j++)
      {
        for (int k = zMin; k <= zMax; k++)
        {
          Location location = new Location(world, i, j, k);
          if (ignoreIfExists && Variable.fakeBlocks.containsKey(location))
          {
            Scheduler.fakeBlocksAsync(null, location, true);
            continue;
          }
          Material type = location.getBlock().getType();
          if (ignoreIfAir && type.isAir())
          {
            continue;
          }
          Variable.fakeBlocks.put(location, blockData.clone());
          Scheduler.fakeBlocksAsync(null, location, true);
        }
      }
    }
  }

  @Override
  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(new LocationArgument("위치", LocationType.BLOCK_POSITION), new BlockStateArgument("블록"));
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Location location = (Location) args.get(0);
      BlockData blockData = (BlockData) args.get(1);
      fakeBlock(location, blockData, false, false);
      MessageUtil.info(sender, "%s 위치에 %s 블록을 저장했습니다", location, blockData.getMaterial());
    });
    commandAPICommand.register();

    commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(
            new LocationArgument("위치1", LocationType.BLOCK_POSITION),
            new LocationArgument("위치2", LocationType.BLOCK_POSITION),
            new BlockStateArgument("블록"));
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Location location = (Location) args.get(0);
      Location location2 = (Location) args.get(1);
      BlockData blockData = (BlockData) args.get(2);
      fakeBlock(location, location2, blockData, false, false);
    });
    commandAPICommand.register();

    commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(
            new LocationArgument("위치1", LocationType.BLOCK_POSITION),
            new LocationArgument("위치2", LocationType.BLOCK_POSITION),
            new BlockStateArgument("블록"),
            new BooleanArgument("이미 존재하면 무시 여부"));
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Location location = (Location) args.get(0);
      Location location2 = (Location) args.get(1);
      BlockData blockData = (BlockData) args.get(2);
      boolean ignoreIfExists = (boolean) args.get(3);
      fakeBlock(location, location2, blockData, ignoreIfExists, false);
    });
    commandAPICommand.register();

    commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(
            new LocationArgument("위치1", LocationType.BLOCK_POSITION),
            new LocationArgument("위치2", LocationType.BLOCK_POSITION),
            new BlockStateArgument("블록"),
            new BooleanArgument("이미 존재하면 무시 여부"),
            new BooleanArgument("공기 블록 무시 여부"));
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Location location = (Location) args.get(0);
      Location location2 = (Location) args.get(1);
      BlockData blockData = (BlockData) args.get(2);
      boolean ignoreIfExists = (boolean) args.get(3);
      boolean ignoreIfAir = (boolean) args.get(4);
      fakeBlock(location, location2, blockData, ignoreIfExists, ignoreIfAir);
    });
    commandAPICommand.register();

    commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(new LocationArgument("위치", LocationType.BLOCK_POSITION), new MultiLiteralArgument("args", List.of("--remove", "--query")));
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Location location = (Location) args.get(0);
      switch ((String) args.get(1))
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
              throw CommandAPI.failWithString("해당 위치에 블록이 존재하지 않습니다");
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
            MessageUtil.info(sender, "%s 위치의 블록 유형은 %s입니다", location, Variable.fakeBlocks.get(location).getMaterial());
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
    commandAPICommand = commandAPICommand.withArguments(new MultiLiteralArgument("list", List.of("list")));
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      MessageUtil.info(sender, "총 %s개의 블록 데이터가 있습니다", Variable.fakeBlocks.keySet().size());
      int i = 0;
      for (Location location : Variable.fakeBlocks.keySet())
      {
        if (i > 20)
        {
          MessageUtil.info(sender, "... 외 %s개", Variable.fakeBlocks.keySet().size() - 20);
          break;
        }
        i++;
        BlockData blockData = Variable.fakeBlocks.get(location);
        MessageUtil.info(sender, "%s : %s", location, blockData.getMaterial());
      }
    });
    commandAPICommand.register();
  }
}

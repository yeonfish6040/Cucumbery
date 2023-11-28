package com.jho5245.cucumbery.commands.brigadier;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.Arrays;
import java.util.List;

public class CommandSetBlock2 extends CommandBase
{
  private final List<Argument<?>> argumentList = Arrays.asList(new LocationArgument("위치", LocationType.BLOCK_POSITION), new BlockStateArgument("블록 데이터"));

  private final List<Argument<?>> argumentList2 = Arrays.asList(new LocationArgument("위치", LocationType.BLOCK_POSITION), new BlockStateArgument("블록 데이터"), new BooleanArgument("블록 종류 보존 여부"));

  private final List<Argument<?>> argumentList3 = Arrays.asList(new LocationArgument("위치", LocationType.BLOCK_POSITION), new BlockStateArgument("블록 데이터"), new BooleanArgument("블록 종류 보존 여부"),
          new BooleanArgument("물리 적용 여부"));

  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(argumentList);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Location location = (Location) args.get(0);
      BlockData blockData = (BlockData) args.get(1);
      Block block = location.getBlock();
      try
      {
        block.setBlockData(blockData);
      }
      catch (Exception ignored)
      {

      }
    });
    commandAPICommand.register();

    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(argumentList2);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Location location = (Location) args.get(0);
      BlockData blockData = (BlockData) args.get(1);
      boolean preserveType = (boolean) args.get(2);
      Block block = location.getBlock();
      Material type = block.getType();
      String data = blockData.toString();
      try
      {
        data = data.split("\\[")[1].split("]")[0];
        data = "[" + data + "]";
        block.setBlockData(Bukkit.createBlockData(preserveType ? type : blockData.getMaterial(), data));
      }
      catch (Exception ignored)
      {

      }
    });
    commandAPICommand.register();

    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(argumentList3);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Location location = (Location) args.get(0);
      BlockData blockData = (BlockData) args.get(1);
      boolean preserveType = (boolean) args.get(2);
      boolean applyPhysics = (boolean) args.get(3);
      Block block = location.getBlock();
      Material type = block.getType();
      String data = blockData.toString();
      try
      {
        data = data.split("\\[")[1].split("]")[0];
        data = "[" + data + "]";
        block.setBlockData(Bukkit.createBlockData(preserveType ? type : blockData.getMaterial(), data), applyPhysics);
      }
      catch (Exception ignored)
      {

      }
    });
    commandAPICommand.register();
  }
}

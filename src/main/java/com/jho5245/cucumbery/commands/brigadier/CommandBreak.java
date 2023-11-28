package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.LocationType;
import org.bukkit.Location;
import org.bukkit.Material;

public class CommandBreak extends CommandBase
{
  @Override
  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(new LocationArgument("위치", LocationType.BLOCK_POSITION));
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Location location = (Location) args.get(0);
      location.getBlock().setType(Material.AIR);
    });
    commandAPICommand.register();
  }
}

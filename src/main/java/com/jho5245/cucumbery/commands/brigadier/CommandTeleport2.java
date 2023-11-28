package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class CommandTeleport2 extends CommandBase
{
  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.executesPlayer((sender, args) ->
    {
      Location location = (Location) args.get(0);
      if (!sender.teleport(location))
      {
        throw CommandAPI.failWithString("이동할 수 없습니다");
      }
    });
    commandAPICommand.register();

    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.executesPlayer((sender, args) ->
    {
      switch ((String) args.get(0))
      {
        case "current" -> sender.teleport(sender.getLocation());
        case "world-spawn" -> sender.teleport(sender.getWorld().getSpawnLocation());
        case "bed" -> {
          Location bed = sender.getBedSpawnLocation();
          sender.teleport(bed != null ? bed : sender.getLocation());
        }
        case "target-block" -> {
          Block block = sender.getTargetBlockExact(256);
          sender.teleport(block != null ? block.getLocation() : sender.getLocation());
        }
      }
    });
    commandAPICommand.register();
  }
}

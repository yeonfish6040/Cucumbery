package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import dev.jorel.commandapi.*;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandTeleport2 extends CommandBase
{
  private final List<Argument> args1 = new ArrayList<>(Collections.singletonList(new LocationArgument("좌표")
          .replaceWithSafeSuggestionsT(info ->
          {
            Player player = (Player) info.sender();
            Location bed = player.getBedSpawnLocation();
            boolean bedNull = bed == null;
            if (bedNull)
            {
              bed = player.getLocation();
            }
            Block target = player.getTargetBlockExact(256);
            boolean targetNull = target == null;
            Location targetLoc = targetNull ? player.getLocation() : target.getLocation();
            return Tooltip.arrayOf(
                    Tooltip.of(player.getLocation(), "현재 위치"),
                    Tooltip.of(player.getWorld().getSpawnLocation(), "월드 스폰 포인트"),
                    Tooltip.of(bed, bedNull ? "현재 위치" : "자신의 스폰 포인트"),
                    Tooltip.of(targetLoc, targetNull ? "현재 위치" : "바라보고 있는 블록")
            );
          })
  ));

  private final List<Argument> args2 = new ArrayList<>(Collections.singletonList(new MultiLiteralArgument("current", "world-spawn", "bed", "target-block")
          .replaceSuggestionsT(info -> new IStringTooltip[]{
                  StringTooltip.of("current", "현재 위치"),
                  StringTooltip.of("world-spawn", "월드 스폰 포인트"),
                  StringTooltip.of("bed", "자신의 스폰 포인트"),
                  StringTooltip.of("target-block", "바라보고 있는 블록")
          })));

  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(args1);
    commandAPICommand = commandAPICommand.executesPlayer((sender, args) ->
    {
      Location location = (Location) args[0];
      if (!sender.teleport(location))
      {
        CommandAPI.fail("이동할 수 없습니다");
      }
    });
    commandAPICommand.register();

    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(args2);
    commandAPICommand = commandAPICommand.executesPlayer((sender, args) ->
    {
      switch ((String) args[0])
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

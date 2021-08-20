package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.storage.data.Constant;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector;
import dev.jorel.commandapi.arguments.LocationArgument;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Velocity extends CommandBase
{
  private final List<Argument> arguments = new ArrayList<>();

  {
    arguments.add(new EntitySelectorArgument("개체", EntitySelector.MANY_ENTITIES));
    arguments.add(new LocationArgument("방향"));
  }

  @SuppressWarnings("unchecked")
  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(arguments);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Collection<Entity> entities = (Collection<Entity>) args[0];
      Location loc = (Location) args[1];
      double x = loc.getX(), y = loc.getY(), z = loc.getZ();
      if (Constant.Sosu2.format(x).endsWith(".5"))
      {
        x -= 0.5;
      }
      if (Constant.Sosu2.format(z).endsWith(".5"))
      {
        z -= 0.5;
      }
      final double originX = x, originY = y, originZ = x;
      boolean relative = Math.abs(x) > 5 || Math.abs(y) > 5 || Math.abs(z) > 5;
      for (Entity entity : entities)
      {
        if (relative)
        {
          Location entityLoc = entity.getLocation();
          double eX = entityLoc.getX(), eY = entityLoc.getY(), eZ = entityLoc.getZ();
          if (Math.abs(eX) > 5 || Math.abs(eY) > 5 || Math.abs(eZ) > 5)
          {
            Vector vector = entity.getVelocity();
            x = x - eX + vector.getX();
            y = y - eY + vector.getY();
            z = z - eZ + vector.getZ();
          }
        }
        if (x > 5)
        {
          x = 5;
        }
        if (x < -5)
        {
          x = -5;
        }
        if (y > 5)
        {
          y = 5;
        }
        if (y < -5)
        {
          y = -5;
        }
        if (z > 5)
        {
          z = 5;
        }
        if (z < -5)
        {
          z = -5;
        }
        entity.setVelocity(new Vector(x, y, z));
        x = originX;
        y = originY;
        z = originZ;
      }
    });
    commandAPICommand.register();
  }
}

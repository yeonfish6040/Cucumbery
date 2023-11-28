package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.storage.data.Constant;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommandVelocity extends CommandBase
{
  private final List<Argument<?>> arguments = new ArrayList<>();

  {
    arguments.add(new EntitySelectorArgument.ManyEntities("개체"));
    arguments.add(new LocationArgument("방향"));
  }
  private final List<Argument<?>> arguments2 = new ArrayList<>();

  {
    arguments2.add(new EntitySelectorArgument.ManyEntities("개체"));
    arguments2.add(new LocationArgument("방향"));
    arguments2.add(new MultiLiteralArgument("sans", List.of("absolute", "relative")));
  }
  private final List<Argument<?>> arguments3 = new ArrayList<>();

  {
    arguments3.add(new EntitySelectorArgument.ManyEntities("개체"));
    arguments3.add(new LocationArgument("방향"));
    arguments3.add(new MultiLiteralArgument("sans", List.of("absolute", "relative")));
    arguments3.add(new DoubleArgument("강도"));
  }

  private final List<Argument<?>> arguments4 = new ArrayList<>();
  {
    arguments4.add(new EntitySelectorArgument.ManyEntities("개체"));
    arguments4.add(new LocationArgument("방향"));
    arguments4.add(new MultiLiteralArgument("absolute", List.of("absolute")));
    arguments4.add(new DoubleArgument("x강도"));
    arguments4.add(new DoubleArgument("y강도"));
    arguments4.add(new DoubleArgument("z강도"));
  }

  @SuppressWarnings("unchecked")
  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(arguments);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Collection<Entity> entities = (Collection<Entity>) args.get(0);
      Location loc = (Location) args.get(1);
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

    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(arguments2);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Collection<Entity> entities = (Collection<Entity>) args.get(0);
      Location loc = (Location) args.get(1);
      String type = (String) args.get(2);
      double x = loc.getX(), y = loc.getY(), z = loc.getZ();
      switch (type)
      {
        case "relative" ->{
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
        }
        case "absolute" -> {

          for (Entity entity : entities)
          {
            Location entityLoc = entity.getLocation();
            double eX = entityLoc.getX(), eY = entityLoc.getY(), eZ = entityLoc.getZ();
            entity.setVelocity(new Vector(x - eX, y - eY, z - eZ));
          }
        }
      }
    });
    commandAPICommand.register();

    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(arguments3);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Collection<Entity> entities = (Collection<Entity>) args.get(0);
      Location loc = (Location) args.get(1);
      String type = (String) args.get(2);
      double m = (double) args.get(3);
      double x = loc.getX(), y = loc.getY(), z = loc.getZ();
      if ("absolute".equals(type))
      {
        for (Entity entity : entities)
        {
          Location entityLoc = entity.getLocation();
          double eX = entityLoc.getX(), eY = entityLoc.getY(), eZ = entityLoc.getZ();
          entity.setVelocity(new Vector((x - eX) * m, (y - eY) * m, (z - eZ) * m));
        }
      }
    });
    commandAPICommand.register();

    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(arguments4);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Collection<Entity> entities = (Collection<Entity>) args.get(0);
      Location loc = (Location) args.get(1);
      String type = (String) args.get(2);
      double xM = (double) args.get(3);
      double yM = (double) args.get(4);
      double zM = (double) args.get(5);
      double x = loc.getX(), y = loc.getY(), z = loc.getZ();
      if ("absolute".equals(type))
      {
        for (Entity entity : entities)
        {
          Location entityLoc = entity.getLocation();
          double eX = entityLoc.getX(), eY = entityLoc.getY(), eZ = entityLoc.getZ();
          entity.setVelocity(new Vector((x - eX) * xM, (y - eY) * yM, (z - eZ) * zM));
        }
      }
    });
    commandAPICommand.register();
  }
}

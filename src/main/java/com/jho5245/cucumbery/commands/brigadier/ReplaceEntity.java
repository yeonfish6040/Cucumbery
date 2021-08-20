package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.MessageUtil;
import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTEntity;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ReplaceEntity extends CommandBase
{
  private final List<Argument> argumentList = Arrays.asList(
          new EntitySelectorArgument("개체", EntitySelectorArgument.EntitySelector.MANY_ENTITIES),
          new EntityTypeArgument("개체 종류"));

  private final List<Argument> argumentList2 = Arrays.asList(
          new EntitySelectorArgument("개체", EntitySelectorArgument.EntitySelector.MANY_ENTITIES),
          new EntityTypeArgument("개체 종류"),
          new BooleanArgument("명령어 출력 숨김 여부"));

  private final List<Argument> argumentList3 = Arrays.asList(
          new EntitySelectorArgument("개체", EntitySelectorArgument.EntitySelector.MANY_ENTITIES),
          new EntityTypeArgument("개체 종류"),
          new BooleanArgument("명령어 출력 숨김 여부"),
          new GreedyStringArgument("추가 nbt"));

  @SuppressWarnings("unchecked")
  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(argumentList);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      CommandSender commandSender = sender.getCallee();
      try
      {
        commandSender.getName();
      }
      catch (Exception e)
      {
        commandSender = sender.getCaller();
      }
      Collection<Entity> entities = (Collection<Entity>) args[0];
      EntityType entityType = (EntityType) args[1];
      int successCount = 0;
      for (Entity entity : entities)
      {
        if (entity instanceof Player)
        {
          continue;
        }

        NBTEntity nbtEntity = new NBTEntity(entity);
        Location location = entity.getLocation();
        entity.remove();
        Entity newEntity = location.getWorld().spawnEntity(location, entityType);
        NBTEntity newNbtEntity = new NBTEntity(newEntity);
        newNbtEntity.mergeCompound(nbtEntity);
        successCount++;
      }
      if (successCount > 0)
      {
        String typeString = entityType.toString();
        MessageUtil.info(commandSender, "&e" + successCount + "&r개의 개체의 종류를 &e" + typeString + "&r 변경하였습니다.");
      }
      else
      {
        CommandAPI.fail("개체를 찾을 수 없습니다.");
      }
    });
    commandAPICommand.register();

    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(argumentList2);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      CommandSender commandSender = sender.getCallee();
      try
      {
        commandSender.getName();
      }
      catch (Exception e)
      {
        commandSender = sender.getCaller();
      }
      Collection<Entity> entities = (Collection<Entity>) args[0];
      EntityType entityType = (EntityType) args[1];
      boolean hideOutput = (boolean) args[2];
      int successCount = 0;
      for (Entity entity : entities)
      {
        if (entity instanceof Player)
        {
          continue;
        }

        NBTEntity nbtEntity = new NBTEntity(entity);
        Location location = entity.getLocation();
        entity.remove();
        Entity newEntity = location.getWorld().spawnEntity(location, entityType);
        NBTEntity newNbtEntity = new NBTEntity(newEntity);
        newNbtEntity.mergeCompound(nbtEntity);
        successCount++;
      }
      if (successCount > 0)
      {
        if (!hideOutput)
        {
          String typeString = entityType.toString();
          MessageUtil.info(commandSender, "&e" + successCount + "&r개의 개체의 종류를 &e" + typeString + "&r 변경하였습니다.");
        }
      }
      else
      {
        CommandAPI.fail("개체를 찾을 수 없습니다.");
      }
    });
    commandAPICommand.register();

    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(argumentList3);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      CommandSender commandSender = sender.getCallee();
      try
      {
        commandSender.getName();
      }
      catch (Exception e)
      {
        commandSender = sender.getCaller();
      }
      Collection<Entity> entities = (Collection<Entity>) args[0];
      EntityType entityType = (EntityType) args[1];
      boolean hideOutput = (boolean) args[2];
      NBTContainer nbtContainer;
      try
      {
        nbtContainer = new NBTContainer((String) args[3]);
      }
      catch (Exception e)
      {
        CommandAPI.fail("잘못된 NBT입니다: " + args[3]);
        return;
      }
      int successCount = 0;
      for (Entity entity : entities)
      {
        if (entity instanceof Player)
        {
          continue;
        }

        NBTEntity nbtEntity = new NBTEntity(entity);
        Location location = entity.getLocation();
        entity.remove();
        Entity newEntity = location.getWorld().spawnEntity(location, entityType);
        NBTEntity newNbtEntity = new NBTEntity(newEntity);
        newNbtEntity.mergeCompound(nbtEntity);
        newNbtEntity.mergeCompound(nbtContainer);
        successCount++;
      }
      if (successCount > 0)
      {
        if (!hideOutput)
        {
          String typeString = entityType.toString();
          MessageUtil.info(commandSender, "&e" + successCount + "&r개의 개체의 종류를 &e" + typeString + "&r 변경하였습니다.");
        }
      }
      else
      {
        CommandAPI.fail("개체를 찾을 수 없습니다.");
      }
    });
    commandAPICommand.register();
  }
}

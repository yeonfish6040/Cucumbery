package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTEntity;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;

import java.util.Arrays;
import java.util.List;

public class Summon extends CommandBase
{

  private final List<Argument> argumentList1 = Arrays.asList(
          new EntityTypeArgument("개체"),
          new IntegerArgument("마리수", 1, 10000));

  private final List<Argument> argumentList2 = Arrays.asList(
          new EntityTypeArgument("개체"),
          new IntegerArgument("마리수", 1, 10000),
          new LocationArgument("위치"));

  private final List<Argument> argumentList3 = Arrays.asList(
          new EntityTypeArgument("개체"),
          new IntegerArgument("마리수", 1, 10000),
          new BooleanArgument("명령어 출력 숨김 여부"));

  private final List<Argument> argumentList4 = Arrays.asList(
          new EntityTypeArgument("개체"),
          new IntegerArgument("마리수", 1, 10000),
          new LocationArgument("위치"),
          new BooleanArgument("명령어 출력 숨김 여부"));

  private final List<Argument> argumentList5 = Arrays.asList(
          new EntityTypeArgument("개체"),
    new IntegerArgument("마리수", 1, 10000),
    new LocationArgument("위치"),
          new BooleanArgument("명령어 출력 숨김 여부"),
    new GreedyStringArgument("nbt"));

  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(argumentList1);
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
      boolean success = false;
      Entity entity = null;
      EntityType entityType = (EntityType) args[0];
      int amount = (int) args[1];
      Location location;
      if (commandSender instanceof Entity)
      {
        location = ((Entity) commandSender).getLocation();
      }
      else if (commandSender instanceof BlockCommandSender)
      {
        location = ((BlockCommandSender) commandSender).getBlock().getLocation();
      }
      else
      {
        location = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
      }
      for (int i = 0; i < amount; i++)
      {
        entity = location.getWorld().spawnEntity(location, entityType);
        success = !entity.isDead();
      }
      if (!success)
      {
        CommandAPI.fail("개체를 소환할 수 없습니다.");
      }
      else
      {
        String typeString = (entity).getCustomName();
        MessageUtil.sendMessage(commandSender, Prefix.INFO, "새로운 ", ComponentUtil.senderComponent(entity),
                ComponentUtil.create(MessageUtil.getFinalConsonant(typeString, MessageUtil.ConsonantType.을를) + " &e" + amount +
                        (entity instanceof LivingEntity ? "마리" : "개") + "&r 소환하였습니다."));
      }
    });
    commandAPICommand.register();

    commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
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
      boolean success = false;
      Entity entity = null;
      EntityType entityType = (EntityType) args[0];
      int amount = (int) args[1];
      Location location = (Location) args[2];
      for (int i = 0; i < amount; i++)
      {
        entity = location.getWorld().spawnEntity(location, entityType);
        success = !entity.isDead();
      }
      if (!success)
      {
        CommandAPI.fail("개체를 소환할 수 없습니다.");
      }
      else
      {
        String typeString = (entity).getCustomName();
        MessageUtil.sendMessage(commandSender, Prefix.INFO, "새로운 ", ComponentUtil.senderComponent(entity),
                ComponentUtil.create(MessageUtil.getFinalConsonant(typeString, MessageUtil.ConsonantType.을를) + " &e" + amount +
                        (entity instanceof LivingEntity ? "마리" : "개") + "&r 소환하였습니다."));
      }
    });
    commandAPICommand.register();

    commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
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
      boolean success = false;
      Entity entity = null;
      EntityType entityType = (EntityType) args[0];
      int amount = (int) args[1];
      boolean hideOutput = (boolean) args[2];
      Location location;
      if (commandSender instanceof Entity)
      {
        location = ((Entity) commandSender).getLocation();
      }
      else if (commandSender instanceof BlockCommandSender)
      {
        location = ((BlockCommandSender) commandSender).getBlock().getLocation();
      }
      else
      {
        location = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
      }
      for (int i = 0; i < amount; i++)
      {
        entity = location.getWorld().spawnEntity(location, entityType);
        success = !entity.isDead();
      }
      if (!success)
      {
        CommandAPI.fail("개체를 소환할 수 없습니다.");
      }
      else if (!hideOutput)
      {
        String typeString = (entity).getCustomName();
        MessageUtil.sendMessage(commandSender, Prefix.INFO, "새로운 ", ComponentUtil.senderComponent(entity),
                ComponentUtil.create(MessageUtil.getFinalConsonant(typeString, MessageUtil.ConsonantType.을를) + " &e" + amount +
                        (entity instanceof LivingEntity ? "마리" : "개") + "&r 소환하였습니다."));
      }
    });
    commandAPICommand.register();

    commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(argumentList4);
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
      boolean success = false;
      Entity entity = null;
      EntityType entityType = (EntityType) args[0];
      int amount = (int) args[1];
      Location location = (Location) args[2];
      boolean hideOutput = (boolean) args[3];
      for (int i = 0; i < amount; i++)
      {
        entity = location.getWorld().spawnEntity(location, entityType);
        success = !entity.isDead();
      }
      if (!success)
      {
        CommandAPI.fail("개체를 소환할 수 없습니다.");
      }
      else if (!hideOutput)
      {
        String typeString = (entity).getCustomName();
        MessageUtil.sendMessage(commandSender, Prefix.INFO, "새로운 ", ComponentUtil.senderComponent(entity),
                ComponentUtil.create(MessageUtil.getFinalConsonant(typeString, MessageUtil.ConsonantType.을를) + " &e" + amount +
                        (entity instanceof LivingEntity ? "마리" : "개") + "&r 소환하였습니다."));
      }
    });
    commandAPICommand.register();

    commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(argumentList5);
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
      boolean success = false;
      Entity entity = null;
      EntityType entityType = (EntityType) args[0];
      int amount = (int) args[1];
      Location location = (Location) args[2];
      boolean hideOutput = (boolean) args[3];
      NBTContainer nbtContainer;
      try
      {
        nbtContainer = new NBTContainer((String) args[4]);
      }
      catch (Exception e)
      {
        CommandAPI.fail("잘못된 NBT입니다: " + args[4]);
        return;
      }
      for (int i = 0; i < amount; i++)
      {
        entity = location.getWorld().spawnEntity(location, entityType);
        NBTEntity nbtEntity = new NBTEntity(entity);
        nbtEntity.mergeCompound(nbtContainer);
        if (entity instanceof Item itemEntity)
        {
          Method.updateItem(itemEntity);
          nbtEntity.mergeCompound(nbtContainer);
        }
        success = !entity.isDead();
      }
      if (!success)
      {
        CommandAPI.fail("개체를 소환할 수 없습니다.");
      }
      else if (!hideOutput)
      {
        String typeString = (entity).getCustomName();
        MessageUtil.sendMessage(commandSender, Prefix.INFO, "새로운 ", ComponentUtil.senderComponent(entity),
                ComponentUtil.create(MessageUtil.getFinalConsonant(typeString, MessageUtil.ConsonantType.을를) + " &e" + amount +
                        (entity instanceof LivingEntity ? "마리" : "개") + "&r 소환하였습니다."));
      }
    });
    commandAPICommand.register();
  }
}

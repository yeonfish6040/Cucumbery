package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CommandReplaceEntity extends CommandBase
{
  private final List<Argument<?>> argumentList = Arrays.asList(
          new EntitySelectorArgument.ManyEntities("개체"),
          new EntityTypeArgument("개체 종류"));

  private final List<Argument<?>> argumentList2 = Arrays.asList(
          new EntitySelectorArgument.ManyEntities("개체"),
          new EntityTypeArgument("개체 종류"),
          new BooleanArgument("명령어 출력 숨김 여부"));

  private final List<Argument<?>> argumentList3 = Arrays.asList(
          new EntitySelectorArgument.ManyEntities("개체"),
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
      Collection<Entity> entities = (Collection<Entity>) args.get(0);
      List<Entity> successEntities = new ArrayList<>();
      List<Entity> resultEntities = new ArrayList<>();
      EntityType entityType = (EntityType) args.get(1);
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
        successEntities.add(entity);
        resultEntities.add(newEntity);
      }
      List<Entity> failureEntities = new ArrayList<>(entities);
      failureEntities.removeAll(successEntities);
      if (failureEntities.size() > 0)
      {
        MessageUtil.sendWarnOrError(successEntities.isEmpty(), sender,
                ComponentUtil.translate("%s은(는) 플레이어여서 개체 유형을 변경할 수 없습니다", failureEntities));
      }
      if (successEntities.size() > 0)
      {
        MessageUtil.info(sender, ComponentUtil.translate("%s의 개체 유형을 %s(으)로 변경했습니다", successEntities, resultEntities));
      }
      else if (!(sender.getCallee() instanceof Player))
      {
        throw CommandAPI.failWithString("개체를 찾을 수 없습니다");
      }
    });
    commandAPICommand.register();

    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(argumentList2);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      CommandSender commandSender = sender.getCallee();
      Collection<Entity> entities = (Collection<Entity>) args.get(0);
      List<Entity> successEntities = new ArrayList<>();
      List<Entity> resultEntities = new ArrayList<>();
      EntityType entityType = (EntityType) args.get(1);
      boolean hideOutput = (boolean) args.get(2);
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
        successEntities.add(entity);
        resultEntities.add(newEntity);
      }
      if (!hideOutput)
      {
        List<Entity> failureEntities = new ArrayList<>(entities);
        failureEntities.removeAll(successEntities);
        if (failureEntities.size() > 0)
        {
          MessageUtil.sendWarnOrError(successEntities.isEmpty(), sender,
                  ComponentUtil.translate("%s은(는) 플레이어여서 개체 유형을 변경할 수 없습니다", failureEntities));
        }
      }
      if (!hideOutput && successEntities.size() > 0)
      {
        MessageUtil.info(sender, ComponentUtil.translate("%s의 개체 유형을 %s(으)로 변경했습니다", successEntities, resultEntities));
      }
      else if (!(sender.getCallee() instanceof Player))
      {
        throw CommandAPI.failWithString("개체를 찾을 수 없습니다");
      }
    });
    commandAPICommand.register();

    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(argumentList3);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      CommandSender commandSender = sender.getCallee();
      Collection<Entity> entities = (Collection<Entity>) args.get(0);
      EntityType entityType = (EntityType) args.get(1);
      boolean hideOutput = (boolean) args.get(2);
      NBTContainer nbtContainer;
      try
      {
        nbtContainer = new NBTContainer((String) args.get(3));
      }
      catch (Exception e)
      {
        throw CommandAPI.failWithString("잘못된 NBT입니다: " + args.get(3));
      }

      List<Entity> successEntities = new ArrayList<>();
      List<Entity> resultEntities = new ArrayList<>();
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
        successEntities.add(entity);
        resultEntities.add(newEntity);
      }
      if (!hideOutput)
      {
        List<Entity> failureEntities = new ArrayList<>(entities);
        failureEntities.removeAll(successEntities);
        if (failureEntities.size() > 0)
        {
          MessageUtil.sendWarnOrError(successEntities.isEmpty(), sender,
                  ComponentUtil.translate("%s은(는) 플레이어여서 개체 유형을 변경할 수 없습니다", failureEntities));
        }
      }
      if (!hideOutput && successEntities.size() > 0)
      {
        MessageUtil.info(sender, ComponentUtil.translate("%s의 개체 유형을 %s(으)로 변경했습니다", successEntities, resultEntities));
      }
      else if (!(sender.getCallee() instanceof Player))
      {
        throw CommandAPI.failWithString("개체를 찾을 수 없습니다");
      }
    });
    commandAPICommand.register();
  }
}

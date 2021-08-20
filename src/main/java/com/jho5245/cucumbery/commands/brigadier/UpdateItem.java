package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.MessageUtil;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UpdateItem extends CommandBase
{
  private final List<Argument> argument = new ArrayList<>();

  {
    argument.add(new EntitySelectorArgument("아이템", EntitySelectorArgument.EntitySelector.MANY_ENTITIES));
  }

  private final List<Argument> argument2 = new ArrayList<>();

  {
    argument2.add(new EntitySelectorArgument("아이템", EntitySelectorArgument.EntitySelector.MANY_ENTITIES));
    argument2.add(new BooleanArgument("명령어 출력 숨김 여부"));
  }

  @SuppressWarnings("unchecked")
  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(argument);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Collection<Entity> entities = (Collection<Entity>) args[0];
      int successCount = 0;
      for (Entity entity : entities)
      {
        if (entity instanceof Item)
        {
          successCount++;
          Item itemEntity = (Item) entity;
          Method.updateItem(itemEntity);
        }
      }
      if (successCount > 0)
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

        MessageUtil.info(commandSender, "&e" + successCount + "개&r의 아이템의 태그를 업데이트 하였습니다.");
      }
      else
      {
        CommandAPI.fail("아이템을 찾을 수 없습니다.");
      }
    });
    commandAPICommand.register();


    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(argument2);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Collection<Entity> entities = (Collection<Entity>) args[0];
      boolean hideOutout = (boolean) args[1];
      int successCount = 0;
      for (Entity entity : entities)
      {
        if (entity instanceof Item itemEntity)
        {
          successCount++;
          Method.updateItem(itemEntity);
        }
      }
      if (successCount > 0)
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
        if (!hideOutout)
        {
          MessageUtil.info(commandSender, "&e" + successCount + "개&r의 아이템의 태그를 업데이트 하였습니다.");
        }
      }
      else if (!hideOutout)
      {
        CommandAPI.fail("아이템을 찾을 수 없습니다.");
      }
    });
    commandAPICommand.register();
  }
}

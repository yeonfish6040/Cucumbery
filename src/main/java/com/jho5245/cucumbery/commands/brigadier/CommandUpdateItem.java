package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommandUpdateItem extends CommandBase
{
  private final List<Argument<?>> argument = new ArrayList<>();

  {
    argument.add(new EntitySelectorArgument.ManyEntities("아이템"));
  }

  private final List<Argument<?>> argument2 = new ArrayList<>();

  {
    argument2.add(new EntitySelectorArgument.ManyEntities("아이템"));
    argument2.add(new BooleanArgument("명령어 출력 숨김 여부"));
  }

  @SuppressWarnings("unchecked")
  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(argument);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Collection<Entity> entities = (Collection<Entity>) args.get(0);
      List<Entity> successEntities = new ArrayList<>();
      for (Entity entity : entities)
      {
        if (entity instanceof Item itemEntity)
        {
          successEntities.add(itemEntity);
          Method.updateItem(itemEntity);
        }
      }
      if (!successEntities.isEmpty())
      {
        MessageUtil.info(sender, ComponentUtil.translate("%s의 아이템 태그를 업데이트 했습니다", successEntities));
        MessageUtil.sendAdminMessage(sender, "%s의 아이템 태그를 업데이트 했습니다", successEntities);
      }
      else
      {
        throw CommandAPI.failWithString("아이템을 찾을 수 없습니다");
      }
    });
    commandAPICommand.register();


    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(argument2);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Collection<Entity> entities = (Collection<Entity>) args.get(0);
      boolean hideOutout = (boolean) args.get(1);
      List<Entity> successEntities = new ArrayList<>();
      for (Entity entity : entities)
      {
        if (entity instanceof Item itemEntity)
        {
          successEntities.add(itemEntity);
          Method.updateItem(itemEntity);
        }
      }
      if (!successEntities.isEmpty())
      {
        if (!hideOutout)
        {
          MessageUtil.info(sender, ComponentUtil.translate("%s의 아이템 태그를 업데이트 했습니다", successEntities));
          MessageUtil.sendAdminMessage(sender, "%s의 아이템 태그를 업데이트 했습니다]", successEntities);
        }
      }
      else
      {
        throw CommandAPI.failWithString("아이템을 찾을 수 없습니다");
      }
    });
    commandAPICommand.register();
  }
}

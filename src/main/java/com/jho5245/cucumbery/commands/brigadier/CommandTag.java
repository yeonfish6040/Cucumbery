package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelector;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("unchecked")
public class CommandTag extends CommandBase
{
  @Override
  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(new EntitySelectorArgument<>("targets", EntitySelector.MANY_ENTITIES), new MultiLiteralArgument("remove"), new MultiLiteralArgument("*"));
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Collection<Entity> entities = new ArrayList<>((Collection<Entity>) args[0]);
      entities.removeIf(entity -> entity.getScoreboardTags().isEmpty());
      if (entities.isEmpty())
      {
        throw CommandAPI.fail("태그를 가지고 있는 개체가 없습니다");
      }
      entities.forEach(entity -> new ArrayList<>(entity.getScoreboardTags()).forEach(entity::removeScoreboardTag));
      MessageUtil.sendMessage(sender, "%s에게서 모든 태그를 제거했습니다", entities);
    });
    commandAPICommand.register();
  }
}

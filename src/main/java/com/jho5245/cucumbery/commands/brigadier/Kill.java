package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Kill extends CommandBase
{
  private final List<Argument> arguments = new ArrayList<>();
  private final List<Argument> arguments2 = new ArrayList<>();

  {
    arguments.add(new EntitySelectorArgument("개체", EntitySelector.MANY_ENTITIES));
  }

  {
    arguments2.add(new EntitySelectorArgument("개체", EntitySelector.MANY_ENTITIES));
    arguments2.add(new BooleanArgument("명령어 출력 숨김 여부"));
  }

  @SuppressWarnings("unchecked")
  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      CommandSender commandSender = sender.getCallee();
      if (commandSender instanceof Entity entity)
      {
        if (commandSender instanceof Player player)
        {
          player.setHealth(0);
        }
        else
        {
          entity.remove();
        }

        MessageUtil.info(sender, ComponentUtil.createTranslate("commands.kill.success.single", sender));
        MessageUtil.sendAdminMessage(sender, null, ComponentUtil.createTranslate("[%s: %s]", sender,
                ComponentUtil.createTranslate("commands.kill.success.single", sender)
        ));
      }
      else
      {
        CommandAPI.fail("개체를 찾을 수 없습니다.");
      }
    });
    commandAPICommand.register();

    /*
     * /ckill2 <개체>
     */
    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(arguments);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      CommandSender commandSender = sender.getCallee();
      Collection<Entity> entities = (Collection<Entity>) args[0];
      List<Entity> successEntities = new ArrayList<>();
      for (Entity entity : entities)
      {
        if (entity instanceof Player player)
        {
          if (player.isDead())
          {
            continue;
          }
          player.setHealth(0);
          if (player.isDead() || player.getHealth() == 0)
          {
            successEntities.add(player);
          }
        }
        else
        {
          entity.remove();
          successEntities.add(entity);
        }
      }
      List<Entity> failureEntities = new ArrayList<>(entities);
      failureEntities.removeAll(successEntities);
      if (!failureEntities.isEmpty())
      {
        MessageUtil.sendWarnOrError(!successEntities.isEmpty() ? MessageUtil.SendMessageType.WARN : MessageUtil.SendMessageType.ERROR,
                sender, ComponentUtil.createTranslate("%s은(는) 이미 죽어 있는 상태여서 죽일 수 없습니다.", failureEntities));
      }
      if (!successEntities.isEmpty())
      {
        MessageUtil.info(successEntities, ComponentUtil.createTranslate("%s이(가) 당신을 죽였습니다.", sender));
        MessageUtil.info(sender, ComponentUtil.createTranslate("commands.kill.success.single", successEntities));
        MessageUtil.sendAdminMessage(sender, new ArrayList<>(successEntities), ComponentUtil.createTranslate("[%s: %s]", sender,
                ComponentUtil.createTranslate("commands.kill.success.single", successEntities)
        ));
      }
      else if (!(commandSender instanceof Player))
      {
        CommandAPI.fail("유효한 개체를 찾을 수 없습니다.");
      }
    });
    commandAPICommand.register();
    /*
     * /ckill2 <갸체> [월드]
     */
    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(arguments2);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      CommandSender commandSender = sender.getCallee();
      Collection<Entity> entities = (Collection<Entity>) args[0];
      boolean hideOutput = (boolean) args[1];
      List<Entity> successEntities = new ArrayList<>();
      for (Entity entity : entities)
      {
        if (entity instanceof Player player)
        {
          if (player.isDead())
          {
            continue;
          }
          player.setHealth(0);
          if (player.isDead() || player.getHealth() == 0)
          {
            successEntities.add(player);
          }
        }
        else
        {
          entity.remove();
          successEntities.add(entity);
        }
      }
      if (!hideOutput)
      {
        List<Entity> failureEntities = new ArrayList<>(entities);
        failureEntities.removeAll(successEntities);
        if (!failureEntities.isEmpty())
        {
          MessageUtil.sendWarnOrError(!successEntities.isEmpty() ? MessageUtil.SendMessageType.WARN : MessageUtil.SendMessageType.ERROR,
                  sender, ComponentUtil.createTranslate("%s은(는) 이미 죽어 있는 상태여서 죽일 수 없습니다.", failureEntities));
        }
      }
      if (!hideOutput && !successEntities.isEmpty())
      {
        MessageUtil.info(successEntities, ComponentUtil.createTranslate("%s이(가) 당신을 죽였습니다.", sender));
        MessageUtil.info(sender, ComponentUtil.createTranslate("commands.kill.success.single", successEntities));
        MessageUtil.sendAdminMessage(sender, new ArrayList<>(successEntities), ComponentUtil.createTranslate("[%s: %s]", sender,
                ComponentUtil.createTranslate("commands.kill.success.single", successEntities)
        ));
      }
      else if (!(commandSender instanceof Player))
      {
        CommandAPI.fail("유효한 개체를 찾을 수 없습니다.");
      }
    });
    commandAPICommand.register();
  }
}

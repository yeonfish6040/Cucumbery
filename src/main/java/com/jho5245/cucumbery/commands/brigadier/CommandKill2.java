package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.jho5245.cucumbery.commands.brigadier.base.ArgumentUtil.HIDE_OUTPUT;
import static com.jho5245.cucumbery.commands.brigadier.base.ArgumentUtil.MANY_ENTITIES;

public class CommandKill2 extends CommandBase
{
  private final List<Argument<?>> arguments = List.of(MANY_ENTITIES);
  private final List<Argument<?>> arguments2 = List.of(MANY_ENTITIES, HIDE_OUTPUT);

  private void kill(@NotNull NativeProxyCommandSender sender, @Nullable Collection<Entity> entities, boolean hideOutput) throws WrapperCommandSyntaxException
  {
    CommandSender commandSender = sender.getCallee();
    if (entities == null)
    {
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
        MessageUtil.info(sender, ComponentUtil.translate("commands.kill.success.single", sender));
        MessageUtil.sendAdminMessage(sender, "commands.kill.success.single", sender);
      }
      else
      {
        throw CommandAPI.failWithString("개체를 찾을 수 없습니다");
      }
    }
    else
    {
      if (entities.isEmpty())
      {
        if (commandSender instanceof BlockCommandSender)
        {
          throw CommandAPI.failWithString("개체를 찾을 수 없습니다");
        }
        else
        {
          MessageUtil.sendError(commandSender, "개체를 찾을 수 없습니다");
        }
        return;
      }
      List<Entity> successEntities = new ArrayList<>();
      for (Entity entity : entities)
      {
        if (entity instanceof Player player)
        {
          if (!UserData.GOD_MODE.getBoolean(player) && (player.isDead() || player.getHealth() == 0))
          {
            continue;
          }
          player.setLastDamageCause(new EntityDamageEvent(player, DamageCause.VOID, Double.MAX_VALUE));
          player.setLastDamage(Double.MAX_VALUE);
          player.setHealth(0);
          successEntities.add(player);
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
        boolean successEntitiesIsEmpty = successEntities.isEmpty();
        if (!failureEntities.isEmpty())
        {
          MessageUtil.sendWarnOrError(successEntitiesIsEmpty,
                  sender, ComponentUtil.translate("%s은(는) 이미 죽어 있는 상태여서 죽일 수 없습니다", failureEntities));
        }
        if (!successEntitiesIsEmpty)
        {
          MessageUtil.info(sender, ComponentUtil.translate("commands.kill.success.single", successEntities));
          MessageUtil.info(successEntities, ComponentUtil.translate("%s이(가) 당신을 죽였습니다", sender));
          MessageUtil.sendAdminMessage(sender, "commands.kill.success.single", successEntities);
        }
      }
      else if (successEntities.isEmpty() && commandSender instanceof BlockCommandSender)
      {
        MessageUtil.sendMessage(commandSender, "유효한 개체를 찾을 수 없습니다");
      }
    }
  }

  @SuppressWarnings("unchecked")
  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      kill(sender, null, false);
    });
    commandAPICommand.register();

    /*
     * /ckill2 <개체>
     */
    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(arguments);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      kill(sender, (Collection<Entity>) args.get(0), false);
    });
    commandAPICommand.register();
    /*
     * /ckill2 <갸체> [월드]
     */
    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(arguments2);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      kill(sender, (Collection<Entity>) args.get(0), (boolean) args.get(1));
    });
    commandAPICommand.register();
  }
}

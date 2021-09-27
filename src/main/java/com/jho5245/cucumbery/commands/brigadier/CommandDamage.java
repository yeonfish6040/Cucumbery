package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.commands.brigadier.base.ArgumentUtil;
import com.jho5245.cucumbery.commands.brigadier.base.ArgumentUtil.ArgumentType;
import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.jho5245.cucumbery.commands.brigadier.base.ArgumentUtil.HIDE_OUTPUT;
import static com.jho5245.cucumbery.commands.brigadier.base.ArgumentUtil.MANY_ENTITIES;

public class CommandDamage extends CommandBase
{
  final private List<Argument> list1 = List.of(MANY_ENTITIES, ArgumentUtil.of(ArgumentType.DOUBLE, 0));
  final private List<Argument> list2 = List.of(MANY_ENTITIES, ArgumentUtil.of(ArgumentType.DOUBLE, 0), HIDE_OUTPUT);
  final private List<Argument> list3 = List.of(MANY_ENTITIES, ArgumentUtil.of(ArgumentType.DOUBLE, 0), ArgumentUtil.of(ArgumentType.ONE_ENTITY, "가해 개체"));
  final private List<Argument> list4 = List.of(MANY_ENTITIES, ArgumentUtil.of(ArgumentType.DOUBLE, 0), ArgumentUtil.of(ArgumentType.ONE_ENTITY, "가해 개체"), HIDE_OUTPUT);

  private void damage(@NotNull NativeProxyCommandSender sender, @NotNull Collection<Entity> entities, double damage, @Nullable Entity damager, boolean hideOutput) throws WrapperCommandSyntaxException
  {
    CommandSender commandSender = sender.getCallee();
    if (entities.isEmpty())
    {
      if (commandSender instanceof BlockCommandSender)
      {
        CommandAPI.fail("개체를 찾을 수 없습니다.");
      }
      else
      {
        MessageUtil.sendError(commandSender, "개체를 찾을 수 없습니다.");
      }
      return;
    }
    List<Entity> successEntities = new ArrayList<>();
    for (Entity entity : entities)
    {
      if (entity instanceof LivingEntity livingEntity)
      {
        if (!livingEntity.isDead())
        {
          double hp = livingEntity.getHealth();
          if (damager == null)
          {
            livingEntity.damage(damage);
          }
          else
          {
            if (!(damager instanceof LivingEntity))
            {
              EntityDamageByEntityEvent damageByEntityEvent = new EntityDamageByEntityEvent(damager, entity, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage);
              Cucumbery.getPlugin().getPluginManager().callEvent(damageByEntityEvent);
            }
            livingEntity.damage(damage, damager);
          }
          if (hp != livingEntity.getHealth())
          {
            successEntities.add(livingEntity);
          }
        }
      }
    }
    String failureReason = "(무적 시간 or 크리에이티브 모드 or PvP 불가능 구역 or 무적 모드로 인한 사망 후 즉시 부활)";
    if (!hideOutput)
    {
      List<Entity> failureEntities = new ArrayList<>(entities);
      failureEntities.removeAll(successEntities);
      boolean successEntitiesIsEmpty = successEntities.isEmpty();
      if (!failureEntities.isEmpty())
      {
        if (damager == null)
        {
          MessageUtil.sendWarnOrError(successEntitiesIsEmpty, sender,
                  ComponentUtil.createTranslate("%s에게 피해를 입힐 수 없습니다. " + failureReason, failureEntities));
        }
        else
        {
          MessageUtil.sendWarnOrError(successEntitiesIsEmpty, sender,
                  ComponentUtil.createTranslate("%s에게 %s을(를) 가해 개체로 하는 피해를 입힐 수 없습니다. " + failureReason, failureEntities, damager));
        }
      }
      if (!successEntitiesIsEmpty)
      {
        String damageString = Constant.THE_COLOR_HEX + Constant.Sosu15.format(damage);
        if (damager == null)
        {
          MessageUtil.info(sender, ComponentUtil.createTranslate("%s에게 %s만큼의 피해를 주었습니다.", successEntities, damageString));
          MessageUtil.sendAdminMessage(sender, new ArrayList<>(successEntities),
                  ComponentUtil.createTranslate("[%s: %s에게 %s만큼의 피해를 주었습니다.]", sender, successEntities, damageString));
        }
        else
        {
          MessageUtil.info(sender, ComponentUtil.createTranslate("%s에게 %s을(를) 가해 개체로 하는 %s만큼의 피해를 주었습니다.", successEntities, damager, damageString));
          MessageUtil.sendAdminMessage(sender, new ArrayList<>(successEntities),
                  ComponentUtil.createTranslate("[%s: %s에게 %s을(를) 가해 개체로 하는 %s만큼의 피해를 주었습니다.]", sender, successEntities, damager, damageString));
        }
      }
    }
    else if (successEntities.isEmpty() && commandSender instanceof BlockCommandSender)
    {
      CommandAPI.fail("조건에 맞는 개체를 찾을 수 없거나 피해를 줄 수 없는 상태입니다. " + failureReason);
    }
  }

  @SuppressWarnings("unchecked")
  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(list1);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      damage(sender, (Collection<Entity>) args[0], (double) args[1], null, false);
    });
    commandAPICommand.register();

    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(list2);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      damage(sender, (Collection<Entity>) args[0], (double) args[1], null, (boolean) args[2]);
    });
    commandAPICommand.register();


    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(list3);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      damage(sender, (Collection<Entity>) args[0], (double) args[1], (Entity) args[2], false);
    });
    commandAPICommand.register();

    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(list4);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      damage(sender, (Collection<Entity>) args[0], (double) args[1], (Entity) args[2], (boolean) args[3]);
    });
    commandAPICommand.register();
  }
}

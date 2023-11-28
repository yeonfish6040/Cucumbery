package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
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
  final private DoubleArgument DAMAGE = new DoubleArgument("대미지", 0);
  final private IntegerArgument INVINCIBLE = new IntegerArgument("무적 시간(틱)", 0, 200);
  final private MultiLiteralArgument NO_INVINCIBLE = new MultiLiteralArgument("-1", List.of("-1"));
  final private EntitySelectorArgument.OneEntity DAMAGER = new EntitySelectorArgument.OneEntity("가해 개체");
  final private List<Argument<?>> list01 = List.of(MANY_ENTITIES, DAMAGE);
  final private List<Argument<?>> list02 = List.of(MANY_ENTITIES, DAMAGE, HIDE_OUTPUT);
  final private List<Argument<?>> list03 = List.of(MANY_ENTITIES, DAMAGE, INVINCIBLE);
  final private List<Argument<?>> list04 = List.of(MANY_ENTITIES, DAMAGE, INVINCIBLE, HIDE_OUTPUT);
  final private List<Argument<?>> list05 = List.of(MANY_ENTITIES, DAMAGE, NO_INVINCIBLE);
  final private List<Argument<?>> list06 = List.of(MANY_ENTITIES, DAMAGE, NO_INVINCIBLE, HIDE_OUTPUT);
  final private List<Argument<?>> list07 = List.of(MANY_ENTITIES, DAMAGE, DAMAGER);
  final private List<Argument<?>> list08 = List.of(MANY_ENTITIES, DAMAGE, DAMAGER, HIDE_OUTPUT);
  final private List<Argument<?>> list09 = List.of(MANY_ENTITIES, DAMAGE, INVINCIBLE, DAMAGER);
  final private List<Argument<?>> list10 = List.of(MANY_ENTITIES, DAMAGE, INVINCIBLE, DAMAGER, HIDE_OUTPUT);
  final private List<Argument<?>> list11 = List.of(MANY_ENTITIES, DAMAGE, NO_INVINCIBLE, DAMAGER);
  final private List<Argument<?>> list12 = List.of(MANY_ENTITIES, DAMAGE, NO_INVINCIBLE, DAMAGER, HIDE_OUTPUT);

  private void damage(@NotNull NativeProxyCommandSender sender, @NotNull Collection<Entity> entities, double damage, boolean hideOutput) throws WrapperCommandSyntaxException
  {
    damage(sender, entities, damage, -1, hideOutput);
  }

  private void damage(@NotNull NativeProxyCommandSender sender, @NotNull Collection<Entity> entities, double damage, int invincibleTimeTicks, boolean hideOutput) throws WrapperCommandSyntaxException
  {
    damage(sender, entities, damage, invincibleTimeTicks, null, hideOutput);
  }

  private void damage(@NotNull NativeProxyCommandSender sender, @NotNull Collection<Entity> entities, double damage, @Nullable Entity damager, boolean hideOutput) throws WrapperCommandSyntaxException
  {
    damage(sender, entities, damage, -1, damager, hideOutput);
  }

  private void damage(@NotNull NativeProxyCommandSender sender, @NotNull Collection<Entity> entities, double damage, int invincibleTimeTicks, @Nullable Entity damager, boolean hideOutput) throws WrapperCommandSyntaxException
  {
    CommandSender commandSender = sender.getCallee();
    if (entities.isEmpty())
    {
      if (commandSender instanceof BlockCommandSender)
      {
        throw CommandAPI.failWithString("개체를 찾을 수 없습니다");
      }
      else if (!hideOutput)
      {
        MessageUtil.sendError(commandSender, "개체를 찾을 수 없습니다");
      }
      return;
    }
    List<Entity> successEntities = new ArrayList<>();
    for (Entity entity : entities)
    {
      if (entity instanceof LivingEntity livingEntity)
      {
        if (livingEntity.isDead())
        {
          continue;
        }
        if (livingEntity.getNoDamageTicks() > 0)
        {
          continue;
        }
        double hp = livingEntity.getHealth();
        if (damager == null)
        {
          livingEntity.damage(damage);
        }
        else
        {
          if (!(damager instanceof LivingEntity))
          {
            @SuppressWarnings("deprecation")
            EntityDamageByEntityEvent damageByEntityEvent = new EntityDamageByEntityEvent(damager, entity, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage);
            Cucumbery.getPlugin().getPluginManager().callEvent(damageByEntityEvent);
          }
          livingEntity.damage(damage, damager);
        }
        if (invincibleTimeTicks != -1)
        {
          livingEntity.setNoDamageTicks(invincibleTimeTicks / 2);
        }
        if (hp != livingEntity.getHealth())
        {
          successEntities.add(livingEntity);
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
                  ComponentUtil.translate("%s에게 피해를 입힐 수 없습니다. " + failureReason, failureEntities));
        }
        else
        {
          MessageUtil.sendWarnOrError(successEntitiesIsEmpty, sender,
                  ComponentUtil.translate("%s에게 %s을(를) 가해 개체로 하는 피해를 입힐 수 없습니다. " + failureReason, failureEntities, damager));
        }
      }
      if (!successEntitiesIsEmpty)
      {
        String damageString = Constant.THE_COLOR_HEX + Constant.Sosu15.format(damage);
        if (damager == null)
        {
          MessageUtil.info(sender, ComponentUtil.translate("%s에게 %s만큼의 피해를 주었습니다", successEntities, damageString));
          MessageUtil.sendAdminMessage(sender, "%s에게 %s만큼의 피해를 주었습니다", successEntities, damageString);
        }
        else
        {
          MessageUtil.info(sender, ComponentUtil.translate("%s에게 %s을(를) 가해 개체로 하는 %s만큼의 피해를 주었습니다", successEntities, damager, damageString));
          MessageUtil.sendAdminMessage(sender, "%s에게 %s을(를) 가해 개체로 하는 %s만큼의 피해를 주었습니다", successEntities, damager, damageString);
        }
      }
    }
    else if (successEntities.isEmpty() && commandSender instanceof BlockCommandSender)
    {
      throw CommandAPI.failWithString("조건에 맞는 개체를 찾을 수 없거나 피해를 줄 수 없는 상태입니다. " + failureReason);
    }
  }

  @SuppressWarnings("unchecked")
  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand = getCommandBase(command, permission, aliases);
    {
      commandAPICommand = commandAPICommand.withArguments(list01);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        damage(sender, (Collection<Entity>) args.get(0), (double) args.get(1), false);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list02);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        damage(sender, (Collection<Entity>) args.get(0), (double) args.get(1), (boolean) args.get(2));
      });
      commandAPICommand.register();


      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list03);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        damage(sender, (Collection<Entity>) args.get(0), (double) args.get(1), (int) args.get(2), false);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list04);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        damage(sender, (Collection<Entity>) args.get(0), (double) args.get(1), (int) args.get(2), (boolean) args.get(3));
      });
      commandAPICommand.register();


      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list05);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        damage(sender, (Collection<Entity>) args.get(0), (double) args.get(1), -1, false);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list06);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        damage(sender, (Collection<Entity>) args.get(0), (double) args.get(1), -1, (boolean) args.get(3));
      });
      commandAPICommand.register();
    }

    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(list07);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      damage(sender, (Collection<Entity>) args.get(0), (double) args.get(1), (Entity) args.get(2), false);
    });
    commandAPICommand.register();

    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(list08);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      damage(sender, (Collection<Entity>) args.get(0), (double) args.get(1), (Entity) args.get(2), (boolean) args.get(3));
    });
    commandAPICommand.register();


    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(list09);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      damage(sender, (Collection<Entity>) args.get(0), (double) args.get(1), (int) args.get(2), (Entity) args.get(3), false);
    });
    commandAPICommand.register();

    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(list10);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      damage(sender, (Collection<Entity>) args.get(0), (double) args.get(1), (int) args.get(2), (Entity) args.get(3), (boolean) args.get(4));
    });
    commandAPICommand.register();


    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(list11);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      damage(sender, (Collection<Entity>) args.get(0), (double) args.get(1), -1, (Entity) args.get(3), false);
    });
    commandAPICommand.register();

    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(list12);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      damage(sender, (Collection<Entity>) args.get(0), (double) args.get(1), -1, (Entity) args.get(3), (boolean) args.get(4));
    });
    commandAPICommand.register();
  }
}

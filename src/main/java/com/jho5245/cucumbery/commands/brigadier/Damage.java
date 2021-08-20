package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Damage extends CommandBase
{
  final private List<Argument> argumentList1 = new ArrayList<>();

  {
    argumentList1.add(new EntitySelectorArgument("개체", EntitySelectorArgument.EntitySelector.MANY_ENTITIES));
    argumentList1.add(new DoubleArgument("대미지", 0));
  }

  final private List<Argument> argumentList2 = new ArrayList<>();

  {
    argumentList2.add(new EntitySelectorArgument("개체", EntitySelectorArgument.EntitySelector.MANY_ENTITIES));
    argumentList2.add(new DoubleArgument("대미지", 0));
    argumentList2.add(new BooleanArgument("명령어 출력 숨김 여부"));
  }

  final private List<Argument> argumentList3 = new ArrayList<>();

  {
    argumentList3.add(new EntitySelectorArgument("개체", EntitySelectorArgument.EntitySelector.MANY_ENTITIES));
    argumentList3.add(new DoubleArgument("대미지", 0));
    argumentList3.add(new EntitySelectorArgument("가해 개체", EntitySelectorArgument.EntitySelector.ONE_ENTITY));
  }

  final private List<Argument> argumentList4 = new ArrayList<>();

  {
    argumentList4.add(new EntitySelectorArgument("개체", EntitySelectorArgument.EntitySelector.MANY_ENTITIES));
    argumentList4.add(new DoubleArgument("대미지", 0));
    argumentList4.add(new EntitySelectorArgument("가해 개체", EntitySelectorArgument.EntitySelector.ONE_ENTITY));
    argumentList4.add(new BooleanArgument("명령어 출력 숨김 여부"));
  }

  @SuppressWarnings("unchecked")
  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand = getCommandBase(command, permission, aliases);
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
      Collection<Entity> entities = (Collection<Entity>) args[0];
      double damage = (double) args[1];
      int successCount = 0;
      for (Entity entity : entities)
      {
        if (entity instanceof LivingEntity)
        {
          LivingEntity livingEntity = (LivingEntity) entity;
          if (!livingEntity.isDead())
          {
            double hp = livingEntity.getHealth();
            livingEntity.damage(damage);
            if (hp != livingEntity.getHealth())
            {
              successCount++;
            }
          }
        }
      }
      if (successCount > 0)
      {
        MessageUtil.info(commandSender, "&e" + successCount + "개&r의 개체에게 &e" + Constant.Sosu15.format(damage) + "&r만큼의 피해를 주었습니다.");
      }
      else
      {
        CommandAPI.fail("조건에 맞는 개체를 찾을 수 없거나 피해를 줄 수 없는 상태입니다. (무적 시간 or 크리에이티브 모드 or PvP 불가능 구역)");
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
      double damage = (double) args[1];
      boolean hideOutput = (boolean) args[2];
      int successCount = 0;
      for (Entity entity : entities)
      {
        if (entity instanceof LivingEntity)
        {
          LivingEntity livingEntity = (LivingEntity) entity;
          if (!livingEntity.isDead())
          {
            double hp = livingEntity.getHealth();
            livingEntity.damage(damage);
            if (hp != livingEntity.getHealth())
            {
              successCount++;
            }
          }
        }
      }
      if (successCount > 0)
      {
        if (!hideOutput)
        {
          MessageUtil.info(commandSender, "&e" + successCount + "개&r의 개체에게 &e" + Constant.Sosu15.format(damage) + "&r만큼의 피해를 주었습니다.");
        }
      }
      else
      {
        CommandAPI.fail("조건에 맞는 개체를 찾을 수 없거나 피해를 줄 수 없는 상태입니다. (무적 시간 or 크리에이티브 모드 or PvP 불가능 구역)");
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
      double damage = (double) args[1];
      Entity damager = (Entity) args[2];
      String damagerDisplay = damager.getName();
      int successCount = 0;
      for (Entity entity : entities)
      {
        if (entity instanceof LivingEntity)
        {
          LivingEntity livingEntity = (LivingEntity) entity;
          if (!livingEntity.isDead())
          {
            double hp = livingEntity.getHealth();
            livingEntity.damage(damage, damager);
            if (hp != livingEntity.getHealth())
            {
              successCount++;
            }
          }
        }
      }
      if (successCount > 0)
      {
        MessageUtil.info(commandSender,
                "&e" + successCount + "개&r의 개체에게 &e" + damagerDisplay + "&r"
                        + " 가해 개체로 하는 &e" + Constant.Sosu15.format(damage) + "&r만큼의 피해를 주었습니다.");
      }
      else
      {
        CommandAPI.fail("조건에 맞는 개체를 찾을 수 없거나 피해를 줄 수 없는 상태입니다. (무적 시간 or 크리에이티브 모드 or PvP 불가능 구역)");
      }
    });
    commandAPICommand.register();

    commandAPICommand = getCommandBase(command, permission, aliases);
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
      Collection<Entity> entities = (Collection<Entity>) args[0];
      double damage = (double) args[1];
      Entity damager = (Entity) args[2];
      String damagerDisplay = damager.getName();
      boolean hideOutput = (boolean) args[3];
      int successCount = 0;
      for (Entity entity : entities)
      {
        if (entity instanceof LivingEntity)
        {
          LivingEntity livingEntity = (LivingEntity) entity;
          if (!livingEntity.isDead())
          {
            double hp = livingEntity.getHealth();
            livingEntity.damage(damage, damager);
            if (hp != livingEntity.getHealth())
            {
              successCount++;
            }
          }
        }
      }
      if (successCount > 0)
      {
        if (!hideOutput)
        {
          MessageUtil.info(commandSender,
                  "&e" + successCount + "개&r의 개체에게 &e" + damagerDisplay + "&r" + " 가해 개체로 하는 &e" + Constant.Sosu15.format(damage) + "&r만큼의 피해를 주었습니다.");
        }
      }
      else
      {
        CommandAPI.fail("조건에 맞는 개체를 찾을 수 없거나 피해를 줄 수 없는 상태입니다. (무적 시간 or 크리에이티브 모드 or PvP 불가능 구역)");
      }
    });
    commandAPICommand.register();
  }
}

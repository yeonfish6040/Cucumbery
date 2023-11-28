package com.jho5245.cucumbery.commands.brigadier.hp;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CommandHealthPoint
{
  private final List<Argument<?>> arguments1 = new ArrayList<>();

  {
    arguments1.add(new MultiLiteralArgument("args", List.of("set", "give", "take")));
    arguments1.add(new EntitySelectorArgument.ManyEntities("개체"));
    arguments1.add(new DoubleArgument("값", 0, Double.MAX_VALUE));
  }

  private final List<Argument<?>> arguments2 = new ArrayList<>();

  {
    arguments2.add(new MultiLiteralArgument("args", List.of("set", "give", "take")));
    arguments2.add(new EntitySelectorArgument.ManyEntities("개체"));
    arguments2.add(new DoubleArgument("값", 0, Double.MAX_VALUE));
    arguments2.add(new BooleanArgument("경고 무시"));
  }

  private final List<Argument<?>> arguments3 = new ArrayList<>();

  {
    arguments3.add(new MultiLiteralArgument("args", List.of("set", "give", "take")));
    arguments3.add(new EntitySelectorArgument.ManyEntities("개체"));
    arguments3.add(new DoubleArgument("값", 0, Double.MAX_VALUE));
    arguments3.add(new BooleanArgument("경고 무시"));
    arguments3.add(new BooleanArgument("명령어 출력 숨김 여부"));
  }

  @SuppressWarnings("unchecked")
  public void registerCommand(String command, String permission, String... aliases)
  {
    /*
     * /hp <set|give|take> <value>
     */
    CommandAPICommand commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(arguments1);
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
      String key = (String) args.get(0);
      Collection<Entity> entities = (Collection<Entity>) args.get(1);
      double value = (double) args.get(2);
      int successCount = 0;

      for (Entity entity : entities)
      {
        if (!(entity instanceof Damageable) && !(entity instanceof Attributable))
        {
          continue;
        }
        Damageable target = null;
        if (entity instanceof Damageable)
        {
          target = (Damageable) entity;
        }
        if (target == null)
        {
          continue;
        }
        if (target.isDead())
        {
          continue;
        }
        Attributable attributable = (Attributable) target;
        double hp = target.getHealth();
        AttributeInstance maxHealthInstance = attributable.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        double maxHealth = Objects.requireNonNull(maxHealthInstance).getValue();
        switch (key)
        {
          case "set":
            if (target.getHealth() != value)
            {
              target.setHealth(Math.min(value, maxHealth));
              successCount++;
            }
            break;
          case "give":
            if (target.getHealth() < maxHealth)
            {
              target.setHealth(Math.min(hp + value, maxHealth));
              successCount++;
            }
            break;
          case "take":
            if (target.getHealth() > 1d)
            {
              target.setHealth(Math.max(1d, hp - value));
              successCount++;
            }
            break;
        }
      }

      if (successCount > 0)
      {
        MessageUtil.info(commandSender, Constant.THE_COLOR_HEX + successCount + "개&r의 개체의 HP를 조정했습니다");
      }
      else
      {
        throw CommandAPI.failWithString("조건에 맞는 개체를 찾을 수 없거나 해당 개체의 HP에 변화가 없습니다");
      }
    });
    commandAPICommand.register();

    /*
     * /hp ~ value false
     */

    commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(arguments2);
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
      String key = (String) args.get(0);
      Collection<Entity> entities = (Collection<Entity>) args.get(1);
      double value = (double) args.get(2);
      boolean force = (boolean) args.get(3);
      int successCount = 0;

      if (!force && !MessageUtil.checkNumberSize(sender, value, 1, Double.MAX_VALUE, false))
      {
        throw CommandAPI.failWithString("숫자는 1 이상이여야 하는데, " + value + "이/가 있습니다");
      }

      for (Entity entity : entities)
      {
        if (!(entity instanceof Damageable) && !(entity instanceof Attributable))
        {
          continue;
        }
        Damageable target = null;
        if (entity instanceof Damageable)
        {
          target = (Damageable) entity;
        }
        if (target == null)
        {
          continue;
        }
        if (target.isDead())
        {
          continue;
        }
        Attributable attributable = (Attributable) target;
        double hp = target.getHealth();
        AttributeInstance maxHealthInstance = attributable.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        double maxHealth = Objects.requireNonNull(maxHealthInstance).getValue();
        double baseMaxHealth = maxHealthInstance.getBaseValue();
        switch (key)
        {
          case "set":
            if (!force && value > maxHealth)
            {
              target.setHealth(maxHealth);
              successCount++;
              continue;
            }
            boolean exceed = value > maxHealth;
            double differ = value - baseMaxHealth;
            if (exceed)
            {
              maxHealthInstance.setBaseValue(differ + value);
            }
            target.setHealth(value);
            successCount++;
            if (exceed)
            {
              maxHealthInstance.setBaseValue(baseMaxHealth);
            }
            break;
          case "give":
            if (!force && hp >= maxHealth)
            {
              continue;
            }
            if (!force && value + hp > maxHealth)
            {
              successCount++;
              target.setHealth(maxHealth);
            }
            else
            {
              exceed = hp + value > maxHealth;
              differ = hp + value - baseMaxHealth;
              if (exceed)
              {
                maxHealthInstance.setBaseValue(maxHealth + differ);
              }
              target.setHealth(hp + value);
              successCount++;
              if (exceed)
              {
                maxHealthInstance.setBaseValue(baseMaxHealth);
              }
            }
            break;
          case "take":
            if (!force && hp <= 1D)
            {
              continue;
            }
            if (!force && hp - value < 1D)
            {
              successCount++;
              target.setHealth(1D);
            }
            else
            {
              exceed = hp - value > maxHealth;
              differ = hp - value - baseMaxHealth;
              if (exceed)
              {
                maxHealthInstance.setBaseValue(maxHealth + differ);
              }
              target.setHealth(Math.max(0d, hp - value));
              successCount++;
              if (exceed)
              {
                maxHealthInstance.setBaseValue(baseMaxHealth);
              }
            }
            break;
        }
      }

      if (successCount > 0)
      {
        MessageUtil.info(commandSender, Constant.THE_COLOR_HEX + successCount + "개&r의 개체의 HP를 조정했습니다");
      }
      else
      {
        throw CommandAPI.failWithString("조건에 맞는 개체를 찾을 수 없습니다");
      }
    });
    commandAPICommand.register();

    /*
     * /hp ~ value <force> <hide>
     */

    commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(arguments3);
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
      String key = (String) args.get(0);
      Collection<Entity> entities = (Collection<Entity>) args.get(1);
      double value = (double) args.get(2);
      boolean force = (boolean) args.get(3);
      boolean hideOutput = (boolean) args.get(4);
      int successCount = 0;

      if (!force && !MessageUtil.checkNumberSize(sender, value, 1, Double.MAX_VALUE, false))
      {
        throw CommandAPI.failWithString("숫자는 1 이상이여야 하는데, " + value + "이/가 있습니다");
      }

      for (Entity entity : entities)
      {
        if (!(entity instanceof Damageable) && !(entity instanceof Attributable))
        {
          continue;
        }
        Damageable target = null;
        if (entity instanceof Damageable)
        {
          target = (Damageable) entity;
        }
        if (target == null)
        {
          continue;
        }
        if (target.isDead())
        {
          continue;
        }
        Attributable attributable = (Attributable) target;
        double hp = target.getHealth();
        AttributeInstance maxHealthInstance = attributable.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        double maxHealth = Objects.requireNonNull(maxHealthInstance).getValue();
        double baseMaxHealth = maxHealthInstance.getBaseValue();
        switch (key)
        {
          case "set":
            if (!force && value > maxHealth)
            {
              target.setHealth(maxHealth);
              successCount++;
              continue;
            }
            boolean exceed = value > maxHealth;
            double differ = value - baseMaxHealth;
            if (exceed)
            {
              maxHealthInstance.setBaseValue(differ + value);
            }
            target.setHealth(value);
            successCount++;
            if (exceed)
            {
              maxHealthInstance.setBaseValue(baseMaxHealth);
            }
            break;
          case "give":
            if (!force && hp >= maxHealth)
            {
              continue;
            }
            if (!force && value + hp > maxHealth)
            {
              successCount++;
              target.setHealth(maxHealth);
            }
            else
            {
              exceed = hp + value > maxHealth;
              differ = hp + value - baseMaxHealth;
              if (exceed)
              {
                maxHealthInstance.setBaseValue(maxHealth + differ);
              }
              target.setHealth(hp + value);
              successCount++;
              if (exceed)
              {
                maxHealthInstance.setBaseValue(baseMaxHealth);
              }
            }
            break;
          case "take":
            if (!force && hp <= 1D)
            {
              continue;
            }
            if (!force && hp - value < 1D)
            {
              successCount++;
              target.setHealth(1D);
            }
            else
            {
              exceed = hp - value > maxHealth;
              differ = hp - value - baseMaxHealth;
              if (exceed)
              {
                maxHealthInstance.setBaseValue(maxHealth + differ);
              }
              target.setHealth(Math.max(0d, hp - value));
              successCount++;
              if (exceed)
              {
                maxHealthInstance.setBaseValue(baseMaxHealth);
              }
            }
            break;
        }
      }

      if (successCount > 0)
      {
        if (!hideOutput)
        {
          MessageUtil.info(commandSender, Constant.THE_COLOR_HEX + successCount + "개&r의 개체의 HP를 조정했습니다");
        }
      }
      else
      {
        throw CommandAPI.failWithString("조건에 맞는 개체를 찾을 수 없습니다");
      }
    });
    commandAPICommand.register();
  }
}

package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector;
import dev.jorel.commandapi.arguments.LiteralArgument;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CommandRide extends CommandBase
{
  private final List<Argument> ride1 = new ArrayList<>();
  private final List<Argument> rideOff1 = new ArrayList<>();
  private final List<Argument> ride2 = new ArrayList<>();
  private final List<Argument> ride2Hide = new ArrayList<>();
  private final List<Argument> rideOff2 = new ArrayList<>();
  private final List<Argument> rideOff2Hide = new ArrayList<>();

  {
    ride1.add(new EntitySelectorArgument("탑승할 개체", EntitySelector.ONE_ENTITY));
  }

  {
    rideOff1.add(new LiteralArgument("--off"));
  }

  {
    ride2.add(new EntitySelectorArgument("탑승시킬 개체", EntitySelector.MANY_ENTITIES));
    ride2.add(new EntitySelectorArgument("탑승할 개체", EntitySelector.ONE_ENTITY));
  }

  {
    ride2Hide.add(new EntitySelectorArgument("탑승시킬 개체", EntitySelector.MANY_ENTITIES));
    ride2Hide.add(new EntitySelectorArgument("탑승할 개체", EntitySelector.ONE_ENTITY));
    ride2Hide.add(new BooleanArgument("명령어 출력 숨김 여부"));
  }

  {
    rideOff2.add(new EntitySelectorArgument("탑승시킬 개체", EntitySelector.MANY_ENTITIES));
    rideOff2.add(new LiteralArgument("--off"));
  }

  {
    rideOff2Hide.add(new EntitySelectorArgument("탑승시킬 개체", EntitySelector.MANY_ENTITIES));
    rideOff2Hide.add(new LiteralArgument("--off"));
    rideOff2Hide.add(new BooleanArgument("명령어 출력 숨김 여부"));
  }

  @SuppressWarnings("unchecked")
  public void registerCommand(String command, String permission, String... aliases)
  {
    /*
     * /ride <탑승할 개체>
     */
    CommandAPICommand commandAPICommand = getCommandBase("ride", permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(ride1);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      if (!(sender.getCallee() instanceof Entity entity))
      {
        MessageUtil.sendError(sender, Prefix.ONLY_PLAYER);
        return;
      }
      Entity vehicle = (Entity) args[0];
      if (entity == vehicle)
      {
        Method.playErrorSound(entity);
        MessageUtil.sendError(entity, "자기 자신은 탑승할 수 없습니다");
        return;
      }

      if (entity.getVehicle() == vehicle)
      {
        MessageUtil.sendError(entity, ComponentUtil.translate("변동 사항이 없습니다. 이미 %s을(를) 탑승하고 있는 상태입니다", vehicle));
        return;
      }

      boolean success = entity.teleport(vehicle);
      success = vehicle.addPassenger(entity) && success;
      if (!success)
      {
        Method.playErrorSound(entity);
        MessageUtil.sendError(sender, ComponentUtil.translate("%s을(를) 탑승할 수 없습니다. 이미 해당 개체가 자신을 탑승하고 있는 상태이거나 너무 멀리 있습니다", vehicle));
        return;
      }
      MessageUtil.info(vehicle, ComponentUtil.translate("%s이(가) 당신을 탑승합니다", entity));
      MessageUtil.info(sender, ComponentUtil.translate("%s을(를) 탑승합니다", vehicle));
      MessageUtil.sendAdminMessage(sender, Collections.singletonList(vehicle), "%s을(를) 탑승합니다", vehicle);
    });
    commandAPICommand.register();


    /*
     * /ride --off
     */
    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(rideOff1);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      if (!(sender.getCallee() instanceof Entity))
      {
        CommandAPI.fail("개체만 사용할 수 있습니다");
      }
      @SuppressWarnings("all")
      Entity entity = (Entity) sender.getCallee();
      Entity vehicle = entity.getVehicle();
      if (vehicle == null)
      {
        if (entity instanceof Player)
        {
          MessageUtil.sendError(entity, "개체를 탑승하고 있지 않습니다");
        }
        return;
      }
      entity.teleport(entity);
      MessageUtil.info(vehicle, ComponentUtil.translate("%s이(가) 당신의 탑승을 중지하였습니다", entity));
      MessageUtil.info(entity, ComponentUtil.translate("더 이상 %s을(를) 탑승하지 않습니다", vehicle));
      MessageUtil.sendAdminMessage(sender, Collections.singletonList(vehicle), "더 이상 %s을(를) 탑승하지 않습니다", vehicle);
    });
    commandAPICommand.register();

    /*
     * /ride <탑승시킬 개체> <탑승할 개체>
     */

    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(ride2);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Collection<Entity> entities = (Collection<Entity>) args[0];
      List<Entity> successEntities = new ArrayList<>();
      Entity vehicle = (Entity) args[1];
      for (Entity entity : entities)
      {
        // 개체와 탑승자가 동일하지 않고 탑승 중인 상태가 아닐 때
        if (vehicle != entity && (entity.getVehicle() == null || entity.getVehicle() != vehicle) && !vehicle.getPassengers().contains(entity))
        {
          boolean ride = entity.teleport(vehicle);
          ride = vehicle.addPassenger(entity) && ride;
          if (!ride || !vehicle.getPassengers().contains(entity))
          {
            continue;
          }
          successEntities.add(entity);
          MessageUtil.info(entity, ComponentUtil.translate("%s에 의해 %s을(를) 탑승합니다", sender, vehicle));
        }
      }
      List<Entity> failureEntities = new ArrayList<>(entities);
      failureEntities.removeAll(successEntities);
      if (!failureEntities.isEmpty())
      {
        MessageUtil.sendWarnOrError(successEntities.isEmpty(),
                sender, ComponentUtil.translate("%s은(는) 해당 개체 위에 탑승 중인 개체이거나 이미 해당 개체가 %s을(를) 탑승하고 있는 상태입니다", failureEntities, vehicle));
      }
      if (!successEntities.isEmpty())
      {
        List<Permissible> permissibles = new ArrayList<>(successEntities);
        permissibles.add(vehicle);
        MessageUtil.info(vehicle, ComponentUtil.translate("%s에 의해 %s이(가) 당신을 탑승합니다", sender, successEntities));
        MessageUtil.info(sender, ComponentUtil.translate("%s이(가) %s을(를) 탑승합니다", successEntities, vehicle));
        MessageUtil.sendAdminMessage(sender, permissibles, "%s을(를) %s에게 탑승시켰습니다", successEntities, vehicle);
      }
      else if (!(sender.getCallee() instanceof Player))
      {
        CommandAPI.fail("조건에 맞는 개체를 찾을 수 없거나 해당 개체 위에 탑승 중인 개체이거나 이미 해당 개체가 탑승하고 있는 상태입니다");
      }
    });
    commandAPICommand.register();

    /*
     * /ride <탑승시킬 개체> <탑승할 개체> [명령어 출력 숨김 여부]
     */

    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(ride2Hide);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Collection<Entity> entities = (Collection<Entity>) args[0];
      List<Entity> successEntities = new ArrayList<>();
      Entity vehicle = (Entity) args[1];
      boolean hideOutput = (boolean) args[2];
      for (Entity entity : entities)
      {
        // 개체와 탑승자가 동일하지 않고 탑승 중인 상태가 아닐 때
        if (vehicle != entity && (entity.getVehicle() == null || entity.getVehicle() != vehicle) && !vehicle.getPassengers().contains(entity))
        {
          boolean ride = entity.teleport(vehicle);
          ride = vehicle.addPassenger(entity) && ride;
          if (!ride || !vehicle.getPassengers().contains(entity))
          {
            continue;
          }
          successEntities.add(entity);
          if (!hideOutput)
          {
            MessageUtil.info(entity, ComponentUtil.translate("%s에 의해 %s을(를) 탑승합니다", sender, vehicle));
          }
        }
      }
      if (!hideOutput)
      {
        List<Entity> failureEntities = new ArrayList<>(entities);
        failureEntities.removeAll(successEntities);
        if (!failureEntities.isEmpty())
        {
          MessageUtil.sendWarnOrError(successEntities.isEmpty(),
                  sender, ComponentUtil.translate("%s은(는) 해당 개체 위에 탑승 중인 개체이거나 이미 해당 개체가 %s을(를) 탑승하고 있는 상태입니다", failureEntities, vehicle));
        }

      }
      if (!hideOutput && !successEntities.isEmpty())
      {
        List<Permissible> permissibles = new ArrayList<>(successEntities);
        permissibles.add(vehicle);
        MessageUtil.info(vehicle, ComponentUtil.translate("%s에 의해 %s이(가) 당신을 탑승합니다", sender, successEntities));
        MessageUtil.info(sender, ComponentUtil.translate("%s이(가) %s을(를) 탑승합니다", successEntities, vehicle));
        MessageUtil.sendAdminMessage(sender, permissibles, "%s을(를) %s에게 탑승시켰습니다", successEntities, vehicle);
      }
      else if (!(sender.getCallee() instanceof Player))
      {
        CommandAPI.fail("조건에 맞는 개체를 찾을 수 없거나 해당 개체 위에 탑승 중인 개체이거나 이미 해당 개체가 탑승하고 있는 상태입니다");
      }
    });
    commandAPICommand.register();

    /*
     * /ride <탑승시킬 개체> --off
     */

    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(rideOff2);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Collection<Entity> entities = (Collection<Entity>) args[0];
      List<Entity> successEntities = new ArrayList<>();
      List<Entity> vehicles = new ArrayList<>();
      for (Entity entity : entities)
      {
        Entity vehicle = entity.getVehicle();
        if (vehicle != null)
        {
          if (!vehicles.contains(vehicle))
          {
            vehicles.add(vehicle);
          }
          if (entity.teleport(entity) || entity.getVehicle() == null)
          {
            successEntities.add(entity);
            MessageUtil.info(entity, ComponentUtil.translate("%s에 의해 %s을(를) 탑승하지 않습니다", sender, vehicle));
          }
        }
      }
      List<Entity> failureEntities = new ArrayList<>(entities);
      failureEntities.removeAll(successEntities);
      if (!failureEntities.isEmpty())
      {
        MessageUtil.sendWarnOrError(successEntities.isEmpty(),
                sender, ComponentUtil.translate("%s은(는) 다른 개체를 탑승하고 있는 상태가 아닙니다", failureEntities));
      }
      if (!successEntities.isEmpty())
      {
        List<Permissible> permissibles = new ArrayList<>(successEntities);
        permissibles.addAll(vehicles);
        MessageUtil.info(vehicles, ComponentUtil.translate("%s에 의해 %s이(가) 당신을 탑승하지 않습니다", sender, successEntities));
        MessageUtil.info(sender, ComponentUtil.translate("%s을(를) %s에게서 탑승을 중지시켰습니다", successEntities, vehicles));
        MessageUtil.sendAdminMessage(sender, permissibles, "%s을(를) %s에게서 탑승을 중지시켰습니다", successEntities, vehicles);
      }
      else if (!(sender.getCallee() instanceof Player))
      {
        CommandAPI.fail("조건에 맞는 개체를 찾을 수 없거나 이미 해당 개체가 다른 개체를 탑승하고 있지 않습니다");
      }
    });
    commandAPICommand.register();

    /*
     * /ride <탑승시킬 개체> --off [명령어 출력 숨김 여부]
     */

    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(rideOff2Hide);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Collection<Entity> entities = (Collection<Entity>) args[0];
      boolean hideOutput = (boolean) args[1];
      List<Entity> successEntities = new ArrayList<>();
      List<Entity> vehicles = new ArrayList<>();
      for (Entity entity : entities)
      {
        Entity vehicle = entity.getVehicle();
        if (vehicle != null)
        {
          if (!vehicles.contains(vehicle))
          {
            vehicles.add(vehicle);
          }
          if (entity.teleport(entity) || entity.getVehicle() == null)
          {
            successEntities.add(entity);
            if (!hideOutput)
            {
              MessageUtil.info(entity, ComponentUtil.translate("%s에 의해 %s을(를) 탑승하지 않습니다", sender, vehicle));
            }
          }
        }
      }
      if (!hideOutput)
      {
        List<Entity> failureEntities = new ArrayList<>(entities);
        failureEntities.removeAll(successEntities);
        if (!failureEntities.isEmpty())
        {
          MessageUtil.sendWarnOrError(successEntities.isEmpty(),
                  sender, ComponentUtil.translate("%s은(는) 다른 개체를 탑승하고 있는 상태가 아닙니다", failureEntities));
        }
      }
      if (!hideOutput && !successEntities.isEmpty())
      {
        List<Permissible> permissibles = new ArrayList<>(successEntities);
        permissibles.addAll(vehicles);
        MessageUtil.info(vehicles, ComponentUtil.translate("%s에 의해 %s이(가) 당신을 탑승하지 않습니다", sender, successEntities));
        MessageUtil.info(sender, ComponentUtil.translate("%s을(를) %s에게서 탑승을 중지시켰습니다", successEntities, vehicles));
        MessageUtil.sendAdminMessage(sender, permissibles, "%s을(를) %s에게서 탑승을 중지시켰습니다", successEntities, vehicles);
      }
      else if (!(sender.getCallee() instanceof Player))
      {
        CommandAPI.fail("조건에 맞는 개체를 찾을 수 없거나 이미 해당 개체가 다른 개체를 탑승하고 있지 않습니다");
      }
    });
    commandAPICommand.register();
  }
}

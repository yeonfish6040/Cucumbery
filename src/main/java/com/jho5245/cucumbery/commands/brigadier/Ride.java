package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector;
import dev.jorel.commandapi.arguments.LiteralArgument;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Ride extends CommandBase
{
  private final List<Argument> ride1 = new ArrayList<>();

  {
    ride1.add(new EntitySelectorArgument("탑승할 개체", EntitySelector.ONE_ENTITY));
  }

  private final List<Argument> rideOff1 = new ArrayList<>();

  {
    rideOff1.add(new LiteralArgument("--off"));
  }

  private final List<Argument> ride2 = new ArrayList<>();

  {
    ride2.add(new EntitySelectorArgument("탑승시킬 개체", EntitySelector.MANY_ENTITIES));
    ride2.add(new EntitySelectorArgument("탑승할 개체", EntitySelector.ONE_ENTITY));
  }

  private final List<Argument> ride2Hide = new ArrayList<>();

  {
    ride2Hide.add(new EntitySelectorArgument("탑승시킬 개체", EntitySelector.MANY_ENTITIES));
    ride2Hide.add(new EntitySelectorArgument("탑승할 개체", EntitySelector.ONE_ENTITY));
    ride2Hide.add(new BooleanArgument("명령어 출력 숨김 여부"));
  }

  private final List<Argument> rideOff2 = new ArrayList<>();

  {
    rideOff2.add(new EntitySelectorArgument("탑승시킬 개체", EntitySelector.MANY_ENTITIES));
    rideOff2.add(new LiteralArgument("--off"));
  }

  private final List<Argument> rideOff2Hide = new ArrayList<>();

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
      CommandSender commandSender = sender.getCallee();
      try
      {
        commandSender.getName();
      }
      catch (Exception e)
      {
        commandSender = sender.getCaller();
      }
      if (!(sender.getCallee() instanceof Entity))
      {
        MessageUtil.sendError(commandSender, Prefix.ONLY_PLAYER);
        return;
      }
      Entity entity = (Entity) commandSender;
      Entity vehicle = (Entity) args[0];
      if (entity == vehicle)
      {
        Method.playErrorSound(entity);
        CommandAPI.fail("&c[오류] §f자기 자신은 탑승할 수 없습니다.");
        return;
      }

      if (entity.getVehicle() == vehicle)
      {
        Method.playErrorSound(entity);
        MessageUtil.sendMessage(entity, Prefix.INFO_ERROR, "변동 사항이 없습니다. 이미 ", vehicle, "을(를) 탑승하고 있는 상태입니다.");
        return;
      }

      boolean success = entity.teleport(vehicle);
      success = vehicle.addPassenger(entity) && success;
      if (!success)
      {
        Method.playErrorSound(entity);
        MessageUtil.sendMessage(entity, Prefix.INFO_ERROR, vehicle, "을(를) 탑승할 수 없습니다. 이미 해당 개체가 자신을 탑승하고 있는 상태이거나 너무 멀리 있습니다.");
        return;
      }

      MessageUtil.info(vehicle, entity, "이(가)", " 당신을 탑승합니다.");
      MessageUtil.info(entity, vehicle, "을(를)", " 탑승합니다.");

      for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers())
      {
        if (onlinePlayer == commandSender || onlinePlayer == vehicle)
        {
          continue;
        }
        if (onlinePlayer.hasPermission("minecraft.admin.command_feedback"))
        {
          MessageUtil.sendMessage(onlinePlayer, "&7&o[", entity, "&7&o: ", vehicle, "을(를) 탑승합니다.]");
        }
      }
      MessageUtil.sendMessage(Bukkit.getServer().getConsoleSender(), "&7&o[", entity, "&7&o: ", vehicle, "을(를) 탑승합니다.]");
    });
    commandAPICommand.register();


    /*
     * /ride --off
     */
    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(rideOff1);
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
      Entity entity = (Entity) commandSender;
      Entity vehicle = entity.getVehicle();
      if (vehicle == null)
      {
        Method.playErrorSound(entity);
        CommandAPI.fail("&c[오류] §f개체를 탑승하고 있지 않습니다.");
        return;
      }

      for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers())
      {
        if (onlinePlayer == commandSender || onlinePlayer == vehicle)
        {
          continue;
        }
        if (onlinePlayer.hasPermission("minecraft.admin.command_feedback"))
        {
          MessageUtil.sendMessage(onlinePlayer, "&7&o[", entity, "&7&o: ", vehicle, "을(를) 탑승하지 않습니다.]");
        }
      }
      MessageUtil.sendMessage(Bukkit.getServer().getConsoleSender(), "&7&o[", entity, "&7&o: ", vehicle, "을(를) 탑승하지 않습니다.]");
    });
    commandAPICommand.register();
    /*
     * /ride <탑승시킬 개체> <탑승할 개체>
     */

    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(ride2);
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
      int successCount = 0;
      Entity entity = null;
      Entity vehicle = (Entity) args[1];

      for (Entity loop : entities)
      {
        entity = loop;
        // 개체와 탑승자가 동일하지 않고 탑승 중인 상태가 아닐 때
        if (vehicle != loop && (loop.getVehicle() == null || loop.getVehicle() != vehicle) && !vehicle.getPassengers().contains(loop))
        {
          boolean ride = loop.teleport(vehicle);
          ride = vehicle.addPassenger(loop) || ride;
          if (!ride)
          {
            continue;
          }
          successCount++;
          if (successCount != 1 && loop instanceof Player)
          {
            MessageUtil.info(entity,  sender, "에 의해 ", vehicle, "을(를) 탑승합니다.");
            MessageUtil.info(vehicle, sender, "에 의해 ", entity, "이(가) 당신을 탑승합니다.");
          }
        }
      }
      if (successCount == 1)
      {
        MessageUtil.info(entity, sender, "에 의해 ", vehicle, "을(를) 탑승합니다.");
        MessageUtil.info(vehicle, sender, "에 의해 ", entity, "이(가) 당신을 탑승합니다.");
        MessageUtil.info(commandSender, entity, "을(를) ", vehicle, "에게 탑승시켰습니다.");
        for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers())
        {
          if (onlinePlayer == commandSender || onlinePlayer == entity || onlinePlayer == vehicle)
          {
            continue;
          }
          if (onlinePlayer.hasPermission("minecraft.admin.command_feedback"))
          {
            MessageUtil.sendMessage(onlinePlayer, "&7&o[", sender, "&7&o: ", entity, "을(를) ", vehicle, "에게 탑승시켰습니다.]");
          }
        }
        MessageUtil.sendMessage(Bukkit.getServer().getConsoleSender(), "&7&o[", sender, "&7&o: ", entity, "을(를) ", vehicle, "에게 탑승시켰습니다.]");
      }
      else if (successCount > 1)
      {
        MessageUtil.sendMessage(commandSender, Prefix.INFO, "&e" + successCount + "개&r의 개체가 ", vehicle, "을(를) 탑승합니다.");
        for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers())
        {
          if (onlinePlayer == commandSender || onlinePlayer == entity || onlinePlayer == vehicle)
          {
            continue;
          }
          if (onlinePlayer.hasPermission("minecraft.admin.command_feedback"))
          {
            MessageUtil.sendMessage(onlinePlayer, "&7&o[", sender, "&7&o: ", successCount + "개의 개체를 ", vehicle, "에게 탑승시켰습니다.]");
          }
        }
        MessageUtil.sendMessage(Bukkit.getServer().getConsoleSender(), "&7&o[", sender, "&7&o: ", successCount + "개의 개체를 ", vehicle, "에게 탑승시켰습니다.]");
      }
      else
      {
        Method.playErrorSound(commandSender);
        MessageUtil.sendMessage(commandSender, Prefix.INFO_ERROR, "조건에 맞는 개체가 존재하지 않거나 해당 개체 위에 탑승 중인 개체이거나 이미 해당 개체가 ", vehicle, "을(를) 탑승하고 있습니다.");
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
      int successCount = 0;
      Entity entity = null;
      Entity vehicle = (Entity) args[1];
      boolean hideOutput = (boolean) args[2];
      boolean vehicleIsPlayer = vehicle instanceof Player && Method.getPlayer(commandSender, vehicle.getUniqueId().toString(), false) != null;

      for (Entity loop : entities)
      {
        entity = loop;
        // 개체와 탑승자가 동일하지 않고 탑승 중인 상태가 아닐 때
        if (vehicle != loop && (loop.getVehicle() == null || loop.getVehicle() != vehicle) && !vehicle.getPassengers().contains(loop))
        {
          boolean ride = loop.teleport(vehicle);
          ride = vehicle.addPassenger(loop) || ride;
          if (!ride)
          {
            continue;
          }
          successCount++;
          if (!hideOutput && successCount != 1 && loop instanceof Player)
          {
            MessageUtil.sendMessage(entity, Prefix.INFO, sender, "에 의해 ", vehicle, "을(를) 탑승합니다.");
            MessageUtil.sendMessage(vehicle, Prefix.INFO, sender, "에 의해 ", entity, "이(가) 당신을 탑승합니다.");
          }
        }
      }
      if (!hideOutput && successCount == 1)
      {
        MessageUtil.sendMessage(entity, Prefix.INFO, sender, "에 의해 ", vehicle, "을(를) 탑승합니다.");
        MessageUtil.sendMessage(vehicle, Prefix.INFO, sender, "에 의해 ", entity, "이(가) 당신을 탑승합니다.");
        MessageUtil.sendMessage(commandSender, Prefix.INFO, entity, "을(를) ", vehicle, "에게 탑승시켰습니다.");
        for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers())
        {
          if (onlinePlayer == commandSender || onlinePlayer == entity || onlinePlayer == vehicle)
          {
            continue;
          }
          if (onlinePlayer.hasPermission("minecraft.admin.command_feedback"))
          {
            MessageUtil.sendMessage(onlinePlayer, "&7&o[", sender,"&7&o: ", entity, "을(를) ", vehicle, "&7&o 에게 탑승시켰습니다.]");
          }
        }
        MessageUtil.sendMessage(Bukkit.getServer().getConsoleSender(), "&7&o[", sender,"&7&o: ", entity, "을(를) ", vehicle, "&7&o 에게 탑승시켰습니다.]");
      }
      else if (!hideOutput && successCount > 1)
      {
        MessageUtil.sendMessage(commandSender, Prefix.INFO, "&e" + successCount + "개&r의 개체가 ", vehicle, "을(를) 탑승합니다.");
        for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers())
        {
          if (onlinePlayer == commandSender || onlinePlayer == entity || onlinePlayer == vehicle)
          {
            continue;
          }
          if (onlinePlayer.hasPermission("minecraft.admin.command_feedback"))
          {
            MessageUtil.sendMessage(onlinePlayer, "&7&o[", sender, "&7&o: " + successCount + "개의 개체를 ", vehicle,"&7&o에게 탑승시켰습니다.]");
          }
        }
        MessageUtil.sendMessage(Bukkit.getServer().getConsoleSender(), "&7&o[", sender, "&7&o: " + successCount + "개의 개체를 ", vehicle,"&7&o에게 탑승시켰습니다.]");
      }
      else if (!hideOutput)
      {
        Method.playErrorSound(commandSender);
        MessageUtil.sendMessage(commandSender, Prefix.INFO_ERROR, "조건에 맞는 개체가 존재하지 않거나 해당 개체 위에 탑승 중인 개체이거나 이미 해당 개체가 ", vehicle, "을(를) 탑승하고 있습니다.");
      }
    });
    commandAPICommand.register();

    /*
     * /rid <탑승시킬 개체> --off
     */

    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(rideOff2);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      CommandSender commandSender = sender.getCallee();
      String senderName = "&e" + ComponentUtil.senderComponent(commandSender) + "&r";

      Collection<Entity> entities = (Collection<Entity>) args[0];
      int successCount = 0;
      Entity entity = null;
      Entity vehicle = null;
      for (Entity loop : entities)
      {
        entity = loop;
        vehicle = entity.getVehicle();
        if (vehicle != null)
        {
          loop.teleport(loop);
          successCount++;
          if (successCount != 1 && loop instanceof Player)
          {
            MessageUtil.sendMessage(entity, Prefix.INFO, sender, "에 의해 ", vehicle, "을(를) 탑승하지 않습니다.");
            MessageUtil.sendMessage(vehicle, Prefix.INFO, sender, "에 의해 ", entity, "이(가) 당신을 탑승하지 않습니다.");
          }
        }
      }
      if (successCount == 1)
      {
        MessageUtil.sendMessage(entity, Prefix.INFO, sender, "에 의해 ", vehicle, "을(를) 탑승하지 않습니다.");
        MessageUtil.sendMessage(vehicle, Prefix.INFO, sender, "에 의해 ", entity, "이(가) 당신을 탑승하지 않습니다.");
        MessageUtil.sendMessage(commandSender, Prefix.INFO, entity, "을(를) ", vehicle, "에게서 탑승을 중지시켰습니다.");
        for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers())
        {
          if (onlinePlayer == commandSender || onlinePlayer == entity || onlinePlayer == vehicle)
          {
            continue;
          }
          if (onlinePlayer.hasPermission("minecraft.admin.command_feedback"))
          {
            MessageUtil.sendMessage(onlinePlayer, "&7&o[", sender,"&7&o: ", entity, "이(가) ", vehicle, "을(를) 탑승하지 않습니다.]");
          }
        }
        MessageUtil.sendMessage(Bukkit.getServer().getConsoleSender(), "&7&o[", sender,"&7&o: ", entity, "이(가) ", vehicle, "을(를) 탑승하지 않습니다.]");
      }
      else if (successCount > 1)
      {
        MessageUtil.info(commandSender, "&e" + successCount + "개&r의 개체가 다른 개체의 탑승을 중지하였습니다.");
        for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers())
        {
          if (onlinePlayer == commandSender || onlinePlayer == entity || onlinePlayer == vehicle)
          {
            continue;
          }
          if (onlinePlayer.hasPermission("minecraft.admin.command_feedback"))
          {
            MessageUtil.sendMessage(onlinePlayer, "&7&o[", sender,"&7&o: " + successCount + "개의 개체의 탑승을 중지시켰습니다.");
          }
        }
        MessageUtil.sendMessage(Bukkit.getServer().getConsoleSender(), "&7&o[", sender,"&7&o: " + successCount + "개의 개체의 탑승을 중지시켰습니다.");
      }
      else
      {
        CommandAPI.fail("조건에 맞는 개체가 존재하지 않거나 해당 개체가 다른 개체를 탑승하고 있지 않습니다.");
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
      CommandSender commandSender = sender.getCallee();
      try
      {
        commandSender.getName();
      }
      catch (Exception e)
      {
        commandSender = sender.getCaller();
      }
      boolean hideOutput = (boolean) args[1];

      Collection<Entity> entities = (Collection<Entity>) args[0];
      int successCount = 0;
      Entity entity = null;
      Entity vehicle = null;
      for (Entity loop : entities)
      {
        entity = loop;
        vehicle = entity.getVehicle();
        if (vehicle != null)
        {
          if (loop.teleport(loop))
          {
            successCount++;
            if (!hideOutput && successCount != 1 && loop instanceof Player)
            {
              MessageUtil.sendMessage(entity, Prefix.INFO, sender, "에 의해 ", vehicle, "을(를) 더이상 탑승하지 않습니다.");
              MessageUtil.sendMessage(vehicle, Prefix.INFO, sender, "에 의해 ", entity, "이(가) 당신을 더이상 탑승하지 않습니다.");
            }
          }
        }
      }
      if (!hideOutput && successCount == 1)
      {
        MessageUtil.sendMessage(entity, Prefix.INFO, sender, "에 의해 ", vehicle, "을(를) 더이상 탑승하지 않습니다.");
        MessageUtil.sendMessage(vehicle, Prefix.INFO, sender, "에 의해 ", entity, "이(가) 당신을 더이상 탑승하지 않습니다.");
        MessageUtil.sendMessage(commandSender, Prefix.INFO, entity, "을(를) ", vehicle, "에게서 탑승을 중지시켰습니다.");
        for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers())
        {
          if (onlinePlayer == commandSender || onlinePlayer == entity || onlinePlayer == vehicle)
          {
            continue;
          }
          if (onlinePlayer.hasPermission("minecraft.admin.command_feedback"))
          {
            MessageUtil.sendMessage(onlinePlayer, "&7&o[", sender, "&7&o: ", entity, "이(가)", vehicle, "&7&o을(를) 더이상 탑승하지 않습니다.]");
          }
        }
        MessageUtil.sendMessage(Bukkit.getServer().getConsoleSender(), "&7&o[", sender, "&7&o: ", entity, "이(가)", vehicle, "&7&o을(를) 더이상 탑승하지 않습니다.]");
      }
      else if (!hideOutput && successCount > 1)
      {
        MessageUtil.info(commandSender, "&e" + successCount + "개&r의 개체가 다른 개체의 탑승을 중지하였습니다.");
        for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers())
        {
          if (onlinePlayer == commandSender || onlinePlayer == entity || onlinePlayer == vehicle)
          {
            continue;
          }
          if (onlinePlayer.hasPermission("minecraft.admin.command_feedback"))
          {
            MessageUtil.sendMessage(onlinePlayer, "&7&o[", sender, "&7&o: " + successCount + "개의 개체의 탑승을 중지시켰습니다.");
          }
        }
        MessageUtil.sendMessage(Bukkit.getServer().getConsoleSender(), "&7&o[", sender, "&7&o: " + successCount + "개의 개체의 탑승을 중지시켰습니다.");
      }
      else if (!hideOutput)
      {
        CommandAPI.fail("조건에 맞는 개체가 존재하지 않거나 해당 개체가 다른 개체를 탑승하고 있지 않습니다.");
      }
    });
    commandAPICommand.register();
  }
}

package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.storage.CustomConfig;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.StringTooltip;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Kill extends CommandBase
{
  StringTooltip[] worldNames;

  {
    List<String> worldNames = CustomConfig.getCustomConfig("data/brigadier_tab_list.yml").getConfig().getStringList("worldNames");
    this.worldNames = new StringTooltip[worldNames.size() + 2];
    for (int i = 0; i < worldNames.size(); i++)
    {
      String worldName = worldNames.get(i);
      String display = Method.getWorldDisplayName(worldName);
      this.worldNames[i] = !display.equals(worldName) ? StringTooltip.of(worldName, display) : StringTooltip.none(display);
    }
    this.worldNames[this.worldNames.length - 2] = StringTooltip.of("__all__", "모든 월드");
    this.worldNames[this.worldNames.length - 1] = StringTooltip.of("__current__", "현재 월드");
  }

  private final List<Argument> arguments = new ArrayList<>();

  {
    arguments.add(new EntitySelectorArgument("개체", EntitySelector.MANY_ENTITIES));
  }

  private final List<Argument> arguments2 = new ArrayList<>();

  {
    arguments2.add(new EntitySelectorArgument("개체", EntitySelector.MANY_ENTITIES));
    arguments2.add(new StringArgument("월드").overrideSuggestionsT(worldNames));
  }

  private final List<Argument> arguments3 = new ArrayList<>();

  {
    arguments3.add(new EntitySelectorArgument("개체", EntitySelector.MANY_ENTITIES));
    arguments3.add(new StringArgument("월드").overrideSuggestionsT(worldNames));
    arguments3.add(new BooleanArgument("명령어 출력 숨김 여부"));
  }

  @SuppressWarnings("unchecked")
  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand = getCommandBase(command, permission, aliases);
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
      if (commandSender instanceof Entity)
      {
        if (commandSender instanceof Player)
        {
          ((Player) commandSender).setHealth(0);
        }
        else
        {
          ((Entity) commandSender).remove();
        }
      }
      else
      {
        MessageUtil.sendError(commandSender, Prefix.ARGS_SHORT);
        MessageUtil.commandInfo(commandSender, command, "<개체> [월드|__all__|__current__] [명령어 출력 숨김 여부]");
      }
    });
    commandAPICommand.register();

    /*
     * /ckill2 <갸체>
     */
    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(arguments);
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
      int count = 0;
      boolean onlyOnePlayer = false;
      for (Entity entity : entities)
      {
        if (entities.size() == 1 && entity instanceof Player)
        {
          if (!entity.isDead())
          {
            ((Player) entity).setHealth(0);
            count++;
            onlyOnePlayer = true;
          }
          break;
        }
      }

      String prefix = "모든 월드에 있는 총 ";
      if (!onlyOnePlayer)
      {
        if (commandSender instanceof Entity || commandSender instanceof BlockCommandSender)
        {
          World world;
          if (commandSender instanceof Entity)
          {
            world = ((Entity) commandSender).getWorld();
          }
          else
          {
            world = ((BlockCommandSender) commandSender).getBlock().getWorld();
          }
          for (Entity entity : entities)
          {
            if (entity.isDead())
            {
              continue;
            }
            if (!entity.getLocation().getWorld().getName().equals(world.getName()))
            {
              continue;
            }
            if (entity instanceof Player)
            {
              ((Player) entity).setHealth(0);
            }
            else
            {
              entity.remove();
            }
            count++;
          }
          prefix = "현재 월드(" + world.getName() + ")에 있는 총 ";
        }
        else
        {
          for (Entity entity : entities)
          {
            if (entity.isDead())
            {
              continue;
            }
            if (entity instanceof Player)
            {
              ((Player) entity).setHealth(0);
            }
            else
            {
              entity.remove();
            }
            count++;
          }
        }
      }
      if (count > 0)
      {
        MessageUtil.info(commandSender, prefix + "&e" + count + "개&r의 조건에 맞는 개체를 죽였습니다.");
      }
      else
      {
        CommandAPI.fail("조건에 맞는 개체를 찾을 수 없습니다.");
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
      try
      {
        commandSender.getName();
      }
      catch (Exception e)
      {
        commandSender = sender.getCaller();
      }
      Collection<Entity> entities = (Collection<Entity>) args[0];
      int count = 0;
      String worldName = (String) args[1];
      String prefix = "모든 월드에 있는 총 ";
      switch (worldName)
      {
        case "__all__":
          for (Entity entity : entities)
          {
            if (entity.isDead())
            {
              continue;
            }
            if (entity instanceof Player)
            {
              ((Player) entity).setHealth(0);
            }
            else
            {
              entity.remove();
            }
            count++;
          }
          break;
        case "__current__":
          World world;
          if (commandSender instanceof Entity)
          {
            world = ((Entity) commandSender).getWorld();
          }
          else if (commandSender instanceof BlockCommandSender)
          {
            world = ((BlockCommandSender) commandSender).getBlock().getWorld();
          }
          else
          {
            world = Bukkit.getWorlds().get(0);
          }
          for (Entity entity : entities)
          {
            if (entity.isDead())
            {
              continue;
            }
            if (!entity.getLocation().getWorld().getName().equals(world.getName()))
            {
              continue;
            }
            if (entity instanceof Player)
            {
              ((Player) entity).setHealth(0);
            }
            else
            {
              entity.remove();
            }
            count++;
          }
          prefix = "현재 월드(&e" + world.getName() + "&r)에 있는 총 ";
          break;
        default:
          world = Bukkit.getServer().getWorld(worldName);
          if (world == null)
          {
            MessageUtil.noArg(commandSender, Prefix.NO_WORLD, worldName);
            return;
          }
          for (Entity entity : entities)
          {
            if (entity.isDead())
            {
              continue;
            }
            if (!entity.getLocation().getWorld().getName().equals(world.getName()))
            {
              continue;
            }
            if (entity instanceof Player)
            {
              ((Player) entity).setHealth(0);
            }
            else
            {
              entity.remove();
            }
            count++;
          }
          prefix = world.getName() + " 월드에 있는 총 ";
          break;
      }
      if (count > 0)
      {
        MessageUtil.info(commandSender, prefix + "&e" + count + "개&r의 조건에 맞는 개체를 죽였습니다.");
      }
      else
      {
        CommandAPI.fail("조건에 맞는 개체를 찾을 수 없습니다.");
      }
    });
    commandAPICommand.register();

    /*
     * /ckill2 <갸체> [월드] [명령어 출력 숨김 여부]
     */
    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(arguments3);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Collection<Entity> entities = (Collection<Entity>) args[0];
      CommandSender commandSender = sender.getCallee();
      try
      {
        commandSender.getName();
      }
      catch (Exception e)
      {
        commandSender = sender.getCaller();
      }
      int count = 0;
      String worldName = (String) args[1];
      String prefix = "모든 월드에 있는 총 ";
      switch (worldName)
      {
        case "__all__":
          for (Entity entity : entities)
          {
            if (entity.isDead())
            {
              continue;
            }
            if (entity instanceof Player)
            {
              ((Player) entity).setHealth(0);
            }
            else
            {
              entity.remove();
            }
            count++;
          }
          break;
        case "__current__":
          World world;
          if (commandSender instanceof Entity)
          {
            world = ((Entity) commandSender).getWorld();
          }
          else if (commandSender instanceof BlockCommandSender)
          {
            world = ((BlockCommandSender) commandSender).getBlock().getWorld();
          }
          else
          {
            world = Bukkit.getWorlds().get(0);
          }
          for (Entity entity : entities)
          {
            if (entity.isDead())
            {
              continue;
            }
            if (!entity.getLocation().getWorld().getName().equals(world.getName()))
            {
              continue;
            }
            if (entity instanceof Player)
            {
              ((Player) entity).setHealth(0);
            }
            else
            {
              entity.remove();
            }
            count++;
          }
          prefix = "현재 월드(&e" + world.getName() + "&r)에 있는 총 ";
          break;
        default:
          world = Bukkit.getServer().getWorld(worldName);
          if (world == null)
          {
            MessageUtil.noArg(commandSender, Prefix.NO_WORLD, worldName);
            return;
          }
          for (Entity entity : entities)
          {
            if (entity.isDead())
            {
              continue;
            }
            if (!entity.getLocation().getWorld().getName().equals(world.getName()))
            {
              continue;
            }
            if (entity instanceof Player)
            {
              ((Player) entity).setHealth(0);
            }
            else
            {
              entity.remove();
            }
            count++;
          }
          prefix = world.getName() + " 월드에 있는 총 ";
          break;
      }
      boolean hideOutput = (boolean) args[2];
      if (!hideOutput)
      {
        if (count > 0)
        {
          MessageUtil.info(commandSender, prefix + "&e" + count + "개&r의 조건에 맞는 개체를 죽였습니다.");
        }
      }
      if (count == 0)
      {
        CommandAPI.fail("조건에 맞는 개체를 찾을 수 없습니다.");
      }
    });
    commandAPICommand.register();
  }
}

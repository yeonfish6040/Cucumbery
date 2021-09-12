package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTEntity;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Data extends CommandBase
{
  final private List<Argument> argumentList = new ArrayList<>();

  {
    argumentList.add(new EntitySelectorArgument("개체", EntitySelectorArgument.EntitySelector.MANY_ENTITIES));
    argumentList.add(new GreedyStringArgument("nbt"));
  }

  @SuppressWarnings("unchecked")
  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(argumentList);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
            {
              Collection<Entity> entities = (Collection<Entity>) args[0];
              String nbt = (String) args[1];
              NBTContainer nbtContainer;
              try
              {
                nbtContainer = new NBTContainer(nbt);
              }
              catch (Exception e)
              {
                CommandAPI.fail("잘못된 NBT입니다: " + nbt);
                return;
              }

              for (Entity entity : entities)
              {
                NBTEntity nbtEntity = new NBTEntity(entity);
                nbtEntity.mergeCompound(nbtContainer);
              }
            }
    );
    commandAPICommand.register();
  }
}

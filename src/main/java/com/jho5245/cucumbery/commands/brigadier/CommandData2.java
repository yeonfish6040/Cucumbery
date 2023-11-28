package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTEntity;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.entity.Entity;

import java.util.Collection;

import static com.jho5245.cucumbery.commands.brigadier.base.ArgumentUtil.MANY_ENTITIES;
import static com.jho5245.cucumbery.commands.brigadier.base.ArgumentUtil.NBT_CONTAINER;

public class CommandData2 extends CommandBase
{
  @SuppressWarnings("unchecked")
  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(MANY_ENTITIES, NBT_CONTAINER);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
            {
              Collection<Entity> entities = (Collection<Entity>) args.get(0);
              NBTContainer nbtContainer = (NBTContainer) args.get(1);
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

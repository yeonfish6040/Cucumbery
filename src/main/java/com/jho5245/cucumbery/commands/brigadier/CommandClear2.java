package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import dev.jorel.commandapi.CommandAPICommand;

public class CommandClear2 extends CommandBase
{
  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {

    });
    commandAPICommand.register();
  }
}

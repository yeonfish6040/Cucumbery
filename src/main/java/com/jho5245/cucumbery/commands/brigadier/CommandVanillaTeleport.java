package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.wrappers.Rotation;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.jho5245.cucumbery.commands.brigadier.base.ArgumentUtil.*;

public class CommandVanillaTeleport extends CommandBase
{
  private final List<Argument<?>> list1 = List.of(LOCATION, ROTATION);

  private final List<Argument<?>> list2 = List.of(LOCATION, ROTATION, HIDE_OUTPUT);

  private void teleport(@NotNull Entity sender, @NotNull Location location, @NotNull Rotation rotation, boolean hideOutput)
  {
    location.setYaw(rotation.getYaw());
    location.setPitch(rotation.getPitch());
    Vector vector = sender.getVelocity();
    float fallDistance = sender.getFallDistance();
    if (sender.teleport(location))
    {
      sender.setVelocity(vector);
      sender.setFallDistance(fallDistance);
      if (!hideOutput && Boolean.TRUE.equals(sender.getWorld().getGameRuleValue(GameRule.SEND_COMMAND_FEEDBACK)))
      {
        MessageUtil.info(sender, ComponentUtil.translate("%s(으)로 순간이동했습니다", location));
        MessageUtil.sendAdminMessage(sender, "%s(으)로 순간이동했습니다", location);
      }
    }
    else
    {
      MessageUtil.sendError(sender, ComponentUtil.translate("commands.teleport.invalidPosition"));
    }
  }

  @Override
  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(list1);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Location location = (Location) args.get(0);
      Rotation rotation = (Rotation) args.get(1);
      if (sender.getCallee() instanceof Entity entity)
      {
        teleport(entity, location, rotation, false);
      }
    });
    commandAPICommand.register();

    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(list2);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Location location = (Location) args.get(0);
      Rotation rotation = (Rotation) args.get(1);
      if (sender.getCallee() instanceof Entity entity)
      {
        teleport(entity, location, rotation, (boolean) args.get(2));
      }
    });
    commandAPICommand.register();
  }
}







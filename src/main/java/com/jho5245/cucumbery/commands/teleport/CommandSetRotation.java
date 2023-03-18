package com.jho5245.cucumbery.commands.teleport;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.util.no_groups.*;

import com.jho5245.cucumbery.util.no_groups.CommandArgumentUtil.Rotation;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandSetRotation implements CucumberyCommandExecutor
{
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_SET_ROTATION, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    int length = args.length;
    if (length < 2)
    {
      MessageUtil.shortArg(sender, 2, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(command));
      return true;
    }
    else if (length <= 3)
    {
      List<Entity> entities = SelectorUtil.getEntities(sender, args[0]);
      if (entities == null)
      {
        return !(sender instanceof BlockCommandSender);
      }
      Rotation rotation = CommandArgumentUtil.rotation(sender, args[1], true);
      if (rotation == null)
      {
        return !(sender instanceof BlockCommandSender);
      }
      if (!MessageUtil.isBoolean(sender, args, 3, true))
      {
        return !(sender instanceof BlockCommandSender);
      }
      boolean hideOutput = args.length == 3 && args[2].equals("true");

      entities.forEach(entity -> entity.setRotation(rotation.yaw(), rotation.pitch()));
      if (!hideOutput)
      {
        String yaw = Constant.THE_COLOR_HEX + Constant.Sosu15.format(rotation.yaw()), pitch = Constant.THE_COLOR_HEX + Constant.Sosu15.format(rotation.pitch());
        MessageUtil.info(sender, "%s의 바라보는 방향을 %s, %s(으)로 설정했습니다", entities, yaw, pitch);
        MessageUtil.sendAdminMessage(sender, "%s의 바라보는 방향을 %s, %s(으)로 설정했습니다", entities, yaw, pitch);
        MessageUtil.info(entities, "%s이(가) 당신의 바라보는 방향을 %s, %s(으)로 설정했습니다", sender, yaw, pitch);
      }
    }
    else
    {
      MessageUtil.longArg(sender, 3, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(command));
      return true;
    }
    return true;
  }

  @Override
  public @NotNull List<Completion> completion(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args, @NotNull Location location)
  {
    int length = args.length;
    if (length == 1)
    {
      return CommandTabUtil.tabCompleterEntity(sender, args, "<개체>");
    }
    if (length == 2)
    {
      return CommandTabUtil.rotationArgument(sender, args, "<방향>", null);
    }
    if (length == 3)
    {
      return CommandTabUtil.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
    }
    return CommandTabUtil.ARGS_LONG;
  }
}

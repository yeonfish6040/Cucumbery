package com.jho5245.cucumbery.commands.teleport;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.util.no_groups.*;

import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import io.papermc.paper.entity.LookAnchor;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandLookAt implements CucumberyCommandExecutor
{
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_LOOK_AT, true))
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
      MessageUtil.commandInfo(sender, label, "<개체> <대상 위치> [명령어 출력 숨김 여부]");
      MessageUtil.commandInfo(sender, label, "<개체> <대상 개체> [명령어 출력 숨김 여부]");
      MessageUtil.commandInfo(sender, label, "<개체> <대상 개체> <기준> [명령어 출력 숨김 여부]");
      return true;
    }
    else if (length <= 4)
    {
      List<Entity> entities = SelectorUtil.getEntities(sender, args[0]);
      if (entities == null)
      {
        return !(sender instanceof BlockCommandSender);
      }
      Entity entity = SelectorUtil.getEntity(sender, args[1], false);
      Location location = CommandArgumentUtil.location(sender, args[1], false, false);
      if (entity == null && location == null)
      {
        MessageUtil.wrongArg(sender, 2, args);
        return !(sender instanceof BlockCommandSender);
      }
      if (location != null)
      {
        if (length == 4)
        {
          MessageUtil.longArg(sender, 3, args);
          MessageUtil.commandInfo(sender, label, "<개체> <대상 위치> [명령어 출력 숨김 여부]");
          return !(sender instanceof BlockCommandSender);
        }
        if (!MessageUtil.isBoolean(sender, args, 3, true))
        {
          return !(sender instanceof BlockCommandSender);
        }
        boolean hideOutput = args.length == 3 && args[2].equals("true");
        return lookAt(sender, entities, location, LookAnchor.FEET, hideOutput) || !(sender instanceof BlockCommandSender);
      }
      else
      {
        boolean hideOutput = false;
        LookAnchor lookAnchor = null;
        if (length >= 3)
        {
          lookAnchor = Method2.valueOf(args[2], LookAnchor.class);
          if (lookAnchor == null && !MessageUtil.isBoolean(sender, args, 3, false))
          {
            MessageUtil.wrongArg(sender, 3, args);
            return !(sender instanceof BlockCommandSender);
          }
          if (MessageUtil.isBoolean(sender, args, 3, false))
          {
            hideOutput = args.length == 3 && args[2].equals("true");
            if (length > 3)
            {
              MessageUtil.longArg(sender, 3, args);
              MessageUtil.commandInfo(sender, label, "<개체> <대상 개체> [명령어 출력 숨김 여부]");
              return !(sender instanceof BlockCommandSender);
            }
          }
          else
          {
            lookAnchor = Method2.valueOf(args[2], LookAnchor.class);
          }
          if (length == 4 && lookAnchor != null)
          {
            if (!MessageUtil.isBoolean(sender, args, 4, true))
            {
              return true;
            }
            hideOutput = args[3].equals("true");
          }
        }
        if (lookAnchor == null)
        {
          lookAnchor = LookAnchor.FEET;
        }
        return lookAt(sender, entities, entity, lookAnchor, hideOutput) || !(sender instanceof BlockCommandSender);
      }
    }
    else
    {
      MessageUtil.longArg(sender, 4, args);
      MessageUtil.commandInfo(sender, label, "<개체> <대상 위치> [명령어 출력 숨김 여부]");
      MessageUtil.commandInfo(sender, label, "<개체> <대상 개체> [명령어 출력 숨김 여부]");
      MessageUtil.commandInfo(sender, label, "<개체> <대상 개체> <기준> [명령어 출력 숨김 여부]");
      return true;
    }
  }

  private boolean lookAt(@NotNull CommandSender sender, @NotNull List<Entity> entities, @NotNull Object o, LookAnchor lookAnchor, boolean hideOutput)
  {
    List<Entity> successEntities = new ArrayList<>();
    for (Entity entity : entities)
    {
      if (entity instanceof Mob mob)
      {
        if (o instanceof Location location)
        {
          if (!location.getWorld().getName().equals(entity.getWorld().getName()))
          {
            continue;
          }
          mob.lookAt(location);
        }
        if (o instanceof Entity e)
        {
          if (!e.getWorld().getName().equals(entity.getWorld().getName()))
          {
            continue;
          }
          mob.lookAt(e);
        }
        successEntities.add(mob);
      }
      if (entity instanceof Player player)
      {
        if (o instanceof Location location)
        {
          if (!location.getWorld().getName().equals(entity.getWorld().getName()))
          {
            continue;
          }
          player.lookAt(location, lookAnchor);
        }
        if (o instanceof Entity e)
        {
          if (!e.getWorld().getName().equals(entity.getWorld().getName()))
          {
            continue;
          }
          player.lookAt(e, LookAnchor.EYES, lookAnchor);
        }
        successEntities.add(player);
      }
    }
    if (!hideOutput)
    {
      List<Entity> failureEntities = new ArrayList<>(entities);
      failureEntities.removeAll(successEntities);
      if (!failureEntities.isEmpty())
      {
        MessageUtil.sendWarnOrError(successEntities.isEmpty(), sender, ComponentUtil.translate("%s은(는) 바라보는 대상을 설정할 수 있는 개체가 아니거나 다른 월드에 있습니다", failureEntities));
      }
      if (!successEntities.isEmpty())
      {
        String s = o instanceof Location ? "위치" : "개체";
        MessageUtil.info(sender, "%s의 바라보는 " + s + "를 %s(으)로 설정했습니다", successEntities, o);
        MessageUtil.sendAdminMessage(sender, "%s의 바라보는 " + s + "를 %s(으)로 설정했습니다", successEntities, o);
        MessageUtil.info(successEntities, "%s이(가) 당신의 바라보는 " + s + "를 %s(으)로 설정했습니다", sender, o);
      }
    }
    return !successEntities.isEmpty();
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
      List<Completion> list = CommandTabUtil.tabCompleterEntity(sender, args, "<대상 개체>"), list2 = CommandTabUtil.locationArgument(sender, args, "<대상 위치>", null, false, null);
      return CommandTabUtil.sortError(list, list2);
    }
    if (length == 3)
    {
      String[] args2 = new String[length - 1];
      System.arraycopy(args, 0, args2, 0, args2.length);
      List<Completion> beforeList = CommandTabUtil.locationArgument(sender, args2, "<대상 위치>", null, false, null);
      List<String> beforeStringList = CucumberyCommandExecutor.legacy(beforeList);
      if (beforeStringList.size() == 1 && beforeStringList.get(0).equals("<대상 위치>"))
      {
        return CommandTabUtil.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
      List<Completion> list = CommandTabUtil.tabCompleterList(args, LookAnchor.values(), "<기준>"), list2 = CommandTabUtil.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      return CommandTabUtil.sortError(list, list2);
    }
    if (length == 4)
    {
      switch (args[2])
      {
        case "eyes", "feet" ->
        {
          String[] args2 = new String[length - 2];
          System.arraycopy(args, 0, args2, 0, args2.length);
          List<Completion> beforeList = CommandTabUtil.locationArgument(sender, args2, "<대상 위치>", null, false, null);
          List<String> beforeStringList = CucumberyCommandExecutor.legacy(beforeList);
          if (!(beforeStringList.size() == 1 && beforeStringList.get(0).equals("<대상 위치>")))
          {
            return CommandTabUtil.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
          }
        }
      }
    }
    return CommandTabUtil.ARGS_LONG;
  }
}

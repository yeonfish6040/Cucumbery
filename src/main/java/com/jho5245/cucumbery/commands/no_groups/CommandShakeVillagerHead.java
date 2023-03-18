package com.jho5245.cucumbery.commands.no_groups;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.util.no_groups.*;

import com.jho5245.cucumbery.util.storage.data.Permission;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandShakeVillagerHead implements CucumberyCommandExecutor
{
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
  {
    boolean failure = !(sender instanceof BlockCommandSender);
    if (!Method.hasPermission(sender, Permission.CMD_SWING_ARM, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return failure;
    }
    int length = args.length;
    if (length == 0)
    {
      MessageUtil.shortArg(sender, 1, args);
      MessageUtil.commandInfo(sender, label, "<개체> [명령어 출력 숨김 여부]");
      return failure;
    }
    else if (length <= 2)
    {
      List<Entity> entities = SelectorUtil.getEntities(sender, args[0]);
      if (entities == null)
      {
        return failure;
      }
      if (!MessageUtil.isBoolean(sender, args, 2, true))
      {
        return failure;
      }
      boolean hideOutput = args.length == 2 && args[1].equals("true");
      return shakeHead(sender, entities, hideOutput) || failure;
    }
    else
    {
      MessageUtil.longArg(sender, 2, args);
      MessageUtil.commandInfo(sender, label, "<개체> [명령어 출력 숨김 여부]");
      return failure;
    }
  }

  @Override
  public @NotNull List<Completion> completion(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args, @NotNull Location location)
  {
    int length = args.length;
    if (length == 1)
    {
      return CommandTabUtil.tabCompleterEntity(sender, args, "<개체>");
    }
    else if (length == 2)
    {
      return CommandTabUtil.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
    }
    return CommandTabUtil.ARGS_LONG;
  }

  private boolean shakeHead(@NotNull CommandSender sender, @NotNull List<Entity> entities, boolean hideOutput)
  {
    List<Entity> successEntities = new ArrayList<>();
    for (Entity entity : entities)
    {
      if (entity instanceof Villager villager)
      {
        villager.shakeHead();
        successEntities.add(villager);
      }
    }
    boolean successEntitiesIsEmpty = successEntities.isEmpty();
    if (!hideOutput)
    {
      List<Entity> failureEntities = new ArrayList<>(entities);
      failureEntities.removeAll(successEntities);
      if (!failureEntities.isEmpty())
      {
        MessageUtil.sendWarnOrError(successEntitiesIsEmpty, sender, "%s은(는) 주민이 아닙니다", failureEntities);
      }
      if (!successEntitiesIsEmpty)
      {
        MessageUtil.info(sender, "%s의 머리를 흔들게 했습니다", successEntities);
        MessageUtil.sendAdminMessage(sender, "%s의 머리를 흔들게 했습니다", successEntities);
      }
    }
    return !successEntitiesIsEmpty;
  }
}

package com.jho5245.cucumbery.commands.no_groups;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.util.no_groups.*;

import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandSetAggro implements CommandExecutor, TabCompleter, AsyncTabCompleter
{
  /**
   * Executes the given command, returning its success.
   * <br>
   * If false is returned, then the "usage" plugin.yml entry for this command
   * (if defined) will be sent to the player.
   *
   * @param sender  Source of the command
   * @param command Command which was executed
   * @param label   Alias of the command which was used
   * @param args    Passed command arguments
   * @return true if a valid command, otherwise false
   */
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
  {
    boolean failure = !(sender instanceof BlockCommandSender);
    if (!Method.hasPermission(sender, Permission.CMD_SET_AGGRO, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return failure;
    }
    int length = args.length;
    if (length < 2)
    {
      MessageUtil.shortArg(sender, 2, args);
      MessageUtil.commandInfo(sender, label, "<표적을 지정할 개체> <표적으로 지정할 개체> [명령어 출력 숨김 여부]");
      return failure;
    }
    else if (length <= 3)
    {
      if (length == 3)
      {
        if (!MessageUtil.isBoolean(sender,  args, 3, true))
        {
          return failure;
        }
      }
      List<Entity> entities = SelectorUtil.getEntities(sender, args[0]);
      boolean targetNull = args[1].equals("--remove");
      Entity target = targetNull ? null : SelectorUtil.getEntity(sender, args[1]);
      if (entities == null || (!targetNull && target == null))
      {
        return failure;
      }
      boolean hideOutput = length == 3 && args[2].equals("true");
      return setAggro(sender, entities, target, hideOutput) || failure;
    }
    else
    {
      MessageUtil.longArg(sender, 3, args);
      MessageUtil.commandInfo(sender, label, "<표적을 지정할 개체> <표적으로 지정할 개체> [명령어 출력 숨김 여부]");
      return failure;
    }
  }

  /**
   * Requests a list of possible completions for a command argument.
   *
   * @param sender  Source of the command.  For players tab-completing a
   *                command inside of a command block, this will be the player, not
   *                the command block.
   * @param command Command which was executed
   * @param label   Alias of the command which was used
   * @param args    The arguments passed to the command, including final
   *                partial argument to be completed
   * @return A List of possible completions for the final argument, or null
   * to default to the command executor
   */
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
  {
    return null;
  }
  /**
   * Requests a list of possible completions for a command argument.
   *
   * @param sender   Source of the command.  For players tab-completing a
   *                 command inside a command block, this will be the player, not
   *                 the command block.
   * @param cmd      the command to be executed.
   * @param label    Alias of the command which was used
   * @param args     The arguments passed to the command, including final
   *                 partial argument to be completed
   * @param location The location of this command was executed.
   * @return A List of possible completions for the final argument, or an empty list.
   */
  @Override
  public @NotNull List<Completion> completion(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args, @NotNull Location location)
  {
    int length = args.length;
    if (length == 1)
    {
      return CommandTabUtil.tabCompleterEntity(sender, args, "<표적을 지정할 개체>");
    }
    if (length == 2)
    {
      List<Completion> list1 = CommandTabUtil.tabCompleterEntity(sender, args, "<표적으로 지정할 개체>"),
              list2 = CommandTabUtil.tabCompleterList(args, "<인수>", false, "--remove");
      return CommandTabUtil.sortError(list1, list2);
    }
    if (length == 3)
    {
      return CommandTabUtil.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
    }
    return CommandTabUtil.ARGS_LONG;
  }

  private boolean setAggro(@NotNull CommandSender sender, @NotNull List<Entity> entities, @Nullable Entity target, boolean hideOutput)
  {
    boolean targetNull = target == null;
    if (!targetNull && !(target instanceof LivingEntity))
    {
      if (!hideOutput)
      {
        MessageUtil.sendError(sender, "%s은(는) 개체 표적으로 지정할 수 있는 개체가 아닙니다", target);
      }
      return false;
    }
    List<Entity> successEntities = new ArrayList<>();
    for (Entity entity : entities)
    {
      if (entity instanceof Mob mob)
      {
        if (mob.getTarget() == target)
        {
          continue;
        }
        if (targetNull)
        {
          mob.setTarget(null);
          successEntities.add(mob);
        }
        if (target instanceof LivingEntity livingEntity)
        {
          mob.setTarget(livingEntity);
          successEntities.add(mob);
        }
      }
    }
    boolean successEntitiesIsEmpty = successEntities.isEmpty();
    if (!hideOutput)
    {
      List<Entity> failureEntities = new ArrayList<>(entities);
      failureEntities.removeAll(successEntities);
      if (!failureEntities.isEmpty())
      {
        if (targetNull)
        {
          MessageUtil.sendWarnOrError(successEntitiesIsEmpty, sender,
                  ComponentUtil.translate("%s은(는) 개체 표적을 가질 수 없는 개체이거나 이미 개체 표적이 없습니다", failureEntities));
        }
        else
        {
          MessageUtil.sendWarnOrError(successEntitiesIsEmpty, sender,
                  ComponentUtil.translate("%s은(는) 개체 표적을 가질 수 없는 개체이거나 이미 개체 표적이 %s(으)로 지정되어 있습니다", failureEntities, target));
        }
      }
      if (!successEntitiesIsEmpty)
      {
        if (targetNull)
        {
          MessageUtil.info(sender, "%s의 개체 표적을 없앴습니다", successEntities);
          MessageUtil.sendAdminMessage(sender, "%s의 개체 표적을 없앴습니다", successEntities);
        }
        else
        {
          MessageUtil.info(sender, "%s의 개체 표적을 %s(으)로 지정했습니다", successEntities, target);
          MessageUtil.sendAdminMessage(sender, "%s의 개체 표적을 %s(으)로 지정했습니다", successEntities, target);
          MessageUtil.info(target, "%s에 의해 %s이(가) 당신을 개체 표적으로 지정했습니다", sender, successEntities);
        }
      }
    }
    return !successEntitiesIsEmpty;
  }
}

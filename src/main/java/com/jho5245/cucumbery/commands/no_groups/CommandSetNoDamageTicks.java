package com.jho5245.cucumbery.commands.no_groups;

import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.SelectorUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandSetNoDamageTicks implements CommandExecutor, TabCompleter
{
  /**
   * Executes the given command, returning its success.
   * <br>
   * If false is returned, then the "usage" plugin.yml entry for this command
   * (if defined) will be sent to the player.
   *
   * @param sender Source of the command
   * @param cmd    Command which was executed
   * @param label  Alias of the command which was used
   * @param args   Passed command arguments
   * @return true if a valid command, otherwise false
   */
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_SET_NO_DAMAGE_TICKS, true))
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
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return !(sender instanceof BlockCommandSender);
    }
    else if (length <= 3)
    {
      boolean hideOutput = args.length == 3 && args[2].equals("true");
      List<Entity> entities = SelectorUtil.getEntities(sender, args[0], !hideOutput);
      if (entities == null)
      {
        return !(sender instanceof BlockCommandSender);
      }
      if (!MessageUtil.isInteger(sender, args[1], true))
      {
        return !(sender instanceof BlockCommandSender);
      }
      int tick = Integer.parseInt(args[1]);
      if (!MessageUtil.checkNumberSize(sender, tick, 0, 2000))
      {
        return !(sender instanceof BlockCommandSender);
      }
      if (!MessageUtil.isBoolean(sender, args, 3, true))
      {
        return !(sender instanceof BlockCommandSender);
      }
      List<Entity> successEntities = new ArrayList<>();
      for (Entity entity : entities)
      {
        if (entity instanceof LivingEntity livingEntity && livingEntity.getNoDamageTicks() != tick)
        {
          livingEntity.setNoDamageTicks(tick);
          successEntities.add(livingEntity);
        }
      }
      if (!hideOutput)
      {
        List<Entity> failureEntities = new ArrayList<>(entities);
        failureEntities.removeAll(successEntities);
        boolean successEntitiesIsEmpty = successEntities.isEmpty();
        if (!failureEntities.isEmpty())
        {
          MessageUtil.sendWarnOrError(successEntitiesIsEmpty, sender,
                  ComponentUtil.translate("%s은(는) 무적 시간을 변경할 수 있는 개체가 아니거나 이미 해당 개체의 무적 시간이 입력한 시간과 같습니다", failureEntities));
        }
        if (!successEntitiesIsEmpty)
        {
          MessageUtil.info(sender, ComponentUtil.translate("%s의 무적 시간을 %s(으)로 변경하였습니다", successEntities, Constant.THE_COLOR_HEX + tick));
          MessageUtil.sendAdminMessage(sender,
                  "%s에게 %s만큼의 피해를 주었습니다", successEntities, Constant.THE_COLOR_HEX + tick);
        }
      }
      return !(sender instanceof BlockCommandSender) || !successEntities.isEmpty();
    }
    else
    {
      MessageUtil.longArg(sender, 3, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return !(sender instanceof BlockCommandSender);
    }
  }

  /**
   * Requests a list of possible completions for a command argument.
   *
   * @param sender  Source of the command.  For players tab-completing a
   *                command inside of a command block, this will be the player, not
   *                the command block.
   * @param command Command which was executed
   * @param alias   The alias used
   * @param args    The arguments passed to the command, including final
   *                partial argument to be completed and command label
   * @return A List of possible completions for the final argument, or null
   * to default to the command executor
   */
  @Override
  public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args)
  {
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(true, args), true))
    {
      return Collections.singletonList(args[0]);
    }
    int length = args.length;
    if (length == 1)
    {
      return Method.tabCompleterEntity(sender, args, "<개체>", true);
    }
    else if (length == 2)
    {
      return Method.tabCompleterIntegerRadius(args, 0, 2000, "<무적 시간(틱)>");
    }
    else if (length == 3)
    {
      return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
    }
    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}

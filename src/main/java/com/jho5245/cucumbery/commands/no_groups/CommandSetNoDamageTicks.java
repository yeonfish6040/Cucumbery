package com.jho5245.cucumbery.commands.no_groups;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.util.no_groups.*;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandSetNoDamageTicks implements CucumberyCommandExecutor
{
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
        if (entity instanceof LivingEntity livingEntity && livingEntity.getNoDamageTicks() > tick)
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
          MessageUtil.info(sender, ComponentUtil.translate("%s의 무적 시간을 %s(으)로 변경했습니다", successEntities, Constant.THE_COLOR_HEX + tick));
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
      return CommandTabUtil.tabCompleterIntegerRadius(args, 0, 2000, "<무적 시간(틱)>");
    }
    else if (length == 3)
    {
      return CommandTabUtil.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
    }
    return CommandTabUtil.ARGS_LONG;
  }
}

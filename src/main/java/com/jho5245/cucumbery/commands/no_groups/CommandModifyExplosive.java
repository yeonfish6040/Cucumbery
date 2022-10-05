package com.jho5245.cucumbery.commands.no_groups;

import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.MessageUtil.ConsonantType;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.SelectorUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Explosive;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandModifyExplosive implements CommandExecutor, TabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_MODIFY_EXPLOSIVE, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    int length = args.length;
    if (length < 3)
    {
      MessageUtil.shortArg(sender, 3, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return !(sender instanceof BlockCommandSender);
    }
    else if (length <= 4)
    {
      List<Entity> entities = SelectorUtil.getEntities(sender, args[0], true);
      if (entities == null)
      {
        return !(sender instanceof BlockCommandSender);
      }
      ModifyType modifyType;
      try
      {
        modifyType = ModifyType.valueOf(args[1].toUpperCase());
      }
      catch (Exception e)
      {
        MessageUtil.wrongArg(sender, 2, args);
        return !(sender instanceof BlockCommandSender);
      }
      if (!MessageUtil.isBoolean(sender, args, 4, true))
      {
        return !(sender instanceof BlockCommandSender);
      }
      boolean hideOutput = args.length == 4 && args[3].equals("true");
      switch (modifyType)
      {
        case FIRE -> {
          if (!MessageUtil.isBoolean(sender, args, 3, true))
          {
            return !(sender instanceof BlockCommandSender);
          }
          boolean b = Boolean.parseBoolean(args[2]);
          return modify(sender, entities, modifyType, b, hideOutput) || !(sender instanceof BlockCommandSender);
        }
        case EXPLODE_POWER -> {
          if (!MessageUtil.isDouble(sender, args[2], true))
          {
            return !(sender instanceof BlockCommandSender);
          }
          double d = Double.parseDouble(args[2]);
          if (!MessageUtil.checkNumberSize(sender, d, 0, 1000))
          {
            return !(sender instanceof BlockCommandSender);
          }
          return modify(sender, entities, modifyType, (float) d, hideOutput) || !(sender instanceof BlockCommandSender);
        }
      }
    }
    else
    {
      MessageUtil.longArg(sender, 3, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return !(sender instanceof BlockCommandSender);
    }
    return true;
  }

  private boolean modify(@NotNull CommandSender sender, @NotNull List<Entity> entities, @NotNull ModifyType type, @NotNull Object value, boolean hideOutput)
  {
    List<Entity> successEntities = new ArrayList<>();
    for (Entity entity : entities)
    {
      if (!(entity instanceof Explosive explosive))
      {
        continue;
      }
      try
      {
        switch (type)
        {
          case FIRE -> {
            explosive.setIsIncendiary((boolean) value);
            successEntities.add(explosive);
          }
          case EXPLODE_POWER -> {
            explosive.setYield((float) value);
            successEntities.add(explosive);
          }
        }
      }
      catch (Exception ignored)
      {

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
                ComponentUtil.translate("%s은(는) 폭발물이 아니여서 데이터를 수정할 수 없습니다", failureEntities));
      }
      if (!successEntitiesIsEmpty)
      {
        MessageUtil.info(sender, ComponentUtil.translate("%s의 %s을(를) %s(으)로 설정했습니다", successEntities, ComponentUtil.translate(type.key), value));
        MessageUtil.sendAdminMessage(sender, new ArrayList<>(successEntities),
                "%s의 %s을(를) %s(으)로 설정했습니다", successEntities, ComponentUtil.translate(type.key), value);
      }
    }
    return !successEntities.isEmpty();
  }

  @NotNull
  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(true, args), true))
    {
      return Collections.singletonList(args[0]);
    }
    int length = args.length;
    if (length == 1)
    {
      return Method.tabCompleterEntity(sender, args, "<폭발물>", true);
    }
    if (length == 2)
    {
      return Method.tabCompleterList(args, ModifyType.values(), "<인수>");
    }
    if (length == 3)
    {
      switch (args[1])
      {
        case "explode_power" -> {
          return Method.tabCompleterDoubleRadius(args, 0, 1000, "<폭발 강도>");
        }
        case "fire" -> {
          return Method.tabCompleterBoolean(args, "<불 번짐 여부>");
        }
        default -> {
          return Collections.singletonList(args[1] + MessageUtil.getFinalConsonant(args[1], ConsonantType.은는) + " 잘못되거나 알 수 없는 인수입니다");
        }
      }
    }
    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }

  enum ModifyType
  {
    FIRE("불 번짐 여부"),
    EXPLODE_POWER("폭발 강도");

    final String key;

    ModifyType(String key)
    {

      this.key = key;
    }
  }
}

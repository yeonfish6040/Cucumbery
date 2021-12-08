package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.customeffect.CustomEffect;
import com.jho5245.cucumbery.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.customeffect.CustomEffectType;
import com.jho5245.cucumbery.customeffect.DisplayType;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandCustomEffect implements CommandExecutor, TabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    boolean failure = !(sender instanceof BlockCommandSender);
    if (!Method.hasPermission(sender, Permission.CMD_CUSTOM_EFFECT, true))
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
      MessageUtil.shortArg(sender, 2, args);
      MessageUtil.commandInfo(sender, label, "<give|clear|modify> ...");
      return failure;
    }
    if (length == 1)
    {
      switch (args[0])
      {
        case "give", "modify" ->{
          MessageUtil.shortArg(sender, 2, args);
          MessageUtil.commandInfo(sender, label, args[0] + " <targets> <effect> ...");
          return failure;
        }
        case "clear" -> {
          if (sender instanceof Player player)
          {
            if (CustomEffectManager.hasEffects(player))
            {
              CustomEffectManager.clearEffects(player);
              MessageUtil.sendMessage(player, "모든 효과를 제거랬습니다.");
              MessageUtil.sendAdminMessage(player, null, ComponentUtil.createTranslate("[%s: 모든 효과를 제거했습니다.]", player));
            }
            else
            {
              MessageUtil.sendError(player, "가지고 있는 효과가 없습니다.");
            }
          }
          else
          {
            MessageUtil.shortArg(sender, 2, args);
            MessageUtil.commandInfo(sender, label, args[0] + "clear <targets> [effect] [hide output]");
            return failure;
          }
        }
        default -> {
          MessageUtil.wrongArg(sender, 1, args);
          MessageUtil.commandInfo(sender, label, "<give|clear|modify>");
          return failure;
        }
      }
    }
    if (length == 2)
    {
      switch (args[0])
      {
        case "give" -> {
          MessageUtil.shortArg(sender, 3, args);
          MessageUtil.commandInfo(sender, label, args[0] + " <targets> <effect> [duration] [amplifier] [display-type] [hide output]");
          return failure;
        }
        case "modify" -> {
          MessageUtil.shortArg(sender, 5, args);
          MessageUtil.commandInfo(sender, label, args[0] + " <targets> <effect> <duration|amplifier|display-type> <value> [hide output]");
          return failure;
        }
        case "clear" -> {
          List<Entity> entities = SelectorUtil.getEntities(sender, args[1]);
          if (entities == null)
          {
            return failure;
          }
          return clearEffect(sender, entities, null, false) || failure;
        }
        default -> {
          MessageUtil.wrongArg(sender, 1, args);
          MessageUtil.commandInfo(sender, label, "<give|clear|modify>");
          return failure;
        }
      }
    }
    if (length == 3)
    {
      List<Entity> entities = SelectorUtil.getEntities(sender, args[1]);
      if (entities == null)
      {
        return failure;
      }
      CustomEffectType customEffectType;
      try
      {
        customEffectType = CustomEffectType.valueOf(args[2].toUpperCase());
      }
      catch (Exception e)
      {
        MessageUtil.noArg(sender, Prefix.ARGS_WRONG, args[2]);
        return failure;
      }
      switch (args[0])
      {
        case "give" -> {
          return giveEffect(sender, entities, customEffectType, 30 * 20, 0, customEffectType.getDefaultDisplayType(), false) || failure;
        }
        case "clear" -> {
          return clearEffect(sender, entities, customEffectType, false) || failure;
        }
        case "modify" -> {
          MessageUtil.shortArg(sender, 5, args);
          MessageUtil.commandInfo(sender, label, args[0] + " <targets> <effect> <duration|amplifier|display-type> <value>");
          return failure;
        }
      }
    }
    if (length == 4)
    {
      List<Entity> entities = SelectorUtil.getEntities(sender, args[1]);
      if (entities == null)
      {
        return failure;
      }
      CustomEffectType customEffectType;
      try
      {
        customEffectType = CustomEffectType.valueOf(args[2].toUpperCase());
      }
      catch (Exception e)
      {
        MessageUtil.noArg(sender, Prefix.ARGS_WRONG, args[2]);
        return failure;
      }
      switch (args[0])
      {
        case "give" -> {
          if (!MessageUtil.isInteger(sender, args[3], true))
          {
            return failure;
          }
          int duration = Integer.parseInt(args[3]);
          if (!MessageUtil.checkNumberSize(sender, duration, 0, Integer.MAX_VALUE))
          {
            return failure;
          }
          return giveEffect(sender, entities, customEffectType, duration, 0, customEffectType.getDefaultDisplayType(), false) || failure;
        }
        case "clear" -> {
          if (!MessageUtil.isBoolean(sender, args, 4, true))
          {
            return failure;
          }
          return clearEffect(sender, entities, customEffectType, Boolean.parseBoolean(args[3])) || failure;
        }
        case "modify" -> {
          MessageUtil.shortArg(sender, 5, args);
          MessageUtil.commandInfo(sender, label, args[0] + " <targets> <effect> <duration|amplifier|display-type> <value>");
          return failure;
        }
      }
    }
    if (length == 5)
    {
      List<Entity> entities = SelectorUtil.getEntities(sender, args[1]);
      if (entities == null)
      {
        return failure;
      }
      CustomEffectType customEffectType;
      try
      {
        customEffectType = CustomEffectType.valueOf(args[2].toUpperCase());
      }
      catch (Exception e)
      {
        MessageUtil.noArg(sender, Prefix.ARGS_WRONG, args[2]);
        return failure;
      }
      switch (args[0])
      {
        case "give" -> {
          if (!MessageUtil.isInteger(sender, args[3], true))
          {
            return failure;
          }
          if (!MessageUtil.isInteger(sender, args[4], true))
          {
            return failure;
          }
          int duration = Integer.parseInt(args[3]);
          if (!MessageUtil.checkNumberSize(sender, duration, 0, Integer.MAX_VALUE))
          {
            return failure;
          }
          int amplifier = Integer.parseInt(args[4]);
          if (!MessageUtil.checkNumberSize(sender, amplifier, 0, customEffectType.getMaxAmplifier()))
          {
            return failure;
          }
          return giveEffect(sender, entities, customEffectType, duration, amplifier, customEffectType.getDefaultDisplayType(), false) || failure;
        }
        case "clear" -> {
          MessageUtil.longArg(sender, 4, args);
          MessageUtil.commandInfo(sender, label, args[0] + " <targets> [effect] [hide output]");
          return failure;
        }
        case "modify" -> {
          switch (args[3])
          {

          }
          MessageUtil.shortArg(sender, 5, args);
          MessageUtil.commandInfo(sender, label, args[0] + " <targets> <effect> <duration|amplifier|display-type> <value>");
          return failure;
        }
      }
    }
    return true;
  }

  public boolean giveEffect(@NotNull CommandSender sender, @NotNull List<Entity> entities, @NotNull CustomEffectType customEffectType, int duration, int amplifier, @NotNull DisplayType displayType, boolean hideOutput)
  {
    CustomEffect customEffect = new CustomEffect(customEffectType, duration, amplifier, displayType);
    List<Entity> successEntities = new ArrayList<>();
    for (Entity entity : entities)
    {
      if (CustomEffectManager.addEffect(entity, customEffect))
      {
        successEntities.add(entity);
      }
    }
    boolean successEntitiesIsEmpty = successEntities.isEmpty();
    if (!hideOutput)
    {
      List<Entity> failureEntities = new ArrayList<>(entities);
      failureEntities.removeAll(successEntities);
      if (!failureEntities.isEmpty())
      {
        MessageUtil.sendWarnOrError(successEntitiesIsEmpty, sender,
                ComponentUtil.createTranslate("%s에게 %s 효과를 적용할 수 없습니다. (대상이 더 강한 효과를 가지고 있습니다.)", failureEntities, customEffect));
      }
      if (!successEntitiesIsEmpty)
      {
        MessageUtil.info(sender, ComponentUtil.createTranslate("%s에게 %s 효과를 적용했습니다.", successEntities, customEffect));
        MessageUtil.info(successEntities, ComponentUtil.createTranslate("%s이(가) 당신에게 %s 효과를 적용했습니다.", sender, customEffect));
        MessageUtil.sendAdminMessage(sender, new ArrayList<>(successEntities), ComponentUtil.createTranslate("[%s: %s에게 %s 효과를 적용했습니다.]", sender, successEntities, customEffect));
      }
    }
    return !successEntitiesIsEmpty;
  }

  public boolean clearEffect(@NotNull CommandSender sender, @NotNull List<Entity> entities, @Nullable CustomEffectType customEffectType, boolean hideOutput)
  {
    boolean all = customEffectType == null;
    List<Entity> successEntities = new ArrayList<>();
    for (Entity entity : entities)
    {
      if (all)
      {
        if (CustomEffectManager.clearEffects(entity))
        {
          successEntities.add(entity);
        }
      }
      else
      {
        if (CustomEffectManager.removeEffect(entity, customEffectType))
        {
          successEntities.add(entity);
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
        if (all)
        {
          MessageUtil.sendWarnOrError(successEntitiesIsEmpty, sender,
                  ComponentUtil.createTranslate("%s은(는) 효과를 가지고 있지 않습니다.", failureEntities));
        }
        else
        {
          MessageUtil.sendWarnOrError(successEntitiesIsEmpty, sender,
                  ComponentUtil.createTranslate("%s은(는) %s 효과를 가지고 있지 않습니다.", failureEntities, customEffectType));
        }
      }
      if (!successEntitiesIsEmpty)
      {
        if (all)
        {
          MessageUtil.info(sender, ComponentUtil.createTranslate("%s의 모든 효과를 제거했습니다.", successEntities));
          MessageUtil.info(successEntities, ComponentUtil.createTranslate("%s이(가) 당신의 모든 효과를 제거했습니다.", sender));
          MessageUtil.sendAdminMessage(sender, new ArrayList<>(successEntities), ComponentUtil.createTranslate("[%s: %s의 모든 효과를 제거했습니다.]", sender, successEntities));
        }
        else
        {
          MessageUtil.info(sender, ComponentUtil.createTranslate("%s의 %s 효과를 제거했습니다.", successEntities, customEffectType));
          MessageUtil.info(successEntities, ComponentUtil.createTranslate("%s이(가) 당신의 %s 효과를 제거했습니다.", sender, customEffectType));
          MessageUtil.sendAdminMessage(sender, new ArrayList<>(successEntities), ComponentUtil.createTranslate("[%s: %s의 %s 효과를 제거했습니다.]", sender, successEntities, customEffectType));
        }
      }
    }
    return !successEntitiesIsEmpty;
  }

  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(true, args), true))
    {
      return Collections.singletonList(args[0]);
    }
    int length = args.length;

    if (length == 1)
    {
      return Method.tabCompleterList(args, "<인수>", "give", "clear", "modify");
    }
    else if (length == 2)
    {
      if (Method.equals(args[0], "give", "clear", "modify"))
      return Method.tabCompleterEntity(sender, args, "<개체>", true);
    }
    else if (length == 3)
    {
      if (args[0].equals("give"))
        return Method.tabCompleterList(args, CustomEffectType.values(), "<효과>");
      if (Method.equals(args[0], "clear", "modify"))
      {
        List<Entity> entities = SelectorUtil.getEntities(sender, args[1], false);
        if (entities == null)
        {
          return Collections.singletonList(Prefix.NO_ENTITY.toString());
        }
        List<String> list = new ArrayList<>();
        for (Entity entity : entities)
        {
          List<CustomEffect> customEffects = CustomEffectManager.getEffects(entity);
          for (CustomEffect customEffect : customEffects)
          {
            String effect = customEffect.getEffectType().toString().toLowerCase();
            list.add(effect);
          }
        }
        if (list.isEmpty())
        {
          return Collections.singletonList(MessageUtil.stripColor(ComponentUtil.serialize(ComponentUtil.createTranslate("%s에게 효과가 없습니다.", entities))));
        }
        return Method.tabCompleterList(args, list, args[0].equals("clear") ? "[효과]" : "<효과>");
      }
    }
    else if (length == 4)
    {
      String effect = args[2];
      CustomEffectType customEffectType;
      try
      {
        customEffectType = CustomEffectType.valueOf(effect.toUpperCase());
      }
      catch (Exception e)
      {
        return Collections.singletonList(effect + MessageUtil.getFinalConsonant(effect, MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 효과입니다.");
      }
      switch (args[0])
      {
        case "give" -> {
          return Method.tabCompleterIntegerRadius(args, 1, Integer.MAX_VALUE, "[지속 시간(틱)]", "max");
        }
        case "modify" -> {
          List<Entity> entities = SelectorUtil.getEntities(sender, args[1], false);
          if (entities == null)
          {
            return Collections.singletonList(Prefix.NO_ENTITY.toString());
          }
          boolean atLeastOne = false;
          for (Entity entity : entities)
          {
            if (CustomEffectManager.hasEffect(entity, customEffectType))
            {
              atLeastOne = true;
              break;
            }
          }
          if (!atLeastOne)
          {
            return Collections.singletonList(MessageUtil.stripColor(ComponentUtil.serialize(ComponentUtil.createTranslate("%s에게 %s 효과가 없습니다.", entities, customEffectType))));
          }
          return Method.tabCompleterList(args, "<인수>", "duration", "amplifier", "display-type");
        }
      }
    }
    else if (length == 5)
    {
      String effect = args[2];
      CustomEffectType customEffectType;
      try
      {
        customEffectType = CustomEffectType.valueOf(effect.toUpperCase());
      }
      catch (Exception e)
      {
        return Collections.singletonList(effect + MessageUtil.getFinalConsonant(effect, MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 효과입니다.");
      }
      switch (args[0])
      {
        case "give" -> {
          return Method.tabCompleterIntegerRadius(args, 0, customEffectType.getMaxAmplifier(), "[농도 레벨]");
        }
        case "modify" -> {
          List<Entity> entities = SelectorUtil.getEntities(sender, args[1], false);
          if (entities == null)
          {
            return Collections.singletonList(Prefix.NO_ENTITY.toString());
          }
          boolean atLeastOne = false;
          for (Entity entity : entities)
          {
            if (CustomEffectManager.hasEffect(entity, customEffectType))
            {
              atLeastOne = true;
              break;
            }
          }
          if (!atLeastOne)
          {
            return Collections.singletonList(MessageUtil.stripColor(ComponentUtil.serialize(ComponentUtil.createTranslate("%s에게 %s 효과가 없습니다.", entities, customEffectType))));
          }
          switch (args[3])
          {
            case "duration" -> {
              return Method.tabCompleterIntegerRadius(args, 1, Integer.MAX_VALUE, "<지속 시간(틱)>", "max");
            }
            case "amplifier" -> {
              return Method.tabCompleterIntegerRadius(args, 0, customEffectType.getMaxAmplifier(), "<농도 레벨>");
            }
            case "display-type" -> {
              List<String> list = new ArrayList<>(Method.enumToList(DisplayType.values()));
              list.add(customEffectType.getDefaultDisplayType().toString().toLowerCase() + "(기본값)");
              list.add("default");
              return Method.tabCompleterList(args, list, "<표시 유형>");
            }
          }
        }
      }
    }
    else if (length == 6)
    {
      String effect = args[2];
      CustomEffectType customEffectType;
      try
      {
        customEffectType = CustomEffectType.valueOf(effect.toUpperCase());
      }
      catch (Exception e)
      {
        return Collections.singletonList(effect + MessageUtil.getFinalConsonant(effect, MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 효과입니다.");
      }
      switch (args[0])
      {
        case "give" -> {
          List<String> list = new ArrayList<>(Method.enumToList(DisplayType.values()));
          list.add(customEffectType.getDefaultDisplayType().toString().toLowerCase() + "(기본값)");
          list.add("default");
          return Method.tabCompleterList(args, list, "[표시 유형]");
        }
        case "modify" -> {
          List<Entity> entities = SelectorUtil.getEntities(sender, args[1], false);
          if (entities == null)
          {
            return Collections.singletonList(Prefix.NO_ENTITY.toString());
          }
          boolean atLeastOne = false;
          for (Entity entity : entities)
          {
            if (CustomEffectManager.hasEffect(entity, customEffectType))
            {
              atLeastOne = true;
              break;
            }
          }
          if (!atLeastOne)
          {
            return Collections.singletonList(MessageUtil.stripColor(ComponentUtil.serialize(ComponentUtil.createTranslate("%s에게 %s 효과가 없습니다.", entities, customEffectType))));
          }
          if (Method.equals(args[2], "duration", "amplifier", "display-type"))
          {
            return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
          }
        }
      }
    }
    else if (length == 7)
    {
      if (args[0].equals("give"))
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }

    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}
















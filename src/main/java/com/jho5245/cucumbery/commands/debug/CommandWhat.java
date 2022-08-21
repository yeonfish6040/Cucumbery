package com.jho5245.cucumbery.commands.debug;

import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.SelectorUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @deprecated why the hell does this command exist
 */
@Deprecated
public class CommandWhat implements CommandExecutor, TabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, "asdf", true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    try
    {
      switch (args[0])
      {
        case "all effects" -> {
          List<LivingEntity> livingEntities = new ArrayList<>();
          LivingEntity livingEntity = null;
          int duration = 30 * 20;
          if (args.length == 1)
          {
            livingEntity = (LivingEntity) sender;
            livingEntities.add(livingEntity);
          }
          else if (args.length == 2)
          {
            Entity entity = SelectorUtil.getEntity(sender, args[1]);
            if (entity instanceof LivingEntity living)
            {
              livingEntity = living;
            }
            else if (entity != null)
            {
              MessageUtil.sendError(sender, ComponentUtil.translate("%s은(는) 효과를 받을 수 있는 대상이 아닙니다", entity));
              return !(sender instanceof BlockCommandSender);
            }
            else if (MessageUtil.isInteger(sender, args[1], true))
            {
              duration = Integer.parseInt(args[1]);
            }
          }
          else if (args.length == 3)
          {
            Entity entity = SelectorUtil.getEntity(sender, args[2]);
            if (entity instanceof LivingEntity living)
            {
              livingEntity = living;
            }
            else if (entity != null)
            {
              MessageUtil.sendError(sender, ComponentUtil.translate("%s은(는) 효과를 받을 수 있는 대상이 아닙니다", entity));
              return !(sender instanceof BlockCommandSender);
            }
          }
          for (PotionEffectType potionEffectType : PotionEffectType.values())
          {
            PotionEffect potionEffect = new PotionEffect(potionEffectType, duration, 0);
            livingEntity.addPotionEffect(potionEffect);
          }
        }
        case "all enchants" -> {

        }
      }
    }
    catch (Throwable e)
    {
      MessageUtil.sendMessage(sender, e.getClass().getName());
    }
    return true;
  }

  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (args.length == 1)
    {
      return Method.tabCompleterList(args, "<능력>", "all effects", "all enchants", "all items");
    }
    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}

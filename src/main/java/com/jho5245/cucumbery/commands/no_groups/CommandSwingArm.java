package com.jho5245.cucumbery.commands.no_groups;

import com.destroystokyo.paper.ClientOption;
import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.util.no_groups.*;

import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.MainHand;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandSwingArm implements CucumberyCommandExecutor
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
      MessageUtil.shortArg(sender, sender instanceof Player ? 1 : 2, args);
      MessageUtil.commandInfo(sender, label, "<main_hand|off_hand> " + (sender instanceof Player ? "[개체]" : "<개체>") + " [명령어 출력 숨김 여부]");
      return failure;
    }
    else if (length <= 3)
    {
      HandType handType = Method2.valueOf(args[0], HandType.class);
      if (handType == null)
      {
        MessageUtil.wrongArg(sender, 1, args);
        return failure;
      }
      List<Entity> entities = new ArrayList<>();
      if (length == 1)
      {
        if (sender instanceof Player player)
        {
          entities.add(player);
        }
        else
        {
          MessageUtil.shortArg(sender, 2, args);
          MessageUtil.commandInfo(sender, label, "<main_hand|off_hand> <개체> [명령어 출력 숨김 여부]");
          return failure;
        }
      }
      else
      {
        entities = SelectorUtil.getEntities(sender, args[1]);
        if (entities == null)
        {
          return failure;
        }
      }
      if (!MessageUtil.isBoolean(sender, args, 3, true))
      {
        return failure;
      }
      boolean hideOutput = args.length == 3 && args[2].equals("true");
      return swingArm(sender, entities, handType, hideOutput) || failure;
    }
    else
    {
      MessageUtil.longArg(sender, 3, args);
      MessageUtil.commandInfo(sender, label, "<main_hand|off_hand> " + (sender instanceof Player ? "[개체]" : "<개체>") + " [명령어 출력 숨김 여부]");
      return failure;
    }
  }

  @Override
  public @NotNull List<Completion> completion(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args, @NotNull Location location)
  {
    int length = args.length;
    if (length == 1)
    {
      return CommandTabUtil.tabCompleterList(args, HandType.values(), "<휘두를 손>");
    }
    else if (length == 2)
    {
      return CommandTabUtil.tabCompleterEntity(sender, args, "[개체]");
    }
    else if (length == 3)
    {
      return CommandTabUtil.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
    }
    return CommandTabUtil.ARGS_LONG;
  }

  private boolean swingArm(@NotNull CommandSender sender, @NotNull List<Entity> entities, @NotNull HandType handType, boolean hideOutput)
  {
    List<Entity> successEntities = new ArrayList<>();
    for (Entity entity : entities)
    {
      if (entity instanceof LivingEntity livingEntity)
      {
        switch (handType)
        {
          case MAIN_HAND -> livingEntity.swingMainHand();
          case OFF_HAND -> livingEntity.swingOffHand();
          case LEFT_HAND ->
          {
            if (livingEntity instanceof Mob mob)
            {
              if (mob.isLeftHanded())
              {
                mob.swingMainHand();
              }
              else
              {
                mob.swingOffHand();
              }
            }
            else if (livingEntity instanceof Player player)
            {
              MainHand mainHand = player.getClientOption(ClientOption.MAIN_HAND);
              if (mainHand == MainHand.RIGHT)
              {
                player.swingOffHand();
              }
              else
              {
                player.swingMainHand();
              }
            }
            else
            {
              livingEntity.swingOffHand();
            }
          }
          case RIGHT_HAND ->
          {
            if (livingEntity instanceof Mob mob)
            {
              if (mob.isLeftHanded())
              {
                mob.swingOffHand();
              }
              else
              {
                mob.swingMainHand();
              }
            }
            else if (livingEntity instanceof Player player)
            {
              MainHand mainHand = player.getClientOption(ClientOption.MAIN_HAND);
              if (mainHand == MainHand.RIGHT)
              {
                player.swingMainHand();
              }
              else
              {
                player.swingOffHand();
              }
            }
            else
            {
              livingEntity.swingMainHand();
            }
          }
        }
        successEntities.add(livingEntity);
      }
    }
    boolean successEntitiesIsEmpty = successEntities.isEmpty();
    if (!hideOutput)
    {
      List<Entity> failureEntities = new ArrayList<>(entities);
      failureEntities.removeAll(successEntities);
      if (!failureEntities.isEmpty())
      {
        MessageUtil.sendWarnOrError(successEntitiesIsEmpty, sender, "%s은(는) 손을 휘두를 수 없는 개체입니다", failureEntities);
      }
      if (!successEntitiesIsEmpty)
      {
        List<Audience> infoTarget = new ArrayList<>(successEntities);
        infoTarget.remove(sender);
        String hand = switch (handType)
                {
                  case MAIN_HAND -> "주로 사용하는 손";
                  case OFF_HAND -> "다른 손";
                  case LEFT_HAND -> "왼손";
                  case RIGHT_HAND -> "오른손";
                };
        Component msg = ComponentUtil.translate(hand);
        MessageUtil.info(sender, "%s의 %s을(를) 휘두르게 했습니다", successEntities, msg);
        MessageUtil.sendAdminMessage(sender, "%s의 %s을(를) 휘두르게 했습니다", successEntities, msg);
        MessageUtil.info(infoTarget, "%s이(가) 당신의 %s을(를) 휘두르게 했습니다", sender, msg);
      }
    }
    return !successEntitiesIsEmpty;
  }

  public enum HandType
  {
    MAIN_HAND,
    OFF_HAND,
    LEFT_HAND,
    RIGHT_HAND
  }
}

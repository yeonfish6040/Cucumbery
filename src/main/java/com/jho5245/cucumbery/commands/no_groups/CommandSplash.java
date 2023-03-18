package com.jho5245.cucumbery.commands.no_groups;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.util.no_groups.*;

import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ThrownPotion;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandSplash implements CucumberyCommandExecutor
{
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
  {
    boolean failure = !(sender instanceof BlockCommandSender);
    if (!Method.hasPermission(sender, Permission.CMD_SPLASH, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return failure;
    }
    int length = args.length;
    if (length < 1)
    {
      MessageUtil.shortArg(sender, 1, args);
      MessageUtil.commandInfo(sender, label, "<물약> [명령어 출력 숨김 여부]");
      return failure;
    }
    else if (length <= 2)
    {
      if (length == 2)
      {
        if (!MessageUtil.isBoolean(sender,  args, 2, true))
        {
          return failure;
        }
      }
      List<Entity> entities = SelectorUtil.getEntities(sender, args[0]);
      if (entities == null)
      {
        return failure;
      }
      List<Entity> successEntities = new ArrayList<>();
      for (Entity entity : entities)
      {
        if (entity instanceof ThrownPotion thrownPotion)
        {
          entity.teleport(entity);
          thrownPotion.splash();
          successEntities.add(thrownPotion);
        }
      }

      boolean successEntitiesIsEmpty = successEntities.isEmpty();
      boolean hideOutput = length == 2 && args[1].equals("true");
      if (!hideOutput)
      {
        List<Entity> failureEntities = new ArrayList<>(entities);
        failureEntities.removeAll(successEntities);
        if (!failureEntities.isEmpty())
        {
          MessageUtil.sendWarnOrError(successEntitiesIsEmpty, sender,
                  ComponentUtil.translate("%s은(는) 물약이 아니여서 깨트릴 수 없었습니다", failureEntities));
        }
        if (!successEntitiesIsEmpty)
        {
          MessageUtil.info(sender, "%s을(를) 즉시 깨트렸습니다", successEntities);
          MessageUtil.sendAdminMessage(sender, "%s을(를) 즉시 깨트렸습니다", successEntities);
        }
      }
      return !successEntitiesIsEmpty || failure;
    }
    else
    {
      MessageUtil.longArg(sender, 2, args);
      MessageUtil.commandInfo(sender, label, "<물약> [명령어 출력 숨김 여부]");
      return failure;
    }
  }

  @Override
  public @NotNull List<Completion> completion(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args, @NotNull Location location)
  {
    int length = args.length;
    if (length == 1)
    {
      return CommandTabUtil.tabCompleterEntity(sender, args, "<물약>");
    }
    if (length == 2)
    {
      return CommandTabUtil.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
    }
    return CommandTabUtil.ARGS_LONG;
  }
}

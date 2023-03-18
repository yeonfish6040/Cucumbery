package com.jho5245.cucumbery.commands.no_groups;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.util.no_groups.*;

import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandKill3 implements CommandExecutor, AsyncTabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_TRUEKILL, true))
    {
      return !(sender instanceof BlockCommandSender);
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    if (args.length == 0 || args.length > 2)
    {
      if (args.length == 0)
      {
        MessageUtil.shortArg(sender, 1, args);
      }
      else
      {
        MessageUtil.longArg(sender, 2, args);
      }
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return !(sender instanceof BlockCommandSender);
    }
    List<Entity> entities = SelectorUtil.getEntities(sender, args[0]);
    if (entities == null)
    {
      return !(sender instanceof BlockCommandSender);
    }
    List<Entity> successEntities = new ArrayList<>();
    for (Entity entity : entities)
    {
      if (entity instanceof Player player)
      {
        if (!player.isDead())
        {
          double health = player.getHealth();
          player.setLastDamageCause(new EntityDamageEvent(player, DamageCause.VOID, Double.MAX_VALUE));
          player.setLastDamage(Double.MAX_VALUE);
          player.setHealth(0);
          if (player.getHealth() != health)
          {
            successEntities.add(player);
          }
        }
      }
      else
      {
        successEntities.add(entity);
        entity.remove();
      }
    }
    if (!MessageUtil.isBoolean(sender, args, 2, true))
    {
      return !(sender instanceof BlockCommandSender);
    }
    boolean hideOutput = args.length == 2 && args[1].equals("true");
    if (!hideOutput)
    {
      MessageUtil.info(sender, "%s을(를) 죽였습니다", successEntities);
    }
    return true;
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
      return Method.tabCompleterEntity(sender, args, "<죽일 개체>", true);
    }
    else if (length == 2)
    {
      return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
    }

    return Collections.singletonList(Prefix.ARGS_LONG.toString());
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
      return CommandTabUtil.tabCompleterEntity(sender, args, "<죽일 개체>");
    }
    else if (length == 2)
    {
      return CommandTabUtil.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
    }

    return CommandTabUtil.ARGS_LONG;
  }
}












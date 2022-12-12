package com.jho5245.cucumbery.commands.no_groups;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.*;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandCall implements CucumberyCommandExecutor
{
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_CALL, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    String usage = cmd.getUsage().replace("/<command> ", "");
    if (!(sender instanceof Player))
    {
      MessageUtil.sendError(sender, Prefix.ONLY_PLAYER);
      return true;
    }
    if (args.length == 0)
    {
      MessageUtil.shortArg(sender, 1, args);
      MessageUtil.commandInfo(sender, label, usage);
      return true;
    }
    else if (args.length == 1)
    {
      Player player = (Player) sender;
      Player target = SelectorUtil.getPlayer(sender, args[0]);
      if (target == null)
      {
        return true;
      }
      if (target == player)
      {
        MessageUtil.sendError(sender, "자기 자신을 호출할 수 없습니다");
        return true;
      }
      FileConfiguration cfg = Cucumbery.config;
      Sound sound;
      try
      {
        sound = Sound.valueOf(cfg.getString("sound-const.call-sound.sound"));
      }
      catch (Exception e)
      {
        sound = Sound.ENTITY_CHICKEN_HURT;
      }
      SoundPlay.playSound(target, sound, (float) cfg.getDouble("sound-const.call-sound.volume"), (float) cfg.getDouble("sound-const.call-sound.pitch"));
      MessageUtil.info(player, "%s을(를) 호출합니다", target);
      MessageUtil.info(target, "%s이(가) 당신을 호출합니다", player);
    }
    else
    {
      MessageUtil.longArg(sender, 1, args);
      MessageUtil.commandInfo(sender, label, usage);
      return true;
    }
    return true;
  }

  @Override
  public @NotNull List<Completion> completion(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args, @NotNull Location location)
  {
    int length = args.length;

    if (length == 1)
    {
      return CommandTabUtil.tabCompleterPlayer(sender, args, "<플레이어>");
    }
    return CommandTabUtil.ARGS_LONG;
  }
}

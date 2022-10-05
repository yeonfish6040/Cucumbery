package com.jho5245.cucumbery.commands.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.sendercomponent.SenderComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandYunnori implements CommandExecutor, TabCompleter
{
  private boolean throwing = false; // 윷을 던지고 있는 상태인가?

  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_YUNNORI, true))
    {
      return true;
    }
    if (!(sender instanceof Player))
    {
      MessageUtil.sendError(sender, Prefix.ONLY_PLAYER);
      return true;
    }
    if (args.length == 0)
    {
      Player player = (Player) sender;
      if (player.getScoreboardTags().contains("observer"))
      {
        MessageUtil.sendError(sender, ComponentUtil.translate("옵저버는 윷을 던질 수 없습니다"));
        return true;
      }
      if (throwing)
      {
        MessageUtil.sendError(sender, ComponentUtil.translate("윷을 던지고 있을땐 잠시 기다려 주세요."));
        return true;
      }
      throwing = true;
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> throwing = false, 30L);

      int i = Method.random(1, 10000);
      for (Player online : Bukkit.getServer().getOnlinePlayers())
      {
        if (online.getScoreboardTags().contains("observer"))
        {
          continue;
        }
        MessageUtil.sendTitle(online, "&a3", "", 2, 16, 2);
        SoundPlay.playSound(online, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
        {
          MessageUtil.sendTitle(online, "&e2", "", 2, 16, 2);
          SoundPlay.playSound(online, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
        }, 10L);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
        {
          MessageUtil.sendTitle(online, "&c1", "", 2, 16, 2);
          SoundPlay.playSound(online, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
        }, 20L);
      }
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        if (i <= 300)
        {
          nac(player);
        }
        else if (i <= 384)
        {
          backDoe(player);
        }
        else if (i <= 1536)
        {
          doe(player);
        }
        else if (i <= 4992)
        {
          gae(player);
        }
        else if (i <= 8448)
        {
          geol(player);
        }
        else if (i <= 9744)
        {
          yut(player);
        }
        else
        {
          mo(player);
        }
      }, 30L);
    }
    else
    {
      String usage = cmd.getUsage().replace("/<command> ", "");
      MessageUtil.longArg(sender, 1, args);
      MessageUtil.commandInfo(sender, label, usage);
    }
    return true;
  }

  private void nac(final Player sender)
  {
    for (Player player : Bukkit.getServer().getOnlinePlayers())
    {
      if (player.getScoreboardTags().contains("observer"))
      {
        continue;
      }
      MessageUtil.sendTitle(player, "&7낙...", "&r턴을 날려부려쓰..", 2, 40, 2);
      SoundPlay.playSound(player, Sound.ENTITY_GENERIC_EXPLODE, 1F, 2F);
      MessageUtil.sendMessage(player, Prefix.INFO_YUNNORI, Constant.separatorSubString(2));
      MessageUtil.sendMessage(player, Prefix.INFO_YUNNORI,
              ComponentUtil.translate("%s님 %s %s",
                      SenderComponentUtil.senderComponent(sender), ComponentUtil.translate("&7낙..."), ComponentUtil.translate("턴을 날려부려쓰..")));
    }
  }

  private void backDoe(final Player sender)
  {
    for (Player player : Bukkit.getServer().getOnlinePlayers())
    {
      if (player.getScoreboardTags().contains("observer"))
      {
        continue;
      }
      MessageUtil.sendTitle(player, "&7빽도!", "&r뒤로 한 칸!", 2, 40, 2);
      SoundPlay.playSound(player, Sound.ENTITY_VILLAGER_NO);
      MessageUtil.sendMessage(player, Prefix.INFO_YUNNORI, Constant.separatorSubString(2));
      MessageUtil.sendMessage(player, Prefix.INFO_YUNNORI,
              ComponentUtil.translate("%s님 %s %s",
                      SenderComponentUtil.senderComponent(sender), ComponentUtil.translate("&7빽도!"), ComponentUtil.translate("뒤로 한 칸!")));
    }
  }

  private void doe(final Player sender)
  {
    for (Player player : Bukkit.getServer().getOnlinePlayers())
    {
      if (player.getScoreboardTags().contains("observer"))
      {
        continue;
      }
      MessageUtil.sendTitle(player, "&r도!", "&r앞으로 한 칸!", 2, 40, 2);
      SoundPlay.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 2F);
      MessageUtil.sendMessage(player, Prefix.INFO_YUNNORI, Constant.separatorSubString(2));
      MessageUtil.sendMessage(player, Prefix.INFO_YUNNORI,
              ComponentUtil.translate("%s님 %s %s",
                      SenderComponentUtil.senderComponent(sender), ComponentUtil.translate("도!"), ComponentUtil.translate("앞으로 한 칸!")));
    }
  }

  private void gae(final Player sender)
  {
    for (Player player : Bukkit.getServer().getOnlinePlayers())
    {
      if (player.getScoreboardTags().contains("observer"))
      {
        continue;
      }
      MessageUtil.sendTitle(player, "&r개!", "&r앞으로 두 칸!", 2, 40, 2);
      SoundPlay.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 2F);
      MessageUtil.sendMessage(player, Prefix.INFO_YUNNORI, Constant.separatorSubString(2));
      MessageUtil.sendMessage(player, Prefix.INFO_YUNNORI,
              ComponentUtil.translate("%s님 %s %s",
                      SenderComponentUtil.senderComponent(sender), ComponentUtil.translate("개!"), ComponentUtil.translate("앞으로 두 칸!")));
    }
  }

  private void geol(final Player sender)
  {
    for (Player player : Bukkit.getServer().getOnlinePlayers())
    {
      if (player.getScoreboardTags().contains("observer"))
      {
        continue;
      }
      MessageUtil.sendTitle(player, "걸이요!", "앞으로 세 칸!", 2, 40, 2);
      SoundPlay.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 2F);
      MessageUtil.sendMessage(player, Prefix.INFO_YUNNORI, Constant.separatorSubString(2));
      MessageUtil.sendMessage(player, Prefix.INFO_YUNNORI,
              ComponentUtil.translate("%s님 %s %s",
                      SenderComponentUtil.senderComponent(sender), ComponentUtil.translate("걸이요!"), ComponentUtil.translate("앞으로 세 칸!")));
    }
  }

  private void yut(final Player sender)
  {
    for (Player player : Bukkit.getServer().getOnlinePlayers())
    {
      if (player.getScoreboardTags().contains("observer"))
      {
        continue;
      }
      MessageUtil.sendTitle(player, ComponentUtil.translate("&a윷이요!"),
              ComponentUtil.translate("#52ee52;앞으로 네 칸!").append(ComponentUtil.translate(" &a&l한 번 더!")),
              2, 40, 2);
      SoundPlay.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
      MessageUtil.sendMessage(player, Prefix.INFO_YUNNORI, Constant.separatorSubString(2));
      MessageUtil.sendMessage(player, Prefix.INFO_YUNNORI,
              ComponentUtil.translate("%s님 %s %s",
                      SenderComponentUtil.senderComponent(sender), ComponentUtil.translate("&a윷이요!"), ComponentUtil.translate("#52ee52;앞으로 네 칸!")));
      MessageUtil.sendMessage(player, Prefix.INFO_YUNNORI, ComponentUtil.translate("&a&l한 번 더!"));
    }
  }

  private void mo(final Player sender)
  {
    for (Player player : Bukkit.getServer().getOnlinePlayers())
    {
      if (player.getScoreboardTags().contains("observer"))
      {
        continue;
      }
      MessageUtil.sendTitle(player, ComponentUtil.translate("&b모!!!"),
              ComponentUtil.translate("&c앞으로 다섯 칸!").append(ComponentUtil.translate(" &a&l한 번 더!")),
              2, 40, 2);
      SoundPlay.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
      MessageUtil.sendMessage(player, Prefix.INFO_YUNNORI, Constant.separatorSubString(2));
      MessageUtil.sendMessage(player, Prefix.INFO_YUNNORI,
              ComponentUtil.translate("%s님 %s %s",
                      SenderComponentUtil.senderComponent(sender), ComponentUtil.translate("&b모!!!"), ComponentUtil.translate("&c앞으로 다섯 칸!")));
      MessageUtil.sendMessage(player, Prefix.INFO_YUNNORI, ComponentUtil.translate("&a&l한 번 더!"));
    }
  }
  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}

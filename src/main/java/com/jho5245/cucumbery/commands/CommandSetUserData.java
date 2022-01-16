package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.commands.sound.CommandSong;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.storage.CustomConfig;
import com.jho5245.cucumbery.util.storage.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandSetUserData implements CommandExecutor, TabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_SETUSERDATA, true))
    {
      return true;
    }
    String usage = cmd.getUsage().replace("/<command> ", "");
    if (args.length < 3)
    {
      MessageUtil.shortArg(sender, 3, args);
      MessageUtil.commandInfo(sender, label, usage);
      return true;
    }
    else if (args.length <= 4)
    {
      OfflinePlayer player = SelectorUtil.getOfflinePlayer(sender, args[0], true);
      if (player == null)
      {
        return true;
      }
      UUID uuid = player.getUniqueId();
      boolean isOnline = player.getPlayer() != null;
      boolean hideMessage = args.length == 4 && args[3].equalsIgnoreCase("true");
      String keyString = args[1];
      UserData key;
      try
      {
        key = UserData.valueOf(keyString.toUpperCase());
      }

      catch (Exception e)
      {
        MessageUtil.noArg(sender, Prefix.NO_KEY, keyString);
        return true;
      }
      keyString += "(" + key.getKey().replace("-", " ") + ")";
      String value = args[2].toLowerCase();
      CustomConfig config = CustomConfig.getPlayerConfig(uuid);
      switch (key)
      {
        case DISPLAY_NAME, PLAYER_LIST_NAME -> {
          MessageUtil.sendError(sender, "닉네임은 닉네임 명령어(&e/nick&r, &e/nickothers&r)를 사용하여 변경해주세요.");
          return true;
        }
        case HEALTH_BAR -> {
          MessageUtil.sendError(sender, "HP바는 HP바 명령어(&e/shp&r)를 사용하여 변경해주세요.");
          return true;
        }
        case ID, UUID -> {
          MessageUtil.sendError(sender, "&e" + keyString + "&r 키의 값은 변경할 수 없습니다.");
          return true;
        }
        case ITEM_DROP_MODE, ITEM_PICKUP_MODE -> {
          if (!value.equals("normal") && !value.equals("sneak") && !value.equals("disabled"))
          {
            MessageUtil.noArg(sender, Prefix.NO_VALUE, value);
            return true;
          }
          config.getConfig().set(key.getKey(), value);
        }
        case ITEM_USE_DELAY, ITEM_DROP_DELAY -> {
          if (!MessageUtil.isInteger(sender, value, true))
          {
            return true;
          }
          int intVal = Integer.parseInt(value);
          if (!MessageUtil.checkNumberSize(sender, intVal, 0, 200))
          {
            return true;
          }
          config.getConfig().set(key.getKey(), intVal);
        }
        default -> {
          if (!value.equals("true") && !value.equals("false"))
          {
            MessageUtil.wrongBool(sender, 3, args);
            return true;
          }
          config.getConfig().set(key.getKey(), Boolean.parseBoolean(value));
        }
      }
      config.saveConfig();
      try
      {
        key.set(player.getUniqueId(), config.getConfig().get(key.getKey()));
      }
      catch (Exception ignored)
      {
      }
      if (!hideMessage)
      {
        MessageUtil.sendMessage(sender, Prefix.INFO_SETDATA, player, "의 &e" + keyString + "&r 값을 &e" + value + "&r으로 설정하였습니다.");
        if (isOnline && !player.equals(sender))
        {
          MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, sender, "이 당신의 &e" + keyString + "&r 값을 &e" + value + "&r으로 설정하였습니다.");
        }
      }
      if (isOnline)
      {
        if (key == UserData.HEALTH_SCALED)
        {
          ((Player) player).setHealthScaled(!UserData.HEALTH_SCALED.getBoolean(uuid));
        }
        if (CommandSong.radioSongPlayer != null)
        {
          if (UserData.LISTEN_GLOBAL.getBoolean(player.getUniqueId()) || UserData.LISTEN_GLOBAL_FORCE.getBoolean(player.getUniqueId()))
          {
            CommandSong.radioSongPlayer.addPlayer((Player) player);
          }
          else
          {
            CommandSong.radioSongPlayer.removePlayer((Player) player);
          }
        }
        if (CommandSong.playerRadio.containsKey(uuid))
        {
          if (UserData.LISTEN_GLOBAL.getBoolean(player.getUniqueId()) || UserData.LISTEN_GLOBAL_FORCE.getBoolean(player.getUniqueId()))
          {
            CommandSong.playerRadio.get(uuid).addPlayer((Player) player);
          }
          else
          {
            CommandSong.playerRadio.get(uuid).removePlayer((Player) player);
          }
        }
        switch (key)
        {
          case USE_HELPFUL_LORE_FEATURE:
          case DISABLE_HELPFUL_FEATURE_WHEN_CREATIVE:
            Method.updateInventory((Player) player);
            break;
          case ENTITY_AGGRO:
          case SPECTATOR_MODE:
            if (!UserData.ENTITY_AGGRO.getBoolean(player) || UserData.SPECTATOR_MODE.getBoolean(player))
            {
              Player online = (Player) player;
              if (UserData.SPECTATOR_MODE.getBoolean(player))
              {
                online.setGameMode(GameMode.SPECTATOR);
              }
              for (Entity entity : online.getWorld().getEntities())
              {
                if (entity instanceof Mob mob)
                {
                  LivingEntity livingEntity = mob.getTarget();
                  if (online.equals(livingEntity))
                  {
                    mob.setTarget(null);
                  }
                }
              }
            }
          default:
            break;
        }
      }
    }
    else
    {
      MessageUtil.longArg(sender, 4, args);
      MessageUtil.commandInfo(sender, label, usage);
      return true;
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
      if (label.equals("sud"))
      {
        return Method.tabCompleterPlayer(sender, args, "<플레이어>", true);
      }
      return Method.tabCompleterOfflinePlayer(sender, args, "<플레이어>", true);
    }
    else if (length == 2)
    {
      if (Method.equals(args[1], "id", "uuid"))
      {
        return Collections.singletonList(args[1] + "(" + UserData.valueOf(args[1].toUpperCase()).getKey().replace("-", " ") + ")" + " 키의 값은 변경할 수 없습니다.");
      }
      List<String> list = Method.enumToList(UserData.values());
      list.remove("id");
      list.remove("uuid");
      return Method.tabCompleterList(args, list, "<데이터 키>");
    }
    else if (length == 3)
    {
      UserData key;
      try
      {
        key = UserData.valueOf(args[1].toUpperCase());
      }
      catch (Exception e)
      {
        return Collections.singletonList(args[1] + MessageUtil.getFinalConsonant(args[1], MessageUtil.ConsonantType.은는) + "잘못된 데이터 키입니다.");
      }
      return switch (key)
              {
                case ITEM_DROP_MODE, ITEM_PICKUP_MODE -> Method.tabCompleterList(args, "<모드>", "normal", "sneak", "disabled");
                default -> Method.tabCompleterBoolean(args, "<값>");
                case DISPLAY_NAME, PLAYER_LIST_NAME -> Collections.singletonList("닉네임은 닉네임 명령어(/nick, /nickothers)를 사용하여 변경해주세요.");
                case HEALTH_BAR -> Collections.singletonList("HP바는 hp바 명령어(/shp)를 사용하여 변경해주세요.");
                case ID, UUID -> Collections.singletonList(args[1] + "(" + key.getKey().replace("-", " ") + ")" + " 키의 값은 변경할 수 없습니다.");
                case ITEM_USE_DELAY, ITEM_DROP_DELAY -> Method.tabCompleterIntegerRadius(args, 0, 200, "<틱>");
              };
    }
    else if (length == 4)
    {
      return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
    }

    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}

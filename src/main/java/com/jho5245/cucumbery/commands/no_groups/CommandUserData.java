package com.jho5245.cucumbery.commands.no_groups;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.util.no_groups.*;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CommandUserData implements CucumberyCommandExecutor
{
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_USERDATA, true))
    {
      return true;
    }
    boolean failure = !(sender instanceof BlockCommandSender);
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return failure;
    }
    String usage = cmd.getUsage().replace("/<command> ", "");
    int length = args.length;
    if (length < 2)
    {
      MessageUtil.shortArg(sender, 3, args);
      MessageUtil.commandInfo(sender, label, usage);
      return true;
    }
    else if (args.length <= 4)
    {
      List<OfflinePlayer> offlinePlayers = SelectorUtil.getOfflinePlayers(sender, args[0], true);
      if (offlinePlayers == null)
      {
        return failure;
      }
      String keyString = args[1].toLowerCase();
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
      if (length == 2)
      {
        HashMap<Object, List<OfflinePlayer>> map = new HashMap<>();
        for (OfflinePlayer offlinePlayer : offlinePlayers)
        {
          Object obj = key.get(offlinePlayer);
          List<OfflinePlayer> list = map.getOrDefault(obj, new ArrayList<>());
          list.add(offlinePlayer);
          map.put(obj, list);
        }
        for (Object obj : map.keySet())
        {
          MessageUtil.info(sender, "%s의 %s은(는) %s입니다", map.get(obj), key, Constant.THE_COLOR_HEX + obj);
        }
        return true;
      }
      boolean hideOutput = args.length == 4 && args[3].equalsIgnoreCase("true");
      String value = args[2].toLowerCase();
      List<OfflinePlayer> successPlayers = new ArrayList<>();
      for (OfflinePlayer offlinePlayer : offlinePlayers)
      {
        UUID uuid = offlinePlayer.getUniqueId();
        if (!key.get(uuid).toString().equalsIgnoreCase(value))
        {
          successPlayers.add(offlinePlayer);
        }
        CustomConfig config = CustomConfig.getPlayerConfig(uuid);
        switch (key)
        {
          case DISPLAY_NAME, PLAYER_LIST_NAME ->
          {
            MessageUtil.sendError(sender, "닉네임은 닉네임 명령어(rg255,204;/nick&r, rg255,204;/nickothers&r)를 사용하여 변경해주세요.");
            return true;
          }
          case HEALTH_BAR ->
          {
            MessageUtil.sendError(sender, "HP바는 HP바 명령어(rg255,204;/shp&r)를 사용하여 변경해주세요.");
            return true;
          }
          case ID, UUID ->
          {
            MessageUtil.sendError(sender, "%s 키의 값은 변경할 수 없습니다", key);
            return true;
          }
          case ITEM_DROP_MODE, ITEM_PICKUP_MODE ->
          {
            if (!value.equals("normal") && !value.equals("sneak") && !value.equals("disabled"))
            {
              MessageUtil.noArg(sender, Prefix.NO_VALUE, value);
              return true;
            }
            config.getConfig().set(key.getKey(), value);
          }
          case ITEM_USE_DELAY, ITEM_DROP_DELAY ->
          {
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
          case INVINCIBLE_TIME, INVINCIBLE_TIME_JOIN ->
          {
            if (!MessageUtil.isInteger(sender, value, true))
            {
              return true;
            }
            int intVal = Integer.parseInt(value);
            if (!MessageUtil.checkNumberSize(sender, intVal, -1, 2000))
            {
              return true;
            }
            config.getConfig().set(key.getKey(), intVal);
          }
          case CUSTOM_MINING_COOLDOWN_DISPLAY_BLOCK -> {
            Material material = Method2.valueOf(value.toUpperCase(), Material.class);
            if (material == null || !material.isBlock())
            {
              MessageUtil.sendError(sender, "argument.block.id.invalid", value);
              return true;
            }
            config.getConfig().set(key.getKey(), material.toString());
          }
          default ->
          {
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
          key.set(uuid, config.getConfig().get(key.getKey()));
        }
        catch (Exception ignored)
        {
        }
      }
      boolean successPlayersEmpty = successPlayers.isEmpty();
      if (!hideOutput)
      {
        List<OfflinePlayer> failurePlayers = new ArrayList<>(offlinePlayers);
        failurePlayers.removeAll(successPlayers);
        if (!failurePlayers.isEmpty())
        {
          MessageUtil.sendWarnOrError(successPlayersEmpty, sender,
                  ComponentUtil.translate("변경 사항이 없습니다. 이미 %s의 %s 값이 %s입니다", failurePlayers, key, Constant.THE_COLOR_HEX + value));
        }
        if (!successPlayersEmpty)
        {
          MessageUtil.info(sender, "%s의 %s 값을 %s(으)로 설정했습니다", successPlayers, key, Constant.THE_COLOR_HEX + value);
          MessageUtil.sendAdminMessage(sender, "%s의 %s 값을 %s(으)로 설정했습니다", successPlayers, key, Constant.THE_COLOR_HEX + value);
          List<Audience> infoTarget = new ArrayList<>();
          successPlayers.forEach(offlinePlayer ->
          {
            if (offlinePlayer instanceof Audience audience)
            {
              infoTarget.add(audience);
            }
          });
          infoTarget.remove(sender);
          MessageUtil.sendMessage(infoTarget, Prefix.INFO_SETDATA, "%s이(가) 당신의 %s 값을 %s(으)로 설정했습니다", sender, key, Constant.THE_COLOR_HEX + value);
        }
      }
      return !successPlayersEmpty || failure;
    }
    else
    {
      MessageUtil.longArg(sender, 4, args);
      MessageUtil.commandInfo(sender, label, usage);
      return failure;
    }
  }

  @Override
  public @NotNull List<Completion> completion(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args, @NotNull Location location)
  {
    int length = args.length;
    if (length == 1)
    {
      if (label.equals("ud"))
      {
        return CommandTabUtil.tabCompleterPlayer(sender, args, "<플레이어>");
      }
      return CommandTabUtil.tabCompleterOfflinePlayer(sender, args, "<플레이어>");
    }
    else if (length == 2)
    {
      return CommandTabUtil.tabCompleterList(args, UserData.values(), "<데이터 키>");
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
        return CommandTabUtil.errorMessage("%s은(는) 잘못된 데이터 키입니다", args[1]);
      }
      return switch (key)
              {
                case ITEM_DROP_MODE, ITEM_PICKUP_MODE -> CommandTabUtil.tabCompleterList(args, "<모드>", false, "normal", "sneak", "disabled");
                case DISPLAY_NAME, PLAYER_LIST_NAME -> CommandTabUtil.errorMessage("닉네임은 닉네임 명령어(/nick, /nickothers)를 사용하여 변경해주세요.");
                case HEALTH_BAR -> CommandTabUtil.errorMessage("HP바는 hp바 명령어(/shp)를 사용하여 변경해주세요.");
                case ID, UUID -> CommandTabUtil.errorMessage(args[1] + "(" + key.getKey().replace("-", " ") + ")" + " 키의 값은 변경할 수 없습니다");
                case ITEM_USE_DELAY, ITEM_DROP_DELAY -> CommandTabUtil.tabCompleterIntegerRadius(args, 0, 200, "<틱>");
                case INVINCIBLE_TIME, INVINCIBLE_TIME_JOIN -> CommandTabUtil.tabCompleterIntegerRadius(args, -1, 2000, "<틱>");
                case CUSTOM_MINING_COOLDOWN_DISPLAY_BLOCK -> CommandTabUtil.tabCompleterList(args, Material.values(), "<블록 유형>", material -> !material.isBlock());
                default -> CommandTabUtil.tabCompleterBoolean(args, "<값>");
              };
    }
    else if (length == 4)
    {
      return CommandTabUtil.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
    }
    return CommandTabUtil.ARGS_LONG;
  }
}

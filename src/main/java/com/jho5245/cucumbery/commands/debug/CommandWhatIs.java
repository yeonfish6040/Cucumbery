package com.jho5245.cucumbery.commands.debug;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.util.no_groups.*;

import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import net.kyori.adventure.text.Component;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommandWhatIs implements CucumberyCommandExecutor
{
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_WHATIS, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    String usage = cmd.getUsage().replace("/<command> ", ""), consoleUsage = usage.replace("\\[월드 이름\\]", "<월드 이름>");
    if (args.length == 0 && !(sender instanceof Player))
    {
      MessageUtil.shortArg(sender, 1, args);
      MessageUtil.commandInfo(sender, label, consoleUsage);
      return true;
    }
    else if (args.length <= 2)
    {
      World world;
      if (sender instanceof Player player && args.length == 0)
      {
        world = player.getLocation().getWorld();
      }
      else
      {
        world = CommandArgumentUtil.world(sender, args[0], true);
      }
      if (world == null)
      {
        return true;
      }
      if (args.length == 2)
      {
        if ("forecast".equals(args[1]))
        {
          weatherForecast(world, sender);
        }
        else
        {
          MessageUtil.wrongArg(sender, 2, args);
          return true;
        }
        return true;
      }
      world.getDifficulty().translationKey();
      String allowAnimals = world.getAllowAnimals() ? "&a동물 소환 가능" : "&c동물 소환 불가능";
      String allowMonsters = world.getAllowMonsters() ? "&a몬스터 소환 가능" : "&c몬스터 소환 불가능";
      int ambientSpawnLimit = world.getAmbientSpawnLimit();
      int animalSpawnLimit = world.getAnimalSpawnLimit();
      String worldType = switch (world.getEnvironment())
              {
                case NETHER -> "네더";
                case NORMAL -> "오버월드";
                case THE_END -> "디 엔드";
                case CUSTOM -> "사용자 지정";
              };
      Component fullTime = MessageUtil.periodRealTimeAndGameTime(world.getFullTime());
      Component gamerule = Component.empty();
      GameRule<?>[] gameRules = GameRule.values();
      for (int i = 0; i < gameRules.length; i++)
      {
        String color = "&" + Integer.toHexString((i + 1) % 2 + 10);
        GameRule<?> rule = gameRules[i];
        String ruleString = rule.getName();
        String key = rule.translationKey();
        Object value = world.getGameRuleValue(rule), defaultValue = world.getGameRuleDefault(rule);
        boolean isDefault = Objects.equals(value, defaultValue);
        if (value == null)
        {
          value = "&ff0000;null";
        }
        if (value.equals(true))
        {
          value = (isDefault ? "&3" : "&9") + value;
        }
        else if (value.equals(false))
        {
          value = (isDefault ? "&c" : "&4") + value;
        }
        else if (value instanceof Number)
        {
          value = (isDefault ? "rg255,204;" : "&6") + value;
        }
        Component component =  ComponentUtil.create(color + ruleString);
        Component hover = Component.empty().append(ComponentUtil.translate(color + key));
        String description = Variable.lang.getString(key.replace(".", "-") + "-description");
        if (description != null)
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.translate(key + ".description"));
        }
        component = component.hoverEvent(hover);
        gamerule = gamerule.append(ComponentUtil.translate("%s : %s", component, value));
        if (i != GameRule.values().length - 1)
        {
          gamerule = gamerule.append(ComponentUtil.create("&7, "));
        }
      }
      int maxHeight = world.getMaxHeight();
      int monsterSpawnLimit = world.getMonsterSpawnLimit();
      String pvp = world.getPVP() ? "&aPVP 가능" : "&cPVP 불가능";
      int seaLevel = world.getSeaLevel();
      long seed = world.getSeed();
      Location spawnLocation = world.getSpawnLocation();
      String x = Constant.Sosu2.format(spawnLocation.getX());
      String y = Constant.Sosu2.format(spawnLocation.getY());
      String z = Constant.Sosu2.format(spawnLocation.getZ());
      String yaw = Constant.Sosu2.format(spawnLocation.getYaw());
      String pitch = Constant.Sosu2.format(spawnLocation.getPitch());
      Vector direction = spawnLocation.getDirection();
      long ticksPerAnimalSpawns = world.getTicksPerAnimalSpawns();
      long ticksPerMonsterSpawns = world.getTicksPerMonsterSpawns();
      long timeNumber = world.getTime();
      String time = (timeNumber >= 6000 && timeNumber < 18000 ? "오후" : "오전") + " " + currentTime((int) timeNumber);
      String uuid = world.getUID().toString();
      int waterAnimalSpanwLimit = world.getWaterAnimalSpawnLimit();
      List<Component> weather = getForecastInfo(world);
      WorldBorder worldBorder = world.getWorldBorder();
      Location center = worldBorder.getCenter();
      String centerX = Constant.Sosu2.format(center.getX());
      String centerZ = Constant.Sosu2.format(center.getZ());
      double damageAmount = worldBorder.getDamageAmount();
      double damageBuffer = worldBorder.getDamageBuffer();
      double size = worldBorder.getSize();
      int warningDistance = worldBorder.getWarningDistance();
      int warningTime = worldBorder.getWarningTime();
      String canGenerateStructures = world.canGenerateStructures() ? "&a구조물 생성이 되는 월드" : "&c구조물 생성이 안되는 월드";

      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, Constant.separatorSubString(3));
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "%s의 정보", world);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "난이도 : %s", ComponentUtil.translate(world.getDifficulty().translationKey(), Constant.THE_COLOR));
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "월드 환경 : rg255,204;" + worldType);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, pvp);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, canGenerateStructures);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "현재 시각 : rg255,204;" + time);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "누적 시간 : %s", fullTime);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "UUID : rg255,204;" + uuid);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "시드 : rg255,204;" + seed);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "해수면 높이 : rg255,204;" + seaLevel);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, allowAnimals);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, allowMonsters);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "- 스폰 위치 -");
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "X : rg255,204;" + x);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "Y : rg255,204;" + y);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "Z : rg255,204;" + z);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "Yaw : rg255,204;" + yaw);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "Pitch : rg255,204;" + pitch);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "Vector : rg255,204;" + direction);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, Constant.separatorSubString(3));
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "최대 동물 소환 제한 : rg255,204;" + animalSpawnLimit);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "최대 몬스터 소환 제한 : rg255,204;" + monsterSpawnLimit);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "최대 수상 동물 소환 제한 : rg255,204;" + waterAnimalSpanwLimit);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "최대 기타 개체 소환 제한 : rg255,204;" + ambientSpawnLimit);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "틱당 동물 소환 : rg255,204;" + ticksPerAnimalSpawns);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "틱당 몬스터 소환 : rg255,204;" + ticksPerMonsterSpawns);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "일기 예보 : %s", weather.get(0));
      if (weather.size() == 2)
      {
        MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, weather.get(1));
      }
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "최대 월드 높이 : rg255,204;" + maxHeight);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "- 세계 경계 -");
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "중심 X : rg255,204;" + centerX);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "중심 Z : rg255,204;" + centerZ);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "피해량 : rg255,204;" + damageAmount);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "피해 버퍼 : rg255,204;" + damageBuffer);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "경고 알림 거리 : rg255,204;" + warningDistance);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "경고 시간 : rg255,204;" + warningTime);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "크기(한 변의 길이) : rg255,204;" + Constant.Sosu2.format(size));
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, Constant.separatorSubString(3));
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "게임룰 : %s", gamerule);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, Constant.separatorSubString(3));
    }
    else
    {
      MessageUtil.longArg(sender, 2, args);
      if (!(sender instanceof Player))
      {
        MessageUtil.commandInfo(sender, label, consoleUsage);
      }
      else
      {
        MessageUtil.commandInfo(sender, label, usage);
      }
    }
    return true;
  }
  
  public static void weatherForecast(@NotNull World world, @NotNull CommandSender sender)
  {
    List<Component> weather = getForecastInfo(world);
    for (Component info : weather)
    {
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, info);
    }
  }

  @Override
  public @NotNull List<Completion> completion(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args, @NotNull Location location)
  {
    int length = args.length;
    if (length == 1)
    {
      return CommandTabUtil.worldArgument(sender, args, "<월드>");
    }
    if (length == 2)
    {
      return CommandTabUtil.tabCompleterList(args, "<인수>", false, Completion.completion("forecast", ComponentUtil.translate("현재 월드의 날씨 정보만 참조")));
    }
    return CommandTabUtil.ARGS_LONG;
  }

  /**
   * 마인크래프트의 하루의 시각을 가져옵니다 (0 = 오전 6시, 6000 = 오후 12시, 12000 = 오후 6시, 18000 = 오전 12시)
   *
   * @param t 현재 시각
   * @return 시각 표시
   */
  @NotNull
  public static String currentTime(int t)
  {
    final int input = t + 6000;
    int time = (int) (input / 1000D);
    if (time > 12 && time <= 24)
    {
      time -= 12;
    }
    if (time > 24)
    {
      time -= 24;
    }
    int minute = (int) (t % 1000 * 0.06D);
    int second = t % 20 * 3;
    return time + "시" + ((minute != 0) ? " " + minute + "분" : "") + ((second != 0) ? " " + second + "초" : "");
  }

  @NotNull
  private static List<Component> getForecastInfo(@NotNull World world)
  {
    List<Component> returnValue = new ArrayList<>();
    int thunderDuration = world.getThunderDuration();
    int weatherDuration = world.getWeatherDuration();
    Component weather = Component.empty();
    boolean additionalInfo = false;
    returnValue.add(ComponentUtil.translate("현재 날씨는 %s이며", ComponentUtil.translate(world.hasStorm() ? (world.isThundering() ? "&e번개를 동반한 비" : "&e비") : "&e맑음")));
    if (!world.hasStorm())
    {
      if (weatherDuration < thunderDuration && world.isThundering())
      {
        weather = ComponentUtil.translate("%s 이후 번개를 동반한 비가 올 예정입니다", MessageUtil.periodRealTimeAndGameTime(thunderDuration));
      }
      else
      {
        weather = ComponentUtil.translate("%s 이후 비가 올 예정입니다", MessageUtil.periodRealTimeAndGameTime(thunderDuration));
      }
    }
    else if (world.hasStorm() && !world.isThundering())
    {
      if (weatherDuration < thunderDuration)
      {
        weather = ComponentUtil.translate("%s 이후 비가 그칠 예정입니다", MessageUtil.periodRealTimeAndGameTime(weatherDuration));
      }
      else
      {
        weather = ComponentUtil.translate("%s 이후 번개를 동반한 비가 올 예정입니다", MessageUtil.periodRealTimeAndGameTime(thunderDuration));
        additionalInfo = true;
      }
    }
    else if (world.hasStorm() && world.isThundering())
    {
      if (weatherDuration < thunderDuration)
      {
        weather = ComponentUtil.translate("%s 이후 비가 그칠 예정입니다", MessageUtil.periodRealTimeAndGameTime(weatherDuration));
      }
      else
      {
        weather = ComponentUtil.translate("%s 이후 번개가 멈출 예정입니다", MessageUtil.periodRealTimeAndGameTime(thunderDuration));
        additionalInfo = true;
      }
    }
    returnValue.add(weather);
    if (additionalInfo)
    {
      returnValue.add(ComponentUtil.translate("아울러, %s 이후 비가 그칠 예정입니다", MessageUtil.periodRealTimeAndGameTime(weatherDuration)));
    }
    return returnValue;
  }
}

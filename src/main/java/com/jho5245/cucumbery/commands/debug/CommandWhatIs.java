package com.jho5245.cucumbery.commands.debug;

import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CommandWhatIs implements CommandExecutor, TabCompleter
{
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
      if (sender instanceof Player && args.length == 0)
      {
        world = ((Player) sender).getLocation().getWorld();
      }
      else
      {
        world = Bukkit.getServer().getWorld(args[0]);
      }
      if (world == null)
      {
        MessageUtil.noArg(sender, Prefix.NO_WORLD, args[0]);
        return true;
      }
      if (args.length == 2)
      {
        if ("forecast".equals(args[1]))
        {
          List<String> weather = getForecastInfo(world);
          MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "---------------일기 예보---------------");
          for (String info : weather)
          {
            MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, info);
          }
        }
        else
        {
          MessageUtil.wrongArg(sender, 2, args);
          return true;
        }
        return true;
      }
      String worldName = world.getName();
      world.getDifficulty().translationKey();
      String difficulty = switch (world.getDifficulty())
              {
                case EASY -> "쉬움";
                case HARD -> "어려움";
                case NORMAL -> "보통";
                case PEACEFUL -> "평화로움";
              };
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
      String fullTime = MessageUtil.periodRealTimeAndGameTime(world.getFullTime());
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
          value = (isDefault ? "&e" : "&6") + value;
        }
        Component component =  ComponentUtil.create(color + ruleString);
        Component hover = Component.empty().append(ComponentUtil.translate(color + key));
        String description = Variable.lang.getString(key.replace(".", "-") + "-description");
        if (description != null)
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(Component.translatable(key + ".description"));
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
      List<String> weather = getForecastInfo(world);
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

      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "-------------------------------------------------");
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "&e&l" + worldName + "&6&l 월드의 정보");
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "난이도 : &e" + difficulty);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "월드 환경 : &e" + worldType);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, pvp);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, canGenerateStructures);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "현재 시각 : &e" + time);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "누적 시간 : &e" + fullTime);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "UUID : &e" + uuid);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "시드 : &e" + seed);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "해수면 높이 : &e" + seaLevel);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, allowAnimals);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, allowMonsters);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "- 스폰 위치 -");
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "X : &e" + x);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "Y : &e" + y);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "Z : &e" + z);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "Yaw : &e" + yaw);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "Pitch : &e" + pitch);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "Vector : &e" + direction);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "------------");
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "최대 동물 소환 제한 : &e" + animalSpawnLimit);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "최대 몬스터 소환 제한 : &e" + monsterSpawnLimit);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "최대 수상 동물 소환 제한 : &e" + waterAnimalSpanwLimit);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "최대 기타 개체 소환 제한 : &e" + ambientSpawnLimit);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "틱당 동물 소환 : &e" + ticksPerAnimalSpawns);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "틱당 몬스터 소환 : &e" + ticksPerMonsterSpawns);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "일기 예보 : &r" + weather.get(0));
      if (weather.size() == 2)
      {
        MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, weather.get(1));
      }
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "최대 월드 높이 : &e" + maxHeight);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "- 세계 경계 -");
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "중심 X : &e" + centerX);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "중심 Z : &e" + centerZ);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "피해량 : &e" + damageAmount);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "피해 버퍼 : &e" + damageBuffer);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "경고 알림 거리 : &e" + warningDistance);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "경고 시간 : &e" + warningTime);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "크기(한 변의 길이) : &e" + Constant.Sosu2.format(size));
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "------------");
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "게임룰 : %s", gamerule);
      MessageUtil.sendMessage(sender, Prefix.INFO_WHATIS, "-------------------------------------------------");
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

  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(true, args), true))
    {
      return Collections.singletonList(args[0]);
    }
    int length = args.length;
    if (length == 1)
    {
      return Method.tabCompleterList(args, Method.listWorlds(), "[월드]");
    }
    else if (length == 2)
    {
      return Method.tabCompleterList(args, "[인수]", "forecast");
    }
    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }

  /**
   * 마인크래프트의 하루의 시각을 가져옵니다 (0 = 오전 6시, 6000 = 오후 12시, 12000 = 오후 6시, 18000 = 오전 12시)
   *
   * @param t 현재 시각
   * @return 시각 표시
   */
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
  private static List<String> getForecastInfo(@NotNull World world)
  {
    List<String> returnValue = new ArrayList<>();
    int thunderDuration = world.getThunderDuration();
    int weatherDuration = world.getWeatherDuration();
    String weather = "";
    boolean additionalInfo = false;
    if (!world.hasStorm())
    {
      if (weatherDuration < thunderDuration && world.isThundering())
      {
        weather = "현재 날씨는 &e맑음&r이며, &e" + MessageUtil.periodRealTimeAndGameTime(thunderDuration) + "&r 이후 번개를 동반한 비가 올 예정입니다";
      }
      else
      {
        weather = "현재 날씨는 &e맑음&r이며, &e" + MessageUtil.periodRealTimeAndGameTime(weatherDuration) + "&r 이후 비가 올 예정입니다";
      }
    }
    else if (world.hasStorm() && !world.isThundering())
    {
      if (weatherDuration < thunderDuration)
      {
        weather = "현재 날씨는 &e비가 오는 중&r이며, &e" + MessageUtil.periodRealTimeAndGameTime(weatherDuration) + "&r 이후 비가 그칠 예정입니다";
      }
      else
      {
        weather = "현재 날씨는 &e비가 오는 중&r이며, &e" + MessageUtil.periodRealTimeAndGameTime(thunderDuration) + "&r 이후 번개를 동반한 비가 올 예정입니다";
        additionalInfo = true;
      }
    }
    else if (world.hasStorm() && world.isThundering())
    {
      if (weatherDuration < thunderDuration)
      {
        weather = "현재 날씨는 &e번개를 동반한 비가 오는 중&r이며, &e" + MessageUtil.periodRealTimeAndGameTime(weatherDuration) + "&r 이후 비가 그칠 예정입니다";
      }
      else
      {
        weather = "현재 날씨는 &e번개를 동한반 비가 오는 중&r이며, &e" + MessageUtil.periodRealTimeAndGameTime(thunderDuration) + "&r 이후 번개구름이 사라질 예정입니다";
        additionalInfo = true;
      }
    }
    returnValue.add(weather);
    if (additionalInfo)
    {
      returnValue.add("아울러, &e" + MessageUtil.periodRealTimeAndGameTime(weatherDuration) + "&r 이후 비가 그칠 예정입니다");
    }
    return returnValue;
  }
}

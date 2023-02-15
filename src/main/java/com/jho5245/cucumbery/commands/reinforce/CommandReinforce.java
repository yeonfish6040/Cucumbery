package com.jho5245.cucumbery.commands.reinforce;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.commands.reinforce.CommandReinforceConstantsAndUtils.OperationType;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeReinforce;
import com.jho5245.cucumbery.util.no_groups.AsyncTabCompleter;
import com.jho5245.cucumbery.util.no_groups.CommandTabUtil;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.SoundCategory;
import org.bukkit.command.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.util.Consumer;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.*;

@SuppressWarnings("all")
public class CommandReinforce implements CommandExecutor, TabCompleter, AsyncTabCompleter
{
  public static final Set<UUID> NO_STAR_CATCH_ENABLED = new HashSet<>(), REINFORCE_OPERATING = new HashSet<>();
  public static final Set<UUID> CHANCE_TIME = new HashSet<>();
  protected static final Set<UUID> ANTI_DESTRUCTION_ENABLED = new HashSet<>();
  protected static final int ANTI_DESTRUCTION_STARFORCE_FROM = 12, ANTI_DESTRUCTION_STARFORCE_TO = 16, ANTI_DESTRUCTION_CUCUMBERFORCE_FROM = 24, ANTI_DESTRUCTION_CUCUMBERFORCE_TO = 33;
  protected static final List<String> OPTION = Arrays.asList("STR", "DEX", "INT", "LUK", "최대 HP", "최대 MP", "방어력", "이동속도", "점프력", "공격력", "마력");
  protected static DecimalFormat df2 = new DecimalFormat("#,###.##"), df3 = new DecimalFormat("#,###.######"), round = new DecimalFormat("####.##");

  protected static double[] getStarForceInfo(int amount)
  {
    double[] info = new double[4]; // 성공 확률, 실패(유지) 확률, 실패(하락) 확률, 파괴 확률

    switch (amount)
    {
      case 0:
        info = new double[]{
                95D, 5D, 0D, 0D
        };
        break;
      case 1:
        info = new double[]{
                90D, 10D, 0D, 0D
        };
        break;
      case 2:
        info = new double[]{
                85D, 15D, 0D, 0D
        };
        break;
      case 3:
        info = new double[]{
                85D, 15D, 0D, 0D
        };
        break;
      case 4:
        info = new double[]{
                80D, 20D, 0D, 0D
        };
        break;
      case 5:
        info = new double[]{
                75D, 25D, 0D, 0D
        };
        break;
      case 6:
        info = new double[]{
                70D, 30D, 0D, 0D
        };
        break;
      case 7:
        info = new double[]{
                65D, 35D, 0D, 0D
        };
        break;
      case 8:
        info = new double[]{
                60D, 40D, 0D, 0D
        };
        break;
      case 9:
        info = new double[]{
                55D, 45D, 0D, 0D
        };
        break;
      case 10:
        info = new double[]{
                50D, 50D, 0D, 0D
        };
        break;
      case 11:
        info = new double[]{
                45D, 55D, 0D, 0D
        };
        break;
      case 12:
        info = new double[]{
                40D, 60D, 0D, 0D
        };
        break;
      case 13:
        info = new double[]{
                35D, 65D, 0D, 0D
        };
        break;
      case 14:
        info = new double[]{
                30D, 70D, 0D, 0D
        };
        break;
      case 15:
        info = new double[]{
                30D, 67.9D, 0D, 2.1D
        };
        break;
      case 16:
        info = new double[]{
                30D, 0D, 67.9D, 2.1D
        };
        break;
      case 17:
        info = new double[]{
                30D, 0D, 67.9D, 2.1D
        };
        break;
      case 18:
        info = new double[]{
                30D, 0D, 67.2D, 2.8D
        };
        break;
      case 19:
        info = new double[]{
                30D, 0D, 67.2D, 2.8D
        };
        break;
      case 20:
        info = new double[]{
                30D, 63D, 0D, 7D
        };
        break;
      case 21:
        info = new double[]{
                30D, 0D, 63D, 7D
        };
        break;
      case 22:
        info = new double[]{
                3D, 0D, 77.6D, 19.4D
        };
        break;
      case 23:
        info = new double[]{
                2D, 0D, 68.6D, 29.4D
        };
        break;
      case 24:
        info = new double[]{
                1D, 0D, 59.4D, 39.6D
        };
        break;
      default:
        break;
    }

    return info;
  }

  protected static double[] getSuperiorInfo(int amount)
  {
    double[] info = new double[4]; // 성공 확률, 실패(유지) 확률, 실패(하락) 확률, 파괴 확률

    switch (amount)
    {
      case 0:
        info = new double[]{
                50D, 50D, 0D, 0D
        };
        break;
      case 1:
        info = new double[]{
                50D, 0D, 50D, 0D
        };
        break;
      case 2:
        info = new double[]{
                45D, 0D, 55D, 0D
        };
        break;
      case 3:
        info = new double[]{
                40D, 0D, 60D, 0D
        };
        break;
      case 4:
        info = new double[]{
                40D, 0D, 60D, 0D
        };
        break;
      case 5:
        info = new double[]{
                40D, 0D, 58.2D, 1.8D
        };
        break;
      case 6:
        info = new double[]{
                40D, 0D, 57D, 3D
        };
        break;
      case 7:
        info = new double[]{
                40D, 0D, 55.8D, 4.2D
        };
        break;
      case 8:
        info = new double[]{
                40D, 0D, 54D, 6D
        };
        break;
      case 9:
        info = new double[]{
                37D, 0D, 53.5D, 9.5D
        };
        break;
      case 10:
        info = new double[]{
                35D, 0D, 52D, 13D
        };
        break;
      case 11:
        info = new double[]{
                35D, 0D, 48.7D, 16.3D
        };
        break;
      case 12:
        info = new double[]{
                3D, 0D, 48.5D, 48.5D
        };
        break;
      case 13:
        info = new double[]{
                2D, 0D, 49D, 49D
        };
        break;
      case 14:
        info = new double[]{
                1D, 0D, 49.5D, 49.5D
        };
        break;
      default:
        break;
    }

    return info;
  }

  protected static double[] getCucumberyInfo(int amount)
  {
    double[] info = new double[4]; // 성공 확률, 실패(유지) 확률, 실패(하락) 확률, 파괴 확률

    switch (amount)
    {
      case 0:
        info = new double[]{
                99D, 1D, 0D, 0D
        };
        break;
      case 1:
        info = new double[]{
                98D, 2D, 0D, 0D
        };
        break;
      case 2:
        info = new double[]{
                97D, 3D, 0D, 0D
        };
        break;
      case 3:
        info = new double[]{
                96D, 4D, 0D, 0D
        };
        break;
      case 4:
        info = new double[]{
                95D, 5D, 0D, 0D
        };
        break;
      case 5:
        info = new double[]{
                94D, 6D, 0D, 0D
        };
        break;
      case 6:
        info = new double[]{
                93D, 7D, 0D, 0D
        };
        break;
      case 7:
        info = new double[]{
                92D, 8D, 0D, 0D
        };
        break;
      case 8:
        info = new double[]{
                91D, 9D, 0D, 0D
        };
        break;
      case 9:
        info = new double[]{
                90D, 10D, 0D, 0D
        };
        break;
      case 10:
        info = new double[]{
                90D, 10D, 0D, 0D
        };
        break;
      case 11:
        info = new double[]{
                85D, 10D, 5D, 0D
        };
        break;
      case 12:
        info = new double[]{
                80D, 15D, 5D, 0D
        };
        break;
      case 13:
        info = new double[]{
                75D, 20D, 5D, 0D
        };
        break;
      case 14:
        info = new double[]{
                70D, 25D, 5D, 0D
        };
        break;
      case 15:
        info = new double[]{
                65D, 25D, 10D, 0D
        };
        break;
      case 16:
        info = new double[]{
                60D, 25D, 15D, 0D
        };
        break;
      case 17:
        info = new double[]{
                60D, 20D, 20D, 0D
        };
        break;
      case 18:
        info = new double[]{
                60D, 15D, 25D, 0D
        };
        break;
      case 19:
        info = new double[]{
                60D, 10D, 30D, 0D
        };
        break;
      case 20:
        info = new double[]{
                60D, 40D, 0D, 0D
        };
        break;
      case 21:
        info = new double[]{
                60D, 0D, 40D, 0D
        };
        break;
      case 22:
        info = new double[]{
                55D, 0D, 45D, 0D
        };
        break;
      case 23:
        info = new double[]{
                50D, 0D, 50D, 0D
        };
        break;
      case 24:
        info = new double[]{
                50D, 0D, 49.9D, 0.1D
        };
        break;
      case 25:
        info = new double[]{
                50D, 0D, 49.8D, 0.2D
        };
        break;
      case 26:
        info = new double[]{
                50D, 0D, 49.6D, 0.4D
        };
        break;
      case 27:
        info = new double[]{
                50D, 0D, 49.3D, 0.7D
        };
        break;
      case 28:
        info = new double[]{
                50D, 0D, 48.6D, 1.4D
        };
        break;
      case 29:
        info = new double[]{
                50D, 0D, 48.6D, 1.4D
        };
        break;
      case 30:
        info = new double[]{
                50D, 47.9D, 0D, 2.1D
        };
        break;
      case 31:
        info = new double[]{
                50D, 0D, 47.9D, 2.1D
        };
        break;
      case 32:
        info = new double[]{
                50D, 0D, 47.9D, 2.1D
        };
        break;
      case 33:
        info = new double[]{
                50D, 0D, 47.2D, 2.8D
        };
        break;
      case 34:
        info = new double[]{
                50D, 0D, 47.2D, 2.8D
        };
        break;
      case 35:
        info = new double[]{
                50D, 0D, 45.8D, 4.2D
        };
        break;
      case 36:
        info = new double[]{
                50D, 0D, 45.8D, 4.2D
        };
        break;
      case 37:
        info = new double[]{
                50D, 0D, 43D, 7D
        };
        break;
      case 38:
        info = new double[]{
                50D, 0D, 43D, 7D
        };
        break;
      case 39:
        info = new double[]{
                50D, 0D, 43D, 7D
        };
        break;
      case 40:
        info = new double[]{
                40D, 46D, 0D, 14D
        };
        break;
      case 41:
        info = new double[]{
                35D, 0D, 51D, 14D
        };
      case 42:
        info = new double[]{
                30D, 0D, 56D, 14D
        };
      case 43:
        info = new double[]{
                30D, 0D, 42D, 28D
        };
        break;
      case 44:
        info = new double[]{
                30D, 0D, 42D, 28D
        };
        break;
      case 45:
        info = new double[]{
                3D, 0D, 62D, 35D
        };
        break;
      case 46:
        info = new double[]{
                2D, 0D, 56D, 42D
        };
        break;
      case 47:
        info = new double[]{
                1D, 0D, 43D, 56D
        };
        break;
      case 48:
        info = new double[]{
                0.1D, 0D, 22.9D, 77D
        };
        break;
      case 49:
        info = new double[]{
                0.01D, 0D, 1.99D, 98D
        };
        break;
      default:
        break;
    }

    return info;
  }

  protected static double[] getEBEBEBInfo(int amount)
  {
    double[] info = new double[4]; // 성공 확률, 실패(유지) 확률, 실패(하락) 확률, 파괴 확률

    switch (amount)
    {
      case 0:
        info = new double[]{
                90D, 10D, 0D, 0D
        };
        break;
      case 1:
        info = new double[]{
                60D, 20D, 20D, 0D
        };
        break;
      case 2:
        info = new double[]{
                40D, 30D, 30D, 0D
        };
        break;
      case 3:
        info = new double[]{
                35D, 32.5D, 32.5D, 0D
        };
        break;
      case 4:
        info = new double[]{
                30D, 10D, 58.6D, 1.4D
        };
        break;
      case 5:
        info = new double[]{
                30D, 5D, 61.6D, 3.4D
        };
        break;
      case 6:
        info = new double[]{
                30D, 0D, 66.6D, 3.4D
        };
        break;
      case 7:
        info = new double[]{
                3D, 0D, 63.6D, 33.4D
        };
        break;
      case 8:
        info = new double[]{
                0.6D, 0D, 43.7D, 55.7D
        };
        break;
      case 9:
        info = new double[]{
                0.3D, 0D, 24.2D, 75.5D
        };
        break;
      default:
        break;
    }
    return info;
  }

  /**
   * 강화 비용을 반환합니다
   *
   * @param progress  강화 상태(강화 전 단계)
   * @param itemLevel 아이템의 레벨
   * @param type      강화 유형
   * @return 해당하는 강화 비용 또는 Vault가 사용중이지 않을 경우 0원
   */
  public static double getCost(int progress, int itemLevel, ReinforceType type)
  {
    if (!Cucumbery.using_Vault_Economy)
    {
      return 0d;
    }
    switch (type)
    {
      case CUCUMBERFORCE:
        return Math.pow(itemLevel, 0.9D) * 2D * Math.pow(progress + 1D, 1.8D) * Math.pow(1.1D, progress);
      case EBEBEB:
        return Math.pow(itemLevel, 0.8D) * 4D * Math.pow(progress + 1D, 4D) * Math.pow(1.3D, progress);
      case STARFORCE:
        if (progress <= 9)
        {
          return Math.round(
                  (1000d + Math.pow(itemLevel, 3) * (progress + 1) / 25d)
                          / 100d) * 100d;
        }
        if (progress == 10)
        {
          return Math.round(
                  (1000d + Math.pow(itemLevel, 3) * Math.pow(progress + 1, 2.7) / 400d)
                          / 100d) * 100d;
        }
        if (progress == 11)
        {
          return Math.round(
                  (1000d + Math.pow(itemLevel, 3) * Math.pow(progress + 1, 2.7) / 220d)
                          / 100d) * 100d;
        }
        if (progress == 12)
        {
          return Math.round(
                  (1000d + Math.pow(itemLevel, 3) * Math.pow(progress + 1, 2.7) / 150d)
                          / 100d) * 100d;
        }
        if (progress == 13)
        {
          return Math.round(
                  (1000d + Math.pow(itemLevel, 3) * Math.pow(progress + 1, 2.7) / 110d)
                          / 100d) * 100d;
        }
        if (progress == 14)
        {
          return Math.round(
                  (1000d + Math.pow(itemLevel, 3) * Math.pow(progress + 1, 2.7) / 75d)
                          / 100d) * 100d;
        }
        return Math.round(
                (1000d + Math.pow(itemLevel, 3) * Math.pow(progress + 1, 2.7) / 200d)
                        / 100d) * 100d;
      case SUPERIOR:
        return Math.round(Math.pow(itemLevel, 3.56) / 100d) * 100d;
      default:
        return 0D;
    }
  }

  public static void checkReinforce(Player player, int amount)
  {
    double[] chance = getStarForceInfo(amount);
    double success = chance[0], failKeep = chance[1], failDown = chance[2], destroy = chance[3];
    if (amount >= 10 && amount < 15)
    {
      Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE, "rg255,204;★10성+★").hoverEvent(ComponentUtil.create("&f10성을 달성했습니다!\n&f스타포스 강화에 실패하여 강화 단계가\n&f하락하더라도 10성 밑으로는 하락하지 않습니다"));
      player.sendMessage(msg);
    }
    else if (amount >= 15 && amount < 20)
    {
      Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE, "&b★★15성+★★").hoverEvent(ComponentUtil.create("&f15성을 달성했습니다!\n&f스타포스 강화에 실패하여 강화 단계가\n&f하락하더라도 15성 밑으로는 하락하지 않습니다"));
      player.sendMessage(msg);
    }
    else if (amount >= 20 && amount < 25)
    {
      Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE, "&d★☆★20성+★☆★").hoverEvent(ComponentUtil.create("&f20성을 달성했습니다!\n&f스타포스 강화에 실패하여 강화 단계가\n&f하락하더라도 20성 밑으로는 하락하지 않습니다"));
      player.sendMessage(msg);
    }
    else if (amount == 25)
    {
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&4★&c☆&6★rg255,204;☆&a★&22&b5&3성&9★&1☆&5★&d☆&4★");
    }
    sendChanceMessage(player, success, failKeep, failDown, destroy);
  }

  public static void checkSuperiorReinforce(Player player, int amount)
  {
    double[] chance = getSuperiorInfo(amount);
    double success = chance[0], failKeep = chance[1], failDown = chance[2], destroy = chance[3];
    sendChanceMessage(player, success, failKeep, failDown, destroy);
  }

  public static void checkCucumberForceReinforce(Player player, int amount)
  {
    double[] chance = getCucumberyInfo(amount);
    double success = chance[0], failKeep = chance[1], failDown = chance[2], destroy = chance[3];
    if (amount >= 20 && amount < 30)
    {
      Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE, "rg255,204;★20성+★").hoverEvent(ComponentUtil.create("&f20성을 달성했습니다!\n&f오이포스 강화에 실패하여 강화 단계가\n&f하락하더라도 20성 밑으로는 하락하지 않습니다"));
      player.sendMessage(msg);
    }
    else if (amount >= 30 && amount < 40)
    {
      Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE, "&b★★30성+★★").hoverEvent(ComponentUtil.create("&f30성을 달성했습니다!\n&f오이포스 강화에 실패하여 강화 단계가\n&f하락하더라도 30성 밑으로는 하락하지 않습니다"));
      player.sendMessage(msg);
    }
    else if (amount >= 40 && amount < 50)
    {
      Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE, "&d★☆★40성+★☆★").hoverEvent(ComponentUtil.create("&f40성을 달성했습니다!\n&f오이포스 강화에 실패하여 강화 단계가\n&f하락하더라도 40성 밑으로는 하락하지 않습니다"));
      player.sendMessage(msg);
    }
    else if (amount == 50)
    {
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&4★&c☆&6★rg255,204;☆&a★&25&b0&3성&9★&1☆&5★&d☆&4★");
    }
    sendChanceMessage(player, success, failKeep, failDown, destroy);
  }

  public static void checkEBEBEBReinforce(Player player, int amount)
  {
    double[] chance = getEBEBEBInfo(amount);
    double success = chance[0], failKeep = chance[1], failDown = chance[2], destroy = chance[3];
    sendChanceMessage(player, success, failKeep, failDown, destroy);
  }

  public static double[] getOptions(ReinforceType type, ItemType itemType, int level, int itemLevel)
  {
    switch (type)
    {
      case CUCUMBERFORCE:
        return new double[]{
                ((long) (itemLevel / 100D) + 1) * ((long) (level / 5D) + 1), ((long) (itemLevel / 100D) + 1) * ((long) (level / 5D) + 1), ((long) (itemLevel / 100D) + 1) * ((long) (level / 5D) + 1),
                ((long) (itemLevel / 100D) + 1) * ((long) (level / 5D) + 1), ((long) (itemLevel / 30D) + 1) * (level + 1), ((long) (itemLevel / 30D) + 1) * (level + 1),
                ((long) (itemLevel / 10D) + 1) * ((long) (level * level / 20D) + 1), ((long) (itemLevel / 100D) + 1) * ((long) (level / 20D) + 1), ((long) (itemLevel / 100D) + 1) * ((long) (level / 20D) + 1),
                ((long) (itemLevel / 50D) + 1) * ((long) (level / 30D) + 1), ((long) (itemLevel / 50D) + 1) * ((long) (level / 30D) + 1)
        };
      case EBEBEB:
        return new double[]{
                ((long) (itemLevel / 10D) + 1) * ((long) (level) + 1), ((long) (itemLevel / 10D) + 1) * ((long) (level) + 1), ((long) (itemLevel / 10D) + 1) * ((long) (level) + 1),
                ((long) (itemLevel / 10D) + 1) * ((long) (level) + 1), ((long) (itemLevel / 3D) + 1) * (level + 1), ((long) (itemLevel / 3D) + 1) * (level + 1),
                ((long) (itemLevel / 1D) + 1) * ((long) (level * level / 5D) + 1), ((long) (itemLevel / 10D) + 1) * ((long) (level / 2D) + 1), ((long) (itemLevel / 10D) + 1) * ((long) (level / 2D) + 1),
                ((long) (itemLevel / 5D) + 1) * ((long) (level / 3D) + 1), ((long) (itemLevel / 5D) + 1) * ((long) (level / 3D) + 1)
        };
      case STARFORCE:
        switch (itemType)
        {
          case BOOTS:
            if (itemLevel < 10)
            {
              switch (level)
              {
                case 0:
                case 1:
                case 2:
                  return new double[]{
                          2, 2, 0, 0, 5, 0, 1, 0, 0, 0, 0
                  };
                case 3:
                  return new double[]{
                          2, 2, 0, 0, 10, 0, 1, 1, 1, 0, 0
                  };
                case 4:
                  return new double[]{
                          2, 2, 0, 0, 10, 0, 1, 0, 0, 0, 0
                  };
                case 5:
                  return new double[]{
                          3, 3, 0, 0, 15, 0, 2, 1, 1, 0, 0
                  };
                case 6:
                case 7:
                  return new double[]{
                          3, 3, 0, 0, 15, 0, 2, 0, 0, 0, 0
                  };
                case 8:
                case 9:
                  return new double[]{
                          3, 3, 0, 0, 20, 0, 3, 0, 0, 0, 0
                  };
                case 10:
                  return new double[]{
                          3, 3, 0, 0, 20, 0, 4, 1, 1, 0, 0
                  };
                case 11:
                case 12:
                  return new double[]{
                          3, 3, 0, 0, 25, 0, 4, 0, 0, 0, 0
                  };
                case 13:
                case 14:
                  return new double[]{
                          3, 3, 0, 0, 25, 0, 5, 1, 1, 0, 0
                  };
                case 15:
                case 16:
                  return new double[]{
                          7, 7, 0, 0, 30, 0, 6, 1, 1, 2, 0
                  };
                case 17:
                  return new double[]{
                          7, 7, 0, 0, 30, 0, 7, 1, 1, 3, 0
                  };
                case 18:
                  return new double[]{
                          7, 7, 0, 0, 35, 0, 7, 1, 1, 4, 0
                  };
                case 19:
                  return new double[]{
                          7, 7, 0, 0, 35, 0, 7, 1, 1, 5, 0
                  };
                case 20:
                  return new double[]{
                          7, 7, 0, 0, 40, 0, 7, 1, 1, 6, 0
                  };
                case 21:
                  return new double[]{
                          7, 7, 0, 0, 40, 0, 7, 1, 1, 8, 0
                  };
                case 22:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 19, 0
                  };
                case 23:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 22, 0
                  };
                case 24:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 0
                  };
              }
            }
            else
            {
              switch (level)
              {
                case 0:
                case 1:
                case 2:
                  return new double[]{
                          2, 2, 0, 0, 5, 0, 1 + (long) (itemLevel / 10), 0, 0, 0, 0
                  };
                case 3:
                  return new double[]{
                          2, 2, 0, 0, 10, 0, 1 + (long) (itemLevel / 10), 1, 1, 0, 0
                  };
                case 4:
                  return new double[]{
                          2, 2, 0, 0, 10, 0, 1 + (long) (itemLevel / 10), 0, 0, 0, 0
                  };
                case 5:
                  return new double[]{
                          3, 3, 0, 0, 15, 0, 2 + (long) (itemLevel / 10), 1, 1, 0, 0
                  };
                case 6:
                case 7:
                  return new double[]{
                          3, 3, 0, 0, 15, 0, 2 + (long) (itemLevel / 10), 0, 0, 0, 0
                  };
                case 8:
                case 9:
                  return new double[]{
                          3, 3, 0, 0, 20, 0, 3 + (long) (itemLevel / 10), 0, 0, 0, 0
                  };
                case 10:
                  return new double[]{
                          3, 3, 0, 0, 20, 0, 4 + (long) (itemLevel / 10), 1, 1, 0, 0
                  };
                case 11:
                case 12:
                  return new double[]{
                          3, 3, 0, 0, 25, 0, 4 + (long) (itemLevel / 10), 0, 0, 0, 0
                  };
                case 13:
                case 14:
                  return new double[]{
                          3, 3, 0, 0, 25, 0, 5 + (long) (itemLevel / 10), 1, 1, 0, 0
                  };
                case 15:
                case 16:
                  return new double[]{
                          7 + (long) (itemLevel / 10), 7 + (long) (itemLevel / 10), 0, 0, 30, 0, 6 + (long) (itemLevel / 10), 1, 1, 2 + (long) (itemLevel / 10), 0
                  };
                case 17:
                  return new double[]{
                          7 + (long) (itemLevel / 10), 7 + (long) (itemLevel / 10), 0, 0, 30, 0, 7 + (long) (itemLevel / 10), 1, 1, 3 + (long) (itemLevel / 10), 0
                  };
                case 18:
                  return new double[]{
                          7 + (long) (itemLevel / 10), 7 + (long) (itemLevel / 10), 0, 0, 35, 0, 7 + (long) (itemLevel / 10), 1, 1, 4 + (long) (itemLevel / 10), 0
                  };
                case 19:
                  return new double[]{
                          7 + (long) (itemLevel / 10), 7 + (long) (itemLevel / 10), 0, 0, 35, 0, 7 + (long) (itemLevel / 10), 1, 1, 5 + (long) (itemLevel / 10), 0
                  };
                case 20:
                  return new double[]{
                          7 + (long) (itemLevel / 10), 7 + (long) (itemLevel / 10), 0, 0, 40, 0, 7 + (long) (itemLevel / 10), 1, 1, 6 + (long) (itemLevel / 10), 0
                  };
                case 21:
                  return new double[]{
                          7 + (long) (itemLevel / 10), 7 + (long) (itemLevel / 10), 0, 0, 40, 0, 7 + (long) (itemLevel / 10), 1, 1, 8 + (long) (itemLevel / 10), 0
                  };
                case 22:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 19 + (long) (itemLevel / 10), 0
                  };
                case 23:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 22 + (long) (itemLevel / 10), 0
                  };
                case 24:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 25 + (long) (itemLevel / 10), 0
                  };
              }
            }
            break;
          case BOW:
          case SWORD:
            if (itemLevel < 10)
            {
              switch (level)
              {
                case 0:
                case 1:
                case 2:
                  return new double[]{
                          2, 2, 0, 0, 5, 5, 0, 0, 0, 1, 0
                  };
                case 3:
                case 4:
                  return new double[]{
                          2, 2, 0, 0, 10, 10, 0, 0, 0, 1, 0
                  };
                case 5:
                case 6:
                case 7:
                  return new double[]{
                          3, 3, 0, 0, 10, 10, 0, 0, 0, 1, 0
                  };
                case 8:
                case 9:
                  return new double[]{
                          3, 3, 0, 0, 15, 15, 0, 0, 0, 1, 0
                  };
                case 10:
                  return new double[]{
                          3, 3, 0, 0, 15, 15, 0, 0, 0, 2, 0
                  };
                case 11:
                case 12:
                case 13:
                  return new double[]{
                          3, 3, 0, 0, 20, 20, 0, 0, 0, 2, 0
                  };
                case 14:
                  return new double[]{
                          3, 3, 0, 0, 25, 25, 0, 0, 0, 2, 0
                  };
                case 15:
                case 16:
                  return new double[]{
                          7, 7, 0, 0, 30, 30, 0, 0, 0, 5, 0
                  };
                case 17:
                  return new double[]{
                          7, 7, 0, 0, 30, 30, 0, 0, 0, 6, 0
                  };
                case 18:
                  return new double[]{
                          7, 7, 0, 0, 35, 35, 0, 0, 0, 7, 7
                  };
                case 19:
                  return new double[]{
                          7, 7, 0, 0, 35, 35, 0, 0, 0, 8, 0
                  };
                case 20:
                  return new double[]{
                          7, 7, 0, 0, 35, 35, 0, 0, 0, 9, 0
                  };
                case 21:
                  return new double[]{
                          7, 7, 0, 0, 40, 40, 0, 0, 0, 11, 0
                  };
                case 22:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 31, 0
                  };
                case 23:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 33, 0
                  };
                case 24:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 35, 0
                  };
              }
            }
            else
            {
              switch (level)
              {
                case 0:
                case 1:
                case 2:
                  return new double[]{
                          2, 2, 0, 0, 5, 5, 0, 0, 0, 1 + (long) (itemLevel / 10D), 0
                  };
                case 3:
                case 4:
                  return new double[]{
                          2, 2, 0, 0, 10, 10, 0, 0, 0, 1 + (long) (itemLevel / 10D), 0
                  };
                case 5:
                case 6:
                case 7:
                  return new double[]{
                          3, 3, 0, 0, 10, 10, 0, 0, 0, 2 + (long) (itemLevel / 10D), 0
                  };
                case 8:
                case 9:
                  return new double[]{
                          3, 3, 0, 0, 15, 15, 0, 0, 0, 2 + (long) (itemLevel / 10D), 0
                  };
                case 10:
                  return new double[]{
                          3, 3, 0, 0, 15, 15, 0, 0, 0, 3 + (long) (itemLevel / 10D), 0
                  };
                case 11:
                case 12:
                case 13:
                  return new double[]{
                          3, 3, 0, 0, 20, 20, 0, 0, 0, 3 + (long) (itemLevel / 10D), 0
                  };
                case 14:
                  return new double[]{
                          3, 3, 0, 0, 25, 25, 0, 0, 0, 4 + (long) (itemLevel / 10D), 0
                  };
                case 15:
                case 16:
                  return new double[]{
                          7 + (long) (itemLevel / 10D), 7 + (long) (itemLevel / 10D), 0, 0, 30, 30, 0, 0, 0, 5 + (long) (itemLevel / 10D), 0
                  };
                case 17:
                  return new double[]{
                          7 + (long) (itemLevel / 10D), 7 + (long) (itemLevel / 10D), 0, 0, 30, 30, 0, 0, 0, 6 + (long) (itemLevel / 10D), 0
                  };
                case 18:
                  return new double[]{
                          7 + (long) (itemLevel / 10D), 7 + (long) (itemLevel / 10D), 0, 0, 35, 35, 0, 0, 0, 7 + (long) (itemLevel / 10D), 0
                  };
                case 19:
                  return new double[]{
                          7 + (long) (itemLevel / 10D), 7 + (long) (itemLevel / 10D), 0, 0, 35, 35, 0, 0, 0, 8 + (long) (itemLevel / 10D), 0
                  };
                case 20:
                  return new double[]{
                          7 + (long) (itemLevel / 10D), 7 + (long) (itemLevel / 10D), 0, 0, 35, 35, 0, 0, 0, 9 + (long) (itemLevel / 10D), 0
                  };
                case 21:
                  return new double[]{
                          7 + (long) (itemLevel / 10D), 7 + (long) (itemLevel / 10D), 0, 0, 40, 40, 0, 0, 0, 11 + (long) (itemLevel / 10D), 0
                  };
                case 22:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 31 + (long) (itemLevel / 10D), 0
                  };
                case 23:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 33 + (long) (itemLevel / 10D), 0
                  };
                case 24:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 35 + (long) (itemLevel / 10D), 0
                  };
              }
            }
            break;
          case ETC:
            if (itemLevel < 10)
            {
              switch (level)
              {
                case 0:
                  return new double[]{
                          2, 2, 2, 2, 5, 0, 2, 0, 0, 0, 0
                  };
                case 1:
                case 2:
                  return new double[]{
                          2, 2, 2, 2, 5, 0, 1, 0, 0, 0, 0
                  };
                case 3:
                case 4:
                  return new double[]{
                          2, 2, 2, 2, 10, 0, 1, 0, 0, 0, 0
                  };
                case 5:
                case 6:
                case 7:
                  return new double[]{
                          3, 3, 3, 3, 15, 0, 2, 0, 0, 0, 0
                  };
                case 8:
                case 9:
                case 10:
                  return new double[]{
                          3, 3, 3, 3, 20, 0, 2, 0, 0, 0, 0
                  };
                case 11:
                case 12:
                case 13:
                  return new double[]{
                          3, 3, 3, 3, 25, 0, 2, 0, 0, 0, 0
                  };
                case 14:
                  return new double[]{
                          3, 3, 3, 3, 30, 0, 3, 0, 0, 0, 0
                  };
                case 15:
                case 16:
                  return new double[]{
                          7, 7, 7, 7, 35, 0, 3, 0, 0, 2, 2
                  };
                case 17:
                  return new double[]{
                          7, 7, 7, 7, 35, 0, 3, 0, 0, 3, 3
                  };
                case 18:
                  return new double[]{
                          7, 7, 7, 7, 40, 0, 3, 0, 0, 4, 4
                  };
                case 19:
                  return new double[]{
                          7, 7, 7, 7, 40, 0, 3, 0, 0, 5, 5
                  };
                case 20:
                  return new double[]{
                          7, 7, 7, 7, 40, 0, 3, 0, 0, 6, 6
                  };
                case 21:
                  return new double[]{
                          7, 7, 7, 7, 45, 0, 3, 0, 0, 8, 8
                  };
                case 22:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 27, 27
                  };
                case 23:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 29, 29
                  };
                case 24:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 31, 31
                  };
              }
            }

            else
            {
              switch (level)
              {
                case 0:
                  return new double[]{
                          2, 2, 2, 2, 5, 0, 2 + (long) (itemLevel / 10D), 0, 0, 0, 0
                  };
                case 1:
                case 2:
                  return new double[]{
                          2, 2, 2, 2, 5, 0, 2 + (long) (itemLevel / 10D), 0, 0, 0, 0
                  };
                case 3:
                case 4:
                  return new double[]{
                          2, 2, 2, 2, 10, 0, 2 + (long) (itemLevel / 10D), 0, 0, 0, 0
                  };
                case 5:
                case 6:
                case 7:
                  return new double[]{
                          3, 3, 3, 3, 15, 0, 3 + (long) (itemLevel / 10D), 0, 0, 0, 0
                  };
                case 8:
                case 9:
                case 10:
                  return new double[]{
                          3, 3, 3, 3, 20, 0, 4 + (long) (itemLevel / 10D), 0, 0, 0, 0
                  };
                case 11:
                case 12:
                case 13:
                  return new double[]{
                          3, 3, 3, 3, 25, 0, 5 + (long) (itemLevel / 10D), 0, 0, 0, 0
                  };
                case 14:
                  return new double[]{
                          3, 3, 3, 3, 30, 0, 6 + (long) (itemLevel / 10D), 0, 0, 0, 0
                  };
                case 15:
                case 16:
                  return new double[]{
                          7 + (long) (itemLevel / 10D), 7 + (long) (itemLevel / 10D), 7 + (long) (itemLevel / 10D), 7 + (long) (itemLevel / 10D), 35, 0, 7 + (long) (itemLevel / 10D), 0, 0,
                          2 + (long) (itemLevel / 10D), 2 + (long) (itemLevel / 10D)
                  };
                case 17:
                  return new double[]{
                          7 + (long) (itemLevel / 10D), 7 + (long) (itemLevel / 10D), 7 + (long) (itemLevel / 10D), 7 + (long) (itemLevel / 10D), 35, 0, 8 + (long) (itemLevel / 10D), 0, 0,
                          3 + (long) (itemLevel / 10D), 3 + (long) (itemLevel / 10D)
                  };
                case 18:
                  return new double[]{
                          7 + (long) (itemLevel / 10D), 7 + (long) (itemLevel / 10D), 7 + (long) (itemLevel / 10D), 7 + (long) (itemLevel / 10D), 40, 0, 9 + (long) (itemLevel / 10D), 0, 0,
                          4 + (long) (itemLevel / 10D), 4 + (long) (itemLevel / 10D)
                  };
                case 19:
                  return new double[]{
                          7 + (long) (itemLevel / 10D), 7 + (long) (itemLevel / 10D), 7 + (long) (itemLevel / 10D), 7 + (long) (itemLevel / 10D), 40, 0, 10 + (long) (itemLevel / 10D), 0, 0,
                          5 + (long) (itemLevel / 10D), 5 + (long) (itemLevel / 10D)
                  };
                case 20:
                  return new double[]{
                          7 + (long) (itemLevel / 10D), 7 + (long) (itemLevel / 10D), 7 + (long) (itemLevel / 10D), 7 + (long) (itemLevel / 10D), 40, 0, 11 + (long) (itemLevel / 10D), 0, 0,
                          6 + (long) (itemLevel / 10D), 6 + (long) (itemLevel / 10D)
                  };
                case 21:
                  return new double[]{
                          7 + (long) (itemLevel / 10D), 7 + (long) (itemLevel / 10D), 7 + (long) (itemLevel / 10D), 7 + (long) (itemLevel / 10D), 45, 0, 12 + (long) (itemLevel / 10D), 0, 0,
                          8 + (long) (itemLevel / 10D), 8 + (long) (itemLevel / 10D)
                  };
                case 22:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 27 + (long) (itemLevel / 10D), 27 + (long) (itemLevel / 10D)
                  };
                case 23:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 29 + (long) (itemLevel / 10D), 29 + (long) (itemLevel / 10D)
                  };
                case 24:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 31 + (long) (itemLevel / 10D), 31 + (long) (itemLevel / 10D)
                  };
              }
            }
            break;
          case HELMET:
          case CHESTPLATE:
          case LEGGINGS:
            if (itemLevel < 10)
            {
              switch (level)
              {
                case 0:
                case 1:
                case 2:
                  return new double[]{
                          2, 2, 0, 0, 5, 0, 1, 0, 0, 0, 0
                  };
                case 3:
                  return new double[]{
                          2, 2, 0, 0, 10, 0, 0, 0, 1, 0, 0
                  };
                case 4:
                  return new double[]{
                          2, 2, 0, 0, 10, 0, 1, 0, 0, 0, 0
                  };
                case 5:
                  return new double[]{
                          3, 3, 0, 0, 15, 0, 2, 0, 0, 0, 0
                  };
                case 6:
                case 7:
                  return new double[]{
                          3, 3, 0, 0, 15, 0, 2, 0, 0, 0, 0
                  };
                case 8:
                case 9:
                  return new double[]{
                          3, 3, 0, 0, 20, 0, 3, 0, 0, 0, 0
                  };
                case 10:
                  return new double[]{
                          3, 3, 0, 0, 20, 0, 4, 0, 0, 0, 0
                  };
                case 11:
                case 12:
                  return new double[]{
                          3, 3, 0, 0, 25, 0, 4, 0, 0, 0, 0
                  };
                case 13:
                case 14:
                  return new double[]{
                          3, 3, 0, 0, 25, 0, 5, 0, 0, 0, 0
                  };
                case 15:
                case 16:
                  return new double[]{
                          7, 7, 0, 0, 30, 0, 6, 0, 0, 2, 0
                  };
                case 17:
                  return new double[]{
                          7, 7, 0, 0, 30, 0, 7, 0, 0, 3, 0
                  };
                case 18:
                  return new double[]{
                          7, 7, 0, 0, 35, 0, 7, 0, 0, 4, 0
                  };
                case 19:
                  return new double[]{
                          7, 7, 0, 0, 35, 0, 7, 0, 0, 5, 0
                  };
                case 20:
                  return new double[]{
                          7, 7, 0, 0, 40, 0, 7, 0, 0, 6, 0
                  };
                case 21:
                  return new double[]{
                          7, 7, 0, 0, 40, 0, 7, 0, 0, 8, 0
                  };
                case 22:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 19, 0
                  };
                case 23:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 22, 0
                  };
                case 24:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 0
                  };
              }
            }
            else
            {
              switch (level)
              {
                case 0:
                case 1:
                case 2:
                  return new double[]{
                          2, 2, 0, 0, 5, 0, 1 + (long) (itemLevel / 10), 0, 0, 0, 0
                  };
                case 3:
                  return new double[]{
                          2, 2, 0, 0, 10, 0, 1 + (long) (itemLevel / 10), 0, 0, 0, 0
                  };
                case 4:
                  return new double[]{
                          2, 2, 0, 0, 10, 0, 1 + (long) (itemLevel / 10), 0, 0, 0, 0
                  };
                case 5:
                  return new double[]{
                          3, 3, 0, 0, 15, 0, 2 + (long) (itemLevel / 10), 0, 0, 0, 0
                  };
                case 6:
                case 7:
                  return new double[]{
                          3, 3, 0, 0, 15, 0, 2 + (long) (itemLevel / 10), 0, 0, 0, 0
                  };
                case 8:
                case 9:
                  return new double[]{
                          3, 3, 0, 0, 20, 0, 3 + (long) (itemLevel / 10), 0, 0, 0, 0
                  };
                case 10:
                  return new double[]{
                          3, 3, 0, 0, 20, 0, 4 + (long) (itemLevel / 10), 0, 0, 0, 0
                  };
                case 11:
                case 12:
                  return new double[]{
                          3, 3, 0, 0, 25, 0, 4 + (long) (itemLevel / 10), 0, 0, 0, 0
                  };
                case 13:
                case 14:
                  return new double[]{
                          3, 3, 0, 0, 25, 0, 5 + (long) (itemLevel / 10), 0, 0, 0, 0
                  };
                case 15:
                case 16:
                  return new double[]{
                          7 + (long) (itemLevel / 10), 7 + (long) (itemLevel / 10), 0, 0, 30, 0, 6 + (long) (itemLevel / 10), 0, 0, 2 + (long) (itemLevel / 10), 0
                  };
                case 17:
                  return new double[]{
                          7 + (long) (itemLevel / 10), 7 + (long) (itemLevel / 10), 0, 0, 30, 0, 7 + (long) (itemLevel / 10), 0, 0, 3 + (long) (itemLevel / 10), 0
                  };
                case 18:
                  return new double[]{
                          7 + (long) (itemLevel / 10), 7 + (long) (itemLevel / 10), 0, 0, 35, 0, 7 + (long) (itemLevel / 10), 0, 0, 4 + (long) (itemLevel / 10), 0
                  };
                case 19:
                  return new double[]{
                          7 + (long) (itemLevel / 10), 7 + (long) (itemLevel / 10), 0, 0, 35, 0, 7 + (long) (itemLevel / 10), 0, 0, 5 + (long) (itemLevel / 10), 0
                  };
                case 20:
                  return new double[]{
                          7 + (long) (itemLevel / 10), 7 + (long) (itemLevel / 10), 0, 0, 40, 0, 7 + (long) (itemLevel / 10), 0, 0, 6 + (long) (itemLevel / 10), 0
                  };
                case 21:
                  return new double[]{
                          7 + (long) (itemLevel / 10), 7 + (long) (itemLevel / 10), 0, 0, 40, 0, 7 + (long) (itemLevel / 10), 0, 0, 8 + (long) (itemLevel / 10), 0
                  };
                case 22:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 19 + (long) (itemLevel / 10), 0
                  };
                case 23:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 22 + (long) (itemLevel / 10), 0
                  };
                case 24:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 25 + (long) (itemLevel / 10), 0
                  };
              }
            }
            break;
          case SHIELD:
            if (itemLevel < 10)
            {
              switch (level)
              {
                case 0:
                case 1:
                case 2:
                  return new double[]{
                          2, 2, 0, 0, 5, 0, 1, 0, 0, 0, 0
                  };
                case 3:
                  return new double[]{
                          2, 2, 0, 0, 10, 0, 0, 0, 1, 0, 0
                  };
                case 4:
                  return new double[]{
                          2, 2, 0, 0, 10, 0, 1, 0, 0, 1, 1
                  };
                case 5:
                  return new double[]{
                          3, 3, 0, 0, 15, 0, 2, 0, 0, 0, 0
                  };
                case 6:
                  return new double[]{
                          3, 3, 0, 0, 15, 0, 2, 0, 0, 1, 1
                  };
                case 7:
                  return new double[]{
                          3, 3, 0, 0, 15, 0, 2, 0, 0, 0, 0
                  };
                case 8:
                  return new double[]{
                          3, 3, 0, 0, 20, 0, 3, 0, 0, 1, 1
                  };
                case 9:
                  return new double[]{
                          3, 3, 0, 0, 20, 0, 3, 0, 0, 0, 0
                  };
                case 10:
                  return new double[]{
                          3, 3, 0, 0, 20, 0, 4, 0, 0, 1, 1
                  };
                case 11:
                  return new double[]{
                          3, 3, 0, 0, 25, 0, 4, 0, 0, 0, 0
                  };
                case 12:
                  return new double[]{
                          3, 3, 0, 0, 25, 0, 4, 0, 0, 1, 1
                  };
                case 13:
                case 14:
                  return new double[]{
                          3, 3, 0, 0, 25, 0, 5, 0, 0, 1, 1
                  };
                case 15:
                case 16:
                  return new double[]{
                          7, 7, 0, 0, 30, 0, 6, 0, 0, 2, 0
                  };
                case 17:
                  return new double[]{
                          7, 7, 0, 0, 30, 0, 7, 0, 0, 3, 0
                  };
                case 18:
                  return new double[]{
                          7, 7, 0, 0, 35, 0, 7, 0, 0, 4, 0
                  };
                case 19:
                  return new double[]{
                          7, 7, 0, 0, 35, 0, 7, 0, 0, 5, 0
                  };
                case 20:
                  return new double[]{
                          7, 7, 0, 0, 40, 0, 7, 0, 0, 6, 0
                  };
                case 21:
                  return new double[]{
                          7, 7, 0, 0, 40, 0, 7, 0, 0, 8, 0
                  };
                case 22:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 19, 0
                  };
                case 23:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 22, 0
                  };
                case 24:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 0
                  };
              }
            }
            else
            {
              switch (level)
              {
                case 0:
                case 1:
                case 2:
                  return new double[]{
                          2, 2, 0, 0, 5, 0, 1 + (long) (itemLevel / 10), 0, 0, 0, 0
                  };
                case 3:
                  return new double[]{
                          2, 2, 0, 0, 10, 0, 1 + (long) (itemLevel / 10), 0, 0, 0, 0
                  };
                case 4:
                  return new double[]{
                          2, 2, 0, 0, 10, 0, 1 + (long) (itemLevel / 10), 0, 0, 1, 1
                  };
                case 5:
                  return new double[]{
                          3, 3, 0, 0, 15, 0, 2 + (long) (itemLevel / 10), 0, 0, 0, 0
                  };
                case 6:
                  return new double[]{
                          3, 3, 0, 0, 15, 0, 2 + (long) (itemLevel / 10), 0, 0, 1, 1
                  };
                case 7:
                  return new double[]{
                          3, 3, 0, 0, 15, 0, 2 + (long) (itemLevel / 10), 0, 0, 0, 0
                  };
                case 8:
                  return new double[]{
                          3, 3, 0, 0, 20, 0, 3 + (long) (itemLevel / 10), 0, 0, 1, 1
                  };
                case 9:
                  return new double[]{
                          3, 3, 0, 0, 20, 0, 3 + (long) (itemLevel / 10), 0, 0, 0, 0
                  };
                case 10:
                  return new double[]{
                          3, 3, 0, 0, 20, 0, 4 + (long) (itemLevel / 10), 0, 0, 1, 1
                  };
                case 11:
                  return new double[]{
                          3, 3, 0, 0, 25, 0, 4 + (long) (itemLevel / 10), 0, 0, 0, 0
                  };
                case 12:
                  return new double[]{
                          3, 3, 0, 0, 25, 0, 4 + (long) (itemLevel / 10), 0, 0, 1, 1
                  };
                case 13:
                case 14:
                  return new double[]{
                          3, 3, 0, 0, 25, 0, 5 + (long) (itemLevel / 10), 0, 0, 1, 1
                  };
                case 15:
                case 16:
                  return new double[]{
                          7 + (long) (itemLevel / 10), 7 + (long) (itemLevel / 10), 0, 0, 30, 0, 6 + (long) (itemLevel / 10), 0, 0, 2 + (long) (itemLevel / 10), 0
                  };
                case 17:
                  return new double[]{
                          7 + (long) (itemLevel / 10), 7 + (long) (itemLevel / 10), 0, 0, 30, 0, 7 + (long) (itemLevel / 10), 0, 0, 3 + (long) (itemLevel / 10), 0
                  };
                case 18:
                  return new double[]{
                          7 + (long) (itemLevel / 10), 7 + (long) (itemLevel / 10), 0, 0, 35, 0, 7 + (long) (itemLevel / 10), 0, 0, 4 + (long) (itemLevel / 10), 0
                  };
                case 19:
                  return new double[]{
                          7 + (long) (itemLevel / 10), 7 + (long) (itemLevel / 10), 0, 0, 35, 0, 7 + (long) (itemLevel / 10), 0, 0, 5 + (long) (itemLevel / 10), 0
                  };
                case 20:
                  return new double[]{
                          7 + (long) (itemLevel / 10), 7 + (long) (itemLevel / 10), 0, 0, 40, 0, 7 + (long) (itemLevel / 10), 0, 0, 6 + (long) (itemLevel / 10), 0
                  };
                case 21:
                  return new double[]{
                          7 + (long) (itemLevel / 10), 7 + (long) (itemLevel / 10), 0, 0, 40, 0, 7 + (long) (itemLevel / 10), 0, 0, 8 + (long) (itemLevel / 10), 0
                  };
                case 22:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 19 + (long) (itemLevel / 10), 0
                  };
                case 23:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 22 + (long) (itemLevel / 10), 0
                  };
                case 24:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 25 + (long) (itemLevel / 10), 0
                  };
              }
            }
            break;
          default:
            break;
        }
        break;
      case SUPERIOR:
        return new double[]{
                ((long) (itemLevel / 15D) + 1) * ((long) (level) + 2), ((long) (itemLevel / 15D) + 1) * ((long) (level) + 2), ((long) (itemLevel / 15D) + 1) * ((long) (level) + 2),
                ((long) (itemLevel / 15D) + 1) * ((long) (level) + 2), ((long) (itemLevel / 3D) + 1) * (level + 2), ((long) (itemLevel / 3D) + 1) * (level + 2),
                ((long) (itemLevel / 3D) + 1) * ((long) (level * level / 5D) + 5), ((long) (itemLevel / 10D) + 1) * ((long) (level / 3D) + 1), ((long) (itemLevel / 10D) + 1) * ((long) (level / 3D) + 1),
                ((long) (itemLevel / 5D) + 1) * ((long) (level / 5D)), ((long) (itemLevel / 5D) + 1) * ((long) (level / 5D))
        };
      default:
        return null;
    }
    return null;
  }

  @SuppressWarnings("unused")
  protected static double[] calculate(double[] values, double itemRank)
  {
    for (int i = 0; i < values.length; i++)
    {
      values[i] *= itemRank;
    }
    return values;
  }

  protected static List<String> convertOptions(double[] options, boolean success)
  {
    List<String> list = new ArrayList<>();
    if (success)
    {
      for (int i = 0; i < options.length; i++)
      {
        if (options[i] != 0)
        {
          list.add(OPTION.get(i) + " : +" + Constant.Sosu2rawFormat.format(options[i]));
        }
      }
      return list;
    }
    for (int i = 0; i < options.length; i++)
    {
      if (options[i] != 0)
      {
        list.add(OPTION.get(i) + " : -" + Constant.Sosu2rawFormat.format(options[i]));
      }
    }
    return list;
  }

  protected static void sendChanceMessage(Player player, double success, double failKeep, double failDown, double destroy)
  {
    if (failDown > 0 && destroy == 0 && failKeep > 0)
    {
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, CommandReinforceConstantsAndUtils.MAY_DROP);
    }
    else if (failDown > 0 && destroy == 0 && failKeep == 0)
    {
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, CommandReinforceConstantsAndUtils.WILL_DROP);
    }
    else if (failDown > 0 && destroy > 0 && failKeep > 0)
    {
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, CommandReinforceConstantsAndUtils.MAY_DESTROY_OR_DROP);
    }
    else if (failDown > 0 && destroy > 0 && failKeep == 0)
    {
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, CommandReinforceConstantsAndUtils.WILL_DESTROY_OR_DROP);
    }
    else if (failDown == 0 && destroy > 0)
    {
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, CommandReinforceConstantsAndUtils.MAY_DESTROY);
    }
    else if (success + destroy == 100)
    {
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, CommandReinforceConstantsAndUtils.WILL_DESTROY);
    }
    MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, CommandReinforceConstantsAndUtils.REINFORCE_SUCCESS_CHANCE, Constant.THE_COLOR_HEX + df2.format(success) + "%");
    if (failKeep > 0)
    {
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, CommandReinforceConstantsAndUtils.REINFORCE_FAILURE_KEEP_CHANCE, Constant.THE_COLOR_HEX + df2.format(failKeep) + "%");
    }
    if (failDown > 0)
    {
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, CommandReinforceConstantsAndUtils.REINFORCE_FAILURE_DROP_CHANCE, Constant.THE_COLOR_HEX + df2.format(failDown) + "%");
    }
    if (destroy > 0)
    {
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, CommandReinforceConstantsAndUtils.REINFORCE_DESTRUCTION_DROP_CHANCE, Constant.THE_COLOR_HEX + df2.format(destroy) + "%");
    }
  }

  protected static void successTitle(Player player)
  {
    Location loc = player.getLocation().add(player.getLocation().getDirection().multiply(4D)).add(0D, 1.7D, 0D);
    player.spawnParticle(Particle.FIREWORKS_SPARK, loc, 500, 0, 0, 0, 1);
    MessageUtil.sendTitle(player, "rg255,204;&n★", CommandReinforceConstantsAndUtils.MESSAGE_SUCCESS, 0, 10, 0);
    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () ->
    {
      MessageUtil.sendTitle(player, "rg255,204;&n★S", CommandReinforceConstantsAndUtils.MESSAGE_SUCCESS, 0, 10, 0);
    }, 1L);
    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () ->
    {
      MessageUtil.sendTitle(player, "rg255,204;&n★SU", CommandReinforceConstantsAndUtils.MESSAGE_SUCCESS, 0, 10, 0);
    }, 2L);
    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () ->
    {
      MessageUtil.sendTitle(player, "rg255,204;&n★SUC", CommandReinforceConstantsAndUtils.MESSAGE_SUCCESS, 0, 10, 0);
    }, 3L);
    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () ->
    {
      MessageUtil.sendTitle(player, "rg255,204;&n★SUCC", CommandReinforceConstantsAndUtils.MESSAGE_SUCCESS, 0, 10, 0);
    }, 4L);
    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () ->
    {
      MessageUtil.sendTitle(player, "rg255,204;&n★SUCCE", CommandReinforceConstantsAndUtils.MESSAGE_SUCCESS, 0, 10, 0);
    }, 5L);
    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () ->
    {
      MessageUtil.sendTitle(player, "rg255,204;&n★SUCCES", CommandReinforceConstantsAndUtils.MESSAGE_SUCCESS, 0, 10, 0);
    }, 6L);
    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () ->
    {
      MessageUtil.sendTitle(player, "rg255,204;&n★SUCCESS", CommandReinforceConstantsAndUtils.MESSAGE_SUCCESS, 0, 10, 0);
    }, 7L);
    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () ->
    {
      MessageUtil.sendTitle(player, "rg255,204;&n★SUCCESS★", CommandReinforceConstantsAndUtils.MESSAGE_SUCCESS, 0, 25, 10);
    }, 8L);
  }

  protected static void failTitle(Player player, boolean particle)
  {
    MessageUtil.sendTitle(player, "r255;&oFAILED", particle ? CommandReinforceConstantsAndUtils.MESSAGE_FAILURE_DROP : CommandReinforceConstantsAndUtils.MESSAGE_FAILURE, 0, 30, 15);
    if (particle)
    {
      Location loc = player.getLocation().add(player.getLocation().getDirection().multiply(4D)).add(0D, 1.7D, 0D);
      player.spawnParticle(Particle.ITEM_CRACK, loc, 10, 0, 0, 0, 0.3, player.getInventory().getItemInMainHand());
    }
  }

  protected static void destroyTitle(Player player)
  {
    MessageUtil.sendTitle(player, "&8&oDESTROYED", CommandReinforceConstantsAndUtils.MESSAGE_DESTROY, 0, 30, 15);
    Location loc = player.getLocation().add(player.getLocation().getDirection().multiply(4D)).add(0D, 1.7D, 0D);
    Bukkit.getServer().getWorld(loc.getWorld().getName()).spawnParticle(Particle.ITEM_CRACK, loc, 500, 0, 0, 0, 0.3, player.getInventory().getItemInMainHand());
  }

  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_REINFORCE, true))
    {
      return true;
    }
    if (!(sender instanceof Player))
    {
      MessageUtil.sendError(sender, Prefix.ONLY_PLAYER);
      return true;
    }
    Player player = (Player) sender;
    UUID uuid = player.getUniqueId();
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    String usage = cmd.getUsage().replace("/<command> ", "");
    if (args.length == 0)
    {
      MessageUtil.shortArg(sender, 1, args);
      MessageUtil.commandInfo(player, label, usage);
      return true;
    }
    else if (args.length == 1)
    {
      switch (args[0])
      {
        case "상태" ->
        {
          CommandReinforceConstantsAndUtils.operate(player, OperationType.OBSERVE);
        }
        case "quit" ->
        {
          if (REINFORCE_OPERATING.contains(uuid))
          {
            MessageUtil.sendError(player, "강화중에는 강화를 중지할 수 없습니다");
            return true;
          }
          if (!Variable.scrollReinforcing.contains(uuid))
          {
            MessageUtil.sendError(player, "강화중인 상태가 아닙니다");
            return true;
          }
          if (CHANCE_TIME.contains(uuid))
          {
            CHANCE_TIME.remove(uuid);
            player.stopSound("reinforce_chancetime");
            for (int i = 0; i <= 20; i++)
            {
              Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () ->
              {
                MessageUtil.sendTitle(player, "", "", 0, 1, 0);
              }, i);
            }
          }
          Variable.scrollReinforcing.remove(uuid);
          MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화를 중지했습니다");
          return true;
        }
        case "시작" ->
        {
          Variable.scrollReinforcing.add(player.getUniqueId());
          CommandReinforceConstantsAndUtils.operate(player, OperationType.SHOW);
        }
        case "파괴방지사용" ->
        {
          if (REINFORCE_OPERATING.contains(uuid))
          {
            MessageUtil.sendError(player, "강화중에는 할 수 없습니다");
            return true;
          }
          if (CustomEffectManager.hasEffect(player, CustomEffectTypeReinforce.ANTI_DESTRUCTION_DISABLED_WARNING))
          {
            MessageUtil.sendWarn(player, "소지 금액이 부족하여 파괴 방지가 해제되었습니다! 숨을 돌린 뒤 다시 시도해주세요!");
            return true;
          }
          if (!Variable.scrollReinforcing.contains(uuid))
          {
            MessageUtil.sendError(player, "강화중인 상태가 아닙니다");
            return true;
          }
          ANTI_DESTRUCTION_ENABLED.add(uuid);
          CommandReinforceConstantsAndUtils.operate(player, OperationType.CONTINUE);
        }
        case "파괴방지미사용" ->
        {
          if (REINFORCE_OPERATING.contains(uuid))
          {
            MessageUtil.sendError(player, "강화중에는 할 수 없습니다");
            return true;
          }
          if (!Variable.scrollReinforcing.contains(uuid))
          {
            MessageUtil.sendError(player, "강화중인 상태가 아닙니다");
            return true;
          }
          ANTI_DESTRUCTION_ENABLED.remove(uuid);
          CommandReinforceConstantsAndUtils.operate(player, OperationType.CONTINUE);
        }
        case "스타캐치해제사용" ->
        {
          if (REINFORCE_OPERATING.contains(uuid))
          {
            MessageUtil.sendError(player, "강화중에는 할 수 없습니다");
            return true;
          }
          if (!Variable.scrollReinforcing.contains(uuid))
          {
            MessageUtil.sendError(player, "강화중인 상태가 아닙니다");
            return true;
          }
          NO_STAR_CATCH_ENABLED.add(uuid);
          CommandReinforceConstantsAndUtils.operate(player, OperationType.CONTINUE);
        }
        case "스타캐치해제미사용" ->
        {
          if (REINFORCE_OPERATING.contains(uuid))
          {
            MessageUtil.sendError(player, "강화중에는 할 수 없습니다");
            return true;
          }
          if (!Variable.scrollReinforcing.contains(uuid))
          {
            MessageUtil.sendError(player, "강화중인 상태가 아닙니다");
            return true;
          }
          NO_STAR_CATCH_ENABLED.remove(uuid);
          CommandReinforceConstantsAndUtils.operate(player, OperationType.CONTINUE);
        }
        case "realstart" ->
        {
          if (!Variable.scrollReinforcing.contains(uuid))
          {
            MessageUtil.sendError(player, "강화중인 상태가 아닙니다");
            return true;
          }
          if (CustomEffectManager.hasEffect(player, CustomEffectTypeReinforce.ANTI_DESTRUCTION_DISABLED_WARNING))
          {
            MessageUtil.sendWarn(player, "소지 금액이 부족하여 파괴 방지가 해제되었습니다! 숨을 돌린 뒤 다시 시도해주세요!");
            return true;
          }
          if (CHANCE_TIME.contains(uuid))
          {
            CHANCE_TIME.remove(uuid);
            player.stopSound("reinforce_chancetime");
            for (int i = 0; i <= 20; i++)
            {
              Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () ->
              {
                MessageUtil.sendTitle(player, "", "", 0, 1, 0);
              }, i);
            }
          }
          else if (!CHANCE_TIME.contains(uuid) && !NO_STAR_CATCH_ENABLED.contains(uuid) && !CustomEffectManager.hasEffect(player, CustomEffectTypeReinforce.STAR_CATCH_FINISHED))
          {
            if (!CustomEffectManager.hasEffect(player, CustomEffectTypeReinforce.STAR_CATCH_PREPARE) &&
                    !CustomEffectManager.hasEffect(player, CustomEffectTypeReinforce.STAR_CATCH_PROCESS) &&
                    !CustomEffectManager.hasEffect(player, CustomEffectTypeReinforce.STAR_CATCH_SUCCESS))
            {
              CustomEffectManager.addEffect(player, CustomEffectTypeReinforce.STAR_CATCH_PREPARE);
              REINFORCE_OPERATING.add(uuid);
              return true;
            }
            MessageUtil.sendWarn(player, "스타캐치 강화를 시도하고 있습니다");
            return true;
          }
          CommandReinforceConstantsAndUtils.operate(player, OperationType.OPERATE);
        }
        case "starcatch" ->
        {
          if (CustomEffectManager.hasEffect(player, CustomEffectTypeReinforce.STAR_CATCH_PROCESS))
          {
            CustomEffect customEffect = CustomEffectManager.getEffect(player, CustomEffectTypeReinforce.STAR_CATCH_PROCESS);
            int duration = customEffect.getDuration();
            boolean ok = false;
            Integer penalty = Variable.starCatchPenalty.get(player.getUniqueId());
            if (penalty == null)
            {
              penalty = 0;
            }
            int level = Math.min(4, penalty / 20);
            Outter:
            for (int i = 1; i <= 10; i++)
            {
              switch (level)
              {
                case 0 ->
                {
                  if (duration >= i * 20 - 14 && duration <= i * 20 - 6)
                  {
                    ok = true;
                    break Outter;
                  }
                }
                case 1 ->
                {
                  if (duration >= i * 20 - 13 && duration <= i * 20 - 7)
                  {
                    ok = true;
                    break Outter;
                  }
                }
                case 2 ->
                {
                  if (duration >= i * 20 - 11 && duration <= i * 20 - 8)
                  {
                    ok = true;
                    break Outter;
                  }
                }
                case 3 ->
                {
                  if (duration >= i * 20 - 10 && duration <= i * 20 - 9)
                  {
                    ok = true;
                    break Outter;
                  }
                }
                default ->
                {
                  if (duration == i * 20 - 10)
                  {
                    ok = true;
                    break Outter;
                  }
                }
              }
            }
            if (ok)
            {
              CustomEffectManager.addEffect(player, CustomEffectTypeReinforce.STAR_CATCH_SUCCESS);
              player.playSound(player.getLocation(), "star_catch_success", SoundCategory.PLAYERS, 2F, 1F);
              player.playSound(player.getLocation(), "star_catch_success", SoundCategory.PLAYERS, 2F, 1F);
              Consumer<Entity> consumer = e ->
              {
                ArmorStand armorStand = (ArmorStand) e;
                armorStand.setMarker(true);
                armorStand.setSmall(true);
                armorStand.setBasePlate(false);
                armorStand.setInvisible(true);
                armorStand.customName(ComponentUtil.translate("&a+강화성공률"));
                armorStand.setCustomNameVisible(true);
                armorStand.addScoreboardTag("damage_indicator");
                armorStand.addScoreboardTag("no_cucumbery_true_invisibility");
                for (Player p : Bukkit.getOnlinePlayers())
                {
                  if (player != p)
                  {
                    p.hideEntity(Cucumbery.getPlugin(), armorStand);
                  }
                }
              };
              Location location = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(3));
              Entity armorStand = location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND, SpawnReason.DEFAULT, consumer);
              CustomEffectManager.addEffect(armorStand, CustomEffectType.DAMAGE_INDICATOR);
            }
            else
            {
              player.playSound(player.getLocation(), "star_catch_failure", SoundCategory.PLAYERS, 1F, 1F);
            }
            CustomEffectManager.removeEffect(player, CustomEffectTypeReinforce.STAR_CATCH_PROCESS);
          }
        }
        default ->
        {
          MessageUtil.wrongArg(sender, 1, args);
          MessageUtil.info(player, "/" + label + usage);
          return true;
        }
      }
    }
    else
    {
      MessageUtil.longArg(sender, 1, args);
      MessageUtil.info(player, "/" + label + usage);
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
      return Method.tabCompleterList(args, "<인수>", "시작", "상태");
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
    if (args.length == 1)
    {
      return CommandTabUtil.tabCompleterList(args, "<인수>", false, "시작", "상태");
    }
    return CommandTabUtil.ARGS_LONG;
  }

  enum ReinforceType
  {
    STARFORCE,
    SUPERIOR,
    CUCUMBERFORCE,
    EBEBEB
  }

  enum ItemType
  {
    SWORD,
    BOW,
    HELMET,
    CHESTPLATE,
    LEGGINGS,
    BOOTS,
    SHIELD,
    ETC
  }
}
package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.rpg.GetStat;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.*;

@SuppressWarnings("all")
public class Reinforce implements CommandExecutor
{
  private static final boolean 확률주작 = false;
  private static final HashMap<Player, Boolean> antiDest = new HashMap<>();
  private static final int ANTI_DESTRUCTION_STARFORCE_FROM = 12, ANTI_DESTRUCTION_STARFORCE_TO = 16, ANTI_DESTRUCTION_CUCUMBERFORCE_FROM = 24, ANTI_DESTRUCTION_CUCUMBERFORCE_TO = 33;
  private static final List<String> OPTION = Arrays.asList("STR", "DEX", "INT", "LUK", "최대 HP", "최대 MP", "방어력", "이동속도", "점프력", "공격력", "마력");
  public static List<Player> chanceTime = new ArrayList<>();
  public static double[] starforceCostRatio = {
          1D, // 0 -> 1
          2.13D, // 1 -> 2
          3.14D, // 2 -> 3
          4.15D, // 3 -> 4
          5.16D, // 4 -> 5
          6.17D, // 5 -> 6
          7.18D, // 6 -> 7
          8.19D, // 7 -> 8
          9.21D, // 8 -> 9
          10.25D, // 9 -> 10
          80.3D, // 10 -> 11
          100.4D, // 11 -> 12
          120.5D, // 12 -> 13
          140.6D, // 13 -> 14
          170.7D, // 14 -> 15
          200.8D, // 15 -> 16
          230.9D, // 16 -> 17
          281.1D, // 17 -> 18
          331.3D, // 18 -> 19
          381.5D, // 19 -> 20
          451.7D, // 20 -> 21
          521.9D, // 21 -> 22
          602.4D, // 22 -> 23
          800.3D, // 23 -> 24
          1000.34D, // 24 -> 25
          0D
  };
  private static DecimalFormat df2 = new DecimalFormat("#,###.##"), df3 = new DecimalFormat("#,###.######"), round = new DecimalFormat("####.##");
  private List<UUID> players = new ArrayList<UUID>();

  private static double[] getStarForceInfo(int amount)
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
                45D, 0D, 55D, 0D
        };
        break;
      case 12:
        if (확률주작)
        {
          info = new double[]{
                  40D, 0D, 0.6D, 59.4D
          };
        }
        else
        {
          info = new double[]{
                  40D, 0D, 59.4D, 0.6D
          };
        }
        break;
      case 13:
        info = new double[]{
                35D, 0D, 63.7D, 1.3D
        };
        break;
      case 14:
        info = new double[]{
                30D, 0D, 68.6D, 1.4D
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

  private static double[] getSuperiorInfo(int amount)
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

  private static double[] getCucumberyInfo(int amount)
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

  private static double[] getEBEBEBInfo(int amount)
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

  public static double getCost(int level, double reinforceRank, ReinforceType type)
  {
    switch (type)
    {
      case CUCUMBERFORCE:
        return Math.pow(reinforceRank, 0.9D) * 2D * Math.pow(level + 1D, 1.8D) * Math.pow(1.1D, level);
      case EBEBEB:
        return Math.pow(reinforceRank, 0.8D) * 4D * Math.pow(level + 1D, 4D) * Math.pow(1.3D, level);
      case STARFORCE:
        return reinforceRank * 5D * Math.pow(1.001D, level + 1) * starforceCostRatio[level];
      case SUPERIOR:
        return Math.pow(reinforceRank, 1.5D) * 1000D;
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
      Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&e★10성+★", "&f10성을 달성했습니다!\n&f스타포스 강화에 실패하여 강화 단계가\n&f하락하더라도 10성 밑으로는 하락하지 않습니다.");
      player.sendMessage(msg);
    }
    else if (amount >= 15 && amount < 20)
    {
      Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&b★★15성+★★", "&f15성을 달성했습니다!\n&f스타포스 강화에 실패하여 강화 단계가\n&f하락하더라도 15성 밑으로는 하락하지 않습니다.");
      player.sendMessage(msg);
    }
    else if (amount >= 20 && amount < 25)
    {
      Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&d★☆★20성+★☆★", "&f20성을 달성했습니다!\n&f스타포스 강화에 실패하여 강화 단계가\n&f하락하더라도 20성 밑으로는 하락하지 않습니다.");
      player.sendMessage(msg);
    }
    else if (amount == 25)
    {
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&4★&c☆&6★&e☆&a★&22&b5&3성&9★&1☆&5★&d☆&4★");
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
      Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&e★20성+★", "&f20성을 달성했습니다!\n&f오이포스 강화에 실패하여 강화 단계가\n&f하락하더라도 20성 밑으로는 하락하지 않습니다.");
      player.sendMessage(msg);
    }
    else if (amount >= 30 && amount < 40)
    {
      Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&b★★30성+★★", "&f30성을 달성했습니다!\n&f오이포스 강화에 실패하여 강화 단계가\n&f하락하더라도 30성 밑으로는 하락하지 않습니다.");
      player.sendMessage(msg);
    }
    else if (amount >= 40 && amount < 50)
    {
      Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&d★☆★40성+★☆★", "&f40성을 달성했습니다!\n&f오이포스 강화에 실패하여 강화 단계가\n&f하락하더라도 40성 밑으로는 하락하지 않습니다.");
      player.sendMessage(msg);
    }
    else if (amount == 50)
    {
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&4★&c☆&6★&e☆&a★&25&b0&3성&9★&1☆&5★&d☆&4★");
    }
    sendChanceMessage(player, success, failKeep, failDown, destroy);
  }

  public static void checkEBEBEBReinforce(Player player, int amount)
  {
    double[] chance = getEBEBEBInfo(amount);
    double success = chance[0], failKeep = chance[1], failDown = chance[2], destroy = chance[3];
    sendChanceMessage(player, success, failKeep, failDown, destroy);
  }

  public static double[] getOptions(ReinforceType type, ItemType itemType, int level, double itemRank)
  {
    if (itemType == null)
    {
      itemType = ItemType.ETC;
    }
    switch (type)
    {
      case CUCUMBERFORCE:
        return new double[]{
                ((long) (itemRank / 100D) + 1) * ((long) (level / 5D) + 1), ((long) (itemRank / 100D) + 1) * ((long) (level / 5D) + 1), ((long) (itemRank / 100D) + 1) * ((long) (level / 5D) + 1),
                ((long) (itemRank / 100D) + 1) * ((long) (level / 5D) + 1), ((long) (itemRank / 30D) + 1) * (level + 1), ((long) (itemRank / 30D) + 1) * (level + 1),
                ((long) (itemRank / 10D) + 1) * ((long) (level * level / 20D) + 1), ((long) (itemRank / 100D) + 1) * ((long) (level / 20D) + 1), ((long) (itemRank / 100D) + 1) * ((long) (level / 20D) + 1),
                ((long) (itemRank / 50D) + 1) * ((long) (level / 30D) + 1), ((long) (itemRank / 50D) + 1) * ((long) (level / 30D) + 1)
        };
      case EBEBEB:
        return new double[]{
                ((long) (itemRank / 10D) + 1) * ((long) (level) + 1), ((long) (itemRank / 10D) + 1) * ((long) (level) + 1), ((long) (itemRank / 10D) + 1) * ((long) (level) + 1),
                ((long) (itemRank / 10D) + 1) * ((long) (level) + 1), ((long) (itemRank / 3D) + 1) * (level + 1), ((long) (itemRank / 3D) + 1) * (level + 1),
                ((long) (itemRank / 1D) + 1) * ((long) (level * level / 5D) + 1), ((long) (itemRank / 10D) + 1) * ((long) (level / 2D) + 1), ((long) (itemRank / 10D) + 1) * ((long) (level / 2D) + 1),
                ((long) (itemRank / 5D) + 1) * ((long) (level / 3D) + 1), ((long) (itemRank / 5D) + 1) * ((long) (level / 3D) + 1)
        };
      case STARFORCE:
        switch (itemType)
        {
          case BOOTS:
            if (itemRank < 10)
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
                          2, 2, 0, 0, 5, 0, 1 + (long) (itemRank / 10), 0, 0, 0, 0
                  };
                case 3:
                  return new double[]{
                          2, 2, 0, 0, 10, 0, 1 + (long) (itemRank / 10), 1, 1, 0, 0
                  };
                case 4:
                  return new double[]{
                          2, 2, 0, 0, 10, 0, 1 + (long) (itemRank / 10), 0, 0, 0, 0
                  };
                case 5:
                  return new double[]{
                          3, 3, 0, 0, 15, 0, 2 + (long) (itemRank / 10), 1, 1, 0, 0
                  };
                case 6:
                case 7:
                  return new double[]{
                          3, 3, 0, 0, 15, 0, 2 + (long) (itemRank / 10), 0, 0, 0, 0
                  };
                case 8:
                case 9:
                  return new double[]{
                          3, 3, 0, 0, 20, 0, 3 + (long) (itemRank / 10), 0, 0, 0, 0
                  };
                case 10:
                  return new double[]{
                          3, 3, 0, 0, 20, 0, 4 + (long) (itemRank / 10), 1, 1, 0, 0
                  };
                case 11:
                case 12:
                  return new double[]{
                          3, 3, 0, 0, 25, 0, 4 + (long) (itemRank / 10), 0, 0, 0, 0
                  };
                case 13:
                case 14:
                  return new double[]{
                          3, 3, 0, 0, 25, 0, 5 + (long) (itemRank / 10), 1, 1, 0, 0
                  };
                case 15:
                case 16:
                  return new double[]{
                          7 + (long) (itemRank / 10), 7 + (long) (itemRank / 10), 0, 0, 30, 0, 6 + (long) (itemRank / 10), 1, 1, 2 + (long) (itemRank / 10), 0
                  };
                case 17:
                  return new double[]{
                          7 + (long) (itemRank / 10), 7 + (long) (itemRank / 10), 0, 0, 30, 0, 7 + (long) (itemRank / 10), 1, 1, 3 + (long) (itemRank / 10), 0
                  };
                case 18:
                  return new double[]{
                          7 + (long) (itemRank / 10), 7 + (long) (itemRank / 10), 0, 0, 35, 0, 7 + (long) (itemRank / 10), 1, 1, 4 + (long) (itemRank / 10), 0
                  };
                case 19:
                  return new double[]{
                          7 + (long) (itemRank / 10), 7 + (long) (itemRank / 10), 0, 0, 35, 0, 7 + (long) (itemRank / 10), 1, 1, 5 + (long) (itemRank / 10), 0
                  };
                case 20:
                  return new double[]{
                          7 + (long) (itemRank / 10), 7 + (long) (itemRank / 10), 0, 0, 40, 0, 7 + (long) (itemRank / 10), 1, 1, 6 + (long) (itemRank / 10), 0
                  };
                case 21:
                  return new double[]{
                          7 + (long) (itemRank / 10), 7 + (long) (itemRank / 10), 0, 0, 40, 0, 7 + (long) (itemRank / 10), 1, 1, 8 + (long) (itemRank / 10), 0
                  };
                case 22:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 19 + (long) (itemRank / 10), 0
                  };
                case 23:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 22 + (long) (itemRank / 10), 0
                  };
                case 24:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 25 + (long) (itemRank / 10), 0
                  };
              }
            }
            break;
          case BOW:
          case SWORD:
            if (itemRank < 10)
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
                          2, 2, 0, 0, 5, 5, 0, 0, 0, 1 + (long) (itemRank / 10D), 0
                  };
                case 3:
                case 4:
                  return new double[]{
                          2, 2, 0, 0, 10, 10, 0, 0, 0, 1 + (long) (itemRank / 10D), 0
                  };
                case 5:
                case 6:
                case 7:
                  return new double[]{
                          3, 3, 0, 0, 10, 10, 0, 0, 0, 2 + (long) (itemRank / 10D), 0
                  };
                case 8:
                case 9:
                  return new double[]{
                          3, 3, 0, 0, 15, 15, 0, 0, 0, 2 + (long) (itemRank / 10D), 0
                  };
                case 10:
                  return new double[]{
                          3, 3, 0, 0, 15, 15, 0, 0, 0, 3 + (long) (itemRank / 10D), 0
                  };
                case 11:
                case 12:
                case 13:
                  return new double[]{
                          3, 3, 0, 0, 20, 20, 0, 0, 0, 3 + (long) (itemRank / 10D), 0
                  };
                case 14:
                  return new double[]{
                          3, 3, 0, 0, 25, 25, 0, 0, 0, 4 + (long) (itemRank / 10D), 0
                  };
                case 15:
                case 16:
                  return new double[]{
                          7 + (long) (itemRank / 10D), 7 + (long) (itemRank / 10D), 0, 0, 30, 30, 0, 0, 0, 5 + (long) (itemRank / 10D), 0
                  };
                case 17:
                  return new double[]{
                          7 + (long) (itemRank / 10D), 7 + (long) (itemRank / 10D), 0, 0, 30, 30, 0, 0, 0, 6 + (long) (itemRank / 10D), 0
                  };
                case 18:
                  return new double[]{
                          7 + (long) (itemRank / 10D), 7 + (long) (itemRank / 10D), 0, 0, 35, 35, 0, 0, 0, 7 + (long) (itemRank / 10D), 0
                  };
                case 19:
                  return new double[]{
                          7 + (long) (itemRank / 10D), 7 + (long) (itemRank / 10D), 0, 0, 35, 35, 0, 0, 0, 8 + (long) (itemRank / 10D), 0
                  };
                case 20:
                  return new double[]{
                          7 + (long) (itemRank / 10D), 7 + (long) (itemRank / 10D), 0, 0, 35, 35, 0, 0, 0, 9 + (long) (itemRank / 10D), 0
                  };
                case 21:
                  return new double[]{
                          7 + (long) (itemRank / 10D), 7 + (long) (itemRank / 10D), 0, 0, 40, 40, 0, 0, 0, 11 + (long) (itemRank / 10D), 0
                  };
                case 22:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 31 + (long) (itemRank / 10D), 0
                  };
                case 23:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 33 + (long) (itemRank / 10D), 0
                  };
                case 24:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 35 + (long) (itemRank / 10D), 0
                  };
              }
            }
            break;
          case ETC:
            if (itemRank < 10)
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
                          2, 2, 2, 2, 5, 0, 2 + (long) (itemRank / 10D), 0, 0, 0, 0
                  };
                case 1:
                case 2:
                  return new double[]{
                          2, 2, 2, 2, 5, 0, 2 + (long) (itemRank / 10D), 0, 0, 0, 0
                  };
                case 3:
                case 4:
                  return new double[]{
                          2, 2, 2, 2, 10, 0, 2 + (long) (itemRank / 10D), 0, 0, 0, 0
                  };
                case 5:
                case 6:
                case 7:
                  return new double[]{
                          3, 3, 3, 3, 15, 0, 3 + (long) (itemRank / 10D), 0, 0, 0, 0
                  };
                case 8:
                case 9:
                case 10:
                  return new double[]{
                          3, 3, 3, 3, 20, 0, 4 + (long) (itemRank / 10D), 0, 0, 0, 0
                  };
                case 11:
                case 12:
                case 13:
                  return new double[]{
                          3, 3, 3, 3, 25, 0, 5 + (long) (itemRank / 10D), 0, 0, 0, 0
                  };
                case 14:
                  return new double[]{
                          3, 3, 3, 3, 30, 0, 6 + (long) (itemRank / 10D), 0, 0, 0, 0
                  };
                case 15:
                case 16:
                  return new double[]{
                          7 + (long) (itemRank / 10D), 7 + (long) (itemRank / 10D), 7 + (long) (itemRank / 10D), 7 + (long) (itemRank / 10D), 35, 0, 7 + (long) (itemRank / 10D), 0, 0,
                          2 + (long) (itemRank / 10D), 2 + (long) (itemRank / 10D)
                  };
                case 17:
                  return new double[]{
                          7 + (long) (itemRank / 10D), 7 + (long) (itemRank / 10D), 7 + (long) (itemRank / 10D), 7 + (long) (itemRank / 10D), 35, 0, 8 + (long) (itemRank / 10D), 0, 0,
                          3 + (long) (itemRank / 10D), 3 + (long) (itemRank / 10D)
                  };
                case 18:
                  return new double[]{
                          7 + (long) (itemRank / 10D), 7 + (long) (itemRank / 10D), 7 + (long) (itemRank / 10D), 7 + (long) (itemRank / 10D), 40, 0, 9 + (long) (itemRank / 10D), 0, 0,
                          4 + (long) (itemRank / 10D), 4 + (long) (itemRank / 10D)
                  };
                case 19:
                  return new double[]{
                          7 + (long) (itemRank / 10D), 7 + (long) (itemRank / 10D), 7 + (long) (itemRank / 10D), 7 + (long) (itemRank / 10D), 40, 0, 10 + (long) (itemRank / 10D), 0, 0,
                          5 + (long) (itemRank / 10D), 5 + (long) (itemRank / 10D)
                  };
                case 20:
                  return new double[]{
                          7 + (long) (itemRank / 10D), 7 + (long) (itemRank / 10D), 7 + (long) (itemRank / 10D), 7 + (long) (itemRank / 10D), 40, 0, 11 + (long) (itemRank / 10D), 0, 0,
                          6 + (long) (itemRank / 10D), 6 + (long) (itemRank / 10D)
                  };
                case 21:
                  return new double[]{
                          7 + (long) (itemRank / 10D), 7 + (long) (itemRank / 10D), 7 + (long) (itemRank / 10D), 7 + (long) (itemRank / 10D), 45, 0, 12 + (long) (itemRank / 10D), 0, 0,
                          8 + (long) (itemRank / 10D), 8 + (long) (itemRank / 10D)
                  };
                case 22:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 27 + (long) (itemRank / 10D), 27 + (long) (itemRank / 10D)
                  };
                case 23:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 29 + (long) (itemRank / 10D), 29 + (long) (itemRank / 10D)
                  };
                case 24:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 31 + (long) (itemRank / 10D), 31 + (long) (itemRank / 10D)
                  };
              }
            }
            break;
          case HELMET:
          case CHESTPLATE:
          case LEGGINGS:
            if (itemRank < 10)
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
                          2, 2, 0, 0, 5, 0, 1 + (long) (itemRank / 10), 0, 0, 0, 0
                  };
                case 3:
                  return new double[]{
                          2, 2, 0, 0, 10, 0, 1 + (long) (itemRank / 10), 0, 0, 0, 0
                  };
                case 4:
                  return new double[]{
                          2, 2, 0, 0, 10, 0, 1 + (long) (itemRank / 10), 0, 0, 0, 0
                  };
                case 5:
                  return new double[]{
                          3, 3, 0, 0, 15, 0, 2 + (long) (itemRank / 10), 0, 0, 0, 0
                  };
                case 6:
                case 7:
                  return new double[]{
                          3, 3, 0, 0, 15, 0, 2 + (long) (itemRank / 10), 0, 0, 0, 0
                  };
                case 8:
                case 9:
                  return new double[]{
                          3, 3, 0, 0, 20, 0, 3 + (long) (itemRank / 10), 0, 0, 0, 0
                  };
                case 10:
                  return new double[]{
                          3, 3, 0, 0, 20, 0, 4 + (long) (itemRank / 10), 0, 0, 0, 0
                  };
                case 11:
                case 12:
                  return new double[]{
                          3, 3, 0, 0, 25, 0, 4 + (long) (itemRank / 10), 0, 0, 0, 0
                  };
                case 13:
                case 14:
                  return new double[]{
                          3, 3, 0, 0, 25, 0, 5 + (long) (itemRank / 10), 0, 0, 0, 0
                  };
                case 15:
                case 16:
                  return new double[]{
                          7 + (long) (itemRank / 10), 7 + (long) (itemRank / 10), 0, 0, 30, 0, 6 + (long) (itemRank / 10), 0, 0, 2 + (long) (itemRank / 10), 0
                  };
                case 17:
                  return new double[]{
                          7 + (long) (itemRank / 10), 7 + (long) (itemRank / 10), 0, 0, 30, 0, 7 + (long) (itemRank / 10), 0, 0, 3 + (long) (itemRank / 10), 0
                  };
                case 18:
                  return new double[]{
                          7 + (long) (itemRank / 10), 7 + (long) (itemRank / 10), 0, 0, 35, 0, 7 + (long) (itemRank / 10), 0, 0, 4 + (long) (itemRank / 10), 0
                  };
                case 19:
                  return new double[]{
                          7 + (long) (itemRank / 10), 7 + (long) (itemRank / 10), 0, 0, 35, 0, 7 + (long) (itemRank / 10), 0, 0, 5 + (long) (itemRank / 10), 0
                  };
                case 20:
                  return new double[]{
                          7 + (long) (itemRank / 10), 7 + (long) (itemRank / 10), 0, 0, 40, 0, 7 + (long) (itemRank / 10), 0, 0, 6 + (long) (itemRank / 10), 0
                  };
                case 21:
                  return new double[]{
                          7 + (long) (itemRank / 10), 7 + (long) (itemRank / 10), 0, 0, 40, 0, 7 + (long) (itemRank / 10), 0, 0, 8 + (long) (itemRank / 10), 0
                  };
                case 22:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 19 + (long) (itemRank / 10), 0
                  };
                case 23:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 22 + (long) (itemRank / 10), 0
                  };
                case 24:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 25 + (long) (itemRank / 10), 0
                  };
              }
            }
            break;
          case SHIELD:
            if (itemRank < 10)
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
                          2, 2, 0, 0, 5, 0, 1 + (long) (itemRank / 10), 0, 0, 0, 0
                  };
                case 3:
                  return new double[]{
                          2, 2, 0, 0, 10, 0, 1 + (long) (itemRank / 10), 0, 0, 0, 0
                  };
                case 4:
                  return new double[]{
                          2, 2, 0, 0, 10, 0, 1 + (long) (itemRank / 10), 0, 0, 1, 1
                  };
                case 5:
                  return new double[]{
                          3, 3, 0, 0, 15, 0, 2 + (long) (itemRank / 10), 0, 0, 0, 0
                  };
                case 6:
                  return new double[]{
                          3, 3, 0, 0, 15, 0, 2 + (long) (itemRank / 10), 0, 0, 1, 1
                  };
                case 7:
                  return new double[]{
                          3, 3, 0, 0, 15, 0, 2 + (long) (itemRank / 10), 0, 0, 0, 0
                  };
                case 8:
                  return new double[]{
                          3, 3, 0, 0, 20, 0, 3 + (long) (itemRank / 10), 0, 0, 1, 1
                  };
                case 9:
                  return new double[]{
                          3, 3, 0, 0, 20, 0, 3 + (long) (itemRank / 10), 0, 0, 0, 0
                  };
                case 10:
                  return new double[]{
                          3, 3, 0, 0, 20, 0, 4 + (long) (itemRank / 10), 0, 0, 1, 1
                  };
                case 11:
                  return new double[]{
                          3, 3, 0, 0, 25, 0, 4 + (long) (itemRank / 10), 0, 0, 0, 0
                  };
                case 12:
                  return new double[]{
                          3, 3, 0, 0, 25, 0, 4 + (long) (itemRank / 10), 0, 0, 1, 1
                  };
                case 13:
                case 14:
                  return new double[]{
                          3, 3, 0, 0, 25, 0, 5 + (long) (itemRank / 10), 0, 0, 1, 1
                  };
                case 15:
                case 16:
                  return new double[]{
                          7 + (long) (itemRank / 10), 7 + (long) (itemRank / 10), 0, 0, 30, 0, 6 + (long) (itemRank / 10), 0, 0, 2 + (long) (itemRank / 10), 0
                  };
                case 17:
                  return new double[]{
                          7 + (long) (itemRank / 10), 7 + (long) (itemRank / 10), 0, 0, 30, 0, 7 + (long) (itemRank / 10), 0, 0, 3 + (long) (itemRank / 10), 0
                  };
                case 18:
                  return new double[]{
                          7 + (long) (itemRank / 10), 7 + (long) (itemRank / 10), 0, 0, 35, 0, 7 + (long) (itemRank / 10), 0, 0, 4 + (long) (itemRank / 10), 0
                  };
                case 19:
                  return new double[]{
                          7 + (long) (itemRank / 10), 7 + (long) (itemRank / 10), 0, 0, 35, 0, 7 + (long) (itemRank / 10), 0, 0, 5 + (long) (itemRank / 10), 0
                  };
                case 20:
                  return new double[]{
                          7 + (long) (itemRank / 10), 7 + (long) (itemRank / 10), 0, 0, 40, 0, 7 + (long) (itemRank / 10), 0, 0, 6 + (long) (itemRank / 10), 0
                  };
                case 21:
                  return new double[]{
                          7 + (long) (itemRank / 10), 7 + (long) (itemRank / 10), 0, 0, 40, 0, 7 + (long) (itemRank / 10), 0, 0, 8 + (long) (itemRank / 10), 0
                  };
                case 22:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 19 + (long) (itemRank / 10), 0
                  };
                case 23:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 22 + (long) (itemRank / 10), 0
                  };
                case 24:
                  return new double[]{
                          0, 0, 0, 0, 0, 0, 0, 0, 0, 25 + (long) (itemRank / 10), 0
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
                ((long) (itemRank / 15D) + 1) * ((long) (level) + 2), ((long) (itemRank / 15D) + 1) * ((long) (level) + 2), ((long) (itemRank / 15D) + 1) * ((long) (level) + 2),
                ((long) (itemRank / 15D) + 1) * ((long) (level) + 2), ((long) (itemRank / 3D) + 1) * (level + 2), ((long) (itemRank / 3D) + 1) * (level + 2),
                ((long) (itemRank / 3D) + 1) * ((long) (level * level / 5D) + 5), ((long) (itemRank / 10D) + 1) * ((long) (level / 3D) + 1), ((long) (itemRank / 10D) + 1) * ((long) (level / 3D) + 1),
                ((long) (itemRank / 5D) + 1) * ((long) (level / 5D)), ((long) (itemRank / 5D) + 1) * ((long) (level / 5D))
        };
      default:
        return null;
    }
    return null;
  }

  @SuppressWarnings("unused")
  private static double[] calculate(double[] values, double itemRank)
  {
    for (int i = 0; i < values.length; i++)
    {
      values[i] *= itemRank;
    }
    return values;
  }

  private static List<String> convertOptions(double[] options, boolean success)
  {
    List<String> list = new ArrayList<>();
    if (success)
    {
      for (int i = 0; i < options.length; i++)
      {
        if (options[i] != 0)
        {
          list.add(OPTION.get(i) + " : +" + Constant.Sosu2.format(options[i]));
        }
      }
      return list;
    }

    for (int i = 0; i < options.length; i++)
    {
      if (options[i] != 0)
      {
        list.add(OPTION.get(i) + " : -" + Constant.Sosu2.format(options[i]));
      }
    }
    return list;
  }

  private static void sendChanceMessage(Player player, double success, double failKeep, double failDown, double destroy)
  {
    if (failDown > 0 && destroy == 0 && failKeep > 0)
    {
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&c실패시 &6강화 단계&c가 &4하락&c할 수 있습니다.");
    }
    else if (failDown > 0 && destroy == 0 && failKeep == 0)
    {
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&c실패 시 &6강화 단계&c가 &4하락&c됩니다.");
    }
    else if (failDown > 0 && destroy > 0 && failKeep > 0)
    {
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&c실패 시 아이템이 &4파괴&c되거나 &6단계&c가 &4하락&c될 수 있습니다.");
    }
    else if (failDown > 0 && destroy > 0 && failKeep == 0)
    {
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&c실패 시 아이템이 &4파괴&c되거나 &6단계&c가 &4하락&c됩니다.");
    }
    else if (failDown == 0 && destroy > 0)
    {
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&c실패 시 아이템이 &4파괴&c될 수 있습니다.");
    }
    else if (success + destroy == 100)
    {
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&c실패 시 아이템이 &4파괴&c됩니다.");
    }
    MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화 성공 확률 : &e" + df2.format(success) + "%");
    if (failKeep > 0)
    {
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화 실패(유지) 확률 : &e" + df2.format(failKeep) + "%");
    }
    if (failDown > 0)
    {
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화 실패(하락) 확률 : &e" + df2.format(failDown) + "%");
    }
    if (destroy > 0)
    {
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "파괴 확률 : &e" + df2.format(destroy) + "%");
    }
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
      if (args[0].equalsIgnoreCase("quit"))
      {
        if (players.contains(player.getUniqueId()))
        {
          MessageUtil.sendError(player, "강화중에는 강화를 중지할 수 없습니다.");
          return true;
        }
        if (!Variable.scrollReinforcing.contains(player.getUniqueId()))
        {
          MessageUtil.sendError(player, "강화중인 상태가 아닙니다.");
          return true;
        }
        if (chanceTime.contains(player))
        {
          chanceTime.remove(player);
          player.stopSound("reinforce_chancetime");
          for (int i = 0; i <= 20; i++)
          {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () ->
            {
              MessageUtil.sendTitle(player, "", "", 0, 1, 0);
            }, i);
          }
        }
        Variable.scrollReinforcing.remove(player.getUniqueId());
        antiDest.put(player, false);
        MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화를 중지하였습니다.");
        return true;
      }
      if (!ItemStackUtil.itemExists(player.getInventory().getItemInMainHand()))
      {
        MessageUtil.sendError(player, Prefix.NO_HOLDING_ITEM);
        return true;
      }
      ItemStack item = player.getInventory().getItemInMainHand();
      boolean isStarforceItem = false, isSuperiorItem = false, isCucumberForceItem = false, isEBEBEBItem = false;
      int current = 0, max = 0;
      long reinforcePoint = 0L;
      ReinforceType type = null;
      ItemType itemType = null;
      double reinforceRank = 1D;
      boolean downTwice = false;
      int downCount = 0;
      if (item.hasItemMeta() && item.getItemMeta().hasLore())
      {
        for (int i = 0; i < item.getItemMeta().getLore().size(); i++)
        {
          String lore = MessageUtil.stripColor(item.getItemMeta().getLore().get(i));
          if (lore.contains("강화 포인트 : "))
          {
            try
            {
              reinforcePoint = Long.parseLong(lore.split("강화 포인트 : ")[1].split(" / ")[0]);
            }
            catch (Exception e)
            {
              reinforcePoint = 0L;
            }
          }
          if (lore.contains("장비 분류 : "))
          {
            String itemName = lore.split("장비 분류 : ")[1];
            switch (itemName)
            {
              case "근거리 무기 - 검":
              case "검":
                itemType = ItemType.SWORD;
                break;
              case "원거리 무기 - 활":
              case "활":
                itemType = ItemType.BOW;
                break;
              case "투구":
                itemType = ItemType.HELMET;
                break;
              case "흉갑":
                itemType = ItemType.CHESTPLATE;
                break;
              case "각반":
                itemType = ItemType.LEGGINGS;
                break;
              case "부츠":
                itemType = ItemType.BOOTS;
                break;
              case "방패":
                itemType = ItemType.SHIELD;
                break;
            }
          }
          if (lore.contains("아이템 강화 등급 : "))
          {
            try
            {
              reinforceRank = Double.parseDouble(lore.split("아이템 강화 등급 : ")[1]);
            }
            catch (Exception e)
            {
              reinforceRank = 1D;
            }
          }
          if (lore.contains("연속 하락 횟수 : "))
          {
            try
            {
              downCount = Integer.parseInt(lore.split("연속 하락 횟수 : ")[1]);

              if (downCount == 2)
              {
                downTwice = true;
              }
            }
            catch (Exception e)
            {
              downCount = 0;
              downTwice = false;
            }
          }
          if (lore.contains("스타포스 : "))
          {
            String[] cut = lore.split("스타포스 : ");
            String a = cut[1];
            String[] cut2 = a.split(" / ");
            try
            {
              current = Integer.parseInt(cut2[0]);
              max = Integer.parseInt(cut2[1]);
              isStarforceItem = true;
              type = ReinforceType.STARFORCE;
            }
            catch (NumberFormatException exception)
            {
              isStarforceItem = false;
            }
          }
          else if (lore.contains("슈페리얼 : "))
          {
            String[] cut = lore.split("슈페리얼 : ");
            String a = cut[1];
            String[] cut2 = a.split(" / ");
            try
            {
              current = Integer.parseInt(cut2[0]);
              max = Integer.parseInt(cut2[1]);
              isSuperiorItem = true;
              type = ReinforceType.SUPERIOR;
            }
            catch (NumberFormatException exception)
            {
              isSuperiorItem = false;
            }
          }
          else if (lore.contains("오이포스 : "))
          {
            String[] cut = lore.split("오이포스 : ");
            String a = cut[1];
            String[] cut2 = a.split(" / ");
            try
            {
              current = Integer.parseInt(cut2[0]);
              max = Integer.parseInt(cut2[1]);
              isCucumberForceItem = true;
              type = ReinforceType.CUCUMBERFORCE;
            }
            catch (NumberFormatException exception)
            {
              isCucumberForceItem = false;
            }
          }
          else if (lore.contains("에베벱 : "))
          {
            String[] cut = lore.split("에베벱 : ");
            String a = cut[1];
            String[] cut2 = a.split(" / ");
            try
            {
              current = Integer.parseInt(cut2[0]);
              max = Integer.parseInt(cut2[1]);
              isEBEBEBItem = true;
              type = ReinforceType.EBEBEB;
            }
            catch (NumberFormatException exception)
            {
              isEBEBEBItem = false;
            }
          }
        }
      }
      if (!isCucumberForceItem && !isEBEBEBItem && !isStarforceItem && !isSuperiorItem)
      {
        MessageUtil.sendError(player, "강화를 할 수 없는 아이템입니다.");
        return true;
      }
      int reinforceCost = (int) Math.round(getCost(current, reinforceRank, type));
      if (args[0].equalsIgnoreCase("상태"))
      {
        if (isStarforceItem)
        {
          MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "스타포스 : &b" + current + "&e / &b" + max);
          if (current >= max)
          {
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "최대 강화수치입니다.");
            return true;
          }
          else
          {
            if (downTwice)
            {
              if (current >= 10 && current < 15)
              {
                Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&e★10성+★", "&f10성을 달성했습니다!\n&f스타포스 강화에 실패하여 강화 단계가\n&f하락하더라도 10성 밑으로는 하락하지 않습니다.");
                player.sendMessage(msg);
              }
              else if (current >= 15 && current < 20)
              {
                Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&b★★15성+★★", "&f15성을 달성했습니다!\n&f스타포스 강화에 실패하여 강화 단계가\n&f하락하더라도 15성 밑으로는 하락하지 않습니다.");
                player.sendMessage(msg);
              }
              else if (current >= 20 && current < 25)
              {
                Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&d★☆★20성+★☆★", "&f20성을 달성했습니다!\n&f스타포스 강화에 실패하여 강화 단계가\n&f하락하더라도 20성 밑으로는 하락하지 않습니다.");
                player.sendMessage(msg);
              }
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&e연속 2회 하락 CHANCE TIME!");
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화 성공 확률 : &e" + 100 + "%");
            }
            else
            {
              checkReinforce(player, current);
            }
            List<String> options = convertOptions(getOptions(type, itemType, current, reinforceRank), true);
            for (String option : options)
            {
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, option);
            }
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "현재 강화 포인트 : &e" + reinforcePoint + "&f, 요구 강화 포인트 : &e" + reinforceCost);
            if (reinforceCost > reinforcePoint)
            {
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&c강화 포인트가 부족합니다.");
            }
          }
        }
        else if (isSuperiorItem)
        {
          MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "슈페리얼 : &b" + current + "&e / &b" + max);
          if (current >= max)
          {
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "최대 강화수치입니다.");
            return true;
          }
          else
          {
            if (downTwice)
            {
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&e연속 2회 하락 CHANCE TIME!");
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화 성공 확률 : &e" + 100 + "%");
            }
            else
            {
              checkSuperiorReinforce(player, current);
            }
            List<String> options = convertOptions(getOptions(type, itemType, current, reinforceRank), true);
            for (String option : options)
            {
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, option);
            }
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "현재 강화 포인트 : &e" + reinforcePoint + "&f, 요구 강화 포인트 : &e" + reinforceCost);
            if (reinforceCost > reinforcePoint)
            {
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&c강화 포인트가 부족합니다.");
            }
          }
        }
        else if (isCucumberForceItem)
        {
          MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "오이포스 : &b" + current + "&e / &b" + max);
          if (current >= max)
          {
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "최대 강화수치입니다.");
            return true;
          }
          else
          {
            if (downTwice)
            {
              if (current >= 20 && current < 30)
              {
                Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&e★20성+★", "&f20성을 달성했습니다!\n&f오이포스 강화에 실패하여 강화 단계가\n&f하락하더라도 20성 밑으로는 하락하지 않습니다.");

                player.sendMessage(msg);
              }
              else if (current >= 30 && current < 40)
              {
                Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&b★★30성+★★", "&f30성을 달성했습니다!\n&f오이포스 강화에 실패하여 강화 단계가\n&f하락하더라도 30성 밑으로는 하락하지 않습니다.");

                player.sendMessage(msg);
              }
              else if (current >= 40 && current < 50)
              {
                Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&d★☆★40성+★☆★", "&f40성을 달성했습니다!\n&f오이포스 강화에 실패하여 강화 단계가\n&f하락하더라도 40성 밑으로는 하락하지 않습니다.");

                player.sendMessage(msg);
              }
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&e연속 2회 하락 CHANCE TIME!");
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화 성공 확률 : &e" + 100 + "%");
            }
            else
            {
              checkCucumberForceReinforce(player, current);
            }
            List<String> options = convertOptions(getOptions(type, itemType, current, reinforceRank), true);
            for (String option : options)
            {
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, option);
            }
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "현재 강화 포인트 : &e" + reinforcePoint + "&f, 요구 강화 포인트 : &e" + reinforceCost);

            if (reinforceCost > reinforcePoint)
            {
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&c강화 포인트가 부족합니다.");
            }
          }
        }
        else if (isEBEBEBItem)
        {
          MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "에베벱 : &b" + current + "&e / &b" + max);
          if (current >= max)
          {
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "최대 강화수치입니다.");
            return true;
          }
          else
          {
            if (downTwice)
            {
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&e연속 2회 하락 CHANCE TIME!");
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화 성공 확률 : &e" + 100 + "%");
            }
            else
            {
              checkEBEBEBReinforce(player, current);
            }
            List<String> options = convertOptions(getOptions(type, itemType, current, reinforceRank), true);
            for (String option : options)
            {
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, option);
            }
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "현재 강화 포인트 : &e" + reinforcePoint + "&f, 요구 강화 포인트 : &e" + reinforceCost);
            if (reinforceCost > reinforcePoint)
            {
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&c강화 포인트가 부족합니다.");
            }
          }
        }
        else
        {
          MessageUtil.sendError(player, "강화를 할 수 없는 아이템입니다.");
          return true;
        }
      }
      else if (args[0].equalsIgnoreCase("시작"))
      {
        if (isStarforceItem || isSuperiorItem || isCucumberForceItem || isEBEBEBItem)
        {
          if (current >= max)
          {
            MessageUtil.sendError(player, "이미 한계까지 강화되어 더 이상 강화할 수 없습니다.");
            return true;
          }
          if (reinforceCost > reinforcePoint)
          {
            MessageUtil.sendError(player, "강화 포인트가 부족합니다. 현재 강화 포인트 : &e" + reinforcePoint + "&f, 요구 강화 포인트 : &e" + reinforceCost);
            return true;
          }
          Variable.scrollReinforcing.add(player.getUniqueId());
          MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "----------------------------------------------------------------------------------------------");
          MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "현재 강화 포인트 : &e" + reinforcePoint + "&f, 요구 강화 포인트 : &e" + reinforceCost);
          MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "" + current + "성 >> " + (current + 1) + "성");
          if (isStarforceItem)
          {
            if (확률주작)
            {
              Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&e★10성+★", "&f10성을 달성했습니다!\n&f스타포스 강화에 실패하여 강화 단계가\n&f하락하더라도 10성 밑으로는 하락하지 않습니다.");
              player.sendMessage(msg);
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&c실패 시 아이템이 &4파괴&c되거나 &6단계&c가 &4하락&c됩니다.");
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화 성공 확률 : &e" + 40 + "%");
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화 실패(하락) 확률 : &e" + 59.4 + "%");
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "파괴 확률 : &e" + 0.6 + "%");
            }
            else
            {
              if (downTwice)
              {
                if (current >= 10 && current < 15)
                {
                  Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&e★10성+★", "&f10성을 달성했습니다!\n&f스타포스 강화에 실패하여 강화 단계가\n&f하락하더라도 10성 밑으로는 하락하지 않습니다.");
                  player.sendMessage(msg);
                }
                else if (current >= 15 && current < 20)
                {
                  Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&b★★15성+★★", "&f15성을 달성했습니다!\n&f스타포스 강화에 실패하여 강화 단계가\n&f하락하더라도 15성 밑으로는 하락하지 않습니다.");

                  player.sendMessage(msg);
                }
                else if (current >= 20 && current < 25)
                {
                  Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&d★☆★20성+★☆★", "&f20성을 달성했습니다!\n&f스타포스 강화에 실패하여 강화 단계가\n&f하락하더라도 20성 밑으로는 하락하지 않습니다.");
                  player.sendMessage(msg);
                }
                MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&e연속 2회 하락 CHANCE TIME!");
                MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화 성공 확률 : &e" + 100 + "%");
                if (!chanceTime.contains(player))
                {
                  chanceTime.add(player);
                }
              }
              else if (antiDest != null && antiDest.containsKey(player) && antiDest.get(player))
              {
                double[] chance = getStarForceInfo(current);
                double success = chance[0], failKeep = chance[1], failDown = chance[2], destroy = chance[3];
                if (failDown != 0 && failKeep == 0)
                {
                  failDown += destroy;
                }
                else if (failKeep != 0 && failDown == 0)
                {
                  failKeep += destroy;
                }
                else
                {
                  failKeep += destroy / 2D;
                  failDown += destroy / 2D;
                }
                destroy = 0D;
                if (current >= 10 && current < 15)
                {
                  Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&e★10성+★", "&f10성을 달성했습니다!\n&f스타포스 강화에 실패하여 강화 단계가\n&f하락하더라도 10성 밑으로는 하락하지 않습니다.");
                  player.sendMessage(msg);
                }
                else if (current >= 15 && current < 20)
                {
                  Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&b★★15성+★★", "&f15성을 달성했습니다!\n&f스타포스 강화에 실패하여 강화 단계가\n&f하락하더라도 15성 밑으로는 하락하지 않습니다.");
                  player.sendMessage(msg);
                }
                else if (current >= 20 && current < 25)
                {
                  Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&d★☆★20성+★☆★", "&f20성을 달성했습니다!\n&f스타포스 강화에 실패하여 강화 단계가\n&f하락하더라도 20성 밑으로는 하락하지 않습니다.");
                  player.sendMessage(msg);
                }
                else if (current == 25)
                {
                  MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&4★&c☆&6★&e☆&a★&22&b5&3성&9★&1☆&5★&d☆&4★");
                }
                sendChanceMessage(player, success, failKeep, failDown, destroy);
              }
              else
              {
                checkReinforce(player, current);
              }
            }
            type = ReinforceType.STARFORCE;
          }
          else if (isSuperiorItem)
          {
            if (downTwice)
            {
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&e연속 2회 하락 CHANCE TIME!");
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화 성공 확률 : &e" + 100 + "%");
              if (!chanceTime.contains(player))
              {
                chanceTime.add(player);
              }
            }
            else
            {
              checkSuperiorReinforce(player, current);
            }
            type = ReinforceType.SUPERIOR;
          }
          else if (isCucumberForceItem)
          {
            if (downTwice)
            {
              if (current >= 20 && current < 30)
              {
                Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&e★20성+★", "&f20성을 달성했습니다!\n&f오이포스 강화에 실패하여 강화 단계가\n&f하락하더라도 20성 밑으로는 하락하지 않습니다.");
                player.sendMessage(msg);
              }
              else if (current >= 30 && current < 40)
              {
                Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&b★★30성+★★", "&f30성을 달성했습니다!\n&f오이포스 강화에 실패하여 강화 단계가\n&f하락하더라도 30성 밑으로는 하락하지 않습니다.");
                player.sendMessage(msg);
              }
              else if (current >= 40 && current < 50)
              {
                Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&d★☆★40성+★☆★", "&f40성을 달성했습니다!\n&f오이포스 강화에 실패하여 강화 단계가\n&f하락하더라도 40성 밑으로는 하락하지 않습니다.");
                player.sendMessage(msg);
              }
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&e연속 2회 하락 CHANCE TIME!");
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화 성공 확률 : &e" + 100 + "%");
              if (!chanceTime.contains(player))
              {
                chanceTime.add(player);
              }
            }
            else if (antiDest != null && antiDest.containsKey(player) && antiDest.get(player))
            {
              double[] chance = getCucumberyInfo(current);
              double success = chance[0], failKeep = chance[1], failDown = chance[2], destroy = chance[3];
              if (failDown != 0 && failKeep == 0)
              {
                failDown += destroy;
              }
              else if (failKeep != 0 && failDown == 0)
              {
                failKeep += destroy;
              }
              else
              {
                failKeep += destroy / 2D;
                failDown += destroy / 2D;
              }
              destroy = 0D;
              if (current >= 20 && current < 30)
              {
                Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&e★20성+★", "&f20성을 달성했습니다!\n&f오이포스 강화에 실패하여 강화 단계가\n&f하락하더라도 20성 밑으로는 하락하지 않습니다.");
                player.sendMessage(msg);
              }
              else if (current >= 30 && current < 40)
              {
                Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&b★★30성+★★", "&f30성을 달성했습니다!\n&f오이포스 강화에 실패하여 강화 단계가\n&f하락하더라도 30성 밑으로는 하락하지 않습니다.");
                player.sendMessage(msg);
              }
              else if (current >= 40 && current < 50)
              {
                Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&d★☆★40성+★☆★", "&f40성을 달성했습니다!\n&f오이포스 강화에 실패하여 강화 단계가\n&f하락하더라도 40성 밑으로는 하락하지 않습니다.");
                player.sendMessage(msg);
              }
              else if (current == 50)
              {
                MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&4★&c☆&6★&e☆&a★&25&b0&3성&9★&1☆&5★&d☆&4★");
              }
              sendChanceMessage(player, success, failKeep, failDown, destroy);
            }
            else
            {
              checkCucumberForceReinforce(player, current);
            }
            type = ReinforceType.CUCUMBERFORCE;
          }
          else if (isEBEBEBItem)
          {
            if (downTwice)
            {
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&e연속 2회 하락 CHANCE TIME!");
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화 성공 확률 : &e" + 100 + "%");
              if (!chanceTime.contains(player))
              {
                chanceTime.add(player);
              }
            }
            else
            {
              checkEBEBEBReinforce(player, current);
            }
            type = ReinforceType.EBEBEB;
          }
          List<String> options = convertOptions(getOptions(type, itemType, current, reinforceRank), true);
          for (String option : options)
          {
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, option);
          }
          Component a = ComponentUtil.create("&9[&3강&e화&9] &f강화를 시작하려면 ");
          Component b = ComponentUtil.create("&a\"강화 시작\"", "&f클릭하면 강화를 시작합니다.\n&c이 작업은 되돌릴 수 없습니다.", ClickEvent.Action.RUN_COMMAND, Constant.REINFORCE_START);
          Component c = ComponentUtil.create("&f을, 강화를 중지하려면 ");
          Component d = ComponentUtil.create("&c\"강화 중지\"", "&f클릭하면 강화를 중지합니다.", ClickEvent.Action.RUN_COMMAND, Constant.REINFORCE_QUIT);
          if ((type == ReinforceType.STARFORCE && current >= ANTI_DESTRUCTION_STARFORCE_FROM && current <= ANTI_DESTRUCTION_STARFORCE_TO) || (type == ReinforceType.CUCUMBERFORCE && current >= ANTI_DESTRUCTION_CUCUMBERFORCE_FROM && current <= ANTI_DESTRUCTION_CUCUMBERFORCE_TO))
          {
            if (antiDest == null || !antiDest.containsKey(player) || !antiDest.get(player))
            {
              Component e = ComponentUtil.create("&f를 채팅창을 열고 클릭해주세요. 파괴 방지를 사용하려면 ");
              Component f = ComponentUtil.create("&e\"여기\"", "&f클릭하면 파괴 방지를 사용합니다.", ClickEvent.Action.RUN_COMMAND, Constant.REINFORCE_USE_ANTI_DESTRUCTION);
              Component g = ComponentUtil.create("&f를 클릭하세요.");
              if (type == ReinforceType.STARFORCE)
              {
                f = ComponentUtil.create("&e\"여기\"", "&f클릭하면 파괴 방지를 사용합니다.\n&f파괴 방지 기능은 12성에서 16성 사이의 아이템으로\n&f강화를 시도할때만 사용할 수 있습니다.", ClickEvent.Action.RUN_COMMAND, Constant.REINFORCE_USE_ANTI_DESTRUCTION);
              }
              else if (type == ReinforceType.CUCUMBERFORCE)
              {
                f = ComponentUtil.create("&e\"여기\"", "&f클릭하면 파괴 방지를 사용합니다.\n&f파괴 방지 기능은 24성에서 33성 사이의 아이템으로\n&f강화를 시도할때만 사용할 수 있습니다.", ClickEvent.Action.RUN_COMMAND,
                        Constant.REINFORCE_USE_ANTI_DESTRUCTION);
              }
              MessageUtil.sendMessage(player, a, b, c, d, e, f, g);
            }
            else
            {
              Component e = ComponentUtil.create("&f를 채팅창을 열고 클릭해주세요. 파괴 방지를 사용하지 않으려면 ");
              Component f = ComponentUtil.create("&b\"여기\"", "&f클릭하면 파괴 방지를 사용하지 않습니다.", ClickEvent.Action.RUN_COMMAND, Constant.REINFORCE_DO_NOT_USE_ANTI_DESTRUCTION);
              Component g = ComponentUtil.create("&f를 클릭하세요.");
              MessageUtil.sendMessage(player, a, b, c, d, e, f, g);
            }
          }
          else
          {
            Component e = ComponentUtil.create("&f를 채팅창을 열고 클릭해주세요.");
            MessageUtil.sendMessage(player, a, b, c, d, e);
          }
        }
        else
        {
          MessageUtil.sendError(player, "강화를 할 수 없는 아이템입니다.");
          return true;
        }
      }
      else if (args[0].equalsIgnoreCase("파괴방지사용"))
      {
        if (!((type == ReinforceType.STARFORCE && current >= ANTI_DESTRUCTION_STARFORCE_FROM && current <= ANTI_DESTRUCTION_STARFORCE_TO) || (type == ReinforceType.CUCUMBERFORCE && current >= ANTI_DESTRUCTION_CUCUMBERFORCE_FROM && current <= ANTI_DESTRUCTION_CUCUMBERFORCE_TO)))
        {
          return true;
        }
        if (!Variable.scrollReinforcing.contains(player.getUniqueId()))
        {
          return true;
        }
        if (players.contains(player.getUniqueId()))
        {
          return true;
        }
        if (antiDest != null && antiDest.containsKey(player) && antiDest.get(player))
        {
          return true;
        }
        reinforceCost = (int) Math.round(getCost(current, reinforceRank, type)) * 2;
        if (reinforceCost > reinforcePoint)
        {
          MessageUtil.sendError(player, "강화 포인트가 부족합니다. 현재 강화 포인트 : &e" + reinforcePoint + "&f, 요구 강화 포인트 : &e" + reinforceCost);
          player.performCommand("강화 파괴방지미사용");
          return true;
        }
        antiDest.put(player, true);
        Variable.scrollReinforcing.add(player.getUniqueId());
        MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "----------------------------------------------------------------------------------------------");
        MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "현재 강화 포인트 : &e" + reinforcePoint + "&f, 요구 강화 포인트 : &e" + reinforceCost);
        MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "" + current + "성 >> " + (current + 1) + "성");
        if (isStarforceItem)
        {
          if (확률주작)
          {
            Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&e★10성+★", "&f10성을 달성했습니다!\n&f스타포스 강화에 실패하여 강화 단계가\n&f하락하더라도 10성 밑으로는 하락하지 않습니다.");
            player.sendMessage(msg);
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&c실패 시 아이템이 &4파괴&c되거나 &6단계&c가 &4하락&c됩니다.");
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화 성공 확률 : &e" + 30 + "%");
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화 실패(하락) 확률 : &e" + 69.3 + "%");
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "파괴 확률 : &e" + 0.7 + "%");
          }
          else
          {
            if (downTwice)
            {
              if (current >= 10 && current < 15)
              {
                Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&e★10성+★", "&f10성을 달성했습니다!\n&f스타포스 강화에 실패하여 강화 단계가\n&f하락하더라도 10성 밑으로는 하락하지 않습니다.");
                player.sendMessage(msg);
              }
              else if (current >= 15 && current < 20)
              {
                Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&b★★15성+★★", "&f15성을 달성했습니다!\n&f스타포스 강화에 실패하여 강화 단계가\n&f하락하더라도 15성 밑으로는 하락하지 않습니다.");
                player.sendMessage(msg);
              }
              else if (current >= 20 && current < 25)
              {
                Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&d★☆★20성+★☆★", "&f20성을 달성했습니다!\n&f스타포스 강화에 실패하여 강화 단계가\n&f하락하더라도 20성 밑으로는 하락하지 않습니다.");
                player.sendMessage(msg);
              }
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&e연속 2회 하락 CHANCE TIME!");
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화 성공 확률 : &e" + 100 + "%");
              if (!chanceTime.contains(player))
              {
                chanceTime.add(player);
              }
            }
            else if (antiDest != null && antiDest.containsKey(player) && antiDest.get(player))
            {
              double[] chance = getStarForceInfo(current);
              double success = chance[0], failKeep = chance[1], failDown = chance[2], destroy = chance[3];
              if (failDown != 0 && failKeep == 0)
              {
                failDown += destroy;
              }
              else if (failKeep != 0 && failDown == 0)
              {
                failKeep += destroy;
              }
              else
              {
                failKeep += destroy / 2D;
                failDown += destroy / 2D;
              }
              destroy = 0D;
              if (current >= 10 && current < 15)
              {
                Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&e★10성+★", "&f10성을 달성했습니다!\n&f스타포스 강화에 실패하여 강화 단계가\n&f하락하더라도 10성 밑으로는 하락하지 않습니다.");
                player.sendMessage(msg);
              }
              else if (current >= 15 && current < 20)
              {
                Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&b★★15성+★★", "&f15성을 달성했습니다!\n&f스타포스 강화에 실패하여 강화 단계가\n&f하락하더라도 15성 밑으로는 하락하지 않습니다.");
                player.sendMessage(msg);
              }
              else if (current >= 20 && current < 25)
              {
                Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&d★☆★20성+★☆★", "&f20성을 달성했습니다!\n&f스타포스 강화에 실패하여 강화 단계가\n&f하락하더라도 20성 밑으로는 하락하지 않습니다.");
                player.sendMessage(msg);
              }
              else if (current == 25)
              {
                MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&4★&c☆&6★&e☆&a★&22&b5&3성&9★&1☆&5★&d☆&4★");
              }
              sendChanceMessage(player, success, failKeep, failDown, destroy);
            }
            else
            {
              checkReinforce(player, current);
            }
          }
          type = ReinforceType.STARFORCE;
        }
        else if (isSuperiorItem)
        {
          if (downTwice)
          {
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&e연속 2회 하락 CHANCE TIME!");
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화 성공 확률 : &e" + 100 + "%");
            if (!chanceTime.contains(player))
            {
              chanceTime.add(player);
            }
          }
          else
          {
            checkSuperiorReinforce(player, current);
          }
          type = ReinforceType.SUPERIOR;
        }
        else if (isCucumberForceItem)
        {
          if (downTwice)
          {
            if (current >= 20 && current < 30)
            {
              Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&e★20성+★", "&f20성을 달성했습니다!\n&f오이포스 강화에 실패하여 강화 단계가\n&f하락하더라도 20성 밑으로는 하락하지 않습니다.");
              player.sendMessage(msg);
            }
            else if (current >= 30 && current < 40)
            {
              Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&b★★30성+★★", "&f30성을 달성했습니다!\n&f오이포스 강화에 실패하여 강화 단계가\n&f하락하더라도 30성 밑으로는 하락하지 않습니다.");
              player.sendMessage(msg);
            }
            else if (current >= 40 && current < 50)
            {
              Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&d★☆★40성+★☆★", "&f40성을 달성했습니다!\n&f오이포스 강화에 실패하여 강화 단계가\n&f하락하더라도 40성 밑으로는 하락하지 않습니다.");
              player.sendMessage(msg);
            }
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&e연속 2회 하락 CHANCE TIME!");
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화 성공 확률 : &e" + 100 + "%");
            if (!chanceTime.contains(player))
            {
              chanceTime.add(player);
            }
          }
          else if (antiDest != null && antiDest.containsKey(player) && antiDest.get(player))
          {
            double[] chance = getCucumberyInfo(current);
            double success = chance[0], failKeep = chance[1], failDown = chance[2], destroy = chance[3];
            if (failDown != 0 && failKeep == 0)
            {
              failDown += destroy;
            }
            else if (failKeep != 0 && failDown == 0)
            {
              failKeep += destroy;
            }
            else
            {
              failKeep += destroy / 2D;
              failDown += destroy / 2D;
            }
            destroy = 0D;
            if (current >= 20 && current < 30)
            {
              Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&e★20성+★", "&f20성을 달성했습니다!\n&f오이포스 강화에 실패하여 강화 단계가\n&f하락하더라도 20성 밑으로는 하락하지 않습니다.");
              player.sendMessage(msg);
            }
            else if (current >= 30 && current < 40)
            {
              Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&b★★30성+★★", "&f30성을 달성했습니다!\n&f오이포스 강화에 실패하여 강화 단계가\n&f하락하더라도 30성 밑으로는 하락하지 않습니다.");
              player.sendMessage(msg);
            }
            else if (current >= 40 && current < 50)
            {
              Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&d★☆★40성+★☆★", "&f40성을 달성했습니다!\n&f오이포스 강화에 실패하여 강화 단계가\n&f하락하더라도 40성 밑으로는 하락하지 않습니다.");
              player.sendMessage(msg);
            }
            else if (current == 50)
            {
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&4★&c☆&6★&e☆&a★&25&b0&3성&9★&1☆&5★&d☆&4★");
            }
            sendChanceMessage(player, success, failKeep, failDown, destroy);
          }
          else
          {
            checkCucumberForceReinforce(player, current);
          }
          type = ReinforceType.CUCUMBERFORCE;
        }
        else if (isEBEBEBItem)
        {
          if (downTwice)
          {
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&e연속 2회 하락 CHANCE TIME!");
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화 성공 확률 : &e" + 100 + "%");
            if (!chanceTime.contains(player))
            {
              chanceTime.add(player);
            }
          }
          else
          {
            checkEBEBEBReinforce(player, current);
          }
          type = ReinforceType.EBEBEB;
        }
        List<String> options = convertOptions(getOptions(type, itemType, current, reinforceRank), true);
        for (String option : options)
        {
          MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, option);
        }
        Component a = ComponentUtil.create("&9[&3강&e화&9] &f강화를 시작하려면 ");
        Component b = ComponentUtil.create("&a\"강화 시작\"", "&f클릭하면 강화를 시작합니다.\n&c이 작업은 되돌릴 수 없습니다.", ClickEvent.Action.RUN_COMMAND, Constant.REINFORCE_START);
        Component c = ComponentUtil.create("&f을, 강화를 중지하려면 ");
        Component d = ComponentUtil.create("&c\"강화 중지\"", "&f클릭하면 강화를 중지합니다.", ClickEvent.Action.RUN_COMMAND, Constant.REINFORCE_QUIT);
        Component e = ComponentUtil.create("&f를 채팅창을 열고 클릭해주세요. 파괴 방지를 사용하지 않으려면 ");
        Component f = ComponentUtil.create("&b\"여기\"", "&f클릭하면 파괴 방지를 사용하지 않습니다.", ClickEvent.Action.RUN_COMMAND, Constant.REINFORCE_DO_NOT_USE_ANTI_DESTRUCTION);
        Component g = ComponentUtil.create("&f를 클릭하세요.");
        MessageUtil.sendMessage(player, a, b, c, d, e, f, g);
      }
      else if (args[0].equalsIgnoreCase("파괴방지미사용"))
      {
        if (!((type == ReinforceType.STARFORCE && current >= ANTI_DESTRUCTION_STARFORCE_FROM && current <= ANTI_DESTRUCTION_STARFORCE_TO) || (type == ReinforceType.CUCUMBERFORCE && current >= ANTI_DESTRUCTION_CUCUMBERFORCE_FROM && current <= ANTI_DESTRUCTION_CUCUMBERFORCE_TO)))
        {
          return true;
        }
        if (!Variable.scrollReinforcing.contains(player.getUniqueId()))
        {
          return true;
        }
        if (players.contains(player.getUniqueId()))
        {
          return true;
        }
        if (antiDest == null || !antiDest.containsKey(player) || !antiDest.get(player))
        {
          return true;
        }
        antiDest.put(player, false);
        reinforceCost = (int) Math.round(getCost(current, reinforceRank, type));
        if (reinforceCost > reinforcePoint)
        {
          MessageUtil.sendError(player, "강화 포인트가 부족합니다. 현재 강화 포인트 : &e" + reinforcePoint + "&f, 요구 강화 포인트 : &e" + reinforceCost);
          return true;
        }
        Variable.scrollReinforcing.add(player.getUniqueId());
        MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "----------------------------------------------------------------------------------------------");
        MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "현재 강화 포인트 : &e" + reinforcePoint + "&f, 요구 강화 포인트 : &e" + reinforceCost);
        MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "" + current + "성 >> " + (current + 1) + "성");
        if (isStarforceItem)
        {
          if (확률주작)
          {
            Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&e★10성+★", "&f10성을 달성했습니다!\n&f스타포스 강화에 실패하여 강화 단계가\n&f하락하더라도 10성 밑으로는 하락하지 않습니다.");
            player.sendMessage(msg);
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&c실패 시 아이템이 &4파괴&c되거나 &6단계&c가 &4하락&c됩니다.");
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화 성공 확률 : &e" + 40 + "%");
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화 실패(하락) 확률 : &e" + 59.4 + "%");
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "파괴 확률 : &e" + 0.6 + "%");
          }
          else
          {
            if (downTwice)
            {
              if (current >= 10 && current < 15)
              {
                Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&e★10성+★", "&f10성을 달성했습니다!\n&f스타포스 강화에 실패하여 강화 단계가\n&f하락하더라도 10성 밑으로는 하락하지 않습니다.");
                player.sendMessage(msg);
              }
              else if (current >= 15 && current < 20)
              {
                Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&b★★15성+★★", "&f15성을 달성했습니다!\n&f스타포스 강화에 실패하여 강화 단계가\n&f하락하더라도 15성 밑으로는 하락하지 않습니다.");
                player.sendMessage(msg);
              }
              else if (current >= 20 && current < 25)
              {
                Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&d★☆★20성+★☆★", "&f20성을 달성했습니다!\n&f스타포스 강화에 실패하여 강화 단계가\n&f하락하더라도 20성 밑으로는 하락하지 않습니다.");
                player.sendMessage(msg);
              }
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&e연속 2회 하락 CHANCE TIME!");
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화 성공 확률 : &e" + 100 + "%");
              if (!chanceTime.contains(player))
              {
                chanceTime.add(player);
              }
            }
            else
            {
              checkReinforce(player, current);
            }
          }
          type = ReinforceType.STARFORCE;
        }
        else if (isSuperiorItem)
        {
          if (downTwice)
          {
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&e연속 2회 하락 CHANCE TIME!");
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화 성공 확률 : &e" + 100 + "%");
            if (!chanceTime.contains(player))
            {
              chanceTime.add(player);
            }
          }
          else
          {
            checkSuperiorReinforce(player, current);
          }
          type = ReinforceType.SUPERIOR;
        }
        else if (isCucumberForceItem)
        {
          if (downTwice)
          {
            if (current >= 20 && current < 30)
            {
              Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&e★20성+★", "&f20성을 달성했습니다!\n&f오이포스 강화에 실패하여 강화 단계가\n&f하락하더라도 20성 밑으로는 하락하지 않습니다.");
              player.sendMessage(msg);
            }

            else if (current >= 30 && current < 40)
            {
              Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&b★★30성+★★", "&f30성을 달성했습니다!\n&f오이포스 강화에 실패하여 강화 단계가\n&f하락하더라도 30성 밑으로는 하락하지 않습니다.");
              player.sendMessage(msg);
            }

            else if (current >= 40 && current < 50)
            {
              Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&d★☆★40성+★☆★", "&f40성을 달성했습니다!\n&f오이포스 강화에 실패하여 강화 단계가\n&f하락하더라도 40성 밑으로는 하락하지 않습니다.");
              player.sendMessage(msg);
            }
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&e연속 2회 하락 CHANCE TIME!");
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화 성공 확률 : &e" + 100 + "%");
            if (!chanceTime.contains(player))
            {
              chanceTime.add(player);
            }
          }
          else
          {
            checkCucumberForceReinforce(player, current);
          }
          type = ReinforceType.CUCUMBERFORCE;
        }
        else if (isEBEBEBItem)
        {
          if (downTwice)
          {
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&e연속 2회 하락 CHANCE TIME!");
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화 성공 확률 : &e" + 100 + "%");
            if (!chanceTime.contains(player))
            {
              chanceTime.add(player);
            }
          }
          else
          {
            checkEBEBEBReinforce(player, current);
          }
          type = ReinforceType.EBEBEB;
        }
        List<String> options = convertOptions(getOptions(type, itemType, current, reinforceRank), true);
        for (String option : options)
        {
          MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, option);
        }
        Component a = ComponentUtil.create("&9[&3강&e화&9] &f강화를 시작하려면 ");
        Component b = ComponentUtil.create("&a\"강화 시작\"", "&f클릭하면 강화를 시작합니다.\n&c이 작업은 되돌릴 수 없습니다.", ClickEvent.Action.RUN_COMMAND, Constant.REINFORCE_START);
        Component c = ComponentUtil.create("&f을, 강화를 중지하려면 ");
        Component d = ComponentUtil.create("&c\"강화 중지\"", "&f클릭하면 강화를 중지합니다.", ClickEvent.Action.RUN_COMMAND, Constant.REINFORCE_QUIT);
        Component e = ComponentUtil.create("&f를 채팅창을 열고 클릭해주세요. 파괴 방지를 사용하려면 ");
        Component f = ComponentUtil.create("&e\"여기\"", "&f클릭하면 파괴 방지를 사용합니다.", ClickEvent.Action.RUN_COMMAND, Constant.REINFORCE_USE_ANTI_DESTRUCTION);
        Component g = ComponentUtil.create("&f를 클릭하세요.");
        if (type == ReinforceType.STARFORCE)
        {
          f = ComponentUtil.create("&e\"여기\"", "&f클릭하면 파괴 방지를 사용합니다.\n&f파괴 방지 기능은 12성에서 16성 사이의 아이템으로\n&f강화를 시도할때만 사용할 수 있습니다.", ClickEvent.Action.RUN_COMMAND,
                  Constant.REINFORCE_USE_ANTI_DESTRUCTION);
        }
        else if (type == ReinforceType.CUCUMBERFORCE)
        {
          f = ComponentUtil.create("&e\"여기\"", "&f클릭하면 파괴 방지를 사용합니다.\n&f파괴 방지 기능은 24성에서 33성 사이의 아이템으로\n&f강화를 시도할때만 사용할 수 있습니다.", ClickEvent.Action.RUN_COMMAND,
                  Constant.REINFORCE_USE_ANTI_DESTRUCTION);
        }
        MessageUtil.sendMessage(player, a, b, c, d, e, f, g);
      }
      else if (args[0].equalsIgnoreCase("realstart"))
      {
        if (!Variable.scrollReinforcing.contains(player.getUniqueId()))
        {
          MessageUtil.sendError(player, "강화를 하고 있지 않습니다.");
          return true;
        }
        if (players.contains(player.getUniqueId()))
        {
          MessageUtil.sendError(player, "이미 강화를 시도하고 있습니다.");
          return true;
        }
        if (chanceTime.contains(player))
        {
          chanceTime.remove(player);
          player.stopSound("reinforce_chancetime");
          for (int i = 0; i <= 20; i++)
          {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () ->
            {
              MessageUtil.sendTitle(player, "", "", 0, 1, 0);
            }, i);
          }
        }
        players.add(player.getUniqueId());
        boolean use = UserData.SERVER_RESOURCEPACK.getBoolean(player.getUniqueId());
        Method.reinforceSound(player, Method.ReinforceSound.OPERATION, use, Method.ReinforceType.COMMAND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), new Runnable()
        {
          @SuppressWarnings("unused")
          public void run()
          {
            Variable.scrollReinforcing.remove(player.getUniqueId());
            players.remove(player.getUniqueId());
            if (!ItemStackUtil.itemExists(item))
            {
              MessageUtil.sendError(player, Prefix.NO_HOLDING_ITEM);
              return;
            }
            ItemStack item = player.getInventory().getItemInMainHand().clone();
            int current = 0, max = 0;
            int line = 0, pointLine = 0;
            long reinforcePoint = 0L, maxReinforcePoint = 0L;
            ReinforceType type = null;
            ItemType itemType = null;
            double reinforceRank = 1D;
            boolean downTwice = false;
            int downCount = 0;
            String prefix = "", pointPrefix = "";
            if (item.hasItemMeta() && item.getItemMeta().hasLore())
            {
              for (int i = 0; i < item.getItemMeta().getLore().size(); i++)
              {
                String lore = MessageUtil.stripColor(item.getItemMeta().getLore().get(i));
                if (lore.contains("강화 포인트 : "))
                {
                  try
                  {
                    reinforcePoint = Long.parseLong(lore.split("강화 포인트 : ")[1].split(" / ")[0]);
                    maxReinforcePoint = Long.parseLong(lore.split("강화 포인트 : ")[1].split(" / ")[1]);
                    pointLine = i;
                    pointPrefix = item.getItemMeta().getLore().get(i).split("강화 포인트 : ")[0];
                  }
                  catch (Exception e)
                  {
                    reinforcePoint = 0L;
                  }
                }
                if (lore.contains("장비 분류 : "))
                {
                  String itemName = lore.split("장비 분류 : ")[1];
                  switch (itemName)
                  {
                    case "근거리 무기 - 검":
                    case "검":
                      itemType = ItemType.SWORD;
                      break;
                    case "원거리 무기 - 활":
                    case "활":
                      itemType = ItemType.BOW;
                      break;
                    case "투구":
                      itemType = ItemType.HELMET;
                      break;
                    case "흉갑":
                      itemType = ItemType.CHESTPLATE;
                      break;
                    case "각반":
                      itemType = ItemType.LEGGINGS;
                      break;
                    case "부츠":
                      itemType = ItemType.BOOTS;
                      break;
                    case "방패":
                      itemType = ItemType.SHIELD;
                      break;
                  }
                }
                if (lore.contains("아이템 강화 등급 : "))
                {
                  try
                  {
                    reinforceRank = Double.parseDouble(lore.split("아이템 강화 등급 : ")[1]);
                  }
                  catch (Exception e)
                  {
                    reinforceRank = 1;
                  }
                }
                if (lore.contains("연속 하락 횟수 : "))
                {
                  try
                  {
                    downCount = Integer.parseInt(lore.split("연속 하락 횟수 : ")[1]);

                    if (downCount == 2)
                    {
                      downTwice = true;
                    }
                  }
                  catch (Exception e)
                  {
                    downCount = 0;
                    downTwice = false;
                  }
                }
                if (lore.contains("스타포스 : "))
                {
                  line = i;
                  String[] cut = lore.split("스타포스 : ");
                  String a = cut[1];
                  String[] cut2 = a.split(" / ");
                  try
                  {
                    prefix = item.getItemMeta().getLore().get(i).split("스타포스 : ")[0];
                    current = Integer.parseInt(cut2[0]);
                    max = Integer.parseInt(cut2[1]);
                    type = ReinforceType.STARFORCE;
                  }
                  catch (NumberFormatException exception)
                  {
                  }
                }
                else if (lore.contains("슈페리얼 : "))
                {
                  line = i;
                  String[] cut = lore.split("슈페리얼 : ");
                  String a = cut[1];
                  String[] cut2 = a.split(" / ");
                  try
                  {
                    prefix = item.getItemMeta().getLore().get(i).split("슈페리얼 : ")[0];
                    current = Integer.parseInt(cut2[0]);
                    max = Integer.parseInt(cut2[1]);
                    type = ReinforceType.SUPERIOR;
                  }
                  catch (NumberFormatException exception)
                  {
                  }
                }
                else if (lore.contains("오이포스 : "))
                {
                  line = i;
                  String[] cut = lore.split("오이포스 : ");
                  String a = cut[1];
                  String[] cut2 = a.split(" / ");
                  try
                  {
                    prefix = item.getItemMeta().getLore().get(i).split("오이포스 : ")[0];
                    current = Integer.parseInt(cut2[0]);
                    max = Integer.parseInt(cut2[1]);
                    type = ReinforceType.CUCUMBERFORCE;
                  }
                  catch (NumberFormatException exception)
                  {
                  }
                }
                else if (lore.contains("에베벱 : "))
                {
                  line = i;
                  String[] cut = lore.split("에베벱 : ");
                  String a = cut[1];
                  String[] cut2 = a.split(" / ");
                  try
                  {
                    prefix = item.getItemMeta().getLore().get(i).split("에베벱 : ")[0];
                    current = Integer.parseInt(cut2[0]);
                    max = Integer.parseInt(cut2[1]);
                    type = ReinforceType.EBEBEB;
                  }
                  catch (NumberFormatException exception)
                  {
                  }
                }
              }
            }
            double[] info = null;
            switch (type)
            {
              case CUCUMBERFORCE:
                info = getCucumberyInfo(current);
                break;
              case STARFORCE:
                info = getStarForceInfo(current);
                break;
              case SUPERIOR:
                info = getSuperiorInfo(current);
                break;
              case EBEBEB:
                info = getEBEBEBInfo(current);
              default:
                break;
            }
            int reinforceCost = (int) Math.round(getCost(current, reinforceRank, type));
            if (antiDest != null && antiDest.containsKey(player) && antiDest.get(player))
            {
              reinforceCost = (int) Math.round(getCost(current, reinforceRank, type)) * 2;
            }
            reinforcePoint -= reinforceCost;
            ItemMeta meta2 = item.getItemMeta();
            List<String> lores2 = meta2.getLore();
            lores2.set(pointLine, pointPrefix + "강화 포인트 : " + reinforcePoint + " / " + maxReinforcePoint);
            meta2.setLore(lores2);
            item.setItemMeta(meta2);
            player.getInventory().setItemInMainHand(item);
            double success = info[0], fail = info[1], failDown2 = info[2], destroy = info[3];
            double random = Math.random() * 100D;
            boolean destroyGG = false;
            boolean bSuccess, bFail, bDestroy;
            if (downTwice)
            {
              success = 100D;
              fail = 0D;
              destroy = 0D;
            }
            if (antiDest != null && antiDest.containsKey(player) && antiDest.get(player))
            {
              if (failDown2 == 0D && fail != 0D)
              {
                fail += destroy;
              }
              else if (fail != 0D && failDown2 != 0D)
              {
                fail += destroy / 2D;
              }
              destroy = 0D;
            }
            double suc = success / 10D, des = destroy / 10D, fai = fail / 10D;
            // bSuccess = (destroy < success && random > destroy && success + destroy >= random) || (destroy >= success && random < success);
            bSuccess = (destroy < success && ((random > des && random <= suc + des) || (random > des + 10D && random <= suc + des + 10D) || (random > des + 20D && random <= suc + des + 20D) || (random > des + 30D && random <= suc + des + 30D) || (random > des + 40D && random <= suc + des + 40D) || (random > des + 50D && random <= suc + des + 50D) || (random > des + 60D && random <= suc + des + 60D) || (random > des + 70D && random <= suc + des + 70D) || (random > des + 80D && random <= suc + des + 80D) || (random > des + 90D && random <= suc + des + 90D))) || (destroy >= success && ((random < suc) || (random >= 10D && random < suc + 10D) || (random >= 20D && random < suc + 20D) || (random >= 30D && random < suc + 30D) || (random >= 40D && random < suc + 40D) || (random >= 50D && random < suc + 50D) || (random >= 60D && random < suc + 60D) || (random >= 70D && random < suc + 70D) || (random >= 80D && random < suc + 80D) || (random >= 90D && random < suc + 75D)));
            // bFail = random > success + destroy && random <= success + destroy + fail;
            bFail = (random > suc + des && random <= suc + des + fai) || (random >= suc + des + 10D && random <= suc + des + fai + 10D) || (random >= suc + des + 20D && random <= suc + des + fai + 20D) || (random >= suc + des + 30D && random <= suc + des + fai + 30D) || (random >= suc + des + 40D && random <= suc + des + fai + 40D) || (random >= suc + des + 50D && random <= suc + des + fai + 50D) || (random >= suc + des + 60D && random <= suc + des + fai + 60D) || (random >= suc + des + 70D && random <= suc + des + fai + 70D) || (random >= suc + des + 80D && random <= suc + des + fai + 80D || (random >= suc + des + 90D && random <= suc + des + fai + 90D));
            // bDestroy = (destroy < success && random < destroy) || (destroy >= success && random > success && random <= success + destroy);
            bDestroy = destroy < success && (random < des || (random >= 10D && random < des + 10D) || (random >= 20D && random < des + 20D) || (random >= 30D && random < des + 30D) || (random >= 40D && random < des + 40D) || (random >= 50D && random < des + 50D) || (random >= 60D && random < des + 60D) || (random >= 70D && random < des + 70D) || (random >= 80D && random < des + 80D) || (random >= 90D && random < des + 90D)) || destroy >= success && ((random >= suc && random < suc + des) || (random >= suc + 10D && random < suc + des + 10D) || (random >= suc + 20D && random < suc + des + 20D) || (random >= suc + 30D && random < suc + des + 30D) || (random >= suc + 40D && random < suc + des + 40D) || (random >= suc + 50D && random < suc + des + 50D) || (random >= suc + 60D && random < suc + des + 60D) || (random >= suc + 70D && random < suc + des + 70D) || (random >= suc + 80D && random < suc + des + 80D) || (random >= suc + 90D && random < suc + des + 90D));
            if (UserData.SHOW_PLUGIN_DEV_DEBUG_MESSAGE.getBoolean(player.getUniqueId()))
            {
              MessageUtil.sendMessage(player, random + "");
              MessageUtil.sendMessage(player, "------------------------성공 범위--------------------------");
              if (destroy < success)
              {
                MessageUtil.sendMessage(player, ((random >= 00D + des && random < 00D + suc + des) ? "&a" : "") + df3.format(00D + des) + " ~ " + df3.format(00D + suc + des));
                MessageUtil.sendMessage(player, ((random >= 10D + des && random < 10D + suc + des) ? "&a" : "") + df3.format(10D + des) + " ~ " + df3.format(10D + suc + des));
                MessageUtil.sendMessage(player, ((random >= 20D + des && random < 20D + suc + des) ? "&a" : "") + df3.format(20D + des) + " ~ " + df3.format(20D + suc + des));
                MessageUtil.sendMessage(player, ((random >= 30D + des && random < 30D + suc + des) ? "&a" : "") + df3.format(30D + des) + " ~ " + df3.format(30D + suc + des));
                MessageUtil.sendMessage(player, ((random >= 40D + des && random < 40D + suc + des) ? "&a" : "") + df3.format(40D + des) + " ~ " + df3.format(40D + suc + des));
                MessageUtil.sendMessage(player, ((random >= 50D + des && random < 50D + suc + des) ? "&a" : "") + df3.format(50D + des) + " ~ " + df3.format(50D + suc + des));
                MessageUtil.sendMessage(player, ((random >= 60D + des && random < 60D + suc + des) ? "&a" : "") + df3.format(60D + des) + " ~ " + df3.format(60D + suc + des));
                MessageUtil.sendMessage(player, ((random >= 70D + des && random < 70D + suc + des) ? "&a" : "") + df3.format(70D + des) + " ~ " + df3.format(70D + suc + des));
                MessageUtil.sendMessage(player, ((random >= 80D + des && random < 80D + suc + des) ? "&a" : "") + df3.format(80D + des) + " ~ " + df3.format(80D + suc + des));
                MessageUtil.sendMessage(player, ((random >= 90D + des && random < 90D + suc + des) ? "&a" : "") + df3.format(90D + des) + " ~ " + df3.format(90D + suc + des));
              }
              else
              {
                MessageUtil.sendMessage(player, ((random >= 00D && random < 00D + suc) ? "&a" : "") + df3.format(00D) + " ~ " + df3.format(00D + suc));
                MessageUtil.sendMessage(player, ((random >= 10D && random < 10D + suc) ? "&a" : "") + df3.format(10D) + " ~ " + df3.format(10D + suc));
                MessageUtil.sendMessage(player, ((random >= 20D && random < 20D + suc) ? "&a" : "") + df3.format(20D) + " ~ " + df3.format(20D + suc));
                MessageUtil.sendMessage(player, ((random >= 30D && random < 30D + suc) ? "&a" : "") + df3.format(30D) + " ~ " + df3.format(30D + suc));
                MessageUtil.sendMessage(player, ((random >= 40D && random < 40D + suc) ? "&a" : "") + df3.format(40D) + " ~ " + df3.format(40D + suc));
                MessageUtil.sendMessage(player, ((random >= 50D && random < 50D + suc) ? "&a" : "") + df3.format(50D) + " ~ " + df3.format(50D + suc));
                MessageUtil.sendMessage(player, ((random >= 60D && random < 60D + suc) ? "&a" : "") + df3.format(60D) + " ~ " + df3.format(60D + suc));
                MessageUtil.sendMessage(player, ((random >= 70D && random < 70D + suc) ? "&a" : "") + df3.format(70D) + " ~ " + df3.format(70D + suc));
                MessageUtil.sendMessage(player, ((random >= 80D && random < 80D + suc) ? "&a" : "") + df3.format(80D) + " ~ " + df3.format(80D + suc));
                MessageUtil.sendMessage(player, ((random >= 90D && random < 90D + suc) ? "&a" : "") + df3.format(90D) + " ~ " + df3.format(90D + suc));
              }
              if (des != 0)
              {
                if (destroy < success)
                {
                  MessageUtil.sendMessage(player, "------------------------파괴 범위--------------------------");
                  MessageUtil.sendMessage(player, ((random >= 00D && random < 00D + des) ? "&a" : "") + df3.format(00D) + " ~ " + df3.format(00D + des));
                  MessageUtil.sendMessage(player, ((random >= 10D && random < 10D + des) ? "&a" : "") + df3.format(10D) + " ~ " + df3.format(10D + des));
                  MessageUtil.sendMessage(player, ((random >= 20D && random < 20D + des) ? "&a" : "") + df3.format(20D) + " ~ " + df3.format(20D + des));
                  MessageUtil.sendMessage(player, ((random >= 30D && random < 30D + des) ? "&a" : "") + df3.format(30D) + " ~ " + df3.format(30D + des));
                  MessageUtil.sendMessage(player, ((random >= 40D && random < 40D + des) ? "&a" : "") + df3.format(40D) + " ~ " + df3.format(40D + des));
                  MessageUtil.sendMessage(player, ((random >= 50D && random < 50D + des) ? "&a" : "") + df3.format(50D) + " ~ " + df3.format(50D + des));
                  MessageUtil.sendMessage(player, ((random >= 60D && random < 60D + des) ? "&a" : "") + df3.format(60D) + " ~ " + df3.format(60D + des));
                  MessageUtil.sendMessage(player, ((random >= 70D && random < 70D + des) ? "&a" : "") + df3.format(70D) + " ~ " + df3.format(70D + des));
                  MessageUtil.sendMessage(player, ((random >= 80D && random < 80D + des) ? "&a" : "") + df3.format(80D) + " ~ " + df3.format(80D + des));
                  MessageUtil.sendMessage(player, ((random >= 90D && random < 90D + des) ? "&a" : "") + df3.format(90D) + " ~ " + df3.format(90D + des));
                }
                else
                {
                  MessageUtil.sendMessage(player, "------------------------파괴 범위--------------------------");
                  MessageUtil.sendMessage(player, ((random >= 00D + suc && random < 00D + suc + des) ? "&a" : "") + df3.format(00D + suc) + " ~ " + df3.format(00D + des + suc));
                  MessageUtil.sendMessage(player, ((random >= 10D + suc && random < 10D + suc + des) ? "&a" : "") + df3.format(10D + suc) + " ~ " + df3.format(10D + des + suc));
                  MessageUtil.sendMessage(player, ((random >= 20D + suc && random < 20D + suc + des) ? "&a" : "") + df3.format(20D + suc) + " ~ " + df3.format(20D + des + suc));
                  MessageUtil.sendMessage(player, ((random >= 30D + suc && random < 30D + suc + des) ? "&a" : "") + df3.format(30D + suc) + " ~ " + df3.format(30D + des + suc));
                  MessageUtil.sendMessage(player, ((random >= 40D + suc && random < 40D + suc + des) ? "&a" : "") + df3.format(40D + suc) + " ~ " + df3.format(40D + des + suc));
                  MessageUtil.sendMessage(player, ((random >= 50D + suc && random < 50D + suc + des) ? "&a" : "") + df3.format(50D + suc) + " ~ " + df3.format(50D + des + suc));
                  MessageUtil.sendMessage(player, ((random >= 60D + suc && random < 60D + suc + des) ? "&a" : "") + df3.format(60D + suc) + " ~ " + df3.format(60D + des + suc));
                  MessageUtil.sendMessage(player, ((random >= 70D + suc && random < 70D + suc + des) ? "&a" : "") + df3.format(70D + suc) + " ~ " + df3.format(70D + des + suc));
                  MessageUtil.sendMessage(player, ((random >= 80D + suc && random < 80D + suc + des) ? "&a" : "") + df3.format(80D + suc) + " ~ " + df3.format(80D + des + suc));
                  MessageUtil.sendMessage(player, ((random >= 90D + suc && random < 90D + suc + des) ? "&a" : "") + df3.format(90D + suc) + " ~ " + df3.format(90D + des + suc));
                }
              }
              if (fai != 0)
              {
                MessageUtil.sendMessage(player, "------------------------실패(유지) 범위--------------------------");
                MessageUtil.sendMessage(player, ((random >= 00D + suc + des && random < 00D + suc + des + fai) ? "&a" : "") + df3.format(00D + suc + des) + " ~ " + df3.format(00D + des + suc + fai));
                MessageUtil.sendMessage(player, ((random >= 10D + suc + des && random < 10D + suc + des + fai) ? "&a" : "") + df3.format(10D + suc + des) + " ~ " + df3.format(10D + des + suc + fai));
                MessageUtil.sendMessage(player, ((random >= 20D + suc + des && random < 20D + suc + des + fai) ? "&a" : "") + df3.format(20D + suc + des) + " ~ " + df3.format(20D + des + suc + fai));
                MessageUtil.sendMessage(player, ((random >= 30D + suc + des && random < 30D + suc + des + fai) ? "&a" : "") + df3.format(30D + suc + des) + " ~ " + df3.format(30D + des + suc + fai));
                MessageUtil.sendMessage(player, ((random >= 40D + suc + des && random < 40D + suc + des + fai) ? "&a" : "") + df3.format(40D + suc + des) + " ~ " + df3.format(40D + des + suc + fai));
                MessageUtil.sendMessage(player, ((random >= 50D + suc + des && random < 50D + suc + des + fai) ? "&a" : "") + df3.format(50D + suc + des) + " ~ " + df3.format(50D + des + suc + fai));
                MessageUtil.sendMessage(player, ((random >= 60D + suc + des && random < 60D + suc + des + fai) ? "&a" : "") + df3.format(60D + suc + des) + " ~ " + df3.format(60D + des + suc + fai));
                MessageUtil.sendMessage(player, ((random >= 70D + suc + des && random < 70D + suc + des + fai) ? "&a" : "") + df3.format(70D + suc + des) + " ~ " + df3.format(70D + des + suc + fai));
                MessageUtil.sendMessage(player, ((random >= 80D + suc + des && random < 80D + suc + des + fai) ? "&a" : "") + df3.format(80D + suc + des) + " ~ " + df3.format(80D + des + suc + fai));
                MessageUtil.sendMessage(player, ((random >= 90D + suc + des && random < 90D + suc + des + fai) ? "&a" : "") + df3.format(90D + suc + des) + " ~ " + df3.format(90D + des + suc + fai));
              }
              if (suc + des + fai != 10D)
              {
                MessageUtil.sendMessage(player, "------------------------실패(하락) 범위--------------------------");
                MessageUtil.sendMessage(player, ((random >= 00D + suc + des + fai && random < 10D) ? "&a" : "") + df3.format(00D + suc + des + fai) + " ~ " + df3.format(10D));
                MessageUtil.sendMessage(player, ((random >= 10D + suc + des + fai && random < 20D) ? "&a" : "") + df3.format(10D + suc + des + fai) + " ~ " + df3.format(20D));
                MessageUtil.sendMessage(player, ((random >= 20D + suc + des + fai && random < 30D) ? "&a" : "") + df3.format(20D + suc + des + fai) + " ~ " + df3.format(30D));
                MessageUtil.sendMessage(player, ((random >= 30D + suc + des + fai && random < 40D) ? "&a" : "") + df3.format(30D + suc + des + fai) + " ~ " + df3.format(40D));
                MessageUtil.sendMessage(player, ((random >= 40D + suc + des + fai && random < 50D) ? "&a" : "") + df3.format(40D + suc + des + fai) + " ~ " + df3.format(50D));
                MessageUtil.sendMessage(player, ((random >= 50D + suc + des + fai && random < 60D) ? "&a" : "") + df3.format(50D + suc + des + fai) + " ~ " + df3.format(60D));
                MessageUtil.sendMessage(player, ((random >= 60D + suc + des + fai && random < 70D) ? "&a" : "") + df3.format(60D + suc + des + fai) + " ~ " + df3.format(70D));
                MessageUtil.sendMessage(player, ((random >= 70D + suc + des + fai && random < 80D) ? "&a" : "") + df3.format(70D + suc + des + fai) + " ~ " + df3.format(80D));
                MessageUtil.sendMessage(player, ((random >= 80D + suc + des + fai && random < 90D) ? "&a" : "") + df3.format(80D + suc + des + fai) + " ~ " + df3.format(90D));
                MessageUtil.sendMessage(player, ((random >= 90D + suc + des + fai && random < 100D) ? "&a" : "") + df3.format(90D + suc + des + fai) + " ~ " + df3.format(100D));
              }
            }
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "----------------------------------------------------------------------------------------------");
            if (bSuccess) // 성공
            {
              double[] options = getOptions(type, itemType, current, reinforceRank);
              List<String> optionsStr = convertOptions(options, true);
              current++;
              ItemMeta meta = item.getItemMeta();
              List<String> lores = meta.getLore();
              switch (type)
              {
                case CUCUMBERFORCE:
                  lores.set(line, prefix + "오이포스 : " + current + " / " + max);
                  break;
                case STARFORCE:
                  lores.set(line, prefix + "스타포스 : " + current + " / " + max);
                  break;
                case SUPERIOR:
                  lores.set(line, prefix + "슈페리얼 : " + current + " / " + max);
                  break;
                case EBEBEB:
                  lores.set(line, prefix + "에베벱 : " + current + " / " + max);
                  break;
                default:
                  break;
              }
              meta.setLore(lores);
              if (downTwice || downCount > 0)
              {
                int downLine = -1;
                for (int i = 0; i < lores.size(); i++)
                {
                  String lore = MessageUtil.stripColor(lores.get(i));

                  if (lore.contains("연속 하락 횟수 : "))
                  {
                    downLine = i;
                    break;
                  }
                }
                if (downLine != -1)
                {
                  downTwice = false;
                  if (lores.size() == 1)
                  {
                    meta.setLore(null);
                  }
                  else
                  {
                    lores.remove(downLine);
                  }
                }
              }
              meta.setLore(lores);
              boolean equal[] = new boolean[optionsStr.size()];
              for (int i = 0; i < equal.length; i++)
              {
                equal[i] = false;
              }
              for (int i = 0; i < optionsStr.size(); i++)
              {
                String str = optionsStr.get(i);
                if (str.contains(" : "))
                {
                  String[] c1 = str.split(" : ");
                  for (int j = 0; j < lores.size(); j++)
                  {
                    String str2 = lores.get(j);
                    if (str2.contains(" : "))
                    {
                      String[] c2 = str2.split(" : ");
                      if (c1[0].startsWith(MessageUtil.stripColor(c2[0].replace(GetStat.PREFIX_1, ""))) && c2[0].startsWith(GetStat.PREFIX_1))
                      {
                        if (c2[1].contains(GetStat.PREFIX_2))
                        {
                          c2[1] = c2[1].split(GetStat.PREFIX_2)[0];
                        }
                        if (c1[1].endsWith("%") && c2[1].endsWith("%"))
                        {
                          equal[i] = true;
                          double temp1 = 0D;
                          double temp2 = 0D;
                          String c1Str = c1[1].substring(0, c1[1].length() - 1);
                          String c2Str = c2[1].substring(0, c2[1].length() - 1);
                          if (c1Str.contains("-"))
                          {
                            try
                            {
                              temp1 -= Double.parseDouble(c1Str.substring(1, c1Str.length()));
                            }
                            catch (NumberFormatException exception)
                            {
                              equal[i] = false;
                            }
                          }
                          else if (c1Str.contains("+"))
                          {
                            try
                            {
                              temp1 += Double.parseDouble(c1Str.substring(1, c1Str.length()));
                            }
                            catch (NumberFormatException exception)
                            {
                              equal[i] = false;
                            }
                          }
                          if (c2Str.contains("-"))
                          {
                            try
                            {
                              temp2 -= Double.parseDouble(c2Str.substring(1, c2Str.length()));
                            }
                            catch (NumberFormatException exception)
                            {
                              equal[i] = false;
                            }
                          }
                          else if (c2Str.contains("+"))
                          {
                            try
                            {
                              temp2 += Double.parseDouble(c2Str.substring(1, c2Str.length()));
                            }
                            catch (NumberFormatException exception)
                            {
                              equal[i] = false;
                            }
                          }
                          temp1 += temp2;
                          String strTemp = "";
                          if (!str2.contains(GetStat.PREFIX_2)) // 기본값만 있을 때
                          {
                            strTemp = GetStat.PREFIX_1 + "&b" + c1[0] + " : " + ((temp1 > 0) ? "+" : "") + round.format(temp1) + "%" + GetStat.PREFIX_2 + "&f (" + round
                                    .format(temp2) + "%" + GetStat.PREFIX_4 + "&b " + ((temp1 - temp2 > 0) ? "+" : "") + round.format(temp1 - temp2) + "%" + GetStat.PREFIX_4 + "&f)";
                          }
                          else if (!str2.contains(GetStat.PREFIX_3) && str2.contains(GetStat.PREFIX_4)) // 업그레이드만 있을 때
                          {
                            String defaultValueString = str2.split(GetStat.PREFIX_2)[1];
                            defaultValueString = str2.split(GetStat.PREFIX_2)[1];
                            defaultValueString = defaultValueString.replace("§f ", "").replace("(", "").replace(")", "");
                            if (defaultValueString.contains(GetStat.PREFIX_4))
                            {
                              defaultValueString = defaultValueString.split(GetStat.PREFIX_4)[0];
                            }
                            double defaultValue = Double.parseDouble(defaultValueString.replace("%", ""));
                            double upgradeValue = Double.parseDouble(MessageUtil.stripColor(str2.split(GetStat.PREFIX_4)[1].replace("&b ", "").replace("%", "").replace("+", "").replace(")", "")));
                            strTemp = GetStat.PREFIX_1 + "&b" + c1[0] + " : " + ((temp1 > 0) ? "+" : "") + round.format(temp1) + "%" + GetStat.PREFIX_2 + "&f (" + round
                                    .format(defaultValue) + "%" + GetStat.PREFIX_4 + "&b " + ((upgradeValue + temp1 - temp2 > 0) ? "+" : "") + round
                                    .format(upgradeValue + temp1 - temp2) + "%" + GetStat.PREFIX_4 + "&f)";
                          }
                          else if (str2.contains(GetStat.PREFIX_3) && !str2.contains(GetStat.PREFIX_4)) // 추가 옵션만 있을 때
                          {
                            String defaultValueString = str2.split(GetStat.PREFIX_2)[1];
                            defaultValueString = str2.split(GetStat.PREFIX_2)[1];
                            defaultValueString = defaultValueString.replace("§f ", "").replace("(", "").replace(")", "");
                            if (defaultValueString.contains(GetStat.PREFIX_3))
                            {
                              defaultValueString = defaultValueString.split(GetStat.PREFIX_3)[0];
                            }
                            double defaultValue = Double.parseDouble(defaultValueString.replace("%", ""));
                            double addiValue = Double.parseDouble(MessageUtil.stripColor(str2.split(GetStat.PREFIX_3)[1].replace("%", "").replace("+", "")));

                            strTemp = GetStat.PREFIX_1 + "&b" + c1[0] + " : " + ((temp1 > 0) ? "+" : "") + round.format(temp1) + "%" + GetStat.PREFIX_2 + "&f (" + round
                                    .format(defaultValue) + "%" + GetStat.PREFIX_3 + "&a " + ((addiValue > 0) ? "+" : "") + round
                                    .format(addiValue) + "%" + GetStat.PREFIX_3 + GetStat.PREFIX_4 + "&b " + ((temp1 - temp2 > 0) ? "+" : "") + round.format(temp1 - temp2) + "%" + GetStat.PREFIX_4 + "&f)";
                          }
                          else // 추가 옵션, 업그레이드 둘 다 있을 때
                          {
                            String defaultValueString = str2.split(GetStat.PREFIX_2)[1];
                            defaultValueString = str2.split(GetStat.PREFIX_2)[1];
                            defaultValueString = defaultValueString.replace("§f ", "").replace("(", "").replace(")", "");
                            if (defaultValueString.contains(GetStat.PREFIX_3))
                            {
                              defaultValueString = defaultValueString.split(GetStat.PREFIX_3)[0];
                            }
                            double defaultValue = Double.parseDouble(defaultValueString.replace("%", ""));
                            double addiValue = Double.parseDouble(MessageUtil.stripColor(str2.split(GetStat.PREFIX_3)[1].replace("%", "").replace("+", "")));
                            double upgradeValue = Double.parseDouble(MessageUtil.stripColor(str2.split(GetStat.PREFIX_4)[1].replace("&b ", "").replace("%", "").replace("+", "").replace(")", "")));

                            strTemp = GetStat.PREFIX_1 + "&b" + c1[0] + " : " + ((temp1 > 0) ? "+" : "") + round.format(temp1) + "%" + GetStat.PREFIX_2 + "&f (" + round
                                    .format(defaultValue) + "%" + GetStat.PREFIX_3 + "&a " + ((addiValue > 0) ? "+" : "") + round
                                    .format(addiValue) + "%" + GetStat.PREFIX_3 + GetStat.PREFIX_4 + "&b " + ((upgradeValue + temp1 - temp2 > 0) ? "+" : "") + round
                                    .format(upgradeValue + temp1 - temp2) + "%" + GetStat.PREFIX_4 + "&f)";
                          }
                          lores.set(j, MessageUtil.n2s(strTemp));
                          meta.setLore(lores);
                          item.setItemMeta(meta);
                          break;
                        }
                        else if (!c1[1].endsWith("%") && !c2[1].endsWith("%"))
                        {
                          equal[i] = true;
                          double temp1 = 0D;
                          double temp2 = 0D;
                          String c1Str = c1[1];
                          String c2Str = c2[1];
                          if (c1Str.contains("-"))
                          {
                            try
                            {
                              temp1 -= Double.parseDouble(c1Str.substring(1, c1Str.length()));
                            }
                            catch (NumberFormatException exception)
                            {
                              equal[i] = false;
                            }
                          }
                          else if (c1Str.contains("+"))
                          {
                            try
                            {
                              temp1 += Double.parseDouble(c1Str.substring(1, c1Str.length()));
                            }
                            catch (NumberFormatException exception)
                            {
                              equal[i] = false;
                            }
                          }
                          if (c2Str.contains("-"))
                          {
                            try
                            {
                              temp2 -= Double.parseDouble(c2Str.substring(1, c2Str.length()));
                            }
                            catch (NumberFormatException exception)
                            {
                              equal[i] = false;
                            }
                          }
                          else if (c2Str.contains("+"))
                          {
                            try
                            {
                              temp2 += Double.parseDouble(c2Str.substring(1, c2Str.length()));
                            }
                            catch (NumberFormatException exception)
                            {
                              equal[i] = false;
                            }
                          }
                          temp1 += temp2;
                          String strTemp = "";
                          if (!str2.contains(GetStat.PREFIX_2)) // 기본값만 있을 때
                          {
                            strTemp = GetStat.PREFIX_1 + "&b" + c1[0] + " : " + ((temp1 > 0) ? "+" : "") + round.format(temp1) + GetStat.PREFIX_2 + "&f (" + round
                                    .format(temp2) + GetStat.PREFIX_4 + "&b " + ((temp1 - temp2 > 0) ? "+" : "") + round.format(temp1 - temp2) + GetStat.PREFIX_4 + "&f)";
                          }
                          else if (!str2.contains(GetStat.PREFIX_3) && str2.contains(GetStat.PREFIX_4)) // 업그레이드만 있을 때
                          {
                            String defaultValueString = str2.split(GetStat.PREFIX_2)[1];
                            defaultValueString = str2.split(GetStat.PREFIX_2)[1];
                            defaultValueString = defaultValueString.replace("§f ", "").replace("(", "").replace(")", "");
                            if (defaultValueString.contains(GetStat.PREFIX_4))
                            {
                              defaultValueString = defaultValueString.split(GetStat.PREFIX_4)[0];
                            }
                            double defaultValue = Double.parseDouble(defaultValueString);
                            double upgradeValue = Double.parseDouble(MessageUtil.stripColor(str2.split(GetStat.PREFIX_4)[1].replace("&b ", "").replace("+", "").replace(")", "")));
                            strTemp = GetStat.PREFIX_1 + "&b" + c1[0] + " : " + ((temp1 > 0) ? "+" : "") + round.format(temp1) + GetStat.PREFIX_2 + "&f (" + round
                                    .format(defaultValue) + GetStat.PREFIX_4 + "&b " + ((upgradeValue + temp1 - temp2 > 0) ? "+" : "") + round
                                    .format(upgradeValue + temp1 - temp2) + GetStat.PREFIX_4 + "&f)";
                          }
                          else if (str2.contains(GetStat.PREFIX_3) && !str2.contains(GetStat.PREFIX_4)) // 추가 옵션만 있을 때
                          {
                            String defaultValueString = str2.split(GetStat.PREFIX_2)[1];
                            defaultValueString = str2.split(GetStat.PREFIX_2)[1];
                            defaultValueString = defaultValueString.replace("§f ", "").replace("(", "").replace(")", "");
                            if (defaultValueString.contains(GetStat.PREFIX_3))
                            {
                              defaultValueString = defaultValueString.split(GetStat.PREFIX_3)[0];
                            }
                            double defaultValue = Double.parseDouble(defaultValueString);
                            double addiValue = Double.parseDouble(MessageUtil.stripColor(str2.split(GetStat.PREFIX_3)[1].replace("+", "")));
                            strTemp = GetStat.PREFIX_1 + "&b" + c1[0] + " : " + ((temp1 > 0) ? "+" : "") + round.format(temp1) + GetStat.PREFIX_2 + "&f (" + round
                                    .format(defaultValue) + GetStat.PREFIX_3 + "&a " + ((addiValue > 0) ? "+" : "") + round
                                    .format(addiValue) + GetStat.PREFIX_3 + GetStat.PREFIX_4 + "&b " + ((temp1 - temp2 > 0) ? "+" : "") + round.format(temp1 - temp2) + GetStat.PREFIX_4 + "&f)";
                          }
                          else // 추가 옵션, 업그레이드 둘 다 있을 때
                          {
                            String defaultValueString = str2.split(GetStat.PREFIX_2)[1];
                            defaultValueString = str2.split(GetStat.PREFIX_2)[1];
                            defaultValueString = defaultValueString.replace("§f ", "").replace("(", "").replace(")", "");
                            if (defaultValueString.contains(GetStat.PREFIX_3))
                            {
                              defaultValueString = defaultValueString.split(GetStat.PREFIX_3)[0];
                            }
                            double defaultValue = Double.parseDouble(defaultValueString);
                            double addiValue = Double.parseDouble(MessageUtil.stripColor(str2.split(GetStat.PREFIX_3)[1].replace("+", "")));
                            double upgradeValue = Double.parseDouble(MessageUtil.stripColor(str2.split(GetStat.PREFIX_4)[1].replace("&b ", "").replace("+", "").replace(")", "")));

                            strTemp = GetStat.PREFIX_1 + "&b" + c1[0] + " : " + ((temp1 > 0) ? "+" : "") + round.format(temp1) + GetStat.PREFIX_2 + "&f (" + round
                                    .format(defaultValue) + GetStat.PREFIX_3 + "&a " + ((addiValue > 0) ? "+" : "") + round
                                    .format(addiValue) + GetStat.PREFIX_3 + GetStat.PREFIX_4 + "&b " + ((upgradeValue + temp1 - temp2 > 0) ? "+" : "") + round
                                    .format(upgradeValue + temp1 - temp2) + GetStat.PREFIX_4 + "&f)";
                          }
                          lores.set(j, MessageUtil.n2s(strTemp));
                          meta.setLore(lores);
                          item.setItemMeta(meta);
                          break;
                        }
                      }
                    }
                  }
                }
                if (!equal[i])
                {
                  for (int j = 0; j < optionsStr.size(); j++)
                  {
                    if (j == i)
                    {
                      String op = optionsStr.get(j);
                      String[] opCut = op.split(" : ");
                      op = opCut[1];
                      double value = 0D;
                      boolean containsPercent = op.contains("%");
                      if (containsPercent)
                      {
                        if (op.contains("+"))
                        {
                          try
                          {
                            value = Double.parseDouble(op.replace("%", "").replace("+", ""));
                          }
                          catch (Exception e)
                          {

                          }
                        }
                        else if (op.contains("-"))
                        {
                          try
                          {
                            value = Double.parseDouble(op.replace("%", ""));
                          }
                          catch (Exception e)
                          {
                          }
                        }
                      }
                      else
                      {
                        if (op.contains("+"))
                        {
                          try
                          {
                            value = Double.parseDouble(op.replace("+", ""));
                          }
                          catch (Exception e)
                          {
                          }
                        }
                        else if (op.contains("-"))
                        {
                          try
                          {
                            value = Double.parseDouble(op);
                          }
                          catch (Exception e)
                          {
                          }
                        }
                      }
                      if (containsPercent)
                      {
                        lores.add(MessageUtil.n2s(GetStat.PREFIX_1 + "&b" + optionsStr.get(j) + GetStat.PREFIX_2 + "&f (0%" + GetStat.PREFIX_4 + "&b " + ((value > 0) ? "+" : "") + round
                                .format(value) + "%" + GetStat.PREFIX_4 + "&f)"));
                      }
                      else
                      {
                        lores.add(MessageUtil.n2s(GetStat.PREFIX_1 + "&b" + optionsStr.get(j) + GetStat.PREFIX_2 + "&f (0" + GetStat.PREFIX_4 + "&b " + ((value > 0) ? "+" : "") + round
                                .format(value) + GetStat.PREFIX_4 + "&f)"));
                      }
                    }
                    meta.setLore(lores);
                    item.setItemMeta(meta);
                  }
                }
                for (int j = 0; j < equal.length; j++)
                {
                  equal[j] = false;
                }
              }
              item.setItemMeta(meta);
              player.getInventory().setItemInMainHand(item);
              Method.reinforceSound(player, Method.ReinforceSound.SUCCESS, use, com.jho5245.cucumbery.util.Method.ReinforceType.COMMAND);
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화에 &e성공&f하였습니다.");
              successTitle(player);
            }
            else if (bDestroy) // 파괴
            {
              destroyTitle(player);
              player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화에 &8실패&f하여 아이템이 &8파괴&f되었습니다.");
              Method.reinforceSound(player, Method.ReinforceSound.DESTROY, use, com.jho5245.cucumbery.util.Method.ReinforceType.COMMAND);
              destroyGG = true;
            }
            else if (bFail) // 실패 - 유지
            {
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화에 &4실패&f하였습니다.");
              Method.reinforceSound(player, Method.ReinforceSound.FAIL, use, com.jho5245.cucumbery.util.Method.ReinforceType.COMMAND);
              failTitle(player, false);
            }
            else // 실패 - 하락
            {
              failTitle(player, true);
              double[] options = getOptions(type, itemType, current - 1, reinforceRank);
              List<String> optionsStr = convertOptions(options, false);
              current--;
              ItemMeta meta = item.getItemMeta();
              List<String> lores = meta.getLore();
              switch (type)
              {
                case CUCUMBERFORCE:
                  lores.set(line, prefix + "오이포스 : " + current + " / " + max);
                  break;
                case STARFORCE:
                  lores.set(line, prefix + "스타포스 : " + current + " / " + max);
                  break;
                case SUPERIOR:
                  lores.set(line, prefix + "슈페리얼 : " + current + " / " + max);
                  break;
                case EBEBEB:
                  lores.set(line, prefix + "에베벱 : " + current + " / " + max);
                default:
                  break;
              }
              meta.setLore(lores);
              boolean hasDownTwiceValue = false;
              for (int i = 0; i < lores.size(); i++)
              {
                String lore = MessageUtil.stripColor(lores.get(i));
                if (lore.startsWith("연속 하락 횟수 : "))
                {
                  String prefix2 = lores.get(i).split("연속 하락 횟수 : ")[0];
                  try
                  {
                    hasDownTwiceValue = true;
                    int count = Integer.parseInt(lore.split("연속 하락 횟수 : ")[1]);
                    count++;
                    lores.set(i, MessageUtil.n2s(prefix2 + "연속 하락 횟수 : " + count));
                    downCount = count;
                    if (downCount == 2)
                    {
                      downTwice = true;
                    }
                    meta.setLore(lores);
                    break;
                  }
                  catch (Exception e)
                  {
                    hasDownTwiceValue = false;
                  }
                }
              }
              if (!hasDownTwiceValue)
              {
                lores.add(MessageUtil.n2s("&b연속 하락 횟수 : 1"));
                meta.setLore(lores);
              }
              boolean[] equal = new boolean[optionsStr.size()];
              for (int i = 0; i < equal.length; i++)
              {
                equal[i] = false;
              }
              for (int i = 0; i < optionsStr.size(); i++)
              {
                String str = optionsStr.get(i);
                if (str.contains(" : "))
                {
                  String[] c1 = str.split(" : ");
                  for (int j = 0; j < lores.size(); j++)
                  {
                    String str2 = lores.get(j);
                    if (str2.contains(" : "))
                    {
                      String[] c2 = str2.split(" : ");
                      if (c1[0].startsWith(MessageUtil.stripColor(c2[0].replace(GetStat.PREFIX_1, ""))) && c2[0].startsWith(GetStat.PREFIX_1))
                      {
                        if (c2[1].contains(GetStat.PREFIX_2))
                        {
                          c2[1] = c2[1].split(GetStat.PREFIX_2)[0];
                        }
                        if (c1[1].endsWith("%") && c2[1].endsWith("%"))
                        {
                          equal[i] = true;
                          double temp1 = 0D;
                          double temp2 = 0D;
                          String c1Str = c1[1].substring(0, c1[1].length() - 1);
                          String c2Str = c2[1].substring(0, c2[1].length() - 1);
                          if (c1Str.contains("-"))
                          {
                            try
                            {
                              temp1 -= Double.parseDouble(c1Str.substring(1, c1Str.length()));
                            }
                            catch (NumberFormatException exception)
                            {
                              equal[i] = false;
                            }
                          }
                          else if (c1Str.contains("+"))
                          {
                            try
                            {
                              temp1 += Double.parseDouble(c1Str.substring(1, c1Str.length()));
                            }
                            catch (NumberFormatException exception)
                            {
                              equal[i] = false;
                            }
                          }
                          if (c2Str.contains("-"))
                          {
                            try
                            {
                              temp2 -= Double.parseDouble(c2Str.substring(1, c2Str.length()));
                            }
                            catch (NumberFormatException exception)
                            {
                              equal[i] = false;
                            }
                          }
                          else if (c2Str.contains("+"))
                          {
                            try
                            {
                              temp2 += Double.parseDouble(c2Str.substring(1, c2Str.length()));
                            }
                            catch (NumberFormatException exception)
                            {
                              equal[i] = false;
                            }
                          }
                          temp1 += temp2;
                          String strTemp = "";
                          if (!str2.contains(GetStat.PREFIX_2)) // 기본값만 있을 때
                          {
                            strTemp = GetStat.PREFIX_1 + "&b" + c1[0] + " : " + ((temp1 > 0) ? "+" : "") + round.format(temp1) + "%" + GetStat.PREFIX_2 + "&f (" + round
                                    .format(temp2) + "%" + GetStat.PREFIX_4 + "&b " + ((temp1 - temp2 > 0) ? "+" : "") + round.format(temp1 - temp2) + "%" + GetStat.PREFIX_4 + "&f)";
                          }
                          else if (!str2.contains(GetStat.PREFIX_3) && str2.contains(GetStat.PREFIX_4)) // 업그레이드만 있을 때
                          {
                            String defaultValueString = str2.split(GetStat.PREFIX_2)[1];
                            defaultValueString = str2.split(GetStat.PREFIX_2)[1];
                            defaultValueString = defaultValueString.replace("§f ", "").replace("(", "").replace(")", "");
                            if (defaultValueString.contains(GetStat.PREFIX_4))
                            {
                              defaultValueString = defaultValueString.split(GetStat.PREFIX_4)[0];
                            }
                            double defaultValue = Double.parseDouble(defaultValueString.replace("%", ""));
                            double upgradeValue = Double.parseDouble(MessageUtil.stripColor(str2.split(GetStat.PREFIX_4)[1].replace("&b ", "").replace("%", "").replace("+", "").replace(")", "")));
                            strTemp = GetStat.PREFIX_1 + "&b" + c1[0] + " : " + ((temp1 > 0) ? "+" : "") + round.format(temp1) + "%" + GetStat.PREFIX_2 + "&f (" + round
                                    .format(defaultValue) + "%" + GetStat.PREFIX_4 + "&b " + ((upgradeValue + temp1 - temp2 > 0) ? "+" : "") + round
                                    .format(upgradeValue + temp1 - temp2) + "%" + GetStat.PREFIX_4 + "&f)";
                          }
                          else if (str2.contains(GetStat.PREFIX_3) && !str2.contains(GetStat.PREFIX_4)) // 추가 옵션만 있을 때
                          {
                            String defaultValueString = str2.split(GetStat.PREFIX_2)[1];
                            defaultValueString = str2.split(GetStat.PREFIX_2)[1];
                            defaultValueString = defaultValueString.replace("§f ", "").replace("(", "").replace(")", "");
                            if (defaultValueString.contains(GetStat.PREFIX_3))
                            {
                              defaultValueString = defaultValueString.split(GetStat.PREFIX_3)[0];
                            }
                            double defaultValue = Double.parseDouble(defaultValueString.replace("%", ""));
                            double addiValue = Double.parseDouble(MessageUtil.stripColor(str2.split(GetStat.PREFIX_3)[1].replace("%", "").replace("+", "")));

                            strTemp = GetStat.PREFIX_1 + "&b" + c1[0] + " : " + ((temp1 > 0) ? "+" : "") + round.format(temp1) + "%" + GetStat.PREFIX_2 + "&f (" + round
                                    .format(defaultValue) + "%" + GetStat.PREFIX_3 + "&a " + ((addiValue > 0) ? "+" : "") + round
                                    .format(addiValue) + "%" + GetStat.PREFIX_3 + GetStat.PREFIX_4 + "&b " + ((temp1 - temp2 > 0) ? "+" : "") + round.format(temp1 - temp2) + "%" + GetStat.PREFIX_4 + "&f)";
                          }
                          else // 추가 옵션, 업그레이드 둘 다 있을 때
                          {
                            String defaultValueString = str2.split(GetStat.PREFIX_2)[1];
                            defaultValueString = str2.split(GetStat.PREFIX_2)[1];
                            defaultValueString = defaultValueString.replace("§f ", "").replace("(", "").replace(")", "");
                            if (defaultValueString.contains(GetStat.PREFIX_3))
                            {
                              defaultValueString = defaultValueString.split(GetStat.PREFIX_3)[0];
                            }
                            double defaultValue = Double.parseDouble(defaultValueString.replace("%", ""));
                            double addiValue = Double.parseDouble(MessageUtil.stripColor(str2.split(GetStat.PREFIX_3)[1].replace("%", "").replace("+", "")));
                            double upgradeValue = Double.parseDouble(MessageUtil.stripColor(str2.split(GetStat.PREFIX_4)[1].replace("&b ", "").replace("%", "").replace("+", "").replace(")", "")));

                            strTemp = GetStat.PREFIX_1 + "&b" + c1[0] + " : " + ((temp1 > 0) ? "+" : "") + round.format(temp1) + "%" + GetStat.PREFIX_2 + "&f (" + round
                                    .format(defaultValue) + "%" + GetStat.PREFIX_3 + "&a " + ((addiValue > 0) ? "+" : "") + round
                                    .format(addiValue) + "%" + GetStat.PREFIX_3 + GetStat.PREFIX_4 + "&b " + ((upgradeValue + temp1 - temp2 > 0) ? "+" : "") + round
                                    .format(upgradeValue + temp1 - temp2) + "%" + GetStat.PREFIX_4 + "&f)";
                          }
                          lores.set(j, MessageUtil.n2s(strTemp));
                          meta.setLore(lores);
                          item.setItemMeta(meta);
                          break;
                        }
                        else if (!c1[1].endsWith("%") && !c2[1].endsWith("%"))
                        {
                          equal[i] = true;
                          double temp1 = 0D;
                          double temp2 = 0D;
                          String c1Str = c1[1];
                          String c2Str = c2[1];
                          if (c1Str.contains("-"))
                          {
                            try
                            {
                              temp1 -= Double.parseDouble(c1Str.substring(1, c1Str.length()));
                            }

                            catch (NumberFormatException exception)
                            {
                              equal[i] = false;
                            }
                          }
                          else if (c1Str.contains("+"))
                          {
                            try
                            {
                              temp1 += Double.parseDouble(c1Str.substring(1, c1Str.length()));
                            }

                            catch (NumberFormatException exception)
                            {
                              equal[i] = false;
                            }
                          }
                          if (c2Str.contains("-"))
                          {
                            try
                            {
                              temp2 -= Double.parseDouble(c2Str.substring(1, c2Str.length()));
                            }
                            catch (NumberFormatException exception)
                            {
                              equal[i] = false;
                            }
                          }
                          else if (c2Str.contains("+"))
                          {
                            try
                            {
                              temp2 += Double.parseDouble(c2Str.substring(1, c2Str.length()));
                            }

                            catch (NumberFormatException exception)
                            {
                              equal[i] = false;
                            }
                          }
                          temp1 += temp2;
                          String strTemp = "";
                          if (!str2.contains(GetStat.PREFIX_2)) // 기본값만 있을 때
                          {
                            strTemp = GetStat.PREFIX_1 + "&b" + c1[0] + " : " + ((temp1 > 0) ? "+" : "") + round.format(temp1) + GetStat.PREFIX_2 + "&f (" + round
                                    .format(temp2) + GetStat.PREFIX_4 + "&b " + ((temp1 - temp2 > 0) ? "+" : "") + round.format(temp1 - temp2) + GetStat.PREFIX_4 + "&f)";
                          }
                          else if (!str2.contains(GetStat.PREFIX_3) && str2.contains(GetStat.PREFIX_4)) // 업그레이드만 있을 때
                          {
                            String defaultValueString = str2.split(GetStat.PREFIX_2)[1];
                            defaultValueString = str2.split(GetStat.PREFIX_2)[1];
                            defaultValueString = defaultValueString.replace("§f ", "").replace("(", "").replace(")", "");
                            if (defaultValueString.contains(GetStat.PREFIX_4))
                            {
                              defaultValueString = defaultValueString.split(GetStat.PREFIX_4)[0];
                            }
                            double defaultValue = Double.parseDouble(defaultValueString);
                            double upgradeValue = Double.parseDouble(MessageUtil.stripColor(str2.split(GetStat.PREFIX_4)[1].replace("&b ", "").replace("+", "").replace(")", "")));
                            strTemp = GetStat.PREFIX_1 + "&b" + c1[0] + " : " + ((temp1 > 0) ? "+" : "") + round.format(temp1) + GetStat.PREFIX_2 + "&f (" + round
                                    .format(defaultValue) + GetStat.PREFIX_4 + "&b " + ((upgradeValue + temp1 - temp2 > 0) ? "+" : "") + round
                                    .format(upgradeValue + temp1 - temp2) + GetStat.PREFIX_4 + "&f)";
                          }
                          else if (str2.contains(GetStat.PREFIX_3) && !str2.contains(GetStat.PREFIX_4)) // 추가 옵션만 있을 때
                          {
                            String defaultValueString = str2.split(GetStat.PREFIX_2)[1];
                            defaultValueString = str2.split(GetStat.PREFIX_2)[1];
                            defaultValueString = defaultValueString.replace("§f ", "").replace("(", "").replace(")", "");
                            if (defaultValueString.contains(GetStat.PREFIX_3))
                            {
                              defaultValueString = defaultValueString.split(GetStat.PREFIX_3)[0];
                            }
                            double defaultValue = Double.parseDouble(defaultValueString);
                            double addiValue = Double.parseDouble(MessageUtil.stripColor(str2.split(GetStat.PREFIX_3)[1].replace("+", "")));
                            strTemp = GetStat.PREFIX_1 + "&b" + c1[0] + " : " + ((temp1 > 0) ? "+" : "") + round.format(temp1) + GetStat.PREFIX_2 + "&f (" + round
                                    .format(defaultValue) + GetStat.PREFIX_3 + "&a " + ((addiValue > 0) ? "+" : "") + round
                                    .format(addiValue) + GetStat.PREFIX_3 + GetStat.PREFIX_4 + "&b " + ((temp1 - temp2 > 0) ? "+" : "") + round.format(temp1 - temp2) + GetStat.PREFIX_4 + "&f)";
                          }
                          else // 추가 옵션, 업그레이드 둘 다 있을 때
                          {
                            String defaultValueString = str2.split(GetStat.PREFIX_2)[1];
                            defaultValueString = str2.split(GetStat.PREFIX_2)[1];
                            defaultValueString = defaultValueString.replace("§f ", "").replace("(", "").replace(")", "");
                            if (defaultValueString.contains(GetStat.PREFIX_3))
                            {
                              defaultValueString = defaultValueString.split(GetStat.PREFIX_3)[0];
                            }
                            double defaultValue = Double.parseDouble(defaultValueString);
                            double addiValue = Double.parseDouble(MessageUtil.stripColor(str2.split(GetStat.PREFIX_3)[1].replace("+", "")));
                            double upgradeValue = Double.parseDouble(MessageUtil.stripColor(str2.split(GetStat.PREFIX_4)[1].replace("&b ", "").replace("+", "").replace(")", "")));
                            strTemp = GetStat.PREFIX_1 + "&b" + c1[0] + " : " + ((temp1 > 0) ? "+" : "") + round.format(temp1) + GetStat.PREFIX_2 + "&f (" + round
                                    .format(defaultValue) + GetStat.PREFIX_3 + "&a " + ((addiValue > 0) ? "+" : "") + round
                                    .format(addiValue) + GetStat.PREFIX_3 + GetStat.PREFIX_4 + "&b " + ((upgradeValue + temp1 - temp2 > 0) ? "+" : "") + round
                                    .format(upgradeValue + temp1 - temp2) + GetStat.PREFIX_4 + "&f)";
                          }
                          lores.set(j, MessageUtil.n2s(strTemp));
                          meta.setLore(lores);
                          item.setItemMeta(meta);
                          break;
                        }
                      }
                    }
                  }
                }
                if (!equal[i])
                {
                  for (int j = 0; j < optionsStr.size(); j++)
                  {
                    if (j == i)
                    {
                      String op = optionsStr.get(j);
                      String[] opCut = op.split(" : ");
                      op = opCut[1];
                      double value = 0D;
                      boolean containsPercent = op.contains("%");
                      if (containsPercent)
                      {
                        if (op.contains("+"))
                        {
                          try
                          {
                            value = Double.parseDouble(op.replace("%", "").replace("+", ""));
                          }
                          catch (Exception e)
                          {
                          }
                        }
                        else if (op.contains("-"))
                        {
                          try
                          {
                            value = Double.parseDouble(op.replace("%", ""));
                          }
                          catch (Exception e)
                          {
                          }
                        }
                      }
                      else
                      {
                        if (op.contains("+"))
                        {
                          try
                          {
                            value = Double.parseDouble(op.replace("+", ""));
                          }
                          catch (Exception e)
                          {
                          }
                        }
                        else if (op.contains("-"))
                        {
                          try
                          {
                            value = Double.parseDouble(op);
                          }
                          catch (Exception e)
                          {
                          }
                        }
                      }
                      if (containsPercent)
                      {
                        lores.add(MessageUtil.n2s(GetStat.PREFIX_1 + "&b" + optionsStr.get(j) + GetStat.PREFIX_2 + "&f (0%" + GetStat.PREFIX_4 + "&b " + ((value > 0) ? "+" : "") + round
                                .format(value) + "%" + GetStat.PREFIX_4 + "&f)"));
                      }
                      else
                      {
                        lores.add(MessageUtil.n2s(GetStat.PREFIX_1 + "&b" + optionsStr.get(j) + GetStat.PREFIX_2 + "&f (0" + GetStat.PREFIX_4 + "&b " + ((value > 0) ? "+" : "") + round
                                .format(value) + GetStat.PREFIX_4 + "&f)"));
                      }
                    }
                    meta.setLore(lores);
                    item.setItemMeta(meta);
                  }
                }
                for (int j = 0; j < equal.length; j++)
                {
                  equal[j] = false;
                }
              }
              item.setItemMeta(meta);
              player.getInventory().setItemInMainHand(item);
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화에 &4실패&f하여 강화 단계가 &4하락&f하였습니다.");
              Method.reinforceSound(player, Method.ReinforceSound.FAIL, use, com.jho5245.cucumbery.util.Method.ReinforceType.COMMAND);
            }
            if (destroyGG)
            {
              return;
            }
            if (current >= max)
            {
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "장비가 한계까지 강화되어 더 이상 강화할 수 없습니다.");
              antiDest.put(player, false);
              return;
            }
            else
            {
              int reinforceCost2 = (int) Math.round(getCost(current, reinforceRank, type));
              if (antiDest != null && antiDest.containsKey(player) && antiDest.get(player))
              {
                reinforceCost2 = (int) Math.round(getCost(current, reinforceRank, type)) * 2;
              }
              if (!((type == ReinforceType.STARFORCE && current >= ANTI_DESTRUCTION_STARFORCE_FROM && current <= ANTI_DESTRUCTION_STARFORCE_TO) || (type == ReinforceType.CUCUMBERFORCE && current >= ANTI_DESTRUCTION_CUCUMBERFORCE_FROM && current <= ANTI_DESTRUCTION_CUCUMBERFORCE_TO)))
              {
                reinforceCost2 = (int) Math.round(getCost(current, reinforceRank, type));
              }
              if (reinforceCost2 > reinforcePoint)
              {
                MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화 포인트가 부족하여 더 이상 강화할 수 없습니다.");
                antiDest.put(player, false);
                return;
              }
              Variable.scrollReinforcing.add(player.getUniqueId());
              if (!((type == ReinforceType.STARFORCE && current >= ANTI_DESTRUCTION_STARFORCE_FROM && current <= ANTI_DESTRUCTION_STARFORCE_TO) || (type == ReinforceType.CUCUMBERFORCE && current >= ANTI_DESTRUCTION_CUCUMBERFORCE_FROM && current <= ANTI_DESTRUCTION_CUCUMBERFORCE_TO)))
              {
                antiDest.remove(player);
              }
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "----------------------------------------------------------------------------------------------");
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "현재 강화 포인트 : &e" + reinforcePoint + "&f, 요구 강화 포인트 : &e" + reinforceCost2);
              MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "" + current + "성 >> " + (current + 1) + "성");
              switch (type)
              {
                case CUCUMBERFORCE:
                  if (downTwice)
                  {
                    if (current >= 20 && current < 30)
                    {
                      Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&e★20성+★", "&f20성을 달성했습니다!\n&f오이포스 강화에 실패하여 강화 단계가\n&f하락하더라도 20성 밑으로는 하락하지 않습니다.");
                      player.sendMessage(msg);
                    }
                    else if (current >= 30 && current < 40)
                    {
                      Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&b★★30성+★★", "&f30성을 달성했습니다!\n&f오이포스 강화에 실패하여 강화 단계가\n&f하락하더라도 30성 밑으로는 하락하지 않습니다.");
                      player.sendMessage(msg);
                    }
                    else if (current >= 40 && current < 50)
                    {
                      Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&d★☆★40성+★☆★", "&f40성을 달성했습니다!\n&f오이포스 강화에 실패하여 강화 단계가\n&f하락하더라도 40성 밑으로는 하락하지 않습니다.");

                      player.sendMessage(msg);
                    }
                    MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&e연속 2회 하락 CHANCE TIME!");
                    MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화 성공 확률 : &e" + 100 + "%");
                    if (!chanceTime.contains(player))
                    {
                      chanceTime.add(player);
                    }
                  }
                  else if (antiDest != null && antiDest.containsKey(player) && antiDest.get(player))
                  {
                    double[] chance = getCucumberyInfo(current);
                    double success1 = chance[0], failKeep = chance[1], failDown = chance[2], destroy1 = chance[3];
                    if (failDown != 0 && failKeep == 0)
                    {
                      failDown += destroy1;
                    }
                    else if (failKeep != 0 && failDown == 0)
                    {
                      failKeep += destroy1;
                    }
                    else
                    {
                      failKeep += destroy1 / 2D;
                      failDown += destroy1 / 2D;
                    }
                    destroy1 = 0D;
                    if (current >= 20 && current < 30)
                    {
                      Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&e★20성+★", "&f20성을 달성했습니다!\n&f오이포스 강화에 실패하여 강화 단계가\n&f하락하더라도 20성 밑으로는 하락하지 않습니다.");
                      player.sendMessage(msg);
                    }
                    else if (current >= 30 && current < 40)
                    {
                      Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&b★★30성+★★", "&f30성을 달성했습니다!\n&f오이포스 강화에 실패하여 강화 단계가\n&f하락하더라도 30성 밑으로는 하락하지 않습니다.");
                      player.sendMessage(msg);
                    }
                    else if (current >= 40 && current < 50)
                    {
                      Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&d★☆★40성+★☆★", "&f40성을 달성했습니다!\n&f오이포스 강화에 실패하여 강화 단계가\n&f하락하더라도 40성 밑으로는 하락하지 않습니다.");
                      player.sendMessage(msg);
                    }
                    else if (current == 50)
                    {
                      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&4★&c☆&6★&e☆&a★&25&b0&3성&9★&1☆&5★&d☆&4★");
                    }
                    sendChanceMessage(player, success, failKeep, failDown, destroy);
                  }
                  else
                  {
                    checkCucumberForceReinforce(player, current);
                  }
                  break;
                case STARFORCE:
                  if (downTwice)
                  {
                    if (current >= 10 && current < 15)
                    {
                      Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&e★10성+★", "&f10성을 달성했습니다!\n&f스타포스 강화에 실패하여 강화 단계가\n&f하락하더라도 10성 밑으로는 하락하지 않습니다.");
                      player.sendMessage(msg);
                    }
                    else if (current >= 15 && current < 20)
                    {
                      Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&b★★15성+★★", "&f15성을 달성했습니다!\n&f스타포스 강화에 실패하여 강화 단계가\n&f하락하더라도 15성 밑으로는 하락하지 않습니다.");
                      player.sendMessage(msg);
                    }
                    else if (current >= 20 && current < 25)
                    {
                      Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&d★☆★20성+★☆★", "&f20성을 달성했습니다!\n&f스타포스 강화에 실패하여 강화 단계가\n&f하락하더라도 20성 밑으로는 하락하지 않습니다.");
                      player.sendMessage(msg);
                    }
                    MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&e연속 2회 하락 CHANCE TIME!");
                    MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화 성공 확률 : &e" + 100 + "%");
                    if (!chanceTime.contains(player))
                    {
                      chanceTime.add(player);
                    }
                  }
                  else if (antiDest != null && antiDest.containsKey(player) && antiDest.get(player))
                  {
                    double[] chance = getStarForceInfo(current);
                    double success1 = chance[0], failKeep = chance[1], failDown = chance[2], destroy1 = chance[3];
                    if (failDown != 0 && failKeep == 0)
                    {
                      failDown += destroy1;
                    }
                    else if (failKeep != 0 && failDown == 0)
                    {
                      failKeep += destroy1;
                    }
                    else
                    {
                      failKeep += destroy1 / 2D;
                      failDown += destroy1 / 2D;
                    }
                    destroy1 = 0D;
                    if (current >= 10 && current < 15)
                    {
                      Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&e★10성+★", "&f10성을 달성했습니다!\n&f스타포스 강화에 실패하여 강화 단계가\n&f하락하더라도 10성 밑으로는 하락하지 않습니다.");
                      player.sendMessage(msg);
                    }
                    else if (current >= 15 && current < 20)
                    {
                      Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&b★★15성+★★", "&f15성을 달성했습니다!\n&f스타포스 강화에 실패하여 강화 단계가\n&f하락하더라도 15성 밑으로는 하락하지 않습니다.");
                      player.sendMessage(msg);
                    }
                    else if (current >= 20 && current < 25)
                    {
                      Component msg = ComponentUtil.create(Prefix.INFO_REINFORCE + "&d★☆★20성+★☆★", "&f20성을 달성했습니다!\n&f스타포스 강화에 실패하여 강화 단계가\n&f하락하더라도 20성 밑으로는 하락하지 않습니다.");
                      player.sendMessage(msg);
                    }
                    else if (current == 25)
                    {
                      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&4★&c☆&6★&e☆&a★&22&b5&3성&9★&1☆&5★&d☆&4★");
                    }
                    sendChanceMessage(player, success, failKeep, failDown, destroy);
                  }
                  else
                  {
                    checkReinforce(player, current);
                  }
                  break;
                case SUPERIOR:
                  if (downTwice)
                  {
                    MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&e연속 2회 하락 CHANCE TIME!");
                    MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화 성공 확률 : &e" + 100 + "%");
                    if (!chanceTime.contains(player))
                    {
                      chanceTime.add(player);
                    }
                  }
                  else
                  {
                    checkSuperiorReinforce(player, current);
                  }
                  break;
                case EBEBEB:
                  if (downTwice)
                  {
                    MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "&e연속 2회 하락 CHANCE TIME!");
                    MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "강화 성공 확률 : &e" + 100 + "%");
                    if (!chanceTime.contains(player))
                    {
                      chanceTime.add(player);
                    }
                  }
                  else
                  {
                    checkEBEBEBReinforce(player, current);
                  }
                default:
                  break;
              }
              List<String> options = convertOptions(getOptions(type, itemType, current, reinforceRank), true);
              for (String option : options)
              {
                MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, option);
              }
              if ((type == ReinforceType.STARFORCE && current >= ANTI_DESTRUCTION_STARFORCE_FROM && current <= ANTI_DESTRUCTION_STARFORCE_TO) || (type == ReinforceType.CUCUMBERFORCE && current >= ANTI_DESTRUCTION_CUCUMBERFORCE_FROM && current <= ANTI_DESTRUCTION_CUCUMBERFORCE_TO))
              {
                if (antiDest == null || !antiDest.containsKey(player) || !antiDest.get(player))
                {
                  Component a = ComponentUtil.create("&9[&3강&e화&9] &f강화를 시작하려면 ");
                  Component b = ComponentUtil.create("&a\"강화 시작\"", "&f클릭하면 강화를 시작합니다.\n&c이 작업은 되돌릴 수 없습니다.", ClickEvent.Action.RUN_COMMAND, Constant.REINFORCE_START);
                  Component c = ComponentUtil.create("&f을, 강화를 중지하려면 ");
                  Component d = ComponentUtil.create("&c\"강화 중지\"", "&f클릭하면 강화를 중지합니다.", ClickEvent.Action.RUN_COMMAND, Constant.REINFORCE_QUIT);
                  Component e = ComponentUtil.create("&f를 채팅창을 열고 클릭해주세요. 파괴 방지를 사용하려면 ");
                  Component f = ComponentUtil.create("&e\"여기\"", "&f클릭하면 파괴 방지를 사용합니다.", ClickEvent.Action.RUN_COMMAND, Constant.REINFORCE_USE_ANTI_DESTRUCTION);
                  Component g = ComponentUtil.create("&f를 클릭하세요.");
                  if (type == ReinforceType.STARFORCE)
                  {
                    f = ComponentUtil.create("&e\"여기\"", "&f클릭하면 파괴 방지를 사용합니다.\n&f파괴 방지 기능은 12성에서 16성 사이의 아이템으로\n&f강화를 시도할때만 사용할 수 있습니다.", ClickEvent.Action.RUN_COMMAND,
                            Constant.REINFORCE_USE_ANTI_DESTRUCTION);
                  }
                  else if (type == ReinforceType.CUCUMBERFORCE)
                  {
                    f = ComponentUtil.create("&e\"여기\"", "&f클릭하면 파괴 방지를 사용합니다.\n&f파괴 방지 기능은 24성에서 33성 사이의 아이템으로\n&f강화를 시도할때만 사용할 수 있습니다.", ClickEvent.Action.RUN_COMMAND,
                            Constant.REINFORCE_USE_ANTI_DESTRUCTION);
                  }
                  MessageUtil.sendMessage(player, a, b, c, d, e, f, g);
                }
                else
                {
                  Component a = ComponentUtil.create("&9[&3강&e화&9] &f강화를 시작하려면 ");
                  Component b = ComponentUtil.create("&a\"강화 시작\"", "&f클릭하면 강화를 시작합니다.\n&c이 작업은 되돌릴 수 없습니다.", ClickEvent.Action.RUN_COMMAND, Constant.REINFORCE_START);
                  Component c = ComponentUtil.create("&f을, 강화를 중지하려면 ");
                  Component d = ComponentUtil.create("&c\"강화 중지\"", "&f클릭하면 강화를 중지합니다.", ClickEvent.Action.RUN_COMMAND, Constant.REINFORCE_QUIT);
                  Component e = ComponentUtil.create("&f를 채팅창을 열고 클릭해주세요. 파괴 방지를 사용하지 않으려면 ");
                  Component f = ComponentUtil.create("&b\"여기\"", "&f클릭하면 파괴 방지를 사용하지 않습니다.", ClickEvent.Action.RUN_COMMAND, Constant.REINFORCE_DO_NOT_USE_ANTI_DESTRUCTION);
                  Component g = ComponentUtil.create("&f를 클릭하세요.");
                  MessageUtil.sendMessage(player, a, b, c, d, e, f, g);
                }
              }
              else
              {
                Component a = ComponentUtil.create("&9[&3강&e화&9] &f강화를 시작하려면 ");
                Component b = ComponentUtil.create("&a\"강화 시작\"", "&f클릭하면 강화를 시작합니다.\n&c이 작업은 되돌릴 수 없습니다.", ClickEvent.Action.RUN_COMMAND, Constant.REINFORCE_START);
                Component c = ComponentUtil.create("&f을, 강화를 중지하려면 ");
                Component d = ComponentUtil.create("&c\"강화 중지\"", "&f클릭하면 강화를 중지합니다.", ClickEvent.Action.RUN_COMMAND, Constant.REINFORCE_QUIT);
                Component e = ComponentUtil.create("&f를 채팅창을 열고 클릭해주세요.");
                MessageUtil.sendMessage(player, a, b, c, d, e);
              }
            }
          }
        }, 60L);
      }
      else
      {
        MessageUtil.wrongArg(sender, 1, args);
        MessageUtil.info(player, "/" + label + usage);
        return true;
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

  private void successTitle(Player player)
  {
    Location loc = player.getLocation().add(player.getLocation().getDirection().multiply(4D)).add(0D, 1.7D, 0D);
    player.spawnParticle(Particle.FIREWORKS_SPARK, loc, 500, 0, 0, 0, 1);
    MessageUtil.sendTitle(player, "&e&n★", "", 0, 10, 0);
    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () ->
    {
      MessageUtil.sendTitle(player, "&e&n★S", "", 0, 10, 0);
    }, 1L);
    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () ->
    {
      MessageUtil.sendTitle(player, "&e&n★SU", "", 0, 10, 0);
    }, 2L);
    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () ->
    {
      MessageUtil.sendTitle(player, "&e&n★SUC", "", 0, 10, 0);
    }, 3L);
    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () ->
    {
      MessageUtil.sendTitle(player, "&e&n★SUCC", "", 0, 10, 0);
    }, 4L);
    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () ->
    {
      MessageUtil.sendTitle(player, "&e&n★SUCCE", "", 0, 10, 0);
    }, 5L);
    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () ->
    {
      MessageUtil.sendTitle(player, "&e&n★SUCCES", "", 0, 10, 0);
    }, 6L);
    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () ->
    {
      MessageUtil.sendTitle(player, "&e&n★SUCCESS", "", 0, 10, 0);
    }, 7L);
    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () ->
    {
      MessageUtil.sendTitle(player, "&e&n★SUCCESS★", "", 0, 10, 0);
    }, 8L);
    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () ->
    {
      MessageUtil.sendTitle(player, "&f&n☆&e&nSUCCESS☆", "", 0, 10, 0);
    }, 13L);
    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () ->
    {
      MessageUtil.sendTitle(player, "&f&n☆S&e&nUCCESS☆", "", 0, 10, 0);
    }, 14L);
    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () ->
    {
      MessageUtil.sendTitle(player, "&f&n☆SU&e&nCCESS☆", "", 0, 10, 0);
    }, 15L);
    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () ->
    {
      MessageUtil.sendTitle(player, "&f&n★SUC&e&nCESS★", "", 0, 10, 0);
    }, 16L);
    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () ->
    {
      MessageUtil.sendTitle(player, "&f&n★SUCC&e&nESS★", "", 0, 10, 0);
    }, 17L);
    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () ->
    {
      MessageUtil.sendTitle(player, "&f&n★SUCCE&e&nSS★", "", 0, 10, 0);
    }, 18L);
    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () ->
    {
      MessageUtil.sendTitle(player, "&f&n☆SUCCES&e&nS☆", "", 0, 10, 0);
    }, 19L);
    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () ->
    {
      MessageUtil.sendTitle(player, "&f&n☆SUCCESS&e&n☆", "", 0, 10, 0);
    }, 20L);
    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () ->
    {
      MessageUtil.sendTitle(player, "&f&n☆SUCCESS☆", "", 0, 10, 0);
    }, 21L);
    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () ->
    {
      MessageUtil.sendTitle(player, "&e&n★SUCCESS★", "", 0, 20, 0);
    }, 26L);
  }

  private void failTitle(Player player, boolean particle)
  {
    MessageUtil.sendTitle(player, "&4&oFAILED", "", 0, 30, 15);
    if (particle)
    {
      Location loc = player.getLocation().add(player.getLocation().getDirection().multiply(4D)).add(0D, 1.7D, 0D);
      player.spawnParticle(Particle.ITEM_CRACK, loc, 10, 0, 0, 0, 0.3, player.getInventory().getItemInMainHand());
    }
  }

  private void destroyTitle(Player player)
  {
    MessageUtil.sendTitle(player, "&8&oDESTROYED", "", 0, 30, 15);
    Location loc = player.getLocation().add(player.getLocation().getDirection().multiply(4D)).add(0D, 1.7D, 0D);
    Bukkit.getServer().getWorld(loc.getWorld().getName()).spawnParticle(Particle.ITEM_CRACK, loc, 500, 0, 0, 0, 0.3, player.getInventory().getItemInMainHand());
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
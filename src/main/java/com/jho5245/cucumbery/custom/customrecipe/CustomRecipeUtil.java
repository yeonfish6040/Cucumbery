package com.jho5245.cucumbery.custom.customrecipe;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.commands.debug.CommandWhatIs;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.itemlore.ItemLoreView;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.TranslatableKeyParser;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.CreateItemStack;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

public class CustomRecipeUtil
{
  public static final int REQUIREMENT_AMOUNT = 40;

  /**
   * 카테고리 추가 접근 조건 혹은 아이템 추가 제작 조건 설명을 추가합니다. 카테고리 추가 접근 조건 설명을 추가할 땐 recipe 값을 null로 하면 됩니다.
   */
  @SuppressWarnings("all")
  public static void createRequirementLore(List<Component> requirementsLore, Player player, FileConfiguration config, String category, String recipe, boolean[] requirements)
  {
    UUID uuid = player.getUniqueId();

    if (requirementsLore == null)
    {
      requirementsLore = new ArrayList<>();
    }
    boolean isForCategory = recipe == null;
    String configSection = (isForCategory ? "" : "recipes." + recipe + ".") + "extra.";
    int requirementOrder = 0;
    String prefixColor = "gb128,255;";
    // 제작 성공 확률
    double chance = config.getDouble(configSection + "chance");
    if (chance > 0d)
    {
      String finalValue = prefixColor + "제작 성공 확률 : rg255,204;" + Constant.Sosu2.format(chance) + "%";
      requirementsLore.add(ComponentUtil.create(finalValue));
    }

    long requireTimeToCraft = config.getLong(configSection + "crafting-time");
    prefixColor = "rgb225,158,83;";

    if (requireTimeToCraft > 0)
    {
      YamlConfiguration playerCraftingTimeConfig = Variable.craftingTime.get(uuid);
      long playerCraftingTime = playerCraftingTimeConfig == null ? 0 : playerCraftingTimeConfig.getLong("crafting-time." + category + "." + recipe);
      if (playerCraftingTime > 0)
      {
        long currentTime = System.currentTimeMillis();
        if (currentTime > playerCraftingTime)
        {
          String finalValue = prefixColor + "남은 제작 시간 : gb255,84;제작 완료!";
          requirementsLore.add(ComponentUtil.create(finalValue));
        }
        else
        {
          String finalValue = prefixColor + "남은 제작 시간 : rg255,204;" + Method.timeFormatMilli(playerCraftingTime - currentTime, false);
          requirementsLore.add(ComponentUtil.create(finalValue));
          if (Cucumbery.using_Vault_Economy)
          {
            double skipCost = config.getDouble(configSection + "crafting-time-skip.cost");
            if (skipCost > 0)
            {
              String skipPermission = config.getString(configSection + "crafting-time-skip.permission");
              boolean playerHasSkipPermission = true;
              if (skipPermission != null)
              {
                playerHasSkipPermission = player.hasPermission(skipPermission);
                finalValue = prefixColor + "제작 시간 스킵에 필요한 퍼미션 노드 : " + (playerHasSkipPermission ? ("rgb0,255,84;" + skipPermission + " (권한 있음)") : ("rgb255,0,84;" + skipPermission + " (권한 없음)"));
                requirementsLore.add(ComponentUtil.create(finalValue));
              }
              if (playerHasSkipPermission)
              {
                boolean timeRelative = config.getBoolean(configSection + "crafting-time-skip.time-relative");
                double finalCost = timeRelative ? skipCost * (playerCraftingTime - currentTime) / requireTimeToCraft : skipCost;
                double playerMoney = Cucumbery.eco.getBalance(player);
                finalValue = getPercentColor(playerMoney / finalCost) + Constant.Jeongsu.format(playerMoney) + "원&7 / rgb0,255,84;" + Constant.Jeongsu.format(finalCost) + "원&7을 지불하여 제작 시간 스킵 가능";
                requirementsLore.add(ComponentUtil.create(finalValue));
              }
            }
          }
        }
      }
      else
      {
        String finalValue = prefixColor + "제작에 필요한 시간 : rg255,204;" + Method.timeFormatMilli(requireTimeToCraft, false);
        requirementsLore.add(ComponentUtil.create(finalValue));
      }
    }

    // 제작 주기
    long craftingTimeInterval = config.getLong(configSection + "crafting-time-interval");
    if (craftingTimeInterval >= 1000)
    {
      YamlConfiguration configLastCraft = Variable.lastCraftsLog.get(uuid);
      long lastCraftTime = configLastCraft == null ? 0 : configLastCraft.getLong("last-crafts." + category + "." + recipe);
      long currentTime = System.currentTimeMillis();
      long timeDifference = (long) (Math.ceil((currentTime - lastCraftTime) / 1000d) * 1000d);
      long nextAvailableTime = lastCraftTime + craftingTimeInterval;
      boolean passRequire = timeDifference >= craftingTimeInterval;
      String finalValue = prefixColor + "제작 가능 주기 : rg255,204;" + Method.timeFormatMilli(craftingTimeInterval, false);
      long displayTimeMs = (long) (Math.ceil(nextAvailableTime / 1000d) * 1000d) - currentTime - 1000L;
      if (!passRequire)
      {
        finalValue += prefixColor + " (" + getPercentColor(timeDifference * 1d / craftingTimeInterval) + Method.timeFormatMilli(displayTimeMs, false) + " 후에 제작 가능" + prefixColor + ")";
      }
      requirementsLore.add(ComponentUtil.create(finalValue));
      requirements[requirementOrder] = passRequire;
      requirementOrder++;
    }

    // 요구 퍼미션 노드
    String permission = config.getString(configSection + "permissions.base-permission");
    prefixColor = "rgb203,211,44;";
    if (permission != null)
    {
      boolean hasPermission = player.hasPermission(permission);
      String finalValue = prefixColor + "요구 퍼미션 노드 : " + (hasPermission ? ("rgb0,255,84;" + permission + " (권한 있음)") : ("rgb255,0,84;" + permission + " (권한 없음)"));
      requirementsLore.add(ComponentUtil.create(finalValue));
      requirements[requirementOrder] = hasPermission;
      requirementOrder++;
    }

    // 요구 레벨
    int playerValueInt = player.getLevel();
    int requireCostInt = config.getInt(configSection + "levelcost");
    // 레벨 조건
    String requireString = "level";
    prefixColor = "rgb61,255,4;";
    boolean requireMinExists = config.contains(configSection + requireString + ".min");
    boolean requireMaxExists = config.contains(configSection + requireString + ".max");
    if (requireCostInt > 0)
    {
      String finalValue = prefixColor + "요구 레벨 : " + getPercentColor(1d * playerValueInt / requireCostInt) + Constant.Jeongsu.format(playerValueInt) + " &7/rgb0,255,84; " + Constant.Jeongsu.format(
              requireCostInt);
      requirementsLore.add(ComponentUtil.create(finalValue));
      requirements[requirementOrder] = playerValueInt >= requireCostInt;
      requirementOrder++;
    }
    if (requireMinExists || requireMaxExists)
    {
      int requireMin = config.getInt(configSection + requireString + ".min");
      int requireMax = config.getInt(configSection + requireString + ".max");
      String display = prefixColor + "레벨 조건 : ";
      String playerValueString = Constant.Jeongsu.format(playerValueInt);
      String requireMinString = Constant.Jeongsu.format(requireMin);
      String requireMaxString = Constant.Jeongsu.format(requireMax);
      // 이상, 이하 문자열 지정
      requireMinString = requireMinExists && requireMin != 0d ? (requireMinString + " 이상 ") : "";
      requireMaxString = requireMaxExists ? (requireMaxString + " 이하") : "";
      // 조건 만족 테스트
      boolean passRequire = (requireMinExists && requireMaxExists && playerValueInt >= requireMin && playerValueInt <= requireMax) ||
              (requireMinExists && !requireMaxExists && playerValueInt >= requireMin) ||
              (!requireMinExists && requireMaxExists && playerValueInt <= requireMax) ||
              (!requireMinExists && !requireMaxExists);
      // 조건 만족 등호 문자열
      String equalsString = passRequire ? "=" : "≠";
      // 조건 만족 문자열 색상
      String color = getRadiusPercentColor(playerValueInt, requireMinExists, requireMaxExists, requireMin, requireMax);
      // 최종적으로 설명에 추가될 문자열
      String finalValueString = display + color + playerValueString + " &7" + equalsString + "rgb0,255,84; " + requireMinString;
      // 최대, 최소 조건이 동일하면 " 이상 " 문자 제거
      if (requireMinExists && requireMaxExists && requireMin == requireMax)
      {
        finalValueString = finalValueString.replace(" 이상 ", "");
      }
      // 동일하지 않을 경우 이상, 이하 문구 전부 추가
      else if (!requireMinExists || requireMin != 0d || requireMaxExists)
      {
        finalValueString += requireMaxString;
      }
      // 어떤 경우도 아닐 경우 추가하지 않음
      else
      {
        finalValueString = "";
      }
      if (!finalValueString.equals(""))
      {
        requirementsLore.add(ComponentUtil.create(finalValueString));
      }
      // 조건을 만족하면 true를 넣고 다음 배열의 값 대기
      requirements[requirementOrder] = passRequire;
      requirementOrder++;
    }

    // 요구 음식 포인트
    playerValueInt = player.getFoodLevel();
    requireCostInt = config.getInt(configSection + "foodlevelcost");
    // 음식 포인트 조건
    requireString = "foodlevel";
    prefixColor = "rgb219,155,122;";
    requireMinExists = config.contains(configSection + requireString + ".min");
    requireMaxExists = config.contains(configSection + requireString + ".max");
    if (requireCostInt > 0)
    {
      String finalValue = prefixColor + "음식 포인트 비용 : " + getPercentColor(1d * playerValueInt / requireCostInt) + Constant.Jeongsu.format(playerValueInt) + " &7/rgb0,255,84; " + Constant.Jeongsu.format(
              requireCostInt);
      requirementsLore.add(ComponentUtil.create(finalValue));
      requirements[requirementOrder] = playerValueInt >= requireCostInt;
      requirementOrder++;
    }
    if (requireMinExists || requireMaxExists)
    {
      int requireMin = config.getInt(configSection + requireString + ".min");
      int requireMax = config.getInt(configSection + requireString + ".max");
      String display = prefixColor + "음식 포인트 조건 : ";
      String playerValueString = Constant.Jeongsu.format(playerValueInt);
      String requireMinString = Constant.Jeongsu.format(requireMin);
      String requireMaxString = Constant.Jeongsu.format(requireMax);
      // 이상, 이하 문자열 지정
      requireMinString = requireMinExists && requireMin != 0d ? (requireMinString + " 이상 ") : "";
      requireMaxString = requireMaxExists ? (requireMaxString + " 이하") : "";
      // 조건 만족 테스트
      boolean passRequire = (requireMinExists && requireMaxExists && playerValueInt >= requireMin && playerValueInt <= requireMax) ||
              (requireMinExists && !requireMaxExists && playerValueInt >= requireMin) ||
              (!requireMinExists && requireMaxExists && playerValueInt <= requireMax) ||
              (!requireMinExists && !requireMaxExists);
      // 조건 만족 등호 문자열
      String equalsString = passRequire ? "=" : "≠";
      // 조건 만족 문자열 색상
      String color = getRadiusPercentColor(playerValueInt, requireMinExists, requireMaxExists, requireMin, requireMax);
      // 최종적으로 설명에 추가될 문자열
      String finalValue = display + color + playerValueString + " &7" + equalsString + "rgb0,255,84; " + requireMinString;
      // 최대, 최소 조건이 동일하면 " 이상 " 문자 제거
      if (requireMinExists && requireMaxExists && requireMin == requireMax)
      {
        finalValue = finalValue.replace(" 이상 ", "");
      }
      // 동일하지 않을 경우 이상, 이하 문구 전부 추가
      else if (!requireMinExists || requireMin != 0d || requireMaxExists)
      {
        finalValue += requireMaxString;
      }
      // 어떤 경우도 아닐 경우 추가하지 않음
      else
      {
        finalValue = "";
      }
      if (!finalValue.equals(""))
      {
        requirementsLore.add(ComponentUtil.create(finalValue));
      }
      requirements[requirementOrder] = passRequire;
      requirementOrder++;
    }

    // 포화도 비용
    double playerValueDouble = player.getSaturation();
    double requireCostDouble = config.getDouble(configSection + "saturationcost");
    // 포화도 조건
    requireString = "saturation";
    prefixColor = "rgb191,255,85;";
    requireMinExists = config.contains(configSection + requireString + ".min");
    requireMaxExists = config.contains(configSection + requireString + ".max");
    if (requireCostDouble > 0)
    {
      String finalValue = prefixColor +
              "포화도 비용 : " +
              getPercentColor(playerValueDouble / requireCostDouble) +
              Constant.Jeongsu.format(playerValueDouble) +
              " &7/rgb0,255,84; " +
              Constant.Jeongsu.format(requireCostDouble);
      requirementsLore.add(ComponentUtil.create(finalValue));
      requirements[requirementOrder] = playerValueDouble >= requireCostDouble;
      requirementOrder++;
    }
    if (requireMinExists || requireMaxExists)
    {
      double requireMin = config.getDouble(configSection + requireString + ".min");
      double requireMax = config.getDouble(configSection + requireString + ".max");
      String display = prefixColor + "포화도 조건 : ";
      String playerValueString = Constant.Sosu2.format(playerValueDouble);
      String requireMinString = Constant.Sosu2.format(requireMin);
      String requireMaxString = Constant.Sosu2.format(requireMax);
      // 이상, 이하 문자열 지정
      requireMinString = requireMinExists && requireMin != 0d ? (requireMinString + " 이상 ") : "";
      requireMaxString = requireMaxExists ? (requireMaxString + " 이하") : "";
      // 조건 만족 테스트
      boolean passRequire = (requireMinExists && requireMaxExists && playerValueDouble >= requireMin && playerValueDouble <= requireMax) ||
              (requireMinExists && !requireMaxExists && playerValueDouble >= requireMin) ||
              (!requireMinExists && requireMaxExists && playerValueDouble <= requireMax) ||
              (!requireMinExists && !requireMaxExists);
      // 조건 만족 등호 문자열
      String equalsString = passRequire ? "=" : "≠";
      // 조건 만족 문자열 색상
      String color = getRadiusPercentColor(playerValueDouble, requireMinExists, requireMaxExists, requireMin, requireMax);
      // 최종적으로 설명에 추가될 문자열
      String finalValue = display + color + playerValueString + " &7" + equalsString + "rgb0,255,84; " + requireMinString;
      // 최대, 최소 조건이 동일하면 " 이상 " 문자 제거
      if (requireMinExists && requireMaxExists && requireMin == requireMax)
      {
        finalValue = finalValue.replace(" 이상 ", "");
      }
      // 동일하지 않을 경우 이상, 이하 문구 전부 추가
      else if (!requireMinExists || requireMin != 0d || requireMaxExists)
      {
        finalValue += requireMaxString;
      }
      // 어떤 경우도 아닐 경우 추가하지 않음
      else
      {
        finalValue = "";
      }
      if (!finalValue.equals(""))
      {
        requirementsLore.add(ComponentUtil.create(finalValue));
      }
      requirements[requirementOrder] = passRequire;
      requirementOrder++;
    }

    // HP 비용
    playerValueDouble = player.getHealth();
    requireCostDouble = config.getDouble(configSection + "healthcost");
    // HP 조건
    requireString = "health";
    prefixColor = "rgb255,151,125;";
    requireMinExists = config.contains(configSection + requireString + ".min");
    requireMaxExists = config.contains(configSection + requireString + ".max");
    if (requireCostDouble > 0)
    {
      String finalValue = prefixColor +
              "HP 비용 : " +
              getPercentColor(playerValueDouble / requireCostDouble) +
              Constant.Jeongsu.format(playerValueDouble) +
              " &7/rgb0,255,84; " +
              Constant.Jeongsu.format(requireCostDouble);
      requirementsLore.add(ComponentUtil.create(finalValue));
      requirements[requirementOrder] = playerValueDouble >= requireCostDouble;
      requirementOrder++;
    }
    if (requireMinExists || requireMaxExists)
    {
      double requireMin = config.getDouble(configSection + requireString + ".min");
      double requireMax = config.getDouble(configSection + requireString + ".max");
      String display = prefixColor + "HP 조건 : ";
      String playerValueString = Constant.Sosu2.format(playerValueDouble);
      String requireMinString = Constant.Sosu2.format(requireMin);
      String requireMaxString = Constant.Sosu2.format(requireMax);
      // 이상, 이하 문자열 지정
      requireMinString = requireMinExists && requireMin != 0d ? (requireMinString + " 이상 ") : "";
      requireMaxString = requireMaxExists ? (requireMaxString + " 이하") : "";
      // 조건 만족 테스트
      boolean passRequire = (requireMinExists && requireMaxExists && playerValueDouble >= requireMin && playerValueDouble <= requireMax) ||
              (requireMinExists && !requireMaxExists && playerValueDouble >= requireMin) ||
              (!requireMinExists && requireMaxExists && playerValueDouble <= requireMax) ||
              (!requireMinExists && !requireMaxExists);
      // 조건 만족 등호 문자열
      String equalsString = passRequire ? "=" : "≠";
      // 조건 만족 문자열 색상
      String color = getRadiusPercentColor(playerValueDouble, requireMinExists, requireMaxExists, requireMin, requireMax);
      // 최종적으로 설명에 추가될 문자열
      String finalValue = display + color + playerValueString + " &7" + equalsString + "rgb0,255,84; " + requireMinString;
      // 최대, 최소 조건이 동일하면 " 이상 " 문자 제거
      if (requireMinExists && requireMaxExists && requireMin == requireMax)
      {
        finalValue = finalValue.replace(" 이상 ", "");
      }
      // 동일하지 않을 경우 이상, 이하 문구 전부 추가
      else if (!requireMinExists || requireMin != 0d || requireMaxExists)
      {
        finalValue += requireMaxString;
      }
      // 어떤 경우도 아닐 경우 추가하지 않음
      else
      {
        finalValue = "";
      }
      if (!finalValue.equals(""))
      {
        requirementsLore.add(ComponentUtil.create(finalValue));
      }
      requirements[requirementOrder] = passRequire;
      requirementOrder++;
    }

    // MHP 비용
    playerValueDouble = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
    requireCostDouble = config.getDouble(configSection + "maxhealthcost");
    // MHP 조건
    requireString = "maxhealth";
    prefixColor = "rgb255,151,125;";
    requireMinExists = config.contains(configSection + requireString + ".min");
    requireMaxExists = config.contains(configSection + requireString + ".max");
    if (requireCostDouble > 0)
    {
      String finalValue = prefixColor +
              "최대 순수 HP 비용 : " +
              getPercentColor(playerValueDouble / requireCostDouble) +
              Constant.Jeongsu.format(playerValueDouble) +
              " &7/rgb0,255,84; " +
              Constant.Jeongsu.format(requireCostDouble);
      requirementsLore.add(ComponentUtil.create(finalValue));
      requirements[requirementOrder] = playerValueDouble >= requireCostDouble;
      requirementOrder++;
    }
    if (requireMinExists || requireMaxExists)
    {
      playerValueDouble = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
      double requireMin = config.getDouble(configSection + requireString + ".min");
      double requireMax = config.getDouble(configSection + requireString + ".max");
      String display = prefixColor + "최대 HP 조건 : ";
      String playerValueString = Constant.Sosu2.format(playerValueDouble);
      String requireMinString = Constant.Sosu2.format(requireMin);
      String requireMaxString = Constant.Sosu2.format(requireMax);
      // 이상, 이하 문자열 지정
      requireMinString = requireMinExists && requireMin != 0d ? (requireMinString + " 이상 ") : "";
      requireMaxString = requireMaxExists ? (requireMaxString + " 이하") : "";
      // 조건 만족 테스트
      boolean passRequire = (requireMinExists && requireMaxExists && playerValueDouble >= requireMin && playerValueDouble <= requireMax) ||
              (requireMinExists && !requireMaxExists && playerValueDouble >= requireMin) ||
              (!requireMinExists && requireMaxExists && playerValueDouble <= requireMax) ||
              (!requireMinExists && !requireMaxExists);
      // 조건 만족 등호 문자열
      String equalsString = passRequire ? "=" : "≠";
      // 조건 만족 문자열 색상
      String color = getRadiusPercentColor(playerValueDouble, requireMinExists, requireMaxExists, requireMin, requireMax);
      // 최종적으로 설명에 추가될 문자열
      String finalValue = display + color + playerValueString + " &7" + equalsString + "rgb0,255,84; " + requireMinString;
      // 최대, 최소 조건이 동일하면 " 이상 " 문자 제거
      if (requireMinExists && requireMaxExists && requireMin == requireMax)
      {
        finalValue = finalValue.replace(" 이상 ", "");
      }
      // 동일하지 않을 경우 이상, 이하 문구 전부 추가
      else if (!requireMinExists || requireMin != 0d || requireMaxExists)
      {
        finalValue += requireMaxString;
      }
      // 어떤 경우도 아닐 경우 추가하지 않음
      else
      {
        finalValue = "";
      }
      if (!finalValue.equals(""))
      {
        requirementsLore.add(ComponentUtil.create(finalValue));
      }
      requirements[requirementOrder] = passRequire;
      requirementOrder++;
    }

    // 돈 플러그인 사용 시
    if (Cucumbery.using_Vault_Economy)
    {
      // 제작 비용
      playerValueDouble = Cucumbery.eco.getBalance(player);
      requireCostDouble = config.getDouble(configSection + "cost");
      // 제작 조건
      requireString = "wealth";
      prefixColor = "rgb60,253,26;";
      requireMinExists = config.contains(configSection + requireString + ".min");
      requireMaxExists = config.contains(configSection + requireString + ".max");
      if (requireCostDouble > 0)
      {
        String finalValue = prefixColor +
                "제작 비용 : " +
                getPercentColor(playerValueDouble / requireCostDouble) +
                Constant.Jeongsu.format(playerValueDouble) +
                "원 &7/rgb0,255,84; " +
                Constant.Sosu2.format(requireCostDouble) +
                "원";
        requirementsLore.add(ComponentUtil.create(finalValue));
        requirements[requirementOrder] = playerValueDouble >= requireCostDouble;
        requirementOrder++;
      }
      if (requireMinExists || requireMaxExists)
      {
        double requireMin = config.getDouble(configSection + requireString + ".min");
        double requireMax = config.getDouble(configSection + requireString + ".max");
        String display = prefixColor + "소지 금액 : ";
        String playerValueString = Constant.Sosu2.format(playerValueDouble) + "원";
        String requireMinString = Constant.Sosu2.format(requireMin);
        String requireMaxString = Constant.Sosu2.format(requireMax);
        // 이상, 이하 문자열 지정
        requireMinString = requireMinExists && requireMin != 0d ? (requireMinString + "원 이상 ") : "";
        requireMaxString = requireMaxExists ? (requireMaxString + "원 이하") : "";
        // 조건 만족 테스트
        boolean passRequire = (requireMinExists && requireMaxExists && playerValueDouble >= requireMin && playerValueDouble <= requireMax) ||
                (requireMinExists && !requireMaxExists && playerValueDouble >= requireMin) ||
                (!requireMinExists && requireMaxExists && playerValueDouble <= requireMax) ||
                (!requireMinExists && !requireMaxExists);
        // 조건 만족 등호 문자열
        String equalsString = passRequire ? "=" : "≠";
        // 조건 만족 문자열 색상
        String color = getRadiusPercentColor(playerValueDouble, requireMinExists, requireMaxExists, requireMin, requireMax);
        // 최종적으로 설명에 추가될 문자열
        String finalValue = display + color + playerValueString + " &7" + equalsString + "rgb0,255,84; " + requireMinString;
        // 최대, 최소 조건이 동일하면 " 이상 " 문자 제거
        if (requireMinExists && requireMaxExists && requireMin == requireMax)
        {
          finalValue = finalValue.replace(" 이상 ", "");
        }
        // 동일하지 않을 경우 이상, 이하 문구 전부 추가
        else if (!requireMinExists || requireMin != 0d || requireMaxExists)
        {
          finalValue += requireMaxString;
        }
        // 어떤 경우도 아닐 경우 추가하지 않음
        else
        {
          finalValue = "";
        }
        if (!finalValue.equals(""))
        {
          requirementsLore.add(ComponentUtil.create(finalValue));
        }
        requirements[requirementOrder] = passRequire;
        requirementOrder++;
      }
    }

    // 최대 접속 인원
    Collection<? extends Player> onlinePlayers = Bukkit.getServer().getOnlinePlayers();
    playerValueInt = onlinePlayers.size();
    // 최대 접속 인원 조건
    requireString = "maxplayer";
    prefixColor = "rgb89,255,189;";
    requireMinExists = config.contains(configSection + requireString + ".min");
    requireMaxExists = config.contains(configSection + requireString + ".max");
    if (requireMinExists || requireMaxExists)
    {
      int requireMin = config.getInt(configSection + requireString + ".min");
      int requireMax = config.getInt(configSection + requireString + ".max");
      String display = prefixColor + "접속 중인 플레이어 수 : ";
      String playerValueString = Constant.Jeongsu.format(playerValueInt) + "명";
      String requireMinString = Constant.Jeongsu.format(requireMin);
      String requireMaxString = Constant.Jeongsu.format(requireMax);
      // 이상, 이하 문자열 지정
      requireMinString = requireMinExists && requireMin != 0d ? (requireMinString + "명 이상 ") : "";
      requireMaxString = requireMaxExists ? (requireMaxString + "명 이하") : "";
      // 조건 만족 테스트
      boolean passRequire = (requireMinExists && requireMaxExists && playerValueInt >= requireMin && playerValueInt <= requireMax) ||
              (requireMinExists && !requireMaxExists && playerValueInt >= requireMin) ||
              (!requireMinExists && requireMaxExists && playerValueInt <= requireMax) ||
              (!requireMinExists && !requireMaxExists);
      // 조건 만족 등호 문자열
      String equalsString = passRequire ? "=" : "≠";
      // 조건 만족 문자열 색상
      String color = getRadiusPercentColor(playerValueInt, requireMinExists, requireMaxExists, requireMin, requireMax);
      // 최종적으로 설명에 추가될 문자열
      String finalValue = display + color + playerValueString + " &7" + equalsString + "rgb0,255,84; " + requireMinString;
      // 최대, 최소 조건이 동일하면 " 이상 " 문자 제거
      if (requireMinExists && requireMaxExists && requireMin == requireMax)
      {
        finalValue = finalValue.replace(" 이상 ", "");
      }
      // 동일하지 않을 경우 이상, 이하 문구 전부 추가
      else if (!requireMinExists || requireMin != 0d || requireMaxExists)
      {
        finalValue += requireMaxString;
      }
      // 어떤 경우도 아닐 경우 추가하지 않음
      else
      {
        finalValue = "";
      }
      if (!finalValue.equals(""))
      {
        requirementsLore.add(ComponentUtil.create(finalValue));
      }
      requirements[requirementOrder] = passRequire;
      requirementOrder++;
    }

    // 최대 접속 인원 (관리자 제외)
    for (Player onlinePlayer : onlinePlayers)
    {
      if (onlinePlayer.hasPermission("wa.sans.ppap.how.do.you.have.this.permission") || onlinePlayer.isOp())
      {
        playerValueInt--;
      }
    }
    // 최대 접속 인원 조건 (관리자 제외)
    requireString = "maxplayeruser";
    prefixColor = "rgb89,255,189;";
    requireMinExists = config.contains(configSection + requireString + ".min");
    requireMaxExists = config.contains(configSection + requireString + ".max");
    if (requireMinExists || requireMaxExists)
    {
      int requireMin = config.getInt(configSection + requireString + ".min");
      int requireMax = config.getInt(configSection + requireString + ".max");
      String display = prefixColor + "접속 중인 플레이어 수(관리자 제외) : ";
      String playerValueString = Constant.Jeongsu.format(playerValueInt) + "명";
      String requireMinString = Constant.Jeongsu.format(requireMin);
      String requireMaxString = Constant.Jeongsu.format(requireMax);
      // 이상, 이하 문자열 지정
      requireMinString = requireMinExists && requireMin != 0d ? (requireMinString + "명 이상 ") : "";
      requireMaxString = requireMaxExists ? (requireMaxString + "명 이하") : "";
      // 조건 만족 테스트
      boolean passRequire = (requireMinExists && requireMaxExists && playerValueInt >= requireMin && playerValueInt <= requireMax) ||
              (requireMinExists && !requireMaxExists && playerValueInt >= requireMin) ||
              (!requireMinExists && requireMaxExists && playerValueInt <= requireMax) ||
              (!requireMinExists && !requireMaxExists);
      // 조건 만족 등호 문자열
      String equalsString = passRequire ? "=" : "≠";
      // 조건 만족 문자열 색상
      String color = getRadiusPercentColor(playerValueInt, requireMinExists, requireMaxExists, requireMin, requireMax);
      // 최종적으로 설명에 추가될 문자열
      String finalValue = display + color + playerValueString + " &7" + equalsString + "rgb0,255,84; " + requireMinString;
      // 최대, 최소 조건이 동일하면 " 이상 " 문자 제거
      if (requireMinExists && requireMaxExists && requireMin == requireMax)
      {
        finalValue = finalValue.replace(" 이상 ", "");
      }
      // 동일하지 않을 경우 이상, 이하 문구 전부 추가
      else if (!requireMinExists || requireMin != 0d || requireMaxExists)
      {
        finalValue += requireMaxString;
      }
      // 어떤 경우도 아닐 경우 추가하지 않음
      else
      {
        finalValue = "";
      }
      if (!finalValue.equals(""))
      {
        requirementsLore.add(ComponentUtil.create(finalValue));
      }
      requirements[requirementOrder] = passRequire;
      requirementOrder++;
    }

    Location location = player.getLocation();
    World world = location.getWorld();
    String worldName = world.getName();
    requireString = "location.world";
    prefixColor = "rgb200,255,200;";
    String configString = config.getString(configSection + requireString);
    if (configString != null)
    {
      String playerValue = Method.getWorldDisplayName(worldName), configValue = Method.getWorldDisplayName(configString);
      boolean passRequire = worldName.contains(configString);
      String equalsString = passRequire ? "&7 = " : "&7 ≠ ";
      String finalValue = prefixColor + "현재 월드 : " + (passRequire ? "rgb0,255,84;" + playerValue : "rgb255,0,84;" + MessageUtil.stripColor(playerValue)) + equalsString + "rgb0,255,84;" + configValue;
      requirementsLore.add(ComponentUtil.create(finalValue));
      requirements[requirementOrder] = passRequire;
      requirementOrder++;
    }

    // 현재 위치 X값
    playerValueDouble = location.getX();
    // 위치 X 조건
    requireString = "location.x";
    prefixColor = "rgb200,200,200;";
    requireMinExists = config.contains(configSection + requireString + ".min");
    requireMaxExists = config.contains(configSection + requireString + ".max");
    if (requireMinExists || requireMaxExists)
    {
      double requireMin = config.getDouble(configSection + requireString + ".min");
      double requireMax = config.getDouble(configSection + requireString + ".max");
      String display = prefixColor + "현재 rgb255,50,50;X좌표 : ";
      String playerValueString = Constant.Sosu2.format(playerValueDouble);
      String requireMinString = Constant.Sosu2.format(requireMin);
      String requireMaxString = Constant.Sosu2.format(requireMax);
      // 이상, 이하 문자열 지정
      requireMinString = requireMinExists && requireMin != 0d ? (requireMinString + " 이상 ") : "";
      requireMaxString = requireMaxExists ? (requireMaxString + " 이하") : "";
      // 조건 만족 테스트
      boolean passRequire = (requireMinExists && requireMaxExists && playerValueDouble >= requireMin && playerValueDouble <= requireMax) ||
              (requireMinExists && !requireMaxExists && playerValueDouble >= requireMin) ||
              (!requireMinExists && requireMaxExists && playerValueDouble <= requireMax) ||
              (!requireMinExists && !requireMaxExists);
      // 조건 만족 등호 문자열
      String equalsString = passRequire ? "=" : "≠";
      // 조건 만족 문자열 색상
      String color = getRadiusPercentColor(playerValueDouble, requireMinExists, requireMaxExists, requireMin, requireMax);
      // 최종적으로 설명에 추가될 문자열
      String finalValue = display + color + playerValueString + " &7" + equalsString + "rgb0,255,84; " + requireMinString;
      // 최대, 최소 조건이 동일하면 " 이상 " 문자 제거
      if (requireMinExists && requireMaxExists && requireMin == requireMax)
      {
        finalValue = finalValue.replace(" 이상 ", "");
      }
      // 동일하지 않을 경우 이상, 이하 문구 전부 추가
      else if (!requireMinExists || requireMin != 0d || requireMaxExists)
      {
        finalValue += requireMaxString;
      }
      // 어떤 경우도 아닐 경우 추가하지 않음
      else
      {
        finalValue = "";
      }
      if (!finalValue.equals(""))
      {
        requirementsLore.add(ComponentUtil.create(finalValue));
      }
      requirements[requirementOrder] = passRequire;
      requirementOrder++;
    }

    // 현재 위치 Y값
    playerValueDouble = location.getY();
    // 위치 Y 조건
    requireString = "location.y";
    prefixColor = "rgb200,200,200;";
    requireMinExists = config.contains(configSection + requireString + ".min");
    requireMaxExists = config.contains(configSection + requireString + ".max");
    if (requireMinExists || requireMaxExists)
    {
      double requireMin = config.getDouble(configSection + requireString + ".min");
      double requireMax = config.getDouble(configSection + requireString + ".max");
      String display = prefixColor + "현재 rgb50,255,50;Y좌표 : ";
      String playerValueString = Constant.Sosu2.format(playerValueDouble);
      String requireMinString = Constant.Sosu2.format(requireMin);
      String requireMaxString = Constant.Sosu2.format(requireMax);
      // 이상, 이하 문자열 지정
      requireMinString = requireMinExists && requireMin != 0d ? (requireMinString + " 이상 ") : "";
      requireMaxString = requireMaxExists ? (requireMaxString + " 이하") : "";
      // 조건 만족 테스트
      boolean passRequire = (requireMinExists && requireMaxExists && playerValueDouble >= requireMin && playerValueDouble <= requireMax) ||
              (requireMinExists && !requireMaxExists && playerValueDouble >= requireMin) ||
              (!requireMinExists && requireMaxExists && playerValueDouble <= requireMax) ||
              (!requireMinExists && !requireMaxExists);
      // 조건 만족 등호 문자열
      String equalsString = passRequire ? "=" : "≠";
      // 조건 만족 문자열 색상
      String color = getRadiusPercentColor(playerValueDouble, requireMinExists, requireMaxExists, requireMin, requireMax);
      // 최종적으로 설명에 추가될 문자열
      String finalValue = display + color + playerValueString + " &7" + equalsString + "rgb0,255,84; " + requireMinString;
      // 최대, 최소 조건이 동일하면 " 이상 " 문자 제거
      if (requireMinExists && requireMaxExists && requireMin == requireMax)
      {
        finalValue = finalValue.replace(" 이상 ", "");
      }
      // 동일하지 않을 경우 이상, 이하 문구 전부 추가
      else if (!requireMinExists || requireMin != 0d || requireMaxExists)
      {
        finalValue += requireMaxString;
      }
      // 어떤 경우도 아닐 경우 추가하지 않음
      else
      {
        finalValue = "";
      }
      if (!finalValue.equals(""))
      {
        requirementsLore.add(ComponentUtil.create(finalValue));
      }
      requirements[requirementOrder] = passRequire;
      requirementOrder++;
    }

    // 현재 위치 Z값
    playerValueDouble = location.getZ();
    // 위치 Z 조건
    requireString = "location.z";
    prefixColor = "rgb200,200,200;";
    requireMinExists = config.contains(configSection + requireString + ".min");
    requireMaxExists = config.contains(configSection + requireString + ".max");
    if (requireMinExists || requireMaxExists)
    {
      double requireMin = config.getDouble(configSection + requireString + ".min");
      double requireMax = config.getDouble(configSection + requireString + ".max");
      String display = prefixColor + "현재 rgb50,50,255;Z좌표 : ";
      String playerValueString = Constant.Sosu2.format(playerValueDouble);
      String requireMinString = Constant.Sosu2.format(requireMin);
      String requireMaxString = Constant.Sosu2.format(requireMax);
      // 이상, 이하 문자열 지정
      requireMinString = requireMinExists && requireMin != 0d ? (requireMinString + " 이상 ") : "";
      requireMaxString = requireMaxExists ? (requireMaxString + " 이하") : "";
      // 조건 만족 테스트
      boolean passRequire = (requireMinExists && requireMaxExists && playerValueDouble >= requireMin && playerValueDouble <= requireMax) ||
              (requireMinExists && !requireMaxExists && playerValueDouble >= requireMin) ||
              (!requireMinExists && requireMaxExists && playerValueDouble <= requireMax) ||
              (!requireMinExists && !requireMaxExists);
      // 조건 만족 등호 문자열
      String equalsString = passRequire ? "=" : "≠";
      // 조건 만족 문자열 색상
      String color = getRadiusPercentColor(playerValueDouble, requireMinExists, requireMaxExists, requireMin, requireMax);
      // 최종적으로 설명에 추가될 문자열
      String finalValue = display + color + playerValueString + " &7" + equalsString + "rgb0,255,84; " + requireMinString;
      // 최대, 최소 조건이 동일하면 " 이상 " 문자 제거
      if (requireMinExists && requireMaxExists && requireMin == requireMax)
      {
        finalValue = finalValue.replace(" 이상 ", "");
      }
      // 동일하지 않을 경우 이상, 이하 문구 전부 추가
      else if (!requireMinExists || requireMin != 0d || requireMaxExists)
      {
        finalValue += requireMaxString;
      }
      // 어떤 경우도 아닐 경우 추가하지 않음
      else
      {
        finalValue = "";
      }
      if (!finalValue.equals(""))
      {
        requirementsLore.add(ComponentUtil.create(finalValue));
      }
      requirements[requirementOrder] = passRequire;
      requirementOrder++;
    }

    // 현재 위치 X축 회전
    playerValueDouble = location.getYaw();
    // 위치 X축 회전 조건
    requireString = "location.yaw";
    prefixColor = "rgb200,200,200;";
    requireMinExists = config.contains(configSection + requireString + ".min");
    requireMaxExists = config.contains(configSection + requireString + ".max");
    if (requireMinExists || requireMaxExists)
    {
      double requireMin = config.getDouble(configSection + requireString + ".min");
      double requireMax = config.getDouble(configSection + requireString + ".max");
      String display = prefixColor + "현재 rgb255,150,150;X축 회전 : ";
      String playerValueString = Constant.Sosu2.format(playerValueDouble);
      String requireMinString = Constant.Sosu2.format(requireMin);
      String requireMaxString = Constant.Sosu2.format(requireMax);
      // 이상, 이하 문자열 지정
      requireMinString = requireMinExists && requireMin != 0d ? (requireMinString + " 이상 ") : "";
      requireMaxString = requireMaxExists ? (requireMaxString + " 이하") : "";
      // 조건 만족 테스트
      boolean passRequire = (requireMinExists && requireMaxExists && playerValueDouble >= requireMin && playerValueDouble <= requireMax) ||
              (requireMinExists && !requireMaxExists && playerValueDouble >= requireMin) ||
              (!requireMinExists && requireMaxExists && playerValueDouble <= requireMax) ||
              (!requireMinExists && !requireMaxExists);
      // 조건 만족 등호 문자열
      String equalsString = passRequire ? "=" : "≠";
      // 조건 만족 문자열 색상
      String color = getRadiusPercentColor(playerValueDouble, requireMinExists, requireMaxExists, requireMin, requireMax);
      // 최종적으로 설명에 추가될 문자열
      String finalValue = display + color + playerValueString + " &7" + equalsString + "rgb0,255,84; " + requireMinString;
      // 최대, 최소 조건이 동일하면 " 이상 " 문자 제거
      if (requireMinExists && requireMaxExists && requireMin == requireMax)
      {
        finalValue = finalValue.replace(" 이상 ", "");
      }
      // 동일하지 않을 경우 이상, 이하 문구 전부 추가
      else if (!requireMinExists || requireMin != 0d || requireMaxExists)
      {
        finalValue += requireMaxString;
      }
      // 어떤 경우도 아닐 경우 추가하지 않음
      else
      {
        finalValue = "";
      }
      if (!finalValue.equals(""))
      {
        requirementsLore.add(ComponentUtil.create(finalValue));
      }
      requirements[requirementOrder] = passRequire;
      requirementOrder++;
    }

    // 현재 위치 Y축 회전
    playerValueDouble = location.getPitch();
    // 위치 Y축 회전 조건
    requireString = "location.pitch";
    prefixColor = "rgb200,200,200;";
    requireMinExists = config.contains(configSection + requireString + ".min");
    requireMaxExists = config.contains(configSection + requireString + ".max");
    if (requireMinExists || requireMaxExists)
    {
      double requireMin = config.getDouble(configSection + requireString + ".min");
      double requireMax = config.getDouble(configSection + requireString + ".max");
      String display = prefixColor + "현재 rgb150,255,150;Y축 회전 : ";
      String playerValueString = Constant.Sosu2.format(playerValueDouble);
      String requireMinString = Constant.Sosu2.format(requireMin);
      String requireMaxString = Constant.Sosu2.format(requireMax);
      // 이상, 이하 문자열 지정
      requireMinString = requireMinExists && requireMin != 0d ? (requireMinString + " 이상 ") : "";
      requireMaxString = requireMaxExists ? (requireMaxString + " 이하") : "";
      // 조건 만족 테스트
      boolean passRequire = (requireMinExists && requireMaxExists && playerValueDouble >= requireMin && playerValueDouble <= requireMax) ||
              (requireMinExists && !requireMaxExists && playerValueDouble >= requireMin) ||
              (!requireMinExists && requireMaxExists && playerValueDouble <= requireMax) ||
              (!requireMinExists && !requireMaxExists);
      // 조건 만족 등호 문자열
      String equalsString = passRequire ? "=" : "≠";
      // 조건 만족 문자열 색상
      String color = getRadiusPercentColor(playerValueDouble, requireMinExists, requireMaxExists, requireMin, requireMax);
      // 최종적으로 설명에 추가될 문자열
      String finalValue = display + color + playerValueString + " &7" + equalsString + "rgb0,255,84; " + requireMinString;
      // 최대, 최소 조건이 동일하면 " 이상 " 문자 제거
      if (requireMinExists && requireMaxExists && requireMin == requireMax)
      {
        finalValue = finalValue.replace(" 이상 ", "");
      }
      // 동일하지 않을 경우 이상, 이하 문구 전부 추가
      else if (!requireMinExists || requireMin != 0d || requireMaxExists)
      {
        finalValue += requireMaxString;
      }
      // 어떤 경우도 아닐 경우 추가하지 않음
      else
      {
        finalValue = "";
      }
      if (!finalValue.equals(""))
      {
        requirementsLore.add(ComponentUtil.create(finalValue));
      }
      requirements[requirementOrder] = passRequire;
      requirementOrder++;
    }

    // 날씨 조건
    String playerValue;
    boolean storm = location.getWorld().hasStorm();
    boolean thunder = location.getWorld().isThundering();
    if (!storm)
    {
      playerValue = "SUN";
    }
    else
    {
      if (!thunder)
      {
        playerValue = "RAIN";
      }
      else
      {
        playerValue = "THUNDER";
      }
    }
    requireString = "weather";
    prefixColor = "rgb113,203,225;";
    configString = config.getString(configSection + requireString);
    if (configString != null)
    {
      String playerValueString = playerValue.replace("SUN", "맑음").replace("RAIN", "비").replace("THUNDER", "폭풍우"), configValue = Method.getWorldDisplayName(configString).replace("STORM", "RAIN")
              .replace("SUN", "맑음").replace("RAIN", "비").replace(
                      "THUNDER", "폭풍우");
      boolean passRequire = playerValue.equals(configString);
      String equalsString = passRequire ? "&7 = " : "&7 ≠ ";
      String finalValue = prefixColor + "현재 날씨 : " + (passRequire ? "rgb0,255,84;" : "rgb255,0,84;") + playerValueString + equalsString + "rgb0,255,84;" + configValue;
      requirementsLore.add(ComponentUtil.create(finalValue));
      requirements[requirementOrder] = passRequire;
      requirementOrder++;
    }

    // 바이옴 조건
    int locationBlockX = location.getBlockX(), locationBlockY = location.getBlockY(), locationBlockZ = location.getBlockZ();
    Biome playerBiome = world.getBiome(locationBlockX, locationBlockY, locationBlockZ);
    Biome configBiome = null;
    playerValue = playerBiome.toString();
    requireString = "biome";
    prefixColor = "gb236,124;";
    configString = config.getString(configSection + requireString);
    try
    {
      configBiome = Biome.valueOf(configString);
      configString = configBiome.toString();
    }
    catch (Exception e)
    {
      configString = null;
    }
    if (configString != null)
    {
      boolean passRequire = playerValue.contains(configString);
      String equalsString = passRequire ? "&7 = " : "&7 ≠ ";
      String finalValue = prefixColor + "현재 생물 군계 : " + (passRequire ? "rgb0,255,84;" : "rgb255,0,84;") + playerBiome.name() + equalsString + "rgb0,255,84;" + configBiome.name();
      requirementsLore.add(ComponentUtil.create(finalValue));
      requirements[requirementOrder] = passRequire;
      requirementOrder++;
    }

    // 바닥에 있는 블록 조건
    requireString = "belowblock";
    prefixColor = "rg242,121;";
    configString = config.getString(configSection + requireString);
    try
    {
      Material configMaterial = Material.valueOf(configString);
      configString = configMaterial.toString();
    }
    catch (Exception e)
    {
      configString = null;
    }
    if (configString != null)
    {
      Block belowBlock = world.getBlockAt(locationBlockX, (int) Math.floor(location.getY() - 0.01), locationBlockZ);
      playerValue = belowBlock.getType().toString();
      boolean passRequire = playerValue.equals(configString);
      String equalsString = passRequire ? "&7=" : "&7≠";
      requirementsLore.add(
              ComponentUtil.translate(
                      prefixColor + "밟고 있는 블록 : %s %s %s",
                      ItemNameUtil.itemName(Material.valueOf(playerValue), passRequire ? TextColor.color(0, 255, 84) : TextColor.color(255, 0, 84)),
                      equalsString,
                      ItemNameUtil.itemName(Material.valueOf(configString), TextColor.color(0, 255, 84))
              )
      );
      requirements[requirementOrder] = passRequire;
      requirementOrder++;
    }

    // 게임 시간 조건
    requireString = "times.game";
    List<String> configTimes = config.getStringList(configSection + requireString);
    if (configTimes != null && configTimes.size() > 0)
    {
      boolean passRequire;
      int requireFound = 0;
      List<Component> validRequireTime = new ArrayList<>();
      for (String configTime : configTimes)
      {
        if (configTime.replace("0", "").equals(":~24:"))
        {
          requireFound = 1;
          validRequireTime.add(ComponentUtil.create("rgb0,255,84; - 시간 제한 없음"));
          break;
        }
      }
      if (requireFound == 0)
      {
        for (String configTime : configTimes)
        {
          try
          {
            String[] fromAndToSplit = configTime.split("~");
            String fromString = fromAndToSplit[0], toString = null;
            if (fromAndToSplit.length == 2)
            {
              toString = fromAndToSplit[1];
            }
            String[] fromSplit = fromString.split(":");
            int fromHour = Integer.parseInt(fromSplit[0]);
            int fromMinute = Integer.parseInt(fromSplit[1]);
            // 값이 음수일 경우 다음 배열로 넘어감
            if (fromHour < 0 || fromMinute < 0 || fromHour > 24 || fromMinute > 59)
            {
              throw new Exception();
            }
            int toHour = -1, toMinute = -1;
            if (toString != null)
            {
              String[] toSplit = toString.split(":");
              toHour = Integer.parseInt(toSplit[0]);
              toMinute = Integer.parseInt(toSplit[1]);
              if (toHour < 0 || toMinute < 0 || toHour > 24 || toMinute > 59)
              {
                throw new Exception();
              }
            }
            int fromTime = fromHour * 1000 + (int) (fromMinute / 0.06);
            int toTime = toHour * 1000 + (int) (toMinute / 0.06);
            // 시각 조건이 범위일 경우, 범위 내에 들어 있어야만 만족
            // from이 to보다 작을 경우, 현재 시각이 from보다 작거나 to보다 작아도 만족
            fromTime -= 6000;
            if (fromTime < -6000)
            {
              throw new Exception();
            }
            if (fromTime == -6000)
            {
              fromTime = 18000;
            }
            if (fromTime < 0)
            {
              fromTime += 24000;
            }
            int time = (int) world.getTime();
            if (fromTime == time)
            {
              requireFound++;
            }
            String fromTimeString = (fromTime >= 6000 && fromTime < 18000 ? "오후" : "오전") + " " + CommandWhatIs.currentTime(fromTime);
            if (fromMinute != 0)
            {
              fromTimeString = fromTimeString.split("시")[0] + "시 " + fromMinute + "분";
            }
            if (fromTimeString.contains("초"))
            {
              //							if (!fromTimeString.contains("분"))
              //							{
              //								String removeSecond = fromTimeString.split("시")[1];
              //								fromTimeString = fromTimeString.replace(removeSecond, "");
              //							}
              //							else
              //							{
              //								String removeSecond = fromTimeString.split("분")[1];
              //								fromTimeString = fromTimeString.replace(removeSecond, "");
              //							}
              String removeSecond;
              if (!fromTimeString.contains("분"))
              {
                removeSecond = fromTimeString.split("시")[1];
              }
              else
              {
                removeSecond = fromTimeString.split("분")[1];
              }
              fromTimeString = fromTimeString.replace(removeSecond, "");
            }
            if (toHour == -1)
            // 시각 조건이 범위가 없을 경우, 일치해야만 만족
            // 조건에 만족하지 않아도 시간 설명은 추가
            {
              validRequireTime.add(ComponentUtil.create("rgb0,255,84; - " + fromTimeString));
            }
            else
            {
              toTime -= 6000;
              if (toTime < -6000)
              {
                throw new Exception();
              }
              if (toTime == -6000)
              {
                toTime = 18000;
              }
              if (toTime < 0)
              {
                toTime += 24000;
              }
              //							boolean fromAndToSuccess = (fromTime <= toTime && time >= fromTime && time <= toTime) || (fromTime > toTime && (time >= fromTime || time <= toTime));
              boolean fromAndToSuccess = (time >= fromTime && time <= toTime) || (fromTime > toTime && (time >= fromTime || time <= toTime));
              if (fromAndToSuccess)
              {
                requireFound++;
              }
              // 조건에 만족하지 않아도 시간 설명은 추가
              String toTimeString = (toTime >= 6000 && toTime < 18000 ? "오후" : "오전") + " " + CommandWhatIs.currentTime(toTime);
              if (toMinute != 0)
              {
                toTimeString = toTimeString.split("시")[0] + "시 " + toMinute + "분";
              }
              if (toTimeString.contains("초"))
              {
                String removeSecond;
                if (!toTimeString.contains("분"))
                {
                  removeSecond = toTimeString.split("시")[1];
                }
                else
                {
                  removeSecond = toTimeString.split("분")[1];
                }
                toTimeString = toTimeString.replace(removeSecond, "");
                //								if (!toTimeString.contains("분"))
                //								{
                //									String removeSecond = toTimeString.split("시")[1];
                //									toTimeString = toTimeString.replace(removeSecond, "");
                //								}
                //								else
                //								{
                //									String removeSecond = toTimeString.split("분")[1];
                //									toTimeString = toTimeString.replace(removeSecond, "");
                //								}
              }
              // from과 to가 똑같으면
              validRequireTime.add(ComponentUtil.create("rgb0,255,84; - " + fromTimeString + (fromTimeString.equals(toTimeString) ? "" : ("~" + toTimeString))));
            }
          }
          catch (Exception ignored)
          {

          }
        }
      }
      // 조건에 충족하는 시간이 1개라도 있을 경우 통과
      passRequire = requireFound > 0;
      if (validRequireTime.size() > 0)
      {
        int time = (int) world.getTime();
        String finalValue = (passRequire ? "rgb0,255,84;" : "rgb255,0,84;") + (time >= 6000 && time < 18000 ? "오후" : "오전") + " " + CommandWhatIs.currentTime(time);
        if (finalValue.contains("초"))
        {
          String removeSecond;
          if (!finalValue.contains("분"))
          {
            removeSecond = finalValue.split("시")[1];
          }
          else
          {
            removeSecond = finalValue.split("분")[1];
          }
          finalValue = finalValue.replace(removeSecond, "");
          //					if (!finalValue.contains("분"))
          //					{
          //						String removeSecond = finalValue.split("시")[1];
          //						finalValue = finalValue.replace(removeSecond, "");
          //					}
          //					else
          //					{
          //						String removeSecond = finalValue.split("분")[1];
          //						finalValue = finalValue.replace(removeSecond, "");
          //					}
        }
        requirementsLore.add(ComponentUtil.create("&b[마인크래프트 시각 조건]"));
        requirementsLore.add(ComponentUtil.create("&b현재 시각 : " + finalValue));
        requirementsLore.addAll(validRequireTime);
        requirements[requirementOrder] = passRequire;
        requirementOrder++;
      }
    }

    // 현실 시간 조건
    requireString = "times.reality.day";
    configTimes = config.getStringList(configSection + requireString);
    if (configTimes != null && configTimes.size() > 0)
    {
      boolean passRequire;
      int requireFound = 0;
      List<Component> validRequireTime = new ArrayList<>();
      for (String configTime : configTimes)
      {
        if (configTime.replace("0", "").equals("::~24::") || configTime.replace("0", "").equals(":~24:"))
        {
          requireFound = 1;
          validRequireTime.add(ComponentUtil.create("rgb0,255,84; - 시간 제한 없음"));
          break;
        }
      }

      Calendar cal = Calendar.getInstance();
      int hour = cal.get(Calendar.HOUR_OF_DAY), minute = cal.get(Calendar.MINUTE), second = cal.get(Calendar.SECOND);
      int adjustHour = Cucumbery.config.getInt("adjust-time-difference-value");
      hour += adjustHour;
      if (hour < 0)
      {
        hour += 24;
      }
      if (hour > 24)
      {
        hour -= 24;
      }
      if (requireFound != 1)
      {
        for (String configTime : configTimes)
        {
          try
          {
            int fromHour, fromMinute, fromSecond = 0, toHour = -1, toMinute = 0, toSecond = 0;
            String[] fromAndToSplit = configTime.split("~");
            String[] fromSplit = fromAndToSplit[0].split(":");
            fromHour = Integer.parseInt(fromSplit[0]);
            fromMinute = Integer.parseInt(fromSplit[1]);
            if (fromSplit.length == 3)
            {
              fromSecond = Integer.parseInt(fromSplit[2]);
            }
            if (fromAndToSplit.length == 2)
            {
              String[] toSplit = fromAndToSplit[1].split(":");
              toHour = Integer.parseInt(toSplit[0]);
              toMinute = Integer.parseInt(toSplit[1]);
              if (toSplit.length == 3)
              {
                toSecond = Integer.parseInt(toSplit[2]);
              }
              if (toHour > 24 || toHour < 0 || toMinute > 59 || toMinute < 0 || toSecond > 59 || toSecond < 0)
              {
                throw new Exception();
              }
            }
            if (fromHour > 24 || fromHour < 0 || fromMinute > 59 || fromMinute < 0 || fromSecond > 59 || fromSecond < 0)
            {
              throw new Exception();
            }
            if (fromHour == 24)
            {
              fromHour = 0;
            }
            if (toHour == 24)
            {
              toHour = 0;
            }
            int fromHourDisplay = fromHour;
            if (fromHourDisplay > 12)
            {
              fromHourDisplay -= 12;
            }
            if (fromHourDisplay == 0)
            {
              fromHourDisplay = 12;
            }
            String fromTimeString = (fromHour >= 12 ? "오후" : "오전") + " " + fromHourDisplay + "시" + (fromMinute > 0 ? " " + fromMinute + "분" : "") + (fromSecond > 0 ? " " + fromSecond + "초" : "");
            if (toHour == -1)
            {
              if (hour == fromHour && minute == fromMinute && (fromSecond == 0 || second == fromSecond))
              {
                requireFound++;
              }
              validRequireTime.add(ComponentUtil.create("rgb0,255,84; - " + fromTimeString));
            }
            else
            {
              int fromTotal = fromHour * 3600 + fromMinute * 60 + fromSecond;
              int toTotal = toHour * 3600 + toMinute * 60 + toSecond;
              int calendarTotal = hour * 3600 + minute * 60 + second;
              //							if ((fromTotal <= toTotal && calendarTotal >= fromTotal && calendarTotal <= toTotal) || (fromTotal > toTotal && (calendarTotal >= fromTotal || calendarTotal <=
              //							toTotal)))
              if ((calendarTotal >= fromTotal && calendarTotal <= toTotal) || (fromTotal > toTotal && (calendarTotal >= fromTotal || calendarTotal <= toTotal)))
              {
                requireFound++;
              }
              int toHourDisplay = toHour;
              if (toHourDisplay > 12)
              {
                toHourDisplay -= 12;
              }
              if (toHourDisplay == 0)
              {
                toHourDisplay = 12;
              }
              String toTimeString = (toHour >= 12 ? "오후" : "오전") + " " + toHourDisplay + "시" + (toMinute > 0 ? " " + toMinute + "분" : "") + (toSecond > 0 ? " " + toSecond + "초" : "");
              validRequireTime.add(ComponentUtil.create("rgb0,255,84; - " + fromTimeString + (fromTimeString.equals(toTimeString) ? "" : ("~" + toTimeString))));
            }
          }
          catch (Exception ignored)
          {
          }
        }
      }
      passRequire = requireFound > 0;
      if (validRequireTime.size() > 0)
      {
        if (hour == 0)
        {
          hour = 12;
        }
        requirementsLore.add(ComponentUtil.create("&b[현실 시각 조건]"));
        String finalValue =
                (passRequire ? "rgb0,255,84;" : "rgb255,0,84;") + (hour < 12 ? "오전" : "오후") + " " + (hour > 12 ? hour - 12 : hour) + "시" + (minute > 0 ? " " + minute + "분" : "") + (second > 0 ? " " + second + "초" : "");
        requirementsLore.add(ComponentUtil.create("&b현재 시각 : " + finalValue));
        requirementsLore.addAll(validRequireTime);
        requirements[requirementOrder] = passRequire;
        requirementOrder++;
      }
    }

    // 현실 날짜 조건
    requireString = "times.reality.date";
    configTimes = config.getStringList(configSection + requireString);
    if (configTimes != null && configTimes.size() > 0)
    {
      boolean passRequire;
      int requireFound = 0;
      List<Component> validRequireTime = new ArrayList<>();
      Calendar cal = Calendar.getInstance();
      // 시차 조절 시간 값 설정을 현재 시각에 더함
      int adjustHour = Cucumbery.config.getInt("adjust-time-difference-value");
      cal.add(Calendar.HOUR, adjustHour);
      int year = cal.get(Calendar.YEAR), month = cal.get(Calendar.MONTH) + 1, date = cal.get(Calendar.DATE);
      for (String configTime : configTimes)
      {
        try
        {
          int fromYear, fromMonth, fromDate, toYear = -1, toMonth = 0, toDate = 0;
          String[] fromAndToSplit = configTime.split("~");
          String[] fromSplit = fromAndToSplit[0].split("-");
          fromYear = Integer.parseInt(fromSplit[0]);
          fromMonth = Integer.parseInt(fromSplit[1]);
          fromDate = Integer.parseInt(fromSplit[2]);
          if (fromAndToSplit.length == 2)
          {
            String[] toSplit = fromAndToSplit[1].split("-");
            toYear = Integer.parseInt(toSplit[0]);
            toMonth = Integer.parseInt(toSplit[1]);
            toDate = Integer.parseInt(toSplit[2]);
            if (toYear < 0 || toMonth > 12 || toMonth <= 0 || toDate > 31 || toDate <= 0)
            {
              throw new Exception();
            }
          }
          if (fromYear < 0 || fromMonth > 12 || fromMonth <= 0 || fromDate > 31 || fromDate <= 0)
          {
            throw new Exception();
          }
          Calendar fromCal = Calendar.getInstance();
          fromCal.set(fromYear, fromMonth, fromDate);
          String fromTimeString = fromYear + "년 " + fromMonth + "월 " + fromDate + "일";
          if (toYear == -1)
          {
            if (cal.after(fromCal))
            {
              requireFound++;
            }
            validRequireTime.add(ComponentUtil.create("rgb0,255,84; - " + fromTimeString));
          }
          else
          {
            Calendar toCal = Calendar.getInstance();
            toCal.set(toYear, toMonth, toDate);
            String toTimeString = toYear + "년 " + toMonth + "월 " + toDate + "일";
            if (cal.after(fromCal) && cal.before(toCal))
            {
              requireFound++;
            }
            validRequireTime.add(ComponentUtil.create("rgb0,255,84; - " + fromTimeString + (fromTimeString.equals(toTimeString) ? "" : ("~" + toTimeString))));
          }
        }
        catch (Exception e)
        {
Cucumbery.getPlugin().getLogger().warning(          e.getMessage());
        }
      }
      passRequire = requireFound > 0;
      if (validRequireTime.size() > 0)
      {
        requirementsLore.add(ComponentUtil.create("&b[현실 날짜 조건]"));
        String finalValue = (passRequire ? "rgb0,255,84;" : "rgb255,0,84;") + year + "년 " + month + "월 " + date + "일";
        requirementsLore.add(ComponentUtil.create("&b현재 날짜 : " + finalValue));
        requirementsLore.addAll(validRequireTime);
        requirements[requirementOrder] = passRequire;
        requirementOrder++;
      }
    }

    // 제작 요구 조건 - 특정 카테고리의 레시피 사용 횟수
    ConfigurationSection requireCategories = config.getConfigurationSection(configSection + "categories");
    if (requireCategories != null && requireCategories.getKeys(false).size() > 0)
    {
      YamlConfiguration logConfig = Variable.craftsLog.get(uuid);
      boolean[] requirementCategories = new boolean[requireCategories.getKeys(false).size()];
      int categoryOrder = 0;
      for (String requireCategory : requireCategories.getKeys(false))
      {
        requireMinExists = config.contains(configSection + "categories." + requireCategory + ".min");
        requireMaxExists = config.contains(configSection + "categories." + requireCategory + ".max");
        if (requireMinExists || requireMaxExists)
        {
          int requireMin = config.getInt(configSection + "categories." + requireCategory + ".min");
          int requireMax = config.getInt(configSection + "categories." + requireCategory + ".max");
          int playerRequire = logConfig == null ? 0 : logConfig.getInt("crafts.categories." + requireCategory);
          String color = getRadiusPercentColor(playerRequire, requireMinExists, requireMaxExists, requireMin, requireMax);
          boolean passRequire = (requireMinExists && requireMaxExists && playerRequire >= requireMin && playerRequire <= requireMax) ||
                  (requireMinExists && !requireMaxExists && playerRequire >= requireMin) ||
                  (!requireMinExists && requireMaxExists && playerRequire <= requireMax) ||
                  (!requireMinExists && !requireMaxExists);
          File categoryFile = new File(Cucumbery.getPlugin().getDataFolder() + "/data/CustomRecipe/" + requireCategory + ".yml");
          if (!categoryFile.exists())
          {
            requirementCategories[categoryOrder] = true;
            categoryOrder++;
            continue;
          }
          YamlConfiguration requireCategoryConfig = Variable.customRecipes.get(requireCategory);
          String categoryDisplay = requireCategoryConfig.getString("extra.display");
          if (categoryDisplay == null)
          {
            categoryDisplay = requireCategory;
          }
          categoryDisplay = MessageUtil.n2s(categoryDisplay);

          String dupCheckString = (category.equals(requireCategory) ? "rgb213,136,43;현재 레시피 목록의 레시피 제작 횟수 : " : categoryDisplay + "rgb213,136,43; 레시피 목록의 레시피 제작 횟수 : ");

          if (requireMinExists && requireMaxExists && requireMin == requireMax)
          {
            requirementsLore.add(ComponentUtil.create("rg255,230;" + dupCheckString + color + Constant.Jeongsu.format(playerRequire) + "회 &7" + (passRequire ? "=" : "≠") + "rgb0,255,84; " + (Constant.Jeongsu.format(requireMin) + "회")));
          }
          else if (!requireMinExists || requireMin != 0d || requireMaxExists)
          {
            requirementsLore.add(ComponentUtil.create("rg255,230;" +
                    dupCheckString +
                    color +
                    Constant.Jeongsu.format(playerRequire) +
                    "회 &7" +
                    (passRequire ? "=" : "≠") +
                    "rgb0,255,84; " +
                    (requireMinExists && requireMin != 0d ? (Constant.Jeongsu.format(requireMin) + "회 이상 ") : "") +
                    (requireMaxExists ? (Constant.Jeongsu.format(requireMax) + "회 이하") : "")));
          }
          requirementCategories[categoryOrder] = passRequire;
          categoryOrder++;
        }
      }
      requirements[requirementOrder] = Method.allIsTrue(requirementCategories);
      requirementOrder++;
    }
    // 제작 요구 조건 - 특정 카테고리의 특정 레시피 사용 횟수
    ConfigurationSection requireCategoryRecipes = config.getConfigurationSection(configSection + "recipes");
    if (requireCategoryRecipes != null && requireCategoryRecipes.getKeys(false).size() > 0)
    {
      YamlConfiguration logConfig = Variable.craftsLog.get(uuid);
      boolean[] requirementCategoryRecipes = new boolean[requireCategoryRecipes.getKeys(false).size()];
      int categoryRecipeOrder = 0;
      for (String requireCategory : requireCategoryRecipes.getKeys(false))
      {
        ConfigurationSection requireRecipes = config.getConfigurationSection(configSection + "recipes." + requireCategory);
        if (requireRecipes != null && requireRecipes.getKeys(false).size() > 0)
        {
          boolean[] requirementRecipes = new boolean[requireRecipes.getKeys(false).size()];
          int recipeOrder = 0;
          for (String requireRecipe : requireRecipes.getKeys(false))
          {
            requireMinExists = config.contains(configSection + "recipes." + requireCategory + "." + requireRecipe + ".min");
            requireMaxExists = config.contains(configSection + "recipes." + requireCategory + "." + requireRecipe + ".max");
            if (requireMinExists || requireMaxExists)
            {
              int requireMin = config.getInt(configSection + "recipes." + requireCategory + "." + requireRecipe + ".min");
              int requireMax = config.getInt(configSection + "recipes." + requireCategory + "." + requireRecipe + ".max");
              int playerRequire = logConfig == null ? 0 : logConfig.getInt("crafts.recipes." + requireCategory + "." + requireRecipe);
              String color = getRadiusPercentColor(playerRequire, requireMinExists, requireMaxExists, requireMin, requireMax);
              boolean passRequire = (requireMinExists && requireMaxExists && playerRequire >= requireMin && playerRequire <= requireMax) ||
                      (requireMinExists && !requireMaxExists && playerRequire >= requireMin) ||
                      (!requireMinExists && requireMaxExists && playerRequire <= requireMax) ||
                      (!requireMinExists && !requireMaxExists);
              File categoryFile = new File(Cucumbery.getPlugin().getDataFolder() + "/data/CustomRecipe/" + requireCategory + ".yml");
              if (!categoryFile.exists())
              {
                requirementRecipes[recipeOrder] = true;
                recipeOrder++;
                continue;
              }
              YamlConfiguration requireCategoryConfig = Variable.customRecipes.get(requireCategory);
              ConfigurationSection requireRecipeSection = requireCategoryConfig.getConfigurationSection("recipes." + requireRecipe);
              if (requireRecipeSection == null || requireRecipeSection.getKeys(false).size() == 0)
              {
                requirementRecipes[recipeOrder] = true;
                recipeOrder++;
                continue;
              }
              String display = ComponentUtil.serialize(ComponentUtil.create(requireCategoryConfig.getString("recipes." + requireRecipe + ".extra.display")));
              if (display == null)
              {
                display = requireRecipe;
              }
              display = MessageUtil.n2s(display);
              String categoryDisplay = ComponentUtil.serialize(ComponentUtil.create(requireCategoryConfig.getString("extra.display")));
              if (categoryDisplay == null)
              {
                categoryDisplay = requireCategory;
              }
              categoryDisplay = MessageUtil.n2s(categoryDisplay);

              String dupCheckString = (category.equals(requireCategory) ? "rg255,230;" : categoryDisplay + "rgb213,136,43; 레시피 목록의 rg255,230;") + display + "rgb213,136,43; 레시피 제작 횟수 : ";
              if (category.equals(requireCategory) && recipe.equals(requireRecipe))
              {
                dupCheckString = "rgb213,136,43;제작 횟수 : ";
              }

              if (requireMinExists && requireMaxExists && requireMin == requireMax)
              {
                requirementsLore.add(ComponentUtil.create(
                        "rg255,230;" + dupCheckString + color + Constant.Jeongsu.format(playerRequire) + "회 &7" + (passRequire ? "=" : "≠") + "rgb0,255,84; " + (Constant.Jeongsu.format(requireMin) + "회")));
              }
              else if (!requireMinExists || requireMin != 0d || requireMaxExists)
              {
                requirementsLore.add(ComponentUtil.create("rg255,230;" +
                        dupCheckString +
                        color +
                        Constant.Jeongsu.format(playerRequire) +
                        "회 &7" +
                        (passRequire ? "=" : "≠") +
                        "rgb0,255,84; " +
                        (requireMinExists && requireMin != 0d ? (Constant.Jeongsu.format(requireMin) + "회 이상 ") : "") +
                        (requireMaxExists ? (Constant.Jeongsu.format(requireMax) + "회 이하") : "")));
              }
              requirementRecipes[recipeOrder] = passRequire;
              recipeOrder++;
            }
          }
          requirementCategoryRecipes[categoryRecipeOrder] = Method.allIsTrue(requirementRecipes);
          categoryRecipeOrder++;
        }
      }
      requirements[requirementOrder] = Method.allIsTrue(requirementCategoryRecipes);
      requirementOrder++;
    }

    // 통계 조건
    ConfigurationSection requireStatistics = config.getConfigurationSection(configSection + "statistics");
    if (requireStatistics != null && requireStatistics.getKeys(false).size() > 0)
    {
      // 각 통계가 모두 조건을 만족하는가?
      boolean[] requirementStatistics = new boolean[requireStatistics.getKeys(false).size()];
      int statisticsOrder = 0;

      // general, entity, material 최대 3개
      for (String requireStatistic : requireStatistics.getKeys(false))
      {
        switch (requireStatistic)
        {
          case "general":
            // 일반 통계 조건 섹션 extra.statistics.general
            ConfigurationSection requireGeneralStatistics = config.getConfigurationSection(configSection + "statistics." + requireStatistic);
            if (requireGeneralStatistics != null && requireGeneralStatistics.getKeys(false).size() > 0)
            {
              // 일반 통계 종류가 모두 조건을 만족하는가?
              boolean[] requirementGeneralStatistics = new boolean[requireGeneralStatistics.getKeys(false).size()];
              int statisticsGeneralOrder = 0;

              // 일반 통계 종류 (걸은 거리, 플레이 시간 등)
              // 일반 통계 조건 섹션 extra.statistics.general.(여기)
              for (String requireGeneralStatistic : requireGeneralStatistics.getKeys(false))
              {
                boolean statisticsMinExists = config.contains(configSection + "statistics." + requireStatistic + "." + requireGeneralStatistic + ".min");
                boolean statisticsMaxExists = config.contains(configSection + "statistics." + requireStatistic + "." + requireGeneralStatistic + ".max");
                if (statisticsMinExists || statisticsMaxExists)
                {
                  int statisticsMin = config.getInt(configSection + "statistics." + requireStatistic + "." + requireGeneralStatistic + ".min");
                  int statisticsMax = config.getInt(configSection + "statistics." + requireStatistic + "." + requireGeneralStatistic + ".max");
                  Statistic statistic = Statistic.valueOf(requireGeneralStatistic);
                  double playerStatistic = player.getStatistic(statistic);
                  List<Component> args = new ArrayList<>();
                  Component key = ComponentUtil.translate(TranslatableKeyParser.getKey(statistic));
                  args.add(key);
                  String statisticName = ComponentUtil.serialize(key);
                  String suffix = "";
                  boolean isCount = false;
                  boolean isNumber = false;
                  boolean needTimeFormat = statistic.toString().contains("TIME") || statistic.toString().contains("MINUTE");
                  boolean isRadius = false;
                  if (statistic.toString().endsWith("CM"))
                  {
                    isRadius = true;
                    suffix = "m";
                  }
                  if (statisticName.endsWith("개수"))
                  {
                    suffix = "개";
                    isCount = true;
                  }
                  if (statisticName.endsWith("횟수"))
                  {
                    suffix = "회";
                    isNumber = true;
                  }
                  TranslatableComponent statisticDisplay = ComponentUtil.translate("rg102,255;%s : %s");
                  String playerValueString = Constant.Jeongsu.format(playerStatistic) + suffix;
                  String minValue = Constant.Jeongsu.format(statisticsMin);
                  String maxValue = Constant.Jeongsu.format(statisticsMax);
                  if (needTimeFormat)
                  {
                    playerValueString = Method.timeFormatMilli((long) playerStatistic * 50L, false);
                    minValue = Method.timeFormatMilli(statisticsMin * 50L);
                    maxValue = Method.timeFormatMilli(statisticsMax * 50L);
                  }
                  else if (isRadius)
                  {
                    playerValueString = Constant.Jeongsu.format(playerStatistic / 100d) + "m";
                    minValue = Constant.Jeongsu.format(statisticsMin / 100d);
                    maxValue = Constant.Jeongsu.format(statisticsMax / 100d);
                  }
                  // 이상, 이하 문자열 지정
                  minValue = statisticsMinExists && statisticsMin != 0d ? (minValue + suffix + " 이상 ") : "";
                  maxValue = statisticsMaxExists ? (maxValue + suffix + " 이하") : "";
                  // 조건 만족 테스트
                  boolean passRequire = (statisticsMinExists && statisticsMaxExists && playerStatistic >= statisticsMin && playerStatistic <= statisticsMax) ||
                          (statisticsMinExists && !statisticsMaxExists && playerStatistic >= statisticsMin) ||
                          (!statisticsMinExists && statisticsMaxExists && playerStatistic <= statisticsMax) ||
                          (!statisticsMinExists && !statisticsMaxExists);
                  // 조건 만족 등호 문자열
                  String equalsString = passRequire ? "=" : "≠";
                  // 조건 만족 문자열 색상
                  String color = getRadiusPercentColor(playerStatistic, statisticsMinExists, statisticsMaxExists, statisticsMin, statisticsMax);
                  // 최종적으로 설명에 추가될 문자열
                  String finalValue = color + playerValueString + " &7" + equalsString + "rgb0,255,84; " + minValue;
                  // 최대, 최소 조건이 동일하면 " 이상 " 문자 제거
                  if (statisticsMinExists && statisticsMaxExists && statisticsMin == statisticsMax)
                  {
                    finalValue = finalValue.replace(" 이상 ", "");
                  }
                  // 동일하지 않을 경우 이상, 이하 문구 전부 추가
                  else if (!statisticsMinExists || statisticsMin != 0d || statisticsMaxExists)
                  {
                    finalValue += maxValue;
                  }
                  // 어떤 경우도 아닐 경우 추가하지 않음
                  else
                  {
                    finalValue = "";
                  }
                  if (!finalValue.equals(""))
                  {
                    args.add(ComponentUtil.create(finalValue));
                    requirementsLore.add(statisticDisplay.args(args));
                  }
                  // 조건을 만족하면 true를 넣고 다음 배열의 값 대기
                  requirementGeneralStatistics[statisticsGeneralOrder] = passRequire;
                  statisticsGeneralOrder++;
                }
              }
              requirementStatistics[statisticsOrder] = Method.allIsTrue(requirementGeneralStatistics);
              statisticsOrder++;
            }
            break;
          case "entity":
            // 개체 통계 조건 섹션 extra.statistics.entity.(여기)
            ConfigurationSection requireEntityStatistics = config.getConfigurationSection(configSection + "statistics." + requireStatistic);
            if (requireEntityStatistics != null && requireEntityStatistics.getKeys(false).size() > 0)
            {
              boolean[] requirementEntityStatistics = new boolean[requireEntityStatistics.getKeys(false).size()];
              int statisticsEntityOrder = 0;

              // 개체 관련 통계 종류 (죽임, 죽음) 최대 2개
              for (String requireEntityStatistic : requireEntityStatistics.getKeys(false))
              {
                // 개체 종류 목록 섹션 extra.statistics.entity.(죽임|죽음).(여기)
                ConfigurationSection requireEntityTypeStatistics = config.getConfigurationSection(configSection + "statistics." + requireStatistic + "." + requireEntityStatistic);
                if (requireEntityTypeStatistics != null && requireEntityTypeStatistics.getKeys(false).size() > 0)
                {
                  boolean[] requirementEntityTypeStatistics = new boolean[requireEntityTypeStatistics.getKeys(false).size()];
                  int statisticsEntityTypeOrder = 0;
                  // 개체 종류 (크리퍼, 좀비 등)
                  for (String requireEntityTypeStatistic : requireEntityTypeStatistics.getKeys(false))
                  {
                    boolean statisticsMinExists = config.contains(configSection + "statistics." + requireStatistic + "." + requireEntityStatistic + "." + requireEntityTypeStatistic + ".min");
                    boolean statisticsMaxExists = config.contains(configSection + "statistics." + requireStatistic + "." + requireEntityStatistic + "." + requireEntityTypeStatistic + ".max");
                    if (statisticsMinExists || statisticsMaxExists)
                    {
                      int recipeMin = config.getInt(configSection + "statistics." + requireStatistic + "." + requireEntityStatistic + "." + requireEntityTypeStatistic + ".min");
                      int recipeMax = config.getInt(configSection + "statistics." + requireStatistic + "." + requireEntityStatistic + "." + requireEntityTypeStatistic + ".max");
                      Statistic statistic = Statistic.valueOf(requireEntityStatistic);
                      EntityType entityType = EntityType.valueOf(requireEntityTypeStatistic);
                      List<Component> args = new ArrayList<>();
                      Component entityComponent = ComponentUtil.translate(entityType.translationKey());
                      if (statistic == Statistic.KILL_ENTITY)
                      {
                        args.add(ComponentUtil.translate("%s을(를) 죽인 횟수", entityComponent));
                      }
                      else if (statistic == Statistic.ENTITY_KILLED_BY)
                      {
                        args.add(ComponentUtil.translate("%s에게 죽은 횟수", entityComponent));
                      }
                      TranslatableComponent statisticDisplay = ComponentUtil.translate("rgb155,255,89;%s : %s");
                      int playerStatistic = player.getStatistic(statistic, entityType);
                      String color = getRadiusPercentColor(playerStatistic, statisticsMinExists, statisticsMaxExists, recipeMin, recipeMax);
                      boolean passRequire = (statisticsMinExists && statisticsMaxExists && playerStatistic >= recipeMin && playerStatistic <= recipeMax) ||
                              (statisticsMinExists && !statisticsMaxExists && playerStatistic >= recipeMin) ||
                              (!statisticsMinExists && statisticsMaxExists && playerStatistic <= recipeMax) ||
                              (!statisticsMinExists && !statisticsMaxExists);
                      if (statisticsMinExists && statisticsMaxExists && recipeMin == recipeMax)
                      {
                        args.add(ComponentUtil.create(color + Constant.Jeongsu.format(playerStatistic) + "회 &7" + (passRequire ? "=" : "≠") + "rgb0,255,84; " + (Constant.Jeongsu.format(recipeMin) + "회")));
                        requirementsLore.add(statisticDisplay.args(args));
                      }
                      else if (!statisticsMinExists || recipeMin != 0d || statisticsMaxExists)
                      {
                        args.add(ComponentUtil.create(color + Constant.Jeongsu.format(playerStatistic) + "회 &7" + (passRequire ? "=" : "≠") + "rgb0,255,84; " +
                                (statisticsMinExists && recipeMin != 0d ? (Constant.Jeongsu.format(recipeMin) + "회 이상 ") : "") + (statisticsMaxExists ? (Constant.Jeongsu.format(recipeMax) + "회 이하") : "")));
                        requirementsLore.add(statisticDisplay.args(args));
                      }
                      requirementEntityTypeStatistics[statisticsEntityTypeOrder] = passRequire;
                      statisticsEntityTypeOrder++;
                    }
                  }
                  requirementEntityStatistics[statisticsEntityOrder] = Method.allIsTrue(requirementEntityTypeStatistics);
                  statisticsEntityOrder++;
                }
              }
              requirementStatistics[statisticsOrder] = Method.allIsTrue(requirementEntityStatistics);
              statisticsOrder++;
            }
            break;
          case "material":
            // 아이템 통계 조건 섹션 extra.statistics.material.(여기)
            ConfigurationSection requireMaterialStatistics = config.getConfigurationSection(configSection + "statistics." + requireStatistic);
            if (requireMaterialStatistics != null && requireMaterialStatistics.getKeys(false).size() > 0)
            {
              boolean[] requirementMaterialStatistics = new boolean[requireMaterialStatistics.getKeys(false).size()];
              int statisticsMaterialOrder = 0;

              // 아이템 관련 통계 종류 (사용, 버리기, 줍기, 제작, 망가뜨림, 블록 부숨) 최대 6개
              for (String requireMaterialStatistic : requireMaterialStatistics.getKeys(false))
              {
                // 아이템 종류 목록 섹션 extra.statistics.material.(사용, 버리기, 줍기, 제작, 망가뜨림, 블록 부숨).(여기)
                ConfigurationSection requireMaterialTypeStatistics = config.getConfigurationSection(configSection + "statistics." + requireStatistic + "." + requireMaterialStatistic);
                if (requireMaterialTypeStatistics != null && requireMaterialTypeStatistics.getKeys(false).size() > 0)
                {
                  boolean[] requirementMaterialTypeStatistics = new boolean[requireMaterialTypeStatistics.getKeys(false).size()];
                  int statisticsMaterialTypeOrder = 0;
                  // 아이템 종류 (다이아몬드, 석탄, 돌 등)
                  for (String requireMaterialTypeStatistic : requireMaterialTypeStatistics.getKeys(false))
                  {
                    boolean statisticsMinExists = config.contains(configSection + "statistics." + requireStatistic + "." + requireMaterialStatistic + "." + requireMaterialTypeStatistic + ".min");
                    boolean statisticsMaxExists = config.contains(configSection + "statistics." + requireStatistic + "." + requireMaterialStatistic + "." + requireMaterialTypeStatistic + ".max");
                    if (statisticsMinExists || statisticsMaxExists)
                    {
                      int recipeMin = config.getInt(configSection + "statistics." + requireStatistic + "." + requireMaterialStatistic + "." + requireMaterialTypeStatistic + ".min");
                      int recipeMax = config.getInt(configSection + "statistics." + requireStatistic + "." + requireMaterialStatistic + "." + requireMaterialTypeStatistic + ".max");
                      Statistic statistic = Statistic.valueOf(requireMaterialStatistic);
                      Material material = Material.valueOf(requireMaterialTypeStatistic);
                      List<Component> args = new ArrayList<>();
                      Component materialComponent = ItemNameUtil.itemName(material);
                      args.add(materialComponent);
                      args.add(ComponentUtil.translate(TranslatableKeyParser.getKey(statistic)));
                      TranslatableComponent statisticDisplay = ComponentUtil.translate("rgb105,255,89;%s %s : %s");
                      int playerStatistic = player.getStatistic(statistic, material);
                      String color = getRadiusPercentColor(playerStatistic, statisticsMinExists, statisticsMaxExists, recipeMin, recipeMax);
                      boolean passRequire = (statisticsMinExists && statisticsMaxExists && playerStatistic >= recipeMin && playerStatistic <= recipeMax) ||
                              (statisticsMinExists && !statisticsMaxExists && playerStatistic >= recipeMin) ||
                              (!statisticsMinExists && statisticsMaxExists && playerStatistic <= recipeMax) ||
                              (!statisticsMinExists && !statisticsMaxExists);
                      if (statisticsMinExists && statisticsMaxExists && recipeMin == recipeMax)
                      {
                        args.add(ComponentUtil.create(color + Constant.Jeongsu.format(playerStatistic) + "회 &7" + (passRequire ? "=" : "≠") + "rgb0,255,84; " + (Constant.Jeongsu.format(recipeMin) + "회")));
                        requirementsLore.add(statisticDisplay.args(args));
                      }
                      else if (!statisticsMinExists || recipeMin != 0d || statisticsMaxExists)
                      {
                        args.add(ComponentUtil.create(color + Constant.Jeongsu.format(playerStatistic) + "회 &7" + (passRequire ? "=" : "≠") + "rgb0,255,84; " +
                                (statisticsMinExists && recipeMin != 0d ? (Constant.Jeongsu.format(recipeMin) + "회 이상 ") : "") + (statisticsMaxExists ? (Constant.Jeongsu.format(recipeMax) + "회 이하") : "")));
                        requirementsLore.add(statisticDisplay.args(args));
                      }
                      requirementMaterialTypeStatistics[statisticsMaterialTypeOrder] = passRequire;
                      statisticsMaterialTypeOrder++;
                    }
                  }
                  requirementMaterialStatistics[statisticsMaterialOrder] = Method.allIsTrue(requirementMaterialTypeStatistics);
                  statisticsMaterialOrder++;
                }
              }
              requirementStatistics[statisticsOrder] = Method.allIsTrue(requirementMaterialStatistics);
              statisticsOrder++;
            }
            break;
        }
      }
      requirements[requirementOrder] = Method.allIsTrue(requirementStatistics);
    }
    if (CustomConfig.UserData.EVENT_EXCEPTION_ACCESS.getBoolean(player.getUniqueId()))
    {
      if (!Method.allIsTrue(requirements))
      {
        requirementsLore.addAll(Arrays.asList(Component.empty(), ComponentUtil.create("&b[관리자 권한으로 추가 " + (isForCategory ? "접근" : "제작") + " 조건을 우회합니다]")));
      }
      Arrays.fill(requirements, true);
    }
  }

  public static String getPercentColor(double ratio)
  {
    if (ratio == 0d)
    {
      return "rgb255,0,84;";
    }
    else if (ratio < 1d)
    {
      int red = 255, green = 0;
      green += (250d * ratio);
      if (green > 255)
      {
        green = 255;
      }
      if (green < 0)
      {
        green = 0;
      }
      if (ratio >= 0.7 && ratio < 1d)
      {
        red -= (50d * (ratio - 0.7));
      }
      if (red < 0)
      {
        red = 0;
      }
      if (red > 255)
      {
        red = 255;
      }
      return "rgb" + red + "," + green + ",84;";
    }
    else
    {
      return "rgb0,255,84;";
    }
  }

  static String getRadiusPercentColor(double current, boolean minExists, boolean maxExists, double min, double max)
  {
    if (min != -1d && minExists && (!maxExists || max == -1d) && current >= min)
    {
      return "gb255,84;";
    }
    if (min != -1d && minExists && current < min)
    {
      return getPercentColor(current / min);
    }
    if (current >= min && current <= max)
    {
      return "gb255,84;";
    }
    double difference = Math.abs(max - min);
    if (difference == 0)
    {
      difference = 1d;
    }
    double ratio = 1d - (current - max) / difference;
    return getPercentColor(ratio);
  }

  public static void openCreationGUI(Player player, ItemStack result, List<ItemStack> ingredients, List<Integer> ingredientAmounts, String category, String recipe, String title)
  {
    Inventory gui = Bukkit.createInventory(
            null, 54, Constant.CUSTOM_RECIPE_CREATE_GUI +
                    title +
                    " - " +
                    MessageUtil.n2s(category) +
                    "§3 - " +
                    MessageUtil.n2s(recipe + Method.format("category:" + category + "recipe:" + recipe, "§")));

    ItemStack deco1 = CreateItemStack.create(Material.ORANGE_STAINED_GLASS_PANE, 1, "§와", true);
    ItemStack deco2 = CreateItemStack.create(Material.GREEN_STAINED_GLASS_PANE, 1, "§와", true);

    gui.setItem(0, CreateItemStack.create(Material.BARRIER, 1, "§c[레시피 삭제]", Arrays.asList("§e시프트 좌클릭§7하여 해당 레시피를 삭제합니다", "§c이 작업은 되돌릴 수 없습니다"), true));
    gui.setItem(1, deco1);
    gui.setItem(2, deco1);

    gui.setItem(6, deco1);
    gui.setItem(7, deco1);
    gui.setItem(8, deco1);

    gui.setItem(9, deco1);
    gui.setItem(10, deco1);
    gui.setItem(11, deco1);

    gui.setItem(15, deco1);
    gui.setItem(16, deco1);
    gui.setItem(17, deco1);

    gui.setItem(18, deco1);
    gui.setItem(19, deco1);
    gui.setItem(20, deco1);
    gui.setItem(21, deco1);

    gui.setItem(13, CreateItemStack.create(Material.ACACIA_SIGN, 1, "§와§d[제작 방법]", Arrays.asList("§7상단에 결과물 아이템을 넣고", "§7하단 27칸에 레시피의 재료가 되는", "§7아이템을 넣고 바로 아래의 제작대를 클릭하면 됩니다"), true));

    gui.setItem(23, deco1);
    gui.setItem(24, deco1);
    gui.setItem(25, deco1);
    gui.setItem(26, deco1);

    gui.setItem(3, deco2);
    gui.setItem(5, deco2);
    gui.setItem(12, deco2);
    gui.setItem(14, deco2);
    gui.setItem(4, CreateItemStack.create(Material.BARRIER, 1, "§e로딩중...", true));
    if (ingredients != null && !ingredients.isEmpty() && ingredientAmounts != null && !ingredientAmounts.isEmpty())
    {
      gui.setItem(13, CreateItemStack.create(Material.ACACIA_SIGN, 1, "§와§d[편집 방법]", Arrays.asList("§7상단에 결과물 아이템을 넣고", "§7하단 27칸에 레시피의 재료가 되는", "§7아이템을 넣고 바로 아래의 지도 제작대를 클릭하면 됩니다"), true));
      gui.setItem(22, CreateItemStack.create(Material.CARTOGRAPHY_TABLE, 1, "§a레시피 편집", "§7클릭하여 레시피를 편집합니다", true));
      for (int i = 0; i < ingredients.size(); i++)
      {
        ItemStack ingredient = ingredients.get(i);
        ItemLore.setItemLore(ingredient, ItemLoreView.of(player));
        int amount = ingredientAmounts.get(i);
        int maxStackSize = ingredient.getMaxStackSize();
        ingredient.setAmount(maxStackSize);
        int stack = amount / maxStackSize;
        for (int j = 0; j < stack; j++)
        {
          gui.addItem(ingredient);
          amount -= maxStackSize;
        }
        if (amount > 0)
        {
          ingredient.setAmount(amount);
          gui.addItem(ingredient);
        }
      }
    }
    else
    {
      gui.setItem(22, CreateItemStack.create(Material.CRAFTING_TABLE, 1, "§a레시피 생성", "§7클릭하여 레시피를 생성합니다", true));
    }
    if (ItemStackUtil.itemExists(result))
    {
      ItemLore.setItemLore(result, ItemLoreView.of(player));
      gui.setItem(4, result);
    }
    else
    {
      gui.setItem(4, null);
    }
    player.openInventory(gui);
  }

  public static List<File> getPlayersCraftLog()
  {
    List<File> logFiles = new ArrayList<>();
    File folder = new File(Cucumbery.getPlugin().getDataFolder() + "/data/CustomRecipe/CraftsLog");
    if (!folder.exists())
    {
      return logFiles;
    }
    File[] logFilesArray = folder.listFiles();
    if (logFilesArray == null)
    {
      logFilesArray = new File[]{};
    }
    for (File logFile : logFilesArray)
    {
      if (logFile.getName().endsWith(".yml"))
      {
        logFiles.add(logFile);
      }
    }
    return logFiles;
  }

  public static void removePlayerCraftLog(@NotNull String uuid, @NotNull String category, @Nullable String recipe)
  {
    File logFile = new File(Cucumbery.getPlugin().getDataFolder() + "/data/CustomRecipe/CraftsLog/" + uuid + ".yml");
    if (!logFile.exists())
    {
      return;
    }
    CustomConfig logFileCustomConfig = CustomConfig.getCustomConfig("data/CustomRecipe/CraftsLog/" + uuid + ".yml");
    YamlConfiguration logFileConfig = logFileCustomConfig.getConfig();
    ConfigurationSection craftsSection = logFileConfig.getConfigurationSection("crafts");
    if (craftsSection != null)
    {
      ConfigurationSection craftsCategoriesSection = craftsSection.getConfigurationSection("categories");
      if (recipe == null && craftsCategoriesSection != null)
      {
        Set<String> configCategoryKeys = craftsCategoriesSection.getKeys(false);
        for (String configCategoryKey : configCategoryKeys)
        {
          if (category.equals(configCategoryKey))
          {
            logFileConfig.set("crafts.categories." + configCategoryKey, null);
          }
        }
        if (Objects.requireNonNull(logFileConfig.getConfigurationSection("crafts.categories")).getKeys(false).size() == 0)
        {
          logFileConfig.set("crafts.categories", null);
        }
      }
      ConfigurationSection craftsRecipesSection = craftsSection.getConfigurationSection("recipes");
      if (craftsRecipesSection != null)
      {
        Set<String> configCategoryKeys = craftsRecipesSection.getKeys(false);
        for (String configCategoryKey : configCategoryKeys)
        {
          if (category.equals(configCategoryKey))
          {
            if (recipe == null)
            {
              logFileConfig.set("crafts.recipes." + category, null);
            }
            else
            {
              ConfigurationSection craftsCategoryRecipesSection = craftsRecipesSection.getConfigurationSection(configCategoryKey);
              if (craftsCategoryRecipesSection != null)
              {
                Set<String> recipeKeys = craftsCategoryRecipesSection.getKeys(false);
                for (String recipeKey : recipeKeys)
                {
                  if (recipe.equals(recipeKey))
                  {
                    logFileConfig.set("crafts.recipes." + category + "." + recipe, null);
                  }
                }
                if (Objects.requireNonNull(logFileConfig.getConfigurationSection("crafts.recipes." + category)).getKeys(false).size() == 0)
                {
                  logFileConfig.set("crafts.recipes." + category, null);
                }
              }
            }
          }
        }
        if (Objects.requireNonNull(logFileConfig.getConfigurationSection("crafts.recipes")).getKeys(false).size() == 0)
        {
          logFileConfig.set("crafts.recipes", null);
        }
      }
    }
    logFileCustomConfig.saveConfig();
    Variable.craftsLog.put(UUID.fromString(uuid), logFileConfig);
  }

  public static void removePlayerLastCraftLog(@NotNull String uuid, @NotNull String category, @Nullable String recipe)
  {
    File logFile = new File(Cucumbery.getPlugin().getDataFolder() + "/data/CustomRecipe/LastCraftsLog/" + uuid + ".yml");
    if (!logFile.exists())
    {
      return;
    }
    CustomConfig logFileCustomConfig = CustomConfig.getCustomConfig("data/CustomRecipe/LastCraftsLog/" + uuid + ".yml");
    YamlConfiguration logFileConfig = logFileCustomConfig.getConfig();
    ConfigurationSection craftsSection = logFileConfig.getConfigurationSection("last-crafts");
    if (craftsSection != null)
    {
      ConfigurationSection craftsCategoriesSection = craftsSection.getConfigurationSection(category);
      if (recipe == null && craftsCategoriesSection != null)
      {
        logFileConfig.set("last-crafts." + category, null);
      }
      ConfigurationSection craftsRecipesSection = craftsSection.getConfigurationSection(category + "." + recipe);
      if (craftsRecipesSection != null)
      {
        Set<String> configRecipeKeys = craftsRecipesSection.getKeys(false);
        for (String configRecipeKey : configRecipeKeys)
        {
          if (Objects.requireNonNull(recipe).equals(configRecipeKey))
          {
            logFileConfig.set("last-crafts." + category + "." + recipe, null);
          }
          if (Objects.requireNonNull(logFileConfig.getConfigurationSection("last-crafts." + category)).getKeys(false).size() == 0)
          {
            logFileConfig.set("last-crafts." + category, null);
          }
        }
      }
    }
    logFileCustomConfig.saveConfig();
    Variable.lastCraftsLog.put(UUID.fromString(uuid), logFileConfig);
  }
}

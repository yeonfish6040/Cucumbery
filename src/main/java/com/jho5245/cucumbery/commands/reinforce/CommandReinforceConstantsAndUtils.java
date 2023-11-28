package com.jho5245.cucumbery.commands.reinforce;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeReinforce;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.itemlore.ItemLoreView;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class CommandReinforceConstantsAndUtils extends CommandReinforce
{
  protected static final String MONEY_INFO = "소지 금액 %s원, 비용 %s원";

  protected static final String REINFORCE_INFO = "%s성 ➜ %s성 (최대 %s성)";

  protected static final String REINFORCE_SUCCESS_CHANCE = "강화 성공 확률 : %s";

  protected static final String REINFORCE_FAILURE_KEEP_CHANCE = "강화 실패(유지) 확률 : %s";

  protected static final String REINFORCE_FAILURE_DROP_CHANCE = "강화 실패(하락) 확률 : %s";

  protected static final String REINFORCE_DESTRUCTION_DROP_CHANCE = "파괴 확률 : %s";

  protected static final Component STARFORCE_10_INFO = ComponentUtil.translate("★10성+★", NamedTextColor.YELLOW).hoverEvent(
          ComponentUtil.translate("★10성+★").append(Component.text("\n"))
                  .append(ComponentUtil.translate("10성을 달성했습니다!")).append(Component.text("\n"))
                  .append(ComponentUtil.translate("스타포스 강화에 실패하여 강화 단계가")).append(Component.text("\n"))
                  .append(ComponentUtil.translate("하락하더라도 10성 밑으로는 하락하지 않습니다"))
  );

  protected static final Component STARFORCE_15_INFO = ComponentUtil.translate("★★15성+★★", NamedTextColor.AQUA).hoverEvent(
          ComponentUtil.translate("★★15성+★★").append(Component.text("\n"))
                  .append(ComponentUtil.translate("15성을 달성했습니다!")).append(Component.text("\n"))
                  .append(ComponentUtil.translate("스타포스 강화에 실패하여 강화 단계가")).append(Component.text("\n"))
                  .append(ComponentUtil.translate("하락하더라도 15성 밑으로는 하락하지 않습니다"))
  );

  protected static final Component STARFORCE_20_INFO = ComponentUtil.translate("★☆★20성+★☆★", NamedTextColor.LIGHT_PURPLE).hoverEvent(
          ComponentUtil.translate("★☆★20성+★☆★").append(Component.text("\n"))
                  .append(ComponentUtil.translate("20성을 달성했습니다!")).append(Component.text("\n"))
                  .append(ComponentUtil.translate("스타포스 강화에 실패하여 강화 단계가")).append(Component.text("\n"))
                  .append(ComponentUtil.translate("하락하더라도 20성 밑으로는 하락하지 않습니다"))
  );

  protected static final Component CUCUMBER_FORCE_20_INFO = ComponentUtil.translate("★20성+★", NamedTextColor.YELLOW).hoverEvent(
          ComponentUtil.translate("★20성+★").append(Component.text("\n"))
                  .append(ComponentUtil.translate("20성을 달성했습니다!")).append(Component.text("\n"))
                  .append(ComponentUtil.translate("오이포스 강화에 실패하여 강화 단계가")).append(Component.text("\n"))
                  .append(ComponentUtil.translate("하락하더라도 20성 밑으로는 하락하지 않습니다"))
  );

  protected static final Component CUCUMBER_ORCE_30_INFO = ComponentUtil.translate("★★30성+★★", NamedTextColor.AQUA).hoverEvent(
          ComponentUtil.translate("★★30성+★★").append(Component.text("\n"))
                  .append(ComponentUtil.translate("30성을 달성했습니다!")).append(Component.text("\n"))
                  .append(ComponentUtil.translate("오이포스 강화에 실패하여 강화 단계가")).append(Component.text("\n"))
                  .append(ComponentUtil.translate("하락하더라도 30성 밑으로는 하락하지 않습니다"))
  );

  protected static final Component CUCUMBER_FORCE_40_INFO = ComponentUtil.translate("★☆★40성+★☆★", NamedTextColor.LIGHT_PURPLE).hoverEvent(
          ComponentUtil.translate("★☆★40성+★☆★").append(Component.text("\n"))
                  .append(ComponentUtil.translate("30성을 달성했습니다!")).append(Component.text("\n"))
                  .append(ComponentUtil.translate("오이포스 강화에 실패하여 강화 단계가")).append(Component.text("\n"))
                  .append(ComponentUtil.translate("하락하더라도 40성 밑으로는 하락하지 않습니다"))
  );

  protected static final Component SUCCESS = ComponentUtil.translate("비용을 지불하여 아이템을 강화합니다");

  protected static final Component MAY_DROP = ComponentUtil.translate("&c실패 시 %s가 %s할 수 있습니다", ComponentUtil.translate(Constant.THE_COLOR_HEX + "강화 단계"), ComponentUtil.translate("r255;하락"));

  protected static final Component WILL_DROP = ComponentUtil.translate("&c실패 시 %s가 %s합니다", ComponentUtil.translate(Constant.THE_COLOR_HEX + "강화 단계"), ComponentUtil.translate("r255;하락"));

  protected static final Component MAY_DESTROY_OR_DROP = ComponentUtil.translate("&c실패 시 아이템이 %s되거나 %s가 %s할 수 있습니다",
          ComponentUtil.translate("&8파괴"), ComponentUtil.translate(Constant.THE_COLOR_HEX + "단계"), ComponentUtil.translate("r255;하락"));

  protected static final Component WILL_DESTROY_OR_DROP = ComponentUtil.translate("&c실패 시 아이템이 %s되거나 %s가 %s합니다",
          ComponentUtil.translate("&8파괴"), ComponentUtil.translate(Constant.THE_COLOR_HEX + "단계"), ComponentUtil.translate("r255;하락"));

  protected static final Component MAY_DESTROY = ComponentUtil.translate("&c실패 시 아이템이 %s될 수 있습니다", ComponentUtil.translate("&8파괴"));

  protected static final Component WILL_DESTROY = ComponentUtil.translate("&c실패 시 아이템이 %s됩니다", ComponentUtil.translate("&8파괴"));

  protected static final Component MESSAGE_SUCCESS = ComponentUtil.translate("강화에 %s했습니다", ComponentUtil.translate("&a성공"));

  protected static final Component MESSAGE_FAILURE = ComponentUtil.translate("강화에 %s했습니다", ComponentUtil.translate("r255;실패"));

  protected static final Component MESSAGE_FAILURE_DROP = ComponentUtil.translate("강화에 %s하여 강화 단계가 %s했습니다", ComponentUtil.translate("r255;실패"), ComponentUtil.translate("r255;하락"));

  protected static final Component MESSAGE_DESTROY = ComponentUtil.translate("강화에 %s하여 아이템이 %s되었습니다", ComponentUtil.translate("&8실패"), ComponentUtil.translate("&8파괴"));
  protected static final Component START_BUTTON = ComponentUtil.translate("             [%s]                     [%s]",
          ComponentUtil.translate("&a강화 시작").hoverEvent(ComponentUtil.translate("클릭하여 강화를 시작합니다")).clickEvent(ClickEvent.runCommand(Constant.REINFORCE_START)),
          ComponentUtil.translate("&c강화 중지").hoverEvent(ComponentUtil.translate("클릭하여 강화를 중지합니다")).clickEvent(ClickEvent.runCommand(Constant.REINFORCE_QUIT)));

  protected static final TranslatableComponent OPTION_BUTTON_ANTI_DESTRUCTION = ComponentUtil.translate("파괴방지 : [%s]").hoverEvent(ComponentUtil.translate("파괴방지")
          .append(Component.text("\n")).append(ComponentUtil.translate("스타포스 15성에서 16성 사이의 아이템으로 스타포스 강화를 시도하거나"))
          .append(Component.text("\n")).append(ComponentUtil.translate("오이포스 24성에서 33성 사이의 아이템으로 강화를 시도할 때"))
          .append(Component.text("\n")).append(ComponentUtil.translate("비용을 2배로 소모하여 파괴 확률을 0%로 만들 수 있습니다"))
          .append(Component.text("\n")).append(ComponentUtil.translate("단, 파괴방지로 인하여 증가하는 비용은 강화 비용 할인의 혜택을 받을 수 없으며"))
          .append(Component.text("\n")).append(ComponentUtil.translate("또한 슈페리얼 강화, 에베벱 강화에는 사용할 수 없습니다")));

  protected static final TranslatableComponent OPTION_BUTTON_STAR_CATCH = ComponentUtil.translate("스타캐치 해제 : [%s]").hoverEvent(ComponentUtil.translate("스타캐치 해제")
          .append(Component.text("\n")).append(ComponentUtil.translate("해제 시 좀 더 빠르게 스타포스 강화를 진행할 수 있지만"))
          .append(Component.text("\n")).append(ComponentUtil.translate("스타캐치로 인한 성공 확률 증가의 효과를 받을 수 없습니다"))
          .append(Component.text("\n"))
          .append(Component.text("\n")).append(ComponentUtil.translate("스타캐치 성공 시 강화 성공 확률이 5% 복리로 증가합니다"))
          .append(Component.text("\n")).append(ComponentUtil.translate("예시 - 성공 확률이 50%일 때 스타캐치를 성공하면 성공 확률이 52.5%로 적용")));

  protected static TranslatableComponent optionButton(@NotNull Component starCatch, @NotNull Component antiDestruction)
  {
    return ComponentUtil.translate("          %s              %s", OPTION_BUTTON_STAR_CATCH.args(starCatch), OPTION_BUTTON_ANTI_DESTRUCTION.args(antiDestruction));
  }

  protected static final Component OPTION_BUTTON_USE = ComponentUtil.translate("&8○");

  protected static final Component OPTION_BUTTON_DONT_USE = ComponentUtil.translate("&a○");

  protected static final Component OPTION_BUTTON_DISABLED = ComponentUtil.translate("&c×");

  protected static void operate(@NotNull Player player, @NotNull OperationType operationType)
  {
    UUID uuid = player.getUniqueId();
    ItemStack itemStack = player.getInventory().getItemInMainHand();
    if (!ItemStackUtil.itemExists(itemStack))
    {
      MessageUtil.sendError(player, Prefix.NO_HOLDING_ITEM);
      Variable.scrollReinforcing.remove(uuid);
      return;
    }
    // 강화 진행도(n성)
    int current, max;
    // 강화 유형
    ReinforceType type;
    // 장비 유형
    ItemType itemType = ItemType.ETC;
    // 강화 등급(장비 레벨)
    int itemLevel = 10;
    // 연속 하락
    boolean downTwice = false;
    // 무조건 성공
    NBTCompound itemTag = NBTAPI.getMainCompound(itemStack);
    NBTCompound reinforceTag = NBTAPI.getCompound(itemTag, CucumberyTag.REINFORCE_TAG);
    {
      if (reinforceTag == null)
      {
        MessageUtil.sendError(player, "강화를 할 수 없는 아이템입니다");
        Variable.scrollReinforcing.remove(uuid);
        return;
      }
      try
      {
        current = reinforceTag.getInteger(CucumberyTag.REINFORCE_CURRENT_TAG);
        max = reinforceTag.getInteger(CucumberyTag.REINFORCE_MAX_TAG);
        type = ReinforceType.valueOf(reinforceTag.getString(CucumberyTag.REINFORCE_REINFORCE_TYPE_TAG).toUpperCase());
      }
      catch (Exception e)
      {
        MessageUtil.sendError(player, "강화를 할 수 없는 아이템입니다");
        Variable.scrollReinforcing.remove(uuid);
        return;
      }
      try
      {
        itemType = switch (reinforceTag.getString(CucumberyTag.REINFORCE_ITEM_TYPE_TAG))
                {
                  case "검" -> ItemType.SWORD;
                  case "활" -> ItemType.BOW;
                  case "투구" -> ItemType.HELMET;
                  case "흉갑" -> ItemType.CHESTPLATE;
                  case "각반" -> ItemType.LEGGINGS;
                  case "부츠" -> ItemType.BOOTS;
                  case "방패" -> ItemType.SHIELD;
                  default -> ItemType.ETC;
                };
      }
      catch (Exception ignored)
      {
      }
      try
      {
        itemLevel = reinforceTag.getInteger(CucumberyTag.REINFORCE_ITEM_LEVEL_TAG);
      }
      catch (Exception ignored)
      {
      }
      try
      {
        downTwice = reinforceTag.getInteger(CucumberyTag.REINFORCE_DROP_AMOUNT_TAG) > 1;
      }
      catch (Exception ignored)
      {
      }
    }
    if (current >= max)
    {
      if (operationType == OperationType.CONTINUE || operationType == OperationType.OBSERVE)
      {
        MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "장비가 한계까지 강화되어 더 이상 강화할 수 없습니다");
      }
      else
      {
        MessageUtil.sendError(player, "이미 한계까지 강화되어 더 이상 강화할 수 없습니다");
      }
      Variable.scrollReinforcing.remove(uuid);
      return;
    }
    double[] chance = switch (type)
            {
              case STARFORCE -> CommandReinforce.getStarForceInfo(current);
              case SUPERIOR -> CommandReinforce.getSuperiorInfo(current);
              case CUCUMBERFORCE -> CommandReinforce.getCucumberyInfo(current);
              case EBEBEB -> CommandReinforce.getEBEBEBInfo(current);
            };

    double success = chance[0], failKeep = chance[1], failDown = chance[2], destroy = chance[3];
    double cost = CommandReinforce.getCost(current, itemLevel, type);
    boolean antiDestroyUsable = (type == ReinforceType.STARFORCE && current >= 12 && current <= 16) || (type == ReinforceType.CUCUMBERFORCE && current >= 24 && current <= 33);
    boolean antiDestApplicable = CommandReinforce.ANTI_DESTRUCTION_ENABLED.contains(uuid) && antiDestroyUsable;
    boolean starforceEvent = type == ReinforceType.STARFORCE && CustomEffectManager.hasEffect(player, CustomEffectTypeReinforce.STARFORCE_5_10_15) && (
            current == 5 || current == 10 || current == 15
    );
    boolean reinforceOpMode = CustomEffectManager.hasEffect(player, CustomEffectTypeReinforce.REINFORCE_OP_MODE);
    boolean costDoubled = antiDestApplicable && !downTwice && !starforceEvent;
    // 파괴 방지 기능은 가격이 100% 증가하고 이 가격은 할인의 영향을 받지 않음
    if (costDoubled)
    {
      cost += CommandReinforce.getCost(current, itemLevel, type);
    }
    double discount = 0d;
    List<Component> discountReason = new ArrayList<>();
    PotionEffect potionEffect = player.getPotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE);
    if (potionEffect != null && current < 17)
    {
      discount += potionEffect.getAmplifier() + 1d;
      discountReason.add(ComponentUtil.create(potionEffect));
    }
    Calendar calendar = Calendar.getInstance();
    if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
    {
      discount += 20d;
      discountReason.add(ComponentUtil.translate("일요일 20% 할인"));
    }
    if (type == ReinforceType.STARFORCE && CustomEffectManager.hasEffect(player, CustomEffectTypeReinforce.STARFORCE_30_SALE))
    {
      discountReason.add(ComponentUtil.create(CustomEffectManager.getEffect(player, CustomEffectTypeReinforce.STARFORCE_30_SALE)));
      discount += 30d;
    }
    // 슈페리얼은 할인 적용 전혀 없음
    if (type == ReinforceType.SUPERIOR)
    {
      discount = 0d;
      discountReason.clear();
    }
    // 90% 이상 할인 불가능
    if (discount > 90d)
    {
      discount = 90d;
    }
    if (costDoubled)
    {
      discount /= 2d;
    }
    double originalCost = cost;
    cost *= 1d - (discount / 100d);
    double playerMoney = Cucumbery.using_Vault_Economy ? Cucumbery.eco.getBalance(player) : 0d;
    String costString = Constant.THE_COLOR_HEX + Constant.Sosu2.format(cost), moneyString = Constant.THE_COLOR_HEX + Constant.Sosu2.format(playerMoney);
    if (cost > playerMoney)
    {
      if (operationType == OperationType.OBSERVE)
      {
        if (costDoubled && cost - CommandReinforce.getCost(current, itemLevel, type) <= playerMoney)
        {
          MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, Constant.SEPARATOR);
          MessageUtil.info(player, "소지 금액이 부족하여 파괴 방지 기능을 사용할 수 없습니다. 소지 금액 : %s원, 강화 비용 : %s원", moneyString, costString);
        }
        else
        {
          MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, Constant.SEPARATOR);
          MessageUtil.info(player, "소지 금액이 부족하여 아이템을 강화할 수 없습니다. 소지 금액 : %s원, 강화 비용 : %s원", moneyString, costString);
        }
      }
      else if (operationType == OperationType.CONTINUE)
      {
        if (costDoubled && cost - CommandReinforce.getCost(current, itemLevel, type) <= playerMoney)
        {
          ANTI_DESTRUCTION_ENABLED.remove(uuid);
          MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, Constant.SEPARATOR);
          MessageUtil.sendWarn(player, "소지 금액이 부족하여 더 이상 파괴 방지 기능을 사용할 수 없습니다. 소지 금액 : %s원, 강화 비용 : %s원", moneyString, costString);
          CustomEffectManager.addEffect(player, CustomEffectTypeReinforce.ANTI_DESTRUCTION_DISABLED_WARNING);
          operate(player, OperationType.CONTINUE);
          return;
        }
        MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, Constant.SEPARATOR);
        MessageUtil.info(player, "소지 금액이 부족하여 더 이상 아이템을 강화할 수 없습니다. 소지 금액 : %s원, 강화 비용 : %s원", moneyString, costString);
        Variable.scrollReinforcing.remove(uuid);
        return;
      }
      else
      {
        MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, Constant.SEPARATOR);
        MessageUtil.sendError(player, "소지 금액이 부족하여 아이템을 강화할 수 없습니다. 소지 금액 : %s원, 강화 비용 : %s원", moneyString, costString);
        Variable.scrollReinforcing.remove(uuid);
        return;
      }
    }
    if (operationType != OperationType.OPERATE)
    {
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, Constant.SEPARATOR);
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, REINFORCE_INFO, Constant.THE_COLOR_HEX + current, Constant.THE_COLOR_HEX + (current + 1), Constant.THE_COLOR_HEX + max);
      switch (type)
      {
        case STARFORCE -> {
          if (current >= 20)
          {
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, STARFORCE_20_INFO);
          }
          else if (current >= 15)
          {
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, STARFORCE_15_INFO);
          }
          else if (current >= 10)
          {
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, STARFORCE_10_INFO);
          }
        }
        case CUCUMBERFORCE -> {
          if (current >= 40)
          {
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, CUCUMBER_FORCE_40_INFO);
          }
          else if (current >= 30)
          {
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, CUCUMBER_ORCE_30_INFO);
          }
          else if (current >= 20)
          {
            MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, CUCUMBER_FORCE_20_INFO);
          }
        }
      }
    }
    if (reinforceOpMode && operationType != OperationType.OPERATE)
    {
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "rg255,204;강화 무조건 성공 적용 중! (%s)", CustomEffectManager.getEffect(player, CustomEffectTypeReinforce.REINFORCE_OP_MODE));
    }
    else if (downTwice)
    {
      CHANCE_TIME.add(uuid);
      success = 100d;
      failDown = 0d;
      failKeep = 0d;
      destroy = 0d;
      if (operationType != OperationType.OPERATE)
      {
        MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, ComponentUtil.translate("rg255,204;연속 2회 하락 CHANCE TIME!").hoverEvent(
                ComponentUtil.translate("강화에 연속 2배 실패하여 다음")
                        .append(Component.text("\n")).append(ComponentUtil.translate("강화의 성공 확률이 100%가 됩니다"))));
      }
    }
    else if (starforceEvent)
    {
      success = 100d;
      failDown = 0d;
      failKeep = 0d;
      destroy = 0d;
      if (operationType != OperationType.OPERATE)
      {
        MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "rg255,204;스타포스 이벤트 적용 중! (%s)", CustomEffectManager.getEffect(player, CustomEffectTypeReinforce.STARFORCE_5_10_15));
      }
    }
    // 파괴 방지 사용
    else if (antiDestApplicable)
    {
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
      destroy = 0d;
    }
    if (operationType != OperationType.OPERATE)
    {
      if (failDown == 0d && destroy == 0d)
      {
        MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, SUCCESS);
      }
      else if (failDown > 0 && destroy == 0 && failKeep > 0)
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
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, CommandReinforceConstantsAndUtils.REINFORCE_SUCCESS_CHANCE, Constant.THE_COLOR_HEX + Constant.Sosu2.format(success) + "%");
      if (failKeep > 0)
      {
        MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, CommandReinforceConstantsAndUtils.REINFORCE_FAILURE_KEEP_CHANCE, Constant.THE_COLOR_HEX + Constant.Sosu2.format(failKeep) + "%");
      }
      if (failDown > 0)
      {
        MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, CommandReinforceConstantsAndUtils.REINFORCE_FAILURE_DROP_CHANCE, Constant.THE_COLOR_HEX + Constant.Sosu2.format(failDown) + "%");
      }
      if (destroy > 0)
      {
        MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, CommandReinforceConstantsAndUtils.REINFORCE_DESTRUCTION_DROP_CHANCE, Constant.THE_COLOR_HEX + Constant.Sosu2.format(destroy) + "%");
      }
      List<String> options = CommandReinforce.convertOptions(CommandReinforce.getOptions(type, itemType, current, itemLevel), true);
      for (int i = 0; i < options.size(); i++)
      {
        if (i >= 5)
        {
          Component extra = Component.empty();
          for (String op : options)
          {
            extra = extra.append(Component.text("\n")).append(Component.text(op));
          }
          MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, ComponentUtil.translate("container.shulkerBox.more", options.size() - 5)
                  .hoverEvent(ComponentUtil.translate("강화 옵션 : %s개", options.size())
                          .append(extra)
                  )
          );
          break;
        }
        String option = options.get(i);
        MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, option);
      }
      if (operationType == OperationType.OBSERVE)
      {
        CHANCE_TIME.remove(uuid);
        Variable.scrollReinforcing.remove(uuid);
        REINFORCE_OPERATING.remove(uuid);
        MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, Constant.SEPARATOR);
        return;
      }
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, Constant.SEPARATOR);
      Component item = ComponentUtil.create(player, itemStack);
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "                           [%s]", ComponentUtil.translate("rg255,204;아이템 미리보기").hoverEvent(item.hoverEvent()).clickEvent(item.clickEvent()));
      boolean starCatchAvailable = !CHANCE_TIME.contains(uuid) && (starforceEvent || success < 100d), antiDestAvailable = (ANTI_DESTRUCTION_ENABLED.contains(uuid) || destroy > 0d) && antiDestroyUsable;
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, optionButton(
              starCatchAvailable ? (NO_STAR_CATCH_ENABLED.contains(uuid) ?
                      OPTION_BUTTON_DONT_USE
                              .clickEvent(ClickEvent.runCommand(Constant.REINFORCE_DO_NOT_USE_DISABLE_STAR_CATCH)) :
                      OPTION_BUTTON_USE
                              .clickEvent(ClickEvent.runCommand(Constant.REINFORCE_USE_DISABLE_STAR_CATCH))) :
                      OPTION_BUTTON_DISABLED,

              antiDestAvailable ? (ANTI_DESTRUCTION_ENABLED.contains(uuid) ?
                      OPTION_BUTTON_DONT_USE
                              .clickEvent(ClickEvent.runCommand(Constant.REINFORCE_DO_NOT_USE_ANTI_DESTRUCTION)) :
                      OPTION_BUTTON_USE
                              .clickEvent(ClickEvent.runCommand(Constant.REINFORCE_USE_ANTI_DESTRUCTION))) :
                      OPTION_BUTTON_DISABLED
      ));
      Component costInfo = ComponentUtil.translate(MONEY_INFO, moneyString, cost == originalCost ? costString : Constant.THE_COLOR_HEX + "&m" + Constant.Sosu2.format(originalCost) + Constant.THE_COLOR_HEX + " " + costString);
      if (cost != originalCost)
      {
        Component hover = ComponentUtil.translate("할인 비율 정보");
        hover = hover.append(Component.text("\n")).append(ComponentUtil.translate("할인 비율 : %s, 최대 할인 가격 : %s", Constant.THE_COLOR_HEX + Constant.Sosu2.format(discount) + "%", "rg255,204;90%"));
        hover = hover.append(Component.text("\n")).append(ComponentUtil.translate("rg255,204;주의! 파괴 방지 비용은 할인의 영향을 받지 않습니다"));
        if (!discountReason.isEmpty())
        {
          hover = hover.append(Component.text("\n\n"));
          hover = hover.append(ComponentUtil.translate("적용 중인 할인 요인들"));
          for (Component reason : discountReason)
          {
            hover = hover.append(Component.text("\n")).append(reason);
          }
        }
        costInfo = costInfo.hoverEvent(hover);
      }
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, costInfo);
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "");
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, START_BUTTON);
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, Constant.SEPARATOR);
      return;
    }
    if (!Variable.scrollReinforcing.contains(uuid))
    {
      MessageUtil.sendError(player, "강화를 하고 있지 않습니다");
      return;
    }
    if (CommandReinforce.REINFORCE_OPERATING.contains(uuid))
    {
      MessageUtil.sendError(player, "이미 강화를 시도하고 있습니다");
      return;
    }
    if (CommandReinforce.CHANCE_TIME.contains(uuid))
    {
      CommandReinforce.CHANCE_TIME.remove(uuid);
      player.stopSound("reinforce_chancetime");
      for (int i = 0; i <= 20; i++)
      {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () ->
                MessageUtil.sendTitle(player, "", "", 0, 1, 0), i);
      }
    }
    CommandReinforce.REINFORCE_OPERATING.add(uuid);
    boolean use = UserData.SERVER_RESOURCEPACK.getBoolean(uuid);
    Method.reinforceSound(player, Method.ReinforceSound.OPERATION, use, Method.ReinforceType.COMMAND);
    if (CustomEffectManager.hasEffect(player, CustomEffectTypeReinforce.STAR_CATCH_SUCCESS))
    {
      if (success * 1.05 <= 100)
      {
        success *= 1.05;
      }
      double otherSum = destroy + failKeep + failDown;
      double left = 100d - success;
      if (otherSum > 0d)
      {
        destroy = destroy / otherSum * left;
        failKeep = failKeep / otherSum * left;
      }
      CustomEffectManager.removeEffect(player, CustomEffectTypeReinforce.STAR_CATCH_SUCCESS);
    }
    if (CustomEffectManager.hasEffect(player, CustomEffectTypeReinforce.STAR_CATCH_FINISHED))
    {
      CustomEffectManager.removeEffect(player, CustomEffectTypeReinforce.STAR_CATCH_FINISHED);
    }
    double finalSuccess = success, finalDestroy = destroy, finalFailKeep = failKeep;
    ItemType finalItemType = itemType;
    int finalItemLevel = itemLevel;
    double finalCost = cost;
    // 강화 시도시마다 스타캐치 난이도 페널티 증가
    Integer i = Variable.starCatchPenalty.get(uuid);
    if (i == null)
    {
      i = 0;
    }
    Variable.starCatchPenalty.put(uuid, i + 1);
    Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
    {
      if (Cucumbery.using_Vault_Economy)
      {
        Cucumbery.eco.withdrawPlayer(player, finalCost);
      }
      REINFORCE_OPERATING.remove(player.getUniqueId());
      double random = Math.random() * 100D;
      double suc = finalSuccess / 10D, des = finalDestroy / 10D, fai = finalFailKeep / 10D;
      // bSuccess = (destroy < success && random > destroy && success + destroy >= random) || (destroy >= success && random < success);
      boolean bSuccess = (finalDestroy < finalSuccess && ((random > des && random <= suc + des) || (random > des + 10D && random <= suc + des + 10D) || (random > des + 20D && random <= suc + des + 20D) || (random > des + 30D && random <= suc + des + 30D) || (random > des + 40D && random <= suc + des + 40D) || (random > des + 50D && random <= suc + des + 50D) || (random > des + 60D && random <= suc + des + 60D) || (random > des + 70D && random <= suc + des + 70D) || (random > des + 80D && random <= suc + des + 80D) || (random > des + 90D && random <= suc + des + 90D))) || (finalDestroy >= finalSuccess && ((random < suc) || (random >= 10D && random < suc + 10D) || (random >= 20D && random < suc + 20D) || (random >= 30D && random < suc + 30D) || (random >= 40D && random < suc + 40D) || (random >= 50D && random < suc + 50D) || (random >= 60D && random < suc + 60D) || (random >= 70D && random < suc + 70D) || (random >= 80D && random < suc + 80D) || (random >= 90D && random < suc + 75D)));
      // bFail = random > success + destroy && random <= success + destroy + fail;
      boolean bFail = (random > suc + des && random <= suc + des + fai) || (random >= suc + des + 10D && random <= suc + des + fai + 10D) || (random >= suc + des + 20D && random <= suc + des + fai + 20D) || (random >= suc + des + 30D && random <= suc + des + fai + 30D) || (random >= suc + des + 40D && random <= suc + des + fai + 40D) || (random >= suc + des + 50D && random <= suc + des + fai + 50D) || (random >= suc + des + 60D && random <= suc + des + fai + 60D) || (random >= suc + des + 70D && random <= suc + des + fai + 70D) || (random >= suc + des + 80D && random <= suc + des + fai + 80D || (random >= suc + des + 90D && random <= suc + des + fai + 90D));
      // bDestroy = (destroy < success && random < destroy) || (destroy >= success && random > success && random <= success + destroy);
      boolean bDestroy = finalDestroy < finalSuccess && (random < des || (random >= 10D && random < des + 10D) || (random >= 20D && random < des + 20D) || (random >= 30D && random < des + 30D) || (random >= 40D && random < des + 40D) || (random >= 50D && random < des + 50D) || (random >= 60D && random < des + 60D) || (random >= 70D && random < des + 70D) || (random >= 80D && random < des + 80D) || (random >= 90D && random < des + 90D)) || finalDestroy >= finalSuccess && ((random >= suc && random < suc + des) || (random >= suc + 10D && random < suc + des + 10D) || (random >= suc + 20D && random < suc + des + 20D) || (random >= suc + 30D && random < suc + des + 30D) || (random >= suc + 40D && random < suc + des + 40D) || (random >= suc + 50D && random < suc + des + 50D) || (random >= suc + 60D && random < suc + des + 60D) || (random >= suc + 70D && random < suc + des + 70D) || (random >= suc + 80D && random < suc + des + 80D) || (random >= suc + 90D && random < suc + des + 90D));
      if (reinforceOpMode)
      {
        bSuccess = true;
      }
      try
      {
        NBTItem nbtItem = new NBTItem(itemStack, true);
        NBTCompound itemTag2 = nbtItem.getCompound(CucumberyTag.KEY_MAIN);
        NBTCompound reinforceTag2 = itemTag2.getCompound(CucumberyTag.REINFORCE_TAG);
        if (bSuccess) // 성공
        {
          double[] options = CommandReinforce.getOptions(type, finalItemType, current, finalItemLevel);
          reinforceTag2.setInteger(CucumberyTag.REINFORCE_CURRENT_TAG, current + 1);
          List<String> optionsStr = CommandReinforce.convertOptions(options, true);
          reinforceTag2.removeKey(CucumberyTag.REINFORCE_DROP_AMOUNT_TAG);
          for (String option : optionsStr)
          {
            String[] split = option.split(" : ");
            String key = split[0], rawValue = split[1];
            if (rawValue.startsWith("+"))
            {
              rawValue = rawValue.substring(1);
            }
            Double value = reinforceTag2.getDouble(key);
            if (value == null)
            {
              value = 0d;
            }
            value += Double.parseDouble(rawValue);
            if (value == 0d)
            {
              reinforceTag2.removeKey(key);
            }
            else
            {
              reinforceTag2.setDouble(key, value);
            }
            if (reinforceTag2.getKeys().isEmpty())
            {
              itemTag2.removeKey(CucumberyTag.REINFORCE_TAG);
            }
          }
          Method.reinforceSound(player, Method.ReinforceSound.SUCCESS, use, Method.ReinforceType.COMMAND);
          successTitle(player);
        }
        else if (bDestroy) // 파괴
        {
          destroyTitle(player);
          player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
          Method.reinforceSound(player, Method.ReinforceSound.DESTROY, use, Method.ReinforceType.COMMAND);
          MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, MESSAGE_DESTROY);
          MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, Constant.SEPARATOR);
          Variable.scrollReinforcing.remove(uuid);
          return;
        }
        else if (bFail) // 실패 - 유지
        {
          Method.reinforceSound(player, Method.ReinforceSound.FAIL, use, Method.ReinforceType.COMMAND);
          failTitle(player, false);
        }
        else // 실패 - 하락
        {
          failTitle(player, true);
          double[] options = getOptions(type, finalItemType, current - 1, finalItemLevel);
          reinforceTag2.setInteger(CucumberyTag.REINFORCE_CURRENT_TAG, current - 1);
          List<String> optionsStr = convertOptions(options, false);
          for (String option : optionsStr)
          {
            String[] split = option.split(" : ");
            String key = split[0], rawValue = split[1];
            if (rawValue.startsWith("+"))
            {
              rawValue = rawValue.substring(1);
            }
            Double value = reinforceTag2.getDouble(key);
            if (value == null)
            {
              value = 0d;
            }
            value += Double.parseDouble(rawValue);
            if (value == 0d)
            {
              reinforceTag2.removeKey(key);
            }
            else
            {
              reinforceTag2.setDouble(key, value);
            }
            if (reinforceTag2.getKeys().isEmpty())
            {
              itemTag2.removeKey(CucumberyTag.REINFORCE_TAG);
            }
          }
          Integer dropStack = reinforceTag2.getInteger(CucumberyTag.REINFORCE_DROP_AMOUNT_TAG);
          if (dropStack == null)
          {
            dropStack = 0;
          }
          dropStack++;
          reinforceTag2.setInteger(CucumberyTag.REINFORCE_DROP_AMOUNT_TAG, dropStack);
          Method.reinforceSound(player, Method.ReinforceSound.FAIL, use, Method.ReinforceType.COMMAND);
        }
      }
      catch (Exception e)
      {
Cucumbery.getPlugin().getLogger().warning(        e.getMessage());
      }
      ItemLore.setItemLore(itemStack, new ItemLoreView(player));
      player.getInventory().setItemInMainHand(itemStack);
      operate(player, OperationType.CONTINUE);
    }, 20L);
  }

  /**
   * To show info or real operation
   */
  public enum OperationType
  {
    /**
     * 최초 명령어 실행
     */
    SHOW,
    /**
     * 명령어 실행 후 실제 작업
     */
    OPERATE,
    /**
     * 실제 작업 후 다시 명령어 실행 부분
     */
    CONTINUE,
    /**
     * /강화 상태 명령어 사용 시
     */
    OBSERVE,
  }
}

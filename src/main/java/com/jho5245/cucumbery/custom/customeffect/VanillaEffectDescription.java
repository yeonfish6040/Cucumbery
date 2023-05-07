package com.jho5245.cucumbery.custom.customeffect;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.custom_mining.MiningManager;
import com.jho5245.cucumbery.custom.customeffect.custom_mining.MiningResult;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeCustomMining;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.PlaceHolderUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.TranslatableKeyParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VanillaEffectDescription
{
  @NotNull
  public static Component getDescription(@NotNull PotionEffectType type)
  {
    return getDescription(type, null);
  }

  @NotNull
  public static Component getDescription(@NotNull PotionEffectType type, @Nullable Player viewer)
  {
    boolean hasCustomMining = viewer != null && CustomEffectManager.hasEffect(viewer, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE);
    if (type.equals(PotionEffectType.SPEED))
    {
      return ComponentUtil.translate("이동 속도가 증가합니다. %s와(과) 곱적용됩니다", "&c" + TranslatableKeyParser.getKey(PotionEffectType.SLOW));
    }
    if (type.equals(PotionEffectType.SLOW))
    {
      return ComponentUtil.translate("이동 속도가 감소합니다. %s와(과) 곱적용됩니다", "&a" + TranslatableKeyParser.getKey(PotionEffectType.SPEED));
    }
    if (type.equals(PotionEffectType.FAST_DIGGING))
    {
      return ComponentUtil.translate("채광 속도와 공격 속도가 증가합니다. %s와(과) 곱적용됩니다", "translate:&c" + TranslatableKeyParser.getKey(PotionEffectType.SLOW_DIGGING));
    }
    if (type.equals(PotionEffectType.SLOW_DIGGING))
    {
      return ComponentUtil.translate("채광 속도와 공격 속도가 감소합니다. %s와(과) 곱적용됩니다", "translate:&a" + TranslatableKeyParser.getKey(PotionEffectType.FAST_DIGGING));
    }
    if (type.equals(PotionEffectType.INCREASE_DAMAGE))
    {
      return ComponentUtil.translate("근거리 대미지가 증가합니다");
    }
    if (type.equals(PotionEffectType.WEAKNESS))
    {
      return ComponentUtil.translate("근거리 대미지가 감소합니다");
    }
    if (type.equals(PotionEffectType.HEAL))
    {
      return ComponentUtil.translate("HP가 회복됩니다. 언데드 개체는 HP가 감소합니다");
    }
    if (type.equals(PotionEffectType.HARM))
    {
      return ComponentUtil.translate("HP가 감소합니다. 언데드 개체는 HP가 회복됩니다");
    }
    if (type.equals(PotionEffectType.JUMP))
    {
      return ComponentUtil.translate("점프 높이가 증가하고 낙하 피해량이 감소합니다");
    }
    if (type.equals(PotionEffectType.CONFUSION))
    {
      return ComponentUtil.translate("화면이 어질어질해져 사물 분간이 어려워집니다");
    }
    if (type.equals(PotionEffectType.REGENERATION))
    {
      return ComponentUtil.translate("일정 시간마다 HP가 회복됩니다");
    }
    if (type.equals(PotionEffectType.DAMAGE_RESISTANCE))
    {
      return ComponentUtil.translate("받는 피해가 감소합니다");
    }
    if (type.equals(PotionEffectType.FIRE_RESISTANCE))
    {
      return ComponentUtil.translate("불과 용암에 의한 피해를 입지 않고,")
              .append(Component.text("\n"))
              .append(ComponentUtil.translate("용암에서의 시야가 소폭 완화됩니다"));
    }
    if (type.equals(PotionEffectType.WATER_BREATHING))
    {
      return ComponentUtil.translate("익사 피해를 입지 않고 물 속에서 숨을 쉴 수 있게 됩니다");
    }
    if (type.equals(PotionEffectType.INVISIBILITY))
    {
      return ComponentUtil.translate("몸이 투명해집니다. 일부 장비나 효과는 보일 수 있습니다");
    }
    if (type.equals(PotionEffectType.BLINDNESS))
    {
      return ComponentUtil.translate("시야가 굉장히 좁아지고, 달리기와 치명타 공격을 할 수 없게 됩니다");
    }
    if (type.equals(PotionEffectType.NIGHT_VISION))
    {
      return ComponentUtil.translate("어두운 곳과 물 속을 밝게 볼 수 있습니다");
    }
    if (type.equals(PotionEffectType.HUNGER))
    {
      return ComponentUtil.translate("일정 시간마다 음식 포인트 또는 포화도가 감소합니다");
    }
    if (type.equals(PotionEffectType.POISON))
    {
      return ComponentUtil.translate("일정 시간마다 HP가 감소합니다. 1 미만으로 감소하지 않습니다")
              .append(Component.text("\n"))
              .append(ComponentUtil.translate("언데드 개체와 거미는 이 효과에 면역입니다"));
    }
    if (type.equals(PotionEffectType.WITHER))
    {
      return ComponentUtil.translate("일정 시간마다 HP가 감소합니다");
    }

    if (type.equals(PotionEffectType.HEALTH_BOOST))
    {
      return ComponentUtil.translate("최대 HP가 증가합니다");
    }
    if (type.equals(PotionEffectType.ABSORPTION))
    {
      return ComponentUtil.translate("최대 HP가 증가합니다. 해당 HP는 잃으면 회복되지 않습니다");
    }

    if (type.equals(PotionEffectType.SATURATION))
    {
      return ComponentUtil.translate("음식 포인트와 포화도가 회복됩니다");
    }

    if (type.equals(PotionEffectType.GLOWING))
    {
      return ComponentUtil.translate("몸에서 블록 너머로도 보이는 빛이 납니다");
    }

    if (type.equals(PotionEffectType.LEVITATION))
    {
      return ComponentUtil.translate("공중으로 떠오릅니다");
    }

    if (type.equals(PotionEffectType.SLOW_FALLING))
    {
      return ComponentUtil.translate("낙하 속도가 감소하고 낙하 피해를 받지 않습니다");
    }
    if (type.equals(PotionEffectType.LUCK))
    {
      return ComponentUtil.translate("행운이 증가합니다");
    }
    if (type.equals(PotionEffectType.UNLUCK))
    {
      return ComponentUtil.translate("행운이 감소합니다");
    }

    if (type.equals(PotionEffectType.CONDUIT_POWER))
    {
      return ComponentUtil.translate("채광 속도가 증가하고 어두운 곳과 물 속을 밝게 볼 수 있습니다")
              .append(Component.text("\n"))
              .append(ComponentUtil.translate("또한, 익사 피해를 입지 않고 물 속에서 숨을 쉴 수 있게 됩니다"));
    }
    if (type.equals(PotionEffectType.DOLPHINS_GRACE))
    {
      return ComponentUtil.translate("물 속에서 이동 속도가 증가합니다");
    }

    if (type.equals(PotionEffectType.BAD_OMEN))
    {
      return ComponentUtil.translate("우민 대장을 죽였습니다. 마을로 가면 습격이 발생합니다");
    }
    if (type.equals(PotionEffectType.HERO_OF_THE_VILLAGE))
    {
      return ComponentUtil.translate("습격을 막아냈습니다! 주민의 거래 가격이 할인됩니다")
              .append(Component.text("\n"))
              .append(ComponentUtil.translate("추가로, 17성 미만의 스타포스 강화 비용이 할인됩니다"));
    }
    if (type.equals(PotionEffectType.DARKNESS))
    {
      return ComponentUtil.translate("주변이 어두워집니다");
    }
    return Component.empty();
  }

  @NotNull
  public static Component getDescription(@NotNull PotionEffect potionEffect)
  {
    return getDescription(potionEffect, null);
  }

  @NotNull
  public static Component getDescription(@NotNull PotionEffect potionEffect, @Nullable Player viewer)
  {
    boolean hasCustomMining = viewer != null && CustomEffectManager.hasEffect(viewer, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE);
    PotionEffectType type = potionEffect.getType();
    int amplifier = potionEffect.getAmplifier();
    int duration = potionEffect.getDuration();
    if (type.equals(PotionEffectType.SPEED))
    {
      return ComponentUtil.translate("이동 속도가 %s 증가합니다. %s와(과) 곱적용됩니다", Constant.THE_COLOR_HEX + (amplifier + 1) * 20 + "%", PotionEffectType.SLOW);
    }
    if (type.equals(PotionEffectType.SLOW))
    {
      return ComponentUtil.translate("이동 속도가 %s 감소합니다. %s와(과) 곱적용됩니다", Constant.THE_COLOR_HEX + Math.min(100, (amplifier + 1) * 15) + "%", PotionEffectType.SPEED);
    }
    if (type.equals(PotionEffectType.FAST_DIGGING))
    {
      String display;
      if (hasCustomMining)
      {
        Block block = viewer.getTargetBlockExact(5);
        MiningResult miningResult = MiningManager.getMiningInfo(viewer, block != null ? block.getLocation() : viewer.getLocation().add(0, -1, 0), true);
        String formula = Cucumbery.config.getString("custom-mining.haste", "0.2*%mining_speed%*%level%")
                .replace("%level%", (amplifier + 1) + "").replace("%mining_speed%", (miningResult != null ? miningResult.miningSpeedBeforeHaste() : 50) + "");
        float value = 0f;
        try
        {
          value = Float.parseFloat(PlaceHolderUtil.evalString("{eval:" + formula + "}"));
        }
        catch (NumberFormatException e)
        {
          MessageUtil.sendWarn(Bukkit.getConsoleSender(), "config.yml 파일에서 custom-mining.haste의 값이 잘못 지정되어 있습니다!");
        }
        display = Constant.Sosu2.format(value);
      }
      else
      {
        display = (amplifier + 1) * 20 + "%";
      }
      return ComponentUtil.translate("채광 속도가 %s 증가하고 공격 속도가", Constant.THE_COLOR_HEX + display)
              .append(Component.text("\n"))
              .append(ComponentUtil.translate("%s 증가합니다. %s와(과) 곱적용됩니다", Constant.THE_COLOR_HEX + (amplifier + 1) * 10 + "%", PotionEffectType.SLOW_DIGGING));
    }
    if (type.equals(PotionEffectType.SLOW_DIGGING))
    {
      return ComponentUtil.translate("채광 속도가 %s 감소하고 공격 속도가", Constant.THE_COLOR_HEX + Constant.Sosu2.format(100 - Math.pow(0.3, amplifier + 1) * 100) + "%")
              .append(Component.text("\n"))
              .append(ComponentUtil.translate("%s 감소합니다. %s와(과) 곱적용됩니다", Constant.THE_COLOR_HEX + Math.min(10, amplifier + 1) * 10 + "%", PotionEffectType.FAST_DIGGING));
    }
    if (type.equals(PotionEffectType.INCREASE_DAMAGE))
    {
      return ComponentUtil.translate("근거리 대미지가 %s 증가합니다", Constant.THE_COLOR_HEX + (amplifier + 1) * 3);
    }
    if (type.equals(PotionEffectType.WEAKNESS))
    {
      return ComponentUtil.translate("근거리 대미지가 %s 감소합니다", Constant.THE_COLOR_HEX + (amplifier + 1) * 4);
    }
    if (type.equals(PotionEffectType.HEAL))
    {
      return ComponentUtil.translate((duration > 2 ? "지속적으로 " : "") + "HP가 %s 회복됩니다. 언데드 개체는 HP가 %s 감소합니다",
              Constant.THE_COLOR_HEX + Constant.Sosu2Floor.format(Math.pow(2, amplifier + 2)), Constant.THE_COLOR_HEX + Constant.Sosu2Floor.format(3 * Math.pow(2, amplifier + 1)));
    }
    if (type.equals(PotionEffectType.HARM))
    {
      return ComponentUtil.translate((duration > 2 ? "지속적으로 " : "") + "HP가 %s 감소합니다. 언데드 개체는 HP가 %s 회복됩니다",
              Constant.THE_COLOR_HEX + Constant.Sosu2Floor.format(3 * Math.pow(2, amplifier + 1)), Constant.THE_COLOR_HEX + Constant.Sosu2Floor.format(Math.pow(2, amplifier + 2)));
    }
    if (type.equals(PotionEffectType.JUMP))
    {
      TranslatableComponent component = ComponentUtil.translate("점프 높이가 %s 증가하고 낙하 피해량이 %s 감소합니다",
              Constant.THE_COLOR_HEX + (amplifier + 1) * 50 + "%", Constant.THE_COLOR_HEX + (amplifier + 1));
      if (amplifier == 255)
      {
        component = ComponentUtil.translate("낙하 피해량이 %s 감소합니다", "rg255,204;256");
      }
      else if (amplifier >= 251)
      {
        int i = switch (amplifier)
                {
                  case 252 -> 75;
                  case 253 -> 50;
                  case 254 -> 25;
                  default -> 95;
                };
        component = ComponentUtil.translate("점프 높이가 %s 감소하고 낙하 피해량이 %s 감소합니다",
                Constant.THE_COLOR_HEX + i + "%", Constant.THE_COLOR_HEX + (amplifier + 1));
      }
      else if (amplifier >= 128)
      {
        component = ComponentUtil.translate("점프를 할 수 없게 되는 대신 낙하 피해량이 %s 감소합니다", Constant.THE_COLOR_HEX + (amplifier + 1));
      }
      else if (amplifier >= 33)
      {
        component = component.append(Component.text("\n"));
        component = component.append(ComponentUtil.translate("&c주의! 충분한 방어 수단이 없으면 낙사할 수 있습니다!"));
      }
      else if (amplifier >= 15)
      {
        component = component.append(Component.text("\n"));
        component = component.append(ComponentUtil.translate("&c주의! 충분한 방어 수단이 없으면 낙하 피해를 입을 수 있습니다!"));
      }
      return component;
    }
    if (type.equals(PotionEffectType.REGENERATION))
    {
      while (amplifier >= 32)
      {
        amplifier -= 32;
      }
      int tick = 50;
      if (amplifier > 5)
      {
        amplifier = 5;
      }
      for (int i = 0; i < amplifier; i++)
      {
        tick /= 2;
      }
      return ComponentUtil.translate("약 %s 마다 HP가 회복됩니다", Constant.THE_COLOR_HEX + Constant.Sosu2.format(tick / 20d) + "초");
    }
    if (type.equals(PotionEffectType.DAMAGE_RESISTANCE))
    {
      return ComponentUtil.translate("받는 피해가 %s 감소합니다", Constant.THE_COLOR_HEX + Math.min(5, amplifier + 1) * 20 + "%");
    }
    if (type.equals(PotionEffectType.HUNGER))
    {
      return ComponentUtil.translate("약 %s 마다 음식 포인트 또는 포화도가 감소합니다", Constant.THE_COLOR_HEX + Constant.Sosu2.format(40d / (amplifier + 1)) + "초");
    }
    if (type.equals(PotionEffectType.POISON))
    {
      while (amplifier >= 32)
      {
        amplifier -= 32;
      }
      int tick = 50;
      if (amplifier > 5)
      {
        amplifier = 5;
      }
      for (int i = 0; i < amplifier; i++)
      {
        tick /= 2;
      }
      return ComponentUtil.translate("약 %s 마다 HP가 감소합니다. 1 미만으로 감소하지 않습니다", Constant.THE_COLOR_HEX + Constant.Sosu2.format(tick / 20d) + "초")
              .append(Component.text("\n"))
              .append(ComponentUtil.translate("언데드 개체와 거미는 이 효과에 면역입니다"));
    }
    if (type.equals(PotionEffectType.WITHER))
    {
      while (amplifier >= 32)
      {
        amplifier -= 32;
      }
      int tick = 50;
      if (amplifier > 5)
      {
        amplifier = 5;
      }
      for (int i = 0; i < amplifier; i++)
      {
        tick /= 2;
      }
      return ComponentUtil.translate("약 %s 마다 HP가 감소합니다", Constant.THE_COLOR_HEX + Constant.Sosu2.format(tick / 20d) + "초");
    }
    if (type.equals(PotionEffectType.HEALTH_BOOST))
    {
      return ComponentUtil.translate("최대 HP가 %s 증가합니다", Constant.THE_COLOR_HEX + (amplifier + 1) * 4);
    }
    if (type.equals(PotionEffectType.ABSORPTION))
    {
      return ComponentUtil.translate("최대 HP가 %s 증가합니다. 해당 HP는 잃으면 회복되지 않습니다", Constant.THE_COLOR_HEX + (amplifier + 1) * 4);
    }
    if (type.equals(PotionEffectType.SATURATION))
    {
      return ComponentUtil.translate("음식 포인트가 %s 회복되고 포화도가 %s 회복됩니다", Constant.THE_COLOR_HEX + (amplifier + 1), Constant.THE_COLOR_HEX + (amplifier + 1) * 2);
    }
    if (type.equals(PotionEffectType.LEVITATION))
    {
      return ComponentUtil.translate("초당 약 %s 블록만큼 공중으로 떠오릅니다", Constant.Sosu2.format((amplifier + 1) * 0.9));
    }
    if (type.equals(PotionEffectType.LUCK))
    {
      return ComponentUtil.translate("행운이 %s 증가합니다", Constant.THE_COLOR_HEX + (amplifier + 1));
    }
    if (type.equals(PotionEffectType.UNLUCK))
    {
      return ComponentUtil.translate("행운이 %s 감소합니다", Constant.THE_COLOR_HEX + (amplifier + 1));
    }
    if (type.equals(PotionEffectType.CONDUIT_POWER))
    {
      return ComponentUtil.translate("채광 속도가 %s 증가하고 공격 속도가 %s 증가합니다. %s와(과) 곱적용됩니다. 또한, 어두운", Constant.THE_COLOR_HEX + (amplifier + 1) * 20 + "%", Constant.THE_COLOR_HEX + (amplifier + 1) * 10 + "%", PotionEffectType.SLOW_DIGGING)
              .append(Component.text("\n"))
              .append(ComponentUtil.translate("곳과 물 속을 밝게 볼 수 있고 익사 피해를 입지 않고 물 속에서 숨을 쉴 수 있게 됩니다"));
    }
    if (type.equals(PotionEffectType.HERO_OF_THE_VILLAGE))
    {
      return ComponentUtil.translate("습격을 막아냈습니다! 주민의 거래 가격이 %s 할인됩니다", Constant.THE_COLOR_HEX + Constant.Sosu2.format(Math.min(98.75, 30 + (amplifier) * 6.25)) + "%")
              .append(Component.text("\n"))
              .append(ComponentUtil.translate("추가로, 17성 미만의 스타포스 강화 비용이 %s 할인됩니다", Constant.THE_COLOR_HEX + Constant.Sosu2.format(amplifier + 1) + "%"));
    }
    return getDescription(potionEffect.getType());
  }
}

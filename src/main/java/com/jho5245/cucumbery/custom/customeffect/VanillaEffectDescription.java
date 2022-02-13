package com.jho5245.cucumbery.custom.customeffect;

import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class VanillaEffectDescription
{
  @NotNull
  public static Component getDescription(@NotNull PotionEffectType potionEffectType)
  {
    if (potionEffectType.equals(PotionEffectType.SPEED))
    {
      return ComponentUtil.translate("이동 속도가 증가합니다");
    }
    if (potionEffectType.equals(PotionEffectType.SLOW))
    {
      return ComponentUtil.translate("이동 속도가 감소합니다");
    }

    if (potionEffectType.equals(PotionEffectType.FAST_DIGGING))
    {
      return ComponentUtil.translate("채광 속도가 증가합니다");
    }
    if (potionEffectType.equals(PotionEffectType.SLOW_DIGGING))
    {
      return ComponentUtil.translate("채광 속도가 감소합니다");
    }

    if (potionEffectType.equals(PotionEffectType.INCREASE_DAMAGE))
    {
      return ComponentUtil.translate("근거리 공격 피해량이 증가합니다");
    }
    if (potionEffectType.equals(PotionEffectType.WEAKNESS))
    {
      return ComponentUtil.translate("근거리 공격 피해량이 감소합니다");
    }

    if (potionEffectType.equals(PotionEffectType.HEAL))
    {
      return ComponentUtil.translate("HP가 회복됩니다. 일부 개체는 피해를 입습니다");
    }
    if (potionEffectType.equals(PotionEffectType.HARM))
    {
      return ComponentUtil.translate("피해를 입습니다. 일부 개체는 HP가 회복됩니다");
    }

    if (potionEffectType.equals(PotionEffectType.JUMP))
    {
      return ComponentUtil.translate("점프 높이가 증가합니다");
    }

    if (potionEffectType.equals(PotionEffectType.CONFUSION))
    {
      return ComponentUtil.translate("화면이 어질어질해져 사물 분간이 어려워집니다");
    }

    if (potionEffectType.equals(PotionEffectType.REGENERATION))
    {
      return ComponentUtil.translate("HP가 점점 회복됩니다");
    }

    if (potionEffectType.equals(PotionEffectType.DAMAGE_RESISTANCE))
    {
      return ComponentUtil.translate("받는 피해가 감소합니다");
    }
    if (potionEffectType.equals(PotionEffectType.FIRE_RESISTANCE))
    {
      return ComponentUtil.translate("화염에 의한 피해를 입지 않습니다");
    }

    if (potionEffectType.equals(PotionEffectType.WATER_BREATHING))
    {
      return ComponentUtil.translate("물 속에서 숨을 쉴 수 있게 됩니다");
    }

    if (potionEffectType.equals(PotionEffectType.INVISIBILITY))
    {
      return ComponentUtil.translate("몸이 투명해집니다. 일부 장비나 효과는 보일 수 있습니다");
    }

    if (potionEffectType.equals(PotionEffectType.BLINDNESS))
    {
      return ComponentUtil.translate("시야가 굉장히 좁아지고, 달릴 수 없게 됩니다");
    }

    if (potionEffectType.equals(PotionEffectType.NIGHT_VISION))
    {
      return ComponentUtil.translate("어두운 곳도 밝게 볼 수 있습니다");
    }

    if (potionEffectType.equals(PotionEffectType.HUNGER))
    {
      return ComponentUtil.translate("음식 포인트와 포화도가 감소합니다");
    }

    if (potionEffectType.equals(PotionEffectType.POISON))
    {
      return ComponentUtil.translate("HP가 점점 감소합니다. 1 미만으로 감소하지 않습니다");
    }
    if (potionEffectType.equals(PotionEffectType.WITHER))
    {
      return ComponentUtil.translate("HP가 점점 빨려나가 죽어갑니다");
    }

    if (potionEffectType.equals(PotionEffectType.HEALTH_BOOST))
    {
      return ComponentUtil.translate("최대 HP가 증가합니다");
    }
    if (potionEffectType.equals(PotionEffectType.ABSORPTION))
    {
      return ComponentUtil.translate("30초마다 자동 갱신되는 회복할 수 없는 최대 HP가 증가합니다");
    }

    if (potionEffectType.equals(PotionEffectType.SATURATION))
    {
      return ComponentUtil.translate("음식 포인트와 포화도가 회복됩니다");
    }

    if (potionEffectType.equals(PotionEffectType.GLOWING))
    {
      return ComponentUtil.translate("몸에서 블록 너머로도 보이는 빛이 납니다");
    }

    if (potionEffectType.equals(PotionEffectType.LEVITATION))
    {
      return ComponentUtil.translate("몸이 공중으로 떠오릅니다");
    }
    if (potionEffectType.equals(PotionEffectType.SLOW_FALLING))
    {
      return ComponentUtil.translate("낙하 속도가 감소하고 낙하 피해를 받지 않습니다");
    }

    if (potionEffectType.equals(PotionEffectType.LUCK))
    {
      return ComponentUtil.translate("행운이 증가합니다");
    }
    if (potionEffectType.equals(PotionEffectType.UNLUCK))
    {
      return ComponentUtil.translate("행운이 감소합니다");
    }

    if (potionEffectType.equals(PotionEffectType.CONDUIT_POWER))
    {
      return ComponentUtil.translate("물 속에서 숨을 쉴 수 있고 시야가 밝아지며 채광 속도가 증가합니다");
    }
    if (potionEffectType.equals(PotionEffectType.DOLPHINS_GRACE))
    {
      return ComponentUtil.translate("물 속에서 이동 속도가 증가합니다");
    }

    if (potionEffectType.equals(PotionEffectType.BAD_OMEN))
    {
      return ComponentUtil.translate("우민 대장을 죽였습니다. 마을로 가면 습격이 발생합니다");
    }
    if (potionEffectType.equals(PotionEffectType.HERO_OF_THE_VILLAGE))
    {
      return ComponentUtil.translate("습격을 막아냈습니다! 주민이 거래를 싸게 해줍니다");
    }
    return Component.empty();
  }

  @NotNull
  public static Component getDescription(@NotNull PotionEffect potionEffect)
  {
    return getDescription(potionEffect.getType());
  }
}

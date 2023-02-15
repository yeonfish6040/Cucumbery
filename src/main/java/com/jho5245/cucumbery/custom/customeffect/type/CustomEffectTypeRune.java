package com.jho5245.cucumbery.custom.customeffect.type;

import com.jho5245.cucumbery.custom.customeffect.CustomEffect.DisplayType;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;

public class CustomEffectTypeRune extends CustomEffectType
{
  public static final CustomEffectType
          RUNE_COOLDOWN = new CustomEffectType("rune_cooldown", "봉인된 룬의 힘", builder().negative().keepOnDeath().description("일정 시간동안 룬을 사용할 수 없습니다").defaultDuration(20 * 60 * 15)),
          RUNE_USING = new CustomEffectType("rune_using", "룬 해방", builder().enumHidden().defaultDuration(20 * 10).defaultDisplayType(DisplayType.BOSS_BAR_ONLY)),
          RUNE_OCCUPIED = new CustomEffectType("rune_occupied", "룬 해방", builder().enumHidden().defaultDuration(20 * 10)),
          RUNE_EXPERIENCE = new CustomEffectType("rune_experience", "해방된 룬의 힘", builder().description("지속시간동안 경험치 획득량이 100% 증가합니다").defaultDuration(20 * 60 * 3).nonRemovable()),
          RUNE_DESTRUCTION = new CustomEffectType("rune_destruction", "파멸의 룬", builder().description("지속시간동안 대미지가 100% 증가합니다").defaultDuration(20 * 60 * 3)
                  .icon(CustomMaterial.RUNE_DESTRUCTION.create())),
          RUNE_EARTHQUAKE = new CustomEffectType("rune_earthquake", "지진의 룬", builder().description("땅에 착지 시 주변 몬스터를 조집니다").defaultDuration(20 * 60)
                  .icon(CustomMaterial.RUNE_EARTHQUAKE.create()));

  protected static void registerEffect()
  {
    register(
            RUNE_COOLDOWN, RUNE_USING, RUNE_OCCUPIED, RUNE_EXPERIENCE, RUNE_DESTRUCTION, RUNE_EARTHQUAKE
    );
  }
}

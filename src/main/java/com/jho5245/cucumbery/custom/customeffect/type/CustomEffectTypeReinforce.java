package com.jho5245.cucumbery.custom.customeffect.type;

import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;

public class CustomEffectTypeReinforce extends CustomEffectType
{
  public static final CustomEffectType
          STARFORCE_30_SALE = new CustomEffectType("starforce_30_sale", "스타포스 강화 비용 30% 할인", builder().description(ComponentUtil.translate("스타포스 강화 비용이 30% 감소합니다"))),
          STARFORCE_5_10_15 = new CustomEffectType("starforce_5_10_15", "스타포스 5,10,15성 100% 성공", builder().description(ComponentUtil.translate("5성, 10성, 15성에서 스타포스 강화 시 성공 확률이 100%가 됩니다"))),
          REINFORCE_OP_MODE = new CustomEffectType("reinforce_op_mode", "강화 무조건 성공", builder().description(ComponentUtil.translate("강화 시 무조건 성공합니다")).enumHidden()),
          ANTI_DESTRUCTION_DISABLED_WARNING = new CustomEffectType("anti_destruction_disabled_warning", "경고! 파괴 방지 해제됨!", builder().defaultDuration(60).enumHidden().negative()),
          STAR_CATCH_SUCCESS = new CustomEffectType("star_catch_success", "스타 캐치 성공!", builder().enumHidden().removeOnQuit().timeHidden().defaultDuration(60)),
          STAR_CATCH_PROCESS = new CustomEffectType("star_catch_process", "스타 캐치", builder().hidden().removeOnQuit().defaultDuration(120)),
          STAR_CATCH_PREPARE = new CustomEffectType("star_catch_prepare", "스타 캐치 준비", builder().hidden().removeOnQuit().defaultDuration(40)),
          STAR_CATCH_FINISHED = new CustomEffectType("star_catch_finished", "스타 캐치 끝", builder().hidden().removeOnQuit().defaultDuration(20));

  protected static void registerEffect()
  {
    register(
            STARFORCE_30_SALE, STARFORCE_5_10_15, REINFORCE_OP_MODE, ANTI_DESTRUCTION_DISABLED_WARNING, STAR_CATCH_PREPARE, STAR_CATCH_PROCESS, STAR_CATCH_SUCCESS, STAR_CATCH_FINISHED
    );
  }
}

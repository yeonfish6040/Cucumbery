package com.jho5245.cucumbery.custom.customeffect.type;

import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;

public class CustomEffectTypeCustomMining extends CustomEffectType
{
  public static final CustomEffectType
          CUSTOM_MINING_SPEED_MODE = new CustomEffectType("custom_mining_speed_mode", "채광 모드", builder().hidden().defaultDuration(-1).keepOnDeath()),
          CUSTOM_MINING_SPEED_MODE_2 = new CustomEffectType("custom_mining_speed_mode_2", "채광 모드 2", builder().hidden().defaultDuration(2).keepOnDeath()),
          CUSTOM_MINING_SPEED_MODE_2_NO_RESTORE = new CustomEffectType("custom_mining_speed_mode_2_no_restore", "채광 모드 2 (복구 없음)", builder().hidden().defaultDuration(2).keepOnDeath()),
          CUSTOM_MINING_SPEED_MODE_PREDEFINED_CUSTOM_ORE_DWARVEN_GOLDS = new CustomEffectType("custom_mining_speed_mode_predefined_custom_ore_dwarven_golds", "채광 모드 커스텀 광물 - 드워프 금괴", builder().hidden().defaultDuration(2).keepOnDeath()),
          CUSTOM_MINING_SPEED_MODE_PREDEFINED_CUSTOM_ORE_MITHRILS = new CustomEffectType("custom_mining_speed_mode_predefined_custom_ore_mithrils", "채광 모드 커스텀 광물 - 미스릴", builder().hidden().defaultDuration(2).keepOnDeath()),
          CUSTOM_MINING_SPEED_MODE_PREDEFINED_CUSTOM_ORE_TITANIUMS = new CustomEffectType("custom_mining_speed_mode_predefined_custom_ore_titaniums", "채광 모드 커스텀 광물 - 티타늄", builder().hidden().defaultDuration(2).keepOnDeath()),
          CUSTOM_MINING_SPEED_MODE_PREDEFINED_CUSTOM_ORE_GEMSTONES = new CustomEffectType("custom_mining_speed_mode_predefined_custom_ore_gemstones", "채광 모드 커스텀 광물 - 젬스톤", builder().hidden().defaultDuration(2).keepOnDeath()),
          CUSTOM_MINING_SPEED_MODE_PROGRESS = new CustomEffectType("custom_mining_speed_mode_progress", "채광 모드 진행", builder().hidden().defaultDuration(-1).removeOnQuit()),
          CUSTOM_MINING_SPEED_MODE_ADJUST_VANILLA_SPEED = new CustomEffectType("custom_mining_speed_mode_adjust_vanilla_speed", "채광 모드 바닐라 속도 보정", builder().hidden().defaultDuration(2).removeOnQuit()),
          HASTE = new CustomEffectType("haste", "개급함", builder().maxAmplifier(999).description("채광 속도가 증가합니다")),
          MINING_FATIGUE = new CustomEffectType("mining_fatigue", "느린 채광", builder().negative().keepOnDeath().maxAmplifier(99).description("채광 속도가 감소합니다")),
          MINING_FORTUNE = new CustomEffectType("mining_fortune", "채광 행운", builder().maxAmplifier(999).description("채광 행운이 증가합니다")),
          MOLE_CLAW = new CustomEffectType("mole_claw", "몰 클로", builder().description("채광 속도가 1 증가합니다\n채광 속도 % 증가의 영향을 받지 않습니다")),
          MINDAS_TOUCH = new CustomEffectType("mindas_touch", "마인더스의 손길", builder().maxAmplifier(99).description("곡괭이의 채광 등급이 증가합니다")),
          TITANIUM_FINDER = new CustomEffectType("titanium_finder", "티타늄 광부", builder().maxAmplifier(499).description(
                  ComponentUtil.translate("%s 채굴 시 %s 등장 확률이 증가합니다 (기본 %s)", CustomMaterial.TITANIUM_ORE, CustomMaterial.TITANIUM_ORE, Constant.THE_COLOR_HEX + "5%"))),
          MINING_BOOSTER = new CustomEffectType("mining_booster", "채광 부스터", builder().removeOnQuit().nonRemovable().nonBuffFreezable().description("잠시 동안 채광 속도가 3배가 됩니다").defaultDuration(200)),
          MINING_BOOSTER_COOLDOWN = new CustomEffectType("mining_booster_cooldown", "채광 부스터 쿨타임", builder().negative().keepOnDeath().enumHidden().description("채광 부스터를 사용할 수 없는 상태입니다").defaultDuration(20 * 120));

  protected static void registerEffect()
  {
    register(
            CUSTOM_MINING_SPEED_MODE, CUSTOM_MINING_SPEED_MODE_2, CUSTOM_MINING_SPEED_MODE_2_NO_RESTORE,
            CUSTOM_MINING_SPEED_MODE_PROGRESS, CUSTOM_MINING_SPEED_MODE_ADJUST_VANILLA_SPEED,

            CUSTOM_MINING_SPEED_MODE_PREDEFINED_CUSTOM_ORE_DWARVEN_GOLDS, CUSTOM_MINING_SPEED_MODE_PREDEFINED_CUSTOM_ORE_MITHRILS,
            CUSTOM_MINING_SPEED_MODE_PREDEFINED_CUSTOM_ORE_TITANIUMS, CUSTOM_MINING_SPEED_MODE_PREDEFINED_CUSTOM_ORE_GEMSTONES,

            HASTE, MINING_FATIGUE,

            MINING_FORTUNE, MOLE_CLAW, MINDAS_TOUCH, TITANIUM_FINDER,

            MINING_BOOSTER, MINING_BOOSTER_COOLDOWN
    );
  }
}

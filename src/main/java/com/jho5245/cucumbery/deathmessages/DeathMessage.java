package com.jho5245.cucumbery.deathmessages;

import com.jho5245.cucumbery.util.storage.data.Variable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum DeathMessage
{
  /**
   * %1$s이(가) 떨어지는 모루에 짓눌렸습니다
   */
  ANVIL,

  /**
   * %1$s이(가) %2$s과(와) 싸우다가 떨어지는 모루에 짓눌렸습니다
   */
  ANVIL_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s과(와) 싸우다가 떨어지는 모루에 짓눌렸습니다
   */
  ANVIL_COMBAT_ITEM,

  /**
   * %1$s이(가) 자살을 시도하다가 떨어지는 모루에 짓눌렸습니다
   */
  ANVIL_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %3$s을(를) 사용하며 자살을 시도하다가 떨어지는 모루에 짓눌렸습니다
   */
  ANVIL_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 화살에게 저격당했습니다
   */
  ARROW,

  /**
   * %1$s이(가) %2$s에게 저격당했습니다
   */
  ARROW_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게 저격당했습니다
   */
  ARROW_COMBAT_ITEM,

  /**
   * %1$s이(가) 스스로를 저격했습니다
   */
  ARROW_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %3$s을(를) 사용하여 스스로를 저격했습니다
   */
  ARROW_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 어딘가에서 날아온 %2$s에게 저격당했습니다
   */
  ARROW_COMBAT_UNKNOWN,

  /**
   * %1$s이(가) %2$s에게 목숨을 빼앗겼습니다
   */
  BAD_RESPAWN,

  /**
   * %1$s이(가) %2$s와(과) 싸우다 %3$s에게 목숨을 빼앗겼습니다
   */
  BAD_RESPAWN_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s와(과) 싸우다 %4$s에게 목숨을 빼앗겼습니다
   */
  BAD_RESPAWN_COMBAT_ITEM,

  /**
   * %1$s이(가) %3$s으(로) 자살했습니다
   */
  BAD_RESPAWN_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %3$s을(를) 사용하고 %4$s으(로) 자살했습니다
   */
  BAD_RESPAWN_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 찔려 사망했습니다
   */
  CACTUS,

  /**
   * %1$s이(가) %2$s에게서 도망치려다 선인장에 찔렸습니다
   */
  CACTUS_COMBAT,

  /**
   * %1$s이(가) %3s을(를) 사용한 %2$s에게서 도망치려다 선인장에 찔렸습니다
   */
  CACTUS_COMBAT_ITEM,

  /**
   * %1$s이(가) 스스로를 %3$s에 찔려 죽었습니다
   */
  CACTUS_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %3$s을(를) 사용하고 스스로를 %4$s에 찔려 죽었습니다
   */
  CACTUS_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) %2$s에 닿아 죽었습니다
   */
  CONTACT,

  /**
   * %1$s이(가) %2$s에게서 도망치려다 %3$s에 닿아 죽었습니다
   */
  CONTACT_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게서 도망치려다 %4$s에 닿아 죽었습니다
   */
  CONTACT_COMBAT_ITEM,

  /**
   * %1$s이(가) 스스로를 %3$s에 닿아 죽었습니다
   */
  CONTACT_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %3$s을(를) 사용하고 스스로를 %4$s에 닿아 죽었습니다
   */
  CONTACT_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 으깨져버렸습니다
   */
  CRAMMING,

  /**
   * %1$s이(가) 으깨져버렸습니다
   */
  CRAMMING_NONE,

  /**
   * %1$s이(가) %2$s에게 짓눌렸습니다
   */
  CRAMMING_COMBAT,

  /**
   * %1$s이(가) %3s을(를) 사용한 %2$s과(와) 싸우다 짓눌렸습니다
   */
  CRAMMING_COMBAT_ITEM,

  /**
   * %1$s이(가) 자살을 시도하다가 %3$s에게 짓눌렸습니다
   */
  CRAMMING_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %3s을(를) 사용하여 자살을 시도하다가 %4$s에게 짓눌렸습니다
   */
  CRAMMING_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) %2$s에게 어깨빵을 당했습니다
   */
  CRAMMING_SOLO,

  /**
   * %1$s이(가) %2$s와(과) 싸우다 %3$s에게 어깨빵을 당했습니다
   */
  CRAMMING_SOLO_COMBAT,

  /**
   * %1$s이(가) %3s을(를) 사용한 %2$s와(과) 싸우다 %4$s에게 어깨빵을 당했습니다
   */
  CRAMMING_SOLO_COMBAT_ITEM,

  /**
   * %1$s이(가) 자살을 시도하다가 %3$s에게 어깨빵을 당했습니다
   */
  CRAMMING_SOLO_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %3$s을(를) 사용하여 자살을 시도하다가 %4$s에게 어깨빵을 당했습니다
   */
  CRAMMING_SOLO_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 드래곤의 숨결에 구워졌습니다
   */
  DRAGON_BREATH,

  /**
   * %1$s이(가) %2$s 때문에 드래곤의 숨결에 구워졌습니다
   */
  DRAGON_BREATH_COMBAT,

  /**
   * %1$s이(가) %3s을(를) 사용한 %2$s 때문에 드래곤의 숨결에 구워졌습니다
   */
  DRAGON_BREATH_COMBAT_ITEM,

  /**
   - "%1$s이(가) 스스로를 드래곤의 숨결에 구웠습니다"
   */
  DRAGON_BREATH_COMBAT_SUICIDE,

  /**
   - "%1$s이(가) %3$s을(를) 사용한 스스로를 드래곤의 숨결에 구웠습니다"
   */
  DRAGON_BREATH_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 익사했습니다
   */
  DROWN,

  /**
   * %1$s이(가) %2$s에게서 도망치려다 익사했습니다
   */
  DROWN_COMBAT,

  /**
   * %1$s이(가) %3s을(를) 사용한 %2$s에게서 도망치려다 익사했습니다
   */
  DROWN_COMBAT_ITEM,

  /**
   - "%1$s이(가) 물속으로 투신자살했습니다"
   */
  DROWN_COMBAT_SUICIDE,

  /**
   - "%1$s이(가) %3$s을(를) 사용하여 익사했습니다"
   */
  DROWN_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 익사했습니다
   */
  DROWN_TOGETHER,

  /**
   * %1$s이(가) %2$s에게서 도망치려다 익사했습니다
   */
  DROWN_TOGETHER_COMBAT,

  /**
   * %1$s이(가) %3s을(를) 사용한 %2$s에게서 도망치려다 익사했습니다
   */
  DROWN_TOGETHER_COMBAT_ITEM,

  /**
   - "%1$s이(가) 물속으로 투신자살했습니다"
   */
  DROWN_TOGETHER_COMBAT_SUICIDE,

  /**
   - "%1$s이(가) %3$s을(를) 사용하여 익사했습니다"
   */
  DROWN_TOGETHER_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 물 밖에서 놀다가 익사했습니다
   */
  DROWN_WATER,

  /**
   * %1$s이(가) %2$s에게서 물 밖으로 도망치려다 익사했습니다
   */
  DROWN_WATER_COMBAT,

  /**
   * %1$s이(가) %3s을(를) 사용한 %2$s에게서 물 밖으로 도망치려다 익사했습니다
   */
  DROWN_WATER_COMBAT_ITEM,

  /**
   - "%1$s이(가) 물 밖에서 자살했습니다"
   */
  DROWN_WATER_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %3$s을(를) 사용하여 물 밖에서 자살했습니다
   */
  DROWN_WATER_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 물 밖에서 놀다가 익사했습니다
   */
  DROWN_WATER_TOGETHER,

  /**
   * %1$s이(가) %2$s에게서 물 밖으로 도망치려다 익사했습니다
   */
  DROWN_WATER_TOGETHER_COMBAT,

  /**
   * %1$s이(가) %3s을(를) 사용한 %2$s에게서 물 밖으로 도망치려다 익사했습니다
   */
  DROWN_WATER_TOGHETER_COMBAT_ITEM,

  /**
   - "%1$s이(가) 물 밖에서 자살했습니다"
   */
  DROWN_WATER_TOGETHER_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %3$s을(를) 사용하여 물 밖에서 자살했습니다
   */
  DROWN_WATER_TOGETHER_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 목이 말라 죽었습니다
   */
  DRY_OUT,

  /**
   * %1$s이(가) %2$s에게서 도망치려다 탈수로 죽었습니다
   */
  DRY_OUT_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게서 도망치려다 탈수로 죽었습니다
   */
  DRY_OUT_COMBAT_ITEM,

  /**
   - "%1$s이(가) 스스로를 말려비틀어 죽었습니다"
   */
  DRY_OUT_COMBAT_SUICIDE,

  /**
   - "%1$s이(가) %3$s을(를) 사용하여 스스로를 말려비틀어 죽었습니다"
   */
  DRY_OUT_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 운동 에너지를 온몸으로 경험했습니다
   */
  ELYTRA,

  /**
   * %1$s이(가) %2$s에게서 도망치려다 운동 에너지를 온몸으로 느꼈습니다
   */
  ELYTRA_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게서 도망치려다 운동 에너지를 온몸으로 느꼈습니다
   */
  ELYTRA_COMBAT_ITEM,

  /**
   - "%1$s이(가) %3$s에 처박아 죽었습니다"
   */
  ELYTRA_COMBAT_SUICIDE,

  /**
   - "%1$s이(가) %3$s을(를) 사용하여 %4$s에 처박아 죽었습니다"
   */
  ELYTRA_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) %2$s을(를) 잘못 사용했습니다
   */
  ENDER_PEARL,

  /**
   * %1$s이(가) %2$s에게서 도망치려다 %3$s을(를) 잘못 사용했습니다
   */
  ENDER_PEARL_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게서 도망치려다 %4$s을(를) 잘못 사용했습니다
   */
  ENDER_PEARL_COMBAT_ITEM,

  /**
   - "%1$s이(가) %3$s을(를) 사용하여 자살했습니다"
   */
  ENDER_PEARL_COMBAT_SUICIDE,

  /**
   - "%1$s이(가) %4$s와(과) %3$s을(를) 사용하여 자살했습니다"
   */
  ENDER_PEARL_COMBAT_SUICIDE_ITEM,

  /**
   * "%1$s이(가) %2$s에게 살해당했습니다"
   */
  EVOKER_FANGS,

  /**
   * "%1$s이(가) %2$s의 %3$s에게 살해당했습니다"
   */
  EVOKER_FANGS_COMBAT,

  /**
   * "%1$s이(가) %3$s을(를) 사용한 %2$s의 %4$s에게 살해당했습니다"
   */
  EVOKER_FANGS_COMBAT_ITEM,

  /**
   * "%1$s이(가) 스스로의 %3$s에게 살해당했습니다"
   */
  EVOKER_FANGS_COMBAT_SUICIDE,

  /**
   * "%1$s이(가) %3$s을(를) 사용한 스스로의 %4$s에게 살해당했습니다"
   */
  EVOKER_FANGS_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 폭파당했습니다
   */
  EXPLOSION,

  /**
   * %1$s이(가) %2$s에게 폭파당했습니다
   */
  EXPLOSION_COMBAT,

  /**
   * %1$s이(가) %2$s에게 %3$s(으)로 폭파당했습니다
   */
  EXPLOSION_COMBAT_ITEM,

  /**
   - "%1%s이(가) 자폭했습니다"
   */
  EXPLOSION_COMBAT_SUICIDE,

  /**
   - "%1%s이(가) %3$s을(를) 사용하여 자폭했습니다"
   */
  EXPLOSION_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 어딘가에서 날아온 %2$s때문에 폭파당했습니다
   */
  EXPLOSION_COMBAT_UNKNOWN,

  /**
   * %1$s이(가) 땅바닥으로 곤두박질쳤습니다
   */
  FALL,

  /**
   * %1$s이(가) %2$s에게서 도망치려다 땅바닥으로 곤두박질쳤습니다
   */
  FALL_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게서 도망치려다 땅바닥으로 곤두박질쳤습니다
   */
  FALL_COMBAT_ITEM,

  /**
   - "%1$s이(가) 높지는 않지만 스스로 몸을 던져 죽었습니다"
   */
  FALL_COMBAT_SUICIDE,

  /**
   - "%1$s이(가) 높지는 않지만 %3$s을(를) 사용하여 스스로 몸을 던져 죽었습니다"
   */
  FALL_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 높은 곳에서 떨어졌습니다
   */
  FALL_HIGH,

  /**
   * %1$s은(는) %2$s 때문에 추락을 피하지 못했습니다
   */
  FALL_HIGH_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s 때문에 높은 곳에서 떨어졌습니다
   */
  FALL_HIGH_COMBAT_ITEM,

  /**
   - "%1$s이(가) 스스로 몸을 던져 죽었습니다"
   */
  FALL_HIGH_COMBAT_SUICIDE,

  /**
   - "%1$s이(가) %3$2을(를) 사용하여 스스로 몸을 던져 죽었습니다"
   */
  FALL_HIGH_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) %2$s에서 땅바닥으로 곤두박질쳤습니다
   */
  FALL_BLOCK,

  /**
   * %1$s이(가) %2$s에게서 도망치려다 %3$s에서 땅바닥으로 곤두박질쳤습니다
   */
  FALL_BLOCK_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게서 도망치려다 %4$s에서 땅바닥으로 곤두박질쳤습니다
   */
  FALL_BLOCK_COMBAT_ITEM,

  /**
   - "%1$s이(가) 높지는 않지만 %3$s에서 스스로 몸을 던져 죽었습니다"
   */
  FALL_BLOCK_COMBAT_SUICIDE,

  /**
   - "%1$s이(가) 높지는 않지만 %3$s을(를) 사용하여 %4$s에서 스스로 몸을 던져 죽었습니다"
   */
  FALL_BLOCK_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 높은 %2$s에서 떨어졌습니다
   */
  FALL_BLOCK_HIGH,

  /**
   * %1$s이(가) %2$s 때문에 높은 %3$s에서 떨어졌습니다
   */
  FALL_BLOCK_HIGH_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s 때문에 높은 %4$s에서 떨어졌습니다
   */
  FALL_BLOCK_HIGH_COMBAT_ITEM,

  /**
   - "%1$s이(가) %3$s에서 스스로 몸을 던져 죽었습니다"
   */
  FALL_BLOCK_HIGH_COMBAT_SUICIDE,

  /**
   - "%1$s이(가) %3$s을(를) 사용하여 %4$s에서 스스로 몸을 던져 죽었습니다"
   */
  FALL_BLOCK_HIGH_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 땅바닥으로 곤두박질쳤습니다
   */
  FALL_ELYTRA,

  /**
   * %1$s이(가) %2$s에게서 도망치려다 땅바닥으로 곤두박질쳤습니다
   */
  FALL_ELYTRA_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게서 도망치려다 땅바닥으로 곤두박질쳤습니다
   */
  FALL_ELYTRA_COMBAT_ITEM,

  /**
   - "%1$s이(가) 높지는 않지만 스스로 몸을 던져 죽었습니다"
   */
  FALL_ELYTRA_COMBAT_SUICIDE,

  /**
   - "%1$s이(가) 높지는 않지만 %3$s을(를) 사용하여 스스로 몸을 던져 죽었습니다"
   */
  FALL_ELYTRA_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 떨어지는 블록에 짓눌렸습니다
   */
  FALLING_BLOCK,

  /**
   * %1$s이(가) %2$s과(와) 싸우다가 떨어지는 블록에 짓눌렸습니다
   */
  FALLING_BLOCK_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s과(와) 싸우다가 떨어지는 %4$s에 짓눌렸습니다
   */
  FALLING_BLOCK_COMBAT_ITEM,

  /**
   * %1$s이(가) %2$s과(와) 싸우다가 떨어지는 블록에 짓눌렸습니다
   */
  FALLING_BLOCK_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s과(와) 싸우다가 떨어지는 %4$s에 짓눌렸습니다
   */
  FALLING_BLOCK_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 떨어지는 종유석에 찔렸습니다
   */
  FALLING_STALACTITE,

  /**
   * %1$s이(가) %2$s과(와) 싸우다가 떨어지는 종유석에 찔렸습니다
   */
  FALLING_STALACTITE_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s과(와) 싸우다가 떨어지는 %4$s에 찔렸습니다 아야야야.
   */
  FALLING_STALACTITE_COMBAT_ITEM,

  /**
   * %1$s이(가) 불에 타 사망했습니다
   */
  FIRE,

  /**
   * %1$s이(가) %2$s과(와) 싸우다가 바삭하게 구워졌습니다
   */
  FIRE_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s과(와) 싸우다가 바삭하게 구워졌습니다
   */
  FIRE_COMBAT_ITEM,

  /**
   * %1$s이(가) 스스로와 싸우다가 바삭하게 구워졌습니다
   */
  FIRE_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %3$s을(를) 사용한 스스로와 싸우다가 바삭하게 구워졌습니다
   */
  FIRE_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 화염구에 맞았습니다
   */
  FIREBALL,

  /**
   * %1$s이(가) %2$s이(가) 던진 화염구에 맞았습니다
   */
  FIREBALL_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s이(가) 던진 화염구에 맞았습니다
   */
  FIREBALL_COMBAT_ITEM,

  /**
   * %1$s이(가) 어딘가에서 날아온 %2$s에 맞았습니다
   */
  FIREBALL_COMBAT_UNKNOWN,

  /**
   * %1$s이(가) 굉음과 함께 폭사했습니다
   */
  FIREWORKS,

  /**
   * %1$s이(가) %2$s이(가) 쏜 폭죽 때문에 굉음과 함께 폭사했습니다
   */
  FIREWORKS_COMBAT,

  /**
   * %1$s이(가) %2$s이(가) 쏜 %3$s 때문에 굉음과 함께 폭사했습니다
   */
  FIREWORKS_COMBAT_ITEM,

  /**
   - "%1$s이(가) 자신이 쏜 폭죽 때문에 굉음과 함께 폭사했습니다"
   */
  FIREWORKS_COMBAT_SUICIDE,

  /**
   - "%1$s이(가) 자신이 쏜 %3$s 때문에 굉음과 함께 폭사했습니다"
   */
  FIREWORKS_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 어딘가에서 날아온 %2$s 때문에 굉음과 함께 폭사했습니다
   */
  FIREWORKS_COMBAT_UNKNOWN,

  /**
   * %1$s이(가) 어딘가에서 날아온 %2$s 때문에 굉음과 함께 폭사했습니다
   */
  FIREWORKS_UNKNOWN,

  /**
   * %1$s이(가) 굉음과 함께 폭사했습니다
   */
  FIREWORKS_CROSSBOW,

  /**
   * %1$s이(가) %2$s과(와) 싸우다가 굉음과 함께 폭사했습니다
   */
  FIREWORKS_CROSSBOW_COMBAT,

  /**
   * %1$s이(가) %2$s이(가) %3$s(으)로 쏜 폭죽 때문에 굉음과 함께 폭사했습니다
   */
  FIREWORKS_CROSSBOW_COMBAT_ITEM,

  /**
   * %1$s이(가) 자신이 쏜 폭죽 때문에 굉음과 함께 폭사했습니다
   */
  FIREWORKS_CROSSBOW_COMBAT_SUICIDE,

  /**
   * %1$s이(가) 자신이 %3$s(으)로 쏜 폭죽 때문에 굉음과 함께 폭사했습니다
   */
  FIREWORKS_CROSSBOW_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 화염에 휩싸였습니다
   */
  FIRE_BLOCK,

  /**
   * %1$s이(가) %2$s과(와) 싸우다가 불에 빠졌습니다
   */
  FIRE_BLOCK_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s과(와) 싸우다가 불에 빠졌습니다
   */
  FIRE_BLOCK_COMBAT_ITEM,

  /**
   * %1$s이(가) %2$s과(와) 싸우다가 불에 빠졌습니다
   */
  FIRE_BLOCK_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s과(와) 싸우다가 불에 빠졌습니다
   */
  FIRE_BLOCK_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s(이)가 얼어 죽었습니다
   */
  FREEZE,

  /**
   * %1$s이(가) %2$s 때문에 얼어 죽었습니다
   */
  FREEZE_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s 때문에 얼어 죽었습니다
   */
  FREEZE_COMBAT_ITEM,

  /**
   * %1$s이(가) %2$s 때문에 얼어 죽었습니다
   */
  FREEZE_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s 때문에 얼어 죽었습니다
   */
  FREEZE_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 사망했습니다
   */
  GENERIC,

  /**
   * %1$s이(가) %2$s 때문에 죽었습니다
   */
  GENERIC_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s 때문에 죽었습니다"
   */
  GENERIC_COMBAT_ITEM,

  /**
   * %1$s이(가) %2$s 때문에 죽었습니다
   */
  GENERIC_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s 때문에 죽었습니다"
   */
  GENERIC_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) /kill %2$s 당했습니다
   */
  KILL,

  /**
   * %1$s이(가) %2$s와(과) 싸우다가 /kill %3$s 당했습니다
   */
  KILL_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s와(과) 싸우다가 /kill %4$s 당했습니다
   */
  KILL_COMBAT_ITEM,

  /**
   * %1$s이(가) %2$s와(과) 싸우다가 /kill %3$s 당했습니다
   */
  KILL_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s와(과) 싸우다가 /kill %4$s 당했습니다
   */
  KILL_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 용암에 빠졌습니다
   */
  LAVA,

  /**
   * %1$s이(가) %2$s에게서 도망치려다 용암에 빠졌습니다
   */
  LAVA_COMBAT,

  /**
   * %1$s이(가) %3$s(을)를 사용한 %2$s에게서 도망치려다 용암에 빠졌습니다
   */
  LAVA_COMBAT_ITEM,

  /**
   * %1$s이(가) %2$s에게서 도망치려다 용암에 빠졌습니다
   */
  LAVA_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %3$s(을)를 사용한 %2$s에게서 도망치려다 용암에 빠졌습니다
   */
  LAVA_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 용암에 빠졌습니다
   */
  LAVA_CAULDRON,

  /**
   * %1$s이(가) %2$s에게서 도망치려다 용암에 빠졌습니다
   */
  LAVA_CAULDRON_COMBAT,

  /**
   * %1$s이(가) %3$s(을)를 사용한 %2$s에게서 도망치려다 용암에 빠졌습니다
   */
  LAVA_CAULDRON_COMBAT_ITEM,

  /**
   * %1$s이(가) %2$s에게서 도망치려다 용암에 빠졌습니다
   */
  LAVA_CAULDRON_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %3$s(을)를 사용한 %2$s에게서 도망치려다 용암에 빠졌습니다
   */
  LAVA_CAULDRON_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 벼락을 맞았습니다
   */
  LIGHTNING_BOLT,

  /**
   * %1$s이(가) %2$s과(와) 싸우다가 벼락을 맞았습니다
   */
  LIGHTNING_BOLT_COMBAT,

  /**
   * %1$s이(가) %3$s(을)를 사용한 %2$s과(와) 싸우다가 벼락을 맞았습니다
   */
  LIGHTNING_BOLT_COMBAT_ITEM,

  /**
   * %1$s이(가) %2$s과(와) 싸우다가 벼락을 맞았습니다
   */
  LIGHTNING_BOLT_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %3$s(을)를 사용한 %2$s과(와) 싸우다가 벼락을 맞았습니다
   */
  LIGHTNING_BOLT_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 마법으로 살해당했습니다
   */
  MAGIC,

  /**
   * %1$s이(가) %2$s에게서 도망치려다 마법으로 살해당했습니다
   */
  MAGIC_COMBAT,

  /**
   * %1$s이(가) %2$s에게서 도망치려다 마법으로 살해당했습니다
   */
  MAGIC_COMBAT_ITEM,

  /**
   * %1$s이(가) %2$s에게서 도망치려다 마법으로 살해당했습니다
   */
  MAGIC_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %2$s에게서 도망치려다 마법으로 살해당했습니다
   */
  MAGIC_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 어딘가에서 날아온 %2$s 때문에 마법으로 살해당했습니다
   */
  MAGIC_COMBAT_UNKNOWN,

  /**
   * %1$s이(가) 바닥이 용암인 것을 알아챘습니다
   */
  MAGMA_BLOCK,

  /**
   * %1$s이(가) %2$s 때문에 위험 지대에 빠졌습니다
   */
  MAGMA_BLOCK_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s 때문에 위험 지대에 빠졌습니다
   */
  MAGMA_BLOCK_COMBAT_ITEM,

  /**
   * %1$s이(가) %2$s 때문에 위험 지대에 빠졌습니다
   */
  MAGMA_BLOCK_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s 때문에 위험 지대에 빠졌습니다
   */
  MAGMA_BLOCK_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 사망했습니다
   */
  MELEE,

  /**
   * %1$s이(가) %2$s에게 살해당했습니다
   */
  MELEE_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게 살해당했습니다
   */
  MELEE_COMBAT_ITEM,

  /**
   * %1$s이(가) 자살했습니다
   */
  MELEE_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %3$s을(를) 사용하여 자살했습니다
   */
  MELEE_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 썰렸습니다
   */
  MELEE_SWEEP,

  /**
   * %1$s이(가) %2$s에게 썰렸습니다
   */
  MELEE_SWEEP_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게 썰렸습니다
   */
  MELEE_SWEEP_COMBAT_ITEM,

  /**
   * %1$s이(가) %2$s에게 썰렸습니다
   */
  MELEE_SWEEP_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게 썰렸습니다
   */
  MELEE_SWEEP_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 썰렸습니다
   */
  MELEE_SWORD,

  /**
   * %1$s이(가) %2$s에게 썰렸습니다
   */
  MELEE_SWORD_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게 썰렸습니다
   */
  MELEE_SWORD_COMBAT_ITEM,

  /**
   * %1$s이(가) 자살했습니다
   */
  MELEE_SWORD_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %3$s을(를) 사용하여 자살했습니다
   */
  MELEE_SWORD_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 녹아내렸습니다
   */
  MELTING,

  /**
   * %1$s이(가) %2$s와(과) 싸우다 녹아내렸습니다
   */
  MELTING_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s와(과) 싸우다 녹아내렸습니다
   */
  MELTING_COMBAT_ITEM,

  /**
   * %1$s이(가) %2$s와(과) 싸우다 녹아내렸습니다
   */
  MELTING_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s와(과) 싸우다 녹아내렸습니다
   */
  MELTING_COMBAT_SUICIDE_ITEM,

  /**
   * %2$s이(가) %1$s에게 %3$s을(를) 먹여 죽였습니다
   */
  PARROT_COOKIE_COMBAT_ITEM,

  /**
   *%1$s이(가) 독살당했습니다
   */
  POISON,

  /**
   *%1$s이(가) %2$s에게 독살당했습니다
   */
  POISON_COMBAT,

  /**
   *%1$s이(가) %3$s을(를) 사용한 %2$s에게 독살당했습니다
   */
  POISON_COMBAT_ITEM,

  /**
   *%1$s이(가) 스스로를 독살했습니다
   */
  POISON_COMBAT_SUICIDE,

  /**
   *%1$s이(가) %3$s을(를) 사용하여 스스로를 독살했습니다
   */
  POISON_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 발사체에게 저격당했습니다
   */
  PROJECTILE,

  /**
   * %1$s이(가) %2$s에게 저격당했습니다
   */
  PROJECTILE_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게 저격당했습니다
   */
  PROJECTILE_COMBAT_ITEM,

  /**
   * %1$s이(가) %2$s에게 저격당했습니다
   */
  PROJECTILE_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게 저격당했습니다
   */
  PROJECTILE_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 어딘가에서 날아온 %2$s에게 저격당했습니다
   */
  PROJECTILE_COMBAT_UNKNOWN,

  /**
   * %1$s이(가) 셜커 탄환에게 살해당했습니다
   */
  SHULKER_BULLET,

  /**
   * %1$s이(가) %2%s에게 살해당했습니다
   */
  SHULKER_BULLET_COMBAT, // minecraft bug probably?

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2%s에게 살해당했습니다
   */
  SHULKER_BULLET_COMBAT_ITEM,

  /**
   * %1$s이(가) %2%s에게 살해당했습니다
   */
  SHULKER_BULLET_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2%s에게 살해당했습니다
   */
  SHULKER_BULLET_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 어딘가에서 날아온 %2%s에게 살해당했습니다
   */
  SHULKER_BULLET_COMBAT_UNKNOWN,

  /**
   * %1$s이(가) 어딘가에서 날아온 %2%s에게 살해당했습니다
   */
  SHULKER_BULLET_UNKNOWN,

  /**
   * %1$s은(는) 강한 음파의 비명에 말살됐습니다
   */
  SONIC_BOOM,

  /**
   * %1$s이(가) %2$s에게서 도망치려다 강한 음파의 비명에 말살됐습니다
   */
  SONIC_BOOM_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 든 %2$s에게서 도망치려다 강한 음파의 비명에 말살됐습니다
   */
  SONIC_BOOM_COMBAT_ITEM,

  /**
   * %1$s이(가) 스스로의 강한 음파의 비명에 말살됐습니다
   */
  SONIC_BOOM_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %3$s을(를) 사용하다 스스로의 강한 음파의 비명에 말살됐습니다
   */
  SONIC_BOOM_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 석순에 찔렸습니다
   */
  STALAGMITE,

  /**
   * %1$s이(가) %2$s와(과) 싸우다가 석순에 찔렸습니다
   */
  STALAGMITE_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s와(과) 싸우다가 %4%s에 찔렸습니다
   */
  STALAGMITE_COMBAT_ITEM,

  /**
   * %1$s이(가) %2$s와(과) 싸우다가 석순에 찔렸습니다
   */
  STALAGMITE_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s와(과) 싸우다가 %4%s에 찔렸습니다
   */
  STALAGMITE_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 아사했습니다
   */
  STARVE,

  /**
   * %1$s이(가) %2$s과(와) 싸우다가 아사했습니다
   */
  STARVE_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s과(와) 싸우다가 아사했습니다
   */
  STARVE_COMBAT_ITEM,

  /**
   * %1$s이(가) %2$s과(와) 싸우다가 아사했습니다
   */
  STARVE_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s과(와) 싸우다가 아사했습니다
   */
  STARVE_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 쏘여 사망했습니다
   */
  STING,

  /**
   * %1$s이(가) %2$s에게 쏘여 사망했습니다
   */
  STING_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게 쏘여 사망했습니다
   */
  STING_COMBAT_ITEM,

  /**
   * %1$s이(가) %2$s에게 쏘여 사망했습니다
   */
  STING_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게 쏘여 사망했습니다
   */
  STING_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 벽 속에서 질식했습니다
   */
  SUFFOCATION,

  /**
   * %1$s이(가) %2$s과(와) 싸우다가 벽 속에서 질식했습니다
   */
  SUFFOCATION_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s과(와) 싸우다가 %4$s 속에서 질식했습니다
   */
  SUFFOCATION_COMBAT_ITEM,

  /**
   * %1$s이(가) %2$s과(와) 싸우다가 벽 속에서 질식했습니다
   */
  SUFFOCATION_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s과(와) 싸우다가 %4$s 속에서 질식했습니다
   */
  SUFFOCATION_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 자살했습니다
   */
  SUICIDE,

  /**
   * %1$s이(가) 자살했습니다
   */
  SUICIDE_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용하여 자살했습니다
   */
  SUICIDE_COMBAT_ITEM,

  /**
   * %1$s이(가) 달콤한 열매 덤불에 찔려 죽었습니다
   */
  SWEET_BERRY_BUSH,

  /**
   * %1$s이(가) %2$s에게서 도망치려다 달콤한 열매 덤불에 찔려 죽었습니다
   */
  SWEET_BERRY_BUSH_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게서 도망치려다 %4$s에 찔려 죽었습니다
   */
  SWEET_BERRY_BUSH_COMBAT_ITEM,

  /**
   * %1$s이(가) %2$s에게서 도망치려다 달콤한 열매 덤불에 찔려 죽었습니다
   */
  SWEET_BERRY_BUSH_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게서 도망치려다 %4$s에 찔려 죽었습니다
   */
  SWEET_BERRY_BUSH_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 누군가를 해치려다 살해당했습니다
   */
  THORNS,

  /**
   * %1$s이(가) %2$s을(를) 해치려다 살해당했습니다
   */
  THORNS_COMBAT,

  /**
   * %1$s이(가) %2$s을(를) 해치려다 %3$s(으)로 살해당했습니다
   */
  THORNS_COMBAT_ITEM,

  /**
   * %1$s이(가) %2$s을(를) 해치려다 살해당했습니다
   */
  THORNS_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %2$s을(를) 해치려다 %3$s(으)로 살해당했습니다
   */
  THORNS_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 삼지창에게 찔렸습니다
   */
  TRIDENT,

  /**
   * %1$s이(가) %2$s에게 찔렸습니다
   */
  TRIDENT_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게 찔렸습니다
   */
  TRIDENT_COMBAT_ITEM,

  /**
   * %1$s이(가) %2$s에게 찔렸습니다
   */
  TRIDENT_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게 찔렸습니다
   */
  TRIDENT_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 어딘가에서 날아온 %2$s에게 찔렸습니다
   */
  TRIDENT_COMBAT_UNKNOWN,

  /**
   * %1$s이(가) 알 수 없는 이유로 죽었습니다 죄송합니다! 이 메시지가 뜨면 개발자가 일을 안 한겁니다! %2$s에서 해당 버그를 제보해주세요!
   */
  UNKNOWN,

  /**
   * %1$s이(가) 세계 밖으로 떨어졌습니다
   */
  VOID,

  /**
   * %1$s은(는) %2$s과(와)는 도저히 한 하늘을 같이 이고 살 수 없었습니다
   */
  VOID_COMBAT,

  /**
   * %1$s은(는) %3$s을(를) 사용한 %2$s과(와)는 도저히 한 하늘을 같이 이고 살 수 없었습니다
   */
  VOID_COMBAT_ITEM,

  /**
   * %1$s은(는) %2$s과(와)는 도저히 한 하늘을 같이 이고 살 수 없었습니다
   */
  VOID_COMBAT_SUICIDE,

  /**
   * %1$s은(는) %3$s을(를) 사용한 %2$s과(와)는 도저히 한 하늘을 같이 이고 살 수 없었습니다
   */
  VOID_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 너무 높은 곳에서 세계 밖으로 떨어졌습니다
   */
  VOID_HIGH,

  /**
   * %1$s은(는) %2$s과(와)는 도저히 한 높은 하늘을 같이 이고 살 수 없었습니다
   */
  VOID_HIGH_COMBAT,

  /**
   * %1$s은(는) %3$s을(를) 사용한 %2$s과(와)는 도저히 한 높은 하늘을 같이 이고 살 수 없었습니다
   */
  VOID_HIGH_COMBAT_ITEM,

  /**
   * %1$s은(는) %2$s과(와)는 도저히 한 높은 하늘을 같이 이고 살 수 없었습니다
   */
  VOID_HIGH_COMBAT_SUICIDE,

  /**
   * %1$s은(는) %3$s을(를) 사용한 %2$s과(와)는 도저히 한 높은 하늘을 같이 이고 살 수 없었습니다
   */
  VOID_HIGH_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) %2$s에서 세계 밖으로 떨어졌습니다+
   *
   */
  VOID_BLOCK,

  /**
   * %1$s은(는) %2$s과(와)는 도저히 한 하늘을 같이 이고 살 수 없었습니다
   */
  VOID_BLOCK_COMBAT,

  /**
   * %1$s은(는) %3$s을(를) 사용한 %2$s과(와)는 도저히 한 하늘을 같이 이고 살 수 없었습니다
   */
  VOID_BLOCK_COMBAT_ITEM,

  /**
   * %1$s은(는) %2$s과(와)는 도저히 한 하늘을 같이 이고 살 수 없었습니다
   */
  VOID_BLOCK_COMBAT_SUICIDE,

  /**
   * %1$s은(는) %3$s을(를) 사용한 %2$s과(와)는 도저히 한 하늘을 같이 이고 살 수 없었습니다
   */
  VOID_BLOCK_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 너무 높은 곳에서 세계 밖으로 떨어졌습니다
   */
  VOID_BLOCK_HIGH,

  /**
   * %1$s은(는) %2$s과(와)는 도저히 한 높은 하늘을 같이 이고 살 수 없었습니다
   */
  VOID_BLOCK_HIGH_COMBAT,

  /**
   * %1$s은(는) %3$s을(를) 사용한 %2$s과(와)는 도저히 한 높은 하늘을 같이 이고 살 수 없었습니다
   */
  VOID_BLOCK_HIGH_COMBAT_ITEM,

  /**
   * %1$s은(는) %2$s과(와)는 도저히 한 높은 하늘을 같이 이고 살 수 없었습니다
   */
  VOID_BLOCK_HIGH_COMBAT_SUICIDE,

  /**
   * %1$s은(는) %3$s을(를) 사용한 %2$s과(와)는 도저히 한 높은 하늘을 같이 이고 살 수 없었습니다
   */
  VOID_BLOCK_HIGH_COMBAT_SUICIDE_ITEM,

  /**
   * death.fell.accident.water
   */
  WATER_ACCIDENT,

  /**
   * %1$s이(가) 사그라졌습니다
   */
  WITHER,

  /**
   * %1$s이(가) %2$s과(와) 싸우다가 사그라졌습니다
   */
  WITHER_COMBAT,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s과(와) 싸우다가 사그라졌습니다
   */
  WITHER_COMBAT_ITEM,

  /**
   * %1$s이(가) %2$s과(와) 싸우다가 사그라졌습니다
   */
  WITHER_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s과(와) 싸우다가 사그라졌습니다
   */
  WITHER_COMBAT_SUICIDE_ITEM,

  /**
   * %1$s이(가) 해골에게 저격당했습니다
   */
  WITHER_SKULL,

  /**
   * %1$s이(가) %2$s의 해골에게 저격당했습니다
   */
  WITHER_SKULL_COMBAT,

  /**
   * %1$s이(가) %2$s의 해골에게 저격당했습니다
   */
  WITHER_SKULL_COMBAT_ITEM,

  /**
   * %1$s이(가) %2$s의 해골에게 저격당했습니다
   */
  WITHER_SKULL_COMBAT_UNKNOWN,

  /**
   * %1$s이(가) %2$s의 해골에게 저격당했습니다
   */
  WITHER_SKULL_COMBAT_SUICIDE,

  /**
   * %1$s이(가) %2$s의 해골에게 저격당했습니다
   */
  WITHER_SKULL_COMBAT_ITEM_SUICIDE,

  /**
   * %1$s이(가) %2$s 밖으로 탈출하려다 질식사했습니다
   */
  WORLD_BORDER,

  /**
   *%1$s이(가) %2$s과(와) 싸우고 %3$s 밖으로 탈출하려다 질식사했습니다
   */
  WORLD_BORDER_COMBAT,

  /**
   *%1$s이(가) %3$s을(를) 사용한 %2$s과(와) 싸우고 %4$s 밖으로 탈출하려다 질식사했습니다
   */
  WORLD_BORDER_COMBAT_ITEM,

  /**
   *%1$s이(가) %3$s 밖으로 스스로의 몸을 던져 질식사했습니다
   */
  WORLD_BORDER_COMBAT_SUICIDE,

  /**
   *%1$s이(가) %3$s을(를) 사용하여 %4$s 밖으로 스스로의 몸을 던져 질식사했습니다
   */
  WORLD_BORDER_COMBAT_SUICIDE_ITEM,
  CUSTOM_DARKNESS_TERROR("%1$s이(가) 너무 어두운 곳에서 채광을 하다 파편에 찔려 죽었습니다"),
  CUSTOM_DARKNESS_TERROR_COMBAT("%1$s이(가) %2$s와(과) 싸우면서 너무 어두운 곳에서 채광을 하다 파편에 찔려 죽었습니다"),
  CUSTOM_DARKNESS_TERROR_COMBAT_ITEM("%1$s이(가) %3$s을(를) 사용한 %2$s와(과) 싸우면서 너무 어두운 곳에서 채광을 하다 파편에 찔려 죽었습니다"),
  CUSTOM_DARKNESS_TERROR_COMBAT_SUICIDE("%1$s이(가) 스스로와의 단련을 하며 너무 어두운 곳에서 채광을 하다 파편에 찔려 죽었습니다"),
  CUSTOM_DARKNESS_TERROR_COMBAT_SUICIDE_ITEM("%1$s이(가) %3$s을(를) 사용한 스스로와의 단련을 하며 너무 어두운 곳에서 채광을 하다 파편에 찔려 죽었습니다"),
  CUSTOM_STOMACHACHE("%1$s이(가) 경험을 소화하려다 탈이 나 죽었습니다"),
  CUSTOM_STOMACHACHE_COMBAT("%1$s이(가) %2$s와(과) 싸우면서 경험을 소화하려다 탈이 나 죽었습니다"),
  CUSTOM_STOMACHACHE_COMBAT_ITEM("%1$s이(가) %3$s을(를) 사용한 %2$s와(과) 싸우면서 경험을 소화하려다 탈이 나 죽었습니다"),
  CUSTOM_STOMACHACHE_COMBAT_SUICIDE("%1$s이(가) 나 자신과의 싸움을 하면서 경험을 소화하려다 탈이 나 죽었습니다"),
  CUSTOM_STOMACHACHE_COMBAT_SUICIDE_ITEM("%1$s이(가) %3$s을(를) 사용한 나 자신과의 싸움을 하면서 경험을 소화하려다 탈이 나 죽었습니다"),
  CUSTOM_EFFECT_ENTITY_REMOVER("%1$s이(가) 제거되었습니다"),
  CUSTOM_EFFECT_ENTITY_REMOVER_COMBAT("%1$s이(가) %2$s와(과) 싸우다가 제거되었습니다"),
  CUSTOM_EFFECT_ENTITY_REMOVER_COMBAT_ITEM("%1$s이(가) %3$s을(를) 사용한 %2$s와(과) 싸우다가 제거되었습니다"),
  CUSTOM_EFFECT_ENTITY_REMOVER_COMBAT_SUICIDE("%1$s이(가) 제거되었습니다"),
  CUSTOM_EFFECT_ENTITY_REMOVER_COMBAT_SUICIDE_ITEM("%1$s이(가) %3$s을(를) 사용하다가 제거되었습니다"),
  ;
  
  private final String defaultString;
  
  DeathMessage()
  {
    this(null);
  }

  DeathMessage(String defaultString)
  {
    this.defaultString = defaultString;
  }

  @NotNull
  public List<String> getKeys()
  {
    List<String> list = new ArrayList<>(Variable.deathMessages.getStringList("death-messages.messages." + this.toString().toLowerCase().replace("_", "-")));
    if (!list.isEmpty() || defaultString == null)
    {
      return list;
    }
    return Collections.singletonList(defaultString);
  }
}
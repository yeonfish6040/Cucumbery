package com.jho5245.cucumbery.deathmessages;

import com.jho5245.cucumbery.util.storage.data.Variable;

import java.util.ArrayList;
import java.util.List;

public enum Messages
{
  /**
   * %1$s이(가) 떨어지는 모루에 짓눌렸습니다
   */
  ANVIL("death-messages.messages.anvil"),

  /**
   * %1$s이(가) %2$s과(와) 싸우다가 떨어지는 모루에 짓눌렸습니다
   */
  ANVIL_COMBAT("death-messages.messages.anvil-combat"),

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s과(와) 싸우다가 떨어지는 모루에 짓눌렸습니다.
   */
  ANVIL_COMBAT_ITEM("death-messages.messages.anvil-combat-item"),

  /**
   * %1$s이(가) 화살에게 저격당했습니다.
   */
  ARROW("death-messages.messages.arrow"),

  /**
   * %1$s이(가) %2$s에게 저격당했습니다
   */
  ARROW_COMBAT("death-messages.messages.arrow-combat"),

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게 저격당했습니다
   */
  ARROW_COMBAT_ITEM("death-messages.messages.arrow-combat-item"),

  /**
   * %1$s이(가) 어딘가에서 날아온 %2$s에게 저격당했습니다.
   */
  ARROW_COMBAT_UNKNOWN("death-messages.messages.arrow-combat-unknown"),

  /**
   * %1$s이(가) %2$s에게 목숨을 빼앗겼습니다
   */
  BAD_RESPAWN("death-messages.messages.bad-respawn"),

  /**
   * %1$s이(가) %2$s와(과) 싸우다 %3$s에게 목숨을 빼앗겼습니다.
   */
  BAD_RESPAWN_COMBAT("death-messages.messages.bad-respawn-combat"),

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s와(과) 싸우다 %4$s에게 목숨을 빼앗겼습니다.
   */
  BAD_RESPAWN_COMBAT_ITEM("death-messages.messages.bad-respawn-combat-item"),

  /**
   * %1$s이(가) 찔려 사망했습니다
   */
  CACTUS("death-messages.messages.cactus"),

  /**
   * %1$s이(가) %2$s에게서 도망치려다 선인장에 찔렸습니다
   */
  CACTUS_COMBAT("death-messages.messages.cactus-combat"),

  /**
   * %1$s이(가) %3s을(를) 사용한 %2$s에게서 도망치려다 선인장에 찔렸습니다
   */
  CACTUS_COMBAT_ITEM("death-messages.messages.cactus-combat-item"),

  /**
   * %1$s이(가) %2$s에 닿아 죽었습니다.
   */
  CONTACT("death-messages.messages.contact"),

  /**
   * %1$s이(가) %2$s에게서 도망치려다 %3$s에 닿아 죽었습니다.
   */
  CONTACT_COMBAT("death-messages.messages.contact-combat"),

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게서 도망치려다 %4$s에 닿아 죽었습니다.
   */
  CONTACT_COMBAT_ITEM("death-messages.messages.contact-combat-item"),

  /**
   * %1$s이(가) 으깨져버렸습니다
   */
  CRAMMING("death-messages.messages.cramming"),

  /**
   * %1$s이(가) %2$s에게 짓눌렸습니다
   */
  CRAMMING_COMBAT("death-messages.messages.cramming-combat"),

  /**
   * %1$s이(가) %3s을(를) 사용한 %2$s과(와) 싸우다 짓눌렸습니다.
   */
  CRAMMING_COMBAT_ITEM("death-messages.messages.cramming-combat-item"),

  /**
   * %1$s이(가) %2$s에게 어깨빵을 당했습니다.
   */
  CRAMMING_SOLO("death-messages.messages.cramming-solo"),

  /**
   * %1$s이(가) %2$s와(과) 싸우다 %3$s에게 어깨빵을 당했습니다.
   */
  CRAMMING_SOLO_COMBAT("death-messages.messages.cramming-solo-combat"),

  /**
   * %1$s이(가) %3s을(를) 사용한 %2$s와(과) 싸우다 %4$s에게 어깨빵을 당했습니다.
   */
  CRAMMING_SOLO_COMBAT_ITEM("death-messages.messages.cramming-solo-combat-item"),

  /**
   * %1$s이(가) 드래곤의 숨결에 구워졌습니다
   */
  DRAGON_BREATH("death-messages.messages.dragon-breath"),

  /**
   * %1$s이(가) %2$s 때문에 드래곤의 숨결에 구워졌습니다
   */
  DRAGON_BREATH_COMBAT("death-messages.messages.dragon-breath-combat"),

  /**
   * %1$s이(가) %3s을(를) 사용한 %2$s 때문에 드래곤의 숨결에 구워졌습니다.
   */
  DRAGON_BREATH_COMBAT_ITEM("death-messages.messages.dragon-breath-combat-item"),

  /**
   * %1$s이(가) 익사했습니다
   */
  DROWN("death-messages.messages.drown"),

  /**
   * %1$s이(가) %2$s에게서 도망치려다 익사했습니다
   */
  DROWN_COMBAT("death-messages.messages.drown-combat"),

  /**
   * %1$s이(가) %3s을(를) 사용한 %2$s에게서 도망치려다 익사했습니다.
   */
  DROWN_COMBAT_ITEM("death-messages.messages.drown-combat-item"),

  /**
   * %1$s이(가) 목이 말라 죽었습니다
   */
  DRY_OUT("death-messages.messages.dry-out"),

  /**
   * %1$s이(가) %2$s에게서 도망치려다 탈수로 죽었습니다
   */
  DRY_OUT_COMBAT("death-messages.messages.dry-out-combat"),

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게서 도망치려다 탈수로 죽었습니다
   */
  DRY_OUT_COMBAT_ITEM("death-messages.messages.dry-out-combat-item"),

  /**
   * %1$s이(가) 운동 에너지를 온몸으로 경험했습니다
   */
  ELYTRA("death-messages.messages.elytra"),

  /**
   * %1$s이(가) %2$s에게서 도망치려다 운동 에너지를 온몸으로 느꼈습니다
   */
  ELYTRA_COMBAT("death-messages.messages.elytra-combat"),

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게서 도망치려다 운동 에너지를 온몸으로 느꼈습니다.
   */
  ELYTRA_COMBAT_ITEM("death-messages.messages.elytra-combat-item"),

  /**
   * %1$s이(가) %2$s을(를) 잘못 사용하였습니다.
   */
  ENDER_PEARL("death-messages.messages.ender-pearl"),

  /**
   * %1$s이(가) %2$s에게서 도망치려다 %3$s을(를) 잘못 사용하였습니다.
   */
  ENDER_PEARL_COMBAT("death-messages.messages.ender-pearl-combat"),

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게서 도망치려다 %4$s을(를) 잘못 사용하였습니다.
   */
  ENDER_PEARL_COMBAT_ITEM("death-messages.messages.ender-pearl-combat-item"),

  /**
   * %1$s이(가) 폭파당했습니다
   */
  EXPLOSION("death-messages.messages.explosion"),

  /**
   * %1$s이(가) %2$s에게 폭파당했습니다
   */
  EXPLOSION_COMBAT("death-messages.messages.explosion-combat"),

  /**
   * %1$s이(가) 어딘가에서 날아온 %2$s때문에 폭파당했습니다
   */
  EXPLOSION_COMBAT_UNKNOWN("death-messages.messages.explosion-combat-unknown"),

  /**
   * %1$s이(가) %2$s에게 %3$s(으)로 폭파당했습니다
   */
  EXPLOSION_COMBAT_ITEM("death-messages.messages.explosion-combat-item"),

  /**
   * %1$s이(가) 땅바닥으로 곤두박질쳤습니다
   */
  FALL("death-messages.messages.fall"),

  /**
   * %1$s이(가) %2$s에게서 도망치려다 땅바닥으로 곤두박질쳤습니다
   */
  FALL_COMBAT("death-messages.messages.fall-combat"),

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게서 도망치려다 땅바닥으로 곤두박질쳤습니다.
   */
  FALL_COMBAT_ITEM("death-messages.messages.fall-combat-item"),

  /**
   * %1$s이(가) 높은 곳에서 떨어졌습니다
   */
  FALL_HIGH("death-messages.messages.fall-high"),

  /**
   * %1$s은(는) %2$s 때문에 추락을 피하지 못했습니다
   */
  FALL_HIGH_COMBAT("death-messages.messages.fall-high-combat"),

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s 때문에 높은 곳에서 떨어졌습니다.
   */
  FALL_HIGH_COMBAT_ITEM("death-messages.messages.fall-high-combat-item"),

  /**
   * %1$s이(가) %2$s에서 땅바닥으로 곤두박질쳤습니다.
   */
  FALL_BLOCK("death-messages.messages.fall-block"),

  /**
   * %1$s이(가) %2$s에게서 도망치려다 %3$s에서 땅바닥으로 곤두박질쳤습니다.
   */
  FALL_BLOCK_COMBAT("death-messages.messages.fall-block-combat"),

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게서 도망치려다 %4$s에서 땅바닥으로 곤두박질쳤습니다.
   */
  FALL_BLOCK_COMBAT_ITEM("death-messages.messages.fall-block-combat-item"),

  /**
   * %1$s이(가) 높은 %2$s에서 떨어졌습니다.
   */
  FALL_BLOCK_HIGH("death-messages.messages.fall-block-high"),

  /**
   * %1$s이(가) %2$s 때문에 높은 %3$s에서 떨어졌습니다.
   */
  FALL_BLOCK_HIGH_COMBAT("death-messages.messages.fall-block-high-combat"),

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s 때문에 높은 %4$s에서 떨어졌습니다.
   */
  FALL_BLOCK_HIGH_COMBAT_ITEM("death-messages.messages.fall-block-high-combat-item"),

  /**
   * %1$s이(가) 떨어지는 블록에 짓눌렸습니다
   */
  FALLING_BLOCK("death-messages.messages.falling-block"),

  /**
   * %1$s이(가) %2$s과(와) 싸우다가 떨어지는 블록에 짓눌렸습니다
   */
  FALLING_BLOCK_COMBAT("death-messages.messages.falling-block-combat"),

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s과(와) 싸우다가 떨어지는 %4$s에 짓눌렸습니다.
   */
  FALLING_BLOCK_COMBAT_ITEM("death-messages.messages.falling-block-combat-item"),

  /**
   * %1$s이(가) 떨어지는 종유석에 찔렸습니다
   */
  FALLING_STALACTITE("death-messages.messages.falling-stalactite"),

  /**
   * %1$s이(가) %2$s과(와) 싸우다가 떨어지는 종유석에 찔렸습니다
   */
  FALLING_STALACTITE_COMBAT("death-messages.messages.falling-stalactite-combat"),

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s과(와) 싸우다가 떨어지는 %4$s에 찔렸습니다. 아야야야.
   */
  FALLING_STALACTITE_COMBAT_ITEM("death-messages.messages.falling-stalactite-combat-item"),

  /**
   * %1$s이(가) 화염구에 맞았습니다.
   */
  FIREBALL("death-messages.messages.fireball"),

  /**
   * %1$s이(가) %2$s이(가) 던진 화염구에 맞았습니다
   */
  FIREBALL_COMBAT("death-messages.messages.fireball-combat"),

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s이(가) 던진 화염구에 맞았습니다
   */
  FIREBALL_COMBAT_ITEM("death-messages.messages.fireball-combat-item"),

  /**
   * %1$s이(가) 어딘가에서 날아온 %2$s에 맞았습니다.
   */
  FIREBALL_COMBAT_UNKNOWN("death-messages.messages.fireball-combat-unknown"),

  /**
   * %1$s이(가) 불에 타 사망했습니다
   */
  FIRE("death-messages.messages.fire"),

  /**
   * %1$s이(가) %2$s과(와) 싸우다가 바삭하게 구워졌습니다
   */
  FIRE_COMBAT("death-messages.messages.fire-combat"),

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s과(와) 싸우다가 바삭하게 구워졌습니다.
   */
  FIRE_COMBAT_ITEM("death-messages.messages.fire-combat-item"),

  /**
   * %1$s이(가) 화염에 휩싸였습니다
   */
  FIRE_BLOCK("death-messages.messages.fire-block"),

  /**
   * %1$s이(가) %2$s과(와) 싸우다가 불에 빠졌습니다
   */
  FIRE_BLOCK_COMBAT("death-messages.messages.fire-block-combat"),

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s과(와) 싸우다가 불에 빠졌습니다.
   */
  FIRE_BLOCK_COMBAT_ITEM("death-messages.messages.fire-block-combat-item"),

  /**
   * %1$s이(가) 굉음과 함께 폭사했습니다.
   */
  FIREWORKS("death-messages.messages.fireworks-combat-item"),

  /**
   * %1$s이(가) %2$s이(가) 쏜 폭죽 때문에 굉음과 함께 폭사했습니다.
   */
  FIREWORKS_COMBAT("death-messages.messages.fireworks-combat-item"),

  /**
   * %1$s이(가) %2$s이(가) 쏜 %3$s 때문에 굉음과 함께 폭사했습니다.
   */
  FIREWORKS_COMBAT_ITEM("death-messages.messages.fireworks-combat-item"),

  /**
   * %1$s이(가) 어딘가에서 날아온 %2$s 때문에 굉음과 함께 폭사했습니다.
   */
  FIREWORKS_COMBAT_UNKNOWN("death-messages.messages.fireworks-combat-unknown"),

  /**
   * %1$s이(가) 굉음과 함께 폭사했습니다
   */
  FIREWORKS_CROSSBOW("death-messages.messages.fireworks-crossbow"),

  /**
   * %1$s이(가) %2$s과(와) 싸우다가 굉음과 함께 폭사했습니다
   */
  FIREWORKS_CROSSBOW_COMBAT("death-messages.messages.fireworks-crossbow-combat"),

  /**
   * %1$s이(가) %2$s이(가) %3$s(으)로 쏜 폭죽 때문에 굉음과 함께 폭사했습니다
   */
  FIREWORKS_CROSSBOW_COMBAT_ITEM("death-messages.messages.fireworks-crossbow-combat-item"),

  /**
   * %1$s(이)가 얼어 죽었습니다
   */
  FREEZE("death-messages.messages.freeze"),

  /**
   * %1$s이(가) %2$s 때문에 얼어 죽었습니다
   */
  FREEZE_COMBAT("death-messages.messages.freeze-combat"),

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s 때문에 얼어 죽었습니다.
   */
  FREEZE_COMBAT_ITEM("death-messages.messages.freeze-combat-item"),

  /**
   * %1$s이(가) 사망했습니다
   */
  GENERIC("death-messages.messages.generic"),

  /**
   * %1$s이(가) %2$s 때문에 죽었습니다
   */
  GENERIC_COMBAT("death-messages.messages.generic-combat"),

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s 때문에 죽었습니다."
   */
  GENERIC_COMBAT_ITEM("death-messages.messages.generic-combat-item"),

  /**
   * %1$s이(가) /kill %2$s 당했습니다.
   */
  KILL("death-messages.messages.kill"),

  /**
   * %1$s이(가) %2$s와(과) 싸우다가 /kill %3$s 당했습니다.
   */
  KILL_COMBAT("death-messages.messages.kill-combat"),

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s와(과) 싸우다가 /kill %4$s 당했습니다.
   */
  KILL_COMBAT_ITEM("death-messages.messages.kill-combat-item"),

  /**
   * %1$s이(가) 용암에 빠졌습니다
   */
  LAVA("death-messages.messages.lava"),

  /**
   * %1$s이(가) %2$s에게서 도망치려다 용암에 빠졌습니다
   */
  LAVA_COMBAT("death-messages.messages.lava-combat"),

  /**
   * %1$s이(가) %3$s(을)를 사용한 %2$s에게서 도망치려다 용암에 빠졌습니다.
   */
  LAVA_COMBAT_ITEM("death-messages.messages.lava-combat-item"),

  /**
   * %1$s이(가) 용암에 빠졌습니다
   */
  LAVA_CAULDRON("death-messages.messages.lava-cauldron"),

  /**
   * %1$s이(가) %2$s에게서 도망치려다 용암에 빠졌습니다
   */
  LAVA_CAULDRON_COMBAT("death-messages.messages.lava-cauldron-combat"),

  /**
   * %1$s이(가) %3$s(을)를 사용한 %2$s에게서 도망치려다 용암에 빠졌습니다.
   */
  LAVA_CAULDRON_COMBAT_ITEM("death-messages.messages.lava-cauldron-combat-item"),

  /**
   * %1$s이(가) 벼락을 맞았습니다
   */
  LIGHTNING_BOLT("death-messages.messages.lightning-bolt"),

  /**
   * %1$s이(가) %2$s과(와) 싸우다가 벼락을 맞았습니다
   */
  LIGHTNING_BOLT_COMBAT("death-messages.messages.lightning-bolt-combat"),

  /**
   * %1$s이(가) %3$s(을)를 사용한 %2$s과(와) 싸우다가 벼락을 맞았습니다.
   */
  LIGHTNING_BOLT_COMBAT_ITEM("death-messages.messages.lightning-bolt-combat-item"),

  /**
   * %1$s이(가) 마법으로 살해당했습니다
   */
  MAGIC("death-messages.messages.magic"),

  /**
   * %1$s이(가) %2$s에게서 도망치려다 마법으로 살해당했습니다
   */
  MAGIC_COMBAT("death-messages.messages.magic-combat"),

  /**
   * %1$s이(가) %2$s에게서 도망치려다 마법으로 살해당했습니다
   */
  MAGIC_COMBAT_ITEM("death-messages.messages.magic-combat-item"),

  /**
   * %1$s이(가) 어딘가에서 날아온 %2$s 때문에 마법으로 살해당했습니다.
   */
  MAGIC_COMBAT_UNKNOWN("death-messages.messages.magic-combat-unknown"),

  /**
   * %1$s이(가) 바닥이 용암인 것을 알아챘습니다
   */
  MAGMA_BLOCK("death-messages.messages.magma-block"),

  /**
   * %1$s이(가) %2$s 때문에 위험 지대에 빠졌습니다
   */
  MAGMA_BLOCK_COMBAT("death-messages.messages.magma-block-combat"),

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s 때문에 위험 지대에 빠졌습니다.
   */
  MAGMA_BLOCK_COMBAT_ITEM("death-messages.messages.magma-block-combat-item"),

  /**
   * %1$s이(가) 사망했습니다
   */
  MELEE("death-messages.messages.melee"),

  /**
   * %1$s이(가) %2$s에게 살해당했습니다
   */
  MELEE_COMBAT("death-messages.messages.melee-combat"),

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게 살해당했습니다
   */
  MELEE_COMBAT_ITEM("death-messages.messages.melee-combat-item"),

  /**
   * %1$s이(가) 썰렸습니다.
   */
  MELEE_SWEEP("death-messages.messages.melee-sweep"),

  /**
   * %1$s이(가) %2$s에게 썰렸습니다.
   */
  MELEE_SWEEP_COMBAT("death-messages.messages.melee-sweep-combat"),

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게 썰렸습니다.
   */
  MELEE_SWEEP_COMBAT_ITEM("death-messages.messages.melee-sweep-combat-item"),

  /**
   * %1$s이(가) 썰렸습니다.
   */
  MELEE_SWORD("death-messages.messages.melee-sword"),

  /**
   * %1$s이(가) %2$s에게 썰렸습니다.
   */
  MELEE_SWORD_COMBAT("death-messages.messages.melee-sword-combat"),

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게 썰렸습니다.
   */
  MELEE_SWORD_COMBAT_ITEM("death-messages.messages.melee-sword-combat-item"),

  /**
   * %1$s이(가) 녹아내렸습니다.
   */
  MELTING("death-messages.messages.melting"),

  /**
   * %1$s이(가) %2$s와(과) 싸우다 녹아내렸습니다.
   */
  MELTING_COMBAT("death-messages.messages.melting-combat"),

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s와(과) 싸우다 녹아내렸습니다.
   */
  MELTING_COMBAT_ITEM("death-messages.messages.melting-combat-item"),

  /**
   * %1$s이(가) 발사체에게 저격당했습니다.
   */
  PROJECTILE("death-messages.messages.projectile"),

  /**
   * %1$s이(가) %2$s에게 저격당했습니다
   */
  PROJECTILE_COMBAT("death-messages.messages.projectile-combat"),

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게 저격당했습니다
   */
  PROJECTILE_COMBAT_ITEM("death-messages.messages.projectile-combat-item"),

  /**
   * %1$s이(가) 어딘가에서 날아온 %2$s에게 저격당했습니다.
   */
  PROJECTILE_COMBAT_UNKNOWN("death-messages.messages.projectile-combat-unknown"),

  /**
   * %1$s이(가) 셜커 탄환에게 살해당했습니다.
   */
  SHULKER_BULLET("death-messages.messages.shulker-bullet"),

  /**
   * %1$s이(가) %2%s에게 살해당했습니다.
   */
  SHULKER_BULLET_COMBAT("death-messages.messages.shulker-bullet-combat"),

  // minecraft bug probably?
  /**
   * %1$s이(가) 어딘가에서 날아온 %2%s에게 살해당했습니다.
   */
  SHULKER_BULLET_UNKNOWN("death-messages.messages.shulker-bullet-unknown"),

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2%s에게 살해당했습니다.
   */
  SHULKER_BULLET_COMBAT_ITEM("death-messages.messages.shulker-bullet-combat-item"),

  /**
   * %1$s이(가) 어딘가에서 날아온 %2%s에게 살해당했습니다.
   */
  SHULKER_BULLET_COMBAT_UNKNOWN("death-messages.messages.shulker-bullet-combat-unknown"),

  /**
   * %1$s이(가) 석순에 찔렸습니다
   */
  STALAGMITE("death-messages.messages.stalagmite"),

  /**
   * %1$s이(가) %2$s와(과) 싸우다가 석순에 찔렸습니다
   */
  STALAGMITE_COMBAT("death-messages.messages.stalagmite-combat"),

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s와(과) 싸우다가 %4%s에 찔렸습니다.
   */
  STALAGMITE_COMBAT_ITEM("death-messages.messages.stalagmite-combat-item"),

  /**
   * %1$s이(가) 아사했습니다
   */
  STARVE("death-messages.messages.starve"),

  /**
   * %1$s이(가) %2$s과(와) 싸우다가 아사했습니다
   */
  STARVE_COMBAT("death-messages.messages.starve-combat"),

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s과(와) 싸우다가 아사했습니다.
   */
  STARVE_COMBAT_ITEM("death-messages.messages.starve-combat-item"),

  /**
   * %1$s이(가) 쏘여 사망했습니다
   */
  STING("death-messages.messages.sting"),

  /**
   * %1$s이(가) %2$s에게 쏘여 사망했습니다
   */
  STING_COMBAT("death-messages.messages.sting-combat"),

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게 쏘여 사망했습니다.
   */
  STING_COMBAT_ITEM("death-messages.messages.sting-combat-item"),

  /**
   * %1$s이(가) 벽 속에서 질식했습니다
   */
  SUFFOCATION("death-messages.messages.suffocation"),

  /**
   * %1$s이(가) %2$s과(와) 싸우다가 벽 속에서 질식했습니다
   */
  SUFFOCATION_COMBAT("death-messages.messages.suffocation-combat"),

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s과(와) 싸우다가 %4$s 속에서 질식했습니다.
   */
  SUFFOCATION_COMBAT_ITEM("death-messages.messages.suffocation-combat-item"),

  /**
   * %1$s이(가) 달콤한 열매 덤불에 찔려 죽었습니다
   */
  SWEET_BERRY_BUSH("death-messages.messages.sweet-berry-bush"),

  /**
   * %1$s이(가) %2$s에게서 도망치려다 달콤한 열매 덤불에 찔려 죽었습니다
   */
  SWEET_BERRY_BUSH_COMBAT("death-messages.messages.sweet-berry-bush-combat"),

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게서 도망치려다 %4$s에 찔려 죽었습니다.
   */
  SWEET_BERRY_BUSH_COMBAT_ITEM("death-messages.messages.sweet-berry-bush-combat-item"),

  /**
   * %1$s이(가) 누군가를 해치려다 살해당했습니다.
   */
  THORNS("death-messages.messages.thorns"),

  /**
   * %1$s이(가) %2$s을(를) 해치려다 살해당했습니다
   */
  THORNS_COMBAT("death-messages.messages.thorns"),

  /**
   * %1$s이(가) %2$s을(를) 해치려다 %3$s(으)로 살해당했습니다
   */
  THORNS_COMBAT_ITEM("death-messages.messages.thorns-combat-item"),

  /**
   * %1$s이(가) 삼지창에게 찔렸습니다
   */
  TRIDENT("death-messages.messages.trident"),

  /**
   * %1$s이(가) %2$s에게 찔렸습니다
   */
  TRIDENT_COMBAT("death-messages.messages.trident-combat"),

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게 찔렸습니다
   */
  TRIDENT_COMBAT_ITEM("death-messages.messages.trident-combat-item"),

  /**
   * %1$s이(가) 어딘가에서 날아온 %2$s에게 찔렸습니다.
   */
  TRIDENT_COMBAT_UNKNOWN("death-messages.messages.trident-combat-unknown"),

  /**
   * %1$s이(가) 알 수 없는 이유로 죽었습니다. 죄송합니다! 이 메시지가 뜨면 개발자가 일을 안 한겁니다! %2$s에서 해당 버그를 제보해주세요!
   */
  UNKNOWN("death-messages.messages.unknown"),

  /**
   * %1$s이(가) 세계 밖으로 떨어졌습니다
   */
  VOID("death-messages.messages.void"),

  /**
   * %1$s은(는) %2$s과(와)는 도저히 한 하늘을 같이 이고 살 수 없었습니다
   */
  VOID_COMBAT("death-messages.messages.void-combat"),

  /**
   * %1$s은(는) %3$s을(를) 사용한 %2$s과(와)는 도저히 한 하늘을 같이 이고 살 수 없었습니다.
   */
  VOID_COMBAT_ITEM("death-messages.messages.void-combat-item"),

  /**
   * %1$s이(가) 너무 높은 곳에서 세계 밖으로 떨어졌습니다.
   */
  VOID_HIGH("death-messages.messages.void-high"),

  /**
   * %1$s은(는) %2$s과(와)는 도저히 한 높은 하늘을 같이 이고 살 수 없었습니다.
   */
  VOID_HIGH_COMBAT("death-messages.messages.void-high-combat"),

  /**
   * %1$s은(는) %3$s을(를) 사용한 %2$s과(와)는 도저히 한 높은 하늘을 같이 이고 살 수 없었습니다.
   */
  VOID_HIGH_COMBAT_ITEM("death-messages.messages.void-high-combat-item"),

  /**
   * %1$s이(가) %2$s에서 세계 밖으로 떨어졌습니다.+
   *
   */
  VOID_BLOCK("death-messages.messages.void-block"),

  /**
   * %1$s은(는) %2$s과(와)는 도저히 한 하늘을 같이 이고 살 수 없었습니다
   */
  VOID_BLOCK_COMBAT("death-messages.messages.void-block-combat"),

  /**
   * %1$s은(는) %3$s을(를) 사용한 %2$s과(와)는 도저히 한 하늘을 같이 이고 살 수 없었습니다.
   */
  VOID_BLOCK_COMBAT_ITEM("death-messages.messages.void-block-combat-item"),

  /**
   * %1$s이(가) 너무 높은 곳에서 세계 밖으로 떨어졌습니다.
   */
  VOID_BLOCK_HIGH("death-messages.messages.void-block-high"),

  /**
   * %1$s은(는) %2$s과(와)는 도저히 한 높은 하늘을 같이 이고 살 수 없었습니다.
   */
  VOID_BLOCK_HIGH_COMBAT("death-messages.messages.void-block-high-combat"),

  /**
   * %1$s은(는) %3$s을(를) 사용한 %2$s과(와)는 도저히 한 높은 하늘을 같이 이고 살 수 없었습니다.
   */
  VOID_BLOCK_HIGH_COMBAT_ITEM("death-messages.messages.void-block-high-combat-item"),

  /**
   * %1$s이(가) 사그라졌습니다
   */
  WITHER("death-messages.messages.wither"),

  /**
   * %1$s이(가) %2$s과(와) 싸우다가 사그라졌습니다
   */
  WITHER_COMBAT("death-messages.messages.wither-combat"),

  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s과(와) 싸우다가 사그라졌습니다.
   */
  WITHER_COMBAT_ITEM("death-messages.messages.wither-combat-item"),

  /**
   * %1$s이(가) %2$s의 해골에게 저격당했습니다
   */
  WITHER_SKULL("death-messages.messages.wither-skull");


  private final String key;

  Messages(String key)
  {
    this.key = key;
  }

  public List<String> getKeys()
  {
    return new ArrayList<>(Variable.deathMessages.getStringList(key));
  }
}
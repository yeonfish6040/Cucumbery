package com.jho5245.cucumbery.deathmessages;

import com.jho5245.cucumbery.util.storage.data.Variable;

import java.util.ArrayList;
import java.util.List;

public enum Messages
{
  /**
   * %1$s이(가) 떨어지는 모루에 짓눌렸습니다
   */
  ANVIL(Variable.deathMessages.getStringList("death-messages.messages.anvil")),
  /**
   * %1$s이(가) %2$s과(와) 싸우다가 떨어지는 모루에 짓눌렸습니다
   */
  ANVIL_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.anvil-combat")),
  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s과(와) 싸우다가 떨어지는 모루에 짓눌렸습니다.
   */
  ANVIL_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.anvil-combat-item")),
  /**
   * %1$s이(가) %2$s에게 저격당했습니다
   */
  ARROW_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.arrow-combat")),
  /**
   * %1$s이(가) %2$s에게 저격당했습니다
   */
  ARROW_COMBAT_UNKNOWN(Variable.deathMessages.getStringList("death-messages.messages.arrow-combat-unknown")),
  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게 저격당했습니다
   */
  ARROW_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.arrow-combat-item")),
  /**
   * %1$s이(가) %2$s에게 목숨을 빼앗겼습니다
   */
  BAD_RESPAWN(Variable.deathMessages.getStringList("death-messages.messages.bad-respawn")),
  /**
   * %1$s이(가) 찔려 사망했습니다
   */
  CACTUS(Variable.deathMessages.getStringList("death-messages.messages.cactus")),
  /**
   * %1$s이(가) %2$s에게서 도망치려다 선인장에 찔렸습니다
   */
  CACTUS_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.cactus-combat")),
  /**
   * %1$s이(가) %3s을(를) 사용한 %2$s에게서 도망치려다 선인장에 찔렸습니다
   */
  CACTUS_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.cactus-combat-item")),
  /**
   * %1$s이(가) 으깨져버렸습니다
   */
  CRAMMING(Variable.deathMessages.getStringList("death-messages.messages.cramming")),
  /**
   * %1$s이(가) %2$s에게 짓눌렸습니다
   */
  CRAMMING_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.cramming-combat")),
  /**
   * %1$s이(가) %3s을(를) 사용한 %2$s과(와) 싸우다 짓눌렸습니다.
   */
  CRAMMING_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.cramming-combat-item")),
  /**
   * %1$s이(가) 드래곤의 숨결에 구워졌습니다
   */
  DRAGON_BREATH(Variable.deathMessages.getStringList("death-messages.messages.dragon-breath")),
  /**
   * %1$s이(가) %2$s 때문에 드래곤의 숨결에 구워졌습니다
   */
  DRAGON_BREATH_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.dragon-breath-combat")),
  /**
   * %1$s이(가) %2$s 때문에 드래곤의 숨결에 구워졌습니다
   */
  DRAGON_BREATH_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.dragon-breath-combat-item")),
  /**
   * %1$s이(가) 익사했습니다
   */
  DROWN(Variable.deathMessages.getStringList("death-messages.messages.drown")),
  /**
   * %1$s이(가) %2$s에게서 도망치려다 익사했습니다
   */
  DROWN_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.drown-combat")),
  /**
   * %1$s이(가) %2$s에게서 도망치려다 익사했습니다
   */
  DROWN_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.drown-combat-item")),
  /**
   * %1$s이(가) 폭파당했습니다
   */
  ENDER_PEARL(Variable.deathMessages.getStringList("death-messages.messages.ender-pearl")),
  /**
   * %1$s이(가) %2$s에게 폭파당했습니다
   */
  ENDER_PEARL_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.ender-pearl-combat")),
  /**
   * %1$s이(가) %2$s에게 %3$s(으)로 폭파당했습니다
   */
  ENDER_PEARL_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.ender-pearl-combat-item")),
  /**
   * %1$s이(가) 폭파당했습니다
   */
  EXPLOSION(Variable.deathMessages.getStringList("death-messages.messages.explosion")),
  /**
   * %1$s이(가) %2$s에게 폭파당했습니다
   */
  EXPLOSION_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.explosion-combat")),
  /**
   * %1$s이(가) %2$s에게 %3$s(으)로 폭파당했습니다
   */
  EXPLOSION_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.explosion-combat-item")),
  /**
   * %1$s이(가) 땅바닥으로 곤두박질쳤습니다
   */
  FALL(Variable.deathMessages.getStringList("death-messages.messages.fall")),
  /**
   * %1$s이(가) %2$s에게서 도망치려다 땅바닥으로 곤두박질쳤습니다
   */
  FALL_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.fall-combat")),
  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게서 도망치려다 땅바닥으로 곤두박질쳤습니다.
   */
  FALL_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.fall-combat-item")),
  /**
   * %1$s이(가) 높은 곳에서 떨어졌습니다
   */
  FALL_HIGH(Variable.deathMessages.getStringList("death-messages.messages.fall-high")),
  /**
   * %1$s은(는) %2$s 때문에 추락을 피하지 못했습니다
   */
  FALL_HIGH_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.fall-high-combat")),
  /**
   * %1$s이(가) 너무 높은 곳에서 떨어진 후, %2$s에게 최후를 맞이했습니다
   */
  FALL_HIGH_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.fall-high-combat-item")),
  /**
   * %1$s이(가) 땅바닥으로 곤두박질쳤습니다
   */
  FALL_BLOCK(Variable.deathMessages.getStringList("death-messages.messages.fall-block")),
  /**
   * %1$s이(가) %2$s에게서 도망치려다 땅바닥으로 곤두박질쳤습니다
   */
  FALL_BLOCK_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.fall-block-combat")),
  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게서 도망치려다 땅바닥으로 곤두박질쳤습니다.
   */
  FALL_BLOCK_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.fall-block-combat-item")),
  /**
   * %1$s이(가) 높은 곳에서 떨어졌습니다
   */
  FALL_BLOCK_HIGH(Variable.deathMessages.getStringList("death-messages.messages.fall-block-high")),
  /**
   * %1$s은(는) %2$s 때문에 추락을 피하지 못했습니다
   */
  FALL_BLOCK_HIGH_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.fall-block-high-combat")),
  /**
   * %1$s이(가) 너무 높은 곳에서 떨어진 후, %2$s에게 최후를 맞이했습니다
   */
  FALL_BLOCK_HIGH_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.fall-block-high-combat-item")),
  /**
   * %1$s이(가) 떨어지는 블록에 짓눌렸습니다
   */
  FALLING_BLOCK(Variable.deathMessages.getStringList("death-messages.messages.falling-block")),
  /**
   * %1$s이(가) %2$s과(와) 싸우다가 떨어지는 블록에 짓눌렸습니다
   */
  FALLING_BLOCK_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.falling-block-combat")),
  /**
   * %1$s이(가) %2$s과(와) 싸우다가 떨어지는 블록에 짓눌렸습니다
   */
  FALLING_BLOCK_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.falling-block-combat-item")),
  /**
   * %1$s이(가) 떨어지는 종유석에 찔렸습니다
   */
  FALLING_STALACTITE(Variable.deathMessages.getStringList("death-messages.messages.falling-stalactite")),
  /**
   * %1$s이(가) %2$s과(와) 싸우다가 떨어지는 종유석에 찔렸습니다
   */
  FALLING_STALACTITE_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.falling-stalactite-combat")),
  /**
   * %1$s이(가) %2$s과(와) 싸우다가 떨어지는 종유석에 찔렸습니다
   */
  FALLING_STALACTITE_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.falling-stalactite-combat-item")),
  /**
   * %1$s이(가) %2$s이(가) 던진 화염구에 맞았습니다
   */
  FIREBALL_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.fireball-combat")),
  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s이(가) 던진 화염구에 맞았습니다
   */
  FIREBALL_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.fireball-combat-item")),
  /**
   * %1$s이(가) 굉음과 함께 폭사했습니다
   */
  FIREWORKS_CROSSBOW(Variable.deathMessages.getStringList("death-messages.messages.fireworks-crossbow")),
  /**
   * %1$s이(가) %2$s과(와) 싸우다가 굉음과 함께 폭사했습니다
   */
  FIREWORKS_CROSSBOW_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.fireworks-crossbow-combat")),
  /**
   * %1$s이(가) %2$s이(가) %3$s(으)로 쏜 폭죽 때문에 굉음과 함께 폭사했습니다
   */
  FIREWORKS_CROSSBOW_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.fireworks-crossbow-combat-item")),
  /**
   * %1$s이(가) %2$s이(가) 쏜 %3$s 때문에 굉음과 함께 폭사했습니다.
   */
  FIREWORKS_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.fireworks-combat-item")),
  /**
   * %1$s이(가) 운동 에너지를 온몸으로 경험했습니다
   */
  ELYTRA(Variable.deathMessages.getStringList("death-messages.messages.elytra")),
  /**
   * %1$s이(가) %2$s에게서 도망치려다 운동 에너지를 온몸으로 느꼈습니다
   */
  ELYTRA_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.elytra-combat")),
  /**
   * %1$s이(가) %2$s에게서 도망치려다 운동 에너지를 온몸으로 느꼈습니다
   */
  ELYTRA_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.elytra-combat-item")),
  /**
   * %1$s(이)가 얼어 죽었습니다
   */
  FREEZE(Variable.deathMessages.getStringList("death-messages.messages.freeze")),
  /**
   * %1$s이(가) %2$s 때문에 얼어 죽었습니다
   */
  FREEZE_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.freeze-combat")),
  /**
   * %1$s이(가) %2$s 때문에 얼어 죽었습니다
   */
  FREEZE_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.freeze-combat-item")),
  /**
   * %1$s이(가) 사망했습니다
   */
  GENERIC(Variable.deathMessages.getStringList("death-messages.messages.generic")),
  /**
   * %1$s이(가) %2$s 때문에 죽었습니다
   */
  GENERIC_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.generic-combat")),
  /**
   * %1$s이(가) %2$s 때문에 죽었습니다
   */
  GENERIC_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.generic-combat-item")),
  /**
   * %1$s이(가) 바닥이 용암인 것을 알아챘습니다
   */
  MAGMA_BLOCK(Variable.deathMessages.getStringList("death-messages.messages.magma-block")),
  /**
   * %1$s이(가) %2$s 때문에 위험 지대에 빠졌습니다
   */
  MAGMA_BLOCK_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.magma-block-combat")),
  /**
   * %1$s이(가) %2$s 때문에 위험 지대에 빠졌습니다
   */
  MAGMA_BLOCK_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.magma-block-combat-item")),
  /**
   * %1$s이(가) 화염에 휩싸였습니다
   */
  FIRE_BLOCK(Variable.deathMessages.getStringList("death-messages.messages.fire-block")),
  /**
   * %1$s이(가) %2$s과(와) 싸우다가 불에 빠졌습니다
   */
  FIRE_BLOCK_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.fire-block-combat")),
  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s과(와) 싸우다가 불에 빠졌습니다.
   */
  FIRE_BLOCK_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.fire-block-combat-item")),
  /**
   * %1$s이(가) 벽 속에서 질식했습니다
   */
  SUFFOCATION(Variable.deathMessages.getStringList("death-messages.messages.suffocation")),
  /**
   * %1$s이(가) %2$s과(와) 싸우다가 벽 속에서 질식했습니다
   */
  SUFFOCATION_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.suffocation-combat")),
  /**
   * %1$s이(가) %2$s과(와) 싸우다가 벽 속에서 질식했습니다
   */
  SUFFOCATION_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.suffocation-combat-item")),
  /**
   * %1$s이(가) 용암에 빠졌습니다
   */
  LAVA(Variable.deathMessages.getStringList("death-messages.messages.lava")),
  /**
   * %1$s이(가) %2$s에게서 도망치려다 용암에 빠졌습니다
   */
  LAVA_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.lava-combat")),
  /**
   * %1$s이(가) %3$s(을)를 사용한 %2$s에게서 도망치려다 용암에 빠졌습니다.
   */
  LAVA_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.lava-combat-item")),
  /**
   * %1$s이(가) 벼락을 맞았습니다
   */
  LIGHTNING_BOLT(Variable.deathMessages.getStringList("death-messages.messages.lightning-bolt")),
  /**
   * %1$s이(가) %2$s과(와) 싸우다가 벼락을 맞았습니다
   */
  LIGHTNING_BOLT_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.lightning-bolt-combat")),
  /**
   * %1$s이(가) %2$s과(와) 싸우다가 벼락을 맞았습니다
   */
  LIGHTNING_BOLT_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.lightning-bolt-combat-item")),
  /**
   * %1$s이(가) 마법으로 살해당했습니다
   */
  MAGIC(Variable.deathMessages.getStringList("death-messages.messages.magic")),
  /**
   * %1$s이(가) %2$s에게서 도망치려다 마법으로 살해당했습니다
   */
  MAGIC_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.magic-combat")),
  /**
   * %1$s이(가) %2$s에게서 도망치려다 마법으로 살해당했습니다
   */
  MAGIC_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.magic-combat-item")),
  /**
   * %1$s이(가) %2$s에게 살해당했습니다
   */
  MELEE_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.melee-combat")),
  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게 살해당했습니다
   */
  MELEE_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.melee-combat-item")),
  /**
   * %1$s이(가) 불에 타 사망했습니다
   */
  FIRE(Variable.deathMessages.getStringList("death-messages.messages.fire")),
  /**
   * %1$s이(가) %2$s과(와) 싸우다가 바삭하게 구워졌습니다
   */
  FIRE_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.fire-combat")),
  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s과(와) 싸우다가 바삭하게 구워졌습니다.
   */
  FIRE_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.fire-combat-item")),
  /**
   * %1$s이(가) 세계 밖으로 떨어졌습니다
   */
  VOID(Variable.deathMessages.getStringList("death-messages.messages.void")),
  /**
   * %1$s은(는) %2$s과(와)는 도저히 한 하늘을 같이 이고 살 수 없었습니다
   */
  VOID_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.void-combat")),
  /**
   * %1$s은(는) %3$s을(를) 사용한 %2$s과(와)는 도저히 한 하늘을 같이 이고 살 수 없었습니다.
   */
  VOID_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.void-combat-item")),
  /**
   * %1$s이(가) 너무 높은 곳에서 세계 밖으로 떨어졌습니다.
   */
  VOID_HIGH(Variable.deathMessages.getStringList("death-messages.messages.void-high")),
  /**
   * %1$s은(는) %2$s과(와)는 도저히 한 높은 하늘을 같이 이고 살 수 없었습니다.
   */
  VOID_HIGH_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.void-high-combat")),
  /**
   * %1$s은(는) %3$s을(를) 사용한 %2$s과(와)는 도저히 한 높은 하늘을 같이 이고 살 수 없었습니다.
   */
  VOID_HIGH_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.void-high-combat-item")),
  /**
   * %1$s이(가) 세계 밖으로 떨어졌습니다
   */
  VOID_BLOCK(Variable.deathMessages.getStringList("death-messages.messages.void-block")),
  /**
   * %1$s은(는) %2$s과(와)는 도저히 한 하늘을 같이 이고 살 수 없었습니다
   */
  VOID_BLOCK_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.void-block-combat")),
  /**
   * %1$s은(는) %3$s을(를) 사용한 %2$s과(와)는 도저히 한 하늘을 같이 이고 살 수 없었습니다.
   */
  VOID_BLOCK_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.void-block-combat-item")),
  /**
   * %1$s이(가) 너무 높은 곳에서 세계 밖으로 떨어졌습니다.
   */
  VOID_BLOCK_HIGH(Variable.deathMessages.getStringList("death-messages.messages.void-block-high")),
  /**
   * %1$s은(는) %2$s과(와)는 도저히 한 높은 하늘을 같이 이고 살 수 없었습니다.
   */
  VOID_BLOCK_HIGH_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.void-block-high-combat")),
  /**
   * %1$s은(는) %3$s을(를) 사용한 %2$s과(와)는 도저히 한 높은 하늘을 같이 이고 살 수 없었습니다.
   */
  VOID_BLOCK_HIGH_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.void-block-high-combat-item")),
  /**
   * %1$s이(가) 석순에 찔렸습니다
   */
  STALAGMITE(Variable.deathMessages.getStringList("death-messages.messages.stalagmite")),
  /**
   * %1$s이(가) %2$s와(과) 싸우다가 석순에 찔렸습니다
   */
  STALAGMITE_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.stalagmite-combat")),
  /**
   * %1$s이(가) %2$s와(과) 싸우다가 석순에 찔렸습니다
   */
  STALAGMITE_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.stalagmite-combat-item")),
  /**
   * %1$s이(가) 아사했습니다
   */
  STARVE(Variable.deathMessages.getStringList("death-messages.messages.starve")),
  /**
   * %1$s이(가) %2$s과(와) 싸우다가 아사했습니다
   */
  STARVE_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.starve-combat")),
  /**
   * %1$s이(가) %2$s과(와) 싸우다가 아사했습니다
   */
  STARVE_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.starve-combat-item")),
  /**
   * %1$s이(가) 쏘여 사망했습니다
   */
  STING(Variable.deathMessages.getStringList("death-messages.messages.sting")),
  /**
   * %1$s이(가) %2$s에게 쏘여 사망했습니다
   */
  STING_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.sting-combat")),
  /**
   * %1$s이(가) 달콤한 열매 덤불에 찔려 죽었습니다
   */
  SWEET_BERRY_BUSH(Variable.deathMessages.getStringList("death-messages.messages.sweet-berry-bush")),
  /**
   * %1$s이(가) %2$s에게서 도망치려다 달콤한 열매 덤불에 찔려 죽었습니다
   */
  SWEET_BERRY_BUSH_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.sweet-berry-bush-combat")),
  /**
   * %1$s이(가) %2$s에게서 도망치려다 달콤한 열매 덤불에 찔려 죽었습니다
   */
  SWEET_BERRY_BUSH_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.sweet-berry-bush-combat-item")),
  /**
   * %1$s이(가) %2$s을(를) 해치려다 살해당했습니다
   */
  THORNS(Variable.deathMessages.getStringList("death-messages.messages.thorns")),
  /**
   * %1$s이(가) %2$s을(를) 해치려다 %3$s(으)로 살해당했습니다
   */
  THORNS_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.thorns-combat-item")),
  /**
   * %1$s이(가) %2$s에게 찔렸습니다
   */
  TRIDENT_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.trident-combat")),
  /**
   * %1$s이(가) 어딘가에서 날아온 %2$s에게 찔렸습니다.
   */
  TRIDENT_COMBAT_UNKNOWN(Variable.deathMessages.getStringList("death-messages.messages.trident-combat-unknown")),
  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게 찔렸습니다
   */
  TRIDENT_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.trident-combat-item")),
  /**
   * %1$s이(가) 사그라졌습니다
   */
  WITHER(Variable.deathMessages.getStringList("death-messages.messages.wither")),
  /**
   * %1$s이(가) %2$s과(와) 싸우다가 사그라졌습니다
   */
  WITHER_COMBAT(Variable.deathMessages.getStringList("death-messages.messages.wither-combat")),
  /**
   * %1$s이(가) %2$s과(와) 싸우다가 사그라졌습니다
   */
  WITHER_COMBAT_ITEM(Variable.deathMessages.getStringList("death-messages.messages.wither-combat-item")),
  /**
   * %1$s이(가) %2$s의 해골에게 저격당했습니다
   */
  WITHER_SKULL(Variable.deathMessages.getStringList("death-messages.messages.wither-skull")),
  ;

  private final List<String> keys;

  Messages(List<String> keys)
  {
    this.keys = new ArrayList<>(keys);
  }

  public List<String> getKeys()
  {
    return new ArrayList<>(keys);
  }
}
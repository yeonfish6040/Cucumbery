package com.jho5245.cucumbery.deathmessages;

import com.jho5245.cucumbery.util.storage.data.Variable;

import java.util.ArrayList;
import java.util.List;

public enum Messages
{
  /**
   * %1$s이(가) 떨어지는 모루에 짓눌렸습니다
   */
  attack_anvil(Variable.deathMessages.getStringList("death-messages.messages.anvil")),
  /**
   * %1$s이(가) %2$s과(와) 싸우다가 떨어지는 모루에 짓눌렸습니다
   */
  attack_anvil_player(Variable.deathMessages.getStringList("death-messages.messages.anvil-player")),
  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s과(와) 싸우다가 떨어지는 모루에 짓눌렸습니다.
   */
  attack_anvil_player_item(Variable.deathMessages.getStringList("death-messages.messages.anvil-player-item")),
  /**
   * %1$s이(가) %2$s에게 저격당했습니다
   */
  attack_arrow_player(Variable.deathMessages.getStringList("death-messages.messages.arrow")),
  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게 저격당했습니다
   */
  attack_arrow_player_item(Variable.deathMessages.getStringList("death-messages.messages.arrow-item")),
  /**
   * %1$s이(가) %2$s에게 목숨을 빼앗겼습니다
   */
  attack_badRespawnPoint_message(Variable.deathMessages.getStringList("death-messages.messages.bad-respawn")),
  /**
   * %1$s이(가) 찔려 사망했습니다
   */
  attack_cactus(Variable.deathMessages.getStringList("death-messages.messages.cactus")),
  /**
   * %1$s이(가) %2$s에게서 도망치려다 선인장에 찔렸습니다
   */
  attack_cactus_player(Variable.deathMessages.getStringList("death-messages.messages.cactus-player")),

  /**
   * %1$s이(가) %3s을(를) 사용한 %2$s에게서 도망치려다 선인장에 찔렸습니다
   */
  attack_cactus_player_item(Variable.deathMessages.getStringList("death-messages.messages.cactus-player-item")),
  /**
   * %1$s이(가) 으깨져버렸습니다
   */
  attack_cramming(Variable.deathMessages.getStringList("death-messages.messages.cramming")),
  /**
   * %1$s이(가) %2$s에게 짓눌렸습니다
   */
  attack_cramming_player(Variable.deathMessages.getStringList("death-messages.messages.cramming-player")),
  /**
   * %1$s이(가) %3s을(를) 사용한 %2$s과(와) 싸우다 짓눌렸습니다.
   */
  attack_cramming_player_item(Variable.deathMessages.getStringList("death-messages.messages.cramming-player-item")),
  /**
   * %1$s이(가) 드래곤의 숨결에 구워졌습니다
   */
  attack_dragonBreath(Variable.deathMessages.getStringList("death-messages.messages.dragon-breath")),
  /**
   * %1$s이(가) %2$s 때문에 드래곤의 숨결에 구워졌습니다
   */
  attack_dragonBreath_player(Variable.deathMessages.getStringList("death-messages.messages.dragon-breath-player")),
  /**
   * %1$s이(가) %2$s 때문에 드래곤의 숨결에 구워졌습니다
   */
  attack_dragonBreath_player_item(Variable.deathMessages.getStringList("death-messages.messages.dragon-breath-player-item")),
  /**
   * %1$s이(가) 익사했습니다
   */
  attack_drown(Variable.deathMessages.getStringList("death-messages.messages.drown")),
  /**
   * %1$s이(가) %2$s에게서 도망치려다 익사했습니다
   */
  attack_drown_player(Variable.deathMessages.getStringList("death-messages.messages.drown-player")),
  /**
   * %1$s이(가) %2$s에게서 도망치려다 익사했습니다
   */
  attack_drown_player_item(Variable.deathMessages.getStringList("death-messages.messages.drown-player-item")),
  /**
   * %1$s이(가) 더욱 심오한 마법으로 살해당했습니다
   */
  attack_even_more_magic(Variable.deathMessages.getStringList("death-messages.messages.even-more-magic")),
  /**
   * %1$s이(가) 폭파당했습니다
   */
  attack_enderPearl(Variable.deathMessages.getStringList("death-messages.messages.ender-pearl")),
  /**
   * %1$s이(가) %2$s에게 폭파당했습니다
   */
  attack_enderPearl_player(Variable.deathMessages.getStringList("death-messages.messages.ender-pearl-player")),
  /**
   * %1$s이(가) %2$s에게 %3$s(으)로 폭파당했습니다
   */
  attack_enderPearl_player_item(Variable.deathMessages.getStringList("death-messages.messages.ender-pearl-player-item")),
  /**
   * %1$s이(가) 폭파당했습니다
   */
  attack_explosion(Variable.deathMessages.getStringList("death-messages.messages.explosion")),
  /**
   * %1$s이(가) %2$s에게 폭파당했습니다
   */
  attack_explosion_player(Variable.deathMessages.getStringList("death-messages.messages.explosion-player")),
  /**
   * %1$s이(가) %2$s에게 %3$s(으)로 폭파당했습니다
   */
  attack_explosion_player_item(Variable.deathMessages.getStringList("death-messages.messages.explosion-player-item")),
  /**
   * %1$s이(가) 땅바닥으로 곤두박질쳤습니다
   */
  attack_fall(Variable.deathMessages.getStringList("death-messages.messages.fall")),
  /**
   * %1$s이(가) %2$s에게서 도망치려다 땅바닥으로 곤두박질쳤습니다
   */
  attack_fall_player(Variable.deathMessages.getStringList("death-messages.messages.fall-player")),
  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게서 도망치려다 땅바닥으로 곤두박질쳤습니다.
   */
  attack_fall_player_item(Variable.deathMessages.getStringList("death-messages.messages.fall-player-item")),
  /**
   * %1$s이(가) 높은 곳에서 떨어졌습니다
   */
  attack_fall_high(Variable.deathMessages.getStringList("death-messages.messages.fall-high")),
  /**
   * %1$s은(는) %2$s 때문에 추락을 피하지 못했습니다
   */
  attack_fall_high_player(Variable.deathMessages.getStringList("death-messages.messages.fall-high-player")),
  /**
   * %1$s이(가) 너무 높은 곳에서 떨어진 후, %2$s에게 최후를 맞이했습니다
   */
  attack_fall_high_player_item(Variable.deathMessages.getStringList("death-messages.messages.fall-high-player-item")),
  /**
   * %1$s이(가) 땅바닥으로 곤두박질쳤습니다
   */
  attack_fall_block(Variable.deathMessages.getStringList("death-messages.messages.fall-block")),
  /**
   * %1$s이(가) %2$s에게서 도망치려다 땅바닥으로 곤두박질쳤습니다
   */
  attack_fall_block_player(Variable.deathMessages.getStringList("death-messages.messages.fall-block-player")),
  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게서 도망치려다 땅바닥으로 곤두박질쳤습니다.
   */
  attack_fall_block_player_item(Variable.deathMessages.getStringList("death-messages.messages.fall-block-player-item")),
  /**
   * %1$s이(가) 높은 곳에서 떨어졌습니다
   */
  attack_fall_block_high(Variable.deathMessages.getStringList("death-messages.messages.fall-block-high")),
  /**
   * %1$s은(는) %2$s 때문에 추락을 피하지 못했습니다
   */
  attack_fall_block_high_player(Variable.deathMessages.getStringList("death-messages.messages.fall-block-high-player")),
  /**
   * %1$s이(가) 너무 높은 곳에서 떨어진 후, %2$s에게 최후를 맞이했습니다
   */
  attack_fall_block_high_player_item(Variable.deathMessages.getStringList("death-messages.messages.fall-block-high-player-item")),
  /**
   * %1$s이(가) 떨어지는 블록에 짓눌렸습니다
   */
  attack_fallingBlock(Variable.deathMessages.getStringList("death-messages.messages.falling-block")),
  /**
   * %1$s이(가) %2$s과(와) 싸우다가 떨어지는 블록에 짓눌렸습니다
   */
  attack_fallingBlock_player(Variable.deathMessages.getStringList("death-messages.messages.falling-block-player")),
  /**
   * %1$s이(가) %2$s과(와) 싸우다가 떨어지는 블록에 짓눌렸습니다
   */
  attack_fallingBlock_player_item(Variable.deathMessages.getStringList("death-messages.messages.falling-block-player-item")),
  /**
   * %1$s이(가) 떨어지는 종유석에 찔렸습니다
   */
  attack_fallingStalactite(Variable.deathMessages.getStringList("death-messages.messages.falling-stalactite")),
  /**
   * %1$s이(가) %2$s과(와) 싸우다가 떨어지는 종유석에 찔렸습니다
   */
  attack_fallingStalactite_player(Variable.deathMessages.getStringList("death-messages.messages.falling-stalactite-player")),
  /**
   * %1$s이(가) %2$s과(와) 싸우다가 떨어지는 종유석에 찔렸습니다
   */
  attack_fallingStalactite_player_item(Variable.deathMessages.getStringList("death-messages.messages.falling-stalactite-player-item")),
  /**
   * %1$s이(가) %2$s이(가) 던진 화염구에 맞았습니다
   */
  attack_fireball_player(Variable.deathMessages.getStringList("death-messages.messages.fireball")),
  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s이(가) 던진 화염구에 맞았습니다
   */
  attack_fireball_player_item(Variable.deathMessages.getStringList("death-messages.messages.fireball-item")),
  /**
   * %1$s이(가) 굉음과 함께 폭사했습니다
   */
  attack_fireworks(Variable.deathMessages.getStringList("death-messages.messages.fireworks")),
  /**
   * %1$s이(가) %2$s과(와) 싸우다가 굉음과 함께 폭사했습니다
   */
  attack_fireworks_player(Variable.deathMessages.getStringList("death-messages.messages.fireworks-player")),
  /**
   * %1$s이(가) %2$s이(가) %3$s(으)로 쏜 폭죽 때문에 굉음과 함께 폭사했습니다
   */
  attack_fireworks_player_item(Variable.deathMessages.getStringList("death-messages.messages.fireworks-item")),
  /**
   * %1$s이(가) %2$s이(가) 쏜 %3$s 때문에 굉음과 함께 폭사했습니다.
   */
  attack_fireworksRocket_player_item(Variable.deathMessages.getStringList("death-messages.messages.fireworks-rocket-item")),
  /**
   * %1$s이(가) 운동 에너지를 온몸으로 경험했습니다
   */
  attack_flyIntoWall(Variable.deathMessages.getStringList("death-messages.messages.fly-into-wall")),
  /**
   * %1$s이(가) %2$s에게서 도망치려다 운동 에너지를 온몸으로 느꼈습니다
   */
  attack_flyIntoWall_player(Variable.deathMessages.getStringList("death-messages.messages.fly-into-wall-player")),
  /**
   * %1$s이(가) %2$s에게서 도망치려다 운동 에너지를 온몸으로 느꼈습니다
   */
  attack_flyIntoWall_player_item(Variable.deathMessages.getStringList("death-messages.messages.fly-into-wall-player-item")),
  /**
   * %1$s(이)가 얼어 죽었습니다
   */
  attack_freeze(Variable.deathMessages.getStringList("death-messages.messages.freeze")),
  /**
   * %1$s이(가) %2$s 때문에 얼어 죽었습니다
   */
  attack_freeze_player(Variable.deathMessages.getStringList("death-messages.messages.freeze-player")),
  /**
   * %1$s이(가) %2$s 때문에 얼어 죽었습니다
   */
  attack_freeze_player_item(Variable.deathMessages.getStringList("death-messages.messages.freeze-player-item")),
  /**
   * %1$s이(가) 사망했습니다
   */
  attack_generic(Variable.deathMessages.getStringList("death-messages.messages.generic")),
  /**
   * %1$s이(가) %2$s 때문에 죽었습니다
   */
  attack_generic_player(Variable.deathMessages.getStringList("death-messages.messages.generic-player")),
  /**
   * %1$s이(가) %2$s 때문에 죽었습니다
   */
  attack_generic_player_item(Variable.deathMessages.getStringList("death-messages.messages.generic-player-item")),
  /**
   * %1$s이(가) 바닥이 용암인 것을 알아챘습니다
   */
  attack_hotFloor(Variable.deathMessages.getStringList("death-messages.messages.hot-floor")),
  /**
   * %1$s이(가) %2$s 때문에 위험 지대에 빠졌습니다
   */
  attack_hotFloor_player(Variable.deathMessages.getStringList("death-messages.messages.hot-floor-player")),
  /**
   * %1$s이(가) %2$s 때문에 위험 지대에 빠졌습니다
   */
  attack_hotFloor_player_item(Variable.deathMessages.getStringList("death-messages.messages.hot-floor-player-item")),
  /**
   * %1$s이(가) 화염에 휩싸였습니다
   */
  attack_inFire(Variable.deathMessages.getStringList("death-messages.messages.in-fire")),
  /**
   * %1$s이(가) %2$s과(와) 싸우다가 불에 빠졌습니다
   */
  attack_inFire_player(Variable.deathMessages.getStringList("death-messages.messages.in-fire-player")),
  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s과(와) 싸우다가 불에 빠졌습니다.
   */
  attack_inFire_player_item(Variable.deathMessages.getStringList("death-messages.messages.in-fire-player-item")),
  /**
   * %1$s이(가) 벽 속에서 질식했습니다
   */
  attack_inWall(Variable.deathMessages.getStringList("death-messages.messages.in-wall")),
  /**
   * %1$s이(가) %2$s과(와) 싸우다가 벽 속에서 질식했습니다
   */
  attack_inWall_player(Variable.deathMessages.getStringList("death-messages.messages.in-wall-player")),
  /**
   * %1$s이(가) %2$s과(와) 싸우다가 벽 속에서 질식했습니다
   */
  attack_inWall_player_item(Variable.deathMessages.getStringList("death-messages.messages.in-wall-player-item")),
  /**
   * %1$s이(가) %2$s에게 마법으로 살해당했습니다
   */
  attack_indirectMagic(Variable.deathMessages.getStringList("death-messages.messages.indirect-magic")),
  /**
   * %1$s이(가) %2$s에게 %3$s(으)로 살해당했습니다
   */
  attack_indirectMagic_player_item(Variable.deathMessages.getStringList("death-messages.messages.indirect-magic-item")),
  /**
   * %1$s이(가) 용암에 빠졌습니다
   */
  attack_lava(Variable.deathMessages.getStringList("death-messages.messages.lava")),
  /**
   * %1$s이(가) %2$s에게서 도망치려다 용암에 빠졌습니다
   */
  attack_lava_player(Variable.deathMessages.getStringList("death-messages.messages.lava-player")),
  /**
   * %1$s이(가) %3$s(을)를 사용한 %2$s에게서 도망치려다 용암에 빠졌습니다.
   */
  attack_lava_player_item(Variable.deathMessages.getStringList("death-messages.messages.lava-player-item")),
  /**
   * %1$s이(가) 벼락을 맞았습니다
   */
  attack_lightningBolt(Variable.deathMessages.getStringList("death-messages.messages.lightning-bolt")),
  /**
   * %1$s이(가) %2$s과(와) 싸우다가 벼락을 맞았습니다
   */
  attack_lightningBolt_player(Variable.deathMessages.getStringList("death-messages.messages.lightning-bolt-player")),
  /**
   * %1$s이(가) %2$s과(와) 싸우다가 벼락을 맞았습니다
   */
  attack_lightningBolt_player_item(Variable.deathMessages.getStringList("death-messages.messages.lightning-bolt-player-item")),
  /**
   * %1$s이(가) 마법으로 살해당했습니다
   */
  attack_magic(Variable.deathMessages.getStringList("death-messages.messages.magic")),
  /**
   * %1$s이(가) %2$s에게서 도망치려다 마법으로 살해당했습니다
   */
  attack_magic_player(Variable.deathMessages.getStringList("death-messages.messages.magic-player")),
  /**
   * %1$s이(가) %2$s에게서 도망치려다 마법으로 살해당했습니다
   */
  attack_magic_player_item(Variable.deathMessages.getStringList("death-messages.messages.magic-player-item")),
  /**
   * %1$s이(가) %2$s에게 살해당했습니다
   */
  attack_mob_player(Variable.deathMessages.getStringList("death-messages.messages.mob")),
  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게 살해당했습니다
   */
  attack_mob_player_item(Variable.deathMessages.getStringList("death-messages.messages.mob-item")),
  /**
   * %1$s이(가) 불에 타 사망했습니다
   */
  attack_onFire(Variable.deathMessages.getStringList("death-messages.messages.fire")),
  /**
   * %1$s이(가) %2$s과(와) 싸우다가 바삭하게 구워졌습니다
   */
  attack_onFire_player(Variable.deathMessages.getStringList("death-messages.messages.fire-player")),
  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s과(와) 싸우다가 바삭하게 구워졌습니다.
   */
  attack_onFire_player_item(Variable.deathMessages.getStringList("death-messages.messages.fire-player-item")),
  /**
   * %1$s이(가) 세계 밖으로 떨어졌습니다
   */
  attack_outOfWorld(Variable.deathMessages.getStringList("death-messages.messages.out-of-world")),
  /**
   * %1$s은(는) %2$s과(와)는 도저히 한 하늘을 같이 이고 살 수 없었습니다
   */
  attack_outOfWorld_player(Variable.deathMessages.getStringList("death-messages.messages.out-of-world-player")),
  /**
   * %1$s은(는) %3$s을(를) 사용한 %2$s과(와)는 도저히 한 하늘을 같이 이고 살 수 없었습니다.
   */
  attack_outOfWorld_player_item(Variable.deathMessages.getStringList("death-messages.messages.out-of-world-player-item")),
  /**
   * %1$s이(가) 너무 높은 곳에서 세계 밖으로 떨어졌습니다.
   */
  attack_outOfWorld_high(Variable.deathMessages.getStringList("death-messages.messages.out-of-world-high")),
  /**
   * %1$s은(는) %2$s과(와)는 도저히 한 높은 하늘을 같이 이고 살 수 없었습니다.
   */
  attack_outOfWorld_high_player(Variable.deathMessages.getStringList("death-messages.messages.out-of-world-high-player")),
  /**
   * %1$s은(는) %3$s을(를) 사용한 %2$s과(와)는 도저히 한 높은 하늘을 같이 이고 살 수 없었습니다.
   */
  attack_outOfWorld_high_player_item(Variable.deathMessages.getStringList("death-messages.messages.out-of-world-high-player-item")),
  /**
   * %1$s이(가) 세계 밖으로 떨어졌습니다
   */
  attack_outOfWorld_block(Variable.deathMessages.getStringList("death-messages.messages.out-of-world")),
  /**
   * %1$s은(는) %2$s과(와)는 도저히 한 하늘을 같이 이고 살 수 없었습니다
   */
  attack_outOfWorld_block_player(Variable.deathMessages.getStringList("death-messages.messages.out-of-world-player")),
  /**
   * %1$s은(는) %3$s을(를) 사용한 %2$s과(와)는 도저히 한 하늘을 같이 이고 살 수 없었습니다.
   */
  attack_outOfWorld_block_player_item(Variable.deathMessages.getStringList("death-messages.messages.out-of-world-player-item")),
  /**
   * %1$s이(가) 너무 높은 곳에서 세계 밖으로 떨어졌습니다.
   */
  attack_outOfWorld_block_high(Variable.deathMessages.getStringList("death-messages.messages.out-of-world-high")),
  /**
   * %1$s은(는) %2$s과(와)는 도저히 한 높은 하늘을 같이 이고 살 수 없었습니다.
   */
  attack_outOfWorld_block_high_player(Variable.deathMessages.getStringList("death-messages.messages.out-of-world-high-player")),
  /**
   * %1$s은(는) %3$s을(를) 사용한 %2$s과(와)는 도저히 한 높은 하늘을 같이 이고 살 수 없었습니다.
   */
  attack_outOfWorld_block_high_player_item(Variable.deathMessages.getStringList("death-messages.messages.out-of-world-high-player-item")),
  /**
   * %1$s이(가) %2$s에게 살해당했습니다
   */
  attack_player(Variable.deathMessages.getStringList("death-messages.messages.player")),
  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게 살해당했습니다
   */
  attack_player_item(Variable.deathMessages.getStringList("death-messages.messages.player-item")),
  /**
   * %1$s이(가) 석순에 찔렸습니다
   */
  attack_stalagmite(Variable.deathMessages.getStringList("death-messages.messages.stalagmite")),
  /**
   * %1$s이(가) %2$s와(과) 싸우다가 석순에 찔렸습니다
   */
  attack_stalagmite_player(Variable.deathMessages.getStringList("death-messages.messages.stalagmite-player")),
  /**
   * %1$s이(가) %2$s와(과) 싸우다가 석순에 찔렸습니다
   */
  attack_stalagmite_player_item(Variable.deathMessages.getStringList("death-messages.messages.stalagmite-player-item")),
  /**
   * %1$s이(가) 아사했습니다
   */
  attack_starve(Variable.deathMessages.getStringList("death-messages.messages.starve")),
  /**
   * %1$s이(가) %2$s과(와) 싸우다가 아사했습니다
   */
  attack_starve_player(Variable.deathMessages.getStringList("death-messages.messages.starve-player")),
  /**
   * %1$s이(가) %2$s과(와) 싸우다가 아사했습니다
   */
  attack_starve_player_item(Variable.deathMessages.getStringList("death-messages.messages.starve-player-item")),
  /**
   * %1$s이(가) 쏘여 사망했습니다
   */
  attack_sting(Variable.deathMessages.getStringList("death-messages.messages.sting")),
  /**
   * %1$s이(가) %2$s에게 쏘여 사망했습니다
   */
  attack_sting_player(Variable.deathMessages.getStringList("death-messages.messages.sting-player")),
  /**
   * %1$s이(가) 달콤한 열매 덤불에 찔려 죽었습니다
   */
  attack_sweetBerryBush(Variable.deathMessages.getStringList("death-messages.messages.sweet-berry-bush")),
  /**
   * %1$s이(가) %2$s에게서 도망치려다 달콤한 열매 덤불에 찔려 죽었습니다
   */
  attack_sweetBerryBush_player(Variable.deathMessages.getStringList("death-messages.messages.sweet-berry-bush-player")),
  /**
   * %1$s이(가) %2$s에게서 도망치려다 달콤한 열매 덤불에 찔려 죽었습니다
   */
  attack_sweetBerryBush_player_item(Variable.deathMessages.getStringList("death-messages.messages.sweet-berry-bush-player-item")),
  /**
   * %1$s이(가) %2$s을(를) 해치려다 살해당했습니다
   */
  attack_thorns(Variable.deathMessages.getStringList("death-messages.messages.thorns")),
  /**
   * %1$s이(가) %2$s을(를) 해치려다 %3$s(으)로 살해당했습니다
   */
  attack_thorns_player_item(Variable.deathMessages.getStringList("death-messages.messages.thorns-item")),
  /**
   * %1$s이(가) %2$s에게 구타당했습니다
   */
  attack_thrown(Variable.deathMessages.getStringList("death-messages.messages.thrown")),
  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게 구타당했습니다
   */
  attack_thrown_player_item(Variable.deathMessages.getStringList("death-messages.messages.thrown-item")),
  /**
   * %1$s이(가) %2$s에게 찔렸습니다
   */
  attack_trident(Variable.deathMessages.getStringList("death-messages.messages.trident")),
  /**
   * %1$s이(가) %3$s을(를) 사용한 %2$s에게 찔렸습니다
   */
  attack_trident_player_item(Variable.deathMessages.getStringList("death-messages.messages.trident-item")),
  /**
   * %1$s이(가) 사그라졌습니다
   */
  attack_wither(Variable.deathMessages.getStringList("death-messages.messages.wither")),
  /**
   * %1$s이(가) %2$s과(와) 싸우다가 사그라졌습니다
   */
  attack_wither_player(Variable.deathMessages.getStringList("death-messages.messages.wither-player")),
  /**
   * %1$s이(가) %2$s과(와) 싸우다가 사그라졌습니다
   */
  attack_wither_player_item(Variable.deathMessages.getStringList("death-messages.messages.wither-player-item")),
  /**
   * %1$s이(가) %2$s의 해골에게 저격당했습니다
   */
  attack_witherSkull(Variable.deathMessages.getStringList("death-messages.messages.wither-skull")),
  /**
   * %1$s이(가) 높은 곳에서 떨어졌습니다
   */
  fell_accident_generic(Variable.deathMessages.getStringList("death-messages.messages.fell")),
  /**
   * %1$s이(가) 사다리에서 떨어졌습니다
   */
  fell_accident_ladder(Variable.deathMessages.getStringList("death-messages.messages.fell-ladder")),
  /**
   * %1$s이(가) 비계에서 떨어졌습니다
   */
  fell_accident_scaffolding(Variable.deathMessages.getStringList("death-messages.messages.fell-scaffolding")),
  /**
   * %1$s이(가) 덩굴에서 떨어졌습니다
   */
  fell_accident_vines(Variable.deathMessages.getStringList("death-messages.messages.fell-vines")),
  /**
   * %1$s이(가) 휘어진 덩굴에서 떨어졌습니다
   */
  fell_accident_twisting_vines(Variable.deathMessages.getStringList("death-messages.messages.fell-twisting-vines")),
  /**
   * %1$s이(가) 늘어진 덩굴에서 떨어졌습니다
   */
  fell_accident_weeping_vines(Variable.deathMessages.getStringList("death-messages.messages.fell-weeping-vines")),
  /**
   * %1$s이(가) 무언가를 타고 오르던 중 떨어졌습니다
   */
  fell_other_climbable(Variable.deathMessages.getStringList("death-messages.messages.fell-other")),
  /**
   * %1$s은(는) %2$s 때문에 추락을 피하지 못했습니다
   */
  fell_assist(Variable.deathMessages.getStringList("death-messages.messages.fell-assist")),
  /**
   * %1$s은(는) %3$s을(를) 사용한 %2$s 때문에 추락을 피하지 못했습니다
   */
  fell_assist_item(Variable.deathMessages.getStringList("death-messages.messages.fell-assist-item")),
  /**
   * %1$s이(가) 너무 높은 곳에서 떨어진 후, %2$s에게 최후를 맞이했습니다
   */
  fell_finish(Variable.deathMessages.getStringList("death-messages.messages.fell-finish")),
  /**
   * %1$s이(가) 너무 높은 곳에서 떨어진 후, %3$s을(를) 사용한 %2$s에게 최후를 맞이했습니다
   */
  fell_finish_item(Variable.deathMessages.getStringList("death-messages.messages.fell-finish-item")),
  /**
   * %1$s은(는) 추락을 피하지 못했습니다
   */
  fell_killer(Variable.deathMessages.getStringList("death-messages.messages.fell-killer")),
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

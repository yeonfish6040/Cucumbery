package com.jho5245.cucumbery.deathmessages;

public enum ConditionType
{
  /**
   * 죽은 이유
   */
  DEATH_TYPE,
  
  /**
   * 죽은 개체의 UUID
   */
  ENTITY_UUID,
  
  /**
   * 죽은 개체의 유형
   */
  ENTITY_TYPE,

  /**
   * 죽은 개체의 표시 이름
   */
  ENTITY_DISPLAY_NAME,
  
  /**
   * 죽은 플레이어의 이름
   */
  PLAYER_NAME,
  
  /**
   * 죽은 플레이어의 닉네임
   */
  PLAYER_DISPLAY_NAME,
  
  /**
   * 죽은 플레이어의 퍼미션 노드
   */
  PLAYER_PERMISSION,
  
  /**
   * 죽은 위치의 월드 이름
   */
  WORLD_NAME,

  /**
   * 죽은 위치의 바이옴 이름
   */
  BIOME_TYPE,
  
  /**
   * 죽인 개체의 유형
   */
  ATTACKER_ENTITY_TYPE,
  
  /**
   * 죽인 개체의 표시 이름
   */
  ATTACKER_ENTITY_DISPLAY_NAME,
  
  /**
   * 죽인 플레이어의 이름
   */
  ATTACKER_PLAYER_NAME,
  
  /**
   * 죽인 플레이어의 닉네임
   */
  ATTACKER_PLAYER_DISPLAY_NAME,
  /**
   * 죽인 플레이어의 퍼미션
   */
  ATTACKER_PLAYER_PERMISSION,
  /**
   * 사용한 무기 종류
   */
  WEAPON_TYPE,
  
  /**
   * 사용한 무기 이름
   */
  WEAPON_DISPLAY_NAME,

  /**
   * NBT
   */
  WEAPON_NBT,
  
  /**
   * 낙사 전 마지막으로 밟은 블록 종류
   */
  LAST_TRAMPLED_BLOCK_TYPE,
}

package com.jho5245.cucumbery.util.storage.data;

import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;

import java.util.Calendar;

public enum Prefix
{
  // 기본

  BRACKET("[%s] "),
  INFO("&d정보", "&5"),
  INFO_TIP("TIP", "rg255,204;"),
  INFO_ERROR("&c오류", "&4"),
  INFO_WARN("rg255,204;경고", "&4"),
  INFO_DEBUG("디버그", "&e&l"),

  // 기타 오류
  NO_PERMISSION("권한이 부족합니다"),
  NO_PLAYER("플레이어를 찾을 수 없습니다"),
  NO_ENTITY("개체를 찾을 수 없습니다"),
  NO_ITEM("아이템을 찾을 수 없습니다"),
  NO_HOLDING_ITEM("주로 사용하는 손에 아이템을 들고 있지 않습니다"),
  NO_MATERIAL("알 수 없는 물질입니다"),
  NO_MATERIAL_ITEM("알 수 없는 아이템입니다"),
  NO_MATERIAL_BLOCK("알 수 없는 블록입니다"),
  NO_OBTAINABLE_MATERIAL("인벤토리에 넣을 수 없는 물질입니다"),
  NO_WORLD("알 수 없는 월드입니다"),
  NO_KEY("알 수 없는 키입니다"),
  NO_VALUE("알 수 없는 값입니다"),
  NO_GAMEMODE("알 수 없는 게임 모드입니다"),
  NO_FILE("파일을 찾을 수 없습니다"),
  NO_UUID("잘못된 UUID입니다"),
  ONLY_PLAYER("게임 내의 플레이어만 사용하거나 접근할 수 있습니다"),
  ONLY_CONSOLE("콘솔만 접근할 수 있습니다"),
  ARGS_SHORT("명령어에 인수가 너무 적습니다"),
  ARGS_LONG("명령어에 인수가 너무 많습니다"),
  ARGS_WRONG("명령어에 잘못된 인수가 있습니다"),
  ONLY_INTEGER("정수만 입력할 수 있습니다"),
  ONLY_NATURALNUMBER("자연수만 입력할 수 있습니다"),
  ONLY_NUMBER("숫자만 입력할 수 있습니다"),
  ONLY_PLUSNUMBER("양수만 입력할 수 있습니다"),

  INFO_TRASHCAN("rg255,204;쓰레기통", "&6"),
  INFO_HEAL("&a회복", "&2"),
  INFO_AIR("&a공기", "&2"),
  INFO_HP("&bHP", "&a"),
  INFO_FOOD("&6음식", "&a"),
  INFO_NICK("&b닉네임", "&b"),
  INFO_TELEPORT("&b텔레포트", "&d"),
  INFO_WARP("&b워프", "&d"),
  INFO_WORKBENCH("rg255,204;제작대", "&6"),
  INFO_ENDERCHEST("&5엔더상자", "&d"),
  INFO_WHOIS("&d누구쎄요", "&f"),
  INFO_WHATIS("&d뭐야여기", "&f"),
  INFO_LEVEL("&a레벨", "&2"),
  INFO_CLEARCHAT("rg255,204;청소채팅", "&c"),
  INFO_HANDGIVE("&b주거니받거니", "&2"),
  INFO_SIGN("rg255,204;표지판", "&5"),
  INFO_SOUND("&b소리", "&3"),
  INFO_ALLPLAYER("&a올플레이어", "&6"),
  INFO_EXP("&a경험치", "&2"),
  INFO_DAMAGE("&c대미지", "&7"),
  INFO_REINFORCE("&3강화", "&9"),
  INFO_MENU("&a메뉴", "&2"),
  INFO_SETDATA("&b데이터", "&d"),
  INFO_ITEMSTORAGE("rg255,204;아이템", "#52ee52;"),
  INFO_UUID("&aUUID", "&5"),
  INFO_SUDO("&a수뻐유저두", "&6"),
  INFO_CONDENSE("블록 압축", "&6"),
  INFO_YUNNORI("&a윷놀이", "&b"),
  INFO_HAT("모자", "&d"),
  INFO_SONG("&e노래", "&b"),
  INFO_HOWIS("&d뭐야이서버", "&f"),
  INFO_AUTO_UPDATER("&a업데이터", "rg255,204;"),
  INFO_CUSTOM_RECIPE("&a제작", "&2"),
  INFO_VIRTUAL_CHEST("rg255,204;가상창고", "&6"),
  INFO_CUSTOM_FIX("&b수리", "&2"),
  INFO_ECONOMY("#44cc00;돈", "#448026;"),
  INFO_ADVANCEMENT("rgb182,223,155;key.advancements", "rgb152,193,145;"),
  INFO_ADVANCEMENT_GOAL("rgb182,223,155;key.advancements", "rgb152,193,145;"),
  INFO_ADVANCEMENT_CHALLENGE("rgb182,223,155;key.advancements", "rgb152,193,145;"),
  INFO_SOCIAL("#52CEFF;소셜", "#5D7DC0;"),
  INFO_JOIN("&a입장", "&2"),
  INFO_QUIT("&c퇴장", "&5"),
  INFO_CUSTOM_MERCHANT("&b상인", "&9"),
  INFO_CUSTOM_EFFECT("&b효과", "&9"),
  INFO_STASH("rg255,204;스태시", "&9"),
  ;

  private final String text;

  private final String color;

  Prefix(String text)
  {
    this.text = text;
    this.color = "";
  }

  Prefix(String text, String color)
  {
    this.text = text;
    this.color = color;
  }

  public Component get()
  {
    return this.get(true);
  }

  public Component get(boolean n2s)
  {
    if (!this.color.equals(""))
    {
      return ComponentUtil.translate(color + BRACKET, n2s,
              ComponentUtil.translate(text)).hoverEvent(HoverEvent.showText(Component.text(Method.getCurrentTime(Calendar.getInstance(), true, false))));
    }
    return ComponentUtil.translate(text, n2s);
  }

  @Override
  public String toString()
  {
    return text;
  }
}

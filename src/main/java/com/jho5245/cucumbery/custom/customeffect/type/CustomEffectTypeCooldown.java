package com.jho5245.cucumbery.custom.customeffect.type;

public class CustomEffectTypeCooldown extends CustomEffectType
{
  public static final CustomEffectType
          COOLDOWN_CHAT = new CustomEffectType("cooldown_chat", "채팅 쿨타임", builder().negative().keepOnDeath().enumHidden()),
          COOLDOWN_ITEM_MEGAPHONE = new CustomEffectType("cooldown_item_megaphone", "아이템 확성기 쿨타임", builder().negative().keepOnDeath().enumHidden()),
          COOLDOWN_GUI_BUTTON = new CustomEffectType("cooldown_gui_button", "", builder().negative().hidden().defaultDuration(5).removeOnQuit()),
          COOLDOWN_ERROR_WARN_SOUND = new CustomEffectType("cooldown_error_warn_sound", "", builder().hidden().defaultDuration(4));

  protected static void registerEffect()
  {
    register(COOLDOWN_CHAT, COOLDOWN_ITEM_MEGAPHONE, COOLDOWN_GUI_BUTTON, COOLDOWN_ERROR_WARN_SOUND);
  }
}

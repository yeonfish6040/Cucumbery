package com.jho5245.cucumbery.util.storage.data;

public enum CustomGameRule
{
  SANS_PPAP("sansPPAP", "샌즈 ppap", false);

  ;
  private final String key;

  private final String display;

  private final Object defaultValue;

  CustomGameRule(String key, String display, Object defaultValue)
  {
    this.key = key;
    this.display = display;
    this.defaultValue = defaultValue;
  }

  public String getKey()
  {
    return key;
  }

  public String getDisplay()
  {
    return display;
  }

  public Object getDefaultValue()
  {
    return defaultValue;
  }
}

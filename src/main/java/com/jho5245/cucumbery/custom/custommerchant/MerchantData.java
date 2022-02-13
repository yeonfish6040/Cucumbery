package com.jho5245.cucumbery.custom.custommerchant;

import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class MerchantData
{
  public static HashMap<String, MerchantData> merchantDataHashMap = new HashMap<>();

  private final String id;

  private String display;

  private YamlConfiguration configuration;

  public MerchantData(@NotNull String id, @Nullable String display, @NotNull YamlConfiguration configuration)
  {
    this.id = id;
    this.display = display;
    this.configuration = configuration;
    merchantDataHashMap.put(id, this);
  }

  @NotNull
  public String getId()
  {
    return id;
  }

  @NotNull
  public String getDisplay()
  {
    if (display == null)
    {
      display = id;
    }
    return display;
  }

  public void setDisplay(@Nullable String display)
  {
    this.display = display;
  }

  public void setMerchantRecipes(@NotNull YamlConfiguration configuration)
  {
    this.configuration = configuration;
  }

  @NotNull
  public YamlConfiguration getConfiguration()
  {
    return configuration;
  }
}

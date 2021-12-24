package com.jho5245.cucumbery.customeffect;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.customeffect.CustomEffect.DisplayType;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectApplyEvent;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectPreApplyEvent;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.CustomConfig;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

@SuppressWarnings("unused")
public class CustomEffectManager
{
  public static final HashMap<UUID, List<CustomEffect>> effectMap = new HashMap<>();

  @NotNull
  public static List<CustomEffect> getEffects(@NotNull Entity entity)
  {
    UUID uuid = entity.getUniqueId();
    if (!effectMap.containsKey(uuid))
    {
      return Collections.emptyList();
    }
    return effectMap.get(uuid);
  }

  @NotNull
  public static List<CustomEffect> getEffects(@NotNull Entity entity, @NotNull DisplayType displayType)
  {
    List<CustomEffect> customEffects = new ArrayList<>(getEffects(entity));
    customEffects.removeIf(customEffect -> customEffect.getDisplayType() != displayType);
    return customEffects;
  }

  public static boolean addEffect(@NotNull Entity entity, @NotNull CustomEffect customEffect)
  {
    return addEffect(entity, customEffect, false);
  }

  public static boolean addEffect(@NotNull Entity entity, @NotNull CustomEffect effect, boolean force)
  {
    if (!force && hasEffect(entity, effect.getEffectType()))
    {
      CustomEffect customEffect = getEffect(entity, effect.getEffectType());
      int originDura = customEffect.getDuration(), newDura = effect.getDuration();
      int originAmpl = customEffect.getAmplifier(), newAmpl = effect.getAmplifier();
      if (!(newAmpl > originAmpl || (originAmpl == newAmpl && newDura > originDura)))
      {
        return false;
      }
    }
    effect = effect.copy();
    EntityCustomEffectPreApplyEvent preApplyEvent = new EntityCustomEffectPreApplyEvent(entity, effect);
    Cucumbery.getPlugin().getPluginManager().callEvent(preApplyEvent);
    if (preApplyEvent.isCancelled())
    {
      return false;
    }
    ArrayList<CustomEffect> customEffects = new ArrayList<>(getEffects(entity));
    @NotNull CustomEffect finalEffect = effect;
    customEffects.removeIf(e -> e.getEffectType() == finalEffect.getEffectType());
    customEffects.add(effect);
    effectMap.put(entity.getUniqueId(), customEffects);
    EntityCustomEffectApplyEvent applyEvent = new EntityCustomEffectApplyEvent(entity, effect);
    Cucumbery.getPlugin().getPluginManager().callEvent(applyEvent);
    return true;
  }

  public static void addEffects(@NotNull Entity entity, @NotNull List<CustomEffect> effects)
  {
    addEffects(entity, effects, false);
  }

  public static void addEffects(@NotNull Entity entity, @NotNull List<CustomEffect> effects, boolean force)
  {
    boolean success = true;
    for (CustomEffect customEffect : effects)
    {
      success = success && addEffect(entity, customEffect, force);
    }
  }

  public static boolean removeEffect(@NotNull Entity entity, @NotNull CustomEffectType effectType)
  {
    if (!hasEffect(entity, effectType))
    {
      return false;
    }
    List<CustomEffect> customEffects = new ArrayList<>(getEffects(entity));
    customEffects.removeIf(effect -> effect.getEffectType() == effectType);
    effectMap.put(entity.getUniqueId(), customEffects);
    return true;
  }

  public static boolean clearEffects(@NotNull Entity entity)
  {
    if (!hasEffects(entity))
    {
      return false;
    }
    effectMap.put(entity.getUniqueId(), Collections.emptyList());
    return true;
  }

  public static boolean hasEffect(@NotNull Entity entity, @NotNull CustomEffectType effectType)
  {
    if (!hasEffects(entity))
    {
      return false;
    }
    List<CustomEffect> customEffects = CustomEffectManager.getEffects(entity);
    for (CustomEffect customEffect : customEffects)
    {
      if (customEffect.getEffectType() == effectType)
      {
        return true;
      }
    }
    return false;
  }

  public static boolean hasEffect(@NotNull Entity entity, @NotNull DisplayType displayType)
  {
    for (CustomEffect customEffect : getEffects(entity))
    {
      if (customEffect.getDisplayType() == displayType)
      {
        return true;
      }
    }
    return false;
  }

  public static boolean hasEffects(@NotNull Entity entity)
  {
    return effectMap.containsKey(entity.getUniqueId()) && !getEffects(entity).isEmpty();
  }

  /**
   * 개체의 효과를 가져옵니다. <p>{@link CustomEffectManager#hasEffect(Entity, CustomEffectType)}가 true인지 먼저 확인해야 합니다.
   *
   * @param entity     효과를 가져올 개체
   * @param effectType 효과의 종류
   * @return 개체의 효과
   * @throws IllegalStateException 개체가 효과를 가지고 있지 않으면 발생
   */
  @NotNull
  public static CustomEffect getEffect(@NotNull Entity entity, @NotNull CustomEffectType effectType) throws IllegalStateException
  {
    Material material;
    if (!hasEffect(entity, effectType))
    {
      throw new IllegalStateException();
    }
    List<CustomEffect> customEffects = CustomEffectManager.getEffects(entity);
    for (CustomEffect customEffect : customEffects)
    {
      if (customEffect.getEffectType() == effectType)
      {
        return customEffect;
      }
    }
    throw new IllegalStateException();
  }

  public static void save()
  {
    //MessageUtil.broadcastDebug("keysize" + effectMap.size());
    for (UUID uuid : effectMap.keySet())
    {
      List<CustomEffect> customEffects = effectMap.get(uuid);
      //MessageUtil.broadcastDebug("trying to save:" + uuid + ", effect size:" + customEffects.size());
      OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
      boolean isPlayer = offlinePlayer.hasPlayedBefore();
      Entity entity = Bukkit.getEntity(uuid);
      if (customEffects.isEmpty() || (!isPlayer && entity == null))
      {
        File file = new File(Cucumbery.getPlugin().getDataFolder() + "/data/CustomEffects/" + (isPlayer ? "" : "non-players/") + uuid + ".yml");
        if (file.exists())
        {
          if (!file.delete())
          {
            MessageUtil.sendError(Bukkit.getConsoleSender(), "could not delete wjat");
          }
          // MessageUtil.broadcastDebug("&cdelete:" + uuid);
        }
        continue;
      }
      CustomConfig customConfig = CustomConfig.getCustomConfig("data/CustomEffects/" + (isPlayer ? "" : "non-players/") + uuid + ".yml");
      // MessageUtil.broadcastDebug("&asave:" + uuid + ", " + customEffects.size());
      YamlConfiguration config = customConfig.getConfig();
      if (entity != null)
      {
        config.set("info.type", entity.getType().toString());
        config.set("info.player-name", offlinePlayer.getName());
        if (!(entity instanceof Player))
        {
          Component component = entity.customName();
          config.set("info.entity-custom-name.name", component != null ? ComponentUtil.serialize(component) : null);
          config.set("info.entity-custom-name.json", component != null ? ComponentUtil.serializeAsJson(component) : null);

        }
        Location location = entity.getLocation();
        config.set("info.location.world", location.getWorld().getName());
        config.set("info.location.x", location.getX());
        config.set("info.location.y", location.getY());
        config.set("info.location.z", location.getZ());
        config.set("info.location.yaw", location.getYaw());
        config.set("info.location.pitch", location.getPitch());
      }
      config.set("effects", null);
      for (CustomEffect customEffect : customEffects)
      {
        CustomEffectType effectType = customEffect.getEffectType();
        int duration = customEffect.getDuration();
        int amplifier = customEffect.getAmplifier();
        int initDuration = customEffect.getInitDuration();
        int initAmplifier = customEffect.getInitAmplifier();
        config.set("effects." + effectType + ".init-duration", initDuration);
        config.set("effects." + effectType + ".init-amplifier", initAmplifier);
        config.set("effects." + effectType + ".duration", duration);
        config.set("effects." + effectType + ".amplifier", amplifier);
        config.set("effects." + effectType + ".display-type", customEffect.getDisplayType().toString());
      }
      customConfig.saveConfig();
    }
    effectMap.keySet().removeIf(uuid ->
    {
      Entity entity = Bukkit.getEntity(uuid);
      boolean isPlayer = Bukkit.getOfflinePlayer(uuid).hasPlayedBefore();
      return effectMap.get(uuid).isEmpty() || (!isPlayer && entity == null);
    });
  }

  public static void load(@NotNull UUID uuid, @NotNull YamlConfiguration config)
  {
    ConfigurationSection root = config.getConfigurationSection("effects");
    if (root != null)
    {
      List<CustomEffect> customEffects = new ArrayList<>();
      for (String typeString : root.getKeys(false))
      {
        try
        {
          CustomEffectType customEffectType = CustomEffectType.valueOf(typeString);
          DisplayType displayType;
          try
          {
            displayType = DisplayType.valueOf(root.getString(typeString + ".display-type"));
          }
          catch (Throwable ignored)
          {
            displayType = customEffectType.getDefaultDisplayType();
          }
          int duration = root.getInt(typeString + ".duration");
          int amplifier = root.getInt(typeString + ".amplifier");
          int initDuration = root.getInt(typeString + ".init-duration");
          int initAmplifier = root.getInt(typeString + ".init-amplifier");
          CustomEffect customEffect = new CustomEffect(customEffectType, initDuration, initAmplifier, displayType);
          customEffect.setDuration(duration);
          customEffect.setAmplifier(amplifier);
          customEffects.add(customEffect);
        }
        catch (Exception ignored)
        {

        }
      }
      effectMap.put(uuid, customEffects);
    }
  }

  @NotNull
  public static Component getDisplay(@NotNull List<CustomEffect> customEffects, boolean showDuration)
  {
    customEffects = new ArrayList<>(customEffects);
    customEffects.removeIf(e -> e.getEffectType().isHidden());
    StringBuilder key = new StringBuilder();
    List<Component> arguments = new ArrayList<>();
    for (CustomEffect customEffect : customEffects)
    {
      int duration = customEffect.getDuration();
      int amplifier = customEffect.getAmplifier();
      String key2 = showDuration ? (amplifier == 0 ? "%1$s%2$s" : "%1$s %3$s%2$s") : "%1$s";
      arguments.add(
              ComponentUtil.translate(key2, customEffect,
                      (duration != -1 && duration != customEffect.getInitDuration() - 1) ?
                              " (" + Method.timeFormatMilli(duration * 50L, duration < 200, 1, true) + ")" :
                              ""
                      , amplifier + 1)
      );
      key.append("%s, ");
    }
    key = new StringBuilder(key.substring(0, key.length() - 2));
    return ComponentUtil.translate(key.toString(), arguments);
  }

  @NotNull
  public static Component getVanillaDisplay(@NotNull Collection<PotionEffect> potionEffects, boolean showDuration)
  {
    StringBuilder key = new StringBuilder();
    List<Component> arguments = new ArrayList<>();
    for (PotionEffect potionEffect : potionEffects)
    {
      int duration = potionEffect.getDuration();
      int amplifier = potionEffect.getAmplifier();
      String key2 = showDuration ? (amplifier == 0 ? "%1$s%2$s" : "%1$s %3$s%2$s") : "%1$s";
      arguments.add(
              ComponentUtil.translate(key2,
                      potionEffect,
                      " (" + Method.timeFormatMilli(duration * 50L, duration < 200, 1, true) + ")"
                      , amplifier + 1)
      );
      key.append("%s, ");
    }
    key = new StringBuilder(key.substring(0, key.length() - 2));
    return ComponentUtil.translate(key.toString(), arguments);
  }

  @SuppressWarnings("all")
  public static boolean isVanillaNegative(@NotNull PotionEffectType potionEffectType)
  {
    if (potionEffectType.equals(PotionEffectType.SLOW))
    {
      return true;
    }
    if (potionEffectType.equals(PotionEffectType.CONFUSION))
    {
      return true;
    }
    if (potionEffectType.equals(PotionEffectType.SLOW_DIGGING))
    {
      return true;
    }
    if (potionEffectType.equals(PotionEffectType.HARM))
    {
      return true;
    }
    if (potionEffectType.equals(PotionEffectType.BLINDNESS))
    {
      return true;
    }
    if (potionEffectType.equals(PotionEffectType.HUNGER))
    {
      return true;
    }
    if (potionEffectType.equals(PotionEffectType.WEAKNESS))
    {
      return true;
    }
    if (potionEffectType.equals(PotionEffectType.POISON))
    {
      return true;
    }
    if (potionEffectType.equals(PotionEffectType.WITHER))
    {
      return true;
    }
    if (potionEffectType.equals(PotionEffectType.GLOWING))
    {
      return true;
    }
    if (potionEffectType.equals(PotionEffectType.LEVITATION))
    {
      return true;
    }
    if (potionEffectType.equals(PotionEffectType.UNLUCK))
    {
      return true;
    }
    if (potionEffectType.equals(PotionEffectType.BAD_OMEN))
    {
      return true;
    }
    return false;
  }
}

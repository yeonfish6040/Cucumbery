package com.jho5245.cucumbery.customeffect;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectApplyEvent;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.storage.CustomConfig;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
      effectMap.put(uuid, Collections.emptyList());
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
      if (customEffect != null && (customEffect.getAmplifier() > effect.getAmplifier() && customEffect.getDuration() > effect.getDuration()))
      {
        return false;
      }
    }
    effect = effect.copy();
    EntityCustomEffectApplyEvent event = new EntityCustomEffectApplyEvent(entity, effect);
    Cucumbery.getPlugin().getPluginManager().callEvent(event);
    if (event.isCancelled())
    {
      return false;
    }
    ArrayList<CustomEffect> customEffects = new ArrayList<>(getEffects(entity));
    @NotNull CustomEffect finalEffect = effect;
    customEffects.removeIf(e -> e.getEffectType() == finalEffect.getEffectType());
    customEffects.add(effect);
    effectMap.put(entity.getUniqueId(), customEffects);
    return true;
  }

  public static boolean addEffects(@NotNull Entity entity, @NotNull List<CustomEffect> effects)
  {
    return addEffects(entity, effects, false);
  }

  public static boolean addEffects(@NotNull Entity entity, @NotNull List<CustomEffect> effects, boolean force)
  {
    boolean success = true;
    for (CustomEffect customEffect : effects)
    {
      success = success && addEffect(entity, customEffect, force);
    }
    return success;
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

  @Nullable
  public static CustomEffect getEffect(@NotNull Entity entity, @NotNull CustomEffectType effectType)
  {
    List<CustomEffect> customEffects = CustomEffectManager.getEffects(entity);
    for (CustomEffect customEffect : customEffects)
    {
      if (customEffect.getEffectType() == effectType)
      {
        return customEffect;
      }
    }
    return null;
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
         //   MessageUtil.sendError(Bukkit.getConsoleSender(), "could not delete wjat");
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
        config.set("effects." + effectType + ".duration", duration);
        config.set("effects." + effectType + ".amplifier", amplifier);
      }
      customConfig.saveConfig();
    }
    effectMap.keySet().removeIf(uuid ->
    {
      Entity entity = Bukkit.getEntity(uuid);
      boolean isPlayer = Bukkit.getOfflinePlayer(uuid).hasPlayedBefore();
      boolean remove = effectMap.get(uuid).isEmpty() || (!isPlayer && entity == null);
      if (remove)
      {
      //  MessageUtil.broadcastDebug("remove from cache : " + uuid);
      }
      return remove;
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
          int duration = root.getInt(typeString + ".duration");
          int amplifier = root.getInt(typeString + ".amplifier");
          CustomEffect customEffect = new CustomEffect(customEffectType, duration, amplifier);
          customEffects.add(customEffect);
        }
        catch (Exception e)
        {
          MessageUtil.sendWarn(Bukkit.getConsoleSender(), Prefix.INFO_CUSTOM_EFFECT, ComponentUtil.createTranslate("invalid custom effect type: %s, for %s(%s)", typeString, uuid, uuid.toString()));
        }
      }
      effectMap.put(uuid, customEffects);
    }
  }
}

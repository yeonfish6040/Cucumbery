package com.jho5245.cucumbery.custom.customeffect;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect.DisplayType;
import com.jho5245.cucumbery.custom.customeffect.children.group.*;
import com.jho5245.cucumbery.events.entity.*;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectRemoveEvent.Reason;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.TranslatableKeyParser;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Damageable;
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
    return Collections.unmodifiableList(effectMap.get(uuid));
  }

  @NotNull
  public static List<CustomEffect> getEffects(@NotNull Entity entity, @NotNull DisplayType displayType)
  {
    List<CustomEffect> customEffects = new ArrayList<>(getEffects(entity));
    customEffects.removeIf(customEffect -> customEffect.getDisplayType() != displayType);
    return Collections.unmodifiableList(customEffects);
  }

  public static boolean addEffect(@NotNull Entity entity, @NotNull CustomEffectType effectType)
  {
    return addEffect(entity, new CustomEffect(effectType));
  }

  public static boolean addEffect(@NotNull Entity entity, @NotNull CustomEffect customEffect)
  {
    return addEffect(entity, customEffect, false);
  }

  @SuppressWarnings("all")
  public static boolean addEffect(@NotNull Entity entity, @NotNull CustomEffect effect, boolean force)
  {
    if (!force && hasEffect(entity, effect.getType()))
    {
      CustomEffect customEffect = getEffect(entity, effect.getType());
      int originDura = customEffect.getDuration(), newDura = effect.getDuration();
      int originAmpl = customEffect.getAmplifier(), newAmpl = effect.getAmplifier();
      if (!(originAmpl != newAmpl || newDura == -1 || (originDura != -1 && originDura < newDura)))
      {
        return false;
      }
    }
    effect = effect.copy();
    EntityCustomEffectPreApplyEvent preApplyEvent = new EntityCustomEffectPreApplyEvent(entity, effect);
    Cucumbery.getPlugin().getPluginManager().callEvent(preApplyEvent);
    if (preApplyEvent.isCancelled() && !force)
    {
      return false;
    }
    List<CustomEffect> customEffects = new ArrayList<>(getEffects(entity));
    @NotNull CustomEffect finalEffect = effect;
    customEffects.removeIf(customEffect ->
    {
      int originAmpl = customEffect.getAmplifier(), newAmpl = finalEffect.getAmplifier();
      int originDura = customEffect.getDuration(), newDura = finalEffect.getDuration();

      if (customEffect.getType() == finalEffect.getType() && (originAmpl == newAmpl) && (force || newDura == -1 || (originDura != -1 && originDura < newDura)))
      {
        if (entity instanceof Attributable attributable && customEffect instanceof AttributeCustomEffect attributeCustomEffect)
        {
          UUID uuid = attributeCustomEffect.getUniqueId();
          Attribute attribute = attributeCustomEffect.getAttribute();
          AttributeInstance attributeInstance = attributable.getAttribute(attribute);
          if (attributeInstance != null)
          {
            AttributeModifier attributeModifier = null;
            for (AttributeModifier modifier : attributeInstance.getModifiers())
            {
              if (modifier.getUniqueId().equals(uuid))
              {
                attributeModifier = modifier;
                break;
              }
            }
            if (attributeModifier != null)
            {
              attributeInstance.removeModifier(attributeModifier);
            }
          }
        }
        return true;
      }
      return false;
    });
    CustomEffectType effectType = effect.getType();
    int initDura = effect.getInitDuration(), initAmple = effect.getInitAmplifier();
    DisplayType displayType = effect.getDisplayType();
    switch (effectType)
    {
      case HEALTH_INCREASE -> {
        effect = new AttributeCustomEffectImple(effectType, initDura, initAmple, displayType, UUID.randomUUID(), Attribute.GENERIC_MAX_HEALTH, Operation.ADD_SCALAR, 0.1);
      }
      case NEWBIE_SHIELD -> {
        if (entity instanceof OfflinePlayer offlinePlayer)
        {
          effect = new OfflinePlayerCustomEffectImple(effectType, initDura, initAmple, displayType, offlinePlayer);
        }
      }
      case BREAD_KIMOCHI -> {
        effect = new AttributeCustomEffectImple(effectType, initDura, initAmple, displayType, UUID.randomUUID(), Attribute.GENERIC_MOVEMENT_SPEED, Operation.ADD_SCALAR, 0.1);
      }
      case TOWN_SHIELD -> {
        effect = new AttributeCustomEffectImple(effectType, initDura, initAmple, displayType, UUID.randomUUID(), Attribute.GENERIC_MOVEMENT_SPEED, Operation.ADD_SCALAR, 0.3);
      }
      case COMBAT_BOOSTER -> {
        effect = new AttributeCustomEffectImple(effectType, initDura, initAmple, displayType, UUID.randomUUID(), Attribute.GENERIC_ATTACK_SPEED, Operation.ADD_NUMBER, 0.5);
      }
    }
    if (effectType.isRealDuration())
    {
      effect = new RealDurationCustomEffectImple(effectType, initDura, initAmple, displayType, System.currentTimeMillis(), System.currentTimeMillis() + initDura * 50L);
    }
    EntityCustomEffectApplyEvent applyEvent = new EntityCustomEffectApplyEvent(entity, effect);
    Cucumbery.getPlugin().getPluginManager().callEvent(applyEvent);
    if (applyEvent.isCancelled())
    {
      return true;
    }
    customEffects.add(effect);
    effectMap.put(entity.getUniqueId(), customEffects);
    EntityCustomEffectPostApplyEvent postApplyEvent = new EntityCustomEffectPostApplyEvent(entity, effect);
    Cucumbery.getPlugin().getPluginManager().callEvent(postApplyEvent);
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
    return removeEffect(entity, effectType, Reason.PLUGIN);
  }

  public static boolean removeEffect(@NotNull Entity entity, @NotNull CustomEffectType effectType, @NotNull Reason reason)
  {
    if (!hasEffect(entity, effectType))
    {
      return false;
    }
    List<CustomEffect> customEffects = new ArrayList<>(getEffects(entity));
    List<CustomEffect> removed = new ArrayList<>();
    customEffects.removeIf(effect ->
    {
      if (effect.getType() == effectType)
      {
        EntityCustomEffectPreRemoveEvent event = new EntityCustomEffectPreRemoveEvent(entity, effect, reason);
        Cucumbery.getPlugin().getPluginManager().callEvent(event);
        if (event.isCancelled())
        {
          return false;
        }
        removed.add(effect);
        return true;
      }
      return false;
    });
    effectMap.put(entity.getUniqueId(), customEffects);
    if (!removed.isEmpty())
    {
      for (CustomEffect effect : removed)
      {
        EntityCustomEffectRemoveEvent event = new EntityCustomEffectRemoveEvent(entity, effect, reason);
        Cucumbery.getPlugin().getPluginManager().callEvent(event);
      }
    }
    return true;
  }

  public static void removeEffect(@NotNull Entity entity, @NotNull CustomEffectType effectType, int amplifier)
  {
    removeEffect(entity, effectType, amplifier, Reason.PLUGIN);
  }

  public static void removeEffect(@NotNull Entity entity, @NotNull CustomEffectType effectType, int amplifier, @NotNull Reason reason)
  {
    if (!hasEffect(entity, effectType))
    {
      return;
    }
    List<CustomEffect> customEffects = new ArrayList<>(getEffects(entity));
    List<CustomEffect> removed = new ArrayList<>();
    customEffects.removeIf(effect ->
    {
      if (effect.getType() == effectType && effect.getAmplifier() == amplifier)
      {
        EntityCustomEffectPreRemoveEvent event = new EntityCustomEffectPreRemoveEvent(entity, effect, reason);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
        {
          return false;
        }
        removed.add(effect);
        return true;
      }
      return false;
    });
    effectMap.put(entity.getUniqueId(), customEffects);
    if (!removed.isEmpty())
    {
      for (CustomEffect effect : removed)
      {
        EntityCustomEffectRemoveEvent event = new EntityCustomEffectRemoveEvent(entity, effect, reason);
        Bukkit.getPluginManager().callEvent(event);
      }
    }
  }

  public static boolean clearEffects(@NotNull Entity entity)
  {
    if (!hasEffects(entity))
    {
      return false;
    }
    List<CustomEffect> customEffects = new ArrayList<>(getEffects(entity));
    for (CustomEffect effect : customEffects)
    {
      EntityCustomEffectPreRemoveEvent event = new EntityCustomEffectPreRemoveEvent(entity, effect);
      Cucumbery.getPlugin().getPluginManager().callEvent(event);
    }
    effectMap.put(entity.getUniqueId(), Collections.emptyList());
    for (CustomEffect effect : customEffects)
    {
      EntityCustomEffectRemoveEvent event = new EntityCustomEffectRemoveEvent(entity, effect);
      Cucumbery.getPlugin().getPluginManager().callEvent(event);
    }
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
      if (customEffect.getType() == effectType)
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
    if (!hasEffect(entity, effectType))
    {
      throw new IllegalStateException();
    }
    List<CustomEffect> customEffects = CustomEffectManager.getEffects(entity);
    CustomEffect returnEffect = null;
    for (CustomEffect customEffect : customEffects)
    {
      if (customEffect.getType() == effectType)
      {
        if (returnEffect == null || returnEffect.getAmplifier() < customEffect.getAmplifier())
        {
          returnEffect = customEffect;
        }
      }
    }
    if (returnEffect != null)
    {
      return returnEffect;
    }
    throw new IllegalStateException();
  }

  public static void refreshAttributeEffects(@NotNull Entity entity)
  {
    if (entity instanceof Attributable attributable)
    {
      for (Attribute attribute : Attribute.values())
      {
        AttributeInstance attributeInstance = attributable.getAttribute(attribute);
        if (attributeInstance != null)
        {
          Collection<AttributeModifier> modifiers = new ArrayList<>(attributeInstance.getModifiers());
          for (AttributeModifier modifier : modifiers)
          {
            if (modifier.getName().startsWith("cucumbery"))
            {
              attributeInstance.removeModifier(modifier);
            }
          }
          // update max health
          if (attribute == Attribute.GENERIC_MAX_HEALTH && entity instanceof Damageable damageable)
          {
            double maxHealth = attributeInstance.getValue();
            if (damageable.getHealth() > maxHealth)
            {
              damageable.setHealth(maxHealth);
            }
          }
        }
      }
      List<CustomEffect> customEffects = getEffects(entity);
      for (CustomEffect customEffect : customEffects)
      {
        if (customEffect instanceof AttributeCustomEffect attributeCustomEffect)
        {
          UUID uuid = attributeCustomEffect.getUniqueId();
          Attribute attribute = attributeCustomEffect.getAttribute();
          AttributeInstance attributeInstance = attributable.getAttribute(attribute);
          if (attributeInstance != null)
          {
            attributeInstance.addModifier(new AttributeModifier(uuid, "cucumbery-" + customEffect.getType().translationKey(), (customEffect.getAmplifier() + 1) * attributeCustomEffect.getMultiplier(), attributeCustomEffect.getOperation()));
          }
        }
      }
    }
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
      for (int i = 0; i < customEffects.size(); i++)
      {
        CustomEffect customEffect = customEffects.get(i);
        CustomEffectType effectType = customEffect.getType();
        int duration = customEffect.getDuration();
        int amplifier = customEffect.getAmplifier();
        int initDuration = customEffect.getInitDuration();
        int initAmplifier = customEffect.getInitAmplifier();
        config.set("effects." + i + ".type", effectType.toString());
        config.set("effects." + i + ".init-duration", initDuration);
        config.set("effects." + i + ".init-amplifier", initAmplifier);
        config.set("effects." + i + ".duration", duration);
        config.set("effects." + i + ".amplifier", amplifier);
        config.set("effects." + i + ".display-type", customEffect.getDisplayType().toString());
        if (customEffect instanceof PlayerCustomEffect playerCustomEffect)
        {
          config.set("effects." + i + ".player", playerCustomEffect.getPlayer().getUniqueId().toString());
        }
        else if (customEffect instanceof OfflinePlayerCustomEffect offlinePlayerCustomEffect)
        {
          config.set("effects." + i + ".offline-player", offlinePlayerCustomEffect.getOfflinePlayer().getUniqueId().toString());
        }
        else if (customEffect instanceof UUIDCustomEffect uuidCustomEffect)
        {
          config.set("effects." + i + ".uuid", uuidCustomEffect.getUniqueId().toString());
          if (uuidCustomEffect instanceof AttributeCustomEffect attributeCustomEffect)
          {
            config.set("effects." + i + ".attribute", attributeCustomEffect.getAttribute().toString());
            config.set("effects." + i + ".operation", attributeCustomEffect.getOperation().toString());
            config.set("effects." + i + ".multiplier", attributeCustomEffect.getMultiplier());
          }
        }
        else if (customEffect instanceof RealDurationCustomEffect realDurationCustomEffect)
        {
          config.set("effects." + i + ".start-time", realDurationCustomEffect.getStartTimeInMillis());
          config.set("effects." + i + ".end-time", realDurationCustomEffect.getEndTimeInMillis());
        }
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
          CustomEffectType customEffectType;
          try
          {
            customEffectType = CustomEffectType.valueOf(root.getString(typeString + ".type"));
          }
          catch (Exception e)
          {
            MessageUtil.consoleSendMessage(Prefix.INFO_WARN, "유효하지 않은 효과입니다: %s", root.getString(typeString + ".type") + "");
            continue;
          }
          DisplayType displayType;
          try
          {
            displayType = DisplayType.valueOf(root.getString(typeString + ".display-type"));
          }
          catch (Throwable e)
          {
            displayType = customEffectType.getDefaultDisplayType();
          }
          int duration = root.getInt(typeString + ".duration");
          int amplifier = root.getInt(typeString + ".amplifier");
          int initDuration = root.getInt(typeString + ".init-duration");
          int initAmplifier = root.getInt(typeString + ".init-amplifier");
          CustomEffect customEffect = null;
          String playerUuidString = root.getString(typeString + ".player");
          if (playerUuidString != null && Method.isUUID(playerUuidString))
          {
            Player player = Bukkit.getPlayer(UUID.fromString(playerUuidString));
            if (player != null)
            {
              customEffect = new PlayerCustomEffectImple(customEffectType, initDuration, initAmplifier, displayType, player);
            }
          }
          String offlinePlayerUuidString = root.getString(typeString + ".offline-player");
          if (offlinePlayerUuidString != null && Method.isUUID(offlinePlayerUuidString))
          {
            customEffect = new OfflinePlayerCustomEffectImple(customEffectType, initDuration, initAmplifier, displayType, Bukkit.getOfflinePlayer(UUID.fromString(offlinePlayerUuidString)));
          }
          String uuidString = root.getString(typeString + ".uuid");
          if (uuidString != null && Method.isUUID(uuidString))
          {
            UUID uuidData = UUID.fromString(uuidString);
            String attribute = root.getString(typeString + ".attribute");
            String operation = root.getString(typeString + ".operation");
            double multiplier = root.getDouble(typeString + ".multiplier");
            if (attribute == null || operation == null)
            {
              customEffect = new UUIDCustomEffectImple(customEffectType, initDuration, initAmplifier, displayType, uuidData);
            }
            else
            {
              try
              {
                Attribute attr = Attribute.valueOf(attribute);
                Operation oper = Operation.valueOf(operation);
                customEffect = new AttributeCustomEffectImple(customEffectType, initDuration, initAmplifier, displayType, uuidData, attr, oper, multiplier);
              }
              catch (Exception e)
              {
                e.printStackTrace();
              }
            }
          }
          if (customEffect == null)
          {
            customEffect = new CustomEffect(customEffectType, initDuration, initAmplifier, displayType);
          }
          if (root.isLong(typeString + ".start-time") && root.isLong(typeString + ".end-time"))
          {
            long startTime = root.getLong(typeString + ".start-time"), endTime = root.getLong(typeString + ".end-time");
            customEffect = new RealDurationCustomEffectImple(customEffectType, initDuration, initAmplifier, displayType, startTime, endTime);
          }
          customEffect.setDuration(duration);
          customEffect.setAmplifier(amplifier);
          customEffects.add(customEffect);
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
      effectMap.put(uuid, customEffects);
    }
  }

  @NotNull
  public static Component getDisplay(@NotNull List<CustomEffect> customEffects)
  {
    return getDisplay(customEffects, true);
  }

  @NotNull
  public static Component getDisplay(@NotNull List<CustomEffect> customEffects, boolean showDuration)
  {
    StringBuilder key = new StringBuilder();
    List<Component> arguments = new ArrayList<>();
    for (CustomEffect customEffect : customEffects)
    {
      int duration = customEffect.getDuration();
      int initDuration = customEffect.getInitDuration();
      int amplifier = customEffect.getAmplifier();
      boolean isInfinite = duration == -1, less10Sec = duration > 0 && duration < 200, less1Min = duration > 0 && duration < 1200;
      boolean ampleZero = amplifier == 0;
      int remain = 255 - (duration % 20 * 10 + 56);
      String timePrefixColor = initDuration > 200 && (initDuration > 20 * 60 || duration * 1d / initDuration <= 0.2) && less10Sec ?
              (customEffect.getType().isNegative() ?
                      ("rgb" + remain + ",255," + remain + ";") :
                      ("rgb255," + remain + "," + remain + ";")
              )
              : "";
      // showDuration = 효과 개수 < 10

      // 지속 시간이 1분 이하거나 효과 10개 미만이며, 농도 레벨이 0
      // 농도 레벨이 0이 아님
      String key2;
      if (isInfinite) // 지속 시간 무제한
      {
        if (ampleZero)
        {
          key2 = "%1$s";
        }
        else
        {
          key2 = "%1$s %3$s";
        }
      }
      else if (!less1Min) // 지속 시간 1분 이상
      {
        if (showDuration)
        {
          if (ampleZero)
          {
            key2 = "%1$s%2$s";
          }
          else
          {
            key2 = "%1$s %3$s%2$s";
          }
        }
        else
        {
          if (ampleZero)
          {
            key2 = "%1$s+";
          }
          else
          {
            key2 = "%1$s+%3$s";
          }
        }
      }
      else // 지속 시간 1분 미만
      {
        if (ampleZero)
        {
          key2 = "%1$s%2$s";
        }
        else
        {
          key2 = "%1$s %3$s%2$s";
        }
      }
      CustomEffectType effectType = customEffect.getType();
      Component effectComponent = ComponentUtil.translate((effectType.isNegative() ? "&c" : "&a") + (showDuration ? effectType.translationKey() : effectType.shortTranslationKey()));
      Component create = ComponentUtil.create(customEffect);
      effectComponent = effectComponent.hoverEvent(create.hoverEvent()).clickEvent(create.clickEvent());
      arguments.add(
              ComponentUtil.translate(key2, effectComponent,
                      !customEffect.isTimeHidden() ?
                              timePrefixColor + (showDuration ? " (" : "(") + Method.timeFormatMilli(duration * 50L, less10Sec, 1, true) + ")" :
                              ""
                      , "rgb200,200,30;" + (amplifier + 1))
      );
      key.append("%s,");
      if (showDuration)
      {
        key.append(" ");
      }
    }
    key = new StringBuilder(key.substring(0, key.length() - (showDuration ? 2 : 1)));
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
      boolean isInfinite = duration > 20 * 60 * 60 * 24 * 365, less10Sec = duration > 0 && duration < 200, less1Min = duration > 0 && duration < 1200;
      boolean ampleZero = amplifier == 0;
      int remain = 255 - (duration % 20 * 10 + 56);
      String timePrefixColor = CustomEffectManager.isVanillaNegative(potionEffect.getType()) ?
              (less10Sec ? "rgb" + remain + ",255," + remain + ";" : "") :
              (less10Sec ? "rgb255," + remain + "," + remain + ";" : "");
      // showDuration = 효과 개수 < 10

      // 지속 시간이 1분 이하거나 효과 10개 미만이며, 농도 레벨이 0
      // 농도 레벨이 0이 아님
      String key2;
      if (isInfinite) // 지속 시간 무제한
      {
        if (ampleZero)
        {
          key2 = "%1$s";
        }
        else
        {
          key2 = "%1$s %3$s";
        }
      }
      else if (!less1Min) // 지속 시간 1분 이상
      {
        if (showDuration)
        {
          if (ampleZero)
          {
            key2 = "%1$s%2$s";
          }
          else
          {
            key2 = "%1$s %3$s%2$s";
          }
        }
        else
        {
          if (ampleZero)
          {
            key2 = "%1$s+";
          }
          else
          {
            key2 = "%1$s+%3$s";
          }
        }
      }
      else // 지속 시간 1분 미만
      {
        if (ampleZero)
        {
          key2 = "%1$s%2$s";
        }
        else
        {
          key2 = "%1$s %3$s%2$s";
        }
      }
      PotionEffectType effectType = potionEffect.getType();
      Component effectComponent = ComponentUtil.translate((isVanillaNegative(effectType) ? "&c" : "&a") + (showDuration ? TranslatableKeyParser.getKey(effectType) : getVanillaShortTranslationKey(effectType)));
      Component create = ComponentUtil.create(potionEffect);
      effectComponent = effectComponent.hoverEvent(create.hoverEvent()).clickEvent(create.clickEvent());
      arguments.add(
              ComponentUtil.translate(key2,
                      effectComponent,
                      timePrefixColor + (showDuration ? " (" : "(") + Method.timeFormatMilli(duration * 50L, duration < 200, 1, true) + ")"
                      , "rgb200,200,30;" + (amplifier + 1))
      );
      key.append("%s,");
      if (showDuration)
      {
        key.append(" ");
      }
    }
    key = new StringBuilder(key.substring(0, key.length() - (showDuration ? 2 : 1)));
    return ComponentUtil.translate(key.toString(), arguments);
  }

  @NotNull
  public static String getVanillaShortTranslationKey(@NotNull PotionEffectType potionEffectType)
  {
    if (potionEffectType.equals(PotionEffectType.SPEED))
    {
      return "속증";
    }
    if (potionEffectType.equals(PotionEffectType.SLOW))
    {
      return "속감";
    }
    if (potionEffectType.equals(PotionEffectType.FAST_DIGGING))
    {
      return "성급";
    }
    if (potionEffectType.equals(PotionEffectType.SLOW_DIGGING))
    {
      return "채피";
    }
    if (potionEffectType.equals(PotionEffectType.WEAKNESS))
    {
      return "나약";
    }
    if (potionEffectType.equals(PotionEffectType.HARM))
    {
      return "즉피";
    }
    if (potionEffectType.equals(PotionEffectType.HEAL))
    {
      return "즉치";
    }
    if (potionEffectType.equals(PotionEffectType.JUMP))
    {
      return "점강";
    }
    if (potionEffectType.equals(PotionEffectType.FIRE_RESISTANCE))
    {
      return "화저";
    }
    if (potionEffectType.equals(PotionEffectType.WATER_BREATHING))
    {
      return "수호";
    }
    if (potionEffectType.equals(PotionEffectType.NIGHT_VISION))
    {
      return "야투";
    }
    if (potionEffectType.equals(PotionEffectType.HEALTH_BOOST))
    {
      return "생강";
    }
    if (potionEffectType.equals(PotionEffectType.LEVITATION))
    {
      return "공부";
    }
    if (potionEffectType.equals(PotionEffectType.SLOW_FALLING))
    {
      return "느낙";
    }
    if (potionEffectType.equals(PotionEffectType.CONDUIT_POWER))
    {
      return "전힘";
    }
    if (potionEffectType.equals(PotionEffectType.DOLPHINS_GRACE))
    {
      return "돌우";
    }
    if (potionEffectType.equals(PotionEffectType.HERO_OF_THE_VILLAGE))
    {
      return "마영";
    }
    return TranslatableKeyParser.getKey(potionEffectType);
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
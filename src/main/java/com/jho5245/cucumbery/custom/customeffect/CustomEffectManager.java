package com.jho5245.cucumbery.custom.customeffect;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect.DisplayType;
import com.jho5245.cucumbery.custom.customeffect.children.group.*;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeCustomMining;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectAbstractApplyEvent.ApplyReason;
import com.jho5245.cucumbery.events.entity.*;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectRemoveEvent.RemoveReason;
import com.jho5245.cucumbery.util.no_groups.ItemSerializer;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.Method2;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil.TimeFormatType;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.TranslatableKeyParser;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTEntity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
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

  public static boolean addEffect(@NotNull Entity entity, @NotNull CustomEffectType effectType, int duration)
  {
    return addEffect(entity, new CustomEffect(effectType, duration));
  }

  public static boolean addEffect(@NotNull Entity entity, @NotNull CustomEffectType effectType, int duration, int amplifier)
  {
    return addEffect(entity, new CustomEffect(effectType, duration, amplifier));
  }

  public static boolean addEffect(@NotNull Entity entity, @NotNull CustomEffect effect)
  {
    return addEffect(entity, effect, false);
  }

  public static boolean addEffect(@NotNull Entity entity, @NotNull CustomEffect effect, boolean force)
  {
    return addEffect(entity, effect, ApplyReason.PLUGIN, force, true);
  }

  public static boolean addEffect(@NotNull Entity entity, @NotNull CustomEffect effect, @NotNull ApplyReason reason)
  {
    return addEffect(entity, effect, reason, false, true);
  }

  @SuppressWarnings("all")
  public static boolean addEffect(@NotNull Entity entity, @NotNull CustomEffect effect, @NotNull ApplyReason reason, boolean force)
  {
    return addEffect(entity, effect, reason, force, true);
  }

  @SuppressWarnings("all")
  public static boolean addEffect(@NotNull Entity entity, @NotNull CustomEffect effect, @NotNull ApplyReason reason, boolean force, boolean callEvent)
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
    if (callEvent && effect.getType().doesCallEvent())
    {
      EntityCustomEffectPreApplyEvent preApplyEvent = new EntityCustomEffectPreApplyEvent(entity, effect, reason);
      Cucumbery.getPlugin().getPluginManager().callEvent(preApplyEvent);
      if (preApplyEvent.isCancelled() && !force)
      {
        return false;
      }
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
    switch (effectType.getIdString().toUpperCase())
    {
      case "HEALTH_INCREASE" ->
      {
        effect = new AttributeCustomEffectImple(effectType, initDura, initAmple, displayType, UUID.randomUUID(), Attribute.GENERIC_MAX_HEALTH, Operation.ADD_SCALAR, 0.1);
      }
      case "NEWBIE_SHIELD" ->
      {
        if (entity instanceof OfflinePlayer offlinePlayer)
        {
          effect = new OfflinePlayerCustomEffectImple(effectType, initDura, initAmple, displayType, offlinePlayer);
        }
      }
      case "BREAD_KIMOCHI" ->
      {
        effect = new AttributeCustomEffectImple(effectType, initDura, initAmple, displayType, UUID.randomUUID(), Attribute.GENERIC_MOVEMENT_SPEED, Operation.ADD_SCALAR, 0.1);
      }
      case "TOWN_SHIELD" ->
      {
        effect = new AttributeCustomEffectImple(effectType, initDura, initAmple, displayType, UUID.randomUUID(), Attribute.GENERIC_MOVEMENT_SPEED, Operation.ADD_SCALAR, 0.3);
      }
      case "COMBAT_BOOSTER" ->
      {
        effect = new AttributeCustomEffectImple(effectType, initDura, initAmple, displayType, UUID.randomUUID(), Attribute.GENERIC_ATTACK_SPEED, Operation.ADD_SCALAR, 0.25);
      }
      case "POSITION_MEMORIZE" ->
      {
        effect = new LocationCustomEffectImple(effectType, initDura, initAmple, displayType, entity.getLocation());
      }
      case "DORMAMMU" ->
      {
        effect = new LocationVelocityCustomEffectImple(effectType, initDura, initAmple, displayType, entity.getLocation(), entity.getVelocity());
      }
    }
    if (effectType == CustomEffectType.STRANGE_CREATIVE_MODE && entity instanceof Player player)
    {
      NBTEntity nbtEntity = new NBTEntity(player);
      NBTCompound nbtCompound = nbtEntity.getCompound("abilities");
      String data = player.getGameMode() + "|" + nbtCompound.getBoolean("instabuild") + "|" + nbtCompound.getBoolean("invulnerable") + "|" + player.getAllowFlight();
      effect = new StringCustomEffectImple(effectType, initDura, initAmple, displayType, data);
      player.setGameMode(GameMode.CREATIVE);
      nbtEntity.mergeCompound(new NBTContainer("{abilities:{instabuild:0b,invulnerable:0b}}"));
      player.setAllowFlight(false);
    }
    if (effectType.isRealDuration() && effect.getDuration() != -1)
    {
      effect = new RealDurationCustomEffectImple(effectType, initDura, initAmple, displayType, System.currentTimeMillis(), System.currentTimeMillis() + initDura * 50L);
    }
    if (callEvent && effect.getType().doesCallEvent())
    {
      EntityCustomEffectApplyEvent applyEvent = new EntityCustomEffectApplyEvent(entity, effect, reason);
      Cucumbery.getPlugin().getPluginManager().callEvent(applyEvent);
      if (applyEvent.isCancelled())
      {
        return true;
      }
    }
    customEffects.add(effect);
    effectMap.put(entity.getUniqueId(), customEffects);
    if (callEvent && effect.getType().doesCallEvent())
    {
      EntityCustomEffectPostApplyEvent postApplyEvent = new EntityCustomEffectPostApplyEvent(entity, effect, reason);
      Cucumbery.getPlugin().getPluginManager().callEvent(postApplyEvent);
    }
    return true;
  }

  public static boolean addEffects(@NotNull Entity entity, @NotNull List<CustomEffect> effects, @NotNull ApplyReason reason, boolean force, boolean callEvent)
  {
    boolean success = false;
    for (CustomEffect customEffect : effects)
    {
      success = addEffect(entity, customEffect, reason, force, callEvent) || success;
    }
    return success;
  }

  public static boolean removeEffect(@NotNull Entity entity, @NotNull CustomEffectType effectType)
  {
    return removeEffect(entity, effectType, RemoveReason.PLUGIN);
  }

  public static boolean removeEffect(@NotNull Entity entity, @NotNull CustomEffectType effectType, @NotNull EntityCustomEffectRemoveEvent.RemoveReason reason)
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
        if (effect.getType().doesCallEvent())
        {
          EntityCustomEffectPreRemoveEvent event = new EntityCustomEffectPreRemoveEvent(entity, effect, reason);
          try
          {
            Cucumbery.getPlugin().getPluginManager().callEvent(event);
          }
          catch (Throwable e)
          {
            MessageUtil.consoleSendMessage("removeEffect 비동기 호출됨 1");
Cucumbery.getPlugin().getLogger().warning(            e.getMessage());
          }
          if (event.isCancelled())
          {
            return false;
          }
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
        if (effect.getType().doesCallEvent())
        {
          EntityCustomEffectRemoveEvent event = new EntityCustomEffectRemoveEvent(entity, effect, reason);
          try
          {
            Cucumbery.getPlugin().getPluginManager().callEvent(event);
          }
          catch (Throwable e)
          {
            MessageUtil.consoleSendMessage("removeEffect 비동기 호출됨 2");
Cucumbery.getPlugin().getLogger().warning(            e.getMessage());
          }
        }
      }
    }
    return true;
  }

  public static void removeEffect(@NotNull Entity entity, @NotNull CustomEffectType effectType, int amplifier)
  {
    removeEffect(entity, effectType, amplifier, RemoveReason.PLUGIN);
  }

  public static void removeEffect(@NotNull Entity entity, @NotNull CustomEffectType effectType, int amplifier, @NotNull EntityCustomEffectRemoveEvent.RemoveReason reason)
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
        if (effect.getType().doesCallEvent())
        {
          EntityCustomEffectPreRemoveEvent event = new EntityCustomEffectPreRemoveEvent(entity, effect, reason);
          try
          {
            Bukkit.getPluginManager().callEvent(event);
          }
          catch (Throwable e)
          {
            MessageUtil.consoleSendMessage("removeEffect 비동기 호출됨 3");
Cucumbery.getPlugin().getLogger().warning(            e.getMessage());
          }
          if (event.isCancelled())
          {
            return false;
          }
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
        if (effect.getType().doesCallEvent())
        {
          EntityCustomEffectRemoveEvent event = new EntityCustomEffectRemoveEvent(entity, effect, reason);
          try
          {
            Bukkit.getPluginManager().callEvent(event);
          }
          catch (Throwable e)
          {
            MessageUtil.consoleSendMessage("removeEffect 비동기 호출됨 4");
Cucumbery.getPlugin().getLogger().warning(            e.getMessage());
          }
        }
      }
    }
  }

  public static boolean clearEffects(@NotNull Entity entity)
  {
    return clearEffects(entity, true);
  }

  public static boolean clearEffects(@NotNull Entity entity, boolean callEvent)
  {
    if (!hasEffects(entity))
    {
      return false;
    }
    List<CustomEffect> customEffects = new ArrayList<>(getEffects(entity));
    if (callEvent)
    {
      for (CustomEffect effect : customEffects)
      {
        if (effect.getType().doesCallEvent())
        {
          EntityCustomEffectPreRemoveEvent event = new EntityCustomEffectPreRemoveEvent(entity, effect);
          Cucumbery.getPlugin().getPluginManager().callEvent(event);
        }
      }
    }
    effectMap.put(entity.getUniqueId(), Collections.emptyList());
    if (callEvent)
    {
      for (CustomEffect effect : customEffects)
      {
        if (effect.getType().doesCallEvent())
        {
          EntityCustomEffectRemoveEvent event = new EntityCustomEffectRemoveEvent(entity, effect);
          Cucumbery.getPlugin().getPluginManager().callEvent(event);
        }
      }
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

  /**
   * 개체의 효과를 가져옵니다.
   *
   * @param entity     효과를 가져올 개체
   * @param effectType 효과의 종류
   * @return 개체의 효과 또는 없을 경우 null
   */
  @Nullable
  public static CustomEffect getEffectNullable(@NotNull Entity entity, @NotNull CustomEffectType effectType)
  {
    if (CustomEffectManager.hasEffect(entity, effectType))
    {
      return CustomEffectManager.getEffect(entity, effectType);
    }
    return null;
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

  public static void saveAll()
  {
    //MessageUtil.broadcastDebug("keysize" + effectMap.size());
    for (UUID uuid : effectMap.keySet())
    {
      save(uuid);
    }
    effectMap.keySet().removeIf(uuid ->
    {
      Entity entity = Method2.getEntityAsync(uuid);
      OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
      boolean isPlayer = offlinePlayer.hasPlayedBefore() || offlinePlayer.getStatistic(Statistic.PLAY_ONE_MINUTE) > 0;
      return effectMap.get(uuid).isEmpty() || (!isPlayer && entity == null);
    });
  }

  public static void save(@NotNull UUID uuid)
  {
    List<CustomEffect> customEffects = effectMap.get(uuid);
    //MessageUtil.broadcastDebug("trying to save:" + uuid + ", effect size:" + customEffects.size());
    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
    boolean isPlayer = offlinePlayer.hasPlayedBefore() || offlinePlayer.getStatistic(Statistic.PLAY_ONE_MINUTE) > 0;
    Entity entity = Method2.getEntityAsync(uuid);
    String entityType = entity != null ? entity.getType().toString().toLowerCase() : "unknown";
    if (customEffects.isEmpty() || (!isPlayer && entity == null))
    {
      File file = new File(Cucumbery.getPlugin().getDataFolder() + "/data/CustomEffects/" + entityType + "/" + uuid + ".yml");
      if (file.exists())
      {
        if (!file.delete())
        {
          MessageUtil.sendError(Bukkit.getConsoleSender(), "could not delete wjat");
        }
        // MessageUtil.broadcastDebug("&cdelete:" + uuid);
      }
      return;
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
      config.set("effects." + i + ".type", effectType.getNamespacedKey().toString());
      config.set("effects." + i + ".init-duration", initDuration);
      config.set("effects." + i + ".init-amplifier", initAmplifier);
      config.set("effects." + i + ".duration", duration);
      config.set("effects." + i + ".amplifier", amplifier);
      config.set("effects." + i + ".display-type", customEffect.getDisplayType().toString());
      if (customEffect instanceof PlayerCustomEffect playerCustomEffect)
      {
        config.set("effects." + i + ".player", playerCustomEffect.getPlayer().getUniqueId().toString());
      }
      if (customEffect instanceof EntityCustomEffect entityCustomEffect)
      {
        config.set("effects." + i + ".entity", entityCustomEffect.getEntity().getUniqueId().toString());
      }
      if (customEffect instanceof OfflinePlayerCustomEffect offlinePlayerCustomEffect)
      {
        config.set("effects." + i + ".offline-player", offlinePlayerCustomEffect.getOfflinePlayer().getUniqueId().toString());
      }
      if (customEffect instanceof UUIDCustomEffect uuidCustomEffect)
      {
        config.set("effects." + i + ".uuid", uuidCustomEffect.getUniqueId().toString());
        if (uuidCustomEffect instanceof AttributeCustomEffect attributeCustomEffect)
        {
          config.set("effects." + i + ".attribute", attributeCustomEffect.getAttribute().toString());
          config.set("effects." + i + ".operation", attributeCustomEffect.getOperation().toString());
          config.set("effects." + i + ".multiplier", attributeCustomEffect.getMultiplier());
        }
      }
      if (customEffect instanceof RealDurationCustomEffect realDurationCustomEffect)
      {
        config.set("effects." + i + ".start-time", realDurationCustomEffect.getStartTimeInMillis());
        config.set("effects." + i + ".end-time", realDurationCustomEffect.getEndTimeInMillis());
      }
      if (customEffect instanceof ItemStackCustomEffect itemStackCustomEffect)
      {
        config.set("effects." + i + ".item", ItemSerializer.serialize(itemStackCustomEffect.getItemStack()));
      }
      if (customEffect instanceof LocationCustomEffect locationCustomEffect)
      {
        Location location = locationCustomEffect.getLocation();
        config.set("effects." + i + ".location.world", location.getWorld().getName());
        config.set("effects." + i + ".location.x", location.getX());
        config.set("effects." + i + ".location.y", location.getY());
        config.set("effects." + i + ".location.z", location.getZ());
        config.set("effects." + i + ".location.yaw", location.getYaw());
        config.set("effects." + i + ".location.pitch", location.getPitch());
      }
      if (customEffect instanceof VelocityCustomEffect velocityCustomEffect)
      {
        Vector vector = velocityCustomEffect.getVelocity();
        config.set("effects." + i + ".velocity.x", vector.getX());
        config.set("effects." + i + ".velocity.y", vector.getY());
        config.set("effects." + i + ".velocity.z", vector.getZ());
      }
      if (customEffect instanceof StringCustomEffect stringCustomEffect)
      {
        config.set("effects." + i + ".string", stringCustomEffect.getString());
      }
    }
    customConfig.saveConfig();
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
          String s = root.getString(typeString + ".type", "");
          try
          {
            s = s.toLowerCase();
            if (!s.contains(":"))
            {
              s = "cucumbery:" + s;
            }
            customEffectType = CustomEffectType.valueOf(s);
          }
          catch (Exception e)
          {
            MessageUtil.consoleSendMessage(Prefix.INFO_WARN, "유효하지 않은 효과입니다: %s", s);
            MessageUtil.consoleSendMessage(Prefix.INFO, "다음 파일 참조: %s", Cucumbery.getPlugin().getDataFolder() + "/data/CustomEffects/" + (Bukkit.getOfflinePlayer(uuid).hasPlayedBefore() ? "" : "non-players/") + uuid + ".yml");
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
          String entityUuidString = root.getString(typeString + ".entity");
          if (entityUuidString != null && Method.isUUID(entityUuidString))
          {
            Entity entity = Bukkit.getEntity(UUID.fromString(entityUuidString));
            if (entity != null)
            {
              customEffect = new EntityCustomEffectImple(customEffectType, initDuration, initAmplifier, displayType, entity);
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
Cucumbery.getPlugin().getLogger().warning(                e.getMessage());
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
          String itemStackString = root.getString(typeString + ".item");
          if (itemStackString != null)
          {
            ItemStack itemStack = ItemSerializer.deserialize(itemStackString);
            customEffect = new ItemStackCustomEffectImple(customEffectType, initDuration, initAmplifier, displayType, itemStack);
          }
          String worldName = root.getString(typeString + ".location.world");
          ConfigurationSection velocities = root.getConfigurationSection(typeString + ".velocity");
          if (worldName != null)
          {
            World world = Bukkit.getWorld(worldName);
            if (world != null)
            {
              double x = root.getDouble(typeString + ".location.x");
              double y = root.getDouble(typeString + ".location.y");
              double z = root.getDouble(typeString + ".location.z");
              float yaw = (float) root.getDouble(typeString + ".location.yaw");
              float pitch = (float) root.getDouble(typeString + ".location.pitch");
              customEffect = new LocationCustomEffectImple(customEffectType, initDuration, initAmplifier, displayType,
                      new Location(world, x, y, z, yaw, pitch));
            }
          }
          if (velocities != null)
          {
            double x = root.getDouble(typeString + ".velocity.x");
            double y = root.getDouble(typeString + ".velocity.y");
            double z = root.getDouble(typeString + ".velocity.z");
            if (customEffect instanceof LocationCustomEffect locationCustomEffect)
            {
              customEffect = new LocationVelocityCustomEffectImple(customEffectType, initDuration, initAmplifier, displayType, locationCustomEffect.getLocation(), new Vector(x, y, z));
            }
            else
            {
              customEffect = new VelocityCustomEffectImple(customEffectType, initDuration, initAmplifier, displayType, new Vector(x, y, z));
            }
          }
          String stringValue = root.getString(typeString + ".string");
          if (stringValue != null)
          {
            customEffect = new StringCustomEffectImple(customEffectType, initDuration, initAmplifier, displayType, stringValue);
          }
          customEffect.setDuration(duration);
          customEffect.setAmplifier(amplifier);
          customEffects.add(customEffect);
        }
        catch (Exception e)
        {
Cucumbery.getPlugin().getLogger().warning(          e.getMessage());
        }
      }
      effectMap.put(uuid, customEffects);
    }
  }

  /**
   * 포션 효과가 표기되는것 일부 숨김(Wrapper 효과 등)
   *
   * @param player        해당 플레이어
   * @param potionEffects 해당 플레이어의 효과 목록
   * @return 숨겨진 효과를 제외한 효과들
   */
  @NotNull
  public static List<PotionEffect> removeDisplay(@NotNull Player player, @NotNull Collection<PotionEffect> potionEffects)
  {
    potionEffects = new ArrayList<>(potionEffects);
    // 커스텀 채광 효과
    potionEffects.removeIf(potionEffect -> potionEffect.getDuration() < 3 && (potionEffect.getType().equals(PotionEffectType.SLOW_DIGGING) || potionEffect.getType().equals(PotionEffectType.FAST_DIGGING)));
    potionEffects.removeIf(potionEffect ->
            (CustomEffectManager.hasEffect(player, CustomEffectType.FANCY_SPOTLIGHT) || CustomEffectManager.hasEffect(player, CustomEffectType.FANCY_SPOTLIGHT_ACTIVATED)) &&
                    potionEffect.getType().equals(PotionEffectType.REGENERATION));
    List<PotionEffectType> removeList = new ArrayList<>();
    for (CustomEffect customEffect : CustomEffectManager.getEffects(player))
    {
      CustomEffectType customEffectType = customEffect.getType();
      if (customEffectType.getNamespacedKey().getNamespace().equals("minecraft"))
      {
        PotionEffectType potionEffectType = PotionEffectType.getByName(customEffectType.getNamespacedKey().getKey());
        if (potionEffectType == null)
        {
          throw new NullPointerException("Invalid Potion Effect Type: " + customEffectType.getIdString());
        }
        removeList.add(potionEffectType);
      }
    }
    potionEffects.removeIf(potionEffect -> removeList.contains(potionEffect.getType()));
    return new ArrayList<>(potionEffects);
  }

  @NotNull
  public static Component getDisplay(@NotNull Entity entity, @NotNull List<CustomEffect> customEffects)
  {
    return getDisplay(entity, customEffects, true);
  }

  @NotNull
  public static Component getDisplay(@NotNull Entity entity, @NotNull List<CustomEffect> customEffects, boolean showDuration)
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
      TextColor textColor = effectType.isNegative() ? NamedTextColor.RED : NamedTextColor.GREEN;
      if (effectType == CustomEffectTypeCustomMining.AQUA_AFFINITY || effectType == CustomEffectTypeCustomMining.AIR_SCAFFOLDING || effectType == CustomEffectTypeCustomMining.HASTE || effectType == CustomEffectTypeCustomMining.MINING_FATIGUE ||
              effectType == CustomEffectTypeCustomMining.TITANIUM_FINDER || effectType == CustomEffectTypeCustomMining.MINING_FORTUNE || effectType == CustomEffectTypeCustomMining.MOLE_CLAW || effectType == CustomEffectTypeCustomMining.MINDAS_TOUCH ||
              effectType == CustomEffectTypeCustomMining.MINING_BOOSTER)
      {
        if (!CustomEffectManager.hasEffect(entity, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
        {
          textColor = NamedTextColor.GRAY;
        }
      }
      if (effectType == CustomEffectType.FLY || effectType == CustomEffectType.FLY_REMOVE_ON_QUIT)
      {
        if (CustomEffectManager.hasEffect(entity, CustomEffectType.FLY_NOT_ENABLED))
        {
          textColor = NamedTextColor.GRAY;
        }
      }
      Component effectComponent = ComponentUtil.translate((showDuration ? effectType.translationKey() : effectType.getShortenTranslationKey())).color(textColor);
      Component create = ComponentUtil.create(entity instanceof Player player ? player : null, customEffect);
      effectComponent = effectComponent.hoverEvent(create.hoverEvent()).clickEvent(create.clickEvent());
      arguments.add(
              ComponentUtil.translate(key2, effectComponent,
                      !customEffect.isTimeHidden() && showDuration ?
                              ComponentUtil.translate(
                                      timePrefixColor + " %s", ComponentUtil.timeFormat(duration * 50L, TimeFormatType.SHORT))
                              : ""
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
  public static Component getVanillaDisplay(@NotNull Entity entity, @NotNull Collection<PotionEffect> potionEffects, boolean showDuration)
  {
    UUID uuid = entity.getUniqueId();
    StringBuilder key = new StringBuilder();
    List<Component> arguments = new ArrayList<>();
    for (PotionEffect potionEffect : potionEffects)
    {
      int duration = potionEffect.getDuration();
      int initDuration = duration;
      if (Variable.potionEffectApplyMap.containsKey(uuid))
      {
        HashMap<String, Integer> hashMap = Variable.potionEffectApplyMap.get(uuid);
        if (hashMap.containsKey(potionEffect.getType().translationKey()))
        {
          initDuration = hashMap.get(potionEffect.getType().translationKey());
        }
      }
      int amplifier = potionEffect.getAmplifier();
      boolean isInfinite = duration == -1, less10Sec = duration > 0 && duration < 200, less1Min = duration > 0 && duration < 1200;
      boolean ampleZero = amplifier == 0;
      int remain = 255 - (duration % 20 * 10 + 56);
      String timePrefixColor = initDuration > 200 && (initDuration > 20 * 60 || duration * 1d / initDuration <= 0.2) && less10Sec ?
              (isVanillaNegative(potionEffect.getType()) ?
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
      PotionEffectType effectType = potionEffect.getType();
      Component effectComponent = ComponentUtil.translate((isVanillaNegative(effectType) ? "&c" : "&a") + (showDuration ? TranslatableKeyParser.getKey(effectType) : getVanillaShortTranslationKey(effectType)));
      Component create = ComponentUtil.create(potionEffect);
      effectComponent = effectComponent.hoverEvent(create.hoverEvent()).clickEvent(create.clickEvent());
      arguments.add(
              ComponentUtil.translate(key2,
                      effectComponent, showDuration ?
                              ComponentUtil.translate(
                                      timePrefixColor + " %s", ComponentUtil.timeFormat(duration * 50L, TimeFormatType.SHORT))
                              : ""
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
    if (potionEffectType.equals(PotionEffectType.DARKNESS))
    {
      return true;
    }
    return false;
  }
}

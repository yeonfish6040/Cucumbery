package com.jho5245.cucumbery.util.itemlore;

import com.jho5245.cucumbery.customeffect.CustomEffectType;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ItemLorePotionDescription
{
  public static final Component ABSORPTION = ComponentUtil.translate("effect.minecraft.night_vision");

  public static final Component BAD_OMEN = ComponentUtil.translate("effect.minecraft.bad_omen");

  public static final Component BLINDNESS = ComponentUtil.translate("effect.minecraft.blindness");

  public static final Component CONDUIT_POWER = ComponentUtil.translate("effect.minecraft.conduit_power");

  public static final Component DOLPHINS_GRACE = ComponentUtil.translate("effect.minecraft.dolphins_grace");

  public static final Component FIRE_RESISTANCE = ComponentUtil.translate("effect.minecraft.fire_resistance");

  public static final Component GLOWING = ComponentUtil.translate("effect.minecraft.glowing");

  public static final Component HASTE = ComponentUtil.translate("effect.minecraft.haste");

  public static final Component HEALTH_BOOST = ComponentUtil.translate("effect.minecraft.health_boost");

  public static final Component HERO_OF_THE_VILLAGE = ComponentUtil.translate("effect.minecraft.hero_of_the_village");

  public static final Component HUNGER = ComponentUtil.translate("effect.minecraft.hunger");

  public static final Component INSTANT_DAMAGE = ComponentUtil.translate("effect.minecraft.instant_damage");

  public static final Component INSTANT_HEALTH = ComponentUtil.translate("effect.minecraft.instant_health");

  public static final Component INVISIBILTY = ComponentUtil.translate("effect.minecraft.invisibility");

  public static final Component JUMP_BOOST = ComponentUtil.translate("effect.minecraft.jump_boost");

  public static final Component LEVITATION = ComponentUtil.translate("effect.minecraft.levitation");

  public static final Component LUCK = ComponentUtil.translate("effect.minecraft.luck");

  public static final Component MINING_FATIGUE = ComponentUtil.translate("effect.minecraft.mining_fatigue");

  public static final Component NAUSEA = ComponentUtil.translate("effect.minecraft.nausea");

  public static final Component NIGHT_VISION = ComponentUtil.translate("effect.minecraft.night_vision");

  public static final Component POISON = ComponentUtil.translate("effect.minecraft.poison");

  public static final Component REGENERATION = ComponentUtil.translate("effect.minecraft.regeneration");

  public static final Component RESISTANCE = ComponentUtil.translate("effect.minecraft.resistance");

  public static final Component SATURATION = ComponentUtil.translate("effect.minecraft.saturation");

  public static final Component SLOW_FALLING = ComponentUtil.translate("effect.minecraft.slow_falling");

  public static final Component SLOWNESS = ComponentUtil.translate("effect.minecraft.slowness");

  public static final Component SPEED = ComponentUtil.translate("effect.minecraft.speed");

  public static final Component STRENGTH = ComponentUtil.translate("effect.minecraft.strength");

  public static final Component UNLUCK = ComponentUtil.translate("effect.minecraft.unluck");

  public static final Component WATER_BREATHING = ComponentUtil.translate("effect.minecraft.water_breathing");

  public static final Component WEAKNESS = ComponentUtil.translate("effect.minecraft.weakness");

  public static final Component WITHER = ComponentUtil.translate("effect.minecraft.wither");

  public static final Component NONE = ComponentUtil.translate("&7effect.none");

  @NotNull
  public static Component getComponent(@NotNull PotionEffectType potionEffectType)
  {
    if (potionEffectType.equals(PotionEffectType.ABSORPTION))
    {
      return ABSORPTION;
    }
    if (potionEffectType.equals(PotionEffectType.BAD_OMEN))
    {
      return BAD_OMEN;
    }
    if (potionEffectType.equals(PotionEffectType.BLINDNESS))
    {
      return BLINDNESS;
    }
    if (potionEffectType.equals(PotionEffectType.CONDUIT_POWER))
    {
      return CONDUIT_POWER;
    }
    if (potionEffectType.equals(PotionEffectType.CONFUSION))
    {
      return NAUSEA;
    }
    if (potionEffectType.equals(PotionEffectType.DAMAGE_RESISTANCE))
    {
      return RESISTANCE;
    }
    if (potionEffectType.equals(PotionEffectType.DOLPHINS_GRACE))
    {
      return DOLPHINS_GRACE;
    }
    if (potionEffectType.equals(PotionEffectType.FAST_DIGGING))
    {
      return HASTE;
    }
    if (potionEffectType.equals(PotionEffectType.FIRE_RESISTANCE))
    {
      return FIRE_RESISTANCE;
    }
    if (potionEffectType.equals(PotionEffectType.GLOWING))
    {
      return GLOWING;
    }
    if (potionEffectType.equals(PotionEffectType.HARM))
    {
      return INSTANT_DAMAGE;
    }
    if (potionEffectType.equals(PotionEffectType.HEAL))
    {
      return INSTANT_HEALTH;
    }
    if (potionEffectType.equals(PotionEffectType.HEALTH_BOOST))
    {
      return HEALTH_BOOST;
    }
    if (potionEffectType.equals(PotionEffectType.HERO_OF_THE_VILLAGE))
    {
      return HERO_OF_THE_VILLAGE;
    }
    if (potionEffectType.equals(PotionEffectType.HUNGER))
    {
      return HUNGER;
    }
    if (potionEffectType.equals(PotionEffectType.INCREASE_DAMAGE))
    {
      return STRENGTH;
    }
    if (potionEffectType.equals(PotionEffectType.INVISIBILITY))
    {
      return INVISIBILTY;
    }
    if (potionEffectType.equals(PotionEffectType.JUMP))
    {
      return JUMP_BOOST;
    }
    if (potionEffectType.equals(PotionEffectType.LEVITATION))
    {
      return LEVITATION;
    }
    if (potionEffectType.equals(PotionEffectType.LUCK))
    {
      return LUCK;
    }
    if (potionEffectType.equals(PotionEffectType.NIGHT_VISION))
    {
      return NIGHT_VISION;
    }
    if (potionEffectType.equals(PotionEffectType.POISON))
    {
      return POISON;
    }
    if (potionEffectType.equals(PotionEffectType.REGENERATION))
    {
      return REGENERATION;
    }
    if (potionEffectType.equals(PotionEffectType.SATURATION))
    {
      return SATURATION;
    }
    if (potionEffectType.equals(PotionEffectType.SLOW))
    {
      return SLOWNESS;
    }
    if (potionEffectType.equals(PotionEffectType.SLOW_DIGGING))
    {
      return MINING_FATIGUE;
    }
    if (potionEffectType.equals(PotionEffectType.SLOW_FALLING))
    {
      return SLOW_FALLING;
    }
    if (potionEffectType.equals(PotionEffectType.SPEED))
    {
      return SPEED;
    }
    if (potionEffectType.equals(PotionEffectType.UNLUCK))
    {
      return UNLUCK;
    }
    if (potionEffectType.equals(PotionEffectType.WATER_BREATHING))
    {
      return WATER_BREATHING;
    }
    if (potionEffectType.equals(PotionEffectType.WEAKNESS))
    {
      return WEAKNESS;
    }
    if (potionEffectType.equals(PotionEffectType.WITHER))
    {
      return WITHER;
    }
    return NONE;
  }

  @NotNull
  public static Component getDescription(double chance, @NotNull Component effect, long duration, int amplifier)
  {
    List<Component> args = new ArrayList<>();
    args.add(ComponentUtil.create(Constant.Sosu2.format(chance) + "%"));
    args.add(effect);
    if (amplifier > 1)
    args.add(Component.text(amplifier));
    if (duration > 0)
    {
      args.add(ComponentUtil.create(" (" + Method.timeFormatMilli(duration * 50, true, 2) + ")"));
    }
    return ComponentUtil.translate(amplifier > 1 ? "rgb255,97,144;%1$s 확률로 %2$s %3$s단계%4$s 적용" : "rgb255,97,144;%1$s 확률로 %2$s%3$s 적용", args);
  }

  @NotNull
  public static Component getDescription(@NotNull Component effect, long duration)
  {
    return getDescription(100d, effect, duration, 1);
  }

  @NotNull
  public static Component getDescription(@NotNull Component effect, long duration, int amplifier)
  {
    return getDescription(100d, effect, duration, amplifier);
  }

  public static List<Component> getPotionList(ItemStack item)
  {
    List<Component> lore = new ArrayList<>(Arrays.asList(Component.empty(), ComponentUtil.translate(Constant.ITEM_LORE_STATUS_EFFECT)));
    PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
    PotionType potionType = potionMeta.getBasePotionData().getType();
    List<Component> customEffects = getCustomEffectList(item);
    Collection<PotionEffect> effects = new ArrayList<>(potionMeta.getCustomEffects());
    effects.removeIf(effect -> effect.getDuration() == 0);
    if (!effects.isEmpty() || !customEffects.isEmpty())
    {
      for (PotionEffect potionEffect : effects)
      {
        lore.add(getDescription(100d, getComponent(potionEffect.getType()), potionEffect.getDuration(), potionEffect.getAmplifier() + 1));
      }
      if (potionType == PotionType.AWKWARD || potionType == PotionType.MUNDANE || potionType == PotionType.THICK || potionType == PotionType.UNCRAFTABLE
              || potionType == PotionType.WATER)
      {
        lore.addAll(customEffects);
        return lore;
      }
    }


    switch (potionType)
    {
      case AWKWARD:
      case MUNDANE:
      case THICK:
      case UNCRAFTABLE:
      case WATER:
        lore.add(NONE);
        break;
      case FIRE_RESISTANCE:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(FIRE_RESISTANCE, 8 * 60 * 20));
        }
        else
        {
          lore.add(getDescription(FIRE_RESISTANCE, 3 * 60 * 20));
        }
        break;
      case INSTANT_DAMAGE:
        if (potionMeta.getBasePotionData().isUpgraded())
        {
          lore.add(getDescription(INSTANT_DAMAGE, 0, 2));
        }
        else
        {
          lore.add(getDescription(INSTANT_DAMAGE, 0, 1));
        }
        break;
      case INSTANT_HEAL:
        if (potionMeta.getBasePotionData().isUpgraded())
        {
          lore.add(getDescription(INSTANT_HEALTH, 0, 2));
        }
        else
        {
          lore.add(getDescription(INSTANT_HEALTH, 0, 1));
        }
        break;
      case INVISIBILITY:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(INVISIBILTY, 8 * 60 * 20, 1));
        }
        else
        {
          lore.add(getDescription(INVISIBILTY, 3 * 60 * 20, 1));
        }
        break;
      case JUMP:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(JUMP_BOOST, 8 * 60 * 20, 1));
        }
        else if (potionMeta.getBasePotionData().isUpgraded())
        {
          lore.add(getDescription(JUMP_BOOST, 90 * 20, 2));
        }
        else
        {
          lore.add(getDescription(JUMP_BOOST, 3 * 60 * 20, 1));
        }
        break;
      case LUCK:
        lore.add(getDescription(LUCK, 5 * 60 * 20, 1));
        break;
      case NIGHT_VISION:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(NIGHT_VISION, 8 * 60 * 20, 1));
        }
        else
        {
          lore.add(getDescription(NIGHT_VISION, 3 * 60 * 20, 1));
        }
        break;
      case POISON:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(POISON, 90 * 20, 1));
        }
        else if (potionMeta.getBasePotionData().isUpgraded())
        {
          lore.add(getDescription(POISON, 21 * 20, 2));
        }
        else
        {
          lore.add(getDescription(POISON, 45 * 20, 1));
        }
        break;
      case REGEN:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(REGENERATION, 90 * 20, 1));
        }
        else if (potionMeta.getBasePotionData().isUpgraded())
        {

          lore.add(getDescription(REGENERATION, 22 * 20, 2));
        }
        else
        {
          lore.add(getDescription(REGENERATION, 45 * 20, 1));
        }
        break;
      case SLOWNESS:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(SLOWNESS, 4 * 60 * 20, 1));
        }
        else if (potionMeta.getBasePotionData().isUpgraded())
        {
          lore.add(getDescription(SLOWNESS, 20 * 20, 4));
        }
        else
        {
          lore.add(getDescription(SLOWNESS, 90 * 20, 1));
        }
        break;
      case SLOW_FALLING:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(SLOW_FALLING, 4 * 60 * 20, 1));
        }
        else
        {
          lore.add(getDescription(SLOW_FALLING, 90 * 20, 1));
        }
        break;
      case SPEED:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(SPEED, 8 * 60 * 20, 1));
        }
        else if (potionMeta.getBasePotionData().isUpgraded())
        {
          lore.add(getDescription(SPEED, 90 * 20, 2));
        }
        else
        {
          lore.add(getDescription(SPEED, 3 * 60 * 20, 1));
        }
        break;
      case STRENGTH:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(STRENGTH, 8 * 60 * 20, 1));
        }
        else if (potionMeta.getBasePotionData().isUpgraded())
        {
          lore.add(getDescription(STRENGTH, 90 * 20, 2));
        }
        else
        {
          lore.add(getDescription(STRENGTH, 3 * 60 * 20, 1));
        }
        break;
      case TURTLE_MASTER:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(SLOWNESS, 40 * 20, 4));
          lore.add(getDescription(RESISTANCE, 40 * 20, 3));
        }
        else if (potionMeta.getBasePotionData().isUpgraded())
        {
          lore.add(getDescription(SLOWNESS, 20 * 20, 6));
          lore.add(getDescription(RESISTANCE, 20 * 20, 4));
        }
        else
        {
          lore.add(getDescription(SLOWNESS, 20 * 20, 4));
          lore.add(getDescription(RESISTANCE, 20 * 20, 3));
        }
        break;
      case WATER_BREATHING:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(WATER_BREATHING, 8 * 60 * 20, 1));
        }
        else
        {
          lore.add(getDescription(WATER_BREATHING, 3 * 60 * 20, 1));
        }
        break;
      case WEAKNESS:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(WEAKNESS, 4 * 60 * 20, 1));
        }
        else
        {
          lore.add(getDescription(WEAKNESS, 90 * 20, 1));
        }
        break;
      default:
        break;
    }
    lore.addAll(customEffects);
    return lore;
  }

  public static List<Component> getSplashPotionList(ItemStack item)
  {
    List<Component> lore = new ArrayList<>(Arrays.asList(Component.empty(), ComponentUtil.translate(Constant.ITEM_LORE_STATUS_EFFECT)));
    PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
    PotionType potionType = potionMeta.getBasePotionData().getType();
    List<Component> customEffects = getCustomEffectList(item);
    Collection<PotionEffect> effects = new ArrayList<>(potionMeta.getCustomEffects());
    effects.removeIf(effect -> effect.getDuration() == 0);
    if (!effects.isEmpty() || !customEffects.isEmpty())
    {
      for (PotionEffect potionEffect : effects)
      {
        lore.add(getDescription(100d, getComponent(potionEffect.getType()), potionEffect.getDuration(), potionEffect.getAmplifier() + 1));
      }
      if (potionType == PotionType.AWKWARD || potionType == PotionType.MUNDANE || potionType == PotionType.THICK || potionType == PotionType.UNCRAFTABLE
              || potionType == PotionType.WATER)
      {
        lore.addAll(customEffects);
        return lore;
      }
    }
    switch (potionType)
    {
      case AWKWARD:
      case MUNDANE:
      case THICK:
      case UNCRAFTABLE:
      case WATER:
        lore.add(NONE);
        break;
      case FIRE_RESISTANCE:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(FIRE_RESISTANCE, 8 * 60 * 20));
        }
        else
        {
          lore.add(getDescription(FIRE_RESISTANCE, 3 * 60 * 20));
        }
        break;
      case INSTANT_DAMAGE:
        if (potionMeta.getBasePotionData().isUpgraded())
        {
          lore.add(getDescription(INSTANT_DAMAGE, 0, 2));
        }
        else
        {
          lore.add(getDescription(INSTANT_DAMAGE, 0, 1));
        }
        break;
      case INSTANT_HEAL:
        if (potionMeta.getBasePotionData().isUpgraded())
        {
          lore.add(getDescription(INSTANT_HEALTH, 0, 2));
        }
        else
        {
          lore.add(getDescription(INSTANT_HEALTH, 0, 1));
        }
        break;
      case INVISIBILITY:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(INVISIBILTY, 8 * 60 * 20, 1));
        }
        else
        {
          lore.add(getDescription(INVISIBILTY, 3 * 60 * 20, 1));
        }
        break;
      case JUMP:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(JUMP_BOOST, 8 * 60 * 20, 1));
        }
        else if (potionMeta.getBasePotionData().isUpgraded())
        {
          lore.add(getDescription(JUMP_BOOST, 90 * 20, 2));
        }
        else
        {
          lore.add(getDescription(JUMP_BOOST, 3 * 60 * 20, 1));
        }
        break;
      case LUCK:
        lore.add(getDescription(LUCK, 5 * 60 * 20, 1));
        break;
      case NIGHT_VISION:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(NIGHT_VISION, 8 * 60 * 20, 1));
        }
        else
        {
          lore.add(getDescription(NIGHT_VISION, 3 * 60 * 20, 1));
        }
        break;
      case POISON:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(POISON, 90 * 20, 1));
        }
        else if (potionMeta.getBasePotionData().isUpgraded())
        {
          lore.add(getDescription(POISON, 21 * 20, 2));
        }
        else
        {
          lore.add(getDescription(POISON, 45 * 20, 1));
        }
        break;
      case REGEN:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(REGENERATION, 90 * 20, 1));
        }
        else if (potionMeta.getBasePotionData().isUpgraded())
        {

          lore.add(getDescription(REGENERATION, 22 * 20, 2));
        }
        else
        {
          lore.add(getDescription(REGENERATION, 45 * 20, 1));
        }
        break;
      case SLOWNESS:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(SLOWNESS, 4 * 60 * 20, 1));
        }
        else if (potionMeta.getBasePotionData().isUpgraded())
        {
          lore.add(getDescription(SLOWNESS, 20 * 20, 4));
        }
        else
        {
          lore.add(getDescription(SLOWNESS, 90 * 20, 1));
        }
        break;
      case SLOW_FALLING:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(SLOW_FALLING, 4 * 60 * 20, 1));
        }
        else
        {
          lore.add(getDescription(SLOW_FALLING, 90 * 20, 1));
        }
        break;
      case SPEED:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(SPEED, 8 * 60 * 20, 1));
        }
        else if (potionMeta.getBasePotionData().isUpgraded())
        {
          lore.add(getDescription(SPEED, 90 * 20, 2));
        }
        else
        {
          lore.add(getDescription(SPEED, 3 * 60 * 20, 1));
        }
        break;
      case STRENGTH:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(STRENGTH, 8 * 60 * 20, 1));
        }
        else if (potionMeta.getBasePotionData().isUpgraded())
        {
          lore.add(getDescription(STRENGTH, 90 * 20, 2));
        }
        else
        {
          lore.add(getDescription(STRENGTH, 3 * 60 * 20, 1));
        }
        break;
      case TURTLE_MASTER:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(SLOWNESS, 40 * 20, 4));
          lore.add(getDescription(RESISTANCE, 40 * 20, 3));
        }
        else if (potionMeta.getBasePotionData().isUpgraded())
        {
          lore.add(getDescription(SLOWNESS, 20 * 20, 6));
          lore.add(getDescription(RESISTANCE, 20 * 20, 4));
        }
        else
        {
          lore.add(getDescription(SLOWNESS, 20 * 20, 4));
          lore.add(getDescription(RESISTANCE, 20 * 20, 3));
        }
        break;
      case WATER_BREATHING:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(WATER_BREATHING, 8 * 60 * 20, 1));
        }
        else
        {
          lore.add(getDescription(WATER_BREATHING, 3 * 60 * 20, 1));
        }
        break;
      case WEAKNESS:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(WEAKNESS, 4 * 60 * 20, 1));
        }
        else
        {
          lore.add(getDescription(WEAKNESS, 90 * 20, 1));
        }
        break;
      default:
        break;
    }
    lore.addAll(customEffects);
    return lore;
  }

  public static List<Component> getLingeringPotionList(ItemStack item)
  {
    List<Component> lore = new ArrayList<>(Arrays.asList(Component.empty(), ComponentUtil.translate(Constant.ITEM_LORE_STATUS_EFFECT)));
    PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
    PotionType potionType = potionMeta.getBasePotionData().getType();
    List<Component> customEffects = getCustomEffectList(item);
    Collection<PotionEffect> effects = new ArrayList<>(potionMeta.getCustomEffects());
    effects.removeIf(effect -> effect.getDuration() == 0);
    if (!effects.isEmpty() || !customEffects.isEmpty())
    {
      for (PotionEffect potionEffect : effects)
      {
        lore.add(getDescription(100d, getComponent(potionEffect.getType()), potionEffect.getDuration(), potionEffect.getAmplifier() + 1));
      }
      if (potionType == PotionType.AWKWARD || potionType == PotionType.MUNDANE || potionType == PotionType.THICK || potionType == PotionType.UNCRAFTABLE
              || potionType == PotionType.WATER)
      {
        lore.addAll(customEffects);
        return lore;
      }
    }
    switch (potionType)
    {
      case AWKWARD:
      case MUNDANE:
      case THICK:
      case UNCRAFTABLE:
      case WATER:
        lore.add(NONE);
        break;
      case FIRE_RESISTANCE:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(FIRE_RESISTANCE, 2 * 60 * 20));
        }
        else
        {
          lore.add(getDescription(FIRE_RESISTANCE, 45 * 20));
        }
        break;
      case INSTANT_DAMAGE:
        if (potionMeta.getBasePotionData().isUpgraded())
        {
          lore.add(getDescription(INSTANT_DAMAGE, 0, 2));
        }
        else
        {
          lore.add(getDescription(INSTANT_DAMAGE, 0, 1));
        }
        break;
      case INSTANT_HEAL:
        if (potionMeta.getBasePotionData().isUpgraded())
        {
          lore.add(getDescription(INSTANT_HEALTH, 0, 2));
        }
        else
        {
          lore.add(getDescription(INSTANT_HEALTH, 0, 1));
        }
        break;
      case INVISIBILITY:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(INVISIBILTY, 2 * 60 * 20, 1));
        }
        else
        {
          lore.add(getDescription(INVISIBILTY, 45 * 20, 1));
        }
        break;
      case JUMP:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(JUMP_BOOST, 2 * 60 * 20, 1));
        }
        else if (potionMeta.getBasePotionData().isUpgraded())
        {
          lore.add(getDescription(JUMP_BOOST, 22 * 20, 2));
        }
        else
        {
          lore.add(getDescription(JUMP_BOOST, 45 * 20, 1));
        }
        break;
      case LUCK:
        lore.add(getDescription(LUCK, 75 * 20, 1));
        break;
      case NIGHT_VISION:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(NIGHT_VISION, 2 * 60 * 20, 1));
        }
        else
        {
          lore.add(getDescription(NIGHT_VISION, 45 * 20, 1));
        }
        break;
      case POISON:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(POISON, 22 * 20, 1));
        }
        else if (potionMeta.getBasePotionData().isUpgraded())
        {
          lore.add(getDescription(POISON, 5 * 20, 2));
        }
        else
        {
          lore.add(getDescription(POISON, 11 * 20, 1));
        }
        break;
      case REGEN:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(REGENERATION, 22 * 20, 1));
        }
        else if (potionMeta.getBasePotionData().isUpgraded())
        {

          lore.add(getDescription(REGENERATION, 5 * 20, 2));
        }
        else
        {
          lore.add(getDescription(REGENERATION, 11 * 20, 1));
        }
        break;
      case SLOWNESS:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(SLOWNESS, 60 * 20, 1));
        }
        else if (potionMeta.getBasePotionData().isUpgraded())
        {
          lore.add(getDescription(SLOWNESS, 5 * 20, 4));
        }
        else
        {
          lore.add(getDescription(SLOWNESS, 22 * 20, 1));
        }
        break;
      case SLOW_FALLING:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(SLOW_FALLING, 22 * 20, 1));
        }
        else
        {
          lore.add(getDescription(SLOW_FALLING, 60 * 20, 1));
        }
        break;
      case SPEED:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(SPEED, 2 * 60 * 20, 1));
        }
        else if (potionMeta.getBasePotionData().isUpgraded())
        {
          lore.add(getDescription(SPEED, 45 * 20, 2));
        }
        else
        {
          lore.add(getDescription(SPEED, 3 * 60 * 20, 1));
        }
        break;
      case STRENGTH:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(STRENGTH, 2 * 60 * 20, 1));
        }
        else if (potionMeta.getBasePotionData().isUpgraded())
        {
          lore.add(getDescription(STRENGTH, 22 * 20, 2));
        }
        else
        {
          lore.add(getDescription(STRENGTH, 45 * 20, 1));
        }
        break;
      case TURTLE_MASTER:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(SLOWNESS, 10 * 20, 4));
          lore.add(getDescription(RESISTANCE, 10 * 20, 3));
        }
        else if (potionMeta.getBasePotionData().isUpgraded())
        {
          lore.add(getDescription(SLOWNESS, 5 * 20, 6));
          lore.add(getDescription(RESISTANCE, 5 * 20, 4));
        }
        else
        {
          lore.add(getDescription(SLOWNESS, 5 * 20, 4));
          lore.add(getDescription(RESISTANCE, 5 * 20, 3));
        }
        break;
      case WATER_BREATHING:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(WATER_BREATHING, 2 * 60 * 20, 1));
        }
        else
        {
          lore.add(getDescription(WATER_BREATHING, 45 * 20, 1));
        }
        break;
      case WEAKNESS:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(WEAKNESS, 60 * 20, 1));
        }
        else
        {
          lore.add(getDescription(WEAKNESS, 22 * 20, 1));
        }
        break;
      default:
        break;
    }
    lore.addAll(customEffects);
    return lore;
  }

  public static List<Component> getTippedArrowList(ItemStack item)
  {
    List<Component> lore = new ArrayList<>(Arrays.asList(Component.empty(), ComponentUtil.translate(Constant.ITEM_LORE_STATUS_EFFECT)));
    PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
    PotionType potionType = potionMeta.getBasePotionData().getType();
    List<Component> customEffects = getCustomEffectList(item);
    Collection<PotionEffect> effects = new ArrayList<>(potionMeta.getCustomEffects());
    effects.removeIf(effect -> effect.getDuration() == 0);
    if (!effects.isEmpty() || !customEffects.isEmpty())
    {
      for (PotionEffect potionEffect : effects)
      {
        lore.add(getDescription(100d, getComponent(potionEffect.getType()), potionEffect.getDuration(), potionEffect.getAmplifier() + 1));
      }
      if (potionType == PotionType.AWKWARD || potionType == PotionType.MUNDANE || potionType == PotionType.THICK || potionType == PotionType.UNCRAFTABLE
              || potionType == PotionType.WATER)
      {
        lore.addAll(customEffects);
        return lore;
      }
    }
    switch (potionType)
    {
      case AWKWARD:
      case MUNDANE:
      case THICK:
      case UNCRAFTABLE:
      case WATER:
        lore.add(NONE);
        break;
      case FIRE_RESISTANCE:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(FIRE_RESISTANCE, 60 * 20));
        }
        else
        {
          lore.add(getDescription(FIRE_RESISTANCE, 22 * 20));
        }
        break;
      case INSTANT_DAMAGE:
        if (potionMeta.getBasePotionData().isUpgraded())
        {
          lore.add(getDescription(INSTANT_DAMAGE, 0, 2));
        }
        else
        {
          lore.add(getDescription(INSTANT_DAMAGE, 0, 1));
        }
        break;
      case INSTANT_HEAL:
        if (potionMeta.getBasePotionData().isUpgraded())
        {
          lore.add(getDescription(INSTANT_HEALTH, 0, 2));
        }
        else
        {
          lore.add(getDescription(INSTANT_HEALTH, 0, 1));
        }
        break;
      case INVISIBILITY:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(INVISIBILTY, 60 * 20, 1));
        }
        else
        {
          lore.add(getDescription(INVISIBILTY, 22 * 20, 1));
        }
        break;
      case JUMP:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(JUMP_BOOST, 60 * 20, 1));
        }
        else if (potionMeta.getBasePotionData().isUpgraded())
        {
          lore.add(getDescription(JUMP_BOOST, 11 * 20, 2));
        }
        else
        {
          lore.add(getDescription(JUMP_BOOST, 22 * 20, 1));
        }
        break;
      case LUCK:
        lore.add(getDescription(LUCK, 37 * 20, 1));
        break;
      case NIGHT_VISION:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(NIGHT_VISION, 60 * 20, 1));
        }
        else
        {
          lore.add(getDescription(NIGHT_VISION, 22 * 20, 1));
        }
        break;
      case POISON:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(POISON, 11 * 20, 1));
        }
        else if (potionMeta.getBasePotionData().isUpgraded())
        {
          lore.add(getDescription(POISON, 2 * 20, 2));
        }
        else
        {
          lore.add(getDescription(POISON, 5 * 20, 1));
        }
        break;
      case REGEN:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(REGENERATION, 11 * 20, 1));
        }
        else if (potionMeta.getBasePotionData().isUpgraded())
        {

          lore.add(getDescription(REGENERATION, 2 * 20, 2));
        }
        else
        {
          lore.add(getDescription(REGENERATION, 5 * 20, 1));
        }
        break;
      case SLOWNESS:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(SLOWNESS, 30 * 20, 1));
        }
        else if (potionMeta.getBasePotionData().isUpgraded())
        {
          lore.add(getDescription(SLOWNESS, 2 * 20, 4));
        }
        else
        {
          lore.add(getDescription(SLOWNESS, 11 * 20, 1));
        }
        break;
      case SLOW_FALLING:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(SLOW_FALLING, 30 * 20, 1));
        }
        else
        {
          lore.add(getDescription(SLOW_FALLING, 11 * 20, 1));
        }
        break;
      case SPEED:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(SPEED, 60 * 20, 1));
        }
        else if (potionMeta.getBasePotionData().isUpgraded())
        {
          lore.add(getDescription(SPEED, 11 * 20, 2));
        }
        else
        {
          lore.add(getDescription(SPEED, 22 * 20, 1));
        }
        break;
      case STRENGTH:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(STRENGTH, 60 * 20, 1));
        }
        else if (potionMeta.getBasePotionData().isUpgraded())
        {
          lore.add(getDescription(STRENGTH, 11 * 20, 2));
        }
        else
        {
          lore.add(getDescription(STRENGTH, 22 * 20, 1));
        }
        break;
      case TURTLE_MASTER:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(SLOWNESS, 5 * 20, 4));
          lore.add(getDescription(RESISTANCE, 5 * 20, 3));
        }
        else if (potionMeta.getBasePotionData().isUpgraded())
        {
          lore.add(getDescription(SLOWNESS, 2 * 20, 6));
          lore.add(getDescription(RESISTANCE, 2 * 20, 4));
        }
        else
        {
          lore.add(getDescription(SLOWNESS, 2 * 20, 4));
          lore.add(getDescription(RESISTANCE, 2 * 20, 3));
        }
        break;
      case WATER_BREATHING:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(WATER_BREATHING, 60 * 20, 1));
        }
        else
        {
          lore.add(getDescription(WATER_BREATHING, 22 * 20, 1));
        }
        break;
      case WEAKNESS:
        if (potionMeta.getBasePotionData().isExtended())
        {
          lore.add(getDescription(WEAKNESS, 30 * 20, 1));
        }
        else
        {
          lore.add(getDescription(WEAKNESS, 11 * 20, 1));
        }
        break;
      default:
        break;
    }
    lore.addAll(customEffects);
    return lore;
  }

  @NotNull
  public static List<Component> getCustomEffectList(@NotNull ItemStack item)
  {
    List<Component> list = new ArrayList<>();
    NBTCompoundList potionsTag = NBTAPI.getCompoundList(NBTAPI.getMainCompound(item), CucumberyTag.CUSTOM_EFFECTS);
    if (potionsTag != null)
    {
      for (NBTCompound potionTag : potionsTag)
      {
        try
        {
          Component effect = ComponentUtil.create(CustomEffectType.valueOf(potionTag.getString(CucumberyTag.CUSTOM_EFFECTS_ID).toUpperCase())).color(null);
          int duration = potionTag.getInteger(CucumberyTag.CUSTOM_EFFECTS_DURATION), amplifier = potionTag.getInteger(CucumberyTag.CUSTOM_EFFECTS_AMPLIFIER);
          list.add(getDescription(effect, duration, amplifier + 1));
        }
        catch (Exception ignored)
        {

        }
      }
    }
    return list;
  }
}

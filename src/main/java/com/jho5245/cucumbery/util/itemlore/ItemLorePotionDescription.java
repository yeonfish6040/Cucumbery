package com.jho5245.cucumbery.util.itemlore;

import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemLorePotionDescription
{
  public static final Component ABSORPTION = ComponentUtil.createTranslate("effect.minecraft.night_vision");

  public static final Component BAD_OMEN = ComponentUtil.createTranslate("effect.minecraft.bad_omen");

  public static final Component BLINDNESS = ComponentUtil.createTranslate("effect.minecraft.blindness");

  public static final Component CONDUIT_POWER = ComponentUtil.createTranslate("effect.minecraft.conduit_power");

  public static final Component DOLPHINS_GRACE = ComponentUtil.createTranslate("effect.minecraft.dolphins_grace");

  public static final Component FIRE_RESISTANCE = ComponentUtil.createTranslate("effect.minecraft.fire_resistance");

  public static final Component GLOWING = ComponentUtil.createTranslate("effect.minecraft.glowing");

  public static final Component HASTE = ComponentUtil.createTranslate("effect.minecraft.haste");

  public static final Component HEALTH_BOOST = ComponentUtil.createTranslate("effect.minecraft.health_boost");

  public static final Component HERO_OF_THE_VILLAGE = ComponentUtil.createTranslate("effect.minecraft.hero_of_the_village");

  public static final Component HUNGER = ComponentUtil.createTranslate("effect.minecraft.hunger");

  public static final Component INSTANT_DAMAGE = ComponentUtil.createTranslate("effect.minecraft.instant_damage");

  public static final Component INSTANT_HEALTH = ComponentUtil.createTranslate("effect.minecraft.instant_health");

  public static final Component INVISIBILTY = ComponentUtil.createTranslate("effect.minecraft.invisibility");

  public static final Component JUMP_BOOST = ComponentUtil.createTranslate("effect.minecraft.jump_boost");

  public static final Component LEVITATION = ComponentUtil.createTranslate("effect.minecraft.levitation");

  public static final Component LUCK = ComponentUtil.createTranslate("effect.minecraft.luck");

  public static final Component MINING_FATIGUE = ComponentUtil.createTranslate("effect.minecraft.mining_fatigue");

  public static final Component NAUSEA = ComponentUtil.createTranslate("effect.minecraft.nausea");

  public static final Component NIGHT_VISION = ComponentUtil.createTranslate("effect.minecraft.night_vision");

  public static final Component POISON = ComponentUtil.createTranslate("effect.minecraft.poison");

  public static final Component REGENERATION = ComponentUtil.createTranslate("effect.minecraft.regeneration");

  public static final Component RESISTANCE = ComponentUtil.createTranslate("effect.minecraft.resistance");

  public static final Component SATURATION = ComponentUtil.createTranslate("effect.minecraft.saturation");

  public static final Component SLOW_FALLING = ComponentUtil.createTranslate("effect.minecraft.slow_falling");

  public static final Component SLOWNESS = ComponentUtil.createTranslate("effect.minecraft.slowness");

  public static final Component SPEED = ComponentUtil.createTranslate("effect.minecraft.speed");

  public static final Component STRENGTH = ComponentUtil.createTranslate("effect.minecraft.strength");

  public static final Component UNLUCK = ComponentUtil.createTranslate("effect.minecraft.unluck");

  public static final Component WATER_BREATHING = ComponentUtil.createTranslate("effect.minecraft.water_breathing");

  public static final Component WEAKNESS = ComponentUtil.createTranslate("effect.minecraft.weakness");

  public static final Component WITHER = ComponentUtil.createTranslate("effect.minecraft.wither");

  public static final Component NONE = ComponentUtil.createTranslate("&7effect.none");

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
    long millis = duration * 50L;
    long year = millis / (1000L * 60 * 60 * 24 * 365);
    millis %= (1000L * 60 * 60 * 24 * 365);
    long day = millis / (1000L * 60 * 60 * 24);
    millis %= (1000L * 60 * 60 * 24);
    long hour = millis / (1000L * 60 * 60);
    millis %= (1000L * 60 * 60);
    long min = millis / (1000L * 60);
    millis %= (1000L * 60);
    double sec = millis / 1000D;
    List<Component> args = new ArrayList<>();
    args.add(ComponentUtil.create(Constant.Sosu2.format(chance) + "%"));
    args.add(effect);
    args.add(ComponentUtil.createTranslate("%s단계", "" + (amplifier)));
    Component arg = null;
    if (year > 0)
    {
      arg = ComponentUtil.create(" (" + year).append(ComponentUtil.createTranslate("년").append(ComponentUtil.create(day != 0 || hour != 0 || min != 0 || sec != 0d ? " " : ")")));
    }
    if (day > 0)
    {
      Component tmp = (ComponentUtil.create((year != 0 ? "" : " (") + day).append(ComponentUtil.createTranslate("일").append(ComponentUtil.create(hour != 0 || min != 0 || sec != 0d ? " " : ")"))));
      if (arg == null)
      {
        arg = tmp;
      }
      else
      {
        arg = arg.append(tmp);
      }
    }
    if (hour > 0)
    {
      Component tmp = ComponentUtil.create((year != 0 || day != 0 ? "" : " (") + hour).append(ComponentUtil.createTranslate("시간").append(ComponentUtil.create(min != 0 || sec != 0d ? " " : ")")));
      if (arg == null)
      {
        arg = tmp;
      }
      else
      {
        arg = arg.append(tmp);
      }
    }
    if (min > 0)
    {
      Component tmp = ComponentUtil.create((year != 0 || day != 0 || hour != 0 ? "" : " (") + min).append(ComponentUtil.createTranslate("분").append(ComponentUtil.create(sec != 0d ? " " : ")")));
      if (arg == null)
      {
        arg = tmp;
      }
      else
      {
        arg = arg.append(tmp);
      }
    }
    if (sec > 0)
    {
      Component tmp = ComponentUtil.create((year != 0 || day != 0 || hour != 0 || min != 0 ? "" : " (") + Constant.Sosu2.format(sec)).append(ComponentUtil.createTranslate("초").append(ComponentUtil.create(")")));
      if (arg == null)
      {
        arg = tmp;
      }
      else
      {
        arg = arg.append(tmp);
      }
    }
    if (arg != null)
    {
      args.add(arg);
    }

    return ComponentUtil.createTranslate("rgb255,97,144;%s 확률로 %s %s%s 적용").args(args);
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
    List<Component> lore = new ArrayList<>(Arrays.asList(Component.empty(), ComponentUtil.createTranslate(Constant.ITEM_LORE_STATUS_EFFECT)));
    PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
    PotionType potionType = potionMeta.getBasePotionData().getType();
    if (potionMeta.getCustomEffects().size() != 0)
    {
      for (PotionEffect potionEffect : potionMeta.getCustomEffects())
      {
        lore.add(getDescription(100d, getComponent(potionEffect.getType()), potionEffect.getDuration() * 50L, potionEffect.getAmplifier() + 1));
      }
      if (potionType == PotionType.AWKWARD || potionType == PotionType.MUNDANE || potionType == PotionType.THICK || potionType == PotionType.UNCRAFTABLE
              || potionType == PotionType.WATER)
      {
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
    return lore;
  }

  public static List<Component> getSplashPotionList(ItemStack item)
  {
    List<Component> lore = new ArrayList<>(Arrays.asList(Component.empty(), ComponentUtil.createTranslate(Constant.ITEM_LORE_STATUS_EFFECT)));
    PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
    PotionType potionType = potionMeta.getBasePotionData().getType();
    if (potionMeta.getCustomEffects().size() != 0)
    {
      for (PotionEffect potionEffect : potionMeta.getCustomEffects())
      {
        lore.add(getDescription(100d, getComponent(potionEffect.getType()), potionEffect.getDuration() * 50L, potionEffect.getAmplifier() + 1));
      }
      if (potionType == PotionType.AWKWARD || potionType == PotionType.MUNDANE || potionType == PotionType.THICK || potionType == PotionType.UNCRAFTABLE
              || potionType == PotionType.WATER)
      {
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
    return lore;
  }

  public static List<Component> getLingeringPotionList(ItemStack item)
  {
    List<Component> lore = new ArrayList<>(Arrays.asList(Component.empty(), ComponentUtil.createTranslate(Constant.ITEM_LORE_STATUS_EFFECT)));
    PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
    PotionType potionType = potionMeta.getBasePotionData().getType();
    if (potionMeta.getCustomEffects().size() != 0)
    {
      for (PotionEffect potionEffect : potionMeta.getCustomEffects())
      {
        lore.add(getDescription(100d, getComponent(potionEffect.getType()), potionEffect.getDuration() * 50L, potionEffect.getAmplifier() + 1));
      }
      if (potionType == PotionType.AWKWARD || potionType == PotionType.MUNDANE || potionType == PotionType.THICK || potionType == PotionType.UNCRAFTABLE
              || potionType == PotionType.WATER)
      {
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
    return lore;
  }

  public static List<Component> getTippedArrowList(ItemStack item)
  {
    List<Component> lore = new ArrayList<>(Arrays.asList(Component.empty(), ComponentUtil.createTranslate(Constant.ITEM_LORE_STATUS_EFFECT)));
    PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
    PotionType potionType = potionMeta.getBasePotionData().getType();
    if (potionMeta.getCustomEffects().size() != 0)
    {
      for (PotionEffect potionEffect : potionMeta.getCustomEffects())
      {
        lore.add(getDescription(100d, getComponent(potionEffect.getType()), potionEffect.getDuration() * 50L, potionEffect.getAmplifier() + 1));
      }
      if (potionType == PotionType.AWKWARD || potionType == PotionType.MUNDANE || potionType == PotionType.THICK || potionType == PotionType.UNCRAFTABLE
              || potionType == PotionType.WATER)
      {
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
    return lore;
  }
}

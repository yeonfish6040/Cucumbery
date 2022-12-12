package com.jho5245.cucumbery.custom.customeffect.type;

import org.bukkit.NamespacedKey;
import org.bukkit.potion.PotionEffectType;

public class CustomEffectTypeMinecraft extends CustomEffectType
{
  public static final CustomEffectType
          SPEED = new CustomEffectType(NamespacedKey.minecraft("speed"), PotionEffectType.SPEED.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          SLOWNESS = new CustomEffectType(NamespacedKey.minecraft("slowness"), PotionEffectType.SLOW.translationKey(), builder().removeOnMilk().negative().maxAmplifier(255)),
          HASTE = new CustomEffectType(NamespacedKey.minecraft("haste"), PotionEffectType.FAST_DIGGING.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          MINING_FATIGUE = new CustomEffectType(NamespacedKey.minecraft("mining_fatigue"), PotionEffectType.SLOW_DIGGING.translationKey(), builder().removeOnMilk().negative().maxAmplifier(255)),
          STRENGTH = new CustomEffectType(NamespacedKey.minecraft("strength"), PotionEffectType.INCREASE_DAMAGE.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          INSTANT_HEALTH = new CustomEffectType(NamespacedKey.minecraft("instant_health"), PotionEffectType.HEAL.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          INSTANT_DAMAGE = new CustomEffectType(NamespacedKey.minecraft("instant_damage"), PotionEffectType.HARM.translationKey(), builder().removeOnMilk().negative().maxAmplifier(255)),
          JUMP_BOOST = new CustomEffectType(NamespacedKey.minecraft("jump_boost"), PotionEffectType.JUMP.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          NAUSEA = new CustomEffectType(NamespacedKey.minecraft("nausea"), PotionEffectType.CONFUSION.translationKey(), builder().removeOnMilk().negative().maxAmplifier(255)),
          REGENERATION = new CustomEffectType(NamespacedKey.minecraft("regeneration"), PotionEffectType.REGENERATION.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          RESISTANCE = new CustomEffectType(NamespacedKey.minecraft("resistance"), PotionEffectType.DAMAGE_RESISTANCE.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          FIRE_RESISTANCE = new CustomEffectType(NamespacedKey.minecraft("fire_resistance"), PotionEffectType.FIRE_RESISTANCE.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          WATER_BREATHING = new CustomEffectType(NamespacedKey.minecraft("water_breathing"), PotionEffectType.WATER_BREATHING.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          INVISIBILITY = new CustomEffectType(NamespacedKey.minecraft("invisibility"), PotionEffectType.INVISIBILITY.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          BLINDNESS = new CustomEffectType(NamespacedKey.minecraft("blindness"), PotionEffectType.BLINDNESS.translationKey(), builder().removeOnMilk().negative().maxAmplifier(255)),
          NIGHT_VISION = new CustomEffectType(NamespacedKey.minecraft("night_vision"), PotionEffectType.NIGHT_VISION.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          HUNGER = new CustomEffectType(NamespacedKey.minecraft("hunger"), PotionEffectType.HUNGER.translationKey(), builder().removeOnMilk().negative().maxAmplifier(255)),
          WEAKNESS = new CustomEffectType(NamespacedKey.minecraft("weakness"), PotionEffectType.WEAKNESS.translationKey(), builder().removeOnMilk().negative().maxAmplifier(255)),
          POISON = new CustomEffectType(NamespacedKey.minecraft("poison"), PotionEffectType.POISON.translationKey(), builder().removeOnMilk().negative().maxAmplifier(255)),
          WITHER = new CustomEffectType(NamespacedKey.minecraft("wither"), PotionEffectType.WITHER.translationKey(), builder().removeOnMilk().negative().maxAmplifier(255)),
          HEALTH_BOOST = new CustomEffectType(NamespacedKey.minecraft("health_boost"), PotionEffectType.HEALTH_BOOST.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          ABSORPTION = new CustomEffectType(NamespacedKey.minecraft("absorption"), PotionEffectType.ABSORPTION.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          SATURATION = new CustomEffectType(NamespacedKey.minecraft("saturation"), PotionEffectType.SATURATION.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          GLOWING = new CustomEffectType(NamespacedKey.minecraft("glowing"), PotionEffectType.GLOWING.translationKey(), builder().removeOnMilk().negative().maxAmplifier(255)),
          LEVITATION = new CustomEffectType(NamespacedKey.minecraft("levitation"), PotionEffectType.LEVITATION.translationKey(), builder().removeOnMilk().negative().maxAmplifier(255)),
          LUCK = new CustomEffectType(NamespacedKey.minecraft("luck"), PotionEffectType.LUCK.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          UNLUCK = new CustomEffectType(NamespacedKey.minecraft("unluck"), PotionEffectType.UNLUCK.translationKey(), builder().removeOnMilk().negative().maxAmplifier(255)),
          SLOW_FALLING = new CustomEffectType(NamespacedKey.minecraft("slow_falling"), PotionEffectType.SLOW_FALLING.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          CONDUIT_POWER = new CustomEffectType(NamespacedKey.minecraft("conduit_power"), PotionEffectType.CONDUIT_POWER.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          DOLPHINS_GRACE = new CustomEffectType(NamespacedKey.minecraft("dolphins_grace"), PotionEffectType.DOLPHINS_GRACE.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          BAD_OMEN = new CustomEffectType(NamespacedKey.minecraft("bad_omen"), PotionEffectType.BAD_OMEN.translationKey(), builder().removeOnMilk().negative().maxAmplifier(255)),
          HERO_OF_THE_VILLAGE = new CustomEffectType(NamespacedKey.minecraft("hero_of_the_village"), PotionEffectType.HERO_OF_THE_VILLAGE.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          DARKNESS = new CustomEffectType(NamespacedKey.minecraft("darkness"), PotionEffectType.DARKNESS.translationKey(), builder().removeOnMilk().negative().maxAmplifier(255)),
          POISON_M = new CustomEffectType(NamespacedKey.minecraft("poison_m"), PotionEffectType.POISON.translationKey(), builder().negative().enumHidden().maxAmplifier(255));

  protected static void registerEffect()
  {
    register(
            SPEED, SLOWNESS, HASTE, MINING_FATIGUE, STRENGTH, INSTANT_HEALTH, INSTANT_DAMAGE, JUMP_BOOST, NAUSEA, REGENERATION,
            RESISTANCE, FIRE_RESISTANCE, WATER_BREATHING, INVISIBILITY, BLINDNESS, NIGHT_VISION, HUNGER, WEAKNESS, POISON, WITHER,
            HEALTH_BOOST, ABSORPTION, SATURATION, GLOWING, LEVITATION, LUCK, UNLUCK, SLOW_FALLING, CONDUIT_POWER, DOLPHINS_GRACE,
            BAD_OMEN, HERO_OF_THE_VILLAGE, DARKNESS,
            POISON_M
    );
  }
}

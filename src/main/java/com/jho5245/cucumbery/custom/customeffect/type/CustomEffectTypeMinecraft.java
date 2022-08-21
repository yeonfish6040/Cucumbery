package com.jho5245.cucumbery.custom.customeffect.type;

import org.bukkit.NamespacedKey;
import org.bukkit.potion.PotionEffectType;

public class CustomEffectTypeMinecraft extends CustomEffectType
{
  public static final CustomEffectType
          MINECRAFT_SPEED = new CustomEffectType(NamespacedKey.minecraft("speed"), PotionEffectType.SPEED.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          MINECRAFT_SLOWNESS = new CustomEffectType(NamespacedKey.minecraft("slowness"), PotionEffectType.SLOW.translationKey(), builder().removeOnMilk().negative().maxAmplifier(255)),
          MINECRAFT_HASTE = new CustomEffectType(NamespacedKey.minecraft("haste"), PotionEffectType.FAST_DIGGING.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          MINECRAFT_MINING_FATIGUE = new CustomEffectType(NamespacedKey.minecraft("mining_fatigue"), PotionEffectType.SLOW_DIGGING.translationKey(), builder().removeOnMilk().negative().maxAmplifier(255)),
          MINECRAFT_STRENGTH = new CustomEffectType(NamespacedKey.minecraft("strength"), PotionEffectType.INCREASE_DAMAGE.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          MINECRAFT_INSTANT_HEALTH = new CustomEffectType(NamespacedKey.minecraft("instant_health"), PotionEffectType.HEAL.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          MINECRAFT_INSTANT_DAMAGE = new CustomEffectType(NamespacedKey.minecraft("instant_damage"), PotionEffectType.HARM.translationKey(), builder().removeOnMilk().negative().maxAmplifier(255)),
          MINECRAFT_JUMP_BOOST = new CustomEffectType(NamespacedKey.minecraft("jump_boost"), PotionEffectType.JUMP.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          MINECRAFT_NAUSEA = new CustomEffectType(NamespacedKey.minecraft("nausea"), PotionEffectType.CONFUSION.translationKey(), builder().removeOnMilk().negative().maxAmplifier(255)),
          MINECRAFT_REGENERATION = new CustomEffectType(NamespacedKey.minecraft("regeneration"), PotionEffectType.REGENERATION.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          MINECRAFT_RESISTANCE = new CustomEffectType(NamespacedKey.minecraft("resistance"), PotionEffectType.DAMAGE_RESISTANCE.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          MINECRAFT_FIRE_RESISTANCE = new CustomEffectType(NamespacedKey.minecraft("fire_resistance"), PotionEffectType.FIRE_RESISTANCE.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          MINECRAFT_WATER_BREATHING = new CustomEffectType(NamespacedKey.minecraft("water_breathing"), PotionEffectType.WATER_BREATHING.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          MINECRAFT_INVISIBILITY = new CustomEffectType(NamespacedKey.minecraft("invisibility"), PotionEffectType.INVISIBILITY.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          MINECRAFT_BLINDNESS = new CustomEffectType(NamespacedKey.minecraft("blindness"), PotionEffectType.BLINDNESS.translationKey(), builder().removeOnMilk().negative().maxAmplifier(255)),
          MINECRAFT_NIGHT_VISION = new CustomEffectType(NamespacedKey.minecraft("night_vision"), PotionEffectType.NIGHT_VISION.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          MINECRAFT_HUNGER = new CustomEffectType(NamespacedKey.minecraft("hunger"), PotionEffectType.HUNGER.translationKey(), builder().removeOnMilk().negative().maxAmplifier(255)),
          MINECRAFT_WEAKNESS = new CustomEffectType(NamespacedKey.minecraft("weakness"), PotionEffectType.WEAKNESS.translationKey(), builder().removeOnMilk().negative().maxAmplifier(255)),
          MINECRAFT_POISON = new CustomEffectType(NamespacedKey.minecraft("poison"), PotionEffectType.POISON.translationKey(), builder().removeOnMilk().negative().maxAmplifier(255)),
          MINECRAFT_WITHER = new CustomEffectType(NamespacedKey.minecraft("wither"), PotionEffectType.WITHER.translationKey(), builder().removeOnMilk().negative().maxAmplifier(255)),
          MINECRAFT_HEALTH_BOOST = new CustomEffectType(NamespacedKey.minecraft("health_boost"), PotionEffectType.HEALTH_BOOST.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          MINECRAFT_ABSORPTION = new CustomEffectType(NamespacedKey.minecraft("absorption"), PotionEffectType.ABSORPTION.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          MINECRAFT_SATURATION = new CustomEffectType(NamespacedKey.minecraft("saturation"), PotionEffectType.SATURATION.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          MINECRAFT_GLOWING = new CustomEffectType(NamespacedKey.minecraft("glowing"), PotionEffectType.GLOWING.translationKey(), builder().removeOnMilk().negative().maxAmplifier(255)),
          MINECRAFT_LEVITATION = new CustomEffectType(NamespacedKey.minecraft("levitation"), PotionEffectType.LEVITATION.translationKey(), builder().removeOnMilk().negative().maxAmplifier(255)),
          MINECRAFT_LUCK = new CustomEffectType(NamespacedKey.minecraft("luck"), PotionEffectType.LUCK.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          MINECRAFT_UNLUCK = new CustomEffectType(NamespacedKey.minecraft("unluck"), PotionEffectType.UNLUCK.translationKey(), builder().removeOnMilk().negative().maxAmplifier(255)),
          MINECRAFT_SLOW_FALLING = new CustomEffectType(NamespacedKey.minecraft("slow_falling"), PotionEffectType.SLOW_FALLING.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          MINECRAFT_CONDUIT_POWER = new CustomEffectType(NamespacedKey.minecraft("conduit_power"), PotionEffectType.CONDUIT_POWER.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          MINECRAFT_DOLPHINS_GRACE = new CustomEffectType(NamespacedKey.minecraft("dolphins_grace"), PotionEffectType.DOLPHINS_GRACE.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          MINECRAFT_BAD_OMEN = new CustomEffectType(NamespacedKey.minecraft("bad_omen"), PotionEffectType.BAD_OMEN.translationKey(), builder().removeOnMilk().negative().maxAmplifier(255)),
          MINECRAFT_HERO_OF_THE_VILLAGE = new CustomEffectType(NamespacedKey.minecraft("hero_of_the_village"), PotionEffectType.HERO_OF_THE_VILLAGE.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          MINECRAFT_DARKNESS = new CustomEffectType(NamespacedKey.minecraft("darkness"), PotionEffectType.DARKNESS.translationKey(), builder().removeOnMilk().negative().maxAmplifier(255)),
          MINECRAFT_POISON_M = new CustomEffectType(NamespacedKey.minecraft("poison_m"), PotionEffectType.POISON.translationKey(), builder().negative().enumHidden().maxAmplifier(255));

  protected static void registerEffect()
  {
    register(
            MINECRAFT_SPEED, MINECRAFT_SLOWNESS, MINECRAFT_HASTE, MINECRAFT_MINING_FATIGUE, MINECRAFT_STRENGTH, MINECRAFT_INSTANT_HEALTH, MINECRAFT_INSTANT_DAMAGE, MINECRAFT_JUMP_BOOST, MINECRAFT_NAUSEA, MINECRAFT_REGENERATION,
            MINECRAFT_RESISTANCE, MINECRAFT_FIRE_RESISTANCE, MINECRAFT_WATER_BREATHING, MINECRAFT_INVISIBILITY, MINECRAFT_BLINDNESS, MINECRAFT_NIGHT_VISION, MINECRAFT_HUNGER, MINECRAFT_WEAKNESS, MINECRAFT_POISON, MINECRAFT_WITHER,
            MINECRAFT_HEALTH_BOOST, MINECRAFT_ABSORPTION, MINECRAFT_SATURATION, MINECRAFT_GLOWING, MINECRAFT_LEVITATION, MINECRAFT_LUCK, MINECRAFT_UNLUCK, MINECRAFT_SLOW_FALLING, MINECRAFT_CONDUIT_POWER, MINECRAFT_DOLPHINS_GRACE,
            MINECRAFT_BAD_OMEN, MINECRAFT_HERO_OF_THE_VILLAGE, MINECRAFT_DARKNESS,
            MINECRAFT_POISON_M
    );
  }
}

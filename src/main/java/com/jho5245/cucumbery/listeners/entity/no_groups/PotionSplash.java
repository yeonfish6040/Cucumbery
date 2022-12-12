package com.jho5245.cucumbery.listeners.entity.no_groups;

import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect.DisplayType;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectAbstractApplyEvent.ApplyReason;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Collection;

public class PotionSplash implements Listener
{
  @EventHandler
  public void onPotionSplash(PotionSplashEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    this.customEffect(event);
  }

  private void customEffect(PotionSplashEvent event)
  {
    ThrownPotion thrownPotion = event.getPotion();
    ItemStack itemStack = thrownPotion.getItem();
    NBTCompoundList potionsTag = NBTAPI.getCompoundList(NBTAPI.getMainCompound(itemStack), CucumberyTag.CUSTOM_EFFECTS);
    if (potionsTag != null && !potionsTag.isEmpty())
    {
      boolean hasVanillaEffects = false;
      if (itemStack.getItemMeta() instanceof PotionMeta potionMeta)
      {
        hasVanillaEffects = potionMeta.hasCustomEffects();
        PotionType potionType = potionMeta.getBasePotionData().getType();
        hasVanillaEffects = hasVanillaEffects || !(potionType == PotionType.AWKWARD || potionType == PotionType.MUNDANE || potionType == PotionType.THICK || potionType == PotionType.UNCRAFTABLE
                || potionType == PotionType.WATER);
      }
      Collection<Entity> entities = new ArrayList<>(event.getAffectedEntities());
      for (Entity entity : thrownPotion.getNearbyEntities(5, 5, 5))
      {
        if (!entities.contains(entity))
        {
          entities.add(entity);
        }
      }
      entities.removeIf(e -> e.getLocation().distance(thrownPotion.getLocation()) > 5d);
      for (ReadWriteNBT potionTag : potionsTag)
      {
        try
        {
          for (Entity entity : entities)
          {
            double intensity = Math.min(1d, Math.max(0d, entity instanceof LivingEntity livingEntity ? event.getIntensity(livingEntity) : (1.1d - entity.getLocation().distance(thrownPotion.getLocation()) / 5d)));
            if (entity instanceof LivingEntity livingEntity && intensity == 0 && !hasVanillaEffects)
            {
              intensity = Math.min(1d, Math.max(0.01d, 1.1d - livingEntity.getEyeLocation().distance(thrownPotion.getLocation()) / 5d));
            }
            int duration = potionTag.getInteger(CucumberyTag.CUSTOM_EFFECTS_DURATION);
            CustomEffectManager.addEffect(entity, new CustomEffect(
                    CustomEffectType.valueOf(potionTag.getString(CucumberyTag.CUSTOM_EFFECTS_ID)),
                    duration == -1 ? -1 : Math.max(1, (int) (duration * intensity)),
                    potionTag.getInteger(CucumberyTag.CUSTOM_EFFECTS_AMPLIFIER),
                    DisplayType.valueOf(potionTag.getString(CucumberyTag.CUSTOM_EFFECTS_DISPLAY_TYPE).toUpperCase())
            ), ApplyReason.SPLASH_POTION);
          }
        }
        catch (Exception ignored)
        {

        }
      }
    }
  }
}

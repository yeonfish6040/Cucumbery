package com.jho5245.cucumbery.listeners.entity.no_groups;

import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect.DisplayType;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectType;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.data.Variable;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.bukkit.inventory.ItemStack;

public class AreaEffectCloudApply implements Listener
{
  @EventHandler
  public void onAreaEffectCloudApply(AreaEffectCloudApplyEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    AreaEffectCloud areaEffectCloud = event.getEntity();
    ItemStack itemStack = Variable.projectile.get(areaEffectCloud.getUniqueId());
    NBTCompoundList potionsTag = NBTAPI.getCompoundList(NBTAPI.getMainCompound(itemStack), CucumberyTag.CUSTOM_EFFECTS);
    if (potionsTag != null && !potionsTag.isEmpty())
    {
      for (NBTCompound potionTag : potionsTag)
      {
        try
        {
          CustomEffect customEffect = new CustomEffect(
                  CustomEffectType.valueOf(potionTag.getString(CucumberyTag.CUSTOM_EFFECTS_ID).toUpperCase()),
                  potionTag.getInteger(CucumberyTag.CUSTOM_EFFECTS_DURATION),
                  potionTag.getInteger(CucumberyTag.CUSTOM_EFFECTS_AMPLIFIER),
                  DisplayType.valueOf(potionTag.getString(CucumberyTag.CUSTOM_EFFECTS_DISPLAY_TYPE).toUpperCase()));
          for (Entity entity : event.getAffectedEntities())
          {
            CustomEffectManager.addEffect(entity, customEffect);
          }
        }
        catch (Exception ignored)
        {

        }
      }
    }
  }
}

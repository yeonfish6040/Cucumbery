package com.jho5245.cucumbery.listeners.block;

import com.destroystokyo.paper.event.block.BeaconEffectEvent;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.util.blockplacedata.BlockPlaceDataConfig;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.*;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class BeaconEffect implements Listener
{
  @EventHandler
  public void onBeaconEffect(BeaconEffectEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Block block = event.getBlock();
    Location location = block.getLocation();
    PotionEffect potionEffect = event.getEffect();
    BlockPlaceDataConfig blockPlaceDataConfig = BlockPlaceDataConfig.getInstance(location.getChunk());
    if (blockPlaceDataConfig != null)
    {
      String dataString = blockPlaceDataConfig.getRawData(location);
      if (dataString != null && dataString.length() - dataString.replace("%", "").length() >= 2)
      {
        dataString = PlaceHolderUtil.placeholder(Bukkit.getConsoleSender(), dataString, null);
      }
      ItemStack dataItem = ItemSerializer.deserialize(dataString);
      if (!dataItem.getType().isAir())
      {
        String expireDate = NBTAPI.getString(NBTAPI.getMainCompound(dataItem), CucumberyTag.EXPIRE_DATE_KEY);
        if (expireDate != null)
        {
          if (Method.isTimeUp(dataItem, expireDate))
          {
            MessageUtil.sendWarn(event.getPlayer(), "%s에 있는 신호기 (%s)의 유효기간이 다 되어 능력을 상실했습니다!", location, dataItem);
            event.setCancelled(true);
            return;
          }
          if (expireDate.startsWith("~"))
          {
            blockPlaceDataConfig.set(location, ItemSerializer.serialize(dataItem));
          }
        }
        NBTItem dataNBTItem = new NBTItem(dataItem);
        String customPotionEffectType = dataNBTItem.getString("CustomPotionEffectType");
        if (!customPotionEffectType.contains(":"))
        {
          customPotionEffectType = "cucumbery:" + customPotionEffectType;
        }
        try
        {
          CustomEffectType customEffectType = CustomEffectType.getByKey(Objects.requireNonNull(NamespacedKey.fromString(customPotionEffectType)));
          CustomEffectManager.addEffect(event.getPlayer(), new CustomEffect(Objects.requireNonNull(customEffectType), potionEffect.getDuration()));
          event.setCancelled(true);
          return;
        }
        catch (Exception ignored)
        {

        }
        String potionEffectType = dataNBTItem.getString("PotionEffectType");
        try
        {
          PotionEffectType effectType = PotionEffectType.getByKey(NamespacedKey.minecraft(potionEffectType));
          event.setEffect(new PotionEffect(Objects.requireNonNull(effectType), potionEffect.getDuration(), potionEffect.getAmplifier(), potionEffect.isAmbient(), potionEffect.hasParticles(), potionEffect.hasIcon()));
        }
        catch (Exception ignored)
        {

        }
      }
    }
  }
}

package com.jho5245.cucumbery.listeners.player.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Variable;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.UUID;

public class PlayerFish implements Listener
{
  @EventHandler
  public void onPlayerFish(PlayerFishEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();
    State state = event.getState();
    ItemStack item = ItemStackUtil.getPlayerUsingItem(player, Material.FISHING_ROD);
    if (ItemStackUtil.itemExists(item))
    {
      if (state == State.CAUGHT_FISH && NBTAPI.isRestricted(player, item, Constant.RestrictionType.NO_FISH))
      {
        event.setCancelled(true);
        if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerFishAlertCooldown.contains(uuid))
        {
          Variable.playerFishAlertCooldown.add(uuid);
          MessageUtil.sendTitle(player, "&c낚시 불가!", "&r사용할 수 없는 낚싯대입니다", 5, 80, 15);
          SoundPlay.playSound(player, Constant.ERROR_SOUND);
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerFishAlertCooldown.remove(uuid), 100L);
        }
        return;
      }

      if (state == State.CAUGHT_FISH && event.getCaught() instanceof Item caughtItem && CustomEnchant.isEnabled() && item.getItemMeta().hasEnchant(CustomEnchant.FRANTIC_LUCK_OF_THE_SEA))
      {
        ItemStack i = caughtItem.getItemStack();
        i.setAmount(i.getAmount() * 2);
        caughtItem.setItemStack(i);
      }
    }

    NBTCompound customItemTag = NBTAPI.getCompound(NBTAPI.getMainCompound(item), CucumberyTag.CUSTOM_ITEM_TAG_KEY);
    String customItemId = NBTAPI.getString(customItemTag, CucumberyTag.ID_KEY);
    if (customItemId != null && customItemId.equals("fishingrod"))
    {
      NBTCompound customItemTagCompound = NBTAPI.getCompound(customItemTag, CucumberyTag.TAG_KEY);
      switch (state)
      {
        case FISHING:
          if (customItemTagCompound != null && customItemTagCompound.hasKey(CucumberyTag.CUSTOM_ITEM_FISHING_ROD_MULTIPLIER))
          {
            Double doubleObj = NBTAPI.getDouble(customItemTagCompound, CucumberyTag.CUSTOM_ITEM_FISHING_ROD_MULTIPLIER);
            double multiplier = doubleObj == null ? 0d : doubleObj;
            FishHook fishHook = event.getHook();
            Vector vector = fishHook.getVelocity();
            vector.multiply(multiplier);
            fishHook.setVelocity(vector);
            fishHook.setGlowing(true);
          }
          break;
        case IN_GROUND:
        case CAUGHT_ENTITY:
        case REEL_IN:
          if (customItemTagCompound != null && customItemTagCompound.hasKey(CucumberyTag.CUSTOM_ITEM_FISHING_ROD_MAX_VALUES))
          {
            NBTCompound maxValuesTag = NBTAPI.getCompound(customItemTagCompound, CucumberyTag.CUSTOM_ITEM_FISHING_ROD_MAX_VALUES);
            if (maxValuesTag != null)
            {
              Boolean b = customItemTagCompound.getBoolean(CucumberyTag.CUSTOM_ITEM_FISHING_ROD_ALLOW_ON_AIR);
              if (!b.equals(true) && state == State.REEL_IN)
              {
                return;
              }
              double maxX = maxValuesTag.hasTag("x") ? maxValuesTag.getDouble("x") : 5;
              double maxY = maxValuesTag.hasTag("y") ? maxValuesTag.getDouble("y") : 5;
              double maxZ = maxValuesTag.hasTag("z") ? maxValuesTag.getDouble("z") : 5;
              FishHook fishHook = event.getHook();
              Location hookLoc = fishHook.getLocation(), playerLoc = player.getLocation();
              double x = (hookLoc.getX() - playerLoc.getX()) / 1.5;
              double y = Math.min(2, (hookLoc.getY() - playerLoc.getY()) / 4 + 0.3);
              double z = (hookLoc.getZ() - playerLoc.getZ()) / 1.5;
              if (x > maxX)
              {
                x = maxX;
              }
              if (x < -maxX)
              {
                x = -maxX;
              }
              if (y > maxY)
              {
                y = maxY;
              }
              if (y < -maxY)
              {
                y = -maxY;
              }
              if (z > maxZ)
              {
                z = maxZ;
              }
              if (z < -maxZ)
              {
                z = -maxZ;
              }
              player.setVelocity(new Vector(x, y, z));
            }
          }
          break;
        default:
          break;
      }
    }
  }
}

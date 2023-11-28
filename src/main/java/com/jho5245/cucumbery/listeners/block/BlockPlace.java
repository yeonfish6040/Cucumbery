package com.jho5245.cucumbery.listeners.block;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeCustomMining;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.itemlore.ItemLoreView;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.blockplacedata.BlockPlaceDataConfig;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.Method2;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.*;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTList;
import de.tr7zw.changeme.nbtapi.NBTType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.UUID;

public class BlockPlace implements Listener
{
  @EventHandler
  public void onBlockPlace(BlockPlaceEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Player player = event.getPlayer();
    Block block = event.getBlock();
    if (block.getType() == Material.POWDER_SNOW)
    {
      EquipmentSlot hand = event.getHand();
      ItemStack item = player.getInventory().getItem(hand);
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
              ItemLore.setItemLore(item, ItemLoreView.of(player)), 0L);
    }

    UUID uuid = player.getUniqueId();
    ItemStack item = event.getItemInHand();
    Location location = block.getLocation();
    // 블록 설치 데이터 보존 처리 여부
    boolean special = false;
    if (event.isCancelled())
    {
      return;
    }
    if (Variable.scrollReinforcing.contains(uuid))
    {
      event.setCancelled(true);
      Method.playSound(player, Constant.ERROR_SOUND);
      MessageUtil.sendError(player, "강화중에는 블록을 설치하실 수 없습니다");
      Component a = ComponentUtil.create(Prefix.INFO, "만약 아이템 강화를 중지하시려면 이 문장을 클릭해주세요.").hoverEvent(ComponentUtil.create("클릭하면 강화를 중지합니다")).clickEvent(ClickEvent.runCommand("/강화 quit"));
      player.sendMessage(a);
      return;
    }
    if (!Cucumbery.config.getBoolean("grant-default-permission-to-players") && !Permission.EVENT_BLOCK_PLACE.has(player))
    {
      event.setCancelled(true);
      if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.blockPlaceAlertCooldown.contains(uuid))
      {
        Variable.blockPlaceAlertCooldown.add(uuid);
        MessageUtil.sendTitle(player, "&c설치 불가!", "&r블록을 설치할 권한이 없습니다", 5, 80, 15);
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.blockPlaceAlertCooldown.remove(uuid), 100L);
      }
      return;
    }
    if (!Permission.EVENT2_ANTI_ALLPLAYER.has(player) && Constant.AllPlayer.BLOCK_PLACE.isEnabled())
    {
      event.setCancelled(true);
      if (!Variable.blockPlaceAlertCooldown.contains(uuid))
      {
        Variable.blockPlaceAlertCooldown.add(uuid);
        MessageUtil.sendMessage(player, Prefix.INFO_ALLPLAYER, "블록을 설치할 수 없는 상태입니다");
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.blockPlaceAlertCooldown.remove(uuid), 100L);
      }
      return;
    }
    if (NBTAPI.isRestricted(player, item, Constant.RestrictionType.NO_PLACE))
    {
      event.setCancelled(true);
      if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.blockPlaceAlertCooldown2.contains(uuid))
      {
        Variable.blockPlaceAlertCooldown2.add(uuid);
        MessageUtil.sendTitle(player, "&c설치 불가!", "&r설치할 수 없는 블록입니다", 5, 80, 15);
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.blockPlaceAlertCooldown2.remove(uuid), 100L);
      }
      return;
    }
    if (CustomEffectManager.hasEffect(player, CustomEffectType.CURSE_OF_CREATIVITY) || CustomEffectManager.hasEffect(player, CustomEffectType.CURSE_OF_CREATIVITY_PLACE))
    {
      event.setCancelled(true);
      return;
    }

    if (player.getGameMode() != GameMode.CREATIVE && CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE) && !CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_2_NO_RESTORE))
    {
      event.setCancelled(true);
      return;
    }

    NBTCompound itemTag = NBTAPI.getMainCompound(item);
    NBTList<String> extraTags = NBTAPI.getStringList(itemTag, CucumberyTag.EXTRA_TAGS_KEY);
    if (NBTAPI.arrayContainsValue(extraTags, Constant.ExtraTag.BLOCK_PLACE_BECOME_WATER.toString()))
    {
      special = true;
      if (location.getWorld().getEnvironment() == World.Environment.NETHER)
      {
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
        {
          block.setBlockData(Bukkit.createBlockData(Material.AIR));
          Method.playSoundLocation(location, Sound.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5, 2);
          location.getWorld().spawnParticle(Particle.SMOKE_LARGE, location.add(0.5, 0.5, 0.5), 8, 0.4, 0.4, 0.4, 0);
        }, 0L);
      }
      else
      {
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
        {
          block.setBlockData(Bukkit.createBlockData(Material.AIR));
          block.setBlockData(Bukkit.createBlockData(Material.WATER));
        }, 0L);
      }
    }
    else if (NBTAPI.arrayContainsValue(extraTags, Constant.ExtraTag.BLOCK_PLACE_BECOME_WATER_EVEN_IN_NETHER.toString()))
    {
      special = true;
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        block.setBlockData(Bukkit.createBlockData(Material.AIR));
        block.setBlockData(Bukkit.createBlockData(Material.WATER));
      }, 0L);
    }
    else if (NBTAPI.arrayContainsValue(extraTags, Constant.ExtraTag.BLOCK_PLACE_BECOME_LAVA.toString()))
    {
      special = true;
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        block.setBlockData(Bukkit.createBlockData(Material.AIR));
        block.setBlockData(Bukkit.createBlockData(Material.LAVA));
      }, 0L);
    }
    if (item.getType() == Material.TNT)
    {
      NBTCompound tntTag = NBTAPI.getCompound(itemTag, CucumberyTag.TNT);
      if (tntTag != null)
      {
        if (tntTag.hasTag(CucumberyTag.TNT_IGNITE) && tntTag.getBoolean(CucumberyTag.TNT_IGNITE))
        {
          special = true;
          player.getLocation().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
          TNTPrimed tnt = player.getLocation().getWorld().spawn(event.getBlock().getLocation().add(0.5D, 0D, 0.5D), TNTPrimed.class);
          if (Cucumbery.config.getBoolean("use-static-tnt") && !Method.configContainsLocation(block.getLocation(), Cucumbery.config.getStringList("no-use-static-tnt-location")))
          {
            tnt.setVelocity(new Vector(Cucumbery.config.getDouble("static-tnt-velocity.x"), Cucumbery.config.getDouble("static-tnt-velocity.y"), Cucumbery.config.getDouble("static-tnt-velocity.z")));
          }
          tnt.setSource(player);
          SoundPlay.playSoundLocation(event.getBlock().getLocation(), Sound.ENTITY_CREEPER_PRIMED);
          int fuseTime = 80;
          if (tntTag.hasTag(CucumberyTag.TNT_FUSE))
          {
            try
            {
              fuseTime = tntTag.getInteger(CucumberyTag.TNT_FUSE);
            }
            catch (Exception ignored)
            {
            }
            tnt.setFuseTicks(fuseTime);
          }

          Boolean fire = tntTag.getBoolean(CucumberyTag.TNT_FIRE);
          if (fire != null && fire)
          {
            tnt.setIsIncendiary(true);
          }

          if (tntTag.hasTag(CucumberyTag.TNT_EXPLODE_POWER) && tntTag.getType(CucumberyTag.TNT_EXPLODE_POWER) == NBTType.NBTTagDouble)
          {
            double explodePower = tntTag.getDouble(CucumberyTag.TNT_EXPLODE_POWER);
            tnt.setYield((float) explodePower);
          }
        }
      }
    }
    if (player.getGameMode() != GameMode.CREATIVE && NBTAPI.arrayContainsValue(NBTAPI.getStringList(NBTAPI.getMainCompound(item), CucumberyTag.EXTRA_TAGS_KEY),
            Constant.ExtraTag.INFINITE.toString()))
    {
      Variable.playerItemConsumeCauseSwapCooldown.add(uuid);
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerItemConsumeCauseSwapCooldown.remove(uuid), 0L);
      ItemStack finalItem = item.clone();
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> player.getInventory().setItem(event.getHand(), finalItem), 0L);
    }

    CustomMaterial customMaterial = CustomMaterial.itemStackOf(item);
    if (customMaterial == CustomMaterial.REDSTONE_BLOCK_INSTA_BREAK)
    {
      Location locationClone = location.clone().add(0.5, 0.5, 0.5);
      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> {
        if (block.getType() == Material.REDSTONE_BLOCK)
        {
          block.setType(Material.AIR);
          BlockData blockData = Material.REDSTONE_BLOCK.createBlockData();
          SoundGroup soundGroup = blockData.getSoundGroup();
          block.getWorld().playSound(location, soundGroup.getBreakSound(), SoundCategory.BLOCKS, 1f, soundGroup.getPitch() * 0.8f);
          BoundingBox boundingBox = block.getBoundingBox();
          double minX = boundingBox.getMinX(), maxX = boundingBox.getMaxX();
          double minY = boundingBox.getMinY(), maxY = Math.min(1d, boundingBox.getMaxY());
          double minZ = boundingBox.getMinZ(), maxZ = boundingBox.getMaxZ();
          double diffX = maxX - minX, diffY = maxY - minY, diffZ = maxZ - minZ;
          minX += 0.2 * diffX;
          maxX -= 0.2 * diffX;
          minY += 0.2 * diffY;
          maxY -= 0.2 * diffY;
          minZ += 0.2 * diffZ;
          maxZ -= 0.2 * diffZ;
          for (Player online : Bukkit.getOnlinePlayers())
          {
            if (!online.getWorld().getName().equals(location.getWorld().getName()))
            {
              continue;
            }
            online.spawnParticle(Particle.BLOCK_CRACK, locationClone.clone().add(minX, minY, minZ), 5, 0, 0, 0, 0, blockData);

            online.spawnParticle(Particle.BLOCK_CRACK, locationClone.clone().add(maxX, minY, minZ), 5, 0, 0, 0, 0, blockData);
            online.spawnParticle(Particle.BLOCK_CRACK, locationClone.clone().add(minX, maxY, minZ), 5, 0, 0, 0, 0, blockData);
            online.spawnParticle(Particle.BLOCK_CRACK, locationClone.clone().add(minX, minY, maxZ), 5, 0, 0, 0, 0, blockData);

            online.spawnParticle(Particle.BLOCK_CRACK, locationClone.clone().add(maxX, maxY, minZ), 5, 0, 0, 0, 0, blockData);
            online.spawnParticle(Particle.BLOCK_CRACK, locationClone.clone().add(maxX, minY, maxZ), 5, 0, 0, 0, 0, blockData);
            online.spawnParticle(Particle.BLOCK_CRACK, locationClone.clone().add(minX, maxY, maxZ), 5, 0, 0, 0, 0, blockData);

            online.spawnParticle(Particle.BLOCK_CRACK, locationClone.clone().add(maxX, maxY, maxZ), 5, 0, 0, 0, 0, blockData);
          }
          if (player.getGameMode() != GameMode.CREATIVE)
          {
            ItemStack itemStack = event.getItemInHand().clone();
            itemStack.setAmount(1);
            block.getWorld().dropItemNaturally(location, itemStack);
          }
        }
      }, 2L);
      return;
    }

    if (!special && Cucumbery.config.getBoolean("use-block-place-data-feature") && !Method.configContainsLocation(location, Cucumbery.config.getStringList("no-use-block-place-data-feature-location")))
    {
      item = item.clone();
      ItemLore.removeItemLore(item);
      NBTItem nbtItem = new NBTItem(item, true);
      if (!NBTAPI.arrayContainsValue(extraTags, Constant.ExtraTag.PRESERVE_BLOCK_ENTITY_TAG))
      {
        nbtItem.removeKey("BlockEntityTag");
      }
      if (!NBTAPI.arrayContainsValue(extraTags, Constant.ExtraTag.PRESERVE_BLOCK_DATA_TAG))
      {
        nbtItem.removeKey("BlockStateTag");
      }
      NBTList<String> extraTag = NBTAPI.getStringList(NBTAPI.getMainCompound(item), CucumberyTag.EXTRA_TAGS_KEY);
      if (item.hasItemMeta() && (NBTAPI.arrayContainsValue(extraTag, Constant.ExtraTag.PRESERVE_BLOCK_NBT) || !Cucumbery.config.getBoolean("block-place-data-feature-with-specific-tag")))
      {
        item.setAmount(1);
        if (nbtItem.hasTag("perspectiveYaw") && nbtItem.getType("perspectiveYaw") == NBTType.NBTTagByte && nbtItem.getBoolean("perspectiveYaw"))
        {
          float yaw = Math.round((player.getLocation().getYaw() - 180f) / 22.5f) * 22.5f;
          NBTList<Float> rotation = nbtItem.addCompound("displays").getFloatList("rotation");
          if (rotation.isEmpty())
          {
            rotation.add(yaw);
            rotation.add(0f);
          }
          else
          {
            rotation.set(0, yaw);
          }
        }
        if (nbtItem.hasTag("perspectivePitch") && nbtItem.getType("perspectivePitch") == NBTType.NBTTagByte && nbtItem.getBoolean("perspectivePitch"))
        {
          float pitch = Math.round(-player.getLocation().getPitch() / 22.5f) * 22.5f;
          NBTList<Float> rotation = nbtItem.addCompound("displays").getFloatList("rotation");
          if (rotation.isEmpty())
          {
            rotation.add(0f);
            rotation.add(pitch);
          }
          else
          {
            rotation.set(1, pitch);
          }
        }
        if (nbtItem.hasTag("perspectiveGrid") && nbtItem.getType("perspectiveGrid") == NBTType.NBTTagByte && nbtItem.getBoolean("perspectiveGrid"))
        {
          float yaw = Math.round((player.getLocation().getYaw() - 180f) / 90f) * 90f;
          float pitch = Math.round(-player.getLocation().getPitch() / 90f) * 90f;
          NBTList<Float> rotation = nbtItem.addCompound("displays").getFloatList("rotation");
          if (rotation.isEmpty())
          {
            rotation.add(yaw);
            rotation.add(pitch);
          }
          else
          {
            rotation.set(0, yaw);
            rotation.set(1, pitch);
          }
        }
        BlockPlaceDataConfig.setItem(location, item);
        BlockPlaceDataConfig.spawnItemDisplay(location);
        if (nbtItem.hasTag("change_material") && nbtItem.hasTag("displays"))
        {
          String materialString = nbtItem.hasTag("change_material") && nbtItem.getType("change_material") == NBTType.NBTTagString ? nbtItem.getString("change_material") : "";
          Material material = Method2.valueOf(materialString, Material.class);
          block.setType(material != null ? material : Material.WHITE_STAINED_GLASS);
        }
      }
      else
      {
        BlockPlaceDataConfig.removeData(location);
      }
    }
  }
}

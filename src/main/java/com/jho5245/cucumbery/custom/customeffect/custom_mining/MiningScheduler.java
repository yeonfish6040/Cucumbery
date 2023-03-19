package com.jho5245.cucumbery.custom.customeffect.custom_mining;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.children.group.LocationCustomEffect;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeCustomMining;
import com.jho5245.cucumbery.events.block.CustomBlockBreakEvent;
import com.jho5245.cucumbery.util.additemmanager.AddItemUtil;
import com.jho5245.cucumbery.util.blockplacedata.BlockPlaceDataConfig;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.itemlore.ItemLoreView;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method2;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.ShulkerBox;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MiningScheduler
{
  public static final HashMap<UUID, Integer> blockBreakKey = new HashMap<>();

  public static void customMining()
  {
    customMining(null, false);
  }

  public static void resetCooldowns(@NotNull World world)
  {
    HashMap<Location, Long> map = Variable.customMiningCooldown;
    map.keySet().removeIf(location ->
    {
      if (location.getWorld().getName().equals(world.getName()))
      {
        Variable.customMiningExtraBlocks.remove(location);
        if (Variable.customMiningExtraBlocksTask.containsKey(location))
        {
          Variable.customMiningExtraBlocksTask.get(location).cancel();
        }
        if (!Variable.fakeBlocks.containsKey(location))
        {
          location.getBlock().getState().update();
        }
        else
        {
          Collection<? extends Player> players = Bukkit.getOnlinePlayers();
          for (Player player : players)
          {
            if (player.getWorld().getName().equals(location.getWorld().getName()) && location.distance(player.getLocation()) <= Cucumbery.config.getDouble("custom-mining.maximum-block-packet-distance"))
            {
              player.sendBlockChange(location, Variable.fakeBlocks.get(location));
            }
          }
        }
        return true;
      }
      return false;
    });
  }

  public static void resetCooldowns(@NotNull Location from, Location to)
  {
    HashMap<Location, Long> map = Variable.customMiningCooldown;
    map.keySet().removeIf(location ->
    {
      int fromX = from.getBlockX(), fromY = from.getBlockY(), fromZ = from.getBlockZ();
      int toX = to.getBlockX(), toY = to.getBlockY(), toZ = to.getBlockZ();
      int minX = Math.min(fromX, toX), minY = Math.min(fromY, toY), minZ = Math.min(fromZ, toZ);
      int maxX = Math.max(fromX, toX), maxY = Math.max(fromY, toY), maxZ = Math.max(fromZ, toZ);
      int x = location.getBlockX(), y = location.getBlockY(), z = location.getBlockZ();
      if (x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ)
      {
        Variable.customMiningExtraBlocks.remove(location);
        if (Variable.customMiningExtraBlocksTask.containsKey(location))
        {
          Variable.customMiningExtraBlocksTask.get(location).cancel();
        }
        if (!Variable.fakeBlocks.containsKey(location))
        {
          location.getBlock().getState().update();
        }
        else
        {
          Collection<? extends Player> players = Bukkit.getOnlinePlayers();
          for (Player player : players)
          {
            if (player.getWorld().getName().equals(location.getWorld().getName()) && location.distance(player.getLocation()) <= Cucumbery.config.getDouble("custom-mining.maximum-block-packet-distance"))
            {
              player.sendBlockChange(location, Variable.fakeBlocks.get(location));
            }
          }
        }
        return true;
      }
      return false;
    });
  }

  public static void customMiningTicks()
  {
    HashMap<Location, Long> map = Variable.customMiningCooldown;
    for (Location location : new ArrayList<>(map.keySet()))
    {
      if (map.containsKey(location))
      {
        long value = map.get(location);
        map.put(location, value - 1);
      }
    }
    map.keySet().removeIf(location ->
    {
      if (map.get(location) <= 0 && !Variable.customMiningExtraBlocks.containsKey(location))
      {
        if (!Variable.fakeBlocks.containsKey(location))
        {
          location.getBlock().getState().update();
        }
        else
        {
          Collection<? extends Player> players = Bukkit.getOnlinePlayers();
          for (Player player : players)
          {
            if (player.getWorld().getName().equals(location.getWorld().getName()) && location.distance(player.getLocation()) <= Cucumbery.config.getDouble("custom-mining.maximum-block-packet-distance"))
            {
              player.sendBlockChange(location, Variable.fakeBlocks.get(location));
            }
          }
        }
        return true;
      }
      return false;
    });
  }

  public static void customMining(@Nullable Player target, boolean distanceLimit)
  {
    HashMap<Location, Long> map = Variable.customMiningCooldown;
    for (Location location : new ArrayList<>(map.keySet()))
    {
      if (map.containsKey(location))
      {
        if (map.get(location) <= 0)
        {
          continue;
        }
        Collection<? extends Player> players = target == null ? Bukkit.getOnlinePlayers() : Collections.singletonList(target);
        players.forEach(p ->
                {
                  if (p.getWorld().getName().equals(location.getWorld().getName()) && (!distanceLimit || p.getLocation().distance(location) <= Cucumbery.config.getDouble("custom-mining.maximum-block-packet-distance")))
                  {
                    Block block = location.getBlock();
                    Material blockType = block.getType();
                    BlockData blockData = Bukkit.createBlockData(Material.AIR);
                    if (blockType.isCollidable())
                    {
                      Material displayBlock = UserData.CUSTOM_MINING_COOLDOWN_DISPLAY_BLOCK.getMaterial(p);
                      if (displayBlock == null)
                      {
                        displayBlock = Material.BEDROCK;
                      }
                      blockData = Bukkit.createBlockData(blockType == displayBlock ? Material.BEDROCK : displayBlock);
                      if (Tag.STAIRS.isTagged(blockType) && blockType != Material.BLACKSTONE_STAIRS)
                      {
                        final BlockData finalBlockData = block.getBlockData();
                        blockData = Bukkit.createBlockData(Material.BLACKSTONE_STAIRS, data ->
                        {
                          Stairs stairs = (Stairs) data;
                          stairs.setShape(((Stairs) finalBlockData).getShape());
                          stairs.setHalf(((Stairs) finalBlockData).getHalf());
                          stairs.setFacing(((Stairs) finalBlockData).getFacing());
                        });
                      }
                      if (Tag.SLABS.isTagged(blockType) && blockType != Material.BLACKSTONE_SLAB)
                      {
                        final BlockData finalBlockData = block.getBlockData();
                        blockData = Bukkit.createBlockData(Material.BLACKSTONE_SLAB, data ->
                        {
                          Slab slab = (Slab) data;
                          slab.setType(((Slab) finalBlockData).getType());
                        });
                      }
                      if (Tag.WALLS.isTagged(blockType) && blockType != Material.BLACKSTONE_WALL)
                      {
                        final BlockData finalBlockData = block.getBlockData();
                        blockData = Bukkit.createBlockData(Material.BLACKSTONE_WALL, data ->
                        {
                          Wall wall = (Wall) data;
                          wall.setUp(((Wall) finalBlockData).isUp());
                          for (BlockFace blockFace : BlockFace.values())
                          {
                            try
                            {
                              wall.setHeight(blockFace, ((Wall) finalBlockData).getHeight(blockFace));
                            }
                            catch (Exception ignored)
                            {

                            }
                          }
                        });
                      }
                      if (block.getBlockData() instanceof MultipleFacing multipleFacing)
                      {
                        if (multipleFacing instanceof Fence || multipleFacing instanceof GlassPane)
                        {
                          blockData = Bukkit.createBlockData(Material.BLACKSTONE_WALL);
                        }
                        else
                        {
                          blockData = Bukkit.createBlockData(Material.BEDROCK);
                        }
                      }
                      if ((Tag.WALLS.isTagged(blockType) || Tag.STAIRS.isTagged(blockType) || Tag.SLABS.isTagged(blockType) || block.getBlockData() instanceof MultipleFacing) && block.getBlockData() instanceof Waterlogged waterlogged)
                      {
                        ((Waterlogged) blockData).setWaterlogged(waterlogged.isWaterlogged());
                      }
                    }
                    if (Variable.customMiningExtraBlocks.containsKey(location))
                    {
                      blockData = Variable.customMiningExtraBlocks.get(location);
                    }
                    p.sendBlockChange(location, blockData);
                  }
                }
        );
      }
    }
    Collection<? extends Player> players = target == null ? Bukkit.getOnlinePlayers() : Collections.singletonList(target);
    players.forEach(p ->
    {
      for (Location location : Variable.customMiningExtraBlocks.keySet())
      {
        if (p.getWorld().getName().equals(location.getWorld().getName()) && (!distanceLimit || p.getLocation().distance(location) <= Cucumbery.config.getDouble("custom-mining.maximum-block-packet-distance")))
        {
          p.sendBlockChange(location, Variable.customMiningExtraBlocks.get(location));
        }
      }
    });
  }

  private static void customMiningPre(@NotNull Player player)
  {
    if (Cucumbery.config.getBoolean("custom-mining.enable-wild-mode"))
    {
      player.addScoreboardTag(Cucumbery.config.getString("custom-mining.tag", "cucumbery_miner"));
      player.addScoreboardTag(Cucumbery.config.getString("custom-mining.tag-3", "cucumbery_miner_3"));
      CustomEffectManager.addEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_2_NO_RESTORE_IGNORE_INSTA_BLOCKS);
    }
    if (Cucumbery.config.getBoolean("custom-mining.enable-by-tag"))
    {
      if (player.getScoreboardTags().contains(Cucumbery.config.getString("custom-mining.tag", "cucumbery_miner")) && !CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
      {
        CustomEffectManager.addEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE);
      }
      if (!player.getScoreboardTags().contains(Cucumbery.config.getString("custom-mining.tag", "cucumbery_miner")) && CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
      {
        CustomEffectManager.removeEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE);
      }
      if (player.getScoreboardTags().contains(Cucumbery.config.getString("custom-mining.tag-2", "cucumbery_miner_2")))
      {
        CustomEffectManager.addEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_2);
      }
      if (player.getScoreboardTags().contains(Cucumbery.config.getString("custom-mining.tag-3", "cucumbery_miner_3")))
      {
        CustomEffectManager.addEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_2_NO_RESTORE);
      }
      if (player.getScoreboardTags().contains(Cucumbery.config.getString("custom-mining.predefined-ores-tag.dwarven-gold", "cucumbery_miner_dwarven_gold")))
      {
        CustomEffectManager.addEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_PREDEFINED_CUSTOM_ORE_DWARVEN_GOLDS);
      }
      if (player.getScoreboardTags().contains(Cucumbery.config.getString("custom-mining.predefined-ores-tag.mithril", "cucumbery_miner_mithril")))
      {
        CustomEffectManager.addEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_PREDEFINED_CUSTOM_ORE_MITHRILS);
      }
      if (player.getScoreboardTags().contains(Cucumbery.config.getString("custom-mining.predefined-ores-tag.titanium", "cucumbery_miner_titanium")))
      {
        CustomEffectManager.addEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_PREDEFINED_CUSTOM_ORE_TITANIUMS);
      }
      if (player.getScoreboardTags().contains(Cucumbery.config.getString("custom-mining.predefined-ores-tag.gemstone", "cucumbery_miner_gemstone")))
      {
        CustomEffectManager.addEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_PREDEFINED_CUSTOM_ORE_GEMSTONES);
      }
    }
    if (player.getGameMode() != GameMode.CREATIVE && CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
    {
//      if (Cucumbery.using_ProtocolLib)
//      {
      player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 2, 0, false, false, false));
//      }
//      else
//      {
//        boolean add = false;
//        if (player.hasPotionEffect(PotionEffectType.SLOW_DIGGING))
//        {
//          PotionEffect potionEffect = player.getPotionEffect(PotionEffectType.SLOW_DIGGING);
//          if (potionEffect != null)
//          {
//            int duration = potionEffect.getDuration();
//            if (duration < 2 && !potionEffect.hasParticles() && !potionEffect.hasIcon() && !potionEffect.isAmbient())
//            {
//              add = true;
//            }
//          }
//        }
//        else
//        {
//          add = true;
//        }
//        if (add)
//        {
//          player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 2, 0, false, false, false));
//        }
//      }
      player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 2, 0, false, false, false));
    }
  }

  public static void customMining(@NotNull Player player)
  {
    customMiningPre(player);
    if (CustomEffectManager.getEffectNullable(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_PROGRESS) instanceof LocationCustomEffect effect)
    {
      final Location location = effect.getLocation();
      UUID uuid = player.getUniqueId();
      Block block = location.getBlock();
      final Material blockType = block.getType();
      if (blockType.isAir())
      {
        MiningManager.quitCustomMining(player);
        return;
      }
      final BlockData originData = block.getBlockData().clone();
      final Location locationClone = location.clone();
      ItemStack itemStack = player.getInventory().getItemInMainHand().clone();
      ItemMeta itemMeta = itemStack.getItemMeta();
      // 드릴 연료 1 이하일 시 사용 불가 처리
      if (ItemStackUtil.itemExists(itemStack))
      {
        NBTItem nbtItem = new NBTItem(itemStack);
        CustomMaterial customMaterial = Method2.valueOf(nbtItem.getString("id") + "", CustomMaterial.class);
        if (customMaterial != null)
        {
          switch (customMaterial)
          {
            case TITANIUM_DRILL_R266, TITANIUM_DRILL_R366, TITANIUM_DRILL_R466, TITANIUM_DRILL_R566, MINDAS_DRILL ->
            {
              NBTCompound itemTag = nbtItem.getCompound(CucumberyTag.KEY_MAIN);
              NBTCompound duraTag = itemTag != null ? itemTag.getCompound(CucumberyTag.CUSTOM_DURABILITY_KEY) : null;
              if (duraTag != null)
              {
                long curDura = duraTag.getLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY);
                if (curDura <= 1)
                {
                  MessageUtil.sendWarn(player, "%s의 연료가 다 떨어져서 사용할 수 없습니다!", itemStack);
                  MiningManager.quitCustomMining(player);
                  return;
                }
              }
            }
          }
        }
      }
      MiningResult miningResult = MiningManager.getMiningInfo(player, location);
      // 채굴 정보가 없거나(쿨타임, 지역 보호 등) 블록을 캘 수 없는 상태(해당 도구의 해당 블록 채광 미지원, 블록의 강도가 -1) 처리
      if (miningResult == null || !miningResult.canMine() || miningResult.blockHardness() == -1)
      {
        MiningManager.quitCustomMining(player);
        return;
      }
      List<ItemStack> drops = miningResult.drops();
      CustomMaterial customMaterial = drops.isEmpty() ? null : CustomMaterial.itemStackOf(drops.get(0));
      if (miningResult.blockTier() > miningResult.miningTier())
      {
        if (miningResult.miningTier() > 0 && !Variable.customMiningTierAlertCooldown.contains(uuid))
        {
          MessageUtil.sendWarn(player, "%s을(를) 캐기 위해 더 높은 등급의 도구가 필요합니다!", drops.isEmpty() || customMaterial == null ? block.getType() : drops.get(0));
          Variable.customMiningTierAlertCooldown.add(uuid);
          Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.customMiningTierAlertCooldown.remove(uuid), 20L);
        }
        MiningManager.quitCustomMining(player);
        return;
      }
      // 채굴 진행도 처리
      float origin = Variable.customMiningProgress.getOrDefault(uuid, 0f);
      float damage = miningResult.miningSpeed() / 20f / miningResult.blockHardness();
      Variable.customMiningProgress.put(uuid, origin + damage);
      float progress = Variable.customMiningProgress.getOrDefault(uuid, 0f);
      progress = Math.max(0f, Math.min(1f, progress));
      // 블록이 캐짐
      boolean instaBreak = miningResult.blockHardness() == Float.MIN_VALUE;
      if (progress >= 1 || instaBreak) // insta break
      {
        // 이벤트 호출
        CustomBlockBreakEvent customBlockBreakEvent = new CustomBlockBreakEvent(block, player);
        Bukkit.getPluginManager().callEvent(customBlockBreakEvent);
        boolean mode2 = CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_2);
        boolean mode3 = CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_2_NO_RESTORE);
        // 블록을 캤을 때 소리 재생 및 채광 모드 2/미복구일 경우 블록 파괴 파티클 처리
        {
          SoundGroup soundGroup = block.getBlockSoundGroup();
          if (customMaterial != null)
          {
            switch (customMaterial)
            {
              case RUBY, AMBER, JADE, SAPPHIRE, TOPAZ, MORGANITE -> soundGroup = Material.AMETHYST_CLUSTER.createBlockData().getSoundGroup();
            }
          }
          if (Variable.customMiningExtraBlocks.containsKey(location))
          {
            soundGroup = Variable.customMiningExtraBlocks.get(location).getSoundGroup();
          }
          if (Variable.fakeBlocks.containsKey(location))
          {
            soundGroup = Variable.fakeBlocks.get(location).getSoundGroup();
          }
          Sound breakSound = miningResult.breakSound();
          String breakCustomSound = miningResult.breakCustomSound();
          float volume = 1f, pitch = soundGroup.getPitch() * 0.8f;
          if (miningResult.breakSoundVolume() != 0f)
          {
            volume = miningResult.breakSoundVolume();
          }
          if (miningResult.breakSoundPitch() != 0f)
          {
            pitch = miningResult.breakSoundPitch();
          }
          Sound sound = breakSound != null ? breakSound : soundGroup.getBreakSound();
          for (Player online : Bukkit.getOnlinePlayers())
          {
            if (online.getWorld().getName().equals(player.getWorld().getName()))
            {
              if (breakCustomSound != null && UserData.SERVER_RESOURCEPACK.getBoolean(online))
              {
                online.playSound(locationClone, breakCustomSound, SoundCategory.BLOCKS, volume, pitch);
              }
              else
              {
                online.playSound(locationClone, sound, SoundCategory.BLOCKS, volume, pitch);
              }
            }
          }
          // 채굴 모드 2, 3은 실제로 블록이 부숴졌으므로 블록이 부서지는 입자 출력
          if (mode2 || mode3)
          {
            BlockData blockData = block.getBlockData();
            if (Variable.fakeBlocks.containsKey(location))
            {
              blockData = Variable.fakeBlocks.get(location);
            }
            VoxelShape voxelShape = block.getCollisionShape();
            Collection<BoundingBox> boundingBoxes = voxelShape.getBoundingBoxes();
            int overridenSize = -1;
            if (boundingBoxes.isEmpty())
            {
              switch (blockType)
              {
                case REDSTONE_WIRE -> boundingBoxes.add(new BoundingBox(0d, 0d, 0d, 1d, 0.0625d, 1d));
                case SNOW ->
                {
                  Snow snow = (Snow) block.getBlockData();
                  boundingBoxes.add(new BoundingBox(0d, 0d, 0d, 1d, 0.0625d * snow.getLayers(), 1d));
                }
              }
              if (Tag.FLOWERS.isTagged(blockType))
              {
                boundingBoxes.add(new BoundingBox(0.3d, 0.3d, 0.3d, 1d, 0.0625d, 1d));
                overridenSize = 1;
              }
              if (boundingBoxes.isEmpty())
              {
                boundingBoxes.add(block.getBoundingBox());
              }
            }
            int size = boundingBoxes.size();
            int count = overridenSize != -1 ? overridenSize : (int) (5d / size);
            for (Player online : Bukkit.getOnlinePlayers())
            {
              if (UserData.SHOW_BLOCK_BREAK_PARTICLE_ON_CUSTOM_MINING.getBoolean(online) && online.getWorld().getName().equals(player.getWorld().getName()))
              {
                for (BoundingBox boundingBox : boundingBoxes)
                {
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
                  online.spawnParticle(Particle.BLOCK_CRACK, location.clone().add(minX, minY, minZ), count, 0, 0, 0, 0, blockData);

                  online.spawnParticle(Particle.BLOCK_CRACK, location.clone().add(maxX, minY, minZ), count, 0, 0, 0, 0, blockData);
                  online.spawnParticle(Particle.BLOCK_CRACK, location.clone().add(minX, maxY, minZ), count, 0, 0, 0, 0, blockData);
                  online.spawnParticle(Particle.BLOCK_CRACK, location.clone().add(minX, minY, maxZ), count, 0, 0, 0, 0, blockData);

                  online.spawnParticle(Particle.BLOCK_CRACK, location.clone().add(maxX, maxY, minZ), count, 0, 0, 0, 0, blockData);
                  online.spawnParticle(Particle.BLOCK_CRACK, location.clone().add(maxX, minY, maxZ), count, 0, 0, 0, 0, blockData);
                  online.spawnParticle(Particle.BLOCK_CRACK, location.clone().add(minX, maxY, maxZ), count, 0, 0, 0, 0, blockData);

                  online.spawnParticle(Particle.BLOCK_CRACK, location.clone().add(maxX, maxY, maxZ), count, 0, 0, 0, 0, blockData);
                }
              }
            }
          }
        }
        // 블록을 캤을 때 블록 통계 처리
        {
          player.incrementStatistic(Statistic.MINE_BLOCK, blockType);
        }
        // 블록을 캤을 때 배고픔 감소 처리
        {
          player.setExhaustion(player.getExhaustion() + 0.005f);
        }
        // 블록 드롭 처리(염력 인챈트나 효과가 있으면 인벤토리에 지급 혹은 블록 위치에 아이템 떨굼
        {
          if (itemMeta != null && itemMeta.getEnchantLevel(CustomEnchant.TELEKINESIS) > 0 || CustomEffectManager.hasEffect(player, CustomEffectType.TELEKINESIS))
          {
            AddItemUtil.addItem(player, drops);
          }
          else
          {
            drops.forEach(itemStack1 -> player.getWorld().dropItemNaturally(location, itemStack1));
          }
          if (mode3 && block.getState() instanceof BlockInventoryHolder inventoryHolder && !(inventoryHolder instanceof ShulkerBox))
          {
            Inventory inventory = inventoryHolder.getInventory();
            for (ItemStack content : inventory.getContents())
            {
              if (ItemStackUtil.itemExists(content))
              {
                if (itemMeta != null && itemMeta.getEnchantLevel(CustomEnchant.TELEKINESIS) > 0 || CustomEffectManager.hasEffect(player, CustomEffectType.TELEKINESIS))
                {
                  AddItemUtil.addItem(player, content);
                }
                else
                {
                  player.getWorld().dropItemNaturally(location, content);
                }
              }
            }
          }
        }
        // 경험치 드롭 처리
        {
          float exp = miningResult.exp();
          int intSide = (int) exp;
          float floatSide = exp - intSide;
          if (Math.random() < floatSide)
          {
            intSide++;
          }
          int finalIntSide = intSide;
          if ((!drops.isEmpty() || Arrays.asList(Material.SPAWNER, Material.SCULK, Material.SCULK_CATALYST, Material.SCULK_SENSOR, Material.SCULK_SHRIEKER).contains(block.getType())) && finalIntSide > 0)
          {
            player.getWorld().spawnEntity(location, EntityType.EXPERIENCE_ORB, SpawnReason.CUSTOM, (entity -> ((ExperienceOrb) entity).setExperience(finalIntSide)));
          }
        }
        // 채굴 모드 처리
        {
          // 채굴 모드 2, 3 처리
          if (mode2 || mode3)
          {
            if (mode3)
            {
              Block block1 = locationClone.add(0, -1, 0).getBlock();
              Material block1Type = block1.getType();
              if ((block.getType() == Material.ICE && (block1Type.isSolid() || block1Type == Material.WATER) && drops.isEmpty() && customMaterial == null) ||
                      (block.getBlockData() instanceof Waterlogged waterlogged && waterlogged.isWaterlogged()))
              {
                block.setType(Material.WATER);
              }
              else
              {
                block.setType(Material.AIR);
              }
              BlockPlaceDataConfig.removeData(location);
              Variable.fakeBlocks.remove(location);

//              Scheduler.fakeBlocksAsync(null, location, true); commented since 2022.11.08 due to no reason for calling it
            }
            else
            {
              if (Variable.customMiningMode2BlockDataTask.containsKey(locationClone))
              {
                Variable.customMiningMode2BlockDataTask.get(locationClone).cancel();
              }
              Variable.customMiningMode2BlockData.put(locationClone, originData);
              boolean isWater = originData instanceof Waterlogged waterlogged && waterlogged.isWaterlogged();
              block.setBlockData(Bukkit.createBlockData(isWater ? Material.WATER : Material.AIR), false);
              Variable.customMiningMode2BlockDataTask.put(locationClone, Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
              {
                Variable.customMiningMode2BlockData.remove(locationClone);
                customMining();
                if (isWater && block.getType() == Material.WATER || !isWater && block.getType().isAir())
                {
                  block.setBlockData(originData, false);
                }
                // 채광 모드 2에서는 재생 속도가 느려짐
              }, (long) (miningResult.regenCooldown() * Cucumbery.config.getDouble("custom-mining.mining-mode-2-regen-multiplier"))));
            }
          }
          // 채굴 모드 처리
          else
          {
            boolean extraBlockIgnoreCooldown = false;
            if (Variable.customMiningExtraBlocks.containsKey(locationClone))
            {
              extraBlockIgnoreCooldown = true;
              if (Variable.customMiningExtraBlocksTask.containsKey(locationClone))
              {
                Variable.customMiningExtraBlocksTask.remove(locationClone).cancel();
              }
              Variable.customMiningExtraBlocks.remove(locationClone);
              customMining();
            }
            else if (!drops.isEmpty() && !drops.get(0).getType().isAir())
            {
              if (customMaterial != null)
              {
                switch (customMaterial)
                {
                  case TUNGSTEN_INGOT, TUNGSTEN_ORE ->
                  {
                    if (CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_PREDEFINED_CUSTOM_ORE_CUCUMBERITES))
                    {
                      if (Math.random() > 0.7)
                      {
                        Variable.customMiningExtraBlocks.put(locationClone, Bukkit.createBlockData(Material.GREEN_WOOL));
                        if (Variable.customMiningExtraBlocksTask.containsKey(locationClone))
                        {
                          Variable.customMiningExtraBlocksTask.remove(locationClone).cancel();
                        }
                        customMining();
                      }
                    }
                  }
                }
              }
              else
              {
                boolean hasAllowedBlocks = CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_ALLOWED_BLOCKS);
                if (hasAllowedBlocks)
                {
                  final BlockData blockData = block.getBlockData();
                  switch (block.getType())
                  {
                    case STONE_STAIRS ->
                    {
                      Variable.customMiningExtraBlocks.put(locationClone, Bukkit.createBlockData(Material.COBBLESTONE_STAIRS, data ->
                      {
                        Stairs stairs = (Stairs) data;
                        stairs.setFacing(((Stairs) blockData).getFacing());
                        stairs.setHalf(((Stairs) blockData).getHalf());
                        stairs.setShape(((Stairs) blockData).getShape());
                      }));
                      if (Variable.customMiningExtraBlocksTask.containsKey(locationClone))
                      {
                        Variable.customMiningExtraBlocksTask.remove(locationClone).cancel();
                      }
                      customMining();
                    }
                    case STONE_SLAB ->
                    {
                      Variable.customMiningExtraBlocks.put(locationClone, Bukkit.createBlockData(Material.COBBLESTONE_SLAB, data ->
                      {
                        Slab slab = (Slab) data;
                        slab.setType(((Slab) blockData).getType());
                      }));
                      if (Variable.customMiningExtraBlocksTask.containsKey(locationClone))
                      {
                        Variable.customMiningExtraBlocksTask.remove(locationClone).cancel();
                      }
                      customMining();
                    }
                  }
                }
                switch (block.getType())
                {
                  case STONE ->
                  {
                    Variable.customMiningExtraBlocks.put(locationClone, Bukkit.createBlockData(Material.COBBLESTONE));
                    if (Variable.customMiningExtraBlocksTask.containsKey(locationClone))
                    {
                      Variable.customMiningExtraBlocksTask.remove(locationClone).cancel();
                    }
                    customMining();
                  }
                  case DEEPSLATE ->
                  {
                    Variable.customMiningExtraBlocks.put(locationClone, Bukkit.createBlockData(Material.COBBLED_DEEPSLATE));
                    if (Variable.customMiningExtraBlocksTask.containsKey(locationClone))
                    {
                      Variable.customMiningExtraBlocksTask.remove(locationClone).cancel();
                    }
                    customMining();
                  }
                }
              }
            }
            if (Variable.customMiningExtraBlocks.containsKey(locationClone))
            {
              int cooldown = Cucumbery.config.getInt("custom-mining.cooldown-extra-block-removal");
              if (cooldown > 0)
              {
                Variable.customMiningExtraBlocksTask.put(locationClone, Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                {
                  Variable.customMiningExtraBlocks.remove(locationClone);
                  customMining();
                }, cooldown));
              }
            }
            if (!extraBlockIgnoreCooldown)
            {
              Variable.customMiningCooldown.put(locationClone, (long) miningResult.regenCooldown());
              customMining();
            }
          }
        }
        // 블록 파괴 진행도 초기화와 채굴 진행도 효과 제거
        MiningManager.quitCustomMining(player);
        // 채굴에 사용된 도구 내구도 처리 및 드릴의 연료 경고 처리(즉시 부숴지는 블록은 내구도가 깎이지 않음)
        boolean dropDura = false;
        if (!instaBreak && ItemStackUtil.itemExists(itemStack) && itemStack.hasItemMeta() && !itemStack.getItemMeta().isUnbreakable())
        {
          NBTItem nbtItem = new NBTItem(itemStack, true);
          NBTCompound itemTag = nbtItem.getCompound(CucumberyTag.KEY_MAIN);
          NBTCompound duraTag = itemTag != null ? itemTag.getCompound(CucumberyTag.CUSTOM_DURABILITY_KEY) : null;
          boolean duraTagExists = duraTag != null;
          long currentDurability = itemStack.getType().getMaxDurability() - ((org.bukkit.inventory.meta.Damageable) itemStack.getItemMeta()).getDamage();
          long maxDurability = itemStack.getType().getMaxDurability();
          double chanceNotLoseDura = 0d;
          if (duraTagExists)
          {
            try
            {
              currentDurability = duraTag.getLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY);
              maxDurability = duraTag.getLong(CucumberyTag.CUSTOM_DURABILITY_MAX_KEY);
              if (duraTag.hasTag(CucumberyTag.CUSTOM_DURABILITY_CHANCE_NOT_TO_CONSUME_DURABILITY))
              {
                chanceNotLoseDura = duraTag.getDouble(CucumberyTag.CUSTOM_DURABILITY_CHANCE_NOT_TO_CONSUME_DURABILITY);
              }
            }
            catch (Exception ignored)
            {

            }
          }
          if (maxDurability > 0)
          {
            int unbreakingLevel = itemStack.getEnchantmentLevel(Enchantment.DURABILITY);
            if (Math.random() >= 1d * unbreakingLevel / (unbreakingLevel + 1) && Math.random() > chanceNotLoseDura / 100d)
            {
              currentDurability--;
              dropDura = true;
            }
            if (currentDurability <= 0)
            {
              player.incrementStatistic(Statistic.BREAK_ITEM, itemStack.getType());
              player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1F, 1F);
              player.spawnParticle(Particle.ITEM_CRACK, player.getEyeLocation().add(0, -0.5, 0), 30, 0, 0, 0, 0.1, itemStack);
              PlayerItemBreakEvent playerItemBreakEvent = new PlayerItemBreakEvent(player, itemStack);
              Bukkit.getPluginManager().callEvent(playerItemBreakEvent);
              itemStack.setAmount(itemStack.getAmount() - 1);
            }
            else
            {
              if (duraTagExists)
              {
                duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY, currentDurability);
              }
              else
              {
                org.bukkit.inventory.meta.Damageable damageable = (org.bukkit.inventory.meta.Damageable) itemMeta;
                if (damageable != null)
                {
                  damageable.setDamage((int) (maxDurability - currentDurability));
                }
                itemStack.setItemMeta(itemMeta);
              }
            }
            player.getInventory().setItemInMainHand(ItemLore.setItemLore(itemStack, ItemLoreView.of(player)));
            CustomMaterial toolCustomMaterial = CustomMaterial.itemStackOf(itemStack);
            if (dropDura && toolCustomMaterial != null)
            {
              switch (toolCustomMaterial)
              {
                case TITANIUM_DRILL_R266, TITANIUM_DRILL_R366, TITANIUM_DRILL_R466, TITANIUM_DRILL_R566, MINDAS_DRILL ->
                {
                  double ratio = currentDurability * 1d / maxDurability;
                  if (ratio == 0.5 || ratio == 0.2 || ratio == 0.1 || ratio == 0.05 || ratio == 0.01)
                  {
                    MessageUtil.info(player, "%s의 연료가 %s%% 남았습니다!", itemStack, Constant.Jeongsu.format(ratio * 100));
                    SoundPlay.playSound(player, Sound.UI_BUTTON_CLICK);
                  }
                }
              }
            }
          }
        }
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if (!dropDura && !mode2 && !mode3 && ItemStackUtil.itemExists(mainHand) && MiningManager.getToolTier(mainHand) > 0)
        {
          NBTItem nbtItem = new NBTItem(mainHand, true);
          if (nbtItem.hasTag("CustomMiningUpdater"))
          {
            nbtItem.removeKey("CustomMiningUpdater");
          }
          else
          {
            nbtItem.setBoolean("CustomMiningUpdater", true);
          }
        }
        // 채굴에 사용된 도구 통계 처리
        if (ItemStackUtil.itemExists(itemStack))
        {
          player.incrementStatistic(Statistic.USE_ITEM, itemStack.getType());
        }
        return;
      }
      // 블록이 캐지는 중
      if (progress > 0.01)
      {
        if (!MiningScheduler.blockBreakKey.containsKey(uuid))
        {
          int random;
          do
          {
            random = (int) (Math.random() * Integer.MAX_VALUE);
          } while (MiningScheduler.blockBreakKey.containsValue(random));
          MiningScheduler.blockBreakKey.put(uuid, random);
        }
        float finalProgress = progress;
        player.getWorld().getPlayers().forEach(p -> p.sendBlockDamage(location, finalProgress, blockBreakKey.get(uuid)));
      }
    }
  }

//  private static void quitMining(@NotNull Player player)
//  {
//    UUID uuid = player.getUniqueId();
//    Location location = player.getEyeLocation();
//    if (!MiningScheduler.blockBreakKey.containsKey(uuid))
//    {
//      int random;
//      do
//      {
//        random = (int) (Math.random() * Integer.MAX_VALUE);
//      } while (MiningScheduler.blockBreakKey.containsValue(random));
//      MiningScheduler.blockBreakKey.put(uuid, random);
//    }
//    Location location2 = new Location(location.getWorld(), 0, 0, 0);
//    player.getWorld().getPlayers().forEach(p -> p.sendBlockDamage(location2, 0f, blockBreakKey.get(uuid)));
//    CustomEffectManager.removeEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_PROGRESS);
//  }
}

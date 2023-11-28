package com.jho5245.cucumbery.listeners.block;

import com.destroystokyo.paper.Namespaced;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.children.group.StringCustomEffectImple;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeCustomMining;
import com.jho5245.cucumbery.util.blockplacedata.BlockPlaceDataConfig;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.ItemSerializer;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.*;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.Chest;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.maxgamer.quickshop.QuickShop;

import java.util.*;

public class BlockBreak implements Listener
{
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onBlockBreak(BlockBreakEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();
    Block block = event.getBlock();
    Location location = block.getLocation();
    Material blockType = block.getType();
    ItemStack itemStack = player.getInventory().getItemInMainHand();
    ItemMeta itemMeta = null;
    if (ItemStackUtil.itemExists(itemStack))
    {
      itemMeta = itemStack.getItemMeta();
    }
    if (Variable.scrollReinforcing.contains(uuid))
    {
      event.setCancelled(true);
      Method.playSound(player, Constant.ERROR_SOUND);
      MessageUtil.sendError(player, "강화중에는 블록을 파괴하실 수 없습니다");
      Component a = ComponentUtil.create(Prefix.INFO, "만약 아이템 강화를 중지하시려면 이 문장을 클릭해주세요.").hoverEvent(ComponentUtil.create("클릭하면 강화를 중지합니다")).clickEvent(ClickEvent.runCommand("/강화 quit"));
      player.sendMessage(a);
      return;
    }
    if (!Cucumbery.config.getBoolean("grant-default-permission-to-players") && !Permission.EVENT_BLOCK_BREAK.has(player))
    {
      event.setCancelled(true);
      if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.blockBreakAlertCooldown.contains(uuid))
      {
        Variable.blockBreakAlertCooldown.add(uuid);
        MessageUtil.sendTitle(player, "&c파괴 불가!", "&r블록을 파괴할 권한이 없습니다", 5, 80, 15);
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.blockBreakAlertCooldown.remove(uuid), 100L);
      }
      return;
    }
    if (player.getGameMode() == GameMode.SURVIVAL && itemMeta != null && itemMeta.hasDestroyableKeys())
    {
      Set<Namespaced> keys = itemMeta.getDestroyableKeys();
      if (!keys.contains(block.getType().getKey()))
      {
        event.setCancelled(true);
        return;
      }
    }
    if (!Permission.EVENT2_ANTI_ALLPLAYER.has(player) && Constant.AllPlayer.BLOCK_BREAK.isEnabled())
    {
      event.setCancelled(true);
      if (!Variable.blockBreakAlertCooldown.contains(uuid))
      {
        Variable.blockBreakAlertCooldown.add(uuid);
        MessageUtil.sendMessage(player, Prefix.INFO_ALLPLAYER, "블록을 파괴할 수 없는 상태입니다");
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.blockBreakAlertCooldown.remove(uuid), 100L);
      }
      return;
    }
    if (Constant.CROP_BLOCKS.contains(blockType))
    {
      if (ItemStackUtil.itemExists(itemStack) && !Constant.HOES.contains(itemStack.getType()))
      {
        if (!UserData.EVENT_EXCEPTION_ACCESS.getBoolean(player.getUniqueId()))
        {
          if (Cucumbery.config.getBoolean("only-harvest-crops-with-hand-or-hoe"))
          {
            if (!Method.configContainsLocation(block.getLocation(), Cucumbery.config.getStringList("no-only-harvest-crops-with-hand-or-hoe-worlds")))
            {
              event.setCancelled(true);
              if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.blockBreakCropHarvestAlertCooldown.contains(uuid))
              {
                Variable.blockBreakCropHarvestAlertCooldown.add(uuid);
                MessageUtil.sendTitle(player, "&c수확 불가!", "&r맨손 혹은 괭이로만 수확할 수 있습니다", 5, 80, 15);
                SoundPlay.playSound(player, Constant.ERROR_SOUND);
                Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.blockBreakCropHarvestAlertCooldown.remove(uuid), 100L);
              }
              return;
            }
          }
        }
      }
    }
    if (NBTAPI.isRestricted(player, itemStack, Constant.RestrictionType.NO_BREAK))
    {
      event.setCancelled(true);
      if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.blockBreakAlertCooldown2.contains(uuid))
      {
        Variable.blockBreakAlertCooldown2.add(uuid);
        MessageUtil.sendTitle(player, "&c파괴 불가!", "&r사용할 수 없는 아이템입니다", 5, 80, 15);
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.blockBreakAlertCooldown2.remove(uuid), 100L);
      }
      return;
    }
    ItemStack placedBlockDataItem = BlockPlaceDataConfig.getItem(location);
    if (NBTAPI.isRestricted(player, placedBlockDataItem, Constant.RestrictionType.NO_BLOCK_BREAK))
    {
      event.setCancelled(true);
      if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.blockBreakAlertCooldown2.contains(uuid))
      {
        Variable.blockBreakAlertCooldown2.add(uuid);
        MessageUtil.sendTitle(player, "&c파괴 불가!", "&r파괴할 수 없는 블록입니다", 5, 80, 15);
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.blockBreakAlertCooldown2.remove(uuid), 100L);
      }
      return;
    }

    if (player.isSneaking() && UserData.DISABLE_COMMAND_BLOCK_BREAK_WHEN_SNEAKING.getBoolean(uuid))
    {
      if (blockType == Material.COMMAND_BLOCK || blockType == Material.REPEATING_COMMAND_BLOCK || blockType == Material.CHAIN_COMMAND_BLOCK)
      {
        event.setCancelled(true);
        if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.blockBreakAlertCooldown2.contains(uuid))
        {
          Variable.blockBreakAlertCooldown2.add(uuid);
          MessageUtil.sendWarn(player, "웅크린 상태에서 명령 블록 파괴 방지 기능으로 명령 블록을 파괴하지 않습니다");
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.blockBreakAlertCooldown2.remove(uuid), 100L);
        }
        return;
      }
    }

    if (CustomEffectManager.hasEffect(player, CustomEffectType.CURSE_OF_CREATIVITY) || CustomEffectManager.hasEffect(player, CustomEffectType.CURSE_OF_CREATIVITY_BREAK))
    {
      event.setCancelled(true);
      return;
    }

    if (player.getGameMode() != GameMode.CREATIVE && CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE)
            && !(CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_2_NO_RESTORE) && (blockType == Material.FIRE || blockType.getHardness() == 0f)))
    {
      if (!Cucumbery.using_mcMMO)
      {
        boolean bypassQuickShop = Cucumbery.using_QuickShop;
        if (bypassQuickShop)
        {
          List<String> blockTypes = QuickShop.getInstance().getConfig().getStringList("shop-blocks");
          bypassQuickShop = blockTypes.contains(blockType.toString());
        }
        if (!bypassQuickShop)
        {
          event.setCancelled(true);
        }
      }
      return;
    }

    if (CustomEnchant.isEnabled() && itemMeta != null && itemMeta.hasEnchant(CustomEnchant.DELICATE))
    {
      if (blockType == Material.PUMPKIN_STEM || blockType == Material.MELON_STEM || block.getBlockData() instanceof Ageable ageable && ageable.getAge() != ageable.getMaximumAge())
      {
        event.setCancelled(true);
        return;
      }
    }

    Variable.customMiningCooldown.remove(location);
    Variable.fakeBlocks.remove(location);
    Variable.customMiningExtraBlocks.remove(location);
    Variable.customMiningMode2BlockData.remove(location);
    if (Variable.customMiningMode2BlockDataTask.containsKey(location))
    {
      Variable.customMiningMode2BlockDataTask.remove(location).cancel();
    }

    if (CustomEffectManager.hasEffect(player, CustomEffectType.DARKNESS_TERROR_ACTIVATED) && Math.random() < 0.15)
    {
      CustomEffectManager.addEffect(player, new StringCustomEffectImple(CustomEffectType.CUSTOM_DEATH_MESSAGE, 10, "custom_darkness_terror"));
      MessageUtil.sendActionBar(player, "&c아야! 너무 어두워서 블록을 캐다가 파편에 맞았습니다!");
      player.damage(1);
      player.setNoDamageTicks(0);
    }

    Object value = player.getWorld().getGameRuleValue(GameRule.DO_TILE_DROPS);

    boolean modified = false;

    boolean isTelekinesis = CustomEnchant.isEnabled() && (itemMeta != null && itemMeta.hasEnchant(CustomEnchant.TELEKINESIS)) ||
            CustomEffectManager.hasEffect(player, CustomEffectType.TELEKINESIS),
            isSmeltingTouch = CustomEnchant.isEnabled() && (itemMeta != null && itemMeta.hasEnchant(CustomEnchant.SMELTING_TOUCH)) ||
                    CustomEffectManager.hasEffect(player, CustomEffectType.SMELTING_TOUCH),
            isSilkTouch = CustomEffectManager.hasEffect(player, CustomEffectType.SILK_TOUCH);

    List<ItemStack> drops = new ArrayList<>(block.getDrops(itemStack, player));

    float farmingFortune = 1f;

    // 수확 인챈트로 인한 드롭율 증가. 단, 자라지 않은 작물은 해당 없음
    int harvestingLevel = CustomEnchant.isEnabled() && itemMeta != null && itemMeta.hasEnchant(CustomEnchant.HARVESTING) ? itemMeta.getEnchantLevel(CustomEnchant.HARVESTING) : 0;
    if (harvestingLevel > 0 && (!(block.getBlockData() instanceof Ageable ageable) || ageable.getAge() == ageable.getMaximumAge()))
    {
      farmingFortune += harvestingLevel * 0.15;
    }

    // 커스텀 채광 모드에서는 즉시 부서지는 작물(밀, 당근 등. 호박, 코코아 콩 등은 제외)을 캘 때 행운 대신 전용 인챈트 사용
    if (CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_2_NO_RESTORE) && Tag.CROPS.isTagged(block.getType()) && block.getType().getHardness() == 0f)
    {
      switch (block.getType())
      {
        case WHEAT, CARROTS, POTATOES, SUGAR_CANE, BEETROOTS, NETHER_WART, SWEET_BERRY_BUSH, RED_MUSHROOM, BROWN_MUSHROOM ->
        {
          ItemStack clone = itemStack.clone();
          if (clone.hasItemMeta())
          {
            ItemMeta cloneMeta = clone.getItemMeta();
            cloneMeta.removeEnchant(Enchantment.LOOT_BONUS_BLOCKS);
            clone.setItemMeta(cloneMeta);
          }
          int intSide = (int) farmingFortune;
          float floatSide = farmingFortune - intSide;
          if (Math.random() < floatSide)
          {
            intSide++;
          }
          if (intSide > 0)
          {
            modified = true;
            drops = new ArrayList<>(block.getDrops(clone, player));
            int finalIntSide = intSide;
            drops.forEach(i -> i.setAmount(i.getAmount() * finalIntSide));
          }
        }
      }
    }

    if (isSilkTouch)
    {
      ItemStack silk = new ItemStack(ItemStackUtil.itemExists(itemStack) ? itemStack.getType() : Material.STONE);
      ItemMeta m = silk.getItemMeta();
      m.addEnchant(Enchantment.SILK_TOUCH, 1, true);
      silk.setItemMeta(m);
      drops = new ArrayList<>(block.getDrops(silk, player));
      modified = true;
    }

    String toolId = (ItemStackUtil.itemExists(itemStack) ? new NBTItem(itemStack).getString("id") : "") + "",
            blockId = (!drops.isEmpty() && ItemStackUtil.itemExists(drops.get(0)) ? new NBTItem(drops.get(0)).getString("id") : "") + "";
    if (CustomMaterial.FLINT_SHOVEL.toString().equalsIgnoreCase(toolId) && blockId.equals("") && blockType == Material.GRAVEL)
    {
      modified = true;
      drops.forEach(item ->
      {
        if (item.getType() == Material.GRAVEL)
        {
          item.setType(Material.FLINT);
        }
      });
    }

    boolean vanilliaDrop = !drops.isEmpty() && player.getGameMode() != GameMode.CREATIVE && event.isDropItems() && value != null && value.equals(true);
    boolean isCoarseTouch = CustomEnchant.isEnabled() && itemMeta != null && itemMeta.hasEnchant(CustomEnchant.COARSE_TOUCH);

    if (isCoarseTouch)
    {
      event.setDropItems(false);
    }

    boolean isUnskilledTouch = CustomEnchant.isEnabled() && itemMeta != null && itemMeta.hasEnchant(CustomEnchant.UNSKILLED_TOUCH);

    if (isUnskilledTouch || isSilkTouch)
    {
      event.setExpToDrop(0);
    }

    if (CustomEnchant.isEnabled() && itemMeta != null && itemMeta.hasEnchant(CustomEnchant.VANISHING_TOUCH))
    {
      if (block.getState() instanceof Container container)
      {
        container.getInventory().setContents(new ItemStack[0]);
      }
    }
    // bukkit error fix
    if (blockType == Material.CHEST || blockType == Material.TRAPPED_CHEST)
    {
      Chest chest = (Chest) block.getBlockData();
      if (chest.isWaterlogged())
      {
        block.setBlockData(Bukkit.createBlockData(Material.AIR));
        block.setBlockData(Bukkit.createBlockData(Material.WATER));
      }
    }

    YamlConfiguration blockPlaceData = BlockPlaceDataConfig.getInstance(location.getChunk()).getConfig();
    List<ItemStack> customDrops = null;

    boolean blockPlaceDataApplied = false;

    if (drops.size() == 1)
    {
      String dataString = blockPlaceData.getString(location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ());
      if (dataString != null)
      {
        ItemStack dataItem = ItemSerializer.deserialize(dataString);
        NBTItem nbtItem = new NBTItem(dataItem);
        if (nbtItem.hasTag("displays") && nbtItem.getCompound("displays").hasTag("rotation") &&
            (nbtItem.hasTag("perspectiveYaw") || nbtItem.hasTag("perspectivePitch") || nbtItem.hasTag("perspectiveGrid")))
        {
          nbtItem.getCompound("displays").removeKey("rotation");
          if (nbtItem.getCompound("displays").getKeys().isEmpty())
          {
            nbtItem.removeKey("displays");
          }
        }
        customDrops = new ArrayList<>();
        customDrops.add(dataItem);
        blockPlaceDataApplied = true;
      }
    }

    boolean hasFranticFortune = CustomEnchant.isEnabled() && itemMeta != null && itemMeta.hasEnchant(CustomEnchant.FRANTIC_FORTUNE);

    if (customDrops == null)
    {
      customDrops = new ArrayList<>(drops);
      if (hasFranticFortune)
      {
        customDrops.addAll(drops);
      }
    }

    if (vanilliaDrop && !isCoarseTouch && !isUnskilledTouch && (CustomEnchant.isEnabled() && itemMeta != null && itemMeta.hasEnchant(CustomEnchant.DULL_TOUCH)))
    {
      int level = itemMeta.getEnchantLevel(CustomEnchant.DULL_TOUCH);
      if (Method.random(1, 100) <= level)
      {
        event.setDropItems(false);
        event.setExpToDrop(0);
        ItemStack flint = Method.usingLoreFeature(player) ? ItemStackUtil.loredItemStack(Material.FLINT) : new ItemStack(Material.FLINT);
        if (ItemStackUtil.countSpace(player.getInventory(), flint) >= 1 && isTelekinesis)
        {
          player.getInventory().addItem(flint);
        }
        else
        {
          player.getWorld().dropItemNaturally(event.getBlock().getLocation(), flint);
        }
      }
    }

    if (vanilliaDrop)
    {
      if (event.isDropItems())
      {
        boolean pluginAffected = false;
        List<ItemStack> dropsClone = null;
        if (isTelekinesis || isSmeltingTouch || isSilkTouch)
        {
          List<Double> dropsExp = new ArrayList<>();
          if (isTelekinesis || isSilkTouch)
          {
            pluginAffected = true;
          }
          if (isSmeltingTouch)
          {
            dropsClone = ItemStackUtil.getSmeltedResult(player, customDrops, dropsExp);
            pluginAffected = pluginAffected || !dropsExp.isEmpty();
          }
          else
          {
            dropsClone = new ArrayList<>(customDrops);
          }

          boolean lecternItemExists = false;

          if (block.getState() instanceof BlockInventoryHolder blockInventoryHolder && !(block.getState() instanceof ShulkerBox))
          {
            Inventory containerInventory = blockInventoryHolder.getInventory();
            for (int i = 0; i < containerInventory.getSize(); i++)
            {
              ItemStack content = containerInventory.getItem(i);
              if (ItemStackUtil.itemExists(content))
              {
                if (block.getState() instanceof Lectern)
                {
                  lecternItemExists = true;
                }
                containerInventory.removeItem(content);
                dropsClone.add(content);
              }
            }
          }

          if (lecternItemExists && blockType == Material.LECTERN)
          {
            event.setCancelled(true);
            Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                    block.setBlockData(Bukkit.createBlockData(Material.AIR)), 0L);
            player.incrementStatistic(Statistic.MINE_BLOCK, Material.LECTERN);
            for (Player online : Bukkit.getOnlinePlayers())
            {
              if (online != player)
              {
                online.spawnParticle(Particle.BLOCK_CRACK, block.getLocation(), 10, Bukkit.createBlockData(Material.LECTERN));
                online.playSound(block.getLocation(), Sound.BLOCK_WOOD_BREAK, SoundCategory.BLOCKS, 1f, 0.7f);
              }
            }
          }

          if (blockType == Material.CAMPFIRE || blockType == Material.SOUL_CAMPFIRE)
          {
            Campfire campfire = (Campfire) block.getState();
            for (int i = 0; i < 4; i++)
            {
              ItemStack campfireItem = campfire.getItem(i);
              if (ItemStackUtil.itemExists(campfireItem))
              {
                dropsClone.add(campfireItem);
                campfire.setItem(i, null);
              }
            }
            campfire.update(true, false);
          }
          if (pluginAffected)
          {
            event.setDropItems(false);
            if (!isUnskilledTouch)
            {
              for (Double dropExp : dropsExp)
              {
                int guaranteeXp = (int) Math.floor(dropExp);
                if (dropExp - guaranteeXp > 0d && Math.random() <= dropExp)
                {
                  guaranteeXp++;
                }
                if (guaranteeXp > 0)
                {
                  int finalGuaranteeXp = guaranteeXp;
                  player.getWorld().spawnEntity(block.getLocation(), EntityType.EXPERIENCE_ORB, SpawnReason.CUSTOM, e -> ((ExperienceOrb) e).setExperience(finalGuaranteeXp));
                }
              }
            }
            if (!isTelekinesis)
            {
              for (ItemStack dropClone : dropsClone)
              {
                player.getWorld().dropItemNaturally(block.getLocation(), dropClone);
              }
            }
            else
            {
              boolean setItemLore = Method.usingLoreFeature(player);
              for (ItemStack dropClone : dropsClone)
              {
                if (setItemLore)
                {
                  ItemLore.setItemLore(dropClone);
                }
                else
                {
                  ItemLore.removeItemLore(dropClone);
                }
                HashMap<Integer, ItemStack> lostItems = player.getInventory().addItem(dropClone);
                // 텔레키네시스로 인벤토리에 아이템을 넣었는데 인벤토리 공간이 좁을 경우
                if (!lostItems.isEmpty())
                {
                  for (int i = 0; i < lostItems.size(); i++)
                  {
                    ItemStack lostItem = lostItems.get(i);
                    player.getWorld().dropItemNaturally(block.getLocation(), lostItem);
                  }
                }
              }
            }
          }
        }
        if (!pluginAffected && blockPlaceDataApplied)
        {
          if (dropsClone == null)
          {
            dropsClone = new ArrayList<>(customDrops);
          }

          boolean lecternItemExists = false;

          if (block.getState() instanceof BlockInventoryHolder blockInventoryHolder && !(block.getState() instanceof ShulkerBox))
          {
            Inventory containerInventory = blockInventoryHolder.getInventory();
            for (int i = 0; i < containerInventory.getSize(); i++)
            {
              ItemStack content = containerInventory.getItem(i);
              if (ItemStackUtil.itemExists(content))
              {
                if (block.getState() instanceof Lectern)
                {
                  lecternItemExists = true;
                }
                containerInventory.removeItem(content);
                dropsClone.add(content);
              }
            }
          }

          if (lecternItemExists && blockType == Material.LECTERN)
          {
            event.setCancelled(true);
            Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                    block.setBlockData(Bukkit.createBlockData(Material.AIR)), 0L);
            player.incrementStatistic(Statistic.MINE_BLOCK, Material.LECTERN);
            for (Player online : Bukkit.getOnlinePlayers())
            {
              if (online != player)
              {
                online.spawnParticle(Particle.BLOCK_CRACK, block.getLocation(), 10, Bukkit.createBlockData(Material.LECTERN));
                online.playSound(block.getLocation(), Sound.BLOCK_WOOD_BREAK, SoundCategory.BLOCKS, 1f, 0.7f);
              }
            }
          }

          if (blockType == Material.CAMPFIRE || blockType == Material.SOUL_CAMPFIRE)
          {
            Campfire campfire = (Campfire) block.getState();
            for (int i = 0; i < 4; i++)
            {
              ItemStack campfireItem = campfire.getItem(i);
              if (ItemStackUtil.itemExists(campfireItem))
              {
                dropsClone.add(campfireItem);
                campfire.setItem(i, null);
              }
            }
            campfire.update(true, false);
          }
          event.setDropItems(false);
          for (ItemStack drop : dropsClone)
          {
            player.getWorld().dropItemNaturally(location, drop);
          }
        }
        if (!blockPlaceDataApplied && !isCoarseTouch && (CustomEnchant.isEnabled() && itemMeta != null && itemMeta.hasEnchant(CustomEnchant.COLD_TOUCH) && itemMeta.hasEnchant(Enchantment.SILK_TOUCH)))
        {
          switch (blockType)
          {
            case ICE, BLUE_ICE, PACKED_ICE ->
            {
              ItemStack result = new ItemStack(Material.ICE);
              NBTItem nbtItem = new NBTItem(result);
              NBTCompound itemTag = nbtItem.getCompound(CucumberyTag.KEY_MAIN);
              NBTCompoundList restrictionTags = itemTag.getCompoundList(CucumberyTag.ITEM_USAGE_RESTRICTIONS_KEY);
              NBTCompound restrictionTag1 = restrictionTags.addCompound();
              restrictionTag1.setString(CucumberyTag.VALUE_KEY, Constant.RestrictionType.NO_CRAFT.toString());
              restrictionTag1.setBoolean(CucumberyTag.ITEM_USAGE_RESTRICTIONS_HIDE_FROM_LORE_KEY, true);
              NBTCompound restrictionTag2 = restrictionTags.addCompound();
              restrictionTag2.setString(CucumberyTag.VALUE_KEY, Constant.RestrictionType.NO_PLACE.toString());
              restrictionTag2.setBoolean(CucumberyTag.ITEM_USAGE_RESTRICTIONS_HIDE_FROM_LORE_KEY, true);
              NBTList<String> customLore = itemTag.getStringList(CucumberyTag.CUSTOM_LORE_KEY);
              int random = Method.random(1, 100);
              String lore = "";
              switch (blockType)
              {
                case ICE:
                  Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> block.setType(Material.AIR), 0L);
                  if (random <= 3)
                  {
                    lore = "&4최악";
                  }
                  else if (random <= 15)
                  {
                    lore = "&c나쁨";
                  }
                  else if (random <= 35)
                  {
                    lore = "rg255,204;조금 나쁨";
                  }
                  else if (random <= 65)
                  {
                    lore = "rg255,204;보통";
                  }
                  else if (random <= 85)
                  {
                    lore = "&a조금 좋음";
                  }
                  else if (random <= 97)
                  {
                    lore = "&b좋음";
                  }
                  else
                  {
                    lore = "&3최상";
                  }
                  break;
                case PACKED_ICE:
                  if (random <= 2)
                  {
                    lore = "&4최악";
                  }
                  else if (random <= 12)
                  {
                    lore = "&c나쁨";
                  }
                  else if (random <= 29)
                  {
                    lore = "rg255,204;조금 나쁨";
                  }
                  else if (random <= 55)
                  {
                    lore = "rg255,204;보통";
                  }
                  else if (random <= 79)
                  {
                    lore = "&a조금 좋음";
                  }
                  else if (random <= 93)
                  {
                    lore = "&b좋음";
                  }
                  else
                  {
                    lore = "&3최상";
                  }
                  break;
                case BLUE_ICE:
                  if (random <= 1)
                  {
                    lore = "&4최악";
                  }
                  else if (random <= 6)
                  {
                    lore = "&c나쁨";
                  }
                  else if (random <= 21)
                  {
                    lore = "rg255,204;조금 나쁨";
                  }
                  else if (random <= 43)
                  {
                    lore = "rg255,204;보통";
                  }
                  else if (random <= 71)
                  {
                    lore = "&a조금 좋음";
                  }
                  else if (random <= 90)
                  {
                    lore = "&b좋음";
                  }
                  else
                  {
                    lore = "&3최상";
                  }
                  break;
                default:
                  break;
              }
              customLore.addAll(Arrays.asList("", MessageUtil.n2s("&6등급 : " + lore)));
              result = nbtItem.getItem();
              if (Method.usingLoreFeature(player))
              {
                ItemLore.setItemLore(result);
              }
              player.getInventory().addItem(result);
            }
            default ->
            {
            }
          }
        }
        else if (!isCoarseTouch && (itemMeta != null && CustomEnchant.isEnabled() && itemMeta.hasEnchant(CustomEnchant.WARM_TOUCH)) && blockType == Material.CACTUS)
        {
          event.setDropItems(false);
          ItemStack result = new ItemStack(Material.CACTUS);
          NBTItem nbtItem = new NBTItem(result);
          NBTCompound itemTag = nbtItem.getCompound(CucumberyTag.KEY_MAIN);
          NBTCompoundList restrictionTags = itemTag.getCompoundList(CucumberyTag.ITEM_USAGE_RESTRICTIONS_KEY);
          NBTCompound restrictionTag1 = restrictionTags.addCompound();
          restrictionTag1.setString(CucumberyTag.VALUE_KEY, Constant.RestrictionType.NO_CRAFT.toString());
          restrictionTag1.setBoolean(CucumberyTag.ITEM_USAGE_RESTRICTIONS_HIDE_FROM_LORE_KEY, true);
          NBTCompound restrictionTag2 = restrictionTags.addCompound();
          restrictionTag2.setString(CucumberyTag.VALUE_KEY, Constant.RestrictionType.NO_SMELT.toString());
          restrictionTag2.setBoolean(CucumberyTag.ITEM_USAGE_RESTRICTIONS_HIDE_FROM_LORE_KEY, true);
          NBTCompound restrictionTag3 = restrictionTags.addCompound();
          restrictionTag3.setString(CucumberyTag.VALUE_KEY, Constant.RestrictionType.NO_COMPOSTER.toString());
          restrictionTag3.setBoolean(CucumberyTag.ITEM_USAGE_RESTRICTIONS_HIDE_FROM_LORE_KEY, true);
          NBTCompound restrictionTag4 = restrictionTags.addCompound();
          restrictionTag4.setString(CucumberyTag.VALUE_KEY, Constant.RestrictionType.NO_PLACE.toString());
          restrictionTag4.setBoolean(CucumberyTag.ITEM_USAGE_RESTRICTIONS_HIDE_FROM_LORE_KEY, true);
          NBTList<String> customLore = itemTag.getStringList(CucumberyTag.CUSTOM_LORE_KEY);
          int random = Method.random(1, 100);
          String lore;
          if (random <= 3)
          {
            lore = "&4최악";
          }
          else if (random <= 15)
          {
            lore = "&c나쁨";
          }
          else if (random <= 35)
          {
            lore = "rg255,204;조금 나쁨";
          }
          else if (random <= 65)
          {
            lore = "rg255,204;보통";
          }
          else if (random <= 85)
          {
            lore = "&a조금 좋음";
          }
          else if (random <= 97)
          {
            lore = "&b좋음";
          }
          else
          {
            lore = "&3최상";
          }
          customLore.addAll(Arrays.asList("", MessageUtil.n2s("&6등급 : " + lore)));
          result = nbtItem.getItem();
          if (Method.usingLoreFeature(player))
          {
            ItemLore.setItemLore(result);
          }
          player.getInventory().addItem(result);
        }
      }
    }

    if (hasFranticFortune && event.isDropItems())
    {
      drops.forEach(item -> block.getLocation().getWorld().dropItemNaturally(block.getLocation(), item));
    }

    if (modified && event.isDropItems())
    {
      event.setDropItems(false);
      drops.forEach(item -> block.getLocation().getWorld().dropItemNaturally(block.getLocation(), item));
    }
    BlockPlaceDataConfig.removeData(location);
  }
}

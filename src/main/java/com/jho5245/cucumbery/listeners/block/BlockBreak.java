package com.jho5245.cucumbery.listeners.block;

import com.destroystokyo.paper.Namespaced;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.ItemSerializer;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.Method2;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.SoundPlay;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.*;
import org.bukkit.block.*;
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
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class BlockBreak implements Listener
{
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onBlockBreak(BlockBreakEvent event)
  {
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();
    Block block = event.getBlock();
    Location location = block.getLocation();
    Material blockType = block.getType();
    ItemStack item = player.getInventory().getItemInMainHand();
    ItemMeta itemMeta = null;
    if (ItemStackUtil.itemExists(item))
    {
      itemMeta = item.getItemMeta();
    }
    if (event.isCancelled())
    {
      return;
    }
    if (Variable.scrollReinforcing.contains(uuid))
    {
      event.setCancelled(true);
      Method.playSound(player, Constant.ERROR_SOUND);
      MessageUtil.sendError(player, "강화중에는 블록을 파괴하실 수 없습니다.");
      Component a = ComponentUtil.create(Prefix.INFO + "만약 아이템 강화를 중지하시려면 이 문장을 클릭해주세요.", "클릭하면 강화를 중지합니다.", ClickEvent.Action.RUN_COMMAND, "/강화 quit");
      player.sendMessage(a);
      return;
    }
    if (!Cucumbery.config.getBoolean("grant-default-permission-to-players") && !Permission.EVENT_BLOCK_BREAK.has(player))
    {
      event.setCancelled(true);
      if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.blockBreakAlertCooldown.contains(uuid))
      {
        Variable.blockBreakAlertCooldown.add(uuid);
        MessageUtil.sendTitle(player, "&c파괴 불가!", "&r블록을 파괴할 권한이 없습니다.", 5, 80, 15);
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
        MessageUtil.sendMessage(player, Prefix.INFO_ALLPLAYER, "블록을 파괴할 수 없는 상태입니다.");
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.blockBreakAlertCooldown.remove(uuid), 100L);
      }
      return;
    }
    if (Constant.CROP_BLOCKS.contains(blockType))
    {
      if (ItemStackUtil.itemExists(item) && !Constant.HOES.contains(item.getType()))
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
                MessageUtil.sendTitle(player, "&c수확 불가!", "&r맨손 혹은 괭이로만 수확할 수 있습니다.", 5, 80, 15);
                SoundPlay.playSound(player, Constant.ERROR_SOUND);
                Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.blockBreakCropHarvestAlertCooldown.remove(uuid), 100L);
              }
              return;
            }
          }
        }
      }
    }
    if (NBTAPI.isRestricted(player, item, Constant.RestrictionType.NO_BREAK))
    {
      event.setCancelled(true);
      if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.blockBreakAlertCooldown2.contains(uuid))
      {
        Variable.blockBreakAlertCooldown2.add(uuid);
        MessageUtil.sendTitle(player, "&c파괴 불가!", "&r사용할 수 없는 아이템입니다.", 5, 80, 15);
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.blockBreakAlertCooldown2.remove(uuid), 100L);
      }
      return;
    }

    ItemStack placedBlockDataItem = Method2.getPlacedBlockDataAsItemStack(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    if (NBTAPI.isRestricted(player, placedBlockDataItem, Constant.RestrictionType.NO_BLOCK_BREAK))
    {
      event.setCancelled(true);
      if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.blockBreakAlertCooldown2.contains(uuid))
      {
        Variable.blockBreakAlertCooldown2.add(uuid);
        MessageUtil.sendTitle(player, "&c파괴 불가!", "&r파괴할 수 없는 블록입니다.", 5, 80, 15);
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
          MessageUtil.sendWarn(player, "웅크린 상태에서 명령 블록 파괴 방지 기능으로 명령 블록을 파괴하지 않습니다.");
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.blockBreakAlertCooldown2.remove(uuid), 100L);
        }
        return;
      }
    }

    NBTCompoundList customEnchantsTag = NBTAPI.getCompoundList(NBTAPI.getMainCompound(item), CucumberyTag.CUSTOM_ENCHANTS_KEY);

    Object value = player.getWorld().getGameRuleValue(GameRule.DO_TILE_DROPS);
    Collection<ItemStack> drops = block.getDrops(item, player);
    boolean vanilliaDrop = drops.size() > 0 && player.getGameMode() != GameMode.CREATIVE && event.isDropItems() && value != null && value.equals(true);

    boolean isCoarseTouch = NBTAPI.commpoundListContainsValue(customEnchantsTag, CucumberyTag.ID_KEY, Constant.CustomEnchant.COARSE_TOUCH.toString());

    if (isCoarseTouch)
    {
      event.setDropItems(false);
    }

    boolean isUnskilledTouch = NBTAPI.commpoundListContainsValue(customEnchantsTag, CucumberyTag.ID_KEY, Constant.CustomEnchant.UNSKILLED_TOUCH.toString());

    if (isUnskilledTouch)
    {
      event.setExpToDrop(0);
    }
    if (NBTAPI.commpoundListContainsValue(customEnchantsTag, CucumberyTag.ID_KEY, Constant.CustomEnchant.VANISHING_TOUCH.toString()))
    {
      if (block.getState() instanceof Container)
      {
        Container container = (Container) block.getState();
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

    YamlConfiguration blockPlaceData = Variable.blockPlaceData.get(location.getWorld().getName());
    List<ItemStack> customDrops = null;

    boolean blockPlaceDataApplied = false;
    boolean noExpDropDueToForcePreverse = false;

    if (drops.size() == 1)
    {
      if (blockPlaceData != null)
      {
        String dataString = blockPlaceData.getString(location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ());
        if (dataString != null)
        {
          ItemStack dataItem = ItemSerializer.deserialize(dataString);
          NBTList<String> extraTag = NBTAPI.getStringList(NBTAPI.getMainCompound(dataItem), CucumberyTag.EXTRA_TAGS_KEY);
          noExpDropDueToForcePreverse = NBTAPI.arrayContainsValue(extraTag, Constant.ExtraTag.FORCE_PRESERVE_BLOCK_NBT);
          for (ItemStack drop : drops)
          {
            if (drop.getType() == dataItem.getType() || noExpDropDueToForcePreverse)
            {
              customDrops = new ArrayList<>();
              customDrops.add(dataItem);
              blockPlaceDataApplied = true;
              break;
            }
          }
        }
      }
    }

    if (customDrops == null)
    {
      customDrops = new ArrayList<>(drops);
    }

    if (vanilliaDrop && !isCoarseTouch && !isUnskilledTouch && NBTAPI.commpoundListContainsValue(customEnchantsTag, CucumberyTag.ID_KEY, Constant.CustomEnchant.DULL_TOUCH.toString()))
    {
      int level = Method.getCustomEnchantLevel(item, Constant.CustomEnchant.DULL_TOUCH);
      if (Method.random(1, 100) <= level)
      {
        event.setDropItems(false);
        event.setExpToDrop(0);
        ItemStack flint = Method.usingLoreFeature(player) ? ItemStackUtil.loredItemStack(Material.FLINT) : new ItemStack(Material.FLINT);
        if (ItemStackUtil.countSpace(player.getInventory(), flint) >= 1 && NBTAPI.commpoundListContainsValue(customEnchantsTag, CucumberyTag.ID_KEY, Constant.CustomEnchant.TELEKINESIS.toString()))
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
        boolean isTelekinesis = NBTAPI.commpoundListContainsValue(customEnchantsTag, CucumberyTag.ID_KEY, Constant.CustomEnchant.TELEKINESIS.toString()), isSmeltingTouch =
                NBTAPI.commpoundListContainsValue(customEnchantsTag, CucumberyTag.ID_KEY, Constant.CustomEnchant.SMELTING_TOUCH.toString());
        boolean pluginAffected = false;
        List<ItemStack> dropsClone = null;
        if (isTelekinesis || isSmeltingTouch)
        {
          List<Double> dropsExp = new ArrayList<>();

          if (isTelekinesis)
          {
            pluginAffected = true;
          }
          if (isSmeltingTouch && !noExpDropDueToForcePreverse)
          {
            dropsClone = ItemStackUtil.getSmeltedResult(player, customDrops, dropsExp);
            pluginAffected = pluginAffected || dropsExp.size() > 0;
          }
          else
          {
            dropsClone = new ArrayList<>(customDrops);
          }

          boolean lecternItemExists = false;

          if (block.getState() instanceof BlockInventoryHolder && !(block.getState() instanceof ShulkerBox))
          {
            BlockInventoryHolder blockInventoryHolder = (BlockInventoryHolder) block.getState();
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
                  ExperienceOrb xpOrb = (ExperienceOrb) player.getWorld().spawnEntity(player.getLocation(), EntityType.EXPERIENCE_ORB);
                  xpOrb.setExperience(guaranteeXp);
                  if (!Method.configContainsLocation(xpOrb.getLocation(), Cucumbery.config.getStringList("no-display-xp-orb-value-worlds")))
                  {
                    String configString = Cucumbery.config.getString("display-xp-orb-value-format");
                    if (configString != null)
                    {
                      xpOrb.setCustomName(MessageUtil.n2s(
                              "§경§험§치" + configString.replace("%value%", guaranteeXp + "")));
                    }
                  }
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
              if (event.getExpToDrop() > 0)
              {
                if (!noExpDropDueToForcePreverse)
                {
                  ExperienceOrb xpOrb = (ExperienceOrb) player.getWorld().spawnEntity(player.getLocation(), EntityType.EXPERIENCE_ORB);
                  xpOrb.setExperience(event.getExpToDrop());
                  if (!Method.configContainsLocation(xpOrb.getLocation(), Cucumbery.config.getStringList("no-display-xp-orb-value-worlds")))
                  {
                    String configString = Cucumbery.config.getString("display-xp-orb-value-format");
                    if (configString != null)
                    {
                      xpOrb.setCustomName(MessageUtil.n2s(
                              "§경§험§치" + configString.replace("%value%", event.getExpToDrop() + "")));
                    }
                  }
                }
                event.setExpToDrop(0);
              }
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
                if (lostItems.size() > 0)
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

          if (block.getState() instanceof BlockInventoryHolder && !(block.getState() instanceof ShulkerBox))
          {
            BlockInventoryHolder blockInventoryHolder = (BlockInventoryHolder) block.getState();
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
          if (noExpDropDueToForcePreverse)
          {
            event.setExpToDrop(0);
          }
          event.setDropItems(false);
          for (ItemStack drop : dropsClone)
          {
            player.getWorld().dropItemNaturally(location, drop);
          }
        }
        if (!blockPlaceDataApplied && !isCoarseTouch && NBTAPI.commpoundListContainsValue(customEnchantsTag, CucumberyTag.ID_KEY, Constant.CustomEnchant.COLD_TOUCH.toString()) && !Objects.requireNonNull(itemMeta)
                .hasEnchant(
                        Enchantment.SILK_TOUCH))
        {
          switch (blockType)
          {
            case ICE:
            case BLUE_ICE:
            case PACKED_ICE:
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
                    lore = "&e조금 나쁨";
                  }
                  else if (random <= 65)
                  {
                    lore = "&e보통";
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
                    lore = "&e조금 나쁨";
                  }
                  else if (random <= 55)
                  {
                    lore = "&e보통";
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
                    lore = "&e조금 나쁨";
                  }
                  else if (random <= 43)
                  {
                    lore = "&e보통";
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
              break;
            default:
              break;
          }
        }
        else if (!isCoarseTouch && NBTAPI.commpoundListContainsValue(customEnchantsTag, CucumberyTag.ID_KEY, Constant.CustomEnchant.WARM_TOUCH.toString()) && blockType == Material.CACTUS)
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
            lore = "&e조금 나쁨";
          }
          else if (random <= 65)
          {
            lore = "&e보통";
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

    if (blockPlaceData != null)
    {
      blockPlaceData.set(location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ(), null);
      Variable.blockPlaceData.put(location.getWorld().getName(), blockPlaceData);
    }
  }
}
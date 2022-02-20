package com.jho5245.cucumbery.listeners.player.interact;

import com.destroystokyo.paper.block.TargetBlockInfo;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectType;
import com.jho5245.cucumbery.listeners.block.NotePlay;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Constant.AllPlayer;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTList;
import org.bukkit.*;
import org.bukkit.Note.Tone;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerInteract implements Listener
{
  private static final Set<UUID> RAILGUN_WHITE_LIST = new HashSet<>();

  static
  {
    RAILGUN_WHITE_LIST.add(UUID.fromString("4962252e-347b-4711-b418-3d1afae250b1"));
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event)
  {
    Player player = event.getPlayer();
    if (player.getGameMode() == GameMode.SPECTATOR)
    {
      return;
    }
    UUID uuid = player.getUniqueId();
    ItemStack item = event.getItem();
    boolean itemExists = ItemStackUtil.itemExists(item);
    Material itemType = Material.AIR;
    if (item != null)
    {
      itemType = item.getType();
    }
    Action action = event.getAction();
    Block block = event.getClickedBlock();
    Material clickedBlockType;
    if (block != null)
    {
      clickedBlockType = block.getType();
    }
    else
    {
      clickedBlockType = Material.AIR;
    }
    if (Variable.scrollReinforcing.contains(uuid))
    {
      event.setCancelled(true);
      return;
    }
    if (!Cucumbery.config.getBoolean("grant-default-permission-to-players") && !Permission.EVENT_INTERACT.has(player))
    {
      event.setCancelled(true);
      if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerInteractAlertCooldown.contains(uuid))
      {
        Variable.playerInteractAlertCooldown.add(uuid);
        MessageUtil.sendTitle(player, "&c행동 불가!", "&r행동할 권한이 없습니다", 5, 80, 15);
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                Variable.playerInteractAlertCooldown.remove(uuid), 100L);
      }
      return;
    }
    if (!Permission.EVENT2_ANTI_ALLPLAYER.has(player) && itemExists && AllPlayer.ITEM_INTERACT.isEnabled())
    {
      event.setCancelled(true);
      if (!Variable.playerInteractAlertCooldown.contains(uuid))
      {
        Variable.playerInteractAlertCooldown.add(uuid);
        MessageUtil.sendMessage(player, Prefix.INFO_ALLPLAYER, "아이템을 사용할 수 없는 상태입니다");
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerInteractAlertCooldown.remove(uuid), 100L);
      }
      return;
    }
    EquipmentSlot hand = event.getHand();
    if (hand != null)
    {
      if (hand == EquipmentSlot.HAND && NBTAPI.isRestricted(player, item, RestrictionType.NO_USE_MAIN_HAND))
      {
        event.setCancelled(true);
        if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerInteractAlertCooldown.contains(uuid))
        {
          Variable.playerInteractAlertCooldown.add(uuid);
          MessageUtil.sendTitle(player, "&c사용 불가!", "주로 사용하는 손에 들고 사용할 수 없는 아이템입니다", 5, 80, 15);
          SoundPlay.playSound(player, Constant.ERROR_SOUND);
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerInteractAlertCooldown.remove(uuid), 100L);
        }
        return;
      }
      if (hand == EquipmentSlot.OFF_HAND && NBTAPI.isRestricted(player, item, RestrictionType.NO_USE_OFF_HAND))
      {
        event.setCancelled(true);
        if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerInteractAlertCooldown.contains(uuid))
        {
          Variable.playerInteractAlertCooldown.add(uuid);
          MessageUtil.sendTitle(player, "&c사용 불가!", "다른 손에 들고 사용할 수 없는 아이템입니다", 5, 80, 15);
          SoundPlay.playSound(player, Constant.ERROR_SOUND);
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerInteractAlertCooldown.remove(uuid), 100L);
        }
        return;
      }
    }
    boolean leftBlock = action == Action.LEFT_CLICK_BLOCK, leftAir = action == Action.LEFT_CLICK_AIR, leftClick = leftBlock || leftAir;
    boolean rightBlock = action == Action.RIGHT_CLICK_BLOCK, rightAir = action == Action.RIGHT_CLICK_AIR, rightClick = rightBlock || rightAir;
    boolean airClick = leftAir || rightAir, blockClick = leftBlock || rightBlock;
    if (!Cucumbery.config.getBoolean("grant-default-permission-to-players") && (blockClick || action == Action.PHYSICAL) && !Permission.EVENT_INTERACT_BLOCK.has(player))
    {
      event.setCancelled(true);
      if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerInteractAlertCooldown.contains(uuid))
      {
        Variable.playerInteractAlertCooldown.add(uuid);
        MessageUtil.sendTitle(player, "&c행동 불가!", "&r블록과 상호작용할 권한이 없습니다", 5, 80, 15);
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerInteractAlertCooldown.remove(uuid), 100L);
      }
      return;
    }

    // 손에 아이템 있음
    if (itemExists)
    {
      if (NBTAPI.isRestricted(player, item, RestrictionType.NO_USE) ||
              (leftClick && NBTAPI.isRestricted(player, item, RestrictionType.NO_USE_LEFT_CLICK)) ||
              (leftAir && NBTAPI.isRestricted(
                      player, item, RestrictionType.NO_USE_LEFT_CLICK_AIR)) ||
              (leftBlock && NBTAPI.isRestricted(player, item, RestrictionType.NO_USE_LEFT_CLICK_BLOCK)) ||
              (rightClick && NBTAPI.isRestricted(player, item, RestrictionType.NO_USE_RIGHT_CLICK)) ||
              (rightAir && NBTAPI.isRestricted(player, item, RestrictionType.NO_USE_RIGHT_CLICK_AIR)) ||
              (rightBlock && NBTAPI.isRestricted(player, item, RestrictionType.NO_USE_RIGHT_CLICK_BLOCK)) ||
              (airClick && NBTAPI.isRestricted(player, item, RestrictionType.NO_USE_AIR_CLICK)) ||
              (blockClick && NBTAPI.isRestricted(player, item, RestrictionType.NO_USE_BLOCK_CLICK)))
      {
        event.setCancelled(true);
        if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerInteractAlertCooldown.contains(uuid))
        {
          Variable.playerInteractAlertCooldown.add(uuid);
          MessageUtil.sendTitle(player, "&c사용 불가!", "&r사용할 수 없는 아이템입니다", 5, 80, 15);
          SoundPlay.playSound(player, Constant.ERROR_SOUND);
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerInteractAlertCooldown.remove(uuid), 100L);
        }
        return;
      }

      // 손에 아이템 있음, 좌클릭
      if (leftClick)
      {
        if (Variable.itemUseCooldown.contains(uuid))
        {
          event.setCancelled(true);
          return;
        }
        boolean itemUsageLeftClick = this.itemUsage(event, player, item, hand == EquipmentSlot.HAND, false, false);

        if (itemUsageLeftClick)
        {
          Variable.itemUseCooldown.add(uuid);
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.itemUseCooldown.remove(uuid), UserData.ITEM_USE_DELAY.getInt(uuid));
        }

        if (player.isSneaking())
        {
          itemUsageLeftClick = this.itemUsage(event, player, item, hand == EquipmentSlot.HAND, false, true) || itemUsageLeftClick;
          Variable.itemUseCooldown.add(uuid);
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.itemUseCooldown.remove(uuid), UserData.ITEM_USE_DELAY.getInt(uuid));
        }

        if (itemUsageLeftClick)
        {
          event.setCancelled(true);
          return;
        }
      }

      // 손에 아이템 있음 - 우클릭
      if (rightClick)
      {
        if (Variable.itemUseCooldown.contains(uuid))
        {
          event.setCancelled(true);
          return;
        }

        boolean itemUsageRightClick = Method.useItem(player, item, hand);

        if (itemUsageRightClick)
        {
          Variable.itemUseCooldown.add(uuid);
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.itemUseCooldown.remove(uuid), UserData.ITEM_USE_DELAY.getInt(uuid));
          event.setCancelled(true);
        }

        if (player.isSneaking())
        {
          itemUsageRightClick = this.itemUsage(event, player, item, hand == EquipmentSlot.HAND, true, true) || itemUsageRightClick;
          Variable.itemUseCooldown.add(uuid);
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.itemUseCooldown.remove(uuid), UserData.ITEM_USE_DELAY.getInt(uuid));
        }

        if (itemUsageRightClick)
        {
          event.setCancelled(true);
          return;
        }

        if ((player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) && NBTAPI.isRestricted(player, item, RestrictionType.NO_EQUIP))
        {
          PlayerInventory inv = player.getInventory();
          boolean helmet = (Constant.HELMETS.contains(itemType) || itemType == Material.TURTLE_HELMET) && !ItemStackUtil.itemExists(inv.getHelmet());
          boolean chestplate = (Constant.CHESTPLATES.contains(itemType) || itemType == Material.ELYTRA) && !ItemStackUtil.itemExists(inv.getChestplate());
          boolean leggings = Constant.LEGGINGSES.contains(itemType) && !ItemStackUtil.itemExists(inv.getLeggings());
          boolean boots = Constant.BOOTSES.contains(itemType) && !ItemStackUtil.itemExists(inv.getBoots());
          if (helmet || chestplate || leggings || boots)
          {
            event.setCancelled(true);
            if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerInteractAlertCooldown.contains(uuid))
            {
              Variable.playerInteractAlertCooldown.add(uuid);
              MessageUtil.sendTitle(player, "&c장착 불가!", "&r장착할 수 없는 아이템입니다", 5, 80, 15);
              SoundPlay.playSound(player, Constant.ERROR_SOUND);
              Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerInteractAlertCooldown.remove(uuid), 100L);
            }
            return;
          }
        }

        // 손에 아이템 있음 - 허공 우클릭
        //				if (rightAir)
        //				{
        //
        //				}
        // 손에 아이템 있음 - 블록 우클릭
        if (rightBlock)
        {
          boolean noStore = NBTAPI.isRestricted(player, item, RestrictionType.NO_STORE);
          boolean noTrade = NBTAPI.isRestricted(player, item, RestrictionType.NO_TRADE);
          boolean noLectern = NBTAPI.isRestricted(player, item, RestrictionType.NO_LECTERN);
          if (noStore || noTrade || noLectern)
          {
            if ((itemType == Material.WRITTEN_BOOK || itemType == Material.WRITABLE_BOOK) && clickedBlockType == Material.LECTERN)
            {
              event.setCancelled(true);
              System.out.println("2");
              if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerInteractAlertCooldown.contains(uuid))
              {
                Variable.playerInteractAlertCooldown.add(uuid);
                if (noStore)
                {
                  MessageUtil.sendTitle(player, "&c사용 불가!", "&r보관 불가인 책을 독서대에 올릴 수 없습니다", 5, 80, 15);
                }
                else if (noTrade)
                {
                  MessageUtil.sendTitle(player, "&c사용 불가!", "&r캐릭터 귀속인 책을 독서대에 올릴 수 없습니다", 5, 80, 15);
                }
                else
                {
                  MessageUtil.sendTitle(player, "&c사용 불가!", "&r독서대에 올릴 수 없는 아이템입니다", 5, 80, 15);
                }
                SoundPlay.playSound(player, Constant.ERROR_SOUND);
                Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerInteractAlertCooldown.remove(uuid), 100L);
              }
              return;
            }
          }
          if (NBTAPI.isRestricted(player, item, RestrictionType.NO_COMPOSTER))
          {
            if (clickedBlockType == Material.COMPOSTER)
            {
              event.setCancelled(true);
              if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerInteractAlertCooldown.contains(uuid))
              {
                Variable.playerInteractAlertCooldown.add(uuid);
                MessageUtil.sendTitle(player, "&c사용 불가!", "&r퇴비통에 사용할 수 없는 아이템입니다", 5, 80, 15);
                SoundPlay.playSound(player, Constant.ERROR_SOUND);
                Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerInteractAlertCooldown.remove(uuid), 100L);
              }
              return;
            }
          }
          if (NBTAPI.isRestricted(player, item, RestrictionType.NO_CAMPFIRE))
          {
            if (clickedBlockType == Material.CAMPFIRE)
            {
              event.setCancelled(true);
              if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerInteractAlertCooldown.contains(uuid))
              {
                Variable.playerInteractAlertCooldown.add(uuid);
                MessageUtil.sendTitle(player, "&c사용 불가!", "&r모닥불에 사용할 수 없는 아이템입니다", 5, 80, 15);
                SoundPlay.playSound(player, Constant.ERROR_SOUND);
                Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerInteractAlertCooldown.remove(uuid), 100L);
              }
              return;
            }
          }
          if ((!player.isSneaking() && itemType.isBlock()) || !itemType.isBlock())
          {
            switch (itemType)
            {
              case LEATHER_HORSE_ARMOR:
              case LEATHER_BOOTS:
              case LEATHER_CHESTPLATE:
              case LEATHER_HELMET:
              case LEATHER_LEGGINGS:
              case BLACK_BANNER:
              case BLUE_BANNER:
              case BROWN_BANNER:
              case CYAN_BANNER:
              case GRAY_BANNER:
              case GREEN_BANNER:
              case LIGHT_BLUE_BANNER:
              case LIGHT_GRAY_BANNER:
              case LIME_BANNER:
              case MAGENTA_BANNER:
              case ORANGE_BANNER:
              case PINK_BANNER:
              case PURPLE_BANNER:
              case RED_BANNER:
              case WHITE_BANNER:
              case YELLOW_BANNER:
              case BLACK_SHULKER_BOX:
              case BLUE_SHULKER_BOX:
              case BROWN_SHULKER_BOX:
              case CYAN_SHULKER_BOX:
              case GRAY_SHULKER_BOX:
              case GREEN_SHULKER_BOX:
              case LIGHT_BLUE_SHULKER_BOX:
              case LIGHT_GRAY_SHULKER_BOX:
              case LIME_SHULKER_BOX:
              case MAGENTA_SHULKER_BOX:
              case ORANGE_SHULKER_BOX:
              case PINK_SHULKER_BOX:
              case PURPLE_SHULKER_BOX:
              case RED_SHULKER_BOX:
              case WHITE_SHULKER_BOX:
              case YELLOW_SHULKER_BOX:
                if (NBTAPI.isRestricted(player, item, RestrictionType.NO_UNDYE))
                {
                  if (clickedBlockType == Material.CAULDRON)
                  {
                    String blockData = block.getBlockData().getAsString();
                    int level = Integer.parseInt(blockData.split("level=")[1].replace("]", ""));
                    if (level != 0)
                    {
                      event.setCancelled(true);
                      if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerInteractAlertCooldown.contains(uuid))
                      {
                        Variable.playerInteractAlertCooldown.add(uuid);
                        MessageUtil.sendTitle(player, "&c탈색 불가!", "&r가마솥에서 탈색할 수 없는 아이템입니다", 5, 80, 15);
                        SoundPlay.playSound(player, Constant.ERROR_SOUND);
                        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerInteractAlertCooldown.remove(uuid), 100L);
                      }
                      return;
                    }
                  }
                }
              default:
                break;
            }
          }

          if (NBTAPI.isRestricted(player, item, RestrictionType.NO_PLACE))
          {
            BlockFace blockFace = event.getBlockFace();
            if (itemType == Material.ARMOR_STAND)
            {
              if (blockFace != BlockFace.DOWN)
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
            }
            if (itemType == Material.END_CRYSTAL)
            {
              if (clickedBlockType == Material.BEDROCK || clickedBlockType == Material.OBSIDIAN)
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
            }
          }

          // 손에 유리병/포션/양동이/물 양동이 들고 블록에 우클릭
          if (itemType == Material.GLASS_BOTTLE || itemType == Material.POTION || itemType == Material.BUCKET || itemType == Material.WATER_BUCKET ||
                  itemType == Material.LAVA_BUCKET || itemType == Material.POWDER_SNOW_BUCKET)
          {
            if (clickedBlockType == Material.CAULDRON || clickedBlockType == Material.WATER_CAULDRON || clickedBlockType == Material.LAVA_CAULDRON || clickedBlockType == Material.POWDER_SNOW_CAULDRON)
            {
              if (Method.usingLoreFeature(player))
              {
                Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Method.updateInventory(player), 0L);
              }
            }
          }

          // 유리병에 대고 블록에 우클릭
          if (itemType == Material.GLASS_BOTTLE)
          {
            if (clickedBlockType == Material.BEE_NEST || clickedBlockType == Material.BEEHIVE)
            {
              if (Method.usingLoreFeature(player))
              {
                Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Method.updateInventory(player), 0L);
              }
            }
          }
          // 손에 나침반을 든 상태로 블록 우클릭
          if (itemType == Material.COMPASS)
          {
            if (clickedBlockType == Material.LODESTONE)
            {
              if (Method.usingLoreFeature(player))
              {
                if (item.getAmount() == 1 && player.getGameMode() != GameMode.CREATIVE)
                {
                  Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Method.updateInventory(player), 0L);
                }
                else
                {
                  Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                  {
                    ItemStack item2 = null;
                    for (int i = 0; i < player.getInventory().getSize(); i++)
                    {
                      item2 = player.getInventory().getItem(i);
                      if (ItemStackUtil.itemExists(item2) && item2.getType() == Material.COMPASS)
                      {
                        ItemMeta item1Meta = item.getItemMeta(), item2Meta = item2.getItemMeta();
                        CompassMeta compassMeta1 = (CompassMeta) item1Meta, compassMeta2 = (CompassMeta) item2Meta;
                        if (ItemStackUtil.hasLore(item) &&
                                ItemStackUtil.hasLore(item2) &&
                                Objects.requireNonNull(item1Meta.lore()).size() == Objects.requireNonNull(item2Meta.lore()).size() &&
                                !compassMeta1.hasLodestone() &&
                                !compassMeta1.isLodestoneTracked() &&
                                (compassMeta2.hasLodestone() || compassMeta2.isLodestoneTracked()))
                        {
                          item2 = player.getInventory().getItem(i);
                          break;
                        }
                      }
                    }
                    if (item2 != null)
                    {
                      ItemStack item2Clone = item2.clone();
                      player.getInventory().remove(item2);
                      ItemLore.setItemLore(item2Clone);
                      player.getInventory().addItem(item2Clone);
                    }
                    for (int i = 0; i < player.getInventory().getSize(); i++)
                    {
                      item2 = player.getInventory().getItem(i);
                      if (ItemStackUtil.itemExists(item2) && item2.getType() == Material.COMPASS)
                      {
                        ItemMeta item1Meta = item.getItemMeta(), item2Meta = item2.getItemMeta();
                        CompassMeta compassMeta1 = (CompassMeta) item1Meta, compassMeta2 = (CompassMeta) item2Meta;
                        if (ItemStackUtil.hasLore(item) &&
                                ItemStackUtil.hasLore(item2) &&
                                Objects.requireNonNull(item1Meta.lore()).size() == Objects.requireNonNull(item2Meta.lore()).size() &&
                                (compassMeta1.hasLodestone() || compassMeta1.isLodestoneTracked()) &&
                                (compassMeta2.hasLodestone() || compassMeta2.isLodestoneTracked()))
                        {
                          ItemStack item2Clone = item2.clone();
                          player.getInventory().remove(item2);
                          ItemLore.setItemLore(item2Clone);
                          player.getInventory().addItem(item2Clone);
                        }
                      }
                    }
                  }, 0L);
                }
              }
            }
          }

          // 폭죽을 블록에 우클릭 혹은 비료를 퇴비통에 우클릭
          if (itemType == Material.FIREWORK_ROCKET || (Constant.COMPOSTABLE_ITEMS.contains(itemType) && clickedBlockType == Material.COMPOSTER))
          {
            if (player.getGameMode() != GameMode.CREATIVE && NBTAPI.arrayContainsValue(NBTAPI.getStringList(NBTAPI.getMainCompound(item.clone()), CucumberyTag.EXTRA_TAGS_KEY),
                    Constant.ExtraTag.INFINITE.toString()))
            {
              Variable.playerItemConsumeCauseSwapCooldown.add(uuid);
              Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerItemConsumeCauseSwapCooldown.remove(uuid), 0L);
              ItemStack finalItem = item.clone();
              Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> player.getInventory().setItem(event.getHand(), finalItem), 0L);
            }
          }

          // 라이터 혹은 화염구를 TNT에 우클릭
          if ((itemType == Material.FLINT_AND_STEEL || itemType == Material.FIRE_CHARGE) && clickedBlockType == Material.TNT)
          {
            if (!player.isSneaking() && Cucumbery.config.getBoolean("use-static-tnt")
                    && !Method.configContainsLocation(block.getLocation(), Cucumbery.config.getStringList("no-use-static-tnt-location")))
            {
              BlockFace blockFace = event.getBlockFace();
              int x = block.getX(), y = block.getY(), z = block.getZ();
              switch (blockFace)
              {
                case DOWN -> y--;
                case UP -> y++;
                case EAST -> x++;
                case WEST -> x--;
                case SOUTH -> z++;
                case NORTH -> z--;
              }
              Material beforeBlockType = player.getWorld().getBlockAt(x, y, z).getType();

              int finalX = x;
              int finalY = y;
              int finalZ = z;
              Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
              {
                Block afterBlock = player.getWorld().getBlockAt(finalX, finalY, finalZ);
                if (beforeBlockType == Material.AIR && afterBlock.getType() == Material.FIRE)
                {
                  afterBlock.setType(Material.AIR);
                }
              }, 0L);
            }
          }
        }

        // 손에 지도를 든 상태로 우클릭
        if (itemType == Material.MAP)
        {
          if (Method.usingLoreFeature(player))
          {
            Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Method.updateInventory(player), 0L);
          }
        }

        // 손에 유리병을 든 상태로 우클릭
        if (itemType == Material.GLASS_BOTTLE)
        {
          List<Entity> entities = player.getNearbyEntities(5D, 5D, 5D);
          for (Entity entity : entities)
          {
            if (entity.getType() == EntityType.AREA_EFFECT_CLOUD)
            {
              AreaEffectCloud effect = (AreaEffectCloud) entity;
              ProjectileSource shooter = effect.getSource();
              if (shooter instanceof EnderDragon)
              {
                if (Method.usingLoreFeature(player))
                {
                  if (item.getAmount() == 1)
                  {
                    Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                    {
                      ItemStack item2 = null;
                      int slot = -1;
                      for (int i = 0; i < player.getInventory().getSize(); i++)
                      {
                        item2 = player.getInventory().getItem(i);
                        if (ItemStackUtil.itemExists(item2) && item2.getType() == Material.DRAGON_BREATH && !ItemStackUtil.hasLore(item2))
                        {
                          slot = i;
                          break;
                        }
                      }
                      if (item2 != null)
                      {
                        int amount = item2.getAmount();
                        player.getInventory().remove(item2);
                        ItemStack newItem = new ItemStack(Material.DRAGON_BREATH, amount);
                        ItemLore.setItemLore(newItem);
                        player.getInventory().setItem(slot, newItem);
                      }
                    }, 0L);
                  }
                  else
                  {
                    Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                    {
                      ItemStack item2 = null;
                      for (int i = 0; i < player.getInventory().getSize(); i++)
                      {
                        item2 = player.getInventory().getItem(i);
                        if (ItemStackUtil.itemExists(item2) && item2.getType() == Material.DRAGON_BREATH && !ItemStackUtil.hasLore(item2))
                        {
                          break;
                        }
                      }
                      if (item2 != null)
                      {
                        int amount = item2.getAmount();
                        player.getInventory().remove(item2);
                        ItemStack newItem = new ItemStack(Material.DRAGON_BREATH, amount);
                        ItemLore.setItemLore(newItem);
                        player.getInventory().addItem(newItem);
                      }
                    }, 0L);
                  }
                  break;
                }
              }
            }
          }
        }

        if (itemType == Material.ENDER_PEARL && NBTAPI.arrayContainsValue(NBTAPI.getStringList(NBTAPI.getMainCompound(item), CucumberyTag.EXTRA_TAGS_KEY),
                Constant.ExtraTag.NO_COOLDOWN_ENDER_PEARL.toString()))
        {
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> player.setCooldown(Material.ENDER_PEARL, 0), 0L);
        }

        // (손에 아이템을 든 상태로 우클릭) 무한 아이템 사용 모드
        switch (itemType)
        {
          case SPLASH_POTION:
          case LINGERING_POTION:
          case EGG:
          case SNOWBALL:
          case ENDER_EYE:
          case ENDER_PEARL:
          case ELDER_GUARDIAN_SPAWN_EGG:
          case BAT_SPAWN_EGG:
          case BEE_SPAWN_EGG:
          case BLAZE_SPAWN_EGG:
          case CAT_SPAWN_EGG:
          case CAVE_SPIDER_SPAWN_EGG:
          case CHICKEN_SPAWN_EGG:
          case COD_SPAWN_EGG:
          case COW_SPAWN_EGG:
          case CREEPER_SPAWN_EGG:
          case DOLPHIN_SPAWN_EGG:
          case DONKEY_SPAWN_EGG:
          case DROWNED_SPAWN_EGG:
          case ENDERMAN_SPAWN_EGG:
          case ENDERMITE_SPAWN_EGG:
          case EVOKER_SPAWN_EGG:
          case FOX_SPAWN_EGG:
          case GHAST_SPAWN_EGG:
          case GUARDIAN_SPAWN_EGG:
          case HOGLIN_SPAWN_EGG:
          case HORSE_SPAWN_EGG:
          case HUSK_SPAWN_EGG:
          case LLAMA_SPAWN_EGG:
          case MAGMA_CUBE_SPAWN_EGG:
          case MOOSHROOM_SPAWN_EGG:
          case MULE_SPAWN_EGG:
          case OCELOT_SPAWN_EGG:
          case PANDA_SPAWN_EGG:
          case PARROT_SPAWN_EGG:
          case PHANTOM_SPAWN_EGG:
          case PIG_SPAWN_EGG:
          case PIGLIN_BRUTE_SPAWN_EGG:
          case PIGLIN_SPAWN_EGG:
          case PILLAGER_SPAWN_EGG:
          case POLAR_BEAR_SPAWN_EGG:
          case PUFFERFISH_SPAWN_EGG:
          case RABBIT_SPAWN_EGG:
          case RAVAGER_SPAWN_EGG:
          case SALMON_SPAWN_EGG:
          case SHEEP_SPAWN_EGG:
          case SHULKER_SPAWN_EGG:
          case SILVERFISH_SPAWN_EGG:
          case SKELETON_HORSE_SPAWN_EGG:
          case SKELETON_SPAWN_EGG:
          case SLIME_SPAWN_EGG:
          case SPIDER_SPAWN_EGG:
          case SQUID_SPAWN_EGG:
          case STRAY_SPAWN_EGG:
          case STRIDER_SPAWN_EGG:
          case TRADER_LLAMA_SPAWN_EGG:
          case TROPICAL_FISH_SPAWN_EGG:
          case TURTLE_SPAWN_EGG:
          case VEX_SPAWN_EGG:
          case VILLAGER_SPAWN_EGG:
          case VINDICATOR_SPAWN_EGG:
          case WANDERING_TRADER_SPAWN_EGG:
          case WITCH_SPAWN_EGG:
          case WITHER_SKELETON_SPAWN_EGG:
          case WOLF_SPAWN_EGG:
          case ZOGLIN_SPAWN_EGG:
          case ZOMBIE_HORSE_SPAWN_EGG:
          case ZOMBIE_SPAWN_EGG:
          case ZOMBIE_VILLAGER_SPAWN_EGG:
          case ZOMBIFIED_PIGLIN_SPAWN_EGG:
            if (player.getGameMode() != GameMode.CREATIVE && NBTAPI.arrayContainsValue(NBTAPI.getStringList(NBTAPI.getMainCompound(item.clone()), CucumberyTag.EXTRA_TAGS_KEY),
                    Constant.ExtraTag.INFINITE.toString()))
            {
              Variable.playerItemConsumeCauseSwapCooldown.add(uuid);
              Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerItemConsumeCauseSwapCooldown.remove(uuid), 0L);
              ItemStack finalItem = item.clone();
              Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> player.getInventory().setItem(event.getHand(), finalItem), 0L);
            }
            break;
        }
      }
    }
    // 손에 아이템 없음
    //		else
    //		{
    //			// 손에 아이템 없음 - 우클릭
    //			if (rightClick)
    //			{
    //				// 손에 아이템 없음 - 허공 우클릭
    //				if (rightAir)
    //				{
    //
    //				}
    //				// 손에 아이템 없음 - 블록 우클릭
    //				if (rightBlock)
    //				{
    //
    //				}
    //			}
    //		}

    // 우클릭
    if (rightClick)
    {
      // 유리병을 들고 물에 우클릭
      if (itemType == Material.GLASS_BOTTLE)
      {
        Block waterSource = player.getTargetBlock(5, TargetBlockInfo.FluidMode.ALWAYS);
        if (waterSource != null && waterSource.getType() == Material.WATER)
        {
          if (Method.usingLoreFeature(player))
          {
            Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Method.updateInventory(player), 0L);
          }
        }
      }
      // 허공 우클릭
      //			if (rightAir)
      //			{
      //
      //			}
      // 블록 우클릭
      if (rightBlock)
      {
        switch (clickedBlockType)
        {
          case CAMPFIRE:
          case SOUL_CAMPFIRE:
            if (NBTAPI.isRestricted(player, item, RestrictionType.NO_TRADE))
            {
              event.setCancelled(true);
              if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerInteractAlertCooldown.contains(uuid))
              {
                Variable.playerInteractAlertCooldown.add(uuid);
                MessageUtil.sendTitle(player, "&c사용 불가!", ComponentUtil.translate("캐릭터 귀속 아이템은 %s에 사용할 수 없습니다", clickedBlockType), 5, 80, 15);
                SoundPlay.playSound(player, Constant.ERROR_SOUND);
                Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerInteractAlertCooldown.remove(uuid), 100L);
              }
              return;
            }
            break;
          case JUKEBOX:
            boolean noTrade = NBTAPI.isRestricted(player, item, RestrictionType.NO_TRADE), noJukeBox = NBTAPI.isRestricted(player, item, RestrictionType.NO_JUKEBOX);
            if ((noTrade || noJukeBox) && itemType.isRecord())
            {
              event.setCancelled(true);
              if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerInteractAlertCooldown.contains(uuid))
              {
                Variable.playerInteractAlertCooldown.add(uuid);
                MessageUtil.sendTitle(player, "&c사용 불가!", noTrade ? "캐릭터 귀속 아이템은 주크박스에 사용할 수 없습니다" : "주크박스에 사용할 수 없는 아이템입니다", 5, 80, 15);
                SoundPlay.playSound(player, Constant.ERROR_SOUND);
                Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerInteractAlertCooldown.remove(uuid), 100L);
              }
              return;
            }
            break;
          case FLOWER_POT:
            if (NBTAPI.isRestricted(player, item, RestrictionType.NO_FLOWER_POT))
            {
              event.setCancelled(true);
              if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerInteractAlertCooldown.contains(uuid))
              {
                Variable.playerInteractAlertCooldown.add(uuid);
                MessageUtil.sendTitle(player, "&c사용 불가!", "&r화분에 사용할 수 없는 아이템입니다", 5, 80, 15);
                SoundPlay.playSound(player, Constant.ERROR_SOUND);
                Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerInteractAlertCooldown.remove(uuid), 100L);
              }
              return;
            }
            break;
          case RESPAWN_ANCHOR:
            if (NBTAPI.isRestricted(player, item, RestrictionType.NO_RESPAWN_ANCHOR))
            {
              if (Objects.requireNonNull(item).getType() == Material.GLOWSTONE)
              {
                event.setCancelled(true);
                if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerInteractAlertCooldown.contains(uuid))
                {
                  Variable.playerInteractAlertCooldown.add(uuid);
                  MessageUtil.sendTitle(player, "&c사용 불가!", "&r리스폰 정박기에 사용할 수 없는  아이템입니다", 5, 80, 15);
                  SoundPlay.playSound(player, Constant.ERROR_SOUND);
                  Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerInteractAlertCooldown.remove(uuid), 100L);
                }
              }
              return;
            }
            break;
          case LODESTONE:
            if (NBTAPI.isRestricted(player, item, RestrictionType.NO_LODESTONE))
            {
              if (Objects.requireNonNull(item).getType() == Material.COMPASS)
              {
                event.setCancelled(true);
                if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerInteractAlertCooldown.contains(uuid))
                {
                  Variable.playerInteractAlertCooldown.add(uuid);
                  MessageUtil.sendTitle(player, "&c사용 불가!", "&r자석석에 사용할 수 없는  아이템입니다", 5, 80, 15);
                  SoundPlay.playSound(player, Constant.ERROR_SOUND);
                  Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerInteractAlertCooldown.remove(uuid), 100L);
                }
              }
              return;
            }
            break;
          default:
            break;
        }
      }
    }

    // 블록 좌/우클릭
    if (blockClick)
    {
      if (itemType == Material.DEBUG_STICK)
      {
        if (Method.usingLoreFeature(player))
        {
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Method.updateInventory(player), 0L);
        }
      }
    }

    // 경작지
    if (action == Action.PHYSICAL)
    {
      if ((Cucumbery.config.getBoolean("block-player-trample-soil") && !Method.configContainsLocation(Objects.requireNonNull(block).getLocation(),
              Cucumbery.config.getStringList("no-block-player-trample-soil-worlds"))) ||
              UserData.TRAMPLE_SOIL_FORCE.getBoolean(uuid))
      {
        if (Objects.requireNonNull(block).getType() == Material.FARMLAND)
        {
          if (UserData.TRAMPLE_SOIL.getBoolean(uuid) || UserData.TRAMPLE_SOIL_FORCE.getBoolean(uuid))
          {
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setCancelled(true);
            if (UserData.TRAMPLE_SOIL_ALERT.getBoolean(uuid) &&
                    !Permission.EVENT_ERROR_HIDE.has(player) &&
                    !UserData.TRAMPLE_SOIL_NO_ALERT_FORCE.getBoolean(uuid) &&
                    !Variable.soilTrampleAlertCooldown.contains(uuid))
            {
              Variable.soilTrampleAlertCooldown.add(uuid);
              MessageUtil.sendTitle(player, "&c경작지 훼손 불가!", "&r경작지를 훼손할 수 없습니다", 5, 80, 15);
              SoundPlay.playSound(player, Constant.ERROR_SOUND);
              Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.soilTrampleAlertCooldown.remove(uuid), 100L);
            }
            return;
          }
        }
      }
    }

    this.noteBlock(event, itemExists);
    this.customItem(event, itemExists);
  }

  private boolean itemUsage(PlayerInteractEvent event, Player player, ItemStack item, boolean mainHand, boolean rightClick, boolean isSneaking)
  {
    String usageTypeString = "좌클릭";
    String usageType = CucumberyTag.USAGE_COMMANDS_LEFT_CLICK_KEY;
    if (rightClick)
    {
      if (isSneaking)
      {
        usageTypeString = "웅크리고 우클릭";
        usageType = CucumberyTag.USAGE_COMMANDS_SNEAK_RIGHT_CLICK_KEY;
      }
      else
      {
        usageTypeString = "우클릭";
        usageType = CucumberyTag.USAGE_COMMANDS_RIGHT_CLICK_KEY;
      }
    }
    else if (isSneaking)
    {
      usageTypeString = "웅크리고 좌클릭";
      usageType = CucumberyTag.USAGE_COMMANDS_SNEAK_LEFT_CLICK_KEY;
    }
    boolean success = false;
    UUID uuid = player.getUniqueId();
    NBTCompound itemTag = NBTAPI.getMainCompound(item);
    NBTCompound usageTag = NBTAPI.getCompound(itemTag, CucumberyTag.USAGE_KEY);
    NBTCompound usageClickTag = NBTAPI.getCompound(usageTag, usageType);
    NBTCompound cooldownTag = NBTAPI.getCompound(usageClickTag, CucumberyTag.COOLDOWN_KEY);
    String permission = NBTAPI.getString(usageClickTag, CucumberyTag.PERMISSION_KEY);
    if (permission != null && !player.hasPermission(permission))
    {
      MessageUtil.sendWarn(player, ComponentUtil.create("아직 %s을(를) " + usageTypeString + " 사용할 권한이 없습니다.)", item));
      event.setCancelled(true);
      return false;
    }
    if (!UserData.EVENT_EXCEPTION_ACCESS.getBoolean(uuid) && cooldownTag != null)
    {
      try
      {
        long cooldownTime = cooldownTag.getLong(CucumberyTag.TIME_KEY);
        String cooldownTagTag = cooldownTag.getString(CucumberyTag.TAG_KEY);
        YamlConfiguration configPlayerCooldown = Variable.cooldownsItemUsage.get(uuid);
        long nextAvailable = configPlayerCooldown == null ? 0 : configPlayerCooldown.getLong(cooldownTagTag);
        long currentTime = System.currentTimeMillis();
        String remainTime = "&e" + Method.timeFormatMilli(nextAvailable - currentTime);
        if (currentTime < nextAvailable)
        {
          MessageUtil.sendWarn(player, ComponentUtil.translate("아직 %s을(를) " + usageTypeString + " 사용할 수 없습니다 (남은 시간 : %s)", item, remainTime));
          event.setCancelled(true);
          return false;
        }
        if (configPlayerCooldown == null)
        {
          configPlayerCooldown = new YamlConfiguration();
        }
        configPlayerCooldown.set(cooldownTagTag, currentTime + cooldownTime);
        Variable.cooldownsItemUsage.put(uuid, configPlayerCooldown);
      }
      catch (Exception e)
      {
        e.printStackTrace();
        MessageUtil.broadcastDebug("오류");
        // DO NOTHING
      }
    }
    if (usageClickTag != null)
    {
      NBTList<String> commandsTag = NBTAPI.getStringList(usageClickTag, CucumberyTag.USAGE_COMMANDS_KEY);
      if (commandsTag != null)
      {
        for (String command : commandsTag)
        {
          Method.performCommand(player, command, true, true, null);
          success = true;
        }
      }
      if (usageClickTag.hasKey(CucumberyTag.USAGE_DISPOSABLE_KEY))
      {
        success = true;
        double disposableChance = 100d;
        if (usageClickTag.hasKey(CucumberyTag.USAGE_DISPOSABLE_KEY))
        {
          disposableChance = usageClickTag.getDouble(CucumberyTag.USAGE_DISPOSABLE_KEY);
        }
        if (Math.random() * 100d < disposableChance && player.getGameMode() != GameMode.CREATIVE)
        {
          item.setAmount(item.getAmount() - 1);
          if (mainHand)
          {
            player.getInventory().setItemInMainHand(item);
          }
          else
          {
            player.getInventory().setItemInOffHand(item);
          }
        }
      }
    }

    if (success)
    {
      event.setCancelled(true);
      return true;
    }
    return false;
  }

  private void noteBlock(PlayerInteractEvent event, boolean itemExists)
  {
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();
    Action action = event.getAction();
    if (action == Action.RIGHT_CLICK_BLOCK && event.getHand() == EquipmentSlot.HAND)
    {
      if (player.getGameMode() != GameMode.CREATIVE)
      {
        return;
      }
      if (!player.isSneaking())
      {
        return;
      }
      if (!UserData.INVERT_NOTE_BLOCK_PITCH_WHEN_SNEAKING_IN_CREATIVE_MODE.getBoolean(uuid))
      {
        return;
      }
      Block block = event.getClickedBlock();
      if ((block != null ? block.getType() : null) != Material.NOTE_BLOCK)
      {
        return;
      }
      ItemStack item = event.getItem();
      if (itemExists && ItemStackUtil.isPickBlockable(item != null ? item.getType() : Material.AIR))
      {
        return;
      }
      event.setCancelled(true);
      player.incrementStatistic(Statistic.NOTEBLOCK_TUNED);
      NoteBlock noteBlock = (NoteBlock) block.getBlockData();
      if (noteBlock.getNote().equals(Note.sharp(0, Tone.F)))
      {
        noteBlock.setNote(Note.sharp(2, Tone.F));
      }
      else
      {
        noteBlock.setNote(noteBlock.getNote().flattened());
      }
      block.setBlockData(noteBlock);
      if (!NotePlay.customNoteBlockSound(block))
      {
        for (Player online : Bukkit.getServer().getOnlinePlayers())
        {
          if (player.getWorld().equals(online.getWorld()))
          {
            online.playNote(block.getLocation(), noteBlock.getInstrument(), noteBlock.getNote());
          }
        }
        int pitchNum = 0;
        String bds = noteBlock.getAsString();
        String pattern = "note=([0-9]{1,2})";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(bds);
        if (m.find())
        {
          pitchNum = Integer.parseInt(m.group(1));
        }
        Location loc = block.getLocation();
        loc.setX(loc.getX() + 0.5);
        loc.setY(loc.getY() + 1.25);
        loc.setZ(loc.getZ() + 0.5);
        double colorNum = 0.041667 * pitchNum;
        block.getWorld().spawnParticle(Particle.NOTE, loc, 0, colorNum, 0, 0, 1);
      }
    }
    else if (action == Action.LEFT_CLICK_BLOCK)
    {
      if (player.getGameMode() != GameMode.CREATIVE)
      {
        return;
      }
      ItemStack item = event.getItem();
      if (!(player.isSneaking() || (itemExists && Constant.SWORDS.contains(Objects.requireNonNull(item).getType()))))
      {
        return;
      }
      if (!UserData.PLAY_NOTE_BLOCK_WHEN_SNEAKING_IN_CREATIVE_MODE.getBoolean(uuid))
      {
        return;
      }
      Block block = event.getClickedBlock();
      if (Objects.requireNonNull(block).getType() != Material.NOTE_BLOCK)
      {
        return;
      }
      event.setCancelled(true);
      player.incrementStatistic(Statistic.NOTEBLOCK_PLAYED);
      // Custom Note Block Sound
      if (!NotePlay.customNoteBlockSound(block))
      {
        NoteBlock noteBlock = (NoteBlock) block.getBlockData();
        for (Player online : Bukkit.getServer().getOnlinePlayers())
        {
          if (player.getWorld().equals(online.getWorld()))
          {
            online.playNote(block.getLocation(), noteBlock.getInstrument(), noteBlock.getNote());
          }
        }
        int pitchNum = 0;
        String bds = noteBlock.getAsString();
        String pattern = "note=([0-9]{1,2})";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(bds);
        if (m.find())
        {
          pitchNum = Integer.parseInt(m.group(1));
        }
        Location loc = block.getLocation();
        loc.setX(loc.getX() + 0.5);
        loc.setY(loc.getY() + 1.25);
        loc.setZ(loc.getZ() + 0.5);
        double colorNum = 0.041667 * pitchNum;
        block.getWorld().spawnParticle(Particle.NOTE, loc, 0, colorNum, 0, 0, 1);
      }
    }
  }

  private void customItem(PlayerInteractEvent event, boolean itemExists)
  {
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();
    ItemStack item = event.getItem();
    Action action = event.getAction();
    if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK)
    {
      return;
    }
    if (!itemExists)
    {
      return;
    }
    NBTCompound customItemTag = NBTAPI.getCompound(NBTAPI.getMainCompound(item), CucumberyTag.CUSTOM_ITEM_TAG_KEY);
    String id = NBTAPI.getString(customItemTag, CucumberyTag.ID_KEY);
    NBTCompound customItemTagCompound = NBTAPI.getCompound(customItemTag, CucumberyTag.TAG_KEY);
    if (customItemTagCompound == null || id == null)
    {
      return;
    }
    if (id.equals("railgun"))
    {
      int range = 0;
      Double damage = 0D;
      int cooldown = 0;
      int piercing = 1;
      boolean penetration = customItemTagCompound.hasKey(CucumberyTag.CUSTOM_ITEM_RAILGUN_BLOCK_PENETRATE);
      boolean[] isRailgun = new boolean[3];
      boolean useFireworkAmmo = customItemTagCompound.hasKey(CucumberyTag.CUSTOM_ITEM_RAILGUN_FIREWORK_ROCKET_REQUIRED);
      boolean hasAmmo = false;
      boolean sortParticle = customItemTagCompound.hasKey(CucumberyTag.CUSTOM_ITEM_RAILGUN_SORT_PARTICLE);
      double density = 4D;
      double width = 1D;
      int fireworkType = 1;
      boolean ignoreInvincible = customItemTagCompound.hasKey(CucumberyTag.CUSTOM_ITEM_RAILGUN_IGNORE_INVINCIBLE);
      boolean reverse = customItemTagCompound.hasKey(CucumberyTag.CUSTOM_ITEM_RAILGUN_REVERSE);
      boolean suicide = customItemTagCompound.hasKey(CucumberyTag.CUSTOM_ITEM_RAILGUN_SUICIDE);
      if (customItemTagCompound.hasKey(CucumberyTag.CUSTOM_ITEM_RAILGUN_RANGE))
      {
        range = customItemTagCompound.getInteger(CucumberyTag.CUSTOM_ITEM_RAILGUN_RANGE);
        isRailgun[0] = true;
      }
      if (customItemTagCompound.hasKey(CucumberyTag.COOLDOWN_KEY))
      {
        cooldown = (int) (customItemTagCompound.getDouble(CucumberyTag.COOLDOWN_KEY) * 20d);
        isRailgun[1] = true;
      }
      if (customItemTagCompound.hasKey(CucumberyTag.CUSTOM_ITEM_RAILGUN_PIERCING))
      {
        piercing = customItemTagCompound.getInteger(CucumberyTag.CUSTOM_ITEM_RAILGUN_PIERCING) + 1;
      }
      if (customItemTagCompound.hasKey(CucumberyTag.CUSTOM_ITEM_RAILGUN_FIREWORK_TYPE))
      {
        fireworkType = customItemTagCompound.getInteger(CucumberyTag.CUSTOM_ITEM_RAILGUN_FIREWORK_TYPE);
      }
      if (customItemTagCompound.hasKey(CucumberyTag.CUSTOM_ITEM_RAILGUN_DENSITY))
      {
        density = customItemTagCompound.getDouble(CucumberyTag.CUSTOM_ITEM_RAILGUN_DENSITY);
      }
      if (customItemTagCompound.hasKey(CucumberyTag.CUSTOM_ITEM_RAILGUN_DAMAGE))
      {
        damage = customItemTagCompound.getDouble(CucumberyTag.CUSTOM_ITEM_RAILGUN_DAMAGE);
        if (damage == null || damage.isNaN() || damage.isInfinite() || damage > Long.MAX_VALUE)
        {
          damage = 1d * Long.MAX_VALUE;
        }
        isRailgun[2] = true;
      }
      if (customItemTagCompound.hasKey(CucumberyTag.CUSTOM_ITEM_RAILGUN_LASER_WIDTH))
      {
        width = customItemTagCompound.getDouble(CucumberyTag.CUSTOM_ITEM_RAILGUN_LASER_WIDTH);
      }
      if (!isRailgun[0] || !isRailgun[1] || !isRailgun[2])
      {
        MessageUtil.sendWarn(player, "해당 레일건의 필수 태그가 지정되지 않아 사용할 수 없습니다");
        return;
      }
      if (!RAILGUN_WHITE_LIST.contains(uuid))
      {
        if (piercing > 5)
        {
          piercing = 5;
        }
        if (range > 100)
        {
          range = 100;
        }
        if (damage > 1000)
        {
          damage = 1000d;
        }
        if (cooldown < 5)
        {
          cooldown = 5;
        }
        if (fireworkType > 7)
        {
          fireworkType = 7;
        }
        ignoreInvincible = false;
        if (width > 3d)
        {
          width = 3d;
        }
      }
      event.setCancelled(true);
      ItemStack firework = null;
      if (useFireworkAmmo)
      {
        firework = player.getInventory().getItemInOffHand();
        hasAmmo = firework.getType() == Material.FIREWORK_ROCKET;
      }
      if (useFireworkAmmo && !hasAmmo)
      {
        MessageUtil.sendError(player, "탄환(폭죽)이 필요한 총입니다. 다른 손에 폭죽을 들고 사용해주세요.");
        return;
      }
      String cooldownTag = customItemTagCompound.getString(CucumberyTag.CUSTOM_ITEM_RAILGUN_COOLDOWN_TAG);
      if (cooldownTag == null || cooldownTag.equals(""))
      {
        cooldownTag = "railgun-default";
      }
      if (!UserData.EVENT_EXCEPTION_ACCESS.getBoolean(uuid))
      {
        try
        {
          YamlConfiguration configPlayerCooldown = Variable.cooldownsItemUsage.get(uuid);
          long nextAvailable = configPlayerCooldown == null ? 0 : configPlayerCooldown.getLong(cooldownTag);
          long currentTime = System.currentTimeMillis();
          if (currentTime < nextAvailable)
          {
            return;
          }
          if (configPlayerCooldown == null)
          {
            configPlayerCooldown = new YamlConfiguration();
          }
          configPlayerCooldown.set(cooldownTag, currentTime + cooldown * 50L);
          Variable.cooldownsItemUsage.put(uuid, configPlayerCooldown);
        }
        catch (Exception e)
        {
          e.printStackTrace();
          MessageUtil.broadcastDebug("오류");
        }
      }
      this.customItemDurability(event, player, item, 1);
      SoundPlay.playSoundLocation(player.getLocation(), Sound.ENTITY_BLAZE_HURT, 1F, 2F);
      Location origin = reverse ? player.getLocation().add(0d, 0.6, 0d) : player.getEyeLocation();
      double random1 = Math.random(), random2 = Math.random();
      if (CustomEffectManager.hasEffect(player, CustomEffectType.IDIOT_SHOOTER))
      {
        double modifier = (CustomEffectManager.getEffect(player, CustomEffectType.IDIOT_SHOOTER).getAmplifier() + 1) * 5d;
        double newYaw = origin.getYaw() + (random1 * modifier - (modifier / 2d));
        if (newYaw < 0)
        {
          newYaw += 360;
        }
        if (newYaw > 360)
        {
          newYaw -= 360;
        }
        double newPitch = origin.getPitch() + (random2 * modifier - (modifier / 2d));
        if (newPitch < -90)
        {
          newPitch = -180 - newPitch;
        }
        if (newPitch > 90)
        {
          newPitch = 180 - newPitch;
        }
        origin.setPitch((float) newPitch);
        origin.setYaw((float) newYaw);
      }
      boolean isZero = density <= 0D;
      Vector step;
      if (isZero)
      {
        step = origin.getDirection().multiply(0.25D * (reverse ? -1d : 1d));
        density = 4D;
      }
      else
      {
        step = origin.getDirection().multiply(1D / density * (reverse ? -1d : 1d));
      }
      for (int i = 0; i < range * density; i++)
      {
        origin.add(step);

        if (!isZero)
        {
          if (sortParticle)
          {
            Location roundOrigin = new Location(origin.getWorld(), origin.getBlockX() + .5, origin.getBlockY() + .5, origin.getBlockZ() + .5);
            player.getLocation().getWorld().spawnParticle(Particle.FIREWORKS_SPARK, roundOrigin, 1, 0, 0, 0, 0);
          }
          else
          {
            player.getLocation().getWorld().spawnParticle(Particle.FIREWORKS_SPARK, origin, 1, 0, 0, 0, 0);
          }
        }
        if (!Constant.PENETRATABLE_BLOCKS.contains(player.getLocation().getWorld().getBlockAt(origin).getType()) && !penetration)
        {
          break;
        }
      }
      origin = player.getEyeLocation();
      if (CustomEffectManager.hasEffect(player, CustomEffectType.IDIOT_SHOOTER))
      {
        double modifier = (CustomEffectManager.getEffect(player, CustomEffectType.IDIOT_SHOOTER).getAmplifier() + 1) * 5d;
        double newYaw = origin.getYaw() + (random1 * modifier - (modifier / 2d));
        if (newYaw < 0)
        {
          newYaw += 360;
        }
        if (newYaw > 360)
        {
          newYaw -= 360;
        }
        double newPitch = origin.getPitch() + (random2 * modifier - (modifier / 2d));
        if (newPitch < -90)
        {
          newPitch = -180 - newPitch;
        }
        if (newPitch > 90)
        {
          newPitch = 180 - newPitch;
        }
        origin.setPitch((float) newPitch);
        origin.setYaw((float) newYaw);
      }
      step = origin.getDirection().multiply(0.125D * (reverse ? -1d : 1d));
      int current = 0;
      boolean alreadyWarned = false;
      for (int i = 0; i < range * 8; i++)
      {
        if (current >= piercing)
        {
          return;
        }
        origin.add(step);
        if (!Constant.PENETRATABLE_BLOCKS.contains(player.getLocation().getWorld().getBlockAt(origin).getType()) && !penetration)
        {
          break;
        }
        for (Entity entity : player.getWorld().getNearbyEntities(origin, 0.1D * width, 0.1D * width, 0.1D * width))
        {
          if (current >= piercing)
          {
            return;
          }
          EntityType entityType = entity.getType();
          if (entityType == EntityType.ARMOR_STAND)
          {
            continue;
          }
          if (entityType == EntityType.UNKNOWN || entityType == EntityType.ENDER_DRAGON)
          {
            if (!ignoreInvincible && !alreadyWarned)
            {
              MessageUtil.sendWarn(player, ComponentUtil.translate("%s에게는 피해를 입힐 수 없다", entity));
            }
            alreadyWarned = true;
          }
          if (entity instanceof LivingEntity livingEntity && (suicide || entity != player))
          {
            if (livingEntity.getHealth() > 0)
            {
              if (!(livingEntity instanceof Player && ((Player) livingEntity).getGameMode() == GameMode.SPECTATOR) &&
                      (ignoreInvincible || (!(livingEntity instanceof Player) || ((Player) livingEntity).getGameMode() != GameMode.CREATIVE)))
              {
                current++;
              }
              if (entity instanceof Player victim)
              {
                if (victim.getGameMode() != GameMode.SPECTATOR)
                {
                  double health = victim.getHealth();
                  Variable.attackerAndWeapon.put(uuid, item.clone());
                  victim.damage(damage, player);
                  if (ignoreInvincible && health == victim.getHealth())
                  {
                    EntityDamageByEntityEvent damageEvent = new EntityDamageByEntityEvent(player, victim, DamageCause.PROJECTILE, damage);
                    victim.setHealth(Math.max(0, victim.getHealth() - damage));
                    victim.setLastDamageCause(damageEvent);
                    victim.setLastDamage(damage);
                  }
                }
                else if (ignoreInvincible)
                {
                  try
                  {
                    EntityDamageByEntityEvent damageEvent = new EntityDamageByEntityEvent(player, victim, DamageCause.PROJECTILE, damage);
                    victim.setHealth(Math.max(0, victim.getHealth() - damage));
                    victim.setLastDamageCause(damageEvent);
                    victim.setLastDamage(damage);
                  }
                  catch (Exception e)
                  {
                    MessageUtil.broadcastDebug("오류 발생");
                    e.printStackTrace();
                  }
                }
              }
              else
              {
                double health = livingEntity.getHealth();
                Variable.attackerAndWeapon.put(uuid, item.clone());
                livingEntity.damage(damage, player);
                if (ignoreInvincible && health == livingEntity.getHealth())
                {
                  livingEntity.setHealth(Math.max(0, health - damage));
                }
              }
              if (livingEntity.getHealth() <= 0.0D && entity.isDead())
              {
                Firework fw = (Firework) player.getWorld().spawnEntity(entity.getLocation(), EntityType.FIREWORK);
                FireworkMeta fwM = fw.getFireworkMeta();
                fw.setMetadata("no_damage", new FixedMetadataValue(Cucumbery.getPlugin(), "asdddf"));
                if (useFireworkAmmo && hasAmmo)
                {
                  fwM = (FireworkMeta) firework.getItemMeta();
                  fw.setFireworkMeta(fwM);
                  if (player.getGameMode() != GameMode.CREATIVE)
                  {
                    firework.setAmount(firework.getAmount() - 1);
                    player.getInventory().setItemInOffHand(firework);
                  }
                }
                else
                {
                  switch (fireworkType)
                  {
                    case 2 -> {
                      FireworkEffect.Type fireworkEffectType = FireworkEffect.Type.BALL;
                      FireworkEffect effect = FireworkEffect.builder().with(fireworkEffectType).withColor(Color.RED).withFade(Color.ORANGE).build();
                      fwM.addEffect(effect);
                    }
                    case 3 -> {
                      FireworkEffect.Type fireworkEffectType = FireworkEffect.Type.STAR;
                      FireworkEffect effect = FireworkEffect.builder().with(fireworkEffectType).withColor(Color.YELLOW).withFade(Color.WHITE).flicker(true).build();
                      fwM.addEffect(effect);
                    }
                    case 4 -> {
                      FireworkEffect.Type fireworkEffectType = FireworkEffect.Type.CREEPER;
                      FireworkEffect effect = FireworkEffect.builder().with(fireworkEffectType).withColor(Color.GREEN).withFade(Color.RED).build();
                      fwM.addEffect(effect);
                    }
                    case 5 -> {
                      FireworkEffect.Type fireworkEffectType = FireworkEffect.Type.BALL_LARGE;
                      FireworkEffect effect = FireworkEffect.builder().with(fireworkEffectType).withColor(Color.BLUE).build();
                      fwM.addEffect(effect);
                    }
                    case 6 -> {
                      FireworkEffect.Type fireworkEffectType = FireworkEffect.Type.BALL_LARGE;
                      FireworkEffect effect = FireworkEffect.builder().with(fireworkEffectType).withColor(Color.WHITE).trail(true).build();
                      fwM.addEffect(effect);
                    }
                    case 7 -> {
                      FireworkEffect.Type fireworkEffectType = FireworkEffect.Type.STAR;
                      FireworkEffect effect = FireworkEffect.builder().with(fireworkEffectType).withColor(Color.YELLOW).flicker(true).build();
                      fwM.addEffect(effect);
                      fireworkEffectType = FireworkEffect.Type.BALL_LARGE;
                      effect = FireworkEffect.builder().with(fireworkEffectType).withColor(Color.WHITE).build();
                      fwM.addEffect(effect);
                    }
                    case 8 -> {
                      FireworkEffect.Type fireworkEffectType = FireworkEffect.Type.STAR;
                      FireworkEffect effect = FireworkEffect.builder().with(fireworkEffectType).withColor(Color.PURPLE).flicker(true).build();
                      fwM.addEffect(effect);
                      fireworkEffectType = FireworkEffect.Type.CREEPER;
                      effect = FireworkEffect.builder().with(fireworkEffectType).withColor(Color.BLACK).trail(true).build();
                      fwM.addEffect(effect);
                      fireworkEffectType = FireworkEffect.Type.BALL_LARGE;
                      effect = FireworkEffect.builder().with(fireworkEffectType).withColor(Color.fromRGB(60, 0, 60), Color.fromRGB(80, 0, 80), Color.fromRGB(100, 0, 100)).build();
                      fwM.addEffect(effect);
                    }
                    case 9 -> {
                      FireworkEffect.Type fireworkEffectType = FireworkEffect.Type.BURST;
                      FireworkEffect effect = FireworkEffect.builder().with(fireworkEffectType).withColor(Color.RED, Color.YELLOW, Color.ORANGE).flicker(true).trail(true).build();
                      fwM.addEffect(effect);
                      fireworkEffectType = FireworkEffect.Type.CREEPER;
                      effect = FireworkEffect.builder().with(fireworkEffectType).withColor(Color.BLACK).trail(true).build();
                      fwM.addEffect(effect);
                      fireworkEffectType = FireworkEffect.Type.STAR;
                      effect = FireworkEffect.builder().with(fireworkEffectType).withColor(Color.LIME).flicker(true).build();
                      fwM.addEffect(effect);
                      fireworkEffectType = FireworkEffect.Type.BALL;
                      effect = FireworkEffect.builder().with(fireworkEffectType).withColor(Color.LIME).trail(true).build();
                      fwM.addEffect(effect);
                      fireworkEffectType = FireworkEffect.Type.BALL_LARGE;
                      effect = FireworkEffect.builder().with(fireworkEffectType).withColor(Color.AQUA, Color.BLUE, Color.WHITE, Color.BLACK).build();
                      fwM.addEffect(effect);
                    }
                    case 10 -> {
                      int distance = 88;
                      FireworkEffect.Type fireworkEffectType = FireworkEffect.Type.CREEPER;
                      int temp = 0;
                      while (temp <= 255)
                      {
                        FireworkEffect effect = FireworkEffect.builder().with(fireworkEffectType).withColor(Color.fromRGB(255, temp, 0)).build();
                        fwM.addEffect(effect);
                        temp += distance;
                      }
                      temp = 255;
                      while (temp >= 0)
                      {
                        FireworkEffect effect = FireworkEffect.builder().with(fireworkEffectType).withColor(Color.fromRGB(temp, 255, 0)).build();
                        fwM.addEffect(effect);
                        temp -= distance;
                      }
                      temp = 0;
                      while (temp <= 255)
                      {
                        FireworkEffect effect = FireworkEffect.builder().with(fireworkEffectType).withColor(Color.fromRGB(0, 255, temp)).build();
                        fwM.addEffect(effect);
                        temp += distance;
                      }
                      temp = 255;
                      while (temp >= 0)
                      {
                        FireworkEffect effect = FireworkEffect.builder().with(fireworkEffectType).withColor(Color.fromRGB(0, temp, 255)).build();
                        fwM.addEffect(effect);
                        temp -= distance;
                      }
                      temp = 0;
                      while (temp <= 255)
                      {
                        FireworkEffect effect = FireworkEffect.builder().with(fireworkEffectType).withColor(Color.fromRGB(temp, 0, 255)).build();
                        fwM.addEffect(effect);
                        temp += distance;
                      }
                      temp = 255;
                      while (temp >= 0)
                      {
                        FireworkEffect effect = FireworkEffect.builder().with(fireworkEffectType).withColor(Color.fromRGB(255, 0, temp)).build();
                        fwM.addEffect(effect);
                        temp -= distance;
                      }
                    }
                    default -> {
                      FireworkEffect.Type fireworkEffectType = FireworkEffect.Type.BALL;
                      FireworkEffect effect = FireworkEffect.builder().with(fireworkEffectType).withColor(Color.WHITE).build();
                      fwM.addEffect(effect);
                    }
                  }
                  fwM.setPower(0);
                  fw.setFireworkMeta(fwM);
                }
                SoundPlay.playSound(player, Sound.ENTITY_BLAZE_DEATH, 1F, 2F);
                SoundPlay.playSound(player, Sound.ENTITY_GENERIC_EXPLODE, 1F, 2F);
                SoundPlay.playSoundLocation(entity.getLocation(), Sound.ENTITY_BLAZE_DEATH, 1F, 2F);
                SoundPlay.playSoundLocation(entity.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1F, 2F);
              }
            }
          }
        }
      }
    }
  }

  @SuppressWarnings({"unchecked", "all"})
  private void customItemDurability(PlayerInteractEvent event, Player player, @Nullable ItemStack item, int damage)
  {
    UUID uuid = player.getUniqueId();
    if (!ItemStackUtil.itemExists(item))
    {
      return;
    }
    if (!Constant.DURABLE_ITEMS.contains(item.getType()))
    {
      return;
    }
    ItemMeta itemMeta = item.getItemMeta();
    if (player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR && !itemMeta.isUnbreakable())
    {
      boolean breaking = false;
      try
      {
        NBTItem nbtItem = new NBTItem(item);
        NBTCompound itemTag = nbtItem.getCompound(CucumberyTag.KEY_MAIN);
        NBTCompound duraTag = NBTAPI.getCompound(itemTag, CucumberyTag.CUSTOM_DURABILITY_KEY);
        long curDura = duraTag.getLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY);
        curDura -= damage;
        breaking = curDura <= 0;
        duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY, curDura);
        switch (Objects.requireNonNull(event.getHand()))
        {
          case HAND:
            player.getInventory().setItemInMainHand(nbtItem.getItem());
            break;
          case OFF_HAND:
            player.getInventory().setItemInOffHand(nbtItem.getItem());
            break;
          default:
            break;
        }
        Method.updateInventory(player);
      }
      catch (Exception e)
      {
        Damageable duraMeta = (Damageable) itemMeta;
        duraMeta.setDamage(duraMeta.getDamage() + damage);
        item.setItemMeta((ItemMeta) duraMeta);
      }
      Damageable duraMeta = (Damageable) itemMeta;
      if (breaking || duraMeta.getDamage() >= item.getType().getMaxDurability())
      {
        if (UserData.SHOW_ITEM_BREAK_TITLE.getBoolean(uuid) && Cucumbery.config.getBoolean("send-title-on-item-break"))
        {
          if (!Method.configContainsLocation(player.getLocation(), Cucumbery.getPlugin().getConfig().getStringList("no-send-title-on-item-break-worlds")))
          {
            MessageUtil.sendTitle(player, ComponentUtil.translate("&c장비 파괴됨!"),
                    ComponentUtil.translate("&e인벤토리 아이템 중 %s이(가) 파괴되었습니다", item), 5, 100, 15);
          }
        }
//        Method.itemBreakParticle(player, item);
//        Method.playSoundLocation(player.getLocation(), Sound.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, Math.random() / 3d + 0.8);
        item.setAmount(item.getAmount() - 1);
        if (item.getAmount() > 0)
        {
          duraMeta.setDamage(0);
          item.setItemMeta((ItemMeta) duraMeta);
          try
          {
            ItemStack itemClone = item.clone();
            NBTItem nbtItem = new NBTItem(itemClone);
            NBTCompound itemTag = nbtItem.getCompound(CucumberyTag.KEY_MAIN);
            NBTCompound duraTag = NBTAPI.getCompound(itemTag, CucumberyTag.CUSTOM_DURABILITY_KEY);
            long maxDura = duraTag.getLong(CucumberyTag.CUSTOM_DURABILITY_MAX_KEY);
            duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY, maxDura);
            itemClone = nbtItem.getItem();
            item.setItemMeta(itemClone.getItemMeta());
          }
          catch (Exception ignored)
          {

          }
        }
        switch (Objects.requireNonNull(event.getHand()))
        {
          case HAND:
            player.getInventory().setItemInMainHand(item);
            break;
          case OFF_HAND:
            player.getInventory().setItemInOffHand(item);
            break;
          default:
            break;
        }
      }
    }
    Method.updateInventory(player);
  }
}

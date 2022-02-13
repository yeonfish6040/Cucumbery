package com.jho5245.cucumbery.util.storage.data;

import com.jho5245.cucumbery.util.no_groups.BossBarMessage;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.maxgamer.quickshop.api.shop.Shop;

import java.util.*;

public class Variable
{
  public static Set<UUID> viewDamage = new HashSet<>();

  public static Set<UUID> blockBreakAlertCooldown = new HashSet<>();

  public static Set<UUID> blockBreakCropHarvestAlertCooldown = new HashSet<>();

  public static Set<UUID> blockBreakAlertCooldown2 = new HashSet<>();

  public static Set<UUID> blockPlaceAlertCooldown = new HashSet<>();

  public static Set<UUID> blockPlaceAlertCooldown2 = new HashSet<>();

  public static Set<UUID> itemUseCooldown = new HashSet<>();

  public static Set<UUID> itemDropAlertCooldown = new HashSet<>();

  public static Set<UUID> itemDropAlertCooldown2 = new HashSet<>();

  public static Set<UUID> itemDropAlertCooldown3 = new HashSet<>();

  public static Set<UUID> itemDropAlertCooldown4 = new HashSet<>();

  public static Set<UUID> itemDropDisabledAlertCooldown = new HashSet<>();

  public static Set<UUID> itemPickupAlertCooldown = new HashSet<>();

  public static Set<UUID> itemPickupScrollReinforcingAlertCooldown = new HashSet<>();

  public static Set<UUID> itemPickupAlertCooldown2 = new HashSet<>();

  public static Set<UUID> soilTrampleAlertCooldown = new HashSet<>();

  public static Set<UUID> itemConsumeAlertCooldown = new HashSet<>();

  public static Set<UUID> itemConsumeAlertCooldown2 = new HashSet<>();

  public static Set<UUID> playerChatAlertCooldown = new HashSet<>();

  public static Set<UUID> playerItemHeldAlertCooldown = new HashSet<>();

  public static Set<UUID> playerSwapHandItemsAlertCooldown = new HashSet<>();

  public static Set<UUID> playerCommandPreprocessAlertCooldown = new HashSet<>();

  public static Set<UUID> playerInteractAlertCooldown = new HashSet<>();

  public static Set<UUID> playerHurtEntityAlertCooldown = new HashSet<>();

  public static Set<UUID> antispecateAlertCooldown = new HashSet<>();

  public static Set<UUID> playerChatNoSpamAlertCooldown = new HashSet<>();

  public static HashMap<UUID, String> playerChatSameMessageSpamAlertCooldown = new HashMap<>();

  public static Set<UUID> playerInteractAtEntityRestrictedItemAlertCooldown = new HashSet<>();

  public static Set<UUID> playerInteractAtEntityAlertCooldown = new HashSet<>();

  public static Set<UUID> inventoryOpenAlertCooldown = new HashSet<>();

  public static Set<UUID> playerMoveAlertCooldown = new HashSet<>();

  public static Set<UUID> playerFishAlertCooldown = new HashSet<>();

  public static Set<UUID> playerShootBowAlertCooldown = new HashSet<>();

  public static Set<UUID> playerBucketUseAlertCooldown = new HashSet<>();

  public static Set<UUID> scrollReinforcing = new HashSet<>();

  public static Set<UUID> playerNotifyIfInventoryIsFullCooldown = new HashSet<>();

  public static Set<UUID> playerItemConsumeCauseSwapCooldown = new HashSet<>();

  public static Set<UUID> playerTakeLecternBookAlertCooldown = new HashSet<>();

  public static Set<UUID> playerLecternChangePageAlertCooldown = new HashSet<>();

  public static Set<UUID> playerLoadCrossbowAlertCooldown = new HashSet<>();

  public static Set<UUID> playerChangeBeaconEffectAlertCooldown = new HashSet<>();

  /**
   * 플레이어가 신호기 효과를 변경할 때 사용하는 아이템이 이벤트가 취소될 경우 바닥에 버리는 것을 방지하기 위함(인벤토리가 가득 찼을때는 제외)
   */
  public static Set<UUID> playerChangeBeaconEffectItemDropCooldown = new HashSet<>();

  public static Set<UUID> playerActionbarCooldownIsShowing = new HashSet<>();

  public static HashMap<UUID, Long> broadcastItemCooldown = new HashMap<>();

  public static HashMap<UUID, Material[]> playerNotifyIfInventoryIsFullCheckInventory = new HashMap<>();

  public static HashMap<UUID, Integer> playerNotifyChatIfInventoryIsFullStack = new HashMap<>();

  public static HashMap<UUID, Location> spectateUpdater = new HashMap<>();

  public static HashMap<UUID, ItemStack> rpg_bow = new HashMap<>();

  public static HashMap<UUID, Integer> rpg_bow2 = new HashMap<>();

  /**
   * UUID에 대한 유저 데이터 config 파일
   */
  public static HashMap<UUID, YamlConfiguration> userData = new HashMap<>();

  /**
   * 유저 데이터 폴더의 uuid 파일 목록
   */
  public static Set<String> userDataUUIDs = new HashSet<>();

  public static YamlConfiguration allPlayerConfig = new YamlConfiguration();

  public static YamlConfiguration deathMessages = new YamlConfiguration();

  public static YamlConfiguration lang = new YamlConfiguration();

  /**
   * @String 파일 경로와 이름
   * @YamlConfiguration config 파일
   */
  public static HashMap<String, YamlConfiguration> warps = new HashMap<>();
  /**
   * @String 파일 경로와 이름
   * @YamlConfiguration config 파일
   */
  public static HashMap<String, YamlConfiguration> itemStorage = new HashMap<>();
  /**
   * @String 파일 경로와 이름
   * @YamlConfiguration config 파일
   */
  public static HashMap<String, YamlConfiguration> commandPacks = new HashMap<>();
  /**
   * @String 파일 경로와 이름
   * @YamlConfiguration config 파일
   */
  public static HashMap<String, YamlConfiguration> customRecipes = new HashMap<>();

  /**
   * @String 파일 경로와 이름
   * @YamlConfiguration config 파일
   */
  public static HashMap<String, YamlConfiguration> customItems = new HashMap<>();

  public static HashMap<UUID, YamlConfiguration> craftingTime = new HashMap<>();

  public static HashMap<UUID, YamlConfiguration> craftsLog = new HashMap<>();

  public static HashMap<UUID, YamlConfiguration> lastCraftsLog = new HashMap<>();

  public static HashMap<UUID, YamlConfiguration> cooldownsItemUsage = new HashMap<>();

  public static Set<String> songFiles = new HashSet<>();

  /**
   * 플레이어들의 uuid, id, displayname, listname 모음
   */
  public static Set<String> nickNames = new HashSet<>();

  /**
   * 플레이어들의 id, displayname, listname에 대한 uuid 값 (닉네임을 넣어서 uuid를 반환)
   */
  public static HashMap<String, UUID> cachedUUIDs = new HashMap<>();

  /**
   * 블록을 설치했을 때 해당 아이템의 NBT 보존
   */
  public static HashMap<String, YamlConfiguration> blockPlaceData = new HashMap<>();

  public static HashMap<String, YamlConfiguration> customGameRules = new HashMap<>();
  /**
   * QuickShop 상점 데이터
   */
  public static List<Shop> shops = new ArrayList<>();

  /**
   * 원거리 공격 시 사용한 아이템과 해당 개체를 저장
   */
  public static HashMap<UUID, ItemStack> projectile = new HashMap<>();

  public static HashMap<UUID, Entity> victimAndDamager = new HashMap<>();

  public static HashMap<UUID, Long> damagerAndCurrentTime = new HashMap<>();

  public static HashMap<UUID, ItemStack> attackerAndWeapon = new HashMap<>();

  public static HashMap<UUID, String> attackerAndWeaponString = new HashMap<>();

  public static HashMap<UUID, ItemStack> victimAndBlockDamager = new HashMap<>();

  public static HashMap<String, Long> blockDamagerAndCurrentTime = new HashMap<>();

  public static HashMap<String, ItemStack> blockAttackerAndWeapon = new HashMap<>();

  public static HashMap<String, ItemStack> blockAttackerAndBlock = new HashMap<>();

  public static HashMap<UUID, String> entityAndSourceLocation = new HashMap<>();

  public static HashMap<UUID, ItemStack> lastTrampledBlock = new HashMap<>();

  public static HashMap<UUID, Material> lastTrampledBlockType = new HashMap<>();

  public static HashMap<UUID, BossBar> customEffectBossBarMap = new HashMap<>();

  public static HashMap<UUID, List<BossBarMessage>> sendBossBarMap = new HashMap<>();

  public static Set<UUID> darknessTerrorFlag = new HashSet<>();

  public static HashMap<UUID, String> customDeathMessageKey = new HashMap<>();

  public static HashMap<UUID, Collection<PotionEffect>> buffFreezerEffects = new HashMap<>();

  public static HashMap<UUID, List<InventoryView>> lastInventory = new HashMap<>();
}

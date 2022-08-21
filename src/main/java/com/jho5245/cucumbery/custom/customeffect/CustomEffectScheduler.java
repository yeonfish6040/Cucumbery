package com.jho5245.cucumbery.custom.customeffect;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.commands.sound.CommandSong;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect.DisplayType;
import com.jho5245.cucumbery.custom.customeffect.children.group.*;
import com.jho5245.cucumbery.custom.customeffect.custom_mining.MiningManager;
import com.jho5245.cucumbery.custom.customeffect.custom_mining.MiningResult;
import com.jho5245.cucumbery.custom.customeffect.type.*;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectRemoveEvent.RemoveReason;
import com.jho5245.cucumbery.util.additemmanager.AddItemUtil;
import com.jho5245.cucumbery.util.gui.GUIManager;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.itemlore.ItemLoreView;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.no_groups.*;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.bossbar.BossBar.Color;
import net.kyori.adventure.bossbar.BossBar.Overlay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.entity.FishHook.HookState;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * see {@link Scheduler#tickSchedules()}
 */
public class CustomEffectScheduler
{
  public static final HashMap<UUID, BukkitTask> spreadTimerTask = new HashMap<>();
  public static final Set<UUID> spreadTimerSet = new HashSet<>();
  private static final Set<UUID> darknessTerrorTimer = new HashSet<>(), darknessTerrorTimer2 = new HashSet<>();
  private static final Set<UUID> podagraTimer = new HashSet<>();

  public static void tick(@NotNull Entity entity)
  {
    List<CustomEffect> customEffects = CustomEffectManager.getEffects(entity);
    if (customEffects.isEmpty())
    {
      return;
    }
    for (CustomEffect customEffect : customEffects)
    {
      if (customEffect instanceof RealDurationCustomEffect realDurationCustomEffect)
      {
        long current = System.currentTimeMillis(), endTime = realDurationCustomEffect.getEndTimeInMillis();
        int remain = (int) ((endTime - current) / 50L);
        // 시간이 다 되면 바로 없애는게 아니라 0.05초 지연을 줌 (실제 개체가 효과를 가지고 있는 판정은 생기지 않음)
        if (customEffect.getDuration() != -1)
        {
          customEffect.setDuration(Math.max(1, remain));
        }
      }
      if (customEffect instanceof PlayerCustomEffect playerCustomEffect)
      {
        Player player = playerCustomEffect.getPlayer();
        UUID uuid1 = player.getUniqueId();
        if (!player.isOnline() || !player.isValid())
        {
          Player newPlayer = Bukkit.getPlayer(uuid1);
          if (newPlayer != null)
          {
            playerCustomEffect.setPlayer(newPlayer);
          }
        }
      }
      customEffect.tick();
      if (customEffect.getDuration() == 0)
      {
        CustomEffectManager.removeEffect(entity, customEffect.getType(), customEffect.getAmplifier(), RemoveReason.TIME_OUT);
      }
    }
  }

  public static void display(@NotNull Player player)
  {
    UUID uuid = player.getUniqueId();
    List<CustomEffect> customEffects = new ArrayList<>(CustomEffectManager.getEffects(player, DisplayType.ACTION_BAR));
    if (!customEffects.isEmpty())
    {
      customEffects.removeIf(CustomEffect::isHidden);
      @NotNull List<CustomEffect> finalCustomEffects = new ArrayList<>(customEffects);
      customEffects.removeIf(e ->
      {
        CustomEffectType customEffectType = e.getType();
        for (CustomEffect effect : finalCustomEffects)
        {
          if (effect.getType() == customEffectType && !customEffectType.isStackDisplayed() && effect.getAmplifier() > e.getAmplifier())
          {
            return true;
          }
        }
        return false;
      });
      MessageUtil.sendActionBar(player, CustomEffectManager.getDisplay(player, customEffects, customEffects.size() <= 10));
    }
    customEffects = new ArrayList<>(CustomEffectManager.getEffects(player, DisplayType.BOSS_BAR));
    customEffects.removeIf(CustomEffect::isHidden);
    @NotNull List<CustomEffect> finalCustomEffects = new ArrayList<>(customEffects);
    customEffects.removeIf(e ->
    {
      CustomEffectType customEffectType = e.getType();
      for (CustomEffect effect : finalCustomEffects)
      {
        if (effect.getType() == customEffectType && !customEffectType.isStackDisplayed() && effect.getAmplifier() > e.getAmplifier())
        {
          return true;
        }
      }
      return false;
    });
    List<PotionEffect> potionEffects = CustomEffectManager.removeDisplay(player, player.getActivePotionEffects());
    boolean showPotionEffect = Cucumbery.config.getBoolean("show-vanilla-potion-effects-on-bossbar") && !potionEffects.isEmpty();
    if (!showPotionEffect && customEffects.isEmpty())
    {
      if (Cucumbery.config.getBoolean("show-boss-bar-when-no-effects"))
      {
        if (!Variable.customEffectBossBarMap.containsKey(uuid))
        {
          BossBar bossBar = BossBar.bossBar(Component.empty(), 0f, Color.WHITE, Overlay.PROGRESS);
          Variable.customEffectBossBarMap.put(uuid, bossBar);
        }
        BossBar bossBar = Variable.customEffectBossBarMap.get(uuid);
        player.showBossBar(bossBar.color(Color.WHITE).progress(0f)
                .name(
                        ComponentUtil.translate("&7적용 중인 효과 없음")
                ));
      }
      else
      {
        if (Variable.customEffectBossBarMap.containsKey(uuid))
        {
          player.hideBossBar(Variable.customEffectBossBarMap.get(uuid));
          Variable.customEffectBossBarMap.remove(uuid);
        }
      }
    }
    else
    {
      int size = customEffects.size() + potionEffects.size();
      boolean showDuration = size <= 10;
      if (!Variable.customEffectBossBarMap.containsKey(uuid))
      {
        BossBar bossBar = BossBar.bossBar(Component.empty(), 1f, BossBar.Color.GREEN, Overlay.PROGRESS);
        Variable.customEffectBossBarMap.put(uuid, bossBar);
      }
      BossBar bossBar = Variable.customEffectBossBarMap.get(uuid);
      Component display = Component.empty();
      if (!customEffects.isEmpty())
      {
        display = display.append(CustomEffectManager.getDisplay(player, customEffects, showDuration));
      }
      if (showPotionEffect)
      {
        if (!customEffects.isEmpty())
        {
          display = display.append(Component.text(showDuration ? ", " : ","));
        }
        display = display.append(CustomEffectManager.getVanillaDisplay(player, potionEffects, showDuration));
      }
      bossBar.name(ComponentUtil.stripEvent(display));
      boolean allIsPositive = true, allIsNegative = true;
      long totalTick = 0, currentTick = 0;
      for (CustomEffect customEffect : customEffects)
      {
        int initDuration = customEffect.getInitDuration();
        int duration = customEffect.getDuration();
        totalTick += (initDuration == -1 || customEffect.isTimeHidden() ? 0 : initDuration);
        currentTick += (duration == -1 || customEffect.isTimeHidden() ? 0 : duration);
        if (customEffect.getType().isNegative())
        {
          allIsPositive = false;
        }
        else
        {
          allIsNegative = false;
        }
      }
      for (PotionEffect potionEffect : potionEffects)
      {
        int initDuration = -1;
        int duration = potionEffect.getDuration();
        if (Variable.potionEffectApplyMap.containsKey(uuid))
        {
          HashMap<String, Integer> hashMap = Variable.potionEffectApplyMap.get(uuid);
          if (hashMap.containsKey(potionEffect.getType().translationKey()))
          {
            initDuration = hashMap.get(potionEffect.getType().translationKey());
            if (duration > initDuration)
            {
              hashMap.put(potionEffect.getType().translationKey(), duration);
              initDuration = duration;
            }
          }
        }
        totalTick += (initDuration == -1 || initDuration > 20 * 60 * 60 * 24 * 365 ? 0 : initDuration);
        currentTick += (duration == -1 || duration > 20 * 60 * 60 * 24 * 365 ? 0 : duration);
        if (CustomEffectManager.isVanillaNegative(potionEffect.getType()))
        {
          allIsPositive = false;
        }
        else
        {
          allIsNegative = false;
        }
      }
      if (allIsNegative)
      {
        bossBar.color(Color.RED);
      }
      else if (allIsPositive)
      {
        bossBar.color(Color.GREEN);
      }
      else
      {
        if (size >= 10)
        {
          bossBar.color(Color.PURPLE);
        }
        else
        {
          bossBar.color(Color.YELLOW);
        }
      }
      float progress = 1f;
      if (totalTick > 0)
      {
        progress = 1f * currentTick / totalTick;
      }
      progress = Math.min(Math.max(progress, 0f), 1f);
      player.showBossBar(bossBar.progress(progress));
    }
    customEffects = CustomEffectManager.getEffects(player, DisplayType.PLAYER_LIST);
    if (!customEffects.isEmpty())
    {
      Component component = Component.empty();
      component = component.append(Component.text("\n"));
      component = component.append(
              ComponentUtil.translate("rg255,204;적용 중인 효과 목록 : %s개", "&2" + customEffects.size())
      );
      component = component.append(Component.text("\n"));
      for (CustomEffect customEffect : customEffects)
      {
        int duration = customEffect.getDuration();
        int amplifier = customEffect.getAmplifier();
        component = component.append(Component.text("\n"));
        component = component.append(
                ComponentUtil.translate(amplifier == 0 ? "%1$s%2$s" : "%1$s %3$s%2$s", customEffect,
                        (duration != -1 && duration != customEffect.getInitDuration() - 1) ?
                                " (" + Method.timeFormatMilli(duration * 50L, duration < 200, 1, true) + ")" :
                                ""
                        , amplifier + 1)
        );
      }
      player.sendPlayerListFooter(ComponentUtil.stripEvent(component));
    }
    InventoryView inventoryView = player.getOpenInventory();
    Component title = inventoryView.title();
    if (GUIManager.isGUITitle(title) && title instanceof TranslatableComponent translatableComponent && translatableComponent.args().get(1) instanceof TextComponent textComponent && textComponent.content().equals(Constant.POTION_EFFECTS))
    {
      CustomEffectGUI.openGUI(player, false);
    }
  }

  public static void rune(@NotNull Player player)
  {
    CustomEffect customEffect = CustomEffectManager.getEffectNullable(player, CustomEffectTypeRune.RUNE_USING);
    if (customEffect instanceof StringCustomEffect stringCustomEffect)
    {
      String s = stringCustomEffect.getString();
      MessageUtil.sendTitle(player, s, ItemStackUtil.itemExists(player.getInventory().getItemInMainHand()) ? "룬을 해방하려면 마우스를 순서대로 클릭하세요. R = 우클릭, L = 좌클릭" : "&c룬을 해방하려면 손에 아이템을 들고 있어야 합니다! R = 우클릭, L = 좌클릭", 0, 3, 0);
    }
  }

  public static void gaesans(@NotNull Player player)
  {
    if (CustomEffectManager.hasEffect(player, CustomEffectType.GAESANS) && player.isSneaking() && ((Entity) player).isOnGround())
    {
      player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 2, 0, false, false, false));
    }
  }

  public static void customMining(@NotNull Player player)
  {
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
    HashMap<Location, Long> map = Variable.customMiningCooldown;
    map.keySet().removeIf(location ->
    {
      if (map.get(location) <= 0 && !Variable.customMiningExtraBlocks.containsKey(location))
      {
        location.getBlock().getState().update();
        return true;
      }
      return false;
    });
    for (Location location : map.keySet())
    {
      map.put(location, map.get(location) - 1);
      Bukkit.getOnlinePlayers().forEach(p ->
              {
                Block block = location.getBlock();
                BlockData blockData = Bukkit.createBlockData(block.getType().isCollidable() ? Material.BEDROCK : Material.AIR);
                if (Variable.customMiningExtraBlocks.containsKey(location))
                {
                  blockData = Variable.customMiningExtraBlocks.get(location);
                }
                p.sendBlockChange(location, blockData);
              }
      );
    }
    if (player.getGameMode() != GameMode.CREATIVE && CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
    {
      player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 8, 0, false, false, false));
      player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 8, 8, false, false, false));
      CustomEffectManager.addEffect(player, new AttributeCustomEffectImple(CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_ADJUST_VANILLA_SPEED, UUID.randomUUID(), Attribute.GENERIC_ATTACK_SPEED, Operation.MULTIPLY_SCALAR_1, 90d / 11d));
    }
    if (CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_PROGRESS))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_PROGRESS);
      if (customEffect instanceof LocationCustomEffect effect)
      {
        Location location = effect.getLocation();
        UUID uuid = player.getUniqueId();
        Block block = location.getBlock();
        ItemStack itemStack = player.getInventory().getItemInMainHand().clone();
        if (ItemStackUtil.itemExists(itemStack))
        {
          NBTItem nbtItem = new NBTItem(itemStack);
          CustomMaterial customMaterial = Method2.valueOf(nbtItem.getString("id") + "", CustomMaterial.class);
          if (customMaterial != null)
          {
            switch (customMaterial)
            {
              case TITANIUM_DRILL_R266, TITANIUM_DRILL_R366, TITANIUM_DRILL_R466, TITANIUM_DRILL_R566 ->
              {
                NBTCompound itemTag = nbtItem.getCompound(CucumberyTag.KEY_MAIN);
                NBTCompound duraTag = itemTag != null ? itemTag.getCompound(CucumberyTag.CUSTOM_DURABILITY_KEY) : null;
                if (duraTag != null)
                {
                  long maxDura = duraTag.getLong(CucumberyTag.CUSTOM_DURABILITY_MAX_KEY), curDura = duraTag.getLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY);
                  if (curDura <= 1)
                  {
                    MessageUtil.sendWarn(player, "%s의 연료가 다 떨어져서 사용할 수 없습니다!", itemStack);
                    player.sendBlockDamage(new Location(Bukkit.getWorlds().get(0), 0, 0, 0), 0f);
                    CustomEffectManager.removeEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_PROGRESS);
                    return;
                  }
                  double ratio = curDura * 1d / maxDura;
                  if (ratio == 0.5)
                  {
                    MessageUtil.info(player, "%s의 연료가 50%% 남았습니다!", itemStack);
                    SoundPlay.playSound(player, Sound.UI_BUTTON_CLICK);
                  }
                  else if (ratio == 0.2)
                  {
                    MessageUtil.info(player, "%s의 연료가 20%% 남았습니다!", itemStack);
                    SoundPlay.playSound(player, Sound.UI_BUTTON_CLICK);
                  }
                  else if (ratio == 0.1)
                  {
                    MessageUtil.info(player, "%s의 연료가 10%% 남았습니다!", itemStack);
                    SoundPlay.playSound(player, Sound.UI_BUTTON_CLICK);
                  }
                  else if (ratio == 0.05)
                  {
                    MessageUtil.info(player, "%s의 연료가 5%% 남았습니다!", itemStack);
                    SoundPlay.playSound(player, Sound.UI_BUTTON_CLICK);
                  }
                  else if (ratio == 0.01)
                  {
                    MessageUtil.info(player, "%s의 연료가 1%% 남았습니다!", itemStack);
                    SoundPlay.playSound(player, Sound.UI_BUTTON_CLICK);
                  }
                }
              }
            }
          }
        }
        MiningResult miningResult = MiningManager.getMiningInfo(player, location);
        if (miningResult == null)
        {
          player.sendBlockDamage(new Location(Bukkit.getWorlds().get(0), 0, 0, 0), 0f);
          CustomEffectManager.removeEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_PROGRESS);
          return;
        }
        if (!miningResult.canMine() || miningResult.blockHardness() == -1)
        {
          player.sendBlockDamage(new Location(Bukkit.getWorlds().get(0), 0, 0, 0), 0f);
          return;
        }
        int blockTier = miningResult.blockTier(), miningTier = miningResult.miningTier();
        List<ItemStack> drops = miningResult.drops();
        if (blockTier > miningTier)
        {
          // 채광 등급이 0이면 경고조차 하지 않음
          if (miningTier > 0 && !Variable.customMiningTierAlertCooldown.contains(uuid))
          {
            MessageUtil.sendWarn(player, "%s을(를) 캐기 위해 더 높은 등급의 도구가 필요합니다!", drops.isEmpty() ? block.getType() : drops.get(0));
            Variable.customMiningTierAlertCooldown.add(uuid);
            Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.customMiningTierAlertCooldown.remove(uuid), 40L);
          }
          player.sendBlockDamage(new Location(Bukkit.getWorlds().get(0), 0, 0, 0), 0f);
          return;
        }
        float origin = Variable.customMiningProgress.getOrDefault(uuid, 0f);
        float damage = miningResult.miningSpeed() / 20f / miningResult.blockHardness();
        Variable.customMiningProgress.put(uuid, origin + damage);
        float progress = Variable.customMiningProgress.getOrDefault(uuid, 0f);
        progress = Math.max(0f, Math.min(1f, progress));
        if (progress >= 1 || miningResult.blockHardness() == Float.MIN_VALUE) // insta break
        {
          if (!block.getType().isAir())
          {
            SoundGroup soundGroup = block.getBlockSoundGroup();
            if (Variable.customMiningExtraBlocks.containsKey(location))
            {
              soundGroup = Bukkit.createBlockData(Material.POLISHED_DIORITE).getSoundGroup();
            }
            player.playSound(player.getLocation(), soundGroup.getBreakSound(), SoundCategory.BLOCKS, 1f, soundGroup.getPitch() * 0.8f);
          }
          if (itemStack.getEnchantmentLevel(CustomEnchant.TELEKINESIS) > 0 || CustomEffectManager.hasEffect(player, CustomEffectType.TELEKINESIS))
          {
            AddItemUtil.addItem(player, drops);
          }
          else
          {
            drops.forEach(itemStack1 -> player.getWorld().dropItemNaturally(location, itemStack1));
          }
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
          Variable.customMiningProgress.put(uuid, 0f);
          boolean noRestoreMode = CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_2_NO_RESTORE);
          if (CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_2) || noRestoreMode)
          {
            if (noRestoreMode)
            {
              block.setType(Material.AIR);
              YamlConfiguration configuration = Variable.blockPlaceData.get(location.getWorld().getName());
              if (configuration != null)
              {
                configuration.set(location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ(), null);
                Variable.blockPlaceData.put(location.getWorld().getName(), configuration);
              }
            }
            else
            {
              final BlockData originData = block.getBlockData().clone();
              final Location locationClone = location.clone();
              Variable.customMiningMode2BlockData.put(locationClone, originData);
              boolean isWater = originData instanceof Waterlogged waterlogged && waterlogged.isWaterlogged();
              block.setBlockData(Bukkit.createBlockData(isWater ? Material.WATER : Material.AIR), false);
              Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
              {
                if (isWater && block.getType() == Material.WATER || !isWater && block.getType().isAir())
                {
                  block.setBlockData(originData, false);
                }
                Variable.customMiningMode2BlockData.remove(locationClone);
                // 채광 모드 2에서는 재생 속도가 느려짐
              }, (long) (miningResult.regenCooldown() * Cucumbery.config.getDouble("custom-mining.mining-mode-2-regen-multiplier")));
            }
          }
          else
          {
            Location locationClone = location.clone();
            boolean extraBlockIgnoreCooldown = false;
            if (Variable.customMiningExtraBlocks.containsKey(locationClone))
            {
              extraBlockIgnoreCooldown = true;
              Variable.customMiningExtraBlocks.remove(locationClone);
            }
            else if (!drops.isEmpty() && !drops.get(0).getType().isAir())
            {
              ItemStack drop = drops.get(0);
              String id = new NBTItem(drop).getString("id") + "";
              CustomMaterial customMaterial = Method2.valueOf(id, CustomMaterial.class);
              if (customMaterial != null)
              {
                switch (customMaterial)
                {
                  case MITHRIL_INGOT, MITHRIL_ORE ->
                  {
                    double chance = 0.05;
                    CustomEffect titaniumFinder = CustomEffectManager.getEffectNullable(player, CustomEffectTypeCustomMining.TITANIUM_FINDER);
                    if (titaniumFinder != null)
                    {
                      chance += (titaniumFinder.getAmplifier() + 1) * 0.001;
                    }
                    if (Math.random() < chance)
                    {
                      Variable.customMiningExtraBlocks.put(locationClone, Bukkit.createBlockData(Material.POLISHED_DIORITE));
                    }
                  }
                }
              }
              else
              {
                switch (block.getType())
                {
                  case STONE -> Variable.customMiningExtraBlocks.put(locationClone, Bukkit.createBlockData(Material.COBBLESTONE));
                  case DEEPSLATE -> Variable.customMiningExtraBlocks.put(locationClone, Bukkit.createBlockData(Material.COBBLED_DEEPSLATE));
                }
              }
            }
            if (!extraBlockIgnoreCooldown)
            {
              Variable.customMiningCooldown.put(locationClone, (long) miningResult.regenCooldown());
            }
          }
          CustomEffectManager.removeEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_PROGRESS);
          if (ItemStackUtil.itemExists(itemStack) && itemStack.hasItemMeta() && !itemStack.getItemMeta().isUnbreakable())
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
                if (duraTag.hasKey(CucumberyTag.CUSTOM_DURABILITY_CHANCE_NOT_TO_CONSUME_DURABILITY))
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
              }
              if (currentDurability <= 0)
              {
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
                  ItemMeta itemMeta = itemStack.getItemMeta();
                  org.bukkit.inventory.meta.Damageable damageable = (org.bukkit.inventory.meta.Damageable) itemMeta;
                  damageable.setDamage((int) (maxDurability - currentDurability));
                  itemStack.setItemMeta(itemMeta);
                }
              }
              player.getInventory().setItemInMainHand(ItemLore.setItemLore(itemStack, ItemLoreView.of(player)));
            }
          }
          return;
        }
        if (progress > 0)
        {
          if (progress > 0.01)
          {
            player.sendBlockDamage(location, progress);
            float finalProgress = progress;
            Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> player.sendBlockDamage(location, finalProgress), 0L);
            Bukkit.getScheduler().runTaskLaterAsynchronously(Cucumbery.getPlugin(), () -> player.sendBlockDamage(location, finalProgress), 0L);
          }
          if (UserData.SHOW_PLUGIN_DEV_DEBUG_MESSAGE.getBoolean(uuid))
          {
            MessageUtil.sendTitle(player, "", Constant.Sosu2Force.format(progress * 100d) + "%", 0, 3, 0);
          }
        }
      }
    }
  }

  public static void masterOfFishing(@NotNull Player player)
  {
    if (CustomEffectManager.hasEffect(player, CustomEffectType.MASTER_OF_FISHING) || CustomEffectManager.hasEffect(player, CustomEffectType.MASTER_OF_FISHING_D))
    {
      FishHook fishHook = player.getFishHook();
      if (fishHook != null && fishHook.getState() == HookState.BOBBING)
      {
        fishHook.setWaitTime(1);
      }
    }
  }

  public static void dynamicLight(@NotNull Player player)
  {
    if (CustomEffectManager.hasEffect(player, CustomEffectType.DYNAMIC_LIGHT))
    {
      PlayerInventory inventory = player.getInventory();
      Material mainHand = inventory.getItemInMainHand().getType(), offHand = inventory.getItemInOffHand().getType();
      if (Constant.OPTIFINE_DYNAMIC_LIGHT_ITEMS.contains(mainHand) || Constant.OPTIFINE_DYNAMIC_LIGHT_ITEMS.contains(offHand))
      {
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 4, 0, false, false, false));
      }
    }
  }

  public static void vanillaEffect(@NotNull Entity entity)
  {
    if (!(entity instanceof LivingEntity livingEntity))
    {
      return;
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_SPEED))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_SPEED);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_SLOWNESS))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_SLOWNESS);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_HASTE))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_HASTE);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_MINING_FATIGUE))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_MINING_FATIGUE);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_INSTANT_HEALTH))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_INSTANT_HEALTH);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_INSTANT_DAMAGE))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_INSTANT_DAMAGE);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.HARM, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_JUMP_BOOST))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_JUMP_BOOST);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_NAUSEA))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_NAUSEA);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 62, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_REGENERATION))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_REGENERATION);
      PotionEffect potionEffect = livingEntity.getPotionEffect(PotionEffectType.REGENERATION);
      if (potionEffect == null || (potionEffect.getDuration() <= 48 && potionEffect.getAmplifier() == 0))
      {
        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 98, customEffect.getAmplifier(), false, false, false));
      }
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_RESISTANCE))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_RESISTANCE);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_FIRE_RESISTANCE))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_FIRE_RESISTANCE);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_WATER_BREATHING))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_WATER_BREATHING);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_INVISIBILITY))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_INVISIBILITY);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_BLINDNESS))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_BLINDNESS);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 21, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_NIGHT_VISION))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_NIGHT_VISION);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 4, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_HUNGER))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_HUNGER);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_WEAKNESS))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_WEAKNESS);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_POISON))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_POISON);
      PotionEffect potionEffect = livingEntity.getPotionEffect(PotionEffectType.POISON);
      if (potionEffect == null || (potionEffect.getDuration() <= 48 && potionEffect.getAmplifier() == 0))
      {
        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 98, customEffect.getAmplifier(), false, false, false));
      }
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_WITHER))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_WITHER);
      PotionEffect potionEffect = livingEntity.getPotionEffect(PotionEffectType.WITHER);
      if (potionEffect == null || (potionEffect.getDuration() <= 48 && potionEffect.getAmplifier() == 0))
      {
        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 98, customEffect.getAmplifier(), false, false, false));
      }
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_HEALTH_BOOST))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_HEALTH_BOOST);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_ABSORPTION))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_ABSORPTION);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_SATURATION))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_SATURATION);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_GLOWING))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_GLOWING);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_LEVITATION))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_LEVITATION);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_LUCK))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_LUCK);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_UNLUCK))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_UNLUCK);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.UNLUCK, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_SLOW_FALLING))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_SLOW_FALLING);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_CONDUIT_POWER))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_CONDUIT_POWER);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.CONDUIT_POWER, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_DOLPHINS_GRACE))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_DOLPHINS_GRACE);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_BAD_OMEN))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_BAD_OMEN);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.BAD_OMEN, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_HERO_OF_THE_VILLAGE))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_HERO_OF_THE_VILLAGE);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_DARKNESS))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectTypeMinecraft.MINECRAFT_DARKNESS);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 60, customEffect.getAmplifier(), false, false, false));
    }
  }

  public static void gliding(@NotNull Player player)
  {
    if (player.isGliding())
    {
      CustomEffectManager.addEffect(player, CustomEffectType.GLIDING);
    }
  }

  public static void starCatch(@NotNull Player player)
  {
    if (CustomEffectManager.hasEffect(player, CustomEffectTypeReinforce.STAR_CATCH_PROCESS))
    {
      int duration = CustomEffectManager.getEffect(player, CustomEffectTypeReinforce.STAR_CATCH_PROCESS).getDuration();
      boolean ok = false;
      Integer penalty = Variable.starCatchPenalty.get(player.getUniqueId());
      if (penalty == null)
      {
        penalty = 0;
      }
      int level = Math.min(4, penalty / 20);
      Outter:
      for (int i = 1; i <= 10; i++)
      {
        switch (level)
        {
          case 0 ->
          {
            if (duration >= i * 20 - 14 && duration <= i * 20 - 6)
            {
              ok = true;
              break Outter;
            }
          }
          case 1 ->
          {
            if (duration >= i * 20 - 13 && duration <= i * 20 - 7)
            {
              ok = true;
              break Outter;
            }
          }
          case 2 ->
          {
            if (duration >= i * 20 - 11 && duration <= i * 20 - 8)
            {
              ok = true;
              break Outter;
            }
          }
          case 3 ->
          {
            if (duration >= i * 20 - 10 && duration <= i * 20 - 9)
            {
              ok = true;
              break Outter;
            }
          }
          default ->
          {
            if (duration == i * 20 - 10)
            {
              ok = true;
              break Outter;
            }
          }
        }
      }
      String meh = "---------------------";
      char[] m = meh.toCharArray();
      int offset = duration % 40;
      if (offset <= 20)
      {
        m[offset] = '★';
      }
      else
      {
        m[40 - offset] = '★';
      }
      if (offset == 0 || offset == 20)
      {
        player.playSound(player.getLocation(), "star_catch_" + (level + 1), SoundCategory.PLAYERS, 2F, 1F);
      }
      meh = "" + new String(m) + "";
      MessageUtil.sendTitle(player, ComponentUtil.translate(meh.replace("★", "%s").replace("-", "─"), (ok ? "&a" : "&c") + "★"), Constant.THE_COLOR_HEX + Constant.Jeongsu.format(duration / 20), 0, 5, 0);
    }
  }

  public static void damageIndicator(@NotNull Entity entity)
  {
    if (!(entity instanceof ArmorStand))
    {
      return;
    }
    if (CustomEffectManager.hasEffect(entity, CustomEffectType.DAMAGE_INDICATOR))
    {
      Location location = entity.getLocation().add(0, 0.02, 0);
      entity.teleport(location);
    }
  }

  public static void townShield(@NotNull Player player)
  {
    if (CustomEffectManager.hasEffect(player, CustomEffectType.TOWN_SHIELD))
    {
      player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 2, 0, true, false, false));
    }
  }

  public static void spreadAndVariation(@NotNull Player player)
  {
    if (player.getGameMode() == GameMode.SPECTATOR)
    {
      return;
    }
    if (CustomEffectManager.hasEffect(player, CustomEffectType.SPREAD) && player.getGameMode() != GameMode.SPECTATOR)
    {
      CustomEffect customEffectSpread = CustomEffectManager.getEffect(player, CustomEffectType.SPREAD);
      int spreadDuration = customEffectSpread.getDuration(), spreadAmplifier = customEffectSpread.getAmplifier();
      int effectSize = 0;
      boolean detoxicate = CustomEffectManager.hasEffect(player, CustomEffectType.VAR_DETOXICATE);
      boolean pneumonia = CustomEffectManager.hasEffect(player, CustomEffectType.VAR_PNEUMONIA);
      boolean podagra = CustomEffectManager.hasEffect(player, CustomEffectType.VAR_PODAGRA);
      boolean stomachache = CustomEffectManager.hasEffect(player, CustomEffectType.VAR_STOMACHACHE);
      Collection<Player> nearbyPlayers = player.getWorld().getNearbyPlayers(player.getLocation(), spreadAmplifier + 1);
      for (Player nearbyPlayer : nearbyPlayers)
      {
        if (nearbyPlayer.getGameMode() == GameMode.SPECTATOR)
        {
          continue;
        }
        CustomEffectManager.addEffect(nearbyPlayer, customEffectSpread);
        if (detoxicate)
        {
          CustomEffectManager.addEffect(nearbyPlayer, CustomEffectManager.getEffect(player, CustomEffectType.VAR_DETOXICATE));
        }
        if (pneumonia)
        {
          CustomEffectManager.addEffect(nearbyPlayer, CustomEffectManager.getEffect(player, CustomEffectType.VAR_PNEUMONIA));
        }
        if (podagra)
        {
          CustomEffectManager.addEffect(nearbyPlayer, CustomEffectManager.getEffect(player, CustomEffectType.VAR_PODAGRA));
        }
        if (stomachache)
        {
          CustomEffectManager.addEffect(nearbyPlayer, CustomEffectManager.getEffect(player, CustomEffectType.VAR_STOMACHACHE));
        }
      }
      if (detoxicate)
      {
        effectSize++;
      }
      if (pneumonia)
      {
        effectSize++;
      }
      if (podagra)
      {
        effectSize++;
      }
      if (stomachache)
      {
        effectSize++;
      }

      UUID uuid = player.getUniqueId();
      if (!spreadTimerSet.contains(uuid))
      {
        spreadTimerSet.add(uuid);
        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> spreadTimerSet.remove(uuid), 20 * 60L);
        spreadTimerTask.put(uuid, bukkitTask);
        if (Math.random() < 0.0008)
        {
          if (effectSize == 4)
          {
            switch ((int) (Math.random() * 4))
            {
              case 0 -> CustomEffectManager.removeEffect(player, CustomEffectType.VAR_PODAGRA);
              case 1 -> CustomEffectManager.removeEffect(player, CustomEffectType.VAR_STOMACHACHE);
              case 2 -> CustomEffectManager.removeEffect(player, CustomEffectType.VAR_PNEUMONIA);
              default -> CustomEffectManager.removeEffect(player, CustomEffectType.VAR_DETOXICATE);
            }
          }
          else if (effectSize == 1)
          {
            List<CustomEffectType> effectTypes = new ArrayList<>(Arrays.asList(CustomEffectType.VAR_PNEUMONIA, CustomEffectType.VAR_PODAGRA, CustomEffectType.VAR_DETOXICATE, CustomEffectType.VAR_STOMACHACHE));
            if (detoxicate)
            {
              effectTypes.remove(CustomEffectType.VAR_DETOXICATE);
            }
            if (pneumonia)
            {
              effectTypes.remove(CustomEffectType.VAR_PNEUMONIA);
            }
            if (podagra)
            {
              effectTypes.remove(CustomEffectType.VAR_PODAGRA);
            }
            if (stomachache)
            {
              effectTypes.remove(CustomEffectType.VAR_STOMACHACHE);
            }
            CustomEffectManager.addEffect(player, new CustomEffect(effectTypes.get((int) (Math.random() * effectTypes.size())), spreadDuration, spreadAmplifier));
          }
          else if (effectSize > 0)
          {
            boolean add = Math.random() < 0.5;
            List<CustomEffectType> effectTypes = new ArrayList<>(Arrays.asList(CustomEffectType.VAR_PNEUMONIA, CustomEffectType.VAR_PODAGRA, CustomEffectType.VAR_DETOXICATE, CustomEffectType.VAR_STOMACHACHE));
            if ((add && detoxicate) || (!add && !detoxicate))
            {
              effectTypes.remove(CustomEffectType.VAR_DETOXICATE);
            }
            if ((add && pneumonia) || (!add && !pneumonia))
            {
              effectTypes.remove(CustomEffectType.VAR_PNEUMONIA);
            }
            if ((add && podagra) || (!add && !podagra))
            {
              effectTypes.remove(CustomEffectType.VAR_PODAGRA);
            }
            if ((add && stomachache) || (!add && !stomachache))
            {
              effectTypes.remove(CustomEffectType.VAR_STOMACHACHE);
            }
            if (add)
            {
              CustomEffectManager.addEffect(player, new CustomEffect(effectTypes.get((int) (Math.random() * effectTypes.size())), spreadDuration, spreadAmplifier));
            }
            else
            {
              CustomEffectManager.removeEffect(player, effectTypes.get((int) (Math.random() * effectTypes.size())));
            }
          }
        }
      }

      if (CustomEffectManager.hasEffect(player, CustomEffectType.VAR_PODAGRA_ACTIVATED) && !player.isSneaking())
      {
        if (podagraTimer.contains(uuid))
        {
          return;
        }
        podagraTimer.add(uuid);
        Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> podagraTimer.remove(uuid), 60);
        player.damage(0.2 + CustomEffectManager.getEffect(player, CustomEffectType.VAR_PODAGRA_ACTIVATED).getAmplifier() * 0.1);
        player.setNoDamageTicks(0);
      }
      if (pneumonia)
      {
        boolean skip = player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR || player.hasPotionEffect(PotionEffectType.WATER_BREATHING);
        Block eyeBlock = player.getEyeLocation().getBlock();
        boolean isInWater = eyeBlock.getType() == Material.WATER || (eyeBlock.getBlockData() instanceof Waterlogged waterlogged && waterlogged.isWaterlogged());
        if (isInWater && !skip && player.getRemainingAir() > 0)
        {
          int amplifier = CustomEffectManager.getEffect(player, CustomEffectType.VAR_PNEUMONIA).getAmplifier();
          double multiplier = (amplifier + 1) * 0.1;
          double decimalNumer = multiplier - (int) multiplier;
          if (Math.random() < decimalNumer)
          {
            player.setRemainingAir((int) Math.floor(player.getRemainingAir() - multiplier));
          }
          else
          {
            player.setRemainingAir((int) Math.ceil(player.getRemainingAir() - multiplier));
          }
        }
        else if (player.getRemainingAir() > -16 && player.getRemainingAir() < player.getMaximumAir())
        {
          player.setRemainingAir(player.getRemainingAir() - 4);
        }
      }
    }
  }

  @SuppressWarnings("all")
  public static void trueInvisibility(@NotNull Entity entity)
  {
    Set<String> scoreboardTags = entity.getScoreboardTags();
    if (scoreboardTags.contains("no_cucumbery_true_invisibility") || CustomEffectManager.hasEffect(entity, CustomEffectType.COMBO_EXPERIENCE))
    {
      return;
    }
    if (scoreboardTags.contains("true_invisibility") || CustomEffectManager.hasEffect(entity, CustomEffectType.TRUE_INVISIBILITY))
    {
      for (Player player : Bukkit.getOnlinePlayers())
      {
        if (entity != player && player.canSee(entity))
        {
          player.hideEntity(Cucumbery.getPlugin(), entity);
        }
      }
    }
    else
    {
      for (Player player : Bukkit.getOnlinePlayers())
      {
        if (entity != player && !player.canSee(entity))
        {
          player.showEntity(Cucumbery.getPlugin(), entity);
        }
      }
    }
  }

  public static void fancySpotlight(@NotNull Player player)
  {
    if (CustomEffectManager.hasEffect(player, CustomEffectType.FANCY_SPOTLIGHT))
    {
      Location location = player.getEyeLocation();
      byte blockLight = location.getBlock().getLightFromBlocks();
      if (blockLight > 10)
      {
        CustomEffectManager.addEffect(player, CustomEffectType.FANCY_SPOTLIGHT_ACTIVATED);
      }
    }
    if (CustomEffectManager.hasEffect(player, CustomEffectType.FANCY_SPOTLIGHT_ACTIVATED))
    {
      player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2, 0, false, false, false));
      PotionEffect potionEffect = player.getPotionEffect(PotionEffectType.REGENERATION);
      if (potionEffect == null || (potionEffect.getDuration() <= 48 && potionEffect.getAmplifier() == 0))
      {
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 98, 0, false, false, false));
      }
    }
  }

  public static void newbieShield(@NotNull Player player)
  {
    int playTime = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
    if (playTime < 20 * 60 * 20)
    {
      CustomEffectManager.addEffect(player, new CustomEffect(CustomEffectType.NEWBIE_SHIELD, 2, 2));
    }
    else if (playTime < 20 * 60 * 40)
    {
      CustomEffectManager.addEffect(player, new CustomEffect(CustomEffectType.NEWBIE_SHIELD, 2, 1));
    }
    else if (playTime < 20 * 60 * 60)
    {
      CustomEffectManager.addEffect(player, new CustomEffect(CustomEffectType.NEWBIE_SHIELD));
    }
  }

  public static void darknessTerror(@NotNull Player player)
  {
    if (CustomEffectManager.hasEffect(player, CustomEffectType.DARKNESS_TERROR_RESISTANCE))
    {
      CustomEffectManager.removeEffect(player, CustomEffectType.DARKNESS_TERROR_ACTIVATED);
      return;
    }
    if (!darknessTerrorTimer.contains(player.getUniqueId()))
    {
      if (!darknessTerrorTimer2.contains(player.getUniqueId()))
      {
        Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> darknessTerrorTimer.add(player.getUniqueId()), 40L);
      }
      darknessTerrorTimer2.add(player.getUniqueId());
      return;
    }
    darknessTerrorTimer.remove(player.getUniqueId());
    darknessTerrorTimer2.remove(player.getUniqueId());
    Location location = player.getEyeLocation();
    Material mainHand = player.getInventory().getItemInMainHand().getType(), offHand = player.getInventory().getItemInOffHand().getType();
    if (CustomEffectManager.hasEffect(player, CustomEffectType.DARKNESS_TERROR) &&
            location.getWorld().getEnvironment() == Environment.NORMAL &&
            player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR && !player.hasPotionEffect(PotionEffectType.NIGHT_VISION) &&
            !location.getBlock().isSolid() && location.getBlock().getLightLevel() == 0
            && !(Constant.OPTIFINE_DYNAMIC_LIGHT_ITEMS.contains(mainHand) || Constant.OPTIFINE_DYNAMIC_LIGHT_ITEMS.contains(offHand)))
    {
      CustomEffectManager.addEffect(player, new CustomEffect(CustomEffectType.DARKNESS_TERROR_ACTIVATED));
    }
    else
    {
      CustomEffectManager.removeEffect(player, CustomEffectType.DARKNESS_TERROR_ACTIVATED);
    }
  }

  public static void serverRadio(@NotNull Player player)
  {
    if (!UserData.LISTEN_GLOBAL.getBoolean(player) || !Cucumbery.using_NoteBlockAPI || CommandSong.radioSongPlayer == null || CommandSong.song == null)
    {
      CustomEffectManager.removeEffect(player, CustomEffectType.SERVER_RADIO_LISTENING);
    }
    else
    {
      Song song = CommandSong.song;
      float speed = song.getSpeed();
      int length = song.getLength();
      int amplifier = (int) Math.floor(length / 100d / speed);
      amplifier = Math.min(4, Math.max(0, amplifier));
      CustomEffect customEffect = CustomEffectManager.getEffectNullable(player, CustomEffectType.SERVER_RADIO_LISTENING);
      if (customEffect == null || customEffect.getAmplifier() != amplifier)
      {
        CustomEffectManager.removeEffect(player, CustomEffectType.SERVER_RADIO_LISTENING);
        CustomEffectManager.addEffect(player, new CustomEffect(CustomEffectType.SERVER_RADIO_LISTENING, CustomEffectType.SERVER_RADIO_LISTENING.getDefaultDuration(), amplifier));
      }
    }
  }

  public static void axolotlsGrace(@NotNull Entity entity)
  {

    if (!(entity instanceof AnimalTamer && entity instanceof Damageable damageable))
    {
      return;
    }
    if (damageable.getHealth() > 5d)
    {
      return;
    }
    List<Entity> entities = entity.getNearbyEntities(15, 15, 15);
    for (Entity loop : entities)
    {
      if (loop instanceof Parrot parrot && parrot.getOwner() == entity)
      {
        CustomEffectManager.addEffect(entity, new CustomEffect(CustomEffectType.PARROTS_CHEER));
        break;
      }
    }
  }

  public static void trollInventoryProperty(@NotNull Player player)
  {
    InventoryView inventoryView = player.getOpenInventory();
    if (inventoryView.getType() == InventoryType.CRAFTING)
    {
      return;
    }
    if (CustomEffectManager.hasEffect(player, CustomEffectType.TROLL_INVENTORY_PROPERTY))
    {
      int min = CustomEffectManager.hasEffect(player, CustomEffectType.TROLL_INVENTORY_PROPERTY_MIN) ? CustomEffectManager.getEffect(player, CustomEffectType.TROLL_INVENTORY_PROPERTY_MIN).getAmplifier() : 0;
      CustomEffect customEffect = CustomEffectManager.getEffect(player, CustomEffectType.TROLL_INVENTORY_PROPERTY);
      for (Property property : Property.values())
      {
        player.setWindowProperty(property, Method.random(min, customEffect.getAmplifier()));
      }
    }
  }

  public static void stop(@NotNull Entity entity)
  {
    if (CustomEffectManager.hasEffect(entity, CustomEffectType.STOP))
    {
      if (!(entity instanceof LivingEntity))
      {
        entity.setVelocity(new Vector(0, 0.04, 0));
      }
    }
  }
}

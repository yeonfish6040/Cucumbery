package com.jho5245.cucumbery.custom.customeffect.custom_mining;

import com.destroystokyo.paper.Namespaced;
import com.destroystokyo.paper.NamespacedTag;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeCustomMining;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeMinecraft;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.*;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Constant.ExtraTag;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTList;
import de.tr7zw.changeme.nbtapi.NBTType;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MiningManager
{

  /**
   * Gets the result of mining of a {@link Player}.
   *
   * @param player        The player to get mining result.
   * @param blockLocation Location of currently mining.
   * @return Player's mining result or null if current block is on cooldown(bedrock)
   */
  @Nullable
  public static MiningResult getMiningInfo(@NotNull Player player, @NotNull Location blockLocation)
  {
    if (Variable.customMiningCooldown.containsKey(blockLocation) && !Variable.customMiningExtraBlocks.containsKey(blockLocation))
    {
      return null;
    }
    if (Cucumbery.using_WorldGuard)
    {
      LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
      RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
      RegionQuery query = container.createQuery();
      com.sk89q.worldedit.util.Location location = BukkitAdapter.adapt(blockLocation);
      if (!UserData.EVENT_EXCEPTION_ACCESS.getBoolean(player) && !query.testState(location, localPlayer, Flags.BUILD) && !query.testState(location, localPlayer, Flags.BLOCK_BREAK))
      {
        return null;
      }
    }
    ItemStack itemStack = player.getInventory().getItemInMainHand().clone();
    if (NBTAPI.isRestricted(player, itemStack, RestrictionType.NO_BREAK))
    {
      return null;
    }
    Block block = blockLocation.getBlock();
    Material blockType = block.getType();
    if (blockType == Material.FIRE)
    {
      return null;
    }
    boolean hasAllowedBlocks = CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_ALLOWED_BLOCKS);
    if (hasAllowedBlocks)
    {
      switch (blockType)
      {
        case STONE, STONE_STAIRS, STONE_SLAB, COAL_ORE, COPPER_ORE ->
        {
        }
        default ->
        {
          return null;
        }
      }
    }
    if (CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_ALLOWED_BLOCKS_MINE))
    {
      switch (blockType)
      {
        case STONE, GRANITE, DIORITE, ANDESITE, DEEPSLATE, CALCITE, TUFF, BASALT, POLISHED_BASALT, SMOOTH_BASALT, OBSIDIAN, BLACKSTONE, GRAY_CONCRETE, GRAY_WOOL,
                WAXED_WEATHERED_CUT_COPPER, WAXED_WEATHERED_COPPER, WAXED_OXIDIZED_COPPER, END_STONE_BRICKS, LAPIS_BLOCK, BLUE_WOOL, BLUE_ICE, PACKED_ICE,
                DARK_PRISMARINE, PRISMARINE_BRICKS, PRISMARINE, LIGHT_BLUE_WOOL, POLISHED_ANDESITE, SMOOTH_STONE, BLACK_WOOL, BLACK_CONCRETE,
                STRIPPED_MANGROVE_WOOD, MANGROVE_PLANKS, CRIMSON_HYPHAE, NETHER_WART_BLOCK, GREEN_WOOL, GREEN_TERRACOTTA, GREEN_GLAZED_TERRACOTTA, LIME_WOOL,
                COAL_ORE, COPPER_ORE, DEEPSLATE_COAL_ORE, DEEPSLATE_COPPER_ORE, DEEPSLATE_DIAMOND_ORE, DEEPSLATE_EMERALD_ORE, DEEPSLATE_GOLD_ORE, DEEPSLATE_IRON_ORE,
                DEEPSLATE_LAPIS_ORE, DEEPSLATE_REDSTONE_ORE, DIAMOND_ORE, EMERALD_ORE, GOLD_ORE, IRON_ORE, LAPIS_ORE, NETHER_GOLD_ORE, NETHER_QUARTZ_ORE, REDSTONE_ORE,
                RAW_COPPER_BLOCK, RAW_GOLD_BLOCK, RAW_IRON_BLOCK, COAL_BLOCK ->
        {

        }
        default ->
        {
          return null;
        }
      }
    }
    boolean ignoreVanillaModification = false;
    {
      try
      {
        NBTItem nbtItem = new NBTItem(itemStack);
        if (nbtItem.hasTag("IgnoreVanillaModification") && nbtItem.getType("IgnoreVanillaModification") == NBTType.NBTTagByte)
        {
          ignoreVanillaModification = nbtItem.getBoolean("IgnoreVanillaModification");
        }
      }
      catch (Exception ignored)
      {
      }
    }
    int blockTier = getVanillaBlockTier(blockType), toolTier = getToolTier(itemStack);
    float blockHardness = getBlockHardness(blockType), toolSpeed = getToolSpeed(itemStack);
    boolean toolSpeedZero = toolSpeed <= 0f;
    float multiplier = 100f;
    YamlConfiguration config = Cucumbery.config;
    if (config.contains("custom-mining.default-block-info.multiplier"))
    {
      multiplier = (float) config.getDouble("custom-mining.default-block-info.multiplier");
    }
    blockHardness *= multiplier;
    // 드롭 경험치
    float expToDrop = 0;
    // 채광 속도 처리
    float miningSpeed = 0f, speedMultiplier = 1f, finalSpeedMultiplier = 1f, bonusSpeed = 0f;
    // 블록 리젠 속도 (틱)
    int regenCooldown = Math.max(0, Cucumbery.config.getInt("custom-mining.default-ore-regen-in-ticks"));
    // 드롭율 배수
    float miningFortune = 1f;
    // 아이템의 드롭율 태그
    {
      if (ItemStackUtil.itemExists(itemStack))
      {
        NBTItem nbtItem = new NBTItem(itemStack);
        if (nbtItem.hasTag("ToolFortune") && nbtItem.getType("ToolFortune") == NBTType.NBTTagFloat)
        {
          miningFortune += nbtItem.getFloat("ToolFortune") / 100f;
        }
      }
    }
    // 드롭 아이템 처리
    ItemStack clone = itemStack.clone();
    if (toolTier > 0)
    {
      clone.setType(Material.NETHERITE_PICKAXE);
      ItemMeta cloneMeta = clone.getItemMeta();
      cloneMeta.removeEnchant(Enchantment.LOOT_BONUS_BLOCKS);
      clone.setItemMeta(cloneMeta);
    }
    boolean isSilkTouch = clone.getEnchantmentLevel(Enchantment.SILK_TOUCH) > 0;
    List<ItemStack> drops = new ArrayList<>(block.getDrops(clone, player));
    // 채광 모드 커스텀 광물 처리(일부 블록은 반드시 커스텀 광물이 됨)
    {
      BlockData blockData = Variable.customMiningExtraBlocks.get(blockLocation);
      Material blockDataType = blockData != null ? blockData.getMaterial() : null;
      if (blockDataType != null)
      {
        blockType = blockDataType;
      }
      boolean hasDwarvenGoldEffect = CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_PREDEFINED_CUSTOM_ORE_DWARVEN_GOLDS),
              hasGemStoneEffect = CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_PREDEFINED_CUSTOM_ORE_GEMSTONES);
      if (hasDwarvenGoldEffect)
      {
        switch (blockType)
        {
          case GRAY_WOOL, LIGHT_GRAY_WOOL, GRAY_CONCRETE, LIGHT_GRAY_CONCRETE ->
          {
            blockTier = 3;
            blockHardness = 400f;
            drops.clear();
            drops.add(new ItemStack(Material.GOLD_INGOT, 2));
            expToDrop = 7f;
          }
          case GOLD_BLOCK ->
          {
            blockTier = 3;
            blockHardness = 600f;
            drops.clear();
            drops.add(new ItemStack(Material.GOLD_INGOT, hasGemStoneEffect ? Method.random(5, 7) : Method.random(2, 4)));
            expToDrop = 7f;
          }
        }
      }
      // 텅스텐
      if (CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_PREDEFINED_CUSTOM_ORE_TUNGSTENS))
      {
        switch (blockType)
        {
          case GRAY_WOOL, LIGHT_GRAY_WOOL, GRAY_CONCRETE, LIGHT_GRAY_CONCRETE ->
          {
            if (!hasDwarvenGoldEffect)
            {
              blockTier = getVanillaBlockTier(Material.STONE);
              blockHardness = getBlockHardness(Material.STONE) * multiplier;
              drops.clear();
              drops.add(new ItemStack(isSilkTouch ? Material.STONE : Material.COBBLESTONE));
              expToDrop = 0f;
            }
          }
          case WAXED_WEATHERED_CUT_COPPER, WAXED_WEATHERED_COPPER, WAXED_OXIDIZED_COPPER ->
          {
            blockTier = 2;
            blockHardness = 300f;
            drops.clear();
            drops.add(CustomMaterial.TUNGSTEN_ORE.create());
            expToDrop = 0f;
          }
          case END_STONE_BRICKS ->
          {
            blockTier = 2;
            blockHardness = 550f;
            drops.clear();
            drops.add(CustomMaterial.TUNGSTEN_ORE.create(2));
            expToDrop = 0f;
          }
        }
      }
      // 코발트
      if (CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_PREDEFINED_CUSTOM_ORE_COBALTS))
      {
        switch (blockType)
        {
          case GRAY_WOOL, LIGHT_GRAY_WOOL, GRAY_CONCRETE, LIGHT_GRAY_CONCRETE ->
          {
            if (!hasDwarvenGoldEffect)
            {
              blockTier = getVanillaBlockTier(Material.STONE);
              blockHardness = getBlockHardness(Material.STONE) * multiplier;
              drops.clear();
              drops.add(new ItemStack(isSilkTouch ? Material.STONE : Material.COBBLESTONE));
              expToDrop = 0f;
            }
          }
          case LAPIS_BLOCK, BLUE_WOOL, PACKED_ICE ->
          {
            blockTier = 4;
            blockHardness = 3500f;
            drops.clear();
            drops.add(CustomMaterial.COBALT_ORE.create());
            expToDrop = 0f;
          }
          case BLUE_ICE ->
          {
            blockTier = 4;
            blockHardness = 6000f;
            drops.clear();
            drops.add(CustomMaterial.COBALT_ORE.create(2));
            expToDrop = 0f;
          }
        }
      }
      // 쉬루마이트
      if (CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_PREDEFINED_CUSTOM_ORE_SHROOMITES))
      {
        switch (blockType)
        {
          case GRAY_WOOL, LIGHT_GRAY_WOOL, GRAY_CONCRETE, LIGHT_GRAY_CONCRETE ->
          {
            if (!hasDwarvenGoldEffect)
            {
              blockTier = getVanillaBlockTier(Material.STONE);
              blockHardness = getBlockHardness(Material.STONE) * multiplier;
              drops.clear();
              drops.add(new ItemStack(isSilkTouch ? Material.STONE : Material.COBBLESTONE));
              expToDrop = 0f;
            }
          }
          case STRIPPED_MANGROVE_WOOD, MANGROVE_PLANKS, CRIMSON_HYPHAE ->
          {
            blockTier = 4;
            blockHardness = 11000f;
            drops.clear();
            drops.add(CustomMaterial.SHROOMITE_ORE.create());
            expToDrop = 1f;
          }
          case NETHER_WART_BLOCK ->
          {
            blockTier = 4;
            blockHardness = 20500f;
            drops.clear();
            drops.add(CustomMaterial.SHROOMITE_ORE.create(2));
            expToDrop = 1f;
          }
        }
      }
      // 오이스터늄
      boolean isCucumberiteFromTungsten = blockDataType == Material.GREEN_WOOL;
      if (CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_PREDEFINED_CUSTOM_ORE_CUCUMBERITES) || isCucumberiteFromTungsten)
      {
        switch (blockType)
        {
          case GRAY_WOOL, LIGHT_GRAY_WOOL, GRAY_CONCRETE, LIGHT_GRAY_CONCRETE ->
          {
            if (!hasDwarvenGoldEffect)
            {
              blockTier = getVanillaBlockTier(Material.STONE);
              blockHardness = getBlockHardness(Material.STONE) * multiplier;
              drops.clear();
              drops.add(new ItemStack(isSilkTouch ? Material.STONE : Material.COBBLESTONE));
              expToDrop = 0f;
            }
          }
          case GREEN_WOOL, GREEN_TERRACOTTA, GREEN_GLAZED_TERRACOTTA ->
          {
            blockTier = 4;
            blockHardness = 13500f;
            drops.clear();
            drops.add(CustomMaterial.CUCUMBERITE_ORE.create());
            expToDrop = 1f;
          }
          case LIME_WOOL ->
          {
            blockTier = 4;
            blockHardness = 25500f;
            drops.clear();
            drops.add(CustomMaterial.CUCUMBERITE_ORE.create(2));
            expToDrop = 1f;
          }
        }
      }
      // 미스릴
      if (CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_PREDEFINED_CUSTOM_ORE_MITHRILS))
      {
        switch (blockType)
        {
          case GRAY_WOOL, LIGHT_GRAY_WOOL, GRAY_CONCRETE, LIGHT_GRAY_CONCRETE ->
          {
            if (!hasDwarvenGoldEffect)
            {
              blockTier = getVanillaBlockTier(Material.STONE);
              blockHardness = getBlockHardness(Material.STONE) * multiplier;
              drops.clear();
              drops.add(new ItemStack(isSilkTouch ? Material.STONE : Material.COBBLESTONE));
              expToDrop = 0f;
            }
          }
          case PRISMARINE, DARK_PRISMARINE, PRISMARINE_BRICKS ->
          {
            blockTier = 5;
            blockHardness = 5500f;
            drops.clear();
            drops.add(CustomMaterial.MITHRIL_ORE.create());
            expToDrop = 0f;
          }
          case LIGHT_BLUE_WOOL ->
          {
            blockTier = 5;
            blockHardness = 10000f;
            drops.clear();
            drops.add(CustomMaterial.MITHRIL_ORE.create(2));
            expToDrop = 0f;
          }
        }
      }
      // 티타늄
      if (CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_PREDEFINED_CUSTOM_ORE_TITANIUMS))
      {
        switch (blockType)
        {
          case GRAY_WOOL, LIGHT_GRAY_WOOL, GRAY_CONCRETE, LIGHT_GRAY_CONCRETE ->
          {
            if (!hasDwarvenGoldEffect)
            {
              blockTier = getVanillaBlockTier(Material.STONE);
              blockHardness = getBlockHardness(Material.STONE) * multiplier;
              drops.clear();
              drops.add(new ItemStack(isSilkTouch ? Material.STONE : Material.COBBLESTONE));
              expToDrop = 0f;
            }
          }
          case POLISHED_ANDESITE, SMOOTH_STONE, BLACK_WOOL ->
          {
            blockTier = 6;
            blockHardness = 7000f;
            drops.clear();
            drops.add(CustomMaterial.TITANIUM_ORE.create());
            expToDrop = 0f;
          }
          case BLACK_CONCRETE ->
          {
            blockTier = 5;
            blockHardness = 13000f;
            drops.clear();
            drops.add(CustomMaterial.TITANIUM_ORE.create(2));
            expToDrop = 0f;
          }
        }
      }
      // 젬스톤
      if (hasGemStoneEffect)
      {
        switch (blockType)
        {
          case RED_STAINED_GLASS ->
          {
            blockTier = 6;
            blockHardness = 2500f;
            drops.clear();
            drops.add(CustomMaterial.RUBY.create(Method.random(3, 6)));
          }
          case RED_STAINED_GLASS_PANE ->
          {
            blockTier = 6;
            blockHardness = 2300f;
            drops.clear();
            drops.add(CustomMaterial.RUBY.create(Method.random(2, 4)));
          }
          case ORANGE_STAINED_GLASS ->
          {
            blockTier = 7;
            blockHardness = 3200f;
            drops.clear();
            drops.add(CustomMaterial.AMBER.create(Method.random(3, 6)));
          }
          case ORANGE_STAINED_GLASS_PANE ->
          {
            blockTier = 7;
            blockHardness = 3000f;
            drops.clear();
            drops.add(CustomMaterial.AMBER.create(Method.random(2, 4)));
          }
          case PURPLE_STAINED_GLASS ->
          {
            blockTier = 7;
            blockHardness = 3200f;
            drops.clear();
            drops.add(new ItemStack(Material.AMETHYST_SHARD, Method.random(3, 6)));
          }
          case PURPLE_STAINED_GLASS_PANE ->
          {
            blockTier = 7;
            blockHardness = 3000f;
            drops.clear();
            drops.add(new ItemStack(Material.AMETHYST_SHARD, Method.random(2, 4)));
          }
          case LIME_STAINED_GLASS ->
          {
            blockTier = 7;
            blockHardness = 3200f;
            drops.clear();
            drops.add(CustomMaterial.JADE.create(Method.random(3, 6)));
          }
          case LIME_STAINED_GLASS_PANE ->
          {
            blockTier = 7;
            blockHardness = 3000f;
            drops.clear();
            drops.add(CustomMaterial.JADE.create(Method.random(2, 4)));
          }
          case LIGHT_BLUE_STAINED_GLASS ->
          {
            blockTier = 7;
            blockHardness = 3200f;
            drops.clear();
            drops.add(CustomMaterial.SAPPHIRE.create(Method.random(3, 6)));
          }
          case LIGHT_BLUE_STAINED_GLASS_PANE ->
          {
            blockTier = 7;
            blockHardness = 3000f;
            drops.clear();
            drops.add(CustomMaterial.SAPPHIRE.create(Method.random(2, 4)));
          }
          case YELLOW_STAINED_GLASS ->
          {
            blockTier = 8;
            blockHardness = 4000f;
            drops.clear();
            drops.add(CustomMaterial.TOPAZ.create(Method.random(3, 6)));
          }
          case YELLOW_STAINED_GLASS_PANE ->
          {
            blockTier = 8;
            blockHardness = 3800f;
            drops.clear();
            drops.add(CustomMaterial.TOPAZ.create(Method.random(2, 4)));
          }
          case PINK_STAINED_GLASS ->
          {
            blockTier = 9;
            blockHardness = 5000f;
            drops.clear();
            drops.add(CustomMaterial.JASPER.create(Method.random(3, 6)));
          }
          case PINK_STAINED_GLASS_PANE ->
          {
            blockTier = 9;
            blockHardness = 4800f;
            drops.clear();
            drops.add(CustomMaterial.JASPER.create(Method.random(2, 4)));
          }
        }
      }
      if (blockDataType == Material.COBBLESTONE || blockDataType == Material.COBBLED_DEEPSLATE)
      {
        blockTier = getVanillaBlockTier(blockType);
        blockHardness = getBlockHardness(blockDataType) * multiplier;
        drops.clear();
        drops.add(new ItemStack(blockDataType));
        expToDrop = 0f;
      }
      if (hasAllowedBlocks)
      {
        switch (blockType)
        {
          case STONE_SLAB, STONE_STAIRS ->
          {
            blockTier = getVanillaBlockTier(Material.STONE);
            blockHardness = getBlockHardness(Material.STONE) * multiplier;
            drops.clear();
            drops.add(new ItemStack(isSilkTouch ? Material.STONE : Material.COBBLESTONE));
            expToDrop = 0f;
          }
          case COBBLESTONE_SLAB, COBBLESTONE_STAIRS ->
          {
            blockTier = getVanillaBlockTier(Material.COBBLESTONE);
            blockHardness = getBlockHardness(Material.COBBLESTONE) * multiplier;
            drops.clear();
            drops.add(new ItemStack(Material.COBBLESTONE));
            expToDrop = 0f;
          }
        }
      }
    }
    boolean hasExtra = Variable.customMiningExtraBlocks.containsKey(blockLocation);
    // 커스텀 블록 채광 소리
    Sound breakSound = null;
    String breakCustomSound = null;
    float breakSoundVolume = 0f, breakSoundPitch = 0f;
    boolean miningMode3 = CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_2_NO_RESTORE);
    // 커스텀 블록 처리 (extra Block 존재 시 무시)
    if (!hasExtra || CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_2_NO_RESTORE))
    {
      YamlConfiguration blockPlaceData = Variable.blockPlaceData.get(blockLocation.getWorld().getName());
      if (blockPlaceData != null)
      {
        String dataString = blockPlaceData.getString(blockLocation.getBlockX() + "_" + blockLocation.getBlockY() + "_" + blockLocation.getBlockZ()) + "";
        if (dataString.length() - dataString.replace("%", "").length() >= 2)
        {
          dataString = PlaceHolderUtil.placeholder(Bukkit.getConsoleSender(), dataString, null);
        }
        ItemStack dataItem = ItemSerializer.deserialize(dataString);
        if (!dataItem.getType().isAir())
        {
          NBTItem dataNBTItem = new NBTItem(dataItem, true);
          boolean removeKeys = dataNBTItem.hasTag("RemoveKeys") && dataNBTItem.getType("RemoveKeys") == NBTType.NBTTagByte && dataNBTItem.getBoolean("RemovedKeys") != null && Boolean.TRUE.equals(dataNBTItem.getBoolean("RemoveKeys"));
          if (removeKeys)
          {
            dataNBTItem.removeKey("RemoveKeys");
          }
          if (dataNBTItem.hasTag("CustomHardness") && dataNBTItem.getType("CustomHardness") == NBTType.NBTTagFloat)
          {
            blockHardness = dataNBTItem.getFloat("CustomHardness");
            if (removeKeys)
            {
              dataNBTItem.removeKey("CustomHardness");
            }
          }
          if (dataNBTItem.hasTag("BlockTier") && dataNBTItem.getType("BlockTier") == NBTType.NBTTagInt)
          {
            blockTier = dataNBTItem.getInteger("BlockTier");
            if (removeKeys)
            {
              dataNBTItem.removeKey("BlockTier");
            }
          }
          if (dataNBTItem.hasTag("CustomExp") && dataNBTItem.getType("CustomExp") == NBTType.NBTTagFloat)
          {
            expToDrop = dataNBTItem.getFloat("CustomExp");
            if (removeKeys)
            {
              dataNBTItem.removeKey("CustomExp");
            }
          }
          if (dataNBTItem.hasTag("RegenCooldown") && dataNBTItem.getType("RegenCooldown") == NBTType.NBTTagInt)
          {
            regenCooldown = dataNBTItem.getInteger("RegenCooldown");
            if (removeKeys)
            {
              dataNBTItem.removeKey("RegenCooldown");
            }
          }
          if (dataNBTItem.hasTag("IgnoreVanillaModification") && dataNBTItem.getType("IgnoreVanillaModification") == NBTType.NBTTagByte)
          {
            ignoreVanillaModification = ignoreVanillaModification || dataNBTItem.getBoolean("IgnoreVanillaModification");
            if (removeKeys)
            {
              dataNBTItem.removeKey("IgnoreVanillaModification");
            }
          }
          if (dataNBTItem.hasTag("BreakSound") && dataNBTItem.getType("BreakSound") == NBTType.NBTTagString)
          {
            try
            {
              breakSound = Sound.valueOf(dataNBTItem.getString("BreakSound"));
            }
            catch (Exception e)
            {
              breakCustomSound = dataNBTItem.getString("BreakSound");
            }
            if (removeKeys)
            {
              dataNBTItem.removeKey("BreakSound");
            }
          }
          if (dataNBTItem.hasTag("BreakSoundVolume") && dataNBTItem.getType("BreakSoundVolume") == NBTType.NBTTagFloat)
          {
            breakSoundVolume = dataNBTItem.getInteger("BreakSoundVolume");
            if (removeKeys)
            {
              dataNBTItem.removeKey("BreakSoundVolume");
            }
          }
          if (dataNBTItem.hasTag("BreakSoundPitch") && dataNBTItem.getType("BreakSoundPitch") == NBTType.NBTTagFloat)
          {
            breakSoundPitch = dataNBTItem.getInteger("BreakSoundPitch");
            if (removeKeys)
            {
              dataNBTItem.removeKey("BreakSoundPitch");
            }
          }
          NBTList<String> extraTag = NBTAPI.getStringList(NBTAPI.getMainCompound(dataItem), CucumberyTag.EXTRA_TAGS_KEY);
          if (removeKeys && NBTAPI.arrayContainsValue(extraTag, ExtraTag.PRESERVE_BLOCK_NBT))
          {
            dataNBTItem.getCompound(CucumberyTag.KEY_MAIN).getStringList(CucumberyTag.EXTRA_TAGS_KEY).removeIf(s -> s.equals(ExtraTag.PRESERVE_BLOCK_NBT.toString()));
          }
          if (removeKeys && NBTAPI.arrayContainsValue(extraTag, ExtraTag.FORCE_PRESERVE_BLOCK_NBT))
          {
            dataNBTItem.getCompound(CucumberyTag.KEY_MAIN).getStringList(CucumberyTag.EXTRA_TAGS_KEY).removeIf(s -> s.equals(ExtraTag.FORCE_PRESERVE_BLOCK_NBT.toString()));
          }
          try
          {
            if (dataNBTItem.getCompound(CucumberyTag.KEY_MAIN).getStringList(CucumberyTag.EXTRA_TAGS_KEY).isEmpty())
            {
              dataNBTItem.getCompound(CucumberyTag.KEY_MAIN).removeKey(CucumberyTag.EXTRA_TAGS_KEY);
            }
            if (dataNBTItem.getCompound(CucumberyTag.KEY_MAIN).getKeys().isEmpty())
            {
              dataNBTItem.removeKey(CucumberyTag.KEY_MAIN);
            }
          }
          catch (Exception ignored)
          {

          }
          if (ignoreVanillaModification || miningMode3)
          {
            drops.clear();
            drops.add(dataItem);
          }
        }
      }
    }
    // 커스텀 아이템 id
    String toolId = (ItemStackUtil.itemExists(itemStack) ? new NBTItem(itemStack).getString("id") : "") + "",
            blockId = (!drops.isEmpty() && ItemStackUtil.itemExists(drops.get(0)) ? new NBTItem(drops.get(0)).getString("id") : "") + "";
    if (CustomMaterial.FLINT_SHOVEL.toString().equalsIgnoreCase(toolId) && blockId.equals("") && blockType == Material.GRAVEL)
    {
      drops.forEach(item ->
      {
        if (item.getType() == Material.GRAVEL)
        {
          item.setType(Material.FLINT);
        }
      });
    }
    // 드롭 경험치 처리
    List<Double> expList = new ArrayList<>();
    // 제련의 손길 처리
    {
      try
      {
        if (itemStack.getItemMeta().hasEnchant(CustomEnchant.SMELTING_TOUCH))
        {
          drops = new ArrayList<>(ItemStackUtil.getSmeltedResult(player, drops, expList));
        }
      }
      catch (Exception ignored)
      {
      }
    }
    // 드롭 경험치가 0이고 바닐라 채광일 경우 도구에 섬세한 손길이 없을 경우 드롭 경험치 추가
    if (expToDrop == 0 && !ignoreVanillaModification && !isSilkTouch)
    {
      for (double d : expList)
      {
        expToDrop += d;
      }
      expToDrop += miningExp(blockType);
    }

    PlayerInventory playerInventory = player.getInventory();
    CustomMaterial helmetType = CustomMaterial.itemStackOf(playerInventory.getHelmet());
    CustomMaterial chestplateType = CustomMaterial.itemStackOf(playerInventory.getChestplate());
    CustomMaterial leggingsType = CustomMaterial.itemStackOf(playerInventory.getLeggings());
    CustomMaterial bootsType = CustomMaterial.itemStackOf(playerInventory.getBoots());
    // 채광 속도 처리
    {
      // 채광 속도 증가 (%영향을 받음)
      {
        // 효율 인챈트
        {
          int enchantDigSpeedLevel = itemStack.getEnchantmentLevel(Enchantment.DIG_SPEED);
          // 바닐라 채광이 아니거나 효율의 영향을 받을 경우에만 속도 증가
          if (enchantDigSpeedLevel > 0 && (blockTier > 0 || ignoreVanillaModification || block.getDestroySpeed(itemStack, true) > block.getDestroySpeed(itemStack, false)))
          {
            toolSpeed += 30f + (enchantDigSpeedLevel - 1) * 20f;
          }
        }
        if (CustomMaterial.MITHRIL_PICKAXE_REFINED.toString().equalsIgnoreCase(toolId) && CustomMaterial.MITHRIL_ORE.toString().equalsIgnoreCase(blockId))
        {
          miningSpeed += 50f;
        }
        if (CustomMaterial.TITANIUM_PICKAXE_REFINED.toString().equalsIgnoreCase(toolId) && CustomMaterial.TITANIUM_ORE.toString().equalsIgnoreCase(blockId))
        {
          miningSpeed += 60f;
        }
        if (CustomMaterial.STONK.toString().equalsIgnoreCase(toolId) && blockId.equals("") && (blockType == Material.STONE || blockType == Material.COBBLESTONE || blockType == Material.DEEPSLATE || blockType == Material.COBBLED_DEEPSLATE))
        {
          miningSpeed += 10000f;
        }
        if (helmetType == CustomMaterial.MINER_HELMET)
        {
          miningSpeed += 15f;
        }
        if (chestplateType == CustomMaterial.MINER_CHESTPLATE)
        {
          miningSpeed += 20f;
        }
        if (leggingsType == CustomMaterial.MINER_LEGGINGS)
        {
          miningSpeed += 20f;
        }
        if (bootsType == CustomMaterial.MINER_BOOTS)
        {
          miningSpeed += 10f;
        }
        if (helmetType == CustomMaterial.MINDAS_HELMET)
        {
          miningSpeed += 250f;
        }
        if (chestplateType == CustomMaterial.MINDAS_CHESTPLATE)
        {
          miningSpeed += 400f;
        }
        if (leggingsType == CustomMaterial.MINDAS_LEGGINGS)
        {
          miningSpeed += 350f;
        }
        if (bootsType == CustomMaterial.MINDAS_BOOTS)
        {
          miningSpeed += 300f;
        }
      }
      // 채광 속도 증가 (%영향을 안받음)
      {
        if (CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.MOLE_CLAW))
        {
          bonusSpeed += 1f;
        }
      }
      // 채광 속도 %증가 (합적용)
      {
        // 개급함 : 레벨당 10% 증가
        if (CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.HASTE))
        {
          speedMultiplier += 0.1 * (CustomEffectManager.getEffect(player, CustomEffectTypeCustomMining.HASTE).getAmplifier() + 1);
        }
      }
      // 채광 속도 %증가 (곱적용)
      {
        // 느린 채굴 효과 : 레벨당 1% 감소
        if (CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.MINING_FATIGUE))
        {
          int amplifier = CustomEffectManager.getEffect(player, CustomEffectTypeCustomMining.MINING_FATIGUE).getAmplifier() + 1;
          finalSpeedMultiplier *= (1f - amplifier * 0.01f);
        }
        // 땅에 서 있지 않고 공중 비계 효과가 없을 경우 : 80% 감소
        if (!((LivingEntity) player).isOnGround() && !CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.AIR_SCAFFOLDING))
        {
          finalSpeedMultiplier *= 0.2f;
        }
        // 친수성이 없고 물 속에 있을 때 친수성 효과가 없을 경우 : 80% 감소
        ItemStack helmet = player.getInventory().getHelmet();
        if (!(helmet != null && helmet.hasItemMeta() && helmet.getItemMeta().hasEnchant(Enchantment.WATER_WORKER)) && !CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.AQUA_AFFINITY))
        {
          Block b = player.getEyeLocation().getBlock();
          if (b.getState() instanceof Waterlogged waterlogged && waterlogged.isWaterlogged() || b.getType() == Material.WATER)
          {
            finalSpeedMultiplier *= 0.2f;
          }
        }
        // 채광 부스터 효과 : 3배
        if (CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.MINING_BOOSTER))
        {
          finalSpeedMultiplier *= 3f;
        }
        // 채굴 피로 효과 : 레벨당 70% 복리로 감소
        boolean vanilla = false;
        PotionEffect miningFatigue = player.getPotionEffect(PotionEffectType.SLOW_DIGGING);
        if (miningFatigue != null && (miningFatigue.getDuration() > 2 || Cucumbery.using_ProtocolLib && miningFatigue.getAmplifier() > 0))
        {
          int amplifier = miningFatigue.getAmplifier();
          finalSpeedMultiplier *= Math.pow(0.3, amplifier + 1);
          vanilla = true;
        }
        CustomEffect customEffect = CustomEffectManager.getEffectNullable(player, CustomEffectTypeMinecraft.MINING_FATIGUE);
        if (!vanilla && customEffect != null)
        {
          int amplifier = customEffect.getAmplifier();
          finalSpeedMultiplier *= Math.pow(0.3, amplifier + 1);
        }
      }

      // 채광 속도에 도구 속도 추가
      miningSpeed += toolSpeed;
    }

    // 바닐라 채광 속도 처리
    {
      if (blockTier == 0 && block.getDestroySpeed(itemStack, false) == 1f)
      {
        float defaultSpeed = (float) config.getDouble("custom-mining.default-tool-info.default.speed");
        if (miningSpeed > defaultSpeed)
        {
          miningSpeed = defaultSpeed;
        }
      }
    }

    // 채광 속도 처리 2
    {
      // 성급함 포션효과
      {
        PotionEffect potionEffect = player.getPotionEffect(PotionEffectType.FAST_DIGGING);
        int potionHasteLevel = potionEffect != null && (potionEffect.getDuration() > (Cucumbery.using_ProtocolLib ? 2 : 10) || potionEffect.getAmplifier() > 0) ? potionEffect.getAmplifier() + 1 : 0;
        if (potionHasteLevel == 0 && CustomEffectManager.hasEffect(player, CustomEffectTypeMinecraft.HASTE))
        {
          potionHasteLevel = CustomEffectManager.getEffect(player, CustomEffectTypeMinecraft.HASTE).getAmplifier() + 1;
        }
        miningSpeed += potionHasteLevel * 50f;
      }
    }

    // 채광 행운 처리
    {
      int enchantFortuneLevel = itemStack.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
      // 바닐라 채광이 아니거나 효율의 영향을 받을 경우에만 속도 증가
      if (enchantFortuneLevel > 0)
      {
        miningFortune += 0.15 * enchantFortuneLevel;
      }
      CustomEffect miningFortuneEffect = CustomEffectManager.getEffectNullable(player, CustomEffectTypeCustomMining.MINING_FORTUNE);
      if (miningFortuneEffect != null)
      {
        miningFortune += (miningFortuneEffect.getAmplifier() + 1) * 0.05;
      }
      if (CustomMaterial.MITHRIL_PICKAXE.toString().equalsIgnoreCase(toolId) && CustomMaterial.MITHRIL_ORE.toString().equalsIgnoreCase(blockId))
      {
        miningFortune += 0.15;
      }
      if (CustomMaterial.TITANIUM_PICKAXE.toString().equalsIgnoreCase(toolId) && CustomMaterial.TITANIUM_ORE.toString().equalsIgnoreCase(blockId))
      {
        miningFortune += 0.2;
      }
      if (helmetType == CustomMaterial.MINER_HELMET)
      {
        miningFortune += 0.05f;
      }
      if (chestplateType == CustomMaterial.MINER_CHESTPLATE)
      {
        miningFortune += 0.1f;
      }
      if (leggingsType == CustomMaterial.MINER_LEGGINGS)
      {
        miningFortune += 0.05f;
      }
      if (bootsType == CustomMaterial.MINER_BOOTS)
      {
        miningFortune += 0.05f;
      }
      if (helmetType == CustomMaterial.MINDAS_HELMET)
      {
        miningFortune += 0.8f;
      }
      if (chestplateType == CustomMaterial.MINDAS_CHESTPLATE)
      {
        miningFortune += 1.2f;
      }
      if (leggingsType == CustomMaterial.MINDAS_LEGGINGS)
      {
        miningFortune += 1.1f;
      }
      if (bootsType == CustomMaterial.MINDAS_BOOTS)
      {
        miningFortune += 1f;
      }
    }

    // 커스텀 이펙트 처리
    {
      if (CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.MINDAS_TOUCH) && toolTier > 0)
      {
        toolTier += CustomEffectManager.getEffect(player, CustomEffectTypeCustomMining.MINDAS_TOUCH).getAmplifier() + 1;
      }
    }

    // 기타 처리
    {
      if (Math.random() > 0.95 && CustomMaterial.TODWOT_PICKAXE.toString().equalsIgnoreCase(toolId))
      {
        drops.add(new ItemStack(Material.BONE));
      }
    }

    boolean canMine;
    // 채광 도구에 특정 블록만 캘 수 있는지 확인
    {
      Set<Namespaced> getCanDestroyables = itemStack.hasItemMeta() ? itemStack.getItemMeta().getDestroyableKeys() : Collections.emptySet();
      Set<String> minecraftKeys = new HashSet<>();
      getCanDestroyables.forEach(namespaced ->
      {
        if (namespaced instanceof NamespacedKey namespacedKey)
        {
          minecraftKeys.add(namespacedKey.getKey());
        }
        if (namespaced instanceof NamespacedTag namespacedTag)
        {
          Iterable<Tag<Material>> tags = Bukkit.getTags(Tag.REGISTRY_BLOCKS, Material.class);
          tags.forEach(t ->
          {
            NamespacedKey namespacedKey = t.getKey();
            if (namespacedTag.getNamespace().equals(namespacedKey.getNamespace()) && namespacedTag.getKey().equals(namespacedKey.getKey()))
            {
              t.getValues().forEach(material -> minecraftKeys.add(material.toString().toLowerCase()));
            }
          });
        }
      });
      canMine = getCanDestroyables.isEmpty() || minecraftKeys.contains(blockType.toString().toLowerCase());
    }

    // 일부 아이템은 채광 행운 미적용
    {
      CustomMaterial customMaterial = drops.isEmpty() ? null : CustomMaterial.itemStackOf(drops.get(0));
      Material material = drops.isEmpty() ? null : drops.get(0).getType();
      if (customMaterial != null)
      {
        switch (customMaterial)
        {
          case MITHRIL_ORE, MITHRIL_INGOT, TITANIUM_ORE, TITANIUM_INGOT, AMBER, JADE, JASPER, RUBY, TOPAZ, SHROOMITE_ORE, SHROOMITE_INGOT, TUNGSTEN_INGOT, TUNGSTEN_ORE, COBALT_INGOT, COBALT_ORE,
                  CUCUMBERITE_INGOT, CUCUMBERITE_ORE ->
          {
          }
          default ->
          {
            if (!customMaterial.toString().startsWith("ENCHANTED_"))
            {
              miningFortune = 1f;
            }
          }
        }
      }
      else if (material != null)
      {
        switch (material)
        {
          case AMETHYST_SHARD, APPLE, BAKED_POTATO, BEEF, BEETROOT, BIRCH_LOG, BLAZE_ROD, BONE, CARROT,
                  CHICKEN, CHORUS_FRUIT, COAL, COD, COOKED_BEEF,
                  COOKED_CHICKEN, COOKED_COD, COOKED_MUTTON, COOKED_PORKCHOP, COOKED_RABBIT, COOKED_SALMON, COPPER_INGOT, RAW_COPPER,
                  DIAMOND, EMERALD, ENDER_PEARL, FEATHER, FLINT, GLOW_INK_SAC, GOLD_INGOT, RAW_GOLD,
                  GREEN_DYE, GUNPOWDER, INK_SAC, IRON_INGOT, RAW_IRON, KELP, LAPIS_LAZULI, LEATHER, MAGMA_CREAM,
                  MUTTON, NETHERITE_INGOT, NETHER_WART, OCHRE_FROGLIGHT, PHANTOM_MEMBRANE, PORKCHOP, POTATO,
                  PUFFERFISH, QUARTZ, RABBIT, RABBIT_FOOT, RABBIT_HIDE, REDSTONE, ROTTEN_FLESH, SALMON, SCUTE,
                  SLIME_BALL, SNOWBALL, SPIDER_EYE, SUGAR, TROPICAL_FISH, VERDANT_FROGLIGHT, WHEAT, WHEAT_SEEDS ->
          {
            if (blockType != Material.CACTUS && block.getBlockData() instanceof Ageable ageable && ageable.getAge() != ageable.getMaximumAge())
            {
              miningFortune = 1f;
            }
            if (blockType == Material.REDSTONE_WIRE)
            {
              miningFortune = 1f;
            }
          }
          default -> miningFortune = 1f;
        }
      }
    }
//
//    // 마법이 부여된 아이템 형태(자원)이 없으면 채광 행운 미적용
//    {
//      if (customMaterial != null && Method2.valueOf("ENCHANTED_" + customMaterial, CustomMaterial.class) == null)
//      {
//        miningFortune = 1f;
//      }
//      if (customMaterial == null && material != null && Method2.valueOf("ENCHANTED_" + material, CustomMaterial.class) == null)
//      {
//        miningFortune = 1f;
//      }
//    }

    // 블록이 즉시 파괴되는 블록이면 최솟값으로 보정
    if (blockHardness == 0f)
    {
      blockHardness = Float.MIN_VALUE;
    }
    if (blockHardness < 0f)
    {
      blockHardness = -1f;
    }
    miningSpeed = miningSpeed * speedMultiplier * finalSpeedMultiplier + bonusSpeed;
    List<ItemStack> drop = new ArrayList<>();
    int intSide = (int) miningFortune;
    float floatSide = miningFortune - intSide;
    if (Math.random() < floatSide)
    {
      intSide++;
    }
    if (intSide > 0)
    {
      drop.addAll(drops);
      if (intSide > 1)
      {
        int finalIntSide = intSide;
        drop.forEach(i -> i.setAmount(i.getAmount() * finalIntSide));
      }
    }
    // 도구 속도가 0이면 블록을 채굴할 수 없음
    if (toolSpeedZero)
    {
      toolSpeed = 0f;
      miningSpeed = 0f;
    }
    if (UserData.SHOW_PLUGIN_DEV_DEBUG_MESSAGE.getBoolean(player))
    {
      MessageUtil.sendActionBar(player, "IgnoreVanilla: %s, ToolSpeed: %s, MiningSpeed: %s, VanillaSpeed: %s, Hardness: %s, Fortune: %s, Progress: %s", ignoreVanillaModification + "", Constant.Sosu2.format(toolSpeed),
              Constant.Sosu2.format(miningSpeed), Constant.Sosu2.format(block.getDestroySpeed(itemStack, true)), Constant.Sosu2.format(blockHardness),
              Constant.Sosu2.format(miningFortune * 100), Constant.Sosu2Force.format(Variable.customMiningProgress.getOrDefault(player.getUniqueId(), 0f) * 100d) + "%");
    }
    return new MiningResult(canMine, toolSpeed, miningSpeed, blockHardness, miningFortune, expToDrop, toolTier, blockTier, regenCooldown, drop, breakSound, breakCustomSound, breakSoundVolume, breakSoundPitch);
  }

  public static float miningExp(@NotNull Material blockType)
  {
    float exp = 0f;
    if (Tag.COAL_ORES.isTagged(blockType))
    {
      exp += Math.random() * 2;
    }
    if (blockType == Material.NETHER_GOLD_ORE)
    {
      exp += Math.random();
    }
    if (Tag.DIAMOND_ORES.isTagged(blockType) || Tag.EMERALD_ORES.isTagged(blockType))
    {
      exp += Math.random() * 4 + 3;
    }
    if (Tag.LAPIS_ORES.isTagged(blockType) || blockType == Material.NETHER_QUARTZ_ORE)
    {
      exp += Math.random() * 3 + 2;
    }
    if (Tag.REDSTONE_ORES.isTagged(blockType))
    {
      exp += Math.random() * 4 + 1;
    }
    if (blockType == Material.SPAWNER)
    {
      exp += Math.random() * 28 + 15;
    }
    if (blockType == Material.SCULK)
    {
      exp += 1f;
    }
    if (blockType == Material.SCULK_SENSOR || blockType == Material.SCULK_SHRIEKER || blockType == Material.SCULK_CATALYST)
    {
      exp += 5f;
    }
    return exp;
  }

  public static int getToolTier(@NotNull ItemStack itemStack)
  {
    Material type = itemStack.getType();
    int toolTier = switch (type)
            {
              case WOODEN_PICKAXE, GOLDEN_PICKAXE -> 1;
              case STONE_PICKAXE -> 2;
              case IRON_PICKAXE -> 3;
              case DIAMOND_PICKAXE -> 4;
              case NETHERITE_PICKAXE -> 5;
              default -> 0;
            };
    YamlConfiguration config = Cucumbery.config;
    if (config.contains("custom-mining.default-tool-info." + type + ".tier"))
    {
      toolTier = config.getInt("custom-mining.default-tool-info." + type + ".tier");
    }
    else if (toolTier == 0 && config.contains("custom-mining.default-tool-info.default.tier"))
    {
      toolTier = config.getInt("custom-mining.default-tool-info.default.tier");
    }
    try
    {
      NBTItem nbtItem = new NBTItem(itemStack);
      if (nbtItem.hasTag("ToolTier") && nbtItem.getType("ToolTier") == NBTType.NBTTagInt)
      {
        toolTier = nbtItem.getInteger("ToolTier");
      }
    }
    catch (Exception ignored)
    {
    }
    return toolTier;
  }

  public static float getToolSpeed(@NotNull ItemStack itemStack)
  {
    Material type = itemStack.getType();
    float toolSpeed = switch (type)
            {
              case GOLDEN_SWORD, SHEARS -> 750f;
              case WOODEN_PICKAXE, WOODEN_AXE, WOODEN_SHOVEL, WOODEN_HOE -> 100f;
              case STONE_PICKAXE, STONE_AXE, STONE_SHOVEL, STONE_HOE -> 200f;
              case IRON_PICKAXE, IRON_AXE, IRON_SHOVEL, IRON_HOE -> 300f;
              case DIAMOND_PICKAXE, DIAMOND_AXE, DIAMOND_SHOVEL, DIAMOND_HOE -> 400f;
              case NETHERITE_PICKAXE, NETHERITE_AXE, NETHERITE_SHOVEL, NETHERITE_HOE -> 450f;
              case WOODEN_SWORD -> 500f;
              case STONE_SWORD -> 550f;
              case IRON_SWORD, GOLDEN_PICKAXE, GOLDEN_AXE, GOLDEN_SHOVEL, GOLDEN_HOE -> 600f;
              case DIAMOND_SWORD -> 650f;
              case NETHERITE_SWORD -> 700f;
              default -> 50f;
            };
    YamlConfiguration config = Cucumbery.config;
    if (config.contains("custom-mining.default-tool-info." + type + ".speed"))
    {
      toolSpeed = (float) config.getDouble("custom-mining.default-tool-info." + type + ".speed");
    }
    else if (toolSpeed == 50f && config.contains("custom-mining.default-tool-info.default.speed"))
    {
      toolSpeed = (float) config.getDouble("custom-mining.default-tool-info.default.speed");
    }
    try
    {
      NBTItem nbtItem = new NBTItem(itemStack);
      if (nbtItem.hasTag("ToolSpeed") && nbtItem.getType("ToolSpeed") == NBTType.NBTTagFloat)
      {
        toolSpeed = nbtItem.getFloat("ToolSpeed");
      }
    }
    catch (Exception ignored)
    {
    }
    return toolSpeed;
  }

  public static int getVanillaBlockTier(@NotNull Material type)
  {
    YamlConfiguration config = Cucumbery.config;
    if (config.contains("custom-mining.default-block-info." + type + ".tier"))
    {
      return config.getInt("custom-mining.default-block-info." + type + ".tier");
    }
    if (Tag.NEEDS_DIAMOND_TOOL.isTagged(type))
    {
      return 4;
    }
    if (Tag.NEEDS_IRON_TOOL.isTagged(type))
    {
      return 3;
    }
    if (Tag.NEEDS_STONE_TOOL.isTagged(type))
    {
      return 2;
    }
    if (Tag.MINEABLE_PICKAXE.isTagged(type))
    {
      return 1;
    }
    return 0;
  }

  public static float getBlockHardness(@NotNull Material type)
  {
    YamlConfiguration config = Cucumbery.config;
    if (config.contains("custom-mining.default-block-info." + type + ".hardness"))
    {
      return (float) config.getDouble("custom-mining.default-block-info." + type + ".hardness");
    }
    return type.getHardness();
  }

  public static void quitCustomMining(@NotNull Player player)
  {
    UUID uuid = player.getUniqueId();
    if (CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
    {
      Variable.customMiningProgress.put(uuid, 0f);
      if (!MiningScheduler.blockBreakKey.containsKey(uuid))
      {
        int random;
        do
        {
          random = (int) (Math.random() * Integer.MAX_VALUE);
        } while (MiningScheduler.blockBreakKey.containsValue(random));
        MiningScheduler.blockBreakKey.put(uuid, random);
      }
      Location location = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
      player.getWorld().getPlayers().forEach(p -> p.sendBlockDamage(location, 0f, MiningScheduler.blockBreakKey.get(player.getUniqueId())));
      CustomEffectManager.removeEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_PROGRESS);
    }
  }
}

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
import com.jho5245.cucumbery.util.no_groups.ItemSerializer;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.PlaceHolderUtil;
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
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
      if (!UserData.EVENT_EXCEPTION_ACCESS.getBoolean(player) && !query.testState(BukkitAdapter.adapt(blockLocation), localPlayer, Flags.BUILD))
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
    boolean ignoreVanillaModification = false;
    {
      try
      {
        NBTItem nbtItem = new NBTItem(itemStack);
        if (nbtItem.hasKey("IgnoreVanillaModification") && nbtItem.getType("IgnoreVanillaModification") == NBTType.NBTTagByte)
        {
          ignoreVanillaModification = nbtItem.getBoolean("IgnoreVanillaModification");
        }
      }
      catch (Exception ignored)
      {
      }
    }
    int blockTier = getVanillaBlockTier(blockType), toolTier = getToolTier(itemStack);
    float blockHardness = blockType.getHardness(), toolSpeed = getToolSpeed(itemStack);
    boolean toolSpeedZero = toolSpeed <= 0f;
    float multiplier = 100f;
    YamlConfiguration config = Cucumbery.config;
    if (config.contains("custom-mining.default-block-info." + blockType + ".hardness"))
    {
      blockHardness = (float) config.getDouble("custom-mining.default-block-info." + blockType + ".hardness");
    }
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
    // 드롭 아이템 처리
    ItemStack clone = itemStack.clone();
    if (toolTier > 0)
    {
      clone.setType(Material.NETHERITE_PICKAXE);
      ItemMeta cloneMeta = clone.getItemMeta();
      cloneMeta.removeEnchant(Enchantment.LOOT_BONUS_BLOCKS);
      clone.setItemMeta(cloneMeta);
    }
    List<ItemStack> drops = new ArrayList<>(block.getDrops(clone, player));
    // 채광 모드 커스텀 광물 처리(일부 블록은 반드시 커스텀 광물이 됨)
    {
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
          case GOLD_BLOCK -> {
            blockTier = 3;
            blockHardness = 600f;
            drops.clear();
            drops.add(new ItemStack(Material.GOLD_INGOT, hasGemStoneEffect ? Method.random(5, 7) : Method.random(2, 4)));
            expToDrop = 7f;
          }
        }
      }
      if (CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_PREDEFINED_CUSTOM_ORE_MITHRILS))
      {
        switch (blockType)
        {
          case GRAY_WOOL, LIGHT_GRAY_WOOL, GRAY_CONCRETE, LIGHT_GRAY_CONCRETE -> {
            if (!hasDwarvenGoldEffect)
            {
              blockTier = 4;
              blockHardness = 500f;
              drops.clear();
              drops.add(CustomMaterial.MITHRIL_ORE.create());
              expToDrop = 15f;
            }
          }
          case PRISMARINE, DARK_PRISMARINE, PRISMARINE_BRICKS -> {
            blockTier = 4;
            blockHardness = 800f;
            drops.clear();
            drops.add(CustomMaterial.MITHRIL_ORE.create(2));
            expToDrop = 15f;
          }
          case LIGHT_BLUE_WOOL -> {
            blockTier = 4;
            blockHardness = 1500f;
            drops.clear();
            drops.add(CustomMaterial.MITHRIL_ORE.create(5));
            expToDrop = 15f;
          }
        }
      }
      BlockData blockData = Variable.customMiningExtraBlocks.get(blockLocation);
      Material blockDataType = blockData != null ? blockData.getMaterial() : null;
      boolean isTitaniumFromMithril = blockDataType == Material.POLISHED_DIORITE;
      if (CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_PREDEFINED_CUSTOM_ORE_TITANIUMS) || isTitaniumFromMithril)
      {
        if (blockType == Material.POLISHED_DIORITE || isTitaniumFromMithril)
        {
          blockTier = 5;
          blockHardness = 2000f;
          drops.clear();
          drops.add(CustomMaterial.TITANIUM_ORE.create());
          expToDrop = 30f;
        }
      }
      if (hasGemStoneEffect)
      {
        switch (blockType)
        {
          case RED_STAINED_GLASS -> {
            blockTier = 6;
            blockHardness = 2500f;
            drops.clear();
            drops.add(CustomMaterial.RUBY.create(Method.random(3, 6)));
          }
          case RED_STAINED_GLASS_PANE -> {
            blockTier = 6;
            blockHardness = 2300f;
            drops.clear();
            drops.add(CustomMaterial.RUBY.create(Method.random(2, 4)));
          }
          case ORANGE_STAINED_GLASS -> {
            blockTier = 7;
            blockHardness = 3200f;
            drops.clear();
            drops.add(CustomMaterial.AMBER.create(Method.random(3, 6)));
          }
          case ORANGE_STAINED_GLASS_PANE -> {
            blockTier = 7;
            blockHardness = 3000f;
            drops.clear();
            drops.add(CustomMaterial.AMBER.create(Method.random(2, 4)));
          }
          case PURPLE_STAINED_GLASS -> {
            blockTier = 7;
            blockHardness = 3200f;
            drops.clear();
            drops.add(CustomMaterial.AMETHYST.create(Method.random(3, 6)));
          }
          case PURPLE_STAINED_GLASS_PANE -> {
            blockTier = 7;
            blockHardness = 3000f;
            drops.clear();
            drops.add(CustomMaterial.AMETHYST.create(Method.random(2, 4)));
          }
          case LIME_STAINED_GLASS -> {
            blockTier = 7;
            blockHardness = 3200f;
            drops.clear();
            drops.add(CustomMaterial.JADE.create(Method.random(3, 6)));
          }
          case LIME_STAINED_GLASS_PANE -> {
            blockTier = 7;
            blockHardness = 3000f;
            drops.clear();
            drops.add(CustomMaterial.JADE.create(Method.random(2, 4)));
          }
          case LIGHT_BLUE_STAINED_GLASS -> {
            blockTier = 7;
            blockHardness = 3200f;
            drops.clear();
            drops.add(CustomMaterial.SAPPHIRE.create(Method.random(3, 6)));
          }
          case LIGHT_BLUE_STAINED_GLASS_PANE -> {
            blockTier = 7;
            blockHardness = 3000f;
            drops.clear();
            drops.add(CustomMaterial.SAPPHIRE.create(Method.random(2, 4)));
          }
          case YELLOW_STAINED_GLASS -> {
            blockTier = 8;
            blockHardness = 4000f;
            drops.clear();
            drops.add(CustomMaterial.TOPAZ.create(Method.random(3, 6)));
          }
          case YELLOW_STAINED_GLASS_PANE -> {
            blockTier = 8;
            blockHardness = 3800f;
            drops.clear();
            drops.add(CustomMaterial.TOPAZ.create(Method.random(2, 4)));
          }
          case PINK_STAINED_GLASS -> {
            blockTier = 9;
            blockHardness = 5000f;
            drops.clear();
            drops.add(CustomMaterial.JASPER.create(Method.random(3, 6)));
          }
          case PINK_STAINED_GLASS_PANE -> {
            blockTier = 9;
            blockHardness = 4800f;
            drops.clear();
            drops.add(CustomMaterial.JASPER.create(Method.random(2, 4)));
          }
        }
      }
      if (blockDataType == Material.COBBLESTONE)
      {
        blockTier = getVanillaBlockTier(blockType);
        blockHardness = blockDataType.getHardness() * multiplier;
        drops.clear();
        drops.add(new ItemStack(blockDataType));
      }
    }
    // 커스텀 블록 처리
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
          boolean removeKeys = dataNBTItem.hasKey("RemoveKeys") && dataNBTItem.getType("RemoveKeys") == NBTType.NBTTagByte && dataNBTItem.getBoolean("RemovedKeys") != null && Boolean.TRUE.equals(dataNBTItem.getBoolean("RemoveKeys"));
          if (removeKeys)
          {
            dataNBTItem.removeKey("RemoveKeys");
          }
          if (dataNBTItem.hasKey("CustomHardness") && dataNBTItem.getType("CustomHardness") == NBTType.NBTTagFloat)
          {
            blockHardness = dataNBTItem.getFloat("CustomHardness");
            if (removeKeys)
            {
              dataNBTItem.removeKey("CustomHardness");
            }
          }
          if (dataNBTItem.hasKey("BlockTier") && dataNBTItem.getType("BlockTier") == NBTType.NBTTagInt)
          {
            blockTier = dataNBTItem.getInteger("BlockTier");
            if (removeKeys)
            {
              dataNBTItem.removeKey("BlockTier");
            }
          }
          if (dataNBTItem.hasKey("CustomExp") && dataNBTItem.getType("CustomExp") == NBTType.NBTTagFloat)
          {
            expToDrop = dataNBTItem.getFloat("CustomExp");
            if (removeKeys)
            {
              dataNBTItem.removeKey("CustomExp");
            }
          }
          if (dataNBTItem.hasKey("RegenCooldown") && dataNBTItem.getType("RegenCooldown") == NBTType.NBTTagInt)
          {
            regenCooldown = dataNBTItem.getInteger("RegenCooldown");
            if (removeKeys)
            {
              dataNBTItem.removeKey("RegenCooldown");
            }
          }
          if (dataNBTItem.hasKey("IgnoreVanillaModification") && dataNBTItem.getType("IgnoreVanillaModification") == NBTType.NBTTagByte)
          {
            ignoreVanillaModification = ignoreVanillaModification || dataNBTItem.getBoolean("IgnoreVanillaModification");
            if (removeKeys)
            {
              dataNBTItem.removeKey("IgnoreVanillaModification");
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
          if (ignoreVanillaModification)
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
    // 드롭 경험치가 0이고 바닐라 채광일 경우 드롭 경험치 추가
    if (expToDrop == 0 && !ignoreVanillaModification)
    {
      for (double d : expList)
      {
        expToDrop += d;
      }
      expToDrop += miningExp(blockType);
    }

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
        if (CustomMaterial.REFINED_MITHRIL_PICKAXE.toString().equalsIgnoreCase(toolId) && CustomMaterial.MITHRIL_ORE.toString().equalsIgnoreCase(blockId))
        {
          miningSpeed += 50f;
        }
        if (CustomMaterial.REFINED_TITANIUM_PICKAXE.toString().equalsIgnoreCase(toolId) && CustomMaterial.TITANIUM_ORE.toString().equalsIgnoreCase(blockId))
        {
          miningSpeed += 60f;
        }
        if (CustomMaterial.STONK.toString().equalsIgnoreCase(toolId) && blockId.equals("") && (blockType == Material.STONE || blockType == Material.COBBLESTONE || blockType == Material.DEEPSLATE || blockType == Material.COBBLED_DEEPSLATE))
        {
          miningSpeed += 10000f;
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
        // 땅에 서 있지 않을 경우 : 80% 감소
        if (!((LivingEntity) player).isOnGround())
        {
          finalSpeedMultiplier *= 0.2f;
        }
        // 친수성이 없고 물 속에 있을 경우 : 80% 감소
        ItemStack helmet = player.getInventory().getHelmet();
        if (!(helmet != null && helmet.hasItemMeta() && helmet.getItemMeta().hasEnchant(Enchantment.WATER_WORKER)))
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
        int potionHasteLevel = potionEffect != null && potionEffect.getDuration() > 10 ? potionEffect.getAmplifier() + 1 : 0;
        if (potionHasteLevel == 0 && CustomEffectManager.hasEffect(player, CustomEffectTypeMinecraft.MINECRAFT_HASTE))
        {
          potionHasteLevel = CustomEffectManager.getEffect(player, CustomEffectTypeMinecraft.MINECRAFT_HASTE).getAmplifier() + 1;
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

    // 섬세한 손길 적용 시 채광 행운 미적용
    if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasEnchant(Enchantment.SILK_TOUCH))
    {
      miningFortune = 1f;
    }

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
      MessageUtil.sendActionBar(player, "IgnoreVanilla: %s, ToolSpeed: %s, MiningSpeed: %s, VanillaSpeed: %s, Hardness: %s, Fortune: %s", ignoreVanillaModification + "", Constant.Sosu2.format(toolSpeed),
              Constant.Sosu2.format(miningSpeed), Constant.Sosu2.format(block.getDestroySpeed(itemStack, true)), Constant.Sosu2.format(blockHardness), Constant.Sosu2.format(miningFortune * 100));
    }
    return new MiningResult(canMine, toolSpeed, miningSpeed, blockHardness, miningFortune, expToDrop, toolTier, blockTier, regenCooldown, drop);
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
      if (nbtItem.hasKey("ToolTier") && nbtItem.getType("ToolTier") == NBTType.NBTTagInt)
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
              case STONE_SWORD, DIAMOND_SWORD, GOLDEN_SWORD, IRON_SWORD, NETHERITE_SWORD, WOODEN_SWORD, SHEARS -> 750f;
              case WOODEN_PICKAXE -> 100f;
              case WOODEN_AXE, WOODEN_HOE, WOODEN_SHOVEL -> 180f;
              case STONE_PICKAXE -> 200f;
              case STONE_AXE, STONE_HOE, STONE_SHOVEL -> 260f;
              case IRON_PICKAXE -> 300f;
              case IRON_AXE, IRON_HOE, IRON_SHOVEL -> 360f;
              case DIAMOND_PICKAXE -> 400f;
              case DIAMOND_AXE, DIAMOND_HOE, DIAMOND_SHOVEL -> 480f;
              case NETHERITE_PICKAXE -> 450f;
              case NETHERITE_AXE, NETHERITE_HOE, NETHERITE_SHOVEL -> 620f;
              case GOLDEN_PICKAXE -> 600f;
              case GOLDEN_AXE, GOLDEN_HOE, GOLDEN_SHOVEL -> 800f;
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
      if (nbtItem.hasKey("ToolSpeed") && nbtItem.getType("ToolSpeed") == NBTType.NBTTagFloat)
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
    int blockTier = 0;
    if (Tag.MINEABLE_PICKAXE.isTagged(type))
    {
      blockTier = 1;
    }
    if (Tag.NEEDS_STONE_TOOL.isTagged(type))
    {
      blockTier = 2;
    }
    if (Tag.NEEDS_IRON_TOOL.isTagged(type))
    {
      blockTier = 3;
    }
    if (Tag.NEEDS_DIAMOND_TOOL.isTagged(type))
    {
      blockTier = 4;
    }
    YamlConfiguration config = Cucumbery.config;
    if (config.contains("custom-mining.default-block-info." + type + ".tier"))
    {
      blockTier = config.getInt("custom-mining.default-block-info." + type + ".tier");
    }
    return blockTier;
  }
}

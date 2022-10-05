package com.jho5245.cucumbery.util.itemlore;

import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeCustomMining;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemLore2CustomMaterial
{
  protected static void setItemLore(@Nullable Player viewer, @NotNull CustomMaterial customMaterial, @NotNull ItemStack itemStack, @NotNull ItemMeta itemMeta, @NotNull List<Component> lore)
  {
    NBTItem nbtItem = new NBTItem(itemStack);
    switch (customMaterial)
    {
      case MINER_HELMET ->
      {
        if (viewer != null && CustomEffectManager.hasEffect(viewer, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&6채광 속도 : %s", Constant.THE_COLOR_HEX + "+15"));
          lore.add(ComponentUtil.translate("&6채광 행운 : %s", Constant.THE_COLOR_HEX + "+5"));
        }
      }
      case MINER_CHESTPLATE -> {
        if (viewer != null && CustomEffectManager.hasEffect(viewer, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&6채광 속도 : %s", Constant.THE_COLOR_HEX + "+20"));
          lore.add(ComponentUtil.translate("&6채광 행운 : %s", Constant.THE_COLOR_HEX + "+10"));
        }
      }
      case MINER_LEGGINGS -> {
        if (viewer != null && CustomEffectManager.hasEffect(viewer, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&6채광 속도 : %s", Constant.THE_COLOR_HEX + "+20"));
          lore.add(ComponentUtil.translate("&6채광 행운 : %s", Constant.THE_COLOR_HEX + "+5"));
        }
      }
      case MINER_BOOTS -> {
        if (viewer != null && CustomEffectManager.hasEffect(viewer, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&6채광 속도 : %s", Constant.THE_COLOR_HEX + "+10"));
          lore.add(ComponentUtil.translate("&6채광 행운 : %s", Constant.THE_COLOR_HEX + "+5"));
        }
      }
      case MINDAS_HELMET ->
      {
        if (viewer != null && CustomEffectManager.hasEffect(viewer, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&6채광 속도 : %s", Constant.THE_COLOR_HEX + "+250"));
          lore.add(ComponentUtil.translate("&6채광 행운 : %s", Constant.THE_COLOR_HEX + "+80"));
        }
      }
      case MINDAS_CHESTPLATE -> {
        if (viewer != null && CustomEffectManager.hasEffect(viewer, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&6채광 속도 : %s", Constant.THE_COLOR_HEX + "+400"));
          lore.add(ComponentUtil.translate("&6채광 행운 : %s", Constant.THE_COLOR_HEX + "+120"));
        }
      }
      case MINDAS_LEGGINGS -> {
        if (viewer != null && CustomEffectManager.hasEffect(viewer, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&6채광 속도 : %s", Constant.THE_COLOR_HEX + "+350"));
          lore.add(ComponentUtil.translate("&6채광 행운 : %s", Constant.THE_COLOR_HEX + "+110"));
        }
      }
      case MINDAS_BOOTS -> {
        if (viewer != null && CustomEffectManager.hasEffect(viewer, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&6채광 속도 : %s", Constant.THE_COLOR_HEX + "+300"));
          lore.add(ComponentUtil.translate("&6채광 행운 : %s", Constant.THE_COLOR_HEX + "+100"));
        }
      }
    }
    switch (customMaterial)
    {
      case THE_MUSIC ->
      {
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&6능력 : %s %s", ComponentUtil.translate("&a'그 노래' 재생"), ComponentUtil.translate("&e;&l우클릭")));
        lore.add(ComponentUtil.translate("&7재사용 대기시간 : %s", ComponentUtil.translate("&a%s초", 10)));
      }
      case MITHRIL_PICKAXE ->
      {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7%s 채광 시 채광 행운 15 증가", CustomMaterial.MITHRIL_ORE.getDisplayName()));
      }
      case MITHRIL_PICKAXE_REFINED ->
      {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7%s 채광 시 채광 속도 50 증가", CustomMaterial.MITHRIL_ORE.getDisplayName()));
      }
      case TITANIUM_PICKAXE ->
      {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7%s 채광 시 채광 행운 20 증가", CustomMaterial.TITANIUM_ORE.getDisplayName()));
      }
      case TITANIUM_PICKAXE_REFINED ->
      {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7%s 채광 시 채광 속도 60 증가", CustomMaterial.TITANIUM_ORE.getDisplayName()));
      }
      case STONK ->
      {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7%s, %s, %s 또는 %s 채광 시 채광 속도 10000 증가", Material.STONE, Material.DEEPSLATE, Material.COBBLESTONE, Material.COBBLED_DEEPSLATE));
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&6능력 : %s %s", ComponentUtil.translate("&a채광 부스터"), ComponentUtil.translate("&e;&l우클릭")));
        lore.add(ComponentUtil.translate("&f10초간 채광 속도가 3배로 증가"));
        lore.add(ComponentUtil.translate("&7재사용 대기시간 : %s", ComponentUtil.translate("&a%s초", 120)));
      }
      case PORTABLE_CRAFTING_TABLE ->
      {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&6능력 : %s %s", ComponentUtil.translate("&a작업대 사용"), ComponentUtil.translate("&e;&l우클릭")));
      }
      case TODWOT_PICKAXE ->
      {
        if (viewer != null && CustomEffectManager.hasEffect(viewer, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&7%s 확률로 %s을(를) 추가로 드롭한다", "rg255,204;5%", Material.BONE));
        }
      }
      case SANS_BOOTS, SANS_CHESTPLATE, SANS_LEGGINGS, SANS_HELMET ->
      {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&6세트 능력 : %s", ComponentUtil.translate("&a와 샌즈!")));
        lore.add(ComponentUtil.translate("&7스켈레톤에게 어그로가 끌리지 않음"));
      }
      case FROG_HELMET, FROG_CHESTPLATE, FROG_LEGGINGS, FROG_BOOTS ->
      {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&6세트 능력 : %s", ComponentUtil.translate("&a개구리 점프")));
        lore.add(ComponentUtil.translate("&7영구적으로 %s 4단계 효과 적용", PotionEffectType.JUMP));
      }
      case MINER_HELMET, MINER_CHESTPLATE, MINER_LEGGINGS, MINER_BOOTS ->
      {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&6세트 능력 : %s", ComponentUtil.translate("&a노동력 충만")));
        lore.add(ComponentUtil.translate("&7영구적으로 %s 1단계 효과 적용", PotionEffectType.FAST_DIGGING));
      }
      case FLINT_SHOVEL ->
      {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7%s 채굴 시 %s 확률로 %s을(를) 드롭함", Material.GRAVEL, "&a100%", Material.FLINT));
      }
      case DRILL_FUEL ->
      {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7모루에서 드릴과 함께 사용하여"));
        lore.add(ComponentUtil.translate("&7드릴의 연료를 1000 충전할 수 있다"));
      }
      case SPYGLASS_TELEPORT ->
      {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&6능력 : %s %s", ComponentUtil.translate("&a보아라, 닿아라"), ComponentUtil.translate("&e&l우클릭")));
        lore.add(ComponentUtil.translate("&f망원경을 3초간 우클릭하고 있으면"));
        lore.add(ComponentUtil.translate("&f바라보고 있는 블록으로 순간 이동한다"));
        lore.add(ComponentUtil.translate("&f최대 100블록 거리까지만 이동 가능"));
        lore.add(ComponentUtil.translate("&e단, 너무 가까운 곳으로는 사용이 불가능하며"));
        lore.add(ComponentUtil.translate("&e우클릭 도중 움직이거나 우클릭을 멈추면 순간 이동이 취소됨"));
        lore.add(ComponentUtil.translate("&7재사용 대기시간 : %s", ComponentUtil.translate("&a%s초", 60)));
      }
      case SMALL_MINING_SACK ->
      {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7광물 종류를 담을 수 있는 가방입니다!"));
        NBTCompound sack = nbtItem.addCompound("Sack");
        int capacity = sack.getInteger("Capacity");
        int current = 0;
        NBTCompound items = sack.addCompound("Items");
        for (String key : items.getKeys())
        {
          current += items.getInteger(key);
        }
        lore.add(ComponentUtil.translate("&a용량 : %s / %s", current, capacity));
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&a[담을 수 있는 아이템들]"));
        lore.add(ComponentUtil.translate("&a%s, %s, %s, %s, %s, %s",
                Material.STONE, Material.COBBLESTONE, CustomMaterial.MITHRIL_ORE, CustomMaterial.MITHRIL_INGOT, CustomMaterial.TITANIUM_ORE, CustomMaterial.TITANIUM_INGOT));
      }
    }
    switch (customMaterial)
    {
      case FROG_BOOTS ->
      {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&6능력 : %s %s", ComponentUtil.translate("&a개구리 착지")));
        lore.add(ComponentUtil.translate("&7낙하 대미지 20% 감소"));
      }
      case MINER_HELMET, MINDAS_HELMET ->
      {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&6능력 : %s %s", ComponentUtil.translate("&a광부의 시야")));
        lore.add(ComponentUtil.translate("&7영구적으로 %s 효과 적용", PotionEffectType.NIGHT_VISION));
      }
    }
  }
}

package com.jho5245.cucumbery.util.itemlore;

import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectType;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemLore2CustomMaterial
{
  protected static void setItemLore(@Nullable Player viewer, @NotNull CustomMaterial customMaterial, @NotNull ItemMeta itemMeta, @NotNull List<Component> lore)
  {
    switch (customMaterial)
    {
      case THE_MUSIC -> {
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&6능력 : %s %s", ComponentUtil.translate("&a'그 노래' 재생"), ComponentUtil.translate("&e&l우클릭")));
        lore.add(ComponentUtil.translate("&7재사용 대기시간 : %s", ComponentUtil.translate("&a%s초", 10)));
      }
      case MITHRIL_PICKAXE -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7%s 채광 시 채광 행운 15 증가", CustomMaterial.MITHRIL.getDisplayName()));
      }
      case REFINED_MITHRIL_PICKAXE -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7%s 채광 시 채광 속도 50 증가", CustomMaterial.MITHRIL.getDisplayName()));
      }
      case TITANIUM_PICKAXE -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7%s 채광 시 채광 행운 20 증가", CustomMaterial.TITANIUM_ORE.getDisplayName()));
      }
      case REFINED_TITANIUM_PICKAXE -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7%s 채광 시 채광 속도 60 증가", CustomMaterial.TITANIUM_ORE.getDisplayName()));
      }
      case STONK -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7%s, %s, %s 또는 %s 채광 시 채광 속도 10000 증가", Material.STONE, Material.DEEPSLATE, Material.COBBLESTONE, Material.COBBLED_DEEPSLATE));
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&6능력 : %s %s", ComponentUtil.translate("&a채광 부스터"), ComponentUtil.translate("&e&l우클릭")));
        lore.add(ComponentUtil.translate("&e10초간 채광 속도가 3배로 증가"));
        lore.add(ComponentUtil.translate("&7재사용 대기시간 : %s", ComponentUtil.translate("&a%s초", 120)));
      }
      case PORTABLE_CRAFTING_TABLE -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&6능력 : %s %s", ComponentUtil.translate("&a작업대 사용"), ComponentUtil.translate("&e&l우클릭")));
      }
      case TODWOT_PICKAXE -> {
        if (viewer != null && CustomEffectManager.hasEffect(viewer, CustomEffectType.CUSTOM_MINING_SPEED_MODE))
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&7%s 확률로 %s을(를) 추가로 드롭한다", "&e5%", Material.BONE));
        }
      }
      case SANS_BOOTS, SANS_CHESTPLATE, SANS_LEGGINGS, SANS_HELMET -> {
        lore.add(Component.empty());
        boolean hasAll = false;
        if (viewer != null)
        {
          PlayerInventory playerInventory = viewer.getInventory();
          ItemStack helmet = playerInventory.getHelmet(), chestplate = playerInventory.getChestplate(), leggings = playerInventory.getLeggings(), boots = playerInventory.getBoots();
          if (ItemStackUtil.itemExists(helmet) && CustomMaterial.SANS_HELMET.toString().toLowerCase().equals(new NBTItem(helmet).getString("id")) &&
                  ItemStackUtil.itemExists(chestplate) && CustomMaterial.SANS_CHESTPLATE.toString().toLowerCase().equals(new NBTItem(chestplate).getString("id")) &&
                  ItemStackUtil.itemExists(leggings) && CustomMaterial.SANS_LEGGINGS.toString().toLowerCase().equals(new NBTItem(leggings).getString("id")) &&
                  ItemStackUtil.itemExists(boots) && CustomMaterial.SANS_BOOTS.toString().toLowerCase().equals(new NBTItem(boots).getString("id")))
          {
            hasAll = true;
          }
        }
        lore.add(ComponentUtil.translate("&6세트 능력 : %s", ComponentUtil.translate((hasAll ? "&a": "&7&m") +  "와 샌즈!")));
        lore.add(ComponentUtil.translate("&e스켈레톤에게 어그로가 끌리지 않음"));
      }
    }
  }
}

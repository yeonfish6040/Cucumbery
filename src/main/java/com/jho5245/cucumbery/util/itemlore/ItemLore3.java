package com.jho5245.cucumbery.util.itemlore;

import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import de.tr7zw.changeme.nbtapi.NBTList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ItemLore3
{
  protected static ItemStack setItemLore(@NotNull ItemStack itemStack, @Nullable Object... params)
  {
    Material type = itemStack.getType();
    ItemMeta itemMeta = itemStack.getItemMeta();
    List<Component> lore = itemMeta.lore();
    List<Component> description = new ArrayList<>(Collections.singletonList(Component.empty()));
    if (lore == null)
    {
      lore = new ArrayList<>();
    }
    switch (type)
    {
      case TORCH -> description.add(ComponentUtil.createTranslate("&7'기본 중의 기본이지!'"));
      case ACACIA_LEAVES, AZALEA_LEAVES, BIRCH_LEAVES, DARK_OAK_LEAVES, FLOWERING_AZALEA_LEAVES, JUNGLE_LEAVES,
              OAK_LEAVES -> description.add(Constant.ITEM_LORE_ONLY_WITH_SILKTOUCH_OR_SWEARS_COMPONENT);
      case BLACK_SHULKER_BOX, BLUE_SHULKER_BOX, BROWN_SHULKER_BOX, CYAN_SHULKER_BOX, GRAY_SHULKER_BOX, GREEN_SHULKER_BOX,
              LIGHT_BLUE_SHULKER_BOX, LIGHT_GRAY_SHULKER_BOX, LIME_SHULKER_BOX, MAGENTA_SHULKER_BOX, ORANGE_SHULKER_BOX,
              PINK_SHULKER_BOX, PURPLE_SHULKER_BOX, RED_SHULKER_BOX, WHITE_SHULKER_BOX, SHULKER_BOX,
              YELLOW_SHULKER_BOX -> {
        NBTList<String> extraTags = NBTAPI.getStringList(NBTAPI.getMainCompound(itemStack), CucumberyTag.EXTRA_TAGS_KEY);
        boolean isPortable = NBTAPI.arrayContainsValue(extraTags, Constant.ExtraTag.PORTABLE_SHULKER_BOX.toString());
        boolean isPlaceRestricted = NBTAPI.isRestrictedFinal(itemStack, Constant.RestrictionType.NO_PLACE);
        if (isPortable)
        {
          if (isPlaceRestricted)
          {
            description.addAll(Arrays.asList(
                    ComponentUtil.createTranslate("&7보다 훨씬 더 간편한 %s이다.", ComponentUtil.itemName(Material.SHULKER_BOX)),
                    ComponentUtil.createTranslate("&7해당 상자는 설치를 할 수 없는 대신"),
                    ComponentUtil.createTranslate("&7%s으로 바로 상자를 열 수 있다.", Component.keybind("key.use").color(NamedTextColor.YELLOW))
            ));
          }
          else
          {
            description.addAll(Arrays.asList(
                    ComponentUtil.createTranslate("&7보다 훨씬 더 간편한 %s이다.", ComponentUtil.itemName(Material.SHULKER_BOX)),
                    ComponentUtil.createTranslate("&7해당 상자는 설치를 할 필요 없이"),
                    ComponentUtil.createTranslate("&7%s으로 바로 상자를 열 수 있다.", Component.keybind("key.use").color(NamedTextColor.YELLOW)),
                    ComponentUtil.createTranslate("&c단, 설치 시 휴대 가능 효과가 사라진다.")
            ));
          }
        }
        else
        {
          description.addAll(Arrays.asList(
                  ComponentUtil.createTranslate("&7일반 %s와(과) 같으나, 설치한 %s을(를) 부수면", ComponentUtil.itemName(Material.CHEST), ComponentUtil.itemName(itemStack)),
                  ComponentUtil.createTranslate("&7내용물이 드롭되지 않고 상자 째로 드롭된다.")
          ));
        }
      }
      case ENDER_CHEST -> description.addAll(Arrays.asList(
              ComponentUtil.createTranslate("&7일반 %s와(과) 같으나, 다른 곳에", ComponentUtil.itemName(Material.CHEST)),
              ComponentUtil.createTranslate("&7설치된 모든 %s와(과) 내용물을 공유한다.", ComponentUtil.itemName(Material.ENDER_CHEST)),
              ComponentUtil.createTranslate("&7또한 다른 플레이어가 자신의 아이템을 가져갈 수 없다.")
      ));
    }
    if (description.size() > 1)
    {
      lore.addAll(description);
      itemMeta.lore(lore);
      itemStack.setItemMeta(itemMeta);
    }
    return itemStack;
  }
}

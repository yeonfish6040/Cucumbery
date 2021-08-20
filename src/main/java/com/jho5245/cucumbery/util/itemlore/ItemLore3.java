package com.jho5245.cucumbery.util.itemlore;

import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemLore3
{
  protected static ItemStack setItemLore(@NotNull ItemStack itemStack, @Nullable Object... params)
  {
    ItemMeta itemMeta = itemStack.getItemMeta();
    List<Component> lore = itemMeta.lore();
    List<Component> description = new ArrayList<>();
    description.add(Component.empty());
    if (lore == null)
    {
      lore = new ArrayList<>();
    }
    switch (itemStack.getType())
    {
      case DIAMOND:
        description.add(ComponentUtil.create("&2와 다이아다"));
        break;
      case ACACIA_LEAVES:
      case AZALEA_LEAVES:
      case BIRCH_LEAVES:
      case DARK_OAK_LEAVES:
      case FLOWERING_AZALEA_LEAVES:
      case JUNGLE_LEAVES:
      case OAK_LEAVES:
        description.add(Constant.ITEM_LORE_ONLY_WITH_SILKTOUCH_OR_SWEARS_COMPONENT);
        break;
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
        description.addAll(Arrays.asList(ComponentUtil.create("&e셜커 상자&7와 염료를 조합해서 얻을 수 있다."), ComponentUtil.create("&7일반 상자와 같으나, 설치한 셜커 상자를 부수면"), ComponentUtil.create("&7내용물이 드롭되지 않고 상자 째로 드롭된다.")));
        break;
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

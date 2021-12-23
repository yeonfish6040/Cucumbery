package com.jho5245.cucumbery.util.itemlore;

import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Constant.CucumberyHideFlag;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ItemLore3
{
  protected static ItemStack setItemLore(@NotNull ItemStack itemStack)
  {
    Material type = itemStack.getType();
    ItemMeta itemMeta = itemStack.getItemMeta();
    List<Component> lore = itemMeta.lore();
    if (lore == null)
    {
      lore = new ArrayList<>();
    }
    NBTList<String> hideFlags = NBTAPI.getStringList(NBTAPI.getMainCompound(itemStack), CucumberyTag.HIDE_FLAGS_KEY);
    if (!NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.TMI_DESCRIPTION))
    {
      List<Component> description = new ArrayList<>(Collections.singletonList(Component.empty()));

      switch (type)
      {
        case TORCH -> description.add(ComponentUtil.translate("&7'기본 중의 기본이지!'"));
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
                      ComponentUtil.translate("&7보다 훨씬 더 간편한 %s이다.", ItemNameUtil.itemName(Material.SHULKER_BOX)),
                      ComponentUtil.translate("&7해당 상자는 설치를 할 수 없는 대신"),
                      ComponentUtil.translate("&7%s으로 바로 상자를 열 수 있다.", Component.keybind("key.use").color(NamedTextColor.YELLOW))
              ));
            }
            else
            {
              description.addAll(Arrays.asList(
                      ComponentUtil.translate("&7보다 훨씬 더 간편한 %s이다.", ItemNameUtil.itemName(Material.SHULKER_BOX)),
                      ComponentUtil.translate("&7해당 상자는 설치를 할 필요 없이"),
                      ComponentUtil.translate("&7%s으로 바로 상자를 열 수 있다.", Component.keybind("key.use").color(NamedTextColor.YELLOW)),
                      ComponentUtil.translate("&c단, 설치 시 휴대 가능 효과가 사라진다.")
              ));
            }
          }
          else
          {
            description.addAll(Arrays.asList(
                    ComponentUtil.translate("&7일반 %s와(과) 같으나, 설치한 %s을(를) 부수면", ItemNameUtil.itemName(Material.CHEST), ItemNameUtil.itemName(Material.SHULKER_BOX)),
                    ComponentUtil.translate("&7내용물이 드롭되지 않고 상자 째로 드롭된다.")
            ));
          }
        }
        case DIAMOND -> description.add(
                ComponentUtil.translate("&2와 다이아다")
        );
        case ENDER_CHEST -> description.addAll(Arrays.asList(
                ComponentUtil.translate("&7일반 %s와(과) 같으나, 다른 곳에", ItemNameUtil.itemName(Material.CHEST)),
                ComponentUtil.translate("&7설치된 모든 %s와(과) 내용물을 공유한다.", ItemNameUtil.itemName(Material.ENDER_CHEST)),
                ComponentUtil.translate("&7또한 다른 플레이어가 자신의 아이템을 가져갈 수 없다.")
        ));
        case FURNACE -> description.add(
                ComponentUtil.translate("&710초당 아이템 1개를 구울 수 있다.")
        );
        case CHORUS_FRUIT -> description.addAll(Arrays.asList(
                ComponentUtil.translate("&7섭취 시 무작위 위치로 순간이동할 수 있다."),
                ComponentUtil.translate("&7근처에 이동할 수 있는 공간이 없다면 순간이동하지 않는다.")
        ));
        case ROTTEN_FLESH -> description.add(
                ComponentUtil.translate("&7%s에게는 먹여도 %s 상태 효과가 적용되지 않는다.",
                        ComponentUtil.translate("&e" + EntityType.WOLF.translationKey()), ComponentUtil.translate("&deffect.minecraft.hunger"))
        );
        case SPLASH_POTION -> description.add(
                ComponentUtil.translate("&7투척한 장소로부터 멀 수록 받는 효과의 지속 시간이 감소한다.")
        );
        case LINGERING_POTION -> description.addAll(Arrays.asList(
                ComponentUtil.translate("&7투척한 장소의 일정 범위 내에서 일정 시간동안"),
                ComponentUtil.translate("&7효과를 부여한다. 효과를 부여받는 대상이 있으면"),
                ComponentUtil.translate("&7효과를 받을 수 있는 범위와 시간이 빠르게 감소한다.")
        ));
        case CRYING_OBSIDIAN -> description.addAll(Arrays.asList(
                ComponentUtil.translate("&7%s에서 리스폰 지점을 설정할 수 있는", ComponentUtil.translate("&c네더")),
                ComponentUtil.translate("&7%s을(를) 만들 수 있는 재료이다.", ItemNameUtil.itemName(Material.RESPAWN_ANCHOR))
        ));
        case TARGET -> description.addAll(Arrays.asList(
                ComponentUtil.translate("&7발사체를 맞추면 전기를 출력한다."),
                ComponentUtil.translate("&7가운데에 가까울 수록 더욱 강력한 전기를 출력한다.")
        ));
        case TWISTING_VINES -> description.add(
                ComponentUtil.translate("&7고드름은 왜 거꾸로 자랄까? 이 덩굴도 그러지!")
        );
        case RESPAWN_ANCHOR -> description.addAll(Arrays.asList(
                ComponentUtil.translate("&7%s(으)로 우클릭하여 충전할 수 있다.", ItemNameUtil.itemName(Material.GLOWSTONE)),
                ComponentUtil.translate("&7충전 후 %s을(를) 우클릭하면 리스폰 지점을 설정할 수 있다.", ItemNameUtil.itemName(itemStack)),
                ComponentUtil.translate("&7%s가(이) 아닌 곳에서 리스폰 지점을 설정하면 %s이(가) 폭발한다.", ComponentUtil.translate("&c네더"), ItemNameUtil.itemName(itemStack))
        ));
        case LODESTONE -> description.add(
                ComponentUtil.translate("&7%s(으)로 우클릭하면 해당 %s은(는) 이 %s을(를) 가리키게 된다.", ItemNameUtil.itemName(Material.COMPASS), ItemNameUtil.itemName(Material.COMPASS), ItemNameUtil.itemName(itemStack))
        );
        case STONECUTTER -> description.addAll(Arrays.asList(
                ComponentUtil.translate("&71개의 재료만으로 %s보다 효율적으로.", ItemNameUtil.itemName(Material.CRAFTING_TABLE)),
                ComponentUtil.translate("&7돌 관련 블록을 반 블록, 계단, 담장으로 제작할 수 있다."),
                ComponentUtil.translate("&7%s 주민과 상호작용할 수 있는 블록이다.", ComponentUtil.translate("&eentity.minecraft.villager.mason"))
        ));
        case SMOKER -> description.addAll(Arrays.asList(
                ComponentUtil.translate("&7%s보다 땔감이 2배로 빠르게 소모되는 대신", ItemNameUtil.itemName(Material.FURNACE)),
                ComponentUtil.translate("&75초당 음식 관련 아이템 하나를 구울 수 있다."),
                ComponentUtil.translate("&7%s 주민과 상호작용할 수 있는 블록이다.", ComponentUtil.translate("&eentity.minecraft.villager.butcher"))
        ));
        case BLAST_FURNACE -> description.addAll(Arrays.asList(
                ComponentUtil.translate("&7%s보다 땔감이 2배로 빠르게 소모되는 대신", ItemNameUtil.itemName(Material.FURNACE)),
                ComponentUtil.translate("&75초당 광물 관련 아이템 하나를 구울 수 있다."),
                ComponentUtil.translate("&7%s 주민과 상호작용할 수 있는 블록이다.", ComponentUtil.translate("&eentity.minecraft.villager.armorer"))
        ));
        case SMITHING_TABLE -> description.addAll(Arrays.asList(
                ComponentUtil.translate("&7다이아몬드 재질의 도구, 무기 또는 갑옷을"),
                ComponentUtil.translate("&7네더라이트 재질의 아이템으로 업그레이드할 수 있다."),
                ComponentUtil.translate("&7%s 주민과 상호작용할 수 있는 블록이다.", ComponentUtil.translate("&eentity.minecraft.villager.toolsmith"))
        ));
        case LOOM -> description.addAll(Arrays.asList(
                ComponentUtil.translate("&7현수막에 염료를 사용하여 무늬를 교합할 수 있다."),
                ComponentUtil.translate("&7현수막 무늬가 있으면 특별한 무늬를 교합할 수 있다."),
                ComponentUtil.translate("&7%s 주민과 상호작용할 수 있는 블록이다.", ComponentUtil.translate("&eentity.minecraft.villager.shepherd"))
        ));
        case LECTERN -> description.addAll(Arrays.asList(
                ComponentUtil.translate("&7%s을(를) 여러 플레이어와 동시에 읽을 수 있다.", ItemNameUtil.itemName(Material.WRITTEN_BOOK)),
                ComponentUtil.translate("&7%s 주민과 상호작용할 수 있는 블록이다.", ComponentUtil.translate("&eentity.minecraft.villager.librarian"))
        ));
        case GRINDSTONE -> description.addAll(Arrays.asList(
                ComponentUtil.translate("&7같은 종류의 두 내구도가 있는 아이템을."),
                ComponentUtil.translate("&7추가 5% 내구도와 수리하거나 아이템에 부여된"),
                ComponentUtil.translate("&7마법을 제거하고 약간의 경험치를 획득할 수 있다."),
                ComponentUtil.translate("&7단, 저주 마법은 제거할 수 없다."),
                ComponentUtil.translate("&7%s 주민과 상호작용할 수 있는 블록이다.", ComponentUtil.translate("&eentity.minecraft.villager.weaponsmith"))
        ));
        case CARTOGRAPHY_TABLE -> description.addAll(Arrays.asList(
                ComponentUtil.translate("&7%s을(를) 복제, 확장하거나 잠글 수 있다.", ItemNameUtil.itemName(Material.MAP)),
                ComponentUtil.translate("&7%s 주민과 상호작용할 수 있는 블록이다.", ComponentUtil.translate("&eentity.minecraft.villager.cartographer"))
        ));
        case COMPOSTER -> description.addAll(Arrays.asList(
                ComponentUtil.translate("&7퇴비 제작 확률이 있는 아이템을 %s으로 사용하여", Component.keybind("key.use").color(NamedTextColor.YELLOW)),
                ComponentUtil.translate("&7정해진 확률대로 %s을(를) 한 칸 채울 수 있다.", ItemNameUtil.itemName(itemStack)),
                ComponentUtil.translate("&78칸을 다 채우면 %s을(를) 하나 얻을 수 있다.", ItemNameUtil.itemName(Material.BONE_MEAL)),
                ComponentUtil.translate("&7%s 주민과 상호작용할 수 있는 블록이다.", ComponentUtil.translate("&eentity.minecraft.villager.farmer"))
        ));
        case FLETCHING_TABLE -> description.add(
                ComponentUtil.translate("&7%s 주민과 상호작용할 수 있는 블록이다.", ComponentUtil.translate("&eentity.minecraft.villager.fletcher"))
        );
        case CAULDRON -> description.add(
                ComponentUtil.translate("&7%s 주민과 상호작용할 수 있는 블록이다.", ComponentUtil.translate("&eentity.minecraft.villager.leatherworker"))
        );
        case BREWING_STAND -> description.add(
                ComponentUtil.translate("&7%s 주민과 상호작용할 수 있는 블록이다.", ComponentUtil.translate("&eentity.minecraft.villager.cleric"))
        );
      }
      if (description.size() > 1)
      {
        lore.addAll(description);
        itemMeta.lore(lore);
        itemStack.setItemMeta(itemMeta);
      }
    }

    // CucumberyItemTag - CustomLore

    NBTItem nbtItem = new NBTItem(itemStack);
    NBTCompound itemTag = nbtItem.getCompound(CucumberyTag.KEY_MAIN);
    NBTList<String> customLores = itemTag != null ? itemTag.getStringList(CucumberyTag.CUSTOM_LORE_KEY) : null;
    if (!NBTAPI.arrayContainsValue(hideFlags, Constant.CucumberyHideFlag.CUSTOM_LORE) && customLores != null && !customLores.isEmpty())
    {
      for (String customLore : customLores)
      {
        lore.add(ComponentUtil.create(customLore));
      }
      itemMeta.lore(lore);
      itemStack.setItemMeta(itemMeta);
    }
    return itemStack;
  }
}

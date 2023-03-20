package com.jho5245.cucumbery.util.storage.component.util;

import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import com.jho5245.cucumbery.util.storage.no_groups.PlayerHeadInfo;
import io.papermc.paper.inventory.ItemRarity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextDecoration.State;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemNameUtil
{
  /**
   * 아이템의 이름을 컴포넌트 형태로 반환합니다.
   *
   * @param type 아이템의 종류
   * @return 컴포넌트 형태의 아이템 이름
   */
  public static Component itemName(@NotNull Material type)
  {
    return itemName(new ItemStack(type), null);
  }

  /**
   * 아이템의 이름을 컴포넌트 형태로 반환합니다.
   *
   * @param type 아이템의 종류
   * @return 컴포넌트 형태의 아이템 이름
   */
  public static Component itemName(@NotNull Material type, @Nullable TextColor defaultColor)
  {
    return itemName(new ItemStack(type), defaultColor);
  }

  /**
   * 아이템의 이름을 컴포넌트 형태로 반환합니다.
   *
   * @param itemStack 아이템
   * @return 컴포넌트 형태의 아이템 이름
   */
  @NotNull
  public static Component itemName(@NotNull ItemStack itemStack)
  {
    return itemName(itemStack, null);
  }

  /**
   * 아이템의 이름을 컴포넌트 형태로 반환합니다.
   *
   * @param itemStack    아이템
   * @param defaultColor 색상이 없을 경우 적용할 기본 값
   * @return 컴포넌트 형태의 아이템 이름
   */
  @NotNull
  public static Component itemName(@NotNull ItemStack itemStack, @Nullable TextColor defaultColor)
  {
    return itemName(itemStack, defaultColor, false);
  }

  /**
   * 아이템의 이름을 컴포넌트 형태로 반환합니다.
   *
   * @param itemStack             아이템
   * @param defaultColor          색상이 없을 경우 적용할 기본 값
   * @param respectCustomMaterial {@link CustomMaterial#getDisplayName()} 을 보전할 것인가?
   * @return 컴포넌트 형태의 아이템 이름
   */
  @NotNull
  public static Component itemName(@NotNull ItemStack itemStack, @Nullable TextColor defaultColor, boolean respectCustomMaterial)
  {
    Material material = itemStack.getType();
    ItemMeta itemMeta = itemStack.getItemMeta();
    Component component;
    // 아이템의 이름이 있을 경우 이름을 가져온다
    if (itemMeta != null && itemMeta.hasDisplayName())
    {
      try
      {
        component = itemMeta.displayName();
      }
      catch (Throwable t)
      {
        itemMeta.displayName(null);
        itemStack.setItemMeta(itemMeta);
        component = itemName(itemStack);
      }

      if (component == null) // 이런 경우는 없다.
      {
        component = Component.empty();
      }

      // 아이템의 기울임꼴 값이 없는 경우 기본적으로 아이템의 이름이 기울어져 있으므로 기울임 효과를 추가한다.
      if (component.decorations().get(TextDecoration.ITALIC) == State.NOT_SET)
      {
        component = component.decoration(TextDecoration.ITALIC, State.TRUE);
      }
      List<Component> children = new ArrayList<>(component.children());
      boolean childrenChanged = false;
      for (int i = 0; i < children.size(); i++)
      {
        Component child = children.get(i);
        if (child.decorations().get(TextDecoration.ITALIC) == State.NOT_SET)
        {
          child = child.decoration(TextDecoration.ITALIC, State.TRUE);
          childrenChanged = true;
        }
        children.set(i, child);
      }
      if (childrenChanged)
      {
        component = component.children(children);
      }
    }

    // 아이템의 이름이 없을 경우 번역된 아이템 이름을 가져온다.
    else
    {
      String id = material.translationKey();
      component = ComponentUtil.translate(id);
      // 특정 아이템은 다른 번역 규칙을 가지고 있으므로 해당 규칙을 적용한다.
      switch (material)
      {
        case WHEAT -> component = ComponentUtil.translate("item.minecraft.wheat");
        case PLAYER_HEAD, PLAYER_WALL_HEAD ->
        {
          String playerName = PlayerHeadInfo.getPlayerHeadInfo(itemStack, PlayerHeadInfo.PlayerHeadInfoType.NAME);
          if (playerName != null)
          {
            component = ComponentUtil.translate("block.minecraft.player_head.named").args(Component.text(playerName));
          }
        }
        case POTION, SPLASH_POTION, LINGERING_POTION, TIPPED_ARROW ->
        {
          PotionMeta potionMeta = (PotionMeta) itemMeta;
          if (potionMeta != null)
          {
            String potionId = potionMeta.getBasePotionData().getType().toString().toLowerCase();
            switch (potionMeta.getBasePotionData().getType())
            {
              default -> component = ComponentUtil.translate(id + ".effect." + potionId);
              case UNCRAFTABLE -> component = ComponentUtil.translate(id + ".effect.empty");
              case JUMP -> component = ComponentUtil.translate(id + ".effect.leaping");
              case REGEN -> component = ComponentUtil.translate(id + ".effect.regeneration");
              case SPEED -> component = ComponentUtil.translate(id + ".effect.swiftness");
              case INSTANT_HEAL -> component = ComponentUtil.translate(id + ".effect.healing");
              case INSTANT_DAMAGE -> component = ComponentUtil.translate(id + ".effect.harming");
            }
          }
        }
        case WRITTEN_BOOK ->
        {
          BookMeta bookMeta = (BookMeta) itemMeta;
          if (bookMeta != null && bookMeta.hasTitle())
          {
            component = bookMeta.title();
            if (component == null || ComponentUtil.serialize(component).isEmpty())
            {
              component = ComponentUtil.translate(id);
            }
          }
        }
        case COMPASS ->
        {
          CompassMeta compassMeta = (CompassMeta) itemMeta;
          if (compassMeta != null && compassMeta.hasLodestone())
          {
            component = ComponentUtil.translate("item.minecraft.lodestone_compass");
          }
        }
        case SHIELD ->
        {
          BlockStateMeta blockStateMeta = (BlockStateMeta) itemMeta;
          if (blockStateMeta != null && blockStateMeta.hasBlockState())
          {
            BlockState blockState = blockStateMeta.getBlockState();
            Banner bannerState = (Banner) blockState;
            DyeColor baseColor = bannerState.getBaseColor();
            component = ComponentUtil.translate(id + "." + baseColor.toString().toLowerCase());
          }
        }
      }
      // 이름이 없는 아이템은 기울임 효과가 없으므로 반드시 false로 한다.
      component = component.decoration(TextDecoration.ITALIC, State.FALSE);
    }

    // 커스텀 아이템의 경우 커스텀 아이템을 가져온다
    if (respectCustomMaterial || itemMeta == null || !itemMeta.hasDisplayName())
    {
      CustomMaterial customMaterial = CustomMaterial.itemStackOf(itemStack);
      if (customMaterial != null)
      {
        component = customMaterial.getDisplayName();
      }
    }

    @Nullable TextColor textColor = component.color();
    // 아이템의 등급이 있고 아이템 이름에 색깔이 없을 경우와 기본 아이템 색깔이 흰색이 아닐 경우 색깔을 추가한다.
    if (material.isItem() && material != Material.AIR)
    {
      ItemRarity itemRarity = material.getItemRarity();
      // 아이템에 마법이 부여되어 있을 경우 기본 아이템 이름의 색깔이 변경되므로 해당 색깔을 추가한다.
      boolean hasEnchants = itemMeta != null && itemMeta.hasEnchants();
      if (textColor == null)
      {
        switch (itemRarity)
        {
          case UNCOMMON -> textColor = hasEnchants ? NamedTextColor.AQUA : NamedTextColor.YELLOW;
          case RARE -> textColor = hasEnchants ? NamedTextColor.LIGHT_PURPLE : NamedTextColor.AQUA;
          case EPIC -> textColor = NamedTextColor.LIGHT_PURPLE;
          default -> textColor = hasEnchants ? NamedTextColor.AQUA : null;
        }
        component = component.color(textColor);
      }
    }
    if (textColor == null && defaultColor != null)
    {
      component = component.color(defaultColor);
    }
    return component;
  }
}

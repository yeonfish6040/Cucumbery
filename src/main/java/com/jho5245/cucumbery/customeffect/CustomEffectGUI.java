package com.jho5245.cucumbery.customeffect;

import com.jho5245.cucumbery.customeffect.CustomEffect.DisplayType;
import com.jho5245.cucumbery.util.ColorUtil;
import com.jho5245.cucumbery.util.ColorUtil.Type;
import com.jho5245.cucumbery.util.CreateGUI;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.CreateItemStack;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.TranslatableKeyParser;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextDecoration.State;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CustomEffectGUI
{
  public static void openGUI(@NotNull Player player, boolean firstOpen)
  {
    Inventory menu = CreateGUI.create(6, ComponentUtil.translate("&8적용 중인 효과 목록"), Constant.POTION_EFFECTS);
    if (firstOpen)
    {
      // deco template
      ItemStack deco = CreateItemStack.create(Material.WHITE_STAINED_GLASS_PANE, 1, Component.text("§와"), false);
      menu.setItem(0, deco);
      menu.setItem(1, deco);
      menu.setItem(2, deco);
      menu.setItem(3, deco);
      menu.setItem(5, deco);
      menu.setItem(6, deco);
      menu.setItem(7, deco);
      menu.setItem(8, deco);
      menu.setItem(9, deco);
      menu.setItem(17, deco);
      menu.setItem(18, deco);
      menu.setItem(26, deco);
      menu.setItem(27, deco);
      menu.setItem(35, deco);
      menu.setItem(36, deco);
      menu.setItem(44, deco);
      menu.setItem(45, deco);
      menu.setItem(46, deco);
      menu.setItem(47, deco);
      menu.setItem(48, deco);
      menu.setItem(49, deco);
      menu.setItem(50, deco);
      menu.setItem(51, deco);
      menu.setItem(52, deco);
      menu.setItem(53, deco);
      menu.setItem(4, CreateItemStack.create(Material.CLOCK, 1, Component.translatable("&e로딩중..."), false));
      player.openInventory(menu);
    }
    else
    {
      menu = player.getOpenInventory().getTopInventory();
      for (int i = 10; i <= 16; i++)
      {
        menu.setItem(i, null);
      }
      for (int i = 19; i <= 25; i++)
      {
        menu.setItem(i, null);
      }
      for (int i = 28; i <= 34; i++)
      {
        menu.setItem(i, null);
      }
      for (int i = 37; i <= 43; i++)
      {
        menu.setItem(i, null);
      }
    }
    List<CustomEffect> customEffects = CustomEffectManager.getEffects(player);
    customEffects.removeIf(effect -> effect.getDisplayType() == DisplayType.NONE || effect.isHidden());
    Collection<PotionEffect> potionEffects = player.getActivePotionEffects();
    boolean isEmpty = customEffects.isEmpty() && potionEffects.isEmpty();
    if (isEmpty)
    {
      menu.setItem(22, CreateItemStack.create(Material.RED_STAINED_GLASS_PANE, 1, ComponentUtil.translate("&c효과 없음!"),
              Arrays.asList(ComponentUtil.translate("&7적용 중인 효과가 하나도 없습니다."), ComponentUtil.translate("&7포션을 마셔서 효과를 적용시켜 보세요!")), false));
    }
    else
    {
      for (int i = 0; i < customEffects.size(); i++)
      {
        CustomEffect customEffect = customEffects.get(i);
        if (menu.firstEmpty() == -1)
        {
          continue;
        }
        CustomEffectType effectType = customEffect.getEffectType();
        String key = effectType.translationKey();
        ItemStack itemStack = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        potionMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ENCHANTS);
        potionMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        potionMeta.displayName(ComponentUtil.translate((effectType.isNegative() ? "&c" : "&a") + key));
        Color color = effectType.getColor();
        ColorUtil colorUtil = new ColorUtil(Type.HSL, "" + ((i * 30) % 255) + ",100,50;");
        potionMeta.setColor(color != null ? color : Color.fromRGB(colorUtil.getRed(), colorUtil.getGreen(), colorUtil.getBlue()));
        potionMeta.lore(customEffectLore(customEffect));
        itemStack.setItemMeta(potionMeta);
        if (effectType.isRightClickRemovable())
        {
          NBTItem nbtItem = new NBTItem(itemStack, true);
          nbtItem.setString("removeEffect", "custom:" + effectType);
          nbtItem.setInteger("removeEffectAmplifier", customEffect.getAmplifier());
        }
        menu.addItem(itemStack);
      }
      for (PotionEffect potionEffect : potionEffects)
      {
        if (menu.firstEmpty() == -1)
        {
          continue;
        }
        PotionEffectType effectType = potionEffect.getType();
        ItemStack itemStack = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        potionMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ENCHANTS);
        potionMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        String effectKey = TranslatableKeyParser.getKey(effectType);
        potionMeta.displayName(ComponentUtil.translate((CustomEffectManager.isVanillaNegative(effectType) ? "&c" : "&a") + effectKey));
        potionMeta.setColor(effectType.getColor());
        potionMeta.lore(potionEffectLore(potionEffect));
        itemStack.setItemMeta(potionMeta);
        if (!CustomEffectManager.isVanillaNegative(effectType))
        {
          NBTItem nbtItem = new NBTItem(itemStack, true);
          nbtItem.setString("removeEffect", "potion:" + effectType.getName());
        }
        menu.addItem(itemStack);
      }
    }
    int size = player.getActivePotionEffects().size() + CustomEffectManager.getEffects(player).size();
    ItemStack info = new ItemStack(Material.PLAYER_HEAD);
    SkullMeta skullMeta = (SkullMeta) info.getItemMeta();
    skullMeta.setOwningPlayer(player);
    skullMeta.displayName(ComponentUtil.translate("&o&q[%s의 효과 정보]", player));
    skullMeta.lore(List.of(ComponentUtil.translate("&7효과 개수 : %s개", size)));
    info.setItemMeta(skullMeta);
    menu.setItem(4, info);
  }

  @SuppressWarnings("all")
  @NotNull
  private static List<Component> customEffectLore(@NotNull CustomEffect customEffect)
  {
    List<Component> lore = new ArrayList<>();
    CustomEffectType effectType = customEffect.getEffectType();
    int duration = customEffect.getDuration();
    int amplifier = customEffect.getAmplifier();
    Component description = customEffect.getDescription();
    boolean isFinite = duration != -1, isAmplifiable = effectType.getMaxAmplifier() > 0, isTimeHidden = effectType.isTimeHidden();
    if (!description.equals(Component.empty()))
    {
      try
      {
        List<Component> children = new ArrayList<>(Collections.singletonList(description.children(Collections.emptyList())));
        children.addAll(description.children());
        for (int i = 0; i < children.size(); i++)
        {
          Component child = children.get(i);
          if (child.equals(Component.text("\n")) && i + 1 != children.size() && children.get(i + 1).equals(Component.text("\n")))
          {
            lore.add(Component.empty());
          }
          if (!child.equals(Component.text("\n")))
          {
            if (child.color() == null)
            {
              child = child.color(NamedTextColor.WHITE);
            }
            if (child.decoration(TextDecoration.ITALIC) == State.NOT_SET)
            {
              child = child.decoration(TextDecoration.ITALIC, State.FALSE);
            }
            lore.add(child);
          }
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      if ((isFinite && !isTimeHidden) || isAmplifiable)
      {
        lore.add(Component.empty());
        if (effectType == CustomEffectType.CURSE_OF_BEANS)
        {
          lore.add(Component.empty());
        }
      }
    }
    if (isFinite && !isTimeHidden)
    {
      lore.add(ComponentUtil.translate("&f지속 시간 : %s", Constant.THE_COLOR_HEX + Method.timeFormatMilli(duration * 50L, duration < 200, 1)));
      if (effectType == CustomEffectType.CURSE_OF_BEANS)
      {
        lore.add(ComponentUtil.translate("&f지속 시간 : %s", Constant.THE_COLOR_HEX + Method.timeFormatMilli(duration * 50L, duration < 200, 1)));
      }
    }
    if (isAmplifiable)
    {
      lore.add(ComponentUtil.translate("&f농도 레벨 : %s단계", amplifier + 1));
      if (effectType == CustomEffectType.CURSE_OF_BEANS)
      {
        lore.add(ComponentUtil.translate("&f농도 레벨 : %s단계", amplifier + 1));
      }
    }
    if (effectType.isRightClickRemovable())
    {
      lore.add(Component.empty());
      if (effectType == CustomEffectType.CURSE_OF_BEANS)
      {
        lore.add(Component.empty());
      }
      lore.add(ComponentUtil.translate("&e우클릭하여 효과 제거"));
    }
    return lore;
  }


  @NotNull
  @SuppressWarnings("all")
  private static List<Component> potionEffectLore(@NotNull PotionEffect potionEffect)
  {
    List<Component> lore = new ArrayList<>();

    PotionEffectType potionEffectType = potionEffect.getType();
    String effectKey = TranslatableKeyParser.getKey(potionEffectType);
    String id = effectKey.substring(17);
    lore.add(VanillaEffectDescription.getDescription(potionEffect).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, State.FALSE));
    int duration = potionEffect.getDuration(), amplifier = potionEffect.getAmplifier();
    lore.add(Component.empty());
    if (duration <= 20 * 60 * 60 * 24 * 365)
    {
      lore.add(ComponentUtil.translate("&f지속 시간 : %s", Constant.THE_COLOR_HEX + Method.timeFormatMilli(duration * 50L, duration < 200, 1)));
    }
    lore.add(ComponentUtil.translate("&f농도 레벨 : %s단계", amplifier + 1));
    if (!CustomEffectManager.isVanillaNegative(potionEffect.getType()))
    {
      lore.add(Component.empty());
      lore.add(ComponentUtil.translate("&e우클릭하여 효과 제거"));
    }
//    PotionEffectType potionEffectType = potionEffect.getType();
//    String effectKey = TranslatableKeyParser.getKey(potionEffectType);
//    String id = effectKey.substring(17);
//    int duration = potionEffect.getDuration(), amplifier = potionEffect.getAmplifier();
//    boolean hasParticles = potionEffect.hasParticles(), hasIcon = potionEffect.hasIcon(), isAmbient = potionEffect.isAmbient();
//    lore.add(ComponentUtil.translate("지속 시간 : %s", Constant.THE_COLOR_HEX + Method.timeFormatMilli(duration * 50L)));
//    lore.add(ComponentUtil.translate("농도 레벨 : %s단계", amplifier + 1));
//    if (!hasParticles)
//    {
//      lore.add(ComponentUtil.translate("&a입자 숨김"));
//    }
//    if (!hasIcon)
//    {
//      lore.add(ComponentUtil.translate("&a우측 상단 아이콘 숨김"));
//    }
//    if (isAmbient)
//    {
//      lore.add(ComponentUtil.translate("&a우측 상단 효과 빛남"));
//    }
//    lore.add(Component.text("minecraft:" + id, NamedTextColor.DARK_GRAY));
    return lore;
  }
}

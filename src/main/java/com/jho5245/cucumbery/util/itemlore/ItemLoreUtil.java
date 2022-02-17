package com.jho5245.cucumbery.util.itemlore;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.no_groups.ColorCode;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Constant.CustomEnchant;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemLoreUtil
{
  /**
   * 아이템이 Cucumbery에 의해서 설명이 변경되는지에 대한 여부를 확인합니다.
   *
   * @param itemStack 확인할 아이템
   * @return 아이템의 설명이 없거나,(lore == null) 아이템의 설명이 1개이고 그 설명이 (+NBT) 인 경우거나, 1번째 설명에 큐컴버리 아이템 등급 태그가 있거나, 설명에 mcMMO Ability Tool 이라는 설명이 마지막으로 끝나면 true, 이외의 경우에는 false
   */
  @SuppressWarnings("all")
  public static boolean isCucumberyTMIFood(@NotNull ItemStack itemStack)
  {
    // for some exceptions due to api
    try
    {
      Material type = itemStack.getType();
      if (type == Material.AIR || !type.isItem())
      {
        return false;
      }
      // 아이템의 메타 데이터
      ItemMeta itemMeta = itemStack.getItemMeta();
      // 메타 데이터가 없으면 true
      if (itemMeta == null)
      {
        return true;
      }
      // 아이템의 설명
      List<Component> lore = itemMeta.lore();
      // 아이템의 설명이 없으면 true
      if (lore == null || lore.size() == 0)
      {
        return true;
      }

      // 아이템의 설명이 있고 그 설명의 개수가 1개이고, 값이 (+NBT) 라면 true
      if (hasOnlyNbtTagLore(itemStack))
      {
        return true;
      }

      Component lastLor = lore.get(lore.size() - 1);

      // 마지막 설명이 [NBT 태그 복사됨] 이면 x
      if (lastLor instanceof TranslatableComponent translatableComponent)
      {
        if (translatableComponent.key().equals(Constant.TMI_LORE_NBT_TAG_COPIED))
        {
          return false;
        }
      }

      // 아이템의 설명이 있고 설명의 개수가 1개 이상
      if (lore != null && lore.size() > 0)
      {
        // 첫 번째 설명이 공백 번역 컴포넌트이고, 텍스트 형태의 매개 변수 1개만 있으면
        Component firstLor = lore.get(0);
        if (firstLor instanceof TranslatableComponent translatableComponent)
        {
          String key = translatableComponent.key();
          if (key.equals(""))
          {
            List<Component> args = translatableComponent.args();
            if (args.size() == 1 && args.get(0) instanceof TextComponent)
            {
              return true;
            }
            else
            {
              return false;
            }
          }
        }

        if (lore.size() > 1)
        {
          String firstLorSerial = ComponentUtil.serialize(firstLor);
          String secondLorSerial = MessageUtil.stripColor(ComponentUtil.serialize(lore.get(1)));
          if (firstLorSerial.equals("") && secondLorSerial.startsWith("아이템 종류"))
          {
            return true;
          }
        }
      }
    }
    catch (Exception ignored)
    {

    }
    return false;
  }

  public static void removeCucumberyTMIFood(@NotNull ItemStack itemStack)
  {
    ItemMeta itemMeta = itemStack.getItemMeta();
    List<Component> lore = itemMeta.lore();
    if (lore == null || lore.size() == 0)
    {
      return;
    }
    lore.set(0, Component.translatable("").args(Component.empty(), Component.empty()));
    itemMeta.lore(lore);
    itemStack.setItemMeta(itemMeta);
  }

  /**
   * 아이템의 설명에 (+NBT) 설명만 있는지 확인합니다.
   *
   * @param itemStack 설명을 확인할 아이템
   * @return 아이템의 설명이 오직 1개이고, 그 설명이 (+NBT) 일때만 true
   */
  public static boolean hasOnlyNbtTagLore(@NotNull ItemStack itemStack)
  {
    // 아이템에
    ItemMeta itemMeta = itemStack.getItemMeta();
    if (itemMeta == null)
    {
      return false;
    }
    List<Component> lore = itemMeta.lore();
    if (lore != null && lore.size() == 1)
    {
      Component component = lore.get(0);
      if (component instanceof TextComponent textComponent)
      {
        return textComponent.content().equals("(+NBT)");
      }

      List<Component> children = component.children();
      if (children.size() > 0 && children.get(0) instanceof TextComponent childTextComponent)
      {
        return childTextComponent.content().equals("(+NBT)");
      }
    }
    return false;
  }

  /**
   * 아이템의 등급 수치를 반환합니다.
   *
   * @param lore 등급을 참조할 아이템의 설명
   * @return 아이템의 등급 수치
   */
  public static long getItemRarityValue(@NotNull List<Component> lore)
  {
    if (lore.size() == 0)
    {
      return 0L;
    }
    Component lor = lore.get(0);
    if (lor instanceof TranslatableComponent translatableComponent)
    {
      List<Component> args = translatableComponent.args();
      if (args.size() == 1 && args.get(0) instanceof TextComponent textComponent)
      {
        try
        {
          return Long.parseLong(textComponent.content());
        }
        catch (Exception e)
        {
          return 0L;
        }
      }
    }
    return 0L;
  }

  /**
   * 아이템의 등급을 변경합니다.
   *
   * @param lore  아이템의 설명
   * @param value 변경할 등급 수치
   */
  public static void setItemRarityValue(@NotNull List<Component> lore, long value)
  {
    setItemRarityValue(lore, value, true);
  }

  /**
   * 아이템의 등급을 설정합니다.
   *
   * @param lore   아이템의 설명
   * @param value  설정할 등급
   * @param change true일 경우, 원래 아이템 등급에 해당 값을 더합니다.
   */
  public static void setItemRarityValue(@NotNull List<Component> lore, long value, boolean change)
  {
    long before = (change ? getItemRarityValue(lore) : 0) + value;
    lore.set(0, Component.translatable("", Component.text(before)));
  }

  /**
   * 아이템에 추가될 마법 부여 TMI
   *
   * @param itemMeta     아이템 메타
   * @param material     아이템의 종류
   * @param object       마법
   * @param enchantLevel 마법 레벨
   * @return 설명
   */
  @NotNull
  public static List<Component> enchantTMIDescription(
          ItemMeta itemMeta, Material material, @NotNull Object object, int enchantLevel)
  {
    List<Component> lore = new ArrayList<>();
    Component component = Component.empty();
    boolean useTMI = Cucumbery.config.getBoolean("use-tmi-enchantment-lore-feature");
    if (object instanceof Enchantment enchant)
    {
      if (enchant.getMaxLevel() == 1 && enchantLevel == 1)
      {
        component = Component.translatable(enchant.translationKey());
      }
      else
      {
        component = ComponentUtil.translate("%s %s", Component.translatable(enchant.translationKey()), Component.text(enchantLevel));
      }
      component = component.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
      component = component.color(enchant.isCursed() ? TextColor.color(255, 85, 85) : TextColor.color(154, 84, 255));
      lore.add(component);

      if (useTMI)
      {
        boolean compat = material == Material.ENCHANTED_BOOK;
        if ((compat || material == Material.BOW) && enchant.equals(Enchantment.ARROW_DAMAGE))
        {
          String msg = "&7화살의 대미지 %s 증가";
          lore.add(ComponentUtil.translate(msg, "&a" + Constant.Sosu2.format(25L * (1 + enchantLevel)) + "%p"));
        }
        if ((compat || material == Material.BOW) && enchant.equals(Enchantment.ARROW_FIRE))
        {
          String msg = "&7화살로 맞춘 대상에게 %s 부여";
          lore.add(ComponentUtil.translate(msg, ComponentUtil.translate("&a%s간 화염", ComponentUtil.translate("%s초", "5"))));
        }
        if ((compat || material == Material.BOW) && enchant.equals(Enchantment.ARROW_INFINITE))
        {
          String msg = "&7화살이 1개 이상 있으면 화살을 소비하지";
          lore.add(ComponentUtil.translate(msg));
          lore.add(ComponentUtil.translate("&7않고 %s (%s)",
                  ComponentUtil.translate("&a계속 사용 가능"), ComponentUtil.translate("&e%s은 제외", ComponentUtil.translate("item.minecraft.tipped_arrow"))));
        }
        if ((compat || material == Material.BOW) && enchant.equals(Enchantment.ARROW_KNOCKBACK))
        {
          String msg = "&7화살로 맞춘 대상이 %s 더 뒤로 밀려남";
          lore.add(ComponentUtil.translate(msg, ComponentUtil.translate("&a%s 블록", Constant.Jeongsu.format(3L * enchantLevel))));
        }
        if (enchant.equals(Enchantment.BINDING_CURSE))
        {
          String msg = "&7해당 아이템을 장착하면 ";
          lore.add(ComponentUtil.translate(msg).append(ComponentUtil.translate("&c절대로 탈착할 수 없음")));
        }
        if ((compat || material == Material.TRIDENT) && enchant.equals(Enchantment.CHANNELING))
        {
          String msg = "&7폭풍우때 적을 맞추면 ";
          lore.add(ComponentUtil.translate(msg).append(ComponentUtil.translate("&a번개를 내려침")));
        }
        if (enchant.equals(Enchantment.DAMAGE_ALL))
        {
          String msg = "&7아이템의 대미지 %s 증가";
          lore.add(ComponentUtil.translate(msg, "&a" + Constant.Sosu2.format(0.5 + 0.5 * enchantLevel)));
        }
        if (enchant.equals(Enchantment.DAMAGE_ARTHROPODS))
        {
          String msg = "&7절지동물류 개체에게 가하는 대미지 %s 증가";
          lore.add(ComponentUtil.translate(msg, "&a" + Constant.Sosu2.format(2.5 * enchantLevel)));
          lore.add(ComponentUtil.translate("&7및 %s 확률로 %s %s (%s) 적용",
                  ComponentUtil.create("&e100%"), ComponentUtil.translate("rgb255,97,144;effect.minecraft.slowness"),
                  ComponentUtil.translate("rgb255,97,144;4단계"),
                  ComponentUtil.translate("&a1~%s초", Constant.Sosu2.format(1 + 0.5 * enchantLevel))));
        }
        if (enchant.equals(Enchantment.DAMAGE_UNDEAD))
        {
          String msg = "&7언데드 개체에게 가하는 대미지 %s 증가";
          lore.add(ComponentUtil.translate(msg, "&a" + Constant.Sosu2.format(2.5 * enchantLevel)));
        }
        if (enchant.equals(Enchantment.DEPTH_STRIDER))
        {
          int level = Math.min(enchantLevel, 3);
          String msg = "&7물 속에서 이동 속도 감소량 %s 감소";
          lore.add(ComponentUtil.translate(msg, "&a" + Constant.Sosu2.format(level * 100d / 3) + "%"));
        }
        if ((compat || Constant.TOOLS.contains(material)) && enchant.equals(Enchantment.DIG_SPEED))
        {
          String msg = "&7블록을 캐는 속도 %s 증가";
          lore.add(ComponentUtil.translate(msg, "&a" + Constant.Jeongsu.format((long) enchantLevel * enchantLevel + 1)));
        }
        if ((compat || Constant.DURABLE_ITEMS.contains(material)) && enchant.equals(Enchantment.DURABILITY))
        {
          String msg = "&7내구도가 감소할 확률 %s 감소";
          if (Constant.ARMORS.contains(material))
          {
            lore.add(ComponentUtil.translate(msg, "&a" + Constant.Sosu2.format(100d - (60d + (40d / (enchantLevel + 1)))) + "%"));
          }
          else
          {
            if (material == Material.ENCHANTED_BOOK)
            {
              lore.add(ComponentUtil.translate(msg, ComponentUtil.translate("%s(갑옷의 경우 %s)",
                      ComponentUtil.create("&a" + Constant.Sosu2.format(100d - 100d / (enchantLevel + 1)) + "%"),
                      ComponentUtil.create("&a" + Constant.Sosu2.format(100d - (60d + (40d / (enchantLevel + 1)))) + "%"))));
            }
            else
            {
              lore.add(ComponentUtil.translate(msg, "&a" + Constant.Sosu2.format(100d - 100d / (enchantLevel + 1)) + "%"));
            }
          }
        }
        if (enchant.equals(Enchantment.FIRE_ASPECT))
        {
          String msg = "&7공격받은 개체에게 %s간 화염 부여";
          lore.add(ComponentUtil.translate(msg, ComponentUtil.translate("&a%s초", Constant.Jeongsu.format(enchantLevel * 4L))));
        }
        if (enchant.equals(Enchantment.FROST_WALKER))
        {
          String msg = "&7지면에 서 있는 경우 주변 %s 이내의 물이 살얼음으로 바뀜";
          lore.add(ComponentUtil.translate(msg, ComponentUtil.translate("&a%s 블록", Constant.Jeongsu.format(enchantLevel + 2))));
        }
        if ((compat || material == Material.TRIDENT) && enchant.equals(Enchantment.IMPALING))
        {
          String msg = "&7수상 개체에게 가하는 대미지 %s 증가";
          lore.add(ComponentUtil.translate(msg, "&a" + Constant.Sosu2.format(2.5 * enchantLevel)));
        }
        if (enchant.equals(Enchantment.KNOCKBACK))
        {
          String msg = "&7공격받은 대상이 %s 더 뒤로 밀려남";
          lore.add(ComponentUtil.translate(msg, ComponentUtil.translate("&a%s 블록", Constant.Jeongsu.format(3L * enchantLevel))));
        }
        if (enchant.equals(Enchantment.LOOT_BONUS_BLOCKS))
        {
          String msg = "&7레드스톤 광석, 작물의 최대 드롭 개수 %s 증가 및";
          lore.add(ComponentUtil.translate(msg, "&a" + Constant.Jeongsu.format(enchantLevel)));
          lore.add(ComponentUtil.translate("&7다른 광석의 아이템 드롭율 %s 증가",
                  "&a" + Constant.Sosu2.format(((1d / (enchantLevel + 2) + (enchantLevel + 1) / 2d) - 1D) * 100D) + "%"));
        }
        if (enchant.equals(Enchantment.LOOT_BONUS_MOBS))
        {
          String msg = "&7몬스터의 전리품 최대 드롭 개수 %s 증가 및 희귀";
          lore.add(ComponentUtil.translate(msg, "&a" + Constant.Jeongsu.format(enchantLevel)));
          lore.add(ComponentUtil.translate("&7아이템, 몬스터가 장착 중인 갑옷의 드롭율 %s 증가", "&a" + Constant.Jeongsu.format(enchantLevel) + "%"));
        }
        if ((compat || material == Material.TRIDENT) && enchant.equals(Enchantment.LOYALTY))
        {
          String msg = "&7삼지창이 대상을 맞추거나 블록에";
          lore.add(ComponentUtil.translate(msg));
          lore.add(ComponentUtil.translate("&7박히면 레벨이 높을 수록 더 빠르게 ").append(ComponentUtil.translate("&a주인에게 돌아옴")));
        }
        if ((compat || material == Material.FISHING_ROD) && enchant.equals(Enchantment.LUCK))
        {
          String treasure = "&85%&7 ➜ &a";
          String junk = "&810%&7 ➜ &a";
          switch (enchantLevel)
          {
            case 0 -> {
              treasure += "5%";
              junk += "10%";
            }
            case 1 -> {
              treasure += "7.1%";
              junk += "8.05%";
            }
            case 2 -> {
              treasure += "9.2%";
              junk += "6.1%";
            }
            case 3 -> {
              treasure += "11.3%";
              junk += "4.15%";
            }
            case 4 -> {
              treasure += "13.4%";
              junk += "2.2%";
            }
            case 5 -> {
              treasure += "15.5%";
              junk += "0.25%";
            }
            default -> {
              if (enchantLevel <= 45)
              {
                treasure += Constant.Sosu1.format(2.1 * enchantLevel + 5.0) + "%";
              }
              else
              {
                treasure += "100%";
              }
              junk += "0%";
            }
          }
          treasure += "&7";
          junk += "&7";
          String msg = "&7낚시 성공 시 보물 획득 확률 %s로 증가";
          lore.add(ComponentUtil.translate(msg, treasure));
          lore.add(ComponentUtil.translate("&7및 쓰레기 획득 확률 %s로 감소", junk));
        }
        if ((compat || material == Material.FISHING_ROD) && enchant.equals(Enchantment.LURE))
        {
          if (enchantLevel <= 5)
          {
            String msg = "&7낚시에 필요한 시간 %s 감소";
            lore.add(ComponentUtil.translate(msg, ComponentUtil.translate("&a%s초", "" + (5 * enchantLevel))));
          }
          else
          {
            String msg = "&7&c낚시에 필요한 시간 ∞로 증가";
            lore.add(ComponentUtil.translate(msg));
          }
        }
        if ((compat || Constant.DURABLE_ITEMS.contains(material)) && enchant.equals(Enchantment.MENDING))
        {
          String msg = "&7획득한 경험치 1당 ";
          lore.add(ComponentUtil.translate(msg).append(ComponentUtil.translate("&a내구도를 2만큼 회복")));
        }
        if ((compat || material == Material.CROSSBOW) && enchant.equals(Enchantment.MULTISHOT))
        {
          String msg = "&7한 번에 ";
          lore.add(ComponentUtil.translate(msg).append(ComponentUtil.translate("&a발사체를 3개 발사")));
        }
        if (enchant.equals(Enchantment.OXYGEN))
        {
          String msg = "&7물 속에서 호흡 시간 %s 증가";
          lore.add(ComponentUtil.translate(msg, ComponentUtil.translate("&a%s초", Constant.Jeongsu.format(enchantLevel * 15L))));
          lore.add(ComponentUtil.translate("&7추가로, 익사 대미지를 받을 확률 %s 감소", "&a" + Constant.Sosu2.format((enchantLevel * 1d / (enchantLevel + 1)) * 100D) + "%"));
        }
        if (enchant.equals(Enchantment.PIERCING))
        {
          String msg = "&7최대 %s개의 개체 관통 및 개체의 방패 방어력 무시";
          lore.add(ComponentUtil.translate(msg, "&a" + Constant.Jeongsu.format(enchantLevel + 1)));
        }
        if (enchant.equals(Enchantment.PROTECTION_ENVIRONMENTAL))
        {
          int decrease = (enchantLevel < 21 ? enchantLevel * 4 : 80);
          String msg = "&7받는 피해 %s 감소(%s), 일부 피해는 제외";
          lore.add(ComponentUtil.translate(msg, ComponentUtil.create("&a" + decrease + "%p"), ComponentUtil.translate("&e최대 80%p")));
        }
        if (enchant.equals(Enchantment.PROTECTION_EXPLOSIONS))
        {
          int decrease = (enchantLevel < 11 ? enchantLevel * 8 : 80);
          String msg = "&7폭발로 받는 피해 %s 감소(%s)";
          lore.add(ComponentUtil.translate(msg, ComponentUtil.create("&a" + decrease + "%p"), ComponentUtil.translate("&e최대 80%p")));
          decrease = (enchantLevel < 7 ? enchantLevel * 15 : 100);
          lore.add(ComponentUtil.translate("&7추가로, 폭발로 밀려나는 거리 %s 감소", "&a" + decrease + "%"));
        }
        if (enchant.equals(Enchantment.PROTECTION_FALL))
        {
          int decrease = (enchantLevel < 7 ? enchantLevel * 12 : 80);
          String msg = "&7낙하로 받는 피해 %s 감소(%s)";
          lore.add(ComponentUtil.translate(msg, ComponentUtil.create("&a" + decrease + "%p"), ComponentUtil.translate("&e최대 80%p")));
          lore.add(ComponentUtil.translate("&7또한, 낙하 피해량 감소로는 갑옷의 내구도가 깎이지 않음"));
        }
        if (enchant.equals(Enchantment.PROTECTION_FIRE))
        {
          int decrease = (enchantLevel < 11 ? enchantLevel * 8 : 100);
          String msg = "&7화염 지속 시간 %s 감소";
          lore.add(ComponentUtil.translate(msg, "&a" + decrease + "%"));
        }
        if (enchant.equals(Enchantment.PROTECTION_PROJECTILE))
        {
          int decrease = (enchantLevel < 11 ? enchantLevel * 8 : 80);
          String msg = "&7발사체로 받는 피해 %s 감소(%s)";
          lore.add(ComponentUtil.translate(msg, ComponentUtil.create("&a" + decrease + "%p"), ComponentUtil.translate("&e최대 80%p")));
        }
        if ((compat || material == Material.CROSSBOW) && enchant.equals(Enchantment.QUICK_CHARGE))
        {
          if (enchantLevel <= 5)
          {
            String msg = "&7쇠뇌의 장전 시간 %s 감소";
            lore.add(ComponentUtil.translate(msg, ComponentUtil.translate("&a%s초", Constant.Sosu2.format(0.25 * enchantLevel))));
          }
          else
          {
            String msg = "&7쇠뇌의 장전 시간 ∞로 증가";
            lore.add(ComponentUtil.translate(msg));
          }
        }
        if ((compat || material == Material.TRIDENT) && enchant.equals(Enchantment.RIPTIDE))
        {
          String msg = "&7삼지창을 더 이상 ";
          lore.add(ComponentUtil.translate(msg).append(ComponentUtil.translate("&c던질 수 없는 대신")));
          lore.add(ComponentUtil.translate("&7비가 오거나 물 속에서 사용하면 레벨이 높을 수록"));
          lore.add(ComponentUtil.translate("&7더욱 강한 삼지창의 추진력을 얻어 잠시 동안 ").append(ComponentUtil.translate("&a날 수 있음")));
        }
        if (enchant.equals(Enchantment.SILK_TOUCH))
        {
          String msg = "&7일부 블록을 원형 그대로 ";
          lore.add(ComponentUtil.translate(msg).append(ComponentUtil.translate("&a얻을 수 있음")));
        }
        if ((compat || Constant.SWORDS.contains(material)) && enchant.equals(Enchantment.SWEEPING_EDGE))
        {
          String msg = "&7검을 휘두를 때 주변 개체의";
          lore.add(ComponentUtil.translate(msg));
          lore.add(ComponentUtil.translate("&7받는 대미지가 검 대미지의 %s 만큼 증가", "&a" + Constant.Sosu2.format(enchantLevel * 1d / (enchantLevel + 1) * 100d) + "%"));
        }
        if (enchant.equals(Enchantment.THORNS))
        {
          int chance = Math.min(enchantLevel * 15, 100);
          int damage = (enchantLevel <= 10 ? -1 : enchantLevel - 10);
          String damageStr = (damage == -1 ? "1~4" : (enchantLevel - 10) + "");
          String msg = "&7자신을 공격한 대상에게 %s 확률로 %s의 대미지를 입힘";
          lore.add(ComponentUtil.translate(msg, "&e" + chance + "%", "&a" + damageStr));
        }
        if (enchant.equals(Enchantment.VANISHING_CURSE))
        {
          String msg = "&7인벤세이브가 꺼져 있을 경우 사망할";
          lore.add(ComponentUtil.translate(msg));
          lore.add(ComponentUtil.translate("&7때 아이템을 떨어트리는 대신 %s함", ComponentUtil.translate("&c아이템이 소멸")));
        }
        if (enchant.equals(Enchantment.WATER_WORKER))
        {
          String msg = "&7물 속에서 블록을 캐는 속도가 ";
          lore.add(ComponentUtil.translate(msg).append(ComponentUtil.translate("&a감소하지 않음")));
        }
        if (enchant.equals(Enchantment.SOUL_SPEED))
        {
          double nonArmorChance = 4d, armorChance = 4d;
          if (material == Material.ENCHANTED_BOOK)
          {
            EnchantmentStorageMeta storageMeta = (EnchantmentStorageMeta) itemMeta;
            if (storageMeta.hasStoredEnchant(Enchantment.DURABILITY))
            {
              int duraLevel = storageMeta.getStoredEnchantLevel(Enchantment.DURABILITY);
              if (duraLevel > 0)
              {
                armorChance *= (60d + (40d / (duraLevel + 1))) / 100d;
                nonArmorChance *= (100d / (duraLevel + 1)) / 100d;
              }
            }
          }
          else if (itemMeta.hasEnchant(Enchantment.DURABILITY))
          {
            int duraLevel = itemMeta.getEnchantLevel(Enchantment.DURABILITY);
            if (duraLevel > 0)
            {
              armorChance *= (60d + (40d / (duraLevel + 1))) / 100d;
              nonArmorChance *= (100d / (duraLevel + 1)) / 100d;
            }
          }

          String msg = "&7영혼 모래, 영혼 흙 위에서 이동 시 %s 확률";
          lore.add(ComponentUtil.translate(msg, "&e" + (Constant.ARMORS.contains(material) || material == Material.ENCHANTED_BOOK ?
                  Constant.Sosu2.format(armorChance) : Constant.Sosu2.format(nonArmorChance)) + "%"));
          lore.add(ComponentUtil.translate("&7로 내구도가 깎이는 대신 이동 속도 %s 증가",
                  "&a" + Constant.Sosu2.format((enchantLevel * 0.125 - 0.1) * 100d) + "%"));
        }
        if (enchant.equals(com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant.TELEKINESIS))
        {
          lore.addAll(Arrays.asList(
                  ComponentUtil.translate("&7블록을 캐거나 적을 잡았을 때 "),
                  ComponentUtil.translate("&7드롭하는 아이템과 경험치가 ").append(ComponentUtil.translate("&a즉시 인벤토리에 들어옴")))
          );
        }
      }
    }
    else if (object instanceof String id)
    {
      CustomEnchant customEnchant;
      try
      {
        customEnchant = CustomEnchant.valueOf(id);
      }
      catch (Exception e)
      {
        return new ArrayList<>();
      }
      boolean isCurse = switch (customEnchant)
              {
                case COARSE_TOUCH, UNSKILLED_TOUCH, VANISHING_TOUCH, DULL_TOUCH -> true;
                default -> false;
              };
      boolean maxIs1 = switch (customEnchant)
              {
                case DULL_TOUCH -> false;
                default -> true;
              };
      maxIs1 = maxIs1 && enchantLevel == 1;

      Component prefix = ComponentUtil.translate((isCurse ? "&c" : "rgb154,84,255;") + customEnchant.getDisplay());

      if (!maxIs1)
      {
        component = ComponentUtil.translate("%s %s", prefix, enchantLevel);
      }

      lore.add(prefix);
      if (useTMI)
      {
        switch (customEnchant)
        {
          case COARSE_TOUCH -> lore.add(
                  ComponentUtil.translate("&7블록을 캐거나 적을 잡았을 때 ").append(ComponentUtil.translate("&c아이템을 드롭하지 않음"))
          );
          case SMELTING_TOUCH -> lore.addAll(Arrays.asList(
                  ComponentUtil.translate("&7블록을 캐거나 적을 잡았을 때 "),
                  ComponentUtil.translate("&7드롭하는 아이템을 %s로 바꿔줌", ComponentUtil.translate("&a제련된 형태")))
          );
          case TELEKINESIS -> lore.addAll(Arrays.asList(
                  ComponentUtil.translate("&7블록을 캐거나 적을 잡았을 때 "),
                  ComponentUtil.translate("&7드롭하는 아이템과 경험치가 ").append(ComponentUtil.translate("&a즉시 인벤토리에 들어옴")))
          );
          case DULL_TOUCH -> lore.add(
                  ComponentUtil.translate("&7블록을 캘 경우 %s 확률로 부싯돌이 대신 나옴", "&e" + Math.min(100, enchantLevel) + "%")
          );
          case COLD_TOUCH -> lore.add(ComponentUtil.translate("&7얼음을 캘 경우 등급에 따른 종류의 얼음이 나옴"));
          case TELEKINESIS_PVP -> lore.addAll(Arrays.asList(
                  ComponentUtil.translate("&7인벤세이브가 꺼져 있는 경우에 플레이어와 싸웠을 때 "),
                  ComponentUtil.translate("&7드롭하는 아이템과 경험치가 ").append(ComponentUtil.translate("&a즉시 인벤토리에 들어옴")))
          );
          case UNSKILLED_TOUCH -> lore.add(
                  ComponentUtil.translate("&7블록을 캐거나 적을 잡았을 때 ").append(ComponentUtil.translate("&c경험치를 드롭하지 않음"))
          );
          case VANISHING_TOUCH -> lore.add(
                  ComponentUtil.translate("&7아이템이 들어있는 컨테이너를 캤을 때 ").append(ComponentUtil.translate("&c아이템을 드롭하지 않음"))
          );
          case WARM_TOUCH -> lore.add(ComponentUtil.translate("&7선인장을 캘 경우 등급에 따른 종류의 선인장이 나옴"));
        }
      }
    }
    List<Component> newLore = new ArrayList<>();
    if (lore.size() == 0)
    {
      newLore.add(component);
    }
    newLore.addAll(lore);
    lore = newLore;
    return lore;
  }

  public static String operationValue(AttributeModifier.Operation operation)
  {
    return switch (operation)
            {
              case ADD_SCALAR -> "%p";
              case MULTIPLY_SCALAR_1 -> "%";
              default -> "";
            };
  }

  public static int getOperationScore(AttributeModifier.Operation operation)
  {
    return switch (operation)
            {
              case ADD_NUMBER -> 1;
              case ADD_SCALAR -> 2;
              case MULTIPLY_SCALAR_1 -> 4;
            };
  }

  public static int getAttributeScore(Attribute attribute)
  {
    return switch (attribute)
            {
              case GENERIC_ARMOR, GENERIC_KNOCKBACK_RESISTANCE -> 2;
              case GENERIC_ARMOR_TOUGHNESS, GENERIC_LUCK -> 4;
              case GENERIC_ATTACK_DAMAGE, GENERIC_MAX_HEALTH, GENERIC_ATTACK_SPEED -> 3;
              case GENERIC_FOLLOW_RANGE, ZOMBIE_SPAWN_REINFORCEMENTS, HORSE_JUMP_STRENGTH, GENERIC_MOVEMENT_SPEED -> 1;
              default -> 0;
            };
  }

  public static void addFireworkEffectLore(@NotNull List<Component> lore, @NotNull FireworkEffect fireworkEffect)
  {
    String effectTypeString = switch (fireworkEffect.getType())
            {
              case BALL -> "small_ball";
              case BALL_LARGE -> "large_ball";
              case STAR, BURST, CREEPER -> fireworkEffect.getType().toString().toLowerCase();
            };
    lore.add(ComponentUtil.translate("&b폭죽 모양 : %s", ComponentUtil.translate("&eitem.minecraft.firework_star.shape." + effectTypeString)));
    // -- 아이템 등급 상승 --
    ItemLoreUtil.setItemRarityValue(lore, +30);
    switch (fireworkEffect.getType())
    {
      case BALL_LARGE:
        ItemLoreUtil.setItemRarityValue(lore, +20);
        break;
      case BURST:
        ItemLoreUtil.setItemRarityValue(lore, +30);
        break;
      case CREEPER:
        ItemLoreUtil.setItemRarityValue(lore, +100);
        break;
      case STAR:
        ItemLoreUtil.setItemRarityValue(lore, +15);
        break;
      default:
        break;
    }
    addColors(lore, ComponentUtil.translate("&a폭죽 색상 : %s"), fireworkEffect.getColors());
    addColors(lore, ComponentUtil.translate("&6사라지는 효과 색상 : "), fireworkEffect.getFadeColors());
    if (fireworkEffect.hasTrail())
    {
      lore.add(ComponentUtil.translate("&citem.minecraft.firework_star.trail"));
    }
    if (fireworkEffect.hasFlicker())
    {
      lore.add(ComponentUtil.translate("&citem.minecraft.firework_star.flicker"));
    }
  }

  private static void addColors(@NotNull List<Component> lore, @NotNull TranslatableComponent prefix, @NotNull List<Color> colors)
  {
    if (colors.size() == 0)
    {
      return;
    }
    StringBuilder key = new StringBuilder("&7");
    List<Component> args = new ArrayList<>();
    for (Color color : colors)
    {
      key.append("%s, ");
      String colorName = ColorCode.getColorName(color, "item.minecraft.firework_star.");
      if (colorName.endsWith("custom_color"))
      {
        args.add(ComponentUtil.create(ColorCode.getColorCode(color) + "#" + Integer.toHexString(0x100 | color.getRed()).substring(1)
                + Integer.toHexString(0x100 | color.getGreen()).substring(1) + Integer.toHexString(0x100 | color.getBlue()).substring(1)));
      }
      else
      {
        args.add(ComponentUtil.translate("&e" + colorName));
      }
    }
    key = new StringBuilder(key.substring(0, key.length() - 2));
    prefix = prefix.args(ComponentUtil.translate(key.toString(), args));
    lore.add(prefix);
  }

  /**
   * 인벤토리에 들어 있는 아이템 설명을 제거하여 nbt 데이터 크기를 줄입니다.(최적화)
   *
   * @param itemStack 최적화할 아이템 메타
   */
  public static void removeInventoryItemLore(@NotNull ItemStack itemStack)
  {
    ItemMeta itemMeta = itemStack.getItemMeta();
    if (itemMeta instanceof BlockStateMeta blockStateMeta)
    {
      if (blockStateMeta.hasBlockState())
      {
        BlockState blockState = blockStateMeta.getBlockState();
        if (blockState instanceof InventoryHolder inventoryHolder)
        {
          Inventory inventory = inventoryHolder.getInventory();
          for (int i = 0; i < inventory.getSize(); i++)
          {
            ItemStack innerItemStack = inventory.getItem(i);
            try
            {
              if (ItemStackUtil.itemExists(innerItemStack))
              {
                removeInventoryItemLore(innerItemStack);
                inventory.setItem(i, ItemLore.removeItemLore(innerItemStack));
              }
            }
            catch (Exception ignored)
            {

            }
          }
          blockStateMeta.setBlockState(blockState);
        }
        itemStack.setItemMeta(blockStateMeta);
      }
    }
  }
}











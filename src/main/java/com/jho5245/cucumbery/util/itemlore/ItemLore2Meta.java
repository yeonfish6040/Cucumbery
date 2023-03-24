package com.jho5245.cucumbery.util.itemlore;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.VanillaEffectDescription;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.ItemStackComponent;
import com.jho5245.cucumbery.util.storage.component.LocationComponent;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Constant.CucumberyHideFlag;
import com.jho5245.cucumbery.util.storage.no_groups.ColorCode;
import com.jho5245.cucumbery.util.storage.no_groups.ItemCategory.Rarity;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.map.MapView;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class ItemLore2Meta
{
  protected static void setItemLore(@Nullable Player viewer, @NotNull ItemStack item,
                                    @NotNull Material type, @NotNull ItemMeta itemMeta,
                                    @NotNull List<Component> lore, @NotNull NBTItem nbtItem,
                                    @Nullable NBTList<String> hideFlags, boolean hideBlockData, boolean hideStatusEffects, @Nullable Object params)
  {
    switch (type)
    {
      case POTION ->
      {
        if (!hideStatusEffects)
        {
          lore.addAll(ItemLorePotionDescription.getPotionList(viewer, item));
        }
        itemMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);

        PotionMeta potionMeta = (PotionMeta) itemMeta;
        PotionData data = potionMeta.getBasePotionData();
        PotionType potionType = data.getType();
        if (!(potionType == PotionType.AWKWARD || potionType == PotionType.MUNDANE || potionType == PotionType.THICK || potionType == PotionType.UNCRAFTABLE || potionType == PotionType.WATER))
        {
          ItemLoreUtil.setItemRarityValue(lore, +50);
        }
        if (data.isExtended())
        {
          ItemLoreUtil.setItemRarityValue(lore, +50);
        }
        if (data.isUpgraded())
        {
          ItemLoreUtil.setItemRarityValue(lore, +50);
        }
        for (PotionEffect effect : potionMeta.getCustomEffects())
        {
          ItemLoreUtil.setItemRarityValue(lore, 10L * ((effect.getDuration() / 200) + 1) * (effect.getAmplifier() + 1));
        }
      }
      case SPLASH_POTION ->
      {
        if (!hideStatusEffects)
        {
          lore.addAll(ItemLorePotionDescription.getSplashPotionList(viewer, item));
        }
        itemMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);

        PotionMeta potionMeta = (PotionMeta) itemMeta;
        PotionData data = potionMeta.getBasePotionData();
        PotionType potionType = data.getType();
        if (!(potionType == PotionType.AWKWARD || potionType == PotionType.MUNDANE || potionType == PotionType.THICK || potionType == PotionType.UNCRAFTABLE || potionType == PotionType.WATER))
        {
          ItemLoreUtil.setItemRarityValue(lore, 50);
        }
        if (data.isExtended())
        {
          ItemLoreUtil.setItemRarityValue(lore, 50);
        }
        if (data.isUpgraded())
        {
          ItemLoreUtil.setItemRarityValue(lore, 50);
        }
        for (PotionEffect effect : potionMeta.getCustomEffects())
        {
          ItemLoreUtil.setItemRarityValue(lore, 10L * ((effect.getDuration() / 200) + 1) * (effect.getAmplifier() + 1));
        }
      }
      case LINGERING_POTION ->
      {
        if (!hideStatusEffects)
        {
          lore.addAll(ItemLorePotionDescription.getLingeringPotionList(viewer, item));
        }
        itemMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);

        PotionMeta potionMeta = (PotionMeta) itemMeta;
        PotionData data = potionMeta.getBasePotionData();
        PotionType potionType = data.getType();
        if (!(potionType == PotionType.AWKWARD || potionType == PotionType.MUNDANE || potionType == PotionType.THICK || potionType == PotionType.UNCRAFTABLE || potionType == PotionType.WATER))
        {
          ItemLoreUtil.setItemRarityValue(lore, +50);
        }
        if (data.isExtended())
        {
          ItemLoreUtil.setItemRarityValue(lore, 50);
        }
        if (data.isUpgraded())
        {
          ItemLoreUtil.setItemRarityValue(lore, 50);
        }
        for (PotionEffect effect : potionMeta.getCustomEffects())
        {
          ItemLoreUtil.setItemRarityValue(lore, 10L * ((effect.getDuration() / 200) + 1) * (effect.getAmplifier() + 1));
        }
      }
      case TIPPED_ARROW ->
      {
        if (!hideStatusEffects)
        {
          lore.addAll(ItemLorePotionDescription.getTippedArrowList(viewer, item));
        }
        itemMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);

        PotionMeta potionMeta = (PotionMeta) itemMeta;
        PotionData data = potionMeta.getBasePotionData();
        PotionType potionType = data.getType();
        if (!(potionType == PotionType.AWKWARD || potionType == PotionType.MUNDANE || potionType == PotionType.THICK || potionType == PotionType.UNCRAFTABLE || potionType == PotionType.WATER))
        {
          ItemLoreUtil.setItemRarityValue(lore, 50);
        }
        if (data.isExtended())
        {
          ItemLoreUtil.setItemRarityValue(lore, 50);
        }
        if (data.isUpgraded())
        {
          ItemLoreUtil.setItemRarityValue(lore, 50);
        }
        for (PotionEffect effect : potionMeta.getCustomEffects())
        {
          ItemLoreUtil.setItemRarityValue(lore, 10L * ((effect.getDuration() / 200) + 1) * (effect.getAmplifier() + 1));
        }
      }
      case SPECTRAL_ARROW ->
      {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate(Constant.ITEM_LORE_STATUS_EFFECT));
        lore.add(ItemLorePotionDescription.getDescription(ItemLorePotionDescription.GLOWING, 10 * 20));
      }
      case WRITABLE_BOOK ->
      {
        // 야생에서는 불가능. 명령어로 강제로 책의 서명을 없앨때만 생기는 현상
        if (itemMeta.hasItemFlag(ItemFlag.HIDE_ITEM_SPECIFICS))
        {
          itemMeta.removeItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
        }
        BookMeta bookMeta = (BookMeta) item.getItemMeta();
        if (bookMeta.hasPages())
        {
          int pageCount = bookMeta.getPageCount();
          ItemLoreUtil.setItemRarityValue(lore, pageCount);
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&7쪽수 : %s", ComponentUtil.translate("&6%s장", pageCount)));
        }
      }
      case WRITTEN_BOOK ->
      {
        itemMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
        BookMeta bookMeta = (BookMeta) item.getItemMeta();
        Component author = bookMeta.author();
        if (author != null && author.color() == null)
        {
          author = author.color(TextColor.color(255, 170, 0));
        }
        int pageCount = bookMeta.getPageCount();
        ItemLoreUtil.setItemRarityValue(lore, pageCount);
        BookMeta.Generation g = bookMeta.getGeneration();
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7저자 : %s", author != null ? author : ComponentUtil.translate("알 수 없음")));
        lore.add(ComponentUtil.translate("&7출판 : %s", ComponentUtil.translate("&6book.generation." + (g != null ? g.ordinal() : "0"))));
        lore.add(ComponentUtil.translate("&7쪽수 : %s", ComponentUtil.translate("&6%s장", pageCount)));
      }
      case WHITE_BANNER, BLACK_BANNER, BLUE_BANNER, BROWN_BANNER, CYAN_BANNER, GRAY_BANNER, GREEN_BANNER, LIGHT_BLUE_BANNER, LIGHT_GRAY_BANNER
              , LIME_BANNER, MAGENTA_BANNER, ORANGE_BANNER, PURPLE_BANNER, PINK_BANNER, RED_BANNER, YELLOW_BANNER ->
      {
        BannerMeta bannerMeta = (BannerMeta) itemMeta;
        if (bannerMeta.numberOfPatterns() != 0)
        {
          bannerMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
          List<Pattern> patterns = bannerMeta.getPatterns();
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("#D0DCDE;[현수막 무늬 목록]"));
          for (Pattern pattern : patterns)
          {
            ItemLoreUtil.setItemRarityValue(lore, +10);
            switch (pattern.getPattern())
            {
              case CREEPER, SKULL -> ItemLoreUtil.setItemRarityValue(lore, Rarity.UNIQUE.getRarityValue());
              case FLOWER -> ItemLoreUtil.setItemRarityValue(lore, Rarity.NORMAL.getRarityValue());
              case GLOBE -> ItemLoreUtil.setItemRarityValue(lore, Rarity.ELITE.getRarityValue());
              case MOJANG -> ItemLoreUtil.setItemRarityValue(lore, Rarity.UNIQUE.getRarityValue() + 300);
              case PIGLIN -> ItemLoreUtil.setItemRarityValue(lore, Rarity.UNIQUE.getRarityValue() + 100);
              default ->
              {
              }
            }
            String patternTranslate = ColorCode.getColorCode(pattern.getColor())
                    + "block.minecraft.banner." + pattern.getPattern().toString().toLowerCase()
                    .replace("_middle", "").replace("stripe_small", "small_stripes")
                    .replace("_mirror", "") + "." +
                    pattern.getColor().toString().toLowerCase();
            lore.add(ComponentUtil.translate(patternTranslate.replace("stripe.", "stripe_middle.")));
          }
          if (bannerMeta.numberOfPatterns() > 6)
          {
            ItemLoreUtil.setItemRarityValue(lore, 20L * bannerMeta.numberOfPatterns());
          }
        }
        else
        {
          itemMeta.removeItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
        }
      }
      case SHIELD ->
      {
        Banner bannerMeta = (Banner) ((BlockStateMeta) itemMeta).getBlockState();
        if (bannerMeta.numberOfPatterns() != 0)
        {
          itemMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
          List<Pattern> patterns = bannerMeta.getPatterns();
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("#D0DCDE;[방패 무늬 목록]"));
          for (Pattern pattern : patterns)
          {
            ItemLoreUtil.setItemRarityValue(lore, +10);
            switch (pattern.getPattern())
            {
              case CREEPER, SKULL -> ItemLoreUtil.setItemRarityValue(lore, Rarity.UNIQUE.getRarityValue());
              case FLOWER -> ItemLoreUtil.setItemRarityValue(lore, Rarity.NORMAL.getRarityValue());
              case GLOBE -> ItemLoreUtil.setItemRarityValue(lore, Rarity.ELITE.getRarityValue());
              case MOJANG -> ItemLoreUtil.setItemRarityValue(lore, Rarity.UNIQUE.getRarityValue() + 300);
              case PIGLIN -> ItemLoreUtil.setItemRarityValue(lore, Rarity.UNIQUE.getRarityValue() + 100);
            }
            String patternTranslate = ColorCode.getColorCode(pattern.getColor())
                    + "block.minecraft.banner." + pattern.getPattern().toString().toLowerCase()
                    .replace("_middle", "").replace("stripe_small", "small_stripes")
                    .replace("_mirror", "") + "." +
                    pattern.getColor().toString().toLowerCase();
            lore.add(ComponentUtil.translate(patternTranslate.replace("stripe.", "stripe_middle.")));
          }
          if (bannerMeta.numberOfPatterns() > 6)
          {
            ItemLoreUtil.setItemRarityValue(lore, 20L * bannerMeta.numberOfPatterns());
          }
        }
        else
        {
          itemMeta.removeItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
        }
      }
      case TROPICAL_FISH_BUCKET ->
      {
        TropicalFishBucketMeta bucketMeta = (TropicalFishBucketMeta) itemMeta;
        if (bucketMeta.hasVariant())
        {
          bucketMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
          lore.add(Component.empty());
          String key = TropicalFishLore.getTropicalFishKey(bucketMeta.getBodyColor(), bucketMeta.getPatternColor(), bucketMeta.getPattern());

          Component arg;
          DyeColor bodyColor = bucketMeta.getBodyColor();
          DyeColor patternColor = bucketMeta.getPatternColor();
          if (key.contains("predefined"))
          {
            arg = ComponentUtil.translate("&6" + key);
          }
          else
          {
            String bodyColorKey = "color.minecraft." + bodyColor.toString().toLowerCase();
            String patternColorKey = "color.minecraft." + patternColor.toString().toLowerCase();
            arg = ComponentUtil.translate("&6" + bodyColorKey);
            if (bodyColor != patternColor)
            {
              arg = arg.append(ComponentUtil.create(", ").append(ComponentUtil.translate("&6" + patternColorKey)));
            }
            arg = arg.append(ComponentUtil.create(" ")).append(ComponentUtil.translate("&6" + key));
          }
          lore.add(ComponentUtil.translate("&7물고기 종 : %s", arg));
        }
      }
      case SUSPICIOUS_STEW ->
      {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate(Constant.ITEM_LORE_STATUS_EFFECT));
        SuspiciousStewMeta stewMeta = (SuspiciousStewMeta) itemMeta;
        List<Component> customPotionEffects = ItemLorePotionDescription.getCustomEffectList(viewer, item);
        if (!stewMeta.hasCustomEffects() && customPotionEffects.isEmpty())
        {
          lore.add(ItemLorePotionDescription.NONE);
        }
        else
        {
          for (PotionEffect effect : stewMeta.getCustomEffects())
          {
            lore.add(ItemLorePotionDescription.getDescription(ItemLorePotionDescription.getComponent(effect.getType()), effect.getDuration(), effect.getAmplifier() + 1));
            lore.addAll(ComponentUtil.convertHoverToItemLore(VanillaEffectDescription.getDescription(new PotionEffect(effect.getType(), 2, effect.getAmplifier())), NamedTextColor.GRAY));
            ItemLoreUtil.setItemRarityValue(lore, 10L * ((effect.getDuration() / 200) + 1) * (effect.getAmplifier() + 1));
          }
          lore.addAll(customPotionEffects);
        }
      }
      case FILLED_MAP ->
      {
        MapMeta mapMeta = (MapMeta) itemMeta;
        mapMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
        if (mapMeta.hasMapView() && mapMeta.getMapView() != null)
        {
          MapView mapView = mapMeta.getMapView();
          int centerX = mapView.getCenterX(), centerZ = mapView.getCenterZ();
          int id = mapView.getId();
          ItemLoreUtil.setItemRarityValue(lore, id * 2L);
          MapView.Scale scale = mapView.getScale();
          String scaleString = switch (scale)
                  {
                    case CLOSE -> "1:2";
                    case CLOSEST -> "1:1";
                    case FAR -> "1:8";
                    case FARTHEST -> "1:16";
                    case NORMAL -> "1:4";
                  };
          World world = mapView.getWorld();
          if (world != null)
          {
            String worldName = Method.getWorldDisplayName(world);
            lore.add(Component.empty());
            lore.add(ComponentUtil.translate("&7지도 ID : %s", "&6" + id));
            lore.add(ComponentUtil.translate("&7축척 : %s", "&6" + scaleString));
            if (Cucumbery.config.getBoolean("use-center-coord-of-map-lore-feature"))
            {
              lore.add(ComponentUtil.translate("&7월드 : %s", "&6" + worldName));
              lore.add(ComponentUtil.translate("&7지도 중심 좌표 : %s", "x=&6" + centerX + "&7, z=&6" + centerZ));
            }
          }
        }
      }
      case FIREWORK_STAR ->
      {
        FireworkEffectMeta fireworkEffectMeta = (FireworkEffectMeta) itemMeta;
        itemMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
        if (fireworkEffectMeta.hasEffect())
        {
          lore.add(Component.empty());
          FireworkEffect fireworkEffect = fireworkEffectMeta.getEffect();
          ItemLoreUtil.addFireworkEffectLore(lore, fireworkEffect);
        }
      }
      case CROSSBOW ->
      {
        CrossbowMeta crossbowMeta = (CrossbowMeta) itemMeta;
        if (crossbowMeta.hasChargedProjectiles())
        {
          itemMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
          lore.add(Component.empty());
          ItemStack chargedProjectile = crossbowMeta.getChargedProjectiles().get(0).clone();
          if (ItemStackUtil.itemExists(chargedProjectile))
          {
            lore.addAll(ItemStackUtil.getItemInfoAsComponents(chargedProjectile, ComponentUtil.translate("rg255,204;[발사체]"), true));
          }
          else
          {
            lore.remove(lore.size() - 1);
            itemMeta.removeItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
          }
        }
        else
        {
          itemMeta.removeItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
        }
      }
      case BOW ->
      {
        if (params instanceof EntityShootBowEvent event)
        {
          ItemStack consumable = event.getConsumable();
          if (ItemStackUtil.itemExists(consumable))
          {
            consumable = consumable.clone();
            lore.add(Component.empty());
            lore.addAll(ItemStackUtil.getItemInfoAsComponents(consumable, ComponentUtil.translate("rg255,204;[발사체]"), true));
          }
        }
      }
      case LEATHER_BOOTS, LEATHER_LEGGINGS, LEATHER_CHESTPLATE, LEATHER_HELMET, LEATHER_HORSE_ARMOR ->
      {
        itemMeta.addItemFlags(ItemFlag.HIDE_DYE);
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemMeta;
        Color color = leatherArmorMeta.getColor();
        int red = color.getRed(), green = color.getGreen(), blue = color.getBlue();
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("#a88932;[%s의 색상]", ItemNameUtil.itemName(item)));
        lore.add(ComponentUtil.create2("rgb" + red + "," + green + "," + blue + ";#" +
                Integer.toHexString(0x100 | red).substring(1) + Integer.toHexString(0x100 | green).substring(1) + Integer.toHexString(0x100 | blue).substring(1)));
      }
      case COMPASS ->
      {
        CompassMeta compassMeta = (CompassMeta) itemMeta;
        if (compassMeta.hasLodestone())
        {
          Location lodestoneLocation = compassMeta.getLodestone();
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("#BEBABA;[%s의 좌표]", ItemNameUtil.itemName(Material.LODESTONE)));
          lore.add(LocationComponent.locationComponent(lodestoneLocation));
        }
        else if (compassMeta.isLodestoneTracked())
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("#BEBABA;[%s의 좌표]", ItemNameUtil.itemName(Material.LODESTONE)));
          lore.add(ComponentUtil.translate("#BD443C;자석석이 " + (Math.random() * 100d > 10d ? "" : "&m미국감&q") + "분실됨"));
        }
      }
      case NOTE_BLOCK ->
      {
        if (!hideBlockData)
        {
          NBTCompound blockStateTag = nbtItem.getCompound(CucumberyTag.MINECRAFT_BLOCK_STATE_TAG_KEY);
          if (blockStateTag == null)
          {
            break;
          }
          String instrument = blockStateTag.getString("instrument");
          String note = blockStateTag.getString("note");
          lore.add(Component.empty());
          if (instrument != null && !instrument.equals(""))
          {
            lore.add(ComponentUtil.translate("rg255,204;악기 : %s", instrument));
          }
          if (note != null)
          {
            try
            {
              String noteString = ItemStackUtil.getNoteString(Integer.parseInt(note));
              lore.add(ComponentUtil.translate("rg255,204;음높이 : %s", noteString));
            }
            catch (Exception ignored)
            {

            }
          }
        }
      }
      case CREEPER_BANNER_PATTERN, FLOWER_BANNER_PATTERN, GLOBE_BANNER_PATTERN, MOJANG_BANNER_PATTERN,
              PIGLIN_BANNER_PATTERN, SKULL_BANNER_PATTERN ->
      {
        itemMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("rg255,204;무늬 : %s", ComponentUtil.translate("&7" + type.translationKey() + ".desc")));
      }
      case MUSIC_DISC_11, MUSIC_DISC_13, MUSIC_DISC_BLOCKS, MUSIC_DISC_CAT, MUSIC_DISC_CHIRP,
              MUSIC_DISC_FAR, MUSIC_DISC_MALL, MUSIC_DISC_MELLOHI, MUSIC_DISC_PIGSTEP, MUSIC_DISC_STAL,
              MUSIC_DISC_STRAD, MUSIC_DISC_WAIT, MUSIC_DISC_WARD, MUSIC_DISC_5, MUSIC_DISC_OTHERSIDE ->
      {
        itemMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
        lore.add(Component.empty());
        @SuppressWarnings("all")
        String composer = switch (type)
                {
                  case MUSIC_DISC_5 -> "Samuel Åberg";
                  case MUSIC_DISC_PIGSTEP, MUSIC_DISC_OTHERSIDE -> "Lena Raine";
                  default -> "C418";
                }, music = switch (type)
                {
                  case MUSIC_DISC_PIGSTEP -> "PigStep";
                  default -> type.toString().toLowerCase().substring("music_disc_".length());
                };
        lore.add(ComponentUtil.translate("rg255,204;작곡가 : %s", "&7" + composer));
        lore.add(ComponentUtil.translate("rg255,204;곡 : %s", "&7" + music));
      }
      case DISC_FRAGMENT_5 ->
      {
        itemMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("rg255,204;곡 : %s", "&75"));
      }
      case GOAT_HORN ->
      {
        if (nbtItem.hasTag("instrument"))
        {
          itemMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
          lore.add(Component.empty());
          String instrument = (nbtItem.getString("instrument") + "").replace("minecraft:", "minecraft.");
          lore.add(ComponentUtil.translate("&7유형 : %s", ComponentUtil.translate("instrument." + instrument)));
        }
        else
        {
          itemMeta.removeItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
        }
      }
      case DEBUG_STICK ->
      {
        if (NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.DEBUG_STICK.toString()))
        {
          break;
        }
        NBTCompound debugProperty = nbtItem.getCompound("DebugProperty");
        if (debugProperty != null && !debugProperty.getKeys().isEmpty())
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&9[디버그 속성]"));
          for (String key : debugProperty.getKeys())
          {
            String value = debugProperty.getString(key);
            Component itemType;
            try
            {
              itemType = ItemNameUtil.itemName(Material.valueOf(key.replace("minecraft:", "").replace(".", "_").toUpperCase()));
            }
            catch (Exception ignored)
            {
              itemType = ItemNameUtil.itemName(Material.STONE);
            }
            lore.add(ComponentUtil.translate("&7%s : %s", itemType, Constant.THE_COLOR_HEX + value));
          }
        }
      }
      case RECOVERY_COMPASS ->
      {
        if (viewer != null)
        {
          Location lastDeathLoc = viewer.getLastDeathLocation();
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&7사망 위치 : %s", lastDeathLoc != null ? lastDeathLoc : "translate:없음"));
        }
      }
      case BEACON ->
      {
        String customPotionEffectType = nbtItem.getString("CustomPotionEffectType");
        boolean isCustom = false;
        if (!customPotionEffectType.contains(":"))
        {
          customPotionEffectType = "cucumbery:" + customPotionEffectType;
        }
        try
        {
          CustomEffectType customEffectType = CustomEffectType.getByKey(Objects.requireNonNull(NamespacedKey.fromString(customPotionEffectType)));
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&7적용되는 효과 : %s", customEffectType));
          isCustom = true;
        }
        catch (Exception ignored)
        {

        }
        if (!isCustom)
        {
          String potionEffectType = nbtItem.getString("PotionEffectType");
          if (potionEffectType != null)
          {
            try
            {
              PotionEffectType effectType = PotionEffectType.getByKey(NamespacedKey.minecraft(potionEffectType));
              lore.add(Component.empty());
              lore.add(ComponentUtil.translate("&7적용되는 효과 : %s", effectType));
            }
            catch (Exception ignored)
            {

            }
          }
        }
      }
      case KNOWLEDGE_BOOK ->
      {
        KnowledgeBookMeta knowledgeBookMeta = (KnowledgeBookMeta) itemMeta;
        if (knowledgeBookMeta.hasRecipes())
        {
          List<NamespacedKey> recipes = knowledgeBookMeta.getRecipes();
          if (!recipes.isEmpty())
          {
            lore.add(Component.empty());
            lore.add(ComponentUtil.translate("&e[배울 수 있는 레시피]"));
            for (int i = 0; i < recipes.size(); i++)
            {
              NamespacedKey namespacedKey = recipes.get(i);
              Recipe recipe = Bukkit.getRecipe(namespacedKey);
              if (recipe == null)
              {
                continue;
              }
              if (recipe instanceof ComplexRecipe)
              {
                continue;
              }
              if (i == 20)
              {
                lore.add(ComponentUtil.translate("&7&ocontainer.shulkerBox.more", recipes.size() - 20));
                break;
              }
              ItemStack result = recipe.getResult();
              Component info = Component.empty();
              if (recipe instanceof BlastingRecipe)
              {
                info = ItemNameUtil.itemName(Material.BLAST_FURNACE);
              }
              else if (recipe instanceof CampfireRecipe)
              {
                info = ComponentUtil.translate("%s 또는 %s", ItemNameUtil.itemName(Material.CAMPFIRE), ItemNameUtil.itemName(Material.SOUL_CAMPFIRE));
              }
              else if (recipe instanceof FurnaceRecipe)
              {
                info = ItemNameUtil.itemName(Material.FURNACE);
              }
              else if (recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe)
              {
                info = ComponentUtil.translate("%s 또는 %s", ComponentUtil.translate("인벤토리"), ItemNameUtil.itemName(Material.CRAFTING_TABLE));
              }
              else if (recipe instanceof SmithingRecipe)
              {
                info = ItemNameUtil.itemName(Material.SMITHING_TABLE);
              }
              else if (recipe instanceof SmokingRecipe)
              {
                info = ItemNameUtil.itemName(Material.SMOKER);
              }
              else if (recipe instanceof StonecuttingRecipe)
              {
                info = ItemNameUtil.itemName(Material.STONECUTTER);
              }
              lore.add(ComponentUtil.translate("&7%s (%s)", ItemStackComponent.itemStackComponent(ItemLore.removeItemLore(result)), info));
            }
          }
        }
      }
    }
  }
}

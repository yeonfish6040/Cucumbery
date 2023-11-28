package com.jho5245.cucumbery.util.storage.component.util;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect.DisplayType;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.VanillaEffectDescription;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.itemlore.ItemLoreUtil;
import com.jho5245.cucumbery.util.no_groups.ItemSerializer;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.MessageUtil.ConsonantType;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.SelectorUtil;
import com.jho5245.cucumbery.util.storage.component.ItemStackComponent;
import com.jho5245.cucumbery.util.storage.component.LocationComponent;
import com.jho5245.cucumbery.util.storage.component.util.sendercomponent.SenderComponentUtil;
import com.jho5245.cucumbery.util.storage.data.*;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.ultimate.CustomEnchantUltimate;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTList;
import io.papermc.paper.advancement.AdvancementDisplay;
import io.papermc.paper.advancement.AdvancementDisplay.Frame;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.*;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextDecoration.State;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.translation.Translatable;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"deprecation", "unused"})
public class ComponentUtil
{
  private static final JSONParser JSON_PARSER = new JSONParser();
  /**
   * 컴포넌트 변환 URL
   */
  public static final Pattern URL = Pattern.compile("^(?:(https?)://)?([-\\w_.]{2,}\\.[a-z]{2,4})(/\\S*)?$");
  private static final Pattern p = Pattern.compile("%s"), p2 = Pattern.compile("%[0-9]+\\$s");
  private static final Pattern yeet = Pattern.compile("(%s|%[0-9]+\\$s)([^가-힣ㄱ-ㅎA-Za-z0-9]+|)" +
          "(이\\(가\\)|\\(이\\)가|가\\(이\\)|\\(가\\)이|을\\(를\\)|\\(을\\)를|를\\(을\\)|\\(를\\)을|와\\(과\\)|\\(와\\)과|과\\(와\\)|\\(과\\)와|은\\(는\\)|\\(은\\)는|는\\(은\\)|\\(는\\)은|\\(으\\)로|으\\(로\\)|\\(이\\)라|이\\(라\\))");
  final static private String q = "이\\(가\\)|\\(이\\)가|가\\(이\\)|\\(가\\)이|을\\(를\\)|\\(을\\)를|를\\(을\\)|\\(를\\)을|와\\(과\\)|\\(와\\)과|과\\(와\\)|\\(과\\)와|은\\(는\\)|\\(은\\)는|는\\(은\\)|\\(는\\)은|\\(으\\)로|으\\(로\\)|\\(이\\)라|이\\(라\\)";

  public static Component create(@NotNull Object... objects)
  {
    return create(null, objects);
  }

  /**
   * 컴포넌트를 생성합니다.
   *
   * @param objects 문자열 또는 다른 컴포넌트
   * @return 컴포넌트
   */
  @NotNull
  @SuppressWarnings("all")
  public static Component create(@Nullable Player player, @NotNull Object... objects)
  {
    Component component = Component.empty();
    boolean n2s = objects.length > 0 && !objects[0].equals(false);
    for (Object object : objects)
    {
      Component senderComponent = SenderComponentUtil.senderComponent(player, object, Constant.THE_COLOR);
      if (!senderComponent.equals(Component.empty()))
      {
        component = component.append(senderComponent);
        continue;
      }
      if (object instanceof Component)
      {
        Component concat = (Component) object;
        if (concat.equals(Component.empty()))
        {
          continue;
        }
        component = component.append(concat.children(Collections.emptyList()));
        for (Component child : concat.children())
        {
          component = component.append(ComponentUtil.create(player, child));
        }
      }
      else if (object instanceof List<?> list)
      {
        for (Object c : list)
        {
          Component concat = ComponentUtil.create(player, c);
          component = component.append(concat);
        }
      }
      else if (object instanceof NBTList<?> list)
      {
        for (Object c : list)
        {
          Component concat = ComponentUtil.create(player, c);
          component = component.append(concat);
        }
      }
      else if (object instanceof Component[] components)
      {
        for (Component c : components)
        {
          Component concat = (Component) c;
          component = component.append(ComponentUtil.create(player, concat));
        }
      }
      else if (object instanceof Material material)
      {
        if (!material.isItem() || material.isAir())
        {
          component = component.append(ItemNameUtil.itemName(material, Constant.THE_COLOR));
          continue;
        }
        ItemStack itemStack = ItemStackUtil.loredItemStack(material, player);
        Component concat = create(player, itemStack);
        component = component.append(concat);
      }
      else if (object instanceof ItemStack itemStack)
      {
        itemStack = itemStack.clone();
        if (player == null || Method.usingLoreFeature(player))
        {
          ItemLore.setItemLore(itemStack, player);
        }
        else
        {
          ItemLore.removeItemLore(itemStack);
        }
        Component concat = ItemStackComponent.itemStackComponent(itemStack, 1, null, false);
        if (player != null && !player.hasPermission("asdf"))
        {
          concat = concat.clickEvent(null);
        }
        if (concat.color() == null)
        {
          concat = concat.color(Constant.THE_COLOR);
        }
        component = component.append(concat);
      }
      else if (object instanceof CustomMaterial customMaterial)
      {
        Component concat = create(player, customMaterial.create());
        component = component.append(concat);
      }
      else if (object instanceof Prefix prefix)
      {
        Component concat = prefix.get();
        component = component.append(concat);
      }
      else if (object instanceof World world)
      {
        String worldName = Method.getWorldDisplayName(world);
        Component concat = ComponentUtil.create(worldName);
        if (concat.color() == null)
        {
          concat = concat.color(Constant.THE_COLOR);
        }
        int playerCount = world.getPlayerCount();
        Component hover = Component.empty().append(ComponentUtil.create(worldName));
        String environmentKey = switch (world.getEnvironment())
                {
                  case NORMAL -> "오버월드";
                  case NETHER -> "네더";
                  case THE_END -> "디 엔드";
                  case CUSTOM -> "사용자 지정";
                };
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.translate("유형 : %s", ComponentUtil.translate(environmentKey).color(Constant.THE_COLOR)));
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.translate("ID : %s", Constant.THE_COLOR_HEX + world.getName()));
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.translate("UUID : %s", Constant.THE_COLOR_HEX + world.getUID()));
        hover = hover.append(Component.text("\n"));
        if (player == null || player.hasPermission("asdf"))
        {
          Location spawnLocation = world.getSpawnLocation();
          hover = hover.append(ComponentUtil.translate("스폰 포인트 : %s",
                  ComponentUtil.translate("&7%s, %s, %s", Constant.THE_COLOR_HEX + spawnLocation.getBlockX(), Constant.THE_COLOR_HEX + spawnLocation.getBlockY(), Constant.THE_COLOR_HEX + spawnLocation.getBlockX())
          ));
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.translate("난이도 : %s", ComponentUtil.translate(Constant.THE_COLOR_HEX + world.getDifficulty().translationKey())));
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.translate("Y축 제한 범위 : %s", Constant.THE_COLOR_HEX + world.getMinHeight() + "~" + world.getMaxHeight()));
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.translate("플레이어 수 : %s", ComponentUtil.translate("%s명", Constant.THE_COLOR_HEX + playerCount)));
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.translate("&7클릭하여 자세한 월드 정보 보기"));
          concat = concat.clickEvent(ClickEvent.suggestCommand(
                  "/whatis " + world.getName()));
        }
        concat = concat.hoverEvent(hover);
        component = component.append(concat);
      }
      else if (object instanceof Location location)
      {
        Component concat = LocationComponent.locationComponent(location);
        if (player != null && !player.hasPermission("asdf"))
        {
          concat = concat.hoverEvent(null).clickEvent(null);
        }
        component = component.append(concat);
      }
      else if (object instanceof PotionEffectType potionEffectType)
      {
        String effectKey = TranslatableKeyParser.getKey(potionEffectType);
        String id = effectKey.substring(17);
        Component concat = ComponentUtil.translate(effectKey, CustomEffectManager.isVanillaNegative(potionEffectType) ? NamedTextColor.RED : NamedTextColor.GREEN);
        Component hover = ComponentUtil.translate(effectKey);
        hover = hover.append(VanillaEffectDescription.getDescription(potionEffectType, player));
        if (player == null || player.hasPermission("asdf"))
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.translate("&7클릭하여 효과를 자신에게 부여"));
          concat = concat.clickEvent(ClickEvent.suggestCommand(Cucumbery.using_CommandAPI ? "/ceffect @s minecraft:" + id : "/effect give @s minecraft:" + id));
        }
        concat = concat.hoverEvent(hover);
        component = component.append(concat);
      }
      else if (object instanceof PotionEffect potionEffect)
      {
        PotionEffectType potionEffectType = potionEffect.getType();
        String effectKey = TranslatableKeyParser.getKey(potionEffectType);
        String id = effectKey.substring(17);
        int duration = potionEffect.getDuration(), amplifier = potionEffect.getAmplifier();
        boolean hasParticles = potionEffect.hasParticles(), hasIcon = potionEffect.hasIcon(), isAmbient = potionEffect.isAmbient();
        Component concat = ComponentUtil.translate(effectKey, CustomEffectManager.isVanillaNegative(potionEffectType) ? NamedTextColor.RED : NamedTextColor.GREEN);
        Component hover = ComponentUtil.translate(effectKey);
        hover = hover.append(Component.text("\n"));
        hover = hover.append(VanillaEffectDescription.getDescription(potionEffect, player));
        hover = hover.append(Component.text("\n"));
        if (duration != -1)
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.translate("지속 시간 : %s", Constant.THE_COLOR_HEX + Method.timeFormatMilli(duration * 50L)));
        }
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.translate("농도 레벨 : %s단계", amplifier + 1));
        if (player == null || player.hasPermission("asdf"))
        {
          if (!hasParticles)
          {
            hover = hover.append(Component.text("\n"));
            hover = hover.append(ComponentUtil.translate("&a입자 숨김"));
          }
          if (!hasIcon)
          {
            hover = hover.append(Component.text("\n"));
            hover = hover.append(ComponentUtil.translate("&a우측 상단 아이콘 숨김"));
          }
          if (isAmbient)
          {
            hover = hover.append(Component.text("\n"));
            hover = hover.append(ComponentUtil.translate("&a우측 상단 효과 빛남"));
          }
          hover = hover.append(Component.text("\n"));
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.translate("&7클릭하여 효과를 자신에게 부여"));
          concat = concat.clickEvent(ClickEvent.suggestCommand(
                  "/ceffect @s minecraft:" + id + " " + duration + " " + amplifier + " " + !hasParticles + " " + !hasIcon + " " + !isAmbient));
        }
        concat = concat.hoverEvent(hover);
        component = component.append(concat);
      }
      else if (object instanceof CustomEffectType effectType)
      {
        String key = effectType.translationKey();
        Component concat = ComponentUtil.translate(key).color(effectType.isNegative() ? NamedTextColor.RED : NamedTextColor.GREEN);
        Component hover = ComponentUtil.translate(key);
        hover = hover.append(Component.text("\n"));
        hover = hover.append(effectType.getDescription());
        Component propertyDescription = effectType.getPropertyDescription();
        if (!propertyDescription.equals(Component.empty()))
        {
          hover = hover.append(propertyDescription);
        }
        if (player == null || player.hasPermission("asdf"))
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.translate("&7클릭하여 효과를 자신에게 부여"));
          String click = "/customeffect give @s " + effectType.toString().toLowerCase();
          concat = concat.clickEvent(ClickEvent.suggestCommand(click));
        }
        ItemStack icon = effectType.getIcon();
        if (icon != null)
        {
          new NBTItem(icon, true).setBoolean("VirtualItem", true);
          ItemMeta itemMeta = icon.getItemMeta();
          if (!itemMeta.hasCustomModelData())
          {
            itemMeta.setCustomModelData(effectType.getId());
          }
          itemMeta.displayName(concat.decoration(TextDecoration.ITALIC, State.FALSE));
          List<Component> lore = convertHoverToItemLore(hover, false);
          lore.remove(0);
          lore.remove(0);
          lore.removeIf(c -> c instanceof TextComponent textComponent && textComponent.content().equals(""));
          int size = lore.size();
          for (int i = 0; i < size; i++)
          {
            Component c = lore.get(i);
            if (i + 1 < size && c instanceof TextComponent textComponent && textComponent.content().equals("\n"))
            {
              lore.remove(i);
              size--;
              if (lore.get(i) instanceof TextComponent t && t.content().equals("\n"))
              {
                lore.set(i, Component.empty());
              }
            }
          }
          itemMeta.lore(lore);
          icon.setItemMeta(itemMeta);
          concat = concat.hoverEvent(icon.asHoverEvent()).clickEvent(concat.clickEvent());
        }
        else
        {
          concat = concat.hoverEvent(hover);
        }
        component = component.append(concat);
      }
      else if (object instanceof CustomEffect customEffect)
      {
        CustomEffectType effectType = customEffect.getType();
        String key = effectType.translationKey();
        int duration = customEffect.getInitDuration();
        int amplifier = customEffect.getInitAmplifier();
        Component concat = ComponentUtil.translate(key).color(effectType.isNegative() ? NamedTextColor.RED : NamedTextColor.GREEN);
        Component hover = ComponentUtil.translate(key);
        Component description = customEffect.getDescription(player);
        boolean isFinite = duration != -1, isAmplifiable = effectType.getMaxAmplifier() > 0;
        if (!description.equals(Component.empty()))
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(description);
          if (isFinite || isAmplifiable)
          {
            hover = hover.append(Component.text("\n"));
          }
        }
        if (isFinite)
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.translate("지속 시간 : %s", Constant.THE_COLOR_HEX + Method.timeFormatMilli(duration * 50L)));
          if (customEffect.isTimeHidden())
          {
            hover = hover.append(Component.text("\n"));
            hover = hover.append(ComponentUtil.translate("rg255,204;지속 시간이 표기되지 않는 효과입니다"));
          }
        }
        if (isAmplifiable)
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.translate("농도 레벨 : %s단계", amplifier + 1));
        }
        if (player == null || player.hasPermission("asdf"))
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.translate("&7클릭하여 효과를 자신에게 부여"));
          DisplayType displayType = customEffect.getDisplayType();
          String click = "/customeffect give @s " + effectType.toString().toLowerCase() + " " + (duration != -1 ? duration / 20d : "infinite") + " " + amplifier + " " + displayType.toString().toLowerCase();
          concat = concat.clickEvent(ClickEvent.suggestCommand(click));
        }
        ItemStack icon = effectType.getIcon();
        if (icon != null)
        {
          new NBTItem(icon, true).setBoolean("VirtualItem", true);
          ItemMeta itemMeta = icon.getItemMeta();
          if (!itemMeta.hasCustomModelData())
          {
            itemMeta.setCustomModelData(effectType.getId());
          }
          itemMeta.displayName(concat.decoration(TextDecoration.ITALIC, State.FALSE));
          List<Component> lore = convertHoverToItemLore(hover, false);
          lore.remove(0);
          lore.remove(0);
          int size = lore.size();
          for (int i = 0; i < size; i++)
          {
            Component c = lore.get(i);
            if (i + 1 < size && c instanceof TextComponent textComponent && textComponent.content().equals("\n"))
            {
              lore.remove(i);
              size--;
              if (lore.get(i) instanceof TextComponent t && t.content().equals("\n"))
              {
                lore.set(i, Component.empty());
              }
            }
          }
          itemMeta.lore(lore);
          icon.setItemMeta(itemMeta);
          concat = concat.hoverEvent(icon.asHoverEvent()).clickEvent(concat.clickEvent());
        }
        else
        {
          concat = concat.hoverEvent(hover);
        }
        component = component.append(concat);
      }
      else if (Cucumbery.using_NoteBlockAPI && object instanceof Song song)
      {
        String display = song.getPath().getName().replace("＃", "#").replace("？", "?").replace("：", ":");
        display = display.substring(0, display.length() - 4);
        Component concat = Component.text(display).color(Constant.THE_COLOR);
        Component hover = ComponentUtil.translate(display);
        String title = song.getTitle(), description = song.getDescription();
        String author = song.getAuthor(), originalAuthor = song.getOriginalAuthor();
        if (!title.equals(""))
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.translate("제목 : %s", Constant.THE_COLOR_HEX + title));
        }
        if (!description.equals(""))
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(Component.text("\n"));
          hover = hover.append(Component.text(description));
          hover = hover.append(Component.text("\n"));
        }
        if (!author.equals(""))
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.translate("제작자 : %s", Constant.THE_COLOR_HEX + author));
        }
        if (!originalAuthor.equals(""))
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.translate("원 제작자 : %s", Constant.THE_COLOR_HEX + originalAuthor));
        }
        if (!author.equals("") || !originalAuthor.equals("") || !title.equals("") || !description.equals(""))
        {
          hover = hover.append(Component.text("\n"));
        }
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.translate("속도 : %sTPS", Constant.THE_COLOR_HEX + song.getSpeed()));
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.translate("길이 : %s", Constant.THE_COLOR_HEX + Method.timeFormatMilli((long) ((song.getLength() / song.getSpeed()) * 1000L), true, 1)));
        if (player != null && player.hasPermission("cucumbery.command.song"))
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.translate("&7클릭하여 노래 재생"));
          display = song.getPath().getName();
          if (display.contains(" "))
          {
            display = "'" + display.replace("'", "''") + "'";
          }
          concat = concat.clickEvent(ClickEvent.suggestCommand("/csong play " + display));
        }
        concat = concat.hoverEvent(hover);
        component = component.append(concat);
      }
      else if (object instanceof Advancement advancement)
      {
        AdvancementDisplay display = advancement.getDisplay();
        if (display != null)
        {
          Frame frame = display.frame();
          Component title = display.title();
          Component description = display.description();
          if (frame == Frame.CHALLENGE)
          {
            title = title.color(TextColor.color(255, 112, 166));
            description = description.color(NamedTextColor.WHITE);
          }
          else if (frame == Frame.GOAL)
          {
            title = title.color(TextColor.color(30, 195, 246));
            description = description.color(TextColor.color(212, 213, 217));
          }
          else
          {
            title = title.color(TextColor.color(106, 255, 105));
            description = description.color(TextColor.color(212, 213, 217));
          }
          Component concat = translate("chat.square_brackets", title.hoverEvent(title.append(Component.text("\n")).append(description))).color(title.color());
          NamespacedKey namespacedKey = advancement.getKey();
          if (player == null || player.hasPermission("asdf"))
          {
            String suggest = "/advancement grant @s only " + namespacedKey.namespace() + ":" + namespacedKey.value();
            concat = concat.clickEvent(ClickEvent.suggestCommand(suggest));
          }
          component = component.append(concat);
        }
      }
      else if (object instanceof UserData userData)
      {
        String key = userData.getKey().replace("-", " ");
        Component concat = ComponentUtil.translate(key, Constant.THE_COLOR);
        if (player != null && player.hasPermission("asdf"))
        {
          Component hover = Component.empty().append(ComponentUtil.translate(key));
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.translate("내부 ID : %s", Constant.THE_COLOR_HEX + userData.toString().toLowerCase()));
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.translate("기본값 : %s", Constant.THE_COLOR_HEX + userData.getDefault()));
          hover = hover.append(Component.text("\n"));
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.translate("&7클릭하여 자신의 해당 유저 데이터 참조"));
          concat = concat.clickEvent(ClickEvent.suggestCommand("/userdata @s " + userData.toString().toLowerCase()));
          concat = concat.hoverEvent(hover);
        }
        component = component.append(concat);
      }
      else if (object instanceof Team team)
      {
        Component displayName = team.displayName(), prefix = team.prefix(), suffix = team.suffix();
        TextColor textColor = team.hasColor() ? team.color() : null;
        int size = team.getSize();
        String name = team.getName();
        component = component.append(Component.empty().append(prefix).append(displayName).append(suffix).hoverEvent(Component.text(name).append(Component.text("\n").append(ComponentUtil.translate("구성원 %s명", size)))));
      }
      else if (object instanceof Enchantment enchantment)
      {
        Component concat = ComponentUtil.translate(enchantment.translationKey());
        if (enchantment.isCursed())
        {
          concat = concat.color(NamedTextColor.RED);
        }
        else if (enchantment instanceof CustomEnchantUltimate)
        {
          concat = concat.color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.BOLD, State.TRUE);
        }
        else
        {
          concat = concat.color(TextColor.color(154, 84, 255));
        }
        Component hover = ComponentUtil.translate(enchantment.translationKey());
        ItemStack template = new ItemStack(Material.ENCHANTED_BOOK);
        List<Component> description = ItemLoreUtil.enchantTMIDescription(player, template, template.getItemMeta(), template.getType(), enchantment, enchantment.getMaxLevel(), true);
        if (description.size() >= 2)
        {
          for (int i = 1; i < description.size(); i++)
          {
            hover = hover.append(Component.text("\n"));
            hover = hover.append(description.get(i));
          }
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.translate("&8&o해당 설명은 %s레벨 기준입니다", enchantment.getMaxLevel()));
        }
        hover = hover.append(Component.text("\n"));
        NamespacedKey namespacedKey = enchantment.getKey();
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.translate("키 : %s", Constant.THE_COLOR_HEX + namespacedKey.toString()));
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.translate("최대 레벨 : %s", Constant.THE_COLOR_HEX + enchantment.getMaxLevel()));
        if (player != null && player.hasPermission("asdf"))
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.translate("&7클릭하여 손에 들고 있는 아이템에 해당 마법 부여"));
          concat = concat.clickEvent(ClickEvent.suggestCommand("/cenchant " + namespacedKey));
        }
        concat = concat.hoverEvent(hover);
        component = component.append(concat);
      }
      else if (object instanceof Translatable translatable)
      {
        component = component.append(ComponentUtil.translate(translatable.translationKey()));
      }
      else if (!(object instanceof Boolean))
      {
        String string = object == null ? "null" : object.toString();
        if (string.startsWith("translate:"))
        {
          string = string.substring(10);
          String[] split = MessageUtil.splitEscape(string, ';');
          string = split[0];
          String[] newArray = new String[split.length - 1];
          for (int i = 1; i < split.length; i++)
          {
            newArray[i - 1] = split[i];
          }
          split = newArray;
          Component concat = ComponentUtil.translate(string, split);
          component = component.append(ComponentUtil.create(concat));
        }
        else if (string.startsWith("selector:"))
        {
          string = string.substring(9);
          component = component.append(Component.selector(string));
        }
        else if (string.startsWith("keybind:"))
        {
          string = string.substring(8);
          component = component.append(Component.keybind(string));
        }
        else if (string.startsWith("player:"))
        {
          Component concat = Component.empty();
          Player player2 = SelectorUtil.getPlayer(player, string.substring("player:".length()), false);
          if (player2 != null)
          {
            concat = create(player, player2);
          }
          component = component.append(concat);
        }
        else if (string.startsWith("players:"))
        {
          Component concat = Component.empty();
          List<Player> players = SelectorUtil.getPlayers(player, string.substring("players:".length()), false);
          if (players != null)
          {
            concat = create(players);
          }
          component = component.append(concat);
        }
        else if (string.startsWith("entity:"))
        {
          Component concat = Component.empty();
          Entity entity = SelectorUtil.getEntity(player, string.substring("entity:".length()), false);
          if (entity != null)
          {
            concat = create(entity);
          }
          component = component.append(concat);
        }
        else if (string.startsWith("entities:"))
        {
          Component concat = Component.empty();
          List<Entity> entities = SelectorUtil.getEntities(player, string.substring("entities:".length()), false);
          if (entities != null)
          {
            concat = create(entities);
          }
          component = component.append(concat);
        }
        else if (string.startsWith("offline_player:"))
        {
          Component concat = Component.empty();
          OfflinePlayer offlinePlayer = SelectorUtil.getOfflinePlayer(player, string.substring("offline_player:".length()), false);
          if (offlinePlayer != null)
          {
            concat = create(offlinePlayer);
          }
          component = component.append(concat);
        }
        else if (string.startsWith("item:"))
        {
          ItemStack itemStack = ItemSerializer.deserialize(string.substring("item:".length()));
          if (!ItemStackUtil.itemExists(itemStack))
          {
            itemStack = new ItemStack(Material.STONE);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.displayName(ComponentUtil.translate("&c잘못된 아이템"));
            itemStack.setItemMeta(itemMeta);
          }
          else if (player == null || Method.usingLoreFeature(player))
          {
            ItemLore.setItemLore(itemStack, player);
          }
          component = component.append(create(itemStack));
        }
        else if (string.startsWith("items:"))
        {
          ItemStack itemStack = ItemSerializer.deserialize(string.substring("items:".length()));
          if (!ItemStackUtil.itemExists(itemStack))
          {
            itemStack = new ItemStack(Material.STONE);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.displayName(ComponentUtil.translate("&c잘못된 아이템"));
            itemStack.setItemMeta(itemMeta);
          }
          else if (player == null || Method.usingLoreFeature(player))
          {
            ItemLore.setItemLore(itemStack, player);
          }
          component = component.append(ItemStackComponent.itemStackComponent(itemStack, Constant.THE_COLOR));
        }
        else if (string.startsWith("world:"))
        {
          World world = Bukkit.getWorld(string.substring("world:".length()));
          if (world != null)
          {
            component = component.append(create(world));
          }
          else
          {
            component = component.append(translate("&c알 수 없는 월드입니다 (%s)", string.substring(6)));
          }
        }
        else if (string.startsWith("location:"))
        {
          string = string.substring("location:".length());
          String[] split = string.split(",");
          boolean invalid = false;
          try
          {
            World world = Bukkit.getWorld(split[0]);
            double x = Double.parseDouble(split[1]);
            double y = Double.parseDouble(split[2]);
            double z = Double.parseDouble(split[3]);
            Location location = new Location(world, x, y, z);
            if (split.length == 6)
            {
              double yaw = Double.parseDouble(split[4]);
              double pitch = Double.parseDouble(split[5]);
              location = new Location(world, x, y, z, (float) yaw, (float) pitch);
            }
            else if (split.length != 4)
            {
              invalid = true;
            }
            if (invalid || world == null)
            {
              component = component.append(translate("&c잘못된 좌표입니다 (%s)", string));
              continue;
            }
            component = component.append(LocationComponent.locationComponent(location));
          }
          catch (Exception ignored)
          {

          }
        }
        else if (string.startsWith("team:"))
        {
          Component concat = Component.empty();
          string = string.substring("team:".length());
          Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(string);
          if (team == null)
          {
            team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(string);
          }
          if (team == null)
          {
            concat = ComponentUtil.translate("&cteam.notFound", string);
          }
          else
          {
            concat = create(player, team);
          }
          component = component.append(concat);
        }
        else
        {
          try
          {
            JSON_PARSER.parse(string);
            Component concat = GsonComponentSerializer.gson().deserialize(string);
            component = component.append(ComponentUtil.create(player, concat));
          }
          catch (Throwable e)
          {
            Component concat = ComponentUtil.create2(string, n2s);
            component = component.append(ComponentUtil.create(player, concat));
          }
        }
      }
    }
    if (ComponentUtil.isPlainComponent(component))
    {
      List<Component> children = component.children();
      if (children.size() > 0)
      {
        if (!children.get(0).equals(Component.empty())
                && (
                ComponentUtil.isPlainComponent(children.get(0)) || children.size() == 1
        )
        )
        {
          component = children.get(0);
          for (int i = 1; i < children.size(); i++)
          {
            Component child = children.get(i);
            if (!child.equals(Component.empty()))
            {
              component = component.append(child);
            }
          }
        }
      }
    }
    List<Component> children = new ArrayList<>();
    for (Component child : component.children())
    {
      if (!child.equals(Component.empty()))
      {
        children.add(child);
      }
    }
    component = component.children(children);
    if (component instanceof TextComponent textComponent && textComponent.content().equals(""))
    {
      List<Component> children2 = component.children();
      if (children2.size() == 1)
      {
        Component newComponent = children2.get(0);
        if (newComponent.color() == null)
        {
          newComponent = newComponent.color(component.color());
        }
        return newComponent;
      }
    }
    return component;
  }

  /**
   * 텍스트 컴포넌트를 생성합니다.
   *
   * @param value 컴포넌트의 문자열 값
   * @return 텍스트 컴포넌트
   */
  public static Component create2(@NotNull String value)
  {
    return ComponentUtil.create2(value, true);
  }

  public static Component create2(@NotNull String value, boolean n2s)
  {
    return create2(null, value, n2s);
  }

  /**
   * 텍스트 컴포넌트를 생성합니다.
   *
   * @param value 컴포넌트의 문자열 값
   * @param n2s   색깔 코드 포맷 여부
   * @return 텍스트 컴포넌트
   */
  public static Component create2(@Nullable Player player, @NotNull String value, boolean n2s)
  {
    List<Component> components = ComponentUtil.fromLegacyText(player, value, n2s);
    Component component = Component.empty();
    for (Component textComponent : components)
    {
      component = component.append(textComponent);
    }
    return component;
  }

  /**
   * 번역 컴포넌트를 생성합니다.
   *
   * @param key 컴포넌트의 키 값
   * @return 번역 컴포넌트
   */
  public static TranslatableComponent translate(String key)
  {
    return ComponentUtil.translate(key, true);
  }

  @NotNull
  private static TranslatableComponent yeet(@NotNull String key, @NotNull TranslatableComponent component)
  {
    String vanillaKey = Variable.lang.getString(key.replace(".", "-"));
    boolean needChange = false;
    if (vanillaKey != null)
    {
      for (MessageUtil.ConsonantType type : MessageUtil.ConsonantType.values())
      {
        if (vanillaKey.contains(type.toString()))
        {
          needChange = true;
          break;
        }
      }
      if (needChange)
      {
        String serial = serialize(component);
        String editKey = yeet2(vanillaKey, serial);
        if (!editKey.equals(vanillaKey))
        {
          component = component.key(editKey);
        }
      }
    }
    else
    {
      String fallback = component.fallback();
      if (fallback != null)
      {
        key = fallback;
      }
      for (MessageUtil.ConsonantType type : MessageUtil.ConsonantType.values())
      {
        if (key.contains(type.toString()))
        {
          needChange = true;
          break;
        }
      }
      if (needChange)
      {
        String serial = serialize(component);
        String editKey = yeet2(key, serial);
        if (!editKey.equals(key))
        {
          component = component.key(editKey);
        }
      }
    }
    return component;
  }

  @NotNull
  private static String yeet2(@NotNull String key, @NotNull String serial)
  {
    Matcher matcher = yeet.matcher(key);
    String[] split = serial.replace("'", "").split(q);
    int loop = 0;
    int count = 1;
    while (matcher.find())
    {
      try
      {
        String a3 = matcher.group(3);
        ConsonantType consonantType = MessageUtil.ConsonantType.valueOf(a3.replace("(", "").replace(")", ""));
        if (Cucumbery.config.getBoolean("use-gusenited-consonant-grammar"))
        {
          consonantType = ConsonantType.values()[(int) (Math.random() * ConsonantType.values().length)];
          String replacer = MessageUtil.getFinalConsonant(Math.random() < 0.5 ? split[loop] : "" + Math.random(), consonantType);
          key = replace(key, q, replacer, count);
          count++;
        }
        else
        {
          String replacer = MessageUtil.getFinalConsonant(split[loop], consonantType);
          if (!a3.equals(replacer))
          {
            key = replace(key, q, replacer, count);
          }
          else
          {
            count++;
          }
        }
        loop++;
      }
      catch (Exception e)
      {
Cucumbery.getPlugin().getLogger().warning(        e.getMessage());
      }
    }
    return key;
  }

  @SuppressWarnings("all")
  private static String replace(@NotNull String key, @NotNull String from, @NotNull String to, int index)
  {
    StringBuilder sb = new StringBuilder();
    Pattern p = Pattern.compile(from);
    Matcher m = p.matcher(key);
    int count = 0;
    while (m.find())
    {
      if (count++ == index - 1)
      {
        m.appendReplacement(sb, to);
      }
    }
    m.appendTail(sb);
    return sb.toString();
  }

  @NotNull
  public static TranslatableComponent translate(@NotNull String key, @NotNull Object... args)
  {
    return translate(null, key, args);
  }

  /**
   * 번역 컴포넌트를 생성합니다.
   *
   * @param key  컴포넌트의 키 값
   * @param args 포맷할 매개 변수
   * @return 번역 컴포넌트
   */
  @NotNull
  @SuppressWarnings("all")
  public static TranslatableComponent translate(@Nullable Player player, @NotNull String key, @NotNull Object... args)
  {
    boolean n2s = args.length > 0 && (args[0] == null || !args[0].equals(false));
    TranslatableComponent component = ComponentUtil.fromLegacyTextTranslate(player, key, n2s);
    List<Component> componentArgs = new ArrayList<>();
    for (Object obj : args)
    {
      Component senderComponent = SenderComponentUtil.senderComponent(player, obj, Constant.THE_COLOR);
      if (!senderComponent.equals(Component.empty()))
      {
        if (senderComponent.decoration(TextDecoration.ITALIC) == State.FALSE)
        {
          senderComponent = senderComponent.decoration(TextDecoration.ITALIC, State.NOT_SET);
        }
        componentArgs.add(senderComponent);
        continue;
      }
      if (obj instanceof List<?> list)
      {
        for (Object o : list)
        {
          Component arg = ComponentUtil.create(player, n2s, o);
          if (arg.decoration(TextDecoration.ITALIC) == State.FALSE)
          {
            arg = arg.decoration(TextDecoration.ITALIC, State.NOT_SET);
          }
          componentArgs.add(arg);
        }
        continue;
      }
      if (obj instanceof Object[] array)
      {
        for (Object value : array)
        {
          componentArgs.add(ComponentUtil.create(player, value));
        }
        continue;
      }
      if (!(obj instanceof Boolean))
      {
        Component arg = ComponentUtil.create(player, n2s, obj);
        if (arg.decoration(TextDecoration.ITALIC) == State.FALSE)
        {
          arg = arg.decoration(TextDecoration.ITALIC, State.NOT_SET);
        }
        componentArgs.add(arg);
      }
    }
    if (!componentArgs.isEmpty())
    {
      component = component.args(componentArgs);
      component = yeet(component.key(), component);
    }
    return component;
  }

  @SuppressWarnings("unused")
  private static List<Component> fromLegacyText(@NotNull String message)
  {
    return fromLegacyText(message, true);
  }

  @SuppressWarnings("all")
  @NotNull
  private static List<Component> fromLegacyText(@NotNull String message, boolean n2s)
  {
    return fromLegacyText(null, message, n2s);
  }

  /**
   * 구버전의 텍스트를 컴포넌트로 변환합니다.
   *
   * @param message 구버전 텍스트
   * @return 컴포넌트
   */
  @NotNull
  private static List<Component> fromLegacyText(@Nullable Audience audience, @NotNull String message, boolean n2s)
  {
    if (n2s)
    {
      message = MessageUtil.n2s(message);
    }
    List<Component> components = new ArrayList<>();
    StringBuilder builder = new StringBuilder();
    TextComponent component = Component.empty();
    Matcher matcher = URL.matcher(message);
    for (int i = 0; i < message.length(); ++i)
    {
      char c = message.charAt(i);
      TextComponent old;
      if (c == 167)
      {
        ++i;
        if (i >= message.length())
        {
          break;
        }

        c = message.charAt(i);
        if (c >= 'A' && c <= 'Z')
        {
          c = (char) (c + 32);
        }

        @Nullable ChatColor format;
        if (c == 'x' && i + 12 < message.length())
        {
          StringBuilder hex = new StringBuilder("#");

          for (int j = 0; j < 6; ++j)
          {
            hex.append(message.charAt(i + 2 + j * 2));
          }
          try
          {
            format = ChatColor.of(hex.toString());
          }
          catch (IllegalArgumentException var11)
          {
            format = null;
          }
          i += 12;
        }
        else
        {
          format = ChatColor.getByChar(c);
        }

        if (format != null)
        {
          if (!builder.isEmpty())
          {
            old = component;
            old = old.content(builder.toString());
            builder = new StringBuilder();
            components.add(old);
          }
          if (format == ChatColor.BOLD)
          {
            component = component.decoration(TextDecoration.BOLD, true);
          }
          else if (format == ChatColor.ITALIC)
          {
            component = component.decoration(TextDecoration.ITALIC, true);
          }
          else if (format == ChatColor.UNDERLINE)
          {
            component = component.decoration(TextDecoration.UNDERLINED, true);
          }
          else if (format == ChatColor.STRIKETHROUGH)
          {
            component = component.decoration(TextDecoration.STRIKETHROUGH, true);
          }
          else if (format == ChatColor.MAGIC)
          {
            component = component.decoration(TextDecoration.OBFUSCATED, true);
          }
          else if (format == ChatColor.RESET)
          {
            component = Component.empty();
            component = component.color(null);
            component = component.decoration(TextDecoration.BOLD, State.NOT_SET);
            component = component.decoration(TextDecoration.ITALIC, State.NOT_SET);
            component = component.decoration(TextDecoration.UNDERLINED, State.NOT_SET);
            component = component.decoration(TextDecoration.STRIKETHROUGH, State.NOT_SET);
            component = component.decoration(TextDecoration.OBFUSCATED, State.NOT_SET);
          }
          else
          {
            component = Component.empty();
            java.awt.Color color = format.getColor();
            component = component.color(TextColor.color(color.getRed(), color.getGreen(), color.getBlue()));
            component = component.decoration(TextDecoration.BOLD, State.NOT_SET);
            component = component.decoration(TextDecoration.ITALIC, false);
            component = component.decoration(TextDecoration.UNDERLINED, State.NOT_SET);
            component = component.decoration(TextDecoration.STRIKETHROUGH, State.NOT_SET);
            component = component.decoration(TextDecoration.OBFUSCATED, State.NOT_SET);
          }
        }
        else
        {
          if (c == 'p' || c == 'q' || c == 'i')
          {
            if (!builder.isEmpty())
            {
              old = component;
              old = old.content(builder.toString());
              builder = new StringBuilder();
              components.add(old);
            }

            switch (c)
            {
              case 'p' -> component = component.color(component.color() != null ? NamedTextColor.WHITE : null);
              case 'q' ->
              {
                component = component.decoration(TextDecoration.BOLD, component.decoration(TextDecoration.BOLD) == State.TRUE ? State.FALSE : State.NOT_SET);
                component = component.decoration(TextDecoration.ITALIC, component.decoration(TextDecoration.ITALIC) == State.TRUE ? State.FALSE : State.NOT_SET);
                component = component.decoration(TextDecoration.UNDERLINED, component.decoration(TextDecoration.UNDERLINED) == State.TRUE ? State.FALSE : State.NOT_SET);
                component = component.decoration(TextDecoration.STRIKETHROUGH, component.decoration(TextDecoration.STRIKETHROUGH) == State.TRUE ? State.FALSE : State.NOT_SET);
                component = component.decoration(TextDecoration.OBFUSCATED, component.decoration(TextDecoration.OBFUSCATED) == State.TRUE ? State.FALSE : State.NOT_SET);
              }
              case 'i' -> component = component.decoration(TextDecoration.ITALIC, State.FALSE);
            }
          }
        }
      }
      else if (audience instanceof Player player && c == '[')
      {
        try
        {
          PlayerInventory playerInventory = player.getInventory();
          char next = message.charAt(i + 1);
          if (next == 'i' && message.charAt(i + 2) == ']')
          {
            i += 2;
            ItemStack itemStack = playerInventory.getItemInMainHand();
            if (ItemStackUtil.itemExists(itemStack))
            {
              if (!builder.isEmpty())
              {
                old = component;
                old = old.content(builder.toString());
                builder = new StringBuilder();
                components.add(old);
              }
              Component com = ItemStackComponent.itemStackComponent(itemStack, itemStack.getAmount(), Constant.THE_COLOR, true);
              if (com.color() == null)
              {
                com = com.color(Constant.THE_COLOR);
              }
              components.add(com);
            }
          }
          else
          {
            builder.append(c);
          }
        }
        catch (Exception e)
        {
          builder.append(c);
        }
      }
      else
      {
        int pos = message.indexOf(32, i);
        if (pos == -1)
        {
          pos = message.length();
        }

        if (matcher.region(i, pos).find())
        {
          if (!builder.isEmpty())
          {
            old = component;
            old = old.content(builder.toString());
            builder = new StringBuilder();
            components.add(old);
          }

          old = component;
          String urlString = message.substring(i, pos);
          component = component.content(urlString);
          String url = urlString.startsWith("http") ? urlString : "http://" + urlString;
          component = component.clickEvent(ClickEvent.openUrl(url));
          component = component.hoverEvent(HoverEvent.showText(ComponentUtil.translate("클릭하여 %s 주소로 연결합니다", Component.text(url).color(NamedTextColor.YELLOW))));
          components.add(component.decoration(TextDecoration.UNDERLINED, State.TRUE));
          i += pos - i - 1;
          component = old;
        }
        else
        {
          builder.append(c);
        }
      }
    }

    component = component.content(builder.toString());
    components.add(component);
    return components;
  }

  private static TranslatableComponent fromLegacyTextTranslate(@NotNull String message, boolean n2s)
  {
    return fromLegacyTextTranslate(null, message, n2s);
  }

  private static TranslatableComponent fromLegacyTextTranslate(@Nullable Audience audience, @NotNull String message, boolean n2s)
  {
    if (n2s)
    {
      message = MessageUtil.n2s(message);
    }
    TranslatableComponent components = null;
    StringBuilder builder = new StringBuilder();
    TranslatableComponent component = Component.translatable("");
    for (int i = 0; i < message.length(); ++i)
    {
      char c = message.charAt(i);
      TranslatableComponent old;
      if (c == 167)
      {
        ++i;
        if (i >= message.length())
        {
          break;
        }

        c = message.charAt(i);
        if (c >= 'A' && c <= 'Z')
        {
          c = (char) (c + 32);
        }
        @Nullable ChatColor format;
        if (c == 'x' && i + 12 < message.length())
        {
          StringBuilder hex = new StringBuilder("#");

          for (int j = 0; j < 6; ++j)
          {
            hex.append(message.charAt(i + 2 + j * 2));
          }

          try
          {
            format = ChatColor.of(hex.toString());
          }
          catch (IllegalArgumentException var11)
          {
            format = null;
          }
          i += 12;
        }
        else
        {
          format = ChatColor.getByChar(c);
        }

        if (format != null)
        {
          if (!builder.isEmpty())
          {
            old = component;
            old = old.key(builder.toString());
            builder = new StringBuilder();
            if (components == null)
            {
              components = old;
            }
            else
            {
              components = components.append(old);
            }
          }

          if (format == ChatColor.BOLD)
          {
            component = component.decoration(TextDecoration.BOLD, true);
          }
          else if (format == ChatColor.ITALIC)
          {
            component = component.decoration(TextDecoration.ITALIC, true);
          }
          else if (format == ChatColor.UNDERLINE)
          {
            component = component.decoration(TextDecoration.UNDERLINED, true);
          }
          else if (format == ChatColor.STRIKETHROUGH)
          {
            component = component.decoration(TextDecoration.STRIKETHROUGH, true);
          }
          else if (format == ChatColor.MAGIC)
          {
            component = component.decoration(TextDecoration.OBFUSCATED, true);
          }
          else if (format == ChatColor.RESET)
          {
            component = Component.translatable("");
            component = component.color(null);
            component = component.decoration(TextDecoration.BOLD, State.NOT_SET);
            component = component.decoration(TextDecoration.ITALIC, State.NOT_SET);
            component = component.decoration(TextDecoration.UNDERLINED, State.NOT_SET);
            component = component.decoration(TextDecoration.STRIKETHROUGH, State.NOT_SET);
            component = component.decoration(TextDecoration.OBFUSCATED, State.NOT_SET);
          }
          else
          {
            component = Component.translatable("");
            java.awt.Color color = format.getColor();
            component = component.color(TextColor.color(color.getRed(), color.getGreen(), color.getBlue()));
            component = component.decoration(TextDecoration.BOLD, State.NOT_SET);
            component = component.decoration(TextDecoration.ITALIC, false);
            component = component.decoration(TextDecoration.UNDERLINED, State.NOT_SET);
            component = component.decoration(TextDecoration.STRIKETHROUGH, State.NOT_SET);
            component = component.decoration(TextDecoration.OBFUSCATED, State.NOT_SET);
          }
        }
        else
        {
          if (c == 'p' || c == 'q' || c == 'i')
          {
            if (!builder.isEmpty())
            {
              old = component;
              old = old.key(builder.toString());
              builder = new StringBuilder();
              if (components == null)
              {
                components = old;
              }
              else
              {
                components = components.append(old);
              }
            }


            switch (c)
            {
              case 'p' -> component = component.color(component.color() != null ? NamedTextColor.WHITE : null);
              case 'q' ->
              {
                component = component.decoration(TextDecoration.BOLD, component.decoration(TextDecoration.BOLD) == State.TRUE ? State.FALSE : State.NOT_SET);
                component = component.decoration(TextDecoration.ITALIC, component.decoration(TextDecoration.ITALIC) == State.TRUE ? State.FALSE : State.NOT_SET);
                component = component.decoration(TextDecoration.UNDERLINED, component.decoration(TextDecoration.UNDERLINED) == State.TRUE ? State.FALSE : State.NOT_SET);
                component = component.decoration(TextDecoration.STRIKETHROUGH, component.decoration(TextDecoration.STRIKETHROUGH) == State.TRUE ? State.FALSE : State.NOT_SET);
                component = component.decoration(TextDecoration.OBFUSCATED, component.decoration(TextDecoration.OBFUSCATED) == State.TRUE ? State.FALSE : State.NOT_SET);
              }
              case 'i' -> component = component.decoration(TextDecoration.ITALIC, State.FALSE);
            }
          }
        }

      }
      else
      {
        builder.append(c);
      }
    }
    component = component.key(builder.toString());
    String key = component.key();
    if (key.startsWith("key:"))
    {
      String[] split = key.split("\\|");
      key = split[0].substring(4);
      if (split.length > 1)
      {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < split.length; i++)
        {
          stringBuilder.append(split[i]);
          if (i + 1 < split.length)
          {
            stringBuilder.append("|");
          }
        }
        String fallback = stringBuilder.toString();
        component = component.key(key).fallback(fallback);
      }
    }
    if (components == null)
    {
      components = component;
    }
    else
    {
      components = components.append(component);
    }
    return components;
  }

  @NotNull
  public static String serializeAsJson(@NotNull Component component)
  {
    return GsonComponentSerializer.gson().serialize(component);
  }

  @NotNull
  public static String serialize(@NotNull Component component)
  {
    StringBuilder builder = new StringBuilder();
    TextColor textColor = component.color();
    boolean italic = component.decoration(TextDecoration.ITALIC) == State.TRUE;
    boolean bold = component.decoration(TextDecoration.BOLD) == State.TRUE;
    boolean underlined = component.decoration(TextDecoration.UNDERLINED) == State.TRUE;
    boolean strikethrough = component.decoration(TextDecoration.STRIKETHROUGH) == State.TRUE;
    boolean obfuscated = component.decoration(TextDecoration.OBFUSCATED) == State.TRUE;
    String color = null;
    if (textColor != null)
    {
      if (textColor.equals(NamedTextColor.BLACK))
      {
        color = "§0";
      }
      else if (textColor.equals(NamedTextColor.DARK_BLUE))
      {
        color = "§1";
      }
      else if (textColor.equals(NamedTextColor.DARK_GREEN))
      {
        color = "§2";
      }
      else if (textColor.equals(NamedTextColor.DARK_AQUA))
      {
        color = "§3";
      }
      else if (textColor.equals(NamedTextColor.DARK_RED))
      {
        color = "§4";
      }
      else if (textColor.equals(NamedTextColor.DARK_PURPLE))
      {
        color = "§5";
      }
      else if (textColor.equals(NamedTextColor.GOLD))
      {
        color = "§6";
      }
      else if (textColor.equals(NamedTextColor.GRAY))
      {
        color = "§7";
      }
      else if (textColor.equals(NamedTextColor.DARK_GRAY))
      {
        color = "§8";
      }
      else if (textColor.equals(NamedTextColor.BLUE))
      {
        color = "§9";
      }
      else if (textColor.equals(NamedTextColor.GREEN))
      {
        color = "§a";
      }
      else if (textColor.equals(NamedTextColor.AQUA))
      {
        color = "§b";
      }
      else if (textColor.equals(NamedTextColor.RED))
      {
        color = "§c";
      }
      else if (textColor.equals(NamedTextColor.LIGHT_PURPLE))
      {
        color = "§d";
      }
      else if (textColor.equals(NamedTextColor.YELLOW))
      {
        color = "§e";
      }
      else if (textColor.equals(NamedTextColor.WHITE))
      {
        color = "§f";
      }
      else
      {
        color = "§x" + Method.format(textColor.asHexString().substring(1), "§");
      }
    }
    if (color != null)
    {
      builder.append(color);
    }
    if (italic)
    {
      builder.append("§o");
    }
    if (bold)
    {
      builder.append("§l");
    }
    if (underlined)
    {
      builder.append("§n");
    }
    if (strikethrough)
    {
      builder.append("§m");
    }
    if (obfuscated)
    {
      builder.append("§k");
    }
    if (component instanceof TextComponent textComponent)
    {
      builder.append(textComponent.content());
    }
    if (component instanceof TranslatableComponent translatableComponent)
    {
      String key = translatableComponent.key();
      String serial = Variable.lang.getString(key.replace(".", "-"));
      if (serial != null)
      {
        key = serial;
      }
      String fallback = translatableComponent.fallback();
      if (fallback != null)
      {
        key = fallback;
      }
      String test = key.replace("%%", "");
      int keyContains = (int) (p.matcher(test).results().count() + p2.matcher(test).results().count());
      List<Component> args = translatableComponent.args();
      List<String> argsSerial = new ArrayList<>();
      for (Component arg : args)
      {
        argsSerial.add(ComponentUtil.serialize(arg));
      }
      int argsSerialSize = argsSerial.size();
      if (argsSerialSize < keyContains)
      {
        for (int i = 0; i < keyContains - argsSerialSize; i++)
        {
          argsSerial.add("");
        }
      }
      try
      {
        key = String.format(key, argsSerial.toArray());
      }
      catch (Exception ignored)
      {
      }
      builder.append(key);
    }
    if (component instanceof SelectorComponent selectorComponent)
    {
      StringBuilder pattern = new StringBuilder(selectorComponent.pattern());
      try
      {
        List<Entity> entities = Bukkit.selectEntities(Bukkit.getConsoleSender(), pattern.toString());
        pattern = new StringBuilder();
        StringBuilder key = new StringBuilder("&7");
        List<Component> args = new ArrayList<>();
        for (Entity entity : entities)
        {
          key.append("%s, ");
          if (entity instanceof Player player)
          {
            args.add(Component.text(player.getName()));
          }
          else
          {
            args.add(ComponentUtil.translate(entity.getType().translationKey()));
          }
        }
        key = new StringBuilder(key.substring(0, key.length() - 2));
        Component translate = ComponentUtil.translate(key.toString(), args);
        pattern.append(ComponentUtil.serialize(translate));
      }
      catch (Exception ignored)
      {
      }
      builder.append(pattern);
    }
    if (component instanceof KeybindComponent keybindComponent)
    {
      builder.append(ComponentUtil.serialize(ComponentUtil.translate(keybindComponent.keybind())));
    }
    List<Component> children = component.children();
    for (Component child : children)
    {
      builder.append(ComponentUtil.serialize(child));
    }
    return builder.toString();
  }

  public static boolean isPlainComponent(@NotNull Component component)
  {
    if (component instanceof TextComponent textComponent)
    {
      if (textComponent.content().equals(""))
      {
        return true;
      }
    }
    return component.color() == null
            && component.hoverEvent() == null
            && component.clickEvent() == null
            && component.decoration(TextDecoration.ITALIC) == State.NOT_SET
            && component.decoration(TextDecoration.BOLD) == State.NOT_SET
            && component.decoration(TextDecoration.STRIKETHROUGH) == State.NOT_SET
            && component.decoration(TextDecoration.OBFUSCATED) == State.NOT_SET
            && component.decoration(TextDecoration.UNDERLINED) == State.NOT_SET;
  }

  /**
   * 컴포넌트의 호버 이벤트와 클릭 이벤트를 제거하여 반환합니다.
   *
   * @param component 이벤트를 제거할 컴포넌트
   * @return 이벤트가 제거된 컴포넌트
   */
  @NotNull
  public static Component stripEvent(@NotNull Component component)
  {
    List<Component> children = new ArrayList<>(component.children());
    children.replaceAll(ComponentUtil::stripEvent);
    component = component.clickEvent(null).hoverEvent(null).children(children).insertion(null);
    if (component instanceof TranslatableComponent translatableComponent)
    {
      List<Component> args = new ArrayList<>(translatableComponent.args());
      args.replaceAll(ComponentUtil::stripEvent);
      component = translatableComponent.args(args);
    }
    return component;
  }

  @NotNull
  public static Component timeFormat(long ms)
  {
    return timeFormat(ms, TimeFormatType.DEFAULT);
  }

  @NotNull
  public static Component timeFormat(long ms, @NotNull TimeFormatType formatType)
  {
    if (ms == 0 || (ms < 1000 && formatType == TimeFormatType.FULL_NO_DECIMALS))
    {
      return ComponentUtil.translate("%s초", 0);
    }
    if (formatType == TimeFormatType.SHORT)
    {
      if (ms >= 1000L * 60 * 60 * 24 * 365)
      {
        return ComponentUtil.translate("%s년", Constant.Sosu1ForceFloor.format(1d * ms / (1000L * 60 * 60 * 24 * 365)));
      }
      if (ms >= 1000L * 60 * 60 * 24)
      {
        return ComponentUtil.translate("%s일", Constant.Sosu1ForceFloor.format(1d * ms / (1000L * 60 * 60 * 24)));
      }
      if (ms >= 1000L * 60 * 999)
      {
        return ComponentUtil.translate("%s시간", Constant.Sosu1ForceFloor.format(1d * ms / (1000L * 60 * 60)));
      }
      if (ms >= 1000L * 60 * 10)
      {
        return ComponentUtil.translate("%s분", Constant.JeongsuFloor.format(Math.floor(1d * ms / (1000L * 60))));
      }
      if (ms >= 1000L * 60)
      {
        return ComponentUtil.translate("%s분", Constant.Sosu1ForceFloor.format(1d * ms / (1000L * 60)));
      }
      if (ms >= 1000L * 10)
      {
        return ComponentUtil.translate("%s초", Constant.JeongsuFloor.format(1d * ms / 1000L));
      }
      return ComponentUtil.translate("%s초", Constant.Sosu1ForceFloor.format(1d * ms / 1000L));
    }
    long year = ms / (1000L * 60 * 60 * 24 * 365);
    ms %= (1000L * 60 * 60 * 24 * 365);
    long day = ms / (1000L * 60 * 60 * 24);
    ms %= (1000L * 60 * 60 * 24);
    long hour = ms / (1000L * 60 * 60);
    ms %= (1000L * 60 * 60);
    long min = ms / (1000L * 60);
    ms %= (1000L * 60);
    double sec = ms / 1000D;
    String displaySec = switch (formatType)
            {
              case FULL_NO_DECIMALS -> Constant.Jeongsu.format(Math.floor(sec));
              case FULL_SINGLE_DECIMAL -> Constant.Sosu1Force.format(sec);
              default -> Constant.Sosu2.format(sec);
            };
    Component component = Component.empty();
    component = component.append(Component.empty());
    if (year > 0)
    {
      component = component.append(ComponentUtil.translate("%s년", year));
    }
    if (day > 0)
    {
      if (year > 0)
      {
        component = component.append(Component.text(" "));
      }
      component = component.append(ComponentUtil.translate("%s일", day));
    }
    if (hour > 0)
    {
      if (year > 0 || day > 0)
      {
        component = component.append(Component.text(" "));
      }
      component = component.append(ComponentUtil.translate("%s시간", hour));
    }
    if (min > 0)
    {
      if (year > 0 || day > 0 || hour > 0)
      {
        component = component.append(Component.text(" "));
      }
      component = component.append(ComponentUtil.translate("%s분", min));
    }
    if (sec > 0)
    {
      if (year > 0 || day > 0 || hour > 0 || min > 0)
      {
        component = component.append(Component.text(" "));
      }
      component = component.append(ComponentUtil.translate("%s초", displaySec));
    }
    return component;
  }

  @NotNull
  public static List<Component> convertHoverToItemLore(@NotNull Component component)
  {
    return convertHoverToItemLore(component, NamedTextColor.WHITE, true);
  }

  @NotNull
  public static List<Component> convertHoverToItemLore(@NotNull Component component, @NotNull TextColor defaultColor)
  {
    return convertHoverToItemLore(component, defaultColor, true);
  }

  @NotNull
  public static List<Component> convertHoverToItemLore(@NotNull Component component, boolean removeLineLore)
  {
    return convertHoverToItemLore(component, NamedTextColor.WHITE, removeLineLore);
  }

  @NotNull
  public static List<Component> convertHoverToItemLore(@NotNull Component component, @NotNull TextColor defaultColor, boolean removeLineLore)
  {
    List<Component> list = new ArrayList<>();
    List<Component> children = component.children();
    list.add(component.children(Collections.emptyList()));
    children.forEach(c -> list.addAll(convertHoverToItemLore(c, defaultColor, removeLineLore)));
    if (removeLineLore)
    {
      list.removeIf(c -> c.equals(Component.text("\n")));
    }
    for (int i = 0; i < list.size(); i++)
    {
      Component c = list.get(i);
      if (c.decoration(TextDecoration.ITALIC) == State.NOT_SET)
      {
        c = c.decoration(TextDecoration.ITALIC, State.FALSE);
      }
      if (c.color() == null)
      {
        c = c.color(defaultColor);
      }
      list.set(i, c);
    }
    return list;
  }

  public enum TimeFormatType
  {
    DEFAULT,
    FULL_SINGLE_DECIMAL,
    FULL_NO_DECIMALS,
    SHORT
  }
}






































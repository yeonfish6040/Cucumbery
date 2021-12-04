package com.jho5245.cucumbery.util.storage.component.util;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.ItemSerializer;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.component.ItemStackComponent;
import com.jho5245.cucumbery.util.storage.component.LocationComponent;
import com.jho5245.cucumbery.util.storage.component.util.sendercomponent.SenderComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.TranslatableKeyParser;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import de.tr7zw.changeme.nbtapi.NBTItem;
import io.papermc.paper.advancement.AdvancementDisplay;
import io.papermc.paper.advancement.AdvancementDisplay.Frame;
import net.kyori.adventure.text.*;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.translation.Translatable;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComponentUtil
{
  private static final JSONParser JSON_PARSER = new JSONParser();
  /**
   * 컴포넌트 변환 URL
   */
  private static final Pattern URL = Pattern.compile("^(?:(https?)://)?([-\\w_.]{2,}\\.[a-z]{2,4})(/\\S*)?$");
  private static final Pattern p = Pattern.compile("%s"), p2 = Pattern.compile("%[0-9]+\\$s");
  private static final Pattern yeet = Pattern.compile("(%s|%[0-9]+\\$s)([^가-힣ㄱ-ㅎA-Za-z0-9]+|)" +
          "(이\\(가\\)|\\(이\\)가|가\\(이\\)|\\(가\\)이|을\\(를\\)|\\(을\\)를|를\\(을\\)|\\(를\\)을|와\\(과\\)|\\(와\\)과|과\\(와\\)|\\(과\\)와|은\\(는\\)|\\(은\\)는|는\\(은\\)|\\(는\\)은|\\(으\\)로|으\\(로\\)|\\(이\\)라|이\\(라\\))");

  /**
   * 컴포넌트를 생성합니다.
   *
   * @param objects 문자열 또는 다른 컴포넌트
   * @return 컴포넌트
   */
  @NotNull
  @SuppressWarnings("all")
  public static Component create(@NotNull Object... objects)
  {
    Component component = Component.empty();
    boolean n2s = objects.length > 0 && !objects[0].equals(false);
    for (Object object : objects)
    {
      Component senderComponent = SenderComponentUtil.senderComponent(object);
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
          component = component.append(ComponentUtil.create(child));
        }
      }
      else if (object instanceof List<?> list)
      {
        for (Object c : list)
        {
          Component concat = ComponentUtil.create(c);
          component = component.append(concat);
        }
      }
      else if (object instanceof Component[] components)
      {
        for (Component c : components)
        {
          Component concat = (Component) c;
          component = component.append(ComponentUtil.create(concat));
        }
      }
      else if (object instanceof Material material)
      {
        ItemStack itemStack = ItemStackUtil.loredItemStack(material);
        Component concat = ItemNameUtil.itemName(itemStack, Constant.THE_COLOR);
        concat = concat.hoverEvent(itemStack.asHoverEvent());
        component = component.append(concat);
      }
      else if (object instanceof ItemStack itemStack)
      {
        itemStack = itemStack.clone();
        Material type = itemStack.getType();
        if (type.isItem() && type != Material.AIR)
        {
          NBTItem nbtItem = new NBTItem(itemStack);
          nbtItem.removeKey("BlockEntityTag");
          itemStack.setItemMeta(nbtItem.getItem().getItemMeta());
        }
        Component concat = ItemNameUtil.itemName(itemStack, Constant.THE_COLOR);
        if (concat instanceof TextComponent textComponent && textComponent.content().equals(""))
        {
          List<Component> children = new ArrayList<>(concat.children());
          for (int i = 0; i < children.size(); i++)
          {
            Component child = children.get(i).hoverEvent(itemStack.asHoverEvent());
            children.set(i, child);
          }
          concat = concat.children(children);
        }
        else
        {
          concat = concat.hoverEvent(itemStack.asHoverEvent());
        }
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
        concat = concat.clickEvent(ClickEvent.suggestCommand("/whatis " + world.getName()));
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
        hover = hover.append(ComponentUtil.createTranslate("유형 : %s", Component.translatable(environmentKey).color(Constant.THE_COLOR)));
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.createTranslate("ID : %s", Constant.THE_COLOR_HEX + world.getName()));
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.createTranslate("UUID : %s", Constant.THE_COLOR_HEX + world.getUID()));
        hover = hover.append(Component.text("\n"));
        Location spawnLocation = world.getSpawnLocation();
        hover = hover.append(ComponentUtil.createTranslate("스폰 포인트 : %s",
                ComponentUtil.createTranslate("&7%s, %s, %s", Constant.THE_COLOR_HEX + spawnLocation.getBlockX(), Constant.THE_COLOR_HEX + spawnLocation.getBlockY(), Constant.THE_COLOR_HEX + spawnLocation.getBlockX())
        ));
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.createTranslate("난이도 : %s", ComponentUtil.createTranslate(Constant.THE_COLOR_HEX + world.getDifficulty().translationKey())));
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.createTranslate("Y축 제한 범위 : %s", Constant.THE_COLOR_HEX + world.getMinHeight() + "~" + world.getMaxHeight()));
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.createTranslate("플레이어 수 : %s", ComponentUtil.createTranslate("%s명", Constant.THE_COLOR_HEX + playerCount)));
        concat = concat.hoverEvent(hover.asHoverEvent());
        component = component.append(concat);
      }
      else if (object instanceof Location location)
      {
        component = component.append(LocationComponent.locationComponent(location));
      }
      else if (object instanceof PotionEffect potionEffect)
      {
        PotionEffectType potionEffectType = potionEffect.getType();
        String effectKey = TranslatableKeyParser.getKey(potionEffectType);
        String id = effectKey.substring(17);
        int duration = potionEffect.getDuration(), amplifier = potionEffect.getAmplifier();
        boolean isInvincible = potionEffectType.equals(PotionEffectType.DAMAGE_RESISTANCE) && amplifier >= 9;
        int invincibleLevel = amplifier - 8;
        if (isInvincible)
        {
          effectKey = "무적";
        }
        boolean hasParticles = potionEffect.hasParticles(), hasIcon = potionEffect.hasIcon(), isAmbient = potionEffect.isAmbient();
        Component concat = Component.translatable(effectKey, Constant.THE_COLOR);
        Component hover = Component.translatable(effectKey);
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.createTranslate("지속 시간 : %s", Constant.THE_COLOR_HEX + Method.timeFormatMilli(duration * 50L)));
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.createTranslate("농도 레벨 : %s단계", isInvincible ? invincibleLevel : (amplifier + 1)));
        if (!hasParticles)
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.createTranslate("&a입자 숨김"));
        }
        if (!hasIcon)
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.createTranslate("&a우측 상단 아이콘 숨김"));
        }
        if (isAmbient)
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.createTranslate("&a우측 상단 효과 빛남"));
        }
        hover = hover.append(Component.text("\n"));
        hover = hover.append(Component.text("minecraft:" + id, NamedTextColor.DARK_GRAY));
        concat = concat.hoverEvent(hover).clickEvent(ClickEvent.suggestCommand(
                "/ceffect @s minecraft:" + id + " " + duration + " " + amplifier + " " + !hasParticles + " " + !hasIcon + " " + !isAmbient));
        component = component.append(concat);
      }
      else if (Cucumbery.using_NoteBlockAPI && object instanceof Song song)
      {
        String display = song.getPath().getName();
        display = display.substring(0, display.length() - 4);
        Component concat = Component.text(display).color(Constant.THE_COLOR).hoverEvent(Component.translatable(display));
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
          Component concat = createTranslate("chat.square_brackets", title.hoverEvent(title.append(Component.text("\n")).append(description))).color(title.color());
          NamespacedKey namespacedKey = advancement.getKey();
          String suggest = "/advancement grant @s only " + namespacedKey.namespace() + ":" + namespacedKey.value();
          concat = concat.clickEvent(ClickEvent.suggestCommand(suggest));
          component = component.append(concat);
        }
      }
      else if (object instanceof Translatable translatable)
      {
        component = component.append(Component.translatable(translatable.translationKey()));
      }
      else if (object instanceof Number number)
      {
        Component concat = Component.text(Constant.Sosu2.format(number), Constant.THE_COLOR);
        component = component.append(concat);
      }
      else if (!(object instanceof Boolean))
      {
        String string = object.toString();
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
          Component concat = ComponentUtil.createTranslate(string, split);
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
          Player player = SelectorUtil.getPlayer(null, string.substring(7), false);
          if (player != null)
          {
            concat = create(player);
          }
          component = component.append(concat);
        }
        else if (string.startsWith("players:"))
        {
          Component concat = Component.empty();
          List<Player> players = SelectorUtil.getPlayers(null, string.substring(8), false);
          if (players != null)
          {
            concat = create(players);
          }
          component = component.append(concat);
        }
        else if (string.startsWith("entity:"))
        {
          Component concat = Component.empty();
          Entity entity = SelectorUtil.getEntity(null, string.substring(7), false);
          if (entity != null)
          {
            concat = create(entity);
          }
          component = component.append(concat);
        }
        else if (string.startsWith("entities:"))
        {
          Component concat = Component.empty();
          List<Entity> entities = SelectorUtil.getEntities(null, string.substring(9), false);
          if (entities != null)
          {
            concat = create(entities);
          }
          component = component.append(concat);
        }
        else if (string.startsWith("offline_player:"))
        {
          Component concat = Component.empty();
          OfflinePlayer offlinePlayer = SelectorUtil.getOfflinePlayer(null, string.substring("offline_player:".length()), false);
          if (offlinePlayer != null)
          {
            concat = create(offlinePlayer);
          }
          component = component.append(concat);
        }
        else if (string.startsWith("item:"))
        {
          ItemStack itemStack = ItemSerializer.deserialize(string.substring(5));
          if (!ItemStackUtil.itemExists(itemStack))
          {
            itemStack = new ItemStack(Material.STONE);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.displayName(ComponentUtil.createTranslate("&c잘못된 아이템"));
            itemStack.setItemMeta(itemMeta);
          }
          else
          {
            ItemLore.setItemLore(itemStack);
          }
          component = component.append(create(itemStack));
        }
        else if (string.startsWith("items:"))
        {
          ItemStack itemStack = ItemSerializer.deserialize(string.substring(5));
          if (!ItemStackUtil.itemExists(itemStack))
          {
            itemStack = new ItemStack(Material.STONE);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.displayName(ComponentUtil.createTranslate("&c잘못된 아이템"));
            itemStack.setItemMeta(itemMeta);
          }
          else
          {
            ItemLore.setItemLore(itemStack);
          }
          component = component.append(ItemStackComponent.itemStackComponent(itemStack));
        }
        else if (string.startsWith("world:"))
        {
          World world = Bukkit.getWorld(string.substring(6));
          if (world != null)
          {
            component = component.append(create(world));
          }
          else
          {
            component = component.append(createTranslate("&c알 수 없는 월드입니다. (%s)", string.substring(6)));
          }
        }
        else
        {
          try
          {
            JSON_PARSER.parse(string);
            Component concat = GsonComponentSerializer.gson().deserialize(string);
            component = component.append(ComponentUtil.create(concat));
          }
          catch (Error | Exception e)
          {
            Component concat = ComponentUtil.create2(string, n2s);
            component = component.append(ComponentUtil.create(concat));
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
    if (component instanceof TextComponent && ((TextComponent) component).content().equals(""))
    {
      List<Component> children2 = component.children();
      if (children2.size() == 1)
      {
        return children2.get(0);
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
  public static Component create2(String value)
  {
    return ComponentUtil.create2(value, true);
  }

  /**
   * 텍스트 컴포넌트를 생성합니다.
   *
   * @param value 컴포넌트의 문자열 값
   * @param n2s   색깔 코드 포맷 여부
   * @return 텍스트 컴포넌트
   */
  public static Component create2(String value, boolean n2s)
  {
    List<TextComponent> components = ComponentUtil.fromLegacyText(value, n2s);
    TextComponent component = Component.empty();
    for (TextComponent textComponent : components)
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
  public static TranslatableComponent createTranslate(String key)
  {
    return ComponentUtil.createTranslate(key, true);
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
    String[] split = serial.split("이\\(가\\)|\\(이\\)가|가\\(이\\)|\\(가\\)이|을\\(를\\)|\\(을\\)를|를\\(을\\)|\\(를\\)을|와\\(과\\)|\\(와\\)과|과\\(와\\)|\\(과\\)와|은\\(는\\)|\\(은\\)는|는\\(은\\)|\\(는\\)은|\\(으\\)로|으\\(로\\)|\\(이\\)라|이\\(라\\)");
    int loop = 0;
    while (matcher.find())
    {
      try
      {
        String a3 = matcher.group(3);
        String replacer = MessageUtil.getFinalConsonant(split[loop], MessageUtil.ConsonantType.valueOf(a3.replace("(", "").replace(")", "")));
        if (!a3.equals(replacer))
        {
          key = key.replaceFirst(a3.replace("(", "\\(").replace(")", "\\)"), replacer);
        }
        loop++;
      }
      catch (Exception ignored)
      {
      }
    }
    return key;
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
  public static TranslatableComponent createTranslate(@NotNull String key, @NotNull Object... args)
  {
    boolean n2s = args.length > 0 && !args[0].equals(false);
    TranslatableComponent component = ComponentUtil.fromLegacyTextTranslate(key, n2s);
    List<Component> componentArgs = new ArrayList<>();
    for (Object obj : args)
    {
      Component senderComponent = SenderComponentUtil.senderComponent(obj);
      if (!senderComponent.equals(Component.empty()))
      {
        if (senderComponent.decoration(TextDecoration.ITALIC) == TextDecoration.State.FALSE)
        {
          senderComponent = senderComponent.decoration(TextDecoration.ITALIC, TextDecoration.State.NOT_SET);
        }
        componentArgs.add(senderComponent);
        continue;
      }
      if (obj instanceof List<?> list)
      {
        for (Object o : list)
        {
          Component arg = ComponentUtil.create(n2s, o);
          if (arg.decoration(TextDecoration.ITALIC) == TextDecoration.State.FALSE)
          {
            arg = arg.decoration(TextDecoration.ITALIC, TextDecoration.State.NOT_SET);
          }
          componentArgs.add(arg);
        }
      }
      if (obj instanceof Object[] array)
      {
        for (Object value : array)
        {
          componentArgs.add(ComponentUtil.create(value));
        }
      }
      if (!(obj instanceof Boolean))
      {
        Component arg = ComponentUtil.create(n2s, obj);
        if (arg.decoration(TextDecoration.ITALIC) == TextDecoration.State.FALSE)
        {
          arg = arg.decoration(TextDecoration.ITALIC, TextDecoration.State.NOT_SET);
        }
        componentArgs.add(arg);
      }
    }
    component = component.args(componentArgs);
    component = yeet(component.key(), component);
    return component;
  }

  @SuppressWarnings("unused")
  private static List<TextComponent> fromLegacyText(@NotNull String message)
  {
    return fromLegacyText(message, true);
  }

  /**
   * 구버전의 텍스트를 컴포넌트로 변환합니다.
   *
   * @param message 구버전 텍스트
   * @return 컴포넌트
   */
  private static List<TextComponent> fromLegacyText(@NotNull String message, boolean n2s)
  {
    if (n2s)
    {
      message = MessageUtil.n2s(message);
    }
    List<TextComponent> components = new ArrayList<>();
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
            component = component.decoration(TextDecoration.BOLD, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.ITALIC, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.UNDERLINED, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.STRIKETHROUGH, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.OBFUSCATED, TextDecoration.State.NOT_SET);
          }
          else
          {
            component = Component.empty();
            java.awt.Color color = format.getColor();
            component = component.color(TextColor.color(color.getRed(), color.getGreen(), color.getBlue()));
            component = component.decoration(TextDecoration.BOLD, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.ITALIC, false);
            component = component.decoration(TextDecoration.UNDERLINED, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.STRIKETHROUGH, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.OBFUSCATED, TextDecoration.State.NOT_SET);
          }
        }
        else
        {
          if (c == 'p' || c == 'q')
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
              case 'q' -> {
                component = component.decoration(TextDecoration.BOLD, component.decoration(TextDecoration.BOLD) == TextDecoration.State.TRUE ? TextDecoration.State.FALSE : TextDecoration.State.NOT_SET);
                component = component.decoration(TextDecoration.ITALIC, component.decoration(TextDecoration.ITALIC) == TextDecoration.State.TRUE ? TextDecoration.State.FALSE : TextDecoration.State.NOT_SET);
                component = component.decoration(TextDecoration.UNDERLINED, component.decoration(TextDecoration.UNDERLINED) == TextDecoration.State.TRUE ? TextDecoration.State.FALSE : TextDecoration.State.NOT_SET);
                component = component.decoration(TextDecoration.STRIKETHROUGH, component.decoration(TextDecoration.STRIKETHROUGH) == TextDecoration.State.TRUE ? TextDecoration.State.FALSE : TextDecoration.State.NOT_SET);
                component = component.decoration(TextDecoration.OBFUSCATED, component.decoration(TextDecoration.OBFUSCATED) == TextDecoration.State.TRUE ? TextDecoration.State.FALSE : TextDecoration.State.NOT_SET);
              }
            }
          }
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
          component = component.hoverEvent(HoverEvent.showText(ComponentUtil.createTranslate("클릭하여 %s 주소로 연결합니다.", Component.text(url).color(NamedTextColor.YELLOW))));
          components.add(component.decoration(TextDecoration.UNDERLINED, TextDecoration.State.TRUE));
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

  @SuppressWarnings("all")
  private static TranslatableComponent fromLegacyTextTranslate(@NotNull String message, boolean n2s)
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
        ChatColor format;
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
          if (builder.length() > 0)
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
            component = component.decoration(TextDecoration.BOLD, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.ITALIC, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.UNDERLINED, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.STRIKETHROUGH, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.OBFUSCATED, TextDecoration.State.NOT_SET);
          }
          else
          {
            component = Component.translatable("");
            java.awt.Color color = format.getColor();
            component = component.color(TextColor.color(color.getRed(), color.getGreen(), color.getBlue()));
            component = component.decoration(TextDecoration.BOLD, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.ITALIC, false);
            component = component.decoration(TextDecoration.UNDERLINED, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.STRIKETHROUGH, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.OBFUSCATED, TextDecoration.State.NOT_SET);
          }
        }
        else
        {
          if (c == 'p' || c == 'q')
          {
            if (builder.length() > 0)
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
              case 'q' -> {
                component = component.decoration(TextDecoration.BOLD, component.decoration(TextDecoration.BOLD) == TextDecoration.State.TRUE ? TextDecoration.State.FALSE : TextDecoration.State.NOT_SET);
                component = component.decoration(TextDecoration.ITALIC, component.decoration(TextDecoration.ITALIC) == TextDecoration.State.TRUE ? TextDecoration.State.FALSE : TextDecoration.State.NOT_SET);
                component = component.decoration(TextDecoration.UNDERLINED, component.decoration(TextDecoration.UNDERLINED) == TextDecoration.State.TRUE ? TextDecoration.State.FALSE : TextDecoration.State.NOT_SET);
                component = component.decoration(TextDecoration.STRIKETHROUGH, component.decoration(TextDecoration.STRIKETHROUGH) == TextDecoration.State.TRUE ? TextDecoration.State.FALSE : TextDecoration.State.NOT_SET);
                component = component.decoration(TextDecoration.OBFUSCATED, component.decoration(TextDecoration.OBFUSCATED) == TextDecoration.State.TRUE ? TextDecoration.State.FALSE : TextDecoration.State.NOT_SET);
              }
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
    boolean italic = component.decoration(TextDecoration.ITALIC) == TextDecoration.State.TRUE;
    boolean bold = component.decoration(TextDecoration.BOLD) == TextDecoration.State.TRUE;
    boolean underlined = component.decoration(TextDecoration.UNDERLINED) == TextDecoration.State.TRUE;
    boolean strikethrough = component.decoration(TextDecoration.STRIKETHROUGH) == TextDecoration.State.TRUE;
    boolean obfuscated = component.decoration(TextDecoration.OBFUSCATED) == TextDecoration.State.TRUE;
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
            args.add(Component.translatable(entity.getType().translationKey()));
          }
        }
        key = new StringBuilder(key.substring(0, key.length() - 2));
        Component translate = ComponentUtil.createTranslate(key.toString(), args);
        pattern.append(ComponentUtil.serialize(translate));
      }
      catch (Exception ignored)
      {
      }
      builder.append(pattern);
    }
    if (component instanceof KeybindComponent keybindComponent)
    {
      builder.append(ComponentUtil.serialize(Component.translatable(keybindComponent.keybind())));
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
            && component.decoration(TextDecoration.ITALIC) == TextDecoration.State.NOT_SET
            && component.decoration(TextDecoration.BOLD) == TextDecoration.State.NOT_SET
            && component.decoration(TextDecoration.STRIKETHROUGH) == TextDecoration.State.NOT_SET
            && component.decoration(TextDecoration.OBFUSCATED) == TextDecoration.State.NOT_SET
            && component.decoration(TextDecoration.UNDERLINED) == TextDecoration.State.NOT_SET;
  }
}

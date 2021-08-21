package com.jho5245.cucumbery.util.storage;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import io.papermc.paper.inventory.ItemRarity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.*;
import org.bukkit.entity.Damageable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.parser.JSONParser;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComponentUtil
{
  /**
   * 아이템의 이름을 컴포넌트 형태로 반환합니다.
   *
   * @param type 아이템의 종류
   * @return 컴포넌트 형태의 아이템 이름
   */
  public static Component itemName(@NotNull Material type)
  {
    return itemName(new ItemStack(type));
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
   * @param itemStack 아이템
   * @param defaultColor 색상이 없을 경우 적용할 기본 값
   * @return 컴포넌트 형태의 아이템 이름
   */
  @NotNull
  public static Component itemName(@NotNull ItemStack itemStack, @Nullable TextColor defaultColor)
  {
    Material material = itemStack.getType();
    ItemMeta itemMeta = itemStack.getItemMeta();

    Component component;

    // 아이템의 이름이 있을 경우 이름을 가져온다
    if (itemMeta != null && itemMeta.hasDisplayName())
    {
      component = itemMeta.displayName();

      if (component == null) // 이런 경우는 없다.
      {
        component = Component.empty();
      }

      // 아이템의 기울임꼴 값이 없는 경우 기본적으로 아이템의 이름이 기울어져 있으므로 기울임 효과를 추가한다.
      if (component.decorations().get(TextDecoration.ITALIC) == TextDecoration.State.NOT_SET)
      {
        component = component.decoration(TextDecoration.ITALIC, TextDecoration.State.TRUE);
      }
    }

    // 아이템의 이름이 없을 경우 번역된 아이템 이름을 가져온다.
    else
    {
      String id = material.getKey().value();
      component = Component.translatable((material.isBlock() ? "block" : "item") + ".minecraft." + id);
      // 특정 아이템은 다른 번역 규칙을 가지고 있으므로 해당 규칙을 적용한다.
      switch (material)
      {
        case PLAYER_HEAD, PLAYER_WALL_HEAD -> {
          String playerName = PlayerHeadInfo.getPlayerHeadInfo(itemStack, PlayerHeadInfo.PlayerHeadInfoType.NAME);
          if (playerName != null)
          {
            component = Component.translatable("block.minecraft.player_head.named").args(Component.text(playerName));
          }
        }
        case POTION, SPLASH_POTION, LINGERING_POTION, TIPPED_ARROW -> {
          PotionMeta potionMeta = (PotionMeta) itemMeta;
          if (potionMeta !=null)
          {
            String potionId = potionMeta.getBasePotionData().getType().toString().toLowerCase();
            switch (potionMeta.getBasePotionData().getType())
            {
              case AWKWARD, FIRE_RESISTANCE, INVISIBILITY, LUCK, MUNDANE, NIGHT_VISION, POISON, SLOW_FALLING, SLOWNESS, STRENGTH, THICK, TURTLE_MASTER, WATER, WATER_BREATHING, WEAKNESS -> component = Component.translatable("item.minecraft." + id + ".effect." + potionId);
              case UNCRAFTABLE -> component = Component.translatable("item.minecraft." + id + ".effect.empty");
              case JUMP -> component = Component.translatable("item.minecraft." + id + ".effect.leaping");
              case REGEN -> component = Component.translatable("item.minecraft." + id + ".effect.regeneration");
              case SPEED -> component = Component.translatable("item.minecraft." + id + ".effect.swiftness");
              case INSTANT_HEAL -> component = Component.translatable("item.minecraft." + id + ".effect.healing");
              case INSTANT_DAMAGE -> component = Component.translatable("item.minecraft." + id + ".effect.harming");
            }
          }
        }
        case WRITTEN_BOOK -> {
          BookMeta bookMeta = (BookMeta) itemMeta;
          if (bookMeta != null && bookMeta.hasTitle())
          {
            component = bookMeta.title();
            if (component == null || ComponentUtil.serialize(component).length() == 0)
            {
            component = Component.translatable("item.minecraft." + id);
            }
          }
        }
        case COMPASS -> {
          CompassMeta compassMeta = (CompassMeta) itemMeta;
          if (compassMeta != null && compassMeta.hasLodestone())
          {
            component = Component.translatable("item.minecraft.lodestone_compass");
          }
        }
        case SHIELD -> {
          BlockStateMeta blockStateMeta = (BlockStateMeta) itemMeta;
          if (blockStateMeta != null && blockStateMeta.hasBlockState())
          {
            BlockState blockState = blockStateMeta.getBlockState();
            Banner bannerState = (Banner) blockState;
            DyeColor baseColor = bannerState.getBaseColor();
            component = Component.translatable("item.minecraft." + id + "." + baseColor.toString().toLowerCase());
          }
        }
      }
      // 이름이 없는 아이템은 기울임 효과가 없으므로 반드시 false로 한다.
      component = component.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    // 아이템의 등급이 있고 아이템 이름에 색깔이 없을 경우와 기본 아이템 색깔이 흰색이 아닐 경우 색깔을 추가한다.
    if (material.isItem() && material != Material.AIR)
    {
      ItemRarity itemRarity = material.getItemRarity();
      TextColor textColor = component.color();
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
    if (component.color() == null && defaultColor != null)
    {
      component = component.color(defaultColor);
    }
    return component;
  }

  /**
   * 문자열을 컴포넌트로 변환합니다.
   * @param string 컴포넌트로 변환할 문자열
   * @return 컴포넌트
   */
  @NotNull
  public static Component fromString(@NotNull String string)
  {
    try
    {
      ItemStack item = new ItemStack(Material.NAME_TAG);
      NBTItem nbtItem = new NBTItem(item);
      NBTCompound display = nbtItem.addCompound("display");
      display.setString("Name", string);
      if (item.equals(nbtItem.getItem()))
        throw new Exception();
      return ComponentUtil.itemName(nbtItem.getItem());
    }
    catch (Exception e)
    {
      return Component.empty();
    }
  }

  /**
   * 콘솔, 명령 블록, 개체, 플레이어, 접속 중이지 않은 플레이어의 정보를 표시할 컴포넌트를 반환합니다.
   *
   * @param object 정보를 가져올 오브젝트
   * @return 해당 오브젝트의 정보를 가진 컴포넌트
   */
  @NotNull
  public static Component senderComponent(@NotNull Object object)
  {
    return senderComponent(object, TextColor.color(255, 204, 0));
  }

  /**
   * 콘솔, 명령 블록, 개체, 플레이어, 접속 중이지 않은 플레이어의 정보를 표시할 컴포넌트를 반환합니다.
   *
   * @param object       정보를 가져올 오브젝트
   * @param defaultColor 해당 개체의 컴포넌트에 색상이 없을 때 기본 색상
   * @return 해당 오브젝트의 정보를 가진 컴포넌트
   */
  @NotNull
  public static Component senderComponent(@NotNull Object object, @Nullable TextColor defaultColor)
  {
    if (object instanceof ConsoleCommandSender)
    {
      return ComponentUtil.create("&d" + Bukkit.getServer().getName()).
              hoverEvent(HoverEvent.showText(
                      ComponentUtil.createTranslate("&e버전 : %s", "&6" + Bukkit.getServer().getVersion())
                              .append(ComponentUtil.create("\n"))
                              .append(ComponentUtil.createTranslate("&e접속 인원 : %s", "&6" + Bukkit.getServer().getOnlinePlayers().size()))
              ));
    }
    else if (object instanceof BlockCommandSender blockCommandSender)
    {
      Block block = blockCommandSender.getBlock();
      Location location = block.getLocation();
      String worldName = location.getWorld().getName();
      int x = location.getBlockX(), y = location.getBlockY(), z = location.getBlockZ();
      String name = blockCommandSender.getName();
      return ComponentUtil.itemName(block.getType()).
              hoverEvent(HoverEvent.showText(
                      ComponentUtil.createTranslate("&e이름 : %s", "&6" + name)
                              .append(ComponentUtil.create("\n"))
                              .append(ComponentUtil.createTranslate("&e좌표 : %s",
                                      ComponentUtil.createTranslate("&8%s, %s, %s, %s", "&6" + worldName, "&6" + x, "&6" + y, "&6" + z)))
              ));
    }
    else if (object instanceof Entity entity)
    {
      Component nameComponent;
      if (entity instanceof Player player)
      {
        nameComponent = player.displayName();
      }
      else
      {
        nameComponent = entity.customName();
        if (nameComponent == null)
        {
          nameComponent = ComponentUtil.createTranslate("entity.minecraft." + entity.getType().getKey().value());
        }
      }
      if (defaultColor != null && nameComponent.color() == null)
      {
        nameComponent = nameComponent.color(defaultColor);
      }

      UUID uuid = entity.getUniqueId();
      Location location = entity.getLocation();
      int x = location.getBlockX(), y = location.getBlockY(), z = location.getBlockZ();

      Component hover = ComponentUtil.createTranslate("유형 : %s",
                      ComponentUtil.createTranslate("&6entity.minecraft." + entity.getType().getKey().value())
              )
              .append(Component.text("\n"))
              .append(ComponentUtil.createTranslate("UUID : %s", "&6" + uuid))
              .append(Component.text("\n"))
              .append(ComponentUtil.createTranslate("좌표 : %s",
                      ComponentUtil.createTranslate("&8%s, %s, %s, %s", location.getWorld(), "&6" + x, "&6" + y, "&6" + z))
              );
      String click = "/minecraft:tp " + uuid;
      if (entity instanceof Damageable damageable && entity instanceof Attributable attributable)
      {
        double health = damageable.getHealth();
        AttributeInstance maxHealthInstance = attributable.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealthInstance != null)
        {
          String healthDisplay = "&c" + Constant.Sosu2.format(health);
          String maxHealthDisplay = "&c" + Constant.Sosu2.format(maxHealthInstance.getValue());
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.createTranslate("HP : %s", ComponentUtil.createTranslate("&7%s / %s", healthDisplay, maxHealthDisplay)));
        }
      }
      if (entity instanceof Villager villager)
      {
        Villager.Profession profession = villager.getProfession();
        String key = "&eentity.minecraft.villager." + profession.toString().toLowerCase();
        if (profession == Villager.Profession.NONE)
        {
          key = "&c없음";
        }
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.createTranslate("직업 : %s", ComponentUtil.createTranslate(key)));
      }
      if (entity instanceof Tameable tameable && tameable.isTamed())
      {
        AnimalTamer animalTamer = tameable.getOwner();
        if (animalTamer != null)
        {
          hover = hover.append(Component.text("\n"));
          String tamerName = animalTamer.getName();
          UUID tamerUUID = animalTamer.getUniqueId();
          if (tamerName != null)
          {
            Player tamerPlayer = Method.getPlayer(null, tamerName, false);
            Component component = ComponentUtil.createTranslate("주인 : %s", tamerPlayer != null ? tamerPlayer : tamerName);
            hover = hover.append(component);
          }
          Component component = ComponentUtil.createTranslate("주인 : %s", animalTamer);
          hover = hover.append(component);
        }
      }
      if (entity instanceof Player player)
      {
        String name = player.getName();
        GameMode gameMode = player.getGameMode();
        hover = hover
                .append(Component.text("\n"))
                .append(ComponentUtil.createTranslate("ID : %s", "&6" + name))
                .append(Component.text("\n"))
                .append(ComponentUtil.createTranslate("게임 모드 : %s", ComponentUtil.createTranslate("&6gameMode." + gameMode.toString().toLowerCase())))
                .append(Component.text("\n"))
                .append(ComponentUtil.create(Constant.ITEM_LORE_SEPARATOR + Constant.ITEM_LORE_SEPARATOR))
                .append(Component.text("\n"))
                .append(ComponentUtil.createTranslate("클릭하여 이 플레이어의 소셜 메뉴 열기 : %s", "&e/socialmenu " + name))
        ;

        click = "/socialmenu " + name;
      }
      nameComponent = nameComponent.hoverEvent(HoverEvent.showText(hover));
      if (object instanceof Player)
      nameComponent = nameComponent.clickEvent(ClickEvent.runCommand(click));
      else
        nameComponent = nameComponent.clickEvent(ClickEvent.suggestCommand(click));
      return nameComponent;
    }
    else if (object instanceof OfflinePlayer offlinePlayer)
    {
      UUID uuid = offlinePlayer.getUniqueId();
      String displayName = Method.getDisplayName(offlinePlayer);
      return ComponentUtil.create(displayName).hoverEvent(HoverEvent.showText(ComponentUtil.createTranslate("UUID : %s", "&6" + uuid)));
    }
    return Component.empty();
  }

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
    boolean n2s = !objects[0].equals(false);
    for (Object object : objects)
    {
      // 개체의 경우 추가
      Component senderComponent = ComponentUtil.senderComponent(object);
      if (!senderComponent.equals(Component.empty()))
      {
       component = component.append(senderComponent);
        continue;
      }
      // 컴포넌트의 경우 추가
      if (object instanceof Component)
      {
        Component concat = (Component) object;
        if (concat.equals(Component.empty()))
          continue;
        component = component.append(concat.children(Collections.emptyList()));
        for (Component child : concat.children())
        {
          component = component.append(ComponentUtil.create(child));
        }
      }
      else if (object instanceof ArrayList components)
      {
        for (Object c : components)
        {
          if (c instanceof Component)
          {
            Component concat = (Component) c;
            component = component.append(ComponentUtil.create(concat));
          }
        }
      }
      // 컴포넌트 배열의 경우 추가
      else if (object instanceof Component[] components)
      {
        for (Component c : components)
        {
          Component concat = (Component) c;
          component = component.append(ComponentUtil.create(concat));
        }
      }
      // 아이템의 경우 추가
      else if (object instanceof Material material)
      {
        ItemStack itemStack = new ItemStack(material);
        Component concat = ComponentUtil.itemName(itemStack, TextColor.color(255, 204, 0));
        concat = concat.hoverEvent(itemStack.asHoverEvent());
        component = component.append(concat);
      }
      else if (object instanceof ItemStack itemStack)
      {
        Component concat = ComponentUtil.itemName(itemStack, TextColor.color(255, 204, 0));
        concat = concat.hoverEvent(itemStack.asHoverEvent());
        component = component.append(concat);
      }
      // Prefix일 경우 추가
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
        Component hover = ComponentUtil.createTranslate("플레이어 수 : %s", playerCount);
        concat = concat.hoverEvent(hover.asHoverEvent());
        component = component.append(concat);
      }
      // boolean이 아닐 경우 추가
      else if (objects.length == 1 || !(object instanceof Boolean))
      {
        String string = object.toString();
        if (string.startsWith("translate:"))
        {
          string = string.substring(10);
          String[] split = string.split(";;");
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
        else
        try
        {
          JSON_PARSER.parse(string);
          Component concat = GsonComponentSerializer.gson().deserialize(string);
          component = component.append(ComponentUtil.create(concat));
        }
        catch (Exception e)
        {
          Component concat;
          if (KOREAN.matcher(string).find())
            concat = ComponentUtil.createTranslate(string, n2s);
          else
            concat = ComponentUtil.create2(string, n2s);
            component = component.append(ComponentUtil.create(concat));
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
                component = component.append(child);
              }
            }
          }
    }
    List<Component> children = new ArrayList<>();
    for (Component child : component.children())
      if (!child.equals(Component.empty()))
        children.add(child);
     component = component.children(children);
     if (component instanceof TextComponent && ((TextComponent) component).content().equals(""))
     {
       List<Component> children2 = component.children();
       if (children2.size() == 1)
       {
         return  children2.get(0);
       }
     }
    return component;
  }

  private static final JSONParser JSON_PARSER = new JSONParser();

  private static final Pattern KOREAN = Pattern.compile("[ㄱ-ㅎㅏ-ㅣ가-힣]");

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
  //  return GsonComponentSerializer.gson().deserialize(ItemEditor.create(n2s ? MessageUtil.n2s(value) : value));
    List<TextComponent> components = ComponentUtil.fromLegacyText(n2s ? MessageUtil.n2s(value) : value);
    TextComponent component = Component.empty();
    for (TextComponent textComponent : components)
    {
//      if (component == null)
//      {
//        component = components.get(0);
//      }
//      else
//      {
        component = component.append(textComponent);
//      }
    }
    return component;
  }

  /**
   * 번역 컴포넌트를 생성합니다.
   *
   * @param key 컴포넌트의 키 값
   * @return 번역 컴포넌트
   */
  public static Component createTranslate(String key)
  {
    return ComponentUtil.createTranslate(key, true);
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
  public static Component createTranslate(@NotNull String key, @NotNull Object... args)
  {
    boolean n2s = args.length > 0 && !args[0].equals(false);
    TranslatableComponent component = ComponentUtil.fromLegacyTextTranslate(n2s ? MessageUtil.n2s(key) : key);
    List<Component> componentArgs = new ArrayList<>();
    for (Object arg : args)
    {
      if (arg instanceof ArrayList)
      {
        List<Component> components = (List<Component>) arg;
        componentArgs.addAll(components);
      }
      else if (!(arg instanceof Boolean))
      componentArgs.add(ComponentUtil.create(arg));
    }
    component = component.args(componentArgs);
    return ComponentUtil.create(component);
  }

  /**
   * 컴포넌트 변환 URL
   */
  private static final Pattern URL = Pattern.compile("^(?:(https?)://)?([-\\w_.]{2,}\\.[a-z]{2,4})(/\\S*)?$");

  /**
   * 구버전의 텍스트를 컴포넌트로 변환합니다.
   *
   * @param message 구버전 텍스트
   * @return 컴포넌트
   */
  private static List<TextComponent> fromLegacyText(@NotNull String message)
  {
    int size = 0;
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
            Color color = format.getColor();
            component = component.color(TextColor.color(color.getRed(), color.getGreen(), color.getBlue()));
            component = component.decoration(TextDecoration.BOLD, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.ITALIC, false);
            component = component.decoration(TextDecoration.UNDERLINED, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.STRIKETHROUGH, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.OBFUSCATED, TextDecoration.State.NOT_SET);
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
          if (builder.length() > 0)
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
          components.add(component);
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

  private static TranslatableComponent fromLegacyTextTranslate(@NotNull String message)
  {
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
            component = component.color(TextColor.color(255, 255, 255));
            component = component.decoration(TextDecoration.BOLD, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.ITALIC, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.UNDERLINED, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.STRIKETHROUGH, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.OBFUSCATED, TextDecoration.State.NOT_SET);
          }
          else
          {
            component = Component.translatable("");
            Color color = format.getColor();
            component = component.color(TextColor.color(color.getRed(), color.getGreen(), color.getBlue()));
            component = component.decoration(TextDecoration.BOLD, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.ITALIC, false);
            component = component.decoration(TextDecoration.UNDERLINED, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.STRIKETHROUGH, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.OBFUSCATED, TextDecoration.State.NOT_SET);
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

  private static String getColor(Component component)
  {
    StringBuilder stringBuilder = new StringBuilder();
    TextColor textColor = component.color();
    if (textColor != null)
    {
      stringBuilder.append(textColor.asHexString()).append(";");
    }
    for (TextDecoration textDecoration : TextDecoration.values())
    {
      if (component.decorations().get(textDecoration) == TextDecoration.State.TRUE)
      {
        switch (textDecoration)
        {
          case OBFUSCATED -> stringBuilder.append("&k");
          case BOLD -> stringBuilder.append("&l");
          case STRIKETHROUGH -> stringBuilder.append("&m");
          case UNDERLINED -> stringBuilder.append("&n");
          case ITALIC -> stringBuilder.append("&o");
        }
      }
    }
    return stringBuilder.toString();
  }


  @NotNull
  public static String serializeAsJson(@NotNull Component component)
  {
    return GsonComponentSerializer.gson().serialize(component);
  }

  @NotNull
  public static String serialize(@NotNull Component component)
  {
    return LegacyComponentSerializer.legacySection().serialize(component);
  }

  public static boolean isPlainComponent(@NotNull Component component)
  {
    if (component instanceof TextComponent textComponent)
    {
      if (textComponent.content().equals(""))
        return true;
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

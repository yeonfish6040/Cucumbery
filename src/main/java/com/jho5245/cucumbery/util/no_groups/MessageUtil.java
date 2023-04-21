package com.jho5245.cucumbery.util.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.itemlore.ItemLoreView;
import com.jho5245.cucumbery.util.josautil.KoreanUtils;
import com.jho5245.cucumbery.util.storage.component.ItemStackComponent;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;
import io.papermc.paper.advancement.AdvancementDisplay.Frame;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEvent.Action;
import net.kyori.adventure.text.event.HoverEvent.ShowItem;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextDecoration.State;
import net.kyori.adventure.title.Title.Times;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.*;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class MessageUtil
{
  private static final Pattern PATTERN_N2S_RGB = Pattern.compile("(([RGBrgb]){1,3})(([0-9]){1,3})(,(([0-9]){1,3}))?(,(([0-9]){1,3}))?;");

  @NotNull
  public static String[] wrapWithQuote(@NotNull String[] args)
  {
    return wrapWithQuote(false, args);
  }

  @NotNull
  public static String[] wrapWithQuote(boolean forTabComplete, @NotNull String[] args)
  {
    String input = listToString(" ", args);
    input = forTabComplete ? PlaceHolderUtil.evalString(PlaceHolderUtil.placeholder(Bukkit.getConsoleSender(), input, null)) : input;
    if (input.equals("'") || input.equals("\""))
    {
      return new String[]{(ComponentUtil.serialize(ComponentUtil.translate("command.expected.separator")))};
    }
    List<String> a = new ArrayList<>();
    StringBuilder builder = new StringBuilder();
    boolean isInQuote = false;
    boolean isInDoubleQuote = false;
    for (int i = 0; i < input.length(); i++)
    {
      char c = input.charAt(i);
      final boolean b = i + 1 < input.length() && input.charAt(i + 1) != ' ';
      if (c == '\"' && !isInQuote)
      {
        if (isInDoubleQuote)
        {
          if (i + 1 < input.length() && input.charAt(i + 1) == '\"')
          {
            builder.append("\"");
            i++;
            continue;
          }
          if (b)
          {
            return new String[]{(ComponentUtil.serialize(ComponentUtil.translate("command.expected.separator")))};
          }
          if (!builder.isEmpty())
          {
            a.add(builder.toString());
            builder = new StringBuilder();
            isInDoubleQuote = false;
          }
        }
        else
        {
          isInDoubleQuote = true;
        }
      }
      else if (c == '\'' && !isInDoubleQuote)
      {
        if (isInQuote)
        {
          if (i + 1 < input.length() && input.charAt(i + 1) == '\'')
          {
            builder.append("'");
            i++;
            continue;
          }
          if (b)
          {
            return new String[]{(ComponentUtil.serialize(ComponentUtil.translate("command.expected.separator")))};
          }
          if (!builder.isEmpty())
          {
            a.add(builder.toString());
            builder = new StringBuilder();
            isInQuote = false;
          }
        }
        else
        {
          isInQuote = true;
        }
      }
      else if (c == ' ' && !isInQuote && !isInDoubleQuote)
      {
        if (!builder.isEmpty() && !builder.toString().equals(" "))
        {
          a.add(builder.toString());
        }
        builder = new StringBuilder();
      }
      else
      {
        builder.append(c);
      }
    }
    if (!builder.isEmpty())
    {
      if (isInDoubleQuote || isInQuote)
      {
        return new String[]{(ComponentUtil.serialize(ComponentUtil.translate("parsing.quote.expected.end")))};
      }
      a.add(builder.toString());
    }
    if ((forTabComplete && a.isEmpty()) || input.endsWith(" "))
    {
      a.add("");
    }
    return Method.listToArray(a);
  }

  @Deprecated
  @NotNull
  public static String[] wrapWithQuote2(boolean forTabComplete, @NotNull String[] args)
  {
    String input = listToString(" ", args);
    input = forTabComplete ? PlaceHolderUtil.evalString(PlaceHolderUtil.placeholder(Bukkit.getConsoleSender(), input, null)) : input;
    if (input.equals("'") || input.equals("\""))
    {
      return new String[]{"command.expected.separator"};
    }
    List<String> a = new ArrayList<>();
    StringBuilder builder = new StringBuilder();
    boolean isInQuote = false;
    boolean isInDoubleQuote = false;
    for (int i = 0; i < input.length(); i++)
    {
      char c = input.charAt(i);
      final boolean b = i + 1 < input.length() && input.charAt(i + 1) != ' ';
      if (c == '\"' && !isInQuote)
      {
        if (isInDoubleQuote)
        {
          if (i + 1 < input.length() && input.charAt(i + 1) == '\"')
          {
            builder.append("\"");
            i++;
            continue;
          }
          if (b)
          {
            return new String[]{"command.expected.separator"};
          }
          if (!builder.isEmpty())
          {
            a.add(builder.toString());
            builder = new StringBuilder();
            isInDoubleQuote = false;
          }
        }
        else
        {
          isInDoubleQuote = true;
        }
      }
      else if (c == '\'' && !isInDoubleQuote)
      {
        if (isInQuote)
        {
          if (i + 1 < input.length() && input.charAt(i + 1) == '\'')
          {
            builder.append("'");
            i++;
            continue;
          }
          if (b)
          {
            return new String[]{"command.expected.separator"};
          }
          if (!builder.isEmpty())
          {
            a.add(builder.toString());
            builder = new StringBuilder();
            isInQuote = false;
          }
        }
        else
        {
          isInQuote = true;
        }
      }
      else if (c == ' ' && !isInQuote && !isInDoubleQuote)
      {
        if (!builder.isEmpty() && !builder.toString().equals(" "))
        {
          a.add(builder.toString());
        }
        builder = new StringBuilder();
      }
      else
      {
        builder.append(c);
      }
    }
    if (!builder.isEmpty())
    {
      if (isInDoubleQuote || isInQuote)
      {
        return new String[]{"parsing.quote.expected.end"};
      }
      a.add(builder.toString());
    }
    if ((forTabComplete && a.isEmpty()) || input.endsWith(" "))
    {
      a.add("");
    }
    return Method.listToArray(a);
  }

  @NotNull
  public static String[] splitEscape(@NotNull String str, char token)
  {
    List<String> list = new ArrayList<>();
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < str.length(); i++)
    {
      char c = str.charAt(i);
      if (c == token)
      {
        if (i + 1 < str.length() && str.charAt(i + 1) == token)
        {
          builder.append(token);
          i++;
          continue;
        }
        if (!builder.isEmpty())
        {
          list.add(builder.toString());
          builder = new StringBuilder();
        }
      }
      else
      {
        builder.append(c);
      }
    }
    list.add(builder.toString());
    return Method.listToArray(list);
  }

  /**
   * 문자열 배열을 하나의 문자열로 반환합니다.
   *
   * @param msg 문자열로 만들 배열
   * @return 하나의 문자열
   */
  @NotNull
  public static String listToString(@NotNull String... msg) // 문자열 배열 -> 문자열 변환
  {
    return MessageUtil.listToString("", msg);
  }

  /**
   * 문자열 배열의 from 부터 to 까지의 배열을 하나의 문자열로 반환합니다.
   *
   * @param from 배열의 시작
   * @param to   배열의 끝
   * @param msg  문자열로 만들 배열
   * @return 하나의 문자열
   */
  @NotNull
  public static String listToString(int from, int to, @NotNull String... msg) // 문자열 배열 -> 문자열 변환
  {
    return MessageUtil.listToString("", from, to, msg);
  }

  /**
   * 문자열 배열을 하나의 문자열로 반환합니다. 각 배열 사이에 토큰이 삽입됩니다.
   *
   * @param token 배열 사이의 토큰
   * @param msg   문자열로 만들 배열
   * @return 하나의 문자열
   */
  @NotNull
  public static String listToString(@NotNull String token, @NotNull String... msg) // 문자열 배열 -> 문자열 변환(배열 토큰 설정)
  {
    return MessageUtil.listToString(token, 0, msg.length, msg);
  }

  /**
   * 문자열 배열의 from 부터 to 까지의 배열을 하나의 문자열로 반환합니다. 각 배열 사이에 토큰이 삽입됩니다.
   * <p>
   * 예 : listToString(" ", 2, 4, args) 일 경우, args[2] + " " + args[3] 반환
   * </p>
   *
   * @param token 배열 사의의 토큰
   * @param from  배열의 시작 (포함)
   * @param to    배열의 끝 (미포함)
   * @param msg   문자열로 만들 배열
   * @return 하나의 문자열
   */
  @NotNull
  public static String listToString(@NotNull String token, int from, int to, @NotNull String... msg) // 문자열 배열 -> 문자열 변환(배열 토큰 설정)
  {
    if (from > to)
    {
      throw new IllegalArgumentException("from is bigger than to");
    }
    if (msg.length == 0)
    {
      return "";
    }
    StringBuilder tmp = new StringBuilder();
    for (int i = from; i < to; i++)
    {
      tmp.append(msg[i]).append((i == to - 1) ? "" : token);
    }
    return tmp.toString();
  }

  /**
   * 매개변수를 그대로 돌려줍니다.
   *
   * @param objects 매개변수
   * @return 매개변수
   */
  @NotNull
  public static Object[] as(Object... objects)
  {
    return objects;
  }

  /**
   * 플레이어마다 컵포넌트의 내용물을(주로 아이템) 다르게 하기 위함
   */
  @SuppressWarnings("all")
  @NotNull
  private static Component translate(@NotNull Player player, @NotNull Component component)
  {
    HoverEvent<?> hoverEvent = component.hoverEvent();
    if (hoverEvent != null && hoverEvent.action() == Action.SHOW_ITEM)
    {
      ShowItem showItem = (ShowItem) hoverEvent.value();
      Material material = Material.valueOf(showItem.item().value().toUpperCase());
      ItemStack itemStack = new ItemStack(material, showItem.count());
      BinaryTagHolder binaryTagHolder = showItem.nbt();
      NBTItem nbtItem = new NBTItem(itemStack, true);
      if (binaryTagHolder != null)
      {
        nbtItem.mergeCompound(new NBTContainer(binaryTagHolder.string()));
      }
      if (material == Material.BUNDLE && "test".equals(new NBTItem(itemStack).getString("test")))
      {
        itemStack = ((BundleMeta) itemStack.getItemMeta()).getItems().get(0);
        nbtItem = new NBTItem(itemStack);
      }
      if (Method.usingLoreFeature(player))
      {
        ItemLore.setItemLore(itemStack, new ItemLoreView(player));
      }
      else
      {
        ItemLore.removeItemLore(itemStack);
      }
      if (player.hasPermission("asdf") && (!nbtItem.hasTag("VirtualItem") || nbtItem.getBoolean("VirtualItem") == null || !nbtItem.getBoolean("VirtualItem")))
      {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<Component> lore = itemMeta.lore();
        if (lore == null)
        {
          lore = new ArrayList<>();
        }
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7클릭하여 /give 명령어로 복사"));
        if (UserData.SHOW_GIVE_COMMAND_NBT_ON_ITEM_ON_CHAT.getBoolean(player))
        {
          try
          {
            ItemStack clone = ItemLore.removeItemLore(itemStack.clone());
            String nbt = new NBTItem(clone).getCompound().toString();
            if (!nbt.equals("{}"))
            {
              int count = 0;
              while (!nbt.isEmpty())
              {
                count++;
                if (count > 20)
                {
                  lore.add(ComponentUtil.translate("&7&ocontainer.shulkerBox.more", nbt.length() / 50));
                  break;
                }
                String cut = nbt.substring(0, Math.min(50, nbt.length()));
                lore.add(Component.text(cut, NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, State.FALSE));
                nbt = nbt.substring(cut.length());
              }
            }
          }
          catch (Exception ignored)
          {

          }
        }
        itemMeta.lore(lore);
        itemStack.setItemMeta(itemMeta);
      }
      else
      {
        component = component.clickEvent(null);
      }
      component = component.hoverEvent(ItemStackComponent.itemStackComponent(itemStack).hoverEvent());
    }
    if (component instanceof TranslatableComponent translatableComponent)
    {
      List<Component> args = new ArrayList<>(translatableComponent.args());
      args.replaceAll(component1 -> translate(player, component1));
      component = translatableComponent.args(args);
    }
    if (!component.children().isEmpty())
    {
      List<Component> children = new ArrayList<>(component.children());
      children.replaceAll(component1 -> translate(player, component1));
      component = component.children(children);
    }
    return component;
  }

  /**
   * 메시지를 보냅니다.
   *
   * @param audience 메시지를 받는 대상
   * @param objects  보낼 메시지
   */
  public static void sendMessage(@NotNull Object audience, @NotNull Object... objects)
  {
    if (Cucumbery.using_CommandAPI && audience instanceof NativeProxyCommandSender proxyCommandSender)
    {
      audience = proxyCommandSender.getCallee();
    }
    if (audience instanceof UUID uuid)
    {
      Entity entity = Bukkit.getEntity(uuid);
      if (entity != null)
      {
        audience = entity;
      }
    }
    if (audience instanceof Collection<?> list)
    {
      for (Object o : list)
      {
        MessageUtil.sendMessage(o, objects);
      }
    }
    if (audience instanceof Audience a)
    {
      // command blocks never get feedback since 2023.02.13
      if (a instanceof BlockCommandSender)
      {
        return;
      }
      Component message;
      if (objects.length == 1 && objects[0] instanceof Component component)
      {
        message = component;
        if (a instanceof Player player)
        {
          message = translate(player, message);
        }
      }
      else
      {
        if (a instanceof Player player)
        {
          message = ComponentUtil.create(player, objects);
        }
        else
        {
          message = ComponentUtil.create(objects);
        }
      }
      if (a instanceof ConsoleCommandSender)
      {
        message = ComponentUtil.create("#52ee52;[Cucumbery] ", message);
      }
      a.sendMessage(message);
      if (a instanceof Entity entity && CustomEffectManager.hasEffect(entity, CustomEffectType.CURSE_OF_BEANS))
      {
        a.sendMessage(message);
      }
    }
  }

  public static void sendMessage(@NotNull Object audience, @NotNull String key)
  {
    sendMessage(audience, (Prefix) null, key, true);
  }

  public static void sendMessage(@NotNull Object audience, @NotNull String key, @NotNull Object... args)
  {
    sendMessage(audience, null, key, args);
  }

  public static void sendMessage(@NotNull Object audience, @NotNull Prefix prefix, @NotNull String key)
  {
    sendMessage(audience, prefix, key, true);
  }

  /**
   * 메시지를 보냅니다.
   *
   * @param audience 메시지를 받는 대상
   * @param prefix   메시지 접두사
   * @param key      메시지 키 값
   * @param args     메시지의 매개 변수
   */
  public static void sendMessage(@NotNull Object audience, @Nullable Prefix prefix, @NotNull String key, @NotNull Object... args)
  {
    if (audience instanceof List<?> list)
    {
      for (Object o : list)
      {
        sendMessage(o, prefix, key, args);
      }
    }
    else
    {
      Player p = audience instanceof Player player ? player : null;
      Component component = ComponentUtil.translate(p, key, args);
      if (prefix != null)
      {
        component = ComponentUtil.create(prefix, component);
      }
      sendMessage(audience, component);
    }
  }

  public static void sendWarn(@NotNull Object audience, @NotNull String key)
  {
    sendWarn(audience, key, true);
  }

  public static void sendWarn(@NotNull Object audience, @NotNull String key, @NotNull Object... args)
  {
    if (Cucumbery.config.getBoolean("sound-const.warning-sound.enable"))
    {
      SoundPlay.playWarnSound(audience);
    }
    sendMessage(audience, Prefix.INFO_WARN, key, args);
  }

  public static void sendError(@NotNull Object audience, @NotNull String key)
  {
    sendError(audience, key, true);
  }

  public static void sendError(@NotNull Object audience, @NotNull String key, @NotNull Object... args)
  {
    if (Cucumbery.config.getBoolean("sound-const.error-sound.enable"))
    {
      SoundPlay.playErrorSound(audience);
    }
    sendMessage(audience, Prefix.INFO_ERROR, key, args);
  }

  public static void sendWarnOrError(boolean error, @NotNull Object audience, @NotNull String key)
  {
    sendWarnOrError(error, audience, key, true);
  }

  public static void sendWarnOrError(boolean error, @NotNull Object audience, @NotNull String key, @NotNull Object... args)
  {
    if (!error)
    {
      sendWarn(audience, key, args);
    }
    else
    {
      sendError(audience, key, args);
    }
  }

  public static void info(@NotNull Object audience, @NotNull String key)
  {
    info(audience, key, true);
  }

  public static void info(@NotNull Object audience, @NotNull String key, @NotNull Object... args)
  {
    sendMessage(audience, Prefix.INFO, key, args);
  }


  public static void sendToast(@NotNull Object audience, @NotNull Component title, @NotNull ItemStack itemStack, @NotNull Frame frame)
  {
    NamespacedKey namespacedKey = NamespacedKey.minecraft("z-cucumbery-toast-" + System.currentTimeMillis());
    if (audience instanceof Player player)
    {
      itemStack = itemStack.clone();
      ItemLore.setItemLore(itemStack, ItemLoreView.of(player));
      new ToastMessage(namespacedKey, title, itemStack, frame).showTo(player);
    }
    else if (audience instanceof Collection<?> collection)
    {
      int stack = 1;
      for (Object o : collection)
      {
        @NotNull ItemStack finalItemStack = itemStack;
        Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
        sendToast(o, title, finalItemStack, frame), stack);
        stack++;
      }
    }
  }

  /**
   * 관리자 메시지(회색 기울임꼴 문자)를 출력합니다. commandSender를 제외한 모든 관리자에게만 출력합니다.
   *
   * @param commandSender 해당 메시지를 실행한 주체
   * @param exception     추가로 메시지를 보여주지 않을 개체
   * @param component     출력할 메시지
   */
  public static void sendAdminMessage(@NotNull Object commandSender, @Nullable List<Permissible> exception, @NotNull Component component)
  {
    Collection<Permissible> collection = new ArrayList<>(Bukkit.getOnlinePlayers());
    if (exception != null)
    {
      collection.removeAll(exception);
    }
    collection.removeIf(c -> !c.hasPermission("minecraft.admin.command_feedback"));
    if (Cucumbery.using_CommandAPI && commandSender instanceof NativeProxyCommandSender proxyCommandSender)
    {
      commandSender = proxyCommandSender.getCallee();
    }
    if (commandSender instanceof Player player)
    {
      collection.remove(player);
    }
    Location location = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
    if (commandSender instanceof BlockCommandSender blockCommandSender)
    {
      location = blockCommandSender.getBlock().getLocation();
    }
    if (commandSender instanceof Entity entity)
    {
      location = entity.getLocation();
    }
    if (Boolean.TRUE.equals(location.getWorld().getGameRuleValue(GameRule.SEND_COMMAND_FEEDBACK)) &&
            (!(commandSender instanceof BlockCommandSender blockCommandSender) || Boolean.TRUE.equals(blockCommandSender.getBlock().getLocation().getWorld().getGameRuleValue(GameRule.COMMAND_BLOCK_OUTPUT))))
    {
      Component message = setAdminMessage(ComponentUtil.translate("chat.type.admin", commandSender, component));
      sendMessage(collection, message);
      if (!(commandSender instanceof ConsoleCommandSender))
      {
        String worldName = location.getWorld().getName();
        int x = location.getBlockX(), y = location.getBlockY(), z = location.getBlockZ();
        message = message.append(ComponentUtil.create("&7 - " + worldName + ", " + x + ", " + y + ", " + z));
        consoleSendMessage(message);
      }
    }
  }

  public static void sendAdminMessage(@NotNull Object commandSender, @Nullable List<Permissible> exception, @NotNull String key)
  {
    sendAdminMessage(commandSender, exception, ComponentUtil.translate(key));
  }

  public static void sendAdminMessage(@NotNull Object commandSender, @Nullable List<Permissible> exception, @NotNull String key, @NotNull Object... args)
  {
    sendAdminMessage(commandSender, exception, ComponentUtil.translate(key, args));
  }

  public static void sendAdminMessage(@NotNull Object commandSender, @NotNull String key)
  {
    sendAdminMessage(commandSender, null, ComponentUtil.translate(key));
  }

  public static void sendAdminMessage(@NotNull Object commandSender, @NotNull String key, @NotNull Object... args)
  {
    sendAdminMessage(commandSender, null, ComponentUtil.translate(key, args));
  }

  @NotNull
  private static Component setAdminMessage(@NotNull Component component)
  {
    component = component.decoration(TextDecoration.ITALIC, TextDecoration.State.TRUE);
    component = component.color(NamedTextColor.GRAY);
    List<Component> children = new ArrayList<>(component.children());
    children.replaceAll(MessageUtil::setAdminMessage);
    if (component instanceof TranslatableComponent translatableComponent)
    {
      List<Component> args = new ArrayList<>(translatableComponent.args());
      args.replaceAll(MessageUtil::setAdminMessage);
      component = translatableComponent.args(args);
    }
    return component.children(children);
  }

  /**
   * 콘솔에 메시지를 보냅니다.
   *
   * @param objects 보낼 메시지
   */
  public static void consoleSendMessage(@NotNull Object... objects)
  {
    sendMessage(Bukkit.getServer().getConsoleSender(), objects);
  }

  public static void consoleSendMessage(@NotNull String key)
  {
    consoleSendMessage(null, key);
  }

  public static void consoleSendMessage(@NotNull String key, @NotNull Object... args)
  {
    consoleSendMessage(null, key, args);
  }

  public static void consoleSendMessage(@Nullable Prefix prefix, @NotNull String key)
  {
    consoleSendMessage(prefix, key, true);
  }

  public static void consoleSendMessage(@Nullable Prefix prefix, @NotNull String key, @NotNull Object... args)
  {
    sendMessage(Bukkit.getConsoleSender(), prefix, key, args);
  }

  /**
   * 모든 플레이어에게 메시지를 보냅니다.
   *
   * @param objects 보낼 메시지
   */
  public static void broadcastPlayer(@NotNull Object... objects)
  {
    broadcastPlayer((Predicate<Player>) null, objects);
  }

  /**
   * 모든 플레이어에게 메시지를 보냅니다.
   *
   * @param exclude 메시지를 보내지 않을 플레이어
   * @param objects 보낼 메시지
   */
  public static void broadcastPlayer(@Nullable Predicate<Player> exclude, @NotNull Object... objects)
  {
    for (Player player : Bukkit.getOnlinePlayers())
    {
      if (exclude != null && exclude.test(player))
      {
        continue;
      }
      MessageUtil.sendMessage(player, objects);
    }
  }

  public static void broadcastPlayer(@NotNull String key)
  {
    broadcastPlayer(null, key);
  }

  public static void broadcastPlayer(@NotNull String key, @NotNull Object... args)
  {
    broadcastPlayer(null, key, args);
  }

  public static void broadcastPlayer(@Nullable Prefix prefix, @NotNull String key)
  {
    broadcastPlayer(prefix, key, true);
  }

  public static void broadcastPlayer(@Nullable Prefix prefix, @NotNull String key, @NotNull Object... args)
  {
    for (Player player : Bukkit.getOnlinePlayers())
    {
      sendMessage(player, prefix, key, args);
    }
  }

  /**
   * 콘솔과 모든 플레이어에게 메시지를 보냅니다.
   *
   * @param objects 보낼 메시지
   */
  public static void broadcast(@NotNull Object... objects)
  {
    consoleSendMessage(objects);
    broadcastPlayer(objects);
  }

  public static void broadcast(@Nullable Prefix prefix, @NotNull String key, @NotNull Object... args)
  {
    consoleSendMessage(prefix, key, args);
    broadcastPlayer(prefix, key, args);
  }

  /**
   * 플레이어에게 디버그 메시지를 보냅니다.
   *
   * @param player  디버그 메시지를 받을 플레이어
   * @param objects 메시지
   */
  public static void sendDebug(@NotNull Player player, @NotNull Object... objects)
  {
    if (CustomConfig.UserData.SHOW_PLUGIN_DEV_DEBUG_MESSAGE.getBoolean(player) || CustomEffectManager.hasEffect(player, CustomEffectType.DEBUG_WATCHER))
    {
      Object[] msg = new Object[objects.length + 1];
      msg[0] = Prefix.INFO_DEBUG;
      System.arraycopy(objects, 0, msg, 1, objects.length);
      MessageUtil.sendMessage(player, msg);
    }
  }

  /**
   * 모든 플레이어에게 디버그 메시지를 보냅니다.
   *
   * @param objects 메시지
   */
  public static void broadcastDebug(@NotNull Object... objects)
  {
    for (Player player : Bukkit.getServer().getOnlinePlayers())
    {
      MessageUtil.sendDebug(player, objects);
    }
  }

  public static void sendWarn(@NotNull Object audience, @NotNull Object... objects)
  {
    if (Cucumbery.config.getBoolean("sound-const.warning-sound.enable"))
    {
      SoundPlay.playWarnSound(audience);
    }
    Object[] newObjects = new Object[objects.length + 1];
    newObjects[0] = Prefix.INFO_WARN;
    System.arraycopy(objects, 0, newObjects, 1, objects.length);
    sendMessage(audience, newObjects);
  }

  public static void sendError(@NotNull Object audience, @NotNull Object... objects)
  {
    if (Cucumbery.config.getBoolean("sound-const.error-sound.enable"))
    {
      SoundPlay.playErrorSound(audience);
    }
    Object[] newObjects = new Object[objects.length + 1];
    newObjects[0] = Prefix.INFO_ERROR;
    System.arraycopy(objects, 0, newObjects, 1, objects.length);
    sendMessage(audience, newObjects);
  }

  public static void sendWarnOrError(boolean error, @NotNull Object audience, @NotNull Object... objects)
  {
    if (!error)
    {
      sendWarn(audience, objects);
    }
    else
    {
      sendError(audience, objects);
    }
  }

  public static void info(@NotNull Object audience, @NotNull Object... objects)
  {
    Object[] newObjects = new Object[objects.length + 1];
    newObjects[0] = Prefix.INFO;
    System.arraycopy(objects, 0, newObjects, 1, objects.length);
    sendMessage(audience, newObjects);
  }

  public static void commandInfo(@NotNull Object audience, @NotNull String label, @NotNull String usage)
  {
    info(audience, "/" + label + " " + usage);
  }

  public static boolean checkQuoteIsValidInArgs(@NotNull CommandSender sender, @NotNull String[] args)
  {
    return checkQuoteIsValidInArgs(sender, args, false);
  }

  public static boolean checkQuoteIsValidInArgs(@NotNull CommandSender sender, @NotNull String[] args, boolean forTabComplete)
  {
    if (args.length == 1 && args[0].equals(ComponentUtil.serialize(ComponentUtil.translate("parsing.quote.expected.end"))))
    {
      if (!forTabComplete)
      {
        MessageUtil.sendError(sender, ComponentUtil.translate("parsing.quote.expected.end"));
      }
      return false;
    }
    if (args.length == 1 && args[0].equals(ComponentUtil.serialize(ComponentUtil.translate("command.expected.separator"))))
    {
      if (!forTabComplete)
      {
        MessageUtil.sendError(sender, ComponentUtil.translate("command.expected.separator"));
      }
      return false;
    }
    return true;
  }

  @Deprecated
  public static boolean checkQuoteIsValidInArgs2(@NotNull CommandSender sender, @NotNull String[] args)
  {
    return (args.length != 1 || !args[0].equals("parsing.quote.expected.end")) && (args.length != 1 || !args[0].equals("command.expected.separator"));
  }

  /**
   * 문자열의 컬러 코드 (§)를 제거합니다.
   *
   * @param msg 컬러 코드를 제거할 문자열
   * @return 컬러 코드가 제거된 문자열
   */
  @NotNull
  public static String stripColor(@NotNull String msg) // 텍스트 컬러 제거
  {
    return msg.replaceAll("§(.)", "");
  }

  /**
   * 문자열의 컬러 코드 기호 (&)를 컬러 코드 (§)로 변환합니다.
   *
   * @param msg 컬러 코드를 변환할 문자열
   * @return 컬러 코드가 변환된 문자열
   */
  @NotNull
  public static String n2s(@NotNull String msg) // 컬러 코드 적용
  {
    return MessageUtil.n2s(msg, N2SType.NORMAL);
  }

  /**
   * 문자열의 컬러 코드 기호 (&)를 컬러 코드 (§)로 변환합니다.
   *
   * @param input 컬러 코드를 변환할 문자열
   * @param type  SPECIAL로 하면 && 로 입력된 모든 문자열도 강제로 §로 변환합니다.
   * @return 컬러 코드가 변환된 문자열
   */
  @NotNull
  public static String n2s(@NotNull String input, @NotNull N2SType type)
  {
    if (type == N2SType.SPECIAL_ONLY)
    {
      return input.replace("&&", "§");
    }
    String tmp = MessageUtil.n2sHex(ChatColor.translateAlternateColorCodes('&', input));
    tmp = ColorUtil.chatEffect(tmp);
    tmp = tmp.replace("&p", "§p").replace("&q", "§q").replace("&i", "§i");
    return type == N2SType.SPECIAL ? tmp.replace("&&", "§") : tmp;
  }

  /**
   * #AAAAAA; #AAA; #AA; #A; 요로코롬 쓰면 댐 뒤에 2개(#AA;, #A;)는 흑백 채널 rgb100,100,100; rg100,100; rbg100,100,100; r100;
   *
   * @param input 컬러코드를 변환할 문자열
   * @return 컬러코드가 변환된 문자열
   */
  @NotNull
  private static String n2sHex(@NotNull String input)
  {
    // #ABCDEF; #ABCDEF
    input = input.replaceAll("#([a-fA-F0-9])([a-fA-F0-9])([a-fA-F0-9])([a-fA-F0-9])([a-fA-F0-9])([a-fA-F0-9]);", "§x§$1§$2§$3§$4§$5§$6");
    // #ABC; => #AABBCC
    input = input.replaceAll("#([a-fA-F0-9])([a-fA-F0-9])([a-fA-F0-9]);", "§x§$1§$1§$2§$2§$3§$3");
    // #AB; => #ABABAB
    input = input.replaceAll("#([a-fA-F0-9])([a-fA-F0-9]);", "§x§$1§$2§$1§$2§$1§$2");
    // #A; => #AAAAAA
    input = input.replaceAll("#([a-fA-F0-9]);", "§x§$1§$1§$1§$1§$1§$1");
    // rgbNNN,NNN,NNN; => #XXXXXX
    Matcher matcher_rgb = PATTERN_N2S_RGB.matcher(input);
    ArrayList<ArrayList<String>> matches_rgb = new ArrayList<>();
    while (matcher_rgb.find())
    {
      ArrayList<String> m = new ArrayList<>();
      m.add(matcher_rgb.group(0));
      m.add(matcher_rgb.group(1));
      m.add(matcher_rgb.group(3));
      m.add(matcher_rgb.group(6));
      m.add(matcher_rgb.group(9));
      matches_rgb.add(m);
    }
    for (ArrayList<String> m : matches_rgb)
    {
      String xc = "RXGXBX";
      for (int n = 0; n < 3; n++)
      {
        if (n > m.get(1).length() - 1)
        {
          break;
        }
        if (m.get(n + 2) == null)
        {
          break;
        }
        char c = m.get(1).toLowerCase().charAt(n);
        String cHex = new StringBuffer(Integer.toHexString(0x100 | Integer.parseInt(m.get(n + 2))).substring(1)).insert(1, "§").insert(0, "§").toString();
        switch (c)
        {
          case 'r' -> xc = xc.replace("RX", cHex);
          case 'g' -> xc = xc.replace("GX", cHex);
          case 'b' -> xc = xc.replace("BX", cHex);
        }
      }
      xc = xc.replaceAll("[RGB]X", "§0§0");
      input = input.replaceAll(m.get(0), "§x" + xc);
    }
    return input;
  }

  @NotNull
  public static String s2a(@NotNull String input)
  {

    /// ESC[ 38;2;r⟩;⟨g⟩;⟨b⟩ m
    Matcher matcher_hex = Pattern.compile("§x§([0-9a-fA-F])§([0-9a-fA-F])§([0-9a-fA-F])§([0-9a-fA-F])§([0-9a-fA-F])§([0-9a-fA-F])").matcher(input);
    ArrayList<ArrayList<String>> matches_hex = new ArrayList<>();
    while (matcher_hex.find())
    {
      ArrayList<String> m = new ArrayList<>();
      m.add(matcher_hex.group(0));
      m.add(matcher_hex.group(1));
      m.add(matcher_hex.group(2));
      m.add(matcher_hex.group(3));
      m.add(matcher_hex.group(4));
      m.add(matcher_hex.group(5));
      m.add(matcher_hex.group(6));
      matches_hex.add(m);
    }
    for (ArrayList<String> m : matches_hex)
    {
      String xc = "\u001b[38;2;R;G;Bm";
      xc = xc.replace("R", String.valueOf(Integer.parseInt(m.get(1) + m.get(2) + "", 16)));
      xc = xc.replace("G", String.valueOf(Integer.parseInt(m.get(3) + m.get(4) + "", 16)));
      xc = xc.replace("B", String.valueOf(Integer.parseInt(m.get(5) + m.get(6) + "", 16)));
      input = input.replace(m.get(0), xc);
    }

    input = input.replace("§0", "\u001b[30m");
    input = input.replace("§4", "\u001b[31m");
    input = input.replace("§2", "\u001b[32m");
    input = input.replace("§6", "\u001b[33m");
    input = input.replace("§1", "\u001b[34m");
    input = input.replace("§5", "\u001b[35m");
    input = input.replace("§3", "\u001b[36m");
    input = input.replace("§7", "\u001b[37m");
    input = input.replace("§8", "\u001b[90m");
    input = input.replace("§c", "\u001b[91m");
    input = input.replace("§a", "\u001b[92m");
    input = input.replace("§e", "\u001b[93m");
    input = input.replace("§9", "\u001b[94m");
    input = input.replace("§d", "\u001b[95m");
    input = input.replace("§b", "\u001b[96m");
    input = input.replace("§f", "\u001b[97m");

    input = input.replace("§l", "\u001b[1m");
    input = input.replace("§m", "\u001b[2m");
    input = input.replace("§n", "\u001b[4m");
    input = input.replace("§o", "\u001b[3m");
    input = input.replace("§r", "\u001b[0m");
    input = input.replace("§k", "\u001b[5m");
    return input + "\u001b[0m";
  }

  @NotNull
  public static String n2a(@NotNull String input)
  {
    return MessageUtil.s2a(MessageUtil.n2s(input));
  }

  /**
   * 입력한 문자열의 대소문자를 전환하여 반환합니다.
   *
   * @param input 대소문자를 전환할 문자열
   * @return 대소문자가 전환된 문자열
   */
  @NotNull
  public static String switchCase(@NotNull String input)
  {
    char[] chars = input.toCharArray();
    for (int i = 0; i < chars.length; i++)
    {
      char c = chars[i];
      if (65 <= c && 97 > c)
      {
        chars[i] = (char) (c + 32);
      }
      else if (96 < c && 123 > c)
      {
        chars[i] = (char) (c - 32);
      }
    }
    return new String(chars);
  }

  public static void noArg(@NotNull Object audience, @NotNull Prefix reason, @NotNull String arg)
  {
    sendError(audience, "%s: %s", reason, arg);
  }

  public static void shortArg(@NotNull Object audience, int input, @NotNull String[] args)
  {
    sendError(audience, Prefix.ARGS_SHORT + " (rg255,204;" + args.length + "개&r 입력, 최소 rg255,204;" + input + "개&r)");
  }

  public static void longArg(@NotNull Object audience, int input, @NotNull String[] args)
  {
    sendError(audience, Prefix.ARGS_LONG + " (rg255,204;" + args.length + "개&r 입력, 최대 rg255,204;" + input + "개&r)");
  }

  public static void wrongArg(@NotNull Object audience, int input, @NotNull String[] args)
  {
    sendError(audience, Prefix.ARGS_WRONG + " (rg255,204;" + input + "번째 &r인수 : rg255,204;" + args[input - 1] + "&r)");
  }

  public static void wrongBool(@NotNull Object audience, int input, @NotNull String[] args)
  {
    sendError(audience, "잘못된 불입니다. '%s' 또는 '%s'가 필요하지만 %s번째 인수에 '%s'이(가) 입력되었습니다", "true", "false", input, args[input - 1]);
  }

  public static void sendTitle(@NotNull Object player, @Nullable Object title, @Nullable Object subTitle)
  {
    sendTitle(player, title, subTitle, -1, -1, -1);
  }

  public static void sendTitle(@NotNull Object player, @Nullable Object title, @Nullable Object subTitle, long fadeIn, long stay, long fadeOut)
  {
    if (player instanceof Audience audience)
    {
      if (title != null)
      {
        audience.sendTitlePart(TitlePart.TITLE, ComponentUtil.create(title));
      }
      if (subTitle != null)
      {
        audience.sendTitlePart(TitlePart.SUBTITLE, ComponentUtil.create(subTitle));
      }
      if (fadeIn >= 0 && stay >= 0 && fadeOut >= 0)
      {
        audience.sendTitlePart(TitlePart.TIMES, Times.times(Duration.ofMillis(50 * fadeIn), Duration.ofMillis(50 * stay), Duration.ofMillis(50 * fadeOut)));
      }
    }
    if (player instanceof Collection<?> collection)
    {
      for (Object o : collection)
      {
        if (o instanceof Audience audience)
        {
          sendTitle(audience, title, subTitle, fadeIn, stay, fadeOut);
        }
      }
    }
  }

  public static void sendActionBar(@NotNull Object player, @NotNull Object... objects)
  {
    if (player instanceof Collection<?> collection)
    {
      for (Object o : collection)
      {
        sendActionBar(o, objects);
      }
    }
    if (player instanceof Audience audience)
    {
      audience.sendActionBar(ComponentUtil.stripEvent(ComponentUtil.create(objects)));
    }
  }

  public static void sendActionBar(@NotNull Object player, @NotNull String key)
  {
    if (player instanceof Collection<?> collection)
    {
      for (Object o : collection)
      {
        sendActionBar(o, key);
      }
    }
    if (player instanceof Audience audience)
    {
      audience.sendActionBar(ComponentUtil.stripEvent(ComponentUtil.translate(key)));
    }
  }

  public static void sendActionBar(@NotNull Object player, @NotNull String key, @NotNull Object... params)
  {
    if (player instanceof Collection<?> collection)
    {
      for (Object o : collection)
      {
        sendActionBar(o, key, params);
      }
    }
    if (player instanceof Audience audience)
    {
      audience.sendActionBar(ComponentUtil.stripEvent(ComponentUtil.translate(key, params)));
    }
  }

  public static boolean checkNumberSize(CommandSender sender, long value, long min, long max)
  {
    return checkNumberSize(sender, value, min, max, false, false, true);
  }

  public static boolean checkNumberSize(CommandSender sender, long value, long min, long max, boolean excludesMin, boolean excludesMax, boolean notice)
  {
    if (excludesMin && value <= min)
    {
      if (notice)
      {
        sendError(sender, "key:argument.long.low.exclusive|long은 %s 초과여야 하는데, %s이(가) 있습니다", Constant.Sosu15.format(min), Constant.Sosu15.format(value));
      }
      return false;
    }
    else if (!excludesMin && value < min)
    {
      if (notice)
      {
        sendError(sender, "argument.long.low", Constant.Sosu15.format(min), Constant.Sosu15.format(value));
      }
      return false;
    }
    else if (excludesMax && value >= max)
    {
      if (notice)
      {
        sendError(sender, "key:argument.long.big.exclusive|long은 %s 미만이어야 하는데, %s이(가) 있습니다", Constant.Sosu15.format(max), Constant.Sosu15.format(value));
      }
      return false;
    }
    else if (!excludesMax && value > max)
    {
      if (notice)
      {
        sendError(sender, "argument.long.big", Constant.Sosu15.format(max), Constant.Sosu15.format(value));
      }
      return false;
    }
    return true;
  }

  public static boolean checkNumberSize(@Nullable CommandSender sender, double value, double min, double max)
  {
    return checkNumberSize(sender, value, min, max, false, false, true);
  }

  public static boolean checkNumberSize(@Nullable CommandSender sender, double value, double min, double max, boolean notice)
  {
    return checkNumberSize(sender, value, min, max, false, false, notice);
  }

  public static boolean checkNumberSize(CommandSender sender, double value, double min, double max, boolean excludesMin, boolean excludesMax)
  {
    return checkNumberSize(sender, value, min, max, excludesMin, excludesMax, true);
  }

  public static boolean checkNumberSize(CommandSender sender, double value, double min, double max, boolean excludesMin, boolean excludesMax, boolean notice)
  {
    if (excludesMin && value <= min)
    {
      if (notice)
      {
        sendError(sender, "key:argument.double.low.exclusive|double은 %s 초과여야 하는데, %s이(가) 있습니다", Constant.Sosu15.format(min), Constant.Sosu15.format(value));
      }
      return false;
    }
    else if (!excludesMin && value < min)
    {
      if (notice)
      {
        sendError(sender, "argument.double.low", Constant.Sosu15.format(min), Constant.Sosu15.format(value));
      }
      return false;
    }
    else if (excludesMax && value >= max)
    {
      if (notice)
      {
        sendError(sender, "key:argument.double.big.exclusive|double은 %s 미만이어야 하는데, %s이(가) 있습니다", Constant.Sosu15.format(max), Constant.Sosu15.format(value));
      }
      return false;
    }
    else if (!excludesMax && value > max)
    {
      if (notice)
      {
        sendError(sender, "argument.double.big", Constant.Sosu15.format(max), Constant.Sosu15.format(value));
      }
      return false;
    }
    return true;
  }

  @SuppressWarnings("all")
  @NotNull
  public static String getFinalConsonant(@Nullable String word, @NotNull ConsonantType type)
  {
    try
    {
      if (word == null || word.length() < 1)
      {
        return type.toString();
      }
      word = word.replace(" ", "");
      word = stripColor(n2s(word));
      word = word.replaceAll("[ㅏㅑㅓㅕㅗㅛㅜㅠㅡㅣㅙㅞㅚㅟㅢㅝㅘㅐㅖ]", "가");
      word = word.replaceAll("[ㄱㄲㄴㄷㄸㅁㅂㅃㅅㅆㅇㅈㅉㅊㅋㅌㅍㅎ]", "각");
      word = word.replaceAll("[+()<>\\[\\]∞.#@&%!?\"'\\\\$_,*~\\-]", "");
      word = word.replace("ㄹ", "갈");
      word = word.replace("'", "").replace("\"", "");
      if (word.length() == 1)
      {
        word = word.toUpperCase();
      }
      char c;
      boolean b = true;
      String test = KoreanUtils.format("%s는", word);
      if (test.endsWith(")"))
      {
        throw new Exception();
      }
      if (test.endsWith("은"))
      {
        test = KoreanUtils.format("%s로", word);
        if (test.endsWith("으로"))
        {
          c = '각';
        }
        else
        {
          c = '갈';
        }
      }
      else
      {
        c = '가';
        b = false;
      }
      switch (type)
      {
        case 와과, 과와, 와, 과 ->
        {
          if (b)
          {
            return "과";
          }
          return "와";
        }
        case 으로 ->
        {
          if (b && ((c - 44032) % (21 * 28)) % 28 != 8)
          {
            return "으로";
          }
          return "로";
        }
        case 은는, 는은, 은, 는 ->
        {
          if (b)
          {
            return "은";
          }
          return "는";
        }
        case 을를, 를을, 을, 를 ->
        {
          if (b)
          {
            return "을";
          }
          return "를";
        }
        case 이가, 가이, 이, 가 ->
        {
          if (b)
          {
            return "이";
          }
          return "가";
        }
        case 이라 ->
        {
          if (b)
          {
            return "이라";
          }
          return "라";
        }
      }
      return type.toString();
    }
    catch (Exception e)
    {
      return type.toString();
    }
  }

  public static boolean isInteger(CommandSender sender, String a, boolean notice)
  {
    try
    {
      try
      {
        Double.parseDouble(a);
      }
      catch (Exception e)
      {
        if (notice)
        {
          MessageUtil.noArg(sender, Prefix.ONLY_NUMBER, a);
        }
        return false;
      }
      Integer.parseInt(a);
      return true;
    }
    catch (Exception e)
    {
      if (notice)
      {
        MessageUtil.noArg(sender, Prefix.ONLY_INTEGER, a);
      }
      return false;
    }
  }

  public static boolean isLong(CommandSender sender, String a, boolean notice)
  {
    try
    {
      try
      {
        Double.parseDouble(a);
      }
      catch (Exception e)
      {
        if (notice)
        {
          MessageUtil.noArg(sender, Prefix.ONLY_NUMBER, a);
        }
        return false;
      }
      Long.parseLong(a);
      return true;
    }
    catch (Exception e)
    {
      if (notice)
      {
        MessageUtil.noArg(sender, Prefix.ONLY_INTEGER, a);
      }
      return false;
    }
  }

  public static boolean isDouble(@Nullable CommandSender sender, @NotNull String a, boolean notice)
  {
    try
    {
      double d = Double.parseDouble(a);
      if (Double.isNaN(d) || Double.isInfinite(d))
      {
        throw new NumberFormatException("Double cannot be NaN or Infinity!");
      }
      return true;
    }
    catch (Exception e)
    {
      if (notice)
      {
        if (sender != null)
        {
          MessageUtil.noArg(sender, Prefix.ONLY_NUMBER, a);
        }
      }
      return false;
    }
  }

  public static boolean isBoolean(@NotNull CommandSender sender, @NotNull String[] args, int input, boolean notice)
  {
    // 입력값이 배열의 길이보다 길면 boolean은 false가 되므로 boolean임
    if (input > args.length)
    {
      return true;
    }
    String arg = args[input - 1];
    if (!arg.equals("true") && !arg.equals("false"))
    {
      if (notice)
      {
        MessageUtil.wrongBool(sender, input, args);
      }
      return false;
    }
    return true;
  }

  /**
   * 밀리초 단위로 시간을 받아 현실 시간과 게임 시간을 나타낸 문자열을 반환합니다.
   *
   * @param time 밀리초
   * @return 현실 시간과 게임 시간을 나타낸 문자열 (현실 시간 : n시 n분 n초, 게임 시간 : n시 n분 n초)
   */
  @NotNull
  public static Component periodRealTimeAndGameTime(long time)
  {
    return ComponentUtil.translate("&7%s, %s",
            ComponentUtil.translate("&2현실 시간 : %s", Component.text(Method.timeFormatMilli(time * 50L, false, 1, true), NamedTextColor.GREEN)
            ).hoverEvent(Component.text(Method.timeFormatMilli(time * 50L))),
            ComponentUtil.translate("&3게임 시간 : %s", Component.text(Method.timeFormatMilli(time * 3600L, false, 1, true), NamedTextColor.AQUA)
            ).hoverEvent(Component.text(Method.timeFormatMilli(time * 3600L))));
  }

  /**
   * 컬러 코드만 연속적으로 막 §1§2§3 하면 제대로 저장되지 않기 때문에 §１§２§３ 으로 저장되게 하기 위함
   *
   * @param str 문자열
   * @return 반환값
   */
  @NotNull
  public static String stringEncode(@NotNull String str)
  {
    return str.replace("0", "０").replace("1", "１").replace("2", "２").replace("3", "３").replace("4", "４").replace("5", "５").replace("6", "６").replace("7", "７").replace("8", "８").replace("9", "９")
            .replace("A", "Ａ").replace("B", "Ｂ").replace("C", "Ｃ").replace("D", "Ｄ").replace("E", "Ｅ").replace("F", "Ｆ").replace("K", "Ｋ").replace("L", "Ｌ").replace("M", "Ｍ").replace("N", "Ｎ")
            .replace("O", "Ｏ").replace("R", "Ｒ").replace("X", "Ｘ").replace("a", "ａ").replace("b", "ｂ").replace("c", "ｃ").replace("d", "ｄ").replace("e", "ｅ").replace("f", "ｆ").replace("k", "ｋ")
            .replace("l", "ｌ").replace("m", "ｍ").replace("n", "ｎ").replace("o", "ｏ").replace("r", "ｒ").replace("x", "ｘ");
  }

  /**
   * §１§２§３ 이렇게 보이는걸 다시 숫자(§1§2§3)로 바꾸기 위해
   *
   * @param str 문자열
   * @return 반환값
   */
  @NotNull
  public static String stringDecode(@NotNull String str)
  {
    return str.replace("０", "0").replace("１", "1").replace("２", "2").replace("３", "3").replace("４", "4").replace("５", "5").replace("６", "6").replace("７", "7").replace("８", "8").replace("９", "9")
            .replace("Ａ", "A").replace("Ｂ", "B").replace("Ｃ", "C").replace("Ｄ", "D").replace("Ｅ", "E").replace("Ｆ", "F").replace("Ｋ", "K").replace("Ｌ", "L").replace("Ｍ", "M").replace("Ｎ", "N")
            .replace("Ｏ", "O").replace("Ｒ", "R").replace("Ｘ", "X").replace("ａ", "a").replace("ｂ", "b").replace("ｃ", "c").replace("ｄ", "d").replace("ｅ", "e").replace("ｆ", "f").replace("ｋ", "k")
            .replace("ｌ", "l").replace("ｍ", "m").replace("ｎ", "n").replace("ｏ", "o").replace("ｒ", "r").replace("ｘ", "x");
  }

  @NotNull
  public static Component boldify(@NotNull Component component)
  {
    List<Component> children = new ArrayList<>(component.children());
    children.replaceAll(MessageUtil::boldify);
    component = component.decoration(TextDecoration.BOLD, State.TRUE).children(children);
    return component;
  }

  public enum N2SType // 컬러 코드 적용 타입
  {
    NORMAL,
    SPECIAL,
    SPECIAL_ONLY
  }

  @SuppressWarnings({"all"})
  public enum ConsonantType
  {
    을를("을(를)"),
    을를_2("(을)를"),
    를을("를(을)"),
    를을_2("(를)을"),
    을("을(를)"),
    를("를(을)"),
    은는("은(는)"),
    은는_2("(은)는"),
    는은("는(은)"),
    는은_2("(는)은"),
    은("은(는)"),
    는("는(은)"),
    이가("이(가)"),
    이가_2("(이)가"),
    가이("가(이)"),
    가이_2("(가)이"),
    이("이(가)"),
    가("가(이)"),
    와과("와(과)"),
    와과_2("(와)과"),
    과와("과(와)"),
    과와_2("(과)와"),
    와("와(과)"),
    과("과(와)"),
    으로("(으)로"),
    으로_2("으(로)"),
    이라("(이)라"),
    이라_2("이(라)");

    private final String a;

    ConsonantType(String a)
    {
      this.a = a;
    }

    @Override
    public String toString()
    {
      return a;
    }
  }
}

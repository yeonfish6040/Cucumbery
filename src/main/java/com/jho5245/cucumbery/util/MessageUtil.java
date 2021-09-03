package com.jho5245.cucumbery.util;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.josautil.KoreanUtils;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.CustomConfig;
import com.jho5245.cucumbery.util.storage.SoundPlay;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class MessageUtil
{
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
        if (builder.length() > 0)
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
   * 메시지를 보냅니다.
   *
   * @param audience 메시지를 받는 대상
   * @param objects  보낼 메시지
   */
  public static void sendMessage(@NotNull Object audience, @NotNull Object... objects)
  {
    if (audience instanceof Audience)
    {
      Component message;
      if (objects.length == 1 && objects[0] instanceof Component component)
      {
        message = component;
      }
      else
         message = ComponentUtil.create(objects);
      ((Audience) audience).sendMessage(message);
    }
  }

  /**
   * 콘솔에 메시지를 보냅니다.
   *
   * @param objects 보낼 메시지
   */
  public static void consoleSendMessage(@NotNull Object... objects)
  {
    MessageUtil.sendMessage(Bukkit.getServer().getConsoleSender(), objects);
  }

  /**
   * 모든 플레이어에게 메시지를 보냅니다.
   *
   * @param objects 보낼 메시지
   */
  public static void broadcastPlayer(@NotNull Object... objects)
  {
    for (Player player : Bukkit.getOnlinePlayers())
    {
      MessageUtil.sendMessage(player, objects);
    }
  }

  /**
   * 플레이어에게 디버그 메시지를 보냅니다.
   *
   * @param player  디버그 메시지를 받을 플레이어
   * @param objects 메시지
   */
  public static void sendDebug(@NotNull Player player, @NotNull Object... objects)
  {
    if (CustomConfig.UserData.SHOW_PLUGIN_DEV_DEBUG_MESSAGE.getBoolean(player))
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

  public static void sendWarn(@NotNull Object audience, @NotNull Prefix msg)
  {
    sendWarn(audience, msg.toString());
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

  public static void sendError(@NotNull Object audience, @NotNull Prefix msg)
  {
    sendError(audience, msg.toString());
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

  public enum N2SType // 컬러 코드 적용 타입
  {
    NORMAL,
    SPECIAL,
    SPECIAL_ONLY
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
    return type == N2SType.SPECIAL ? tmp.replace("&&", "§") : tmp;
  }

  private static final Pattern PATTERN_N2S_RGB = Pattern.compile("(([RGBrgb]){1,3})(([0-9]){1,3})(,(([0-9]){1,3}))?(,(([0-9]){1,3}))?;");

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
    sendError(audience, reason + " &r(&e" + arg + "&r)");
  }

  public static void shortArg(@NotNull Object audience, int input, @NotNull String[] args)
  {
    sendError(audience, Prefix.ARGS_SHORT + " (&e" + args.length + "개&r 입력, 최소 &e" + input + "개&r)");
  }

  public static void longArg(@NotNull Object audience, int input, @NotNull String[] args)
  {
    sendError(audience, Prefix.ARGS_LONG + " (&e" + args.length + "개&r 입력, 최대 &e" + input + "개&r)");
  }

  public static void wrongArg(@NotNull Object audience, int input, @NotNull String[] args)
  {
    sendError(audience, Prefix.ARGS_WRONG + " (&e" + input + "번째 &r인수 : &e" + args[input - 1] + "&r)");
  }

  public static void wrongBool(@NotNull Object audience, int input, @NotNull String[] args)
  {
    sendError(audience, "잘못된 불입니다. '&etrue&r' 또는 '&efalse&r'가 필요하지만 &e" + input + "번&r째 인수에 '&e" + args[input - 1] + "&r'" + getFinalConsonant(args[input - 1], ConsonantType.이가) + " 입력되었습니다.");
  }

  public static void sendTitle(@NotNull Player player, @Nullable Object title, @Nullable Object subTitle, int fadeIn, int stay, int fadeOut)
  {
    @SuppressWarnings("all")
    Title t = Title.title(title != null ? ComponentUtil.create(title) : Component.empty(), subTitle != null ? ComponentUtil.create(subTitle) : Component.empty(),
            Title.Times.of(Duration.ofMillis(fadeIn * 50L), Duration.ofMillis(stay * 50L), Duration.ofMillis(fadeOut * 50L)));
    player.showTitle(t);
  }

  public static void sendTitle(@NotNull Player player, @Nullable Object[] title, @Nullable Object[] subTitle, int fadeIn, int stay, int fadeOut)
  {
    @SuppressWarnings("all")
    Title t = Title.title(ComponentUtil.create(title), subTitle != null ? ComponentUtil.create(subTitle) : Component.empty(),
            Title.Times.of(Duration.ofMillis(fadeIn * 50L), Duration.ofMillis(stay * 50L), Duration.ofMillis(fadeOut * 50L)));
    player.showTitle(t);
  }

  public static void sendActionBar(@NotNull Player player, @NotNull Object ... objects)
  {
    player.sendActionBar(ComponentUtil.create(objects));
  }

  public static boolean checkNumberSize(CommandSender sender, long value, long min, long max)
  {
    return checkNumberSize(sender, value, min, max, false, false, true);
  }

  public static boolean checkNumberSize(CommandSender sender, long value, long min, long max, boolean excludesMin, boolean excludesMax, boolean notice)
  {
    String valueString = "&e" + Constant.Sosu15.format(value) + "&r";
    valueString += getFinalConsonant(valueString, ConsonantType.이가);
    if (excludesMin && value <= min)
    {
      if (notice)
      {
        sendError(sender, "정수는 &e" + Constant.Sosu15.format(min) + "&r 초과여야 하는데, " + valueString + " 있습니다.");
      }
      return false;
    }
    else if (!excludesMin && value < min)
    {
      if (notice)
      {
        sendError(sender, "정수는 &e" + Constant.Sosu15.format(min) + "&r 이상이어야 하는데, " + valueString + " 있습니다.");
      }
      return false;
    }
    else if (excludesMax && value >= max)
    {
      if (notice)
      {
        sendError(sender, "정수는 &e" + Constant.Sosu15.format(max) + "&r 미만이어야 하는데, " + valueString + " 있습니다.");
      }
      return false;
    }
    else if (!excludesMax && value > max)
    {
      if (notice)
      {
        sendError(sender, "정수는 &e" + Constant.Sosu15.format(max) + "&r 이하여야 하는데, " + valueString + " 있습니다.");
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

  public static boolean checkNumberSize(@Nullable CommandSender sender, double value, double min, double max, boolean excludessMin, boolean excludessMax)
  {
    return checkNumberSize(sender, value, min, max, excludessMin, excludessMax, true);
  }

  public static boolean checkNumberSize(@Nullable CommandSender sender, double value, double min, double max, boolean excludessMin, boolean excludessMax, boolean notice)
  {
    String valueString = "&e" + Constant.Sosu15.format(value) + "&r";
    valueString += getFinalConsonant(valueString, ConsonantType.이가);
    if (excludessMin && value <= min)
    {
      if (notice)
      {
        if (sender != null)
        {
          sendError(sender, "숫자는 &e" + Constant.Sosu15.format(min) + "&r 초과여야 하는데, " + valueString + " 있습니다.");
        }
      }
      return false;
    }
    else if (!excludessMin && value < min)
    {
      if (notice)
      {
        if (sender != null)
        {
          sendError(sender, "숫자는 &e" + Constant.Sosu15.format(min) + "&r 이상이어야 하는데, " + valueString + " 있습니다.");
        }
      }
      return false;
    }
    else if (excludessMax && value >= max)
    {
      if (notice)
      {
        if (sender != null)
        {
          sendError(sender, "숫자는 &e" + Constant.Sosu15.format(max) + "&r 미만이어야 하는데, " + valueString + " 있습니다.");
        }
      }
      return false;
    }
    else if (!excludessMax && value > max)
    {
      if (notice)
      {
        if (sender != null)
        {
          sendError(sender, "숫자는 &e" + Constant.Sosu15.format(max) + "&r 이하여야 하는데, " + valueString + " 있습니다.");
        }
      }
      return false;
    }
    return true;
  }

  @SuppressWarnings({"all"})
  public enum ConsonantType
  {
    을를("을(를)"),
    를을("를(을)"),
    은는("은(는)"),
    는은("는(은)"),
    이가("이(가)"),
    가이("가(이)"),
    와과("와(과)"),
    과와("과(와)"),
    으로("(으)로"),
    이라("(이)라");

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

  @NotNull
  public static String getFinalConsonant(@Nullable String word, @NotNull ConsonantType type)
  {
    try
    {
      if (word == null || word.length() < 1)
      {
        return type.toString();
      }
      String wordClone = word;
      wordClone = stripColor(n2s(wordClone)).replace(" ", "");
/*      wordClone = wordClone.replace("TNT", "티엔티");
      while (wordClone.matches("(.*)[^A-Za-z0-9가-힣]"))
      {
        wordClone = wordClone.substring(0, wordClone.length() - 1);
      }*/
      char c = wordClone.charAt(wordClone.length() - 1);
/*      switch (c)
      {
        case '0' -> c = '영';
        case '1' -> c = '일';
        case '2', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
               'o', 'p', 'q', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' -> c = '이';
        case  'l', 'm', 'n' , 'r'-> c = '익';
        case '3' -> c = '삼';
        case '4' -> c = '사';
        case '5' -> c = '오';
        case '6' -> c = '육';
        case '7' -> c = '칠';
        case '8' -> c = '팔';
        case '9' -> c = '구';
      }*/
      /*
      if (c < 44032 || c > 55203)
      {
        return type.toString();
      }*/
      //String nfd = Normalizer.normalize(c + "", Normalizer.Form.NFD);
      boolean b = true; // nfd.length() != 2;
      String test = KoreanUtils.format("%s는", c);
      if (test.endsWith("은"))
      {
        test = KoreanUtils.format("%s로", c);
        if (test.endsWith("으로"))
        c = '각';
        else
          c = '갈';
      }
      else
      {
        c = '가';
        b = false;
      }
      switch (type)
      {
        case 와과, 과와 -> {
          if (b)
          {
            return "과";
          }
          return "와";
        }
        case 으로 -> {
          if (b && ((c - 44032) % (21 * 28)) % 28 != 8)
          {
            return "으로";
          }
          return "로";
        }
        case 은는, 는은 -> {
          if (b)
          {
            return "은";
          }
          return "는";
        }
        case 을를, 를을 -> {
          if (b)
          {
            return "을";
          }
          return "를";
        }
        case 이가, 가이 -> {
          if (b)
          {
            return "이";
          }
          return "가";
        }
        case 이라 -> {
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
      Double.parseDouble(a);
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
  public static String periodRealTimeAndGameTime(long time)
  {
    return MessageUtil.n2s("&2현실 시간 : &a" + Method.timeFormatMilli(time * 50L) + "&r, &3게임 시간 : &b" + Method.timeFormatMilli(time * 3600L));
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
}

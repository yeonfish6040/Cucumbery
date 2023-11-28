package com.jho5245.cucumbery.util.addons;

import com.jho5245.cucumbery.Cucumbery;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Songs
{
  private static final ExecutorService songsExecutorService = Executors.newFixedThreadPool(1);
  private static final Timer songsLoopTimer = new Timer();
  public static List<String> list = new ArrayList<>();

  @SuppressWarnings("all")
  public static File download(String name, boolean force) throws IOException
  {
    if (!name.endsWith(".nbs"))
    name += ".nbs";
    File file = new File(Cucumbery.getPlugin().getDataFolder() + "/data/songs/" + name);
    if (!force && file.exists())
    {
      return file;
    }
    else
    {
      file.delete();
      if (!file.getParentFile().exists())
      {
        file.getParentFile().mkdirs();
      }
      file.createNewFile();
    }

    URL url = new URL(tranformStyle("https://cucumbery.com/api/songs/" + name + "/download"));
    HttpURLConnection connection = (HttpURLConnection) (url).openConnection();
    connection.setRequestMethod("GET");
    connection.setRequestProperty("User-Agent", "Cucumbery");
    connection.setConnectTimeout(2000);
    connection.setReadTimeout(2000);
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "Cp1252"));
    FileOutputStream fis = new FileOutputStream(file);
    OutputStreamWriter osw = new OutputStreamWriter(fis, "Cp1252");
    BufferedWriter writer = new BufferedWriter(osw);
    char[] buffer = new char[1024];
    int count;
    while ((count = bufferedReader.read(buffer, 0, 1024)) != -1)
    {
      writer.write(buffer, 0, count);
    }
    writer.close();
    osw.close();
    fis.close();
    bufferedReader.close();
    return file;
  }

  public static void updateList()
  {
    try
    {
      URL url = new URL("https://cucumbery.com/api/songs");
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.setRequestProperty("User-Agent", "Cucumbery");
      connection.setConnectTimeout(2000);
      connection.setReadTimeout(2000);
      int responseCode = connection.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK)
      {
        JSONArray array = getJsonArray(connection);
        list.clear();
        for (Object o : array)
        {
          String songName = ((JSONObject) o).get("name").toString();
          if (songName.endsWith(".nbs"))
          {
            list.add(songName.substring(0, songName.length() - 4));
          }
        }
      }
      connection.disconnect();
    }
    catch (Exception ignored)
    {

    }
  }

  private static JSONArray getJsonArray(HttpURLConnection connection) throws IOException, ParseException
  {
    Reader inputReader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
    BufferedReader streamReader = new BufferedReader(inputReader);
    String streamLine;
    StringBuilder content = new StringBuilder();
    while ((streamLine = streamReader.readLine()) != null)
    {
      content.append(streamLine);
    }
    streamReader.close();
    JSONParser parser = new JSONParser();
    JSONObject object = (JSONObject) parser.parse(content.toString());
    JSONArray array = (JSONArray) object.get("data");
    return array;
  }

  public static String tranformStyle(String source)
  {
    char[] arr = source.toCharArray();
    StringBuilder sb = new StringBuilder();
    for (char temp : arr)
    {
      if (isSpecial(temp))
      {
        sb.append(URLEncoder.encode(String.valueOf(temp), StandardCharsets.UTF_8));
        continue;
      }
      sb.append(temp);
    }
    String re = sb.toString();
    re = re.replace(" ", "%20");
    return re;
  }

  public static boolean isSpecial(char c)
  {
    Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
    return ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
            || ub == Character.UnicodeBlock.HANGUL_JAMO
            || ub == Character.UnicodeBlock.HANGUL_JAMO_EXTENDED_A
            || ub == Character.UnicodeBlock.HANGUL_JAMO_EXTENDED_B
            || ub == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO
            || ub == Character.UnicodeBlock.HANGUL_SYLLABLES
            || ub == Character.UnicodeBlock.HIRAGANA
            || ub == Character.UnicodeBlock.KATAKANA
            || ub == Character.UnicodeBlock.KANA_EXTENDED_A
            || ub == Character.UnicodeBlock.KANA_SUPPLEMENT
            || ub == Character.UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS
            || ub == Character.UnicodeBlock.CJK_COMPATIBILITY
            || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
            || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
            || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT
            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C
            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D
            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_E
            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_F
            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_G
            || ub == Character.UnicodeBlock.CJK_RADICALS_SUPPLEMENT
            || ub == Character.UnicodeBlock.CJK_STROKES
            || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
            || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
  }

  public static void onEnable()
  {
    songsExecutorService.submit(() -> songsLoopTimer.schedule(new TimerTask()
    {
      @Override
      public void run()
      {
        updateList();
      }
    }, 0, 2000));
  }

  public static void onDisable()
  {
    songsLoopTimer.cancel();
    songsExecutorService.shutdownNow();
  }
}

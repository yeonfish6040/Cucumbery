package com.jho5245.cucumbery.util.storage.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Updater
{
  private static final Cucumbery plugin = Cucumbery.getPlugin();
  private static final String pluginAPI = plugin.getDescription().getAPIVersion();
  private static final String userAgent = "Updater";
  private static final ExecutorService updaterExecutorService = Executors.newFixedThreadPool(1);
  private static final Timer updaterLoopTimer = new Timer();
  public static Updater defaultUpdater = new Updater("https://cucumbery.com/api/builds", "dev");
  private final String api; //http://cherry.wany.io/api/builds
  private final String channel;
  private @Nullable String version;
  private File file;

  public Updater(String api, String channel)
  {
    this.api = api;
    this.channel = channel;
  }

  public static void onEnable()
  {
    String channel = "dev"; //OR Config
    updaterExecutorService.submit(() ->
    {
      switch (channel)
      {
        case "release" -> {
          updaterLoopTimer.schedule(new TimerTask()
          {
            @Override
            public void run()
            {
              if (Cucumbery.config.getBoolean("auto-updater.enable"))
              {
                defaultUpdater.updateLatest();
              }
            }
          }, 0, 1000 * 60 * 60);
        }
        case "dev" -> {
          updaterLoopTimer.schedule(new TimerTask()
          {
            @Override
            public void run()
            {
              if (Cucumbery.config.getBoolean("auto-updater.enable"))
              {
                defaultUpdater.updateLatest();
              }
            }
          }, 0, 1000 * 2);
        }
      }
    });
  }

  public static void onDisable()
  {
    updaterLoopTimer.cancel();
    updaterExecutorService.shutdownNow();
  }

  public void download() throws IOException
  {
    if (this.version == null)
    {
      return;
    }
    File file = new File(plugin.getDataFolder() + "/" + plugin.getDescription().getName() + ".jar.temp");
    if (file.exists())
    {
      file.delete();
    }

    URL url = new URL(this.api + "/" + this.channel + "/" + this.version + "/download");

    file.getParentFile().mkdirs();
    file.createNewFile();

    BufferedInputStream bis = new BufferedInputStream(url.openStream());
    FileOutputStream fis = new FileOutputStream(file);
    byte[] buffer = new byte[1024];
    int count = 0;
    while ((count = bis.read(buffer, 0, 1024)) != -1)
    {
      fis.write(buffer, 0, count);
    }

    fis.close();
    bis.close();
    this.file = file;
  }

  public void update()
  {
    if (this.file == null || this.version == null)
    {
      return;
    }
    File pluginsDir = new File("plugins");
    File pluginFile = new File(pluginsDir, plugin.getDescription().getName() + "-" + this.version + ".jar");

    byte[] data = new byte[0];
    try
    {
      data = Files.readAllBytes(this.file.toPath());
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    Path path = pluginFile.toPath();
    file.delete();

    try
    {
      Files.write(path, data);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    Bukkit.getScheduler().runTask(plugin, () ->
    {
      PluginLoader.unload();
      if (!pluginFile.getPath().equals(Cucumbery.file.getPath()))
      {
        Cucumbery.file.delete();
      }
      PluginLoader.load(pluginFile);
    });
  }

  public void getLatestVersion()
  {
    this.version = null;
    try
    {
      URL url = new URL(this.api + "/" + this.channel + "/latest" + "?api=" + pluginAPI);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.setRequestProperty("User-Agent", userAgent);
      connection.setConnectTimeout(2000);
      connection.setReadTimeout(2000);
      int responseCode = connection.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK)
      {
        Reader inputReader = new InputStreamReader(connection.getInputStream());
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
        JSONObject data = (JSONObject) object.get("data");
        this.version = data.get("version").toString();
      }
      connection.disconnect();
    }
    catch (Exception ignored)
    {

    }

  }

  public boolean updateLatest()
  {
    return updateLatest(false);
  }

  public boolean updateLatest(boolean force)
  {
    try
    {
      this.getLatestVersion();
      if (!force && this.version != null && this.version.equals(plugin.getDescription().getVersion()))
      {
        return false;
      }
      this.download();
      this.update();
    }
    catch (Exception ignored)
    {

    }

    return true;
  }
}

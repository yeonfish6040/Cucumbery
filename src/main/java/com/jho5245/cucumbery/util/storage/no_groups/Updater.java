package com.jho5245.cucumbery.util.storage.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import io.wany.amethyst.Json;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import io.wany.amethyst.network.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Updater
{

  private static final String PACKAGE = "Cucumbery";
  private static final String API = "api.wany.io/amethy/repository/" + PACKAGE;
  public static String CHANNEL = "dev";

  private static final ExecutorService onEnableExecutor = Executors.newFixedThreadPool(1);
  private static final Timer onEnableTimer = new Timer();

  private static Plugin PLUGIN;
  private static File FILE;
  private static File PLUGINS_DIR;
  private static String VERSION;

  @SuppressWarnings("deprecation")
  public static void onEnable()
  {

    PLUGIN = Cucumbery.getPlugin();
    FILE = Cucumbery.file;
    PLUGINS_DIR = FILE.getParentFile();
    VERSION = PLUGIN.getDescription().getVersion();

//    // 콘피그에서 업데이터 채널 가져오기
//    if (Cucumbery.config.contains("updater.channel"))
//    {
//      CHANNEL = Cucumbery.config.getString("updater.channel");
//    }
//    else
//    {
//      Cucumbery.config.set("updater.channel", CHANNEL);
//    }


    // 업데이터 자동화 체커
    onEnableExecutor.submit(() -> onEnableTimer.schedule((new TimerTask()
    {
      @Override
      public void run()
      {
        if (Cucumbery.config.getBoolean("auto-updater.enable"))
        {
          automation();
        }
      }
    }), 5000, 2000));
  }

  public static void onDisable()
  {
    // 업데이터 자동화 체커 종료
    onEnableTimer.cancel();
    onEnableExecutor.shutdown();
  }

  /**
   * 최신 플러그인 버전 가져오기
   *
   * @return 최신 플러그인 버전
   * @throws Exception 오류
   */
  public static String getLatest() throws Exception
  {
    Json res = HTTPRequest.JsonGet("https://" + API + "/" + CHANNEL + "/latest");
    return res.getString("data.version");
  }

  /**
   * 현재 플러그인이 최신 버전인지 확인
   *
   * @return 최신 버전 여부
   * @throws Exception 오류
   */
  public static boolean isLatest() throws Exception
  {
    return getLatest().equals(VERSION);
  }

  /**
   * 특정 버전의 플러그인 패키지 다운로드
   *
   * @param version 플러그인 버전
   * @return 다운로드한 플러그인 패키지 파일
   * @throws Exception 오류
   */
  public static File download(String version) throws Exception
  {
    File file = new File(PLUGINS_DIR + "/" + version + ".temp");

    try
    {
      if (file.exists())
      {
        file.delete();
      }
      file.getParentFile().mkdirs();
      file.createNewFile();
    }
    catch (SecurityException | IOException exception)
    {
      file.delete();
      throw exception;
    }

    try
    {
      BufferedInputStream bis = new BufferedInputStream(
              new URL("https://" + API + "/" + CHANNEL + "/" + version + "/download").openStream());
      FileOutputStream fis = new FileOutputStream(file);
      byte[] buffer = new byte[1024];
      int count;
      while ((count = bis.read(buffer, 0, 1024)) != -1)
      {
        fis.write(buffer, 0, count);
      }
      fis.close();
      bis.close();
    }
    catch (Exception exception)
    {
      file.delete();
      throw exception;
    }

    return file;
  }

  /**
   * 특정 버전으로 플러그인 업데이트
   *
   * @param tempFile 다운로드한 플러그인 패키지 파일
   * @param version  다운로드한 플러그인 버전
   * @throws Exception 오류
   */
  public static void update(File tempFile, String version) throws Exception
  {
    String name = PLUGIN.getName();
    File newFile = new File(PLUGINS_DIR, name + "-" + version + ".jar");

    byte[] data;
    data = Files.readAllBytes(tempFile.toPath());
    Path path = newFile.toPath();
    tempFile.delete();
    Files.write(path, data);

    Bukkit.getScheduler().runTask(PLUGIN, () ->
    {
      PluginLoader.unload();
      if (!newFile.getPath().equals(FILE.getPath()))
      {
        FILE.delete();
      }
      PluginLoader.load(newFile);
    });
  }

  public static void automation()
  {
    try
    {
      String version = Updater.getLatest();
      if (!VERSION.equals(version))
      {
        MessageUtil.consoleSendMessage("Found newer version of plugin");
        MessageUtil.consoleSendMessage("Updating plugin...");
        File file = Updater.download(version);
        Updater.update(file, version);
      }
    }
    catch (Exception ignored)
    {
    }
  }

}
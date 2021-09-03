package com.jho5245.cucumbery.commands.teleport;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.MessageUtil.ConsonantType;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.CustomConfig;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Warp implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_WARP, true))
    {
      return true;
    }
    String usage = cmd.getUsage().replace("/<command> ", ""), consoleUsage = usage.replace("[플레이어 ID]", "<플레이어 ID>");
    switch (cmd.getName().toLowerCase())
    {
      case "cwarps":
        if (args.length == 0)
        {
          int warpSize = Variable.warps.size();
          MessageUtil.sendMessage(sender, Prefix.INFO_WARP, "워프 목록 : &e" + warpSize + "&e개");
          if (warpSize == 0)
          {
            return true;
          }
          List<Component> txt = new ArrayList<>();
          txt.add(ComponentUtil.create(Prefix.INFO_WARP.toString()));
          for (String configName : Variable.warps.keySet())
          {
            YamlConfiguration warpConfig = Variable.warps.get(configName);
            ConfigurationSection warp = warpConfig.getConfigurationSection("warp");
            String warpText;
            if (warp == null || warp.getKeys(false).size() == 0)
            {
              warpText = "&c워프 정보 없음";
            }
            else
            {
              String display = warpConfig.getString("warp.display");
              String world = warpConfig.getString("warp.world");
              double x = warpConfig.getDouble("warp.x"), y = warpConfig.getDouble("warp.y"), z = warpConfig.getDouble("warp.z"), yaw = warpConfig.getDouble("warp.yaw"), pitch = warpConfig.getDouble(
                      "warp.pitch");
              warpText = "&e[워프 정보]\n&r워프 이름 : &e" +
                      configName +
                      "\n" +
                      ((configName.equals(display)) ? "" : "&r워프 표시 이름 : &e" + display + "\n") +
                      "&r월드 이름 : &e" +
                      world +
                      "\n&rX : &e" +
                      Constant.Sosu2.format(x) +
                      "\n&rY : &e" +
                      Constant.Sosu2.format(y) +
                      "\n&rZ : &e" +
                      Constant.Sosu2.format(z) +
                      "\n&rYaw : &e" +
                      Constant.Sosu2.format(yaw) +
                      "\n&rPitch : &e" +
                      Constant.Sosu2.format(pitch);
            }
            txt.add(ComponentUtil.create("&e" + configName, warpText, ClickEvent.Action.SUGGEST_COMMAND, "/cwarp " + configName));
            txt.add(ComponentUtil.create("&r, "));
          }
          txt.remove(txt.size() - 1);
          Component[] txt2 = new Component[txt.size()];
          MessageUtil.sendMessage(sender, (Object) txt.toArray(txt2));
        }
        else
        {
          MessageUtil.longArg(sender, 0, args);
          MessageUtil.commandInfo(sender, label, "");
        }
        break;
      case "cwarp":
        if (args.length == 0)
        {
          if (!(sender instanceof Player))
          {
            MessageUtil.shortArg(sender, 2, args);
            MessageUtil.commandInfo(sender, label, consoleUsage);
            return true;
          }
          MessageUtil.shortArg(sender, 1, args);
          MessageUtil.commandInfo(sender, label, usage);
          return true;
        }
        else if (args.length <= 5)
        {
          if (args.length == 1 && !(sender instanceof Player))
          {
            MessageUtil.shortArg(sender, 2, args);
            MessageUtil.commandInfo(sender, label, consoleUsage);
            return true;
          }
          Player player;
          if (args.length == 1)
          {
            player = (Player) sender;
          }
          else
          {
            if (args.length == 3 && !MessageUtil.isBoolean(sender, args, 3, true))
            {
              return true;
            }
            player = SelectorUtil.getPlayer(sender, args[1]);
            if (player == null)
            {
              return true;
            }
          }
          if (!MessageUtil.isBoolean(sender, args, 3, true))
          {
            return true;
          }
          if (!MessageUtil.isBoolean(sender, args, 4, true))
          {
            return true;
          }
          if (!MessageUtil.isBoolean(sender, args, 5, true))
          {
            return true;
          }
          boolean preserveKinetic = args.length >= 3 && args[2].equals("true");
          boolean preservePotential = args.length >= 4 && args[3].equals("true");
          boolean hideOutput = args.length >= 5 && args[4].equals("true");
          File folder = new File(Cucumbery.getPlugin().getDataFolder() + "/data/Warps");
          boolean noValidWarps = false;
          if (!folder.exists())
          {
            noValidWarps = true;
          }
          else if (folder.listFiles() == null || Objects.requireNonNull(folder.listFiles()).length == 0)
          {
            noValidWarps = true;
          }
          if (noValidWarps)
          {
            MessageUtil.sendError(player, "사용할 수 있는 워프가 존재하지 않습니다.");
            return true;
          }
          YamlConfiguration config = Variable.warps.get(args[0]);
          if (config == null || config.getConfigurationSection("warp") == null || Objects.requireNonNull(config.getConfigurationSection("warp")).getKeys(false).size() == 0)
          {
            MessageUtil.sendError(sender, "워프 &e" + args[0] + "&r" + MessageUtil.getFinalConsonant(args[0], MessageUtil.ConsonantType.을를) + " 찾을 수 없습니다.");
            return true;
          }
          String name = config.getString("warp.display");
          if (name == null)
          {
            name = args[0];
          }
          World world = Bukkit.getServer().getWorld(Objects.requireNonNull(config.getString("warp.world")));
          if (world == null)
          {
            MessageUtil.sendError(sender, "워프 좌표에 저장된 월드를 찾을 수 없습니다.");
            return true;
          }
          double x = config.getDouble("warp.x");
          double y = config.getDouble("warp.y");
          double z = config.getDouble("warp.z");
          double yaw = config.getDouble("warp.yaw");
          double pitch = config.getDouble("warp.pitch");
          Vector vector = player.getVelocity().clone();
          float fallDistance = player.getFallDistance();
          player.teleport(new Location(world, x, y, z, (float) yaw, (float) pitch));
          if (preserveKinetic)
          {
            player.setVelocity(vector);
          }
          if (preservePotential)
          {
            player.setFallDistance(fallDistance);
          }
          if (!hideOutput)
          {
            Component a = ComponentUtil.create(Prefix.INFO_WARP.toString());
            Component b = ComponentUtil.create("&e" + MessageUtil.n2s(name), "&e[워프 정보]\n&r워프 이름 : &e" +
                            args[0] +
                            (Objects.equals(config.getString("warp.display"), args[0]) ? "" : "\n&r워프 표시 이름 : &e" + name) +
                            "\n&r월드 : &e" +
                            config.getString("warp.world") +
                            "\n&rX : &e" +
                            Constant.Sosu2.format(config.getDouble("warp.x")) +
                            "\n&rY : &e" +
                            Constant.Sosu2.format(config.getDouble("warp.y")) +
                            "\n&rZ : &e" +
                            Constant.Sosu2.format(config.getDouble("warp.z")) +
                            "\n&rYaw : &e" +
                            Constant.Sosu2.format(config.getDouble("warp.yaw")) +
                            "\n&rPitch : &e" +
                            Constant.Sosu2.format(config.getDouble("warp.pitch")), ClickEvent.Action.SUGGEST_COMMAND,
                    "/cwarp " + args[0]);
            Component c = ComponentUtil.create("&r" + MessageUtil.getFinalConsonant(MessageUtil.stripColor(name), ConsonantType.으로) + " 이동하였습니다.");
            if (sender.equals(player))
            {
              MessageUtil.sendMessage(player, a, b, c);
            }
            else
            {
              a = ComponentUtil.create(MessageUtil.as(Prefix.INFO_WARP, sender, "이 당신을 "));
              c = ComponentUtil.create("&r" + MessageUtil.getFinalConsonant(MessageUtil.stripColor(name), ConsonantType.으로) + " 이동시켰습니다.");
              if (!player.equals(sender))
              {
                MessageUtil.sendMessage(player, a, b, c);
              }
              a = ComponentUtil.create(MessageUtil.as(Prefix.INFO_WARP, player + "을(를) "));
              c = ComponentUtil.create("&r" + MessageUtil.getFinalConsonant(MessageUtil.stripColor(name), ConsonantType.으로) + " 이동시켰습니다.");
              MessageUtil.sendMessage(sender, a, b, c);
            }
          }
        }
        else
        {
          MessageUtil.longArg(sender, 3, args);
          if (!(sender instanceof Player))
          {
            MessageUtil.commandInfo(sender, label, consoleUsage);
            return true;
          }
          MessageUtil.commandInfo(sender, label, usage);
          return true;
        }
        break;
      case "csetwarp":
        if (!(sender instanceof Player player))
        {
          MessageUtil.sendError(sender, Prefix.ONLY_PLAYER);
          return true;
        }
        if (args.length < 1)
        {
          MessageUtil.shortArg(player, 1, args);
          MessageUtil.commandInfo(sender, label, usage);
          return true;
        }
        else if (args.length <= 4)
        {
          String warpName = args[0];
          String warpDisplayname = (args.length >= 2) ? args[1] : warpName;
          File folder = new File(Cucumbery.getPlugin().getDataFolder() + "/data/Warps");
          if (!folder.exists())
          {
            boolean success = folder.mkdirs();
            if (!success)
            {
              System.err.println("[Cucumbery] Could not create Warps folder!");
            }
          }
          File file = new File(Cucumbery.getPlugin().getDataFolder() + "/data/Warps/" + args[0] + ".yml");
          CustomConfig warpConfig = CustomConfig.getCustomConfig("data/Warps/" + args[0] + ".yml");
          YamlConfiguration config = warpConfig.getConfig();
          Location location = player.getLocation();
          World world = location.getWorld();
          double x = location.getX(), y = location.getY(), z = location.getZ(), yaw = location.getYaw(), pitch = location.getPitch();
          boolean hideOutput = false;
          if (args.length == 4)
          {
            if (!args[3].equals("true") && !args[3].equals("false"))
            {
              MessageUtil.wrongBool(sender, 4, args);
              return true;
            }
            if (args[3].equals("true"))
            {
              hideOutput = true;
            }
          }
          boolean override = args.length >= 3 && args[2].equals("true");
          if (!file.exists())
          {
            try
            {
              boolean success = file.createNewFile();
              if (!success)
              {
                System.err.println("[Cucumbery] Could not create Warp file!");
              }
            }
            catch (IOException ignored)
            {

            }
          }
          ConfigurationSection warp = config.getConfigurationSection("warp");
          if (warp != null && warp.getKeys(false).size() != 0)
          {
            if (!hideOutput && override)
            {
              MessageUtil.sendWarn(sender, "이미 해당 이름으로 저장된 워프(&e" + args[0] + "&r)가 존재하여 덮어씌웠습니다.");
            }
            else
            {
              MessageUtil.sendError(sender, "이미 해당 이름으로 저장된 워프(&e" + args[0] + "&r)가 존재하여 덮어씌울 수 없습니다.");
              return true;
            }
          }
          config.set("warp.display", warpDisplayname);
          config.set("warp.world", world.getName());
          config.set("warp.x", x);
          config.set("warp.y", y);
          config.set("warp.z", z);
          config.set("warp.yaw", yaw);
          config.set("warp.pitch", pitch);
          if (!hideOutput)
          {
            Component a = ComponentUtil.create(Prefix.INFO_WARP + "현재 위치에 ");
            Component b = ComponentUtil.create("&e" + MessageUtil.n2s(warpDisplayname), "&e[워프 정보]\n&r워프 이름 : &e" +
                            args[0] +
                            (Objects.equals(config.getString("warp.display"), args[0]) ? "" : "\n&r워프 표시 이름 : &e" +
                                    warpDisplayname) +
                            "\n&r월드 : &e" +
                            config.getString("warp.world") +
                            "\n&rX : &e" +
                            Constant.Sosu2.format(config.getDouble("warp.x")) +
                            "\n&rY : &e" +
                            Constant.Sosu2.format(config.getDouble("warp.y")) +
                            "\n&rZ : &e" +
                            Constant.Sosu2.format(config.getDouble("warp.z")) +
                            "\n&rYaw : &e" +
                            Constant.Sosu2.format(config.getDouble("warp.yaw")) +
                            "\n&rPitch : &e" +
                            Constant.Sosu2.format(config.getDouble("warp.pitch")), ClickEvent.Action.SUGGEST_COMMAND,
                    "/cwarp " + args[0]);
            Component c = ComponentUtil.create("&r" + MessageUtil.getFinalConsonant(MessageUtil.stripColor(MessageUtil.n2s(warpDisplayname)), MessageUtil.ConsonantType.이라) + "는 워프를 설정하였습니다.");
            MessageUtil.sendMessage(player, a, b, c);
          }
          warpConfig.saveConfig();
          Variable.warps.put(warpName, config);
        }
        else
        {
          MessageUtil.longArg(player, 3, args);
          MessageUtil.commandInfo(sender, label, usage);
          return true;
        }
        break;
      case "cdelwarp":
        if (args.length == 0)
        {
          MessageUtil.shortArg(sender, 1, args);
          MessageUtil.commandInfo(sender, label, usage);
          return true;
        }
        else if (args.length <= 2)
        {
          File file = new File(Cucumbery.getPlugin().getDataFolder() + "/data/Warps/" + args[0] + ".yml");
          YamlConfiguration config = Variable.warps.get(args[0]);
          boolean hideOutput = false;
          if (args.length == 2)
          {
            if (!args[1].equals("true") && !args[1].equals("false"))
            {
              MessageUtil.wrongBool(sender, 2, args);
              return true;
            }
            if (args[1].equals("true"))
            {
              hideOutput = true;
            }
          }
          if (!file.exists() || config == null)
          {
            MessageUtil.sendError(sender, "워프 &e" + args[0] + "&r" + MessageUtil.getFinalConsonant(args[0], MessageUtil.ConsonantType.을를) + " 찾을 수 없습니다.");
            return true;
          }
          if (!hideOutput)
          {
            if (sender instanceof Player player)
            {
              Component a = ComponentUtil.create(Prefix.INFO_WARP + "워프 ");
              Component b = ComponentUtil.create(
                      "&e" + args[0], "&e[워프 정보]\n&r워프 이름 : &e" +
                              args[0] +
                              (Objects.equals(config.getString("warp.display"), args[0]) ? "" : "\n&r워프 표시 이름 : &e" + MessageUtil.n2s(
                                      Objects.requireNonNull(config.getString("warp.display")))) +
                              "\n&r월드 : &e" +
                              config.getString("warp.world") +
                              "\n&rX : &e" +
                              Constant.Sosu2.format(config.getDouble("warp.x")) +
                              "\n&rY : &e" +
                              Constant.Sosu2.format(config.getDouble("warp.y")) +
                              "\n&rZ : &e" +
                              Constant.Sosu2.format(config.getDouble("warp.z")) +
                              "\n&rYaw : &e" +
                              Constant.Sosu2.format(config.getDouble("warp.yaw")) +
                              "\n&rPitch : &e" +
                              Constant.Sosu2.format(config.getDouble("warp.pitch")));
              Component c = ComponentUtil.create("&r" + MessageUtil.getFinalConsonant(args[0], MessageUtil.ConsonantType.을를) + " 제거하였습니다.");
              MessageUtil.sendMessage(player, a, b, c);
            }
            else
            {
              MessageUtil.sendMessage(sender, Prefix.INFO_WARP, "워프 &e" + args[0] + "&r" + MessageUtil.getFinalConsonant(args[0], MessageUtil.ConsonantType.을를) + " 제거하였습니다.");
            }
          }
          if (file.exists())
          {
            boolean success = file.delete();
            if (!success)
            {
              System.err.println("[Cucumbery] Could not delete warp file!");
            }
          }
          Variable.warps.remove(args[0]);
        }
        else
        {
          MessageUtil.longArg(sender, 2, args);
          MessageUtil.commandInfo(sender, label, usage);
          return true;
        }
        break;
    }
    return true;
  }
}

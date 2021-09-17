package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.storage.CustomConfig;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class CommandPack implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_COMMANDPACK, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    if (args.length < 2)
    {
      MessageUtil.shortArg(sender, 3, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return true;
    }
    else
    {
      switch (args[0])
      {
        case "remove" -> {
          if (args.length > 2)
          {
            MessageUtil.longArg(sender, 2, args);
            MessageUtil.commandInfo(sender, label, "remove <명령어 팩 파일>");
            return true;
          }

          String fileName = args[1];
          File file = new File(Cucumbery.getPlugin().getDataFolder() + "/data/CommandPacks/" + fileName + ".yml");
          if (!file.exists())
          {
            MessageUtil.sendError(sender, "&e" + fileName + "&r" + MessageUtil.getFinalConsonant(fileName, MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 명령어 팩 파일 이름입니다.");
            return true;
          }
          if (!file.delete())
          {
            System.err.println("[Cucumbery] could not delete " + fileName + ".yml file!");
          }
          Variable.commandPacks.remove(fileName);
          MessageUtil.info(sender, "&e" + fileName + ".yml&r 명령어 팩 파일을 제거하였습니다.");
        }
        case "execute" -> {
          if (args.length < 4)
          {
            MessageUtil.shortArg(sender, 4, args);
            MessageUtil.commandInfo(sender, label, "execute <플레이어|--console> <명령어 팩 파일 이름> <명령어 팩>");
            return true;
          }
          if (args.length > 4)
          {
            MessageUtil.longArg(sender, 4, args);
            MessageUtil.commandInfo(sender, label, "execute <플레이어|--console> <명령어 팩 파일 이름> <명령어 팩>");
            return true;
          }
          CommandSender executor;
          if (args[1].equals("--console"))
          {
            executor = Bukkit.getConsoleSender();
          }
          else
          {
            executor = SelectorUtil.getPlayer(sender, args[1]);
            if (executor == null)
            {
              return true;
            }
          }
          Method.commandPack(executor, args[2], args[3]);
        }
        case "edit" -> {
          if (args.length < 4)
          {
            MessageUtil.shortArg(sender, 5, args);
            MessageUtil.commandInfo(sender, label, "edit <명령어 팩 파일 이름> <명령어 팩> <list|add|remove|set|insert> ...");
            return true;
          }
          String fileName = args[1];
          String packName = args[2];

          YamlConfiguration configuration = Variable.commandPacks.get(fileName);
          List<String> commands = null;
          if (configuration != null)
          {
            commands = configuration.getStringList(packName);
          }

          switch (args[3])
          {
            case "list" -> {
              if (args.length > 4)
              {
                MessageUtil.longArg(sender, 4, args);
                MessageUtil.commandInfo(sender, label, "edit " + fileName + " " + packName + " list");
                return true;
              }
              if (commands == null || commands.size() == 0)
              {
                MessageUtil.sendError(sender, "해당 명령어 팩은 존재하지 않거나 명령어가 없습니다. (파일 이름 : &e" + fileName + ".yml&r, 팩 이름 : &e" + packName + "&r)");
                return true;
              }
              MessageUtil.info(sender, "&e" + fileName + ".yml&r 파일의 &e" + packName + "&r 명령어 팩에는 명령어가 &e" + commands.size() + "개&r 있습니다.");
              for (int i = 0; i < commands.size(); i++)
              {
                MessageUtil.info(sender, "&e" + (i + 1) + "번째 &r명령어 : &e" + commands.get(i));
              }
            }
            case "add" -> {
              if (args.length < 5)
              {
                MessageUtil.shortArg(sender, 5, args);
                MessageUtil.commandInfo(sender, label, "edit " + fileName + " " + packName + " add <명령어>");
                return true;
              }
              File file = new File(Cucumbery.getPlugin().getDataFolder() + "/data/CommandPacks/" + fileName + ".yml");
              if (!file.exists())
              {
                MessageUtil.info(sender, "새로운 명령어 팩 파일을 생성합니다. (&e" + fileName + ".yml&r)");
              }
              CustomConfig commandPackFile = CustomConfig.getCustomConfig(file);
              configuration = commandPackFile.getConfig();
              if (commands == null)
              {
                commands = configuration.getStringList(packName);
              }
              String command = MessageUtil.listToString(" ", 4, args.length, args);
              commands.add(command);
              configuration.set(packName, commands);
              commandPackFile.saveConfig();
              Variable.commandPacks.put(fileName, configuration);
              MessageUtil.info(sender, "&e" + fileName + ".yml&r 파일의 &e" + packName + "&r 명령어 팩의 &e" + commands.size() + "번째&r 줄에 &e" + command + "&r 명령어를 추가하였습니다.");
            }
            case "remove" -> {
              if (commands == null || commands.size() == 0)
              {
                MessageUtil.sendError(sender, "해당 명령어 팩은 존재하지 않거나 명령어가 없습니다. (파일 이름 : &e" + fileName + ".yml&r, 팩 이름 : &e" + packName + "&r)");
                return true;
              }

              if (args.length > 5)
              {
                MessageUtil.longArg(sender, 4, args);
                MessageUtil.commandInfo(sender, label, "edit " + fileName + " " + packName + " remove [줄|--all]");
                return true;
              }
              File file = new File(Cucumbery.getPlugin().getDataFolder() + "/data/CommandPacks/" + fileName + ".yml");
              CustomConfig commandPackFile = CustomConfig.getCustomConfig(file);
              configuration = commandPackFile.getConfig();
              int line = commands.size();
              if (args.length == 5)
              {
                if (args[4].equals("--all"))
                {
                  line = 0;
                }
                else
                {
                  try
                  {
                    line = Integer.parseInt(args[4]);
                  }
                  catch (Exception e)
                  {
                    MessageUtil.noArg(sender, Prefix.ONLY_INTEGER, args[4]);
                    return true;
                  }

                  if (!MessageUtil.checkNumberSize(sender, line, 1, commands.size()))
                  {
                    return true;
                  }
                }
              }
              line--;

              if (line == -1)
              {
                commands.clear();
              }
              else
              {
                commands.remove(line);
              }
              if (commands.size() > 0)
              {
                configuration.set(packName, commands);
                commandPackFile.saveConfig();
                Variable.commandPacks.put(fileName, configuration);
                MessageUtil.info(sender, "&e" + fileName + ".yml&r 파일의 &e" + packName + "&r 명령어 팩의 &e" + (line + 1) + "번째&r 명령어를 제거하였습니다.");
              }
              else
              {
                MessageUtil.info(sender, "&e" + fileName + ".yml&r 파일의 &e" + packName + "&r 명령어 팩의 모든 명령어를 제거하였습니다.");
                configuration.set(packName, null);
                if (configuration.getKeys(false).size() == 0)
                {
                  commandPackFile.delete();
                  Variable.commandPacks.remove(fileName);
                  MessageUtil.info(sender, "남은 명령어 팩이 없어 파일을 삭제합니다. (&e" + fileName + ".yml&r)");
                }
                else
                {
                  commandPackFile.saveConfig();
                  Variable.commandPacks.put(fileName, configuration);
                }
              }
            }
            case "set" -> {
              if (args.length < 6)
              {
                MessageUtil.shortArg(sender, 6, args);
                MessageUtil.commandInfo(sender, label, "edit " + fileName + " " + packName + " set <줄> <명령어>");
                return true;
              }
              int line;
              try
              {
                line = Integer.parseInt(args[4]);
              }
              catch (Exception e)
              {
                MessageUtil.noArg(sender, Prefix.ONLY_INTEGER, args[4]);
                return true;
              }
              File file = new File(Cucumbery.getPlugin().getDataFolder() + "/data/CommandPacks/" + fileName + ".yml");
              if (!file.exists())
              {
                MessageUtil.info(sender, "새로운 명령어 팩 파일을 생성합니다. (&e" + fileName + ".yml&r)");
              }
              CustomConfig commandPackFile = CustomConfig.getCustomConfig(file);
              configuration = commandPackFile.getConfig();
              if (commands == null)
              {
                commands = configuration.getStringList(packName);
              }
              String command = MessageUtil.listToString(" ", 5, args.length, args);
              int size = commands.size();
              for (int i = 0; i < line - size; i++)
              {
                commands.add("");
              }
              commands.set(line - 1, command);
              configuration.set(packName, commands);
              commandPackFile.saveConfig();
              Variable.commandPacks.put(fileName, configuration);
              MessageUtil.info(sender, "&e" + fileName + ".yml&r 파일의 &e" + packName + "&r 명령어 팩의 &e" + line + "번째&r 줄의 명령어를 &e" + command + "&r으로 설정하였습니다.");
            }
            case "insert" -> {
              if (args.length < 6)
              {
                MessageUtil.shortArg(sender, 6, args);
                MessageUtil.commandInfo(sender, label, "edit " + fileName + " " + packName + " insert <줄> <명령어>");
                return true;
              }

              if (commands == null || commands.size() == 0)
              {
                MessageUtil.sendError(sender, "해당 명령어 팩은 존재하지 않거나 명령어가 없습니다. (파일 이름 : &e" + fileName + ".yml&r, 팩 이름 : &e" + packName + "&r)");
                return true;
              }

              int line;
              try
              {
                line = Integer.parseInt(args[4]);
              }
              catch (Exception e)
              {
                MessageUtil.noArg(sender, Prefix.ONLY_INTEGER, args[4]);
                return true;
              }
              if (!MessageUtil.checkNumberSize(sender, line, 1, commands.size()))
              {
                return true;
              }
              File file = new File(Cucumbery.getPlugin().getDataFolder() + "/data/CommandPacks/" + fileName + ".yml");
              CustomConfig commandPackFile = CustomConfig.getCustomConfig(file);
              configuration = commandPackFile.getConfig();
              String command = MessageUtil.listToString(" ", 5, args.length, args);
              commands.add(line - 1, command);
              configuration.set(packName, commands);
              commandPackFile.saveConfig();
              Variable.commandPacks.put(fileName, configuration);
              MessageUtil.info(sender, "&e" + fileName + ".yml&r 파일의 &e" + packName + "&r 명령어 팩의 &e" + line + "번째&r 줄에 &e" + command + "&r 명령어를 들여썼습니다.");
            }
            default -> {
              MessageUtil.wrongArg(sender, 4, args);
              MessageUtil.commandInfo(sender, label, "edit <명령어 팩 파일 이름> <명령어 팩> <list|add|remove|set|insert> ...");
              return true;
            }
          }
        }
        default -> {
          MessageUtil.wrongArg(sender, 1, args);
          MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
          return true;
        }
      }
    }
    return true;
  }
}

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
import org.bukkit.command.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandCommandPack implements CommandExecutor, TabCompleter
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
              if (commands == null || commands.isEmpty())
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
              if (commands == null || commands.isEmpty())
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
              if (!commands.isEmpty())
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
                if (configuration.getKeys(false).isEmpty())
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

              if (commands == null || commands.isEmpty())
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
  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(true, args), true))
    {
      return Collections.singletonList(args[0]);
    }
    int length = args.length;
    String lastArg = args[length - 1];
    if (length == 1)
    {
      return Method.tabCompleterList(args, "<인수>", "edit", "remove", "execute");
    }
    if (length == 2)
    {
      switch (args[0])
      {
        case "execute":
          return Method.tabCompleterList(args, Method.addAll(Method.tabCompleterPlayer(sender, args), "--console"), "[<플레이어>]", true);
        case "edit":
          if (Variable.commandPacks.isEmpty())
          {
            return Method.tabCompleterList(args, "<새로운 명령어 팩 파일 이름>", true);
          }
          return Method.tabCompleterList(args, Variable.commandPacks.keySet(), (Variable.commandPacks.containsKey(lastArg) ? "<편집할 명령어 팩 파일 이름>" : "<새로운 명령어 팩 파일 이름>"), true);
        case "remove":
          if (Variable.commandPacks.isEmpty())
          {
            return Collections.singletonList("유효한 명령어 팩 파일이 존재하지 않습니다.");
          }
          return Method.tabCompleterList(args, Variable.commandPacks.keySet(), "<명령어 팩 파일 이름>");
      }
    }
    else
    {
      switch (args[0])
      {
        case "execute":
        {
          if (args.length == 3)
          {
            if (Variable.commandPacks.isEmpty())
            {
              return Collections.singletonList("유효한 명령어 팩 파일이 존재하지 않습니다.");
            }
            return Method.tabCompleterList(args, Variable.commandPacks.keySet(), "<명령어 팩 파일 이름>");
          }
          if (args.length == 4)
          {
            if (Variable.commandPacks.isEmpty())
            {
              return Collections.singletonList("유효한 명령어 팩 파일이 존재하지 않습니다.");
            }
            YamlConfiguration config = Variable.commandPacks.get(args[2]);
            if (config == null)
            {
              return Collections.singletonList(args[2] + MessageUtil.getFinalConsonant(args[2], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 명령어 팩 파일입니다.");
            }
            return Method.tabCompleterList(args, config.getKeys(false), "<명령어 팩>");
          }
        }
        case "edit":
        {
          YamlConfiguration config = Variable.commandPacks.get(args[1]);
          if (args.length == 3)
          {
            if (Variable.commandPacks.isEmpty())
            {
              return Method.tabCompleterList(args, "<새로운 명령어 팩 이름>", true);
            }
            if (config == null)
            {
              return Method.tabCompleterList(args, "<새로운 명령어 팩 이름>", true);
            }
            return Method.tabCompleterList(args, config.getKeys(false), (config.getKeys(false).contains(lastArg) ? "<편집할 명령어 팩 이름>" : "<새로운 명령어 팩 이름>"), true);
          }
          if (args.length == 4)
          {
            return Method.tabCompleterList(args, "<인수>", "list", "add", "remove", "set", "insert");
          }
          if (args.length == 5)
          {
            switch (args[3])
            {
              case "set":
                return Method.tabCompleterIntegerRadius(args, 1, Integer.MAX_VALUE, "<줄>");
              case "insert":
              {
                if (config == null)
                {
                  return Collections.singletonList("명령어를 들여쓸 수 없습니다.");
                }
                List<String> commands = config.getStringList(args[2]);
                if (commands.isEmpty())
                {
                  return Collections.singletonList("명령어를 들여쓸 수 없습니다.");
                }
                return Method.tabCompleterIntegerRadius(args, 1, commands.size(), "<줄>");
              }
              case "remove":
              {
                if (config == null)
                {
                  return Collections.singletonList("제거할 명령어가 없습니다.");
                }
                List<String> commands = config.getStringList(args[2]);
                if (commands.isEmpty())
                {
                  return Collections.singletonList("제거할 명령어가 없습니다.");
                }
                return Method.tabCompleterIntegerRadius(args, 1, commands.size(), "[줄]", "--all");
              }
            }
          }

          if (Method.equals(args[3], "add") || args.length >= 6 && Method.equals(args[3], "set", "insert"))
          {
            int argLength = 5;
            boolean insertOrSet = args[3].equals("insert") || args[3].equals("set");
            if (insertOrSet)
            {
              argLength++;
            }
            if (length == argLength)
            {
              List<String> cmds = Method.getAllServerCommands();
              List<String> newCmds = new ArrayList<>();
              newCmds.add("chat:" + "<채팅 메시지>");
              newCmds.add("opchat:" + "<오피 권한으로 채팅 메시지>");
              newCmds.add("chat:/" + "<채팅 명령어>");
              newCmds.add("opchat:/" + "<오피 권한으로 채팅 명령어>");
              for (String cmd2 : cmds)
              {
                newCmds.add(cmd2);
                newCmds.add("chat:/" + cmd2);
                newCmds.add("op:" + cmd2);
                newCmds.add("opchat:/" + cmd2);
                newCmds.add("console:" + cmd2);
              }
              List<String> list = new ArrayList<>(Method.tabCompleterList(args, Variable.commandPacks.keySet(), "<명령어 팩 파일>", true));
              for (int i = 0; i < list.size(); i++)
              {
                String fileName = list.get(i);
                newCmds.add("commandpack:" + fileName);
                list.set(i, "commandpack:" + fileName);
              }
              return Method.tabCompleterList(args, newCmds, "<명령어>", true);
            }
            else
            {
              String cmdLabel = args[argLength - 1];
              if (cmdLabel.startsWith("op:"))
              {
                cmdLabel = cmdLabel.substring(3);
              }
              if (cmdLabel.startsWith("chat:/"))
              {
                cmdLabel = cmdLabel.substring(6);
              }
              else if (cmdLabel.startsWith("chat:"))
              {
                return Collections.singletonList("[메시지]");
              }
              if (cmdLabel.startsWith("opchat:/"))
              {
                cmdLabel = cmdLabel.substring(8);
              }
              else if (cmdLabel.startsWith("opchat:"))
              {
                return Collections.singletonList("[메시지]");
              }
              if (cmdLabel.startsWith("console:"))
              {
                cmdLabel = cmdLabel.substring(8);
              }
              if (length == argLength + 1 && (cmdLabel.equals("?") || cmdLabel.equals("bukkit:?") || cmdLabel.equals("bukkit:help")))
              {
                return Method.tabCompleterList(args, Method.getAllServerCommands(), "<명령어>");
              }
              PluginCommand command = Bukkit.getServer().getPluginCommand(cmdLabel);
              String[] args2 = new String[length - argLength];
              System.arraycopy(args, argLength, args2, 0, length - argLength);
              if (command != null)
              {
                org.bukkit.command.TabCompleter completer = command.getTabCompleter();
                if (completer != null)
                {
                  return completer.onTabComplete(sender, command, command.getLabel(), args2);
                }
              }
              return Collections.singletonList("[<인수>]");
            }
          }
        }
      }
    }

    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}

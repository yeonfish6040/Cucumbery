package com.jho5245.cucumbery.commands.sound;

import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.MessageUtil.ConsonantType;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.SelectorUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import com.jho5245.cucumbery.util.storage.component.util.sendercomponent.SenderComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandPlaySound implements CommandExecutor, TabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    String usage = cmd.getUsage().replace("/<command> ", "");
    switch (cmd.getName().toLowerCase())
    {
      case "playsoundall" -> {
        if (!Method.hasPermission(sender, Permission.CMD_PLAYSOUNDALL, true))
        {
          return true;
        }
        if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
        {
          return !(sender instanceof BlockCommandSender);
        }
        if (args.length < 1)
        {
          MessageUtil.shortArg(sender, 1, args);
          MessageUtil.commandInfo(sender, label, usage);
          return true;
        }
        else if (args.length <= 5)
        {
          Sound sound = null;
          SoundCategory category = SoundCategory.MASTER;
          float volume = 1F, pitch = 1F;
          boolean hideOutput = false;
          if (args.length == 5)
          {
            if (!args[4].equals("true") && !args[4].equals("false"))
            {
              MessageUtil.wrongBool(sender, 5, args);
              return true;
            }
            if (args[4].equals("true"))
            {
              hideOutput = true;
            }
          }
          boolean isCustomSound = false;
          String customSound = null;
          try
          {
            sound = Sound.valueOf(args[0].toUpperCase());
          }
          catch (Exception e)
          {
            isCustomSound = true;
            customSound = args[0];
          }
          if (args.length >= 2)
          {
            try
            {
              category = SoundCategory.valueOf(args[1].toUpperCase());
            }
            catch (Exception ignored)
            {
            }
          }
          if (args.length >= 3)
          {
            try
            {
              volume = Float.parseFloat(args[2]);
            }
            catch (Exception ignored)
            {
            }
          }
          if (args.length >= 4)
          {
            try
            {
              pitch = Float.parseFloat(args[3]);
            }
            catch (Exception ignored)
            {
            }
          }
          try
          {
            if (isCustomSound)
            {
              SoundPlay.playSoundGlobally(customSound, category, volume, pitch);
            }
            else
            {
              SoundPlay.playSoundGlobally(sound, category, volume, pitch);
            }
            if (!hideOutput)
            {
              String s = Constant.THE_COLOR_HEX + ((isCustomSound) ? customSound : sound.toString().toLowerCase());
              MessageUtil.broadcastPlayer(
                      Prefix.INFO_SOUND, "%s이(가) 모두에게 %s을(를) 재생했습니다", sender, s);
              MessageUtil.sendMessage(sender, Prefix.INFO_SOUND, "모든 플레이어에게 %s을(를) 재생했습니다", s);
            }
          }
          catch (Exception e)
          {
            MessageUtil.sendError(sender, "소리 재생에 실패했습니다");
            MessageUtil.sendMessage(sender, Prefix.INFO_SOUND, "소리 이름의 정규식은 rg255,204;[a-z0-9/._-]&r입니다");
            return true;
          }
        }
        else
        {
          MessageUtil.longArg(sender, 5, args);
          MessageUtil.commandInfo(sender, label, usage);
          return true;
        }
      }
      case "playsoundall2" -> {
        if (!Method.hasPermission(sender, Permission.CMD_PLAYSOUNDALL2, true))
        {
          return true;
        }
        if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
        {
          return !(sender instanceof BlockCommandSender);
        }
        if (args.length < 1)
        {
          MessageUtil.shortArg(sender, 1, args);
          MessageUtil.commandInfo(sender, label, usage);
          return true;
        }
        else if (args.length <= 5)
        {
          Sound sound = null;
          SoundCategory category = SoundCategory.MASTER;
          float volume = 1F, pitch = 1F;
          boolean hideOutput = false;
          if (args.length == 5)
          {
            if (!args[4].equals("true") && !args[4].equals("false"))
            {
              MessageUtil.wrongBool(sender, 5, args);
              return true;
            }
            if (args[4].equals("true"))
            {
              hideOutput = true;
            }
          }
          boolean isCustomSound = false;
          String customSound = null;
          try
          {
            sound = Sound.valueOf(args[0].toUpperCase());
          }
          catch (Exception e)
          {
            isCustomSound = true;
            customSound = args[0];
          }
          if (args.length >= 2)
          {
            try
            {
              category = SoundCategory.valueOf(args[1].toUpperCase());
            }
            catch (Exception ignored)
            {
            }
          }
          if (args.length >= 3)
          {
            try
            {
              volume = Float.parseFloat(args[2]);
            }
            catch (Exception ignored)
            {
            }
          }
          if (args.length >= 4)
          {
            try
            {
              pitch = Float.parseFloat(args[3]);
            }
            catch (Exception ignored)
            {
            }
          }

          for (Player player : Bukkit.getServer().getOnlinePlayers())
          {
            if (UserData.LISTEN_GLOBAL.getBoolean(player.getUniqueId()) || UserData.LISTEN_GLOBAL_FORCE.getBoolean(player.getUniqueId()))
            {
              try
              {
                if (isCustomSound)
                {
                  SoundPlay.playSound(player, customSound, category, volume, pitch);
                }
                else
                {
                  SoundPlay.playSound(player, sound, category, volume, pitch);
                }
                if (!hideOutput)
                {
                  MessageUtil.sendMessage(player,
                          Prefix.INFO_SOUND, SenderComponentUtil.senderComponent(sender), "이 당신에게 rg255,204;" + ((isCustomSound) ? customSound : sound.toString()) + "&r"
                                  + MessageUtil.getFinalConsonant(((isCustomSound) ? customSound : sound.toString()), ConsonantType.을를) + " 재생했습니다");
                }
              }
              catch (Exception e)
              {
                MessageUtil.sendError(sender, "소리 재생에 실패했습니다");
                MessageUtil.sendMessage(sender, Prefix.INFO_SOUND, "소리 이름의 정규식은 rg255,204;[a-z0-9/._-]&r입니다");
                return true;
              }
            }
          }
          if (!hideOutput)
          {
            MessageUtil.sendMessage(sender, Prefix.INFO_SOUND, "서버 라디오 들음을 활성화한 모든 플레이어에게 rg255,204;" + ((isCustomSound) ? customSound : sound.toString())
                    + "&r" + MessageUtil.getFinalConsonant(((isCustomSound) ? customSound : sound.toString()), ConsonantType.을를) + " 재생했습니다");
          }
        }
        else
        {
          MessageUtil.longArg(sender, 5, args);
          MessageUtil.commandInfo(sender, label, usage);
          return true;
        }
      }
      case "cplaysound" -> {
        args = MessageUtil.wrapWithQuote(args);
        if (!Method.hasPermission(sender, Permission.CMD_PLAYSOUND, true))
        {
          return true;
        }
        if (args.length < 2)
        {
          MessageUtil.shortArg(sender, 1, args);
          MessageUtil.commandInfo(sender, label, usage);
          return true;
        }
        else if (args.length <= 6)
        {
          Player target = SelectorUtil.getPlayer(sender, args[0]);
          if (target == null)
          {
            return true;
          }
          Sound sound = null;
          SoundCategory category = SoundCategory.MASTER;
          float volume = 1F, pitch = 1F;
          boolean hideOutput = false;
          if (args.length == 6)
          {
            if (!args[5].equals("true") && !args[5].equals("false"))
            {
              MessageUtil.wrongBool(sender, 6, args);
              return true;
            }
            if (args[5].equals("true"))
            {
              hideOutput = true;
            }
          }
          boolean isCustomSound = false;
          String customSound = null;
          try
          {
            sound = Sound.valueOf(args[1].toUpperCase());
          }
          catch (Exception e)
          {
            isCustomSound = true;
            customSound = args[1];
          }
          if (args.length >= 3)
          {
            try
            {
              category = SoundCategory.valueOf(args[2].toUpperCase());
            }
            catch (Exception ignored)
            {
            }
          }
          if (args.length >= 4)
          {
            try
            {
              volume = Float.parseFloat(args[3]);
            }
            catch (Exception ignored)
            {
            }
          }
          if (args.length >= 5)
          {
            try
            {
              pitch = Float.parseFloat(args[4]);
            }
            catch (Exception ignored)
            {
            }
          }
          if (isCustomSound)
          {
            try
            {
              SoundPlay.playSound(target, customSound, category, volume, pitch);
            }
            catch (Exception e)
            {
              MessageUtil.sendError(sender, "소리 재생에 실패했습니다");
              MessageUtil.sendMessage(sender, Prefix.INFO_SOUND, "소리 이름의 정규식은 rg255,204;[a-z0-9/._-]&r입니다");
              e.printStackTrace();
              return true;
            }
          }
          else
          {
            SoundPlay.playSound(target, sound, category, volume, pitch);
          }
          if (!hideOutput)
          {
            MessageUtil.sendMessage(target,
                    Prefix.INFO_SOUND, SenderComponentUtil.senderComponent(sender), "이 당신에게 rg255,204;" + ((isCustomSound) ? customSound : sound.toString()) + "&r"
                            + MessageUtil.getFinalConsonant(((isCustomSound) ? customSound : sound.toString()), ConsonantType.을를) + " 재생했습니다");
            MessageUtil.sendMessage(sender,
                    Prefix.INFO_SOUND, SenderComponentUtil.senderComponent(target), "&r에게 rg255,204;" + ((isCustomSound) ? customSound : sound.toString()) + "&r"
                            + MessageUtil.getFinalConsonant(((isCustomSound) ? customSound : sound.toString()), ConsonantType.을를) + " 재생했습니다");
          }
        }
        else
        {
          MessageUtil.longArg(sender, 6, args);
          MessageUtil.commandInfo(sender, label, usage);
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
    String name = cmd.getName();
    int length = args.length;

    if (name.equals("playsoundall") || name.equals("playsoundall2"))
    {
      if (length == 1)
      {
        return Method.tabCompleterList(args, Sound.values(), "<소리>", true);
      }
      else if (length == 2)
      {
        return Method.tabCompleterList(args, SoundCategory.values(), "[소리 유형]");
      }
      else if (length == 3)
      {
        return Method.tabCompleterDoubleRadius(args, 0, Float.MAX_VALUE, "[음량]");
      }
      else if (length == 4)
      {
        return Method.tabCompleterDoubleRadius(args, 0.5, 2, "[음색]");
      }
      else if (length == 5)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("cplaysound") && Method.hasPermission(sender, Permission.CMD_PLAYSOUNDALL, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else if (length == 2)
      {
        return Method.tabCompleterList(args, Sound.values(), "<소리>", true);
      }
      else if (length == 3)
      {
        return Method.tabCompleterList(args, SoundCategory.values(), "[소리 유형]");
      }
      else if (length == 4)
      {
        return Method.tabCompleterDoubleRadius(args, 0, Float.MAX_VALUE, "[음량]");
      }
      else if (length == 5)
      {
        return Method.tabCompleterDoubleRadius(args, 0.5, 2, "[음색]");
      }
      else if (length == 6)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }

    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}

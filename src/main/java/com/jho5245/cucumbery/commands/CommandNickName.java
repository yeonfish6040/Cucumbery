package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.MessageUtil.ConsonantType;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.CustomConfig;
import com.jho5245.cucumbery.util.storage.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.component.util.sendercomponent.SenderComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

public class CommandNickName implements CommandExecutor, TabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_NICK, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    if (!Cucumbery.config.getBoolean("use-nickname-feature"))
    {
      MessageUtil.sendError(sender, "닉네임 기능이 비활성화 되어 있습니다. config에서 &euse-nickname-feature&r 값을 &etrue&r로 설정해주세요.");
      return true;
    }
    String usage = Method.getUsage(cmd);
    if (cmd.getName().equalsIgnoreCase("nickname"))
    {
      if (!(sender instanceof Player))
      {
        MessageUtil.sendError(sender, Prefix.ONLY_PLAYER);
        MessageUtil.commandInfo(sender, "nicknameothers", Objects.requireNonNull(Cucumbery.getPlugin().getCommand("nicknameothers")).getUsage().replace("/<command>", ""));
        return true;
      }
      if (args.length < 2)
      {
        MessageUtil.shortArg(sender, 2, args);
        MessageUtil.commandInfo(sender, label, usage);
        return true;
      }
      else
      {
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        String inputName = MessageUtil.listToString(" ", 1, args.length, args);
        if (inputName.length() > 4096)
        {
          MessageUtil.sendError(player, "너무 긴 닉네임입니다. 최대 &e4096자&r까지 입력할 수 있으나, &e" + inputName.length() + "자&r가 입력되었습니다.");
          return true;
        }
        Component nickName;
        if (inputName.equals("--item"))
        {
          ItemStack item = player.getInventory().getItemInMainHand();
          if (!ItemStackUtil.itemExists(item))
          {
            nickName = ComponentUtil.create(inputName);
          }
          else
          {
            nickName = ItemNameUtil.itemName(item);
          }
        }
        else
        {
          nickName = ComponentUtil.create(inputName);
        }

        boolean off = args.length == 2 && args[1].equalsIgnoreCase("--off");
        Component finalNickname = off ? Component.text(player.getName()) : nickName;
        Component senderComponent = SenderComponentUtil.senderComponent(player);
        finalNickname = finalNickname.hoverEvent(senderComponent.hoverEvent()).clickEvent(senderComponent.clickEvent());
        String serialNickname = off ? null : ComponentUtil.serializeAsJson(nickName);
        String originDisplay = MessageUtil.stripColor(ComponentUtil.serialize(player.displayName())), originList = MessageUtil.stripColor(ComponentUtil.serialize(player.playerListName()));
        String type;
        if (args[0].equalsIgnoreCase("all"))
        {
          type = "모든";
          player.displayName(finalNickname);
          if (Cucumbery.using_Vault_Chat)
          {
            try
            {
              String prefix = Cucumbery.chat.getPlayerPrefix(player), suffix = Cucumbery.chat.getPlayerSuffix(player);
              if (prefix != null)
              {
                finalNickname = ComponentUtil.create(false, prefix, finalNickname);
              }
              if (suffix != null)
              {
                finalNickname = ComponentUtil.create(false, finalNickname, suffix);
              }
            }
            catch (Exception e)
            {
              e.printStackTrace();
            }
          }
          player.playerListName(finalNickname);
          UserData.DISPLAY_NAME.set(uuid, serialNickname);
          UserData.PLAYER_LIST_NAME.set(uuid, serialNickname);
        }
        else if (args[0].equalsIgnoreCase("display"))
        {
          type = "채팅";
          player.displayName(finalNickname);
          UserData.DISPLAY_NAME.set(uuid, serialNickname);
        }
        else if (args[0].equalsIgnoreCase("list"))
        {
          type = "목록";
          if (Cucumbery.using_Vault_Chat)
          {
            try
            {
              String prefix = Cucumbery.chat.getPlayerPrefix(player), suffix = Cucumbery.chat.getPlayerSuffix(player);
              if (prefix != null)
              {
                finalNickname = ComponentUtil.create(false, prefix, finalNickname);
              }
              if (suffix != null)
              {
                finalNickname = ComponentUtil.create(false, finalNickname, suffix);
              }
            }
            catch (Exception e)
            {
              e.printStackTrace();
            }
          }
          player.playerListName(finalNickname);
          UserData.PLAYER_LIST_NAME.set(uuid, serialNickname);
        }
        else
        {
          MessageUtil.wrongArg(sender, 1, args);
          MessageUtil.commandInfo(sender, label, usage);
          return true;
        }
        if (off)
        {
          MessageUtil.sendMessage(player, Prefix.INFO_NICK, type + " 닉네임§r을 초기화 하였습니다.");
        }
        else
        {
          MessageUtil.sendMessage(player, false, Prefix.INFO_NICK, "§e" + type + " 닉네임§r을 ", nickName, "§r" +
                  MessageUtil.getFinalConsonant(ComponentUtil.serialize(nickName), ConsonantType.으로) + " 변경" + "하였습니다.");
        }
        Variable.nickNames.remove(originDisplay);
        Variable.nickNames.remove(originList);
        Variable.cachedUUIDs.remove(originDisplay);
        Variable.cachedUUIDs.remove(originList);
        Variable.nickNames.add(uuid.toString());
        Variable.nickNames.add(player.getName());
        Variable.nickNames.add(MessageUtil.stripColor(ComponentUtil.serialize(player.displayName())));
        Variable.nickNames.add(MessageUtil.stripColor(ComponentUtil.serialize(player.playerListName())));
        Variable.cachedUUIDs.put(player.getName(), uuid);
        Variable.cachedUUIDs.put(MessageUtil.stripColor(ComponentUtil.serialize(player.displayName())), uuid);
        Variable.cachedUUIDs.put(MessageUtil.stripColor(ComponentUtil.serialize(player.playerListName())), uuid);
        File nickNamesFile = new File(Cucumbery.getPlugin().getDataFolder() + "/data/Nicknames.yml");
        if (!nickNamesFile.exists())
        {
          boolean success = nickNamesFile.getParentFile().mkdirs();
          if (!success)
          {
            System.err.println("[Cucumbery] could not create NickNames.yml file!");
          }
        }
        CustomConfig nickNamesCustonConfig = CustomConfig.getCustomConfig(nickNamesFile);
        YamlConfiguration nickNamesConfig = nickNamesCustonConfig.getConfig();
        List<String> nicks = new ArrayList<>(Variable.nickNames);
        nickNamesConfig.set("nicks", nicks);
        nickNamesCustonConfig.saveConfig();
      }
    }
    else if (cmd.getName().equalsIgnoreCase("nicknameothers"))
    {
      if (args.length < 4)
      {
        MessageUtil.shortArg(sender, 4, args);
        MessageUtil.commandInfo(sender, label, usage);
        return true;
      }
      OfflinePlayer offlinePlayer = SelectorUtil.getOfflinePlayer(sender, args[0]);
      if (offlinePlayer == null)
      {
        return true;
      }
      UUID uuid = offlinePlayer.getUniqueId();
      Player target = offlinePlayer.getPlayer();
      boolean isOnline = target != null && offlinePlayer.isOnline();
      boolean hideMessage = args[2].equals("true");
      String nickName = MessageUtil.listToString(" ", 3, args.length, args);
      nickName = MessageUtil.listToString(MessageUtil.splitEscape(nickName, '+'));
      if (nickName.length() > 4096)
      {
        MessageUtil.sendError(sender, "너무 긴 닉네임입니다. 최대 &e4096자&r까지 입력할 수 있으나, &e" + nickName.length() + "자&r가 입력되었습니다.");
        return true;
      }
      String originDisplay = UserData.DISPLAY_NAME.getString(uuid), originList = UserData.PLAYER_LIST_NAME.getString(uuid);
      String inputName = nickName;
      nickName = nickName + "§r";
      String prefix;
      boolean off = args.length == 4 && args[3].equalsIgnoreCase("--off");
      if (args[1].equalsIgnoreCase("all"))
      {
        prefix = "모든";
        if (isOnline)
        {
          target.displayName(off ? null : ComponentUtil.create(nickName));
          target.playerListName(off ? null : ComponentUtil.create(nickName));
        }
        UserData.DISPLAY_NAME.set(uuid, off ? null : inputName);
        UserData.PLAYER_LIST_NAME.set(uuid, off ? null : inputName);
        if (originDisplay != null)
        {
          Variable.nickNames.remove(originDisplay);
          Variable.cachedUUIDs.remove(originDisplay);
        }
        if (originList != null)
        {
          Variable.nickNames.remove(originList);
          Variable.cachedUUIDs.remove(originList);
        }
        if (!off)
        {
          Variable.nickNames.add(MessageUtil.stripColor(inputName));
          Variable.cachedUUIDs.put(MessageUtil.stripColor(inputName), uuid);
        }
      }
      else if (args[1].equalsIgnoreCase("display"))
      {
        prefix = "채팅";
        if (isOnline)
        {
          target.displayName(off ? null : ComponentUtil.create(nickName));
        }
        UserData.DISPLAY_NAME.set(uuid, off ? null : inputName);
        Variable.nickNames.remove(originDisplay);
        Variable.cachedUUIDs.remove(originDisplay);
        if (!off)
        {
          Variable.nickNames.add(MessageUtil.stripColor(inputName));
          Variable.cachedUUIDs.put(MessageUtil.stripColor(inputName), uuid);
          Variable.nickNames.add(originList);
          Variable.cachedUUIDs.put(originList, uuid);
        }
      }
      else if (args[1].equalsIgnoreCase("list"))
      {
        prefix = "목록";
        if (isOnline)
        {
          target.playerListName(off ? null : ComponentUtil.create(nickName));
        }
        UserData.PLAYER_LIST_NAME.set(uuid, off ? null : inputName);
        Variable.nickNames.remove(originList);
        Variable.cachedUUIDs.remove(originList);
        if (!off)
        {
          Variable.nickNames.add(MessageUtil.stripColor(inputName));
          Variable.cachedUUIDs.put(MessageUtil.stripColor(inputName), uuid);
          Variable.nickNames.add(originDisplay);
          Variable.cachedUUIDs.put(originDisplay, uuid);
        }
      }
      else
      {
        MessageUtil.wrongArg(sender, 2, args);
        MessageUtil.commandInfo(sender, label, usage);
        return true;
      }
      if (!hideMessage)
      {
        if (target != null && !target.equals(sender))
        {
          MessageUtil.sendMessage(target, Prefix.INFO_NICK, sender, "이(가) 당신의 §e" + prefix + " 닉네임§r을 " +
                  ((off) ? "초기화" : "§e" + inputName + "§r" + MessageUtil.getFinalConsonant(inputName, ConsonantType.으로) + " 변경") + "하였습니다.", false);
        }
        MessageUtil.sendMessage(sender, Prefix.INFO_NICK, offlinePlayer, "§r의 §e" + prefix + " 닉네임§r을 " + ((off) ? "초기화" :
                "§e" + inputName + "§r" + MessageUtil.getFinalConsonant(
                        inputName, ConsonantType.으로) + " 변경") + "하였습니다.", false);
      }
      File nickNamesFile = new File(Cucumbery.getPlugin().getDataFolder() + "/data/Nicknames.yml");
      if (!nickNamesFile.exists())
      {
        boolean success = nickNamesFile.getParentFile().mkdirs();
        if (!success)
        {
          System.err.println("[Cucumbery] could not create NickNames.yml file!");
        }
      }
      CustomConfig nickNamesCustonConfig = CustomConfig.getCustomConfig(nickNamesFile);
      YamlConfiguration nickNamesConfig = nickNamesCustonConfig.getConfig();
      List<String> nicks = new ArrayList<>(Variable.nickNames);
      nickNamesConfig.set("nicks", nicks);
      nickNamesCustonConfig.saveConfig();
    }
    return true;
  }


  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Cucumbery.config.getBoolean("use-nickname-feature"))
    {
      return Collections.singletonList("닉네임 기능이 비활성화 되어 있습니다.");
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(true, args), true))
    {
      return Collections.singletonList(args[0]);
    }
    int length = args.length;
    String name = cmd.getName();
    switch (name)
    {
      case "nickname" -> {
        if (!(sender instanceof Player))
        {
          return Collections.singletonList(Prefix.ONLY_PLAYER.toString());
        }
        if (length == 1)
        {
          return Method.tabCompleterList(args, "<닉네임 유형>", "all", "display", "list");
        }
        else if (length == 2)
        {
          return Method.tabCompleterList(args, "<닉네임>", true, "<닉네임>", "--off");
        }
      }
      case "nicknameothers" -> {
        if (length == 1)
        {
          if (label.equals("cnicks"))
          {
            return Method.tabCompleterPlayer(sender, args);
          }
          return Method.tabCompleterOfflinePlayer(sender, args);
        }
        else if (length == 2)
        {
          return Method.tabCompleterList(args, "<닉네임 유형>", "all", "display", "list");
        }
        else if (length == 3)
        {
          return Method.tabCompleterBoolean(args, "<명령어 출력 숨김 여부>");
        }
        else if (length == 4)
        {
          return Method.tabCompleterList(args, "<닉네임>", true, "<닉네임>", "--off");
        }
      }
    }
    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}
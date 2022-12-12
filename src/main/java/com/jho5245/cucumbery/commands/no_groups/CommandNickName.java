package com.jho5245.cucumbery.commands.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.SelectorUtil;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
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
      MessageUtil.sendError(sender, "닉네임 기능이 비활성화 되어 있습니다. config에서 rg255,204;use-nickname-feature&r 값을 rg255,204;true&r로 설정해주세요.");
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
          MessageUtil.sendError(player, "너무 긴 닉네임입니다. 최대 rg255,204;4096자&r까지 입력할 수 있으나, rg255,204;" + inputName.length() + "자&r가 입력되었습니다");
          return true;
        }
        Component nickName;
        if (inputName.equals("--item"))
        {
          ItemStack item = player.getInventory().getItemInMainHand();
          if (!ItemStackUtil.itemExists(item))
          {
            nickName = ComponentUtil.create(false, inputName);
          }
          else
          {
            nickName = ItemNameUtil.itemName(item);
          }
        }
        else
        {
          nickName = ComponentUtil.create(false, inputName);
        }

        boolean off = args.length == 2 && args[1].equalsIgnoreCase("--off");
        Component finalNickname = off ? Component.text(player.getName()) : nickName;
        Component senderComponent = SenderComponentUtil.senderComponent(player, player, null);
        finalNickname = finalNickname.hoverEvent(senderComponent.hoverEvent()).clickEvent(senderComponent.clickEvent());
        String serialNickname = off ? null : ComponentUtil.serializeAsJson(nickName);
        String originDisplay = MessageUtil.stripColor(ComponentUtil.serialize(player.displayName())), originList = MessageUtil.stripColor(ComponentUtil.serialize(player.playerListName()));
        String type;
        if (args[0].equalsIgnoreCase("all"))
        {
          type = "모든";
          player.displayName(finalNickname);
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
          player.playerListName(finalNickname);
          UserData.PLAYER_LIST_NAME.set(uuid, serialNickname);
        }
        else
        {
          MessageUtil.wrongArg(sender, 1, args);
          MessageUtil.commandInfo(sender, label, usage);
          return true;
        }
        senderComponent = SenderComponentUtil.senderComponent(player, player, null);
        player.displayName(senderComponent);
        final Component originalDisplayName = player.displayName();
        player.displayName(player.playerListName());
        senderComponent = SenderComponentUtil.senderComponent(player, player, null);
        player.playerListName(senderComponent.hoverEvent(null).clickEvent(null).insertion(null));
        player.displayName(originalDisplayName);
        finalNickname = senderComponent;
        if (off)
        {
          MessageUtil.sendMessage(player, Prefix.INFO_NICK, "%s을 초기화 했습니다", type + " 닉네임");
        }
        else
        {
          MessageUtil.sendMessage(player, Prefix.INFO_NICK, "%s을 %s(으)로 변경했습니다", type + " 닉네임", finalNickname);
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
      boolean off = args.length == 4 && args[3].equalsIgnoreCase("--off");
      Component finalNickname = off ? null : ComponentUtil.create(false, nickName);
      String nicknameSerialJson = off ? null : ComponentUtil.serializeAsJson(finalNickname);
      String nicknameSerial = off ? null : ComponentUtil.serialize(finalNickname);
      if (nickName.length() > 4096)
      {
        MessageUtil.sendError(sender, "너무 긴 닉네임입니다. 최대 rg255,204;4096자&r까지 입력할 수 있으나, rg255,204;" + nickName.length() + "자&r가 입력되었습니다");
        return true;
      }
      String originDisplay = UserData.DISPLAY_NAME.getString(uuid), originList = UserData.PLAYER_LIST_NAME.getString(uuid);
      if (originDisplay != null)
      {
        originDisplay = ComponentUtil.serialize(ComponentUtil.create(originDisplay));
      }
      if (originList != null)
      {
        originList = ComponentUtil.serialize(ComponentUtil.create(originList));
      }
      String type;
      if (args[1].equalsIgnoreCase("all"))
      {
        type = "모든";
        if (isOnline)
        {
          target.displayName(finalNickname);
          target.playerListName(finalNickname);
        }
        UserData.DISPLAY_NAME.set(uuid, nicknameSerialJson);
        UserData.PLAYER_LIST_NAME.set(uuid, nicknameSerialJson);
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
          Variable.nickNames.add(nicknameSerial);
          Variable.cachedUUIDs.put(nicknameSerial, uuid);
        }
      }
      else if (args[1].equalsIgnoreCase("display"))
      {
        type = "채팅";
        if (isOnline)
        {
          target.displayName(finalNickname);
        }
        UserData.DISPLAY_NAME.set(uuid, nicknameSerialJson);
        Variable.nickNames.remove(originDisplay);
        Variable.cachedUUIDs.remove(originDisplay);
        if (!off)
        {
          Variable.nickNames.add(nicknameSerial);
          Variable.cachedUUIDs.put(nicknameSerial, uuid);
          Variable.nickNames.add(originList);
          Variable.cachedUUIDs.put(originList, uuid);
        }
      }
      else if (args[1].equalsIgnoreCase("list"))
      {
        type = "목록";
        if (isOnline)
        {
          target.playerListName(finalNickname);
        }
        UserData.PLAYER_LIST_NAME.set(uuid, nicknameSerialJson);
        Variable.nickNames.remove(originList);
        Variable.cachedUUIDs.remove(originList);
        if (!off)
        {
          Variable.nickNames.add(nicknameSerial);
          Variable.cachedUUIDs.put(nicknameSerial, uuid);
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
      if (target != null)
      {
        Component senderComponent = SenderComponentUtil.senderComponent(target, target, null);
        target.displayName(senderComponent);
        final Component originalDisplayName = target.displayName();
        target.displayName(target.playerListName());
        senderComponent = SenderComponentUtil.senderComponent(target, target, null);
        target.playerListName(senderComponent.hoverEvent(null).clickEvent(null).insertion(null));
        target.displayName(originalDisplayName);
        finalNickname = senderComponent;
      }
      if (!hideMessage)
      {
        if (target != null && !target.equals(sender))
        {
          if (off)
          {
            MessageUtil.sendMessage(target, Prefix.INFO_NICK, "%s이(가) 당신의 %s을 초기화 했습니다", sender, type + " 닉네임");
          }
          else
          {
            MessageUtil.sendMessage(target, Prefix.INFO_NICK, "%s이(가) 당신의 %s을 %s(으)로 변경했습니다", sender, type + " 닉네임", finalNickname);
          }
        }
        if (off)
        {
          MessageUtil.sendMessage(sender, Prefix.INFO_NICK, "%s의 %s을 초기화 했습니다", offlinePlayer, type + " 닉네임");
        }
        else
        {
          MessageUtil.sendMessage(sender, Prefix.INFO_NICK, "%s의 %s을 %s(으)로 변경했습니다", offlinePlayer, type + " 닉네임", finalNickname);
        }
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
      return Collections.singletonList("닉네임 기능이 비활성화 되어 있습니다");
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

package com.jho5245.cucumbery.commands.no_groups;

import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.CommandTabUtil;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandEditCommandBlock implements CommandExecutor, TabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_EDIT_COMMAND_BLOCK, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    if (!(sender instanceof Player player))
    {
      MessageUtil.sendError(sender, Prefix.ONLY_PLAYER);
      return true;
    }
    PlayerInventory playerInventory = player.getInventory();
    ItemStack item = playerInventory.getItemInMainHand();
    if (!ItemStackUtil.itemExists(item))
    {
      MessageUtil.sendError(player, Prefix.NO_HOLDING_ITEM);
      return true;
    }
    switch (item.getType())
    {
      case COMMAND_BLOCK, CHAIN_COMMAND_BLOCK, REPEATING_COMMAND_BLOCK -> {
        if (args.length < 2)
        {
          MessageUtil.shortArg(sender, 2, args);
          MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
          return true;
        }
        NBTItem nbtItem = new NBTItem(item);
        NBTCompound blockEntityTag = nbtItem.getCompound("BlockEntityTag");
        NBTCompound blockStateTag = nbtItem.getCompound(CucumberyTag.MINECRAFT_BLOCK_STATE_TAG_KEY);
        switch (args[0])
        {
          case "name" -> {
            String name = MessageUtil.listToString(" ", 1, args.length, args);
            BlockStateMeta blockStateMeta = (BlockStateMeta) item.getItemMeta();
            CommandBlock commandBlock = (CommandBlock) blockStateMeta.getBlockState();
            commandBlock.setName(name);
            blockStateMeta.setBlockState(commandBlock);
            item.setItemMeta(blockStateMeta);
            playerInventory.setItemInMainHand(item);
            ItemStackUtil.updateInventory(player);
            MessageUtil.info(player, "주로 사용하는 손에 들고 있는 rg255,204;" + ItemNameUtil.itemName(item) + "&r의 이름을 rg255,204;" + name + "&r" + MessageUtil.getFinalConsonant(name,
                    MessageUtil.ConsonantType.으로) + " 설정했습니다");
          }
          case "command" -> {
            String command = MessageUtil.listToString(" ", 1, args.length, args);
            BlockStateMeta blockStateMeta = (BlockStateMeta) item.getItemMeta();
            CommandBlock commandBlock = (CommandBlock) blockStateMeta.getBlockState();
            commandBlock.setCommand(command);
            blockStateMeta.setBlockState(commandBlock);
            item.setItemMeta(blockStateMeta);
            playerInventory.setItemInMainHand(item);
            ItemStackUtil.updateInventory(player);
            MessageUtil.info(player, "주로 사용하는 손에 들고 있는 rg255,204;" + ItemNameUtil.itemName(item) + "&r의 명령어를 rg255,204;" + command + "&r" + MessageUtil.getFinalConsonant(command,
                    MessageUtil.ConsonantType.으로) + " 설정했습니다");
          }
          case "auto" -> {
            if (args.length > 2)
            {
              MessageUtil.longArg(sender, 2, args);
              MessageUtil.commandInfo(sender, label, args[0] + " <항상 활성화>");
              return true;
            }
            if (!MessageUtil.isBoolean(sender, args, 2, true))
            {
              return true;
            }
            boolean input = Boolean.parseBoolean(args[1]);
            if (blockEntityTag == null)
            {
              blockEntityTag = nbtItem.addCompound("BlockEntityTag");
            }
            blockEntityTag.setBoolean("auto", input);
            playerInventory.setItemInMainHand(nbtItem.getItem());
            ItemStackUtil.updateInventory(player);
            MessageUtil.info(player, "주로 사용하는 손에 들고 있는 rg255,204;" + ItemNameUtil.itemName(item) + "&r의 항상 활성화 태그를 rg255,204;" + input + "&r(으)로 설정했습니다");
          }
          case "conditional" -> {
            if (args.length > 2)
            {
              MessageUtil.longArg(sender, 2, args);
              MessageUtil.commandInfo(sender, label, args[0] + " <조건적>");
              return true;
            }
            if (!MessageUtil.isBoolean(sender, args, 2, true))
            {
              return true;
            }
            boolean input = Boolean.parseBoolean(args[1]);
            if (blockStateTag == null)
            {
              blockStateTag = nbtItem.addCompound(CucumberyTag.MINECRAFT_BLOCK_STATE_TAG_KEY);
            }
            blockStateTag.setString("conditional", input + "");
            playerInventory.setItemInMainHand(nbtItem.getItem());
            ItemStackUtil.updateInventory(player);
            MessageUtil.info(player, "주로 사용하는 손에 들고 있는 rg255,204;" + ItemNameUtil.itemName(item) + "&r의 조건적 태그를 rg255,204;" + input + "&r(으)로 설정했습니다");
          }
          default -> {
            MessageUtil.wrongArg(sender, 1, args);
            return true;
          }
        }
      }
      default -> MessageUtil.sendError(player, "해당 명령어는 명령 블록에만 사용할 수 있습니다");
    }
    return true;
  }

  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!(sender instanceof Player player))
    {
      return Collections.emptyList();
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(true, args), true))
    {
      return Collections.singletonList(args[0]);
    }
    int length = args.length;

    ItemStack item = player.getInventory().getItemInMainHand();
    if (!ItemStackUtil.itemExists(item))
    {
      return Collections.singletonList(Prefix.NO_HOLDING_ITEM.toString());
    }
    switch (item.getType())
    {
      case COMMAND_BLOCK:
      case CHAIN_COMMAND_BLOCK:
      case REPEATING_COMMAND_BLOCK:
        break;
      default:
        return Collections.singletonList("해당 명령어는 명령 블록에만 사용할 수 있습니다");
    }
    if (length == 1)
    {
      return Method.tabCompleterList(args, "<인수>", "name", "command", "auto", "conditional");
    }
    else
    {
      switch (args[0])
      {
        case "command":
          return CommandTabUtil.getCommandsTabCompleter(sender, args, 2, false);
        case "name":
          if (args.length == 2)
          {
            return Method.tabCompleterList(args, "<이름>", true, "<이름>", "@");
          }
          return Method.tabCompleterList(args, "[이름]", true);
        case "auto":
          if (length == 2)
          {
            return Method.tabCompleterBoolean(args, "<항상 활성화>");
          }
        case "conditional":
          if (length == 2)
          {
            return Method.tabCompleterBoolean(args, "<조건적>");
          }
      }
    }

    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}

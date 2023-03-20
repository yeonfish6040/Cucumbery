package com.jho5245.cucumbery.commands.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.SelectorUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandSocialMenu implements CommandExecutor, TabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    if (!(sender instanceof Player player))
    {
      MessageUtil.sendError(sender, Prefix.ONLY_PLAYER);
      return true;
    }
    if (args.length != 1)
    {
      if (args.length == 0)
      {
        player.performCommand("socialmenu " + player.getName());
        return true;
      }
      MessageUtil.longArg(player, 1, args);
      MessageUtil.commandInfo(player, label, Method.getUsage(cmd));
      return true;
    }
    Player target = SelectorUtil.getPlayer(player, args[0]);
    if (target == null)
    {
      return true;
    }
    String name = target.getName();
    MessageUtil.sendMessage(player, Prefix.INFO_SOCIAL, Constant.SEPARATOR);
    MessageUtil.sendMessage(player, Prefix.INFO_SOCIAL, ComponentUtil.translate("소셜 메뉴 - %s", target));
    Component panel = Component.empty().append(Prefix.INFO_SOCIAL.get()).append(ComponentUtil.translate("     "));
    if (!player.equals(target))
    {
      panel = panel.append(
              ComponentUtil.translate("rg255,204;[귓속말 보내기]")
                      .hoverEvent(HoverEvent.showText(ComponentUtil.translate("클릭하여 %s에게 귓속말을 보냅니다 %s", target, "&7 - /w " + name + " <메시지>")))
                      .clickEvent(ClickEvent.suggestCommand("/w " + name + " "))
      );
      panel = panel.append(Component.text(" "));
      if (Cucumbery.getPlugin().getPluginManager().isPluginEnabled("Essentials"))
      {
        panel = panel.append(
                ComponentUtil.translate("&b[텔레포트 요청 보내기]")
                        .hoverEvent(HoverEvent.showText(ComponentUtil.translate("클릭하여 %s에게 텔레포트 요청을 보냅니다 %s", target, "&7/tpa " + name)))
                        .clickEvent(ClickEvent.suggestCommand("/tpa " + name))
        );
      }
    }
    else
    {
      panel = panel.append(
              ComponentUtil.translate("rg255,204;[아이템 자랑하기]")
                      .hoverEvent(HoverEvent.showText(ComponentUtil.translate("클릭하여 주로 사옹하는 손에 들고 있는 아이템을 다른 사람에게 자랑합니다 %s", "&7 - /bitem [메시지]")))
                      .clickEvent(ClickEvent.suggestCommand("/bitem "))
      );
      panel = panel.append(Component.text(" "));
      panel = panel.append(
              ComponentUtil.translate("&a[메뉴 열기]")
                      .hoverEvent(HoverEvent.showText(ComponentUtil.translate("클릭하여 메뉴를 엽니다 %s", "&7 - /menu")))
                      .clickEvent(ClickEvent.runCommand("/menu"))
      );
      panel = panel.append(Component.text(" "));
      panel = panel.append(
              ComponentUtil.translate("&a[쓰레기통 열기]")
                      .hoverEvent(HoverEvent.showText(
                              ComponentUtil.translate("클릭하여 쓰레기통을 엽니다 %s", "&7 - /trashcan")
                                      .append(Component.text("\n"))
                                      .append(ComponentUtil.translate("쓰레기통에서는 바닥에 버릴 수 없는 아이템도 버릴 수 있습니다"))
                      ))
                      .clickEvent(ClickEvent.runCommand("/trashcan"))
      );
    }
    MessageUtil.sendMessage(player, panel);
    if (player.hasPermission("asdf"))
    {
      Component adminPanel = Component.empty().append(Prefix.INFO_SOCIAL.get()).append(ComponentUtil.translate("     "));
      if (!player.equals(target))
      {
        adminPanel = adminPanel.append(
                ComponentUtil.translate("&d[정보 보기]")
                        .hoverEvent(HoverEvent.showText(ComponentUtil.translate("클릭하여 %s의 정보를 봅니다 %s", target, "&7 - /whois " + name)))
                        .clickEvent(ClickEvent.runCommand("/whois " + name))
        );
        adminPanel = adminPanel.append(Component.text(" "));
        adminPanel = adminPanel.append(
                ComponentUtil.translate("&b[텔레포트]")
                        .hoverEvent(HoverEvent.showText(ComponentUtil.translate("클릭하여 %s에게 즉시 텔레포트합니다 %s", target, "&7 - /tp " + name)))
                        .clickEvent(ClickEvent.runCommand("/tp " + name))
        );
        adminPanel = adminPanel.append(Component.text(" "));
        adminPanel = adminPanel.append(
                ComponentUtil.translate("&a[관전]")
                        .hoverEvent(HoverEvent.showText(ComponentUtil.translate("클릭하여 %s을(를) 즉시 관전합니다 %s", target, "&7 - /cspectate " + name)))
                        .clickEvent(ClickEvent.runCommand("/cspectate @s " + name + " true true true"))
        );
        adminPanel = adminPanel.append(Component.text(" "));
        adminPanel = adminPanel.append(
                ComponentUtil.translate("&a[관전 중지]")
                        .hoverEvent(HoverEvent.showText(ComponentUtil.translate("클릭하여 관전을 중지합니다 %s", "&7 - /spectate")))
                        .clickEvent(ClickEvent.runCommand("/spectate"))
        );
        MessageUtil.sendMessage(player, adminPanel);
        adminPanel = Component.empty().append(Prefix.INFO_SOCIAL.get()).append(ComponentUtil.translate("     "));
        if (Cucumbery.getPlugin().getPluginManager().getPlugin("OpenInv") != null)
        {
          adminPanel = adminPanel.append(
                  ComponentUtil.translate("&6[인벤토리 열기]")
                          .hoverEvent(HoverEvent.showText(ComponentUtil.translate("클릭하여 %s의 인벤토리를 엽니다 %s", target, "&7 - /openinv " + name)))
                          .clickEvent(ClickEvent.runCommand("/openinv " + name))
          );
        }
        adminPanel = adminPanel.append(Component.text(" "));
        adminPanel = adminPanel.append(
                ComponentUtil.translate("&4[접속 제한]")
                        .hoverEvent(HoverEvent.showText(ComponentUtil.translate("클릭하여 %s을(를) 접속 제한합니다 %s", target, "&7 - /ban " + name + " [사유]")))
                        .clickEvent(ClickEvent.suggestCommand("/ban " + name + " "))
        );
        adminPanel = adminPanel.append(Component.text(" "));
        adminPanel = adminPanel.append(
                ComponentUtil.translate("&4[강퇴]")
                        .hoverEvent(HoverEvent.showText(ComponentUtil.translate("클릭하여 %s을(를) 강퇴합니다 %s", target, "&7 - /kick " + name + " [사유]")))
                        .clickEvent(ClickEvent.suggestCommand("/kick " + name + " "))
        );
      }
      MessageUtil.sendMessage(player, adminPanel);
    }
    MessageUtil.sendMessage(player, Prefix.INFO_SOCIAL, Constant.SEPARATOR);
    return true;
  }

  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(true, args), true))
    {
      return Collections.singletonList(args[0]);
    }
    int length = args.length;

    if (length == 1)
    {
      return Method.tabCompleterPlayer(sender, args);
    }

    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}








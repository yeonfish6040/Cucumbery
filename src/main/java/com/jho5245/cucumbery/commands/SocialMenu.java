package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SocialMenu implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!(sender instanceof Player player))
    {
      MessageUtil.sendError(sender, Prefix.ONLY_PLAYER);
      return true;
    }
    if (args.length != 1)
    {
      if (args.length == 0)
      MessageUtil.shortArg(player, 1, args);
      if (args.length > 1)
        MessageUtil.longArg(player, 1, args);
      MessageUtil.commandInfo(player, label, Method.getUsage(cmd));
      return true;
    }
    Player target = Method.getPlayer(player, args[0]);
    if (target == null || player.equals(target))
      return true;
    String name = target.getName();
    MessageUtil.sendMessage(player, Prefix.INFO_SOCIAL, ComponentUtil.create2("&m&l                                              "));
    MessageUtil.sendMessage(player, Prefix.INFO_SOCIAL, ComponentUtil.createTranslate("소셜 메뉴 - %s", target));
    Component panel = Component.empty().append(Prefix.INFO_SOCIAL.get()).append(Component.translatable("     "));
    panel = panel.append(
            ComponentUtil.createTranslate("&e[귓속말 보내기]")
                    .hoverEvent(HoverEvent.showText(ComponentUtil.createTranslate("클릭하여 %s에게 귓속말을 보냅니다. %s", target, "&7 - /msg " + name + " <메시지>")))
                    .clickEvent(ClickEvent.suggestCommand("/msg " + name + " "))
    );
    panel = panel.append(Component.text(" "));
    if (Cucumbery.getPlugin().getPluginManager().isPluginEnabled("Essentials"))
    {
      panel = panel.append(
              ComponentUtil.createTranslate("&b[텔레포트 요청 보내기]")
                      .hoverEvent(HoverEvent.showText(ComponentUtil.createTranslate("클릭하여 %s에게 텔레포트 요청을 보냅니다. %s", target, "&7/tpa " + name)))
                      .clickEvent(ClickEvent.suggestCommand("/tpa " + name))
      );
    }
    MessageUtil.sendMessage(player, panel);
    if (player.hasPermission("asdf"))
    {
      Component adminPanel = Component.empty().append(Prefix.INFO_SOCIAL.get()).append(Component.translatable("     "));
      adminPanel = adminPanel.append(
              ComponentUtil.createTranslate("&d[정보 보기]")
                      .hoverEvent(HoverEvent.showText(ComponentUtil.createTranslate("클릭하여 %s의 정보를 봅니다. %s", target, "&7 - /whois " + name)))
                      .clickEvent(ClickEvent.runCommand("/whois " + name))
      );
      adminPanel = adminPanel.append(Component.text(" "));
      adminPanel = adminPanel.append(
              ComponentUtil.createTranslate("&b[텔레포트]")
                      .hoverEvent(HoverEvent.showText(ComponentUtil.createTranslate("클릭하여 %s에게 즉시 텔레포트합니다. %s", target, "&7 - /tp " + name)))
                      .clickEvent(ClickEvent.runCommand("/tp " + name))
      );
      adminPanel = adminPanel.append(Component.text(" "));
      adminPanel = adminPanel.append(
              ComponentUtil.createTranslate("&a[관전]")
                      .hoverEvent(HoverEvent.showText(ComponentUtil.createTranslate("클릭하여 %s을(를) 즉시 관전합니다. %s", target, "&7 - /cspectate " + name)))
                      .clickEvent(ClickEvent.runCommand("/cspectate " + name))
      );
      adminPanel = adminPanel.append(Component.text(" "));
      adminPanel = adminPanel.append(
              ComponentUtil.createTranslate("&4[접속 제한]")
                      .hoverEvent(HoverEvent.showText(ComponentUtil.createTranslate("클릭하여 %s을(를) 접속 제한합니다. %s", target, "&7 - /ban " + name + " [사유]")))
                      .clickEvent(ClickEvent.suggestCommand("/ban " + name + " "))
      );
      adminPanel = adminPanel.append(Component.text(" "));
      adminPanel = adminPanel.append(
              ComponentUtil.createTranslate("&4[강퇴]")
                      .hoverEvent(HoverEvent.showText(ComponentUtil.createTranslate("클릭하여 %s을(를) 강퇴합니다. %s", target, "&7 - /kick " + name + " [사유]")))
                      .clickEvent(ClickEvent.suggestCommand("/kick " + name + " "))
      );

      MessageUtil.sendMessage(player, adminPanel);
    }
    MessageUtil.sendMessage(player, Prefix.INFO_SOCIAL, ComponentUtil.create2("&m&l                                              "));
    return true;
  }
}








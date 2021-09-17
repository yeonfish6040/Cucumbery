package com.jho5245.cucumbery.commands.debug;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GetPositions implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
		if (!Method.hasPermission(sender, Permission.CMD_GETPOSITIONS, true))
		{
			return true;
		}
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    String usage = cmd.getUsage().replace("/<command> ", "");
    if (args.length <= 1)
    {
      boolean worldSpecified = false;
      World world = null;
      if (args.length == 1)
      {
        world = Bukkit.getServer().getWorld(args[0]);
        if (world == null)
        {
          MessageUtil.noArg(sender, Prefix.NO_WORLD, args[0]);
          return true;
        }
        worldSpecified = true;
        if (world.getPlayers().isEmpty())
        {
          MessageUtil.info(sender, "해당 월드에는 플레이어가 존재하지 않습니다.");
          return true;
        }
      }
      MessageUtil.info(sender, "&6--------------------&c플레이어 좌표 목록&6--------------------");
      for (Player player : Bukkit.getServer().getOnlinePlayers())
      {
        Location loc = player.getLocation();
        World playerWorld = loc.getWorld();
				if (worldSpecified && !playerWorld.equals(world))
				{
					continue;
				}
				MessageUtil.info(sender, ComponentUtil.createTranslate("%s : %s", player, loc));
//        String worldName = playerWorld.getName();
//        double x = loc.getX(), y = loc.getY(), z = loc.getZ(), yaw = loc.getYaw(), pitch = loc.getPitch();
//        String xDisplay = Constant.Sosu5Force(x).replace("0", "&00&e");
//        xDisplay = xDisplay.replace("&e", xDisplay.startsWith("&a+") ? "&a" : "&c").replace(".", "_");
//        String yDisplay = Constant.Sosu5Force(y).replace("0", "&00&e");
//        yDisplay = yDisplay.replace("&e", yDisplay.startsWith("&a+") ? "&a" : "&c").replace(".", "_");
//        String zDisplay = Constant.Sosu5Force(z).replace("0", "&00&e");
//        zDisplay = zDisplay.replace("&e", zDisplay.startsWith("&a+") ? "&a" : "&c").replace(".", "_");
//        String yawDisplay = Constant.Sosu2OnlyForce(yaw).replace("0", "&00&e");
//        yawDisplay = yawDisplay.replace("&e", yawDisplay.startsWith("&a+") ? "&a" : "&c").replace(".", "_");
//        String pitchDisplay = Constant.Sosu2OnlyForce(pitch).replace("0", "&00&e");
//        pitchDisplay = pitchDisplay.replace("&e", pitchDisplay.startsWith("&a+") ? "&a" : "&c").replace(".", "_");
//        double distance = player.getLocation().distance(sender instanceof Player ? ((Player) sender).getLocation() : player.getLocation());
//        String distanceDisplay = Constant.Sosu5Force(distance).replace("0", "&00&e");
//        distanceDisplay = distanceDisplay.replace("&e", distanceDisplay.startsWith("&a+") ? "&e" : "&c").replace(".", "_");
//        Component msg = ComponentUtil.create(Prefix.INFO, "x=&e" + xDisplay + "&r, y=&e" + yDisplay + "&r, z=&e" + zDisplay + "&r, yaw=&e" + yawDisplay + "&r, pitch=&e" + pitchDisplay
//                + "&r, 거리=&e" + distanceDisplay + "m&r, ", player, "의 위치 : " + (worldSpecified ? "" : ("월드=&e" + worldName + "&r, ")));
//        MessageUtil.sendMessage(sender, ComponentUtil.create(MessageUtil.as(msg), MessageUtil.as("클릭하면 ", player, "에게 이동할 수 있습니다."),
//                ClickEvent.Action.SUGGEST_COMMAND, "/tp " + player.getName()));
      }
    }
    else
    {
      MessageUtil.longArg(sender, 2, args);
      MessageUtil.commandInfo(sender, label, usage);
    }
    return true;
  }
}

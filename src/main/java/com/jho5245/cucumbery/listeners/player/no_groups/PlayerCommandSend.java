package com.jho5245.cucumbery.listeners.player.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.data.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PlayerCommandSend implements Listener
{
	@EventHandler
	public void onPlayerCommandSend(PlayerCommandSendEvent event)
	{
		Player player = event.getPlayer();
		Collection<String> commands = event.getCommands();
		if (!Cucumbery.config.getBoolean("grant-default-permission-to-players") && !Method.hasPermission(player, Permission.EVENT_COMMANDPREPROCESS, false))
		{
			commands.clear();
			return;
		}
		{
			if (!player.hasPermission("bukkit.command.reload"))
			{
				commands.remove("rlc");
				commands.remove("cherry:rlc");
			}
			if (!player.hasPermission("minecraft.command.reload"))
			{
				commands.remove("minecraft:reload");
			}
			if (!player.hasPermission("minecraft.command.msg"))
			{
				commands.remove("minecraft:msg");
				commands.remove("minecraft:tell");
				commands.remove("minecraft:w");
			}
			if (!player.hasPermission("minecraft.command.teammsg"))
			{
				commands.remove("teammsg");
				commands.remove("minecraft:teammsg");
				commands.remove("tm");
				commands.remove("minecraft:tm");
			}
			if (!player.hasPermission("minecraft.command.me"))
			{
				commands.remove("me");
				commands.remove("minecraft:me");
			}
			if (!player.hasPermission("minecraft.command.trigger"))
			{
				commands.remove("trigger");
				commands.remove("minecraft:trigger");
			}
			if (!player.hasPermission("minecraft.command.list"))
			{
				commands.remove("list");
				commands.remove("minecraft:list");
			}
			if (!player.hasPermission("bukkit.command.help"))
			{
				commands.remove("icanhasbukkit");
			}
			if (!Cucumbery.config.getBoolean("rpg-enabled"))
			{
				commands.removeAll(this.deleteCommands(player, null, "debugdamage"));
			}
		}

		commands.removeAll(this.deleteCommands(player, Permission.CMD_MAINCOMMAND, "cucumbery"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_ADVANCED_TELEPORT, "advancedteleport"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_AFEED, "advancedfeed"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_AIRPOINT, "airpoint"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_ALLPLAYER, "allplayer"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_BROADCAST, "broadcast"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_BROADCASTITEM, "broadcastitem"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_CALC_DISTANCE, "calcdistance"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_CALL, "call"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_CHECK_AMOUNT, "checkamount"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_CHECKPERMISSION, "checkpermission"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_CLEARCHAT, "clearchat"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_COMMANDPACK, "commandpack"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_DELWARP, "cdelwarp"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_ENDERCHEST, "enderchest"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_FEED, "feed"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_FORCECHAT, "forcechat"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_GETPOSITIONS, "getpositions"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_GUICOMMANDS, "menu"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_HANDGIVE, "handgive"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_HAT, "hat"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_HEAL, "heal"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_ITEMDATA, "itemdata"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_ITEMSTORAGE, "citemstorage"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_MAINCOMMAND, "cucumbery"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_MHP, "maxhealthpoint"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_NICK, "nickname"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_NICK_OTHERS, "nicknameothers"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_PLAYSOUND, "cplaysound"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_PLAYSOUNDALL, "playsoundall"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_PLAYSOUNDALL2, "playsoundall2"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_REINFORCE, "reinforce"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_SENDMESSAGE, "sendmessage"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_SETDATA, "itemflag"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_SETDATA, "setrepaircost"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_SETDATA, "setdata"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_SETDATA, "addlore"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_SETDATA, "setlore"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_SETDATA, "deletelore"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_SETDATA, "insertlore"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_SETDATA, "setname"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_SETDATA, "setname2"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_SETDATA, "setlore2"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_SETHELDITEMSLOT, "sethelditemslot"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_USERDATA, "userdata"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_SETWARP, "csetwarp"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_SHP, "healthbar"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_SPECTATE, "cspectate"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_SUDO, "sudo"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_SWAP_TELEPORT, "swapteleport"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_SWAPHELDITEM, "swaphelditem"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_TRASHCAN, "trashcan"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_UPDATE_INVENTORY, "updateinventory"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_WARP, "cwarp"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_WARPS, "cwarps"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_WHATIS, "whatis"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_WHOIS, "whois"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_WORKBENCH, "workbench"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_YUNNORI, "yunnori"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_ADVANCED_TELEPORT, "testcommand"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_UPDATE_COMMANDS, "updatecommands"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_SONG, "csong"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_SONG, "csong2"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_SETDATA, "citemtag"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_HOWIS, "howis"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_CUSTOMRECIPE, "customrecipe"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_VIRTUAL_CHEST, "virtualchest"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_VIRTUAL_CHEST_ADMIN, "virtualchestadd"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_VIRTUAL_CHEST_ADMIN, "virtualchestadmin"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_CUSTOM_FIX, "customfix"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_ECONOMY, "economy"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_RESPAWN, "respawn"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_REMOVE_BED_SPAWN_LOCATION, "removebedspawnlocation"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_CHECK_CONFIG, "checkconfig"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_TELEPORT, "cteleport"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_EDIT_COMMAND_BLOCK, "editcommandblock"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_EDIT_BLOCK_DATA, "editblockdata"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_CONSOLE_SUDO, "consolesudo"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_VELOCITY, "velocity2"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_QUICKSHOP_ADDON, "quickshopaddon"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_REPEAT, "repeat"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_TRUEKILL, "ckill2"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_VIEW_INVNETORY, "viewinventory"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_CUSTOM_MERCHANT_ADMIN, "custommerchant"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_CUSTOM_EFFECT, "customeffect"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_MODIFY_EXPLOSIVE, "modifyexplosive"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_SEND_TOAST, "sendtoast"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_SEND_BOSSBAR, "sendbossbar"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_SET_NO_DAMAGE_TICKS, "setnodamageticks"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_SET_AGGRO, "setaggro"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_STASH, "stash"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_BLOCK_PLACE_DATA, "blockplacedata"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_SPLASH, "splash"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_SWING_ARM, "swingarm"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_SHAKE_VILLAGER_HEAD, "shakevillagerhead"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_SET_ROTATION, "setrotation"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_LOOK_AT, "lookat"));
		commands.removeAll(this.deleteCommands(player, Permission.CMD_ENCHANT, "cenchant"));

		// 뭔데 왜 /cucumbery:nick 안사라지는데
		if (!Method.hasPermission(player, Permission.CMD_NICK, false))
			commands.remove("cucumbery:nick");

		// cucumbery:heal도 안사라지네 에센셜때문이냐
		if (!Method.hasPermission(player, Permission.CMD_HEAL, false))
		{
			commands.remove("회복");
			commands.remove("cheal");
			commands.remove("ghlqhr");
		}
		
		// cucumbery:hat도 안사라지네 에센셜때문이냐
		if (!Method.hasPermission(player, Permission.CMD_HAT, false))
		{
			commands.remove("모자");
			commands.remove("햇");
			commands.remove("ㅎㅁㅅ");
			commands.remove("모자");
			commands.remove("got");
		}
		
		// cucumbery:workbench도 안사라지네 에센셜때문이냐
		if (!Method.hasPermission(player, Permission.CMD_WORKBENCH, false))
		{
			commands.remove("cwb");
			commands.remove("cworkbench");
			commands.remove("작업대");
			commands.remove("제작대");
			commands.remove("가상작업대");
			commands.remove("가상제작대");
			commands.remove("조합대");
			commands.remove("wkrdjqeo");
			commands.remove("wpwkreo");
			commands.remove("rktkdwpwkreo");
			commands.remove("rktkdwkrdjqeo");
			commands.remove("whgkqeo");
		}
		
		// cucumbery:enderchest도 안사라지네 에센셜때문이냐
		if (!Method.hasPermission(player, Permission.CMD_ENDERCHEST, false))
		{
			commands.remove("cec");
			commands.remove("cenderchest");
			commands.remove("엔상");
			commands.remove("엔더상자");
			commands.remove("dpstkd");
			commands.remove("dpsejtkdwk");
		}
		
		if (!Permission.EVENT2_COMMAND_SEND_COLON.has(player))
		{
			List<String> cmds = new ArrayList<>();
			for (String command : commands)
			{
				if (command.contains(":"))
					cmds.add(command);
			}
			commands.removeAll(cmds);
		}
	}

	private List<String> deleteCommands(Player player, Permission permission, String command)
	{
		try
		{
			if (command.equals("menu") || command.equals("broadcastitem") || command.equals("trashcan"))
			{
				if (Cucumbery.config.getBoolean("grant-default-permission-to-players"))
				{
					return Collections.emptyList();
				}
			}
			List<String> cmds = new ArrayList<>();
			PluginCommand cmd = Bukkit.getServer().getPluginCommand(command);
			if (cmd != null && (permission == null || !Method.hasPermission(player, permission, false)))
			{
				cmds.add(command);
				cmds.add("cucumbery:" + command);
				cmds.addAll(cmd.getAliases());
				for (String alias : cmd.getAliases())
				{
					cmds.add("cucumbery:" + alias);
				}
			}
			return cmds;
		}
		catch (Exception e)
		{
			return Collections.emptyList();
		}
	}
}

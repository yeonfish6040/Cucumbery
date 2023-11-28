package com.jho5245.cucumbery.util.rpg;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StatManager implements Listener
{
//	private static final StatManager statManager = new StatManager();
//
//	public static StatManager getStatManager()
//	{
//		return statManager;
//	}

	public static int size = 9;

	public void createNewStat(Player player)
	{
		File loc1 = new File(Cucumbery.getPlugin().getDataFolder() + "/data");
		File loc2 = new File(Cucumbery.getPlugin().getDataFolder() + "/data/RPG");
		File StatFile = new File(Cucumbery.getPlugin().getDataFolder() + "/data/RPG/" + player.getUniqueId().toString() + ".yml");

		try
		{
			if (!StatFile.exists())
			{
				loc1.mkdir();
				loc2.mkdir();

				StatFile.createNewFile();

				BufferedWriter w = new BufferedWriter(new FileWriter(StatFile));

				w.append("스탯 포인트 : " + "0" + "\n" + // 1
						"STR : " + "0" + "\n" + // 2
						"DEX : " + "0" + "\n" + // 3
						"INT : " + "0" + "\n" + // 4
						"LUK : " + "0" + "\n" + // 5
						"숙련도 : " + "1500" + "\n" + // 6
						"활당김 : " + "0" + "\n" + // 7
						"레벨 : " + "1" + "\n" + // 8
						"경험치 : " + "0" + "" + // 9
						"");

				w.flush();
				w.close();
			}
		}

		catch (IOException exception)
		{
			MessageUtil.consoleSendMessage(Prefix.INFO_ERROR + "§e" + player.getUniqueId().toString() + "§e.yml&r 파일을 생성하는 도중 오류가 발생했습니다");
			Cucumbery.getPlugin().getLogger().warning(exception.getMessage());
		}
	}

	public long[] getStat(Player player)
	{
		long[] stat = new long[12];

		File loc1 = new File(Cucumbery.getPlugin().getDataFolder() + "/data");
		File loc2 = new File(Cucumbery.getPlugin().getDataFolder() + "/data/RPG");
		File StatFile = new File(Cucumbery.getPlugin().getDataFolder() + "/data/RPG/" + player.getUniqueId() + ".yml");

		try
		{
			if (!StatFile.exists())
			{
				loc1.mkdir();
				loc2.mkdir();

				StatFile.createNewFile();
				BufferedWriter w = new BufferedWriter(new FileWriter(StatFile));

				w.append("스탯 포인트 : " + "0" + "\n" + // 1
						"STR : " + "0" + "\n" + // 2
						"DEX : " + "0" + "\n" + // 3
						"INT : " + "0" + "\n" + // 4
						"LUK : " + "0" + "\n" + // 5
						"숙련도 : " + "1500" + "\n" + // 6
						"활당김 : " + "0" + "\n" + // 7
						"레벨 : " + "1" + "\n" + // 8
						"경험치 : " + "0" + "" + // 9
						"");

				w.flush();
				w.close();
			}

			BufferedReader r = new BufferedReader(new FileReader(StatFile));

			List<Long> list = new ArrayList<Long>();

			String temp = "";

			while ((temp = r.readLine()) != null)
			{
				list.add(Long.valueOf(cutter(temp)));
			}

			r.close();

			for (int i = 0; i < size; i++)
			{
				stat[i] = Long.valueOf(list.get(i));
			}

			return stat;
		}

		catch (IOException exception)
		{
			MessageUtil.consoleSendMessage(Prefix.INFO_ERROR + "§e" + player.getUniqueId().toString() + "§e.yml&r 파일을 생성하는 도중 오류가 발생했습니다");
Cucumbery.getPlugin().getLogger().warning(			exception.getMessage());
		}

		return stat;
	}

	public long cutter(String line)
	{
		String[] cutter = line.split(" : ");
		
		try
		{
			return Long.parseLong(cutter[1]);
		}
		
		catch (NumberFormatException e)
		{
			return (long) Double.parseDouble(cutter[1]);
		}
		
		catch (Exception e)
		{
Cucumbery.getPlugin().getLogger().warning(			e.getMessage());
			return 0;
		}
	}

	public void setStat(Player player, long[] stat)
	{
		File loc1 = new File(Cucumbery.getPlugin().getDataFolder() + "/data");
		File loc2 = new File(Cucumbery.getPlugin().getDataFolder() + "/data/RPG");
		File StatFile = new File(Cucumbery.getPlugin().getDataFolder() + "/data/RPG/" + player.getUniqueId().toString() + ".yml");

		try
		{
			if (!StatFile.exists())
			{
				loc1.mkdir();
				loc2.mkdir();

				StatFile.createNewFile();

				BufferedWriter w = new BufferedWriter(new FileWriter(StatFile));

				w.append("스탯 포인트 : " + "0" + "\n" + // 1
						"STR : " + "0" + "\n" + // 2
						"DEX : " + "0" + "\n" + // 3
						"INT : " + "0" + "\n" + // 4
						"LUK : " + "0" + "\n" + // 5
						"숙련도 : " + "1500" + "\n" + // 6
						"활당김 : " + "0" + "\n" + // 7
						"레벨 : " + "1" + "\n" + // 8
						"경험치 : " + "0" + "" + // 9
						"");

				w.flush();
				w.close();
			}

			BufferedWriter w = new BufferedWriter(new FileWriter(StatFile));

			w.append("스탯 포인트 : " + stat[0] + "\n" + // 1
					"STR : " + stat[1] + "\n" + // 2
					"DEX : " + stat[2] + "\n" + // 3
					"INT : " + stat[3] + "\n" + // 4
					"LUK : " + stat[4] + "\n" + // 5
					"숙련도 : " + stat[5] + "\n" + // 6
					"활당김 : " + stat[6] + "\n" + // 7
					"레벨 : " + stat[7] + "\n" + // 8
					"경험치 : " + stat[8] + "" + // 9
					"");

			w.flush();
			w.close();
		}

		catch (IOException exception)
		{
			MessageUtil.consoleSendMessage(Prefix.INFO_ERROR + "§e" + player.getUniqueId().toString() + "§e.yml&r 파일을 생성하는 도중 오류가 발생했습니다");
Cucumbery.getPlugin().getLogger().warning(			exception.getMessage());
		}
	}

	public void statUp(long[] stat, Player player, int num, ClickType click)
	{
		if (click == ClickType.LEFT)
		{
			if (stat[0] >= 1)
			{
				if (num == 5 && stat[5] + 1 > 9900)
				{
					MessageUtil.sendError(player, "§e숙련도&r는 99%까지만 채울 수 있습니다");
					return;
				}

				Method.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
				stat[0]--;
				stat[num]++;

				this.setStat(player, stat);
			}
			else
			{
				MessageUtil.sendError(player, "스탯 포인트가 부족합니다");
				return;
			}
		}

		else if (click == ClickType.RIGHT)
		{
			if (stat[0] >= 10)
			{
				if (num == 5 && stat[5] + 1 > 9900)
				{
					MessageUtil.sendError(player, "§e숙련도&r는 99%까지만 채울 수 있습니다");
					return;
				}
				Method.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
				stat[0] -= 10;
				stat[num] += 10;
				this.setStat(player, stat);
			}
			else
			{
				MessageUtil.sendError(player, "스탯 포인트가 부족합니다");
				return;
			}
		}
		else if (click == ClickType.SHIFT_LEFT)
		{
			if (stat[0] >= 100)
			{
				if (num == 5 && stat[5] + 100 > 9900)
				{
					MessageUtil.sendError(player, "§e숙련도&r는 99%까지만 채울 수 있습니다");
					return;
				}
				Method.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
				stat[0] -= 100;
				stat[num] += 100;
				this.setStat(player, stat);
			}
			else
			{
				MessageUtil.sendError(player, "스탯 포인트가 부족합니다");
				return;
			}
		}

		else if (click == ClickType.SHIFT_RIGHT)
		{
			if (stat[0] > 0)
			{
				if (num == 5 && stat[5] >= 9900)
				{
					MessageUtil.sendError(player, "§e숙련도&r는 99%까지만 채울 수 있습니다");
					return;
				}

				Method.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);

				if (num == 5)
				{
					long amount = stat[0];

					if (stat[5] + stat[0] > 9900)
					{
						amount = 9900 - stat[5];
					}

					stat[5] += amount;
					stat[0] -= amount;
				}

				else
				{
					stat[num] += stat[0];
					stat[0] = 0;
				}

				this.setStat(player, stat);
			}

			else
			{
				MessageUtil.sendError(player, "스탯 포인트가 부족합니다");
				return;
			}
		}
	}

	public void giveXp(Player player, long[] stat, long xp)
	{
		stat[8] += xp;

		setStat(player, stat);

		if (stat[7] == 250)
			return;
		
		int levelUp = 0;

		while (stat[8] >= Level.getExp((int) stat[7]))
		{
			stat[8] -= Level.getExp((int) stat[7]);
			stat[7]++;
			levelUp++;
			
			if (stat[7] == 250)
				break;
		}

		stat[0] += 5 * levelUp;

		if (levelUp >= 1)
		{
			MessageUtil.sendActionBar(player, "레벨업을 §e" + levelUp + "&r 번 하여 스탯포인트를 §e" + levelUp * 5 + "&r 만큼 획득했습니다");
		}

		setStat(player, stat);
	}
/*
	@EventHandler
	public void onExpGain(PlayerExpChangeEvent event)
	{
		if (!Cucumbery.getPlugin().getConfig().getBoolean("rpg-enabled"))
			return;

		List<String> worlds = Cucumbery.getPlugin().getConfig().getStringList("no-rpg-enabled-worlds");

		Player player = event.getPlayer();

		if (worlds.contains(player.getWorld().getName()))
			return;

		long[] stat = new long[size];

		stat = StatManager.getStatManager().getStat(player);

		giveXp(player, stat, event.getAmount());
	}

	@EventHandler
	public void onLevelUp(PlayerLevelChangeEvent event)
	{
		if (!Cucumbery.getPlugin().getConfig().getBoolean("rpg-enabled"))
			return;

		List<String> worlds = Cucumbery.getPlugin().getConfig().getStringList("no-rpg-enabled-worlds");

		Player player = event.getPlayer();

		if (worlds.contains(player.getWorld().getName()))
			return;

		int newLevel = event.getNewLevel(), oldLevel = event.getOldLevel();

		if (oldLevel >= newLevel)
			return;

		long[] stat = new long[size];

		stat = StatManager.getStatManager().getStat(player);

		int differance = newLevel - oldLevel;

		stat[0] += 5 * differance;

		MessageUtil.sendActionBar(player, "레벨업을 §e" + differance + "&r 번 하여 스탯포인트를 §e" + differance * 5 + "&r 만큼 획득했습니다");

		setStat(player, stat);
	}*/
}

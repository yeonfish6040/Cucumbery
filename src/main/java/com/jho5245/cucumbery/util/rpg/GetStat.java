package com.jho5245.cucumbery.util.rpg;

public class GetStat
{
/*	private static final GetStat getStat = new GetStat();

	public static GetStat getStatClass()
	{
		return getStat;
	}*/

	public static final String PREFIX_1 = "§알§피§지§일", PREFIX_2 = "§알§피§지§이", PREFIX_3 = "§알§피§지§삼", PREFIX_4 = "§알§피§지§사";

/*	public enum Stat
	{
		STR,
		DEX,
		INT,
		LUK,
		CRIT_CHANCE,
		MIN_CRIT,
		MIN_CRIT_PER,
		MAX_CRIT,
		MAX_CRIT_PER,
		MINECRAFT_DAMAGE,
		MINECRAFT_DAMAGE_PER,
		STAT_DAMAGE,
		STAT_DAMAGE_PER,
		MOB_DAMAGE,
		MOB_DAMAGE_PER,
		BOSS_DAMAGE,
		BOSS_DAMAGE_PER,
		DAMAGE,
		DAMAGE_PER,
		FINAL_DAMAGE,
		FINAL_DAMAGE_PER,
		PROFICIENCY,
		PATK,
		MATK,
		BONUS_EXP,
		BONUS_EXP_PER,
		IGNORE_DEF
	}
	
	public enum RPGSlot
	{
		MAIN_HAND,
		OFF_HAND,
		HELMET,
		CHESTPLATE,
		LEGGNINGS,
		BOOTS,
		INVENTORY
	}*/
/*
	public long getLongStat(Player player, Stat type)
	{
		long[] stat = new long[StatManager.size];

		stat = StatManager.getStatManager().getStat(player);

		long value = 0L;

		if (type == Stat.STR)
		{
			value += stat[1];
			value += (long) this.getDoubleStat(player, Stat.STR);
		}

		else if (type == Stat.DEX)
		{
			value += stat[2];
			value += (long) this.getDoubleStat(player, Stat.DEX);
		}

		else if (type == Stat.INT)
		{
			value += stat[3];
			value += (long) this.getDoubleStat(player, Stat.INT);
		}

		else if (type == Stat.LUK)
		{
			value += stat[4];
			value += (long) this.getDoubleStat(player, Stat.LUK);
		}

		else if (type == Stat.PROFICIENCY)
		{
			value = stat[5];
			if (value > 9900)
				value = 9900;
		}

		return value;
	}

	public double getDoubleStat(Player player, Stat type)
	{
		long[] stat = new long[StatManager.size];

		stat = StatManager.getStatManager().getStat(player);

		long STR = 0L, DEX = 0L, INT = 0L, LUK = 0L;

		double critChance = 5D, minCritPER = 20D, maxCritPER = 50D;
		double minecraftDamagePER = 0D, statDamagePER = 0D, damagePER = 0D, mobDamagePER = 0D, bossDamagePER = 0D, finalDamagePER = 1D, PATKPER = 0D, MATKPER = 0D, bounsXP = 0D,
				bounsXPPER = 0D, ignoreDef = 0D;

		double proficiency = stat[5] / 100D;

		double minCrit = 0L, maxCrit = 0L, minecraftDamage = 0L, statDamage = 0L, damage = 0L, mobDamage = 0L, bossDamage = 0L, finalDamage = 0L, PATK = 0D, MATK = 0D;

		STR = stat[1];
		DEX = stat[2];
		INT = stat[3];
		LUK = stat[4];

		double STRPER = 0D, DEXPER = 0D, INTPER = 0D, LUKPER = 0D;

		boolean hasMainHand = false, hasOffHand = false, hasHelmet = false, hasChestplate = false, hasLeggings = false, hasBoots = false;

		ItemStack main = player.getInventory().getItemInMainHand(), off = player.getInventory().getItemInOffHand(), helmet = player.getInventory().getHelmet(),
				chestplate = player.getInventory().getChestplate(), leggings = player.getInventory().getLeggings(), boots = player.getInventory().getBoots();

		if (main != null && main.getType() != Material.AIR && main.hasItemMeta() && main.getItemMeta().hasLore())
		{
			for (int i = 0; i < main.getItemMeta().getLore().size(); i++)
			{
				String lore = MessageUtil.stripColor(main.getItemMeta().getLore().get(i));
				if (lore.contains("슬롯 분류 : ") && lore.contains("주로 사용하는 손"))
				{
					hasMainHand = true;
					break;
				}
			}

			for (int i = 0; i < main.getItemMeta().getLore().size(); i++)
			{
				String lore = MessageUtil.stripColor(main.getItemMeta().getLore().get(i));

				if (lore.contains("슬롯 분류 : ") && lore.contains("인벤토리"))
				{
					hasMainHand = false;
					break;
				}
			}
		}

		if (off != null && off.getType() != Material.AIR && off.hasItemMeta() && off.getItemMeta().hasLore())
		{
			for (int i = 0; i < off.getItemMeta().getLore().size(); i++)
			{
				String lore = MessageUtil.stripColor(off.getItemMeta().getLore().get(i));

				if (lore.contains("슬롯 분류 : ") && lore.contains("다른 손"))
				{
					hasOffHand = true;
					break;
				}
			}

			for (int i = 0; i < off.getItemMeta().getLore().size(); i++)
			{
				String lore = MessageUtil.stripColor(off.getItemMeta().getLore().get(i));

				if (lore.contains("슬롯 분류 : ") && lore.contains("인벤토리"))
				{
					hasOffHand = false;
					break;
				}
			}
		}

		if (helmet != null && helmet.getType() != Material.AIR && helmet.hasItemMeta() && helmet.getItemMeta().hasLore())
		{
			for (int i = 0; i < helmet.getItemMeta().getLore().size(); i++)
			{
				String lore = MessageUtil.stripColor(helmet.getItemMeta().getLore().get(i));

				if (lore.contains("슬롯 분류 : ") && lore.contains("머리"))
				{
					hasHelmet = true;
					break;
				}
			}

			for (int i = 0; i < helmet.getItemMeta().getLore().size(); i++)
			{
				String lore = MessageUtil.stripColor(helmet.getItemMeta().getLore().get(i));

				if (lore.contains("슬롯 분류 : ") && lore.contains("인벤토리"))
				{
					hasHelmet = false;
					break;
				}
			}
		}

		if (chestplate != null && chestplate.getType() != Material.AIR && chestplate.hasItemMeta() && chestplate.getItemMeta().hasLore())
		{
			for (int i = 0; i < chestplate.getItemMeta().getLore().size(); i++)
			{
				String lore = MessageUtil.stripColor(chestplate.getItemMeta().getLore().get(i));

				if (lore.contains("슬롯 분류 : ") && lore.contains("몸"))
				{
					hasChestplate = true;
					break;
				}
			}

			for (int i = 0; i < chestplate.getItemMeta().getLore().size(); i++)
			{
				String lore = MessageUtil.stripColor(chestplate.getItemMeta().getLore().get(i));

				if (lore.contains("슬롯 분류 : ") && lore.contains("인벤토리"))
				{
					hasChestplate = false;
					break;
				}
			}
		}

		if (leggings != null && leggings.getType() != Material.AIR && leggings.hasItemMeta() && leggings.getItemMeta().hasLore())
		{
			for (int i = 0; i < leggings.getItemMeta().getLore().size(); i++)
			{
				String lore = MessageUtil.stripColor(leggings.getItemMeta().getLore().get(i));

				if (lore.contains("슬롯 분류 : ") && lore.contains("다리"))
				{
					hasLeggings = true;
					break;
				}
			}

			for (int i = 0; i < leggings.getItemMeta().getLore().size(); i++)
			{
				String lore = MessageUtil.stripColor(leggings.getItemMeta().getLore().get(i));

				if (lore.contains("슬롯 분류 : ") && lore.contains("인벤토리"))
				{
					hasLeggings = false;
					break;
				}
			}
		}

		if (boots != null && boots.getType() != Material.AIR && boots.hasItemMeta() && boots.getItemMeta().hasLore())
		{
			for (int i = 0; i < boots.getItemMeta().getLore().size(); i++)
			{
				String lore = MessageUtil.stripColor(boots.getItemMeta().getLore().get(i));

				if (lore.contains("슬롯 분류 : ") && lore.contains("발"))
				{
					hasBoots = true;
					break;
				}
			}

			for (int i = 0; i < boots.getItemMeta().getLore().size(); i++)
			{
				String lore = MessageUtil.stripColor(boots.getItemMeta().getLore().get(i));

				if (lore.contains("슬롯 분류 : ") && lore.contains("인벤토리"))
				{
					hasBoots = false;
					break;
				}
			}
		}

		if (hasMainHand)
		{
			ItemMeta meta = main.getItemMeta();
			List<String> lore = meta.getLore();

			for (int i = 0; i < lore.size(); i++)
			{
				String s = lore.get(i);

				if (s.contains("§a§m§e§1"))
					s = s.split("§a§m§e§1")[0];

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("STR : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("STR : +");

					if (s.contains("%"))
					{
						try
						{
							STRPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							STR += Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("STR : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("STR : -");

					if (s.contains("%"))
					{
						try
						{
							STRPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							STR -= Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("DEX : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("DEX : +");

					if (s.contains("%"))
					{
						try
						{
							DEXPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							DEX += Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("DEX : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("DEX : -");

					if (s.contains("%"))
					{
						try
						{
							DEXPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							DEX -= Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("INT : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("INT : +");

					if (s.contains("%"))
					{
						try
						{
							INTPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							INT += Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("INT : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("INT : -");

					if (s.contains("%"))
					{
						try
						{
							INTPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							INT -= Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("LUK : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("LUK : +");

					if (s.contains("%"))
					{
						try
						{
							LUKPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							LUK += Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("LUK : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("LUK : -");

					if (s.contains("%"))
					{
						try
						{
							LUKPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							LUK -= Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("올스탯 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("올스탯 : +");

					if (s.contains("%"))
					{
						try
						{
							STRPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							DEXPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							INTPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							LUKPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							STR += Long.parseLong(cutter[1]);
							DEX += Long.parseLong(cutter[1]);
							INT += Long.parseLong(cutter[1]);
							LUK += Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("올스탯 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("올스탯 : -");

					if (s.contains("%"))
					{
						try
						{
							STRPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							DEXPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							INTPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							LUKPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							STR -= Long.parseLong(cutter[1]);
							DEX -= Long.parseLong(cutter[1]);
							INT -= Long.parseLong(cutter[1]);
							LUK -= Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 확률 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("크리티컬 확률 : +");

					try
					{
						critChance += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
					}

					catch (NumberFormatException exception)
					{

					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 확률 : -"))
				{
					s = MessageUtil.stripColor(s);

					String[] cutter = s.split("크리티컬 확률 : -");

					try
					{
						critChance -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
					}

					catch (NumberFormatException exception)
					{

					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 최소 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("크리티컬 최소 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							minCritPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							minCrit += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 최소 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("크리티컬 최소 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							minCritPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							minCrit -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 최대 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("크리티컬 최대 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							maxCritPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							maxCrit += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 최대 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("크리티컬 최대 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							maxCritPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							maxCrit -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("일반 몬스터 공격 시 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("일반 몬스터 공격 시 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							mobDamagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							mobDamage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("일반 몬스터 공격 시 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("일반 몬스터 공격 시 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							mobDamagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							mobDamage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("보스 몬스터 공격 시 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("보스 몬스터 공격 시 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							bossDamagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							bossDamage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("보스 몬스터 공격 시 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("보스 몬스터 공격 시 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							bossDamagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							bossDamage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("마인크래프트 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("마인크래프트 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							minecraftDamagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							minecraftDamage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("마인크래프트 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("마인크래프트 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							minecraftDamagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							minecraftDamage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("스탯 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("스탯 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							statDamagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							statDamage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("스탯 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("스탯 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							statDamagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							statDamage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							damagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							damage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							damagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							damage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("최종 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("최종 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							finalDamagePER *= 1D + Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1)) / 100D;
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							finalDamage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("최종 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("최종 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							finalDamagePER *= 1D - Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1)) / 100D;
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							finalDamage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("공격력 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("공격력 : +");

					if (s.contains("%"))
					{
						try
						{
							PATKPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							PATK += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("공격력 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("공격력 : -");

					if (s.contains("%"))
					{
						try
						{
							PATKPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							PATK -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("마력 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("마력 : +");

					if (s.contains("%"))
					{
						try
						{
							MATKPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							MATK += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("마력 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("마력 : -");

					if (s.contains("%"))
					{
						try
						{
							MATKPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							MATK -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("경험치 획득량 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("경험치 획득량 : +");

					if (s.contains("%"))
					{
						try
						{
							bounsXPPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							bounsXP += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("경험치 획득량 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("경험치 획득량 : -");

					if (s.contains("%"))
					{
						try
						{
							bounsXPPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							bounsXP -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("몬스터 방어율 무시 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("몬스터 방어율 무시 : +");

					try
					{
						double ignore = Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));

						ignoreDef = ignoreDef + ignore / 100D - ignoreDef * ignore / 100D;
					}

					catch (NumberFormatException exception)
					{

					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("몬스터 방어율 무시 : -"))
				{
					s = MessageUtil.stripColor(s);

					String[] cutter = s.split("몬스터 방어율 무시 : -");

					try
					{
						double ignore = -Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));

						ignoreDef = ignoreDef + ignore / 100D - ignoreDef * ignore / 100D;
					}

					catch (NumberFormatException exception)
					{

					}
				}
			}
		}

		if (hasOffHand)
		{
			ItemMeta meta = off.getItemMeta();
			List<String> lore = meta.getLore();

			for (int i = 0; i < lore.size(); i++)
			{
				String s = lore.get(i);

				if (s.contains("§a§m§e§1"))
					s = s.split("§a§m§e§1")[0];

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("STR : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("STR : +");

					if (s.contains("%"))
					{
						try
						{
							STRPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							STR += Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("STR : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("STR : -");

					if (s.contains("%"))
					{
						try
						{
							STRPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							STR -= Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("DEX : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("DEX : +");

					if (s.contains("%"))
					{
						try
						{
							DEXPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							DEX += Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("DEX : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("DEX : -");

					if (s.contains("%"))
					{
						try
						{
							DEXPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							DEX -= Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("INT : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("INT : +");

					if (s.contains("%"))
					{
						try
						{
							INTPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							INT += Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("INT : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("INT : -");

					if (s.contains("%"))
					{
						try
						{
							INTPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							INT -= Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("LUK : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("LUK : +");

					if (s.contains("%"))
					{
						try
						{
							LUKPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							LUK += Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("LUK : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("LUK : -");

					if (s.contains("%"))
					{
						try
						{
							LUKPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							LUK -= Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("올스탯 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("올스탯 : +");

					if (s.contains("%"))
					{
						try
						{
							STRPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							DEXPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							INTPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							LUKPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							STR += Long.parseLong(cutter[1]);
							DEX += Long.parseLong(cutter[1]);
							INT += Long.parseLong(cutter[1]);
							LUK += Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("올스탯 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("올스탯 : -");

					if (s.contains("%"))
					{
						try
						{
							STRPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							DEXPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							INTPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							LUKPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							STR -= Long.parseLong(cutter[1]);
							DEX -= Long.parseLong(cutter[1]);
							INT -= Long.parseLong(cutter[1]);
							LUK -= Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 확률 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("크리티컬 확률 : +");

					try
					{
						critChance += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
					}

					catch (NumberFormatException exception)
					{

					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 확률 : -"))
				{
					s = MessageUtil.stripColor(s);

					String[] cutter = s.split("크리티컬 확률 : -");

					try
					{
						critChance -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
					}

					catch (NumberFormatException exception)
					{

					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 최소 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("크리티컬 최소 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							minCritPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							minCrit += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 최소 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("크리티컬 최소 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							minCritPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							minCrit -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 최대 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("크리티컬 최대 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							maxCritPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							maxCrit += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 최대 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("크리티컬 최대 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							maxCritPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							maxCrit -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("일반 몬스터 공격 시 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("일반 몬스터 공격 시 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							mobDamagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							mobDamage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("일반 몬스터 공격 시 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("일반 몬스터 공격 시 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							mobDamagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							mobDamage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("보스 몬스터 공격 시 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("보스 몬스터 공격 시 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							bossDamagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							bossDamage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("보스 몬스터 공격 시 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("보스 몬스터 공격 시 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							bossDamagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							bossDamage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("마인크래프트 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("마인크래프트 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							minecraftDamagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							minecraftDamage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("마인크래프트 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("마인크래프트 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							minecraftDamagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							minecraftDamage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("스탯 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("스탯 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							statDamagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							statDamage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("스탯 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("스탯 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							statDamagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							statDamage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							damagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							damage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							damagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							damage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("최종 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("최종 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							finalDamagePER *= 1D + Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1)) / 100D;
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							finalDamage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("최종 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("최종 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							finalDamagePER *= 1D - Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1)) / 100D;
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							finalDamage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("공격력 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("공격력 : +");

					if (s.contains("%"))
					{
						try
						{
							PATKPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							PATK += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("공격력 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("공격력 : -");

					if (s.contains("%"))
					{
						try
						{
							PATKPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							PATK -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("마력 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("마력 : +");

					if (s.contains("%"))
					{
						try
						{
							MATKPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							MATK += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("마력 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("마력 : -");

					if (s.contains("%"))
					{
						try
						{
							MATKPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							MATK -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("경험치 획득량 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("경험치 획득량 : +");

					if (s.contains("%"))
					{
						try
						{
							bounsXPPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							bounsXP += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("경험치 획득량 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("경험치 획득량 : -");

					if (s.contains("%"))
					{
						try
						{
							bounsXPPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							bounsXP -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("몬스터 방어율 무시 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("몬스터 방어율 무시 : +");

					try
					{
						double ignore = Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));

						ignoreDef = ignoreDef + ignore / 100D - ignoreDef * ignore / 100D;
					}

					catch (NumberFormatException exception)
					{

					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("몬스터 방어율 무시 : -"))
				{
					s = MessageUtil.stripColor(s);

					String[] cutter = s.split("몬스터 방어율 무시 : -");

					try
					{
						double ignore = -Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));

						ignoreDef = ignoreDef + ignore / 100D - ignoreDef * ignore / 100D;
					}

					catch (NumberFormatException exception)
					{

					}
				}
			}
		}

		if (hasHelmet)
		{
			ItemMeta meta = helmet.getItemMeta();
			List<String> lore = meta.getLore();

			for (int i = 0; i < lore.size(); i++)
			{
				String s = lore.get(i);

				if (s.contains("§a§m§e§1"))
					s = s.split("§a§m§e§1")[0];

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("STR : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("STR : +");

					if (s.contains("%"))
					{
						try
						{
							STRPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							STR += Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("STR : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("STR : -");

					if (s.contains("%"))
					{
						try
						{
							STRPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							STR -= Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("DEX : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("DEX : +");

					if (s.contains("%"))
					{
						try
						{
							DEXPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							DEX += Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("DEX : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("DEX : -");

					if (s.contains("%"))
					{
						try
						{
							DEXPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							DEX -= Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("INT : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("INT : +");

					if (s.contains("%"))
					{
						try
						{
							INTPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							INT += Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("INT : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("INT : -");

					if (s.contains("%"))
					{
						try
						{
							INTPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							INT -= Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("LUK : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("LUK : +");

					if (s.contains("%"))
					{
						try
						{
							LUKPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							LUK += Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("LUK : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("LUK : -");

					if (s.contains("%"))
					{
						try
						{
							LUKPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							LUK -= Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("올스탯 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("올스탯 : +");

					if (s.contains("%"))
					{
						try
						{
							STRPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							DEXPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							INTPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							LUKPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							STR += Long.parseLong(cutter[1]);
							DEX += Long.parseLong(cutter[1]);
							INT += Long.parseLong(cutter[1]);
							LUK += Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("올스탯 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("올스탯 : -");

					if (s.contains("%"))
					{
						try
						{
							STRPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							DEXPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							INTPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							LUKPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							STR -= Long.parseLong(cutter[1]);
							DEX -= Long.parseLong(cutter[1]);
							INT -= Long.parseLong(cutter[1]);
							LUK -= Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 확률 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("크리티컬 확률 : +");

					try
					{
						critChance += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
					}

					catch (NumberFormatException exception)
					{

					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 확률 : -"))
				{
					s = MessageUtil.stripColor(s);

					String[] cutter = s.split("크리티컬 확률 : -");

					try
					{
						critChance -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
					}

					catch (NumberFormatException exception)
					{

					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 최소 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("크리티컬 최소 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							minCritPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							minCrit += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 최소 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("크리티컬 최소 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							minCritPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							minCrit -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 최대 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("크리티컬 최대 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							maxCritPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							maxCrit += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 최대 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("크리티컬 최대 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							maxCritPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							maxCrit -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("일반 몬스터 공격 시 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("일반 몬스터 공격 시 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							mobDamagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							mobDamage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("일반 몬스터 공격 시 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("일반 몬스터 공격 시 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							mobDamagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							mobDamage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("보스 몬스터 공격 시 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("보스 몬스터 공격 시 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							bossDamagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							bossDamage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("보스 몬스터 공격 시 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("보스 몬스터 공격 시 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							bossDamagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							bossDamage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("마인크래프트 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("마인크래프트 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							minecraftDamagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							minecraftDamage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("마인크래프트 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("마인크래프트 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							minecraftDamagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							minecraftDamage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("스탯 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("스탯 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							statDamagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							statDamage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("스탯 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("스탯 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							statDamagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							statDamage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							damagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							damage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							damagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							damage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("최종 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("최종 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							finalDamagePER *= 1D + Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1)) / 100D;
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							finalDamage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("최종 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("최종 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							finalDamagePER *= 1D - Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1)) / 100D;
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							finalDamage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("공격력 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("공격력 : +");

					if (s.contains("%"))
					{
						try
						{
							PATKPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							PATK += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("공격력 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("공격력 : -");

					if (s.contains("%"))
					{
						try
						{
							PATKPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							PATK -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("마력 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("마력 : +");

					if (s.contains("%"))
					{
						try
						{
							MATKPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							MATK += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("마력 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("마력 : -");

					if (s.contains("%"))
					{
						try
						{
							MATKPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							MATK -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("경험치 획득량 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("경험치 획득량 : +");

					if (s.contains("%"))
					{
						try
						{
							bounsXPPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							bounsXP += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("경험치 획득량 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("경험치 획득량 : -");

					if (s.contains("%"))
					{
						try
						{
							bounsXPPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							bounsXP -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("몬스터 방어율 무시 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("몬스터 방어율 무시 : +");

					try
					{
						double ignore = Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));

						ignoreDef = ignoreDef + ignore / 100D - ignoreDef * ignore / 100D;
					}

					catch (NumberFormatException exception)
					{

					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("몬스터 방어율 무시 : -"))
				{
					s = MessageUtil.stripColor(s);

					String[] cutter = s.split("몬스터 방어율 무시 : -");

					try
					{
						double ignore = -Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));

						ignoreDef = ignoreDef + ignore / 100D - ignoreDef * ignore / 100D;
					}

					catch (NumberFormatException exception)
					{

					}
				}
			}
		}

		if (hasChestplate)
		{
			ItemMeta meta = chestplate.getItemMeta();
			List<String> lore = meta.getLore();

			for (int i = 0; i < lore.size(); i++)
			{
				String s = lore.get(i);

				if (s.contains("§a§m§e§1"))
					s = s.split("§a§m§e§1")[0];

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("STR : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("STR : +");

					if (s.contains("%"))
					{
						try
						{
							STRPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							STR += Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("STR : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("STR : -");

					if (s.contains("%"))
					{
						try
						{
							STRPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							STR -= Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("DEX : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("DEX : +");

					if (s.contains("%"))
					{
						try
						{
							DEXPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							DEX += Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("DEX : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("DEX : -");

					if (s.contains("%"))
					{
						try
						{
							DEXPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							DEX -= Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("INT : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("INT : +");

					if (s.contains("%"))
					{
						try
						{
							INTPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							INT += Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("INT : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("INT : -");

					if (s.contains("%"))
					{
						try
						{
							INTPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							INT -= Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("LUK : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("LUK : +");

					if (s.contains("%"))
					{
						try
						{
							LUKPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							LUK += Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("LUK : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("LUK : -");

					if (s.contains("%"))
					{
						try
						{
							LUKPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							LUK -= Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("올스탯 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("올스탯 : +");

					if (s.contains("%"))
					{
						try
						{
							STRPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							DEXPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							INTPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							LUKPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							STR += Long.parseLong(cutter[1]);
							DEX += Long.parseLong(cutter[1]);
							INT += Long.parseLong(cutter[1]);
							LUK += Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("올스탯 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("올스탯 : -");

					if (s.contains("%"))
					{
						try
						{
							STRPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							DEXPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							INTPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							LUKPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							STR -= Long.parseLong(cutter[1]);
							DEX -= Long.parseLong(cutter[1]);
							INT -= Long.parseLong(cutter[1]);
							LUK -= Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 확률 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("크리티컬 확률 : +");

					try
					{
						critChance += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
					}

					catch (NumberFormatException exception)
					{

					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 확률 : -"))
				{
					s = MessageUtil.stripColor(s);

					String[] cutter = s.split("크리티컬 확률 : -");

					try
					{
						critChance -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
					}

					catch (NumberFormatException exception)
					{

					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 최소 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("크리티컬 최소 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							minCritPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							minCrit += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 최소 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("크리티컬 최소 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							minCritPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							minCrit -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 최대 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("크리티컬 최대 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							maxCritPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							maxCrit += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 최대 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("크리티컬 최대 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							maxCritPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							maxCrit -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("일반 몬스터 공격 시 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("일반 몬스터 공격 시 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							mobDamagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							mobDamage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("일반 몬스터 공격 시 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("일반 몬스터 공격 시 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							mobDamagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							mobDamage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("보스 몬스터 공격 시 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("보스 몬스터 공격 시 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							bossDamagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							bossDamage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("보스 몬스터 공격 시 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("보스 몬스터 공격 시 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							bossDamagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							bossDamage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("마인크래프트 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("마인크래프트 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							minecraftDamagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							minecraftDamage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("마인크래프트 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("마인크래프트 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							minecraftDamagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							minecraftDamage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("스탯 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("스탯 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							statDamagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							statDamage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("스탯 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("스탯 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							statDamagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							statDamage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							damagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							damage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							damagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							damage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("최종 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("최종 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							finalDamagePER *= 1D + Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1)) / 100D;
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							finalDamage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("최종 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("최종 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							finalDamagePER *= 1D - Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1)) / 100D;
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							finalDamage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("공격력 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("공격력 : +");

					if (s.contains("%"))
					{
						try
						{
							PATKPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							PATK += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("공격력 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("공격력 : -");

					if (s.contains("%"))
					{
						try
						{
							PATKPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							PATK -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("마력 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("마력 : +");

					if (s.contains("%"))
					{
						try
						{
							MATKPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							MATK += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("마력 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("마력 : -");

					if (s.contains("%"))
					{
						try
						{
							MATKPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							MATK -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("경험치 획득량 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("경험치 획득량 : +");

					if (s.contains("%"))
					{
						try
						{
							bounsXPPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							bounsXP += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("경험치 획득량 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("경험치 획득량 : -");

					if (s.contains("%"))
					{
						try
						{
							bounsXPPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							bounsXP -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("몬스터 방어율 무시 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("몬스터 방어율 무시 : +");

					try
					{
						double ignore = Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));

						ignoreDef = ignoreDef + ignore / 100D - ignoreDef * ignore / 100D;
					}

					catch (NumberFormatException exception)
					{

					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("몬스터 방어율 무시 : -"))
				{
					s = MessageUtil.stripColor(s);

					String[] cutter = s.split("몬스터 방어율 무시 : -");

					try
					{
						double ignore = -Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));

						ignoreDef = ignoreDef + ignore / 100D - ignoreDef * ignore / 100D;
					}

					catch (NumberFormatException exception)
					{

					}
				}
			}
		}

		if (hasLeggings)
		{
			ItemMeta meta = leggings.getItemMeta();
			List<String> lore = meta.getLore();

			for (int i = 0; i < lore.size(); i++)
			{
				String s = lore.get(i);

				if (s.contains("§a§m§e§1"))
					s = s.split("§a§m§e§1")[0];

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("STR : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("STR : +");

					if (s.contains("%"))
					{
						try
						{
							STRPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							STR += Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("STR : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("STR : -");

					if (s.contains("%"))
					{
						try
						{
							STRPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							STR -= Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("DEX : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("DEX : +");

					if (s.contains("%"))
					{
						try
						{
							DEXPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							DEX += Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("DEX : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("DEX : -");

					if (s.contains("%"))
					{
						try
						{
							DEXPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							DEX -= Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("INT : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("INT : +");

					if (s.contains("%"))
					{
						try
						{
							INTPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							INT += Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("INT : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("INT : -");

					if (s.contains("%"))
					{
						try
						{
							INTPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							INT -= Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("LUK : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("LUK : +");

					if (s.contains("%"))
					{
						try
						{
							LUKPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							LUK += Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("LUK : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("LUK : -");

					if (s.contains("%"))
					{
						try
						{
							LUKPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							LUK -= Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("올스탯 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("올스탯 : +");

					if (s.contains("%"))
					{
						try
						{
							STRPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							DEXPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							INTPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							LUKPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							STR += Long.parseLong(cutter[1]);
							DEX += Long.parseLong(cutter[1]);
							INT += Long.parseLong(cutter[1]);
							LUK += Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("올스탯 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("올스탯 : -");

					if (s.contains("%"))
					{
						try
						{
							STRPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							DEXPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							INTPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							LUKPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							STR -= Long.parseLong(cutter[1]);
							DEX -= Long.parseLong(cutter[1]);
							INT -= Long.parseLong(cutter[1]);
							LUK -= Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 확률 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("크리티컬 확률 : +");

					try
					{
						critChance += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
					}

					catch (NumberFormatException exception)
					{

					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 확률 : -"))
				{
					s = MessageUtil.stripColor(s);

					String[] cutter = s.split("크리티컬 확률 : -");

					try
					{
						critChance -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
					}

					catch (NumberFormatException exception)
					{

					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 최소 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("크리티컬 최소 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							minCritPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							minCrit += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 최소 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("크리티컬 최소 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							minCritPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							minCrit -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 최대 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("크리티컬 최대 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							maxCritPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							maxCrit += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 최대 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("크리티컬 최대 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							maxCritPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							maxCrit -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("일반 몬스터 공격 시 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("일반 몬스터 공격 시 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							mobDamagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							mobDamage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("일반 몬스터 공격 시 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("일반 몬스터 공격 시 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							mobDamagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							mobDamage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("보스 몬스터 공격 시 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("보스 몬스터 공격 시 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							bossDamagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							bossDamage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("보스 몬스터 공격 시 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("보스 몬스터 공격 시 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							bossDamagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							bossDamage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("마인크래프트 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("마인크래프트 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							minecraftDamagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							minecraftDamage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("마인크래프트 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("마인크래프트 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							minecraftDamagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							minecraftDamage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("스탯 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("스탯 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							statDamagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							statDamage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("스탯 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("스탯 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							statDamagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							statDamage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							damagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							damage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							damagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							damage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("최종 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("최종 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							finalDamagePER *= 1D + Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1)) / 100D;
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							finalDamage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("최종 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("최종 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							finalDamagePER *= 1D - Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1)) / 100D;
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							finalDamage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("공격력 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("공격력 : +");

					if (s.contains("%"))
					{
						try
						{
							PATKPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							PATK += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("공격력 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("공격력 : -");

					if (s.contains("%"))
					{
						try
						{
							PATKPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							PATK -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("마력 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("마력 : +");

					if (s.contains("%"))
					{
						try
						{
							MATKPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							MATK += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("마력 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("마력 : -");

					if (s.contains("%"))
					{
						try
						{
							MATKPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							MATK -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("경험치 획득량 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("경험치 획득량 : +");

					if (s.contains("%"))
					{
						try
						{
							bounsXPPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							bounsXP += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("경험치 획득량 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("경험치 획득량 : -");

					if (s.contains("%"))
					{
						try
						{
							bounsXPPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							bounsXP -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("몬스터 방어율 무시 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("몬스터 방어율 무시 : +");

					try
					{
						double ignore = Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));

						ignoreDef = ignoreDef + ignore / 100D - ignoreDef * ignore / 100D;
					}

					catch (NumberFormatException exception)
					{

					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("몬스터 방어율 무시 : -"))
				{
					s = MessageUtil.stripColor(s);

					String[] cutter = s.split("몬스터 방어율 무시 : -");

					try
					{
						double ignore = -Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));

						ignoreDef = ignoreDef + ignore / 100D - ignoreDef * ignore / 100D;
					}

					catch (NumberFormatException exception)
					{

					}
				}
			}
		}

		if (hasBoots)
		{
			ItemMeta meta = boots.getItemMeta();
			List<String> lore = meta.getLore();

			for (int i = 0; i < lore.size(); i++)
			{
				String s = lore.get(i);

				if (s.contains("§a§m§e§1"))
					s = s.split("§a§m§e§1")[0];

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("STR : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("STR : +");

					if (s.contains("%"))
					{
						try
						{
							STRPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							STR += Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("STR : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("STR : -");

					if (s.contains("%"))
					{
						try
						{
							STRPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							STR -= Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("DEX : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("DEX : +");

					if (s.contains("%"))
					{
						try
						{
							DEXPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							DEX += Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("DEX : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("DEX : -");

					if (s.contains("%"))
					{
						try
						{
							DEXPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							DEX -= Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("INT : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("INT : +");

					if (s.contains("%"))
					{
						try
						{
							INTPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							INT += Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("INT : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("INT : -");

					if (s.contains("%"))
					{
						try
						{
							INTPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							INT -= Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("LUK : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("LUK : +");

					if (s.contains("%"))
					{
						try
						{
							LUKPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							LUK += Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("LUK : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("LUK : -");

					if (s.contains("%"))
					{
						try
						{
							LUKPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							LUK -= Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("올스탯 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("올스탯 : +");

					if (s.contains("%"))
					{
						try
						{
							STRPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							DEXPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							INTPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							LUKPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							STR += Long.parseLong(cutter[1]);
							DEX += Long.parseLong(cutter[1]);
							INT += Long.parseLong(cutter[1]);
							LUK += Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("올스탯 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("올스탯 : -");

					if (s.contains("%"))
					{
						try
						{
							STRPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							DEXPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							INTPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							LUKPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							STR -= Long.parseLong(cutter[1]);
							DEX -= Long.parseLong(cutter[1]);
							INT -= Long.parseLong(cutter[1]);
							LUK -= Long.parseLong(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 확률 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("크리티컬 확률 : +");

					try
					{
						critChance += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
					}

					catch (NumberFormatException exception)
					{

					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 확률 : -"))
				{
					s = MessageUtil.stripColor(s);

					String[] cutter = s.split("크리티컬 확률 : -");

					try
					{
						critChance -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
					}

					catch (NumberFormatException exception)
					{

					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 최소 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("크리티컬 최소 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							minCritPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							minCrit += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 최소 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("크리티컬 최소 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							minCritPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							minCrit -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 최대 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("크리티컬 최대 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							maxCritPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							maxCrit += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 최대 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("크리티컬 최대 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							maxCritPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							maxCrit -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("일반 몬스터 공격 시 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("일반 몬스터 공격 시 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							mobDamagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							mobDamage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("일반 몬스터 공격 시 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("일반 몬스터 공격 시 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							mobDamagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							mobDamage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("보스 몬스터 공격 시 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("보스 몬스터 공격 시 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							bossDamagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							bossDamage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("보스 몬스터 공격 시 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("보스 몬스터 공격 시 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							bossDamagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							bossDamage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("마인크래프트 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("마인크래프트 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							minecraftDamagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							minecraftDamage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("마인크래프트 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("마인크래프트 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							minecraftDamagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							minecraftDamage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("스탯 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("스탯 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							statDamagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							statDamage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("스탯 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("스탯 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							statDamagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							statDamage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							damagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							damage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							damagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							damage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("최종 대미지 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("최종 대미지 : +");

					if (s.contains("%"))
					{
						try
						{
							finalDamagePER *= 1D + Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1)) / 100D;
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							finalDamage += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("최종 대미지 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("최종 대미지 : -");

					if (s.contains("%"))
					{
						try
						{
							finalDamagePER *= 1D - Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1)) / 100D;
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							finalDamage -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("공격력 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("공격력 : +");

					if (s.contains("%"))
					{
						try
						{
							PATKPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							PATK += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("공격력 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("공격력 : -");

					if (s.contains("%"))
					{
						try
						{
							PATKPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							PATK -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("마력 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("마력 : +");

					if (s.contains("%"))
					{
						try
						{
							MATKPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							MATK += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("마력 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("마력 : -");

					if (s.contains("%"))
					{
						try
						{
							MATKPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							MATK -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("경험치 획득량 : +"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("경험치 획득량 : +");

					if (s.contains("%"))
					{
						try
						{
							bounsXPPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							bounsXP += Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}

				if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("경험치 획득량 : -"))
				{
					s = MessageUtil.stripColor(s);
					String[] cutter = s.split("경험치 획득량 : -");

					if (s.contains("%"))
					{
						try
						{
							bounsXPPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
						}

						catch (NumberFormatException exception)
						{

						}
					}

					else if (!s.contains("%"))
					{
						try
						{
							bounsXP -= Double.parseDouble(cutter[1]);
						}

						catch (NumberFormatException exception)
						{

						}
					}
				}
			}
		}

		for (int i = 0; i < player.getInventory().getSize(); i++)
		{
			if (player.getInventory().getItem(i) != null && player.getInventory().getItem(i).getType() != Material.AIR && player.getInventory().getItem(i).hasItemMeta()
					&& player.getInventory().getItem(i).getItemMeta().hasLore())
			{
				ItemStack item = player.getInventory().getItem(i);
				ItemMeta meta = item.getItemMeta();
				List<String> lore = meta.getLore();

				boolean isInventoryItem = false;

				for (int j = 0; j < lore.size(); j++)
				{
					String str = MessageUtil.stripColor(lore.get(j));

					if (str.contains("슬롯 분류 : ") && str.contains("인벤토리"))
					{
						isInventoryItem = true;
						break;
					}
				}

				for (int j = 0; j < lore.size(); j++)
				{
					String str = MessageUtil.stripColor(lore.get(j));

					if (str.contains("슬롯 분류 : ") && (str.contains("주로 사용하는 손") || str.contains("다른 손") || str.contains("머리") || str.contains("몸") || str.contains("다리") || str.contains("발")))
					{
						isInventoryItem = false;
						break;
					}
				}

				if (isInventoryItem)
				{
					for (int j = 0; j < lore.size(); j++)
					{
						String s = lore.get(j);

						if (s.contains("§a§m§e§1"))
							s = s.split("§a§m§e§1")[0];

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("STR : +"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("STR : +");

							if (s.contains("%"))
							{
								try
								{
									STRPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
								}

								catch (NumberFormatException exception)
								{

								}
							}

							else if (!s.contains("%"))
							{
								try
								{
									STR += Long.parseLong(cutter[1]);
								}

								catch (NumberFormatException exception)
								{

								}
							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("STR : -"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("STR : -");

							if (s.contains("%"))
							{
								try
								{
									STRPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
								}

								catch (NumberFormatException exception)
								{

								}
							}

							else if (!s.contains("%"))
							{
								try
								{
									STR -= Long.parseLong(cutter[1]);
								}

								catch (NumberFormatException exception)
								{

								}
							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("DEX : +"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("DEX : +");

							if (s.contains("%"))
							{
								try
								{
									DEXPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
								}

								catch (NumberFormatException exception)
								{

								}
							}

							else if (!s.contains("%"))
							{
								try
								{
									DEX += Long.parseLong(cutter[1]);
								}

								catch (NumberFormatException exception)
								{

								}
							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("DEX : -"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("DEX : -");

							if (s.contains("%"))
							{
								try
								{
									DEXPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
								}

								catch (NumberFormatException exception)
								{

								}
							}

							else if (!s.contains("%"))
							{
								try
								{
									DEX -= Long.parseLong(cutter[1]);
								}

								catch (NumberFormatException exception)
								{

								}
							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("INT : +"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("INT : +");

							if (s.contains("%"))
							{
								try
								{
									INTPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
								}

								catch (NumberFormatException exception)
								{

								}
							}

							else if (!s.contains("%"))
							{
								try
								{
									INT += Long.parseLong(cutter[1]);
								}

								catch (NumberFormatException exception)
								{

								}
							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("INT : -"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("INT : -");

							if (s.contains("%"))
							{
								try
								{
									INTPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
								}

								catch (NumberFormatException exception)
								{

								}
							}

							else if (!s.contains("%"))
							{
								try
								{
									INT -= Long.parseLong(cutter[1]);
								}

								catch (NumberFormatException exception)
								{

								}
							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("LUK : +"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("LUK : +");

							if (s.contains("%"))
							{
								try
								{
									LUKPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
								}

								catch (NumberFormatException exception)
								{

								}
							}

							else if (!s.contains("%"))
							{
								try
								{
									LUK += Long.parseLong(cutter[1]);
								}

								catch (NumberFormatException exception)
								{

								}
							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("LUK : -"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("LUK : -");

							if (s.contains("%"))
							{
								try
								{
									LUKPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
								}

								catch (NumberFormatException exception)
								{

								}
							}

							else if (!s.contains("%"))
							{
								try
								{
									LUK -= Long.parseLong(cutter[1]);
								}

								catch (NumberFormatException exception)
								{

								}
							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("올스탯 : +"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("올스탯 : +");

							if (s.contains("%"))
							{
								try
								{
									STRPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
									DEXPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
									INTPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
									LUKPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
								}

								catch (NumberFormatException exception)
								{

								}
							}

							else if (!s.contains("%"))
							{
								try
								{
									STR += Long.parseLong(cutter[1]);
									DEX += Long.parseLong(cutter[1]);
									INT += Long.parseLong(cutter[1]);
									LUK += Long.parseLong(cutter[1]);
								}

								catch (NumberFormatException exception)
								{

								}
							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("올스탯 : -"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("올스탯 : -");

							if (s.contains("%"))
							{
								try
								{
									STRPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
									DEXPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
									INTPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
									LUKPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
								}

								catch (NumberFormatException exception)
								{

								}
							}

							else if (!s.contains("%"))
							{
								try
								{
									STR -= Long.parseLong(cutter[1]);
									DEX -= Long.parseLong(cutter[1]);
									INT -= Long.parseLong(cutter[1]);
									LUK -= Long.parseLong(cutter[1]);
								}

								catch (NumberFormatException exception)
								{

								}
							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 확률 : +"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("크리티컬 확률 : +");

							try
							{
								critChance += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							}

							catch (NumberFormatException exception)
							{

							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 확률 : -"))
						{
							s = MessageUtil.stripColor(s);

							String[] cutter = s.split("크리티컬 확률 : -");

							try
							{
								critChance -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
							}

							catch (NumberFormatException exception)
							{

							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 최소 대미지 : +"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("크리티컬 최소 대미지 : +");

							if (s.contains("%"))
							{
								try
								{
									minCritPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
								}

								catch (NumberFormatException exception)
								{

								}
							}

							else if (!s.contains("%"))
							{
								try
								{
									minCrit += Double.parseDouble(cutter[1]);
								}

								catch (NumberFormatException exception)
								{

								}
							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 최소 대미지 : -"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("크리티컬 최소 대미지 : -");

							if (s.contains("%"))
							{
								try
								{
									minCritPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
								}

								catch (NumberFormatException exception)
								{

								}
							}

							else if (!s.contains("%"))
							{
								try
								{
									minCrit -= Double.parseDouble(cutter[1]);
								}

								catch (NumberFormatException exception)
								{

								}
							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 최대 대미지 : +"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("크리티컬 최대 대미지 : +");

							if (s.contains("%"))
							{
								try
								{
									maxCritPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
								}

								catch (NumberFormatException exception)
								{

								}
							}

							else if (!s.contains("%"))
							{
								try
								{
									maxCrit += Double.parseDouble(cutter[1]);
								}

								catch (NumberFormatException exception)
								{

								}
							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("크리티컬 최대 대미지 : -"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("크리티컬 최대 대미지 : -");

							if (s.contains("%"))
							{
								try
								{
									maxCritPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
								}

								catch (NumberFormatException exception)
								{

								}
							}

							else if (!s.contains("%"))
							{
								try
								{
									maxCrit -= Double.parseDouble(cutter[1]);
								}

								catch (NumberFormatException exception)
								{

								}
							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("일반 몬스터 공격 시 대미지 : +"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("일반 몬스터 공격 시 대미지 : +");

							if (s.contains("%"))
							{
								try
								{
									mobDamagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
								}

								catch (NumberFormatException exception)
								{

								}
							}

							else if (!s.contains("%"))
							{
								try
								{
									mobDamage += Double.parseDouble(cutter[1]);
								}

								catch (NumberFormatException exception)
								{

								}
							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("일반 몬스터 공격 시 대미지 : -"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("일반 몬스터 공격 시 대미지 : -");

							if (s.contains("%"))
							{
								try
								{
									mobDamagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
								}

								catch (NumberFormatException exception)
								{

								}
							}

							else if (!s.contains("%"))
							{
								try
								{
									mobDamage -= Double.parseDouble(cutter[1]);
								}

								catch (NumberFormatException exception)
								{

								}
							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("보스 몬스터 공격 시 대미지 : +"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("보스 몬스터 공격 시 대미지 : +");

							if (s.contains("%"))
							{
								try
								{
									bossDamagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
								}

								catch (NumberFormatException exception)
								{

								}
							}

							else if (!s.contains("%"))
							{
								try
								{
									bossDamage += Double.parseDouble(cutter[1]);
								}

								catch (NumberFormatException exception)
								{

								}
							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("보스 몬스터 공격 시 대미지 : -"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("보스 몬스터 공격 시 대미지 : -");

							if (s.contains("%"))
							{
								try
								{
									bossDamagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
								}

								catch (NumberFormatException exception)
								{

								}
							}

							else if (!s.contains("%"))
							{
								try
								{
									bossDamage -= Double.parseDouble(cutter[1]);
								}

								catch (NumberFormatException exception)
								{

								}
							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("마인크래프트 대미지 : +"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("마인크래프트 대미지 : +");

							if (s.contains("%"))
							{
								try
								{
									minecraftDamagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
								}

								catch (NumberFormatException exception)
								{

								}
							}

							else if (!s.contains("%"))
							{
								try
								{
									minecraftDamage += Double.parseDouble(cutter[1]);
								}

								catch (NumberFormatException exception)
								{

								}
							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("마인크래프트 대미지 : -"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("마인크래프트 대미지 : -");

							if (s.contains("%"))
							{
								try
								{
									minecraftDamagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
								}

								catch (NumberFormatException exception)
								{

								}
							}

							else if (!s.contains("%"))
							{
								try
								{
									minecraftDamage -= Double.parseDouble(cutter[1]);
								}

								catch (NumberFormatException exception)
								{

								}
							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("스탯 대미지 : +"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("스탯 대미지 : +");

							if (s.contains("%"))
							{
								try
								{
									statDamagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
								}

								catch (NumberFormatException exception)
								{

								}
							}

							else if (!s.contains("%"))
							{
								try
								{
									statDamage += Double.parseDouble(cutter[1]);
								}

								catch (NumberFormatException exception)
								{

								}
							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("스탯 대미지 : -"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("스탯 대미지 : -");

							if (s.contains("%"))
							{
								try
								{
									statDamagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
								}

								catch (NumberFormatException exception)
								{

								}
							}

							else if (!s.contains("%"))
							{
								try
								{
									statDamage -= Double.parseDouble(cutter[1]);
								}

								catch (NumberFormatException exception)
								{

								}
							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("대미지 : +"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("대미지 : +");

							if (s.contains("%"))
							{
								try
								{
									damagePER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
								}

								catch (NumberFormatException exception)
								{

								}
							}

							else if (!s.contains("%"))
							{
								try
								{
									damage += Double.parseDouble(cutter[1]);
								}

								catch (NumberFormatException exception)
								{

								}
							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("대미지 : -"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("대미지 : -");

							if (s.contains("%"))
							{
								try
								{
									damagePER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
								}

								catch (NumberFormatException exception)
								{

								}
							}

							else if (!s.contains("%"))
							{
								try
								{
									damage -= Double.parseDouble(cutter[1]);
								}

								catch (NumberFormatException exception)
								{

								}
							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("최종 대미지 : +"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("최종 대미지 : +");

							if (s.contains("%"))
							{
								try
								{
									finalDamagePER *= 1D + Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1)) / 100D;
								}

								catch (NumberFormatException exception)
								{

								}
							}

							else if (!s.contains("%"))
							{
								try
								{
									finalDamage += Double.parseDouble(cutter[1]);
								}

								catch (NumberFormatException exception)
								{

								}
							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("최종 대미지 : -"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("최종 대미지 : -");

							if (s.contains("%"))
							{
								try
								{
									finalDamagePER *= 1D - Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1)) / 100D;
								}

								catch (NumberFormatException exception)
								{

								}
							}

							else if (!s.contains("%"))
							{
								try
								{
									finalDamage -= Double.parseDouble(cutter[1]);
								}

								catch (NumberFormatException exception)
								{

								}
							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("공격력 : +"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("공격력 : +");

							if (s.contains("%"))
							{
								try
								{
									PATKPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
								}

								catch (NumberFormatException exception)
								{

								}
							}

							else if (!s.contains("%"))
							{
								try
								{
									PATK += Double.parseDouble(cutter[1]);
								}

								catch (NumberFormatException exception)
								{

								}
							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("공격력 : -"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("공격력 : -");

							if (s.contains("%"))
							{
								try
								{
									PATKPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
								}

								catch (NumberFormatException exception)
								{

								}
							}

							else if (!s.contains("%"))
							{
								try
								{
									PATK -= Double.parseDouble(cutter[1]);
								}

								catch (NumberFormatException exception)
								{

								}
							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("마력 : +"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("마력 : +");

							if (s.contains("%"))
							{
								try
								{
									MATKPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
								}

								catch (NumberFormatException exception)
								{

								}
							}

							else if (!s.contains("%"))
							{
								try
								{
									MATK += Double.parseDouble(cutter[1]);
								}

								catch (NumberFormatException exception)
								{

								}
							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("마력 : -"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("마력 : -");

							if (s.contains("%"))
							{
								try
								{
									MATKPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
								}

								catch (NumberFormatException exception)
								{

								}
							}

							else if (!s.contains("%"))
							{
								try
								{
									MATK -= Double.parseDouble(cutter[1]);
								}

								catch (NumberFormatException exception)
								{

								}
							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("경험치 획득량 : +"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("경험치 획득량 : +");

							if (s.contains("%"))
							{
								try
								{
									bounsXPPER += Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
								}

								catch (NumberFormatException exception)
								{

								}
							}

							else if (!s.contains("%"))
							{
								try
								{
									bounsXP += Double.parseDouble(cutter[1]);
								}

								catch (NumberFormatException exception)
								{

								}
							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("경험치 획득량 : -"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("경험치 획득량 : -");

							if (s.contains("%"))
							{
								try
								{
									bounsXPPER -= Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));
								}

								catch (NumberFormatException exception)
								{

								}
							}

							else if (!s.contains("%"))
							{
								try
								{
									bounsXP -= Double.parseDouble(cutter[1]);
								}

								catch (NumberFormatException exception)
								{

								}
							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("몬스터 방어율 무시 : +"))
						{
							s = MessageUtil.stripColor(s);
							String[] cutter = s.split("몬스터 방어율 무시 : +");

							try
							{
								double ignore = Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));

								ignoreDef = ignoreDef + ignore / 100D - ignoreDef * ignore / 100D;
							}

							catch (NumberFormatException exception)
							{

							}
						}

						if (s.length() > 6 && s.startsWith(PREFIX_1) && MessageUtil.stripColor(s.substring(6, s.length())).startsWith("몬스터 방어율 무시 : -"))
						{
							s = MessageUtil.stripColor(s);

							String[] cutter = s.split("몬스터 방어율 무시 : -");

							try
							{
								double ignore = -Double.parseDouble(cutter[1].substring(0, cutter[1].length() - 1));

								ignoreDef = ignoreDef + ignore / 100D - ignoreDef * ignore / 100D;
							}

							catch (NumberFormatException exception)
							{

							}
						}
					}
				}
			}
		}

		STR += Math.floor(STR * STRPER / 100D);
		DEX += Math.floor(DEX * DEXPER / 100D);
		INT += Math.floor(INT * INTPER / 100D);
		LUK += Math.floor(LUK * LUKPER / 100D);

		STR -= stat[1];
		DEX -= stat[2];
		INT -= stat[3];
		LUK -= stat[4];

		PATK += Math.floor(PATK * PATKPER / 100D);
		MATK += Math.floor(MATK * MATKPER / 100D);

		bounsXP += Math.floor(bounsXP * bounsXPPER / 100D);

		if (STR + stat[1] < 0)
			STR = -stat[1];

		if (DEX + stat[2] < 0)
			DEX = -stat[2];

		if (INT + stat[3] < 0)
			INT = -stat[3];

		if (LUK + stat[4] < 0)
			LUK = -stat[4];

		if (critChance < 0D)
			critChance = 0D;

		if (critChance > 100D)
			critChance = 100D;

		if (minCritPER < 0D)
			minCritPER = 0D;

		if (maxCritPER < 0D)
			maxCritPER = 0D;

		if (minCritPER > maxCritPER)
			minCritPER = maxCritPER;

		if (mobDamagePER < -100D)
			mobDamagePER = -100D;

		if (bossDamagePER < -100D)
			bossDamagePER = -100D;

		if (minecraftDamagePER < -100D)
			minecraftDamagePER = -100D;

		if (statDamagePER < -100D)
			statDamagePER = -100D;

		if (damagePER < -100D)
			damagePER = -100D;

		if (finalDamagePER < 0D)
			finalDamagePER = 0D;

		if (PATK < 1)
			PATK = 1;

		if (MATK < 1)
			MATK = 1;
		
		if (ignoreDef > 1D)
			ignoreDef = 1D;
		
		if (ignoreDef < 0D)
			ignoreDef = 0D;

		switch (type)
		{
			case BOSS_DAMAGE:
				return bossDamage;
			case BOSS_DAMAGE_PER:
				return bossDamagePER;
			case CRIT_CHANCE:
				return critChance;
			case DAMAGE:
				return damage;
			case DAMAGE_PER:
				return damagePER;
			case DEX:
				return DEX;
			case FINAL_DAMAGE:
				return finalDamage;
			case FINAL_DAMAGE_PER:
				return finalDamagePER * 100D - 100D;
			case INT:
				return INT;
			case LUK:
				return LUK;
			case MAX_CRIT:
				return maxCrit;
			case MAX_CRIT_PER:
				return maxCritPER;
			case MIN_CRIT:
				return minCrit;
			case MIN_CRIT_PER:
				return minCritPER;
			case MINECRAFT_DAMAGE:
				return minecraftDamage;
			case MINECRAFT_DAMAGE_PER:
				return minecraftDamagePER;
			case MOB_DAMAGE:
				return mobDamage;
			case MOB_DAMAGE_PER:
				return mobDamagePER;
			case STAT_DAMAGE:
				return statDamage;
			case STAT_DAMAGE_PER:
				return statDamagePER;
			case PROFICIENCY:
				return proficiency;
			case STR:
				return STR;
			case PATK:
				return PATK;
			case MATK:
				return MATK;
			case BONUS_EXP:
				return bounsXP;
			case BONUS_EXP_PER:
				return bounsXPPER;
			case IGNORE_DEF:
				return ignoreDef;
			default:
				return 0D;
		}
	}*/
}

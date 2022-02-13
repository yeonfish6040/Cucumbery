package com.jho5245.cucumbery.util.rpg;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class StatGUI
{
/*	private static final StatGUI statGUI = new StatGUI();

	DecimalFormat d1 = new DecimalFormat("#,###.##"), d2 = new DecimalFormat("#,###");

	public static StatGUI getStatGUI()
	{
		return statGUI;
	}*/

	public void stack(String display, Material type, int durability, int amount, List<String> lore, boolean hideflag, int loc, Inventory inv)
	{
		ItemStack item = new ItemStack(type, amount);
		ItemMeta itemMeta = item.getItemMeta();
		((Damageable) itemMeta).setDamage(durability);
		itemMeta.setDisplayName(display);
		itemMeta.setLore(lore);
		if (hideflag)
			hideFlag(itemMeta);
		item.setItemMeta(itemMeta);
		inv.setItem(loc, item);
	}

	public void stack(ItemStack item, int loc, Inventory inv)
	{
		inv.setItem(loc, item);
	}

	public void hideFlag(ItemMeta meta)
	{
		meta.addItemFlags(ItemFlag.values());
	}
/*
	public void statGUI(Player player)
	{
		long[] stat = new long[StatManager.size];

		stat = StatManager.getStatManager().getStat(player);

		Inventory gui = Bukkit.createInventory(null, 54, Constant.CANCEL_STRING + "§8스탯");

		for (int i = 0; i <= 9; i++)
		{
			this.stack("&r", Material.WHITE_STAINED_GLASS_PANE, 0, 1, null, true, i, gui);
		}

		this.stack("&r", Material.WHITE_STAINED_GLASS_PANE, 0, 1, null, true, 18, gui);
		this.stack("&r", Material.WHITE_STAINED_GLASS_PANE, 0, 1, null, true, 27, gui);
		this.stack("&r", Material.WHITE_STAINED_GLASS_PANE, 0, 1, null, true, 36, gui);
		this.stack("&r", Material.WHITE_STAINED_GLASS_PANE, 0, 1, null, true, 37, gui);
		this.stack("&r", Material.WHITE_STAINED_GLASS_PANE, 0, 1, null, true, 17, gui);
		this.stack("&r", Material.WHITE_STAINED_GLASS_PANE, 0, 1, null, true, 26, gui);
		this.stack("&r", Material.WHITE_STAINED_GLASS_PANE, 0, 1, null, true, 35, gui);
		this.stack("&r", Material.WHITE_STAINED_GLASS_PANE, 0, 1, null, true, 43, gui);
		this.stack("&r", Material.WHITE_STAINED_GLASS_PANE, 0, 1, null, true, 44, gui);
		this.stack("&r", Material.WHITE_STAINED_GLASS_PANE, 0, 1, null, true, 37, gui);
		this.stack("&r", Material.WHITE_STAINED_GLASS_PANE, 0, 1, null, true, 46, gui);
		this.stack("&r", Material.WHITE_STAINED_GLASS_PANE, 0, 1, null, true, 47, gui);
		this.stack("&r", Material.WHITE_STAINED_GLASS_PANE, 0, 1, null, true, 48, gui);
		this.stack("&r", Material.WHITE_STAINED_GLASS_PANE, 0, 1, null, true, 49, gui);
		this.stack("&r", Material.WHITE_STAINED_GLASS_PANE, 0, 1, null, true, 50, gui);
		this.stack("&r", Material.WHITE_STAINED_GLASS_PANE, 0, 1, null, true, 51, gui);
		this.stack("&r", Material.WHITE_STAINED_GLASS_PANE, 0, 1, null, true, 52, gui);

		this.stack("§b메인 메뉴로&r", Material.BOOKSHELF, 0, 1, null, true, 53, gui);

		this.stack("§b메인 메뉴로&r", Material.BOOKSHELF, 0, 1, null, true, 45, gui);

		String s1 = "§7크리티컬 확률 : §6" + d1.format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.CRIT_CHANCE)) + "%";
		String s2 = "";

		if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MIN_CRIT) >= 0)
		{
			s2 = "§7크리티컬 최소 추가 대미지 : §6+" + d1.format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MIN_CRIT));
		}

		else
		{
			s2 = "§7크리티컬 최소 추가 대미지 : §6" + d1.format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MIN_CRIT));
		}
		String s3 = "§7크리티컬 최소 : §6" + d1.format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MIN_CRIT_PER)) + "%";

		String s4 = "";

		if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MAX_CRIT) >= 0)
		{
			s4 = "§7크리티컬 최대 추가 대미지 : §6+" + d1.format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MAX_CRIT));
		}

		else
		{
			s4 = "§7크리티컬 최대 추가 대미지 : §6" + d1.format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MAX_CRIT));
		}

		String s5 = "§7크리티컬 최대 : §6" + d1.format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MAX_CRIT_PER)) + "%";

		String s6 = "";

		if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.DAMAGE) >= 0)
		{
			s6 = "§7추가 대미지 : §6+" + d1.format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.DAMAGE));
		}

		else
		{
			s6 = "§7추가 대미지 : §6" + d1.format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.DAMAGE));
		}

		String s7 = "";

		if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.DAMAGE_PER) >= 0)
		{
			s7 = "§7대미지 : §6+" + d1.format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.DAMAGE_PER)) + "%";
		}

		else
		{
			s7 = "§7대미지 : §6" + d1.format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.DAMAGE_PER)) + "%";
		}

		String s8 = "";

		if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MINECRAFT_DAMAGE_PER) >= 0)
		{
			s8 = "§7마인크래프트 대미지 : §6+" + d1.format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MINECRAFT_DAMAGE_PER)) + "%";
		}

		else
		{
			s8 = "§7마인크래프트 대미지 : §6" + d1.format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MINECRAFT_DAMAGE_PER)) + "%";
		}

		String s9 = "";

		if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MINECRAFT_DAMAGE) >= 0)
		{
			s9 = "§7추가 마인크래프트 대미지 : §6+" + d1.format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MINECRAFT_DAMAGE));
		}

		else
		{
			s9 = "§7추가 마인크래프트 대미지 : §6" + d1.format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MINECRAFT_DAMAGE));
		}

		String s10 = "";

		if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.STAT_DAMAGE_PER) >= 0)
		{
			s10 = "§7스탯 대미지 : §6+" + d1.format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.STAT_DAMAGE_PER)) + "%";
		}

		else
		{
			s10 = "§7스탯 대미지 : §6" + d1.format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.STAT_DAMAGE_PER)) + "%";
		}

		String s11 = "";

		if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.STAT_DAMAGE) >= 0)
		{
			s11 = "§7추가 스탯 대미지 : §6+" + d1.format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.STAT_DAMAGE));
		}

		else
		{
			s11 = "§7추가 스탯 대미지 : §6" + d1.format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.STAT_DAMAGE));
		}

		String s12 = "";

		if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MOB_DAMAGE_PER) >= 0)
		{
			s12 = "§7일반 몬스터 공격 시 대미지 : §6+" + d1.format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MOB_DAMAGE_PER)) + "%";
		}

		else
		{
			s12 = "§7일반 몬스터 공격 시 대미지 : §6" + d1.format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MOB_DAMAGE_PER)) + "%";
		}

		String s13 = "";

		if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MOB_DAMAGE) >= 0)
		{
			s13 = "§7추가 일반 몬스터 공격 시 대미지 : §6+" + d1.format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MOB_DAMAGE));
		}

		else
		{
			s13 = "§7추가 일반 몬스터 공격 시 대미지 : §6" + d1.format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MOB_DAMAGE));
		}

		String s14 = "";

		if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.BOSS_DAMAGE_PER) >= 0)
		{
			s14 = "§7보스 몬스터 공격 시 대미지 : §6+" + d1.format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.BOSS_DAMAGE_PER)) + "%";
		}

		else
		{
			s14 = "§7보스 몬스터 공격 시 대미지 : §6" + d1.format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.BOSS_DAMAGE_PER)) + "%";
		}

		String s15 = "";

		if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.BOSS_DAMAGE) >= 0)
		{
			s15 = "§7추가 보스 몬스터 공격 시 대미지 : §6+" + d1.format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.BOSS_DAMAGE));
		}

		else
		{
			s15 = "§7추가 보스 몬스터 공격 시 대미지 : §6" + d1.format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.BOSS_DAMAGE));
		}

		String s16 = "§7최종 대미지 : §6" + d1.format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.FINAL_DAMAGE_PER) + 100D) + "%";

		String s17 = "§7추가 최종 대미지 : §6" + ((GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.FINAL_DAMAGE) >= 0) ? "+" : "")
				+ d1.format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.FINAL_DAMAGE));

		String s18 = "§7몬스터 방어율 무시 : §6" + d1.format(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.IGNORE_DEF) * 100D) + "%";

		ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);

		this.stack("§a§l[설명]", Material.OAK_SIGN, 0, 1, Arrays.asList("§e대미지 계산은 위에서 아래 순으로,", "§e%만큼 곱적용 후 %가 아닌 값이 합산 됩니다", "§e크리티컬 최소, 최대는 % 사이의 값을 곱적용 후,",
				"§e추가 크리티컬 최소 대미지, 추가 크리티컬", "§e최대 대미지가 합산되어 그 사이의 값이 적용됩니다", "§c크리티컬 최소 대미지는 크리티컬 최대 대미지보다", "§c높을 수 없습니다 (합산 이후 값 포함)", "§e크리티컬 확률은 최대 100% 적용"), true, 39, gui);

		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		meta.setOwningPlayer(player);

		StatManager.getStatManager().setStat(player, stat);

		meta.setDisplayName("§a§l[정보]");

		int level = (int) stat[7];

		long currentExp = stat[8], maxExp = Level.getExp(level);

		meta.setLore(Arrays.asList("§a레벨 : §b" + level, "§e경험치 : §a" + d1.format(currentExp) + "§b / §a" + d1.format(maxExp), s8, s9, s10, s11, s7, s6, s12, s13, s14, s15, s1, s3, s2,
				s5, s4, s16, s17, s18));

		this.hideFlag(meta);

		skull.setItemMeta(meta);

		this.stack(skull, 40, gui);

		this.stack("§b[스탯 공격력]", Material.TOTEM_OF_UNDYING, 0, 1,
				Arrays
						.asList(
								"§7근거리 공격력 : §6"
										+ d1.format(
												DamageCalculator.getDamageCalc()
														.meleeMinDamage(GetStat.getStatClass().getLongStat(player, GetStat.Stat.STR), GetStat.getStatClass().getLongStat(player, GetStat.Stat.DEX),
																GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.PATK), GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.PROFICIENCY)))
										+ "§e ~ §6"
										+ d1.format(DamageCalculator.getDamageCalc().meleeMaxDamage(GetStat.getStatClass().getLongStat(player, GetStat.Stat.STR),
												GetStat.getStatClass().getLongStat(player, GetStat.Stat.DEX), GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.PATK))),
								"§7원거리 공격력 : §6"
										+ d1.format(
												DamageCalculator.getDamageCalc()
														.rangeMinDamage(GetStat.getStatClass().getLongStat(player, GetStat.Stat.STR), GetStat.getStatClass().getLongStat(player, GetStat.Stat.DEX),
																GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.PATK), GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.PROFICIENCY)))
										+ "§e ~ §6"
										+ d1.format(DamageCalculator.getDamageCalc().rangeMaxDamage(GetStat.getStatClass().getLongStat(player, GetStat.Stat.STR),
												GetStat.getStatClass().getLongStat(player, GetStat.Stat.DEX), GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.PATK))),
								"§7마법 공격력 : §6"
										+ d1.format(DamageCalculator.getDamageCalc().magicMinDamage(GetStat.getStatClass().getLongStat(player, GetStat.Stat.INT),
												GetStat.getStatClass().getLongStat(player, GetStat.Stat.LUK), GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MATK),
												GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.PROFICIENCY)))
										+ "§e ~ §6" + d1.format(DamageCalculator.getDamageCalc().magicMaxDamage(GetStat.getStatClass().getLongStat(player, GetStat.Stat.INT),
												GetStat.getStatClass().getLongStat(player, GetStat.Stat.LUK), GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.MATK)))),
				true, 41, gui);

		if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.STR) == 0D)
		{
			this.stack("§c[STR]", Material.RED_STAINED_GLASS_PANE, 14, 1,
					Arrays.asList("&r근거리 공격력에 크게 관여합니다", "&r원거리 공격력에 조금 관여합니다", "&r현재 STR : §e" + GetStat.getStatClass().getLongStat(player, GetStat.Stat.STR)), true, 11, gui);
		}

		else if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.STR) < 0D)
		{
			this.stack("§c[STR]", Material.RED_STAINED_GLASS_PANE, 14, 1,
					Arrays.asList("&r근거리 공격력에 크게 관여합니다", "&r원거리 공격력에 조금 관여합니다", "&r현재 STR : §e" + GetStat.getStatClass().getLongStat(player, GetStat.Stat.STR) + "&r (§e" + stat[1]
							+ "&r - §e" + (long) Math.abs(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.STR)) + "&r)"),
					true, 11, gui);
		}

		else if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.STR) > 0D)
		{
			this.stack("§c[STR]", Material.RED_STAINED_GLASS_PANE, 14, 1,
					Arrays.asList("&r근거리 공격력에 크게 관여합니다", "&r원거리 공격력에 조금 관여합니다", "&r현재 STR : §e" + GetStat.getStatClass().getLongStat(player, GetStat.Stat.STR) + "&r (§e" + stat[1]
							+ "&r + §e" + (long) Math.abs(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.STR)) + "&r)"),
					true, 11, gui);
		}

		if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.STR) == 0D)
		{
			this.stack("§c[STR+]", Material.RED_STAINED_GLASS, 14, 1,
					Arrays.asList("§7현재 남은 스탯 포인트 : §6" + stat[0], "§e좌클릭&r으로 STR을 §e1 &r만큼 증가 시킵니다", "§e우클릭&r으로 STR을 §e10 &r만큼 증가 시킵니다", "§e시프트 + 좌클릭&r으로 STR을 §e100 &r만큼 증가 시킵니다",
							"§e시프트 + 우클릭&r으로 현재 가진 스탯 포인트를 전부 다 사용하여 STR를 증가 시킵니다", "&r현재 STR : §e" + GetStat.getStatClass().getLongStat(player, GetStat.Stat.STR)),
					true, 20, gui);
		}

		else if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.STR) < 0D)
		{
			this.stack("§c[STR+]", Material.RED_STAINED_GLASS, 14, 1,
					Arrays.asList("§7현재 남은 스탯 포인트 : §6" + stat[0], "§e좌클릭&r으로 STR을 §e1 &r만큼 증가 시킵니다", "§e우클릭&r으로 STR을 §e10 &r만큼 증가 시킵니다", "§e시프트 + 좌클릭&r으로 STR을 §e100 &r만큼 증가 시킵니다",
							"§e시프트 + 우클릭&r으로 현재 가진 스탯 포인트를 전부 다 사용하여 STR를 증가 시킵니다", "&r현재 STR : §e" + GetStat.getStatClass().getLongStat(player, GetStat.Stat.STR) + "&r (§e" + stat[1]
									+ "&r - §e" + (long) Math.abs(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.STR)) + "&r)"),
					true, 20, gui);
		}

		else if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.STR) > 0D)
		{
			this.stack("§c[STR+]", Material.RED_STAINED_GLASS, 14, 1,
					Arrays.asList("§7현재 남은 스탯 포인트 : §6" + stat[0], "§e좌클릭&r으로 STR을 §e1 &r만큼 증가 시킵니다", "§e우클릭&r으로 STR을 §e10 &r만큼 증가 시킵니다", "§e시프트 + 좌클릭&r으로 STR을 §e100 &r만큼 증가 시킵니다",
							"§e시프트 + 우클릭&r으로 현재 가진 스탯 포인트를 전부 다 사용하여 STR를 증가 시킵니다", "&r현재 STR : §e" + GetStat.getStatClass().getLongStat(player, GetStat.Stat.STR) + "&r (§e" + stat[1]
									+ "&r + §e" + (long) Math.abs(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.STR)) + "&r)"),
					true, 20, gui);
		}

		if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.DEX) == 0D)
		{
			this.stack("§b[DEX]", Material.LIGHT_BLUE_STAINED_GLASS_PANE, 3, 1,
					Arrays.asList("&r원거리 공격력에 크게 관여합니다", "&r근거리 공격력에 조금 관여합니다", "&r현재 DEX : §e" + GetStat.getStatClass().getLongStat(player, GetStat.Stat.DEX)), true, 12, gui);
		}

		else if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.DEX) < 0D)
		{
			this.stack("§b[DEX]", Material.LIGHT_BLUE_STAINED_GLASS_PANE, 3, 1,
					Arrays.asList("&r원거리 공격력에 크게 관여합니다", "&r근거리 공격력에 조금 관여합니다", "&r현재 DEX : §e" + GetStat.getStatClass().getLongStat(player, GetStat.Stat.DEX) + "&r (§e" + stat[2]
							+ "&r - §e" + (long) Math.abs(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.DEX)) + "&r)"),
					true, 12, gui);
		}

		else if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.DEX) > 0D)
		{
			this.stack("§b[DEX]", Material.LIGHT_BLUE_STAINED_GLASS_PANE, 3, 1,
					Arrays.asList("&r원거리 공격력에 크게 관여합니다", "&r근거리 공격력에 조금 관여합니다", "&r현재 DEX : §e" + GetStat.getStatClass().getLongStat(player, GetStat.Stat.DEX) + "&r (§e" + stat[2]
							+ "&r + §e" + (long) Math.abs(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.DEX)) + "&r)"),
					true, 12, gui);
		}

		if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.DEX) == 0D)
		{
			this.stack("§b[DEX+]", Material.LIGHT_BLUE_STAINED_GLASS, 3, 1,
					Arrays.asList("§7현재 남은 스탯 포인트 : §6" + stat[0], "§e좌클릭&r으로 DEX를 §e1 &r만큼 증가 시킵니다", "§e우클릭&r으로 DEX를 §e10 &r만큼 증가 시킵니다", "§e시프트 + 좌클릭&r으로 DEX를 §e100 &r만큼 증가 시킵니다",
							"§e시프트 + 우클릭&r으로 현재 가진 스탯 포인트를 전부 다 사용하여 DEX를 증가 시킵니다", "&r현재 DEX : §e" + GetStat.getStatClass().getLongStat(player, GetStat.Stat.DEX)),
					true, 21, gui);
		}

		else if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.DEX) < 0D)
		{
			this.stack("§b[DEX+]", Material.LIGHT_BLUE_STAINED_GLASS, 3, 1,
					Arrays.asList("§7현재 남은 스탯 포인트 : §6" + stat[0], "§e좌클릭&r으로 DEX를 §e1 &r만큼 증가 시킵니다", "§e우클릭&r으로 DEX를 §e10 &r만큼 증가 시킵니다", "§e시프트 + 좌클릭&r으로 DEX를 §e100 &r만큼 증가 시킵니다",
							"§e시프트 + 우클릭&r으로 현재 가진 스탯 포인트를 전부 다 사용하여 DEX를 증가 시킵니다", "&r현재 DEX : §e" + GetStat.getStatClass().getLongStat(player, GetStat.Stat.DEX) + "&r (§e" + stat[2]
									+ "&r - §e" + (long) Math.abs(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.DEX)) + "&r)"),
					true, 21, gui);
		}

		else if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.DEX) > 0D)
		{
			this.stack("§b[DEX+]", Material.LIGHT_BLUE_STAINED_GLASS, 3, 1,
					Arrays.asList("§7현재 남은 스탯 포인트 : §6" + stat[0], "§e좌클릭&r으로 DEX를 §e1 &r만큼 증가 시킵니다", "§e우클릭&r으로 DEX를 §e10 &r만큼 증가 시킵니다", "§e시프트 + 좌클릭&r으로 DEX를 §e100 &r만큼 증가 시킵니다",
							"§e시프트 + 우클릭&r으로 현재 가진 스탯 포인트를 전부 다 사용하여 DEX를 증가 시킵니다", "&r현재 DEX : §e" + GetStat.getStatClass().getLongStat(player, GetStat.Stat.DEX) + "&r (§e" + stat[2]
									+ "&r + §e" + (long) Math.abs(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.DEX)) + "&r)"),
					true, 21, gui);
		}

		if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.INT) == 0D)
		{
			this.stack("§d[INT]", Material.MAGENTA_STAINED_GLASS_PANE, 2, 1,
					Arrays.asList("&r마법 공격력에 크게 관여합니다", "&r현재 INT : §e" + GetStat.getStatClass().getLongStat(player, GetStat.Stat.INT)), true, 13, gui);
		}

		else if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.INT) < 0D)
		{
			this.stack("§d[INT]", Material.MAGENTA_STAINED_GLASS_PANE, 2, 1,
					Arrays.asList("&r마법 공격력에 크게 관여합니다", "&r현재 INT : §e" + GetStat.getStatClass().getLongStat(player, GetStat.Stat.INT) + "&r (§e" + stat[3] + "&r - §e"
							+ (long) Math.abs(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.INT)) + "&r)"),
					true, 13, gui);
		}

		else if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.INT) > 0D)
		{
			this.stack("§d[INT]", Material.MAGENTA_STAINED_GLASS_PANE, 2, 1,
					Arrays.asList("&r마법 공격력에 크게 관여합니다", "&r현재 INT : §e" + GetStat.getStatClass().getLongStat(player, GetStat.Stat.INT) + "&r (§e" + stat[3] + "&r + §e"
							+ (long) Math.abs(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.INT)) + "&r)"),
					true, 13, gui);
		}

		if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.INT) == 0D)
		{
			this.stack("§d[INT+]", Material.MAGENTA_STAINED_GLASS, 2, 1,
					Arrays.asList("§7현재 남은 스탯 포인트 : §6" + stat[0], "§e좌클릭&r으로 INT를 §e1 &r만큼 증가 시킵니다", "§e우클릭&r으로 INT를 §e10 &r만큼 증가 시킵니다", "§e시프트 + 좌클릭&r으로 INT를 §e100 &r만큼 증가 시킵니다",
							"§e시프트 + 우클릭&r으로 현재 가진 스탯 포인트를 전부 다 사용하여 INT를 증가 시킵니다", "&r현재 INT : §e" + GetStat.getStatClass().getLongStat(player, GetStat.Stat.INT)),
					true, 22, gui);
		}

		else if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.INT) < 0D)
		{
			this.stack("§d[INT+]", Material.MAGENTA_STAINED_GLASS, 2, 1,
					Arrays.asList("§7현재 남은 스탯 포인트 : §6" + stat[0], "§e좌클릭&r으로 INT를 §e1 &r만큼 증가 시킵니다", "§e우클릭&r으로 INT를 §e10 &r만큼 증가 시킵니다", "§e시프트 + 좌클릭&r으로 INT를 §e100 &r만큼 증가 시킵니다",
							"§e시프트 + 우클릭&r으로 현재 가진 스탯 포인트를 전부 다 사용하여 INT를 증가 시킵니다", "&r현재 INT : §e" + GetStat.getStatClass().getLongStat(player, GetStat.Stat.INT) + "&r (§e" + stat[3]
									+ "&r - §e" + (long) Math.abs(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.INT)) + "&r)"),
					true, 22, gui);
		}

		else if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.INT) > 0D)
		{
			this.stack("§d[INT+]", Material.MAGENTA_STAINED_GLASS, 2, 1,
					Arrays.asList("§7현재 남은 스탯 포인트 : §6" + stat[0], "§e좌클릭&r으로 INT를 §e1 &r만큼 증가 시킵니다", "§e우클릭&r으로 INT를 §e10 &r만큼 증가 시킵니다", "§e시프트 + 좌클릭&r으로 INT를 §e100 &r만큼 증가 시킵니다",
							"§e시프트 + 우클릭&r으로 현재 가진 스탯 포인트를 전부 다 사용하여 INT를 증가 시킵니다", "&r현재 INT : §e" + GetStat.getStatClass().getLongStat(player, GetStat.Stat.INT) + "&r (§e" + stat[3]
									+ "&r + §e" + (long) Math.abs(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.INT)) + "&r)"),
					true, 22, gui);
		}

		if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.LUK) == 0D)
		{
			this.stack("§a[LUK]", Material.LIME_STAINED_GLASS_PANE, 5, 1,
					Arrays.asList("&r좋은 일이 일어날 확률을 올려줍니다", "&r마법 공격력에 조금 관여합니다", "&r현재 LUK : §e" + GetStat.getStatClass().getLongStat(player, GetStat.Stat.LUK)), true, 14, gui);
		}

		else if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.LUK) < 0D)
		{
			this.stack("§a[LUK]", Material.LIME_STAINED_GLASS_PANE, 5, 1,
					Arrays.asList("&r좋은 일이 일어날 확률을 올려줍니다", "&r마법 공격력에 조금 관여합니다", "&r현재 LUK : §e" + GetStat.getStatClass().getLongStat(player, GetStat.Stat.LUK) + "&r (§e" + stat[4]
							+ "&r - §e" + (long) Math.abs(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.LUK)) + "&r)"),
					true, 14, gui);
		}

		else if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.LUK) > 0D)
		{
			this.stack("§a[LUK]", Material.LIME_STAINED_GLASS_PANE, 5, 1,
					Arrays.asList("&r좋은 일이 일어날 확률을 올려줍니다", "&r마법 공격력에 조금 관여합니다", "&r현재 LUK : §e" + GetStat.getStatClass().getLongStat(player, GetStat.Stat.LUK) + "&r (§e" + stat[4]
							+ "&r + §e" + (long) Math.abs(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.LUK)) + "&r)"),
					true, 14, gui);
		}

		if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.LUK) == 0D)
		{
			this.stack("§a[LUK+]", Material.LIME_STAINED_GLASS, 5, 1,
					Arrays.asList("§7현재 남은 스탯 포인트 : §6" + stat[0], "§e좌클릭&r으로 LUK을 §e1 &r만큼 증가 시킵니다", "§e우클릭&r으로 LUK을 §e10 &r만큼 증가 시킵니다", "§e시프트 + 좌클릭&r으로 LUK을 §e100 &r만큼 증가 시킵니다",
							"§e시프트 + 우클릭&r으로 현재 가진 스탯 포인트를 전부 다 사용하여 LUK을 증가 시킵니다", "&r현재 LUK : §e" + GetStat.getStatClass().getLongStat(player, GetStat.Stat.LUK)),
					true, 23, gui);
		}

		else if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.LUK) < 0D)
		{
			this.stack("§a[LUK+]", Material.LIME_STAINED_GLASS, 5, 1,
					Arrays.asList("§7현재 남은 스탯 포인트 : §6" + stat[0], "§e좌클릭&r으로 LUK을 §e1 &r만큼 증가 시킵니다", "§e우클릭&r으로 LUK을 §e10 &r만큼 증가 시킵니다", "§e시프트 + 좌클릭&r으로 LUK을 §e100 &r만큼 증가 시킵니다",
							"§e시프트 + 우클릭&r으로 현재 가진 스탯 포인트를 전부 다 사용하여 LUK을 증가 시킵니다", "&r현재 LUK : §e" + GetStat.getStatClass().getLongStat(player, GetStat.Stat.LUK) + "&r (§e" + stat[4]
									+ "&r - §e" + (long) Math.abs(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.LUK)) + "&r)"),
					true, 23, gui);
		}

		else if (GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.LUK) > 0D)
		{
			this.stack("§a[LUK+]", Material.LIME_STAINED_GLASS, 5, 1,
					Arrays.asList("§7현재 남은 스탯 포인트 : §6" + stat[0], "§e좌클릭&r으로 LUK을 §e1 &r만큼 증가 시킵니다", "§e우클릭&r으로 LUK을 §e10 &r만큼 증가 시킵니다", "§e시프트 + 좌클릭&r으로 LUK을 §e100 &r만큼 증가 시킵니다",
							"§e시프트 + 우클릭&r으로 현재 가진 스탯 포인트를 전부 다 사용하여 LUK을 증가 시킵니다", "&r현재 LUK : §e" + GetStat.getStatClass().getLongStat(player, GetStat.Stat.LUK) + "&r (§e" + stat[4]
									+ "&r + §e" + (long) Math.abs(GetStat.getStatClass().getDoubleStat(player, GetStat.Stat.LUK)) + "&r)"),
					true, 23, gui);
		}

		this.stack("§6[숙련도]", Material.ORANGE_STAINED_GLASS_PANE, 1, 1,
				Arrays.asList("&r대미지의 편차를 줄여줍니다. §c[최대 99% 적용]", "&r현재 숙련도 : §e" + d1.format(GetStat.getStatClass().getLongStat(player, GetStat.Stat.PROFICIENCY) / 100D) + "§e%"), true,
				15, gui);

		this.stack("§6[숙련도+]", Material.ORANGE_STAINED_GLASS, 1, 1,
				Arrays.asList("§7현재 남은 스탯 포인트 : §6" + stat[0], "§e좌클릭&r으로 숙련도를 §e0.01% &r만큼 증가 시킵니다", "§e우클릭&r으로 숙련도를 §e0.1% &r만큼 증가 시킵니다", "§e시프트 + 좌클릭&r으로 숙련도를 §e1% &r만큼 증가 시킵니다",
						"§e시프트 + 우클릭&r으로 현재 가진 스탯 포인트를 전부 다 사용하여 숙련도를 증가 시킵니다 (스탯 포인트 × 0.01%)", "§c[주의 사항] 숙련도는 최대 §499%§c 까지만 올릴 수 있습니다",
						"&r현재 숙련도 : §e" + d1.format(GetStat.getStatClass().getLongStat(player, GetStat.Stat.PROFICIENCY) / 100D) + "§e%"),
				true, 24, gui);

		player.openInventory(gui);
	}*/
}

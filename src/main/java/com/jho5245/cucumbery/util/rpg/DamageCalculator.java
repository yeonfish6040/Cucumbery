package com.jho5245.cucumbery.util.rpg;

import com.jho5245.cucumbery.Cucumbery;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Random;


public class DamageCalculator
{
	private static final DamageCalculator damageCalculator = new DamageCalculator();
	
	public static DamageCalculator getDamageCalc()
	{
		return damageCalculator;
	}
	
	Random r = new Random();
	
	public double meleeMaxDamage(long STR, long DEX, double PATK)
	{
		FileConfiguration config = Cucumbery.config;
		
		double maxDamage = config.getDouble("max-rpg-stat-damage");
		
		double meleeRatio = config.getDouble("rpg-career-damage.melee");
		
		double dmg = (4 * STR + DEX) * 0.01 * PATK / 100D * meleeRatio;
		
		if (dmg > maxDamage)
			dmg = maxDamage;
		
		return dmg;
	}
	
	public double meleeMinDamage(long STR, long DEX, double PATK, double proficiency)
	{
		FileConfiguration config = Cucumbery.config;
		
		double maxDamage = config.getDouble("max-rpg-stat-damage");
		
		double meleeRatio = config.getDouble("rpg-career-damage.melee");
		
		double dmg = (4 * STR + DEX) * 0.01 * PATK / 100D * proficiency / 100D * meleeRatio;
		
		if (dmg > maxDamage)
			dmg = maxDamage;
		
		return dmg;
	}
	
	public double rangeMaxDamage(long STR, long DEX, double PATK)
	{
		FileConfiguration config = Cucumbery.config;
		
		double maxDamage = config.getDouble("max-rpg-stat-damage");
		
		double rangedRatio = config.getDouble("rpg-career-damage.ranged");
		
		double dmg = (4 * DEX + STR) * 0.01 * PATK / 100D * rangedRatio;
		
		if (dmg > maxDamage)
			dmg = maxDamage;
		
		return dmg;
	}
	
	public double rangeMinDamage(long STR, long DEX, double PATK, double proficiency)
	{
		FileConfiguration config = Cucumbery.config;
		
		double maxDamage = config.getDouble("max-rpg-stat-damage");
		
		double rangedRatio = config.getDouble("rpg-career-damage.ranged");
		
		double dmg = (4 * DEX + STR) * 0.01 * PATK / 100D * proficiency / 100D * rangedRatio;
		
		if (dmg > maxDamage)
			dmg = maxDamage;
		
		return dmg;
	}
	
	public double magicMaxDamage(long INT, long LUK, double MATK)
	{
		FileConfiguration config = Cucumbery.config;
		
		double maxDamage = config.getDouble("max-rpg-stat-damage");
		
		double magicalRatio = config.getDouble("rpg-career-damage.magical");
		
		double dmg = (4 * INT + LUK) * 0.01 * MATK / 100D * magicalRatio;
		
		if (dmg > maxDamage)
			dmg = maxDamage;
		
		return dmg;
	}
	
	public double magicMinDamage(long INT, long LUK, double MATK, double proficiency)
	{
		FileConfiguration config = Cucumbery.config;
		
		double maxDamage = config.getDouble("max-rpg-stat-damage");
		
		double magicalRatio = config.getDouble("rpg-career-damage.magical");
		
		double dmg = (4 * INT + LUK) * 0.01 * MATK / 100D * proficiency / 100D * magicalRatio;
		
		if (dmg > maxDamage)
			dmg = maxDamage;
		
		return dmg;
	}
	
	public int random(int min, int max)
	{
		if (min > max)
			return max;
		
		return r.nextInt(max - min + 1) + min;
	}
	
	public boolean critChance(double chance)
	{
		int i = random(0, 10000) / 100;

		return i <= chance;
	}
	
	public double criticalDamageRatio(int min, int max)
	{
		return 1D + random(random(min, max), max) / 100D;
	}
}
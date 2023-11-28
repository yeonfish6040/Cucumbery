package com.jho5245.cucumbery.util.storage.data.custom_enchant;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.touch.*;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.ultimate.CustomEnchantUltimate;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.ultimate.EnchantCloseCall;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.ultimate.EnchantHighRiskHighReturn;
import io.papermc.paper.enchantments.EnchantmentRarity;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.*;

public abstract class CustomEnchant extends Enchantment
{
	/**
	 * A decorative enchant for glowing items.
	 */
	public static Enchantment GLOW;

	/**
	 * If an {@link ItemStack} has this enchant, it is never dropped upon death.
	 */
	public static Enchantment KEEP_INVENTORY;

	/**
	 * Blocks and mob drops go directly to {@link Player}'s inventory.
	 */
	public static Enchantment TELEKINESIS;

	/**
	 * If keepInv is false, player kill drops go directly to {@link Player}'s inventory.
	 */
	public static Enchantment TELEKINESIS_PVP;

	/**
	 * By predefined chance table, mining {@link Material#ICE} will drop certain ice block.
	 */
	public static Enchantment COLD_TOUCH;

	/**
	 * Removes target's {@link LivingEntity#getNoDamageTicks()} (set to zero).
	 */
	public static Enchantment JUSTIFICATION;

	/**
	 * Removes target's {@link LivingEntity#getNoDamageTicks()} (set to zero). Only applicable for {@link Material#BOW}
	 */
	public static Enchantment JUSTIFICATION_BOW;

	/**
	 * Blocks and mob drops become its smelted form.
	 */
	public static Enchantment SMELTING_TOUCH;

	/**
	 * By predefined chance table, mining {@link Material#CACTUS} will drop certain ice block.
	 */
	public static Enchantment WARM_TOUCH;

	/**
	 * Blocks and mob drops will disappear.
	 */
	public static Enchantment COARSE_TOUCH;

	/**
	 * Blocks drops will become {@link Material#FLINT} in chance.
	 */
	public static Enchantment DULL_TOUCH;

	/**
	 * Blocks and mob drops will not grant any xp.
	 */
	public static Enchantment UNSKILLED_TOUCH;

	/**
	 * Blocks that is {@link BlockInventoryHolder#getInventory()}'s item will be vanished.
	 */
	public static Enchantment VANISHING_TOUCH;

	/**
	 * ANY Blocks drops will be doubled
	 */
	public static Enchantment FRANTIC_FORTUNE;

	/**
	 * Fishing root items will be DOUBLED
	 */
	public static Enchantment FRANTIC_LUCK_OF_THE_SEA;

	/**
	 * If kill an entity with any item with this Enchant, the killer on death message will be hidden.
	 */
	public static Enchantment ASSASSINATION;

	/**
	 * If kill an entity with any item with this Enchant, the killer on death message will be hidden. Only applicable for {@link Material#BOW}
	 */
	public static Enchantment ASSASSINATION_BOW;

	/**
	 * If a throwable weapon/projectile has this, It's accuracy will be dropped.
	 */
	public static Enchantment IDIOT_SHOOTER;

	/**
	 * 1+(1*레벨) 추가대미지, 1레벨 오를때마다 상대의 방패 피격시 상대의 방패사용불가 0.5초씩 추가
	 */
	public static Enchantment CLEAVING;

	/**
	 * 플레이어의 손에 방패가 있을 때 다른 몹이나 플레이어가 피격 시 막고있지 않을 때 (6*레벨)% 확률로 막아지고(해당 공격에 대해 무적) 방패의 내구도가 ((들어온 대미지/6)*레벨) 만큼 깎인다. (1 이하의 데미지는 내구도 깎지 않음, 소수점은 정수로 반올림) (특징:
	 * 레벨이 높아질수록 막을 확률은 높아지지만 이 효과로 막았을때 내구도가 더 많이 깎임) (내구성 인챈트와 같이 인챈트 불가)
	 */
	public static Enchantment DEFENSE_CHANCE;

	/**
	 * 괭이의 농업 행운 증가 - 작물 드롭율 증가
	 */
	public static Enchantment HARVESTING;

	/**
	 * 도끼의 농업 행운 증가 - 작물 드롭율 증가
	 */
	public static Enchantment SUNDER;

	/**
	 * 도끼, 괭이가 자라지 않은 작물과 수박, 호박의 줄기를 부수지 않도록 함
	 */
	public static Enchantment DELICATE;

	/**
	 * 신발을 신고 경작지 위에서 점프해도 경작지가 파괴되지 않음
	 */
	public static Enchantment FARMERS_GRACE;

	// Ulitmate Enchants
	public static Enchantment HIGH_RISK_HIGH_RETURN;

	/**
	 * 구사 일생
	 * <p>Grants chances not to die upon death.
	 */
	public static Enchantment CLOSE_CALL;

	private static final Map<NamespacedKey, Enchantment> byKey = new HashMap<>();

	private static final Map<String, Enchantment> byName = new HashMap<>();

	public static void registerEnchants()
	{
		GLOW = registerEnchant(new EnchantGlow("glow"));

		KEEP_INVENTORY = registerEnchant(new EnchantKeepInventory("keep_inventory"));
		TELEKINESIS = registerEnchant(new EnchantTelekinesis("telekinesis"));
		TELEKINESIS_PVP = registerEnchant(new EnchantTelekinesisPVP("telekinesis_pvp"));

		COLD_TOUCH = registerEnchant(new EnchantColdTouch("cold_touch"));
		JUSTIFICATION = registerEnchant(new EnchantJustification("justification"));
		JUSTIFICATION_BOW = registerEnchant(new EnchantJustificationBow("justification_bow"));
		SMELTING_TOUCH = registerEnchant(new EnchantSmeltingTouch("smelting_touch"));
		WARM_TOUCH = registerEnchant(new EnchantWarmTouch("warm_touch"));

		COARSE_TOUCH = registerEnchant(new EnchantCoarseTouch("coarse_touch"));
		DULL_TOUCH = registerEnchant(new EnchantDullTouch("dull_touch"));
		UNSKILLED_TOUCH = registerEnchant(new EnchantUnskilledTouch("unskilled_touch"));
		VANISHING_TOUCH = registerEnchant(new EnchantVanishingTouch("vanishing_touch"));

		FRANTIC_FORTUNE = registerEnchant(new EnchantFranticFortune("frantic_fortune"));
		FRANTIC_LUCK_OF_THE_SEA = registerEnchant(new EnchantFranticLuckOfTheSea("frantic_luck_of_the_sea"));

		ASSASSINATION = registerEnchant(new EnchantAssassination("assassination"));
		ASSASSINATION_BOW = registerEnchant(new EnchantAssassinationBow("assassination_bow"));

		IDIOT_SHOOTER = registerEnchant(new EnchantIdiotShooter("idiot_shooter"));

		CLEAVING = registerEnchant(new EnchantCleaving("cleaving"));

		DEFENSE_CHANCE = registerEnchant(new EnchantDefenseChance("defense_chance"));

		HARVESTING = registerEnchant(new EnchantHarvesting("harvesting"));

		SUNDER = registerEnchant(new EnchantSunder("sunder"));

		DELICATE = registerEnchant(new EnchantDelicate("delicate"));

		FARMERS_GRACE = registerEnchant(new EnchantFarmersGrace("farmers_grace"));

		// Ulitmate Enchants

		HIGH_RISK_HIGH_RETURN = registerEnchant(new EnchantHighRiskHighReturn("high_risk_high_return"));

		CLOSE_CALL = registerEnchant(new EnchantCloseCall("close_call"));
	}

	private static CustomEnchant registerEnchant(@NotNull CustomEnchant enchant)
	{
		Enchantment.registerEnchantment(enchant);
		byKey.put(enchant.getKey(), enchant);
		byName.put(enchant.getName(), enchant);
		return enchant;
	}

	public static boolean isEnabled()
	{
		return GLOW != null;
	}

	public CustomEnchant(@NotNull NamespacedKey namespacedKey)
	{
		super(namespacedKey);
	}

	public CustomEnchant(@NotNull String name)
	{
		super(Objects.requireNonNull(NamespacedKey.fromString(name, Cucumbery.getPlugin())));
	}

	@Override
	public int getMaxLevel()
	{
		return 1;
	}

	@Override
	public int getStartLevel()
	{
		return 1;
	}

	@Override
	@NotNull
	@SuppressWarnings("deprecation")
	public EnchantmentTarget getItemTarget()
	{
		return EnchantmentTarget.ALL;
	}

	@Override
	public boolean canEnchantItem(@NotNull ItemStack itemStack)
	{
		return true;
	}

	@Override
	@NotNull
	public final String getName()
	{
		return translationKey();
	}

	@Override
	public boolean isTreasure()
	{
		return false;
	}

	@Override
	public boolean isCursed()
	{
		return false;
	}

	@Override
	public boolean conflictsWith(@NotNull Enchantment other)
	{
		return false;
	}

	@Override
	@NotNull
	public Component displayName(int level)
	{
		return ComponentUtil.translate("%s %s", ComponentUtil.translate(translationKey()), level);
	}

	@Override
	@NotNull
	public abstract String translationKey();

	@NotNull
	public String translationKeyFallback()
	{
		String key = translationKey();
		if (key.startsWith("key:"))
			return key.split("\\|")[1];
		return key;
	}

	@Override
	public boolean isTradeable()
	{
		return false;
	}

	@Override
	public boolean isDiscoverable()
	{
		return true;
	}

	@Override
	@NotNull
	public EnchantmentRarity getRarity()
	{
		return EnchantmentRarity.COMMON;
	}

	@Override
	public float getDamageIncrease(int level, @NotNull EntityCategory entityCategory)
	{
		return 0f;
	}

	@Override
	@NotNull
	public Set<EquipmentSlot> getActiveSlots()
	{
		return new HashSet<>(Arrays.asList(EquipmentSlot.values()));
	}

	/**
	 * @return true if this custom enchant is ultimate enchant.
	 */
	public boolean isUltimate()
	{
		return this instanceof CustomEnchantUltimate;
	}

	public int enchantCost()
	{
		return 5;
	}

  public int getMinModifiedCost(int i)
  {
    return 0;
  }

  public int getMaxModifiedCost(int i)
  {
    return 0;
  }

	public static void onEnable()
	{
		try
		{
			Field f = Enchantment.class.getDeclaredField("acceptingNew");
			f.setAccessible(true);
			f.set(null, true);
		}
		catch (Exception e)
		{
			Cucumbery.getPlugin().getLogger().warning(e.getMessage());
		}
		registerEnchants();
	}

	@SuppressWarnings("unchecked")
	public static void onDisable()
	{
		try
		{
			Field byKeyField = Enchantment.class.getDeclaredField("byKey");
			Field byNameField = Enchantment.class.getDeclaredField("byName");

			byKeyField.setAccessible(true);
			byNameField.setAccessible(true);

			HashMap<NamespacedKey, Enchantment> byKey = (HashMap<NamespacedKey, Enchantment>) byKeyField.get(null);
			HashMap<String, Enchantment> byName = (HashMap<String, Enchantment>) byNameField.get(null);

			byKey.keySet().removeIf(CustomEnchant.byKey::containsKey);
			byName.keySet().removeIf(CustomEnchant.byName::containsKey);
		}
		catch (Exception e)
		{
			Cucumbery.getPlugin().getLogger().warning(e.getMessage());
		}
	}
}

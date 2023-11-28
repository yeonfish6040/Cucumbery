package com.jho5245.cucumbery.util.storage.data.custom_potion_effect;

import com.jho5245.cucumbery.Cucumbery;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffectTypeWrapper;
import org.jetbrains.annotations.NotNull;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public abstract class CustomPotionEffectType extends PotionEffectTypeWrapper implements Translatable
{
	public static PotionEffectType NOTHING;

	private static final PotionEffectType[] effects = new PotionEffectType[1];

	private static final Map<Integer, PotionEffectType> byId = new HashMap<>();

	private static final Map<String, PotionEffectType> byName = new HashMap<>();

	private static final Map<NamespacedKey, PotionEffectType> byKey = new HashMap<>();

	protected CustomPotionEffectType(int id, @NotNull String name)
	{
		super(id, name);
	}

	private static void registerPotionEffects()
	{
		NOTHING = registerPotionEffect(new PotionEffectTypeNothing(33, "nothing"));
	}

	private static PotionEffectType registerPotionEffect(@NotNull CustomPotionEffectType potionEffect)
	{
		try
		{
			Field f = PotionEffectType.class.getDeclaredField("byId");
			f.setAccessible(true);
		}
		catch (Exception e)
		{
			Cucumbery.getPlugin().getLogger().warning(e.getMessage());
		}
		PotionEffectType.registerPotionEffectType(potionEffect);
		byId.put(potionEffect.getId(), potionEffect);
		byName.put(potionEffect.getName(), potionEffect);
		byKey.put(potionEffect.getKey(), potionEffect);
		return potionEffect;
	}

	@Override
	@Deprecated
	public double getDurationModifier()
	{
		return 0d;
	}

	@Override
	@NotNull
	public NamespacedKey getKey()
	{
		return NamespacedKey.minecraft(this.getName());
	}

	@NotNull
	public abstract String translationKey();

	@Override
	@NotNull
	public abstract String getName();

	@Override
	public boolean isInstant()
	{
		return false;
	}

	@Override
	@NotNull
	public PotionEffectType getType()
	{
		return byId.get(this.getId());
	}

	@Override
	@NotNull
	public Color getColor()
	{
		return Color.fromRGB(0, 0, 0);
	}

	public static void onEnable()
	{
		if (!Cucumbery.config.getBoolean("use-custom-potion-effect-features"))
		{
			return;
		}
		try
		{
			Field acceptingNew = Enchantment.class.getDeclaredField("acceptingNew");
			acceptingNew.setAccessible(true);
			acceptingNew.set(null, true);
			Field field = PotionEffectType.class.getDeclaredField("byId");
			field.setAccessible(true);

			final Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
			unsafeField.setAccessible(true);
			final Unsafe unsafe = (Unsafe) unsafeField.get(null);

			/*
			 * Check if the array byId isn't resized
			 */
			if (((PotionEffectType[]) field.get(null)).length == 33)
			{
				/*
				 * If he isn't, add all custom effect (that we created), in our List
				 */
				for (int i : byId.keySet())
				{
					effects[i] = byId.get(i);
				}

				/*
				 * Calculate the new array size and create new array with the new array size
				 */
				int arraySize = 33 + effects.length;

				PotionEffectType[] customEffectTypes = new PotionEffectType[arraySize];

				/*
				 * Add all normal effects to our new array
				 */
				PotionEffectType[] effectTypes = (PotionEffectType[]) field.get(null);
				System.arraycopy(effectTypes, 0, customEffectTypes, 0, 33);

				/*
				 * redefine the variable byId with our new array
				 */
				final Field ourField = PotionEffectType.class.getDeclaredField("byId");
				final Object staticFieldBase = unsafe.staticFieldBase(ourField);
				final long staticFieldOffset = unsafe.staticFieldOffset(ourField);
				unsafe.putObject(staticFieldBase, staticFieldOffset, customEffectTypes);


				/*
				 * Check if the variable byId is resized
				 */
				System.out.println("byId size : " + ((PotionEffectType[]) field.get(null)).length);

				/*
				 * Accessing to the boolean and set him to true
				 */
				field = PotionEffectType.class.getDeclaredField("acceptingNew");
				field.setAccessible(true);

				field.set(null, true);

				/*
				 * Register our custom effects
				 */
				for (PotionEffectType effectType : effects)
				{
					PotionEffectType.registerPotionEffectType(effectType);

					//noinspection deprecation
					System.out.println(effectType + "(" + effectType.getId() + ") registered");
				}
			}
			registerPotionEffects();
		}
		catch (Exception e)
		{
			Cucumbery.getPlugin().getLogger().warning(e.getMessage());
		}
	}

	public static void onDisable()
	{
		try
		{
			Field byKeyField = PotionEffectType.class.getDeclaredField("byKey");
			Field byNameField = PotionEffectType.class.getDeclaredField("byName");

			byKeyField.setAccessible(true);
			byNameField.setAccessible(true);

			@SuppressWarnings("unchecked") HashMap<NamespacedKey, PotionEffectType> byKey = (HashMap<NamespacedKey, PotionEffectType>) byKeyField.get(null);
			@SuppressWarnings("unchecked") HashMap<String, PotionEffectType> byName = (HashMap<String, PotionEffectType>) byNameField.get(null);

			byKey.keySet().removeIf(CustomPotionEffectType.byKey::containsKey);
			byName.keySet().removeIf(CustomPotionEffectType.byName::containsKey);

			Field field = PotionEffectType.class.getDeclaredField("byId");
			field.setAccessible(true);

			final Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
			unsafeField.setAccessible(true);
			final Unsafe unsafe = (Unsafe) unsafeField.get(null);

			PotionEffectType[] effectTypes = new PotionEffectType[33];
			System.arraycopy((PotionEffectType[]) field.get(null), 0, effectTypes, 0, effectTypes.length);

			/*
			 * redefine the variable byId with our new array
			 */
			final Field ourField = PotionEffectType.class.getDeclaredField("byId");
			final Object staticFieldBase = unsafe.staticFieldBase(ourField);
			final long staticFieldOffset = unsafe.staticFieldOffset(ourField);
			unsafe.putObject(staticFieldBase, staticFieldOffset, effectTypes);
		}
		catch (Exception e)
		{
			Cucumbery.getPlugin().getLogger().warning(e.getMessage());
		}
	}
}

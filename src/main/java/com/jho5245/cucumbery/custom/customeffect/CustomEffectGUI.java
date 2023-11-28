package com.jho5245.cucumbery.custom.customeffect;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect.DisplayType;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.util.gui.GUIManager;
import com.jho5245.cucumbery.util.no_groups.ColorUtil;
import com.jho5245.cucumbery.util.no_groups.ColorUtil.Type;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil.TimeFormatType;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.TranslatableKeyParser;
import com.jho5245.cucumbery.util.storage.no_groups.CreateItemStack;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextDecoration.State;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CustomEffectGUI
{
	public static void openGUI(@NotNull Player player, boolean firstOpen)
	{
		Inventory menu = GUIManager.create(6, ComponentUtil.translate("&8적용 중인 효과 목록"), Constant.POTION_EFFECTS);
		if (firstOpen)
		{
			// deco template
			ItemStack deco = CreateItemStack.create(Material.WHITE_STAINED_GLASS_PANE, 1, Component.text("§와"), false);
			menu.setItem(0, deco);
			menu.setItem(1, deco);
			menu.setItem(2, deco);
			menu.setItem(3, deco);
			menu.setItem(5, deco);
			menu.setItem(6, deco);
			menu.setItem(7, deco);
			menu.setItem(8, deco);
			menu.setItem(9, deco);
			menu.setItem(17, deco);
			menu.setItem(18, deco);
			menu.setItem(26, deco);
			menu.setItem(27, deco);
			menu.setItem(35, deco);
			menu.setItem(36, deco);
			menu.setItem(44, deco);
			menu.setItem(45, deco);
			menu.setItem(46, deco);
			menu.setItem(47, deco);
			menu.setItem(48, deco);
			menu.setItem(49, deco);
			menu.setItem(50, deco);
			menu.setItem(51, deco);
			menu.setItem(52, deco);
			menu.setItem(53, deco);
			menu.setItem(4, CreateItemStack.create(Material.CLOCK, 1, ComponentUtil.translate("rg255,204;로딩중..."), false));
			player.openInventory(menu);
		}
		else
		{
			menu = player.getOpenInventory().getTopInventory();
			for (int i = 10; i <= 16; i++)
			{
				menu.setItem(i, null);
			}
			for (int i = 19; i <= 25; i++)
			{
				menu.setItem(i, null);
			}
			for (int i = 28; i <= 34; i++)
			{
				menu.setItem(i, null);
			}
			for (int i = 37; i <= 43; i++)
			{
				menu.setItem(i, null);
			}
		}
		List<CustomEffect> customEffects = new ArrayList<>(CustomEffectManager.getEffects(player));
		customEffects.removeIf(effect -> effect.getDisplayType() == DisplayType.NONE || effect.getDisplayType() == DisplayType.BOSS_BAR_ONLY || effect.isHidden());
		List<PotionEffect> potionEffects = CustomEffectManager.removeDisplay(player, player.getActivePotionEffects());
		boolean isEmpty = customEffects.isEmpty() && potionEffects.isEmpty();
		if (isEmpty)
		{
			menu.setItem(22, CreateItemStack.create(Material.RED_STAINED_GLASS_PANE, 1, ComponentUtil.translate("&c효과 없음!"),
					Arrays.asList(ComponentUtil.translate("&7적용 중인 효과가 하나도 없습니다"), ComponentUtil.translate("&7포션을 마셔서 효과를 적용시켜 보세요!")), false));
		}
		else
		{
			for (int i = 0; i < customEffects.size(); i++)
			{
				CustomEffect customEffect = customEffects.get(i);
				if (menu.firstEmpty() == -1)
				{
					continue;
				}
				CustomEffectType effectType = customEffect.getType();
				String key = effectType.translationKey();
				ItemStack itemStack = customEffect.getIcon();
				if (itemStack == null)
				{
					itemStack = new ItemStack(Material.POTION);
				}
				itemStack.setAmount(Math.min(64, customEffect.getAmplifier() + 1));
				ItemMeta itemMeta = itemStack.getItemMeta();
				if (customEffect.getIcon() == null && itemStack.getType() == Material.POTION)
				{
					ColorUtil colorUtil = new ColorUtil(Type.HSL, "" + ((i * 30) % 255) + ",100,50;");
					PotionMeta potionMeta = (PotionMeta) itemMeta;
					potionMeta.setColor(Color.fromRGB(colorUtil.getRed(), colorUtil.getGreen(), colorUtil.getBlue()));
					itemStack.setItemMeta(potionMeta);
				}
				itemMeta = itemStack.getItemMeta();
				itemMeta.addItemFlags(ItemFlag.values());
				itemMeta.displayName(ComponentUtil.translate(key).color(effectType.isNegative() ? NamedTextColor.RED : NamedTextColor.GREEN)
						.decoration(TextDecoration.ITALIC, State.FALSE));
				itemMeta.lore(customEffectLore(player, customEffect));
				if (!itemMeta.hasCustomModelData())
				{
					itemMeta.setCustomModelData(effectType.getId());
				}
				if (effectType.getCustomModelData() != -1)
				{
					itemMeta.setCustomModelData(effectType.getCustomModelData());
				}
				if (effectType == CustomEffectType.FLY || effectType == CustomEffectType.FLY_REMOVE_ON_QUIT)
				{
					if (CustomEffectManager.hasEffect(player, CustomEffectType.FLY_NOT_ENABLED))
					{
						itemMeta.setCustomModelData(5202);
					}
				}
				itemStack.setItemMeta(itemMeta);
				if (effectType.isRemoveable())
				{
					NBTItem nbtItem = new NBTItem(itemStack, true);
					nbtItem.setString("removeEffect", "custom:" + effectType.getNamespacedKey());
					nbtItem.setInteger("removeEffectAmplifier", customEffect.getAmplifier());
				}
				menu.addItem(itemStack);
			}
			for (PotionEffect potionEffect : potionEffects)
			{
				if (menu.firstEmpty() == -1)
				{
					continue;
				}
				PotionEffectType effectType = potionEffect.getType();
				ItemStack itemStack = new ItemStack(Material.POTION, Math.min(64, potionEffect.getAmplifier() + 1));
				PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
				potionMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS, ItemFlag.HIDE_ENCHANTS);
				String effectKey = TranslatableKeyParser.getKey(effectType);
				@SuppressWarnings("deprecation") int id = potionEffect.getType().getId() + 15200;
				potionMeta.setCustomModelData(id);
				potionMeta.displayName(ComponentUtil.translate((CustomEffectManager.isVanillaNegative(effectType) ? "&c" : "&a") + effectKey));
				potionMeta.setColor(effectType.getColor());
				potionMeta.lore(potionEffectLore(player, potionEffect));
				itemStack.setItemMeta(potionMeta);
				if (!CustomEffectManager.isVanillaNegative(effectType))
				{
					NBTItem nbtItem = new NBTItem(itemStack, true);
					nbtItem.setString("removeEffect", "potion:" + effectType.getName());
				}
				menu.addItem(itemStack);
			}
		}
		int size = potionEffects.size() + customEffects.size();
		ItemStack info = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta skullMeta = (SkullMeta) info.getItemMeta();
		skullMeta.setOwningPlayer(player);
		skullMeta.displayName(ComponentUtil.translate("&o&q[%s의 효과 정보]", player));
		skullMeta.lore(List.of(ComponentUtil.translate("&7효과 개수 : %s개", size)));
		info.setItemMeta(skullMeta);
		menu.setItem(4, info);
		UUID uuid = player.getUniqueId();
		InventoryView inventory = GUIManager.getLastInventory(uuid);
		if (inventory != null)
		{
			menu.setItem(45, CreateItemStack.getPreviousButton(inventory.title()));
		}
	}

	@SuppressWarnings("all")
	@NotNull
	private static List<Component> customEffectLore(@NotNull Player player, @NotNull CustomEffect customEffect)
	{
		List<Component> lore = new ArrayList<>();
		CustomEffectType effectType = customEffect.getType();
		int duration = customEffect.getDuration();
		int amplifier = customEffect.getAmplifier();
		Component description = customEffect.getDescription(player);
		boolean isFinite = duration != -1, isAmplifiable = effectType.getMaxAmplifier() > 0, isTimeHidden =
				effectType.isTimeHidden() || (effectType.isTimeHiddenWhenFull() && duration == customEffect.getInitDuration());
		if (!description.equals(Component.empty()))
		{
			try
			{
				List<Component> children = new ArrayList<>(Collections.singletonList(description.children(Collections.emptyList())));
				children.addAll(description.children());
				for (int i = 0; i < children.size(); i++)
				{
					Component child = children.get(i);
					if (child.equals(Component.text("\n")) && i + 1 != children.size() && children.get(i + 1).equals(Component.text("\n")))
					{
						lore.add(Component.empty());
					}
					if (!child.equals(Component.text("\n")))
					{
						if (child.color() == null)
						{
							child = child.color(NamedTextColor.WHITE);
						}
						if (child.decoration(TextDecoration.ITALIC) == State.NOT_SET)
						{
							child = child.decoration(TextDecoration.ITALIC, State.FALSE);
						}
						lore.add(child);
					}
				}
			}
			catch (Exception e)
			{
				Cucumbery.getPlugin().getLogger().warning(e.getMessage());
			}
			if ((isFinite && !isTimeHidden) || isAmplifiable)
			{
				lore.add(Component.empty());
				if (effectType == CustomEffectType.CURSE_OF_BEANS)
				{
					lore.add(Component.empty());
				}
			}
		}
		if (isFinite && !isTimeHidden)
		{
			Component time = ComponentUtil.timeFormat(duration * 50L, duration < 200 ? TimeFormatType.FULL_SINGLE_DECIMAL : TimeFormatType.FULL_NO_DECIMALS)
					.color(Constant.THE_COLOR);
			lore.add(ComponentUtil.translate("&f지속 시간 : %s", time));
			if (effectType == CustomEffectType.CURSE_OF_BEANS)
			{
				lore.add(ComponentUtil.translate("&f지속 시간 : %s", time));
			}
		}
		if (isAmplifiable)
		{
			lore.add(ComponentUtil.translate("&f농도 레벨 : %s단계", amplifier + 1));
		}
		if (effectType.isRemoveable())
		{
			lore.add(Component.empty());
			if (CustomEffectManager.hasEffect(player, CustomEffectType.NO_BUFF_REMOVE))
			{
				lore.add(ComponentUtil.translate("rg255,204;&m우클릭하여 효과 제거"));
				lore.add(ComponentUtil.translate("&c%s - 효과를 제거할 수 없는 상태입니다", CustomEffectType.NO_BUFF_REMOVE));
			}
			else
			{
				lore.add(ComponentUtil.translate("rg255,204;우클릭하여 효과 제거"));
			}
		}
		if (effectType == CustomEffectType.CONTINUAL_SPECTATING && player.getGameMode() == GameMode.SPECTATOR && player.getSpectatorTarget() instanceof Player)
		{
			lore.add(Component.empty());
			lore.add(ComponentUtil.translate("rg255,204;좌클릭하여 30초간 관전 외출"));
		}
		if (effectType == CustomEffectType.CONTINUAL_RIDING && player.getVehicle() != null && player.getVehicle().isValid() && !player.getVehicle().isDead())
		{
			lore.add(Component.empty());
			lore.add(ComponentUtil.translate("rg255,204;좌클릭하여 30초간 탑승 외출"));
		}
		if (effectType == CustomEffectType.POSITION_MEMORIZE)
		{
			lore.add(Component.empty());
			lore.add(ComponentUtil.translate("rg255,204;좌클릭하여 즉시 저장된 위치로 이동"));
		}
		return lore;
	}

	@NotNull
	@SuppressWarnings("all")
	private static List<Component> potionEffectLore(@NotNull Player player, @NotNull PotionEffect potionEffect)
	{
		List<Component> lore = new ArrayList<>();

		PotionEffectType potionEffectType = potionEffect.getType();
		String effectKey = TranslatableKeyParser.getKey(potionEffectType);
		String id = effectKey.substring(17);
		Component description = VanillaEffectDescription.getDescription(potionEffect, player);
		if (!description.equals(Component.empty()))
		{
			try
			{
				List<Component> children = new ArrayList<>(Collections.singletonList(description.children(Collections.emptyList())));
				children.addAll(description.children());
				for (int i = 0; i < children.size(); i++)
				{
					Component child = children.get(i);
					if (child.equals(Component.text("\n")) && i + 1 != children.size() && children.get(i + 1).equals(Component.text("\n")))
					{
						lore.add(Component.empty());
					}
					if (!child.equals(Component.text("\n")))
					{
						if (child.color() == null)
						{
							child = child.color(NamedTextColor.WHITE);
						}
						if (child.decoration(TextDecoration.ITALIC) == State.NOT_SET)
						{
							child = child.decoration(TextDecoration.ITALIC, State.FALSE);
						}
						lore.add(child);
					}
				}
			}
			catch (Exception e)
			{
				Cucumbery.getPlugin().getLogger().warning(e.getMessage());
			}
		}
		int duration = potionEffect.getDuration(), amplifier = potionEffect.getAmplifier();
		lore.add(Component.empty());
		if (duration <= 20 * 60 * 60 * 24 * 365)
		{
			Component time = ComponentUtil.timeFormat(duration * 50L, duration < 200 ? TimeFormatType.FULL_SINGLE_DECIMAL : TimeFormatType.FULL_NO_DECIMALS)
					.color(Constant.THE_COLOR);
			lore.add(ComponentUtil.translate("&f지속 시간 : %s", time));
		}
		lore.add(ComponentUtil.translate("&f농도 레벨 : %s단계", amplifier + 1));
		if (!CustomEffectManager.isVanillaNegative(potionEffect.getType()))
		{
			lore.add(Component.empty());
			if (CustomEffectManager.hasEffect(player, CustomEffectType.NO_BUFF_REMOVE))
			{
				lore.add(ComponentUtil.translate("rg255,204;&m우클릭하여 효과 제거"));
				lore.add(ComponentUtil.translate("&c%s - 효과를 제거할 수 없는 상태입니다", CustomEffectType.NO_BUFF_REMOVE));
			}
			else
			{
				lore.add(ComponentUtil.translate("rg255,204;우클릭하여 효과 제거"));
			}
		}
		//    PotionEffectType potionEffectType = potionEffect.getType();
		//    String effectKey = TranslatableKeyParser.getKey(potionEffectType);
		//    String id = effectKey.substring(17);
		//    int duration = potionEffect.getDuration(), amplifier = potionEffect.getAmplifier();
		//    boolean hasParticles = potionEffect.hasParticles(), hasIcon = potionEffect.hasIcon(), isAmbient = potionEffect.isAmbient();
		//    lore.add(ComponentUtil.translate("지속 시간 : %s", Constant.THE_COLOR_HEX + Method.timeFormatMilli(duration * 50L)));
		//    lore.add(ComponentUtil.translate("농도 레벨 : %s단계", amplifier + 1));
		//    if (!hasParticles)
		//    {
		//      lore.add(ComponentUtil.translate("&a입자 숨김"));
		//    }
		//    if (!hasIcon)
		//    {
		//      lore.add(ComponentUtil.translate("&a우측 상단 아이콘 숨김"));
		//    }
		//    if (isAmbient)
		//    {
		//      lore.add(ComponentUtil.translate("&a우측 상단 효과 빛남"));
		//    }
		//    lore.add(Component.text("minecraft:" + id, NamedTextColor.DARK_GRAY));
		return lore;
	}
}

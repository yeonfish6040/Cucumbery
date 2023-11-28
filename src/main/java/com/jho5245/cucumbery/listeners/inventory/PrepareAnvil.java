package com.jho5245.cucumbery.listeners.inventory;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.itemlore.ItemLoreView;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant;
import com.jho5245.cucumbery.util.storage.no_groups.CreateItemStack;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PrepareAnvil implements Listener
{
	@EventHandler
	public void onPrepareAnvil(PrepareAnvilEvent event)
	{
		HumanEntity humanEntity = event.getView().getPlayer();
		if (!(humanEntity instanceof Player player))
		{
			return;
		}
		AnvilInventory anvilInventory = event.getInventory();
		ItemStack firstItem = anvilInventory.getFirstItem(), secondItem = anvilInventory.getSecondItem(), resultItem = anvilInventory.getResult();
		boolean firstItemExists = ItemStackUtil.itemExists(firstItem), secondItemExists = ItemStackUtil.itemExists(
				secondItem), resultItemExists = ItemStackUtil.itemExists(resultItem);
		@Nullable Component firstItemName, secondItemName, resultItemName;
		if (firstItemExists)
		{
			firstItemName = firstItem.displayName();
		}
		else
		{
			firstItemName = null;
		}
		if (secondItemExists)
		{
			secondItemName = secondItem.displayName();
		}
		else
		{
			secondItemName = null;
		}
		if (resultItemExists)
		{
			resultItemName = resultItem.displayName();
		}
		else
		{
			resultItemName = null;
		}
		ItemStack restrictedItem = CreateItemStack.create(Material.BARRIER, 1, Constant.NO_ANVIL_ITEM_DISPLAYNAME, true);
		boolean restricted = false;
		ItemMeta itemMeta = restrictedItem.getItemMeta();
		List<Component> lore = new ArrayList<>();
		boolean firstItemNoAnvil = NBTAPI.isRestricted(player, firstItem, RestrictionType.NO_ANVIL), secondItemNoAnvil = NBTAPI.isRestricted(player, secondItem,
				RestrictionType.NO_ANVIL);
		if (firstItemNoAnvil || secondItemNoAnvil)
		{
			lore.add(ComponentUtil.translate("&c모루에서 사용할 수 없는 아이템이 존재합니다"));
		}
		if (firstItemExists && firstItemNoAnvil)
		{
			restricted = true;
			lore.add(ComponentUtil.translate("rg255,204;왼쪽에 있는 아이템(%s)", firstItem));
		}
		if (secondItemExists && secondItemNoAnvil)
		{
			restricted = true;
			lore.add(ComponentUtil.translate("rg255,204;오른쪽에 있는 아이템(%s)", secondItem));
		}
		boolean firstItemNoUseEnchantedBookEnchant = NBTAPI.isRestricted(player, firstItem,
				RestrictionType.NO_ANVIL_ENCHANTED_BOOK_ENCHANT), secondItemNoUseEnchantedBookEnchant = NBTAPI.isRestricted(player, secondItem,
				RestrictionType.NO_ANVIL_ENCHANTED_BOOK_ENCHANT);
		if ((firstItemNoUseEnchantedBookEnchant || secondItemNoUseEnchantedBookEnchant) && ItemStackUtil.itemExists(firstItem)
				&& firstItem.getType() != Material.ENCHANTED_BOOK && ItemStackUtil.itemExists(secondItem) && secondItem.getType() == Material.ENCHANTED_BOOK)
		{
			restricted = true;
			lore.add(ComponentUtil.translate("&c모루에서 마법이 부여된 책으로 마법을 부여할 수 없는 아이템이 존재합니다"));
			if (firstItemExists && firstItemNoUseEnchantedBookEnchant)
			{
				lore.add(ComponentUtil.translate("rg255,204;왼쪽에 있는 아이템(%s)", firstItem));
			}
			if (secondItemExists && secondItemNoUseEnchantedBookEnchant)
			{
				lore.add(ComponentUtil.translate("rg255,204;오른쪽에 있는 아이템(%s)", secondItem));
			}
		}

		boolean firstItemNoComposition = NBTAPI.isRestricted(player, firstItem,
				RestrictionType.NO_ANVIL_COMPOSITION), secondItemNoComposition = NBTAPI.isRestricted(player, secondItem, RestrictionType.NO_ANVIL_COMPOSITION);
		if ((firstItemNoComposition || secondItemNoComposition) && ItemStackUtil.itemExists(firstItem) && ItemStackUtil.itemExists(secondItem)
				&& firstItem.getType() != Material.ENCHANTED_BOOK && firstItem.getType() == secondItem.getType())
		{
			restricted = true;
			lore.add(ComponentUtil.translate("&c모루에서 같은 종류의 아이템끼리 합성할 수 없는 아이템이 존재합니다"));
			if (firstItemExists && firstItemNoComposition)
			{
				lore.add(ComponentUtil.translate("rg255,204;왼쪽에 있는 아이템(%s)", firstItem));
			}
			if (secondItemExists && secondItemNoComposition)
			{
				lore.add(ComponentUtil.translate("rg255,204;오른쪽에 있는 아이템(%s)", secondItem));
			}
		}

		if (NBTAPI.isRestricted(player, firstItem, RestrictionType.NO_ANVIL_COMPOSITION_LEFT))
		{
			if (Objects.requireNonNull(firstItem).getType() != Material.ENCHANTED_BOOK && ItemStackUtil.itemExists(secondItem)
					&& firstItem.getType() == secondItem.getType())
			{
				restricted = true;
				lore.add(ComponentUtil.translate("&c왼쪽에 있는 아이템 같은 종류의 아이템끼리 합성할 수 없는 아이템이 존재합니다"));
				lore.add(ComponentUtil.translate("rg255,204;왼쪽에 있는 아이템(%s)", firstItem));
			}
		}

		if (NBTAPI.isRestricted(player, secondItem, RestrictionType.NO_ANVIL_COMPOSITION_RIGHT))
		{
			if (Objects.requireNonNull(secondItem).getType() != Material.ENCHANTED_BOOK && ItemStackUtil.itemExists(firstItem)
					&& firstItem.getType() == secondItem.getType())
			{
				restricted = true;
				lore.add(ComponentUtil.translate("&c오른쪽에 있는 아이템 같은 종류의 아이템끼리 합성할 수 없는 아이템이 존재합니다"));
				lore.add(ComponentUtil.translate("rg255,204;오른쪽에 있는 아이템(%s)", secondItem));
			}
		}

		boolean firstItemNoUseEnchantedBookComposition =
				ItemStackUtil.itemExists(firstItem) && firstItem.getType() == Material.ENCHANTED_BOOK && NBTAPI.isRestricted(player, firstItem,
						RestrictionType.NO_ANVIL_ENCHANTED_BOOK_COMPOSITION), secondItemNoUseEnchantedBookComposition =
				ItemStackUtil.itemExists(secondItem) && secondItem.getType() == Material.ENCHANTED_BOOK && NBTAPI.isRestricted(player, secondItem,
						RestrictionType.NO_ANVIL_ENCHANTED_BOOK_COMPOSITION);
		if (firstItemNoUseEnchantedBookComposition || secondItemNoUseEnchantedBookComposition)
		{
			lore.add(ComponentUtil.translate("&c모루에서 마법이 부여된 책끼리 합성할 수 없는 아이템이 존재합니다"));
		}
		if (firstItemExists && firstItemNoUseEnchantedBookComposition)
		{
			restricted = true;
			lore.add(ComponentUtil.translate("rg255,204;왼쪽에 있는 아이템(%s)", firstItem));
		}
		if (secondItemExists && secondItemNoUseEnchantedBookComposition)
		{
			restricted = true;
			lore.add(ComponentUtil.translate("rg255,204;오른쪽에 있는 아이템(%s)", secondItem));
		}

		if (NBTAPI.isRestricted(player, firstItem, RestrictionType.NO_ANVIL_ENCHANTED_BOOK_COMPOSITION_LEFT))
		{
			if (Objects.requireNonNull(firstItem).getType() == Material.ENCHANTED_BOOK && ItemStackUtil.itemExists(secondItem)
					&& firstItem.getType() == secondItem.getType())
			{
				restricted = true;
				lore.add(ComponentUtil.translate("&c왼쪽에 있는 마법이 부여된 책끼리 합성할 수 없는 아이템이 존재합니다"));
				lore.add(ComponentUtil.translate("rg255,204;왼쪽에 있는 아이템(%s)", firstItem));
			}
		}

		if (NBTAPI.isRestricted(player, secondItem, RestrictionType.NO_ANVIL_ENCHANTED_BOOK_COMPOSITION_RIGHT))
		{
			if (Objects.requireNonNull(secondItem).getType() == Material.ENCHANTED_BOOK && ItemStackUtil.itemExists(firstItem)
					&& firstItem.getType() == secondItem.getType())
			{
				restricted = true;
				lore.add(ComponentUtil.translate("&c오른쪽에 있는 마법이 부여된 책끼리 합성할 수 없는 아이템이 존재합니다"));
				lore.add(ComponentUtil.translate("rg255,204;오른쪽에 있는 아이템(%s)", secondItem));
			}
		}

		if (NBTAPI.isRestricted(player, firstItem, RestrictionType.NO_ANVIL_RENAME))
		{
			if (!Objects.equals(firstItemName, resultItemName))
			{
				restricted = true;
				lore.add(ComponentUtil.translate("&c모루에서 이름을 바꿀 수 없는 아이템이 존재합니다"));
				lore.add(ComponentUtil.translate("rg255,204;왼쪽에 있는 아이템(%s)", firstItem));
			}
		}

		if (NBTAPI.isRestricted(player, firstItem, RestrictionType.NO_ANVIL_ENCHANT) && ItemStackUtil.itemExists(resultItem))
		{
			ItemMeta firstItemMeta = Objects.requireNonNull(firstItem).getItemMeta();
			ItemMeta resultItemMeta = resultItem.getItemMeta();

			if (!firstItemMeta.getEnchants().equals(resultItemMeta.getEnchants()))
			{
				restricted = true;
				lore.add(ComponentUtil.translate("&c모루에서 마법을 부여할 수 없는 아이템이 존재합니다"));
				lore.add(ComponentUtil.translate("rg255,204;왼쪽에 있는 아이템(%s)", firstItem));
			}
		}

		boolean firstItemNoRepair = (NBTAPI.isRestricted(player, firstItem, RestrictionType.NO_ANVIL_REPAIR));
		boolean secondItemNoRepair = (NBTAPI.isRestricted(player, secondItem, RestrictionType.NO_ANVIL_REPAIR));
		if ((firstItemNoRepair || secondItemNoRepair) && ItemStackUtil.itemExists(resultItem))
		{
			int firstItemDamage = 0, secondItemDamage = 0;
			if (firstItemNoRepair)
			{
				firstItemDamage = ((Damageable) Objects.requireNonNull(firstItem).getItemMeta()).getDamage();
			}
			if (secondItemNoRepair)
			{
				secondItemDamage = ((Damageable) Objects.requireNonNull(secondItem).getItemMeta()).getDamage();
			}
			Damageable resultItemDuraMeta = (Damageable) resultItem.getItemMeta();
			int resultItemDamage = resultItemDuraMeta.getDamage();
			if ((firstItemNoRepair && firstItemDamage != resultItemDamage) || (secondItemNoRepair && secondItemDamage != resultItemDamage))
			{
				restricted = true;
				lore.add(ComponentUtil.translate("&c모루에서 수리할 수 없는 아이템이 존재합니다"));
				if (firstItemNoRepair)
				{
					lore.add(ComponentUtil.translate("rg255,204;왼쪽에 있는 아이템(" + firstItemName + "rg255,204;)"));
				}
				if (secondItemNoRepair)
				{
					lore.add(ComponentUtil.translate("rg255,204;오른쪽에 있는 아이템(" + secondItemName + "rg255,204;)"));
				}
			}
		}
		if (restricted && ItemStackUtil.itemExists(resultItem))
		{
			itemMeta.lore(lore);
			restrictedItem.setItemMeta(itemMeta);
			if (Cucumbery.config.getBoolean("blind-result-when-using-anvil-with-unavailable-item"))
			{
				event.setResult(null);
			}
			else
			{
				event.setResult(restrictedItem);
			}
			return;
		}
		int maxRepairCost = Cucumbery.config.getInt("max-anvil-repair-cost");
		if (maxRepairCost > -1)
		{
			anvilInventory.setMaximumRepairCost(maxRepairCost);
		}
		boolean customDuraFix = false;
		if (ItemStackUtil.itemExists(firstItem) && ItemStackUtil.itemExists(secondItem))
		{
			CustomMaterial firstCustomMaterial = CustomMaterial.itemStackOf(firstItem), secondCustomMaterial = CustomMaterial.itemStackOf(secondItem);
			if (firstCustomMaterial != null && secondCustomMaterial != null)
			{
				switch (firstCustomMaterial)
				{
					case TITANIUM_DRILL_R266, TITANIUM_DRILL_R366, TITANIUM_DRILL_R466, TITANIUM_DRILL_R566, MINDAS_DRILL ->
					{
						int repairAmount = switch (secondCustomMaterial)
						{
							case SMALL_DRILL_FUEL -> 100;
							case MEDIUM_DRILL_FUEL -> 1000;
							case LARGE_DRILL_FUEL -> 10000;
							default -> 0;
						};
						if (repairAmount > 0)
						{
							ItemStack result = firstItem.clone();
							NBTItem resultNBTItem = new NBTItem(result, true);
							NBTCompound itemTag = resultNBTItem.addCompound(CucumberyTag.KEY_MAIN);
							NBTCompound duraTag = itemTag.addCompound(CucumberyTag.CUSTOM_DURABILITY_KEY);
							long current = duraTag.getLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY);
							if (current > 0)
							{
								int fixCount = secondItem.getAmount();
								long calc = current - (long) fixCount * repairAmount;
								// 과다 수리 방지(수리된 내구도 - 수리량 <= 최대 내구도)
								if (calc >= 0)
								{
									duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY, Math.max(current - (long) fixCount * repairAmount, 0));
									event.setResult(result);
									event.getInventory().setRepairCost(5 * fixCount);
									customDuraFix = true;
								}
							}
						}
					}
				}
			}
			resultItem = anvilInventory.getItem(2);
		}

		// 커스텀 인챈트
		{
			if (CustomEnchant.isEnabled() && firstItemExists && secondItemExists)
			{
				CustomMaterial firstItemCustomMaterial = CustomMaterial.itemStackOf(firstItem), secondItemCustomMaterial = CustomMaterial.itemStackOf(secondItem);
				boolean applied = false;
				ItemStack result = ItemStackUtil.itemExists(event.getResult()) ? event.getResult() : firstItem.clone();
				ItemMeta resultMeta = result.getItemMeta();
				// 장비 + 책
				if (firstItem.getType() != Material.ENCHANTED_BOOK && secondItem.getType() == Material.ENCHANTED_BOOK)
				{
					ItemMeta firstItemMeta = firstItem.getItemMeta();
					EnchantmentStorageMeta secondItemMeta = (EnchantmentStorageMeta) secondItem.getItemMeta();
					Map<Enchantment, Integer> firstItemEnchants = firstItemMeta.getEnchants(), secondItemEnchants = secondItemMeta.getStoredEnchants();
					for (Enchantment enchantment : firstItemEnchants.keySet())
					{
						if (enchantment instanceof CustomEnchant)
						{
							resultMeta.addEnchant(enchantment, firstItemEnchants.get(enchantment), true);
							applied = true;
						}
					}
					for (Enchantment enchantment : secondItemEnchants.keySet())
					{
						// 인챈트 가능한 아이템인가?(예 : 효율은 도구에만)
						boolean canEnchant = enchantment.canEnchantItem(firstItem);
						// 특정 인챈트는 특정 아이템에 예외적으로 인챈트 가능(예: 드릴에 효율/행운)
						{
							if (enchantment.equals(Enchantment.DIG_SPEED) || enchantment.equals(Enchantment.LOOT_BONUS_BLOCKS))
							{
								if (firstItemCustomMaterial != null && firstItemCustomMaterial.isDrill())
								{
									canEnchant = true;
								}
							}
							// 방패에 가시 적용 가능
							if (enchantment.equals(Enchantment.THORNS) && firstItem.getType() == Material.SHIELD)
							{
								canEnchant = true;
							}
						}
						if (!canEnchant)
						{
							continue;
						}
						int level = secondItemEnchants.get(enchantment);
						if (!firstItemEnchants.keySet().isEmpty())
						{
							for (Enchantment firstEnchant : firstItemEnchants.keySet())
							{
								// 서로 다른 인챈트가 충돌할 경우 기존 인챈트를 제거한다
								if (!enchantment.equals(firstEnchant) && enchantment.conflictsWith(firstEnchant))
								{
									resultMeta.removeEnchant(firstEnchant);
									continue;
								}
								int firstEnchantLevel = firstItemEnchants.get(firstEnchant);
								// 인챈트가 서로 같을 경우
								if (enchantment.equals(firstEnchant))
								{
									// 레벨이 서로 같고 레벨 +1이 최대 레벨 이하일 경우
									if (level == firstEnchantLevel && level + 1 <= enchantment.getMaxLevel())
									{
										level += 1;
									}
									// 아닌 경우 만약 원래 인챈트 레벨이 더 높을 경우 기존 값 유지
									else if (firstEnchantLevel > level)
									{
										level = firstEnchantLevel;
									}
								}
							}
						}
						resultMeta.addEnchant(enchantment, level, true);
						applied = true;
					}
				}
				boolean equipmentEquals = (firstItemCustomMaterial == null && secondItemCustomMaterial == null && firstItem.getType() != Material.ENCHANTED_BOOK
						&& firstItem.getType() == secondItem.getType()) || (firstItemCustomMaterial != null && firstItemCustomMaterial == secondItemCustomMaterial);
				if (equipmentEquals)
				// 장비 + 장비
				{
					ItemMeta firstItemMeta = firstItem.getItemMeta(), secondItemMeta = secondItem.getItemMeta();
					Map<Enchantment, Integer> firstItemEnchants = firstItemMeta.getEnchants(), secondItemEnchants = secondItemMeta.getEnchants();
					for (Enchantment enchantment : firstItemEnchants.keySet())
					{
						if (enchantment instanceof CustomEnchant)
						{
							resultMeta.addEnchant(enchantment, firstItemEnchants.get(enchantment), true);
							applied = true;
						}
					}
					for (Enchantment enchantment : secondItemEnchants.keySet())
					{
						// 인챈트 가능한 아이템인가?(예 : 효율은 도구에만)
						boolean canEnchant = enchantment.canEnchantItem(firstItem);
						// 특정 인챈트는 특정 아이템에 예외적으로 인챈트 가능(예: 드릴에 효율/행운)
						{
							if (enchantment.equals(Enchantment.DIG_SPEED) || enchantment.equals(Enchantment.LOOT_BONUS_BLOCKS))
							{
								if (firstItemCustomMaterial != null && firstItemCustomMaterial.isDrill())
								{
									canEnchant = true;
								}
							}
							// 방패에 가시 적용 가능
							if (enchantment.equals(Enchantment.THORNS) && firstItem.getType() == Material.SHIELD)
							{
								canEnchant = true;
							}
						}
						if (!canEnchant)
						{
							continue;
						}
						int level = secondItemEnchants.get(enchantment);
						if (!firstItemEnchants.keySet().isEmpty())
						{
							for (Enchantment firstEnchant : firstItemEnchants.keySet())
							{
								// 서로 다른 인챈트가 충돌할 경우 기존 인챈트를 제거한다
								if (!enchantment.equals(firstEnchant) && enchantment.conflictsWith(firstEnchant))
								{
									resultMeta.removeEnchant(firstEnchant);
									continue;
								}
								int firstEnchantLevel = firstItemEnchants.get(firstEnchant);
								// 인챈트가 서로 같을 경우
								if (enchantment.equals(firstEnchant))
								{
									// 레벨이 서로 같고 레벨 +1이 최대 레벨 이하일 경우
									if (level == firstEnchantLevel && level + 1 <= enchantment.getMaxLevel())
									{
										level += 1;
									}
									// 아닌 경우 만약 원래 인챈트 레벨이 더 높을 경우 기존 값 유지
									else if (firstEnchantLevel > level)
									{
										level = firstEnchantLevel;
									}
								}
							}
						}
						resultMeta.addEnchant(enchantment, level, true);
						applied = true;
					}
				}
				if (firstItem.getType() == Material.ENCHANTED_BOOK && secondItem.getType() == Material.ENCHANTED_BOOK)
				// 책 + 책
				{
					EnchantmentStorageMeta firstItemMeta = (EnchantmentStorageMeta) firstItem.getItemMeta(), secondItemMeta = (EnchantmentStorageMeta) secondItem.getItemMeta();
					Map<Enchantment, Integer> firstItemEnchants = firstItemMeta.getStoredEnchants(), secondItemEnchants = secondItemMeta.getStoredEnchants();
					for (Enchantment enchantment : firstItemEnchants.keySet())
					{
						if (enchantment instanceof CustomEnchant)
						{
							((EnchantmentStorageMeta) resultMeta).addStoredEnchant(enchantment, firstItemEnchants.get(enchantment), true);
							applied = true;
						}
					}
					for (Enchantment enchantment : secondItemEnchants.keySet())
					{
						int level = secondItemEnchants.get(enchantment);
						if (!firstItemEnchants.keySet().isEmpty())
						{
							for (Enchantment firstEnchant : firstItemEnchants.keySet())
							{
								// 서로 다른 인챈트가 충돌할 경우 기존 인챈트를 제거한다
								if (!enchantment.equals(firstEnchant) && enchantment.conflictsWith(firstEnchant))
								{
									resultMeta.removeEnchant(firstEnchant);
									continue;
								}
								int firstEnchantLevel = firstItemEnchants.get(firstEnchant);
								// 인챈트가 서로 같을 경우
								if (enchantment.equals(firstEnchant))
								{
									// 레벨이 서로 같고 레벨 +1이 최대 레벨 이하일 경우
									if (level == firstEnchantLevel && level + 1 <= enchantment.getMaxLevel())
									{
										level += 1;
									}
									// 아닌 경우 만약 원래 인챈트 레벨이 더 높을 경우 기존 값 유지
									else if (firstEnchantLevel > level)
									{
										level = firstEnchantLevel;
									}
								}
							}
						}
						((EnchantmentStorageMeta) resultMeta).addStoredEnchant(enchantment, level, true);
						applied = true;
					}
				}
				if (applied)
				{
					result.setItemMeta(resultMeta);
					event.setResult(ItemLore.setItemLore(result, ItemLoreView.of(player)));
					if (anvilInventory.getRepairCost() <= 0)
					{
						anvilInventory.setRepairCost(5);
					}
				}
			}
		}

		NBTCompound itemTag = NBTAPI.getMainCompound(firstItem);
		NBTCompound duraTag = NBTAPI.getCompound(itemTag, CucumberyTag.CUSTOM_DURABILITY_KEY);
		if (!customDuraFix && ItemStackUtil.itemExists(resultItem) && ItemStackUtil.itemExists(secondItem) && duraTag != null)
		{
			long maxDura = duraTag.getLong(CucumberyTag.CUSTOM_DURABILITY_MAX_KEY);
			long curDura = duraTag.getLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY);
			int firstItemMaxOriginMaxDura = Objects.requireNonNull(firstItem).getType().getMaxDurability();
			int firstItemOriginDura = firstItem.getType().getMaxDurability() - ((Damageable) firstItem.getItemMeta()).getDamage();
			int resultDamage = ((Damageable) resultItem.getItemMeta()).getDamage();
			int resultItemOriginDura = resultItem.getType().getMaxDurability() - resultDamage;
			double ratio = (resultItemOriginDura - firstItemOriginDura * 1d) / firstItemMaxOriginMaxDura;
			NBTCompound secondItemDuraTag = NBTAPI.getCompound(NBTAPI.getMainCompound(secondItem), CucumberyTag.CUSTOM_DURABILITY_KEY);
			if (secondItemDuraTag != null)
			{
				long secondItemcurDura = secondItemDuraTag.getLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY);
				curDura -= secondItemcurDura;
				curDura -= (long) (maxDura * 0.1);
			}
			else if (firstItem.getType() == secondItem.getType())
			{
				int fixAmount = firstItemMaxOriginMaxDura - ((Damageable) secondItem.getItemMeta()).getDamage();
				curDura -= fixAmount;
				curDura -= (long) (maxDura * 0.1);
			}
			else
			{
				long fixAmount = resultItemOriginDura == resultItem.getType().getMaxDurability() ? (maxDura - curDura) : (long) Math.min(ratio * maxDura, maxDura);
				curDura -= fixAmount;
			}
			if (curDura < 0)
			{
				curDura = 0;
			}
			NBTItem nbtItem = new NBTItem(resultItem);
			NBTCompound resultItemTag = nbtItem.getCompound(CucumberyTag.KEY_MAIN);
			NBTCompound resultDuraTag = NBTAPI.getCompound(resultItemTag, CucumberyTag.CUSTOM_DURABILITY_KEY);
			if (resultDuraTag != null)
			{
				resultDuraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY, curDura);
			}
			resultItem = nbtItem.getItem();
			Damageable duraMeta = (Damageable) resultItem.getItemMeta();
			duraMeta.setDamage(resultDamage);
			resultItem.setItemMeta(duraMeta);
			ItemLore.setItemLore(resultItem, new ItemLoreView(player));
			event.setResult(resultItem);
		}
		else if (Method.usingLoreFeature(player))
		{
			ItemStack item = event.getResult();
			if (ItemStackUtil.itemExists(item))
			{
				ItemLore.setItemLore(item, new ItemLoreView(player));
			}
		}
	}
}

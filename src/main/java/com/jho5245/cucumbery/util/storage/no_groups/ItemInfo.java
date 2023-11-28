package com.jho5245.cucumbery.util.storage.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.ItemSerializer;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.banner.Pattern;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.map.MapView;
import org.bukkit.map.MapView.Scale;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemInfo
{
	public static void sendInfo(CommandSender sender, ItemStack item, boolean mainHand, boolean shortend)
	{
		if (mainHand)
		{
			MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "주로 사용하는 손에 들고 있는 아이템 정보");
		}
		else
		{
			MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "다른 손에 들고 있는 아이템 정보");
		}
		sendInfo(sender, item, shortend);
	}

	public static void sendInfo(CommandSender sender, ItemStack item)
	{
		sendInfo(sender, item, true);
	}

	@SuppressWarnings("all")
	public static void sendInfo(CommandSender sender, ItemStack item, boolean shortend)
	{
		try
		{
			if (!ItemStackUtil.itemExists(item))
			{
				MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "아이템 코드 : %s, 내구도 : %s, 개수 : %s", "air", -1, 0);
				return;
			}
			int durability = ((Damageable) item.getItemMeta()).getDamage();
			Material type = item.getType();
			ItemStack itemStackWithoutTMI = item.clone(), itemStackForTranslationName = itemStackWithoutTMI.clone();
			ItemMeta clone3Meta = itemStackForTranslationName.getItemMeta();
			clone3Meta.displayName(null);
			itemStackForTranslationName.setItemMeta(clone3Meta);
			MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "아이템 코드 : %s, 내구도 : %s, 개수 : %s", type.toString().toLowerCase(), durability, item.getAmount());
			MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "아이템 번역 이름 : %s", ItemNameUtil.itemName(itemStackForTranslationName));
			Component itemComponent = ComponentUtil.create(sender instanceof Player player ? player : null, item);
			MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "미리 보기 : %s",
					ComponentUtil.create("§3[마우스를 올리세요]").hoverEvent(itemComponent.hoverEvent()).clickEvent(itemComponent.clickEvent()));
			ItemLore.removeItemLore(itemStackWithoutTMI);
			MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, Constant.SEPARATOR.substring(0, Constant.SEPARATOR.length() - 1));
			String serial = ItemSerializer.serialize(itemStackWithoutTMI);
			MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "NBT(NO TMI) : %s",
					ComponentUtil.create("§3[마우스를 올리세요(클릭하여 복사)]").hoverEvent(HoverEvent.showText(ComponentUtil.create2(serial.replace("\"", "§r\""), false)))
							.clickEvent(ClickEvent.copyToClipboard(serial)));
			serial = ItemSerializer.serialize(item);
			MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "NBT : %s",
					ComponentUtil.create("§3[마우스를 올리세요(클릭하여 복사)]").hoverEvent(HoverEvent.showText(ComponentUtil.create2(serial.replace("\"", "§r\""), false)))
							.clickEvent(ClickEvent.copyToClipboard(serial)));
			MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, Constant.SEPARATOR.substring(0, Constant.SEPARATOR.length() - 1));
			ItemMeta itemMeta = item.getItemMeta();

			if (itemMeta.hasLocalizedName())
			{
				MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, itemMeta.getLocalizedName());
			}
			if (itemMeta.isUnbreakable())
			{
				MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, ((itemMeta.isUnbreakable()) ? "&a내구도 무한 아이템" : "&c내구도 무한 아이템이 아님"));
			}
			if (itemMeta.hasEnchants())
			{
				MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "----- 부여된 마법 목록 -----");
				Component message = Prefix.INFO_ITEMSTORAGE.get();
				Map<Enchantment, Integer> enchants = itemMeta.getEnchants();
				for (Enchantment enchantment : enchants.keySet())
				{
					int level = itemMeta.getEnchantLevel(enchantment);
					int maxLevel = enchantment.getMaxLevel();
					boolean isCursed = enchantment.isCursed();
					Component component;
					if (maxLevel == 1 && level == 1)
					{
						component = ComponentUtil.translate(enchantment.translationKey());
					}
					else
					{
						component = ComponentUtil.translate("%s %s", ComponentUtil.translate(enchantment.translationKey()), level);
					}
					component = component.color(isCursed ? TextColor.color(255, 85, 85) : TextColor.color(154, 84, 255));
					message = message.append(component).append(Component.text(" "));
				}
				MessageUtil.sendMessage(sender, message);
			}
			else
			{
				MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "&c부여된 마법 없음");
			}
			if ((itemMeta instanceof EnchantmentStorageMeta))
			{
				MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "-- 저장된 마법 목록 --");
				EnchantmentStorageMeta storeMeta = (EnchantmentStorageMeta) itemMeta;
				if (storeMeta.hasStoredEnchants())
				{
					Component message = Prefix.INFO_ITEMSTORAGE.get();
					Map<Enchantment, Integer> enchants = storeMeta.getStoredEnchants();
					for (Enchantment enchantment : enchants.keySet())
					{
						int level = storeMeta.getStoredEnchantLevel(enchantment);
						int maxLevel = enchantment.getMaxLevel();
						boolean isCursed = enchantment.isCursed();
						Component component;
						if (maxLevel == 1 && level == 1)
						{
							component = ComponentUtil.translate(enchantment.translationKey());
						}
						else
						{
							component = ComponentUtil.translate("%s %s", ComponentUtil.translate(enchantment.translationKey()), level);
						}
						component = component.color(isCursed ? TextColor.color(255, 85, 85) : TextColor.color(154, 84, 255));
						message = message.append(component).append(Component.text(" "));
					}
					MessageUtil.sendMessage(sender, message);
				}
				else
				{
					MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "&c저장된 마법 없음");
				}
			}
			if ((itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)) || (itemMeta.hasItemFlag(ItemFlag.HIDE_DESTROYS)) || (itemMeta.hasItemFlag(ItemFlag.HIDE_ENCHANTS))
					|| (itemMeta.hasItemFlag(ItemFlag.HIDE_PLACED_ON)) || (itemMeta.hasItemFlag(ItemFlag.HIDE_ITEM_SPECIFICS)) || (itemMeta.hasItemFlag(
					ItemFlag.HIDE_UNBREAKABLE)))
			{
				MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "-- 아이템 플래그 목록 --");
				String flag = Prefix.INFO_ITEMSTORAGE.toString();
				if (itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES))
				{
					flag += "설명 숨김, ";
				}
				if (itemMeta.hasItemFlag(ItemFlag.HIDE_DESTROYS))
				{
					flag += "파괴 가능 블록 숨김, ";
				}
				if (itemMeta.hasItemFlag(ItemFlag.HIDE_ENCHANTS))
				{
					flag += "부여된 마법 숨김, ";
				}
				if (itemMeta.hasItemFlag(ItemFlag.HIDE_PLACED_ON))
				{
					flag += "설치 가능 블록 숨김, ";
				}
				if (itemMeta.hasItemFlag(ItemFlag.HIDE_ITEM_SPECIFICS))
				{
					flag += "포션 효과 숨김, ";
				}
				if (itemMeta.hasItemFlag(ItemFlag.HIDE_UNBREAKABLE))
				{
					flag += "내구성 숨김, ";
				}
				flag = flag.substring(0, flag.length() - 2);
				MessageUtil.sendMessage(sender, flag);
			}
			else
			{
				MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "&c아이템 플래그 없음");
			}
			if (itemMeta instanceof Repairable)
			{
				Repairable repairMeta = (Repairable) itemMeta;
				if (repairMeta.hasRepairCost())
				{
					MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "&r모루 사용 횟수 : rg255,204;" + ItemStackUtil.getAnvilUsedTime(itemMeta) + "번");
				}
				else
				{
					MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "&c모루 사용 횟수 없음");
				}
			}
			switch (type)
			{
				case PLAYER_HEAD:
					String name = PlayerHeadInfo.getPlayerHeadInfo(item, PlayerHeadInfo.PlayerHeadInfoType.NAME);
					String uuid = PlayerHeadInfo.getPlayerHeadInfo(item, PlayerHeadInfo.PlayerHeadInfoType.UUID);
					String url = PlayerHeadInfo.getPlayerHeadInfo(item, PlayerHeadInfo.PlayerHeadInfoType.URL);
					MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "&9[플레이어 머리 정보]");
					MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, Constant.THE_COLOR_HEX + "닉네임 : rg255,204;" + (name != null ? name : "&c없음"));
					MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, Constant.THE_COLOR_HEX + "UUID : rg255,204;" + (uuid != null ? uuid : "&c없음"));
					MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, Constant.THE_COLOR_HEX + "url : %s",
							url != null ? Component.text(url).color(NamedTextColor.YELLOW)
									.hoverEvent(ComponentUtil.translate("chat.link.open").append(Component.text("\n")).append(ComponentUtil.translate("시프트 클릭하여 hash 채팅창에 입력")))
									.clickEvent(ClickEvent.openUrl(url)).insertion(url.split("texture/")[1]) : "&c없음");
					break;
				case POTION:
				case SPLASH_POTION:
				case LINGERING_POTION:
				case TIPPED_ARROW:
					PotionMeta potionMeta = (PotionMeta) itemMeta;
					boolean isExtended;
					if (!potionMeta.hasCustomEffects())
					{
						MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "&c커스텀 포션 효과 없음 &r(기본값 : rg255,204;" + item + "&r)");
						PotionEffectType potionEffectType = potionMeta.getBasePotionData().getType().getEffectType();
						int maxLevel = potionMeta.getBasePotionData().getType().getMaxLevel();
						isExtended = potionMeta.getBasePotionData().isExtended();
						boolean isUpgraded = potionMeta.getBasePotionData().isUpgraded();
						if (potionMeta.getBasePotionData().getType().getEffectType() != null)
						{
							Color color = potionMeta.getBasePotionData().getType().getEffectType().getColor();
							int r = color.getRed();
							int g = color.getGreen();
							int b = color.getBlue();
							MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "포션 색상 : &c빨강 " + r + "&r, &a초록 " + g + "&r, &9파랑 " + b);
							MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE,
									"포션 효과 : rg255,204;" + potionEffectType + "&r (rg255,204;내부 이름 : rg255,204;" + potionEffectType.getName() + "&r)");
							MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "포션 최대 레벨 : rg255,204;" + maxLevel);
							if (isExtended)
							{
								MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "지속 시간 증가 적용 (레드스톤 사용)");
							}
							if (isUpgraded)
							{
								MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "효과 증폭 적용 (발광석 가루 사용)");
							}
						}
					}
					else
					{
						MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "커스텀 포션 효과 개수 : rg255,204;" + potionMeta.getCustomEffects().size() + "개");
						MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "---커스텀 포션 효과 목록---");
						if (shortend && potionMeta.getCustomEffects().size() > 5 && sender instanceof Player)
						{
							List<Component> msg = new ArrayList<>();
							msg.add(ComponentUtil.create(Prefix.INFO_ITEMSTORAGE + "&b"));
							for (int i = 0; i < potionMeta.getCustomEffects().size(); i++)
							{
								if (i != 0)
								{
									msg.add(ComponentUtil.create(" "));
								}
								PotionEffect effect = potionMeta.getCustomEffects().get(i);
								int amplifier = effect.getAmplifier();
								int duration = effect.getDuration();
								PotionEffectType PotionEffectType = effect.getType();
								msg.add(ComponentUtil.create("[" + (i + 1) + "]",
										"&r효과 이름 : rg255,204;" + (PotionEffectType) + "\n&r농도 레벨 : rg255,204;" + amplifier + "단계\n&r지속 시간 : rg255,204;" + Method.timeFormatMilli(
												duration * 50L)));
							}
							Component[] arr = new Component[msg.size()];
							MessageUtil.sendMessage(sender, msg.toArray(arr));
						}
						else
						{
							StringBuilder effects = new StringBuilder(Prefix.INFO_ITEMSTORAGE.toString());
							for (PotionEffect effect : potionMeta.getCustomEffects())
							{
								int amplifier = effect.getAmplifier();
								int duration = effect.getDuration();
								PotionEffectType PotionEffectType = effect.getType();
								effects.append("&r효과 이름 : rg255,204;").append(PotionEffectType).append("&r, 농도 레벨 : rg255,204;").append(amplifier)
										.append("단계&r, 지속 시간 : rg255,204;").append(Method.timeFormatMilli(duration * 50L)).append("\n");
							}
							MessageUtil.sendMessage(sender, effects.toString().endsWith("\n") ? effects.substring(0, effects.length() - 1) : effects.toString());
						}
					}
					break;
				case FIREWORK_ROCKET:
					FireworkMeta fireworkMeta = (FireworkMeta) itemMeta;
					MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "폭죽 비행 시간 : rg255,204;" + fireworkMeta.getPower());
					if (!fireworkMeta.hasEffects())
					{
						MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "&c폭죽 효과 없음");
					}
					else
					{
						MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "폭죽 효과 개수 : rg255,204;" + fireworkMeta.getEffectsSize() + "개");
						if (sender instanceof Player)
						{
							List<Component> msg = new ArrayList<>();
							msg.add(ComponentUtil.create(Prefix.INFO_ITEMSTORAGE + "&b"));
							for (int i = 0; i < fireworkMeta.getEffectsSize(); i++)
							{
								if (i != 0)
								{
									msg.add(ComponentUtil.create(" "));
								}
								FireworkEffect fireworkEffect = fireworkMeta.getEffects().get(i);
								StringBuilder fireworkMsg = new StringBuilder("&r폭죽 모양 : rg255,204;" + (fireworkEffect.getType()) + "\n");
								for (int j = 0; j < fireworkEffect.getColors().size(); j++)
								{
									Color color = fireworkEffect.getColors().get(j);
									int red = color.getRed();
									int green = color.getGreen();
									int blue = color.getBlue();
									fireworkMsg.append("rg255,204;").append(j + 1).append("번째&r 폭죽 색상 : rg255,204;").append(color).append("&r(&c빨강 ").append(red)
											.append("&r, &a초록 ").append(green).append("&r, &9파랑 ").append(blue).append("&r)\n");
								}
								for (int j = 0; j < fireworkEffect.getFadeColors().size(); j++)
								{
									Color color = fireworkEffect.getFadeColors().get(j);
									int red = color.getRed();
									int green = color.getGreen();
									int blue = color.getBlue();
									fireworkMsg.append("rg255,204;").append(j + 1).append("번째&r 사라지는 효과 색상 : rg255,204;").append(color).append("&r(&c빨강 ").append(red)
											.append("&r, &a초록 ").append(green).append("&r, &9파랑 ").append(blue).append("&r)\n");
								}
								if (fireworkEffect.hasTrail())
								{
									fireworkMsg.append("rg255,204;잔상 효과&r\n");
								}
								if (fireworkEffect.hasFlicker())
								{
									fireworkMsg.append("rg255,204;반짝이는 효과&r\n");
								}
								msg.add(ComponentUtil.create("&b[" + (i + 1) + "]", fireworkMsg.substring(0, fireworkMsg.length() - 1)));
							}
							Component[] arr = new Component[msg.size()];
							MessageUtil.sendMessage(sender, msg.toArray(arr));
						}
						else
						{
							for (int i = 0; i < fireworkMeta.getEffectsSize(); i++)
							{
								FireworkEffect fireworkEffect = fireworkMeta.getEffects().get(i);
								StringBuilder fireworkMsg = new StringBuilder(Prefix.INFO_ITEMSTORAGE + "폭죽 모양 : rg255,204;" + fireworkEffect.getType() + "\n");
								for (int j = 0; j < fireworkEffect.getColors().size(); j++)
								{
									Color color = fireworkEffect.getColors().get(j);
									int red = color.getRed();
									int green = color.getGreen();
									int blue = color.getBlue();
									fireworkMsg.append("rg255,204;").append(j + 1).append("번째&r 폭죽 색상 : rg255,204;").append(color).append("&r(&c빨강 ").append(red)
											.append("&r, &a초록 ").append(green).append("&r, &9파랑 ").append(blue).append("&r), ");
								}
								for (int j = 0; j < fireworkEffect.getFadeColors().size(); j++)
								{
									Color color = fireworkEffect.getFadeColors().get(j);
									int red = color.getRed();
									int green = color.getGreen();
									int blue = color.getBlue();
									fireworkMsg.append("rg255,204;").append(j + 1).append("번째&r 사라지는 효과 색상 : rg255,204;").append(color).append("&r(&c빨강 ").append(red)
											.append("&r, &a초록 ").append(green).append("&r, &9파랑 ").append(blue).append("&r), ");
								}
								if (fireworkEffect.hasTrail())
								{
									fireworkMsg.append("rg255,204;잔상 효과&r, ");
								}
								if (fireworkEffect.hasFlicker())
								{
									fireworkMsg.append("rg255,204;반짝이는 효과&r, ");
								}
								MessageUtil.sendMessage(sender, fireworkMsg.substring(0, fireworkMsg.length() - 2));
							}
						}
					}
					break;
				case FIREWORK_STAR:
					FireworkEffectMeta fireworkEffectMeta = (FireworkEffectMeta) itemMeta;
					if (!fireworkEffectMeta.hasEffect())
					{
						MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "&c폭죽 탄약 효과 없음");
					}
					else
					{
						FireworkEffect fireworkEffect = fireworkEffectMeta.getEffect();
						StringBuilder msg = new StringBuilder(Prefix.INFO_ITEMSTORAGE + "폭죽 모양 : rg255,204;" + fireworkEffect.getType() + "&r, 폭죽 색상 rg255,204;");
						for (int j = 0; j < fireworkEffect.getColors().size(); j++)
						{
							Color color = fireworkEffect.getColors().get(j);
							int red = color.getRed();
							int green = color.getGreen();
							int blue = color.getBlue();
							msg.append("폭죽 색상 : rg255,204;").append(color).append("&r(&c빨강 ").append(red).append("&r, &a초록 ").append(green).append("&r, &9파랑 ").append(blue)
									.append("&r)");
						}
						for (int j = 0; j < fireworkEffect.getFadeColors().size(); j++)
						{
							Color color = fireworkEffect.getFadeColors().get(j);
							int red = color.getRed();
							int green = color.getGreen();
							int blue = color.getBlue();
							msg.append(", 사라지는 효과 색상 : rg255,204;").append(color).append("&r(&c빨강 ").append(red).append("&r, &a초록 ").append(green).append("&r, &9파랑 ")
									.append(blue).append("&r)");
						}
						if (fireworkEffect.hasTrail())
						{
							msg.append(", rg255,204;잔상 효과&r");
						}
						if (fireworkEffect.hasFlicker())
						{
							msg.append(", rg255,204;반짝이는 효과&r");
						}
						MessageUtil.sendMessage(sender, msg.toString());
					}
					break;
				case WRITABLE_BOOK:
					BookMeta bookMeta = (BookMeta) itemMeta;
					int page = bookMeta.getPageCount();
					if (page > 0)
					{
						MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "책 페이지 수 : " + page);
						if (sender instanceof Player)
						{
							MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "--책 내용--");
							List<String> pages = bookMeta.getPages();
							List<Component> msg = new ArrayList<>();
							msg.add(ComponentUtil.create(Prefix.INFO_ITEMSTORAGE + "&b"));
							for (int i = 0; i < pages.size(); i++)
							{
								if (pages.get(i).length() > 0)
								{
									if (i != 0)
									{
										msg.add(ComponentUtil.create(" "));
									}
									msg.add(ComponentUtil.create("[" + (i + 1) + "]", pages.get(i)));
								}
							}
							Component[] arr = new Component[msg.size()];
							MessageUtil.sendMessage(sender, msg.toArray(arr));
							MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "--책 내용(색깔 없음)--");
							msg = new ArrayList<>();
							msg.add(ComponentUtil.create(Prefix.INFO_ITEMSTORAGE + "&b"));
							for (int i = 0; i < pages.size(); i++)
							{
								if (pages.get(i).length() > 0)
								{
									if (i != 0)
									{
										msg.add(ComponentUtil.create(" "));
									}
									msg.add(ComponentUtil.create("[" + (i + 1) + "]", pages.get(i).replace("§", "&")));
								}
							}
							arr = new Component[msg.size()];
							MessageUtil.sendMessage(sender, msg.toArray(arr));
						}
					}
					break;
				case WRITTEN_BOOK:
					bookMeta = (BookMeta) itemMeta;
					String title = bookMeta.getTitle();
					String author = bookMeta.getAuthor();
					String generation;
					page = bookMeta.getPageCount();
					if (bookMeta.hasGeneration())
					{
						switch (bookMeta.getGeneration())
						{
							case ORIGINAL:
								generation = "원본";
								break;
							case COPY_OF_ORIGINAL:
								generation = "원본의 복사본";
								break;
							case COPY_OF_COPY:
								generation = "복사본의 복사본";
								break;
							case TATTERED:
							default:
								generation = "낡음";
								break;
						}
					}
					else
					{
						generation = "원본";
					}
					MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "책 제목 : " + (title != null ? title : "없음"));
					MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "책 저자 : " + ((author != null && author.length() != 0) ? author : "알 수 없음"));
					MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "책 출판 : " + generation);
					if (page > 0)
					{
						MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "책 페이지 수 : " + page);
						if (sender instanceof Player)
						{
							MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "--책 내용--");
							List<String> pages = bookMeta.getPages();
							List<Component> msg = new ArrayList<>();
							msg.add(ComponentUtil.create(Prefix.INFO_ITEMSTORAGE + "&b"));
							for (int i = 0; i < pages.size(); i++)
							{
								if (pages.get(i).length() > 0)
								{
									if (i != 0)
									{
										msg.add(ComponentUtil.create(" "));
									}
									msg.add(ComponentUtil.create("[" + (i + 1) + "]", pages.get(i)));
								}
							}
							Component[] arr = new Component[msg.size()];
							MessageUtil.sendMessage(sender, msg.toArray(arr));
							MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "--책 내용(색깔 없음)--");
							msg = new ArrayList<>();
							msg.add(ComponentUtil.create(Prefix.INFO_ITEMSTORAGE + "&b"));
							for (int i = 0; i < pages.size(); i++)
							{
								if (pages.get(i).length() > 0)
								{
									if (i != 0)
									{
										msg.add(ComponentUtil.create(" "));
									}
									msg.add(ComponentUtil.create("[" + (i + 1) + "]", pages.get(i).replace("§", "&")));
								}
							}
							arr = new Component[msg.size()];
							MessageUtil.sendMessage(sender, msg.toArray(arr));
						}
					}
					break;
				case WHITE_BANNER:
				case BLACK_BANNER:
				case BLUE_BANNER:
				case BROWN_BANNER:
				case CYAN_BANNER:
				case GRAY_BANNER:
				case GREEN_BANNER:
				case LIGHT_BLUE_BANNER:
				case LIGHT_GRAY_BANNER:
				case LIME_BANNER:
				case MAGENTA_BANNER:
				case ORANGE_BANNER:
				case PURPLE_BANNER:
				case PINK_BANNER:
				case RED_BANNER:
				case YELLOW_BANNER:
					BannerMeta bannerMeta = (BannerMeta) itemMeta;
					if (bannerMeta.numberOfPatterns() != 0)
					{
						MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "-- 현수막 무늬 목록 --");
						if (bannerMeta.numberOfPatterns() > 5 && sender instanceof Player)
						{
							List<Pattern> patterns = bannerMeta.getPatterns();
							List<Component> msg = new ArrayList<>();
							msg.add(ComponentUtil.create(Prefix.INFO_ITEMSTORAGE + "&b"));
							for (int i = 0; i < patterns.size(); i++)
							{
								if (i != 0)
								{
									msg.add(ComponentUtil.create(" "));
								}
								Pattern pattern = patterns.get(i);
								msg.add(ComponentUtil.create("[" + (i + 1) + "]", pattern.getColor() + " " + (pattern.getPattern())));
							}
							Component[] arr = new Component[msg.size()];
							MessageUtil.sendMessage(sender, msg.toArray(arr));
						}
						else
						{
							List<Pattern> patterns = bannerMeta.getPatterns();
							for (Pattern pattern : patterns)
							{
								MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "" + (pattern.getColor()) + " " + (pattern.getPattern()) + "&r");
							}
						}
					}
					else
					{
						MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "&c현수막 무늬 없음");
					}
					break;
				case SHIELD:
					BlockStateMeta blockStateMeta = (BlockStateMeta) itemMeta;
					BlockState blockState = blockStateMeta.getBlockState();
					Banner bannerState = (Banner) blockState;
					List<Pattern> patterns = bannerState.getPatterns();
					if (bannerState.numberOfPatterns() != 0)
					{
						MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "-- 방패 무늬 목록 --");
						if (bannerState.numberOfPatterns() > 5 && sender instanceof Player)
						{
							List<Component> msg = new ArrayList<>();
							msg.add(ComponentUtil.create(Prefix.INFO_ITEMSTORAGE + "&b"));
							for (int i = 0; i < patterns.size(); i++)
							{
								if (i != 0)
								{
									msg.add(ComponentUtil.create(" "));
								}
								Pattern pattern = patterns.get(i);
								msg.add(ComponentUtil.create("[" + (i + 1) + "]", (pattern.getColor()) + " " + (pattern.getPattern())));
							}
							Component[] arr = new Component[msg.size()];
							MessageUtil.sendMessage(sender, msg.toArray(arr));
						}
						else
						{
							for (Pattern pattern : patterns)
							{
								MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "" + pattern.getColor() + " " + pattern.getPattern() + "&r");
							}
						}
					}
					else
					{
						MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "&c방패 무늬 없음");
					}
					break;
				case TROPICAL_FISH_BUCKET:
					TropicalFishBucketMeta bucketMeta = (TropicalFishBucketMeta) itemMeta;
					if (bucketMeta.hasVariant())
					{
						MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "--열대어가 담긴 양동이 정보--");
						MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "물고기 종 : rg255,204;" + item);
					}
					else
					{
						MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "&c열대어가 담긴 양동이 정보 없음");
					}
					break;
				case SUSPICIOUS_STEW:
					MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "--수상한 스튜 포션 효과 정보--");
					SuspiciousStewMeta stewMeta = (SuspiciousStewMeta) itemMeta;
					if (!stewMeta.hasCustomEffects())
					{
						MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "&c수상한 스튜 포션 효과 없음");
					}
					else
					{
						for (PotionEffect effect : stewMeta.getCustomEffects())
						{
							int count = 1;
							String id = (effect.getType()).toString();
							int duration = effect.getDuration();
							int amplifier = effect.getAmplifier();
							MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE,
									Constant.THE_COLOR_HEX + count + "번&r째 효과 - 이름 : rg255,204;" + id + "&r, 지속 시간 : rg255,204;" + Method.timeFormatMilli(duration * 50L)
											+ "&r, 증폭 : rg255,204;" + amplifier);
						}
					}
					// try
					// {
					// String itemString = ConvertItemStack.converItemStackToJsonRegular(item).split("Effects:")[1].replace("\\{", "").replace("\\[", "").replace("\\]",
					// "").replace("\\}", "");
					// if (itemString.contains("AttributeModifiers"))
					// itemString = itemString.split(",AttributeModifiers")[0];
					// if (itemString.contains("CanDestroy"))
					// itemString = itemString.split(",CanDestroy")[0];
					// if (itemString.contains("CanPlaceOn"))
					// itemString = itemString.split(",CanPlaceOn")[0];
					// String[] cut = itemString.split("\\,");
					// List<String> effects = new ArrayList<>();
					// for (int i = 0; i < cut.length; i += 2)
					// {
					// effects.add(cut[i] + "," + cut[i + 1]);
					// }
					// if (effects.size() > 2 && sender instanceof Player)
					// {
					// List<Component> msg = new ArrayList<>();
					// msg.add(ComponentUtil.create(Prefix.INFO_ITEMSTORAGE + "&b"));
					// for (int i = 0; i < effects.size(); i++)
					// {
					// String effect = effects.get(i);
					// if (i != 0)
					// msg.add(ComponentUtil.create(" "));
					// effect = effect.replace("EffectDuration:", "").replace("EffectId:", "").replace("b", "");
					// int duration = Integer.parseInt(effect.split("\\,")[0]);
					// int id = Integer.parseInt(effect.split("\\,")[1]);
					// String effectType = PotionName.getPotionEffectName(id);
					// msg.add(ComponentUtil.create("[" + (i + 1) + "]", "포션 효과 : rg255,204;" + effectType + "&r, 지속 시간 : rg255,204;" + Constant.Sosu2.format(duration / 20D) + "rg255,204;초"));
					// }
					// Component[] arr = new Component[msg.size()];
					// MessageUtil.sendMessage(sender, msg.toArray(arr));
					// }
					// else
					// {
					// for (String effect : effects)
					// {
					// effect = effect.replace("EffectDuration:", "").replace("EffectId:", "").replace("b", "");
					// int duration = Integer.parseInt(effect.split("\\,")[0]);
					// int id = Integer.parseInt(effect.split("\\,")[1]);
					// String effectType = PotionName.getPotionEffectName(id);
					// MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "포션 효과 : rg255,204;" + effectType + "&r, 지속 시간 : rg255,204;" + Constant.Sosu2.format(duration / 20D) + "rg255,204;초");
					// }
					// }
					// }
					//
					// catch (Exception e)
					// {
					// break;
					// }
					break;
				case FILLED_MAP:
					MapMeta mapMeta = (MapMeta) itemMeta;
					if (mapMeta.hasMapView())
					{
						mapMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
						MapView mapView = mapMeta.getMapView();
						int centerX = mapView.getCenterX(), centerZ = mapView.getCenterZ();
						int id = mapView.getId();
						Scale scale = mapView.getScale();
						String scaleString = "알 수 없음";
						switch (scale)
						{
							case CLOSE:
								scaleString = "1:2";
								break;
							case CLOSEST:
								scaleString = "1:1";
								break;
							case FAR:
								scaleString = "1:8";
								break;
							case FARTHEST:
								scaleString = "1:16";
								break;
							case NORMAL:
								scaleString = "1:4";
								break;
							default:
								break;
						}
						World world = mapView.getWorld();
						String worldName = Method.getWorldDisplayName(world);
						MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "--지도 정보--");
						MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "rg255,204;지도 ID : " + id + "&r");
						MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "rg255,204;축척 : " + scaleString + "&r");
						MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "rg255,204;월드 : " + worldName + "&r");
						MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "rg255,204;지도 중심 좌표 : x=" + centerX + ", z=" + centerZ + "&r");
					}
					else
					{
						MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "&c");
					}
					break;
				case LEATHER_BOOTS:
				case LEATHER_CHESTPLATE:
				case LEATHER_HELMET:
				case LEATHER_LEGGINGS:
					LeatherArmorMeta armorMeta = (LeatherArmorMeta) itemMeta;
					Color color = armorMeta.getColor();
					int red = color.getRed(), green = color.getGreen(), blue = color.getBlue();
					MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE,
							"가죽 갑옷 색상 : rg255,204;" + (color) + "&r(&c빨강 " + red + "&r, &a초록 " + green + "&r, &9파랑 " + blue + "&r)");
					break;
				case ACACIA_SIGN:
				case BIRCH_SIGN:
				case DARK_OAK_SIGN:
				case JUNGLE_SIGN:
				case OAK_SIGN:
				case SPRUCE_SIGN:
					BlockStateMeta signMeta = (BlockStateMeta) item.getItemMeta();
					Sign sign = (Sign) signMeta.getBlockState();
					int lineSize = 0;
					for (String line : sign.getLines())
					{
						if (line.length() > 0)
						{
							lineSize++;
						}
					}
					if (lineSize > 0)
					{
						MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "--표지판 설명--");
						for (int i = 0; i < sign.getLines().length; i++)
						{
							String line = sign.getLine(i);
							if (line.length() > 0)
							{
								MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "&6" + (i + 1) + "&6번째 줄rg255,204; : " + line + "&r");
							}
						}
						MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "--표지판 설명(색깔 없음)--");
						for (int i = 0; i < sign.getLines().length; i++)
						{
							String line = sign.getLine(i);
							if (line.length() > 0)
							{
								MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "&6" + (i + 1) + "&6번째 줄rg255,204; : " + line.replace("§", "&") + "&r");
							}
						}
					}
				default:
					break;
			}
			NBTCompoundList restrictions = NBTAPI.getCompoundList(NBTAPI.getMainCompound(item), CucumberyTag.ITEM_USAGE_RESTRICTIONS_KEY);
			if (restrictions != null && restrictions.size() > 0)
			{
				StringBuilder msg = new StringBuilder(Prefix.INFO_ITEMSTORAGE + "아이템 사용 제한 태그 목록 : ");
				for (ReadWriteNBT restriction : restrictions)
				{
					String value = restriction.getString(CucumberyTag.VALUE_KEY);
					for (RestrictionType restType : Constant.RestrictionType.values())
					{
						if (restType.toString().equals(value))
						{
							boolean hide = NBTAPI.hideFromLore(restriction);
							msg.append("&c").append(hide ? "&n" : "").append((restType.getTag())).append("&r");
						}
					}
				}
				MessageUtil.sendMessage(sender, msg.toString());
			}
		}
		catch (Exception localException)
		{
			Cucumbery.getPlugin().getLogger().warning(localException.getMessage());
		}
	}
}
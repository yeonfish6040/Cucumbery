package com.jho5245.cucumbery.commands.itemtag;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect.DisplayType;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.MessageUtil.ConsonantType;
import com.jho5245.cucumbery.util.no_groups.MessageUtil.N2SType;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.Method2;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Constant.CucumberyHideFlag;
import com.jho5245.cucumbery.util.storage.data.Constant.ExtraTag;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.no_groups.ItemCategory.Rarity;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import de.tr7zw.changeme.nbtapi.*;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandItemTag implements CommandExecutor
{
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
	{
		if (!Method.hasPermission(sender, Permission.CMD_SETDATA, true))
		{
			return true;
		}
		if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
		{
			return !(sender instanceof BlockCommandSender);
		}
		if (!(sender instanceof Player))
		{
			MessageUtil.sendError(sender, Prefix.ONLY_PLAYER);
			return true;
		}
		String usage = cmd.getUsage().replace("/<command> ", "");
		if (!(args.length == 1 && args[0].equals("id")) && args.length < 2)
		{
			MessageUtil.shortArg(sender, 2, args);
			MessageUtil.commandInfo(sender, label, usage);
		}
		else
		{
			boolean hideOutput = false;
			if (MessageUtil.listToString(" ", args).endsWith(" --silent"))
			{
				String[] argsClone = new String[args.length - 1];
				System.arraycopy(args, 0, argsClone, 0, argsClone.length);
				args = argsClone;
				hideOutput = true;
			}
			Player player = (Player) sender;
			PlayerInventory playerInventory = player.getInventory();
			ItemStack item = playerInventory.getItemInMainHand();
			if (!ItemStackUtil.itemExists(item))
			{
				if (!hideOutput)
				{
					MessageUtil.sendError(sender, Prefix.NO_HOLDING_ITEM);
				}
				return true;
			}
			item = item.clone();
			Material material = item.getType();
			NBTItem nbtItem = new NBTItem(item);
			NBTCompound itemTag = nbtItem.getCompound(CucumberyTag.KEY_MAIN);
			switch (args[0])
			{
				case "id" ->
				{
					if (args.length > 1)
					{
						MessageUtil.longArg(sender, 1, args);
						MessageUtil.commandInfo(sender, label, "id");
						return true;
					}
					if (itemTag != null)
					{
						MessageUtil.info(sender, "%s의 CucumberyItemTags는 다음과 같습니다: %s", item, itemTag);
					}
					else
					{
						MessageUtil.sendError(sender, "%s에는 CucumberyItemTags가 없습니다", item);
					}
				}
				case "nbt" ->
				{
					if (args.length < 3)
					{
						MessageUtil.shortArg(sender, 3, args);
						MessageUtil.commandInfo(sender, label, "nbt <set|remove|merge> ...");
						return true;
					}
					switch (args[1])
					{
						case "set" ->
						{
							if (args.length < 5)
							{
								MessageUtil.shortArg(sender, 4, args);
								MessageUtil.commandInfo(sender, label, "nbt set <자료형> <키> <값>");
								return true;
							}
							String type = args[2];
							String key = args[3];
							switch (type)
							{
								case "boolean", "byte", "byte-array", "short", "int", "int-array", "int-list", "long", "long-list", "float", "float-list", "double", "double-list", "uuid" ->
								{
									if (args.length > 5)
									{
										MessageUtil.longArg(sender, 5, args);
										MessageUtil.commandInfo(sender, label, "nbt set " + type + " " + key + " <값>");
										return true;
									}
									String input = args[4];
									switch (type)
									{
										case "boolean":
											if (!MessageUtil.isBoolean(sender, args, 5, true))
											{
												return true;
											}
											boolean b = Boolean.parseBoolean(input);
											nbtItem.setBoolean(key, b);
											playerInventory.setItemInMainHand(nbtItem.getItem());
											ItemStackUtil.updateInventory(player);
											input += "&r" + MessageUtil.getFinalConsonant(input, ConsonantType.으로);
											MessageUtil.info(sender, "주로 사용하는 손에 들고 있는 아이템에서 rg255,204;" + key + "&r 태그의 rg255,204;" + type + "&r 값을 rg255,204;" + input + " 설정했습니다");
											break;
										case "byte":
											if (!MessageUtil.isInteger(sender, input, true))
											{
												return true;
											}
											int i = Integer.parseInt(input);
											if (!MessageUtil.checkNumberSize(sender, i, Byte.MIN_VALUE, Byte.MAX_VALUE))
											{
												return true;
											}
											nbtItem.setByte(key, (byte) i);
											playerInventory.setItemInMainHand(nbtItem.getItem());
											ItemStackUtil.updateInventory(player);
											input = i + "&r" + MessageUtil.getFinalConsonant(input, ConsonantType.으로);
											MessageUtil.info(sender, "주로 사용하는 손에 들고 있는 아이템에서 rg255,204;" + key + "&r 태그의 rg255,204;" + type + "&r 값을 rg255,204;" + input + " 설정했습니다");
											break;
										case "byte-array":
											String[] split = input.split(",");
											byte[] byteArray = new byte[split.length];
											for (int j = 0; j < split.length; j++)
											{
												String s = split[j];
												if (!MessageUtil.isInteger(sender, s, true))
												{
													return true;
												}
												int k = Integer.parseInt(s);
												if (!MessageUtil.checkNumberSize(sender, k, Byte.MIN_VALUE, Byte.MAX_VALUE))
												{
													return true;
												}
												byteArray[j] = (byte) k;
											}
											nbtItem.setByteArray(key, byteArray);
											playerInventory.setItemInMainHand(nbtItem.getItem());
											ItemStackUtil.updateInventory(player);
											input = Arrays.toString(byteArray) + "&r" + MessageUtil.getFinalConsonant(input, ConsonantType.으로);
											MessageUtil.info(sender, "주로 사용하는 손에 들고 있는 아이템에서 rg255,204;" + key + "&r 태그의 rg255,204;" + type + "&r 값을 rg255,204;" + input + " 설정했습니다");
											break;
										case "short":
											if (!MessageUtil.isInteger(sender, input, true))
											{
												return true;
											}
											i = Integer.parseInt(input);
											if (!MessageUtil.checkNumberSize(sender, i, Short.MIN_VALUE, Short.MAX_VALUE))
											{
												return true;
											}
											nbtItem.setShort(key, (short) i);
											playerInventory.setItemInMainHand(nbtItem.getItem());
											ItemStackUtil.updateInventory(player);
											input = i + "&r" + MessageUtil.getFinalConsonant(input, ConsonantType.으로);
											MessageUtil.info(sender, "주로 사용하는 손에 들고 있는 아이템에서 rg255,204;" + key + "&r 태그의 rg255,204;" + type + "&r 값을 rg255,204;" + input + " 설정했습니다");
											break;
										case "int":
											if (!MessageUtil.isInteger(sender, input, true))
											{
												return true;
											}
											i = Integer.parseInt(input);
											nbtItem.setInteger(key, i);
											playerInventory.setItemInMainHand(nbtItem.getItem());
											ItemStackUtil.updateInventory(player);
											input = i + "&r" + MessageUtil.getFinalConsonant(input, ConsonantType.으로);
											MessageUtil.info(sender, "주로 사용하는 손에 들고 있는 아이템에서 rg255,204;" + key + "&r 태그의 rg255,204;" + type + "&r 값을 rg255,204;" + input + " 설정했습니다");
											break;
										case "int-array":
											split = input.split(",");
											int[] intArray = new int[split.length];
											for (int j = 0; j < split.length; j++)
											{
												String s = split[j];
												if (!MessageUtil.isInteger(sender, s, true))
												{
													return true;
												}
												int k = Integer.parseInt(s);
												intArray[j] = k;
											}
											nbtItem.setIntArray(key, intArray);
											playerInventory.setItemInMainHand(nbtItem.getItem());
											ItemStackUtil.updateInventory(player);
											input = Arrays.toString(intArray) + "&r" + MessageUtil.getFinalConsonant(input, ConsonantType.으로);
											MessageUtil.info(sender, "주로 사용하는 손에 들고 있는 아이템에서 rg255,204;" + key + "&r 태그의 rg255,204;" + type + "&r 값을 rg255,204;" + input + " 설정했습니다");
											break;
										case "int-list":
											split = input.split(",");
											NBTList<Integer> nbtIntegerList = nbtItem.getIntegerList(key);
											nbtIntegerList.clear();
											for (String s : split)
											{
												if (!MessageUtil.isInteger(sender, s, true))
												{
													return true;
												}
												int k = Integer.parseInt(s);
												nbtIntegerList.add(k);
											}
											playerInventory.setItemInMainHand(nbtItem.getItem());
											ItemStackUtil.updateInventory(player);
											input = nbtIntegerList + "&r" + MessageUtil.getFinalConsonant(input, ConsonantType.으로);
											MessageUtil.info(sender, "주로 사용하는 손에 들고 있는 아이템에서 rg255,204;" + key + "&r 태그의 rg255,204;" + type + "&r 값을 rg255,204;" + input + " 설정했습니다");
											break;
										case "long":
											if (!MessageUtil.isLong(sender, input, true))
											{
												return true;
											}
											long l = Long.parseLong(input);
											nbtItem.setLong(key, l);
											playerInventory.setItemInMainHand(nbtItem.getItem());
											ItemStackUtil.updateInventory(player);
											input = l + "&r" + MessageUtil.getFinalConsonant(input, ConsonantType.으로);
											MessageUtil.info(sender, "주로 사용하는 손에 들고 있는 아이템에서 rg255,204;" + key + "&r 태그의 rg255,204;" + type + "&r 값을 rg255,204;" + input + " 설정했습니다");
											break;
										case "long-list":
											split = input.split(",");
											NBTList<Long> nbtLongList = nbtItem.getLongList(key);
											nbtLongList.clear();
											for (String s : split)
											{
												if (!MessageUtil.isLong(sender, s, true))
												{
													return true;
												}
												long k = Long.parseLong(s);
												nbtLongList.add(k);
											}
											playerInventory.setItemInMainHand(nbtItem.getItem());
											ItemStackUtil.updateInventory(player);
											input = nbtLongList + "&r" + MessageUtil.getFinalConsonant(input, ConsonantType.으로);
											MessageUtil.info(sender, "주로 사용하는 손에 들고 있는 아이템에서 rg255,204;" + key + "&r 태그의 rg255,204;" + type + "&r 값을 rg255,204;" + input + " 설정했습니다");
											break;
										case "float":
											if (!MessageUtil.isDouble(sender, input, true))
											{
												return true;
											}
											double d = Double.parseDouble(input);
											if (!MessageUtil.checkNumberSize(player, d, -Float.MAX_VALUE, Float.MAX_VALUE))
											{
												return true;
											}
											nbtItem.setFloat(key, (float) d);
											playerInventory.setItemInMainHand(nbtItem.getItem());
											ItemStackUtil.updateInventory(player);
											input = d + "&r" + MessageUtil.getFinalConsonant(input, ConsonantType.으로);
											MessageUtil.info(sender, "주로 사용하는 손에 들고 있는 아이템에서 rg255,204;" + key + "&r 태그의 rg255,204;" + type + "&r 값을 rg255,204;" + input + " 설정했습니다");
											break;
										case "float-list":
											split = input.split(",");
											NBTList<Float> nbtFloatList = nbtItem.getFloatList(key);
											nbtFloatList.clear();
											for (String s : split)
											{
												if (!MessageUtil.isDouble(sender, s, true))
												{
													return true;
												}
												double k = Double.parseDouble(s);
												if (!MessageUtil.checkNumberSize(sender, k, -Float.MAX_VALUE, Float.MAX_VALUE))
												{
													return true;
												}
												nbtFloatList.add((float) k);
											}
											playerInventory.setItemInMainHand(nbtItem.getItem());
											ItemStackUtil.updateInventory(player);
											input = nbtFloatList + "&r" + MessageUtil.getFinalConsonant(input, ConsonantType.으로);
											MessageUtil.info(sender, "주로 사용하는 손에 들고 있는 아이템에서 rg255,204;" + key + "&r 태그의 rg255,204;" + type + "&r 값을 rg255,204;" + input + " 설정했습니다");
											break;
										case "double":
											if (!MessageUtil.isDouble(sender, input, true))
											{
												return true;
											}
											d = Double.parseDouble(input);
											nbtItem.setDouble(key, d);
											playerInventory.setItemInMainHand(nbtItem.getItem());
											ItemStackUtil.updateInventory(player);
											input = d + "&r" + MessageUtil.getFinalConsonant(input, ConsonantType.으로);
											MessageUtil.info(sender, "주로 사용하는 손에 들고 있는 아이템에서 rg255,204;" + key + "&r 태그의 rg255,204;" + type + "&r 값을 rg255,204;" + input + " 설정했습니다");
											break;
										case "double-list":
											split = input.split(",");
											NBTList<Double> nbtDoubleList = nbtItem.getDoubleList(key);
											nbtDoubleList.clear();
											for (String s : split)
											{
												if (!MessageUtil.isDouble(sender, s, true))
												{
													return true;
												}
												double k = Double.parseDouble(s);
												nbtDoubleList.add(k);
											}
											playerInventory.setItemInMainHand(nbtItem.getItem());
											ItemStackUtil.updateInventory(player);
											input = nbtDoubleList + "&r" + MessageUtil.getFinalConsonant(input, ConsonantType.으로);
											MessageUtil.info(sender, "주로 사용하는 손에 들고 있는 아이템에서 rg255,204;" + key + "&r 태그의 rg255,204;" + type + "&r 값을 rg255,204;" + input + " 설정했습니다");
											break;
										case "uuid":
											if (!Method.isUUID(input))
											{
												MessageUtil.noArg(sender, Prefix.NO_UUID, input);
												return true;
											}
											UUID uuid = UUID.fromString(input);
											nbtItem.setUUID(key, uuid);
											playerInventory.setItemInMainHand(nbtItem.getItem());
											ItemStackUtil.updateInventory(player);
											input = uuid + "&r" + MessageUtil.getFinalConsonant(input, ConsonantType.으로);
											MessageUtil.info(sender, "주로 사용하는 손에 들고 있는 아이템에서 rg255,204;" + key + "&r 태그의 rg255,204;" + type + "&r 값을 rg255,204;" + input + " 설정했습니다");
											break;
									}
								}
								case "string" ->
								{
									String input = MessageUtil.listToString(" ", 4, args.length, args);
									nbtItem.setString(key, input);
									playerInventory.setItemInMainHand(nbtItem.getItem());
									ItemStackUtil.updateInventory(player);
									input += "&r" + MessageUtil.getFinalConsonant(input, ConsonantType.으로);
									MessageUtil.info(sender, "주로 사용하는 손에 들고 있는 아이템에서 rg255,204;" + key + "&r 태그의 rg255,204;" + type + "&r 값을 rg255,204;" + input + " 설정했습니다");
								}
								case "string-list" ->
								{
									String input = MessageUtil.listToString(" ", 4, args.length, args);
									String[] split = input.split(";;");
									NBTList<String> nbtStringList = nbtItem.getStringList(key);
									nbtStringList.clear();
									nbtStringList.addAll(Arrays.asList(split));
									playerInventory.setItemInMainHand(nbtItem.getItem());
									ItemStackUtil.updateInventory(player);
									input = nbtStringList + "&r" + MessageUtil.getFinalConsonant(input, ConsonantType.으로);
									MessageUtil.info(sender, "주로 사용하는 손에 들고 있는 아이템에서 rg255,204;" + key + "&r 태그의 rg255,204;" + type + "&r 값을 rg255,204;" + input + " 설정했습니다");
								}
								case "compound" ->
								{
									String input = MessageUtil.listToString(" ", 4, args.length, args);
									try
									{
										nbtItem.removeKey(key);
										NBTContainer nbtContainer = new NBTContainer(input);
										NBTCompound nbtCompound = nbtItem.addCompound(key);
										nbtCompound.mergeCompound(nbtContainer);
										playerInventory.setItemInMainHand(nbtItem.getItem());
										ItemStackUtil.updateInventory(player);
										input = nbtContainer + "&r" + MessageUtil.getFinalConsonant(input, ConsonantType.으로);
										MessageUtil.info(sender, "주로 사용하는 손에 들고 있는 아이템에서 rg255,204;" + key + "&r 태그의 rg255,204;" + type + "&r 값을 rg255,204;" + input + " 설정했습니다");
									}
									catch (Exception e)
									{
										MessageUtil.sendError(sender,
												Constant.THE_COLOR_HEX + input + "&r" + MessageUtil.getFinalConsonant(input, ConsonantType.은는) + " 잘못된 nbt입니다");
										return true;
									}
								}
								case "compound-list" ->
								{
									String input = MessageUtil.listToString(" ", 4, args.length, args);
									String[] split = input.split(";;");
									NBTCompoundList nbtCompoundList = nbtItem.getCompoundList(key);
									nbtCompoundList.clear();
									for (String nbtString : split)
									{
										try
										{
											NBTContainer nbtContainer = new NBTContainer(nbtString);
											nbtCompoundList.addCompound(nbtContainer);
										}
										catch (Exception e)
										{
											MessageUtil.sendError(sender,
													Constant.THE_COLOR_HEX + nbtString + "&r" + MessageUtil.getFinalConsonant(nbtString, ConsonantType.은는) + " 잘못된 nbt입니다");
											return true;
										}
									}
									playerInventory.setItemInMainHand(nbtItem.getItem());
									ItemStackUtil.updateInventory(player);
									input = nbtCompoundList + "&r" + MessageUtil.getFinalConsonant(input, ConsonantType.으로);
									MessageUtil.info(sender, "주로 사용하는 손에 들고 있는 아이템에서 rg255,204;" + key + "&r 태그의 rg255,204;" + type + "&r 값을 rg255,204;" + input + " 설정했습니다");
								}
								default ->
								{
									MessageUtil.wrongArg(sender, 3, args);
									MessageUtil.commandInfo(sender, label, "nbt set <자료형> <키> <값>");
									return true;
								}
							}
						}
						case "remove" ->
						{
							String key = args[2];
							if (key.contains("."))
							{
								boolean success = false;
								String[] split = key.split("\\.");
								NBTCompound nbtCompound = nbtItem.getCompound(split[0]);
								for (int i = 1; i < split.length; i++)
								{
									if (i == split.length - 1 && nbtCompound != null)
									{
										NBTAPI.removeKey(nbtCompound, split[i]);
										success = true;
										break;
									}
									if (nbtCompound == null)
									{
										break;
									}
									try
									{
										if (i == split.length - 1 && nbtCompound.hasKey(split[i]))
										{
											nbtCompound.removeKey(split[i]);
											success = true;
											break;
										}
										nbtCompound = nbtCompound.getCompound(split[i]);
									}
									catch (Exception e)
									{
										Cucumbery.getPlugin().getLogger().warning(e.getMessage());
									}
								}

								if (success)
								{
									playerInventory.setItemInMainHand(nbtItem.getItem());
									ItemStackUtil.updateInventory(player);
									if (!hideOutput)
									{
										MessageUtil.info(sender, "주로 사용하는 손에 들고 있는 아이템에서 rg255,204;" + key + "&r 태그를 제거했습니다");
									}
									return true;
								}
							}
							if (!nbtItem.hasTag(key))
							{
								if (!hideOutput)
								{
									MessageUtil.sendError(sender, "주로 사용하는 손에 들고 있는 아이템에는 rg255,204;" + key + "&r 태그가 없습니다");
								}
								return true;
							}
							NBTAPI.removeKey(nbtItem, key);
							playerInventory.setItemInMainHand(nbtItem.getItem());
							ItemStackUtil.updateInventory(player);
							if (!hideOutput)
							{
								MessageUtil.info(sender, "주로 사용하는 손에 들고 있는 아이템에서 rg255,204;" + key + "&r 태그를 제거했습니다");
							}
						}
						case "merge" ->
						{
							String input = MessageUtil.listToString(" ", 2, args.length, args);
							try
							{
								NBTContainer nbtContainer = new NBTContainer("{" + input + "}");
								nbtItem.mergeCompound(nbtContainer);
								playerInventory.setItemInMainHand(nbtItem.getItem());
								ItemStackUtil.updateInventory(player);
								if (!hideOutput)
								{
									MessageUtil.info(sender, "주로 사용하는 손에 들고 있는 아이템에 %s 태그 값을 병합했습니다", Constant.THE_COLOR_HEX + input);
								}
							}
							catch (Exception e)
							{
								MessageUtil.sendError(sender, "%s은(는) 잘못된 nbt입니다", Constant.THE_COLOR_HEX + input);
								return true;
							}
						}
						default ->
						{
							MessageUtil.wrongArg(sender, 2, args);
							MessageUtil.commandInfo(sender, label, "nbt <set|remove|merge> ...");
							return true;
						}
					}
				}
				case "restriction" ->
				{
					switch (args[1])
					{
						case "add":
						case "remove":
						case "modify":
							break;
						default:
							MessageUtil.wrongArg(sender, 2, args);
							MessageUtil.commandInfo(sender, label, "restriction <add|remove|modify> ...");
							return true;
					}
					if (args.length < 3)
					{
						MessageUtil.shortArg(sender, 3, args);
						MessageUtil.commandInfo(sender, label, "restriction <add|remove|modify> ...");
						return true;
					}
					Constant.RestrictionType restrictType;
					try
					{
						restrictType = Constant.RestrictionType.valueOf(args[2].toUpperCase());
					}
					catch (Exception e)
					{
						MessageUtil.wrongArg(sender, 3, args);
						MessageUtil.commandInfo(sender, label,
								" " + args[1] + " <태그>" + (args[1].equals("add") ? " [태그 설명 숨김 여부]" : "") + (args[1].equals("modify") ? " <태그 설명 숨김 여부>" : ""));
						return true;
					}
					NBTCompoundList restrictionTags = NBTAPI.getCompoundList(itemTag, CucumberyTag.ITEM_USAGE_RESTRICTIONS_KEY);
					switch (args[1])
					{
						case "add" ->
						{
							if (args.length > 5)
							{
								MessageUtil.longArg(sender, 4, args);
								MessageUtil.commandInfo(sender, label, "restriction add <태그> [태그 설명 숨김 여부] [우회 퍼미션 노드]");
								return true;
							}
							if (itemTag == null)
							{
								itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
							}
							if (restrictionTags == null)
							{
								restrictionTags = itemTag.getCompoundList(CucumberyTag.ITEM_USAGE_RESTRICTIONS_KEY);
							}
							for (ReadWriteNBT restrictionTag : restrictionTags)
							{
								String value = restrictionTag.getString(CucumberyTag.VALUE_KEY);
								if (restrictType.toString().equals(value))
								{
									MessageUtil.sendError(player,
											"이미 해당 태그(rg255,204;" + restrictType.toString() + "&r(rg255,204;" + restrictType.getRawTag() + "&r))가 존재하여 추가할 수 없습니다");
									return true;
								}
							}
							NBTListCompound restrictionTag = restrictionTags.addCompound();
							restrictionTag.setString(CucumberyTag.VALUE_KEY, restrictType.toString());
							restrictionTag.setBoolean(CucumberyTag.ITEM_USAGE_RESTRICTIONS_HIDE_FROM_LORE_KEY, args.length >= 4 && args[3].equals("true"));
							if (args.length == 5)
							{
								if (args[4].equals("--remove"))
								{
									MessageUtil.sendError(player, "해당 퍼미션 노드는 사용할 수 없습니다");
									return true;
								}
								restrictionTag.setString(CucumberyTag.PERMISSION_KEY, args[4]);
							}
							playerInventory.setItemInMainHand(nbtItem.getItem());
							if (!hideOutput)
							{
								MessageUtil.sendMessage(player, Prefix.INFO_SETDATA,
										"주로 사용하는 손에 들고 있는 아이템에 rg255,204;" + restrictType.toString() + "&r(rg255,204;" + restrictType.getRawTag() + "&r) 태그 값을 추가했습니다");
							}
							ItemStackUtil.updateInventory(player);
						}
						case "modify" ->
						{
							if (itemTag == null || restrictionTags == null)
							{
								MessageUtil.sendError(sender, "수정할 태그 값이 없습니다");
								return true;
							}
							if (args.length < 5)
							{
								MessageUtil.shortArg(sender, 4, args);
								MessageUtil.commandInfo(sender, label, "restriction modify <태그> <hide|permission> ...");
								return true;
							}
							if (args.length > 5)
							{
								MessageUtil.longArg(sender, 4, args);
								MessageUtil.commandInfo(sender, label, "restriction modify <태그> <hide|permission> ...");
								return true;
							}
							switch (args[3])
							{
								case "hide" ->
								{
									if (!MessageUtil.isBoolean(sender, args, 5, true))
									{
										return true;
									}
									boolean bool = args[4].equals("true");
									boolean contains = false;
									for (ReadWriteNBT tag : restrictionTags)
									{
										String value = tag.getString(CucumberyTag.VALUE_KEY);
										if (restrictType.toString().equals(value))
										{
											tag.setBoolean(CucumberyTag.ITEM_USAGE_RESTRICTIONS_HIDE_FROM_LORE_KEY, bool);
											contains = true;
											break;
										}
									}
									if (contains)
									{
										playerInventory.setItemInMainHand(nbtItem.getItem());
										if (!hideOutput)
										{
											MessageUtil.sendMessage(player, Prefix.INFO_SETDATA,
													"주로 사용하는 손에 들고 있는 아이템의 rg255,204;" + restrictType.toString() + "&r(rg255,204;" + restrictType.getRawTag()
															+ "&r) 태그의 HideFromLore 태그 값을 rg255,204;" + bool + "&r으로 수정했습니다");
										}
										ItemStackUtil.updateInventory(player);
									}
									else
									{
										MessageUtil.sendError(player,
												"입력한 태그(rg255,204;" + restrictType.toString() + "&r(rg255,204;" + restrictType.getRawTag() + "&r))가 주로 사용하는 손에 들고 있는 아이템에 존재하지 않습니다");
									}
								}
								case "permission" ->
								{
									String permission = args[4];
									boolean contains = false;
									for (ReadWriteNBT tag : restrictionTags)
									{
										String value = tag.getString(CucumberyTag.VALUE_KEY);
										if (restrictType.toString().equals(value))
										{
											contains = true;
											if (permission.equals("--remove"))
											{
												if (!tag.hasTag(CucumberyTag.PERMISSION_KEY))
												{
													MessageUtil.sendError(player, "입력한 태그(rg255,204;" + restrictType.toString() + "&r(rg255,204;" + restrictType.getRawTag()
															+ "&r))의 퍼미션 노드 값이 주로 사용하는 손에 들고 있는 아이템에 존재하지 않습니다");
													return true;
												}
												tag.removeKey(CucumberyTag.PERMISSION_KEY);
											}
											else
											{
												tag.setString(CucumberyTag.PERMISSION_KEY, permission);
											}
											break;
										}
									}
									if (contains)
									{
										playerInventory.setItemInMainHand(nbtItem.getItem());
										if (!hideOutput)
										{
											MessageUtil.sendMessage(player,
													Prefix.INFO_SETDATA + "주로 사용하는 손에 들고 있는 아이템의 rg255,204;" + restrictType.toString() + "&r(rg255,204;" + restrictType.getRawTag()
															+ "&r) 태그의 Permission 태그 값을 rg255,204;" + (permission.equals("--remove") ? ("&r삭제했습니다") : (permission + "&r으로 수정했습니다")));
										}
										ItemStackUtil.updateInventory(player);
									}
									else
									{
										MessageUtil.sendError(player,
												"입력한 태그(rg255,204;" + restrictType.toString() + "&r(rg255,204;" + restrictType.getRawTag() + "&r))가 주로 사용하는 손에 들고 있는 아이템에 존재하지 않습니다");
									}
								}
							}
						}
						case "remove" ->
						{
							if (args.length > 3)
							{
								MessageUtil.longArg(sender, 3, args);
								MessageUtil.commandInfo(sender, label, "restriction remove <태그>");
								return true;
							}
							if (itemTag == null || restrictionTags == null)
							{
								MessageUtil.sendError(sender, "제거할 태그 값이 없습니다");
								return true;
							}
							boolean contains = false;
							for (int i = 0; i < restrictionTags.size(); i++)
							{
								NBTCompound restrictionTag2 = restrictionTags.get(i);
								String value = restrictionTag2.getString(CucumberyTag.VALUE_KEY);
								if (restrictType.toString().equals(value))
								{
									restrictionTags.remove(i);
									contains = true;
									break;
								}
							}
							if (contains)
							{
								if (restrictionTags.size() == 0)
								{
									NBTAPI.removeKey(itemTag, CucumberyTag.ITEM_USAGE_RESTRICTIONS_KEY);
								}
								playerInventory.setItemInMainHand(nbtItem.getItem());
								if (!hideOutput)
								{
									MessageUtil.sendMessage(player, Prefix.INFO_SETDATA,
											"주로 사용하는 손에 들고 있는 아이템에서 rg255,204;" + restrictType.toString() + "&r(rg255,204;" + restrictType.getRawTag() + "&r) 태그 값을 제거했습니다");
								}
								ItemStackUtil.updateInventory(player);
							}
							else
							{
								MessageUtil.sendError(player,
										"입력한 태그(rg255,204;" + restrictType.toString() + "&r(rg255,204;" + restrictType.getRawTag() + "&r))가 주로 사용하는 손에 들고 있는 아이템에 존재하지 않습니다");
							}
						}
						default ->
						{
							MessageUtil.wrongArg(sender, 1, args);
							MessageUtil.commandInfo(sender, label, "restriction <add|remove|modify> <태그>");
							return true;
						}
					}
				}
				case "customlore" ->
				{
					NBTList<String> customLoresTag = NBTAPI.getStringList(itemTag, CucumberyTag.CUSTOM_LORE_KEY);
					switch (args[1])
					{
						case "list":
							if (args.length > 2)
							{
								MessageUtil.longArg(player, 2, args);
								MessageUtil.commandInfo(sender, label, "customlore list");
								return true;
							}
							if (customLoresTag == null || customLoresTag.size() == 0)
							{
								MessageUtil.sendError(player, "주로 사용하는 손에 들고 있는 아이템에는 커스텀 설명이 없습니다");
								return true;
							}
							MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템의 커스텀 설명의 개수는 rg255,204;" + customLoresTag.size() + "개&r입니다");
							for (int i = 0; i < customLoresTag.size(); i++)
							{
								MessageUtil.sendMessage(player, false, Prefix.INFO_SETDATA,
										ComponentUtil.create("§e" + (i + 1) + "번째§r 커스텀 설명 : " + customLoresTag.get(i), "클릭하여 §e" + (i + 1) + "번째§r 커스텀 설명을 수정합니다",
												ClickEvent.Action.SUGGEST_COMMAND, "/" + label + " " + args[0] + " set " + (i + 1) + " " + customLoresTag.get(i).replace("§", "&")));
							}
							break;
						case "add":
							if (args.length < 3)
							{
								MessageUtil.shortArg(sender, 3, args);
								MessageUtil.commandInfo(sender, label, "customlore add <설명|--empty>");
								return true;
							}
							boolean isEmptyLore = args.length == 3 && args[2].equals("--empty");
							String inputLore = (isEmptyLore ? "" : MessageUtil.listToString(" ", 2, args.length, args));
							if (itemTag == null)
							{
								itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
							}
							if (customLoresTag == null)
							{
								customLoresTag = itemTag.getStringList(CucumberyTag.CUSTOM_LORE_KEY);
							}
							customLoresTag.add(inputLore);

							playerInventory.setItemInMainHand(nbtItem.getItem());
							if (!hideOutput)
							{
								MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템에 rg255,204;" + customLoresTag.size() + "번째&r 커스텀 설명의 줄에 " + (
										isEmptyLore
												? "공백"
												: (Constant.THE_COLOR_HEX + inputLore + "&r" + MessageUtil.getFinalConsonant(MessageUtil.stripColor(inputLore), ConsonantType.이라)
														+ "는")) + " 설명을 추가했습니다");
							}
							ItemStackUtil.updateInventory(player);
							break;
						case "remove":
							if (args.length > 3)
							{
								MessageUtil.longArg(sender, 3, args);
								MessageUtil.commandInfo(sender, label, "customlore remove [줄|--all]");
								return true;
							}
							if (customLoresTag == null || customLoresTag.size() == 0)
							{
								MessageUtil.sendError(sender, "제거할 커스텀 설명이 없습니다");
								return true;
							}
							boolean contains = false;
							int line = customLoresTag.size() - 1;
							boolean clearAll = false;
							if (args.length == 3)
							{
								if (args[2].equals("--all"))
								{
									clearAll = true;
									contains = true;
								}
								else
								{
									try
									{
										line = Integer.parseInt(args[2]);
									}
									catch (Exception e)
									{
										MessageUtil.wrongArg(sender, 3, args);
										return true;
									}
									if (!MessageUtil.checkNumberSize(player, line, 1, customLoresTag.size()))
									{
										return true;
									}

									line--; // 숫자를 3이라고 입력하면 get(2) 를 해야하므로 1을 뺌
								}
							}
							if (!clearAll)
							{
								for (int i = 0; i < customLoresTag.size(); i++)
								{
									if (line == i)
									{
										customLoresTag.remove(i);
										contains = true;
										break;
									}
								}
							}
							if (contains)
							{
								if (clearAll || customLoresTag.size() == 0)
								{
									NBTAPI.removeKey(itemTag, CucumberyTag.CUSTOM_LORE_KEY);
								}
								playerInventory.setItemInMainHand(nbtItem.getItem());
								if (!hideOutput)
								{
									if (clearAll)
									{
										MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템의 모든 커스텀 설명을 제거했습니다");
									}
									else
									{
										MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템의 rg255,204;" + (line + 1) + "번째 줄&r의 커스텀 설명을 제거했습니다");
									}
								}
								ItemStackUtil.updateInventory(player);
							}
							else
							{
								MessageUtil.sendError(player, "주로 사용하는 손에 들고 있는 아이템에 rg255,204;" + (line + 1) + "번째 줄&r의 커스텀 설명이 존재하지 않습니다");
								return true;
							}
							break;
						case "insert":
							if (args.length < 4)
							{
								MessageUtil.shortArg(sender, 4, args);
								MessageUtil.commandInfo(sender, label, "customlore insert <들여쓸 줄> <설명|--empty>");
								return true;
							}
							if (!MessageUtil.isInteger(sender, args[2], true))
							{
								return true;
							}

							isEmptyLore = args.length == 4 && args[3].equals("--empty");
							inputLore = (isEmptyLore ? "" : MessageUtil.listToString(" ", 3, args.length, args));
							inputLore = MessageUtil.n2s(inputLore, N2SType.SPECIAL);
							if (customLoresTag == null || customLoresTag.size() == 0)
							{
								MessageUtil.sendError(player, "커스텀 설명을 들여쓸 수 없습니다");
								return true;
							}
							int inputLoreLine = Integer.parseInt(args[2]);
							if (!MessageUtil.checkNumberSize(sender, inputLoreLine, 1, customLoresTag.size()))
							{
								return true;
							}
							List<String> tempLore = new ArrayList<>();
							for (int i = 0; i < inputLoreLine - 1; i++)
							{
								tempLore.add(customLoresTag.get(i));
							}
							tempLore.add(inputLore);
							for (int i = inputLoreLine - 1; i < customLoresTag.size(); i++)
							{
								tempLore.add(customLoresTag.get(i));
							}
							customLoresTag.clear();
							customLoresTag.addAll(tempLore);
							playerInventory.setItemInMainHand(nbtItem.getItem());
							if (!hideOutput)
							{
								MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템에 rg255,204;" + inputLoreLine + "번째&r 커스텀 설명의 줄에 " + (isEmptyLore
										? "공백"
										: (Constant.THE_COLOR_HEX + inputLore + "&r" + MessageUtil.getFinalConsonant(MessageUtil.stripColor(inputLore), ConsonantType.이라) + "는"))
										+ " 설명을 들여썼습니다");
							}
							ItemStackUtil.updateInventory(player);
							break;
						case "set":
							if (args.length < 4)
							{
								MessageUtil.shortArg(sender, 4, args);
								MessageUtil.commandInfo(sender, label, "customlore set <줄> <설명|--empty>");
								return true;
							}
							if (!MessageUtil.isInteger(sender, args[2], true))
							{
								return true;
							}
							inputLoreLine = Integer.parseInt(args[2]);
							if (!MessageUtil.checkNumberSize(sender, inputLoreLine, 1, Integer.MAX_VALUE))
							{
								return true;
							}
							isEmptyLore = args.length == 4 && args[3].equals("--empty");
							inputLore = (isEmptyLore ? "" : MessageUtil.listToString(" ", 3, args.length, args));
							inputLore = MessageUtil.n2s(inputLore, N2SType.SPECIAL);
							//							inputLore = Method.jsonParse(inputLore, true);
							if (itemTag == null)
							{
								itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
							}
							if (customLoresTag == null)
							{
								customLoresTag = itemTag.getStringList(CucumberyTag.CUSTOM_LORE_KEY);
							}
							int customLoreSize = customLoresTag.size();
							for (int i = 0; i < inputLoreLine - customLoreSize; i++)
							{
								customLoresTag.add("");
							}
							customLoresTag.set(inputLoreLine - 1, inputLore);

							playerInventory.setItemInMainHand(nbtItem.getItem());
							if (!hideOutput)
							{
								MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템에 rg255,204;" + inputLoreLine + "번째&r 커스텀 설명의 줄에 " + (isEmptyLore
										? "공백"
										: (Constant.THE_COLOR_HEX + inputLore + "&r" + MessageUtil.getFinalConsonant(MessageUtil.stripColor(inputLore), ConsonantType.이라) + "는"))
										+ " 설명을 설정했습니다");
							}
							ItemStackUtil.updateInventory(player);
							break;
						default:
							MessageUtil.wrongArg(sender, 2, args);
							MessageUtil.commandInfo(sender, label, "customlore <add|remove|set|insert|list> ...");
							return true;
					}
				}
				case "abovecustomlore" ->
				{
					NBTList<String> aboveCustomLoresTag = NBTAPI.getStringList(itemTag, CucumberyTag.ABOVE_CUSTOM_LORE_KEY);
					switch (args[1])
					{
						case "list":
							if (args.length > 2)
							{
								MessageUtil.longArg(player, 2, args);
								MessageUtil.commandInfo(sender, label, "abovecustomlore list");
								return true;
							}
							if (aboveCustomLoresTag == null || aboveCustomLoresTag.size() == 0)
							{
								MessageUtil.sendError(player, "주로 사용하는 손에 들고 있는 아이템에는 상단 커스텀 설명이 없습니다");
								return true;
							}
							MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템의 상단 커스텀 설명의 개수는 rg255,204;" + aboveCustomLoresTag.size() + "개&r입니다");
							for (int i = 0; i < aboveCustomLoresTag.size(); i++)
							{
								MessageUtil.sendMessage(player, false, Prefix.INFO_SETDATA,
										ComponentUtil.create("§e" + (i + 1) + "번째§r 상단 커스텀 설명 : " + aboveCustomLoresTag.get(i), "클릭하여 §e" + (i + 1) + "번째§r 상단 커스텀 설명을 수정합니다",
												ClickEvent.Action.SUGGEST_COMMAND,
												"/" + label + " " + args[0] + " set " + (i + 1) + " " + aboveCustomLoresTag.get(i).replace("§", "&")));
							}
							break;
						case "add":
							if (args.length < 3)
							{
								MessageUtil.shortArg(sender, 3, args);
								MessageUtil.commandInfo(sender, label, "abovecustomlore add <설명|--empty>");
								return true;
							}
							boolean isEmptyLore = args.length == 3 && args[2].equals("--empty");
							String inputLore = (isEmptyLore ? "" : MessageUtil.listToString(" ", 2, args.length, args));
							inputLore = MessageUtil.n2s(inputLore, N2SType.SPECIAL);
							//							String inputLore = Method.jsonParse(inputLore, true);
							if (itemTag == null)
							{
								itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
							}
							if (aboveCustomLoresTag == null)
							{
								aboveCustomLoresTag = itemTag.getStringList(CucumberyTag.ABOVE_CUSTOM_LORE_KEY);
							}
							aboveCustomLoresTag.add(inputLore);

							playerInventory.setItemInMainHand(nbtItem.getItem());
							if (!hideOutput)
							{
								MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템에 rg255,204;" + aboveCustomLoresTag.size() + "번째&r 상단 커스텀 설명의 줄에 " + (
										isEmptyLore
												? "공백"
												: (Constant.THE_COLOR_HEX + inputLore + "&r" + MessageUtil.getFinalConsonant(MessageUtil.stripColor(inputLore), ConsonantType.이라)
														+ "는")) + " 설명을 추가했습니다");
							}
							ItemStackUtil.updateInventory(player);
							break;
						case "remove":
							if (args.length > 3)
							{
								MessageUtil.longArg(sender, 3, args);
								MessageUtil.commandInfo(sender, label, "abovecustomlore remove [줄|--all]");
								return true;
							}
							if (aboveCustomLoresTag == null || aboveCustomLoresTag.size() == 0)
							{
								MessageUtil.sendError(sender, "제거할 상단 커스텀 설명이 없습니다");
								return true;
							}
							boolean contains = false;
							int line = aboveCustomLoresTag.size() - 1;
							boolean clearAll = false;
							if (args.length == 3)
							{
								if (args[2].equals("--all"))
								{
									clearAll = true;
									contains = true;
								}
								else
								{
									try
									{
										line = Integer.parseInt(args[2]);
									}
									catch (Exception e)
									{
										MessageUtil.wrongArg(sender, 3, args);
										return true;
									}
									if (!MessageUtil.checkNumberSize(player, line, 1, aboveCustomLoresTag.size()))
									{
										return true;
									}

									line--; // 숫자를 3이라고 입력하면 get(2) 를 해야하므로 1을 뺌
								}
							}
							if (!clearAll)
							{
								for (int i = 0; i < aboveCustomLoresTag.size(); i++)
								{
									if (line == i)
									{
										aboveCustomLoresTag.remove(i);
										contains = true;
										break;
									}
								}
							}
							if (contains)
							{
								if (clearAll || aboveCustomLoresTag.size() == 0)
								{
									NBTAPI.removeKey(itemTag, CucumberyTag.ABOVE_CUSTOM_LORE_KEY);
								}
								playerInventory.setItemInMainHand(nbtItem.getItem());
								if (!hideOutput)
								{
									if (clearAll)
									{
										MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템의 모든 상단 커스텀 설명을 제거했습니다");
									}
									else
									{
										MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템의 rg255,204;" + (line + 1) + "번째 줄&r의 상단 커스텀 설명을 제거했습니다");
									}
								}
								ItemStackUtil.updateInventory(player);
							}
							else
							{
								MessageUtil.sendError(player, "주로 사용하는 손에 들고 있는 아이템에 rg255,204;" + (line + 1) + "번째 줄&r의 상단 커스텀 설명이 존재하지 않습니다");
								return true;
							}
							break;
						case "insert":
							if (args.length < 4)
							{
								MessageUtil.shortArg(sender, 4, args);
								MessageUtil.commandInfo(sender, label, "abovecustomlore insert <들여쓸 줄> <설명|--empty>");
								return true;
							}
							if (!MessageUtil.isInteger(sender, args[2], true))
							{
								return true;
							}

							isEmptyLore = args.length == 4 && args[3].equals("--empty");
							inputLore = (isEmptyLore ? "" : MessageUtil.listToString(" ", 3, args.length, args));
							inputLore = MessageUtil.n2s(inputLore, N2SType.SPECIAL);
							if (itemTag == null)
							{
								nbtItem.addCompound(CucumberyTag.KEY_MAIN);
							}
							if (aboveCustomLoresTag == null || aboveCustomLoresTag.size() == 0)
							{
								MessageUtil.sendError(player, "커스텀 설명을 들여쓸 수 없습니다");
								return true;
							}
							int inputLoreLine = Integer.parseInt(args[2]);
							if (!MessageUtil.checkNumberSize(sender, inputLoreLine, 1, aboveCustomLoresTag.size()))
							{
								return true;
							}
							List<String> tempLore = new ArrayList<>();
							for (int i = 0; i < inputLoreLine - 1; i++)
							{
								tempLore.add(aboveCustomLoresTag.get(i));
							}
							tempLore.add(inputLore);
							for (int i = inputLoreLine - 1; i < aboveCustomLoresTag.size(); i++)
							{
								tempLore.add(aboveCustomLoresTag.get(i));
							}
							aboveCustomLoresTag.clear();
							aboveCustomLoresTag.addAll(tempLore);
							playerInventory.setItemInMainHand(nbtItem.getItem());
							if (!hideOutput)
							{
								MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템에 rg255,204;" + inputLoreLine + "번째&r 상단 커스텀 설명의 줄에 " + (isEmptyLore
										? "공백"
										: (Constant.THE_COLOR_HEX + inputLore + "&r" + MessageUtil.getFinalConsonant(MessageUtil.stripColor(inputLore), ConsonantType.이라) + "는"))
										+ " 설명을 들여썼습니다");
							}
							ItemStackUtil.updateInventory(player);
							break;
						case "set":
							if (args.length < 4)
							{
								MessageUtil.shortArg(sender, 4, args);
								MessageUtil.commandInfo(sender, label, "abovecustomlore set <줄> <설명|--empty>");
								return true;
							}
							if (!MessageUtil.isInteger(sender, args[2], true))
							{
								return true;
							}
							inputLoreLine = Integer.parseInt(args[2]);
							if (!MessageUtil.checkNumberSize(sender, inputLoreLine, 1, Integer.MAX_VALUE))
							{
								return true;
							}
							isEmptyLore = args.length == 4 && args[3].equals("--empty");
							inputLore = (isEmptyLore ? "" : MessageUtil.listToString(" ", 3, args.length, args));
							inputLore = MessageUtil.n2s(inputLore, N2SType.SPECIAL);
							//							inputLore = Method.jsonParse(inputLore, true);
							if (itemTag == null)
							{
								itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
							}
							if (aboveCustomLoresTag == null)
							{
								aboveCustomLoresTag = itemTag.getStringList(CucumberyTag.ABOVE_CUSTOM_LORE_KEY);
							}
							int customLoreSize = aboveCustomLoresTag.size();
							for (int i = 0; i < inputLoreLine - customLoreSize; i++)
							{
								aboveCustomLoresTag.add("");
							}
							aboveCustomLoresTag.set(inputLoreLine - 1, inputLore);
							playerInventory.setItemInMainHand(nbtItem.getItem());
							if (!hideOutput)
							{
								MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템에 rg255,204;" + inputLoreLine + "번째&r 상단 커스텀 설명의 줄에 " + (isEmptyLore
										? "공백"
										: (Constant.THE_COLOR_HEX + inputLore + "&r" + MessageUtil.getFinalConsonant(MessageUtil.stripColor(inputLore), ConsonantType.이라) + "는"))
										+ " 설명을 설정했습니다");
							}
							ItemStackUtil.updateInventory(player);
							break;
						default:
							MessageUtil.wrongArg(sender, 2, args);
							MessageUtil.commandInfo(sender, label, "abovecustomlore <add|remove|set|insert|list> ...");
							return true;
					}
				}
				case "extratag" ->
				{
					if (args.length < 3)
					{
						MessageUtil.shortArg(sender, 3, args);
						MessageUtil.commandInfo(sender, label, "extratag <add|remove> <태그>");
						return true;
					}
					if (args.length > 3)
					{
						MessageUtil.longArg(sender, 3, args);
						MessageUtil.commandInfo(sender, label, "extratag <add|remove> <태그>");
						return true;
					}
					NBTList<String> extraTag = NBTAPI.getStringList(itemTag, CucumberyTag.EXTRA_TAGS_KEY);
					ExtraTag extraTagType;
					switch (args[1])
					{
						case "add":
							try
							{
								extraTagType = ExtraTag.valueOf(args[2].toUpperCase());
							}
							catch (Exception e)
							{
								MessageUtil.wrongArg(sender, 3, args);
								MessageUtil.commandInfo(sender, label, "extratag add <태그>");
								return true;
							}
							if (extraTagType == ExtraTag.PORTABLE_SHULKER_BOX && !Constant.SHULKER_BOXES.contains(material))
							{
								MessageUtil.sendError(sender, "해당 태그는 셜커 상자에만 사용할 수 있습니다");
								return true;
							}
							if (extraTagType == ExtraTag.NO_COOLDOWN_ENDER_PEARL && material != Material.ENDER_PEARL)
							{
								MessageUtil.sendError(sender, "해당 태그는 엔더 진주에만 사용할 수 있습니다");
								return true;
							}
							if (itemTag == null)
							{
								itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
							}
							if (extraTag == null)
							{
								extraTag = itemTag.getStringList(CucumberyTag.EXTRA_TAGS_KEY);
							}
							else if (NBTAPI.arrayContainsValue(extraTag, extraTagType))
							{
								MessageUtil.sendError(sender,
										"이미 주로 사용하는 손에 들고 있는 " + (extraTagType == ExtraTag.PORTABLE_SHULKER_BOX ? "셜커 상자" : "아이템") + "에 " + extraTagType.getDisplay()
												+ " 태그가 있습니다");
								return true;
							}
							extraTag.add(extraTagType.toString());
							playerInventory.setItemInMainHand(nbtItem.getItem());
							if (!hideOutput)
							{
								MessageUtil.sendMessage(player, Prefix.INFO_SETDATA,
										"주로 사용하는 손에 들고 있는 " + (extraTagType == ExtraTag.PORTABLE_SHULKER_BOX ? "셜커 상자" : "아이템") + "에 " + extraTagType.getDisplay()
												+ " 태그 값을 추가했습니다");
							}
							ItemStackUtil.updateInventory(player);
							break;
						case "remove":
							try
							{
								extraTagType = ExtraTag.valueOf(args[2].toUpperCase());
							}
							catch (Exception e)
							{
								MessageUtil.wrongArg(sender, 3, args);
								MessageUtil.commandInfo(sender, label, "extratag add <태그>");
								return true;
							}
							if (extraTagType == ExtraTag.PORTABLE_SHULKER_BOX && !Constant.SHULKER_BOXES.contains(material))
							{
								MessageUtil.sendError(sender, "해당 태그는 셜커 상자만 사용할 수 있습니다");
								return true;
							}
							if (extraTagType == ExtraTag.NO_COOLDOWN_ENDER_PEARL && material != Material.ENDER_PEARL)
							{
								MessageUtil.sendError(sender, "해당 태그는 엔더 진주에만 사용할 수 있습니다");
								return true;
							}
							if (itemTag == null || extraTag == null || !NBTAPI.arrayContainsValue(extraTag, extraTagType))
							{
								MessageUtil.sendError(sender,
										"주로 사용하는 손에 들고 있는 " + (extraTagType == ExtraTag.PORTABLE_SHULKER_BOX ? "셜커 상자" : "아이템") + "에 " + extraTagType.getDisplay() + " 태그 값이 없습니다");
								return true;
							}
							extraTag.remove(extraTagType.toString());
							if (extraTag.size() == 0)
							{
								NBTAPI.removeKey(itemTag, CucumberyTag.EXTRA_TAGS_KEY);
							}
							playerInventory.setItemInMainHand(nbtItem.getItem());
							if (!hideOutput)
							{
								MessageUtil.sendMessage(player, Prefix.INFO_SETDATA,
										"주로 사용하는 손에 들고 있는 " + (extraTagType == ExtraTag.PORTABLE_SHULKER_BOX ? "셜커 상자" : "아이템") + "에서 " + extraTagType.getDisplay()
												+ " 태그 값을 제거했습니다");
							}
							ItemStackUtil.updateInventory(player);
							break;
						default:
							MessageUtil.wrongArg(sender, 2, args);
							MessageUtil.commandInfo(sender, label, "extratag <add|remove> <태그>");
							return true;
					}
				}
				case "customdurability" ->
				{
					NBTCompound duraTag = NBTAPI.getCompound(itemTag, CucumberyTag.CUSTOM_DURABILITY_KEY);
					switch (args[1])
					{
						case "durability":
							if (args.length > 4)
							{
								MessageUtil.longArg(sender, 4, args);
								MessageUtil.commandInfo(sender, label, "customdurability durability <내구도> [최대 내구도]");
								return true;
							}
							if (args.length < 3)
							{
								MessageUtil.shortArg(sender, 3, args);
								MessageUtil.commandInfo(sender, label, "customdurability durability <내구도> [최대 내구도]");
								return true;
							}
							if (!MessageUtil.isLong(sender, args[2], true))
							{
								return true;
							}
							long curDura;
							long maxDura = Long.parseLong(args[2]);
							if (!MessageUtil.checkNumberSize(sender, maxDura, 0, Long.MAX_VALUE))
							{
								return true;
							}
							if (args.length == 4)
							{
								if (!MessageUtil.isLong(sender, args[3], true))
								{
									return true;
								}
								curDura = Long.parseLong(args[2]);
								maxDura = Long.parseLong(args[3]);
								if (!MessageUtil.checkNumberSize(sender, maxDura, 1, Long.MAX_VALUE))
								{
									return true;
								}
							}
							else
							{
								curDura = maxDura;
							}
							if (maxDura == 0)
							{
								if (itemTag == null || duraTag == null || duraTag.getLong(CucumberyTag.CUSTOM_DURABILITY_MAX_KEY) == 0)
								{
									MessageUtil.sendError(sender, "주로 사용하는 손에 들고 있는 아이템에는 커스텀 내구도가 없습니다");
									return true;
								}
								double chanceNotToConsumeDura = duraTag.getDouble(CucumberyTag.CUSTOM_DURABILITY_CHANCE_NOT_TO_CONSUME_DURABILITY);
								if (chanceNotToConsumeDura == 0d)
								{
									NBTAPI.removeKey(itemTag, CucumberyTag.CUSTOM_DURABILITY_KEY);
								}
								else
								{
									duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY, 0L);
									duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_MAX_KEY, 0L);
								}
								playerInventory.setItemInMainHand(nbtItem.getItem());
								ItemStackUtil.updateInventory(player);
								if (!hideOutput)
								{
									MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템의 커스텀 내구도를 제거했습니다");
								}
								return true;
							}
							if (itemTag == null)
							{
								itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
							}
							if (duraTag == null)
							{
								duraTag = itemTag.addCompound(CucumberyTag.CUSTOM_DURABILITY_KEY);
							}
							double chance = duraTag.getDouble(CucumberyTag.CUSTOM_DURABILITY_CHANCE_NOT_TO_CONSUME_DURABILITY);
							duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY, maxDura - curDura);
							duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_MAX_KEY, maxDura);
							duraTag.setDouble(CucumberyTag.CUSTOM_DURABILITY_CHANCE_NOT_TO_CONSUME_DURABILITY, chance);
							playerInventory.setItemInMainHand(nbtItem.getItem());
							ItemStackUtil.updateInventory(player);
							if (!hideOutput)
							{
								MessageUtil.sendMessage(player, Prefix.INFO_SETDATA,
										"주로 사용하는 손에 들고 있는 아이템의 커스텀 내구도의 현재 내구도를 rg255,204;" + curDura + "&r, 최대 내구도를 rg255,204;" + maxDura + "&r으로 설정했습니다");
							}
							break;
						case "chance":
							if (args.length > 3)
							{
								MessageUtil.longArg(sender, 3, args);
								MessageUtil.commandInfo(sender, label, "customdurability chance <내구도 감소 확률 무효(%)>");
								return true;
							}
							if (args.length < 3)
							{
								MessageUtil.shortArg(sender, 3, args);
								MessageUtil.commandInfo(sender, label, "customdurability chance <내구도 감소 확률 무효(%)>");
								return true;
							}
							if (!MessageUtil.isDouble(sender, args[2], true))
							{
								return true;
							}
							double chanceNotToConsumeDura = Double.parseDouble(args[2]);
							if (!MessageUtil.checkNumberSize(sender, chanceNotToConsumeDura, 0, 100))
							{
								return true;
							}
							if (chanceNotToConsumeDura == 0d)
							{
								if (duraTag == null || duraTag.getDouble(CucumberyTag.CUSTOM_DURABILITY_CHANCE_NOT_TO_CONSUME_DURABILITY) == 0d)
								{
									MessageUtil.sendError(sender, "주로 사용하는 손에 들고 있는 아이템에는 내구도 감소 무효 확률 태그 값이 없습니다");
									return true;
								}
								maxDura = duraTag.getLong(CucumberyTag.CUSTOM_DURABILITY_MAX_KEY);
								if (maxDura == 0)
								{
									NBTAPI.removeKey(itemTag, CucumberyTag.CUSTOM_DURABILITY_KEY);
								}
								else
								{
									duraTag.setDouble(CucumberyTag.CUSTOM_DURABILITY_CHANCE_NOT_TO_CONSUME_DURABILITY, chanceNotToConsumeDura);
								}
								playerInventory.setItemInMainHand(nbtItem.getItem());
								ItemStackUtil.updateInventory(player);
								if (!hideOutput)
								{
									MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템의 내구도 감소 무효 확률 태그 값을 제거했습니다");
								}
								return true;
							}
							if (itemTag == null)
							{
								itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
							}
							if (duraTag == null)
							{
								curDura = 0L;
								maxDura = 0L;
								duraTag = itemTag.addCompound(CucumberyTag.CUSTOM_DURABILITY_KEY);
							}
							else
							{
								curDura = duraTag.getLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY);
								maxDura = duraTag.getLong(CucumberyTag.CUSTOM_DURABILITY_MAX_KEY);
							}
							duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY, maxDura - curDura);
							duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_MAX_KEY, maxDura);
							duraTag.setDouble(CucumberyTag.CUSTOM_DURABILITY_CHANCE_NOT_TO_CONSUME_DURABILITY, chanceNotToConsumeDura);

							playerInventory.setItemInMainHand(nbtItem.getItem());
							ItemStackUtil.updateInventory(player);
							if (!hideOutput)
							{
								MessageUtil.sendMessage(player, Prefix.INFO_SETDATA,
										"주로 사용하는 손에 들고 있는 아이템의 내구도 감소 무효 확률 태그 값을 rg255,204;" + Constant.Sosu15.format(chanceNotToConsumeDura) + "%&r로 설정했습니다");
							}
							break;
						default:
							MessageUtil.wrongArg(sender, 2, args);
							MessageUtil.commandInfo(sender, label, "customdurability <durability|chance> ...");
							return true;
					}
				}
				case "customitemtype" ->
				{
					String customItemTypeTag = itemTag == null ? null : itemTag.getString(CucumberyTag.CUSTOM_ITEM_TYPE_KEY);
					if (args.length == 2 && args[1].equals("--remove"))
					{
						if (customItemTypeTag == null)
						{
							MessageUtil.sendError(sender, "주로 사용하는 손에 들고 있는 아이템에는 사용자 지정 아이템 종류 태그 값이 없습니다");
							return true;
						}
						NBTAPI.removeKey(itemTag, CucumberyTag.CUSTOM_ITEM_TYPE_KEY);
						playerInventory.setItemInMainHand(nbtItem.getItem());
						ItemStackUtil.updateInventory(player);
						MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템의 사용자 지정 아이템 종류 태그 값을 제거했습니다");
					}
					else
					{
						if (itemTag == null)
						{
							itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
						}
						String customItemType = MessageUtil.listToString(" ", 1, args.length, args);
						itemTag.setString(CucumberyTag.CUSTOM_ITEM_TYPE_KEY, customItemType);
						playerInventory.setItemInMainHand(nbtItem.getItem());
						ItemStackUtil.updateInventory(player);
						if (!hideOutput)
						{
							MessageUtil.sendMessage(player, Prefix.INFO_SETDATA,
									"주로 사용하는 손에 들고 있는 아이템의 사용자 지정 아이템 종류 태그 값을 rg255,204;" + customItemType + "&r" + MessageUtil.getFinalConsonant(customItemType,
											ConsonantType.으로) + " 지정했습니다");
						}
						return true;
					}
				}
				case "hideflag" ->
				{
					if (args.length < 3)
					{
						MessageUtil.shortArg(sender, 3, args);
						MessageUtil.commandInfo(sender, label, "hideflag <add|remove> <태그>");
						return true;
					}
					if (args.length > 3)
					{
						MessageUtil.longArg(sender, 3, args);
						MessageUtil.commandInfo(sender, label, "hideflag <add|remove> <태그>");
						return true;
					}
					boolean all = args[2].equals("--all") || args[2].equals("--모두");
					boolean ables = args[2].equals("--ables") || args[2].equals("--가능충");
					boolean dura = args[2].equals("--dura") || args[2].equals("--내구도");
					boolean isGroup = all || ables || dura;
					NBTList<String> hideFlagsTag = NBTAPI.getStringList(itemTag, CucumberyTag.HIDE_FLAGS_KEY);
					CucumberyHideFlag hideFlagType = null;
					if (!isGroup)
					{
						try
						{
							hideFlagType = CucumberyHideFlag.valueOf(args[2].toUpperCase());
						}
						catch (Exception e)
						{
							MessageUtil.wrongArg(sender, 3, args);
							MessageUtil.commandInfo(sender, label, "hideflag add <태그>");
							return true;
						}
					}
					switch (args[1])
					{
						case "add" ->
						{
							if (itemTag == null)
							{
								itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
							}
							if (hideFlagsTag == null)
							{
								hideFlagsTag = itemTag.getStringList(CucumberyTag.HIDE_FLAGS_KEY);
							}
							if (isGroup)
							{
								if (hideFlagsTag.size() == CucumberyHideFlag.values().length)
								{
									MessageUtil.sendError(sender, "이미 주로 사용하는 손에 들고 있는 아이템에는 모든 설명 숨김 태그가 있습니다");
									return true;
								}

								if (all)
								{
									hideFlagsTag.clear();
									for (CucumberyHideFlag cucumberyHideFlag : CucumberyHideFlag.values())
									{
										hideFlagsTag.add(cucumberyHideFlag.toString());
									}
								}
								if (ables)
								{
									boolean success = false;
									if (!NBTAPI.arrayContainsValue(hideFlagsTag, CucumberyHideFlag.COMPOSTABLE))
									{
										success = true;
										hideFlagsTag.add(CucumberyHideFlag.COMPOSTABLE.toString());
									}
									if (!NBTAPI.arrayContainsValue(hideFlagsTag, CucumberyHideFlag.PLACABLE))
									{
										success = true;
										hideFlagsTag.add(CucumberyHideFlag.PLACABLE.toString());
									}
									if (!NBTAPI.arrayContainsValue(hideFlagsTag, CucumberyHideFlag.CRAFTABLE))
									{
										success = true;
										hideFlagsTag.add(CucumberyHideFlag.CRAFTABLE.toString());
									}
									if (!NBTAPI.arrayContainsValue(hideFlagsTag, CucumberyHideFlag.SMELTABLE))
									{
										success = true;
										hideFlagsTag.add(CucumberyHideFlag.SMELTABLE.toString());
									}
									if (!NBTAPI.arrayContainsValue(hideFlagsTag, CucumberyHideFlag.FUEL))
									{
										success = true;
										hideFlagsTag.add(CucumberyHideFlag.FUEL.toString());
									}
									if (!NBTAPI.arrayContainsValue(hideFlagsTag, CucumberyHideFlag.CONSUMABLE))
									{
										success = true;
										hideFlagsTag.add(CucumberyHideFlag.CONSUMABLE.toString());
									}
									if (!NBTAPI.arrayContainsValue(hideFlagsTag, CucumberyHideFlag.BREWABLE))
									{
										success = true;
										hideFlagsTag.add(CucumberyHideFlag.BREWABLE.toString());
									}
									if (!success)
									{
										MessageUtil.sendError(sender, "이미 주로 사용하는 손에 들고 있는 아이템에는 모든 가능충 관련 설명 숨김 태그가 있습니다");
										return true;
									}
								}
								if (dura)
								{
									boolean success = false;
									if (!NBTAPI.arrayContainsValue(hideFlagsTag, CucumberyHideFlag.DURABILITY))
									{
										success = true;
										hideFlagsTag.add(CucumberyHideFlag.DURABILITY.toString());
									}
									if (!NBTAPI.arrayContainsValue(hideFlagsTag, CucumberyHideFlag.DURABILITY_CHANCE_NOT_TO_CONSUME))
									{
										success = true;
										hideFlagsTag.add(CucumberyHideFlag.DURABILITY.toString());
									}
									if (!success)
									{
										MessageUtil.sendError(sender, "이미 주로 사용하는 손에 들고 있는 아이템에는 모든 내구도 관련 설명 숨김 태그가 있습니다");
										return true;
									}
								}
							}
							else
							{
								if (NBTAPI.arrayContainsValue(hideFlagsTag, hideFlagType))
								{
									MessageUtil.sendError(sender,
											"이미 주로 사용하는 손에 들고 있는 아이템에 설명 숨김(rg255,204;" + hideFlagType + "&r(rg255,204;" + hideFlagType.getDisplay() + "&r)) 태그가 있습니다");
									return true;
								}
								hideFlagsTag.add(hideFlagType.toString());
							}
							playerInventory.setItemInMainHand(nbtItem.getItem());
							if (!hideOutput)
							{
								if (isGroup)
								{
									if (all)
									{
										MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템에 모든 설명 숨김 태그 값을 추가했습니다");
									}
									if (ables)
									{
										MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템에 모든 가능충 관련 설명 숨김 태그 값을 추가했습니다");
									}
									if (dura)
									{
										MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템에 모든 내구도 관련 설명 숨김 태그 값을 추가했습니다");
									}
								}
								else
								{
									MessageUtil.sendMessage(player, Prefix.INFO_SETDATA,
											"주로 사용하는 손에 들고 있는 아이템에 설명 숨김(rg255,204;" + hideFlagType + "&r(rg255,204;" + hideFlagType.getDisplay() + "&r)) 태그 값을 추가했습니다");
								}
							}
							ItemStackUtil.updateInventory(player);
						}
						case "remove" ->
						{
							if (isGroup && (hideFlagsTag == null || hideFlagsTag.size() == 0))
							{
								MessageUtil.sendError(sender, "주로 사용하는 손에 들고 있는 아이템에는 설명 숨김 태그가 없습니다");
								return true;
							}
							if (!isGroup)
							{
								if (hideFlagsTag == null || !NBTAPI.arrayContainsValue(hideFlagsTag, hideFlagType))
								{
									MessageUtil.sendError(sender,
											"주로 사용하는 손에 들고 있는 아이템에는 설명 숨김(rg255,204;" + hideFlagType + "&r(rg255,204;" + hideFlagType.getDisplay() + "&r)) 태그 값이 없습니다");
									return true;
								}
								hideFlagsTag.remove(hideFlagType.toString());
							}
							if (ables)
							{
								boolean success = false;
								if (NBTAPI.arrayContainsValue(hideFlagsTag, CucumberyHideFlag.COMPOSTABLE))
								{
									success = true;
									hideFlagsTag.remove(CucumberyHideFlag.COMPOSTABLE.toString());
								}
								if (NBTAPI.arrayContainsValue(hideFlagsTag, CucumberyHideFlag.PLACABLE))
								{
									success = true;
									hideFlagsTag.remove(CucumberyHideFlag.PLACABLE.toString());
								}
								if (NBTAPI.arrayContainsValue(hideFlagsTag, CucumberyHideFlag.CRAFTABLE))
								{
									success = true;
									hideFlagsTag.remove(CucumberyHideFlag.CRAFTABLE.toString());
								}
								if (NBTAPI.arrayContainsValue(hideFlagsTag, CucumberyHideFlag.SMELTABLE))
								{
									success = true;
									hideFlagsTag.remove(CucumberyHideFlag.SMELTABLE.toString());
								}
								if (NBTAPI.arrayContainsValue(hideFlagsTag, CucumberyHideFlag.FUEL))
								{
									success = true;
									hideFlagsTag.remove(CucumberyHideFlag.FUEL.toString());
								}
								if (NBTAPI.arrayContainsValue(hideFlagsTag, CucumberyHideFlag.CONSUMABLE))
								{
									success = true;
									hideFlagsTag.remove(CucumberyHideFlag.CONSUMABLE.toString());
								}
								if (NBTAPI.arrayContainsValue(hideFlagsTag, CucumberyHideFlag.BREWABLE))
								{
									success = true;
									hideFlagsTag.remove(CucumberyHideFlag.BREWABLE.toString());
								}
								if (!success)
								{
									MessageUtil.sendError(sender, "이미 주로 사용하는 손에 들고 있는 아이템에는 모든 가능충 관련 설명 숨김 태그가 없습니다");
									return true;
								}
							}
							if (dura)
							{
								boolean success = false;
								if (NBTAPI.arrayContainsValue(hideFlagsTag, CucumberyHideFlag.COMPOSTABLE))
								{
									success = true;
									hideFlagsTag.remove(CucumberyHideFlag.COMPOSTABLE.toString());
								}
								if (NBTAPI.arrayContainsValue(hideFlagsTag, CucumberyHideFlag.PLACABLE))
								{
									success = true;
									hideFlagsTag.remove(CucumberyHideFlag.PLACABLE.toString());
								}
								if (!success)
								{
									MessageUtil.sendError(sender, "이미 주로 사용하는 손에 들고 있는 아이템에는 모든 가능충 관련 설명 숨김 태그가 없습니다");
									return true;
								}
							}
							if (all || hideFlagsTag.size() == 0)
							{
								NBTAPI.removeKey(itemTag, CucumberyTag.HIDE_FLAGS_KEY);
							}
							playerInventory.setItemInMainHand(nbtItem.getItem());
							if (!hideOutput)
							{
								if (isGroup)
								{
									if (all)
									{
										MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템에서 모든 설명 숨김 태그 값을 제거했습니다");
									}
									if (ables)
									{
										MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템에 모든 가능충 관련 설명 숨김 태그 값을 제거했습니다");
									}
									if (dura)
									{
										MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템에 모든 내구도 관련 설명 숨김 태그 값을 제거했습니다");
									}
								}
								else
								{
									MessageUtil.sendMessage(player, Prefix.INFO_SETDATA,
											"주로 사용하는 손에 들고 있는 아이템에서 설명 숨김(rg255,204;" + hideFlagType + "&r(rg255,204;" + hideFlagType.getDisplay() + "&r)) 태그 값을 제거했습니다");
								}
							}
							ItemStackUtil.updateInventory(player);
						}
						default ->
						{
							MessageUtil.wrongArg(sender, 2, args);
							MessageUtil.commandInfo(sender, label, "hideflag <add|remove> <태그>");
							return true;
						}
					}
				}
				case "customrarity" ->
				{
					if (args.length > 3)
					{
						MessageUtil.longArg(sender, 3, args);
						MessageUtil.commandInfo(sender, label, "customrarity <base|value> <값>");
						return true;
					}
					if (args.length < 3)
					{
						MessageUtil.shortArg(sender, 3, args);
						MessageUtil.commandInfo(sender, label, "customrarity <base|value> <값>");
						return true;
					}
					NBTCompound customRarityTag = NBTAPI.getCompound(itemTag, CucumberyTag.CUSTOM_RARITY_KEY);
					switch (args[1])
					{
						case "base" ->
						{
							if (args[2].equals("--remove"))
							{
								if (customRarityTag == null || !customRarityTag.hasTag(CucumberyTag.CUSTOM_RARITY_BASE_KEY))
								{
									MessageUtil.sendError(sender, "주로 사용하는 손에 들고 있는 아이템에 커스텀 희귀도 기본값 태그 값이 없습니다");
									return true;
								}
								NBTAPI.removeKey(customRarityTag, CucumberyTag.CUSTOM_RARITY_BASE_KEY);
								playerInventory.setItemInMainHand(nbtItem.getItem());
								if (!hideOutput)
								{
									MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템의 커스텀 희귀도 기본값 태그 값을 제거했습니다");
								}
								ItemStackUtil.updateInventory(player);
								return true;
							}
							Rarity rarity;
							try
							{
								rarity = Rarity.valueOf(args[2].toUpperCase());
							}
							catch (Exception e)
							{
								MessageUtil.wrongArg(sender, 3, args);
								MessageUtil.commandInfo(sender, label, "customrarity base <값>");
								return true;
							}
							if (itemTag == null)
							{
								itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
							}
							if (customRarityTag == null)
							{
								customRarityTag = itemTag.addCompound(CucumberyTag.CUSTOM_RARITY_KEY);
							}
							customRarityTag.setString(CucumberyTag.CUSTOM_RARITY_BASE_KEY, rarity.toString());
							playerInventory.setItemInMainHand(nbtItem.getItem());
							if (!hideOutput)
							{
								MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템의 커스텀 희귀도 기본값 태그 값을 rg255,204;" + rarity + "&r으로 설정했습니다");
							}
							ItemStackUtil.updateInventory(player);
						}
						case "set" ->
						{
							if (args[2].equals("--remove"))
							{
								if (customRarityTag == null || !customRarityTag.hasTag(CucumberyTag.CUSTOM_RARITY_FINAL_KEY))
								{
									MessageUtil.sendError(sender, "주로 사용하는 손에 들고 있는 아이템에 커스텀 희귀도 태그 값이 없습니다");
									return true;
								}
								NBTAPI.removeKey(customRarityTag, CucumberyTag.CUSTOM_RARITY_FINAL_KEY);
								playerInventory.setItemInMainHand(nbtItem.getItem());
								if (!hideOutput)
								{
									MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템의 커스텀 희귀도 태그 값을 제거했습니다");
								}
								ItemStackUtil.updateInventory(player);
								return true;
							}
							Rarity rarity;
							try
							{
								rarity = Rarity.valueOf(args[2].toUpperCase());
							}
							catch (Exception e)
							{
								MessageUtil.wrongArg(sender, 3, args);
								MessageUtil.commandInfo(sender, label, "customrarity set <값>");
								return true;
							}
							if (itemTag == null)
							{
								itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
							}
							if (customRarityTag == null)
							{
								customRarityTag = itemTag.addCompound(CucumberyTag.CUSTOM_RARITY_KEY);
							}
							customRarityTag.setString(CucumberyTag.CUSTOM_RARITY_FINAL_KEY, rarity.toString());
							playerInventory.setItemInMainHand(nbtItem.getItem());
							if (!hideOutput)
							{
								MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템의 커스텀 희귀도 태그 값을 rg255,204;" + rarity + "&r으로 설정했습니다");
							}
							ItemStackUtil.updateInventory(player);
						}
						case "value" ->
						{
							if (!MessageUtil.isInteger(sender, args[2], true))
							{
								return true;
							}
							int rarityValue = Integer.parseInt(args[2]);
							if (!MessageUtil.checkNumberSize(sender, rarityValue, -2_000_000_000, 2_000_000_000))
							{
								return true;
							}
							if (rarityValue == 0)
							{
								if (customRarityTag == null || !customRarityTag.hasTag(CucumberyTag.VALUE_KEY))
								{
									MessageUtil.sendError(sender, "주로 사용하는 손에 들고 있는 아이템에 커스텀 희귀도 수치 태그 값이 없습니다");
									return true;
								}
								NBTAPI.removeKey(customRarityTag, CucumberyTag.VALUE_KEY);
								playerInventory.setItemInMainHand(nbtItem.getItem());
								if (!hideOutput)
								{
									MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템의 커스텀 희귀도 수치 태그 값을 제거했습니다");
								}
								ItemStackUtil.updateInventory(player);
								return true;
							}
							if (itemTag == null)
							{
								itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
							}
							if (customRarityTag == null)
							{
								customRarityTag = itemTag.addCompound(CucumberyTag.CUSTOM_RARITY_KEY);
							}
							customRarityTag.setInteger(CucumberyTag.VALUE_KEY, rarityValue);
							playerInventory.setItemInMainHand(nbtItem.getItem());
							if (!hideOutput)
							{
								MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템의 커스텀 희귀도 수치 태그 값을 rg255,204;" + rarityValue + "&r으로 설정했습니다");
							}
							ItemStackUtil.updateInventory(player);
						}
						default ->
						{
							MessageUtil.wrongArg(sender, 2, args);
							MessageUtil.commandInfo(sender, label, "customrarity <base|set|value> <값>");
							return true;
						}
					}
				}
				case "usage" ->
				{
					if (args.length < 3)
					{
						MessageUtil.shortArg(player, 3, args);
						MessageUtil.commandInfo(sender, label, "usage <command|cooldown|disposable|equip|permission> ...");
						return true;
					}
					NBTCompound usageTag = NBTAPI.getCompound(itemTag, CucumberyTag.USAGE_KEY);
					Constant.ItemUsageType itemUsageType = Constant.ItemUsageType.RIGHT_CLICK;
					NBTCompound usageTypeTag = NBTAPI.getCompound(usageTag, itemUsageType.getKey());
					if (Method.equals(args[1], "cooldown", "command", "disposable", "permission"))
					{
						try
						{
							itemUsageType = Constant.ItemUsageType.valueOf(args[2].toUpperCase());
						}
						catch (Exception e)
						{
							MessageUtil.sendError(sender,
									Constant.THE_COLOR_HEX + args[2] + "&r" + MessageUtil.getFinalConsonant(args[2], ConsonantType.은는) + " 알 수 없거나 잘못된 실행 유형입니다");
							return true;
						}
						usageTypeTag = NBTAPI.getCompound(usageTag, itemUsageType.getKey());
					}
					switch (args[1])
					{
						case "permission":
							if (args.length < 4)
							{
								MessageUtil.shortArg(player, 4, args);
								MessageUtil.commandInfo(sender, label, "usage permission " + args[2] + " <퍼미션 노드|--remove>");
								return true;
							}
							if (args.length > 4)
							{
								MessageUtil.longArg(player, 4, args);
								MessageUtil.commandInfo(sender, label, "usage permission " + args[2] + " <퍼미션 노드|--remove>");
								return true;
							}
							String inputPermission = args[3];
							String originPermission = NBTAPI.getString(usageTypeTag, CucumberyTag.PERMISSION_KEY);
							if (inputPermission.equals("--remove"))
							{
								if (originPermission == null)
								{
									if (!hideOutput)
									{
										MessageUtil.sendError(player, "변경 사항이 없습니다. 이미 주로 사용하는 손에 들고 있는 아이템의 " + itemUsageType.getDisplay() + " 요구 퍼미션 노드 값이 없습니다");
									}
									return true;
								}
								NBTAPI.removeKey(usageTypeTag, CucumberyTag.PERMISSION_KEY);
								if (!hideOutput)
								{
									MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템의 " + itemUsageType.getDisplay() + " 시 요구 퍼미션 노드 값을 제거했습니다");
								}
							}
							else
							{
								if (itemTag == null)
								{
									itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
								}
								if (usageTag == null)
								{
									usageTag = itemTag.addCompound(CucumberyTag.USAGE_KEY);
								}
								if (usageTypeTag == null)
								{
									usageTypeTag = usageTag.addCompound(itemUsageType.getKey());
								}
								usageTypeTag.setString(CucumberyTag.PERMISSION_KEY, inputPermission);
								if (!hideOutput)
								{
									MessageUtil.sendMessage(player, Prefix.INFO_SETDATA,
											"주로 사용하는 손에 들고 있는 아이템의 " + itemUsageType.getDisplay() + " 시 요구 퍼미션 노드 값을 rg255,204;" + inputPermission + "&r으로 설정했습니다");
								}
							}
							playerInventory.setItemInMainHand(nbtItem.getItem());
							break;
						case "disposable":
							if (args[2].contains("attack"))
							{
								MessageUtil.sendError(sender, Constant.THE_COLOR_HEX + itemUsageType.getDisplay() + " 태그에는 소비 확률을 적용할 수 없습니다");
								return true;
							}
							if (args.length < 4)
							{
								MessageUtil.shortArg(player, 4, args);
								MessageUtil.commandInfo(sender, label, "usage disposable " + args[2] + " <아이템 소비 확률(%)>");
								return true;
							}
							if (args.length > 4)
							{
								MessageUtil.longArg(player, 4, args);
								MessageUtil.commandInfo(sender, label, "usage disposable " + args[2] + " <아이템 소비 확률(%)>");
								return true;
							}
							if (!MessageUtil.isDouble(sender, args[3], true))
							{
								return true;
							}
							double chance = Double.parseDouble(args[3]);
							if (chance != -1d && !MessageUtil.checkNumberSize(sender, chance, 0, 100))
							{
								return true;
							}
							if (itemTag == null)
							{
								itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
							}
							if (usageTag == null)
							{
								usageTag = itemTag.addCompound(CucumberyTag.USAGE_KEY);
							}
							if (usageTypeTag == null)
							{
								usageTypeTag = usageTag.addCompound(itemUsageType.getKey());
							}
							if (chance == -1d)
							{
								NBTAPI.removeKey(usageTypeTag, CucumberyTag.USAGE_DISPOSABLE_KEY);
							}
							else
							{
								usageTypeTag.setDouble(CucumberyTag.USAGE_DISPOSABLE_KEY, chance);
							}
							playerInventory.setItemInMainHand(nbtItem.getItem());
							if (!hideOutput)
							{
								MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템의 " + itemUsageType.getDisplay() + " 시 소비 확률 태그 값을 rg255,204;" + (
										chance == -1d
												? "&r제거했습니다"
												: Constant.Sosu2.format(chance) + "%&r로 설정했습니다"));
							}
							break;
						case "command":
							if (args.length < 4)
							{
								MessageUtil.shortArg(player, 4, args);
								MessageUtil.commandInfo(sender, label, "usage command " + args[2] + "<add|remove|list|set|insert> ...");
								return true;
							}
							NBTList<String> commandsTag = NBTAPI.getStringList(usageTypeTag, CucumberyTag.USAGE_COMMANDS_KEY);
							switch (args[3])
							{
								case "list":
									if (args.length > 4)
									{
										MessageUtil.longArg(player, 4, args);
										MessageUtil.commandInfo(sender, label, "usage command " + args[2] + "list");
										return true;
									}
									if (commandsTag == null || commandsTag.isEmpty())
									{
										MessageUtil.sendError(player, "주로 사용하는 손에 들고 있는 아이템에는 " + itemUsageType.getDisplay() + " 시 명령어 실행 태그 값이 없습니다");
										return true;
									}
									MessageUtil.sendMessage(player, Prefix.INFO_SETDATA,
											"주로 사용하는 손에 들고 있는 아이템의 " + itemUsageType.getDisplay() + " 시 실행되는 명령어의 개수는 rg255,204;" + commandsTag.size() + "개&r입니다");
									for (int i = 0; i < commandsTag.size(); i++)
									{
										String command = commandsTag.get(i);
										MessageUtil.sendMessage(player, Prefix.INFO_SETDATA,
												ComponentUtil.translate("%s번째 명령어 : %s", Constant.THE_COLOR_HEX + (i + 1), ComponentUtil.create2(command.replace("§", "&"), false))
														.hoverEvent(ComponentUtil.translate("클릭하여 %s번째 명령어를 수정합니다", Constant.THE_COLOR_HEX + (i + 1))).clickEvent(ClickEvent.suggestCommand(
																"/itag usage command " + itemUsageType.toString().toLowerCase() + " set " + (i + 1) + " " + command.replace("§", "&"))));
									}
									break;
								case "add":
									if (args.length < 5)
									{
										MessageUtil.shortArg(sender, 5, args);
										MessageUtil.commandInfo(sender, label, "usage command " + args[2] + " add <명령어>");
										return true;
									}
									if (itemTag == null)
									{
										itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
									}
									if (usageTag == null)
									{
										usageTag = itemTag.addCompound(CucumberyTag.USAGE_KEY);
									}
									if (usageTypeTag == null)
									{
										usageTypeTag = usageTag.addCompound(itemUsageType.getKey());
									}
									if (commandsTag == null)
									{
										commandsTag = usageTypeTag.getStringList(CucumberyTag.USAGE_COMMANDS_KEY);
									}
									String inputCommand = MessageUtil.n2s(MessageUtil.listToString(" ", 4, args.length, args), N2SType.SPECIAL);
									commandsTag.add(inputCommand);

									playerInventory.setItemInMainHand(nbtItem.getItem());
									if (!hideOutput)
									{
										MessageUtil.sendMessage(player, Prefix.INFO_SETDATA,
												"주로 사용하는 손에 들고 있는 아이템의 " + itemUsageType.getDisplay() + " 시 실행되는 명령어에 rg255,204;/" + inputCommand + "&r 명령어를 추가했습니다");
									}
									break;
								case "remove":
									if (args.length > 5)
									{
										MessageUtil.longArg(sender, 5, args);
										MessageUtil.commandInfo(sender, label, "usage command " + args[2] + " remove [줄|--all]");
										return true;
									}
									if (commandsTag == null)
									{
										MessageUtil.sendError(sender, "주로 사용하는 손에 들고 있는 아이템에는 " + itemUsageType.getDisplay() + " 시 명령어 실행 태그 값이 없습니다");
										return true;
									}
									boolean contains = false;
									int line = commandsTag.size() - 1;
									boolean clearAll = false;
									if (args.length == 5)
									{
										if (args[4].equals("--all"))
										{
											clearAll = true;
											contains = !commandsTag.isEmpty();
										}
										else
										{
											try
											{
												line = Integer.parseInt(args[4]);
											}
											catch (Exception e)
											{
												MessageUtil.noArg(sender, Prefix.ONLY_NUMBER, args[4]);
												return true;
											}
											if (!MessageUtil.checkNumberSize(player, line, 1, commandsTag.size()))
											{
												return true;
											}

											line--; // 숫자를 3이라고 입력하면 get(2) 를 해야하므로 1을 뺌
										}
									}
									if (!clearAll)
									{
										for (int i = 0; i < commandsTag.size(); i++)
										{
											if (line == i)
											{
												commandsTag.remove(i);
												contains = true;
												break;
											}
										}
									}
									if (contains)
									{
										if (clearAll || commandsTag.size() == 0)
										{
											NBTAPI.removeKey(usageTypeTag, CucumberyTag.USAGE_COMMANDS_KEY);
										}
										playerInventory.setItemInMainHand(nbtItem.getItem());
										if (!hideOutput)
										{
											if (clearAll)
											{
												MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템의 " + itemUsageType.getDisplay() + " 시 실행되는 모든 명령어를 제거했습니다");
											}
											else
											{
												MessageUtil.sendMessage(player, Prefix.INFO_SETDATA,
														"주로 사용하는 손에 들고 있는 아이템의 " + itemUsageType.getDisplay() + " 시 실행되는 명령어의 rg255,204;" + (line + 1) + "번째&r 명령어를 제거했습니다");
											}
										}
									}
									else
									{
										MessageUtil.sendError(player, "주로 사용하는 손에 들고 있는 아이템에 사용 시 실행되는 명령어에 rg255,204;" + (line + 1) + "번째&r 명령어가 존재하지 않습니다");
										return true;
									}
									break;
								case "insert":
									if (args.length < 6)
									{
										MessageUtil.shortArg(sender, 6, args);
										MessageUtil.commandInfo(sender, label, "usage command " + args[2] + " insert <들여쓸 줄> <명령어>");
										return true;
									}
									if (!MessageUtil.isInteger(sender, args[4], true))
									{
										return true;
									}
									if (commandsTag == null)
									{
										MessageUtil.sendError(sender, "주로 사용하는 손에 들고 있는 아이템에는 " + itemUsageType.getDisplay() + " 시 명령어 실행 태그 값이 없습니다");
										return true;
									}
									int inputLoreLine = Integer.parseInt(args[4]);
									if (!MessageUtil.checkNumberSize(sender, inputLoreLine, 1, commandsTag.size()))
									{
										return true;
									}
									List<String> newCommandsTag = new ArrayList<>();
									for (int i = 0; i < inputLoreLine - 1; i++)
									{
										newCommandsTag.add(commandsTag.get(i));
									}
									inputCommand = MessageUtil.n2s(MessageUtil.listToString(" ", 5, args.length, args), N2SType.SPECIAL);
									newCommandsTag.add(inputCommand);
									for (int i = inputLoreLine - 1; i < commandsTag.size(); i++)
									{
										newCommandsTag.add(commandsTag.get(i));
									}
									commandsTag.clear();
									commandsTag.addAll(newCommandsTag);
									playerInventory.setItemInMainHand(nbtItem.getItem());
									if (!hideOutput)
									{
										MessageUtil.sendMessage(player, Prefix.INFO_SETDATA,
												"주로 사용하는 손에 들고 있는 아이템에 " + itemUsageType.getDisplay() + " 시 실행되는 명령어의 rg255,204;" + inputLoreLine + "번째&r에 rg255,204;/" + inputCommand
														+ "&r" + MessageUtil.getFinalConsonant(inputCommand, ConsonantType.이라) + "는 명령어를 들여썼습니다");
									}
									break;
								case "set":
									if (args.length < 6)
									{
										MessageUtil.shortArg(sender, 6, args);
										MessageUtil.commandInfo(sender, label, "usage command " + args[2] + " set <줄> <설명|--empty>");
										return true;
									}
									if (!MessageUtil.isInteger(sender, args[4], true))
									{
										return true;
									}
									inputLoreLine = Integer.parseInt(args[4]);
									if (!MessageUtil.checkNumberSize(sender, inputLoreLine, 1, Integer.MAX_VALUE))
									{
										return true;
									}
									inputCommand = MessageUtil.n2s(MessageUtil.listToString(" ", 5, args.length, args), N2SType.SPECIAL);
									if (itemTag == null)
									{
										itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
									}
									if (usageTag == null)
									{
										usageTag = itemTag.addCompound(CucumberyTag.USAGE_KEY);
									}
									if (usageTypeTag == null)
									{
										usageTypeTag = usageTag.addCompound(itemUsageType.getKey());
									}
									if (commandsTag == null)
									{
										commandsTag = usageTypeTag.getStringList(CucumberyTag.USAGE_COMMANDS_KEY);
									}
									int commandsTagSize = commandsTag.size();
									for (int i = 0; i < inputLoreLine - commandsTagSize; i++)
									{
										commandsTag.add("");
									}
									commandsTag.set(inputLoreLine - 1, inputCommand);

									playerInventory.setItemInMainHand(nbtItem.getItem());
									if (!hideOutput)
									{
										MessageUtil.sendMessage(player, Prefix.INFO_SETDATA,
												"주로 사용하는 손에 들고 있는 아이템에 " + itemUsageType.getDisplay() + " 시 실행되는 명령어의 rg255,204;" + inputLoreLine + "번째&r 명령어를 rg255,204;/"
														+ inputCommand + "&r" + MessageUtil.getFinalConsonant(inputCommand, ConsonantType.으로) + " 설정했습니다");
									}
									ItemStackUtil.updateInventory(player);
									break;
								default:
									MessageUtil.wrongArg(sender, 4, args);
									MessageUtil.commandInfo(sender, label, "usage command <add|remove|set|insert|list> ...");
									return true;
							}
							break;
						case "cooldown":
							if (args.length < 5)
							{
								MessageUtil.shortArg(sender, 5, args);
								MessageUtil.commandInfo(sender, label, "usage cooldown " + args[2] + " <tag|time> ...");
								return true;
							}
							NBTCompound cooldownTag = NBTAPI.getCompound(usageTypeTag, CucumberyTag.COOLDOWN_KEY);
							String cooldownTagTag = cooldownTag == null ? null : cooldownTag.getString(CucumberyTag.TAG_KEY);
							switch (args[3])
							{
								case "tag":
									if (args.length > 5)
									{
										MessageUtil.longArg(sender, 5, args);
										MessageUtil.commandInfo(sender, label, "usage cooldown " + args[2] + " tag <태그 이름>");
										return true;
									}
									String inputTag = args[4];
									if (itemTag == null)
									{
										itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
									}
									if (usageTag == null)
									{
										usageTag = itemTag.addCompound(CucumberyTag.USAGE_KEY);
									}
									if (usageTypeTag == null)
									{
										usageTypeTag = usageTag.addCompound(itemUsageType.getKey());
									}
									if (cooldownTag == null)
									{
										cooldownTag = usageTypeTag.addCompound(CucumberyTag.COOLDOWN_KEY);
										cooldownTag.setLong(CucumberyTag.TIME_KEY, 1000L);
									}
									if (cooldownTagTag != null && cooldownTagTag.equals(inputTag))
									{
										MessageUtil.sendError(sender,
												"변경 사항이 없습니다. 이미 주로 사용하는 손에 들고 있는 아이템의 " + itemUsageType.getDisplay() + " 대기 시간 태그의 값이 rg255,204;" + inputTag + "&r입니다");
										return true;
									}
									cooldownTag.setString(CucumberyTag.TAG_KEY, inputTag);
									playerInventory.setItemInMainHand(nbtItem.getItem());
									ItemStackUtil.updateInventory(player);
									if (!hideOutput)
									{
										MessageUtil.sendMessage(player, Prefix.INFO_SETDATA,
												"주로 사용하는 손에 들고 있는 아이템의 " + itemUsageType.getDisplay() + " 대기 시간 태그의 값을 rg255,204;" + inputTag + "&r" + MessageUtil.getFinalConsonant(
														inputTag, ConsonantType.으로) + " 설정했습니다");
									}
									break;
								case "time":
									if (args.length > 5)
									{
										MessageUtil.longArg(sender, 5, args);
										MessageUtil.commandInfo(sender, label, "usage cooldown " + args[2] + " time <재사용 대기 시간(초)>");
										return true;
									}
									if (!MessageUtil.isDouble(sender, args[4], true))
									{
										return true;
									}
									long inputTime = (long) (Double.parseDouble(args[4]) * 1000d);
									if (!MessageUtil.checkNumberSize(sender, inputTime, 0, Long.MAX_VALUE))
									{
										return true;
									}
									if ((cooldownTag == null || !cooldownTag.hasTag(CucumberyTag.TIME_KEY)) && inputTime == 0)
									{
										MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 주로 사용하는 손에 들고 있는 아이템의 " + itemUsageType.getDisplay() + " 대기 시간 태그의 값이 없습니다");
										return true;
									}
									if (itemTag == null)
									{
										itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
									}
									if (usageTag == null)
									{
										usageTag = itemTag.addCompound(CucumberyTag.USAGE_KEY);
									}
									if (usageTypeTag == null)
									{
										usageTypeTag = usageTag.addCompound(itemUsageType.getKey());
									}
									if (cooldownTag == null)
									{
										cooldownTag = usageTypeTag.addCompound(CucumberyTag.COOLDOWN_KEY);
									}
									long tagTime = 0L;
									if (cooldownTag.hasTag(CucumberyTag.TIME_KEY))
									{
										tagTime = cooldownTag.getLong(CucumberyTag.TIME_KEY);
									}
									if (inputTime == tagTime)
									{
										MessageUtil.sendError(sender,
												"변경 사항이 없습니다. 이미 주로 사용하는 손에 들고 있는 아이템의 " + itemUsageType.getDisplay() + " 대기 시간 태그의 값이 rg255,204;" + Method.timeFormatMilli(tagTime, 3)
														+ "&r입니다");
										return true;
									}
									if (inputTime != 0)
									{
										cooldownTag.setLong(CucumberyTag.TIME_KEY, inputTime);
										if (cooldownTagTag == null)
										{
											cooldownTag.setString(CucumberyTag.TAG_KEY, itemUsageType.toString().toLowerCase() + "-" + System.currentTimeMillis());
										}
									}
									else
									{
										NBTAPI.removeKey(usageTypeTag, CucumberyTag.COOLDOWN_KEY);
									}
									playerInventory.setItemInMainHand(nbtItem.getItem());
									ItemStackUtil.updateInventory(player);
									String inputTimeString = Method.timeFormatMilli(inputTime, 3);
									if (!hideOutput)
									{
										MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템의 " + itemUsageType.getDisplay() + " 대기 시간 값을 rg255,204;" + (
												inputTime == 0
														? "&r제거했습니다"
														: (inputTimeString + "&r" + MessageUtil.getFinalConsonant(inputTimeString, ConsonantType.으로) + " 설정했습니다")));
									}
									break;
								default:
									MessageUtil.wrongArg(sender, 3, args);
									MessageUtil.commandInfo(sender, label, "usage cooldown " + args[2] + " <tag|time> ...");
									return true;
							}
							break;
						case "equip":
							if (args.length > 3)
							{
								MessageUtil.longArg(player, 3, args);
								MessageUtil.commandInfo(sender, label, "usage equip <장착 가능 슬롯|--remove>");
								return true;
							}
							String inputEquipmentSlot = args[2];
							String equipmentSlotTag = usageTag == null ? null : usageTag.getString(CucumberyTag.USAGE_EQUIPMENT_SLOT_KEY);
							if (equipmentSlotTag == null && inputEquipmentSlot.equals("--remove"))
							{
								MessageUtil.sendError(player, "주로 사용하는 손에 들고 있는 아이템에는 장착 가능 슬롯 태그 값이 없습니다");
								return true;
							}
							if (itemTag == null)
							{
								itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
							}
							if (usageTag == null)
							{
								usageTag = itemTag.addCompound(CucumberyTag.USAGE_KEY);
							}
							if (equipmentSlotTag == null)
							{
								equipmentSlotTag = "";
							}
							if (inputEquipmentSlot.equals("--remove"))
							{
								NBTAPI.removeKey(usageTag, CucumberyTag.USAGE_EQUIPMENT_SLOT_KEY);
							}
							else
							{
								inputEquipmentSlot = inputEquipmentSlot.toUpperCase();
								if (equipmentSlotTag.equals(inputEquipmentSlot))
								{
									MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 주로 사용하는 손에 들고 있는 아이템의 장착 가능 슬롯 태그의 값이 rg255,204;" + inputEquipmentSlot + "&r입니다");
									return true;
								}
								switch (inputEquipmentSlot)
								{
									case "HELMET":
									case "CHESTPLATE":
									case "LEGGINGS":
									case "BOOTS":
										usageTag.setString(CucumberyTag.USAGE_EQUIPMENT_SLOT_KEY, inputEquipmentSlot);
										break;
									default:
										MessageUtil.wrongArg(sender, 3, args);
										MessageUtil.commandInfo(sender, label, "usage equip <장착 가능 슬롯|--remove>");
										return true;
								}
							}
							playerInventory.setItemInMainHand(nbtItem.getItem());
							if (!hideOutput)
							{
								MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템의 장착 가능 슬롯 태그의 값을 rg255,204;" + (inputEquipmentSlot.equals("--remove")
										? "&r제거했습니다"
										: (inputEquipmentSlot + "&r으로 설정했습니다")));
							}
							break;
						default:
							MessageUtil.wrongArg(sender, 2, args);
							MessageUtil.commandInfo(sender, label, "usage <command|cooldown|disposable|equip> ...");
							return true;
					}
				}
				case "expiredate" ->
				{
					String expireDate = itemTag == null ? null : itemTag.getString(CucumberyTag.EXPIRE_DATE_KEY);
					boolean remove = args.length == 2 && args[1].equals("--remove");
					if (remove && expireDate == null)
					{
						MessageUtil.sendError(sender, "주로 사용하는 손에 들고 있는 아이템에 기간제 태그의 값이 없습니다");
						return true;
					}
					if (itemTag == null)
					{
						itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
					}
					String inputDate = MessageUtil.listToString(" ", 1, args.length, args);
					boolean absolute = inputDate.contains("--absolute");
					if (absolute)
					{
						inputDate = inputDate.replace("--absolute", "");
					}
					boolean relative = inputDate.contains("--relative");
					if (relative)
					{
						inputDate = inputDate.replace("--relative", "");
					}
					inputDate = inputDate.replace("y", "년").replace("mo", "개월").replace("d", "일").replace("h", "시간").replace("m", "분").replace("s", "초");
					if (remove)
					{
						NBTAPI.removeKey(itemTag, CucumberyTag.EXPIRE_DATE_KEY);
					}
					else
					{
						itemTag.setString(CucumberyTag.EXPIRE_DATE_KEY, inputDate);
					}
					playerInventory.setItemInMainHand(nbtItem.getItem());
					if (!hideOutput)
					{
						MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "주로 사용하는 손에 들고 있는 아이템의 기간제 태그의 값을 rg255,204;" + (remove
								? "&r제거했습니다"
								: (inputDate + "&r" + MessageUtil.getFinalConsonant(inputDate, ConsonantType.으로) + " 설정했습니다")));
					}
					ItemStackUtil.updateInventory(player);
				}
				case "tnt" ->
				{
					if (material != Material.TNT)
					{
						MessageUtil.sendError(sender, "해당 태그는 TNT에만 사용할 수 있습니다");
						return true;
					}
					if (args.length < 3)
					{
						MessageUtil.shortArg(player, 3, args);
						MessageUtil.commandInfo(sender, label, "tnt <fuse|ignite|unstable|fire|explode-power> <값>");
						return true;
					}
					if (args.length > 3)
					{
						MessageUtil.longArg(player, 3, args);
						MessageUtil.commandInfo(sender, label, "tnt <fuse|ignite|unstable|fire|explode-power> <값>");
						return true;
					}
					NBTCompound tntTag = NBTAPI.getCompound(itemTag, CucumberyTag.TNT);
					switch (args[1])
					{
						case "unstable" ->
						{
							NBTCompound blockStateTag = nbtItem.getCompound(CucumberyTag.MINECRAFT_BLOCK_STATE_TAG_KEY);
							if (!MessageUtil.isBoolean(sender, args, 3, true))
							{
								return true;
							}
							boolean inputBoolean = Boolean.parseBoolean(args[2]);
							if (!inputBoolean && (blockStateTag == null || !blockStateTag.hasTag("unstable")))
							{
								MessageUtil.sendError(sender, "주로 사용하는 손에 들고 있는 TNT에 unstable 태그가 없습니다");
								return true;
							}
							if (inputBoolean)
							{
								if (blockStateTag == null)
								{
									blockStateTag = nbtItem.addCompound(CucumberyTag.MINECRAFT_BLOCK_STATE_TAG_KEY);
								}
								blockStateTag.setString("unstable", true + "");
							}
							else
							{
								NBTAPI.removeKey(blockStateTag, "unstable");
							}
							playerInventory.setItemInMainHand(nbtItem.getItem());
							if (!hideOutput)
							{
								MessageUtil.sendMessage(player, Prefix.INFO_SETDATA,
										"주로 사용하는 손에 들고 있는 TNT의 unstable 태그 값을 rg255,204;" + (!inputBoolean ? "&r삭제했습니다" : true + "&r으로 설정했습니다"));
							}
						}
						case "ignite" ->
						{
							if (!MessageUtil.isBoolean(sender, args, 3, true))
							{
								return true;
							}
							boolean inputBoolean = Boolean.parseBoolean(args[2]);
							if (!inputBoolean)
							{
								if (tntTag == null || !tntTag.hasTag(CucumberyTag.TNT_IGNITE))
								{
									MessageUtil.sendError(sender, "주로 사용하는 손에 들고 있는 TNT에 ignite 태그가 없습니다");
									return true;
								}
							}
							if (itemTag == null)
							{
								itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
							}
							if (tntTag == null)
							{
								tntTag = itemTag.addCompound(CucumberyTag.TNT);
								tntTag.setInteger(CucumberyTag.TNT_FUSE, 80);
							}
							tntTag.setBoolean(CucumberyTag.TNT_IGNITE, inputBoolean);
							if (!inputBoolean)
							{
								NBTAPI.removeKey(tntTag, CucumberyTag.TNT_IGNITE);
							}
							playerInventory.setItemInMainHand(nbtItem.getItem());
							if (!hideOutput)
							{
								MessageUtil.sendMessage(player, Prefix.INFO_SETDATA,
										"주로 사용하는  손에 들고 있는 TNT의 ignite 태그 값을 rg255,204;" + (!inputBoolean ? "&r삭제했습니다" : true + "&r으로 설정했습니다"));
							}
						}
						case "fuse" ->
						{
							if (!MessageUtil.isInteger(sender, args[2], true))
							{
								return true;
							}
							int inputInteger = Integer.parseInt(args[2]);
							if (!MessageUtil.checkNumberSize(sender, inputInteger, -1, (int) (Math.pow(2, 16) - 1)))
							{
								return true;
							}
							if (inputInteger == -1)
							{
								if (tntTag == null || !tntTag.hasTag(CucumberyTag.TNT_FUSE))
								{
									MessageUtil.sendError(sender, "주로 사용하는 손에 들고 있는 TNT에 ignite 태그가 없습니다");
									return true;
								}
							}
							if (itemTag == null)
							{
								itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
							}
							if (tntTag == null)
							{
								tntTag = itemTag.addCompound(CucumberyTag.TNT);
								tntTag.setBoolean(CucumberyTag.TNT_IGNITE, true);
							}
							tntTag.setInteger(CucumberyTag.TNT_FUSE, inputInteger);
							if (inputInteger == -1)
							{
								NBTAPI.removeKey(tntTag, CucumberyTag.TNT_IGNITE);
							}
							playerInventory.setItemInMainHand(nbtItem.getItem());
							if (!hideOutput)
							{
								MessageUtil.sendMessage(player, Prefix.INFO_SETDATA,
										"주로 사용하는  손에 들고 있는 TNT의 fuse 태그 값을 rg255,204;" + (inputInteger == -1 ? "&r삭제했습니다" : inputInteger + "&r으로 설정했습니다"));
							}
						}
						case "fire" ->
						{
							if (!MessageUtil.isBoolean(sender, args, 3, true))
							{
								return true;
							}
							boolean inputBoolean = Boolean.parseBoolean(args[2]);
							if (!inputBoolean)
							{
								if (tntTag == null || !tntTag.hasTag(CucumberyTag.TNT_FIRE))
								{
									MessageUtil.sendError(sender, "주로 사용하는 손에 들고 있는 TNT에 Fire 태그가 없습니다");
									return true;
								}
							}
							if (itemTag == null)
							{
								itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
							}
							if (tntTag == null)
							{
								tntTag = itemTag.addCompound(CucumberyTag.TNT);
								tntTag.setInteger(CucumberyTag.TNT_FUSE, 80);
								tntTag.setBoolean(CucumberyTag.TNT_IGNITE, true);
							}
							tntTag.setBoolean(CucumberyTag.TNT_FIRE, inputBoolean);
							if (!inputBoolean)
							{
								NBTAPI.removeKey(tntTag, CucumberyTag.TNT_FIRE);
							}
							playerInventory.setItemInMainHand(nbtItem.getItem());
							if (!hideOutput)
							{
								MessageUtil.sendMessage(player, Prefix.INFO_SETDATA,
										"주로 사용하는  손에 들고 있는 TNT의 Fire 태그 값을 rg255,204;" + (!inputBoolean ? "&r삭제했습니다" : true + "&r으로 설정했습니다"));
							}
						}
						case "explode-power" ->
						{
							if (!MessageUtil.isDouble(sender, args[2], true))
							{
								return true;
							}
							double inputDouble = Double.parseDouble(args[2]);
							if (!MessageUtil.checkNumberSize(sender, inputDouble, -1d, 500d))
							{
								return true;
							}
							if (inputDouble == -1)
							{
								if (tntTag == null || !tntTag.hasTag(CucumberyTag.TNT_EXPLODE_POWER))
								{
									MessageUtil.sendError(sender, "주로 사용하는 손에 들고 있는 TNT에 ExplodePower 태그가 없습니다");
									return true;
								}
							}
							if (itemTag == null)
							{
								itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
							}
							if (tntTag == null)
							{
								tntTag = itemTag.addCompound(CucumberyTag.TNT);
								tntTag.setInteger(CucumberyTag.TNT_FUSE, 80);
								tntTag.setBoolean(CucumberyTag.TNT_IGNITE, true);
							}
							tntTag.setDouble(CucumberyTag.TNT_EXPLODE_POWER, inputDouble);
							if (inputDouble == -1d)
							{
								NBTAPI.removeKey(tntTag, CucumberyTag.TNT_EXPLODE_POWER);
							}
							playerInventory.setItemInMainHand(nbtItem.getItem());
							if (!hideOutput)
							{
								MessageUtil.sendMessage(player, Prefix.INFO_SETDATA,
										"주로 사용하는 손에 들고 있는 TNT의 ExplodePower 태그 값을 rg255,204;" + (inputDouble == -1 ? "&r삭제했습니다" : inputDouble + "&r으로 설정했습니다"));
							}
						}
						default ->
						{
							MessageUtil.wrongArg(sender, 2, args);
							MessageUtil.commandInfo(sender, label, "tnt <fuse|ignite|unstable|fire|explode-power> <값>");
						}
					}
				}
				case "customitem" ->
				{
					NBTCompound customItemTag = NBTAPI.getCompound(itemTag, CucumberyTag.CUSTOM_ITEM_TAG_KEY);
					String customItemType = NBTAPI.getString(customItemTag, CucumberyTag.ID_KEY);
					if (args.length >= 3)
					{
						switch (args[1])
						{
							case "setid":
								if (args.length == 3)
								{
									String id = args[2];
									if (id.equals("--remove"))
									{
										if (customItemTag == null)
										{
											MessageUtil.sendError(sender, "주로 사용하는 손에 들고 있는 아이템에 커스텀 아이템 태그가 없습니다");
											return true;
										}
										NBTAPI.removeKey(itemTag, CucumberyTag.CUSTOM_ITEM_TAG_KEY);
										playerInventory.setItemInMainHand(nbtItem.getItem());
										if (!hideOutput)
										{
											MessageUtil.info(player, "주로 사용하는 손에 들고 있는 아이템의 커스텀 아이템 태그를 제거했습니다");
										}
										ItemStackUtil.updateInventory(player);
										return true;
									}
									switch (id)
									{
										case CucumberyTag.CUSTOM_ITEM_RAILGUN_ID:
											break;
										case CucumberyTag.CUSTOM_ITEM_FISHING_LOD_ID:
											if (material != Material.FISHING_ROD)
											{
												MessageUtil.sendError(sender, "해당 태그는 낚싯대에만 사용할 수 있습니다");
												return true;
											}
											break;
										default:
											MessageUtil.wrongArg(sender, 3, args);
											return true;
									}
									if (itemTag == null)
									{
										itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
									}
									if (customItemTag == null)
									{
										customItemTag = itemTag.addCompound(CucumberyTag.CUSTOM_ITEM_TAG_KEY);
									}
									customItemTag.setString(CucumberyTag.ID_KEY, id);
									if (!hideOutput)
									{
										MessageUtil.info(player,
												"주로 사용하는 손에 들고 있는 아이템의 커스텀 아이탬 태그값을 rg255,204;" + id + "&r" + MessageUtil.getFinalConsonant(id, ConsonantType.으로) + " 설정했습니다");
									}
									playerInventory.setItemInMainHand(nbtItem.getItem());
								}
								else
								{
									MessageUtil.longArg(sender, 3, args);
									MessageUtil.commandInfo(sender, label, "customitem setid <아이템 ID|--remove>");
									return true;
								}
								break;
							case "modify":
								if (customItemType == null)
								{
									MessageUtil.sendError(sender, "주로 사용하는 손에 들고 있는 아이템에 커스텀 아이템 태그가 없습니다");
									MessageUtil.commandInfo(sender, label, "customitem setid <아이템 ID|--remove> 명령어로 아이템의 태그를 먼저 추가해주세요.");
									return true;
								}
								switch (customItemType)
								{
									case CucumberyTag.CUSTOM_ITEM_RAILGUN_ID ->
									{
										if (args.length < 4)
										{
											MessageUtil.shortArg(sender, 4, args);
											MessageUtil.commandInfo(sender, label, "customitem modify <키> <값>");
											return true;
										}
										if (args.length > 4)
										{
											MessageUtil.longArg(sender, 4, args);
											MessageUtil.commandInfo(sender, label, "customitem modify <키> <값>");
											return true;
										}
										NBTCompound customItemTagCompound = customItemTag.addCompound(CucumberyTag.TAG_KEY);
										String keyDisplay = args[2].replace("range", "사정 거리").replace("sortparticle", "입자 정렬").replace("ignoreinvincible", "피해 무효 무시")
												.replace("density", "블록당 폭죽 입자 밀도").replace("damage", "대미지").replace("blockpenetrate", "블록 관통")
												.replace("fireworkrocketrequired", "탄환용 폭죽 필요").replace("cooldowntag", "재발사 대기 시간 태그").replace("cooldown", "재발사 대기 시간")
												.replace("piercing", "관통 횟수").replace("laserwidth", "레이저 두께").replace("fireworktype", "폭죽 타입").replace("reverse", "뒤로 발사")
												.replace("suicide", "자살 모드");
										switch (args[2])
										{
											case "range" ->
											{
												if (!MessageUtil.isInteger(sender, args[3], true))
												{
													return true;
												}
												int range = Integer.parseInt(args[3]);
												if (!MessageUtil.checkNumberSize(sender, range, 1, 256))
												{
													return true;
												}
												customItemTagCompound.setInteger(CucumberyTag.CUSTOM_ITEM_RAILGUN_RANGE, range);
											}
											case "density" ->
											{
												if (!MessageUtil.isInteger(sender, args[3], true))
												{
													return true;
												}
												int density = Integer.parseInt(args[3]);
												if (!MessageUtil.checkNumberSize(sender, density, 0, 10))
												{
													return true;
												}
												customItemTagCompound.setInteger(CucumberyTag.CUSTOM_ITEM_RAILGUN_DENSITY, density);
											}
											case "piercing" ->
											{
												if (!MessageUtil.isInteger(sender, args[3], true))
												{
													return true;
												}
												int piercing = Integer.parseInt(args[3]);
												if (!MessageUtil.checkNumberSize(sender, piercing, 0, 100))
												{
													return true;
												}
												customItemTagCompound.setInteger(CucumberyTag.CUSTOM_ITEM_RAILGUN_PIERCING, piercing);
											}
											case "fireworktype" ->
											{
												if (!MessageUtil.isInteger(sender, args[3], true))
												{
													return true;
												}
												int fireworkType = Integer.parseInt(args[3]);
												if (!MessageUtil.checkNumberSize(sender, fireworkType, 1, 10))
												{
													return true;
												}
												customItemTagCompound.setInteger(CucumberyTag.CUSTOM_ITEM_RAILGUN_FIREWORK_TYPE, fireworkType);
											}
											case "damage" ->
											{
												if (!MessageUtil.isDouble(sender, args[3], true))
												{
													return true;
												}
												double damage = Double.parseDouble(args[3]);
												if (!MessageUtil.checkNumberSize(sender, damage, 0, Double.MAX_VALUE))
												{
													return true;
												}
												customItemTagCompound.setDouble(CucumberyTag.CUSTOM_ITEM_RAILGUN_DAMAGE, damage);
											}
											case "cooldown" ->
											{
												if (!MessageUtil.isDouble(sender, args[3], true))
												{
													return true;
												}
												double cooldown = Double.parseDouble(args[3]);
												if (!MessageUtil.checkNumberSize(sender, cooldown, 0, 3600))
												{
													return true;
												}
												customItemTagCompound.setDouble(CucumberyTag.COOLDOWN_KEY, cooldown);
											}
											case "cooldowntag" -> customItemTagCompound.setString(CucumberyTag.CUSTOM_ITEM_RAILGUN_COOLDOWN_TAG, args[3]);
											case "laserwidth" ->
											{
												if (!MessageUtil.isDouble(sender, args[3], true))
												{
													return true;
												}
												double laserWidth = Double.parseDouble(args[3]);
												if (!MessageUtil.checkNumberSize(sender, laserWidth, 0, 100))
												{
													return true;
												}
												customItemTagCompound.setDouble(CucumberyTag.CUSTOM_ITEM_RAILGUN_LASER_WIDTH, laserWidth);
											}
											case "sortparticle", "ignoreinvincible", "blockpenetrate", "fireworkrocketrequired", "reverse", "suicide" ->
											{
												if (!MessageUtil.isBoolean(sender, args, 4, true))
												{
													return true;
												}
												boolean inputBoolean = Boolean.parseBoolean(args[3]);
												if (!inputBoolean)
												{
													if ((args[2].equals("sortparticle") && !customItemTagCompound.hasKey(CucumberyTag.CUSTOM_ITEM_RAILGUN_SORT_PARTICLE)) || (
															args[2].equals("ignoreinvincible") && !customItemTagCompound.hasKey(CucumberyTag.CUSTOM_ITEM_RAILGUN_IGNORE_INVINCIBLE)) || (
															args[2].equals("blockpenetrate") && !customItemTagCompound.hasKey(CucumberyTag.CUSTOM_ITEM_RAILGUN_BLOCK_PENETRATE)) || (
															args[2].equals("fireworkrocketrequired") && !customItemTagCompound.hasKey(
																	CucumberyTag.CUSTOM_ITEM_RAILGUN_FIREWORK_ROCKET_REQUIRED)) || (args[2].equals("reverse") && !customItemTagCompound.hasKey(
															CucumberyTag.CUSTOM_ITEM_RAILGUN_REVERSE)) || (args[2].equals("suicide") && !customItemTagCompound.hasKey(
															CucumberyTag.CUSTOM_ITEM_RAILGUN_SUICIDE)))
													{
														MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 주로 사용하는 손에 들고 있는 아이템의 railgun 커스텀 아이템 태그에 " + keyDisplay + "&r값이 없습니다");
														return true;
													}
													switch (args[2])
													{
														case "sortparticle" -> customItemTagCompound.removeKey(CucumberyTag.CUSTOM_ITEM_RAILGUN_SORT_PARTICLE);
														case "ignoreinvincible" -> customItemTagCompound.removeKey(CucumberyTag.CUSTOM_ITEM_RAILGUN_IGNORE_INVINCIBLE);
														case "blockpenetrate" -> customItemTagCompound.removeKey(CucumberyTag.CUSTOM_ITEM_RAILGUN_BLOCK_PENETRATE);
														case "fireworkrocketrequired" -> customItemTagCompound.removeKey(CucumberyTag.CUSTOM_ITEM_RAILGUN_FIREWORK_ROCKET_REQUIRED);
														case "reverse" -> customItemTagCompound.removeKey(CucumberyTag.CUSTOM_ITEM_RAILGUN_REVERSE);
														case "suicide" -> customItemTagCompound.removeKey(CucumberyTag.CUSTOM_ITEM_RAILGUN_SUICIDE);
													}
												}
												else
												{
													switch (args[2])
													{
														case "sortparticle" -> customItemTagCompound.setBoolean(CucumberyTag.CUSTOM_ITEM_RAILGUN_SORT_PARTICLE, true);
														case "ignoreinvincible" -> customItemTagCompound.setBoolean(CucumberyTag.CUSTOM_ITEM_RAILGUN_IGNORE_INVINCIBLE, true);
														case "blockpenetrate" -> customItemTagCompound.setBoolean(CucumberyTag.CUSTOM_ITEM_RAILGUN_BLOCK_PENETRATE, true);
														case "fireworkrocketrequired" -> customItemTagCompound.setBoolean(CucumberyTag.CUSTOM_ITEM_RAILGUN_FIREWORK_ROCKET_REQUIRED, true);
														case "reverse" -> customItemTagCompound.setBoolean(CucumberyTag.CUSTOM_ITEM_RAILGUN_REVERSE, true);
														case "suicide" -> customItemTagCompound.setBoolean(CucumberyTag.CUSTOM_ITEM_RAILGUN_SUICIDE, true);
													}
												}
											}
											case "particle-type" ->
											{
												Particle particle = Method2.valueOf(args[3].toUpperCase(), Particle.class);
												if (particle == null)
												{
													MessageUtil.wrongArg(sender, 4, args);
													return true;
												}
												customItemTagCompound.setString(CucumberyTag.CUSTOM_ITEM_RAILGUN_PARTICLE_TYPE, particle.toString());
											}
											default ->
											{
												MessageUtil.wrongArg(sender, 3, args);
												return true;
											}
										}
										if (!customItemTagCompound.hasKey(CucumberyTag.CUSTOM_ITEM_RAILGUN_RANGE))
										{
											customItemTagCompound.setInteger(CucumberyTag.CUSTOM_ITEM_RAILGUN_RANGE, 10);
										}
										if (!customItemTagCompound.hasKey(CucumberyTag.COOLDOWN_KEY))
										{
											customItemTagCompound.setDouble(CucumberyTag.COOLDOWN_KEY, 1.5d);
										}
										if (!customItemTagCompound.hasKey(CucumberyTag.CUSTOM_ITEM_RAILGUN_DAMAGE))
										{
											customItemTagCompound.setDouble(CucumberyTag.CUSTOM_ITEM_RAILGUN_DAMAGE, 5d);
										}
										if (!customItemTagCompound.hasKey(CucumberyTag.CUSTOM_ITEM_RAILGUN_COOLDOWN_TAG))
										{
											customItemTagCompound.setString(CucumberyTag.CUSTOM_ITEM_RAILGUN_COOLDOWN_TAG, "railgun-" + System.currentTimeMillis());
										}
										if (!hideOutput)
										{
											MessageUtil.info(player,
													"주로 사용하는 손에 들고 있는 아이템의 railgun 커스텀 아이템 태그의 rg255,204;" + keyDisplay + "&r 값을 rg255,204;" + args[3] + "&r으로 설정했습니다");
										}
										playerInventory.setItemInMainHand(nbtItem.getItem());
									}
									case CucumberyTag.CUSTOM_ITEM_FISHING_LOD_ID ->
									{
										if (material != Material.FISHING_ROD)
										{
											MessageUtil.sendError(sender, "해당 태그는 낚싯대에만 사용할 수 있습니다");
											return true;
										}
										if (args.length < 4)
										{
											MessageUtil.shortArg(sender, 4, args);
											MessageUtil.commandInfo(sender, label, "customitem modify <키> <값>");
											return true;
										}
										if (args.length > 4)
										{
											MessageUtil.longArg(sender, 4, args);
											MessageUtil.commandInfo(sender, label, "customitem modify <키> <값>");
											return true;
										}
										NBTCompound customItemTagCompound = customItemTag.addCompound(CucumberyTag.TAG_KEY);
										NBTCompound maxValuesCompound = customItemTagCompound.getCompound(CucumberyTag.CUSTOM_ITEM_FISHING_ROD_MAX_VALUES);

										switch (args[2])
										{
											case "multiplier" ->
											{
												if (!MessageUtil.isDouble(sender, args[3], true))
												{
													return true;
												}
												double input = Double.parseDouble(args[3]);
												if (!MessageUtil.checkNumberSize(sender, input, 0d, 5d))
												{
													return true;
												}
												customItemTagCompound.setDouble(CucumberyTag.CUSTOM_ITEM_FISHING_ROD_MULTIPLIER, input);
											}
											case "x", "y", "z" ->
											{
												if (!MessageUtil.isDouble(sender, args[3], true))
												{
													return true;
												}
												double input = Double.parseDouble(args[3]);
												if (!MessageUtil.checkNumberSize(sender, input, 0d, 5d))
												{
													return true;
												}
												if (maxValuesCompound == null)
												{
													maxValuesCompound = customItemTagCompound.addCompound(CucumberyTag.CUSTOM_ITEM_FISHING_ROD_MAX_VALUES);
												}
												maxValuesCompound.setDouble(args[2], input);
											}
											case "allow-on-air" ->
											{
												if (!MessageUtil.isBoolean(sender, args, 4, true))
												{
													return true;
												}
												boolean input = Boolean.parseBoolean(args[3]);
												customItemTagCompound.setBoolean(CucumberyTag.CUSTOM_ITEM_FISHING_ROD_ALLOW_ON_AIR, input);
											}
											default ->
											{
												MessageUtil.wrongArg(sender, 3, args);
												return true;
											}
										}
										if (!hideOutput)
										{
											MessageUtil.info(player,
													"주로 사용하는 손에 들고 있는 낚싯대의 fishingrod 커스텀 아이템 태그의 rg255,204;" + args[2] + "&r 값을 rg255,204;" + args[3] + "&r으로 설정했습니다");
										}
										playerInventory.setItemInMainHand(nbtItem.getItem());
									}
								}
								break;
							default:
								MessageUtil.wrongArg(sender, 2, args);
								MessageUtil.commandInfo(sender, label, "customitem <setid|modify> ...");
								return true;
						}
					}
				}
				case "food" ->
				{
					if (!ItemStackUtil.isEdible(material))
					{
						MessageUtil.sendError(sender, "해당 태그는 먹을 수 있는 아이템에만 사용할 수 있습니다");
						return true;
					}
					if (args.length < 2)
					{
						MessageUtil.shortArg(sender, 3, args);
						MessageUtil.commandInfo(sender, label, "food <disable-status-effect|food-level|saturation> ...");
						return true;
					}
					NBTCompound foodTag = NBTAPI.getCompound(itemTag, CucumberyTag.FOOD_KEY);
					switch (args[1])
					{
						case "disable-status-effect" ->
						{
							if (!ItemStackUtil.hasStatusEffect(material))
							{
								MessageUtil.sendError(sender, "해당 태그는 상태 효과에 영향을 줄 수 있는 아이템에만 사용할 수 있습니다");
								return true;
							}
							if (args.length < 3)
							{
								MessageUtil.shortArg(sender, 3, args);
								MessageUtil.commandInfo(sender, label, "food disable-status-effect <섭취 시 상태 효과 미적용 여부>");
								return true;
							}
							if (args.length > 3)
							{
								MessageUtil.longArg(sender, 3, args);
								MessageUtil.commandInfo(sender, label, "food disable-status-effect <섭취 시 상태 효과 미적용 여부>");
								return true;
							}
							if (!MessageUtil.isBoolean(sender, args, 3, true))
							{
								return true;
							}
							boolean input = Boolean.parseBoolean(args[2]);
							if (!input)
							{
								if (foodTag == null || !foodTag.hasTag(CucumberyTag.FOOD_DISABLE_STATUS_EFFECT_KEY))
								{
									MessageUtil.sendError(sender, "주로 사용하는 손에 들고 있는 아이템에는 섭취 시 상태 효과 미적용 여부 태그가 없습니다");
									return true;
								}

								NBTAPI.removeKey(foodTag, CucumberyTag.FOOD_DISABLE_STATUS_EFFECT_KEY);
								playerInventory.setItemInMainHand(nbtItem.getItem());
								ItemStackUtil.updateInventory(player);
								MessageUtil.info(sender, "주로 사용하는 손에 들고 있는 아이템에서 섭취 시 상태 효과 미적용 여부 태그를 제거했습니다");
								return true;
							}
							if (itemTag == null)
							{
								itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
							}
							if (foodTag == null)
							{
								foodTag = itemTag.addCompound(CucumberyTag.FOOD_KEY);
							}
							if (foodTag.getBoolean(CucumberyTag.FOOD_DISABLE_STATUS_EFFECT_KEY))
							{
								MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 주로 사용하는 손에 들고 있는 아이템에 섭취 시 상태 효과 미적용 여부 태그가 있습니다");
								return true;
							}
							foodTag.setBoolean(CucumberyTag.FOOD_DISABLE_STATUS_EFFECT_KEY, true);
							playerInventory.setItemInMainHand(nbtItem.getItem());
							ItemStackUtil.updateInventory(player);
							MessageUtil.info(sender, "주로 사용하는 손에 들고 있는 아이템에 섭취 시 상태 효과 미적용 여부 태그를 추가했습니다");
						}
						case "food-level" ->
						{
							if (args.length < 3)
							{
								MessageUtil.shortArg(sender, 3, args);
								MessageUtil.commandInfo(sender, label, "food food-level <음식 포인트|--remove>");
								return true;
							}
							if (args.length > 3)
							{
								MessageUtil.longArg(sender, 3, args);
								MessageUtil.commandInfo(sender, label, "food food-level <음식 포인트|--remove>");
								return true;
							}
							if (args[2].equals("--remove"))
							{
								if (foodTag == null || !foodTag.hasTag(CucumberyTag.FOOD_LEVEL_KEY))
								{
									MessageUtil.sendError(sender, "주로 사용하는 손에 들고 있는 아이템에는 음식 포인트 태그가 없습니다");
									return true;
								}
								NBTAPI.removeKey(foodTag, CucumberyTag.FOOD_LEVEL_KEY);
								playerInventory.setItemInMainHand(nbtItem.getItem());
								ItemStackUtil.updateInventory(player);
								MessageUtil.info(sender, "주로 사용하는 손에 들고 있는 아이템에서 음식 포인트 태그를 제거했습니다");
								return true;
							}
							if (!MessageUtil.isInteger(sender, args[2], true))
							{
								return true;
							}
							int input = Integer.parseInt(args[2]);
							if (!MessageUtil.checkNumberSize(sender, input, -20, 20))
							{
								return true;
							}
							if (itemTag == null)
							{
								itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
							}
							if (foodTag == null)
							{
								foodTag = itemTag.addCompound(CucumberyTag.FOOD_KEY);
							}
							if (foodTag.hasTag(CucumberyTag.FOOD_LEVEL_KEY) && foodTag.getInteger(CucumberyTag.FOOD_LEVEL_KEY) == input)
							{
								MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 주로 사용하는 손에 들고 있는 아이템의 음식 포인트 태그 값이 rg255,204;" + input + "&r입니다");
								return true;
							}
							foodTag.setInteger(CucumberyTag.FOOD_LEVEL_KEY, input);
							playerInventory.setItemInMainHand(nbtItem.getItem());
							ItemStackUtil.updateInventory(player);
							MessageUtil.info(sender, "주로 사용하는 손에 들고 있는 아이템의 음식 포인트 태그 값을 rg255,204;" + input + "&r으로 설정했습니다");
						}
						case "saturation" ->
						{
							if (args.length < 3)
							{
								MessageUtil.shortArg(sender, 3, args);
								MessageUtil.commandInfo(sender, label, "food saturation <포화도|--remove>");
								return true;
							}
							if (args.length > 3)
							{
								MessageUtil.longArg(sender, 3, args);
								MessageUtil.commandInfo(sender, label, "food saturation <포화도|--remove>");
								return true;
							}
							if (args[2].equals("--remove"))
							{
								if (foodTag == null || !foodTag.hasTag(CucumberyTag.SATURATION_KEY))
								{
									MessageUtil.sendError(sender, "주로 사용하는 손에 들고 있는 아이템에는 포화도 태그가 없습니다");
									return true;
								}
								NBTAPI.removeKey(foodTag, CucumberyTag.SATURATION_KEY);
								playerInventory.setItemInMainHand(nbtItem.getItem());
								ItemStackUtil.updateInventory(player);
								MessageUtil.info(sender, "주로 사용하는 손에 들고 있는 아이템에서 포화도 태그를 제거했습니다");
								return true;
							}
							if (!MessageUtil.isDouble(sender, args[2], true))
							{
								return true;
							}
							double input = Double.parseDouble(args[2]);
							if (!MessageUtil.checkNumberSize(sender, input, -20, 20))
							{
								return true;
							}
							if (itemTag == null)
							{
								itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
							}
							if (foodTag == null)
							{
								foodTag = itemTag.addCompound(CucumberyTag.FOOD_KEY);
							}
							if (foodTag.hasTag(CucumberyTag.SATURATION_KEY) && foodTag.getInteger(CucumberyTag.SATURATION_KEY) == input)
							{
								MessageUtil.sendError(sender, "변경 사항이 없습니다. 이미 주로 사용하는 손에 들고 있는 아이템의 포화도 태그 값이 rg255,204;" + Constant.Sosu15.format(input) + "&r입니다");
								return true;
							}
							foodTag.setDouble(CucumberyTag.SATURATION_KEY, input);
							playerInventory.setItemInMainHand(nbtItem.getItem());
							ItemStackUtil.updateInventory(player);
							MessageUtil.info(sender, "주로 사용하는 손에 들고 있는 아이템의 포화도 태그 값을 rg255,204;" + Constant.Sosu15.format(input) + "&r으로 설정했습니다");
						}
						case "nourishment" ->
						{
							if (args.length < 3)
							{
								MessageUtil.shortArg(sender, 3, args);
								MessageUtil.commandInfo(sender, label, "food saturation <든든함|--remove>");
								return true;
							}
							if (args.length == 3 && args[2].equals("--remove"))
							{
								if (foodTag == null || !foodTag.hasTag(CucumberyTag.NOURISHMENT_KEY))
								{
									MessageUtil.sendError(sender, "주로 사용하는 손에 들고 있는 아이템에는 든든함 태그가 없습니다");
									return true;
								}
								NBTAPI.removeKey(foodTag, CucumberyTag.NOURISHMENT_KEY);
								playerInventory.setItemInMainHand(nbtItem.getItem());
								ItemStackUtil.updateInventory(player);
								MessageUtil.info(sender, "주로 사용하는 손에 들고 있는 아이템에서 든든함 태그를 제거했습니다");
								return true;
							}
							String input = MessageUtil.listToString(" ", 2, args.length, args);
							if (itemTag == null)
							{
								itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
							}
							if (foodTag == null)
							{
								foodTag = itemTag.addCompound(CucumberyTag.FOOD_KEY);
							}
							foodTag.setString(CucumberyTag.NOURISHMENT_KEY, input);
							playerInventory.setItemInMainHand(nbtItem.getItem());
							ItemStackUtil.updateInventory(player);
							MessageUtil.info(sender,
									"주로 사용하는 손에 들고 있는 아이템의 든든함 태그 값을 rg255,204;" + input + "&r" + MessageUtil.getFinalConsonant(input, ConsonantType.으로) + " 설정했습니다");
						}
						default ->
						{
							MessageUtil.wrongArg(sender, 2, args);
							MessageUtil.commandInfo(sender, label, "food <disable-status-effect|food-level|saturation|nourishment> ...");
							return true;
						}
					}
				}
				case "customtag" ->
				{
					if (args.length < 2)
					{
						MessageUtil.shortArg(sender, 3, args);
						MessageUtil.commandInfo(sender, label, "customtag <list|add|remove> ...");
						return true;
					}
					NBTCompound tmiTag = nbtItem.getCompound(CucumberyTag.KEY_TMI);
					NBTCompound customTag = NBTAPI.getCompound(tmiTag, CucumberyTag.TMI_CUSTOM_TAGS);
					NBTCompound vanillaTag = NBTAPI.getCompound(tmiTag, CucumberyTag.TMI_VANILLA_TAGS);
					switch (args[1])
					{
						case "list" ->
						{
							if (customTag == null)
							{
								if (vanillaTag == null)
								{
									MessageUtil.sendError(player, "no custom neither vanlla tags");
									return true;
								}
								Set<String> keys = vanillaTag.getKeys();
								List<String> newKeys = new ArrayList<>();
								for (String key : keys)
								{
									if (vanillaTag.getBoolean(key))
									{
										newKeys.add(key);
									}
								}
								MessageUtil.info(player, "there are ", newKeys.size(), " vanilla tags and no custom tags");
								MessageUtil.info(player, "&a" + MessageUtil.listToString("&7, &a", Method.listToArray(newKeys)));
								return true;
							}
							Set<String> keys = customTag.getKeys();
							List<String> newKeys = new ArrayList<>();
							for (String key : keys)
							{
								if (customTag.getBoolean(key))
								{
									newKeys.add(key);
								}
							}
							MessageUtil.info(player, "there are ", newKeys.size(), " custom tags");
							StringBuilder key = new StringBuilder();
							List<Component> components = new ArrayList<>();
							for (String k : newKeys)
							{
								key.append("%s, ");
								components.add(Component.text(k, NamedTextColor.GREEN).hoverEvent(ComponentUtil.translate("click to remove %s tag", k))
										.clickEvent(ClickEvent.suggestCommand("/itag customtag remove " + k)));
							}
							key = new StringBuilder(key.substring(0, key.length() - 2));
							MessageUtil.info(player, ComponentUtil.translate(key.toString(), components));
						}
						case "add" ->
						{
							if (args.length < 3)
							{
								MessageUtil.shortArg(sender, 3, args);
								MessageUtil.commandInfo(sender, label, "customtag add <new key>");
								return true;
							}
							String input = args[2];
							if (tmiTag == null)
							{
								tmiTag = nbtItem.addCompound(CucumberyTag.KEY_TMI);
							}
							if (customTag == null)
							{
								customTag = tmiTag.addCompound(CucumberyTag.TMI_CUSTOM_TAGS);
							}

							Set<String> keys = customTag.getKeys();
							if (keys.contains(input) && customTag.getBoolean(input))
							{
								MessageUtil.sendError(player, "there already is tag called ", input);
								return true;
							}
							customTag.setBoolean(input, true);

							MessageUtil.info(player, "tag ", input, " added");
							player.getInventory().setItemInMainHand(nbtItem.getItem());
							ItemStackUtil.updateInventory(player);

						}
						case "remove" ->
						{
							if (args.length < 3)
							{
								MessageUtil.shortArg(sender, 3, args);
								MessageUtil.commandInfo(sender, label, "customtag remove <key>");
								return true;
							}
							if (customTag == null)
							{
								MessageUtil.sendError(player, "there are no custom tags!");
								return true;
							}
							Set<String> keys = customTag.getKeys();
							String input = args[2];
							if (!keys.contains(input))
							{
								MessageUtil.sendError(player, "there is no tag called ", input);
								return true;
							}
							customTag.removeKey(input);
							if (customTag.getKeys().isEmpty())
							{
								tmiTag.removeKey(CucumberyTag.TMI_CUSTOM_TAGS);
							}
							if (tmiTag.getKeys().isEmpty())
							{
								nbtItem.removeKey(CucumberyTag.KEY_TMI);
							}
							MessageUtil.info(player, "tag ", input, " removed");
							player.getInventory().setItemInMainHand(nbtItem.getItem());
							ItemStackUtil.updateInventory(player);
						}
					}
				}
				case "potion" ->
				{
					final boolean isPotionType =
							material == Material.POTION || material == Material.SPLASH_POTION || material == Material.LINGERING_POTION || material == Material.TIPPED_ARROW
									|| ItemStackUtil.isEdible(material);
					if (!isPotionType)
					{
						MessageUtil.sendError(sender, ComponentUtil.translate("해당 태그는 음식 또는 포션 유형의 아이템에만 사용할 수 있습니다"));
						return true;
					}
					if (args.length < 2)
					{
						MessageUtil.shortArg(sender, 2, args);
						MessageUtil.commandInfo(sender, label, " potion <list|add|remove|set>");
						return true;
					}
					NBTCompoundList potionListTag = NBTAPI.getCompoundList(itemTag, CucumberyTag.CUSTOM_EFFECTS);
					switch (args[1])
					{
						case "list" ->
						{
							if (args.length > 2)
							{
								MessageUtil.longArg(sender, 2, args);
								MessageUtil.commandInfo(sender, label, " potion list");
								return true;
							}
							if (potionListTag == null || potionListTag.isEmpty())
							{
								MessageUtil.sendError(sender, ComponentUtil.translate("%s에는 포션 태그가 없습니다", item));
								return true;
							}
							MessageUtil.info(sender, ComponentUtil.translate("%s에는 포션 태그가 %s개 있습니다", item, potionListTag.size()));
							for (ReadWriteNBT nbtCompound : potionListTag)
							{
								try
								{
									String rawKey = nbtCompound.getString(CucumberyTag.CUSTOM_EFFECTS_ID);
									String[] rawKeySplit = rawKey.split(":");
									CustomEffectType customEffectType = CustomEffectType.getByKey(new NamespacedKey(rawKeySplit[0], rawKeySplit[1]));
									int duration = nbtCompound.getInteger(CucumberyTag.CUSTOM_EFFECTS_DURATION), amplifier = nbtCompound.getInteger(
											CucumberyTag.CUSTOM_EFFECTS_AMPLIFIER);
									String displayType = nbtCompound.getString(CucumberyTag.CUSTOM_EFFECTS_DISPLAY_TYPE);
									Component component = ComponentUtil.create(new CustomEffect(customEffectType, duration, amplifier, DisplayType.valueOf(displayType)));
									MessageUtil.info(sender, component, ComponentUtil.translate("(%s)", displayType));
								}
								catch (Exception e)
								{
									MessageUtil.info(sender, ComponentUtil.translate("잘못된 포션 태그입니다: %s", nbtCompound.toString()));
								}
							}
						}
						case "remove" ->
						{
							if (args.length > 3)
							{
								MessageUtil.longArg(sender, 6, args);
								MessageUtil.commandInfo(sender, label, " potion remove [줄]");
								return true;
							}
							if (potionListTag == null || potionListTag.isEmpty())
							{
								MessageUtil.sendError(sender, ComponentUtil.translate("%s에는 포션 태그가 없습니다", item));
								return true;
							}
							int line = potionListTag.size();
							if (args.length == 3)
							{
								if (!MessageUtil.isInteger(sender, args[2], true))
								{
									return true;
								}
								line = Integer.parseInt(args[2]);
							}
							if (!MessageUtil.checkNumberSize(sender, line, 1, potionListTag.size(), true))
							{
								return true;
							}
							CustomEffect customEffect = null;
							try
							{
								NBTCompound potionTag = potionListTag.get(line - 1);
								String rawKey = potionTag.getString(CucumberyTag.CUSTOM_EFFECTS_ID);
								String[] rawKeySplit = rawKey.split(":");
								CustomEffectType customEffectType = CustomEffectType.getByKey(new NamespacedKey(rawKeySplit[0], rawKeySplit[1]));
								customEffect = new CustomEffect(customEffectType, potionTag.getInteger(CucumberyTag.CUSTOM_EFFECTS_DURATION),
										potionTag.getInteger(CucumberyTag.CUSTOM_EFFECTS_AMPLIFIER),
										DisplayType.valueOf(potionTag.getString(CucumberyTag.CUSTOM_EFFECTS_DISPLAY_TYPE).toUpperCase()));
							}
							catch (Exception ignored)
							{

							}
							potionListTag.remove(line - 1);
							playerInventory.setItemInMainHand(nbtItem.getItem());
							ItemStackUtil.updateInventory(player);
							MessageUtil.info(sender, ComponentUtil.translate("%s에 있는 %s번째 효과(%s)를 제거했습니다 (남은 효과 개수 : %s개)", item, line,
									customEffect != null ? customEffect : ComponentUtil.translate("&c잘못된 효과입니다!"), potionListTag.size()));
						}
						case "set" ->
						{

						}
						case "add" ->
						{
							if (args.length > 6)
							{
								MessageUtil.longArg(sender, 6, args);
								MessageUtil.commandInfo(sender, label, " potion add <효과> [지속 시간(초)] [농도 레벨] [표시 유형]");
								return true;
							}
							CustomEffectType customEffectType;
							try
							{
								String[] rawKeySplit = args[2].split(":");
								customEffectType = CustomEffectType.getByKey(new NamespacedKey(rawKeySplit[0], rawKeySplit[1]));
								if (customEffectType == null)
								{
									throw new Exception();
								}
							}
							catch (Exception e)
							{
								MessageUtil.wrongArg(sender, 3, args);
								return true;
							}
							int duration = customEffectType.getDefaultDuration(), amplifier = 0;
							DisplayType displayType = customEffectType.getDefaultDisplayType();
							if (args.length >= 4)
							{
								if (args[3].equals("infinite"))
								{
									duration = -1;
								}
								else if (!args[3].equals("default"))
								{
									if (MessageUtil.isDouble(sender, args[3], true))
									{
										double input = Double.parseDouble(args[3]);
										if (!MessageUtil.checkNumberSize(sender, input, 0.05, Integer.MAX_VALUE / 20d, true))
										{
											return true;
										}
										duration = (int) (input * 20);
									}
								}
							}
							if (args.length >= 5)
							{
								if (args[4].equals("max"))
								{
									amplifier = customEffectType.getMaxAmplifier();
								}
								else if (MessageUtil.isInteger(sender, args[4], true))
								{
									amplifier = Integer.parseInt(args[4]);
									if (!MessageUtil.checkNumberSize(sender, amplifier, 0, customEffectType.getMaxAmplifier(), true))
									{
										return true;
									}
								}
								else
								{
									return true;
								}
							}
							if (args.length == 6)
							{
								try
								{
									displayType = DisplayType.valueOf(args[5].toUpperCase());
								}
								catch (Exception e)
								{
									MessageUtil.wrongArg(sender, 5, args);
									return true;
								}
							}
							if (potionListTag != null)
							{
								for (ReadWriteNBT nbtCompound : potionListTag)
								{
									String id = nbtCompound.getString(CucumberyTag.ID_KEY);
									Integer origiAamplifier = nbtCompound.getInteger(CucumberyTag.CUSTOM_EFFECTS_AMPLIFIER);
									if (customEffectType.toString().equals(id) && origiAamplifier != null && origiAamplifier.equals(amplifier))
									{
										MessageUtil.sendError(sender, ComponentUtil.translate("%s에는 이미 %s 효과가 있습니다", item, customEffectType));
										return true;
									}
								}
							}
							if (itemTag == null)
							{
								itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
							}
							if (potionListTag == null)
							{
								potionListTag = itemTag.getCompoundList(CucumberyTag.CUSTOM_EFFECTS);
							}
							NBTCompound nbtCompound = new NBTContainer();
							nbtCompound.setString(CucumberyTag.CUSTOM_EFFECTS_ID, customEffectType.toString());
							nbtCompound.setInteger(CucumberyTag.CUSTOM_EFFECTS_DURATION, duration);
							nbtCompound.setInteger(CucumberyTag.CUSTOM_EFFECTS_AMPLIFIER, amplifier);
							nbtCompound.setString(CucumberyTag.CUSTOM_EFFECTS_DISPLAY_TYPE, displayType.toString());
							potionListTag.addCompound(nbtCompound);
							playerInventory.setItemInMainHand(nbtItem.getItem());
							ItemStackUtil.updateInventory(player);
							MessageUtil.info(sender, ComponentUtil.translate("%s에 %s 효과를 추가했습니다 (총 %s개 효과 보유)", nbtItem.getItem(),
									new CustomEffect(customEffectType, duration, amplifier, displayType), potionListTag.size()));
						}
					}
				}
				case "customdisplayname" ->
				{
					if (args.length < 2)
					{
						MessageUtil.shortArg(sender, 2, args);
						MessageUtil.commandInfo(sender, label, "customdisplayname <prefix|suffix|name|remove>");
						return true;
					}
					NBTCompound displayCompound = NBTAPI.getCompound(itemTag, CucumberyTag.ITEMSTACK_DISPLAY_KEY);
					switch (args[1])
					{
						case "prefix" ->
						{
							NBTCompoundList prefixes = NBTAPI.getCompoundList(displayCompound, CucumberyTag.ITEMSTACK_DISPLAY_PREFIX);
							if (args.length < 3)
							{
								MessageUtil.shortArg(sender, 3, args);
								MessageUtil.commandInfo(sender, label, "customdisplayname prefix <add|remove|set|insert|list>");
								return true;
							}
							switch (args[2])
							{
								case "list" ->
								{
									if (args.length > 3)
									{
										MessageUtil.longArg(sender, 3, args);
										MessageUtil.commandInfo(sender, label, "customdisplayname prefix list");
										return true;
									}
									if (prefixes == null || prefixes.isEmpty())
									{
										MessageUtil.sendError(sender, "%s에는 접두어가 없습니다", item);
										return true;
									}
									MessageUtil.info(sender, "%s에는 접두어가 %s개 있습니다", item, prefixes.size());
									for (int i = 0; i < prefixes.size(); i++)
									{
										NBTCompound nbtCompound = prefixes.get(i);
										String text = nbtCompound.getString("text");
										if (text != null)
										{
											Component message = Component.text(text);
											message = message.hoverEvent(HoverEvent.showText(ComponentUtil.translate("클릭하여 %1$s번째 접두어를 수정합니다\n우클릭하여 채팅창에 %1$s번째 접두어를 삽입합니다", i + 1)));
											message = message.clickEvent(ClickEvent.suggestCommand("/itag customdisplayname prefix set " + (i + 1) + " "));
											message = message.insertion(text);
											NBTCompound nbt = nbtCompound.getCompound("nbt");
											MessageUtil.info(sender, "%s번째 접두어 : %s" + (nbt != null ? ", nbt : %s" : ""), i + 1, message, nbt != null ? nbt.toString() : "");
										}
										else
										{
											MessageUtil.info(sender, "잘못된 %s번째 접두어입니다", i + 1);
										}
									}
								}
								case "add" ->
								{
									if (args.length < 4)
									{
										MessageUtil.shortArg(sender, 4, args);
										MessageUtil.commandInfo(sender, label, "customdisplayname prefix add <접두어> [nbt]");
										return true;
									}
									if (args.length > 5)
									{
										MessageUtil.longArg(sender, 5, args);
										MessageUtil.commandInfo(sender, label, "customdisplayname prefix add <접두어> [nbt]");
										return true;
									}
									String prefix = args[3];
									String nbt = null;
									if (args.length == 5)
									{
										try
										{
											nbt = args[4];
											new NBTContainer(nbt);
										}
										catch (Exception exception)
										{
											MessageUtil.sendError(sender, "잘못된 NBT입니다: %s", nbt);
											return true;
										}
									}
									if (itemTag == null)
									{
										itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
									}
									if (displayCompound == null)
									{
										displayCompound = itemTag.addCompound(CucumberyTag.ITEMSTACK_DISPLAY_KEY);
									}
									if (prefixes == null)
									{
										prefixes = displayCompound.getCompoundList(CucumberyTag.ITEMSTACK_DISPLAY_PREFIX);
									}
									NBTCompound compound = prefixes.addCompound();
									compound.setString("text", prefix);
									if (nbt != null)
									{
										NBTCompound nbtCompound = compound.addCompound("nbt");
										nbtCompound.mergeCompound(new NBTContainer(nbt));
									}
									String originalName = displayCompound.getString(CucumberyTag.ORIGINAL_NAME);
									ItemStack itemStack = nbtItem.getItem();
									ItemMeta itemMeta = itemStack.getItemMeta();
									Component displayName = itemMeta.displayName();
									if ((originalName == null || originalName.equals("")) && displayName != null && !(displayName instanceof TranslatableComponent t
											&& t.args().size() == 4 && t.args().get(3) instanceof TextComponent c && c.content().equals("Custom Display")))
									{
										originalName = ComponentUtil.serializeAsJson(displayName);
										displayCompound.setString(CucumberyTag.ORIGINAL_NAME, originalName);
										itemStack = nbtItem.getItem();
										itemMeta = itemStack.getItemMeta();
									}
									itemMeta.displayName(originalName == null || originalName.equals("") ? null : ComponentUtil.create(originalName));
									itemStack.setItemMeta(itemMeta);
									ItemLore.setItemLore(itemStack);
									playerInventory.setItemInMainHand(itemStack);
									Component prefixComponent = ComponentUtil.create(prefix);
									if (prefixComponent.color() == null)
									{
										prefixComponent = prefixComponent.color(Constant.THE_COLOR);
									}
									MessageUtil.info(sender, "아이템에 %s번째 접두어 %s을(를) 추가했습니다. 전 : %s, 후 : %s" + (nbt != null ? ", nbt : %s" : ""), prefixes.size(), prefixComponent,
											item, itemStack, nbt + "");
								}
								case "remove" ->
								{
									if (args.length > 4)
									{
										MessageUtil.longArg(sender, 4, args);
										MessageUtil.commandInfo(sender, label, "customdisplayname prefix remove [줄|--all]");
										return true;
									}
									if (prefixes == null || prefixes.isEmpty())
									{
										MessageUtil.sendError(sender, "%s에는 접두어가 없습니다", item);
										return true;
									}
									int line = prefixes.size() - 1;
									if (args.length == 4)
									{
										if (args[3].equals("--all"))
										{
											line = -1;
										}
										else
										{
											if (!MessageUtil.isInteger(sender, args[3], true))
											{
												return true;
											}
											line = Integer.parseInt(args[3]);
											if (!MessageUtil.checkNumberSize(sender, line, 1, prefixes.size(), true))
											{
												return true;
											}
											line--;
										}
									}
									String originalName = displayCompound.getString(CucumberyTag.ORIGINAL_NAME);
									if (line == -1 || prefixes.size() == 1)
									{
										NBTAPI.removeKey(displayCompound, CucumberyTag.ITEMSTACK_DISPLAY_PREFIX);
										ItemStack itemStack = nbtItem.getItem();
										ItemMeta itemMeta = itemStack.getItemMeta();
										itemMeta.displayName(originalName == null || originalName.equals("") ? null : ComponentUtil.create(originalName));
										itemStack.setItemMeta(itemMeta);
										ItemLore.setItemLore(itemStack);
										playerInventory.setItemInMainHand(itemStack);
										MessageUtil.info(sender, "아이템의 모든 접두어를 제거했습니다. 전 : %s, 후 : %s", item, itemStack);
										return true;
									}
									String text = prefixes.get(line).getString("text");
									if (text == null)
									{
										text = "translate:&c잘못된 접두어";
									}
									Component prefixComponent = ComponentUtil.create(text);
									if (prefixComponent.color() == null)
									{
										prefixComponent = prefixComponent.color(Constant.THE_COLOR);
									}
									prefixes.remove(line);
									ItemStack itemStack = nbtItem.getItem();
									ItemMeta itemMeta = itemStack.getItemMeta();
									itemMeta.displayName(originalName == null || originalName.equals("") ? null : ComponentUtil.create(originalName));
									itemStack.setItemMeta(itemMeta);
									ItemLore.setItemLore(itemStack);
									playerInventory.setItemInMainHand(itemStack);
									MessageUtil.info(sender, "아이템의 %s번째 접두어 %s을(를) 제거했습니다. 전 : %s, 후 : %s", line + 1, prefixComponent, item, itemStack);
								}
								case "set" ->
								{
									if (args.length < 5)
									{
										MessageUtil.shortArg(sender, 5, args);
										MessageUtil.commandInfo(sender, label, "customdisplayname prefix set <줄> <접두어> [nbt]");
										return true;
									}
									if (args.length > 6)
									{
										MessageUtil.longArg(sender, 6, args);
										MessageUtil.commandInfo(sender, label, "customdisplayname prefix set <줄> <접두어> [nbt]");
										return true;
									}
									if (prefixes == null || prefixes.isEmpty())
									{
										MessageUtil.sendError(sender, "%s에는 접두어가 없습니다", item);
										return true;
									}
									if (!MessageUtil.isInteger(sender, args[3], true))
									{
										return true;
									}
									int line = Integer.parseInt(args[3]);
									if (!MessageUtil.checkNumberSize(sender, line, 1, prefixes.size()))
									{
										return true;
									}
									line--;
									String prefix = args[4];
									String nbt = null;
									if (args.length == 6)
									{
										try
										{
											nbt = args[5];
											new NBTContainer(nbt);
										}
										catch (Exception exception)
										{
											MessageUtil.sendError(sender, "잘못된 NBT입니다: %s", nbt);
											return true;
										}
									}
									NBTCompound compound = prefixes.get(line);
									compound.setString("text", prefix);
									if (nbt != null)
									{
										NBTCompound nbtCompound = compound.addCompound("nbt");
										nbtCompound.mergeCompound(new NBTContainer(nbt));
									}
									String originalName = displayCompound.getString(CucumberyTag.ORIGINAL_NAME);
									ItemStack itemStack = nbtItem.getItem();
									ItemMeta itemMeta = itemStack.getItemMeta();
									itemMeta.displayName(originalName == null || originalName.equals("") ? null : ComponentUtil.create(originalName));
									itemStack.setItemMeta(itemMeta);
									ItemLore.setItemLore(itemStack);
									playerInventory.setItemInMainHand(itemStack);
									Component prefixComponent = ComponentUtil.create(prefix);
									if (prefixComponent.color() == null)
									{
										prefixComponent = prefixComponent.color(Constant.THE_COLOR);
									}
									MessageUtil.info(sender, "아이템의 %s번째 접두어를 %s(으)로 변경했습니다. 전 : %s, 후 : %s" + (nbt != null ? ", nbt : %s" : ""), line + 1, prefixComponent, item,
											itemStack, nbt + "");
								}
								default ->
								{
									MessageUtil.wrongArg(sender, 3, args);
									MessageUtil.commandInfo(sender, label, "customdisplayname prefix <add|remove|set|list>");
									return true;
								}
							}
						}
						case "suffix" ->
						{
							NBTCompoundList suffixes = NBTAPI.getCompoundList(displayCompound, CucumberyTag.ITEMSTACK_DISPLAY_SUFFIX);
							if (args.length < 3)
							{
								MessageUtil.shortArg(sender, 3, args);
								MessageUtil.commandInfo(sender, label, "customdisplayname suffix <add|remove|set|insert|list>");
								return true;
							}
							switch (args[2])
							{
								case "list" ->
								{
									if (args.length > 3)
									{
										MessageUtil.longArg(sender, 3, args);
										MessageUtil.commandInfo(sender, label, "customdisplayname suffix list");
										return true;
									}
									if (suffixes == null || suffixes.isEmpty())
									{
										MessageUtil.sendError(sender, "%s에는 접미어가 없습니다", item);
										return true;
									}
									MessageUtil.info(sender, "%s에는 접미어가 %s개 있습니다", item, suffixes.size());
									for (int i = 0; i < suffixes.size(); i++)
									{
										NBTCompound nbtCompound = suffixes.get(i);
										String text = nbtCompound.getString("text");
										if (text != null)
										{
											Component message = Component.text(text);
											message = message.hoverEvent(HoverEvent.showText(ComponentUtil.translate("클릭하여 %1$s번째 접미어를 수정합니다\n우클릭하여 채팅창에 %1$s번째 접미어를 삽입합니다", i + 1)));
											message = message.clickEvent(ClickEvent.suggestCommand("/itag customdisplayname suffix set " + (i + 1) + " "));
											message = message.insertion(text);
											NBTCompound nbt = nbtCompound.getCompound("nbt");
											MessageUtil.info(sender, "%s번째 접미어 : %s" + (nbt != null ? ", nbt : %s" : ""), i + 1, message, nbt != null ? nbt.toString() : "");
										}
										else
										{
											MessageUtil.info(sender, "잘못된 %s번째 접미어입니다", i + 1);
										}
									}
								}
								case "add" ->
								{
									if (args.length < 4)
									{
										MessageUtil.shortArg(sender, 4, args);
										MessageUtil.commandInfo(sender, label, "customdisplayname suffix add <접미어> [nbt]");
										return true;
									}
									if (args.length > 5)
									{
										MessageUtil.longArg(sender, 5, args);
										MessageUtil.commandInfo(sender, label, "customdisplayname suffix add <접미어> [nbt]");
										return true;
									}
									String suffix = args[3];
									String nbt = null;
									if (args.length == 5)
									{
										try
										{
											nbt = args[4];
											new NBTContainer(nbt);
										}
										catch (Exception exception)
										{
											MessageUtil.sendError(sender, "잘못된 NBT입니다: %s", nbt);
											return true;
										}
									}
									if (itemTag == null)
									{
										itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
									}
									if (displayCompound == null)
									{
										displayCompound = itemTag.addCompound(CucumberyTag.ITEMSTACK_DISPLAY_KEY);
									}
									if (suffixes == null)
									{
										suffixes = displayCompound.getCompoundList(CucumberyTag.ITEMSTACK_DISPLAY_SUFFIX);
									}
									NBTCompound compound = suffixes.addCompound();
									compound.setString("text", suffix);
									if (nbt != null)
									{
										NBTCompound nbtCompound = compound.addCompound("nbt");
										nbtCompound.mergeCompound(new NBTContainer(nbt));
									}
									String originalName = displayCompound.getString(CucumberyTag.ORIGINAL_NAME);
									ItemStack itemStack = nbtItem.getItem();
									ItemMeta itemMeta = itemStack.getItemMeta();
									Component displayName = itemMeta.displayName();
									if ((originalName == null || originalName.equals("")) && displayName != null && !(displayName instanceof TranslatableComponent t
											&& t.args().size() == 4 && t.args().get(3) instanceof TextComponent c && c.content().equals("Custom Display")))
									{
										originalName = ComponentUtil.serializeAsJson(displayName);
										displayCompound.setString(CucumberyTag.ORIGINAL_NAME, originalName);
										itemStack = nbtItem.getItem();
										itemMeta = itemStack.getItemMeta();
									}
									itemMeta.displayName(originalName == null || originalName.equals("") ? null : ComponentUtil.create(originalName));
									itemStack.setItemMeta(itemMeta);
									ItemLore.setItemLore(itemStack);
									playerInventory.setItemInMainHand(itemStack);
									Component suffixComponent = ComponentUtil.create(suffix);
									if (suffixComponent.color() == null)
									{
										suffixComponent = suffixComponent.color(Constant.THE_COLOR);
									}
									MessageUtil.info(sender, "아이템에 %s번째 접미어 %s을(를) 추가했습니다. 전 : %s, 후 : %s" + (nbt != null ? ", nbt : %s" : ""), suffixes.size(), suffixComponent,
											item, itemStack, nbt + "");
								}
								case "remove" ->
								{
									if (args.length > 4)
									{
										MessageUtil.longArg(sender, 4, args);
										MessageUtil.commandInfo(sender, label, "customdisplayname suffix remove [줄|--all]");
										return true;
									}
									if (suffixes == null || suffixes.isEmpty())
									{
										MessageUtil.sendError(sender, "%s에는 접미어가 없습니다", item);
										return true;
									}
									int line = suffixes.size() - 1;
									if (args.length == 4)
									{
										if (args[3].equals("--all"))
										{
											line = -1;
										}
										else
										{
											if (!MessageUtil.isInteger(sender, args[3], true))
											{
												return true;
											}
											line = Integer.parseInt(args[3]);
											if (!MessageUtil.checkNumberSize(sender, line, 1, suffixes.size(), true))
											{
												return true;
											}
											line--;
										}
									}
									String originalName = displayCompound.getString(CucumberyTag.ORIGINAL_NAME);
									if (line == -1 || suffixes.size() == 1)
									{
										NBTAPI.removeKey(displayCompound, CucumberyTag.ITEMSTACK_DISPLAY_SUFFIX);
										ItemStack itemStack = nbtItem.getItem();
										ItemMeta itemMeta = itemStack.getItemMeta();
										itemMeta.displayName(originalName == null || originalName.equals("") ? null : ComponentUtil.create(originalName));
										itemStack.setItemMeta(itemMeta);
										ItemLore.setItemLore(itemStack);
										playerInventory.setItemInMainHand(itemStack);
										MessageUtil.info(sender, "아이템의 모든 접미어를 제거했습니다. 전 : %s, 후 : %s", item, itemStack);
										return true;
									}
									String text = suffixes.get(line).getString("text");
									if (text == null)
									{
										text = "translate:&c잘못된 접미어";
									}
									Component suffixComponent = ComponentUtil.create(text);
									if (suffixComponent.color() == null)
									{
										suffixComponent = suffixComponent.color(Constant.THE_COLOR);
									}
									suffixes.remove(line);
									ItemStack itemStack = nbtItem.getItem();
									ItemMeta itemMeta = itemStack.getItemMeta();
									itemMeta.displayName(originalName == null || originalName.equals("") ? null : ComponentUtil.create(originalName));
									itemStack.setItemMeta(itemMeta);
									ItemLore.setItemLore(itemStack);
									playerInventory.setItemInMainHand(itemStack);
									MessageUtil.info(sender, "아이템의 %s번째 접미어 %s을(를) 제거했습니다. 전 : %s, 후 : %s", line + 1, suffixComponent, item, itemStack);
								}
								case "set" ->
								{
									if (args.length < 5)
									{
										MessageUtil.shortArg(sender, 5, args);
										MessageUtil.commandInfo(sender, label, "customdisplayname suffix set <줄> <접미어> [nbt]");
										return true;
									}
									if (args.length > 6)
									{
										MessageUtil.longArg(sender, 6, args);
										MessageUtil.commandInfo(sender, label, "customdisplayname suffix set <줄> <접미어> [nbt]");
										return true;
									}
									if (suffixes == null || suffixes.isEmpty())
									{
										MessageUtil.sendError(sender, "%s에는 접미어가 없습니다", item);
										return true;
									}
									if (!MessageUtil.isInteger(sender, args[3], true))
									{
										return true;
									}
									int line = Integer.parseInt(args[3]);
									if (!MessageUtil.checkNumberSize(sender, line, 1, suffixes.size()))
									{
										return true;
									}
									line--;
									String suffix = args[4];
									String nbt = null;
									if (args.length == 6)
									{
										try
										{
											nbt = args[5];
											new NBTContainer(nbt);
										}
										catch (Exception exception)
										{
											MessageUtil.sendError(sender, "잘못된 NBT입니다: %s", nbt);
											return true;
										}
									}
									NBTCompound compound = suffixes.get(line);
									compound.setString("text", suffix);
									if (nbt != null)
									{
										NBTCompound nbtCompound = compound.addCompound("nbt");
										nbtCompound.mergeCompound(new NBTContainer(nbt));
									}
									String originalName = displayCompound.getString(CucumberyTag.ORIGINAL_NAME);
									ItemStack itemStack = nbtItem.getItem();
									ItemMeta itemMeta = itemStack.getItemMeta();
									itemMeta.displayName(originalName == null || originalName.equals("") ? null : ComponentUtil.create(originalName));
									itemStack.setItemMeta(itemMeta);
									ItemLore.setItemLore(itemStack);
									playerInventory.setItemInMainHand(itemStack);
									Component suffixComponent = ComponentUtil.create(suffix);
									if (suffixComponent.color() == null)
									{
										suffixComponent = suffixComponent.color(Constant.THE_COLOR);
									}
									MessageUtil.info(sender, "아이템의 %s번째 접미어를 %s으(로) 변경했습니다. 전 : %s, 후 : %s" + (nbt != null ? ", nbt : %s" : ""), line + 1, suffixComponent, item,
											itemStack, nbt + "");
								}
								default ->
								{
									MessageUtil.wrongArg(sender, 3, args);
									MessageUtil.commandInfo(sender, label, "customdisplayname suffix <add|remove|set|list>");
									return true;
								}
							}
						}
						case "name" ->
						{
							if (args.length < 3)
							{
								MessageUtil.shortArg(sender, 3, args);
								MessageUtil.commandInfo(sender, label, "customdisplayname name <name|--remove>");
								return true;
							}
							if (args.length > 3)
							{
								MessageUtil.longArg(sender, 3, args);
								MessageUtil.commandInfo(sender, label, "customdisplayname name <name|--remove>");
								return true;
							}
							boolean remove = args[2].equals("--remove");
							String name = NBTAPI.getString(displayCompound, CucumberyTag.ITEMSTACK_DISPLAY_NAME);
							if (remove)
							{
								if (name == null)
								{
									MessageUtil.sendError(sender, "%s에는 이름이 없습니다", item);
									return true;
								}
								displayCompound.removeKey(CucumberyTag.ITEMSTACK_DISPLAY_NAME);
								if (displayCompound.getKeys().isEmpty())
								{
									itemTag.removeKey(CucumberyTag.ITEMSTACK_DISPLAY_KEY);
								}
								if (itemTag.getKeys().isEmpty())
								{
									nbtItem.removeKey(CucumberyTag.KEY_MAIN);
								}
								String originalName = displayCompound.getString(CucumberyTag.ORIGINAL_NAME);
								ItemStack itemStack = nbtItem.getItem();
								ItemMeta itemMeta = itemStack.getItemMeta();
								Component displayName = itemMeta.displayName();
								if ((originalName == null || originalName.equals("")) && displayName != null && !(displayName instanceof TranslatableComponent t
										&& t.args().size() == 4 && t.args().get(3) instanceof TextComponent c && c.content().equals("Custom Display")))
								{
									originalName = ComponentUtil.serializeAsJson(displayName);
									displayCompound.setString(CucumberyTag.ORIGINAL_NAME, originalName);
									itemStack = nbtItem.getItem();
									itemMeta = itemStack.getItemMeta();
								}
								itemMeta.displayName(originalName == null || originalName.equals("") ? null : ComponentUtil.create(originalName));
								itemStack.setItemMeta(itemMeta);
								ItemLore.setItemLore(itemStack);
								playerInventory.setItemInMainHand(itemStack);
								Component nameComponent = ComponentUtil.create(name);
								if (nameComponent.color() == null)
								{
									nameComponent = nameComponent.color(Constant.THE_COLOR);
								}
								MessageUtil.info(sender, "이름 %s을(를) 제거했습니다. 전 : %s, 후 : %s", nameComponent, item, itemStack);
								return true;
							}
							if (itemTag == null)
							{
								itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
							}
							if (displayCompound == null)
							{
								displayCompound = itemTag.addCompound(CucumberyTag.ITEMSTACK_DISPLAY_KEY);
							}
							displayCompound.setString(CucumberyTag.ITEMSTACK_DISPLAY_NAME, args[2]);
							String originalName = displayCompound.getString(CucumberyTag.ORIGINAL_NAME);
							ItemStack itemStack = nbtItem.getItem();
							ItemMeta itemMeta = itemStack.getItemMeta();
							Component displayName = itemMeta.displayName();
							if ((originalName == null || originalName.equals("")) && displayName != null && !(displayName instanceof TranslatableComponent t
									&& t.args().size() == 4 && t.args().get(3) instanceof TextComponent c && c.content().equals("Custom Display")))
							{
								originalName = ComponentUtil.serializeAsJson(displayName);
								displayCompound.setString(CucumberyTag.ORIGINAL_NAME, originalName);
								itemStack = nbtItem.getItem();
								itemMeta = itemStack.getItemMeta();
							}
							itemMeta.displayName(originalName == null || originalName.equals("") ? null : ComponentUtil.create(originalName));
							itemStack.setItemMeta(itemMeta);
							ItemLore.setItemLore(itemStack);
							playerInventory.setItemInMainHand(itemStack);
							Component nameComponent = ComponentUtil.create(args[2]);
							if (nameComponent.color() == null)
							{
								nameComponent = nameComponent.color(Constant.THE_COLOR);
							}
							MessageUtil.info(sender, "이름을 %s으(로) 설정했습니다. 전 : %s, 후 : %s", nameComponent, item, itemStack);
						}
						case "remove" ->
						{
							if (displayCompound == null)
							{
								MessageUtil.sendError(sender, "%s에는 사용자 지정 이름 태그가 없습니다", item);
								return true;
							}
							String originalName = displayCompound.getString(CucumberyTag.ORIGINAL_NAME);
							itemTag.removeKey(CucumberyTag.ITEMSTACK_DISPLAY_KEY);
							if (itemTag.getKeys().isEmpty())
							{
								nbtItem.removeKey(CucumberyTag.KEY_MAIN);
							}
							ItemStack itemStack = nbtItem.getItem();
							ItemMeta itemMeta = itemStack.getItemMeta();
							itemMeta.displayName(originalName == null || originalName.equals("") ? null : ComponentUtil.create(originalName));
							itemStack.setItemMeta(itemMeta);
							ItemLore.setItemLore(itemStack);
							playerInventory.setItemInMainHand(itemStack);
							MessageUtil.info(sender, "사용자 지정 이름 태그를 제거했습니다. 전 : %s, 후 : %s", item, itemStack);
						}
						default ->
						{
							MessageUtil.wrongArg(sender, 2, args);
							MessageUtil.commandInfo(sender, label, "customdisplayname <prefix|suffix|name|remove>");
							return true;
						}
					}
				}
				default ->
				{
					MessageUtil.wrongArg(sender, 1, args);
					MessageUtil.commandInfo(sender, label, usage);
					return true;
				}
			}
		}
		return true;
	}
}

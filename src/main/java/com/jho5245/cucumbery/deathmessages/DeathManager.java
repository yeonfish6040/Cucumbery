package com.jho5245.cucumbery.deathmessages;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.custom.customeffect.children.group.StringCustomEffect;
import com.jho5245.cucumbery.events.DeathMessageEvent;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.no_groups.ItemSerializer;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.sendercomponent.SenderComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class DeathManager
{
	public static final Component BAD_RESPAWM_POINT = ComponentUtil.translate("chat.square_brackets",
					ComponentUtil.translate("death.attack.badRespawnPoint.link")).hoverEvent(HoverEvent.showText(Component.text("MC-28723")))
			.clickEvent(ClickEvent.openUrl("https://bugs.mojang.com/browse/MCPE-28723"));

	public static final Component downloadURL = ComponentUtil.translate("&9&l여기")
			.hoverEvent(HoverEvent.showText(ComponentUtil.translate("클릭하여 %s 주소로 이동합니다", "rg255,204;https://cucumbery.com/api/builds/dev/latest/download")))
			.clickEvent(ClickEvent.openUrl("https://cucumbery.com/api/builds/dev/latest/download"));

	public static final Component reportBugURL = ComponentUtil.translate("&9&l여기")
			.hoverEvent(HoverEvent.showText(ComponentUtil.translate("클릭하여 %s 주소로 이동합니다", "rg255,204;https://github.com/jho5245/Cucumbery/issues/new")))
			.clickEvent(ClickEvent.openUrl("https://github.com/jho5245/Cucumbery/issues/new"));

	public static final int MAXIMUM_COMPONENT_SERIAL_LENGTH = 100000;

	private static final int COOLDOWN_IN_TICKS = 5;

	public static String BRACKET = Variable.deathMessages.getString("death-messages.prefix.bracket", "&6[%s]");

	public static Component DEATH_PREFIX = ComponentUtil.translate(BRACKET,
			ComponentUtil.translate(Variable.deathMessages.getString("death-messages.prefix.death")));

	public static Component DEATH_PREFIX_PVP = ComponentUtil.translate(BRACKET,
			ComponentUtil.translate(Variable.deathMessages.getString("death-messages.prefix.pvp")));

	private static boolean goBack = false;

	public static void manageDeathMessages(EntityDeathEvent event)
	{
		LivingEntity entity = event.getEntity();
		if (deathMessageApplicable(entity))
		{
			if (!(entity instanceof Player))
			{
				if (goBack)
				{
					return;
				}
				goBack = true;
				Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> goBack = false, COOLDOWN_IN_TICKS);
			}
			Component timeFormat = ComponentUtil.translate("사망 시각 : %s", Constant.THE_COLOR_HEX + Method.getCurrentTime(Calendar.getInstance(), true, false));
			DEATH_PREFIX_PVP = DEATH_PREFIX_PVP.hoverEvent(HoverEvent.showText(timeFormat));
			DEATH_PREFIX = DEATH_PREFIX.hoverEvent(HoverEvent.showText(timeFormat));
			// 접두사 기능 붙여넣기
			boolean usePrefix = Variable.deathMessages.getBoolean("death-messages.prefix.enable");
			Location location = entity.getLocation();
			boolean isPlayerDeath = event instanceof PlayerDeathEvent;
			PlayerDeathEvent playerDeathEvent = isPlayerDeath ? (PlayerDeathEvent) event : null;
			EntityDamageEvent damageCause = entity.getLastDamageCause();
			if (damageCause == null)
			{
				damageCause = new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.VOID, Double.MAX_VALUE);
			}
			EntityDamageEvent.DamageCause cause = damageCause.getCause();
			List<Object> args = new ArrayList<>();
			World world = location.getWorld();
			String worldName = world.getName();
			args.add(entity);
			List<Component> extraArgs = new ArrayList<>();
			String key = "";
			@Nullable Object damager = getDamager(event);
			@Nullable ItemStack weapon = getWeapon(event);
			if (damager != null)
			{
				long time = 0;
				if (damager instanceof ItemStack itemStack && Variable.blockDamagerAndCurrentTime.containsKey(ItemSerializer.serialize(itemStack)))
				{
					time = Variable.blockDamagerAndCurrentTime.get(ItemSerializer.serialize(itemStack));
				}
				else if (damager instanceof Entity e && Variable.damagerAndCurrentTime.containsKey(e.getUniqueId()))
				{
					time = Variable.damagerAndCurrentTime.get(e.getUniqueId());
				}
				if (cause == DamageCause.VOID && damageCause.getDamage() < Math.pow(2, 10) && System.currentTimeMillis() - time > 60000)
				{
					damager = null;
					weapon = null;
				}
				if (!(cause == DamageCause.VOID && damageCause.getDamage() < Math.pow(2, 10)))
				{
					long current = System.currentTimeMillis();
					boolean success = switch (cause)
					{
						case FALL -> current - time > 20000;
						case FIRE, FIRE_TICK -> current - time > 10000;
						default -> current - time > 5000;
					};
					if (success)
					{
						damager = null;
						weapon = null;
					}
				}
			}
			ItemStack lastTrampledBlock = getLastTrampledBlock(entity.getUniqueId());
			float fallDistance = entity.getFallDistance();
			switch (cause)
			{
				case CONTACT ->
				{
					if (damageCause instanceof EntityDamageByBlockEvent blockEvent)
					{
						Block block = blockEvent.getDamager();
						if (block != null)
						{
							Material type = block.getType();
							ItemStack itemStack = ItemStackUtil.getItemStackFromBlock(block);
							extraArgs.add(ComponentUtil.create(itemStack));
							if (type == Material.SWEET_BERRY_BUSH)
							{
								key = "sweet_berry_bush";
							}
							else if (type == Material.POINTED_DRIPSTONE)
							{
								key = "stalagmite";
							}
							else if (type == Material.CACTUS)
							{
								key = "cactus";
							}
							else
							{
								key = "contact";
							}
						}
					}
				}
				case ENTITY_ATTACK ->
				{
					if (damageCause instanceof EntityDamageByEntityEvent damageByEntityEvent)
					{
						Entity damagerEntity = damageByEntityEvent.getDamager();
						if (damagerEntity instanceof AreaEffectCloud)
						{
							key = "magic";
						}
						else if (damagerEntity instanceof EvokerFangs evokerFangs)
						{
							key = "evoker_fangs";
							extraArgs.add(ComponentUtil.create(evokerFangs));
						}
						else
						{
							key = "melee";
							if (damagerEntity instanceof Bee)
							{
								key = "sting";
							}
							if (entity instanceof Parrot && damager instanceof Player && damageCause.getDamage() > Math.pow(2, 10))
							{
								if (ItemStackUtil.itemExists(weapon) && weapon.getType() == Material.COOKIE)
								{
									key = "parrot_cookie";
								}
							}
							if (weapon != null)
							{
								Material type = weapon.getType();
								switch (type)
								{
									case DIAMOND_SWORD, GOLDEN_SWORD, IRON_SWORD, NETHERITE_SWORD, STONE_SWORD, WOODEN_SWORD -> key += "_sword";
								}
							}
						}
					}
					else
					{
						key = "entity_attack";
					}
				}
				case ENTITY_SWEEP_ATTACK -> key = "melee_sweep";
				case MAGIC -> key = "magic";
				case PROJECTILE ->
				{
					if (damageCause instanceof EntityDamageByEntityEvent damageByEntityEvent)
					{
						Entity damagerEntity = damageByEntityEvent.getDamager();
						if (damagerEntity instanceof Trident)
						{
							key = "trident";
						}
						else if (damagerEntity instanceof Fireball fireball)
						{
							if (!(fireball instanceof SizedFireball))
							{
								extraArgs.add(SenderComponentUtil.senderComponent(fireball));
							}
							if (fireball instanceof DragonFireball)
							{
								key = "dragon_fireball";
							}
							else if (fireball instanceof SizedFireball sizedFireball)
							{
								key = "fireball";
								ItemStack itemStack = sizedFireball.getDisplayItem().clone();
								if (Method.usingLoreFeature(entity.getLocation()))
								{
									ItemLore.setItemLore(itemStack);
								}
								extraArgs.add(ComponentUtil.create(itemStack));
							}
							else if (fireball instanceof WitherSkull)
							{
								key = "wither_skull";
							}
							else
							{
								key = "fireball";
							}
						}
						else if (damagerEntity instanceof ShulkerBullet shulkerBullet)
						{
							key = "shulker_bullet";
							extraArgs.add(ComponentUtil.create(shulkerBullet));
						}
						else if (damagerEntity instanceof AbstractArrow)
						{
							key = "arrow";
						}
						else
						{
							key = "projectile";
						}
					}
					else
					{
						key = "projectile";
					}
				}
				case WORLD_BORDER ->
				{
					WorldBorder worldBorder = world.getWorldBorder();
					key = "world_border";
					Component worldBorderComponent = ComponentUtil.translate("세계 경계", Constant.THE_COLOR);
					Location center = worldBorder.getCenter();
					double size = worldBorder.getSize();
					Component hover = ComponentUtil.translate("세계 경계");
					hover = hover.append(Component.text("\n"));
					hover = hover.append(ComponentUtil.translate("크기 : %s", size));
					hover = hover.append(Component.text("\n"));
					hover = hover.append(ComponentUtil.translate("중심 좌표 : %s", center));
					worldBorderComponent = worldBorderComponent.hoverEvent(hover);
					extraArgs.add(worldBorderComponent);
				}
				case SUFFOCATION ->
				{
					key = "suffocation";
					extraArgs.add(ComponentUtil.create(
							ItemStackUtil.getItemStackFromBlock(ItemStackUtil.getExactBlockFromWithLimited(entity.getEyeLocation(), Constant.OCCLUDING_BLOCKS))));
				}
				case FALL ->
				{
					if (damageCause instanceof EntityDamageByEntityEvent damageByEntityEvent && damageByEntityEvent.getDamager() instanceof EnderPearl enderPearl
							&& entity.equals(enderPearl.getShooter()))
					{
						key = "ender_pearl";
						extraArgs.add(ComponentUtil.create(enderPearl.getItem()));
					}
					else if (CustomEffectManager.hasEffect(entity, CustomEffectType.GLIDING))
					{
						key = "fall_elytra";
					}
					else
					{
						key = "fall";
						if (lastTrampledBlock != null)
						{
							key += "_block";
							extraArgs.add(ComponentUtil.create(lastTrampledBlock));
						}
						if (fallDistance >= 6)
						{
							key += "_high";
						}
					}
					extraArgs.add(ComponentUtil.create(Constant.Sosu2.format(entity.getFallDistance())).color(Constant.THE_COLOR));
				}
				case FIRE ->
				{
					key = "fire_block";
					if (damageCause instanceof EntityDamageByBlockEvent damageByBlockEvent)
					{
						Block block = damageByBlockEvent.getDamager();
						if (block == null)
						{
							block = ItemStackUtil.getExactBlockFromWithLimited(location,
									Arrays.asList(Material.LAVA, Material.LAVA_CAULDRON, Material.FIRE, Material.SOUL_FIRE, Material.CAMPFIRE, Material.SOUL_CAMPFIRE));
						}
						extraArgs.add(ComponentUtil.create(ItemStackUtil.getItemStackFromBlock(block)));
					}
					else
					{
						extraArgs.add(ComponentUtil.create(ItemStackUtil.getItemStackFromBlock(ItemStackUtil.getExactBlockFromWithLimited(location,
								Arrays.asList(Material.LAVA, Material.LAVA_CAULDRON, Material.FIRE, Material.SOUL_FIRE, Material.CAMPFIRE, Material.SOUL_CAMPFIRE)))));
					}
				}
				case FIRE_TICK -> key += "fire";
				case MELTING -> key = "melting";
				case LAVA ->
				{
					key = "lava";
					Block block = ItemStackUtil.getExactBlockFromWithLimited(location, Arrays.asList(Material.LAVA, Material.LAVA_CAULDRON));
					if (block.getType() == Material.LAVA_CAULDRON)
					{
						key += "_cauldron";
						extraArgs.add(ComponentUtil.create(ItemStackUtil.getItemStackFromBlock(block)));
					}
				}
				case DROWNING ->
				{
					if (entity instanceof Snowman || entity instanceof Blaze)
					{
						key = "melting";
					}
					else
					{
						key = "drown";
						List<Entity> nearbyEntities = entity.getNearbyEntities(3, 3, 3);
						if (entity instanceof WaterMob)
						{
							key += "_water";
							nearbyEntities.removeIf(e -> e instanceof WaterMob || e instanceof Axolotl || e instanceof Turtle || CustomEffectManager.hasEffect(e,
									CustomEffectType.DAMAGE_INDICATOR));
						}
						else
						{
							nearbyEntities.removeIf(e -> !(e instanceof WaterMob || e instanceof Axolotl || e instanceof Turtle));
						}
						if (!nearbyEntities.isEmpty())
						{
							extraArgs.add(SenderComponentUtil.senderComponent(nearbyEntities.get(Method.random(0, nearbyEntities.size() - 1))));
							key += "_together";
						}
					}
				}
				case BLOCK_EXPLOSION ->
				{
					key += "bad_respawn";
					extraArgs.add(BAD_RESPAWM_POINT);
				}
				case ENTITY_EXPLOSION ->
				{
					if (damageCause instanceof EntityDamageByEntityEvent damageByEntityEvent)
					{
						Entity damagerEntity = damageByEntityEvent.getDamager();
						if (damagerEntity instanceof Firework)
						{
							key = "fireworks";
							if (damager != null)
							{
								if (ItemStackUtil.itemExists(weapon) && weapon.getType() == Material.CROSSBOW)
								{
									key = "fireworks_crossbow";
								}
							}
						}
						else
						{
							key = "explosion";
						}
					}
				}
				case KILL ->
				{
					key = "kill";
					if (entity instanceof Player player)
					{
						extraArgs.add(Component.text(player.getName()));
					}
					else
					{
						extraArgs.add(Component.text(entity.getUniqueId().toString()));
					}
				}
				case VOID ->
				{
					key = "void";
					if (lastTrampledBlock != null)
					{
						extraArgs.add(ComponentUtil.create(lastTrampledBlock));
						key += "_block";
					}
					if (fallDistance > 350)
					{
						key += "_high";
					}
					extraArgs.add(ComponentUtil.create(Constant.Sosu2.format(entity.getFallDistance())).color(Constant.THE_COLOR));
				}
				case LIGHTNING ->
				{
					key = "lightning_bolt";
					if (damageCause instanceof EntityDamageByEntityEvent damageByEntityEvent)
					{
						Entity damagerEntity = damageByEntityEvent.getDamager();
						if (damagerEntity instanceof LightningStrike lightningStrike)
						{
							extraArgs.add(SenderComponentUtil.senderComponent(lightningStrike));
						}
					}
				}
				case SUICIDE -> key = "suicide";
				case STARVATION -> key = "starve";
				case POISON -> key = "poison";
				case WITHER -> key = "wither";
				case FALLING_BLOCK ->
				{
					if (damageCause instanceof EntityDamageByEntityEvent damageByEntityEvent)
					{
						Entity damagerEntity = damageByEntityEvent.getDamager();
						if (damagerEntity instanceof FallingBlock fallingBlock)
						{
							Material material = fallingBlock.getBlockData().getMaterial();
							switch (material)
							{
								case ANVIL -> key = "anvil";
								case POINTED_DRIPSTONE -> key = "falling_stalactite";
								default -> key = "falling_block";
							}
							extraArgs.add(ComponentUtil.create(fallingBlock));
						}
					}
					else
					{
						key = "falling_block";
					}
				}
				case THORNS ->
				{
					key = "thorns";
					// 가시 피해로 죽은 사인의 무기는 죽은 자의 무기를 가져온다
					weapon = Variable.attackerAndWeapon.get(entity.getUniqueId());
				}
				case DRAGON_BREATH -> key = "dragon_breath";
				case CUSTOM -> key = "generic";
				case FLY_INTO_WALL ->
				{
					key = "elytra";
					Vector vector = location.getDirection();
					Location locationClone = location.clone();
					locationClone.add(vector.setX(vector.getX() / 2).setY(vector.getY() / 2).setZ(vector.getZ() / 2));
					extraArgs.add(
							ComponentUtil.create(ItemStackUtil.getItemStackFromBlock(ItemStackUtil.getExactBlockFromWithLimited(locationClone, Constant.OCCLUDING_BLOCKS))));
				}
				case HOT_FLOOR ->
				{
					key = "magma_block";
					if (damageCause instanceof EntityDamageByBlockEvent damageByBlockEvent)
					{
						Block block = damageByBlockEvent.getDamager();
						if (block != null)
						{
							extraArgs.add(ComponentUtil.create(ItemStackUtil.getItemStackFromBlock(block)));
						}
						else
						{
							extraArgs.add(ComponentUtil.create(Material.MAGMA_BLOCK));
						}
					}
				}
				case CRAMMING ->
				{
					key = "cramming";
					Collection<LivingEntity> livingEntities = world.getNearbyLivingEntities(location, 1.5d);
					livingEntities.removeIf(e -> e.equals(entity));
					if (livingEntities.isEmpty())
					{
						key += "_none";
					}
					else
					{
						extraArgs.add(SenderComponentUtil.senderComponent(livingEntities));
					}
					Integer i = world.getGameRuleValue(GameRule.MAX_ENTITY_CRAMMING);
					if (i != null)
					{
						if (i.equals(1) && livingEntities.size() == 1)
						{
							key += "_solo";
						}
					}
				}
				case DRYOUT -> key = "dry_out";
				case FREEZE -> key = "freeze";
				case SONIC_BOOM -> key = "sonic_boom";
			}
			if (CustomEffectManager.hasEffect(entity, CustomEffectType.CUSTOM_DEATH_MESSAGE))
			{
				CustomEffect customEffect = CustomEffectManager.getEffect(entity, CustomEffectType.CUSTOM_DEATH_MESSAGE);
				if (customEffect instanceof StringCustomEffect stringCustomEffect)
				{
					key = stringCustomEffect.getString();
				}
			}
			boolean assassinationEnchant =
					(damager instanceof Entity e && CustomEffectManager.hasEffect(e, CustomEffectType.ASSASSINATION)) || (weapon != null && weapon.hasItemMeta()
							&& weapon.getItemMeta().hasEnchants() && (
							(weapon.getType() != Material.BOW && weapon.getType() != Material.CROSSBOW && CustomEnchant.isEnabled() && weapon.getItemMeta()
									.hasEnchant(CustomEnchant.ASSASSINATION)) || (CustomEnchant.isEnabled() && weapon.getItemMeta()
									.hasEnchant(CustomEnchant.ASSASSINATION_BOW))));
			if (damager != null)
			{
				if (damager instanceof ItemStack itemStack)
				{
					args.add(ComponentUtil.create(itemStack));
				}
				else if (damager instanceof Entity damagerEntity)
				{
					if (assassinationEnchant)
					{
						args.add(ComponentUtil.translate("&7&o누군가"));
						extraArgs.add(ComponentUtil.create(Constant.Sosu2.format(0)).color(Constant.THE_COLOR));
					}
					else
					{
						args.add(SenderComponentUtil.senderComponent(damagerEntity));
						double distance = -1;
						try
						{
							distance = damagerEntity.getLocation().distance(entity.getLocation());
						}
						catch (Exception ignored)
						{

						}
						extraArgs.add(ComponentUtil.create(Constant.Sosu2.format(distance)).color(Constant.THE_COLOR));
					}
				}
				key += "_combat";
				if (entity.equals(damager))
				{
					key += "_suicide";
				}
			}
			if (ItemStackUtil.itemExists(weapon))
			{
				args.add(ComponentUtil.create(weapon));
				key += "_item";
			}
			else
			{
				Projectile projectile = getDamagerProjectile(event);
				if (projectile != null && assassinationEnchant)
				{
					projectile.setShooter(null);
				}
				if (projectile != null && projectile.getShooter() == null)
				{
					if (damager == null)
					{
						if (projectile instanceof Firework firework)
						{
							ItemStack itemStack = new ItemStack(Material.FIREWORK_ROCKET);
							itemStack.setItemMeta(firework.getFireworkMeta());
							extraArgs.add(ComponentUtil.create(itemStack));
						}
						else
						{
							extraArgs.add(ComponentUtil.create(projectile));
						}
					}
					key += "_unknown";
				}
			}

			if (isPlayerDeath)
			{
				Component msg = playerDeathEvent.deathMessage();
				if (msg instanceof TranslatableComponent translatableComponent)
				{
					String k = translatableComponent.key();
					// MessageUtil.broadcastDebug(k);
					if (k.equals("death.fell.accident.water"))
					{
						key = "water_accident";
					}
				}
			}
			MessageUtil.broadcastDebug(key);
			DeathMessage deathMessage;
			try
			{
				deathMessage = DeathMessage.valueOf(key.toUpperCase());
			}
			catch (Exception e)
			{
				deathMessage = DeathMessage.UNKNOWN;
			}
			List<String> keys = deathMessage.getKeys();
			// 조건부 데스 메시지가 있으면 데스 메시지 치환
			Condition.changeDeathMessages(event, key, keys);

			if (deathMessage == DeathMessage.UNKNOWN)
			{
				keys.clear();
				args.clear();
				extraArgs.clear();
				args.add(SenderComponentUtil.senderComponent(entity));
				args.add(reportBugURL);
				args.add(
						Component.text(key).hoverEvent(ComponentUtil.translate("chat.copy.click")).clickEvent(ClickEvent.copyToClipboard(key)).color(Constant.THE_COLOR));
				keys.add("%1$s이(가) 알 수 없는 이유로 죽었습니다. 죄송합니다! 이 메시지가 뜨면 개발자가 일을 안 한겁니다! %2$s에서 해당 버그를 제보해주세요! 키 : %3$s");
			}

			if (key.equals("kill_combat"))
			{
				extraArgs.add(downloadURL);
				keys.add("%1$s이(가) 알 수 없는 이유로 죽었습니다. 죄송합니다! 이 메시지가 뜨면 개발자가 일을 안 한겁니다! %2$s에서 해당 버그를 제보해주세요! 사실 이 메시지는 이스터 에그이며, 플러그인 다운로드 주소는 %" + (extraArgs.size()
						+ args.size()) + "$s입니다!");
			}
			// 해당 키 목록에서 랜덤 키를 하나 가져옴
			if (!keys.isEmpty())
			{
				int random = Method.random(0, keys.size() - 1);
				key = keys.get(random);
			}
			else
			{
				keys.add(key);
				args.clear();
				args.add(SenderComponentUtil.senderComponent(entity));
				key = "%1$s이(가) 알 수 없는 이유로 죽었습니다. 죄송합니다! 이 메시지가 뜨면 서버 관리자가 플러그인 업데이트 유지 보수를 안 한겁니다! 서버 관리자에게 문의해주세요! 키 : %2$s";
			}
			args.addAll(extraArgs);
			@Nullable Component deathMessageComponent = ComponentUtil.translate(key, args);
			if (ComponentUtil.serializeAsJson(deathMessageComponent).length() > MAXIMUM_COMPONENT_SERIAL_LENGTH)
			{
				for (int i = 0; i < args.size(); i++)
				{
					Object o = args.get(i);
					if (o instanceof Component component)
					{
						HoverEvent<?> hoverEvent = component.hoverEvent();
						if (hoverEvent != null && hoverEvent.action() == HoverEvent.Action.SHOW_ITEM)
						{
							args.set(i, component.hoverEvent(null));
						}
					}
				}
				deathMessageComponent = ComponentUtil.translate("death.attack.message_too_long", ComponentUtil.translate(key, args));
			}
			final Component insiderComponent = deathMessageComponent;
			Component insiderPrefix = Component.empty();
			boolean isPvP = isPlayerDeath && (damager instanceof Player && !entity.equals(damager));
			if (usePrefix)
			{
				Projectile projectile = getDamagerProjectile(event);
				if (projectile != null)
				{
					ProjectileSource projectileSource = projectile.getShooter();
					if (entity.equals(projectileSource))
					{
						isPvP = false;
					}
				}
				AreaEffectCloud areaEffectCloud = getDamagerAreaEffectCloud(event);
				if (areaEffectCloud != null)
				{
					ProjectileSource projectileSource = areaEffectCloud.getSource();
					if (entity.equals(projectileSource))
					{
						isPvP = false;
					}
				}
				deathMessageComponent = Component.empty().append(isPvP ? DEATH_PREFIX_PVP : DEATH_PREFIX).append(deathMessageComponent);
				insiderPrefix = isPvP ? DEATH_PREFIX_PVP : DEATH_PREFIX;
			}
			Variable.victimAndDamager.remove(entity.getUniqueId());
			Variable.victimAndBlockDamager.remove(entity.getUniqueId());
			Variable.lastTrampledBlock.remove(entity.getUniqueId());
			Variable.lastTrampledBlockType.remove(entity.getUniqueId());
			if (damager != null)
			{
				if (damager instanceof Entity e)
				{
					//MessageUtil.broadcastDebug(e);
					Variable.attackerAndWeapon.remove(e.getUniqueId());
					Variable.attackerAndWeaponString.remove(e.getUniqueId());
				}
			}
			// 모든 플레이어에게 데스메시지 보냄
			if (event.isCancelled() || !entity.isDead())
			{
				List<String> cancelledMessages = Variable.deathMessages.getStringList("death-messages.event-cancelled-messages");
				if (!cancelledMessages.isEmpty())
				{
					String message = cancelledMessages.get(Method.random(0, cancelledMessages.size() - 1));
					deathMessageComponent = deathMessageComponent.append(ComponentUtil.translate(message, entity));
				}
			}
			if (playerDeathEvent != null)
			{
				playerDeathEvent.deathMessage(null);
			}
			if (!key.equals("none"))
			{
				DeathMessageEvent deathMessageEvent = new DeathMessageEvent(entity, deathMessageComponent, isPvP, damager);
				Bukkit.getPluginManager().callEvent(deathMessageEvent);
				boolean finalIsPvP = isPvP;
				@Nullable Object finalDamager = damager;
				Predicate<Player> playerPredicate = player -> !(((player == entity || player == finalDamager) && UserData.SHOW_DEATH_SELF_MESSAGE.getBoolean(player))
						|| (!(player == entity || player == finalDamager) && UserData.SHOW_DEATH_MESSAGE.getBoolean(player) && (!finalIsPvP
						|| UserData.SHOW_DEATH_PVP_MESSAGE.getBoolean(player))));
				MessageUtil.broadcastPlayer(playerPredicate, deathMessageComponent);
				if (CustomEffectManager.hasEffect(entity, CustomEffectType.CURSE_OF_BEANS))
				{
					for (Player online : Bukkit.getOnlinePlayers())
					{
						if (!playerPredicate.test(online))
						{
							if (!CustomEffectManager.hasEffect(online, CustomEffectType.CURSE_OF_BEANS))
							{
								MessageUtil.sendMessage(online, deathMessageComponent);
							}
						}
					}
				}
				if (CustomEffectManager.hasEffect(entity, CustomEffectType.INSIDER))
				{
					for (Player online : Bukkit.getOnlinePlayers())
					{
						if (entity != online)
						{
							MessageUtil.sendTitle(online, insiderPrefix, insiderComponent, 5, 100, 15);
						}
					}
				}
				int x = location.getBlockX();
				int y = location.getBlockY();
				int z = location.getBlockZ();
				// 콘솔에 디버그를 보내기 위함
				deathMessageComponent = deathMessageComponent.append(ComponentUtil.create("&7 - " + worldName + ", " + x + ", " + y + ", " + z));
				MessageUtil.consoleSendMessage(deathMessageComponent);
			}
		}
	}

	public static boolean deathMessageApplicable(@NotNull Entity entity)
	{
		if (entity.getScoreboardTags().contains("damage_indicator"))
		{
			return false;
		}
		// 갑옷 거치대는 데스 메시지 띄우지 않음
		if (entity instanceof ArmorStand)
		{
			return false;
		}
		Location location = entity.getLocation();
		boolean success = entity instanceof Player;
		Component customName = entity.customName();
		if (customName != null)
		{
			// 개체 이름에 색상이 있는 경우에는 죽어도 데스 메시지를 띄우지 않음
			success =
					success || customName.color() == null && (entity.getVehicle() != null || entity instanceof Endermite || !location.getNearbyPlayers(500d).isEmpty());
		}
		if (entity instanceof Tameable tameable)
		{
			success = success || tameable.isTamed();
		}
		if (entity instanceof Villager villager)
		{
			success = success || !villager.isAdult() || villager.getVillagerExperience() > 0;
		}
		if (entity instanceof IronGolem ironGolem)
		{
			success = success || ironGolem.isPlayerCreated();
		}
		if (entity instanceof Snowman snowman && snowman.isDerp())
		{
			success = true;
		}
		if (entity instanceof Boss boss && !boss.getLocation().getNearbyPlayers(500d).isEmpty())
		{
			success = true;
		}
		if (entity instanceof ZombieVillager zombieVillager)
		{
			success = success || zombieVillager.isConverting() || zombieVillager.getConversionPlayer() != null;
		}
		EntityDamageEvent damageCause = entity.getLastDamageCause();
		if (damageCause == null)
		{
			damageCause = new EntityDamageEvent(entity, DamageCause.KILL, Double.MAX_VALUE);
		}
		DamageCause cause = damageCause.getCause();
		if (cause == DamageCause.VOID)
		{
			if (damageCause.getDamage() < 100)
			{
				if (!(entity instanceof Enderman && location.getWorld().getEnvironment() == Environment.THE_END))
				{
					success = true;
				}
			}
		}
		return success;
	}

	@Nullable
	protected static ItemStack getLastTrampledBlock(@NotNull UUID uuid)
	{
		if (Variable.lastTrampledBlock.containsKey(uuid))
		{
			return Variable.lastTrampledBlock.get(uuid);
		}
		return null;
	}

	@Nullable
	protected static Object getDamager(EntityDeathEvent event)
	{
		LivingEntity entity = event.getEntity();
		EntityDamageEvent entityDamageEvent = entity.getLastDamageCause();
		if (entityDamageEvent instanceof EntityDamageByEntityEvent damageEvent && damageEvent.getCause() != EntityDamageEvent.DamageCause.FALL)
		{
			EntityDamageEvent.DamageCause cause = entityDamageEvent.getCause();
			Entity damager = damageEvent.getDamager();
			if (damager instanceof LivingEntity)
			{
				return damager;
			}
			else if (damager instanceof Projectile projectile)
			{
				ProjectileSource projectileSource = projectile.getShooter();
				if (projectileSource == null)
				{
					if (cause != EntityDamageEvent.DamageCause.PROJECTILE)
					{
						return damager;
					}
					if (Variable.victimAndBlockDamager.containsKey(entity.getUniqueId()))
					{
						ItemStack itemStack = Variable.victimAndBlockDamager.get(entity.getUniqueId());
						if (ItemStackUtil.itemExists(itemStack) && itemStack.getType() == Material.DISPENSER)
						{
							return itemStack;
						}
						return itemStack;
					}
					return damager;
				}
				if (projectileSource instanceof LivingEntity)
				{
					return projectileSource;
				}
			}
			else if (damager instanceof AreaEffectCloud areaEffectCloud)
			{
				ProjectileSource projectileSource = areaEffectCloud.getSource();
				if (projectileSource == null)
				{
					if (Variable.victimAndBlockDamager.containsKey(entity.getUniqueId()))
					{
						ItemStack itemStack = Variable.victimAndBlockDamager.get(entity.getUniqueId());
						if (ItemStackUtil.itemExists(itemStack) && itemStack.getType() == Material.DISPENSER)
						{
							return itemStack;
						}
						return itemStack;
					}
					return damager;
				}
				if (projectileSource instanceof LivingEntity)
				{
					return projectileSource;
				}
			}
			else if (damager instanceof TNTPrimed tntPrimed)
			{
				Entity tntPrimer = tntPrimed.getSource();
				if (tntPrimer != null)
				{
					return tntPrimer;
				}

				return damager;
			}
			else if (damager instanceof EvokerFangs evokerFangs)
			{
				LivingEntity owner = evokerFangs.getOwner();
				if (owner != null)
				{
					return owner;
				}
			}
			else if (!((cause == EntityDamageEvent.DamageCause.FALLING_BLOCK && damager instanceof FallingBlock) || (cause == EntityDamageEvent.DamageCause.LIGHTNING
					&& damager instanceof LightningStrike)))
			{
				return damager;
			}
		}
		if (Variable.victimAndDamager.containsKey(entity.getUniqueId()))
		{
			return Variable.victimAndDamager.get(entity.getUniqueId());
		}
		if (Variable.victimAndBlockDamager.containsKey(entity.getUniqueId()))
		{
			ItemStack itemStack = Variable.victimAndBlockDamager.get(entity.getUniqueId());
			if (ItemStackUtil.itemExists(itemStack) && itemStack.getType() == Material.DISPENSER)
			{
				return itemStack;
			}
			return itemStack;
		}
		return entity.getKiller();
	}

	@Nullable
	protected static Projectile getDamagerProjectile(EntityDeathEvent event)
	{
		LivingEntity livingEntity = event.getEntity();
		EntityDamageEvent entityDamageEvent = livingEntity.getLastDamageCause();
		if (entityDamageEvent instanceof EntityDamageByEntityEvent damageEvent)
		{
			Entity damager = damageEvent.getDamager();
			if (damager instanceof Projectile projectile)
			{
				return projectile;
			}
		}
		return null;
	}

	@Nullable
	protected static AreaEffectCloud getDamagerAreaEffectCloud(EntityDeathEvent event)
	{
		LivingEntity livingEntity = event.getEntity();
		EntityDamageEvent entityDamageEvent = livingEntity.getLastDamageCause();
		if (entityDamageEvent instanceof EntityDamageByEntityEvent damageEvent)
		{
			Entity damager = damageEvent.getDamager();
			if (damager instanceof AreaEffectCloud areaEffectCloud)
			{
				return areaEffectCloud;
			}
		}
		return null;
	}

	protected static boolean isMelee(EntityDeathEvent event)
	{
		LivingEntity livingEntity = event.getEntity();
		EntityDamageEvent entityDamageEvent = livingEntity.getLastDamageCause();
		if (entityDamageEvent instanceof EntityDamageByEntityEvent damageEvent)
		{
			Entity damager = damageEvent.getDamager();
			return damager instanceof LivingEntity;
		}
		return false;
	}

	@Nullable
	protected static ItemStack getWeapon(EntityDeathEvent event)
	{
		Object damagerObject = getDamager(event);
		if (damagerObject instanceof LivingEntity damager)
		{
			Entity entity = event.getEntity();
			// parrot cookie weapon
			EntityDamageEvent damageEvent = entity.getLastDamageCause();
			if (entity instanceof Parrot && damageEvent != null && damageEvent.getCause() == DamageCause.ENTITY_ATTACK)
			{
				ItemStack cookie = ItemSerializer.deserialize(Variable.attackerAndWeaponString.get(damager.getUniqueId()));
				if (ItemStackUtil.itemExists(cookie) && cookie.getType() == Material.COOKIE)
				{
					return cookie;
				}
			}
			EntityEquipment entityEquipment = damager.getEquipment();
			if (entityEquipment != null)
			{
				ItemStack mainHand = entityEquipment.getItemInMainHand();
				Projectile projectile = getDamagerProjectile(event);
				AreaEffectCloud areaEffectCloud = getDamagerAreaEffectCloud(event);
				if (isMelee(event))
				{
					return mainHand;
				}
				ItemStack weapon = null;
				if (areaEffectCloud != null)
				{
					weapon = Variable.projectile.get(areaEffectCloud.getUniqueId());
					Bukkit.getServer().getScheduler()
							.runTaskLater(Cucumbery.getPlugin(), () -> Variable.projectile.remove(areaEffectCloud.getUniqueId()), areaEffectCloud.getDuration());
				}
				if (Variable.attackerAndWeapon.containsKey(damager.getUniqueId()))
				{
					return Variable.attackerAndWeapon.get(damager.getUniqueId());
				}
				if (projectile != null)
				{
					weapon = Variable.projectile.get(projectile.getUniqueId());
					Variable.projectile.remove(projectile.getUniqueId());
				}
				return weapon;
			}
		}
		else
		{
			Projectile projectile = getDamagerProjectile(event);
			if (projectile != null && projectile.getShooter() instanceof BlockProjectileSource blockProjectileSource)
			{
				Block block = blockProjectileSource.getBlock();
				if (Variable.blockAttackerAndWeapon.containsKey(block.getLocation().toString()))
				{
					return Variable.blockAttackerAndWeapon.get(block.getLocation().toString());
				}
			}
			if (!(damagerObject instanceof ItemStack) && damagerObject != null && damagerObject.equals(projectile))
			{
				return null;
			}
			if (projectile instanceof EnderPearl && event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent damageByEntityEvent
					&& damageByEntityEvent.getCause() == EntityDamageEvent.DamageCause.FALL)
			{
				return null;
			}
			if (projectile != null && Variable.projectile.containsKey(projectile.getUniqueId()))
			{
				return Variable.projectile.get(projectile.getUniqueId());
			}

			if (projectile instanceof ThrowableProjectile throwableProjectile)
			{
				return throwableProjectile.getItem();
			}
			else if (projectile instanceof AbstractArrow abstractArrow)
			{
				return Method.usingLoreFeature(projectile.getLocation()) ? ItemLore.setItemLore(abstractArrow.getItemStack()) : abstractArrow.getItemStack();
			}
			else
			{
				if (projectile instanceof Firework firework)
				{
					ItemStack itemStack = new ItemStack(Material.FIREWORK_ROCKET);
					itemStack.setItemMeta(firework.getFireworkMeta());
					return Method.usingLoreFeature(projectile.getLocation()) ? ItemLore.setItemLore(itemStack) : itemStack;
				}
			}
			AreaEffectCloud areaEffectCloud = getDamagerAreaEffectCloud(event);
			if (areaEffectCloud != null)
			{
				if (Variable.projectile.containsKey(areaEffectCloud.getUniqueId()))
				{
					return Variable.projectile.get(areaEffectCloud.getUniqueId());
				}
				if (areaEffectCloud.getSource() instanceof BlockProjectileSource blockProjectileSource)
				{
					Block block = blockProjectileSource.getBlock();
					if (Variable.blockAttackerAndWeapon.containsKey(block.getLocation().toString()))
					{
						return Variable.blockAttackerAndWeapon.get(block.getLocation().toString());
					}
				}
			}
		}
		return null;
	}
}

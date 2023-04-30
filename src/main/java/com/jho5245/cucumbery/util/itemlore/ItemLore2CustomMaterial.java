package com.jho5245.cucumbery.util.itemlore;

import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeCustomMining;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.Method2;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class ItemLore2CustomMaterial
{
  protected static void setItemLore(@Nullable Player viewer, @NotNull CustomMaterial customMaterial, @NotNull ItemStack itemStack, @NotNull ItemMeta itemMeta, @NotNull List<Component> lore)
  {
    NBTItem nbtItem = new NBTItem(itemStack);
    switch (customMaterial)
    {
      case MINER_HELMET ->
      {
        if (viewer != null && CustomEffectManager.hasEffect(viewer, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&6채광 속도 : %s", Constant.THE_COLOR_HEX + "+15"));
          lore.add(ComponentUtil.translate("&6채광 행운 : %s", Constant.THE_COLOR_HEX + "+5"));
        }
      }
      case MINER_CHESTPLATE -> {
        if (viewer != null && CustomEffectManager.hasEffect(viewer, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&6채광 속도 : %s", Constant.THE_COLOR_HEX + "+20"));
          lore.add(ComponentUtil.translate("&6채광 행운 : %s", Constant.THE_COLOR_HEX + "+10"));
        }
      }
      case MINER_LEGGINGS -> {
        if (viewer != null && CustomEffectManager.hasEffect(viewer, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&6채광 속도 : %s", Constant.THE_COLOR_HEX + "+20"));
          lore.add(ComponentUtil.translate("&6채광 행운 : %s", Constant.THE_COLOR_HEX + "+5"));
        }
      }
      case MINER_BOOTS -> {
        if (viewer != null && CustomEffectManager.hasEffect(viewer, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&6채광 속도 : %s", Constant.THE_COLOR_HEX + "+10"));
          lore.add(ComponentUtil.translate("&6채광 행운 : %s", Constant.THE_COLOR_HEX + "+5"));
        }
      }
      case MINDAS_HELMET ->
      {
        if (viewer != null && CustomEffectManager.hasEffect(viewer, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&6채광 속도 : %s", Constant.THE_COLOR_HEX + "+250"));
          lore.add(ComponentUtil.translate("&6채광 행운 : %s", Constant.THE_COLOR_HEX + "+80"));
        }
      }
      case MINDAS_CHESTPLATE -> {
        if (viewer != null && CustomEffectManager.hasEffect(viewer, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&6채광 속도 : %s", Constant.THE_COLOR_HEX + "+400"));
          lore.add(ComponentUtil.translate("&6채광 행운 : %s", Constant.THE_COLOR_HEX + "+120"));
        }
      }
      case MINDAS_LEGGINGS -> {
        if (viewer != null && CustomEffectManager.hasEffect(viewer, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&6채광 속도 : %s", Constant.THE_COLOR_HEX + "+350"));
          lore.add(ComponentUtil.translate("&6채광 행운 : %s", Constant.THE_COLOR_HEX + "+110"));
        }
      }
      case MINDAS_BOOTS -> {
        if (viewer != null && CustomEffectManager.hasEffect(viewer, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&6채광 속도 : %s", Constant.THE_COLOR_HEX + "+300"));
          lore.add(ComponentUtil.translate("&6채광 행운 : %s", Constant.THE_COLOR_HEX + "+100"));
        }
      }
    }
    switch (customMaterial)
    {
      case THE_MUSIC ->
      {
        itemMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&6능력 : %s %s", ComponentUtil.translate("&a'그 노래' 재생"), ComponentUtil.translate("&e&l우클릭")));
        lore.add(ComponentUtil.translate("&7재사용 대기시간 : %s", ComponentUtil.translate("&a%s초", 10)));
      }
      case MITHRIL_PICKAXE ->
      {
        if (viewer != null && CustomEffectManager.hasEffect(viewer, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&7%s 채광 시 채광 행운 15 증가", CustomMaterial.MITHRIL_ORE.getDisplayName()));
        }
      }
      case MITHRIL_PICKAXE_REFINED ->
      {
        if (viewer != null && CustomEffectManager.hasEffect(viewer, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&7%s 채광 시 채광 속도 50 증가", CustomMaterial.MITHRIL_ORE.getDisplayName()));
        }
      }
      case TITANIUM_PICKAXE ->
      {
        if (viewer != null && CustomEffectManager.hasEffect(viewer, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&7%s 채광 시 채광 행운 20 증가", CustomMaterial.TITANIUM_ORE.getDisplayName()));
        }
      }
      case TITANIUM_PICKAXE_REFINED ->
      {
        if (viewer != null && CustomEffectManager.hasEffect(viewer, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&7%s 채광 시 채광 속도 60 증가", CustomMaterial.TITANIUM_ORE.getDisplayName()));
        }
      }
      case STONK ->
      {
        if (viewer != null && CustomEffectManager.hasEffect(viewer, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&7%s, %s, %s 또는 %s 채광 시 채광 속도 10000 증가", Material.STONE, Material.DEEPSLATE, Material.COBBLESTONE, Material.COBBLED_DEEPSLATE));
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&6능력 : %s %s", ComponentUtil.translate("&a채광 부스터"), ComponentUtil.translate("&e&l우클릭")));
          lore.add(ComponentUtil.translate("&f10초간 채광 속도가 3배로 증가"));
          lore.add(ComponentUtil.translate("&7재사용 대기시간 : %s", ComponentUtil.translate("&a%s초", 120)));
        }
      }
      case PORTABLE_CRAFTING_TABLE ->
      {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&6능력 : %s %s", ComponentUtil.translate("&a작업대 열기"), ComponentUtil.translate("&e&l우클릭")));
      }
      case PORTABLE_ENDER_CHEST ->
      {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&6능력 : %s %s", ComponentUtil.translate("&a엔더 상자 열기"), ComponentUtil.translate("&e&l우클릭")));
      }
      case TODWOT_PICKAXE ->
      {
        if (viewer != null && CustomEffectManager.hasEffect(viewer, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&7%s 확률로 %s을(를) 추가로 드롭한다", "rg255,204;5%", Material.BONE));
        }
      }
      case SANS_BOOTS, SANS_CHESTPLATE, SANS_LEGGINGS, SANS_HELMET ->
      {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&6세트 능력 : %s", ComponentUtil.translate("&a와 샌즈!")));
        lore.add(ComponentUtil.translate("&7스켈레톤에게 어그로가 끌리지 않음"));
      }
      case FROG_HELMET, FROG_CHESTPLATE, FROG_LEGGINGS, FROG_BOOTS ->
      {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&6세트 능력 : %s", ComponentUtil.translate("&a개구리 점프")));
        lore.add(ComponentUtil.translate("&7영구적으로 %s 4단계 효과 적용", PotionEffectType.JUMP));
      }
      case MINER_HELMET, MINER_CHESTPLATE, MINER_LEGGINGS, MINER_BOOTS ->
      {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&6세트 능력 : %s", ComponentUtil.translate("&a노동력 충만")));
        lore.add(ComponentUtil.translate("&7영구적으로 %s 1단계 효과 적용", PotionEffectType.FAST_DIGGING));
      }
      case FLINT_SHOVEL ->
      {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7%s 채굴 시 %s 확률로 %s을(를) 드롭함", Material.GRAVEL, "&a100%", Material.FLINT));
      }
      case SMALL_DRILL_FUEL ->
      {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7모루에서 드릴과 함께 사용하여"));
        lore.add(ComponentUtil.translate("&7드릴의 연료를 100 충전할 수 있다"));
      }
      case MEDIUM_DRILL_FUEL ->
      {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7모루에서 드릴과 함께 사용하여"));
        lore.add(ComponentUtil.translate("&7드릴의 연료를 1000 충전할 수 있다"));
      }
      case LARGE_DRILL_FUEL ->
      {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7모루에서 드릴과 함께 사용하여"));
        lore.add(ComponentUtil.translate("&7드릴의 연료를 10000 충전할 수 있다"));
      }
      case SPYGLASS_TELEPORT ->
      {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&6능력 : %s %s", ComponentUtil.translate("&a보아라, 닿아라"), ComponentUtil.translate("&e&l우클릭")));
        lore.add(ComponentUtil.translate("&7망원경을 3초간 우클릭하고 있으면"));
        lore.add(ComponentUtil.translate("&7바라보고 있는 블록으로 순간 이동한다"));
        lore.add(ComponentUtil.translate("&7최대 100블록 거리까지만 이동 가능"));
        lore.add(ComponentUtil.translate("&e단, 너무 가까운 곳으로는 사용이 불가능하며"));
        lore.add(ComponentUtil.translate("&e우클릭 도중 움직이거나 우클릭을 멈추면 순간 이동이 취소됨"));
        lore.add(ComponentUtil.translate("&7재사용 대기시간 : %s", ComponentUtil.translate("&a%s초", 60)));
      }
      case SMALL_MINING_SACK ->
      {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7광물 종류를 담을 수 있는 가방입니다!"));
        NBTCompound sack = nbtItem.addCompound("Sack");
        int capacity = sack.getInteger("Capacity");
        int current = 0;
        NBTCompound items = sack.addCompound("Items");
        for (String key : items.getKeys())
        {
          current += items.getInteger(key);
        }
        lore.add(ComponentUtil.translate("&a용량 : %s / %s", current, capacity));
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&a[담을 수 있는 아이템들]"));
        lore.add(ComponentUtil.translate("&a%s, %s, %s, %s, %s, %s",
                Material.STONE, Material.COBBLESTONE, CustomMaterial.MITHRIL_ORE, CustomMaterial.MITHRIL_INGOT, CustomMaterial.TITANIUM_ORE, CustomMaterial.TITANIUM_INGOT));
      }
      case TNT_COMBAT -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7전투형으로 특별히 개량된 TNT로 블록을 파괴"));
        lore.add(ComponentUtil.translate("&7하지 않으며, 개체에게 폭발 피해만 입힘"));
      }
      case TNT_DRAIN -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7다른 %s보다 넓은 범위를 가지고 보다 빠르게(약 1초) 폭발하며", ItemNameUtil.itemName(Material.TNT)));
        lore.add(ComponentUtil.translate("&7블록을 파괴하거나 개체에게 피해를 입히지 않으며 범위 내에 있는"));
        lore.add(ComponentUtil.translate("&7물 관련 블록을 전부 제거하고 다른 %s와(과) 연쇄 폭발할 수 있음", CustomMaterial.TNT_DRAIN.getDisplayName()));
        lore.add(ComponentUtil.translate("&e단, 켈프와 해초는 해당 블록을 파괴할 수 없는 상태이거나"));
        lore.add(ComponentUtil.translate("&e레드스톤 회로를 통해 TNT에 불을 붙인 경우에는 파괴되지 않고"));
        lore.add(ComponentUtil.translate("&e%s을(를) 제외한 다른 TNT와는 연쇄 폭발을 일으키지 않음", CustomMaterial.TNT_DRAIN.getDisplayName()));
      }
      case FIREWORK_ROCKET_EXPLOSIVE -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7블록을 파괴하지 않는 강도 4의 폭발을 발생시킴"));
      }
      case FIREWORK_ROCKET_EXPLOSIVE_DESTRUCTION -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7블록을 파괴하는 강도 4의 폭발을 발생시킴"));
      }
      case FIREWORK_ROCKET_EXPLOSIVE_FLAME -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7블록을 파괴하지 않으나 불이 나는 강도 4의 폭발을 발생시킴"));
      }
      case ARROW_CRIT -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&733% 확률로 대미지 2배로 증가"));
        lore.add(ComponentUtil.translate("&7%s 마법의 영향을 받지 않음", Enchantment.ARROW_INFINITE));
      }
      case ARROW_EXPLOSIVE -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7블록을 파괴하지 않는 강도 2의 폭발을 적중 위치에 발생시킴"));
        lore.add(ComponentUtil.translate("&7%s 마법의 영향을 받지 않음", Enchantment.ARROW_INFINITE));
      }
      case ARROW_EXPLOSIVE_DESTRUCTION -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7블록을 파괴하는 강도 2의 폭발을 적중 위치에 발생시킴"));
        lore.add(ComponentUtil.translate("&7%s 마법의 영향을 받지 않음", Enchantment.ARROW_INFINITE));
      }
      case ARROW_FLAME -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7%s 마법의 영향을 받지 않음", Enchantment.ARROW_INFINITE));
      }
      case ARROW_INFINITE -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7활과 함께 사용 시 사라지지 않음"));
        lore.add(ComponentUtil.translate("&7쇠뇌에는 아무런 효과가 없으며 적중하지 못한 화살은 주울 수 없음"));
      }
      case ARROW_MOUNT -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7자신이 발사한 화살에 탑승하여 같이 발사됨"));
        lore.add(ComponentUtil.translate("&7블록에 적중한 화살은 50% 확률로 파괴됨"));
        lore.add(ComponentUtil.translate("&7%s 마법의 영향을 받지 않음", Enchantment.ARROW_INFINITE));
      }
      case ARROW_MOUNT_INFINITE -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7자신이 발사한 화살에 탑승하여 같이 발사됨"));
        lore.add(ComponentUtil.translate("&7블록에 적중해도 화살은 파괴되지 않음"));
        lore.add(ComponentUtil.translate("&7%s 마법의 영향을 받지 않음", Enchantment.ARROW_INFINITE));
      }
      case ARROW_MOUNT_DISPOSAL -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7자신이 발사한 화살에 탑승하여 같이 발사됨"));
        lore.add(ComponentUtil.translate("&7블록에 적중한 화살은 100% 확률로 파괴됨"));
        lore.add(ComponentUtil.translate("&7%s 마법의 영향을 받지 않음", Enchantment.ARROW_INFINITE));
      }
      case BOW_CRIT -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7사용 시 화살의 속성이 %s(으)로 변경됨", CustomMaterial.ARROW_CRIT));
        lore.addAll(ItemStackUtil.getItemInfoAsComponents(CustomMaterial.ARROW_CRIT.create(), null, null, true));
      }
      case BOW_ENDER_PEARL -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7화살 대신 엔더 진주를 발사함"));
        lore.add(ComponentUtil.translate("&7단, 반드시 %s 또는 %s만 엔더 진주를 발사할 수 있음", Material.ARROW, CustomMaterial.ARROW_INFINITE));
      }
      case BOW_EXPLOSIVE -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7사용 시 화살의 속성이 %s(으)로 변경됨", CustomMaterial.ARROW_EXPLOSIVE));
        lore.addAll(ItemStackUtil.getItemInfoAsComponents(CustomMaterial.ARROW_EXPLOSIVE.create(), null, null, true));
      }
      case BOW_EXPLOSIVE_DESTRUCTION -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7사용 시 화살의 속성이 %s(으)로 변경됨", CustomMaterial.ARROW_EXPLOSIVE_DESTRUCTION));
        lore.addAll(ItemStackUtil.getItemInfoAsComponents(CustomMaterial.ARROW_EXPLOSIVE_DESTRUCTION.create(), null, null, true));
      }
      case BOW_FLAME -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7사용 시 화살의 속성이 %s(으)로 변경됨", CustomMaterial.ARROW_FLAME));
        lore.addAll(ItemStackUtil.getItemInfoAsComponents(CustomMaterial.ARROW_FLAME.create(), null, null, true));
      }
      case BOW_INFINITE -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7화살이 소모되지 않으나 적중하지 못한 화살은"));
        lore.add(ComponentUtil.translate("&7주울 수 없고 일부 화살에는 적용되지 않음"));
      }
      case BOW_MOUNT -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7사용 시 화살의 속성이 %s(으)로 변경됨", ItemNameUtil.itemName(CustomMaterial.ARROW_MOUNT.create())));
        lore.addAll(ItemStackUtil.getItemInfoAsComponents(CustomMaterial.ARROW_MOUNT.create(), null, null, true));
      }
      case RAINBOW_HELMET, RAINBOW_BOOTS, RAINBOW_CHESTPLATE, RAINBOW_LEGGINGS -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7장착하면 지속적으로 색깔이 변한다"));
      }
      case SPIDER_BOOTS -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&6능력 : %s %s", ComponentUtil.translate("&a더블 점프"), ComponentUtil.translate("&e&l웅크리기")));
        lore.add(ComponentUtil.translate("&7공중에서 웅크리면 더블 점프가 발동된다"));
        lore.add(ComponentUtil.translate("&e단, 특정한 위치에서는 능력이 비활성화된다"));
        lore.add(ComponentUtil.translate("&8음식 포인트 비용 : %s", "&31"));
      }
      case UNBINDING_SHEARS -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7장착하고 있는 귀속 저주 마법이 부여된"));
        lore.add(ComponentUtil.translate("&7아이템을 벗을 수 있게 해준다"));
        lore.add(ComponentUtil.translate("&7현재 장착하고 있는 아이템에 우클릭하여"));
        lore.add(ComponentUtil.translate("&7사용할 수 있고 한 번 사용하면 사라진다"));
        lore.add(ComponentUtil.translate("&e일부 아이템에는 사용할 수 없다"));
      }
      case TRACKER -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7특정 개체에게 웅크리고 우클릭하면"));
        lore.add(ComponentUtil.translate("&7해당 개체의 위치를 알 수 있다"));
        lore.add(ComponentUtil.translate("&e플레이어를 포함한 일부 개체에게는 사용할 수 없다"));
        Component track = ComponentUtil.translate("&c없음");
        String uuidStr = nbtItem.getString("Tracking");
        if (uuidStr != null && Method.isUUID(uuidStr))
        {
          UUID uuid = UUID.fromString(uuidStr);
          Entity entity = Method2.getEntityAsync(uuid);
          if (entity == null)
          {
            track = ComponentUtil.translate("&c잘못된 개체");
          }
          else {
            track = ComponentUtil.create(entity);
          }
        }
        lore.add(ComponentUtil.translate("&7현재 트래킹 중인 개체 : %s", track));
      }
      case TNT_DONUT -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7폭발 시 원 몰라 설명 대충"));
        lore.add(ComponentUtil.translate("&7수치 : %s", nbtItem.getShort("Size")));
      }
      case REDSTONE_BLOCK_INSTA_BREAK -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7설치 즉시 파괴됩니다! 짧은 레드스톤"));
        lore.add(ComponentUtil.translate("&7신호가 필요한 곳에 사용하세요!"));
      }
      case CUSTOM_CRAFTING_TABLE -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7야생에서 일반적으로 얻을 수 없는"));
        lore.add(ComponentUtil.translate("&7아이템을 제작할 수 있다."));
      }
      case CUSTOM_CRAFTING_TABLE_PORTABLE -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7야생에서 일반적으로 얻을 수 없는"));
        lore.add(ComponentUtil.translate("&7아이템을 제작할 수 있다."));
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&6능력 : %s %s", ComponentUtil.translate("&a커스텀 제작대 열기"), ComponentUtil.translate("&e&l우클릭")));
      }
    }
    switch (customMaterial)
    {
      case FROG_BOOTS ->
      {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&6능력 : %s %s", ComponentUtil.translate("&a개구리 착지")));
        lore.add(ComponentUtil.translate("&7낙하 대미지 20% 감소"));
      }
      case MINER_HELMET, MINDAS_HELMET ->
      {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&6능력 : %s %s", ComponentUtil.translate("&a광부의 시야")));
        lore.add(ComponentUtil.translate("&7영구적으로 %s 효과 적용", PotionEffectType.NIGHT_VISION));
      }
    }
  }
}

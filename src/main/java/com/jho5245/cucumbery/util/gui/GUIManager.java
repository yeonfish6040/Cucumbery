package com.jho5245.cucumbery.util.gui;

import com.jho5245.cucumbery.commands.no_groups.CommandStash;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.CreateItemStack;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class GUIManager
{
  public static void openGUI(@NotNull Player player, @NotNull GUIType gui)
  {
    openGUI(player, gui, true);
  }

  public static void openGUI(@NotNull Player player, @NotNull GUIType gui, boolean firstOpen)
  {
    switch (gui)
    {
      case MAIN_MENU -> mainMenu(player);
      case SERVER_SETTINGS -> serverMenu(player);
      case SERVER_SETTINGS_ADMIN -> serverAdminMenu(player);
      case ITEM_DROP_MODE_MENU -> itemDropModeMenu(player);
      case ITEM_PICKUP_MODE_MENU -> itemPickupModeMenu(player);
      case ITEM_STASH -> CommandStash.openStash(player, firstOpen);
    }
  }

  private static void mainMenu(Player player)
  {
    Inventory inv = Bukkit.createInventory(null, 9, Constant.CANCEL_STRING + Constant.MAIN_MENU);
    inv.setItem(3, CreateItemStack.create(Material.TRIPWIRE_HOOK, 1, "&b개인 설정", "&7서버에서 제공하는 몇몇 기능들을 설정합니다", true));
    inv.setItem(5, CreateItemStack.create(Material.CRAFTING_TABLE, 1, "&e아이템 제작", Arrays.asList("&7아이템 제작 메뉴를 엽니다", "&eEpicCraftingsPlus&7 플러그인이 일을 안해서 내가 직접 만들었다"), true));

    player.openInventory(inv);

    InventoryView lastInventory = getLastInventory(player.getUniqueId());
    if (lastInventory != null)
    {
      inv.setItem(0, CreateItemStack.getPreviousButton(lastInventory.title()));
    }
  }

  private static void serverMenu(Player player)
  {
    Inventory inv = Bukkit.createInventory(null, 54, Constant.CANCEL_STRING + Constant.SERVER_SETTINGS);
    inv.setItem(1, CreateItemStack.toggleItem(UserData.LISTEN_JOIN.getBoolean(player.getUniqueId()), "&b입장 소리 듣기",
            Arrays.asList("&7다른 플레이어가 서버에 입장할 때 효과음을 듣습니다", "&e해당 기능을 사용하지 않아도", "&e일부 유저(관리자 등)가 입장할 때는 효과음이 들릴 수 있습니다", "", "&6현재 설정 : &a입장 효과음을 듣습니다", "", "&c클릭하면 해당 기능을 사용하지 않습니다"), "&b입장 소리 듣기",
            Arrays.asList("&7다른 플레이어가 서버에 입장할 때 효과음을 듣습니다", "&e해당 기능을 사용하지 않아도", "&e일부 유저(관리자 등)가 입장할 때는 효과음이 들릴 수 있습니다", "", "&6현재 설정 : &c입장 효과음을 듣지 않습니다", "", "&a클릭하면 해당 기능을 사용합니다")));
    inv.setItem(2, CreateItemStack
            .toggleItem(UserData.SHOW_JOIN_TITLE.getBoolean(player.getUniqueId()), "&b입장 시 타이틀 보기", Arrays.asList("&7서버에 접속할때 환명 메시지 타이틀을 봅니다", "", "&6현재 설정 : &a입장 타이틀을 봅니다", "", "&c클릭하면 타이틀을 띄우지 않습니다"),
                    "&b입장 시 타이틀 보기", Arrays.asList("&7서버에 접속할때 환명 메시지 타이틀을 봅니다", "", "&6현재 설정 : &c입장 타이틀을 보지 않습니다", "", "&a클릭하면 타이틀을 띄웁니다")));
    inv.setItem(3, CreateItemStack.toggleItem(UserData.LISTEN_CHAT.getBoolean(player.getUniqueId()), "&b채팅 소리 듣기",
            Arrays.asList("&7상대방과 자신이 채팅을 할때 소리를 듣습니다", "&e해당 기능을 사용하지 않아도", "&e일부 유저(관리자 등)의 채팅 소리는 들릴 수 있습니다", "", "&6현재 설정 : &a채팅 소리를 듣습니다", "", "&c클릭하면 소리를 끕니다"), "&b채팅 소리 듣기",
            Arrays.asList("&7상대방과 자신이 채팅을 할때 소리를 듣습니다", "&e해당 기능을 사용하지 않아도", "&e일부 유저(관리자 등)의 채팅 소리는 들릴 수 있습니다", "", "&6현재 설정 : &c채팅 소리를 듣지 않습니다", "", "&a클릭하면 소리를 켭니다")));
    inv.setItem(4, CreateItemStack
            .toggleItem(UserData.LISTEN_COMMAND.getBoolean(player.getUniqueId()), "&b명령어 소리 듣기", Arrays.asList("&7명령어를 입력할 때 소리를 듣습니다", "&e특정 상황에서는 해당 기능을 켜도 소리가 들리지 않을 수 있습니다", "", "&6현재 설정 : &a명령어 입력 소리를 듣습니다", "", "&c클릭하면 소리를 끕니다"),
                    "&b명령어 소리 듣기", Arrays.asList("&7명령어를 입력할 때 소리를 듣습니다", "", "&6현재 설정 : &c명령어 입력 소리를 듣지 않습니다", "", "&a클릭하면 소리를 켭니다")));
    inv.setItem(5, CreateItemStack.toggleItem(UserData.LISTEN_GLOBAL.getBoolean(player.getUniqueId()), "&b서버 라디오 사용",
            Arrays.asList("&7서버에서 전체 플레이어에게 재생되는 소리를 듣습니다", "&e해당 기능을 사용하지 않아도", "&e일부 라디오 소리는 들릴 수 있습니다", "", "&6현재 설정 : &a서버 라디오를 사용합니다", "", "&c클릭하면 서버 라디오를 사용하지 않습니다"), "&b서버 라디오 사용",
            Arrays.asList("&7서버에서 전체 플레이어에게 재생되는 소리를 듣습니다", "&e해당 기능을 사용하지 않아도", "&e일부 라디오 소리는 들릴 수 있습니다", "", "&6현재 설정 : &c서버 라디오를 사용하지 않습니다", "", "&a클릭하면 서버 라디오를 사용합니다")));
    inv.setItem(6, CreateItemStack.toggleItem(UserData.SERVER_RESOURCEPACK.getBoolean(player.getUniqueId()), "&b서버 리소스팩 소리 사용",
            Arrays.asList("&7서버에서 제공하는 소리(마인카트, 강화)를 사용합니다", "", "&6현재 설정 : &a서버 리소스팩 소리를 사용합니다", "", "&c클릭하면 소리를 사용하지 않습니다"), "&b서버 리소스팩 소리 사용",
            Arrays.asList("&7서버에서 제공하는 소리(마인카트, 강화)를 사용합니다", "", "&6현재 설정 : &c서버 리소스팩 소리를 사용하지 않습니다", "", "&a클릭하면 소리를 사용합니다")));
    inv.setItem(10, CreateItemStack.toggleItem(UserData.LISTEN_QUIT.getBoolean(player.getUniqueId()), "&b퇴장 소리 듣기",
            Arrays.asList("&7다른 플레이어가 서버에서 퇴장할 때 효과음을 듣습니다", "&e해당 기능을 사용하지 않아도", "&e일부 유저(관리자 등)가 퇴장할 때는 효과음이 들릴 수 있습니다", "", "&6현재 설정 : &a퇴장 효과음을 듣습니다", "", "&c클릭하면 해당 기능을 사용하지 않습니다"), "&b퇴장 소리 듣기",
            Arrays.asList("&7다른 플레이어가 서버에서 퇴장할 때 효과음을 듣습니다", "&e해당 기능을 사용하지 않아도", "&e일부 유저(관리자 등)가 퇴장할 때는 효과음이 들릴 수 있습니다", "", "&6현재 설정 : &c퇴장 효과음을 듣지 않습니다", "", "&a클릭하면 해당 기능을 사용합니다")));
    inv.setItem(11, CreateItemStack.toggleItem(UserData.SHOW_ACTIONBAR_ON_ATTACK.getBoolean(player.getUniqueId()), "&b공격할 때 액션바 띄움",
            Arrays.asList("&7전투와 PVP를 할 때 액션바로 HP를 봅니다", "", "&6현재 설정 : &a액션바를 띄웁니다", "", "&c클릭하면 액션바를 띄우지 않습니다"), "&b공격할 때 액션바 띄움",
            Arrays.asList("&7전투와 PVP를 할 때 액션바로 HP를 봅니다", "", "&6현재 설정 : &c액션바를 띄우지 않습니다", "", "&a클릭하면 액션바를 띄웁니다")));
    inv.setItem(12, CreateItemStack.toggleItem(UserData.SHOW_ACTIONBAR_ON_ATTACK_PVP.getBoolean(player.getUniqueId()), "&bPvP할 때 액션바 띄움", Arrays
            .asList("&7PvP를 할 때 액션바로 HP를 봅니다", "&7공격할 때 액션바 띄움 기능을 사용하지 않으면", "&7이 기능을 사용해도 액션바를 띄우지 않습니다", "&e해당 기능을 사용해도 일부 유저(관리자 등)의", "&eHP는 보이지 않을 수 있습니다", "", "&6현재 설정 : &a액션바를 띄웁니다", "",
                    "&c클릭하면 액션바를 띄우지 않습니다"), "&bPvP할 때 액션바 띄움", Arrays
            .asList("&7PvP를 할 때 액션바로 HP를 봅니다", "&7공격할 때 액션바 띄움 기능을 사용하지 않으면", "&7이 기능을 사용해도 액션바를 띄우지 않습니다", "&e해당 기능을 사용해도 일부 유저(관리자 등)의", "&eHP는 보이지 않을 수 있습니다", "", "&6현재 설정 : &c액션바를 띄우지 않습니다", "",
                    "&a클릭하면 액션바를 띄웁니다")));
    inv.setItem(13, CreateItemStack.toggleItem(UserData.FIREWORK_LAUNCH_ON_AIR.getBoolean(player.getUniqueId()), "&b폭죽 즉시 발사",
            Arrays.asList("&7폭죽을 발사할 때 블록을 바라보지 않고", ComponentUtil.translate("&7%s 키로 즉시 발사할 수 있습니다", Component.keybind("key.use")), "&a웅크린 상태에서 발사하면 바라보는 방향으로 발사합니다", "", "&6현재 설정 : &a즉시 발사 기능을 사용합니다", "", "&c클릭하면 기능을 사용하지 않습니다"), "&b폭죽 즉시 발사",
            Arrays.asList("&7폭죽을 발사할 때 블록을 바라보지 않고", ComponentUtil.translate("&7%s 키로 즉시 발사할 수 있습니다", Component.keybind("key.use")), "&a웅크린 상태에서 발사하면 바라보는 방향으로 발사합니다", "", "&6현재 설정 : &c즉시 발사 기능을 사용하지 않습니다", "", "&a클릭하면 기능을 사용합니다")));


    inv.setItem(14, CreateItemStack.toggleItem(UserData.TRAMPLE_SOIL.getBoolean(player.getUniqueId()), "&b경작지 파괴 방지 기능 사용", Arrays
            .asList("&7경작지에서 점프를 했을때 경작지가 흙으로 바뀌지 않게 해줍니다", "&7플레이어가 아닌 개체는 여전히 경작지를 파괴할 수 있습니다", "&e특정한 상황에서는 해당 기능을 사용해도", "&e경작지가 흙으로 바뀔 수 있습니다", "", "&6현재 설정 : &a점프해도 경작지가 파괴되지 않습니다", "",
                    "&c클릭하면 파괴 방지 기능을 사용하지 않습니다"), "&b경작지 파괴 방지 기능 사용", Arrays
            .asList("&7경작지에서 점프를 했을때 경작지가 흙으로 바뀌지 않게 해줍니다", "&7플레이어가 아닌 개체는 여전히 경작지를 파괴할 수 있습니다", "&e특정한 상황에서는 해당 기능을 사용해도", "&e경작지가 흙으로 바뀔 수 있습니다", "", "&6현재 설정 : &c점프하면 경작지가 파괴됩니다", "",
                    "&a클릭하면 파괴 방지 기능을 사용합니다")));
    if (Permission.GUI_SERVER_SETTINGS_ADMIN.has(player))
    {
      inv.setItem(18, CreateItemStack.create(Material.COMMAND_BLOCK, 1, "&b관리자 전용 설정", Arrays.asList("&7관리자 전용 개인 설정 메뉴를 엽니다", "&7아직 귀찮아서 미완성인게 많습니다 ㅁㄴㅇㄹ"), true));
    }
    List<String> trueLore = Arrays.asList("", "&6현재 설정 : &a켜짐", "", "&c클릭하면 해당 기능을 끕니다");
    List<String> falseLore = Arrays.asList("", "&6현재 설정 : &c꺼짐", "", "&a클릭하면 해당 기능을 켭니다");

    inv.setItem(19, CreateItemStack
          .toggleItem(getBool(player, UserData.SHOW_DEATH_SELF_MESSAGE), "&b자신의 데스 메시지 표시",
                  Arrays.asList("&7플레이어가 사망할 때 또는 자신이 공격한", "&7다른 개체가 죽을 때 데스 메시지를 표시합니다"), trueLore, falseLore));

    inv.setItem(28, CreateItemStack
          .toggleItem(getBool(player, UserData.SHOW_DEATH_MESSAGE), "&b데스 메시지 표시",
                  Arrays.asList("&7자신이 공격하지 않은 다른 개체가", "&7죽을 때 데스 메시지를 표시합니다"), trueLore, falseLore));

    inv.setItem(37, CreateItemStack
          .toggleItem(getBool(player, UserData.SHOW_DEATH_PVP_MESSAGE), "&bPvP 데스 메시지 표시",
                  Arrays.asList("&7자신이 공격하지 않은 다른 플레이어가", "&7PvP를 하여 죽을 때 데스 메시지를 표시합니다"), trueLore, falseLore));


    inv.setItem(15, CreateItemStack.toggleItem(UserData.TRAMPLE_SOIL_ALERT.getBoolean(player.getUniqueId()), "&b경작지 파괴 방지 기능 타이틀 띄움",
            Arrays.asList("&7경작지 파괴 방지 기능 사용으로 인해", "&7경작지 파괴가 막혔을때 타이틀을 띄워줍니다", "&e특정한 상황에서는 해당 기능을 사용해도", "&e타이틀이 뜨지 않을 수 있습니다", "", "&6현재 설정 : &a타이틀을 봅니다", "", "&c클릭하면 타이틀을 띄우지 않습니다"),
            "&b경작지 파괴 방지 기능 타이틀 띄움",
            Arrays.asList("&7경작지 파괴 방지 기능 사용으로 인해", "&7경작지 파괴가 막혔을때 타이틀을 띄워줍니다", "&e특정한 상황에서는 해당 기능을 사용해도", "&e타이틀이 뜨지 않을 수 있습니다", "", "&6현재 설정 : &c타이틀을 보지 않습니다", "", "&a클릭하면 타이틀을 띄웁니다")));

    if (player.getGameMode() == GameMode.CREATIVE)
    {
      inv.setItem(7, CreateItemStack.toggleItem(getBool(player, UserData.COPY_NOTE_BLOCK_VALUE_WHEN_SNEAKING), "&b[크리에이티브 전용] 웅크리기 상태에서만 소리 블록 값 복사",
              Arrays.asList("&7크리에이티브 모드에서 소리 블록을 픽블록으로 복사하여 ", "&7소리 블록의 악기/음높이를 복사해올 때", "&7웅크리기 상태에서만 복사를 합니다", "&7해당 기능을 사용하지 않으면 웅크리지", "&7않은 상태에서도 복사할 수 있습니다"), trueLore, falseLore));
      inv.setItem(16, CreateItemStack
              .toggleItem(getBool(player, UserData.COPY_NOTE_BLOCK_INSTRUMENT), "&b[크리에이티브 전용] 소리 블록 악기 복사", Arrays.asList("&7크리에이티브 모드에서 소리 블록을 픽블록으로 복사할 때", "&7해당 소리 블록의 악기를 복사합니다"), trueLore,
                      falseLore));
      inv.setItem(25, CreateItemStack
              .toggleItem(getBool(player, UserData.COPY_NOTE_BLOCK_PITCH), "&b[크리에이티브 전용] 소리 블록 음높이 복사", Arrays.asList("&7크리에이티브 모드에서 소리 블록을 픽블록으로 복사할 때", "&7해당 소리 블록의 음높이를 복사합니다"), trueLore, falseLore));
      inv.setItem(34, CreateItemStack.toggleItem(getBool(player, UserData.PLAY_NOTE_BLOCK_WHEN_SNEAKING_IN_CREATIVE_MODE), "&b[크리에이티브 전용] 웅크리기 상태에서 소리 블록 클릭으로 재생",
              Arrays.asList("&7크리에이티브 모드에서 웅크린 상태로 소리 블록을 클릭하거나", "&7검 형태의 무기 아이템으로 소리 블록을 클릭하면", "&7블록을 부수는 대신 소리를 재생합니다"), trueLore, falseLore));
      inv.setItem(43, CreateItemStack.toggleItem(getBool(player, UserData.INVERT_NOTE_BLOCK_PITCH_WHEN_SNEAKING_IN_CREATIVE_MODE), "&b[크리에이티브 전용] 웅크리기로 소리 블록 음높이 낮춤",
              Arrays.asList("&7크리에이티브 모드에서 웅크린 상태로 소리 블록을 우클릭하면", "&7음높이를 올리는 대신 낮춥니다"), trueLore, falseLore));
    }
    inv.setItem(20, CreateItemStack.toggleItem(UserData.LISTEN_HELDITEM.getBoolean(player.getUniqueId()), "&b인벤토리 도구 변경 소리 듣기",
            Arrays.asList("&7손에 들고 있는 아이템을 변경할 때 소리를 듣습니다", "", "&6현재 설정 : &a도구 변경 소리를 듣습니다", "", "&c클릭하면 소리를 끕니다"), "&b인벤토리 도구 변경 소리 듣기",
            Arrays.asList("&7손에 들고 있는 아이템을 변경할 때 소리를 듣습니다", "", "&6현재 설정 : &c도구 변경 소리를 듣지 않습니다", "", "&a클릭하면 소리를 켭니다")));
    inv.setItem(21, CreateItemStack.toggleItem(UserData.LISTEN_CONTAINER.getBoolean(player.getUniqueId()), "&b컨테이너 열고 닫는 소리 듣기",
            Arrays.asList("&7인벤토리(화로, 발사기, 마법 부여대, 양조기", "&7등) GUI를 열고 닫을때 소리를 듣습니다", "", "&6현재 설정 : &a컨테이너 소리를 듣습니다", "", "&c클릭하면 소리를 끕니다"), "&b컨테이너 열고 닫는 소리 듣기",
            Arrays.asList("&7인벤토리(화로, 발사기, 마법 부여대, 양조기", "&7등) GUI를 열고 닫을때 소리를 듣습니다", "", "&6현재 설정 : &c컨테이너 소리를 듣지 않습니다", "", "&a클릭하면 소리를 켭니다")));
    inv.setItem(22, CreateItemStack
            .toggleItem(UserData.LISTEN_ITEM_DROP.getBoolean(player.getUniqueId()), "&b아이템 버리는 소리 듣기", Arrays.asList("&7아이템을 버릴때 소리를 재생합니다", "", "&6현재 설정 : &a아이템을 버릴때 소리를 듣습니다", "", "&c클릭하면 소리를 끕니다"),
                    "&b아이템 버리는 소리 듣기", Arrays.asList("&7아이템을 버릴때 소리를 재생합니다", "", "&6현재 설정 : &c아이템을 버릴때 소리를 듣지 않습니다", "", "&a클릭하면 소리를 켭니다")));
    inv.setItem(23, CreateItemStack.toggleItem(UserData.SHOW_ACTIONBAR_ON_ITEM_DROP.getBoolean(player.getUniqueId()), "&b아이템 버릴때 액션바 띄움",
            Arrays.asList("&7아이템을 버릴때 액션바를 띄웁니다", "", "&6현재 설정 : &a아이템을 버릴때 액션바를 띄웁니다", "", "&c클릭하면 액션바를 띄우지 않습니다"), "&b아이템 버릴때 액션바 띄움",
            Arrays.asList("&7아이템을 버릴때 액션바를 띄웁니다", "", "&6현재 설정 : &c아이템을 버릴때 액션바를 띄우지 않습니다", "", "&a클릭하면 액션바를 띄웁니다")));
    inv.setItem(24, CreateItemStack.toggleItem(UserData.SHOW_ACTIONBAR_ON_ITEM_PICKUP.getBoolean(player.getUniqueId()), "&b아이템 주울때 액션바 띄움",
            Arrays.asList("&7아이템을 주울때 액션바를 띄웁니다", "", "&6현재 설정 : &a아이템을 주울때 액션바를 띄웁니다", "", "&c클릭하면 액션바를 띄우지 않습니다"), "&b아이템 주울때 액션바 띄움",
            Arrays.asList("&7아이템을 주울때 액션바를 띄웁니다", "", "&6현재 설정 : &c아이템을 주울때 액션바를 띄우지 않습니다", "", "&a클릭하면 액션바를 띄웁니다")));

    inv.setItem(29, CreateItemStack
            .toggleItem(getBool(player, UserData.SHOW_ITEM_BREAK_TITLE), "&b장비 파괴 타이틀 띄움", Arrays.asList("&7인벤토리 내의 장비의 내구도가 다 소진되어", "&7장비가 파괴되었을때 장비의 이름을 타이틀로 띄웁니다"), trueLore, falseLore));
    inv.setItem(30, CreateItemStack.toggleItem(getBool(player, UserData.NOTIFY_IF_INVENTORY_IS_FULL), "&b인벤토리 꽉참 알림 띄움",
            Arrays.asList("&7인벤토리가 가득 찼을때 타이틀과", "&7채팅창에 경고 메시지를 띄워줍니다", "&e특정 상황에서는 해당 기능을 사용해도", "&e경고 메시지가 뜨지 않을 수 있습니다"), trueLore, falseLore));
    inv.setItem(31, CreateItemStack.toggleItem(getBool(player, UserData.SHOW_ACTIONBAR_WHEN_ITEM_IS_COOLDOWN), "&b아이템 재사용/재발동 대기시간 액션바",
            Arrays.asList("&7아이템의 남은 재사용 대기 시간 혹은 ", "&7남은 재발동 대기 시간을 액션바에 띄웁니다", "&e특정 상황에서는 해당 기능을 사용해도", "&e액션바가 뜨지 않을 수 있습니다"), trueLore, falseLore));

    List<String> list = new ArrayList<>(Arrays.asList(
            "&7바닥에 떨어져 있는 아이템의 이름을 표시합니다", "&e특정 상황에서 또는 특정 아이템은 해당 기능에 관계없이", "&e이름이 표시되지 않거나 항상 표시될 수 있습니다"
    ));

    if (UserData.FORCE_HIDE_DROPPED_ITEM_CUSTOM_NAME.getBoolean(player))
    {
      list.add("");
      list.add("&c현재 바닥에 떨어진 아이템 이름을 볼 수 없는 상태입니다");
    }

//    inv.setItem(32, CreateItemStack.toggleItem(getBool(player, UserData.SHOW_DROPPED_ITEM_CUSTOM_NAME), "&b바닥에 떨어진 아이템 이름 표시",
//            list, trueLore, falseLore));
    inv.setItem(33, CreateItemStack.toggleItem(getBool(player, UserData.SHOW_DAMAGE_INDICATOR), "&b대미지 숫자 표시",
            List.of("&7몬스터 혹은 다른 플레이어가 대미지를 입을 때", "&7해당 개체의 상단에 대미지 숫자를 표시합니다"), trueLore, falseLore));
    String itemDropMode = switch (UserData.ITEM_DROP_MODE.getString(player.getUniqueId()))
            {
              case "normal" -> "기본";
              case "sneak" -> "웅크리기";
              case "disabled" -> "비활성화";
              default -> "알 수 없음";
            };
    String itemPickupMode = switch (UserData.ITEM_PICKUP_MODE.getString(player.getUniqueId()))
            {
              case "normal" -> "기본";
              case "sneak" -> "웅크리기";
              case "disabled" -> "비활성화";
              default -> "알 수 없음";
            };
    inv.setItem(39, CreateItemStack
            .create(Material.EGG, 1, "&6아이템 버리기 모드 설정", Arrays.asList("&7아이템 버리기 모드를 설정합니다", "&7아이템 버리기 모드는 3가지가 있으며", "&e기본&7, &e웅크리기&7, &e비활성화&7가 있습니다", "", "&6현재 설정 : &e" + itemDropMode),
                    true));
    inv.setItem(41, CreateItemStack
            .create(Material.SPONGE, 1, "&6아이템 줍기 모드 설정", Arrays.asList("&7아이템 줍기 모드를 설정합니다", "&7아이템 줍기 모드는 3가지가 있으며", "&e기본&7, &e웅크리기&7, &e비활성화&7가 있습니다", "", "&6현재 설정 : &e" + itemPickupMode),
                    true));
    inv.setItem(53, CreateItemStack.create(Material.BOOKSHELF, 1, "&b메인 메뉴로", true));

    player.openInventory(inv);
    InventoryView lastInventory = getLastInventory(player.getUniqueId());
    if (lastInventory != null)
    {
      inv.setItem(45, CreateItemStack.getPreviousButton(lastInventory.title()));
    }
  }

  private static boolean getBool(Player player, UserData key)
  {
    return key.getBoolean(player.getUniqueId());
  }

  private static void serverAdminMenu(Player player)
  {
    Inventory inv = Bukkit.createInventory(null, 54, Constant.CANCEL_STRING + Constant.SERVER_SETTINGS_ADMIN);
    List<String> trueLore = Arrays.asList("", "&6현재 설정 : &a켜짐", "", "&c클릭하면 해당 기능을 끕니다");
    List<String> falseLore = Arrays.asList("", "&6현재 설정 : &c꺼짐", "", "&a클릭하면 해당 기능을 켭니다");
    inv.setItem(0, CreateItemStack
            .toggleItem(getBool(player, UserData.PLAY_JOIN), "&b입장 소리 재생", Arrays.asList("&e내부 아이디 : PLAY_JOIN", "", "&7서버에 입장할 때 다른 플레이어가 입장 효과음을 듣게 합니다", "&7해당 플레이어가 입장 소리 듣기를 꺼둔 상태면 들리지 않습니다"),
                    trueLore, falseLore));
    inv.setItem(1, CreateItemStack
            .toggleItem(getBool(player, UserData.PLAY_JOIN_FORCE), "&b입장 소리 무조건 재생", Arrays.asList("&e내부 아이디 : PLAY_JOIN_FORCE", "", "&7서버에 입장할 때 config와 플레이어들의 설정에 관계 없이", "&7반드시 입장 효과음을 듣게 합니다"),
                    trueLore, falseLore));
    inv.setItem(2, CreateItemStack
            .toggleItem(getBool(player, UserData.PLAY_QUIT), "&b퇴장 소리 재생", Arrays.asList("&e내부 아이디 : PLAY_QUIT", "", "&7서버에서 퇴장할 때 다른 플레이어가 퇴장 효과음을 듣게 합니다", "&7해당 플레이어가 퇴장 소리 듣기를 꺼둔 상태면 들리지 않습니다"),
                    trueLore, falseLore));
    inv.setItem(3, CreateItemStack
            .toggleItem(getBool(player, UserData.PLAY_QUIT_FORCE), "&b퇴장 소리 무조건 재생", Arrays.asList("&e내부 아이디 : PLAY_QUIT_FORCE", "", "&7서버에서 퇴장할 때 config와 플레이어들의 설정에 관계 없이", "&7반드시 퇴장 효과음을 듣게 합니다"),
                    trueLore, falseLore));
    inv.setItem(4, CreateItemStack.toggleItem(getBool(player, UserData.LISTEN_JOIN_FORCE), "&b입장 소리 무조건 들음",
            Arrays.asList("&e내부 아이디 : LISTEN_JOIN_FORCE", "", "&7다른 플레이어가 서버에 입장할 때", "&7config와 플레이어들의 설정에 관계 없이", "&7반드시 입장 효과음을 듣습니다", "", "&6현재 설정 : &a켜짐", "", "&c클릭하면 해당 기능을 끕니다"), "&b입장 소리 무조건 들음",
            Arrays.asList("&e내부 아이디 : LISTEN_JOIN_FORCE", "", "&7다른 플레이어가 서버에 입장할 때", "&7config와 플레이어들의 설정에 관계 없이", "&7반드시 입장 효과음을 듣습니다", "", "&6현재 설정 : &c꺼짐", "", "&a클릭하면 해당 기능을 켭니다")));
    inv.setItem(5, CreateItemStack.toggleItem(getBool(player, UserData.LISTEN_QUIT_FORCE), "&b퇴장 소리 무조건 들음",
            Arrays.asList("&e내부 아이디 : LISTEN_QUIT_FORCE", "", "&7다른 플레이어가 서버에서 퇴장할 때", "&7config와 플레이어들의 설정에 관계 없이", "&7반드시 퇴장 효과음을 듣습니다", "", "&6현재 설정 : &a켜짐", "", "&c클릭하면 해당 기능을 끕니다"), "&b퇴장 소리 무조건 들음",
            Arrays.asList("&e내부 아이디 : LISTEN_QUIT_FORCE", "", "&7다른 플레이어가 서버에서 퇴장할 때", "&7config와 플레이어들의 설정에 관계 없이", "&7반드시 퇴장 효과음을 듣습니다", "", "&6현재 설정 : &c꺼짐", "", "&a클릭하면 해당 기능을 켭니다")));
    inv.setItem(6, CreateItemStack.toggleItem(getBool(player, UserData.PLAY_CHAT), "&b채팅 소리 재생",
            Arrays.asList("&e내부 아이디 : PLAY_CHAT", "", "&7채팅할 때 다른 플레이어가 채팅 효과음을 듣게 합니다", "&7해당 플레이어가 채팅 소리 듣기를 꺼둔 상태면 들리지 않습니다", "", "&6현재 설정 : &a켜짐", "", "&c클릭하면 해당 기능을 끕니다"), "&b채팅 소리 재생",
            Arrays.asList("&e내부 아이디 : PLAY_CHAT", "", "&7채팅할 때 다른 플레이어가 채팅 효과음을 듣게 합니다", "&7해당 플레이어가 채팅 소리 듣기를 꺼둔 상태면 들리지 않습니다", "", "&6현재 설정 : &c꺼짐", "", "&a클릭하면 해당 기능을 켭니다")));
    inv.setItem(7, CreateItemStack.toggleItem(getBool(player, UserData.PLAY_CHAT_FORCE), "&b채팅 소리 무조건 재생",
            Arrays.asList("&e내부 아이디 : PLAY_CHAT_FORCE", "", "&7채팅할 때 config와 플레이어들의 설정에 관계 없이", "&7반드시 채팅 효과음을 듣게 합니다", "", "&6현재 설정 : &a켜짐", "", "&c클릭하면 해당 기능을 끕니다"), "&b채팅 소리 무조건 재생",
            Arrays.asList("&e내부 아이디 : PLAY_CHAT_FORCE", "", "&7채팅할 때 config와 플레이어들의 설정에 관계 없이", "&7반드시 채팅 효과음을 듣게 합니다", "", "&6현재 설정 : &c꺼짐", "", "&a클릭하면 해당 기능을 켭니다")));
    inv.setItem(8, CreateItemStack.toggleItem(getBool(player, UserData.LISTEN_CHAT_FORCE), "&b채팅 소리 무조건 들음",
            Arrays.asList("&e내부 아이디 : LISTEN_CHAT_FORCE", "", "&7다른 플레이어가 채팅할 때", "&7config와 플레이어들의 설정에 관계 없이", "&7반드시 채팅 효과음을 듣습니다", "", "&6현재 설정 : &a켜짐", "", "&c클릭하면 해당 기능을 끕니다"), "&b채팅 소리 무조건 들음",
            Arrays.asList("&e내부 아이디 : LISTEN_CHAT_FORCE", "", "&7다른 플레이어가 채팅할 때", "&7config와 플레이어들의 설정에 관계 없이", "&7반드시 채팅 효과음을 듣습니다", "", "&6현재 설정 : &c꺼짐", "", "&a클릭하면 해당 기능을 켭니다")));
    inv.setItem(9, CreateItemStack.toggleItem(getBool(player, UserData.LISTEN_GLOBAL_FORCE), "&b서버 라디오 무조건 들음",
            Arrays.asList("&e내부 아이디 : LISTEN_GLOBAL_FORCE", "", "&7서버 라디오 기능에 상관 없이", "&7반드시 서버 라디오 소리를 듣습니다", "", "&6현재 설정 : &a켜짐", "", "&c클릭하면 해당 기능을 끕니다"), "&b서버 라디오 무조건 들음",
            Arrays.asList("&e내부 아이디 : LISTEN_GLOBAL_FORCE", "", "&7서버 라디오 기능에 상관 없이", "&7반드시 서버 라디오 소리를 듣습니다", "", "&6현재 설정 : &c꺼짐", "", "&a클릭하면 해당 기능을 켭니다")));
    inv.setItem(10, CreateItemStack.toggleItem(getBool(player, UserData.SHOW_JOIN_MESSAGE), "&b입장 메시지 띄움",
            Arrays.asList("&e내부 아이디 : SHOW_JOIN_MESSAGE", "", "&7서버에 입장할 때 모든 플레이어에게 입장 메시지를 띄웁니다", "&7해당 플레이어가 입장 메시지 출력을 꺼둔 상태면 띄우지 않습니다", "", "&6현재 설정 : &a켜짐", "", "&c클릭하면 해당 기능을 끕니다"),
            "&b입장 메시지  띄움",
            Arrays.asList("&e내부 아이디 : SHOW_JOIN_MESSAGE", "", "&7서버에 입장할 때 config와 플레이어들의 설정에 관계 없이", "&7모든 플레이어에게 반드시 입장 메시지(tellraw 기능)를 띄웁니다", "", "&6현재 설정 : &c꺼짐", "", "&a클릭하면 해당 기능을 켭니다")));
    inv.setItem(11, CreateItemStack.toggleItem(getBool(player, UserData.SHOW_JOIN_MESSAGE_FORCE), "&b입장 메시지 무조건 띄움",
            Arrays.asList("&e내부 아이디 : SHOW_JOIN_MESSAGE_FORCE", "", "&7서버에 입장할 때 config와 플레이어들의 설정에 관계 없이", "&7모든 플레이어에게 반드시 입장 메시지(tellraw 기능)를 띄웁니다", "", "&6현재 설정 : &a켜짐", "", "&c클릭하면 해당 기능을 끕니다"),
            "&b입장 메시지 무조건 띄움",
            Arrays.asList("&e내부 아이디 : SHOW_JOIN_MESSAGE_FORCE", "", "&7서버에 입장할 때 config와 플레이어들의 설정에 관계 없이", "&7모든 플레이어에게 반드시 입장 메시지(tellraw 기능)를 띄웁니다", "", "&6현재 설정 : &c꺼짐", "", "&a클릭하면 해당 기능을 켭니다")));
    inv.setItem(12, CreateItemStack.toggleItem(getBool(player, UserData.SHOW_QUIT_MESSAGE), "&b퇴장 메시지 띄움",
            Arrays.asList("&e내부 아이디 : SHOW_QUIT_MESSAGE_FORCE", "", "&7서버에서 퇴장할 때 config와 플레이어들의 설정에 관계 없이", "&7모든 플레이어에게 반드시 퇴장 메시지(tellraw 기능)를 띄웁니다", "", "&6현재 설정 : &a켜짐", "", "&c클릭하면 해당 기능을 끕니다"),
            "&b퇴장 메시지 띄움",
            Arrays.asList("&e내부 아이디 : SHOW_QUIT_MESSAGE", "", "&7서버에서 퇴장할 때 config와 플레이어들의 설정에 관계 없이", "&7모든 플레이어에게 반드시 퇴장 메시지(tellraw 기능)를 띄웁니다", "", "&6현재 설정 : &c꺼짐", "", "&a클릭하면 해당 기능을 켭니다")));
    inv.setItem(13, CreateItemStack.toggleItem(getBool(player, UserData.SHOW_QUIT_MESSAGE_FORCE), "&b퇴장 메시지 무조건 띄움",
            Arrays.asList("&e내부 아이디 : SHOW_QUIT_MESSAGE_FORCE", "", "&7서버에서 퇴장할 때 config와 플레이어들의 설정에 관계 없이", "&7모든 플레이어에게 반드시 퇴장 메시지(tellraw 기능)를 띄웁니다", "", "&6현재 설정 : &a켜짐", "", "&c클릭하면 해당 기능을 끕니다"),
            "&b퇴장 메시지 무조건 띄움",
            Arrays.asList("&e내부 아이디 : SHOW_QUIT_MESSAGE_FORCE", "", "&7서버에서 퇴장할 때 config와 플레이어들의 설정에 관계 없이", "&7모든 플레이어에게 반드시 퇴장 메시지(tellraw 기능)를 띄웁니다", "", "&6현재 설정 : &c꺼짐", "", "&a클릭하면 해당 기능을 켭니다")));
    inv.setItem(14, CreateItemStack.toggleItem(getBool(player, UserData.OUTPUT_JOIN_MESSAGE_FORCE), "&b입장 메시지 무조건 출력",
            Arrays.asList("&e내부 아이디 : LISTEN_JOIN_FORCE", "", "&7다른 플레이어가 서버에 입장할 때", "&7config와 플레이어들의 설정에 관계 없이", "&7반드시 입장 효과음을 듣습니다", "", "&6현재 설정 : &a켜짐", "", "&c클릭하면 해당 기능을 끕니다"), "&b입장 소리 무조건 들음",
            Arrays.asList("&e내부 아이디 : LISTEN_JOIN_FORCE", "", "&7다른 플레이어가 서버에 입장할 때", "&7config와 플레이어들의 설정에 관계 없이", "&7반드시 입장 효과음을 듣습니다", "", "&6현재 설정 : &c꺼짐", "", "&a클릭하면 해당 기능을 켭니다")));

    inv.setItem(53, CreateItemStack.create(Material.BOOKSHELF, 1, "&b메인 메뉴로", true));
    player.openInventory(inv);
    InventoryView lastInventory = getLastInventory(player.getUniqueId());
    if (lastInventory != null)
    {
      inv.setItem(45, CreateItemStack.getPreviousButton(lastInventory.title()));
    }
  }

  private static void itemDropModeMenu(Player player)
  {
    Inventory inv = Bukkit.createInventory(null, 27, Constant.CANCEL_STRING + Constant.ITEM_DROP_MODE_MENU);
    String itemDropMode = switch (UserData.ITEM_DROP_MODE.getString(player.getUniqueId()))
            {
              case "normal" -> "기본";
              case "sneak" -> "웅크리기";
              case "disabled" -> "비활성화";
              default -> "알 수 없음";
            };
    inv.setItem(11,
            CreateItemStack.create(Material.IRON_SHOVEL, 1, "&6기본", Arrays.asList("&7아이템을 기존의 마인크래프트 방식대로 버립니다", "", "&6현재 설정 : &e" + itemDropMode, "", "&e클릭하면 아이템 버리기 모드를 &6기본&e으로 설정합니다"), true));
    inv.setItem(13, CreateItemStack.create(Material.SHIELD, 1, "&6시프트 드롭",
            Arrays.asList("&7아이템을 웅크린 상태에서만 버릴 수 있으며,", "&7인벤토리를 연 상태에서는 아이템을 버릴 수 없습니다", "", "&6현재 설정 : &e" + itemDropMode, "", "&e클릭하면 아이템 버리기 모드를 &6웅크리기&e로 설정합니다"), true));
    inv.setItem(15,
            CreateItemStack.create(Material.BARRIER, 1, "&6비활성화", Arrays.asList("&7아이템을 어떤 상황에서도 버릴 수 없습니다", "", "&6현재 설정 : &e" + itemDropMode, "", "&e클릭하면 아이템 버리기 모드를 &6비활성화&e로 설정합니다"), true));

    inv.setItem(26, CreateItemStack.create(Material.BOOKSHELF, 1, "&b메인 메뉴로", true));

    player.openInventory(inv);
    InventoryView lastInventory = getLastInventory(player.getUniqueId());
    if (lastInventory != null)
    {
      inv.setItem(18, CreateItemStack.getPreviousButton(lastInventory.title()));
    }
  }

  private static void itemPickupModeMenu(Player player)
  {
    Inventory inv = Bukkit.createInventory(null, 27, Constant.CANCEL_STRING + Constant.ITEM_PICKUP_MODE_MENU);
    String itemPickupMode = switch (UserData.ITEM_PICKUP_MODE.getString(player.getUniqueId()))
            {
              case "normal" -> "기본";
              case "sneak" -> "웅크리기";
              case "disabled" -> "비활성화";
              default -> "알 수 없음";
            };
    inv.setItem(11,
            CreateItemStack.create(Material.IRON_SHOVEL, 1, "&6기본", Arrays.asList("&7아이템을 기존의 마인크래프트 방식대로 줍습니다", "", "&6현재 설정 : &e" + itemPickupMode, "", "&e클릭하면 아이템 줍기 모드를 &6기본&e으로 설정합니다"), true));
    inv.setItem(13,
            CreateItemStack.create(Material.SHIELD, 1, "&6웅크리기", Arrays.asList("&7아이템을 웅크린 상태에서만 주울 수 있습니다", "", "&6현재 설정 : &e" + itemPickupMode, "", "&e클릭하면 아이템 줍기 모드를 &6웅크리기&e로 설정합니다"),
                    true));
    inv.setItem(15,
            CreateItemStack.create(Material.BARRIER, 1, "&6비활성화", Arrays.asList("&7아이템을 어떤 상황에서도 주울 수 없습니다", "", "&6현재 설정 : &e" + itemPickupMode, "", "&e클릭하면 아이템 줍기 모드를 &6비활성화&e로 설정합니다"), true));

    inv.setItem(26, CreateItemStack.create(Material.BOOKSHELF, 1, "&b메인 메뉴로", true));

    player.openInventory(inv);
    InventoryView lastInventory = getLastInventory(player.getUniqueId());
    if (lastInventory != null)
    {
      inv.setItem(18, CreateItemStack.getPreviousButton(lastInventory.title()));
    }
  }

  @NotNull
  public static Inventory create(int row, @NotNull Component title, @NotNull String key)
  {
    TranslatableComponent guiTitle = Constant.GUI_PREFIX.args(Arrays.asList(title, Component.text(key), Constant.GUI_PREFIX.args().get(2)));
    return Bukkit.createInventory(null, row * 9, guiTitle);
  }

  public static boolean isGUITitle(@NotNull Component title)
  {
    return title instanceof TranslatableComponent translatableComponent && translatableComponent.key().equals("%1$s") && translatableComponent.args().size() == 3 && translatableComponent.args().get(2).equals(Component.text(Constant.CANCEL_STRING));
  }

  @NotNull
  public static String getGUIKey(@NotNull Component title)
  {
    return ((TextComponent) ((TranslatableComponent) title).args().get(1)).content();
  }

  @Nullable
  public static InventoryView getLastInventory(@NotNull UUID uuid)
  {
    if (!Variable.lastInventory.containsKey(uuid))
    {
      return null;
    }
    List<InventoryView> views = Variable.lastInventory.get(uuid);
    if (views.size() >= 2)
    {
      return views.get(views.size() - 2);
    }
    return null;
  }

  public enum GUIType
  {
    MAIN_MENU,
    RPG,
    SERVER_SETTINGS,
    SERVER_SETTINGS_ADMIN,
    ITEM_DROP_MODE_MENU,
    ITEM_PICKUP_MODE_MENU,
    ITEM_STASH,
  }
}

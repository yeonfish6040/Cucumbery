package com.jho5245.cucumbery.util.storage.data;

import org.bukkit.command.CommandSender;

import static com.jho5245.cucumbery.util.storage.data.Permission.Constant.*;

public enum Permission
{
  GUI_SERVER_SETTINGS_ADMIN(GUI + "serversettingsadmin"),

  EVENT_BLOCK_BREAK(E + "block.break"),
  EVENT_BLOCK_PLACE(E + "block.place"),
  EVENT_ITEM_PICKUP(E + "item.pickup"),
  EVENT_ITEM_DROP(E + "item.drop"),
  EVENT_CHAT(E + "chat"),
  EVENT_OPENCONTAINER(E + "opencontainer"),
  EVENT_INVENTORYCLICK(E + "inventoryclick"),
  EVENT_ITEMHELD(E + "itemheld"),
  EVENT_COMMANDPREPROCESS(E + "commandpreprocess"),
  EVENT_SWAPHANDITEMS(E + "swaphanditems"),
  EVENT_INTERACT(E + "interact"),
  EVENT_INTERACT_BLOCK(E + "interact.block"),
  EVENT_INTERACT_ENTITY(E + "interact.entity"),
  EVENT_INTERACT_AT_ENTITY(E + "interact.atentity"),
  EVENT_HURT_ENTITY(E + "hurtentity"),
  EVENT_CHAT_SPAM(E2 + "chat.spam"),
  EVENT_ERROR_HIDE("error.hide"),
  EVENT_ITEM_CONSUME(E + "item.consume"),
  EVENT_ITEM_CRAFT(E + "item.craft"),
  EVENT_ITEM_ENCHANT(E + "item.enchant"),

  TABCOMPLETE("tabcomplete"),

  EVENT2_ANTI_SPECTATE(E2 + "antispectate"),
  EVENT2_ANTI_SPECTATE_BYPASS(E2 + "antispectate" + BYPASS),
  EVENT2_ANTI_RIDE(E2 + "antiride"),
  EVENT2_ANTI_RIDE_BYPASS(E2 + "antiride" + BYPASS),
  EVENT2_CHAT_COLOR(E2 + "chat.color"),
  EVENT2_ANTI_ALLPLAYER(E2 + "antiallplayer"),
  EVENT2_COMMAND_SEND_COLON(E2 + "command.send.colon"),

  CMD_MAINCOMMAND(C + "maincommand"),
  CMD_TRASHCAN(C + "trashcan"),
  CMD_TRASHCAN_OTHERS(C + "trashcan" + O),
  CMD_NICK(C + "nick"),
  CMD_NICK_OTHERS(C + "nick" + O),
  CMD_GUICOMMANDS(C + "guicommands"),
  CMD_GUICOMMANDS_OTHERS(C + "guicommands" + O),
  CMD_SETDATA(C + "setdata"),
  CMD_BROADCAST(C + "broadcast"),
  CMD_SENDMESSAGE(C + "message"),
  CMD_ADVANCED_TELEPORT(C + "avancedtp"),
  CMD_ITEMSTORAGE(C + "itemstorage"),
  CMD_ITEMSTORAGE_MODIFY(C + "itemstorage" + ".modify"),
  CMD_ITEMDATA(C + "itemdata"),
  CMD_ITEMDATA_OTHERS(C + "itemdata" + O),
  CMD_CLEARCHAT(C + "clearchat"),
  CMD_WORKBENCH(C + "workbench"),
  CMD_WORKBENCH_OTHERS(C + "workbench" + O),
  CMD_ENDERCHEST(C + "enderchest"),
  CMD_ENDERCHEST_OTHERS(C + "enderchest" + O),
  CMD_HEAL(C + "heal"),
  CMD_HEAL_OTHERS(C + "heal" + O),
  CMD_FEED(C + "feed"),
  CMD_FEED_OTHERS(C + "feed" + O),
  CMD_AFEED(C + "afeed"),
  CMD_MHP(C + "mhp"),
  CMD_SHP(C + "shp"),
  CMD_HP(C + "hp"),
  CMD_WHOIS(C + "whois"),
  CMD_PLAYSOUND(C + "playsound"),
  CMD_PLAYSOUNDALL(C + "playsoundall"),
  CMD_PLAYSOUNDALL2(C + "playsoundall2"),
  CMD_HANDGIVE(C + "handgive"),
  CMD_BROADCASTITEM(C + "broadcastitem"),
  CMD_BROADCASTITEM_BYPASS(C + "broadcastitem" + BYPASS),
  CMD_REINFORCE(C + "reinforce"),
  CMD_VELOCITY(C + "velocity"),
  CMD_WARP(C + "warp"),
  CMD_WARPS(C + "warps"),
  CMD_SETWARP(C + "setwarp"),
  CMD_DELWARP(C + "delwarp"),
  CMD_WARP_OTHERS(C + "warp" + O),
  CMD_SUDO(C + "sudo"),
  CMD_GIVE(C + "give"),
  CMD_USERDATA(C + "userdata"),
  CMD_WHATIS(C + "whatis"),
  CMD_SPECTATE(C + "spectate"),
  CMD_SPECTATE_OTHERS(C + "spectate" + O),
  CMD_CALL(C + "call"),
  CMD_AIRPOINT(C + "airpoint"),
  CMD_FORCECHAT(C + "forcechat"),
  CMD_CHECKPERMISSION(C + "checkpermission"),
  CMD_SETHELDITEMSLOT(C + "sethelditemslot"),
  CMD_SWAPHELDITEM(C + "swaphelditem"),
  CMD_GETPOSITIONS(C + "getpositions"),
  CMD_ALLPLAYER(C + "allplayer"),
  CMD_COMMANDPACK(C + "commandpack"),
  CMD_SWAP_TELEPORT(C + "swapteleport"),
  CMD_CALC_DISTANCE(C + "calcdistance"),
  CMD_CHECK_AMOUNT(C + "checkamount"),
  CMD_CONDENSE(C + "condense"),
  CMD_CONDENSE_OTHERS(C + "condense" + O),
  CMD_UPDATE_INVENTORY(C + "updateinventory"),
  CMD_YUNNORI(C + "yunnori"),
  CMD_HAT(C + "hat"),
  CMD_OPENINVENTORY(C + "openinventory"),
  CMD_UPDATE_COMMANDS(C + "updatecommands"),
  CMD_SONG(C + "song"),
  CMD_HOWIS(C + "howis"),
  CMD_CUSTOMRECIPE(C + "customrecipe"),
  CMD_CUSTOMRECIPE_ADMIN(C + "customrecipe" + ".admin"),
  CMD_VIRTUAL_CHEST(C + "virtualchest"),
  CMD_VIRTUAL_CHEST_UNLIMITED(C + "virtualchest" + ".unlimited"),
  CMD_VIRTUAL_CHEST_ADMIN(C + "virtualchest" + ".admin"),
  CMD_CUSTOM_FIX(C + "customfix"),
  CMD_ECONOMY(C + "economy"),
  CMD_TRUEKILL(C + "truekill"),
  CMD_RESPAWN(C + "respawn"),
  CMD_REMOVE_BED_SPAWN_LOCATION(C + "removebedspawnlocation"),
  CMD_CHECK_CONFIG(C + "checkconfig"),
  CMD_TELEPORT(C + "teleport"),
  CMD_EDIT_COMMAND_BLOCK(C + "editcommandblock"),
  CMD_EDIT_BLOCK_DATA(C + "editblockdata"),
  CMD_CONSOLE_SUDO(C + "consolesudo"),
  CMD_QUICKSHOP_ADDON(C + "quickshopaddon"),
  CMD_REPEAT(C + "repeat"),
  CMD_VIEW_INVNETORY(C + "viewinventory"),
  CMD_CUSTOM_MERCHANT_ADMIN(C + "custommerchant.admin"),
  CMD_CUSTOM_EFFECT(C + "customeffect"),
  CMD_MODIFY_EXPLOSIVE(C + "modifyexplosive"),
  CMD_SEND_TOAST(C + "sendtoast"),
  CMD_SEND_BOSSBAR(C + "sendbossbar"),
  CMD_DELAY(C + "delay"),
  CMD_SET_NO_DAMAGE_TICKS(C + "setnodamageticks"),
  CMD_SET_AGGRO(C + "setaggro"),
  CMD_STASH(C + "stash"),
  CMD_BLOCK_PLACE_DATA(C + "blockplacedata"),

  CMD_SPLASH(C + "splash"),

  CMD_SWING_ARM(C + "swingarm"),

  CMD_SHAKE_VILLAGER_HEAD(C + "shakevillagerhead"),

  CMD_SET_ROTATION(C + "setrotation"),

  CMD_LOOK_AT(C + "lookat"),

  CMD_ENCHANT(C + "enchant"),

  OTHER_EVAL("eval"),
  OTHER_PLACEHOLDER("placeholder"),
  ;

  private final String permission;

  Permission(String permission)
  {
    this.permission = "cucumbery." + permission;
  }

  @Override
  public String toString()
  {
    return permission;
  }

  public boolean has(CommandSender sender)
  {
    return sender.hasPermission(permission);
  }

  static class Constant
  {
    public static final String C = "command.";
    public static final String E = "event.";
    public static final String O = ".others";
    public static final String E2 = "event2.";
    public static final String BYPASS = ".bypass";
    public static final String GUI= "gui.";
  }
}




























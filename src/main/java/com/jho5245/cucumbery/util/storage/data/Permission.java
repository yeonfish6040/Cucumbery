package com.jho5245.cucumbery.util.storage.data;

import org.bukkit.command.CommandSender;

public enum Permission
{
  P("cucumbery."),
  E("event."),
  C("command."),
  EX("exception."),
  O(".others"),
  E2("event2."),
  BYPASS(".bypass"),
  GUI("gui."),

  GUI_SERVER_SETTINGS_ADMIN(P + "" + GUI + "serversettingsadmin"),

  EVENT_BLOCK_BREAK(P + "" + E + "block.break"),
  EVENT_BLOCK_PLACE(P + "" + E + "block.place"),
  EVENT_ITEM_PICKUP(P + "" + E + "item.pickup"),
  EVENT_ITEM_DROP(P + "" + E + "item.drop"),
  EVENT_CHAT(P + "" + E + "chat"),
  EVENT_OPENCONTAINER(P + "" + E + "opencontainer"),
  EVENT_INVENTORYCLICK(P + "" + E + "inventoryclick"),
  EVENT_ITEMHELD(P + "" + E + "itemheld"),
  EVENT_COMMANDPREPROCESS(P + "" + E + "commandpreprocess"),
  EVENT_SWAPHANDITEMS(P + "" + E + "swaphanditems"),
  EVENT_INTERACT(P + "" + E + "interact"),
  EVENT_INTERACT_BLOCK(EVENT_INTERACT + ".block"),
  EVENT_INTERACT_ENTITY(EVENT_INTERACT + ".entity"),
  EVENT_INTERACT_AT_ENTITY(EVENT_INTERACT + ".atentity"),
  EVENT_HURT_ENTITY(P + "" + E + "hurtentity"),
  EVENT_CHAT_SPAM(P + "" + E2 + "chat.spam"),
  EVENT_ERROR_HIDE(P + "error.hide"),
  EVENT_ITEM_CONSUME(P + "" + E + "item.consume"),
  EVENT_ITEM_CRAFT(P + "" + E + "item.craft"),
  EVENT_ITEM_ENCHANT(P + "" + E + "item.enchant"),

  TABCOMPLETE(P + "tabcomplete"),

  EVENT2_ANTI_SPECTATE(P + "" + E2 + "antispectate"),
  EVENT2_ANTI_SPECTATE_BYPASS(EVENT2_ANTI_SPECTATE + "" + BYPASS),
  EVENT2_ANTI_RIDE(P + "" + E2 + "antiride"),
  EVENT2_ANTI_RIDE_BYPASS(EVENT2_ANTI_RIDE + "" + BYPASS),
  EVENT2_CHAT_COLOR(P + "" + E2 + "chat.color"),
  EVENT2_ANTI_ALLPLAYER(P + "" + E2 + "antiallplayer"),
  EVENT2_COMMAND_SEND_COLON(P + "" + E2 + "command.send.colon"),

  CMD_MAINCOMMAND(P + "" + C + "maincommand"),
  CMD_TRASHCAN(P + "" + C + "trashcan"),
  CMD_TRASHCAN_OTHERS(CMD_TRASHCAN + "" + O),
  CMD_NICK(P + "" + C + "nick"),
  CMD_NICK_OTHERS(CMD_NICK + "" + O),
  CMD_GUICOMMANDS(P + "" + C + "guicommands"),
  CMD_GUICOMMANDS_OTHERS(CMD_GUICOMMANDS + "" + O),
  CMD_SETDATA(P + "" + C + "setdata"),
  CMD_BROADCAST(P + "" + C + "broadcast"),
  CMD_SENDMESSAGE(P + "" + C + "message"),
  CMD_ADVANCED_TELEPORT(P + "" + C + "avancedtp"),
  CMD_ITEMSTORAGE(P + "" + C + "itemstorage"),
  CMD_ITEMSTORAGE_MODIFY(CMD_ITEMSTORAGE + ".modify"),
  CMD_ITEMDATA(P + "" + C + "itemdata"),
  CMD_ITEMDATA_OTHERS(CMD_ITEMDATA + "" + O),
  CMD_CLEARCHAT(P + "" + C + "clearchat"),
  CMD_WORKBENCH(P + "" + C + "workbench"),
  CMD_WORKBENCH_OTHERS(CMD_WORKBENCH + "" + O),
  CMD_ENDERCHEST(P + "" + C + "enderchest"),
  CMD_ENDERCHEST_OTHERS(CMD_ENDERCHEST + "" + O),
  CMD_HEAL(P + "" + C + "heal"),
  CMD_HEAL_OTHERS(CMD_HEAL + "" + O),
  CMD_FEED(P + "" + C + "feed"),
  CMD_FEED_OTHERS(CMD_FEED + "" + O),
  CMD_AFEED(P + "" + C + "afeed"),
  CMD_MHP(P + "" + C + "mhp"),
  CMD_SHP(P + "" + C + "shp"),
  CMD_HP(P + "" + C + "hp"),
  CMD_WHOIS(P + "" + C + "whois"),
  CMD_PLAYSOUND(P + "" + C + "playsound"),
  CMD_PLAYSOUNDALL(P + "" + C + "playsoundall"),
  CMD_PLAYSOUNDALL2(P + "" + C + "playsoundall2"),
  CMD_HANDGIVE(P + "" + C + "handgive"),
  CMD_BROADCASTITEM(P + "" + C + "broadcastitem"),
  CMD_BROADCASTITEM_BYPASS(CMD_BROADCASTITEM + ".bypass"),
  CMD_REINFORCE(P + "" + C + "reinforce"),
  CMD_VELOCITY(P + "" + C + "velocity"),
  CMD_WARP(P + "" + C + "warp"),
  CMD_WARPS(P + "" + C + "warps"),
  CMD_SETWARP(P + "" + C + "setwarp"),
  CMD_DELWARP(P + "" + C + "delwarp"),
  CMD_WARP_OTHERS(CMD_WARP + "" + O),
  CMD_SUDO(P + "" + C + "sudo"),
  CMD_GIVE(P + "" + C + "give"),
  CMD_SETUSERDATA(P + "" + C + "setuserdata"),
  CMD_WHATIS(P + "" + C + "whatis"),
  CMD_SPECTATE(P + "" + C + "spectate"),
  CMD_SPECTATE_OTHERS(CMD_SPECTATE + "" + O),
  CMD_CALL(P + "" + C + "call"),
  CMD_AIRPOINT(P + "" + C + "airpoint"),
  CMD_GETUSERDATA(P + "" + C + "getuserdata"),
  CMD_FORCECHAT(P + "" + C + "forcechat"),
  CMD_CHECKPERMISSION(P + "" + C + "checkpermission"),
  CMD_SETHELDITEMSLOT(P + "" + C + "sethelditemslot"),
  CMD_SWAPHELDITEM(P + "" + C + "swaphelditem"),
  CMD_GETPOSITIONS(P + "" + C + "getpositions"),
  CMD_ALLPLAYER(P + "" + C + "allplayer"),
  CMD_COMMANDPACK(P + "" + C + "commandpack"),
  CMD_SWAP_TELEPORT(P + "" + C + "swapteleport"),
  CMD_CALC_DISTANCE(P + "" + C + "calcdistance"),
  CMD_CHECK_AMOUNT(P + "" + C + "checkamount"),
  CMD_CONDENSE(P + "" + C + "condense"),
  CMD_CONDENSE_OTHERS(CMD_CONDENSE + "" + O),
  CMD_UPDATE_INVENTORY(P + "" + C + "updateinventory"),
  CMD_YUNNORI(P + "" + C + "yunnori"),
  CMD_HAT(P + "" + C + "hat"),
  CMD_OPENINVENTORY(P + "" + C + "openinventory"),
  CMD_UPDATE_COMMANDS(P + "" + C + "updatecommands"),
  CMD_SONG(P + "" + C + "song"),
  CMD_HOWIS(P + "" + C + "howis"),
  CMD_CUSTOMRECIPE(P + "" + C + "customrecipe"),
  CMD_CUSTOMRECIPE_ADMIN(CMD_CUSTOMRECIPE + ".admin"),
  CMD_VIRTUAL_CHEST(P + "" + C + "virtualchest"),
  CMD_VIRTUAL_CHEST_UNLIMITED(CMD_VIRTUAL_CHEST + ".unlimited"),
  CMD_VIRTUAL_CHEST_ADMIN(CMD_VIRTUAL_CHEST + ".admin"),
  CMD_CUSTOM_FIX(P + "" + C + "customfix"),
  CMD_ECONOMY(P + "" + C + "economy"),
  CMD_TRUEKILL(P + "" + C + "truekill"),
  CMD_RESPAWN(P + "" + C + "respawn"),
  CMD_REMOVE_BED_SPAWN_LOCATION(P + "" + C + "removebedspawnlocation"),
  CMD_CHECK_CONFIG(P + "" + C + "checkconfig"),
  CMD_TELEPORT(P + "" + C + "teleport"),
  CMD_EDIT_COMMAND_BLOCK(P + "" + C + "editcommandblock"),
  CMD_EDIT_BLOCK_DATA(P + "" + C + "editblockdata"),
  CMD_CONSOLE_SUDO(P + "" + C + "consolesudo"),
  CMD_QUICKSHOP_ADDON(P + "" + C + "quickshopaddon"),
  CMD_REPEAT(P + "" + C + "repeat"),
  CMD_VIEW_INVNETORY(P + "" + C + "viewinventory"),
  CMD_CUSTOM_MERCHANT_ADMIN(P + "" + C + "custommerchant.admin"),
  CMD_CUSTOM_EFFECT(P + "" + C + "customeffect"),

  OTHER_EVAL(P + "" + "eval"),
  OTHER_PLACEHOLDER(P + "" + "placeholder"),
  ;

  private final String permission;

  Permission(String type)
  {
    this.permission = type;
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
}

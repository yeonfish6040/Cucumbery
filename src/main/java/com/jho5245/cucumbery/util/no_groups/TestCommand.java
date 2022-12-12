package com.jho5245.cucumbery.util.no_groups;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import de.tr7zw.changeme.nbtapi.NBTEntity;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("all")
public class TestCommand implements CommandExecutor, AsyncTabCompleter
{
  private static final NamespacedKey test = new NamespacedKey("cucumbery", "test_recipe");

  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    args = MessageUtil.wrapWithQuote(args);
    if (!Method.hasPermission(sender, "asdf", true))
    {
      return true;
    }
    try
    {
      if (sender instanceof Player player)
      {
        String key = args[0];
        NBTEntity nbtEntity = new NBTEntity(player);
        player.sendMessage(nbtEntity.getCompound(key).toString());
//        player.sendMessage("prev. walk speed : " + player.getWalkSpeed());
//        player.setWalkSpeed(Float.valueOf(args[0]));
//        player.sendMessage("prev. fly speed : " + player.getFlySpeed());
//        player.setFlySpeed(Float.valueOf(args[1]));
//        try
//        {
//          Location location = CommandArgumentUtil.location(sender, args[0], true, true);
//          if (location != null)
//          {
//            MessageUtil.info(player, MiningManager.getMiningInfo(player, location));
//          }
//        }
//        catch (Exception ignored) {}
//        switch (args[0])
//        {
//          case "add" ->
//                  {
//                    ItemStack result = new ItemStack(Material.TNT);
//                    ShapedRecipe shapedRecipe = new ShapedRecipe(test, result);
//                    shapedRecipe = shapedRecipe.shape("aaa", "aba", "aaa").setIngredient('a', new ItemStack(Material.DIAMOND, 1, (short) 1));
//                    MessageUtil.sendMessage(player, Bukkit.addRecipe(shapedRecipe) ? "레시피 등록 완료" : "등록 실패");
//                  }
//                  case "remove" ->
//                          {
//                            MessageUtil.sendMessage(player, Bukkit.removeRecipe(test) ? "레시피 등록 해제 완료" : "등록 해제 실패");
//                          }
//        }
//        List<Entity> entities = player.getNearbyEntities(10, 10, 10);
//        for (Entity e : entities)
//        {
//          if (e instanceof Frog frog)
//          {
//            Entity target = SelectorUtil.getEntity(sender, args[0]);
//            frog.setTongueTarget(target);
//            MessageUtil.broadcastPlayer("개구리 : %s, 대상 : %s", frog, target);
//          }
//        }
        // title part test
/*        {
          String input = MessageUtil.listToString(args);

          String[] split = MessageUtil.splitEscape(input, ';');

          if (!split.equals(""))
            player.sendTitlePart(TitlePart.TITLE, ComponentUtil.create(split[0]));
          if (split.length >= 2 && !split[1].equals(""))
          {
            player.sendTitlePart(TitlePart.SUBTITLE, ComponentUtil.create(split[1]));
          }
          if (split.length == 5)
          {
            player.sendTitlePart(TitlePart.TIMES, Times.times
                    (
                            Duration.ofMillis((long) (1000 * Double.parseDouble(split[2]))),
                            Duration.ofMillis((long) (1000 * Double.parseDouble(split[3]))),
                            Duration.ofMillis((long) (1000 * Double.parseDouble(split[4])))
                    ));
          }
        }*/
        /*
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        switch (args[0])
        {
          case "add" -> {
            ItemStack result = new ItemStack(Material.TNT);
            ItemMeta itemMeta = result.getItemMeta();
            itemMeta.displayName(ComponentUtil.translate("&iitem.minecraft.diamond"));
            result.setItemMeta(itemMeta);
            ItemStack ingredient = new ItemStack(Material.DIAMOND);
            ShapedRecipe shapedRecipe = new ShapedRecipe(test, result);
            shapedRecipe = shapedRecipe.shape("aaa", "a#a", "aaa").setIngredient('a', ingredient);
              MessageUtil.sendMessage(player, Bukkit.addRecipe(shapedRecipe) ? "recipe added!" : "&ccould not add that recipe!");
          }
          case "remove" -> {
            MessageUtil.sendMessage(player, Bukkit.removeRecipe(test) ? "recipe removed!" : "could not reipce that recipe!");
          }
        }*/
      }
      if (args.length >= 2)
      {
        switch (args[0])
        {
          case "entities" -> {
            List<Entity> entities = SelectorUtil.getEntities(sender, args[1], true);
            MessageUtil.sendMessage(sender, entities != null ? entities : "null");
          }
          case "entity" -> {
            Entity entity = SelectorUtil.getEntity(sender, args[1], true);
            MessageUtil.sendMessage(sender, entity != null ? entity : "null");
          }
          case "players" -> {
            List<Player> players = SelectorUtil.getPlayers(sender, args[1], true);
            MessageUtil.sendMessage(sender, players != null ? players : "null");
          }
          case "player" -> {
            Player p = SelectorUtil.getPlayer(sender, args[1], true);
            MessageUtil.sendMessage(sender, p != null ? p : "null");
          }
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return true;
  }

  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(true, args), true))
    {
      return Collections.singletonList(args[0]);
    }
    int length = args.length;
    if (length == 1)
    {
      return Method.tabCompleterList(args, "<뭐 왜 왓>", "entities", "entity", "players", "player");
    }
    else if (length == 2)
    {
      switch (args[0])
      {
        case "entities" -> {
          return Method.tabCompleterEntity(sender, args, "<개체>", true);
        }
        case "entity" -> {
          return Method.tabCompleterEntity(sender, args, "<개체>");
        }
        case "players" -> {
          return Method.tabCompleterPlayer(sender, args, "<플레이어>", true);
        }
        case "player" -> {
          return Method.tabCompleterPlayer(sender, args, "<플레이어>");
        }
      }
      return Collections.EMPTY_LIST;
    }
    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }

  @NotNull
  public List<Completion> completion(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args, @NotNull Location location)
  {
    int length = args.length;
    if (length == 1)
    {
      List<Completion> list1 = CommandTabUtil.tabCompleterEntity(sender, args, "<개체>"), list2 = CommandTabUtil.worldArgument(sender, args, "<월드>"),
              list3 = CommandTabUtil.itemStackArgument(sender, args, "<아이템>"), list4 = CommandTabUtil.locationArgument(sender, args, "<위치>", null, false),
      list5 = CommandTabUtil.rotationArgument(sender, args, "<방향>", null), list6 = CommandTabUtil.tabCompleterList(args, "<아아무거아무거나>", false,
              "햄버거와 뚱이", "푸아그라탕 샌즈", "와니 필통에 있는 볼펜심 안에 있는 초록색 잉크", "지은지 70년 이상 지나서 철거한 건물 콘크리트 가루",
              "밀랓비탈헝 갹간 국방적으로 깍ㄲ인 구리 세로 반 브록", " <뭐> ", " <나닛>", " ㅇㅅㅇ", " 오", " 오이", "오이 베드서버에 있는 줄 알았는데", "키보드가 있는 것으로 알려졌다 보니 정말 죄송합니다 오후 서울", " 난", " 알아요",
              "자기 서버에 있는것 같구만", " ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", "밀랍칠한 약간 국방적으로 깎인 구리 세로 반 블록 — 오늘 오후 1:23\n" +
                      "베이스 스트링 아 참고로 난 영화 보고 있다 ㅋㅋㄹㅃㅃ\n" +
                      "난 영화 속에 나오는 우주 정거장은 항상 웃기다고 생각했었어", "gusen1116 — 오늘 오후 1:24\n" +
                      "봐라, 않는 그리하였는가? 그들은 오아이스도 인간은 그들에게 이것은 그와 아니다. 그들은 같이 피어나기 온갖 대중을 풀밭에 생명을 우리의 사막이다.\n" +
                      "아니 근데\n" +
                      "군포가 뭐하는 곳이기래\n" +
                      "내 택배가 4일째 거기에 있냐\n" +
                      "밀랍칠한 약간 국방적으로 깎인 구리 세로 반 블록 — 오늘 오후 1:26\n" +
                      "군머포장\n" +
                      "특징:늦음\n" +
                      "gusen1116 — 오늘 오후 1:27\n" +
                      "아", "", "");
      List<Completion> list = CommandTabUtil.tabCompleterList(args, CustomEffectType.getEffectTypeMap(), "<effect>");
      return CommandTabUtil.sortError(list1, list2, list3, list4, list5, list);
    }
    if (length == 2)
    {
      if (CommandArgumentUtil.world(sender, args[0], false) != null)
      return CommandTabUtil.locationArgument(sender, args, "<위치>", location, false);
    }
    if (length == 3)
    {
      if (CommandArgumentUtil.location(sender, args[1], false, false) != null)
      return CommandTabUtil.rotationArgument(sender, args, "<방향>", location);
    }
    return CommandTabUtil.ARGS_LONG;
  }
}


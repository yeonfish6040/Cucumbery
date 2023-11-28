package com.jho5245.cucumbery.util.no_groups;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import de.tr7zw.changeme.nbtapi.NBTItem;
import io.papermc.paper.advancement.AdvancementDisplay.Frame;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

public record ToastMessage(NamespacedKey id, Component title, ItemStack itemStack,
                           Frame frame)
{
  public ToastMessage(@NotNull NamespacedKey id, @NotNull Component title, @NotNull ItemStack itemStack, @NotNull Frame frame)
  {
    this.id = id;
    this.title = ComponentUtil.stripEvent(title);
    this.itemStack = itemStack;
    this.frame = frame;
  }

  public void showTo(Player player)
  {
    showTo(Collections.singletonList(player));
  }

  public void showTo(Collection<? extends Player> players)
  {
    add();
    grant(players);
    Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
    {
      revoke(players);
      remove();
    }, 20);
  }

  @SuppressWarnings("deprecation")
  private void add()
  {
    try
    {
      Bukkit.getUnsafe().loadAdvancement(id, getJson());
    }
    catch (Exception e)
    {
Cucumbery.getPlugin().getLogger().warning(      e.getMessage());
    }
  }

  @SuppressWarnings("deprecation")
  private void remove()
  {
    Bukkit.getUnsafe().removeAdvancement(id);
  }

  private void grant(Collection<? extends Player> players)
  {
    Advancement advancement = Bukkit.getAdvancement(id);
    if (advancement == null)
    {
      return;
    }
    AdvancementProgress progress;
    for (Player player : players)
    {
      progress = player.getAdvancementProgress(advancement);
      if (!progress.isDone())
      {
        for (String criteria : progress.getRemainingCriteria())
        {
          progress.awardCriteria(criteria);
        }
      }
    }
  }

  private void revoke(Collection<? extends Player> players)
  {
    Advancement advancement = Bukkit.getAdvancement(id);
    if (advancement == null)
    {
      return;
    }
    AdvancementProgress progress;
    for (Player player : players)
    {
      progress = player.getAdvancementProgress(advancement);
      if (progress.isDone())
      {
        for (String criteria : progress.getAwardedCriteria())
        {
          progress.revokeCriteria(criteria);
        }
      }
    }
  }

  @NotNull
  private String getJson()
  {
    JsonObject json = new JsonObject();

    JsonObject icon = new JsonObject();
    icon.addProperty("item", this.itemStack.getType().toString().toLowerCase());
    String nbt = new NBTItem(itemStack).toString();
    if (!nbt.equals("{}"))
    {
      icon.addProperty("nbt", nbt);
    }
    JsonObject display = new JsonObject();
    display.add("icon", icon);
    display.add("title", GsonComponentSerializer.gson().serializeToTree(this.title));
    display.add("description", GsonComponentSerializer.gson().serializeToTree(Component.empty()));
    display.addProperty("background", "minecraft:textures/gui/advancements/backgrounds/adventure.png");
    display.addProperty("frame", this.frame.toString().toLowerCase());
    display.addProperty("announce_to_chat", false);
    display.addProperty("show_toast", true);
    display.addProperty("hidden", true);

    JsonObject criteria = new JsonObject();
    JsonObject trigger = new JsonObject();

    trigger.addProperty("trigger", "minecraft:impossible");
    criteria.add("impossible", trigger);

    json.add("criteria", criteria);
    json.add("display", display);

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    return gson.toJson(json);
  }
}

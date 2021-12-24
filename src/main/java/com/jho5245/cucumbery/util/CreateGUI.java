package com.jho5245.cucumbery.util;

import com.jho5245.cucumbery.util.storage.data.Constant;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class CreateGUI
{
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
}

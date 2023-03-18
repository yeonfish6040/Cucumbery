package com.jho5245.cucumbery.util.storage.no_groups;

import com.jho5245.cucumbery.custom.customeffect.custom_mining.MiningManager;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class ItemStackBuilder
{
  private final ItemStack itemStack;

  private final NBTItem nbtItem;

  public ItemStackBuilder(@NotNull ItemStack itemStack)
  {
    this.itemStack = itemStack.clone();
    this.nbtItem = new NBTItem(itemStack, true);
  }

  @NotNull
  public ItemStackBuilder blockTier(int blockTier)
  {
    nbtItem.setInteger(MiningManager.BLOCK_TIER, blockTier);
    nbtItem.setBoolean(MiningManager.REMOVE_KEYS, true);
    return this;
  }

  @NotNull
  public ItemStackBuilder blockHardness(float blockHardness)
  {
    nbtItem.setFloat(MiningManager.BLOCK_HARDNESS, blockHardness);
    nbtItem.setBoolean(MiningManager.REMOVE_KEYS, true);
    return this;
  }

  @NotNull
  public ItemStackBuilder blockExp(float blockExp)
  {
    nbtItem.setFloat(MiningManager.BLOCK_EXP, blockExp);
    nbtItem.setBoolean(MiningManager.REMOVE_KEYS, true);
    return this;
  }

  @NotNull
  public ItemStackBuilder blockStandard(int blockTier, float blockHardness, float blockExp)
  {
    nbtItem.setInteger(MiningManager.BLOCK_TIER, blockTier);
    nbtItem.setFloat(MiningManager.BLOCK_HARDNESS, blockHardness);
    nbtItem.setFloat(MiningManager.BLOCK_EXP, blockExp);
    nbtItem.setBoolean(MiningManager.REMOVE_KEYS, true);
    return this;
  }

  @NotNull
  public ItemStackBuilder breakSound(@NotNull Sound sound)
  {
    nbtItem.setString(MiningManager.BREAK_SOUND, sound.toString());
    nbtItem.setBoolean(MiningManager.REMOVE_KEYS, true);
    return this;
  }

  @NotNull
  public ItemStackBuilder breakSound(@NotNull Sound sound, float pitch)
  {
    nbtItem.setString(MiningManager.BREAK_SOUND, sound.toString());
    nbtItem.setFloat(MiningManager.BREAK_SOUND_PITCH, pitch);
    nbtItem.setBoolean(MiningManager.REMOVE_KEYS, true);
    return this;
  }

  @NotNull
  public ItemStackBuilder breakSound(@NotNull Sound sound, float volume, float pitch)
  {
    nbtItem.setString(MiningManager.BREAK_SOUND, sound.toString());
    nbtItem.setFloat(MiningManager.BREAK_SOUND_VOLUME, volume);
    nbtItem.setFloat(MiningManager.BREAK_SOUND_PITCH, pitch);
    nbtItem.setBoolean(MiningManager.REMOVE_KEYS, true);
    return this;
  }

  @NotNull
  public ItemStackBuilder breakSound(@NotNull String sound)
  {
    nbtItem.setString(MiningManager.BREAK_SOUND, sound);
    nbtItem.setBoolean(MiningManager.REMOVE_KEYS, true);
    return this;
  }

  @NotNull
  public ItemStackBuilder breakSound(@NotNull String sound, float pitch)
  {
    nbtItem.setString(MiningManager.BREAK_SOUND, sound);
    nbtItem.setFloat(MiningManager.BREAK_SOUND_PITCH, pitch);
    nbtItem.setBoolean(MiningManager.REMOVE_KEYS, true);
    return this;
  }

  @NotNull
  public ItemStackBuilder breakSound(@NotNull String sound, float volume, float pitch)
  {
    nbtItem.setString(MiningManager.BREAK_SOUND, sound);
    nbtItem.setFloat(MiningManager.BREAK_SOUND_VOLUME, volume);
    nbtItem.setFloat(MiningManager.BREAK_SOUND_PITCH, pitch);
    nbtItem.setBoolean(MiningManager.REMOVE_KEYS, true);
    return this;
  }

  /**
   * @deprecated internal API
   * @return this
   */
  @Deprecated
  @NotNull
  public ItemStackBuilder method()
  {
    nbtItem.setBoolean(MiningManager.REMOVE_KEYS, true);
    return this;
  }


  @NotNull
  public ItemStack build()
  {
    return itemStack;
  }
}

package com.jho5245.cucumbery.custom.customeffect.custom_mining;

import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record MiningResult(boolean canMine, float toolSpeed, float miningSpeed, float miningSpeedBeforeHaste, float blockHardness, float miningFortune, float exp, int miningTier, int blockTier, int regenCooldown, List<ItemStack> drops, @Nullable
                           Sound breakSound, @Nullable String breakCustomSound, float breakSoundVolume, float breakSoundPitch)
{
  public MiningResult(boolean canMine, float toolSpeed, float miningSpeed, float miningSpeedBeforeHaste, float blockHardness, float miningFortune, float exp, int miningTier, int blockTier, int regenCooldown, @NotNull List<ItemStack> drops, @Nullable
  Sound breakSound, @Nullable String breakCustomSound, float breakSoundVolume, float breakSoundPitch)
  {
    this.canMine = canMine;
    this.toolSpeed = toolSpeed;
    this.miningSpeed = miningSpeed;
    this.miningSpeedBeforeHaste = miningSpeedBeforeHaste;
    this.blockHardness = blockHardness;
    this.miningFortune = miningFortune;
    this.exp = exp;
    this.miningTier = miningTier;
    this.blockTier = blockTier;
    this.regenCooldown = regenCooldown;
    this.drops = drops;
    this.breakSound = breakSound;
    this.breakCustomSound = breakCustomSound;
    this.breakSoundVolume = breakSoundVolume;
    this.breakSoundPitch = breakSoundPitch;
  }
}

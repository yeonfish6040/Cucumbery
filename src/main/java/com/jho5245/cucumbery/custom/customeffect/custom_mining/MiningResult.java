package com.jho5245.cucumbery.custom.customeffect.custom_mining;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record MiningResult(boolean canMine, float toolSpeed, float miningSpeed, float blockHardness, float miningFortune, float exp, int miningTier, int blockTier, int regenCooldown, List<ItemStack> drops)
{
  public MiningResult(boolean canMine, float toolSpeed, float miningSpeed, float blockHardness, float miningFortune, float exp, int miningTier, int blockTier, int regenCooldown, @NotNull List<ItemStack> drops)
  {
    this.canMine = canMine;
    this.toolSpeed = toolSpeed;
    this.miningSpeed = miningSpeed;
    this.blockHardness = blockHardness;
    this.miningFortune = miningFortune;
    this.exp = exp;
    this.miningTier = miningTier;
    this.blockTier = blockTier;
    this.regenCooldown = regenCooldown;
    this.drops = drops;
  }
}

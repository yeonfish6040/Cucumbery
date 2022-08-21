package com.jho5245.cucumbery.listeners.player.no_groups;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.custom.customeffect.children.group.PlayerCustomEffect;
import com.jho5245.cucumbery.custom.customeffect.children.group.StringCustomEffectImple;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeRune;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerPickupExperience implements Listener
{
  @EventHandler
  public void onPlayerPickupExperience(PlayerPickupExperienceEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Player player = event.getPlayer();
    if (UserData.SPECTATOR_MODE.getBoolean(player))
    {
      event.setCancelled(true);
      return;
    }
    String pickUpMode = UserData.ITEM_PICKUP_MODE.getString(player);
    switch (pickUpMode)
    {
      case "sneak" -> {
        if (!player.isSneaking())
        {
          event.setCancelled(true);
          return;
        }
      }
      case "disabled" -> {
        event.setCancelled(true);
        return;
      }
    }
    ExperienceOrb experienceOrb = event.getExperienceOrb();
    int experience = experienceOrb.getExperience();
    if (CustomEffectManager.hasEffect(experienceOrb, CustomEffectType.COMBO_EXPERIENCE))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(experienceOrb, CustomEffectType.COMBO_EXPERIENCE);
      if (customEffect instanceof PlayerCustomEffect playerCustomEffect)
      {
        Player p = playerCustomEffect.getPlayer();
        if (!player.getUniqueId().equals(p.getUniqueId()))
        {
          event.setCancelled(true);
          return;
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0));
      }
    }
    else
    {
      double bonus = 0d;
      if (CustomEffectManager.hasEffect(player, CustomEffectTypeRune.RUNE_EXPERIENCE))
      {
        bonus += 1d;
      }
      if (CustomEffectManager.hasEffect(player, CustomEffectType.EXPERIENCE_BOOST))
      {
        int amplifier = CustomEffectManager.getEffect(player, CustomEffectType.EXPERIENCE_BOOST).getAmplifier();
        bonus += 0.05 * (amplifier + 1);
      }
      if (bonus > 0d)
      {
        experience = (int) (experience * (1d + bonus));
        experienceOrb.setExperience(experience);
      }
      if (CustomEffectManager.hasEffect(player, CustomEffectType.VAR_STOMACHACHE))
      {
        if (player.getTotalExperience() > experience)
        {
          player.giveExp(experience * (-2));
        }
        else
        {
          CustomEffectManager.addEffect(player, new StringCustomEffectImple(CustomEffectType.CUSTOM_DEATH_MESSAGE, 10, "custom_stomachache"));
          int amplifier = CustomEffectManager.getEffect(player, CustomEffectType.VAR_STOMACHACHE).getAmplifier();
          if (experience < 100 || !(Math.random() * 100d < amplifier * 0.1d))
          {
            double damage = experience - player.getTotalExperience();
            player.damage(damage * 0.1);
            player.setNoDamageTicks(0);
          }
          else
          {
            player.setHealth(0);
          }
          experienceOrb.remove();
          event.setCancelled(true);
          player.setTotalExperience(0);
          player.setLevel(0);
          player.setExp(0f);
        }
        if (UserData.SHOW_ACTIONBAR_ON_ITEM_PICKUP.getBoolean(player))
        {
          MessageUtil.sendActionBar(player, ComponentUtil.translate("&c%s을(를) 잃었습니다", ComponentUtil.translate("rg255,204;경험치 %s", experience)));
        }
        return;
      }
    }
    if (UserData.SHOW_ACTIONBAR_ON_ITEM_PICKUP.getBoolean(player))
    {
      MessageUtil.sendActionBar(player, ComponentUtil.translate("&b%s을(를) 주웠습니다", ComponentUtil.translate("rg255,204;경험치 %s", experience)));
    }
  }
}

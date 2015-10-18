package org.realcodingteam.txhcf;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Util {

	public static boolean isMiner(Player player) {
		boolean bool = false;
		
		for (ItemStack item: player.getInventory().getArmorContents()) {
			if (item.getType().name().contains("IRON")) bool = true;
			else bool = false;
		}
		
		return bool;
	}
	
	public static boolean isArcher(Player player) {
		boolean bool = false;
		
		for (ItemStack item: player.getInventory().getArmorContents()) {
			if (item.getType().name().contains("LEATHER")) bool = true;
			else bool = false;
		}
		
		return bool;
	}
	
	public static boolean isBard(Player player) {
		boolean bool = false;
		
		for (ItemStack item: player.getInventory().getArmorContents()) {
			if (item.getType().name().contains("GOLD")) bool = true;
			else bool = false;
		}
		
		return bool;
	}
	
	@SuppressWarnings("deprecation")
	public static void archerEvent(final Player player) {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(HCF.instance, new BukkitRunnable() {
			public void run() {
				if (isArcher(player)) {
					player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10, 2));
					player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10, 1));
				}
			}
		}, 0, 10);
	}
	
	@SuppressWarnings("deprecation")
	public static void minerEvent(final Player player) {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(HCF.instance, new BukkitRunnable() {
			public void run() {
				if (isMiner(player)) {
					player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 10, 1));
					player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 10, 2));
					
					if (player.getLocation().getY() < 15) 
						player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10, 1));
				}
			}
		}, 0, 10);
	}

	@SuppressWarnings("deprecation")
	public static void bardEvent(final Player player, final List<Player> players) {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(HCF.instance, new BukkitRunnable() {
			public void run() {
				if (isBard(player)) {
					switch (player.getItemInHand().getType()) {
					case SUGAR:
						effectNearbyEntities(new PotionEffect(PotionEffectType.SPEED, 10, 1), players);
					case BLAZE_POWDER:
						effectNearbyEntities(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 10, 0), players);
					case IRON_INGOT:
						effectNearbyEntities(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10, 1), players);
					case FEATHER:
						effectNearbyEntities(new PotionEffect(PotionEffectType.JUMP, 10, 1), players);
					case GHAST_TEAR:
						effectNearbyEntities(new PotionEffect(PotionEffectType.REGENERATION, 10, 0), players);
					default: 
					}
				}
			}
		}, 0, 10);
	}
	
	public static void effectNearbyEntities(PotionEffect pe, List<Player> players) {
		for (Player player : players)
			player.addPotionEffect(pe);
	}
	
	public static double calcArcherDMG(double distance, double damage) {
		double dmg = distance * 0.09;
		return dmg + damage;
	}
}

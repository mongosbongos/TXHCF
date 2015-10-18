package org.realcodingteam.txhcf;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.massivecraft.factions.FPlayers;

public class Util {

	public static boolean isMiner(Player player) {
		for (ItemStack item: player.getInventory().getArmorContents()) {
			if (!item.getType().name().contains("IRON"))
				return false;
		}
		
		return true;
	}
	
	public static boolean isArcher(Player player) {
		for (ItemStack item: player.getInventory().getArmorContents()) {
			if (!item.getType().name().contains("LEATHER"))
				return false;
		}
		
		return true;
	}
	
	public static boolean isBard(Player player) {
		for (ItemStack item: player.getInventory().getArmorContents()) {
			if (!item.getType().name().contains("GOLD"))
				return false;
		}
		
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public static void archerEvent(final Player player) {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(HCF.instance, new BukkitRunnable() {
			public void run() {
				if (isArcher(player)) {
					player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 2));
					player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 99999, 1));
				} else {
					player.removePotionEffect(PotionEffectType.SPEED);
					player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
				}
			}
		}, 0, 10);
	}
	
	@SuppressWarnings("deprecation")
	public static void minerEvent(final Player player) {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(HCF.instance, new BukkitRunnable() {
			public void run() {
				if (isMiner(player)) {
					player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 99999, 1));
					player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 99999, 2));
					player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 99999, 0));
					
					if (player.getLocation().getY() < 15) 
						player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 1));
					else {
						if (player.hasPotionEffect(PotionEffectType.INVISIBILITY))
							player.removePotionEffect(PotionEffectType.INVISIBILITY);
					}
				} else {
					player.removePotionEffect(PotionEffectType.NIGHT_VISION);
					player.removePotionEffect(PotionEffectType.FAST_DIGGING);
					player.removePotionEffect(PotionEffectType.INVISIBILITY);
					player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
				}
			}
		}, 0, 10);
	}

	@SuppressWarnings("deprecation")
	public static void bardEvent(final Player player) {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(HCF.instance, new BukkitRunnable() {
			public void run() {
				if (isBard(player)) {				
					List<Player> players = new ArrayList<Player>();
					
					for (Player p : FPlayers.getInstance().getByPlayer(player).getFaction().getOnlinePlayers()) {
						if (player.getLocation().distance(p.getLocation()) < 15)
							players.add(p);
					}
					
					player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 99999, 1));
					player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 1));
					player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 99999, 0));
					
					switch (player.getItemInHand().getType()) {
					case SUGAR:
						effectNearbyEntities(new PotionEffect(PotionEffectType.SPEED, 140, 1), players);
						break;
					case BLAZE_POWDER:
						effectNearbyEntities(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 140, 0), players);
						break;
					case IRON_INGOT:
						effectNearbyEntities(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 140, 1), players);
						break;
					case FEATHER:
						effectNearbyEntities(new PotionEffect(PotionEffectType.JUMP, 140, 1), players);
						break;
					case GHAST_TEAR:
						effectNearbyEntities(new PotionEffect(PotionEffectType.REGENERATION, 140, 0), players);
						break;
					default: 
					}
				} else {
					player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
					player.removePotionEffect(PotionEffectType.SPEED);
					player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
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

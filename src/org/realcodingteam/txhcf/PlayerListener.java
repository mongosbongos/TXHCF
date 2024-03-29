package org.realcodingteam.txhcf;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.massivecraft.factions.FPlayers;

public class PlayerListener implements Listener {

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
		if (Util.isArcher(player))
			Util.archerEvent(player);
		
		if (Util.isMiner(player))
			Util.minerEvent(player);
		
		if (Util.isBard(player)) 
			Util.bardEvent(player);
		
		if (player.getWorld().getEnvironment() == Environment.NETHER) {
			if (player.getLocation().getBlock().getType() == Material.STATIONARY_WATER)
				player.teleport((new Location(Bukkit.getWorld("world"), 0, 70, -400)));
		}
		
		if (player.getWorld().getEnvironment() == Environment.THE_END) {
			if (player.getLocation().getBlock().getType() == Material.STATIONARY_WATER)
				player.teleport((new Location(Bukkit.getWorld("world"), 0, 70, -400)));
		}
	}
	
	@EventHandler
	public void onInventoryInteract(InventoryInteractEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) { return; }
		
		Player player = (Player) event.getWhoClicked();
		
		if (Util.isArcher(player)) {
			Util.archerEvent(player);
		}
		
		if (Util.isMiner(player)) {
			Util.minerEvent(player);
		}
		
		if (Util.isBard(player)) {
			Util.bardEvent(player);
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		List<Player> players = FPlayers.getInstance().getByPlayer(player).getFaction().getOnlinePlayers();
		
		switch (event.getAction()) {
			case RIGHT_CLICK_AIR: 
			case RIGHT_CLICK_BLOCK:
				if (Util.isBard(player)) {
					List<Player> list = new ArrayList<Player>();
					ItemStack item = player.getItemInHand();
					
					for (Player p : FPlayers.getInstance().getByPlayer(player).getFaction().getOnlinePlayers()) {
						if (player.getLocation().distance(p.getLocation()) < 15)
							list.add(p);
					}
					
					switch (player.getItemInHand().getType()) {
					case SUGAR:
						Util.effectNearbyEntities(new PotionEffect(PotionEffectType.SPEED, 160, 3), players);
						item.setAmount(item.getAmount() - 1);
						break;
					case BLAZE_POWDER:
						Util.effectNearbyEntities(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 160, 2), players);
						item.setAmount(item.getAmount() - 1);
						break;
					case IRON_INGOT:
						Util.effectNearbyEntities(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 160, 2), players);
						item.setAmount(item.getAmount() - 1);
						break;
					case FEATHER:
						Util.effectNearbyEntities(new PotionEffect(PotionEffectType.JUMP, 160, 2), players);
						item.setAmount(item.getAmount() - 1);
						break;
					case GHAST_TEAR:
						Util.effectNearbyEntities(new PotionEffect(PotionEffectType.REGENERATION, 160, 2), players);
						item.setAmount(item.getAmount() - 1);
						break;
					default: 
					}
					player.setItemInHand(item);
				}
			default:
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if (event.getCause() != DamageCause.PROJECTILE) { return; }
		
		Projectile proj = (Projectile) event.getDamager();
		if (!(proj.getShooter() instanceof Player)) { return; }
		Player player = (Player) proj.getShooter();
		Location loc = event.getEntity().getLocation();
		
		if (Util.isArcher((Player) player)) {
			event.setDamage(DamageModifier.ARMOR, -2);
			event.setDamage(Util.calcArcherDMG(player.getLocation().distance(loc), event.getDamage()));
		}
	}
	
	@EventHandler
	public void onItemSwitch(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		
		if (Util.isBard(player)) {
			Util.bardEvent(player);
		}
	}
	
	@EventHandler
	public void onPortal(PlayerPortalEvent event) {
		if (event.getCause() == TeleportCause.END_PORTAL) {
			if (event.getFrom().getWorld().getEnvironment() == Environment.NORMAL) 
				event.setTo(new Location(Bukkit.getWorld("world_the_end"), 89, 58, 37));
		} else if (event.getCause() == TeleportCause.NETHER_PORTAL) {
			if (event.getFrom().getWorld().getEnvironment() == Environment.NORMAL) 
				event.setTo(new Location(Bukkit.getWorld("world_nether"), 0, 36, 10));
		}
	}
	
	@EventHandler
	public void onCraft(CraftItemEvent event) {
		ItemStack item = event.getRecipe().getResult();
		
		if (item.getType() == Material.GOLDEN_APPLE) {
			if (item.getData().getData() == (short) 1) {
				event.setCancelled(true);
			}
		}
	}
}

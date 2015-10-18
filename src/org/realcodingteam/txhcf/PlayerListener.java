package org.realcodingteam.txhcf;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
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
		
		if (player.getWorld().getEnvironment() == Environment.NETHER) {
			if (player.getLocation().getBlock().getType() == Material.STATIONARY_WATER)
				player.teleport((new Location(Bukkit.getWorld("hcf"), 0, 70, -400)));
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
	}
	
	@EventHandler//exactly jc
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		List<Player> players = FPlayers.getInstance().getByPlayer(player).getFaction().getOnlinePlayers();
		
		switch (event.getAction()) {
			case RIGHT_CLICK_AIR: 
			case RIGHT_CLICK_BLOCK:
				if (Util.isBard(player)) {//yes
					List<Player> list = new ArrayList<Player>();
					ItemStack item = player.getItemInHand();
					
					for (Player p : FPlayers.getInstance().getByPlayer(player).getFaction().getOnlinePlayers()) {
						if (player.getLocation().distance(p.getLocation()) < 15)
							list.add(p);
					}
					
					switch (player.getItemInHand().getType()) {
					case SUGAR:
						Util.effectNearbyEntities(new PotionEffect(PotionEffectType.SPEED, 10, 2), players);
						item.setAmount(item.getAmount() - 1);
					case BLAZE_POWDER:
						Util.effectNearbyEntities(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 10, 1), players);
						item.setAmount(item.getAmount() - 1);
					case IRON_INGOT:
						Util.effectNearbyEntities(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10, 2), players);
						item.setAmount(item.getAmount() - 1);
					case FEATHER:
						Util.effectNearbyEntities(new PotionEffect(PotionEffectType.JUMP, 10, 2), players);
						item.setAmount(item.getAmount() - 1);
					case GHAST_TEAR:
						Util.effectNearbyEntities(new PotionEffect(PotionEffectType.REGENERATION, 10, 1), players);
						item.setAmount(item.getAmount() - 1);
					default: 
						
					}
				}
			default:
		}
	}
	
	@EventHandler
	public void onItemSwitch(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		
		if (Util.isBard(player)) {
			List<Player> list = new ArrayList<Player>();
			
			for (Player p : FPlayers.getInstance().getByPlayer(player).getFaction().getOnlinePlayers()) {
				if (player.getLocation().distance(p.getLocation()) < 15)
					list.add(p);
			}
			
			Util.bardEvent(player, list);
		}
	}
	
	@EventHandler
	public void onTp(PlayerTeleportEvent event) {
		if (event.getCause() == TeleportCause.END_PORTAL) {
			if (event.getTo().getWorld().getEnvironment() == Environment.NORMAL) 
				event.setTo(new Location(Bukkit.getWorld("hcf"), 0, 70, -400));
			if (event.getFrom().getWorld().getEnvironment() == Environment.NORMAL) 
				event.setTo(new Location(Bukkit.getWorld("world_the_end"), 99.5, 58, 37.5));
		} else if (event.getCause() == TeleportCause.NETHER_PORTAL) {
			if (event.getFrom().getWorld().getEnvironment() == Environment.NORMAL) 
				event.setTo(new Location(Bukkit.getWorld("world_nether"), 0.56, 36, 10.8));
		}
	}
}

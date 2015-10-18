package org.realcodingteam.txhcf;

import org.bukkit.plugin.java.JavaPlugin;

public class HCF extends JavaPlugin {
	
	public static HCF instance;

	public void onEnable() {
		try {
			instance = this;
			
			getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

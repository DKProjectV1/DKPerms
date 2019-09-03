package ch.dkrieger.permissionsystem.bukkit.tools.tablist;

import ch.dkrieger.permissionsystem.bukkit.tools.tablist.utils.TabListStyle;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import ch.dkrieger.permissionsystem.lib.player.PlayerDesign;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.07.18 17:13
 *
 */

public class TabListManager {
	
	private static TabListManager INSTANCE;
	
	public TabListManager(){
		INSTANCE = this;
		new TabListStyle();
	}

	public void loadPlayer(Player player){
		updatePlayer(player);
		for(Player players : Bukkit.getOnlinePlayers()) if(players != player) setTab(players, player);
	}

	public void updatePlayer(Player player){
		setTab(player);
	}

	public void setTab(Player player, Player receiver){
		if(PermissionPlayerManager.getInstance() == null) return;
		PermissionPlayer permplayer = PermissionPlayerManager.getInstance().getPermissionPlayer(player.getUniqueId());
		if(permplayer == null) return;
		PermissionGroup group = permplayer.getHighestGroup();
		if(group == null) return;
		PlayerDesign design = group.getPlayerDesign();
		if(design == null) return;
		String prefix = ChatColor.translateAlternateColorCodes('&',design.getPrefix());
		String suffix = ChatColor.translateAlternateColorCodes('&',design.getSuffix());
		String priority = ""+getPriority(group.getPriority());
		if(prefix.equalsIgnoreCase("-1")) prefix = "";
		if(suffix.equalsIgnoreCase("-1")) suffix = "";

		TabListStyle.setStyle(prefix, suffix, priority, player,receiver);
	}

	public void setTab(Player player) {
		for(Player players : Bukkit.getOnlinePlayers()) setTab(player, players);
	}

	private String getPriority(int priority){
		String p = ""+priority;
		if(p.length() == 1) return "00000"+priority;
		if(p.length() == 2) return "0000"+priority;
		if(p.length() == 3) return "000"+priority;
		if(p.length() == 4) return "00"+priority;
		if(p.length() == 5) return "0"+priority;
		if(p.length() == 6) return ""+priority;
		return "0";
	}

	public static TabListManager getInstance() {
		return INSTANCE;
	}
}

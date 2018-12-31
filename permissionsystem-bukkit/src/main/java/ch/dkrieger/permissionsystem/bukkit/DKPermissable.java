package ch.dkrieger.permissionsystem.bukkit;

import ch.dkrieger.permissionsystem.lib.config.Config;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;

import java.util.UUID;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 23:29
 *
 */

public class DKPermissable extends PermissibleBase {

	private final UUID uuid;

	public DKPermissable(Player player) {
		super(player);
		this.uuid = player.getUniqueId();
		clearPermissions();
	}	
	@Override
	public boolean hasPermission(Permission perm) {
		return hasPermission(perm.getName());
	}
	@Override
	public boolean hasPermission(String permission) {
		PermissionPlayer permissionplayer = PermissionPlayerManager.getInstance().getPermissionPlayer(this.uuid);
		if(permissionplayer == null) return false;
		String world = null;
		try{
			world = Bukkit.getPlayer(this.uuid).getWorld().getName();
		}catch (NullPointerException exception){}
		Boolean has = permissionplayer.hasPermission(permission,BukkitBootstrap.getInstance().getServerName(),world);
		if(!has){
			for(Permission perm : Bukkit.getPluginManager().getPermissions()){
				if(perm.getChildren().containsKey(permission)){
					has = permissionplayer.hasPermission(perm.getName(),BukkitBootstrap.getInstance().getServerName(),world);
					if(has) break;
				}
			}
		}
		return has;
	}
	@Override
	public boolean isPermissionSet(Permission perm){
		return hasPermission(perm);
	}
	@Override
	public boolean isPermissionSet(String permission) {
		return hasPermission(permission);
	}
	@Override
	public boolean isOp() {
		if(!Config.SECURITY_OPERATOR_ENABLED) return false;
		else return super.isOp();
	}
	@Override
	public void setOp(boolean value) {
		super.setOp(false);
	}
}

package ch.dkrieger.permissionsystem.bukkit;

import ch.dkrieger.permissionsystem.lib.config.Config;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import ch.dkrieger.permissionsystem.lib.utils.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 23:29
 *
 */

public class DKPermissable extends PermissibleBase {

	private final UUID uuid;
	private final DKPermissable instance;

	public DKPermissable(Player player) {
		super(player);
		instance = this;
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

	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		PermissionPlayer player = PermissionPlayerManager.getInstance().getPermissionPlayer(this.uuid);
		if(player == null) return new HashSet<>();
		Set<PermissionAttachmentInfo> permission = new HashSet<>();
		String world = null;
		try{
			world = Bukkit.getPlayer(this.uuid).getWorld().getName();
		}catch (NullPointerException ignored){}
		GeneralUtil.iterateForEach(player.getAllPermissions(BukkitBootstrap.getInstance().getServerName(), world)
				,object -> permission.add(new PermissionAttachmentInfo(instance,object.getPermission(),null
				,!object.getPermission().startsWith("-"))));
		return permission;
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
		throw new UnsupportedOperationException("Bukkit permission attachments are blocked for security reasons");
	}
	@Override
	public PermissionAttachment addAttachment(Plugin plugin) {
		throw new UnsupportedOperationException("Bukkit permission attachments are blocked for security reasons");
	}
	@Override
	public void removeAttachment(PermissionAttachment attachment) {
		throw new UnsupportedOperationException("Bukkit permission attachments are blocked for security reasons");
	}
	@Override
	public void recalculatePermissions() {
		clearPermissions();
	}
	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
		throw new UnsupportedOperationException("Bukkit permission attachments are blocked for security reasons");
	}
	@Override
	public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
		throw new UnsupportedOperationException("Bukkit permission attachments are blocked for security reasons");
	}
}

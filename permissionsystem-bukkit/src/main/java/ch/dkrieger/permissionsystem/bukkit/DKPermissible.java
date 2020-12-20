package ch.dkrieger.permissionsystem.bukkit;

import ch.dkrieger.permissionsystem.lib.config.Config;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import ch.dkrieger.permissionsystem.lib.utils.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.permissions.*;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.logging.Level;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 23:29
 *
 */

public class DKPermissible extends PermissibleBase {

	private final UUID uuid;


	private final Map<String,PermissionAttachmentInfo> permissions;
	private final List<PermissionAttachment> attachments;


	public DKPermissible(Player player) {
		super(player);
		this.uuid = player.getUniqueId();

		this.permissions = new HashMap<>();
		this.attachments = new LinkedList<>();

		recalculatePermissions();
	}

	@Override
	public boolean isOp() {
		if(!Config.SECURITY_OPERATOR_ENABLED) return false;
		else return super.isOp();
	}

	@Override
	public boolean isPermissionSet(String permission) {
		PermissionPlayer permissionplayer = PermissionPlayerManager.getInstance().getPermissionPlayer(this.uuid);
		if(permissionplayer == null) return false;
		String world = null;

		Player bukkitPlayer = Bukkit.getPlayer(this.uuid);
		if(bukkitPlayer != null){
			World bukkitWorld = bukkitPlayer.getWorld();
			world = bukkitWorld.getName();
		}
		boolean result =  permissionplayer.isPermissionSet(permission,BukkitBootstrap.getInstance().getServerName(),world);
		if(result) return true;
		return permissions.containsKey(permission);
	}

	@Override
	public boolean isPermissionSet(Permission permission) {
		if (permission == null) {
			throw new IllegalArgumentException("Permission cannot be null");
		}

		String name = permission.getName().toLowerCase(java.util.Locale.ENGLISH);

		if (isPermissionSet(name)) {
			return permissions.get(name).getValue();
		}
		return permission.getDefault().getValue(isOp());
	}

	@Override
	public boolean hasPermission(String permission) {
		PermissionPlayer permissionplayer = PermissionPlayerManager.getInstance().getPermissionPlayer(this.uuid);
		if(permissionplayer == null) return false;
		String world = null;

		Player bukkitPlayer = Bukkit.getPlayer(this.uuid);
		if(bukkitPlayer != null){
			World bukkitWorld = bukkitPlayer.getWorld();
			world = bukkitWorld.getName();
		}

		boolean has = permissionplayer.hasPermission(permission,BukkitBootstrap.getInstance().getServerName(),world);
		if(!has){
			for(Permission perm : Bukkit.getPluginManager().getPermissions()){
				if(perm.getChildren().containsKey(permission)){
					has = permissionplayer.hasPermission(perm.getName(),BukkitBootstrap.getInstance().getServerName(),world);
					if(has) break;
				}
			}
		}

		if(has) return true;

		PermissionAttachmentInfo attachmentInfo = permissions.get(permission);
		if (attachmentInfo != null) {
			return attachmentInfo.getValue();
		}
		return false;
	}

	@Override
	public boolean hasPermission(Permission permission) {
		return hasPermission(permission.getName());
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
		if (name == null) {
			throw new IllegalArgumentException("Permission name cannot be null");
		} else if (plugin == null) {
			throw new IllegalArgumentException("Plugin cannot be null");
		} else if (!plugin.isEnabled()) {
			throw new IllegalArgumentException("Plugin " + plugin.getDescription().getFullName() + " is disabled");
		}

		PermissionAttachment result = addAttachment(plugin);
		result.setPermission(name, value);

		recalculatePermissions();

		return result;
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin) {
		if (plugin == null) {
			throw new IllegalArgumentException("Plugin cannot be null");
		} else if (!plugin.isEnabled()) {
			throw new IllegalArgumentException("Plugin " + plugin.getDescription().getFullName() + " is disabled");
		}

		PermissionAttachment result = new PermissionAttachment(plugin, this);

		attachments.add(result);
		recalculatePermissions();

		return result;
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
		if (name == null) {
			throw new IllegalArgumentException("Permission name cannot be null");
		} else if (plugin == null) {
			throw new IllegalArgumentException("Plugin cannot be null");
		} else if (!plugin.isEnabled()) {
			throw new IllegalArgumentException("Plugin " + plugin.getDescription().getFullName() + " is disabled");
		}

		PermissionAttachment result = addAttachment(plugin, ticks);

		if (result != null) {
			result.setPermission(name, value);
		}

		return result;
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
		if (plugin == null) {
			throw new IllegalArgumentException("Plugin cannot be null");
		} else if (!plugin.isEnabled()) {
			throw new IllegalArgumentException("Plugin " + plugin.getDescription().getFullName() + " is disabled");
		}

		PermissionAttachment result = addAttachment(plugin);

		if (Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new RemoveAttachmentRunnable(result), ticks) == -1) {
			Bukkit.getServer().getLogger().log(Level.WARNING, "Could not add PermissionAttachment to " + this + " for plugin " + plugin.getDescription().getFullName() + ": Scheduler returned -1");
			result.remove();
			return null;
		} else {
			return result;
		}
	}

	@Override
	public void removeAttachment(PermissionAttachment attachment) {
		if (attachment == null) {
			throw new IllegalArgumentException("Attachment cannot be null");
		}

		if (attachments.contains(attachment)) {
			attachments.remove(attachment);
			PermissionRemovedExecutor removedExecutor = attachment.getRemovalCallback();

			if (removedExecutor != null) {
				removedExecutor.attachmentRemoved(attachment);
			}

			recalculatePermissions();
		} else {
			throw new IllegalArgumentException("Given attachment is not part of Permissible object " + this);
		}
	}

	@Override
	public void recalculatePermissions() {
		if(permissions == null) return;
		clearPermissions();

		for (PermissionAttachment attachment : attachments) {
			calculateChildPermissions(attachment.getPermissions(), false, attachment);
		}

	}

	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {//@Todo optimize
		PermissionPlayer player = PermissionPlayerManager.getInstance().getPermissionPlayer(this.uuid);
		if(player == null) return new HashSet<>();
		Set<PermissionAttachmentInfo> permission = new HashSet<>(permissions.values());

		Player bukkitPlayer = Bukkit.getPlayer(this.uuid);
		if(bukkitPlayer != null){
			String world = bukkitPlayer.getWorld().getName();
			GeneralUtil.iterateForEach(player.getAllPermissions(BukkitBootstrap.getInstance().getServerName(), world)
					,object -> permission.add(new PermissionAttachmentInfo(this,object.getPermission(),null
							,!object.getPermission().startsWith("-"))));
		}
		return permission;
	}

	@Override
	public synchronized void clearPermissions() {
		Set<String> permissions = this.permissions.keySet();

		for (String name : permissions) {
			Bukkit.getServer().getPluginManager().unsubscribeFromPermission(name, this);
		}

		Bukkit.getServer().getPluginManager().unsubscribeFromDefaultPerms(false, this);
		Bukkit.getServer().getPluginManager().unsubscribeFromDefaultPerms(true, this);

		this.permissions.clear();
	}

	private void calculateChildPermissions(Map<String, Boolean> children, boolean invert, PermissionAttachment attachment) {
		for (Map.Entry<String, Boolean> entry : children.entrySet()) {
			String name = entry.getKey();

			Permission permission = Bukkit.getServer().getPluginManager().getPermission(name);
			boolean value = entry.getValue() ^ invert;

			permissions.put(name, new PermissionAttachmentInfo(this, name, attachment, value));
			Bukkit.getServer().getPluginManager().subscribeToPermission(name, this);

			if (permission != null) {
				calculateChildPermissions(permission.getChildren(), !value, attachment);
			}
		}
	}

	private static class RemoveAttachmentRunnable implements Runnable {

		private final PermissionAttachment attachment;

		public RemoveAttachmentRunnable(PermissionAttachment attachment) {
			this.attachment = attachment;
		}

		@Override
		public void run() {
			attachment.remove();
		}
	}
}

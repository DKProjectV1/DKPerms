package ch.dkrieger.permissionsystem.bungeecord;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 15.07.18 13:36
 *
 */

import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import de.dytanic.cloudnet.bridge.internal.util.CloudPlayerCommandSender;
import de.dytanic.cloudnet.lib.player.CloudPlayer;
import net.md_5.bungee.api.CommandSender;

public class CloudNetV2Extension {

    public static Boolean isCloudSender(CommandSender sender){
        return sender instanceof CloudPlayerCommandSender;
    }
    public static Boolean hasPermission(CommandSender sender,String permission){
        if(sender instanceof CloudPlayerCommandSender){
            CloudPlayer player = ((CloudPlayerCommandSender)sender).getCloudPlayer();
            if(player != null){
                PermissionPlayer permplayer = PermissionPlayerManager.getInstance().getPermissionPlayer(player.getUniqueId());
                if(permplayer != null) return permplayer.hasPermission(permission);
            }
        }
        return false;
    }
}

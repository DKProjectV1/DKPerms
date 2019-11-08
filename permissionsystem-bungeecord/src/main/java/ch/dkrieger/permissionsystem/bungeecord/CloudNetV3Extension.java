package ch.dkrieger.permissionsystem.bungeecord;

import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import de.dytanic.cloudnet.ext.syncproxy.bungee.util.LoginPendingConnectionCommandSender;
import net.md_5.bungee.api.CommandSender;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 06.11.19, 20:38
 *
 */

public class CloudNetV3Extension {

    public static boolean isCloudSender(CommandSender sender){
        return sender instanceof LoginPendingConnectionCommandSender;
    }

    public static boolean hasPermission(CommandSender sender,String permission){
        if(sender instanceof LoginPendingConnectionCommandSender){
            PermissionPlayer permplayer = PermissionPlayerManager.getInstance().getPermissionPlayer(((LoginPendingConnectionCommandSender) sender).getUniqueId());
            if(permplayer != null) return permplayer.hasPermission(permission);
        }
        return false;
    }
}

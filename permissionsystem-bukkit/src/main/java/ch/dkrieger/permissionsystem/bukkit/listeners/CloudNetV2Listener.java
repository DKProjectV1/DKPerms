package ch.dkrieger.permissionsystem.bukkit.listeners;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 11.07.18 09:44
 *
 */

import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import de.dytanic.cloudnet.api.CloudAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class CloudNetV2Listener implements Listener{

    @EventHandler(priority= EventPriority.HIGHEST)
    public void onLogin(PlayerLoginEvent event){
        if(event.getResult() == PlayerLoginEvent.Result.KICK_BANNED){
            if(event.getKickMessage().equalsIgnoreCase(CloudAPI.getInstance().getCloudNetwork().getMessages().getString("joinpower-deny"))){
                int joinPower = CloudAPI.getInstance().getServerGroupData(CloudAPI.getInstance().getGroup()).getJoinPower();
                PermissionPlayer player = PermissionPlayerManager.getInstance().getPermissionPlayer(event.getPlayer().getUniqueId());
                if(player != null && joinPower <= player.getJoinPower()) event.allow();
            }
        }
    }
}

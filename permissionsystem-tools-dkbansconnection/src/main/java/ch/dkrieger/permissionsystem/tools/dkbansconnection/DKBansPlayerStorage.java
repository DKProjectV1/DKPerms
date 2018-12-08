package ch.dkrieger.permissionsystem.tools.dkbansconnection;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.07.18 22:12
 *
 */

import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.NetworkPlayerManager;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerStorage;

import java.util.UUID;

public class DKBansPlayerStorage implements PermissionPlayerStorage{

    public PermissionPlayer getPermissionPlayer(UUID uuid) throws Exception {
        NetworkPlayer player = NetworkPlayerManager.getInstance().getNetworkPlayer(uuid);
        if(player != null) return new PermissionPlayer(player.getPlayerID(),player.getName(),uuid);
        return null;
    }
    public PermissionPlayer getPermissionPlayer(String name) throws Exception {
        NetworkPlayer player = NetworkPlayerManager.getInstance().getNetworkPlayer(name);
        if(player != null) return new PermissionPlayer(player.getPlayerID(),player.getName(),player.getUUID());
        return null;
    }
    public PermissionPlayer createPermissionPlayer(UUID uuid, String name) {
        return null;
    }
    public void updateName(UUID uuid, String name) {
        return;
    }
}

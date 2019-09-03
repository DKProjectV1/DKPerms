package ch.dkrieger.permissionsystem.lib.player;

import java.util.UUID;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 20:43
 *
 */

public interface PermissionPlayerStorage {

    PermissionPlayer getPermissionPlayer(UUID uuid) throws Exception;

    PermissionPlayer getPermissionPlayer(String name) throws Exception;

    PermissionPlayer createPermissionPlayer(UUID uuid, String name);

    void updateName(UUID uuid, String name);

}

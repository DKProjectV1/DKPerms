package ch.dkrieger.permissionsystem.lib.player;

import java.util.UUID;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 20:43
 *
 */

public interface PermissionPlayerStorage {

    public PermissionPlayer getPermissionPlayer(UUID uuid) throws Exception;

    public PermissionPlayer getPermissionPlayer(String name) throws Exception;

    public PermissionPlayer createPermissionPlayer(UUID uuid, String name);

    public void updateName(UUID uuid, String name);

}

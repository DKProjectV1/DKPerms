package ch.dkrieger.permissionsystem.lib.group;

import java.util.List;
import java.util.UUID;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 20:43
 *
 */

public interface PermissionGroupStorage {

    List<PermissionGroup> loadGroups();

    PermissionGroup createGroup(String name);

    void deleteGroup(UUID uuid);

    void setSetting(UUID uuid,String identifier, Object value);

    List<UUID> getPlayers(PermissionGroup group);

}

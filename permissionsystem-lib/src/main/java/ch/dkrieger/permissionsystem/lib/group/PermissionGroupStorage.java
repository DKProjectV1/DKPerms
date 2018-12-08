package ch.dkrieger.permissionsystem.lib.group;

import java.util.List;
import java.util.UUID;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 20:43
 *
 */

public interface PermissionGroupStorage {

    public List<PermissionGroup> loadGroups();

    public PermissionGroup createGroup(String name);

    public void deleteGroup(UUID uuid);

    public void setSetting(UUID uuid,String identifier, Object value);

    public List<UUID> getPlayers(PermissionGroup group);

}

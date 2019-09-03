package ch.dkrieger.permissionsystem.lib.command;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 23.06.18 12:51
 *
 */

import java.util.Collection;

public interface PermissionCommandManager {

    Collection<PermissionCommand> getCommands();

    PermissionCommand getCommand(String name);

    void registerCommand(PermissionCommand command);

}

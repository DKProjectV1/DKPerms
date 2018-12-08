package ch.dkrieger.permissionsystem.lib.command;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 23.06.18 12:51
 *
 */

import java.util.Collection;

public interface PermissionCommandManager {

    public Collection<PermissionCommand> getCommands();

    public PermissionCommand getCommand(String name);

    public void registerCommand(PermissionCommand command);

}

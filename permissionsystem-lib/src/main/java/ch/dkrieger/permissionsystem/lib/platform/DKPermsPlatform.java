package ch.dkrieger.permissionsystem.lib.platform;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 10.06.18 13:22
 *
 */

import ch.dkrieger.permissionsystem.lib.command.PermissionCommandManager;
import ch.dkrieger.permissionsystem.lib.task.PermissionTaskManager;

import java.io.File;

public interface DKPermsPlatform {

    public String getPlatformName();

    public String getServerVersion();

    public File getFolder();

    public PermissionCommandManager getCommandManager();

    public PermissionTaskManager getTaskManager();

    public String translateColorCodes(String value);

}

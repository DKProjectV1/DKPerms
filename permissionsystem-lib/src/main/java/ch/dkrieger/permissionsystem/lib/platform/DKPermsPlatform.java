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

    String getPlatformName();

    String getServerVersion();

    File getFolder();

    PermissionCommandManager getCommandManager();

    PermissionTaskManager getTaskManager();

    String translateColorCodes(String value);

}

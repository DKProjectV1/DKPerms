package ch.dkrieger.permissionsystem.lib.importation;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 15.07.18 20:51
 *
 */

import ch.dkrieger.permissionsystem.lib.command.PermissionCommandSender;

import java.io.File;

public interface PermissionImport {

    String getName();

    boolean isAvailable();

    boolean needFile();

    void importData(PermissionCommandSender sender, File file);

}

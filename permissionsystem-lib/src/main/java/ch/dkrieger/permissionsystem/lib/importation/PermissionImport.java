package ch.dkrieger.permissionsystem.lib.importation;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 15.07.18 20:51
 *
 */

import ch.dkrieger.permissionsystem.lib.command.PermissionCommandSender;

import java.io.File;

public interface PermissionImport {

    public String getName();

    public Boolean isAvailable();

    public Boolean needFile();

    public void importData(PermissionCommandSender sender, File file);

}

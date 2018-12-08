package ch.dkrieger.permissionsystem.lib.command;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 23.06.18 12:51
 *
 */

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public abstract class PermissionCommand {

    private String name, permission;
    private List<String> aliases;

    public PermissionCommand(String name, String permission, String... aliases) {
        this.name = name;
        this.permission = permission;
        this.aliases = Arrays.asList(aliases);
    }
    public String getName() {
        return name;
    }
    public String getPermission() {
        return permission;
    }
    public List<String> getAliases() {
        return aliases;
    }
    public Boolean hasAliase(String name){
        if(this.name.equalsIgnoreCase(name) || this.aliases.contains(name.toLowerCase())) return true;
        return false;
    }
    public abstract void execute(PermissionCommandSender sender, String[] args);

    public abstract List<String> tabcomplete(PermissionCommandSender sender, String[] args);
}

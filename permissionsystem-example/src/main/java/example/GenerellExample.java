package example;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 15.07.18 16:51
 *
 */

import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;

import java.util.concurrent.TimeUnit;

public class GenerellExample {

    public GenerellExample() {
        //Get a player from database
        PermissionPlayer player = PermissionPlayerManager.getInstance().getPermissionPlayer("Davide Wietlisbach");

        player.getHighestGroup(); //get the highest permission group
        player.getPlayerDesign(); //get the design from the highest group

        player.getPlayerDesign().getPrefix();
        player.getPlayerDesign().getSuffix();
        player.getPlayerDesign().getDisplay();
        player.getPlayerDesign().getColor();

        PermissionGroup group = PermissionGroupManager.getInstance().getGroup("admin");

        group.getDescription();//get description
        group.getPriority();//get priority
        group.getJoinpower();//get joinpower (only CloudNet)
        group.getPlayerDesign();//get playerdesign
        group.getPlayers();//get list of all players (in group)

        group.isTeam(); // is a team group
        group.isDefault(); //is a default group

        group.setDescription("Hey"); //set description
        group.setPriority(1); //set priority
        group.setDefault(false); //set default
        group.setTeam(true); //set team
        group.setPrefix("&4Admin_&8-_&4"); //set prefix

        //player and group
        player.getPermissionData(); //get all permissions in data form
        player.getPermissions("server-1","world-1"); //get permissions from this player
        player.getAllPermissions("server-1","world-1"); // get all permissions (groups etc.)

        player.hasPermission("permission","server-1","world-1"); //check a permission

        //add permissions
        player.addPermission("permission");
        player.addPermission("permission",2L, TimeUnit.DAYS);
        player.addPermission("permission","world-1",2L, TimeUnit.DAYS);

        player.addServerPermission("server-1","permission");
        player.addServerPermission("server-1","permission",40L, TimeUnit.SECONDS);
        player.addServerPermission("server-1","permission","world-1",40L, TimeUnit.SECONDS);

        player.addServerGroupPermission("server","permission");
        player.addServerGroupPermission("server","permission",40L, TimeUnit.SECONDS);
        player.addServerGroupPermission("server","permission","world-1",40L, TimeUnit.SECONDS);

        //remove permissions
        player.removePermission("permission");
        player.removePermission("permission","world-1");

        player.removeServerPermission("server-1","permission");
        player.removeServerPermission("server-1","permission","world-1");

        player.removeServerGroupPermission("server","permission");
        player.removeServerGroupPermission("server","permission","world-1");

        //clear permissions
        player.clearAllPermissions();
        player.clearPermissions();//global permissions
        player.clearPermissions("world-1");

        player.clearServerPermissions(); //All server permissions
        player.clearServerPermissions("permission");
        player.clearServerPermissions("permission","world-1");

        player.clearServerGroupPermissions(); //All servergroup permissions
        player.clearServerGroupPermissions("permission");
        player.clearServerGroupPermissions("permission","world-1");

        //groups

        player.setGroup(group,30L,TimeUnit.DAYS);
        player.addGroup(group,30L,TimeUnit.DAYS);
        player.removeGroup(group);
        player.clearGroups();

    }
}

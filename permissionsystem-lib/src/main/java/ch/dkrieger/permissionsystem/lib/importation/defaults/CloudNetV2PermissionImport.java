package ch.dkrieger.permissionsystem.lib.importation.defaults;

import ch.dkrieger.permissionsystem.lib.PermissionSystem;
import ch.dkrieger.permissionsystem.lib.PermissionType;
import ch.dkrieger.permissionsystem.lib.command.PermissionCommandSender;
import ch.dkrieger.permissionsystem.lib.entity.PermissionEntityProvider;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;
import ch.dkrieger.permissionsystem.lib.importation.PermissionImport;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.database.Database;
import de.dytanic.cloudnet.lib.player.OfflinePlayer;
import de.dytanic.cloudnet.lib.player.permission.GroupEntityData;
import de.dytanic.cloudnet.lib.player.permission.PermissionEntity;
import de.dytanic.cloudnet.lib.utility.document.Document;

import java.io.File;
import java.util.*;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 15.07.18 21:27
 *
 */

public class CloudNetV2PermissionImport implements PermissionImport {

    public static boolean AVAILABLE = false;

    @Override
    public String getName() {
        return "CloudNetV2";
    }
    @Override

    public boolean isAvailable() {
        return AVAILABLE;
    }
    @Override
    public boolean needFile() {
        return false;
    }

    @Override
    public void importData(PermissionCommandSender sender, File file) {
        Map<UUID,List<String>> implementation = new LinkedHashMap<>();
        for(de.dytanic.cloudnet.lib.player.permission.PermissionGroup group : CloudAPI.getInstance().getPermissionPool().getGroups().values()){
            PermissionGroup newGroup = PermissionGroupManager.getInstance().getGroup(group.getName());
            if(newGroup == null) newGroup = PermissionGroupManager.getInstance().createGroup(group.getName());
            newGroup.setPrefix(group.getPrefix());
            newGroup.setSuffix(group.getSuffix());
            newGroup.setDisplay(group.getDisplay());
            newGroup.setDefault(group.isDefaultGroup());
            newGroup.setJoinPower(group.getJoinPower());
            newGroup.setPriority(group.getTagId());
            for(Map.Entry<String,Boolean> entry : group.getPermissions().entrySet()){
                if(entry.getValue()) newGroup.addPermission(entry.getKey());
            }
            for(Map.Entry<String,List<String>> entry : group.getServerGroupPermissions().entrySet()){
                if(entry.getValue() != null && !(entry.getValue().isEmpty())){
                    for(String permission : entry.getValue())
                        newGroup.addServerGroupPermission(entry.getKey().toLowerCase(),permission.toLowerCase());
                }
            }
            if(!group.getImplementGroups().isEmpty()){
                implementation.put(newGroup.getUUID(),new LinkedList<>());
                for(String name : group.getImplementGroups()) implementation.get(newGroup.getUUID()).add(name);
            }
        }

        for(Map.Entry<UUID,List<String>> entry : implementation.entrySet()){
            PermissionGroup group = PermissionGroupManager.getInstance().getGroup(entry.getKey());
            if(group != null){
                for(String name : entry.getValue()){
                    PermissionGroup implement = PermissionGroupManager.getInstance().getGroup(name);
                    if(implement != null) group.addGroup(implement);
                }
            }
        }

        Database database = CloudAPI.getInstance().getDatabaseManager().getDatabase("cloudnet_internal_players");
        if(database != null){
            database.loadDocuments();
            for(Document data : database.getDocs()){
                try{
                    OfflinePlayer player = data.getObject("offlinePlayer",OfflinePlayer.TYPE);
                    PermissionEntity entity = player.getPermissionEntity();
                    PermissionPlayer newplayer = PermissionPlayerManager.getInstance().getPermissionPlayer(player.getUniqueId());
                    if(newplayer == null) newplayer = PermissionPlayerManager.getInstance().createPermissionPlayer(player.getUniqueId(),player.getName());
                    for(Map.Entry<String,Boolean> entry : entity.getPermissions().entrySet()){
                        if(entry.getValue()) newplayer.addPermission(entry.getKey());
                    }
                    for(GroupEntityData entitydata : entity.getGroups()){
                        PermissionGroup group = PermissionGroupManager.getInstance().getGroup(entitydata.getGroup());
                        if(group != null) PermissionEntityProvider.getInstance().getStorage().addEntity(PermissionType.PLAYER
                                ,player.getUniqueId(),group.getUUID(),(entitydata.getTimeout() > 0 ? entitydata.getTimeout() : -1L));
                    }
                }catch (Exception ignored){}
            }
        }
        PermissionSystem.getInstance().sync();
    }
}

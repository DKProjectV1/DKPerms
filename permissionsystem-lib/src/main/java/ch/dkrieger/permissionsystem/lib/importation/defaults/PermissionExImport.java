package ch.dkrieger.permissionsystem.lib.importation.defaults;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 15.07.18 21:33
 *
 */

import ch.dkrieger.permissionsystem.lib.PermissionSystem;
import ch.dkrieger.permissionsystem.lib.command.PermissionCommandSender;
import ch.dkrieger.permissionsystem.lib.config.SimpleConfig;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;
import ch.dkrieger.permissionsystem.lib.importation.PermissionImport;
import ch.dkrieger.permissionsystem.lib.platform.DKPermsPlatform;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import ch.dkrieger.permissionsystem.lib.utils.NetworkUtil;

import java.io.File;
import java.util.*;

public class PermissionExImport implements PermissionImport{

    @Override
    public String getName() {
        return "PermissionEx";
    }
    @Override
    public Boolean isAvailable() {
        return true;
    }
    @Override
    public Boolean needFile() {
        return true;
    }
    @Override
    public void importData(PermissionCommandSender sender, File file) {
        SimpleConfig config = new PermissionExConfig(file);
        config.load();
        Map<UUID,List<String>> implementation = new LinkedHashMap<>();
        for(String name : config.getKeys("groups")){
            if(!name.contains(".")){
                try{
                    PermissionGroup group = PermissionGroupManager.getInstance().getGroup(name);
                    if(group == null) group = PermissionGroupManager.getInstance().createGroup(name);
                    if(config.contains("groups."+name+".default")) group.setDefault(config.getBooleanValue("groups."+name+".default"));
                    if(config.contains("groups."+name+".prefix")) group.setPrefix(config.getStringValue("groups."+name+".prefix"));
                    if(config.contains("groups."+name+".suffix")) group.setSuffix(config.getStringValue("groups."+name+".suffix"));
                    if(config.contains("groups."+name+".rank")) group.setPriority(config.getIntValue("groups."+name+".rank"));
                    if(config.contains("groups."+name+".options.default")) group.setDefault(config.getBooleanValue("groups."+name+".options.default"));
                    if(config.contains("groups."+name+".options.prefix")) group.setPrefix(config.getStringValue("groups."+name+".options.prefix"));
                    if(config.contains("groups."+name+".options.suffix")) group.setSuffix(config.getStringValue("groups."+name+".options.suffix"));
                    if(config.contains("groups."+name+".options.rank")) group.setPriority(config.getIntValue("groups."+name+".options.rank"));
                    if(config.contains("groups."+name+".options.permissions")){
                        for(String permission : config.getStringListValue("groups."+name+".permissions")) group.addPermission(permission);
                    }
                    if(config.contains("groups."+name+".permissions")){
                        for(String permission : config.getStringListValue("groups."+name+".permissions")) group.addPermission(permission);
                    }
                    if(config.contains("groups."+name+".worlds")){
                        for(String world : config.getKeys("groups."+name+".worlds")){
                            if(!world.contains(".")){
                                if(config.contains("groups."+name+".worlds."+world+".permissions")){
                                    for(String permission : config.getStringListValue("groups."+name+".worlds."+world+".permissions"))
                                        group.addPermission(permission,world);
                                }
                            }
                        }
                    }
                    if(config.contains("groups."+name+".inheritance")){
                        implementation.put(group.getUUID(),new LinkedList<>());
                        for(String impl : config.getStringListValue("groups."+name+".inheritance"))
                            implementation.get(group.getUUID()).add(impl);
                    }
                }catch (Exception exception){}
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
        for(String uuid : config.getKeys("users")) {
            try{
                UUID uuid0 = UUID.fromString(uuid);
                PermissionPlayer player = PermissionPlayerManager.getInstance().getPermissionPlayer(uuid0);
                String name = config.getStringValue("users."+uuid+".options.name");
                if(name == null || name.equalsIgnoreCase("")) name = NetworkUtil.getRandomString(14);
                if(player == null) player = PermissionPlayerManager.getInstance().createPermissionPlayer(uuid0,name);
                if(config.contains("users."+uuid+".group")){
                    for(String group : config.getStringListValue("users."+uuid+".group")){
                        PermissionGroup group0 = PermissionGroupManager.getInstance().getGroup(group);
                        if(group0 != null) player.addGroup(group0);
                    }
                }
                if(config.contains("users."+uuid+".permissions")){
                    for(String permission : config.getStringListValue("users."+uuid+".permissions")){
                        player.addPermission(permission);
                    }
                }
                if(config.contains("users."+uuid+".worlds")){
                    for(String world : config.getKeys("users."+uuid+".worlds")) {
                        if(!world.contains(".")) {
                            if(config.contains("users."+uuid+".worlds."+world+".permissions")){
                                for(String permission : config.getStringListValue("users."+uuid+".worlds."+world+".permissions"))
                                    player.addPermission(permission,world);
                            }
                        }
                    }
                }
            }catch (Exception exception){}
        }
    }
    private class PermissionExConfig extends SimpleConfig {
        public PermissionExConfig(File file) {
            super(PermissionSystem.getInstance().getPlatform(),file);
        }
        @Override
        public void onLoad() {}
        @Override
        public void registerDefaults() {}
    }
}

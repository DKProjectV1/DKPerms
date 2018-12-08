package ch.dkrieger.permissionsystem.lib.storage.yaml;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 10.07.18 13:14
 *
 */

import ch.dkrieger.permissionsystem.lib.PermissionSystem;
import ch.dkrieger.permissionsystem.lib.config.Config;
import ch.dkrieger.permissionsystem.lib.config.SimpleConfig;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupStorage;
import ch.dkrieger.permissionsystem.lib.player.PlayerDesign;

import java.io.File;
import java.util.*;

public class YamlPermissionGroupStorage extends SimpleConfig implements PermissionGroupStorage{

    private SimpleConfig players;

    public YamlPermissionGroupStorage(SimpleConfig players) {
        super(PermissionSystem.getInstance().getPlatform(),new File(Config.STORAGE_FOLDER,"groups.yml"));
        this.players = players;
        loadConfig();
    }
    @Override
    public void onLoad() {}
    @Override
    public void registerDefaults() {}
    @Override
    public List<PermissionGroup> loadGroups() {
        List<PermissionGroup> groups = new LinkedList<>();
        Collection<String> keys = getKeys("groups");
        if(keys != null){
            for(String uuid : keys){
                try{
                    groups.add(new PermissionGroup(getStringValue("groups."+uuid+".name"),UUID.fromString(uuid)
                            ,getStringValue("groups."+uuid+".description"),getBooleanValue("groups."+uuid+".default")
                            ,getBooleanValue("groups."+uuid+".team"),getIntValue("groups."+uuid+".priority")
                            ,getIntValue("groups."+uuid+".tsgroupid"),getIntValue("groups."+uuid+".joinpower")
                            ,new PlayerDesign(getStringValue("groups."+uuid+".prefix"),getStringValue("groups."+uuid+".suffix"),
                            getStringValue("groups."+uuid+".display"),getStringValue("groups."+uuid+".color"))));
                }catch (Exception exception){}
            }
        }
        return groups;
    }
    @Override
    public PermissionGroup createGroup(String name) {
        UUID uuid = UUID.randomUUID();
        while(contains("groups."+uuid+".name")) uuid = UUID.randomUUID();
        setValue("groups."+uuid+".name",name);
        setValue("groups."+uuid+".description","New PermissionGroup");
        setValue("groups."+uuid+".default",false);
        setValue("groups."+uuid+".team",false);
        setValue("groups."+uuid+".priority",0);
        setValue("groups."+uuid+".tsgroupid",-1);
        setValue("groups."+uuid+".joinpower",0);
        setValue("groups."+uuid+".prefix","-1");
        setValue("groups."+uuid+".suffix","-1");
        setValue("groups."+uuid+".display","-1");
        setValue("groups."+uuid+".color","-1");
        save();
        return new PermissionGroup(name,uuid,"New PermissionGroup",false,false,-1,-1
                ,0,new PlayerDesign("-1","-1","-1","-1"));
    }
    @Override
    public void deleteGroup(UUID uuid) {
        setValue("groups."+uuid,null);
        save();
    }
    @Override
    public void setSetting(UUID uuid, String identifier, Object value) {
        setValue("groups."+uuid+"."+identifier,value);
        save();
    }
    @Override
    public List<UUID> getPlayers(PermissionGroup group) {
        List<UUID> list = new LinkedList<>();
        for(String uuid : this.players.getKeys("players")){
            try{
                List<String> groups = this.players.getStringListValue("players."+uuid+".groups");
                if(groups != null){
                    for(String entity : groups){
                        if(entity.split(";")[0].equalsIgnoreCase(""+group.getUUID())) list.add(UUID.fromString(uuid));
                    }
                }
            }catch (Exception exception){}
        }
        return list;
    }
}

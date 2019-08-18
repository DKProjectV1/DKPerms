package ch.dkrieger.permissionsystem.lib.config;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 10.06.18 13:30
 *
 */

import ch.dkrieger.permissionsystem.lib.PermissionSystem;
import ch.dkrieger.permissionsystem.lib.platform.DKPermsPlatform;
import ch.dkrieger.permissionsystem.lib.storage.StorageType;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class Config extends SimpleConfig {

    public static Boolean ADVANCED;

    public static StorageType STORAGE_TYPE;
    public static File STORAGE_FOLDER;

    public static String STORAGE_MYSQL_HOST;
    public static String STORAGE_MYSQL_PORT;
    public static String STORAGE_MYSQL_USER;
    public static String STORAGE_MYSQL_PASSWORD;
    public static String STORAGE_MYSQL_DATABASE;
    public static int STORAGE_MYSQL_MAX_CONNECTIONS;
    public static boolean STORAGE_MYSQL_SSL;

    public static String STORAGE_MONGODB_HOST;
    public static int STORAGE_MONGODB_PORT;
    public static String STORAGE_MONGODB_USER;
    public static String STORAGE_MONGODB_PASSWORD;
    public static String STORAGE_MONGODB_DATABASE;

    public static Boolean SECURITY_OPERATOR_ENABLED;
    public static Boolean SECURITY_DISABLECOMMANDS;

    public static Boolean SYNCHRONISE_TASK_ENABLED;
    public static Long SYNCHRONISE_TASK_DELAY;
    public static Boolean SYNCHRONISE_GROUP_ONFIRSTPLAYERJOIN;
    public static Boolean SYNCHRONISE_GROUP_ONLASTPLAYERLEAVE;
    public static Boolean SYNCHRONISE_CHANNEL;

    public static char SERVERGROUP_SPLIT;

    public static boolean PLAYER_DISPLAYNAME_ENABLED;
    public static String PLAYER_DISPLAYNAME_FORMAT;

    public static Boolean DEBUG_ENABLED;
    public static PermissionSystem.PermissionDebugLevel DEBUG_LEVEL;

    public static boolean COMMAND_PERMISSION_ENABLED;
    public static String COMMAND_PERMISSION_PERMISSION;
    public static String COMMAND_PERMISSION_NAME;
    public static List<String> COMMAND_PERMISSION_ALIASES;

    public static boolean COMMAND_RANK_ENABLED;
    public static String COMMAND_RANK_PERMISSION;
    public static String COMMAND_RANK_NAME;
    public static List<String> COMMAND_RANK_ALIASES;

    public static boolean COMMAND_TEAM_ENABLED;
    public static String COMMAND_TEAM_PERMISSION;
    public static String COMMAND_TEAM_NAME;
    public static List<String> COMMAND_TEAM_ALIASES;

    private boolean advanced;

    public Config(DKPermsPlatform platform,Boolean advanced) {
        super(platform,new File(platform.getFolder(),"config.yml"));
        this.advanced = advanced;
    }
    @Override
    public void onLoad() {
        try{
            STORAGE_TYPE = StorageType.valueOf(getStringValue("storage.type").toUpperCase());
        }catch (Exception exception){
            STORAGE_TYPE = StorageType.YAML;
        }
        STORAGE_FOLDER = new File(getStringValue("storage.folder"));
        STORAGE_MYSQL_HOST = getStringValue("storage.mysql.host");
        STORAGE_MYSQL_PORT = getStringValue("storage.mysql.port");
        STORAGE_MYSQL_USER = getStringValue("storage.mysql.user");
        STORAGE_MYSQL_PASSWORD = getStringValue("storage.mysql.password");
        STORAGE_MYSQL_DATABASE = getStringValue("storage.mysql.database");
        STORAGE_MYSQL_MAX_CONNECTIONS= getIntValue("storage.mysql.maxConnections");
        STORAGE_MYSQL_SSL = getBooleanValue("storage.mysql.ssl");
        
        SECURITY_OPERATOR_ENABLED = getBooleanValue("security.operator.enabled");
        SECURITY_DISABLECOMMANDS = getBooleanValue("security.disablecommands");

        SYNCHRONISE_TASK_ENABLED = getBooleanValue("synchronise.task.enabled");
        SYNCHRONISE_TASK_DELAY = getLongValue("synchronise.task.delay");
        SYNCHRONISE_GROUP_ONFIRSTPLAYERJOIN = getBooleanValue("synchronise.groups.onfirstplayerjoin");
        SYNCHRONISE_GROUP_ONLASTPLAYERLEAVE = getBooleanValue("synchronise.groups.onlastplayerleave");
        SYNCHRONISE_CHANNEL = getBooleanValue("synchronise.channel");

        SERVERGROUP_SPLIT = getStringValue("servergroup.split").charAt(0);

        ADVANCED = getBooleanValue("advanced");

        DEBUG_ENABLED = getBooleanValue("debug.enabled");
        try{
            DEBUG_LEVEL = PermissionSystem.PermissionDebugLevel.NORMAL.valueOf(getStringValue("debug.level"));
        }catch (Exception exception){
            DEBUG_LEVEL = PermissionSystem.PermissionDebugLevel.NORMAL;
        }
        PLAYER_DISPLAYNAME_ENABLED = getBooleanValue("player.displayname.enabled");
        PLAYER_DISPLAYNAME_FORMAT = getStringValue("player.displayname.format");

        COMMAND_PERMISSION_ENABLED = getBooleanValue("commands.permission.enabled");
        COMMAND_PERMISSION_NAME = getStringValue("commands.permission.name");
        COMMAND_PERMISSION_PERMISSION = getStringValue("commands.permission.permission");
        COMMAND_PERMISSION_ALIASES = getStringListValue("commands.permission.aliases");

        COMMAND_RANK_ENABLED = getBooleanValue("commands.rank.enabled");
        COMMAND_RANK_NAME = getStringValue("commands.rank.name");
        COMMAND_RANK_PERMISSION = getStringValue("commands.rank.permission");
        COMMAND_RANK_ALIASES = getStringListValue("commands.rank.aliases");

        COMMAND_TEAM_ENABLED = getBooleanValue("commands.team.enabled");
        COMMAND_TEAM_NAME = getStringValue("commands.team.name");
        COMMAND_TEAM_PERMISSION = getStringValue("commands.team.permission");
        COMMAND_TEAM_ALIASES = getStringListValue("commands.team.aliases");
    }

    @Override
    public void registerDefaults() {
        addValue("advanced",this.advanced);
        addValue("storage.type","YAML");
        addValue("storage.folder",getPlatform().getFolder()+"/datas/");
        addValue("storage.mysql.host","localhost");
        addValue("storage.mysql.port","3306");
        addValue("storage.mysql.user","root");
        addValue("storage.mysql.password","password");
        addValue("storage.mysql.database","PermissionSystem");
        addValue("storage.mysql.maxConnections",10);
        addValue("storage.mysql.ssl",false);

        addValue("security.operator.enabled",false);
        addValue("security.disablecommands",false);

        addValue("synchronise.task.enabled",false);
        addValue("synchronise.task.delay",10);
        addValue("synchronise.groups.onfirstplayerjoin",true);
        addValue("synchronise.groups.onlastplayerleave",false);
        addValue("synchronise.channel",true);

        addValue("servergroup.split","-");

        addValue("debug.enabled",true);
        addValue("debug.level","normal");

        addValue("player.displayname.enabled",true);
        addValue("player.displayname.format","[color][player]");

        addValue("commands.permission.enabled",true);
        addValue("commands.permission.permission","dkperms.admin");
        addValue("commands.permission.name","permission");
        addValue("commands.permission.aliases",Collections.singletonList("perms"));

        addValue("commands.rank.enabled",true);
        addValue("commands.rank.permission","dkperms.rank");
        addValue("commands.rank.name","rank");
        addValue("commands.rank.aliases",Collections.singletonList("ranks"));

        addValue("commands.team.enabled",true);
        addValue("commands.team.permission","dkperms.team");
        addValue("commands.team.name","team");
        addValue("commands.team.aliases",Collections.singletonList("teammembers"));
    }
}

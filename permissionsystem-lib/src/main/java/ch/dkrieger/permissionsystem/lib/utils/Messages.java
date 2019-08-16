package ch.dkrieger.permissionsystem.lib.utils;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 10.06.18 13:17
 *
 */

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Messages {

    public Messages(String systemname) {
        SYSTEM_NAME = systemname;
        SYSTEM_PREFIX = "["+systemname+"] ";
    }
    public static String SYSTEM_NAME;
    public static String SYSTEM_PREFIX;

    public static String PREFIX;
    public static String ERROR;
    public static String NOPERMISSIONS;

    public static String UNIT_SECOND_SINGULAR;
    public static String UNIT_SECOND_PLURAL;
    public static String UNIT_MINUTE_SINGULAR;
    public static String UNIT_MINUTE_PLURAL;
    public static String UNIT_HOUR_SINGULAR;
    public static String UNIT_HOUR_PLURAL;
    public static String UNIT_DAY_SINGULAR;
    public static String UNIT_DAY_PLURAL;
    public static String UNIT_WEEK_SINGULAR;
    public static String UNIT_WEEK_PLURAL;
    public static String UNIT_MONTH_SINGULAR;
    public static String UNIT_MONTH_PLURAL;
    public static String UNIT_YEAR_SINGULAR;
    public static String UNIT_YEAR_PLURAL;

    public static String PERMISSION_SYNCHRONISEING;
    public static String PERMISSION_SYNCHRONISEED;

    public static String PERMISSION_GROUP_INFO_FORMAT;
    public static String PERMISSION_GROUP_INFO_NAME;
    public static String PERMISSION_GROUP_INFO_DESCRIPTION;
    public static String PERMISSION_GROUP_INFO_UUID;
    public static String PERMISSION_GROUP_INFO_DEFAULT;
    public static String PERMISSION_GROUP_INFO_TEAM;
    public static String PERMISSION_GROUP_INFO_PRIORITY;
    public static String PERMISSION_GROUP_INFO_PREFIX;
    public static String PERMISSION_GROUP_INFO_SUFFIX;
    public static String PERMISSION_GROUP_INFO_DISPLAY;
    public static String PERMISSION_GROUP_INFO_COLOR;
    public static String PERMISSION_GROUP_INFO_TSGROUPID;
    public static String PERMISSION_GROUP_INFO_JOINPOWER;

    public static String PERMISSION_GROUP_PERMISSION_ADD_GLOBAL;
    public static String PERMISSION_GROUP_PERMISSION_ADD_SERVER;
    public static String PERMISSION_GROUP_PERMISSION_ADD_GROUP;

    public static String PERMISSION_GROUP_PERMISSION_REMOVE_GLOBAL;
    public static String PERMISSION_GROUP_PERMISSION_REMOVE_SERVER;
    public static String PERMISSION_GROUP_PERMISSION_REMOVE_GROUP;

    public static String PERMISSION_GROUP_PERMISSION_CLEAR_ALL;
    public static String PERMISSION_GROUP_PERMISSION_CLEAR_GLOBAL_ALL;
    public static String PERMISSION_GROUP_PERMISSION_CLEAR_GLOBAL_WORLD;
    public static String PERMISSION_GROUP_PERMISSION_CLEAR_SERVER_ALL;
    public static String PERMISSION_GROUP_PERMISSION_CLEAR_SERVER_SERVER;
    public static String PERMISSION_GROUP_PERMISSION_CLEAR_SERVER_WORLD;
    public static String PERMISSION_GROUP_PERMISSION_CLEAR_GROUP_ALL;
    public static String PERMISSION_GROUP_PERMISSION_CLEAR_GROUP_GROUP;
    public static String PERMISSION_GROUP_PERMISSION_CLEAR_GROUP_WORLD;

    public static String PERMISSION_GROUP_IMPLEMENTATION_LIST_HEADER;
    public static String PERMISSION_GROUP_IMPLEMENTATION_LIST_FORMAT;

    public static String PERMISSION_GROUP_IMPLEMENTATION_SET;
    public static String PERMISSION_GROUP_IMPLEMENTATION_ADD;
    public static String PERMISSION_GROUP_IMPLEMENTATION_REMOVE;
    public static String PERMISSION_GROUP_IMPLEMENTATION_CLEAR;

    public static String PERMISSION_GROUP_LIST_HEADER;
    public static String PERMISSION_GROUP_LIST_FORMAT;
    public static String PERMISSION_GROUP_LIST_HOVER;

    public static String PERMISSION_GROUP_NOTFOUND;
    public static String PERMISSION_GROUP_ALREADYEXISTS;
    public static String PERMISSION_GROUP_CREATED;
    public static String PERMISSION_GROUP_DELETED;
    public static String PERMISSION_GROUP_REANMED;
    public static String PERMISSION_GROUP_SETTINGSCHANGED;
    public static String PERMISSION_GROUP_COPY_START;
    public static String PERMISSION_GROUP_COPY_FINISH;
    public static String PERMISSION_GROUP_PLAYERLIST_HEADER;
    public static String PERMISSION_GROUP_PLAYERLIST_FORMAT;

    public static String PERMISSION_PLAYER_NOTFOUND;

    public static String PERMISSION_PLAYER_INFO_FORMAT;
    public static String PERMISSION_PLAYER_INFO_ID;
    public static String PERMISSION_PLAYER_INFO_NAME;
    public static String PERMISSION_PLAYER_INFO_UUID;

    public static String PERMISSION_PLAYER_PERMISSION_ADD_GLOBAL;
    public static String PERMISSION_PLAYER_PERMISSION_ADD_SERVER;
    public static String PERMISSION_PLAYER_PERMISSION_ADD_GROUP;

    public static String PERMISSION_PLAYER_PERMISSION_REMOVE_GLOBAL;
    public static String PERMISSION_PLAYER_PERMISSION_REMOVE_SERVER;
    public static String PERMISSION_PLAYER_PERMISSION_REMOVE_GROUP;

    public static String PERMISSION_PLAYER_PERMISSION_CLEAR_ALL;
    public static String PERMISSION_PLAYER_PERMISSION_CLEAR_GLOBAL_ALL;
    public static String PERMISSION_PLAYER_PERMISSION_CLEAR_GLOBAL_WORLD;
    public static String PERMISSION_PLAYER_PERMISSION_CLEAR_SERVER_ALL;
    public static String PERMISSION_PLAYER_PERMISSION_CLEAR_SERVER_SERVER;
    public static String PERMISSION_PLAYER_PERMISSION_CLEAR_SERVER_WORLD;
    public static String PERMISSION_PLAYER_PERMISSION_CLEAR_GROUP_ALL;
    public static String PERMISSION_PLAYER_PERMISSION_CLEAR_GROUP_GROUP;
    public static String PERMISSION_PLAYER_PERMISSION_CLEAR_GROUP_WORLD;

    public static String PERMISSION_PLAYER_GROUP_LIST_HEADER;
    public static String PERMISSION_PLAYER_GROUP_LIST_FORMAT;
    public static String PERMISSION_PLAYER_GROUP_SET;
    public static String PERMISSION_PLAYER_GROUP_ADD;
    public static String PERMISSION_PLAYER_GROUP_REMOVE;
    public static String PERMISSION_PLAYER_GROUP_CLEAR;

    public static String PERMISSION_PLAYER_PERMISSION_CHECK_HAS;
    public static String PERMISSION_PLAYER_PERMISSION_CHECK_HASNOT;

    public static String PERMISSION_LIST_GLOBAL_HEADER;
    public static String PERMISSION_LIST_GLOBAL_FORMAT;
    public static String PERMISSION_LIST_WORLD_HEADER;
    public static String PERMISSION_LIST_WORLD_WORLD;
    public static String PERMISSION_LIST_WORLD_FORMAT;

    public static String PERMISSION_LIST_SERVER_HEADER;
    public static String PERMISSION_LIST_SERVER_SERVER;
    public static String PERMISSION_LIST_SERVER_FORMAT;
    public static String PERMISSION_LIST_SERVER_WORLD_HEADER;
    public static String PERMISSION_LIST_SERVER_WORLD_WORLD;
    public static String PERMISSION_LIST_SERVER_WORLD_FORMAT;

    public static String PERMISSION_LIST_SERVERGROUP_HEADER;
    public static String PERMISSION_LIST_SERVERGROUP_SERVERGROUP;
    public static String PERMISSION_LIST_SERVERGROUP_FORMAT;
    public static String PERMISSION_LIST_SERVERGROUP_WORLD_HEADER;
    public static String PERMISSION_LIST_SERVERGROUP_WORLD_WORLD;
    public static String PERMISSION_LIST_SERVERGROUP_WORLD_FORMAT;

    public static String PERMISSION_IMPORT_LIST_HEADER;
    public static String PERMISSION_IMPORT_LIST;
    public static String PERMISSION_IMPORT_NOTFOUND;
    public static String PERMISSION_IMPORT_REQUIREDFILE;
    public static String PERMISSION_IMPORT_FILENOTFOUND;
    public static String PERMISSION_IMPORT_START;
    public static String PERMISSION_IMPORT_FINISH;

    public static List<String> PERMISSION_HELP_MAIN;
    public static List<String> PERMISSION_HELP_GROUP_ABOVE;
    public static List<String> PERMISSION_HELP_GROUP_BELOW;
    public static List<String> PERMISSION_HELP_PLAYER_ABOVE;
    public static List<String> PERMISSION_HELP_PLAYER_BELOW;
    public static List<String> PERMISSION_HELP_PERMISSION;

    public static String RANK_LIST_MY;
    public static String RANK_LIST_OTHER;
    public static String RANK_LIST_FORMAT;
    public static String RANK_NOPERMISSION_FOR_RANK;
    public static String RANK_NOPERMISSION_FOR_PLAYER;
    public static String RANK_NOFOUND;
    public static String RANK_SEPARATOR;
    public static List<String> RANK_HELP;

    public static String TEAM_LIST_HEADER;
    public static String TEAM_LIST_BETWEEN;
    public static String TEAM_LIST_FOOTER;
    public static String TEAM_LIST_FORMAT_GROUP;
    public static String TEAM_LIST_FORMAT_PLAYER;

    public static String getMessage(Long duration, TimeUnit unit){
        if(unit == TimeUnit.SECONDS){
            if(duration == 1) return UNIT_SECOND_SINGULAR;
            else return UNIT_SECOND_PLURAL;
        }else if(unit == TimeUnit.MINUTES){
            if(duration == 1) return UNIT_MINUTE_SINGULAR;
            else return UNIT_MINUTE_PLURAL;
        }else if(unit == TimeUnit.HOURS){
            if(duration == 1) return UNIT_HOUR_SINGULAR;
            else return UNIT_HOUR_PLURAL;
        }else if(unit == TimeUnit.DAYS){
            if(duration == 1) return UNIT_DAY_SINGULAR;
            else return UNIT_DAY_PLURAL;
        }
        return "?";
    }
}

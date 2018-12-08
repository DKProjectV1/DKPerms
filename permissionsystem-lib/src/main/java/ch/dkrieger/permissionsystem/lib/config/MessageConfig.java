package ch.dkrieger.permissionsystem.lib.config;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 24.06.18 15:38
 *
 */

import ch.dkrieger.permissionsystem.lib.platform.DKPermsPlatform;
import ch.dkrieger.permissionsystem.lib.utils.Messages;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class MessageConfig extends SimpleConfig{

    public MessageConfig(DKPermsPlatform platform) {
        super(platform,new File(platform.getFolder(),"messages.yml"));
    }
    @Override
    public void onLoad() {
        Messages.PREFIX = get("prefix");
        Messages.ERROR = get("error");
        Messages.NOPERMISSIONS = get("nopermissions");

        Messages.UNIT_SECOND_SINGULAR = get("unit.second.singular");
        Messages.UNIT_SECOND_PLURAL = get("unit.second.plural");
        Messages.UNIT_MINUTE_SINGULAR = get("unit.minute.singular");
        Messages.UNIT_MINUTE_PLURAL = get("unit.minute.plural");
        Messages.UNIT_HOUR_SINGULAR = get("unit.hour.singular");
        Messages.UNIT_HOUR_PLURAL = get("unit.hour.plural");
        Messages.UNIT_DAY_SINGULAR = get("unit.day.singular");
        Messages.UNIT_DAY_PLURAL = get("unit.day.plural");
        Messages.UNIT_WEEK_SINGULAR = get("unit.week.singular");
        Messages.UNIT_WEEK_PLURAL = get("unit.week.plural");
        Messages.UNIT_MONTH_SINGULAR = get("unit.month.singular");
        Messages.UNIT_MONTH_PLURAL = get("unit.month.plural");
        Messages.UNIT_YEAR_SINGULAR = get("unit.year.singular");
        Messages.UNIT_YEAR_PLURAL = get("unit.year.plural");

        Messages.PERMISSION_SYNCHRONISEING = get("synchronising.starting");
        Messages.PERMISSION_SYNCHRONISEED = get("synchronising.finished");

        Messages.PERMISSION_GROUP_NOTFOUND = get("group.notfound");
        Messages.PERMISSION_GROUP_ALREADYEXISTS = get("group.alreadyexists");
        Messages.PERMISSION_GROUP_CREATED = get("group.created");
        Messages.PERMISSION_GROUP_DELETED = get("group.deleted");
        Messages.PERMISSION_GROUP_REANMED = get("group.renamed");
        Messages.PERMISSION_GROUP_SETTINGSCHANGED= get("group.settingschanged");
        Messages.PERMISSION_GROUP_COPY_START = get("group.copy.start");
        Messages.PERMISSION_GROUP_COPY_FINISH = get("group.copy.finidh");
        Messages.PERMISSION_GROUP_PLAYERLIST_HEADER = get("group.playerlist.header");
        Messages.PERMISSION_GROUP_PLAYERLIST_FORMAT = get("group.playerlist.format");

        Messages.PERMISSION_GROUP_LIST_HEADER = get("group.list.header");
        Messages.PERMISSION_GROUP_LIST_FORMAT = get("group.list.format");
        Messages.PERMISSION_GROUP_LIST_HOVER = get("group.list.hover");

        Messages.PERMISSION_GROUP_INFO_FORMAT = get("group.info.format");
        Messages.PERMISSION_GROUP_INFO_NAME = get("group.info.name");
        Messages.PERMISSION_GROUP_INFO_DESCRIPTION = get("group.info.description");
        Messages.PERMISSION_GROUP_INFO_UUID = get("group.info.uuid");
        Messages.PERMISSION_GROUP_INFO_DEFAULT= get("group.info.defaults");
        Messages.PERMISSION_GROUP_INFO_TEAM = get("group.info.team");
        Messages.PERMISSION_GROUP_INFO_PRIORITY = get("group.info.priority");
        Messages.PERMISSION_GROUP_INFO_PREFIX = get("group.info.prefix");
        Messages.PERMISSION_GROUP_INFO_SUFFIX = get("group.info.suffix");
        Messages.PERMISSION_GROUP_INFO_DISPLAY = get("group.info.display");
        Messages.PERMISSION_GROUP_INFO_COLOR = get("group.info.color");
        Messages.PERMISSION_GROUP_INFO_TSGROUPID = get("group.info.tsgroupid");
        Messages.PERMISSION_GROUP_INFO_JOINPOWER = get("group.info.joinpower");

        Messages.PERMISSION_GROUP_PERMISSION_ADD_GLOBAL = get("group.permission.add.global");
        Messages.PERMISSION_GROUP_PERMISSION_ADD_SERVER = get("group.permission.add.server");
        Messages.PERMISSION_GROUP_PERMISSION_ADD_GROUP = get("group.permission.add.group");

        Messages.PERMISSION_GROUP_PERMISSION_REMOVE_GLOBAL = get("group.permission.remove.global");
        Messages.PERMISSION_GROUP_PERMISSION_REMOVE_SERVER = get("group.permission.remove.server");
        Messages.PERMISSION_GROUP_PERMISSION_REMOVE_GROUP = get("group.permission.remove.group");

        Messages.PERMISSION_GROUP_PERMISSION_CLEAR_ALL = get("group.permission.clear.all");
        Messages.PERMISSION_GROUP_PERMISSION_CLEAR_GLOBAL_ALL = get("group.permission.clear.global.all");
        Messages.PERMISSION_GROUP_PERMISSION_CLEAR_GLOBAL_WORLD= get("group.permission.clear.global.world");
        Messages.PERMISSION_GROUP_PERMISSION_CLEAR_SERVER_ALL = get("group.permission.clear.server.all");
        Messages.PERMISSION_GROUP_PERMISSION_CLEAR_SERVER_SERVER = get("group.permission.clear.server.server");
        Messages.PERMISSION_GROUP_PERMISSION_CLEAR_SERVER_WORLD = get("group.permission.clear.server.world");
        Messages.PERMISSION_GROUP_PERMISSION_CLEAR_GROUP_ALL = get("group.permission.clear.group.all");
        Messages.PERMISSION_GROUP_PERMISSION_CLEAR_GROUP_GROUP = get("group.permission.clear.group.group");
        Messages.PERMISSION_GROUP_PERMISSION_CLEAR_GROUP_WORLD = get("group.permission.clear.group.world");

        Messages.PERMISSION_GROUP_IMPLEMENTATION_LIST_HEADER = get("group.implementation.list.header");
        Messages.PERMISSION_GROUP_IMPLEMENTATION_LIST_FORMAT= get("group.implementation.list.format");
        Messages.PERMISSION_GROUP_IMPLEMENTATION_SET = get("group.implementation.set");
        Messages.PERMISSION_GROUP_IMPLEMENTATION_ADD  = get("group.implementation.add");
        Messages.PERMISSION_GROUP_IMPLEMENTATION_REMOVE  = get("group.implementation.remove");
        Messages.PERMISSION_GROUP_IMPLEMENTATION_CLEAR  = get("group.implementation.clear");

        Messages.PERMISSION_PLAYER_NOTFOUND = get("player.notfound");
        Messages.PERMISSION_PLAYER_INFO_FORMAT = get("player.info.format");
        Messages.PERMISSION_PLAYER_INFO_ID = get("player.info.id");
        Messages.PERMISSION_PLAYER_INFO_NAME= get("player.info.name");
        Messages.PERMISSION_PLAYER_INFO_UUID = get("player.info.uuid");

        Messages.PERMISSION_PLAYER_PERMISSION_ADD_GLOBAL = get("player.permission.add.global");
        Messages.PERMISSION_PLAYER_PERMISSION_ADD_SERVER = get("player.permission.add.server");
        Messages.PERMISSION_PLAYER_PERMISSION_ADD_GROUP = get("player.permission.add.group");

        Messages.PERMISSION_PLAYER_PERMISSION_REMOVE_GLOBAL = get("player.permission.remove.global");
        Messages.PERMISSION_PLAYER_PERMISSION_REMOVE_SERVER = get("player.permission.remove.server");
        Messages.PERMISSION_PLAYER_PERMISSION_REMOVE_GROUP = get("player.permission.remove.group");

        Messages.PERMISSION_PLAYER_PERMISSION_CLEAR_ALL = get("player.permission.clear.all");
        Messages.PERMISSION_PLAYER_PERMISSION_CLEAR_GLOBAL_ALL = get("player.permission.clear.global.all");
        Messages.PERMISSION_PLAYER_PERMISSION_CLEAR_GLOBAL_WORLD = get("player.permission.clear.global.world");
        Messages.PERMISSION_PLAYER_PERMISSION_CLEAR_SERVER_ALL= get("player.permission.clear.server.all");
        Messages.PERMISSION_PLAYER_PERMISSION_CLEAR_SERVER_SERVER = get("player.permission.clear.server.server");
        Messages.PERMISSION_PLAYER_PERMISSION_CLEAR_SERVER_WORLD = get("player.permission.clear.server.world");
        Messages.PERMISSION_PLAYER_PERMISSION_CLEAR_GROUP_ALL = get("player.permission.clear.group.all");
        Messages.PERMISSION_PLAYER_PERMISSION_CLEAR_GROUP_GROUP = get("player.permission.clear.group.group");
        Messages.PERMISSION_PLAYER_PERMISSION_CLEAR_GROUP_WORLD = get("player.permission.clear.group.world");

        Messages.PERMISSION_PLAYER_PERMISSION_CHECK_HAS =  get("player.permission.check.has");
        Messages.PERMISSION_PLAYER_PERMISSION_CHECK_HASNOT = get("player.permission.check.hasnot");

        Messages.PERMISSION_PLAYER_GROUP_LIST_HEADER = get("player.group.list.header");
        Messages.PERMISSION_PLAYER_GROUP_LIST_FORMAT = get("player.group.list.format");
        Messages.PERMISSION_PLAYER_GROUP_SET = get("player.group.set");
        Messages.PERMISSION_PLAYER_GROUP_ADD = get("player.group.add");
        Messages.PERMISSION_PLAYER_GROUP_REMOVE = get("player.group.remove");
        Messages.PERMISSION_PLAYER_GROUP_CLEAR = get("player.group.clear");

        Messages.PERMISSION_LIST_GLOBAL_HEADER = get("permission.list.global.header");
        Messages.PERMISSION_LIST_GLOBAL_FORMAT = get("permission.list.global.format");
        Messages.PERMISSION_LIST_WORLD_HEADER = get("permission.list.world.header");
        Messages.PERMISSION_LIST_WORLD_WORLD = get("permission.list.world.world");
        Messages.PERMISSION_LIST_WORLD_FORMAT = get("permission.list.world.format");

        Messages.PERMISSION_LIST_SERVER_HEADER = get("permission.list.server.header");
        Messages.PERMISSION_LIST_SERVER_SERVER = get("permission.list.server.server");
        Messages.PERMISSION_LIST_SERVER_FORMAT = get("permission.list.server.format");
        Messages.PERMISSION_LIST_SERVER_WORLD_HEADER = get("permission.list.server.world.header");
        Messages.PERMISSION_LIST_SERVER_WORLD_WORLD = get("permission.list.server.world.world");
        Messages.PERMISSION_LIST_SERVER_WORLD_FORMAT= get("permission.list.server.world.format");

        Messages.PERMISSION_LIST_SERVERGROUP_HEADER = get("permission.list.servergroup.header");
        Messages.PERMISSION_LIST_SERVERGROUP_SERVERGROUP = get("permission.list.servergroup.servergroup");
        Messages.PERMISSION_LIST_SERVERGROUP_FORMAT = get("permission.list.servergroup.format");
        Messages.PERMISSION_LIST_SERVERGROUP_WORLD_HEADER = get("permission.list.servergroup.world.header");
        Messages.PERMISSION_LIST_SERVERGROUP_WORLD_WORLD = get("permission.list.servergroup.world.world");
        Messages.PERMISSION_LIST_SERVERGROUP_WORLD_FORMAT = get("permission.list.servergroup.world.format");

        Messages.PERMISSION_IMPORT_LIST_HEADER = get("permission.import.list.header");
        Messages.PERMISSION_IMPORT_LIST = get("permission.import.list.list");
        Messages.PERMISSION_IMPORT_NOTFOUND = get("permission.import.notfound");
        Messages.PERMISSION_IMPORT_REQUIREDFILE = get("permission.import.requiredfile");
        Messages.PERMISSION_IMPORT_FILENOTFOUND = get("permission.import.filenotfound");
        Messages.PERMISSION_IMPORT_START = get("permission.import.start");
        Messages.PERMISSION_IMPORT_FINISH = get("permission.import.finish");

        Messages.PERMISSION_HELP_MAIN = getStringListValue("permission.help.main");
        Messages.PERMISSION_HELP_GROUP_ABOVE= getStringListValue("permission.help.group.above");
        Messages.PERMISSION_HELP_GROUP_BELOW = getStringListValue("permission.help.group.below");
        Messages.PERMISSION_HELP_PLAYER_ABOVE = getStringListValue("permission.help.player.above");
        Messages.PERMISSION_HELP_PLAYER_BELOW = getStringListValue("permission.help.player.below");
        Messages.PERMISSION_HELP_PERMISSION = getStringListValue("permission.help.permission");

        Messages.RANK_LIST_MY = get("rank.list.my");
        Messages.RANK_LIST_OTHER = get("rank.list.other");
        Messages.RANK_LIST_FORMAT = get("rank.list.format");
        Messages.RANK_NOPERMISSION_FOR_RANK = get("rank.nopermissions.for.rank");
        Messages.RANK_NOPERMISSION_FOR_PLAYER= get("rank.nopermissions.for.player");
        Messages.RANK_NOFOUND = get("rank.nofound");
        Messages.RANK_HELP = new LinkedList<>();
        for(String message : getStringListValue("rank.help")) Messages.RANK_HELP.add(getPlatform().translateColorCodes(message));

        Messages.TEAM_LIST_HEADER = get("team.list.header");
        Messages.TEAM_LIST_BETWEEN = get("team.list.between");
        Messages.TEAM_LIST_FOOTER = get("team.list.footer");
        Messages.TEAM_LIST_FORMAT_GROUP = get("team.list.format.group");
        Messages.TEAM_LIST_FORMAT_PLAYER = get("team.list.format.player");
    }
    @Override
    public void registerDefaults() {
        addValue("prefix", "&8» &9DKPerms &8| &f");
        addValue("error", "&cAn error occurred, please contact a network administrator");
        addValue("nopermissions", "&4You don't have permission to execute this command.");
        
        addValue("unit.second.singular","Second");
        addValue("unit.second.plural","Seconds");
        addValue("unit.minute.singular","Minute");
        addValue("unit.minute.plural","Minutes");
        addValue("unit.hour.singular","Hour");
        addValue("unit.hour.plural","Hours");
        addValue("unit.day.singular","Day");
        addValue("unit.day.plural","Days");
        addValue("unit.week.singular","Week");
        addValue("unit.week.plural","Weeks");
        addValue("unit.month.singular","Month");
        addValue("unit.month.plural","Months");
        addValue("unit.year.singular","Year");
        addValue("unit.year.plural","Years");

        addValue("synchronising.starting", "&7Synchronising all permissions...");
        addValue("synchronising.finished", "&7Synchronised all permissions.");

        addValue("group.notfound", "&cThe group &e[group] &cwas not found.");
        addValue("group.alreadyexists", "&cThe group &e[group] &calready exists.");
        addValue("group.created", "&7The group &e[group] &7was created.");
        addValue("group.deleted", "&7The group &e[group] &7was deleted.");
        addValue("group.renamed", "&7The group &e[group] &7was reanmed to &e[name]&7.");
        addValue("group.settingschanged", "&e[setting] &7was set to [value]&7.");
        addValue("group.copy.start", "&7Copy the group &e[group]...");
        addValue("group.copy.finidh", "&7Copied the group &e[group] &7to &e[copy&7.]");
        addValue("group.playerlist.header", "&7Players from group &e[group]");
        addValue("group.playerlist.format", " &8- &7[color][player]");

        addValue("group.list.header", "&7All Groups&8:");
        addValue("group.list.format", "&8» &7[color][name]");
        addValue("group.list.hover", "&8» &7Click for more informations.");
        addValue("group.info.format", "&8» &e[id]&8: &7[value]");
        addValue("group.info.name", "Name");
        addValue("group.info.description","Description");
        addValue("group.info.uuid", "UUID");
        addValue("group.info.defaults", "Default");
        addValue("group.info.team", "Team");
        addValue("group.info.priority", "Priority");
        addValue("group.info.prefix", "Prefix");
        addValue("group.info.suffix", "Suffix");
        addValue("group.info.display", "Display");
        addValue("group.info.color", "Color");
        addValue("group.info.tsgroupid", "TsGroupID");
        addValue("group.info.joinpower", "Joinpower");

        addValue("group.permission.add.global", "&7The &7permission &e[permission]&8(&e[world]&8) &7was added to the group &e[group]&7.");
        addValue("group.permission.add.server", "&7The &7permission &e[permission]&8(&e[world]&8) &7was added for the server &e[server] &7to the group &e[group]&7.");
        addValue("group.permission.add.group", "&7The &7permission &e[permission]&8(&e[world]&8) &7was added for the servergroup &e[servergroup] &7to the group &e[group]&7.");

        addValue("group.permission.remove.global", "&7The &7permission &e[permission]&8(&e[world]&8) &7was removed from the group &e[group]&7.");
        addValue("group.permission.remove.server", "&7The &7permission &e[permission]&8(&e[world]&8) &7was removed for the server &e[server] &7from the group &e[group]&7.");
        addValue("group.permission.remove.group", "&7The &7permission &e[permission]&8(&e[world]&8) &7was removed for the servergroup &e[servergroup] &7from the group &e[group]&7.");

        addValue("group.permission.clear.all", "&7All permissions in the group &e[group] &7were cleared.");
        addValue("group.permission.clear.global.all", "&7All global permissions in the group &e[group] &7were cleared.");
        addValue("group.permission.clear.global.world", "&7All global permissions for the world &e[world] &7in the group &e[group] &7were cleared.");
        addValue("group.permission.clear.server.all", "&7All server permissions in the group &e[group] &7were cleared.");
        addValue("group.permission.clear.server.server", "&7All permissions for the server &e[server] in the group &e[group] &7were cleared.");
        addValue("group.permission.clear.server.world", "&7All permissions for the world &e[world] &7on the server &e[server] &7in the group &e[group] &7were cleared.");
        addValue("group.permission.clear.group.all", "&7All servergroup permissions in the group &e[group] &7were cleared.");
        addValue("group.permission.clear.group.group", "&7All permissions for the servergroup &e[servergroup] in the group &e[group] &7were cleared.");
        addValue("group.permission.clear.group.world", "&7All permissions for the world &e[world] &7on the servergroup &e[servergroup] &7in the group &e[group] &7were cleared.");

        addValue("group.implementation.list.header", "&8» &eImplementationGroups");
        addValue("group.implementation.list.format", " &8- &7[color][name]&8@&7[timeout]");
        addValue("group.implementation.set", "&7The &7group &e[implementation] &7was set to the group &e[group]&7.");
        addValue("group.implementation.add", "&7The &7group &e[implementation] &7was added to the group &e[group]&7.");
        addValue("group.implementation.remove", "&7The &7group &e[implementation] &7was removed from the group &e[group]&7.");
        addValue("group.implementation.clear", "&7All implementation groups from the group &e[group] &7were cleared.");

        addValue("player.notfound", "&cThe Player &e[player] &cwas not found.");
        addValue("player.info.format", "&8» &e[id]&8: &7[value]");
        addValue("player.info.id", "ID");
        addValue("player.info.name", "Name");
        addValue("player.info.uuid", "UUID");

        addValue("player.permission.add.global", "&7The &7permission &e[permission]&8(&e[world]&8) &7was added to the player &e[player]&7.");
        addValue("player.permission.add.server", "&7The &7permission &e[permission]&8(&e[world]&8) &7was added for the server &e[server] &7to the player &e[player]&7.");
        addValue("player.permission.add.group", "&7The &7permission &e[permission]&8(&e[world]&8) &7was added for the server &e[server] &7to the player &e[player]&7.");

        addValue("player.permission.remove.global", "&7The &7permission &e[permission]&8(&e[world]&8) &7was removed from the player &e[player]&7.");
        addValue("player.permission.remove.server", "&7The &7permission &e[permission]&8(&e[world]&8) &7was removed for the server &e[server] &7from the player &e[player]&7.");
        addValue("player.permission.remove.group", "&7The &7permission &e[permission]&8(&e[world]&8) &7was removed for the server &e[server] &7from the player &e[player]&7.");

        addValue("player.permission.clear.all", "&7All permissions from player &e[player] &7were cleared.");
        addValue("player.permission.clear.global.all", "&7All global permissions from player &e[player] &7were cleared.");
        addValue("player.permission.clear.global.world", "&7All global permissions for the world &e[world] &7from player &e[player] &7were cleared.");
        addValue("player.permission.clear.server.all", "&7All server permissions from player &e[player] &7were cleared.");
        addValue("player.permission.clear.server.server", "&7All permissions for the server &e[server] from &e[player] &7were cleared.");
        addValue("player.permission.clear.server.world", "&7All permissions for the world &e[world] &7on the server &e[server] &7from &e[player] &7were cleared.");
        addValue("player.permission.clear.group.all", "&7All servergroup permissions from player &e[player] &7were cleared.");
        addValue("player.permission.clear.group.group", "&7All permissions for the servergroup &e[servergroup] from &e[player] &7were cleared.");
        addValue("player.permission.clear.group.world", "&7All permissions for the world &e[world] &7on the servergroup &e[servergroup] &7from player &e[player] &7were cleared.");

        addValue("player.permission.check.has", "&7The player &e[player]&7 has the &7permission &e[permission]&8(&e[world]&8) &7on the server &e[server]&7.");
        addValue("player.permission.check.hasnot", "&7The player &e[player]&7 hasn't the &7permission &e[permission]&8(&e[world]&8) &7on the server &e[server]&7.");

        addValue("player.group.list.header", "&8» &eGroups");
        addValue("player.group.list.format", " &8- &7[color][name]&8@&7[timeout]");
        addValue("player.group.set", "&7The &7group &e[group] &7was set to the player &e[player]&7.");
        addValue("player.group.add", "&7The &7group &e[group] &7was added to the player &e[player]&7.");
        addValue("player.group.remove", "&7The &7group &e[group] &7was removed from the player &e[player]&7.");
        addValue("player.group.clear", "&7All groups from player &e[player] &7were cleared.");

        addValue("permission.list.global.header","&8» &ePermissions:");
        addValue("permission.list.global.format"," &8- &7[permission]&8@&7[timeout]");
        addValue("permission.list.world.header"," &8» &eWorldPermissions:");
        addValue("permission.list.world.world","  &8» &e[world]");
        addValue("permission.list.world.format","   &8- &7[permission]&8@&7[timeout]");

        addValue("permission.list.server.header","&8» &eServerPermissions:");
        addValue("permission.list.server.server"," &8» &e[server]");
        addValue("permission.list.server.format","  &8- &7[permission]&8@&7[timeout]");
        addValue("permission.list.server.world.header",  "   &8» &eWorldPermissions:");
        addValue("permission.list.server.world.world","    &8» &e[world]");
        addValue("permission.list.server.world.format","     &8- &7[permission]&8@&7[timeout]");

        addValue("permission.list.servergroup.header","&8» &eServerGroupPermissions");
        addValue("permission.list.servergroup.servergroup"," &8» &e[servergroup]");
        addValue("permission.list.servergroup.format","  &8- &7[permission]&8@&7[timeout]");
        addValue("permission.list.servergroup.world.header","   &8» &eWorldPermissions:");
        addValue("permission.list.servergroup.world.world","    &8» &e[world]");
        addValue("permission.list.servergroup.world.format","     &8- &7[permission]&8@&7[timeout]");

        addValue("permission.import.list.header","&7Available Imports &8(&7/perms import <type> {file}&8)");
        addValue("permission.import.list.list","&8» &7[import]");
        addValue("permission.import.notfound","&cThe import &e[import] &cwas not found.");
        addValue("permission.import.requiredfile","&cThis import required a file &8(&7/perms import [import] <file>&8)");
        addValue("permission.import.filenotfound","&cThe file &e[file] &cwas not found.");
        addValue("permission.import.start","&7Importing permissions from &e[import]&7...");
        addValue("permission.import.finish","&7Imported all permissions.");

        List<String> help_main = new LinkedList<>();
        List<String> help_player_above = new LinkedList<>();
        List<String> help_player_below = new LinkedList<>();
        List<String> help_group_above = new LinkedList<>();
        List<String> help_group_below = new LinkedList<>();
        List<String> help_permission = new LinkedList<>();

        help_main.add("");
        help_main.add("&7/perms sync | Reload all permission, groups and players.");
        help_main.add("&7/perms import | Import permissions.");
        help_main.add("&7/perms groups | List all groups.");
        help_main.add("&7/perms group | For more group informations.");
        help_main.add("&7/perms user | For more user information.");
        help_main.add("");

        help_player_above.add("");
        help_player_above.add("&8» &7/perms user <player> ");

        help_player_below.add("");
        help_player_below.add(" &8- &7group set <group> {time} {unit}");
        help_player_below.add(" &8- &7group add <group> {time} {unit}");
        help_player_below.add(" &8- &7group remove <group>");
        help_player_below.add(" &8- &7check <permission> {server} {world}");
        help_player_below.add("");

        help_group_above.add("");
        help_group_above.add("&8» &7/perms group <name> ");
        help_group_above.add(" &8- &7create");
        help_group_above.add(" &8- &7delete");
        help_group_above.add(" &8- &7rename <name>");
        help_group_above.add("");
        help_group_above.add(" &8- &7setdefault <true/false>");
        help_group_above.add(" &8- &7setteam <true/false>");
        help_group_above.add(" &8- &7setdescription <description>");
        help_group_above.add(" &8- &7setpriority <priority>");
        help_group_above.add("<-joinpower-> &8- &7setjoinpower <joinpower>");
        help_group_above.add("<-tsgroup-> &8- &7settsgroupid <tsgroupid>");
        help_group_above.add(" &8- &7setprefix <prefix>");
        help_group_above.add(" &8- &7setsuffix <suffix>");
        help_group_above.add(" &8- &7setdisplay <display>");
        help_group_above.add(" &8- &7setcolor <color>");
        help_group_above.add(" &8- &7copy <newgroup>");
        help_group_above.add("");

        help_group_below.add("");
        help_group_below.add(" &8- &7implementation set <group> {time} {unit}");
        help_group_below.add(" &8- &7implementation add <group> {time} {unit}");
        help_group_below.add(" &8- &7implementation remove <group>");
        help_group_below.add("");

        help_permission.add(" &8- &7add <permission> {time} {unit}");
        help_permission.add(" &8- &7add <permission> <world> {time}");
        help_permission.add("<-advanced-> &8- &7add server <server> <permission> {time} {unit}");
        help_permission.add("<-advanced-> &8- &7add server <server> <permission> {time} {unit}");
        help_permission.add("<-advanced-> &8- &7add group <group> <permission> {time} {unit}");
        help_permission.add("<-advanced-> &8- &7add group <group> <permission> <world> {time} {unit}");
        help_permission.add(" &8- &7remove <permission> {world}");
        help_permission.add("<-advanced-> &8- &7remove server <server> <permission> {world}");
        help_permission.add("<-advanced-> &8- &7remove group <group> <permission> {world}");
        help_permission.add(" &8- &7clearall");
        help_permission.add(" &8- &7clear {world}");
        help_permission.add("<-advanced-> &8-&7 clear server <server> {world}");
        help_permission.add("<-advanced-> &8-&7 clear group <group> {world}");

        addValue("permission.help.main", help_main);
        addValue("permission.help.group.above", help_group_above);
        addValue("permission.help.group.below", help_group_below);
        addValue("permission.help.player.above", help_player_above);
        addValue("permission.help.player.below", help_player_below);
        addValue("permission.help.permission", help_permission);

        addValue("rank.list.my","&7Your ranks");
        addValue("rank.list.other","&7Ranks from [player]");
        addValue("rank.list.format","&8» [color][name]&8@&7[timeout]");
        addValue("rank.nopermissions.for.rank","&cYou don't have permissions to set, add or remove the rank [group].");
        addValue("rank.nopermissions.for.player","&cYou don't have permissions to change ranks from &7[player]&c.");
        addValue("rank.nofound","&cNo Rank was found.");
        List<String> rank_help = new LinkedList<>();
        rank_help.add("");
        rank_help.add("&7/rank list");
        rank_help.add("&7/rank <player>");
        rank_help.add("&7/rank <player> promote {time} {unit}");
        rank_help.add("&7/rank <player> demote {time} {unit}");
        rank_help.add("&7/rank <player> set <rank> {time} {unit}");
        rank_help.add("&7/rank <player> add <rank> {time} {unit}");
        rank_help.add("&7/rank <player> remove <rank> {time} {unit}");
        rank_help.add("");
        addValue("rank.help",rank_help);

        addValue("team.list.header","");
        addValue("team.list.between","");
        addValue("team.list.footer","");
        addValue("team.list.format.group","&8» &7[color][name] &8| &7[description]");
        addValue("team.list.format.player"," &8- &7[color][player]");
    }
    public String get(String path){
        String value = getStringValue(path);
        if(value != null) return getPlatform().translateColorCodes(value);
        return null;
    }
}

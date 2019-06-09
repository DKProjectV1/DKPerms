package ch.dkrieger.permissionsystem.lib.command.defaults;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 23.06.18 14:45
 *
 */

import ch.dkrieger.permissionsystem.lib.PermissionSystem;
import ch.dkrieger.permissionsystem.lib.command.PermissionCommand;
import ch.dkrieger.permissionsystem.lib.command.PermissionCommandSender;
import ch.dkrieger.permissionsystem.lib.config.Config;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupEntity;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;
import ch.dkrieger.permissionsystem.lib.importation.PermissionImport;
import ch.dkrieger.permissionsystem.lib.importation.PermissionImportManager;
import ch.dkrieger.permissionsystem.lib.permission.PermissionEntity;
import ch.dkrieger.permissionsystem.lib.permission.data.PermissionData;
import ch.dkrieger.permissionsystem.lib.permission.data.SimplePermissionData;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import ch.dkrieger.permissionsystem.lib.utils.GeneralUtil;
import ch.dkrieger.permissionsystem.lib.utils.Messages;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CommandPermission extends PermissionCommand {

    public static Boolean ADVANCED, JOINPOWER, TSGROUP;

    public CommandPermission() {
        super("permission","dkperms.admin","perms");
        JOINPOWER = false;
        TSGROUP = false;
        ADVANCED = Config.ADVANCED;
    }
    @Override
    public void execute(PermissionCommandSender sender, String[] args) {
        if(args.length >= 1){
           if(args[0].equalsIgnoreCase("sync") || args[0].equalsIgnoreCase("reload")){
                sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_SYNCHRONISEING);
                PermissionSystem.getInstance().sync();
                sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_SYNCHRONISEED);
                return;
           }else if(args[0].equalsIgnoreCase("group") && args.length == 1){
               sendHelp(sender,HelpType.GROUP);
               return;
           }else if(args[0].equalsIgnoreCase("player") || args[0].equalsIgnoreCase("user") && args.length == 1){
               sendHelp(sender,HelpType.PLAYER);
               return;
           }else if(args[0].equalsIgnoreCase("import") && args.length == 1){
               sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_IMPORT_LIST_HEADER);
               for(PermissionImport imp : PermissionImportManager.getInstance().getAvailableImports()){
                   sender.sendMessage(Messages.PERMISSION_IMPORT_LIST.replace("[import]",imp.getName()));
               }
               sender.sendMessage("");
               return;
           }else if(args[0].equalsIgnoreCase("groups")){
                sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_LIST_HEADER);
                for(PermissionGroup group : PermissionGroupManager.getInstance().getSortedGroups()){
                    TextComponent text = new TextComponent(Messages.PERMISSION_GROUP_LIST_FORMAT
                            .replace("[name]",group.getName())
                            .replace("[color]",group.getColor())
                            .replace("[prefix]",group.getPlayerDesign().getPrefix())
                            .replace("[suffix]",group.getPlayerDesign().getSuffix())
                            .replace("[display]",group.getPlayerDesign().getDisplay())
                            .replace("[description]",group.getDescription())
                            .replace("[priority]",""+group.getPriority())
                            .replace("[name]",group.getName()));
                    text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/perms group "+group.getName()));
                    text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder(Messages.PERMISSION_GROUP_LIST_HOVER).create()));
                    sender.sendMessage(text);
                }
                sender.sendMessage("");
                return;
            }else if(args.length >= 2){
                if(args[0].equalsIgnoreCase("group")){
                    PermissionGroup group = PermissionGroupManager.getInstance().getGroup(args[1]);
                    if(args.length >= 3){
                        if(args[2].equalsIgnoreCase("create")){
                            if(group != null){
                                sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_ALREADYEXISTS
                                        .replace("[group]",group.getColor()+group.getName()));
                                return;
                            }
                            PermissionGroupManager.getInstance().createGroup(args[1]);
                            sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_CREATED.replace("[group]",args[1]));
                            return;
                        }else if(group == null){
                            sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_NOTFOUND.replace("[group]",args[1]));
                            return;
                        }else if(args[2].equalsIgnoreCase("delete")){
                            PermissionGroupManager.getInstance().deleteGroup(group);
                            sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_DELETED.replace("[group]",args[1]));
                            return;
                        }else if(args[2].equalsIgnoreCase("list") || args[2].equalsIgnoreCase("players") || args[2].equalsIgnoreCase("playerlist")){
                            sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_PLAYERLIST_HEADER.replace("[group]",args[1]));
                            for(PermissionPlayer player : group.getPlayers()){
                                if(player == null) continue;
                                PermissionGroup playergroup = player.getHighestGroup();
                                if(playergroup == null) return;
                                sender.sendMessage(Messages.PERMISSION_GROUP_PLAYERLIST_FORMAT
                                        .replace("[group_color]", group.getPlayerDesign().getColor())
                                        .replace("[group_prefix]", group.getPlayerDesign().getColor())
                                        .replace("[group_display]", group.getPlayerDesign().getDisplay())
                                        .replace("[group_suffix]", group.getPlayerDesign().getDisplay())
                                        .replace("[group_priority]", "" +group.getPriority())
                                        .replace("[group_name]", group.getName())
                                        .replace("[group_description]",group.getDescription())
                                        .replace("[color]",playergroup.getPlayerDesign().getColor())
                                        .replace("[prefix]",playergroup.getPlayerDesign().getPrefix())
                                        .replace("[suffix]",playergroup.getPlayerDesign().getSuffix())
                                        .replace("[display]",playergroup.getPlayerDesign().getDisplay())
                                        .replace("[player]",player.getName()));
                            }
                            sender.sendMessage("");
                            return;
                        }else if(args[2].equalsIgnoreCase("rename")){
                            if(PermissionGroupManager.getInstance().getGroup(args[3]) != null){
                                sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_ALREADYEXISTS
                                .replace("[group]",args[3]));
                                return;
                            }
                            group.setName(args[3]);
                            sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_REANMED
                                    .replace("[group]",args[1]).replace("[name]",args[3]));
                            return;
                        }else if(args[2].equalsIgnoreCase("copy")){
                            if(PermissionGroupManager.getInstance().getGroup(args[3]) != null){
                                sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_ALREADYEXISTS
                                        .replace("[group]",args[3]));
                                return;
                            }
                            sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_COPY_START.replace("[group]",group.getName()));
                            PermissionGroup copy = PermissionGroupManager.getInstance().createGroup(args[3]);
                            group.copy(copy);
                            sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_COPY_FINISH.replace("[group]",group.getName()).replace("[copy]",copy.getName()));
                            return;
                        }else if(args[2].equalsIgnoreCase("clearall")){
                            group.clearAllPermissions();
                            sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_PERMISSION_CLEAR_ALL
                            .replace("[group]",group.getColor()+group.getColor()+group.getName()));
                            return;
                        }else if(args[2].equalsIgnoreCase("clear")){
                            if(args.length >= 4){
                                if(args[3].equalsIgnoreCase("server")){
                                    if(args.length >= 5){
                                        String world = null;
                                        if(args.length >= 6){ world = args[5];
                                        sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_PERMISSION_CLEAR_SERVER_WORLD
                                                .replace("[group]",group.getColor()+group.getColor()+group.getName()).replace("[servergroup]",args[4])
                                                .replace("[world]",world));
                                        }else{
                                            sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_PERMISSION_CLEAR_SERVER_SERVER
                                                    .replace("[group]",group.getColor()+group.getColor()+group.getName()).replace("[servergroup]",args[4]));
                                        }
                                        group.clearServerPermissions(args[4],world);
                                    }else{
                                        sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_PERMISSION_CLEAR_SERVER_ALL
                                                .replace("[group]",group.getColor()+group.getColor()+group.getName()));
                                        group.clearServerPermissions();
                                    }
                                }else if(args[3].equalsIgnoreCase("group")){
                                    if(args.length >= 5){
                                        String world = null;
                                        if(args.length >= 6){
                                            world = args[5];
                                            sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_PERMISSION_CLEAR_GROUP_WORLD
                                                    .replace("[group]",group.getColor()+group.getColor()+group.getName()).replace("[servergroup]",args[4])
                                                    .replace("[world]",world));
                                        }else{
                                            sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_PERMISSION_CLEAR_GROUP_GROUP
                                                    .replace("[group]",group.getColor()+group.getColor()+group.getName()).replace("[servergroup]",args[4]));
                                        }
                                        group.clearServerGroupPermissions(args[4],world);
                                    }else{
                                        sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_PERMISSION_CLEAR_GROUP_ALL
                                                .replace("[group]",group.getColor()+group.getColor()+group.getName()));
                                        group.clearServerGroupPermissions();
                                    }
                                }else{
                                    sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_PERMISSION_CLEAR_GLOBAL_WORLD
                                            .replace("[group]",group.getColor()+group.getColor()+group.getName()).replace("[world]",args[3]));
                                    group.clearPermissions(args[3]);
                                }
                            }else{
                                sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_PERMISSION_CLEAR_GLOBAL_ALL
                                        .replace("[group]",group.getColor()+group.getName()));
                                group.clearPermissions();
                            }
                            return;
                        }else if(args.length >= 4){
                            if(args[2].equalsIgnoreCase("setdefault")){
                                if(args[3].equalsIgnoreCase("true") || args[3].equalsIgnoreCase("false")){
                                    group.setDefault(Boolean.valueOf(args[3]));
                                    sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_SETTINGSCHANGED
                                            .replace("[group]",group.getColor()+group.getName())
                                            .replace("[setting]","Default").replace("[value]",args[3]));
                                    return;
                                }
                            }else if(args[2].equalsIgnoreCase("setdescription")){
                                String description = "";
                                for(int arg = 3;arg < args.length;arg++) description += args[arg]+" ";
                                if(description.length() > 0) description = description.substring(0,description.length()-1);
                                group.setDescription(description);
                                sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_SETTINGSCHANGED
                                        .replace("[group]",group.getColor()+group.getName())
                                        .replace("[setting]","Description").replace("[value]",description));
                                return;
                            }else if(args[2].equalsIgnoreCase("setteam")){
                                if(args[3].equalsIgnoreCase("true") || args[3].equalsIgnoreCase("false")){
                                    group.setTeam(Boolean.valueOf(args[3]));
                                    sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_SETTINGSCHANGED
                                            .replace("[group]",group.getColor()+group.getName())
                                            .replace("[setting]","Team").replace("[value]",args[3]));
                                    return;
                                }
                            }else if(args[2].equalsIgnoreCase("setpriority")){
                                if(GeneralUtil.isNumber(args[3])){
                                    group.setPriority(Integer.valueOf(args[3]));
                                    sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_SETTINGSCHANGED
                                            .replace("[group]",group.getColor()+group.getName())
                                            .replace("[setting]","Priority").replace("[value]",args[3]));
                                    return;
                                }
                            }else if(args[2].equalsIgnoreCase("setprefix")){
                                String message = "";
                                for(int i= 3;i<args.length;i++){
                                    message +=args[i]+" ";
                                }
                                group.setPrefix(message.substring(0,message.length()-1));

                                sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_SETTINGSCHANGED
                                        .replace("[group]",group.getColor()+group.getName())
                                        .replace("[setting]","Prefix").replace("[value]",args[3]));
                                return;
                            }else if(args[2].equalsIgnoreCase("setsuffix")){
                                String message = "";
                                for(int i= 3;i<args.length;i++){
                                    message +=args[i]+" ";
                                }
                                group.setSuffix(message.substring(0,message.length()-1));
                                sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_SETTINGSCHANGED
                                        .replace("[group]",group.getColor()+group.getName())
                                        .replace("[setting]","Suffix").replace("[value]",args[3]));
                                return;
                            }else if(args[2].equalsIgnoreCase("setdisplay")){
                                String message = "";
                                for(int i= 3;i<args.length;i++){
                                    message +=args[i]+" ";
                                }
                                group.setDisplay(message.substring(0,message.length()-1));
                                sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_SETTINGSCHANGED
                                        .replace("[group]",group.getColor()+group.getName())
                                        .replace("[setting]","Display").replace("[value]",args[3]));
                                return;
                            }else if(args[2].equalsIgnoreCase("setcolor")){
                                group.setColor(args[3]);
                                sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_SETTINGSCHANGED
                                        .replace("[group]",group.getColor()+group.getName())
                                        .replace("[setting]","Color").replace("[value]",args[3]));
                                return;
                            }else if(args[2].equalsIgnoreCase("setjoinpower")){
                                if(GeneralUtil.isNumber(args[3])){
                                    group.setJoinPower(Integer.valueOf(args[3]));
                                    sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_SETTINGSCHANGED
                                            .replace("[group]",group.getColor()+group.getName())
                                            .replace("[setting]","JoinPower").replace("[value]",args[3]));
                                    return;
                                }
                            }else if(args[2].equalsIgnoreCase("settsgroup")){
                                if(GeneralUtil.isNumber(args[3])){
                                    group.setTsGroupID(Integer.valueOf(args[3]));
                                    sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_SETTINGSCHANGED
                                            .replace("[group]",group.getColor()+group.getName())
                                            .replace("[setting]","TsGroupID").replace("[value]",args[3]));
                                    return;
                                }
                            }else if(args[2].equalsIgnoreCase("add")){
                                if(args[3].equalsIgnoreCase("server") && args.length >= 6){
                                    String world = null;
                                    DurationResult duration = new DurationResult(-1L,null);
                                    if(args.length >= 7){
                                        if(GeneralUtil.isNumber(args[6])){
                                            if(args.length >= 8) duration = getDuration(Long.valueOf(args[6]),args[7]);
                                            else duration = getDuration(Long.valueOf(args[6]),"");
                                        }else{
                                            world = args[6];
                                            if(args.length >= 8 && GeneralUtil.isNumber(args[7])){
                                                if(args.length >= 9) duration = getDuration(Long.valueOf(args[7]),args[8]);
                                                else duration = getDuration(Long.valueOf(args[7]),"");
                                            }
                                        }
                                    }
                                    sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_PERMISSION_ADD_SERVER
                                            .replace("[group]",group.getColor()+group.getName()).replace("[permission]",args[5])
                                            .replace("[server]",args[4])
                                            .replace("[world]",(world == null ? "global" : world)));
                                    group.addServerPermission(args[4],args[5],world,duration.getDuration(),duration.getUnit());
                                }else if(args[3].equalsIgnoreCase("group") && args.length >= 6){
                                    String world = null;
                                    DurationResult duration = new DurationResult(-1L,null);
                                    if(args.length >= 7){
                                        if(GeneralUtil.isNumber(args[6])){
                                            if(args.length >= 8) duration = getDuration(Long.valueOf(args[6]),args[7]);
                                            else duration = getDuration(Long.valueOf(args[6]),"");
                                        }else{
                                            world = args[6];
                                            if(args.length >= 8 && GeneralUtil.isNumber(args[7])) duration = getDuration(Long.valueOf(args[7]),"");
                                            else if(args.length >= 9) duration = getDuration(Long.valueOf(args[7]),args[8]);
                                        }
                                    }
                                    sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_PERMISSION_ADD_GROUP
                                            .replace("[group]",group.getColor()+group.getName()).replace("[permission]",args[5])
                                            .replace("[servergroup]",args[4])
                                            .replace("[world]",(world == null ? "global" : world)));
                                    group.addServerGroupPermission(args[4],args[5],world,duration.getDuration(),duration.getUnit());
                                }else{
                                    String world = null;
                                    DurationResult duration = new DurationResult(-1L,null);
                                    if(args.length >= 5){
                                        if(GeneralUtil.isNumber(args[4])){
                                            if(args.length >= 6) duration = getDuration(Long.valueOf(args[4]),args[5]);
                                            else duration = getDuration(Long.valueOf(args[4]),"");
                                        }else{
                                            world = args[4];
                                            if(args.length >= 6 && GeneralUtil.isNumber(args[5])){
                                                if(args.length >= 7) duration = getDuration(Long.valueOf(args[5]),args[6]);
                                                else duration = getDuration(Long.valueOf(args[5]),"");
                                            }
                                        }
                                    }
                                    sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_PERMISSION_ADD_GLOBAL
                                            .replace("[group]",group.getColor()+group.getName()).replace("[permission]",args[3])
                                            .replace("[world]",(world == null ? "global" : world)));
                                    group.addPermission(args[3],world,duration.getDuration(),duration.getUnit());
                                }
                            }else if(args[2].equalsIgnoreCase("remove")){
                                if(args[3].equalsIgnoreCase("server") && args.length >= 6){
                                    String world = null;
                                    if(args.length >= 7) world = args[6];
                                    sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_PERMISSION_REMOVE_SERVER
                                            .replace("[group]",group.getColor()+group.getName()).replace("[permission]",args[5])
                                            .replace("[server]",args[4])
                                            .replace("[world]",(world == null ? "global" : world)));
                                    group.removeServerPermission(args[4],args[5],world);
                                }else if(args[3].equalsIgnoreCase("group") && args.length >= 6){
                                    String world = null;
                                    if(args.length >= 7) world = args[6];
                                    sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_PERMISSION_REMOVE_GROUP
                                            .replace("[group]",group.getColor()+group.getName()).replace("[permission]",args[5])
                                            .replace("[servergroup]",args[4])
                                            .replace("[world]",(world == null ? "global" : world)));
                                    group.removeServerGroupPermission(args[4],args[5],world);
                                }else{
                                    String world = null;
                                    if(args.length >= 5) world = args[4];
                                    sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_PERMISSION_REMOVE_GLOBAL
                                            .replace("[group]",group.getColor()+group.getName()).replace("[permission]",args[3])
                                            .replace("[world]",(world == null ? "global" : world)));
                                    group.removePermission(args[3],world);
                                }
                            }else if((args[2].equalsIgnoreCase("implementation") ||
                                    args[2].equalsIgnoreCase("implement") || args[2].equalsIgnoreCase("impl")
                                    || args[2].equalsIgnoreCase("group")) && args.length >= 5){
                                PermissionGroup implementation = PermissionGroupManager.getInstance().getGroup(args[4]);
                                if(implementation == null){
                                    sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_NOTFOUND
                                            .replace("[group]",args[4]));
                                    return;
                                }else if(args[3].equalsIgnoreCase("set")){
                                    DurationResult duration = new DurationResult(-1L,null);
                                    if(args.length >= 6 && GeneralUtil.isNumber(args[5])){
                                        if(args.length >= 7) duration = getDuration(Long.valueOf(args[5]),args[6]);
                                        else duration = getDuration(Long.valueOf(args[5]),"");
                                    }
                                    sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_IMPLEMENTATION_SET
                                            .replace("[group]",group.getColor()+group.getName()).replace("[implementation]",implementation.getColor()+implementation.getName()));
                                    group.setGroup(implementation,duration.getDuration(),duration.getUnit());
                                }else if(args[3].equalsIgnoreCase("add")){
                                    DurationResult duration = new DurationResult(-1L,null);
                                    if(args.length >= 6 && GeneralUtil.isNumber(args[5])){
                                        if(args.length >= 7) duration = getDuration(Long.valueOf(args[5]),args[6]);
                                        else duration = getDuration(Long.valueOf(args[5]),"");
                                    }
                                    sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_IMPLEMENTATION_ADD
                                            .replace("[group]",group.getColor()+group.getName()).replace("[implementation]",implementation.getColor()+implementation.getName()));
                                    group.addGroup(implementation,duration.getDuration(),duration.getUnit());
                                }else if(args[3].equalsIgnoreCase("remove")){
                                    sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_IMPLEMENTATION_REMOVE
                                            .replace("[group]",group.getColor()+group.getName()).replace("[implementation]",implementation.getColor()+implementation.getName()));
                                    group.removeGroup(implementation);
                                }
                                return;
                            }
                            return;
                        }
                    }else{
                        if(group == null){
                            sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_NOTFOUND.replace("[group]",args[1]));
                            return;
                        }else{
                            sender.sendMessage("");
                            sender.sendMessage(Messages.PERMISSION_GROUP_INFO_FORMAT.replace("[id]"
                                    ,Messages.PERMISSION_GROUP_INFO_NAME).replace("[value]",group.getColor()+group.getName()));
                            sender.sendMessage(Messages.PERMISSION_GROUP_INFO_FORMAT.replace("[id]"
                                    ,Messages.PERMISSION_GROUP_INFO_DESCRIPTION).replace("[value]",group.getDescription()));
                            sender.sendMessage(Messages.PERMISSION_GROUP_INFO_FORMAT.replace("[id]"
                                    ,Messages.PERMISSION_GROUP_INFO_UUID).replace("[value]",""+group.getUUID()));
                            sender.sendMessage(Messages.PERMISSION_GROUP_INFO_FORMAT.replace("[id]"
                                    ,Messages.PERMISSION_GROUP_INFO_DEFAULT).replace("[value]",""+group.isDefault()));
                            sender.sendMessage(Messages.PERMISSION_GROUP_INFO_FORMAT.replace("[id]"
                                    ,Messages.PERMISSION_GROUP_INFO_TEAM).replace("[value]",""+group.isTeam()));
                            sender.sendMessage(Messages.PERMISSION_GROUP_INFO_FORMAT.replace("[id]"
                                    ,Messages.PERMISSION_GROUP_INFO_PRIORITY).replace("[value]",""+group.getPriority()));
                            if(JOINPOWER) sender.sendMessage(Messages.PERMISSION_GROUP_INFO_FORMAT.replace("[id]"
                                    ,Messages.PERMISSION_GROUP_INFO_JOINPOWER).replace("[value]",""+group.getJoinpower()));
                            if(TSGROUP) sender.sendMessage(Messages.PERMISSION_GROUP_INFO_FORMAT.replace("[id]"
                                    ,Messages.PERMISSION_GROUP_INFO_TSGROUPID).replace("[value]",""+group.getTsgroupID()));
                            sender.sendMessage(Messages.PERMISSION_GROUP_INFO_FORMAT.replace("[id]"
                                    ,Messages.PERMISSION_GROUP_INFO_PREFIX).replace("[value]",""+group.getPlayerDesign().getPrefix()+"Player"));
                            sender.sendMessage(Messages.PERMISSION_GROUP_INFO_FORMAT.replace("[id]"
                                    ,Messages.PERMISSION_GROUP_INFO_SUFFIX).replace("[value]","Player"+group.getPlayerDesign().getSuffix()));
                            sender.sendMessage(Messages.PERMISSION_GROUP_INFO_FORMAT.replace("[id]"
                                    ,Messages.PERMISSION_GROUP_INFO_DISPLAY).replace("[value]",""+group.getPlayerDesign().getDisplay()+"Player"));
                            sender.sendMessage(Messages.PERMISSION_GROUP_INFO_FORMAT.replace("[id]"
                                    ,Messages.PERMISSION_GROUP_INFO_COLOR).replace("[value]",""+group.getPlayerDesign().getColor()+"Color"));
                            sender.sendMessage("");
                            sendPermissionData(sender,group.getPermissionData());
                            if(group.getGroups().size() > 0){
                                sender.sendMessage(Messages.PERMISSION_GROUP_IMPLEMENTATION_LIST_HEADER);
                                for(PermissionGroupEntity entity : group.getGroups()){
                                    if(!(entity.hasTimeOut()) && entity.getGroup() != null){
                                        TextComponent text = new TextComponent(Messages.PERMISSION_GROUP_IMPLEMENTATION_LIST_FORMAT
                                                .replace("[name]",entity.getGroup().getName())
                                                .replace("[color]",entity.getGroup().getColor())
                                                .replace("[prefix]",entity.getGroup().getPlayerDesign().getPrefix())
                                                .replace("[suffix]",entity.getGroup().getPlayerDesign().getSuffix())
                                                .replace("[display]",entity.getGroup().getPlayerDesign().getDisplay())
                                                .replace("[description]",entity.getGroup().getDescription())
                                                .replace("[priority]",""+entity.getGroup().getPriority())
                                                .replace("[timeout]",entity.getTimeOutDate())
                                                .replace("[name]",entity.getGroup().getName()));
                                        text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/perms group "+entity.getGroup().getName()));
                                        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder(Messages.PERMISSION_GROUP_LIST_HOVER).create()));
                                        sender.sendMessage(text);
                                    }
                                }
                                sender.sendMessage("");
                            }
                        }
                        return;
                    }
                    sendHelp(sender,HelpType.GROUP);
                    return;
                }else if(args[0].equalsIgnoreCase("user") || args[0].equalsIgnoreCase("player")){
                    PermissionPlayer player = PermissionPlayerManager.getInstance().getPermissionPlayer(args[1]);
                    if(player == null){
                        sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_PLAYER_NOTFOUND.replace("[player]",args[1]));
                        return;
                    }else if(args.length >= 3){
                        if(args[2].equalsIgnoreCase("clearall")){
                            player.clearAllPermissions();
                            sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_PLAYER_PERMISSION_CLEAR_ALL
                                    .replace("[player]",player.getColor()+player.getName()));
                            return;
                        }else if(args[2].equalsIgnoreCase("clear")){
                            if(args.length >= 4){
                                if(args[3].equalsIgnoreCase("server")){
                                    if(args.length >= 5){
                                        String world = null;
                                        if(args.length >= 6){ world = args[5];
                                            sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_PLAYER_PERMISSION_CLEAR_SERVER_WORLD
                                                    .replace("[player]",player.getColor()+player.getName()).replace("[servergroup]",args[4])
                                                    .replace("[world]",world));
                                        }else{
                                            sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_PLAYER_PERMISSION_CLEAR_SERVER_SERVER
                                                    .replace("[player]",player.getColor()+player.getName()).replace("[servergroup]",args[4]));
                                        }
                                        player.clearServerPermissions(args[4],world);
                                    }else{
                                        sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_PLAYER_PERMISSION_CLEAR_SERVER_ALL
                                                .replace("[player]",player.getColor()+player.getName()));
                                        player.clearServerPermissions();
                                    }
                                }else if(args[3].equalsIgnoreCase("group")){
                                    if(args.length >= 5){
                                        String world = null;
                                        if(args.length >= 6){
                                            world = args[5];
                                            sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_PLAYER_PERMISSION_CLEAR_GROUP_WORLD
                                                    .replace("[player]",player.getColor()+player.getName()).replace("[servergroup]",args[4])
                                                    .replace("[world]",world));
                                        }else{
                                            sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_PLAYER_PERMISSION_CLEAR_GROUP_GROUP
                                                    .replace("[player]",player.getColor()+player.getName()).replace("[servergroup]",args[4]));
                                        }
                                        player.clearServerGroupPermissions(args[4],world);
                                    }else{
                                        sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_PLAYER_PERMISSION_CLEAR_GROUP_ALL
                                                .replace("[player]",player.getColor()+player.getName()));
                                        player.clearServerGroupPermissions();
                                    }
                                }else{
                                    sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_PLAYER_PERMISSION_CLEAR_GLOBAL_WORLD
                                            .replace("[player]",player.getColor()+player.getName()).replace("[world]",args[3]));
                                    player.clearPermissions(args[3]);
                                }
                            }else{
                                sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_PLAYER_PERMISSION_CLEAR_GLOBAL_ALL
                                        .replace("[player]",player.getColor()+player.getName()));
                                player.clearPermissions();
                            }
                            return;
                        }else if(args.length >= 4){
                            if(args[2].equalsIgnoreCase("check")){//perms user dkrieger check test
                                String server = null;
                                if(args.length >= 5) server = args[4];
                                String world = null;
                                if(args.length >= 6) world = args[5];
                                if(server != null && server.equalsIgnoreCase("local")) server = null;

                                if(player.hasPermission(args[3],server,world)){
                                    sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_PLAYER_PERMISSION_CHECK_HAS
                                            .replace("[player]",player.getColor()+player.getName())
                                            .replace("[permission]",args[3])
                                            .replace("[server]",(server != null?server:"all"))
                                            .replace("[world]",(world != null?world:"global")));
                                }else{
                                    sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_PLAYER_PERMISSION_CHECK_HASNOT
                                            .replace("[player]",player.getColor()+player.getName())
                                            .replace("[permission]",args[3])
                                            .replace("[server]",(server != null?server:"all"))
                                            .replace("[world]",(world != null?world:"global")));
                                }
                                return;
                            }else if(args[2].equalsIgnoreCase("add")){
                                if(args[3].equalsIgnoreCase("server") && args.length >= 6){
                                    String world = null;
                                    DurationResult duration = new DurationResult(-1L,null);
                                    if(args.length >= 7){
                                        if(GeneralUtil.isNumber(args[6])){
                                            if(args.length >= 8) duration = getDuration(Long.valueOf(args[6]),args[7]);
                                            else duration = getDuration(Long.valueOf(args[6]),"");
                                        }else{
                                            world = args[6];
                                            if(args.length >= 8 && GeneralUtil.isNumber(args[7])){
                                                if(args.length >= 9) duration = getDuration(Long.valueOf(args[7]),args[8]);
                                                else duration = getDuration(Long.valueOf(args[7]),"");
                                            }
                                        }
                                    }
                                    sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_PLAYER_PERMISSION_ADD_SERVER
                                            .replace("[player]",player.getName()).replace("[permission]",args[5])
                                            .replace("[server]",args[4])
                                            .replace("[world]",(world == null ? "global" : world)));
                                    player.addServerPermission(args[4],args[5],world,duration.getDuration(),duration.getUnit());
                                }else if(args[3].equalsIgnoreCase("group") && args.length >= 6){
                                    String world = null;
                                    DurationResult duration = new DurationResult(-1L,null);
                                    if(args.length >= 7){
                                        if(GeneralUtil.isNumber(args[6])){
                                            if(args.length >= 8) duration = getDuration(Long.valueOf(args[6]),args[7]);
                                            else duration = getDuration(Long.valueOf(args[6]),"");
                                        }else{
                                            world = args[6];
                                            if(args.length >= 8 && GeneralUtil.isNumber(args[7])) duration = getDuration(Long.valueOf(args[7]),"");
                                            else if(args.length >= 9) duration = getDuration(Long.valueOf(args[7]),args[8]);
                                        }
                                    }
                                    sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_PLAYER_PERMISSION_ADD_SERVER
                                            .replace("[player]",player.getName()).replace("[permission]",args[5])
                                            .replace("[servergroup]",args[4])
                                            .replace("[world]",(world == null ? "global" : world)));
                                    player.addServerGroupPermission(args[4],args[5],world,duration.getDuration(),duration.getUnit());
                                }else{
                                    String world = null;
                                    DurationResult duration = new DurationResult(-1L,null);
                                    if(args.length >= 5){
                                        if(GeneralUtil.isNumber(args[4])){
                                            if(args.length >= 6) duration = getDuration(Long.valueOf(args[4]),args[5]);
                                            else duration = getDuration(Long.valueOf(args[4]),"");
                                        }else{
                                            world = args[4];
                                            if(args.length >= 6 && GeneralUtil.isNumber(args[5])){
                                                if(args.length >= 7) duration = getDuration(Long.valueOf(args[5]),args[6]);
                                                else duration = getDuration(Long.valueOf(args[5]),"");
                                            }
                                        }
                                    }
                                    sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_PLAYER_PERMISSION_ADD_GLOBAL
                                            .replace("[player]",player.getName()).replace("[permission]",args[3])
                                            .replace("[world]",(world == null ? "global" : world)));
                                    player.addPermission(args[3],world,duration.getDuration(),duration.getUnit());
                                }
                                return;
                            }else if(args[2].equalsIgnoreCase("remove")){
                                if(args[3].equalsIgnoreCase("server") && args.length >= 6){
                                    String world = null;
                                    if(args.length >= 7) world = args[6];
                                    sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_PLAYER_PERMISSION_REMOVE_SERVER
                                            .replace("[player]",player.getName()).replace("[permission]",args[5])
                                            .replace("[server]",args[4])
                                            .replace("[world]",(world == null ? "global" : world)));
                                    player.removeServerPermission(args[4],args[5],world);
                                }else if(args[3].equalsIgnoreCase("group") && args.length >= 6){
                                    String world = null;
                                    if(args.length >= 7) world = args[6];
                                    sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_PLAYER_PERMISSION_REMOVE_GROUP
                                            .replace("[player]",player.getName()).replace("[permission]",args[5])
                                            .replace("[servergroup]",args[4])
                                            .replace("[world]",(world == null ? "global" : world)));
                                    player.removeServerGroupPermission(args[4],args[5],world);
                                }else{
                                    String world = null;
                                    if(args.length >= 5) world = args[4];
                                    sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_PLAYER_PERMISSION_REMOVE_GLOBAL
                                            .replace("[player]",player.getName()).replace("[permission]",args[3])
                                            .replace("[world]",(world == null ? "global" : world)));
                                    player.removePermission(args[3],world);
                                }
                                return;
                            }else if(args[2].equalsIgnoreCase("group") && args.length >= 5){
                                PermissionGroup implementation = PermissionGroupManager.getInstance().getGroup(args[4]);
                                if(implementation == null){
                                    sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_NOTFOUND
                                    .replace("[group]",args[4]));
                                    return;
                                }else if(args[3].equalsIgnoreCase("set")){
                                    DurationResult duration = new DurationResult(-1L,null);
                                    if(args.length >= 6 && GeneralUtil.isNumber(args[5])){
                                        if(args.length >= 7) duration = getDuration(Long.valueOf(args[5]),args[6]);
                                        else duration = getDuration(Long.valueOf(args[5]),"");
                                    }
                                    sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_PLAYER_GROUP_SET
                                            .replace("[player]",player.getName()).replace("[group]",implementation.getColor()+implementation.getName()));
                                    player.setGroup(implementation,duration.getDuration(),duration.getUnit());
                                    return;
                                }else if(args[3].equalsIgnoreCase("add")){
                                    DurationResult duration = new DurationResult(-1L,null);
                                    if(args.length >= 6 && GeneralUtil.isNumber(args[5])){
                                        if(args.length >= 7) duration = getDuration(Long.valueOf(args[5]),args[6]);
                                        else duration = getDuration(Long.valueOf(args[5]),"");
                                    }
                                    sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_PLAYER_GROUP_ADD
                                            .replace("[player]",player.getName()).replace("[group]",implementation.getColor()+implementation.getName()));
                                    player.addGroup(implementation,duration.getDuration(),duration.getUnit());
                                    return;
                                }else if(args[3].equalsIgnoreCase("remove")){
                                    sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_PLAYER_GROUP_REMOVE
                                            .replace("[player]",player.getName()).replace("[group]",implementation.getColor()+implementation.getName()));
                                    player.removeGroup(implementation);
                                    return;
                                }
                            }
                        }
                        sendHelp(sender,HelpType.PLAYER);
                        return;
                    }else{
                        sender.sendMessage("");
                        sender.sendMessage(Messages.PERMISSION_PLAYER_INFO_FORMAT.replace("[id]"
                                ,Messages.PERMISSION_PLAYER_INFO_ID).replace("[value]",""+player.getID()));
                        sender.sendMessage(Messages.PERMISSION_PLAYER_INFO_FORMAT.replace("[id]"
                                ,Messages.PERMISSION_PLAYER_INFO_NAME).replace("[value]",player.getColor()+player.getName()));
                        sender.sendMessage(Messages.PERMISSION_PLAYER_INFO_FORMAT.replace("[id]"
                                ,Messages.PERMISSION_PLAYER_INFO_UUID).replace("[value]",""+player.getUUID()));
                        sender.sendMessage("");
                        sendPermissionData(sender,player.getPermissionData());
                        if(player.getGroups().size() > 0){
                            sender.sendMessage(Messages.PERMISSION_PLAYER_GROUP_LIST_HEADER);
                            for(PermissionGroupEntity entity : player.getGroups()){
                                if(!entity.hasTimeOut()){
                                    PermissionGroup group = entity.getGroup();
                                    if(group != null){
                                        TextComponent text = new TextComponent(Messages.PERMISSION_PLAYER_GROUP_LIST_FORMAT
                                                .replace("[name]",group.getName())
                                                .replace("[color]",group.getColor())
                                                .replace("[prefix]",group.getPlayerDesign().getPrefix())
                                                .replace("[suffix]",group.getPlayerDesign().getSuffix())
                                                .replace("[display]",group.getPlayerDesign().getDisplay())
                                                .replace("[description]",group.getDescription())
                                                .replace("[timeout]",entity.getTimeOutDate())
                                                .replace("[priority]",""+group.getPriority())
                                                .replace("[name]",group.getName()));
                                        text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/perms group "+group.getName()));
                                        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder(Messages.PERMISSION_GROUP_LIST_HOVER).create()));
                                        sender.sendMessage(text);
                                    }
                                }
                            }
                            sender.sendMessage("");
                        }
                        return;
                    }
                }else if(args[0].equalsIgnoreCase("import") || args[0].equalsIgnoreCase("importieren")){
                    PermissionImport imp = PermissionImportManager.getInstance().getImport(args[1]);
                    if(imp == null){
                        sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_IMPORT_NOTFOUND
                                .replace("[import]",args[1]));
                        return;
                    }
                    File file = null;
                    if(imp.needFile()){
                        if(args.length >= 3){
                            file = new File(args[2]);
                            if(file.isDirectory() || !(file.exists())){
                                sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_IMPORT_FILENOTFOUND
                                        .replace("[file]",file.getPath()));
                                return;
                            }
                        }else{
                            sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_IMPORT_REQUIREDFILE
                                    .replace("[import]",imp.getName()));
                            return;
                        }
                    }
                    sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_IMPORT_START
                            .replace("[import]",imp.getName()));
                    imp.importData(sender,file);
                    sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_IMPORT_FINISH
                            .replace("[import]",imp.getName()));
                    return;
                }
            }
        }
        int page = 1;
        if(args.length >= 1 && GeneralUtil.isNumber(args[0])) page = Integer.valueOf(args[0]);
        sendHelp(sender,HelpType.MAIN);
    }
    private void sendPermissionData(PermissionCommandSender sender, PermissionData data){
        sender.sendMessage(Messages.PERMISSION_LIST_GLOBAL_HEADER);
        for(PermissionEntity entity : data.getPermissions()){
            if(entity.hasTimeOut()) continue;
            sender.sendMessage(Messages.PERMISSION_LIST_GLOBAL_FORMAT.replace("[permission]",entity.getPermission())
                    .replace("[timeout]",entity.getTimeOutDate()));
        }
        if(data.getWorldPermissions().size() > 0) {
            sender.sendMessage(Messages.PERMISSION_LIST_WORLD_HEADER);
            for(Map.Entry<String,List<PermissionEntity>> entry : data.getWorldPermissions().entrySet()){
                sender.sendMessage(Messages.PERMISSION_LIST_WORLD_WORLD.replace("[world]",entry.getKey()));
                for(PermissionEntity entity : entry.getValue()){
                    if(entity.hasTimeOut()) continue;
                    sender.sendMessage(Messages.PERMISSION_LIST_WORLD_FORMAT.replace("[permission]",entity.getPermission())
                            .replace("[timeout]",entity.getTimeOutDate()));
                }
            }
        }
        sender.sendMessage("");
        if(ADVANCED){
            if(!data.getServerPermissions().isEmpty()){
                sender.sendMessage(Messages.PERMISSION_LIST_SERVER_HEADER);
                for(Map.Entry<String,SimplePermissionData> entry : data.getServerPermissions().entrySet()){
                    sender.sendMessage(Messages.PERMISSION_LIST_SERVER_SERVER.replace("[server]",entry.getKey()));
                    for(PermissionEntity entity : entry.getValue().getPermissions()){
                        if(entity.hasTimeOut()) continue;
                        sender.sendMessage(Messages.PERMISSION_LIST_SERVER_FORMAT.replace("[permission]",entity.getPermission())
                                .replace("[timeout]",entity.getTimeOutDate()));
                    }
                    if(entry.getValue().getWorldPermissions().size() > 0){
                        sender.sendMessage(Messages.PERMISSION_LIST_SERVER_WORLD_HEADER);
                        for(Map.Entry<String,List<PermissionEntity>> entry2 : entry.getValue().getWorldPermissions().entrySet()){
                            sender.sendMessage(Messages.PERMISSION_LIST_SERVER_WORLD_WORLD.replace("[world]",entry2.getKey()));
                            for(PermissionEntity entity : entry2.getValue()){
                                if(entity.hasTimeOut()) continue;
                                sender.sendMessage(Messages.PERMISSION_LIST_SERVER_WORLD_FORMAT.replace("[permission]",entity.getPermission())
                                        .replace("[timeout]",entity.getTimeOutDate()));
                            }
                        }
                    }
                }
                sender.sendMessage("");
            }
            if(!data.getGroupPermissions().isEmpty()){
                sender.sendMessage(Messages.PERMISSION_LIST_SERVERGROUP_HEADER);
                for(Map.Entry<String,SimplePermissionData> entry : data.getGroupPermissions().entrySet()){
                    sender.sendMessage(Messages.PERMISSION_LIST_SERVERGROUP_SERVERGROUP.replace("[servergroup]",entry.getKey()));
                    for(PermissionEntity entity : entry.getValue().getPermissions()){
                        if(entity.hasTimeOut()) continue;
                        sender.sendMessage(Messages.PERMISSION_LIST_SERVERGROUP_FORMAT.replace("[permission]",entity.getPermission())
                                .replace("[timeout]",entity.getTimeOutDate()));
                    }
                    if(entry.getValue().getWorldPermissions().size() > 0){
                        sender.sendMessage(Messages.PERMISSION_LIST_SERVERGROUP_WORLD_HEADER);
                        for(Map.Entry<String,List<PermissionEntity>> entry2 : entry.getValue().getWorldPermissions().entrySet()){
                            sender.sendMessage(Messages.PERMISSION_LIST_SERVERGROUP_WORLD_WORLD.replace("[world]",entry2.getKey()));
                            for(PermissionEntity entity : entry2.getValue()){
                                if(entity.hasTimeOut()) continue;
                                sender.sendMessage(Messages.PERMISSION_LIST_SERVERGROUP_WORLD_FORMAT.replace("[permission]",entity.getPermission())
                                        .replace("[timeout]",entity.getTimeOutDate()));
                            }
                        }
                    }
                }
            }
        }
    }
    @Override
    public List<String> tabcomplete(PermissionCommandSender sender, String[] args) {
        /*if(args.length == 1){
            String search = args[0].toLowerCase();
            List<String> list = new LinkedList<>();*/
        return new LinkedList<>();
    }
    private void sendHelp(PermissionCommandSender sender, HelpType type){
        if(type == HelpType.MAIN){
            for(String message : Messages.PERMISSION_HELP_MAIN){
                sender.sendMessage(PermissionSystem.getInstance().getPlatform().translateColorCodes(message));
            }
        }else if(type == HelpType.GROUP){
            for(String message : Messages.PERMISSION_HELP_GROUP_ABOVE){
                if(!ADVANCED && message.contains("<-advanced->")) continue;
                if(!TSGROUP && message.contains("<-tsgroup->")) continue;
                if(!JOINPOWER && message.contains("<-joinpower->")) continue;
                sender.sendMessage(PermissionSystem.getInstance().getPlatform().translateColorCodes(message)
                        .replace("<-advanced->","").replace("<-tsgroup->","")
                        .replace("<-joinpower->",""));
            }
            sendPermissionHelp(sender);
            for(String message : Messages.PERMISSION_HELP_GROUP_BELOW){
                if(!ADVANCED && message.contains("<-advanced->")) continue;
                if(!TSGROUP && message.contains("<-tsgroup->")) continue;
                if(!JOINPOWER && message.contains("<-joinpower->")) continue;
                sender.sendMessage(PermissionSystem.getInstance().getPlatform().translateColorCodes(message)
                        .replace("<-advanced->","").replace("<-tsgroup->","")
                        .replace("<-joinpower->",""));
            }
        }else if(type == HelpType.PLAYER){
            for(String message : Messages.PERMISSION_HELP_PLAYER_ABOVE){
                if(!ADVANCED && message.contains("<-advanced->")) continue;
                if(!TSGROUP && message.contains("<-tsgroup->")) continue;
                if(!JOINPOWER && message.contains("<-joinpower->")) continue;
                sender.sendMessage(PermissionSystem.getInstance().getPlatform().translateColorCodes(message)
                        .replace("<-advanced->","").replace("<-tsgroup->","")
                        .replace("<-joinpower->",""));
            }
            sendPermissionHelp(sender);
            for(String message : Messages.PERMISSION_HELP_PLAYER_BELOW){
                if(!ADVANCED && message.contains("<-advanced->")) continue;
                if(!TSGROUP && message.contains("<-tsgroup->")) continue;
                if(!JOINPOWER && message.contains("<-joinpower->")) continue;
                sender.sendMessage(PermissionSystem.getInstance().getPlatform().translateColorCodes(message)
                        .replace("<-advanced->","").replace("<-tsgroup->","")
                        .replace("<-joinpower->",""));
            }
        }
    }
    private void sendPermissionHelp(PermissionCommandSender sender){
        for(String message : Messages.PERMISSION_HELP_PERMISSION){
            if(!ADVANCED && message.contains("<-advanced->")) continue;
            if(!TSGROUP && message.contains("<-tsgroup->")) continue;
            if(!JOINPOWER && message.contains("<-joinpower->")) continue;
            sender.sendMessage(PermissionSystem.getInstance().getPlatform().translateColorCodes(message)
                    .replace("<-advanced->","").replace("<-tsgroup->","")
                    .replace("<-joinpower->",""));
        }
    }
    public static DurationResult getDuration(Long duration, String value){
        TimeUnit unit = TimeUnit.DAYS;
        if(value != null && !(value.equalsIgnoreCase(""))){
            try{
                unit = TimeUnit.valueOf(value.toUpperCase());
            }catch (Exception exception){
                if(value.equalsIgnoreCase("month") || value.equalsIgnoreCase("months")
                        || value.equalsIgnoreCase("monate") || value.equalsIgnoreCase("monat")) duration = duration*30;
                else if(value.equalsIgnoreCase("year") || value.equalsIgnoreCase("years")
                        || value.equalsIgnoreCase("jahr") || value.equalsIgnoreCase("jahre")
                        || value.equalsIgnoreCase("y")) duration = duration*360;
                else if(value.equalsIgnoreCase("day") || value.equalsIgnoreCase("tag") || value.equalsIgnoreCase("tage")) unit = TimeUnit.DAYS;
                else if(value.equalsIgnoreCase("hour") || value.equalsIgnoreCase("stunde") || value.equalsIgnoreCase("stunden")) unit = TimeUnit.HOURS;
                else if(value.equalsIgnoreCase("minute") || value.equalsIgnoreCase("minuten") || value.equalsIgnoreCase("minute")) unit = TimeUnit.MINUTES;
                else if(value.equalsIgnoreCase("second") || value.equalsIgnoreCase("sekunde") || value.equalsIgnoreCase("sekunden")) unit = TimeUnit.SECONDS;
            }
        }
        return new DurationResult(duration,unit);
    }
    public static class DurationResult {
        private Long duration;
        private TimeUnit unit;

        public DurationResult(Long duration, TimeUnit unit) {
            this.duration = duration;
            this.unit = unit;
        }
        public Long getDuration() {
            return duration;
        }
        public TimeUnit getUnit() {
            return unit;
        }
        public void setDuration(Long duration) {
            this.duration = duration;
        }
        public void setUnit(TimeUnit unit) {
            this.unit = unit;
        }
    }
    private enum HelpType {
        MAIN(),
        PLAYER(),
        GROUP();
    }
}

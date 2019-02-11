package ch.dkrieger.permissionsystem.lib.command.defaults;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 01.07.18 19:38
 *
 */

import ch.dkrieger.permissionsystem.lib.command.PermissionCommand;
import ch.dkrieger.permissionsystem.lib.command.PermissionCommandSender;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupEntity;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import ch.dkrieger.permissionsystem.lib.utils.Messages;
import ch.dkrieger.permissionsystem.lib.utils.GeneralUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.LinkedList;
import java.util.List;

public class CommandRank extends PermissionCommand{

    public CommandRank() {
        super("rank","dkperms.rank.see","rang","ranks");
    }
    @Override
    public void execute(PermissionCommandSender sender, String[] args) {
        if(args.length == 1 && sender.hasPermission("dkperms.rank.see.others")){
            if(args[0].equalsIgnoreCase("list") && sender.hasPermission("dkperms.rank.change")){
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
            }
            PermissionPlayer player = PermissionPlayerManager.getInstance().getPermissionPlayer(args[0]);
            if(player == null){
                sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_PLAYER_NOTFOUND);
                return;
            }
            sender.sendMessage(Messages.PREFIX+Messages.RANK_LIST_OTHER.replace("[player]",player.getColor()+player.getName()));
            for(PermissionGroupEntity entity : player.getSortedGroupEntities()){
                PermissionGroup group = entity.getGroup();
                if(group == null) continue;
                sender.sendMessage(Messages.RANK_LIST_FORMAT.replace("[color]",group.getPlayerDesign().getColor())
                        .replace("[display]",group.getPlayerDesign().getDisplay())
                        .replace("[prefix]",group.getPlayerDesign().getPrefix())
                        .replace("[suffix]",group.getPlayerDesign().getSuffix())
                        .replace("[description]",group.getDescription())
                        .replace("[name]",group.getName())
                        .replace("[timeout]",entity.getTimeOutDate())
                        .replace("[priority]",""+group.getPriority()));
            }
            sender.sendMessage("");
            return;
        }else if(args.length >= 1 && sender.hasPermission("dkperms.rank.change")){
            if(args.length >= 2){
                PermissionPlayer player = PermissionPlayerManager.getInstance().getPermissionPlayer(args[0]);
                if(player == null){
                    sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_PLAYER_NOTFOUND.replace("[player]",args[0]));
                    return;
                }
                if(!sender.hasPermission("dkperms.rank.change.all")){
                    if(sender.getUUID() != null){
                        PermissionGroup pgroup = player.getHighestGroup();
                        if(pgroup != null){
                            PermissionPlayer permsender = PermissionPlayerManager.getInstance().getPermissionPlayer(sender.getUUID());
                            if(permsender != null){
                                PermissionGroup permgroup = permsender.getHighestGroup();
                                System.out.print(pgroup.getPriority()+" | "+permgroup.getPriority());
                                if(pgroup.getPriority() < permgroup.getPriority()){
                                    sender.sendMessage(Messages.PREFIX+Messages.RANK_NOPERMISSION_FOR_PLAYER
                                            .replace("[player]",player.getColor()+player.getName()));
                                    return;
                                }
                            }else{
                                System.out.print("sender null");
                                sender.sendMessage(Messages.PREFIX+Messages.RANK_NOPERMISSION_FOR_PLAYER
                                        .replace("[player]",player.getColor()+player.getName()));
                                return;
                            }
                        }
                    }
                }
                if(args[1].equalsIgnoreCase("demote")){
                    if(sender.hasPermission("dkperms.rank.demote")){
                        CommandPermission.DurationResult duration = new CommandPermission.DurationResult(-1L,null);
                        if(args.length >= 3 && GeneralUtil.isNumber(args[2])){
                            if(args.length >= 4) duration = CommandPermission.getDuration(Long.valueOf(args[2]),args[3]);
                            else duration = CommandPermission.getDuration(Long.valueOf(args[2]),"");
                        }
                        PermissionGroup group = player.demote(duration.getDuration(),duration.getUnit());
                        if(group == null){
                            sender.sendMessage(Messages.PREFIX+Messages.RANK_NOFOUND);
                            return;
                        }
                        if(duration.getDuration() <= 0){
                            sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_PLAYER_GROUP_SET
                                    .replace("[player]",player.getColor()+player.getName()).replace("[group]",group.getColor()+group.getName()));
                        }else{
                            sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_PLAYER_GROUP_ADD
                                    .replace("[player]",player.getColor()+player.getName()).replace("[group]",group.getColor()+group.getName()));
                        }
                    }else sender.sendMessage(Messages.PREFIX+Messages.NOPERMISSIONS);
                }else if(args[1].equalsIgnoreCase("promote")){
                    if(sender.hasPermission("dkperms.rank.promote")){
                        CommandPermission.DurationResult duration = new CommandPermission.DurationResult(-1L,null);
                        if(args.length >= 3 && GeneralUtil.isNumber(args[2])){
                            if(args.length >= 4) duration = CommandPermission.getDuration(Long.valueOf(args[2]),args[3]);
                            else duration = CommandPermission.getDuration(Long.valueOf(args[2]),"");
                        }
                        PermissionGroup group = player.promote(duration.getDuration(),duration.getUnit());
                        if(group == null){
                            sender.sendMessage(Messages.PREFIX+Messages.RANK_NOFOUND);
                            return;
                        }
                        if(duration.getDuration() <= 0){
                            sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_PLAYER_GROUP_SET
                                    .replace("[player]",player.getColor()+player.getName()).replace("[group]",group.getColor()+group.getName()));
                        }else{
                            sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_PLAYER_GROUP_ADD
                                    .replace("[player]",player.getColor()+player.getName()).replace("[group]",group.getColor()+group.getName()));
                        }
                    }else sender.sendMessage(Messages.NOPERMISSIONS);
                }else if(args.length >= 3){
                    PermissionGroup group = PermissionGroupManager.getInstance().getGroup(args[2]);
                    if(group == null){
                        sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_GROUP_NOTFOUND.replace("[group]",args[2]));
                        return;
                    }
                    if(!sender.hasPermission("dkperms.rank.change."+group.getName().toLowerCase())){
                        sender.sendMessage(Messages.PREFIX+Messages.RANK_NOPERMISSION_FOR_RANK
                                .replace("[group]",group.getColor()+group.getName()));
                        return;
                    }
                    CommandPermission.DurationResult duration = new CommandPermission.DurationResult(-1L,null);
                    if(args.length >= 4 && GeneralUtil.isNumber(args[3])){
                        if(args.length >= 5) duration = CommandPermission.getDuration(Long.valueOf(args[3]),args[4]);
                        else duration = CommandPermission.getDuration(Long.valueOf(args[3]),"");
                    }
                    if(args[1].equalsIgnoreCase("set")){
                        sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_PLAYER_GROUP_SET
                                .replace("[player]",player.getColor()+player.getName()).replace("[group]",group.getColor()+group.getName()));
                        player.setGroup(group,duration.getDuration(),duration.getUnit());
                    }else if(args[1].equalsIgnoreCase("add")){
                        sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_PLAYER_GROUP_ADD
                                .replace("[player]",player.getColor()+player.getName()).replace("[group]",group.getColor()+group.getName()));
                        player.addGroup(group,duration.getDuration(),duration.getUnit());
                    }else if(args[1].equalsIgnoreCase("remove")){
                        sender.sendMessage(Messages.PREFIX+Messages.PERMISSION_PLAYER_GROUP_REMOVE
                                .replace("[player]",player.getColor()+player.getName()).replace("[group]",group.getColor()+group.getName()));
                        player.removeGroup(group);
                    }else for(String message : Messages.RANK_HELP) sender.sendMessage(message);
                }else for(String message : Messages.RANK_HELP) sender.sendMessage(message);
            }else for(String message : Messages.RANK_HELP) sender.sendMessage(message);
            return;
        }
        PermissionPlayer player = PermissionPlayerManager.getInstance().getPermissionPlayer(sender.getUUID());
        sender.sendMessage(Messages.PREFIX+Messages.RANK_LIST_MY);
        for(PermissionGroupEntity entity : player.getSortedGroupEntities()){
            PermissionGroup group = entity.getGroup();
            if(group == null) continue;
            sender.sendMessage(Messages.RANK_LIST_FORMAT.replace("[color]",group.getPlayerDesign().getColor())
                    .replace("[display]",group.getPlayerDesign().getDisplay())
                    .replace("[prefix]",group.getPlayerDesign().getPrefix())
                    .replace("[suffix]",group.getPlayerDesign().getSuffix())
                    .replace("[description]",group.getDescription())
                    .replace("[name]",group.getName())
                    .replace("[timeout]",entity.getTimeOutDate())
                    .replace("[priority]",""+group.getPriority()));
        }
        sender.sendMessage("");
    }
    @Override
    public List<String> tabcomplete(PermissionCommandSender sender, String[] args) {
        return new LinkedList<>();
    }
}

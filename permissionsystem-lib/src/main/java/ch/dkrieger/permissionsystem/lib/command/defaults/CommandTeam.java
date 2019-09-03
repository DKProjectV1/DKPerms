package ch.dkrieger.permissionsystem.lib.command.defaults;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 01.07.18 19:38
 *
 */

import ch.dkrieger.permissionsystem.lib.command.PermissionCommand;
import ch.dkrieger.permissionsystem.lib.command.PermissionCommandSender;
import ch.dkrieger.permissionsystem.lib.config.Config;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.utils.Messages;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommandTeam extends PermissionCommand{

    private static boolean forceUpdate;

    private List<String> messages;
    private long lastupdate;

    public CommandTeam() {
        super(Config.COMMAND_TEAM_NAME,Config.COMMAND_TEAM_PERMISSION,Config.COMMAND_TEAM_ALIASES);
        this.messages = new LinkedList<>();
        forceUpdate = true;
    }

    @Override
    public void execute(PermissionCommandSender sender, String[] args) {
        if(args.length >= 1 && args[0].equalsIgnoreCase("reload")) load();
        else{
            if(forceUpdate) load();
            else if(lastupdate+TimeUnit.MINUTES.toMillis(10) < System.currentTimeMillis()) load();
        }
        for(String message : this.messages) sender.sendMessage(message);
    }

    private void load(){
        this.messages.clear();
        this.messages.add(Messages.TEAM_LIST_HEADER);
        for(PermissionGroup group : PermissionGroupManager.getInstance().getSortedGroups()){
            if(group.isTeam()) {
                this.messages.add(Messages.TEAM_LIST_FORMAT_GROUP
                        .replace("[color]", group.getPlayerDesign().getColor())
                        .replace("[prefix]", group.getPlayerDesign().getColor())
                        .replace("[display]", group.getPlayerDesign().getDisplay())
                        .replace("[suffix]", group.getPlayerDesign().getDisplay())
                        .replace("[priority]", "" + group.getPriority())
                        .replace("[name]", group.getName())
                        .replace("[description]",group.getDescription()));
                for(PermissionPlayer player : group.getPlayers()){
                    if(player == null) continue;
                    PermissionGroup playergroup = player.getHighestGroup();
                    this.messages.add(Messages.TEAM_LIST_FORMAT_PLAYER
                            .replace("[group_color]", group.getPlayerDesign().getColor())
                            .replace("[group_prefix]", group.getPlayerDesign().getColor())
                            .replace("[group_display]", group.getPlayerDesign().getDisplay())
                            .replace("[group_suffix]", group.getPlayerDesign().getDisplay())
                            .replace("[group_priority]", "" + group.getPriority())
                            .replace("[group_name]", group.getName())
                            .replace("[group_description]",group.getDescription())
                            .replace("[color]",playergroup.getPlayerDesign().getColor())
                            .replace("[prefix]",playergroup.getPlayerDesign().getPrefix())
                            .replace("[suffix]",playergroup.getPlayerDesign().getSuffix())
                            .replace("[display]",playergroup.getPlayerDesign().getDisplay())
                            .replace("[player]",player.getName()));
                }
                this.messages.add(Messages.TEAM_LIST_BETWEEN);
            }
        }
        this.messages.remove(this.messages.size()-1);
        this.messages.add(Messages.TEAM_LIST_FOOTER);
        forceUpdate = false;
        this.lastupdate = System.currentTimeMillis();
    }

    @Override
    public List<String> tabComplete(PermissionCommandSender sender, String[] args) {
        return new LinkedList<>();
    }

    public static void forceUpdate(){
        forceUpdate = true;
    }
}

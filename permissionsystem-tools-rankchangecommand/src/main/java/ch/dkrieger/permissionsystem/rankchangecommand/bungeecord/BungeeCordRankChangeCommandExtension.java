package ch.dkrieger.permissionsystem.rankchangecommand.bungeecord;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 11.02.19 11:36
 *
 */

import ch.dkrieger.permissionsystem.bungeecord.event.ProxiedPermissionPlayerUpdateEvent;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdateCause;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdateData;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdateOption;
import ch.dkrieger.permissionsystem.rankchangecommand.RankChangeCommandConfig;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.List;
import java.util.UUID;

public class BungeeCordRankChangeCommandExtension extends Plugin implements Listener {

    private RankChangeCommandConfig config;

    @Override
    public void onEnable() {
        BungeeCord.getInstance().getPluginManager().registerListener(this,this);
        config = new RankChangeCommandConfig();
    }

    @EventHandler
    public void onUpdate(ProxiedPermissionPlayerUpdateEvent event) {
        if(event.getData() != null && event.getData().getCause() != null && event.getData().getCause() == PermissionUpdateCause.GROUP
                && event.getData().getValue() != null) {
            PermissionGroup group = PermissionGroupManager.getInstance().getGroup(UUID.fromString(event.getData().getValue()));
            if(group != null){
                if(event.getData().getOption() == PermissionUpdateOption.SET || event.getData().getOption() == PermissionUpdateOption.ADD){
                    executeCommands(config.commandsAdd,event.getPlayer(),group, event.getData());
                }else if(event.getData().getOption() == PermissionUpdateOption.REMOVE){
                    executeCommands(config.commandsRemove,event.getPlayer(),group, event.getData());
                }
            }
        }
    }
    private void executeCommands(List<String> commands, PermissionPlayer player,PermissionGroup group, PermissionUpdateData updateData){
        for(String command : commands){
            BungeeCord.getInstance().getPluginManager().dispatchCommand(BungeeCord.getInstance().getConsole()
                    ,command.replace("[player-name]",player.getName()).replace("[player-uuid]",player.getUUID().toString())
                            .replace("[player-id]",String.valueOf(player.getID())).replace("[duration]",String.valueOf(updateData.getDuration()))
                            .replace("[rank]",group.getName()));
        }
    }
}

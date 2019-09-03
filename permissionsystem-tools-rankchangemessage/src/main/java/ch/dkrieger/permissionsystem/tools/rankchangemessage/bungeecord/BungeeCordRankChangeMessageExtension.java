package ch.dkrieger.permissionsystem.tools.rankchangemessage.bungeecord;

import ch.dkrieger.permissionsystem.bungeecord.event.ProxiedPermissionPlayerUpdateEvent;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdateCause;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdateOption;
import ch.dkrieger.permissionsystem.lib.utils.Messages;
import ch.dkrieger.permissionsystem.tools.rankchangemessage.RankChangeMessageConfig;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 02.08.18 15:49
 *
 */

public class BungeeCordRankChangeMessageExtension extends Plugin implements Listener {

    @Override
    public void onEnable() {
        BungeeCord.getInstance().getPluginManager().registerListener(this,this);
        new RankChangeMessageConfig();
    }

    @EventHandler
    public void onUpdate(ProxiedPermissionPlayerUpdateEvent event) {
        if(event.getData() != null && event.getData().getCause() != null && event.getData().getCause() == PermissionUpdateCause.GROUP
        && event.getData().getValue() != null) {
            ProxiedPlayer player = BungeeCord.getInstance().getPlayer(event.getUUID());
            if(player == null) return;
            PermissionGroup group = PermissionGroupManager.getInstance().getGroup(UUID.fromString(event.getData().getValue()));
            if(group != null){
                if(event.getData().getOption() == PermissionUpdateOption.SET){
                    String message = null;
                    if(event.getData().getDuration() > 0) message = RankChangeMessageConfig.RANK_SET_TEMPORARY;
                    else message = RankChangeMessageConfig.RANK_SET_PERMANENT;
                    message = message.replace("[duration]",""+event.getData().getDuration())
                            .replace("[unit]",Messages.getMessage(event.getData().getDuration(),event.getData().getUnit()))
                            .replace("[group]",group.getColor()+group.getName());
                    player.sendMessage(Messages.PREFIX+message);
                }else if(event.getData().getOption() == PermissionUpdateOption.ADD){
                    String message = null;
                    if(event.getData().getDuration() > 0) message = RankChangeMessageConfig.RANK_ADD_TEMPORARY;
                    else message = RankChangeMessageConfig.RANK_ADD_PERMANENT;
                    message = message.replace("[duration]",""+event.getData().getDuration())
                            .replace("[unit]",Messages.getMessage(event.getData().getDuration(),event.getData().getUnit()))
                            .replace("[group]",group.getColor()+group.getName());
                    player.sendMessage(Messages.PREFIX+message);
                }else if(event.getData().getOption() == PermissionUpdateOption.REMOVE){
                    String message = RankChangeMessageConfig.RANK_REMOVE;
                    message = message.replace("[duration]",""+event.getData().getDuration())
                            .replace("[unit]",Messages.getMessage(event.getData().getDuration(),event.getData().getUnit()))
                            .replace("[group]",group.getColor()+group.getName());
                    player.sendMessage(Messages.PREFIX+message);
                }else if(event.getData().getOption() == PermissionUpdateOption.CLEAR){
                    String message = RankChangeMessageConfig.RANK_CLEAR;
                    message = message.replace("[duration]",""+event.getData().getDuration())
                            .replace("[unit]",Messages.getMessage(event.getData().getDuration(),event.getData().getUnit()))
                            .replace("[group]",group.getColor()+group.getName());
                    player.sendMessage(Messages.PREFIX+message);
                }
            }
        }
    }
}

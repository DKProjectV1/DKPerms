package ch.dkrieger.permissionsystem.tools.rankchangemessage.bukkit;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 02.08.18 14:45
 *
 */

import ch.dkrieger.permissionsystem.bukkit.event.BukkitPermissionPlayerUpdateEvent;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdateCause;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdateOption;
import ch.dkrieger.permissionsystem.lib.utils.Messages;
import ch.dkrieger.permissionsystem.tools.rankchangemessage.RankChangeMessageConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class BukkitRankChangeMessageExtension extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this,this);
        new RankChangeMessageConfig();
    }
    @EventHandler
    public void onUpdate(BukkitPermissionPlayerUpdateEvent event) {
        if(event.getData() != null && event.getData().getCause() != null && event.getData().getCause() == PermissionUpdateCause.GROUP
        && event.getData().getValue() != null) {
            Player player = Bukkit.getPlayer(event.getUUID());
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

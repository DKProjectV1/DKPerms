package ch.dkrieger.permissionsystem.tools.rankchangemessage;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 02.08.18 14:44
 *
 */

import ch.dkrieger.permissionsystem.lib.PermissionSystem;
import ch.dkrieger.permissionsystem.lib.config.MessageConfig;

import java.util.concurrent.TimeUnit;

public class RankChangeMessageConfig {

    public static String RANK_SET_PERMANENT;
    public static String RANK_SET_TEMPORARY;
    public static String RANK_ADD_PERMANENT;
    public static String RANK_ADD_TEMPORARY;
    public static String RANK_REMOVE;
    public static String RANK_CLEAR;

    public RankChangeMessageConfig() {
        PermissionSystem.getInstance().getPlatform().getTaskManager().runTaskLater(new Runnable() {
            public void run() {
                MessageConfig config = PermissionSystem.getInstance().getMessageConfig();

                config.addValue("extension.rankchangemessage.set.permanent","&7Your rank was set to &e[group]&7.");
                config.addValue("extension.rankchangemessage.set.temporary","&7Your rank was set to &e[group] &7for &e[duration] &4[unit].");

                config.addValue("extension.rankchangemessage.add.permanent","&7The rank &e[group] &7was added to your.");
                config.addValue("extension.rankchangemessage.add.temporary","&7The rank &e[group] &7was added for &e[duration] &4[unit] &7to your.");

                config.addValue("extension.rankchangemessage.remove","&7The rank &e[group] &7was removed from your.");
                config.addValue("extension.rankchangemessage.clear","&7Your ranks have been removed.");
                config.save();

                RANK_SET_PERMANENT = config.get("extension.rankchangemessage.set.permanent");
                RANK_SET_TEMPORARY = config.get("extension.rankchangemessage.set.temporary");

                RANK_ADD_PERMANENT= config.get("extension.rankchangemessage.add.permanent");
                RANK_ADD_TEMPORARY= config.get("extension.rankchangemessage.add.temporary");

                RANK_REMOVE = config.get("extension.rankchangemessage.remove");
                RANK_CLEAR = config.get("extension.rankchangemessage.clear");
            }
        },2L, TimeUnit.SECONDS);
    }
}

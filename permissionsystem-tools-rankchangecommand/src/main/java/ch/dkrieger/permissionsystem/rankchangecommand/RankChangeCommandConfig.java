package ch.dkrieger.permissionsystem.rankchangecommand;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 11.02.19 11:35
 *
 */

import ch.dkrieger.permissionsystem.lib.PermissionSystem;
import ch.dkrieger.permissionsystem.lib.config.Config;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RankChangeCommandConfig {

    public List<String> commandsRemove, commandsAdd;

    public RankChangeCommandConfig() {
        PermissionSystem.getInstance().getPlatform().getTaskManager().runTaskLater(() -> {
            Config config = PermissionSystem.getInstance().getConfig();

            config.addValue("extension.rankchangecommand.add",Collections.singletonList("mycommand add [player-name] [player-uuid] [player-id] [rank] [duration]"));
            config.addValue("extension.rankchangecommand.remove",Collections.singletonList("mycommand remove [player-name] [player-uuid] [player-id] [rank] [duration]"));
            config.save();

            commandsRemove = config.getStringListValue("extension.rankchangecommand.add");
            commandsAdd = config.getStringListValue("extension.rankchangecommand.remove");
        },2L, TimeUnit.SECONDS);
    }
}

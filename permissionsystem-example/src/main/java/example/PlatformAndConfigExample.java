package example;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 15.07.18 17:12
 *
 */

import ch.dkrieger.permissionsystem.lib.PermissionSystem;
import ch.dkrieger.permissionsystem.lib.command.PermissionCommand;
import ch.dkrieger.permissionsystem.lib.command.PermissionCommandSender;
import ch.dkrieger.permissionsystem.lib.config.SimpleConfig;
import ch.dkrieger.permissionsystem.lib.platform.DKPermsPlatform;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlatformAndConfigExample {

    public PlatformAndConfigExample() {
        DKPermsPlatform platform = PermissionSystem.getInstance().getPlatform();
        platform.getTaskManager(); //get the taskmanager from a platform
        platform.getCommandManager(); //register a permission command

        //intern config

        //register a command
        PermissionSystem.getInstance().getPlatform().getCommandManager().registerCommand(new command());

        //register a task
        PermissionSystem.getInstance().getPlatform().getTaskManager().runTaskAsync(new Runnable() {
            public void run() {
                System.out.println("I am Async");
            }
        });
        PermissionSystem.getInstance().getPlatform().getTaskManager().scheduleTask(new Runnable() {
            public void run() {
                System.out.println("Hey ");
            }
        },2L, TimeUnit.MINUTES);
    }

    public static class config extends SimpleConfig {

        public config() {
            super(PermissionSystem.getInstance().getPlatform(),
                    new File(PermissionSystem.getInstance().getPlatform().getFolder().getPath()+"/extension/","config.yml"));
        }

        public void onLoad() {
            boolean enabled = getBooleanValue("enabled");
        }

        public void registerDefaults() {
            addValue("enabled",true);
        }
    }

    public static class command extends PermissionCommand {

        public command() {
            super("test","test.use","test2","test3");
        }

        public void execute(PermissionCommandSender sender, String[] args) {
            sender.sendMessage("Hey "+sender.getName());
        }

        @Override
        public List<String> tabComplete(PermissionCommandSender sender, String[] args) {
            return null;
        }
    }
}

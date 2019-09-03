package ch.dkrieger.permissionsystem.lib.storage.mysql.table;
import ch.dkrieger.permissionsystem.lib.storage.mysql.MySQL;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 20:43
 *
 */

public class TableManager {

    private static TableManager INSTANCE;

    private Table players;

    private Table perms_groups;
    private Table perms_permissions;
    private Table perms_entity;

    public TableManager(MySQL mysql){
        INSTANCE = this;
        String systemname = mysql.getSystemName().toLowerCase();

        this.players = new Table(mysql,systemname+"_players");

        this.perms_groups = new Table(mysql,systemname+"_groups");
        this.perms_entity = new Table(mysql,systemname+"_entity");
        this.perms_permissions = new Table(mysql,systemname+"_permissions");

        create();
    }
    public Table getPlayerTable(){
        return this.players;
    }

    public Table getGroupTable(){
        return this.perms_groups;
    }

    public Table getPermissionTable(){
        return this.perms_permissions;
    }

    public Table getEntityTable(){
        return this.perms_entity;
    }

    private void create(){
        this.players.create().create("`id` int(30) NOT NULL AUTO_INCREMENT").create("`name` varchar(20) NOT NULL")
                .create("`uuid` varchar(120) NOT NULL").create("PRIMARY KEY (`id`)").execute();
        this.perms_groups.create().create("`uuid` varchar(120) NOT NULL").create("`name` varchar(20) NOT NULL")
                .create("`description` varchar(300) NOT NULL DEFAULT '-1'").create("`priority` int(10) NOT NULL DEFAULT '0'")
                .create("`default` varchar(10) NOT NULL DEFAULT 'false'")
                .create("`team` varchar(10) NOT NULL DEFAULT 'false'").create("`prefix` varchar(20) NOT NULL DEFAULT '-1'")
                .create("`suffix` varchar(20) NOT NULL DEFAULT '-1'").create("`display` varchar(40) NOT NULL DEFAULT '-1'")
                .create("`color` varchar(10) NOT NULL DEFAULT '-1'").create("`tsgroupid` int(10) NOT NULL DEFAULT '-1'")
                .create("`joinpower` int(111) NOT NULL DEFAULT '-1'").create("`created` varchar(40) NOT NULL")
                .create("PRIMARY KEY (`uuid`)").execute();
        this.perms_permissions.create().create("`id` int(30) NOT NULL AUTO_INCREMENT")
                .create("`permissiontype` varchar(20) NOT NULL").create("`uuid` varchar(120) NOT NULL")
                .create("`type` varchar(40) NOT NULL").create("`value` varchar(50) NOT NULL")
                .create("`world` varchar(100) NOT NULL").create("`permission` varchar(100) NOT NULL")
                .create("`timeout` varchar(40) NOT NULL").create("`created` varchar(40) NOT NULL").create("PRIMARY KEY (`id`)").execute();
        this.perms_entity.create().create("`id` int(30) NOT NULL AUTO_INCREMENT").create("`permissiontype` varchar(20) NOT NULL")
                .create("`uuid` varchar(120) NOT NULL").create("`groupuuid` varchar(120) NOT NULL")
                .create("`timeout` varchar(40) NOT NULL").create("`created` varchar(40) NOT NULL")
                .create("PRIMARY KEY (`id`)").execute();
        this.perms_groups.query().executeWithOutError("ALTER TABLE `"+perms_entity.getName()+"` ADD CONSTRAIN1771T `perms_group_connection` FOREIGN KEY (`groupuuid`) REFERENCES `"+perms_groups.getName()+"`(`uuid`) ON DELETE CASCADE ON UPDATE CASCADE;");
    }

    public static TableManager getInstance(){
        return INSTANCE;
    }
}

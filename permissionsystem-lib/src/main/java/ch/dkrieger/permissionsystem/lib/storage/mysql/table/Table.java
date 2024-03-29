package ch.dkrieger.permissionsystem.lib.storage.mysql.table;

import ch.dkrieger.permissionsystem.lib.storage.mysql.MySQL;
import ch.dkrieger.permissionsystem.lib.storage.mysql.query.*;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 20:43
 *
 */

public class Table {

    private String name;
    private MySQL mysql;

    public Table(MySQL mysql, String name){
        this.mysql = mysql;
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public CreateQuery create(){
        return new CreateQuery("CREATE TABLE IF NOT EXISTS `"+this.name+"` (");
    }

    public InsertQuery insert(){
        return new InsertQuery("INSERT INTO `"+this.name+"` (");
    }

    public UpdateQuery update(){
        return new UpdateQuery("UPDATE `"+this.name+"` SET");
    }

    public SelectQuery select(){
        return select("*");
    }

    public SelectQuery select(String selection){
        return new SelectQuery( "SELECT "+selection+" FROM `"+this.name+"`");
    }

    public DeleteQuery delete(){
        return new DeleteQuery( "DELETE FROM `"+this.name+"`");
    }

    public CustomQuery query(){
        return new CustomQuery();
    }
}

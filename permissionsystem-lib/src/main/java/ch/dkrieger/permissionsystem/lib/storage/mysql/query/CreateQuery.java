package ch.dkrieger.permissionsystem.lib.storage.mysql.query;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 20:43
 *
 */

public class CreateQuery extends Query {

    public CreateQuery(Connection connection, String query){
        super(connection, query);
        firstvalue = true;
    }

    public CreateQuery create(String value) {
        if(!firstvalue) query = query.substring(0,query.length()-1)+",";
        else firstvalue = false;
        query += value+")";
        return this;
    }

    public void execute(){
        try(Connection connection = this.connection) {
            Statement statement = connection.createStatement();
            statement.execute(query);
            statement.close();
        } catch(SQLException exception){
            exception.printStackTrace();
        }
    }
}

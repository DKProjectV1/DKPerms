package ch.dkrieger.permissionsystem.lib.storage.mysql.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 20:43
 *
 */

public class DeleteQuery extends Query {

    public DeleteQuery(Connection connection, String query) {
        super(connection, query);
        this.firstvalue = true;
    }

    public DeleteQuery where(String key, Object value) {
        if(and) query += " AND";
        else{
            query += " WHERE";
            and = true;
        }
        query += " "+key+"=";
        values.add(value);
        query += "?";
        return this;
    }

    public DeleteQuery whereLower(String key, Object value) {
        if(and) query += " AND";
        else{
            query += " WHERE";
            and = true;
        }
        query += " "+key+"<";
        values.add(value);
        query += "?";
        return this;
    }

    public DeleteQuery whereHigher(String key, Object value) {
        if(and) query += " AND";
        else{
            query += " WHERE";
            and = true;
        }
        query += " "+key+">";
        values.add(value);
        query += "?";
        return this;
    }

    public void execute() {
        try(Connection connection = this.connection) {
            PreparedStatement pstatement = connection.prepareStatement(query);
            int i = 1;
            for (Object object : values) {
                pstatement.setString(i,object.toString());
                i++;
            }
            pstatement.executeUpdate();
            pstatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

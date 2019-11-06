package ch.dkrieger.permissionsystem.lib.storage.mysql.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 20:43
 *
 */

public class UpdateQuery extends Query {

    public UpdateQuery(String query) {
        super( query);
    }

    public UpdateQuery set(String field, Object value) {
        if (comma) query += ",";
        query += " `"+field+"`=?";
        values.add(value);
        comma = true;
        return this;
    }

    public UpdateQuery where(String key, Object value) {
        if(and) query += " AND";
        else query += " WHERE";
        query +=" "+key+"=?";
        values.add(value);
        and = true;
        return this;
    }

    public void execute() {
        try(Connection connection = getConnection()) {
            PreparedStatement pstatement = connection.prepareStatement(query);
            int i = 1;
            for(Object object : values) {
                pstatement.setString(i, object.toString());
                i++;
            }
            pstatement.executeUpdate();
            pstatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

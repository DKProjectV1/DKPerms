package ch.dkrieger.permissionsystem.lib.storage.mysql.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 20:43
 *
 */

public class SelectQuery extends  Query{

    private PreparedStatement pstatement;
    private Connection connection;

    public SelectQuery(String query) {
        super( query);
    }

    public SelectQuery where(String key, Object value) {
        if(!and){
            query += " WHERE";
            and = true;
        }else query += " AND";
        query += " `"+key+"`=?";
        values.add(value);
        return this;
    }

    public SelectQuery whereWithOr(String key, Object value) {
        if(!and){
            query += " WHERE";
            and = true;
        }else query += " or";
        query += " `"+key+"`=?";
        values.add(value);
        return this;
    }

    public ResultSet execute() throws SQLException{
        connection = getConnection();
        pstatement = connection.prepareStatement(query);
        int i = 1;
        for (Object object : values) {
            pstatement.setString(i, object.toString());
            i++;
        }
        return pstatement.executeQuery();
    }

    public void close() throws SQLException {
        if(pstatement != null)  pstatement.close();
        if(connection != null) connection.close();
    }
}

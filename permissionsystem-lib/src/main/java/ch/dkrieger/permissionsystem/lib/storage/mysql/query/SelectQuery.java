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

    public SelectQuery(Connection connection, String query) {
        super(connection, query);
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
        pstatement = connection.prepareStatement(query);
        int i = 1;
        for (Object object : values) {
            pstatement.setString(i, object.toString());
            i++;
        }
        return pstatement.executeQuery();
    }

    public void close() throws SQLException {
        if(connection != null) connection.close();
        if(pstatement != null)  pstatement.close();
    }
}

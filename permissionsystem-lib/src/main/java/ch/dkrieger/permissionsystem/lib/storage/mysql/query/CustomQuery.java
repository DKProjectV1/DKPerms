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

public class CustomQuery extends Query{

    private PreparedStatement pstatement;

    public CustomQuery(Connection connection) {
        super(connection, "");
    }

    public void execute(){
        throw new UnsupportedOperationException("Not allowed in custom query");
    }
    public void execute(String query) {
        this.query = query;
        try(Connection connection = this.connection) {
            pstatement = connection.prepareStatement(query);
            int i = 1;
            for (Object object : values) {
                pstatement.setString(i, object.toString());
                i++;
            }
            pstatement.executeUpdate();
            pstatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public ResultSet executeAndGetResult(String query) throws SQLException{
        pstatement = connection.prepareStatement(query);
        int i = 1;
        for(Object object : values) {
            pstatement.setString(i, object.toString());
            i++;
        }
        ResultSet result = pstatement.executeQuery();
        return result;
    }
    public void executeSave(String query) throws SQLException{
        this.query = query;
        PreparedStatement pstatement;
        pstatement = connection.prepareStatement(query);
        int i = 1;
        for (Object object : values) {
            pstatement.setString(i, object.toString());
            i++;
        }
        pstatement.executeUpdate();
        pstatement.close();
        connection.close();
    }
    public void executeWithOutError(String query){
        try{ executeSave(query);
        }catch (Exception exception){}
    }
    public void close() throws SQLException{
        if(pstatement != null)  pstatement.close();
    }
}

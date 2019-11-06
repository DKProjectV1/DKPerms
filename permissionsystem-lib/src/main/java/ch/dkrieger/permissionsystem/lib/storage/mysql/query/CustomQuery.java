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

    public CustomQuery() {
        super(null);
    }

    public void execute(){
        throw new UnsupportedOperationException("Not allowed in custom query");
    }
    public void execute(String query) {
        this.query = query;
        try(Connection connection = getConnection()) {
            PreparedStatement pstatement = connection.prepareStatement(query);
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

    public void executeSave(String query) throws SQLException{
        this.query = query;
        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(query);
            int i = 1;
            for (Object object : values) {
                statement.setString(i, object.toString());
                i++;
            }
            statement.executeUpdate();
            statement.close();
        }
    }

    public void executeWithOutError(String query){
        try{ executeSave(query);
        }catch (Exception ignored){}
    }

    public void close() throws SQLException{
        getConnection().close();
    }
}

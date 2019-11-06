package ch.dkrieger.permissionsystem.lib.storage.mysql.query;

import ch.dkrieger.permissionsystem.lib.PermissionSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 20:43
 *
 */

public class QueryBuilder {

    private final List<Query> queries;
    private String query;

    public QueryBuilder(Query... queries){
        this.queries = new LinkedList<>();
        this.queries.addAll(Arrays.asList(queries));
    }

    public QueryBuilder append(Query query){
        this.queries.add(query);
        return this;
    }

    public QueryBuilder remove(Query query){
        this.queries.remove(query);
        return this;
    }

    public QueryBuilder build(){
        for(Query query : this.queries){
            if(this.query == null) this.query = query.toString();
            else this.query += ";"+query.toString();
        }
        return this;
    }

    public void execute(){
        if(queries.size() <= 0) return;
        try(Connection connection = PermissionSystem.getInstance().getMySQL().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            int i = 1;
            for(Query query : this.queries){
                for(Object object : query.getValues()) {
                    statement.setString(i,object.toString());
                    i++;
                }
            }
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void buildAndExecute(){
        build();
        execute();
    }
}

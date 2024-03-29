package ch.dkrieger.permissionsystem.lib.storage.mysql;

import ch.dkrieger.permissionsystem.lib.config.Config;
import ch.dkrieger.permissionsystem.lib.utils.Messages;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 20:43
 *
 */

public class MySQL{

	private HikariDataSource dataSource;

	private String systemName, host, port, user, password, database;

	public MySQL(String systemName, String host,int port,String user,String password,String database){
		this(systemName,host,String.valueOf(port),user,password,database);
	}

	public MySQL(String systemName, String host,String port,String user,String password,String database){
		this.systemName = systemName;
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
		this.database = database;
	}

	public void loadDriver(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch(ClassNotFoundException exception) {
			System.out.println(Messages.SYSTEM_PREFIX+"Could not load MySQL driver.");
		}
	}

	public boolean connect(){
		if(!isConnect()){
			loadDriver();

			System.out.println(Messages.SYSTEM_PREFIX+"connecting to MySQL server at "+this.host+":"+port);
			HikariConfig configuration = new HikariConfig();
			configuration.setJdbcUrl("jdbc:mysql://"+host+":"+port+"/"+database);
			configuration.setUsername(user);
			configuration.setPassword(password);
			configuration.setPoolName(getSystemName());

			configuration.addDataSourceProperty("cachePrepStmts",true);
			configuration.addDataSourceProperty("characterEncoding","utf-8");
			configuration.addDataSourceProperty("useUnicode",true);
			configuration.addDataSourceProperty("allowMultiQueries",true);

			configuration.addDataSourceProperty("ssl",Config.STORAGE_MYSQL_SSL);
			configuration.addDataSourceProperty("useSSL",Config.STORAGE_MYSQL_SSL);

			configuration.setMaximumPoolSize(Config.STORAGE_MYSQL_MAX_CONNECTIONS);

					this.dataSource = new HikariDataSource(configuration);
			System.out.println(Messages.SYSTEM_PREFIX+"successful connected to MySQL server at "+this.host+":"+port);
			return true;
		}
		return false;
	}

	public void disconnect(){
		if(isConnect()){
			this.dataSource.close();
			System.out.println(Messages.SYSTEM_PREFIX+"successful disconnected from MySQL server at "+this.host+":"+port);
		}
	}

	public boolean isConnect(){
		return dataSource != null && !this.dataSource.isClosed();
	}

	public Connection getConnection(){
		try {
			return this.dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getSystemName(){
		return this.systemName;
	}
}

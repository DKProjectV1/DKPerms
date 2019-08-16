package ch.dkrieger.permissionsystem.lib.storage.mysql;

import ch.dkrieger.permissionsystem.lib.utils.Messages;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 20:43
 *
 */

public class MySQL implements Runnable{

	private DataSource dataSource;

	private String systemname, host, port, user, password, database;
	private Connection conn;

	public MySQL(String systemname, String host,int port,String user,String password,String database){
		this(systemname,host,String.valueOf(port),user,password,database);
	}
	public MySQL(String systemname, String host,String port,String user,String password,String database){
		this.systemname = systemname;
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
			try {
				conn = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+database+"?autoReconnect=true&allowMultiQueries=true&reconnectAtTxEnd=true",user, password);
				System.out.println(Messages.SYSTEM_PREFIX+"successful connected to MySQL server at "+this.host+":"+port);
				return true;
			}catch (SQLException exception) {
				System.out.println(Messages.SYSTEM_PREFIX+"Could not connect to MySQL server at "+this.host+":"+port);
				System.out.println(Messages.SYSTEM_PREFIX+"Error: "+exception.getMessage());
				System.out.println(Messages.SYSTEM_PREFIX+"Check your login datas in the config.");
				conn = null;
			}
		}
		return false;
	}
	public void disconect(){
		if(isConnect()){
			try {
				conn.close();
				conn = null;
				System.out.println(Messages.SYSTEM_PREFIX+"successful disconnected from MySQL server at "+this.host+":"+port);
			}catch (SQLException exception) {
				conn = null;
			}
		}
	}
	public void reconnect(){
		disconect();
		connect();
	}
	public boolean isConnect(){
		if(conn == null) return false; else return true;
	}
	public Connection getConnecion(){
		return conn;
	}
	public String getSystemName(){
		return this.systemname;
	}
	@Override
	public void run() {
		disconect();
		connect();
	}
}

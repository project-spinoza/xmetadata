package org.projectspinoza.dd.xmetadata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class XmetaTest {
	private static Logger LOG = LogManager.getRootLogger();
	
	private Connection connection;
	private Properties settings;
	

	public XmetaTest(Properties settings) {
		this.settings = settings;
	}

	public boolean connect() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		LOG.debug("connecting to database: {}", settings);
		String db_url = getDbUrl();
		LOG.debug("db_url: {}", db_url);
		Class.forName(settings.getProperty("jdbc_driver")).newInstance();
        connection = DriverManager.getConnection(db_url, settings.getProperty("db_user"), settings.getProperty("db_pass"));
        LOG.debug("connection succeeded");
		return true;
	}
	
	public Connection getConnection(){
		return this.connection;
	}
	
	public void close() throws SQLException{
		connection.close();
		LOG.debug("Connection closed");
	}
	
	private String getDbUrl(){
		 return "jdbc:mysql://"
				 	+ settings.getProperty("db_host") + ":"
				 	+ settings.get("db_port") + "/" 
				 	+ settings.getProperty("db_name") + "?autoReconnect=true&useSSL=false";
	}

}

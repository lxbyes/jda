package me.leckie.jda.jdbc.impl;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

import me.leckie.jda.jdbc.ConnectionMng;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 最基本的jdbc使用,获取jdbc连接<br>
 * 
 * @author Leckie
 * @date 2014年12月30日
 */
public class DefaultConnectionMng implements ConnectionMng {

	Logger logger = LoggerFactory.getLogger(getClass());

	public Connection connect() {
		String user = "root";
		String password = "root";
		String url = "jdbc:mysql://localhost:3306/auth2";

		Connection con = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, user, password);
		} catch ( ClassNotFoundException e ) {
			logger.debug("加载驱动失败！");
			e.printStackTrace();
		} catch ( SQLException e ) {
			logger.debug("获取连接失败！");
			e.printStackTrace();
		}
		return con;
	}

	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub

	}

	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T unwrap(Class< T > iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isWrapperFor(Class< ? > iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public Connection getConnection() throws SQLException {
		return connect();
	}

	public Connection getConnection(String username, String password) throws SQLException {
		return getConnection();
	}
}

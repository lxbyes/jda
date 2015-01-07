package me.leckie.jda.pool;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Vector;

import javax.sql.PooledConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据库连接池<br>
 * 
 * @author Leckie
 * @date 2015年1月6日
 */
public class ConnectionPool {

	Logger logger = LoggerFactory.getLogger(ConnectionPool.class);

	// 数据库驱动
	private String jdbcDriver = "";
	// 数据库url
	private String url = "";
	// 数据库用户名
	private String username = "";
	// 数据库密码
	private String password = "";
	// 测试连接是否可用的语句
	private String testSql = "";
	// 连接池初始连接数
	private int initialConnections = 10;
	// 连接不够用时增加的连接数
	private int incrementalConnections = 5;
	// 最大连接数
	private int maxConnections = 50;
	// 存放连接的向量
	private Vector< PooledConnection > connections = null;

	public ConnectionPool(String jdbcDriver, String url, String username, String password) {
		this.jdbcDriver = jdbcDriver;
		this.url = url;
		this.username = username;
		this.password = password;
	}

	public int getInitialConnections() {
		return initialConnections;
	}

	public void setInitialConnections(int initialConnections) {
		this.initialConnections = initialConnections;
	}

	public int getIncrementalConnections() {
		return incrementalConnections;
	}

	public void setIncrementalConnections(int incrementalConnections) {
		this.incrementalConnections = incrementalConnections;
	}

	public int getMaxConnections() {
		return maxConnections;
	}

	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}

	public Vector< PooledConnection > getConnections() {
		return connections;
	}

	public void setConnections(Vector< PooledConnection > connections) {
		this.connections = connections;
	}

	public String getTestSql() {
		return testSql;
	}

	public void setTestSql(String testSql) {
		this.testSql = testSql;
	}

	public synchronized void createPool() throws InstantiationException, IllegalAccessException,
	        ClassNotFoundException, SQLException {
		if (connections != null) {
			// 确保连接池没有创建
			return;
		}
		// 实例化驱动实例
		Driver driver = (Driver) Class.forName(this.jdbcDriver).newInstance();
		logger.debug("加载驱动实例..." + this.jdbcDriver);
		// 注册JDBC驱动
		DriverManager.registerDriver(driver);
		connections = new Vector< PooledConnection >();
		createConnections(this.initialConnections);
	}

	private void createConnections(int initialConnections) throws SQLException {
		// 循环创建指定数目的数据库连接
		for (int i = 0; i < initialConnections; i++) {
			if (this.maxConnections > 0 && this.maxConnections <= this.connections.size()) {
				// 连接数已经达到最大
				break;
			}
			try {
				connections.addElement(new ConnectionWrapper(newConnection()));
			} catch ( SQLException e ) {
				logger.debug("创建数据库连接失败 。。。" + e.getMessage());
				throw new SQLException();
			}
		}
	}

	private Connection newConnection() throws SQLException {
		Connection conn = DriverManager.getConnection(this.url, this.username, this.password);
		// 如果是第一次创建连接，检查连接
		if (connections.size() == 0) {
			DatabaseMetaData metaData = conn.getMetaData();
			int driverMaxConnections = metaData.getMaxConnections();
			if (driverMaxConnections > 0 && driverMaxConnections < this.maxConnections) {
				this.maxConnections = driverMaxConnections;
			}
		}
		return conn;
	}

	public Connection getConnection() throws InterruptedException, SQLException {
		if (connections == null) {
			logger.debug("连接池尚未建立...");
			return null;
		}
		Connection conn = getFreeConnection();
		while (conn == null) {
			// 等待，直到有空闲连接
			Thread.sleep(200);
			conn = getFreeConnection();
		}
		return conn;

	}

	private Connection getFreeConnection() throws SQLException {
		Connection conn = findFreeConnection();
		if (conn == null) {
			// 增加连接数
			createConnections(incrementalConnections);
			// 如果增加连接数后仍不能获得连接，则返回null
			conn = findFreeConnection();
		}
		return conn;
	}

	private Connection findFreeConnection() throws SQLException {
		Connection conn = null;
		ConnectionWrapper wrapper = null;
		Enumeration< PooledConnection > enumeration = connections.elements();
		// 遍历所有对象，找到可用连接
		while (enumeration.hasMoreElements()) {
			wrapper = (ConnectionWrapper) enumeration.nextElement();
			if (!wrapper.isBusy()) {
				conn = wrapper.getConnection();
				wrapper.setBusy(true);
				// 测试连接是否可用，不可用重新创建
				if (!testConnection(conn)) {
					try {
						conn = newConnection();
					} catch ( Exception e ) {
						logger.debug("创建连接失败...");
						throw e;
					}
					wrapper.setConnection(conn);
				}
				// 已经找到连接
				break;
			}
		}
		return conn;
	}

	private boolean testConnection(Connection conn) {
		try {
			// 判定测试表是否存在
			if ("".equals(testSql)) {
				// 假如测试表为空，试着使用此连接的 setAutoCommit() 方法
				// 来判定连接否可用（此方法只在部分数据库可用，假如不可用 ,
				// 抛出异常）。注重：使用测试表的方法更可靠
				conn.setAutoCommit(true);
			} else { // 有测试表的时候使用测试表测试
				// check if this connection is valid
				Statement stmt = conn.createStatement();
				stmt.execute(testSql);
			}
		} catch ( SQLException e ) {
			// 上面抛出异常，此连接己不可用，关闭它，并返回 false;
			closeConnection(conn);
			return false;
		}
		// 连接可用，返回 true
		return true;
	}

	private void closeConnection(Connection conn) {
		try {
			conn.close();
		} catch ( SQLException e ) {
			logger.debug("关闭数据库连接出错...");
			e.printStackTrace();
		}

	}

	public void freeConnection(Connection conn) throws SQLException {
		if (connections == null) {
			return;
		}
		Enumeration< PooledConnection > enumeration = connections.elements();
		ConnectionWrapper wrapper = null;
		while (enumeration.hasMoreElements()) {
			wrapper = (ConnectionWrapper) enumeration.nextElement();
			if (conn == wrapper.getConnection()) {
				wrapper.setBusy(false);
				break;
			}
		}

	}

	public synchronized void refreshConnections() throws InterruptedException, SQLException {
		if (connections == null) {
			return;
		}

		Enumeration< PooledConnection > enumeration = connections.elements();
		while (enumeration.hasMoreElements()) {
			ConnectionWrapper wrapper = (ConnectionWrapper) enumeration.nextElement();
			if (wrapper.isBusy()) {
				Thread.sleep(5000);
			}
			closeConnection(wrapper.getConnection());
			wrapper.setConnection(newConnection());
			wrapper.setBusy(false);
		}
	}

	public synchronized void closeConnectionPool() throws InterruptedException, SQLException {
		if (connections == null) {
			return;
		}
		Enumeration< PooledConnection > enumeration = connections.elements();
		while (enumeration.hasMoreElements()) {
			ConnectionWrapper wrapper = (ConnectionWrapper) enumeration.nextElement();
			if (wrapper.isBusy()) {
				Thread.sleep(5000);
			}
			closeConnection(wrapper.getConnection());
		}
		connections.clear();
		connections = null;
	}

	public void info() throws SQLException {
		System.out.println("----------------------info-----------------------------");
		if (connections == null) {
			return;
		}
		Enumeration< PooledConnection > enumeration = connections.elements();
		while (enumeration.hasMoreElements()) {
			ConnectionWrapper wrapper = (ConnectionWrapper) enumeration.nextElement();
			System.out.println("busy:" + wrapper.isBusy() + ", isclosed:" + wrapper.getConnection().isClosed());
		}
	}
}

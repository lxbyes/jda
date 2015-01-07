package me.leckie.jda.pool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import me.leckie.jda.UnitTestBase;
import me.leckie.jda.pool.ConnectionPool;

import org.junit.Test;

public class ConnectionPoolTest extends UnitTestBase {

	@Test
	public void test() throws SQLException, InterruptedException {
		ConnectionPool connectionPool =
		        new ConnectionPool("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/auth2", "root", "root");
		try {
			connectionPool.createPool();
		} catch ( InstantiationException e ) {
			e.printStackTrace();
		} catch ( IllegalAccessException e ) {
			e.printStackTrace();
		} catch ( ClassNotFoundException e ) {
			e.printStackTrace();
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
		Connection con = null;
		try {
			for (int i = 0; i < 10; i++) {
				con = connectionPool.getConnection();
				// connectionPool.freeConnection(con);
			}
		} catch ( InterruptedException e ) {
			e.printStackTrace();
		} catch ( SQLException e ) {
			e.printStackTrace();
		}

		connectionPool.info();

		Statement statement = con.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT id, username , password FROM user");
		while (resultSet.next()) {
			System.out.println(resultSet.getLong("id"));
		}
		con.close();
		connectionPool.freeConnection(con);
		connectionPool.info();
		connectionPool.closeConnectionPool();
		connectionPool.info();
	}
}

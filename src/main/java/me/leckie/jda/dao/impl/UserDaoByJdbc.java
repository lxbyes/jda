package me.leckie.jda.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import me.leckie.jda.dao.UserDao;
import me.leckie.jda.jdbc.impl.DefaultConnectionMng;
import me.leckie.jda.moudle.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通过jdbc实现对User的访问<br>
 * 
 * @author Leckie
 * @date 2014年12月30日
 */
public class UserDaoByJdbc implements UserDao {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private DataSource connectionMng = new DefaultConnectionMng();

	public Connection getConnection() throws Exception {
		return connectionMng.getConnection();
	}

	public long save(User user) throws Exception {
		Connection con = getConnection();
		Statement statement = con.createStatement();
		int result =
		        statement.executeUpdate("INSERT INTO user(username, password) VALUES('" + user.getUsername() + "','"
		                + user.getPassword() + "')", Statement.RETURN_GENERATED_KEYS);
		if (result == 0) {
			logger.debug("添加失败！");
		}
		ResultSet resultSet = statement.getGeneratedKeys();
		long id = -1L;
		if (resultSet.next()) {
			id = resultSet.getLong(1);
		}
		resultSet.close();
		statement.close();
		con.close();
		return id;
	}

	public void update(User user) throws Exception {
		Connection con = getConnection();
		PreparedStatement ps = con.prepareStatement("UPDATE user SET username=?, password=? WHERE id=?");

		ps.setString(1, user.getUsername());
		ps.setString(2, user.getPassword());
		ps.setLong(3, user.getId());

		ps.executeUpdate();

		ps.close();
		con.close();
	}

	public void delete(long id) throws Exception {
		Connection con = getConnection();
		PreparedStatement ps = con.prepareStatement("DELETE FROM user WHERE id=?");
		ps.setLong(1, id);
		ps.executeUpdate();

		ps.close();
		con.close();

	}

	public List< User > list() throws Exception {
		Connection con = getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT id, username, password FROM user");
		ResultSet rs = ps.executeQuery();
		List< User > list = new ArrayList< User >();
		while (rs.next()) {
			User user = new User();
			user.setId(rs.getLong("id"));
			user.setUsername(rs.getString("username"));
			user.setPassword(rs.getString("password"));
			list.add(user);
		}
		rs.close();
		ps.close();
		con.close();
		return list;
	}

	public User get(long id) throws Exception {
		Connection con = getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT id, username, password FROM user WHERE id=?");
		ps.setLong(1, id);
		ResultSet resultSet = ps.executeQuery();
		User user = new User();
		while (resultSet.next()) {
			user.setId(resultSet.getLong(1));
			user.setUsername(resultSet.getString(2));
			user.setPassword(resultSet.getString("password"));
		}
		resultSet.close();
		ps.close();
		con.close();
		return user;
	}

}

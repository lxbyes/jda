package me.leckie.jda.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import me.leckie.jda.dao.UserDao;
import me.leckie.jda.jdbc.impl.DefaultConnectionMng;
import me.leckie.jda.moudle.User;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class UserDaoByJdbcTemplate implements UserDao {

	private JdbcTemplate jdbcTemplate = new JdbcTemplate(new DefaultConnectionMng());

	public long save(final User user) throws Exception {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement("INSERT INTO user(username, password) VALUES (?,?)");
				ps.setString(1, user.getUsername());
				ps.setString(2, user.getPassword());

				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().longValue();
	}

	public void update(User user) throws Exception {
		jdbcTemplate.update("UPDATE user SET username=?, password=? WHERE id=?", new Object[] { user.getUsername(),
		        user.getPassword(), user.getId() });

	}

	public void delete(long id) throws Exception {
		jdbcTemplate.update("DELETE FROM user WHERE id=?", new Object[] { id });

	}

	public List< User > list() throws Exception {
		List< User > list = jdbcTemplate.query("SELECT id, username, password FROM user", new RowMapper< User >() {
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User user = new User();
				user.setId(rs.getLong("id"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				return user;
			}

		});
		return list;
	}

	public User get(long id) throws Exception {
		User user =
		        jdbcTemplate.queryForObject("SELECT id, username, password FROM user WHERE id=?",
		                new RowMapper< User >() {
			                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				                User user = new User();
				                user.setId(rs.getLong("id"));
				                user.setUsername(rs.getString("username"));
				                user.setPassword(rs.getString("password"));
				                return user;
			                }
		                }, new Object[] { id });
		return user;
	}
}

package me.leckie.jda.jdbc;

import java.util.List;
import java.util.UUID;

import me.leckie.jda.UnitTestBase;
import me.leckie.jda.dao.UserDao;
import me.leckie.jda.dao.impl.UserDaoByJdbcTemplate;
import me.leckie.jda.moudle.User;

import org.junit.Test;

public class JdbcTemplateOperTest extends UnitTestBase {

	@Test
	public void save() {
		UserDao userDao = new UserDaoByJdbcTemplate();
		User user = new User();
		user.setUsername("Leckie");
		user.setPassword(UUID.randomUUID().toString().substring(16));
		try {
			long id = userDao.save(user);
			System.out.println(id);
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	@Test
	public void update() {
		UserDao userDao = new UserDaoByJdbcTemplate();
		try {
			User user = userDao.get(1);
			System.out.println(user);
			user.setUsername("Vickie");
			userDao.update(user);
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	@Test
	public void delete() {
		UserDao userDao = new UserDaoByJdbcTemplate();
		try {
			userDao.delete(11);
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	@Test
	public void list() {
		UserDao userDao = new UserDaoByJdbcTemplate();
		try {
			List< User > list = userDao.list();
			System.out.println(list);
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
}

package me.leckie.jda.jdbc;

import java.util.List;
import java.util.UUID;

import me.leckie.jda.UnitTestBase;
import me.leckie.jda.dao.UserDao;
import me.leckie.jda.dao.impl.UserDaoByJdbc;
import me.leckie.jda.moudle.User;

import org.junit.Test;

/**
 * 
 * @author Leckie
 * @date 2014年12月30日
 */
public class JdbcOperTest extends UnitTestBase {

	@Test
	public void save() {
		UserDao userDao = new UserDaoByJdbc();
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
		UserDao userDao = new UserDaoByJdbc();
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
		UserDao userDao = new UserDaoByJdbc();
		try {
			userDao.delete(6);
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	@Test
	public void list() {
		UserDao userDao = new UserDaoByJdbc();
		try {
			List< User > list = userDao.list();
			System.out.println(list);
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
}

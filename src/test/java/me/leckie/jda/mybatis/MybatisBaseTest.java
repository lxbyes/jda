package me.leckie.jda.mybatis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.leckie.jda.UnitTestBase;
import me.leckie.jda.dao.UserDao;
import me.leckie.jda.moudle.User;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

/**
 * mybatis基本用法测试<br>
 * 
 * @author Leckie
 * @date 2014年12月31日
 */
public class MybatisBaseTest extends UnitTestBase {

	/**
	 * 获得MyBatis SqlSessionFactory<br>
	 * SqlSessionFactory负责创建SqlSession，一旦创建成功，就可以用SqlSession实例来执行映射语句<br>
	 * ，commit，rollback，close等方法。<br>
	 * 
	 * @return
	 */
	private SqlSessionFactory getSessionFactory() {
		SqlSessionFactory sessionFactory = null;
		String resource = "conf/configuration.xml";
		try {
			sessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader(resource));
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		return sessionFactory;
	}

	/**
	 * 请求一次会话<br>
	 * 
	 * @return
	 */
	private SqlSession openSession() {
		return getSessionFactory().openSession();
	}

	@Test
	public void get() {
		SqlSession session = openSession();
		// UserDao userDao = session.getMapper(UserDao.class);
		User user = null;
		try {
			// user = userDao.get(1);
			user = session.selectOne("me.leckie.jda.dao.UserDao.getUser", 10L);
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			// 一定要关闭session
			session.close();
		}
		System.out.println(user);
	}

	@Test
	public void limit() {
		SqlSession session = openSession();
		List< User > list = null;
		RowBounds rowBounds = new RowBounds(2, 5);
		list = session.selectList("me.leckie.jda.dao.UserDao.list", null, rowBounds);
		System.out.println(list);
		session.close();
	}

	@Test
	public void resultHandler() {
		SqlSession session = openSession();
		final List< User > list = new ArrayList< User >();
		session.select("me.leckie.jda.dao.UserDao.list", new ResultHandler() {
			@Override
			public void handleResult(ResultContext context) {
				User user = (User) context.getResultObject();
				list.add(user);
				if (list.size() >= 5) {
					context.stop();
				}
			}
		});
		session.close();
		System.out.println(list);
	}

	@Test
	public void save() {
		User user = new User();
		user.setUsername(String.valueOf(UUID.randomUUID()));
		user.setPassword(String.valueOf(UUID.randomUUID()));

		SqlSession session = openSession();
		UserDao userDao = session.getMapper(UserDao.class);
		try {
			long id = userDao.save(user);
			System.out.println(id);
			session.commit();// 需要手动提交事务
		} catch ( Exception e ) {
			session.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		System.out.println(user);
	}

	@Test
	public void update() {
		User user = new User();
		user.setId(10);
		user.setPassword(UUID.randomUUID().toString());
		user.setUsername(UUID.randomUUID().toString());
		SqlSession session = openSession();
		UserDao userDao = session.getMapper(UserDao.class);
		try {
			userDao.update(user);
			session.commit();
		} catch ( Exception e ) {
			e.printStackTrace();
			session.rollback();
		} finally {
			session.close();
		}
		System.out.println(user);
	}

	@Test
	public void delete() {
		SqlSession session = openSession();
		UserDao userDao = session.getMapper(UserDao.class);
		try {
			userDao.delete(117);
			session.commit();
		} catch ( Exception e ) {
			session.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	@Test
	public void list() {
		SqlSession session = openSession();
		UserDao userDao = session.getMapper(UserDao.class);
		try {
			System.out.println(userDao.list());
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
}

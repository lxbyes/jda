package me.leckie.jda.cache;

import java.io.IOException;
import java.net.InetSocketAddress;

import me.leckie.jda.UnitTestBase;
import me.leckie.jda.moudle.User;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author leckie
 * @date Jan 19, 2015
 */
public class MemcachedTest extends UnitTestBase {
	
	private MemcachedClient client = null;
	
	@Before
	public void before() throws IOException {
		 client = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
	}

	@After
	public void after() {
		client = null;
	}
	
	@Test
	public void add() {
		User u = new User();
		u.setId(2);
		u.setUsername("leckie21");
		u.setPassword("passwordaaaaa");
		
		OperationFuture<Boolean> add = client.add("2", 120, u);
		System.out.println(add.getKey());
		
		System.out.println(client.get("2"));
	}
	
	@Test
	public void set() {
		client.set("leckie", 3600, "Leckie");
		Object leckie = client.get("leckie");
		System.out.println(leckie.toString());
		
		User user = new User();
		user.setId(1);
		user.setUsername("Leckie");
		user.setPassword("pwd");
		
		client.set("leckie001", 1200, user);
		
		User u = (User)client.get("leckie001");
		System.out.println(u);
	}
	
	@Test
	public void replace() {
		User user = new User();
		user.setId(11);
		user.setUsername("Leckie1111");
		user.setPassword("pwd");
		
		client.replace("leckie001", 120, user);
		System.out.println((User)client.get("leckie001"));
	}
	
	@Test
	public void delete() {
		client.delete("leckie001");
		System.out.println((User)client.get("leckie001"));
	}
	
	@Test
	public void get() {
		System.out.println(client.get("leckie"));
	}
}

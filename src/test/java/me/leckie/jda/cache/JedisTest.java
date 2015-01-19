package me.leckie.jda.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.leckie.jda.UnitTestBase;
import me.leckie.jda.moudle.User;
import me.leckie.jda.utils.SerializeUtils;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 
 * @author leckie
 * @date Jan 19, 2015
 */
public class JedisTest extends UnitTestBase {

	private Jedis jedis;
	
	private JedisPool pool;
	
	@Before 
	public void before() {
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		config.setMaxIdle(30);
		config.setMaxWaitMillis(10000);
		config.setTestOnBorrow(true);
		config.setTestOnBorrow(true);
		
		pool = new JedisPool(config, "127.0.0.1", 6379);
		jedis = pool.getResource();
	}
	
	@Test
	public void set() {
		jedis.set("leckie", "Leckie");
		
		System.out.println(jedis.get("leckie"));
	}
	
	@Test
	public void setObj() {
		User user = new User();
		user.setId(3);
		user.setUsername("Vickie");
		user.setPassword("123456");
		
		/**
		 * 需要自己序列化<br>
		 */
		jedis.set("leckie001".getBytes(), SerializeUtils.serialize(user));
		
		System.out.println(SerializeUtils.unSerialize(jedis.get("leckie001".getBytes())));
	}
	
	@Test
	public void testList() {
		for(int i=0; i<10; i++) {
			jedis.rpush("list", "msg" + i);
		}
		
		List<String> values = jedis.lrange("list", 0, -1);
		System.out.println(values);
	}
	
	@Test
	public void testMap() {
		Map<String, String> map = new HashMap<String, String>(); 
		for(int i=0; i<10; i++) {
			map.put("key" + i, "value" + i);
		}
		jedis.hmset("map", map);
		
		System.out.println(jedis.hmget("map", "key1"));
		Set<String> keys = jedis.hkeys("map");
		System.out.println(keys);
		System.out.println(jedis.hvals("map"));
		
		for(String key : keys) {
			System.out.println(jedis.hmget("map", key));
		}
		
		// another way
		Iterator<String> it = jedis.hkeys("map").iterator();
		while(it.hasNext()) {
			System.out.println(jedis.hmget("map", it.next()));
		}
	}
	
	@Test
	public void testOther() {
		System.out.println(jedis.keys("*"));
	}
	
	@After
	public void after() {
		pool.returnResource(jedis);
		pool.close();
	}
}

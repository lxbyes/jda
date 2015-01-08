package me.leckie.jda.mongo;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import me.leckie.jda.UnitTestBase;

import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

/**
 * MonggoDB 基本访问方式<br>
 * 
 * @author Leckie
 * @date 2015年1月8日
 */
public class MongoOpTest extends UnitTestBase {

	Logger logger = LoggerFactory.getLogger(getClass());

	private MongoClient client;
	private DB db;
	private DBCollection users;

	@Before
	public void init() {
		try {
			client = new MongoClient();
		} catch ( UnknownHostException e ) {
			logger.debug("获取客户端失败...");
			e.printStackTrace();
		}
		// 获取demo DB, 如果不存在，MongoDB会自动创建
		db = client.getDB("demo");

		// 获取users DBCollection，如果没有，MongoDB会自动创建
		users = db.getCollection("users");
		echo("init...");
	}

	@After
	public void destory() {
		users = null;
		db = null;
		if (client != null) {
			client.close();
		}
		client = null;
		// 调用垃圾回收
		System.gc();
		echo("destory...");
	}

	@Test
	public void add() {
		queryAll();

		DBObject addr = new BasicDBObject();
		addr.put("city", "ChongQing");
		addr.put("detail", "huijinlu");
		addr.put("code", 86);

		DBObject user = new BasicDBObject();
		user.put("name", "leckie");
		user.put("age", 24);
		user.put("addr", addr);
		echo(users.save(user).getN()); // save
		// echo(users.insert(new BasicDBObject("name", "vickie")).getN()); //
		// insert
		echo(user);
		List< DBObject > list = new ArrayList< DBObject >();
		user = new BasicDBObject("name", "haha");
		user.put("index", 0);
		list.add(user);
		user = new BasicDBObject("name", "haha");
		user.put("index", 1);
		list.add(user);
		user = new BasicDBObject("name", "haha");
		user.put("index", 2);
		list.add(user);
		echo(list);
		echo(users.insert(list).getN());
		echo(list);
		echo("count" + users.getCount());
		echo(user.get("_id"));

		queryAll();
	}

	@Test
	public void remove() {
		echo(users.remove(new BasicDBObject("name", "haha")).getN());
	}

	@Test
	public void update() {
		echo(users.findOne(new BasicDBObject("_id", new ObjectId("54ae030697c26c488af5c916"))));
		DBObject user = new BasicDBObject();
		user.put("name", "zhangsan");
		echo(users.update(new BasicDBObject("_id", new ObjectId("54ae030697c26c488af5c916")), user).getN());
		echo(users.findOne(new BasicDBObject("_id", new ObjectId("54ae030697c26c488af5c916"))));
	}

	@Test
	public void getJson() {
		DBObject user = users.findOne();
		echo(user);
		echo(JSON.serialize(user));
	}

	@Test
	public void queryAll() {
		DBCursor cursor = users.find();
		while (cursor.hasNext()) {
			echo(cursor.next());
		}
	}

	public void echo(Object o) {
		System.out.println(o);
	}

}

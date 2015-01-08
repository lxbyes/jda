package me.leckie.jda.mongo;

import java.net.UnknownHostException;

import me.leckie.jda.UnitTestBase;

import org.junit.Test;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

/**
 * MonggoDB 基本访问方式<br>
 * 
 * @author Leckie
 * @date 2015年1月8日
 */
public class MongoOpTest extends UnitTestBase {

	@Test
	public void testQuery() throws UnknownHostException {
		MongoClient client = new MongoClient();
		DB db = client.getDB("test");
		DBCollection table1 = db.getCollection("table1");
		DBCursor cursor = table1.find();
		while (cursor.hasNext()) {
			System.out.println(cursor.next());
		}
	}
}

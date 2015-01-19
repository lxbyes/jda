package me.leckie.jda.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Serialize Utils<br>
 * 
 * @author leckie
 * @date Jan 19, 2015
 */
public class SerializeUtils {

	/**
	 * serialize object<br>
	 * 
	 * @param o
	 * @return
	 */
	public static byte[] serialize(Object o) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(o);
			return baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * unSerialize Object<br>
	 * 
	 * @param bytes
	 * @return
	 */
	public static Object unSerialize(byte[] bytes) {
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		
		try {
			bais = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	} 
}

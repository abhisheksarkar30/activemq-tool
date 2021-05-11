/**
 * 
 */
package edu.abhi.tools.activemq.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/**
 * @author abhisheksa
 *
 */
public class ResourceLoader {
	
	private static final Properties resources = new Properties();

	private ResourceLoader() {}

	public static void init(String filePath) {
		try(InputStream is = new FileInputStream(filePath)) {
			resources.load(is);
		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	/**
	 * @return the resources
	 */
	public static Set<Entry<Object, Object>> getResourceProperties() {
		return resources.entrySet();
	}
	
	public static String getResourceProperty(String key) {
		return resources.getProperty(key);
	}
	
	public static Properties getResource() {
		return resources;
	}

}

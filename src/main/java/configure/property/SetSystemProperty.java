package configure.property;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/** 
 * @author  作者: Owen_Study
 * @Email   邮箱: owen_study@126.com
 * @version 创建时间：May 17, 2017 10:54:28 AM 
 * 类说明 
 */
public class SetSystemProperty {
 
//	//初始化方法
//	public SetSystemProperty(String fileName){
//		profilepath=fileName; 
//	}
	//属性文件的路径
    static String profilepath="run.configure";
    /**
    * 采用静态方法
    */
    private static Properties props = new Properties();
    static {
        try {
            props.load(SetSystemProperty.class.getClassLoader().getResourceAsStream(profilepath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (IOException e) {       
            System.exit(-1);
        }
    }

    /**
    * 读取属性文件中相应键的值
    * @param key
    *            主键
    * @return String
    */
    public static String getKeyValue(String key) {
        return props.getProperty(key);
    }

    /**
    * 根据主键key读取主键的值value
    * @param filePath 属性文件路径
    * @param key 键名
    */
    public static String readValue( String key) {
        Properties props = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(
            		profilepath));
            props.load(in);
            String value = props.getProperty(key);
//            System.out.println(key +"键的值是："+ value);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
   
    /**
    * 更新（或插入）一对properties信息(主键及其键值)
    * 如果该主键已经存在，更新该主键的值；
    * 如果该主键不存在，则插件一对键值。
    * @param keyname 键名
    * @param keyvalue 键值
    */
    public static void writeProperties(String keyname,String keyvalue) {       
        try {
            // 调用 Hashtable 的方法 put，使用 getProperty 方法提供并行性。
            // 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
            OutputStream fos = new FileOutputStream(profilepath,true);
            props.setProperty(keyname, keyvalue);
            // 以适合使用 load 方法加载到 Properties 表中的格式，
            // 将此 Properties 表中的属性列表（键和元素对）写入输出流
            props.store(fos, "Update '" + keyname + "' value");
        } catch (IOException e) {
            System.err.println("属性文件更新错误");
        }
    }

    /**
    * 更新properties文件的键值对
    * 如果该主键已经存在，更新该主键的值；
    * 如果该主键不存在，则插件一对键值。
    * @param keyname 键名
    * @param keyvalue 键值
    */
    public void updateProperties(String keyname,String keyvalue) {
        try {
            props.load(new FileInputStream(profilepath));
            // 调用 Hashtable 的方法 put，使用 getProperty 方法提供并行性。
            // 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
            OutputStream fos = new FileOutputStream(profilepath);           
            props.setProperty(keyname, keyvalue);
            // 以适合使用 load 方法加载到 Properties 表中的格式，
            // 将此 Properties 表中的属性列表（键和元素对）写入输出流
            props.store(fos, "Update '" + keyname + "' value");
        } catch (IOException e) {
            System.err.println("属性文件更新错误");
        }
    }
    //测试代码
    public static void main(String[] args) {
    	System.out.println("Test filepath:");
    	System.out.println("Test filepath:"+SetSystemProperty.class.getResource("/"));
    	System.out.println("Start...");
    	System.out.println(Thread.currentThread().getClass().getResource(profilepath));
        readValue("MAIL_SERVER_PASSWORD");
        writeProperties("MAIL_SERVER_PASSWORD", "327@qq.com");  
        getKeyValue("MAIL_SERVER_PASSWORD");
        System.out.println("操作完成");
//        
        
//        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
//        InputStream in=classloader.getResourceAsStream(profilepath);
//        try{
//        props.load(in);
//        } catch (IOException e) {
//            System.err.println("属性文件更新错误");
//        }
//
//        String value = props.getProperty("MAIL_SERVER_PASSWORD");
//        System.out.println("MAIL_SERVER_PASSWORD" +"键的值是："+ value);
//        props.setProperty("MAIL_SERVER_PASSWORD", "xxxxx");
//        value = props.getProperty("MAIL_SERVER_PASSWORD");
//        System.out.println("MAIL_SERVER_PASSWORD" +"键的值是："+ value);
        

    }
}

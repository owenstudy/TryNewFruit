package service;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
/** 
 * @author  作者: Owen_Study
 * @Email   邮箱: owen_study@126.com
 * @version 创建时间：May 29, 2017 11:00:44 PM 
 * 类说明 
 */
public class URLAvailability {

	private static URL url=null;
	private static HttpURLConnection connection;
	private static String succ="";
	private static int statusCode = -1;  
	private static int testCounts = 5;//尝试链接次数
	/**
	 * 功能描述 : 检测当前URL是否可连接或是否有效,
	 * 最多连接网络 5 次, 如果 5 次都不成功说明该地址不存在或视为无效地址.
	 * 
	 * @param urlStr
	 *            指定URL网络地址
	 * 
	 * @return String
	 */
	 private synchronized String isConnectable(String urlStr) {
	int counts = 0;
	if (urlStr == null || urlStr.length() <= 0) {
	  return succ;                  
	}
	while (counts < testCounts) {
	  try {//非格式,io错误的链接
	url = new URL(urlStr);
	connection = (HttpURLConnection) url.openConnection();
	statusCode = connection.getResponseCode();
	if (statusCode == 200) {
	succ = connection.getURL().toString();
	break;
	}
	counts++; 
	System.out.println("连接次数："+counts);
	  } catch (Exception ex) {//格式,io错误的链接
	counts++; 
	System.out.println("连接次数："+counts);
	  }
	}
	    return succ;
	 }
	 


	 
	 /** 
	  * 功能：检测当前URL是否可连接或是否有效, 
	  * 描述：最多连接网络 5 次, 如果 5 次都不成功，视为该地址不可用 
	  * @param urlStr 指定URL网络地址 
	  * @return URL 
	  */  
	 public synchronized URL isConnect(String urlStr) {  
	int counts = 1;  
	if (urlStr == null || urlStr.length() <= 0) {                         
	return null;                   
	}  
	while (counts <= testCounts) {  
	try {
	url = new URL(urlStr);  
	connection = (HttpURLConnection) url.openConnection();  
	statusCode = connection.getResponseCode();  
	System.out.println("第"+(counts) +"/"+testCounts+"次链接，   statusCode:"+statusCode);  
	counts++;   
	//非格式错误链接
	if (statusCode == 200) {//可连接
	System.out.println("URL可用！");  
	break;  
	}else{//不可连接
	url = null;
	}
	} catch (MalformedURLException e) {//格式错误
	System.out.println("URL不可用，尝试连接第"+(counts) +"/"+testCounts+"次");  
	url = null; 
	counts++;   
	continue;  
	} catch (IOException e) {//io错误
	System.out.println("URL不可用，尝试连接第"+(counts) +"/"+testCounts+"次");  
	url = null; 
	counts++;   
	}
	}  
	return url;  
	 }  
	/** 
	  * 功能：检测当前URL是否可连接或是否有效, 
	  * 描述：最多连接网络 5 次, 如果 5 次都不成功，视为该地址不可用 
	  * @param urlStr 指定URL网络地址 
	  * @return URL 
	  */  
	public synchronized boolean isConnected(String urlStr) {  
	  int counts = 1;
	  boolean connected = false;  
	  if (urlStr == null || urlStr.length() <= 0) {                         
	   return false;                   
	  }  
	  while (counts <= testCounts) {  
	try {
	url = new URL(urlStr);  
	connection = (HttpURLConnection) url.openConnection();  
	statusCode = connection.getResponseCode();  
	System.out.println("第"+(counts) +"/"+testCounts+"次链接，   statusCode:"+statusCode);  
	counts++;   
	//非格式错误链接
	if (statusCode == 200) {//可连接
	   System.out.println("URL可用！"); 
	   connected= true;
	   break;
	}else{//不可连接
	connected= false;
	}
	} catch (Exception e) {//io,malformed error
	System.out.println("URL不可用，尝试连接第"+(counts) +"/"+testCounts+"次");  
	connected =false; 
	    counts++;   
	}
	  } 
	  return connected;
	}  




	/**
	* @param args
	*/
	public static void main(String[] args) {
	String  url1="https://m.eqxiu.com/s/Vb1JaE6g";
	URLAvailability  ua = new URLAvailability();
	// URL url = ua.isConnect(url1);
	// System.out.println(url==null?"无效":"有效");
	System.out.println("+++++++++++++++");
	//String urlStr = ua.isConnectable(url1);
	// System.out.println("urlStr:"+urlStr);
	System.out.println("+++++++++++++++");
	boolean flag = ua.isConnected(url1);
	System.out.println("flag:"+flag);


	}


	}

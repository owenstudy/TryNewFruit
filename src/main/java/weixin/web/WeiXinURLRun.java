package weixin.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.servlet.ServletException;

import org.json.JSONException;
import org.json.JSONObject;

import weixin.access.WeixinConstant;

//运行微信URL的公共类
public class WeiXinURLRun {

	//执行URL，获取返回的数据,URL中的参数需要在传送之前替换完成，直接转换成JSON对象返回
	private static JSONObject runWeiXinURL(String urlAddress) throws IOException{
		
		
	    JSONObject resultObject = null;
	    URL url = new URL(urlAddress);  //创建url连接  
	    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(); //打开连接  
	    urlConnection.setDoOutput(true);  
	    urlConnection.setDoInput(true);  
	    urlConnection.setRequestMethod("GET");  
	    urlConnection.setUseCaches(false);  
	    urlConnection.connect();  
	    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));  
	    StringBuffer buffer = new StringBuffer();//<span style="font-family: Arial, Helvetica, sans-serif;">//存储服务器返回的信息</span>  
	    String line = "";   
	    while ((line = reader.readLine()) != null) {  
	        buffer.append(line);  
	    }  
	    String result = buffer.toString();  
	    System.out.println("调用URL: "+urlAddress);  
	    System.out.println("调用结果："+result);
	    try {  
	    	resultObject = new JSONObject(result); //将服务器返回的字符串转换成json格式  
	    } catch (JSONException e) {  
	        // TODO Auto-generated catch block  
	        e.printStackTrace();  
	    }  
	        	
		return resultObject;
	}
	//获取access token相关的信息 ，传入用户授权的code
	 public static JSONObject getoAuthAccessToken(String code )  throws  IOException{
		String ACCESS_URL=WeixinConstant.WEIXIN_OAUTH_ACCESS_URL;
		String userImageURL="";
		System.out.println("call oAuth:appid");
		System.out.println(WeixinConstant.APPID);
	    String requestUrlString = ACCESS_URL.replace("APPID", WeixinConstant.APPID).replace("APPSECRET", WeixinConstant.APPSECRET).replace("CODE", code);//将请求用户的URL中的///参数替换成真正的内容  
		
		JSONObject resultObject=null;
	    resultObject=runWeiXinURL(requestUrlString);        
	    
	    return resultObject; 
	   }
	
	//获取用户的头像，在我的订单碳显示出来  
	public static String getUserImage(String accessToken , String openid )  throws  IOException{
		String USERINFO_URL=WeixinConstant.WEIXIN_USERINFO_URL;
		String userImageURL="";
		String userInof_URL = USERINFO_URL.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openid);//将请求用户的URL中的///参数替换成真正的内容
		
		JSONObject resultObject=null;
	    resultObject=runWeiXinURL(userInof_URL);        
	    userImageURL=resultObject.getString("headimgurl"); //用户头像的URL,用来 摘取用户信息    	    	
	    
	    return userImageURL; 
	   }
	//检查当前的access token是不是有效，0有效，其它为无效
	public static int verifyAccessToken(String accessToken , String openid )  throws  IOException{
		String VERIFY_TOKEN_URL=WeixinConstant.WEIXIN_VERIFY_TOKEN_URL;
		int errorCode=-1;
		String verify_token_url = VERIFY_TOKEN_URL.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openid);//将请求用户的URL中的///参数替换成真正的内容

		JSONObject resultObject=null;
	    resultObject=runWeiXinURL(verify_token_url);        
	    errorCode=resultObject.getInt("errcode"); //用户头像的URL,用来 摘取用户信息

	    return errorCode; 
	    }
	  //refresh access token并返回新的access token
	public static String refreshAccessToken(String refreshAccessToken , String openid )  throws  IOException{
		String REFRESH_TOKEN_URL=WeixinConstant.WEIXIN_REFRESH_TOKEN_URL;
		String newAccessToken="";
		String refresh_token_url = REFRESH_TOKEN_URL.replace("APPID", WeixinConstant.APPID).replace("REFRESH_TOKEN", refreshAccessToken).replace("OPENID", openid);//将请求用户的URL中的///参数替换成真正的内容
		System.out.println("refresh_token_url:"+refresh_token_url);

		JSONObject resultObject=null;
	    resultObject=runWeiXinURL(refresh_token_url);        
	    newAccessToken=resultObject.getString("access_token"); //用户头像的URL,用来 摘取用户信息

	   	return newAccessToken; 
	    }	
	
	  //refresh access token并返回新的access token
	public static String getJSTicket(String accessToken )  throws  IOException{
		String JSTICKET_URL=WeixinConstant.WEIXIN_JSTICKET_URL;
		String jsTicket="";
		String url = JSTICKET_URL.replace("ACCESS_TOKEN", accessToken);//将请求用户的URL中的///参数替换成真正的内容

		JSONObject resultObject=null;
	    resultObject=runWeiXinURL(url);        
	    jsTicket=resultObject.getString("ticket"); //用户头像的URL,用来 摘取用户信息

	   	return jsTicket; 
	    }

	//SH1字符加密
	public static String SHA1(String decript) {  
	    try {  
	        MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");  
	        digest.update(decript.getBytes());  
	        byte messageDigest[] = digest.digest();  
	        // Create Hex String  
	        StringBuffer hexString = new StringBuffer();  
	        // 字节数组转换为 十六进制 数  
	            for (int i = 0; i < messageDigest.length; i++) {  
	                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);  
	                if (shaHex.length() < 2) {  
	                    hexString.append(0);  
	                }  
	                hexString.append(shaHex);  
	            }  
	            return hexString.toString();  
	   
	        } catch (NoSuchAlgorithmException e) {  
	            e.printStackTrace();  
	        }  
	        return "";  
	}  
	//获取access token相关的信息 ，传入用户授权的code
	 public static String getAccessToken( )  throws  IOException{
		String ACCESS_URL=WeixinConstant.WEIXIN_ACCESS_TOKEN_URL;
	    String requestUrlString = ACCESS_URL.replace("APPID", WeixinConstant.APPID).replace("APPSECRET", WeixinConstant.APPSECRET);//将请求用户的URL中的///参数替换成真正的内容  
		
		JSONObject resultObject=null;
	    resultObject=runWeiXinURL(requestUrlString);        
	    
	    return resultObject.getString("access_token"); 
	   }
	 public static JSONObject getJSSDKConfigue(String url) throws IOException{
		 JSONObject configJson=new JSONObject();
			 //1、获取AccessToken  
		    String accessToken = WeiXinURLRun.getAccessToken();  
		      
		    //2、获取Ticket  
		    //TODO 需要缓存
//		    String jsapi_ticket="asdfasdfasd";
		    String jsapi_ticket = WeiXinURLRun.getJSTicket(accessToken);  
		      
		    //3、时间戳和随机字符串  
		    String noncestr = UUID.randomUUID().toString().replace("-", "").substring(0, 16);//随机字符串  
		    String timestamp = String.valueOf(System.currentTimeMillis() / 1000);//时间戳  
		      
		    System.out.println("accessToken:"+accessToken+"\njsapi_ticket:"+jsapi_ticket+"\n时间戳："+timestamp+"\n随机字符串："+noncestr);  
		      
		    //4、获取url  
		    //
//		    String url="http://trynewfruit.bceapp.com/JSSDKTest.html";  
		      
		    //5、将参数排序并拼接字符串  
		    String str = "jsapi_ticket="+jsapi_ticket+"&noncestr="+noncestr+"&timestamp="+timestamp+"&url="+url;  
		     
		    //6、将字符串进行sha1加密  
		    String signature =SHA1(str);  
		    System.out.println("参数："+str+"\n签名："+signature);  
		    
		    //生成JSON configure数据
		    configJson.put("sigature", signature);
		    configJson.put("noncestr", noncestr);
		    configJson.put("timestamp", timestamp);
		    configJson.put("url", url);
		    configJson.put("appid", WeixinConstant.APPID);
		    configJson.put("access_token", accessToken);
		    		 
		 return configJson;
	 }
    public static void main(String[] args) throws IOException {  
    	int verifyResult;
    	String newAccessToken="";
    	String jsTicket="";
	String accessToken="";
    //调用生成结果的JSON
    JSONObject resultObject=null;    
    accessToken=WeiXinURLRun.getAccessToken();	
    
	String openid="oJePmvmqwkHk4DscKo7za4wB63UU";
//    	String refreshAccessToken="5cer1tg7gHoT4LCyRa0jtBGOL_rv-JiabGzMeLyDcR7w3ULU1RnA_F_DZ_paU103KT-g0ScSU_JeUK_6ixctx2tg95ulYmZBJAxFHuHh9y8";
	String refreshAccessToken="rtIr4by3TREh29GRHEthikAd8y8wEAIiSqFH15FIdAclOR5zmtMQmUf4T2wKPp5mlxhztnh2gxXAGe0UbpeTgoeniSJA7Zqnen2UAss3Rqk";
	
//	verifyResult=WeiXinURLRun.verifyAccessToken(accessToken,openid);
	
	jsTicket=WeiXinURLRun.getJSTicket(accessToken);
	System.out.println("jsTicket:"+jsTicket);

	//refresh access token
//	newAccessToken=WeiXinURLRun.refreshAccessToken(refreshAccessToken,openid);
	System.out.println("newAccessToken:"+newAccessToken);
	
	
	 //1、获取AccessToken  
    accessToken = WeiXinURLRun.getAccessToken();  
      
    //2、获取Ticket  
    String jsapi_ticket = WeiXinURLRun.getJSTicket(accessToken);  
      
    //3、时间戳和随机字符串  
    String noncestr = UUID.randomUUID().toString().replace("-", "").substring(0, 16);//随机字符串  
    String timestamp = String.valueOf(System.currentTimeMillis() / 1000);//时间戳  
      
    System.out.println("accessToken:"+accessToken+"\njsapi_ticket:"+jsapi_ticket+"\n时间戳："+timestamp+"\n随机字符串："+noncestr);  
      
    //4、获取url  
    String url="http://trynewfruit.bceapp.com/JSSDKTest.html";  
    /*根据JSSDK上面的规则进行计算，这里比较简单，我就手动写啦 
    String[] ArrTmp = {"jsapi_ticket","timestamp","nonce","url"}; 
    Arrays.sort(ArrTmp); 
    StringBuffer sf = new StringBuffer(); 
    for(int i=0;i<ArrTmp.length;i++){ 
        sf.append(ArrTmp[i]); 
    } 
    */  
      
    //5、将参数排序并拼接字符串  
    String str = "jsapi_ticket="+jsapi_ticket+"&noncestr="+noncestr+"&timestamp="+timestamp+"&url="+url;  
     
    //6、将字符串进行sha1加密  
    String signature =SHA1(str);  
    System.out.println("参数："+str+"\n签名："+signature);  
}

	
	
}

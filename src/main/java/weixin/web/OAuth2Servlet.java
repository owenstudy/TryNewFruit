package weixin.web;

import java.io.BufferedReader;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.net.HttpURLConnection;  
import java.net.MalformedURLException;
import java.net.URL;  

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;








import javax.servlet.RequestDispatcher;  
import javax.servlet.ServletException;  
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
  

import javax.servlet.http.HttpSession;

import org.json.JSONException;  
import org.json.JSONObject;  

import com.weixin.common.CommonUtil;

import weixin.access.WeixinConstant;

/**
 * Servlet implementation class OAuth2Servlet
 */
@WebServlet("/OAuth2Servlet")
public class OAuth2Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String APPID = WeixinConstant.APPID;//这个是你服务号appid，和URL里面的appid是一个意思  
private static String APPSECRET = WeixinConstant.APPSECRET; //这个是你服务号的app秘钥  
private static String ACCESS_URL = WeixinConstant.WEIXIN_OAUTH_ACCESS_URL; //获取用户授权的token  
private static String USERINFO_URL =WeixinConstant.WEIXIN_USERINFO_URL;  //这个是请求获取用户基本信息的URL  
private static String VERIFY_TOKEN_URL =WeixinConstant.WEIXIN_VERIFY_TOKEN_URL; //access token是否有效的URL
private static String REFRESH_TOKEN_URL   =WeixinConstant.WEIXIN_REFRESH_TOKEN_URL; //access token刷新的URL

private static Logger log = LoggerFactory.getLogger(CommonUtil.class);

/**
 * @see HttpServlet#HttpServlet()
 */
public OAuth2Servlet() {
    super();
    // TODO Auto-generated constructor stub
}

/**
 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
 */

public void doGet(HttpServletRequest request, HttpServletResponse response)  
        throws ServletException, IOException {  
    System.out.println("来自微信的请求...");  
    request.setCharacterEncoding("UTF-8");  
    response.setCharacterEncoding("UTF-8");  
    String code = request.getParameter("code");//获取OAuth2.0请求后，服务器返回的code内容,这个code在接下来向微信服务请求用户信息的时候要用到
    String menuIndex=request.getParameter("state");  //菜单点击时传入的参数，用来分辨哪个菜单被点击了
    
    String openid = "";   //用来接收用户的appid
    String accessToken="";  //用户的访问token
    String userImageURL=""; //用户头像
    String refreshToken=""; //刷新使用的access token
    //调用生成结果的JSON
    JSONObject resultObject=null;
    
    //TODO 考虑对每个用户进行页面授权缓存，可以减少网络访问压力，目前暂时用户每次说都直接重新获取
    resultObject=WeiXinURLRun.getoAuthAccessToken(code);
    
  	openid = resultObject.getString("openid");  //获取得到用户appid  
    accessToken=resultObject.getString("access_token"); //访问token,用来 摘取用户信息
    refreshToken=resultObject.getString("refresh_token"); //刷新 的refresh token
    log.info("当前使用的code:{},获取的网页访问授权access code:{}",code,accessToken);
    int verifyTokenResult=0;
    verifyTokenResult=WeiXinURLRun.verifyAccessToken(accessToken,openid); //校验当前的access token是不是有效，0为有效，其它为无效
     //如果当前的access token无效，则刷新
    if(verifyTokenResult!=0){
            System.out.println("verifyTokenResult:"+verifyTokenResult);
            //刷新access token以便取得用户的相关信息
        	accessToken=WeiXinURLRun.refreshAccessToken(refreshToken,openid);
        	System.out.println("refresh accessToken:"+accessToken);
        }
        userImageURL=WeiXinURLRun.getUserImage(accessToken,openid);  //用户头像URL
    
    //request中参数设置，暂时没有使用
    request.setAttribute("code", code);  
    request.setAttribute("openid", openid);  
    System.out.println("当前用户的CODE:"+code+"当前用户的openid:"+openid);
    
    //把openid保存到session，供其它页面使用时判断
	HttpSession session = request.getSession();
	session.setAttribute("openid", openid);
	session.setAttribute("headimgurl", userImageURL);
    
    //根据传入的state参数来判断是哪个菜单被点击了，然后转发到相应的网页
	RequestDispatcher res;
    if(menuIndex.equals("21")){
    	System.out.println("转向页面:手机激活");
    	res = request.getRequestDispatcher("/MobileActive.html");  //跳转页面  
        res.forward(request, response);	
    }else if(menuIndex.equals("31")){
    	System.out.println("转向页面:尝鲜预定");
    	res = request.getRequestDispatcher("/ChooseFruit.html");  //跳转页面  
        res.forward(request, response);	
    	
    }else if(menuIndex.equals("32")){
    	System.out.println("转向页面:我的订单");        	
    	res = request.getRequestDispatcher("/MyOrder.html");  //跳转页面  
        res.forward(request, response);	        	
    }else if(menuIndex.equals("13")){
    	System.out.println("转向页面:Test");        	
    	res = request.getRequestDispatcher("/JSSDKTest.html");  //跳转页面  
        res.forward(request, response);	        	
    }
      
}  
    public static void main(String[] args) throws ServletException, IOException {  
    	int verifyResult;
    	String newAccessToken="";
	String accessToken="vtugQkkmznJ6o-Vv1snltulQlJAYvIj0MLz7Stq3X-xX3igEJsymd4L5lSefxowP6PLWKtCcQER4obfzGFhGjkBv16ykfHFp7fs8zRwjuLg";
	String openid="oJePmvmqwkHk4DscKo7za4wB63UU";
//    	String refreshAccessToken="5cer1tg7gHoT4LCyRa0jtBGOL_rv-JiabGzMeLyDcR7w3ULU1RnA_F_DZ_paU103KT-g0ScSU_JeUK_6ixctx2tg95ulYmZBJAxFHuHh9y8";
	String refreshAccessToken="rtIr4by3TREh29GRHEthikAd8y8wEAIiSqFH15FIdAclOR5zmtMQmUf4T2wKPp5mlxhztnh2gxXAGe0UbpeTgoeniSJA7Zqnen2UAss3Rqk";
	
	verifyResult=WeiXinURLRun.verifyAccessToken(accessToken,openid);
	
	//refresh access token
//	newAccessToken=WeiXinURLRun.refreshAccessToken(refreshAccessToken,openid);
	System.out.println("newAccessToken:"+newAccessToken);
}

	/**
 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
 */
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	// TODO Auto-generated method stub
	}

}

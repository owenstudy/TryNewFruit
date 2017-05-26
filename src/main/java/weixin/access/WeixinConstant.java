package weixin.access;

import configure.property.SetSystemProperty;

//定义微信中用到的各个 常用参数，暂时放在这个中方便变更时修改
public class WeixinConstant {
	//变更类，用来保存常用的参数
	public static SetSystemProperty configure;
	
	//APPID
	public static final String APPID =configure.readValue("wechat.appid");
	//APPSECRET
	public static final String APPSECRET = configure.readValue("wechat.appsecret");
	//微信授权后重定位的URL
	public static final String REDIRECT_URI = configure.readValue("wechat.redirecturi");
	//微信访问的token
	public static final String TOKEN = configure.readValue("wechat.token");;
	//这个是请求获取webAuth访问的链接
	public static final String WEIXIN_OAUTH_ACCESS_URL="https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=APPSECRET&code=CODE&grant_type=authorization_code"; 
	//这个是请求获取用户基本信息的URL
	public static final String WEIXIN_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	//校验access token有效性的URL
	public static final String WEIXIN_VERIFY_TOKEN_URL ="https://api.weixin.qq.com/sns/auth?access_token=ACCESS_TOKEN&openid=OPENID";
	//刷新access token 的URL
	public static final String WEIXIN_REFRESH_TOKEN_URL   ="https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
	//JSSDK ticket URL
	public static final String WEIXIN_JSTICKET_URL= "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN"+"&type=jsapi";
	//获取access token的URL
	public static final String WEIXIN_ACCESS_TOKEN_URL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	
}

package weixin.access;

//定义微信中用到的各个 常用参数，暂时放在这个中方便变更时修改
public class WeixinConstant {
	//APPID
	public static final String APPID = "wxabb5aaef2a8dfb73";
	//APPSECRET
	public static final String APPSECRET = ""; 
	//微信授权后重定位的URL
	public static final String REDIRECT_URI = "http://trynewfruit.bceapp.com/OAuth2Servlet";
	//微信访问的token
	public static final String TOKEN = "";
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

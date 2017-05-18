package com.weixin.common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import com.weixin.common.MyX509TrustManager;
import com.weixin.common.CommonUtil;
import com.weixin.common.Token;

import org.junit.Test;

import weixin.access.WeixinConstant;

/** 
 * @author  作者: Owen_Study
 * @Email   邮箱: owen_study@126.com
 * @version 创建时间：May 10, 2017 12:27:24 PM 
 * 类说明 
 */
public class TokenTest {
//	@Test
    public void testGetToken1() throws Exception {
        String tokenUrl = WeixinConstant.WEIXIN_ACCESS_TOKEN_URL;
        tokenUrl=tokenUrl.replace("APPID", WeixinConstant.APPID).replace("APPSECRET", WeixinConstant.APPSECRET);
        // 建立连接
        URL url = new URL(tokenUrl);
        HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();

        // 创建SSLContext对象，并使用我们指定的信任管理器初始化
        TrustManager[] tm = { new MyX509TrustManager() };
        SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
        sslContext.init(null, tm, new java.security.SecureRandom());
        // 从上述SSLContext对象中得到SSLSocketFactory对象
        SSLSocketFactory ssf = sslContext.getSocketFactory();

        httpUrlConn.setSSLSocketFactory(ssf);
        httpUrlConn.setDoOutput(true);
        httpUrlConn.setDoInput(true);

        // 设置请求方式（GET/POST）
        httpUrlConn.setRequestMethod("GET");

        // 取得输入流
        InputStream inputStream = httpUrlConn.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(
                inputStream, "utf-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        // 读取响应内容
        StringBuffer buffer = new StringBuffer();
        String str = null;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
        }
        bufferedReader.close();
        inputStreamReader.close();
        // 释放资源
        inputStream.close();
        httpUrlConn.disconnect();
        // 输出返回结果
        System.out.println(buffer);
    }

//    @Test
    public void testGetToken2() {
        Token token = CommonUtil.getToken(WeixinConstant.APPID,WeixinConstant.APPSECRET);
        System.out.println("access_token:"+token.getAccessToken());
        System.out.println("expires_in:"+token.getExpiresIn());
    }
		    
}

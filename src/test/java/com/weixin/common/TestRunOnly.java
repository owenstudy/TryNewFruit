package com.weixin.common;

import mobile.active.SMSSender;
import publicsetting.PublicParameters;
import weixin.access.WeixinConstant;

/** 
 * @author  作者: Owen_Study
 * @Email   邮箱: owen_study@126.com
 * @version 创建时间：May 27, 2017 5:57:58 PM 
 * 类说明 
 */
public class TestRunOnly {
    public static void main(String[] args) {  

    System.out.println("call oAuth:appid");
	System.out.println(WeixinConstant.APPID);
	String httpResponse;
	System.out.println("PublicParameters.SMSSINGATURE:"+PublicParameters.SMSSINGATURE);
	if(PublicParameters.REAL_SEND_SMS){
		 System.out.println("PublicParameters.SMSSINGATURE:"+PublicParameters.SMSSINGATURE);
		 httpResponse =  SMSSender.sendMessage("13166366407","Test verify code."+PublicParameters.SMSSINGATURE,"verify_code");	
	}	
    }
    
    
}

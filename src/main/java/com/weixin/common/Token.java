package com.weixin.common;
/** 
 * @author  作者: Guangjun Li
 * @version 创建时间：May 9, 2017 5:43:38 PM 
 * 微信的access token相关信息
 */
public class Token {

	    // 接口访问凭证
	    private String accessToken;
	    // 凭证有效期，单位：秒
	    private int expiresIn;

	    public String getAccessToken() {
	        return accessToken;
	    }

	    public void setAccessToken(String accessToken) {
	        this.accessToken = accessToken;
	    }

	    public int getExpiresIn() {
	        return expiresIn;
	    }

	    public void setExpiresIn(int expiresIn) {
	        this.expiresIn = expiresIn;
	    }
}

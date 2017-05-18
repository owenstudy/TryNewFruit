package com.weixin.common;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weixin.access.WeixinConstant;
import weixin.web.WeiXinURLRun;

/**
 * Servlet implementation class WeiXinJSSDK
 */
@WebServlet("/WeiXinJSSDK")
public class WeiXinJSSDK extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(WeiXinJSSDK.class);
	      
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WeiXinJSSDK() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		JSONObject jsSDK=new JSONObject();
		StringBuffer url = request.getRequestURL();
		String testurl=request.getRequestURI();
		System.out.println("testurl:"+testurl);
		String strURL=url.toString();
		strURL=strURL+".html";
		strURL="http://trynewfruit.bceapp.com/JSSDKTest.html";
		System.out.println("strURL:"+strURL);
		jsSDK=WeiXinURLRun.getJSSDKConfigue(strURL);
		//jsSDK=WeiXinURLRun.getJSSDKConfigue("");
        PrintWriter pw = response.getWriter();   
        pw.print(jsSDK.toString());  
        pw.close();
	}

}

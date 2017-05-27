package integration.sms;


import mobile.active.SMSSender;
import publicsetting.PublicParameters;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;

/**
 * Created by chundong.hcd on 2017/4/21.
 *
 * /integrationsms
 * notify when phone user get fruit assign
 *
 */
@WebServlet("/integrationsms")
public class IntegrationServlet extends HttpServlet {

    private static Logger logger = LoggerFactory.getLogger(IntegrationServlet.class);

    private static final String NOTIFY_MSG = "亲爱的用户，恭喜您收到水果啦，请到微信公众号激活手机号并领取水果。";

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {



        String phone = request.getParameter("phone");

        String content = request.getParameter("content");

        if(null == content){
        	content = NOTIFY_MSG;
        }
        logger.info("get phone:{} and will send notify message:\n{}", phone, content);
//        System.out.println("get phone: and will send notify message:"+phone+ content);
        //调用发短信接口，收件人：phone，消息内容：content
        // TODO
//        String httpResponse =  SMSSender.sendMessage(phone,content+PublicParameters.SMSSINGATURE,"verify_code");
    	String httpResponse;
    	if(PublicParameters.REAL_SEND_SMS){
    		 httpResponse =  SMSSender.sendMessage(phone,content,"verify_code");	
    	}else{
        	//以下是虚拟调用    	
    		JSONObject resp = new JSONObject();
    		resp.put("error", 0);
    		resp.put("msg", "模拟测试错误");
    		httpResponse=resp.toString();
    		logger.info("模拟短信发送到:{}，以下内容已经成功发送:{}",phone,content+PublicParameters.SMSSINGATURE);
    	}
		//对调用结果进行分析
        JSONObject result = new JSONObject(httpResponse);
        try {
            JSONObject jsonObj = new JSONObject(httpResponse);
            
            int error_code = jsonObj.getInt("error");
            String error_msg = jsonObj.getString("msg");
            if(error_code==0){
                System.out.println("Send message success.mobile:"+phone);
                result.put("sendstatus", "success");
            }else{
                System.out.println("Send message failed,code is "+error_code+",msg is "+error_msg);
                result.put("sendstatus", "failed");
            }
        } catch (JSONException ex) {
            //Logger.getLogger(SMSSender.class.getName()).log(Level.SEVERE, null, ex);
//            result.put("sendstatus", "failed");;
        }

    }
}

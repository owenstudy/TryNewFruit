package mobile.active;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Date; 
import java.util.Calendar;
import java.text.SimpleDateFormat; 

import org.json.JSONObject;

/**
 * Servlet implementation class SMSVerifyCodeSender
 */
@WebServlet("/SMSVerifyCodeSender")
public class SMSVerifyCodeSender extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SMSVerifyCodeSender() {
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
		// Auto-generated method stub
		//取得传过来的参数mobile
		String mobile=request.getParameter("mobile");
		//调用接口发送校验码到手机
		String sendStatus=SMSVerifyCode.SendVerifyCode(mobile); 
		//把调用结果返回
        JSONObject jsonSendStatus = new JSONObject(sendStatus);
		JSONObject resp = new JSONObject();
		resp.put("mobile", mobile);
		resp.put("sendstatus", jsonSendStatus.get("sendstatus"));
		//resp.put("verify_code", jsonSendStatus.get("verify_code"));
		
		//保存成功发送的验证码到session
		HttpSession session = request.getSession();
		Date sendtime = new Date(); 
		
		System.out.println("SMSVerifyCodeSender:" + session);
		System.out.println(mobile);
		
		session.setAttribute("mobile", mobile);
		session.setAttribute("sendstatus", jsonSendStatus.get("sendstatus"));
		session.setAttribute("verify_code", jsonSendStatus.get("verify_code"));
		session.setAttribute("sendtime", sendtime);
		
		//返回发送状态给调用方
        PrintWriter pw = response.getWriter();   
        pw.print(resp.toString());  
        pw.close();
	}

}

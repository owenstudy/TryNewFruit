package mobile.active;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date; 
import java.util.Calendar;
import java.text.SimpleDateFormat; 

/**
 * Servlet implementation class MobileActive
 */
@WebServlet("/MobileActive")
public class MobileActive extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public MobileActive() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 设置响应内容类型
		response.setContentType("text/html;charset=UTF-8");

		PrintWriter out = response.getWriter();
		String title = "使用 POST 方法读取表单数据";
		// 处理中文
		String name =new String(request.getParameter("mobile").getBytes("ISO8859-1"),"UTF-8");
		String docType = "<!DOCTYPE html> \n";
		out.println(docType +
		    "<html>\n" +
		    "<head><title>" + title + "</title></head>\n" +
		    "<body bgcolor=\"#f0f0f0\">\n" +
		    "<h1 align=\"center\">" + title + "</h1>\n" +
		    "<ul>\n" +
		    "  <li><b>手机号</b>："
		    + name + "\n" +
		    "  <li><b>校验码</b>："
		    + request.getParameter("verify_code") + "\n" +
		    "</ul>\n" +
		    "</body></html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Auto-generated method stub
		//doGet(request, response);
		 String mobile = request.getParameter("mobile");
		 String verify_code=request.getParameter("verify_code");
		 //系统生成的校验码
		 String system_Verify_Code; 
		 //取得系统中已经生成 的verify code，Session数据在发送验证码中已经
			HttpSession session = request.getSession();
			String session_mobile= session.getAttribute("mobile").toString();
			String session_verify_code=session.getAttribute("verify_code").toString();
			String session_sendstatus=session.getAttribute("sendstatus").toString();
			Date sesion_sendtime=(Date)session.getAttribute("sendtime");
			String openid=session.getAttribute("openid").toString();
			
			System.out.println("openid in mobile active:"+openid);
			System.out.println("MobileActive的sessionid:"+session.getId());
		 JSONObject json = new JSONObject();

		 //测试使用
	     json.put("mobile", mobile);
	     json.put("verify_code", verify_code);
	     //System.out.println(verify_code);
	     //调用激活接口TODO
	     
	    //测试session使用
	       // HttpSession session = request.getSession();
	       // session.setAttribute("mobile", mobile);
	       // session.setAttribute("verify_code", "1234");
	        //1分钟后失效
	        //session.setMaxInactiveInterval(60);

	     //TODO 调用后台接口来检查手机状态
//	     json.put("status", "ACTIVE");
	        
	        //判断session预先保存的校验 码和POST进来的校验码是不是一致
	        System.out.println("verify code in Session:"+session_verify_code);
	        Date now = new Date(); 
	        //短信验证码发送的时间和用户提交的时间进行对比，超过5分钟则提示过期
	        long verify_code_seconds =(now.getTime()-sesion_sendtime.getTime())/1000/60;
	        
	        if(session_verify_code.equals(verify_code) && session_sendstatus.equals("success") && verify_code_seconds<5){
               json.put("verify_code_status", "true");	        	
	        }else{
	        	json.put("verify_code_status", "false");	 
	        };
	        
	        PrintWriter pw = response.getWriter();   
	        pw.print(json.toString());  
	        pw.close();
	}

}

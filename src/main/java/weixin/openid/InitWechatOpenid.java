package weixin.openid;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

/**
 * Servlet implementation class InitWechatOpenid
 */
@WebServlet("/InitWechatOpenid")
public class InitWechatOpenid extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InitWechatOpenid() {
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
		HttpSession session = request.getSession();
		System.out.println("当InitWechatid调用时的sessionid:"+session.getId());		
		String openid;
		String headimgurl=""; 
		if(session.getAttribute("openid")!=null){
			openid=session.getAttribute("openid").toString();
			headimgurl=session.getAttribute("headimgurl").toString();
		}else{
			openid="xxxxx";
		}
//		String openid=session.getAttribute("openid").toString();
		System.out.println("调用InitWechatOpenid时Session中的openid:"+openid);
		//从session中取到openid 
		JSONObject json = new JSONObject();
	     json.put("openid", openid);
	     json.put("headimgurl", headimgurl);
        //返回openid
        PrintWriter pw = response.getWriter();   
        pw.print(json.toString());  
        pw.close();

	}

}

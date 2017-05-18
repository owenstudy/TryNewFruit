package order.choosefruit;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

/**
 * Servlet implementation class ChooseFruit
 */
@WebServlet("/ChooseFruit")
public class ChooseFruit extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChooseFruit() {
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
		//  Auto-generated method stub
		//保存成功发送的验证码到session
		HttpSession session = request.getSession();
		String chosefruitlist=request.getParameter("fruitList").toString();
//		System.out.println("选择水果列表:"+chosefruitlist);
//		JSONObject jsonFruitList = new JSONObject(chosefruitlist);
//		jsonFruitList.put("fruitList", chosefruitlist);
		session.setAttribute("fruitList", chosefruitlist);
		System.out.println("用户选择的水果列表："+session.getAttribute("fruitList").toString());
	}

}

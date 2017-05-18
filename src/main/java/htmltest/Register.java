package htmltest;

import java.io.IOException;  

//为了Servlet能够传值回前台的Jsp要使用request.getRequestDispatcher("").forward();方法，此方法要求必须处理IO异常  
import javax.servlet.*;  
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;  
//要使用Servlet必须引入这个两个包  
@WebServlet("/Register")
public class Register extends HttpServlet {  
protected void doGet(HttpServletRequest request,  
        HttpServletResponse response) throws ServletException {  
    //固定部分，说明收到Get传过来的值，怎么处理，即使你的页面不用到Get去传值，最好也留个空方法在此  
    String username = request.getParameter("username");  
    String password = request.getParameter("password");  
    //接收index.jsp传过来的值，下Post同  
    request.setAttribute("username", username);  
    request.setAttribute("password", password);  
    //打包，准备把值传到WEB-INF/ok.jsp中，下Post同  
    try {  
        request.getRequestDispatcher("WEB-INF/MobileActive.html").forward(request,  
                response);  
    } catch (IOException e) {  
        e.printStackTrace();  
    }  
    //指明传输方向，下Post同  
}  

protected void doPost(HttpServletRequest request,  
        HttpServletResponse response) throws ServletException {  
    //固定部分，说明收到Post传过来的值，怎么处理  
    String username = request.getParameter("username");  
    String password = request.getParameter("password");  
    request.setAttribute("username", username);  
    request.setAttribute("password", password);  
    try {  
        request.getRequestDispatcher("WEB-INF/Index.html").forward(request,  
                response);  
    } catch (IOException e) {  
        e.printStackTrace();  
    }  
}  
}  

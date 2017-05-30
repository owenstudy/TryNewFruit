package service;

import java.util.Date;  
import java.util.Map;  

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;

import menu.main.MenuManager;
import message.resp.TextMessage;  
import service.MessageUtil;  
  
/** 
 * 核心服务类 
 *  
 * @author liufeng 
 * @date 2013-05-20 
 */  
public class CoreService {  
	//输入内容是菜单，则进入新的流程
	public static String strMenu="";
	//自定义的谁证码
	public static String strMenuPassword="";
	//自定义的菜单链接
	public static String strMenuURI="";
	
    /** 
     * 处理微信发来的请求 
     *  
     * @param request 
     * @return 
     */  
    public static String processRequest(HttpServletRequest request) {  
        String respMessage = null;  
        try {  
            // 默认返回的文本消息内容  
            String respContent = "请求处理异常，请稍候尝试！";  
  
            // xml请求解析  
            Map<String, String> requestMap = MessageUtil.parseXml(request);  
  
            // 发送方帐号（open_id）  
            String fromUserName = requestMap.get("FromUserName");  
            // 公众帐号  
            String toUserName = requestMap.get("ToUserName");  
            // 消息类型  
            String msgType = requestMap.get("MsgType");  
            
  
            // 回复文本消息  
            TextMessage textMessage = new TextMessage();  
            textMessage.setToUserName(fromUserName);  
            textMessage.setFromUserName(toUserName);  
            textMessage.setCreateTime(new Date().getTime());  
            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);  
            textMessage.setFuncFlag(0);  
  
            // 文本消息  
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {  
                //消息内容
                String msgContent=requestMap.get("Content");  
//                respContent = "您发送的是文本消息！";  
            	respContent=MenuProcess(msgContent);
                System.out.println("您发送的是文本消息！");
            }  
            // 图片消息  
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {  
                respContent = "您发送的是图片消息！";  
            }  
            // 地理位置消息  
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {  
                respContent = "您发送的是地理位置消息！";  
            }  
            // 链接消息  
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {  
                respContent = "您发送的是链接消息！";  
            }  
            // 音频消息  
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {  
                respContent = "您发送的是音频消息！";  
            }  
            // 事件推送  
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {  
                // 事件类型  
                String eventType = requestMap.get("Event");  
                // 订阅  
                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {  
                    respContent = "谢谢您的关注！";  
                }  
                // 取消订阅  
                else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {  
                    // TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息  
                }  
                // 自定义菜单点击事件  
                else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {  
                    // TODO 自定义菜单权没有开放，暂不处理该类消息  
                	 // 事件KEY值，与创建自定义菜单时指定的KEY值对应  
                    String eventKey = requestMap.get("EventKey");  
  
                    if (eventKey.equals("11")) {  
                        respContent = "11菜单项被点击！";  
                    } else if (eventKey.equals("12")) {  
                        respContent = "12菜单项被点击！";  
                    } else if (eventKey.equals("13")) {  
                        respContent = "13菜单项被点击！";  
                    } else if (eventKey.equals("14")) {  
                        respContent = "14菜单项被点击！";  
                    } else if (eventKey.equals("21")) {  
                        respContent = "21菜单项被点击！";  
                    } else if (eventKey.equals("22")) {  
                        respContent = "22菜单项被点击！";  
                    } else if (eventKey.equals("23")) {  
                        respContent = "23菜单项被点击！";  
                    } else if (eventKey.equals("24")) {  
                        respContent = "24菜单项被点击！";  
                    } else if (eventKey.equals("25")) {  
                        respContent = "25菜单项被点击！";  
                    } else if (eventKey.equals("31")) {  
                        respContent = "31菜单项被点击！";  
                    } else if (eventKey.equals("32")) {  
                        respContent = "32菜单项被点击！，当前用户的openid:"+fromUserName;  
                        //TODO 打开我的菜单网页
                       
                    } else if (eventKey.equals("33")) {  
                        respContent = "33菜单项被点击！";  
                    }  
                }  
            }  
  
            textMessage.setContent(respContent);  
            respMessage = MessageUtil.textMessageToXml(textMessage);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        return respMessage;  
    }  

//在消息中处理每个月的水果介绍菜单    
public static String MenuProcess(String textMessage){
	String responseString="";
	String menuPassword="trynewfruit";
	//接收到菜单消息时返回
	if(textMessage.equals("菜单")){
		responseString="请回复输入菜单定义的验证码";
		strMenu="menu";
	}else if (strMenu.equals("menu") && textMessage.equals(menuPassword)){
		//有值代表校验码验证通过
		strMenuPassword=menuPassword;
		responseString="请回复输入每个月的水果介绍网页链接。如 : https://m.eqxiu.com/s/Vb1JaE6g";
	}else if (strMenu.equals("menu") && strMenuPassword.equals(menuPassword)){
		//校验URI有有效性
		URLAvailability  ua = new URLAvailability();
		boolean flag = ua.isConnected(textMessage);
		if (flag==true){
			//开始创建菜单
			boolean menuFlag=MenuManager.createMenu12(textMessage);
			if (menuFlag==true){
				responseString="菜单创建成功！";	
				strMenu="";
				strMenuPassword="";
				strMenuURI="";
			}else{
				responseString="菜单创建失败，请确认链接是否正确，不能含有空行或联系系统管理员解决！";				
			}
		}else{
			responseString="网页链接不是有效的链接，请重新输入!";
		}
	}else{
		responseString="如果您有任何疑问，你可以留言或者给我们打电话!";
	}
	
	return responseString;
}
}  


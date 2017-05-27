package menu.main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weixin.access.WeixinConstant;
import menu.pojo.AccessToken;
import menu.pojo.Button;
import menu.pojo.CommonButton;
import menu.pojo.ComplexButton;
import menu.pojo.Menu;
import menu.util.WeixinUtil;  
import menu.pojo.ViewButton;

import java.net.*;
  
/** 
 * 菜单管理器类 
 *  
 * @author liufeng 
 * @date 2013-08-08 
 */  
public class MenuManager {  
    private static Logger log = LoggerFactory.getLogger(MenuManager.class);  
  
    public static void main(String[] args) {  
        // 第三方用户唯一凭证  
//        String appId = "wxabb5aaef2a8dfb73";  
    	String appId = WeixinConstant.APPID;
        // 第三方用户唯一凭证密钥  
        String appSecret = WeixinConstant.APPSECRET;  
  
        // 调用接口获取access_token  
        AccessToken at = WeixinUtil.getAccessToken(appId, appSecret);  
  
        if (null != at) {  
            // 调用接口创建菜单  
            int result = WeixinUtil.createMenu(getMenu(), at.getToken());  
  
            // 判断菜单创建结果  
            if (0 == result)  
                log.info("菜单创建成功！");  
            else  
                log.info("菜单创建失败，错误码：" + result);  
        }  
    }  
  
    /** 
     * 组装菜单数据 
     *  
     * @return 
     */  
    private static Menu getMenu() {  
    	/*
        CommonButton btn11 = new CommonButton();  
        btn11.setName("关于偿鲜");  
        btn11.setType("click");  
        btn11.setKey("11"); 
        */
    	//重定向的网址
    	String redirect_URI=WeixinConstant.REDIRECT_URI;
        String oAuthURL="https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
        oAuthURL=oAuthURL.replace("APPID", WeixinConstant.APPID).replace("REDIRECT_URI",redirect_URI).replace("SCOPE", "snsapi_userinfo");  //snsapi_base
        System.out.println(oAuthURL);
    	
        ViewButton btn11 = new ViewButton();  
        btn11.setName("关于偿鲜");  
        btn11.setType("view");  
        btn11.setUrl("http://i.eqxiu.com/s/NHQ3QnuY");  
  

        ViewButton btn12 = new ViewButton();  
        btn12.setName("水果计划");  
        btn12.setType("view");
        //TODO
        btn12.setUrl("http://i.eqxiu.com/s/NHQ3QnuY");
        
//        ViewButton btn13 = new ViewButton();  
//        btn13.setName("Test");  
//        btn13.setType("view");
//        //TODO
//        //btn13.setUrl(oAuthURL.replace("STATE", "13"));
//        btn13.setUrl("http://trynewfruit.bceapp.com/JSSDKTest.html");
        
        /*
        CommonButton btn12 = new CommonButton();  
        btn12.setName("水果类型");  
        btn12.setType("click");  
        btn12.setKey("12");
        */
//        CommonButton btn13 = new CommonButton();  
//        btn13.setName("测试使用");  
//        btn13.setType("click");  
//        btn13.setKey("13");  
        /*  
        CommonButton btn14 = new CommonButton();  
        btn14.setName("历史上的今天");  
        btn14.setType("click");  
        btn14.setKey("14");  
  */
        ViewButton btn21 = new ViewButton();  
        btn21.setName("手机激活");  
        btn21.setType("view");  
        //设置传送菜单定位参数
        btn21.setUrl(oAuthURL.replace("STATE", "21"));  
/*
        CommonButton btn21 = new CommonButton();  
        btn21.setName("验证派送");  
        btn21.setType("click");  
        btn21.setKey("21");  
  */      
        
  
        /*
        CommonButton btn22 = new CommonButton();  
        btn22.setName("经典游戏");  
        btn22.setType("click");  
        btn22.setKey("22");  
  
        CommonButton btn23 = new CommonButton();  
        btn23.setName("美女电台");  
        btn23.setType("click");  
        btn23.setKey("23");  
  
        CommonButton btn24 = new CommonButton();  
        btn24.setName("人脸识别");  
        btn24.setType("click");  
        btn24.setKey("24");  
  
        CommonButton btn25 = new CommonButton();  
        btn25.setName("聊天唠嗑");  
        btn25.setType("click");  
        btn25.setKey("25");  
        */
//  
//        CommonButton btn31 = new CommonButton();  
//        btn31.setName("订单派送");  
//        btn31.setType("click");  
//        btn31.setKey("31");  
        
        ViewButton btn31 = new ViewButton();  
        btn31.setName("偿鲜预定");  
        btn31.setType("view");  
        //设置传送菜单定位参数
        btn31.setUrl(oAuthURL.replace("STATE", "31"));  
        
        ViewButton btn32 = new ViewButton();  
        btn32.setName("我的订单");  
        btn32.setType("view");  
        //设置传送菜单定位参数
        btn32.setUrl(oAuthURL.replace("STATE", "32"));  
  
//        CommonButton btn32 = new CommonButton();  
//        btn32.setName("我的订单");  
//        btn32.setType("click");  
//        btn32.setKey("32");  
  /*
        CommonButton btn33 = new CommonButton();  
        btn33.setName("幽默笑话");  
        btn33.setType("click");  
        btn33.setKey("33");  
  */
        ComplexButton mainBtn1 = new ComplexButton();  
        mainBtn1.setName("了解偿鲜");  
        mainBtn1.setSub_button(new Button[] { btn11, btn12 });  
  
        ComplexButton mainBtn2 = new ComplexButton();  
        mainBtn2.setName("手机激活");  
        mainBtn2.setSub_button(new Button[] { btn21});  
  
        ComplexButton mainBtn3 = new ComplexButton();  
        mainBtn3.setName("我的尝鲜");  
        mainBtn3.setSub_button(new Button[] { btn31, btn32 });  
  
        /** 
         * 这是公众号xiaoqrobot目前的菜单结构，每个一级菜单都有二级菜单项<br> 
         *  
         * 在某个一级菜单下没有二级菜单的情况，menu该如何定义呢？<br> 
         * 比如，第三个一级菜单项不是“更多体验”，而直接是“幽默笑话”，那么menu应该这样定义：<br> 
         * menu.setButton(new Button[] { mainBtn1, btn21, mainBtn3 }); 
         */  
        Menu menu = new Menu();  
        menu.setButton(new Button[] { mainBtn1, btn21,mainBtn3  });  
  
        return menu;  
    }  
}  

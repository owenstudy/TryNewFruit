package mobile.active;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import publicsetting.PublicParameters;

//生成短信校验码
public class SMSVerifyCode {
	
	 //test
	/*
    public static void main(String[] args) {
    	String randomVal;
    	randomVal=SMSVerifyCode.CreateRandomCode();
    	System.out.println(randomVal);
    	
    	SMSVerifyCode.SendVerifyCode("13166366407");
    }
    */
	
    
    //随机生成6位的校验码
	private static String CreateRandomCode()
    {
    	java.util.Random random=new java.util.Random();// 定义随机类
    	int result=random.nextInt(1000000);// 返回[0,10)集合中的整数，注意不包括10
    	//只生成6位数的验证码
    	while(result<100000){
    		result=random.nextInt(1000000);
    	}
    	
    	return String.valueOf(result+1);              // +1后，[0,10)集合变为[1,11)集合，满足要求
	
}
  //发送手机校验码
	public static String SendVerifyCode(String mobile){
		String VerifyCode; 
		//生成随机的验证码
		VerifyCode=SMSVerifyCode.CreateRandomCode(); 
    	String message="验证码："+VerifyCode+PublicParameters.SMSSINGATURE; 
    	System.out.println("message:"+message);
        //TODO在生产环境才调用，测试用虚拟的值 
    	String httpResponse;
    	if(PublicParameters.REAL_SEND_SMS){
    		 httpResponse =  SMSSender.sendMessage(mobile,message,"verify_code");	
    	}else{
        	//以下是虚拟调用    	
    		JSONObject resp = new JSONObject();
    		resp.put("error", 0);
    		resp.put("msg", "模拟测试错误");
    		httpResponse=resp.toString();		
    	}
    		
		//对调用结果进行分析
        JSONObject result = new JSONObject(httpResponse);
        try {
            JSONObject jsonObj = new JSONObject(httpResponse);
            
            int error_code = jsonObj.getInt("error");
            String error_msg = jsonObj.getString("msg");
            if(error_code==0){
                System.out.println("Send message success.mobile:"+mobile+", verify_code:"+VerifyCode);
                result.put("sendstatus", "success");
            }else{
                System.out.println("Send message failed,code is "+error_code+",msg is "+error_msg);
                result.put("sendstatus", "failed");
            }
        } catch (JSONException ex) {
            Logger.getLogger(SMSSender.class.getName()).log(Level.SEVERE, null, ex);
            result.put("sendstatus", "failed");;
        }
        
        result.put("mobile",mobile);
        result.put("verify_code", VerifyCode);
        return result.toString();
		
	}
	
    
}

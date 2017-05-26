package publicsetting;

import configure.property.SetSystemProperty;

public class PublicParameters {
	//变更类，用来保存常用的参数
	public static SetSystemProperty configure;	
//	短信的发送签名，从短信服务器获取后在些更新
	public static final String  SMSAPIKEY= "key-"+configure.readValue("sms.apikey");
//	短信的签名，一般是公司的名称
	public static final String SMSSINGATURE = configure.readValue("sms.singature");
//	是否真正发送SMS，在测试阶段可以关闭，在生产环境需要打开
	public static final boolean REAL_SEND_SMS =Boolean.valueOf(configure.readValue("sms.realsend")).booleanValue() ;
	

}

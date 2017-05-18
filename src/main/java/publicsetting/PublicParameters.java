package publicsetting;

public class PublicParameters {
//	短信的发送签名，从短信服务器获取后在些更新
	public static final String  SMSAPIKEY= "key-"+"25eeafd835ded2ea4a78fd6d38f5d063";
//	短信的签名，一般是公司的名称
	public static final String SMSSINGATURE = "【尝尝鲜】";
//	是否真正发送SMS，在测试阶段可以关闭，在生产环境需要打开
	public static final boolean REAL_SEND_SMS = true;
	

}

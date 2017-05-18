     //Ajax, 点击获取验证码后到服务器生成发送
	$(document).ready(function(){
    		//从session中取当前用户的openid
    		initOpenid()    		
    		//mobile点击校验事件	
    		mobileClick();
    		$('#StartActive').click( function(){
	    		 //把数据传送到session中
    			startActiveClick();
	    	 });
       	 //刚开始打开时，默认的按钮【开始激活】不能点击，只有在输入验证码后才能开始激活
       	 $("#StartActive").attr("disabled",true);
 		//自动在网页中增加统一的页脚
 		addFooter();
    	 
     });


//取得手机的校验码
//     function getMobileVerifyCode(){
//    	 form.action="MobileActive";
//         form.submit();
//     }

     //点击得到验证码后的计时器
     var countdown=60; 
     function settime(obj) { 
//    	 mobile=document.getElementsByName("mobile").value;
    	 //只有手机号码合法时才会进入倒计时
    	 if (mobileNumCheck()){
         if (countdown == 0) { 
             obj.removeAttribute("disabled");    
             obj.innerHTML="获取验证码"; 
             countdown = 60; 
             return;
         } else { 
             obj.setAttribute("disabled", true); 
             obj.innerHTML="重新发送(" + countdown + ")"; 
             countdown--; 
         } 
     setTimeout(function() { 
         settime(obj) }
         ,1000) 
     }
     }
     
     //根据POST得到的反馈的情况进行处理
     //ACTIVE的提示用户无须再进行激活
     //NONACTIVE并且存在
     function postMobileCheck(data, status){
    	 
    	 if(status=='success'){
				console.log('active post return data:'+data);
				var jsonResult=JSON.parse(data);
				var mobile_verify_status=jsonResult.verify_code_status;
				var openid=jsonResult.openid; 
				console.log('手机校验码校验状态:'+mobile_verify_status);
					//对未激活的手机发送验证码
					if(mobile_verify_status=='false'){
						MsgInfo("输入验证码错误");
						//$('#dialog_verify_code_error').fadeIn(200);						
						//alert('验证码错误！');
						console.log('输入验证码错误!');
					}else{
						//开始激活操作
						console.log('开始手机激活操作.......');
						//调用接口激活
						//需要接收微信传递的wechatOPENid
						activeMobile(jsonResult.mobile,getCurrOpenid());
						//alert('手机已经成功激活，你可以现在或者其它时间到【我的偿鲜】中进行订单预定')
					}
    	 //POST状态失败的处理
    	 }else{    		
		   MsgInfo("服务器处理异常，请稍候再试！");
    		 //$('#dialog_systemerror').fadeIn(200);               	    		 
    		 //alert("服务器处理异常，请稍候再试！");
    	 }
     }
     

//mobile点击校验事件
function mobileClick(){
	$("#getVerifyCode").click(function(){
		  input_mobile=$("input[id='mobile']").val();
		 //调用后台接口来检查电话号码是不是存在
		 $.get(httpURL+"rest/user/validatePhone/"+input_mobile,
		function(data,status){
			 //对获取信息进行解析
			 mobileStatusVerify(data,status);
		 });
	 });
}     
//对返回的手机信息进行解析     
function mobileStatusVerify(data,status){
	//postMobileCheck(data,status);
		console.log("Server return data:"+data.toString());
		//var jsonResult=JSON.parse(data);
		var jsonResult = data; 		
		console.log("mobile:"+input_mobile+" status is:"+jsonResult.value);
		//手机号码在后台的检查，只有从服务器正确返回时才进行处理
       if(status=="success"){     
       	//手机号码在后台不存在
       	if (jsonResult.value=="NA"){
				MsgInfo("手机号在系统中未注册，需要注册后才能激活");
         		 $("#StartActive").attr("disabled",true);
       		//$('#dialog_mobile_noregister').fadeIn(200);                    																	
				//alert("手机号在系统中未注册，需要注册后才能激活");					
		   } 
		   //手机号码已经激活	   
       	else if (jsonResult.value=="ACTIVE") {
				MsgInfo("手机已经激活！");
				//只有成功发送短信后才把开始激活按钮变成可点击
         		 $("#StartActive").attr("disabled",true);
       		//$('#dialog_mobile_actived').fadeIn(200);                    																		   
		   }
		   //后台已经存在并且处于未激活状态	   
      	else if(jsonResult.value=="NOTACTIVE"){
         //调用接口开始发送验证码短信	
      		sendSMSVerifyCode(input_mobile);   
       }else{
       	MsgInfo("调用接口失败，请稍微再试！");
       	//$('#"dialog_SMS_send_failed"').fadeIn(200); 
       };                    	
       };         
}

//Ajax，点击激活手机时提交到服务端验证是否手机已经在后台存在并且未激活
function startActiveClick(){
	 //激活按钮点击事件
		if (mobileNumCheck()){
			var input_mobile=$("input[id='mobile']").val();
			var input_verify_code=$("input[name='mobile_verify_code']").val();
			$.post("MobileActive",{
				mobile:input_mobile,
				verify_code:input_verify_code
			},
			function(data,status){
				postMobileCheck(data,status);
			});    				
		};
	
}

//发送短信验证码
     function sendSMSVerifyCode(mobile){
			$.post("SMSVerifyCodeSender",{
				mobile:mobile			},
			function(data,status){				
				//短信已经成功发出
				if(status="success"){
					//只有成功发送短信后才把开始激活按钮变成可点击
              		 $("#StartActive").attr("disabled",false);
					console.log(mobile+":短信成功发送!");
				}else{
					console.log(mobile+":校验码发送失败!");
					MsgInfo("校验码发送失败")
					//$('#"dialog_SMS_send_failed"').fadeIn(200); 					
				}
			});    				
     }
     //调用激活的程序接口
     function activeMobile(mobile, wechatOPENid){
		 $.get(httpURL+"rest/user/register/"+mobile+"/"+wechatOPENid,
		 			function(data,status){
			 if(status=="success"){
				 console.log("手机激活成功:"+mobile);
				 MsgInfo("恭喜您，手机已经成功激活，你可以到【我的偿鲜】去预定水果！");
				 //$('#dialog_activesuccess').fadeIn(200);                    	
				 
			 }else{
				 console.log("手机激活失败:"+mobile);
			 }
		 });
     }
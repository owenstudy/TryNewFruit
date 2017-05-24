//var httpURL="http://localhost:8080/FruitSMSDemo/";
//var httpURL="http://tryfruit.applinzi.com/";
var httpURL="http://www.trynewfruit.cn/";

//var httpURL="http://172.18.29.34:8080/fruit/";
//是否用真实的微信openid，只有在真实需要测试微信 的时候 才从微信服务端取出openid
var useRealOpenid=false;

var CURR_OPENID;
//订单状态公共变量
var C_ORDER_PENDING_STATUS=1;
var C_ORDER_DELIVERING_STATUS=2;
var C_ORDER_DELIVERED_STATUS=3; 

//校验手机号码的合法性
function mobileNumCheck(){
	 var re=/^(13[0-9]{9})|(15[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})$/;   
	 var mobile=document.getElementById("mobile");
	 var mobileinput=mobile.value;
//	 var mobileinput= $(this).val();
	 //var mobile=mobileinput[0]; 
//	 mobileinput=mobile;
//	 console.log(mobileinput);
	 if(!re.test(mobileinput))    		 
	  { 
		 MsgInfo(mobileinput+' 不是正确的手机号码');
			//$('#dialog_mobile_formatwrong').fadeIn(200);                    												    		 
     //alert(mobile+' 不是正确的手机号码'); 
	  return false; 
	 }
	 else
		 return true; 
}

//取得当前用户的openid
function getCurrOpenid(){
	   //TODO 需要研究如何取得openid
//	   return "1234567890";
	if(useRealOpenid==false){
		   return "oJePmvmqwkHk4DscKo7za4wB63UU";		
//		return "1111222233334444";
	}else{
		   return CURR_OPENID; 
	}
}

//从系统session中取得openid
function initOpenid(){
	$.post("InitWechatOpenid",function(data,status){
		if(status=="success"){
			var jsonResult=JSON.parse(data);
			var openid=jsonResult.openid;
			CURR_OPENID=openid;
//			console.log("curr openid:"+CURR_OPENID);			
		}else{
			CURR_OPENID="yyyyyy";
		}
//		alert("当前用户的openid:"+CURR_OPENID);
	})
}

function addFooter(){
	//每个HTML自动增加页脚
	var  strFooter=
	"<div class=\"weui-msg__extra-area\">"+
	"<div class=\"weui-footer\">"+
	"<p class=\"weui-footer__text\">Copyright &copy; 2017-2020  尝尝鲜</p>"+
	"</div>";	
	var objE = document.createElement("div");
	objE.innerHTML = strFooter; 
	document.body.appendChild(objE);
//	 console.log(objE.innerHTML) ;
	
}
//调用接口来判断是不是当前用户的wechatopenid是不是已经存在并且激活，余额>0
function validateOpenid(openid,buttonid){   
	 $.ajax({
		 method: "GET",
		 url:httpURL+"rest/user/validateWechatId/"+openid,
		 headers: {
		        "Auth": openid
		    },
		 success: function(data,status){
			 console.log("查询确认当前微信用户是不是存在， return data:"+data+"status:"+status);
			 if(data=="client unauthorized!"){
				 MsgInfo("当前用户不存在，注册激活后再试！");	
	    		 $("#"+buttonid).attr("disabled",true);				 
			 }
			 else{
				 if (data.value=="ACTIVE"){
		    		 $("#"+buttonid).attr("disabled",false);				 
				 }else{
		    		 $("#"+buttonid).attr("disabled",true);				 
				 }				 
			 }
		 },
		 error: function(){
		 }
 });
	 
};
function MsgInfo(message){
	//确认对话框
	 	var $messageDialog = $('#dialog_template');
	     $('#dialogs').on('click', '.weui-dialog__btn_primary', function(){
	         $(this).parents('.js_dialog').fadeOut(200);
	     });
	     
		 //设置显示的消息内容
		 $(".weui-dialog__bd:last").text(message);
	     $messageDialog.fadeIn(200);

}

//loading start
function startLoadingData(){
    var $loadingToast = $('#loadingToast');
        if ($loadingToast.css('display') != 'none') return;
        $loadingToast.fadeIn(100);
};

//loading finish
function finishLoadingData(){
    var $loadingToast = $('#loadingToast');
        $loadingToast.fadeOut(100);
};

//调用GET方法的公共函数
function getRestfulCall(url, openid, successcallback, failedcallback){
	startLoadingData();	
	 $.ajax({
		 method: "GET",
		 //"rest/user/validateWechatId/
		 url:httpURL+url+openid,
		 headers: {
		        "Auth": openid
		    },
		 success: function(data,status){
			 successcallback(data, status);
			 console.log("URL return data:"+JSON.stringify(data)+"status:"+status);
			 finishLoadingData();	
			 },
		 error: function(){
			 failedcallback();
			 finishLoadingData();
		 }
 });
	
}

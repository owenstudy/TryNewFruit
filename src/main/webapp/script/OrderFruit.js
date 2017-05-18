//全局变量，从session中取得用户选择的水果列表
var fruitList;
//用户输入的联系人信息
var contactInfo; 
//默认地址
var defaultAddress; 

$(document).ready(function(){
	
	//从session中取当前用户的openid
	 getSessionOpenid();    		
    //消息弹出框的提示按钮点击动作，点击后消息
	 $('#dialogs').on('click', '.weui-dialog__btn', function(){
	            $(this).parents('.js_dialog').fadeOut(200);
	        });    	
	 //用户点击事件
	 $('#OrderSubmit').click( function(){
		 saveOrder();
	 });	
	 //输入remark时的长度显示
	 remarkKeyin();
		//自动在网页中增加统一的页脚
		addFooter();
});

//从系统session中取得openid
function getSessionOpenid(){
	startLoadingData();
	$.post("InitWechatOpenid",function(data,status){
		if(status=="success"){
			var jsonResult=JSON.parse(data);
			var openid=jsonResult.openid;
			CURR_OPENID=openid;
			 //校验openid是不是正确
			 validateOpenid(getCurrOpenid(),"OrderSubmit");
			 //得到水果选择信息
			 getFruitList();
			 //得到当前用户的默认地址
			 initDefaultAddress();
//			console.log("curr openid:"+CURR_OPENID);		
			 finishLoadingData();
		}else{
			CURR_OPENID="yyyyyy";
			finishLoadingData();
		}
//		alert("当前用户的openid:"+CURR_OPENID);
	})
}


//不错得用户输入的联系人信息
function getContactInfo(){
//	ordercontract= document.getElementsByName("ordercontract");
	var ordercontract=$("#ordercontract").val();
	var ordercontractname= $("#ordercontractname").val();
	var orderaddress=  $("#orderaddress").val();
	var orderremark=  $("#orderremark").val();
	contactInfo={"ordercontract":ordercontract,"ordercontractname":ordercontractname,"orderaddress":orderaddress,"orderremark":orderremark};
	console.log("用户输入的联系信息:"+JSON.stringify(contactInfo));
}
//从服务器中取得用户选择的水果列表
function getFruitList(){
	$.get("OrderFruit",
	function(data){
//		console.log("用户选择的水果列表:"+data);
		fruitList=JSON.parse(data);
		console.log("用户选择的水果列表:"+JSON.stringify(fruitList));
	});    				
	
}
//保存订单信息到数据库
function saveOrder(){
	
	startLoadingData();
	//读取用户输入的数据
	getContactInfo();
	var orderInfo={};	
	orderInfo.fruitList=fruitList;
	orderInfo.address=contactInfo.orderaddress; 
	orderInfo.contactName=contactInfo.ordercontractname;
	orderInfo.contactPhone=contactInfo.ordercontract; 
	console.log("订单信息:"+JSON.stringify(orderInfo));
	
	var currOpenID=getCurrOpenid();
//	console.log("curr openid:"+currOpenID);
	//只有校验通过后才进行调用接口
	if(checkMandatoryField()){
	console.log("订单信息："+JSON.stringify(orderInfo));
	//默认地址标志
//	var defaultAddressIndi=$("#defaultAddressIndi").attr("checked");
	var defaultAddressIndi=document.getElementById("defaultAddressIndi").checked;
	var postURL;
	if(defaultAddressIndi==true){
		 postURL=httpURL+"rest/order/userOrder/"+getCurrOpenid()+"/1";
	}else{
		postURL=httpURL+"rest/order/userOrder/"+getCurrOpenid()+"/0";
	};
//	console.log("post URL:"+postURL);
//	调用接口来保存订单信息
// 测试通过，POST调用时需要参考这个样例
	 $.ajax({
		 method: "POST",
//		 dataType:"json",
//		 url:httpURL+"rest/order/userOrder/"+getCurrOpenid(),
		 url:postURL,
		 "crossDomain": true,
		 headers: {
		        "auth": getCurrOpenid(),
			    //此处需要设置类型，和下面data类型匹配
			    "content-type": "application/json"
		    },
		    //此处提交必须转换成字符串，否则会报下面的错误
		    //415 (Unsupported Media Type)
		    data: JSON.stringify(orderInfo),
		 success: function(data,status){
			 if(data.code=="SUCCESS"){
				 $("#OrderSubmit").attr("disabled",true);				
				 //TODO 对返回的地址根据页面的是否为默认地址选项进行处理
				 console.log("订单已经成功保存！"+JSON.stringify(orderInfo));
				 MsgInfo("订单保存成功!")
			 }else{
				 MsgInfo("订单保存失败!");
				 console.log("订单保存失败，返回数据："+JSON.stringify(data));
			 }
			 finishLoadingData();
		 },
		 error: function(){
			 finishLoadingData();
			 console.log("订单保存失败！"+JSON.stringify(orderInfo));
			 MsgInfo("订单保存失败，请稍候再试")
		 }
    });
	}
	
}
//检查字段的必输性
function checkMandatoryField(){
	var ordercontract=$("#ordercontract").val();
	var ordercontractname= $("#ordercontractname").val();
	var orderaddress=  $("#orderaddress").val();
	var orderremark=  $("#orderremark").val();
	
	//当前系统日期
	var today = new Date();//获得当前日期
	var day = today.getDate();//获得当前日期	

    if(ordercontract.length==0||ordercontractname.length==0||orderaddress.length==0){
    	MsgInfo("订单的联系人，联系电话，联系地址不能为空!");
    	return false; 
    }else if(orderremark.length>100){
    	MsgInfo("订单备注不能超过长度不能超过100!");
    	return false; 
    }
    else{
    	return true; 
    }
    
}

//输入注释的时候自动显示更新输入的字符数量
function remarkKeyin(){
	$("#orderremark").on("keyup",function(){
		var keyinWords=$("#orderremark").val();
		var keyinWordsLengh=keyinWords.length;
		$("span").html(keyinWordsLengh);
//		console.log(keyinWords);
		if(keyinWordsLengh>100){
			$("#orderremark").val(keyinWords.substr(0,keyinWordsLengh-1));
		}
//		console.log("current type in words are:"+keyinWords+$("span").html());
	});
}

//获取当前用户的默认地址
function initDefaultAddress(){
	var openid=getCurrOpenid();
	
	$.ajax({
		 method: "GET",
		 url:httpURL+"rest/order/queryAddress/"+openid,
		 headers: {
		        "Auth": openid
		    },
		 success: function(data,status){
			 console.log("Address infromation:"+data+"status:"+status);
			 var addressList=JSON.parse(data.msg);
			 //取得默认地址放在全局变量里面
			 getDefaultAddress(addressList);
			 //把默认地址显示出来 放到地址列表 TODO
			 if(defaultAddress!=""){
				 showDefaultAddress();
			 }
		 },
		 error: function(){
			 console.log("获取地址异常,请稍候再试！");
		 }
   });
}

//把默认地址显示到UI上
function showDefaultAddress(){

	$("#ordercontract").val(defaultAddress.contactPhone);
	$("#ordercontractname").val(defaultAddress.contactName);
	$("#orderaddress").val(defaultAddress.address);
}

//从地址列表中取到默认的地址
function getDefaultAddress(addressList){
	defaultAddress="";
	$.each(addressList, function(index, address){
		if (address.defaultAddr=="1"){
			defaultAddress=address;
		}
	})
}

//当前地址保存为常用地址
function saveDefaultAddress(address){
	var openid=getCurrOpenid();
	
	$.ajax({
		 method: "GET",
		 url:httpURL+"rest/order/defaultAddr/"+openid+"/"+address.id,
		 headers: {
		        "Auth": openid
		    },
		 success: function(data,status){
			 if (data.code=="SUCCESS"){
				 console.log("常用地址保存成功!");
			 }else{
				 console.log("常用地址保存失败,请稍候再试！"); 
			 }
		 },
		 error: function(){
			 console.log("常用地址保存失败,请稍候再试！");
		 }
   });
	
}


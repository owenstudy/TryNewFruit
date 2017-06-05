/**
 * 
 */
//订单的列表
var orderList=[{"id":1,"orderdate":"2017/4/19"},{"id":2,"orderdate":"2017/4/19"}];


$(document).ready(function(){
	 //检查浏览器
	 onlyWechatAccess();	
//	generateOrderList();
	getSessionOpenid();
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
			//用户头像的URL
			var headimgurl=jsonResult.headimgurl;
//			headimgurl="http://wx.qlogo.cn/mmopen/OM4v0FU2h0ukp5H9ZbGlkykbIXUea8ib2kSia1zxkIjNzDLkq3WHP65skPxzYHFBB63eeKMqfqKVMInZU4XJynbpv1Qwveffyo/0";
			if(headimgurl!=""){
				$("#headimgurl").attr("src",headimgurl);				
			}
			CURR_OPENID=openid;
//			console.log("curr openid:"+getCurrOpenid());			
			//取得默认的待派送列表 
			filterOrder(C_ORDER_PENDING_STATUS);
		}else{
			CURR_OPENID="wrongopenid";
		}
//		alert("当前用户的openid:"+CURR_OPENID);
	})
}


function filterOrder(orderstatus){
    
	clearOrders();
	orderTitle(orderstatus);
	getOrderList(orderstatus);
	highLightClickBtn(orderstatus);
//	generateOrderList();
}

//加亮显示当前点击的按钮
function highLightClickBtn(orderstatus){
	if(orderstatus==2){
		$("#PendingDelivery").css("background-color","");
		$("#delivering").css("background-color","green");
		$("#delivered").css("background-color","");
	}else if(orderstatus==3){
		$("#PendingDelivery").css("background-color","");
		$("#delivering").css("background-color","");
		$("#delivered").css("background-color","green");
	}else{
		$("#PendingDelivery").css("background-color","green");
		$("#delivering").css("background-color","");
		$("#delivered").css("background-color","");
	}
}

//根据状态取当前用户的订单
function getOrderList(orderstatus){
	var openid=getCurrOpenid();
	 startLoadingData();
	 $.ajax({
		 method: "GET",
		 url:httpURL+"rest/order/query/"+openid+"/"+orderstatus,
		 headers: {
		        "Auth": openid
		    },
		 success: function(data,status){
			 console.log("order data:"+data+"status:"+status);
			 orderList=JSON.parse(data.msg); 
			 //订单在页面展现
			 generateOrderList();
			 finishLoadingData();
		 },
		 error: function(){
			 finishLoadingData();
			 MsgInfo("获取订单列表异常,请稍候再试！");
		 }
    });

}

//根据服务端的盐酥鸡列表动态生成订单列表
function generateOrderList(){
	   //Fruit template in HTML 
	 var templatenode=document.getElementById("order_data_template");

	 console.log("Order list:"+orderList.toString());
	 
	 var pendingOrder=0; 
	 //对列表中的每个订单在列表中生成
	 $.each(orderList,function(index, order){
		 console.log("order index:"+index+order);
		 var clonenode=templatenode.cloneNode(true);
		 clonenode.id=order.id;
		 //把订单的id保存在name中以便点击时使用
		 clonenode.name=order.id;
		 if(index==0){
			 templatenode.name=order.id;
		  	orderDisplay(templatenode,order);
		 }else{
			 templatenode.parentNode.appendChild(clonenode);
			 orderDisplay(clonenode,order);
		 }
		 //总的待派送订单数量
		 if(order.statusId==C_ORDER_PENDING_STATUS){
			 pendingOrder=pendingOrder+1;			 
		 }
	 });
	 //在图标中显示未派送订单的数量
	 $("#pendingNum").text(pendingOrder); 
	 
}

//设置每一行记录的数据到页面上
function orderDisplay(templateNode, orderInfo){
	var orderStatus=orderInfo.statusId; 
	$(templateNode).children(".weui-cell__bd").each(function(index,value){
		if(index==0){
			//订单号
			$(this).text(orderInfo.id);
		}else if(index==1){
			//日期,点击不同的按钮显示不同的日期
			if(orderStatus==C_ORDER_PENDING_STATUS){
				$(this).text(orderInfo.insertDate);
			}else if (orderStatus==C_ORDER_DELIVERING_STATUS){
				$(this).text(orderInfo.deliveryDate);
			}else{
				$(this).text(orderInfo.finishDate);
			}
			
		}else if(index==2){
			//订单内容，把水果列表组合一起
			var orderdetail="";
			$.each(orderInfo.orderDetail,function(fruitindex,fruit){
				orderdetail=orderdetail+fruit.fruitName+"["+fruit.orderUnit+"]份,";
			})
			orderdetail=orderdetail.substring(0,orderdetail.length-1);			
			
			$(this).text(orderdetail);
//		}else if(index==3){
//			//派送地址
//			$(this).text(orderInfo.address);
//			
//		}else if(index==4){
//			//联系人
//			$(this).text(orderInfo.contactName);
//		}else if(index==5){
//			//联系电话
//			$(this).text(orderInfo.contactPhone);
//		}else if(index==6){
//			//扩展
		}
//		console.log("node:"+index+$(this).text());
		
	});
}

//对显示日期标题显示内容进行调整，等待派送显示订单日期，派送中显示派送日期，派送完成显示完成日期
function orderTitle(orderStatus){
	if(orderStatus==C_ORDER_PENDING_STATUS){
		$("#orderTitle").children(".weui-cell__bd:first").next().text("订单日期")
	}else if (orderStatus==C_ORDER_DELIVERING_STATUS){
		$("#orderTitle").children(".weui-cell__bd:first").next().text("派送日期")
	}else{
		$("#orderTitle").children(".weui-cell__bd:first").next().text("订单日期")
	}
}

//清除存在的订单信息
function clearOrders(){
	$("#order_data_template").nextAll().each(function(i, value){
//		alert($(this).attr("id"));
		$(this).remove();
		console.log(i+":"+$(this).text()); 
	})
	console.log("template:"+$("#order_data_template").children(".weui-cell__bd:last").text());
	//清除第一个预留空节点的值
	$("#order_data_template").children(".weui-cell__bd").text("");
	 //在图标中显示未派送订单的数量
	 $("#pendingNum").text(0); 
	
}

//test
function showOrderDetailxx(id){
	console.log(id);
}
//显示订单明细信息
function showOrderDetail(id){
	var currOrderid=id;
	console.log("curr order id is:"+currOrderid);
	var currOrderDetail;
	//查找orderlist中为当前点击的订单数据
	$.each(orderList, function(index,data){
		var orderid=data.id;
		if(orderid==currOrderid){
			//获取当前的订单信息
			 currOrderDetail=data;
		}
	});
	console.log("当前订单的详细信息: "+JSON.stringify(currOrderDetail));
	//把当前的订单记录保存在session中，之后的页面直接从session取出数据 
	$.post("OrderDetail", {
		orderid:currOrderid,
		orderlist:JSON.stringify(currOrderDetail)
	},function(data, status){
		//把当前定单的数据
		if(status=="success"){
			naviToOrderDetail();
			console.log("当前订单数据已经成功保存在session");			
		}else{
			console.log("当前订单数据在session保存失败");
		}
	})
}

//点击打开订单的明细列表
function naviToOrderDetail(){
	   //window.navigate("OrderFruit.html");
	   window.location.href="MyOrderDetails.html";
}

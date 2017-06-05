/**
 * 
 */
//当前用户选择的订单明细信息
var currOrderDetail;

$(document).ready(function(){
	 //检查浏览器
	 onlyWechatAccess();	
	//从服务器中取得用户选择的水果列表
	getOrderDetail();
	//取得服务端的openid
	initOpenid();
	 //绑定派送地址的事件
	 $('#cancelOrder').click( function(){
		 //取消确认窗口
		 cancelOrderConfirm();
	 });
	
	
})

//取消订单确认对话框
function cancelOrderConfirm(){
	var $cancelConfirm = $('#cancelConfirm');
    $('#dialogs').on('click', '.weui-dialog__btn_default', function(){
        $(this).parents('.js_dialog').fadeOut(200);
    });
    
    $('#dialogs').on('click', '.weui-dialog__btn_primary', function(){
        $(this).parents('.js_dialog').fadeOut(200);
        //cancel order 
        console.log("Start to post and cancel current order...");
        //取消订单
        cancelOrder();
    });
    

	$cancelConfirm.fadeIn(200);

 }


//取消订单
function cancelOrder(){
	var openid=getCurrOpenid();
	var currOrderid=currOrderDetail.id;
   	//获取系统设置的最大可下订单日期
  	 $.ajax({
  		 method: "GET",
  		 url:httpURL+"rest/order/cancleOrder/"+currOrderid,
  		 headers: {
  		        "Auth": openid
  		    },
  		 success: function(data,status){
  			if(data.code=="SUCCESS"){
  				$("#cancelOrder").attr("disabled",true);	
  				MsgInfo("订单已经成功取消！");
  			}else{
  				MsgInfo("订单取消失败，请稍微再试！");
  			}
  			 console.log("订单取消返回数据:"+JSON.stringify(data));				 
  		 },
  		 error: function(){
  			 MsgInfo("取消订单服务调用异常,请稍候再试！");
  		 }
      });
	
}


//从服务器中取得用户选择的水果列表
function getOrderDetail(){
	$.get("OrderDetail",
	function(data,status){
		if(status=="success"){
			currOrderDetail=JSON.parse(data);
			console.log("用户选择的订单明细:"+JSON.stringify(currOrderDetail));
			//把取得的订单信息显示出来
			generateOrderDetail();
			//取消按钮处理，只有等派送的才可以显示取消订单按钮
			if(currOrderDetail.statusId!="1"){
				$("#cancelOrder").hide();
			}
		}
	});    				
	
}

//把订单的明细信息显示出来
function generateOrderDetail(){
	   //Fruit template in HTML 
	 var templatenode=document.getElementById("orderDetailTemplate");

	 //生成订单明细
	 //订单号
	 orderDisplay(templatenode,"订单号",currOrderDetail.id);
	 
	 //其它属性
	 var orderStatus=currOrderDetail.statusId; 
	 orderItemCloneAndShow(templatenode,"订单状态",currOrderDetail.statusValue);	 
	 orderItemCloneAndShow(templatenode,"订单日期",currOrderDetail.insertDate);
	 if(orderStatus==C_ORDER_DELIVERING_STATUS){
		 orderItemCloneAndShow(templatenode,"派送日期",currOrderDetail.deliveryDate);
	 }
	 else if(orderStatus==C_ORDER_DELIVERED_STATUS){
		 orderItemCloneAndShow(templatenode,"完成日期",currOrderDetail.finishDate);			 
		 }
	//订单内容，把水果列表组合一起
	var orderdetail="";
	$.each(currOrderDetail.orderDetail,function(fruitindex,fruit){
		orderdetail=orderdetail+fruit.fruitName+"["+fruit.orderUnit+"]份,";
	})
	orderdetail=orderdetail.substring(0,orderdetail.length-1);			
	orderItemCloneAndShow(templatenode,"订单明细",orderdetail);
	orderItemCloneAndShow(templatenode,"联系人",currOrderDetail.contactName);
	orderItemCloneAndShow(templatenode,"联系电话",currOrderDetail.contactPhone);
	orderItemCloneAndShow(templatenode,"联系地址",currOrderDetail.address);
	orderItemCloneAndShow(templatenode,"订单备注",currOrderDetail.remark);
	 	 
}

//复制一个订单的子项目并显示出来
function orderItemCloneAndShow(templateNode,orderTitle,orderValue){
	 var clonenode=templateNode.cloneNode(true);
	 clonenode.id=orderTitle;
	 templateNode.parentNode.appendChild(clonenode);
	 orderDisplay(clonenode,orderTitle,orderValue);

}

//设置订单的子项目到页面上
function orderDisplay(templateNode, orderTitle, orderValue){
	$(templateNode).children(".weui-cell__bd").each(function(index,value){
		//标题名称
		if(index==0){
			$(this).text(orderTitle);
		//标题对应的值
		}else if(index==1){
			$(this).text(orderValue);
		}
	});
}
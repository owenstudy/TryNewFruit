//全局变量，一次性得到水果的余额
var fruitBalance=-1; 
var fruitList; 
//系统级别的最大可下订单日
var maxOrderDay;
//当月允许的最大订单数量
var maxOrderNumPerMonth;
//当月目前已经存在的订单数量
var currOrderNumCurrMonth


//对于多个+,-控件设定统一的onclick事件
    	     $(document).ready(function(){
    	    	 //检查浏览器
    	    	 onlyWechatAccess();
   	    		//从session中取当前用户的openid
    	    	 getSessionOpenid();    		
    	         //消息弹出框的提示按钮点击动作，点击后消息
//    	    	 $('#dialogs').on('click', '.weui-dialog__btn', function(){
//    			            $(this).parents('.js_dialog').fadeOut(200);
//    			        });    	    	
    	    	 //绑定派送地址的事件
    	    	 $('#ChooseFruit').click( function(){
    	    		 //把数据传送到session中
    	    		 saveChoseFruit();
    	    	 });
    	    	 //自动在网页中增加统一的页脚
    	  		addFooter();
    	  	   
      });  
   //保存选择的水果信息到session中
   //
    	   //从系统session中取得openid
    	     function getSessionOpenid(){
    	    	 startLoadingData();
    	     	$.post("InitWechatOpenid",function(data,status){
    	     		if(status=="success"){
    	     			var jsonResult=JSON.parse(data);
    	     			var openid=jsonResult.openid;
    	     			CURR_OPENID=openid;
//    	     			console.log("实际微信的openid:"+CURR_OPENID);
    	       	  	   //加载时自动检查当时用户是不是存在
    	       	  	   validateOpenid(getCurrOpenid(),"ChooseFruit");
    	     	    	 //初始化水果列表
    	     	    	 getFruitList(getCurrOpenid());
    	       	  	   //如果用户存在则取出当前用户的余额，同时显示在UI上面
    	       	  	   getFruitBalance(getCurrOpenid());
    	       	  	   //检查不是订单日期在系统允许的最大可下单日期之前，如果是的话则禁止进行订单的下一步骤操作
    	    	    	 //获取系统级别最大的可下订单日期
    	    	    	 getMaxOrderDay();
    	     			//每个月允许的最大订单数量
    	    	    	 checkMaxOrderNumPerMonth();
    	    	    	 
    	    	    	 //加载完成
    	    	    	 finishLoadingData();
//    	     			console.log("curr openid:"+getCurrOpenid());			
    	     		}else{
    	     			CURR_OPENID="wrongopenid";
    	     			finishLoadingData();
    	     		}
//    	     		alert("当前用户的openid:"+CURR_OPENID);
    	     	})
    	     }
    	     
   function saveChoseFruit(){
	   //取得用户输入的数据值 
	       var chosefruitlist=[];
		   $(".weui-btn.weui-btn_mini.weui-btn_default").each(function(){
			   var currFruitAmt=parseInt($(this).val());
			   var fruitid=parseInt($(this).attr("id"));
			   var fruitname=$(this).attr("name");
			   if(currFruitAmt>0){
				   var currFruit={};
				   currFruit.fruitId=fruitid;
				   currFruit.fruitName=fruitname;
				   currFruit.orderUnit=currFruitAmt;
				   
				   console.log(JSON.stringify(currFruit));
				   chosefruitlist.push(currFruit);
			   } 			   
			   console.log("用户选择的水果列表:"+JSON.stringify(chosefruitlist))
		   });	  
		   startLoadingData()   
		var currChooseFruitNum=getTotalChooseAmt();
		if (currChooseFruitNum==0){
			MsgInfo("至少需要选择一种水果!");
			finishLoadingData();
		}else{
			$.post("ChooseFruit",{
				fruitList:JSON.stringify(chosefruitlist)
//				fruitList:chosefruitlist
			},
			function(data,status){
				if(status=="success"){
		    		 //跳转到地址输入界面
		    		 jumpAddressInput();				
					console.log("水果保存到session成功！");
					finishLoadingData();
				}else{
					console.log("水果保存到session让失败！");
					MsgInfo("水果保存时出现错误，请微信再试！");
					finishLoadingData();
				}
				
			});    							
		}
	   
   }   	     
   //取得当前页面中已经选择的数量 ，确保总的数量不超过用户的可用数量
   function getTotalChooseAmt(){
	   var totalAmt=0; 
	   $(".weui-btn.weui-btn_mini.weui-btn_default").each(function(){
		   totalAmt=totalAmt+parseInt($(this).val());
		   console.log($(this).val());
		   
	   });
	   return totalAmt;
   } 	     
   //关联+-按钮的事件处理
   function plusminus(){
  	 $("input").click(function(){
  		 
		 console.log("click button value:"+$(this).next().val()); 
			//当前系统日期
			var today = new Date();//获得当前日期
			var day = today.getDate();//获得当前日期	
        if($(this).val()=="-"){
        	var currValue=parseInt($(this).next().val());
        	var nextValue=currValue-1;
        	//最小值为0
        	if (nextValue<0){nextValue=0};                     	
        	$(this).next().val(nextValue);                    	
        }else if($(this).val()=="+"){
        	currValue=parseInt($(this).prev().val());
        	nextValue=currValue+1;
        	//比较当前的最大可用余额
        	//fruitBalance=getFruitBalance(getCurrOpenid());
        	var currAmt=getTotalChooseAmt();
        	//取得单个订单的最大可订数量
        	var currfruitid=parseInt($(this).prev().attr("id"));
        	var fruitmaxordernum=getMaxFruitOrderNum(currfruitid);
        	//比较当前帐户可用的最大数量，不能超过当前用户的余额也不能超过单个订单的最大订单值
        	if(currAmt+1>fruitBalance ){
        		nextValue=currValue;
//        		alert("超过您的可用水果数量");
        		MsgInfo("超过您的可用水果数量");
        	}
        	else if(currValue+1>fruitmaxordernum){
        		nextValue=currValue;
//        		alert("超过当前水果允许的最大订单数量，最大可订数量为："+fruitmaxordernum);
        		MsgInfo("超过当前水果允许的最大订单数量，最大可订数量为："+fruitmaxordernum);
        	}
        	else if (day>getMaxFruitOrderDay(currfruitid)){
        		nextValue=currValue;
            	MsgInfo("为了有足够的时间预订运输水果，当前水果的订单日期不能超过每月的:"+getMaxFruitOrderDay(currfruitid)+"号,谢谢您的理解！");
        		
        	}
        	
        	$(this).prev().val(nextValue);                    	                    	
        }
		});
	   
   }
   //调用接口来查询当月可用的水果类型
   //此处调用接口
   function getFruitList(openid){
	   //测试使用样例
	   var defaultFruitList=[{fruitid:1, fruitname:"defaultFruit1", fruitmonth:"201704",ordermaxno:8},
	              {fruitid:2, fruitname:"defaultFruit2", fruitmonth:"201704",ordermaxno:8}];	   

		 $.ajax({
			 method: "GET",
			 url:httpURL+"rest/fruit/listFruit/",
			 headers: {
			        "Auth": openid
			    },
			 success: function(data,status){
				 //console.log("Balance return data:"+data+"status:"+status);
					var jsonResult=JSON.parse(data.msg);
				 fruitList=jsonResult;
				 console.log("fruistlist:"+JSON.stringify(fruitList));				 
				 generateFruitList();
	    	       //对每个输入的+-按钮进行点击处理
	    	    	plusminus();				 
			 },
			 error: function(){
				 fruitList=defaultFruitList;
				 MsgInfo("获取水果列表异常,请稍候再试！");
				 //alert("获取balance异常")
 				 showBalance(-1);				 
			 }
	    });
	   
   }
   
   //根据服务端的水果列表动态生成可选择的水果列表
   function generateFruitList(){
	   //Fruit template in HTML 
  	 var templatenode=document.getElementById("fruit_template");

  	 console.log("fruit list:"+fruitList.toString());
  	 //对水果列表中的每个水果在列表中生成
  	 for (fruitindex in fruitList){  		
  		 //水果名称
  		 var fruitName=fruitList[fruitindex].fruitName;
  		var fruitid=fruitList[fruitindex].id;
  		 var clonenode=templatenode.cloneNode(true);
  		 clonenode.id=fruitid;
  		 if(fruitindex==0){
  	  		 $(".weui-label:last").text(fruitName);
  	  		 $(".weui-btn.weui-btn_mini.weui-btn_default:last").attr("id",fruitid);  
  	  		$(".weui-btn.weui-btn_mini.weui-btn_default:last").attr("name",fruitName); 
  	  		 console.log( $(".weui-btn.weui-btn_mini.weui-btn_default:last").attr("id"));   	  		 
  		 }else{
  	  		 templatenode.parentNode.appendChild(clonenode);
  	  		 $(".weui-label:last").text(fruitName);  		
  	  		 //给输入的按钮设置一个id值，这个id值就是fruitid以便在后面取用户输入值是一同取出
  	  		 $(".weui-btn.weui-btn_mini.weui-btn_default:last").attr("id",fruitid);  
   	  		$(".weui-btn.weui-btn_mini.weui-btn_default:last").attr("name",fruitName);   	  		 
  	  		 console.log( $(".weui-btn.weui-btn_mini.weui-btn_default:last").attr("id")); 
  	  		 
  		 }  		   		
  	 }
   }
   
   //得到某一水果的最大可订单数
   function getMaxFruitOrderNum(fruitid){
	   var returnresult=-1; 
	  	 for (fruitindex in fruitList){  	
	   		var currfruitid=fruitList[fruitindex].id;
	   		if(currfruitid==fruitid){
	   			returnresult= parseInt(fruitList[fruitindex].maxOrderNum); 
	   		}
	   		
	  	 }
			return returnresult;
   }
   //得到某一水果的最大可下订单日期，这个是水果级别的限制
   function getMaxFruitOrderDay(fruitid){
	   //去掉了水果级别的最大可下订单日期
//	  	 for (fruitindex in fruitList){  	
//	   		var currfruitid=fruitList[fruitindex].id;
//	   		if(currfruitid==fruitid){
//	   			var maxFruitOrderDateString=fruitList[fruitindex].maxOrderDay
//	   			var maxFruitOrderDate=new   Date(Date.parse(maxFruitOrderDateString.replace(/-/g,   "-")));   
//	   			var maxFruitOrderDay= maxFruitOrderDate.getDate(); 
//	   		}
//	   		
//	  	 }
	  	 
//			return Math.min(maxFruitOrderDay,maxOrderDay);
	   return maxOrderDay;
   }
   
 //获取当月的最大可下订单日期，这个参数是系统级别的限制
   function getMaxOrderDay(){
   	
   	var openid=getCurrOpenid();
   	//获取系统设置的最大可下订单日期
   	 $.ajax({
   		 method: "GET",
   		 url:httpURL+"rest/pub/maxOrderDay",
   		 headers: {
   		        "Auth": openid
   		    },
   		 success: function(data,status){
   			var jsonResult=JSON.parse(data.msg);
   			 console.log("最大可下订单日期返回数据:"+data.msg);				 
   			 maxOrderDay=data.msg;
   		 },
   		 error: function(){
   			 maxOrderDay="28"
   			 MsgInfo("获取当前最大可下订单日错误,请稍候再试！");
   		 }
       });

   }
   //得到当前用户的水果余额
   //此处调用接口
   function getFruitBalance(openid){
	   	 
	     //fruitBalance= 10;
		 //showBalance(fruitBalance);				 
	   
//	   console.info(openid);
	     
		 $.ajax({
			 method: "GET",
			 url:httpURL+"rest/assign/checkBalance/"+openid,
			 headers: {
			        "Auth": openid
			    },
			 success: function(data,status){
				 console.log("Balance return data:"+data+"status:"+status);
				 fruitBalance=data.value;
				 showBalance(data.value);				 
			 },
			 error: function(){
				 fruitBalance=-1;
				 MsgInfo("获取水果余额异常,请稍候再试！");
 				 showBalance(-1);				 
			 }
	    });
	    
//		 var settings = {
//				  "async": true,
//				  "crossDomain": true,
//				  "url": httpURL+"rest/assign/checkBalance/"+openid,
//				  "method": "GET",
//				  "headers": {
//					"Access-Control-Allow-Origin":"*",
//				    "auth": openid,
//				    "cache-control": "no-cache",
//				  }
//				}
//		 $.ajax(settings).done(function (response) {
//			  console.log(response);
//				 fruitBalance=data.value;
//				 showBalance(data.value);
//			}); 		 
	   
   }
   //  
   //在页面上显示当前用户的余额
   function showBalance(fruitBalance){	   
	   $(".weui-media-box__desc").text(fruitBalance);
   }
   
   //点击派送地址按钮后跳转到地址输入界面
   function jumpAddressInput(){
	   //window.navigate("OrderFruit.html");
	   window.location.href="OrderFruit.html";
   }
   
   function getMaxOrderNumPerMonth(data,status){
	   console.log("getMaxOrderNumPerMonth success data:"+JSON.stringify(data));
	   returnMsg=JSON.parse(data.msg);
	   maxOrderNumPerMonth=returnMsg.maxNumConfig;	   
	   currOrderNumCurrmonth=returnMsg.currentNums;
	   if (currOrderNumCurrmonth>=maxOrderNumPerMonth){
		   //禁止点击输入地址按钮
		   $("#ChooseFruit").attr("disabled",true);
		   MsgInfo("为了让更多人能够品尝，目前我们暂时允许一个客户每个月可以下:["+maxOrderNumPerMonth+"]个订单，你已经预定了["+currOrderNumCurrmonth+"]个订单,下个月再来预定吧，谢谢您的理解和支持！");
	   }
	   
	   
   }
   
   function failedcallback(data,status){
	   console.log("failed call back:"+JSON.stringify(data));
   }
   //取得当前用户的在途订单数量，并和最大的可下订单数量进行
   //TODO
   function checkMaxOrderNumPerMonth(){
	   var openid=getCurrOpenid();
//  	 startLoadingData();	   
	   getRestfulCall("rest/order/waitForNums/",openid,getMaxOrderNumPerMonth,failedcallback);
	   
//	   
//		 $.ajax({
//			 method: "GET",
//			 url:httpURL+"rest/order/waitForNums/"+openid,
//			 headers: {
//			        "Auth": openid
//			    },
//			 success: function(data,status){
//				 
//				 console.log("maxOrderNumPerMonth return data:"+JSON.stringify(data)+"status:"+status);
//				 jsondata=JSON.parse(data.msg);
//				 
//				 maxOrderNum=jsondata.maxNumConfig;
//				 currOrderNum=jsondata.currentNums;
//				 //在派送的订单数量超过设置的最大数量时报错
//				 if (data.code!="SUCCESS"){
//					 //禁止点南
//					 $("。weui-btn_primary").attr("disabled",true);
//					 MsgInfo("为了保证大家都能品尝到可口的水果，我们暂时只能提供一个客户每个月预订:"+maxOrderNum+"你已经预定了:"+currOrderNum+"等下个月再来预定吧，谢谢理解！");
//				  	 //加载完成
//				  	 finishLoadingData();
//				 }
//			 },
//			 error: function(){
//				 $("。weui-btn_primary").attr("disabled",true);
//				 MsgInfo("获取水果每月最大的可订数量异常,请稍候再试！");
//			  	 //加载完成
//			  	 finishLoadingData();
//			 }
//	    });
//		 
   }
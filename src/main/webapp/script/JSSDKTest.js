/**
 * 
 */
$(document).ready(function(){
	$.post("WeiXinJSSDK",function(data,status){
		console.log("data:"+data);
		configJson=JSON.parse(data);
		console.log("Started call wx.config......");
		wx.config({  
		    debug: true, //调试阶段建议开启  
		    appId: configJson.appid,//APPID  
		    timestamp: configJson.timestamp,//上面main方法中拿到的时间戳timestamp  
		    nonceStr: configJson.noncestr,//上面main方法中拿到的随机数nonceStr  
		    signature: configJson.sigature,//上面main方法中拿到的签名signature  
		    jsApiList: [  
		           //所有要调用的 API 都要加到这个列表中  
		           'checkJsApi',
		           'onMenuShareTimeline',
		           'onMenuShareAppMessage',
		           'onMenuShareQQ',
		           'onMenuShareWeibo',
		           'hideMenuItems',
		           'showMenuItems',
		           'hideAllNonBaseMenuItem',
		           'showAllNonBaseMenuItem',
		           'translateVoice',
		           'startRecord',
		           'stopRecord',
		           'onRecordEnd',
		           'playVoice',
		           'pauseVoice',
		           'stopVoice',
		           'uploadVoice',
		           'downloadVoice',
		           'chooseImage',
		           'previewImage',
		           'uploadImage',
		           'downloadImage',
		           'getNetworkType',
		           'openLocation',
		           'getLocation',
		           'hideOptionMenu',
		           'showOptionMenu',
		           'closeWindow',
		           'scanQRCode',
		           'chooseWXPay',
		           'openProductSpecificView',
		           'addCard',
		           'chooseCard',
		           'openCard'
		    ]  
			});

	})
}); 


wx.ready(function () {
    // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。

	$("#ScanQrCode").click(function(){
		wx.scanQRCode({
		    needResult: 0, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
		    scanType: ["qrCode","barCode"], // 可以指定扫二维码还是一维码，默认二者都有
		    success: function (res) {
		    var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
		    console.log("scan result:"+result);
		}
		});		
	})
	
	//    _shareAppMessage();
//    _shareTimeline();
});

wx.error(function (res){
    // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
    alert('error');
});
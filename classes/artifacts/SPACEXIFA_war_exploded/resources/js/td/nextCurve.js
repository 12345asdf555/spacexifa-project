/**
 * 
 */
function back() {
	var url = "td/AllTd";
	var img = new Image();
	img.src = url; // 设置相对路径给Image, 此时会发送出请求
	url = img.src; // 此时相对路径已经变成绝对路径
	img.src = null; // 取消请求
	window.location.href = url;
};
var time = new Array();
var chart;
var display = new Array();
var historytime;
var machine = new Array();
var time = new Array();
var ele = new Array();
var vol = new Array();
var gas = new Array();
var machstatus = new Array();
var work = new Array();
var wait = new Array();
var worktime = new Array();
var trackcards = new Array();
var cardid = new Array();
var wpsid = new Array();
var productid = new Array();
var employeeid = new Array();
var stepid = new Array();
var junctionid = new Array();
var welderid = new Array();
var machin_id = new Array();
var dglength;
var websocketURL;
var welderName;
var taskNum;
var symbol = 0;
var symbol1 = 0;
var sym = 0;
var timerele;
var timervol;
var socket;
var redata;
var rowdex = 0;
var maxele = 0;
var minele = 0;
var maxvol = 0;
var minvol = 0;
var presetele=0;
var presetvol=0;
var rows;
var fmch;
var tongdao;
var sint = 0;
var series;
var chart;
var series1;
var series2;
var chart1;
var time1 = 0,time2 = 0;
var typeValue,xmlFlag,xmlTypeValue,xmlFlag1;
var led = [ "0,1,2,4,5,6", "2,5", "0,2,3,4,6", "0,2,3,5,6", "1,2,3,5", "0,1,3,5,6", "0,1,3,4,5,6", "0,2,5", "0,1,2,3,4,5,6", "0,1,2,3,5,6" ];
var lockReconnect = false;//避免重复连接
$(function() {
	imgnum = $("#type").val();
	xmlTypeValue = $("#xmlTypeValue").val();//value值
	xmlFlag1 = $("#xmlFlag").val();//value值
	$("#mrjpg").attr("src", "resources/images/welder_"+0+"3.png");
//	var livewidth = $("#livediv").width() * 0.9;
//	var liveheight = $("#livediv").height() / 2;
//	$("#body31").width(livewidth);
//	$("#body31").height(liveheight);
//	$("#body32").width(livewidth);
//	$("#body32").height(liveheight);
//	$("#body33").width(livewidth);
//	$("#body33").height(liveheight);
	var width = $("#treeDiv").width();
	$(".easyui-layout").layout({
		onCollapse : function() {
			$("#dg").datagrid({
				height : $("#body").height(),
				width : $("#body").width()
			})
		},
		onExpand : function() {
			$("#dg").datagrid({
				height : $("#body").height(),
				width : $("#body").width()
			})
		}
	});
	$("#myTree").tree({
		onClick : function(node) {
			$("#dg").datagrid('load', {
				"parent" : node.id
			})
		}
	})
	$.ajax({
		type : "post",
		async : false,
		url : "td/AllTdbf",
		data : {},
		dataType : "json", //返回数据形式为json  
		success : function(result) {
			if (result) {
				websocketURL = eval(result.web_socket);
			}
		},
		error : function(errorMsg) {
			alert("数据请求失败，请联系系统管理员!");
		}
	});
	$.ajax({
		type : "post",
		async : false,
		url : "td/getLiveWelder",
		data : {},
		dataType : "json", //返回数据形式为json  
		success : function(result) {
			if (result) {
				welderName = eval(result.rows);//焊工编号
			}
		},
		error : function(errorMsg) {
			alert("数据请求失败，请联系系统管理员!");
		}
	});
	//获取工作、焊接时间以及设备类型
	$.ajax({
		type : "post",
		async : false,
		url : "td/getLiveTime?machineid="+$("#machineid").val(),
		data : {},
		dataType : "json", //返回数据形式为json  
		success : function(result) {
			if (result) {
				worktime = eval(result);
//				if(worktime.worktime!=null && worktime.worktime!=''){
//					time1 = worktime.worktime;
//				}
//				if(worktime.time!=null && worktime.time!=''){
//					time2 = worktime.time;
//				}
//				var t1 = secondToDate(time1);
			  //  $("#r3").html(t1);
//			    var t2 = secondToDate(time2);
			   // $("#r4").html(t2);
			}
		},
		error : function(errorMsg) {
			alert("数据请求失败，请联系系统管理员!");
		}
	});
	
//	function serach(){
//		var timebuf = historytime;
//		if(null != timebuf){
//			var date = new Date().getTime();
//			if(date - timebuf > 60000){
//				$("#l5").val("关机");
//				$("#l5").css("background-color", "#818181");
//				$("#mrjpg").attr("src", "resources/images/welder_03.png");
//			}
//		}
//	}
//	setInterval(serach,60000);// 注意函数名没有引号和括弧！
	
	$.ajax({
		type : "post",
		async : true,
		url : "hierarchy/getUserInsframework",
		data : {},
		dataType : "json",
		success : function(result){
//			var str = "<span>"+result.uname+"</span>";
//			$("#username").append(str);
//			for(var r=0;r<result.ary.length;r++){
//				resourceary.push(result.ary[r].resource);
//			}
			anaylsis(result.ipurl);
		},
		error : function(errorMsg){
			alert("数据请求失败，请联系系统管理员!");
		}
	})
	
	websocket();
})
$(function(){
//	function teft(){
		//alert(1);
		//获取电子跟踪卡号，产品图号，
				$.ajax({
					type : "post",
					async : false,
					url : "td/getTrackCard",
					data : {},
					dataType : "json", //返回数据形式为json  
					success : function(result) {
						if (result) {
							trackcards = eval(result.rows);
						}
					},
					error : function(errorMsg) {
						alert("数据请求失败，请联系系统管理员!");
					}
				});
//	}
//	setInterval(teft,30000);// 注意函数名没有引号和括弧！
})
function websocket() {
	if (typeof (WebSocket) == "undefined") {
		WEB_SOCKET_SWF_LOCATION = "resources/js/WebSocketMain.swf";
		WEB_SOCKET_DEBUG = true;
	}
	createWebSocket();
}

function createWebSocket() {
    try {
    	socket = new WebSocket(websocketURL);
    	webclient();
    } catch(e) {
    	console.log('catch');
    	reconnect();
    }
  }

function webclient() {
//	setTimeout(function() {
//		if (socket.readyState != 1) {
//			alert("与服务器连接失败,请检查网络设置!");
//		}
//	}, 10000);
	socket.onopen = function() {
		//监听加载状态改变  
		document.onreadystatechange = completeLoading();

		//加载状态为complete时移除loading效果 
		function completeLoading() {
			var loadingMask = document.getElementById('loadingDiv');
			loadingMask.parentNode.removeChild(loadingMask);
		}
		lockReconnect = false;
	};
	socket.onmessage = function(msg) {
		redata = msg.data;
		iview();
	};
	//关闭事件
	socket.onclose = function(e) {
		if(lockReconnect == true){
			return;
		};
		reconnect();
	};
	//发生了错误事件
	socket.onerror = function(e) {
		if(lockReconnect == true){
			return;
		};
		reconnect();
	}
}

function reconnect(){
	if(lockReconnect == true){
		return;
	};
	lockReconnect = true;
    var tt = window.setInterval(function () {
    	if(lockReconnect == false){
    		window.clearInterval(tt);
    	}
    	try {
    		createWebSocket();
		} catch (e) {
			console.log(e.message);
		}
    }, 10000);
}

function iview() {
	var z = 0;
	time.length = 0;
	vol.length = 0;
	ele.length = 0;
	if(redata.length%135==0){
		for (var i = 0; i < redata.length; i += 135) {
//			console.log($("#machineid").val());
			if(parseInt(redata.substring(4+i, 8+i), 10)==$("#machineid").val()){
				historytime = new Date().getTime();
				time1++;
			    var t1 = secondToDate(time1);
			    //$("#r3").html(t1);//工作时长
			    if(redata.substring(36 + i, 38 + i)!="00"){
				    time2++;
				    var t2 = secondToDate(time2);
				   // $("#r4").html(t2);//焊接时长
			    }
				if(display.length){
					var ttme = redata.substring(54 + i, 73 + i);
					//						time.push(Date.parse(redata.substring(20+i, 39+i)));
					ttme = ttme.replace(/-/g, '/');
					time.push(Date.parse(new Date(ttme)));
					console.log(parseInt(redata.substring(99+i, 103+i)));
//					time.push((new Date(ttme)).getTime());
					if(typeValue == xmlTypeValue && xmlFlag == xmlFlag1 && xmlFlag1=="1"){
						for (var d = 0; d < display.length; d++) {
							var firstPosition = display[d].getElementsByTagName("FirstPosition");//起始位
							var lastPosition = display[d].getElementsByTagName("LastPosition");//结束位
							var unitValue = display[d].getElementsByTagName("UnitValue");//单位
							if (document.all) {
								firstPosition = firstPosition[0].text;
								lastPosition = lastPosition[0].text;
								unitValue = unitValue[0].text;
							} else {
								firstPosition = firstPosition[0].textContent;
								lastPosition = lastPosition[0].textContent;
								unitValue = unitValue[0].textContent;
							}
//						eval("series"+d).addPoint([ Date.parse(new Date(ttme)), parseInt(redata.substring(parseInt(firstPosition) + i, parseInt(lastPosition) + i), 10) ], true, true);
							var dataValue = parseInt(redata.substring(parseInt(firstPosition) + i, parseInt(lastPosition) + i), 10);
							dataValue = dataValue * parseFloat(unitValue);
							eval("series"+d).addPoint([ Date.parse(new Date(ttme)), dataValue ], true, true);
							//activeLastPointToolip(eval("chart"+d));
						}
					}
					if(typeValue == xmlTypeValue && xmlFlag == xmlFlag1 && xmlFlag1=="2"){
						for (var d = 0; d < display.length; d++) {
							var firstPosition = display[d].getElementsByTagName("FirstPosition");//起始位
							var lastPosition = display[d].getElementsByTagName("LastPosition");//结束位
							var unitValue = display[d].getElementsByTagName("UnitValue");//单位
							var divId = display[d].getElementsByTagName("DivId");//div的id
							if (document.all) {
								firstPosition = firstPosition[0].text;
								lastPosition = lastPosition[0].text;
								unitValue = unitValue[0].text;
								divId = divId[0].text;
							} else {
								firstPosition = firstPosition[0].textContent;
								lastPosition = lastPosition[0].textContent;
								unitValue = unitValue[0].textContent;
								divId = divId[0].textContent;
							}
	//					eval("series"+d).addPoint([ Date.parse(new Date(ttme)), parseInt(redata.substring(parseInt(firstPosition) + i, parseInt(lastPosition) + i), 10) ], true, true);
							var dataValue = parseInt(redata.substring(parseInt(firstPosition) + i, parseInt(lastPosition) + i), 10);
							dataValue = dataValue * parseFloat(unitValue);
							if($("#"+divId+redata.substring(8+i, 12+i)).length>0){
								eval("series"+d+redata.substring(8+i, 12+i)).addPoint([ Date.parse(new Date(ttme)), dataValue ], true, true);
							}
						}
					}
				}
				for (var m = 0; m < trackcards.length; m++) {
					if (trackcards[m].insid == parseInt(redata.substring(99 + i, 103 + i),10)) {
						$("#r1").val(trackcards[m].fproduct_drawing_no);
						$("#r2").val(trackcards[m].fprocessname);
						$("#r5").val(trackcards[m].fwelded_junction_no);
						$("#r6").val(trackcards[m].fwps_lib_version);
						$("#r7").val(trackcards[m].femployee_name);
						$("#r9").val(trackcards[m].fproduct_number);
						$("#r10").val(trackcards[m].fjunction);
						$("#r11").val(trackcards[m].fstep_name);
						break;
					}
				}
				$("#r3").html("--");
				for (var k = 0; k < welderName.length; k++) {
					if (welderName[k].fid == parseInt(redata.substring(0 + i, 4 + i),10)) {
						$("#r3").val(welderName[k].fname);
					}
				}
				$("#l1").val(worktime.machine);
				$("#l2").val(worktime.machineno);
				$("#l3").val(worktime.mvaluename);
				var imgnum = $("#type").val();
				if (time.length != 0 && z < time.length) {
					var mstatus = redata.substring(36 + i, 38 + i);
					switch (mstatus) {
					case "00":
						$("#l5").val("待机");
						$("#l5").css("background-color", "#55a7f3");
						$("#mrjpg").attr("src", "resources/images/welder_"+0+"1.png");
						break;
					case "03":
						$("#l5").val("焊接");
						$("#l5").css("background-color", "#7cbc16");
						$("#mrjpg").attr("src", "resources/images/welder_"+0+"0.png");
						break;
					case "05":
						$("#l5").val("收弧");
						$("#l5").css("background-color", "#7cbc16");
						$("#mrjpg").attr("src", "resources/images/welder_"+0+"0.png");
						break;
					case "07":
						$("#l5").val("启弧");
						$("#l5").css("background-color", "#7cbc16");
						$("#mrjpg").attr("src", "resources/images/welder_"+0+"0.png");
						break;
					default:
						$("#l5").val("报警");
						$("#l5").css("background-color", "#a41c26");
						$("#mrjpg").attr("src", "resources/images/welder_"+0+"2.png");
						break;
					}
				}
			}
		}
	}
}
function activeLastPointToolip(chart) {
	var points = chart.series[0].points;
	chart.yAxis[0].removePlotLine('plot-line-8');
	chart.yAxis[0].removePlotLine('plot-line-9');
		chart.yAxis[0].addPlotLine({ //在y轴上增加 
		value : warnvol_up, //在值为2的地方 
		width : 2, //标示线的宽度为2px 
		color : 'black', //标示线的颜色 
		dashStyle : 'longdashdot',
		id : 'plot-line-8', //标示线的id，在删除该标示线的时候需要该id标示 });
		label : {
			text : '报警电压上限', //标签的内容
			align : 'center', //标签的水平位置，水平居左,默认是水平居中center
			x : 10
		}
	})
	chart.yAxis[0].addPlotLine({ //在y轴上增加 
		value : warnvol_down, //在值为2的地方 
		width : 2, //标示线的宽度为2px 
		color : 'black', //标示线的颜色 
		dashStyle : 'longdashdot',
		id : 'plot-line-9', //标示线的id，在删除该标示线的时候需要该id标示 });
		label : {
			text : '报警电压下限', //标签的内容
			align : 'center', //标签的水平位置，水平居左,默认是水平居中center
			x : 10 //标签相对于被定位的位置水平偏移的像素，重新定位，水平居左10px
		}
	})
}
//监听窗口大小变化
window.onresize = function() {
//	setTimeout(domresize, 500);
}

function secondToDate(result) {
	var h = Math.floor(result / 3600) < 10 ? '0' + Math.floor(result / 3600) : Math.floor(result / 3600);
	var m = Math.floor((result / 60 % 60)) < 10 ? '0' + Math.floor((result / 60 % 60)) : Math.floor((result / 60 % 60));
	var s = Math.floor((result % 60)) < 10 ? '0' + Math.floor((result % 60)) : Math.floor((result % 60));
	return result = h + ":" + m + ":" + s;
}

function anaylsis(ipurl){
	//处理ie不支持indexOf
	if (!Array.prototype.indexOf){
  		Array.prototype.indexOf = function(elt /*, from*/){
	    var len = this.length >>> 0;
	    var from = Number(arguments[1]) || 0;
	    from = (from < 0)
	         ? Math.ceil(from)
	         : Math.floor(from);
	    if (from < 0)
	      from += len;
	    for (; from < len; from++)
	    {
	      if (from in this &&
	          this[from] === elt)
	        return from;
	    }
	    return -1;
	  };
	}
	var object = loadxmlDoc(ipurl+"ConfigFile/machine.xml");
	var menuinfo = object.getElementsByTagName("Typeinfo");
	for (var i = 0; i < menuinfo.length; i++) {
		var name = menuinfo[i].getElementsByTagName("TypeName");//型号
		typeValue = menuinfo[i].getElementsByTagName("TypeValue");//value值
		xmlFlag = menuinfo[i].getElementsByTagName("symbol");//value值
		if (document.all) {
			name = name[0].text;
			typeValue = typeValue[0].text;
			xmlFlag = xmlFlag[0].text;
		} else {
			var userAgent = navigator.userAgent;
			if(userAgent.indexOf('Trident')!=-1){
				name = name[0].text;
				typeValue = typeValue[0].text;
				xmlFlag = xmlFlag[0].text;
				
			}else{
				name = name[0].textContent;
				typeValue = typeValue[0].textContent;
				xmlFlag = xmlFlag[0].textContent;
			}
		}
		if(typeValue == xmlTypeValue && xmlFlag == xmlFlag1 && xmlFlag1=="1"){
			display = menuinfo[i].getElementsByTagName("Display");//显示内容
			for (var d = 0; d < display.length; d++) {
				var curveName = display[d].getElementsByTagName("CurveName");//曲线名称
				var divId = display[d].getElementsByTagName("DivId");//div的id
				var Unit = display[d].getElementsByTagName("Unit");//曲线单位
				var Color = display[d].getElementsByTagName("Color");//曲线颜色
				var MaxValue = display[d].getElementsByTagName("MaxValue");//曲线最大值
				var MinValue = display[d].getElementsByTagName("MinValue");//曲线最小值
				if (document.all) {
					curveName = curveName[0].text;
					divId = divId[0].text;
					Unit = Unit[0].text;
					Color = Color[0].text;
					MaxValue = MaxValue[0].text;
					MinValue = MinValue[0].text;
				} else {
					var userAgent = navigator.userAgent;
					if(userAgent.indexOf('Trident')!=-1){
						curveName = curveName[0].text;
						divId = divId[0].text;
						Unit = Unit[0].text;
						Color = Color[0].text;
						MaxValue = MaxValue[0].text;
						MinValue = MinValue[0].text;
						
					}else{
						curveName = curveName[0].textContent;
						divId = divId[0].textContent;
						Unit = Unit[0].textContent;
						Color = Color[0].textContent;
						MaxValue = MaxValue[0].textContent;
						MinValue = MinValue[0].textContent;
					}
				}
				//eval("var "+divId.toString()+"Arr=new Array()");//动态生成的存放数据的数组
				//eval(divId.toString()+"Arr").push("xxx");//数组赋值
//				alert(eval(divId.toString()+"Arr[0]"))//数组调用
				var str = '<div style="float:left;width:33%;height:50%;">'+
		       		'<div style="float:left; padding-top:1%;width:5%;height:95%;background-color: #37d512;border-radius: 6px;font-size:16pt;color:#ffffff;margin:10px;text-align: center;">'+
		       		curveName+'<div style="width:95%;height:12%;border-radius: 50%;font-size:14pt;background-color: #ffffff;color: #000;">'+Unit+'</div></div>'+
					'<div id="'+divId+'" style="float:left;width:90%;height:95%;"></div></div>';
				$("#livediv").append(str);
//				eval("series"+d+"=[]");
				eval("chart"+d+"={}");
				var s = "series"+d;
	            window[s] = [];
				var cur = {div:divId,name:curveName,color:Color,max:MaxValue,min:MinValue,se:"series"+d,ch:"chart"+d}
				curve(cur);
			}
			break;
		}
		if(typeValue == xmlTypeValue && xmlFlag == xmlFlag1 && xmlFlag1=="2"){
			var GatherNo =  menuinfo[i].getElementsByTagName("GatherNo");
			if (document.all) {
				GatherNo = GatherNo[0].text;
			} else {
				var userAgent = navigator.userAgent;
				if(userAgent.indexOf('Trident')!=-1){
					GatherNo = GatherNo[0].text;
					
				}else{
					GatherNo = GatherNo[0].textContent;
				}
			}
			var smallArr = GatherNo.split("_");
			var normalArr = smallArr[0].split(",");
			for(var n=0;n<normalArr.length;n++){
				display = menuinfo[i].getElementsByTagName("Display");//显示内容
				for (var d = 0; d < display.length; d++) {
					var curveName = display[d].getElementsByTagName("CurveName");//曲线名称
					var divId = display[d].getElementsByTagName("DivId");//div的id
					var Unit = display[d].getElementsByTagName("Unit");//曲线单位
					var Color = display[d].getElementsByTagName("Color");//曲线颜色
					var MaxValue = display[d].getElementsByTagName("MaxValue");//曲线最大值
					var MinValue = display[d].getElementsByTagName("MinValue");//曲线最小值
					var flag = display[d].getElementsByTagName("flag");
					if (document.all) {
						curveName = curveName[0].text;
						divId = divId[0].text;
						Unit = Unit[0].text;
						Color = Color[0].text;
						MaxValue = MaxValue[0].text;
						MinValue = MinValue[0].text;
						flag = flag[0].text;
					} else {
						var userAgent = navigator.userAgent;
						if(userAgent.indexOf('Trident')!=-1){
							curveName = curveName[0].text;
							divId = divId[0].text;
							Unit = Unit[0].text;
							Color = Color[0].text;
							MaxValue = MaxValue[0].text;
							MinValue = MinValue[0].text;
							flag = flag[0].text;
							
						}else{
							curveName = curveName[0].textContent;
							divId = divId[0].textContent;
							Unit = Unit[0].textContent;
							Color = Color[0].textContent;
							MaxValue = MaxValue[0].textContent;
							MinValue = MinValue[0].textContent;
							flag = flag[0].textContent;
						}
					}
					//eval("var "+divId.toString()+"Arr=new Array()");//动态生成的存放数据的数组
					//eval(divId.toString()+"Arr").push("xxx");//数组赋值
	//				alert(eval(divId.toString()+"Arr[0]"))//数组调用
					if(flag=="0"){
						var str = '<div style="float:left;width:33%;height:50%;">'+
						'<div style="float:left; padding-top:1%;width:5%;height:95%;background-color: #37d512;border-radius: 6px;font-size:16pt;color:#ffffff;margin:10px;text-align: center;">'+
						curveName+'<div style="width:95%;height:12%;border-radius: 50%;font-size:14pt;background-color: #ffffff;color: #000;">'+Unit+'</div></div>'+
						'<div id="'+divId+normalArr[n]+'" style="float:left;width:90%;height:95%;"></div></div>';
						$("#livediv").append(str);
						//				eval("series"+d+"=[]");
						eval("chart"+d+"={}");
						var s = "series"+d+normalArr[n];
						window[s] = [];
						var cur = {div:divId+normalArr[n],name:curveName,color:Color,max:MaxValue,min:MinValue,se:"series"+d+normalArr[n],ch:"chart"+d}
						curve(cur);
					}
					if(smallArr[1]!="" && flag=="1"){
						var str = '<div style="float:left;width:33%;height:50%;">'+
						'<div style="float:left; padding-top:1%;width:5%;height:95%;background-color: #37d512;border-radius: 6px;font-size:16pt;color:#ffffff;margin:10px;text-align: center;">'+
						curveName+'<div style="width:95%;height:12%;border-radius: 50%;font-size:14pt;background-color: #ffffff;color: #000;">'+Unit+'</div></div>'+
						'<div id="'+divId+smallArr[1]+'" style="float:left;width:90%;height:95%;"></div></div>';
						$("#livediv").append(str);
	//				eval("series"+d+"=[]");
						eval("chart"+d+"={}");
						var s = "series"+d+smallArr[1];
						window[s] = [];
						var cur = {div:divId+smallArr[1],name:curveName,color:Color,max:MaxValue,min:MinValue,se:"series"+d+smallArr[1],ch:"chart"+d}
						curve(cur);
						smallArr[1]=""
					}
				}
			}
			break;
		}
	}
}

function loadxmlDoc(file) {
	try {
		//IE
		xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
	} catch (e) {
		//Firefox, Mozilla, Opera, etc
		xmlDoc = document.implementation.createDocument("", "", null);
	}

	try {
		xmlDoc.async = false;
		xmlDoc.load(file); //chrome没有load方法
	} catch (e) {
		//针对Chrome,不过只能通过http访问,通过file协议访问会报错
		var xmlhttp = new window.XMLHttpRequest();
		xmlhttp.open("GET", file, false);
		xmlhttp.send(null);
		xmlDoc = xmlhttp.responseXML.documentElement;
	}
	return xmlDoc;
}

function curve(value) {
	Highcharts.setOptions({
		global : {
			useUTC : false
		}
	});

	$('#'+value.div+'').highcharts({
		chart : {
			type : 'spline',
			animation : false, // don't animate in old IE
			marginRight : 70,
			events : {
				load : function() {
					// set up the updating of the chart each second
					window[value.se] = this.series[0],
					window[value.ch] = this;
				}
			}
		},
		title : {
			text : false
		},
		xAxis : {
			type : 'datetime',
			tickPixelInterval : 150 /*,
	  		        tickWidth:0,
		  		    labels:{
		  		    	enabled:false
		  		    }*/
		},
		yAxis : [ {
			max : parseFloat(value.max), // 定义Y轴 最大值  
			min : parseFloat(value.min), // 定义最小值  
			minPadding : 0.2,
			maxPadding : 0.2,
			tickInterval : (parseFloat(value.max)-parseFloat(value.min))/5,
			color : value.color,
			title : {
				text : '',
				style : {
					color : value.color
				}
			}
		} ],
		tooltip : {
			formatter : function() {
				return '<b>' + this.series.name + '</b><br/>' +
					Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
					Highcharts.numberFormat(this.y, 2);
			}
		},
		legend : {
			enabled : false
		},
		exporting : {
			enabled : false
		},
		credits : {
			enabled : false // 禁用版权信息
		},
		series : [ {
			name : value.name,
			data : (function() {
				// generate an array of random data
				var data = [],
					i;
				for (i = -9; i <= 0; i += 1) {
					data.push({
						x : time[0] - 1000 + i * 1000,
						y : 0
					});
				}
				return data;
			}())
		} ]
	}, function(c) {
//		activeLastPointToolip(c)
	});

//	activeLastPointToolip(chart);

}
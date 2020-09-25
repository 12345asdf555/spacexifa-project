var insfid;
var lockReconnect = false;//避免重复连接
var websocketURL, symbol=0, welderName, taskNum, socket, tt;
var showflag = 0,offFlag=0,timeflag;
var liveary = new Array(), machine = new Array();
var off = new Array(), on = new Array(), warn = new Array(), stand = new Array(), cleardata = new Array();
$(function(){
	loadtree();
	websocketUrl();
	websocket();
	//状态发生改变
	$("#status").combobox({
		onChange : function(newValue, oldValue){
			statusClick(newValue);
		}
	});
})
function loadtree() {
	$("#myTree").tree({
		url : 'insframework/getConmpany', //请求路径
		onLoadSuccess : function(node, data) {
			var tree = $(this);
			if (data) {
				$(data).each(function(index, d) {
					if (this.state == 'closed') {
						tree.tree('expandAll');
					}
					$('#_easyui_tree_1 .tree-icon').css("background", "url(resources/images/menu_1.png) no-repeat center center");
					var nownodes = $('#myTree').tree('find', data[0].id);
					//判断是否拥有子节点,改变子节点图标
					if (nownodes.children != null) {
						for(var i=0;i<nownodes.children.length;i++){
							var nextnodes1 = nownodes.children[i];
							$('#' + nextnodes1.domId + ' .tree-icon').css("background", "url(resources/images/menu_2.png) no-repeat center center");
							if(nextnodes1.children != null){
								for(var j=0;j<nextnodes1.children.length;j++){
									var nextnodes2 = nextnodes1.children[j];
									$('#' + nextnodes2.domId + ' .tree-icon').css("background", "url(resources/images/menu_3.png) no-repeat center center");
									if(nextnodes2.children != null){
										for(var x=0;x<nextnodes2.children.length;x++){
											var nextnodes3 = nextnodes2.children[x];
											$('#' + nextnodes3.domId + ' .tree-icon').css("background", "url(resources/images/menu_3.png) no-repeat center center");
										}
									}
								}
							}
							
						}
					}
				});
			}
			if (data.length > 0) {
				//找到第一个元素
				var nownodes = $('#myTree').tree('find', data[0].id);
				insfid = nownodes.id;
				//默认选中第一个项目部
				$('#myTree').tree('select', nownodes.target);
				getMachine(insfid);
			}

		},
		//树形菜单点击事件,获取项目部id，默认选择当前组织机构下的第一个
		onClick : function(node) {
			showflag = 0;
/*			document.getElementById("load").style.display="block";
			var sh = '<div id="show" style="align="center""><img src="resources/images/load.gif"/>正在加载，请稍等...</div>';
			$("#bodydiv").append(sh);
			document.getElementById("show").style.display="block";*/
			var nownodes = $('#myTree').tree('find', node.id);
			insfid = nownodes.id;
			$("#bodydiv").html("");
			getMachine(insfid);
		}
	});
}

function websocketUrl() {
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
}

//获取焊机，任务及焊工信息
function getMachine(insfid) {
	var url,welderurl;
	if (insfid == "" || insfid == null) {
		url = "td/getLiveMachine";
		welderurl = "td/getLiveWelder";
	} else {
		url = "td/getLiveMachine?parent=" + insfid;
		welderurl = "td/getLiveWelder?parent=" + insfid;
	}
	$.ajax({
		type : "post",
		async : false,
		url : url,
		data : {},
		dataType : "json", //返回数据形式为json  
		success : function(result) {
			if (result) {
				machine = eval(result.rows);
				for(var i=0;i<machine.length;i++){
					var type = machine[i].type,imgnum=0;
//					if(type==41){
//						imgnum = 1;
//					}else if(type==42){
						imgnum = 3;
//					}else if(type==43){
//						imgnum = 2;
//					}
//					if(machine[i].fequipment_no=="6666"){
//						imgnum = 4;
//					}
					if(offFlag==0){
						off.push(machine[i].fid);
					}
					var gatherArr = new Array();
					if(machine[i].fgather_no!=null && machine[i].fgather_no!=""){
						gatherArr = machine[i].fgather_no.split(",");
					}else{
						continue;
					}
					if($.inArray("0008", gatherArr)!=(-1)){
						var str = '<div id="machine'+machine[i].fid+'" style="position:ABSOLUTE;width:75px;height:95px;left:40px;top:41px;">'+
						'<a href="td/goNextcurve?value='+machine[i].fid+'&valuename='+machine[i].fequipment_no+'&type='+machine[i].type+'&model='+machine[i].model+'&typeValue=1&flag=1"><img id="img'+machine[i].fid+'" src="resources/images/welder_0'+imgnum+'.png" style="height:100%;width:100%;"></a></div>';
						$("#bodydiv").append(str);
						$("#machine"+machine[i].fid).show();
					}
					else if($.inArray("0019", gatherArr)!=(-1)){
						var str = '<div id="machine'+machine[i].fid+'" style="position:ABSOLUTE;width:75px;height:95px;left:200px;top:41px;"><a href="td/goNextcurve?value='+machine[i].fid+'&valuename='+machine[i].fequipment_no+'&type='+machine[i].type+'&model='+machine[i].model+'&typeValue=2&flag=1"><img id="img'+machine[i].fid+'" src="resources/images/welder_0'+imgnum+'.png" style="height:100%;width:100%;"></a></div>';
						$("#bodydiv").append(str);
						$("#machine"+machine[i].fid).show();
					}
					else if($.inArray("0023", gatherArr)!=(-1)){
						var str = '<div id="machine'+machine[i].fid+'" style="position:ABSOLUTE;width:75px;height:95px;left:550px;top:60px;"><a href="td/goNextcurve?value='+machine[i].fid+'&valuename='+machine[i].fequipment_no+'&type='+machine[i].type+'&model='+machine[i].model+'&typeValue=1&flag=1"><img id="img'+machine[i].fid+'" src="resources/images/welder_0'+imgnum+'.png" style="height:100%;width:100%;"></a></div>';
						$("#bodydiv").append(str);
						$("#machine"+machine[i].fid).show();
					}
					else if($.inArray("0017", gatherArr)!=(-1)){
						var str = '<div id="machine'+machine[i].fid+'" style="position:ABSOLUTE;width:75px;height:95px;left:630px;top:60px;"><a href="td/goNextcurve?value='+machine[i].fid+'&valuename='+machine[i].fequipment_no+'&type='+machine[i].type+'&model='+machine[i].model+'&typeValue=1&flag=1"><img id="img'+machine[i].fid+'" src="resources/images/welder_0'+imgnum+'.png" style="height:100%;width:100%;"></a></div>';
						$("#bodydiv").append(str);
						$("#machine"+machine[i].fid).show();
					}
					else if($.inArray("0009", gatherArr)!=(-1)){
						var str = '<div id="machine'+machine[i].fid+'" style="position:ABSOLUTE;width:75px;height:95px;left:710px;top:60px;"><a href="td/goNextcurve?value='+machine[i].fid+'&valuename='+machine[i].fequipment_no+'&type='+machine[i].type+'&model='+machine[i].model+'&typeValue=1&flag=1"><img id="img'+machine[i].fid+'" src="resources/images/welder_0'+imgnum+'.png" style="height:100%;width:100%;"></a></div>';
						$("#bodydiv").append(str);
						$("#machine"+machine[i].fid).show();
					}
					else if($.inArray("0014", gatherArr)!=(-1)){
						var str = '<div id="machine'+machine[i].fid+'" style="position:ABSOLUTE;width:75px;height:95px;left:790px;top:60px;"><a href="td/goNextcurve?value='+machine[i].fid+'&valuename='+machine[i].fequipment_no+'&type='+machine[i].type+'&model='+machine[i].model+'&typeValue=1&flag=1"><img id="img'+machine[i].fid+'" src="resources/images/welder_0'+imgnum+'.png" style="height:100%;width:100%;"></a></div>';
						$("#bodydiv").append(str);
						$("#machine"+machine[i].fid).show();
					}
					else if($.inArray("0002", gatherArr)!=(-1)){
						var str = '<div id="machine'+machine[i].fid+'" style="position:ABSOLUTE;width:75px;height:95px;left:870px;top:60px;"><a href="td/goNextcurve?value='+machine[i].fid+'&valuename='+machine[i].fequipment_no+'&type='+machine[i].type+'&model='+machine[i].model+'&typeValue=1&flag=1"><img id="img'+machine[i].fid+'" src="resources/images/welder_0'+imgnum+'.png" style="height:100%;width:100%;"></a></div>';
						$("#bodydiv").append(str);
						$("#machine"+machine[i].fid).show();
					}
					else if($.inArray("0015", gatherArr)!=(-1)){
						var str = '<div id="machine'+machine[i].fid+'" style="position:ABSOLUTE;width:75px;height:95px;left:950px;top:60px;"><a href="td/goNextcurve?value='+machine[i].fid+'&valuename='+machine[i].fequipment_no+'&type='+machine[i].type+'&model='+machine[i].model+'&typeValue=1&flag=1"><img id="img'+machine[i].fid+'" src="resources/images/welder_0'+imgnum+'.png" style="height:100%;width:100%;"></a></div>';
						$("#bodydiv").append(str);
						$("#machine"+machine[i].fid).show();
					}
					else if($.inArray("0022", gatherArr)!=(-1)){
						var str = '<div id="machine'+machine[i].fid+'" style="position:ABSOLUTE;width:75px;height:95px;left:1030px;top:60px;"><a href="td/goNextcurve?value='+machine[i].fid+'&valuename='+machine[i].fequipment_no+'&type='+machine[i].type+'&model='+machine[i].model+'&typeValue=1&flag=1"><img id="img'+machine[i].fid+'" src="resources/images/welder_0'+imgnum+'.png" style="height:100%;width:100%;"></a></div>';
						$("#bodydiv").append(str);
						$("#machine"+machine[i].fid).show();
					}
					else if($.inArray("0001", gatherArr)!=(-1)){
						var str = '<div id="machine'+machine[i].fid+'" style="position:ABSOLUTE;width:75px;height:95px;left:1110px;top:60px;"><a href="td/goNextcurve?value='+machine[i].fid+'&valuename='+machine[i].fequipment_no+'&type='+machine[i].type+'&model='+machine[i].model+'&typeValue=1&flag=1"><img id="img'+machine[i].fid+'" src="resources/images/welder_0'+imgnum+'.png" style="height:100%;width:100%;"></a></div>';
						$("#bodydiv").append(str);
						$("#machine"+machine[i].fid).show();
					}
					else if($.inArray("0003", gatherArr)!=(-1)){
						var str = '<div id="machine'+machine[i].fid+'" style="position:ABSOLUTE;width:75px;height:95px;left:1190px;top:60px;"><a href="td/goNextcurve?value='+machine[i].fid+'&valuename='+machine[i].fequipment_no+'&type='+machine[i].type+'&model='+machine[i].model+'&typeValue=1&flag=1"><img id="img'+machine[i].fid+'" src="resources/images/welder_0'+imgnum+'.png" style="height:100%;width:100%;"></a></div>';
						$("#bodydiv").append(str);
						$("#machine"+machine[i].fid).show();
					}
					else if($.inArray("0018", gatherArr)!=(-1)){
						var str = '<div id="machine'+machine[i].fid+'" style="position:ABSOLUTE;width:75px;height:95px;left:45px;top:230px;"><a href="td/goNextcurve?value='+machine[i].fid+'&valuename='+machine[i].fequipment_no+'&type='+machine[i].type+'&model='+machine[i].model+'&typeValue=1&flag=1"><img id="img'+machine[i].fid+'" src="resources/images/welder_0'+imgnum+'.png" style="height:100%;width:100%;"></a></div>';
						$("#bodydiv").append(str);
						$("#machine"+machine[i].fid).show();
					}
					else if($.inArray("0020", gatherArr)!=(-1)||$.inArray("0024", gatherArr)!=(-1)){
						if($("#machine"+machine[i].fid).length<=0){
							var str = '<div id="machine'+machine[i].fid+'" style="position:ABSOLUTE;width:75px;height:95px;left:200px;top:230px;"><a href="td/goNextcurve?value='+machine[i].fid+'&valuename='+machine[i].fequipment_no+'&type='+machine[i].type+'&model='+machine[i].model+'&typeValue=2&flag=2"><img id="img'+machine[i].fid+'" src="resources/images/welder_0'+imgnum+'.png" style="height:100%;width:100%;"></a></div>';
							$("#bodydiv").append(str);
							$("#machine"+machine[i].fid).show();
						}
					}
					else if($.inArray("0007", gatherArr)!=(-1)){
						var str = '<div id="machine'+machine[i].fid+'" style="position:ABSOLUTE;width:75px;height:95px;left:630px;top:230px;"><a href="td/goNextcurve?value='+machine[i].fid+'&valuename='+machine[i].fequipment_no+'&type='+machine[i].type+'&model='+machine[i].model+'&typeValue=1&flag=1"><img id="img'+machine[i].fid+'" src="resources/images/welder_0'+imgnum+'.png" style="height:100%;width:100%;"></a></div>';
						$("#bodydiv").append(str);
						$("#machine"+machine[i].fid).show();
					}
					else if($.inArray("0013", gatherArr)!=(-1)){
						var str = '<div id="machine'+machine[i].fid+'" style="position:ABSOLUTE;width:75px;height:95px;left:710px;top:250px;"><a href="td/goNextcurve?value='+machine[i].fid+'&valuename='+machine[i].fequipment_no+'&type='+machine[i].type+'&model='+machine[i].model+'&typeValue=1&flag=1"><img id="img'+machine[i].fid+'" src="resources/images/welder_0'+imgnum+'.png" style="height:100%;width:100%;"></a></div>';
						$("#bodydiv").append(str);
						$("#machine"+machine[i].fid).show();
					}
					else if($.inArray("0006", gatherArr)!=(-1)||$.inArray("0011", gatherArr)!=(-1)){
						if($("#machine"+machine[i].fid).length<=0){
							var str = '<div id="machine'+machine[i].fid+'" style="position:ABSOLUTE;width:75px;height:95px;left:990px;top:230px;"><a href="td/goNextcurve?value='+machine[i].fid+'&valuename='+machine[i].fequipment_no+'&type='+machine[i].type+'&model='+machine[i].model+'&typeValue=1&flag=2"><img id="img'+machine[i].fid+'" src="resources/images/welder_0'+imgnum+'.png" style="height:100%;width:100%;"></a></div>';
							$("#bodydiv").append(str);
							$("#machine"+machine[i].fid).show();
						}
					}
					else if($.inArray("0005", gatherArr)!=(-1)){
						var str = '<div id="machine'+machine[i].fid+'" style="position:ABSOLUTE;width:75px;height:95px;left:1250px;top:280px;"><a href="td/goNextcurve?value='+machine[i].fid+'&valuename='+machine[i].fequipment_no+'&type='+machine[i].type+'&model='+machine[i].model+'&typeValue=2&flag=1"><img id="img'+machine[i].fid+'" src="resources/images/welder_0'+imgnum+'.png" style="height:100%;width:100%;"></a></div>';
						$("#bodydiv").append(str);
						$("#machine"+machine[i].fid).show();
					}
					else if($.inArray("0010", gatherArr)!=(-1)||$.inArray("0021", gatherArr)!=(-1)||$.inArray("0012", gatherArr)!=(-1)){
						if($("#machine"+machine[i].fid).length<=0){
							var str = '<div id="machine'+machine[i].fid+'" style="position:ABSOLUTE;width:75px;height:95px;left:50px;top:460px;"><a href="td/goNextcurve?value='+machine[i].fid+'&valuename='+machine[i].fequipment_no+'&type='+machine[i].type+'&model='+machine[i].model+'&typeValue=3&flag=2"><img id="img'+machine[i].fid+'" src="resources/images/welder_0'+imgnum+'.png" style="height:100%;width:100%;"></a></div>';
							$("#bodydiv").append(str);
							$("#machine"+machine[i].fid).show();
						}
					}
					else if($.inArray("0004", gatherArr)!=(-1)){
						var str = '<div id="machine'+machine[i].fid+'" style="position:ABSOLUTE;width:75px;height:95px;left:250px;top:460px;"><a href="td/goNextcurve?value='+machine[i].fid+'&valuename='+machine[i].fequipment_no+'&type='+machine[i].type+'&model='+machine[i].model+'&typeValue=1&flag=1"><img id="img'+machine[i].fid+'" src="resources/images/welder_0'+imgnum+'.png" style="height:100%;width:100%;"></a></div>';
						$("#bodydiv").append(str);
						$("#machine"+machine[i].fid).show();
					}
					else if($.inArray("0025", gatherArr)!=(-1)){
						var str = '<div id="machine'+machine[i].fid+'" style="position:ABSOLUTE;width:75px;height:95px;left:1008px;top:620px;"><a href="td/goNextcurve?value='+machine[i].fid+'&valuename='+machine[i].fequipment_no+'&type='+machine[i].type+'&model='+machine[i].model+'&typeValue=4&flag=1"><img id="img'+machine[i].fid+'" src="resources/images/welder_0'+imgnum+'.png" style="height:100%;width:100%;"></a></div>';
						$("#bodydiv").append(str);
						$("#machine"+machine[i].fid).show();
					}
					else if($.inArray("0026", gatherArr)!=(-1)){
						var str = '<div id="machine'+machine[i].fid+'" style="position:ABSOLUTE;width:75px;height:95px;left:1230px;top:620px;"><a href="td/goNextcurve?value='+machine[i].fid+'&valuename='+machine[i].fequipment_no+'&type='+machine[i].type+'&model='+machine[i].model+'&typeValue=3&flag=1"><img id="img'+machine[i].fid+'" src="resources/images/welder_0'+imgnum+'.png" style="height:100%;width:100%;"></a></div>';
						$("#bodydiv").append(str);
						$("#machine"+machine[i].fid).show();
					}
				}
				showflag=1;
				$("#off").html(off.length);
				offFlag=1;
			}
		},
		error : function(errorMsg) {
			alert("数据请求失败，请联系系统管理员!");
		}
	});
	
	//获取焊工信息
	$.ajax({  
	      type : "post",  
	      async : false,
	      url : welderurl,  
	      data : {},  
	      dataType : "json", //返回数据形式为json  
	      success : function(result) {
	          if (result) {
	        	  welderName=eval(result.rows);
	          }  
	      },
	      error : function(errorMsg) {  
	          alert("数据请求失败，请联系系统管理员!");
		}
	});
	//任务
	$.ajax({
		type : "post",
		async : false,
		url : "weldtask/getWeldTask",
		data : {},
		dataType : "json", //返回数据形式为json  
		success : function(result) {
			if (result) {
				taskNum = eval(result.rows);
			}
		},
		error : function(errorMsg) {
			alert("数据请求失败，请联系系统管理员!");
		}
	});
}
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
		//			datatable();
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
//		redata = redata.substring(0,99)+"00010001000100010001"+
//		redata.substring(99,198)+"00010001000100010001"+
//		redata.substring(198)+"00010001000100010001";
		//没有数据时默认显示全部
		if(redata==null || redata=="" && showflag==0){
			for(var i=0;i<machine.length;i++){
				$("#machine"+machine[i].fid).show();
			}
			showflag = 1;
		}
		iview();
		if(symbol==0){
			clearData();
		}
		symbol++;
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

function iview(){
	if(redata.length%135==0){
		for(var i = 0;i < redata.length;i+=135){
//			if(redata.substring(8+i, 12+i)!="0000"){
				for(var f=0;f<machine.length;f++){
					if(machine[f].fid==(parseInt(redata.substring(4+i, 8+i),10))){
						var type = machine[f].type,imgnum=0;
						if(type==41){
							imgnum = 1;
						}else if(type==42){
							imgnum = 3;
						}else if(type==43){
							imgnum = 2;
						}
						var cleardataIndex = $.inArray(parseInt(redata.substring(4+i, 8+i),10), cleardata);
						if(cleardataIndex==(-1)){
							cleardata.push(parseInt(redata.substring(4+i, 8+i),10));
							cleardata.push(new Date().getTime());
						}else{
							cleardata.splice(cleardataIndex+1, 1, new Date().getTime());
						}
//						$("#m3"+machine[f].fid).html("--");
//						$("#m2"+machine[f].fid).html("--");
						for(var k=0;k<welderName.length;k++){
							if(welderName[k].fid==parseInt(redata.substring(0+i, 4+i),10)){
								$("#m3"+machine[f].fid).html(welderName[k].fname);
							}
						}
						for(var t=0;t<taskNum.length;t++){
							if(taskNum[t].id==parseInt(redata.substring(99+i, 103+i),10)){
								$("#m2"+machine[f].fid).html(taskNum[t].weldedJunctionno);
							}
						}
//						var liveele = parseInt(redata.substring(38+i, 42+i),10);
//			            var livevol = parseFloat((parseInt(redata.substring(42+i, 46+i),10)/10).toFixed(2));
//			            $("#m4"+machine[f].fid).html(liveele+"A");
//						$("#m5"+machine[f].fid).html(livevol+"V");
						var mstatus = redata.substring(36 + i, 38 + i);
						var livestatus,livestatusid,liveimg;
						if(mstatus=="00"){
							var num;
							num = $.inArray(parseInt(redata.substring(4+i, 8+i),10), stand);
							if(num==(-1)){
								stand.push(parseInt(redata.substring(4+i, 8+i),10));
							}
							num = $.inArray(parseInt(redata.substring(4+i, 8+i),10), warn);
							if(num!=(-1)){
								warn.splice(num, 1);
							}
							num = $.inArray(parseInt(redata.substring(4+i, 8+i),10), off);
							if(num!=(-1)){
								off.splice(num, 1);
							}
							num = $.inArray(parseInt(redata.substring(4+i, 8+i),10), on);
							if(num!=(-1)){
								on.splice(num, 1);
							}
						}else if(mstatus=="03"||mstatus=="05"||mstatus=="07"){
							var num;
							num = $.inArray(parseInt(redata.substring(4+i, 8+i),10), on);
							if(num==(-1)){
								on.push(parseInt(redata.substring(4+i, 8+i),10));
							}
							num = $.inArray(parseInt(redata.substring(4+i, 8+i),10), warn);
							if(num!=(-1)){
								warn.splice(num, 1);
							}
							num = $.inArray(parseInt(redata.substring(4+i, 8+i),10), off);
							if(num!=(-1)){
								off.splice(num, 1);
							}
							num = $.inArray(parseInt(redata.substring(4+i, 8+i),10), stand);
							if(num!=(-1)){
								stand.splice(num, 1);
							}
						}else{
							var num;
							num = $.inArray(parseInt(redata.substring(4+i, 8+i),10), warn);
							if(num==(-1)){
								warn.push(parseInt(redata.substring(4+i, 8+i),10));
							}
							num = $.inArray(parseInt(redata.substring(4+i, 8+i),10), on);
							if(num!=(-1)){
								on.splice(num, 1);
							}
							num = $.inArray(parseInt(redata.substring(4+i, 8+i),10), off);
							if(num!=(-1)){
								off.splice(num, 1);
							}
							num = $.inArray(parseInt(redata.substring(4+i, 8+i),10), stand);
							if(num!=(-1)){
								stand.splice(num, 1);
							}
						}
						$("#standby").html(stand.length);
						$("#work").html(on.length);
						$("#off").html(off.length);
						$("#warn").html(warn.length);
						switch (mstatus){
						case "00":
							livestatus = "待机";
							liveimg = "resources/images/welder_0"+1+".png";
							break;
						case "03":
							livestatus = "工作";
							liveimg = "resources/images/welder_0"+0+".png";
							break;
						case "05":
							livestatus = "收弧";
							liveimg = "resources/images/welder_0"+0+".png";
							break;
						case "07":
							livestatus = "启弧";
							liveimg = "resources/images/welder_0"+0+".png";
							break;
						default:
							livestatus = "报警";
							liveimg = "resources/images/welder_0"+2+".png";
							break;
						}
						$("#m6"+machine[f].fid).html(livestatus);
						$("#img"+parseInt(redata.substring(4+i, 8+i),10)).attr("src",liveimg);
						$("#machine"+parseInt(redata.substring(4+i, 8+i),10)).show();
					}
				}
//			}
		};
	}
}
  	
function clearData(){
	window.setInterval(function() {
		timeflag = new Date().getTime();
		for(var i=0;i<cleardata.length;i=i+2){
			if(timeflag-cleardata[i+1]>=30000){
				cleardata.splice(i+1, 1);
				$("#img"+cleardata[i]).attr("src","resources/images/welder_03.png");
				var num;
				num = $.inArray(cleardata[i], stand);
				if(num!=(-1)){
					stand.splice(num, 1);
				}
				num = $.inArray(cleardata[i], warn);
				if(num!=(-1)){
					warn.splice(num, 1);
				}
				num = $.inArray(cleardata[i], on);
				if(num!=(-1)){
					on.splice(num, 1);
				}
				num = $.inArray(cleardata[i], off);
				if(num==(-1)){
					off.push(cleardata[i]);
				}
				$("#standby").html(stand.length);
				$("#work").html(on.length);
				$("#off").html(off.length);
				$("#warn").html(warn.length);
				$("#machine"+cleardata[i]).show();
				cleardata.splice(i, 1);
			}
		}
	}, 30000)
}
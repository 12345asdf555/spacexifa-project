$(function () {
    typeCombobox();
    InsframeworkCombobox();
    manuCombobox();
    statusRadio();
    gatherCombobox();
    machineModel();
    searchMachineModel();
//	getTime();
//	$("#iId").combobox({
//        onChange:function(){  
//			itemid = $("#iId").combobox("getValue");
//			gatherCombobox();
//        	if(selectRow!=null){
//    			var str = $("#gid").html();
//    			str += "<option value=\"" + selectRow.gid + "\" >"  
//    			        + selectRow.gatherId + "</option>";
//    			$("#gid").html(str);
//    			$("#gid").combobox();
//    			$("#gid").combobox("select", selectRow.gid);
//        	}
//        } 
//     });
    $('#dlg').dialog({
        onClose: function () {
            $('#iId').combobox('clear');
            $('#manuno').combobox('clear');
            $('#tId').combobox('clear');
            $('#gid').combobox('clear');
            $("#fm").form("disableValidation");
        }
    })
    $("#fm").form("disableValidation");
})


var url = "";
var flag = 1;

function addWeldingMachine() {
    selectRow = null;
    flag = 1;
    $('#fm').form('clear');
    $('#dlg').window({
        title: "新增焊机设备",
        modal: true
    });
    $('#dlg').window('open');
    var isnetworkingId = document.getElementsByName("isnetworkingId");
    var statusId = document.getElementsByName("statusId");
    isnetworkingId[0].checked = 'checked';
    statusId[0].checked = 'checked';
    // url = "weldingMachine/addWeldingMachine";
}

function editWeldingMachine() {
    flag = 2;
    $('#fm').form('clear');
    var row = $('#weldingmachineTable').datagrid('getSelected');
    selectRow = row;
    if (row) {
        $('#dlg').window({
            title: "修改焊机设备",
            modal: true
        });
        $('#dlg').window('open');
        $('#valideno').val(row.equipmentNo);
        $('#validgid').val(row.gatherId);
        $('#validinsf').val(row.iId);
        $('#old_gid').val(row.gid); //修改前的采集id
        $('#fm').form('load', row);
        $("#gid").combobox("setValue", row.gid);
    }
}

//提交
function saveWeldingMachine() {
    var messager = "";
    var url2 = "";
    if (flag == 1) {
        messager = "新增成功！";
        url2 = "weldingMachine/addWeldingMachine";
    } else if (flag == 2){
        messager = "修改成功！";
        var row = $('#weldingmachineTable').datagrid('getSelected')
        url2 = "weldingMachine/editWeldingMachine?wid=" + row.id;
    }
    $('#fm').form('submit', {
        url: url2,
        onSubmit: function () {
            return $(this).form('enableValidation').form('validate');
        },
        success: function (result) {
            if (result) {
                var result = eval('(' + result + ')');
                if (!result.success) {
                    $.messager.show({
                        title: 'Error',
                        msg: result.errorMsg
                    });
                } else {
                    $.messager.alert("提示", messager);
                    $('#dlg').dialog('close');
                    $('#weldingmachineTable').datagrid('reload');
                    $("#weldingmachineTable").datagrid('clearSelections');
//					var url = "weldingMachine/goWeldingMachine";
//					var img = new Image();
//				    img.src = url;  // 设置相对路径给Image, 此时会发送出请求
//				    url = img.src;  // 此时相对路径已经变成绝对路径
//				    img.src = null; // 取消请求
//					window.location.href = encodeURI(url);
                    $("#valideno").val("");
                    $("#validgid").val("");
                    $("#valideid").val("");
                    $('#old_gid').val("");
                }
            }

        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    });
}

var itemid = "";

//采集序号
function gatherCombobox() {
    $.ajax({
        type: "post",
        async: false,
        url: "weldingMachine/getGatherAll?itemid=" + itemid,
        data: {},
        dataType: "json", //返回数据形式为json
        success: function (result) {
            var optionStr = "";
            if (result) {
                if (result.ary.length <= 0) {
                    optionStr += "<option></option>";
                } else {
                    for (var i = 0; i < result.ary.length; i++) {
                        optionStr += "<option value=\"" + result.ary[i].id + "\" >"
                            + result.ary[i].name + "</option>";
                    }
                }
                $("#gid").html(optionStr);
            }
        }
    });
    $.ajax({
        type: "post",
        async: false,
        url: "weldingMachine/getGatherAll?itemid=" + "",
        data: {},
        dataType: "json", //返回数据形式为json
        success: function (result) {
            var optionStr = "";
            if (result) {
                if (result.ary.length <= 0) {
                    optionStr += "<option></option>";
                } else {
                    optionStr += "<option value=''>请选择</option>";
                    for (var i = 0; i < result.ary.length; i++) {
                        optionStr += "<option value=\"" + result.ary[i].id + "\" >"
                            + result.ary[i].name + "</option>";
                    }
                }
                $("#sgid").html(optionStr);
            }
        }
    });
    $("#gid").combobox();
    $("#sgid").combobox();
}

//设备类型
function typeCombobox() {
    $.ajax({
        type: "post",
        async: false,
        url: "weldingMachine/getTypeAll",
        data: {},
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                var optionStr = '', soptionStr = '';
                soptionStr += "<option value=''>请选择</option>";
                for (var i = 0; i < result.ary.length; i++) {
                    optionStr += "<option value=\"" + result.ary[i].id + "\" >"
                        + result.ary[i].name + "</option>";
                    soptionStr += "<option value=\"" + result.ary[i].id + "\" >"
                        + result.ary[i].name + "</option>";
                }
                $("#tId").append(optionStr);
                $("#stId").append(soptionStr);
            }
        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    });
    $("#tId").combobox();
    $("#stId").combobox();
}

//所属项目
function InsframeworkCombobox() {
    $.ajax({
        type: "post",
        async: false,
        url: "weldingMachine/getInsframeworkAll",
        data: {},
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                var optionStr = '', soptionStr = '';
                soptionStr += "<option value=''>请选择</option>";
                for (var i = 0; i < result.ary.length; i++) {
                    optionStr += "<option value=\"" + result.ary[i].id + "\" >"
                        + result.ary[i].name + "</option>";
                    soptionStr += "<option value=\"" + result.ary[i].id + "\" >"
                        + result.ary[i].name + "</option>";
                }
                $("#iId").html(optionStr);
                $("#siId").html(soptionStr);
            }
        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    });
    $("#iId").combobox();
    $("#siId").combobox();
}

//厂商
function manuCombobox() {
    $.ajax({
        type: "post",
        async: false,
        url: "weldingMachine/getManuAll",
        data: {},
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                var optionStr = '', soptionStr = '';
                soptionStr += "<option value=''>请选择</option>";
                for (var i = 0; i < result.ary.length; i++) {
                    optionStr += "<option value=\"" + result.ary[i].id + "\" >"
                        + result.ary[i].name + "</option>";
                    soptionStr += "<option value=\"" + result.ary[i].id + "\" >"
                        + result.ary[i].name + "</option>";
                }
                $("#manuno").html(optionStr);
                $("#smanuno").html(soptionStr);
            }
        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    });
    $("#manuno").combobox();
    $("#smanuno").combobox();
}

//焊机状态
function statusRadio() {
    $.ajax({
        type: "post",
        async: false,
        url: "weldingMachine/getStatusAll",
        data: {},
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                var str = "", sstr = "";
                for (var i = 0; i < result.ary.length; i++) {
                    str += "<input type='radio' class='radioStyle' name='statusId' id='sId' value=\"" + result.ary[i].id + "\" />"
                        + result.ary[i].name;
                    sstr += "<input type='radio' class='radioStyle' name='statusId' id='ssId' value=\"" + result.ary[i].id + "\" />"
                        + result.ary[i].name;
                }
                $("#radios").html(str);
                $("input[name='statusId']").eq(0).attr("checked", true);
                $("#sradios").html(sstr);
//	            $("input[name='sstatusId']").eq(0).attr("checked",true);
            }
        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    });
}

//型号
function machineModel() {
    $("#manuno").combobox({
        onChange: function (newValue, oldValue) {
            $('#model').combobox('clear');
            $.ajax({
                type: "post",
                async: false,
                url: "weldingMachine/getModelAll?str=" + newValue,
                data: {},
                dataType: "json", //返回数据形式为json
                success: function (result) {
                    if (result) {
                        if (result.ary.length != 0) {
                            var boptionStr = '';
                            for (var i = 0; i < result.ary.length; i++) {
                                boptionStr += "<option value=\"" + result.ary[i].id + "\" >"
                                    + result.ary[i].name + "</option>";
                            }
                            $("#model").html(boptionStr);
                            $("#model").combobox({editable:false});
                            $("#model").combobox('select', result.ary[0].id);
                        }
                    }
                },
                error: function (errorMsg) {
                    alert("数据请求失败，请联系系统管理员!");
                }
            });
        }
    })
}

function searchMachineModel() {
    $('#smodel').combobox('clear');
    $.ajax({
        type: "post",
        async: false,
        url: "weldingMachine/getModelAll?str=",
        data: {},
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                if (result.ary.length != 0) {
                    var boptionStr = '';
                    boptionStr += "<option value=''>请选择</option>";
                    for (var i = 0; i < result.ary.length; i++) {
                        boptionStr += "<option value=\"" + result.ary[i].id + "\" >"
                            + result.ary[i].name + "</option>";
                    }
                    $("#smodel").html(boptionStr);
                    $("#smodel").combobox();
//		        	$("#smodel").combobox('select',result.ary[0].id);
                }
            }
        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    });
}

////时间格式化
//function getTime(){
//	//获取当前时间
//	$('#sjoinTime').datebox({
//        onHidePanel : function() {
//        	//获取当前时间
//    		var now = new Date();  
//    		now.setDate(now.getDate());//获取当前的日期 
//    	    var year = now.getFullYear();//年  
//    	    var month = now.getMonth() + 1;//月  
//    	    var day = now.getDate();//日
//    	    
//    	    var oldtime = year + "-";
//    	      
//    	    if(month < 10){
//    	        oldtime += "0";
//    	    }
//    	    oldtime += month + "-";
//    	      
//    	    if(day < 10){
//    	        oldtime += "0";
//    	    }          
//    	    oldtime += day + " 00:00:00";
//    		$("#sjoinTime").datetimebox('setValue',oldtime);
//        },
//        onSelect: function(date){
//        	//获取当前时间
//    		var now = new Date();  
//    		now.setDate(now.getDate());//获取当前的日期 
//    	    var year = now.getFullYear();//年  
//    	    var month = now.getMonth() + 1;//月  
//    	    var day = now.getDate();//日
//    	    
//    	    var oldtime = year + "-";
//    	      
//    	    if(month < 10){
//    	        oldtime += "0";
//    	    }
//    	    oldtime += month + "-";
//    	      
//    	    if(day < 10){
//    	        oldtime += "0";
//    	    }          
//    	    oldtime += day + " 00:00:00";
//    		$("#sjoinTime").datetimebox('setValue',oldtime);
//        } 
//    });
//}
$(function(){
	statusRadio();
	$('#dlg').dialog( {
		onClose : function() {
			$("#fm").form("disableValidation");
		}
	})
	$("#fm").form("disableValidation");
});

function RoleDatagrid(){
	var urls="";
	var row = $('#dg').datagrid('getSelected');
	if(flag==1){
		urls="role/getAllAuthority";
	}else{
		urls="role/getAllAuthority1?id="+row.id;
	}
	$("#tt").datagrid( {
		fitColumns : true,
		height : '250px',
		width : '80%',
		idField : 'roles_name',
		url : urls,
		rownumbers : false,
		showPageList : false,
		checkOnSelect:true,
		selectOnCheck:true,
		columns : [ [ {
		    field:'ck',
			checkbox:true
		},{
			field : 'id',
			title : 'id',
			width : 100,
			halign : "center",
			align : "left",
			hidden: true
		},{
			field : 'authorities_desc',
			title : '权限描述',
			width : 100,
			halign : "center",
			align : "left"
		}]],
		rowStyler: function(index,row){
            if ((index % 2)!=0){
            	//处理行代背景色后无法选中
            	var color=new Object();
                return color;
            }
		},
		onBeforeLoad:function(data){
			 $('#tt').datagrid('clearChecked');
		},
		onLoadSuccess:function(data){
			 if(data){
				 $.each(data.rows, function(index, item){
					 if(item.symbol==1){
				         $('#tt').datagrid('checkRow', index);
					 }
				 })
			 }
		}
	});
}

var url = "";
var flag = 1;
function saveRole(){
	flag = 1;
	$("#fm").form("disableValidation");
	$('#dlg').window( {
		title : "新增角色",
		modal : true
	});
	$('#dlg').window('open');
	$('#fm').form('clear');
	RoleDatagrid();
	var statusid = document.getElementsByName("statusid");
	statusid[0].checked =  'checked';
	url = "role/addRole";
}

function editRole(){
	flag = 2;
	$('#fm').form('clear');
	var row = $('#dg').datagrid('getSelected');
	if (row) {
		$('#dlg').window( {
			title : "修改角色",
			modal : true
		});
		$('#dlg').window('open');
		RoleDatagrid();
		$('#fm').form('load', row);
		$('#validName').val(row.roleName);
		url = "role/updateRole?rid="+ row.id;
	}else{
		alert("请先选择一条数据。");
		return;
	}
}

function validationFrom(){
	return $("#fm").form('enableValidation').form('validate');
}
//提交
function save(){
    var rows = $('#tt').datagrid('getSelections');
    var sid = $("input[name='statusid']:checked").val();
    var roleName = $('#roleName').val();
    var roleDesc = $('#roleDesc').val();
    if(!validationFrom()){
    	return;
    }
    var str="";
	for(var i=0; i<rows.length; i++){
		str += rows[i].id+",";
	}
	var url2 = "";
	if(flag==1){
		messager = "新增成功！";
		url2 = url+"?status="+sid+"&aid="+str+"&roleName="+encodeURI(roleName)+"&roleDesc="+encodeURI(roleDesc);
	}else{
		messager = "修改成功！";
		url2 = url+"&status="+sid+"&aid="+str+"&roleName="+encodeURI(roleName)+"&roleDesc="+encodeURI(roleDesc);
	}
/*	$('#fm').form('submit', {
		url : url2,
		onSubmit : function() {
			return $(this).form('enableValidation').form('validate');
		},
		success : function(result) {
			if(result){
				var result = eval('(' + result + ')');
				if (!result.success) {
					$.messager.show( {
						title : 'Error',
						msg : result.errorMsg
					});
				} else {
					$.messager.alert("提示", messager);
					$('#dlg').dialog('close');
					$('#dg').datagrid('reload');
				}
			}
			alert(123);
			
		},  
	    error : function(errorMsg) {  
	        alert("数据请求失败，请联系系统管理员!");  
	    } 
	});*/
	$.ajax({  
	    type : "post",  
	    async : false,
	    url : url2,  
	    data : {},  
	    dataType : "json", //返回数据形式为json  
	    success : function(result) {
	    	if (result) {
				if (!result.success) {
					$.messager.show( {
						title : 'Error',
						msg : result.errorMsg
					});
				} else {
					$.messager.alert("提示", messager);
					$('#dlg').dialog('close');
					$('#dg').datagrid('reload');
					$("#dg").datagrid('clearSelections');
				}
	    	}  
	    },  
	    error : function(errorMsg) {  
	        alert("数据请求失败，请联系系统管理员!");  
	    }  
	})
}
function statusRadio(){
	$.ajax({  
	    type : "post",  
	    async : false,
	    url : "role/getStatusAll",  
	    data : {},  
	    dataType : "json", //返回数据形式为json  
	    success : function(result) {
	    	if (result) {
	    		var str = "",sstr = "";
	    		for (var i = 0; i < result.ary.length; i++) {
	    			str += "<input type='radio' class='radioStyle' name='statusid' id='sId' value=\"" + result.ary[i].id + "\" />"  
                    + result.ary[i].name+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	    			sstr += "<input type='radio' class='radioStyle' name='sstatusid' id='ssId' value=\"" + result.ary[i].id + "\" />"  
	    			+ result.ary[i].name+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	    		}
	            $("#radios").html(str);
	            $("input[name='statusid']").eq(0).attr("checked",true);
	            $("#sradios").html(sstr);
	        }  
	    },  
	    error : function(errorMsg) {  
	        alert("数据请求失败，请联系系统管理员!");  
	    }  
	})
}
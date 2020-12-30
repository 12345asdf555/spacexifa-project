$(function(){
	weldDatagrid();
});

function weldDatagrid(){
	$("#welderTable").datagrid( {
		height : $("#body").height(),
		width : $("#body").width(),
		idField : 'id',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		url : "welders/getAllWelder",
		singleSelect : true,
		rownumbers : true,
		showPageList : false,
		pagination : true,
		fitColumns : true,
		columns : [ [ {
			field : 'id',
			title : '序号',
			width : 100,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'name',
			title : '姓名',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'welderno',
			title : '编号',
			width : 150,
			halign : "center",
			align : "left"
		}, {
			field : 'cellphone',
			title : '手机',
			width : 150,
			halign : "center",
			align : "left"
		},{
			field : 'leveid',
			title : '级别id',
			width : 100,
			halign : "center",
			align : "left",
			hidden:true
		},{
			field : 'cardnum',
			title : '卡号',
			width : 200,
			halign : "center",
			align : "left"
		}, {
			field : 'levename',
			title : '级别',
			width : 150,
			halign : "center",
			align : "left"
		},{
			field : 'quali',
			title : '资质id',
			width : 100,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'qualiname',
			title : '资质',
			width : 150,
			halign : "center",
			align : "left"
		},{
			field : 'owner',
			title : '部门id',
			width : 100,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'ownername',
			title : '部门',
			width : 150,
			halign : "center",
			align : "left"
		},{
			field : 'back',
			title : '备注',
			width : 200,
			halign : "center",
			align : "left"
		}/*,{
			field : 'edit',
			title : '编辑',
			width : 150,
			halign : "center",
			align : "left",
			formatter:function(value,row,index){
			var str = "";
			str += '<a id="edit" class="easyui-linkbutton" href="javascript:editWelder()"/>';
			str += '<a id="remove" class="easyui-linkbutton" href="javascript:removeWelder()"/>';
			return str;
			}
		}*/
		] ],
		rowStyler: function(index,row){
            if ((index % 2)!=0){
            	//处理行代背景色后无法选中
            	var color=new Object();
                return color;
            }
        },
		nowrap : false,
		onLoadSuccess:function(data){
	        $("a[id='edit']").linkbutton({text:'修改',plain:true,iconCls:'icon-update'});
	        $("a[id='remove']").linkbutton({text:'删除',plain:true,iconCls:'icon-delete'});
	        }
	});
}

//导出到excel
function exportDg(){
	$.messager.confirm("提示", "文件默认保存在浏览器的默认路径，<br/>如需更改路径请设置浏览器的<br/>“下载前询问每个文件的保存位置“属性！",function(result){
		if(result){
			var url = "export/exporWelder";
			var img = new Image();
		    img.src = url;  // 设置相对路径给Image, 此时会发送出请求
		    url = img.src;  // 此时相对路径已经变成绝对路径
		    img.src = null; // 取消请求
			window.location.href = encodeURI(url);
		}
	});
}

//导入
function importclick(){
	$("#importdiv").dialog("open").dialog("setTitle","从excel导入数据");
}

function importWeldingMachine(){
	var file = $("#file").val();
	if(file == null || file == ""){
		$.messager.alert("提示", "请选择要上传的文件！");
		return false;
	}else{
		$('#importfm').form('submit', {
			url : "import/importWelder",
			success : function(result) {
				if(result){
					var result = eval('(' + result + ')');
					if (!result.success) {
						$.messager.show( {
							title : 'Error',
							msg : result.msg
						});
					} else {
						$('#importdiv').dialog('close');
						$('#welderTable').datagrid('reload');
						$.messager.alert("提示", result.msg);
					}
				}
				
			},  
		    error : function(errorMsg) {  
		        alert("数据请求失败，请联系系统管理员!");  
		    } 
		});
	}
}

function searchData(){
	var search = "";
	var sname = $("#sname").textbox('getValue');
	var swelderno = $("#swelderno").textbox('getValue');
	var scellphone = $("#scellphone").textbox('getValue');
	var scardnum = $("#scardnum").textbox('getValue');
	var sleveid = $("#sleveid").combobox("getValue");
	var squali = $("#squali").combobox("getValue");
	var sowner = $("#sowner").combobox("getValue");
	var sback = $("#sback").textbox('getValue');
	if(sname != ""){
		if(search == ""){
			search += " tb_welder.fname LIKE "+"'%" + sname + "%'";
		}else{
			search += " AND tb_welder.fname LIKE "+"'%" + sname + "%'";
		}
	}
	if(swelderno != ""){
		if(search == ""){
			search += " fwelder_no LIKE "+"'%" + swelderno + "%'";
		}else {
			search += " AND fwelder_no LIKE "+"'%" + swelderno + "%'";
		}
	}
	if(scellphone != ""){
		if(search == ""){
			search += " FCellPhone LIKE "+"'%" + scellphone + "%'";
		}else{
			search += " AND FCellPhone LIKE "+"'%" + scellphone + "%'";
		}
	}
	if(scardnum != ""){
		if(search == ""){
			search += " FCardNUm LIKE "+"'%" + scardnum + "%'";
		}else{
			search += " AND FCardNUm LIKE "+"'%" + scardnum + "%'";
		}
	}
	if(sleveid != ""){
		if(search == ""){
			search += " d.fvalue LIKE "+"'%" + sleveid + "%'";
		}else{
			search += " AND d.fvalue LIKE "+"'%" + sleveid + "%'";
		}
	}
	if(squali != ""){
		if(search == ""){
			search += " di.fvalue LIKE "+"'%" + squali + "%'";
		}else{
			search += " AND di.fvalue LIKE "+"'%" + squali + "%'";
		}
	}
	if(sowner != ""){
		if(search == ""){
			search += " i.fid LIKE "+"'%" + sowner + "%'";
		}else{
			search += " AND i.fid LIKE "+"'%" + sowner + "%'";
		}
	}
	if(sback != ""){
		if(search == ""){
			search += " tb_welder.fback LIKE "+"'%" + sback + "%'";
		}else{
			search += " AND tb_welder.fback LIKE "+"'%" + sback + "%'";
		}
	}
	$('#welderTable').datagrid('load', {
		"searchStr" : search
	});
}

//监听窗口大小变化
window.onresize = function() {
	setTimeout(domresize, 500);
}

//改变表格高宽
function domresize() {
	$("#welderTable").datagrid('resize', {
		height : $("#body").height(),
		width : $("#body").width()
	});
}


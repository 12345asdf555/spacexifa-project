<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html style="height: 100%">
  <head>
    <base href="<%=basePath%>">
    
    <title>工艺管理</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link rel="stylesheet" type="text/css" href="" />
	<link rel="stylesheet" type="text/css" href="resources/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="resources/css/datagrid.css" />
	<link rel="stylesheet" type="text/css" href="resources/themes/default/easyui.css" />
	<link rel="stylesheet" type="text/css" href="resources/css/base.css" />
	
	<script type="text/javascript" src="resources/js/weldingtask/json2.js"></script>
	<script type="text/javascript" src="resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="resources/js/jquery.easyui.min.js"></script>
<!-- 	<script type="text/javascript" src="resources/js/datagrid-detailview.js" charset="utf-8"></script> -->
	<script type="text/javascript" src="resources/js/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="resources/js/easyui-extend-check.js"></script>
	<script type="text/javascript" src="resources/js/search/search.js"></script>
	<script type="text/javascript" src="resources/js/wpslib/allWps.js"></script>
 	<script type="text/javascript" src="resources/js/wpslib/addeditWps.js"></script>
	<script type="text/javascript" src="resources/js/wpslib/removeWps.js"></script>
<!--	<script type="text/javascript" src="resources/js/wpslib/giveWpslib.js"></script>
	<script type="text/javascript" src="resources/js/wpslib/differentMachine.js"></script>
	<script type="text/javascript" src="resources/js/wpslib/control.js"></script>
	<script type="text/javascript" src="resources/js/wpslib/comboboxCheck.js"></script> -->
	<script type="text/javascript" src="resources/js/getTimeToHours.js"></script>
	<script type="text/javascript" src="resources/js/swfobject.js"></script>
	<script type="text/javascript" src="resources/js/web_socket.js"></script>
	<style type="text/css">
		table tr td{
			font-size:12px;
		}
		.leftTd{
			text-align: right;
		}
		.textbox-text{
			width:85px;
		}
	</style>
  </head>
  
  <body style="height: 100%">
  	  	<div class="functiondiv">
			<div>
				<div style="float: left;">
					<label>产品图号：</label>
					<input class="easyui-textbox" name="product_drawing_no" id="product_drawing_no" />
				</div>
				<div  style="float: left;">
					<label>产品名称：</label>
					<input class="easyui-textbox" name="product_name" id="product_name" />
				</div>
				<div  style="float: left;">
					<label>产品版本号：</label>
					<input class="easyui-textbox" name="product_version" id="product_version" />
				</div>
				<div>
					<label>工艺规程编号：</label>
					<input class="easyui-textbox" name="wps_lib_name" id="wps_lib_name" />
				</div>
			</div>
			<div>
				<div  style="float: left;">
					<label>工艺规程版本号：</label>
					<input class="easyui-textbox" name="wps_lib_version" id="wps_lib_version" />
				</div>
				<div  style="float: left;">
					<label>工艺来源：</label>
					<select class="easyui-combobox" name="wflag" id="wflag" data-options="editable:false">
						<option value="">无</option>
						<option value="0">自建</option>
						<option value="1">MES</option>
					</select>
				</div>
				<div  style="float: left;">
					<label>工艺审核：</label>
					<select class="easyui-combobox" name="status" id="status" data-options="editable:false">
						<option value="">无</option>
						<option value="0">待审核</option>
						<option value="1">已通过</option>
					</select>
				</div>
				<div  style="float: left;">
					<a href="javascript:searchWps();" class="easyui-linkbutton" iconCls="icon-select">查找</a>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
			</div>
		</div>
  	<div id="body" style="height: 78%">
  		<!-- 工艺台账列表 -->
  		<div id="wpsTableDiv" style="height: auto;">
	   		<table id="wpslibTable" style="table-layout: fixed; width:100%;"></table>
  		</div>
	     <!-- 添加修改工艺台账 -->
		<div id="addOrUpdate" class="easyui-dialog" style="width: 400px; height: 400px; padding:10px 20px" closed="true" buttons="#tdd-buttons">
			<form id="addOrUpdatefm" class="easyui-form" method="post" data-options="novalidate:true">
				<div style="width: 100%">
					<div class="fitem">
						<lable><span class="required">*</span>产品图号</lable>
						<input type="hidden" id="validPdo">
						<input class="easyui-textbox" name="fproduct_drawing_no" id="fproduct_drawing_no"  data-options="required:true"/>
					</div>
					<div class="fitem">
						<lable><span class="required">*</span>产品名称</lable>
						<input class="easyui-textbox" name="fproduct_name" id="fproduct_name"  data-options="required:true"/>
					</div>
					<div class="fitem">
						<lable><span class="required">*</span>产品版本号</lable>
						<input type="hidden" id="validProduct">
						<input class="easyui-textbox" name="fproduct_version" id="fproduct_version"  data-options="required:true"/>
					</div>
					<div class="fitem">
						<lable><span class="required">*</span>工艺规程编号</lable>
						<input type="hidden" id="validWpsname">
						<input class="easyui-textbox" name="fwps_lib_name" id="fwps_lib_name"  data-options="required:true"/>
					</div>
					<div class="fitem">
						<lable><span class="required">*</span>工艺规程版本号</lable>
						<input type="hidden" id="validWpsversion">
						<input class="easyui-textbox" name="fwps_lib_version" id="fwps_lib_version"  data-options="validType:'xifawpsValidate',required:true"/>
					</div>
					<div class="fitem">
						<lable><span class="required">*</span>工艺来源</lable>
						<select class="easyui-combobox" name="flag" id="flag" data-options="required:true,editable:false,disabled:true">
							<option value="0">自建</option>
							<option value="1">MES</option>
						</select>
					</div>
					<div align="center">
						<a href="javascript:saveWps();" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
						<!-- <a href="javascript:closeDialog('wltdlg');" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a> -->
					</div>
				</div>
			</form>
		</div>
		<div id="addVersion-buttons">
			<a href="javascript:saveVersion();" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
			<a href="javascript:closeDialog('addVersionDiv');" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
	    <div class="functiondiv">
			<div>
				<a href="javascript:addWps();" class="easyui-linkbutton" iconCls="icon-newadd">新增工艺</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="javascript:editWps('');" class="easyui-linkbutton" iconCls="icon-update">修改工艺</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="javascript:deleteWps();" class="easyui-linkbutton" iconCls="icon-delete">删除工艺</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="javascript:wpsDetails();" class="easyui-linkbutton" iconCls="icon-navigation">工艺规程详情</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="javascript:addVersion();" class="easyui-linkbutton" iconCls="icon-navigation">新建版本</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="javascript:importclick();" class="easyui-linkbutton" iconCls="icon-newadd"> 工艺导入</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="javascript:exportclick();" class="easyui-linkbutton" iconCls="icon-newadd"> 工艺导出</a>&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
		</div>
		<div id="importdiv" class="easyui-dialog" style="width:300px; height:200px;" closed="true">
			<form id="importfm" method="post" class="easyui-form" data-options="novalidate:true" enctype="multipart/form-data"> 
				<div>
					<span><input type="file" name="file" id="file"></span>
					<input type="button" value="上传" onclick="importWpsExcel()" class="upButton"/>
				</div>
			</form>
		</div>
		<div id="load" style="width:100%;height:100%;"></div>
	</div>
	<style type="text/css">
	    #load{ display: none; position: absolute; left:0; top:0;width: 100%; height: 40%; background-color: #555753; z-index:10001; -moz-opacity: 0.4; opacity:0.5; filter: alpha(opacity=70);}
		#show{display: none; position: absolute; top: 45%; left: 45%; width: 180px; height: 5%; padding: 8px; border: 8px solid #E8E9F7; background-color: white; z-index:10002; overflow: auto;}
	</style>
  </body>
</html>
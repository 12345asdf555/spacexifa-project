<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>焊工管理</title>
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
	
	<script type="text/javascript" src="resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="resources/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="resources/js/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="resources/js/welder/allWelder.js"></script>
	<script type="text/javascript" src="resources/js/easyui-extend-check.js"></script>
	<script type="text/javascript" src="resources/js/search/search.js"></script>
	<script type="text/javascript" src="resources/js/welder/addWelder.js"></script>
	<script type="text/javascript" src="resources/js/welder/destroyWelder.js"></script>
	
  </head>
  <body>
  	  	<div class="functiondiv">
		<div>
			<div style="float: left;">
				<label>姓名：</label>
				<input class="easyui-textbox" name="sname" id="sname" />
			</div>
			<div style="float: left;">
				<label>编号：</label>
				<input class="easyui-textbox" name="swelderno" id="swelderno" />
			</div>
			<div style="float: left;">
				<label>手机：</label>
				<input class="easyui-textbox" name="scellphone" id="scellphone" />
			</div>
			<div>
				<label>卡号：</label>
				<input class="easyui-textbox" name="scardnum" id="scardnum" />
			</div>
		</div>
		<div>
			<div  style="float: left;">
				<label>级别：</label>
				<select class="easyui-combobox" name="sleveid" id="sleveid" data-options="editable:false">
				</select>
			</div>
			<div  style="float: left;">
				<label>资质：</label>
				<select class="easyui-combobox" name="squali" id="squali" data-options="editable:false">
				</select>
			</div>
			<div style="float: left;">
				<label>部门：</label>
				<select class="easyui-combobox" name="sowner" id="sowner" data-options="editable:false">
				</select>
			</div>
			<div style="float: left;">
				<label>备注：</label>
				<input class="easyui-textbox" name="sback" id="sback" />
			</div>
			<div  style="float: left;">
				<a href="javascript:searchData();" class="easyui-linkbutton" iconCls="icon-select">查找</a>&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
		</div>
	</div>
  	<div id="body"  style="height: 78%">
		<div id="importdiv" class="easyui-dialog" style="width:300px; height:200px;" closed="true">
			<form id="importfm" method="post" class="easyui-form" data-options="novalidate:true" enctype="multipart/form-data"> 
				<div>
					<span><input type="file" name="file" id="file"></span>
					<input type="button" value="上传" onclick="importWeldingMachine()" class="upButton"/>
				</div>
			</form>
		</div>
		
	    <table id="welderTable" style="table-layout: fixed; width:100%;"></table>
		<!-- 自定义多条件查询 -->
	    <div id="searchdiv" class="easyui-dialog" style="width:800px; height:400px;" closed="true" buttons="#searchButton" title="自定义条件查询">
	    	<div id="div0">
		    	<select class="fields" id="fields"></select>
		    	<select class="condition" id="condition"></select>
		    	<input class="content" id="content"/>
		    	<select class="joint" id="joint"></select>
		    	<a href="javascript:newSearchWelder();" class="easyui-linkbutton" iconCls="icon-add"></a>
		    	<a href="javascript:removeSerach();" class="easyui-linkbutton" iconCls="icon-remove"></a>
	    	</div>
	    </div>
	    <div id="searchButton">
			<a href="javascript:searchWelder();" class="easyui-linkbutton" iconCls="icon-ok">查询</a>
			<a href="javascript:close();" class="easyui-linkbutton" iconCls="icon-cancel">取消</a>
		</div>
	    <!-- 添加修改 -->
		<div id="dlg" class="easyui-dialog" style="width: 400px; height: 500px; padding:10px 20px" closed="true" buttons="#dlg-buttons">
			<form id="fm" class="easyui-form" method="post" data-options="novalidate:true"><br/>
	            <div class="fitem">
	            	<lable><span class="required">*</span>姓名</lable>
	                <input name="name" class="easyui-textbox" data-options="required:true">
	            </div>
				<div class="fitem">
	            	<lable><span class="required">*</span>编号</lable>
	            	<input id="validName" type="hidden" >
	                <input name="welderno" id="welderno" class="easyui-textbox" data-options="validType:['checkNumber','welderValidate'],required:true">
	            </div>
	            <div class="fitem">
	            	<lable><span class="required"></span>手机</lable>
	                <input name="cellphone" type="easyui-textbox" class="easyui-textbox">
	            </div>
	            <div class="fitem">
	            	<lable><span class="required">*</span>卡号</lable>
	                <input name="cardnum" class="easyui-textbox" data-options="required:true">
	            </div>
	            <div class="fitem">
					<lable><span class="required">*</span>级别</lable>
					<select class="easyui-combobox" name="leveid" id="leveid" data-options="required:true,editable:false"></select>
	        	</div>
	        	<div class="fitem">
					<lable><span class="required">*</span>资质</lable>
					<select class="easyui-combobox" name="quali" id="quali" data-options="required:true,editable:false"></select>
	        	</div>
	            <div class="fitem">
					<lable><span class="required">*</span>部门</lable>
					<select class="easyui-combobox" name="owner" id="owner" data-options="required:true,editable:false"></select>
	        	</div>
				<div class="fitem">
	            	<lable>备注</lable>
	                <input name="back" class="easyui-textbox">
	            </div>
			</form>
		</div>
		<div id="dlg-buttons">
			<a href="javascript:save();" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
			<a href="javascript:close1();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<!-- 删除 -->
		<div id="rdlg" class="easyui-dialog" style="width: 400px; height: 500px; padding:10px 20px" closed="true" buttons="#remove-buttons">
			<form id="rfm" class="easyui-form" method="post" data-options="novalidate:true"><br/>
				<div style="margin-bottom:10px;display: none;">
	                <input name="FID" id="FID"  type="hidden">
	            </div>
	            <div class="fitem">
	            	<lable>编号</lable>
	                <input name="welderno" id="welderno" class="easyui-textbox"  readonly="true" >
	            </div>
	            <div class="fitem">
	            	<lable>姓名</lable>
	                <input name="name" class="easyui-textbox" readonly="true" >
	            </div>
	            <div class="fitem">
	            	<lable>手机</lable>
	                <input name="cellphone" type="easyui-textbox"  class="easyui-textbox" readonly="true" >
	            </div>
	            <div class="fitem">
	            	<lable>卡号</lable>
	                <input name="cardnum" class="easyui-textbox" readonly="true" >
	            </div>
	            <div class="fitem">
					<lable>级别</lable>
					<input name="leveid" id="leveid" type="hidden" >
					<input class="easyui-textbox" name="levename" id="levename" readonly="true" />
	        	</div>
	        	<div class="fitem">
					<lable>资质</lable>
					<input name="quali" id="quali" type="hidden"  >
					<input class="easyui-textbox" name="qualiname" id="qualiname"  readonly="true" />
	        	</div>
	            <div class="fitem">
					<lable>部门</lable>
					<input name="owners" id="owners" type="hidden" >
					<input class="easyui-textbox" name="ownername" id="ownername" readonly="true" />
	        	</div>
				<div class="fitem">
	            	<lable>备注</lable>
	                <input name="back" readonly="true" class="easyui-textbox">
	            </div>
			</form>
		</div>
		<div id="remove-buttons">
			<a href="javascript:remove();" class="easyui-linkbutton" iconCls="icon-ok">删除</a>
			<a href="javascript:close2();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		<div class="functiondiv">
			<div>
				<a href="javascript:saveWelder();" class="easyui-linkbutton" iconCls="icon-newadd">新增</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="javascript:editWelder()" class="easyui-linkbutton" iconCls="icon-update">修改</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="javascript:removeWelder()" class="easyui-linkbutton" iconCls="icon-delete">删除</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="javascript:importclick();" class="easyui-linkbutton" iconCls="icon-import">导入</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="javascript:exportDg();" class="easyui-linkbutton" iconCls="icon-export">导出</a>&nbsp;&nbsp;&nbsp;&nbsp;	
			<!-- 	<a href="javascript:insertSearchWelder();" class="easyui-linkbutton" iconCls="icon-select" >查找</a> -->
			</div>
		</div>
	</div>
  </body>
</html>

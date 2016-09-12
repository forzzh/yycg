<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/base/tag.jsp"%>
<html> 
<head>
<title>添加采购药品查询</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="content-type" content="text/html; charset=UTF-8">

<%@ include file="/WEB-INF/jsp/base/common_css.jsp"%>
<%@ include file="/WEB-INF/jsp/base/common_js.jsp"%>

<script type="text/javascript">

var addyycgdmx = function(){
	_confirm('您确定要执行添加选中的药品吗?',null,
	  function(){
		var indexs = [];//定义一个数组准备存放删除记录的序号
		//通过jquery easyui的datagrid的getSelections函数，得到当前所有选中的行(对象数组)
		var rows = $('#gysypmllist').datagrid('getSelections');
		//循环遍历选中行
		for(var i=0;i<rows.length;i++){
			//通过jquery easyui的datagrid的getRowIndex方法得行的序号
			var index=$('#gysypmllist').datagrid('getRowIndex',rows[i]);
			//将选中行的序号放入indexs数组
			indexs.push(index);
		}
		//如果存在选中的行
		if(rows.length>0){
			//将选中的行通过indexs.join(',')，将选中行的序号中间以逗号分隔组成一个字符串，调用$("#indexs").val方法，将这个字符串放入indexs对象
			$("#indexs").val(indexs.join(','));
			//执行ajax的form提交
			jquerySubByFId('gysypmlForm', addyycgdmx_callback, null);
		}else{
			alert_warn("请选择要添加的药品");
		}
		
	  }
	)
	
};

function addyycgdmx_callback(data) {
	var result = getCallbackData(data);
	_alert(result);//显示失败明细的
     gysypmlquery();//删除完成刷新页面（重新执行查询方法）
}


/* function ypxxedit_callback(redata){
	$('#ypxxquery_div').css("display","none");
	$("#ypxxedit_div").css("display","block");
	$("#ypxxedit_div").html(redata);
} */
function ypxxinfo(id){
	var sendUrl = "${baseurl}ypml/ypxxview.action?id="+id;
	createmodalwindow("药品信息查看", 900, 500, sendUrl);
}



//工具栏

var toolbar = [ {
	id : 'addyycgdmx',
	text : '确认添加',
	iconCls : 'icon-add',
	handler : addyycgdmx
	}];

var frozenColumns;

var columns = [ [{
	checkbox:true
},{
	field : 'id',
	hidden : true,//隐藏
	formatter: function(value,row,index){
		
		return '<input type="hidden" name="yycgdmxCustoms['+index+'].ypxxid" value="'+value+'" />';
	}
},{
	field : 'usergysid',
	hidden : true,//隐藏
	formatter: function(value,row,index){
		
		return '<input type="hidden" name="yycgdmxCustoms['+index+'].usergysid" value="'+value+'" />';
	}
},{
	field : 'usergysmc',
	title : '供货企业',
	width : 80
},{
	field : 'controlmc',
	title : '供货状态',
	width : 80
},{
	field : 'bm',
	title : '流水号',
	width : 80
},{
	field : 'mc',
	title : '通用名',
	width : 130
},{
	field : 'jx',
	title : '剂型',
	width : 80
},{
	field : 'gg',
	title : '规格',
	width : 100
},{
	field : 'zhxs',
	title : '转换系数',
	width : 50
},{
	field : 'scqymc',
	title : '生产企业',
	width : 180
},{
	field : 'spmc',
	title : '商品名称',
	width : 150
},{
	field : 'zbjg',
	title : '中标价',
	width : 50
},{
	field : 'jyztmc',
	title : '交易状态',
	width : 60
}
,{
	field : 'opt3',
	title : '详细',
	width : 60,
	formatter:function(value, row, index){
		return '<a href=javascript:ypxxinfo(\''+row.ypxxid+'\')>查看</a>';
	}
}]];

//datagrid加载
function initGrid(){
	$('#gysypmllist').datagrid({
		title : '供应药品列表',
		//nowrap : false,
		striped : true,
		//collapsible : true,
		url : '${baseurl}cgd/queryaddyycgdmx_result.action',
		 queryParams:{//url的参数，初始加载datagrid时使用的参数
			 yycgdid:'${yycgdid}'//yycgdid是参数名称，如果参数名称中间有点，将参数用单引号括起来
		},
		//sortName : 'code',
		//sortOrder : 'desc',
		//remoteSort : false,
		idField : 'gysypmlid',//json数据集的主键
		//frozenColumns : frozenColumns,
		columns : columns,
		pagination : true,
		rownumbers : true,
		toolbar : toolbar,
		loadMsg:"",
		pageList:[15,30,50,100]/* ,//设置每页显示个数
		onClickRow : function(index, field, value) {
					$('#gysypmllist').datagrid('unselectRow', index);
				},
		//将加载成功后执行：清除选中的行
		onLoadSuccess:function(){
			$('#gysypmllist').datagrid('clearSelections');
		} */
		});

	}
	$(function() {
		initGrid();
		
		/* //加载药品类型
		getDictinfoIdlist('001','ypxxCustom_lb','00101');

		//加载交易状态
		getDictinfoCodelist('003','ypxxCustom.jyzt'); */
	});

	//列表查询
	function gysypmlquery() {
 
		var formdata = $("#gysypmlForm").serializeJson();
		//alert(formdata);
		$('#gysypmllist').datagrid('unselectAll');
		$('#gysypmllist').datagrid('load', formdata);
	}
</script>
</HEAD>
<BODY>
<div id="ypxxquery_div">
    <form id="gysypmlForm" name="gysypmlForm" action="${baseurl}cgd/addyycgdmxsubmit.action" method="post">
			<input type="hidden" name="yycgdid" value="${yycgdid}">
			<input type="hidden" name="indexs" id="indexs" />
			<TABLE  class="table_search">
				<TBODY>
					<TR>
						
						<TD class="left">通用名：</td>
						<td><INPUT type="text"  name="ypxxCustom.mc" /></TD>
						<!-- 自行添加 -->
						<TD class="left">剂型：</TD>
						<td ><INPUT type="text" name="ypxxCustom.jx" /></td>
						<!-- 自行添加 -->
						<TD class="left">规格：</TD>
						<td ><INPUT type="text" name="ypxxCustom.gg" /></td>
						<!-- 自行添加 -->
						<TD class="left">转换系数：</TD>
						<td ><INPUT type="text" name="ypxxCustom.zhxs" /></td>
					</TR>
					<TR>
						<TD class="left">流水号：</TD>
						<td ><INPUT type="text" name="ypxxCustom.bm" /></td>
						<!-- 自行添加 -->
						<TD class="left">生产企业：</TD>
						<td ><INPUT type="text" name="ypxxCustom.scqymc" /></td>
						<!-- 自行添加 -->
						<TD class="left">商品名称：</TD>
						<td ><INPUT type="text" name="ypxxCustom.spmc" /></td>
						<!-- 自行添加 -->
						 <td class="left">价格范围：</td>
				  		<td>
				      		<INPUT id="ypxxCustom.zbjglower" name="ypxxCustom.zbjglower" style="width:70px"/>
							至
							<INPUT id="ypxxCustom.zbjgupper" name="ypxxCustom.zbjgupper" style="width:70px"/>
							
				 		 </td>
					</tr>
					<tr>
					   <!-- 自行添加 -->
						<TD class="left">药品类别：</TD>
						<td >
							<select id="ypxxCustom_lb" name="ypxxCustom.lb" style="width:150px">	
							    <option value="">全部</option> 
								<c:forEach items="${lblist}" var="value">
									<option value="${value.id}">${value.info}</option>
								</c:forEach>
							</select>
						</td>
						<TD class="left">交易状态：</TD>
						<td >
							<select id="ypxxCustom.jyzt" name="ypxxCustom.jyzt" style="width:150px">
							     <option value="">全部</option>
								<c:forEach items="${jyztlist}" var="value">
									<option value="${value.dictcode}">${value.info}</option>
								</c:forEach>
							</select>
							
						</td>
						
				 		 <td class="left" height="25">质量层次：</td>
				  		<td>
				  		<select id="ypxxCustom.zlcc" name="ypxxCustom.zlcc" style="width:150px">
								<option value="">全部</option>
								<c:forEach items="${zlcclist}" var="value">
									<option value="${value.id}">${value.info}</option>
								</c:forEach>
						</select>
					
				  		</td>
				  		<td class="left" height="25">供货状态：</td>
				  		<td>
				  		<select id="gysypmlCustom.control" name="gysypmlCustom.control" style="width:80px">
								<option value="">全部</option>
								<c:forEach items="${controllist}" var="value">
									<option value="${value.dictcode}">${value.info}</option>
								</c:forEach>
						</select>
						<a id="btn" href="#" onclick="gysypmlquery()" class="easyui-linkbutton" iconCls='icon-search'>查询</a>
				  		</td>
						
					</TR>
					
				</TBODY>
			</TABLE>
	   
		<TABLE border=0 cellSpacing=0 cellPadding=0 width="99%" align=center>
			<TBODY>
				<TR>
					<TD>
						<table id="gysypmllist"></table>
					</TD>
				</TR>
			</TBODY>
		</TABLE>
		 </form>
	</div>


</BODY>
</HTML>


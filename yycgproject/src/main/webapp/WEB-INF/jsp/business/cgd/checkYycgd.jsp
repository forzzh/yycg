<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/base/tag.jsp"%>
<html> 
<head>
<title>医院采购单审核</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="content-type" content="text/html; charset=UTF-8">

<%@ include file="/WEB-INF/jsp/base/common_css.jsp"%>
<%@ include file="/WEB-INF/jsp/base/common_js.jsp"%>


<script type="text/javascript">

function yycgdchecksubmit(){
	_confirm('您确认提交审核结果吗?',null,
			  function(){
				var indexs = [];//记录的序号
				var rows = $('#yycgdlist').datagrid('getSelections');//获得以datagrid所有选中的行
				//alert(rows.length);
				for(var i=0;i<rows.length;i++){
					var index=$('#yycgdlist').datagrid('getRowIndex',rows[i]);//获取指定行的序号
					indexs.push(index);
				}
				if(rows.length>0){
					//alert(indexs.join(','));
					$("#indexs").val(indexs.join(','));//将数组数据中间以逗号分隔拼接成一个串，放在indexs里边
					jquerySubByFId('yycgdqueryForm', yycgdchecksubmit_callback, null);
				}else{
					alert_warn("请选择要提交审核的采购单");
				}
				
			  }
			)
}
function yycgdchecksubmit_callback(data){
	var result = getCallbackData(data);
	_alert(result);
	yycgdquery();//刷新本窗口
}

function yycgdview(bm){
	var sendUrl = "${baseurl}cgd/viewcgd.action?id="+bm;
	parent.opentabwindow(bm+'采购单查看',sendUrl);//打开一个新标签
}


//工具栏

var toolbar = [ {
	id : 'yycgdchecksubmit',
	text : '提交审核结果',
	iconCls : 'icon-add',
	handler : yycgdchecksubmit
	}];

var frozenColumns;

var columns = [ [
{
	checkbox:true
},
{
	field : 'id',
	hidden : true,
	formatter: function(value,row,index){
		return '<input type="hidden" name="yycgdCustoms['+index+'].id" value="'+value+'" />';
	}
},
{
	field : 'opt',
	title : '审核结果',
	width : 100,
	formatter: function(value,row,index){
		var string= '<select name="yycgdCustoms['+index+'].zt">'
		+'<option value=""></option>'
		+'<option value="3">审核通过</option>'
		+'<option value="4">审核不通过</option>'
		+'</select>';
		return string
	}
},
{
	field : 'opt2',
	title : '审核意见',
	width : 180,
	formatter: function(value,row,index){
		return '<input type="text" name="yycgdCustoms['+index+'].shyj" />';

	}
},{
	field : 'opt3',
	title : '查看',
	width : 60,
	formatter:function(value, row, index){
		return '<a href=javascript:yycgdview("'+row.bm+'")>查看</a>';
	}
},
 
 {
	field : 'useryymc',
	title : '医院名称',
	width : 100
},{
	field : 'bm',
	title : '采购单编号',
	width : 80
},{
	field : 'mc',
	title : '采购单名称',
	width : 150
},{
	field : 'cjtime',
	title : '建单时间',
	width : 80,
	formatter: function(value,row,index){
		if(value){
			try{
			//通过js日期格式化
			var date = new Date(value);
			var y = date.getFullYear();//获取年
			var m = date.getMonth()+1;//获取月
			var d = date.getDate();
			return y+"-"+m+"-"+d;
			}catch(e){
				alert(e);
			}
		}
		
	}
},{
	field : 'xgtime',
	title : '修改时间',
	width : 80,
	formatter: function(value,row,index){
		if(value){
			try{
			var date = new Date(value);
			var y = date.getFullYear();
			var m = date.getMonth()+1;
			var d = date.getDate();
			return y+"-"+m+"-"+d;
			}catch(e){
				alert(e);
			}
		}
		
	}
},{
	field : 'tjtime',
	title : '提交时间',
	width : 80,
	formatter: function(value,row,index){
		if(value){
			try{
			var date = new Date(value);
			var y = date.getFullYear();
			var m = date.getMonth()+1;
			var d = date.getDate();
			return y+"-"+m+"-"+d;
			}catch(e){
				alert(e);
			}
		}
		
	}
},{
	field : 'shtime',
	title : '审核时间',
	width : 80,
	formatter: function(value,row,index){
		if(value){
			try{
			var date = new Date(value);
			var y = date.getFullYear();
			var m = date.getMonth()+1;
			var d = date.getDate();
			return y+"-"+m+"-"+d;
			}catch(e){
				alert(e);
			}
		}
		
	}
},{
	field : 'yycgdztmc',
	title : '采购单<br>状态', 
	width : 60
}]];

function initGrid(){
	$('#yycgdlist').datagrid({
		title : '采购单列表',
		//nowrap : false,
		striped : true,
		//collapsible : true,
		url : '${baseurl}cgd/checkYycgd_result.action',
		queryParams:{//查询参数，只在加载时使用，点击查询使用load重新加载不使用此参数
			year:'${year}'
		}, 
		//sortName : 'code',
		//sortOrder : 'desc',
		//remoteSort : false,
		idField : 'id',//查询结果集主键采购单id
		//frozenColumns : frozenColumns,
		columns : columns,
		autoRowHeight:true,
		pagination : true,
		rownumbers : true,
		toolbar : toolbar,
		loadMsg:"",
		pageList:[15,30,50,100],
		onClickRow : function(index, field, value) {
					$('#yycgdlist').datagrid('unselectRow', index);
				}
		});

	}
	$(function() {
		initGrid();
	});

	function yycgdquery() {
		var formdata = $("#yycgdqueryForm").serializeJson();//将form中的input数据取出来
		$('#yycgdlist').datagrid('unselectAll');//清空列表所有选中状态
		$('#yycgdlist').datagrid('load', formdata);
	}
	

	
	$(function(){
		//加载采购单状态
		//getDictinfoCodelist('010','yycgdCustom.zt');
		//加载年
		//businessyearlist('businessyear');
	

	});
</script>
</HEAD>
<BODY>
    <form id="yycgdqueryForm" name="yycgdqueryForm" method="post" action="${baseurl}cgd/checkcgdsubmit.action">
    <input type="hidden" id="indexs" name="indexs" />
			<TABLE  class="table_search">
				<TBODY>
					<TR>
						<TD class="left">年份(如2014)：</TD>
						<td >
						<select name="year" id="year">
						<option value="2014">2014</option>
						<option value="2013">2013</option>
						</select>
						
						</td>
						<TD class="left">采购时间：</TD>
						<td >
						 <INPUT id="yycgdCustom.cjtime_start"
							name="yycgdCustom.cjtime_start" 
							 onfocus="WdatePicker({isShowWeek:false,skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" style="width:80px"/>--
					 <INPUT id="yycgdCustom.cjtime_end" 
							name="yycgdCustom.cjtime_end"
							 onfocus="WdatePicker({isShowWeek:false,skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" style="width:80px"/>
						
						</td>
						<TD class="left">医院名称：</TD>
						<td ><INPUT type="text" name="useryyCustom.mc" /></td>
						
						
					</TR>
					<TR> 
					<TD class="left">采购单编号：</td>
						<td><INPUT type="text"  name="yycgdCustom.bm" /></TD>
					<TD class="left">采购单名称：</TD>
						<td ><INPUT type="text" name="yycgdCustom.mc" /></td>
					  
						<td colspan=2>
							
							<a id="btn" href="#" onclick="yycgdquery()" class="easyui-linkbutton" iconCls='icon-search'>查询</a>	
						</td>
					</tr>
					
				</TBODY>
			</TABLE>
	   
		<TABLE border=0 cellSpacing=0 cellPadding=0 width="99%" align=center>
			<TBODY>
				<TR>
					<TD>
						<table id="yycgdlist"></table>
					</TD>
				</TR>
			</TBODY>
		</TABLE>
		 </form>


</BODY>
</HTML>


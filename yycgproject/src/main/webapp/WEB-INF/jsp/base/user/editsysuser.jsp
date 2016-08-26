<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/base/tag.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="${baseurl}js/easyui/styles/default.css">
<%@ include file="/WEB-INF/jsp/base/common_css.jsp"%>
<%@ include file="/WEB-INF/jsp/base/common_js.jsp"%>
<title>修改用户</title>
<script type="text/javascript">
  function sysusersave(){
	
	  jquerySubByFId('userform',sysusersave_callback,null,"json");
	  
  }
  //ajax调用的回调函数，ajax请求完成调用此函数，传入的参数是action返回的结果
  function sysusersave_callback(data){
	 
	  message_alert(data);

	  
  }
 
</script>
</head>
<body>


<form id="userform" action="${baseurl}user/editsysusersubmit.action" method="post">
<!-- 更新用户的id -->
<input type="hidden" name="id" value="${sysuserCustom.id}"/>
<TABLE border=0 cellSpacing=0 cellPadding=0 width="100%" bgColor=#c4d8ed>

   <TBODY>
   <TR>
				<TD background=images/r_0.gif width="100%">
					<TABLE cellSpacing=0 cellPadding=0 width="100%">
						<TBODY>
							<TR>
								<TD>&nbsp;系统用户信息</TD>
								<TD align=right>&nbsp;</TD>
							</TR>
						</TBODY>
					</TABLE>
				</TD>
			</TR>
			
			<TR>
				<TD>
					<TABLE class="toptable grid" border=1 cellSpacing=1 cellPadding=4
						align=center>
						<TBODY>
							
							<TR>
								<TD height=30 width="15%" align=right >用户账号：</TD>
								<TD class=category width="35%">
								<div>
								<input type="text" id="sysuser_userid" name="sysuserCustom.userid" value="${sysuserCustom.userid}" />
								</div>
								<!-- sysuser_useridTip用于显示提示信息，提示div的id等于校验input的id+Tip -->
								<div id="sysuser_useridTip"></div>
								</TD>
								<TD height=30 width="15%" align=right >用户名称：</TD>
								<TD class=category width="35%">
								<div>
								<input type="text" id="sysuser_username" name="sysuserCustom.username" value="${sysuserCustom.username}"  />
								</div>
								<div id="sysuser_usernameTip"></div>
								</TD>
							</TR>
							<TR>
								<TD height=30 width="15%" align=right >用户密码：</TD>
								<TD class=category width="35%">
								<div>
								    如果要修改密码请在此输入<br/>
									<input type="password" id="sysuser_password" name="sysuserCustom.pwd" />
								
								
								</div>
								<div id="sysuser_passwordTip"></div>
								</TD>
								<TD height=30 width="15%" align=right >用户类型：</TD>
								<TD class=category width="35%">
								<div>
								<select name="sysuserCustom.groupid" id="sysuser_groupid">
									
								<option value="">请选择</option>
								<option value="1" <c:if test="${sysuserCustom.groupid=='1'}">selected</c:if>>卫生局</option>
								<option value="2" <c:if test="${sysuserCustom.groupid=='2'}">selected</c:if>>卫生院</option>
								<option value="3" <c:if test="${sysuserCustom.groupid=='3'}">selected</c:if>>卫生室</option>
								<option value="4" <c:if test="${sysuserCustom.groupid=='4'}">selected</c:if>>供货商</option>
								<option value="0" <c:if test="${sysuserCustom.groupid=='0'}">selected</c:if>>系统管理员</option>
	
								</select>
								</div>
								<div id="sysuser_groupidTip"></div>
								</TD>
								
								
							</TR>
							<TR>
							    <TD height=30 width="15%" align=right >用户单位名称：</TD><!-- 用处：根据名称获取单位id -->
								<TD class=category width="35%">
								<input type="text" name="sysuserCustom.sysmc" value="${sysuserCustom.sysmc}"/>
								</TD>
								<TD height=30 width="15%" align=right>用户状态：</TD>
								<TD class=category width="35%">
								<input type="radio" name="sysuserCustom.userstate" value="1" <c:if test="${sysuserCustom.userstate=='1'}">checked</c:if>/>正常
								<input type="radio" name="sysuserCustom.userstate" value="0" <c:if test="${sysuserCustom.userstate=='0'}">checked</c:if>/>暂停
								</TD>
								
							</TR>
							<tr>
							  <td colspan=4 align=center class=category>
								<a id="submitbtn"  class="easyui-linkbutton"   iconCls="icon-ok" href="#" onclick="sysusersave()">提交</a>
								<a id="closebtn"  class="easyui-linkbutton" iconCls="icon-cancel" href="#" onclick="parent.closemodalwindow()">关闭</a>
							  </td>
							</tr>
						
							</TBODY>
						</TABLE>
					</TD>
				</TR>
   </TBODY>
</TABLE>
</form>
</body>
</html>
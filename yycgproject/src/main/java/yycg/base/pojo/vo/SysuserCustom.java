package yycg.base.pojo.vo;

import yycg.base.pojo.po.Sysuser;

/**
 * 
 * <p>Title: SysuserCustom</p>
 * <p>Description: 扩展类，用于提交信息、查询条件</p>
 * <p>Company: www.itcast.com</p> 
 * @author	苗润土
 * @date	2014年11月26日上午10:38:43
 * @version 1.0
 */
public class SysuserCustom extends Sysuser {
	
	//单位名称 
	private String sysmc;
	
	//用户类型名称 
	private String groupname;

	public String getSysmc() {
		return sysmc;
	}

	public void setSysmc(String sysmc) {
		this.sysmc = sysmc;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	
	
}

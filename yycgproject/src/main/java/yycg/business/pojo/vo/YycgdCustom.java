package yycg.business.pojo.vo;

import java.util.Date;

import yycg.business.pojo.po.Yycgd;

public class YycgdCustom extends Yycgd {
	
	//医院名称 
	private String useryymc;
	//采购单状态名称
	private String yycgdztmc;
	
	//采购起始时间
	private Date cjtime_start;
	
	//采购截止时间
	private Date cjtime_end;

	public String getYycgdztmc() {
		return yycgdztmc;
	}

	public void setYycgdztmc(String yycgdztmc) {
		this.yycgdztmc = yycgdztmc;
	}

	public Date getCjtime_start() {
		return cjtime_start;
	}

	public void setCjtime_start(Date cjtime_start) {
		this.cjtime_start = cjtime_start;
	}

	public Date getCjtime_end() {
		return cjtime_end;
	}

	public void setCjtime_end(Date cjtime_end) {
		this.cjtime_end = cjtime_end;
	}

	public String getUseryymc() {
		return useryymc;
	}

	public void setUseryymc(String useryymc) {
		this.useryymc = useryymc;
	}
	
	

}

package yycg.business.pojo.vo;

import yycg.business.pojo.po.Ypxx;

public class YpxxCustom extends Ypxx{
	
	//开始价格，查询条件 
	private Float price_start;
	//结束价格，查询条件 
	private Float price_end;
	
	//交易状态名称
	private String jyztmc;

	public String getJyztmc() {
		return jyztmc;
	}

	public void setJyztmc(String jyztmc) {
		this.jyztmc = jyztmc;
	}

	public Float getPrice_start() {
		return price_start;
	}

	public void setPrice_start(Float price_start) {
		this.price_start = price_start;
	}

	public Float getPrice_end() {
		return price_end;
	}

	public void setPrice_end(Float price_end) {
		this.price_end = price_end;
	}
	
	
}

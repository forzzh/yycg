package yycg.base.service;

import java.util.List;

import yycg.base.pojo.po.Basicinfo;
import yycg.base.pojo.po.Dictinfo;

/**
 * 系统级别service
 * @author Thinkpad
 *
 */
public interface SystemConfigService {
	/**
	 * 根据typecode获取数据字典的信息
	 */
	public List findDictinfoByType(String typecode) throws Exception;
	
	/**
	 * 根据数据字典中的typecode，和dictcode获取一条信息
	 */
	public Dictinfo  findDictinfoByDictcode(String typecode,String dictcode) throws Exception;
	
	/**
	 * 根据系统参数id获取系统参数表信息
	 */
	public Basicinfo findBasicinfoById(String id)throws Exception;
}

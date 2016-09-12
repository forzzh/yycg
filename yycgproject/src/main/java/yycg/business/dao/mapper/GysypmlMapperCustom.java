package yycg.business.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import yycg.business.pojo.po.Gysypml;
import yycg.business.pojo.po.GysypmlExample;
import yycg.business.pojo.vo.GysypmlCustom;
import yycg.business.pojo.vo.GysypmlQueryVo;

public interface GysypmlMapperCustom {
    
	//供货商药品目录查询列表
	public List<GysypmlCustom> findGysypmlList(GysypmlQueryVo gysypmlQueryVo) throws Exception;
	public int findGysypmlCount(GysypmlQueryVo gysypmlQueryVo) throws Exception;
	//供货商添加药品目录查询列表
	public List<GysypmlCustom> findAddGysypmlList(GysypmlQueryVo gysypmlQueryVo) throws Exception;
	public int findAddGysypmlCount(GysypmlQueryVo gysypmlQueryVo) throws Exception;
	
	//供货商药品目录控制列表
	public List<GysypmlCustom> findGysypmlControlList(GysypmlQueryVo gysypmlQueryVo) throws Exception;
	public int findGysypmlControlCount(GysypmlQueryVo gysypmlQueryVo) throws Exception;
}
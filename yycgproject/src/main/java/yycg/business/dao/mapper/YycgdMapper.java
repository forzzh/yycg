package yycg.business.dao.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import yycg.business.pojo.po.Yycgd;
import yycg.business.pojo.po.YycgdExample;

public interface YycgdMapper {
    int countByExample(YycgdExample example);

    int deleteByExample(YycgdExample example);

    int deleteByPrimaryKey(String id);

    int insert(Yycgd record);

    int insertSelective(Yycgd record);

    List<Yycgd> selectByExample(YycgdExample example);

    Yycgd selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Yycgd record, @Param("example") YycgdExample example);

    int updateByExample(@Param("record") Yycgd record, @Param("example") YycgdExample example);

    int updateByPrimaryKeySelective(Yycgd record);

    int updateByPrimaryKey(Yycgd record);
}
package yycg.business.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import yycg.base.dao.mapper.UseryyMapper;
import yycg.base.pojo.po.Useryy;
import yycg.base.process.context.Config;
import yycg.base.process.result.ResultUtil;
import yycg.base.service.SystemConfigService;
import yycg.business.dao.mapper.YpxxMapper;
import yycg.business.dao.mapper.YycgdMapper;
import yycg.business.dao.mapper.YycgdMapperCustom;
import yycg.business.dao.mapper.YycgdmxMapper;
import yycg.business.pojo.po.Ypxx;
import yycg.business.pojo.po.Yycgd;
import yycg.business.pojo.po.YycgdExample;
import yycg.business.pojo.po.Yycgdmx;
import yycg.business.pojo.po.YycgdmxExample;
import yycg.business.pojo.vo.YycgdCustom;
import yycg.business.pojo.vo.YycgdQueryVo;
import yycg.business.pojo.vo.YycgdmxCustom;
import yycg.business.service.CgdService;
import yycg.util.UUIDBuild;

public class CgdServiceImpl implements CgdService {

	@Autowired
	private YycgdMapper yycgdMapper;

	@Autowired
	private YycgdMapperCustom yycgdMapperCustom;

	@Autowired
	private SystemConfigService systemConfigService;

	@Autowired
	private UseryyMapper useryyMapper;

	@Autowired
	private YpxxMapper ypxxMapper;

	@Autowired
	private YycgdmxMapper yycgdmxMapper;

	@Override
	public String insertYycgd(String useryyid, String year,
			YycgdCustom yycgdCustom) throws Exception {

		// 采购单号
		String bm = yycgdMapperCustom.getYycgdBm(year);
		// 采购单主键
		yycgdCustom.setId(bm);// 采购单id主键和bm一致，目的是为了方便操作采购单
		// 采购单号
		yycgdCustom.setBm(bm);

		// 创建采购单医院
		yycgdCustom.setUseryyid(useryyid);

		// 创建时间
		yycgdCustom.setCjtime(new Date());

		// 采购单状态
		yycgdCustom.setZt("1");// 默认为未提交

		// 设置年份，为了操作动态表
		yycgdCustom.setBusinessyear(year);

		yycgdMapper.insert(yycgdCustom);
		// 返回采购单id
		return bm;

	}

	@Override
	public YycgdCustom findYycgdById(String id) throws Exception {

		// 从采购单id中获取年份
		String year = id.substring(0, 4);
		YycgdExample yycgdExample = new YycgdExample();

		YycgdExample.Criteria criteria = yycgdExample.createCriteria();
		criteria.andIdEqualTo(id);
		// 通过 yycgdExample传入年份
		yycgdExample.setBusinessyear(year);

		List<Yycgd> list = yycgdMapper.selectByExample(yycgdExample);
		Yycgd yycgd = null;
		YycgdCustom yycgdCustom = new YycgdCustom();
		if (list != null && list.size() == 1) {
			yycgd = list.get(0);
			// 将yycgd属性值 拷贝到yycgdCustom
			BeanUtils.copyProperties(yycgd, yycgdCustom);
		} else {
			// 抛出异常
			// 系统中找到该采购单。。。
			ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE, 501,
					null));
		}

		// 可以向扩展对象添加属性值
		// 添加采购单状态 名称
		String zt = yycgd.getZt();

		// 根据状态 代码查询状态对应的名称
		String yycgdztmc = systemConfigService
				.findDictinfoByDictcode("010", zt).getInfo();
		yycgdCustom.setYycgdztmc(yycgdztmc);

		return yycgdCustom;
	}

	@Override
	public void updateYycgd(String id, YycgdCustom yycgdCustom)
			throws Exception {
		// 从采购单id中获取年份
		String year = id.substring(0, 4);
		// 从数据库查询采购单信息
		YycgdCustom yycgdCustom_old = this.findYycgdById(id);
		// 向对象设置修改的值
		yycgdCustom_old.setLxr(yycgdCustom.getLxr());
		yycgdCustom_old.setLxdh(yycgdCustom.getLxdh());
		yycgdCustom_old.setMc(yycgdCustom.getMc());
		yycgdCustom_old.setBz(yycgdCustom.getBz());// 备注信息
		// 设置年份
		yycgdCustom_old.setBusinessyear(year);
		yycgdMapper.updateByPrimaryKey(yycgdCustom_old);

	}

	@Override
	public List<YycgdmxCustom> findYycgdmxListByYycgdid(String yycgdid,
			YycgdQueryVo yycgdQueryVo) throws Exception {

		// 非空判断
		yycgdQueryVo = yycgdQueryVo != null ? yycgdQueryVo : new YycgdQueryVo();

		// 通过采购单id得到年份
		String businessyear = yycgdid.substring(0, 4);

		// 在service设置固定业务参数
		YycgdmxCustom yycgdmxCustom = yycgdQueryVo.getYycgdmxCustom();
		if (yycgdmxCustom == null) {
			yycgdmxCustom = new YycgdmxCustom();
		}
		yycgdmxCustom.setYycgdid(yycgdid);
		yycgdQueryVo.setYycgdmxCustom(yycgdmxCustom);

		// 设置年份
		yycgdQueryVo.setBusinessyear(businessyear);

		return yycgdMapperCustom.findYycgdmxList(yycgdQueryVo);
	}

	@Override
	public int findYycgdmxCountByYycgdid(String yycgdid,
			YycgdQueryVo yycgdQueryVo) throws Exception {
		// 非空判断
		yycgdQueryVo = yycgdQueryVo != null ? yycgdQueryVo : new YycgdQueryVo();
		// 通过采购单id得到年份
		String businessyear = yycgdid.substring(0, 4);
		// 在service设置固定业务参数
		YycgdmxCustom yycgdmxCustom = yycgdQueryVo.getYycgdmxCustom();
		if (yycgdmxCustom == null) {
			yycgdmxCustom = new YycgdmxCustom();
		}
		yycgdmxCustom.setYycgdid(yycgdid);
		yycgdQueryVo.setYycgdmxCustom(yycgdmxCustom);
		// 设置年份
		yycgdQueryVo.setBusinessyear(businessyear);
		return yycgdMapperCustom.findYycgdmxCount(yycgdQueryVo);
	}

	@Override
	public List<YycgdmxCustom> findAddYycgdmxList(String useryyid,
			String yycgdid, YycgdQueryVo yycgdQueryVo) throws Exception {

		// 根据医院id得到医院区域id
		Useryy useryy = useryyMapper.selectByPrimaryKey(useryyid);

		String dq = useryy.getDq();// 医院区域id

		// 向yycgdQueryVo设置业务参数
		// 医院地区
		Useryy useryy_l = yycgdQueryVo.getUseryy();
		if (useryy_l == null) {
			useryy_l = new Useryy();
		}
		useryy_l.setDq(dq);
		yycgdQueryVo.setUseryy(useryy_l);

		// 采购单id
		YycgdCustom yycgdCustom = yycgdQueryVo.getYycgdCustom();
		if (yycgdCustom == null) {
			yycgdCustom = new YycgdCustom();
		}
		yycgdCustom.setId(yycgdid);
		yycgdQueryVo.setYycgdCustom(yycgdCustom);

		// 设置年份
		String businessyear = yycgdid.substring(0, 4);
		yycgdQueryVo.setBusinessyear(businessyear);

		return yycgdMapperCustom.findAddYycgdmxList(yycgdQueryVo);
	}

	@Override
	public int findAddYycgdmxCount(String useryyid, String yycgdid,
			YycgdQueryVo yycgdQueryVo) throws Exception {

		// 根据医院id得到医院区域id
		Useryy useryy = useryyMapper.selectByPrimaryKey(useryyid);

		String dq = useryy.getDq();// 医院区域id

		// 向yycgdQueryVo设置业务参数
		// 医院地区
		Useryy useryy_l = yycgdQueryVo.getUseryy();
		if (useryy_l == null) {
			useryy_l = new Useryy();
		}
		useryy_l.setDq(dq);
		yycgdQueryVo.setUseryy(useryy_l);

		// 采购单id
		YycgdCustom yycgdCustom = yycgdQueryVo.getYycgdCustom();
		if (yycgdCustom == null) {
			yycgdCustom = new YycgdCustom();
		}
		yycgdCustom.setId(yycgdid);
		yycgdQueryVo.setYycgdCustom(yycgdCustom);

		// 设置年份
		String businessyear = yycgdid.substring(0, 4);
		yycgdQueryVo.setBusinessyear(businessyear);

		return yycgdMapperCustom.findAddYycgdmxCount(yycgdQueryVo);
	}

	// 根据采购单id和药品id查询采购单明细
	public Yycgdmx findYycgdmxByYycgdidAndYpxxid(String yycgdid, String ypxxid)
			throws Exception {
		YycgdmxExample yycgdmxExample = new YycgdmxExample();
		YycgdmxExample.Criteria criteria = yycgdmxExample.createCriteria();
		criteria.andYycgdidEqualTo(yycgdid);
		criteria.andYpxxidEqualTo(ypxxid);
		// 设置年份
		String year = yycgdid.substring(0, 4);
		yycgdmxExample.setBusinessyear(year);
		List<Yycgdmx> list = yycgdmxMapper.selectByExample(yycgdmxExample);
		if (list != null && list.size() == 1) {
			return list.get(0);
		}
		return null;

	}

	@Override
	public void insertYycgdmx(String yycgdid, String ypxxid, String usergysid)
			throws Exception {

		// 根据药品id得到药品信息
		Ypxx ypxx = ypxxMapper.selectByPrimaryKey(ypxxid);
		if (ypxx == null) {
			// 抛出异常，药品在系统中不存在
			// ...
		}

		// 校验采购单明细表唯 一约束
		Yycgdmx yycgdmx_l = this.findYycgdmxByYycgdidAndYpxxid(yycgdid, ypxxid);
		if (yycgdmx_l != null) {
			// 该药品在采购单中已存在
			ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE, 508,
					null));
		}

		String businessyear = yycgdid.substring(0, 4);
		// 对比数据表准备、处理数据

		Yycgdmx yycgdmx = new Yycgdmx();
		// 设置年份
		yycgdmx.setBusinessyear(businessyear);
		yycgdmx.setId(UUIDBuild.getUUID());// 主键
		yycgdmx.setYycgdid(yycgdid);
		yycgdmx.setYpxxid(ypxxid);
		yycgdmx.setUsergysid(usergysid);
		yycgdmx.setZbjg(ypxx.getZbjg());// 中标价格
		yycgdmx.setJyjg(ypxx.getZbjg());// 本系统交易价格和中标价格相等
		yycgdmx.setCgzt("1");// 默认1、未确认送货

		yycgdmxMapper.insert(yycgdmx);

	}

	@Override
	public void updateYycgdmx(String yycgdid, String ypxxid, Integer cgl)
			throws Exception {

		String businessyear = yycgdid.substring(0, 4);

		// 根据采购单id和药品id获取采购单明细记录
		Yycgdmx yycgdmx = this.findYycgdmxByYycgdidAndYpxxid(yycgdid, ypxxid);
		if (yycgdmx == null) {
			// 抛出异常，找不到采购药品记录
			// ....
		}
		// 校验采购量
		if (cgl == null || cgl <= 0) {
			// 请输入大于0的数值
			// ..
		}

		// 计算采购金额
		Float jyjg = yycgdmx.getJyjg();
		// 采购金额
		Float cgje = jyjg * cgl;

		// 定义一个更新对象
		Yycgdmx yycgdmx_update = new Yycgdmx();
		yycgdmx_update.setId(yycgdmx.getId());
		yycgdmx_update.setCgl(cgl);
		yycgdmx_update.setCgje(cgje);
		// 设置年份
		yycgdmx_update.setBusinessyear(businessyear);
		yycgdmxMapper.updateByPrimaryKeySelective(yycgdmx_update);

	}

	@Override
	public List<YycgdCustom> findYycgdList(String useryyid, String year,
			YycgdQueryVo yycgdQueryVo) throws Exception {

		yycgdQueryVo = yycgdQueryVo != null ? yycgdQueryVo : new YycgdQueryVo();
		// 设置查询年份
		yycgdQueryVo.setBusinessyear(year);

		// 设置医院id
		Useryy useryy = yycgdQueryVo.getUseryy();
		if (useryy == null) {
			useryy = new Useryy();
		}
		useryy.setId(useryyid);
		yycgdQueryVo.setUseryy(useryy);

		return yycgdMapperCustom.findYycgdList(yycgdQueryVo);
	}

	@Override
	public int findYycgdCount(String useryyid, String year,
			YycgdQueryVo yycgdQueryVo) throws Exception {

		yycgdQueryVo = yycgdQueryVo != null ? yycgdQueryVo : new YycgdQueryVo();
		// 设置查询年份
		yycgdQueryVo.setBusinessyear(year);
		// 设置医院id
		Useryy useryy = yycgdQueryVo.getUseryy();
		if (useryy == null) {
			useryy = new Useryy();
		}
		useryy.setId(useryyid);
		yycgdQueryVo.setUseryy(useryy);

		return yycgdMapperCustom.findYycgdCount(yycgdQueryVo);
	}

}

package yycg.base.service.impl;

import java.util.List;

import org.codehaus.jackson.map.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import yycg.base.dao.mapper.SysuserMapper;
import yycg.base.dao.mapper.SysuserMapperCustom;
import yycg.base.dao.mapper.UsergysMapper;
import yycg.base.dao.mapper.UserjdMapper;
import yycg.base.dao.mapper.UseryyMapper;
import yycg.base.pojo.po.Sysuser;
import yycg.base.pojo.po.SysuserExample;
import yycg.base.pojo.po.Usergys;
import yycg.base.pojo.po.UsergysExample;
import yycg.base.pojo.po.Userjd;
import yycg.base.pojo.po.UserjdExample;
import yycg.base.pojo.po.Useryy;
import yycg.base.pojo.po.UseryyExample;
import yycg.base.pojo.vo.ActiveUser;
import yycg.base.pojo.vo.SysuserCustom;
import yycg.base.pojo.vo.SysuserQueryVo;
import yycg.base.process.context.Config;
import yycg.base.process.result.ExceptionResultInfo;
import yycg.base.process.result.ResultInfo;
import yycg.base.process.result.ResultUtil;
import yycg.base.service.UserService;
import yycg.util.MD5;
import yycg.util.ResourcesUtil;
import yycg.util.UUIDBuild;

public class UserServiceImpl implements UserService {

	// 注入 mapper
	@Autowired
	private SysuserMapper sysuserMapper;

	@Autowired
	private UserjdMapper userjdMapper;

	@Autowired
	private UseryyMapper useryyMapper;

	@Autowired
	private UsergysMapper usergysMapper;

	@Autowired
	private SysuserMapperCustom sysuserMapperCustom;

	@Override
	public List<SysuserCustom> findSysuserList(SysuserQueryVo sysuserQueryVo)
			throws Exception {
		return sysuserMapperCustom.findSysuserList(sysuserQueryVo);
	}

	@Override
	public int findSysuserCount(SysuserQueryVo sysuserQueryVo) throws Exception {
		return sysuserMapperCustom.findSysuserCount(sysuserQueryVo);
	}

	// 根据账号查询用户方法
	public Sysuser findSysuserByUserid(String userId) throws Exception {
		SysuserExample sysuserExample = new SysuserExample();
		SysuserExample.Criteria criteria = sysuserExample.createCriteria();
		// 设置查询条件，根据账号查询
		criteria.andUseridEqualTo(userId);
		List<Sysuser> list = sysuserMapper.selectByExample(sysuserExample);

		if (list != null && list.size() == 1) {
			return list.get(0);
		}
		return null;
	}

	// 根据单位名称 查询单位信息
	public Userjd findUserjdByMc(String mc) throws Exception {

		UserjdExample userjdExample = new UserjdExample();
		UserjdExample.Criteria criteria = userjdExample.createCriteria();
		criteria.andMcEqualTo(mc);
		List<Userjd> list = userjdMapper.selectByExample(userjdExample);

		if (list != null && list.size() == 1) {
			return list.get(0);
		}
		return null;

	}

	public Useryy findUseryyByMc(String mc) throws Exception {

		UseryyExample useryyExample = new UseryyExample();
		UseryyExample.Criteria criteria = useryyExample.createCriteria();
		criteria.andMcEqualTo(mc);
		List<Useryy> list = useryyMapper.selectByExample(useryyExample);

		if (list != null && list.size() == 1) {
			return list.get(0);
		}
		return null;

	}

	public Usergys findUsergysByMc(String mc) throws Exception {

		UsergysExample usergysExample = new UsergysExample();
		UsergysExample.Criteria criteria = usergysExample.createCriteria();
		criteria.andMcEqualTo(mc);
		List<Usergys> list = usergysMapper.selectByExample(usergysExample);

		if (list != null && list.size() == 1) {
			return list.get(0);
		}
		return null;

	}

	@Override
	public void insertSysuser(SysuserCustom sysuserCustom) throws Exception {

		// 参数校验
		// 通用的参数合法校验，非空校验，长度校验
		// ...使用一些工具类来完成

		// 数据业务合法性校验
		// 账号唯一性校验，查询数据库校验出来
		// 思路：根据用户账号查询sysuser表，如果查询到说明 账号重复
		Sysuser sysuser = this.findSysuserByUserid(sysuserCustom.getUserid());
		if (sysuser != null) {
			// 账号重复
			// 抛出异常，可预知异常
			// throw new Exception("账号重复");

			// 使用系统自定义异常类
			/*
			 * ResultInfo resultInfo = new ResultInfo();
			 * resultInfo.setType(ResultInfo.TYPE_RESULT_FAIL); //从资源文件中获取异常信息
			 * String message = ResourcesUtil.getValue("resources.messages",
			 * "213"); resultInfo.setMessage(message);
			 */
			// 使用ResultUtil构造resultInfo
			// ResultInfo resultInfo =
			// ResultUtil.createFail("resources.messages", 213, null);

			// throw new ExceptionResultInfo(resultInfo);
			ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE, 213,
					null));
		}

		// 根据用户类型，输入单位名称必须存在对应的单位表
		String groupid = sysuserCustom.getGroupid();// 用户类型
		String sysmc = sysuserCustom.getSysmc();// 单位名称
		String sysid = null;// 单位id
		if (groupid.equals("1") || groupid.equals("2")) {
			// 监督单位
			// 根据单位名称查询单位信息
			Userjd userjd = this.findUserjdByMc(sysmc);
			if (userjd == null) {
				// 抛出异常，可预知异常
				throw new Exception("单位名称输入错误");
			}
			sysid = userjd.getId();
		} else if (groupid.equals("3")) {
			// 卫生室
			// 根据单位名称查询单位信息
			Useryy useryy = this.findUseryyByMc(sysmc);
			if (useryy == null) {
				// 抛出异常，可预知异常
				throw new Exception("单位名称输入错误");
			}
			sysid = useryy.getId();
		} else if (groupid.equals("4")) {
			// 供货商
			// 根据单位名称查询单位信息
			Usergys usergys = this.findUsergysByMc(sysmc);
			if (usergys == null) {
				// 抛出异常，可预知异常
				throw new Exception("单位名称输入错误");
			}
			sysid = usergys.getId();
		}
		// 设置主键
		sysuserCustom.setId(UUIDBuild.getUUID());
		// 设置单位id
		sysuserCustom.setSysid(sysid);
		//对密码加密 
		sysuserCustom.setPwd(new MD5().getMD5ofStr(sysuserCustom.getPwd()));
		sysuserMapper.insert(sysuserCustom);

	}

	@Override
	public void deleteSysuser(String id) throws Exception {
		// 校验约束
		// 校验用户是否存在
		Sysuser sysuser = sysuserMapper.selectByPrimaryKey(id);
		if (sysuser == null) {
			// 用户不存在
			ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE, 212,
					null));
		}
		// 执行删除
		sysuserMapper.deleteByPrimaryKey(id);

	}

	@Override
	public void updateSysuser(String id, SysuserCustom sysuserCustom)
			throws Exception {
		// 非空校验。。。

		// 修改用户账号不允许暂用别人的账号
		// 如果判断账号修改了
		// 页面提交的账号可能是用户修改的账号
		String userid_page = sysuserCustom.getUserid();

		// 数据库中的账号是用户原始账号
		// 通过id查询数据库
		Sysuser sysuser = sysuserMapper.selectByPrimaryKey(id);
		if (sysuser == null) {
			// 抛出异常
			// 找不到要修改用户信息
			// .....
		}
		// 用户原始账号
		String userid = sysuser.getUserid();

		if (!userid_page.equals(userid)) {
			// 通过页面提交的账号查询数据库，如果查到说明暂用别人的账号
			Sysuser sysuser_1 = this.findSysuserByUserid(userid_page);
			if (sysuser_1 != null) {
				// 说明暂用别人的账号
				ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE,
						213, null));
			}
		}

		// 根据页面提交的单位名称查询单位id
		String groupid = sysuserCustom.getGroupid();// 用户类型
		String sysmc = sysuserCustom.getSysmc();// 页面输入的单位名称
		String sysid = null;// 单位id
		if (groupid.equals("1") || groupid.equals("2")) {
			// 监督单位
			// 根据单位名称查询单位信息
			Userjd userjd = this.findUserjdByMc(sysmc);
			if (userjd == null) {
				// 抛出异常，可预知异常
				ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE,
						217, null));
			}
			sysid = userjd.getId();
		} else if (groupid.equals("3")) {
			// 卫生室
			// 根据单位名称查询单位信息
			Useryy useryy = this.findUseryyByMc(sysmc);
			if (useryy == null) {
				// 抛出异常，可预知异常
				ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE,
						217, null));
			}
			sysid = useryy.getId();
		} else if (groupid.equals("4")) {
			// 供货商
			// 根据单位名称查询单位信息
			Usergys usergys = this.findUsergysByMc(sysmc);
			if (usergys == null) {
				// 抛出异常，可预知异常
				ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE,
						217, null));
			}
			sysid = usergys.getId();
		}

		// 密码修改
		// 如果从页面提交的密码值为空说明用户不修改密码，否则 就需要对密码进行加密存储
		String pwd_page = sysuserCustom.getPwd().trim();
		String pwd_md5 = null;
		if (pwd_page != null && !pwd_page.equals("")) {
			// 说明用户修改密码了
			pwd_md5 = new MD5().getMD5ofStr(pwd_page);
		}

		// 设置更新用户信息

		// 调用mapper更新用户
		// 使用updateByPrimaryKey更新，要先查询用户

		Sysuser sysuser_update = sysuserMapper.selectByPrimaryKey(id);

		sysuser_update.setUserid(sysuserCustom.getUserid());
		sysuser_update.setUsername(sysuserCustom.getUsername());
		sysuser_update.setUserstate(sysuserCustom.getUserstate());
		if (pwd_md5 != null) {
			sysuser_update.setPwd(pwd_md5);
		}
		sysuser_update.setGroupid(sysuserCustom.getGroupid());
		sysuser_update.setSysid(sysid);// 单位id
		sysuserMapper.updateByPrimaryKey(sysuser_update);

	}

	@Override
	public SysuserCustom findSysuserById(String id) throws Exception {

		// 从数据库查询用户
		Sysuser sysuser = sysuserMapper.selectByPrimaryKey(id);

		// 根据sysid查询单位名称
		String groupid = sysuser.getGroupid();
		String sysid = sysuser.getSysid();// 单位id
		String sysmc = null;
		if (groupid.equals("1") || groupid.equals("2")) {
			// 监督单位
			// 根据单位id查询单位信息
			Userjd userjd = userjdMapper.selectByPrimaryKey(sysid);
			if (userjd == null) {
				// 抛出异常，可预知异常
				ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE,
						217, null));
			}
			sysmc = userjd.getMc();
		} else if (groupid.equals("3")) {
			// 卫生室
			// 根据单位id查询单位信息
			Useryy useryy = useryyMapper.selectByPrimaryKey(sysid);
			if (useryy == null) {
				// 抛出异常，可预知异常
				ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE,
						217, null));
			}
			sysmc = useryy.getMc();
		} else if (groupid.equals("4")) {
			// 供货商
			// 根据单位id查询单位信息
			Usergys usergys = usergysMapper.selectByPrimaryKey(sysid);
			if (usergys == null) {
				// 抛出异常，可预知异常
				ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE,
						217, null));
			}
			sysmc = usergys.getMc();
		}

		SysuserCustom sysuserCustom = new SysuserCustom();

		// 将sysuser中数据设置到sysuserCustom
		BeanUtils.copyProperties(sysuser, sysuserCustom);

		sysuserCustom.setSysmc(sysmc);// 单位名称

		return sysuserCustom;
	}

	@Override
	public ActiveUser checkUserInfo(String userid, String pwd) throws Exception {
		// 校验用户是否存在
		Sysuser sysuser = this.findSysuserByUserid(userid);
		if (sysuser == null) {
			// 用户不存在
			ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE, 101,
					null));
		}

		// 校验用户密码是否合法
		String pwd_db = sysuser.getPwd();// md5密文
		String pwd_md5 = new MD5().getMD5ofStr(pwd);

		if (!pwd_db.equalsIgnoreCase(pwd_md5)) {
			// 用户名或密码错误
			ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE, 114,
					null));
		}

		// 构建用户身份信息
		ActiveUser activeUser = new ActiveUser();
		activeUser.setUserid(userid);
		activeUser.setUsername(sysuser.getUsername());
		activeUser.setGroupid(sysuser.getGroupid());
		activeUser.setSysid(sysuser.getSysid());// 单位id（重要）
		String sysmc = null;// 单位名称
		// 根据sysid查询单位名称
		String groupid = sysuser.getGroupid();
		String sysid = sysuser.getSysid();// 单位id
		if (groupid.equals("1") || groupid.equals("2")) {
			// 监督单位
			// 根据单位id查询单位信息
			Userjd userjd = userjdMapper.selectByPrimaryKey(sysid);
			if (userjd == null) {
				// 抛出异常，可预知异常
				ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE,
						217, null));
			}
			sysmc = userjd.getMc();
		} else if (groupid.equals("3")) {
			// 卫生室
			// 根据单位id查询单位信息
			Useryy useryy = useryyMapper.selectByPrimaryKey(sysid);
			if (useryy == null) {
				// 抛出异常，可预知异常
				ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE,
						217, null));
			}
			sysmc = useryy.getMc();
		} else if (groupid.equals("4")) {
			// 供货商
			// 根据单位id查询单位信息
			Usergys usergys = usergysMapper.selectByPrimaryKey(sysid);
			if (usergys == null) {
				// 抛出异常，可预知异常
				ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE,
						217, null));
			}
			sysmc = usergys.getMc();
		}

		activeUser.setSysmc(sysmc);// 单位名称

		return activeUser;
	}
}

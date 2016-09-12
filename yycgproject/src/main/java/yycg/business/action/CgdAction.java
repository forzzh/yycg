package yycg.business.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import yycg.base.pojo.po.Dictinfo;
import yycg.base.pojo.po.Useryy;
import yycg.base.pojo.vo.ActiveUser;
import yycg.base.pojo.vo.PageQuery;
import yycg.base.process.context.Config;
import yycg.base.process.result.DataGridResultInfo;
import yycg.base.process.result.ExceptionResultInfo;
import yycg.base.process.result.ResultInfo;
import yycg.base.process.result.ResultUtil;
import yycg.base.process.result.SubmitResultInfo;
import yycg.base.service.SystemConfigService;
import yycg.business.pojo.vo.GysypmlCustom;
import yycg.business.pojo.vo.GysypmlQueryVo;
import yycg.business.pojo.vo.YpxxCustom;
import yycg.business.pojo.vo.YycgdCustom;
import yycg.business.pojo.vo.YycgdQueryVo;
import yycg.business.pojo.vo.YycgdmxCustom;
import yycg.business.service.CgdService;
import yycg.util.MyUtil;

/**
 * 
 * <p>
 * Title: CgdAction
 * </p>
 * <p>
 * Description:采购单管理
 * </p>
 * <p>
 * Company: www.itcast.com
 * </p>
 * 
 * @author 苗润土
 * @date 2014年12月3日上午10:18:17
 * @version 1.0
 */
@Controller
@RequestMapping("/cgd")
public class CgdAction {

	@Autowired
	private CgdService cgdService;
	@Autowired
	private SystemConfigService systemConfigService;

	// 创建采购单基本信息页面
	@RequestMapping("/addcgd")
	public String addcgd(HttpSession session, Model model) throws Exception {

		ActiveUser activeUser = (ActiveUser) session
				.getAttribute(Config.ACTIVEUSER_KEY);
		// 用户所属单位名称(医院名称 )
		String sysmc = activeUser.getSysmc();

		// 准备页面需要的数据

		// 生成采购单名称：医院名称+当前时间+“采购单”
		String yycgdmc = sysmc + MyUtil.getDate() + "采购单";
		model.addAttribute("yycgdmc", yycgdmc);

		// 采购年份：当前年份
		String year = MyUtil.get_YYYY(MyUtil.getDate());
		model.addAttribute("year", year);

		return "/business/cgd/addcgd";

	}

	// 创建采购单基本信息保存方法
	@RequestMapping("/addcgdsubmit")
	public @ResponseBody
	SubmitResultInfo addcgdsubmit(HttpSession session, String year,
			YycgdQueryVo yycgdQueryVo) throws Exception {
		ActiveUser activeUser = (ActiveUser) session
				.getAttribute(Config.ACTIVEUSER_KEY);
		// 医院id
		String useryyid = activeUser.getSysid();
		// 获取采购单id
		String yycgdid = cgdService.insertYycgd(useryyid, year,
				yycgdQueryVo.getYycgdCustom());

		ResultInfo resultInfo = ResultUtil.createSuccess(Config.MESSAGE, 906,
				null);
		// 获取采购单id，将id通过ResultInfo中sysdata传到页面
		resultInfo.getSysdata().put("yycgdid", yycgdid);

		return ResultUtil.createSubmitResult(resultInfo);

	}

	// 采购单修改页面方法
	@RequestMapping("/editcgd")
	public String editcgd(Model model, String id) throws Exception {

		// 采购状态
		List<Dictinfo> cgztlist = systemConfigService.findDictinfoByType("011");
		List<Dictinfo> jyztlist = systemConfigService.findDictinfoByType("003");
		model.addAttribute("cgztlist", cgztlist);
		model.addAttribute("jyztlist", jyztlist);

		// 调用service获取采购单信息

		YycgdCustom yycgdCustom = cgdService.findYycgdById(id);
		model.addAttribute("yycgd", yycgdCustom);

		return "/business/cgd/editcgd";

	}

	// 采购单修改提交
	@RequestMapping("/editcgdsubmit")
	public @ResponseBody
	SubmitResultInfo editcgdsubmit(String id, YycgdQueryVo yycgdQueryVo)
			throws Exception {
		// 执行修改操作
		cgdService.updateYycgd(id, yycgdQueryVo.getYycgdCustom());

		return ResultUtil.createSubmitResult(ResultUtil.createSuccess(
				Config.MESSAGE, 906, null));
	}

	// 采购单药品明细查询结果集
	@RequestMapping("/queryYycgdmx_result")
	public @ResponseBody
	DataGridResultInfo queryYycgdmx_result(String id,// 采购单id
			YycgdQueryVo yycgdQueryVo, int page, int rows) throws Exception {

		// 查询数据总数
		int total = cgdService.findYycgdmxCountByYycgdid(id, yycgdQueryVo);

		// 分页参数
		PageQuery pageQuery = new PageQuery();
		pageQuery.setPageParams(total, rows, page);
		// 设置分页参数
		yycgdQueryVo.setPageQuery(pageQuery);

		// 分页查询
		List<YycgdmxCustom> list = cgdService.findYycgdmxListByYycgdid(id,
				yycgdQueryVo);

		DataGridResultInfo dataGridResultInfo = new DataGridResultInfo();
		dataGridResultInfo.setTotal(total);
		dataGridResultInfo.setRows(list);

		// dataGridResultInfo.setFooter(footer);
		return dataGridResultInfo;
	}

	// 采购药品添加查询页面
	@RequestMapping("/queryaddyycgdmx")
	public String queryaddyycgdmx(Model model, String yycgdid) throws Exception {
		// 药品类别
		List<Dictinfo> yplblist = systemConfigService.findDictinfoByType("001");
		model.addAttribute("yplblist", yplblist);
		// 将采购单id传到页面
		model.addAttribute("yycgdid", yycgdid);

		return "/business/cgd/queryaddyycgdmx";
	}

	// 采购药品添加查询列表结果集,json
	@RequestMapping("/queryaddyycgdmx_result")
	public @ResponseBody
	DataGridResultInfo queryaddyycgdmx_result(HttpSession session,
			String yycgdid,// 采购单id
			YycgdQueryVo yycgdQueryVo,// 查询条件
			int page, int rows) throws Exception {

		// 当前用户
		ActiveUser activeUser = (ActiveUser) session
				.getAttribute(Config.ACTIVEUSER_KEY);
		// 用户所属的单位(医院单位id)
		String useryyid = activeUser.getSysid();// 单位id

		// 列表的总数
		int total = cgdService.findAddYycgdmxCount(useryyid, yycgdid,
				yycgdQueryVo);

		// 分页参数
		PageQuery pageQuery = new PageQuery();
		pageQuery.setPageParams(total, rows, page);
		yycgdQueryVo.setPageQuery(pageQuery);// 设置分页参数

		// 分页查询列表
		List<YycgdmxCustom> list = cgdService.findAddYycgdmxList(useryyid,
				yycgdid, yycgdQueryVo);

		DataGridResultInfo dataGridResultInfo = new DataGridResultInfo();
		dataGridResultInfo.setTotal(total);
		dataGridResultInfo.setRows(list);

		return dataGridResultInfo;
	}

	// 采购单药品添加提交
	@RequestMapping("/addyycgdmxsubmit")
	public @ResponseBody
	SubmitResultInfo addyycgdmxsubmit(String yycgdid,// 采购单id
			YycgdQueryVo yycgdQueryVo, int[] indexs // 页面选择序号
	) throws Exception {

		// 页面提交的业务数据（多个），要处理的业务数据，页面中传入的参数
		List<YycgdmxCustom> list = yycgdQueryVo.getYycgdmxCustoms();

		// 处理数据的总数
		int count = indexs.length;
		// 处理成功的数量
		int count_success = 0;
		// 处理失败的数量
		int count_error = 0;

		// 处理失败的原因
		List<ResultInfo> msgs_error = new ArrayList<ResultInfo>();

		for (int i = 0; i < count; i++) {

			ResultInfo resultInfo = null;

			// 根据选中行的序号获取要处理的业务数据(单个)
			YycgdmxCustom yycgdmxCustom = list.get(indexs[i]);
			String ypxxid = yycgdmxCustom.getYpxxid();// 药品信息id
			String usergysid = yycgdmxCustom.getUsergysid();// 供货商id

			try {
				cgdService.insertYycgdmx(yycgdid, ypxxid, usergysid);
			} catch (Exception e) {
				e.printStackTrace();

				// 进行异常解析
				if (e instanceof ExceptionResultInfo) {
					resultInfo = ((ExceptionResultInfo) e).getResultInfo();
				} else {
					// 构造未知错误异常
					resultInfo = ResultUtil.createFail(Config.MESSAGE, 900,
							null);
				}

			}
			if (resultInfo == null) {
				// 说明成功
				count_success++;
			} else {
				count_error++;
				// 记录失败原因
				msgs_error.add(resultInfo);
			}

		}

		// 提示用户成功数量、失败数量、失败原因
		// 改成返回详细信息
		return ResultUtil.createSubmitResult(
				ResultUtil.createSuccess(Config.MESSAGE, 907, new Object[] {
						count_success, count_error }), msgs_error);
	}

	// 采购单药品保存
	@RequestMapping("/savecgl")
	public @ResponseBody
	SubmitResultInfo savecgl(String id,// 采购单id
			YycgdQueryVo yycgdQueryVo, int[] indexs // 页面选择序号
	) throws Exception {

		// 页面提交的业务数据（多个），要处理的业务数据，页面中传入的参数
		List<YycgdmxCustom> list = yycgdQueryVo.getYycgdmxCustoms();

		// 处理数据的总数
		int count = indexs.length;
		// 处理成功的数量
		int count_success = 0;
		// 处理失败的数量
		int count_error = 0;

		// 处理失败的原因
		List<ResultInfo> msgs_error = new ArrayList<ResultInfo>();

		for (int i = 0; i < count; i++) {

			ResultInfo resultInfo = null;

			// 根据选中行的序号获取要处理的业务数据(单个)
			YycgdmxCustom yycgdmxCustom = list.get(indexs[i]);
			String ypxxid = yycgdmxCustom.getYpxxid();// 药品信息id
			Integer cgl = yycgdmxCustom.getCgl();// 采购量

			try {
				cgdService.updateYycgdmx(id, ypxxid, cgl);
			} catch (Exception e) {
				e.printStackTrace();

				// 进行异常解析
				if (e instanceof ExceptionResultInfo) {
					resultInfo = ((ExceptionResultInfo) e).getResultInfo();
				} else {
					// 构造未知错误异常
					resultInfo = ResultUtil.createFail(Config.MESSAGE, 900,
							null);
				}

			}
			if (resultInfo == null) {
				// 说明成功
				count_success++;
			} else {
				count_error++;
				// 记录失败原因
				msgs_error.add(resultInfo);
			}

		}

		// 提示用户成功数量、失败数量、失败原因
		// 改成返回详细信息
		return ResultUtil.createSubmitResult(
				ResultUtil.createSuccess(Config.MESSAGE, 907, new Object[] {
						count_success, count_error }), msgs_error);
	}

	//采购单列表维护页面
	@RequestMapping("/yycgdlist")
	public String yycgdlist(Model model) throws Exception {
		// 药品类别
		List<Dictinfo> cgdztlist = systemConfigService.findDictinfoByType("010");
		model.addAttribute("cgdztlist", cgdztlist);
		
		//当前年份
		model.addAttribute("year", MyUtil.get_YYYY(MyUtil.getDate()));

		return "/business/cgd/yycgdlist";
	}

	// 采购单列表维护结果集,json
	@RequestMapping("/yycgdlist_result")
	public @ResponseBody
	DataGridResultInfo yycgdlist_result(//HttpSession session,
			ActiveUser activeUser,
			String year,//年份
			YycgdQueryVo yycgdQueryVo,// 查询条件
			int page, int rows) throws Exception {

		// 当前用户
		/*ActiveUser activeUser = (ActiveUser) session
				.getAttribute(Config.ACTIVEUSER_KEY);*/
		// 用户所属的单位(医院单位id)
		String useryyid = activeUser.getSysid();// 单位id

		// 列表的总数
		int total = cgdService.findYycgdCount(useryyid, year, yycgdQueryVo);

		// 分页参数
		PageQuery pageQuery = new PageQuery();
		pageQuery.setPageParams(total, rows, page);
		yycgdQueryVo.setPageQuery(pageQuery);// 设置分页参数

		// 分页查询列表
		List<YycgdCustom> list = cgdService.findYycgdList(useryyid, year, yycgdQueryVo);

		DataGridResultInfo dataGridResultInfo = new DataGridResultInfo();
		dataGridResultInfo.setTotal(total);
		dataGridResultInfo.setRows(list);

		return dataGridResultInfo;
	}

}

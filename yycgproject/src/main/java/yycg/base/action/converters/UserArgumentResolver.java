package yycg.base.action.converters;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;

import yycg.base.pojo.vo.ActiveUser;
import yycg.base.process.context.Config;

/**
 * 
 * <p>Title: UserArgumentResolver</p>
 * <p>Description:当前用户身份ActiveUser参数解析器 </p>
 * <p>Company: www.itcast.com</p> 
 * @author	苗润土
 * @date	2014-10-31上午11:33:00
 * @version 1.0
 */
public class UserArgumentResolver implements WebArgumentResolver {

	//methodParameter存储action方法的参数信息
	//webRequest是springmvc封装对象，通过此对象获取request和session等域的值
	@Override
	public Object resolveArgument(MethodParameter methodParameter,
			NativeWebRequest webRequest) throws Exception {
		
		//如果参数的类型是activeUser从session中取当前用户信息设置到action方法形参上
		if(methodParameter.getParameterType().equals(ActiveUser.class)){
			
			//从session中取当前用户信息
			ActiveUser activeUser = (ActiveUser) webRequest.getAttribute(Config.ACTIVEUSER_KEY, WebRequest.SCOPE_SESSION);
			
			return activeUser;//将参数值向action的方法形参赋值
		}
		
		return UNRESOLVED;
	}

}

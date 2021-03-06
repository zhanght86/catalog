package com.jusfoun.catalog.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

//import com.jusfoun.catalog.modules.sys.utils.UserUtils;
import com.google.common.collect.Maps;
import com.jusfoun.catalog.common.config.Global;
import com.jusfoun.catalog.common.controller.BaseController;
import com.jusfoun.catalog.common.security.shiro.session.SessionDAO;
//import com.jusfoun.catalog.common.servlet.ValidateCodeServlet;
import com.jusfoun.catalog.common.servlet.ValidateCodeServlet;
import com.jusfoun.catalog.common.tool.CacheTool;
import com.jusfoun.catalog.common.tool.CookieTool;
import com.jusfoun.catalog.common.tool.IdGenTool;
import com.jusfoun.catalog.common.tool.ServletTool;
import com.jusfoun.catalog.common.tool.StringTool;
//import com.jusfoun.catalog.modules.sys.security.FormAuthenticationFilter;
import com.jusfoun.catalog.security.FormAuthenticationFilter;
import com.jusfoun.catalog.security.SystemAuthorizingRealm.Principal;
import com.jusfoun.catalog.utils.LogUtils;
import com.jusfoun.catalog.utils.UserUtils;

/**
 * 登录Controller
 * @author Connor
 * @version 2015-12-18
 */
@Controller
public class LoginController extends BaseController{
	
	@Autowired
	private SessionDAO sessionDAO;
	
	/**
	 * 管理登录
	 */
	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
		Principal principal = UserUtils.getPrincipal();

		// 默认页签模式
		String tabmode = CookieTool.getCookie(request, "tabmode");
		if (tabmode == null){
			CookieTool.setCookie(response, "tabmode", "1");
		}
		
		if (logger.isDebugEnabled()){
			logger.debug("login, active session size: {}", sessionDAO.getActiveSessions(false).size());
		}
		
		// 如果已登录，再次访问主页，则退出原账号。
		if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))){
			CookieTool.setCookie(response, "LOGINED", "false");
		}
		
		// 如果已经登录，则跳转到管理首页
		if(principal != null && !principal.isMobileLogin()){
			return "redirect:" + adminPath;
		}
//		String view;
//		view = "/WEB-INF/views/modules/sys/sysLogin.jsp";
//		view = "classpath:";
//		view += "jar:file:/D:/GitHub/jeesite/src/main/webapp/WEB-INF/lib/jeesite.jar!";
//		view += "/"+getClass().getName().replaceAll("\\.", "/").replace(getClass().getSimpleName(), "")+"view/sysLogin";
//		view += ".jsp";
		return "/auth/login";
	}

	/**
	 * 登录失败，真正登录的POST请求由Filter完成
	 */
	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.POST)
	public String loginFail(HttpServletRequest request, HttpServletResponse response, Model model) {
		Principal principal = UserUtils.getPrincipal();
		
		// 如果已经登录，则跳转到管理首页
		if(principal != null){
			return "redirect:" + adminPath;
		}

		String username = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
		boolean rememberMe = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM);
		boolean mobile = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_MOBILE_PARAM);
		String exception = (String)request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		String message = (String)request.getAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM);


		if (StringTool.isBlank(message) || StringTool.equals(message, "null")){
			message = "用户或密码错误, 请重试.";
		}

		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM, rememberMe);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_MOBILE_PARAM, mobile);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, exception);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM, message);

		if (logger.isDebugEnabled()){
			logger.debug("login fail, active session size: {}, message: {}, exception: {}",
					sessionDAO.getActiveSessions(false).size(), message, exception);
		}

		// 非授权异常，登录失败，验证码加1。
		if (!UnauthorizedException.class.getName().equals(exception)){
			model.addAttribute("isValidateCodeLogin", isValidateCodeLogin(username, true, false));
		}

		// 验证失败清空验证码
		request.getSession().setAttribute(ValidateCodeServlet.VALIDATE_CODE, IdGenTool.uuid());

		// 如果是手机登录，则返回JSON字符串
//		if (mobile){
//	        return renderString(response, model);
//		}
		
		return "/auth/login";
	}

	/**
	 * 登录成功，进入管理首页
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "${adminPath}")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
		Principal principal = UserUtils.getPrincipal();

		// 登录成功后，验证码计算器清零
		isValidateCodeLogin(principal.getLoginName(), false, true);

		if (logger.isDebugEnabled()){
			logger.debug("show index, active session size: {}", sessionDAO.getActiveSessions(false).size());
		}

		// 如果已登录，再次访问主页，则退出原账号。
		if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))){
			String logined = CookieTool.getCookie(request, "LOGINED");
			if (StringTool.isBlank(logined) || "false".equals(logined)){
				CookieTool.setCookie(response, "LOGINED", "true");
			}else if (StringTool.equals(logined, "true")){
				UserUtils.getSubject().logout();
				return new ModelAndView("redirect:" + adminPath + "/login");
			}
		}

		// 如果是手机登录，则返回JSON字符串
//		if (principal.isMobileLogin()){
//			if (request.getParameter("login") != null){
//				return renderString(response, principal);
//			}
//			if (request.getParameter("index") != null){
//				return "modules/sys/sysIndex";
//			}
//			return "redirect:" + adminPath + "/login";
//		}
		
//		// 登录成功后，获取上次登录的当前站点ID
//		UserUtils.putCache("siteId", StringTool.toLong(CookieTool.getCookie(request, "siteId")));

//		System.out.println("==========================a");
//		try {
//			byte[] bytes = com.thinkgem.jeesite.common.utils.FileTool.readFileToByteArray(
//					com.thinkgem.jeesite.common.utils.FileTool.getFile("c:\\sxt.dmp"));
//			UserUtils.getSession().setAttribute("kkk", bytes);
//			UserUtils.getSession().setAttribute("kkk2", bytes);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
////		for (int i=0; i<1000000; i++){
////			//UserUtils.getSession().setAttribute("a", "a");
////			request.getSession().setAttribute("aaa", "aa");
////		}
//		System.out.println("==========================b");
//		ModelAndView view = new ModelAndView("admin/dashboard");
//		return view;
		
    	if(UserUtils.getUser().isAdmin()){
    		// 系统管理员
    		return new ModelAndView("redirect:/admin/statistic/info");
    	}else{
    		// 部门管理员
    		return new ModelAndView("redirect:/admin/register/applyIndex");
    	}
	}
	
	/**
	 * 退出系统
	 */
	@RequestMapping(value = "${adminPath}/logout")
	public ModelAndView logout() {
		LogUtils.saveLog(ServletTool.getRequest(), "退出系统");
		UserUtils.getSubject().logout();
		return new ModelAndView("redirect:/admin/login");
	}
	
	/**
	 * 获取主题方案
	 */
	@RequestMapping(value = "/theme/{theme}")
	public String getThemeInCookie(@PathVariable String theme, HttpServletRequest request, HttpServletResponse response){
		if (StringTool.isNotBlank(theme)){
			CookieTool.setCookie(response, "theme", theme);
		}else{
			theme = CookieTool.getCookie(request, "theme");
		}
		return "redirect:"+request.getParameter("url");
	}
	
	/**
	 * 是否是验证码登录
	 * @param useruame 用户名
	 * @param isFail 计数加1
	 * @param clean 计数清零
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isValidateCodeLogin(String useruame, boolean isFail, boolean clean){
		Map<String, Integer> loginFailMap = (Map<String, Integer>) CacheTool.get("loginFailMap");
		if (loginFailMap==null){
			loginFailMap = Maps.newHashMap();
			CacheTool.put("loginFailMap", loginFailMap);
		}
		Integer loginFailNum = loginFailMap.get(useruame);
		if (loginFailNum==null){
			loginFailNum = 0;
		}
		if (isFail){
			loginFailNum++;
			loginFailMap.put(useruame, loginFailNum);
		}
		if (clean){
			loginFailMap.remove(useruame);
		}
		return loginFailNum >= 1;
	}
}

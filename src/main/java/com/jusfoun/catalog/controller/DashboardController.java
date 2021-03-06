package com.jusfoun.catalog.controller;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jusfoun.catalog.common.controller.BaseController;
import com.jusfoun.catalog.common.mapper.JsonMapper;
import com.jusfoun.catalog.entity.Business;
import com.jusfoun.catalog.entity.Job;
import com.jusfoun.catalog.entity.Office;
import com.jusfoun.catalog.entity.ResourceInfo;
import com.jusfoun.catalog.entity.SearchLog;
import com.jusfoun.catalog.entity.SubjectInfo;
import com.jusfoun.catalog.service.BusinessService;
import com.jusfoun.catalog.service.JobService;
import com.jusfoun.catalog.service.OfficeService;
import com.jusfoun.catalog.service.ResourceService;
import com.jusfoun.catalog.service.SearchLogService;
import com.jusfoun.catalog.service.SubjectService;

/**
 * Created by huanglei on 15/12/27.
 */
@Controller
public class DashboardController extends BaseController{

	@Resource
	private OfficeService officeService;
	@Resource
	private SearchLogService searchLogService;
//    @Autowired
//    private IUserService userService;
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private BusinessService businessService;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private SubjectService subjectService;


    @RequestMapping(value = "${adminPath}/dashboard", method = RequestMethod.GET)
    public String loadAdminHomePage(Model m) {
        return "admin/dashboard";
    }


    @RequestMapping(value = "/")
    public String loadFrontHomePage() {
        return "index";
    }

    @RequestMapping(value = "/upgrade-your-browser")
    public String upgradeYourBrowser() {
        return "upgrade-your-browser";
    }

    @RequestMapping(value = "/upgrade-your-browser",method = RequestMethod.POST)
    public String login(){
        return "";
    }
    
    @ResponseBody
    @RequestMapping(value = "/search")
	public String search(@RequestParam(value = "search_value", required=true) String value,
			@RequestParam(value = "search_type", required=true) int type,
			@RequestParam(value = "officeId", required=false) Integer officeId) {
    	String ofclStr="{}";
//    	if(!StringUtils.isEmpty(value)){
    		SearchLog log = new SearchLog();
    		if(value==null||"".equals(value)){
    			log.setKeyword("nil");
    		}else{
    			log.setKeyword(value);
    		}
    		
    		log.setSrhTime(new Date());
    		log.setSrhType(type);
    		if(officeId!=null&&!"".equals(officeId)){
    			log.setOfficeId(officeId);
    		}
    		searchLogService.save(log);
    		
    		Map<String,Object> param = new HashMap<String, Object>();
    		param.put("duty", value.trim());
    		if(officeId!=null){
    			param.put("id", officeId);
    		}
    		List<Office> officeList = officeService.queryOffices(param);
    		for(Office office : officeList){
    			office.setDuty(StringEscapeUtils.unescapeHtml4(office.getDuty().replaceAll(value.trim(), "&lt;span style='color:orange'&gt;"+value.trim()+"&lt;/span&gt;")));
    		}
    		ofclStr=JsonMapper.toJsonString(officeList);
//    	}
		return ofclStr;
	}
    
    @ResponseBody
    @RequestMapping(value = "/srh/jobDg")
    public String srhJobDg(int page,int rows,HttpServletRequest request){
    	String name=request.getParameter("name")==null?null:StringUtils.trim(request.getParameter("name"));
    	String officeId=request.getParameter("officeId")==null?null:StringUtils.trim(request.getParameter("officeId"));
    	String type=StringUtils.trim(request.getParameter("search_type"));
    	
    	String retVal="{\"total\":0,\"rows\":[]}";
//    	if(!StringUtils.isBlank(name)){
    		SearchLog log = new SearchLog();
    		if(name==null||"".equals(name)){
    			log.setKeyword("nil");
    		}else{
    			log.setKeyword(name);
    		}
    		log.setSrhTime(new Date());
    		log.setSrhType(Integer.valueOf(type));
    		if(officeId!=null&&!"".equals(officeId)){
    			log.setOfficeId(Integer.valueOf(officeId));
    		}
    		searchLogService.save(log);
    		
    		//求得开始记录与结束记录
    		int start = (page-1)*rows+1;
    		int end = page * rows;
    		Job job=new Job();
    		job.setName(name);
    		if(StringUtils.isNotEmpty(officeId)){
    			job.setOfficeId(Integer.valueOf(officeId));
    		}
    		job.getSqlMap().put("start", ""+start);
    		job.getSqlMap().put("end", ""+end);
    		int jobCount=jobService.findSrhListCount(job);
    		List<Job> jobList=jobService.findSrhList(job);
    		String json = JsonMapper.toJsonString(jobList);
    		StringBuffer sb=new StringBuffer();
    		sb.append("{\"total\":").append(jobCount).append(",\"rows\":").append(json).append("}");
    		retVal=sb.toString();
//    	}
    	return retVal;
    }
    
    @ResponseBody
    @RequestMapping(value = "/srh/bizDg")
    public String srhBizDg(int page,int rows,HttpServletRequest request){
    	String officeId=request.getParameter("officeId")==null?null:StringUtils.trim(request.getParameter("officeId"));
    	String type=StringUtils.trim(request.getParameter("search_type"));
    	String name=request.getParameter("name")==null?null:StringUtils.trim(request.getParameter("name"));
    	
    	String retVal="{\"total\":0,\"rows\":[]}";
//    	if(!StringUtils.isBlank(name)){
    		SearchLog log = new SearchLog();
    		if(name==null||"".equals(name)){
    			log.setKeyword("nil");
    		}else{
    			log.setKeyword(name);
    		}
    		log.setSrhTime(new Date());
    		log.setSrhType(Integer.valueOf(type));
    		if(officeId!=null&&!"".equals(officeId)){
    			log.setOfficeId(Integer.valueOf(officeId));
    		}
    		searchLogService.save(log);
    		
    		//求得开始记录与结束记录
    		int start = (page-1)*rows+1;
    		int end = page * rows;
    		Business biz=new Business();
    		if(StringUtils.isNotEmpty(officeId)){
    			biz.setChargeOfficeId(Long.valueOf(officeId));
    		}
    		biz.setName(name);
    		biz.getSqlMap().put("start", ""+start);
    		biz.getSqlMap().put("end", ""+end);
    		int bizCount=businessService.findSrhListCount(biz);
    		List<Business> bizList=businessService.findSrhList(biz);
    		String json = JsonMapper.toJsonString(bizList);
    		StringBuffer sb=new StringBuffer();
    		sb.append("{\"total\":").append(bizCount).append(",\"rows\":").append(json).append("}");
    		retVal=sb.toString();
//    	}
    	return retVal;
    }
    
    @ResponseBody
    @RequestMapping(value = "/srh/rscDg")
    public String srhRscDg(int page,int rows,HttpServletRequest request){
    	String officeId=request.getParameter("officeId")==null?null:StringUtils.trim(request.getParameter("officeId"));
    	String type=StringUtils.trim(request.getParameter("search_type"));
    	String name=request.getParameter("name")==null?null:StringUtils.trim(request.getParameter("name"));
    	String retVal="{\"total\":0,\"rows\":[]}";
    	//if(StringUtils.isNotBlank(name)){
    		SearchLog log = new SearchLog();
    		if(name==null||"".equals(name)){
    			log.setKeyword("nil");
    		}else{
    			log.setKeyword(name);
    		}
    		log.setSrhTime(new Date());
    		log.setSrhType(Integer.valueOf(type));
    		if(officeId!=null&&!"".equals(officeId)){
    			log.setOfficeId(Integer.valueOf(officeId));
    		}
    		searchLogService.save(log);
    		
    		//求得开始记录与结束记录
    		int start = (page-1)*rows+1;
    		int end = page * rows;
    		ResourceInfo rsc=new ResourceInfo();
    		if(StringUtils.isNotEmpty(officeId)){
    			rsc.setResponseParty(officeId);
    		}
    		rsc.setName(name);
    		rsc.getSqlMap().put("start", ""+start);
    		rsc.getSqlMap().put("end", ""+end);
    		List<ResourceInfo> rscList=resourceService.findSrhList(rsc);
    		int rscCount=resourceService.findSrhListCount(rsc);
    		String json = JsonMapper.toJsonString(rscList);
    		StringBuffer sb=new StringBuffer();
    		sb.append("{\"total\":").append(rscCount).append(",\"rows\":").append(json).append("}");
    		retVal=sb.toString();
    	//}
    	return retVal;
    }
    
    @ResponseBody
    @RequestMapping(value = "/srh/subDg")
    public String srhSubDg(int page,int rows,HttpServletRequest request){
    	String type=StringUtils.trim(request.getParameter("search_type"));
    	String name=request.getParameter("name")==null?null:StringUtils.trim(request.getParameter("name"));
    	String retVal="{\"total\":0,\"rows\":[]}";
//    	if(!StringUtils.isBlank(name)){
    		SearchLog log = new SearchLog();
    		if(name==null||"".equals(name)){
    			log.setKeyword("nil");
    		}else{
    			log.setKeyword(name);
    		}
    		log.setSrhTime(new Date());
    		log.setSrhType(Integer.valueOf(type));
    		searchLogService.save(log);
    		
    		//求得开始记录与结束记录
    		int start = (page-1)*rows+1;
    		int end = page * rows;
    		SubjectInfo sub=new SubjectInfo();
    		sub.setName(name);
    		sub.getSqlMap().put("start", ""+start);
    		sub.getSqlMap().put("end", ""+end);
    		List<ResourceInfo> rscList=subjectService.findSrhList(sub);
    		int rscCount=subjectService.findSrhListCount(sub);
    		String json = JsonMapper.toJsonString(rscList);
    		StringBuffer sb=new StringBuffer();
    		sb.append("{\"total\":").append(rscCount).append(",\"rows\":").append(json).append("}");
    		retVal=sb.toString();
//    	}
    	return retVal;
    }
}
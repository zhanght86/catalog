package com.jusfoun.catalog.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jusfoun.catalog.common.service.CrudService;
import com.jusfoun.catalog.dao.BusinessDao;
import com.jusfoun.catalog.entity.Business;

@Service
@Transactional(readOnly = true)
public class BusinessService extends CrudService<BusinessDao, Business>{
	
	/**
	 * 更新业务
	 * @param biz
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean updateBiz(Business biz){
		int num=dao.update(biz);
		if(num>0){
			return true;
		}
		return false;
	}

	/**根据机构d查询对应的业务
	 * @param officeId 机构id
	 * @return
	 */
	public List<Business> findBusinessByOfficeId(Integer officeId) {
		return dao.findBusinessByOfficeId(officeId);
	}

	/**
	 * 查询列表中的个数
	 * @param biz
	 * @return
	 */
	public int findListCount(Business biz) {
		return dao.findListCount(biz);
	}

	public int findListCountBySubId(Map<String, Object> sqlMap){
		return dao.findListCountBySubId(sqlMap);
	}
	
	public List<Business> findListBySubId(Map<String, Object> sqlMap){
		return dao.findListBySubId(sqlMap);
	}
}

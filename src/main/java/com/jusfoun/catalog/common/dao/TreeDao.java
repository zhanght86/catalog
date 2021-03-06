package com.jusfoun.catalog.common.dao;

import com.jusfoun.catalog.common.entity.TreeEntity;

import java.util.List;

/**
 * DAO支持类实现
 * @author Connor
 * @version 2015-12-18
 * @param <T>
 */
public interface TreeDao<T extends TreeEntity<T>> extends CrudDao<T> {

	/**
	 * 找到所有子节点
	 * @param entity
	 * @return
	 */
	public List<T> findByParentIdsLike(T entity);

	/**
	 * 更新所有父节点字段
	 * @param entity
	 * @return
	 */
	public int updateParentIds(T entity);
	
}
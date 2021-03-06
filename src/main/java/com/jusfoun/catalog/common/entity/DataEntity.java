package com.jusfoun.catalog.common.entity;

import com.jusfoun.catalog.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jusfoun.catalog.utils.UserUtils;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 数据Entity类
 * @author Connor
 * @version 2015-12-18
 */
public abstract class DataEntity<T> extends BaseEntity<T> {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 未申请注册
	 */
	public static final String STATUS_UNAPPLY = "3";
	/**
	 * 申请注册中
	 */
	public static final String STATUS_APPLYING= "4";
	/**
	 * 已注册
	 */
	public static final String STATUS_APPLYED = "5";
	/**
	 * 申请注销中
	 */
	public static final String STATUS_CANCELLING = "6";
	/**
	 * 已注销
	 */
	public static final String STATUS_CANCELLED = "7";
	
	protected String remarks;	// 备注
	protected User createBy;	// 创建者
	protected Date createDate;	// 创建日期
	protected User updateBy;	// 更新者
	protected Date updateDate;	// 更新日期
	protected String delFlag; 	// 删除标记（0：启用状态；1：废止状态；2：删除(注销)状态；）
	
	public DataEntity() {
		super();
		this.delFlag = DEL_FLAG_NORMAL;
	}
	
	public DataEntity(Integer id) {
		super(id);
	}
	
	/**
	 * 插入之前执行方法，需要手动调用
	 */
	@Override
	public void preInsert(){
		User user = UserUtils.getUser();
		if (user.getId() != null && user.getId() != 0){
			this.updateBy = user;
			this.createBy = user;
		}
		this.updateDate = new Date();
		this.createDate = this.updateDate;
	}
	
	/**
	 * 更新之前执行方法，需要手动调用
	 */
	@Override
	public void preUpdate(){
		User user = UserUtils.getUser();

		if (user.getId() != null && user.getId() != 0){
			this.updateBy = user;
		}
		this.updateDate = new Date();
	}
	
	@Length(min=0, max=255)
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	@JsonIgnore
	public User getCreateBy() {
		return createBy;
	}

	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@JsonIgnore
	public User getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(User updateBy) {
		this.updateBy = updateBy;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@JsonIgnore
	@Length(min=1, max=1)
	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

}

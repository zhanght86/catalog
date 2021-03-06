package com.jusfoun.catalog.entity;

import com.jusfoun.catalog.common.entity.DataEntity;

/**
 * 资源Entity
 * @author liudebin
 *
 */
public class ResourceInfo extends DataEntity<ResourceInfo>{
   
	private static final long serialVersionUID = 1L;
	
	private String name;//资源名称
    private String ofBusiness;//所属业务
    private Integer type;//资源类型
    private String code;//资源编码
    private String responseParty;//资源责任方
    private Integer responseAttr;//责任属性
    private String responseCu;//责任方联系方式
    private Integer collectWay;//收集方式
    private Integer securityLevel;//资源安全级别
    private String infoField;//信息字段
    private Integer isDbSupport;//是否数据库支撑
    private String shareRegion;//共享范围
    private String shareMode;//共享方式
    private String updateCycle;//更新周期
    private String descr;//资源描述
    private String status;//资源状态
    private Integer subjectId;//所属主题

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOfBusiness() {
        return ofBusiness;
    }

    public void setOfBusiness(String ofBusiness) {
        this.ofBusiness = ofBusiness;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getResponseParty() {
        return responseParty;
    }

    public void setResponseParty(String responseParty) {
        this.responseParty = responseParty;
    }

    public Integer getResponseAttr() {
        return responseAttr;
    }

    public void setResponseAttr(Integer responseAttr) {
        this.responseAttr = responseAttr;
    }

    public String getResponseCu() {
        return responseCu;
    }

    public void setResponseCu(String responseCu) {
        this.responseCu = responseCu;
    }

    public Integer getCollectWay() {
        return collectWay;
    }

    public void setCollectWay(Integer collectWay) {
        this.collectWay = collectWay;
    }

    public Integer getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(Integer securityLevel) {
        this.securityLevel = securityLevel;
    }

    public String getInfoField() {
        return infoField;
    }

    public void setInfoField(String infoField) {
        this.infoField = infoField;
    }

    public Integer getIsDbSupport() {
        return isDbSupport;
    }

    public void setIsDbSupport(Integer isDbSupport) {
        this.isDbSupport = isDbSupport;
    }

    public String getShareRegion() {
        return shareRegion;
    }

    public void setShareRegion(String shareRegion) {
        this.shareRegion = shareRegion;
    }

    public String getShareMode() {
        return shareMode;
    }

    public void setShareMode(String shareMode) {
        this.shareMode = shareMode;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

	public String getUpdateCycle() {
		return updateCycle;
	}

	public void setUpdateCycle(String updateCycle) {
		this.updateCycle = updateCycle;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}
    
}
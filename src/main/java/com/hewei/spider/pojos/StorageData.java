package com.hewei.spider.pojos;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * 
 * @author hewei
 * 
 * @date 2015/6/15  0:22
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class StorageData {

	private String url;

	private String originalHtml;

	private String searchText;

	private String name;

	private String desc;

	private String experience;

    @JSONField(serialize = false)
    private boolean errorPage;

    private long id;

    private Date createTime;

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOriginalHtml() {
		return originalHtml;
	}

	public void setOriginalHtml(String originalHtml) {
		this.originalHtml = originalHtml;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isErrorPage() {
		return errorPage;
	}

	public void setErrorPage(boolean errorPage) {
		this.errorPage = errorPage;
	}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
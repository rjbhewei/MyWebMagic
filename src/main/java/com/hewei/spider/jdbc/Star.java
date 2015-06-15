package com.hewei.spider.jdbc;

import java.util.Date;

/**
 * @author hewei
 * @version 5.0
 * @date 2015/6/15  14:14
 * @desc
 */
public class Star {

    private long id;

    private String name;

    private String url;

    private Date createTime;

    public Star() {
    }

    public Star(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

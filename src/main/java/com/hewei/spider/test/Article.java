package com.hewei.spider.test;

import java.util.Date;

/**
 * 
 * @author hewei
 * 
 * @date 2015/6/9  16:02
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class Article {

    private int id;

    private Date createDate;

    private String description;

    public Article(){}

    public Article(int id, Date createDate, String description) {
        this.id = id;
        this.createDate = createDate;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

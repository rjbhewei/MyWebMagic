package com.hewei.spider.jdbc;

/**
 * @author hewei
 * @version 5.0
 * @date 2015/6/15  14:14
 * @desc
 */
public class Star {

    private String name;

    private String url;

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
}

package com.hewei.oschina.pojos;
/**
 * 
 * @author hewei
 * 
 * @date 2015/9/9  20:16
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class OsChinaActivity implements OsChinaPojo{
    private String url;
    private String picture;
    private String title;
    private String desc;
    private String time;
    private String location;

    public OsChinaActivity(String url, String picture, String title, String desc, String time, String location) {
        this.url = url;
        this.picture = picture;
        this.title = title;
        this.desc = desc;
        this.time = time;
        this.location = location;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

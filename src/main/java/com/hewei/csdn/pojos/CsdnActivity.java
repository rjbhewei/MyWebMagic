package com.hewei.csdn.pojos;

import com.hewei.common.pojos.BaseEsPojo;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/10  18:45
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class CsdnActivity implements BaseEsPojo {

    private String dayTime;

    private String addr;

    private String url;

    private String title;

    private String expense;

    private String specificTime;

    private String location;

    public CsdnActivity(String dayTime, String addr, String url, String title, String expense, String specificTime, String location) {
        this.dayTime = dayTime;
        this.addr = addr;
        this.url = url;
        this.title = title;
        this.expense = expense;
        this.specificTime = specificTime;
        this.location = location;
    }

    public String getDayTime() {
        return dayTime;
    }

    public String getAddr() {
        return addr;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getExpense() {
        return expense;
    }

    public String getSpecificTime() {
        return specificTime;
    }

    public String getLocation() {
        return location;
    }
}

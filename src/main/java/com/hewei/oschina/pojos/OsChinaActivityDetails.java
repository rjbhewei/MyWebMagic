package com.hewei.oschina.pojos;
/**
 * 
 * @author hewei
 * 
 * @date 2015/9/9  21:04
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class OsChinaActivityDetails {
    private String title;
    private String time;//时间
    private String location;//地址
    private String expense;//费用
    private String type;//类型
    private String initiator;//发起人

    private String desc;

    public OsChinaActivityDetails(String title, String time, String location, String expense, String type, String initiator, String desc) {
        this.title = title;
        this.time = time;
        this.location = location;
        this.expense = expense;
        this.type = type;
        this.initiator = initiator;
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
